/*
 * BackOfficeWindow.java
 *
 * Created on August 16, 2006, 12:43 PM
 */

package com.floreantpos.bo.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.actions.AboutAction;
import com.floreantpos.add.service.ReprintSaleWRT;
import com.floreantpos.bo.actions.CardPaymentAction;
import com.floreantpos.bo.actions.CategoryExplorerAction;
import com.floreantpos.bo.actions.ConfigureRestaurantAction;
import com.floreantpos.bo.actions.CouponExplorerAction;
import com.floreantpos.bo.actions.DataExportAction;
import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.bo.actions.GroupExplorerAction;
import com.floreantpos.bo.actions.GutscheinExplorerAction;
import com.floreantpos.bo.actions.InventoryExplorerAction;
import com.floreantpos.bo.actions.ItemExplorerAction;
import com.floreantpos.bo.actions.MenuUsageReportAction;
import com.floreantpos.bo.actions.ModifierExplorerAction;
import com.floreantpos.bo.actions.ModifierGroupExplorerAction;
import com.floreantpos.bo.actions.SalesReportAction;
import com.floreantpos.bo.actions.TableReportAction;
import com.floreantpos.bo.actions.TaxExplorerAction;
import com.floreantpos.bo.actions.TicketExplorerAction;
import com.floreantpos.bo.actions.UserExplorerAction;
import com.floreantpos.bo.actions.UserTypeExplorerAction;
import com.floreantpos.bo.actions.WordDesign;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.UIConfig;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PropertyListDialog;
import com.floreantpos.ui.dialog.SalesReportArchiveView;
import com.floreantpos.ui.dialog.SalesResetDialog;
import com.floreantpos.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;
import com.jidesoft.swing.JideTabbedPane;
import com.khan.online.sales.ManualOnlineImportAction;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author MShahriar
 */
public class BackOfficeWindow extends javax.swing.JFrame {

	private static final String POSY = "bwy";//$NON-NLS-1$
	private static final String POSX = "bwx";//$NON-NLS-1$
	private static final String WINDOW_HEIGHT = "bwheight";//$NON-NLS-1$
	private static final String WINDOW_WIDTH = "bwwidth";//$NON-NLS-1$

	private static BackOfficeWindow instance;

	/** Creates new form BackOfficeWindow */
	public BackOfficeWindow() {
		setIconImage(Application.getApplicationIcon().getImage());

		initComponents();

		createMenus();
		positionWindow();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setMinimumSize(new Dimension(800, 600));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					close();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		setTitle(Application.getTitle() + "- " + com.floreantpos.POSConstants.BACK_OFFICE); //$NON-NLS-1$
		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
	}

	private void positionWindow() {
		int width = AppConfig.getInt(WINDOW_WIDTH, 900); // $NON-NLS-1$
		int height = AppConfig.getInt(WINDOW_HEIGHT, 650); // $NON-NLS-1$
		setSize(width, height);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - width) >> 1;
		int y = (screenSize.height - height) >> 1;

		x = AppConfig.getInt(POSX, x); // $NON-NLS-1$
		y = AppConfig.getInt(POSY, y); // $NON-NLS-1$

		setLocation(x, y);
	}

	private void createMenus() {
		User user = Application.getCurrentUser();
		UserType newUserType = user.getType();

		Set<UserPermission> permissions = null;

		if (newUserType != null) {
			permissions = newUserType.getPermissions();
		}

		JMenuBar menuBar = new JMenuBar();

		if (newUserType == null) {
			createAdminMenu(menuBar);
			createExplorerMenu(menuBar);
			createReportMenu(menuBar);
		} else {
			if (permissions != null && permissions.contains(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
				createAdminMenu(menuBar);
			}
			if (permissions != null && permissions.contains(UserPermission.VIEW_EXPLORERS)) {
				createExplorerMenu(menuBar);
			}
			if (permissions != null && permissions.contains(UserPermission.VIEW_REPORTS)) {
				createReportMenu(menuBar);
			}
		}

		createInventoryMenus(menuBar);

		JMenu helpMenu = new JMenu();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_HELP))
			helpMenu.setText(POSConstants.BACK_HELP);
		else
			helpMenu.setText("Hilfe");
		
		helpMenu.add(new AboutAction());
		menuBar.add(helpMenu);
		Font font = new Font("Times New Roman", Font.BOLD, 16);

		ImageIcon keyBoard = IconFactory.getIcon("OK");

		JButton btnOnscreenKeyboard = new JButton();
		if(keyBoard==null) {
			btnOnscreenKeyboard.setText("Keyboard");
			btnOnscreenKeyboard.setFont(new Font("Tahoma", Font.BOLD, 16));
		} else {
			btnOnscreenKeyboard.setBorderPainted(false);
			btnOnscreenKeyboard.setIcon(keyBoard);
			btnOnscreenKeyboard.setBackground(new Color(209, 222, 235));
		}
		btnOnscreenKeyboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton exitButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_CLOSE))
			exitButton.setText(POSConstants.BACK_CLOSE);
		else
			exitButton.setText("SCHLIESSEN");
		
		exitButton.setPreferredSize(new Dimension(25, 25));
		exitButton.setBackground(new Color(255, 153, 153));
		exitButton.setFont(font);
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}

		});
		menuBar.add(btnOnscreenKeyboard);
		menuBar.add(exitButton);
		menuBar.setBackground(new Color(209, 222, 235));

		UIManager.put("MenuBar.font", font);
		setJMenuBar(menuBar);
	}

	private void createInventoryMenus(JMenuBar menuBar) {
		InventoryPlugin plugin = Application.getPluginManager().getPlugin(InventoryPlugin.class);
		if (plugin == null) {
			return;
		}

		AbstractAction[] actions = plugin.getActions();
		if (actions == null) {
			return;
		}

		JMenu inventoryMenu = new JMenu("Inventory");
		for (AbstractAction abstractAction : actions) {
			inventoryMenu.add(abstractAction);
		}

		menuBar.add(inventoryMenu);
	}

	private void createReportMenu(JMenuBar menuBar) {
		JMenu reportMenu = new JMenu(com.floreantpos.POSConstants.REPORTS);
		reportMenu.add(new SalesReportAction());
		reportMenu.add(new ManualOnlineImportAction());
		//reportMenu.add(new HourlyLaborReportAction("Shift"));

		try {
			reportMenu.add(new TableReportAction("Zwisenabschluss"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuBar.add(reportMenu);
	}

	private void createExplorerMenu(JMenuBar menuBar) {
		JMenu explorerMenu = new JMenu(com.floreantpos.POSConstants.EXPLORERS);
		this.setBackground(new Color(209, 222, 235));
		menuBar.add(explorerMenu);

		explorerMenu.add(new CategoryExplorerAction());
		explorerMenu.add(new GroupExplorerAction());
		explorerMenu.add(new ItemExplorerAction());
		explorerMenu.add(new ModifierGroupExplorerAction());
		explorerMenu.add(new ModifierExplorerAction());
		explorerMenu.add(new CouponExplorerAction());
		explorerMenu.add(new TaxExplorerAction());
	}

	private void createAdminMenu(JMenuBar menuBar) {
		JMenu adminMenu = new JMenu(com.floreantpos.POSConstants.ADMIN);
		adminMenu.setBackground(new Color(209, 222, 235));
		adminMenu.add(new ConfigureRestaurantAction());
		adminMenu.add(new UserExplorerAction());
		adminMenu.add(new UserTypeExplorerAction());
		adminMenu.add(new DataExportAction());
		adminMenu.add(new DataImportAction());
		menuBar.add(adminMenu);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		mainPanel = new JPanel();
		JPanel configPanel = new JPanel();
		JPanel explorerPanel = new JPanel();
		setLayout(new MigLayout());
		configPanel.setLayout(new MigLayout());
		explorerPanel.setLayout(new MigLayout());
		mainPanel.setLayout(new MigLayout());

		configPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adminkonfigurationen"));
		explorerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Explorer"));
		JButton configButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_CONFIGURATION))
			configButton.setText(POSConstants.BACK_CONFIGURATION);
		else
			configButton.setText("Konfiguration");
		
		setFont(configButton);
		configButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ConfigureRestaurantAction("Konfiguration");
			}
		});
		configPanel.add(configButton, "growx");
		
		JButton userButton = new JButton("");
		if(StringUtils.isNotEmpty(POSConstants.BACK_USER))
			userButton.setText(POSConstants.BACK_USER);
		else
			userButton.setText("BENUTZER");
		
		
		setFont(userButton);
		userButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new UserExplorerAction("Benutzer");
			}
		});
		configPanel.add(userButton, "growx");

		JButton exportMenuButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_PRODUCT_EXPORT))
			exportMenuButton.setText(POSConstants.BACK_PRODUCT_EXPORT);
		else
			exportMenuButton.setText("PRODUKT EXPORT");
		
		setFont(exportMenuButton);
		exportMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new DataExportAction("Export Menu");
			}
		});
		configPanel.add(exportMenuButton, "growx");
		
		JButton importMenuButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_PRODUCT_IMPORT))
			importMenuButton.setText(POSConstants.BACK_PRODUCT_IMPORT);
		else
			importMenuButton.setText("PRODUKT IMPORT");
		
		setFont(importMenuButton);
		importMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new DataImportAction("Import Menu");
			}

		});
		configPanel.add(importMenuButton, "growx");
		configPanel.setBackground(new Color(209, 222, 235));

		JButton wordConfigButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_CONFIG_TEXT))
			wordConfigButton.setText(POSConstants.BACK_CONFIG_TEXT);
		else
			wordConfigButton.setText("KONFIG TEXT");
		
		setFont(wordConfigButton);
		wordConfigButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WordDesign dialog = new WordDesign("Konfig Text");
				dialog.pack();
				dialog.open();
			}
		});

		configPanel.add(wordConfigButton, "growx,wrap");

		JButton ticketButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_BILLS))
			ticketButton.setText(POSConstants.BACK_BILLS);
		else
			ticketButton.setText("RECHNUNGEN");
		
		setFont(ticketButton);
		ticketButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new TicketExplorerAction("Rechnungen");

			}
		});

		configPanel.add(ticketButton, "growx");

		JButton salesReportReset = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_ABSRESET))
			salesReportReset.setText(POSConstants.BACK_ABSRESET);
		else
			salesReportReset.setText("ABSCHLUSS RESET");
		
		setFont(salesReportReset);
		salesReportReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SalesResetDialog dialog = new SalesResetDialog();
				dialog.setPreferredSize(new Dimension(800, 600));
				dialog.pack();
				dialog.open();

			}
		});


		JButton salesReportResetPrint = new JButton("ABSCHLUSS BEARBEITEN");
		
		
		setFont(salesReportResetPrint);
		salesReportResetPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ReprintSaleWRT dialog = new ReprintSaleWRT();
				dialog.setPreferredSize(new Dimension(800, 600));
				dialog.pack();
				dialog.open();

			}
		});

		JButton salesReportButton = new JButton("ABSCHLUSS ARCHIV");
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_ABS_ARCHIV))
			salesReportButton.setText(POSConstants.BACK_ABS_ARCHIV);
		else
			salesReportButton.setText("ABSCHLUSS ARCHIV");
		
		setFont(salesReportButton);
		salesReportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SalesReportArchiveView dialog = new SalesReportArchiveView();
				dialog.setPreferredSize(new Dimension(800, 600));
				dialog.pack();
				dialog.open();
			}
		});
		configPanel.add(salesReportButton, "growx");

		JButton gdpdu = new JButton("GDPDU");
		setFont(gdpdu);
		gdpdu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					showGdpdu();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		configPanel.add(gdpdu, "growx");
		
		
		JButton menuusage = new JButton("VERKAUFSSTATISTIK");
		if(StringUtils.isNotEmpty(POSConstants.VERKAUFSSTATISTIK))
			menuusage.setText(POSConstants.VERKAUFSSTATISTIK);
		
		setFont(menuusage);
		menuusage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new MenuUsageReportAction("Menuestatistik");

			}
		});

		configPanel.add(menuusage, "growx");

		JButton cardPayments = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_ZAHLUNGABS))
			cardPayments.setText(POSConstants.BACK_ZAHLUNGABS);
		else
			cardPayments.setText("ZAHLUNGABSCHLUSS");
		
		setFont(cardPayments);
		cardPayments.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new CardPaymentAction("Kartenabschluss");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		configPanel.add(cardPayments, "growx, wrap");
		User currentUser = Application.getCurrentUser();
		if(currentUser.getFirstName().equals("Master")) {
			configPanel.add(salesReportResetPrint, "growx, wrap");
		} else {
			configPanel.add(salesReportReset, "growx");
		}

		JButton categoryButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_CATEGORIES))
			categoryButton.setText(POSConstants.BACK_CATEGORIES);
		else
			categoryButton.setText("KATEGORIEN");
		
		setFont(categoryButton);
		categoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new CategoryExplorerAction("Menu Kategorien");

			}

		});
		explorerPanel.add(categoryButton, "growx");
		JButton groupButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_GROUPS))
			groupButton.setText(POSConstants.BACK_GROUPS);
		else
			groupButton.setText("GRUPPEN");
		
		setFont(groupButton);
		groupButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new GroupExplorerAction("Produkt Gruppen");

			}

		});
		explorerPanel.add(groupButton, "growx");
		JButton itemButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_PRODUCT))
			itemButton.setText(POSConstants.BACK_PRODUCT);
		else
			itemButton.setText("PRODUKT");
		
		setFont(itemButton);
		itemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ItemExplorerAction("Produkt");
			}
		});
		
		JButton ausWahlButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_AUSWAHL))
			ausWahlButton.setText(POSConstants.BACK_AUSWAHL);
		else
			ausWahlButton.setText("  AUSWAHL ");
		
		setFont(ausWahlButton);
		ausWahlButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PropertyListDialog dialog = new PropertyListDialog();
				dialog.pack();
				dialog.open();
			}
		});
		
		explorerPanel.add(itemButton, "growx");
		explorerPanel.add(ausWahlButton, "growx");
		JButton modifierGroupButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_EXTRA_GROUPS))
			modifierGroupButton.setText(POSConstants.BACK_EXTRA_GROUPS);
		else
			modifierGroupButton.setText("EXTRA GRUPPEN");
		
		setFont(modifierGroupButton);
		modifierGroupButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ModifierGroupExplorerAction("Extra");

			}

		});
		explorerPanel.add(modifierGroupButton, "growx");
		JButton modifierButton = new JButton("EXTRAS");
		setFont(modifierButton);
		modifierButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ModifierExplorerAction("Extra");
			}
		});
		explorerPanel.add(modifierButton, "growx");

		JButton couponButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_RABATTE))
			couponButton.setText(POSConstants.BACK_RABATTE);
		else
			couponButton.setText("RABATTE");
		
		setFont(couponButton);
		couponButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new CouponExplorerAction("Rabatte");
			}
		});
		explorerPanel.add(couponButton, "growx,wrap");

		JButton taxButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_STEUERN))
			taxButton.setText(POSConstants.BACK_STEUERN);
		else
			taxButton.setText("STEUERN");
		
		setFont(taxButton);
		taxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new TaxExplorerAction("Steuer");
			}
		});
		explorerPanel.add(taxButton, "growx");

		JButton customerButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_USER))
			customerButton.setText(POSConstants.BACK_USER);
		else
			customerButton.setText("KUNDEN");
		
		setFont(customerButton);
		customerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OrderServiceExtension plugin = Application.getCustomerPlugin();
				if (plugin == null) {
					return;
				}

				plugin.createCustomerButton();

			}
		});
		
		JButton gutscheinButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_GUTSCHEIN))
			gutscheinButton.setText(POSConstants.BACK_GUTSCHEIN);
		else
			gutscheinButton.setText("GUTSCHEIN");
		
		setFont(gutscheinButton);
		gutscheinButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new GutscheinExplorerAction("Gutschein");
			}
		});
		
		explorerPanel.add(customerButton, "growx");
		JButton inventoryButton = new JButton();
		
		if(StringUtils.isNotEmpty(POSConstants.BACK_INVENTUR))
			inventoryButton.setText(POSConstants.BACK_INVENTUR);
		else
			inventoryButton.setText("INVENTUR");
		
		setFont(inventoryButton);
		inventoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new InventoryExplorerAction("INVENTUR");
			}
		});
		
		
		explorerPanel.add(inventoryButton, "growx");
		
		explorerPanel.add(gutscheinButton, "growx");	

		mainPanel.setBackground(new Color(209, 222, 235));
		mainPanel.add(configPanel, "wrap");
		mainPanel.add(explorerPanel, "wrap");
		explorerPanel.setBackground(new Color(209, 222, 235));

		jPanel1.setBackground(new Color(209, 222, 235));
		tabbedPane = new JideTabbedPane();
		tabbedPane.setBackground(new Color(209, 222, 235));
		tabbedPane.setTabShape(JideTabbedPane.SHAPE_WINDOWS);
		tabbedPane.setShowCloseButtonOnTab(true);
		tabbedPane.setTabInsets(new Insets(6, 6, 6, 6));
		getContentPane().setLayout(new java.awt.BorderLayout(6, 0));
		tabbedPane.setBackground(new Color(243, 249, 255));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jPanel1.setLayout(new java.awt.BorderLayout(8, 0));

		jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel1.add(tabbedPane, java.awt.BorderLayout.CENTER);
		tabbedPane.setMinimumSize(new Dimension(800, 600));
		jPanel1.setBackground(new Color(209, 222, 235));
		getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		getContentPane().setBackground(new Color(209, 222, 235));
		pack();
	}// </editor-fold>//GEN-END:initComponents

	public void setFont(JButton button) {
		button.setFont(UIConfig.getButtonFont());
		button.setPreferredSize(new Dimension(120, 50));
		button.setBackground(Color.getHSBColor(159, 63, 90));
	}

	/**
	 * @param args
	 *          the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BackOfficeWindow().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel mainPanel;
	private JideTabbedPane tabbedPane;

	// End of variables declaration//GEN-END:variables

	public javax.swing.JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void showGdpdu() throws IOException {

		SimpleDateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");
		File directory = new File("gdpdu");
		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}
		String mainDirectory = df1.format(new Date());
		directory = new File("gdpdu/" + mainDirectory);
		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}

		boolean failed = false;
		if (!GdpduTicket(directory.getPath())) {
			failed = true;
		}
		if (!GdpduTicketItem(directory.getPath())) {
			failed = true;
		}
		if (!GdpduMenuItem(directory.getPath())) {
			failed = true;
		}

		if (!GdpduSalesReport(directory.getPath())) {
			failed = true;
		}
		if (!failed) {
			JOptionPane.showMessageDialog(null, POSConstants.FINISH);
		}
		// GdpduGroupStatistics();
	}

	public boolean GdpduTicket(String dir) throws IOException {

		System.out.println(dir);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date startDate;
		try {
			startDate = df.parse(TerminalConfig.getStartDate());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError("Fehler");
			return false;
		}
		Session session = TicketDAO.getInstance().createNewSession();
		Transaction tx = session.beginTransaction();

		startDate = BusinessDateUtil.startOfOfficialDay(startDate);
		Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());

		FileWriter writer = new FileWriter(dir + "/Rechnungen.csv");
		writer.append("Id");
		writer.append(';');
		writer.append("Kellner");
		writer.append(';');
		writer.append("Zeit_Erstellt");
		writer.append(';');
		writer.append("Type");
		writer.append(';');
		writer.append("Gesamt");
		writer.append(';');
		writer.append("Status");
		writer.append(';');
		writer.append("Hash");
		writer.append(';');
		writer.append('\n');
		List<Ticket> tickets = TicketDAO.getInstance().findGdpduTickets(startDate, endDate);

		List<Ticket> newTickets = TicketDAO.getInstance().findGdpduTicketsNoSrToday();
		if (newTickets != null && !newTickets.isEmpty()) {
			tickets.addAll(newTickets);
		}

		Collections.sort(tickets, new Ticket.ItemComparator());
		Integer index = 1;
		for (Ticket ticket : tickets) {
			writer.append(index + "");

			ticket.setGdpduid(index);
			writer.append(';');
			User owner = ticket.getOwner();
			writer.append(owner != null ? owner.getFirstName() : StringUtils.EMPTY);
			writer.append(';');
			SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			String createDate = ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : StringUtils.EMPTY;
			writer.append(createDate);
			writer.append(';');
			writer.append(ticket.getType() == TicketType.DINE_IN ? POSConstants.DINE_IN : POSConstants.HOME_DELIVERY);
			writer.append(';');
			String totalAmount = NumberUtil.formatNumber(ticket.getTotalAmount());
			writer.append(NumberUtil.formatNumber(ticket.getTotalAmount()));
			writer.append(';');
			String status = "";
			if (ticket.isPaid()) {
				if (ticket.getCashPayment() != null && ticket.getCashPayment())
					status = POSConstants.CASH_PAID;
				else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment())
					status = POSConstants.CARD_PAID;
				else if (ticket.getCashPayment() != null && !ticket.getCashPayment())
					status = POSConstants.ONLINE_PAID;
			} else {
				status = POSConstants.NOT_PAID;
			}
			writer.append(status);
			writer.append(';');
			String hash = md5(String.valueOf(index + createDate + totalAmount));
			writer.append(hash);
			writer.append(';');
			writer.append('\n');
			session.saveOrUpdate(ticket);
			index++;
		}
		writer.flush();
		writer.close();
		tx.commit();
		session.close();
		return true;
	}

	private String md5(String object) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (md != null) {
			md.update(object.getBytes());
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest).toUpperCase();
		}
		return StringUtils.EMPTY;
	}

	public boolean GdpduTicketItem(String dir) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Restaurant restaurant = RestaurantDAO.getRestaurant();
			Date startDate;
			try {
				startDate = df.parse(TerminalConfig.getStartDate());
			} catch (Exception e) {
				e.printStackTrace();
				POSMessageDialog.showError("Fehler");
				return false;
			}
			startDate = BusinessDateUtil.startOfOfficialDay(startDate);
			Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
			SimpleDateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");
			String str = df1.format(startDate) + "_bis_" + df.format(endDate);
			FileWriter writer = new FileWriter(dir + "/Artikel.csv");
			writer.append("Id");
			writer.append(';');
			writer.append("Name");
			writer.append(';');
			writer.append("Gruppe");
			writer.append(';');
			writer.append("Kategorie");
			writer.append(';');
			writer.append("Zeit erstellt");
			writer.append(';');
			writer.append("Steuer");
			writer.append(';');
			writer.append("Einzelpreis");
			writer.append(';');
			writer.append("Anz.");
			writer.append(';');
			writer.append("Gesamt");
			writer.append(';');
			writer.append('\n');

			List<TicketItem> ticketItems = TicketItemDAO.getInstance().findAllByDates(startDate, endDate);
			Collections.sort(ticketItems, new TicketItem.DateComparator());
			for (TicketItem ticketItem : ticketItems) {
				Ticket ticket = ticketItem.getTicket();
				if (ticket == null || ticket.getGdpduid() == 0) {
					continue;
				}
				if (ticket != null && ticket.getGdpduid() != null) {
					writer.append(ticket.getGdpduid() + "");
					writer.append(';');
				}
				writer.append(ticketItem.getName());
				writer.append(';');
				writer.append(ticketItem.getGroupName() != null ? ticketItem.getGroupName() : StringUtils.EMPTY);
				writer.append(';');
				writer.append(ticketItem.getCategoryName() != null ? ticketItem.getCategoryName() : StringUtils.EMPTY);
				writer.append(';');
				String date = null;
				if (ticketItem.getModifiedTime() != null) {
					SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					date = ticketItem.getCreateDate() == null
							? ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : null
									: dt.format(ticketItem.getCreateDate());
				}
				writer.append(date != null ? date : "");
				writer.append(';');

				writer.append(NumberUtil.formatNumber(ticketItem.getTaxRate()));
				writer.append(';');
				writer.append(NumberUtil.formatNumber(ticketItem.getUnitPrice()));
				writer.append(';');
				writer.append(ticketItem.getItemCount() + "");
				writer.append(';');
				writer.append(NumberUtil.formatNumber(ticketItem.getTotalAmount()));
				writer.append(';');
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (Exception xpxw) {
			xpxw.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean GdpduMenuItem(String dir) throws IOException {
		FileWriter writer = new FileWriter(dir + "/Speisekarte.csv");
		writer.append("Artikel Id");
		writer.append(';');
		writer.append("Name");
		writer.append(';');
		writer.append("Preis");
		writer.append(';');
		writer.append("Steuer");
		writer.append(';');
		writer.append("Kategorie");
		writer.append(';');
		writer.append("Gruppe");
		writer.append(';');
		writer.append('\n');

		List<MenuItem> menuitems = MenuItemDAO.getInstance().findAll();
		for (MenuItem item : menuitems) {
			writer.append(item.getItemId());
			writer.append(';');
			writer.append(item.getName());
			writer.append(';');
			writer.append(NumberUtil.formatNumber(item.getPrice()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(item.getTax().getRate()));
			writer.append(';');
			MenuCategory category = null;
			MenuGroup group = item.getParent();
			if (group != null) {
				category = group.getParent();
			}
			writer.append(category != null ? category.getName() : StringUtils.EMPTY);
			writer.append(';');
			writer.append(group != null ? group.getName() : StringUtils.EMPTY);
			writer.append(';');
			writer.append('\n');
		}
		writer.flush();
		writer.close();
		return true;
	}

	public boolean GdpduSalesReport(String dir) throws IOException {
		FileWriter writer = new FileWriter(dir + "/Tagesbericht.csv");
		writer.append("Id");
		writer.append(';');
		writer.append("von");
		writer.append(';');
		writer.append("bis");
		writer.append(';');
		writer.append("Datum");
		writer.append(';');
		writer.append("Speisen+Getraenke");
		writer.append(';');
		writer.append("Speisen+Getraenke Mwst.");
		writer.append(';');
		writer.append("Umsatz Gesamt");
		writer.append(';');
		writer.append("Mwst 19%");
		writer.append(';');
		writer.append("Mwst 7%");
		writer.append(';');
		writer.append("Bar bezahlt");
		writer.append(';');
		writer.append("Bar bezahlt Mwst.");
		writer.append(';');
		writer.append("EC karte");
		writer.append(';');
		writer.append("EC karte Mwst.");
		writer.append(';');
		writer.append("Online");
		writer.append(';');
		writer.append("Online Mwst.");
		writer.append(';');
		writer.append('\n');
		List<Salesreportdb> saleslist = SalesReportDAO.getInstance().findAll();
		for (Salesreportdb salesreport : saleslist) {
			Double totalfooddrinks = salesreport.getFood() + salesreport.getBeverage();
			Double totalfooddrinkstax = salesreport.getFoodtax() + salesreport.getBeveragetax();
			writer.append(salesreport.getSalesid() + "");
			writer.append(';');

			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
			String startdate = dateTimeFormatter.format(salesreport.getStartdate());
			String enddate = dateTimeFormatter.format(salesreport.getEnddate());

			writer.append(startdate);
			writer.append(';');
			writer.append(enddate);
			writer.append(';');
			writer.append(salesreport.getReporttime());
			writer.append(';');
			writer.append(NumberUtil.formatNumber(totalfooddrinks));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(totalfooddrinkstax));
			writer.append(';');

			writer.append(NumberUtil.formatNumber(salesreport.getAwt()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getAwtn()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getAwts()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCashpayment()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCashtax()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCardpayment()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCardtax()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getOnline()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getOnlinetax()));
			writer.append(';');
			writer.append('\n');
		}
		writer.flush();
		writer.close();
		return true;
	}


	private void saveSizeAndLocation() {
		AppConfig.putInt(WINDOW_WIDTH, BackOfficeWindow.this.getWidth());
		AppConfig.putInt(WINDOW_HEIGHT, BackOfficeWindow.this.getHeight()); // $NON-NLS-1$
		AppConfig.putInt(POSX, BackOfficeWindow.this.getX()); // $NON-NLS-1$
		AppConfig.putInt(POSY, BackOfficeWindow.this.getY()); // $NON-NLS-1$
	}

	public void close() {
		saveSizeAndLocation();
		instance = null;
		dispose();
	}

	public static BackOfficeWindow getInstance() {
		if (instance == null) {
			instance = new BackOfficeWindow();
			Application.getInstance().setBackOfficeWindow(instance);
		}

		return instance;
	}

}
