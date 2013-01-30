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

package ws.rocket.path.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

/**
 * Annotation used on {@link ws.rocket.path.TreeNode} values (classes) for describing the node to be constructed where
 * the annotated value object will belong to. Since the node value object is known (instance of the annotated class),
 * this annotation describes the key (reference to a key object) and the possible child nodes (references to their value
 * objects).
 * <p>
 * For both node key and child node values it is possible to use either reference by bean name or reference by type.
 * When key attributes are omitted, the tree node key will be <code>null</code>. When children attributes are omitted,
 * the constructed tree node will not have any children.
 * 
 * @see ws.rocket.path.TreeNode
 * @author Martti Tamm
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface TreeNode {

  /**
   * Specifies the node key as a string. This value is used when the value of this attribute is not empty.
   * 
   * @return The node key as a string.
   */
  @Nonbinding
  String key() default "";

  /**
   * Reference to node key object by bean type. This value is used when {@link #key()} is empty and <code>keyType != Object.class</code>. When CDI
   * cannot resolve the type to exactly one bean, tree construction will fail.
   * 
   * @return The CDI bean type for the node key object.
   */
  @Nonbinding
  Class<?> keyType() default Object.class;

  /**
   * Reference to node key object by CDI bean name. This value is used when {@link #key()} is empty, <code>{@link #keyType()} == Object.class</code>, and the
   * value of this attribute is not empty.
   * 
   * @return The CDI bean name for the node key object.
   */
  @Nonbinding
  String keyName() default "";

  /**
   * Reference to the values of child-tree-nodes by CDI bean names. This value is used when <code>childTypes</code> is
   * empty.
   * 
   * @return An array of CDI bean names of value objects for constructing the child-tree-nodes.
   */
  @Nonbinding
  String[] children() default {};

  /**
   * Reference to the values of child-tree-nodes by CDI bean types. This value is used when not empty.
   * 
   * @return An array of CDI bean types of value objects for constructing the child-tree-nodes.
   */
  @Nonbinding
  Class<?>[] childTypes() default {};

}
