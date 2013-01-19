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

package ws.rocket.path.support;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.rocket.path.DynamicKey;
import ws.rocket.path.TreeNode;
import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.cache.Expirable;
import ws.rocket.path.meta.http.cache.Modifiable;
import ws.rocket.path.meta.http.cache.Taggable;

/**
 * A factory for creating a standard response view. The factory enlists all kind of response types that can be returned
 * by an application.
 * <p>
 * This class can be used, if suits the needs as it is. However, it stands mostly as an example of how a view factory
 * should look like. Therefore, when the view factory needs to customized, a new view factory should be created,
 * optionally reusing some of the code from here.
 * <p>
 * Note that a view object is never re-used between requests. This is because of multiple threads that may render a view
 * with different data.
 * 
 * @author Martti Tamm
 */
public final class SampleViewFactory {

  /**
   * Main application-level view template.
   */
  private static final String JSP_MAIN = "/WEB-INF/jsp/main.jspx";

  /**
   * Login view template.
   */
  private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jspx";

  private SampleViewFactory() {
    throw new RuntimeException("This class cannot be instantiated.");
  }

  /**
   * Creates a view that renders the main template with the content from provided template.
   * 
   * @param jspPath Optional path to a sub-view to be rendered as main page content.
   * @return The view object that renders the main template with some content.
   */
  public static View main(String jspPath) {
    return new JspView(HttpServletResponse.SC_OK, JSP_MAIN, jspPath);
  }

  /**
   * Creates a view that renders the login template. This method can be extended in the future to accept a redirect URL
   * to be opened after successful login.
   * 
   * @return The view object that renders the login template.
   */
  public static View login() {
    return new JspView(HttpServletResponse.SC_OK, JSP_LOGIN, null);
  }

  /**
   * Creates a view that renders the access denied (HTTP status 403) page. This implementation renders the main
   * application-level template with the access-denied template as its content.
   * <p>
   * Typically, this response view is returned when traversing the requested path, a tree node restricted access to it
   * and its sub-nodes.
   * 
   * @return The view object that renders the access denied page.
   */
  public static View accessDenied() {
    return new JspView(HttpServletResponse.SC_FORBIDDEN, JSP_MAIN, "/WEB-INF/jsp/fail/accessDenied.jspx");
  }

  /**
   * Creates a view that renders the page-not-found (HTTP status 404) page. This implementation renders the main
   * application-level template with the page-not-found template as its content.
   * <p>
   * Typically, this response view is returned when the requested path is not supported by the tree.
   * 
   * @return The view object that renders the page-not-found page.
   */
  public static View notFound() {
    return new JspView(HttpServletResponse.SC_NOT_FOUND, JSP_MAIN, "/WEB-INF/jsp/fail/notFound.jspx");
  }

  /**
   * Creates a view that renders the method-not-allowed (HTTP status 405) page. This implementation renders the main
   * application-level template with the method-not-allowed template as its content.
   * <p>
   * Typically, this response view is returned when the requested resource does not support the method in HTTP request.
   * 
   * @return The view object that renders the method-not-allowed page.
   */
  public static View methodNotAllowed() {
    return new JspView(HttpServletResponse.SC_METHOD_NOT_ALLOWED, JSP_MAIN, "/WEB-INF/jsp/fail/methodNotAllowed.jspx");
  }

  /**
   * Creates a view that renders an application error (HTTP status 500) page. This implementation renders the main
   * application-level template with the application-error template as its content.
   * <p>
   * Typically, this response view is returned when an unexpected runtime exception was detected. The view may render a
   * stack-trace.
   * 
   * @param e The caught exception.
   * @return The view object that renders the application-error page.
   */
  public static View error(Exception e) {
    return new JspView(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, JSP_MAIN, "/WEB-INF/jsp/fail/exception.jspx", e);
  }

  /**
   * Creates a view that sends a redirect (HTTP status 302 &ndash; FOUND) to the provided URL.
   * <p>
   * Typically, this response view is returned when the client needs to be redirected to another page. Note that there
   * are alternative redirects available, too.
   * 
   * @param url URL where the client needs to redirected. Either complete URL, absolute path, or relative path.
   * @return The view object that renders the application-error page.
   */
  public static View redirect(String url) {
    return new RedirectView(url);
  }

  /**
   * Creates a view that prints out all available paths in the tree. This view does nothing with provided attributes.
   * 
   * @return The view object that renders all available paths in the tree.
   */
  public static View simpleLinks() {
    return new SimpleLinksView();
  }

  private abstract static class AbstractView implements View {

    private TreeNode root;

    private TreeNode node;

    private Map<String, Object> attrs = new HashMap<String, Object>();

    @Override
    public View attr(String key, Object value) {
      this.attrs.put(key, value);
      return this;
    }

    @Override
    public final void setNodes(TreeNode root, TreeNode node) {
      this.root = root;
      this.node = node;
    }

    @Override
    public void writeHeaders(HttpServletResponse response) {
      long now = new Date().getTime();
      response.setDateHeader("Date", now);

      Object value = this.node.getValue();

      if (value instanceof Expirable) {
        long maxAge = ((Expirable) value).getMaxAge();
        response.setDateHeader("Expires", now + maxAge);
        response.setHeader("Cache-Control", "max-age=" + maxAge);

      } else if (value instanceof Modifiable) {
        Date lastModified = ((Modifiable) value).getLastModified();
        if (lastModified != null) {
          response.setDateHeader("Last-Modified", lastModified.getTime());
        }

      } else if (value instanceof Taggable) {
        String etag = ((Taggable) value).getETag();
        if (etag != null) {
          response.setHeader("ETag", etag);
        }
      }
    }

    @Override
    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {
      for (Entry<String, Object> entry : this.attrs.entrySet()) {
        request.setAttribute(entry.getKey(), entry.getValue());
      }
      request.setAttribute("rootNode", this.root);
      request.setAttribute("node", this.node);
      this.attrs.clear();
    }

    protected TreeNode getRootNode() {
      return this.root;
    }
  }

  private static class RedirectView implements View {

    private final int statusCode;

    private final String url;

    public RedirectView(String url) {
      this(url, HttpServletResponse.SC_FOUND);
    }

    public RedirectView(String url, int statusCode) {
      this.url = url;
      this.statusCode = statusCode;
    }

    @Override
    public View attr(String key, Object value) {
      return this;
    }

    @Override
    public void setNodes(TreeNode root, TreeNode node) {}

    @Override
    public void writeHeaders(HttpServletResponse response) {
      response.setStatus(this.statusCode);
      response.addHeader("Location", this.url);
    }

    @Override
    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {}
  }

  private static class JspView extends AbstractView {

    private final int statusCode;

    private final String jspPath;

    private final String subJspPath;

    private final Exception exception;

    public JspView(int statusCode, String jspPath, String subJspPath) {
      this(statusCode, jspPath, subJspPath, null);
    }

    public JspView(int statusCode, String jspPath, String subJspPath, Exception e) {
      this.statusCode = statusCode;
      this.jspPath = jspPath;
      this.subJspPath = subJspPath;
      this.exception = e;
    }

    @Override
    public void writeHeaders(HttpServletResponse response) {
      response.setStatus(this.statusCode);
      response.setContentType("text/html");

      if (response.getCharacterEncoding() == null) {
        response.setCharacterEncoding("UTF-8");
      }

      super.writeHeaders(response);
    }

    @Override
    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {
      super.writeContent(request, response);

      if (this.subJspPath != null) {
        request.setAttribute("viewPath", this.subJspPath);
      }

      if (this.exception != null) {
        request.setAttribute("exception", this.exception);
      }

      request.getRequestDispatcher(this.jspPath).forward(request, response);
    }
  }

  private static class SimpleLinksView extends AbstractView {

    private List<String> pathElems = new ArrayList<String>();

    @Override
    public View attr(String key, Object value) {
      return this;
    }

    @Override
    public void writeHeaders(HttpServletResponse response) {
      response.setContentType("text/plain");
      response.setHeader("Cache-Control", "max-age=86400");
    }

    @Override
    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {
      PrintWriter out = response.getWriter();
      out.write("/\n");
      descend(getRootNode(), out);
    }

    private void descend(TreeNode node, PrintWriter out) throws IOException {
      boolean linkWritten = writeLink(node, out);

      for (TreeNode child : node.getChildren()) {
        descend(child, out);
      }

      if (linkWritten) {
        this.pathElems.remove(this.pathElems.size() - 1);
      }
    }

    private boolean writeLink(TreeNode node, PrintWriter out) throws IOException {
      Object key = node.getKey();

      if (key instanceof String) {
        this.pathElems.add((String) key);
      } else if (key instanceof DynamicKey) {
        this.pathElems.add(((DynamicKey) key).getName());
      } else {
        return false;
      }

      for (String pathElem : this.pathElems) {
        out.write('/');
        out.write(pathElem);
      }

      out.write('\n');

      return true;
    }
  }
}
