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

package ws.rocket.path;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Generalized contract for a tree node key, which needs to resolve on its own whether the active path item resolves to
 * current tree node. The key is given direct access to current {@link TreePath} being traversed (can be modified, too)
 * and incoming request. Using this information the key must respond whether the tree node at hand needs to be traversed
 * for delivering the request.
 * <p>
 * Dynamic key contrasts to static key by
 * <ul>
 * <li>having more than one path item options;
 * <li>ability to match keys to values from an external resource (e.g. database);
 * <li>ability to match more than one path item (including: fetching values from remaining path);
 * <li>ability to mark a path processed even if it has more path entries.
 * </ul>
 * <p>
 * Implementations of this contract must not include other access restriction checks, as they needs to be handled by
 * other contracts. Tree traversing algorithms use this contract just to find out whether the requested path goes
 * through the tree node that the key belogns to or not.
 * 
 * @author Martti Tamm
 */
public interface DynamicKey {

  /**
   * Key name for representing it in URI template. (Name can also be derived from bean name.) It does not have to be URI
   * template variable expression as not all applications have to use it, but is recommended.
   * 
   * @return A string for representing the tree node key in URI template.
   */
  String getName();

  /**
   * Tree traversing check for identifying whether the tree node, where this key belongs, matches to current path item
   * and therefore needs to be traversed. The request is provided so that the key could also set some request attributes
   * that child nodes could use (e.g. data from request or database).
   * 
   * @param path The current path (from request) being traversed.
   * @param request The incoming request.
   * @return A Boolean that is <code>true</code> when tree traversing algorithm should continue working with current
   *         tree node.
   * @see HttpServletRequest#setAttribute(String, Object)
   * @see HttpServletRequest#getAttribute(String)
   */
  boolean contains(TreePath path, HttpServletRequest request);

  /**
   * Optional contract where the dynamic key may list all of its keys (up to the count limit). Keys that wish to not
   * provide this feature may return <code>null</code>.
   * 
   * @param limit A positive integer indicating how many keys need to be returned.
   * @return A list containing the keys (up to the specified amount), or <code>null</code>.
   */
  List<String> getKeys(int limit);

}
