package com.khana.restclient.android;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

/**
 * @author Jyoti Rai
 *
 */

public class BaseAndroidClientInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	private int id;

//	@SerializedName("uid")
//	private String deviceId;

	@SerializedName("clint_ip")
	private String clientIp;

	@SerializedName("client_port")
	private String clientPort;

	@SerializedName("client_id")
	private String clientId;
	
	@SerializedName("active-Date")
	@JsonIgnore
	private Date activeDate;

	
	@SerializedName("client_AliasName")
	@JsonIgnore
	private String aliasName;


	//constructors
	public BaseAndroidClientInfo () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAndroidClientInfo (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

//	public String getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getClientPort() {
		return clientPort;
	}

	public void setClientPort(String clientPort) {
		this.clientPort = clientPort;
	}
	
	public String getAliasName() {
		return aliasName!=null?aliasName:"";
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
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
