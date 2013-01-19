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

package ws.rocket.path.meta.http.cache;

import java.util.Date;

/**
 * Declares an object as modifiable with a date property indicating the last modified time-stamp.
 * <p>
 * A request handler that reaches this target object can optionally implement support for following HTTP headers:
 * <ul>
 * <li><code>Last-Modified</code> (in response)
 * <li><code>If-Modified-Since</code> (in request)
 * <li><code>If-Unmodified-Since</code> (in request)
 * <li><code>Date</code> (in response; recommended for keeping time consistent with <code>Last-Modified</code>)
 * </ul>
 * The handler may also provide additional cache control headers.
 * <p>
 * Using HTTP caching, applications can reduce network traffic and server load while improving end-user browsing
 * experience. However, caching must be well-thought before used. For example, dynamic queries must not be cached. On
 * the other hand, caching suggestions should be included in the response to avoid unexpected default behaviour of
 * user-agents (e.g. browsers).
 * 
 * @author Martti Tamm
 */
public interface Modifiable {

  /**
   * Provides the last modified date of the resource contained by this object.
   * <p>
   * When this method returns <code>null</code> then caching headers by last modified date must be omitted.
   * 
   * @return A date object or <code>null</code>.
   */
  Date getLastModified();

}
