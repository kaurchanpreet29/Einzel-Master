package com.floreantpos.main;


import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;

public class PoleDisplay
{
	public PoleDisplay()
	{
		super();
	}

	void connect ( String portName ) throws Exception
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(),6000);
			Thread.sleep(3000);
			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_ODD);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialWriter(out))).start();
			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}     
	}


	/** */
	public static class SerialWriter implements Runnable 
	{
		OutputStream out;

		public SerialWriter ( OutputStream out )
		{
			this.out = out;
		}
		public void ClearDisplay(){
			try{
				out.write(ESCPOS.SELECT_DISPLAY);
				out.write(ESCPOS.VISOR_CLEAR);
				out.write(ESCPOS.VISOR_HOME);
				out.flush();
			}
			catch(IOException e){

			}
		}
		public void PrintFirstLine(String text){
			try{
				ClearDisplay();
				if(text.length()>20)            //Display can hold only 20 characters per line.Most of displays have 2 lines.
					text=text.substring(0,20);
				out.write(text.getBytes());
				out.flush();
			}
			catch(IOException r){
			}

		}
		public void run ()
		{
			while ( true )
			{
				PrintFirstLine("Welcome to Germany");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
			}            
		}
	}

	public static void main ( String[] args )
	{
		try
		{
			(new PoleDisplay()).connect("COM5");
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}