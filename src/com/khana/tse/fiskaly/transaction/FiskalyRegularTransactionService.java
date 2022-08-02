package com.khana.tse.fiskaly.transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jfree.util.Log;
import org.json.JSONObject;

import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpClient;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.fiskaly.sdk.FiskalyHttpTimeoutException;
import com.floreantpos.main.Application;
import com.floreantpos.model.dao.RestaurantDAO;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.LineItem;


public class FiskalyRegularTransactionService {


	private static final String _BASEURL = "https://kassensichv.io/api/v1";

	//	private static String TSS_ID ="0ca501fa-4f82-4682-9979-7f8f9b54fddd";
	//	
	//	private static String  CLIENT_ID="6f3d9750-6b4c-4bdc-8c20-77c3a960e2dc";
	//	private static String CLIENT_ID2;
	//	
	//	private static UUID TX_ID;
	//	private static UUID TX_ID2;
	private static String TX_ID_RECEIPT;

	//	private static final String API_KEY = System.getenv("FISKALY_API_KEY");
	//	private static final String API_SECRET = System.getenv("FISKALY_API_SECRET");
	private static String API_KEY;
	private static String API_SECRET;

	public FiskalyRegularTransactionService() {
		if(RestaurantDAO.getRestaurant().isTseLive()) {
			FiskalyRegularTransactionService.API_KEY = "live_cb6zppxj72p32jv7uywgppvxk_khana-kassensystem";
			FiskalyRegularTransactionService.API_SECRET = "UAD8FTUMSvtTdfLhzLNuYVvsngk6BZnnzNMJLmQg5Mn";
		} else {
			FiskalyRegularTransactionService.API_KEY = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
			FiskalyRegularTransactionService.API_SECRET = "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
		}		
	}
	
	private UUIDGenerator UUID = new UUIDGenerator();

	public synchronized FiskalyHttpResponse createTSS() throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException{

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "TSE_"+Application.getInstance().getRestaurant().getName()+"_"+Application.getInstance().getRestaurant().getZipCode());
		requestBody.put("state", "INITIALIZED");		
		UUID TssID = UUID.generateUUID();

		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TssID, requestBody.toString().getBytes());
		return response;
	}

	public synchronized FiskalyHttpResponse createClientID(String TSS_ID) throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException {		
		JSONObject requestBody = new JSONObject();
		requestBody.put("serial_number", UUID.generateUUID());
		UUID ClienId;
		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0)
			ClienId = UUID.generateUUID();
		else
		ClienId = UUID.getClientUniqueID();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/client/" + ClienId, requestBody.toString().getBytes());
		return response;
	}

	public synchronized FiskalyHttpResponse startReceipt(FiskalyKeyParameter param) throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException {

		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE);
		requestBody.put("client_id", param.getClientId());
		this.TX_ID_RECEIPT = UUID.generateUUID().toString();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + TX_ID_RECEIPT, requestBody.toString().getBytes());		
		return response;
	}
	
	public synchronized FiskalyHttpResponse reStartReceipt(FiskalyKeyParameter param) throws FiskalyHttpException, FiskalyClientException, 
		FiskalyHttpTimeoutException, IOException, URISyntaxException {
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE);
		requestBody.put("client_id", param.getClientId());
		this.TX_ID_RECEIPT = param.getTransactionIdReceipt();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + TX_ID_RECEIPT, requestBody.toString().getBytes());		
		return response;
	}

	public synchronized FiskalyHttpResponse startOrder(FiskalyKeyParameter param) throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException {
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE);
		requestBody.put("client_id", param.getClientId());

		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + UUID.generateUUID(), requestBody.toString().getBytes());
		return response;

	}

	public synchronized FiskalyHttpResponse updateOrder(FiskalyKeyParameter param, List<LineItem> lineItems) throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException {
		FiskalyHttpResponse response = null;
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.ACTIVE);
		requestBody.put("client_id", param.getClientId());

		JSONObject arrayElementOne = new JSONObject();

		JSONObject order = new JSONObject();
		order.put("line_items", lineItems);

		arrayElementOne.put("order", order);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);

		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", param.getLatestRevision());

		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + param.getTransactionId(), requestBody.toString().getBytes(), requestParams);

		return response;
	}

	public synchronized FiskalyHttpResponse finishOrder(FiskalyKeyParameter param, List<LineItem> lineItems) throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException {

		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.FINISHED);
		requestBody.put("client_id", param.getClientId());

		JSONObject arrayElementOne = new JSONObject();

		JSONObject order = new JSONObject();
		order.put("line_items", lineItems);

		arrayElementOne.put("order", order);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);

		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", param.getLatestRevision());

		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + param.getTransactionId(), requestBody.toString().getBytes(), requestParams);
		return response;
	}

	public synchronized FiskalyHttpResponse finishReceipt(FiskalyKeyParameter param, JSONObject receipt) throws FiskalyClientException, FiskalyHttpTimeoutException, IOException, URISyntaxException {

		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.FINISHED);
		requestBody.put("client_id", param.getClientId());

		JSONObject arrayElementOne = new JSONObject();		


		arrayElementOne.put("receipt", receipt);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);

		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", param.getLatestRevisionReceipt());		
		System.out.println("2. Transaction Id Recept: " + param.getTransactionIdReceipt());

		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + param.getTransactionIdReceipt(), requestBody.toString().getBytes(), requestParams);
			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());			
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage() + "    Failed here");
		}		

		return null;
	}


	public synchronized FiskalyHttpResponse cancelReceipt(FiskalyKeyParameter param, JSONObject receipt) throws FiskalyClientException, FiskalyHttpTimeoutException, IOException, URISyntaxException {		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", FiskalyState.CANCELLED);
		requestBody.put("client_id", param.getClientId());

		JSONObject arrayElementOne = new JSONObject();	


		arrayElementOne.put("receipt", receipt);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);

		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", param.getLatestRevisionReceipt());		
		System.out.println("2. Transaction Id Recept: " + param.getTransactionIdReceipt());
		try {
			final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
			final FiskalyHttpResponse response = client.request("PUT", "/tss/" + param.getTssId() + "/tx/" + param.getTransactionIdReceipt(), requestBody.toString().getBytes(), requestParams);
			return response;
		} catch (FiskalyHttpException e){
			Log.warn(e.getMessage());			
			System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() + "  Message: " + e.getMessage() + "    Failed here");
		}		

		return null;
	}

	public synchronized FiskalyHttpResponse getTSSInfo() throws FiskalyHttpException, FiskalyClientException, 
	FiskalyHttpTimeoutException, IOException, URISyntaxException{
		JSONObject requestBody = new JSONObject();
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("GET", "/tss/" + Application.getInstance().getRestaurant().getTseId(), requestBody.toString().getBytes());
		return response;

	}


	/*
	 * 
	 * 
	 * public FiskalyHttpResponse createSecondClientID() throws
	 * FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException,
	 * IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("serial_number",
	 * "ERS " + UUID.generateUUID()); final FiskalyHttpClient client = new
	 * FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL); final FiskalyHttpResponse
	 * response = client.request("PUT", "/tss/" + TSS_ID + "/client/" + CLIENT_ID2,
	 * requestBody.toString().getBytes()); return response; }
	 * 
	 * public FiskalyHttpResponse updateOrder2(KeyParameter param, List<LineItem>
	 * lineItems) throws FiskalyHttpException, FiskalyClientException,
	 * FiskalyHttpTimeoutException, IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("state",
	 * FiskalyState.ACTIVE); requestBody.put("client_id", param.getClientId());
	 * 
	 * JSONObject arrayElementOne = new JSONObject();
	 * 
	 * JSONObject order = new JSONObject(); order.put("line_items", lineItems);
	 * 
	 * arrayElementOne.put("order", order); JSONObject standardV1 = new
	 * JSONObject(); standardV1.put("standard_v1", arrayElementOne);
	 * requestBody.put("schema", standardV1);
	 * 
	 * Map<String, Integer> requestParams = new HashMap<String, Integer>();
	 * requestParams.put("last_revision", param.getLatestRevision());
	 * 
	 * final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET,
	 * _BASEURL); final FiskalyHttpResponse response = client.request("PUT", "/tss/"
	 * + param.getTssId() + "/tx/" + param.getTransactionId(),
	 * requestBody.toString().getBytes(), requestParams);
	 * 
	 * return response;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * public FiskalyHttpResponse startOrderWithSecondClient() throws
	 * FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException,
	 * IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("state",
	 * FiskalyState.ACTIVE); requestBody.put("client_id", CLIENT_ID2);
	 * 
	 * this.TX_ID2 = UUID.generateUUID();
	 * 
	 * final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET,
	 * _BASEURL); final FiskalyHttpResponse response = client.request("PUT", "/tss/"
	 * + TSS_ID + "/tx/" + TX_ID2, requestBody.toString().getBytes());
	 * 
	 * return response;
	 * 
	 * }
	 * 
	 * 
	 * public FiskalyHttpResponse updateOrder1WithSecondClient() throws
	 * FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException,
	 * IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("state",
	 * FiskalyState.ACTIVE); requestBody.put("client_id", CLIENT_ID2);
	 * 
	 * JSONObject arrayElementOne = new JSONObject();
	 * 
	 * JSONObject order = new JSONObject(); order.put("line_items",
	 * Arrays.asList(new LineItem("1", "Coke", "2.00")));
	 * 
	 * arrayElementOne.put("order", order); JSONObject standardV1 = new
	 * JSONObject(); standardV1.put("standard_v1", arrayElementOne);
	 * requestBody.put("schema", standardV1);
	 * 
	 * Map<String, Integer> requestParams = new HashMap<String, Integer>();
	 * requestParams.put("last_revision", 1);
	 * 
	 * try { final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY,
	 * API_SECRET, _BASEURL); final FiskalyHttpResponse response =
	 * client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2,
	 * requestBody.toString().getBytes(), requestParams); return response; } catch
	 * (FiskalyHttpException e){ Log.warn(e.getMessage());
	 * System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() +
	 * "  Message: " + e.getMessage()); }
	 * 
	 * return null; }
	 * 
	 * public FiskalyHttpResponse updateOrder2WithSecondClient() throws
	 * FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException,
	 * IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("state",
	 * FiskalyState.ACTIVE); requestBody.put("client_id", CLIENT_ID2);
	 * 
	 * JSONObject arrayElementOne = new JSONObject();
	 * 
	 * JSONObject order = new JSONObject(); order.put("line_items",
	 * Arrays.asList(new LineItem("1", "Lemonade", "2.50")));
	 * 
	 * arrayElementOne.put("order", order); JSONObject standardV1 = new
	 * JSONObject(); standardV1.put("standard_v1", arrayElementOne);
	 * requestBody.put("schema", standardV1);
	 * 
	 * Map<String, Integer> requestParams = new HashMap<String, Integer>();
	 * requestParams.put("last_revision", 2);
	 * 
	 * try { final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY,
	 * API_SECRET, _BASEURL); final FiskalyHttpResponse response =
	 * client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2,
	 * requestBody.toString().getBytes(), requestParams); return response; } catch
	 * (FiskalyHttpException e){ Log.warn(e.getMessage());
	 * System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() +
	 * "  Message: " + e.getMessage()); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * 
	 * public FiskalyHttpResponse finishOrderWithSecondClient() throws
	 * FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException,
	 * IOException, URISyntaxException {
	 * 
	 * JSONObject requestBody = new JSONObject(); requestBody.put("state",
	 * FiskalyState.FINISHED); requestBody.put("client_id", CLIENT_ID2);
	 * 
	 * JSONObject arrayElementOne = new JSONObject();
	 * 
	 * JSONObject order = new JSONObject(); order.put("line_items",
	 * Arrays.asList(new LineItem("1", "Coke", "2.00"), new LineItem("1",
	 * "Lemonade", "2.50")));
	 * 
	 * arrayElementOne.put("order", order); JSONObject standardV1 = new
	 * JSONObject(); standardV1.put("standard_v1", arrayElementOne);
	 * requestBody.put("schema", standardV1);
	 * 
	 * Map<String, Integer> requestParams = new HashMap<String, Integer>();
	 * requestParams.put("last_revision", 3);
	 * 
	 * try { final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY,
	 * API_SECRET, _BASEURL); final FiskalyHttpResponse response =
	 * client.request("PUT", "/tss/" + TSS_ID + "/tx/" + TX_ID2,
	 * requestBody.toString().getBytes(), requestParams); return response; } catch
	 * (FiskalyHttpException e){ Log.warn(e.getMessage());
	 * System.out.println("Status" + e.getStatus() + "  Error: " + e.getError() +
	 * "  Message: " + e.getMessage()); }
	 * 
	 * return null; }
	 */


}
