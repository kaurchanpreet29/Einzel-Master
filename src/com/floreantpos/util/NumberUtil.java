package com.floreantpos.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
	private final static NumberFormat numberFormat = NumberFormat.getNumberInstance();
	
	static {
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);
	}

	public static double roundToTwoDigit(double value) {
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String formatToTwoDecimal(Double number) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(number);
	}

	public static String formatNumber(Double number) {
	  if(number == null) {
      return numberFormat.format(0);
    }
    
    String value = numberFormat.format(number);
    return value;
	}
	public static String formatNumber(Double number,boolean negative) {
		if(number == null) {
			return numberFormat.format(0);
		}
		
		String value = numberFormat.format(number);
		return value;
	}
	
	public static String paddingTwoDigit(int number) {
		return String.format("%02d", number);
	}
	
	public static String paddingThreeDigit(int number) {
		return String.format( "%03d", number);
	}
	public static String paddingFourDigit(int number) {
		return String.format( "%04d", number);
	}
}
