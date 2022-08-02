package com.khana.serial;


import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRead implements Runnable, SerialPortEventListener {
  static CommPortIdentifier portId;
  static Enumeration portList;

  InputStream inputStream;
  OutputStream output;
  SerialPort serialPort;
  Thread readThread;
  byte[] readBuffer;
  
  public static final int ESC = 27;
  /** Start of Text. */
  public static final int M = 77;
  /** End of Text. */
  public static final int ETX = 3;
  public static final int SCALE = 97;
  /** Enquiry. */
  public static final int LF = 10;
  
  private boolean bConnected = false;

  // the timeout value for connecting with the port
  final static int TIMEOUT = 2000;

  final static int NEW_LINE_ASCII = 10;

  // a string for recording what goes on in the program
  // this string is written to the GUI
  String logText = "";
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRead.class.getSimpleName());

  public static void main(String[] args) {
    portList = CommPortIdentifier.getPortIdentifiers();
    System.out.println("portList... " + portList);
    while (portList.hasMoreElements()) {
      portId = (CommPortIdentifier) portList.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        System.out.println("port identified is Serial.. "
            + portId.getPortType());
        if (portId.getName().equals("COM9")) {
          System.out.println("port identified is COM8.. " + portId.getName());
          // if (portId.getName().equals("/dev/term/a")) {
          SimpleRead reader = new SimpleRead();
        } else {
          System.out.println("unable to open port");
        }
      }
    }
  }

  public SimpleRead() {
    try {
      System.out.println("In SimpleRead() contructor");
      serialPort = (SerialPort) portId.open("SimpleReadApp1111", 500);
      System.out.println(" Serial Port.. " + serialPort);
    } catch (PortInUseException e) {
      System.out.println("Port in use Exception");
    }
    try {
      inputStream = serialPort.getInputStream();
      output = serialPort.getOutputStream();
      System.out.println(" Input Stream... " + inputStream);
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
    try {
      serialPort.addEventListener(this);

    } catch (TooManyListenersException e) {
      System.out.println("Tooo many Listener exception");
    }
    serialPort.notifyOnDataAvailable(true);
    try {

      serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
          SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

      // no handshaking or other flow control
      serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

      // timer on any read of the serial port
      serialPort.enableReceiveTimeout(500);

      System.out.println("................");

    } catch (UnsupportedCommOperationException e) {
      System.out.println("UnSupported comm operation");
    }
    readThread = new Thread(this);
    readThread.start();
  }

  public void run() {
    try {
      System.out.println("In run() function ");
      Thread.sleep(500);
      } catch (InterruptedException e) {
      System.out.println("Interrupted Exception in run() method");
    }
  }

  public void serialEvent(SerialPortEvent event) {

    // System.out.println("In Serial Event function().. " + event +
    // event.getEventType());
    switch (event.getEventType()) {
    /*
     * case SerialPortEvent.BI: case SerialPortEvent.OE: case
     * SerialPortEvent.FE: case SerialPortEvent.PE: case SerialPortEvent.CD:
     * case SerialPortEvent.CTS: case SerialPortEvent.DSR: case
     * SerialPortEvent.RI: case SerialPortEvent.OUTPUT_BUFFER_EMPTY: break;
     */
    case SerialPortEvent.DATA_AVAILABLE:
      readBuffer = new byte[8];

      try {

        while (inputStream.available() > 0) {

          int numBytes = inputStream.read(readBuffer);
          // System.out.println("Number of bytes read " + numBytes);
        }

        System.out.print(new String(readBuffer));

      } catch (IOException e) {
        System.out.println("IO Exception in SerialEvent()");
      }
      break;
    }
  }
  
  public void writeData()
  {
      try
      {
//          output.write(ESC);
//          output.flush();
//          //this is a delimiter for the data
//          output.write(M);
//          output.flush();
//          
//          output.write(ETX);
//          output.flush();
//          
//          //will be read as a byte so it is a space key
//          output.write(SCALE);
//          output.flush();
//          output.write(LF);
//          output.flush();
    	  
    	  output.write(new byte[]{0x1B, 0x4D, 0x03, 0x61, 0x0A});
          
          LOGGER.info("Reading response");
          LOGGER.debug("No. of bytes: {}", inputStream.available());
          byte bytesIn[] = new byte[20];
          inputStream.read(bytesIn);
          LOGGER.info("Response: {}", new String(bytesIn));
          System.out.println(bytesIn.toString());
      }
      catch (Exception e)
      {
          logText = "Failed to write data. (" + e.toString() + ")";
      }
  }
  
}