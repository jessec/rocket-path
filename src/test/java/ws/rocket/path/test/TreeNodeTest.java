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

package ws.rocket.path.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

import org.testng.annotations.Test;

import ws.rocket.path.TreeNode;

/**
 * Test-cases for {@link TreeNode} class. Also provides common assertion method for evaluating <code>TreeNode</code>s to
 * match conditions.
 * 
 * @author Martti Tamm
 */
public final class TreeNodeTest {

  /**
   * Tests <code>TreeNode</code> with empty constructor. Expects node to have no key, no value, and no child-nodes.
   */
  @Test
  public void testEmpty() {
    TreeNode node = new TreeNode();

    assertEmptyNode(node);
  }

  /**
   * Tests <code>TreeNode</code> with known key and value. Expects node to have same key, same value, and no
   * child-nodes.
   */
  @Test
  public void testKeyValue() {
    String key = "myKey";
    BigDecimal value = new BigDecimal("3.14159265");

    TreeNode node = new TreeNode(key, value);

    assertKeyValueNoChild(node, key, value);
  }

  /**
   * Tests <code>TreeNode</code> with known key, value, and 3 child-nodes. Expects node to have same key, same value,
   * and same child-nodes.
   */
  @Test
  public void testChildren() {
    TreeNode nodeChildA = new TreeNode();
    TreeNode nodeChildB = new TreeNode();
    TreeNode nodeChildC = new TreeNode();
    TreeNode node = new TreeNode("key", null, nodeChildA, nodeChildB, nodeChildC);

    assertKeyValue(node, "key", null, 3);

    assertEmptyNode(nodeChildA);
    assertEmptyNode(nodeChildB);
    assertEmptyNode(nodeChildC);

    assertEquals(Arrays.asList(nodeChildA, nodeChildB, nodeChildC), node.getChildren());
  }

  /**
   * Asserts that given node has no key, no value and no child-nodes.
   * 
   * @param node The tree node to test.
   */
  public static void assertEmptyNode(TreeNode node) {
    assertNull(node.getKey(), "Key must be null");
    assertNull(node.getValue(), "Value must be null");
    assertNoChildren(node);
  }

  /**
   * Asserts that given node has same key, same value and given amount of child-nodes.
   * 
   * @param node The tree node to test.
   * @param key The expected key.
   * @param value The expected value.
   * @param childCount The expected child-node count.
   */
  public static void assertKeyValue(TreeNode node, Object key, Object value, int childCount) {
    assertKeyValue(node, key, value);
    assertEquals(childCount, node.getChildren().size());
  }

  /**
   * Asserts that given node has same key, same value and no child-nodes.
   * 
   * @param node The tree node to test.
   * @param key The expected key.
   * @param value The expected value.
   */
  public static void assertKeyValueNoChild(TreeNode node, Object key, Object value) {
    assertKeyValue(node, key, value);
    assertNoChildren(node);
  }

  /**
   * Asserts that given node has same key and same value. (No check on child-nodes.)
   * 
   * @param node The tree node to test.
   * @param key The expected key.
   * @param value The expected value.
   */
  public static void assertKeyValue(TreeNode node, Object key, Object value) {
    assertSame(node.getKey(), key, "TreeNode key was not retained");
    assertSame(node.getValue(), value, "TreeNode value was not retained");
  }

  /**
   * Asserts that given node has no child-nodes. (No check on key and value.)
   * 
   * @param node The tree node to test.
   */
  public static void assertNoChildren(TreeNode node) {
    assertNotNull(node.getChildren());
    assertTrue(node.getChildren().isEmpty(), "TreeNode was expected to have no child-TreeNodes");
  }
}
