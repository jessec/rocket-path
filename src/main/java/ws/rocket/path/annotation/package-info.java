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
 * Annotations for describing keys and values of tree nodes so that a tree could be composed at runtime using CDI (
 * <em>Contexts and Dependency Injection</em>) mechanism. This approach for composing a tree is provided as an
 * alternative and is not mandatory. It might not even suit for all needs. For example, this approach requires each tree
 * node to have a value object. The default tree node producer currently always assigns a not null key for each node.
 * However, this annotation based method for constructing the tree could be simple and well enough for most cases.
 * <p>
 * Tree is constructed when CDI encounters a dependency injection point annotated with
 * {@link ws.rocket.path.annotation.RootNode}. The annotation refers to the value bean of the root tree node (by
 * default, looks for CDI bean named "root"). For example, a <em>Servlet</em> class could request root node:
 * 
 * <pre>
 * public MyServlet extends GenericServlet {
 * 
 *   &#64;Inject
 *   &#64;RootNode
 *   private TreeNode rootNode;
 * 
 *   private MyDeliveryAlgorithm handler;
 * 
 *   public init() {
 *     this.handler = new MyDeliveryAlgorithm(this.rootNode);
 *   }
 * 
 *   public void service(ServletRequest req, ServletResponse res) {
 *     this.handler.deliver(req, res);
 *   }
 * }
 * </pre>
 * <p>
 * <code>TreeNode</code> producer uses the data from <code>RootNode</code> annotation to search for the value bean of
 * the root node. An example of a value bean is following (note the {@link ws.rocket.path.annotation.TreeNode}
 * annotation!):
 * 
 * <pre>
 * &#64;TreeNode(children = {'client', 'manage', 'browse', 'api' })
 * public class Root implements KeyBuilder {
 * 
 *   public Object buildKey() {
 *     return KeysFactory.root();
 *   }
 * }
 * </pre>
 * <p>
 * The previous code sample for a node value illustrates many features but let's look at them individually. First, the
 * class annotation describes the CDI bean names of values belonging to child-nodes (so there will be 4 child-nodes in
 * this example). Their order will be also the same as for their names in this <code>children</code> attribute.
 * <p>
 * Beans annotated with <code>TreeNode</code> also get a default name as the annotation includes
 * {@link javax.inject.Named} annotation. In this example, the bean name is "root". That would be also the
 * <code>TreeNode</code> key value but this class uses the {@link ws.rocket.path.annotation.KeyBuilder} contract to
 * instantiate its own custom <code>TreeNode</code> key. A key can also be a bean: just add the bean name or type to
 * <code>TreeNode</code> annotation to be looked up when node is created.
 * <p>
 * Each annotation supports referring to classes either by CDI bean name(s) or Java class(es). Both methods have their
 * pros and cons, however, they cannot be mixed in one annotation declaration. When unsure, referring by class could be
 * preferred as it is less vulnerable to code refactoring (e.g. when renaming a class).
 */
package ws.rocket.path.annotation;