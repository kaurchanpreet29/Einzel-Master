package com.khana.restclient.android;

import java.util.List;

/**
 * @author Jyoti Rai
 *
 */

public class SupportedVersion {
    public String latestVersion;
    public List<String> supportedVersions;
	public String getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}
	public List<String> getSupportedVersions() {
		return supportedVersions;
	}
	public void setSupportedVersions(List<String> supportedVersions) {
		this.supportedVersions = supportedVersions;
	}
	public SupportedVersion() {
		
	}
}