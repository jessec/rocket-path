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

package ws.rocket.path.test;

import org.testng.annotations.Test;

import ws.rocket.path.TreePath;

/**
 * Test-cases for {@link TreePathTest} class.
 * 
 * @author Martti Tamm
 */
public final class TreePathTest {

  /**
   * Tests that creating <code>TreePath</code> with <code>null</code> results with null-pointer-exception.
   */
  @Test(expectedExceptions = NullPointerException.class)
  public void testNullPath() {
    new TreePath(null);
  }
}
