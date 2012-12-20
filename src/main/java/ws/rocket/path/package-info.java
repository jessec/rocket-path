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
 * The main package for the tree classes. <a href="http://en.wikipedia.org/wiki/Tree_%28data_structure%29"
 * target="_blank">Tree</a> is a very common data structure in computer systems and elsewhere. In this library, tree is
 * used for maintaining contract-based components and for delivering HTTP requests to them depending on the request URI.
 * The tree and its construction classes are provided with this library together with some default (and optional)
 * contracts for tree components (mostly for node value objects). The contracts are not mandatory for library users as
 * different applications may have different requirements for the objects in tree nodes. Hopefully the default contracts
 * cover most common use-cases. Also note that contract handlers are to be implemented by library users. Examples can be
 * seen in {@link ws.rocket.path.support.SampleAlgorithm} and {@link ws.rocket.path.support.SampleViewFactory}.
 * 
 * <p>
 * This library provides a tree structure where a node is implemented as {@link ws.rocket.path.TreeNode} object.
 * Each tree node has a key and a value pair, with any amount of references to child tree nodes with same structure.
 * Note that key and value objects are not restricted by type, by uniqueness constraints nor are they mandatory.
 * However, the tree node class is final and its object state needs to be declared at construction.
 * 
 * <p>
 * There are three built-in methods for tree construction:
 * 
 * <ol>
 * <li>the primary way to create a tree is to create tree nodes manually, starting from the leaves and moving up until
 * the root can be constructed. Since child-nodes need to be provided to the parent node during node construction,
 * child-nodes need to be created by that time. However, since most of the time, it is more convenient to build a tree
 * starting from the root node, two other methods are introduced. Example:
 * 
 * <pre>
 * TreeNode child1 = new TreeNode(&quot;child1&quot;, &quot;value1&quot;);
 * TreeNode child2 = new TreeNode(&quot;child2&quot;, &quot;value2&quot;);
 * TreeNode parent = new TreeNode(&quot;root&quot;, &quot;rootValue&quot;, child1, child2);
 * </pre>
 * 
 * <li>the builder pattern makes building the tree starting from the root more flexible. Building a huge tree where each
 * node has its own key and value object can introduce a lot of class imports for the object taking care of the
 * building. With the builder pattern, {@link ws.rocket.path.builder.TreeNodeBuilderAware} contract was introduced
 * so that all value objects implementing it could get a handle of its containing tree-node builder object. With that
 * builder, the value object can define the child nodes of its tree-node. In summary, with the builder pattern, tree
 * definition gets often delegated to the value objects of tree nodes. Example:
 * 
 * <pre>
 * * TreeNode root = new TreeNodeBuilder(&quot;root&quot;, null)
 *   .add(new TreeNode(&quot;1&quot;, null))
 *   .add(&quot;2&quot;, new Obj1TreeNodeBuilderAware())
 *   .add(&quot;3&quot;, new Obj2TreeNodeBuilderAware())
 *   .build();
 * </pre>
 * 
 * <li>the final built-in method for constructing a tree is with the help of Contexts and Dependency Injection (CDI)
 * API. A tree gets constructed when a dependency to be injected is described like
 * <code>@Inject @RootNode TreeNode field;</code>. Tree construction starts with a value object named "root" (a custom
 * name or value object type can be specified in the annotation attribute). The value object is scanned for annotation
 * {@link ws.rocket.path.annotation.TreeNode}, which would contain information for building the sub-tree. In
 * summary, the tree is described with the annotations, and it is constructed once requested through CDI.
 * 
 * </ol>
 * 
 * <p>
 * Once a tree is constructed and reference to the root node is given, it is quite easy to write algorithms for
 * processing the tree. This library provides some most common contracts for tree keys and values for delivering HTTP
 * requests. The keys are treated as request URI (path) elements (some of which may be dynamic/variable, others static).
 * The values are treated as components (corresponding to the URI/path) implementing contracts depending on what kind of
 * HTTP methods they are willing to process. Given this contract-based information about the URIs and corresponding HTTP
 * methods of a web application, it is possible to
 * 
 * <ul>
 * <li>generate an overview of the web application resources and processing methods (API);
 * <li>know whether a request is supported or not before the component becomes aware of the request.
 * </ul>
 * 
 * <p>
 * In addition, value objects may implement a contract for marking a resource protected when a condition is not met
 * (e.g. due to user privileges). This contract can augment the overview of web application resources report by
 * highlighting which resources are accessible by the current user and which are not. This really simplifies security
 * testing.
 * 
 * <p>
 * In summary, contracts are what give semantics to the tree keys and values, while contracts are processed by
 * well-known (custom) algorithms. The contracts are not limited to those provided by this library but can be
 * additionally defined as the nature of web/presentation layer evolves and it simplifies request processing.
 */
package ws.rocket.path;