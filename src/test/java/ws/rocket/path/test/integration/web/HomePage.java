package ws.rocket.path.test.integration.web;

import java.util.ResourceBundle;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ws.rocket.path.meta.MenuListable;
import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.Viewable;
import ws.rocket.path.support.SampleViewFactory;


@Named("home")
public class HomePage implements Viewable, MenuListable {

  public View doGet(HttpServletRequest request) {
    return SampleViewFactory.main(null);
  }

  public String getName(ResourceBundle bundle) {
    return bundle.getString("test.rootName");
  }
}
