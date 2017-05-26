# ARTIK Cloud Java SDK starter code

This sample code sends and gets a message to and from ARTIK Cloud using the [ARTIK Cloud Java/Android SDK](https://github.com/artikcloud/artikcloud-java).

### Prerequisites
* Java 7,8
* ARTIK Cloud Java/Android SDK (version 2.0.7)

### Setup / Installation:

 1. Sign into [My ARTIK Cloud](https://artik.cloud/)
 2. Connect a new device at "my.artik.cloud".  Select the Demo Fire Sensor (from cloud.artik.sample.demofiresensor) and name your sensor SampleFireSensor (or any name you'd like).
 3. Click the Settings icon of the device you just added. Get the **device ID** and **device token**.  Click "Generate Device Token" if needed.
 4. To import project with Eclipse:  File -> Import -> Import as Maven Project 
 5. Add ARTIK Cloud libraries by running "mvn install".   For manual & detailed installation see the [instructions](https://github.com/artikcloud/artikcloud-java) here.  
 5. Add your "Device Id" and "Device Token" to the Config.java file.

## Run the code
 
Run the SendReceiveMessage.java application.

It sends a random temperature value to ARTIK Cloud on behalf of the device, then retrieves the last message it sent.

If sending goes well, you receive a response with a message id (mid). ARTIK Cloud uses this response to acknowledge the receipt of the message as shown below.

```java
//response after sending message
class MessageIDEnvelope {
    data: class MessageID {
        mid: ae3901fd19fb4bd98cee030bedb373d1
    }
}
```

After sending, retrieve the last message with a synchronous call. Below is the response. It has a 'temp' value that was sent earlier.

```java
//response after getting last message
class NormalizedMessage {
    cts: 1475839013635
    ts: 1475839013635
    mid: 4ecc382d3c2044c3b456474bb923bdaa
    sdid: 77519dfc680f45798c3203922087fda8
    sdtid: dt856e54302a294fba80414b87eb7b79a3
    uid: e03d4458bc8b462db048775dc17107f9
    mv: 1
    data: {temp=199.0}
}
```

## Peek into the implementation

Take a closer look at the following files:
* /src/SendReceiveMessage.java 

Import the artikcloud package and needed models:

```java
import cloud.artik.client.*;
import cloud.artik.client.auth.*;
import cloud.artik.model.MessageAction;
import cloud.artik.model.MessageIDEnvelope;
import cloud.artik.model.NormalizedMessage;
import cloud.artik.model.NormalizedMessagesEnvelope;
import cloud.artik.api.MessagesApi;

//and also our Config.java class — did you remember rename the class and fill in your credentials?
import config.Config;
```

Set your client credentials:

```java
ApiClient defaultClient = Configuration.getDefaultApiClient();
defaultClient.setDebugging(true);

// Configure OAuth2 access token for authorization: artikcloud_oauth
OAuth artikcloud_oauth = (OAuth) defaultClient.getAuthentication(Config.ARTIKCLOUD_OAUTH);
artikcloud_oauth.setAccessToken(Config.DEVICETOKEN);
```

The two methods sendMessageAction() and getLastNormalizedMessages() are the part of the MessagesAPI. Lets create an instance of the API first.

```java
// instantiate the MessageAPI to send and receive messages
MessagesApi messageApiInstance = new MessagesApi();
```

To send a message we instantiate the MessageAction data type which either sends a Message or Action.   We will send a message for this example.   Please note that in our future SDK, we plan to separate MessageAction into two discrete class:  Message and Action.

```java
Message data = new Message(); 

//set the device id as the source device and 'message' as the message type.
data.setSdid(Config.DEVICEID);

// Let's prepare a random temp value to send to our sample sensor
Map <String, Object> myData = new HashMap<String, Object>();
double randomTemperature = Math.floor(Math.random() * 200);
myData.put("temp", randomTemperature);
data.setData(myData);

// Now lets send our data.   You should receive a message id (mid) in a successful response.
try {
    MessageIDEnvelope result = messageApiInstance.sendMessage(data);
    System.out.println(result);
```

API call to get message for the device: 

```java
   NormalizedMessagesEnvelope normalizedMessagesEnvelope =
     messageApiInstance.getLastNormalizedMessages(1, Config.DEVICEID, null);
   List<NormalizedMessage> messages = normalizedMessagesEnvelope.getData();
   for(NormalizedMessage message: messages) {
    System.out.println(message);
   }
```

Check out the Messages REST API: 
  - [POST a message REST call](https://developer.artik.cloud/documentation/api-reference/rest-api.html#post-a-message-or-action).  
  - [Get the last message REST call](https://developer.artik.cloud/documentation/api-reference/rest-api.html#get-last-normalized-messages)

## View your data in My ARTIK Cloud

Have you visited ARTIK Cloud [data visualization tool](https://artik.cloud/my/data)?

Select your device from the charts to view your device data in realtime. Try running the application multiple times in your terminal to send a few random values.  Here's a screenshot:

![GitHub Logo](https://github.com/artikcloud/tutorial-python-sdksample/blob/master/img/screenshot-firesensor-datachart.png)

## More examples

 - [ARTIK Cloud Java/Android SDK documentation] (https://github.com/artikcloud/artikcloud-java)
 - [Your first Android app tutorial](https://developer.artik.cloud/documentation/tutorials/your-first-android-app.html)

More about ARTIK Cloud
---------------

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-reference/

Peek into advanced sample applications at https://developer.artik.cloud/documentation/samples/

To create and manage your services and devices on ARTIK Cloud, visit the Developer Dashboard at https://developer.artik.cloud

License and Copyright
---------------------

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2017 Samsung Electronics Co., Ltd.

