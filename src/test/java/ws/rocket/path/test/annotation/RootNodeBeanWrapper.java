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

package ws.rocket.path.test.annotation;

import javax.inject.Inject;

import ws.rocket.path.TreeNode;
import ws.rocket.path.annotation.RootNode;

/**
 * A wrapper-class used for dependency injection. Rather than asking CDI the resource directly, CDI injects resource
 * into a field in this wrapper, which is called to get the injected resource.
 * 
 * @author Martti Tamm
 */
public final class RootNodeBeanWrapper {

  @Inject
  @RootNode("root")
  private TreeNode node;

  /**
   * Provides the created and injected {@link TreeNode} (a root node).
   * 
   * @return The injected node.
   */
  public TreeNode getNode() {
    return this.node;
  }
}
