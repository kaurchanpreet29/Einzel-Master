package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ConfigurationDialog extends POSDialog implements ChangeListener, ActionListener {
	private static final String OK = com.floreantpos.POSConstants.OK;
	private static final String CANCEL = com.floreantpos.POSConstants.CANCEL;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private List<ConfigurationView> views = new ArrayList<ConfigurationView>();
	
	
	public ConfigurationDialog(Frame parent) throws IOException {
		super(parent, false);
		setTitle(Messages.getString("CONFIGURATION_WINDOW_TITLE")); //$NON-NLS-1$
		setBackground(new Color(209,222,235));
		setLayout(new MigLayout("fill", "", "[fill,grow][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$		
		
		getContentPane().setBackground(new Color(209,222,235));
		RestaurantConfigurationView restaurantConfiguration = new RestaurantConfigurationView();
		restaurantConfiguration.setTabbedPane(tabbedPane, 0, new Color(209,222,235));
		addView(restaurantConfiguration);
		addView(new TerminalConfigurationView());
		addView(new PrintConfigurationView());
		addView(new TaxConfigurationView());
		addView(new DatabaseConfigurationView());
		addView(new DeleteAllView());
		tabbedPane.addChangeListener(this);
		tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 14));
		JPanel bottomPanel = new JPanel(new MigLayout("fillx")); //$NON-NLS-1$
		
		JButton btnOk = new JButton(CANCEL);
		btnOk.setBackground(new Color(255,153,153));
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnOk.addActionListener(this);
		bottomPanel.setBackground(new Color(209,222,235));
		JButton btnCancel = new JButton(OK);
		btnCancel.addActionListener(this);
		btnCancel.setBackground(new Color(102,255,102));
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 16));
		CC componentConstraints = new CC();
		componentConstraints.alignX("center").spanX();
		bottomPanel.add(btnCancel, "dock west, gapright 5, gaptop 5"); //$NON-NLS-1$
		bottomPanel.add(btnOk, "dock west, gaptop 5"); //$NON-NLS-1$

		
		add(bottomPanel, componentConstraints); //$NON-NLS-1$
		add(tabbedPane, "newline,growx, gaptop 5"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		FloorLayoutPlugin floorLayoutPlugin = Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
		if(floorLayoutPlugin != null) {
			floorLayoutPlugin.initConfigurationView(this);
		}
	}
	
	public void addView(ConfigurationView view) {
		tabbedPane.addTab(view.getName(), view);
		tabbedPane.setBackground(new Color(209,222,235));
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
		ConfigurationView view = (ConfigurationView) tabbedPane.getSelectedComponent();
		if(!view.isInitialized()) {
			try {
				view.initialize();
			} catch (Exception e1) {
				POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e1);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(OK.equalsIgnoreCase(e.getActionCommand())) {
			try {
				for (ConfigurationView view : views) {
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
		else if(CANCEL.equalsIgnoreCase(e.getActionCommand())) {
			setCanceled(true);
			dispose();
		}
	}

}
