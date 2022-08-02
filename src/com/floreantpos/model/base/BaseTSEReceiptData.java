package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseTSEReceiptData implements  Comparable, Serializable{	
	
	// constructors
		public BaseTSEReceiptData () {
			initialize();
		}

		/**
		 * Constructor for primary key
		 */
		public BaseTSEReceiptData (java.lang.Integer id) {
			this.setId(id);
			initialize();
		}

		protected void initialize () {}



		private int hashCode = Integer.MIN_VALUE;

		// primary key
		private java.lang.Integer id;

	
public java.lang.Integer getId() {
			return id;
		}

		public void setId(java.lang.Integer id) {
			this.id = id;
		}



	private int transaction;
	
	private Date start;
	
	private Date finish;
	
	private int signatureCount;
	
	private String serialNumber;
	
	private String signature;	
	
	private String timeFormat;
	
	private String signatureAlgorithm;
	
	private String signaturePublicKey;
	
	private String clientID;
	
	private String erstbestellung;
	
	private String QRCode;
	private String latestRevision;

	public String getLatestRevision() {
		return latestRevision;
	}

	public void setLatestRevision(String latestRevision) {
		this.latestRevision = latestRevision;
	}

	public int getTransaction() {
		return transaction;
	}

	public void setTransaction(int transaction) {
		this.transaction = transaction;
	}


	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public String getSignaturePublicKey() {
		return signaturePublicKey;
	}

	public void setSignaturePublicKey(String signaturePublicKey) {
		this.signaturePublicKey = signaturePublicKey;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getErstbestellung() {
		return erstbestellung;
	}

	public void setErstbestellung(String erstbestellung) {
		this.erstbestellung = erstbestellung;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getFinish() {
		return finish;
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	public int getSignatureCount() {
		return signatureCount;
	}

	public void setSignatureCount(int signatureCount) {
		this.signatureCount = signatureCount;
	}

	

	@Override
	public String toString() {
		return "TSEReceiptData [transaction=" + transaction + ", start=" + start + ", finish=" + finish
				+ ", serialNumber=" + serialNumber + ", signature=" + signature + ", timeFormat=" + timeFormat
				+ ", signatureAlgorithm=" + signatureAlgorithm + ", signaturePublicKey=" + signaturePublicKey
				+ ", clientID=" + clientID + ", erstbestellung=" + erstbestellung + ", QRCode=" + QRCode + ", signatureCount=" + signatureCount +"]";
	}
	
}
