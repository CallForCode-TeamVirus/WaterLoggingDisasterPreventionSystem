package com.wlpds.service;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendingNotification {
	Logger logger = LoggerFactory.getLogger(SendingNotification.class);
	
	URLConnection myURLConnection=null;
	URL myURL=null;
	BufferedReader reader=null;

	public void sendTextNotification(String message) throws Exception{
		@SuppressWarnings("deprecation")
		String encoded_message=URLEncoder.encode(message);

		StringBuilder sbPostData= new StringBuilder(Constants.NOTIFICATION_ENDPOINT);
		sbPostData.append("authkey="+Constants.NOTIFICATION_API_CREDS);
		sbPostData.append("&mobiles="+Constants.NOTIFICATION_MOBILE);
		sbPostData.append("&message="+encoded_message);
		sbPostData.append("&route="+Constants.NOTIFICATION_ROUTE);
		sbPostData.append("&sender="+Constants.NOTIFICATION_SENDER_ID);

		//final string
		String mainUrl = sbPostData.toString();
		try
		{
			//prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			//reading response
			String response;
			while ((response = reader.readLine()) != null)
				logger.info(response);

			//finally close connection
			reader.close();
		}
		catch (IOException e){
			logger.error(e.getMessage());
			throw e;
		}
	}
}

