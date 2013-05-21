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

package ws.rocket.path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Default solution for iterating over path elements in a path string. The default separator between path elements is
 * '/' (forward-slash). This class does not support modification of the underlying path.
 * <p>
 * Although this class implements the {@link Iterator} contract, it supports iterating in both ways (although, starts
 * from the left), and provides some useful methods to get more information about the path.
 * 
 * @author Martti Tamm
 */
public final class TreePath implements Iterator<String> {

  private static final String DEFAULT_PATH_SEPARATOR = "/";

  private static final String DEFAULT_EXTENSION_SEPARATOR = ".";

  private final String[] path;

  private final String extension;

  private final String pathSeparator;

  private final String extensionSeparator;

  private int depth = 0;

  /**
   * Creates a new tree path iterator. The default path items separator will be used ('/').
   * 
   * @param path The path string to iterate.
   */
  public TreePath(String path) {
    this(path, DEFAULT_PATH_SEPARATOR, DEFAULT_EXTENSION_SEPARATOR);
  }

  /**
   * Creates a new tree path iterator using given path items separator.
   * 
   * @param path The path string to iterate.
   * @param pathSeparator The separator string to use.
   * @param extensionSeparator The separator string to use on last path segment. May be <code>null</code> for no
   *        extension check.
   */
  public TreePath(String path, String pathSeparator, String extensionSeparator) {
    if (pathSeparator == null || pathSeparator.length() == 0) {
      throw new IllegalArgumentException("Path separator must not be null nor an empty string.");
    }

    this.pathSeparator = pathSeparator;
    this.extensionSeparator = extensionSeparator;

    // Initialize an array of path segments:
    if (path == null) {
      this.path = new String[0];
    } else {
      StringTokenizer st = new StringTokenizer(path, this.pathSeparator);

      this.path = new String[st.countTokens()];

      for (int i = 0; st.hasMoreTokens(); i++) {
        this.path[i] = st.nextToken();
      }
    }

    // Read the extension and remove it from the last path segment:
    if (this.extensionSeparator != null && this.path.length > 0
        && this.path[this.path.length - 1].contains(this.extensionSeparator)) {
      String pathSegment = this.path[this.path.length - 1];
      int separatorIndex = pathSegment.lastIndexOf(this.extensionSeparator);

      this.path[this.path.length - 1] = pathSegment.substring(0, separatorIndex);
      this.extension = pathSegment.substring(separatorIndex + 1);
    } else {
      this.extension = null;
    }
  }

  @Override
  public boolean hasNext() {
    return this.depth < this.path.length;
  }

  /**
   * Specifies whether the current path iterator can return the previous path element using {@link #previous()}.
   * 
   * @return A Boolean that is <code>true</code> when the {@link #previous()} method can return the previous path
   *         element.
   */
  public boolean hasPrevious() {
    return this.depth > 0;
  }

  @Override
  public String next() {
    String result = getNext();
    this.depth++;
    return result;
  }

  /**
   * Returns the previous path element and moves the current path element cursor one position backward (if the iterator
   * is not in the beginning of the path). Otherwise {@link NoSuchElementException} will be thrown.
   * 
   * @return The previous path element.
   * @see #hasPrevious()
   */
  public String previous() {
    String result = getPrevious();
    this.depth--;
    return result;
  }

  public String previousPath() {
    String result = getPreviousPath();
    this.depth = 0;
    return result;
  }

  public String followingPath() {
    String result = getFollowingPath();
    this.depth = this.path.length;
    return result;
  }

  public String pathToCurrent() {
    String result = getPathToCurrent();
    this.depth = 0;
    return result;
  }

  public String pathFromCurrent() {
    String result = getPathFromCurrent();
    this.depth = this.path.length;
    return result;
  }

  /**
   * Not to be used in tree path: results with runtime exception.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Provides the depth of the path where the path element cursor currently is positioned. At first path element, the
   * depth is 0, at second path element, the depth is 1, and so on.
   * 
   * @return A zero-based depth of the path where the path element cursor currently is positioned.
   */
  public int getDepth() {
    return this.depth;
  }

  /**
   * Provides the extension that was extracted from the last path segment. May return an empty string when the last path
   * segment ended with the separator. Returns <code>null</code> when no extension is present on the last path segment.
   * 
   * @return A string with the extension, or <code>null</code> when it's not present.
   */
  public String getExtension() {
    return this.extension;
  }

  /**
   * Returns the following path element (if the iterator is not in the end of the path) without changing the current
   * path element cursor. Otherwise {@link NoSuchElementException} will be thrown.
   * 
   * @return The following path element.
   * @see #hasNext()
   * @see #next()
   */
  public String getNext() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return this.path[this.depth];
  }

  /**
   * Returns the previous path element (if the iterator is not in the beginning of the path) without changing the
   * current path element cursor. Otherwise {@link NoSuchElementException} will be thrown.
   * 
   * @return The previous path element.
   * @see #hasPrevious()
   * @see #previous()
   */
  public String getPrevious() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    return this.path[this.depth - 1];
  }

  /**
   * Provides the (sub)path from the beginning of the path until the current path element (excluded).
   * <p>
   * When cursor is at the first position (the beginning of the path) or path is empty, returns an empty string.
   * 
   * @return A String of the composed path.
   */
  public String getPreviousPath() {
    return getPath(0, this.depth);
  }

  /**
   * Provides the (sub)path from the the current path element (excluded) until the end of the path.
   * <p>
   * When cursor is at the last position (the end of the path) or path is empty, returns an empty string.
   * 
   * @return A String of the composed path.
   */
  public String getFollowingPath() {
    return getPath(this.depth + 1, this.path.length);
  }

  /**
   * Provides the (sub)path from the beginning of the path until the current path element (included).
   * <p>
   * When the path is empty, returns an empty string.
   * 
   * @return A String of the composed path.
   */
  public String getPathToCurrent() {
    return getPath(0, this.depth + 1);
  }

  /**
   * Provides the (sub)path from the the current path element (included) until the end of the path.
   * <p>
   * When the path is empty, returns an empty string.
   * 
   * @return A String of the composed path.
   */
  public String getPathFromCurrent() {
    return getPath(this.depth, this.path.length);
  }

  private String getPath(int from, int to) {
    StringBuilder sb = new StringBuilder();

    for (int i = from; i < to && i < this.path.length; i++) {
      sb.append(this.pathSeparator).append(this.path[i]);
    }

    if (to >= this.path.length && this.extension != null) {
      sb.append(this.extensionSeparator).append(this.extension);
    }

    return sb.toString();
  }

  @Override
  public String toString() {
    return getPath(0, this.path.length);
  }

}
