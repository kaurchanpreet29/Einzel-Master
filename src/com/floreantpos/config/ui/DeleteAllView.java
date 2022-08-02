package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.VoidReason;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.model.dao.VoidReasonDAO;
import com.floreantpos.model.dao._RootDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.BusinessDateUtil;
import com.floreantpos.util.DatabaseConnectionException;

public class DeleteAllView extends ConfigurationView{
	JCheckBox cbMenu;
	JCheckBox cbTickets;
	JCheckBox cbTax;
	JCheckBox cbSalesReport;
	JCheckBox cbAll;
	JCheckBox cbVoidReason;
	JCheckBox cbInventoryReset;
    JCheckBox cbLicense;
    JButton btnLoeshen;
    JButton btnClose;
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker =  new JXDatePicker();

	public DeleteAllView()
	{
		super();
		initComponents();
	}
	JButton btnResetRechnug;
	JButton btnResetRechNummer;
	private void initComponents() {
		setLayout(new MigLayout()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBackground(new Color(209,222,235));
		
		JPanel panel = new JPanel(new MigLayout());
		if(StringUtils.isNotEmpty(POSConstants.Daten_Loeschen))
			panel.setBorder(BorderFactory.createTitledBorder(POSConstants.Daten_Loeschen));
		else
			panel.setBorder(BorderFactory.createTitledBorder("Daten Loeschen"));
		
		fromDatePicker.setDate(new java.util.Date());
		toDatePicker.setDate(new java.util.Date());
		btnLoeshen = new JButton("Rechnung Löschen");
		if(StringUtils.isNotEmpty(POSConstants.Rechnung_Loeschen))
			btnLoeshen.setText(POSConstants.Rechnung_Loeschen);
		else
			btnLoeshen.setText("Rechnung Löschen");
		
		btnLoeshen.setBackground(new Color(249, 145, 122));
		btnLoeshen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				deletePerionTickets();
				
			}
		});
		
		
		
		
		cbMenu = new JCheckBox();
		if(StringUtils.isNotEmpty(POSConstants.Menue))
			cbMenu.setText(POSConstants.Menue);
		else
			cbMenu.setText("Menue");
		
		cbTickets = new JCheckBox();
		if(StringUtils.isNotEmpty(POSConstants.Rechnung))
			cbTickets.setText(POSConstants.Rechnung);
		else
			cbTickets.setText("Rechnung");
		
		cbTax = new JCheckBox();
		if(StringUtils.isNotEmpty(POSConstants.Steuer))
			cbTax.setText(POSConstants.Steuer);
		else
			cbTax.setText("Steuer");
		
		cbSalesReport = new JCheckBox("Sales Report");
		cbAll = new JCheckBox("Alles auswaehlen");
		if(StringUtils.isNotEmpty(POSConstants.Alles_auswaehlen))
			cbAll.setText(POSConstants.Alles_auswaehlen);
		else
			cbAll.setText("Alles auswaehlen");
		
		cbVoidReason = new JCheckBox("Grund für Loeschvorgang");
		if(StringUtils.isNotEmpty(POSConstants.Grund_für_Loeschvorgang))
			cbVoidReason.setText(POSConstants.Grund_für_Loeschvorgang);
		else
			cbVoidReason.setText("Grund für Loeschvorgang");
		
		cbLicense = new JCheckBox("LIZENZ");
		if(StringUtils.isNotEmpty(POSConstants.LIZENZ))
			cbLicense.setText(POSConstants.LIZENZ);
		else
			cbLicense.setText("LIZENZ");
		
		cbInventoryReset = new JCheckBox("Invetuar");
		if(StringUtils.isNotEmpty(POSConstants.Invetuar))
			cbInventoryReset.setText(POSConstants.Invetuar);
		else
			cbInventoryReset.setText("Invetuar");
		
		cbAll.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				
				if(cbAll.isSelected())
				{
					cbMenu.setSelected(true);
					cbTickets.setSelected(true);
					cbTax.setSelected(true);
					cbSalesReport.setSelected(true);
					cbVoidReason.setSelected(true);
				}
				
			}
			
		});
		
		
		
		
		btnResetRechnug = new JButton("Rechnung Reset");
		btnResetRechnug.setBackground(new Color(249, 145, 122));
		btnResetRechnug.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetRechnugPerionTickets();
			}
		});
		
		

		btnClose = new JButton("Close Ticket");
		btnClose.setBackground(new Color(249, 145, 122));
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				closePerionTickets();
			}
		});
		
		btnResetRechNummer = new JButton("Set Rechnung Nummer");
		btnResetRechNummer.setBackground(new Color(249, 145, 122));
		btnResetRechNummer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nummer = JOptionPane.showInputDialog("Bitte Rechnug Nummer Geben");	
				restt.setTicketid(Integer.parseInt(nummer.trim()));
				updateRechNummer();
				RestaurantDAO.getInstance().saveOrUpdate(restt);
			}
		});
		
		
		rechNummer = new JLabel();
		rechNummer.setFocusable(false);		
		
		
		
		
		panel.add(cbAll,"wrap");
		cbAll.setBackground(new Color(209,222,235));
		panel.add(cbMenu);
		cbMenu.setBackground(new Color(209,222,235));
		panel.add(cbTickets);
		cbTickets.setBackground(new Color(209,222,235));
		panel.add(cbTax);
		cbTax.setBackground(new Color(209,222,235));
		panel.add(cbVoidReason);
		panel.add(cbSalesReport);
		cbSalesReport.setBackground(new Color(209,222,235));
		cbVoidReason.setBackground(new Color(209,222,235));
		panel.setBackground(new Color(209,222,235));
		cbLicense.setBackground(new Color(209,222,235));
		panel.add(cbLicense);
		cbInventoryReset.setBackground(new Color(209,222,235));
		panel.add(cbInventoryReset, "wrap");
		if(Application.getCurrentUser().getFirstName().compareTo("Master")==0||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
			restt = RestaurantDAO.getRestaurant();
			panel.add(rechNummer);
			panel.add(fromDatePicker);
			panel.add(toDatePicker);
			panel.add(btnClose);
			panel.add(btnResetRechNummer);
			panel.add(btnLoeshen);
			panel.add(btnResetRechnug, "wrap");
			updateRechNummer();
		}		
		add(panel);
		
	}
	
	public JLabel rechNummer;
	Restaurant restt;
		protected void updateRechNummer() {
				try {
					rechNummer.setText("Current Rechnung Nr. "+restt.getTicketid());
				}catch(Exception ex) {
					rechNummer.setText("Error");
				}	
			
		}	
		
		private void resetRechnugPerionTickets() {
			java.util.Date fromDate = fromDatePicker.getDate();
			java.util.Date toDate = toDatePicker.getDate();
			try {
				List<PosTransaction> findAll = PosTransactionDAO.getInstance().findAll();
				for (PosTransaction posTransaction : findAll) {
					PosTransactionDAO.getInstance().delete(posTransaction);
				}
				fromDate = BusinessDateUtil.startOfOfficialDay(fromDate);
				toDate = BusinessDateUtil.endOfOfficialDay(toDate);			
				List<Ticket> list = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
				int rechnugNummer = RestaurantDAO.getRestaurant().getTicketid();
				
				for (Ticket ticket : list) {
					ticket.setTicketid(rechnugNummer);				
					TicketDAO.getInstance().saveOrUpdate(ticket);
					rechnugNummer = rechnugNummer+1;
				}
				restt.setTicketid(rechnugNummer);
				RestaurantDAO.getInstance().saveOrUpdate(restt);
				updateRechNummer();
				 JOptionPane.showMessageDialog(null, "Erfolg");
			}catch(Exception ex) {
				 JOptionPane.showMessageDialog(null, "Error : "+ex.toString().substring(0, 20));
			}
		}
		
		
		private void closePerionTickets() {
			java.util.Date fromDate = fromDatePicker.getDate();
			java.util.Date toDate = toDatePicker.getDate();
			try {		
				fromDate = BusinessDateUtil.startOfOfficialDay(fromDate);
				toDate = BusinessDateUtil.endOfOfficialDay(toDate);			
				List<Ticket> list = TicketDAO.getInstance().findDateTickets(fromDate, toDate);

				for (Ticket ticket : list) {
					if (!ticket.isClosed()) {
						ticket.setPaid(true);
						ticket.setClosed(true);
						System.out.println(ticket.getCreateDate());
						TicketDAO.getInstance().saveOrUpdate(ticket);
					}
				}

				JOptionPane.showMessageDialog(null, "Erfolg");
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Error : "+ex.toString().substring(0, 20));
			}
		}
	
	private void deletePerionTickets() {
		java.util.Date fromDate = fromDatePicker.getDate();
		java.util.Date toDate = toDatePicker.getDate();
		try {
			List<PosTransaction> findAll = PosTransactionDAO.getInstance().findAll();
			for (PosTransaction posTransaction : findAll) {
				PosTransactionDAO.getInstance().delete(posTransaction);
			}

			List<Ticket> list = TicketDAO.getInstance().findDateTickets(fromDate, toDate);

			List<TicketItem> itemlist = TicketItemDAO.getInstance().findAllByDates(fromDate, toDate);
		
			
			for (TicketItem item : itemlist) {
				TicketItemDAO.getInstance().delete(item);
			}
			for (Ticket ticket : list) {
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				TicketDAO.getInstance().delete(ticket);
				System.out.println("deleted  "+ticket.getCreateDateFormatted());				
			}
			

			List<Salesreportdb> reports = SalesReportDAO.getInstance().findDateReports(fromDate, toDate);
			if(reports != null) {
				Session sesion = SalesReportDAO.getInstance().createNewSession();
				Transaction tx = sesion.beginTransaction();
				reports.stream().forEach(report -> {						
					sesion.delete(report);						
				});
				tx.commit();
				sesion.close();				
			}
			POSMessageDialog.showError("Erfolg");
		}catch(Exception ex) {
			POSMessageDialog.showError("Error....");
		}
	}

	@Override
	public boolean save() throws Exception {

		
		initializeDb();
		
		if(cbTickets.isSelected())
		{
			List<PosTransaction> findAll = PosTransactionDAO.getInstance().findAll();
			for (PosTransaction posTransaction : findAll) {
				PosTransactionDAO.getInstance().delete(posTransaction);
			}
			List<Ticket> list = TicketDAO.getInstance().findAll();
			for (Ticket ticket : list) {
				TicketDAO.getInstance().delete(ticket);
			}
			List<TicketItem> ticketItems = TicketItemDAO.getInstance().findAll();
			ticketItems.stream().forEach(ticketItem -> TicketItemDAO.getInstance().delete(ticketItem));
			
			List<KitchenTicket> list1 = KitchenTicketDAO.getInstance().findAll();
			for (KitchenTicket ticket : list1) {
				TicketDAO.getInstance().delete(ticket);
			}
		}
		
		if (cbMenu.isSelected())
		{
			List <MenuItem>  menuList = MenuItemDAO.getInstance().findAll();
			for (MenuItem item: menuList)
			{
				MenuItemDAO.getInstance().delete(item);
			}
			List <MenuGroup>  menuGroup = MenuGroupDAO.getInstance().findAll();
			for (MenuGroup item: menuGroup)
			{
				MenuGroupDAO.getInstance().delete(item);
			}
			List <MenuCategory>  menuCategory = MenuCategoryDAO.getInstance().findAll();
			for (MenuCategory item: menuCategory)
			{
				MenuCategoryDAO.getInstance().delete(item);
			}
			List <MenuModifier>  menuModifier = MenuModifierDAO.getInstance().findAll();
			for (MenuModifier item: menuModifier)
			{
				MenuModifierDAO.getInstance().delete(item);
			}
			List <MenuItemModifierGroup>  menuGroupModifier = MenuItemModifierGroupDAO.getInstance().findAll();
			for (MenuItemModifierGroup item: menuGroupModifier)
			{
				MenuItemModifierGroupDAO.getInstance().delete(item);
			}
		
			ModifierGroupDAO dao = new ModifierGroupDAO();
			List<MenuModifierGroup> mGroupList = dao.findAll();
			for (MenuModifierGroup item: mGroupList)
			{
				dao.delete(item);
			}
		}
		
		if(cbTax.isSelected())
		{
			List<Tax> taxList = TaxDAO.getInstance().findAll();
			for (Tax tax: taxList)
			{
				TaxDAO.getInstance().delete(tax);
			}
		}
		
		if(cbSalesReport.isSelected()) {
		  List<Salesreportdb> salesReports = SalesReportDAO.getInstance().findAll();
		  salesReports.forEach(report ->SalesReportDAO.getInstance().delete(report));
		}
		if(cbVoidReason.isSelected())
		{
			List <VoidReason> reasonList = VoidReasonDAO.getInstance().findAll();
			for (VoidReason reason: reasonList)
			{
				VoidReasonDAO.getInstance().delete(reason);
			}
		}
		if(cbLicense.isSelected())
		{
			RestaurantDAO dao = new RestaurantDAO();
			Restaurant restaurant = dao.get(Integer.valueOf(1));
			restaurant.setLicenseKey("");
			restaurant.setLicenseMac("");
			restaurant.setLicenseExpiryDate(null);
			dao.saveOrUpdate(restaurant);
		}
		
		if(cbInventoryReset.isSelected())
		{
			List <MenuItem>  menuList = MenuItemDAO.getInstance().findAll();
			for (MenuItem item: menuList)
			{
				item.setSold(0);
				item.setInstock(0);
				item.setDamaged(0);
				MenuItemDAO.getInstance().saveOrUpdate(item);
			}
		}
		return true;
	}

	@Override
	public void initialize() throws Exception {
		setInitialized(true);
		
	}
	public Configuration initializeDb() throws DatabaseConnectionException {
		try {

			return _RootDAO.reInitialize();

		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}

	}
	@Override
	public String getName() {
		return "Daten loeschen";
	}
}
