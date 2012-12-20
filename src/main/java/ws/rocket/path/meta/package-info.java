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

/**
 * Semantic contracts for keys and values of tree nodes. These default contracts (interfaces) are not exclusive nor
 * restrictive: API users may define own contracts and use them instead. In addition, these contracts here may be
 * modified in non-backward compatible way from version to version, if it proves necessary. Although contracts were
 * initially designed to be used on values, nothing stops from using them on keys, too. However, the usage of each
 * contract should be consistent.
 * <p>
 * Interfaces declare contracts for classes implementing them and for independent algorithms working with objects
 * implementing them. Contracts also define characteristics for these classes. For example, a class implementing a menu
 * item interface declares to algorithms recognizing the contract that these objects need to be listed in a generated
 * menu; a contract declaring that access to an object is restricted in some cases could make algorithms aware that the
 * object needs go give permission before continuing working with it. All in all, applications may contain a variety of
 * contracts telling algorithms how to work with objects and it is the job of algorithms to handle these contracts
 * correctly.
 * <p>
 * When runtime objects of an application are stored in a semantic tree, having well-known contracts, that describe the
 * functionality of objects, enables developers to write algorithms that perform some actions on tree nodes. These
 * actions do not alter the tree but just deliver some requests to tree node value objects and return the result. It is
 * important that tree is not altered runtime because this enables the use of a single tree for working with requests
 * from different sources (clients, users) and uses memory more efficiently. To be able to do that, the tree should be
 * immutable and thread-safe, therefore actions can be delivered and response handled by threads. And since tree is
 * immutable, a request processing state needs to be handled separately by the algorithm handling the action.
 * <p>
 * A special contract is {@link ws.rocket.path.meta.View}, which is not to be implemented by tree node key or value.
 * Instead a factory should be created capable of creating predefined and common views on demand. This simplifies the
 * management of different views in a single place (the factory).
 * <p>
 * When processing tree nodes, the root node and the node where action was delivered are also provided to the view
 * before it renders response. Therefore, the contracts on tree node keys and values can also be checked and used from
 * the view.
 * <p>
 * All in all, it is important to understand that using contracts on keys and values of tree nodes is a design decision
 * and therefore needs to be evaluated case-by-case. Users of this library are not required to use these predefined
 * contracts for describing their tree elements but are encouraged to do so. Since each application tends to be unique
 * at some aspects, the choice of algorithms and supported contracts needs to be hand-made. Contracts provided here,
 * however, could satisfy the most common needs of applications to begin with.
 * 
 * @see ws.rocket.path.meta.View
 */
package ws.rocket.path.meta;