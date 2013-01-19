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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.rocket.path.DynamicKey;
import ws.rocket.path.TreeNode;
import ws.rocket.path.TreePath;
import ws.rocket.path.meta.Restrictable;
import ws.rocket.path.meta.View;
import ws.rocket.path.meta.http.Deletable;
import ws.rocket.path.meta.http.Submitable;
import ws.rocket.path.meta.http.Uploadable;
import ws.rocket.path.meta.http.Viewable;

/**
 * Sample algorithms for delivery of HTTP requests to objects and then handling the response view from the object. This
 * class should not be used in a production code: it is only meant to be taken as an example, API users may copy code
 * from here to use in their application and refine it as needed.
 * <p>
 * This class is marked final and API users should also mark their algorithm implementation class final (no need for
 * sub-classing).
 * 
 * @author Martti Tamm
 */
public final class SampleAlgorithm {

  private final TreeNode root;

  /**
   * Creates a sample algorithm instance for working with given root node.
   * 
   * @param root The root node that this class will use for performing actions.
   */
  public SampleAlgorithm(TreeNode root) {
    this.root = root;
  }

  /**
   * Handles the incoming request and composes a response.
   * 
   * @param request The incoming request wrapper.
   * @param response The response object wrapper.
   * @throws ServletException When composing the response fails.
   * @throws IOException When writing to response stream fails.
   */
  public void deliver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    final String method = request.getMethod();

    TreeNode node;
    View view;

    try {
      node = getNode(request);

      if (node == null) {
        node = this.root;
        view = SampleViewFactory.notFound();
      } else if (("GET".equals(method) || "HEAD".equals(method)) && node.getValue() instanceof Viewable) {
        view = ((Viewable) node.getValue()).doGet(request);
      } else if ("POST".equals(method) && node.getValue() instanceof Submitable) {
        view = ((Submitable) node.getValue()).doPost(request);
      } else if ("PUT".equals(method) && node.getValue() instanceof Uploadable) {
        view = ((Uploadable) node.getValue()).doPut(request);
      } else if ("DELETE".equals(method) && node.getValue() instanceof Deletable) {
        view = ((Deletable) node.getValue()).doDelete(request);
      } else {
        view = SampleViewFactory.methodNotAllowed();
      }
    } catch (AccessDeniedException e) {
      view = SampleViewFactory.accessDenied();
      node = null;
    }

    try {
      if (view != null) {
        view.setNodes(this.root, node);
        view.writeHeaders(response);
        if (!"HEAD".equals(method)) {
          view.writeContent(request, response);
        }
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  /**
   * Uses the HTTP request URI (path info) to lookup a tree node.
   * <p>
   * Notes:
   * <ul>
   * <li>When the target node or an intermediary node is not found, <code>null</code> will be returned.
   * <li>When the target node or an intermediary node value implements {@link Restrictable} and is not available for the
   * request subject, {@link AccessDeniedException} (internal) will be thrown.
   * </ul>
   * 
   * @param request The incoming request.
   * @return The targeted node by path, or <code>null</code> when node cannot be found in the tree.
   * @throws AccessDeniedException When a node restricts user access.
   */
  private TreeNode getNode(HttpServletRequest request) throws AccessDeniedException {
    TreePath path = new TreePath(request.getPathInfo());
    TreeNode node = this.root;

    // We store references to traversed tree nodes so that they could be easily retrieved later.
    List<TreeNode> nodes = new ArrayList<TreeNode>(path.getDepth());
    request.setAttribute("treePath", nodes);

    while (path.hasNext()) {
      nodes.add(node);

      if (node.getValue() instanceof Restrictable && !((Restrictable) node.getValue()).isAvailable(request)) {
        throw new AccessDeniedException();
      }

      String item = path.getNext();
      TreeNode next = null;

      // First we loop over child-nodes detecting next node by key equals-check.
      for (TreeNode child : node.getChildren()) {
        if (item.equals(child.getKey())) {
          next = child;
          break;
        }
      }

      // When first loop does not produce a result, the second loop attempt to match a child-node by DynamicKey
      // contract.
      // This loop may be more expensive (time/resource) depending on the contract implementation.
      if (next == null) {
        for (TreeNode child : node.getChildren()) {
          if (child.getKey() instanceof DynamicKey && ((DynamicKey) child.getKey()).contains(path, request)) {
            next = child;
            break;
          }
        }
      }

      node = next;

      if (node == null) {
        break;
      }

      path.next();
    }

    return node;
  }

  private static class AccessDeniedException extends Exception {

    private static final long serialVersionUID = 1L;

  }
}
