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
 * 
 * @author Martti Tamm
 */
public final class SampleViewFactory {

  private static final String JSP_MAIN = "/WEB-INF/main.jspx";

  private static final String JSP_LOGIN = "/WEB-INF/login.jspx";

  public static View main(String jspPath) {
    return new JspView(HttpServletResponse.SC_OK, JSP_MAIN, jspPath);
  }

  public static View login() {
    return new JspView(HttpServletResponse.SC_OK, JSP_LOGIN, null);
  }

  public static View accessDenied() {
    return new JspView(HttpServletResponse.SC_FORBIDDEN, JSP_MAIN, "/WEB-INF/fail/accessDenied.jspx");
  }

  public static View notFound() {
    return new JspView(HttpServletResponse.SC_NOT_FOUND, JSP_MAIN, "/WEB-INF/fail/notFound.jspx");
  }

  public static View methodNotAllowed() {
    return new JspView(HttpServletResponse.SC_METHOD_NOT_ALLOWED, JSP_MAIN, "/WEB-INF/fail/methodNotAllowed.jspx");
  }

  public static View error(Exception e) {
    return new JspView(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, JSP_MAIN, "/WEB-INF/fail/exception.jspx", e);
  }

  public static View redirect(String url) {
    return new RedirectView(url);
  }

  public static View simpleLinks() {
    return new SimpleLinksView();
  }

  private abstract static class AbstractView implements View {

    private TreeNode root;
    private TreeNode node;
    private Map<String, Object> attrs = new HashMap<String, Object>();

    public View attr(String key, Object value) {
      this.attrs.put(key, value);
      return this;
    }

    public final void setNodes(TreeNode root, TreeNode node) {
      this.root = root;
      this.node = node;
    }

    public void writeHeaders(HttpServletResponse response) {
      long now = new Date().getTime();
      response.setDateHeader("Date", now);

      if (this.node instanceof Expirable) {
        long maxAge = ((Expirable) this.node).getMaxAge();
        response.setDateHeader("Expires", now + maxAge);
        response.setHeader("Cache-Control", "max-age=" + maxAge);

      } else if (this.node instanceof Modifiable) {
        Date lastModified = ((Modifiable) this.node).getLastModified();
        if (lastModified != null) {
          response.setDateHeader("Last-Modified", lastModified.getTime());
        }

      } else if (this.node instanceof Taggable) {
        String etag = ((Taggable) this.node).getETag();
        if (etag != null) {
          response.setHeader("ETag", etag);
        }
      }
    }

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

    public View attr(String key, Object value) {
      return this;
    }

    public void setNodes(TreeNode root, TreeNode node) {
    }

    public void writeHeaders(HttpServletResponse response) {
      response.setStatus(this.statusCode);
      response.addHeader("Location", this.url);
    }

    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {
    }
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

    public void writeHeaders(HttpServletResponse response) {
      response.setStatus(this.statusCode);
      response.setContentType("text/html");
      if (response.getCharacterEncoding() == null) {
        response.setCharacterEncoding("UTF-8");
      }
      super.writeHeaders(response);
    }

    public void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException,
        ServletException {
      super.writeContent(request, response);
      request.setAttribute("viewPath", this.subJspPath);
      request.setAttribute("exception", this.exception);
      request.getRequestDispatcher(this.jspPath).forward(request, response);
    }
  }

  private static class SimpleLinksView extends AbstractView {

    private List<String> pathElems = new ArrayList<String>();

    public View attr(String key, Object value) {
      return this;
    }

    public void writeHeaders(HttpServletResponse response) {
      response.setContentType("text/plain");
      response.setHeader("Cache-Control", "max-age=86400");
    }

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
