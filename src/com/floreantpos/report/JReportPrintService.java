package com.floreantpos.report;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.demo.KitchenDisplay;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.services.QrcodeBarcodeService;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.NumberUtil;
import com.khana.tse.fiskaly.FiskalyKeyParameter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack;

public class JReportPrintService {
	private static final String TIP_AMOUNT = "tipAmount";
	private static final String SERVICE_CHARGE = "serviceCharge";
	private static final String TAX_AMOUNT = "taxAmount";
	private static final String TAX_AMOUNT7 = "taxAmount7";
	private static final String TAX_AMOUNT0 = "taxAmount0";
	private static final String DISCOUNT_AMOUNT = "discountAmount";
	private static final String HEADER_LINE5 = "headerLine5";
	private static final String HEADER_LINE6 = "headerLine6";
	private static final String HEADER_LINE7 = "headerLine7";
	private static final String HEADER_LINE4 = "headerLine4";
	private static final String HEADER_LINE3 = "headerLine3";
	private static final String HEADER_LINE2 = "headerLine2";
	private static final String HEADER_LINE1 = "headerLine1";
	private static final String REPORT_DATE = "reportDate";
	private static final String REPORT_TYPE = "reportType";
	private static final String REPORT_TIME = "reportTime";
	private static final String PRICE_TEXT = "itemPriceText";
	private static final String SERVER_NAME = "serverName";
	private static final String GUEST_COUNT = "guestCount";
	private static final String TABLE_NO = "tableNo";
	private static final String CHECK_NO = "checkNo";
	private static final String TERMINAL = "terminal";
	private static final String SHOW_FOOTER = "showFooter";
	private static final String SHOW_HEADER_SEPARATOR = "showHeaderSeparator";
	private static final String SHOW_SUBTOTAL = "showSubtotal";
	private static final String RECEIPT_TYPE = "receiptType";
	private static final String SUB_TOTAL_TEXT = "subTotalText";
	private static final String QUANTITY_TEXT = "quantityText";
	private static final String ITEM_TEXT = "itemText";
	private static final String ITEM_ID_TEXT = "itemIdText";
	private static final String CURRENCY_SYMBOL = "currencySymbol";
	private static final String DISCOUNT_TEXT = "discountText";
	private static Log logger = LogFactory.getLog(JReportPrintService.class);
	public static void printGenericReport(String title, String data) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put("title", title);
		map.put("data", data);
		JasperPrint jasperPrint = createJasperPrint("/com/floreantpos/report/template/GenericReport.jasper", map,
				new JREmptyDataSource());
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		if (Application.getPrinters().getReceiptPrinterEnable())
			printQuitely(jasperPrint);
	}

	public static JasperPrint createKassenbuchPrint(Map<String, String> map) throws Exception {
		String FILE_RECEIPT_REPORT = "";
		FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/KassenBeschribungReport.jasper";
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
	}

	public static JasperPrint createJasperPrint(String reportFile, Map<String, String> properties,
			JRDataSource dataSource) throws Exception {
		InputStream ticketReportStream = null;
		try {
			ticketReportStream = JReportPrintService.class.getResourceAsStream(reportFile);

			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, properties, dataSource);
			return jasperPrint;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {
			IOUtils.closeQuietly(ticketReportStream);
		}
		return null;
	}

	/*public static JasperPrint createPrintZWS(Ticket ticket, Map<String, String> map, PosTransaction transaction,
			boolean isOfficial) throws Exception {
		String FILE_RECEIPT_REPORT = "";
		if (isOfficial)
			FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport1.jasper";
		else {
			if (!TerminalConfig.isPrintLight()) {

				if (ticket.getType() == TicketType.HOME_DELIVERY) {
					System.out.println("first");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/DeliveryTicketReceiptReport.jasper";
				}else if(!TerminalConfig.isDetailedRecieptEnable()&&TerminalConfig.isTseEnable()&&TerminalConfig.isTseTier3()&&ticket.getTseReceiptDataId()==null) {
					System.out.println("second");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportZWS.jasper";
				} else if(TerminalConfig.isDetailedRecieptEnable()) {
					System.out.println("isDetailedRecieptEnable_");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportDetaild.jasper";
				} else if(TerminalConfig.isFooterMsgEnabled()) {
					System.out.println("third_");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport_footerMsg.jasper";
				} else {
					System.out.println("third");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport.jasper";
				}
			} else {
				System.out.println("fourth");
				FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportLight.jasper";
			}
		}

		if(TerminalConfig.isDetailedRecieptEnable()) {
			TicketDataSourceDetailed dataSourceDetailed = new TicketDataSourceDetailed(ticket);		
			return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSourceDetailed));
		}
		
		TicketDataSource dataSource = new TicketDataSource(ticket);		
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}*/
	
	public static JasperPrint createPrint(Ticket ticket, Map<String, String> map, PosTransaction transaction,
			boolean isOfficial) throws Exception {
		String FILE_RECEIPT_REPORT = "";
		if (isOfficial) {
			
			FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport1.jasper";
		}
		else {
			if (!TerminalConfig.isPrintLight()) {
				if (ticket.getType() == TicketType.HOME_DELIVERY) {	
					System.out.println("five");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/DeliveryTicketReceiptReport.jasper";
				} else if(TerminalConfig.isTseEnable()&&TerminalConfig.isTseTier3()&&ticket.getTseReceiptDataId()==null) {	
					System.out.println("six");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportZWS.jasper";
					
				} else if(TerminalConfig.isDetailedRecieptEnable()) {
					System.out.println("isDetailedRecieptEnable");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportDetaild.jasper";
				 
				} else if(TerminalConfig.isFooterMsgEnabled()) {
					System.out.println("seven_");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport_footerMsg.jasper";
				} else {	
					System.out.println("seven");
					FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport.jasper";
				}
			} else {
				System.out.println("eight");
				FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportLight.jasper";
			}
		}

		if(TerminalConfig.isDetailedRecieptEnable()) {
			TicketDataSourceDetailed dataSourceDetailed = new TicketDataSourceDetailed(ticket);		
			return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSourceDetailed));
		}
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static JasperPrint createKitchenMessagePrint(Map<String, String> map) throws Exception {
		String FILE_RECEIPT_REPORT = "";
		FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/KitchenMessage.jasper";

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
	}

	static String tseId ="";
	public static void generateTseData(Ticket ticket, HashMap map) {
		
		if(ticket.getTseReceiptDataId()!=null && ticket.getTseReceiptDataId()!=0) {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			TSEReceiptData data = TSEReceiptDataDAO.getInstance().get(ticket.getTseReceiptDataId());
			 
			// if(dateFormat.format(date)==dateFormat.format(data.getStart())) {
			tseId = data.getSerialNumber();
			map.put("tse_signature", data.getSignature());
			map.put("tse_transactionnr", data.getTransaction()+"");
			
			Calendar calendar = Calendar.getInstance(); 
			
				 //map.put("tse_start", dateFormat.format(data.getStart())+"T"+timeFormat.format(data.getStart()));
			     map.put("tse_start", dateFormat.format(ticket.getCreateDate())+"T"+timeFormat.format(data.getStart()));
				 map.put("tse_finish", dateFormat.format(data.getFinish())+"T"+timeFormat.format(data.getFinish()));
			
			map.put("tse_serialnumber", data.getSerialNumber());			
			map.put("tse_signaturecount", data.getSignatureCount()+"");
			map.put("tse_timeformat", data.getTimeFormat());	

			map.put("tse_algorithm", data.getSignatureAlgorithm());
			map.put("tse_publickey", data.getSignaturePublicKey());
			try {
				map.put("tse_qrcode",  QrcodeBarcodeService.generateQRCodeInputStream(data.getQRCode()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//	}
		}
			else {	
				 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date startDate = new Date();
			map.put("tse_start", dateFormat.format(startDate)+"T"+timeFormat.format(startDate));
			if(StringUtils.isEmpty(tseId))
					tseId = Application.getInstance().getRestaurant().getTseId();
			map.put("tse_id", tseId);
			map.put("cashdrawer", "24-1");
			startDate = new Date();
			map.put("tse_finish", dateFormat.format(startDate)+"T"+timeFormat.format(startDate));
		}
	}

	public static void generateTseDataZWS(Ticket ticket, HashMap map) {
		
		if(ticket.getTseReceiptDataId()!=null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			TSEReceiptData data = TSEReceiptDataDAO.getInstance().get(ticket.getTseReceiptDataId());
		 
			System.out.println("data.getStart(): "+data.getClientID());
			tseId = data.getSerialNumber();
			map.put("tse_signature", data.getSignature());
			map.put("tse_transactionnr", data.getTransaction()+"");
			
			Calendar calendar = Calendar.getInstance(); 
			Date date = new Date();
			 
				 map.put("tse_start", dateFormat.format(date)+"T"+timeFormat.format(calendar.getTime()));
				 map.put("tse_finish", dateFormat.format(date)+"T"+timeFormat.format(calendar.getTime()));
			
			System.out.println("TodayDate: "+dateFormat.format(date));
			    
			map.put("tse_serialnumber", data.getSerialNumber());			
			map.put("tse_signaturecount", data.getSignatureCount()+"");
			map.put("tse_timeformat", data.getTimeFormat());	

			map.put("tse_algorithm", data.getSignatureAlgorithm());
			map.put("tse_publickey", data.getSignaturePublicKey());
			try {
				map.put("tse_qrcode",  QrcodeBarcodeService.generateQRCodeInputStream(data.getQRCode()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static JasperPrint createA4Print(Ticket ticket, Map<String, String> map, PosTransaction transaction)
			throws Exception {
		String FILE_RECEIPT_REPORT = "";

		if(TerminalConfig.isA4WithBackgroundSP())
			FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportA4WithBackground_SP.jasper";
		else if(TerminalConfig.isWholeSale()||TerminalConfig.isA4WithBackground())
			FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportA4WithBackground.jasper";
		else
			FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportA4WithHeaderFooter.jasper";

		//		FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReportA4WithBackground_Special.jasper";

		TicketDataSourceA4 dataSource = new TicketDataSourceA4(ticket);
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static void printTicket(Ticket ticket, boolean copy) {
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		try {

			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;

			TicketPrintProperties printProperties = new TicketPrintProperties("** RECHNUNG **", true, true, true, false);

			printProperties.setPrintCookingInstructions(false);
			HashMap map = null;
			if(copy) {
				map = populateTicketProperties(ticket, printProperties, null, PrintType.REGULAR, true, 0.00);
				map.put("copy", "COPY");
			}else
				map = populateTicketProperties(ticket, printProperties, null, PrintType.REGULAR, true, 0.00);

			JasperPrint jasperPrint = createPrint(ticket, map, null, false);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));


			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printDeletedTicket(Ticket ticket, String reason) {
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		try {

			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;
			TicketPrintProperties printProperties = new TicketPrintProperties("***STORNO: "+ reason+" ***", true, true, true, false);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null, PrintType.REGULAR, true, 0.00);
			map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
			JasperPrint jasperPrint = createPrint(ticket, map, null, false);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));

			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void userForm(User user) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			Code128Bean code128 = new Code128Bean();
			code128.setHeight(15f);
			code128.setModuleWidth(0.3);
			code128.setQuietZone(10);
			code128.doQuietZone(true);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 300, Image.SCALE_DEFAULT, false);
			code128.generateBarcode(canvas, "RES" + user.getFirstName() + "F");
			canvas.finish();

			InputStream in = new ByteArrayInputStream(baos.toByteArray());
			// Image image = ImageIO.read(in);

			HashMap map = new HashMap();
			Restaurant restaurant = RestaurantDAO.getRestaurant();
			map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getAddressLine3());
			map.put(HEADER_LINE5, restaurant.getTelephone());

			map.put("idText", "ID:");
			map.put("id", user.getAutoId() + "");
			map.put("firstNameText", "VorName:");
			map.put("firstName", user.getFirstName());
			map.put("lastNameText", "NachName:");
			map.put("lastName", user.getLastName());
			// map.put("barcode", in);
			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/userForm.jasper";

			JasperPrint jasperPrint = createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
			if (jasperPrint != null)
				jasperPrint.setName("USER_" + user.getFirstName());

			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printOfficialTicket(Ticket ticket) {
		try {
			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true, false);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null, PrintType.REGULAR, true, 0.00);

			JasperPrint jasperPrint = createPrint(ticket, map, null, true);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));

			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printOnlineTicket(Ticket ticket, Customer customer) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;
			TicketPrintProperties printProperties = new TicketPrintProperties("*** Online Bestellen ***", true, true, true,
					false);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateOnlineTicketProperties(ticket, printProperties, null, customer);

			JasperPrint jasperPrint = createPrint(ticket, map, null, false);
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());

			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static HashMap populateTicketPropertiesA4(Ticket ticket, TicketPrintProperties printProperties,
			PosTransaction transaction) {
		Restaurant restaurant = RestaurantDAO.getRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;		

		String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
		if (customerName == null || customerName.compareTo("null") == 0 || customerName.length() <= 0)
			customerName = "";
		String customerPostCode = ticket.getProperty(Ticket.CUSTOMER_POSTCODE);
		if (customerPostCode == null || customerPostCode.compareTo("null") == 0 || customerPostCode.length() <= 0)
			customerPostCode = "";
		String customerCity = ticket.getProperty(Ticket.CUSTOMER_CITYNAME);
		if (customerCity == null || customerCity.compareTo("null") == 0 || customerCity.length() <= 0)
			customerCity = "";
		String customerFirmName = ticket.getProperty(Ticket.CUSTOMER_FIRMNAME);
		if (customerFirmName == null || customerFirmName.compareTo("null") == 0 || customerFirmName.length() <= 0)
			customerFirmName = "";

		String customerDoor = ticket.getProperty(Ticket.CUSTOMER_DOOR);
		if (customerDoor == null || customerDoor.compareTo("null") == 0 || customerDoor.length() <= 0)
			customerDoor = "";
		String customerAddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS);
		if (customerAddress == null || customerAddress.compareTo("null") == 0 || customerAddress.length() <= 0)
			customerAddress = "";
		String customerSalutation = ticket.getProperty(Ticket.CUSTOMER_SALUTATION);
		if (customerSalutation == null || customerSalutation.compareTo("null") == 0 || customerSalutation.length() <= 0)
			customerSalutation = "";
		String CustomerNr = ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);


		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		try {
			if(!TerminalConfig.isWholeSale()) {
				BufferedImage img_pay = ImageIO.read(new File("resources/images/header_logo.png"));
				ByteArrayOutputStream bas_pay = new ByteArrayOutputStream();
				ImageIO.write(img_pay, "png", bas_pay);
				InputStream is_header = new ByteArrayInputStream(bas_pay.toByteArray());
				map.put("headerLogo", is_header);

				img_pay = ImageIO.read(new File("resources/images/footer_logo.png"));
				bas_pay = new ByteArrayOutputStream();
				ImageIO.write(img_pay, "png", bas_pay);
				InputStream is_footer = new ByteArrayInputStream(bas_pay.toByteArray());
				map.put("footerLogo", is_footer);
				map.put("footerLogoEnd", is_footer);
			}

		} catch (Exception e) {
			System.out.println("Unable to load header/footer for A4 Rechnung");
		}

		try {
			generateTseData(ticket, map);
		}catch(Exception ex) {

		}

		try {			
			String greeting = restaurant.getGreetingText();
			map.put("greetingText", greeting);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			// String greeting = getGreetings();
			String footerText = restaurant.getFooterText();
			map.put("footerText", footerText);
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		map.put("itemTaxText", "Steuer");

		if(StringUtils.isEmpty(customerFirmName))
			map.put("Name", customerSalutation+" "+customerName);
		else if(StringUtils.isEmpty(customerName))
			map.put("Name", customerFirmName);
		else
			map.put("Name", customerFirmName + "\n" + customerSalutation+" "+customerName);	
		map.put("Address", customerAddress + " " + customerDoor);

		if(customerPostCode != null&&customerPostCode.contains(".")) {
			customerPostCode = customerPostCode.substring(0, customerPostCode.indexOf("."));
		}
		if (customerPostCode != null && customerCity != null) {
			map.put("Zip", customerPostCode + " " + customerCity);

		}else if(customerPostCode != null) {
			map.put("Zip", customerPostCode);
		}else if(customerCity != null) {
			map.put("Zip", customerCity);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
		String ticketDate;
		if (ticket.getClosingDate() != null)
			ticketDate = dateFormat.format(ticket.getClosingDate());
		else if(ticket.getCreateDate() !=null)
			ticketDate = dateFormat.format(ticket.getCreateDate());
		else
			ticketDate = dateFormat.format(new Date());
		map.put("DateValue", "Rechnugsdatum: ");
		map.put("Date", ticketDate);
		String deleiveryDate;
		if(ticket.getDeliveryDate() !=null)
			deleiveryDate = dateFormat.format(ticket.getDeliveryDate());
		else
			deleiveryDate = ticketDate;

		map.put("DDateText", "Lieferdatum:");
		map.put("deliveryDate", deleiveryDate);
		map.put("kundenNr", CustomerNr);
		map.put("kundenText", "Kunden Nr. ");

		if(ticket.getTicketid()!=null) {
			map.put("ReceiptValue", POSConstants.RECEIPT_NR);
			map.put("ReceiptNr", ticket.getTicketid().toString());
			if(TerminalConfig.isA4WithBackgroundSP()) {
				DateFormat dateFormat1 = new SimpleDateFormat("YYYY");
				map.put("ReceiptNr", dateFormat1.format(ticket.getCreateDate())+"-"+ticket.getTicketid().toString());
			}
		}

		if(transaction!=null) {
			map.put("ReceiptValue", POSConstants.RECEIPT_NR);
			map.put("ReceiptNr", POSConstants.OFFER);
		}

		map.put("steuerText", "Steuer-ID/ USt-ID:");
		if(restaurant.getTicketFooterMessage2()!=null&&!restaurant.getTicketFooterMessage2().isEmpty())
			map.put("steuerNr", restaurant.getTicketFooterMessage2());
		String customernr = ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);
		if(customernr != null&&customerSalutation.compareTo("null") != 0&&customerSalutation.length() > 0){

			map.put("kundenNr", customernr);
		}

		map.put("RestaurantName", restaurant.getName());
		map.put(CURRENCY_SYMBOL, currencySymbol);
		if(customerSalutation.compareTo("An")!=0)
			map.put("Start", "Sehr geehrter " + customerSalutation + " " + customerName+",");
		else
			map.put("Start", "Sehr geehrter "+ customerName+",");

		map.put(ITEM_TEXT, " Produkt");
		map.put(ITEM_ID_TEXT, " Nr.");
		map.put("itemPriceText", " Preis ");
		map.put("itemQtyText", " Menge");
		map.put(QUANTITY_TEXT, "");
		map.put(SUB_TOTAL_TEXT, " Gesamt ");
		String discountName = "";
		List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
		if(couponList!=null) {
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				discountName = discount.getName();
			}
		}

	
		if ((ticket.getOnlinePayment() != null) && (ticket.getOnlinePayment() == true)) {
			map.put("zahlartText", " Zahlungsart: Online");
		} else if ((ticket.getRechnugPayemnt() != null) && (ticket.getRechnugPayemnt() == true)) {
			map.put("zahlartText", " Zahlungsart: Rechnung");
		}else if ((ticket.getCashPayment() != null) && (ticket.getCashPayment() == true)) {
			map.put("zahlartText", " Zahlungsart: Bar");
		} else if (ticket.getSplitPayment() != null&&ticket.getSplitPayment()) {
			map.put("zahlartText", " Zahlungsart: Bar/Karte");
		} else if (ticket.getGutscheinPayment() != null&&ticket.getGutscheinPayment()) {
			map.put("zahlartText", " Zahlungsart:Gutschein");
		}else if ((ticket.getCashPayment() != null) && (ticket.getCashPayment() == false)) {
			map.put("zahlartText", " Zahlungsart: Karte");
		}


		map.put(DISCOUNT_TEXT, discountName + " Rabatt");
		//		if (ticket.getType() == TicketType.DINE_IN) {
		//			map.put("taxTextO", POSConstants.RECEIPT_REPORT_TAX_DINEIN_DLABEL + " " + currencySymbol);
		//			map.put("taxAmountO", "0.00");
		//		} else {
		//			map.put("taxTextO", POSConstants.RECEIPT_REPORT_TAX_HOMED_DLABEL + " " + currencySymbol);
		//		}

		List<TicketItem> ticketList = ticket.getTicketItems();
		for (Iterator<TicketItem> itr = ticketList.iterator(); itr.hasNext();) {
			TicketItem item = itr.next();
			if (item.getItemId() == 999) {
				map.put("deliveryText", "Anfahrt" + " " + currencySymbol);
				map.put("deliveryAmount", NumberUtil.formatNumber(item.getUnitPrice()));
				break;
			}
		}
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));

		/*
		 * if (printProperties.isShowHeader()) { map.put(HEADER_LINE1,
		 * restaurant.getName()); map.put(HEADER_LINE2, restaurant.getAddressLine1());
		 * map.put(HEADER_LINE3, restaurant.getAddressLine2()); map.put(HEADER_LINE4,
		 * restaurant.getAddressLine3()); map.put(HEADER_LINE5,
		 * restaurant.getTelephone()); }
		 */
		Double total = ticket.getTotalAmount();
		//		total = total + ticket.getDiscountAmount();

		if (ticket.getDiscountAmount() > 0.0) {
			map.put(DISCOUNT_AMOUNT, "- "+NumberUtil.formatNumber(ticket.getDiscountAmount()));
			map.put("grandTotal", NumberUtil.formatNumber(totalAmount+ticket.getDiscountAmount())+ " "+currencySymbol);
		}else {
			map.put("grandTotal", NumberUtil.formatNumber(totalAmount)+ " "+currencySymbol);
		}

		if (RestaurantDAO.getRestaurant().isItemPriceIncludesTax()) {
			map.put("netAmount", NumberUtil.formatNumber(total - ticket.getTaxAmount())+ " "+currencySymbol);
		} else {
			map.put("netAmount", NumberUtil.formatNumber(ticket.getSubtotalAmount())+ " "+currencySymbol);
		}
		total = ticket.getTotalAmount() + ticket.getDiscountAmount();

		if (printProperties.isShowFooter()) {
			if (ticket.getDiscountAmount() > 0.0) {
				map.put(DISCOUNT_AMOUNT, "- "+NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			map.put(TAX_AMOUNT,NumberUtil.formatNumber(ticket.getTax19()) + " " + currencySymbol);
			if(TerminalConfig.isWholeSale()||TerminalConfig.isA4WithBackground()) {
				map.put("mst19",  " MwSt. "+NumberUtil.formatNumber(OrderView.taxDineIn)+" %");
				map.put("mst7", " MwSt. "+NumberUtil.formatNumber(OrderView.taxHomeDelivery)+" %");
			}else {
				map.put("mst19",  NumberUtil.formatNumber(OrderView.taxDineIn)+" %");
				map.put("mst7", NumberUtil.formatNumber(OrderView.taxHomeDelivery)+" %");
			}
			//				if (TerminalConfig.isSupermarket()) {
			map.put(TAX_AMOUNT7,  NumberUtil.formatNumber(ticket.getTax7()) + " " + currencySymbol);
			map.put(TAX_AMOUNT0, NumberUtil.formatNumber(ticket.getTax0()) + " " + currencySymbol);
			map.put("mst0", " 0.0 %");        
			//				}


			if (ticket.getServiceCharge() > 0.0) {
				map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
			}

			map.put("totalText", "Gesamt "+POSConstants.TOTAL_AMOUNT +" ");
			if (ticket.getType() == TicketType.DINE_IN) {
				map.put("taxText", POSConstants.RECEIPT_REPORT_TAX_HOMED_DLABEL + "  " + currencySymbol);
			} else {
				map.put("taxText", POSConstants.RECEIPT_REPORT_TAX_DINEIN_DLABEL + "  " + currencySymbol);
			}
			map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL + "  " + currencySymbol);
			map.put("netAmountText", POSConstants.NET_AMOUNT + "  " + currencySymbol);
			map.put("grandSubtotal", NumberUtil.formatNumber(totalAmount)+ " " + currencySymbol);
			/*
			 * map.put("footerMessage", restaurant.getTicketFooterMessage());
			 * map.put("footerMessage1", restaurant.getTicketFooterMessage1());
			 * map.put("footerMessage2", restaurant.getTicketFooterMessage2());
			 */
			map.put("copyType", printProperties.getReceiptCopyType());

		}

		return map;
	}

	public static void printTransactionA4(PosTransaction transaction) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			if (!Application.getPrinters().getA4PrinterEnable())
				return;
			Ticket ticket = transaction.getTicket();
			TicketPrintProperties printProperties = new TicketPrintProperties("", true, true, true, true);

			HashMap map = populateTicketPropertiesA4(ticket, printProperties, transaction);

			JasperPrint jasperPrint = null;
			jasperPrint = createA4Print(ticket, map, transaction);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
			jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
			printQuitelyA4(jasperPrint);
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static JasperPrint createRefundPrint(Ticket ticket, HashMap map) throws Exception {
		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/RefundReceipt.jasper";

		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static JasperPrint createCashDrawerPrint(Map<String, String> map) throws Exception {
		String FILE_RECEIPT_REPORT = "";
		FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/cashdrawerprint.jasper";

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
	}

	public static void printRefundTicket(Ticket ticket, RefundTransaction posTransaction) {
		try {
			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;
			TicketPrintProperties printProperties = new TicketPrintProperties("*** REFUND RECEIPT ***", true, true, true,
					false);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, posTransaction, PrintType.REGULAR, true, 0.00);
			map.put("refundAmountText", "Total Refund");
			map.put("refundAmount", String.valueOf(posTransaction.getAmount()));
			map.put("cashRefundText", "Cash Refund");
			map.put("cashRefund", String.valueOf(posTransaction.getAmount()));

			JasperPrint jasperPrint = createRefundPrint(ticket, map);
			jasperPrint.setName("REFUND_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			if (!Application.getPrinters().getReceiptPrinterEnable())
				return;
			Ticket ticket = transaction.getTicket();

			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true, false);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction, PrintType.REGULAR, true, 0.00);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Customer Copy");
				JasperPrint jasperPrint = createPrint(ticket, map, transaction, false);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");

				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				map.put("copyType", "Merchant Copy");
				jasperPrint = createPrint(ticket, map, transaction, false);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy");

				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			} else {
				JasperPrint jasperPrint = createPrint(ticket, map, transaction, false);
				jasperPrint.setName("Ticket-" + ticket.getId());
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction, boolean printCustomerCopy, boolean OfficialCopy,
			boolean secondary) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			Ticket ticket = transaction.getTicket();
			if ((ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0)) {
				if (!Application.getPrinters().getReceiptPrinterEnable())
					return;
			}
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true, true);

			printProperties.setPrintCookingInstructions(false);

			HashMap map;
			if (secondary == false)
				map = populateTicketProperties(ticket, printProperties, transaction, PrintType.REGULAR, true, 0.00);
			else
				map = populateTicketProperties(ticket, printProperties, transaction, PrintType.REGULAR2, true, 0.00);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Merchant Copy");

				JasperPrint jasperPrint = null;
				if (OfficialCopy) {
					jasperPrint = createPrint(ticket, map, transaction, true);
				} else {
					jasperPrint = createPrint(ticket, map, transaction, false);
				}
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy");

				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				if (printCustomerCopy) {
					map.put("copyType", "Customer Copy");

					jasperPrint = createPrint(ticket, map, transaction, false);
					jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");

					jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
					printQuitely(jasperPrint);
				}
			} else {

				JasperPrint jasperPrint = null;
				if (OfficialCopy) {
					jasperPrint = createPrint(ticket, map, transaction, true);
				} else {
					jasperPrint = createPrint(ticket, map, transaction, false);
				}
				jasperPrint.setName("Ticket-" + ticket.getId());

				if (ticket.getTicketType().compareTo(TicketType.HOME_DELIVERY.name()) == 0) {
					if (TerminalConfig.isKitchenPrint()) {
						jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
					} else {
						PosPrinters printers = Application.getPrinters();
						jasperPrint.setProperty("printerName", printers.getDefaultKitchenPrinter().getDeviceName());
					}

				} else {
					jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());

				}

				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void beginRow(StringBuilder html) {
		html.append("<div>");
	}

	public static void endRow(StringBuilder html) {
		html.append("</div>");
	}

	public static void addColumn(StringBuilder html, String columnText) {
		html.append("<span>" + columnText + "</span>");
	}

	public static HashMap populateTicketPropertiesZWS(Ticket ticket, TicketPrintProperties printProperties,
			PosTransaction transaction, PrintType printType, boolean logo, double balance) throws IOException {
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		if(TerminalConfig.isRechnungNummerPrintEnable()&&ticket.getTicketid()!=null) {
			int rechNummer = ticket.getTicketid();
			map.put("rechNummer", rechNummer);
			map.put("receiptName", POSConstants.RECEIPT_NR+"  ");
		}

		//if(TerminalConfig.isTseEnable()&&TerminalConfig.isDupTseEnable()&& ticket.getTseReceiptDataId()!=0)	
		if(TerminalConfig.isTseEnable()&&TerminalConfig.isTseTier3()&& ticket.getTseReceiptDataId()!=0)		
				generateTseDataZWS(ticket, map);
			
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(ITEM_ID_TEXT, POSConstants.RECEIPT_REPORT_ITEM_ID_LABEL);
		// map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
		map.put(QUANTITY_TEXT, "");
		map.put(SUB_TOTAL_TEXT, "EUR");
		//		map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put(SHOW_HEADER_SEPARATOR, Boolean.FALSE);
		map.put(SHOW_FOOTER, Boolean.valueOf(printProperties.isShowFooter()));

		if(balance > 0.00&&ticket.getCashPayment()!=null&&ticket.getCashPayment()) {
			map.put("returnMoney", "Geg. BAR: "+NumberUtil.formatNumber(ticket.getPaidAmount())+ " " + currencySymbol+" | Rückgeld BAR: " + NumberUtil.formatNumber(balance) + " " + currencySymbol);
		}
		map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());

		map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		Date date = ticket.getClosingDate()!=null ? ticket.getClosingDate():ticket.getCreateDate();
		
		if (TerminalConfig.isDateOnly()) {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY");			
			String timeStamp = dateFormat.format(date);
			map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL);
		} else {
			map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + Application.formatDate(date) + " Uhr");
		}

		
		StringBuilder ticketHeaderBuilder = buildTicketHeader(ticket, printProperties);


		map.put("ticketHeader", ticketHeaderBuilder.toString());
		if(TerminalConfig.isQRcode())
			map.put("code", LogoCache.getQrCodeBarcode());

		if (printProperties.isShowHeader() && !TerminalConfig.isLogoEnabled()) {
			if (printType == PrintType.REGULAR2)
				map.put(HEADER_LINE1, restaurant.getSecondaryName());
			else
				map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getZipCode() + " " + restaurant.getAddressLine3());

			if (printType == PrintType.REGULAR2 && restaurant.getSecondaryTelephone() != null
					&& restaurant.getSecondaryTelephone().length() > 0)
				map.put(HEADER_LINE5, "Tel.:" + restaurant.getSecondaryTelephone());
			else if (restaurant.getTelephone() != null && restaurant.getTelephone().length() > 0)
				map.put(HEADER_LINE5, "Tel.:" + restaurant.getTelephone());

			if (restaurant.getFax() != null && restaurant.getFax().length() > 0)
				map.put(HEADER_LINE6, "Fax.:" + restaurant.getFax());

			if (restaurant.getTicketFooterMessage2().length() > 0)
				map.put(HEADER_LINE7, "Steuer-Nr.:" + restaurant.getTicketFooterMessage2());
		} else if (printProperties.isShowHeader() && TerminalConfig.isLogoEnabled() && (logo == true)) {
			if (printType == PrintType.REGULAR2)
				map.put("logo", LogoCache.getLogoHeader2());
			else if (printType == PrintType.REGULAR3)
				map.put("logo", LogoCache.getLogoHeader3());
			else if (printType == PrintType.REGULAR4)
				map.put("logo", LogoCache.getLogoHeader4());
			else
				map.put("logo", LogoCache.getLogoHeader1());

			if (printType == PrintType.REGULAR2)
				map.put("footer", LogoCache.getLogoFooter2());
			else if (printType == PrintType.REGULAR3)
				map.put("footer", LogoCache.getLogoFooter3());
			else if (printType == PrintType.REGULAR4)
				map.put("footer", LogoCache.getLogoFooter4());
			else
				map.put("footer", LogoCache.getLogoFooter1());
		}

		Double total = ticket.getTotalAmount();

		if (ticket.getDiscountAmount() > 0.0) {
			map.put(DISCOUNT_AMOUNT, " (-) "+NumberUtil.formatNumber(ticket.getDiscountAmount())+" " +currencySymbol);
		}
		System.out.println("ticket.getTax19(): " + ticket.getTax19() );
		map.put(TAX_AMOUNT,NumberUtil.formatNumber(ticket.getTax19()));
		map.put("mst19",  OrderView.taxDineIn+" %");
		if (TerminalConfig.isSupermarket()) {
			map.put(TAX_AMOUNT7,  NumberUtil.formatNumber(ticket.getTax7()));
			map.put("mst7", OrderView.taxHomeDelivery+" %");
			map.put(TAX_AMOUNT0, NumberUtil.formatNumber(ticket.getTax0()));
			map.put("mst0", " 0.0 %");        
		}
		map.put("netAmount", NumberUtil.formatNumber(total - ticket.getTaxAmount()));
		if(ticket.getGutscheinPayment()!=null && ticket.getGutscheinPayment()==true) {
			total = ticket.getTotalAmount();
		} else {
		    total = ticket.getTotalAmount() + ticket.getDiscountAmount();
		}
		if (ticket.getServiceCharge() > 0.0) {
			map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge())+ " " + currencySymbol);
		}

		if (ticket.getGratuity() != null) {
			tipAmount = ticket.getGratuity().getAmount();
			map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount)+ " " + currencySymbol);
		}

		map.put("totalText", POSConstants.TOTAL_AMOUNT );
		String discountName = "";
		List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
		if (couponList != null) {
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				discountName = discount.getName();					
			}
			if(discountName.length()>6)
				discountName = discountName.substring(0,5)+".";
			map.put("discountText", discountName + " Rabatt ");
		}

		if(restaurant.isItemPriceIncludesTax())
			map.put("grandTotal", NumberUtil.formatNumber(total)+" " +currencySymbol);
		else
			map.put("grandTotal", NumberUtil.formatNumber(total-ticket.getTaxAmount())+" " +currencySymbol);
		map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL);
		map.put("tipsText", POSConstants.RECEIPT_REPORT_TIPS_LABEL);
		map.put("netAmountText", POSConstants.NET_AMOUNT);
		map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL);
		if (ticket.getPfand() != null && ticket.getPfand() > 0.00) {
			map.put("pfand1", TerminalConfig.getPfand1() + " " + currencySymbol);
			map.put("pfandAmount1", " (-) "+NumberUtil.formatNumber(ticket.getPfand())+" " +currencySymbol);
		}

		if (ticket.getPfand2() != null && ticket.getPfand2() > 0.00) {
			map.put("pfand2", TerminalConfig.getPfand2() );
			map.put("pfandAmount2", " (-) "+NumberUtil.formatNumber(ticket.getPfand2())+ " " + currencySymbol);
		}

		if (ticket.getPfand3() != null && ticket.getPfand3() > 0.00) {
			map.put("pfand3", TerminalConfig.getPfand3() );
			map.put("pfandAmount3", " (-) "+NumberUtil.formatNumber(ticket.getPfand3())+ " " + currencySymbol);
		}

		map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getTotalAmountWithoutPfand())+ " " + currencySymbol);
		map.put("footerMessage", restaurant.getTicketFooterMessage());
		map.put("footerMessage1", restaurant.getTicketFooterMessage1());
		if(TerminalConfig.isFooterMsgEnabled()) {
	        map.put("footerMessage2", restaurant.getFooterText());	
		 }
		map.put("copyType", printProperties.getReceiptCopyType());
		if (transaction != null) {
			double changedAmount = transaction.getTenderAmount() - transaction.getAmount();
			if (changedAmount < 0) {
				changedAmount = 0;
			}
			map.put("changedAmount", NumberUtil.formatNumber(changedAmount)+ " " + currencySymbol);
		}

		try {

			if(TerminalConfig.isUpdatedDesign()) {
				if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true) {
					map.put("payLogo", LogoCache.getOnlineDinein());
				}  else if (ticket.getRechnugPayemnt() != null && ticket.getRechnugPayemnt() == true) {
					map.put("payLogo", LogoCache.getTransferDinein());
				}  else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true) {
					map.put("payLogo", LogoCache.getCashDinein());
				}  else if (ticket.getSplitPayment() != null && ticket.getSplitPayment()) {
					map.put("payLogo", LogoCache.getBothDinein());
				} else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true) {					 
					map.put("payLogo", LogoCache.getGutschein());
				} else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false) {
					map.put("payLogo", LogoCache.getCardDinein());				
				} else {
					map.put("payLogo", LogoCache.getCashDinein());
				}
			}
		} catch (Exception e) { 	}
		String first = "Folgende Angaben werden ausschliesslich " + "vom Gast handschriftl. eingetragen";
		map.put("GuestAdditionalInfo1", first);
		String second = "Angaben zum Nachweis von" + " Betriebsaufwendungen nach      " + "(Par. 4 Abs. 5 Ziffer 2 EstG";
		map.put("GuestAdditionalInfo2", second);
		String third = "Bewirtete Personen";
		map.put("GuestAdditionalInfo3", third);
		String fourth = "Anlass der Bewirtung";
		map.put("GuestAdditionalInfo4", fourth);
		String fifth = "Höhe der Aufwendung";
		map.put("GuestAdditionalInfo5", fifth);
		String sixth = "(Bewirtung in Geschäfte)";
		map.put("GuestAdditionalInfo6", sixth);
		String seventh = "(in anderen Fällen)";
		map.put("GuestAdditionalInfo7", seventh);
		String eigth = "(Ort)                (Datum)";
		map.put("GuestAdditionalInfo8", eigth);
		String nine = "(Unterschrift des Gastes)";
		map.put("GuestAdditionalInfo9", nine);
		// }

		return map;
	}
	
	public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties,
			PosTransaction transaction, PrintType printType, boolean logo, double balance) throws IOException {
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		if(TerminalConfig.isRechnungNummerPrintEnable()&&ticket.getTicketid()!=null) {
			int rechNummer = ticket.getTicketid();
			map.put("rechNummer", rechNummer);
			map.put("receiptName", POSConstants.RECEIPT_NR+"  ");
		}

		if(TerminalConfig.isTseEnable())				 
				generateTseData(ticket, map);
			
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(ITEM_ID_TEXT, POSConstants.RECEIPT_REPORT_ITEM_ID_LABEL);
		// map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
		map.put(QUANTITY_TEXT, "");
		map.put(SUB_TOTAL_TEXT, "EUR");
		//		map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put(SHOW_HEADER_SEPARATOR, Boolean.FALSE);
		map.put(SHOW_FOOTER, Boolean.valueOf(printProperties.isShowFooter()));

		if(balance > 0.00&&ticket.getCashPayment()!=null&&ticket.getCashPayment()) {
			map.put("returnMoney", "Geg. BAR: "+NumberUtil.formatNumber(ticket.getPaidAmount())+ " " + currencySymbol+" | Rückgeld BAR: " + NumberUtil.formatNumber(balance) + " " + currencySymbol);
		}
		map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());

		map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		Date date = ticket.getClosingDate()!=null ? ticket.getClosingDate():ticket.getCreateDate();
		if (TerminalConfig.isDateOnly()) {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY");			
			String timeStamp = dateFormat.format(date);
			map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + timeStamp);
		} else {
			map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + Application.formatDate(date) + " Uhr");
		}

		StringBuilder ticketHeaderBuilder = buildTicketHeader(ticket, printProperties);

		map.put("ticketHeader", ticketHeaderBuilder.toString());
		if(TerminalConfig.isQRcode())
			map.put("code", LogoCache.getQrCodeBarcode());

		if (printProperties.isShowHeader() && !TerminalConfig.isLogoEnabled()) {
			if (printType == PrintType.REGULAR2)
				map.put(HEADER_LINE1, restaurant.getSecondaryName());
			else
				map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getZipCode() + " " + restaurant.getAddressLine3());

			if (printType == PrintType.REGULAR2 && restaurant.getSecondaryTelephone() != null
					&& restaurant.getSecondaryTelephone().length() > 0)
				map.put(HEADER_LINE5, "Tel.:" + restaurant.getSecondaryTelephone());
			else if (restaurant.getTelephone() != null && restaurant.getTelephone().length() > 0)
				map.put(HEADER_LINE5, "Tel.:" + restaurant.getTelephone());

			if (restaurant.getFax() != null && restaurant.getFax().length() > 0)
				map.put(HEADER_LINE6, "Fax.:" + restaurant.getFax());

			if (restaurant.getTicketFooterMessage2().length() > 0)
				map.put(HEADER_LINE7, "Steuer-Nr.:" + restaurant.getTicketFooterMessage2());
		} else if (printProperties.isShowHeader() && TerminalConfig.isLogoEnabled() && (logo == true)) {
			if (printType == PrintType.REGULAR2)
				map.put("logo", LogoCache.getLogoHeader2());
			else if (printType == PrintType.REGULAR3)
				map.put("logo", LogoCache.getLogoHeader3());
			else if (printType == PrintType.REGULAR4)
				map.put("logo", LogoCache.getLogoHeader4());
			else
				map.put("logo", LogoCache.getLogoHeader1());

			if (printType == PrintType.REGULAR2)
				map.put("footer", LogoCache.getLogoFooter2());
			else if (printType == PrintType.REGULAR3)
				map.put("footer", LogoCache.getLogoFooter3());
			else if (printType == PrintType.REGULAR4)
				map.put("footer", LogoCache.getLogoFooter4());
			else
				map.put("footer", LogoCache.getLogoFooter1());
		}

		Double total = ticket.getTotalAmount();
		if(ticket.getGutscheinPayment()!=null && ticket.getGutscheinPayment()==true) {
			total = ticket.getTotalAmount();
			 
		} else {
		    total = ticket.getTotalAmount() + ticket.getDiscountAmount();
		}
 
		if (ticket.getDiscountAmount() > 0.0) {
			map.put(DISCOUNT_AMOUNT, " (-) "+NumberUtil.formatNumber(ticket.getDiscountAmount())+" " +currencySymbol);
		}
		
		map.put(TAX_AMOUNT,NumberUtil.formatNumber(ticket.getTax19()));
		map.put("mst19",  OrderView.taxDineIn+" %");
		if (TerminalConfig.isSupermarket()) {
			map.put(TAX_AMOUNT7,  NumberUtil.formatNumber(ticket.getTax7()));
			map.put("mst7", OrderView.taxHomeDelivery+" %");
			map.put(TAX_AMOUNT0, NumberUtil.formatNumber(ticket.getTax0()));
			map.put("mst0", " 0.0 %");        
		}
		map.put("netAmount", NumberUtil.formatNumber(total - ticket.getTaxAmount()));
		//total = ticket.getTotalAmount() + ticket.getDiscountAmount();

		if (ticket.getServiceCharge() > 0.0) {
			map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge())+ " " + currencySymbol);
		}

		 if(ticket.getTipAmount()!=null&&ticket.getTipAmount()>0.00) {
				map.put("tipAmount", NumberUtil.formatNumber(ticket.getTipAmount())+" " +currencySymbol);
				map.put("tipsText", "Trinkgeld: ");
				map.put("paidAmount",NumberUtil.formatNumber(ticket.getTotalAmount()+ticket.getTipAmount())+" " +currencySymbol);
			}

		map.put("totalText", POSConstants.TOTAL_AMOUNT );
		String discountName = "";
		List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
		if (couponList != null) {
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				discountName = discount.getName();					
			}
			if(discountName.length()>6)
				discountName = discountName.substring(0,5)+".";
			map.put("discountText", discountName + " Rabatt ");
		}

		if(restaurant.isItemPriceIncludesTax())
			map.put("grandTotal", NumberUtil.formatNumber(total)+" " +currencySymbol);
		else
			map.put("grandTotal", NumberUtil.formatNumber(total-ticket.getTaxAmount())+" " +currencySymbol);
		map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL);
		
		map.put("netAmountText", POSConstants.NET_AMOUNT);
		map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL);
		if (ticket.getPfand() != null && ticket.getPfand() > 0.00) {
			map.put("pfand1", TerminalConfig.getPfand1() + " " + currencySymbol);
			map.put("pfandAmount1", " (-) "+NumberUtil.formatNumber(ticket.getPfand())+" " +currencySymbol);
		}

		if (ticket.getPfand2() != null && ticket.getPfand2() > 0.00) {
			map.put("pfand2", TerminalConfig.getPfand2() );
			map.put("pfandAmount2", " (-) "+NumberUtil.formatNumber(ticket.getPfand2())+ " " + currencySymbol);
		}

		if (ticket.getPfand3() != null && ticket.getPfand3() > 0.00) {
			map.put("pfand3", TerminalConfig.getPfand3() );
			map.put("pfandAmount3", " (-) "+NumberUtil.formatNumber(ticket.getPfand3())+ " " + currencySymbol);
		}

		double gSubTotal=0.00;
		if(ticket.getGutscheinPayment()!=null && ticket.getGutscheinPayment()==true) {
			gSubTotal = ticket.getTotalAmountWithoutPfand()+ ticket.getTipAmount()-ticket.getDiscountAmount();
			
		} else {
			gSubTotal = ticket.getTotalAmountWithoutPfand()+ ticket.getTipAmount();
		}
		
		map.put("grandSubtotal", NumberUtil.formatNumber(gSubTotal) + " " + currencySymbol);
		map.put("footerMessage", restaurant.getTicketFooterMessage());
		map.put("footerMessage1", restaurant.getTicketFooterMessage1());
		 if(TerminalConfig.isFooterMsgEnabled()) {
	        map.put("footerMessage2", restaurant.getFooterText());	
		 }
		map.put("copyType", printProperties.getReceiptCopyType());
		
		if (transaction != null) {
			double changedAmount = transaction.getTenderAmount() - transaction.getAmount();
			if (changedAmount < 0) {
				changedAmount = 0;
			}
			map.put("changedAmount", NumberUtil.formatNumber(changedAmount)+ " " + currencySymbol);
		}

		try {

			if(TerminalConfig.isUpdatedDesign()) {
				if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true) {
					map.put("payLogo", LogoCache.getOnlineDinein());
				}  else if (ticket.getRechnugPayemnt() != null && ticket.getRechnugPayemnt() == true) {
					map.put("payLogo", LogoCache.getTransferDinein());
				}  else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true) {
					map.put("payLogo", LogoCache.getCashDinein());
				}  else if (ticket.getSplitPayment() != null && ticket.getSplitPayment()) {
					map.put("payLogo", LogoCache.getBothDinein());
				} else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true) {
					System.out.println("gutschein true");
					map.put("payLogo", LogoCache.getGutschein());
				} else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false) {
					map.put("payLogo", LogoCache.getCardDinein());				
				} else {
					map.put("payLogo", LogoCache.getCashDinein());
				}

			}


			//					if (ticket.getCashPayment() != null && ticket.getCashPayment() == true && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {// over
			//						map.put("payLogo", LogoCache.getCashDeliveryDrink());
			//					}else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getCashPickupDrink());
			//					}
			//
			//					// Bar ohne Getraenke
			//					else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getCashNoDrinkDelivery());
			//					} else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getCashNoDrinkPickup());
			//					}
			//
			//					// Online Payments - Getraenke
			//					else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getOnlineDeliveryDrink());
			//					} else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getOnlinePickupDrink());
			//					}
			//
			//					// Online Payments - Ohne Getraenke
			//					else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getOnlineNoDrinkDelivery());
			//					} else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getOnlineNoDrinkPickup());
			//					}					
			//
			//					else if (ticket.getSplitPayment() != null && ticket.getSplitPayment()==true
			//							&& ticket.isClosed() && (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getBothDelivery());
			//					} else if (ticket.getSplitPayment() != null && ticket.getSplitPayment()==true
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getBothPickup());
			//					}					
			//
			//					// Karte Payments - Getraenke
			//					else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getCardDeliveryDrink());
			//					} else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false && isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getCardPickupDrink());
			//					}
			//
			//					// Karte Payments - Ohne Getraenke
			//					else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("99") == 0)) {
			//						map.put("payLogo", LogoCache.getCardNoDrinkDelivery());
			//					} else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false && !isDrinkAvailable(ticket)
			//							&& (ticket.getTableNumbers().compareTo("98") == 0)) {
			//						map.put("payLogo", LogoCache.getCardNoDrinkPickup());
			//					}

			//				}
		} catch (Exception e) {



		}
		String first = "Folgende Angaben werden ausschliesslich " + "vom Gast handschriftl. eingetragen";
		map.put("GuestAdditionalInfo1", first);
		String second = "Angaben zum Nachweis von" + " Betriebsaufwendungen nach      " + "(Par. 4 Abs. 5 Ziffer 2 EstG";
		map.put("GuestAdditionalInfo2", second);
		String third = "Bewirtete Personen";
		map.put("GuestAdditionalInfo3", third);
		String fourth = "Anlass der Bewirtung";
		map.put("GuestAdditionalInfo4", fourth);
		String fifth = "Höhe der Aufwendung";
		map.put("GuestAdditionalInfo5", fifth);
		String sixth = "(Bewirtung in Geschäfte)";
		map.put("GuestAdditionalInfo6", sixth);
		String seventh = "(in anderen Fällen)";
		map.put("GuestAdditionalInfo7", seventh);
		String eigth = "(Ort)                (Datum)";
		map.put("GuestAdditionalInfo8", eigth);
		String nine = "(Unterschrift des Gastes)";
		map.put("GuestAdditionalInfo9", nine);
		// }


		return map;
	}

	public static boolean isDrinkAvailable(Ticket ticket) {
		List<TicketItem> itemList = ticket.getTicketItems();
		for (Iterator itr = itemList.iterator(); itr.hasNext();) {
			TicketItem item = (TicketItem) itr.next();
			if (item.isBeverage())
				return true;
		}
		return false;
	}

	public static HashMap populateOnlineTicketProperties(Ticket ticket, TicketPrintProperties printProperties,
			PosTransaction transaction, Customer customer) {
		Restaurant restaurant = RestaurantDAO.getRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(ITEM_ID_TEXT, POSConstants.RECEIPT_REPORT_ITEM_ID_LABEL);
		map.put(QUANTITY_TEXT, "");
		map.put(SUB_TOTAL_TEXT, "EUR");
		map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put(SHOW_HEADER_SEPARATOR, Boolean.FALSE);
		map.put(SHOW_FOOTER, Boolean.valueOf(printProperties.isShowFooter()));

		map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());

		if (!TerminalConfig.isTicketOId()) {
			map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		}
		map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		Date date = ticket.getClosingDate()!=null ? ticket.getClosingDate():ticket.getCreateDate();

		map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + Application.formatDate(date));

		StringBuilder ticketHeaderBuilder = buildOnlineTicketHeader(ticket, printProperties, customer);

		map.put("ticketHeader", ticketHeaderBuilder.toString());

		if (printProperties.isShowHeader()) {
			map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getZipCode() + " " + restaurant.getAddressLine3());
			map.put(HEADER_LINE5, restaurant.getTelephone());
		}
		Double total = ticket.getTotalAmount();

		if (printProperties.isShowFooter()) {

			if (ticket.getDiscountAmount() > 0.0) {
				map.put(DISCOUNT_AMOUNT, "- "+NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			total = total + ticket.getDiscountAmount();
			if (RestaurantDAO.getRestaurant().isItemPriceIncludesTax()) {
				map.put("netAmount", NumberUtil.formatNumber(total - ticket.getTaxAmount()));

			} else {
				map.put("netAmount", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
			}
			total = ticket.getTotalAmount() + ticket.getDiscountAmount();

			if (ticket.getServiceCharge() > 0.0) {
				map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
			}

			map.put("totalText", POSConstants.TOTAL_AMOUNT + currencySymbol);
			String discountName = "";
			List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				discountName = discount.getName();
			}
			map.put("discountText", "- " + discountName + " Rabatt" + currencySymbol);
			map.put("taxText", POSConstants.RECEIPT_REPORT_TAX_HOMED_DLABEL + currencySymbol);
			map.put("taxText7", POSConstants.RECEIPT_REPORT_TAX_DINEIN_DLABEL + currencySymbol);

			map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL + " " + currencySymbol);
			map.put("tipsText", POSConstants.RECEIPT_REPORT_TIPS_LABEL + currencySymbol);
			map.put("netAmountText", POSConstants.NET_AMOUNT + currencySymbol);
			map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL + currencySymbol);

			map.put("grandSubtotal", NumberUtil.formatNumber(totalAmount));
			map.put("footerMessage", restaurant.getTicketFooterMessage());
			map.put("footerMessage1", restaurant.getTicketFooterMessage1());
			map.put("footerMessage2", restaurant.getTicketFooterMessage2());
			map.put("copyType", printProperties.getReceiptCopyType());
		}

		return map;
	}

	private static StringBuilder buildTicketHeader(Ticket ticket, TicketPrintProperties printProperties) {
		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");
		DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date date = ticket.getClosingDate()!=null?ticket.getClosingDate():ticket.getCreateDate();

		String timeStamp = dateFormat1.format(date) + " Uhr";

		if (ticket.getId() != null && !TerminalConfig.isTicketOId()) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_NR + ticket.getId());
			endRow(ticketHeaderBuilder);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY");		
		String timeStamp1 = dateFormat.format(date);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		if (TerminalConfig.isDateOnly()) {			
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.DATE + ": " + timeStamp1);
			endRow(ticketHeaderBuilder);
		} else {
			Calendar calendar = Calendar.getInstance(); 
			 
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.DATE + ": " + dateFormat.format(date)+" " + timeFormat.format(calendar.getTime()));
			endRow(ticketHeaderBuilder);
		}
		
		
		/*  beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, "");
        endRow(ticketHeaderBuilder);*/
		String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
		String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);
		String customerPhone2 = ticket.getProperty(Ticket.CUSTOMER_PHONE2);
		String customerLandMark = ticket.getProperty(Ticket.CUSTOMER_LANDMARK);
		String customerBellName = ticket.getProperty(Ticket.CUSTOMER_BELLNAME);
		String customerPostCode = ticket.getProperty(Ticket.CUSTOMER_POSTCODE);
		String customerCity = ticket.getProperty(Ticket.CUSTOMER_CITYNAME);
		String customerFirmName = ticket.getProperty(Ticket.CUSTOMER_FIRMNAME);
		String customerId = ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);
		String customerAddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS);
		String customerDoor = ticket.getProperty(Ticket.CUSTOMER_DOOR);
		Date deliveryDate = ticket.getDeliveryDate();
		// customer info section
		if (ticket.getType() != TicketType.DINE_IN) {

			int hour = deliveryDate.getHours();
			int min = deliveryDate.getMinutes();
			String minutes = min + "";
			if (min < 10)
				minutes = "0" + minutes;
			String deliveryTime = hour + ":" + minutes + " Uhr";


			if (StringUtils.isNotEmpty(customerName)) {

				if (ticket.getTableNumbers().compareTo("98") != 0) {
					//					if (!TerminalConfig.isLogoEnabled()) {
					//						beginRow(ticketHeaderBuilder);
					//						addColumn(ticketHeaderBuilder, "*******LIEFERUNG*******");
					//						endRow(ticketHeaderBuilder);
					//					}
					if (StringUtils.isNotEmpty(customerId)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "Kunden-Nr: " + customerId);
						endRow(ticketHeaderBuilder);
					}
					if (StringUtils.isNotEmpty(customerFirmName)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, customerFirmName);
						endRow(ticketHeaderBuilder);
					}
					if (StringUtils.isNotEmpty(customerName)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, customerName);
						endRow(ticketHeaderBuilder);
					}

					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, ticket.getDeliveryAddress());
					endRow(ticketHeaderBuilder);

					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerPostCode + " " + customerCity);
					endRow(ticketHeaderBuilder);
					if (StringUtils.isNotEmpty(customerLandMark)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, POSConstants.LAND_MARK + ": " + customerLandMark);
						endRow(ticketHeaderBuilder);
					}

					if (StringUtils.isNotEmpty(customerPhone) && (StringUtils.isNotEmpty(customerPhone2))) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "TEL: " + customerPhone + "/" + customerPhone2);
						endRow(ticketHeaderBuilder);
					} else if (StringUtils.isNotEmpty(customerPhone) && (StringUtils.isEmpty(customerPhone2))) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "TEL: " + customerPhone);
						endRow(ticketHeaderBuilder);
					} else if (StringUtils.isEmpty(customerPhone) && (StringUtils.isNotEmpty(customerPhone2))) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "TEL: " + customerPhone2);
						endRow(ticketHeaderBuilder);
					}
					if (StringUtils.isNotEmpty(customerBellName)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, POSConstants.BELL_NAME + ": " + customerBellName);
						endRow(ticketHeaderBuilder);
					}
					if (TerminalConfig.isDeliveryTime()) {
						if (StringUtils.isNotEmpty(deliveryTime)) {
							beginRow(ticketHeaderBuilder);
							addColumn(ticketHeaderBuilder, "Liefer Zeit: " + deliveryTime);
							endRow(ticketHeaderBuilder);
						}
					}

				} else if (ticket.getTableNumbers().compareTo("98") == 0) {
					//					if (!TerminalConfig.isLogoEnabled()) {
					//						beginRow(ticketHeaderBuilder);
					//						addColumn(ticketHeaderBuilder, "*******ABHOLUNG*******");
					//						endRow(ticketHeaderBuilder);
					//					}
					if (StringUtils.isNotEmpty(customerId)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "Kunden-Nr: " + customerId);
						endRow(ticketHeaderBuilder);
					}
					if (StringUtils.isNotEmpty(customerFirmName)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, customerFirmName);
						endRow(ticketHeaderBuilder);
					}
					if (StringUtils.isNotEmpty(customerName)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, customerName);
						endRow(ticketHeaderBuilder);
					}

					if (StringUtils.isNotEmpty(customerPhone)) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, "TEL: " + customerPhone);
						endRow(ticketHeaderBuilder);
					}
					if (TerminalConfig.isDeliveryTime()) {
						if (StringUtils.isNotEmpty(deliveryTime)) {
							beginRow(ticketHeaderBuilder);
							addColumn(ticketHeaderBuilder, "Liefer Zeit: " + deliveryTime);
							endRow(ticketHeaderBuilder);
						}
					}
				}
			} else if (ticket.getTableNumbers().compareTo("99") == 0) {
				//				if (!TerminalConfig.isLogoEnabled()) {
				//					beginRow(ticketHeaderBuilder);
				//					addColumn(ticketHeaderBuilder, "*******LIEFERUNG*******");
				//					endRow(ticketHeaderBuilder);
				//				}
			} else if (ticket.getTableNumbers().compareTo("98") == 0) {
				//				if (!TerminalConfig.isLogoEnabled()) {
				//					beginRow(ticketHeaderBuilder);
				//					addColumn(ticketHeaderBuilder, "*******ABHOLUNG*******");
				//					endRow(ticketHeaderBuilder);
				//				}
			}

		} else {
			if (StringUtils.isNotEmpty(customerName)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******KUNDEN DATEN*******");
				endRow(ticketHeaderBuilder);
				if (StringUtils.isNotEmpty(customerId)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Kunden-Nr: " + customerId);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerFirmName)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerFirmName);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerName)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerName);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerAddress) && (StringUtils.isNotEmpty(customerDoor))) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerAddress + " " + customerDoor);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerPostCode) && StringUtils.isNotEmpty(customerCity)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerPostCode + " " + customerCity);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerLandMark)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, POSConstants.LAND_MARK + ": " + customerLandMark);
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(customerPhone) && (StringUtils.isNotEmpty(customerPhone2))) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "TEL: " + customerPhone + "/" + customerPhone2);
					endRow(ticketHeaderBuilder);
				} else if (StringUtils.isNotEmpty(customerPhone) && (StringUtils.isEmpty(customerPhone2))) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "TEL: " + customerPhone);
					endRow(ticketHeaderBuilder);
				} else if (StringUtils.isEmpty(customerPhone) && (StringUtils.isNotEmpty(customerPhone2))) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "TEL: " + customerPhone2);
					endRow(ticketHeaderBuilder);
				}
				if (StringUtils.isNotEmpty(customerBellName)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, POSConstants.BELL_NAME + ": " + customerBellName);
					endRow(ticketHeaderBuilder);
				}
			}
		}

		if (!TerminalConfig.isUpdatedDesign()) {
			if ((ticket.getOnlinePayment() != null) && (ticket.getOnlinePayment() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******ONLINE ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if ((ticket.getCashPayment() != null) && (ticket.getCashPayment() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******BAR ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if ((ticket.getRechnugPayemnt() != null) && (ticket.getRechnugPayemnt() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******"+POSConstants.BILL.toUpperCase()+" ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if (ticket.getSplitPayment() != null&&ticket.getSplitPayment()) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "   ****KARTE/BAR ZAHLUNG****");
				endRow(ticketHeaderBuilder);
			} else if (ticket.getGutscheinPayment() != null&&ticket.getGutscheinPayment()) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "   ****GUTSCHEIN ZAHLUNG****");
				endRow(ticketHeaderBuilder);
				
			} else if (ticket.getCardAmount() != null&&ticket.getCardAmount()>0.00) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******KARTEN ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			 
			}else{
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******BAR ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			}
		}
		ticketHeaderBuilder.append("</html>");
		return ticketHeaderBuilder;
	}

	private static StringBuilder buildOnlineTicketHeader(Ticket ticket, TicketPrintProperties printProperties,
			Customer customer) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy, h:m a");

		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");
		DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = new Date();
		String timeStamp = dateFormat1.format(date);

		if (ticket.getId() != null) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_NR + ticket.getId());
			endRow(ticketHeaderBuilder);
		}

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.DATE + ": " + timeStamp);
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "");
		endRow(ticketHeaderBuilder);

		if (StringUtils.isNotEmpty(customer.getAddress())) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, "*******LIEFERUNG*******");
			endRow(ticketHeaderBuilder);

			if (StringUtils.isNotEmpty(customer.getName())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customer.getName());
				endRow(ticketHeaderBuilder);
			}
			if (StringUtils.isNotEmpty(customer.getAddress())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customer.getAddress() + " " + customer.getDoorNo());
				endRow(ticketHeaderBuilder);
			}
			if (StringUtils.isNotEmpty(customer.getBezerk())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customer.getBezerk());
				endRow(ticketHeaderBuilder);
			}
			if (StringUtils.isNotEmpty(customer.getZipCode()) && StringUtils.isNotEmpty(customer.getCity())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customer.getZipCode() + " " + customer.getCity());
				endRow(ticketHeaderBuilder);
			}
			if (StringUtils.isNotEmpty(customer.getTelephoneNo()) && (StringUtils.isNotEmpty(customer.getTelephoneNo2()))) {
				String telephone = customer.getTelephoneNo() + "," + customer.getTelephoneNo2();
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, telephone);
				endRow(ticketHeaderBuilder);
			} else if (StringUtils.isNotEmpty(customer.getTelephoneNo())
					&& (StringUtils.isEmpty(customer.getTelephoneNo2()))) {
				String telephone = customer.getTelephoneNo();
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, telephone);
				endRow(ticketHeaderBuilder);
			} else if (StringUtils.isEmpty(customer.getTelephoneNo())
					&& (StringUtils.isNotEmpty(customer.getTelephoneNo2()))) {
				String telephone = customer.getTelephoneNo2();
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, telephone);
				endRow(ticketHeaderBuilder);
			}

			if (StringUtils.isNotEmpty(customer.getEmail())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customer.getEmail());
				endRow(ticketHeaderBuilder);
			}
			if ((ticket.getOnlinePayment() != null) && (ticket.getOnlinePayment() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******ONLINE ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if ((ticket.getCashPayment() != null) && (ticket.getCashPayment() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******BAR ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if ((ticket.getRechnugPayemnt() != null) && (ticket.getRechnugPayemnt() == true)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******BANK ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			} else if (ticket.getSplitPayment() != null&&ticket.getSplitPayment()) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "   ****KARTE/BAR ZAHLUNG****");
				endRow(ticketHeaderBuilder);
			} else if (ticket.getGutscheinPayment() != null&&ticket.getGutscheinPayment()) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "   ****GUTSCHEIN ZAHLUNG****");
				endRow(ticketHeaderBuilder);
			} else if (ticket.getCardAmount() != null&&ticket.getCardAmount()>0.00) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******KARTEN ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			 
			} else {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*******BAR ZAHLUNG*******");
				endRow(ticketHeaderBuilder);
			}
		}
		ticketHeaderBuilder.append("</html>");
		return ticketHeaderBuilder;
	}

	public static JasperPrint createKitchenPrint(KitchenTicket ticket, TicketType type, boolean kitchenPrint,
			Ticket orgTicket) throws Exception {
		HashMap map = new HashMap();
		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, "*** KUECHE RECHNUNG *** ");
		map.put("cardPayment", true);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);

		Restaurant restaurant = RestaurantDAO.getRestaurant();
		Integer serial_no = 0;

		if (kitchenPrint) {
			if (restaurant.getKitchenSerialNo() == null)
				serial_no = 1;
			else
				serial_no = restaurant.getKitchenSerialNo();

			int newSerial_no = serial_no + 1;
			restaurant.setKitchenSerialNo(newSerial_no);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			map.put("ticketHeader", "KUECHE");
		} else {
			if (restaurant.getBarSerialNo() == null)
				serial_no = 1;
			else
				serial_no = restaurant.getBarSerialNo();

			int newSerial_no = serial_no + 1;
			restaurant.setBarSerialNo(newSerial_no);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			StringBuilder ticketHeaderBuilder = new StringBuilder();
			ticketHeaderBuilder.append("<html>");
			String customerName = orgTicket.getProperty(Ticket.CUSTOMER_NAME);

			String customerPhone = orgTicket.getProperty(Ticket.CUSTOMER_PHONE);
			String customerId = orgTicket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);

			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, "THEKE");
			endRow(ticketHeaderBuilder);

			if (StringUtils.isNotEmpty(customerId)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "Kunden-Nr: " + customerId);
				endRow(ticketHeaderBuilder);
			}

			if (StringUtils.isNotEmpty(customerName)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customerName);
				endRow(ticketHeaderBuilder);
			}

			if (StringUtils.isNotEmpty(customerPhone)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, customerPhone);
				endRow(ticketHeaderBuilder);
			}

			map.put("ticketHeader", ticketHeaderBuilder.toString());
		}

		map.put("serialNo", "Nr#: " + serial_no + "");

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = ticket.getClosingDate()!=null ? ticket.getClosingDate():ticket.getCreateDate();
		String timeStamp = dateFormat.format(date);

		map.put(PRICE_TEXT, POSConstants.PRICE.toString() + "(" + Application.getCurrencySymbol().toString() + ")");

		map.put(REPORT_DATE, Application.formatDate(date) + ", " + timeStamp + " Uhr");

		if (type.toString().compareTo(TicketType.DINE_IN.toString()) == 0)
			map.put(REPORT_TYPE, POSConstants.DINE_IN);
		else {
			if (orgTicket.getTableNumbers().compareTo("98") == 0)
				map.put(REPORT_TYPE, "ABHOLUNG");
			else
				map.put(REPORT_TYPE, POSConstants.HOME_DELIVERY);
		}

		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/KitchenReceipt.jasper";

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));

	}

	public static JasperPrint createDeleteKitchenPrint(KitchenTicket ticket, TicketType type) throws Exception {
		HashMap map = new HashMap();
		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, "*** KUECHE RECHNUNG *** ");
		map.put("cardPayment", true);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		if (type.toString().compareTo(TicketType.DINE_IN.toString()) != 0) {
			map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		} else if (ticket.getTableNumbers() != null) {
			map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		}

		// map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL +
		// ticket.getNumberOfGuests());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = ticket.getClosingDate()!=null ? ticket.getClosingDate():ticket.getCreateDate();

		String timeStamp = dateFormat.format(date);
		map.put(PRICE_TEXT, POSConstants.PRICE.toString() + "(" + Application.getCurrencySymbol().toString() + ")");
		map.put(REPORT_DATE, Application.formatDate(date));
		map.put(REPORT_TIME, timeStamp + " Uhr");
		if (type.toString().compareTo(TicketType.DINE_IN.toString()) == 0)
			map.put(REPORT_TYPE, POSConstants.DINE_IN);
		else
			map.put(REPORT_TYPE, POSConstants.HOME_DELIVERY);
		map.put("stornoHeader", "ART_STORNO");
		map.put("ticketHeader", "KUECHE RECHNUNG");

		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/KitchenReceiptDelete.jasper";

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static void printTicketToKitchen(Ticket ticket) {
		// if(TerminalConfig.isTabVersion())return;
		Session session = null;
		Transaction transaction = null;
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		try {
			session = TicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			if (Application.getPrinters().getKitchenPrinterEnable()) {
				if (ticket.getTicketType().compareTo(TicketType.HOME_DELIVERY.name()) == 0) {
					if (!TerminalConfig.isKitchenPrint())
						return;
				}
				List<KitchenTicket> kitchenTickets = KitchenTicket.fromTicket(ticket);

				for (KitchenTicket kitchenTicket : kitchenTickets) {

					if (kitchenTicket.getPrinter() == null)
						System.out.println("Get printer null");
					String deviceName = kitchenTicket.getPrinter().getDeviceName();

					JasperPrint jasperPrint = createKitchenPrint(kitchenTicket, ticket.getType(), true, ticket);
					jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
					jasperPrint.setProperty("printerName", deviceName);
					// JasperViewer.viewReport(jasperPrint, false);

					System.out.println("Printing to kitchen printer: " + deviceName);
					KitchenDisplay.instance.addTicket(kitchenTicket);

					session.saveOrUpdate(kitchenTicket);

					printQuitely(jasperPrint);
				}
			}

			if (Application.getPrinters().getBarPrinterEnable()) {
				List<KitchenTicket> kitchenBarTickets = KitchenTicket.fromBarTicket(ticket);

				for (KitchenTicket kitchenTicket : kitchenBarTickets) {

					String deviceName = kitchenTicket.getBarPrinter();

					JasperPrint jasperPrint = createKitchenPrint(kitchenTicket, ticket.getType(), false, ticket);
					jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
					jasperPrint.setProperty("printerName", deviceName);
					// JasperViewer.viewReport(jasperPrint, false);

					KitchenDisplay.instance.addTicket(kitchenTicket);

					session.saveOrUpdate(kitchenTicket);

					printQuitely(jasperPrint);

					// markItemsAsPrinted(kitchenTicket);
				}
				session.saveOrUpdate(ticket);
				transaction.commit();
			}

		} catch (Exception e) {
			transaction.rollback();
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			session.close();
		}
	}

	/*
	 * public static void printTabTicketToKitchen(Ticket ticket, Session s) { try {
	 * //session = TicketDAO.getInstance().createNewSession(); //transaction =
	 * session.beginTransaction();
	 * 
	 * if(Application.getPrinters().getKitchenPrinterEnable()) {
	 * if(ticket.getTicketType().compareTo(TicketType.HOME_DELIVERY.name()) == 0) {
	 * if(!TerminalConfig.isKitchenPrint()) return; } List<KitchenTicket>
	 * kitchenTickets = KitchenTicket.fromTicket(ticket);
	 * 
	 * for (KitchenTicket kitchenTicket : kitchenTickets) {
	 * 
	 * if(kitchenTicket.getPrinter() == null)
	 * System.out.println("Get printer null");
	 * 
	 * String deviceName = kitchenTicket.getPrinter().getDeviceName();
	 * 
	 * JasperPrint jasperPrint =
	 * createKitchenPrint(kitchenTicket,ticket.getType(),true,ticket);
	 * jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
	 * jasperPrint.setProperty("printerName", deviceName);
	 * System.out.println("Printing to kitchen printer: "+ deviceName);
	 * KitchenDisplay.instance.addTicket(kitchenTicket);
	 * 
	 * s.saveOrUpdate(kitchenTicket);
	 * 
	 * printQuitely(jasperPrint); } }
	 * if(Application.getPrinters().getBarPrinterEnable()) { List<KitchenTicket>
	 * kitchenBarTickets = KitchenTicket.fromBarTicket(ticket);
	 * 
	 * for (KitchenTicket kitchenTicket : kitchenBarTickets) {
	 * 
	 * String deviceName = kitchenTicket.getBarPrinter();
	 * 
	 * JasperPrint jasperPrint =
	 * createKitchenPrint(kitchenTicket,ticket.getType(),false,ticket);
	 * jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
	 * jasperPrint.setProperty("printerName", deviceName);
	 * KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);
	 * 
	 * printQuitely(jasperPrint); } TicketDAO.getInstance().saveOrUpdate(ticket,s);
	 * }
	 * 
	 * } catch (Exception e) {
	 * logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e); } finally {} }
	 */
	public static void printDeletedTicketToKitchen(Ticket ticket) {
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		Session session = null;
		Transaction transaction = null;
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		try {
			session = TicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();
            
			//			if (Application.getPrinters().getKitchenPrinterEnable()) {
			List<KitchenTicket> kitchenTickets = KitchenTicket.deletefromTicket(ticket);

			for (KitchenTicket kitchenTicket : kitchenTickets) {

				if (kitchenTicket.getPrinter() == null)
					System.out.println("Get printer null");

				String deviceName = kitchenTicket.getPrinter().getDeviceName();

				JasperPrint jasperPrint = createDeleteKitchenPrint(kitchenTicket, ticket.getType());

				jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
				jasperPrint.setProperty("printerName", deviceName);

				// JasperViewer.viewReport(jasperPrint, false);

				System.out.println("Printing to kitchen printer: " + deviceName);
				kitchenTicket.setVoided(true);
				kitchenTicket.setClosingDate(new Date());
				//					KitchenDisplay.instance.addTicket(kitchenTicket);
				session.saveOrUpdate(kitchenTicket);

				printQuitely(jasperPrint);

				System.out.println(kitchenTicket.isVoided());
			}
			//			}
			//			if (Application.getPrinters().getBarPrinterEnable()) {
			//				List<KitchenTicket> kitchenBarTickets = KitchenTicket.deletefromBarTicket(ticket);
			//
			//				for (KitchenTicket kitchenTicket : kitchenBarTickets) {
			//
			//					String deviceName = kitchenTicket.getBarPrinter();
			//
			//					JasperPrint jasperPrint = createDeleteKitchenPrint(kitchenTicket, ticket.getType());
			//					jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
			//					jasperPrint.setProperty("printerName", deviceName);
			//					// JasperViewer.viewReport(jasperPrint, false);
			//
			//					KitchenDisplay.instance.addTicket(kitchenTicket);
			//
			//					session.saveOrUpdate(kitchenTicket);
			//
			//					printQuitely(jasperPrint);
			//
			//					// markItemsAsPrinted(kitchenTicket);
			//				}
			//				session.saveOrUpdate(ticket);
			//				transaction.commit();
			//			}

		} catch (Exception e) {
			transaction.rollback();
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			session.close();
		}
	}

	/*
	 * public static void printTabDeletedTicketToKitchen(Ticket ticket,Session s) {
	 * try { if(Application.getPrinters().getKitchenPrinterEnable()) {
	 * List<KitchenTicket> kitchenTickets = KitchenTicket.deletefromTicket(ticket);
	 * 
	 * for (KitchenTicket kitchenTicket : kitchenTickets) {
	 * 
	 * if(kitchenTicket.getPrinter() == null)
	 * System.out.println("Get printer null");
	 * 
	 * String deviceName = kitchenTicket.getPrinter().getDeviceName();
	 * 
	 * JasperPrint jasperPrint =
	 * createDeleteKitchenPrint(kitchenTicket,ticket.getType());
	 * 
	 * 
	 * jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
	 * jasperPrint.setProperty("printerName", deviceName);
	 * //JasperViewer.viewReport(jasperPrint, false);
	 * 
	 * System.out.println("Printing to kitchen printer: "+ deviceName);
	 * KitchenDisplay.instance.addTicket(kitchenTicket);
	 * 
	 * s.saveOrUpdate(kitchenTicket);
	 * 
	 * printQuitely(jasperPrint);
	 * 
	 * //markItemsAsPrinted(kitchenTicket); } }
	 * if(Application.getPrinters().getBarPrinterEnable()) { List<KitchenTicket>
	 * kitchenBarTickets = KitchenTicket.deletefromBarTicket(ticket);
	 * 
	 * for (KitchenTicket kitchenTicket : kitchenBarTickets) {
	 * 
	 * String deviceName = kitchenTicket.getBarPrinter();
	 * 
	 * JasperPrint jasperPrint =
	 * createDeleteKitchenPrint(kitchenTicket,ticket.getType());
	 * jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
	 * jasperPrint.setProperty("printerName", deviceName);
	 * //JasperViewer.viewReport(jasperPrint, false);
	 * 
	 * KitchenDisplay.instance.addTicket(kitchenTicket);
	 * 
	 * s.saveOrUpdate(kitchenTicket);
	 * 
	 * printQuitely(jasperPrint);
	 * 
	 * //markItemsAsPrinted(kitchenTicket); } s.saveOrUpdate(ticket); }
	 * 
	 * } catch (Exception e) {
	 * logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e); } finally {} }
	 */

	public static void printQuitely(JasperPrint jasperPrint) throws JRException {
		try {
			jasperPrint.setPageHeight(3000);
			JasperPrintManager.printReport(jasperPrint, false);
			String printerName = jasperPrint.getProperty("printerName");
			if (printerName != null) {
				System.out.println("Printing to :" + printerName);        
			} else {
				System.out.println("Printing");
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static void printQuitelyA4(JasperPrint jasperPrint) throws JRException {
		try {
			//jasperPrint.setPageHeight(3000);
			JasperPrintManager.printReport(jasperPrint, false);
			String printerName = jasperPrint.getProperty("printerName");
			if (printerName != null) {
				System.out.println("Printing to :" + printerName);        
			} else {
				System.out.println("Printing");
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static void printTseCertificate(FiskalyKeyParameter param, boolean a4) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

			HashMap map = new HashMap<String, String>();

			map.put("type", "SD");
			map.put("tse_idnr", param.getTssId());
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.YEAR, 5);
			cal.set(Calendar.MONTH, Calendar.JULY);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			map.put("tse_valid_till", format.format(cal.getTime()));
			map.put("tse_provider", "Fiskaly GmbH");
			map.put("tse_provider_certificate", "BSI-DSZ-CC-1130");
			map.put("tse_serialnumber", param.getTssSerialNumer());
			map.put("barcode", QrcodeBarcodeService.generateBarcode(param.getTssId()));

			try {
				map.put("tse_qrcode",  QrcodeBarcodeService.generateQRCodeInputStream("Fiskaly "+param.getTssSerialNumer()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TseCertificate.jasper";
			if(a4) {
				Restaurant restaurant = RestaurantDAO.getRestaurant();
				map.put(HEADER_LINE1, restaurant.getName());
				map.put(HEADER_LINE2, restaurant.getAddressLine1());
				map.put(HEADER_LINE3, restaurant.getZipCode() + " " + restaurant.getAddressLine3());
				map.put("kasseInfo", "Programmierprotokoll für Khana Kassemsysteme");

				try {
					BufferedImage img_pay = ImageIO.read(new File("resources/images/logo_khana.png"));
					ByteArrayOutputStream bas_pay = new ByteArrayOutputStream();
					ImageIO.write(img_pay, "png", bas_pay);
					InputStream is_khnaa = new ByteArrayInputStream(bas_pay.toByteArray());
					map.put("logo", is_khnaa);
				}catch(Exception ex) {

				}

				FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/Tse_Progrmmierportokoll.jasper";
			}

			JasperPrint jasperPrint = createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());


			String deviceName = Application.getPrinters().getA4Printer();
			jasperPrint.setProperty("printerName", deviceName);
			System.out.println("Printing receipt print to " + deviceName);
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTicketForOwner(Ticket ticket, PrintType type)
	{
		try{
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

			TicketPrintProperties printProperties = new TicketPrintProperties("RECHNUNG", true, true, true,false);
			 
			HashMap map = JReportPrintService.populateTicketProperties(ticket, printProperties, null, PrintType.REGULAR, true, 0.00);
			JasperPrint jasperPrint;
			 
			jasperPrint = JReportPrintService.createPrintForOwner(ticket, map, null);
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			JReportPrintService.printQuitely(jasperPrint);
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public static JasperPrint createPrintForOwner(Ticket ticket, Map<String, Object> map, PosTransaction transaction)
			throws Exception {	
		String FILE_RECEIPT_REPORT = "";	 
		FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport_owner.jasper";
		
		return createJasperPrint_gutchein(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
	}
	
	public static JasperPrint createJasperPrint_gutchein(String reportFile, Map<String, Object> properties,
			JRDataSource dataSource) throws Exception {
		InputStream ticketReportStream = null;
		try {
			ticketReportStream = JReportPrintService.class.getResourceAsStream(reportFile);
			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, properties, dataSource);
			return jasperPrint;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ticketReportStream);
		}
		return null;
	}
	
	public static void printGutschein(Gutschein gutshein) {
		try {
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

			HashMap map = new HashMap();
			String mydata = gutshein.getBarcode();
			Restaurant restaurant = RestaurantDAO.getRestaurant();
			map.put("restaurantName", restaurant.getName()+" "+restaurant.getAddressLine1());
			map.put("restaurant_city", restaurant.getAddressLine2());
			map.put("rstaurant_Address", restaurant.getAddressLine3());
			map.put("wishNote", gutshein.getName());
			String gutsValue = NumberUtil.formatNumber(gutshein.getValue());
			map.put("gutscheinValue", gutsValue.substring(0, gutsValue.indexOf(","))+Application.getCurrencySymbol());

			map.put("barcodeNr", mydata);			
			map.put("barcode", QrcodeBarcodeService.generateBarcode(mydata));
			try {
				if(TerminalConfig.isLogoEnabled())
					map.put("logo", ReportUtil.getLogoStream("logo"));
			} catch(Throwable ex) {

			}			

			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/GutscheinTemplate.jasper";
			JasperPrint jasperPrint = createJasperPrint(FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
			if (jasperPrint != null)
				jasperPrint.setName("GUTS_" + gutshein.getBarcode());

			String deviceName = Application.getPrinters().getReportPrinter();
			jasperPrint.setProperty("printerName", deviceName);
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	// private static void markItemsAsPrinted(KitchenTicket ticket) {
	// List<TicketItem> ticketItems = ticket.getTicketItems();
	// if (ticketItems != null) {
	// for (TicketItem ticketItem : ticketItems) {
	// if (!ticketItem.isPrintedToKitchen()) {
	// ticketItem.setPrintedToKitchen(true);
	// }
	//
	// List<TicketItemModifierGroup> modifierGroups =
	// ticketItem.getTicketItemModifierGroups();
	// if (modifierGroups != null) {
	// for (TicketItemModifierGroup modifierGroup : modifierGroups) {
	// modifierGroup.setPrintedToKitchen(true);
	// }
	// }
	//
	// List<TicketItemCookingInstruction> cookingInstructions =
	// ticketItem.getCookingInstructions();
	// if (cookingInstructions != null) {
	// for (TicketItemCookingInstruction ticketItemCookingInstruction :
	// cookingInstructions) {
	// ticketItemCookingInstruction.setPrintedToKitchen(true);
	// }
	// }
	// }
	// }
	//
	// KitchenTicketDAO.getInstance().saveOrUpdate(ticket);
	// }

	private static String getCardNumber(BankCardMagneticTrack track) {
		String no = "";

		try {
			if (track.getTrack1().hasPrimaryAccountNumber()) {
				no = track.getTrack1().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			} else if (track.getTrack2().hasPrimaryAccountNumber()) {
				no = track.getTrack2().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return no;
	}
}
