package com.khana.schlussel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.floreantpos.config.TerminalConfig;


/** 
 * <br/><b>Hauptprogramm</b>
 * <br/>
 * <br><b>AddimatUSB2Testprogram_JNI_V01</b>
 * <li>Zeigt, wie RXTXComm zur Kommunikation mit einem Addimat USB2 Stiftschloss 
 * eingesetzt werden kann.
 * <li>Zeigt, wie AddimatDevMgr001 eingesetzt werden kann.
 * <li>Zeigt, wie ein ShutdownHook eingesetzt werden kann.
 * <br/>
 * <br><b>Konfiguration:</b>
 * <br>Library: RXTXComm.jar     -> C:\Programme\Java\jdk1.6.06\lib
 * <br>DLL's:
 * <li>rxtxSerial.dll 			-> C:\Programme\Java\jre1.6.0_06\bin
 * <li>rxtxParallel.dll 		-> C:\Programme\Java\jre1.6.0_06\bin
 * <li>AddimatDevMgr001.dll 	-> C:\Windows\System32
 * <br>   
 * <br><b>Eclipse-Tool (Plugin):</b> 
 * <li>FatJar zur Erstellung eines ausf�hrbaren jar-Files in dem ein anderes jar-File enthalten ist, hier RXTXComm.jar
 * <br>Quelle: http://fjep.sourceforge.net/
 * <br/>  
 * @author Peter Aeschimann aap2@bfh.ch
 * <br>Letzte Aenderung: 11.07.2008
 * 
 */
public class AddimatListener {

	private static final long serialVersionUID = 1L;

	Container c;
	
	JRadioButton rbEnq1 = new JRadioButton("ENQ1");
	JRadioButton rbEnq9 = new JRadioButton("ENQ9");
	JRadioButton rbEnq1Enq9 = new JRadioButton("ENQ1 + ENQ9");
	ButtonGroup bgEnqMode = new ButtonGroup();
	
	
	JRadioButton rbStandard = new JRadioButton("Standard");
	JRadioButton rbPolling = new JRadioButton("Polling");
	ButtonGroup bgMode = new ButtonGroup();
	
	JComboBox cbComPort = new JComboBox();
	JLabel lbPollingMode = new JLabel(" Polling Mode:");
	
	JTextField tfStandardMode = new JTextField("");
	JTextField tfEnq1Mode = new JTextField("");
	JTextField tfEnq9Mode = new JTextField("");
	
	JLabel lbStandard = new JLabel("Standard");
	JLabel lbEnq1 = new JLabel("ENQ1");
	JLabel lbEnq9 = new JLabel("ENQ9");
	
	final SerialComm vcp;  
	public SerialComm getVcp() {
		return vcp;
	}

	AddimatShutdownHook shutdownHook;
	String shutDownReportFilename = "";

	
	
	public AddimatListener() {
		// Kommunikationsschnittstelle instanzieren
		vcp = new SerialComm(this);
		
		//Shutdown Hook installieren, damit die Kommunikationsschnittstelle
		// bei laufendem Programm und Hinunterfahren des Rechners normal
		// beendet wird.
		// Achtung: Funktioniert nur, wenn das Testprogramm mit jawa
		// anstelle von javaw gestartet wird. Java Bug Report 4486580.
		initShutDownHook();
		
		// GUI Kompmonenten initalisieren
		initComponents();
	}
	
	/**
	 * Sucht zuerst das File "shutdown.log" im aktuellen Working Directory und
	 * wenn es eines gibt, wird dessen Inhalt angezeigt, anschliessend wird es
	 * gel�scht und dann wird ein ShutdownHook zur Runtime zugef�gt, damit
	 * eine offene Kommunikationsschnittstelle bei einem Shutdown und laufenden
	 * Programm ordnungsgem�ss geschlossen wird.
	 */
	void initShutDownHook() {
		shutDownReportFilename = System.getProperty("user.dir");
		shutDownReportFilename+="\\shutdown.log";
//		try {
//			File target = new File(shutDownReportFilename);
//			if (target.exists()) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(target) ) );
//				String report = "Shutdown Report:\n";
//				report += in.readLine();
//				in.close();
//				JOptionPane.showMessageDialog(null,report, "Shutdown Report", JOptionPane.OK_OPTION);
//				target.delete();
//			}
//	    } catch (Exception e) {
//		    System.err.println("Unable to handle " + shutDownReportFilename + "("
//		          + e.getMessage() + ")");
//		} 
		
		// Shutdown Hook zur JVM zufuegen, damit die Kommunikationsschnittstelle
		// bei einem Shutdown ordnunngsgem�ss geschlossen werden kann.
		shutdownHook = new AddimatShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}
	
	protected void finalize() {
	}


	void initComponents() {
		
		JFrame frame = new JFrame();
		
		frame.setTitle("Addimat USB2 Testprogram JNI V01");
		frame.setResizable(false);
		frame.addWindowListener(new WindowListener());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = frame.getContentPane();
		c.setLayout(null);
		
		JLabel lbComPort = new JLabel( "COM Port:");
		lbComPort.setBounds(9,5,70,20);
		c.add(lbComPort);
		cbComPort.setBounds(9,35,70,20);
		c.add(cbComPort);
		
		JLabel lbMode = new JLabel(" Mode:");
		lbMode.setBounds(130,5,70,20);
		c.add(lbMode);
		rbStandard.setBounds(130,35,100,20);
		rbStandard.setText("Standard");
		c.add(rbStandard);
		rbPolling.setBounds(130,60,100,20);
		rbPolling.setText("Polling");
		c.add(rbPolling);
		
		
		bgEnqMode.add(rbEnq1);
		bgEnqMode.add(rbEnq9);
		bgEnqMode.add(rbEnq1Enq9);
		
		
		lbPollingMode.setBounds(255,5,100,20);
		c.add(lbPollingMode);
		rbEnq1.setBounds(255,35,100,20);
		rbEnq1.setText("ENQ1");
		c.add(rbEnq1);
		rbEnq9.setBounds(255,60,100,20);
		rbEnq9.setText("ENQ9");
		c.add(rbEnq9);
		rbEnq1Enq9.setBounds(255,85,100,20);
		rbEnq1Enq9.setText("ENQ1 + ENQ9");
		c.add(rbEnq1Enq9);
		
	    tfStandardMode.setBounds(10,150,240,30);
	    
	    lbStandard.setBounds(260,150,70,30);
	    c.add(tfStandardMode);
	    c.add(lbStandard);
	    
	    tfEnq1Mode.setBounds(10,180,240,30);
	    lbEnq1.setBounds(260,180,70,30);
	    c.add(tfEnq1Mode);
	    c.add(lbEnq1);
	    
	    tfEnq9Mode.setBounds(10,210,240,30);
	    lbEnq9.setBounds(260,210,70,30);
	    c.add(tfEnq9Mode);
	    c.add(lbEnq9);
	    
		bgMode.add(rbStandard);
		bgMode.add(rbPolling);
		
		rbStandard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectMode(ReceiveMode.STANDARD);
			}
		});
		
		rbPolling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectMode(ReceiveMode.ENQ1);
			}
		});
		
		rbEnq1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectMode(ReceiveMode.ENQ1);
			}
		});
		
		rbEnq9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectMode(ReceiveMode.ENQ9);
			}
		});
		
		rbEnq1Enq9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectMode(ReceiveMode.ENQ1_ENQ9);
			}
		});
		
		/** getComPorts wird �ber AddimatDevMgr001 aufgerufen und ist eine 
		 * Windows-DLL Funktion, sie gibt alle COM Ports zur�ck, 
		 * an die zur Zeit ein Stiftschloss angeschlossen ist, das noch
		 * nicht ge�ffnet ist.
		 * Format: "COM3;COM7;COM9;"
		 * Wenn bekannt ist, welche COM-Port durch ein Stiftschloss belegt ist, 
		 * braucht es diese Funktion nicht.
		 */
		
		
//		String comPorts = AddimatJDevMgr001.getComPorts("Addimat USB 2 Waiter Lock (serial)");
//		String comPort[] = comPorts.split("\\;");
//		cbComPort.addItem("");
//		for (String port:comPort) {
//			cbComPort.addItem(port);
//		}
//		cbComPort.addItem("COM8");
		
//		cbComPort.addItemListener( new ItemListener() {
//	          public void itemStateChanged( ItemEvent e ) {
//	        	  // itemStateChanged() kommt auch, wenn das Hauptfenster
//	        	  // geschlossen wird.
//	        	  // Daher nur SELECTED Events auswerten.
//	              if (e.getStateChange() == ItemEvent.SELECTED) {
//	            	  JComboBox selectedChoice = (JComboBox)e.getSource();
//		              int result = vcp.openSerialPort((String)selectedChoice.getSelectedItem());
//		              if (result!=0) {
//		            	  String errMsg = vcp.getErrorMessage();
//		            	  JOptionPane.showMessageDialog(null,errMsg, "FEHLER", JOptionPane.OK_OPTION);
//		              }
//	              } 
//	          }
//	        });
		
		
//		cbComPort.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int result = vcp.openSerialPort("COM8");
//		         if (result!=0) {
//		       	  String errMsg = vcp.getErrorMessage();
//		       	  JOptionPane.showMessageDialog(null,errMsg, "FEHLER", JOptionPane.OK_OPTION);
//		         }
//				
//			}
//		});	
		
		int result = vcp.openSerialPort(TerminalConfig.getAdimatCom());
        if (result!=0) {
      	  String errMsg = vcp.getErrorMessage();
      	  JOptionPane.showMessageDialog(null,errMsg, "FEHLER", JOptionPane.OK_OPTION);
        }
	    
		/**
		 * Das Testprogramm wird im Standard-Mode aufgestartet.
		 * Kurzbeschreibung der verschiedenen Modi: siehe ReceiveMode.java.
		 * 
		 */
		
		selectMode(ReceiveMode.STANDARD);		
//		frame.pack();
//		frame.setSize(400,290);
//		frame.setVisible(true);
	}
	
	
	/**
	 * Wenn der Ben�tzer die Betriebsart, den "mode", des Stiftschlosses �ndert,
	 * werden hier die GUI-Elemente aktualisiert.
	 * Zus�tzlich wird die Betriebsart mit vcp.setReceiveMode(ReceiveMode.STANDARD)
	 * aktualisiert.
	 * vcp ist eine Objekt der Klasse SerialComm , welches die Daten�bertragung
	 * zum und vom Stiftschloss verwaltet.
	 */
	void selectMode(ReceiveMode mode) {
		tfStandardMode.setText("");
		tfEnq1Mode.setText("");
		tfEnq9Mode.setText("");
		switch (mode) {
		case STANDARD:
			rbStandard.setSelected(true);
			rbEnq1.setSelected(true);
		    lbPollingMode.setEnabled(false);
		    rbEnq1.setEnabled(false);
		    rbEnq9.setEnabled(false);
		    rbEnq1Enq9.setEnabled(false);
		    lbStandard.setEnabled(true);
		    lbEnq1.setEnabled(false);
		    lbEnq9.setEnabled(false);
		    tfStandardMode.setEnabled(true);
		    tfEnq1Mode.setEnabled(false);
		    tfEnq9Mode.setEnabled(false);
		    vcp.setReceiveMode(ReceiveMode.STANDARD);
			break;
		case ENQ1:
			rbEnq1.setSelected(true);
		    lbPollingMode.setEnabled(true);
		    rbEnq1.setEnabled(true);
		    rbEnq9.setEnabled(true);
		    rbEnq1Enq9.setEnabled(true);
		    lbStandard.setEnabled(false);
		    lbEnq1.setEnabled(true);
		    lbEnq9.setEnabled(false);
		    tfStandardMode.setEnabled(false);
		    tfEnq1Mode.setEnabled(true);
		    tfEnq9Mode.setEnabled(false);
		    vcp.setReceiveMode(ReceiveMode.ENQ1);
			break;
		case ENQ9:
		    lbPollingMode.setEnabled(true);
		    rbEnq1.setEnabled(true);
		    rbEnq9.setEnabled(true);
		    rbEnq1Enq9.setEnabled(true);
		    lbStandard.setEnabled(false);
		    lbEnq1.setEnabled(false);
		    lbEnq9.setEnabled(true);
		    tfStandardMode.setEnabled(false);
		    tfEnq1Mode.setEnabled(false);
		    tfEnq9Mode.setEnabled(true);
		    vcp.setReceiveMode(ReceiveMode.ENQ9);
			break;
		case ENQ1_ENQ9:
		    lbPollingMode.setEnabled(true);
		    rbEnq1.setEnabled(true);
		    rbEnq9.setEnabled(true);
		    rbEnq1Enq9.setEnabled(true);
		    lbStandard.setEnabled(false);
		    lbEnq1.setEnabled(true);
		    lbEnq9.setEnabled(true);
		    tfStandardMode.setEnabled(false);
		    tfEnq1Mode.setEnabled(true);
		    tfEnq9Mode.setEnabled(true);
		    vcp.setReceiveMode(ReceiveMode.ENQ1_ENQ9);
			break;
		}
	}
	
	/**
	 * shutdown() wird durch den Shutdown Hook bei einem Shutdown von Windows
	 * aufgerufen, z.B. durch den Ben�tzer oder auch durch einen Windows Update, 
	 * vorausgesetzt, das Testprogramm wurde mit java gestartet.
	 * <br/>
	 * <br/>shutdown() ruft vcp.closeSerialPort()auf, womit das Polling
	 * gestoppt und die Serielle Schnittstelle geschlossen wird.
	 * <br/>Datum/Zeit des Shutdowns und der Fehlercode beim Schliessen 
	 * werden in einem File "shutdown.log" im Arbeitsverzeichnis abgelegt.
	 * 
	 * <br>Dieses File wird beim n�chsten Shutdown �berschrieben.
	 * <br/>
	 * <br/>Wie im java bug report 
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4486580
	 * beschrieben, funktioniert der ShutdownHook Mechanismus nur, 
	 * wenn das Testprogramm mit java (anstelle von javaw) gestartet 
	 * worden ist. Beim Aufstarten mittels java ist w�hrend dem Betrieb
	 * des Testprogramms eine Kommandokonsole im Hintergrund ge�ffnet,
	 * was ein Sch�nheitsfehler ist.
	 * <br/>
	 * <br/>
	 * <br/>Wenn das Testprogramm mit javaw gestartet wird, dann wird
	 * Windows die JVM gleichzeitig schliessen, wie shutdown() d.h. 
	 * der Code in shutdown() wird w�hrend seiner Ausf�hrung irgendwo
	 * unterbrochen und die Eintr�ge in "shutdown.log" fehlen oder
	 * das ganze File "shutdown.log" fehlt.
	 * <br/>
	 * <br/>Wenn die serielle Schnittstelle vor einem einem Hinunterfahren
	 * des Rechners nicht geschlossen wird, dann kann sie nachher meistens
	 * nicht mehr gestartet werden: Fehlercode: 0x02 at termios.c(860)
	 * <br/>, resp: Meldung: "COM Port wird bereits ben�tzt.".
	 * <br/>In diesem Fall kann ein Reset mittels AddimatJDevMgr001
	 * versucht werden (indem man das Testprogramm beendet, es neu startet, 
	 * und die betroffene COM Port nochmals w�hlt), aber es kann trotzdem
	 * n�tig werden, den Rechner nochmals zu booten.
	 * 
	 */
	public void shutdown() {
	    try {
	    	FileWriter outFile = new FileWriter(shutDownReportFilename);
	    	PrintWriter out = new PrintWriter(outFile);
	    	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	        Date date = new Date();
	        String msg = "Shutdown Hook wurde ausgel�st um: "+dateFormat.format(date);
	    	switch(vcp.closeSerialPort()) {
	    		case 0: 	msg+=". Die COM Port wurde geschlossen.";
	    					break;
	    		case -1:	msg+=". Die COM Port konnte dabei nicht geschlossen werden.";
	    					break;
	    		case -2:	msg+=". Die COM Port war bereits geschlossen.";
	    					break;
	    		default:;
	    	}
	    	out.println(msg);
	    	out.close();
	    	
	    } catch (IOException e){
	    	e.printStackTrace();
	    }
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AddimatListener();
			}
		});
		
	}
	
	class WindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			vcp.closeSerialPort();
		}
	}
	
	class AddimatShutdownHook extends Thread {
		/**
		 * Die run()-Mehode des ShutdownHook wird einerseits beim normalen Schliessen des Windows
		 * aufgerufen und andererseits bei einem Shutdown von Windows, die eigentliche shutdown()
		 * Methode wird hier nur aufgerufen, wenn die COM Port noch offen ist.
		 */
        public void run() {
        	if (vcp.isOpen()) {
        		shutdown();
        	}
        }
    }


}
