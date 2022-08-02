package com.khana.weight.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.ESCPOS;
import com.floreantpos.ui.dialog.WeightDialog;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class ScaleWeight implements SerialPortEventListener{
	static  Enumeration       portList;
	static CommPortIdentifier portId;
	static SerialPort       serialPort;
	static OutputStream       outputStream;
	static BufferedReader 		inputStream;
	private static ScaleWeight instance;
	static boolean        outputBufferEmptyFlag = false;
	static boolean success = false;
	static String weight;

	public static String getWeight() {
		return weight;
	}
	public static void setWeight(String weight) {
		ScaleWeight.weight = weight;
	}
	public void StartScaleWeight() {
		boolean portFound = false;
		String  defaultPort = TerminalConfig.getComWeight();
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
						System.out.println("Weight Machine is online now");
					} catch (UnsupportedCommOperationException e) {

					}

					try {
						serialPort.notifyOnOutputEmpty(true);
						serialPort.addEventListener(this);
						serialPort.notifyOnDataAvailable(true);
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
		else
			success = true;
	}   
	public void reqStableWeights(){
		try{
			if(!success)return;        
			outputStream.write(WEIGHTS.REQ_STABLE_RESULT);        
			outputStream.flush();
		}
		catch(IOException e){

		}
	}
	public boolean isOutputStream()
	{
		if(outputStream != null)
			return true;
		else
			return false;

	}

	public void reqImmediateWeights(){
		try{
			if(!success)return; 
			outputStream.write(WEIGHTS.REQ_IMMEDIATE_RESULT);
			outputStream.flush();
		}
		catch(IOException r){
		}

	}



	public void SerialReader (java.io.InputStream inputStream2)
	{
		byte[] buffer = new byte[1024];
		int len = -1;
		try
		{

			inputStream2.read(buffer);
			//System.out.println("Received a signal.");
			System.out.print(new String(buffer));

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}            
	}

	public void reqStableBasicWeights(){
		try{
			if(!success)return; 

			outputStream.write(WEIGHTS.REQ_STABLE_RESULT_BASIC);     
			outputStream.flush();

			//      try {
			//    	  inputStream = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			//    	  System.out.println(inputStream.toString());
			//            } catch (IOException e) {
			//                       
			//                        }
		}
		catch(IOException r){
		}      
	}

	public void init(){
		try{
			outputStream.write(ESCPOS.Anim);
		}
		catch(IOException i){}
	}

	public void close(){
		serialPort.close();   
	}
	public static ScaleWeight getInstance()
	{     
		instance = new ScaleWeight();       
		return instance;     
	}
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
				System.out.print("Result "+ new String(readBuffer));
				setWeight(new String(readBuffer));
				try {
					WeightDialog.setWeight(new String(readBuffer));
				}catch(Exception ex) {
					
				}			

			} catch (IOException e) {System.out.println(e);}
			break;
		}
	}
}
