package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.TicketView.ArrowAction;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class PaymentView extends JPanel {
	private static final String ADD = "0";

	private static final String REMOVE = "1";

	protected SettleTicketDialog settleTicketView;

	private PosButton btnGratuity;

	private com.floreantpos.swing.PosButton btnCancel;
	//private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.TransparentPanel calcButtonPanel;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel6;
	private com.floreantpos.swing.TransparentPanel actionButtonPanel;
	private com.floreantpos.swing.PosButton posButton1;
	private com.floreantpos.swing.PosButton posButton10;
	private com.floreantpos.swing.PosButton posButton11;
	private com.floreantpos.swing.PosButton posButton12;
	private com.floreantpos.swing.PosButton posButton2;
	private com.floreantpos.swing.PosButton posButton3;
	private com.floreantpos.swing.PosButton posButton4;
	private com.floreantpos.swing.PosButton posButton5;
	private com.floreantpos.swing.PosButton posButton6;
	private com.floreantpos.swing.PosButton posButton7;
	private com.floreantpos.swing.PosButton posButton8;
	private com.floreantpos.swing.PosButton posButton9;
	private com.floreantpos.swing.FocusedTextField tfAmountTendered;
	private com.floreantpos.swing.FocusedTextField tfDueAmount;
	private com.floreantpos.swing.TransparentPanel transparentPanel1;
	private PosButton btnCash;
	private PosButton btnCard;
	private PosButton btnOnline;
//	private POSToggleButton btnTaxExempt;
	private PosButton btnCoupon;
	private PosButton btnViewCoupons;
	private JLabel label;
	JLabel lblCopies;
	JComboBox cbCopies;
	private PaymentType paymentType;
	public PaymentView(SettleTicketDialog settleTicketView) {
		this.settleTicketView = settleTicketView; 

		initComponents();

		//		btnUseKalaId.setActionCommand(ADD);
	}
	public class ArrowAction extends AbstractAction {

	    private String cmd;

	    public ArrowAction(String cmd) {
	        this.cmd = cmd;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if (cmd.equalsIgnoreCase("ESC")) {
	        	btnCancelActionPerformed();
	        } else if (cmd.equalsIgnoreCase("ENTER")) {
	        	getBtnCash().setBackground(Color.GREEN);
				btnCard.setBackground(null);
				setPaymentType(PaymentType.CASH);
				doFinish();
	        } 
	        else if(cmd.equalsIgnoreCase("RIGHT"))
	        {
	        	btnCard.setBackground(Color.GREEN);
				setPaymentType(PaymentType.CARD);
				getBtnCash().setBackground(null);
				doFinish();
			
	        }
	       
	    }
	}
	private void initComponents() {
		calcButtonPanel = new com.floreantpos.swing.TransparentPanel();
		posButton1 = new com.floreantpos.swing.PosButton();
		posButton2 = new com.floreantpos.swing.PosButton();
		posButton3 = new com.floreantpos.swing.PosButton();
		posButton4 = new com.floreantpos.swing.PosButton();
		posButton5 = new com.floreantpos.swing.PosButton();
		posButton6 = new com.floreantpos.swing.PosButton();
		posButton9 = new com.floreantpos.swing.PosButton();
		posButton8 = new com.floreantpos.swing.PosButton();
		posButton7 = new com.floreantpos.swing.PosButton(); 
		posButton11 = new com.floreantpos.swing.PosButton();
		posButton10 = new com.floreantpos.swing.PosButton();
		posButton12 = new com.floreantpos.swing.PosButton();
		actionButtonPanel = new com.floreantpos.swing.TransparentPanel();
		transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
		jLabel4 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		tfDueAmount = new com.floreantpos.swing.FocusedTextField();
		tfDueAmount.setFocusable(false);
		tfAmountTendered = new com.floreantpos.swing.FocusedTextField();
		setBackground(new Color(209,222,235));
		setLayout(new MigLayout());

		calcButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		calcButtonPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
		calcButtonPanel.setBackground(new Color(209,222,235));
		posButton1.setAction(calAction);
		posButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7_32.png"))); // NOI18N
		posButton1.setActionCommand("7");
		posButton1.setFocusable(false);
		calcButtonPanel.add(posButton1);

		posButton2.setAction(calAction);
		posButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/8_32.png"))); // NOI18N
		posButton2.setActionCommand("8");
		posButton2.setFocusable(false);
		calcButtonPanel.add(posButton2);

		posButton3.setAction(calAction);
		posButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/9_32.png"))); // NOI18N
		posButton3.setActionCommand("9");
		posButton3.setFocusable(false);
		calcButtonPanel.add(posButton3);

		posButton4.setAction(calAction);
		posButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/4_32.png"))); // NOI18N
		posButton4.setActionCommand("4");
		posButton4.setFocusable(false);
		calcButtonPanel.add(posButton4);

		posButton5.setAction(calAction);
		posButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/5_32.png"))); // NOI18N
		posButton5.setActionCommand("5");
		posButton5.setFocusable(false);
		calcButtonPanel.add(posButton5);

		posButton6.setAction(calAction);
		posButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/6_32.png"))); // NOI18N
		posButton6.setActionCommand("6");
		posButton6.setFocusable(false);
		calcButtonPanel.add(posButton6);

		posButton9.setAction(calAction);
		posButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1_32.png"))); // NOI18N
		posButton9.setActionCommand(REMOVE);
		posButton9.setFocusable(false);
		calcButtonPanel.add(posButton9);

		posButton8.setAction(calAction);
		posButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2_32.png"))); // NOI18N
		posButton8.setActionCommand("2");
		posButton8.setFocusable(false);
		calcButtonPanel.add(posButton8);

		posButton7.setAction(calAction);
		posButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3_32.png"))); // NOI18N
		posButton7.setActionCommand("3");
		posButton7.setFocusable(false);
		calcButtonPanel.add(posButton7);

		posButton11.setAction(calAction);
		posButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/0_32.png"))); // NOI18N
		posButton11.setActionCommand(ADD);
		posButton11.setFocusable(false);
		calcButtonPanel.add(posButton11);

		posButton10.setAction(calAction);
		posButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dot_32.png"))); // NOI18N
		posButton10.setActionCommand(".");
		posButton10.setFocusable(false);
		calcButtonPanel.add(posButton10);

		posButton12.setAction(calAction);
		posButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_32.png"))); // NOI18N
		posButton12.setText("CLEAR");
		posButton12.setFocusable(false);
		calcButtonPanel.add(posButton12);

		add(calcButtonPanel, "cell 0 1,grow, gapy 0 20px");
		actionButtonPanel.setLayout(new MigLayout("", "[fill,grow][fill,grow]", "[][][][]"));
		actionButtonPanel.setBackground(new Color(209,222,235));
//		btnTaxExempt = new POSToggleButton(com.floreantpos.POSConstants.TAX_EXEMPT);
//		btnTaxExempt.setText("TAX EXEMPT");
//		btnTaxExempt.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				settleTicketView.doTaxExempt(btnTaxExempt.isSelected());
//			}
//		});


//		btnMyKalaDiscount = new PosButton();
//		btnMyKalaDiscount.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				settleTicketView.submitMyKalaDiscount();
//			}
//		});
//
//		btnMyKalaDiscount.setText("LOYALTY DISCOUNT");
//		jPanel4.add(btnMyKalaDiscount, "cell 1 0,growx");
		
		/*btnFinish = new com.floreantpos.swing.PosButton(POSConstants.PAY.toUpperCase());
		actionButtonPanel.add(btnFinish, "span 2, growx, wrap");
		btnFinish.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinish(evt);
			}
		});*/
		
		setBtnCash(new PosButton("BAR"));
		getBtnCash().setBackground(Color.GREEN);
		actionButtonPanel.add(getBtnCash());
		getBtnCash().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtnCash().setBackground(Color.GREEN);
				btnCard.setBackground(null);
				setPaymentType(PaymentType.CASH);
				doFinish();
			}
		});
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
			
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
		
		am.put("ESC", new ArrowAction("ESC"));
		am.put("ENTER", new ArrowAction("ENTER"));
		am.put("RIGHT", new ArrowAction("RIGHT"));
		
		
		btnCard = new PosButton("EC-KARTE");
		actionButtonPanel.add(btnCard,"wrap");
		btnCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCard.setBackground(Color.GREEN);
				setPaymentType(PaymentType.CARD);
				getBtnCash().setBackground(null);
				doFinish();
			}
		});
		btnCard.setBackground(null);
		btnOnline = new PosButton("ONLINE");
		actionButtonPanel.add(btnOnline);
		btnOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOnline.setBackground(Color.GREEN);
				btnCard.setBackground(null);
				setPaymentType(PaymentType.ONLINE);
				getBtnCash().setBackground(null);
				doFinish();
			}
		});
		btnOnline.setBackground(null);
		btnGratuity = new PosButton(com.floreantpos.POSConstants.ADD_GRATUITY_TEXT);
		//actionButtonPanel.add(btnGratuity, "growx");
		btnGratuity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSetGratuity();
			}
		});
		btnGratuity.setBackground(null);
		
		
		btnCoupon = new PosButton(com.floreantpos.POSConstants.COUPON_DISCOUNT);
		//actionButtonPanel.add(btnCoupon, "growx, wrap");
		btnCoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doApplyCoupon();
			}
		});
		btnCoupon.setBackground(null);
		btnViewCoupons = new PosButton(com.floreantpos.POSConstants.VIEW_DISCOUNTS);
		//actionButtonPanel.add(btnViewCoupons, "growx");
		btnViewCoupons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doViewDiscounts();
			}
		});
		btnViewCoupons.setBackground(null);
		
		
		btnCancel = new com.floreantpos.swing.PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			btnCancel.setText(POSConstants.ABBRECHEN);
		
		actionButtonPanel.add(btnCancel, "growx");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settleTicketView.dispose();
			}
		});
		btnCancel.setBackground(new Color(255,153,153));
		
		label = new JLabel("");
		add(label, "cell 0 2,growy");

		add(actionButtonPanel, "cell 0 4,grow");

		transparentPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 15, 15));
		transparentPanel1.setBackground(new Color(209,222,235));
		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel4.setText("GESAMT:");

		jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel6.setText("BAR:");
		
		lblCopies = new JLabel("DRUCK:");
		lblCopies.setFont(new java.awt.Font("Tahoma", 1, 12));
		cbCopies = new JComboBox();
		cbCopies.setBackground(Color.WHITE);
		cbCopies.setFont(new Font("Times New Roman",Font.BOLD, 16));
		for(int i = 1;i < 6;i++)
		{
			cbCopies.addItem(i);
		}
		tfDueAmount.setEditable(false);
		tfDueAmount.setBackground(new Color(255,204,229));
		tfDueAmount.setFont(new Font("Times New Roman", Font.BOLD, 26));
		tfDueAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		tfAmountTendered.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfAmountTendered.setFont(new Font("Times New Roman", Font.BOLD, 26));
		add(transparentPanel1, "cell 0 0,growx,aligny top");

		transparentPanel1.setLayout(new MigLayout("", "[][grow,fill]", "[19px][][19px]"));
		transparentPanel1.add(jLabel4, "cell 0 0,alignx right,aligny center");

		transparentPanel1.add(jLabel6, "cell 0 2,alignx left,aligny center");
		transparentPanel1.add(tfDueAmount, "cell 1 0,growx,aligny top");
		transparentPanel1.add(tfAmountTendered, "cell 1 2,growx,aligny top");
		
		transparentPanel1.add(lblCopies, "cell 0 3,growx,aligny center");
		transparentPanel1.add(cbCopies, "cell 1 3,growx,aligny top");
	}// </editor-fold>//GEN-END:initComponents

	protected void removeKalaId() {
		Ticket ticket = settleTicketView.getTicket();
		ticket.getProperties().remove(SettleTicketDialog.LOYALTY_ID);
		TicketDAO.getInstance().saveOrUpdate(ticket);

		POSMessageDialog.showMessage("Loyalty Id removed");
	}

	public void setPaymentType(PaymentType type)
	{
		paymentType = type;
	}
	public PaymentType getPaymentType()
	{
		return paymentType;
	}
	public void addMyKalaId() {
		String loyaltyid = JOptionPane.showInputDialog("Enter loyalty id:");

		if (StringUtils.isEmpty(loyaltyid)) {
			return;
		}

		Ticket ticket = settleTicketView.getTicket();
		ticket.addProperty(SettleTicketDialog.LOYALTY_ID, loyaltyid);
		TicketDAO.getInstance().saveOrUpdate(ticket);

		POSMessageDialog.showMessage("Loyalty id set.");
	}

	protected void doSetGratuity() {
		settleTicketView.doSetGratuity();
	}

	protected void doTaxExempt() {
	}

	private void doFinish() {//GEN-FIRST:event_doFinish
		settleTicketView.setCopies(cbCopies.getSelectedItem().toString());
		settleTicketView.doSettle();
		
	}//GEN-LAST:event_doFinish

	private void btnCancelActionPerformed() {//GEN-FIRST:event_btnCancelActionPerformed
		Ticket ticket = settleTicketView.getTicket();
		if(ticket.getCouponAndDiscounts().size() > 0)
		{
			POSMessageDialog.showError("Bitte loeschen Sie den Rabatt");
			return;
		}
		List<TicketItem> ticketItemList = ticket.getDeletedItems();
		if(ticketItemList != null)
		{
			JReportPrintService.printDeletedTicketToKitchen(ticket);
			ticket.clearDeletedItems();
		}
		OrderController.saveOrder(ticket);
		
		if (ticket.needsKitchenPrint()) {
			JReportPrintService.printTicketToKitchen(ticket);
		}
		OrderController.saveOrder(ticket);

		if(TerminalConfig.isTabVersion())
		{
			openTicket();
			createNewTicket();
			OrderView.getInstance().getCategoryView().initialize();
		}
		else
			RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
		settleTicketView.setCanceled(true);
		settleTicketView.dispose();
	}//GEN-LAST:event_btnCancelActionPerformed
	
	public void openTicket()
	{
		List<ShopTable> tables = new ArrayList();
		
	    ShopTable table = ShopTableDAO.getInstance().getByNumber("99");
		if(table == null)
		{
			table = new ShopTable();
			table.setNumber("99");
		    tables.add(table);
		}
		else
		 tables.add(table);
				
		Application application = Application.getInstance();
		
		Ticket ticket = new Ticket();
		
		if (tables.size() == 1)
		{
			for (ShopTable shopTable : tables) {
				if (shopTable.getNumber().compareTo("99") == 0)
					ticket.setType(TicketType.HOME_DELIVERY);
				else
				{
					shopTable.setOccupied(true);
					ticket.setType(TicketType.DINE_IN);
				}
				ticket.addTotables(shopTable);
				break;
			}
		}
		else
			ticket.setType(TicketType.DINE_IN);
		
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		
		ticket.setNumberOfGuests(2);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
		
		if (tables.size() != 1)
		{
			for (ShopTable shopTable : tables) {
			shopTable.setOccupied(true);
			ticket.addTotables(shopTable);
			}
		}

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	
	}
	
	public void createNewTicket()
	{
		try {
			OrderServiceExtension orderService = new DefaultOrderServiceExtension();
			orderService.createNewTicket(TicketType.DINE_IN);
			
			if(orderService.getSelectedTable() != 0)
			{
				String tableSel = PosGuiUtil.getSelectedTable()+"";
				List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();
				for(Iterator<Ticket> itr = ticketList.iterator(); itr.hasNext();)
				{
					Ticket ticket = itr.next();
					if(ticket.getType() == TicketType.DINE_IN && (!ticket.isPaid()))
					{
						if(ticket.getTableNumbers().compareTo(tableSel) == 0)
						{
							editTicket(ticket);
							break;
						}
					}
				}
			}
				
		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
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
	// End of variables declaration//GEN-END:variables

	Action calAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JTextField textField = tfAmountTendered;

			PosButton button = (PosButton) e.getSource();
			String s = button.getActionCommand();
			if (s.equals("CLEAR")) {
				textField.setText(ADD);
			}
			else if (s.equals(".")) {
				if (textField.getText().indexOf('.') < 0) {
					textField.setText(textField.getText() + ".");
				}
			}
			else {
				String string = textField.getText();
				int index = string.indexOf('.');
				if (index < 0) {
					double value = 0;
					try {
						value = Double.parseDouble(string);
					} catch (NumberFormatException x) {
						Toolkit.getDefaultToolkit().beep();
					}
					if (value == 0) {
						textField.setText(s);
					}
					else {
						textField.setText(string + s);
					}
				}
				else {
					textField.setText(string + s);
				}
			}
		}
	};
//	private PosButton btnMyKalaDiscount;

	public void updateView() {
//		btnTaxExempt.setEnabled(true);
//		btnTaxExempt.setSelected(settleTicketView.getTicket().isTaxExempt());

		double dueAmount = getDueAmount();
		System.out.println("Due amount: "+ NumberUtil.formatNumber(dueAmount));
		tfDueAmount.setText(dueAmount+"");
		tfAmountTendered.setText(dueAmount+"");
	}

	public double getTenderedAmount() throws ParseException {
		NumberFormat numberFormat = NumberFormat.getInstance();
		System.out.println("Getting: "+ tfAmountTendered.getText());
		double doubleValue = numberFormat.parse(tfAmountTendered.getText().replace('.', ',')).doubleValue();
		return doubleValue;
	}

	public SettleTicketDialog getSettleTicketView() {
		return settleTicketView;
	}

	public void setSettleTicketView(SettleTicketDialog settleTicketView) {
		this.settleTicketView = settleTicketView;
	}

	protected double getPaidAmount() {
		return settleTicketView.getTicket().getPaidAmount();
	}

	protected double getDueAmount() {
		return settleTicketView.getTicket().getDueAmount();
	}

	protected double getAdvanceAmount() {
		Gratuity gratuity = settleTicketView.getTicket().getGratuity();
		return gratuity != null ? gratuity.getAmount() : 0;
	}

	protected double getTotalGratuity() {
		return settleTicketView.getTicket().getPaidAmount();
	}

	public void setDefaultFocus() {
		tfAmountTendered.requestFocus();
	}

	public PosButton getBtnCash() {
		return btnCash;
	}

	public void setBtnCash(PosButton btnCash) {
		this.btnCash = btnCash;
	}

}
