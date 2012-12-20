package ws.rocket.path.test.integration.web;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.Viewable;
import ws.rocket.path.support.SampleViewFactory;


@Named("login")
public class LoginPage implements Viewable {

  public View doGet(HttpServletRequest request) {
    return SampleViewFactory.login();
  }
}
