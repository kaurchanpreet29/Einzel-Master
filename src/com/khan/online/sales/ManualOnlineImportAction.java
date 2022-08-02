package com.khan.online.sales;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.report.Report;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ManualOnlineImportAction extends AbstractAction
{
	  public ManualOnlineImportAction()
	  {
	    super("Online Sales Import");
	  }
	  
	  public void actionPerformed(ActionEvent ee)
	  {
	    JFileChooser fileChooser = new JFileChooser("C:\\Data\\Import");
	    fileChooser.setFileFilter(new FileFilter()
	    {
	      public String getDescription()
	      {
	        return "CSV file";
	      }
	      
	      public boolean accept(File f)
	      {
	        return f.getName().toLowerCase().endsWith(".csv");
	      }
	    });
	    fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
	    File file = fileChooser.getSelectedFile();
	    if (file == null) {
	      return;
	    }
	    SalesImporter.getImporter().importSalesReport(file);
	  }
	  
	  
		}
