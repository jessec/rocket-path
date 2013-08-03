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

package ws.rocket.path.annotation;

/**
 * Contract for annotated tree node value objects wishing to construct their own tree node key manually. The created key
 * object, when not <code>null</code>, will be eligible for dependency injection (with CDI).
 * <p>
 * This contract overrides the <code>key*</code> attributes of {@link TreeNode} annotation when present on the same tree
 * node object.
 * 
 * @author Martti Tamm
 */
public interface KeyBuilder {

  /**
   * Builds and returns the key object for the same tree node as the implementing value object. The created key object,
   * when not <code>null</code>, will be eligible for dependency injection (with CDI).
   * 
   * @return The key object for the tree node. May be <code>null</code>.
   */
  Object buildKey();

}
