package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.GutscheinDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;

public class DirectDiscountDialog extends POSDialog {

	private com.floreantpos.ui.views.NumberSelectionView numberSelectionView;

	private List<Gutschein> gutscheinList;

	private JTable table;

	private GutscheinExplorerTableModel tableModel;

	private Ticket ticket;

	private JCheckBox cbExternalDiscount = new JCheckBox("Wert-Bon/Sodexo");

	private Integer gutscheinId = 0;

	private JTextField tfBarcode = new JTextField();

	private JButton selectButton;

	private PosButton deleteRabat;
	private PosButton deleteGutschein;

	private PosButton okButton;

	private PosButton cancelButton;

	public DirectDiscountDialog(Ticket ticket) {
		this.ticket = ticket;
		initComponents();
	}

	public void initComponents() {
		getContentPane().setLayout(new BorderLayout(1,1));
		getContentPane().setBackground(new Color(35,35,36));
		numberSelectionView = new com.floreantpos.ui.views.NumberSelectionView();
		numberSelectionView.setTitle("Rabatt Preis");
		JPanel numberPanel = new JPanel();
		numberPanel.setLayout(new BorderLayout());
		JPanel discountTextPanel = new JPanel();
		discountTextPanel.setLayout(new MigLayout("", "[][grow][]",""));

		JTextField tfDirectDiscount = new JTextField();
		tfDirectDiscount.setEditable(false);
		JLabel lblCurrentDiscount = new JLabel("Aktuelle Rabatte");
		if(StringUtils.isNotEmpty(POSConstants.Aktuelle_Rabatte))
			lblCurrentDiscount.setText(POSConstants.Aktuelle_Rabatte);
		
		JLabel lblEuros = new JLabel("€");

		lblCurrentDiscount.setBackground(new Color(35,35,36));
		lblCurrentDiscount.setForeground(Color.WHITE);
		lblEuros.setBackground(new Color(35,35,36));
		lblEuros.setForeground(Color.WHITE);

		tfDirectDiscount.setBackground(new Color(35,35,36));
		tfDirectDiscount.setForeground(Color.WHITE);

		lblCurrentDiscount.setFont(new Font(null, Font.BOLD, 20));
		tfDirectDiscount.setFont(new Font(null, Font.BOLD, 20));

		discountTextPanel.add(lblCurrentDiscount);
		discountTextPanel.add(tfDirectDiscount, "growx");
		discountTextPanel.add(lblEuros);
		discountTextPanel.setBackground(new Color(35,35,36));

		List<TicketCouponAndDiscount> couponAndDiscounts = ticket.getCouponAndDiscounts();
		Double discountedValue = 0.0;

		if(couponAndDiscounts != null && couponAndDiscounts.size() > 0) {
			for(TicketCouponAndDiscount ticketCouponAndDiscount:couponAndDiscounts)
				if(ticketCouponAndDiscount != null&&ticketCouponAndDiscount.getType()==CouponAndDiscount.DIRECT_RABATT||
				ticketCouponAndDiscount != null&&ticketCouponAndDiscount.getType()==CouponAndDiscount.PERCENTAGE_PER_ITEM||
				ticketCouponAndDiscount != null&&ticketCouponAndDiscount.getType()==CouponAndDiscount.PERCENTAGE_PER_ORDER) {				
					discountedValue += ticketCouponAndDiscount.getUsedValue();							
				}
		}

		if(ticket.getGutschrift() != null&&ticket.getGutschrift()>0.0) {
			discountedValue += ticket.getGutschrift();			
		}
		tfDirectDiscount.setText(NumberUtil.formatNumber(discountedValue));		
		
		if(StringUtils.isNotEmpty(POSConstants.Wertbon))
			cbExternalDiscount.setText(POSConstants.Wertbon);
		  
		cbExternalDiscount.setBackground(new Color(35,35,36));
		cbExternalDiscount.setForeground(Color.WHITE);
		cbExternalDiscount.setFont(new Font(null, Font.BOLD, 28));
		numberPanel.add(discountTextPanel, BorderLayout.NORTH);
		numberPanel.add(numberSelectionView, BorderLayout.CENTER);
		numberPanel.add(cbExternalDiscount, BorderLayout.SOUTH);
		numberPanel.setBackground(new Color(35,35,36));

		getContentPane().add(numberPanel, BorderLayout.WEST);
		gutscheinList = GutscheinDAO.getInstance().findOpenGutscheins();

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout(1,1));

		tableModel = new GutscheinExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(Color.GRAY);
		table.setRowHeight(60);
		table.setBackground(new Color(35, 35, 36));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent event) {
				if (event.getClickCount() == 2) {
					performOkAction();
				}
			}
		});

		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getViewport().setBackground(new Color(35,35,36));
		centerPanel.add(jScrollPane, BorderLayout.CENTER);
		centerPanel.setBackground(new Color(35,35,36));

		if(couponAndDiscounts!= null && couponAndDiscounts.size() > 0) {
			boolean first = true;
			for(TicketCouponAndDiscount ticketCouponAndDiscount:couponAndDiscounts) {
				if(ticketCouponAndDiscount.getType()==CouponAndDiscount.GUTSCHEIN) {
					Integer gutschein = ticketCouponAndDiscount.getGutschein();
					if(gutschein != null) {
						if(first) {
							gutscheinList.clear();
							first = false;
						}
						gutscheinList.add(GutscheinDAO.getInstance().get(gutschein));					
					}
				}
			}

			table.repaint();
			table.revalidate();
			if(!first)
				table.setRowSelectionInterval(0, 0);
		}

		JPanel bottomPanel =new JPanel();
		bottomPanel.setBackground(new Color(35,35,36));

		deleteRabat = new PosButton("Dir-Rbt. entf.");
		if(StringUtils.isNotEmpty(POSConstants.Dir_Rbt_entf))
			deleteRabat.setText(POSConstants.Dir_Rbt_entf);
		
		deleteRabat.setBackground(new Color(125,6,42));
		deleteRabat.setForeground(Color.WHITE);

		deleteGutschein = new PosButton("Guts. entf.");
		if(StringUtils.isNotEmpty(POSConstants.Guts_entf))
			deleteGutschein.setText(POSConstants.Guts_entf);
		
		deleteGutschein.setBackground(new Color(125,6,42));
		deleteGutschein.setForeground(Color.WHITE);

		selectButton = new PosButton("Waehlen");
		if(StringUtils.isNotEmpty(POSConstants.Waehlen_dirRbt))
			selectButton.setText(POSConstants.Waehlen_dirRbt);
		
		selectButton.setBackground(new Color(2, 64, 2));
		selectButton.setForeground(Color.WHITE);

		tfBarcode.setText("Barcode scan");
		tfBarcode.setFont(new Font(null, Font.PLAIN, 18));
		tfBarcode.setBackground(new Color(35,35,35));
		tfBarcode.setForeground(Color.WHITE);
		tfBarcode.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(tfBarcode.getText().equals("Barcode scan")) {
					tfBarcode.setText("");
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		tfBarcode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gutscheinList.clear();
				gutscheinList.add(GutscheinDAO.getInstance().findByBarcode(tfBarcode.getText()));  
				table.repaint();
				table.revalidate();
			}

		});

		deleteRabat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(cbExternalDiscount.isSelected()) {
					if(getValue()==0)
						ticket.setGutschrift(0.00);
					else
						ticket.setGutschrift(ticket.getGutschrift()-getValue());

					if(StringUtils.isNotEmpty(POSConstants.Wertbon_entf))
						POSMessageDialog.showError(POSConstants.Wertbon_entf);
					else 
						POSMessageDialog.showError("Wertbon/Sodexo entfernt");   
				}else {
					List<TicketCouponAndDiscount> couponAndDiscounts = ticket
							.getCouponAndDiscounts();
					List<TicketCouponAndDiscount> couponDisNew = null;
					Double value = 0.0;
					if (couponAndDiscounts != null && couponAndDiscounts.size() > 0) {
						for(TicketCouponAndDiscount discount :couponAndDiscounts) {
							if(discount.getType()==CouponAndDiscount.DIRECT_RABATT) {
								value = discount.getUsedValue()/ticket.getPositiveSubtotal(TerminalConfig.isIncludeRabattModifier());								
							} else {
								if(couponDisNew==null)
									couponDisNew = new ArrayList<TicketCouponAndDiscount>();
								couponDisNew.add(discount);
							}
							ticket.setCouponAndDiscounts(couponDisNew);
						}

						if(value>0)
							ticket.calculatePrice(value, true,CouponAndDiscount.PERCENTAGE_PER_ITEM);
						
					}

					POSMessageDialog.showError("Rabatt entfernt");  
					if(StringUtils.isNotEmpty(POSConstants.Wertbon_entf))
						POSMessageDialog.showError(POSConstants.Wertbon_entf);
					
					gutscheinList = GutscheinDAO.getInstance().findOpenGutscheins();
					table.repaint();
					table.revalidate();
					tfDirectDiscount.setText("0,00");
				}

				OrderController.saveOrder(ticket);
				setCanceled(true);
				dispose();
			}
		});


		deleteGutschein.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(cbExternalDiscount.isSelected()) {
					if(getValue()==0)
						ticket.setGutschrift(0.00);
					else
						ticket.setGutschrift(ticket.getGutschrift()-getValue());
					
					if(StringUtils.isNotEmpty(POSConstants.Wertbon_entf))
						POSMessageDialog.showError(POSConstants.Wertbon_entf);
					else
						POSMessageDialog.showError("Wertbon/Sodexo entfernt");   
					
					
				}else {
					List<TicketCouponAndDiscount> couponAndDiscounts = ticket
							.getCouponAndDiscounts();
					List<TicketCouponAndDiscount> couponDisNew = null;
					if (couponAndDiscounts != null && couponAndDiscounts.size() > 0) {
						for(TicketCouponAndDiscount discount :couponAndDiscounts) {
							if(discount.getType()==CouponAndDiscount.GUTSCHEIN) {
								Integer gutschein = discount.getGutschein();
								int selectedRow = table.getSelectedRow();
								int id =0; 
								if (selectedRow < 0) {
									return;
								}
								Gutschein gutschein1 = gutscheinList.get(selectedRow);
								id = gutschein1.getId();
								if(gutschein != null && gutschein > 0&&gutschein==id) {									
									Gutschein dbGutschein = GutscheinDAO.getInstance().get(gutschein);
									double discountAmnt = ticket.getDiscountAmount();
									double discountAmnt1 = ticket.calculateDiscountFromType(discount, ticket.getTotalAmount()+discountAmnt);
									ticket.setDiscountAmount(discountAmnt-discountAmnt1);
									ticket.setTotalAmount(ticket.getTotalAmount()+dbGutschein.getValue()-ticket.getTipAmount());
									ticket.setTipAmount(0.0);
									dbGutschein.setClosed(false);
									if(dbGutschein.isSplitted()) {
										dbGutschein.setValue(dbGutschein.getValue() + dbGutschein.getSplittedAmount());
										dbGutschein.setSplitted(false);
										dbGutschein.setSplittedAmount(0.00);
									}
									GutscheinDAO.getInstance().saveOrUpdate(dbGutschein);									
								}else {
									if(couponDisNew==null)
										couponDisNew = new ArrayList<TicketCouponAndDiscount>();
									couponDisNew.add(discount);
								}
							}
							else {
								if(couponDisNew==null)
									couponDisNew = new ArrayList<TicketCouponAndDiscount>();
								couponDisNew.add(discount);
							}
							ticket.setCouponAndDiscounts(couponDisNew);
						}
					}
					
					if(StringUtils.isNotEmpty(POSConstants.Rabatt_entfernt))
						POSMessageDialog.showError(POSConstants.Rabatt_entfernt);
					else
					    POSMessageDialog.showError("Rabatt entfernt");  
					
					gutscheinList = GutscheinDAO.getInstance().findOpenGutscheins();
					table.repaint();
					table.revalidate();
					tfDirectDiscount.setText("0,00");
				}


				OrderController.saveOrder(ticket);
				setCanceled(true);
				dispose();
			}
		});


		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {			

				if(tfBarcode.getText()!=null&&StringUtils.isNumeric(tfBarcode.getText())) {
					gutscheinList.clear();
					gutscheinList.add(GutscheinDAO.getInstance().findByBarcode(tfBarcode.getText()));   
					tfBarcode.setText("");
					table.repaint();
					table.revalidate();
					performOkAction();
				} else {
					int selectedRow = table.getSelectedRow();
					int id =0; 
					if (selectedRow > 0) {
						Gutschein gutschein = gutscheinList.get(selectedRow);
						id = gutschein.getId();
						performOkAction();
					}					
				}
			}
		});
		bottomPanel.setLayout(new MigLayout("", "[grow][][]", ""));
		bottomPanel.add(tfBarcode, "growx");
		bottomPanel.add(selectButton);

		centerPanel.add(bottomPanel, BorderLayout.SOUTH);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setBackground(new Color(35,35,36));
		southPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
		okButton = new PosButton("Fertig");
		if(StringUtils.isNotEmpty(POSConstants.Fertig))
			okButton.setText(POSConstants.Fertig);
		
		okButton.setForeground(Color.WHITE);
		okButton.setBackground(new Color(2, 64, 2));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(existCoupon(ticket)) {
					if(StringUtils.isNotEmpty(POSConstants.Ticket_bereits_Rabatt))
						POSMessageDialog.showError(POSConstants.Ticket_bereits_Rabatt);
					else
					    POSMessageDialog.showError("Dieses Ticket hat bereits einen Rabatt");
				
					return;
				}  

				setCanceled(false);
				dispose();
			}
		});

		cancelButton = new PosButton("Abbrechen");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		 
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(125, 6, 42));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		PosButton btnOnscreenKeyboard = new PosButton();
		btnOnscreenKeyboard.setText("Keyboard");
		btnOnscreenKeyboard.setFont(new Font("Tahoma", Font.BOLD, 16));
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

		southPanel.add(okButton);
		southPanel.add(cancelButton);
		southPanel.add(deleteRabat);
		southPanel.add(deleteGutschein);
		southPanel.add(btnOnscreenKeyboard);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
	}

	public boolean isExternalRabatt() {
		return cbExternalDiscount.isSelected();  
	}

	public boolean existCoupon(Ticket ticket) {
		boolean exist = false;
		List<TicketCouponAndDiscount> couponList =ticket.getCouponAndDiscounts();
		if(couponList!=null&&couponList.size()>0)
		{
			for(TicketCouponAndDiscount discount:couponList) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow > 0) {
					Gutschein gutschein = gutscheinList.get(selectedRow);
					gutscheinId = gutschein.getId();

				}
				if(discount.getType()==(CouponAndDiscount.DIRECT_RABATT)&&gutscheinId==0&&!cbExternalDiscount.isSelected()) {				
					exist =true;
					break;
				}
			}
		}	

		return exist;
	}



	public boolean gutscheinExist(Ticket ticket) {
		boolean exist = false;
		List<TicketCouponAndDiscount> couponList =ticket.getCouponAndDiscounts();
		if(couponList!=null&&couponList.size()>0)
		{
			for(TicketCouponAndDiscount discount:couponList) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow > 0) {
					Gutschein gutschein = gutscheinList.get(selectedRow);
					gutscheinId = gutschein.getId();

				}
				if(discount.getType()==(CouponAndDiscount.GUTSCHEIN)) {				
					exist =true;
					break;
				}
			}
		}	

		return exist;
	}


	public void performOkAction() {
		if(existCoupon(ticket)) {
			if(StringUtils.isNotEmpty(POSConstants.Ticket_bereits_Rabatt))
				POSMessageDialog.showError(POSConstants.Ticket_bereits_Rabatt);
			else
			    POSMessageDialog.showError("Dieses Ticket hat bereits einen Rabatt");
			
			return;
		}

		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return;
		}
		Gutschein gutschein = gutscheinList.get(selectedRow);
		gutscheinId = gutschein.getId();

		numberSelectionView.setValue(gutschein.getValue()*100);

		Double gutscheinValue = gutschein.getValue();
		ticket.setChangesAvailable(true);

		Double trinkGeld = ticket.getTipAmount();
		Double ticketAmount = ticket.getTotalAmount()+trinkGeld;
		if(gutscheinValue > ticketAmount) {
			boolean yesNo=false;
			
			if(!gutscheinExist(ticket)&&trinkGeld==0.0) {
				if(StringUtils.isNotEmpty(POSConstants.Restbetrag))
				     yesNo=POSMessageDialog.showYesNo(POSConstants.Restbetrag);
				else
					 yesNo=POSMessageDialog.showYesNo("Restbetrag als Trinkgeld?");
				
				if(yesNo) {
				Double difference = gutscheinValue - ticketAmount;
				gutschein.setSplitted(false);
				GutscheinDAO.getInstance().saveOrUpdate(gutschein);
				ticket.setTipAmount(difference);
				numberSelectionView.setValue((difference+ticketAmount)*100);
				}
			} else {
				Double difference = gutscheinValue - ticketAmount;
				gutschein.setValue(difference);
				gutschein.setSplitted(true);
				gutschein.setSplittedAmount(ticketAmount);
				numberSelectionView.setValue(ticketAmount*100);
			}
				
			GutscheinDAO.getInstance().saveOrUpdate(gutschein);
		} else {
			gutschein.setSplitted(false);
			gutschein.setClosed(true);
			
			GutscheinDAO.getInstance().saveOrUpdate(gutschein);
		}  
		setCanceled(false);
		dispose();
	}

	public double getValue() {
		return Double.valueOf(numberSelectionView.getValue());
	}

	public Integer getGutscheinId() {
		if(gutscheinId == null) {
			return 0;
		}
		return gutscheinId;  
	}

	class GutscheinExplorerTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		String[] columnNames = { POSConstants.ID, POSConstants.Preis_EUR, "Barcode",
				POSConstants.Gueltig_bis, POSConstants.Unendlich, "Status" };

		@Override
		public int getRowCount() {
			if (gutscheinList == null) {
				return 0;
			}
			return gutscheinList.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (gutscheinList == null||gutscheinList.isEmpty())
				return ""; //$NON-NLS-1$

			Gutschein gutschein = gutscheinList.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return String.valueOf(gutschein.getId());

			case 1:
				return NumberUtil.formatNumber(gutschein.getValue());

			case 2:
				return gutschein.getBarcode();

			case 3:
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
				return df.format(gutschein.getExpiryDate());

			case 4:
				return gutschein.isUnlimited();

			case 5:
				if (gutschein.isClosed() != null && gutschein.isClosed()) {
					if(StringUtils.isNotEmpty(POSConstants.Abgeschlossen))
						return POSConstants.Abgeschlossen;
					else
					    return "Abgeschlossen";
				} else {
					if(StringUtils.isNotEmpty(POSConstants.Offnen_dirRbt))
						return POSConstants.Offnen_dirRbt;
					else
					    return "Offnen";
				}
			}

			return null;
		}
	}
}
