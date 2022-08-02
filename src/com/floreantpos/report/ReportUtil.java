package com.floreantpos.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.util.CollectionUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.util.BusinessDateUtil;

public class ReportUtil {
	public static void populateRestaurantProperties(Map map) throws IOException {

		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		map.put("restaurantName", restaurant.getName());
		map.put("addressLine1", restaurant.getAddressLine1());
		map.put("addressLine2", restaurant.getAddressLine2());
		map.put("addressLine3", restaurant.getZipCode() + " " +restaurant.getAddressLine3());
		map.put("phone", "Tel.:"+restaurant.getTelephone());
		map.put("taxNr", "Steuer-Nr.:"+restaurant.getTicketFooterMessage2());
	}

	private static final String HEADER_LINE6 = "headerLine6";
	private static final String HEADER_LINE7 = "headerLine7";
	private static final String HEADER_LINE4 = "headerLine4";
	private static final String HEADER_LINE3 = "headerLine3";
	private static final String HEADER_LINE2 = "headerLine2";
	private static final String HEADER_LINE1 = "headerLine1";
	private static final String HEADER_LINE5 = "headerLine5";
	private static final String HEADER_LINE8 = "headerLine8";

	public static void  populateHeaderDetails(Map map, PrintType printType, boolean mitStuer) throws IOException {
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));

		if (printType == PrintType.REGULAR2)
			map.put(HEADER_LINE1, restaurant.getSecondaryName());
		else
			map.put(HEADER_LINE1, restaurant.getName());
		map.put(HEADER_LINE2, restaurant.getAddressLine1());
		map.put(HEADER_LINE3, restaurant.getAddressLine2());
		map.put(HEADER_LINE4, restaurant.getZipCode() + " " + restaurant.getAddressLine3());

		if (printType == PrintType.REGULAR2 && restaurant.getSecondaryTelephone() != null
				&& restaurant.getSecondaryTelephone().length() > 0)
			map.put(HEADER_LINE5, "Tel:" + restaurant.getSecondaryTelephone());
		else if (restaurant.getTelephone() != null && restaurant.getTelephone().length() > 0)
			map.put(HEADER_LINE5, "Tel:" + restaurant.getTelephone());

		if (restaurant.getFax() != null && restaurant.getFax().length() > 0)
			map.put(HEADER_LINE6, "Fax:" + restaurant.getFax());

		if (restaurant.getFax() != null && restaurant.getFax().length() > 0)
			map.put(HEADER_LINE7, "Email:" + restaurant.getFax());	  
		if (restaurant.getFax() != null && restaurant.getFax().length() > 0)
			map.put(HEADER_LINE8, "Web:" + restaurant.getFax());	  

	}
	
	public static String removeLeadingZeroes(String str) {
		String strPattern = "^0+(?!$)";
		str = str.replaceAll(strPattern, "");
		return str;
	}

	public static int generateRandom(int max, int min){
		Random rn = new Random();
		int n = max - min + 1;
		int i = rn.nextInt() % n;
		return (min + i);

	}

	public static boolean isSpecial(Date checkDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 30);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.YEAR, 2020);	
		cal.set(Calendar.HOUR_OF_DAY, 05);
		cal.set(Calendar.MINUTE, 59);
		Date startDate = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.YEAR, 2021);
		Date endDate = cal.getTime();
		return startDate.compareTo(checkDate) * checkDate.compareTo(endDate) >= 0;		
	}

	public static double getDiscountByType(List<TicketCouponAndDiscount> disList, boolean gutschein, boolean dirRabat, boolean rabatt) {
		double discount = 0.0;		
		if(disList!=null&&disList.size()>0)
			for(TicketCouponAndDiscount dis:disList)
				if(gutschein&&dis.getType()==CouponAndDiscount.GUTSCHEIN)
					discount += dis.getUsedValue();
				else if(rabatt&&dis.getType()==CouponAndDiscount.PERCENTAGE_PER_ITEM||rabatt&&dis.getType()==CouponAndDiscount.PERCENTAGE_PER_ORDER)
					discount += dis.getUsedValue();
				else if(dirRabat&&dis.getType()==CouponAndDiscount.DIRECT_RABATT)
					discount += dis.getUsedValue();
		return discount;
	}

	public static double getFixRabatt(final TicketItem ticketItem) {
		double couponAmount = 0.00;
		List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
		try {
		if(!CollectionUtils.isEmpty(modifierGroups)) {
			for(TicketItemModifierGroup group : modifierGroups) {
				List<TicketItemModifier> modifiers = group.getTicketItemModifiers();

				if(!CollectionUtils.isEmpty(modifiers)) {
					for(TicketItemModifier modifier : modifiers) {
						if(modifier.getTotalAmount()<0&&modifier.getNameDisplay().contains("Art-Rabatt"))
							couponAmount -= modifier.getTotalAmount();
					}
				}
			}
		  }
		} catch (Exception e) {}
		return couponAmount;
	} 
	
	public static boolean isTseActiv(Date checkDate) {
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		Date startDate = restaurant.getTseActivateDate();
		Date endDate = restaurant.getTseValidDate();
		if(TerminalConfig.isBuildMode()||TerminalConfig.isDebugMode())
			System.out.println("TSE active Date "+startDate+" checkDate "+checkDate+" EndDate "+endDate);
		if(startDate==null||endDate==null) {
			if(TerminalConfig.isTseEnable()&&restaurant.isTseLive()&&restaurant.getTseId()!=null) {
				startDate = BusinessDateUtil.startOfOfficialDay(new Date());			
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.YEAR, 5);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				endDate = cal.getTime();
				restaurant.setTseActivateDate(startDate);
				restaurant.setTseValidDate(endDate);
				dao.saveOrUpdate(restaurant);
			}
			
			return false;
		}
			
		return startDate.compareTo(checkDate) * checkDate.compareTo(endDate) >= 0;
	}

	public static InputStream getLogoStream(String logoName) {
		InputStream is = new ByteArrayInputStream(getLogoBas(logoName).toByteArray());
		return is;
	}
	
	private static ByteArrayOutputStream headerbas;
	public static ByteArrayOutputStream getLogoBas(String logoName) {
		try {
			headerbas = new ByteArrayOutputStream();
			BufferedImage headerImage = ImageIO.read(new File(
					"resources/images/"+logoName+".png"));
			ImageIO.write(headerImage, "png", headerbas);
			return headerbas;
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static String twoDigitPadding(String num) {
		if (num.length() == 1) {
			num = "0" + num;
		}
		return num;
	}
}
