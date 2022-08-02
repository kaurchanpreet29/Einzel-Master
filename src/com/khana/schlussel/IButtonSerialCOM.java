package com.khana.schlussel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.ESCPOS;
import com.floreantpos.ui.views.PasswordScreen;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class IButtonSerialCOM implements SerialPortEventListener{

	static  Enumeration       portList;
	static CommPortIdentifier portId;
	static SerialPort    serialPort;
	static OutputStream    outputStream;
	static BufferedReader 	inputStream;
	static boolean        outputBufferEmptyFlag = false;
	static boolean success = false;
	String Value;

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}
	//	public void IButtonSerialCOM() {
	//		StartIButtonSerialCOM();
	//	}

	public void StartIButtonSerialCOM() {
		pass = new PasswordScreen();
		boolean portFound = false;
		String  defaultPort = TerminalConfig.getAdimatCom();
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
						System.out.println("Ibutton is online now");
					} catch (UnsupportedCommOperationException e) {

					}


					try {
						serialPort.addEventListener(this);
						serialPort.notifyOnDataAvailable(true);
					} catch (Exception e) {
						System.out.println("Error setting event notification");
						System.out.println(e.toString());
						System.exit(-1);
					}


					try {
						SerialReader(serialPort.getInputStream());
					} catch (Exception e) {}

				} 
			} 
		} 

		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
		} 
		else
			success = true;
	}   

	public boolean isOutputStream()
	{
		if(outputStream != null)
			return true;
		else
			return false;

	}



	public void SerialReader (java.io.InputStream inputStream2)
	{
		byte[] buffer = new byte[20];
		int len = -1;
		try
		{
			inputStream2.read(buffer);
			String strData = new String(buffer);
			if(strData.contains("_")) {				
				strData = strData.substring(1, 8);
				if(strData.compareTo("0000000")!=0)
					setValue(strData);
				else
					setValue(null);				
				pass.doLoginAdimat(strData);
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}            
	}


	public void init(){
		try{
			outputStream.write(ESCPOS.Anim);
		}
		catch(IOException i){}
	}

	PasswordScreen pass;

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];
			try {
				while (serialPort.getInputStream().available() > 0) {
					int numBytes = serialPort.getInputStream().read(readBuffer);
				}
				String strData = new String(readBuffer);
				if(strData.contains("_")) {				
					strData = strData.substring(1, 8);
					if(strData.compareTo("0000000")!=0)
						setValue(strData);
					else
						setValue(null);				
					pass.doLoginAdimat(strData);
				}
			} catch (IOException e) {System.out.println(e);}
			break;
		}
	}


}
