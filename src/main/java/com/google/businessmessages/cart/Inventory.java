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

import com.google.common.collect.ImmutableCollection;
import java.util.Optional;

/**
 * The Inventory is responsible for tracking a business's collection of items and supports two main
 * functions: returning the entire collection of items and returning a particular item instance from
 * the inventory.
 *
 * <p>This interface is currently implemented by MockInventory and will need to be implemented by a
 * similar class for custom use.
 */
public interface Inventory {

  /**
   * Gets the collection of items in the inventory.
   *
   * @return inventoryItems The collection of items in the inventory.
   */
  ImmutableCollection<InventoryItem> getInventory();

  /**
   * Gets the inventory item instance specified by the item id.
   *
   * @param itemId The item's unique identifier.
   * @return item The Optional containing the InventoryItem instance if exists in the inventory,
   *     empty if it does not.
   */
  Optional<InventoryItem> getItem(String itemId);
}
