package com.khana.schlussel;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.ui.views.PasswordScreen;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
/**
 * SerialComm enth�lt alle Funktionen f�r die Kommunikation mit einem
 * Addimat USB 2 Stiftschloss, f�r die COM Port Datenkommunikation 
 * wird RXTXComm.jar eingesetzt (www.rtxt.org).
 * 
 * @author Peter Aeschimann 14. Juli 2008  aap2@bfh.ch 
 *
 */



public class SerialComm {	
	Boolean serialPortGeoeffnet=false;
	Boolean hexMode=false;
	CommPortIdentifier serialPortId;
	SerialPort serialPort;
	OutputStream outputStream;
	InputStream inputStream;
	String portName;
	String errMsg="";	
	@SuppressWarnings("unchecked")
	Enumeration enumComm;
	String Value;
	
	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	Timer timer = new Timer();
	AddimatListener pAddimatUSB2Testprogram;
	
	ReceiveMode receiveMode;
	Boolean bAlternatePolling = false;
	

	/**
	 * 
	 * @param ptr Referenz auf das Main-Programm, damit ein direkter
	 * Zugriff auf die Anzeigefelder f�r die Stiftschlossnummer m�glich ist.
	 * 
	 */
	public SerialComm(AddimatListener ptr) {
		pAddimatUSB2Testprogram = ptr;
		timer.schedule  ( new Task(), 300, 300 );
	}
	
	public String getErrorMessage() {
		return errMsg;
	}
		

	public boolean isOpen() {
		return serialPortGeoeffnet;
	}
	/**
	 * Oeffnet die angegebene COM Port, initialisiert einen 
	 * input- und output Stream und setzt ein internes Flag, dass
	 * die COM Port geoeffnet ist.
	 * 
	 * @param portName COM Port Name, z.B. "COM17"
	 * @return
	 * <li> 0: OK
	 * <li>-1: COM Port Name nicht definiert.
	 * <li>-2: COM Port wird bereits benuetzt.
	 * <li>-3: Fehler beim Oeffnen.
	 * <br>Eine textuelle Fehlerbeschreibung kann mit getErrorMessage() erhalten werden.
	 */
	public int openSerialPort(String portName) {
		
		serialPortGeoeffnet = false;
		int errorcode=0;
		
		errMsg="";
		if (portName.length()==0) {
			errMsg="COM Port Name nicht definiert.";
			errorcode = -1;
		}
		else {		
			try {
				serialPortId = CommPortIdentifier.getPortIdentifier(portName);
				if (serialPortId.isCurrentlyOwned()) {
					errMsg = "COM Port "+portName+" wird bereits ben�tzt.\n";
					errorcode = -2;
				}
				else {
					serialPort = (SerialPort) serialPortId.open(this.getClass().getName(),2000);
					if(TerminalConfig.getKeyType().compareTo("addimat")==0) {
						serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
					}else
						serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
					
					serialPort.setDTR(true);
					outputStream = serialPort.getOutputStream();
					inputStream = serialPort.getInputStream();
					serialPort.addEventListener(new serialPortEventListener());
					serialPort.notifyOnDataAvailable(true);	
					serialPortGeoeffnet = true;
					System.out.println("COM Port is open.\n");					
				} 
			}catch (Exception e) {
				errMsg = "Fehler beim Oeffnen von "+portName+".\n"+e.getMessage();
				errorcode = -3;
			}
		}
		return errorcode;
	}
		
	/**
	 * <br/>Stops the polling timer and closes the serial port (if open).
	 * @return 
	 * <li> 0 OK
	 * <li>-1 Serialport cannot be closed.
	 * <br>Eine textuelle Fehlerbeschreibung kann mit getErrorMessage() erhalten werden.
	 * 
	 * 
	 */
	public int closeSerialPort() {
		int errorcode=0;
		timer.cancel(); // Polling beenden
		try {
			Thread.sleep(500); // L�nger warten als 1 Polling Cycle
			} catch (InterruptedException e){
		}
		try {
			serialPort.close();
			System.out.println("COM Port has been closed.\n");
			serialPortGeoeffnet = false;
		}
		catch (Exception e) {
			errMsg = "Port kann nicht geschlossen werden. "+e.getMessage();
			System.err.println(errMsg);
			errorcode=-1;
		}
		return errorcode;
	}
	
	public void setReceiveMode(ReceiveMode mode) {
	
		switch(mode) {
			case ENQ1_ENQ9:		
				receiveMode=ReceiveMode.ENQ1;
				bAlternatePolling=true;
				break;
			default:			
				receiveMode = mode;
				bAlternatePolling=false;
			break;
		}
	}
	
    String toHexString ( byte[] b ,int len) {
		char[] hexChar = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    	StringBuffer sb = new StringBuffer( b.length * 2 );
    	for ( int i=0; i<len; i++ ) {
    		// High Nibble
    		sb.append( hexChar [( b[i] & 0xf0 ) >>> 4] ); // fill left with zero bits
    		// Low Nibble
    		sb.append( hexChar [b[i] & 0x0f] );
    	}
    	return sb.toString();
    }
    PasswordScreen pass = new PasswordScreen();
    void serialPortDatenVerfuegbar() {
		// Es wird hier angenommen, dass immer das ganze Telegram, vom Anfang bis \r\n 
		// ankommt, was beim neuen Stiftschoss zutrifft, beim alten nicht.
		// Beim alten Stiftschlosses (RS232, COM1, 9600Bd) m�ssten die einzelnen Datenpakete 
		// zuerst gesammelt und verbunden werden, bis das letzte Packet ankommt. 
		try {
			byte[] data = new byte[150];
			int num;
			while(inputStream.available() > 0) {
				num = inputStream.read(data, 0, data.length);
				String strData;				
				switch (receiveMode) {
				case STANDARD:
					strData = new String(data, 0, num);
//					pAddimatUSB2Testprogram.tfStandardMode.setText(strData);
						if(strData.length()>16)
							setValue(strData.substring(0,16));					
					pass.doLoginAdimat(strData);
					break;
				case ENQ1:
					strData = toHexString(data,num);
					if(strData.length()>16)
						setValue(strData.substring(0,16));		
//					PasswordScreen pass1 = new PasswordScreen();
					pass.doLoginAdimat(strData);
					pAddimatUSB2Testprogram.tfEnq1Mode.setText(strData);
					if (bAlternatePolling) {
    					receiveMode = ReceiveMode.ENQ9;
    				}
					break;
				case ENQ9:
					strData = toHexString(data,num);
					pAddimatUSB2Testprogram.tfEnq9Mode.setText(strData);
					if (bAlternatePolling) { // wenn ENQ1_ENQ9 mode
    					receiveMode = ReceiveMode.ENQ1;
    				}
					break;
				default:
					strData = new String(data, 0, num);					
						if(strData.length()>15)
							setValue(strData.substring(1,13));	
					
					System.out.println(getValue());
					pass.doLoginAdimat(strData);
					break;
				}
			}
			
		} catch (Exception e) { 
			System.err.println("Fehler beim Lesen empfangener Daten. "+e.toString());
		}
	}
	

	
	public void check(String password) {
		
	}
	class Task   extends TimerTask  
	{
	    @Override
		public void run()  {
	    	if (serialPortGeoeffnet == false) {
	    		return;
	    	}
	    	else {
	    		try {
	    			switch(receiveMode) {
	    			case STANDARD:
	    				// Verbindung pruefen. 
	    				// Das Stiftschloss gibt auf ene '0' keine Antwort, aber wenn der 
	    				// USB Stecker ausgezogen worden ist, gibt es eine Exception.
	    				outputStream.write('0'); 
	    				break;
	    			case ENQ1:
	    				outputStream.write('1');
	    				break;
	    			case ENQ9:
	    				outputStream.write('9');
	    				break;
	    			default:
	    				outputStream.write('7');
	    				break;
	    			}
	    			
	    		} catch (Exception e) {
	    			this.cancel(); // Timer-Task beenden.
	    			String errMsg="";
	    			errMsg ="Verbindungsunterbruch.\n";
	    			errMsg+="Bitte folgendermassen vorgehen:\n";
	    			errMsg+=" (1) das Programm beenden,\n"; 
	    			errMsg+=" (2) den USB Stecker zum Stiftschloss neu einstecken,\n";
	    			errMsg+=" (3) warten, bis die LED des Stiftschlosses langsam blinkt oder Dauerlicht anzeigt,\n";
	    			errMsg+=" (4) dann das Programm wieder starten.\n";
	    			JOptionPane.showMessageDialog(null,errMsg, "FEHLER", JOptionPane.OK_OPTION);
	    			System.err.println("Fehler beim Senden. "+e.toString());
	    		}
	    	}
	    }
	}

	/**
	 * Event-Handling:
	 *
	 */
	class serialPortEventListener implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			switch (event.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				serialPortDatenVerfuegbar();
				break;
			case SerialPortEvent.BI:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.FE:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			case SerialPortEvent.PE:
			case SerialPortEvent.RI:
			default:
			}
		}
	}	
}
