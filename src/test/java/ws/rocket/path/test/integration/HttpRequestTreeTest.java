package ws.rocket.path.test.integration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ws.rocket.path.TreeNode;
import ws.rocket.path.test.integration.web.HomePage;
import ws.rocket.path.test.integration.web.LoginPage;
import ws.rocket.path.test.integration.web.LogoutPage;
import ws.rocket.path.test.integration.web.RootPage;


public class HttpRequestTreeTest {

  private TreeNode rootNode;
  private Weld weld = new Weld();

  @BeforeTest
  public void beforeTests() {
    WeldContainer weldContainer = this.weld.initialize();
    this.rootNode = weldContainer.instance().select(HttpRequestTreeBean.class).get().getNode();
  }

  @AfterTest
  public void afterTests() {
    this.weld.shutdown();
  }

  @Test
  public void testTree() {
    assertNotNull(this.rootNode);
    assertEquals(this.rootNode.getKey(), "rootPage", "Root node key must be 'rootPage'.");
    assertTrue(this.rootNode.getValue() instanceof RootPage, "Root node value must be a class instance of RootPage.");

    assertEquals(this.rootNode.getChildren().size(), 3, "Expected the root node to have 3 child-nodes.");
    testChild(rootNode, 0, "home", HomePage.class);
    testChild(rootNode, 1, "login", LoginPage.class);
    testChild(rootNode, 2, "logout", LogoutPage.class);
  }

  private static void testChild(TreeNode parent, int childIndex, String childName, Class<?> childType) {
    assertEquals(parent.getChildren().get(childIndex).getKey(), childName, "Child-node key at index " + childIndex
        + " not correct.");
    assertEquals(parent.getChildren().get(childIndex).getValue().getClass(), childType, "Child-node value at index "
        + childIndex + " not correct.");
  }
}
