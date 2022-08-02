/*
 * FoodItemEditor.java
 *
 * Created on August 2, 2006, 10:34 PM
 */

package com.floreantpos.ui.model;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemPrice;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.services.UtilityService;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleDocument;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IUpdatebleView;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;

import com.floreantpos.ui.views.ColorPanel;
import com.floreantpos.ui.views.LightColorPanel;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author MShahriar
 */
public class MenuItemForm extends BeanEditor<MenuItem> implements
ActionListener, ChangeListener {
	// ShiftTableModel shiftTableModel;

	ColorPanel colorPanel = new ColorPanel();

	LightColorPanel foreColorPanel = new LightColorPanel();

	/** Creates new form FoodItemEditor */
	public MenuItem menuItem;
	
	public MenuItem getMenuItem() {
		return menuItem;
	}

	public MenuItemForm() throws Exception {		
		this(new MenuItem());
	}

	public static MenuItemForm mForm;
	public static MenuItemForm getmForm() {
		if(mForm==null)
			try {
				mForm = new MenuItemForm();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			try {
				mForm = new MenuItemForm();
			} catch (Exception e) {
				e.printStackTrace();
			}
			setId();	
		}

		return mForm;
	}

	protected void doSelectImageFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "tif"));

		int returnVal=fileChooser.showOpenDialog(null);
		File f=fileChooser.getSelectedFile();
		if(returnVal==JFileChooser.APPROVE_OPTION)
		{
			if(f.getName().endsWith(".jpg")||f.getName().endsWith(".gif")||f.getName().endsWith(".png"))
			{
				File imageFile = fileChooser.getSelectedFile();
				try {
					byte[] itemImage = FileUtils.readFileToByteArray(imageFile);
					int imageSize = itemImage.length / 1024;

					if (imageSize > 20) {
						Image img = 	  ImageIO.read(imageFile);
						BufferedImage bi = resizeImage(img, 150, 150, true);
						File outputfile = new File(imageFile.getName()+"_Converted");
						ImageIO.write(bi, "jpg", outputfile);
						itemImage = FileUtils.readFileToByteArray(outputfile);          
					}

					ImageIcon imageIcon = new ImageIcon(new ImageIcon(itemImage).getImage()
							.getScaledInstance(100, 70, Image.SCALE_SMOOTH));
					lblImagePreview.setIcon(imageIcon);

					MenuItem menuItem = getBean();
					menuItem.setImage(itemImage);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				JOptionPane.showMessageDialog(null,"select .jpg,.bmp or. gif");
		}
	}

	protected BufferedImage resizeImage(Image originalImage, 
			int scaledWidth, int scaledHeight, 
			boolean preserveAlpha)
	{

		System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
		g.dispose();
		return scaledBI;
	}

	protected void doClearImage() {
		MenuItem menuItem = getBean();
		menuItem.setImage(null);
		lblImagePreview.setIcon(null);
	}

	public static void setId() {		
		id1 = restaurant.getArtikelnummer() != null ? restaurant.getArtikelnummer() : 1;
		tfItemId.setText(Integer.toString(id1));
		tfName.setText("Artikel Name");
		tfBarcode.setText("");
		tfPrice.setText("0.00");
		cbCategory.setSelectedIndex(category);
		cbGroup.setSelectedIndex(group);
		cbTax.setSelectedIndex(tax);
	}

	public static int category;
	public static int group;
	public static int tax;
	boolean simpleForm;
	public MenuItemForm(MenuItem menuItem1) throws Exception {
		this.menuItem = menuItem1;
		this.simpleForm = false;
		init();
	}

	public MenuItemForm(MenuItem menuItem1, boolean simpleForm) throws Exception {
		this.menuItem = menuItem1;
		this.simpleForm = simpleForm;
		init();
	}

	public void init()throws Exception {

		initComponents();
		tfName.setDocument(new FixedLengthDocument(70));
		tfLName.setDocument(new FixedLengthDocument(70));
		tfItemId.setDocument(new FixedLengthDocument(30));
		tfPrice.setDocument(new FixedLengthDocument(30));
		tfPrice_.setDocument(new FixedLengthDocument(30));

		list = new ArrayList<String>();
		list.add(POSConstants.DINE_IN);
		list.add(POSConstants.HOME_DELIVERY);

		List<String> taxes = new ArrayList<String>(); 
		taxes.add(OrderView.getTaxHomeDelivery()+"%");
		taxes.add(OrderView.getTaxDineIn()+"%");
		taxes.add("0%");
		cbTax = new JComboBox();
		cbTax.setBackground(Color.WHITE);
		for (Iterator<String> itr = taxes.iterator(); itr.hasNext();) {			
			cbTax.addItem(itr.next());
		}

		/*cbTax.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        List<MenuCategory> categoryList;
        List<MenuCategory> visibleList = new ArrayList();
        if (cbTax.getSelectedIndex() == 1)
          categoryList = MenuCategoryDAO.getInstance()
              .findByCategory("IM HAUS");
        else if (cbTax.getSelectedIndex() == 0)
          categoryList = MenuCategoryDAO.getInstance().findByCategory(
              "LIEFERUNG");
        else
          categoryList = MenuCategoryDAO.getInstance().findByCategory("ZERO");

        for (MenuCategory category : categoryList) {
          if (category.isVisible())
            visibleList.add(category);
        }
        cbCategory.setModel(new ComboBoxModel(visibleList));
      }
    });*/

		if(menuItem.getTax() != null) {
			System.out.println("menuItem.getTax(): " + menuItem.getTax() );
			cbTax.setSelectedItem(menuItem.getTax());
		}

		List<MenuCategory> categoryList = MenuCategoryDAO.getInstance().findAll();
		cbCategory.setModel(new ComboBoxModel(categoryList));
		foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));
		cbCategory.setBackground(Color.WHITE);
		cbCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuCategory category = (MenuCategory) cbCategory.getSelectedItem();
				List<MenuGroup> foodGroups = foodGroupDAO.findByParent(category);
				cbGroup.setModel(new ComboBoxModel(foodGroups));
				if(cbCategory.getSelectedItem().toString().equals("Lebensmittel")||cbCategory.getSelectedItem().toString().equals("Gemüse")||
						cbCategory.getSelectedItem().toString().equals("Angebot")||cbCategory.getSelectedItem().toString().equals("Tiefkühl")) {
					cbTax.setSelectedIndex(0);
				} else {
					cbTax.setSelectedIndex(1);
				}
			}
		});

		/*
		 * cbGroupType = new JComboBox(); for (Iterator<String> itr =
		 * list.iterator(); itr.hasNext();) cbGroupType.addItem(itr.next());
		 * 
		 * foodGroupDAO = new MenuGroupDAO();
		 * 
		 * 
		 * cbGroupType.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { List<MenuGroup> foodGroups =
		 * foodGroupDAO.findByGroupType(cbGroupType.getSelectedItem().toString());
		 * cbGroup.setModel(new ComboBoxModel(foodGroups)); } });
		 */
		menuItemModifierGroups = menuItem.getMenuItemModiferGroups();

		menuItemMGListModel = new MenuItemMGListModel();

		tableTicketItemModifierGroups.setModel(menuItemMGListModel);
		tableTicketItemModifierGroups.setBackground(Color.WHITE);
		// shiftTable.setModel(shiftTableModel = new
		// ShiftTableModel(menuItem.getShifts()));
		
		Hibernate.initialize(menuItem);
		menuItemPriceList = menuItem.getMenuitemprice();
		 

		btnNewModifierGroup.addActionListener(this);
		btnEditModifierGroup.addActionListener(this);
		btnDeleteModifierGroup.addActionListener(this);
		
		btnNewMenuItemPrice.addActionListener(this);
		btnEditMenuItemPrice.addActionListener(this);
		btnDeleteMenuItemPrice.addActionListener(this);
		
		menuItemPriceModel = new MenuItemPriceModel();
		tableMenuItemPrice.setModel(menuItemPriceModel);
		tableMenuItemPrice.setBackground(Color.WHITE);
		
		btnAddShift.addActionListener(this);
		btnDeleteShift.addActionListener(this);

		tfDiscountRate.setDocument(new DoubleDocument());
		jPanel1.setLayout(new MigLayout("", "[104px][100px,grow][][49px]",
				"[19px][][25px][][19px][19px][][][25px][][15px]"));
		lblItemId = new JLabel("Produkt. Id");
		jPanel1.add(lblItemId, "cell 0 0"); //$NON-NLS-1$

		jPanel1.add(tfItemId, "cell 1 0,growx,aligny top");
		tfBuyPrice = new DoubleTextField(5);
		tfBuyPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		tfBuyPrice.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(!tfBuyPrice.getText().equals("0.00"))
					tfBuyPrice.setText(blockEntry(tfBuyPrice.getText()));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(!tfBuyPrice.getText().equals("0.00"))
					tfBuyPrice.setText(blockEntry(tfBuyPrice.getText()));
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(!tfBuyPrice.getText().equals("0.00"))
					tfBuyPrice.setText(blockEntry(tfBuyPrice.getText()));
			}
		});

		cbPfand = new JCheckBox("Pfand");
		cbPfand.setEnabled(false);
		cbPfand.setBackground(Color.WHITE);
		// jPanel1.add(tfBuyPrice, "cell 1 4,growx");

		jPanel1.add(jLabel3, "cell 0 7,alignx left,aligny center");
		jPanel1.add(jLabel3_, "cell 0 22,alignx left,aligny center");
		// jPanel1.add(lblGroupType, "cell 0 2,alignx left,aligny center");
		jPanel1.add(jLabel4, "cell 0 6,alignx left,aligny center");

		JLabel lblImage = new JLabel("Bild:");
		if(StringUtils.isNotEmpty(POSConstants.Bild))
			lblImage.setText(POSConstants.Bild);
		
		lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
		setLayout(new BorderLayout(0, 0));

		lblImagePreview = new JLabel("");
		lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
		lblImagePreview
		.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblImagePreview.setPreferredSize(new Dimension(60, 120));

		JButton btnSelectImage = new JButton("Select");
		btnSelectImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSelectImageFile();
			}
		});

		btnClearImage = new JButton("Loeschen");
		
		if(StringUtils.isNotEmpty(POSConstants.DELETE))
			btnClearImage.setText(POSConstants.DELETE);
		
		btnClearImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doClearImage();
			}
		});

		cbShowTextWithImage = new JCheckBox("Nur Bilder anzeigen");
		if(StringUtils.isNotEmpty(POSConstants.Bilder_anzeigen))
			cbShowTextWithImage.setText(POSConstants.Bilder_anzeigen);
		
		cbShowTextWithImage.setBackground(new Color(209, 222, 235));
		cbShowTextWithImage.setActionCommand("Zeigen Text mit Bild");
		jPanel1.add(jLabel2, "cell 0 9,alignx left,aligny center"); //$NON-NLS-1$
		jPanel1.add(jLabel1, "cell 0 1,alignx left,aligny center"); //$NON-NLS-1$
		jPanel1.add(jLabel1_, "cell 0 20,alignx left,aligny center"); //$NON-NLS-1$
		jPanel1.add(tfName, "cell 1 1 2 0,growx,aligny top"); //$NON-NLS-1$
		jPanel1.add(tfLName, "cell 1 20 0 0,growx,aligny top"); //$NON-NLS-1$
		
		if(StringUtils.isNotEmpty(POSConstants.CATEGORY))
			jPanel1.add(new JLabel(POSConstants.CATEGORY), "cell 0 4,growx,aligny top");
		else
		   jPanel1.add(new JLabel("Kategorie"), "cell 0 4,growx,aligny top");
		
		jPanel1.add(cbCategory, "cell 1 4,growx,aligny top");
		jPanel1.add(btnNewCategory, "cell 2 4,,alignx right,growx"); 
		if(TerminalConfig.isSupermarket()) {
			
			if(StringUtils.isNotEmpty(POSConstants.Steuer))
				jPanel1.add(new JLabel(POSConstants.Steuer), "cell 0 5,alignx left,aligny top"); //$NON-NLS-1$
			else
			jPanel1.add(new JLabel("Steuer"), "cell 0 5,alignx left,aligny top"); //$NON-NLS-1$
			
			jPanel1.add(cbTax, "cell 1 5,alignx left,growx"); //$NON-NLS-1$
			//			jPanel1.add(btnNewTax, "cell 2 5,alignx right,growx"); //$NON-NLS-1$
		}
		jPanel1.add(cbGroup, "cell 1 6,growx,aligny top"); //$NON-NLS-1$
		cbGroup.setBackground(Color.WHITE);
		jPanel1.add(btnNewGroup, "cell 2 6,alignx right,growx"); //$NON-NLS-1$
		//jPanel1.add(tfDiscountRate, "cell 1 9,growx,aligny top"); //$NON-NLS-1$
		jPanel1.add(tfPrice, "cell 1 7,growx,aligny top"); //$NON-NLS-1$
		jPanel1.add(tfPrice_, "cell 1 22,growx,aligny top"); //$NON-NLS-1$
		cbPrinter = new JComboBox<VirtualPrinter>(
				new DefaultComboBoxModel<VirtualPrinter>(VirtualPrinterDAO
						.getInstance().findAll().toArray(new VirtualPrinter[0])));
		//jPanel1.add(cbPrinter, "cell 1 10,growx"); //$NON-NLS-1$
		jPanel1.add(chkVisible, "cell 1 11,alignx left,aligny top"); //$NON-NLS-1$
		chkVisible.setBackground(new Color(209, 222, 235));
		jPanel1.add(chkChangePrice, "cell 2 11,alignx left,aligny top"); //$NON-NLS-1$
		chkChangePrice.setBackground(new Color(209, 222, 235));
		chkVisible.setBackground(new Color(209, 222, 235));
		setBackground(new Color(209, 222, 235));
		//		jPanel1.add(jLabel5, "cell 2 6"); //$NON-NLS-1$
		tfWeight = new com.floreantpos.swing.FixedLengthTextField();
		tfSubartid = new com.floreantpos.swing.FixedLengthTextField();
		tfSubartid.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				changeListener();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				changeListener();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				changeListener();
			}
		});
		
		if(StringUtils.isNotEmpty(POSConstants.Gewicht))
			jPanel1.add(new JLabel(POSConstants.Gewicht+" (grams)"), "cell 0 12, aligny top");
		else 
		    jPanel1.add(new JLabel("Gewicht (grams)"), "cell 0 12, aligny top");
		
		jPanel1.add(tfWeight, "cell 1 12, growx,aligny top");

		jPanel1.add(new JLabel("Sub Pro.-id"), "cell 0 13, aligny top");
		jPanel1.add(tfSubartid, "cell 1 13, growx,aligny top");
		lblCheck = new JLabel();
		lblCheck.setText("");
		lblCheck.setForeground(Color.RED);
		lblCheck.setFont(new Font("Times New Roman", Font.BOLD, 14));
		jPanel1.add(lblCheck, "cell 2 13, aligny top");

		JLabel lblDescription = new JLabel("Beschreibung");		
		if(StringUtils.isNotEmpty(POSConstants.Beschreibung))
			lblDescription.setText(POSConstants.Beschreibung);
		
		jPanel1.add(lblDescription, "cell 0 14, aligny top");
		jPanel1.add(tfDescription, "cell 1 14, growx");
		
		JLabel lblDescription2 = new JLabel("Beschreibung2");		
		if(StringUtils.isNotEmpty(POSConstants.Beschreibung))
			lblDescription2.setText(POSConstants.Beschreibung+"2");
		
		jPanel1.add(lblDescription2, "cell 0 23, aligny top");
		jPanel1.add(tfDescription2, "cell 1 23, growx");

		JLabel lblNote = new JLabel("Bemerkung");
		if(StringUtils.isNotEmpty(POSConstants.Bemerkung))
			lblNote.setText(POSConstants.Bemerkung);
		
		jPanel1.add(lblNote, "cell 0 15, aligny top");
		jPanel1.add(tfNote, "cell 1 15, growx");
		lblBarcode = new JLabel(Messages.getString("MenuItemForm.lblBarcode.text")); //$NON-NLS-1$
		jPanel1.add(lblBarcode, "cell 0 16,alignx leading");

		tfBarcode = new FixedLengthTextField(120);
		jPanel1.add(tfBarcode, "cell 1 16,growx");

		JButton generateButton = new JButton("Generieren");
		if(StringUtils.isNotEmpty(POSConstants.Generieren))
			generateButton.setText(POSConstants.Generieren);
		
		generateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int randomNumber = 0;
				while (true) {
					Random randomGenerator = new Random();
					randomNumber = randomGenerator.nextInt(2147483640);
					if (randomNumber < 0)
						continue;
					if (MenuItemDAO.getInstance().findByBarcode(randomNumber + "").size() > 0)
						continue;
					else
						break;
				}
				tfBarcode.setText(randomNumber + "");
			}

		});
		jPanel1.add(generateButton, "cell 2 16,growx");

		JButton printBarcode = new JButton("Drucken");
		if(StringUtils.isNotEmpty(POSConstants.PRINT))
			printBarcode.setText(POSConstants.PRINT);
		
		printBarcode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (tfBarcode.getText().length() <= 0)
					return;
				printBarcodLevel(menuItem);
			}
		});
		jPanel1.add(printBarcode, "cell 3 16,growx");
				
		imagePanel.setBackground(new Color(209, 222, 235));
		imagePanel.setLayout(new MigLayout());
		imagePanel.add(lblImage, "growx");
		imagePanel.add(lblImagePreview, "growx");
		imagePanel.add(cbShowTextWithImage, "growx, wrap");
		imagePanel.add(btnSelectImage, "growx");
		imagePanel.add(btnClearImage);
		if(!simpleForm)
			tabbedPane.add("Image", imagePanel);

		cbType = new javax.swing.JComboBox();
		cbType.setBackground(Color.WHITE);  
		cbType.addItem(String.valueOf(1));
		if(TerminalConfig.isPriceCategoryKunden()) {
			List<Customer> CustList = CustomerDAO.getInstance().findAll();
			if(CustList!=null) {
				for (Customer cust:CustList) {
					try {
						if(cust.getLoyaltyNo()!=null)
							cbType.addItem(cust.getLoyaltyNo());
					}catch(Exception ex) {

					}

				}	
			}
		}else if(TerminalConfig.isPriceCategory()) {			
			for (int i=2; i<=20;i++) {
				try {
					cbType.addItem(String.valueOf(i));
				}catch(Exception ex) {

				}	

			}
		}

		tfBarcode1 = new FixedLengthTextField(120);
		tfBarcode2 = new FixedLengthTextField(120);		
		extraPricePanel.setBackground(new Color(209, 222, 235));
		extraPricePanel.setLayout(new MigLayout());
		extraPricePanel.add(new JLabel("Buy Price"), "growx");
		extraPricePanel.add(tfBuyPrice, "growx");
		extraPricePanel.add(new JLabel("Barcode-1"), "growx");
		extraPricePanel.add(tfBarcode1, "growx");
		extraPricePanel.add(new JLabel("Barcode-2"), "growx");
		extraPricePanel.add(tfBarcode2, "growx, wrap");
		extraPricePanel.add(cbPfand, "growx");
		extraPricePanel.add(new JLabel("Price Category"), "growx");
		extraPricePanel.add(cbType, "growx, wrap" );
		if(!simpleForm)
			tabbedPane.add(POSConstants.MORE, extraPricePanel);

		add(tabbedPane, BorderLayout.CENTER);
		if(simpleForm)
			add(new QwertyKeyPad(), BorderLayout.SOUTH);
		tabbedPane.setBackground(new Color(209, 222, 235));
		jPanel1.setBackground(new Color(209, 222, 235));
		setBean(menuItem);
		setBackground(new Color(209, 222, 235));
		addRecepieExtension();
	}
	JComboBox<String> cbType;
	public void addRecepieExtension() {
		InventoryPlugin plugin = Application.getPluginManager().getPlugin(
				InventoryPlugin.class);
		if (plugin == null) {
			return;
		}

		plugin.addRecepieView(tabbedPane);
	}


	public void printBarcodLevel(MenuItem item) {
		try {
			Double itemPrice = Double.parseDouble(tfPrice.getText());
			String price = NumberUtil.formatNumber(itemPrice);

			HashMap map = new HashMap();
			map.put("itemName", UtilityService.getNameFormated(tfName.getText()));
			map.put("itemPrice", price + " €");
			map.put("itemId", tfItemId.getText());
			map.put("barcode", UtilityService.createBarcode(tfBarcode.getText(), 400));
			if(!tfNote.getText().isEmpty()||!tfDescription.getText().isEmpty())
				map.put("infoLable", UtilityService.buildInfoLable(tfNote.getText(), tfDescription.getText()));

			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/barcode.jasper";

			JasperPrint jasperPrint = JReportPrintService.createJasperPrint(
					FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
			if (jasperPrint != null)
				jasperPrint.setName("barcode:" + tfName.getText());
			jasperPrint.setProperty("printerName", Application.getPrinters()
					.getReceiptPrinter());
			JReportPrintService.printQuitely(jasperPrint);			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		tabbedPane = new javax.swing.JTabbedPane();
		imagePanel = new javax.swing.JPanel(); 
		 
		extraPricePanel = new javax.swing.JPanel(); 
		jPanel1 = new javax.swing.JPanel();    
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel1_ = new javax.swing.JLabel();
		
		jLabel1_.setHorizontalAlignment(SwingConstants.TRAILING);
		tfName = new com.floreantpos.swing.FixedLengthTextField();
		tfLName = new com.floreantpos.swing.FixedLengthTextField();
		tfName.setText("Artikel Name");
		tfItemId = new com.floreantpos.swing.FixedLengthTextField();
		restaurant = RestaurantDAO.getRestaurant();
		id1 = restaurant.getArtikelnummer() != null ? restaurant.getArtikelnummer() : 1;
		tfItemId.setText(Integer.toString(id1));
		tfDescription = new JTextArea(3, 500);
		tfDescription2 = new JTextArea(3, 500);
		tfNote = new JTextArea(2, 50);
		jLabel4 = new javax.swing.JLabel();
		jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
		cbGroup = new javax.swing.JComboBox();
		cbCategory = new javax.swing.JComboBox();
		btnNewGroup = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
		tfPrice =  new com.floreantpos.swing.FixedLengthTextField();		
		tfPrice.setText("0.00");

		tfPrice.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(!tfPrice.getText().equals("0.00"))
					tfPrice.setText(blockEntry(tfPrice.getText()));

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(!tfPrice.getText().equals("0.00"))
					tfPrice.setText(blockEntry(tfPrice.getText()));

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(!tfPrice.getText().equals("0.00"))
					tfPrice.setText(blockEntry(tfPrice.getText()));
			}
		});

		jLabel3_= new javax.swing.JLabel();
		jLabel3_.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel3_.setText("Einkaufspreis");
		if(StringUtils.isNotEmpty(POSConstants.Einkaufspreis))
			jLabel3_.setText(POSConstants.Einkaufspreis);
		
		tfPrice_=  new com.floreantpos.swing.FixedLengthTextField();		
		tfPrice_.setText("0.00");

		tfPrice_.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(!tfPrice_.getText().equals("0.00"))
					tfPrice_.setText(blockEntry(tfPrice_.getText()));

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(!tfPrice_.getText().equals("0.00"))
					tfPrice_.setText(blockEntry(tfPrice_.getText()));

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(!tfPrice_.getText().equals("0.00"))
					tfPrice_.setText(blockEntry(tfPrice_.getText()));
			}
		});

		jLabel6 = new javax.swing.JLabel();
		jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
		btnNewTax = new javax.swing.JButton();
		btnNewCategory = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel5 = new javax.swing.JLabel();
		tfDiscountRate = new DoubleTextField();
		tfDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
		chkVisible = new javax.swing.JCheckBox();
		chkChangePrice = new javax.swing.JCheckBox();
		jPanel2 = new javax.swing.JPanel();
		btnNewModifierGroup = new javax.swing.JButton();
		btnDeleteModifierGroup = new javax.swing.JButton();
		btnEditModifierGroup = new javax.swing.JButton();
		
		btnNewMenuItemPrice = new javax.swing.JButton();
		btnNewMenuItemPrice.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnNewMenuItemPrice.setBackground(new Color(102, 255, 102));

		btnEditMenuItemPrice = new javax.swing.JButton();
		btnEditMenuItemPrice.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnEditMenuItemPrice.setBackground(new Color(102, 255, 102));

		btnDeleteMenuItemPrice = new javax.swing.JButton();
		btnDeleteMenuItemPrice.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnDeleteMenuItemPrice.setBackground(new Color(255, 153, 153));
		
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setBackground(Color.WHITE);
		
		jScrollPanePrice = new javax.swing.JScrollPane();
		jScrollPanePrice.setBackground(Color.WHITE);
		jScrollPanePrice.getViewport().setBackground(Color.WHITE);
		
		tableTicketItemModifierGroups = new javax.swing.JTable();
		jPanel3 = new javax.swing.JPanel();
		jPanelPrice = new javax.swing.JPanel();
		btnDeleteShift = new javax.swing.JButton();
		btnAddShift = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.setBackground(Color.WHITE);
		shiftTable = new javax.swing.JTable();
		
		tableMenuItemPrice = new javax.swing.JTable();
		tableMenuItemPrice.getTableHeader().setBackground(new Color(209, 222, 235));
		
		jLabel1.setText(Messages.getString("LABEL_NAME"));
		jLabel1_.setText("Lieferant");
		if(StringUtils.isNotEmpty(POSConstants.Liferant)) {
			jLabel1_.setText(POSConstants.Liferant); }
		
		jLabel4.setText(Messages.getString("LABEL_GROUP"));

		btnNewGroup.setText(" + ");
		btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCreateNewGroup(evt);
			}
		});

		if (Application.getInstance().isPriceIncludesTax()) {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_INCLUDING_TAX"));
		} else {
			jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_EXCLUDING_TAX"));
		}

		tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfPrice_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel6.setText(Messages.getString("LABEL_TAX"));

		btnNewTax.setText(" + ");
		btnNewTax.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewTaxdoCreateNewTax(evt);
			}
		});


		btnNewCategory.setText(" + ");
		btnNewCategory.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					createNewCategory(evt);
					List<MenuCategory> categoryList = MenuCategoryDAO.getInstance().findAll();
					cbCategory.setModel(new ComboBoxModel(categoryList));
					foodGroupDAO = new MenuGroupDAO();
					List<MenuGroup> foodGroups = foodGroupDAO.findAll();
					cbGroup.setModel(new ComboBoxModel(foodGroups));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// jLabel2.setText(com.floreantpos.POSConstants.DISCOUNT_RATE + ":");

		jLabel5.setText("€");

		chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
		chkVisible.setBorder(javax.swing.BorderFactory
				.createEmptyBorder(0, 0, 0, 0));
		chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chkChangePrice.setText("Aleternativer Preis");
		tabbedPane.addTab(com.floreantpos.POSConstants.GENERAL, jPanel1);

		btnNewModifierGroup.setText(com.floreantpos.POSConstants.ADD);
		btnNewModifierGroup.setActionCommand("AddModifierGroup");
		btnNewModifierGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewModifierGroupActionPerformed(evt);
			}
		});

		btnDeleteModifierGroup.setText(com.floreantpos.POSConstants.DELETE);
		btnDeleteModifierGroup.setActionCommand("DeleteModifierGroup");

		btnEditModifierGroup.setText(com.floreantpos.POSConstants.EDIT);
		btnEditModifierGroup.setActionCommand("EditModifierGroup");

		tableTicketItemModifierGroups
		.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {},
			{}, {}, {} }, new String[] {

			}));
		
		
		jScrollPane1.setViewportView(tableTicketItemModifierGroups);
		
		btnNewMenuItemPrice.setText(com.floreantpos.POSConstants.ADD);
		btnNewMenuItemPrice.setActionCommand("AddMenuItemPrice");

		btnDeleteMenuItemPrice.setText(com.floreantpos.POSConstants.DELETE);
		btnDeleteMenuItemPrice.setActionCommand("DeleteMenuItemPrice");

		btnEditMenuItemPrice.setText(com.floreantpos.POSConstants.EDIT);
		btnEditMenuItemPrice.setActionCommand("EditMenuItemPrice");


		tableMenuItemPrice.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { {}, {}, {}, {} }, new String[] {

				}));

		jScrollPanePrice.setViewportView(tableMenuItemPrice);
		
		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);

		jPanel2Layout.setHorizontalGroup(jPanel2Layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(
						org.jdesktop.layout.GroupLayout.TRAILING,
						jPanel2Layout.createSequentialGroup()
						.addContainerGap(280, Short.MAX_VALUE).add(btnNewModifierGroup)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(btnEditModifierGroup)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(btnDeleteModifierGroup).addContainerGap())
				.add(
						jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jScrollPane1,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 377,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(86, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
						org.jdesktop.layout.GroupLayout.TRAILING,
						jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								270, Short.MAX_VALUE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(
								jPanel2Layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(btnDeleteModifierGroup).add(btnEditModifierGroup)
								.add(btnNewModifierGroup)).addContainerGap()));

		//		tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUPS, jPanel2);
		tabbedPane.setBackground(new Color(209, 222, 235));
		jPanel2.setBackground(new Color(209, 222, 235));
		btnDeleteShift.setText(com.floreantpos.POSConstants.DELETE_SHIFT);

		btnAddShift.setText(com.floreantpos.POSConstants.ADD_SHIFT);

		shiftTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
			{ null, null, null, null }, { null, null, null, null },
			{ null, null, null, null }, { null, null, null, null } }, new String[] {
					"Title 1", "Title 2", "Title 3", "Title 4" }));
		jScrollPane2.setViewportView(shiftTable);

		jPanelPrice.setLayout(new MigLayout());
		jPanelPrice.setBackground(new Color(209, 222, 235));
		jPanelPrice.add(jScrollPanePrice, "wrap");
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(new Color(209, 222, 235));
		btnPanel.setLayout(new MigLayout());
		btnPanel.add(btnNewMenuItemPrice);
		btnPanel.add(btnEditMenuItemPrice);
		btnPanel.add(btnDeleteMenuItemPrice);
		
		org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
						jPanel3Layout
						.createSequentialGroup()
						.addContainerGap(76, Short.MAX_VALUE)
						.add(
								jPanel3Layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(org.jdesktop.layout.GroupLayout.TRAILING,
										jScrollPane2,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 387,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(
										org.jdesktop.layout.GroupLayout.TRAILING,
										jPanel3Layout.createSequentialGroup().add(btnAddShift)
										.add(5, 5, 5).add(btnDeleteShift)))
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
						jPanel3Layout
						.createSequentialGroup()
						.add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								281, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(
								jPanel3Layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(btnAddShift).add(btnDeleteShift))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		// tabbedPane.addTab(com.floreantpos.POSConstants.SHIFTS, jPanel3);
		if(!simpleForm) {
			jPanelPrice.add(btnPanel);
			tabbedPane.addTab("Preis", jPanelPrice);
			tabbedPane.addTab("Color", colorPanel);
			tabbedPane.addTab("Fore-Color", foreColorPanel);
		}

		jPanel3.setBackground(new Color(209, 222, 235));
		tabbedPane.addChangeListener(this);
	}// </editor-fold>//GEN-END:initComponents

	public void changeListener() {
		List<MenuItem> itemList = MenuItemDAO.getInstance().findById(true,
				tfSubartid.getText());
		int found = 0;
		for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
			MenuItem item = itr.next();
			if (item != null)
				found = 1;
		}
		if (found == 0) {
			lblCheck.setForeground(Color.RED);
			lblCheck.setText("Nicht gefunden");
		} else {
			lblCheck.setForeground(new Color(0, 102, 0));
			lblCheck.setText("Gefunden");
		}
	}

	private void btnNewTaxdoCreateNewTax(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewTaxdoCreateNewTax
		BeanEditorDialog dialog = new BeanEditorDialog(new TaxForm(false),
				BackOfficeWindow.getInstance(), true);
		dialog.open();
	}// GEN-LAST:event_btnNewTaxdoCreateNewTax

	private void createNewCategory(java.awt.event.ActionEvent evt) throws Exception {// GEN-FIRST:event_btnNewTaxdoCreateNewTax
		BeanEditorDialog dialog = new BeanEditorDialog(MenuCategoryForm.getmForm(),
				BackOfficeWindow.getInstance(), true);
		dialog.open();
	}

	private void btnNewModifierGroupActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewModifierGroupActionPerformed

	}// GEN-LAST:event_btnNewModifierGroupActionPerformed

	private void doCreateNewGroup(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doCreateNewGroup
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(editor, getParentFrame(),
				true);
		dialog.open();
		if (!dialog.isCanceled()) {
			MenuGroup foodGroup = (MenuGroup) editor.getBean();
			ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
			model.addElement(foodGroup);
			model.setSelectedItem(foodGroup);
		}
	}// GEN-LAST:event_doCreateNewGroup

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnAddShift;
	private javax.swing.JButton btnDeleteModifierGroup;
	private javax.swing.JButton btnDeleteShift;
	private javax.swing.JButton btnEditModifierGroup;
	private javax.swing.JButton btnNewGroup;
	private javax.swing.JButton btnNewModifierGroup;
	private javax.swing.JButton btnNewTax;
	private javax.swing.JButton btnNewCategory;
	private static javax.swing.JComboBox cbCategory;
	private static javax.swing.JComboBox cbGroup;
	private javax.swing.JCheckBox chkVisible;
	private javax.swing.JCheckBox chkChangePrice;
	private static javax.swing.JComboBox cbTax;
	private javax.swing.JTable tableMenuItemPrice;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel1_;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel3_;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanelPrice;
	private javax.swing.JScrollPane jScrollPanePrice;
	private javax.swing.JButton btnEditMenuItemPrice;
	private javax.swing.JButton btnNewMenuItemPrice;
	private javax.swing.JButton btnDeleteMenuItemPrice;
	private javax.swing.JPanel imagePanel;
	private javax.swing.JPanel extraPricePanel;
 
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane tabbedPane;
	private javax.swing.JTable shiftTable;
	private javax.swing.JTable tableTicketItemModifierGroups;
	private DoubleTextField tfDiscountRate;
	private static com.floreantpos.swing.FixedLengthTextField tfName;
	private static com.floreantpos.swing.FixedLengthTextField tfLName;
	private JLabel lblCheck;
	private static com.floreantpos.swing.FixedLengthTextField tfItemId;
	private com.floreantpos.swing.FixedLengthTextField tfWeight;
	private com.floreantpos.swing.FixedLengthTextField tfSubartid;
	private JTextArea tfDescription;
	private JTextArea tfDescription2;
	private JTextArea tfNote;
	private static JTextField tfPrice;	
	private static JTextField tfPrice_;

	private List<MenuItemModifierGroup> menuItemModifierGroups;
	
	private List<MenuItemPrice> menuItemPriceList;
	private MenuItemPriceModel menuItemPriceModel;

	private MenuItemMGListModel menuItemMGListModel;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JCheckBox cbShowTextWithImage;
	private DoubleTextField tfBuyPrice;
	private JLabel lblItemId;
	
	JCheckBox cbPfand;
	
	private JComboBox<VirtualPrinter> cbPrinter;
	private JLabel lblBarcode;
	private static FixedLengthTextField tfBarcode;
	private FixedLengthTextField tfBarcode1;
	private FixedLengthTextField tfBarcode2;
	private List<String> list;
	MenuGroupDAO foodGroupDAO;
	MenuCategoryDAO categoryDAO;
	private static Restaurant restaurant;
	private static int id1;


	private void addMenuItemModifierGroup() {
		try {
			MenuItemModifierGroupForm form = new MenuItemModifierGroupForm();
			BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
					true);
			dialog.open();
			if (!dialog.isCanceled()) {
				MenuItemModifierGroup modifier = (MenuItemModifierGroup) form.getBean();
				// modifier.setParentMenuItem((MenuItem) this.getBean());
				menuItemMGListModel.add(modifier);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void editMenuItemModifierGroup() {
		try {
			int index = tableTicketItemModifierGroups.getSelectedRow();
			if (index < 0)
				return;

			MenuItemModifierGroup menuItemModifierGroup = menuItemMGListModel
					.get(index);

			MenuItemModifierGroupForm form = new MenuItemModifierGroupForm(
					menuItemModifierGroup);
			BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
					true);
			dialog.open();
			if (!dialog.isCanceled()) {
				// menuItemModifierGroup.setParentMenuItem((MenuItem) this.getBean());
				menuItemMGListModel.fireTableDataChanged();
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void deleteMenuItemModifierGroup() {
		try {
			int index = tableTicketItemModifierGroups.getSelectedRow();
			if (index < 0)
				return;

			if (ConfirmDeleteDialog.showMessage(this,
					com.floreantpos.POSConstants.CONFIRM_DELETE,
					com.floreantpos.POSConstants.CONFIRM) == ConfirmDeleteDialog.YES) {
				menuItemMGListModel.remove(index);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}
	
	private void addMenuItemPrice() {
		try {
			System.out.println("addMenuItem_");
			MenuItemPriceForm form = new MenuItemPriceForm();
			BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
					true);
			dialog.setSize(200, 200);
			dialog.open();
			if (!dialog.isCanceled()) {
				System.out.println("addMenuItem_Price");
				MenuItemPrice price = (MenuItemPrice) form.getBean();
				menuItemPriceModel.add(price);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void editMenuItemPrice() {
		try {
			int index = tableMenuItemPrice.getSelectedRow();
			if (index < 0)
				return;

			MenuItemPrice menuItemPrice = menuItemPriceModel.get(index);
			MenuItemPriceForm form = new MenuItemPriceForm(menuItemPrice);
			BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(),
					true);
			dialog.open();
			if (!dialog.isCanceled()) {
				// menuItemModifierGroup.setParentMenuItem((MenuItem) this.getBean());
				menuItemPriceModel.fireTableDataChanged();
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void deleteMenuItemPrice() {
		try {
			int index = tableMenuItemPrice.getSelectedRow();
			if (index < 0)
				return;

			if (ConfirmDeleteDialog.showMessage(this,
					com.floreantpos.POSConstants.CONFIRM_DELETE,
					com.floreantpos.POSConstants.CONFIRM) == ConfirmDeleteDialog.YES) {
				menuItemPriceModel.remove(index);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}
	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;
			
			/*
			 * if(tfBarcode.getText().length() > 0) {
			 * if(MenuItemDAO.getInstance().findByBarcode(tfBarcode.getText()).size()
			 * > 0) { POSMessageDialog.showError(this,"Barcode bereits vorhanden");
			 * return false; } }
			 */

			MenuItem menuItem = getBean();
			MenuItemDAO menuItemDAO = new MenuItemDAO();
			menuItemDAO.saveOrUpdate(menuItem);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Speichern Fehler: "+e.getMessage().substring(0, 20));
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		menuItem = getBean();
		if (menuItem.getId() != null
				&& !Hibernate.isInitialized(menuItem.getMenuItemModiferGroups())) {
			// initialize food item modifer groups.
			MenuItemDAO dao = new MenuItemDAO();
			Session session = dao.getSession();
			menuItem = (MenuItem) session.merge(menuItem);
			Hibernate.initialize(menuItem.getMenuItemModiferGroups());
			session.close();
		}

		tfLName.setText(menuItem.getLieferantName());
		
		tfName.setText(menuItem.getName());
		id1 = restaurant.getArtikelnummer() != null ? restaurant.getArtikelnummer() : 1;
		//		if(id1==1) {
		//			int i = JOptionPane.showConfirmDialog(this, "Artkel nummer ist 1?", "Wollen sie Aendern", JOptionPane.YES_NO_OPTION);
		//			if (i == JOptionPane.YES_OPTION) {
		//				id1 = 1;
		//			}else {
		//				String Id = JOptionPane.showInputDialog(new JTextField(), "Bitte Neue Artikel nummer geben");
		//				try {
		//					id1 = Integer.parseInt(Id);
		//				}catch(Exception Ex) {
		//					JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Error "+ Ex.getMessage());
		//					return;
		//				}
		//				
		//			}
		//
		//		}

		if(menuItem.getItemId()!=null)
			tfItemId.setText(menuItem.getItemId());
		else
			tfItemId.setText(Integer.toString(id1));

		tfDescription.setText(menuItem.getDescription());
		tfDescription2.setText(menuItem.getDescription2());
		tfNote.setText(menuItem.getNote());
		tfBarcode.setText(menuItem.getBarcode());
		tfBarcode1.setText(menuItem.getBarcode1());
		tfBarcode2.setText(menuItem.getBarcode2());


		tfPrice.setText(String.valueOf(menuItem.getPrice()));
		tfPrice_.setText(String.valueOf(menuItem.getLiferantBuyPrice()));
		tfBuyPrice.setText(String.valueOf(menuItem.getPrice()));
		//cbPfand.setSelected(menuItem.isPfand());
		cbPfand.setSelected(false);
		cbType.setSelectedIndex(menuItem.getPriceCategory()-1);	
		tfDiscountRate.setText(String.valueOf(menuItem.getDiscountRate()));
		chkVisible.setSelected(menuItem.isVisible());
		if (menuItem.isChangeprice() != null)
			chkChangePrice.setSelected(menuItem.isChangeprice());
		if (menuItem.getWeightgrams() != null)
			tfWeight.setText(menuItem.getWeightgrams() + "");
		if (menuItem.getSubitemid() != null)
			tfSubartid.setText(menuItem.getSubitemid());

		if (menuItem.getRed() != null) {
			colorPanel.setRed(menuItem.getRed());
		}
		if (menuItem.getGreen() != null) {
			colorPanel.setGreen(menuItem.getGreen());
		}
		if (menuItem.getBlue() != null) {
			colorPanel.setBlue(menuItem.getBlue());
		}

		if (menuItem.getFred() != null) {
			foreColorPanel.setRed(menuItem.getFred());
		}
		if (menuItem.getFgreen() != null) {
			foreColorPanel.setGreen(menuItem.getFgreen());
		}
		if (menuItem.getFblue() != null) {
			foreColorPanel.setBlue(menuItem.getFblue());
		}
		cbShowTextWithImage.setSelected(menuItem.isShowImageOnly());
		if (menuItem.getImage() != null) {
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(menuItem.getImage())
					.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
			lblImagePreview.setIcon(imageIcon);
		}

		if (menuItem.getId() == null) {
			// cbGroup.setSelectedIndex(0);
			// cbTax.setSelectedIndex(0);
		} else {
			cbCategory.setSelectedItem(menuItem.getParent().getParent());
			cbGroup.setSelectedItem(menuItem.getParent());
		}

		cbPrinter.setSelectedItem(menuItem.getVirtualPrinter());
	}
	@Override
	protected boolean updateModel() {
		String itemName = tfName.getText();
		
		String liferantItemName = tfLName.getText();
		
		String itemId = tfItemId.getText();
		try {
			int id = Integer.parseInt(itemId);
		} catch (Exception e) {
			MessageDialog.showError("Produkt id ist nicht gueltig");
			return false;
		}

		if (cbGroup.getSelectedItem() == null) {
			MessageDialog.showError("Gruppen ist nicht gueltig");
			return false;
		}

		if (cbCategory.getSelectedItem() == null) {
			MessageDialog.showError("Kategorie ist nicht gueltig");
			return false;
		}
		String description = tfDescription.getText();
		String description2 = tfDescription2.getText();
		String note = tfNote.getText();
		if (POSUtil.isBlankOrNull(itemName)) {
			System.out.println("itemName in updateModel()>>"+ itemName);
			MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}

		menuItem = getBean();
		menuItem.setName(itemName);
		menuItem.setLieferantName(liferantItemName);
		 
		menuItem.setItemId(itemId);
		menuItem.setBarcode(ReportUtil.removeLeadingZeroes(tfBarcode.getText()));
		menuItem.setBarcode1(ReportUtil.removeLeadingZeroes(tfBarcode1.getText()));
		menuItem.setBarcode2(ReportUtil.removeLeadingZeroes(tfBarcode2.getText()));

		menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
		menuItem.setDescription(description);
		menuItem.setDescription2(description2);
		menuItem.setNote(note);
		String price = tfPrice.getText().replace(',', '.');
		String price2 = tfBuyPrice.getText().replace(',', '.');
		String price_ = tfPrice_.getText().replace(',', '.');
		menuItem.setPrice(NumberUtil.roundToTwoDigit(Double.valueOf(price)));
		menuItem.setLiferantBuyPrice(NumberUtil.roundToTwoDigit(Double.valueOf(price_)));
		menuItem.setBuyPrice(NumberUtil.roundToTwoDigit(Double.valueOf(price2)));
		menuItem.setPfand(cbPfand.isSelected());
		menuItem.setPriceCategory(Integer.parseInt(cbType.getSelectedItem()!=null?cbType.getSelectedItem().toString():"1"));

		if(cbTax.getSelectedIndex() == 1) {
			menuItem.setTax(TaxDAO.getInstance().findByName(POSConstants.DINE_IN));
		} else if (cbTax.getSelectedIndex() == 0) {
			menuItem.setTax(TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY));
		} else {
			menuItem.setTax(TaxDAO.getInstance().findByName("ZERO"));
		}

		menuItem.setRed(colorPanel.getRed());
		menuItem.setGreen(colorPanel.getGreen());
		menuItem.setBlue(colorPanel.getBlue());

		menuItem.setFred(foreColorPanel.getRed());
		menuItem.setFgreen(foreColorPanel.getGreen());
		menuItem.setFblue(foreColorPanel.getBlue());

		menuItem.setVisible(chkVisible.isSelected());
		menuItem.setChangeprice(chkChangePrice.isSelected());
		String weight = tfWeight.getText();
		weight = weight.replace(',', '.');
		if (weight != null && weight.length() > 0)
			menuItem.setWeightgrams(Double.parseDouble(weight));

		menuItem.setSubitemid(tfSubartid.getText());
		menuItem.setShowImageOnly(cbShowTextWithImage.isSelected());

		try {
			menuItem.setDiscountRate(Double.parseDouble(tfDiscountRate.getText()));
		} catch (Exception x) {
		}
		menuItem.setMenuItemModiferGroups(menuItemModifierGroups);
		// menuItem.setShifts(shiftTableModel.getShifts());

		int tabCount = tabbedPane.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			Component componentAt = tabbedPane.getComponent(i);
			if (componentAt instanceof IUpdatebleView) {
				IUpdatebleView view = (IUpdatebleView) componentAt;
				if (!view.updateModel(menuItem)) {
					return false;
				}
			}
		}

		menuItem.setVirtualPrinter((VirtualPrinter) cbPrinter.getSelectedItem());
		category = cbCategory.getSelectedIndex();
		group = cbGroup.getSelectedIndex();
		tax = cbTax.getSelectedIndex();
		cbCategory.setSelectedItem(cbCategory.getSelectedIndex());
		cbGroup.setSelectedItem(cbGroup.getSelectedIndex());
		cbTax.setSelectedIndex(cbTax.getSelectedIndex());
		try {
			restaurant.setArtikelnummer(Integer.parseInt(itemId)+1);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return true;
	}

	public String blockEntry(String text) {
		if(text.contains(".")) {
			if(text.substring(text.indexOf("."),text.length()).length()>3)
				text = text.substring(0, text.indexOf(".")+3);
		}
		return text;
	}

	@Override
	public String getDisplayText() {
		MenuItem foodItem = getBean();
		if (foodItem.getId() == null) {
			return com.floreantpos.POSConstants.NEW_MENU_ITEM;
		}
		return com.floreantpos.POSConstants.EDIT_MENU_ITEM;
	}

	class MenuItemMGListModel extends AbstractTableModel {
		String[] cn = { com.floreantpos.POSConstants.GROUP_NAME,
				com.floreantpos.POSConstants.MIN_QUANTITY,
				com.floreantpos.POSConstants.MAX_QUANTITY };

		MenuItemMGListModel() {
		}

		public MenuItemModifierGroup get(int index) {
			return menuItemModifierGroups.get(index);
		}

		public void add(MenuItemModifierGroup group) {
			if (menuItemModifierGroups == null) {
				menuItemModifierGroups = new ArrayList<MenuItemModifierGroup>();
			}
			menuItemModifierGroups.add(group);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemModifierGroup group) {
			if (menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(group);
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (menuItemModifierGroups == null)
				return 0;

			return menuItemModifierGroups.size();

		}

		@Override
		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemModifierGroup menuItemModifierGroup = menuItemModifierGroups
					.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return menuItemModifierGroup.getModifierGroup().getName();

			case 1:
				return Integer.valueOf(menuItemModifierGroup.getMinQuantity());

			case 2:
				return Integer.valueOf(menuItemModifierGroup.getMaxQuantity());
			}
			return null;
		}
	}

	class MenuItemPriceModel extends AbstractTableModel {
		String[] cn = { "Name", "Price" };

		MenuItemPriceModel() {
		}

		public MenuItemPrice get(int index) {
			return menuItemPriceList.get(index);
		}

		public void add(MenuItemPrice menuitemprice) {
			if (menuItemPriceList == null) {
				menuItemPriceList = new ArrayList<MenuItemPrice>();
			}
			System.out.println("menuitemprice: "+ menuitemprice);
			menuItemPriceList.add(menuitemprice);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (menuItemPriceList == null) {
				return;
			}
			menuItemPriceList.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemModifierGroup group) {
			if (menuItemPriceList == null) {
				return;
			}
			menuItemPriceList.remove(group);
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (menuItemPriceList == null)
				return 0;
			return menuItemPriceList.size();
		}

		@Override
		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemPrice menuitemprice = menuItemPriceList.get(rowIndex);
             System.out.println("menuitemprice_"+ menuitemprice.getName());
			switch (columnIndex) {
			case 0:
				return menuitemprice.getName();

			case 1:
				return NumberUtil.formatNumber(menuitemprice.getPrice());

			}
			return null;
		}
	}

	class ShiftTableModel extends AbstractTableModel {
		List<MenuItemShift> shifts;
		String[] cn = { com.floreantpos.POSConstants.START_TIME,
				com.floreantpos.POSConstants.END_TIME,
				com.floreantpos.POSConstants.PRICE };
		Calendar calendar = Calendar.getInstance();

		ShiftTableModel(List<MenuItemShift> shifts) {
			if (shifts == null) {
				this.shifts = new ArrayList<MenuItemShift>();
			} else {
				this.shifts = new ArrayList<MenuItemShift>(shifts);
			}
		}

		public MenuItemShift get(int index) {
			return shifts.get(index);
		}

		public void add(MenuItemShift group) {
			if (shifts == null) {
				shifts = new ArrayList<MenuItemShift>();
			}
			shifts.add(group);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (shifts == null) {
				return;
			}
			shifts.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemShift group) {
			if (shifts == null) {
				return;
			}
			shifts.remove(group);
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (shifts == null)
				return 0;

			return shifts.size();

		}

		@Override
		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public List<MenuItemShift> getShifts() {
			return shifts;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemShift shift = shifts.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return ShiftUtil.buildShiftTimeRepresentation(shift.getShift()
						.getStartTime());

			case 1:
				return ShiftUtil.buildShiftTimeRepresentation(shift.getShift()
						.getEndTime());

			case 2:
				return String.valueOf(shift.getShiftPrice());
			}
			return null;
		}
	}

	/*
	 * private void addShift() { //TODO: ??? MenuItemShiftDialog dialog = new
	 * MenuItemShiftDialog((Dialog) this.getTopLevelAncestor());
	 * dialog.setSize(350, 220); dialog.open();
	 * 
	 * if(!dialog.isCanceled()) { MenuItemShift menuItemShift =
	 * dialog.getMenuItemShift(); shiftTableModel.add(menuItemShift); } }
	 */

	/*
	 * private void deleteShift() { int selectedRow = shiftTable.getSelectedRow();
	 * if(selectedRow >= 0) { shiftTableModel.remove(selectedRow); } }
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("AddModifierGroup")) {
			addMenuItemModifierGroup();
		} else if (actionCommand.equals("EditModifierGroup")) {
			editMenuItemModifierGroup();
		} else if (actionCommand.equals("DeleteModifierGroup")) {
			deleteMenuItemModifierGroup();
		} else if (actionCommand.equals("AddMenuItemPrice")) {
			addMenuItemPrice();
		} else if (actionCommand.equals("EditMenuItemPrice")) {
			editMenuItemPrice();
		} else if (actionCommand.equals("DeleteMenuItemPrice")) {
			deleteMenuItemPrice();
		} else if (actionCommand.equals(com.floreantpos.POSConstants.ADD_SHIFT)) {
			// addShift();
		} else if (actionCommand.equals(com.floreantpos.POSConstants.DELETE_SHIFT)) {
			// deleteShift();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Component selectedComponent = tabbedPane.getSelectedComponent();
		if (!(selectedComponent instanceof IUpdatebleView)) {
			return;
		}

		IUpdatebleView view = (IUpdatebleView) selectedComponent;

		MenuItem menuItem = getBean();
		view.initView(menuItem);
	}
}
