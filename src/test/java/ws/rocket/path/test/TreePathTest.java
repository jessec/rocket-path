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
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ws.rocket.path.TreePath;

/**
 * Test-cases for {@link TreePathTest} class.
 *
 * @author Martti Tamm
 */
public final class TreePathTest {

  private static final String[] ALLOWED_EXTS = { "txt", "html", "RSS" };

  {
    Arrays.sort(ALLOWED_EXTS);
  }

  /**
   * Test data for trying out different constructions of <code>TreePath</code>s.
   * <p>
   * Parameters:
   * <ol>
   * <li><code>TreePath</code> to test,
   * <li>expected <code>toString()</code> value,
   * <li>expected last path segment value (<code>null</code> means empty path),
   * <li>expected extension value,
   * <li>expected path length.
   * </ol>
   *
   * @return Test data.
   */
  @DataProvider(name = "TreePathDataProvider")
  public Object[][] getTreePathsData() {
    final String[] extEmpty = new String[0];
    final String[] rssLower = { "rss" };
    final String[] rssUpper = { "RSS" };
    final String[] htmlLower = { "html" };
    final String[] htmlUpper = { "HTML" };

    return new Object[][] {
      // Testing the simplest constructor (it ; no extension info)
      // 1. path is segmented using default separator
      // 2. no extension is extracted nor validated
      { new TreePath(null), "", null, null, 0 },
      { new TreePath(""), "", null, null, 0 },
      { new TreePath("/path/to////my/web-page.rss"), "/path/to/my/web-page.rss", "web-page.rss", null, 4 },
      { new TreePath("rel.path.to.my/web.page-html"), "/rel.path.to.my/web.page-html", "web.page-html", null, 2 },

      // Testing constructor with three parameters
      // 1. applies custom segment & extension separators
      { new TreePath(null, null, null), "", null, null, 0 },
      { new TreePath("", "-", "="), "", null, null, 0 },
      { new TreePath("-path-to----my-web/page=rss", null, null), "-path-to----my-web/page=rss", "-path-to----my-web/page=rss", null, 1 },
      { new TreePath("-path-to----my-web/page=rss", "", ""), "-path-to----my-web/page=rss", "-path-to----my-web/page=rss", null, 1 },
      { new TreePath("-path-to----my-web/page=rss", "-", null), "-path-to-my-web/page=rss", "web/page=rss", null, 4 },
      { new TreePath("rel.path.to.my/web.page-html", ".", null), ".rel.path.to.my/web.page-html", "page-html", null, 5 },
      { new TreePath("-path-to----my-web/page=rss", "-", "="), "-path-to-my-web/page=rss", "web/page", "rss", 4 },
      { new TreePath("rel.path.to.my/web.page-html", ".", "="), ".rel.path.to.my/web.page-html", "page-html", null, 5 },

      // Testing constructor with three parameters:
      // 1. path & extension are interpreted using default separators (correspondingly: '/' and '.')
      // 2. validates extension to match a list of values in the array
      // 3. boolean alters case-sensitivity when matching extension to values in the array.
      { new TreePath(null, null, false), "", null, null, 0 },
      { new TreePath(null, null, true), "", null, null, 0 },
      { new TreePath("", null, false), "", null, null, 0 },
      { new TreePath("", null, true), "", null, null, 0 },
      { new TreePath("/path/to////web-page.rss", extEmpty, false), "/path/to/web-page.rss", "web-page", "rss", 3 },
      { new TreePath("/path/to////web-page.rss", extEmpty, true), "/path/to/web-page.rss", "web-page", "rss", 3 },
      { new TreePath("/path/to////MY/web-page.rss", rssLower, false), "/path/to/MY/web-page.rss", "web-page", "rss", 4 },
      { new TreePath("/path/to////MY/web-page.rss", rssLower, true), "/path/to/MY/web-page.rss", "web-page", "rss", 4 },
      { new TreePath("/path/to////my/web-page.rss", rssUpper, false), "/path/to/my/web-page.RSS", "web-page", "RSS", 4 },
      { new TreePath("/path/to////my/web-page.rss", rssUpper, true), "/path/to/my/web-page.rss", "web-page.rss", null, 4 },

      // Testing constructor with five parameters:
      // 1. applies custom segment & extension separators
      // 2. validates extension to match a list of values in the array
      // 3. boolean alters case-sensitivity when matching extension to values in the array.
      { new TreePath(null, null, null, null, false), "", null, null, 0 },
      { new TreePath(null, null, null, null, true), "", null, null, 0 },
      { new TreePath("", null, null, null, false), "", null, null, 0 },
      { new TreePath("", null, null, null, true), "", null, null, 0 },
      { new TreePath("relpath.to......my/web.page", null, null, null, false), "relpath.to......my/web.page", "relpath.to......my/web.page", null, 1 },
      { new TreePath("relpath.to......my/web.page", "", "", null, false), "relpath.to......my/web.page", "relpath.to......my/web.page", null, 1 },
      { new TreePath("relpath.to......my/web.page", ".", "/", null, false), ".relpath.to.my/web.page", "page", null, 4 },
      { new TreePath("relpath.to......my/web.page", ".", "/", null, true), ".relpath.to.my/web.page", "page", null, 4 },
      { new TreePath("relpath.to......my/web.page", ".", "/", extEmpty, false), ".relpath.to.my/web.page", "page", null, 4 },
      { new TreePath("relpath.to......my/web.page", ".", "/", extEmpty, true), ".relpath.to.my/web.page", "page", null, 4 },
      { new TreePath("relpath.to......my/web", ".", "/", null, false), ".relpath.to.my/web", "my", "web", 3 },
      { new TreePath("relpath.to......my/web", ".", "/", null, true), ".relpath.to.my/web", "my", "web", 3 },
      { new TreePath("relpath.TO......my/web", ".", "/", extEmpty, false), ".relpath.TO.my/web", "my", "web", 3 },
      { new TreePath("relpath.TO......my/web", ".", "/", extEmpty, true), ".relpath.TO.my/web", "my", "web", 3 },
      { new TreePath("rel.path.to......my/web.page/html", ".", "/", htmlLower, false), ".rel.path.to.my/web.page/html", "page", "html", 5 },
      { new TreePath("rel.path.to......my/web.page/html", ".", "/", htmlLower, true), ".rel.path.to.my/web.page/html", "page", "html", 5 },
      { new TreePath("rel.path.to......my/web.page/html", ".", "/", htmlUpper, false), ".rel.path.to.my/web.page/HTML", "page", "HTML", 5 },
      { new TreePath("rel.path.to......my/web.page/html", ".", "/", htmlUpper, true), ".rel.path.to.my/web.page/html", "page/html", null, 5 },
    };
  }

  /**
   * Tests the properties of <code>TreePath</code> to verify its segmentation logic works as expected. The input data is
   * taken from {@link #getTreePathsData()}.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider")
  public void testSegmentation(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    assertEquals(path.getPosition(), 0, "Expected path to be at first position (i.e. 0).");
    assertEquals(path.toString(), toStringPath, "toString() path is not correct.");

    if (lastSegment == null) {
      assertTrue(path.isPathEmpty(), "Path must be empty when last path segment is null.");
      assertNull(path.getExtension(), "Path extension must be null when last path segment is null.");
      assertNull(extension, "Provided extension must be null when provided last path segment is also null.");
    } else {
      assertEquals(path.end().getPrevious(), lastSegment, "Last path segment  is not correct.");
      assertEquals(path.getExtension(), extension, "Path extension is not correct.");

      if (extension != null) {
        path.setExtensionToSegment(true);
        assertNull(path.getExtension(), "Path extension was expected to be moved back to last path segment.");

        String last = path.getPrevious(); // The last path segment.

        assertNotNull(last, "Last path segment must not be null.");
        assertTrue(last.startsWith(lastSegment), "Last path segment must begin with expected last segment.");
        assertTrue(last.endsWith(extension), "Last path segment must end with expected extension");
        assertEquals(last.length(), lastSegment.length() + extension.length() + 1,
            "Length of last path segment must be eqaul to the lengths of expected last segment and extension + 1.");

        path.setExtensionToSegment(false);

        assertEquals(path.getExtension(), extension, "Path extension was expected to be restored.");
        assertEquals(path.getPrevious(), lastSegment, "Last path segment without extension was expected to be restored.");
      }
    }

    assertEquals(path.getPathLength(), expectLength, "Path length not correct.");
  }

  /**
   * Tests forward and backward navigation on the tree path. The input data is taken from {@link #getTreePathsData()}.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider")
  public void testNavigation(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    assertEquals(path.getPosition(), 0, "Expected path to be at first position (i.e. 0).");

    int loopCount = 0;
    String prevSegment = null;

    while (path.hasNext()) {
      loopCount++;
      String segment = path.next();

      assertEquals(path.getPosition(), loopCount, "Position must match to loop count.");
      assertNotEquals(prevSegment, segment, "Previous and current path segment are equal?");

      prevSegment = segment;
    }

    assertEquals(path.getPosition(), path.getPathLength(), "Path position must be at the end.");
    assertEquals(loopCount, path.getPathLength(), "Loop count should be equal to path length.");

    prevSegment = null;

    while (path.hasPrevious()) {
      loopCount--;
      String segment = path.previous();

      assertEquals(path.getPosition(), loopCount, "Position must match to loop count.");
      assertNotEquals(prevSegment, segment, "Previous and current path segment are equal?");

      prevSegment = segment;
    }

    assertEquals(path.getPosition(), 0, "Path position must be at the end.");
    assertEquals(loopCount, 0, "Loop count should be back in zero.");

    assertEquals(path.end().getPosition(), path.getPathLength(), "end() should move to last path position.");
    assertEquals(path.beginning().getPosition(), 0, "beginning() should move to first path position.");
  }

  /**
   * Tests forward and backward navigation on the tree path. The input data is taken from {@link #getTreePathsData()}.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider")
  public void testPreviousFollowingPaths(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    if (path.getPathLength() <= 1) {
      return;
    }

    final String pathSep = toStringPath.substring(0, 1);

    String previousSegment = null;
    String previousPath = null;
    String followingPath = null;

    // This simplifies testing as there's no checking and messing with path extension:
    path.setExtensionToSegment(true);

    while (path.hasNext()) {
      String segment = path.getNext();

      if (previousSegment == null) {
        assertEquals(path.getPreviousPath(), "");
        assertEquals(path.getPathToCurrent().substring(1), segment);
        previousPath = path.getPreviousPath();
        followingPath = path.getFollowingPath();
      } else {
        String currentPrevPath = path.getPreviousPath();
        String currentFollowingPath = path.getFollowingPath();
        String pathFromCurrent = path.getPathFromCurrent();
        String pathToCurrent = path.getPathToCurrent();

        // Validate against full path
        validatePath(toStringPath, currentPrevPath, pathFromCurrent);
        validatePath(toStringPath, pathToCurrent, currentFollowingPath);

        // Validate previous/following path, which excludes current path segment:
        validatePath(currentPrevPath, previousPath, previousSegment);
        validatePath(followingPath.substring(1), segment, currentFollowingPath);

        // Validate previous/following path, which includes current path segment:
        validatePath(pathToCurrent, currentPrevPath, pathSep + segment);
        validatePath(pathFromCurrent, pathSep + segment, currentFollowingPath);

        previousPath = currentPrevPath;
        followingPath = currentFollowingPath;
      }

      previousSegment = path.next();
    }

    assertEquals(path.getFollowingPath(), "");
    assertEquals(path.getPathFromCurrent(), "");
  }

  /**
   * Tests forward and backward navigation on the tree path. The input data is taken from {@link #getTreePathsData()}.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider", expectedExceptions = NoSuchElementException.class)
  public void testIllegalPreviousFailure(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    int pos = path.beginning().getPosition();
    try {
      path.previous();
    } finally {
      assertEquals(path.getPosition(), pos, "Position should not change when no more segments are available");
    }
  }

  /**
   * Tests for a failure when attempting to call the next item at the end of the path.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider", expectedExceptions = NoSuchElementException.class)
  public void testIllegalNextFailure(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    int pos = path.end().getPosition();
    try {
      path.next();
    } finally {
      assertEquals(path.getPosition(), pos, "Position should not change when no more segments are available");
    }
  }

  /**
   * Tests {@link TreePath#append(TreePath)} method.
   *
   * @param path Object to test.
   * @param toStringPath Expected <code>toString()</code> value.
   * @param lastSegment Expected last path segment value.
   * @param extension Expected path extension value.
   * @param expectLength Expected path segments count.
   */
  @Test(dataProvider = "TreePathDataProvider")
  public void testAppendingPaths(TreePath path, String toStringPath, String lastSegment, String extension, int expectLength) {
    // TODO
  }

  private void validatePath(String path, String prefixPath, String endsWith) {
    if (prefixPath != null && prefixPath.length() > 0) {
      assertTrue(path.startsWith(prefixPath), "Path: [" + path + "]; Prefix: [" + prefixPath + "]");
    }
    if (endsWith != null && endsWith.length() > 0) {
      assertTrue(path.endsWith(endsWith), "Path: [" + path + "]; EndsWith: [" + endsWith + "]");
    }
  }
}
