package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.NumberSelectionDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;

import net.miginfocom.swing.MigLayout;

public class TablePlan extends ConfigurationView{

	public TablePlan() throws IOException
	{
		super();
		initComponents();
	}
	
	public TablePlan getObject()
	{
		return this;
	}
	public void initComponents() throws IOException
	{
		tablePanel = new JPanel();
		tablePanel.setLayout(new MigLayout());
		
		tableList = new ArrayList();
		final JCheckBox cbDelete = new JCheckBox("Loeschen");
		cbDelete.setBackground(new Color(209,222,235));
		cbDelete.setFont(new Font("Times New Roman",1,14));
		cbDelete.setPreferredSize(new Dimension(40,40));
		int totalTables = ShopTableDAO.getInstance().findAll().size();
		
		List<PosButton> buttonList = new ArrayList();
		for(int i = 1; i <= totalTables;i++)
		{
			final PosButton button = new PosButton();
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					if(cbDelete.isSelected())
					{
						if(isOccupied(button.getText()))
						{
							POSMessageDialog.showError("Tisch " + button.getText() + " belegt");
							return;
						}
						deleteTable(button.getText());
						button.setText("");
						return;
					}
					int tableNumber = NumberSelectionDialog2.take(BackOfficeWindow.getInstance(),"Hinzufuegen Sie die Tische");
					if(tableNumber == 0)return;
					if(button.getText().length() > 0)
					{
						if(isOccupied(button.getText())) 
						{
							POSMessageDialog.showError("Tisch " + button.getText() + " belegt");
							return;
						}
						int index = getIndex(Integer.parseInt(button.getText()));
						if(index != -1)
						{
							System.out.println("Removing from list:"+index);
							tableList.remove(index);
						}
					}
					
					button.setFont(new Font("Times New Roman",1,24));
					int index = getIndex(tableNumber);
					if(index == -1)
					{
						button.setText(tableNumber+"");
						ShopTable table = new ShopTable();
						table.setNumber(tableNumber+"");
						tableList.add(table);
					}
					
				}
			
			});
			button.setPreferredSize(new Dimension(75,75));
			button.setBackground(new Color(102,255,102));
			if(i % 9 == 0)
				tablePanel.add(button,"wrap");
			else
				tablePanel.add(button);
			
			buttonList.add(button);
	  }
	 
	  List<ShopTable> tableList1 = ShopTableDAO.getInstance().findAll();
		  if(tableList1 != null)
		  {
			  for(Iterator<ShopTable> itr = tableList1.iterator();itr.hasNext();)
			  {
				  ShopTable table = itr.next();
				  tableList.add(table);
			  }
			  if(tableList.size() > 0)
			  {
				  initializeTables(buttonList, tableList1);
			  }
	  }
	  tablePanel.add(cbDelete);
	  add(tablePanel);
	  
	  tablePanel.setBackground(new Color(209,222,235));
	 
	  setBackground(new Color(209,222,235));
	}
	
	public void deleteTable(String number)
	{
		if(number.length() > 0)
		{
			int index = getIndex(Integer.parseInt(number));
			if(index != -1)
			{
				tableList.remove(index);
				ShopTable table = ShopTableDAO.getInstance().getByNumber(number);
				if(table != null)
					ShopTableDAO.getInstance().delete(table);
			}
			
		}
	}
	public int getIndex(int number)
	{
		int index = 0;
		for(Iterator<ShopTable> itr = tableList.iterator();itr.hasNext();)
		{
			ShopTable table = itr.next();
			if(table.getNumber().compareTo(number+"") == 0)
				return index;
			index++;
		}
		return -1;
	}
	public boolean isOccupied(String number)
	{
		
		ShopTable table = ShopTableDAO.getInstance().getByNumber(number);
		if(table == null)return false;
		if(table.isOccupied())return true;
		else return false;
		
	}
	
	public void initializeTables(List<PosButton> buttonList, List<ShopTable> tableList)
	{
		Iterator<PosButton> buttonIterator = buttonList.iterator(); 
		for(Iterator<ShopTable> itr = tableList.iterator(); itr.hasNext();)
		{
			PosButton button = buttonIterator.next();
			ShopTable table = itr.next();
			String tableNumber = table.getNumber()+"";
			button.setFont(new Font("Times New Roman",1,24));
			button.setText(tableNumber.trim());
		}
	}
	@Override
	public boolean save() throws Exception {
			
		for(Iterator<ShopTable> itr = tableList.iterator(); itr.hasNext();)
		{
			ShopTable table = itr.next();
			String tableNr = table.getNumber()+"";
			ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNr.trim());
			if(shopTable == null)
			{
				System.out.println("Creating new shopTable for table:" + tableNr);
				shopTable = new ShopTable();
				shopTable.setNumber(tableNr);
				ShopTableDAO.getInstance().save(shopTable);
			}
		}
		return true;
	}

	@Override
	public void initialize() throws Exception {
		setInitialized(true);
	}
	public class ImagePanel extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private int iWidth2;
	private int iHeight2;

	public ImagePanel(Image image)
	{
	    this.image = image;
	    this.iWidth2 = image.getWidth(this);
	    this.iHeight2 = image.getHeight(this);
	}


	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (image != null)
	    {
	        int x = this.getParent().getWidth() - iWidth2;
	        int y = this.getParent().getHeight() - iHeight2;
	        g.drawImage(image,250,250,this);
	    }
	}
	}
	@Override
	public String getName() {
		
		return "Tischplan";
	}

	private JPanel tablePanel;
	
	List<ShopTable> tableList;
}
