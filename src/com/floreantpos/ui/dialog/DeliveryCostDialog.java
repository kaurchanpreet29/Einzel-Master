package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.floreantpos.main.Application;
import com.floreantpos.model.Gebiet;
import com.floreantpos.model.Street;
import com.floreantpos.model.StreetDB;
import com.floreantpos.model.dao.StreetDAO;

import net.miginfocom.swing.MigLayout;
public class DeliveryCostDialog extends POSDialog{

	JPanel panel;
	JTextField tfId;
	JComboBox cbOpen;
	JTextField tfPlz;
	JTextField tfBezirk;
	JTextField tfOrt;
	JTextField tfmindestbestellwert;
	JTextField tflieferkosten;
	JTextField tflieferzeit;
	JButton saveButton;
	JButton cancelButton;
	Gebiet gebiet;
	JPanel panel_5;
	private JList streetList;
	private JScrollPane scrollPane;
	private DefaultListModel listModel;
	List<Street> tempList;
	public DeliveryCostDialog(int id)
	{
		setBackground(new Color(209,222,235));
		tempList = new ArrayList();	
		setLayout(new MigLayout("", "[][][][][][grow]", "[19px][][][][][grow]"));
		panel_5 = new JPanel(new GridLayout());
	    listModel = new DefaultListModel();
		
		streetList = new JList(listModel);
		streetList.setFont(new Font("Times New Roman",Font.BOLD,18));
		streetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		streetList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent lse) {
	          	 if (streetList.getSelectedIndex() != -1){
	          		 if (streetList.getSelectedValue() != null)
	          		 {
	          			 tfPlz.setText("");
	          			 searchAndAdd(streetList.getSelectedValue().toString());
	          		 }
	          	 }
	         }
	      });
	    
		scrollPane = new JScrollPane(streetList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBackground(new Color(209,222,235));
	   	panel_5.add(scrollPane);
	   	panel_5.setBackground(new Color(209,222,235));
	   	panel_5.setPreferredSize(new Dimension(250,500));
		add(panel_5,BorderLayout.WEST);
		setTitle("Neue LieferungKosten");
		populateAllStreetList();
		populateList();
 		revalidate();
		repaint();
	
		panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		
		JLabel lblId = new JLabel("Id");
		panel.add(lblId);
		tfId = new JTextField(10);
		tfId.setText(id+"");
		panel.add(tfId,"wrap");
		
		JLabel lblOpen = new JLabel("Beliefern");
		panel.add(lblOpen);
		cbOpen = new JComboBox();
		cbOpen.setBackground(Color.WHITE);
		cbOpen.addItem("True");
		cbOpen.addItem("False");
		panel.add(cbOpen,"wrap");
		
		JLabel lblPlz = new JLabel("Plz");
		panel.add(lblPlz);
		tfPlz = new JTextField(10);
		tfPlz.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
			   if(tfPlz.getText().length() == 0)
			   {
	    	      populateAllStreetList();
			   }
			  else
				 populateStreetList();
			   
			 populateList();
			
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		panel.add(tfPlz,"wrap");
		
		JLabel lblBezirk = new JLabel("Bezirk");
		panel.add(lblBezirk);
		tfBezirk = new JTextField(20);
		panel.add(tfBezirk,"wrap");
		
		JLabel lblOrt = new JLabel("Ort");
		panel.add(lblOrt);
		tfOrt = new JTextField(20);
		panel.add(tfOrt,"wrap");
		
		JLabel lblM = new JLabel("Mindestbestellwert (€)");
		panel.add(lblM);
		tfmindestbestellwert = new JTextField(10);
		panel.add(tfmindestbestellwert,"wrap");
		
		JLabel lblLieferkosten = new JLabel("Lieferkosten (€)");
		panel.add(lblLieferkosten);
		tflieferkosten = new JTextField(10);
		panel.add(tflieferkosten,"wrap");
		
		JLabel lblLieferzeit = new JLabel("Lieferzeit");
		panel.add(lblLieferzeit);
		tflieferzeit = new JTextField(10);
		panel.add(tflieferzeit,"wrap"); 
		
		
		saveButton = new JButton();
		saveButton.setText("Speichern");
		saveButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		saveButton.setBackground(new Color(102,255,102));
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gebiet = new Gebiet();
				gebiet.setId(Integer.valueOf(tfId.getText()));
				gebiet.setIsopen(Boolean.valueOf(cbOpen.getSelectedItem().toString()));
				gebiet.setPlz(Integer.valueOf(tfPlz.getText()));
				gebiet.setBezirk(tfBezirk.getText());
				gebiet.setOrt(tfOrt.getText());
				String minValue = tfmindestbestellwert.getText().replace(',', '.');
				gebiet.setMindest(Double.valueOf(minValue));
				String cost = tflieferkosten.getText().replace(',', '.');
				gebiet.setLieferkosten(Double.valueOf(cost));
				gebiet.setLieferzeit(Integer.valueOf(tflieferzeit.getText()));
				dispose();
			}
			
		});
		cancelButton = new JButton();
		cancelButton.setText("Abbrechen"); 
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		panel.add(saveButton);
		panel.add(cancelButton);
		getContentPane().setBackground(new Color(209,222,235));
		JPanel panel2 = new JPanel();
		panel2.setBackground(new Color(209,222,235));
		getContentPane().add(panel,BorderLayout.CENTER);
   }
	public void populateStreetList()
	{
		tempList.clear();
		for (Iterator<Street> itr = StreetDAO.getInstance().findAll().iterator(); itr.hasNext();)
		{
			Street str = itr.next();
			if (str.getPlz().contains(tfPlz.getText()))
			{
				tempList.add(str);
			}
		}
	}
	public void searchAndAdd(String name)
	{
		for (Iterator<Street> itr = tempList.iterator(); itr.hasNext();)
		{
			Street str = itr.next();
			
			if((tfPlz.getText() == null || tfPlz.getText().length() == 0) && str.getBezirk().compareTo(name) == 0)
			{
				tfBezirk.setText(str.getBezirk());
				tfPlz.setText(str.getPlz());
				tfOrt.setText(str.getOrt());
				break;
			}
			else if (tfPlz.getText() != null)
			{
				if(((str.getBezirk() != null) && (str.getBezirk().compareTo(name) == 0) && (str.getPlz().compareTo(tfPlz.getText())== 0)))
				{
						tfBezirk.setText(str.getBezirk());
						tfPlz.setText(str.getPlz());
						tfOrt.setText(str.getOrt());
						break;
				}
			}
		}
	}
	public void populateAllStreetList()
	{
		tempList.clear();
		for (Iterator<Street> itr =StreetDAO.getInstance().findAll().iterator(); itr.hasNext();)
		{
			Street str = itr.next();
			tempList.add(str);
		}
	}
	public void populateList()
	{
		  java.util.List<Street> list  = tempList;
		  List<String> temp = new ArrayList();
		
		  listModel.clear();
		  for (Iterator itr = list.iterator(); itr.hasNext();)
		  {
			  Street street = (Street)itr.next();
			  if (street != null )
			  {
				  if(tfPlz != null && tfPlz.getText() != null )
				  {
					   if(street.getPlz().contains(tfPlz.getText()))
					   {
						  temp.add(street.getBezirk());
					   }
				  }
				  else
					  temp.add(street.getBezirk());
			  }
		  }
		  Set<String> hs = new HashSet();
		
		  
		  hs.addAll(temp);
		  temp.clear();
		  temp.addAll(hs);
		  if(temp != null && temp.size() > 0)
		  {
			 Collections.sort(temp);
		  }
		  for (Iterator<String> itr = temp.iterator(); itr.hasNext();)
		  {
			 listModel.addElement(itr.next());
		  }
   }
   public Gebiet getGebiet()
   {
		return gebiet;
   }
	
}
