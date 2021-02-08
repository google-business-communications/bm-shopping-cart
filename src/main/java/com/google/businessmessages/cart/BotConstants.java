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

import java.util.HashMap;
import java.util.Map;

/** Defines constants used by CartBot. */
public interface BotConstants {

  String CREDENTIALS_FILE_NAME = "bm-agent-service-account-credentials.json";

  // the URL for the API endpoint
  String BM_API_URL = "https://businessmessages.googleapis.com/";

  String BOT_AGENT_NAME = "BM Cart Bot";

  // List of suggestion strings
  String VIEW_CART_TEXT = "View Cart";
  String CONTINUE_SHOPPING_TEXT = "Continue Shopping";
  String SHOP_TEXT = "Shop Our Collection";
  String HOURS_TEXT = "Inquire About Hours";
  String HELP_TEXT = "Help";
  String ADD_ITEM_TEXT = "\uD83D\uDED2 Add to Cart";
  String INCREMENT_COUNT_TEXT = "\u2795";
  String DECREMENT_COUNT_TEXT = "\u2796";

  // List of recognized commands to produce certain responses
  String DELETE_ITEM_COMMAND = "del-cart-";
  String ADD_ITEM_COMMAND = "add-cart-";
  String VIEW_CART_COMMAND = "cart";
  String HOURS_COMMAND = "hours";
  String SHOP_COMMAND = "shop";
  String HELP_COMMAND = "^help.*|^commands\\s.*|see the help menu";

  // List of pre-programmed responses
  String RSP_DEFAULT =
      "Sorry, I didn't quite get that. Perhaps you were looking for one of these options?";

  String RSP_HOURS_TEXT = "We are open Monday - Friday from 9 A.M. to 5 P.M.";

  String RSP_HELP_TEXT =
      "Welcome to the help menu! This program will echo any text that you enter that is not part"
          + " of a supported command. The supported commands are: \n\n"
          + "Help - Shows the list of supported commands and functions\n\n"
          + "Inquire About Hours - Will respond with the times that our store is open.\n\n"
          + "Shop Our Collection/Continue Shopping - Will respond with a collection of mock"
          + " inventory items.\n\n"
          + "View Cart - Will respond with all of the items in your cart.\n\n";

  Map<String, String> INVENTORY_IMAGES =
      new HashMap<String, String>() {
        {
          put(
              "Blue Running Shoes",
              "https://firebasestorage.googleapis.com/v0/b/bm-shopping-cart-ycsl.appspot.com/o/blue_running_shoes.jpg?alt=media");
          put(
              "Neon Running Shoes",
              "https://firebasestorage.googleapis.com/v0/b/bm-shopping-cart-ycsl.appspot.com/o/neon_running_shoes.jpg?alt=media");
          put(
              "Pink Running Shoes",
              "https://firebasestorage.googleapis.com/v0/b/bm-shopping-cart-ycsl.appspot.com/o/pink_running_shoes.jpg?alt=media");
          put(
              "Teal Running Shoes",
              "https://firebasestorage.googleapis.com/v0/b/bm-shopping-cart-ycsl.appspot.com/o/teal_running_shoes.jpg?alt=media");
          put(
              "White Running Shoes",
              "https://firebasestorage.googleapis.com/v0/b/bm-shopping-cart-ycsl.appspot.com/o/white_running_shoe.jpg?alt=media");
        }
      };
}
