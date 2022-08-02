package com.khana.tse.fiskaly;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FiskalyNumberFormat {
	public static String formatToTwoDecimal(Double number) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(number).replace(",", ".");
	}
	
	
	public static String formatToThreeDecimal(Double number) {
		NumberFormat formatter = new DecimalFormat("#0.000");
		return formatter.format(number).replace(",", ".");
	}
}
