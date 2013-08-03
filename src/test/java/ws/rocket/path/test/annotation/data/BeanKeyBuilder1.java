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

package ws.rocket.path.test.annotation.data;

import javax.inject.Named;

import ws.rocket.path.annotation.KeyBuilder;
import ws.rocket.path.annotation.TreeNode;

/**
 * The value of a node used in testing the annotations based tree construction and the {@link KeyBuilder} contract. The
 * key returned by the implemented method must be used when creating the tree node.
 *
 * @author Martti Tamm
 */
@Named("keyBuilder1")
@TreeNode(key = "keyBuilder1")
public final class BeanKeyBuilder1 implements KeyBuilder {

  @Override
  public Object buildKey() {
    return "key:keyBuilder1";
  }
}
