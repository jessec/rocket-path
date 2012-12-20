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

package ws.rocket.path;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ws.rocket.path.annotation.KeyBuilder;

/**
 * A data structure which can be used for composing trees. A tree node can have a key, a value, and any amount of child
 * tree nodes. Note that the types for key and value are not predefined nor mandatory. Also note that this class does
 * not enforce any rules upon the key and value object (including uniqueness).
 * <p>
 * Since a tree usually has many leaf nodes, which don't contain child-nodes, a special value instance is used for
 * noting an empty array of child-nodes. It helps to consume less runtime memory and also makes null-checks against
 * child-nodes array redundant. However, any one, who should synchronize over empty children array, may run into
 * problems, since the array is shared over tree node instances.
 * <p>
 * This tree node can be constructed only once. In addition, <code>TreeNode</code> is serializable as long as its keys,
 * values and children are also serializable.
 * 
 * @see KeyBuilder
 * @author Martti Tamm
 */
public class TreeNode implements Serializable {

  private static final long serialVersionUID = 1L;

  private Object key;

  private Object value;

  private List<TreeNode> children;

  /**
   * Creates a simple tree node with out a key, value and children.
   */
  public TreeNode() {
    this(null, null);
  }

  /**
   * Creates a tree node with given key, value, and children.
   * 
   * @param key A key for the tree node.
   * @param value A value for the tree node.
   * @param children Child-nodes for the tree node.
   */
  public TreeNode(Object key, Object value, TreeNode... children) {
    this.key = key;
    this.value = value;
    if (children == null || children.length == 0) {
      this.children = Collections.emptyList();
    } else {
      this.children = Collections.unmodifiableList(Arrays.asList(children));
    }
  }

  /**
   * Provides the key of this tree node.
   * 
   * @return Tree node key.
   */
  public Object getKey() {
    return this.key;
  }

  /**
   * Provides the value of this tree node.
   * 
   * @return Tree node value.
   */
  public Object getValue() {
    return this.value;
  }

  /**
   * Provides an immutable list of child nodes of this tree node. The returned value must not be used for
   * synchronization.
   * 
   * @return The child-nodes of this tree node.
   */
  public List<TreeNode> getChildren() {
    return this.children;
  }

  @Override
  public String toString() {
    return new StringBuilder("TreeNode@").append(this.key).append('=').append(this.value).append(" (child nodes: ")
        .append(this.children.size()).append(')').toString();
  }
}
