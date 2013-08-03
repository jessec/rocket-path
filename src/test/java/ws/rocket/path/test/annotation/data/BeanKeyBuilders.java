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

import ws.rocket.path.annotation.TreeNode;

/**
 * The value of a node used in testing the annotations based tree construction.
 * <p>
 * This class is also used to test the following:
 * <ol>
 * <li>key object (of the tree node) lookup by bean name (attribute <code>keyName</code>); note that the "root" bean is
 * taken as it is without creating a new tree, though it is annotated with <code>&#64;TreeNode</code>.
 * <li>the <code>childNames</code> attribute is used for creating child-nodes.
 * </ol>
 *
 * @author Martti Tamm
 */
@TreeNode(keyName = "root", childNames = { "keyBuilder1", "keyBuilder2" })
public final class BeanKeyBuilders {

}
