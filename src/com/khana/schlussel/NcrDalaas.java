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

public class NcrDalaas implements SerialPortEventListener{

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

	public void StartNcrDalaasSerialCOM() {
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
						System.out.println("Port is used...");
					}

					try {
						serialPort.setSerialPortParams(9600, 
								SerialPort.DATABITS_8, 
								SerialPort.STOPBITS_1, 
								SerialPort.PARITY_NONE);
						serialPort.setRTS(true);
						serialPort.setDTR(true);
						System.out.println("NCR Kellnerschloos is online now");
					} catch (UnsupportedCommOperationException e) {
						System.out.println("Unable to set port");
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
					} catch (Exception e) {
						e.printStackTrace();
					}

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
		byte[] buffer = new byte[48];
		int len = -1;
		try
		{
			inputStream2.read(buffer);
			String strData = new String(buffer);
			if(strData.equals("OUT")) {
				setValue(null);
				pass.doLoginAdimat("asdfasdf");
			}else if(!strData.isEmpty()){
				strData = strData.replace("OUT", "");
				strData = strData.substring(5, strData.length());				
				setValue(strData);						
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
				if(strData.equals("OUT")) {
					setValue(null);
					pass.doLoginAdimat("asdfasdf");
				}else if(!strData.isEmpty()){
					strData = strData.replace("OUT", "");
					strData = strData.substring(5, strData.length());				
					setValue(strData);						
					pass.doLoginAdimat(strData);
				}	
			} catch (IOException e) {System.out.println(e);}
			break;
		}
	}


}
