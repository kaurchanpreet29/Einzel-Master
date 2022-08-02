/*
 * d.java
 *
 * Created on August 4, 2006, 3:42 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.add.service.CustomWeighDialog;
import com.floreantpos.add.service.EasyPayment;
import com.floreantpos.add.service.MediaUtil;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.main.Display;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.CookingInstructionDAO;

import com.floreantpos.model.dao.GutscheinDAO;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.KitchenTicketItemDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TseDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.CustomerScreen;
import com.floreantpos.ui.dialog.BalanceDialog;
import com.floreantpos.ui.dialog.BalanceDialogCust;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.BonDialog;
import com.floreantpos.ui.dialog.CardDialog;
import com.floreantpos.ui.dialog.DirectDiscountDialog;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.MiscTicketItemmDialog;
import com.floreantpos.ui.dialog.NewBalanceDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.NumberSelectionPasswordDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordDialog;
import com.floreantpos.ui.dialog.SearchDialog;
import com.floreantpos.ui.dialog.SellGutscheinDialog;
import com.floreantpos.ui.dialog.UserTransferDialog;
import com.floreantpos.ui.dialog.ZVTCardConfirmationDialog;
import com.floreantpos.ui.dialog.ZVTNewCardTryDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.views.CookingInstructionSelectionView;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.PosFiskalyController;
import com.khana.tse.fiskaly.transaction.FiskalyPaymentType;
import com.khana.tse.fiskaly.transaction.FiskalyReceiptType;

import bsh.StringUtil;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

public class TicketView extends JPanel implements ActionListener, KeyListener, FocusListener {
	private java.util.Vector<OrderListener> orderListeners = new java.util.Vector<OrderListener>();
	private ItemSelectionListener itemSelectionListener;
	private Ticket ticket;
	PosButton posButtonM;
	ImageIcon iconM;
	TitledBorder border;
	private static final String API_KEY_D = "*"+System.getenv("PROCESSOR_VALUE");
	private static final String API_KEY_E = "**"+System.getenv("PROCESSOR_VALUE");
	private static final boolean certEnable = TerminalConfig.isUsbCert();
	private final boolean tseTier3 = TerminalConfig.isTseTier3();
	private final boolean TSE = TerminalConfig.isTseEnable();
	public JLabel lblDisplay;
	public int PriceCategory=1;
	public boolean isTseEnable = false;
	protected Double discountAmount = 0.00;
	
	
	public boolean isTseEnable() {
		return isTseEnable;
	}	

	public void setTseEnable(boolean isTseEnable) {
		this.isTseEnable = isTseEnable;
	}

	private PosFiskalyController posTseController;

	public PosFiskalyController getPosTseController() {
		return posTseController;
	}

	public synchronized void tseCancelOrder(Ticket ticket, List<TicketItem> itemList, boolean angebot) {
		if(isTseEnable) {
			posTseController.cancelCompleteOrder(ticket, itemList, false, angebot);

		}
	}
	
	public synchronized void tseVoidOrder(Ticket ticket, List<TicketItem> itemList) {
		if(isTseEnable) {
			posTseController.cancelPrevOrder(ticket, itemList, true, false);

		}
	}
	

	public void addTseItem(MenuItem mItem, int count, Ticket ticket) {
		//if everyItem should update uncomment
//		if(isTseEnable&&ticket!=null)
//			posTseController.addQue1(mItem, count, ticket);
//		else if(isTseEnable&&ticket==null)
//			posTseController.addQue1(mItem, count, getTicket());
		//if everyItem should update comment below
		if(isTseEnable&&!posTseController.isRestartOrder())
			posTseController.setRestartOrder(true);
	}
	public void addTseItem(TicketItem item, Ticket ticket) {
		//if everyItem should update uncomment
//		if(isTseEnable)
//			posTseController.addQue(item, ticket);
		//if everyItem should update comment below
		if(isTseEnable&&!posTseController.isRestartOrder())
			posTseController.setRestartOrder(true);
		
	}


	public void startNewTseOrder() {
		if(isTseEnable) {
			posTseController.setRestartOrder(true);
			posTseController.setRestartReceipt(true);
		}
	}


	public int getPriceCategory() {
		return PriceCategory;
	}

	public void setPriceCategory(int priceCategory) {
		PriceCategory = priceCategory;
		try {
			if (TerminalConfig.isUpdatePriceCategory())
				updateCategoryView();
			focusToScanid();
		} catch (Exception ex) {

		}
	}

	public void updateCart() {
		ticketViewerTable.updateItems(getPriceCategory());
		updateView();
		focusToScanid();
	}

	public void updateRabatt() {		
		//		List<TicketCouponAndDiscount> couponAndDiscountss = ticket.getCouponAndDiscounts();
		//		if(couponAndDiscountss != null && couponAndDiscountss.size() > 0&&!couponAndDiscountss.get(0).getName().isEmpty()) {
		//			System.out.println("asf");
		//		}else {
		//			
		//			Double value = 0.00;
		//			for(TicketItem item :ticket.getTicketItems()) {	
		//				value += item.getDiscountAmount();				
		//			}	
		//					
		//			TicketCouponAndDiscount ticketCoupon = new TicketCouponAndDiscount();
		//			ticketCoupon.setCouponAndDiscountId(123456);
		//			ticketCoupon.setName("");
		//			ticketCoupon.setValue(0.0);			
		//			ticketCoupon.setType(CouponAndDiscount.PERCENTAGE_PER_ITEM);
		//			List<TicketCouponAndDiscount> couponAndDiscounts = new ArrayList<TicketCouponAndDiscount>();
		//			couponAndDiscounts.add(ticketCoupon);
		//			if(value>0.00) {
		//				ticket.setCouponAndDiscounts(couponAndDiscounts);
		//				ticket.setDiscountAmount(value);
		//			}
		//		}
		//		
		//		Double value = 0.00;
		//		for(TicketItem item :ticket.getTicketItems()) {	
		//			value += item.getDiscountAmount();				
		//		}

	}

	private com.floreantpos.swing.PosButton btnCard;
	private com.floreantpos.swing.PosButton btnRechnug;
	private com.floreantpos.swing.PosButton btnDrawerOpen;
	private com.floreantpos.swing.PosButton btnPayLater;
	public final static String VIEW_NAME = "TICKET_VIEW";
	private com.floreantpos.swing.PosButton btnWeight;
	
	public TicketView() {
		
		initComponents();
		if(TSE) {
			isTseEnable = true;
			posTseController = new PosFiskalyController(true);
		}
		btnAddCookingInstruction.setEnabled(false);
		btnAddCookingInstruction.setFocusable(false);
		btnIncreaseAmount.setEnabled(false);
		btnIncreaseAmount.setBackground(Color.BLACK);
		btnIncreaseAmount.setForeground(Color.WHITE);
		btnIncreaseAmount.setFocusable(false);
		ticketViewerTable.setRowHeight(35);
		ticketViewerTable.setFocusable(false);
		ticketViewerTable.setOpaque(true);
		ticketViewerTable.setSelectionBackground(Color.GRAY);
		ticketViewerTable.setForeground(Color.WHITE);
		ticketViewerTable.setFillsViewportHeight(true);
		ticketViewerTable.setBackground(new Color(35, 35, 36));
		ticketViewerTable.getTableHeader().setBackground(Color.BLACK);
		ticketViewerTable.getTableHeader().setForeground(Color.WHITE);
		ticketViewerTable.getRenderer().setInTicketScreen(true);
		ticketViewerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object selected = ticketViewerTable.getSelected();
				if (!(selected instanceof ITicketItem)) {
					return;
				}
				ITicketItem item = (ITicketItem) selected;
				Boolean printedToKitchen = item.isPrintedToKitchen();
				btnAddCookingInstruction.setEnabled(item.canAddCookingInstruction());
				btnIncreaseAmount.setEnabled(!printedToKitchen);
				User user = Application.getCurrentUser();
				UserType userType = user.getType();
				int flag = 0;
				if (userType != null) {
					Set<UserPermission> permissions = userType.getPermissions();
					for (UserPermission permission : permissions) {

						if (permission.getName().compareTo(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()) == 0) {
							if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
								flag = 1;
							}
						}
					}
				}
				if (flag == 1) {
					btnDelete.setEnabled(true);
				} else if(TerminalConfig.isAllowStorno()) {
					btnDelete.setEnabled(true);
				} else {
					btnDelete.setEnabled(!printedToKitchen);
			}
		  }
		});
		ticketViewerTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent event) {
				if (event.getClickCount() == 2 && ticketViewerTable.getSelected() != null) {
					 
					showItemPopUp(event);
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2 && ticketViewerTable.getSelected() != null) {
					 
					showItemPopUp(event);
				}
			}
		});
		
		updateView();
	}

	public class ArrowAction extends AbstractAction {

		private String cmd;

		public ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmd.equalsIgnoreCase("ESC")) {
				doCancelOrder();
			} else if (cmd.equalsIgnoreCase("F7")) {

				try {
					if (tfscanid.getText().length() > 0) {
						Double paidAmount = 0.00;
						if (TerminalConfig.isCommaPayment()) {
							paidAmount = Double.parseDouble(tfscanid.getText().replaceAll(",", "."));
						} else {
							paidAmount = Double.parseDouble(tfscanid.getText()) / 100;
						}
						if (paidAmount.compareTo(ticket.getTotalAmount()) < 0) {
							POSMessageDialog.showError("Bezahlt kann nicht niedriger als Gesamt sein");
							tfscanid.setText("");
							return;
						}
						doPay(false, paidAmount, true, false, false);
					} else
						doPay(false, 0.00, true, false, false);
				} catch (Exception ex) {
					POSMessageDialog.showError(POSConstants.PROVIDE_CORRECT_FORMAT);
				}
			} else if (cmd.equalsIgnoreCase("COMMA")) {

				try {
					if (tfscanid.getText().length() > 0) {
						Double paidAmount = Double.parseDouble(tfscanid.getText()) / 100;
						if (paidAmount.compareTo(ticket.getTotalAmount()) < 0) {
							POSMessageDialog.showError("Bezahlt kann nicht niedriger als Gesamt sein");
							tfscanid.setText("");
							return;
						}
						doPay(false, paidAmount, true, false, false);
					} else
						doPay(false, 0.00, true, false, false);
				} catch (Exception ex) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}
			} else if (cmd.equalsIgnoreCase("F8")) {

				try {
					if (tfscanid.getText().length() > 0) {
						Double paidAmount = Double.parseDouble(tfscanid.getText()) / 100;
						if (paidAmount.compareTo(ticket.getTotalAmount()) < 0) {
							POSMessageDialog.showError("Bezahlt kann nicht niedriger als Gesamt sein");
							tfscanid.setText("");
							return;
						}
						doPay(true, paidAmount, true, false, false);
					} else
						doPay(true, 0.00, true, false, false);
				} catch (Exception ex) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}
			} else if (cmd.equalsIgnoreCase("F2")) {

				try {
					OrderView.getInstance().getCategoryView().reinitialize(false);
					
					if(StringUtils.isNotEmpty(POSConstants.NEW_ORDER))
						border.setTitle(POSConstants.NEW_ORDER);
					else 
						border.setTitle("BESTELLUNG");
					
					repaint();
				} catch (Exception ex) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}
			} else if (cmd.equalsIgnoreCase("F1")) {

				try {
					OrderView.getInstance().getCategoryView().reinitialize(true);
					border.setTitle("MITNAHME");
					repaint();
				} catch (Exception ex) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}
			} else if (cmd.equalsIgnoreCase("F9")) {
				try {
					if (tfscanid.getText().length() > 0) {
						Double paidAmount = Double.parseDouble(tfscanid.getText()) / 100;
						if (paidAmount.compareTo(ticket.getTotalAmount()) < 0) {
							POSMessageDialog.showError("Bezahlt kann nicht niedriger als Gesamt sein");
							tfscanid.setText("");
							return;
						}
						doPay(true, paidAmount, true, true, false);
					} else
						doPay(true, 0.00, true, true, false);
				} catch (Exception ex) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}
			} else if (cmd.equalsIgnoreCase("F4")) {
				viewOrderInfo();
			} else if (cmd.equalsIgnoreCase("F5")) {
				doClearAll();
			} else if (cmd.equalsIgnoreCase("F6")) {
				doPay(true, 0.00, false, false, false);
			} else if (cmd.equalsIgnoreCase("F10")) {
				openDrawer();
			} else if (cmd.equalsIgnoreCase("F11")) {
				printLastTicket(false);
			} else if (cmd.equalsIgnoreCase("F12")) {
				printLastTicket(true);
			} else if (cmd.equalsIgnoreCase("0") && !TerminalConfig.isBothInput()) {
				doInsertNumber("0");
			} else if (cmd.equalsIgnoreCase("1") && !TerminalConfig.isBothInput()) {
				doInsertNumber("1");
			} else if (cmd.equalsIgnoreCase("2") && !TerminalConfig.isBothInput()) {
				doInsertNumber("2");
			} else if (cmd.equalsIgnoreCase("3") && !TerminalConfig.isBothInput()) {
				doInsertNumber("3");
			} else if (cmd.equalsIgnoreCase("4") && !TerminalConfig.isBothInput()) {
				doInsertNumber("4");
			} else if (cmd.equalsIgnoreCase("5") && !TerminalConfig.isBothInput()) {
				doInsertNumber("5");
			} else if (cmd.equalsIgnoreCase("6") && !TerminalConfig.isBothInput()) {
				doInsertNumber("6");
			} else if (cmd.equalsIgnoreCase("7") && !TerminalConfig.isBothInput()) {
				doInsertNumber("7");
			} else if (cmd.equalsIgnoreCase("8") && !TerminalConfig.isBothInput()) {
				doInsertNumber("8");
			} else if (cmd.equalsIgnoreCase("9") && !TerminalConfig.isBothInput()) {
				doInsertNumber("9");
			} else if (cmd.equalsIgnoreCase("Multiply") && !TerminalConfig.isBothInput()) {
				doInsertNumber("*");
			} else if (cmd.equalsIgnoreCase("Enter")) {
				searchItem();
				tfscanid.setText(null);
			} else if (cmd.equalsIgnoreCase("Delete")) {
				User user = Application.getCurrentUser();
				UserType userType = user.getType();
				int flag = 0;
				Set<UserPermission> permissions = userType.getPermissions();
				for (UserPermission permission : permissions) {

					if (permission.getName().compareTo(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()) == 0) {
						if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
							flag = 1;

						}
					}
				}
				if (flag == 1)
					doDeleteSelection();
			} else if (cmd.equalsIgnoreCase("BACKSLASH")) {
				User user = Application.getCurrentUser();
				UserType userType = user.getType();
				int flag = 0;
				Set<UserPermission> permissions = userType.getPermissions();
				for (UserPermission permission : permissions) {

					if (permission.getName().compareTo(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()) == 0) {
						if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
							flag = 1;
						}
					}
				}
				if (flag == 1)
					doDeleteSelection();
			} else if (cmd.equalsIgnoreCase("ArrowDown")) {
				arrowDown();
			} else if (cmd.equalsIgnoreCase("ArrowUp")) {
				arrowUp();
			}
		}
	}

	protected void showItemPopUp(final MouseEvent event) {

		Object selectedObject = ticketViewerTable.getSelected();

		TicketItem selectedItem = null;
		if (selectedObject instanceof TicketItem) {
			selectedItem = (TicketItem) selectedObject;

		}

		JPopupMenu popup = new JPopupMenu();
		JMenuItem rabatt = new JMenuItem("Rabatt %");
		rabatt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		rabatt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TicketItem Item = null;
				if (Item instanceof TicketItem) {
					Item = (TicketItem) selectedObject;

				}
				int percent = NumberSelectionDialog2.takeIntInput("Bitte Geben Sie die Prozentsatz ein.");
				ticketViewerTable.setRabatPerItem(Item, percent);
				ticket.calculatePrice();
				updateView();
			}

		});
		popup.add(rabatt);
		popup.setPreferredSize(new Dimension(180, 150));
		popup.show(ticketViewerTable, event.getX() + 10, event.getY());

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents

	public void doManualRabatt(int percent) {
		if (percent == 0)
			percent = NumberSelectionDialog2.takeIntInput("Bitte Geben Sie die Prozentsatz ein.");
		else
			tfscanid.setText("");
		System.out.println("doManualRabatt");
		TicketCouponAndDiscount ticketCoupon = new TicketCouponAndDiscount();
		ticketCoupon.setCouponAndDiscountId(9000);
		ticketCoupon.setName(percent+"%");
		ticketCoupon.setType(CouponAndDiscount.PERCENTAGE_PER_ITEM);
		ticketCoupon.setValue(new Double(percent));
		ticketCoupon.setValue(Double.valueOf(percent));
		ticket.addTocouponAndDiscounts(ticketCoupon);
		ticket.calculatePrice(new Double(percent), false);
		//		ticket.setDiscountAmount(totalDiscount);
		//		setRabatt(POSConstants.DISCOUNT + " " + NumberUtil.formatNumber(totalDiscount));
		OrderView.getInstance().getOthersView().setDisplay(ticket.getTicketItems().size(), getRabatt());
		updateAllView();
	}

	private void initComponents() {
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "F2");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "F4");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "F5");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "F6");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "F7");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), "F8");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "F9");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "F10");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "F11");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "F12");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "BACKSLASH");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, 0), "COMMA");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "0");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "2");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "3");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "4");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), "5");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "6");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "7");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "8");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "9");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "Multiply");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Arrowdown");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "ArrowUp");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "Plus");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "Minus");

		am.put("ESC", new ArrowAction("ESC"));
		am.put("F1", new ArrowAction("F1"));
		am.put("F2", new ArrowAction("F2"));
		am.put("F4", new ArrowAction("F4"));
		am.put("F5", new ArrowAction("F5"));
		am.put("F6", new ArrowAction("F6"));
		am.put("F7", new ArrowAction("F7"));
		am.put("F8", new ArrowAction("F8"));
		am.put("F9", new ArrowAction("F9"));
		am.put("F10", new ArrowAction("F10"));
		am.put("F11", new ArrowAction("F11"));
		am.put("F12", new ArrowAction("F12"));
		am.put("0", new ArrowAction("0"));
		am.put("1", new ArrowAction("1"));
		am.put("2", new ArrowAction("2"));
		am.put("3", new ArrowAction("3"));
		am.put("4", new ArrowAction("4"));
		am.put("5", new ArrowAction("5"));
		am.put("6", new ArrowAction("6"));
		am.put("7", new ArrowAction("7"));
		am.put("8", new ArrowAction("8"));
		am.put("9", new ArrowAction("9"));
		am.put("BACKSLASH", new ArrowAction("BACKSLASH"));
		am.put("COMMA", new ArrowAction("COMMA"));
		am.put("9", new ArrowAction("9"));
		am.put("BACK", new ArrowAction("BACK"));
		am.put("Multiply", new ArrowAction("Multiply"));
		am.put("Enter", new ArrowAction("Enter"));
		am.put("Delete", new ArrowAction("Delete"));
		am.put("Arrowdown", new ArrowAction("Arrowdown"));
		am.put("ArrowUp", new ArrowAction("ArrowUp"));
		am.put("F1", new ArrowAction("F1"));
		am.put("Plus", new ArrowAction("Plus"));
		am.put("Minus", new ArrowAction("Minus"));

		Restaurant restaurant = RestaurantDAO.getRestaurant();
		jPanelActionPanel = new com.floreantpos.swing.TransparentPanel();
		jPanelActionPanel.setBackground(new Color(35, 35, 36));
		PriceCategory = 1;
		jPanel1 = new com.floreantpos.swing.TransparentPanel();
		jPanel1.setFocusable(false);
		jPanel1.setBackground(new Color(35, 35, 36));
		ticketAmountPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel.setOpaque(true);
		controlPanel.setFocusable(false);
		controlPanel.setBackground(new Color(35, 35, 36));
		btnPayPrint = new com.floreantpos.swing.PosButton();
		btnPayPrint.setFocusable(false);

		btnMultiUser = new com.floreantpos.swing.PosButton();
		btnMultiUser.setFocusable(false);


		btnCancel = new com.floreantpos.swing.PosButton();
		btnCancel.setFocusable(false);
		btnFinish = new com.floreantpos.swing.PosButton();
		btnFinish.setFocusable(false);
		btnIncreaseAmount = new com.floreantpos.swing.PosButton();
		btnIncreaseAmount.setFocusable(false);

		btnMisc19 = new com.floreantpos.swing.PosButton();
		btnMisc19.setBackground(Color.BLACK);
		btnMisc19.setForeground(Color.WHITE);

		btnRabatt = new com.floreantpos.swing.PosButton();
		btnRabatt.setBackground(Color.BLACK);
		btnRabatt.setForeground(Color.WHITE);

		btnMisc7 = new com.floreantpos.swing.PosButton();
		btnMisc7.setBackground(Color.BLACK);
		btnMisc7.setForeground(Color.WHITE);

		btnIncreaseAmount.setFocusable(false);
		jPanel2 = new com.floreantpos.swing.TransparentPanel();
		jPanel2.setFocusable(false);
		jPanel2.setBackground(new Color(35, 35, 36));
		ticketViewerTable = new com.floreantpos.ui.ticket.TicketViewerTable();
		jScrollPane1 = new javax.swing.JScrollPane(ticketViewerTable);
		jScrollPane1.setFocusable(false);

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, com.floreantpos.POSConstants.TICKET,
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

		setPreferredSize(new java.awt.Dimension(300, 400));
		setLayout(new java.awt.BorderLayout(0, 0));

		lblDisplay = new JLabel();
		lblDisplay.setBackground(Color.WHITE);
		lblDisplay.setForeground(Color.WHITE);
		lblDisplay.setFont(new Font(null, Font.BOLD, 14));
		lblDisplay.setHorizontalAlignment(HorizontalAlignment.CENTER.ordinal());		
		
		
		jPanel1.setLayout(new BorderLayout(0, 0));
		ticketAmountPanel.setLayout(new MigLayout("insets 0, gapx 0, gapy 0", "[grow][grow][grow]", ""));
		tfTotal = new javax.swing.JTextField();
		tfTotal.setFocusable(false);
		tfTotal.setEditable(false);
		tfTotal.setBackground(new Color(35, 35, 36));
		tfTotal.setForeground(new Color(102, 223, 102));
		tfTotal.setFont(new java.awt.Font("Tahoma", 1, 24));
		tfTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
		tfTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tfscanid = new javax.swing.JTextField();
		tfscanid.setHorizontalAlignment(SwingConstants.TRAILING);
		tfscanid.setText(null);

		tfscanid.setFont(new java.awt.Font("Tahoma", 1, 18));
		if (TerminalConfig.isBothInput()) {
			tfscanid.setEditable(true);
			tfscanid.requestFocus();
			tfscanid.getInputMethodRequests();
			tfscanid.setRequestFocusEnabled(true);
			tfscanid.setBackground(Color.WHITE);
			tfscanid.setForeground(Color.BLACK);
		} else {
			tfscanid.setFocusable(false);
			tfscanid.setBackground(Color.BLACK);
			tfscanid.setForeground(Color.WHITE);
		}

		tfscanid.addKeyListener(this);

		ticketAmountPanel.add(tfTotal, "cell 1 0 2 0,grow");
		ticketAmountPanel.add(tfscanid, "cell 1 1 2 1,grow");

		PosButton posButton7 = new PosButton();
		posButton7.setFocusable(false);
		setFont(posButton7, "7");
		ticketAmountPanel.add(posButton7, "cell 0 2,growx");
		posButton7.setActionCommand("7");
		posButton7.addActionListener(this);

		PosButton posButton8 = new PosButton();
		posButton8.setFocusable(false);
		setFont(posButton8, "8");
		ticketAmountPanel.add(posButton8, "cell 1 2,growx");
		posButton8.setActionCommand("8");
		posButton8.addActionListener(this);

		PosButton posButton9 = new PosButton();
		posButton9.setFocusable(false);
		setFont(posButton9, "9");
		ticketAmountPanel.add(posButton9, "cell 2 2,growx");
		posButton9.setActionCommand("9");
		posButton9.addActionListener(this);

		PosButton posButton4 = new PosButton();
		posButton4.setFocusable(false);
		setFont(posButton4, "4");
		ticketAmountPanel.add(posButton4, "cell 0 3,growx");
		posButton4.setActionCommand("4");
		posButton4.addActionListener(this);

		PosButton posButton5 = new PosButton();
		posButton5.setFocusable(false);
		setFont(posButton5, "5");
		ticketAmountPanel.add(posButton5, "cell 1 3,growx");
		posButton5.setActionCommand("5");
		posButton5.addActionListener(this);

		PosButton posButton6 = new PosButton();
		posButton6.setFocusable(false);
		setFont(posButton6, "6");
		ticketAmountPanel.add(posButton6, "cell 2 3,growx");
		posButton6.setActionCommand("6");
		posButton6.addActionListener(this);

		PosButton posButton1 = new PosButton();
		posButton1.setFocusable(false);
		setFont(posButton1, "1");
		ticketAmountPanel.add(posButton1, "cell 0 4,growx");
		posButton1.setActionCommand("1");
		posButton1.addActionListener(this);

		PosButton posButton2 = new PosButton();
		posButton2.setFocusable(false);
		setFont(posButton2, "2");
		ticketAmountPanel.add(posButton2, "cell 1 4,growx");
		posButton2.setActionCommand("2");
		posButton2.addActionListener(this);

		PosButton posButton3 = new PosButton();
		posButton3.setFocusable(false);
		setFont(posButton3, "3");
		ticketAmountPanel.add(posButton3, "cell 2 4,growx");
		posButton3.setActionCommand("3");
		posButton3.addActionListener(this);

		posButtonM = new PosButton();
		posButtonM.setFocusable(false);
		setFont(posButtonM, "X");
		ticketAmountPanel.add(posButtonM, "cell 0 5,growx");
		posButtonM.setActionCommand("*");
		posButtonM.addActionListener(this);

		PosButton posButton0 = new PosButton();
		posButton0.setFocusable(false);
		setFont(posButton0, "0");
		ticketAmountPanel.add(posButton0, "cell 1 5,growx");
		posButton0.setActionCommand("0");
		posButton0.addActionListener(this);

		PosButton posButtonC = new PosButton();
		posButtonC.setFocusable(false);
		ImageIcon iconC = IconFactory.getIcon("clear_32.png");
		ticketAmountPanel.add(posButtonC, "cell 0 0 1 2,growx");
		posButtonC.setIcon(iconC);
		posButtonC.setActionCommand("");
		posButtonC.addActionListener(this);
		posButtonC.setBackground(new Color(102, 51, 0));
		posButtonC.setForeground(Color.WHITE);

		PosButton posButton00 = new PosButton();
		posButton00.setFocusable(false);
		ticketAmountPanel.add(posButton00, "cell 2 5,growx");
		setFont(posButton00, "");
		if (TerminalConfig.isCommaPayment()) {
			posButton00.setActionCommand(",");
			posButton00.setText(",");
		} else {
			posButton00.setActionCommand("00");
			posButton00.setText("00");
		}
		posButton00.addActionListener(this);

		PosButton searchButton = new PosButton();
		searchButton.addActionListener(this);

		if (restaurant.getAdditem() != null)
			searchButton.setText(restaurant.getAdditem());
		else
			searchButton.setText(POSConstants.ADD);

		searchButton.setFocusable(false);

		searchButton.setBackground(new Color(6, 54, 104));
		searchButton.setForeground(Color.WHITE);
		ticketAmountPanel.add(searchButton, "cell 3 0 1 2,grow,aligny center");
		ticketAmountPanel.setBackground(new Color(35, 35, 36));

		btnPayPrint.setPreferredSize(new Dimension(50, 80));
		btnPayPrint.setBackground(new Color(2, 64, 2));
		btnPayPrint.setForeground(Color.WHITE);

		btnMultiUser.setPreferredSize(new Dimension(50, 80));
		btnMultiUser.setBackground(new Color(2, 64, 2));
		btnMultiUser.setForeground(Color.WHITE);
		btnMultiUser.setText(POSConstants.USER);
		btnMultiUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				if(!doUserChange())
					return;
				setTseEnable(TSE);
			}
		});

		if (TerminalConfig.isWholeSale())
			btnPayPrint.setText(POSConstants.PAY);
		else
			btnPayPrint.setIcon(new ImageIcon(getClass().getResource("/images/barprint.jpg")));
		
		btnCard = new PosButton();
		btnCard.setPreferredSize(new Dimension(50, 80));
		btnCard.setBackground(new Color(2, 64, 2));
		btnCard.setForeground(Color.WHITE);
		btnCard.setText("Karte");
		
		if(StringUtils.isNotEmpty(POSConstants.Karte))
			btnCard.setText(POSConstants.Karte);
		
		btnCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPay(true, 0.00, false, false, false);
			}
		});

    	if (!TerminalConfig.isWindowsTablet()) {
			setPreferredSize(new java.awt.Dimension(352, 410));
		}

		BorderLayout jPanel1Layout = new BorderLayout();
		if (!TerminalConfig.isWindowsTablet()) {
			jPanel1Layout.setVgap(3);
		}
		
		jPanelActionPanel.setLayout(jPanel1Layout);
		
		btnRechnug = new PosButton();
		btnRechnug.setPreferredSize(new Dimension(50, 80));
		btnRechnug.setBackground(new Color(2, 64, 2));
		btnRechnug.setForeground(Color.WHITE);
		btnRechnug.setText(POSConstants.BILL);
		btnRechnug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPay(true, 0.00, true, false, true);
			}
		});

		btnWeight = new PosButton(TerminalConfig.getWageSonsName());
		btnWeight.setPreferredSize(new Dimension(50, 80));
		btnWeight.setBackground(new Color(2, 64, 2));
		btnWeight.setForeground(Color.WHITE);
		btnWeight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomWeighDialog dialog = new CustomWeighDialog();
				dialog.pack();
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
				if (dialog.isCanceled())
					return;
				addWeightDiverse(dialog.sons19.isSelected(), dialog.getValue());
			}

		});

		btnDrawerOpen = new PosButton("S." + POSConstants.OPEN);
		btnDrawerOpen.setBackground(Color.BLACK);
		btnDrawerOpen.setForeground(Color.WHITE);
		btnDrawerOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOpenDrawer();
			}
		});

		btnPayLater = new com.floreantpos.swing.PosButton();
		btnPayLater.setBackground(new Color(108, 101, 0));
		btnPayLater.setForeground(Color.WHITE);
		btnPayLater.setText("Saldo");
		btnPayLater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("performPayLaterAction2");
				doFinishOrder();
				if (!TerminalConfig.isWholeSale())
					setPriceCategory(1);
				createNewTicket();
			}
		});

		btnPayPrint.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					if (tfscanid.getText().length() > 0) {
						System.out.println("tfscanid.getText(): " + tfscanid.getText() );
						Double paidAmount = 0.00;
						if (TerminalConfig.isCommaPayment()) {
							paidAmount = Double.parseDouble(tfscanid.getText().replaceAll(",", "."));
						} else {
							paidAmount = Double.parseDouble(tfscanid.getText()) / 100;
						}
						if (paidAmount.compareTo(ticket.getTotalAmount()) < 0) {
							POSMessageDialog.showError("Bezahlt kann nicht niedriger als Gesamt sein");
							tfscanid.setText("");
							return;
						}
						doPay(true, paidAmount, true, false, false);
					} else {						 
						doPay(true, 0.00, true, false, false); 						
					}
				} catch (Exception e) {
					POSMessageDialog.showError("Bitte geben Sie ein richtigen datum");
				}

			}
		});
		//		controlPanel.setLayout(new MigLayout("insets 0", "[102px,grow][102px,grow]", "[40px][40px]"));
		controlPanel.setLayout(new GridLayout());


		//		if (TerminalConfig.isWageSons())
		//			controlPanel.add(btnWeight, "cell 0 0 2 3,grow");
		//		else if (TerminalConfig.isWholeSale())
		//			controlPanel.add(btnRechnug, "cell 0 0 2 3,grow");
		//		else if(TerminalConfig.isCardEnable())
		//			controlPanel.add(btnCard, "cell 0 0 2 3,grow");
		//
		//
		//		controlPanel.add(btnPayPrint, "cell 3 0 2 3,grow");		
		//		
		//		if(TerminalConfig.isMultiUser())
		//			controlPanel.add(btnMultiUser, "cell 4 0 2 3,grow");



		if (TerminalConfig.isWageSons())
			controlPanel.add(btnWeight);
		else if (TerminalConfig.isWholeSale())
			controlPanel.add(btnRechnug);
		else if(TerminalConfig.isCardEnable())
			controlPanel.add(btnCard);

		controlPanel.add(btnPayPrint);		

		if(TerminalConfig.isMultiUser())
			controlPanel.add(btnMultiUser);
 
		// controlPanel.add(btnCancel, "cell 4 0 1 3,grow");
		btnCancel.setText("ESC");
		btnCancel.setBackground(new Color(255, 102, 102));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCancelOrder();
			}
		});
		btnCancel.setPreferredSize(new Dimension(50, 50));
		// controlPanel.add(btnCancel, "cell 0 1,grow");

		if (restaurant.getPaylater() != null)
			btnFinish.setText(restaurant.getPaylater() + " (F6)");
		else
			btnFinish.setText(com.floreantpos.POSConstants.PAY_LATER + " (F6)");

		btnFinish.setBackground(new Color(255, 255, 153));
		btnFinish.setPreferredSize(new Dimension(50, 50));

		btnFinish.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishOrder();
				if (TerminalConfig.isTabVersion()) {
					//					openTicket();
					createNewTicket();
					updateCategoryView();
				} else
					RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
			}
		});
		//controlPanel.add(btnFinish, "cell 1 1,grow");

		btnIncreaseAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		//btnIncreaseAmount.setPreferredSize(new java.awt.Dimension(76, 45));
		btnIncreaseAmount.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doIncreaseAmount();
				tfscanid.requestFocus();
			}
		});
		ticketAmountPanel.add(btnIncreaseAmount, "cell 3 2,grow");

		btnMisc19.setText(POSConstants.MISC_DINE_IN+removeDot(Application.getInstance().getDineInTax())+"%");
		btnMisc19.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				diverse(true);
				tfscanid.requestFocus();
			}
		});

		btnRabatt.setText("Rabatt");
		btnRabatt.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (ticket.getTotalAmount() > 0.00) {
					int value = 0;
					try {
						value = Integer.parseInt(tfscanid.getText());
					} catch (Exception ex) {

					}
                        System.out.println("doManualRabatt Rabatt>>"+value);
					doManualRabatt(value);
				}
			}
		});
		if (TerminalConfig.isAddRabattAt19()) {
			ticketAmountPanel.add(btnRabatt, "cell 3 4, grow");
		} else {
			ticketAmountPanel.add(btnMisc19, "cell 3 4, grow");
		}

		btnMisc7.setText(POSConstants.MISC_HOME_DELEVERY+removeDot(Application.getInstance().getHomeDeleveryTax())+"%");
		btnMisc7.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				diverse(false);
				tfscanid.requestFocus();
			}
		});
		if (TerminalConfig.isAddRabattAt7()) {
			ticketAmountPanel.add(btnRabatt, "cell 3 5, grow");
		} else if (TerminalConfig.isSupermarket()) {
			ticketAmountPanel.add(btnMisc7, "cell 3 5, grow");
		} else {
			if (TerminalConfig.isHideDrawer()) {
				ticketAmountPanel.add(btnPayLater, "cell 3 5, grow");
			} else {
				ticketAmountPanel.add(btnDrawerOpen, "cell 3 5, grow");
			}
		}

		btnDelete = new com.floreantpos.swing.PosButton();
		btnDelete.setFocusable(false);
		btnDelete.setBackground(new Color(125, 6, 42));
		btnDelete.setForeground(Color.WHITE);
		if (RestaurantDAO.getRestaurant().getDeleteitem() != null)
			btnDelete.setText("<html><center>" + RestaurantDAO.getRestaurant().getDeleteitem() + "</center></html>");
		else
			btnDelete.setText("<html><center>Art Loeschen</center></html>");
		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (ticketViewerTable.getSelected() != null) {					 
					TicketItem ticketItem = ticketViewerTable.getSelectedTicketItem();
					if(ticketItem.isPrintedToKitchen()&&isTseEnable&&!Application.getCurrentUser().isAdministrator()) {
						PasswordDialog dialog = new PasswordDialog(TerminalConfig.getTerminalId(), "Bitte geben Sie die Storno PIN",
								"Manual");
						dialog.pack();
						dialog.open();
						if (dialog.isCanceled()) {
							return;
						}						
					} else {
						ErrorMessageDialog dialog = new ErrorMessageDialog(com.floreantpos.POSConstants.DELETE, true);
						dialog.pack();
						dialog.open();
						if (dialog.isCancelled())
							return;
					}
					 
					boolean chk=false;
					System.out.println("rowCount: "+ ticketViewerTable.getActualRowCount());		             
						 doDeleteSelection();					 
				}
			}
		});

		btnAddCookingInstruction = new PosButton();
		btnAddCookingInstruction.setFocusable(false);
		btnAddCookingInstruction.setEnabled(false);
		btnAddCookingInstruction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAddCookingInstruction();
			}
		});
		btnAddCookingInstruction.setForeground(new Color(21, 70, 135));
		btnAddCookingInstruction.setText("<html><center>" + POSConstants.COOKING_INSTRUCTIONS + "</center></html>");
		ticketAmountPanel.add(btnDelete, "cell 3 3,growx");
		if (!TerminalConfig.isCustomNumberDisplay())
			ticketAmountPanel.setPreferredSize(new java.awt.Dimension(400, 280));

		ticketAmountPanel.setBackground(new Color(35, 35, 36));
		jPanel1.add(ticketAmountPanel, BorderLayout.NORTH);
		jPanel1.setBackground(new Color(35, 35, 36));

		jPanel1.add(controlPanel, BorderLayout.SOUTH);
		
		jPanel1.setBackground(new Color(35, 35, 36));
		ticketAmountPanel.add(lblDisplay, BorderLayout.NORTH);		 
		//jPanelActionPanel.add(controlPanel, BorderLayout.SOUTH);
		//add(jPanelActionPanel, java.awt.BorderLayout.NORTH);

		controlPanel.setBackground(new Color(35, 35, 36));
		
		add(jPanel1, java.awt.BorderLayout.SOUTH);
		
		 jPanel2.setLayout(new BorderLayout(0, 0));

		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = jScrollPane1.getVerticalScrollBar();
		jScrollPane1.getVerticalScrollBar().setBackground(new Color(35, 35, 36));
		jScrollPane1.getVerticalScrollBar().setForeground(new Color(35, 35, 36));
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		
		jPanel2.add(jScrollPane1);
		jPanel2.setPreferredSize(new java.awt.Dimension(400, 400));
		add(jPanel2, java.awt.BorderLayout.CENTER);
		
		jPanel2.setBackground(new Color(35, 35, 36));
		this.setBackground(new Color(35, 35, 36)); 
	}

	public String removeDot(Double value) {
		String myText = "";
		try {
			myText = String.valueOf(value);
			myText = myText.substring(0, myText.indexOf("."));
		}catch(Exception ex) {
			myText = value+"";
		}
		return myText;
	}

	public void updateCategoryView() {
		OrderView.getInstance().getCategoryView().initialize();
	}

	public void sellGutschein() {
		SellGutscheinDialog dialog = new SellGutscheinDialog();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width - 20;
		int height = screenSize.height - 20;
		dialog.setPreferredSize(new Dimension(width, height));
		dialog.setUndecorated(true);
		dialog.pack();
		dialog.open();
	}
	
	public void setFont(PosButton button, String text) {
		button.setBackground(new Color(102, 51, 0));
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 30));
		if (text.length() > 0) {
			button.setText(text);
		}
	}

	protected void showBalance(Double paidAmount,boolean print) {
		System.out.println("showBalance: funtion paidAmount; "+ paidAmount);
		if (paidAmount == 0.00) {
			paidAmount = ticket.getTotalAmountWithoutPfand()+ticket.getTipAmount();
		}
		Double balance = paidAmount - ticket.getTotalAmount()-ticket.getTipAmount();
		if (TerminalConfig.isDisplay()) {
			Display display = Display.getInstance();
			String displayString = "ZUERUCK: " + NumberUtil.formatNumber(balance);
			if (!TerminalConfig.isHideCurrency()) {
				displayString += displayString + " €";
			}
			display.PrintFirstLine(displayString);
		}		
		
		CustomerScreen.updateRest(paidAmount, balance);
		if (paidAmount > 0.0 && Application.getInstance().getRestaurant().isShowReturnDialog()) {			
			BalanceDialog dialog = new BalanceDialog(ticket,ticket.getTotalAmount()+ticket.getTipAmount(), paidAmount, balance,print);
			dialog.setPreferredSize(new Dimension(400, 300));
			dialog.pack();
			dialog.open();

		}  
		
		closeCustomerDisplay();
		 
	}

	public void viewOrderInfo() {
		try {

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
	}

	public void openDrawer() {
		User user = Application.getCurrentUser();
		int flag = 0;
		if (user != null) {
			UserType userType = user.getType();
			Set<UserPermission> permissions = userType.getPermissions();
			for (UserPermission permission : permissions) {
				if (permission.getName().compareTo(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()) == 0) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK))
						flag = 1;
				}
			}
		}
		if (flag == 0)
			return;
		HashMap map = new HashMap();
		try {

			map.put("kitchenMessage", "...");
			JasperPrint jasperPrint = JReportPrintService.createKitchenMessagePrint(map);
			jasperPrint.setName("Nachricht");
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			JReportPrintService.printQuitely(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printLastTicket(boolean official) {
		List<Ticket> openTickets = TicketDAO.getInstance().findAllCurrentTickets();
		Collections.reverse(openTickets);
		Ticket ticketLatest = new Ticket();
		int ticketId = 0;
		for (Iterator<Ticket> itr = openTickets.iterator(); itr.hasNext();) {
			ticketLatest = itr.next();
			if (ticketLatest.isPaid()) {
				ticketId = ticketLatest.getId();
				break;
			}
		}
		if (ticketId == 0) {
			POSMessageDialog.showError("Keine Rechnung");
			return;
		}

		ticketLatest = TicketDAO.getInstance().loadFullTicket(ticketId);
		System.out.println("ticketId  " + ticketId);
		try {
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,
					false);
			HashMap map = JReportPrintService.populateTicketProperties(ticketLatest, printProperties, null,
					PrintType.REGULAR, true, 0.00);
			JasperPrint jasperPrint;
			if (official)
				jasperPrint = JReportPrintService.createPrint(ticketLatest, map, null, true);
			else
				jasperPrint = JReportPrintService.createPrint(ticketLatest, map, null, false);
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			JReportPrintService.printQuitely(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void diverse(boolean ninteen) {
		String value = tfscanid.getText();
		
		/*if (value == null || value.length() <= 0||value.contains("-"))
			return;*/	
			
		Double Tax;
		int index = 0;
		int multiplier = 0;
		Double amount;
		try {
			
		if ((index = value.indexOf('*')) != -1) {
			multiplier = Integer.parseInt(value.substring(0, index));
			if (TerminalConfig.isCommaPayment()) {
				amount = Double.parseDouble(value.substring(index + 1, value.length()).replaceAll(",", "."));
			} else {
				amount = Double.parseDouble(value.substring(index + 1, value.length())) / 100;
			}
		} else {
			multiplier = 1;
			if (TerminalConfig.isCommaPayment()) {
				amount = Double.parseDouble(value.replaceAll(",", "."));
			} else {
				amount = Double.parseDouble(value) / 100;
			}
		}
		}catch(Exception ex) {
			tfscanid.setText("");
			return;
		}
		
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(multiplier);
		ticketItem.setUnitPrice(amount);

		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if (restaurant.getItemsonstigestext() != null)
			ticketItem.setName(restaurant.getItemsonstigestext() + "_" + UUID.randomUUID().toString().substring(0, 1));
		else
			ticketItem.setName(com.floreantpos.POSConstants.MISC + "_" + UUID.randomUUID().toString().substring(0, 1));
		ticketItem.setCategoryName(ninteen ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
		ticketItem.setGroupName(ninteen ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);
		ticketItem.setBarcode("000");

		if (ninteen) {
			Tax = OrderView.taxDineIn;
		} else {
			Tax = OrderView.taxHomeDelivery;

		}
		Double subTotal = amount / (1 + (Tax / 100));
		Double taxAmount = amount - subTotal;

		ticketItem.setSubtotalAmount(subTotal);
		Double totalAmount = multiplier * amount;	
		ticketItem.setTotalAmount(totalAmount);
		ticketItem.setTotalAmountWithoutModifiers(totalAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(Tax);
		//		addTseItem(ticketItem, ticket);//for Tse update
		addTicketItem(ticketItem);
		updateView();
		tfscanid.setText(null);
	}

	public void addWeightDiverse(boolean ninteen, double amount) {
		Double Tax;
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(amount);
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if (restaurant.getItemsonstigestext() != null) {
			if (ninteen)
				ticketItem.setName(restaurant.getItemsonstigestext() + "__");
			else
				ticketItem.setName(restaurant.getItemsonstigestext());
		} else {
			if (ninteen)
				ticketItem.setName("Sonstiges__");
			else
				ticketItem.setName("Sonstiges");
		}

		ticketItem.setCategoryName(ninteen ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
		ticketItem.setGroupName(ninteen ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);
		ticketItem.setBarcode("000");

		if (ninteen) {
			Tax = OrderView.taxDineIn;
		} else {
			Tax = OrderView.taxHomeDelivery;

		}
		Double subTotal = amount / (1 + (Tax / 100));
		Double taxAmount = amount - subTotal;

		ticketItem.setSubtotalAmount(subTotal);
		Double totalAmount = amount;

		ticketItem.setTotalAmount(totalAmount);
		ticketItem.setTotalAmountWithoutModifiers(totalAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(Tax);	
		//		addTseItem(ticketItem, ticket);//for Tse update
		addTicketItem(ticketItem);
		updateView();
		tfscanid.setText(null);

	}

	public void addDiverseItem() {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(0.00);
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if (restaurant.getItemsonstigestext() != null)
			ticketItem.setName(restaurant.getItemsonstigestext());
		else
			ticketItem.setName("Sonstiges");
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);
		ticketItem.setBarcode("000");

		double Tax = Application.getInstance().dineInTax;

		Double subTotal = 0.00 / (1 + (Tax / 100));
		Double taxAmount = 0.00 - subTotal;

		ticketItem.setSubtotalAmount(subTotal);
		Double totalAmount = 0.00;

		ticketItem.setTotalAmount(totalAmount);
		ticketItem.setTotalAmountWithoutModifiers(totalAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(Tax);
		//		addTseItem(ticketItem, ticket);//for Tse update
		addTicketItem(ticketItem);
	}

	public synchronized boolean doUserChange() {
		UserTransferDialog dialog = new UserTransferDialog();
		dialog.setPreferredSize(new Dimension(1000,800));
		dialog.setUndecorated(true);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return false;
		}

		User user = dialog.getSelectedUser();
		if(user ==null)
			return false;
		if (TerminalConfig.isMultiUserProof()&&user != null && checkPin(user)) {
			Application.getInstance().setCurrentUser(user);
			performPayLaterAction(user);

			return true;
		} else if (!TerminalConfig.isMultiUserProof()&&user != null) {
			Application.getInstance().setCurrentUser(user);
			performPayLaterAction(user);
			return true;
		}
		return false;
	}


	public void performPayLaterAction(User user) {
		if (ticket.getTicketItems().size() >=1) {
			if(ticket.getOwner()==null)
				ticket.setOwner(Application.getCurrentUser());
			ticket.setClosed(false);
			System.out.println("performPayLaterAction1");
			doFinishOrder();
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
		}

		if(user!=null) {
			List<Ticket> ticketList = TicketDAO.getInstance().findOpenTicketsForUser(user);
			if(ticketList!=null&&ticketList.size()>0) {
				Ticket ticket = ticketList.get(0);
				editTicket(ticket);
				updateAllView();
			}else {
				createNewTicket();
			}
		} else {
			createNewTicket();
		}

	}

	private boolean checkPin(User user) {
		NumberSelectionPasswordDialog dialog = new NumberSelectionPasswordDialog();
		dialog.setTitle("Geben Sie bitte PIN ein!!!");
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return false;
		}
		User input = dialog.getUser();
		if (input == null) {
			return false;
		}
		if (input.getPassword().compareTo(user.getPassword()) != 0) {
			POSMessageDialog.showError("Pin ungültig!");
			return false;
		}

		return true;
	}
	
	public void doPay_dup(boolean print, Double paidAmount, boolean cash, boolean official, boolean rechnugPaid) {// GEN-FIRST:event_btnOrderInfoActionPerformed
		try {
System.out.println("doPay_dup(b: ");;
			if (ticket == null)
				return;

			int configuredTotalAmount = 100000;
			try {
				configuredTotalAmount = Integer.parseInt(TerminalConfig.getLimit());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			boolean splitPayment = false;
			boolean a4Rechnug = false;
			if (ticket.getTotalAmount() >= configuredTotalAmount) {
				String message = "Bestellung gesamt ist zu hoch";
				if(StringUtils.isNotEmpty(POSConstants.zu_hoch))
					message =  POSConstants.zu_hoch;
				else
					message = "Bestellung gesamt ist zu hoch";
					
				ErrorMessageDialog dialog = new ErrorMessageDialog(message, true);
				dialog.pack();
				dialog.open();
				if (dialog.isCancelled())
					return;
			}

			if (ticket.getTicketItems().size() <= 0) {
				
				if(StringUtils.isNotEmpty(POSConstants.TICKET_IS_EMPTY_))
					POSMessageDialog.showError(POSConstants.TICKET_IS_EMPTY_);
				else
				    POSMessageDialog.showError("Bestellung ist leer");
			
				return;
			}

			PaymentType paymentType = PaymentType.CASH;
			Double cardValue = 0.00;
			boolean wholSale = false;
			if (paidAmount.compareTo(0.00) == 0 && !Application.getInstance().getRestaurant().isFastPayment()
					&& !rechnugPaid) {
				NewBalanceDialog dialog = new NewBalanceDialog(cash, ticket.getTotalAmountWithoutPfand()+ticket.getTipAmount(), this);
				dialog.setPreferredSize(new Dimension(800, 700));
				 
				if (TerminalConfig.isWholeSale()) {
					dialog.cbA4Rechnung.setSelected(true);
					wholSale = true;
				}

				dialog.pack();
				dialog.open();

				if (dialog.isCanceled()) {
					return;
				}
				a4Rechnug = dialog.cbA4Rechnung.isSelected();
				paymentType = dialog.getPaymentType();
				print = dialog.isPrint();
				paidAmount = dialog.getPaidAmount();
				cash = !dialog.isCard();
				if (paymentType.equals(PaymentType.BOTH)) {
					cardValue = dialog.getCardAmountValue();
					ticket.setCardAmount(dialog.getCardAmountValue());
					splitPayment = true;
				} else if(paymentType.equals(PaymentType.RECHNUNG)) {
					rechnugPaid = true;					
				}
			}

			Double totalAmnt = ticket.getTotalAmountWithoutPfand();
			if ((paidAmount - totalAmnt) < 0 && wholSale) {

			}

			if (paymentType.equals(PaymentType.BOTH)) {
				if (!TerminalConfig.isKhanaZvtEnable()) {
					if (!isExternalCardPayment(cardValue, splitPayment)) {
						return;
					}
				} else {
					if (!iskhanaCardPayment(cardValue, splitPayment)) {
						return;
					}
				}

			} else if (!cash) {
				if (paidAmount == 0.00) {
					paidAmount = ticket.getTotalAmount();
				}				
				System.out.println("Paid Amount "
						+ paidAmount+" Card Value "+cardValue);

				if (!TerminalConfig.isKhanaZvtEnable()) {
					if (!isExternalCardPayment(paidAmount, splitPayment)) {
						return;
					}
				} else {
					if (!iskhanaCardPayment(paidAmount, splitPayment)) {
						return;
					}
				}
			}
			
			/*
			 * If every item should update please remove below
			 */
						

			 if(isTseEnable&&print||isTseEnable&&!tseTier3) {
				 	if(posTseController.isRestartOrder()||ticket.getTseReceiptTxId()==null) {
					posTseController.setInstantParam(posTseController.tseStartOrder(Application.getInstance().getParam()));
					posTseController.setRestartOrder(false);
					 
					if(posTseController.isRestartReceipt()||ticket.getTseReceiptTxId()==null)
						posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
						
					posTseController.setInstantParam(posTseController.tseFinishOrder(ticketViewerTable.getAllTicketItems(), posTseController.getInstantParam()));

				}
//			}
//			/*
//			 * If every item should update please remove above
//			 */			
//
//			if (isTseEnable&&print) {
				//uncomment if every item should update
//				try {
//					posTseController.setInstantParam(posTseController.tseFinishOrder(ticket.getTicketItems(), posTseController.getInstantParam()));	
//				}catch(Exception ex) {
//
//				}
				TSEReceiptData receiptData = null;
				FiskalyKeyParameter param = Application.getInstance().getParam();
				try {
					System.out.println("Receipt Data Start " + ticket.getTseReceiptTxRevisionNr());
					if (ticket.getTseReceiptTxRevisionNr() != null)
						param.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
					else if (posTseController.getInstantParam()!=null&&posTseController.getInstantParam().getLatestRevisionReceipt() > 0)
						param.setLatestRevisionReceipt(posTseController.getInstantParam().getLatestRevisionReceipt());
					if (ticket.getTseReceiptTxId() != null)
						param.setTransactionIdReceipt(ticket.getTseReceiptTxId());
					else if (posTseController.getInstantParam()!=null&&posTseController.getInstantParam().getTransactionIdReceipt()!=null&&!posTseController.getInstantParam().getTransactionIdReceipt().isEmpty()) {
						ticket.setTseReceiptTxId(posTseController.getInstantParam().getTransactionIdReceipt());
						ticket.setTseClientId(posTseController.getInstantParam().getClientId());
						param.setTransactionIdReceipt(posTseController.getInstantParam().getTransactionIdReceipt());
					} else {
						posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
					}

					if (paymentType.compareTo(PaymentType.CASH) == 0)
						receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.CASH,
								FiskalyReceiptType.RECEIPT);
					else
						receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.NON_CASH,
								FiskalyReceiptType.RECEIPT);

					if(receiptData!=null)
						TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData);

					if (receiptData != null) {
						ticket.setTseReceiptDataId(receiptData.getId());
						ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
	
			setTseEnable(TSE);
			PrintType type = PrintType.REGULAR;
			if (TerminalConfig.isMultipleBon() && print) {
				int bon = 1;
				for (TicketItem item : ticket.getTicketItems()) {
					if (item.getBon() != null) {
						bon = item.getBon();
						break;
					}
				}
				BonDialog dialog = new BonDialog(bon);
				dialog.pack();
				dialog.open();
				System.out.println(dialog.isCanceled());

				if (dialog.isCancelled())
					return;
				type = dialog.getPrintType();
			}

			if (type == null)
				type = PrintType.REGULAR;

			Application application = Application.getInstance();
			User currentUser = Application.getCurrentUser();
			Terminal terminal = application.getTerminal();
			if (paymentType.equals(PaymentType.BOTH)) {
				ticket.setSplitPayment(true);
				CardDialog dialog = new CardDialog();
				dialog.pack();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				if (dialog.getCardPaymentType() != null) {
					CardType = dialog.getCardPaymentType().name();
					setPaymentCardType();
					ticket.setCashPayment(false);
				}
			} else if (rechnugPaid) {
				ticket.setRechnugPayemnt(true);
				ticket.setRechnugpaid(false);
			}else if(paymentType.equals(PaymentType.ONLINE)) {
				ticket.setOnlinePayment(true);
				ticket.setCardpaymenttype(null);
				ticket.setCashPayment(false);
			}else if(paymentType.equals(PaymentType.GUTSCHEIN)) {
				ticket.setOnlinePayment(false);
				ticket.setGutscheinPayment(true);
				ticket.setCardpaymenttype(null);
				ticket.setCashPayment(false);
			}else 	if (cash && !rechnugPaid) {
				ticket.setCashPayment(true);
				ticket.setOnlinePayment(false);
				ticket.setCardpaymenttype(null);
			} else if (TerminalConfig.isOnlyEC()) {
				CardType = "EC_CARD";
				setPaymentCardType();
				ticket.setCashPayment(false);
			} else {
				CardDialog dialog = new CardDialog();
				dialog.pack();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				if (dialog.getCardPaymentType() != null) {
					CardType = dialog.getCardPaymentType().name();
					setPaymentCardType();
					ticket.setCashPayment(false);
				}
			}
			
			if (border.getTitle().compareTo("BESTELLUNG") == 0 || border.getTitle().compareTo(POSConstants.NEW_ORDER) == 0 )				
				ticket.setType(TicketType.DINE_IN);
			else
				ticket.setType(TicketType.HOME_DELIVERY);

			ticket.setVoided(false);
			ticket.setDrawerResetted(false);
			ticket.setTerminal(terminal);
			ticket.setPaidAmount(ticket.getTotalAmount());
			ticket.setDueAmount(0.00);
			ticket.setPaid(true);
			ticket.setClosed(true);
			
			if (Application.getCurrentUser().getFirstName().compareTo("Master") != 0
					|| ticket.getClosingDate() == null&&ticket.getTicketid()==null) {
				ticket.setCreateDate(new Date());
				ticket.setCreationHour(new Date().getHours());
				ticket.setClosingDate(new Date());
				//if(userChange)
				ticket.setOwner(currentUser);
			}

			if (print) {
				final Restaurant restaurant = RestaurantDAO.getRestaurant();
				if (ticket.getTicketid() == null || ticket.getTicketid() == 0) {
					int restaurantTicketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
					ticket.setTicketid(restaurantTicketId);
					restaurant.setTicketid(++restaurantTicketId);
					RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				}
			}
			 
			if (Application.getCurrentUser().getFirstName().compareTo("Master") != 0) {
			try {
				
				List<TicketItem> ticketItemList = ticket.getDeletedItems();
				 
				if (ticketItemList != null) {
					JReportPrintService.printDeletedTicketToKitchen(ticket);
				}
			} catch(Exception ex) {
		   }
		  }

			/// inventur
			try {
				if (TerminalConfig.isInventur()) {
					if (ticket.isReOpened() == null && ticket.isRefunded() != null && !ticket.isRefunded()
							|| !ticket.isReOpened() && ticket.isRefunded() != null && !ticket.isRefunded())
						manageInventory(false);
					else if (ticket.isRefunded() != null && ticket.isRefunded()) {
						manageInventory(true);
					}
				}
			} catch (Exception ex) {

			}

			if(paidAmount!=0)
				ticket.setPaidAmount(paidAmount);
			else
				ticket.setPaidAmount(ticket.getTotalAmountWithoutPfand());

			OrderController.saveOrder(ticket);

			double balance = paidAmount - ticket.getTotalAmountWithoutPfand();
			if (print) {
				try {
					new PrintThread(ticket, official, type, balance, a4Rechnug);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			if(TerminalConfig.isAlwaysOpenCashdrawer()||cash||paymentType.equals(PaymentType.BOTH)) {
				try {		
					executeOpenCashDrawer();
				} catch(Exception ex) {

				}
			}

			if (cash) {				
				showBalance(paidAmount,print);
			}
			setPrinted();
			
			if (TerminalConfig.isTabVersion()) {
				//				openTicket();
				createNewTicket();
				updateCategoryView();
			} else
				RootView.getInstance().showView(SwitchboardView.VIEW_NAME);

			tfscanid.setText("");
			setRabatt(null);
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
			 
		} catch (Exception e) {
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
		}
	}


	private void handleGutschein() {
		List<TicketCouponAndDiscount> couponAndDiscounts = ticket.getCouponAndDiscounts();
		if (couponAndDiscounts != null && couponAndDiscounts.size() > 0) {
			for(TicketCouponAndDiscount ticketCouponAndDiscount:couponAndDiscounts) {
				if(ticketCouponAndDiscount != null&&ticketCouponAndDiscount.getType()==CouponAndDiscount.GUTSCHEIN) {
					Integer gutschein = ticketCouponAndDiscount.getGutschein();
					if (gutschein != null && gutschein > 0) {
						Gutschein dbGutschein = GutscheinDAO.getInstance().get(gutschein);
						if (!dbGutschein.isSplitted()) {
							dbGutschein.setClosed(true);
							GutscheinDAO.getInstance().saveOrUpdate(dbGutschein);
						}
					}
				}
			}
		}
	}
	
	public void doPay(boolean print, Double paidAmount, boolean cash, boolean official, boolean rechnugPaid) {// GEN-FIRST:event_btnOrderInfoActionPerformed
		try {
			PaymentType paymentType = cash ? PaymentType.CASH : PaymentType.CARD;
			if (ticket == null)
				return;
			
			int configuredTotalAmount = 100000;
			try {
				configuredTotalAmount = Integer.parseInt(TerminalConfig.getLimit());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			boolean splitPayment = false;
			boolean a4Rechnug = false;
			if (ticket.getTotalAmount() >= configuredTotalAmount) {
				String message = "Bestellung gesamt ist zu hoch";
				ErrorMessageDialog dialog = new ErrorMessageDialog(message, true);
				dialog.pack();
				dialog.open();
				if (dialog.isCancelled())
					return;
			}

			if (ticket.getTicketItems().size() <= 0) {
				POSMessageDialog.showError("Bestellung ist leer");
				return;
			}

			if (!gutscheinEingeloschst) {			
			Double cardValue = 0.00;
			boolean wholSale = false;
			if (paidAmount.compareTo(0.00) == 0 && !Application.getInstance().getRestaurant().isFastPayment()
					&& !rechnugPaid) {
				 
				NewBalanceDialog dialog1 = new NewBalanceDialog(cash, ticket.getTotalAmountWithoutPfand()+ticket.getTipAmount(), this);
				//dialog1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dialog1.setPreferredSize(new Dimension(800, 700));
				//dialog.setUndecorated(true);
				
				if (TerminalConfig.isWholeSale()) {
					dialog1.cbA4Rechnung.setSelected(true);
					wholSale = true;
				}

				dialog1.pack();
				dialog1.open();
				
				if (dialog1.isCanceled()) {
					return;
				}
				 
				a4Rechnug = dialog1.cbA4Rechnung.isSelected();
				paymentType = dialog1.getPaymentType();
				print = dialog1.isPrint();
				paidAmount = dialog1.getPaidAmount();
				cash = !dialog1.isCard();
				if (paymentType.equals(PaymentType.BOTH)) {
					cardValue = dialog1.getCardAmountValue();
					ticket.setCardAmount(dialog1.getCardAmountValue());
					splitPayment = true;
				} else if(paymentType.equals(PaymentType.RECHNUNG)) {
					rechnugPaid = true;					
				}
			}

			Double totalAmnt = ticket.getTotalAmountWithoutPfand();
			if ((paidAmount - totalAmnt) < 0 && wholSale) {

			}

			if (paymentType.equals(PaymentType.BOTH)) {
				if (!TerminalConfig.isKhanaZvtEnable()) {
					if (!isExternalCardPayment(cardValue, splitPayment)) {
						return;
					}
				} else {
					if (!iskhanaCardPayment(cardValue, splitPayment)) {
						return;
					}
				}

			} else if (!cash) {
				if (paidAmount == 0.00) {
					paidAmount = ticket.getTotalAmount()+ticket.getTipAmount();
				}				
				System.out.println("Paid Amount "
						+ paidAmount+" Card Value "+cardValue);

				if (!TerminalConfig.isKhanaZvtEnable()) {
					if (!isExternalCardPayment(paidAmount, splitPayment)) {
						return;
					}
				} else {
					if (!iskhanaCardPayment(paidAmount, splitPayment)) {
						return;
					}
				}
			}
		}
			/*
			 * If every item should update please remove below
			 */
			
			double gutsheinPayment=0.00;
			if (gutscheinEingeloschst) {
				paymentType = PaymentType.GUTSCHEIN;
				print = true;
				
				gutscheinEingeloschst = false;
				
				gutsheinPayment = ticket.getSubtotalAmount();
			}

			 if(isTseEnable&&print||isTseEnable&&!tseTier3) {
				 	if(posTseController.isRestartOrder()||ticket.getTseReceiptTxId()==null) {
					posTseController.setInstantParam(posTseController.tseStartOrder(Application.getInstance().getParam()));
					posTseController.setRestartOrder(false);
					 
					if(posTseController.isRestartReceipt()||ticket.getTseReceiptTxId()==null)
						posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
						
					posTseController.setInstantParam(posTseController.tseFinishOrder(ticketViewerTable.getAllTicketItems(), posTseController.getInstantParam()));

				}
//			}
//			/*
//			 * If every item should update please remove above
//			 */			
//
//			if (isTseEnable&&print) {
				//uncomment if every item should update
//				try {
//					posTseController.setInstantParam(posTseController.tseFinishOrder(ticket.getTicketItems(), posTseController.getInstantParam()));	
//				}catch(Exception ex) {
//
//				}
				TSEReceiptData receiptData = null;
				FiskalyKeyParameter param = Application.getInstance().getParam();
				try {
					System.out.println("Receipt Data Start " + ticket.getTseReceiptTxRevisionNr());
					if (ticket.getTseReceiptTxRevisionNr() != null)
						param.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
					else if (posTseController.getInstantParam()!=null&&posTseController.getInstantParam().getLatestRevisionReceipt() > 0)
						param.setLatestRevisionReceipt(posTseController.getInstantParam().getLatestRevisionReceipt());
					if (ticket.getTseReceiptTxId() != null)
						param.setTransactionIdReceipt(ticket.getTseReceiptTxId());
					else if (posTseController.getInstantParam()!=null&&posTseController.getInstantParam().getTransactionIdReceipt()!=null&&!posTseController.getInstantParam().getTransactionIdReceipt().isEmpty()) {
						ticket.setTseReceiptTxId(posTseController.getInstantParam().getTransactionIdReceipt());
						ticket.setTseClientId(posTseController.getInstantParam().getClientId());
						param.setTransactionIdReceipt(posTseController.getInstantParam().getTransactionIdReceipt());
					}else {
						posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
					}

					if (paymentType.compareTo(PaymentType.CASH) == 0)
						receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.CASH,
								FiskalyReceiptType.RECEIPT);
					else
						receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.NON_CASH,
								FiskalyReceiptType.RECEIPT);

					if(receiptData!=null)
						TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData);

					if (receiptData != null) {
						ticket.setTseReceiptDataId(receiptData.getId());
						ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
					}
					
					boolean paymentHandled = handlePayment(paymentType, official);
					if (!paymentHandled) {
						return;
					}
					
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
	
			setTseEnable(TSE);
			PrintType type = PrintType.REGULAR;
			if (TerminalConfig.isMultipleBon() && print) {
				int bon = 1;
				for (TicketItem item : ticket.getTicketItems()) {
					if (item.getBon() != null) {
						bon = item.getBon();
						break;
					}
				}
				BonDialog dialog = new BonDialog(bon);
				dialog.pack();
				dialog.open();
				System.out.println(dialog.isCanceled());

				if (dialog.isCancelled())
					return;
				type = dialog.getPrintType();
			}

			if (type == null)
				type = PrintType.REGULAR;

			System.out.println("");
			
			Application application = Application.getInstance();
			User currentUser = Application.getCurrentUser();
			Terminal terminal = application.getTerminal();
			if (paymentType.equals(PaymentType.BOTH)) {
				ticket.setSplitPayment(true);
				CardDialog dialog = new CardDialog();
				dialog.pack();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				if (dialog.getCardPaymentType() != null) {
					CardType = dialog.getCardPaymentType().name();
					setPaymentCardType();
					ticket.setCashPayment(false);
				}
			} else if (rechnugPaid) {
				ticket.setRechnugPayemnt(true);
				ticket.setRechnugpaid(false);
			} else if(paymentType.equals(PaymentType.ONLINE)) {
				ticket.setOnlinePayment(true);
				ticket.setCardpaymenttype(null);
				ticket.setGutscheinPayment(false);
				ticket.setCashPayment(false);
			} else if(paymentType.equals(PaymentType.GUTSCHEIN) ) {				
				ticket.setOnlinePayment(false);
				ticket.setCardpaymenttype(null);
				ticket.setGutscheinPayment(true);
				ticket.setCashPayment(false);
			} else 	if (cash && !rechnugPaid) {
				ticket.setCashPayment(true);
				ticket.setOnlinePayment(false);
				ticket.setGutscheinPayment(false);
				ticket.setCardpaymenttype(null);
			} else if (TerminalConfig.isOnlyEC()) {
				CardType = "EC_CARD";
				setPaymentCardType();
				ticket.setCashPayment(false);
			
			} else {
				CardDialog dialog = new CardDialog();
				dialog.pack();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				if (dialog.getCardPaymentType() != null) {
					CardType = dialog.getCardPaymentType().name();
					setPaymentCardType();
					ticket.setCashPayment(false);
				}
			}
			
			if (border.getTitle().compareTo("BESTELLUNG") == 0)
				ticket.setType(TicketType.DINE_IN);
			else
				ticket.setType(TicketType.HOME_DELIVERY);

			//if (lblDisplay.getText()!=null || lblDisplay.getText()!="") {
			
			System.out.println("subTotal: " + ticket.getSubtotalAmount());
			
			ticket.setVoided(false);
			ticket.setDrawerResetted(false);
			ticket.setTerminal(terminal);
			if(gutsheinPayment>0) {
				ticket.setTotalAmount(gutsheinPayment);
			}
			 
			ticket.setPaidAmount(ticket.getTotalAmount());
			 
			ticket.setDueAmount(0.00);
			ticket.setPaid(true);
			ticket.setClosed(true);
		//}
			
			if (Application.getCurrentUser().getFirstName().compareTo("Master") != 0
					|| ticket.getClosingDate() == null&&ticket.getTicketid()==null) {
				ticket.setCreateDate(new Date());
				ticket.setCreationHour(new Date().getHours());
				ticket.setClosingDate(new Date());
				//				if(userChange)
				ticket.setOwner(currentUser);
			}

			if (print) {
				final Restaurant restaurant = RestaurantDAO.getRestaurant();
				if (ticket.getTicketid() == null || ticket.getTicketid() == 0) {
					int restaurantTicketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
					ticket.setTicketid(restaurantTicketId);
					restaurant.setTicketid(++restaurantTicketId);
					RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				}
			}
			
			if (Application.getCurrentUser().getFirstName().compareTo("Master") != 0) {
				try {
					List<TicketItem> ticketItemList = ticket.getDeletedItems();
					 
					if (ticketItemList != null) {
						JReportPrintService.printDeletedTicketToKitchen(ticket);
					}
				} catch(Exception ex) {
	
				}
			}

			/// inventur
			try {
				if (TerminalConfig.isInventur()) {
					if (ticket.isReOpened() == null && ticket.isRefunded() != null && !ticket.isRefunded()
							|| !ticket.isReOpened() && ticket.isRefunded() != null && !ticket.isRefunded())
						manageInventory(false);
					else if (ticket.isRefunded() != null && ticket.isRefunded()) {
						manageInventory(true);
					}
				}
			} catch (Exception ex) {

			}

			if(paidAmount!=0)
				ticket.setPaidAmount(paidAmount);
			else
				ticket.setPaidAmount(ticket.getTotalAmountWithoutPfand());

			OrderController.saveOrder(ticket);

			double balance = paidAmount - ticket.getTotalAmountWithoutPfand();
			if (print) {
				try {
					new PrintThread(ticket, official, type, balance, a4Rechnug);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			if(TerminalConfig.isAlwaysOpenCashdrawer()||cash||paymentType.equals(PaymentType.BOTH)) {
				try {		
					executeOpenCashDrawer();
				} catch(Exception ex) {

				}
			}

			if (cash) {				
				showBalance(paidAmount,print);
			}
			setPrinted();

			if (TerminalConfig.isTabVersion()) {
				//				openTicket();
				createNewTicket();
				updateCategoryView();
			} else
				RootView.getInstance().showView(SwitchboardView.VIEW_NAME);

			tfscanid.setText("");
			setRabatt(null);
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
			
		} catch (Exception e) {
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
		}
	}

	public void setPrinted() {
		List<TicketItem> itemList = ticket.getTicketItems();
		for(TicketItem item:itemList) {
			item.setPrintedToKitchen(true);			
		}
		OrderController.saveOrder(ticket);
	}

	protected boolean handlePayment(PaymentType paymentType, boolean official) {
		 
		if (ticket.getTipAmount() == null) {
			ticket.setTipAmount(0.00);
		}
		
		ticket.setPaid(true);
		if (paymentType.ordinal() == PaymentType.CASH.ordinal()) {
			ticket.setCashPayment(true);
			ticket.setOnlinePayment(false);
			ticket.setGutscheinPayment(false);
			ticket.setSplitPayment(false);
		} else if (paymentType.ordinal() == PaymentType.ONLINE_BAR.ordinal()) {
			ticket.setCashPayment(true);
			ticket.setOnlinePayment(false);			
			ticket.setGutscheinPayment(false);
			ticket.setSplitPayment(false);
			
		} else if (paymentType.ordinal() == PaymentType.ONLINE.ordinal()) {
			ticket.setOnlinePayment(true);
			ticket.setCashPayment(false);
			ticket.setGutscheinPayment(false);
			ticket.setSplitPayment(false);
		
		} else if (paymentType.ordinal() == PaymentType.GUTSCHEIN.ordinal()) {
			ticket.setOnlinePayment(false);
			ticket.setCashPayment(false);
			ticket.setGutscheinPayment(true);
			ticket.setSplitPayment(false);
		} else if (paymentType.ordinal() == PaymentType.BOTH.ordinal()) {
			ticket.setOnlinePayment(false);
			ticket.setCashPayment(false);
			ticket.setGutscheinPayment(false);
			ticket.setSplitPayment(true);
			setPaymentCardType();
		} else {
			// cardSet
			setPaymentCardType();
			// till here
		/*	String text = lblDisplay.getText();
			if (text.length() > 1) {
				Double paidAmount = Double.parseDouble(text) / 100.00;
				if (paidAmount >= ticket.getTotalAmount()) {
					Double tipAmount = paidAmount - ticket.getTotalAmount();
					ticket.setTipAmount(tipAmount);
				}
			}*/
			ticket.setCashPayment(false);
			ticket.setOnlinePayment(false);		
			ticket.setGutscheinPayment(false);
		}
		ticket.setClosed(true);
		return true;
	}
	
	// cardType
	String CardType = "cardType";

	protected void setPaymentCardType() {
		if (CardType.equals("girocard") || CardType.equals("EC_CARD")) {
			ticket.setCardpaymenttype(1);
		} else if (CardType.equals("MASTERCARD") || CardType.equals("MASTER") || CardType.equals("MasterCard")) {
			ticket.setCardpaymenttype(4);
		} else if (CardType.contains("Visa") || CardType.contains("VISA")) {
			ticket.setCardpaymenttype(6);
		} else if (CardType.equals("AMEX") || CardType.equals("AMERICAN_EXPRESS")) {
			ticket.setCardpaymenttype(0);
		} else if (CardType.equals("Maestro")) {
			ticket.setCardpaymenttype(1);
		} else if (CardType.equals("GUTSCHEIN")) {
			ticket.setCardpaymenttype(2);
		} else if (CardType.equals("KHANA")) {
			ticket.setCardpaymenttype(3);
		} else if (CardType.equals("PAYPAL")) {
			ticket.setCardpaymenttype(5);
		} else if (CardType.equals("GUTSCHEIN2")) {
			ticket.setCardpaymenttype(8);
		} else {
			ticket.setCardpaymenttype(7);
		}
	}
	// till here

	private boolean isExternalCardPayment(double paidAmount, boolean splitPayment) {
		if (StringUtils.isBlank(TerminalConfig.getZvtCardPayment())) {
			return true;
		}

		// added
		if (TerminalConfig.isCardTerminalDisable()) {
			ZVTCardConfirmationDialog dialog = new ZVTCardConfirmationDialog(ticket.getTotalAmount(),
					ticket.getTotalAmount());
			dialog.setPreferredSize(new Dimension(250, 250));
			dialog.pack();
			dialog.open();
			if (dialog.isCanceled()) {
				return false;
			}
			return true;
		}
		// till here

		if (paidAmount < ticket.getTotalAmount() && !splitPayment) {
			POSMessageDialog.showError("Eingabe ist kleiner als die Gesamt");
			return false;
		}

		/*
		 * if (StringUtils.isNotBlank(TerminalConfig.getZvtCardPayment())) { Double
		 * totalAmount =
		 * NumberUtil.roundToTwoDigit(NumberUtil.roundToTwoDigit(paidAmount) * 100.00);
		 * int totalAmountinCents = totalAmount.intValue(); Runtime runtime =
		 * Runtime.getRuntime(); try { String executingCommand =
		 * TerminalConfig.getZvtCardPayment() + totalAmountinCents;
		 * System.out.println("Executing command :" + executingCommand);
		 * runtime.exec(executingCommand); } catch (IOException e) {
		 * System.out.println(e); } }
		 */
		try {
			Double totalAmount = NumberUtil.roundToTwoDigit(NumberUtil.roundToTwoDigit(paidAmount) * 100.00);
			int totalAmountinCents = totalAmount.intValue();

			System.out.println("Amount in Cents "+totalAmountinCents+" amnt "+totalAmount);

			EasyPayment.zvtExecute(totalAmountinCents);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String check = null;
		try {
			check = EasyPayment.checkService();
			CardType = EasyPayment.cardType();
			System.out.println("Card Type:" + CardType);

		} catch (Exception ex) {
			System.out.println(ex);
		}

		if (check == "SP") {
			return true;
		} else {
			ZVTNewCardTryDialog dialog = new ZVTNewCardTryDialog(ticket.getTotalAmount(), paidAmount);
			dialog.setPreferredSize(new Dimension(250, 210));
			dialog.pack();
			dialog.open();
			if (dialog.isCanceled()) {
				return false;
			}
		}
		return false;
	}

	private boolean iskhanaCardPayment(double paidAmount, boolean splitPayment) {
		// paidAmount = ticket.getTotalAmount();
		if (!isReachable(TerminalConfig.getKhanaCardIp())) {
			POSMessageDialog.showError("FEHLER: Karten Terminal nichts erreichbar!");
			return false;
		}

		if (ticket.getTotalAmount() == 0) {
			POSMessageDialog.showError("Zahlung ist 0,00");
			return true;
		}
		if (paidAmount < ticket.getTotalAmount() && !splitPayment) {
			POSMessageDialog.showError("Eingabe ist kleiner als die Gesamt");
			return false;
		}

		String terminalIp = TerminalConfig.getKhanaCardIp();
		String terminalPort = TerminalConfig.getKhanaCardPort();

		if (!TerminalConfig.isKhanaZvtEnable() || StringUtils.isBlank(terminalIp)
				|| StringUtils.isBlank(terminalPort)) {

			ZVTCardConfirmationDialog dialog = new ZVTCardConfirmationDialog(ticket.getTotalAmount(),
					ticket.getTotalAmount());
			dialog.setTitle("Bitte Manual einprufen..");
			dialog.setPreferredSize(new Dimension(250, 250));
			dialog.pack();
			dialog.open();

			if (dialog.isCanceled()) {
				return false;
			}
			return true;
		}

		int totalAmountinCents = 0;
		try {
			Double totalAmount = NumberUtil.roundToTwoDigit(NumberUtil.roundToTwoDigit(paidAmount) * 100.00);
			totalAmountinCents = totalAmount.intValue();
			System.out.println("Amount in Cents "+totalAmountinCents+" amnt "+totalAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String check = null;
		try {
			List<String> commands = new ArrayList<String>();
			commands.add("ZVT.exe");
			commands.add(terminalIp);
			commands.add(terminalPort);
			commands.add(Integer.toString(totalAmountinCents));
			String s = null;

			ProcessBuilder pb = new ProcessBuilder(commands);
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);

				if (s.contains("approved") || s.contains("erfolgt")) {
					check = "approved";
					break;
				}
				if (s.contains("abgebrochen")) {
					check = "abgebrochen";
					break;
				}
			}

			while ((s = stdError.readLine()) != null) {
				return false;
			}

		} catch (Exception ex) {
			System.out.println("Unable to Execute KhanaZVT ...");

			ex.printStackTrace();
			return false;
		}

		if (check != null && check == "approved") {
			POSMessageDialog.showMessage("Zahlung Erfolg..");
			return true;
		} else {
			ZVTNewCardTryDialog dialog = new ZVTNewCardTryDialog(ticket.getTotalAmount(), paidAmount);
			dialog.setPreferredSize(new Dimension(300, 200));
			dialog.pack();
			dialog.open();
			if (dialog.isCanceled()) {
				return false;
			}
		}
		return false;
		// till Here
	}

	public void doUpdateItem() {// GEN-FIRST:event_doInsertMisc
		Object selectedObject = ticketViewerTable.getSelected();
        System.out.println("doUpdateItem>");
		TicketItem selectedItem = null;
		if (selectedObject instanceof TicketItem) {
			selectedItem = (TicketItem) selectedObject;
			MiscTicketItemmDialog dialog = new MiscTicketItemmDialog(Application.getPosWindow(), true,
					ticket.getType().name(), selectedItem, false, "");
			dialog.setSize(new Dimension(700, 400));
			dialog.open();
			if (!dialog.isCanceled()) {				
				if(isTseEnable) {
					posTseController.addQueType2(dialog.getName(), dialog.getUnitPrice(), dialog.getCount(), ticket);
					posTseController.cancelItem(selectedItem, true, POSConstants.ARTICKEL_UPDATE);
				}
				if(dialog.getUnitPrice()<0)
					ticket.setRefunded(true);
				ticketViewerTable.updateTicketItem(dialog.getName(), dialog.getCount(), dialog.getUnitPrice());
			}
		}
		updateView();
		focusToScanid();
	}// GEN

	private void handleExternalCardPayment() {
		Double totalAmount = (ticket.getTotalAmount() * 100.00);
		int totalAmountinCents = totalAmount.intValue();
		if (StringUtils.isNotBlank(TerminalConfig.getCardMachineCom())) {
			Runtime runtime = Runtime.getRuntime();
			try {
				System.out.println("totalAmountinCents: "+ totalAmountinCents);
				String executingCommand = "ZVT.exe " + totalAmountinCents + " " + TerminalConfig.getCardMachineCom();
				System.out.println("Executing command :" + executingCommand);
				runtime.exec(executingCommand);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
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

		executeOpenCashDrawer();
	}

	public void manageInventory(boolean retour) {
		List<TicketItem> itemList = null;
		itemList = ticket.getTicketItems();
		for (TicketItem item : itemList) {
			if (item.getBarcode() == null)
				continue;
			if (item.isPrintedToKitchen())
				continue;
			List<MenuItem> menuItemList = MenuItemDAO.getInstance().findByBarcode(item.getBarcode());
			if (menuItemList != null) {
				if (!item.isPfand()) {
					for (MenuItem menuItem : menuItemList) {
						if (menuItem.getInstock() != null) {
							menuItem.setInstock(menuItem.getInstock() - item.getItemCount());
							if (menuItem.getSold() != null)
								menuItem.setSold(menuItem.getSold() + item.getItemCount());
							else
								menuItem.setSold(item.getItemCount());
							try {
								MenuItemDAO.getInstance().saveOrUpdate(menuItem);
							} catch (Exception exx) {
								exx.printStackTrace();
							}
						} else {
							menuItem.setSold(item.getItemCount());
							menuItem.setInstock(0 - item.getItemCount());
							try {
								MenuItemDAO.getInstance().saveOrUpdate(menuItem);
							} catch (Exception exx) {
								exx.printStackTrace();
							}
						}

					}

				} else {
					for (MenuItem menuItem : menuItemList) {
						if (menuItem.getInstock() != null) {
							menuItem.setInstock(menuItem.getInstock() + item.getItemCount());
							if (menuItem.getSold() != null)
								menuItem.setSold(menuItem.getSold() - item.getItemCount());
							else
								menuItem.setSold(item.getItemCount());
							try {
								MenuItemDAO.getInstance().saveOrUpdate(menuItem);
							} catch (Exception exx) {
								exx.printStackTrace();
							}
						} else {
							menuItem.setSold(0 - item.getItemCount());
							menuItem.setInstock(item.getItemCount());
							try {
								MenuItemDAO.getInstance().saveOrUpdate(menuItem);
							} catch (Exception exx) {
								exx.printStackTrace();
							}
						}

					}
				}

			}
		}

	}

	public void focusToScanid() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tfscanid.requestFocus();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if (actionCommand.equals(POSConstants.CLEAR)) {
			doClearAll();
		} else if (actionCommand.equals(POSConstants.ADD)
				|| ((restaurant.getAdditem() != null) && (actionCommand.equals(restaurant.getAdditem())))) {
			String value = tfscanid.getText();
			if (value == null || value.length() <= 0||value.contains("-"))
				return;	
			if(TSE&&certEnable&&value.equals(API_KEY_D)) {
				this.isTseEnable = false;
				tfscanid.setText("");
				return;
			}else if(TSE&&certEnable&&value.equals(API_KEY_E)) {
				this.isTseEnable = TSE;
				tfscanid.setText("");
				return;
			}
			
			searchItem();
			tfscanid.setText(null);
		} else {
			doInsertNumber(actionCommand);
		}
	}
	boolean playContinue = false;
	public void searchItem() {
		String temp = tfscanid.getText();
		int delimIndex = temp.indexOf('*');
		String itemId = "";
		int itemMultiple = 1;
		if (delimIndex != -1) {
			try {

				itemMultiple = Integer.parseInt(temp.substring(0, delimIndex));
				if (itemMultiple > 100) {
					int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Wollen sie geht weiter???",
							"Anzahl ist hoch " + itemMultiple + " mal", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (option != JOptionPane.YES_OPTION) {
						return;
					}
				}

				itemId = temp.substring(delimIndex + 1, temp.length());
			} catch (Exception e) {
				POSMessageDialog.showError(POSConstants.PROVIDE_CORRECT_FORMAT);
				return;
			}
		} else {
			itemId = tfscanid.getText();
		}
		tfscanid.setText("");
		if (itemId.length() == 0) {
			return;
		}
		List<MenuItem> items = new ArrayList<>();
		try {
			if (TerminalConfig.isPriceCategory() && getPriceCategory() > 1
					|| TerminalConfig.isPriceCategoryKunden() && getPriceCategory() > 1)
				items = MenuItemDAO.getInstance().findByIdPriceCategory(itemId, getPriceCategory());
			else
				items = MenuItemDAO.getInstance().findById(false, itemId);
		} catch (Exception ex) {

		}
        
		boolean barcode = false;
		boolean checked = false;
		if (items.isEmpty() && TerminalConfig.isBothInput()) {
			try {
				itemId = itemId.trim();

				if (TerminalConfig.isPriceCategory() && getPriceCategory() > 1
						|| TerminalConfig.isPriceCategoryKunden() && getPriceCategory() > 1) {
					System.out.println("barcodeScann Here if");
					items = MenuItemDAO.getInstance().findByBarcodePrice(itemId, getPriceCategory());					
					if (items.isEmpty())
						items = MenuItemDAO.getInstance().findByBarcodePrice(ReportUtil.removeLeadingZeroes(itemId), getPriceCategory());
					if (items.isEmpty())
						items = MenuItemDAO.getInstance().findByBarcode1Price(itemId, getPriceCategory());
					if (items.isEmpty())
						items = MenuItemDAO.getInstance().findByBarcode2Price(itemId, getPriceCategory());
					
				} else {
					System.out.println("barcodeScann Here else");
					items = MenuItemDAO.getInstance().findByBarcodePrice(itemId, 1);
					System.out.println("items size:"+items.size());
					
					if (items.isEmpty()) {						
						items = MenuItemDAO.getInstance().findByBarcodePrice(ReportUtil.removeLeadingZeroes(itemId), 1);
						
					}
					
					if (items.isEmpty())
						items = MenuItemDAO.getInstance().findByBarcode1Price(itemId, 1);
					if (items.isEmpty())
						items = MenuItemDAO.getInstance().findByBarcode2Price(itemId, 1);					
				}
				
				if (items.size() > 1) {
					MenuItem menuItem = null;
					SearchDialog dialog = new SearchDialog(items, ticket.getType());
					dialog.setTitle("Khana Kassesysteme");
					dialog.pack();
					dialog.open();
					if (dialog.isCanceled()) {
						return;
					}
					menuItem= dialog.getMenuItem();
					itemSelectionListener.itemSelected(menuItem, ticket);
					return;
					 
					 
				}

				barcode = true;
				checked = true;
			} catch (Exception ex) {
				barcode = false;
				checked = true;
			}
		}

		// if(items.isEmpty()&&TerminalConfig.isPriceCategory()&&getPriceCategory()>1||items.isEmpty()&&TerminalConfig.isPriceCategoryKunden()&&getPriceCategory()>1)
		// {
		// items = MenuItemDAO.getInstance().findByBarcodePrice(itemId, 1);
		// barcode = true;
		// if(items.isEmpty())
		// items = MenuItemDAO.getInstance().findById(false, itemId);
		// }


		if(items==null&&barcode&&itemId.length()>6&&TerminalConfig.isItemAdd()||items!=null&&items.size()==0&&barcode&&itemId.length()>6&&TerminalConfig.isItemAdd()) {
			try {
				System.out.println("isPlaySound");
				if(TerminalConfig.isPlaySound())
					MediaUtil.getInstance().playNotification(1);
			}catch(Exception ex) {

			}finally{

			}
						
			int option = JOptionPane.showOptionDialog(BackOfficeWindow.getInstance(),
					"Artikel nicht gefunden!!! \nMoechten Sie Artikel mit dieses Barcode " + itemId + " erstellen??", POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				MenuItem menuItem = new MenuItem();
				menuItem.setBarcode(ReportUtil.removeLeadingZeroes(itemId));
				MenuItemForm editor;
				try {
					editor = new MenuItemForm(menuItem, true);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.setPreferredSize(new Dimension(800,740));
					dialog.open();
					if (dialog.isCanceled())
						return;
					items.add(editor.getMenuItem());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();


				}
			}else {
				return;
			}

//			if (TerminalConfig.isPriceCategory() && getPriceCategory() > 1
//					|| TerminalConfig.isPriceCategoryKunden() && getPriceCategory() > 1) {
//				items = MenuItemDAO.getInstance().findByBarcodePrice(itemId, getPriceCategory());
//				if (items.isEmpty())
//					items = MenuItemDAO.getInstance().findByBarcode1Price(itemId, getPriceCategory());
//				if (items.isEmpty())
//					items = MenuItemDAO.getInstance().findByBarcode2Price(itemId, getPriceCategory());
//			} else {
//				items = MenuItemDAO.getInstance().findByBarcodePrice(itemId, 1);
//				if (items.isEmpty())
//					items = MenuItemDAO.getInstance().findByBarcode1Price(itemId, 1);
//				if (items.isEmpty())
//					items = MenuItemDAO.getInstance().findByBarcode2Price(itemId, 1);
//			}			
		}


		if (items == null||items!=null&&items.size()==0) {
			try {if(TerminalConfig.isPlaySound()) {
				playContinue = true;
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {while(playContinue)				
						MediaUtil.getInstance().playNotification(1);		
					}
				});
				t.start();
				ErrorMessageDialog dialog;
				if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
					dialog = new ErrorMessageDialog(POSConstants.nicht_gefunden);
				else
				   dialog = new ErrorMessageDialog("Artikel nicht gefunden");
				
				dialog.pack();
				dialog.open();
				if (!dialog.isCanceled()) {
					playContinue = false;					
				}
				return;
			} else {
				if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
					POSMessageDialog.showError(POSConstants.nicht_gefunden);
				else
				    POSMessageDialog.showError("Artikel nicht gefunden");
				
				return;
			}

			}catch(Exception ex) {

			}finally{

			}
		}

		MenuItem menuItem = null;
		int found = 0;
		int count = 0;

		for (int i = 0; i < items.size(); i++) {
			menuItem = items.get(i);
			String type;
			if (menuItem.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0)
				type = "BESTELLUNG";
			else
				type = "MITNAHME";

			if (border.getTitle().compareTo(type) == 0) {
				count++;
			} else {
				continue;
			}
		}
		if (count > 1) {
			SearchDialog dialog = new SearchDialog(items, ticket.getType());
			dialog.setTitle("Khana Kassesysteme");
			dialog.pack();
			dialog.open();
			if (dialog.isCanceled()) {
				return;
			}

			menuItem = dialog.getMenuItem();
			if (menuItem == null)
				return;
		} else {
			Restaurant restaurant = RestaurantDAO.getRestaurant();
			for (int i = 0; i < items.size(); i++) {
				menuItem = items.get(i);
				String type;
				if (menuItem.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0)
					type = "BESTELLUNG";
				else
					type = "MITNAHME";

				if (restaurant.getDupdinein() != null && restaurant.getDupdinein()) {

					found = 1;
					break;
				} else {
					if (border.getTitle().compareTo(type) == 0) {
						found = 1;
						break;
					} else {
						continue;
					}
				}
			}
			if (found == 0 && !barcode && menuItem == null) {
				if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
					POSMessageDialog.showError(POSConstants.nicht_gefunden);
				else
				    POSMessageDialog.showError("Artikel nicht gefunden");
			
				return;
			}

		}

		int countt = 1;
		if (TerminalConfig.isTakeItemCount()) {
			try {
				countt = Integer.parseInt(getItemZahl());
				if (countt > 10) {
					ErrorMessageDialog dialog = new ErrorMessageDialog("Art. Anzahl " + count + " ist zu hoch", true);
					dialog.pack();
					dialog.open();
					if (dialog.isCancelled())
						return;
				}
			} catch (Exception ex) {

			}
		}

		try {
			for (int i = 0; i < itemMultiple; i++) {
				if(menuItem.isPfand())
					itemSelectionListener.pfandSelected(menuItem, ticket, true);
				else {
					itemSelectionListener.itemSelected(menuItem, ticket);					
					if (menuItem.getSubitemid() != null) {
						List<MenuItem> itemlist = MenuItemDAO.getInstance().findById(true, menuItem.getSubitemid());
						for (Iterator<MenuItem> itr = itemlist.iterator(); itr.hasNext();) {
							MenuItem item = itr.next();
							if (item == null)
								continue;
							itemSelectionListener.pfandSelected(item, ticket, false);
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
				POSMessageDialog.showError(POSConstants.nicht_gefunden);
			else
			    POSMessageDialog.showError("Artikel nicht gefunden");
		
			return;
		}
	}

	private void doClearAll() {

		tfscanid.setText(null);
	}

	private void doInsertNumber(String number) {
		String s = tfscanid.getText();

		s = s + number;
		if (number.compareTo("CLEAR") != 0)
			tfscanid.setText(s);
	}

	protected void doAddCookingInstruction() {
		try {
			Object object = ticketViewerTable.getSelected();
			if (!(object instanceof TicketItem)) {
				POSMessageDialog.showError("Bitte wählen Sie einen Artikel");
				return;
			}

			TicketItem ticketItem = (TicketItem) object;

			if (ticketItem.isPrintedToKitchen()) {
				POSMessageDialog.showError("Bestellung bereits an Kueche gesendet");
				return;
			}

			List<CookingInstruction> list = CookingInstructionDAO.getInstance().findAll();
			CookingInstructionSelectionView cookingInstructionSelectionView = new CookingInstructionSelectionView();
			BeanEditorDialog dialog = new BeanEditorDialog(cookingInstructionSelectionView, Application.getPosWindow(),
					true);
			dialog.setBean(list);
			dialog.setSize(800, 600);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			if (dialog.isCanceled()) {
				return;
			}

			List<TicketItemCookingInstruction> instructions = cookingInstructionSelectionView
					.getTicketItemCookingInstructions();
			ticketItem.addCookingInstructions(instructions);
			ticketViewerTable.updateView();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		}
	}

	public void doFinishOrder_Update() {
		ticket.setTseReceiptDataId(0);
		ticket.setTseReceiptTxRevisionNr("null");
	}
	
	public synchronized void doFinishOrder_() {
		try {
			List<TicketItem> itemList = ticket.getTicketItems();
			Collections.sort(itemList, new TicketItem.ItemComparator());
			if (ticket.getMinOrder() != null) {
				if (!isDeliveryCostAdded(ticket))
					addDeliveryCost(ticket);
			}
			    TSEReceiptData receiptData = null;
		    	TseDataDAO tseDataDAO = new  TseDataDAO();
		    	
		    	receiptData = tseDataDAO.findById();
			    if (receiptData != null) {
			    	
					ticket.setTseReceiptDataId(receiptData.getId());
					ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
				}
			   
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}
	
	public synchronized void doFinishOrder_Saldo() {
		try {
			 
			 System.out.println("doFinishOrder_Saldo: tewst");
			 if (ticket.getMinOrder() != null) {
				if (!isDeliveryCostAdded(ticket))
					addDeliveryCost(ticket);
			}
			
		updateModel(null);
		OrderController.saveOrder(ticket);
		
		} catch (StaleObjectStateException e) {
			POSMessageDialog.showError(
					"Speichern fehlgeschlagen, das Ticket wurde bereits von einer anderen Person bearbeitet");
			return;
		} catch (PosException x) {
			POSMessageDialog.showError(x.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}
	
	public synchronized void doFinishOrder() {// GEN-FIRST:event_doFinishOrder
		try {
			List<TicketItem> itemList = ticket.getTicketItems();
			Collections.sort(itemList, new TicketItem.ItemComparator());
			if (ticket.getMinOrder() != null) {
				if (!isDeliveryCostAdded(ticket))
					addDeliveryCost(ticket);
			}
			
			/*
			 * If every item should update please remove below
			 */

//			if(isTseEnable&&close) {
//				if(posTseController.isRestartOrder()) {
//					posTseController.setInstantParam(posTseController.tseStartOrder(Application.getInstance().getParam()));
//					posTseController.setRestartOrder(false);
//					/*
//					 * for sofort storno please remove this
//					 */
//					if(posTseController.isRestartReceipt())
//						posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
//				}
//				posTseController.setInstantParam(posTseController.tseFinishOrder(ticketViewerTable.getAllTicketItems(), posTseController.getInstantParam()));
//			}
			/*
			 * If every item should update please remove above
			 */		
			setPrinted();
			updateModel(null);
			OrderController.saveOrder(ticket);

		} catch (StaleObjectStateException e) {
			POSMessageDialog.showError(
					"Speichern fehlgeschlagen, das Ticket wurde bereits von einer anderen Person bearbeitet");
			return;
		} catch (PosException x) {
			POSMessageDialog.showError(x.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_doFinishOrder

	public void doCancelOrder() {// GEN-FIRST:event_doCancelOrder
		User user = Application.getCurrentUser();
		UserType userType = user.getType();
		int flag = 0;
		if (userType != null) {
			Set<UserPermission> permissions = userType.getPermissions();
			for (UserPermission permission : permissions) {
				if (permission.getName().compareTo(UserPermission.PERFORM_ADMINISTRATIVE_TASK.getName()) == 0) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						flag = 1;
					}
				}
			}
		}
		if (!ticket.isReOpened()) {
			ticket.setPfand(0.00);
			ticket.setPfand2(0.00);
			ticket.setPfand3(0.00);
			ticket.setDiscountAmount(0.00);
			ticket.setCouponAndDiscounts(null);
			ticket.removeCustomer();
			if (!TerminalConfig.isWholeSale())
				setPriceCategory(1);
			ticket.setRefunded(false);
			updateAllView();
			//			if(isTseEnable) {
			//				boolean cancel = false;
			//				for (TicketItem item : ticket.getTicketItems()) {
			//					if(item.isPrintedToKitchen())
			//						posTseController.cancelItem(item, true, StringUtils.EMPTY);
			//					else
			//						posTseController.cancelItem(item, true, POSConstants.ARTICKEL_INSTANT_CANCEL);
			//					cancel = true;
			//				}
			//
			//				if(cancel) {
			//					posTseController.tseFinishOrder(new ArrayList<TicketItem>(), posTseController.getInstantParam());
			//					posTseController.tseInstantCancelOrder(posTseController.getInstantParam());
			//				}
			//			}
		}
		setTseEnable(TSE);

		try {
			if (TerminalConfig.isKundenScreen())
				closeCustomerDisplay();
		} catch (Exception ex) {

		}
		if (flag == 1) {
			RootView.getInstance().getSwitchboadView().updateButtons();
			RootView.getInstance().getSwitchboadView().updateView(POSConstants.ALL_TICKETS);
			RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
			return;
		}
		Application.getInstance().logout();
	}// GEN-LAST:event_doCancelOrder

	private synchronized void updateModel(TSEReceiptData receiptData) {
		if (ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0) {
			throw new PosException(com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
		}
		if (receiptData != null) {
			ticket.setTseReceiptDataId(receiptData.getId());
			ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
		}
		ticket.calculatePrice();
	}

	public boolean executeOpenCashDrawer() {
		boolean success = true;

		if (StringUtils.isNotEmpty(TerminalConfig.getCashDrawerFile())) {
			openCashDrawerFile();
		}else if(TerminalConfig.isCashDrawerPrint()) {
			cashdrawerOpen();
		} else if (TerminalConfig.isCashDrawer()) {
			openCashDrawer();
		}else {
			success = false;
		}

		return success;
	}

	public void cashdrawerOpen() {

		byte[] open = {27, 112, 48, 55, 121};
		//        byte[] cutter = {29, 86,49};
		String printer = Application.getPrinters().getReceiptPrinter();
		PrintServiceAttributeSet printserviceattributeset = new HashPrintServiceAttributeSet();
		printserviceattributeset.add(new PrinterName(printer,null));
		PrintService[] printservice = PrintServiceLookup.lookupPrintServices(null, printserviceattributeset);
		if(printservice.length!=1){
			System.out.println("Printer not found");
		}
		PrintService pservice = printservice[0];
		DocPrintJob job = pservice.createPrintJob();
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		Doc doc = new SimpleDoc(open,flavor,null);
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		try {
			job.print(doc, aset);
		} catch (PrintException ex) {
			System.out.println(ex.getMessage());
		}
	}



	public void openCashDrawerFile() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec(TerminalConfig.getCashDrawerFile());
			InputStream is = p1.getInputStream();
			int i = 0;
			while ((i = is.read()) != -1) {
				System.out.print((char) i);
			}
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}
	}

	public void openCashDrawer() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec("cmd.exe /c copy /b print.dat " + TerminalConfig.getCashDrawerCom());
			InputStream is = p1.getInputStream();
			int i = 0;
			while ((i = is.read()) != -1) {
				System.out.print((char) i);
			}
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}
	}


	public boolean isReachable(String ipAddress) {
		boolean isConnected = false;
		if (!ipAddress.isEmpty()) {
			try {
				List<String> commands = new ArrayList<String>();
				commands.add("ping");
				commands.add(ipAddress);
				String s = null;

				ProcessBuilder pb = new ProcessBuilder(commands);
				Process process = pb.start();

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);

					if (s.contains("Reply") && s.contains(ipAddress)
							|| s.contains("Antwort") && s.contains(ipAddress)) {
						return true;

					} else if (s.contains("out") || s.contains("berschreitung")) {
						return false;

					}

				}

				// read any errors from the attempted command
				while ((s = stdError.readLine()) != null) {
					return false;

				}

			} catch (Exception ex) {
				return false;
			}
		}
		return isConnected;

	}

	public void openCashDrawerPrint() {
		try {
			//			HashMap map = new HashMap();
			//			Date date = new Date();
			//			DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			//			String timeStamp = dateFormat1.format(date);
			//			map.put("kitchenMessage", "Schublade :" + timeStamp);
			//			JasperPrint jasperPrint = JReportPrintService.createCashDrawerPrint(map);
			//			jasperPrint.setName("SchubladeMessage");
			//			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			//			JReportPrintService.printQuitely(jasperPrint);

			byte[] open = {27, 112, 48, 55, 121};
			//        byte[] cutter = {29, 86,49};
			String printer = Application.getPrinters().getReceiptPrinter();
			PrintServiceAttributeSet printserviceattributeset = new HashPrintServiceAttributeSet();
			printserviceattributeset.add(new PrinterName(printer,null));
			PrintService[] printservice = PrintServiceLookup.lookupPrintServices(null, printserviceattributeset);
			if(printservice.length!=1){
				System.out.println("Printer not found");
			}
			PrintService pservice = printservice[0];
			DocPrintJob job = pservice.createPrintJob();
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			Doc doc = new SimpleDoc(open,flavor,null);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			try {
				job.print(doc, aset);
			} catch (PrintException ex) {
				System.out.println(ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void openTicket() { List<ShopTable> tables = new ArrayList();
	 * ShopTable table = ShopTableDAO.getInstance().getByNumber("99"); if (table ==
	 * null) { table = new ShopTable(); table.setNumber("99"); tables.add(table); }
	 * else tables.add(table);
	 * 
	 * Application application = Application.getInstance();
	 * 
	 * Ticket ticket = new Ticket();
	 * 
	 * if (tables.size() == 1) { for (ShopTable shopTable : tables) { if
	 * (shopTable.getNumber().compareTo("99") == 0)
	 * ticket.setType(TicketType.HOME_DELIVERY); else { shopTable.setOccupied(true);
	 * ticket.setType(TicketType.DINE_IN); } ticket.addTotables(shopTable); break; }
	 * } else ticket.setType(TicketType.DINE_IN);
	 * 
	 * ticket.setPriceIncludesTax(application.isPriceIncludesTax());
	 * 
	 * ticket.setNumberOfGuests(2); ticket.setTerminal(application.getTerminal());
	 * ticket.setOwner(Application.getCurrentUser());
	 * ticket.setShift(application.getCurrentShift());
	 * 
	 * if (tables.size() != 1) { for (ShopTable shopTable : tables) {
	 * shopTable.setOccupied(true); ticket.addTotables(shopTable); } }
	 * 
	 * Calendar currentTime = Calendar.getInstance();
	 * ticket.setCreateDate(currentTime.getTime());
	 * ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
	 * OrderView.getInstance().setCurrentTicket(ticket);
	 * RootView.getInstance().showView(OrderView.VIEW_NAME); focusToScanid();
	 * 
	 * }
	 */

	public void createNewTicket() {
		try {
			OrderServiceExtension orderService = new DefaultOrderServiceExtension();
			if (TerminalConfig.istype())
				orderService.createNewTicket(TicketType.HOME_DELIVERY);
			else
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
			startNewTseOrder();

			if (TerminalConfig.istype()) {
				border.setTitle("MITNAHME");
			} else {
				border.setTitle("BESTELLUNG");
			}
			
			this.updateView();

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
		if(isTseEnable)
			posTseController.setRestartOrder(true);
		Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticket.getId());
		OrderView.getInstance().setCurrentTicket(ticketToEdit);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	public void addDeliveryCost(Ticket ticket) {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(ticket.getDeliveryCost());
		ticketItem.setName("Lieferkost");
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setShouldPrintToKitchen(false);
		ticketItem.setItemId(999);
		ticketItem.setBeverage(false);
		Double Tax = Application.getInstance().getHomeDeleveryTax();
		Double subTotal = ticket.getDeliveryCost() / (1 + (Tax / 100));
		Double taxAmount = ticket.getDeliveryCost() - subTotal;
		ticketItem.setSubtotalAmount(taxAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(Tax);
		//		addTseItem(ticketItem, ticket);//for Tse update
		addTicketItem(ticketItem);
	}

	public boolean isDeliveryCostAdded(Ticket ticket) {
		List<TicketItem> ticketList = ticket.getTicketItems();
		for (Iterator<TicketItem> itr = ticketList.iterator(); itr.hasNext();) {
			TicketItem item = itr.next();
			if (item.getItemId() == 999) {
				return true;
			}
		}
		return false;
	}

	private void doDeleteSelection() {// GEN-FIRST:event_doDeleteSelection
			setRabatt(null);
			
		 if(ticket.getId()!=null&&ticket.getId()>0) {
			try {
				updateTicketItems(ticket.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
			
			ticket.setDiscountAmount(0.00);
			ticket.setCouponAndDiscounts(null);
			int selectedRow = ticketViewerTable.getSelectedRow();
			Object object = ticketViewerTable.deleteSelectedItem();
			
			try {
				TicketItem ticketItem_ = (TicketItem) object;
				if(ticket.getId()!=null&&ticket.getId()>0 && Application.getCurrentUser().getFirstName().compareTo("Master") != 0) {
					if(isTseEnable&&ticketItem_.isPrintedToKitchen()) {
						System.out.println("if 1");
							posTseController.cancelItem(ticketItem_, true, "");
							posTseController.setRestartOrder(true);
							ticket.addDeletedItems(ticketItem_);
							updateView();
					}
				}				
			} catch(Exception ex) {

			}
			
			TicketItem ticketItem = (TicketItem) object;
			
			System.out.println("selectedRow: "+ selectedRow + ", ticketItmCount: "+ ticketItem.getItemCount() + ", ActualCountRow: "+ ticketViewerTable.getActualRowCount());
			
			if(selectedRow==0 && ticketViewerTable.getActualRowCount()==0&&ticket.getId()!=null&&ticket.getId()>0) {
				
				TicketDAO.getInstance().delete(ticket.getId());
				updateView();
				return;
			}			
		
			if (object != null) {					
					if(ticket.getId()!=null && Application.getCurrentUser().getFirstName().compareTo("Master") != 0&&(TerminalConfig.isKhanaServer())) {
						saveDeleteKitchenItem(ticketItem);
					}	
					
						updateView();
				
				if (object instanceof TicketItemModifier) {
					
					ModifierView modifierView = OrderView.getInstance().getModifierView();
					if (modifierView.isVisible()) {
						modifierView.updateVisualRepresentation();
					}
				}			 			
		    }
			
			if (ticketViewerTable.getRowCount() > 0) {
				 
				if (selectedRow == (ticketViewerTable.getRowCount()))
					ticketViewerTable.setRowSelectionInterval(0, ticketViewerTable.getRowCount() - 1);
				else {
					if (selectedRow != -1)
						ticketViewerTable.setRowSelectionInterval(0, selectedRow);
				}
			}
			 
		if(ticket.getId()!=null&&ticket.getId()>0) {
			try {
				updateTicketItems(ticket.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
			
		}// GEN-LAST:event_doDeleteSelection

	public static void updateTicketItems(int ticketId) throws Exception {
		
		if(ticketId>0) {
	      //Registering the driver
	      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	      boolean result= false;
	      
	      String conection = AppConfig.getConnectString();
	      String URL = conection+";create=true";
	      Connection conn = DriverManager.getConnection(URL);
	     
	      Statement stmt = conn.createStatement();
	      Statement stmt1 = conn.createStatement();
	      Statement stmt2 = conn.createStatement();
	      int itemId=0;
	      ResultSet getTableId = stmt.executeQuery("SELECT * FROM TICKET_ITEM WHERE TICKET_ID="+ticketId);
	      String aa = "SELECT * FROM TICKET_ITEM WHERE TICKET_ID="+ticketId;
	       
	      int i=0;
	      while (getTableId.next()) {	    	  
	    	  itemId=getTableId.getInt("ID");   	  
	    	    String sql = "UPDATE TICKET_ITEM SET ITEM_ORDER="+ i +" WHERE TICKET_ID="+ticketId+" AND ID="+getTableId.getInt("ID");
	    	   
		        stmt2.executeUpdate(sql);
		        i++;	    	 
	      }
	     
	      getTableId.close();
	     	     
	      stmt.close();	 
	      stmt1.close();
	      stmt2.close();
	      conn.close();
		 }
	   }
	
	public void arrowDown() {
		int nextRow = ticketViewerTable.getSelectedRow() + 1;

		if (ticketViewerTable.getSelectedRow() != -1) {
			if (ticketViewerTable.getRowCount() > 7 && ticketViewerTable.getSelectedRow() > 6) {
				ticketViewerTable.scrollDown();
			}
			if (nextRow <= ticketViewerTable.getRowCount() - 1)
				ticketViewerTable.setRowSelectionInterval(0, nextRow);

		} else if (ticketViewerTable.getRowCount() > 0) {
			ticketViewerTable.setRowSelectionInterval(0, 0);
			ticketViewerTable.scrollAbove();
		}
	}

	public void arrowUp() {
		int previousRow = ticketViewerTable.getSelectedRow() - 1;

		if (ticketViewerTable.getSelectedRow() != -1) {

			if (ticketViewerTable.getRowCount() > 8 && ticketViewerTable.getSelectedRow() < 8) {
				ticketViewerTable.scrollUp();
			}

			if (previousRow >= 0)
				ticketViewerTable.setRowSelectionInterval(0, previousRow);
		} else if (ticketViewerTable.getRowCount() > 0) {
			ticketViewerTable.setRowSelectionInterval(0, 0);
			ticketViewerTable.scrollAbove();
		}
	}

	private void doIncreaseAmount() {// GEN-FIRST:event_doIncreaseAmount
		if (ticketViewerTable.increaseItemAmount()) {
			ModifierView modifierView = OrderView.getInstance().getModifierView();
			if (modifierView.isVisible()) {
				modifierView.updateVisualRepresentation();
			}
			updateView();
		}
	}// GEN-LAST:event_doIncreaseAmount

	/*
	 * public void doPayNow() {// GEN-FIRST:event_doPayNow try {
	 * 
	 * if (ticket.getTicketItems().size() <= 0) {
	 * POSMessageDialog.showError("Bestellung ist leer"); return; }
	 * 
	 * updateModel(); // OrderController.saveOrder(ticket); firePayOrderSelected();
	 * if (TerminalConfig.isTabVersion()) { // openTicket(); createNewTicket();
	 * updateCategoryView(); } } catch (PosException e) {
	 * POSMessageDialog.showError(e.getMessage()); } }
	 */

	private void doScrollDown(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doScrollDown
		ticketViewerTable.scrollDown();
	}// GEN-LAST:event_doScrollDown

	private void doScrollUp() {// GEN-FIRST:event_doScrollUp
		ticketViewerTable.scrollUp();
	}// GEN-LAST:event_doScrollUp

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.TransparentPanel controlPanel;
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnDelete;
	private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.PosButton btnIncreaseAmount;
	private com.floreantpos.swing.PosButton btnMisc19;
	private com.floreantpos.swing.PosButton btnRabatt;

	private com.floreantpos.swing.PosButton btnMisc7;
	private com.floreantpos.swing.PosButton btnPayPrint;
	public com.floreantpos.swing.TransparentPanel jPanel1;
	private com.floreantpos.swing.TransparentPanel jPanel2;
	private com.floreantpos.swing.TransparentPanel ticketAmountPanel;
	private com.floreantpos.swing.PosButton btnMultiUser;
	// private com.floreantpos.swing.TransparentPanel scrollerPanel;
	
	private com.floreantpos.swing.TransparentPanel jPanelActionPanel;
	
	private javax.swing.JScrollPane jScrollPane1;
	// private javax.swing.JTextField tfDiscount;
	private javax.swing.JTextField tfscanid;
	// private javax.swing.JTextField tfTax;
	private javax.swing.JTextField tfTotal;
	public TicketItem lastItem;
	private com.floreantpos.ui.ticket.TicketViewerTable ticketViewerTable;
	// private JTextField tfServiceCharge;
	// private JLabel lblServiceCharge;
	private PosButton btnAddCookingInstruction;

	// End of variables declaration//GEN-END:variables

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket _ticket) {
		this.ticket = _ticket;
		discountAmount = 0.00;
		ticketViewerTable.setTicket(_ticket);
		updateView();
	}

	public void addTicketItem(TicketItem ticketItem) {
		if (ticketItem != null)
			ticketViewerTable.addTicketItem(ticketItem);
		updateView();
	}

	public String getItemZahl() {
		String value = tfscanid.getText();
		return value;
	}

	public void resetTextField() {
		tfscanid.setText("");
	}

	public void setItemSelectionListener(ItemSelectionListener itemSelectionListener) {
		this.itemSelectionListener = itemSelectionListener;
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		ticketViewerTable.removeModifier(parent, modifier);
	}

	// private NumberFormat numberFormat = new DecimalFormat("0.00");

	public void updateAllView() {
		ticketViewerTable.updateView();
		updateView();
	}

	public String getPaidAmount() {
		return tfscanid.getText();
	}

	public Double getTotalAmount() {
		return ticket.getTotalAmount();
	}

	public void selectRow(int rowIndex) {
		ticketViewerTable.selectRow(rowIndex);
	}

	public void updateView() {
		if (ticket == null) {
			clearDisplay();
			tfTotal.setText("");
			setBorder(BorderFactory.createTitledBorder(null, "Ticket [ NEW ]", TitledBorder.CENTER,
					TitledBorder.DEFAULT_POSITION));
			return;
		}

		try {
		ticket.calculatePrice();
		} catch (Exception e) {  };
		try {
			if(ticket.getTicketItems()!=null&&!ticket.getTicketItems().isEmpty()) {				 
				  updateAmount();				 
			} else {
				tfTotal.setText("0,00");
				//seperateTicketAmountPanel.getTfTotal().setText("0,00");
			}
		} catch (Exception ex) {

		}

		int w = 20;
		String display1 = StringUtils.center(StringUtils.center(TerminalConfig.getKundenDisplayText1(), w - 2), w, "");
		String displayString = StringUtils.center(StringUtils.center(TerminalConfig.getKundenDisplayText2(), w - 2), w,
				"");
		//double totalAmount = ticket.getTotalAmountWithoutPfand();
		double totalAmount = ticket.getTotalAmountWithoutPfand();
		if (!Application.getInstance().isPriceIncludesTax())
			totalAmount = totalAmount - ticket.getTaxAmount();
		if (TerminalConfig.isDisplay()) {
			try {
				try {
					lastItem = (TicketItem) ticketViewerTable.get(ticketViewerTable.getSelectedRow());
					try {
						display1 = lastItem.getName().substring(0, 11) + " . "
								+ NumberUtil.formatNumber(lastItem.getUnitPrice());
					} catch (Exception ex) {
						display1 = lastItem.getName() + " . " + NumberUtil.formatNumber(lastItem.getUnitPrice());
					}
					totalAmount = ticket.getTotalAmountWithoutPfand();
					displayString = "GESAMT: " + NumberUtil.formatNumber(totalAmount);
				} catch (Exception ex) {
					// ex.printStackTrace();
					if (totalAmount > 0) {
						display1 = "";
						displayString = "GESAMT: " + NumberUtil.formatNumber(totalAmount);
					}
				}

				Display display = Display.getInstance();
				if (!TerminalConfig.isHideCurrency()) {
					displayString = displayString + " €";
					display1 = display1 + " €";
				}
				display.PrintFirstLine(display1);
				display.PrintSecondLine(displayString);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (border == null) {
			border = new TitledBorder("BESTELLUNG");
			border.setTitleJustification(TitledBorder.CENTER);
			border.setTitleColor(Color.WHITE);
			border.setTitleFont(new Font("Serif", Font.BOLD, 16));
			setBorder(border);
		}
		 
		int count = getItemCount(ticket.getTicketItems());
		if (TerminalConfig.isKundenScreen() && ticket.getTicketItems() != null && ticket.getTicketItems().size() > 0) {
			startCustomerDisplay();
			CustomerScreen.updateView(count, ticket, ticketViewerTable.getSelectedRow(), lblDisplay.getText());
		} else {
			closeCustomerDisplay();
		}
		 
		OrderView.getInstance().getOthersView().setDisplay(count, getRabatt());
		this.requestDefaultFocus();
		if (TerminalConfig.isBothInput()) {
			tfscanid.requestFocus();
		}
		
		 
		//OrderView.getInstance().getOthersView().clearCustomer();
		//OrderView.getInstance().getOthersView().setCustomer(ticket, null);
		this.updateDisplayIfAvailabel();
	}

	public void updateDisplayIfAvailabel() {
		OthersView view = OrderView.getInstance().getOthersView();
		if (this.ticket.getTipAmount() != null && this.ticket.getTipAmount() > 0.00) {
			view.updateTipsAmount(this.ticket.getTipAmount());
		}
		 
		updateGutschein();
	}
	
	public String rabatt;

	public String getRabatt() {
		return rabatt != null ? rabatt : "";
	}

	public void setRabatt(String rabatt) {
		this.rabatt = rabatt;
	}

	public static int getItemCount(List<TicketItem> ticketItems) {
		int count = 0;
		for (TicketItem item : ticketItems) {
			count += item.getItemCount();
		}

		return count;
	}

	JFrame frameCus = new CustomerScreen();

	public void startCustomerDisplay() {
		frameCus.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameCus.setResizable(true);
		frameCus.setVisible(true);
		frameCus.setTitle("");
		frameCus.setFocusable(false);
		frameCus.getContentPane().setLayout(new MigLayout());
		int displayNummer = 1;
		/*
		 * GraphicsDevice device =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; try
		 * { device.setFullScreenWindow((Window) frame); } finally {
		 * device.setFullScreenWindow(null); }
		 */
		try {
			displayNummer = TerminalConfig.getSDNummer() != null ? Integer.parseInt(TerminalConfig.getSDNummer()) : 1;
		} catch (Exception ex) {
			TerminalConfig.setSDNummer(Integer.toString(displayNummer));
		}
		showOnScreen(displayNummer, frameCus);
	}

	public void closeCustomerDisplay() {
		try {
			frameCus.hide();
		} catch (Exception ex) {

		}
	}

	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

	private TicketItem createMiscFood() {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(0.00);
		ticketItem.setName("Sonstiges Speisen");
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(0);
		ticketItem.setPrintedToKitchen(true);
		ticketItem.setBon(1);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);
		double tax = Application.getInstance().homeDeleveryTax;
		Double taxAmount = 0.00;
		ticketItem.setSubtotalAmount(taxAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(tax);
		return ticketItem;
	}

	public void updateAmount() {
		String discountName = "";
		double discountValue = 0.00;
		double gutsValue = 0.0;

		List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
		if (couponList != null) {
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				if (discount.getType()==CouponAndDiscount.GUTSCHEIN) {
					gutsValue += discount.getValue();
				}				
			}
			if(gutsValue>0) {
				discountValue = gutsValue;				
			}
		}
		if (ticket.getGutschrift() != null && ticket.getGutschrift() > 0.00&&gutsValue<0.0) {
			discountValue = ticket.getGutschrift();
		} else {
			discountValue += ticket.getGutschrift();
		}

		double tipAmnt = 0.00;
		if (ticket.getTipAmount() != null && ticket.getTipAmount() > 0.00) {
			tipAmnt = ticket.getTipAmount();			 
		}
	
		String totalAmount="";
		if(ticket.getGutscheinPayment()!=null&&ticket.getGutscheinPayment()) {
		           totalAmount = NumberUtil.formatNumber(ticket.getSubtotalAmount() - discountValue + tipAmnt);
		} else {
			       totalAmount = NumberUtil.formatNumber(ticket.getSubtotalAmount()-ticket.getDiscountAmount() + tipAmnt);
		}
		
		if (TerminalConfig.isDisplay()) {
			int w = 20;
			String display1 = StringUtils.center(StringUtils.center(TerminalConfig.getKundenDisplayText1(), w - 2), w,
					"");
			String displayString = StringUtils.center(StringUtils.center(TerminalConfig.getKundenDisplayText2(), w - 2),
					w, "");
			try {
				try {
					Object object = ticketViewerTable.get(ticketViewerTable.getSelectedRow());
					if (object instanceof TicketItem) {
						TicketItem lastItem = (TicketItem) object;
						try {
							display1 = lastItem.getName().substring(0, 11) + " . "
									+ NumberUtil.formatNumber(lastItem.getUnitPrice());
						} catch (Exception ex) {
							try {
								display1 = lastItem.getName() + " . "
										+ NumberUtil.formatNumber(lastItem.getUnitPrice());
							} catch (Exception exxx) {

							}
						}
						displayString = "GESAMT: " + totalAmount;
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					if (getTotalAmount(ticket) > 0) {
						display1 = "";
						displayString = "GESAMT: " + totalAmount;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Display display = Display.getInstance();
			display.PrintFirstLine(display1);
			display.PrintSecondLine(displayString);
		}
          
		this.tfTotal.setText(totalAmount + " " + Application.getCurrencySymbol());
		
		try {
			OrderView.getInstance().tfTotal.setText(totalAmount + " €");
		} catch (Exception ex) {

		}
	}
	
	public void setTips(int cents) {
		double tipAmount = cents / (double) 100;
		this.ticket.setTipAmount(tipAmount);
		this.ticket.setChangesAvailable(true);		
	     OrderView.getInstance().getOthersView().updateTipsAmount(tipAmount);
	}
	
	public Double getTotalAmount(Ticket ticket1) {
		Double totalAmount = 0.00;
		List<TicketItem> ticketList = ticket.getTicketItems();
		for (Iterator<TicketItem> itr = ticketList.iterator(); itr.hasNext();) {
			TicketItem item = itr.next();
			if (item.getItemId() != 999) {
				totalAmount += item.getTotalAmount();
			}
		}
		return totalAmount;
	}

	public void addOrderListener(OrderListener listenre) {
		orderListeners.add(listenre);
	}

	public void removeOrderListener(OrderListener listenre) {
		orderListeners.remove(listenre);
	}

	public void firePayOrderSelected() {
		for (OrderListener listener : orderListeners) {
			listener.payOrderSelected(getTicket());
		}
	}

	public void setControlsVisible(boolean visible) {
		if (visible) {
			controlPanel.setVisible(true);
			btnIncreaseAmount.setEnabled(true);
			btnDelete.setEnabled(true);
			tfscanid.setVisible(true);
		} else {
			controlPanel.setVisible(false);
			btnIncreaseAmount.setEnabled(false);
			btnDelete.setEnabled(false);
			tfscanid.setVisible(true);
		}		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			searchItem();
			tfscanid.setText(null);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}
	public Ticket getCurrentTicket() {
		return ticket;
	}
	
	private boolean checkPin() {
		final User user = Application.getCurrentUser();
		if (user != null && user.getFirstName().equals("Master")) {
			return true;
		}

		if (!OrderView.getInstance().getOthersView().checkPin())
			return false;
		return true;
	}
	
	public double getFixRabatt() {
		double totalDiscount = 0.0;
		try {
		List<TicketItem> items = ticket.getTicketItems();
		if(items!=null) {
			for(TicketItem item:items) {
				totalDiscount += ReportUtil.getFixRabatt(item);				 
			}
		}
		} catch (Exception e) {}
		return totalDiscount;
	}

	public void clearDisplay() {
		
		lblDisplay.setText("");
		lblDisplay.setBackground(Color.WHITE);
		lblDisplay.setFont(new Font(null, Font.BOLD, 14));
	}
	
	boolean gutscheinEingeloschst = false;
	public void updateGutschein() {
		clearDisplay();
		List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
		String discountName = "";
		boolean show = false;
		boolean guts = false;
		boolean dirRabt = false;
		double dvalue = 0.00;
		double gutsValue = 0.0;
		double fixRabatValue = getFixRabatt();
		if (couponList != null) {
			for (Iterator itr = couponList.iterator(); itr.hasNext();) {
				TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
				if (discount.getType() == CouponAndDiscount.GUTSCHEIN) {
					//dvalue = getCurrentTicket().getDiscountAmount();
					dvalue = discount.getUsedValue();
					 
					System.out.println("Gutschein: "+discount.getUsedValue());
					gutsValue += discount.getUsedValue();
					if (gutsValue == (ticket.getSubtotalAmount() + ticket.getTipAmount())
							|| gutsValue > (ticket.getSubtotalAmount() + ticket.getTipAmount())) {
						gutscheinEingeloschst = true;
					}
					if (!show && !guts) {
						show = true;
						guts = true;
						discountName = "Gutschein eingelöst";
					} else if (!guts) {
						discountName = discountName + "/ Guts. englst";
					}
				}
				if (discount.getType() == CouponAndDiscount.DIRECT_RABATT) {
					if (fixRabatValue > 0)
						dvalue = fixRabatValue + getCurrentTicket().getDiscountAmount();
					dvalue = getCurrentTicket().getDiscountAmount();
					if (!show && !dirRabt) {
						show = true;
						discountName = "DIREKT-RABATT";
						dirRabt = true;
					} else if (!dirRabt) {
						discountName = discountName + "/ DRKT-RBT";
					}
				}
			}
		}

		if (show) {			 
			String displayText = lblDisplay.getText();
			lblDisplay.setText((StringUtils.isNotBlank(displayText) ? displayText : "") + " " + discountName + ": "
					+ NumberUtil.formatNumber(dvalue) + " " + Application.getCurrencySymbol());
			System.out.println("(show): "+ lblDisplay.getText());
			
			OrderController.saveOrder(ticket);
			
		} else if (ticket.getExtraDeliveryInfo() != null && !ticket.getExtraDeliveryInfo().isEmpty()) {
			lblDisplay.setFont(new Font("Myrid Proc Cond", Font.BOLD, 10));
			lblDisplay.setForeground(new Color(0, 255, 0));
			lblDisplay.setText(ticket.getExtraDeliveryInfo());
			System.out.println("(show)_: "+ lblDisplay.getText());
		}

	}
	
	protected void saveDeleteKitchenItem(TicketItem item) {
		 
			try {
				List<KitchenTicket> kitcheTickets = KitchenTicketDAO.getInstance().findByTicketId(ticket.getId());
				List<KitchenTicketItem> itemList = new ArrayList<>();
				for (KitchenTicket kTicket : kitcheTickets) {
					if (kTicket != null)
						itemList.addAll(kTicket.getTicketItems());
				}

				for (KitchenTicketItem kItem : itemList) {
					if (kItem == null)
						continue;
					if (item.getItemCode().compareTo(kItem.getMenuItemCode()) == 0) {
					
						if (kItem.getQuantity() > 1) {
							 
							double itemPrice = Double.valueOf(kItem.getMenuItemPrice().replace(",", "."))
									/ kItem.getQuantity();
							kItem.setMenuItemPrice(String.valueOf((itemPrice * (kItem.getQuantity() - 1))));
							kItem.setQuantity(kItem.getQuantity() - 1);
							KitchenTicketItemDAO.getInstance().saveOrUpdate(kItem);
						} else {
							KitchenTicketItemDAO.getInstance().delete(kItem.getId());
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		KitchenTicket deleteItem = new KitchenTicket();
		//deleteItem.setStornotext(item.getStornoreason());
		deleteItem.setCreateDate(new Date());
		 
		deleteItem.setTicketId(ticket.getId());
		deleteItem.setItemCount(1);
		deleteItem.setItemName(item.getName());
		//deleteItem.setItemunitprice(NumberUtil.formatNumber(item.getUnitPrice()));
		//deleteItem.setTotalAmount(item.getUnitPrice() * item.getItemCount());
		deleteItem.setTotalAmount(item.getUnitPrice());
		//deleteItem.setTotalprice(NumberUtil.formatNumber(item.getUnitPrice() * item.getItemCount()));
		if (Application.getCurrentUser() != null && Application.getCurrentUser().getFirstName() != null)
			deleteItem.setServerName(Application.getCurrentUser().getFirstName());
		if (ticket.getId() != null)
			deleteItem.setTicketId(ticket.getId());
		deleteItem.setTableNumbers(ticket.getTableNumbers());
		if (item.isHasModifiers()) {
			int mCount = 0;
			Double value = 0.00;
			List<TicketItemModifierGroup> ticketItemModifierGroups = item.getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier itemModifier : ticketItemModifiers) {
							if (itemModifier.getTotalAmount() > 0.00) {
								saveDeleteKitchenItem(itemModifier);
							}
						}
					}
				}
			}
		}
		KitchenTicketDAO.getInstance().saveOrUpdate(deleteItem);
	}
	
	protected void saveDeleteKitchenItem(TicketItemModifier item) {
	    KitchenTicket deleteItem = new KitchenTicket();
		 
		deleteItem.setCreateDate(new Date());
		deleteItem.setItemCount(1);
		deleteItem.setServerName(item.getName());
		//deleteItem.setItemunitprice(NumberUtil.formatNumber(item.getUnitPrice()));
		deleteItem.setTotalAmount(item.getTotalAmount());
		//deleteItem.setTotalprice(NumberUtil.formatNumber(item.getTotalAmount()));
		if (Application.getCurrentUser() != null && Application.getCurrentUser().getFirstName() != null)
			deleteItem.setServerName(Application.getCurrentUser().getFirstName());
		if (ticket.getId() != null)
			deleteItem.setTicketId(ticket.getId());
		deleteItem.setTableNumbers(ticket.getTableNumbers());
		KitchenTicketDAO.getInstance().saveOrUpdate(deleteItem);
	}
	
	public void performGutscheinDirectRabatt() {
		if (!checkPin()) {
			return;
		}

		DirectDiscountDialog dialog = new DirectDiscountDialog(ticket);
		dialog.setPreferredSize(new Dimension(1000, 600));
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			updateAllView();
			return;
		}

		Double value = dialog.getValue();
		value = (value / 100);

		Integer gutscheinId = dialog.getGutscheinId();

		if (value > ticket.getTotalAmount() && gutscheinId == 0) {
			POSMessageDialog.showError("Rabatt ist höher als gesamtpreis!");
			return;
		} else if (value > 0) {
			if (dialog.isExternalRabatt()) {
				ticket.setGutschrift(ticket.getGutschrift() + value);
			} else {
				TicketCouponAndDiscount coupon = new TicketCouponAndDiscount();
				if (gutscheinId == 0) {
					// Todo: Direkt Rabatt Error in this Sectiom

					coupon.setName("Direkt");
					coupon.setType(CouponAndDiscount.DIRECT_RABATT);

					Double percentage = (value / ticket.getPositiveSubtotal(TerminalConfig.isIncludeRabattModifier()))
							* 100;
					coupon.setValue(percentage);
					coupon.setUsedValue(value);

					List<TicketCouponAndDiscount> couponAndDiscounts = ticket.getCouponAndDiscounts() != null
							? ticket.getCouponAndDiscounts()
							: new ArrayList<>();

					couponAndDiscounts.add(coupon);

					ticket.setCouponAndDiscounts(couponAndDiscounts);
					// ticket.calculatePrice(percentage, false,
					// CouponAndDiscount.PERCENTAGE_PER_ITEM);
					ticket.calculatePrice();
				} else if (gutscheinId > 0) {
					
					coupon.setName("Gutsch.");
					coupon.setType(CouponAndDiscount.GUTSCHEIN);
					if (dialog.getGutscheinId() > 0) {
						coupon.setGutschein(dialog.getGutscheinId());
					}
					coupon.setValue(value);
					coupon.setUsedValue(value);
					ticket.addTocouponAndDiscounts(coupon);
					double discountAmnt = ticket.getDiscountAmount();					
					discountAmnt += ticket.calculateDiscountFromType(coupon, ticket.getTotalAmount() + discountAmnt);
					ticket.setDiscountAmount(discountAmnt);			
					System.out.println("performGutscheinDirectRabatt: " + ticket.getTotalAmount());
					 
					ticket.setTotalAmount(ticket.getTotalAmount() + ticket.getTipAmount());
				}

				OrderController.saveOrder(ticket);
			}
		}
		updateView();
	}
}
