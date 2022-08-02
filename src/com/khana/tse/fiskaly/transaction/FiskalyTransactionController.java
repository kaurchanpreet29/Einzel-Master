package com.khana.tse.fiskaly.transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.util.Log;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.fiskaly.sdk.FiskalyHttpTimeoutException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.TicketItem;
import com.khana.tse.fiskaly.AmountsPerPaymentType;
import com.khana.tse.fiskaly.AmountsPerVatRate;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.FiskalyNumberFormat;
import com.khana.tse.fiskaly.LineItem;
import com.khana.tse.fiskaly.Receipt;

public class FiskalyTransactionController {

	FiskalyRegularTransactionService service = new FiskalyRegularTransactionService();
	static FiskalyKeyParameter params = new FiskalyKeyParameter();

	public static void main(String[] args) {
		FiskalyTransactionController con = new FiskalyTransactionController();

		//		params = con.createTSS();
		//		System.out.println(params.toString());
		//		
		params = con.createClientId(params);
		//		System.out.println(params.toString());
		params.setClientId("6f3d9750-6b4c-4bdc-8c20-77c3a960e2dc");
		params.setTssId("0ca501fa-4f82-4682-9979-7f8f9b54fddd");
		params.setTransactionIdReceipt("2ef1711c-58c2-4b8c-8632-b48ee8183947");

		params = con.startReceipt(params);
		params.setLatestRevisionReceipt(1);

		//		System.out.println(params.toString());
		//		
		//		params = con.startOrder(params);
		//		System.out.println(params.toString());
		//		
		//		params = con.updateOrder(params, Arrays.asList());
		//		System.out.println(params.toString());
		//		
		//		params = con.finishOrder(params, Arrays.asList(new LineItem("1", "Beer", FiskalyNumberFormat.formatToTwoDecimal(10.0)), new LineItem("1", "Salad", FiskalyNumberFormat.formatToTwoDecimal(5.0))));
		//		System.out.println(params.toString());


		Receipt receipt = new Receipt();
		receipt.setReceiptType(FiskalyReceiptType.RECEIPT.name());
		receipt.setAmountsPerVatRate(Arrays.asList(new AmountsPerVatRate(VatRate.SPECIAL_RATE_1.name(), FiskalyNumberFormat.formatToThreeDecimal(10.0)),new AmountsPerVatRate(VatRate.SPECIAL_RATE_2.name(), FiskalyNumberFormat.formatToThreeDecimal(5.0))));
		receipt.setAmountsPerPaymentType(Arrays.asList(new AmountsPerPaymentType(FiskalyPaymentType.NON_CASH.name(), FiskalyNumberFormat.formatToThreeDecimal(15.0))));

		//		params = con.finishReceipt(params, receipt);
		con.finishReceipt(params, receipt);

		//		Receipt receipt1 = new Receipt();
		//		receipt1.setReceiptType(FiskalyReceiptType.CANCELLATION.name());		
		//		receipt1.setAmountsPerVatRate(Arrays.asList());
		//		params = con.cancelReceipt(params, receipt1);
		System.out.println(params.toString());

	}

	public synchronized FiskalyKeyParameter createTSS() {
		FiskalyKeyParameter params = new FiskalyKeyParameter();
		try {
			FiskalyHttpResponse response = service.createTSS();
			params = getParamsFromResponse(response, params, "createTSS");

		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			Log.warn(e.getMessage());		
			System.out.println("Method: createTSS  " + "Message: " + e.getMessage());
		}

		return params;
	}


	public synchronized FiskalyKeyParameter createClientId(FiskalyKeyParameter param) {
		try {
			FiskalyHttpResponse response = service.createClientID(param.getTssId());			
			param = getParamsFromResponse(response, param, "createClient");

		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: createClient  " + "Message: " + e.getMessage());
		}	

		return param;
	}

	public synchronized FiskalyKeyParameter startReceipt(FiskalyKeyParameter param) {
		try {
			FiskalyHttpResponse response = service.startReceipt(param);
			param = getParamsFromResponse(response, param, "startReceipt");

		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: startReceipt  " + "Message: " + e.getMessage());
		}		
		System.out.println("1. Transaction Id Recept: " + param.getTransactionIdReceipt());
		return param;
	}


	public synchronized FiskalyKeyParameter reStartReceipt(FiskalyKeyParameter param) {
		try {
			FiskalyHttpResponse response = service.reStartReceipt(param);
			param = getParamsFromResponse(response, param, "startReceipt");

		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: startReceipt  " + "Message: " + e.getMessage());
		}		
		return param;
	}


	public synchronized FiskalyKeyParameter startOrder(FiskalyKeyParameter param) {
		try {
			FiskalyHttpResponse response = service.startOrder(param);
			param = getParamsFromResponse(response, param, "startOrder");
		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: startOrder  " + "Message: " + e.getMessage());
		}

		return param;		
	}


	public synchronized FiskalyKeyParameter updateOrder(FiskalyKeyParameter param, List<TicketItem> itemList) {

		try {
			FiskalyHttpResponse response = service.updateOrder(param, createLineItems(itemList));
			param = getParamsFromResponse(response, param, "updateOrder");
		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: updateOrder1  " + "Message: " + e.getMessage());
		}		

		return param;
	}


	public synchronized FiskalyKeyParameter finishOrder(FiskalyKeyParameter param, List<TicketItem> itemList) {
		try {
			FiskalyHttpResponse response = service.finishOrder(param, createLineItems(itemList));
			param = getParamsFromResponse(response, param, "finishOrder");
		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: updateOrder2  " + "Message: " + e.getMessage());
		}		

		return param;	
	}

	public synchronized TSEReceiptData finishReceipt(FiskalyKeyParameter param, Receipt receipt) {
		TSEReceiptData data = new TSEReceiptData();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		jsonObj.put("amounts_per_payment_type", receipt.getAmountsPerPaymentType());

		try {
			FiskalyHttpResponse response = service.finishReceipt(param, jsonObj);
			//			System.out.println(response.toString());
			////			data = new Gson().fromJson(response.body.toString(), ReceiptResponsePojo.class);
			//						System.out.println("Body "+response.toString());
			data = getReceiptDateFromResponse(response, data);
			//			System.out.println("Receipt Data /n"+data.toString());
			//			param = getParamsFromResponse(response, param, "finishOrder");
		} catch (FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}	

		return data;	

	}


	/*
	 * CancelReceiptShouldBePrinted or not?
	 */
	public synchronized com.floreantpos.model.TSEReceiptData cancelReceipt(FiskalyKeyParameter param, Receipt receipt) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		TSEReceiptData data = new TSEReceiptData();
		try {
			FiskalyHttpResponse response = service.cancelReceipt(param, jsonObj);
			System.out.println("Storno "+response.toString());
			param = getParamsFromResponse(response, param, "finishOrder");

			data = getReceiptDateFromResponse(response, data);

		} catch (FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}		

		return data;	

	} 

	private synchronized FiskalyKeyParameter getParamsFromResponse(FiskalyHttpResponse response, FiskalyKeyParameter params, String method) throws JsonProcessingException, IOException {

		String body = new String(response.body, "UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(body);
		if(method.equals("createTSS")) {
			params.setTssId(json.get("_id").asText());
		}

		if(method.equals("tss_info")) {
			params.setTssId(json.get("_id").asText());
			params.setTssSerialNumer(json.get("certificate_serial").asText());
		}

		if(method.equals("createClient")) {

			params.setClientId(json.get("_id").asText());
		}
		if(method.equals("startReceipt")) {			
			//			params.setTransactionId(json.get("_id").asText());
			params.setTransactionIdReceipt(json.get("_id").asText());
			params.setLatestRevisionReceipt(json.get("latest_revision").asInt());
			//			params.setLatestRevision(json.get("latest_revision").asInt());
		}
		if(method.equals("startOrder")) {			
			params.setTransactionId(json.get("_id").asText());
			params.setLatestRevision(json.get("latest_revision").asInt());
		}
		if(method.equals("updateOrder") || method.equals("finishOrder") || method.equals("updateOrder2") ) {
			params.setLatestRevision(json.get("latest_revision").asInt());
		}
		return params;
	}

	private synchronized com.floreantpos.model.TSEReceiptData getReceiptDateFromResponse(FiskalyHttpResponse response, TSEReceiptData tseRData) throws JsonProcessingException, IOException {		
		String body = new String(response.body, "UTF-8");		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(body);
		if(json.has("number"))
			tseRData.setTransaction(json.get("number").asInt());

		if(json.has("time_start"))
			tseRData.setStart(new java.util.Date(json.get("time_start").asLong()*1000));

		if(json.has("time_end"))
			tseRData.setFinish(new java.util.Date(json.get("time_end").asLong()*1000));

		if(json.has("certificate_serial"))
			tseRData.setSerialNumber(json.get("certificate_serial").asText());

		if(json.has("signature")&&json.get("signature").has("value"))
			tseRData.setSignature(json.get("signature").get("value").asText());

		if(json.has("log")&&json.get("log").has("timestamp_format"))
			tseRData.setTimeFormat(json.get("log").get("timestamp_format").asText());

		if(json.has("signature")&&json.get("signature").has("algorithm"))
			tseRData.setSignatureAlgorithm(json.get("signature").get("algorithm").asText());

		if(json.has("qr_code_data"))
			tseRData.setQRCode(json.get("qr_code_data").asText());

		if(json.has("signature")&&json.get("signature").has("public_key"))
			tseRData.setSignaturePublicKey(json.get("signature").get("public_key").asText());

		if(json.has("signature")&&json.get("signature").has("counter"))
			tseRData.setSignatureCount(json.get("signature").get("counter").asInt());

		if(json.has("qr_code_data"))
			tseRData.setQRCode(json.get("qr_code_data").asText());

		if(json.has("client_serial_number"))
			tseRData.setClientID(json.get("client_serial_number").asText());

		if(json.has("latest_revision"))
			tseRData.setLatestRevision(json.get("latest_revision").asText());
		return tseRData;
	}

	private synchronized List<LineItem> createLineItems(List<TicketItem> itemList){
		List<LineItem> LineItems = new ArrayList<LineItem>();
		for(TicketItem item: itemList)
			LineItems.add(new LineItem(String.valueOf(item.getItemCount()), item.getName(), FiskalyNumberFormat.formatToTwoDecimal(item.getItemCount()*item.getUnitPrice())));
		return LineItems;
	}

	public synchronized Receipt createReceipt(List<TicketItem> items, FiskalyPaymentType paymetType, FiskalyReceiptType receiptType){

		Receipt receipt = new Receipt();
		if(receiptType.compareTo(FiskalyReceiptType.CANCELLATION)==0){
			receipt.setReceiptType(FiskalyReceiptType.CANCELLATION.name());
			receipt.setAmountsPerVatRate(Arrays.asList());			
		}else {
			receipt.setReceiptType(receiptType.name());
			double imhaus = 0.0;
			double ausserHaus = 0.0;
			double zero =0.0;
			double imhausT = Application.getInstance().dineInTax;
			double ausserHausT = Application.getInstance().homeDeleveryTax;
			for(TicketItem item:items) {
				if(item.getTaxRate()==imhausT)
					imhaus +=item.getTotalAmount();
				else if(item.getTaxRate()==ausserHausT)
					ausserHaus += item.getTotalAmount();
				else if(item.getTaxRate()==0.0)
					zero += item.getTotalAmount();
			}


			if(TerminalConfig.isSpecial())
				receipt.setAmountsPerVatRate(Arrays.asList(new AmountsPerVatRate(VatRate.SPECIAL_RATE_1.name(), FiskalyNumberFormat.formatToThreeDecimal(imhaus)),
						new AmountsPerVatRate(VatRate.SPECIAL_RATE_2.name(), FiskalyNumberFormat.formatToThreeDecimal(ausserHaus)),
						new AmountsPerVatRate(VatRate.NULL.name(), FiskalyNumberFormat.formatToThreeDecimal(zero))));
			else
				receipt.setAmountsPerVatRate(Arrays.asList(new AmountsPerVatRate(VatRate.NORMAL.name(), FiskalyNumberFormat.formatToThreeDecimal(imhaus)),
						new AmountsPerVatRate(VatRate.REDUCED_1.name(), FiskalyNumberFormat.formatToThreeDecimal(ausserHaus)),
						new AmountsPerVatRate(VatRate.NULL.name(), FiskalyNumberFormat.formatToThreeDecimal(zero))));

			receipt.setAmountsPerPaymentType(Arrays.asList(new AmountsPerPaymentType(paymetType.name(), FiskalyNumberFormat.formatToThreeDecimal(imhaus+ausserHaus+zero))));		
		}

		return receipt;

	}



	public synchronized FiskalyKeyParameter finishReceipt1(FiskalyKeyParameter param, Receipt receipt) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		jsonObj.put("amounts_per_payment_type", receipt.getAmountsPerPaymentType());

		System.out.println("A. Transaction Id Recept: " + param.getTransactionIdReceipt());
		System.out.println("Transaction Id: " + param.getTransactionId());
		System.out.println(jsonObj.toString());
		try {
			FiskalyHttpResponse response = service.finishReceipt(param, jsonObj);
			System.out.println(response.toString());
			param = getParamsFromResponse(response, param, "finishOrder");
		} catch (FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}		

		return param;	

	}

	public synchronized FiskalyKeyParameter getTSSInfo() {
		FiskalyKeyParameter params = new FiskalyKeyParameter();
		try {
			FiskalyHttpResponse response = service.getTSSInfo();
			params = getParamsFromResponse(response, params, "tss_info");

		} catch (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException | IOException
				| URISyntaxException e) {
			Log.warn(e.getMessage());		
			System.out.println("Method: createTSS  " + "Message: " + e.getMessage());
		}

		return params;
	}

	/*
	 * public KeyParameter updateOrder2(KeyParameter param, List<LineItem>
	 * lineItems) {
	 * 
	 * try { FiskalyHttpResponse response = service.updateOrder2(param, lineItems);
	 * param = getParamsFromResponse(response, param, "updateOrder2"); } catch
	 * (FiskalyHttpException | FiskalyClientException | FiskalyHttpTimeoutException
	 * | IOException | URISyntaxException e) {
	 * System.out.println("Method: updateOrder2  " + "Message: " + e.getMessage());
	 * }
	 * 
	 * return param; }
	 */



}
