package com.khana.restclient.android;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;


import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jyoti Rai
 *
 */

public class AndroidClient {
	public static String BASE_URL; 
	public static String ON_CHANGED;
	public static String URL;
	public static String SUPPORTED_VERSION;
//	public static String PING;
	public static String VERSION;
//	public static String BELL;
	static HttpClient httpClient;

	enum  SendType {
		ping,
		status,
		supportedVersions,
		onChanged,
		bell,
		restart,
		signOut
	}



	public static synchronized boolean postOnchange(AndroidClientInfo client, String onChanged) {
		if(VERSION==null)
			getVersions(client);
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.onChanged);

		try {     
			if(httpClient==null)
				httpClient  = new HttpClient();
		httpClient.start();
		ContentResponse response = null;
		response =	httpClient.newRequest(ON_CHANGED+onChanged)
        .timeout(10, TimeUnit.SECONDS)
        .send();		
		httpClient.stop();
		if(response.getContentAsString().compareTo("1")==0)
			return true;
		else
			return false;
		
		} catch(Exception ex) {
			return false;

		}
	}
	
	
	public static synchronized boolean postBell(AndroidClientInfo client) {
		if(VERSION==null)
			getVersions(client);
		
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.bell);
		try {     
			if(httpClient==null)
				httpClient  = new HttpClient();
		httpClient.start();
		ContentResponse response = null;
		response = httpClient.newRequest(URL)
        .timeout(10, TimeUnit.SECONDS).send();
		httpClient.stop();
		if(response.getStatus()==200)
			return true;
		else
			return false;
		
		} catch(Exception ex) {
			return false;

		}
	}
	

	public static synchronized  StatusPojo getStatus(AndroidClientInfo client) {
		if(VERSION==null)
			getVersions(client);
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.status);
		try { 
			if(httpClient==null)
				httpClient  = new HttpClient();
			httpClient.start();
			ContentResponse response = null;
			response = httpClient.newRequest(URL)
			        .timeout(10, TimeUnit.SECONDS).send();
			httpClient.stop();
			ObjectMapper om = new ObjectMapper();
			return om.readValue(response.getContentAsString(), StatusPojo.class);

		} catch(Exception ex) {

			return null;
		}
	}


	public static synchronized  boolean postPing(AndroidClientInfo client) {
		if(VERSION==null)
			getVersions(client);
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.ping);
		try { 
			if(httpClient==null)
				httpClient  = new HttpClient();
			httpClient.start();
			ContentResponse response = null;
			response = httpClient.newRequest(URL)
			        .timeout(10, TimeUnit.SECONDS).send();
			httpClient.stop();
			if(response.getStatus()==200)
				return true;
			else
				return false;

		} catch(Exception ex) {

			return false;
		}
	}
	
	
	public static synchronized  boolean restart(AndroidClientInfo client) {
		if(VERSION==null)
			getVersions(client);
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.restart);
		try { 
			if(httpClient==null)
				httpClient  = new HttpClient();
			httpClient.start();
			ContentResponse response = null;
			response = httpClient.newRequest(URL)
			        .timeout(10, TimeUnit.SECONDS).send();
			httpClient.stop();
			
			if(response.getStatus()==200)
				return true;
			else
				return false;

		} catch(Exception ex) {

			return false;
		}
	}
	
	public static synchronized  boolean signOut(AndroidClientInfo client) {
		if(VERSION==null)
			getVersions(client);
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.signOut);
		try {
			if(httpClient==null)
			httpClient  = new HttpClient();
			httpClient.start();
			ContentResponse response = null;
			response = httpClient.newRequest(URL)
			        .timeout(10, TimeUnit.SECONDS).send();
			httpClient.stop();
			
			if(response.getStatus()==200)
				return true;
			else
				return false;

		} catch(Exception ex) {

			return false;
		}
	}
	

	public static synchronized SupportedVersion getVersions(AndroidClientInfo client) {
		setEndPoint(client.getClientIp(), client.getClientPort(), SendType.supportedVersions);
		try { 
			if(httpClient==null)
				httpClient  = new HttpClient();
			httpClient.start();
			ContentResponse response = null;
			response = httpClient.newRequest(SUPPORTED_VERSION)
			        .timeout(60, TimeUnit.SECONDS).send();
			httpClient.stop();
			ObjectMapper om = new ObjectMapper();
			SupportedVersion sVersion = om.readValue(response.getContentAsString(), SupportedVersion.class);
			VERSION =sVersion.latestVersion;
			return sVersion;
		} catch(Exception ex) {
			return null;
		}
	}


	public static void setEndPoint(String Ip, String Port, SendType type) {
		BASE_URL = "http://"+Ip+":"+Port;
		if(type==SendType.onChanged)
			ON_CHANGED = BASE_URL + "/"+VERSION+"/onChanged/?what=";
		else if(type==SendType.supportedVersions)
			SUPPORTED_VERSION = BASE_URL + "/supportedVersions";
		else
			URL = BASE_URL + "/"+VERSION+"/"+type;
		
		
	}

	/*public static void analyzeResponse(ResponseEntity<String> response, SendType type) {
		HttpStatus statusCode = response.getStatusCode();;
		if(statusCode.is4xxClientError()) {
			System.out.println("Android server is not responding");
			return;
		}

		if(!statusCode.is2xxSuccessful()) {
			System.out.println(type.name()+" sending to android server failed");  
		}
	}*/

}
