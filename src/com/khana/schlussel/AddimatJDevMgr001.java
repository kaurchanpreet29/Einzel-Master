package com.khana.schlussel;
/**
 * Microsoft Windows specific JNI Class to AddimatJDevMgr001.dll.
 * <br/>Tested under XP and Vista/32 
 * <br/>
 * <br/>AddimatJDevMgr001.dll can be installed in C:\WINDOWS\system32 (recommended)
 * <br/>or in java\jre\bin (not recommended because of possible java version updates).
 * <br/>or in the root-directory of the application.
 * <p><b>Purpose:</b>
 * <ul>
 * <li>Get a list of connected COM ports of a specific device. 
 * <li>Reset a specific COM port driver.
 * </ul>
 * <br/>
 * <br/>Changes:
 * <ul>
 * <li>12.06.2008 disableComPort definition removed.
 * <li>12.06.2008 enableComPort  definition removed.
 * </ul>
 * 
 * @author Peter Aeschimann 31.05.2008 - Version 001
 * 
 */
public class AddimatJDevMgr001 {
	
	/**
	 * Returns a string with a list of all connected virtual com ports of a specified UsbDevice.
	 * <br/>If the string is "", no com ports are connected. 
	 * <br/>Otherwise, the com ports in the string are separated by a semicolon.
	 * 
	 * <p><b>Examples:</b>
	 * <p>String ports = AddimatDevMgr001.getPorts("Addimat USB 2 Waiter Lock (serial)");
	 * <p>ports returns "COM6;COM9;":
	 * <br/>->COM6 and COM9 are both connected to a virtual com port device named "Addimat USB 2 Waiter Lock (serial)"
	 * <p>
	 * <br/>ports returns "COM6;":
	 * <br/>->COM6 is connected to a virtual com port device named "Addimat USB 2 Waiter Lock (serial)"
	 * <p>
	 * <br/>ports returns "":
	 * <br/>->No virtual com port device named "Addimat USB 2 Waiter Lock (serial)" are connected.
	 * <p>
	 * <p><b>Example:</b>
	 * <p>String ports = AddimatDevMgr001.getPorts("");
	 * <p>ports returns "COM1;COM6;"
	 * <br/>->COM1 is usually a legacy port. On legacy ports, you cannot see, whether it is connected to a device or not.
	 * <br/>->COM6 is usually a connected virtual com port.
	 *
	 * @param  strFriendlyUsbDeviceName UsbDeviceName as seen in Microsoft Windows Device Manager
	 * @return string with a ";" separated list of all connected virtual com ports of the specified UsbDevice.
	 * 
	 */
	public static native String getComPorts (String strFriendlyUsbDeviceName);
	

	/**
	 *  Reset a specific COM port driver.
	 *  
	 *  <p><b>Example:</b>
	 *  <br/>int result = AddimatDevMgr001.resetComPort("COM6");
	 *  <br/>
	 *  <br/>If there exists a COM6, the device driver of this COM port will be reset and the result value is 0.
	 *  <br/>If there exists no COM6, nothing will be done, and the result value is 4.
	 *  <br/>If you disable a COM port that is already reset, the result value is also 0.
	 *  
	 *  @param strPortName Microsoft Windows COM port name, e.g. "COM6"
	 *  @return ErrorCodes:
	 *  <ul>
	 *  <li> 0 OK (normal case, when device is connected).
	 *  <li> 1 INVALID_DEVINFO_HANDLE (should never occur).
	 *  <li> 2 ENUMERATION_ERROR (should never occur).
	 *  <li> 3 PARAM_CHANGE_ERROR (should never occur).
	 *  <li> 4 DEVICE_NOT_FOUND (normal case, when device is disconnected).
	 *  </ul>
	 */
	public static native int resetComPort(String strPortName);
	
	
	static {
	    System.loadLibrary("AddimatJDevMgr001");
	}
}


