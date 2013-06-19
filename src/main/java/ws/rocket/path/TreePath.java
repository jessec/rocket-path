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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Represents a String-based path (to a tree node) enabling forward and backward iteration over path elements
 * (segments). To construct this object, path as a String object must be given with the separator (unless default
 * separator '/' may be used) for splitting the path into segments.
 * <p>
 * Additionally, some constructors also enable path extension extraction by specifying extension separator (default is
 * '.'). Allowed extensions may be given to restrict what is considered an extension. So when an unknown extension is
 * met, it will remain unextracted from the last path segment.
 * <p>
 * For more advanced extension tuning, a parsed path can be manipulated via {@link #setExtensionToSegment(boolean)} by a
 * path processor that recognizes that a valid extracted extension must still be part of the last path segment. When set
 * to <code>true</code>, the {@link #getNext()} method will return the last path segment together with extension, and
 * {@link #getExtension()} will return <code>null</code>.
 * <p>
 * This class does not support modification of the underlying path, only iterating and printing out previous, following,
 * and full path. Although this class implements the {@link Iterator} contract, it supports iterating in both ways
 * (although, starts from the left).
 * <h3>Note About Parsing</h3>
 * <p>
 * When constructed from a path string using a separator, empty path segments between separators will be ignored. That
 * includes path item separators at the beginning or end of the string. In addition, <code>null</code> and empty string
 * as a path default to empty path.
 * <p>
 * With regard to extensions, when there are multiple extension separators in the last path segment, only the last one
 * counts. When there is no character after the extension separator, the last path segment will remain as it is.
 * Otherwise, the extension will be removed from the path segment together with the separator. However,
 * {@link #getExtension()} method will return the extension without the separator.
 * <h3>Navigating</h3>
 * <p>
 * When iterating over path segments, a zero-based index can be retrieved via {@link #getPosition()}. The total count of
 * path segments is returned by {@link #getPathLength()}. Methods {@link #hasNext()} and {@link #hasPrevious()} can be
 * used for detecting whether navigating forward/backward is possible.
 *
 * @author Martti Tamm
 */
public final class TreePath implements Iterator<String>, Serializable {

  public static final String DEFAULT_PATH_SEPARATOR = "/";

  public static final String DEFAULT_EXTENSION_SEPARATOR = ".";

  private final String[] path;

  private final String extension;

  private final String pathSeparator;

  private final String extensionSeparator;

  private int position = 0;

  private boolean extensionToSegment;

  /**
   * Creates a new tree path iterator that uses default path item separator ('/') to extract path elements. This
   * constructor does not attempt to identify a possible extension in the last path element.
   *
   * @param path The path string to iterate. A <code>null</code> value defaults to empty path.
   */
  public TreePath(String path) {
    this(path, DEFAULT_PATH_SEPARATOR, null, null, true);
  }

  /**
   * Creates a new tree path iterator that uses given path item separator to extract path elements, and given extension
   * separator to extract an optional extension from the last path segment. An empty separator (including
   * <code>null</code>) value means no separator. Empty segments and empty extension will be skipped/removed from the
   * outcome. When an extension is found, it will be removed from the last path segment, and made available through
   * {@link #getExtension()}.
   * <p>
   * This constructor does not place any restrictions on extensions, such as valid extensions or case-sensitivity. There
   * are other constructors for that:
   * <ul>
   * <li>{@link TreePath#TreePath(String, String[], boolean)}, and
   * <li>{@link TreePath#TreePath(String, String, String, String[], boolean)}.
   * </ul>
   *
   * @param path The path string to iterate. A <code>null</code> value defaults to empty path.
   * @param pathSeparator The string to use for resolving path segments. May be <code>null</code> for no path segments.
   * @param extensionSeparator The string to use on last path segment for resolving a possible extension. May be
   *        <code>null</code> for no extension check.
   */
  public TreePath(String path, String pathSeparator, String extensionSeparator) {
    this(path, pathSeparator, extensionSeparator, null, false);
  }

  /**
   * Creates a new tree path iterator that uses default path item separator ('/') to extract path elements, and default
   * extension separator ('.') for identifying an optional extension in the last path segment. Empty segments and empty
   * extension will be skipped/removed from the outcome. When an extension is found, it will be removed from the last
   * path segment, and made available through {@link #getExtension()}.
   * <p>
   * This constructor enables more control over what can be considered an extension by accepting an array of allowed
   * extensions, and a boolean indicating whether extension comparison is case-sensitive or not. When the array is
   * <code>null</code> or empty, all kinds of extensions are allowed. Otherwise, the extension must be one of those in
   * array. To disable extension evaluation completely, use either of constructors:
   * <ul>
   * <li>{@link TreePath#TreePath(String)}, or
   * <li>{@link TreePath#TreePath(String, String, String)} where extension separator is null.
   * </ul>
   * <p>
   * When extension matching is done in case-insensitive manner and the extension matches, the matching extension from
   * the array will be returned by {@link #getExtension()}.
   *
   * @param path The path string to iterate. A <code>null</code> value defaults to empty path.
   * @param allowedExtensions An array of allowed extensions, or <code>null</code>.
   * @param extensionCaseSensitive A boolean that is <code>true</code>, when matching extension to values in the array
   *        must be case-sensitive.
   */
  public TreePath(String path, String[] allowedExtensions, boolean extensionCaseSensitive) {
    this(path, DEFAULT_PATH_SEPARATOR, DEFAULT_EXTENSION_SEPARATOR, allowedExtensions, extensionCaseSensitive);
  }

  /**
   * Creates a new tree path iterator that uses given path item separator to extract path elements, and given extension
   * separator to extract an optional extension from the last path segment. An empty separator (including
   * <code>null</code>) value means no separator. Empty segments and empty extension will be skipped/removed from the
   * outcome. When an extension is found, it will be removed from the last path segment, and made available through
   * {@link #getExtension()}.
   * <p>
   * This constructor enables more control over what can be considered an extension by accepting an array of allowed
   * extensions, and a boolean indicating whether extension comparison is case-sensitive or not. When the array is
   * <code>null</code> or empty, all kinds of extensions are allowed. Otherwise, the extension must be one of those in
   * array. (When extension separator is <code>null</code>, the array and the boolean parameters are ignored).
   * <p>
   * When extension matching is done in case-insensitive manner and the extension matches, the matching extension from
   * the array will be returned by {@link #getExtension()}.
   *
   * @param path The path string to iterate. A <code>null</code> value defaults to empty path.
   * @param pathSeparator The string to use for resolving path segments. May be <code>null</code> for no path segments.
   * @param extensionSeparator The string to use on last path segment for resolving a possible extension. May be
   *        <code>null</code> for no extension check.
   * @param allowedExtensions An array of allowed extensions, or <code>null</code>.
   * @param extensionCaseSensitive A boolean that is <code>true</code>, when matching extension to values in the array
   *        must be case-sensitive.
   */
  public TreePath(String path, String pathSeparator, String extensionSeparator, String[] allowedExtensions,
      boolean extensionCaseSensitive) {

    this.pathSeparator = pathSeparator;
    this.extensionSeparator = extensionSeparator;

    boolean separatePathSegments = this.pathSeparator != null && this.pathSeparator.length() > 0;
    boolean separateExtension = this.extensionSeparator != null && this.extensionSeparator.length() > 0;
    boolean validateExtension = allowedExtensions != null && allowedExtensions.length > 0;

    // Initialize an array of path segments:
    if (path == null || path.length() == 0) {
      this.path = new String[0];
    } else if (!separatePathSegments) {
      this.path = new String[1];
      this.path[0] = path;
    } else {
      StringTokenizer st = new StringTokenizer(path, this.pathSeparator);
      List<String> tokens = new ArrayList<String>(st.countTokens());

      while (st.hasMoreTokens()) {
        String token = st.nextToken();
        if (token != null && token.length() > 0) {
          tokens.add(token);
        }
      }

      this.path = tokens.toArray(new String[tokens.size()]);
    }

    // Initialize extension from the last path segment
    String ext = null;
    int lastPathIndex = this.path.length - 1;

    if (separateExtension && lastPathIndex >= 0 && this.path[lastPathIndex].contains(this.extensionSeparator)) {
      String pathSegment = this.path[lastPathIndex];
      int separatorIndex = pathSegment.lastIndexOf(this.extensionSeparator);

      ext = pathSegment.substring(separatorIndex + 1);

      // Validate, if extension is allowed:
      if (validateExtension) {
        boolean extAllowed = false;

        for (String allowedExtension : allowedExtensions) {
          if (extensionCaseSensitive) {
            extAllowed = ext.equals(allowedExtension);
          } else {
            extAllowed = ext.equalsIgnoreCase(allowedExtension);
          }

          if (extAllowed) {
            ext = allowedExtension;
            break;
          }
        }

        if (!extAllowed) {
          ext = null;
        }
      }

      // Trim extension with extension separator from the last path segment.
      if (ext != null) {
        this.path[lastPathIndex] = pathSegment.substring(0, separatorIndex);
      }
    }

    this.extension = ext;
  }

  /**
   * This is a private constructor used internally when initializing a new tree path and the state variables have been
   * explicitly resolved. Therefore, this constructor allows them to be explicitly set, however, it still validates the
   * parameters as described.
   *
   * @param path An array of path segments. Must not be <code>null</code>. The values must not be null and must not
   *        contain the path separator.
   * @param extension An optional extension of the path. (Empty string defaults to <code>null</code>.)
   * @param pathSeparator A path separator to use. May be <code>null</code> in which case the path array must not have
   *        longer length than 1. (Empty string defaults to <code>null</code>.)
   * @param extensionSeparator An extension separator to use. May be <code>null</code> in which case the extension must
   *        also be <code>null</code>. (Empty string defaults to <code>null</code>.)
   */
  private TreePath(String[] path, String extension, String pathSeparator, String extensionSeparator) {
    boolean emptyPathSep = pathSeparator == null || pathSeparator.length() == 0;
    boolean emptyExtSep = extensionSeparator == null || extensionSeparator.length() == 0;
    boolean emptyExtension = extension == null || extension.length() == 0;

    if (path == null) {
      throw new RuntimeException("Path segments array must not be null");
    } else if (Arrays.asList(path).contains(null)) {
      throw new RuntimeException("Path segments array must not contain a null value");
    } else if (emptyPathSep && path.length > 1) {
      throw new RuntimeException("When path segments separator is null, the array must not contain more than 1 value");
    } else if (emptyExtSep && !emptyExtension) {
      throw new RuntimeException("Extension separator must not be null when extension is not null.");
    }

    this.path = path;
    this.extension = emptyExtension ? null : extension;
    this.pathSeparator = emptyPathSep ? null : pathSeparator;
    this.extensionSeparator = emptyExtSep ? null : extensionSeparator;
  }

  @Override
  public boolean hasNext() {
    return this.position < this.path.length;
  }

  /**
   * Specifies whether the current path iterator can return the previous path element using {@link #previous()}.
   *
   * @return A Boolean that is <code>true</code> when the {@link #previous()} method can return the previous path
   *         element.
   */
  public boolean hasPrevious() {
    return this.position > 0;
  }

  @Override
  public String next() {
    String result = getNext();
    this.position++;
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
    this.position--;
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
   * Provides the zero-based index of the current path segment. When path is created, this position is initially 0.
   *
   * @return An integer from 0 to {@link #getPathLength()}.
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * Provides the path segment count of this path object.
   *
   * @return A non-negative integer indicating the path segment count.
   */
  public int getPathLength() {
    return this.path.length;
  }

  /**
   * Reports whether this path contains no path segments.
   *
   * @return A Boolean that is <code>true</code> when the path has no segments.
   */
  public boolean isPathEmpty() {
    return this.path.length == 0;
  }

  /**
   * Provides the extension that was extracted from the last path segment. May return an empty string when the last path
   * segment ended with the separator. Returns <code>null</code> when no extension is present on the last path segment.
   *
   * @return A string with the extension, or <code>null</code> when it's not present.
   */
  public String getExtension() {
    return this.extensionToSegment ? null : this.extension;
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
    return getPathSegment(this.position);
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
    return getPathSegment(this.position - 1);
  }

  /**
   * Provides the (sub)path from the beginning of the path until the current path element (excluded).
   * <p>
   * When the current position is at the beginning of the path or path is empty, the method will return an empty string.
   *
   * @return A String of the composed path.
   */
  public String getPreviousPath() {
    return getPath(0, this.position);
  }

  /**
   * Provides the (sub)path from the the current path element (excluded) until the end of the path.
   * <p>
   * * When the current position is at the end of the path or path is empty, the method will return an empty string.
   *
   * @return A String of the composed path.
   */
  public String getFollowingPath() {
    return getPath(this.position + 1, this.path.length);
  }

  /**
   * Provides the (sub)path from the beginning of the path until the current path element (included).
   * <p>
   * When the path is empty, the method will return an empty string.
   *
   * @return A String of the composed path.
   */
  public String getPathToCurrent() {
    return getPath(0, this.position + 1);
  }

  /**
   * Provides the (sub)path from the the current path element (included) until the end of the path.
   * <p>
   * When the path is empty, the method will return an empty string.
   *
   * @return A String of the composed path.
   */
  public String getPathFromCurrent() {
    return getPath(this.position, this.path.length);
  }

  private String getPath(int from, int to) {
    StringBuilder sb = new StringBuilder();

    for (int i = from; i < to; i++) {
      if (this.pathSeparator != null) {
        sb.append(this.pathSeparator);
      }
      sb.append(this.path[i]);
    }

    if (sb.length() > 0 && this.extensionSeparator != null && this.extension != null && to == this.path.length) {
      sb.append(this.extensionSeparator).append(this.extension);
    }

    return sb.toString();
  }

  private String getPathSegment(int pos) {
    String segment = this.path[pos];

    if (pos == this.path.length - 1 && this.extensionToSegment && this.extension != null) {
      segment += this.extensionSeparator + this.extension;
    }

    return segment;
  }

  @Override
  public String toString() {
    return getPath(0, this.path.length);
  }

  /**
   * Moves the path segment cursor (position) to the first path segment. This method does not alter the path itself.
   *
   * @return Reference to the current tree path instance.
   */
  public TreePath beginning() {
    this.position = 0;
    return this;
  }

  /**
   * Moves the path segment cursor (position) to the last path segment. This method does not alter the path itself.
   *
   * @return Reference to the current tree path instance.
   */
  public TreePath end() {
    this.position = this.path.length;
    return this;
  }

  /**
   * Creates a new instance where the path will consist of this tree path and the suffix tree path (in this order). If
   * this tree path contains extension, it will be discarded. The returned path will have the same extension as the
   * suffix path. For this method to work, both tree path objects must use the same path separator, or a runtime
   * exception will be thrown.
   * <p>
   * This method will not alter the states of current and suffix tree paths.
   * <p>
   * The returned path will have its path segment cursor at the beginning of the path.
   *
   * @param suffixPath The path to append to the current path.
   * @return A new instance of tree path where the path is the sum of current path and suffix path. Its extension, path
   *         and extension separators will be derived from the suffix path.
   */
  public TreePath append(TreePath suffixPath) {
    if (this.pathSeparator == null && suffixPath.pathSeparator != null || this.pathSeparator != null
        && !this.pathSeparator.equals(suffixPath.pathSeparator)) {
      throw new IllegalArgumentException("Cannot append tree path that uses different path segment separator.");
    }

    String[] path = new String[this.path.length + suffixPath.path.length];
    System.arraycopy(this.path, 0, path, 0, this.path.length);
    System.arraycopy(suffixPath.path, 0, path, this.path.length, suffixPath.path.length);

    TreePath result = new TreePath(path, suffixPath.extension, suffixPath.pathSeparator, suffixPath.extensionSeparator);
    result.position = getPathLength();
    return result;
  }

  /**
   * Reports whether this path instance will include the resolved extension as part of the last segment even if the
   * extension was found and extracted. By default, this method will always return <code>false</code>, unless modified
   * by {@link #setExtensionToSegment(boolean)}.
   * <p>
   * When this method returns <code>true</code>, it means that in cases when extension was found, successfully
   * validated, and extracted, the extension would still be appear as it were part of the last path segment (and
   * {@link #getExtension()} will return <code>null</code>).
   * <p>
   * This is a feature enabling better handling of cases where extensions are expected and yet extension is also
   * optional. Although, an extension may be valid, sometimes the extension may still be part of the resource name. For
   * example, "data.json" may look as it were a name of file with data in JSON format. However, an application may
   * figure out that it's just a bad name of a file with full name "data.json.pdf", which is possibly a binary version
   * of the JSON data. In that case, the application may alter the {@link #isExtensionToSegment()} property to remember
   * that the extracted extension should actually be treated as part of the path segment name.
   *
   * @return A Boolean that is <code>true</code> when extension info is suppressed and extension will appear as the
   *         suffix of last path segment.
   */
  public boolean isExtensionToSegment() {
    return extensionToSegment;
  }

  /**
   * Alters whether possibly extracted extension should be treated as an extension or as the suffix of the last path
   * segment. By default, {@link #isExtensionToSegment()} is <code>false</code>.
   *
   * @param extensionToSegment A Boolean that is <code>true</code> when the extacted extension should be overridden and
   *        treated as suffix of the last path segment.
   * @see #isExtensionToSegment()
   */
  public void setExtensionToSegment(boolean extensionToSegment) {
    this.extensionToSegment = extensionToSegment;
  }

}
