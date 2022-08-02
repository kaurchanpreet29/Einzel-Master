package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.ui.OnlineConfigurationView;
import com.floreantpos.config.ui.OnlineRestaurantConfigurationView2;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class OnlineConfigurationDialog2 extends POSDialog implements ChangeListener, ActionListener {
	private static final String OK = com.floreantpos.POSConstants.OK;
	private static final String CANCEL = com.floreantpos.POSConstants.CANCEL;
	private static JTabbedPane tabbedPane = new JTabbedPane();
	private List<OnlineConfigurationView> views = new ArrayList<OnlineConfigurationView>();
	
	
	public OnlineConfigurationDialog2(Frame parent) throws IOException {
		super(parent, false);
		setTitle(Messages.getString("CONFIGURATION_WINDOW_TITLE")); //$NON-NLS-1$
		setBackground(new Color(209,222,235));
		setLayout(new MigLayout("fill", "", "[fill,grow][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tabbedPane.removeAll();
		add(tabbedPane, "span, grow" ); //$NON-NLS-1$
		OnlineRestaurantConfigurationView2 onlineRestaurantConfigurationView = new OnlineRestaurantConfigurationView2();
		onlineRestaurantConfigurationView.setTabbedPane(tabbedPane, 0, new Color(209,222,235));
		addView(onlineRestaurantConfigurationView);
		
		OnlineView2 onlineView = new OnlineView2(); 
		addView(onlineView);
		
		OnlineTextView2 onlineTextView = new OnlineTextView2();
		addView(onlineTextView);
		OnlinePaymentView2 onlinePaymentView =new OnlinePaymentView2();
		addView(onlinePaymentView);
		addView(new OnlineDayView2());
		
		tabbedPane.addChangeListener(this);
		tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 14));
		tabbedPane.setBackground(new Color(160,160,160));
		getContentPane().setBackground(new Color(209,222,235));
		JPanel bottomPanel = new JPanel(new MigLayout("fillx")); //$NON-NLS-1$
		JButton btnOk = new JButton("Speichern");
		btnOk.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnOk.setBackground(new Color(102,255,102));
		btnOk.addActionListener(this);
		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(this);
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		bottomPanel.add(btnCancel, "dock east, gaptop 5"); //$NON-NLS-1$
		bottomPanel.add(btnOk, "dock east, gapright 5,gaptop 5"); //$NON-NLS-1$
		bottomPanel.setBackground(new Color(209,222,235));
		add(bottomPanel, "newline,growx, gaptop 10"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public void addView(OnlineConfigurationView view) {
			tabbedPane.addTab(view.getName(), view);
			views.add(view);
	}
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
		if(b) {
			stateChanged(null);
		}
	}

	public void stateChanged(ChangeEvent e) {
		OnlineConfigurationView view = (OnlineConfigurationView) tabbedPane.getSelectedComponent();
		if(view == null)return;
		if(!view.isInitialized()) {
			try {
				view.initialize();
			} catch (Exception e1) {
				POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e1);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if("Speichern".equalsIgnoreCase(e.getActionCommand())) {
			try {
				for (OnlineConfigurationView view : views) {
					if(view.isInitialized())
						view.save();
				}
				setCanceled(false);
				dispose();
			} catch (PosException x) {
				POSMessageDialog.showError(this, x.getMessage());
			} catch (Exception x) {
				POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, x);
			}
		}
		else if("Abbrechen".equalsIgnoreCase(e.getActionCommand())) {
			setCanceled(true);
			dispose();
		}
	}

}
