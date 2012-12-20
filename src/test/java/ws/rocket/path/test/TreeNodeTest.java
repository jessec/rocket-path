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
 * Tests {@link TreeNode} class.
 * 
 * @author Martti Tamm
 */
public class TreeNodeTest {

  @Test
  public void testEmpty() {
    TreeNode node = new TreeNode();

    assertEmptyNode(node);
  }

  @Test
  public void testSimple() {
    String key = "myKey";
    BigDecimal value = new BigDecimal("3.14159265");

    TreeNode node = new TreeNode(key, value);

    assertKeyValueNoChild(node, key, value);
  }

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

  public static void assertEmptyNode(TreeNode node) {
    assertNull(node.getKey(), "Key must be null");
    assertNull(node.getValue(), "Value must be null");
    assertNoChildren(node);
  }

  public static void assertKeyValue(TreeNode node, Object key, Object value, int childCount) {
    assertKeyValue(node, key, value);
    assertEquals(childCount, node.getChildren().size());
  }

  public static void assertKeyValueNoChild(TreeNode node, Object key, Object value) {
    assertKeyValue(node, key, value);
    assertNoChildren(node);
  }

  public static void assertKeyValue(TreeNode node, Object key, Object value) {
    assertSame(node.getKey(), key, "TreeNode key was not retained");
    assertSame(node.getValue(), value, "TreeNode value was not retained");
  }

  public static void assertNoChildren(TreeNode node) {
    assertNotNull(node.getChildren());
    assertTrue(node.getChildren().isEmpty(), "TreeNode was expected to have no child-TreeNodes");
  }
}
