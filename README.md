Rocket-Path Library
===================

A simple tree structure, written in Java, where each node can have a key, a value, and any number of child nodes. The
tree can provide a structure to an application (or part of it) by binding together components (objects) working as a
whole. Application uses the root node as the starting point for deliverying requests or actions to application objects.
Therefore, the library is useful for applications where a path (e.g. HTTP request path) can be used for identifying a
component. As an example, Rocket-Path can complement the Java Servlet API to simplify web app (component) development.

The tree nodes of this library are immutable, serializable, and data within is totally accesible through getter methods.

All-in-all, the library provides following:

* a ``TreeNode`` class for constructing trees with provided keys and values;
* two alternative/simplifying methods for constructing a tree;
* default contracts for components capable of handling HTTP requests;
* sample algorithms for HTTP-based applications;
* a sample view factory for rendering HTTP responses.

As a bonus, the library user gets an application structure that...

* can evolve (scale) over time;
* can be visualized;
* can be tested;
* has normal memory usage.

Overview
--------

There are three ways to construct a tree:

1. creating instances of ``TreeNode`` class directly;
2. using ``TreeNodeBuilder`` as a more convenient way;
3. ``@TreeNode`` annotations on node value classes (with some help from CDI).

To be able to write algorithms that deliver actions to nodes, an application should introduce some interfaces
(contracts), which describe the characteristics of the objects. Rocket-Path library comes with some contracts for HTTP
request handlers that integrates well with the _Java Servlet (3.0) API_.

Please refer to the [JavaDoc](http://mrtamm.github.com/rocket-path/javadoc/0.1/) or the
[User Guide](https://github.com/mrtamm/rocket-path/wiki/User-Guide) of the Rocket-Path library to get more detailed
information.

### Dependencies ###

Rocket-Path library does not have any strict dependencies. However, it has integrated support for working with _Contexts
And Dependecy Injection_ (CDI) and _Java Servlet (3.0) API_. The former is supported in ``ws.rocket.path.annotation``
package and Servlet API contracts are referred to in some contracts (interfaces). Note that everyone can define own
interfaces for contracts and the default contracts in ``ws.rocket.path.meta`` package are optional.

### Building ###

This project uses [Gradle](http://www.gradle.org/) for

* downloading dependencies (automatically),
* generating Eclipse IDE project preferences file (``gradle eclipse``),
* executing unit tests (``gradle test``),
* building the project (``gradle jar``).

To view more available tasks, execute following command:

	gradle tasks [--all]

Gradle uses .``/build/`` directory for storing build process results:

	./build/libs/           - composed JAR file directory
	./build/docs/javadoc/   - generated JavaDoc directory
	./build/reports/tests/  - generated report for executed tests

Gradle can be configured by editing ``./build.gradle`` file.

### License ###

This library is open-sourced with [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0) and is free to use.
