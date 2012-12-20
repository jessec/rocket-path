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

/**
 * Declares an object response as expirable after certain amount of time.
 * <p>
 * A request handler that reaches this target object can optionally implement support for following HTTP headers in a
 * response:
 * <ul>
 * <li><code>Expires</code> (<code>Date</code> header should also be provided)
 * <li><code>Cache-Control: max-age=</code><em>xxx</em>
 * <li><code>Cache-Control: s-maxage=</code><em>xxx</em>
 * </ul>
 * 
 * @author Martti Tamm
 */
public interface Expirable {

  /**
   * Provides the maximum age in seconds until the cached response expires.
   * <p>
   * When this method returns a non-positive value then the max-age header may be omitted.
   * 
   * @return A time in seconds until the response should expire.
   */
  long getMaxAge();

}
