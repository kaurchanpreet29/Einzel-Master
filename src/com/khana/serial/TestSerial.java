package com.khana.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSerial implements SerialPortEventListener {
  // for containing the ports that will be found
  private Enumeration ports = null;
  // map the port names to CommPortIdentifiers
  private HashMap portMap = new HashMap();

  // this is the object that contains the opened port
  private CommPortIdentifier selectedPortIdentifier = null;
  private SerialPort serialPort = null;

  final static int SPACE_ASCII = 32;
  final static int DASH_ASCII = 45;
  public static final int ESC = 27;
  /** Start of Text. */
  public static final int M = 77;
  /** End of Text. */
  public static final int ETX = 3;
  public static final int SCALE = 97;
  /** Enquiry. */
  public static final int LF = 10;
  
  // input and output streams for sending and receiving data
  private InputStream input = null;
  private OutputStream output = null;

  // just a boolean flag that i use for enabling
  // and disabling buttons depending on whether the program
  // is connected to a serial port or not
  private boolean bConnected = false;

  // the timeout value for connecting with the port
  final static int TIMEOUT = 2000;

  final static int NEW_LINE_ASCII = 10;

  // a string for recording what goes on in the program
  // this string is written to the GUI
  String logText = "";
  private static final Logger LOGGER = LoggerFactory.getLogger(TestSerial.class.getSimpleName());

  public TestSerial() {
    searchForPorts();
    connect();
    initIOStream();
    initListener();
    writeData(1, 2);
    //serialEvent(evt);
  }

  // search for all the serial ports
  // pre: none
  // post: adds all the found ports to a combo box on the GUI
  public void searchForPorts() {
    ports = CommPortIdentifier.getPortIdentifiers();

    while (ports.hasMoreElements()) {
      CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

      // get only serial ports
      if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        portMap.put(curPort.getName(), curPort);
      }
    }
  }

  // connect to the selected port in the combo box
  // pre: ports are already found by using the searchForPorts method
  // post: the connected comm port is stored in commPort, otherwise,
  // an exception is generated
  public void connect() {
    String selectedPort = "COM9";
    selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
    CommPort commPort = null;

    try {
      // the method below returns an object of type CommPort
      commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
      // the CommPort object can be casted to a SerialPort object
      serialPort = (SerialPort) commPort;
      // for controlling GUI elements
      setConnected(true);
    } catch (PortInUseException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // open the input and output streams
  // pre: an open port
  // post: initialized intput and output streams for use to communicate data
  public boolean initIOStream() {
    // return value for whather opening the streams is successful or not
    boolean successful = false;

    try {
      //
      input = serialPort.getInputStream();
      output = serialPort.getOutputStream();
    System.out.println(input+"   Output  "+output);
      successful = true;
      return successful;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void initListener() {
    try {
      serialPort.addEventListener(this);
      serialPort.notifyOnDataAvailable(true);
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
  }

  final public boolean getConnected() {
    return bConnected;
  }

  public void setConnected(boolean bConnected) {
    this.bConnected = bConnected;
  }

//method that can be called to send data
  //pre: open serial port
  //post: data sent to the other device
  
//  public void writeData(int leftThrottle, int rightThrottle)
//  {
//      try
//      {
//          output.write(leftThrottle);
//          output.flush();
//          //this is a delimiter for the data
//          output.write(DASH_ASCII);
//          output.flush();
//          
//          output.write(rightThrottle);
//          output.flush();
//          //will be read as a byte so it is a space key
//          output.write(SPACE_ASCII);
//          output.flush();
//      }
//      catch (Exception e)
//      {
//          logText = "Failed to write data. (" + e.toString() + ")";
//      }
//  }
  
  public void writeData(int leftThrottle, int rightThrottle)
  {
      try
      {
          output.write(ESC);
          output.flush();
          //this is a delimiter for the data
          output.write(M);
          output.flush();
          
          output.write(ETX);
          output.flush();
          
          //will be read as a byte so it is a space key
          output.write(SCALE);
          output.flush();
          output.write(LF);
          output.flush();
          
          LOGGER.info("Reading response");
          LOGGER.debug("No. of bytes: {}", input.available());
          byte bytesIn[] = new byte[20];
          input.read(bytesIn);
          LOGGER.info("Response: {}", new String(bytesIn));
      }
      catch (Exception e)
      {
          logText = "Failed to write data. (" + e.toString() + ")";
      }
  }
  
  public void serialEvent(SerialPortEvent evt) {
    if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
      try {
        byte singleData = (byte) input.read();

        if (singleData != NEW_LINE_ASCII) {
         System.out.println(new String(new byte[] { singleData }));
        } else {
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String args[]) {
    new TestSerial();
  }
}
