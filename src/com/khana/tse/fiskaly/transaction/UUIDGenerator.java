package com.khana.tse.fiskaly.transaction;

import java.util.UUID;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class UUIDGenerator {
	
	public static UUID generateUUID() {
		return UUID.randomUUID();
	}
	public static UUID createUUIDV4(String systemId) {
		return UUID.nameUUIDFromBytes(systemId.getBytes());     
		
	}
	public static UUID getClientUniqueID() {
		SystemInfo systemInfo = new SystemInfo();
		OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
	    HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
	    ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

	    String vendor = operatingSystem.getManufacturer();
	    String processorSerialNumber = computerSystem.getSerialNumber();
		return UUID.nameUUIDFromBytes((vendor+processorSerialNumber).getBytes());     
		
	}
	

	
}
