package ws.rocket.path.test.integration;

import javax.inject.Inject;
import javax.inject.Singleton;

import ws.rocket.path.TreeNode;
import ws.rocket.path.annotation.RootNode;


@Singleton
public class HttpRequestTreeBean {

  @Inject
  @RootNode("rootPage")
  private TreeNode node;

  public TreeNode getNode() {
    return this.node;
  }
}
