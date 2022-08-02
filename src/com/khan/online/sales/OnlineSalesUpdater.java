package com.khan.online.sales;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.io.MacInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.ticket.TicketViewerTable;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;

import net.miginfocom.swing.MigLayout;

public class OnlineSalesUpdater {
	static String  newFilePath;
	//static String watchPath = "C:\\tconnect\\TConnect\\temp\\in";

	public static Ticket ticket;	
	public OnlineSalesUpdater updateListener;
	public static NodeList ItemsList;
	public static List<Node> ItemList;
	static JPopupMenu popUp = new JPopupMenu();


	public static String getNewFilePath() {
		return newFilePath;
	}

	public static void setNewFilePath(String newFilePath) {
		OnlineSalesUpdater.newFilePath = newFilePath;
	}

	public OnlineSalesUpdater(String watchPath, Boolean FTP) {
		try{
			File destinationFolder = new File(watchPath);
		if (!destinationFolder.exists())
		{
		    destinationFolder.mkdirs();
		}
		}catch(Exception ex) {
			
		}
		
		if(FTP) {
			run(watchPath);
		}else {
			runEmailListener(watchPath);
		}
	}


	public void run(String watchPath) {
		File dir = new File(watchPath);
		Path path = dir.toPath();

		try {       	
			Boolean isFolder = (Boolean) Files.getAttribute(path,
					"basic:isDirectory", NOFOLLOW_LINKS);
			if (!isFolder) {
				throw new IllegalArgumentException("Path: " + path
						+ " is not a folder");
			}
		} catch (IOException ioe) {
			// Folder does not exists
			ioe.printStackTrace();
		}

		System.out.println("Online Sales Listner started " + path.toString());
	
		FileSystem fs = path.getFileSystem();
		
		try (WatchService service = fs.newWatchService()) {
			path.register(service, ENTRY_CREATE, ENTRY_MODIFY); 

			// Start the infinite polling loop
			WatchKey key = null;
			while (true) {
				key = service.take();

				// Dequeueing events
				Kind<?> kind = null;
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					// Get the type of the event
					kind = watchEvent.kind();
					if (OVERFLOW == kind) {
						continue; // loop
					} else if (ENTRY_CREATE == kind) {
						// A new Path was created
						Path newPath = ((WatchEvent<Path>) watchEvent)
								.context();
						// Output
						System.out.println("New path created: " + newPath);
						setNewFilePath(watchPath+"\\"+newPath.toString());
						doImportAdnMove(watchPath, "C:\\Data\\Processed", "");
						//TicketBuilderFromXml.createLieferTicket(newFilePath, "Lieferando");
					} else if (ENTRY_MODIFY == kind) {
						// modified
						Path newPath = ((WatchEvent<Path>) watchEvent)
								.context();
						// Output
						System.out.println("New path modified: " + newPath);
						setNewFilePath(watchPath+"\\"+newPath.toString());
						doImportAdnMove(watchPath, "C:\\Data\\Processed", "");
						//TicketBuilderFromXml.createLieferTicket(newFilePath);
					}
				}

				if (!key.reset()) {
					break; // loop
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}
	
	public static void runEmailListener(String EmailPath) {
		File dir = new File(EmailPath);
		Path path = dir.toPath();

		try {       	
			Boolean isFolder = (Boolean) Files.getAttribute(path,
					"basic:isDirectory", NOFOLLOW_LINKS);
			if (!isFolder) {
				throw new IllegalArgumentException("Path: " + path
						+ " is not a folder");
			}
		} catch (IOException ioe) {
			// Folder does not exists
			ioe.printStackTrace();
		}
		System.out.println("Email attachement listener started at  " + path.toString());


	
		FileSystem fs = path.getFileSystem();
		
		try (WatchService service = fs.newWatchService()) {
			path.register(service, ENTRY_CREATE, ENTRY_MODIFY); 

			// Start the infinite polling loop
			WatchKey key = null;
			while (true) {
				key = service.take();

				// Dequeueing events
				Kind<?> kind = null;
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					// Get the type of the event
					kind = watchEvent.kind();
					if (OVERFLOW == kind) {
						continue; // loop
					} else if (ENTRY_CREATE == kind) {
						// A new Path was created
						Path newPath = ((WatchEvent<Path>) watchEvent)
								.context();
						// Output
						System.out.println("New path created: " + newPath);
						setNewFilePath(EmailPath+"\\"+newPath.toString());
						doImportAdnMove(EmailPath, "C:\\KhanaLiefer\\Orders", "Email_");
						//TicketBuilderFromXml.createLieferTicket(newFilePath, "Attachements");
					} else if (ENTRY_MODIFY == kind) {
						// modified
						Path newPath = ((WatchEvent<Path>) watchEvent)
								.context();
						// Output
						System.out.println("New path modified: " + newPath);
						setNewFilePath(EmailPath+"\\"+newPath.toString());						
						doImportAdnMove(EmailPath, "C:\\KhanaLiefer\\Orders", "Email_");
						//TicketBuilderFromXml.createLieferTicket(newFilePath, "Attachements");
					}
				}

				if (!key.reset()) {
					break; // loop
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}


	public static void doImportAdnMove(String Source, String Destination, String name) {
		//With Java 7 or newer you can use Files.move(from, to, CopyOption... options).
	
		try{
			File destinationFolder = new File(Destination);
			File sourceFolder = new File(Source);
		if (!destinationFolder.exists())
		{
		    destinationFolder.mkdirs();
		}

		if (sourceFolder.exists() && sourceFolder.isDirectory())
		{
		    File[] listOfFiles = sourceFolder.listFiles();

		    if (listOfFiles != null)
		    {
		        for (File child : listOfFiles )
		        {
					SalesImporter.getImporter().importSalesReport(child);
		            child.renameTo(new File(destinationFolder + "\\"+name + child.getName()));
		            //System.out.println(child.getPath());
		        }
		    }
		}
		else
		{
		    System.out.println(sourceFolder + "  Folder does not exists");
		}
	
	}catch(Exception ex) {
		ex.printStackTrace();
	}
	}
}