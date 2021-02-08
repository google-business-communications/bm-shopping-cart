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
package com.google.businessmessages.cart.servlets;

// [START callback for receiving consumer messages]

// [START import_libraries]
import com.google.api.services.businessmessages.v1.model.BusinessMessagesRepresentative;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.businessmessages.cart.BotConstants;
import com.google.businessmessages.cart.CartBot;
import com.google.communications.businessmessages.v1.RepresentativeType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// [END import_libraries]

/** Servlet for starting the conversation with the bot. */
@WebServlet(name = "AgentCallback", value = "/callback")
public class AgentCallback extends HttpServlet {

  private static final Logger logger = Logger.getLogger(AgentCallback.class.getName());

  public AgentCallback() {
    super();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // set the response type to JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String jsonResponse =
        request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    logger.info(jsonResponse);

    // Load the JSON string into a Json parser
    JsonParser parser = new JsonParser();
    JsonObject obj = parser.parse(jsonResponse).getAsJsonObject();

    // Parse incoming request
    String conversationId = obj.get("conversationId").getAsString();

    // Use memcache to de-dupe messages
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    if (obj.has("message")) {
      String message = obj.get("message").getAsJsonObject().get("text").getAsString();
      String messageId = obj.get("message").getAsJsonObject().get("messageId").getAsString();

      // Check to see if this message has already been seen, if so, ignore
      if (!syncCache.contains(messageId)) {
        syncCache.put(messageId, true);

        routeTextResponse(conversationId, message);
      }
    } else if (obj.has("requestId")) {
      String requestId = obj.get("requestId").getAsString();

      // Check to see if this response has already been seen, if so, ignore
      if (!syncCache.contains(requestId)) {
        syncCache.put(requestId, true);

        handleNonMessageResponse(obj, conversationId);
      } else {
        logger.info("Request ID found in the cache.");
      }
    } else { // Survey responses
      handleNonMessageResponse(obj, conversationId);
    }
  }

  /**
   * Returns a Business Messages Representative
   *
   * @return BusinessMessagesRepresentative for the conversation.
   */
  private BusinessMessagesRepresentative getRepresentative() {
    return new BusinessMessagesRepresentative()
      .setRepresentativeType(RepresentativeType.BOT.toString())
      .setDisplayName(BotConstants.BOT_AGENT_NAME);
  }

    /**
   * Handles events received by the Business Messages platform
   */
  private void handleNonMessageResponse(JsonObject obj, String conversationId) {
    if (obj.has("suggestionResponse")) {
      String postbackData =
          obj.get("suggestionResponse").getAsJsonObject().get("postbackData").getAsString();

      routeTextResponse(conversationId, postbackData);
    } else if (obj.has("userStatus")) {
      obj = obj.get("userStatus").getAsJsonObject();

      if (obj.has("isTyping")) {
        logger.info("User is typing");
      }
    } else if (obj.has("receipts")) {
      JsonArray receipts = obj.get("receipts").getAsJsonObject().getAsJsonArray("receipts");

      for (JsonElement element : receipts) {
        String receiptType = element.getAsJsonObject().get("receiptType").getAsString();
        String messageId = element.getAsJsonObject().get("message").getAsString();

        logger.info("Receipt: (" + receiptType + ", " + messageId + ")");
      }
    }
  }

  private void routeTextResponse(String conversationId, String message) {
    new CartBot(getRepresentative()).routeMessage(message, conversationId);
  }
}
// [END callback for receiving consumer messages]
