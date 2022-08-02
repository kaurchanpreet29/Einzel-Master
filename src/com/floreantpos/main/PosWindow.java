package com.floreantpos.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import org.jdesktop.swingx.plaf.metal.MetalStatusBarUI;

import com.floreantpos.IconFactory;
import com.floreantpos.config.AppConfig;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.User;
import com.floreantpos.swing.GlassPane;

public class PosWindow extends JFrame implements WindowListener {
  private static final String EXTENDEDSTATE = "extendedstate"; //$NON-NLS-1$
  private static final String WLOCY = "wlocy"; //$NON-NLS-1$
  private static final String WLOCX = "wlocx"; //$NON-NLS-1$
  private static final String WHEIGHT = "wheight"; //$NON-NLS-1$
  private static final String WWIDTH = "wwidth"; //$NON-NLS-1$
  private static JLabel statusUser;
  ImageIcon timeLogo = IconFactory.getIcon("uhr.png");
  ImageIcon printerLogo = IconFactory.getIcon("printer.png");
  ImageIcon noPrinterLogo = IconFactory.getIcon("printer_no.png");
  private static JLabel statusDate;
  ImageIcon userLogo = IconFactory.getIcon("user.png");
  private GlassPane glassPane;
  private JXStatusBar statusBar;
  private JLabel statusPrinterError;
  private javax.swing.ImageIcon statusLogo;

  public PosWindow() {
    setIconImage(Application.getApplicationIcon().getImage());

    addWindowListener(this);
    this.setBackground(Color.BLACK);
    glassPane = new GlassPane();
    glassPane.setOpacity(0.6f);
    setGlassPane(glassPane);
    statusBar = new JXStatusBar();

    statusBar.setUI(new MetalStatusBarUI());
    statusBar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);
    
    getContentPane().add(statusBar, BorderLayout.SOUTH);
    getContentPane().setBackground(new Color(5, 29, 53));

    statusUser = new JLabel("");
    statusUser.setForeground(Color.WHITE);
    statusUser.setFont(new Font(null,Font.BOLD, 18));
    statusUser.setBackground(new Color(5, 29, 53));
    statusUser.setIcon(userLogo);
    try {
      statusLogo = IconFactory.getIcon("khana_status_logo.png");
      JLabel lblLogo = new JLabel();
      lblLogo.setIcon(statusLogo);
      statusBar.add(lblLogo);
      statusBar.add(Box.createHorizontalStrut(20));
    } catch (Exception e) {
    }
    
    
    statusBar.add(statusUser,JXStatusBar.Constraint.ResizeBehavior.FILL);
    statusBar.add(Box.createHorizontalStrut(20));
    
    statusPrinterError = new JLabel("");
    statusPrinterError.setIcon(printerLogo);
    printErr = new JLabel("");
    printErr.setIcon(printerLogo);

    statusBar.add(printErr);
    statusBar.add(Box.createHorizontalStrut(20));
    

    statusDate = new JLabel("");
    statusDate.setIcon(timeLogo);
    statusDate.setForeground(Color.WHITE);
    statusDate.setFont(new Font(null, Font.BOLD, 19));
    statusBar.add(statusDate);
  }

  public void setStatus(String status, Color color) {
    statusPrinterError.setFont(new Font("Times New Roman", Font.BOLD, 16));
    statusPrinterError.setForeground(Color.BLACK);
   
//    final PrintService[] services = PrintServiceLookup.lookupPrintServices(
//        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
//    PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//    for (final PrintService service : services) {
//      if (service.getName().equals(
//          Application.getPrinters().getReceiptPrinter())) {
//        printService = service;
//        break;
//      }
//    }
//    if ((printService == null) && (services.length > 0)) {
//      printService = services[0];
//    }

    int count = 0;
    count = getExistQueuePrinter();
//    if (printService != null) {
//      AttributeSet att = printService.getAttributes();
//
//      for (Attribute a : att.toArray()) {
//        String attributeName;
//        String attributeValue;
//        attributeName = a.getName();
//        attributeValue = att.get(a.getClass()).toString();
//        if (attributeName.compareTo("queued-job-count") == 0) {
//          count = Integer.parseInt(attributeValue);
//          break;
//        }
//      }
//    }
    statusPrinterError.setVisible(true);
    
    User currentUser = Application.getCurrentUser();
    if(currentUser != null) {
      statusUser.setText(currentUser.getFirstName());
    } else {
      statusUser.setText("");  
    }
  
    if(count > 0) {
      statusPrinterError.setFont(new Font(null, Font.BOLD, 24));
      statusPrinterError.setIcon(noPrinterLogo);      
    } else {
      statusPrinterError.setFont(new Font(null, Font.BOLD, 16));
      statusPrinterError.setIcon(printerLogo);
    }
    statusPrinterError.setForeground(Color.WHITE);

    statusPrinterError.setText(count+"");
    
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.YY HH:mm:ss");
    statusDate.setText(df.format(new Date()));
    repaint();
    System.out.println("ll");
  }
  private static JLabel printErr;
  static ImageIcon printerLogo1 = IconFactory.getIcon("printer.png");

  
  public Integer getExistQueuePrinter() {
	    int queue = 0;
	    PrintService myService = null;
	    PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
	    if (printService != null) {
	        myService = printService;
	        AttributeSet attributes = printService.getAttributes();
	        for (Attribute a : attributes.toArray()) {
	            String name = a.getName();
	            String value = attributes.get(a.getClass()).toString();
	            if (name.equals("queued-job-count")) {
	                queue = Integer.parseInt(value);
	            }
	        }

	        Object[] obj = attributes.toArray();

	        return queue;    

	    }
	    return null;
	}
  
  
  public static void setPrintError() {
	  printErr.setFont(new Font("Times New Roman", Font.BOLD, 16));
	  printErr.setForeground(Color.BLACK);
	  int count = Application.getNotPrint();
	    printErr.setVisible(true);
	 
	    if(count > 0) {
	    	printErr.setFont(new Font(null, Font.BOLD, 24));
	    	printErr.setIcon(printerLogo1);
		    printErr.setForeground(Color.RED);

	    } else {
	    	printErr.setFont(new Font(null, Font.BOLD, 16));
	    	printErr.setIcon(printerLogo1);
		    printErr.setForeground(Color.WHITE);
	        }

	    printErr.setText(count+"");
  }
   
  public static int printCount() {
	  final PrintService[] services = PrintServiceLookup.lookupPrintServices(
			        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
			    PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
			    for (final PrintService service : services) {
			      if (service.getName().equals(
			          PosPrinters.load().getReceiptPrinter())) {
			        printService = service;
			        break;
			      }
			    }
			    if ((printService == null) && (services.length > 0)) {
			      printService = services[0];
			    }

			    int count = 1;
			    if (printService != null) {
			      AttributeSet att = printService.getAttributes();

			      for (Attribute a : att.toArray()) {

			        String attributeName;
			        String attributeValue;
			        attributeName = a.getName();
			        attributeValue = att.get(a.getClass()).toString();
			        if (attributeName.compareTo("queued-job-count") == 0) {
			          count = Integer.parseInt(attributeValue);
			          break;
			        }
			      }
			    }
			   Application.setNotPrint(count);
		return count;
		
	}
  
  public static void timeee() {
		try {
	        while (true) {
	        	User currentUser = Application.getCurrentUser();
	            if(currentUser != null) {
	              statusUser.setText(currentUser.getFirstName());
	              printCount();	              
	            } else {
	              statusUser.setText("");  
	            }
	          SimpleDateFormat df = new SimpleDateFormat("dd.MM.YY HH:mm:ss");
	          statusDate.setText(df.format(new Date()));
	          setPrintError();
	          Thread.sleep(1 * 1000);
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
  
  public void setupSizeAndLocation() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setSize(AppConfig.getInt(WWIDTH, (int) screenSize.getWidth()),
        AppConfig.getInt(WHEIGHT, (int) screenSize.getHeight()));

    setLocation(
        AppConfig.getInt(WLOCX, ((screenSize.width - getWidth()) >> 1)),
        AppConfig.getInt(WLOCY, ((screenSize.height - getHeight()) >> 1)));
    setMinimumSize(new Dimension(800, 600));
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    int extendedState = AppConfig.getInt(EXTENDEDSTATE, -1);
    if (extendedState != -1) {
      setExtendedState(extendedState);
    }
  }

  public void enterFullScreenMode() {
    setUndecorated(true);
  }

  public void leaveFullScreenMode() {
    GraphicsDevice window = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices()[0];
    setUndecorated(false);
    window.setFullScreenWindow(null);
  }

  public void saveSizeAndLocation() {
    int width = getWidth();
    int height = getHeight();
    AppConfig.putInt(WWIDTH, width);
    AppConfig.putInt(WHEIGHT, height);

    Point locationOnScreen = getLocationOnScreen();
    AppConfig.putInt(WLOCX, locationOnScreen.x);
    AppConfig.putInt(WLOCY, locationOnScreen.y);

    AppConfig.putInt(EXTENDEDSTATE, getExtendedState());
  }

  public void setGlassPaneVisible(boolean b) {
    glassPane.setVisible(b);
  }

  /*
   * public void setGlassPaneMessage(String message) {
   * glassPane.setMessage(message); }
   */

  public void windowOpened(WindowEvent e) {
  }

  public void windowClosing(WindowEvent e) {
    Application.getInstance().exitPOS(true);
  }

  public void windowClosed(WindowEvent e) {
  }

  public void windowIconified(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }
}
