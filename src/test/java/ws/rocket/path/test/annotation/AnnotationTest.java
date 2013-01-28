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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ws.rocket.path.TreeNode;
import ws.rocket.path.test.model.Home;
import ws.rocket.path.test.model.Login;
import ws.rocket.path.test.model.Logout;
import ws.rocket.path.test.model.Root;

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
   * Tests the tree constructed by {@link ws.rocket.path.annotation.RootNodeProducer} via Weld.
   */
  @Test
  public void testTree() {
    assertNotNull(this.rootNode);
    assertEquals(this.rootNode.getKey(), "[root]", "Root node key must be '[root]'.");
    assertTrue(this.rootNode.getValue() instanceof Root, "Root node value must be a class instance of Root.");

    assertEquals(this.rootNode.getChildren().size(), 3, "Expected the root node to have 3 child-nodes.");
    testChild(rootNode, 0, "home", Home.class);
    testChild(rootNode, 1, "login", Login.class);
    testChild(rootNode, 2, "logout", Logout.class);
  }

  private static void testChild(TreeNode parent, int childIndex, String childName, Class<?> childType) {
    assertEquals(parent.getChildren().get(childIndex).getKey(), childName, "Child-node key at index " + childIndex
        + " not correct.");
    assertEquals(parent.getChildren().get(childIndex).getValue().getClass(), childType, "Child-node value at index "
        + childIndex + " not correct.");
  }
}
