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
 * Sepcial contract for tree node values that contain a list of records to be displayed. The reason for this interface
 * is to separate page logic from the lists of data rows. When a page contains one list, the same node serving the page
 * can implement this contract, too. When more than one list is shown per page, the page node can add child-nodes per
 * each list it contains and deliver list rendering to them.
 * <p>
 * This contract does not separate the HTTP method. A general recommendation is to apply custom list filtering only when
 * the POST request is sent (usually via AJAX request), otherwise apply default filtering. Either way, the returned view
 * object should be capable of rendering only the list portion of the page. (Whether the view renders filter inputs and
 * paging links is specific to each application.)
 * 
 * @author Martti Tamm
 */
public interface DataList {

  /**
   * Provides a view for the list request.
   * 
   * @param request The incoming request.
   * @return An instance of view implementation.
   */
  View handle(HttpServletRequest request);

}
