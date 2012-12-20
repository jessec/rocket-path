package ws.rocket.path.test.integration.web;

import ws.rocket.path.annotation.TreeNode;

@TreeNode(childTypes = { HomePage.class, LoginPage.class, LogoutPage.class })
public class RootPage {

}
