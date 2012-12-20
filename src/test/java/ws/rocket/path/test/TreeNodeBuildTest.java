package ws.rocket.path.test;

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


public class TreeNodeBuildTest {

  private static final String TEST_KEY1 = "myKey";
  private static final String TEST_KEY2 = "anotherKey";
  private static final String TEST_KEY3 = "thirdKey";

  private static final BigDecimal TEST_VALUE1 = new BigDecimal("3.14159265");
  private static final Object TEST_VALUE2 = new Object();
  private static final String TEST_VALUE3 = "a node value";

  @Test
  public void testDefault() {
    TreeNode node = new TreeNodeBuilder().build();

    assertEmptyNode(node);
  }

  @Test
  public void testEmpty() {
    TreeNode node = new TreeNodeBuilder().build();
    
    assertKeyValue(node, null, null);
    assertNoChildren(node);
  }

  @Test
  public void testSimple() {
    TreeNode node = new TreeNodeBuilder(TEST_KEY1, TEST_VALUE1).build();

    assertKeyValue(node, TEST_KEY1, TEST_VALUE1);
    assertNoChildren(node);
  }

  @Test
  public void testSimpleWithOneLevel() {
    TreeNode node = new TreeNodeBuilder(TEST_KEY1, TEST_VALUE1)
        .addChild(TEST_KEY2, TEST_VALUE2)
        .addChild(TEST_KEY3, TEST_VALUE3)
        .addChild(new TreeNode())
        .build();

    assertKeyValue(node, TEST_KEY1, TEST_VALUE1, 3);

    assertKeyValueNoChild(node.getChildren().get(0), TEST_KEY2, TEST_VALUE2);
    assertKeyValueNoChild(node.getChildren().get(1), TEST_KEY3, TEST_VALUE3);
    assertEmptyNode(node.getChildren().get(2));
  }

  @Test
  public void testSimpleWithTwoLevels() {
    TreeNodeWithChildren nodeValue = new TreeNodeWithChildren();
    TreeNode node = new TreeNodeBuilder()
        .addChild(TEST_KEY1, nodeValue)
        .addChild(new TreeNode())
        .build();

    assertKeyValue(node, null, null, 2);

    assertEmptyNode(node.getChildren().get(1));

    TreeNode node2 = node.getChildren().get(0);
    assertKeyValue(node2, TEST_KEY1, nodeValue, 3);

    assertKeyValueNoChild(node2.getChildren().get(0), TEST_KEY1, TEST_VALUE1);
    assertKeyValueNoChild(node2.getChildren().get(1), TEST_KEY2, TEST_VALUE2);
    assertKeyValueNoChild(node2.getChildren().get(2), TEST_KEY3, TEST_VALUE3);
  }

  @Test
  public void testKeyValueCallback() {
    TreeNodeWithChildren nodeValue = new TreeNodeWithChildren();
    TreeNodeCallback callback = new KeyValueReplacer();

    TreeNode node = new TreeNodeBuilder(callback)
        .addChild(TEST_KEY1, nodeValue)
        .addChild(new TreeNode())
        .build();

    assertKeyValue(node, TEST_KEY3, TEST_VALUE3, 2);
    assertKeyValueNoChild(node.getChildren().get(0), TEST_KEY3, TEST_VALUE3);
    assertEmptyNode(node.getChildren().get(1));
  }

  private class TreeNodeWithChildren implements TreeNodeBuilderAware {

    public void initNode(TreeNodeBuilder builder) {
      builder.addChild(new TreeNode(TEST_KEY1, TEST_VALUE1))
          .addChild(TEST_KEY2, TEST_VALUE2)
          .addChild(new TreeNode(TEST_KEY3, TEST_VALUE3));
    }
  }

  private class KeyValueReplacer implements TreeNodeCallback {

    public Object onKey(Object key) {
      return TEST_KEY3;
    }
    
    public Object onValue(Object value) {
      return TEST_VALUE3;
    }
  }
}
