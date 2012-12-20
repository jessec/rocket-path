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

package ws.rocket.path.meta;

import javax.servlet.http.HttpServletRequest;

/**
 * Contract for tree values wishing to limit access to them and their descendant objects.
 * 
 * @author Martti Tamm
 */
public interface Restrictable {

  /**
   * Reports whether the object is available for delivering or handling the request. The return value does not have to
   * depend on the request object.
   * <p>
   * As an example, when <code>false</code> is returned:
   * <ul>
   * <li>for anonymous users, a login page could be served;
   * <li>for authenticated users, an access denied page could be served.
   * </ul>
   * 
   * @param request The request being handled.
   * @return A Boolean that is <code>true</code> when the request can be handled by the object (including its
   *         descendants in tree) implementing this contract
   */
  boolean isAvailable(HttpServletRequest request);
}
