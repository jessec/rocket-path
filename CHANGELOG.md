CHANGELOG for _Rocket-Path_
---------------------------

### Version 0.2 ###

1. ``TreePath``:
   * now extension is NOT extracted when the most simple constructor is used;
   * new constructors for validating and controlling extension extraction;
   * renamed ``getDepth()`` to ``getPosition()``;
   * ``setExtensionToPathSegment()`` to suppress parsed extension after path is parsed;
   * new methods: ``append()``, ``getPathLength``, ``beginning()``, ``end()``, ``isPathEmpty()``;
   * additional tests for testing all of the functionality of ``TreePath``.
2. ``@TreeNode`` annotation:
   * renamed attribute ``children`` to ``childNames`` for consistency with ``childTypes``.
3. ``RootNodeProducer``:
   * now when a node value implements ``TreeNodeBuilderAware``, the builder contract will take precedence.
4. Other changes:
   * removed packages ``meta`` and ``support``, and interface ``DynamicKey`` (see: mrtamm/rocket-embedded);
   * major review and update of documentation.
