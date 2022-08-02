package com.floreantpos.ui.views.order;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.floreantpos.config.TerminalConfig;

public class CashDrawerPOS {
	static  Enumeration       portList;
	static CommPortIdentifier portId;
	static OutputStream       outputStream;
	static SerialPort	      serialPort;
	public static void startPort() {
		System.out.println("com1 Started");
		boolean portFound = false;
		String  defaultPort = TerminalConfig.getCashDrawerCom();
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(defaultPort)) {
					portFound = true;
					try {
						serialPort = (SerialPort) portId.open("SimpleWrite", 2000);

					} catch (PortInUseException e) {
						System.out.println("Port is offline now.");
						continue;
					} 

					try {
						outputStream = serialPort.getOutputStream();

					} catch (IOException e) {

					}

					try {
						serialPort.setSerialPortParams(9600, 
								SerialPort.DATABITS_8, 
								SerialPort.STOPBITS_1, 
								SerialPort.PARITY_NONE);
						System.out.println("Display is online now");
					} catch (UnsupportedCommOperationException e) {

					}


					try {
						serialPort.notifyOnOutputEmpty(true);
					} catch (Exception e) {
						System.out.println("Error setting event notification");
						System.out.println(e.toString());
						System.exit(-1);
					}


					try {
						Thread.sleep(2000); 
					} catch (Exception e) {}

				} 
			} 
		} 

		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
		} 
	}   
	public static void OpenDrawer() throws IOException{
		if(outputStream == null)
			startPort();
		
		if(outputStream == null)
			return;
		
		try{
			outputStream.write(ESCPOS.Drawer);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeCom()
	{
		try
		{
			outputStream.close();
			serialPort.close();
			outputStream = null;
			serialPort = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
