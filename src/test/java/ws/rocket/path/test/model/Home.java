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

package ws.rocket.path.test.model;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import ws.rocket.path.annotation.TreeNode;
import ws.rocket.path.meta.MenuListable;
import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.Viewable;
import ws.rocket.path.support.SampleViewFactory;

/**
 * A sample class/object that can be used as the value of a child-node of the root node.
 * 
 * @author Martti Tamm
 */
@TreeNode(key = "home")
public final class Home implements Viewable, MenuListable {

  @Override
  public View doGet(HttpServletRequest request) {
    return SampleViewFactory.main(null);
  }

  @Override
  public String getName(ResourceBundle bundle) {
    return bundle.getString("title.home");
  }
}
