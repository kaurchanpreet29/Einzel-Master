package com.floreantpos.services;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.ecs.html.Address;
import org.apache.ecs.storage.Array;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.services.EnumUtil.ExportReportType;
import com.sun.jna.StringArray;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;


/**
 * @author Jyoti Rai
 *
 */

public class EmailASESUtil {

	/**
	 * 
	 */
	// Replace sender@example.com with your "From" address.
	// This address must be verified.
	static final String FROM = "info@khana.de";
	static final String FROMNAME = "Khana";

	// Replace recipient@example.com with a "To" address. If your account 
	// is still in the sandbox, this address must be verified.
	static final String TO = "tse.khanakassensystem.de";

	static final String HEADER = "";

	// Replace smtp_username with your Amazon SES SMTP user name.
	static final String FOOTER = "";

	// Replace smtp_username with your Amazon SES SMTP user name.
	static final String SMTP_USERNAME = "AKIAUP7D7C35HCVV5YBK";

	// Replace smtp_password with your Amazon SES SMTP password.
	static final String SMTP_PASSWORD = "BJ5ymBOTpv3JZweMvOjLfP1Owv1kSY3PACNdFyFbxRZq";

	static final String HOST = "email-smtp.eu-central-1.amazonaws.com";
	static final String SUBJECT = "Foodbee.de Neu Bestellung ";
	static final String REPLY_TO="info@foodbee.de";

	// The port you will connect to on the Amazon SES SMTP endpoint. 
	static final int PORT = 587;

	public EmailASESUtil() {
		// TODO Auto-generated constructor stub
	}
	final String extension = "png";
	final float zoom = 2f;
	String fileName = "order";
	public void sendFoodBeeEmail(JasperPrint jasperPrint, String orderId, String text) {
		try {
			if(!TO.isEmpty()) {
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", PORT); 
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");

				// Create a Session object to represent a mail session with the specified properties. 
				Session session = Session.getDefaultInstance(props);

				// Create a message with the specified information. 
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(FROM,FROMNAME));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
				msg.setSubject(SUBJECT+" ("+orderId+")");
				msg.setReplyTo(InternetAddress.parse(REPLY_TO, false));		        
				MimeBodyPart p1 = new MimeBodyPart();
				p1.setText(HEADER+"\nVorschau");
				// Create second part
				MimeBodyPart p2 = new MimeBodyPart();
				File folder = new File("onlineOrder");
				if(!folder.exists())
					folder.mkdirs();
				if(jasperPrint!=null) {
					String fileName = (jasperPrint.getName()!=null?jasperPrint.getName():"order")+".pdf";
					fileName = "onlineOrder\\"+fileName;        
					boolean succes = writePdf(jasperPrint, fileName);
					if(succes) {
						FileDataSource fds = new FileDataSource(fileName);
						p2.setDataHandler(new DataHandler(fds));
						p2.setFileName(fds.getName());
					}
				}else if(text!=null&&!text.isEmpty()) {
					p2.setContent(text, "text/html; charset=utf-8");
					System.out.println();
				}		       

				MimeBodyPart p3 = new MimeBodyPart();
				p3.setText(FOOTER);

				Multipart mp = new MimeMultipart();
				mp.addBodyPart(p1);
				mp.addBodyPart(p2);		        
				mp.addBodyPart(p3); 
				msg.setContent(mp);
				Transport transport = session.getTransport();

				// Send the message.
				try
				{
					transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);        	
					transport.sendMessage(msg, msg.getAllRecipients());
					File fileToMove = new File(fileName);
					moveFile(fileToMove, folder.getName());
				}
				catch (Exception ex) {
					System.out.println("The email was not sent.");
					System.out.println("Error message: " + ex.getMessage());
					ex.printStackTrace();
				}
				finally
				{            
					transport.close();
				}

			}
		}catch(Exception ex) {

		}
	}



	
	
	public void moveFile(File fileToMove, String source) {
		File destinationFolder = new File(source+"\\Sent");
		if(!destinationFolder.exists())
			destinationFolder.mkdirs();
		fileToMove.renameTo(new File(destinationFolder + "\\" + fileToMove.getName()));		
	}
	public boolean sendEmail(JasperPrint jasperPrint, String subject, String TO, String text) {
		boolean success = false;
		Restaurant rest = Application.getInstance().getRestaurant();
		String from = rest.getEmail()!=null?rest.getEmail():"info@foodbee.de";
		if(from!=null) {
			try {
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", PORT); 
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");

				// Create a Session object to represent a mail session with the specified properties. 
				Session session = Session.getDefaultInstance(props);

				// Create a message with the specified information. 
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(from,rest.getName()));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
				msg.setSubject(subject);        
				msg.setReplyTo(InternetAddress.parse(REPLY_TO, false));	
				MimeBodyPart p1 = new MimeBodyPart();
				p1.setText("Bitte finden Sie Ihre "+subject+"\nVorschau");
				// Create second part
				MimeBodyPart p2 = new MimeBodyPart();
				File folder = new File("EmailFiles");
				if(!folder.exists())
					folder.mkdirs();
				if(jasperPrint!=null) {
					String fileName = (jasperPrint.getName()!=null?jasperPrint.getName():"report")+".pdf";

					fileName = "EmailFiles\\"+fileName;
					boolean succes = saveReport(jasperPrint,ExportReportType.pdf.name(), fileName);       

					if(succes) {
						FileDataSource fds = new FileDataSource(fileName);
						p2.setDataHandler(new DataHandler(fds));
						p2.setFileName(fds.getName());
					}
				}else if(text!=null&&!text.isEmpty()) {
					p2.setContent(text, "text/html; charset=utf-8");
				}else {
					p2.setContent("Vielen Dank!!", "text/html; charset=utf-8");
				}

				Multipart mp = new MimeMultipart();
				mp.addBodyPart(p1);
				mp.addBodyPart(p2);		        
				MimeBodyPart p3 = new MimeBodyPart();    

				p3.setText("MfG\n"+rest.getName());
				mp.addBodyPart(p3); 
				msg.setContent(mp);

				Transport transport = session.getTransport();

				// Send the message.
				try
				{
					transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);        	
					transport.sendMessage(msg, msg.getAllRecipients());

					success = true;
					if(jasperPrint!=null) {
						File fileToMove = new File(fileName);
						moveFile(fileToMove, folder.getName());
					}

				}
				catch (Exception ex) {
					System.out.println("The email was not sent.");
					System.out.println("Error message: " + ex.getMessage());
					ex.printStackTrace();
				}
				finally
				{            
					transport.close();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}

		}
		return success;
	}



	public boolean sendOrderUpdateEmail(String subject, String TO, String text, String fromName) {
		boolean success = false;
		if(text==null||text.isEmpty())
			return success;
		String from = "info@foodbee.de";
		if(from!=null) {
			try {
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", PORT); 
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");

				Session session = Session.getDefaultInstance(props);

				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(from,fromName));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
				msg.setSubject(subject);        
				msg.setReplyTo(InternetAddress.parse(REPLY_TO, false));	

				MimeBodyPart messagePart = new MimeBodyPart();
				messagePart.setContent(text, "text/html; charset=utf-8");

				Multipart mp = new MimeMultipart();
				mp.addBodyPart(messagePart);

				msg.setContent(mp);
				Transport transport = session.getTransport();

				// Send the message.
				try
				{
					transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);        	
					transport.sendMessage(msg, msg.getAllRecipients());		            
					success = true;  

				}
				catch (Exception ex) {
					System.out.println("The email was not sent.");
					System.out.println("Error message: " + ex.getMessage());
					ex.printStackTrace();
				}
				finally
				{            
					transport.close();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}

		}
		return success;
	}
	

	public boolean sendTseEmail(String subject, String TO, String text, String fromName) {
		boolean success = false;
		if(text==null||text.isEmpty())
			return success;
		String from = "info@khana.de";
		if(from!=null) {
			try {
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", PORT); 
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");

				Session session = Session.getDefaultInstance(props);

				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(from,fromName));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
				msg.setSubject(subject);        
				msg.setReplyTo(InternetAddress.parse(from, false));	

				MimeBodyPart messagePart = new MimeBodyPart();
				messagePart.setContent(text, "text/html; charset=utf-8");

				Multipart mp = new MimeMultipart();
				mp.addBodyPart(messagePart);

				msg.setContent(mp);
				Transport transport = session.getTransport();

				// Send the message.
				try
				{
					transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);        	
					transport.sendMessage(msg, msg.getAllRecipients());		            
					success = true;  

				}
				catch (Exception ex) {
					System.out.println("The email was not sent.");
					System.out.println("Error message: " + ex.getMessage());
					ex.printStackTrace();
				}
				finally
				{            
					transport.close();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}

		}
		return success;
	}
	
	
	
	

	public synchronized boolean writePdf(JasperPrint jasperPrint, String fileName) {
		boolean success = false;
		try {
			jasperPrint.setPageHeight(842);
			jasperPrint.setPageWidth(595);

			JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			success = true;
		}catch(Exception ex) {

		}
		return success;
	}	

	private synchronized Boolean saveReport(JasperPrint jasperPrint, String type, String destFileName)
			throws JRException, Exception {
		Boolean reportWrite = false;
		switch (type) {
		case "pdf":
			jasperPrint.setPageHeight(842);
			jasperPrint.setPageWidth(595);
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
			reportWrite = true;
			break;
		case "html":
			JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
			reportWrite = true;
			break;
		case "xml":
			JasperExportManager.exportReportToXmlFile(jasperPrint, destFileName, true);
			reportWrite = true;
			break;
		case "csv":
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFileName);
			exporter.exportReport();
			reportWrite = true;
			break;
		case "jpg":
			OutputStream out = new FileOutputStream(destFileName);
			BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0,2f);   
			ImageIO.write(image, "jpg", out); //write image to file
			reportWrite = true;
			break;
		case "png":
			OutputStream out1 = new FileOutputStream(destFileName);
			BufferedImage image1 = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0,2f);   
			ImageIO.write(image1, "png", out1); //write image to file
			reportWrite = true;
			break;
		default:
			break;
		}

		return reportWrite;
	}


	public synchronized boolean writeImage(JasperPrint jasperPrint) {
		boolean success = false;
		try {
			try(OutputStream out = new FileOutputStream("onlineOrder\\"+fileName + "." + extension)){
				BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0,zoom);   
				ImageIO.write(image, extension, out); //write image to file
				success = true;
			} catch (IOException e) {
				e.printStackTrace();			        
			}

		}catch(Exception ex) {

		}
		return success;
	}
}
