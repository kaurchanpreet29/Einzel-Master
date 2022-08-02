package com.floreantpos.bo.actions;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

public class OnlineMenuAction { 
	public OnlineMenuAction(int shop)
	{
		List<MenuGroup> groupList = MenuGroupDAO.getInstance().findAll(); 
		String lieferkarte = "";
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		
		for(Iterator<MenuGroup> itr = groupList.iterator();itr.hasNext();)
		{
			MenuGroup group = itr.next();
			
			if(shop == 1)
			{
				if((group.getParent().getType().compareTo("LIEFERUNG") != 0) || ((restaurant.getName().compareTo(group.getParent().getShop()) != 0)))
					continue;
			}
			else if(shop == 2)
			{
				if((group.getParent().getType().compareTo("LIEFERUNG") != 0) || ((restaurant.getSecondaryName().compareTo(group.getParent().getShop()) != 0)))
					continue;
			}
			String description = group.getDescription();
			if(description == null || description.length() == 0)description ="";
			
			String note = group.getNote();
			if(note == null || note.length() == 0) note="";
			
			String link = group.getLink();
			if(link == null || link.length() == 0) link = group.getName();
			
			String secName = group.getSecname();
			if(secName == null || secName.length() == 0) secName = group.getName();
			
			String context = "<div class="+ "\"alert\""+ ">" + "<div class="+ "\"row-fluid\""+ "><div class="+ "\"span12\""+
					  "><h3>"+ secName + "</h3><small>"+ description+ "</small></div></div></div>"+
					  "<img src="+ "\"menu/"+ group.getId()+ ".png" +"\"" +"/>";
			
			System.out.println("Group Name:"+ group.getName());
			List<com.floreantpos.model.MenuItem> itemList = MenuItemDAO.getInstance().findByParent(group, false);
			
			for(Iterator<MenuItem> itemItr = itemList.iterator();itemItr.hasNext();)
			{
				MenuItem item = itemItr.next();
				int found = 0;
				String itemDescription = item.getDescription();
				if(itemDescription == null || itemDescription.length() == 0) itemDescription="";
				
				String itemNote = item.getNote();
				if(itemNote == null || itemNote.length() == 0) itemNote="";
				
				context = context + "<div class="+ "\"well well-small\"" + "><div class="+ "\"row-fluid\"" 
								  + "><div class="+ "\"span1\"" + "><h3>"+ item.getItemId()+"</h3></div>"
								  + "<div class="+ "\"span666\"" + "><h3>"+item.getName()+ "</h3><small>"+itemDescription +"</small><sup>"
								  +itemNote+"</sup></div>" + "<div class='item' data-id='"+ item.getItemId() +"'data-name='"+ item.getName() 
								  +"' data-price='" + NumberUtil.formatNumber(item.getPrice()).replace(',', '.') + "'>" + "<div class="+ "\"span2\""+ ">"
								  +"<button class="+ "\"add-to-cart btn btn-success\"" + "type="+ "\"button\"" + "><h4>"
								  + NumberUtil.formatNumber(item.getPrice()).replace('.',',') +" &euro; <i class="
								  + "\"icon-shopping-cart\""+ "></i></h4></button>"; 
								 
			Session s = MenuItemDAO.getInstance().createNewSession();
			MenuItem itemDup = MenuItemDAO.getInstance().get(item.getId(), s);
			List<MenuItemModifierGroup> zutatenGroupList = itemDup.getMenuItemModiferGroups();
			if(zutatenGroupList.size() > 0)
			{
				context = context + "<br/><br/>" + "<a href=" + "\""+ "#"+ item.getName().replace(' ', '-')+"\""
						  +"role="+ "\"button\""+ "class=" + "\"btn btn-mini btn btn-danger\"" 
						  +"data-toggle="+ "\"modal\""+ ">Zutaten</a>"
						  +"<div id="+"\""+ item.getName().replace(' ', '-') +"\""+ "class="+ "\"modal hide fade\""
						  + " tabindex=" + "\"-1\"" + "role="+ "\"dialog\""+ " aria-labelledby="+"\"myModalLabel\""
						  + " aria-hidden=" + "\"true\""+ ">"+ "<div class="+ "\"modal-header\"" + "><button type="
						  + "\"button\"" +"class="+ "\"close\"" +"data-dismiss="+ "\"modal\""+ " aria-hidden="
						  + "\"true\"" + ">x</button><h3 id=" + "\"myModalLabel\"" + ">"+ item.getName()+"</h3></div>"
						  +"<div class=" + "\"modal-body\"" + ">";
		    }
			 
			int index = 0;
			for(Iterator<MenuItemModifierGroup> zutatenGrpItr = zutatenGroupList.iterator();zutatenGrpItr.hasNext();)
			{
				MenuItemModifierGroup zutatenGrp = zutatenGrpItr.next();
				MenuModifierGroup modifierGrp = zutatenGrp.getModifierGroup();
				for(Iterator<MenuModifier> modifierItr = modifierGrp.getModifiers().iterator(); modifierItr.hasNext();)
				{
					String newIndex = item.getItemId() + "-"+index;
					MenuModifier modifier = modifierItr.next();
					String modifierName = modifier.getName().replace(',', '.');
					context = context + "<div class='item' data-id='"+ newIndex + "'data-name='" 
									  + modifierName +"' data-price='"+ NumberUtil.formatNumber(modifier.getExtraPrice()).replace(',', '.') 
									  + "'><div class="+ "\"span4\""+ "><button class="+ "\"add-to-cart btn btn-mini btn btn-warning\""
									  + "type="+ "\"button\""+ "><h4>"+ modifierName + "-" + NumberUtil.formatNumber(modifier.getExtraPrice()) 
									  + " &euro; <i class="+ "\"icon-shopping-cart\""+ "></i></h4></button></div></div>";
					
					System.out.println(modifier.getName());
					System.out.println(modifier.getExtraPrice());
					found = 1;
					index++;
				}
			}
			s.close();
			System.out.println("---------------------------------------------");
			if(found == 1)
			{
				context = context + "</div><div class="+ "\"modal-footer\""+ "><button class=" + "\"btn\""+ " data-dismiss="
						+ "\"modal\"" + " aria-hidden="+ "\"true\"" + ">Close</button></div></div>";
			}
			context = context + "</div></div></div></div>";
		}
		context += "<div class="+ "\"alert\""+ ">" + "<div class="+ "\"row-fluid\""+ "><div class="+ "\"span12\""+
					  "><small>"+ note + "</small></div></div></div>";
		lieferkarte += context;
		
		File file = new File("cache", link+".html");
		try {
		            Writer out = new BufferedWriter(new OutputStreamWriter(
		            	    new FileOutputStream(file), "iso-8859-1"));
		            out.write(context);
		            out.close();
		        }
		        catch(IOException ex) {
		        	ex.printStackTrace();
		            POSMessageDialog.showError(BackOfficeWindow.getInstance(),"Fehler!!!");
		            return;
		    }
		}
		File file = new File("cache", "Lieferkarte.html");
		try {
				Writer out = new BufferedWriter(new OutputStreamWriter(
	            	    new FileOutputStream(file), "iso-8859-1"));
				out.write(lieferkarte);
	            out.close();
	        }
	        catch(IOException ex) {
	        	ex.printStackTrace();
	            POSMessageDialog.showError("Fehler!!!");
	            return;
	    }
		POSMessageDialog.showMessage(BackOfficeWindow.getInstance(),"Erfolgreich Hochgeladen");
		
		if(shop == 1)
		{
			String path = TerminalConfig.getFtpMenuPath() + "cache/";
			uploadFiles(path);
		}
		else if(shop == 2)
		{
			String path = TerminalConfig.getFtpMenuPathSec() + "cache/";
			uploadFiles(path);
		}
	}
	public void uploadFiles(String path)
	{
	}
}
