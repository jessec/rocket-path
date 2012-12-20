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

package ws.rocket.path.builder;

import java.util.ArrayList;
import java.util.List;

import ws.rocket.path.TreeNode;

/**
 * A builder version for creating {@link TreeNode}s. Since the latter requires node children up front, it's not very
 * convenient for constructing a tree. The builder delays the node creation until {@link #build()} is called.
 * <p>
 * Builder supports a callback which is called with every node key and value. The callback has complete power to
 * override the provided value, to initialize it with global configuration, etc. Call-backs, however, are not required
 * for constructing a tree.
 * <p>
 * So that the information about a node's children could be kept close to the node, node values may implement
 * {@link TreeNodeBuilderAware} to provide a callback method. The method is executed when the value object reaches the
 * builder. In the callback, the children of the containing node can be declared using the provided instance of
 * {@link TreeNodeBuilder} for that particular <code>TreeNode</code>.
 * <p>
 * Child <code>TreeNode</code>s are created immediately when constructor or {@link #addChild(Object, Object)} is called.
 * The <code>TreeNode</code> currently being built is constructed when {@link #build()} is called. The node will have
 * the child <code>TreeNode</code>s that have been defined in the builder (with the same order).
 * 
 * @author Martti Tamm
 */
public class TreeNodeBuilder {

  private final Object key;

  private final Object value;

  private final List<TreeNode> children = new ArrayList<TreeNode>();

  private final TreeNodeCallback callback;

  /**
   * Creates a new builder. The created <code>TreeNode</code> won't have neither key nor value.
   */
  public TreeNodeBuilder() {
	this(null, null, null);
  }

  /**
   * Creates a new builder. The created <code>TreeNode</code> won't have neither key nor value unless the callback
   * overrides them.
   * 
   * @param callback Optional callback which is called with every key and value in the created tree.
   */
  public TreeNodeBuilder(TreeNodeCallback callback) {
	this(null, null, callback);
  }

  /**
   * Creates a new builder. The created <code>TreeNode</code> will have given key and value pair.
   * 
   * @param key The key for the <code>TreeNode</code> being created.
   * @param value The value for the <code>TreeNode</code> being created.
   */
  public TreeNodeBuilder(Object key, Object value) {
	this(key, value, null);
  }

  /**
   * Creates a new builder. The created <code>TreeNode</code> will have given key and value pair unless the callback
   * overrides them.
   * 
   * @param key The key for the <code>TreeNode</code> being created.
   * @param value The value for the <code>TreeNode</code> being created.
   * @param callback Optional callback which is called with every key and value in the created tree.
   */
  public TreeNodeBuilder(Object key, Object value, TreeNodeCallback callback) {
	this.key = callback != null ? callback.onKey(key) : key;
	this.value = callback != null ? callback.onValue(value) : value;
	this.callback = callback;

	if (value instanceof TreeNodeBuilderAware) {
	  ((TreeNodeBuilderAware) value).initNode(this);
	}
  }

  /**
   * Adds a child <code>TreeNode</code> to the current node. The child node will have given key and value unless a
   * callback has been defined in the builder which overrides them.
   * 
   * @param key The key for the <code>TreeNode</code> being created.
   * @param value The value for the <code>TreeNode</code> being created.
   * @return The current instance of builder.
   */
  public TreeNodeBuilder addChild(Object key, Object value) {
	if (this.callback != null) {
	  key = this.callback.onKey(key);
	  value = this.callback.onValue(value);
	}

	if (value instanceof TreeNodeBuilderAware) {
	  this.children.add(new TreeNodeBuilder(key, value, this.callback).build());
	} else {
	  this.children.add(new TreeNode(key, value));
	}

	return this;
  }

  /**
   * Adds a child <code>TreeNode</code> to the current node. The node will be ignored if it's <code>null</code>. The
   * node will be added as-is. The node won't be inspected nor altered.
   * 
   * @param node The child node to add.
   * @return The current instance of builder.
   */
  public TreeNodeBuilder addChild(TreeNode node) {
	if (node != null) {
	  this.children.add(node);
	}
	return this;
  }

  /**
   * Constructs a <code>TreeNode</code> with key, value, and <code>TreeNode</code> children available to this builder.
   * 
   * @return The created <code>TreeNode</code>.
   */
  public TreeNode build() {
	TreeNode[] childrenArray = this.children.toArray(new TreeNode[this.children.size()]);
	return new TreeNode(this.key, this.value, childrenArray);
  }
}
