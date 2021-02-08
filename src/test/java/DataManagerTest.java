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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.businessmessages.cart.DataManager;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataManagerTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  DataManager datamanager;

  @Before
  public void initDataManager() {
    helper.setUp();
    datamanager = DataManager.getInstance();
  }

  @Test
  public void testSaveCart() {
    String testSaveCartConversationId = "testSaveCartConversationId";
    String testSaveCartId = "testSaveCartId";

    datamanager.saveCart(testSaveCartConversationId, testSaveCartId);

    final Query q =
        new Query("Cart")
            .setFilter(
                new Query.CompositeFilter(
                    CompositeFilterOperator.AND,
                    Arrays.asList(
                        new Query.FilterPredicate(
                            "conversation_id",
                            Query.FilterOperator.EQUAL,
                            testSaveCartConversationId),
                        new Query.FilterPredicate(
                            "cart_id", Query.FilterOperator.EQUAL, testSaveCartId))));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery pq = datastore.prepare(q);
    List<Entity> testCart = pq.asList(FetchOptions.Builder.withLimit(1));
    assertThat(testCart).isNotEmpty();
    assertThat((String) testCart.get(0).getProperty("conversation_id"))
        .isEqualTo(testSaveCartConversationId);
    assertThat((String) testCart.get(0).getProperty("cart_id")).isEqualTo(testSaveCartId);
  }

  @Test
  public void testGetCart() {
    String testGetCartConversationId = "testGetCartConversationId";
    String testGetCartId = "testGetCartId";
    Entity testCart = new Entity("Cart");
    testCart.setProperty("conversation_id", testGetCartConversationId);
    testCart.setProperty("cart_id", testGetCartId);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testCart);

    Entity resultCart = datamanager.getCart(testGetCartConversationId);

    assertThat(resultCart).isNotNull();
    assertThat((String) resultCart.getProperty("conversation_id"))
        .isEqualTo(testGetCartConversationId);
    assertThat((String) resultCart.getProperty("cart_id")).isEqualTo(testGetCartId);
  }

  @Test
  public void testGetExistingItem() {
    String testGetItemCartId = "testGetItemCartId";
    String testGetItemId = "testGetItemId";
    String testGetItemTitle = "testGetItemTitle";
    Entity testGetItem = new Entity("CartItem");
    testGetItem.setProperty("cart_id", testGetItemCartId);
    testGetItem.setProperty("item_id", testGetItemId);
    testGetItem.setProperty("item_title", testGetItemTitle);
    testGetItem.setProperty("count", 1);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testGetItem);

    Entity testResult = datamanager.getExistingItem(testGetItemCartId, testGetItemId);

    assertThat(testResult).isNotNull();
    assertThat((String) testResult.getProperty("cart_id")).isEqualTo(testGetItemCartId);
    assertThat((String) testResult.getProperty("item_id")).isEqualTo(testGetItemId);
    assertThat((String) testResult.getProperty("item_title")).isEqualTo(testGetItemTitle);
    assertThat(((Long) testResult.getProperty("count")).intValue()).isEqualTo(1);
  }

  @Test
  public void testAddItemToCart() {
    String testAddItemCartId = "testAddItemCartId";
    String testAddItemId = "testAddItemId";
    String testAddItemTitle = "testAddItemTitle";

    datamanager.addItemToCart(testAddItemCartId, testAddItemId, testAddItemTitle);

    final Query q =
        new Query("CartItem")
            .setFilter(
                new Query.CompositeFilter(
                    CompositeFilterOperator.AND,
                    Arrays.asList(
                        new Query.FilterPredicate(
                            "cart_id", Query.FilterOperator.EQUAL, testAddItemCartId),
                        new Query.FilterPredicate(
                            "item_id", Query.FilterOperator.EQUAL, testAddItemId))));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery pq = datastore.prepare(q);
    List<Entity> testCart = pq.asList(FetchOptions.Builder.withLimit(1));
    assertThat(testCart).isNotEmpty();
    assertThat((String) testCart.get(0).getProperty("cart_id")).isEqualTo(testAddItemCartId);
    assertThat((String) testCart.get(0).getProperty("item_id")).isEqualTo(testAddItemId);
    assertThat((String) testCart.get(0).getProperty("item_title")).isEqualTo(testAddItemTitle);
    assertThat(((Long) testCart.get(0).getProperty("count")).intValue()).isEqualTo(1);
  }

  @Test
  public void testDeleteItemFromCart() {
    String testDeleteItemCartId = "testDeleteItemCartId";
    String testDeleteItemId = "testDeleteItemId";
    Entity testDeleteItem = new Entity("CartItem");
    testDeleteItem.setProperty("cart_id", testDeleteItemCartId);
    testDeleteItem.setProperty("item_id", testDeleteItemId);
    testDeleteItem.setProperty("count", 1);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testDeleteItem);

    datamanager.deleteItemFromCart(testDeleteItemCartId, testDeleteItemId);

    final Query q =
        new Query("CartItem")
            .setFilter(
                new Query.CompositeFilter(
                    CompositeFilterOperator.AND,
                    Arrays.asList(
                        new Query.FilterPredicate(
                            "cart_id", Query.FilterOperator.EQUAL, testDeleteItemCartId),
                        new Query.FilterPredicate(
                            "item_id", Query.FilterOperator.EQUAL, testDeleteItemId))));
    PreparedQuery pq = datastore.prepare(q);
    List<Entity> testCart = pq.asList(FetchOptions.Builder.withLimit(50));
    assertThat(testCart).isEmpty();
  }

  @Test
  public void testGetCartFromData() {
    String testGetCartId = "testGetCartId";
    String testGetCartItemId1 = "testGetCartItemId1";
    String testGetCartItemId2 = "testGetCartItemId2";
    Entity testGetCartItem1 = new Entity("CartItem");
    testGetCartItem1.setProperty("cart_id", testGetCartId);
    testGetCartItem1.setProperty("item_id", testGetCartItemId1);
    testGetCartItem1.setProperty("count", 1);
    Entity testGetCartItem2 = new Entity("CartItem");
    testGetCartItem2.setProperty("cart_id", testGetCartId);
    testGetCartItem2.setProperty("item_id", testGetCartItemId2);
    testGetCartItem2.setProperty("count", 1);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(testGetCartItem1);
    datastore.put(testGetCartItem2);

    List<Entity> testCart = datamanager.getCartFromData(testGetCartId);

    assertThat(testCart.size()).isEqualTo(2);
  }

  @After
  public void cleanUp() {
    helper.tearDown();
  }
}
