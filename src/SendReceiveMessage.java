import cloud.artik.client.*;
import cloud.artik.client.auth.*;
import cloud.artik.model.Message;
import cloud.artik.model.MessageAction;
import cloud.artik.model.MessageIDEnvelope;
import cloud.artik.model.NormalizedMessage;
import cloud.artik.model.NormalizedMessagesEnvelope;
import cloud.artik.api.MessagesApi;

import java.util.*;

/**
 * Sample code below will send and retrieve a message to ARTIKCLOUD
 * and send a random temperature value to the device.
 * 
 * This sample uses the "Demo Fire Sensor" Device Type.
 * Device Type Id:  dt856e54302a294fba80414b87eb7b79a3
 * cloud.artik.sample.demofiresensor
 * 
 * (or create your own Device Type at developer.artik.cloud)
 * 
 * Add above device to your account (https://my.artik.cloud)
 * to retrieve Device Id and Device Token.
 * 
 * Enter Device Id and Device Token into the Config.java file.
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

        // message object that is passed in the body
        Message data = new Message(); 
        data.setSdid(Config.DEVICEID);
         
        // prepare a random temp value to send to our demo sensor
        Map <String, Object> myData = new HashMap<String, Object>();
        
        double randomTemperature = Math.floor(Math.random() * 200);
        myData.put("temp", randomTemperature);
        data.setData(myData);
        
       
        // Now lets send our data.   You should receive a message id (mid) in a successful response.
        try {
            MessageIDEnvelope result = messageApiInstance.sendMessage(data);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MessagesApi#sendMessage");
            e.printStackTrace();
        }
        
        // Now let's retrieve our data. 
        try {
        	
        	//synchronous call example
        	//messageApiInstance.getLastNormalizedMessages(...)
        	
        	//async call example
        	//messageApiInstance.getLastNormalizedMessagesAsync(...)
        	
        	//@param count <int> - max entries to return
        	//@param sdids <string> - containing list of device ids of interest, comma delimited
        	//@param fieldPresense <string> - retrieval only if device has named field
            NormalizedMessagesEnvelope normalizedMessagesEnvelope =
            		messageApiInstance.getLastNormalizedMessages(1, Config.DEVICEID, null);
            
            //each message is wrapped in NormalizedMessage object.  
            //iterate through each messsage and print out some of its values
            List<NormalizedMessage> messages = normalizedMessagesEnvelope.getData();
            for(NormalizedMessage message: messages) {
            	System.out.println(message);
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling MessagesApi#getLastNormalizedMessages");
            e.printStackTrace();
        }
        
    }
}
