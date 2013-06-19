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
 * A builder-based solution for creating trees. Instead of creating each tree node individually, the builder approach
 * simplifies construction by allowing tree nodes be conveniently described and delegating the actual construction to
 * the builder. A simple example of usage would be following:
 *
 * <pre>
 * TreeNode root = new TreeNodeBuilder("root", "root value")
 *  .addChild("child1", "child1 value")
 *  .addChild("child2", "child2 value")
 *  .addChild("child3", "child3 value")
 *  .build();
 * </pre>
 *
 * <h3>Key And Value Callback</h3>
 * <p>
 * Sometimes the <em>key</em> and <em>value</em> objects need to be applied a common initialization process (e.g.
 * dependency injection). For this purpose, an implementation of {@link ws.rocket.path.builder.TreeNodeCallback} can be
 * provided to the <code>TreeNodeBuilder</code> constructor. Each <em>key</em> and <em>value</em> object provided to the
 * {@link ws.rocket.path.builder.TreeNodeBuilder#addChild(Object, Object)} method will go through the callback before
 * passed to {@link ws.rocket.path.TreeNode} constructor.
 * <h3>Delegation of Subtree Creation</h3>
 * <p>
 * Since declaring a huge tree in one Java file can introduce a lot of class imports and can make the tree creator look
 * guilty of knowing too much about other classes, the builder-approach helps to delegate some tree construction work to
 * tree node <em>value</em> objects. Every node <em>value</em> implementing
 * {@link ws.rocket.path.builder.TreeNodeBuilderAware} is given an instance of <code>TreeNodeBuilder</code> for the
 * <code>TreeNode</code> instance where the <em>value</em> object is placed so that it could further describe the child
 * nodes of its tree node. Using this approach, tree node <em>values</em> have an option to describe the child-nodes of
 * their tree nodes and delegate the description of further descendants to the <em>values</em> of child-nodes. It should
 * reduce the problem of how much classes know about other classes in an application.
 */
package ws.rocket.path.builder;

