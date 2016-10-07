import cloud.artik.client.*;
import cloud.artik.client.auth.*;
import cloud.artik.model.MessageAction;
import cloud.artik.model.MessageIDEnvelope;
import cloud.artik.api.MessagesApi;

import java.util.*;
import config.Config;



/**
 * Sample code below will send and retrieve a message to ARTIKCLOUD.
 * 
 * This sample uses the "Demo Fire Sensor" as the device which you can 
 * add via the ARTIK Cloud dashboard.   A random temperature value wil
 * be sent to the device.
 * 
 * Be sure you have filled out your credentials in the Config file which uses
 * a device id / device token to make sample api calls below.
 *
 */
public class SendReceiveMessage {
	
	public static void main(String[] args) {
		
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setDebugging(true);

        // Configure OAuth2 access token for authorization: artikcloud_oauth
        OAuth artikcloud_oauth = (OAuth) defaultClient.getAuthentication(Config.ARTIKCLOUD_OAUTH);
        artikcloud_oauth.setAccessToken(Config.DEVICETOKEN);
        
        // instantiate the MessageAPI to send and receive messages
        MessagesApi messageApiInstance = new MessagesApi();
        
        // The ArtikCloud 2.0.3 SDK library needs a MessageAction data type for sending / receiving messages
        // In upcoming future versions > 2.0.3, this will separate into separate Message and Action classes.
        MessageAction data = new MessageAction(); // Message | Message object that is passed in the body
        data.setSdid(Config.DEVICEID);
        data.setType("message");
        
 
        // Let's prepare a random temp value to send to our sample sensor
        Map <String, Object> myData = new HashMap<String, Object>();
        
        double randomTemperature = Math.floor(Math.random() * 200);
        myData.put("temp", randomTemperature);
        data.setData(myData);
        
       
        // Now lets send our data.   You should receive a message id (mid) in a successful response.
        try {
            MessageIDEnvelope result = messageApiInstance.sendMessageAction(data);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MessagesApi#sendMessage");
            e.printStackTrace();
        }
        
        // Now let's retrieve our data. 
        try {
        	//synchronous call, or alternatively use .getLastNormalizedMessagesAsync()
        	//messageApiInstance.getLastNormalizedMessages(count, sdids, fieldPresence)
        	//@param count <int> - max entries to return
        	//@param sdids <string> - containing list of device ids of interest, comma delimited
        	//@param fieldPresense <string> - retrieval only if device has named field
            messageApiInstance.getLastNormalizedMessages(1, Config.DEVICEID, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling MessagesApi#getLastNormalizedMessages");
            e.printStackTrace();
        }
        
    }
}
