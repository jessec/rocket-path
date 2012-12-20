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

package ws.rocket.path.meta;

import java.util.ResourceBundle;

/**
 * A contract for tree node keys or values (which-ever is more preferred) for providing a localized name of the node.
 * The returned name can be rendered in the UI (e.g. as HTML page title).
 * 
 * @author Martti Tamm
 */
public interface Nameable {

  /**
   * Resolves the localized name for the tree node.
   * 
   * @param bundle A resource bundle for the current locale.
   * @return The localized name (may be <code>null</code>).
   */
  String getName(ResourceBundle bundle);

}
