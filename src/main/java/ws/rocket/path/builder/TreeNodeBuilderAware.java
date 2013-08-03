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
 * Contract for {@link ws.rocket.path.TreeNode} values that wish to participate in its node initialization when the
 * builder pattern is used. The implementor can further initialize its node by describing its child nodes.
 * 
 * @author Martti Tamm
 */
public interface TreeNodeBuilderAware {

  /**
   * Current tree node initialization using the builder. Implementor may, for example, describe (or manually create)
   * additional child nodes of the current tree node.
   * 
   * @param builder The builder instance for current tree node.
   */
  void initNode(TreeNodeBuilder builder);
}
