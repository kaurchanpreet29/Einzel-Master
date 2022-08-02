package com.khana.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
 
public class SerialPortReadWrite {
   InputStream in;
   OutputStream out;
 
    public SerialPortReadWrite() {
        super();
    }
 
public void open() throws PortInUseException {
        Enumeration port_list = CommPortIdentifier.getPortIdentifiers();
 
        while (port_list.hasMoreElements()) {
            CommPortIdentifier port_id = (CommPortIdentifier) port_list.nextElement();
            if (port_id.getName().equals("/dev/ttyS0")) {
 
                try {
                    SerialPort port = (SerialPort) port_id.open("PortListOpen", 20);
                    System.out.println("Opened successfully");
                    try {
                        int baudRate = 9600; //
                        port.setSerialPortParams(
                                baudRate,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        port.setDTR(true);
                  
                        System.out.println("properties are set");
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println(e);
                    }
                }finally {
					
				}
            }
        }
}   
    public void SerialReader ( InputStream in )
        {
            this.in = in;
        }
 
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    //System.out.println("Received a signal.");
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    
   
   public void SerialWriter (OutputStream out){
            this.out = out;
   }
  
        public void run1 ()
        {
            try
            {                
//              String toSend = "229";
 //               this.out.write(229);
                byte[] array = {0x02, 0x00, 0x01};
                while ( true )
                {
                   this.out.write(new byte[]{0x02, 0x00, 0x01});
                   this.out.flush();
                     
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
   
}

