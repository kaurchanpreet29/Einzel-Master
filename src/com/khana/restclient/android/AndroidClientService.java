package com.khana.restclient.android;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jyoti Rai
 *
 */

public class AndroidClientService {
	public static List<AndroidClientInfo> clientList;
	private static AndroidClient androidRestController;
	private static AndroidClient getAndroidRestController() {
		return androidRestController;
	}

	public static List<AndroidClientInfo> getClientList() {
		return clientList;
	}

	public static void addClientList(AndroidClientInfo clientInfo) {
		boolean add = true;
		if(!clientList.isEmpty()) {
			for(AndroidClientInfo client:clientList) {
				if(client.getClientId().compareTo(clientInfo.getClientId())==0) {
					add = false;
					break;
				}
			}

		}
				
		if(clientList.isEmpty()||add) {
			clientList.add(clientInfo);
			}
	}

	public AndroidClientService() {
		init();
	}

	private void init() {
		clientList = new ArrayList<>();
		androidRestController = new AndroidClient();
	}

	public static void onChanged(String onChanged) {
		for(AndroidClientInfo currentUser:clientList)
			androidRestController.postOnchange(currentUser, onChanged);
	}

	public static void ping(AndroidClientInfo currentUser) {		
		androidRestController.postPing(currentUser);
	}
	public static void bell(AndroidClientInfo currentUser) {		
		androidRestController.postBell(currentUser);
	}
	public static StatusPojo getStatus(AndroidClientInfo currentUser) {	
		return androidRestController.getStatus(currentUser);
	}
	public static void restart(AndroidClientInfo currentUser) {		
		androidRestController.restart(currentUser);
	}
	public static void logOut(AndroidClientInfo currentUser) {		
		androidRestController.signOut(currentUser);
	}

}
