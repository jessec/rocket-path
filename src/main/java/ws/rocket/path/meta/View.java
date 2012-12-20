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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.rocket.path.TreeNode;

/**
 * Contract for objects capable of returning an HTTP response. There may be created views for HTML, XML, JSON, redirect,
 * etc response.
 * <p>
 * There is an order in which the view methods are called:
 * <ol>
 * <li>{@link #attr(String, Object)} may be called any number of times to set view data;
 * <li>{@link #setNodes(TreeNode, TreeNode)} is called once to provide the tree nodes, which can be used at rendering;
 * <li>{@link #writeHeaders(HttpServletResponse)} is called once for outputing HTTP status and headers;
 * <li>{@link #writeContent(HttpServletRequest, HttpServletResponse)} may be called once (except for HTTP HEAD request)
 * for outputing HTTP response body.
 * </ol>
 * <p>
 * Implementations should also pay attention to caching (see package <code>ws.rocket.path.meta.http.cache</code>).
 * <p>
 * To better facilitate all possible view implementations, library users should introduce a <code>ViewFactory</code>
 * for their application that request handlers could use.
 * 
 * @author Martti Tamm
 */
public interface View {

  /**
   * Associates a new request attribute with the view. When the view is rendered, the attribute value can be used.
   * <p>
   * Depending on the view type, a view does not have to provide functionality for this method. However, when
   * implemented, the view must not expect to have a request object available at the time this method is called.
   * 
   * @param key The key for the request attribute.
   * @param value The value for the request attribute.
   * @return The current instance of the view.
   */
  View attr(String key, Object value);

  /**
   * Provides the tree root node and the current node for which the view is being rendered. This method is called by
   * request handler before any of the write*() methods is called. Tree node components should not call this method.
   * 
   * @param root The root node of the tree (always present).
   * @param node The requested node to be rendered (can be <code>null</code> in some cases).
   */
  void setNodes(TreeNode root, TreeNode node);

  /**
   * Triggers the view object to write any HTTP headers to the provided response wrapper. At the time this method is
   * called, {@link #setNodes(TreeNode, TreeNode)} is already called.
   * 
   * @param response The response wrapper.
   */
  void writeHeaders(HttpServletResponse response);

  /**
   * Triggers the view object to write HTTP message content (<em>entity</em>) to the provided response wrapper. At the
   * time this method is called, {@link #setNodes(TreeNode, TreeNode)} is already called.
   * 
   * @param request The incoming request object.
   * @param response The response wrapper.
   */
  void writeContent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
