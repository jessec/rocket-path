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

/**
 * Annotation-based approach for describing a tree so that it could be composed at runtime (on demand) using CDI (
 * <em>Contexts and Dependency Injection</em>) mechanism.
 * <p>
 * This approach for composing a tree is provided as an alternative and is not mandatory. It might not even suit for all
 * needs. For example, this approach requires each tree node to have a value object (as its class must be annotated).
 * However, this annotation-based method for constructing the tree could be simple and well enough for most cases.
 * <p>
 * Tree is constructed when CDI encounters a dependency injection point (field, parameter) with
 * {@link ws.rocket.path.TreeNode} type and with {@link ws.rocket.path.annotation.RootNode} annotation. The annotation
 * refers to the value bean of the root tree node:
 *
 * <pre>
 * &#064;Inject
 * &#064;RootNode(&quot;root&quot;)
 * private TreeNode root;
 * </pre>
 * <p>
 * {@link ws.rocket.path.annotation.RootNodeProducer} uses the data (CDI bean name or type) from <code>RootNode</code>
 * annotation to search for the value bean of the root node. An example of a bean, which will become the <em>value</em>
 * of a tree node, is following (complementing the previous sample):
 *
 * <pre>
 * &#064;Named(&quot;root&quot;)
 * &#064;TreeNode(childNames = { &quot;child1&quot;, &quot;child2&quot; })
 * public class Root implements KeyBuilder {
 *
 *   public Object buildKey() {
 *     return KeysFactory.root();
 *   }
 * }
 * </pre>
 * <p>
 * The previous code sample for a node value illustrates many features but let's look at them individually:
 * <ol>
 * <li>the CDI <code>&#64;Named</code> annotation is used so that the bean could be referred by name;
 * <li>the <code>&#64;TreeNode</code> annotation is used for describing the values belonging to child-nodes (so there
 * will be 2 child-nodes in this example, and with the same order).
 * <li>the class optionally implements {@link ws.rocket.path.annotation.KeyBuilder} contract to create a more
 * sophisticated key for its <code>TreeNode</code>. Otherwise, the <code>&#64;TreeNode</code> could be used instead
 * (though, the interfaces ought to be removed then as it takes precedence).
 * </ol>
 * <p>
 * When a node value object implements {@link ws.rocket.path.builder.TreeNodeBuilderAware} contract, it will override
 * annotations on the same class.
 * <p>
 * Each annotation supports referring to classes either by CDI bean name(s) or Java class(es). Both methods have their
 * pros and cons, however, they cannot be mixed in one annotation declaration. When unsure, referring by class could be
 * preferred as it is less vulnerable to code refactoring (e.g. when renaming a class).
 */
package ws.rocket.path.annotation;

