package com.floreantpos.add.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.event.UsbServicesListener;

import org.apache.commons.collections.map.HashedMap;
public class Hotplugger {
 public static void main(String[] args)
 throws UsbException, InterruptedException {
 UsbServices services = UsbHostManager.getUsbServices();
 services.addUsbServicesListener((UsbServicesListener) new UsbServicesListener() {
	
	@Override
	public void usbDeviceDetached(UsbServicesEvent event) {
		UsbDevice device = event.getUsbDevice( );
		if((""+device.getUsbDeviceDescriptor().idProduct()+device.getUsbDeviceDescriptor().idVendor()).equals("40962316"))
			System.out.println(getDeviceInfo(device) + " was removed from the bus.");
		
	}
	
	@Override
	public void usbDeviceAttached(UsbServicesEvent event) {
		UsbDevice device = event.getUsbDevice( );
		System.out.println(getDeviceInfo(device) + " was added to the bus."+device.getUsbDeviceDescriptor().idProduct()+device.getUsbDeviceDescriptor().idVendor());
		
		HashMap<String, String> map = new HashMap<String, String>(1);
		map.put("PROCESSOR_SP_ID", device.getUsbDeviceDescriptor().idVendor()+device.getUsbDeviceDescriptor().idProduct()+"");
		try {
			setEnv(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
});
 // Keep this program from exiting immediately
 Thread.sleep(500000);
 }
 
 private static String getDeviceInfo(UsbDevice device) {
	 try {
	 String product = device.getProductString( );
	 String serial = device.getSerialNumberString( );
	 if (product == null) return "Unknown USB device";
	 if (serial != null) return product + " " + serial;
	 else return product;
	 }
	 catch (Exception ex) {
	 }
	 return "Unknown USB device";
	 }
 
 
 protected static void setEnv(Map<String, String> newenv) throws Exception {
	  try {
	    Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
	    Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
	    theEnvironmentField.setAccessible(true);
	    Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
	    env.putAll(newenv);
	    Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
	    theCaseInsensitiveEnvironmentField.setAccessible(true);
	    Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
	    cienv.putAll(newenv);
	  } catch (NoSuchFieldException e) {
	    Class[] classes = Collections.class.getDeclaredClasses();
	    Map<String, String> env = System.getenv();
	    for(Class cl : classes) {
	      if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
	        Field field = cl.getDeclaredField("m");
	        field.setAccessible(true);
	        Object obj = field.get(env);
	        Map<String, String> map = (Map<String, String>) obj;
	        map.clear();
	        map.putAll(newenv);
	      }
	    }
	  }
	}
 
}
