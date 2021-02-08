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
import static com.google.common.truth.Truth.assertThat;

import com.google.businessmessages.cart.Inventory;
import com.google.businessmessages.cart.InventoryItem;
import com.google.businessmessages.cart.MockInventory;
import com.google.common.collect.UnmodifiableIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;

public class MockInventoryTest {

  @Test
  public void testGetInventory() {
    Map<String, String> testItemMap = new HashMap<>();
    testItemMap.put("testItem1", "testUrl1");
    testItemMap.put("testItem2", "testUrl2");
    testItemMap.put("testItem3", "testUrl3");
    testItemMap.put("testItem4", "testUrl4");
    Inventory testInventory = new MockInventory(testItemMap);

    Object[] testItemCollection = testInventory.getInventory().toArray();

    assertThat(testItemCollection).hasLength(4);
    for (int i = 0; i < testItemCollection.length; i++) {
      InventoryItem currentItem = (InventoryItem) testItemCollection[i];
      assertThat(testItemMap).containsEntry(currentItem.getTitle(), currentItem.getMediaUrl());
    }
  }

  @Test
  public void testGetItem() {
    Map<String, String> testItemMap = new HashMap<>();
    testItemMap.put("testItem1", "testUrl1");
    Inventory testInventory = new MockInventory(testItemMap);
    UnmodifiableIterator<InventoryItem> iterator = testInventory.getInventory().iterator();
    InventoryItem testItem = iterator.next();

    Optional<InventoryItem> resultItem = testInventory.getItem(testItem.getId());

    assertThat(resultItem.isPresent()).isTrue();
    assertThat(resultItem.get().getId()).isEqualTo(testItem.getId());
    assertThat(resultItem.get().getTitle()).isEqualTo(testItem.getTitle());
    assertThat(resultItem.get().getMediaUrl()).isEqualTo(testItem.getMediaUrl());
  }
}
