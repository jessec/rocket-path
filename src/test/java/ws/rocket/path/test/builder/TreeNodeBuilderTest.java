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

package ws.rocket.path.test.builder;

import static ws.rocket.path.test.TreeNodeTest.assertEmptyNode;
import static ws.rocket.path.test.TreeNodeTest.assertKeyValue;
import static ws.rocket.path.test.TreeNodeTest.assertKeyValueNoChild;
import static ws.rocket.path.test.TreeNodeTest.assertNoChildren;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import ws.rocket.path.TreeNode;
import ws.rocket.path.builder.TreeNodeBuilder;
import ws.rocket.path.builder.TreeNodeBuilderAware;
import ws.rocket.path.builder.TreeNodeCallback;

/**
 * Test cases for {@link TreeNodeBuilder} class and its related contracts: {@link TreeNodeCallback} and
 * {@link TreeNodeBuilderAware}.
 * 
 * @author Martti Tamm
 */
public final class TreeNodeBuilderTest {

  private static final String TEST_KEY1 = "myKey";

  private static final String TEST_KEY2 = "anotherKey";

  private static final String TEST_KEY3 = "thirdKey";

  private static final BigDecimal TEST_VALUE1 = new BigDecimal("3.14159265");

  private static final Object TEST_VALUE2 = new Object();

  private static final String TEST_VALUE3 = "a node value";

  /**
   * Tests the builder against no parameters. An empty TreeNode with no key, value, and child-nodes is expected.
   */
  @Test
  public void testEmpty() {
    TreeNode node = new TreeNodeBuilder().build();
    assertEmptyNode(node);
  }

  /**
   * Tests the builder with a simple key and value. A TreeNode with same key and value but with no child-nodes is
   * expected.
   */
  @Test
  public void testKeyValue() {
    TreeNode node = new TreeNodeBuilder(TEST_KEY1, TEST_VALUE1).build();

    assertKeyValue(node, TEST_KEY1, TEST_VALUE1);
    assertNoChildren(node);
  }

  /**
   * Tests the builder with a simple key and value, and with 3 different child nodes (tree depth = 2). A TreeNode with
   * same key and value, and with described child-nodes is expected.
   */
  @Test
  public void testKeyValueWithOneLevel() {
    // @formatter:off
    TreeNode node = new TreeNodeBuilder(TEST_KEY1, TEST_VALUE1)
      .addChild(TEST_KEY2, TEST_VALUE2)
      .addChild(TEST_KEY3, TEST_VALUE3)
      .addChild(new TreeNode())
      .build();
    // @formatter:on

    assertKeyValue(node, TEST_KEY1, TEST_VALUE1, 3);

    assertKeyValueNoChild(node.getChildren().get(0), TEST_KEY2, TEST_VALUE2);
    assertKeyValueNoChild(node.getChildren().get(1), TEST_KEY3, TEST_VALUE3);
    assertEmptyNode(node.getChildren().get(2));
  }

  /**
   * Tests the builder with no key and value, and with 2 different child nodes, of which one has value object that
   * implements {@link TreeNodeBuilderAware} and adds 3 child-nodes to its node (tree depth = 3). A TreeNode with no key
   * and value, and with described child-nodes is expected.
   */
  @Test
  public void testTreeNodeBuilderAware() {
    TreeNodeWithChildren nodeValue = new TreeNodeWithChildren();

    // @formatter:off
    TreeNode node = new TreeNodeBuilder()
      .addChild(TEST_KEY1, nodeValue)
      .addChild(new TreeNode())
      .build();
    // @formatter:on

    assertKeyValue(node, null, null, 2);

    assertEmptyNode(node.getChildren().get(1));

    TreeNode node2 = node.getChildren().get(0);
    assertKeyValue(node2, TEST_KEY1, nodeValue, 3);

    assertKeyValueNoChild(node2.getChildren().get(0), TEST_KEY1, TEST_VALUE1);
    assertKeyValueNoChild(node2.getChildren().get(1), TEST_KEY2, TEST_VALUE2);
    assertKeyValueNoChild(node2.getChildren().get(2), TEST_KEY3, TEST_VALUE3);
  }

  /**
   * Tests builder with {@link TreeNodeCallback} that replaces all keys and values with known constants. The callback is
   * not expected to change {@link TreeNode}s not created by the builder.
   */
  @Test
  public void testTreeNodeCallback() {
    TreeNodeWithChildren nodeValue = new TreeNodeWithChildren();
    TreeNodeCallback callback = new KeyValueReplacer();

    // @formatter:off
    TreeNode node = new TreeNodeBuilder(callback)
      .addChild(TEST_KEY1, nodeValue)
      .addChild(new TreeNode())
      .build();
    // @formatter:on

    assertKeyValue(node, TEST_KEY3, TEST_VALUE3, 2);
    assertKeyValueNoChild(node.getChildren().get(0), TEST_KEY3, TEST_VALUE3);
    assertEmptyNode(node.getChildren().get(1));
  }

  /**
   * A sample {@link TreeNodeBuilderAware} class that always adds 3 different child-nodes to its {@link TreeNode}.
   * 
   * @author Martti Tamm
   */
  private static final class TreeNodeWithChildren implements TreeNodeBuilderAware {

    @Override
    public void initNode(TreeNodeBuilder builder) {
      // @formatter:off
      builder
        .addChild(new TreeNode(TEST_KEY1, TEST_VALUE1))
        .addChild(TEST_KEY2, TEST_VALUE2)
        .addChild(new TreeNode(TEST_KEY3, TEST_VALUE3));
      // @formatter:on
    }
  }

  /**
   * A sample {@link TreeNodeCallback} class that always replaces keys with {@link TreeNodeBuilderTest#TEST_KEY3} and
   * values with {@link TreeNodeBuilderTest#TEST_VALUE3} .
   * 
   * @author Martti Tamm
   */
  private static final class KeyValueReplacer implements TreeNodeCallback {

    @Override
    public Object onKey(Object key) {
      return TEST_KEY3;
    }

    public Object onValue(Object value) {
      return TEST_VALUE3;
    }
  }
}
