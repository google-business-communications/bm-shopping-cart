BUSINESS MESSAGES: SHOPPING CART SAMPLE

This Business Messages agent demonstrates how a shopping cart may be built in a conversational surface
leveraging the functionality and platform of the [Business Messages API](https://businessmessages.googleapis.com)
to facilitate messaging.

This project is setup to run on the Google App Engine.

See the Google App Engine (https://cloud.google.com/appengine/docs/java/) standard environment
documentation for more detailed instructions.

PREREQUISITES

You must have the following software installed on your development machine:

* [Apache Maven](http://maven.apache.org) 3.3.9 or greater
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Google App Engine SDK for Java](https://cloud.google.com/appengine/docs/standard/java/download)

SETUP

Set up the Business Messages agent:

1. Open Google Cloud Console (https://console.cloud.google.com/) with your Business Messages Platform Google account and create a new project for your agent.

Note the Project ID and Project number.

2. Click the menu icon in the top left, then select APIs & Services > Library.

3. Search for and enable "Business Messages API".

4. Register this sample as an agent. See "Register an agent"
(https://developers.google.com/business-communications/business-messages/guides/set-up/agent).

The webhook URL will be https://PROJECT_ID.appspot.com/callback

Where PROJECT_ID is the project ID for the project you created when you registered for Business Messages.

5. Create a service account.

    1. Navigate to [Credentials](https://console.cloud.google.com/apis/credentials).

    2. Click **Create service account**.

    3. For **Service account name**, enter your agent's name, then click **Create**.

    4. For **Select a role**, choose **Project** > **Editor**, the click **Continue**.

    5. Under **Create key**, choose **JSON**, then click **Create**.

       Your browser downloads the service account key. Store it in a secure location.

    6. Click **Done**.

6. Copy the JSON credentials file into the /src/main/resources folder and rename
it to "bm-agent-service-account-credentials.json".

RUN THE SAMPLE

1. In a terminal, navigate to this sample's root directory.

2. Run the following commands:

    gcloud config set project PROJECT_ID

    Where PROJECT_ID is the project ID for the project you created when you registered for
    Business Messages.

    mvn appengine:deploy

3. On your mobile device, use the test business URL that you received when you registered your agent
to open a conversation with your agent and type in "Hello". Once delivered, you should receive
"Hello" back from the agent. Type "help" to tap the Help suggestion to explore other functionality.
