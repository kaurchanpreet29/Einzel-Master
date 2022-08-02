/*
 * OthersView.java
 *
 * Created on August 23, 2006, 12:40 AM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;
import org.apache.xpath.axes.ReverseAxesWalker;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hsqldb.lib.StringUtil;
import org.jdesktop.swingx.JXCollapsiblePane;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ExplorerButtonPanel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.customer.CustomerSelectionDialog;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.report.ServerReport;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DeliveryHistoryDialog;
import com.floreantpos.ui.dialog.ItemSearchDialog;
import com.floreantpos.ui.dialog.LastTicketListDialog;
import com.floreantpos.ui.dialog.MiscTicketItemmDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.OpenTicketListDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordfieldDialog;
import com.floreantpos.ui.dialog.PfandDialog;
import com.floreantpos.ui.dialog.ReportViewerDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoDialogZWS;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.OrderInfoViewZWS;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author MShahriar
 */
public class OthersView extends JXCollapsiblePane {
	private Ticket currentTicket;
	private ItemSelectionListener itemSelectionListener;
	private JLabel lblDisplay;
	TicketView ticketView;
	JComboBox<String> cbType;
	JButton btnUpdateCart = new JButton("Update");
	JButton btnHistory = new JButton("History");


	/** Creates new form OthersView */
	public OthersView(TicketView ticketView) {
		this.ticketView = ticketView;
		initComponents();
	}

	public OthersView(ItemSelectionListener itemSelectionListener) {
		initComponents();

		setItemSelectionListener(itemSelectionListener);
	}

	public class ArrowAction extends AbstractAction {

		private String cmd;

		public ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmd.equalsIgnoreCase("D")) {
				doInsertMisc();
			} else if (cmd.equalsIgnoreCase("Z")) {
				doViewOrderInfo();
			} else if (cmd.equalsIgnoreCase("K")) {
				doAddEditCustomer();
			} else if (cmd.equalsIgnoreCase("T")) {
				updateTableNumber();
			}
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
		buttonPanel = new JPanel();
		retour = false;
		btnOrderInfo = new com.floreantpos.swing.PosButton();
		btnItemSearch = new com.floreantpos.swing.PosButton();
		btnItemUpdate = new com.floreantpos.swing.PosButton();
		btnAngebote = new com.floreantpos.swing.PosButton();


		btnMisc = new com.floreantpos.swing.PosButton();
		btnBarbewert = new com.floreantpos.swing.PosButton();
		btnCard = new com.floreantpos.swing.PosButton();
		btnMore = new com.floreantpos.swing.PosButton();
		btnOpenDrawer = new com.floreantpos.swing.PosButton();
		btnKellnerAbschulss = new com.floreantpos.swing.PosButton();
		tfItemSearch = new JTextField();
		btnCancel = new com.floreantpos.swing.PosButton();
		btnOpenTicket = new com.floreantpos.swing.PosButton();
		btnPaylater = new com.floreantpos.swing.PosButton();
		btnRabatt = new com.floreantpos.swing.PosButton();
		btnRabattd = new com.floreantpos.swing.PosButton();
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "D");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "Z");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "K");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "T");

		am.put("D", new ArrowAction("D"));
		am.put("Z", new ArrowAction("Z"));
		am.put("K", new ArrowAction("K"));
		am.put("T", new ArrowAction("T"));

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, "=", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.DEFAULT_POSITION));
		setLayout(new BorderLayout());
		setBackground(new Color(35, 35, 36));
		final Border border = BorderFactory.createLineBorder(new Color(153, 0, 0), 3);
		tfItemSearch.setText("Barcode eingeben..");
		tfItemSearch.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tfItemSearch.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				tfItemSearch.setText("");
				tfItemSearch.setBorder(border);        
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfItemSearch.setText("Barcode eingeben..");
				tfItemSearch.setBorder(null);
			}

		});

		tfItemSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!retour) {
					if (!searchItem(tfItemSearch.getText())) {
						POSMessageDialog.showError("Art. nicht gefunden");
					}
				}else {
					if (!searchItemForRetour(tfItemSearch.getText())) {
						POSMessageDialog.showError("Art. nicht gefunden");
					}
				}

				tfItemSearch.setText("");
			}
		});
		
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));

		lblDisplay = new JLabel();
		lblDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		lblDisplay.setForeground(Color.RED);
		lblDisplay.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblDisplay.setVisible(true);
		lblDisplay.setText("Test String");
		JPanel lblPanel = new JPanel();
		lblPanel.setLayout(new BorderLayout());
		lblPanel.setBackground(Color.WHITE);
		lblPanel.add(lblDisplay, BorderLayout.CENTER);
		
		btnUpdateCart.setBackground(Color.BLACK);
		btnUpdateCart.setForeground(Color.WHITE);
		btnUpdateCart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = getCurrentTicket();
				if (ticket != null) {
					ticketView.updateCart();
				}
			}

		});		
		
		btnHistory.setBackground(Color.BLACK);
		btnHistory.setForeground(Color.WHITE);
		btnHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = getCurrentTicket();
				if (ticket != null) {
					Customer customer = CustomerDAO.getInstance().get(Integer.valueOf(ticket.getProperty(ticket.CUSTOMER_ID)));
									
					if(customer!=null) {
						List<Ticket> list = TicketDAO.getInstance().findTktByCustomer(customer);
						
						if(list!=null&&list.size()>0) {
							DeliveryHistoryDialog dialog = null;
							try {
								Collections.reverse(list);
								dialog = new DeliveryHistoryDialog(list, null);
							} catch (Exception ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
							dialog.pack();
			    			dialog.open();
						}						
					}					
				}
			}
		});
		
		cbType = new javax.swing.JComboBox();
		cbType.setBackground(Color.WHITE); 
		cbType.setMinimumSize(new Dimension(80, 20));
		
		if(TerminalConfig.isPriceCategoryKunden()) {
			List<Customer> CustList = CustomerDAO.getInstance().findAll();
			if(CustList!=null) {
				for (Customer cust:CustList) {
					try {
						if(cust.getLoyaltyNo()!=null)
							cbType.addItem(cust.getLoyaltyNo());
					}catch(Exception ex) {
						cbType.addItem(String.valueOf(1));
					}

				}	
			}else
				cbType.addItem(String.valueOf(1));

		}else if(TerminalConfig.isPriceCategory()) {
			List<MenuCategory> catList = MenuCategoryDAO.getInstance().findAll();
			Collections.sort(catList, new MenuCategory.ItemComparator());
			int priceCate = 1;
			cbType.addItem(String.valueOf(1));
			try {
				for (MenuCategory cat:catList) {			
					if(priceCate!=cat.getPriceCategory()) {
						priceCate = cat.getPriceCategory();
						cbType.addItem(String.valueOf(priceCate));
					}		
				}
			}catch(Exception ex) {
				cbType.addItem(String.valueOf(1));
			}

		}

		cbType.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketView.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));			
			}
		});

		if(TerminalConfig.isWholeSale()||TerminalConfig.isUpdatePriceCategory()) {
			
			JPanel historyPanel = new JPanel();
			historyPanel.setLayout(new MigLayout());
			historyPanel.setBackground(Color.WHITE);
//			historyPanel.add(btnHistory);
			historyPanel.add(btnUpdateCart);
			lblPanel.add(historyPanel, BorderLayout.EAST);
			JPanel lblPanel1 = new JPanel();
			lblPanel1.setLayout(new MigLayout());
			lblPanel1.setBackground(Color.WHITE);
			lblPanel1.add(new JLabel("Price List"), "cell 0 0, growx");
			lblPanel1.add(cbType, "cell 1 0 2 0, growx");
			lblPanel.add(lblPanel1, BorderLayout.WEST);
		}

		if (restaurant.getTicketpreview() != null)
			btnOrderInfo.setText(restaurant.getTicketpreview() + " (F4)");
		else
			btnOrderInfo.setText(com.floreantpos.POSConstants.ORDER_INFO + " (F4)");
		btnOrderInfo.setBackground(Color.BLACK);
		btnOrderInfo.setForeground(Color.WHITE);

		btnOrderInfo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ticketView.doFinishOrder_();
				
				doViewOrderInfoZWS();
				ticketView.focusToScanid();	
				ticketView.doFinishOrder_Update();
			}
		});
		
	
		btnAngebote.setText(POSConstants.OFFER);
		btnAngebote.setBackground(Color.BLACK);
		btnAngebote.setForeground(Color.WHITE);

		btnAngebote.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doPrintOffer();
			}
		});

		btnKellnerAbschulss.setText(POSConstants.SERVER_PRODUCTIVITY_REPORT);   
		btnKellnerAbschulss.setBackground(Color.BLACK);
		btnKellnerAbschulss.setForeground(Color.WHITE);

		btnKellnerAbschulss.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showServerReport();
			}
		});
		
		PosButton btnSalesAbschulss = new PosButton();
		btnSalesAbschulss.setText("T. Absch.");   
		btnSalesAbschulss.setBackground(Color.BLACK);
		btnSalesAbschulss.setForeground(Color.WHITE);

		btnSalesAbschulss.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SwitchboardView.getInstance().showSalesReport();
			}
		});
		PosButton tips = new PosButton("Trinkgeld");
		if(StringUtils.isNotEmpty(POSConstants.Trinkgeld ))
			tips.setText(POSConstants.Trinkgeld);
		
		setFont(tips);
		tips.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tips.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				 
				int value=0;				
				if(StringUtils.isNotEmpty(POSConstants.Trinkgeld ))
					value = NumberSelectionDialog2.takeIntInput(POSConstants.Trinkgeld);
				else
				    value = NumberSelectionDialog2.takeIntInput("Trinkgeld");
				
				ticketView.setTips(value);
			}
		});
		
		PosButton gutscheinSell = new PosButton("Gutschein Verkauf");
		if(StringUtils.isNotEmpty(POSConstants.Gutschein_Verkaufen ))
			gutscheinSell.setText(POSConstants.Gutschein_Verkaufen);
		
		setFont(gutscheinSell);
		gutscheinSell.setFont(new Font("Tahoma", Font.PLAIN, 20));
		gutscheinSell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ticketView.sellGutschein();				
			}
		});
		
		char firstWord = POSConstants.MORE.charAt(0);
		String str = POSConstants.MORE.substring(1, POSConstants.MORE.length());
		btnMore.setText(firstWord+str.toLowerCase());
		
		btnMore.setBackground(Color.BLACK);
		btnMore.setForeground(Color.WHITE);
		btnMore.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				final JPopupMenu popup = new JPopupMenu();
				popup.setLayout(new GridLayout(3, 3, 5, 5));
				popup.setBackground(new Color(35, 35, 36));

				PosButton lastTickets = new PosButton(POSConstants.LAST_FIVE_BILL);
				lastTickets.setBackground(Color.BLACK);
				lastTickets.setForeground(Color.WHITE);
				lastTickets.setFont(new Font("Tahoma", Font.PLAIN, 20));
				lastTickets.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						popup.setVisible(false);
						performLastTicketsAction();
					}
				});
				
				if (TerminalConfig.isTRINKGELD())
					popup.add(tips);		
				/*if (TerminalConfig.isOpenDrawer() && !TerminalConfig.isHideDrawer())
					popup.add(btnOpenDrawer);*/
				if (!TerminalConfig.isSontiges())
					popup.add(btnMisc);
				if (!TerminalConfig.isCardEnable())
					popup.add(btnCard);
				/*if (!TerminalConfig.isPreviewEnable())
					popup.add(btnOrderInfo);*/
				if (!TerminalConfig.isRabattEnable())
					popup.add(btnRabatt);
				if(!Application.getCurrentUser().isAdministrator()&&Application.getInstance().getRestaurant().isKellenerSalesReport())
					popup.add(btnSalesAbschulss);
				if (!TerminalConfig.isOrders())
					popup.add(btnOpenTicket);
				if (!TerminalConfig.isItemSearch())
					popup.add(btnItemSearch);
				if (!TerminalConfig.isWholeSale())
					popup.add(btnItemUpdate);
				if (!TerminalConfig.isSaldo())
					popup.add(btnPaylater);
				if (!TerminalConfig.isCustomer())
					popup.add(btnCustomer);
				if (TerminalConfig.isServerReport()&&!Application.getCurrentUser().isAdministrator())
					popup.add(btnKellnerAbschulss);
				
				if (TerminalConfig.isRabattDirektEnable())
				popup.add(gutscheinSell);
				
				PosButton addButton =new PosButton("Product Hinzufügen");
				if(StringUtils.isNotEmpty(POSConstants.Produkt_Add))
					addButton.setText(POSConstants.Produkt_Add);
				
				addButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							while (true) {
								MenuItemForm editor = new MenuItemForm(new MenuItem(), true);
								BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
								dialog.setPreferredSize(new Dimension(800,740));
								dialog.open();
								if (dialog.isCanceled())
									return;
							}

						} catch (Throwable x) {
							BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
						}
					}

				});
				popup.add(addButton);

				popup.add(lastTickets);
				popup.add(btnBarbewert);
				popup.show(btnMore, btnMore.getMousePosition().x - 150, btnMore.getMousePosition().y - 250);
			}
		});
		
		btnItemSearch.setText("Art. Suchen");
		if(StringUtils.isNotEmpty(POSConstants.Art_suchen))
			btnItemSearch.setText(POSConstants.Art_suchen);
		 
		btnItemSearch.setForeground(Color.WHITE);
		btnItemSearch.setBackground(Color.BLACK);
		btnItemSearch.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String tickettype;
				if (ticketView.border.getTitle().compareTo(POSConstants.DINE_IN) == 0)
					tickettype = POSConstants.DINE_IN;
				else
					tickettype = POSConstants.HOME_DELIVERY;

				ItemSearchDialog dialog = new ItemSearchDialog(tickettype, ticketView.getPriceCategory());
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

				dialog.setPreferredSize(new Dimension((int) screenSize.getWidth()-5,(int) screenSize.getHeight()-5));
				dialog.setUndecorated(true);
				dialog.pack();
				dialog.open();

				if (!dialog.isCanceled()) {
					MenuItem item = dialog.getMenuItem();
					int count = dialog.getCount();
					List<MenuItem> itemList;
					if (item != null) {
						itemList = MenuItemDAO.getInstance().findByIdTypeNamePrice(item.getItemId(), tickettype, item.getPrice(),
								item.getName());
						for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
							item = itr.next();
							if (item.getParent().getParent().getType().compareTo(tickettype) == 0)
								break;
						}
						if(count>1) {
							for (int i = 0; i < count; i++) {
								itemSelectionListener.itemSelected(item);
							}
						}else            	
							itemSelectionListener.itemSelected(item);
					}
				}
			}
		});
		
		btnItemUpdate.setText("Art. Edit");
		btnItemUpdate.setForeground(Color.WHITE);
		btnItemUpdate.setBackground(Color.BLACK);
		btnItemUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketView.doUpdateItem();				
			}
		});

		btnCustomer = new PosButton(POSConstants.CUSTOMER);
		btnCustomer.setBackground(Color.BLACK);
		btnCustomer.setForeground(Color.WHITE);
		btnCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAddEditCustomer();
			}
		});
		if (restaurant.getDiverse() != null)
			btnMisc.setText(restaurant.getDiverse());
		else
			btnMisc.setText(com.floreantpos.POSConstants.MISC);

		btnMisc.setBackground(Color.BLACK);
		btnMisc.setForeground(Color.WHITE);
		btnMisc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doInsertMisc();
			}
		});

		btnOpenDrawer.setBackground(Color.BLACK);
		btnOpenDrawer.setText(POSConstants.OPEN);
		btnOpenDrawer.setForeground(Color.WHITE);
		btnOpenDrawer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doOpenDrawer();
				ticketView.focusToScanid();
			}
		});

		btnRabatt.setBackground(Color.BLACK);
		btnRabatt.setForeground(Color.WHITE);
		btnRabatt.setText(POSConstants.DISCOUNT);
		btnRabatt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				DiscountDialog dialog = new DiscountDialog(getCurrentTicket());
//				dialog.setPreferredSize(new Dimension(500, 600));
//				dialog.pack();
//				dialog.open();
				addRabatt();
			}
		});
		
		btnRabattd.setText(TerminalConfig.getRabattDirectText());
		setFont(btnRabattd);
		btnRabattd.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (ticketView.getCurrentTicket().getTotalAmount() != null
						&& ticketView.getCurrentTicket().getTotalAmount() > 0.00
						|| ticketView.getTicket().getCouponAndDiscounts() != null
								&& ticketView.getTicket().getCouponAndDiscounts().size() > 0)
					OrderView.getInstance().getTicketView().performGutscheinDirectRabatt();

			}
		});
		
		btnPfand = new PosButton(POSConstants.CREDIT);
		btnPfand.setBackground(Color.BLACK);
		btnPfand.setForeground(Color.WHITE);
		btnPfand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PfandDialog dialog = new PfandDialog(getCurrentTicket());
				dialog.pack();
				dialog.open();
				double totalAmount = getCurrentTicket().getTotalAmountWithoutPfand();

				List<TicketItem> ticketItems = getCurrentTicket().getTicketItems();
				if (totalAmount < 0.00 && ticketItems != null && ticketItems.isEmpty()) {
					ticketView.addDiverseItem();
				}
				ticketView.updateView();
			}
		});

		btnBarbewert.setBackground(Color.BLACK);
		btnBarbewert.setForeground(Color.WHITE);
		btnBarbewert.setText(POSConstants.OFFICIAL_RECEIPT);
		btnBarbewert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketView.doPay(true, 0.00, true, true, false);
			}

		});

		btnCard.setBackground(Color.BLACK);
		btnCard.setForeground(Color.WHITE);
		btnCard.setText("Kartenzahlung");
		if(StringUtils.isNotEmpty(POSConstants.Kartenzahlung ))
			btnCard.setText(POSConstants.Kartenzahlung);
		
		btnCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketView.doPay(true, 0.00, false, false, false);
			}

		});

		btnOpenTicket.setBackground(Color.BLACK);
		btnOpenTicket.setForeground(Color.WHITE);
		btnOpenTicket.setText("Bestellungen");
		if(StringUtils.isNotEmpty(POSConstants.Bestellungen ))
			btnOpenTicket.setText(POSConstants.Bestellungen);
		
		btnOpenTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenTicketListDialog dialog = new OpenTicketListDialog();
				dialog.setPreferredSize(new Dimension(800, 600));
				dialog.pack();
				dialog.open();

			}

		});
		btnPaylater.setBackground(new Color(108, 101, 0));
		btnPaylater.setForeground(Color.WHITE);
		btnPaylater.setText("Saldo");
		btnPaylater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Application.getCurrentUser().isAdministrator()){
				System.out.println("Saldo: ");
				ticketView.doFinishOrder_Saldo();				
				} else {
					ticketView.doFinishOrder();
				}
				ticketView.createNewTicket();
			}

		});

		btnCancel.setBackground(new Color(125, 6, 42));
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setText(POSConstants.CANCEL);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ticketView.doCancelOrder();
			}
		});   
		//    if (TerminalConfig.isItemBarcode())
		//      buttonPanel.add(tfItemSearch);
		if (TerminalConfig.isCustomer())
			buttonPanel.add(btnCustomer);
		if (TerminalConfig.isOpenDrawer())
			buttonPanel.add(btnOpenDrawer);		
		if (TerminalConfig.isSontiges())
			buttonPanel.add(btnMisc);
		if (TerminalConfig.isCardEnable())
			buttonPanel.add(btnCard);
		if (TerminalConfig.isPreviewEnable())
			buttonPanel.add(btnOrderInfo);
		if (TerminalConfig.isRabattEnable())
			buttonPanel.add(btnRabatt);
		if (TerminalConfig.isRabattDirektEnable())
			buttonPanel.add(btnRabattd);
		if (TerminalConfig.isOrders())
			buttonPanel.add(btnOpenTicket);
		if (TerminalConfig.isItemSearch())
			buttonPanel.add(btnItemSearch);
		if (TerminalConfig.isOffer())
			buttonPanel.add(btnAngebote);
		if(TerminalConfig.isWholeSale())
			buttonPanel.add(btnItemUpdate);
		if (TerminalConfig.isSaldo())
			buttonPanel.add(btnPaylater);


		buttonPanel.add(btnPfand);
		buttonPanel.add(btnMore);
		buttonPanel.add(btnCancel);
		buttonPanel.setBackground(new Color(35, 35, 36));
		add(lblPanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
	}// </editor-fold>//GEN-END:initComponents
	public boolean retour;  

	public void setRetour(boolean retour) {
		this.retour = retour;
	}
	
	public void addRabatt() {	
		if(ticketView.getTicket().getTotalAmount()!=null&&ticketView.getTicket().getTotalAmount()>0.00) {			
			CouponAndDiscountDialog dialog = new CouponAndDiscountDialog();
			dialog.setTitle("Waehlen Sie die Rabatt..");
			dialog.setTicket(ticketView.getTicket());
			try {
				dialog.initData();
			}catch(Exception ex) {

			}
			dialog.open();
			if (!dialog.isCanceled()) {
				//getCurrentTicket().calculatePrice(dialog.getSelectedCoupon().getValue(), false);
				getCurrentTicket().addTocouponAndDiscounts(dialog.getSelectedCoupon());
			ticketView.updateAllView();
			} else {
				return;
			}
		}
	}	

	public void setFont(PosButton button) {
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLACK);
	}

	public void performLastTicketsAction() {
		LastTicketListDialog dialog = new LastTicketListDialog();
		dialog.setPreferredSize(new Dimension(800, 600));
		dialog.pack();
		dialog.open();
	}

	private void doOpenDrawer() {
		User user = Application.getCurrentUser();
		int flag = 0;
		if (user != null) {
			UserType userType = user.getType();
			Set<UserPermission> permissions = userType.getPermissions();
			for (UserPermission permission : permissions) {
				if ((permission.getName().equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()))
						|| (permission.getName().equals(UserPermission.PERFORM_MANAGER_TASK.getName()))) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)
							|| (permission.equals(UserPermission.PERFORM_MANAGER_TASK)))
						flag = 1;
				}
			}
		}
		if (flag == 0)
			return;
		
		if (!ticketView.executeOpenCashDrawer()) {
			POSMessageDialog.showError("Kassenschublade nicht konfiguriert");
		}
	}

	public void updateTipsAmount(double amount) {
		String displayText = lblDisplay.getText();
		lblDisplay.setText((StringUtils.isNotBlank(displayText) ? displayText : "") + " TRINKGELD:"
				+ NumberUtil.formatNumber(amount) + " " + Application.getCurrencySymbol());
		ticketView.updateAmount();
	}

	public void setDisplay(int count, String rabat) {
		 
		lblDisplay.setText("");
		boolean pfandOrGiveAwayAvailable = false;
		if(currentTicket!=null&&currentTicket.getDiscountAmount()>0)
		   rabat = POSConstants.DISCOUNT+" "+NumberUtil.formatNumber(currentTicket.getDiscountAmount())+" "+Application.getCurrencySymbol();

		
		String pfandText = StringUtils.EMPTY;
		if (currentTicket != null && currentTicket.getPfand() != null && currentTicket.getPfand().compareTo(0.00) > 0) {
			pfandText = "[ " + TerminalConfig.getPfand1() + ": " + NumberUtil.formatNumber(currentTicket.getPfand())
			+ " € ] ";
			lblDisplay.setText(pfandText);
			lblDisplay.setVisible(true);
			pfandOrGiveAwayAvailable = true;
		}

		if (currentTicket != null && currentTicket.getPfand2() != null && currentTicket.getPfand2().compareTo(0.00) > 0) {
			pfandText += "[ " + TerminalConfig.getPfand2() + ": " + NumberUtil.formatNumber(currentTicket.getPfand2())
			+ " € ] ";
			lblDisplay.setText(pfandText);
			lblDisplay.setVisible(true);
			pfandOrGiveAwayAvailable = true;
		}

		if (currentTicket != null && currentTicket.getPfand3() != null && currentTicket.getPfand3().compareTo(0.00) > 0) {
			pfandText += "[ " + TerminalConfig.getPfand3() + ": " + NumberUtil.formatNumber(currentTicket.getPfand3())
			+ " € ] ";
			lblDisplay.setText(pfandText);
			lblDisplay.setVisible(true);
			pfandOrGiveAwayAvailable = true;
		}

		String KundenName = "";
		String KundenNr = "";
		String kunden = "";
		try {
			KundenName = currentTicket.getProperty(Ticket.CUSTOMER_NAME);
			KundenNr = currentTicket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);
		} catch (Exception ex){

		}

		if (currentTicket != null && !StringUtils.isEmpty(KundenName)||currentTicket != null && !StringUtils.isEmpty(KundenNr)) {
			kunden += POSConstants.CUSTOMER +": "+ KundenName+" "+KundenNr;
			lblDisplay.setText(pfandText);
			lblDisplay.setVisible(true);        
		}

		if (!pfandOrGiveAwayAvailable) {
			lblDisplay.setText("");
			lblDisplay.setVisible(false);
		}

		if(count>1||StringUtils.isNotEmpty(rabat)&&count>0||StringUtils.isNotEmpty(pfandText)&&count>0||StringUtils.isNotEmpty(kunden)&&count>0) {		
			 
			lblDisplay.setText(POSConstants.TOTAL_COUNT+": "+count+"  "+" "+kunden+" "+pfandText+"  "+rabat);
			lblDisplay.setVisible(true);
		} else if(count==1||StringUtils.isNotEmpty(rabat)&&count>0||StringUtils.isNotEmpty(pfandText)&&count>0||StringUtils.isNotEmpty(kunden)&&count>0) {		
			 
			lblDisplay.setText(kunden+" "+pfandText+"  "+rabat);
			lblDisplay.setVisible(true);
		} else if(StringUtils.isNotEmpty(kunden)||StringUtils.isNotEmpty(pfandText)||StringUtils.isNotEmpty(rabat)) {
			 
			lblDisplay.setText(kunden+" "+pfandText+"  "+rabat);
			lblDisplay.setVisible(true);
		}
	}

	private synchronized void doFinishOrder(Ticket ticket) {
		try {

			List<TicketItem> ticketItemList = ticket.getDeletedItems();
			if (ticketItemList != null) {
				JReportPrintService.printDeletedTicketToKitchen(ticket);
				ticket.clearDeletedItems();
			}
			updateModel(ticket);

			OrderController.saveOrder(ticket);
			//OrderController.saveOrder(ticket);

		} catch (StaleObjectStateException e) {
			POSMessageDialog
			.showError("Speichern fehlgeschlagen, das Ticket wurde bereits von einer anderen Person bearbeitet");
			return;
		} catch (PosException x) {
			POSMessageDialog.showError(x.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_doFinishOrder

	private synchronized void updateModel(Ticket ticket) {
		if (ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0) {
			throw new PosException(com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
		}
		ticket.calculatePrice();
	}

	public void createNewTicket() {
		try {
			OrderServiceExtension orderService = new DefaultOrderServiceExtension();
			orderService.createNewTicket(TicketType.DINE_IN);

			if (orderService.getSelectedTable() != 0) {
				String tableSel = PosGuiUtil.getSelectedTable() + "";
				List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();
				for (Iterator<Ticket> itr = ticketList.iterator(); itr.hasNext();) {
					Ticket ticket = itr.next();
					if (ticket.getType() == TicketType.DINE_IN && (!ticket.isPaid())) {
						if (ticket.getTableNumbers().compareTo(tableSel) == 0) {
							editTicket(ticket);
							break;
						}
					}
				}
			}

		} catch (TicketAlreadyExistsException e) {
			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.EDIT_TICKET_CONFIRMATION,
					POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	public void editTicket(Ticket ticket) {
		if (ticket.isPaid()) {
			POSMessageDialog.showMessage("Bezahlte Rechnung kann nicht bearbeitet werden");
			return;
		}

		Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticket.getId());
		OrderView.getInstance().setCurrentTicket(ticketToEdit);

		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	protected void doAddEditCustomer() {
		CustomerSelectionDialog dialog = new CustomerSelectionDialog(getCurrentTicket());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(new Dimension((int) screenSize.getWidth()-5,(int) screenSize.getHeight()-5));
		dialog.open();
	}

	public void clearCustomer() {
		lblDisplay.setText("");
	}
	
	public void setCustomer(Ticket ticket, Customer cust) {
		String text = "";
		String deliveryCost = "";
		String customerName = "";
		String customerAddress = "";
		String customerTel = "";

		if (ticket != null && ticket.getProperty(Ticket.CUSTOMER_ADDRESS) != null) {
			customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
			customerAddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS) + " "
					+ ticket.getProperty(Ticket.CUSTOMER_DOOR) + " " + ticket.getProperty(Ticket.CUSTOMER_POSTCODE)
					+ " " + ticket.getProperty(Ticket.CUSTOMER_CITYNAME);
			;
			if (ticket.getProperty(Ticket.CUSTOMER_PHONE) != null)
				customerTel = "Tel. " + ticket.getProperty(Ticket.CUSTOMER_PHONE);

			deliveryCost = NumberUtil.formatNumber(ticket.getDeliveryCost());

		} else if (cust != null) {
			customerName = cust.getName();
			customerAddress = cust.getAddress() + " " + cust.getDoorNo() + " " + cust.getZipCode() + " "
					+ cust.getCity();
			customerTel = "Tel. " + cust.getTelephoneNo();
		}
		/*
		 * if ((!StringUtil.isEmpty(serverName))) { text += serverName; }
		 */
		if (customerName != null && (!StringUtil.isEmpty(customerName))) {
			text += customerName;
		}
		if (customerAddress != null && !StringUtil.isEmpty(customerAddress)) {
			text += ", " + customerAddress;
		}
		if (customerTel != null && !StringUtil.isEmpty(customerTel)) {
			text += ", " + customerTel;
		}
		if (!StringUtil.isEmpty(deliveryCost) && deliveryCost.length() > 1 && deliveryCost.compareTo("0,00") != 0) {
			text += " // Lk.: " + deliveryCost + " €";
		}

		lblDisplay.setText(text);
	}
	
	private void doInsertMisc() {// GEN-FIRST:event_doInsertMisc
//		MiscTicketItemDialog dialog = new MiscTicketItemDialog(Application.getPosWindow(), true,
//				getCurrentTicket().getType().name());
		
		
		MiscTicketItemmDialog dialog = new MiscTicketItemmDialog(Application.getPosWindow(), true,
				getCurrentTicket().getType().name(), null, false, "");
		
		
		dialog.setSize(new Dimension(700, 400));
		
//		dialog.setPreferredSize(new Dimension(700, 500));
		dialog.open();
		if (!dialog.isCanceled()) {
			TicketItem ticketItem = dialog.getTicketItem();
			if (ticketItem != null) {
				RootView.getInstance().getOrderView().getTicketView().addTicketItem(ticketItem);
				RootView.getInstance().getOrderView().getTicketView().addTseItem(ticketItem, getCurrentTicket());
			}
		}
	}// GEN-LAST:event_doInsertMisc
	
	private void doViewOrderInfoZWS() {// GEN-FIRST:event_btnOrderInfoActionPerformed
		try {

			Ticket ticket = getCurrentTicket();

			if (ticket == null)
				System.out.println("ticket null");
			List<Ticket> tickets = new ArrayList<Ticket>();
			tickets.add(ticket);

			OrderInfoViewZWS view = new OrderInfoViewZWS(tickets);

			OrderInfoDialogZWS dialog = new OrderInfoDialogZWS(view, true);

			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			dialog.setLocationRelativeTo(Application.getPosWindow());

			dialog.setVisible(true);

		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void doViewOrderInfo() {// GEN-FIRST:event_btnOrderInfoActionPerformed
		try {

			Ticket ticket = getCurrentTicket();

			if (ticket == null)
				System.out.println("ticket null");
			List<Ticket> tickets = new ArrayList<Ticket>();
			tickets.add(ticket);

			OrderInfoView view = new OrderInfoView(tickets);

			OrderInfoDialog dialog = new OrderInfoDialog(view, true);

			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			dialog.setLocationRelativeTo(Application.getPosWindow());

			dialog.setVisible(true);

		} catch (Exception e) {
			// TODO: handle exception
		}
		// TicketDetailDialog dialog = new
		// TicketDetailDialog(Application.getPosWindow(), true);
		// dialog.setTicket(getCurrentTicket());
		// dialog.open();
		//
		// if(!dialog.isCanceled()) {
		// OrderView.getInstance().getTicketView().updateView();
		// }

	}// GEN-LAST:event_btnOrderInfoActionPerformed
	
	
	private void doPrintOffer() {// GEN-FIRST:event_btnOrderInfoActionPerformed
		try {
			Ticket ticket = getCurrentTicket();
			if (ticket != null) {
				TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
				PosTransaction transaction = new PosTransaction();
				HashMap map =JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, new PosTransaction());
				map.put("mst19",  null);
				map.put("mst7", null);
				map.put("taxAmount7",  null);
				map.put("taxAmount0", null);
				map.put("taxAmount", null);
				map.put("zahlartText", " Zahlungsart: Angebot");
				JasperPrint jasperPrint = JReportPrintService.createA4Print(ticket, map, null);
					jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
				JReportPrintService.printQuitelyA4(jasperPrint);
				ticketView.tseCancelOrder(ticket, ticket.getTicketItems(), true);
			}
			ticketView.createNewTicket();
			ticketView.focusToScanid();
			ticketView.updateAllView();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}// GEN-LAST:event_btnOrderInfoActionPerformed


	private void btnCustomerNumberActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerNumberActionPerformed
		updateGuestNumber();
	}// GEN-LAST:event_btnCustomerNumberActionPerformed

	private void updateGuestNumber() {
		Ticket thisTicket = getCurrentTicket();
		int guestNumber = thisTicket.getNumberOfGuests();

		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(com.floreantpos.POSConstants.NUMBER_OF_GUESTS);
		dialog.setValue(guestNumber);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		guestNumber = (int) dialog.getValue();
		if (guestNumber == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.GUEST_NUMBER_CANNOT_BE_0);
			return;
		}

		thisTicket.setNumberOfGuests(guestNumber);
		updateView();
	}

	private void btnTableNumberActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTableNumberActionPerformed
		updateTableNumber();
	}// GEN-LAST:event_btnTableNumberActionPerformed

	private void updateTableNumber() {
		Session session = null;
		Transaction transaction = null;

		try {

			Ticket thisTicket = getCurrentTicket();

			FloorLayoutPlugin floorLayoutPlugin = Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
			List<ShopTable> tables = null;

			if (floorLayoutPlugin != null) {
				tables = floorLayoutPlugin.captureTableNumbers(thisTicket);
			} else {
				tables = PosGuiUtil.captureTable(thisTicket, true);
			}

			if (tables == null) {
				return;
			}

			session = TicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			clearShopTable(session, thisTicket);
			session.saveOrUpdate(thisTicket);

			for (ShopTable shopTable : tables) {
				shopTable.setOccupied(true);
				session.merge(shopTable);

				thisTicket.addTotables(shopTable);
			}

			session.merge(thisTicket);
			transaction.commit();

			updateView();

		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void clearShopTable(Session session, Ticket thisTicket) {
		Set<ShopTable> tables2 = thisTicket.getTables();

		if (tables2 == null)
			return;

		for (ShopTable shopTable : tables2) {
			shopTable.setOccupied(false);
			shopTable.setBooked(false);

			session.saveOrUpdate(shopTable);
		}

		tables2.clear();
	}

	public com.floreantpos.swing.PosButton btnOpenDrawer;
	private com.floreantpos.swing.PosButton btnCard;
	private com.floreantpos.swing.PosButton btnMore;
	private com.floreantpos.swing.PosButton btnBarbewert;
	private com.floreantpos.swing.PosButton btnOpenTicket;
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnPaylater;
	private com.floreantpos.swing.PosButton btnRabatt;
	private com.floreantpos.swing.PosButton btnRabattd;
	private com.floreantpos.swing.PosButton btnMisc;
	private com.floreantpos.swing.PosButton btnPfand;
	private com.floreantpos.swing.PosButton btnOrderInfo;
	private com.floreantpos.swing.PosButton btnItemSearch;
	private com.floreantpos.swing.PosButton btnItemUpdate;
	private com.floreantpos.swing.PosButton btnAngebote;
	private com.floreantpos.swing.PosButton btnCustomer;
	private com.floreantpos.swing.PosButton btnKellnerAbschulss;
	public JTextField tfItemSearch;

	private JPanel buttonPanel;

	// End of variables declaration//GEN-END:variables

	public void updateView() {
	}

	public Ticket getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		this.currentTicket = currentTicket;
		updateView();
	}

	public ItemSelectionListener getItemSelectionListener() {
		return itemSelectionListener;
	}

	public void setItemSelectionListener(ItemSelectionListener itemSelectionListener) {

		// Stack Trace exmaple
		/*
		 * StackTraceElement[] arr = Thread.currentThread().getStackTrace(); for(int
		 * i=0; i<arr.length; i++) { System.out.println(arr[i].toString()); }
		 */

		this.itemSelectionListener = itemSelectionListener;
	}

	public boolean searchItem(String barcode) {
		MenuItem item = null;
		barcode = barcode.trim();

		boolean found = false;
		for (MenuItem menuItem : MenuItemDAO.getInstance().findByBarcode(barcode)) {
			if (menuItem == null) {
				continue;
			} else {
				item = menuItem;
				found = true;
				break;
			}
		}
		if (item != null) {
			if(item.isPfand()) {
				itemSelectionListener.pfandSelected(item, getCurrentTicket(), true);
				
			}else{
				itemSelectionListener.itemSelected(item, getCurrentTicket());
				if (item.getSubitemid() != null) {
					List<MenuItem> itemlist = MenuItemDAO.getInstance().findById(true, item.getSubitemid());
					for (Iterator<MenuItem> itr = itemlist.iterator(); itr.hasNext();) {
						MenuItem subitem = itr.next();
						if (subitem == null)
							continue;				
						itemSelectionListener.pfandSelected(subitem, getCurrentTicket(), false);
						break;
					}
				}	
				}
			
		}

		return found;
	}


	// Retour is Going on




	public boolean searchItemForRetour(String barcode) {
		MenuItem item = null;	    
		int delimIndex = barcode.indexOf('*');
		String itemId = "";
		int itemMultiple = 1;
		if (delimIndex != -1) {
			try {				
				itemMultiple = Integer.parseInt(barcode.substring(0, delimIndex));
				itemId = barcode.substring(delimIndex + 1, barcode.length());
			} catch (Exception e) {
				POSMessageDialog.showError(POSConstants.PROVIDE_CORRECT_FORMAT);				
			}
		} else {
			itemId = barcode;
		}    


		itemId = itemId.trim();
		boolean found = false;
		List<MenuItem> itemList = new ArrayList<>();
		try {
			itemList = MenuItemDAO.getInstance().findByBarcode(itemId);
		}catch(Exception ex) {

		}

		if(itemList.isEmpty())
			itemList = MenuItemDAO.getInstance().findById(false, itemId);	    
		for (MenuItem menuItem :itemList ) {
			if (menuItem == null) 
				continue;			
				item = menuItem;
				found = true;
				itemSelectionListener.pfandSelected(menuItem, getCurrentTicket(), true);
				if (menuItem.getSubitemid() != null) {
					List<MenuItem> itemlist = MenuItemDAO.getInstance().findById(true, menuItem.getSubitemid());
					for (Iterator<MenuItem> itr = itemlist.iterator(); itr.hasNext();) {
						MenuItem itemm = itr.next();
						if (itemm == null)
							continue;
						itemSelectionListener.pfandSelected(itemm, getCurrentTicket(), true);
						break;
					}
				}				
				
				break;			
		}
//		if (item != null) {
//			TicketItem item_new = new TicketItem();
//			item_new.setId(item.getId());
//			item_new.setItemId(Integer.valueOf(item.getItemId()));
//			item_new.setTotalAmountWithoutModifiers(itemMultiple*(0-item.getPrice()));
//			item_new.setItemCount(itemMultiple);
//			item_new.setBeverage(item.getParent().getParent().isBeverage());
//			item_new.setCategoryName(item.getParent().getParent().getName());
//			item_new.setGroupName(item.getParent().getDescription());
//			item_new.setHasModifiers(false);
//			item_new.setShouldPrintToKitchen(false);
//			item_new.setUnitPrice(0-item.getPrice());
//			item_new.setPfand(true);
//			item_new.setName(item.getName());
//			item_new.setPriceIncludesTax(true);			
//			item_new.setTaxRate(item.getTax().getRate());
//			item_new.setBarcode(item.getBarcode());
//			item_new.setTotalAmountWithoutModifiers(itemMultiple*(0-item.getPrice()));
//			ticketView.addTicketItem(item_new);	    	
//		}
		return found;
	}

	public boolean checkPin() {
		final User user = Application.getCurrentUser();
		if (user != null && user.getFirstName().equals("Master")||user != null&&user.isAdministrator()) {
			return true;
		}
		String rabatPin = TerminalConfig.getRabattPin();

		if(rabatPin.isEmpty()) {
			return true;
		}


		PasswordfieldDialog dialog = new PasswordfieldDialog();
		dialog.setTitle("Bitte geben Sie den Rabatt-Pin");
		dialog.pack();
		dialog.open();
		if(dialog.isCanceled())
			return false;
		String PIN = dialog.getValue();

		if (PIN.equals(rabatPin)) {
			return true;
		}else {			
			POSMessageDialog.showError("Ungueltig pin");
			return false;	

		}

	}

	public PosButton getPayLaterButton() {
		return this.btnPaylater;
	}

	private void showServerReport() {
		try {
			Report report = new ServerReport();
			report.setType(3);
			ReportViewer viewer = new ReportViewer(report);
			ReportViewerDialog dialog = new ReportViewerDialog(viewer);
			viewer.setDialog(dialog);
			dialog.setSize(800, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}
}
