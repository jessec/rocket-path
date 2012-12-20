package ws.rocket.path.test.integration.web;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.Submitable;
import ws.rocket.path.support.SampleViewFactory;


@Named("logout")
public class LogoutPage implements Submitable {

  public View doPost(HttpServletRequest request) {
    return SampleViewFactory.redirect("/");
  }
}
