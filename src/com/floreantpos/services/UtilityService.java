package com.floreantpos.services;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.util.NumberUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public class UtilityService {
	public UtilityService() {

	}

	public static void printBarcodLevel(MenuItem item) {
		try {
			Double itemPrice = item.getPrice();
			String price = NumberUtil.formatNumber(itemPrice);

			HashMap map = new HashMap();
			map.put("description", item.getDescription2());
			map.put("itemName", UtilityService.getNameFormated(item.getName()));
			map.put("itemPrice", price + " €");
			map.put("itemId", item.getItemId());
			map.put("barcode", UtilityService.createBarcode(item.getBarcode(), 400));
			map.put("infoLable", UtilityService.buildInfoLable(item.getNote(), item.getDescription()));

			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/barcode.jasper";

			if(TerminalConfig.isProductLablePrintBig())
				FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/product_lable_big.jasper";
			else if(TerminalConfig.isKitchenLablePrint())
				FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/product_lable.jasper";

			JasperPrint jasperPrint = JReportPrintService.createJasperPrint(
					FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
			if (jasperPrint != null)
				jasperPrint.setName("barcode:" + item.getName());
			jasperPrint.setProperty("printerName", Application.getPrinters()
					.getSonstigePrinter());
			JReportPrintService.printQuitely(jasperPrint);			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public static final String delimiter = " ";

	public static boolean isMatching(String keywords, String textCompare) {
		boolean exists = true;
		String[] matches = textCompare.split(delimiter);
		String[] keyGiven = keywords.split(delimiter); 
		for (int i=0; i <keyGiven.length; i++){
					if(!matches[i].contains(keyGiven[i])) {
					exists = false;
					break;}			   
		}
		return exists;
	}




	public static String buildInfoLable(String bemerkung, String description) {
		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");	
		if(description!=null&&!description.isEmpty()) {
			JReportPrintService.beginRow(ticketHeaderBuilder);
			JReportPrintService.addColumn(ticketHeaderBuilder, description+" / "+bemerkung);
			JReportPrintService.endRow(ticketHeaderBuilder);
		}

		JReportPrintService.beginRow(ticketHeaderBuilder);
		if(bemerkung==null||bemerkung.isEmpty())
			JReportPrintService.addColumn(ticketHeaderBuilder, POSConstants.PRICE);
		else
			JReportPrintService.addColumn(ticketHeaderBuilder, POSConstants.PRICE+" / "+bemerkung);
		JReportPrintService.endRow(ticketHeaderBuilder);
		return ticketHeaderBuilder.toString();

	}


	public static String getNameFormated(String name) {
		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");
		//	while(name.length()>24) {
		//		System.out.println(name);
		//				
		//	}
		try {
			JReportPrintService.beginRow(ticketHeaderBuilder);
			JReportPrintService.addColumn(ticketHeaderBuilder, name.toUpperCase());
			JReportPrintService.endRow(ticketHeaderBuilder);
			//		name = name.substring(24,name.length());
		}catch(Exception ex) {

		}	
		return ticketHeaderBuilder.toString();
	}



	public static InputStream createBarcode(String barcode, int size) {
		InputStream straeam = null;
		try {
			Code128Bean code128 = new Code128Bean();
			code128.setHeight(15f);
			code128.setModuleWidth(0.3);
			code128.setQuietZone(10);
			code128.doQuietZone(true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos,
					"image/x-png", size, Image.SCALE_DEFAULT, false);
			code128.generateBarcode(canvas, barcode);
			canvas.finish();
			straeam = new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception ex) {

		}			

		return straeam;
	}
}
