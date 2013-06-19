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
 * <p>
 * The main package of the <em>Rocket-Path</em> <a href="http://en.wikipedia.org/wiki/Tree_%28data_structure%29"
 * target="_blank">tree</a> library. This package contains the main data structures.
 * <h3>TreeNode</h3>
 * <p>
 * A tree consists of "nodes" and "edges" between them. {@link ws.rocket.path.TreeNode} has <em>key</em> and
 * <em>value</em> properties, and contains any amount of references to child-nodes. Those references are the
 * "directed edges". Usually there's no need for nodes to contain references to parent nodes. When traversing a tree,
 * starting from the root node, the passed nodes can be remembered on the way. Therefore, it's easy to find out the
 * parents of a node.
 * <p>
 * The main characteristic of all trees is that there exists a root node from which it is possible to traverse to any
 * node. The root node is the starting point for traversing a tree. In contrast, leaf nodes are all the nodes without
 * child-nodes.
 * <p>
 * The primary way to create a tree is to create <code>TreeNode</code>s manually, starting from the leaves and moving up
 * until the root can be constructed. Since child-nodes need to be provided to the parent node during
 * <code>TreeNode</code> construction, child-<code>TreeNode</code>s need to be created by that time. Here is an example:
 * </p>
 *
 * <pre>
 * TreeNode child1 = new TreeNode(&quot;child1&quot;, &quot;value1&quot;);
 * TreeNode child2 = new TreeNode(&quot;child2&quot;, &quot;value2&quot;);
 * TreeNode parent = new TreeNode(&quot;root&quot;, &quot;rootValue&quot;, child1, child2);
 * </pre>
 * <p>
 * <code>TreeNode</code> class cannot be extended and its state cannot be modified once an instance is constructed.
 * Therefore it should be thread-safe (which does not necessarily apply to objects stored as <em>keys</em> and
 * <em>values</em>.
 * </p>
 * <h3>TreePath</h3>
 * <p>
 * Sometimes it is necessary to refer to a node somewhere in the tree. The most common way is to refer by path. This
 * library provides {@link ws.rocket.path.TreePath} for helping out. It works by taking a path string (like the one used
 * in file systems), splits it into path segments, and helps iterating over the segments. Optionally, it may also
 * extract an extension (similar to file extension) from the last path segment.
 * <p>
 * It is important that <code>TreePath</code> does not actually traverse a tree but just tracks the current position in
 * the path being traversed. There are many ways to identify a child-node by a path segment value (most preferably by
 * comparing segment value to node <em>key</em>). It is up to an algorithm to identify that node and to move to the next
 * segment in a path when a child-node is identified. This approach allows <code>TreePath</code> to be used in more
 * cases.
 */
package ws.rocket.path;

