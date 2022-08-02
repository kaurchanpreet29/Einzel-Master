package com.khana.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
  
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class declaration
 * 
 * 
 * @author
 * @version 1.8, 08/03/00
 */
public class TestMySerial implements Runnable, SerialPortEventListener
{
   static CommPortIdentifier portId1;
  
   InputStream inputStream;
   OutputStream ouputStream;
  
   byte[] totalReadBuffer = null;
  
   public SerialPort serialPort1;
  
   Thread readThread;
  
   protected String divertCode = "10";
  
   static String TimeStamp;
  
   String scannedInput = "";
  
   String tempInput = "";
  
   /**
    * Method declaration
    * 
    * 
    * @param args
    * 
    * @see
    */
   private static final Logger LOGGER = LoggerFactory.getLogger(TestMySerial.class.getSimpleName());

   public static void main(String[] args)
   {
  
     
   };
   
   
   
   public void openPorts() {
	   try
	      {
	         Enumeration ports = CommPortIdentifier.getPortIdentifiers();
	         while(ports.hasMoreElements())
	         {
	            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
	            String type;
	            switch(port.getPortType())
	            {
	            case CommPortIdentifier.PORT_PARALLEL:
	               type = "Parallel";
	               break;
	            case CommPortIdentifier.PORT_SERIAL:
	               type = "Serial";
	               break;
	            default: // / Shouldn't happen
	               type = "Unknown";
	               break;
	            }
	            System.out.println(port.getName() + ": " + type);
	         }
	  
	         portId1 = CommPortIdentifier.getPortIdentifier("COM9");
	         
	      }
	      catch(Exception e)
	      {
	         TimeStamp = new java.util.Date().toString();
	         System.out.println(TimeStamp + ": COM9 " + portId1);
	         System.out.println(TimeStamp + ": msg1 - " + e);
	      }
   }
   
  
   /**
    * Constructor declaration
 * @throws NoSuchPortException 
    * 
    * 
    * @see
    */
   public TestMySerial() throws NoSuchPortException
   {
      try
      {
    	  
   	   	portId1 = CommPortIdentifier.getPortIdentifier("COM9");

         TimeStamp = new java.util.Date().toString();
         serialPort1 = (SerialPort) portId1.open("ComControl", 2000);
         System.out.println(TimeStamp
                            + ": "
                            + portId1.getName()
                            + " opened for scanner input");
      }
      catch(PortInUseException e)
      {
      }
      try
      {
         inputStream = serialPort1.getInputStream();
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
      try
      {
         serialPort1.addEventListener(this);
      }
      catch(TooManyListenersException e)
      {
      }
      serialPort1.notifyOnDataAvailable(true);
      try
      {
  
         serialPort1.setSerialPortParams(9600,
                                         SerialPort.DATABITS_8,
                                         SerialPort.STOPBITS_1,
                                         SerialPort.PARITY_NONE);
  
         serialPort1.setDTR(false);
         serialPort1.setRTS(false);
  
      }
      catch(UnsupportedCommOperationException e)
      {
         e.printStackTrace();
      }
  
      readThread = new Thread(this);
      readThread.start();
   }
  
   /**
    * Method declaration
    * 
    * 
    * @see
    */
   public void run()
   {
      try
      {
         Thread.sleep(30000);
      }
      catch(InterruptedException e)
      {
         e.printStackTrace();
      }
   }

   public void sendData() {
	   try {
		ouputStream = serialPort1.getOutputStream();
		ouputStream.write(new byte[]{0x1B, 0x4D, 0x03, 0x61, 0x0A});
		ouputStream.flush();
	     
       LOGGER.info("Reading response");
       LOGGER.debug("No. of bytes: {}", inputStream.available());
       byte bytesIn[] = new byte[20];
       inputStream.read(bytesIn);
       LOGGER.info("Response: {}", new String(bytesIn));
       System.out.println(new String(bytesIn));
	   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
   }
   
  
   /**
    * Method declaration
    * 
    * 
    * @param event
    * 
    * @see
    */
   public void serialEvent(SerialPortEvent event)
   {
      switch(event.getEventType())
      {
      case SerialPortEvent.BI:
      case SerialPortEvent.OE:
      case SerialPortEvent.FE:
      case SerialPortEvent.PE:
      case SerialPortEvent.CD:
      case SerialPortEvent.CTS:
      case SerialPortEvent.DSR:
         System.out.println("data set ready");
      case SerialPortEvent.RI:
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
         System.out.println("empty");
         break;
      case SerialPortEvent.DATA_AVAILABLE:
         TimeStamp = new java.util.Date().toString();
         System.out.println("*DATA avail: " + TimeStamp);
  
         try
         {
            System.out.println("count: " + inputStream.available());
            byte[] readBuffer = new byte[inputStream.available()];
            while(inputStream.available() > 0)
            {
               int numBytes = inputStream.read(readBuffer);
            }
            totalReadBuffer = readBuffer;
            System.out.print(new String(readBuffer));
         }
         catch(IOException e)
         {
         }
         catch(Exception ex)
         {
            ex.printStackTrace();
         }
         break;
      }
      System.out.println("end of event");
   }
}