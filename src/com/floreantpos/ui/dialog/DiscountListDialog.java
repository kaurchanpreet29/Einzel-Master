package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DiscountListDialog extends POSDialog implements ActionListener {
    private JPanel contentPane;
    private PosButton buttonCancel;
    private PosButton btnScrollUp;
    private PosButton btnScrollDown;
    private PosButton btnDeleteSelected;
    private JTable tableDiscounts;

    private List<Ticket> tickets;
    private DiscountViewTableModel discountViewTableModel;
    private DefaultListSelectionModel selectionModel;
    private TicketCouponAndDiscount discountCoupon;
    private boolean modified = false;
    private boolean delete = false;

    public boolean isDelete() {
		return delete;
	}

	public DiscountListDialog(List<Ticket> tickets) {
        super(Application.getPosWindow(), true, false);

        this.tickets = tickets;

        setSize(700, 500);
        discountViewTableModel = new DiscountViewTableModel();
        tableDiscounts.setModel(discountViewTableModel);
        tableDiscounts.setBackground(Color.WHITE);
        tableDiscounts.getTableHeader().setBackground(new Color(209,222,235));
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDiscounts.setSelectionModel(selectionModel);
        tableDiscounts.setRowHeight(100);
        tableDiscounts.setRowSelectionInterval(0, 0);
        btnScrollUp.setActionCommand("scrollUP");
        btnScrollDown.setActionCommand("scrollDown");
        btnScrollUp.addActionListener(this);
        btnScrollDown.addActionListener(this);

        setContentPane(contentPane);
        setModal(true);
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        buttonCancel.setBackground(new Color(255,153,153));
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnDeleteSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doDeleteSelection();
            }
        });
        btnDeleteSelected.setBackground(new Color(255,153,153));
        getContentPane().setBackground(new Color(209,222,235));
        contentPane.setBackground(new Color(209,222,235));
    }
    
    private void doDeleteSelection() {
    	
        try {
            int selectedRow = selectionModel.getLeadSelectionIndex();
            if (selectedRow < 0) {
                POSMessageDialog.showError(this, com.floreantpos.POSConstants.SELECT_ITEM_TO_DELETE);
                return;
            }
            if (ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.YES) {
                return;
            }
            delete = true;
            Object object = discountViewTableModel.get(selectedRow);
            modified = discountViewTableModel.delete((TicketDiscount) object);
            onOK();
        } catch (Exception e) {
            POSMessageDialog.showError(this, "An error occured while delete.", e);
        }
    }

    private void onOK() {
        tickets = null;
        setCanceled(false);
        dispose();
    }

    private void onCancel() {
        tickets = null;
        setCanceled(true);
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnDeleteSelected = new PosButton();
        btnDeleteSelected.setIcon(new ImageIcon(getClass().getResource("/images/delete_32.png")));
        btnDeleteSelected.setPreferredSize(new Dimension(200, 50));
        btnDeleteSelected.setText("Loeschen");
        panel2.setBackground(new Color(209,222,235));
        panel2.add(btnDeleteSelected);
        
        buttonCancel = new PosButton();
        buttonCancel.setIcon(new ImageIcon(getClass().getResource("/images/cancel_32.png")));
        buttonCancel.setPreferredSize(new Dimension(200, 50));
        buttonCancel.setText(com.floreantpos.POSConstants.CANCEL);
        panel2.add(buttonCancel);
        final JPanel panel3 = new JPanel();
        panel3.setBackground(new Color(209,222,235));
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(458, 310), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(Color.WHITE);
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableDiscounts = new JTable();
        tableDiscounts.setBackground(Color.WHITE);
        tableDiscounts.setOpaque(true);
        scrollPane1.setViewportView(tableDiscounts);
        scrollPane1.getViewport().setBackground(Color.WHITE);
        final JPanel panel4 = new JPanel();
        panel4.setBackground(new Color(209,222,235));
        panel4.setLayout(new FormLayout("fill:p:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow"));
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnScrollUp = new PosButton();
        btnScrollUp.setIcon(new ImageIcon(getClass().getResource("/images/up_32.png")));
        btnScrollUp.setPreferredSize(new Dimension(50, 50));
        btnScrollUp.setText("");
        CellConstraints cc = new CellConstraints();
        panel4.add(btnScrollUp, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.BOTTOM));
        btnScrollDown = new PosButton();
        btnScrollDown.setIcon(new ImageIcon(getClass().getResource("/images/down_32.png")));
        btnScrollDown.setPreferredSize(new Dimension(50, 50));
        btnScrollDown.setText("");
        panel4.add(btnScrollDown, cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.TOP));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    class DiscountViewTableModel extends AbstractTableModel {
        String[] columnNames = {"Name", "Rabatt", "Gesamt"};
        ArrayList rows = new ArrayList();

        DiscountViewTableModel() {
            for (Iterator iter = tickets.iterator(); iter.hasNext();) {
                Ticket ticket = (Ticket) iter.next();
                List<TicketCouponAndDiscount> coupons = ticket.getCouponAndDiscounts();

                if (coupons != null) {
                    for (TicketCouponAndDiscount coupon : coupons) {
                        TicketDiscount ticketDiscount = new TicketDiscount(ticket, coupon);
                        rows.add(ticketDiscount);
                    }
                }

            }

        }

        public int getRowCount() {
            return rows.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            TicketDiscount ticketDiscount = (TicketDiscount) rows.get(rowIndex);
            Object discountObject = ticketDiscount.getDiscountObject();

            switch (columnIndex) {
                case 0:
                    if (discountObject instanceof TicketCouponAndDiscount) {
                        return ((TicketCouponAndDiscount) discountObject).getName();
                    }
                    return null;

                case 1:
                    if (discountObject instanceof TicketCouponAndDiscount) {
                        return CouponAndDiscount.COUPON_TYPE_NAMES[((TicketCouponAndDiscount) discountObject).getType()];
                    }
                    return null;

                case 2:
                    if (discountObject instanceof TicketCouponAndDiscount) {
                        return NumberUtil.formatNumber(((TicketCouponAndDiscount) discountObject).getValue());
                    }
                    return null;
            }

            return null;
        }

        public boolean delete(TicketDiscount ticketDiscount) {
            Ticket ticket = ticketDiscount.getTicket();
            Object object = ticketDiscount.getDiscountObject();

            if (object instanceof TicketCouponAndDiscount) {
//            	setDiscountCoupon((TicketCouponAndDiscount)object);
//                boolean b = ticket.getCouponAndDiscounts().remove(object);
//                rows.remove(ticketDiscount);
//                fireTableDataChanged();
                return true;
            }
            return false;
        }

        public Object get(int index) {
            return rows.get(index);
        }
    }

    double value;
    
    public double getValue() {
		return value;
	}

	public void actionPerformed(ActionEvent e) {
        if ("scrollUP".equals(e.getActionCommand())) {

            int selectedRow = selectionModel.getLeadSelectionIndex();

            if (selectedRow <= 0) {
                selectedRow = 0;
            } else {
                --selectedRow;
            }

            selectionModel.setLeadSelectionIndex(selectedRow);
            Rectangle cellRect = tableDiscounts.getCellRect(selectedRow, 0, false);
            tableDiscounts.scrollRectToVisible(cellRect);
        } else if ("scrollDown".equals(e.getActionCommand())) {
            int selectedRow = selectionModel.getLeadSelectionIndex();

            if (selectedRow < 0) {
                selectedRow = 0;
            } else if (selectedRow >= discountViewTableModel.getRowCount() - 1) {
                //return;
            } else {
                ++selectedRow;
            }

            selectionModel.setLeadSelectionIndex(selectedRow);
            Rectangle cellRect = tableDiscounts.getCellRect(selectedRow, 0, false);
            tableDiscounts.scrollRectToVisible(cellRect);
        }
    }

    public boolean isModified() {
        return modified;
    }

    class TicketDiscount {
        private Ticket ticket;
        private Object discountObject;

        public TicketDiscount() {

        }

        public TicketDiscount(Ticket ticket, Object discount) {
            this.ticket = ticket;
            this.discountObject = discount;
        }

        public Object getDiscountObject() {
            return discountObject;
        }

        public void setDiscountObject(Object discount) {
            this.discountObject = discount;
        }

        public Ticket getTicket() {
            return ticket;
        }

        public void setTicket(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TicketDiscount)) {
                return false;
            }

            TicketDiscount other = (TicketDiscount) obj;
            return this.discountObject.equals(other.discountObject);
        }
    }
    public void setDiscountCoupon(TicketCouponAndDiscount coupon)
    {
    	this.discountCoupon = coupon;
    }
    public TicketCouponAndDiscount getDiscountCoupon()
    {
    	return discountCoupon;
    }
 }
