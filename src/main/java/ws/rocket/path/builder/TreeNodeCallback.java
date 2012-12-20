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

package ws.rocket.path.builder;

/**
 * Callback contract for {@link TreeNodeBuilder} to control key and value object initialization of tree nodes. This
 * callback has power to modify or even replace the objects, so use carefully!
 * <p>
 * To apply the callback, it must be provided to the <code>TreeNodeBuilder</code> constructor and it will be used when
 * building the nodes through-out the (sub)tree. However, the callback won't be applied when the builder is given an
 * already created <code>TreeNode</code>.
 * 
 * @author Martti Tamm
 */
public interface TreeNodeCallback {

  /**
   * Callback method that is called when the builder receives a key object.
   * 
   * @param key The received key object.
   * @return The key object to use (by the builder) for constructing the node with.
   */
  Object onKey(Object key);

  /**
   * Callback method that is called when the builder receives a value object.
   * 
   * @param value The received value object.
   * @return The value object to use (by the builder) for constructing the node with.
   */
  Object onValue(Object value);

}
