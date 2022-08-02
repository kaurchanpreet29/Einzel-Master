package com.floreantpos.services;

import java.util.Calendar;
import java.util.Date;

import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class SystemServiceUtil {
	public static SystemServiceUtil myService;
	public static  String processorSerialNumber;
	public static String vendor;
	public static String getVendor() {
		return vendor;
	}

	public static void setVendor(String vendor) {
		SystemServiceUtil.vendor = vendor;
	}
	public static void main(final String[] args) {
		init();
	}

	public static String getProcessorSerialNumber() {
		return processorSerialNumber;
	}

	public static void setProcessorSerialNumber(String processorSerialNumber) {
		SystemServiceUtil.processorSerialNumber = processorSerialNumber;
	}

	public static SystemServiceUtil getMyService() {
		if(myService==null)
			myService = new SystemServiceUtil();
		return myService;
	}

	public static void setMyService(SystemServiceUtil myService) {
		SystemServiceUtil.myService = myService;
	}

	public SystemServiceUtil() {
		init();
	}

	private static void init() {
		try {
			SystemInfo systemInfo = new SystemInfo();
			OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
			HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
			ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
			setVendor(operatingSystem.getManufacturer());
			setProcessorSerialNumber(computerSystem.getSerialNumber());	
		}catch(Exception t) {
			setVendor(null);
			setProcessorSerialNumber(null);
			t.printStackTrace();
		}finally {

		}

	}

	public static boolean shouldTseInfo(Date checkDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 28);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.YEAR, 2021);	
		cal.set(Calendar.HOUR_OF_DAY, 05);
		cal.set(Calendar.MINUTE, 59);
		Date startDate = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.YEAR, 2021);
		Date endDate = cal.getTime();
		return startDate.compareTo(checkDate) * checkDate.compareTo(endDate) >= 0;
	}
}
