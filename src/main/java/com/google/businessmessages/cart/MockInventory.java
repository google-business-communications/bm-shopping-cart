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
import com.google.common.collect.ImmutableList;
import java.util.Map;
import java.util.Optional;

/**
 * Mock implementation of Inventory. This class pulls items from a pre-defined map of items to
 * demonstrate the "Shop" feature in the bot.
 */
public class MockInventory implements Inventory {
  private ImmutableCollection<InventoryItem> inventoryItems;

  public MockInventory(Map<String, String> nameToMedia) {
    ImmutableCollection.Builder<InventoryItem> builder = new ImmutableList.Builder<>();
    for (Map.Entry<String, String> ent : nameToMedia.entrySet()) {
      builder.add(new InventoryItem(ent.getKey(), ent.getValue()));
    }
    inventoryItems = builder.build();
  }

  @Override
  public ImmutableList<InventoryItem> getInventory() {
    return (ImmutableList<InventoryItem>) inventoryItems;
  }

  @Override
  public Optional<InventoryItem> getItem(String itemId) {
    return inventoryItems.stream().filter(x -> x.getId().equals(itemId)).findFirst();
  }
}
