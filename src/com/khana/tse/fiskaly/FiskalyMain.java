package com.khana.tse.fiskaly;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fiskaly.sdk.FiskalyHttpClient;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.jidesoft.plaf.windows.TMSchema.State;

public class FiskalyMain {
	
	private static final String _BASEURL = "https://kassensichv.io/api/v1";
	
	private static final String TSS_ID = "b5b259bb-2f78-4d7b-a4c7-0f91a45e8ff0";
	
	private static final String GUID = "cf7975c9-a0e7-4554-a979-de21cd8460c5";
	
	private static final String CLIENT_ID = "638902fb-779b-44fb-aa14-6564302df7d8";
	
	private static final String tx_id_or_number = "feaedf74-66a9-413c-a43f-e1f2a5d1e528";
	final String apiKey = "test_5attkhrimgehy8y78j6eqibja_testkhana";
	final String apiSecret = "sTbEIr51VsmtFmSZeoEg7w79sinR1NyvlauaVYJScBE";
	
	public static void main(String[] args) throws Exception {
		
		FiskalyMain obj = new FiskalyMain();
		
//		final String apiKey = System.getenv("FISKALY_API_KEY");
//		  final String apiSecret = System.getenv("FISKALY_API_SECRET");
		final String apiKey = "test_5attkhrimgehy8y78j6eqibja_testkhana";
		final String apiSecret = "sTbEIr51VsmtFmSZeoEg7w79sinR1NyvlauaVYJScBE";
		  final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		  final FiskalyHttpResponse response = client.request("GET", "/tss");
		  System.out.println(response);
		  
//		  obj.listAllTransactionsTSS();
		  //obj.generateClientID();
//		  obj.InitiateTransaction();
		  obj.UpdateTransaction();
		  
		
	}
	
	public void InitiateTransaction() throws Exception {
		
//		FiskalyHttpResponse result = generateClientID();
//		
//		Byte[] body = ArrayUtils.toObject(result.body);
//		JSONObject clientId = new JSONObject(body);
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", State.ACTIVE);
		//requestBody.put("client_id", clientId.getJSONObject("client_id"));
		requestBody.put("client_id", CLIENT_ID);
	
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();
		
		requestParams.put("last_revision", 0);
		
		
//		final String apiKey = System.getenv("FISKALY_API_KEY");
//		final String apiSecret = System.getenv("FISKALY_API_SECRET");
		
	
		
		
		final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + GUID, requestBody.toString().getBytes(), requestParams);
		System.out.println(response);
	}
	
	public void UpdateTransaction() throws Exception {		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", "ACTIVE");
		requestBody.put("client_id", CLIENT_ID);

		JSONObject arrayElementOne = new JSONObject();		
		
		JSONObject receipt = new JSONObject();
		receipt.put("receipt_type", "RECEIPT");
		receipt.put("amounts_per_vat_rate", Arrays.asList(new AmountsPerVatRate(State.NORMAL.name(), "14.28")));
		receipt.put("amounts_per_payment_type", Arrays.asList(new AmountsPerPaymentType("NON_CASH", "14.28")));

		arrayElementOne.put("receipt", receipt);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", 2);
		
//		final String apiKey = System.getenv("FISKALY_API_KEY");
//		final String apiSecret = System.getenv("FISKALY_API_SECRET");
		final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + tx_id_or_number, requestBody.toString().getBytes(), requestParams);		
		System.out.println(response);
		
	}
	
	
public void updateOrder() throws Exception {
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("state", "ACTIVE");
		requestBody.put("client_id", CLIENT_ID);

		JSONObject arrayElementOne = new JSONObject();		
		
		JSONObject receipt = new JSONObject();
		receipt.put("receipt_type", "RECEIPT");
		receipt.put("amounts_per_vat_rate", Arrays.asList(new AmountsPerVatRate(State.NORMAL.name(), "14.28")));
		receipt.put("amounts_per_payment_type", Arrays.asList(new AmountsPerPaymentType("NON_CASH", "14.28")));

		arrayElementOne.put("receipt", receipt);
		JSONObject standardV1 = new JSONObject();
		standardV1.put("standard_v1", arrayElementOne);
		requestBody.put("schema", standardV1);
		
		Map<String, Integer> requestParams = new HashMap<String, Integer>();		
		requestParams.put("last_revision", 2);
		
//		final String apiKey = System.getenv("FISKALY_API_KEY");
//		final String apiSecret = System.getenv("FISKALY_API_SECRET");
		final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TSS_ID + "/tx/" + tx_id_or_number, requestBody.toString().getBytes(), requestParams);		
		System.out.println(response);
		
	}
	
	
	
	
	
	public void listAllTransactionsTSS() throws Exception {
		
//		  final String apiKey = System.getenv("FISKALY_API_KEY");
//		  final String apiSecret = System.getenv("FISKALY_API_SECRET");
		  final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		  final FiskalyHttpResponse response = client.request("GET", "/tss/" + TSS_ID + "/tx");
		  System.out.println(response);
		
	}
	
	public FiskalyHttpResponse generateClientID() throws Exception {
		
		JSONObject obj = new JSONObject();
	    obj.put("serial_number", "ERS " + GUID);
	    
	    obj.toString().getBytes();
		
//		final String apiKey = System.getenv("FISKALY_API_KEY");
//		final String apiSecret = System.getenv("FISKALY_API_SECRET");
		final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, _BASEURL);
		final FiskalyHttpResponse response = client.request("GET", "/tss/" + TSS_ID + "/client/" + GUID);
		System.out.println(response);
		
		return response;
	}
	
	

}



















