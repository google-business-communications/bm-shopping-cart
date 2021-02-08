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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.businessmessages.cart.Cart;
import com.google.businessmessages.cart.CartItem;
import com.google.businessmessages.cart.CartManager;
import com.google.common.collect.UnmodifiableIterator;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CartManagerTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @Test
  public void testGetCart_isCreatedIfMissing() {
    String testGetCartConversationId = "testGetCartConversationId";

    Cart testCart = CartManager.getOrCreateCart(testGetCartConversationId);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    final Query q =
        new Query("Cart")
            .setFilter(
                new Query.FilterPredicate(
                    "conversation_id", Query.FilterOperator.EQUAL, testGetCartConversationId));
    PreparedQuery pq = datastore.prepare(q);
    List<Entity> resultCart = pq.asList(FetchOptions.Builder.withLimit(1));
    assertThat(resultCart).isNotEmpty();
    assertThat(resultCart.get(0).getProperty("cart_id")).isEqualTo(testCart.getId());
  }

  @Test
  public void testGetCart_getsIfExists() {
    String testGetSavedCartConversationId = "testGetSavedCartConversationId";
    Cart cart = CartManager.getOrCreateCart(testGetSavedCartConversationId);
    String testPopulateItemTitle1 = "testPopulateItemTitle1";
    Entity testPopulateItem1 = new Entity("CartItem");
    testPopulateItem1.setProperty("cart_id", cart.getId());
    testPopulateItem1.setProperty("item_title", testPopulateItemTitle1);
    testPopulateItem1.setProperty("count", 1);
    String testPopulateItemTitle2 = "testPopulateItemTitle2";
    Entity testPopulateItem2 = new Entity("CartItem");
    testPopulateItem2.setProperty("cart_id", cart.getId());
    testPopulateItem2.setProperty("item_title", testPopulateItemTitle2);
    testPopulateItem2.setProperty("count", 1);
    HashSet<String> testItemTitles = new HashSet<>();
    testItemTitles.add(testPopulateItemTitle1);
    testItemTitles.add(testPopulateItemTitle2);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testPopulateItem1);
    datastore.put(testPopulateItem2);

    cart = CartManager.getOrCreateCart(testGetSavedCartConversationId);

    assertThat(cart.getItems().size()).isEqualTo(2);
    for (CartItem currentItem : cart.getItems()) {
      assertThat(currentItem.getTitle()).isIn(testItemTitles);
    }
  }

  @Test
  public void testAddItem() {
    Cart cart = CartManager.getOrCreateCart("testAddConversationId");
    String testAddItemTitle = "testAddItemTitle";
    String testAddItemId = "testAddItemId";

    cart = CartManager.addItem(cart.getId(), testAddItemId, testAddItemTitle);

    assertThat(cart.getItems().size()).isEqualTo(1);
    UnmodifiableIterator<CartItem> iterator = cart.getItems().iterator();
    CartItem currentItem = iterator.next();
    assertThat(currentItem.getTitle()).isEqualTo(testAddItemTitle);
    assertThat(currentItem.getId()).isEqualTo(testAddItemId);
  }

  @Test
  public void testDeleteItem() {
    Cart cart = CartManager.getOrCreateCart("testDeleteConversationId");
    String testDeleteItemId = "testDeleteItemId";
    String testDeleteItemTitle = "testDeleteItemTitle";
    Entity testDeleteItem = new Entity("CartItem");
    testDeleteItem.setProperty("cart_id", cart.getId());
    testDeleteItem.setProperty("item_id", testDeleteItemId);
    testDeleteItem.setProperty("item_title", testDeleteItemTitle);
    testDeleteItem.setProperty("count", 1);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testDeleteItem);

    cart = CartManager.deleteItem(cart.getId(), testDeleteItemId);

    assertThat(cart.getItems()).isEmpty();
  }

  @After
  public void cleanUp() {
    helper.tearDown();
  }
}
