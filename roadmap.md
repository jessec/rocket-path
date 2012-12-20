Rocket-Path Library Roadmap
===========================

Each time a new version of the library is released, it is stable for use in production environment. However, changes in
the version numbers is to be interpreted as follows:

* increase of the major version number indicates that maturity of previously developed API contracts in terms that they
  are guaranteed to remain backward compatible.
* increase of the minor version number indicates an improvment in the current development branch, which may revise some
  of the API contracts that has been developed in this branch (indicated by major version number); API contracts from
  previous branches must be preserved as much as possible.
* version numbers of dependencies (although few) may be altered by any release but it is up to the library user to
  declare in a project which versions of these dependecies are to be actually used.

Roadmap for features up to version 1.0
--------------------------------------

### Version 0.2 ###

1. JSP tag(s) for composing links;
2. JSP tag(s) for invoking a table-based list rendering;

### Version 0.3 ###

1. More ViewFactory samples for XML, SOAP, JSON, TXT, file download (without external APIs - i.e. only JDK and JEE)
2. Tests for ViewFactory samples.

### Version 0.4 ###

1. New ViewFactory samples for debugging applications.

### [Unscheduled] ###

1. Refactoring of ViewFactories (standardizing them)
