package com.floreantpos.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LogoCache {

	private static ByteArrayOutputStream headerbas;

	private static ByteArrayOutputStream headerbas2;

	private static ByteArrayOutputStream headerbas3;

	private static ByteArrayOutputStream headerbas4;

	private static ByteArrayOutputStream footerbas;

	private static ByteArrayOutputStream footerbas2;

	private static ByteArrayOutputStream footerbas3;

	private static ByteArrayOutputStream footerbas4;

	private static ByteArrayOutputStream cashDeliveryNoDrinkbas;

	private static ByteArrayOutputStream onlineDeliveryNoDrinkbas;

	private static ByteArrayOutputStream cardDeliveryNoDrinkbas;

	private static ByteArrayOutputStream cashPickupNoDrinkbas;

	private static ByteArrayOutputStream onlinePickupNoDrinkbas;

	private static ByteArrayOutputStream cardPickupNoDrinkbas;

	private static ByteArrayOutputStream cashDeliveryDrinkbas;

	private static ByteArrayOutputStream onlineDeliveryDrinkbas;

	private static ByteArrayOutputStream cardDeliveryDrinkbas;

	private static ByteArrayOutputStream cardPickupDrinkbas;

	private static ByteArrayOutputStream cashPickupDrinkbas;

	private static ByteArrayOutputStream onlinePickupDrinkbas;

	private static ByteArrayOutputStream cashDineinbas;

	private static ByteArrayOutputStream cardDineinbas;
	
	private static ByteArrayOutputStream gutscheinbas;

	private static ByteArrayOutputStream qrBarCodebas;
	
	private static ByteArrayOutputStream onlineDineinbas;

	private static ByteArrayOutputStream transferDineinbas;



	public static InputStream getLogoHeader1() {
		InputStream is = new ByteArrayInputStream(getHeaderBas().toByteArray());
		return is;
	}

	public static InputStream getLogoHeader2() {
		ByteArrayOutputStream os = getHeaderBas2();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getHeaderBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getLogoHeader3() {
		ByteArrayOutputStream os = getHeaderBas3();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getHeaderBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getLogoHeader4() {
		ByteArrayOutputStream os = getHeaderBas4();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getHeaderBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getLogoFooter1() {
		ByteArrayOutputStream footerBas = getFooterBas();
		if(footerBas != null) {
			InputStream is = new ByteArrayInputStream(footerbas.toByteArray());
			return is;
		}
		return null;
	}
	public static InputStream getQrCodeBarcode() {
		try {
			qrBarCodebas = getQrcodeBarcodeBas();

		}catch(Exception ex) {

		}
		if(qrBarCodebas != null) {
			InputStream is = new ByteArrayInputStream(qrBarCodebas.toByteArray());
			return is;
		}
		return null;
	}

	public static InputStream getLogoFooter2() {
		ByteArrayOutputStream os = getFooterBas2();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getFooterBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getLogoFooter3() {
		ByteArrayOutputStream os = getFooterBas3();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getFooterBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getLogoFooter4() {
		ByteArrayOutputStream os = getFooterBas4();
		if (os == null) {
			InputStream is = new ByteArrayInputStream(getFooterBas().toByteArray());
			return is;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public static InputStream getCashNoDrinkDelivery() {
		InputStream is = new ByteArrayInputStream(getCashDeliveryNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getOnlineNoDrinkDelivery() {
		InputStream is = new ByteArrayInputStream(getOnlineDeliveryNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCardNoDrinkDelivery() {
		InputStream is = new ByteArrayInputStream(getCardDeliveryNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCashNoDrinkPickup() {
		InputStream is = new ByteArrayInputStream(getCashPickupNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getOnlineNoDrinkPickup() {
		InputStream is = new ByteArrayInputStream(getOnlinePickupNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCardNoDrinkPickup() {
		InputStream is = new ByteArrayInputStream(getCardPickupNoDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCashDinein() {
		InputStream is = new ByteArrayInputStream(getCashDineinBas().toByteArray());
		return is;
	}
	
	
	public static InputStream getOnlineDinein() {
		InputStream is = new ByteArrayInputStream(getOnlineDineinBas().toByteArray());
		return is;
	}
	public static InputStream getTransferDinein() {
		InputStream is = new ByteArrayInputStream(getTransferDineinBas().toByteArray());
		return is;
	}
	

	public static InputStream getCardDinein() {
		InputStream is = new ByteArrayInputStream(getCardDineinBas().toByteArray());
		return is;
	}
	
	public static InputStream getGutschein() {
		InputStream is = new ByteArrayInputStream(getGutscheinBas().toByteArray());
		return is;
	}

	public static InputStream getCashDeliveryDrink() {
		InputStream is = new ByteArrayInputStream(getCashDeliveryDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getOnlineDeliveryDrink() {
		InputStream is = new ByteArrayInputStream(getOnlineDeliveryDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCardDeliveryDrink() {
		InputStream is = new ByteArrayInputStream(getCardDeliveryDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCardPickupDrink() {
		InputStream is = new ByteArrayInputStream(getCardPickupDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getCashPickupDrink() {
		InputStream is = new ByteArrayInputStream(getCashPickupDrinkBas()
				.toByteArray());
		return is;
	}

	public static InputStream getOnlinePickupDrink() {
		InputStream is = new ByteArrayInputStream(getOnlinePickupDrinkBas()
				.toByteArray());
		return is;
	}

	private static ByteArrayOutputStream bothDeliveryDrinkbas;

	private static ByteArrayOutputStream bothPickupDrinkbas;

	private static ByteArrayOutputStream bothDineinbas;


	public static InputStream getBothDinein() {
		InputStream is = new ByteArrayInputStream(getBothDineinBas().toByteArray());
		return is;
	}
	public static InputStream getBothPickup() {
		InputStream is = new ByteArrayInputStream(getBothPickupBas()
				.toByteArray());
		return is;
	}

	public static InputStream getBothDelivery() {
		InputStream is = new ByteArrayInputStream(getBothDeliveryBas()
				.toByteArray());
		return is;
	}

	public static ByteArrayOutputStream getBothDineinBas() {

		if (bothDineinbas != null) {
			return bothDineinbas;
		}
		try {
			bothDineinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO
					.read(new File("resources/images/bth.png"));
			ImageIO.write(image, "png", bothDineinbas);
			return bothDineinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ByteArrayOutputStream getBothPickupBas() {

		if (bothPickupDrinkbas != null) {
			return bothPickupDrinkbas;
		}
		try {
			bothPickupDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/btha.png"));
			ImageIO.write(image, "png", bothPickupDrinkbas);
			return bothPickupDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getBothDeliveryBas() {

		if (bothDeliveryDrinkbas != null) {
			return bothDeliveryDrinkbas;
		}
		try {
			bothDeliveryDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/bthl.png"));
			ImageIO.write(image, "png", bothDeliveryDrinkbas);
			return bothDeliveryDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getHeaderBas() {

		if (headerbas != null) {
			return headerbas;
		}
		try {
			headerbas = new ByteArrayOutputStream();
			BufferedImage headerImage = ImageIO.read(new File(
					"resources/images/logo.png"));
			ImageIO.write(headerImage, "png", headerbas);
			return headerbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getHeaderBas2() {

		if (headerbas2 != null) {
			return headerbas2;
		}
		try {
			headerbas2 = new ByteArrayOutputStream();
			BufferedImage headerImage = ImageIO.read(new File(
					"resources/images/logo_2.png"));
			ImageIO.write(headerImage, "png", headerbas2);
			return headerbas2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getHeaderBas3() {

		if (headerbas3 != null) {
			return headerbas3;
		}
		try {
			headerbas3 = new ByteArrayOutputStream();
			BufferedImage headerImage = ImageIO.read(new File(
					"resources/images/logo_3.png"));
			ImageIO.write(headerImage, "png", headerbas3);
			return headerbas3;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getHeaderBas4() {

		if (headerbas4 != null) {
			return headerbas4;
		}
		try {
			headerbas4 = new ByteArrayOutputStream();
			BufferedImage headerImage = ImageIO.read(new File(
					"resources/images/logo_4.png"));
			ImageIO.write(headerImage, "png", headerbas4);
			return headerbas4;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getFooterBas() {

		if (footerbas != null) {
			return footerbas;
		}
		try {
			footerbas = new ByteArrayOutputStream();
			BufferedImage footerImage = ImageIO.read(new File(
					"resources/images/footer.png"));
			ImageIO.write(footerImage, "png", footerbas);
			return footerbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getQrcodeBarcodeBas() {

		if (qrBarCodebas != null) {
			return qrBarCodebas;
		}
		try {
			qrBarCodebas = new ByteArrayOutputStream();
			BufferedImage footerImage = ImageIO.read(new File(
					"resources/images/qbcode.png"));
			ImageIO.write(footerImage, "png", qrBarCodebas);
			return qrBarCodebas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}  

	public static ByteArrayOutputStream getFooterBas2() {

		if (footerbas2 != null) {
			return footerbas2;
		}
		try {
			footerbas2 = new ByteArrayOutputStream();
			BufferedImage footerImage = ImageIO.read(new File(
					"resources/images/footer_2.png"));
			ImageIO.write(footerImage, "png", footerbas2);
			return footerbas2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getFooterBas3() {
		if (footerbas3 != null) {
			return footerbas3;
		}
		try {
			footerbas3 = new ByteArrayOutputStream();
			BufferedImage footerImage = ImageIO.read(new File(
					"resources/images/footer_3.png"));
			ImageIO.write(footerImage, "png", footerbas3);
			return footerbas3;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getFooterBas4() {

		if (footerbas4 != null) {
			return footerbas4;
		}
		try {
			footerbas4 = new ByteArrayOutputStream();
			BufferedImage footerImage = ImageIO.read(new File(
					"resources/images/footer_4.png"));
			ImageIO.write(footerImage, "png", footerbas4);
			return footerbas4;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCashDeliveryNoDrinkBas() {

		if (cashDeliveryNoDrinkbas != null) {
			return cashDeliveryNoDrinkbas;
		}
		try {
			cashDeliveryNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/bol.png"));
			ImageIO.write(image, "png", cashDeliveryNoDrinkbas);
			return cashDeliveryNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getOnlineDeliveryNoDrinkBas() {
		if (onlineDeliveryNoDrinkbas != null) {
			return onlineDeliveryNoDrinkbas;
		}
		try {
			onlineDeliveryNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/ool.png"));
			ImageIO.write(image, "png", onlineDeliveryNoDrinkbas);
			return onlineDeliveryNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCardDeliveryNoDrinkBas() {

		if (cardDeliveryNoDrinkbas != null) {
			return cardDeliveryNoDrinkbas;
		}
		try {
			cardDeliveryNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/kol.png"));
			ImageIO.write(image, "png", cardDeliveryNoDrinkbas);
			return cardDeliveryNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCashPickupNoDrinkBas() {

		if (cashPickupNoDrinkbas != null) {
			return cashPickupNoDrinkbas;
		}
		try {
			cashPickupNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/boa.png"));
			ImageIO.write(image, "png", cashPickupNoDrinkbas);
			return cashPickupNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getOnlinePickupNoDrinkBas() {

		if (onlinePickupNoDrinkbas != null) {
			return onlinePickupNoDrinkbas;
		}
		try {
			onlinePickupNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/ooa.png"));
			ImageIO.write(image, "png", onlinePickupNoDrinkbas);
			return onlinePickupNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCardPickupNoDrinkBas() {

		if (cardPickupNoDrinkbas != null) {
			return cardPickupNoDrinkbas;
		}
		try {
			cardPickupNoDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/koa.png"));
			ImageIO.write(image, "png", cardPickupNoDrinkbas);
			return cardPickupNoDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCashDeliveryDrinkBas() {

		if (cashDeliveryDrinkbas != null) {
			return cashDeliveryDrinkbas;
		}
		try {
			cashDeliveryDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/bgl.png"));
			ImageIO.write(image, "png", cashDeliveryDrinkbas);
			return cashDeliveryDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getOnlineDeliveryDrinkBas() {

		if (onlineDeliveryDrinkbas != null) {
			return onlineDeliveryDrinkbas;
		}
		try {
			onlineDeliveryDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/ogl.png"));
			ImageIO.write(image, "png", onlineDeliveryDrinkbas);
			return onlineDeliveryDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCardDeliveryDrinkBas() {

		if (cardDeliveryDrinkbas != null) {
			return cardDeliveryDrinkbas;
		}
		try {
			cardDeliveryDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/kgl.png"));
			ImageIO.write(image, "png", cardDeliveryDrinkbas);
			return cardDeliveryDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCardPickupDrinkBas() {

		if (cardPickupDrinkbas != null) {
			return cardPickupDrinkbas;
		}
		try {
			cardPickupDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/kga.png"));
			ImageIO.write(image, "png", cardPickupDrinkbas);
			return cardPickupDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCashPickupDrinkBas() {

		if (cashPickupDrinkbas != null) {
			return cashPickupDrinkbas;
		}
		try {
			cashPickupDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/bga.png"));
			ImageIO.write(image, "png", cashPickupDrinkbas);
			return cashPickupDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getOnlinePickupDrinkBas() {

		if (onlinePickupDrinkbas != null) {
			return onlinePickupDrinkbas;
		}
		try {
			onlinePickupDrinkbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/oga.png"));
			ImageIO.write(image, "png", onlinePickupDrinkbas);
			return onlinePickupDrinkbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayOutputStream getCashDineinBas() {

		if (cashDineinbas != null) {
			return cashDineinbas;
		}
		try {
			cashDineinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/bar.png"));
			ImageIO.write(image, "png", cashDineinbas);
			return cashDineinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static ByteArrayOutputStream getOnlineDineinBas() {

		if (onlineDineinbas != null) {
			return onlineDineinbas;
		}
		try {
			onlineDineinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/online.png"));
			ImageIO.write(image, "png", onlineDineinbas);
			return onlineDineinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ByteArrayOutputStream getTransferDineinBas() {

		if (transferDineinbas != null) {
			return transferDineinbas;
		}
		try {
			transferDineinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File("resources/images/transfer.png"));
			ImageIO.write(image, "png", transferDineinbas);
			return transferDineinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ByteArrayOutputStream getGutscheinBas() {

		if (gutscheinbas != null) {
			return gutscheinbas;
		}
		try {
			gutscheinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO
					.read(new File("resources/images/guts.png"));
			ImageIO.write(image, "png", gutscheinbas);
			return gutscheinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ByteArrayOutputStream getCardDineinBas() {

		if (cardDineinbas != null) {
			return cardDineinbas;
		}
		try {
			cardDineinbas = new ByteArrayOutputStream();
			BufferedImage image = ImageIO
					.read(new File("resources/images/karte.png"));
			ImageIO.write(image, "png", cardDineinbas);
			return cardDineinbas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
