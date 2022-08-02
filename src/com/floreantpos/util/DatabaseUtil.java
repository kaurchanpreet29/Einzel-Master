package com.floreantpos.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.main.Application;
import com.floreantpos.model.CallList;
import com.floreantpos.model.Cashbook;
import com.floreantpos.model.Gebiet;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Street;
import com.floreantpos.model.StreetDB;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.VoidReason;
import com.floreantpos.model.dao.CallListDAO;
import com.floreantpos.model.dao.CashbookDAO;
import com.floreantpos.model.dao.DeliveryCostDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.StreetDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.model.dao.VoidReasonDAO;
import com.floreantpos.model.dao._RootDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.CashBookData;
import com.floreantpos.ui.forms.CashBookIndex;

public class DatabaseUtil {
	private static Log logger = LogFactory.getLog(DatabaseUtil.class);

	public static void checkConnection(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password)
			throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);

		configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
		configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

		configuration = configuration.setProperty("hibernate.connection.url", connectionString);
		configuration = configuration.setProperty("hibernate.connection.username", user);
		configuration = configuration.setProperty("hibernate.connection.password", password);

		checkConnection(configuration);
	}

	public static void checkConnection() throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);
		checkConnection(configuration);
	}

	public static void checkConnection(Configuration configuration) throws DatabaseConnectionException {
		try {
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.close();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	public static boolean createDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password, boolean exportSampleData) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");

			SchemaExport schemaExport = new SchemaExport(configuration);
			schemaExport.create(true, true);

			_RootDAO.initialize();

			Restaurant restaurant = new Restaurant();
			restaurant.setId(Integer.valueOf(1));
			restaurant.setName("Sample Restaurant");
			restaurant.setAddressLine1("Somewhere");
			restaurant.setTelephone("+0123456789");
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);

			Tax tax = new Tax();
			tax.setName("US");
			tax.setRate(Double.valueOf(6));
			TaxDAO.getInstance().saveOrUpdate(tax);

			Shift shift = new Shift();
			shift.setName(com.floreantpos.POSConstants.GENERAL);
			java.util.Date shiftStartTime = ShiftUtil.buildShiftStartTime(0, 0, 0, 11, 59, 1);
			java.util.Date shiftEndTime = ShiftUtil.buildShiftEndTime(0, 0, 0, 11, 59, 1);

			shift.setStartTime(shiftStartTime);
			shift.setEndTime(shiftEndTime);
			long length = Math.abs(shiftStartTime.getTime() - shiftEndTime.getTime());

			shift.setShiftLength(Long.valueOf(length));
			ShiftDAO.getInstance().saveOrUpdate(shift);

			UserType type = new UserType();
			type.setName(com.floreantpos.POSConstants.ADMINISTRATOR);
			type.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
			UserTypeDAO.getInstance().saveOrUpdate(type);

			User u = new User();
			u.setUserId(123);
			u.setSsn("123");
			u.setPassword("1111");
			u.setFirstName("Administrator");
			u.setLastName(com.floreantpos.POSConstants.USER);
			u.setType(type);

			UserDAO dao = new UserDAO();
			dao.saveOrUpdate(u);

			if(!exportSampleData) {
				return true;
			}

			//DataImportAction.importMenuItems(DatabaseUtil.class.getResourceAsStream("/floreantpos-menu-items.xml"));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static boolean updateDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "update");

			SchemaUpdate schemaUpdate = new SchemaUpdate(configuration);
			schemaUpdate.execute(true, true);

			_RootDAO.initialize();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static Configuration initialize() throws DatabaseConnectionException {
		try {

			return _RootDAO.reInitialize();

		} catch (Exception e) {
			logger.error(e);
			throw new DatabaseConnectionException(e);
		}

	}

	public static void main(String[] args) throws Exception {

		initialize();
		//cashBookTest();
		
		for(MenuItem item: MenuItemDAO.getInstance().findAll()) {
		  MenuItemDAO.getInstance().delete(item);
    }
		
		for(MenuGroup menuGroup: MenuGroupDAO.getInstance().findAll()) {
		  MenuGroupDAO.getInstance().delete(menuGroup);
        }
		
		
		for(MenuCategory menuGroup: MenuCategoryDAO.getInstance().findAll()) {
		  MenuCategoryDAO.getInstance().delete(menuGroup);
		}
	}

	public static void cashBookTest()
	{

		
	}
}
