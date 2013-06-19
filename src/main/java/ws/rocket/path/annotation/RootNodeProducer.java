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
import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import ws.rocket.path.TreeNode;
import ws.rocket.path.builder.TreeNodeBuilder;
import ws.rocket.path.builder.TreeNodeBuilderAware;

/**
 * CDI (Contexts And Dependency Injection) producer creating root {@link TreeNode}s.
 * <p>
 * A field or method wishing for a tree to be constructed, must use {@link Inject} and {@link RootNode} annotations
 * where tree injection must be done. For example:
 *
 * <pre>
 * &#064;Inject
 * &#064;RootNode
 * private TreeNode hierarchy;
 * </pre>
 * <p>
 * This producer uses meta information from the <code>&#064;RootNode</code> annotation for looking up the value object
 * of the root node. After that, the producer uses {@link ws.rocket.path.annotation.TreeNode} annotation on the value
 * objects for determining the key value and child-nodes of the current node. When a value object implements
 * {@link TreeNodeBuilder} contract, its method will be used for creating child-nodes instead of the references in
 * <code>&#064;TreeNode</code> annotation.
 * <p>
 * The algorithm follows:
 * <ol>
 * <li>This producer is given an injection point with <code>&#064;RootNode</code> annotation and of type
 * <code>TreeNode</code>.
 * <li>The tree node value object is looked up (first successful lookup will become the value object):
 * <ul>
 * <li>by CDI bean type (from the <code>&#064;RootNode</code> or <code>&#064;TreeNode</code> annotation when present);
 * <li>by CDI bean name (from the <code>&#064;RootNode</code> or <code>&#064;TreeNode</code> annotation when present);
 * <li>if the injection point is class field, by CDI bean name (using field name).
 * </ul>
 * <li>The key for the tree node is resolved and created (first successful result will become the key object):
 * <ul>
 * <li>if value object implements {@link KeyBuilder}, the value returned by its method will be used for the key;
 * <li>CDI bean lookup by type (from the <code>&#064;TreeNode</code> annotation when present);
 * <li>CDI bean lookup by name (from the <code>&#064;TreeNode</code> annotation when present);
 * <li>default to <code>null</code>.
 * </ul>
 * <li>If value object implements {@link TreeNodeBuilder}, the child nodes are defined by the value object as described
 * in the builder documentation (however, note that this approach skips CDI dependency injection).
 * <li>Otherwise, the child-nodes of the current tree node, when present, are resolved and created as described in steps
 * 2-6.
 * <li>A tree node instance is created using the resolved key object, value object, and child-nodes.
 * </ol>
 * <p>
 * As can been seen from the algorithm, the tree is created starting from the leaves of the nodes, although the meta
 * information is read starting from the root of the tree. In addition, thanks to CDI, all associated node key and value
 * objects get their annotated dependencies injected.
 *
 * @author Martti Tamm
 */
public final class RootNodeProducer {

  @Inject
  private BeanManager manager;

  /**
   * Produces a tree where root {@link TreeNode} has the value object with the same name and/or type as provided in the
   * {@link RootNode} annotation (or class field name, if injection point is a class field).
   * <p>
   * Producer fails with an exception when the injection point does not declare neither root node value bean name nor
   * type, and injection point is not a class field.
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

    if (rootAnnotation.type() != null && rootAnnotation.type() != Object.class) {
      return findTreeNode(rootAnnotation.type());
    } else if (rootAnnotation.value() != null && rootAnnotation.value().trim().length() > 0) {
      return findTreeNode(rootAnnotation.value());
    } else if (injectionPoint.getMember() instanceof Field) {
      return findTreeNode(injectionPoint.getMember().getName());
    } else {
      throw new RuntimeException("No reference to tree node value bean: " + injectionPoint);
    }
  }

  private TreeNode findTreeNode(String beanName) {
    Set<Bean<?>> beans = this.manager.getBeans(beanName);
    if (beans.isEmpty()) {
      throw new RuntimeException("Could not find TreeNode value by name '" + beanName + "'.");
    } else if (beans.size() > 1) {
      throw new RuntimeException("Currently only one TreeNode value is expected. Name: '" + beanName + ", count: "
          + beans.size() + "'.");
    }

    return createTreeNode(beans.iterator().next());
  }

  private TreeNode findTreeNode(Class<?> beanType) {
    Set<Bean<?>> beans = this.manager.getBeans(beanType);
    if (beans.isEmpty()) {
      throw new RuntimeException("Could not find TreeNode value by type: " + beanType);
    } else if (beans.size() > 1) {
      throw new RuntimeException("Currently only one TreeNode value is expected. Type: " + beanType + ", count: "
          + beans.size() + "'.");
    }

    return createTreeNode(beans.iterator().next());
  }

  private TreeNode createTreeNode(Bean<?> bean) {
    Object value = getBeanInstance(bean);

    ws.rocket.path.annotation.TreeNode meta = bean.getBeanClass().getAnnotation(
        ws.rocket.path.annotation.TreeNode.class);

    TreeNode result;

    if (value instanceof TreeNodeBuilderAware) {
      result = new TreeNodeBuilder(resolveKey(bean, value, meta), value).build();
    } else {
      result = new TreeNode(resolveKey(bean, value, meta), value, resolveChildren(bean, meta));
    }

    return result;
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
          key = getBeanInstance(beans.iterator().next());
        }
      }
    }

    return key;
  }

  private TreeNode[] resolveChildren(Bean<?> valueBean, ws.rocket.path.annotation.TreeNode meta) {

    if (meta == null || meta.childNames().length == 0 && meta.childTypes().length == 0) {
      return null;
    } else if (meta.childNames().length > 0 && meta.childTypes().length > 0) {
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
      childNodes = new TreeNode[meta.childNames().length];
      int i = 0;

      for (String childName : meta.childNames()) {
        childNodes[i++] = findTreeNode(childName);
      }
    }

    return childNodes;
  }

  private Object getBeanInstance(Bean<?> bean) {
    return this.manager.getReference(bean, bean.getBeanClass(), this.manager.createCreationalContext(bean));
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
