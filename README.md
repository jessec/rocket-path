Rocket-Path Library
===================

A simple tree structure, written in Java, where each node can have a key, a value, and any number of child nodes. This
library also provides two additional methods to simplify construction of such trees. Finally, the library contains a
``TreePath`` class for representing paths to tree nodes and for tracking the current position in the path. All in all,
the *Rocket-Path* library is small, light-weight, but helpful for constructing simple trees and navigating through them.

Using tree structures for solving complicated tasks in an application has many benefits, including the following:

* can evolve (scale) over time;
* can be visualized;
* can be tested;
* has normal memory usage.

Overview
--------

The tree nodes of this library are immutable, serializable, and data within is totally accesible through getter methods.
Created trees cannot be altered after creation. To alter a tree, a new tree must be created where unchanged subtrees may
be reused from the previous tree.

There are three ways to construct a tree:

1. __Creating instances of ``TreeNode`` class directly__

	```java
	// new TreeNode(Object key, Object value, TreeNode... children);
	TreeNode alice    = new TreeNode(Integer.valueOf(1), "Alice");
	TreeNode bob      = new TreeNode(Integer.valueOf(2), "Bob");
	TreeNode mary     = new TreeNode(Integer.valueOf(3), "Mary");
	TreeNode managers = new TreeNode("managers", "Managers", alice, bob, mary);
	```

2. __Using ``TreeNodeBuilder`` as a more convenient way__

	```java
	private class ManagersNode implements TreeNodeBuilderAware {
	
	  public void initNode(TreeNodeBuilder builder) {
	    builder.addChild(Integer.valueOf(1), "Alice");
	    builder.addChild(Integer.valueOf(2), "Bob");
	    builder.addChild(Integer.valueOf(3), "Mary");
	  }
	}
	```

	```java
	// new TreeNodeBuilder([Object key, Object value, ][TreeNodeCallback callback])

	TreeNode team = new TreeNodeBuilder(null, "Development team")
	  .addChild("managers", new ManagersNode())
	  .addChild("developers", new DevelopersNode())
	  .addChild("analysts", new AnalystsNode())
	  .addChild(new TreeNode("testers", alice, bob, mary))
	  .build();
	```

3. __``@TreeNode`` annotations on node value classes (with some help from CDI)__

	```java
	@Named("teamA") // CDI named bean, one way for referring to beans other than class.
	@TreeNode(key = "DevTeam", childTypes = { Managers.class, Developers.clas, Analysts.class, Testers.class })
	public class DevelopmentTeam {}
	```

	Examples of how to inject a constructed tree:

	```java
	@Inject @RootNode(type = DevelopmentTeam.class) private TreeNode team; // by root node value object type
	@Inject @RootNode("teamA") private TreeNode team;                      // by root node value bean name (explicit)
	@Inject @RootNode private TreeNode teamA;                              // by root node value bean name (implicit)
	```

``TreePath``
------------

It is easier to refer to a tree node when referring to it by some kind of path. ``TreePath`` is a class for
parsing, formatting, representing and traversing a tree path of type ``String``. By default, it assumes that path
elements are separated by forward slash. Additionally supports extension parsing when other than the simplest
constructor is used for creating a path (since detecting whether extension is really an extension and not part of the
last path segment, it needs more careful handling). Default extension separator is dot. Empty path segments are ignored
(so a preceding or trailing slash won't have any significance). ``TreePath`` also provides handy methods for tracking
the current position in path and printing out the previous, following, or full path.

Construction:

```java
new TreePath(String path)
new TreePath(String path, String pathItemSeparator, String extensionSeparator)
new TreePath(String path, String[] allowedExtensions, boolean extensionsCaseSensitive)
new TreePath(String path, String itemSep, String extSep, String[] allowedExts, boolean extCaseSensitive)
```

More information
----------------

Although this README covers the most use-cases, please refer to the complete
[JavaDoc](http://mrtamm.github.com/rocket-path/javadoc/0.1/) or the
[User Guide](https://github.com/mrtamm/rocket-path/wiki/User-Guide) of the *Rocket-Path* library to get more detailed
information.

This library used to contain a tree-based HTTP request processing code in version 0.1 which is now removed and
maintained separately: rocket-path-servlet.

### Dependencies ###

*Rocket-Path* is compatible with Java 6 or newer runtime. If _Contexts And Dependecy Injection_ (CDI) is installed at the
platform, the annotations-based approch for constructing trees is also enabled.

There are no further dependencies.

### Building ###

This project uses [Gradle](http://www.gradle.org/) for

* downloading dependencies (automatically),
* generating Eclipse IDE project preferences file (``gradle eclipse``),
* executing unit tests (``gradle test``),
* building the project (``gradle jar``).

To view more available tasks, execute following command:

	gradle tasks [--all]

Gradle uses ``./build/`` directory for storing build process results:

	./build/libs/           - composed JAR file directory
	./build/docs/javadoc/   - generated JavaDoc directory
	./build/reports/tests/  - generated report for executed tests

Gradle can be configured by editing ``./build.gradle`` file.

### License ###

This library is open-sourced with [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0) and is free to use.
