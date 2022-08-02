package com.khana.restclient.android;

import java.io.Serializable;

/**
 * @author Jyoti Rai
 *
 */

public class StatusPojo implements Serializable{

	public StatusPojo() {

	}

	public int currentUserId;
	public int getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Device device;
	public String uid;	

}


class WlanInfo{
	public WlanInfo() {

	}
	public String bssid;
	public int frequency;
	public String frequencyUnit;
	public String ipAddress;
	public int linkSpeed;
	public String linkSpeedUnit;
	public int signalQuality;
	public String ssid;

	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getFrequencyUnit() {
		return frequencyUnit;
	}
	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getLinkSpeed() {
		return linkSpeed;
	}
	public void setLinkSpeed(int linkSpeed) {
		this.linkSpeed = linkSpeed;
	}
	public String getLinkSpeedUnit() {
		return linkSpeedUnit;
	}
	public void setLinkSpeedUnit(String linkSpeedUnit) {
		this.linkSpeedUnit = linkSpeedUnit;
	}
	public int getSignalQuality() {
		return signalQuality;
	}
	public void setSignalQuality(int signalQuality) {
		this.signalQuality = signalQuality;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

}

class Device{
	public Device() {

	}
	public String androidVersion;
	public int batteryCapacity;
	public String brand;
	public String manufacturer;
	public String model;
	public WlanInfo wlanInfo;
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public int getBatteryCapacity() {
		return batteryCapacity;
	}
	public void setBatteryCapacity(int batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public WlanInfo getWlanInfo() {
		return wlanInfo;
	}
	public void setWlanInfo(WlanInfo wlanInfo) {
		this.wlanInfo = wlanInfo;
	}
}
