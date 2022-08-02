package com.floreantpos.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.usb.UsbDevice;
import javax.usb.UsbHostManager;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.add.service.ReprintSaleWRT;
import com.floreantpos.add.service.WeightServer;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppProperties;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.isdnmonitor.CallMon;
import com.floreantpos.model.Loyalty;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.ReservationDB;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.SalesId;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.PrinterConfigurationDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.ui.views.LoginScreen;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.POSUtil;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.khan.online.sales.OnlineSalesUpdater;
import com.khana.backup.FtpBackup;
import com.khana.online.DBOnlineInformation;
import com.khana.schlussel.AddimatListener;
import com.khana.schlussel.IButtonSerialCOM;
import com.khana.schlussel.NcrDalaas;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.update.Updater;
import com.oro.orderextension.OrderWithCustomerService;

import jpos.CashDrawer;
import jpos.util.JposPropertiesConst;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;

public class Application{
	final static Logger logger = LoggerFactory.getLogger(Application.class);
	private boolean developmentMode = false;
//	private Timer autoDrawerPullTimer;
	private PluginManager pluginManager;
//	private Loyalty loyalty;
	private Terminal terminal;
	private PosWindow posWindow;
	private User currentUser;
	private RootView rootView;
	private BackOfficeWindow backOfficeWindow;
	private Shift currentShift;
	public PrinterConfiguration printConfiguration;
	private Restaurant restaurant;
	private PosPrinters printers;
	private ReservationDB reservationDb;
	private static Application instance;
	private static FloorLayoutPlugin layoutPlugin;
	private static OrderServiceExtension orderServicePlugin;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy"); //$NON-NLS-1$
	private static ImageIcon applicationIcon;
	private CallMon callMon;
	private boolean systemInitialized;
	private static boolean enabledGenerateReport;
	private final OnlineHandler onlineHandler = new OnlineHandler();
	private Timer onlineTimer = new Timer(1000,onlineHandler);
	public final static String VERSION = AppProperties.getVersion();
//	private static CashDrawer drawer;
	private static int notPrint =0;
	private OnlineSalesUpdater salesUpdater;
	private WeightServer weightServer;
	public int tagesAbsId;

	//	private static final String API_KEY_TEST = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
	//	private static final String API_SECRET_TEST= "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
	//	
	//	private static final String API_KEY_LIVE = "live_cb6zppxj72p32jv7uywgppvxk_khana-kassensystem";
	//	private static final String API_SECRET_LIVE = "UAD8FTUMSvtTdfLhzLNuYVvsngk6BZnnzNMJLmQg5Mn";
	public Double dineInTax;
	public FiskalyKeyParameter param;
	public FiskalyKeyParameter getParam() {
		return param;
	}

	public Double getDineInTax() {
		if(dineInTax==null) {
			try {
				dineInTax = TaxDAO.getInstance().findByName(POSConstants.DINE_IN).getRate();
			}catch(Exception ex){
				dineInTax = 0.0;
			}
		}

		return dineInTax;
	}

	public Double getHomeDeleveryTax() {
		if(homeDeleveryTax==null) {
			try {
				homeDeleveryTax = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY).getRate();
			}catch(Exception ex){
				homeDeleveryTax = 0.0;
			}
		}
		return homeDeleveryTax;
	}

	public Double homeDeleveryTax;


	public int getTagesAbsId() {
		return tagesAbsId;
	}

	public static int getNotPrint() {
		return notPrint;
	}

	public static void setNotPrint(int NotPrint) {
		Application.notPrint = NotPrint;
	}


	private Application() {
		Locale.setDefault(Locale.forLanguageTag(Locale.GERMANY.toLanguageTag()));
		applicationIcon = new ImageIcon(getClass().getResource("/icons/icon.png")); //$NON-NLS-1$
		posWindow = new PosWindow();
		posWindow.setTitle(getTitle());
		posWindow.setBackground(Color.WHITE);
	}

//	public static CashDrawer getDrawer()
//	{
//		if(drawer != null)
//		{
//			return drawer;
//		}
//		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME ,"jpos.xml");
//		try{
//			CashDrawer drawer = new CashDrawer();
//			return drawer;
//		}
//		catch(Exception e){e.printStackTrace();}
//		return drawer;
//
//	}

	public void setTaxRate() {
		Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		dineInTax = dineIn.getRate();
		Tax homeDelevery = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);    
		homeDeleveryTax = homeDelevery.getRate();
		if(TerminalConfig.isTseEnable()) {
			param = new FiskalyKeyParameter();
			param.setClientId(TerminalConfig.getTseClientId());
			param.setTssId(restaurant.getTseId());

		}

	}


	public void sheduleAutoSales() {	
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
		int hour =Integer.parseInt(restaurant.getAutoSalesHour());
		ZonedDateTime nextRun = now.withHour(16).withMinute(32).withSecond(0);
		if(now.compareTo(nextRun) > 0)
			nextRun = nextRun.plusDays(1);

		Duration duration = Duration.between(now, nextRun);
		long initalDelay = duration.getSeconds();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				takeTagesAbs();				
			}
		},
				initalDelay,
				TimeUnit.DAYS.toSeconds(1),
				TimeUnit.SECONDS);

		System.out.print("Auto Sales report will be taken at "+hour);
	}

	public void takeTagesAbs() {		
		try {
			ReprintSaleWRT sales = new ReprintSaleWRT();
			sales.showSalesReport(BusinessDateUtil.startOfOfficialDay(restaurant.getStartToday()));	
			sales.doPrint();
		}catch(Exception ex) {
		}		
		try {
			Thread.currentThread().sleep(15*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		restartApp();		
		//		System.exit(0);	
	}


	public void sheduleAutoRestart() {		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		tomorrow.set(Calendar.HOUR_OF_DAY, 6);
		tomorrow.set(Calendar.MINUTE,1);
		long initalDelay = (tomorrow.getTime().getTime() - new Date().getTime());
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Started");
					Thread.sleep(15*1000);
					posWindow.saveSizeAndLocation();
					restartSystem();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		},
				initalDelay,
				TimeUnit.DAYS.toMillis(1),
				TimeUnit.MILLISECONDS);
		System.out.println("Auto restart enable");

	}

	public void restartSystem()
	{
		Runtime runtime = Runtime.getRuntime();
		try {
			if(TerminalConfig.getOs().equals("Linux"))
				runtime.exec("./restart.sh");
			else
				runtime.exec("shutdown /r");
		} catch (IOException e) {
			System.out.println(e);
		}


		System.exit(0);
	}

	public static void restartApp() {
		try {

			Runtime.getRuntime().exec("cmd /c start restart.vbs");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void start() throws ClientProtocolException, IOException {
		pluginManager = PluginManagerFactory.createPluginManager();
		pluginManager.addPluginsFrom(new File("plugins/").toURI());

		if (developmentMode) {
			pluginManager.addPluginsFrom(new File("/home/mshahriar/project/oro/target/classes").toURI());
		}

		setApplicationLook();

		rootView = RootView.getInstance();

		posWindow.getContentPane().add(rootView);
		posWindow.setupSizeAndLocation();

		if (TerminalConfig.isFullscreenMode()) {
			posWindow.enterFullScreenMode();
		}

		posWindow.setVisible(true);
		initializeSystem();
		try {
			Restaurant rest = RestaurantDAO.getRestaurant();
			tagesAbsId = rest.getSalesid()!=null?rest.getSalesid():Integer.parseInt(TerminalConfig.getSalesId());
			Date Yesterday = rest.getStartToday();
			System.out.println("System Day "+Yesterday+" Current Day "+new Date());		
			checkDate(Yesterday);
			if(rest.getTicketid()==1)
				resetRechnugPerionTickets();		

		}catch(Exception ex) {

		}		

		try {
			new DBOnlineInformation();	
		}catch(Throwable ex) {}


		if(TerminalConfig.isAdimatComEnable()) {
			try {
				if(TerminalConfig.getKeyType().compareTo("addimat")==0)					
					startAdimat();
				else if(TerminalConfig.getKeyType().compareTo("ibutton")==0)
					startIbutton();
				else
					startNcrDalaas();
			}catch(Exception ex) {
				logger.info(ex.getMessage());
				ex.printStackTrace();
			}			
		}		

		onlineTimer.start();
		try {
			if(TerminalConfig.isUsbCert()) {
				String usbCert = restaurant.getCertUsbId();
				if(usbCert!=null&&StringUtils.isNotEmpty(usbCert)) {
					startUsbCert();
					System.out.println("................");
				}	
			}
						
		}catch (Exception e) {System.out.println("Unable to start usbCert "+e.getMessage());}


		try {
			if(!TerminalConfig.isBuildMode()&&TerminalConfig.isRemoteBackup())
				uploadBackup();

		}catch(Exception ex) {if(TerminalConfig.isBuildMode())
			ex.printStackTrace();
		System.out.println("Unable to upload data to Server "+ex.getMessage());}
	}
	public NcrDalaas ncrDalaas;

	public NcrDalaas getNcrListener() {
		return ncrDalaas;
	}
	public void startNcrDalaas() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ncrDalaas = new NcrDalaas();
				ncrDalaas.StartNcrDalaasSerialCOM();			
			}
		});
		t.start();
	}

	public AddimatListener adiListener;
	public AddimatListener getAdiListener() {
		return adiListener;
	}

	public IButtonSerialCOM ibuttonListener;
	public IButtonSerialCOM getIbuttonListener() {
		return ibuttonListener;
	}

	public void startAdimat() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				adiListener = new AddimatListener();

			}
		});
		t.start();
	}	

	public void startIbutton() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ibuttonListener = new IButtonSerialCOM();
				ibuttonListener.StartIButtonSerialCOM();

			}
		});
		t.start();
	}



	public void startUsbCert() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					UsbServices services = UsbHostManager.getUsbServices();
					services.addUsbServicesListener((UsbServicesListener) new UsbServicesListener() {
						boolean found = false;
						@Override
						public void usbDeviceDetached(UsbServicesEvent event) {
							UsbDevice device = event.getUsbDevice( );
							String registerdUsbId = ""+device.getUsbDeviceDescriptor().idVendor()+device.getUsbDeviceDescriptor().idProduct();

							if(registerdUsbId.equals(restaurant.getCertUsbId()))
								found = false;						
							TerminalConfig.setUsbCert(found);						
						}

						@Override
						public void usbDeviceAttached(UsbServicesEvent event) {
							UsbDevice device = event.getUsbDevice( );
							String registerdUsbId = ""+device.getUsbDeviceDescriptor().idVendor()+device.getUsbDeviceDescriptor().idProduct();

							if(registerdUsbId.equals(restaurant.getCertUsbId()))
								found = true;
							TerminalConfig.setUsbCert(found);
						}
					});
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		t.start();
	}



	public void checkDate(Date Yesterday) {
		try {
			Date tday = new Date();
			if(Yesterday==null) {
				restaurant.setStartToday(tday);
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);				
			}else if(isBefore(BusinessDateUtil.endOfOfficialDay(Yesterday), tday)) {
				restaurant.setStartToday(tday);
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				System.out.println("Setted Today"+tday);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	public boolean isBefore(Date yday, Date tday) {	  
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(yday);		  
		Calendar today = Calendar.getInstance();
		today.setTime(tday);

		if(tday.getTime() - yday.getTime()>0)
			return true;
		else if(yesterday.get(Calendar.MONTH)!=today.get(Calendar.MONTH))
			return true;
		else if(yesterday.get(Calendar.DAY_OF_MONTH)>today.get(Calendar.DAY_OF_MONTH)+1)
			return true;
		else
			return  false;

	}


	public boolean checkValidLicenseKey() throws ClientProtocolException, IOException
	{
		String licenseKey = RestaurantDAO.getRestaurant().getLicenseKey();

		if(licenseKey != null)
		{
			if(licenseKey.compareTo("1981198219831984") == 0)
				return true;
		}

		String macAddress = "";
		Enumeration<NetworkInterface> nwInterface = NetworkInterface.getNetworkInterfaces();
		while (nwInterface.hasMoreElements()) {
			NetworkInterface nis = nwInterface.nextElement();
			if (nis != null) {
				if(!nis.getName().contains("eth"))
					continue;
				byte[] mac = nis.getHardwareAddress();
				if (mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
					}
					macAddress = sb.toString();
					break;

				} 
			}
		}
		if(macAddress.length() == 0)
		{
			return false;
		}
		else
		{
			String storedMacAddress = RestaurantDAO.getRestaurant().getLicenseMac();
			if(storedMacAddress == null)return false;

			System.out.println("Stored mac address:"+ storedMacAddress);
			System.out.println("Present mac address:"+ macAddress);
			Date expiryDate = RestaurantDAO.getRestaurant().getLicenseExpiryDate();

			if(expiryDate != null)
			{
				int expiryYear = expiryDate.getYear() + 1900;
				String expiry = expiryDate.getDate()+"-"+expiryDate.getMonth() + "-"+expiryYear; 
				Date todayDate = new Date();
				int todayYear = todayDate.getYear()+1900;
				String today = todayDate.getDate()+"-"+todayDate.getMonth() + "-"+todayYear;
				System.out.println("Expiry Date: "+expiryDate.getDate() + "-" + expiryDate.getMonth());
				System.out.println("Today date:"+ todayDate.getDate() + "-" + todayDate.getMonth());
				if(expiryDate.after(todayDate))
				{
					System.out.println("Time is there");
					return true;
				}
				else if (expiry.compareTo(today) == 0)
				{
					POSMessageDialog.showError("Ihre Lizenz abläuft morgen");
					return true;
				}
				else
				{
					String url = "http://www.khana.de/floreantpos/deactivate.php?lizenzkey=";
					String key = RestaurantDAO.getRestaurant().getLicenseKey();
					url = url+ key.toUpperCase();
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httpost = new HttpPost(url);
					httpclient.execute(httpost);
					RestaurantDAO dao = new RestaurantDAO(); 
					Restaurant restaurant = dao.get(Integer.valueOf(1));
					restaurant.setLicenseExpiryDate(null);
					restaurant.setLicenseKey("");
					restaurant.setLicenseMac("");
					dao.saveOrUpdate(restaurant);
					POSMessageDialog.showError("Ihre Lizenz abläuft, Bitte kontaktieren Sie uns");
					return false;
				}
			}
			else if(storedMacAddress.compareTo(macAddress) == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private void uploadBackup() {
		Thread backuper = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new FtpBackup();
				}catch(Exception ex) {}

			}
		});
		backuper.start();
	}


	private void resetRechnugPerionTickets() {	
		try {			
			List<Ticket> list = TicketDAO.getInstance().findAll();
			int rechnugNummer = RestaurantDAO.getRestaurant().getTicketid();			
			for (Ticket ticket : list) {
				ticket.setTicketid(ticket.getId());			
				TicketDAO.getInstance().saveOrUpdate(ticket);
				rechnugNummer = ticket.getId();
			}
			restaurant.setTicketid(rechnugNummer+1);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
			for(MenuItem item:itemList) {
				item.setPriceCategory(1);
				MenuItemDAO.getInstance().saveOrUpdate(item);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public CallMon getCallMonitorInstance()
	{
		return callMon;
	}

	public void startIsdnService()
	{
		callMon = new CallMon();

	}

	public static void enabledGenerateReport()
	{
		enabledGenerateReport = true;
	}

	public static boolean getEnabledGenerateReport()
	{
		return enabledGenerateReport;
	}
	private void setApplicationLook() {
		try {

			initializeFont();
			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			UIManager.put("ComboBox.is3DEnabled", Boolean.FALSE); //$NON-NLS-1$
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	private void initializeFont() {
		String uiDefaultFont = TerminalConfig.getUiDefaultFont();
		if(StringUtils.isEmpty(uiDefaultFont)) {
			return;
		}

		Font sourceFont = UIManager.getFont("Label.font");
		Font font = new Font(uiDefaultFont, sourceFont.getStyle(), sourceFont.getSize());

		UIManager.put("Button.font", font);
		UIManager.put("ToggleButton.font", font);
		UIManager.put("RadioButton.font", font);
		UIManager.put("CheckBox.font", font);
		UIManager.put("ColorChooser.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("Label.font", font);
		UIManager.put("List.font", font);
		UIManager.put("MenuBar.font", font);
		UIManager.put("MenuItem.font", font);
		UIManager.put("RadioButtonMenuItem.font", font);
		UIManager.put("CheckBoxMenuItem.font", font);
		UIManager.put("Menu.font", font);
		UIManager.put("PopupMenu.font", font);
		UIManager.put("OptionPane.font", font);
		UIManager.put("Panel.font", font);
		UIManager.put("ProgressBar.font", font);
		UIManager.put("ScrollPane.font", font);
		UIManager.put("Viewport.font", font);
		UIManager.put("TabbedPane.font", font);
		UIManager.put("Table.font", font);
		UIManager.put("TableHeader.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("PasswordField.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("TextPane.font", font);
		UIManager.put("EditorPane.font", font);
		UIManager.put("TitledBorder.font", font);
		UIManager.put("ToolBar.font", font);
		UIManager.put("ToolTip.font", font);
		UIManager.put("Tree.font", font);
	}

	public void initializeSystem() {		

		if (isSystemInitialized()) {
			return;
		} 
		try {
			posWindow.setGlassPaneVisible(true);
			initTerminal();
			initPrintConfig();
			refreshRestaurant();
			loadPrinters();		
			try {
				if(!TerminalConfig.isBuildMode())
					checkForUpdate();
				else
					System.out.println("APP_VERSION - "+VERSION);
			}catch(Throwable ex) {

			}			

			try {
				setTaxRate();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Die Steuer ist leer, bitte erstellen Sie die Steuer.");
				Calendar cal = Calendar.getInstance();
				if(TerminalConfig.isSpecial()&&cal.get(Calendar.YEAR)<2021) {
					dineInTax = 16.00;
					homeDeleveryTax = 5.0;
				}else {
					dineInTax = 19.00;
					homeDeleveryTax = 7.0;
				}

			}

			if(TerminalConfig.isWeightServer()) {
				startWeightServer(); 
			}
			if(TerminalConfig.isAutoTagesAbs())
				sheduleAutoSales();
			if(TerminalConfig.isOnlineSalesStart()) {
				startOnlineSalesListner();
			}
			if(TerminalConfig.isAutoRestartSystem()||TerminalConfig.isKhanaServer())
				sheduleAutoRestart();
//			loyalty = Loyalty.loadLoyalty();

			if(!TerminalConfig.isBuildMode()) {
				if(!loadNativeAbsoluteLib())
					loadNativeLib();
			}

			setSystemInitialized(true);

		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			if (writer.toString().contains("Another instance of Derby may have already booted")) {
				POSMessageDialog.showError("Another FloreantPOS instance may be already running.\n" + "Multiple instances cannot be run in Derby single mode");
				return;
			}else {
				int option = JOptionPane.showConfirmDialog(getPosWindow(),
						Messages.getString("Application.0"), Messages.getString(POSConstants.POS_MESSAGE_ERROR), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (option == JOptionPane.YES_OPTION) {
					DatabaseConfigurationDialog.show(Application.getPosWindow());
				}
			}

		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			getPosWindow().setGlassPaneVisible(false);
		}
	}




	public boolean loadNativeAbsoluteLib() {
		try {
			String system = System.getProperty("sun.arch.data.model");

			if(system.compareTo("64")==0) {
				System.load(System.getProperty("user.dir")+"\\Native_lib\\"+"com.fiskaly.client-windows-amd64-v1.2.100.dll");//Absolute Way
			}else if(system.compareTo("32")==0) {
				System.load(System.getProperty("user.dir")+"\\Native_lib\\"+"com.fiskaly.client-windows-386-v1.2.100.dll");
			}
		}catch(Throwable ex) {

			if(TerminalConfig.isBuildMode())
				ex.printStackTrace();
			System.out.println("Unable to load Native Lib "+ex.getMessage());
			return false;
		}
		return true;
	}

	public void loadNativeLib() {
		try {
			String system = System.getProperty("sun.arch.data.model");
			if(system.compareTo("64")==0) {
				Runtime.getRuntime().loadLibrary("com.fiskaly.client-windows-amd64-v1.2.100");//System Library from bin
			}else if(system.compareTo("32")==0) {
				Runtime.getRuntime().loadLibrary("com.fiskaly.client-windows-386-v1.2.100");}
		}catch(Throwable ex) {
			if(TerminalConfig.isBuildMode())
				ex.printStackTrace();
			System.out.println("Unable to load Native Lib "+ex.getMessage());
		}
	}



	public static void checkForUpdate() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Updater updater = new Updater();
					updater.checkForUpdate();
				} catch (Throwable e) {
					e.printStackTrace();
				}		
			}
		});
		t.start();				
	}



	public void startOnlineSalesListner()
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					salesUpdater = new OnlineSalesUpdater("C:\\Data\\Online", true);
				}catch(Exception ex) {
					System.out.println("Unable to Start Online Listner");
				}
			}
		});
		t.start();
	}

	public void startWeightServer()
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					weightServer = WeightServer.getInstance();
				}catch(Exception ex) {
					System.out.println("Unable to Start Weight server");
				}
			}
		});

		t.start();

	}

	private void loadPrinters() {
		printers = PosPrinters.load();
		if(printers == null) {
			printers = new PosPrinters();
		}
	}

	public ReservationDB getReservationDb()
	{
		if(reservationDb ==null)
		{
			reservationDb = new ReservationDB();
		}
		return reservationDb;
	}
	public long getSalesId()
	{
		long id = SalesId.getNewId();
		return id;
	}

//	public long getLoyaltyId()
//	{
//		long id = Loyalty.getNewId();
//		return id;
//	}
//	public void saveLoyalty(long id)
//	{
//		loyalty.IncrementAndsave(id);
//	}
	private void initPrintConfig() {
		printConfiguration = PrinterConfigurationDAO.getInstance().get(PrinterConfiguration.ID);
		if (printConfiguration == null) {
			printConfiguration = new PrinterConfiguration();
		}
	}

	private void initTerminal() {
		int terminalId = TerminalConfig.getTerminalId();

		if (terminalId == -1) {
			Random random = new Random();
			terminalId = random.nextInt(10000) + 1;
		}

		Terminal terminal = null;
		try {
			terminal = TerminalDAO.getInstance().get(new Integer(terminalId));
			if (terminal == null) {
				terminal = new Terminal();
				terminal.setId(terminalId);
				terminal.setOpeningBalance(new Double(500));
				terminal.setCurrentBalance(new Double(500));
				terminal.setName(String.valueOf(terminalId)); //$NON-NLS-1$

				TerminalDAO.getInstance().saveOrUpdate(terminal);
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}

		TerminalConfig.setTerminalId(terminalId);
		RootView.getInstance().getLoginScreen().setTerminalId(terminalId);
		this.terminal = terminal;
	}

	public void refreshRestaurant() {
		try {
			this.restaurant = RestaurantDAO.getRestaurant();

			if (restaurant.getUniqueId() == null || restaurant.getUniqueId() == 0) {
				restaurant.setUniqueId(RandomUtils.nextInt());
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			}

//			if (restaurant.isAutoDrawerPullEnable() && autoDrawerPullTimer == null) {
//				autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
//				autoDrawerPullTimer.start();
//			}
//			else {
//				if (autoDrawerPullTimer != null) {
//					autoDrawerPullTimer.stop();
//					autoDrawerPullTimer = null;
//				}
//			}

		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}

	}

	public static String getCurrencyName() {
		Application application = getInstance();
		if (application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencyName();
	}

	public static String getCurrencySymbol() {
		Application application = getInstance();
		if (application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencySymbol();
	}

	public synchronized static Application getInstance() {
		if (instance == null) {
			instance = new Application();

		}
		return instance;
	}

	public void exitPOS(boolean ask) {
		User user = getCurrentUser();

		if (ask&&user != null && !user.hasPermission(UserPermission.SHUT_DOWN)) {
			POSMessageDialog.showError("You do not have permission to execute this action");
			return;
		}
		if(ask) {
			ErrorMessageDialog dialog = new ErrorMessageDialog(com.floreantpos.POSConstants.SURE_EXIT, true);
			dialog.pack();
			dialog.open();
			if(dialog.isCancelled())return;
		}		

		posWindow.saveSizeAndLocation();
		if(ask)
			stopAllOldProcess();
		System.exit(0);
	}

	private static void stopAllOldProcess() {
		try {
			Process process = Runtime.getRuntime().exec("taskkill /f /im java.exe");
			process = Runtime.getRuntime().exec("taskkill /f /im javaw.exe");
			process = Runtime.getRuntime().exec("taskkill /f /im java.exe");
		}catch(Exception ex) {

		}
	}

	public void shutdownPOS() {
		User user = getCurrentUser();

		if (user != null && !user.hasPermission(UserPermission.SHUT_DOWN)) {
			POSMessageDialog.showError("You do not have permission to execute this action");
			return;
		}

		ErrorMessageDialog dialog = new ErrorMessageDialog(com.floreantpos.POSConstants.SURE_SHUTDOWN, true);
		dialog.pack();
		dialog.open();
		if(dialog.isCancelled())return;
		posWindow.saveSizeAndLocation();
		shutDownSystem();
	}
	public void shutdownPOS(int time) throws InterruptedException {
		User user = getCurrentUser();

		if (user != null && !user.hasPermission(UserPermission.SHUT_DOWN)) {
			POSMessageDialog.showError("You do not have permission to execute this action");
			return;
		}

		ErrorMessageDialog dialog = new ErrorMessageDialog(com.floreantpos.POSConstants.SURE_SHUTDOWN, true, true);
		dialog.pack();
		dialog.open();
		if(dialog.isCancelled())return;
		Thread.currentThread().sleep(15*1000);
		posWindow.saveSizeAndLocation();		
		shutDownSystem();
	}
	public void shutDownSystem()
	{
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec("shutdown -s -t 0");
		} catch (IOException e) {
			System.out.println(e);
		}

		System.exit(0);
	}

	public void logout() {
		if (backOfficeWindow != null) {
			backOfficeWindow.setVisible(false);
			backOfficeWindow = null;
			currentShift = null;
		}

		setCurrentUser(null);
		RootView.getInstance().showView(LoginScreen.VIEW_NAME);
	}

	public static User getCurrentUser() {
		return getInstance().currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public RootView getRootView() {
		return rootView;
	}

	public void setRootView(RootView rootView) {
		this.rootView = rootView;
	}

	public static PosWindow getPosWindow() {
		return getInstance().posWindow;
	}

	//	public BackOfficeWindow getBackOfficeWindow() {
	//		return backOfficeWindow;
	//	}

	public void setBackOfficeWindow(BackOfficeWindow backOfficeWindow) {
		this.backOfficeWindow = backOfficeWindow;
	}

	public Terminal getTerminal() {

		TerminalDAO.getInstance().refresh(terminal);
		return terminal;
	}

	//	public static PrinterConfiguration getPrinterConfiguration() {
	//		return getInstance().printConfiguration;
	//	}

	public static PosPrinters getPrinters() {
		return getInstance().printers;
	}

	public static String getTitle() {
		Date date = new Date();
		return "Khana Kassensysteme";//$NON-NLS-1$
	}

	public static ImageIcon getApplicationIcon() {
		return applicationIcon;
	}

	public static void setApplicationIcon(ImageIcon applicationIcon) {
		Application.applicationIcon = applicationIcon;
	}

	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public Shift getCurrentShift() {
		return currentShift;
	}

	public void setCurrentShift(Shift currentShift) {
		this.currentShift = currentShift;
	}

//	public void setAutoDrawerPullEnable(boolean enable) {
//		if (enable) {
//			if (autoDrawerPullTimer != null) {
//				return;
//			}
//			else {
//				autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
//				autoDrawerPullTimer.start();
//			}
//		}
//		else {
//			autoDrawerPullTimer.stop();
//			autoDrawerPullTimer = null;
//		}
//	}

	public boolean isSystemInitialized() {
		return systemInitialized;
	}

	public void setSystemInitialized(boolean systemInitialized) {
		this.systemInitialized = systemInitialized;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public static PluginManager getPluginManager() {
		return getInstance().pluginManager;
	}

	public static OrderServiceExtension getCustomerPlugin()
	{
		if (orderServicePlugin == null)
		{
			orderServicePlugin = new OrderWithCustomerService();
			return orderServicePlugin;
		}
		else
			return orderServicePlugin;
	}
	public static File getWorkingDir() {
		File file = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		return file.getParentFile();
	}

	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(boolean developmentMode) {
		this.developmentMode = developmentMode;
	}

	public boolean isPriceIncludesTax() {
		Restaurant restaurant = getRestaurant();
		if (restaurant == null) {
			return false;
		}

		return POSUtil.getBoolean(restaurant.isItemPriceIncludesTax());
	}
	private void checkFtpOnline()
	{	
	}

	public class OnlineHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			instance.checkFtpOnline();
		}
	}


}
