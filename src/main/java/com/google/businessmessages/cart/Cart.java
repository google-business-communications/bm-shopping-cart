/*
 * Copyright (C) 2020 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.businessmessages.cart;

import com.google.common.collect.ImmutableList;

/**
 * The Cart is responsible for keeping track of all items, CartItems, the user adds to their
 * shopping cart.
 */
public class Cart {
  private final String cartId;
  private final ImmutableList<CartItem> cartItems;

  public Cart(String cartId, ImmutableList<CartItem> cartItems) {
    this.cartId = cartId;
    this.cartItems = cartItems;
  }

  /**
   * Gets the unique id that belongs to this cart.
   *
   * @return cartId The unique id belonging to the cart instance.
   */
  public String getId() {
    return this.cartId;
  }

  /**
   * Gets the collection of items in the cart.
   *
   * @return cartItems The collection of items associated with the cart instance.
   */
  public ImmutableList<CartItem> getItems() {
    return cartItems;
  }
}
