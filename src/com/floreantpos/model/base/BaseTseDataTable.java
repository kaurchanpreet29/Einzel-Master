package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TSE_RECEIPT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TSE_RECEIPT"
 */

public abstract class BaseTseDataTable  implements Comparable, Serializable {
	
	public static String REF = "TSEReceiptData";
	public static String PROP_TX_NR = "transaction";
	public static String PROP_STARTDATE = "start";
	public static String PROP_FINISHDATE = "finish";
	public static String PROP_SINATURE_COUNT = "signatureCount";
	public static String PROP_SERIAL_NUMBER = "serialNumber";
	public static String PROP_SIGNATURE = "signature";
	public static String PROP_TIME_FORMAT = "timeFormat";
	public static String PROP_SIG_ALGO = "signatureAlgorithm";
	public static String PROP_SIG_PUB_KEY = "signaturePublicKey";
	public static String PROP_CLEINT_ID = "clientID";
	public static String PROP_ERSETE_BESTELLUNG = "erstbestellung";
	public static String PROP_QR_CODE = "QRCode";
	public static String PROP_LAT_REV_NR = "latestRevision";

	// constructors
	public BaseTseDataTable () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTseDataTable (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}

	protected java.lang.String finish;
	protected java.lang.Integer signatureCount;
	protected java.lang.String serialNumber;
	protected java.lang.Integer transaction;
	protected java.lang.String signature;
	protected java.lang.String timeFormat;
	protected java.lang.String signatureAlgorithm;
	protected java.lang.String signaturePublicKey;
	protected java.lang.String clientID;
	protected java.lang.String erstbestellung;
	protected java.lang.String QRCode;
	protected java.lang.String latestRevision;

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String start;
		public java.lang.String getStart() {
			return start;
		}

		public void setStart(java.lang.String start) {
			this.start = start;
		}

		public java.lang.String getFinish() {
			return finish;
		}

		public void setFinish(java.lang.String finish) {
			this.finish = finish;
		}

		public java.lang.Integer getSignatureCount() {
			return signatureCount;
		}

		public void setSignatureCount(java.lang.Integer signatureCount) {
			this.signatureCount = signatureCount;
		}

		public java.lang.String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(java.lang.String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public java.lang.Integer getTransaction() {
			return transaction;
		}

		public void setTransaction(java.lang.Integer transaction) {
			this.transaction = transaction;
		}

		public java.lang.String getSignature() {
			return signature;
		}

		public void setSignature(java.lang.String signature) {
			this.signature = signature;
		}

		public java.lang.String getTimeFormat() {
			return timeFormat;
		}

		public void setTimeFormat(java.lang.String timeFormat) {
			this.timeFormat = timeFormat;
		}

		public java.lang.String getSignatureAlgorithm() {
			return signatureAlgorithm;
		}

		public void setSignatureAlgorithm(java.lang.String signatureAlgorithm) {
			this.signatureAlgorithm = signatureAlgorithm;
		}

		public java.lang.String getSignaturePublicKey() {
			return signaturePublicKey;
		}

		public void setSignaturePublicKey(java.lang.String signaturePublicKey) {
			this.signaturePublicKey = signaturePublicKey;
		}

		public java.lang.String getClientID() {
			return clientID;
		}

		public void setClientID(java.lang.String clientID) {
			this.clientID = clientID;
		}

		public java.lang.String getErstbestellung() {
			return erstbestellung;
		}

		public void setErstbestellung(java.lang.String erstbestellung) {
			this.erstbestellung = erstbestellung;
		}

		public java.lang.String getQRCode() {
			return QRCode;
		}

		public void setQRCode(java.lang.String qRCode) {
			QRCode = qRCode;
		}

		public java.lang.String getLatestRevision() {
			return latestRevision;
		}

		public void setLatestRevision(java.lang.String latestRevision) {
			this.latestRevision = latestRevision;
		}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}


	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TseData)) return false;
		else {
			com.floreantpos.model.TseData tseData = (com.floreantpos.model.TseData) obj;
			if (null == this.getId() || null == tseData.getId()) return false;
			else return (this.getId().equals(tseData.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


	

}