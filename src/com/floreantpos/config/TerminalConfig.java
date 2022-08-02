package com.floreantpos.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.util.PasswordHasher;

public class TerminalConfig {
	private static final String UI_DEFAULT_FONT = "ui_default_font";

	private static final String MONTH_REPORT = "MONTH_REPORT";

	private static final String SERVER_REPORT = "SERVER_REPORT";

	private static final String CALCULATOR = "CALCULATOR";

	private static final String SUPER_MARKET = "SUPERMARKET";

	private static final String NO_GUI = "NO_GUI";

	private static final String MAIN_CUSTOMER = "MAIN_CUSTOMER";
 
	private static final String BON_ROLL = "BON_ROLL";

	private static final String RESERVATION = "RESERVATION";

	private static final String AUTO_LOGOFF_TIME = "AUTO_LOGOFF_TIME";

	private static final String AUTO_LOGOFF_ENABLE = "AUTO_LOGOFF_ENABLE";

	private static final String CASH_PAYMENT_ONLY = "CASH_PAYMENT_ONLY";

	private static final String MORE_PRINT = "MORE_PRINT";

	private static final String DATE_ONLY = "DATE_ONLY";

	private static final String HIDE_DRAWER = "HIDE_DRAWER";

	private static final String ORDERS = "ORDERS";
	private static final String RABATT_DIREKT_ENABLE = "RABATT_DIREKT_ENABLE";
	private static final String LIMIT = "LIMIT";

	private static final String TICKET_ARCHIVE = "TICKET_ARCHIVE";

	private static final String TICKET_REOPEN = "TICKET_REOPEN";

	private static final String CUSTOMER_ENABLE = "CUSTOMER_ENABLE";

	private static final String HIDE_ITEM_PRICE = "HIDE_ITEM_PRICE";

	private static final String HIDE_ITEM_ID = "HIDE_ITEM_ID";

	private static final String TICKET_DELETE = "TICKET_DELETE";

	private static final String FTP_ENABLE = "FTP_ENABLE";

	private static final String DISPLAY = "DISPLAY";

	private static final String ALL_TICKETS = "ALL_TICKETS";

	private static final String HIDE_CURRENCY = "HIDE_CURRENCY";

	private static final String TICKET_O_ID = "TICKET_O_ID";

	private static final String COM = "COM";

	private static final String PFAND1 = "PFAND1";

	private static final String PFAND2 = "PFAND2";

	private static final String PFAND3 = "PFAND3";

	private static final String CD_FILE = "CD_FILE";

	private static final String CD_COM = "CD_COM";

	private static final String START_DATE = "START_DATE";

	private static final String CARD_MACHINE_COM = "CARD_MACHINE_COM";

	private static final String SALDO = "SALDO";

	private static final String RABATT_ENABLE = "RABATT_ENABLE";

	private static final String OPEN_DRAWER = "OPEN_DRAWER";

	private static final String ITEM_BARCODE = "ITEM_BARCODE";

	private static final String PREVIEW_ENABLE = "PREVIEW_ENABLE";

	private static final String SONSTIGES = "SONSTIGES";

	private static final String ITEM_SEARCH = "ITEM_SEARCH";

	private static final String CARD_ENABLE = "CARD_ENABLE";

	private static final String LOGO_ENABLE = "LOGO_ENABLE";
	
	private static final String FOOTER_MSG_ENABLE = "FOOTER_MSG_ENABLE";

	private static final String DINE_IN_ENABLE = "DINE_IN_ENABLE";

	private static final String BARCODE_ENABLE = "BARCODE_ENABLE";

	private static final String RESERVATION_ENABLE = "RESERVATION_ENABLE";

	private static final String PICK_UP_ENABLE = "PICK_UP_ENABLE";

	private static final String COMMA_PAYMENT = "COMMA_PAYMENT";

	private static final String ONLINE_BTN_ENABLE = "ONLINE_BTN_ENABLE";

	private static final String HOME_DELIVERY_ENABLE = "HOME_DELIVERY_ENABLE";

	private static final String ZVT_CARD_CMD = "ZVT_CARD_CMD";

	private static final String CATEGORY_HEIGHT = "CATEGORY_HEIGHT";

	private static final String CATEGORY_WIDTH = "CATEGORY_WIDTH";

	private static final String DEFAULT_PASS_LEN = "DEFAULT_PASS_LEN";

	private static final String CASH_DRAWER = "CASH_DRAWER";
	
	private static final String INHAVER_BELEG_BOTH = "INHAVER_BELEG_BOTH";
	private static final String INHAVER_BELEG = "INHAVER_BELEG";

	private static final String CASH_DRAWER_PRINT = "CASH_DRAWER_PRINT";

	private static final String TOUCH_FONT_SIZE = "TOUCH_FONT_SIZE";//$NON-NLS-1$

	private static final String TOUCH_BUTTON_HEIGHT = "TOUCH_BUTTON_HEIGHT";//$NON-NLS-1$

	private static final String TOUCH_ITEM_BUTTON_HEIGHT = "TOUCH_ITEM_BUTTON_HEIGHT";//$NON-NLS-1$

	private static final String TOUCH_ITEM_BUTTON_WIDTH = "TOUCH_ITEM_BUTTON_WIDTH";//$NON-NLS-1$

	private static final String BAR_TAB_ENABLE = "BarTab_enable";//$NON-NLS-1$

	private static final String GUEST_CAPTURE_ENABLE = "Enable Guest Capture";

	private static final String ISDN_ENABLE = "Start ISDN Service";

	private static final String DELIVERY_TIME = "Enable Lieferung zeit";

	private static final String ONLINE_ENABLE = "Start Online Service";

	private static final String MESSAGE_ENABLE = "Start Message Service";

	private static final String ADMIN_PASSWORD = "admin_pass";//$NON-NLS-1$

	private static final String SMS_USERNAME = "Message_Username";//$NON-NLS-1$

	private static final String SMS_PASSWORD = "Message_Password";//$NON-NLS-1$

	private static final String FTP_USERNAME = "Ftp_Username";//$NON-NLS-1$

	private static final String FTP_PASSWORD = "Ftp_Password";//$NON-NLS-1$

	private static final String FTP_SERVER = "Ftp_Server";//$NON-NLS-1$

	private static final String FTP_PATH = "Ftp_Path";//$NON-NLS-1$

	private static final String FTP_MENU_PATH = "Ftp_Menu_Path";//$NON-NLS-1$
	
	private static final String LAST_TSE_NR = "LAST_TSE_NR";//$NON-NLS-1$

	private static final String FTP_MENU_PATH_SEC = "Ftp_Menu_Path_Sec";//$NON-NLS-1$

	private static final String FTP_PATH_SEC = "Ftp_Path_SEC";//$NON-NLS-1$

	private static final String FTP_KAPATH = "Ftp_KAPath";//$NON-NLS-1$

	private static final String LIEFERKOSTEN_ENABLE = "LieferKosten";//$NON-NLS-1$

	private static final String SALES_ID = "SalesId";//$NON-NLS-1$

	private static final String MONTH_SALES_ID = "MonthSalesId";//$NON-NLS-1$
	
	private static final String YEAR_SALES_ID = "YearSalesId";//$NON-NLS-1$

	private static final String LOYALTY_NO = "LoyaltyNo";//$NON-NLS-1$

	private static final String PRINT_LIGHT = "PrintLight";//$NON-NLS-1$

	private static final String ITEM_RED = "ITEM_RED";//$NON-NLS-1$

	private static final String ITEM_GREEN = "ITEM_GREEN";//$NON-NLS-1$

	private static final String ITEM_BLUE = "ITEM_BLUE";//$NON-NLS-1$

	private static final String SHORT_RECEIPT = "SHORT_RECEIPT";//$NON-NLS-1$

	private static final String TAB_VERSION = "TAB_VERSION";//$NON-NLS-1$

	private static final String TAB_PRINT = "TAB_PRINT";//$NON-NLS-1$

	private static final String BON_NR = "BON_NR";//$NON-NLS-1$

	private static final String KITCHEN_PRINT = "KITCHEN_PRINT";//$NON-NLS-1$
	
	private static final String WINDOWS_TABLET = "WINDOWS_TABLET";
	
	private static final String RABATT_DIRECT_TEXT = "RABATT_DIRECT_TEXT";
	private static final String RABATT_MODFIER = "RABATT_MODFIER";
	private static final String TYPE = "TYPE";//$NON-NLS-1$
	private static final String CARD_TERMINAL_DISABLE = "CARD_TERMINAL_DISABLE";
	private static final String KUNDEN_DISPLAY_TEXT1 = "KUNDEN_DISPLAY_TEXT1";
	private static final String KUNDEN_DISPLAY_TEXT2 = "KUNDEN_DISPLAY_TEXT2";
	private static final String BARCODE_AND_NUMBER = "BARCODE_AND_NUMBER";
	private static final String QR_CODE = "QR_CODE";
	private static final String CLOSE_TIME = "CLOSE_TIME";
	private static final String ADD_RABATT = "ADD_RABATT";
	private static final String ONLINE_SALES = "ONLINE_SALES";
	private static final String COM_WEIGHT = "COM_WEIGHT";
	private static final String KUNDEN_SCREEN_WIDTH = "KUNDEN_SCREEN_WIDTH";
	private static final String KUNDEN_SCREEN_HEIGHT = "KUNDEN_SCREEN_HEIGHT";
	private static final String KHNA_CARD_IP = "KHNA_CARD_IP";
	private static final String KHNA_CARD_PORT = "KHNA_CARD_PORT";
	private static final String KHNA_CARD_CMD_ENABLE = "KHNA_CARD_CMD_ENABLE";
	private static final String WEIGHT_SERVER = "WEIGHT_SERVER";
	private static final String WEIGHT_CLIENT = "WEIGHT_CLIENT";
	private static final String WEIGHT_SERVER_IP = "WEIGHT_SERVER_IP";
	private static final String NUMBER_DISPLAY_WIDTH = "NUMBER_DISPLAY_WIDTH";
	private static final String connection_string="connection_string";
	private static final String NUMBER_DISPLAY_HEIGHT = "NUMBER_DISPLAY_HEIGHT";
	private static final String CUSTOM_NUMBER_DISPLAY = "CUSTOM_NUMBER_DISPLAY";
	private static final String ONLY_EC = "ONLY_EC";
	private static final String SET_SERVER = "SET_SERVER";
	private static final String SET_CLIENT = "SET_CLIENT";
	private static final String UPDATED_DESIGN = "UPDATED_DESIGN";
	private static final String WAGE_BUTTON_NAME = "WAGE_BUTTON_NAME";//$NON-NLS-1$
	private static final String WAGE_BUTTON_ENABLE = "WAGE_BUTTON_ENABLE";//$NON-NLS-1$
	private static final String INVENTUR_ALERT = "INVENTUR_ALERT";//$NON-NLS-1$
	private static final String ADD_RABATT7 = "ADD_RABATT7";
	private static final String FINANZ_PRUFUNG = "FINANZ_PRUFUNG";
	private static final String INVENTUR = "INVENTUR";//$NON-NLS-1$
	private static final String PRICE_CATEGORY = "PRICE_CATEGORY";//$NON-NLS-1$
	private static final String AUTO_TAGES_ABS = "AUTO_TAGES_ABS";//$NON-NLS-1$
	private static final String OS = "OS";
	private static final String ITEM_SORTING = "ITEM_SORTING";
	private static final String ITEM__PRICE_SORTING = "ITEM__PRICE_SORTING";

	private static final String PRICE_CATEGORY_KUNDEN = "PRICE_CATEGORY_KUNDEN";//$NON-NLS-1$
	private static final String PRICE_CATEGORY_UPATE = "PRICE_CATEGORY_UPATE";//$NON-NLS-1$	
	private static final String WHOLE_SALE = "WHOLE_SALE";//$NON-NLS-1$	
	private static final String OFFER = "OFFER";//$NON-NLS-1$
	private static final String WAITER_ALLOW_STORNO = "WAITER_ALLOW_STORNO";
	private static final String RECHNUNG_NUMMER_PRINT = "RECHNUNG_NUMMER_PRINT";//$NON-NLS-1$
	static final String TERMINAL_ID = "terminal_id"; //$NON-NLS-1$
	static final String FULLSCREEN_MODE = "fullscreen_mode"; //$NON-NLS-1$
	static final String DAILY_REPORT = "short_daily_report"; //$NON-NLS-1$
	static final String TISCH_PLAN_1 = "tisch_plan_1"; //$NON-NLS-1$
	static final String TISCH_PLAN_2 = "tisch_plan_2"; //$NON-NLS-1$
	private static final String SECOND_DISPLAY = "SecondDisplay";//$NON-NLS-1$
	private static final String KUNDEN_DISPLAY_2 = "KUNDEN_DISPLAY_2";//$NON-NLS-1$
	private static final String AUTO_RESTART = "AUTO_RESTART";//$NON-NLS-1$
	static final String POS_SPECIAL_TIME = "POS_SPECIAL_TIME"; //$NON-NLS-1$
	private static final String PRINT_NR = "PRINT_NR";
	private static final String LIFERANT_OPTION_DISPLAY = "LIFERANT_OPTION_DISPLAY";
	
	private static final String GUTSCHEIN_BALANCE_DIALOG_DISPLAY = "GUTSCHEIN_BALANCE_DIALOG_DISPLAY";
		
	static final String TSE_CLIENT_ID = "TSE_CLIENT_ID"; //$NON-NLS-1$	
	static final String TSE_ENABLE = "TSE_ENABLE"; //$NON-NLS-1$	
	static final String DUPTSE_ENABLE = "DUPTSE_ENABLE"; //$NON-NLS-1$	
	static final String BUILD_MODE = "BUILD_MODE"; //$NON-NLS-1$
	
	private static final String PAY_TRANSFER = "PAY_TRANSFER";
	private static final String DEBUG_MODE = "DEBUG_MODE";//$NON-NLS-1$
	private static final String MULTI_USER_MODE = "MULTI_USER_MODE";//$NON-NLS-1$
	private static final String MULTI_USER_POOF = "MULTI_USER_POOF";
	static final String RABATT_PIN = "Rabatt_pin"; //$NON-NLS-1$
    static final String TSE_BACKEND_DISPLAY="TSE_BACKEND_DISPLAY";
    static final String RECH_GEDRUKT_DISPLAY="RECH_GEDRUKT_DISPLAY";
    private static final String TAGES_REFRESH = "TAGES_REFRESH";
    private static final String TRINKGELD = "TRINKGELD";
    private static final String NORMAL_AUSWAL_ENABLE = "NORMAL_AUSWAL_ENABLE";
    
    private static final String DETAILED_RECIEPT="DETAILED_RECIEPT";

	private static PropertiesConfiguration config = AppConfig.getConfig();

	
	public static boolean isGutInBalanceDialog() {
		return config.getBoolean(GUTSCHEIN_BALANCE_DIALOG_DISPLAY, false);
	}

	public static void setGutInBalanceDialog(boolean enable) {
		config.setProperty(GUTSCHEIN_BALANCE_DIALOG_DISPLAY, enable);
	}
	
	public static boolean isAllowStorno() {
		return config.getBoolean(WAITER_ALLOW_STORNO, false);
	}

	public static void setAllowStorno(boolean enable) {
		config.setProperty(WAITER_ALLOW_STORNO, enable);
	}
	
	public static boolean isDetailedRecieptEnable() {
		return config.getBoolean(DETAILED_RECIEPT, false);
	}

	public static void setDetailedRecieptEnable(boolean enable) {
		config.setProperty(DETAILED_RECIEPT, enable);
	}
	
	public static boolean isNormalAuswalEnable() {
		return config.getBoolean(NORMAL_AUSWAL_ENABLE, true);
	}

	public static void setNormalAuswalEnable(boolean enable) {
		config.setProperty(NORMAL_AUSWAL_ENABLE, enable);
	}
	
	public static int getTerminalId() {
		return config.getInt(TERMINAL_ID, -1);
	}
	
	public static String getPrintnr() {
		return config.getString(PRINT_NR, "0");
	}

	public static void setPrintnr(String symbol) {
		config.setProperty(PRINT_NR, symbol);
	}

	public static void setTerminalId(int id) {
		config.setProperty(TERMINAL_ID, id);
	}

	public static boolean isGuestCaptureEnable() {
		return config.getBoolean(GUEST_CAPTURE_ENABLE, false);
	}

	public static void setCaptureGuests(boolean enable) {
		config.setProperty(GUEST_CAPTURE_ENABLE, enable);
	}

	public static boolean isLieferKostenEnable() {
		return config.getBoolean(LIEFERKOSTEN_ENABLE, false);
	}

	public static void setLieferKosten(boolean enable) {
		config.setProperty(LIEFERKOSTEN_ENABLE, enable);
	}

	public static boolean isDineInEnable() {
		return config.getBoolean(DINE_IN_ENABLE, false);
	}

	public static void setDineInEnable(boolean enable) {
		config.setProperty(DINE_IN_ENABLE, enable);
	}

	public static String getMonthSalesId() {
		return config.getString(MONTH_SALES_ID, "1");
	}

	public static void setMonthSalesId(String id) {
		config.setProperty(MONTH_SALES_ID, id);
	}
	
	public static String getYearId() {
		return config.getString(YEAR_SALES_ID, "1");
	}

	public static void setYearId(String yid) {
		config.setProperty(YEAR_SALES_ID, yid);
	}

	public static boolean isBarCodeEnable() {
		return config.getBoolean(BARCODE_ENABLE, false);
	}

	public static void setBarCodeEnable(boolean enable) {
		config.setProperty(BARCODE_ENABLE, enable);
	}

	public static boolean isReservationEnable() {
		return config.getBoolean(RESERVATION_ENABLE, false);
	}

	public static void setReservationEnable(boolean enable) {
		config.setProperty(RESERVATION_ENABLE, enable);
	}

	public static boolean isOnlineBtnEnable() {
		return config.getBoolean(ONLINE_BTN_ENABLE, false);
	}

	public static void setOnlineBtnEnable(boolean enable) {
		config.setProperty(ONLINE_BTN_ENABLE, enable);
	}

	public static boolean isPickupEnable() {
		return config.getBoolean(PICK_UP_ENABLE, false);
	}

	public static void setPickupEnable(boolean enable) {
		config.setProperty(PICK_UP_ENABLE, enable);
	}

	public static boolean isCommaPayment() {
		return config.getBoolean(COMMA_PAYMENT, false);
	}

	public static void setCommaPayment(boolean enable) {
		config.setProperty(COMMA_PAYMENT, enable);
	}

	public static boolean isHomeDeliveryEnable() {
		return config.getBoolean(HOME_DELIVERY_ENABLE, false);
	}

	public static void setHomeDeliveryEnable(boolean enable) {
		config.setProperty(HOME_DELIVERY_ENABLE, enable);
	}

	public static boolean isIsdnEnabled() {
		return config.getBoolean(ISDN_ENABLE, false);
	}

	public static void setIsdn(boolean enable) {
		config.setProperty(ISDN_ENABLE, enable);
	}

	public static boolean isLogoEnabled() {
		return config.getBoolean(LOGO_ENABLE, false);
	}

	public static void setLogo(boolean enable) {
		config.setProperty(LOGO_ENABLE, enable);
	}

	public static boolean isFooterMsgEnabled() {
		return config.getBoolean(FOOTER_MSG_ENABLE, false);
	}

	public static void setFooterMsgEnabled(boolean enable) {
		config.setProperty(FOOTER_MSG_ENABLE, enable);
	}
	
	public static boolean isTabPrint() {
		return config.getBoolean(TAB_PRINT, false);
	}

	public static void setTabPrint(boolean enable) {
		config.setProperty(TAB_PRINT, enable);
	}

	public static String getBonNr() {
		return config.getString(BON_NR, "1");
	}

	public static void setBonNr(String nr) {
		config.setProperty(BON_NR, nr);
	}

	public static boolean isCashDrawer() {
		return config.getBoolean(CASH_DRAWER, false);
	}

	public static void setCashDrawer(boolean enable) {
		config.setProperty(CASH_DRAWER, enable);
	}

	public static Boolean isNoGUI() {
		return config.getBoolean(NO_GUI, false);
	}

	public static void setNoGUI(boolean enable) {
		config.setProperty(NO_GUI, enable);
	}

	public static boolean isCashDrawerPrint() {
		return config.getBoolean(CASH_DRAWER_PRINT, false);
	}

	public static void setCashDrawerPrint(boolean enable) {
		config.setProperty(CASH_DRAWER_PRINT, enable);
	}

	public static boolean isCashPaymentOnly() {
		return config.getBoolean(CASH_PAYMENT_ONLY, false);
	}

	public static void setCashPaymentOnly(boolean enable) {
		config.setProperty(CASH_PAYMENT_ONLY, enable);
	}
	
	public static boolean isTRINKGELD() {
		return config.getBoolean(TRINKGELD, false);
	}

	public static void setTRINKGELD(boolean enable) {
		config.setProperty(TRINKGELD, enable);
	}
	
	public static boolean isMultipleBon() {
		return config.getBoolean(MORE_PRINT, false);
	}

	public static void setMultipleBon(boolean enable) {
		config.setProperty(MORE_PRINT, enable);
	}

	public static boolean isDateOnly() {
		return config.getBoolean(DATE_ONLY, false);
	}

	public static void setDateOnly(boolean enable) {
		config.setProperty(DATE_ONLY, enable);
	}

	public static boolean isHideDrawer() {
		return config.getBoolean(HIDE_DRAWER, false);
	}

	public static void setHideDrawer(boolean enable) {
		config.setProperty(HIDE_DRAWER, enable);
	}


	public static boolean isOnlineEnabled() {
		return config.getBoolean(ONLINE_ENABLE, false);
	}

	public static void setOnline(boolean enable) {
		config.setProperty(ONLINE_ENABLE, enable);
	}

	public static boolean isMessageEnabled() {
		return config.getBoolean(MESSAGE_ENABLE, false);
	}

	public static void setMessage(boolean enable) {
		config.setProperty(MESSAGE_ENABLE, enable);
	}

	public static String getZvtCardPayment(){
		return config.getString(ZVT_CARD_CMD,"");
	}
	public static void setZvtCardPayment(String cmd){
		config.setProperty(ZVT_CARD_CMD, cmd);
	}

	//added by jyoti
	public static boolean isCardTerminalDisable() {
		return config.getBoolean(CARD_TERMINAL_DISABLE, false);
	}

	public static void setTerminalDisable(boolean enable) {
		config.setProperty(CARD_TERMINAL_DISABLE, enable);
	}


	public static boolean isRechnungNummerPrintEnable() {
		return config.getBoolean(RECHNUNG_NUMMER_PRINT, false);
	}

	public static void setRechnungNummerPrintEnable(boolean enable) {
		config.setProperty(RECHNUNG_NUMMER_PRINT, enable);
	}
	public static String getKundenDisplayText1(){
		return config.getString(KUNDEN_DISPLAY_TEXT1,"     WILLKOMMEN !!!");
	}
	public static void setKundenDisplayText1(String cmd){
		config.setProperty(KUNDEN_DISPLAY_TEXT1, cmd);
	}
	public static String getKundenDisplayText2(){
		return config.getString(KUNDEN_DISPLAY_TEXT2,"KHANA KASSENSYSTEME");
	}
	public static void setKundenDisplayText2(String cmd){
		config.setProperty(KUNDEN_DISPLAY_TEXT2, cmd);
	}
	
	public static String getKundenScreenWidth(){
		return config.getString(KUNDEN_SCREEN_WIDTH,"600");
	}
	public static void setKundenScreenWidth(String cmd){
		config.setProperty(KUNDEN_SCREEN_WIDTH, cmd);
	}
	public static String getKundenScreenHeight(){
		return config.getString(KUNDEN_SCREEN_HEIGHT,"600");
	}
	public static void setKundenScreenHeight(String cmd){
		config.setProperty(KUNDEN_SCREEN_HEIGHT, cmd);
	}
	

	public static boolean isBothInput() {
		return config.getBoolean(BARCODE_AND_NUMBER, false);
	}

	public static void setBothInput(boolean enable) {
		config.setProperty(BARCODE_AND_NUMBER, enable);
	}

	public static boolean isQRcode() {
		return config.getBoolean(QR_CODE, false);
	}

	public static void setQRcode(boolean enable) {
		config.setProperty(QR_CODE, enable);
	}
	
	public static int getCloseHour() {
		return config.getInt(CLOSE_TIME, 23);
	}

	public static void setCloseHour(int id) {
		config.setProperty(CLOSE_TIME, id);
	}
	public static boolean isAddRabattAt19() {
		return config.getBoolean(ADD_RABATT, false);
	}

	public static void setAddRabattAt19(boolean enable) {
		config.setProperty(ADD_RABATT, enable);
	}
	
	public static boolean isAddRabattAt7() {
		return config.getBoolean(ADD_RABATT7, false);
	}

	public static void setAddRabattAt7(boolean enable) {
		config.setProperty(ADD_RABATT7, enable);
	}
	
	public static boolean isOnlineSalesStart() {
		return config.getBoolean(ONLINE_SALES, false);
	}

	public static void setOnlineSalesStart(boolean enable) {
		config.setProperty(ONLINE_SALES, enable);
	}
	
	
	public static String getSDNummer() {
		return config.getString(SECOND_DISPLAY);
	}

	public static void setSDNummer(String sdn) {
		config.setProperty(SECOND_DISPLAY, sdn);
	}
	
	public static boolean isKundenScreen() {
		return config.getBoolean(KUNDEN_DISPLAY_2, false);
	}

	public static void setKundenScreen(boolean enable) {
		config.setProperty(KUNDEN_DISPLAY_2, enable);
	}

	
	public static String getKhanaCardIp() {
		return config.getString(KHNA_CARD_IP, "192.168.0.11");
	}

	public static void setKhanaCardIp(String cmd) {
		config.setProperty(KHNA_CARD_IP, cmd);
	}
	public static String getKhanaCardPort() {
		return config.getString(KHNA_CARD_PORT, "");
	}

	public static void setKhanaCardPort(String cmd) {
		config.setProperty(KHNA_CARD_PORT, cmd);
	}


	public static boolean isKhanaZvtEnable() {
		return config.getBoolean(KHNA_CARD_CMD_ENABLE, false);
	}

	public static void setKhanaZvtEnable(boolean enable) {
		config.setProperty(KHNA_CARD_CMD_ENABLE, enable);
	}
	
	public static boolean isWeightServer() {
		return config.getBoolean(WEIGHT_SERVER, false);
	}

	public static void seteWeightServer(boolean enable) {
		config.setProperty(WEIGHT_SERVER, enable);
	}
	
	public static boolean isWeightClient() {
		return config.getBoolean(WEIGHT_CLIENT, false);
	}

	public static void seteWeightClient(boolean enable) {
		config.setProperty(WEIGHT_CLIENT, enable);
	}
	
	public static String getWeightServerIp() {
		return config.getString(WEIGHT_SERVER_IP, "192.168.0.25");
	}

	public static void setWeightServerIp(String cmd) {
		config.setProperty(WEIGHT_SERVER_IP, cmd);
	}
	
	private static final String CASH_BOOK = "CASH_BOOK";
	private static final String CASH_BOOK_TEXT = "CASH_BOOK_TEXT";

	public static Boolean isCashBookEnable() {
		return config.getBoolean(CASH_BOOK, false);
	}

	public static void setCashBookEnable(boolean enable) {
		config.setProperty(CASH_BOOK, enable);
	}
	
	public static String getCashbookText() {
		return config.getString(CASH_BOOK_TEXT, "KASSENBOOK");
	}

	public static void setCashbookText(String cmd) {
		config.setProperty(CASH_BOOK_TEXT, cmd);
	}
	
	
	private static final String ADIMAT_COM = "ADIMAT_COM";
	private static final String ADIMAT_ENABLE = "ADIMAT_ENABLE";
	private static final String KEY_TYPE = "KEY_TYPE";

	public static String getAdimatCom() {
		return config.getString(ADIMAT_COM, "COM8");
	}

	public static void setAdimatCom(String pin) {
		config.setProperty(ADIMAT_COM, pin);
	}	

	
	public static boolean isAdimatComEnable() {
		return config.getBoolean(ADIMAT_ENABLE, false);
	}

	public static void setAdimatComEnable(boolean enable) {
		config.setProperty(ADIMAT_ENABLE, enable);
	}
	public static String getKeyType(){
		return config.getString(KEY_TYPE,"ibutton");
	}
	public static void setKeyType(String cmd){
		config.setProperty(KEY_TYPE, cmd);
	}
	
	public static boolean isFinanzPrufung() {
		return config.getBoolean(FINANZ_PRUFUNG, false);
	}

	public static void setFinanzPrufung(boolean enable) {
		config.setProperty(FINANZ_PRUFUNG, enable);
	}
	
	private static final String TAKE_ITEM_COUNT = "TAKE_ITEM_COUNT";

	public static boolean isTakeItemCount() {
		return config.getBoolean(TAKE_ITEM_COUNT, true);
	}

	public static void setTakeItemCount(boolean enable) {
		config.setProperty(TAKE_ITEM_COUNT, enable);
	}
	
	private static final String SUPER_ADMIN = "SUPER_ADMIN";

	private static final String SUPER_ADMIN_PASSWORD = "SUPER_ADMIN_PASSWORD";
	
	
	public static void setSuperAdminPassword(String superAdminPassword) {
		config.setProperty(SUPER_ADMIN_PASSWORD, superAdminPassword);
	}

	public static String getSuperAdminPassword() {
		return config.getString(SUPER_ADMIN_PASSWORD, "1937");
	}	

	public static void setSuperAdmin(boolean superAdmin) {
		config.setProperty(SUPER_ADMIN, superAdmin);
	}

	public static Boolean isSuperAdmin() {
		return config.getBoolean(SUPER_ADMIN, true);
	}	
	
	public static String getNumberDisplayWidth(){
		return config.getString(NUMBER_DISPLAY_WIDTH,"450");
	}
	public static void setNumberDisplayWidth(String cmd){
		config.setProperty(NUMBER_DISPLAY_WIDTH, cmd);
	}
	
	public static String getDatabaseInfo() {
		return config.getString(connection_string, "");
	}
	
	public static String getNumberDisplayHeight(){
		return config.getString(NUMBER_DISPLAY_HEIGHT,"300");
	}
	public static void setNumberDisplayHeight(String cmd){
		config.setProperty(NUMBER_DISPLAY_HEIGHT, cmd);
	}
	

	public static boolean isCustomNumberDisplay() {
		return config.getBoolean(CUSTOM_NUMBER_DISPLAY, false);
	}

	public static void setCustomNumberDisplay(boolean enable) {
		config.setProperty(CUSTOM_NUMBER_DISPLAY, enable);
	}

	public static boolean isOnlyEC() {
		return config.getBoolean(ONLY_EC, false);
	}

	public static void setOnlyEC(boolean enable) {
		config.setProperty(ONLY_EC, enable);
	}
	
	public static boolean isKhanaServer() {
		return config.getBoolean(SET_SERVER, false);
	}

	public static void setKhanaServer(boolean enable) {
		config.setProperty(SET_SERVER, enable);
	}
	
	public static boolean isKhanaClient() {
		return config.getBoolean(SET_CLIENT, false);
	}

	public static void setKhanaClient(boolean enable) {
		config.setProperty(SET_CLIENT, enable);
	}
	
	public static String getTseBackendDisplay() {
		return TSE_BACKEND_DISPLAY;
	}
	
	public static boolean isDupTseBackendDispaly() {
		return config.getBoolean(TSE_BACKEND_DISPLAY, false);
	}
	
	public static void setDupTseBackendDispaly(boolean enable) {
		config.setProperty(TSE_BACKEND_DISPLAY, enable);
	}
	
	public static boolean isInhaberBeleg() {
		return config.getBoolean(INHAVER_BELEG, false);
	}

	public static void setInhaberBeleg(boolean enable) {
		config.setProperty(INHAVER_BELEG, enable);
	}
	
	public static boolean isInhaberBelegBoth() {
		return config.getBoolean(INHAVER_BELEG_BOTH, false);
	}

	public static void setInhaberBelegBoth(boolean enable) {
		config.setProperty(INHAVER_BELEG_BOTH, enable);
	}
	
	public static boolean isUpdatedDesign() {
		return config.getBoolean(UPDATED_DESIGN, true);
	}

	public static void setUpdatedDesign(boolean enable) {
		config.setProperty(UPDATED_DESIGN, enable);
	}
	
	public static void setWageSonsName(String com) {
		config.setProperty(WAGE_BUTTON_NAME, com);
	}
	
	public static String getWageSonsName(){
		return config.getString(WAGE_BUTTON_NAME,"WAGE_SONS");
	}

	public static boolean isWageSons() {
		return config.getBoolean(WAGE_BUTTON_ENABLE, false);
	}

	public static void setWageSons(boolean enable) {
		config.setProperty(WAGE_BUTTON_ENABLE, enable);
	}
	
	public static boolean isInventurAlert() {
		return config.getBoolean(INVENTUR_ALERT, false);
	}

	public static void setInventurAlert(boolean enable) {
		config.setProperty(INVENTUR_ALERT, enable);
	}
	
	public static boolean isInventur() {
		return config.getBoolean(INVENTUR, false);
	}

	public static void setInventur(boolean enable) {
		config.setProperty(INVENTUR, enable);
	}
	
	public static boolean isPriceCategory() {
		return config.getBoolean(PRICE_CATEGORY, false);
	}

	public static void setPriceCategory(boolean enable) {
		config.setProperty(PRICE_CATEGORY, enable);
	}
	
	public static boolean isAutoTagesAbs() {
		return config.getBoolean(AUTO_TAGES_ABS, false);
	}

	public static void setAutoTagesAbs(boolean enable) {
		config.setProperty(AUTO_TAGES_ABS, enable);
	}
	
	public static boolean isPriceCategoryKunden() {
		return config.getBoolean(PRICE_CATEGORY_KUNDEN, false);
	}

	public static void setPriceCategoryKunden(boolean enable) {
		config.setProperty(PRICE_CATEGORY_KUNDEN, enable);
	}
	
	public static boolean isUpdatePriceCategory() {
		return config.getBoolean(PRICE_CATEGORY_UPATE, false);
	}

	public static void setUpdatePriceCategory(boolean enable) {
		config.setProperty(PRICE_CATEGORY_UPATE, enable);
	}
	
	public static boolean isWholeSale() {
		return config.getBoolean(WHOLE_SALE, false);
	}

	public static void setWholeSale(boolean enable) {
		config.setProperty(WHOLE_SALE, enable);
	}
	
	public static boolean isOffer() {
		return config.getBoolean(OFFER, false);
	}

	public static void setOffer(boolean enable) {
		config.setProperty(OFFER, enable);
	}

	public static boolean isAutoRestartSystem() {
		return config.getBoolean(AUTO_RESTART, false);
	}

	public static void setAutoRestartSystem(boolean enable) {
		config.setProperty(AUTO_RESTART, enable);
	}
	
	public static boolean isSpecial() {
		return config.getBoolean(POS_SPECIAL_TIME, false);
	}

	public static void setSpecial(boolean enable) {
		config.setProperty(POS_SPECIAL_TIME, enable);
	}

	//till Here

	public static boolean isDisplay() {
		return config.getBoolean(DISPLAY, false);
	}

	public static void setDisplay(boolean enable) {
		config.setProperty(DISPLAY, enable);
	}

	public static boolean isAllTickets() {
		return config.getBoolean(ALL_TICKETS, false);
	}

	public static void setAllTickets(boolean enable) {
		config.setProperty(ALL_TICKETS, enable);
	}

	public static boolean isHideCurrency() {
		return config.getBoolean(HIDE_CURRENCY, false);
	}

	public static void setHideCurrency(boolean enable) {
		config.setProperty(HIDE_CURRENCY, enable);
	}

	public static boolean isTicketOId() {
		return config.getBoolean(TICKET_O_ID, false);
	}

	public static void setTicketOId(boolean enable) {
		config.setProperty(TICKET_O_ID, enable);
	}

	public static String getCom() {
		return config.getString(COM, "COM1");
	}

	public static void setCom(String com) {
		config.setProperty(COM, com);
	}
	
	public static String getComWeight() {
		return config.getString(COM_WEIGHT, "COM9");
	}

	public static void setComWeight(String com) {
		config.setProperty(COM_WEIGHT, com);
	}

	public static String getPfand1() {
		return config.getString(PFAND1, "Pfand1");
	}

	public static void setPfand1(String pfand1) {
		config.setProperty(PFAND1, pfand1);
	}

	public static String getPfand2() {
		return config.getString(PFAND2, "Pfand2");
	}

	public static void setPfand2(String pfand2) {
		config.setProperty(PFAND2, pfand2);
	}

	public static String getPfand3() {
		return config.getString(PFAND3, "Pfand3");
	}

	public static void setPfand3(String pfand3) {
		config.setProperty(PFAND3, pfand3);
	}

	public static String getCashDrawerFile() {
		return config.getString(CD_FILE, "");
	}

	public static void setCashDrawerFile(String file) {
		config.setProperty(CD_FILE, file);
	}

	public static String getCashDrawerCom() {
		return config.getString(CD_COM, "");
	}

	public static void setCashDrawerCom(String com) {
		config.setProperty(CD_COM, com);
	}

	public static String getCardMachineCom() {
		return config.getString(CARD_MACHINE_COM, "");
	}

	public static void setCardMachineCom(String com) {
		config.setProperty(CARD_MACHINE_COM, com);
	}

	public static boolean isFtpEnabled() {
		return config.getBoolean(FTP_ENABLE, false);
	}

	public static void setFtp(boolean enable) {
		config.setProperty(FTP_ENABLE, enable);
	}

	public static void setBarTabEnable(boolean enable) {
		config.setProperty(BAR_TAB_ENABLE, enable);
	}

	public static boolean isFullscreenMode() {
		return config.getBoolean(FULLSCREEN_MODE, false);
	}

	public static void setFullscreenMode(boolean fullscreen) {
		config.setProperty(FULLSCREEN_MODE, fullscreen);
	}

	public static boolean isDailyReport() {
		return config.getBoolean(DAILY_REPORT, false);
	}

	public static void setDailyReport(boolean report) {
		config.setProperty(DAILY_REPORT, report);
	}

	public static String getRabattPin() {
		return config.getString(RABATT_PIN, "1005");
	}

	public static void setRabattPin(String pin) {
		config.setProperty(RABATT_PIN, pin);
	}
	
	public static boolean isKitchenPrint() {
		return config.getBoolean(KITCHEN_PRINT, false);
	}

	public static void setKitchenPrint(boolean print) {
		config.setProperty(KITCHEN_PRINT, print);
	}

	public static boolean isPlan1() {
		return config.getBoolean(TISCH_PLAN_1, false);
	}

	public static void setStartDate(String date) {
		config.setProperty(START_DATE, date);
	}

	public static String getStartDate() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return config.getString(START_DATE, df.format(new Date()));
	}

	public static void setPlan1(boolean plan) {
		config.setProperty(TISCH_PLAN_1, plan);
	}

	public static boolean isPlan2() {
		return config.getBoolean(TISCH_PLAN_2, false);
	}

	public static void setPlan2(boolean plan) {
		config.setProperty(TISCH_PLAN_2, plan);
	}

	public static boolean isPrintLight() {
		return config.getBoolean(PRINT_LIGHT, false);
	}

	public static void setPrintLight(boolean print) {
		config.setProperty(PRINT_LIGHT, print);
	}

	public static boolean isDeliveryTime() {
		return config.getBoolean(DELIVERY_TIME, false);
	}

	public static void setDeliveryTime(boolean time) {
		config.setProperty(DELIVERY_TIME, time);
	}

	public static boolean isShortReceipt() {
		return config.getBoolean(SHORT_RECEIPT, false);
	}

	public static void setShortReceipt(boolean receipt) {
		config.setProperty(SHORT_RECEIPT, receipt);
	}

	public static boolean isTabVersion() {
		return config.getBoolean(TAB_VERSION, false);
	}

	public static void setTabVersion(boolean tab) {
		config.setProperty(TAB_VERSION, tab);
	}

	public static boolean istype() {
		return config.getBoolean(TYPE, false);
	}

	public static void setType(boolean type) {
		config.setProperty(TYPE, type);
	}

	public static String getCategoryHeight() {
		return config.getString(CATEGORY_HEIGHT, "100");
	}

	public static void setCategoryHeight(String height) {
		config.setProperty(CATEGORY_HEIGHT, height);
	}

	public static String getCategoryWidth() {
		return config.getString(CATEGORY_WIDTH, "135");
	}

	public static void setCategoryWidth(String width) {
		config.setProperty(CATEGORY_WIDTH, width);
	}

	public static boolean isTagesAbsRefresh() {
		return config.getBoolean(TAGES_REFRESH, false);
	}
	
	public static void setTagesAbsRefresh(boolean enable) {
		config.setProperty(TAGES_REFRESH, enable);
	}
	
	public static String getAdminPassword() {
		return config
				.getString(ADMIN_PASSWORD, PasswordHasher.hashPassword("1111"));
	}

	public static void setAdminPassword(String password) {
		config.setProperty(ADMIN_PASSWORD, PasswordHasher.hashPassword(password));
	}

	public static boolean isPreviewEnable() {
		return config.getBoolean(PREVIEW_ENABLE, true);
	}

	public static void setPreviewEnable(boolean enable) {
		config.setProperty(PREVIEW_ENABLE, enable);
	}

	public static boolean isOpenDrawer() {
		return config.getBoolean(OPEN_DRAWER, true);
	}

	public static void setOpenDrawer(boolean enable) {
		config.setProperty(OPEN_DRAWER, enable);
	}

	public static boolean isSontiges() {
		return config.getBoolean(SONSTIGES, false);
	}

	public static void setSonstiges(boolean enable) {
		config.setProperty(SONSTIGES, enable);
	}

	public static boolean isItemSearch() {
		return config.getBoolean(ITEM_SEARCH, false);
	}

	public static void setItemSearch(boolean enable) {
		config.setProperty(ITEM_SEARCH, enable);
	}

	public static boolean isOrders() {
		return config.getBoolean(ORDERS, false);
	}

	public static void setOrders(boolean enable) {
		config.setProperty(ORDERS, enable);
	}

	public static boolean isSaldo() {
		return config.getBoolean(SALDO, false);
	}

	public static void setSaldo(boolean enable) {
		config.setProperty(SALDO, enable);
	}

	public static boolean isTicketDelete() {
		return config.getBoolean(TICKET_DELETE, false);
	}

	public static void setTicketDelete(boolean enable) {
		config.setProperty(TICKET_DELETE, enable);
	}

	public static boolean isTicketArchive() {
		return config.getBoolean(TICKET_ARCHIVE, false);
	}

	public static void setTicketArchive(boolean enable) {
		config.setProperty(TICKET_ARCHIVE, enable);
	}

	public static boolean isTicketReopen() {
		return config.getBoolean(TICKET_REOPEN, false);
	}

	public static void setTicketReopen(boolean enable) {
		config.setProperty(TICKET_REOPEN, enable);
	}

	public static boolean isCustomer() {
		return config.getBoolean(CUSTOMER_ENABLE, false);
	}

	public static void setCustomer(boolean enable) {
		config.setProperty(CUSTOMER_ENABLE, enable);
	}

	public static boolean isHideItemPrice() {
		return config.getBoolean(HIDE_ITEM_PRICE, false);
	}

	public static void setHideItemPrice(boolean enable) {
		config.setProperty(HIDE_ITEM_PRICE, enable);
	}

	public static boolean isHideItemId() {
		return config.getBoolean(HIDE_ITEM_ID, false);
	}

	public static void setHideItemId(boolean enable) {
		config.setProperty(HIDE_ITEM_ID, enable);
	}

	public static boolean isItemBarcode() {
		return config.getBoolean(ITEM_BARCODE, false);
	}

	public static void setItemBarcode(boolean enable) {
		config.setProperty(ITEM_BARCODE, enable);
	}

	public static boolean isRabattEnable() {
		return config.getBoolean(RABATT_ENABLE, false);
	}

	public static void setRabattEnable(boolean enable) {
		config.setProperty(RABATT_ENABLE, enable);
	}

	public static boolean isRabattDirektEnable() {
		return config.getBoolean(RABATT_DIREKT_ENABLE, false);
	}

	public static void setRabattDirektEnable(boolean enable) {
		config.setProperty(RABATT_DIREKT_ENABLE, enable);
	}
	
	public static boolean isWindowsTablet() {
		return config.getBoolean(WINDOWS_TABLET, false);
	}

	public static void setWindowsTablet(boolean enable) {
		config.setProperty(WINDOWS_TABLET, enable);
	}
	
	public static boolean isIncludeRabattModifier(){
		return config.getBoolean(RABATT_MODFIER, true);
	}

	public static void setIncludeRabattModifier(boolean enable) {
		config.setProperty(RABATT_MODFIER, enable);
	}	
	
	public static boolean isCardEnable() {
		return config.getBoolean(CARD_ENABLE, false);
	}

	public static void setCardEnable(boolean enable) {
		config.setProperty(CARD_ENABLE, enable);
	}

	public static String getMessageUserName() {
		return config.getString(SMS_USERNAME, "Administrator");
	}

	public static void setMessageUserName(String username) {
		config.setProperty(SMS_USERNAME, username);
	}

	public static String getSalesId() {
		return config.getString(SALES_ID);
	}

	public static void setSalesId(String id) {
		config.setProperty(SALES_ID, id);
	}

	public static int getItemRed() {
		return config.getInt(ITEM_RED, 102);
	}

	public static void setItemRed(int red) {
		config.setProperty(ITEM_RED, red);
	}

	public static int getItemGreen() {
		return config.getInt(ITEM_GREEN, 255);
	}

	public static void setItemGreen(int green) {
		config.setProperty(ITEM_GREEN, green);
	}

	public static int getItemBlue() {
		return config.getInt(ITEM_BLUE, 102);
	}

	public static void setItemBlue(int blue) {
		config.setProperty(ITEM_BLUE, blue);
	}

	public static String getLoyaltyNo() {
		return config.getString(LOYALTY_NO);
	}

	public static void setLoyaltyNo(String id) {
		config.setProperty(LOYALTY_NO, id);
	}

	public static String getLimit() {
		return config.getString(LIMIT, "1000");
	}

	public static void setLimit(String id) {
		config.setProperty(LIMIT, id);
	}

	public static String getFtpUserName() {
		return config.getString(FTP_USERNAME, "Administrator");
	}

	public static void setFtpUserName(String username) {
		config.setProperty(FTP_USERNAME, username);
	}

	public static String getMessagePassword() {
		return config.getString(SMS_PASSWORD);
	}

	public static void setMessagePassword(String password) {
		config.setProperty(SMS_PASSWORD, password);
	}

	public static String getFtpPassword() {
		return config.getString(FTP_PASSWORD);
	}

	public static void setFtpPassword(String password) {
		config.setProperty(FTP_PASSWORD, password);
	}

	public static String getFtpServer() {
		return config.getString(FTP_SERVER);
	}

	public static void setFtpServer(String server) {
		config.setProperty(FTP_SERVER, server);
	}

	public static String getFtpPath() {
		return config.getString(FTP_PATH);
	}

	public static void setFtpPath(String path) {
		config.setProperty(FTP_PATH, path);
	}
	
	public static String getLastTseNr() {
		return config.getString(LAST_TSE_NR);
	}

	public static void setLastTseNr(String number) {
		config.setProperty(LAST_TSE_NR, number);
	}
	
	public static String getFtpMenuPath() {
		return config.getString(FTP_MENU_PATH);
	}

	public static void setFtpMenuPath(String path) {
		config.setProperty(FTP_MENU_PATH, path);
	}

	public static String getFtpMenuPathSec() {
		return config.getString(FTP_MENU_PATH_SEC);
	}

	public static void setFtpMenuPathSec(String path) {
		config.setProperty(FTP_MENU_PATH_SEC, path);
	}

	public static String getFtpPathSec() {
		return config.getString(FTP_PATH_SEC);
	}

	public static void setFtpPathSec(String path) {
		config.setProperty(FTP_PATH_SEC, path);
	}

	public static String getFtpKAPath() {
		return config.getString(FTP_KAPATH);
	}

	public static void setFtpKAPath(String path) {
		config.setProperty(FTP_KAPATH, path);
	}

	public static boolean matchAdminPassword(String password) {
		return getAdminPassword().equals(PasswordHasher.hashPassword(password));
	}

	public static void setTouchScreenButtonHeight(int height) {
		config.setProperty(TOUCH_BUTTON_HEIGHT, height);
	}

	public static int getTouchScreenButtonHeight() {
		return config.getInt(TOUCH_BUTTON_HEIGHT, 45);
	}

	public static void setTouchScreenItemButtonHeight(int height) {
		config.setProperty(TOUCH_ITEM_BUTTON_HEIGHT, height);
	}

	public static int getTouchScreenItemButtonHeight() {
		return config.getInt(TOUCH_ITEM_BUTTON_HEIGHT, 50);
	}

	public static void setTouchScreenItemButtonWidth(int size) {
		config.setProperty(TOUCH_ITEM_BUTTON_WIDTH, size);
	}

	public static int getTouchScreenItemButtonWidth() {
		return config.getInt(TOUCH_ITEM_BUTTON_WIDTH, 80);
	}

	public static void setTouchScreenFontSize(int size) {
		config.setProperty(TOUCH_FONT_SIZE, size);
	}

	public static int getTouchScreenFontSize() {
		return config.getInt(TOUCH_FONT_SIZE, 12);
	}

	public static void setDefaultPassLen(int defaultPassLen) {
		config.setProperty(DEFAULT_PASS_LEN, defaultPassLen);
	}

	public static int getDefaultPassLen() {
		return config.getInt(DEFAULT_PASS_LEN, 4);
	}

	public static boolean isAutoLogoffEnable() {
		return config.getBoolean(AUTO_LOGOFF_ENABLE, true);
	}

	public static void setAutoLogoffEnable(boolean enable) {
		config.setProperty(AUTO_LOGOFF_ENABLE, enable);
	}

	public static void setAutoLogoffTime(int time) {
		config.setProperty(AUTO_LOGOFF_TIME, time);
	}

	public static int getAutoLogoffTime() {
		return config.getInt(AUTO_LOGOFF_TIME, 60);
	}

	public static String getUiDefaultFont() {
		return config.getString(UI_DEFAULT_FONT);
	}

	public static void setUiDefaultFont(String fontName) {
		config.setProperty(UI_DEFAULT_FONT, fontName);
	}

	public static boolean isCalculator() {
		return config.getBoolean(CALCULATOR, false);
	}

	public static void setCalculator(boolean enable) {
		config.setProperty(CALCULATOR, enable);
	}

	public static boolean isSupermarket() {
		return config.getBoolean(SUPER_MARKET, true);
	}

	public static void setSuperMarket(boolean enable) {
		config.setProperty(SUPER_MARKET, enable);
	}

	public static boolean isMonthReport() {
		return config.getBoolean(MONTH_REPORT, false);
	}

	public static void setMonthReport(boolean enable) {
		config.setProperty(MONTH_REPORT, enable);
	}

	public static boolean isServerReport() {
		return config.getBoolean(SERVER_REPORT, false);
	}

	public static void setServerReport(boolean enable) {
		config.setProperty(SERVER_REPORT, enable);
	}

	public static boolean isReservation() {
		return config.getBoolean(RESERVATION, false);
	}

	public static void setReservation(boolean enable) {
		config.setProperty(RESERVATION, enable);
	}

	public static boolean isMainCustomerButton() {
		return config.getBoolean(MAIN_CUSTOMER, false);
	}

	public static void setMainCustomerButton(boolean enable) {
		config.setProperty(MAIN_CUSTOMER, enable);
	}

	public static boolean isBonRoll() {
		return config.getBoolean(BON_ROLL, false);
	}

	public static void setBonRoll(boolean enable) {
		config.setProperty(BON_ROLL, enable);
	}
	
	public static void setOs(String os) {
		config.setProperty(OS, os);
	}

	public static String getOs() {
		return config.getString(OS, "Windows");
	}
	
	public static boolean isItemSorting() {
		return config.getBoolean(ITEM_SORTING, false);
	}

	public static void setItemSorting(boolean enable) {
		config.setProperty(ITEM_SORTING, enable);
	}
	
	public static boolean isItemPriceSorting() {
		return config.getBoolean(ITEM__PRICE_SORTING, false);
	}

	public static void setItemPriceSorting(boolean enable) {
		config.setProperty(ITEM__PRICE_SORTING, enable);
	}
	
	public static String getTseClientId(){
		return config.getString(TSE_CLIENT_ID,"");
	}
	public static void setTseClientId(String cmd){
		config.setProperty(TSE_CLIENT_ID, cmd);
	}
	
	public static boolean isTseEnable() {
		return config.getBoolean(TSE_ENABLE, false);
	}
	public static void setTseEnable(boolean enable) {
		config.setProperty(TSE_ENABLE, enable);
	}
	
	/*public static boolean isDupTseEnable() {
		return config.getBoolean(DUPTSE_ENABLE, true);
	}
	public static void setDupTseEnable(boolean enable) {
		config.setProperty(DUPTSE_ENABLE, enable);
	}*/
	
	static final String TSE_CONTACTED = "TSE_CONTACTED"; //$NON-NLS-1$	

	public static boolean isTseContacted() {
		return config.getBoolean(TSE_CONTACTED, false);
	}
	public static void setTseContacted(boolean enable) {
		config.setProperty(TSE_CONTACTED, enable);
	}
	
	private static final String TSE_TIER3 = "TSE_TIER3";

	
	public static boolean isTseTier3() {
		return config.getBoolean(TSE_TIER3, true);
	}
	public static void setTseTier3(boolean enable) {
		config.setProperty(TSE_TIER3, enable);
	}	
	
	
	static final String USB_CERT = "USB_CERT"; //$NON-NLS-1$
	public static boolean isUsbCert() {
		return config.getBoolean(USB_CERT, false);
	}
	
	public static void setUsbCert(boolean enable) {
		config.setProperty(USB_CERT, enable);
	}	
	
	
	public static boolean isBuildMode() {
		return config.getBoolean(BUILD_MODE, false);
	}
	public static void setBuildMode(boolean enable) {
		config.setProperty(BUILD_MODE, enable);
	}
	
	public static boolean isPayTransfer() {
		return config.getBoolean(PAY_TRANSFER, false);
	}
	public static void setPayTransfer(boolean enable) {
		config.setProperty(PAY_TRANSFER, enable);
	}
	
	public static String getRabattDirectText() {
		return config.getString(RABATT_DIRECT_TEXT, "Rabatt Direkt");
	}

	public static void setRabattDirectText(String rabattDirectText) {
		config.setProperty(RABATT_DIRECT_TEXT, rabattDirectText);
	}
	
	public static void setDebugMode(boolean debugMode) {
		config.setProperty(DEBUG_MODE, debugMode);
	}

	public static Boolean isDebugMode() {
		return config.getBoolean(DEBUG_MODE, false);
	}
	static final String KITCHEN_LABLE = "KITCHEN_LABLE"; //$NON-NLS-1$

	public static void setKitchenLablePrint(boolean enable) {
		config.setProperty(KITCHEN_LABLE, enable);
	}
	public static boolean isKitchenLablePrint() {
		return config.getBoolean(KITCHEN_LABLE, false);
	}
	
	static final String PRODUCT_LABLE_BIG = "PRODUCT_LABLE_BIG"; //$NON-NLS-1$

	public static void setProductLablePrintBig(boolean enable) {
		config.setProperty(PRODUCT_LABLE_BIG, enable);
	}
	public static boolean isProductLablePrintBig() {
		return config.getBoolean(PRODUCT_LABLE_BIG, false);
	}
	
	public static void setMultiUser(boolean multi) {
		config.setProperty(MULTI_USER_MODE, multi);
	}

	public static Boolean isMultiUser() {
		return config.getBoolean(MULTI_USER_MODE, false);
	}
	public static void setMultiUserProof(boolean multi) {
		config.setProperty(MULTI_USER_POOF, multi);
	}

	public static Boolean isMultiUserProof() {
		return config.getBoolean(MULTI_USER_POOF, false);
	}
	
	private static final String PLAY_SOUND = "PLAY_SOUND";

	public static boolean isPlaySound() {
		return config.getBoolean(PLAY_SOUND, true);
	}
	
	public static void setPlaySound(boolean enable) {
		config.setProperty(PLAY_SOUND, enable);
	}

	
	private static final String WITH_BACKGROUND = "WITH_BACKGROUND";
	private static final String WITH_BACKGROUND_SP = "WITH_BACKGROUND_SP";
	private static final String ITEM_ADD_DIRECT = "ITEM_ADD_DIRECT";
	private static final String CASH_DRAWER_ALL = "CASH_DRAWER_ALL";
	private static final String REMOTE_BACKUP = "REMOTE_BACKUP";

	public static boolean isA4WithBackground() {
		return config.getBoolean(WITH_BACKGROUND, false);
	}

	public static void setA4WithBackground(boolean enable) {
		config.setProperty(WITH_BACKGROUND, enable);
	}
	public static boolean isA4WithBackgroundSP() {
		return config.getBoolean(WITH_BACKGROUND_SP, false);
	}

	public static void setA4WithBackgroundSP(boolean enable) {
		config.setProperty(WITH_BACKGROUND_SP, enable);
	}

	public static boolean isItemAdd() {
		return config.getBoolean(ITEM_ADD_DIRECT, true);
	}
	public static void setItemAdd(boolean enable) {
		config.setProperty(ITEM_ADD_DIRECT, enable);
	}

	public static boolean isAlwaysOpenCashdrawer() {
		return config.getBoolean(CASH_DRAWER_ALL, true);
	}
	
	public static void setAlwaysOpenCashdrawer(boolean enable) {
		config.setProperty(CASH_DRAWER_ALL, enable);
	}
	
	public static boolean isRemoteBackup() {
		return config.getBoolean(REMOTE_BACKUP, true);
	}
	
	public static void setRemoteBackup(boolean enable) {
		config.setProperty(REMOTE_BACKUP, enable);
	}
	private static final String ZAHLUNG_STUER = "ZAHLUNG_STUER";

	public static boolean isZahlungSteur() {
		return config.getBoolean(ZAHLUNG_STUER, false);
	}
	
	public static void setZahlungSteur(boolean enable) {
		config.setProperty(ZAHLUNG_STUER, enable);
	}
	
	public static boolean isRechGedruktDisplay() {
		return config.getBoolean(RECH_GEDRUKT_DISPLAY, false);
	}
	
	public static void setRechGedruktDisplay(boolean enable) {
		config.setProperty(RECH_GEDRUKT_DISPLAY, enable);
	}
	
	public static boolean isLiferantOptionDisplay() {
		return config.getBoolean(LIFERANT_OPTION_DISPLAY, false);
	}
	
	public static void setLiferantOptionDisplay(boolean enable) {
		config.setProperty(LIFERANT_OPTION_DISPLAY, enable);
	}
	
}
