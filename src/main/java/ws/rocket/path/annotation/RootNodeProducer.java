// @formatter:off
/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// @formatter:on

package ws.rocket.path.annotation;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import ws.rocket.path.DynamicKey;
import ws.rocket.path.TreeNode;

/**
 * Producer for root {@link TreeNode}s according to CDI (Contexts And Dependency Injection) standard.
 * <p>
 * A field or method wishing for a tree to be constructed, must use {@link Inject} and {@link RootNode} annotations
 * where tree injection must be done. This producer uses meta information from annotations for resolving the key and
 * value objects with possible child <code>TreeNode</code>s, and finally returns the root node of the initialized tree.
 * Annotations affecting the construction are <code>RootNode</code> (on the field/method being injected), which points
 * to the value object of the root node, and {@link ws.rocket.path.annotation.TreeNode} (on node value objects), which
 * may describe the current node key, and value objects of child-nodes.
 * <p>
 * The algorithm follows:
 * <ol>
 * <li>This producer is given an injection point annotated with <code>RootNode</code> and of type <code>TreeNode</code>.
 * <li>The tree node value object of the currently examined node is first looked for by value bean type and then, if
 * that failed, by value bean name (from the annotation).
 * <li>A reference to the value object is acquired.
 * <li>If the value object class is annotated with <code>TreeNode</code>:
 * <ul>
 * <li>if the annotation contains info about the node key (name or type), the key is looked for and used on the node
 * being created.
 * <li>if the annotation contains info about the child nodes (their value objects by name or type), the child nodes are
 * created as described in steps 2-6 using the meta information from the annotation.
 * </ul>
 * <li>Otherwise, the node receives the bean name (a string) and is left without child nodes.
 * <li>The tree node is created and returned.
 * </ol>
 * <p>
 * As can been seen from the algorithm, the tree is created starting from the leaves of the nodes, although the meta
 * information is read starting from the root of the tree. In addition, thanks to CDI, all associated node key and value
 * objects get their annotated dependencies injected.
 * <p>
 * Note: when node value object is missing <code>TreeNode</code> and {@link javax.inject.Named} annotations, the node
 * key would default to <code>null</code> as the bean name is <code>null</code>. However, currently the key value is
 * manually composed just as it would be done with the <code>Named</code> annotation. This fact is explicitly brought
 * out here to avoid surprises, but this behaviour can also be altered in the future.
 * 
 * @author Martti Tamm
 */
public final class RootNodeProducer {

  @Inject
  private BeanManager manager;

  /**
   * Produces a tree where root {@link TreeNode} has the value object with the same name and/or type as provided in the
   * {@link RootNode} annotation.
   * 
   * @param injectionPoint The point where the tree is to be injected.
   * @return The created tree.
   */
  @Produces
  @RootNode
  public TreeNode createTree(InjectionPoint injectionPoint) {
    RootNode rootAnnotation = null;

    for (Annotation annotation : injectionPoint.getQualifiers()) {
      if (annotation instanceof RootNode) {
        rootAnnotation = (RootNode) annotation;
      }
    }

    if (rootAnnotation == null) {
      return findTreeNode(injectionPoint.getMember().getName());
    } else if (rootAnnotation.type() != null && rootAnnotation.type() != Object.class) {
      return findTreeNode(rootAnnotation.type());
    } else if (rootAnnotation.value() != null && rootAnnotation.value().trim().length() > 0) {
      return findTreeNode(rootAnnotation.value());
    }

    throw new RuntimeException("Could not resolve the value object for the root node of the tree "
        + "specified with annotation: " + rootAnnotation);
  }

  private TreeNode findTreeNode(String beanName) {
    Set<Bean<?>> beans = this.manager.getBeans(beanName);
    if (beans.isEmpty()) {
      throw new RuntimeException("Could not find TreeNode value by name '" + beanName + "'.");
    } else if (beans.size() > 1) {
      throw new RuntimeException("Currently only one TreeNode value is expected. Name: '" + beanName + "'.");
    }

    return createTreeNode(beans.iterator().next());
  }

  private TreeNode findTreeNode(Class<?> beanType) {
    Set<Bean<?>> beans = this.manager.getBeans(beanType);
    if (beans.isEmpty()) {
      throw new RuntimeException("Could not find TreeNode value by type: " + beanType);
    } else if (beans.size() > 1) {
      throw new RuntimeException("Currently only one TreeNode value is expected to exist. Type: " + beanType);
    }

    return createTreeNode(beans.iterator().next());
  }

  private TreeNode createTreeNode(Bean<?> bean) {
    Object value = this.manager.getReference(bean, bean.getBeanClass(), this.manager.createCreationalContext(bean));

    ws.rocket.path.annotation.TreeNode meta = bean.getBeanClass().getAnnotation(
        ws.rocket.path.annotation.TreeNode.class);

    return new TreeNode(resolveKey(bean, value, meta), value, resolveChildren(bean, meta));
  }

  private Object resolveKey(Bean<?> valueBean, Object value, ws.rocket.path.annotation.TreeNode meta) {
    Object key = null;

    if (value instanceof KeyBuilder) {
      key = ((KeyBuilder) value).buildKey();
      if (key != null) {
        injectDependencies(key);
      }

    } else if (meta != null) {
      Set<Bean<?>> beans = null;

      if (meta.key().trim().length() > 0) {
        key = meta.key();
      } else if (meta.keyType() != Object.class) {
        if (!DynamicKey.class.isAssignableFrom(meta.keyType())) {
          throw new RuntimeException("TreeNode key, if not String, must implement DynamicKey. Problem detected at "
              + valueBean);
        }

        beans = this.manager.getBeans(meta.keyType());

      } else if (meta.keyName().trim().length() > 0) {
        beans = this.manager.getBeans(meta.keyName());
      }

      if (beans != null) {
        if (beans.isEmpty()) {
          throw new RuntimeException("Could not find TreeNode key for annotation " + meta + " at " + valueBean);

        } else if (beans.size() > 1) {
          throw new RuntimeException("Found too many (" + beans.size() + ") TreeNode key candidates for annotation "
              + meta + " at " + valueBean);

        } else {
          key = beans.iterator().next();
        }
      }
    }

    return key;
  }

  private TreeNode[] resolveChildren(Bean<?> valueBean, ws.rocket.path.annotation.TreeNode meta) {

    if (meta == null || meta.children().length == 0 && meta.childTypes().length == 0) {
      return null;
    } else if (meta.children().length > 0 && meta.childTypes().length > 0) {
      throw new RuntimeException("Child-TreeNode values are identified with both names and types; expected only one "
          + "to be provided (preferably types).");
    }

    TreeNode[] childNodes;

    if (meta.childTypes().length > 0) {
      childNodes = new TreeNode[meta.childTypes().length];
      int i = 0;

      for (Class<?> childType : meta.childTypes()) {
        childNodes[i++] = findTreeNode(childType);
      }
    } else {
      childNodes = new TreeNode[meta.children().length];
      int i = 0;

      for (String childName : meta.children()) {
        childNodes[i++] = findTreeNode(childName);
      }
    }

    return childNodes;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void injectDependencies(Object key) {
    AnnotatedType type = this.manager.createAnnotatedType(key.getClass());
    InjectionTarget target = this.manager.createInjectionTarget(type);
    CreationalContext creationalContext = this.manager.createCreationalContext(null);
    target.inject(key, creationalContext);
    creationalContext.release();
  }
}
