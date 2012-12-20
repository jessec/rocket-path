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
 * Builder design pattern based solution for creating trees. Instead of creating each tree node individually, the builder
 * approach simplifies tree construction by allowing tree nodes be conveniently described and delegating the actual tree
 * construction to the builder. A simple example of usage would be following:
 * 
 * <pre>
 * TreeNode root = new TreeNodeBuilder("root", "root value")
 *  .addChild("child1", "child1 value")
 *  .addChild("child2", "child2 value")
 *  .addChild("child3", "child3 value")
 *  .build();
 * </pre>
 * 
 * <p>
 * Sometimes the key and value objects need to be applied a common initialization process (e.g. dependency injection).
 * For this purpose, an implementation of {@link ws.rocket.path.builder.TreeNodeCallback} can be provided to the
 * builder class constructor. Each key and value object will be provided to the callback before using these objects for
 * creating a tree node.
 * 
 * <p>
 * Since declaring a huge tree in one Java file can introduce a lot of class/package imports and can make the tree
 * creator look guilty of knowing too much about other classes, the builder pattern helps to delegate some tree
 * construction work to the value objects of (sub)tree nodes. Every node value object implementing
 * {@link ws.rocket.path.builder.TreeNodeBuilderAware} is given an instance of
 * {@link ws.rocket.path.builder.TreeNodeBuilder} for the {@link ws.rocket.path.TreeNode} instance where the
 * value object is placed so that the value object could further describe the child nodes of its tree node, and so on.
 * Using this approach, value objects have an option to describe the child nodes of their tree nodes and delegate the
 * description of further descendants to the value objects of child nodes. It should reduce the problem of how much
 * classes know about other classes in an application.
 */
package ws.rocket.path.builder;