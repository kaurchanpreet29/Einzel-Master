package com.floreantpos.report;

public class ReportItem {
	private String id;
	private Double food;
	private Double foodTax;
	private Double beverage;
	private Double beverageTax;
	private double tt;
	private double awt;
	private double awot;
	private double ttd;
	private double tts;
	private double ttz;
	private double awt19;
	private double netton;
	private double nettos;
	private double nettoz;
	private Double awt7;
	private double cancelledtrans;
	private Double soldGutschein;
	private double cancelledTax;
	private double cashPayment;
	private long cashPaymentCount;
	private double cashTax;
	private double cardPayment;
	private long cardPaymentCount;
	private double cardTax;
	private double taxType;
	
	private double goodscheinCardPayment;
	private long goodscheinCardPaymentCount;
	private double goodscheinCardTax;
	
	private double directRabatt;
	
	private String goodscheinCardPayment_text;
	//private String goodscheinCardPaymentCount_text;
	//private String goodscheinCardTax_text;
	
	private double onlinePayment;
	private double onlineTax;
	private long cancelledItems;
	private long totalInvoices;
	private long totalSoldItems;
	
	private long voidArticles;
	private long voidTickets;
	private Double totalCash;
	private Double voidAmount;
	private Double discountAmount;
	private Double awt0;
	private Double tt0;
	private long insert;
	private Double insertAmt;

	private Double pfand1;
	private Double pfand2;
	private Double pfand3;


	private int anzahlRetour;
	private Double retourGesamt;
	private Double retourTax;
	private Double gesamtSumme;
	private Double gesamtMwst19;
	private Double gesamtMwst7;

	//salesReportText_Field
	private String einnahme_text;
	private String umasat_gesamt_text;
	private String gesamt_19_text;
	private String gesamt_7_text;
	private String gesamt_0_text;
	private String gesamt_netto_text;
	private String netto_19_text;
	private String netto_7_text;
	private String netto_0_text;
	private String mwst_19_text;
	private String mwst_7_text;
	private String mwst_0_text;
	private String anzahl_retour_text;
	private String retour_gesamt_text;
	private String retour_mwst_text;
	private String anzahl_storno_text;
	private String anzahl_storno_gesamt_text;
	private String storno_mwst_text;
	private String anzahl_rechnugen_text;
	private String anzahl_sold_items_text;
	private String cashpayment_text;
	private String mwst_gesamt_text;
	private String mwst_gesamt_text1;
	private String anzahl_text;
	private String anzahl_text1;
	private String anzahl_text2; 
	private String cardpayment_text;
	private String kunden_rabatt_text;
	private String cash_in_cashdrawer_text;
	private String gesamt_summe_text;
	private String warengroup_abs_text;
	private String warengroup_text;
	private String gesamt_text;
	private String anzahl_text_rechnug;
	private String mwst_gesamt_text_rechnug;
	private String rechnugPayment_text;
	private String rechnugPayment_tax;
	private String rechnugPament_anzahl;
	private String rechnugPaymentAmount;
	
	private String anzahl_text_online;
	private String mwst_gesamt_text_online;
	private String onlinePayment_text;
	private String onlinePayment_tax;
	private String onlinePament_anzahl;
	private String onlinePaymentAmount;
	
	private String gutschein_eingelost_text;
	private String verkaufte_gutschein_text;	
	private String bar_tip_text;
	private String other_tip_text;
	
	private String rabatt_19text;
	private String rabatt_7text;
	private Double rabatt_19;
	private Double rabatt_7;

	private String cash19Text;
	private String cash7Text;
	private String card19Text;
	private String card7Text;
	
	private Double cash19;
	private Double cash7;
	private Double card19;
	private Double card7;
	
	private Double btrinkgeld;
	private Double Ktrinkgeld;
	private double ticketGutscheinAmount;
	private double unitPrice;

	public ReportItem() {
		super();
	}

	
	public String getRabatt_19text() {
		return rabatt_19text;
	}

	public void setRabatt_19text(String rabatt_19text) {
		this.rabatt_19text = rabatt_19text;
	}
	
	public String getRabatt_7text() {
		return rabatt_7text;
	}

	public void setRabatt_7text(String rabatt_7text) {
		this.rabatt_7text = rabatt_7text;
	}
	
	public double getRabatt_19() {
		return rabatt_19;
	}

	public void setRabatt_19(double rabatt_19) {
		this.rabatt_19 = rabatt_19;
	}
	
	public double getRabatt_7() {
		return rabatt_7;
	}

	public void setRabatt_7(double rabatt_7) {
		this.rabatt_7 = rabatt_7;
	}
	
	public String getOther_tip_text() {
		return other_tip_text;
	}

	public void setOther_tip_text(String other_tip_text) {
		this.other_tip_text = other_tip_text;
	}
	
	public String getBar_tip_text() {
		return bar_tip_text;
	}

	public void setBar_tip_text(String bar_tip_text) {
		this.bar_tip_text = bar_tip_text;
	}
	
	public String getVerkaufte_gutschein_text() {
		return verkaufte_gutschein_text;
	}

	public void setVerkaufte_gutschein_text(String verkaufte_gutschein_text) {
		this.verkaufte_gutschein_text = verkaufte_gutschein_text;
	}
	
	public String getGutschein_eingelost_text() {
		return gutschein_eingelost_text;
	}

	public void setGutschein_eingelost_text(String gutschein_eingelost_text) {
		this.gutschein_eingelost_text = gutschein_eingelost_text;
	}
	
	public String getGoodscheinCardPayment_text() {
		return goodscheinCardPayment_text;
	}

	public void setGoodscheinCardPayment_text(String goodscheinCardPayment_text) {
		this.goodscheinCardPayment_text = goodscheinCardPayment_text;
	}
	
	public Double getFood() {
		return food;
	}
	
	
	public double getDirectRabatt() {
		return directRabatt;
	}

	public void setDirectRabatt(double directRabatt) {
		this.directRabatt = directRabatt;
	}
	
	public double getGoodscheinCardPayment() {
		return goodscheinCardPayment;
	}

	public void setGoodscheinCardPayment(double goodscheinCardPayment) {
		this.goodscheinCardPayment = goodscheinCardPayment;
	}

	public long getGoodscheinCardPaymentCount() {
		return goodscheinCardPaymentCount;
	}

	public void setGoodscheinCardPaymentCount(long goodscheinCardPaymentCount) {
		this.goodscheinCardPaymentCount = goodscheinCardPaymentCount;
	}

	public double getGoodscheinCardTax() {
		return goodscheinCardTax;
	}

	public void setGoodscheinCardTax(double goodscheinCardTax) {
		this.goodscheinCardTax = goodscheinCardTax;
	}
	
	public long getCashPaymentCount() {
		return cashPaymentCount;
	}

	public Double getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	public void setCashPaymentCount(long cashPaymentCount) {
		this.cashPaymentCount = cashPaymentCount;
	}

	public long getCardPaymentCount() {
		return cardPaymentCount;
	}

	public void setCardPaymentCount(long cardPaymentCount) {
		this.cardPaymentCount = cardPaymentCount;
	}

	public void setFood(Double food) {
		this.food = food;
	}

	public void setFoodTax(Double foodTax) {
		this.foodTax = foodTax;
	}

	public Double getPfand1() {
		return pfand1;
	}

	public void setPfand1(Double pfand1) {
		this.pfand1 = pfand1;
	}

	public Double getPfand2() {
		return pfand2;
	}

	public void setPfand2(Double pfand2) {
		this.pfand2 = pfand2;
	}

	public Double getPfand3() {
		return pfand3;
	}

	public void setPfand3(Double pfand3) {
		this.pfand3 = pfand3;
	}

	public Double getFoodTax() {
		return foodTax;
	}

	public void setBeverage(Double beverage) {
		this.beverage = beverage;
	}

	public Double getBeverage() {
		return beverage;
	}

	public Double getBeverageCount() {
		return beverageTax;
	}

	public void setBeverageTax(Double beverageTax) {
		this.beverageTax = beverageTax;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getawt() {
		return awt;
	}

	public void setAwt(Double awt) {
		this.awt = awt;
	}

	public Double getawt19() {
		return awt19;
	}

	public void setAwt19(Double awt19) {
		this.awt19 = awt19;
	}

	public Double getawt7() {
		return awt7;
	}

	public void setAwt7(Double awt7) {
		this.awt7 = awt7;
	}

	public Double getCancelledTrans() {
		return cancelledtrans;
	}

	public void setCancelledTrans(Double cancelledTrans) {
		this.cancelledtrans = cancelledTrans;
	}

	public void setCancelledTax(Double cancelledTax) {
		this.cancelledTax = cancelledTax;
	}

	public Double getCancelledTax() {
		return cancelledTax;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAwot() {
		return awot;
	}
	
	public void setAwot(Double awot) {
		this.awot = awot;
	}

	public double getttd() {
		return ttd;
	}

	public void setttd(double ttd) {
		this.ttd = ttd;
	}

	public double gett() {
		return tt;
	}

	public void sett(double tt) {
		this.tt = tt;
	}

	public double gettts() {
		return tts;
	}

	public void settts(double tts) {
		this.tts = tts;
	}

	public double getttz() {
		return ttz;
	}

	public void setttz(double ttz) {
		this.ttz = ttz;
	}

	public Double getCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(Double cashPayment) {
		this.cashPayment = cashPayment;
	}

	public Double getCashTax() {
		return cashTax;
	}

	public void setCashTax(Double cashTax) {
		this.cashTax = cashTax;
	}

	public Double getCardPayment() {
		return cardPayment;
	}

	public void setCardPayment(Double cardPayment) {
		this.cardPayment = cardPayment;
	}

	public Double getCardTax() {
		return cardTax;
	}

	public void setCardTax(Double cardTax) {
		this.cardTax = cardTax;
	}

	public long getCancelledItems() {
		return cancelledItems;
	}

	public void setCancelledItems(long cancelledItems) {
		this.cancelledItems = cancelledItems;
	}

	public Double getOnlinePayment() {
		return onlinePayment;
	}

	public void setOnlinePayment(Double onlinePayment) {
		this.onlinePayment = onlinePayment;
	}

	public Double getOnlineTax() {
		return onlineTax;
	}

	public void setOnlineTax(Double onlineTax) {
		this.onlineTax = onlineTax;
	}

	public long getTotalInvoices() {
		return totalInvoices;
	}

	public void setTotalInvoices(long totalInvoices) {
		this.totalInvoices = totalInvoices;
	}

	public long getTotalSoldItems() {
		return totalSoldItems;
	}

	public void setTotalSoldItems(long totalSoldItems) {
		this.totalSoldItems = totalSoldItems;
	}

	public long getVoidTickets() {
		return voidTickets;
	}

	public void setVoidTickets(long voidTickets) {
		this.voidTickets = voidTickets;
	}
	
	public long getVoidArticles() {
		return voidArticles;
	}

	public void setVoidArticles(long voidArticles) {
		this.voidArticles = voidArticles;
	}

	public Double getVoidAmount() {
		return voidAmount;
	}

	public void setVoidAmount(Double VoidAmount) {
		this.voidAmount = VoidAmount;
	}

	public void setAwt0(Double awt0) {
		this.awt0 = awt0;
	}

	public Double getAwt0() {
		return awt0;
	}

	public void settt0(Double tt0) {
		this.tt0 = tt0;
	}

	public Double gettt0() {
		return tt0;
	}

	public void setInsert(long insert) {
		this.insert = insert;
	}

	public long getInsert() {
		return insert;
	}

	public void setInsertAmt(Double insertAmt) {
		this.insertAmt = insertAmt;
	}

	public Double getInsertAmt() {
		return insertAmt;
	}

	public double getNetton() {
		return netton;
	}

	public void setNetton(double netton) {
		this.netton = netton;
	}

	public double getNettos() {
		return nettos;
	}

	public void setNettos(double nettos) {
		this.nettos = nettos;
	}

	public double getNettoz() {
		return nettoz;
	}

	public void setNettoz(double nettoz) {
		this.nettoz = nettoz;
	}

	public int getAnzahlRetour() {
		return anzahlRetour;
	}

	public void setAnzahlRetour(int anzahlRetour) {
		this.anzahlRetour = anzahlRetour;
	}

	public Double getRetourGesamt() {
		return retourGesamt;
	}

	public void setRetourGesamt(Double retourGesamt) {
		
		this.retourGesamt = retourGesamt;
	}

	public Double getRetourTax() {
		return retourTax;
	}

	public void setRetourTax(Double retourTax) {
		this.retourTax = retourTax;
	}

	public Double getGesamtSumme() {
		return gesamtSumme;
	}

	public void setGesamtSumme(Double gesamtSumme) {
		this.gesamtSumme = gesamtSumme;
	}

	public Double getGesamtMwst19() {
		return gesamtMwst19;
	}

	public void setGesamtMwst19(Double gesamtMwst19) {
		this.gesamtMwst19 = gesamtMwst19;
	}

	public Double getGesamtMwst7() {
		return gesamtMwst7;
	}

	public void setGesamtMwst7(Double gesamtMwst7) {
		this.gesamtMwst7 = gesamtMwst7;
	}

	public String getEinnahme_text() {
		return einnahme_text;
	}

	public void setEinnahme_text(String einnahme_text) {
		this.einnahme_text = einnahme_text;
	}

	public String getUmasat_gesamt_text() {
		return umasat_gesamt_text;
	}

	public void setUmasat_gesamt_text(String umasat_gesamt_text) {
		this.umasat_gesamt_text = umasat_gesamt_text;
	}

	public String getGesamt_19_text() {
		return gesamt_19_text;
	}

	public void setGesamt_19_text(String gesamt_19_text) {
		this.gesamt_19_text = gesamt_19_text;
	}

	public String getGesamt_7_text() {
		return gesamt_7_text;
	}

	public void setGesamt_7_text(String gesamt_7_text) {
		this.gesamt_7_text = gesamt_7_text;
	}

	public String getGesamt_0_text() {
		return gesamt_0_text;
	}

	public void setGesamt_0_text(String gesamt_0_text) {
		this.gesamt_0_text = gesamt_0_text;
	}

	public String getGesamt_netto_text() {
		return gesamt_netto_text;
	}

	public void setGesamt_netto_text(String gesamt_netto_text) {
		this.gesamt_netto_text = gesamt_netto_text;
	}

	public String getNetto_19_text() {
		return netto_19_text;
	}

	public void setNetto_19_text(String netto_19_text) {
		this.netto_19_text = netto_19_text;
	}

	public String getNetto_7_text() {
		return netto_7_text;
	}

	public void setNetto_7_text(String netto_7_text) {
		this.netto_7_text = netto_7_text;
	}

	public String getNetto_0_text() {
		return netto_0_text;
	}

	public void setNetto_0_text(String netto_0_text) {
		this.netto_0_text = netto_0_text;
	}

	public String getMwst_19_text() {
		return mwst_19_text;
	}

	public void setMwst_19_text(String mwst_19_text) {
		this.mwst_19_text = mwst_19_text;
	}

	public String getMwst_7_text() {
		return mwst_7_text;
	}

	public void setMwst_7_text(String mwst_7_text) {
		this.mwst_7_text = mwst_7_text;
	}

	public String getMwst_0_text() {
		return mwst_0_text;
	}

	public void setMwst_0_text(String mwst_0_text) {
		this.mwst_0_text = mwst_0_text;
	}

	public String getAnzahl_retour_text() {
		return anzahl_retour_text;
	}

	public void setAnzahl_retour_text(String anzahl_retour_text) {
		this.anzahl_retour_text = anzahl_retour_text;
	}

	public String getRetour_gesamt_text() {
		return retour_gesamt_text;
	}

	public void setRetour_gesamt_text(String rtourn_gesamt_text) {
		this.retour_gesamt_text = rtourn_gesamt_text;
	}

	public String getRetour_mwst_text() {
		return retour_mwst_text;
	}

	public void setRetour_mwst_text(String retour_mwst_text) {
		this.retour_mwst_text = retour_mwst_text;
	}

	public String getAnzahl_storno_text() {
		return anzahl_storno_text;
	}

	public void setAnzahl_storno_text(String anzahl_storno_text) {
		this.anzahl_storno_text = anzahl_storno_text;
	}

	public String getAnzahl_storno_gesamt_text() {
		return anzahl_storno_gesamt_text;
	}

	public void setAnzahl_storno_gesamt_text(String anzahl_storno_gesamt_text) {
		this.anzahl_storno_gesamt_text = anzahl_storno_gesamt_text;
	}

	public String getStorno_mwst_text() {
		return storno_mwst_text;
	}

	public void setStorno_mwst_text(String storno_mwst_text) {
		this.storno_mwst_text = storno_mwst_text;
	}

	public String getAnzahl_rechnugen_text() {
		return anzahl_rechnugen_text;
	}

	public void setAnzahl_rechnugen_text(String anzahl_rechnugen_text) {
		this.anzahl_rechnugen_text = anzahl_rechnugen_text;
	}

	public String getAnzahl_sold_items_text() {
		return anzahl_sold_items_text;
	}

	public void setAnzahl_sold_items_text(String anzahl_sold_items_text) {
		this.anzahl_sold_items_text = anzahl_sold_items_text;
	}

	public String getCashpayment_text() {
		return cashpayment_text;
	}

	public void setCashpayment_text(String cashpayment_text) {
		this.cashpayment_text = cashpayment_text;
	}

	public String getMwst_gesamt_text() {
		return mwst_gesamt_text;
	}

	public void setMwst_gesamt_text(String mwst_gesamt_text) {
		this.mwst_gesamt_text = mwst_gesamt_text;
	}

	public String getAnzahl_text() {
		return anzahl_text;
	}

	public void setAnzahl_text(String anzahl_text) {
		this.anzahl_text = anzahl_text;
	}

	public String getCardpayment_text() {
		return cardpayment_text;
	}

	public void setCardpayment_text(String cardpayment_text) {
		this.cardpayment_text = cardpayment_text;
	}

	public String getKunden_rabatt_text() {
		return kunden_rabatt_text;
	}

	public void setKunden_rabatt_text(String kunden_rabatt_text) {
		this.kunden_rabatt_text = kunden_rabatt_text;
	}

	public String getCash_in_cashdrawer_text() {
		return cash_in_cashdrawer_text;
	}

	public void setCash_in_cashdrawer_text(String cash_in_cashdrawer_text) {
		this.cash_in_cashdrawer_text = cash_in_cashdrawer_text;
	}

	public String getGesamt_summe_text() {
		return gesamt_summe_text;
	}

	public void setGesamt_summe_text(String gesamt_summe_text) {
		this.gesamt_summe_text = gesamt_summe_text;
	}

	public String getWarengroup_abs_text() {
		return warengroup_abs_text;
	}

	public void setWarengroup_abs_text(String warengroup_abs_text) {
		this.warengroup_abs_text = warengroup_abs_text;
	}

	public String getWarengroup_text() {
		return warengroup_text;
	}

	public void setWarengroup_text(String warengroup_text) {
		this.warengroup_text = warengroup_text;
	}

	public String getGesamt_text() {
		return gesamt_text;
	}

	public void setGesamt_text(String gesamt_text) {
		this.gesamt_text = gesamt_text;
	}

	public String getMwst_gesamt_text1() {
		return mwst_gesamt_text1;
	}

	public void setMwst_gesamt_text1(String mwst_gesamt_text1) {
		this.mwst_gesamt_text1 = mwst_gesamt_text1;
	}

	public String getAnzahl_text1() {
		return anzahl_text1;
	}

	public void setAnzahl_text1(String anzahl_text1) {
		this.anzahl_text1 = anzahl_text1;
	}

	public String getAnzahl_text2() {
		return anzahl_text2;
	}

	public void setAnzahl_text2(String anzahl_text2) {
		this.anzahl_text2 = anzahl_text2;
	}

	public String getAnzahl_text_rechnug() {
		return anzahl_text_rechnug;
	}

	public void setAnzahl_text_rechnug(String anzahl_text_rechnug) {
		this.anzahl_text_rechnug = anzahl_text_rechnug;
	}

	public String getMwst_gesamt_text_rechnug() {
		return mwst_gesamt_text_rechnug;
	}

	public void setMwst_gesamt_text_rechnug(String mwst_gesamt_text_rechnug) {
		this.mwst_gesamt_text_rechnug = mwst_gesamt_text_rechnug;
	}

	public String getRechnugPayment_text() {
		return rechnugPayment_text;
	}

	public void setRechnugPayment_text(String rechnugPayment_text) {
		this.rechnugPayment_text = rechnugPayment_text;
	}

	public String getRechnugPayment_tax() {
		return rechnugPayment_tax;
	}

	public void setRechnugPayment_tax(String rechnugPayment_tax) {
		this.rechnugPayment_tax = rechnugPayment_tax;
	}

	public String getRechnugPament_anzahl() {
		return rechnugPament_anzahl;
	}

	public void setRechnugPament_anzahl(String rechnugPament_anzahl) {
		this.rechnugPament_anzahl = rechnugPament_anzahl;
	}

	public String getRechnugPaymentAmount() {
		return rechnugPaymentAmount;
	}

	public void setRechnugPaymentAmount(String rechnugPaymentAmount) {
		this.rechnugPaymentAmount = rechnugPaymentAmount;
	}
	

	public String getMwst_gesamt_text_online() {
		return mwst_gesamt_text_online;
	}

	public void setMwst_gesamt_text_online(String mwst_gesamt_text_online) {
		this.mwst_gesamt_text_online = mwst_gesamt_text_online;
	}

	public String getOnlinePayment_text() {
		return onlinePayment_text;
	}

	public void setOnlinePayment_text(String onlinePayment_text) {
		this.onlinePayment_text = onlinePayment_text;
	}

	public String getOnlinePayment_tax() {
		return onlinePayment_tax;
	}

	public void setOnlinePayment_tax(String onlinePayment_tax) {
		this.onlinePayment_tax = onlinePayment_tax;
	}

	public String getOnlinePament_anzahl() {
		return onlinePament_anzahl;
	}

	public void setOnlinePament_anzahl(String onlinePament_anzahl) {
		this.onlinePament_anzahl = onlinePament_anzahl;
	}

	public String getOnlinePaymentAmount() {
		return onlinePaymentAmount;
	}

	public void setOnlinePaymentAmount(String onlinePaymentAmount) {
		this.onlinePaymentAmount = onlinePaymentAmount;
	}

	public void setOnlinePayment(double onlinePayment) {
		this.onlinePayment = onlinePayment;
	}
	
	public String getAnzahl_text_online() {
		return anzahl_text_online;
	}

	public void setAnzahl_text_online(String anzahl_text_online) {
		this.anzahl_text_online = anzahl_text_online;
	}


	/**
	 * @return the cash19
	 */
	public Double getCash19() {
		return cash19;
	}

	/**
	 * @param cash19 the cash19 to set
	 */
	public void setCash19(Double cash19) {
		this.cash19 = cash19;
	}

	/**
	 * @return the cash7
	 */
	public Double getCash7() {
		return cash7;
	}

	/**
	 * @param cash7 the cash7 to set
	 */
	public void setCash7(Double cash7) {
		this.cash7 = cash7;
	}

	/**
	 * @return the card19
	 */
	public Double getCard19() {
		return card19;
	}

	/**
	 * @param card19 the card19 to set
	 */
	public void setCard19(Double card19) {
		this.card19 = card19;
	}

	/**
	 * @return the card7
	 */
	public Double getCard7() {
		return card7;
	}

	/**
	 * @param card7 the card7 to set
	 */
	public void setCard7(Double card7) {
		this.card7 = card7;
	}

	/**
	 * @return the cash19Text
	 */
	public String getCash19Text() {
		return cash19Text;
	}

	/**
	 * @param cash19Text the cash19Text to set
	 */
	public void setCash19Text(String cash19Text) {
		this.cash19Text = cash19Text;
	}

	/**
	 * @return the cash7Text
	 */
	public String getCash7Text() {
		return cash7Text;
	}

	/**
	 * @param cash7Text the cash7Text to set
	 */
	public void setCash7Text(String cash7Text) {
		this.cash7Text = cash7Text;
	}

	/**
	 * @return the card19Text
	 */
	public String getCard19Text() {
		return card19Text;
	}

	/**
	 * @param card19Text the card19Text to set
	 */
	public void setCard19Text(String card19Text) {
		this.card19Text = card19Text;
	}

	/**
	 * @return the card7Text
	 */
	public String getCard7Text() {
		return card7Text;
	}

	/**
	 * @param card7Text the card7Text to set
	 */
	public void setCard7Text(String card7Text) {
		this.card7Text = card7Text;
	}
	
	public Double getBtrinkgeld() {
		return btrinkgeld;
	}

	public void setBtrinkgeld(Double btrinkgeld) {
		this.btrinkgeld = btrinkgeld;
	}

	public Double getKtrinkgeld() {
		return Ktrinkgeld;
	}

	public void setKtrinkgeld(Double ktrinkgeld) {
		Ktrinkgeld = ktrinkgeld;
	}
	
	public Double getSoldGutschein() {
		return soldGutschein != null ? soldGutschein : 0.00;
	}

	public void setSoldGutschein(Double soldGutschein) {
		this.soldGutschein = soldGutschein;
	}

	public Double getTicketGutscheinAmount() {
		return ticketGutscheinAmount;
	}

	public void setTicketGutscheinAmount(Double gutscheinAmount) {
		this.ticketGutscheinAmount = gutscheinAmount;
	}
	
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public Double getTaxType() {
		return taxType;
	}

	public void setTaxType(Double taxType) {
		this.taxType = taxType;
	}
	
	
}
