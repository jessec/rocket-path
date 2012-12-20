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
 * Declares an object response as tagged (tag per entity state). When the entity changes, a new tag must be generated
 * and attached. The new tag must not have been used before on the entity.
 * <p>
 * A request handler that reaches this target object can optionally implement support for following HTTP headers:
 * <ul>
 * <li><code>ETag</code> (in response)
 * <li><code>If-Match</code> (in request)
 * <li><code>If-None-Match</code> (in request)
 * <li><code>If-Unmodified-Since</code> (in request)
 * </ul>
 * 
 * @author Martti Tamm
 */
public interface Taggable {

  /**
   * Provides the ETag value to use with ETag header. The value should include quotes around the string value.
   * <p>
   * When this method returns <code>null</code> then caching headers by ETag string must be omitted.
   * 
   * @return The ETag string value, or <code>null</code>.
   */
  String getETag();

}
