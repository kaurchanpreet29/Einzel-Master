package com.floreantpos.services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import bsh.StringUtil;

public class QrcodeBarcodeService {
	public static InputStream generateQRCodeInputStream(String barcodeText) throws Exception {
		if(StringUtils.isEmpty(barcodeText))
			return null;
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = 
				barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 400, 400);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
		return new ByteArrayInputStream(os.toByteArray());
	}

	public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = 
				barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 400, 400);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}


	public static InputStream generateBarcode(String myData) {
		try {
			Code128Bean code128 = new Code128Bean();
			code128.setHeight(15f);			
			code128.setModuleWidth(0.3);//imp Dont Change
			code128.setQuietZone(10);//imp Dont Change
			code128.doQuietZone(true);
			code128.setMsgPosition(HumanReadablePlacement.HRP_NONE);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 400, Image.SCALE_DEFAULT, true);
			code128.generateBarcode(canvas, myData);
			canvas.finish();
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception ex) {
			return null;
		}
	}
}