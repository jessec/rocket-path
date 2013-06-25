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

package ws.rocket.path.test.annotation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ws.rocket.path.TreeNode;
import ws.rocket.path.test.annotation.data.BeanChildBuilder1;
import ws.rocket.path.test.annotation.data.BeanChildBuilder2;
import ws.rocket.path.test.annotation.data.BeanChildBuilders;
import ws.rocket.path.test.annotation.data.BeanKeyBuilder1;
import ws.rocket.path.test.annotation.data.BeanKeyBuilder2;
import ws.rocket.path.test.annotation.data.BeanKeyBuilders;
import ws.rocket.path.test.annotation.data.BeanLeaf;
import ws.rocket.path.test.annotation.data.BeanNoChildren;
import ws.rocket.path.test.annotation.data.BeanNoData;
import ws.rocket.path.test.annotation.data.BeanNoKey;
import ws.rocket.path.test.annotation.data.Root;

/**
 * Test-cases for {@link ws.rocket.path.annotation.TreeNode} and {@link ws.rocket.path.annotation.RootNode} annotations.
 * The actual logic being tested is located at {@link ws.rocket.path.annotation.RootNodeProducer}.
 * <p>
 * This test-case relies on <a href="http://www.seamframework.org/Weld" target="_blank">Weld</a>, an open-source
 * implementation of CDI.
 *
 * @author Martti Tamm
 */
public final class AnnotationTest {

  private TreeNode rootNode;

  private Weld weld = new Weld();

  /**
   * Initializes the Weld container and retrieves the constructed {@link TreeNode} through a wrapper bean &ndash;
   * {@link RootNodeBeanWrapper}, which retrieved from Weld container.
   */
  @BeforeClass
  public void beforeTests() {
    new RootNodeBeanWrapper().getNode();
    WeldContainer weldContainer = this.weld.initialize();
    this.rootNode = weldContainer.instance().select(RootNodeBeanWrapper.class).get().getNode();
  }

  /**
   * Shuts down the Weld container.
   */
  @AfterClass
  public void afterTests() {
    this.weld.shutdown();
  }

  /**
   * Tests the root node of the tree constructed by {@link ws.rocket.path.annotation.RootNodeProducer} via Weld.
   */
  @Test
  public void testRootNode() {
    testNode(this.rootNode, "[root]", Root.class, 3);
  }

  /**
   * Tests the first child-node of the constructed tree.
   */
  @Test(dependsOnMethods = "testRootNode")
  public void testKeyBuilders() {
    TreeNode node = this.rootNode.getChildren().get(0);
    testNode(node, this.rootNode.getValue(), BeanKeyBuilders.class, 2);

    testChildLeaf(node, 0, "key:keyBuilder1", BeanKeyBuilder1.class);
    testChildLeaf(node, 1, null, BeanKeyBuilder2.class);
  }

  /**
   * Tests the second child-node of the constructed tree.
   */
  @Test(dependsOnMethods = "testRootNode")
  public void testTreeNodeBuilderAware() {
    TreeNode node = this.rootNode.getChildren().get(1);
    testNode(node, this.rootNode.getValue(), BeanChildBuilders.class, 2);

    TreeNode child0 = node.getChildren().get(0);
    testNode(child0, null, BeanChildBuilder1.class, 1);
    testNode(child0.getChildren().get(0), "123", "abc", 0);

    testChildLeaf(node, 1, null, BeanChildBuilder2.class);
  }

  /**
   * Tests the third child-node of the constructed tree.
   */
  @Test(dependsOnMethods = "testRootNode")
  public void testTreeNodeAnnotation() {
    TreeNode node = this.rootNode.getChildren().get(2);
    testNode(node, null, BeanNoKey.class, 3);

    testChildLeaf(node, 0, "NoChildren", BeanNoChildren.class);
    testChildLeaf(node, 1, null, BeanNoData.class);
    testChildLeaf(node, 2, null, BeanLeaf.class);
  }

  private static void testChildLeaf(TreeNode parent, int childIndex, Object key, Class<?> childType) {
    TreeNode node = parent.getChildren().get(childIndex);
    testNode(node, key, childType, 0);
  }

  private static void testNode(TreeNode node, Object key, Class<?> valueType, int childCount) {
    assertEquals(node.getKey(), key, "TreeNode key was not retained.");
    assertTrue(valueType.isInstance(node.getValue()), "Node value type mismatch.");
    assertEquals(childCount, node.getChildren().size(), "Child-node count mismatch.");
  }

  private static void testNode(TreeNode node, Object key, Object value, int childCount) {
    assertEquals(node.getKey(), key, "TreeNode key was not retained.");
    assertEquals(node.getValue(), value, "Node value  mismatch.");
    assertEquals(childCount, node.getChildren().size(), "Child-node count mismatch.");
  }
}
