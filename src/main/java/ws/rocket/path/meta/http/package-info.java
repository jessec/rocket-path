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

/**
 * Semantic contracts for tree node values capable of handling HTTP requests. There is exactly one contract for each
 * HTTP methods except HEAD, OPTONS and TRACE, which are usually optional and not handled by tree nodes. Another kind of
 * exception is {@link ws.rocket.path.meta.http.Editable} contract, which is a short-hand for
 * {@link ws.rocket.path.meta.http.Viewable} + {@link ws.rocket.path.meta.http.Submitable}, indicating a
 * resource (URI) that has a view (<em>viewable</em>) where data can be edited (<em>submitable</em>).
 * <p>
 * All contracts declare a very similar method with corresponding HTTP method name:
 * 
 * <pre>
 * View doXxx(HttpServletRequest request);
 * </pre>
 * <p>
 * Instead of composing the response directly, each implementation returns a {@link ws.rocket.path.meta.View}
 * object, which handles the rendering of response. To better facilitate all possible views that the application may
 * return, a view factory should be created that the node values may use.
 * 
 * @see ws.rocket.path.meta.View
 */
package ws.rocket.path.meta.http;

