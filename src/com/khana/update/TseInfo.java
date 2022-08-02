/**
 * 
 */
package com.khana.update;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jyoti Rai
 * 
 *
 */
@Entity
public class TseInfo {
	@JsonProperty("id") 
	@Column(name="id")
	int id;
	
	@JsonProperty("tse_enable")
	@Column(name="tse_enable")
	boolean tse_enable;
	
	@JsonProperty("tse_Id")
	@Column(name="tse_Id")
	String tse_Id;
	
	@JsonProperty("tse_create_date")
	@Column(name="tse_create_date")
	Date tse_create_date;
	
	@JsonProperty("tse_valid_date")
	@Column(name="tse_valid_date")
	Date tse_valid_date;
	
	@JsonProperty("tse_security_key")
	@Column(name="tse_security_key")
	String tse_security_key;
	
	@JsonProperty("client_info")
	@Column(name="client_info")
	String client_info;


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the tse_enable
	 */
	public boolean isTse_enable() {
		return tse_enable;
	}
	/**
	 * @param tse_enable the tse_enable to set
	 */
	public void setTse_enable(boolean tse_enable) {
		this.tse_enable = tse_enable;
	}
	/**
	 * @return the tse_Id
	 */
	public String getTse_Id() {
		return tse_Id;
	}
	/**
	 * @param tse_Id the tse_Id to set
	 */
	public void setTse_Id(String tse_Id) {
		this.tse_Id = tse_Id;
	}
	/**
	 * @return the tse_create_date
	 */
	public Date getTse_create_date() {
		return tse_create_date;
	}
	/**
	 * @param tse_create_date the tse_create_date to set
	 */
	public void setTse_create_date(Date tse_create_date) {
		this.tse_create_date = tse_create_date;
	}
	/**
	 * @return the tse_valid_date
	 */
	public Date getTse_valid_date() {
		return tse_valid_date;
	}
	/**
	 * @param tse_valid_date the tse_valid_date to set
	 */
	public void setTse_valid_date(Date tse_valid_date) {
		this.tse_valid_date = tse_valid_date;
	}
	/**
	 * @return the tse_security_key
	 */
	public String getTse_security_key() {
		return tse_security_key;
	}
	/**
	 * @param tse_security_key the tse_security_key to set
	 */
	public void setTse_security_key(String tse_security_key) {
		this.tse_security_key = tse_security_key;
	}
	/**
	 * @return the client_info
	 */
	public String getClient_info() {
		return client_info;
	}
	/**
	 * @param client_info the client_info to set
	 */
	public void setClient_info(String client_info) {
		this.client_info = client_info;
	}

	@Override
	public String toString() {
		return "TseInfo [id=" + id + ", tse_enable=" + tse_enable + ", tse_Id=" + tse_Id + ", tse_create_date="
				+ tse_create_date + ", tse_valid_date=" + tse_valid_date + ", tse_security_key=" + tse_security_key
				+ ", client_info=" + client_info + "]";
	}


}
