package com.khana.tse.fiskaly;

import java.lang.reflect.Field;

import com.google.gson.annotations.SerializedName;

public class FiskalyKeyParameter {

	@SerializedName("tss_id")
	private String TssId;

	@SerializedName("client_id")
	private String clientId;

	@SerializedName("tx_id")
	private String TransactionId;
	
	@SerializedName("tx_id_receipt")
	private String transactionIdReceipt;

	@SerializedName("latest_revision")
	private int latestRevision;
	
	@SerializedName("latest_revision_receipt")
	private int latestRevisionReceipt;
	
	
	@SerializedName("certificate_serial")
	private String tssSerialNumer;

	public String getTssSerialNumer() {
		return tssSerialNumer;
	}

	public void setTssSerialNumer(String tssSerialNumer) {
		this.tssSerialNumer = tssSerialNumer;
	}

	public String getTssId() {
		return TssId;
	}

	public void setTssId(String tssId) {
		TssId = tssId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}	

	public String getTransactionIdReceipt() {
		return transactionIdReceipt;
	}

	public void setTransactionIdReceipt(String transactionIdReceipt) {
		this.transactionIdReceipt = transactionIdReceipt;
	}

	public int getLatestRevision() {
		return latestRevision;
	}

	public void setLatestRevision(int latestRevision) {
		this.latestRevision = latestRevision;
	}

	public int getLatestRevisionReceipt() {
		return latestRevisionReceipt;
	}

	public void setLatestRevisionReceipt(int latestRevisionReceipt) {
		this.latestRevisionReceipt = latestRevisionReceipt;
	}	

	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
}
