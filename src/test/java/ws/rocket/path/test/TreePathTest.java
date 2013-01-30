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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import ws.rocket.path.TreePath;

/**
 * Test-cases for {@link TreePathTest} class.
 * 
 * @author Martti Tamm
 */
public final class TreePathTest {

  private static final String TEST_PATH = "/path/to////my/web-page.rss";

  /**
   * Tests that creating <code>TreePath</code> with <code>null</code> results with empty path.
   */
  @Test
  public void testNullPath() {
    TreePath path = new TreePath(null);

    assertEquals(path.getDepth(), 0);
    assertNull(path.getExtension());
    assertFalse(path.hasNext());
    assertFalse(path.hasPrevious());
    assertEquals(path.getPathToCurrent(), "");
    assertEquals(path.getPathFromCurrent(), "");
    assertEquals(path.getPreviousPath(), "");
    assertEquals(path.getFollowingPath(), "");
    assertEquals(path.toString(), "");
  }

  /**
   * Tests whether path segmentation works correctly.
   */
  @Test
  public void testPathSegmentation() {
    TreePath path = new TreePath(TEST_PATH);

    assertEquals(path.getDepth(), 0);
    assertEquals(path.getExtension(), "rss");
    assertFalse(path.hasPrevious());
    assertTrue(path.hasNext());

    String segment = null;

    for (int depth = 0; depth < 4; depth++) {
      assertEquals(path.getDepth(), depth);

      if (!path.hasPrevious()) {
        assertNull(segment);
      } else {
        assertEquals(path.getPrevious(), segment, "The previous path segment is not what expected.");
      }

      segment = path.getNext();

      assertEquals(path.getDepth(), depth);
      assertEquals(path.next(), segment);
    }

    assertFalse(path.hasNext());
  }

  /**
   * Checks that segmented path can be traversed correctly both forward and backward.
   */
  @Test
  public void testPathTraversing() {
    TreePath path = new TreePath(TEST_PATH);

    int segmentPosition = 0;

    while (path.hasNext()) {
      path.next();
      segmentPosition++;
    }

    assertEquals(segmentPosition, 4, "The last segment position is expected to be 4.");
    assertEquals(path.getDepth(), 4, "The path depth is expected to be 4.");

    while (path.hasPrevious()) {
      path.previous();
      segmentPosition--;
    }

    assertEquals(segmentPosition, 0, "Expected to be back at the beginning.");
    assertEquals(path.getDepth(), 0, "Expected to be back at the beginning.");
  }

}
