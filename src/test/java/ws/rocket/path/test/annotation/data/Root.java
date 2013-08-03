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

package ws.rocket.path.test.annotation.data;

import javax.inject.Named;

import ws.rocket.path.annotation.TreeNode;

/**
 * The value of the root node used in testing the annotations based tree construction.
 * <p>
 * This class is also used to test the following:
 * <ol>
 * <li>bean name based root node value lookup (<code>&#64;RootNode("root")</code>);
 * <li>the <code>key</code> attribute is preferred to attributes <code>keyName</code> and <code>keyType</code>;
 * <li>the <code>childTypes</code> attribute is used for creating child-nodes.
 * </ol>
 *
 * @author Martti Tamm
 */
@Named
@TreeNode(key = "[root]", childTypes = { BeanKeyBuilders.class, BeanChildBuilders.class, BeanNoKey.class })
public final class Root {

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Root; // All Roots are equal (to simplify test execution)
  }

  @Override
  public int hashCode() {
    return 29;
  }
}
