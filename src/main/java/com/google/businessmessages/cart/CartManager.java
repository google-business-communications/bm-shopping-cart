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

import com.google.appengine.api.datastore.Entity;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.UUID;

/**
 * Manages communication between the Cart object and the data layer. Updates Cart data within the
 * data layer and returns a new and updated Cart instance.
 */
public class CartManager {

  /**
   * Gets the existing Cart data associated with the given conversationId if it exists. Otherwise,
   * creates new Cart data. Returns new instance of Cart based on data.
   *
   * @param conversationId The unique id that maps between the agent and the user.
   * @return The new Cart instance constructed with persisted data, if any.
   */
  public static Cart getOrCreateCart(String conversationId) {
    DataManager dataManager = DataManager.getInstance();
    Entity cartEntity = dataManager.getCart(conversationId);
    String cartId;
    if (cartEntity == null) {
      cartId = UUID.randomUUID().toString();
      dataManager.saveCart(conversationId, cartId);
    } else {
      cartId = (String) cartEntity.getProperty(DataManager.PROPERTY_CART_ID);
    }
    return new Cart(cartId, getCartItems(cartId));
  }

  /**
   * Gets the collection of items associated with the given cartId. Can be invoked upon the
   * initialization of a user's cart, or when items are added or deleted from the cart.
   *
   * @param cartId The unique identifier of the cart whose items will be returned.
   * @return The immutable collection of items associated with the given cartId.
   */
  private static ImmutableList<CartItem> getCartItems(String cartId) {
    ImmutableList.Builder<CartItem> builder = new ImmutableList.Builder<>();
    DataManager dataManager = DataManager.getInstance();
    List<Entity> itemList = dataManager.getCartFromData(cartId);
    if (itemList != null) {
      for (Entity ent : itemList) {
        String id = (String) ent.getProperty(DataManager.PROPERTY_ITEM_ID);
        String title = (String) ent.getProperty(DataManager.PROPERTY_ITEM_TITLE);
        int count = ((Long) ent.getProperty(DataManager.PROPERTY_COUNT)).intValue();
        builder.add(new CartItem(id, title, count));
      }
    }
    return builder.build();
  }

  /**
   * Adds the specified item to the cart and then returns a new instance of cart with an updated
   * collection of items in it.
   *
   * @param cartId the unique identifier of the cart this item will be added to.
   * @param itemId The unique identifier of the item being added.
   * @param itemTitle The title of the item being added.
   * @return The new instance of Cart with the updated collection of items.
   */
  public static Cart addItem(String cartId, String itemId, String itemTitle) {
    DataManager dataManager = DataManager.getInstance();
    dataManager.addItemToCart(cartId, itemId, itemTitle);
    return new Cart(cartId, getCartItems(cartId));
  }

  /**
   * Deletes the specified item from the cart and then returns a new instance of cart with an
   * updated collection of items in it.
   *
   * @param cartId The unique identifier of the cart this item will be deleted from.
   * @param itemId The unique identifier of the item being deleted.
   * @return The new instance of Cart with the updated collection of items.
   */
  public static Cart deleteItem(String cartId, String itemId) {
    DataManager dataManager = DataManager.getInstance();
    dataManager.deleteItemFromCart(cartId, itemId);
    return new Cart(cartId, getCartItems(cartId));
  }
}
