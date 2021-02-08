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

import java.util.UUID;

/**
 * The item stored inside Inventory. Each item sold by a business can be encapsulated in an instance
 * of this class. InventoryItem contains vital metadata about the item being sold by the business.
 */
public class InventoryItem {
  private String id;
  private String title;
  private String mediaUrl;
  private double price;

  public InventoryItem(String itemTitle, String itemMediaURL) {
    this.id = UUID.nameUUIDFromBytes(itemTitle.getBytes()).toString();
    this.title = itemTitle;
    this.mediaUrl = itemMediaURL;
  }

  public InventoryItem(String itemTitle, String itemMediaURL, double itemPrice) {
    this(itemTitle, itemMediaURL);
    this.price = itemPrice;
  }

  /**
   * Gets the item's unique identifier.
   *
   * @return id The unique id associated with this item.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Gets the item's title.
   *
   * @return title The title of the item.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Gets the item's price.
   *
   * @return price The price of the item.
   */
  public double getPrice() {
    return this.price;
  }

  /**
   * Gets the url at which the item's image is located.
   *
   * @return mediaUrl The url that leads to the image of this item.
   */
  public String getMediaUrl() {
    return this.mediaUrl;
  }
}
