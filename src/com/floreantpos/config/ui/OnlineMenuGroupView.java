package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class OnlineMenuGroupView extends POSDialog{

	private JComboBox cb00;
	private JComboBox cb01;
	private JComboBox cb02;
	private JComboBox cb03;
	
	private JComboBox cb10;
	private JComboBox cb11;
	private JComboBox cb12;
	private JComboBox cb13;
	
	private JComboBox cb20;
	private JComboBox cb21;
	private JComboBox cb22;
	private JComboBox cb23;
	
	private JComboBox cb30;
	private JComboBox cb31;
	private JComboBox cb32;
	private JComboBox cb33;
	
	private JComboBox cb40;
	private JComboBox cb41;
	private JComboBox cb42;
	private JComboBox cb43;
	
	private JComboBox cb50;
	private JComboBox cb51;
	private JComboBox cb52;
	private JComboBox cb53;
	
	private JComboBox cb60;
	private JComboBox cb61;
	private JComboBox cb62;
	private JComboBox cb63;
	
	private JComboBox cb70;
	private JComboBox cb71;
	private JComboBox cb72;
	private JComboBox cb73;
	
	List groupList;
	int shop = 0;
	public OnlineMenuGroupView(Frame parent, int shop) {
		super(parent,false);
		this.shop = shop;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800,600));
		setBackground(new Color(209,222,235));
		setTitle("Menu Grupplen Plan");
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		panel.setLayout(new GridLayout(8,4,4,4));
		setBackground(new Color(209,222,235));
		groupList = new ArrayList();
		List<MenuGroup> group = MenuGroupDAO.getInstance().findAll();
		groupList.add("");
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		
		if(shop == 1)
		{
			for(Iterator<MenuGroup> itr = group.iterator();itr.hasNext();)
			{
				MenuGroup group1 = itr.next();
			
				if((group1.getParent().getType().compareTo("LIEFERUNG") != 0)|| (restaurant.getName().compareTo(group1.getParent().getShop()) != 0))
					continue;
				if(group1.getLink() != null && group1.getLink().length() > 0)
					groupList.add(group1.getLink());
				else
					groupList.add(group1.getName());
			}
		}
		else if (shop == 2)
		{
			for(Iterator<MenuGroup> itr = group.iterator();itr.hasNext();)
			{
				MenuGroup group1 = itr.next();
			
				if((group1.getParent().getType().compareTo("LIEFERUNG") != 0)|| (restaurant.getSecondaryName().compareTo(group1.getParent().getShop()) != 0))
					continue;
				if(group1.getLink() != null && group1.getLink().length() > 0)
					groupList.add(group1.getLink());
				else
					groupList.add(group1.getName());
			}
		}
		cb00 = new JComboBox();
		customizeCombo(cb00);
		panel.add(cb00);
		cb01 = new JComboBox();
		customizeCombo(cb01);
		panel.add(cb01);
		cb02 = new JComboBox();
		customizeCombo(cb02);
		panel.add(cb02);
		cb03 = new JComboBox();
		customizeCombo(cb03);
		panel.add(cb03);
		
		cb10 = new JComboBox();
		customizeCombo(cb10);
		panel.add(cb10);
		cb11 = new JComboBox();
		customizeCombo(cb11);
		panel.add(cb11);
		cb12 = new JComboBox();
		customizeCombo(cb12);
		panel.add(cb12);
		cb13 = new JComboBox();
		customizeCombo(cb13);
		panel.add(cb13);
		
		cb20 = new JComboBox();
		customizeCombo(cb20);
		panel.add(cb20);
		cb21 = new JComboBox();
		customizeCombo(cb21);
		panel.add(cb21);
		cb22 = new JComboBox();
		customizeCombo(cb22);
		panel.add(cb22);
		cb23 = new JComboBox();
		customizeCombo(cb23);
		panel.add(cb23);
		
		cb30 = new JComboBox();
		customizeCombo(cb30);
		panel.add(cb30);
		cb31 = new JComboBox();
		customizeCombo(cb31);
		panel.add(cb31);
		cb32 = new JComboBox();
		customizeCombo(cb32);
		panel.add(cb32);
		cb33 = new JComboBox();
		customizeCombo(cb33);
		panel.add(cb33);
		
		cb40 = new JComboBox();
		customizeCombo(cb40);
		panel.add(cb40);
		cb41 = new JComboBox();
		customizeCombo(cb41);
		panel.add(cb41);
		cb42 = new JComboBox();
		customizeCombo(cb42);
		panel.add(cb42);
		cb43 = new JComboBox();
		customizeCombo(cb43);
		panel.add(cb43);
		
		cb50 = new JComboBox();
		customizeCombo(cb50);
		panel.add(cb50);
		cb51 = new JComboBox();
		customizeCombo(cb51);
		panel.add(cb51);
		cb52 = new JComboBox();
		customizeCombo(cb52);
		panel.add(cb52);
		cb53 = new JComboBox();
		customizeCombo(cb53);
		panel.add(cb53);
		
		cb60 = new JComboBox();
		customizeCombo(cb60);
		panel.add(cb60);
		cb61 = new JComboBox();
		customizeCombo(cb61);
		panel.add(cb61);
		cb62 = new JComboBox();
		customizeCombo(cb62);
		panel.add(cb62);
		cb63 = new JComboBox();
		customizeCombo(cb63);
		panel.add(cb63);
		
		cb70 = new JComboBox();
		customizeCombo(cb70);
		panel.add(cb70);
		cb71 = new JComboBox();
		customizeCombo(cb71);
		panel.add(cb71);
		cb72 = new JComboBox();
		customizeCombo(cb72);
		panel.add(cb72);
		cb73 = new JComboBox();
		customizeCombo(cb73);
		panel.add(cb73);
		panel.setPreferredSize(new Dimension(700,500));
		PosButton saveButton = new PosButton("Speichern");
		saveButton.setBackground(new Color(102,255,102));
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				dispose();
			}
			
		});
		PosButton cancelButton = new PosButton("Abbrechen");
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					setCanceled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				dispose();
			}
			
		});
		
		bottomPanel.add(saveButton);
		bottomPanel.add(cancelButton);
		add(panel, BorderLayout.NORTH);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public void customizeCombo(JComboBox cb)
	{
		System.out.println("----");
		if(groupList == null)return;
		cb.setModel(new ComboBoxModel(groupList));
		cb.setFont(new Font("Times New Roman", Font.BOLD, 14));
		cb.setBackground(Color.WHITE);
	}
	
	
	public boolean save() throws Exception {
	
		deleteFolder(new File("menu/img"));
		List<MenuGroup> groupList = MenuGroupDAO.getInstance().findAll();
		for(Iterator<MenuGroup> itr = groupList.iterator(); itr.hasNext();)
		{
			MenuGroup group = itr.next();
			String linkName = group.getLink();
			String secName = group.getSecname();
			if(linkName == null || linkName.length() == 0)
				linkName = group.getName();
			if(secName == null || secName.length() == 0)
				secName = group.getName();
			
			if(cb00.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb00.getSelectedItem().toString());
			else if(cb01.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb01.getSelectedItem().toString());
			else if(cb02.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb02.getSelectedItem().toString());
			else if(cb03.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb03.getSelectedItem().toString());
			else if(cb10.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb10.getSelectedItem().toString());
			else if(cb11.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb11.getSelectedItem().toString());
			else if(cb12.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb12.getSelectedItem().toString());
			else if(cb13.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb13.getSelectedItem().toString());
			else if(cb20.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb20.getSelectedItem().toString());
			else if(cb21.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb21.getSelectedItem().toString());
			else if(cb22.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb22.getSelectedItem().toString());
			else if(cb23.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb23.getSelectedItem().toString());
			else if(cb30.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb30.getSelectedItem().toString());
			else if(cb31.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb31.getSelectedItem().toString());
			else if(cb32.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb32.getSelectedItem().toString());
			else if(cb33.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb33.getSelectedItem().toString());
			else if(cb40.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb40.getSelectedItem().toString());
			else if(cb41.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb41.getSelectedItem().toString());
			else if(cb42.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb42.getSelectedItem().toString());
			else if(cb43.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb43.getSelectedItem().toString());
			else if(cb50.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb50.getSelectedItem().toString());
			else if(cb51.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb51.getSelectedItem().toString());
			else if(cb52.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb52.getSelectedItem().toString());
			else if(cb53.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb53.getSelectedItem().toString());
			else if(cb60.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb60.getSelectedItem().toString());
			else if(cb61.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb61.getSelectedItem().toString());
			else if(cb62.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb62.getSelectedItem().toString());
			else if(cb63.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb63.getSelectedItem().toString());
			else if(cb70.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb70.getSelectedItem().toString());
			else if(cb71.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb71.getSelectedItem().toString());
			else if(cb72.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb72.getSelectedItem().toString());
			else if(cb73.getSelectedItem().toString().compareTo(linkName) == 0)
				createImage(secName, cb73.getSelectedItem().toString());

			
		}
		String context = "<div id='navi' style='background-image: url(menu/category.png); height:100px; width:685px;'>"
					+ "<table width="+ "\"685px\""+ ">";
		
		if(cb00.getSelectedItem().toString().length() > 0 || cb01.getSelectedItem().toString().length() > 0 || cb02.getSelectedItem().toString().length() > 0
				|| cb03.getSelectedItem().toString().length() > 0)
		{
			context = context + "<tr>";
			if(cb00.getSelectedItem().toString().length() > 0)
				context = context + "<td width="+ "\"150px\""+ "><a href='"+ cb00.getSelectedItem()+ ".html'><img src='menu/img/" + cb00.getSelectedItem() + ".png' /></a></td>";
			if(cb01.getSelectedItem().toString().length() > 0)	
				context = context +"</td><td width="+ "\"150px\""+ "><a href='"+ cb01.getSelectedItem()+".html'><img src='menu/img/"+ cb01.getSelectedItem()+ ".png' /></a></td>";
			if(cb02.getSelectedItem().toString().length() > 0)
				context = context+"<td width="+ "\"150px\"" + "><a href='"+ cb02.getSelectedItem()+".html'><img src='menu/img/"+ cb02.getSelectedItem()+".png' /></a></td>";
			if(cb03.getSelectedItem().toString().length() > 0)
				context = context+"<td><a href='"+cb03.getSelectedItem() +".html'><img src='menu/img/"+cb03.getSelectedItem()+".png' /></a></td>";
			context = context +"</tr>";
		}	
	
	if(cb10.getSelectedItem().toString().length() > 0 || cb11.getSelectedItem().toString().length() > 0 || cb12.getSelectedItem().toString().length() > 0
			|| cb13.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb10.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+cb10.getSelectedItem() +".html'><img src='menu/img/"+ cb10.getSelectedItem()+ ".png'/></a></td>";
		if(cb11.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb11.getSelectedItem()+".html'><img src='menu/img/"+cb11.getSelectedItem()+".png' /></a></td>";
		if(cb12.getSelectedItem().toString().length() > 0)
			context =context+"<td><a href='"+ cb12.getSelectedItem()+".html'><img src='menu/img/"+cb12.getSelectedItem()+".png' /></a></td>";
		if(cb13.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb13.getSelectedItem()+".html'><img src='menu/img/"+cb13.getSelectedItem()+".png' /></a></td>";
		context = context +"</tr>";
	}
	if(cb20.getSelectedItem().toString().length() > 0 || cb21.getSelectedItem().toString().length() > 0 || cb22.getSelectedItem().toString().length() > 0
			|| cb23.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb20.getSelectedItem().toString().length() > 0)				
			context = context	+"<td><a href='"+cb20.getSelectedItem() +".html'><img src='menu/img/"+ cb20.getSelectedItem()+ ".png'/></a></td>";
		if(cb21.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb21.getSelectedItem()+".html'><img src='menu/img/"+cb21.getSelectedItem()+".png' /></a></td>";
		if(cb22.getSelectedItem().toString().length() > 0)
			context =context+"<td><a href='"+ cb22.getSelectedItem()+".html'><img src='menu/img/"+cb22.getSelectedItem()+".png' /></a></td>";
		if(cb23.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb23.getSelectedItem()+".html'><img src='menu/img/"+cb23.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";
	}
	
	if(cb30.getSelectedItem().toString().length() > 0 || cb31.getSelectedItem().toString().length() > 0 || cb32.getSelectedItem().toString().length() > 0
			|| cb33.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb30.getSelectedItem().toString().length() > 0)
			context = context	+"<td><a href='"+cb30.getSelectedItem() +".html'><img src='menu/img/"+ cb30.getSelectedItem()+ ".png'/></a></td>";
		if(cb31.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb31.getSelectedItem()+".html'><img src='menu/img/"+cb31.getSelectedItem()+".png' /></a></td>";	
		if(cb32.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb32.getSelectedItem()+".html'><img src='menu/img/"+cb32.getSelectedItem()+".png' /></a></td>";
		if(cb33.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb33.getSelectedItem()+".html'><img src='menu/img/"+cb33.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";
	}
	if(cb40.getSelectedItem().toString().length() > 0 || cb41.getSelectedItem().toString().length() > 0 || cb42.getSelectedItem().toString().length() > 0
			|| cb43.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb40.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+cb40.getSelectedItem() +".html'><img src='menu/img/"+ cb40.getSelectedItem()+ ".png'/></a></td>";
		if(cb41.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb41.getSelectedItem()+".html'><img src='menu/img/"+cb41.getSelectedItem()+".png' /></a></td>";
		if(cb42.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb42.getSelectedItem()+".html'><img src='menu/img/"+cb42.getSelectedItem()+".png' /></a></td>";
		if(cb43.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb43.getSelectedItem()+".html'><img src='menu/img/"+cb43.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";	
	}
	if(cb50.getSelectedItem().toString().length() > 0 || cb51.getSelectedItem().toString().length() > 0 || cb52.getSelectedItem().toString().length() > 0
			|| cb53.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb50.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+cb50.getSelectedItem() +".html'><img src='menu/img/"+ cb50.getSelectedItem()+ ".png'/></a></td>";
		if(cb51.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb51.getSelectedItem()+".html'><img src='menu/img/"+cb51.getSelectedItem()+".png' /></a></td>";
		if(cb52.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb52.getSelectedItem()+".html'><img src='menu/img/"+cb52.getSelectedItem()+".png' /></a></td>";
		if(cb53.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb53.getSelectedItem()+".html'><img src='menu/img/"+cb53.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";
	}
	if(cb60.getSelectedItem().toString().length() > 0 || cb61.getSelectedItem().toString().length() > 0 || cb62.getSelectedItem().toString().length() > 0
			|| cb63.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb60.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+cb60.getSelectedItem() +".html'><img src='menu/img/"+ cb60.getSelectedItem()+ ".png'/></a></td>";
		if(cb61.getSelectedItem().toString().length() > 0)
			context = context	+"<td><a href='"+ cb61.getSelectedItem()+".html'><img src='menu/img/"+cb61.getSelectedItem()+".png' /></a></td>";
		if(cb62.getSelectedItem().toString().length() > 0)
			context = context +"<td><a href='"+ cb62.getSelectedItem()+".html'><img src='menu/img/"+cb62.getSelectedItem()+".png' /></a></td>";
		if(cb63.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb63.getSelectedItem()+".html'><img src='menu/img/"+cb63.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";
	}
	if(cb70.getSelectedItem().toString().length() > 0 || cb71.getSelectedItem().toString().length() > 0 || cb72.getSelectedItem().toString().length() > 0
			|| cb73.getSelectedItem().toString().length() > 0)
	{
		context = context + "<tr>";
		if(cb70.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+cb70.getSelectedItem() +".html'><img src='menu/img/"+ cb70.getSelectedItem()+ ".png'/></a></td>";
		if(cb71.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb71.getSelectedItem()+".html'><img src='menu/img/"+cb71.getSelectedItem()+".png' /></a></td>";
		if(cb72.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb72.getSelectedItem()+".html'><img src='menu/img/"+cb72.getSelectedItem()+".png' /></a></td>";
		if(cb73.getSelectedItem().toString().length() > 0)
			context = context+"<td><a href='"+ cb73.getSelectedItem()+".html'><img src='menu/img/"+cb73.getSelectedItem()+".png' /></a></td>";
		context = context + "</tr>";
	}
		context = context + "</table></div>";
		File file = new File("menu/img", "category.html");
		try 
		{
		     Writer out = new BufferedWriter(new OutputStreamWriter(
		     new FileOutputStream(file), "iso-8859-1"));
		     out.write(context);
		     out.close();
		}
		catch(IOException ex) {
		    	ex.printStackTrace();
		        POSMessageDialog.showError("Fehler!!!");
		        return false;
		}
		
		if(shop == 1)
		{
			String path = TerminalConfig.getFtpMenuPath()+"img/";
			uploadImages(path);
			String htmlPath = TerminalConfig.getFtpMenuPath();
			
		}
		else if(shop == 2)
		{
			String path = TerminalConfig.getFtpMenuPathSec()+"img/";
			uploadImages(path);
			String htmlPath = TerminalConfig.getFtpMenuPathSec();
			
		}
		return true;
	}
	
	public void createImage(String text, String linkname)
	{
		try
		{
		int width = 150, height = 14;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        Font font = new Font("Tahoma", Font.PLAIN, 12);
	    ig2.setFont(font);
	    FontMetrics fontMetrics = ig2.getFontMetrics();
	    //int stringWidth = fontMetrics.stringWidth(text);
	    int stringHeight = fontMetrics.getAscent();
	    ig2.setPaint(new Color(250,245,178));
	    ig2.drawString(text, 5, height / 2 + stringHeight / 4);

	    ImageIO.write(bi, "PNG", new File("menu/img",linkname+".png"));
	    } catch (IOException ie) {
	      ie.printStackTrace();
	    }
	}
	
	public void uploadImages(String path)
	{
	}
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	        	if(!f.isDirectory()) {
	                f.delete();
	            }
	        }
	    }
	}
}
