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
 * Semantic contracts for tree node values enabling HTTP response caching.
 * <p>
 * Using HTTP caching, applications can reduce network traffic and server load while improving end-user browsing
 * experience. However, caching must be well-thought before used. For example, dynamic queries should not be cached [for
 * a long time]. On the other hand, caching rules should be included in responses to avoid unexpected default behaviour
 * of user-agents (e.g. browsers).
 * <p>
 * Tree node value classes may implement any of these contracts to specify that the generated response must be cached.
 * Only one such contract needs to be implemented.
 * <p>
 * Handling of these contracts (adding the appropriate HTTP caching headers) is usually delegated to
 * {@link ws.rocket.path.meta.View} objects, which have access to the targeted tree node.
 */
package ws.rocket.path.meta.http.cache;