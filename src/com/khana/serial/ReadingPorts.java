package com.khana.serial;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 *
 * @author IamUsman
 */
public class ReadingPorts implements SerialPortEventListener, Runnable {

  static CommPortIdentifier portId;
  static Enumeration portList;
  static SerialPort port;
  static InputStream inputStream;
  static OutputStream outputStream;
  static Thread readThread;
  static byte buffer[];
  static BufferedReader br;

  /**
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    //start();
  }

  public ReadingPorts() {
    try {
      port = (SerialPort) portId.open("COM9", 500);
      inputStream = port.getInputStream();
      outputStream = port.getOutputStream();
      br = new BufferedReader(new InputStreamReader(inputStream));
      System.out.println("** Connected To COM8 **");
      port.addEventListener(this);
      port.notifyOnDataAvailable(true);
      port.setSerialPortParams(9600, SerialPort.DATABITS_8,
          SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
      port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
      //port.enableReceiveTimeout(500);
      System.out.println("................................");
//      readThread = new Thread(this);
//      readThread.start();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  
  public void start() {
	  portList = CommPortIdentifier.getPortIdentifiers();
	    while (portList.hasMoreElements()) {
	      portId = (CommPortIdentifier) portList.nextElement();
	      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	        if (portId.getName().equals("COM9")) {
	          if (!portId.isCurrentlyOwned()) {
	            ReadingPorts rp = new ReadingPorts();	            
	          } else {
	            System.out
	                .println("This port is already used by some other program");
	          }

	        }
	      }
	    }
  }

  public void serialEvent(SerialPortEvent event) {

    switch (event.getEventType()) {

    case SerialPortEvent.DATA_AVAILABLE:
      buffer = new byte[8];
      try {
        while (inputStream.available() > 0) {
          int numBytes = inputStream.read(buffer);
          System.out.println(new String(buffer, 0, numBytes));
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      break;

    }

  }

  @Override
  public void run() {
    try {
      System.out.println("In Run");
      Thread.sleep(2000);
    } catch (Exception ex) {
      ex.printStackTrace();
      ;
    }

  }
  
  public void sendCommand(String cmd) {
	  try {
		SerialWriter writer = new SerialWriter(port.getOutputStream(), cmd);
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
  
  public class SerialWriter implements Runnable {

	    OutputStream out;
	    String str;
	    public SerialWriter(OutputStream out, String str) {
	        this.out = out;
	        this.str = str;
	    }

	    public void run() {
	        try {

	            byte[] array = this.str.getBytes();

	                this.out.write(array);
	                this.out.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
  
  public void sendData() {
	   try {
		   outputStream.write(new byte[]{0x1B, 0x4D, 0x03, 0x61, 0x0A});
		   outputStream.flush();    
     
      byte bytesIn[] = new byte[20];
      inputStream.read(bytesIn);
      System.out.println(new String(bytesIn));
	   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
  }
}