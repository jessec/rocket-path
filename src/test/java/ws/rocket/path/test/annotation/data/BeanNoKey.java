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
 * The value of a node used in testing the annotations based tree construction. This bean does not declare any key.
 *
 * @author Martti Tamm
 */
@TreeNode(childTypes = { BeanNoChildren.class, BeanNoData.class, BeanLeaf.class })
public final class BeanNoKey {

}
