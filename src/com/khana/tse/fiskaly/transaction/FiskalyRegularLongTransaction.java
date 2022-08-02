package com.khana.tse.fiskaly.transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jfree.util.Log;
import org.json.JSONObject;

import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpClient;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.fiskaly.sdk.FiskalyHttpTimeoutException;
import com.khana.tse.fiskaly.AmountsPerPaymentType;
import com.khana.tse.fiskaly.AmountsPerVatRate;
import com.khana.tse.fiskaly.LineItem;

public class FiskalyRegularLongTransaction {
	
	
	private static final String _BASEURL = "https://kassensichv.io/api/v1";
	
	private static String TSS_ID="56ae75b6-f6ea-4254-aa1a-e8b059e75ab9";
	
	private static String CLIENT_ID="fe2eedbe-69a7-4409-ab43-cc7b74d6ec0f";
	private static String CLIENT_ID2 ="4b594883-8f3f-4d64-8320-bcdd22ccc879";
		
	private static UUID TX_ID;
	private static UUID TX_ID2;
	private static UUID TX_ID_RECEIPT;
	
//	private static final String API_KEY = System.getenv("FISKALY_API_KEY");
//	private static final String API_SECRET = System.getenv("FISKALY_API_SECRET");
	
	private static final String API_KEY = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
	private static final String API_SECRET = "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
	
//	private static final String API_KEY = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
//	private static final String API_SECRET = "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
	

	private UUIDGenerator UUID;

	public static void main(String[] args) throws FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		FiskalyRegularLongTransaction obj = new FiskalyRegularLongTransaction();
		
//		obj.createTSS();
//		
//		obj.createClientID();
		
		obj.startReceipt();
		
		obj.startOrder();
		
		obj.updateOrder1();
		
		obj.updateOrder2();
		
		obj.finishOrder();
		
//		obj.createSecondClientID();
		obj.startOrderWithSecondClient();
		obj.updateOrder1WithSecondClient();
		obj.updateOrder2WithSecondClient();
		obj.finishOrderWithSecondClient();
		obj.finishReceipt();
	}
	
	public synchronized FiskalyHttpResponse createTSS() throws FiskalyHttpException, FiskalyClientException, 
		FiskalyHttpTimeoutException, IOException, URISyntaxException{
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "Updated TSS");
		requestBody.put("FiskalyState", "INITIALIZED");
//		this.TSS_ID =  UUID.generateUUID();
		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID, requestBody.toString().getBytes());
			return response;		
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());			
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());
		}
		return null;
	
	}
	
	public synchronized FiskalyHttpResponse createClientID() throws FiskalyHttpException, FiskalyClientException, 
		FiskalyHttpTimeoutException, IOException, URISyntaxException {		
		JSONObject requestBody = new JSONObject();
		requestBody.put("serial_number", UUID.generateUUID());
//		this.CLIENT_ID = UUID.generateUUID();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/client/" + CLIENT_ID, requestBody.toString().getBytes());
		return response;
	}
	
	public synchronized FiskalyHttpResponse createSecondClientID() throws FiskalyHttpException, FiskalyClientException, 
													FiskalyHttpTimeoutException, IOException, URISyntaxException {
	
		JSONObject requestBody = new JSONObject();
		requestBody.put("serial_number", "ERS " + UUID.generateUUID());
//		this.CLIENT_ID2 = UUID.generateUUID();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/client/" + CLIENT_ID2, requestBody.toString().getBytes());
		return response;
	}
	
	public synchronized FiskalyHttpResponse startReceipt() throws FiskalyHttpException, FiskalyClientException, 
											FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE.name());
		requestBody.put("client_id", CLIENT_ID);
		this.TX_ID_RECEIPT = UUID.generateUUID();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID_RECEIPT, requestBody.toString().getBytes());
		System.out.println("start Recipt  \n"+response.toString());
		return response;
		
	}
	
	
	public synchronized FiskalyHttpResponse startOrder() throws FiskalyHttpException, FiskalyClientException, 
											FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE.name());
		requestBody.put("client_id", CLIENT_ID);
		
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + UUID.generateUUID(), requestBody.toString().getBytes());
		System.out.println("start Order first Client \n"+response.toString());
		return response;
		
	}
	
	public synchronized FiskalyHttpResponse updateOrder1() throws FiskalyHttpException, FiskalyClientException, 
											FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE.name());
		requestBody.put("client_id", CLIENT_ID);
		
		JSONObject arrayElementOne = new JSONObject();
		
		JSONObject order = new JSONObject();
		order.put("line_items", Arrays.asList(new LineItem("2", "Beer", "6.00")));
		
		arrayElementOne.put("order", order);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		this.TX_ID = UUID.generateUUID();
		
		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID, requestBody.toString().getBytes());
			System.out.println("update Order1 first Client \n"+response.toString());
			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());			
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());			
		}	
		
		return null;
	}
	
	
	public synchronized FiskalyHttpResponse updateOrder2() throws FiskalyHttpException, FiskalyClientException, 
											FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE.name());
		requestBody.put("client_id", CLIENT_ID);
		
		JSONObject arrayElementOne = new JSONObject();
		
		JSONObject order = new JSONObject();
		order.put("line_items", Arrays.asList(new LineItem("1", "Coke", "2.00")));
		
		arrayElementOne.put("order", order);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", 1);
		
		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID, requestBody.toString().getBytes(), requestParams);
			System.out.println("update Order2 first Client \n"+response.toString());
			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());		
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());			
		}	
		
		return null;
	}
	
	
	public synchronized FiskalyHttpResponse finishOrder() throws FiskalyHttpException, FiskalyClientException, 
											FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.FINISHED);
		requestBody.put("client_id", CLIENT_ID);
		
		JSONObject arrayElementOne = new JSONObject();
		
		JSONObject order = new JSONObject();
		order.put("line_items", Arrays.asList(new LineItem("1", "Beer", "6.00"), new LineItem("1", "Coke", "2.00")));
		
		arrayElementOne.put("order", order);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", 2);
		
		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID, requestBody.toString().getBytes(), requestParams);
			System.out.println("finish Order first Client \n"+response.toString());
			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());		
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());		
		}	
		
		return null;
	}
	
	public synchronized FiskalyHttpResponse startOrderWithSecondClient() throws FiskalyHttpException, FiskalyClientException, 
										FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE);
		requestBody.put("client_id", CLIENT_ID2);
		
		this.TX_ID2 = UUID.generateUUID();
		
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2, requestBody.toString().getBytes());
		System.out.println("start Order second Client \n"+response.toString());
		return response;
	
	}
	
	public synchronized FiskalyHttpResponse updateOrder1WithSecondClient() throws FiskalyHttpException, FiskalyClientException, 
										FiskalyHttpTimeoutException, IOException, URISyntaxException {		
			JSONObject requestBody = new JSONObject();
			requestBody.put("state", FiskalyState.ACTIVE);
			requestBody.put("client_id", CLIENT_ID2);
			
			JSONObject arrayElementOne = new JSONObject();
			
			JSONObject order = new JSONObject();
			order.put("line_items", Arrays.asList(new LineItem("1", "Coke2", "2.00")));
			
			arrayElementOne.put("order", order);
			JSONObject standardV1 = new JSONObject();
			standardV1.put("standard_v1", arrayElementOne);
			requestBody.put("schema", standardV1);
			
			Map<String, Integer> requestParams = new HashMap<String, Integer>();		
			requestParams.put("last_revision", 1);
			
			try {
				final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
				final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2, requestBody.toString().getBytes(), requestParams);
				System.out.println("update Order1 second Client \n"+response.toString());

				return response;
			} catch (FiskalyHttpException e){
				Log.warn(e.getMessage());				
				System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());				
			}	
		
		return null;
	}
	
	public synchronized FiskalyHttpResponse updateOrder2WithSecondClient() throws FiskalyHttpException, FiskalyClientException, 
															FiskalyHttpTimeoutException, IOException, URISyntaxException {
	
			JSONObject requestBody = new JSONObject();
			requestBody.put("state", FiskalyState.ACTIVE);
			requestBody.put("client_id", CLIENT_ID2);
			
			JSONObject arrayElementOne = new JSONObject();
			
			JSONObject order = new JSONObject();
			order.put("line_items", Arrays.asList(new LineItem("1", "Lemonade2", "2.50")));
			
			arrayElementOne.put("order", order);
			JSONObject standardV1 = new JSONObject();
			standardV1.put("standard_v1", arrayElementOne);
			requestBody.put("schema", standardV1);
			
			Map<String, Integer> requestParams = new HashMap<String, Integer>();		
			requestParams.put("last_revision", 2);
			
			try {
				final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
				final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2, requestBody.toString().getBytes(), requestParams);
				System.out.println("update Order2 second Client \n"+response.toString());

				return response;
			} catch (FiskalyHttpException e){
				Log.warn(e.getMessage());				
				System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());				
			}	
		
		return null;
		
	}
	
	
	public synchronized FiskalyHttpResponse finishOrderWithSecondClient() throws FiskalyHttpException, FiskalyClientException, 
														FiskalyHttpTimeoutException, IOException, URISyntaxException {
			
			JSONObject requestBody = new JSONObject();
			requestBody.put("state", FiskalyState.FINISHED);
			requestBody.put("client_id", CLIENT_ID2);
			
			JSONObject arrayElementOne = new JSONObject();
			
			JSONObject order = new JSONObject();
			order.put("line_items", Arrays.asList(new LineItem("1", "Coke2", "2.00"), new LineItem("1", "Lemonade2", "2.50")));
			
			arrayElementOne.put("order", order);
			JSONObject standardV1 = new JSONObject();
			standardV1.put("standard_v1", arrayElementOne);
			requestBody.put("schema", standardV1);
			
			Map<String, Integer> requestParams = new HashMap<String, Integer>();		
			requestParams.put("last_revision", 3);
			
			try {
				final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
				final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2, requestBody.toString().getBytes(), requestParams);
				System.out.println("finish Order second Client \n"+response.toString());

				return response;
			} catch (FiskalyHttpException e){
				Log.warn(e.getMessage());				
				System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage());				
			}	
		
		return null;
	}
	
	public synchronized FiskalyHttpResponse finishReceipt() throws FiskalyClientException, FiskalyHttpTimeoutException, IOException, URISyntaxException {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.FINISHED);
		requestBody.put("client_id", CLIENT_ID);

		JSONObject arrayElementOne = new JSONObject();		
		
		JSONObject receipt = new JSONObject();
		receipt.put("receipt_type", FiskalyReceiptType.RECEIPT);
		receipt.put("amounts_per_vat_rate", Arrays.asList(new AmountsPerVatRate("NORMAL", "14.875")));
		receipt.put("amounts_per_payment_type", Arrays.asList(new AmountsPerPaymentType("CASH", "14.875")));

		arrayElementOne.put("receipt", receipt);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", 1);		

		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID_RECEIPT, requestBody.toString().getBytes(), requestParams);
			System.out.println("finsh Recipt \n"+response.toString());

			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());			
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage() + "    Failed here");
		}		
		
		return null;
	}
	

}
