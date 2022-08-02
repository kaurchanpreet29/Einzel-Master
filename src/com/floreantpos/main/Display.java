package com.floreantpos.main;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.floreantpos.config.TerminalConfig;
public class Display {

    static  Enumeration       portList;
    static CommPortIdentifier portId;
    static SerialPort       serialPort;
    static OutputStream       outputStream;
    private static Display instance;
    static boolean        outputBufferEmptyFlag = false;
    static boolean success = false;
  
 public void StartDisplay() {
    boolean portFound = false;
    String  defaultPort = TerminalConfig.getCom();
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
  else
    success = true;
    }   
    public void ClearDisplay(){
        try{
          if(!success)return;
          /*clearFirstLine();
            clearSecondLine();*/
          outputStream.write(ESCPOS.SELECT_DISPLAY);
            outputStream.write(ESCPOS.VISOR_CLEAR);
            outputStream.write(ESCPOS.Down_Line);
            outputStream.write(ESCPOS.VISOR_CLEAR);
            outputStream.write(ESCPOS.VISOR_HOME);
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
   
  public void clearFirstLine(){
    try{
      if(!success)return; 
    String text = "                    ";
    outputStream.write(text.getBytes());
    outputStream.flush();
    }
    catch(IOException r){
    }
    
  }
  public void clearSecondLine(){
      try{
        if(!success)return; 
      String text = "                    ";
      outputStream.write(ESCPOS.SELECT_DISPLAY);
      outputStream.write(ESCPOS.Down_Line);
      outputStream.write(ESCPOS.Left_Line);
      outputStream.write(text.getBytes());
      outputStream.flush();
      }
      catch(IOException r){
      }
      
  }
  
  public void PrintFirstLine(String text){
    try{
      if(!success)return; 
    ClearDisplay();
    if(text.length()>20)           
        text=text.substring(0,20);
    outputStream.write(text.getBytes());
    outputStream.flush();
   
    }
    catch(IOException r){
    }
    
  }
  public void PrintSecondLine(String text){
    try{
    if(!success)return; 
    outputStream.write(ESCPOS.SELECT_DISPLAY);
    outputStream.write(ESCPOS.Down_Line);
    outputStream.write(ESCPOS.Left_Line);
    if(text.length()>20)
        text=text.substring(0,20);
        outputStream.write(text.getBytes());
        outputStream.flush();
    }
    catch(IOException y){
        System.out.println("Failed to print second line because of :"+y);
    }
  }
  public void ShowGreeting(){
    if(!success)return; 
    String text1="*****Thank You******";                             
    String text2="     Come Again     ";                              
    ClearDisplay();
    PrintFirstLine(text1);
    PrintSecondLine(text2);
        try {
            Thread.sleep(5000);                                  
        } catch (InterruptedException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearDisplay();
  }
    public void init(){
    try{
     outputStream.write(ESCPOS.Anim);
    }
    catch(IOException i){}
    }

    public void close(){
     serialPort.close();
     System.exit(1);
   
    }
    public static Display getInstance()
    {
      if(instance == null)
      {
        instance = new Display();
        instance.StartDisplay();
        return instance;
      }
      else
        return instance;
    }
}
