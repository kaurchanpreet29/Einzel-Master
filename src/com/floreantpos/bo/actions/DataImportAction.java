package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemPrice;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuItemPriceDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.datamigrate.Elements;

public class DataImportAction extends AbstractAction {

	public DataImportAction() {
		super("Produkt Import");
	}
	public DataImportAction(String name) {
		super(name);
		init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = DataExportAction.getFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		try {
			deleteOldMenu();
		}catch(Exception ex) {}
		try {
			importMenuItemsFromFile(file);
		} catch (Exception e1) {
			e1.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}
	}

	public void init() {
		JFileChooser fileChooser = DataExportAction.getFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();	

		try {
			deleteOldMenu();
		}catch(Exception ex) {

		}
		try {
			importMenuItemsFromFile(file);
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Success!");
		} catch (Exception e1) {
			//			e1.printStackTrace();
			try {
				setMenuCategory(file, null);
				setMenuGroup(file, null);
				setItemss(file, null);		
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Success!");
			} catch (Exception e11) {

				e11.printStackTrace();

			}

		}

	}

	
	public static void deleteOldMenu() {
		List<MenuCategory> menuCategory = MenuCategoryDAO.getInstance().findAll();
		boolean overwrite = false;
		if(menuCategory!=null&&menuCategory.size()>0) {
			int option = JOptionPane.showConfirmDialog(BackOfficeWindow.getInstance(),POSConstants.Achtung, POSConstants.Menue_ueberschreiben, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				overwrite = true;
			}
		}

		if(overwrite) {
			List<MenuItem> menuList = MenuItemDAO.getInstance().findAll();
			for (MenuItem item : menuList) {
				MenuItemDAO.getInstance().delete(item);
			}
			List<MenuGroup> menuGroup = MenuGroupDAO.getInstance().findAll();
			for (MenuGroup item : menuGroup) {
				MenuGroupDAO.getInstance().delete(item);
			}

			for (MenuCategory item : menuCategory) {
				MenuCategoryDAO.getInstance().delete(item);
			}
			List<MenuModifier> menuModifier = MenuModifierDAO.getInstance().findAll();
			for (MenuModifier item : menuModifier) {
				MenuModifierDAO.getInstance().delete(item);
			}
			List<MenuItemModifierGroup> menuGroupModifier = MenuItemModifierGroupDAO
					.getInstance().findAll();
			for (MenuItemModifierGroup item : menuGroupModifier) {
				MenuItemModifierGroupDAO.getInstance().delete(item);
			}	
		}
	}
	
	public void setItemss(File file, Session session) {
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook fs = new XSSFWorkbook(fis);
			XSSFSheet sheet = fs.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			int cols = 0; // No of columns
			int tmp = 0;
			// This trick ensures that we get the data properly even if it doesn't start from first few rows

			row = sheet.getRow(0);
			if(row != null) {
				tmp = sheet.getRow(0).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
			int start = 0;
			for(int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				String number ="";
				String barcode = "";
				String itemName ="";
				String groupName ="";
				String inhalt ="";
				int bestand =0;
				String CategoryName = "";
				String price = "";
				Double TaxRate = 0.00;
				Double price2 = 0.00;
				int priceCategory = 0;
				if(row != null) {
					int iteration =0;
					for(int c = 0; c < cols; c++) {
						cell = row.getCell((short)c);
						if(cell != null&&cell.toString().compareTo("")!=0) {
							if(iteration==0) {
								String temp = cell.toString();
								if(temp.contains(".")) 
									temp = temp.substring(0,temp.indexOf("."));
								number = temp;
							}
							if(iteration==2) {
								price = cell.toString();
							}
							if(iteration==1) {
								itemName = cell.toString();
							}
							if(iteration==4) {
								groupName = cell.toString();
							}
							if(iteration==5) {									
								CategoryName = cell.toString();								
							}
							if(iteration==6) {
								try {
									TaxRate = Double.parseDouble(cell.toString().replace(',', '.'));
								}catch(Exception ex) {

								}
							}
							if(iteration==7) {
								barcode =cell.toString();
							}
							if(iteration==8) {	
								try {
									price2 = Double.parseDouble(cell.toString().replace(',', '.'));
								}catch(Exception ex) {

								}
							}
							if(iteration==9) {
								try {
									String temp = cell.toString();
									if(temp.contains(".")) 
										temp = temp.substring(0,temp.indexOf("."));
									priceCategory =Integer.parseInt(temp);						
									System.out.println("Price Category : "+priceCategory);
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
							if(iteration==10) {
								try {
									String temp = cell.toString();
									if(temp.contains(".")) 
										temp = temp.substring(0,temp.indexOf("."));
									if(temp.contains(","))
										temp = temp.substring(0,temp.indexOf(","));
									bestand =Integer.parseInt(temp);	
									
								}catch(Exception ex) {

								}
							}
							if(iteration==11) {
								inhalt =cell.toString();
							}

						}
						++iteration;
					}
				}

				MenuItem menuItem = new MenuItem();					
				menuItem.setName(itemName);
				menuItem.setItemId(number);
				if(barcode.contains("E"))
					barcode = barcode.substring(0, barcode.indexOf("E"));
				barcode = barcode.replace(",", "");
				barcode = barcode.replace(".", "");
				menuItem.setBarcode(ReportUtil.removeLeadingZeroes(barcode));
				menuItem.setParent(MenuGroupDAO.getInstance().findByName(groupName).get(0));
				menuItem.setDescription("");
				menuItem.setNote("");
				menuItem.setUnitType(inhalt);
				price = price.replace(',', '.');
				try {
					menuItem.setPrice(Double.valueOf(price));
					if(Double.valueOf(price)==0.00)
						menuItem.setChangeprice(true);				
					else
						menuItem.setChangeprice(false);	
				}catch(Exception ex) {
					menuItem.setPrice(0.00);
					menuItem.setChangeprice(false);	
				}

				if(TaxRate == OrderView.taxDineIn) {
					menuItem.setTax(TaxDAO.getInstance().findByName(POSConstants.DINE_IN));
				} else if (TaxRate == OrderView.taxHomeDelivery) {
					menuItem.setTax(TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY));
				}


				menuItem.setBuyPrice(price2);				

				menuItem.setRed(0);
				menuItem.setGreen(0);
				menuItem.setBlue(0);
				menuItem.setInstock(bestand);
				menuItem.setFred(255);
				menuItem.setFgreen(255);
				menuItem.setFblue(255);
				menuItem.setVisible(true);
				menuItem.setPriceCategory(priceCategory);
				menuItem.setSubitemid("");
				menuItem.setShowImageOnly(false);
				menuItem.setDiscountRate(0.00);
				if(!itemName.isEmpty()) {
					try {
						MenuItemDAO.getInstance().saveOrUpdate(menuItem);
					} catch (Exception x) {
					}		
				}							

				++start;
			}
			fs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}



	public void setMenuCategory(File file, Session session) {
		try {

			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook fs = new XSSFWorkbook(fis);
			XSSFSheet sheet = fs.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			for(int i = 0; i < 10 || i < rows; i++) {

			}
			row = sheet.getRow(0);
			if(row != null) {
				tmp = sheet.getRow(0).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
			int start = 0;
			String oldName = "";

			for(int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				int number = 1;
				String currentName ="";
				double TaxRate = 0.00;
				int priceCategory =0;
				MenuCategory menuCategory = new MenuCategory();
				if(row != null) {						
					int iteration =0;
					for(int c = 0; c < cols; c++) {
						cell = row.getCell((short)c);
						if(cell != null&&cell.toString().compareTo("")!=0) {								
							if(iteration==5) {									
								currentName = cell.toString();								
							}
							if(iteration==6) {									
								TaxRate = Double.parseDouble(cell.toString().replace(',', '.'));								
							}
							if(iteration==9) {
								try {
									String temp = cell.toString();
									if(temp.contains(".")) 
										temp = temp.substring(0,temp.indexOf("."));
									priceCategory =Integer.parseInt(temp);						
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						++iteration;
					}
				}

				if(oldName.isEmpty()||MenuCategoryDAO.getInstance().findByNameUnique(currentName)==null) {						
					menuCategory.setName(currentName);
					menuCategory.setVisible(true);
					if(TaxRate==OrderView.taxHomeDelivery)
						menuCategory.setType(POSConstants.HOME_DELIVERY);
					else
						menuCategory.setType(POSConstants.DINE_IN);	
					menuCategory.setPriceCategory(priceCategory);
					menuCategory.setCategoryid(number);
					if(!currentName.isEmpty())
						MenuCategoryDAO.getInstance().saveOrUpdate(menuCategory);
					oldName = currentName;
					number++;
				}
				++start;
			}			

			fs.close();

		}catch(Exception ex) {
			ex.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), ex.getMessage());
		}
	}


	public void setMenuGroup(File file, Session session) {
		try {

			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook fs = new XSSFWorkbook(fis);
			XSSFSheet sheet = fs.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			for(int i = 0; i < 10 || i < rows; i++) {

			}
			row = sheet.getRow(0);
			if(row != null) {
				tmp = sheet.getRow(0).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
			int start = 0;
			String oldName = null;

			for(int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				int number = 0;
				String groupName ="";
				String oldname ="";
				String CategoryName = "";
				MenuItem newItem = new MenuItem();
				if(row != null) {
					int iteration =0;
					for(int c = 0; c < cols; c++) {
						cell = row.getCell((short)c);
						if(cell != null&&cell.toString().compareTo("")!=0) {								
							if(iteration==4) {
								groupName = cell.toString();
							}
							if(iteration==5) {									
								CategoryName = cell.toString();								
							}
						}
						++iteration;
					}
				}

				if(oldname.isEmpty()||MenuGroupDAO.getInstance().findByName(groupName).get(0)==null) {
					MenuGroup foodGroup = null;
					boolean update = true;
					try {
						foodGroup = MenuGroupDAO.getInstance().findByName(groupName).get(0);
						update = false;
					}catch(Exception ex) {
						foodGroup = new MenuGroup();
					}



					List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(CategoryName);
					if(category != null) {
						foodGroup.setParent(category.get(0));
					}

					foodGroup.setName(groupName);
					foodGroup.setGroupid(number);						
					foodGroup.setDiscount(0.00);
					foodGroup.setVisible(true);
					if(update&&!groupName.isEmpty())
						MenuGroupDAO.getInstance().saveOrUpdate(foodGroup);
					oldName = groupName;
					number++;
				}
				++start;
			}			

			fs.close();

		}catch(Exception ex) {
			ex.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), ex.getMessage());
		}
	}

	public static void importMenuItemsFromFile(File file) throws Exception {
		if (file == null)
			return;

		FileInputStream	inputStream = new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream, "iso-8859-1");
		importMenuItems(reader);
	}

	public static void importMenuItems(Reader reader) throws Exception {

		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();

		Map<String, Object> objectMap = new HashMap<String, Object>();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Elements elements = (Elements) unmarshaller.unmarshal(reader);

			session = dao.createNewSession();

			transaction = session.beginTransaction();

			List<Tax> taxes = elements.getTaxes();
			if (taxes != null) {
				for (Tax tax : taxes) {
					objectMap.put(tax.getUniqueId(), tax);
					tax.setId(null);

					TaxDAO.getInstance().save(tax, session);
				}
			}

			List<MenuCategory> menuCategories = elements.getMenuCategories();
			if (menuCategories != null) {
				for (MenuCategory menuCategory : menuCategories) {

					objectMap.put(menuCategory.getUniqueId(), menuCategory);
					menuCategory.setId(null);

					MenuCategoryDAO.getInstance().save(menuCategory, session);
				}
			}

			List<MenuGroup> menuGroups = elements.getMenuGroups();
			if (menuGroups != null) {
				for (MenuGroup menuGroup : menuGroups) {

					MenuCategory menuCategory = menuGroup.getParent();
					if (menuCategory != null) {
						menuCategory = (MenuCategory) objectMap.get(menuCategory.getUniqueId());
						menuGroup.setParent(menuCategory);
					}

					objectMap.put(menuGroup.getUniqueId(), menuGroup);
					menuGroup.setId(null);

					MenuGroupDAO.getInstance().saveOrUpdate(menuGroup, session);
				}
			}

			List<MenuModifierGroup> menuModifierGroups = elements.getMenuModifierGroups();
			if (menuModifierGroups != null) {
				for (MenuModifierGroup menuModifierGroup : menuModifierGroups) {
					objectMap.put(menuModifierGroup.getUniqueId(), menuModifierGroup);
					menuModifierGroup.setId(null);
					System.out.println("Menu modifier groups.............");
					MenuModifierGroupDAO.getInstance().saveOrUpdate(menuModifierGroup, session);
				}
			}

			List<MenuModifier> menuModifiers = elements.getMenuModifiers();
			if (menuModifiers != null) {
				for (MenuModifier menuModifier : menuModifiers) {

					objectMap.put(menuModifier.getUniqueId(), menuModifier);
					menuModifier.setId(null);
					System.out.println("Menu Modifier");
					MenuModifierGroup menuModifierGroup = menuModifier.getModifierGroup();
					if (menuModifierGroup != null) {
						menuModifierGroup = (MenuModifierGroup) objectMap.get(menuModifierGroup.getUniqueId());
						menuModifier.setModifierGroup(menuModifierGroup);
					}

					Tax tax = menuModifier.getTax();
					if (tax != null) {
						tax = (Tax) objectMap.get(tax.getUniqueId());
						menuModifier.setTax(tax);
					}

					MenuModifierDAO.getInstance().saveOrUpdate(menuModifier, session);
				}
			}

			List<MenuItemModifierGroup> menuItemModifierGroups = elements.getMenuItemModifierGroups();
			if (menuItemModifierGroups != null) {
				for (MenuItemModifierGroup mimg : menuItemModifierGroups) {
					objectMap.put(mimg.getUniqueId(), mimg);
					mimg.setId(null);
					System.out.println("Menu Item modifier groups.............");
					MenuModifierGroup menuModifierGroup = mimg.getModifierGroup();
					if (menuModifierGroup != null) {
						menuModifierGroup = (MenuModifierGroup) objectMap.get(menuModifierGroup.getUniqueId());
						mimg.setModifierGroup(menuModifierGroup);
					}

					MenuItemModifierGroupDAO.getInstance().save(mimg, session);
				}
			}

			List<MenuItemPrice> priceList = elements.getMenuItemPrices();
			List<MenuItemPrice> priceList1= new ArrayList<MenuItemPrice>();
			if(priceList!=null&&!priceList.isEmpty()) {
				for(MenuItemPrice price:priceList) {
					if(price!=null&&price.getName()!=null) {
						MenuItemPrice price1 = new MenuItemPrice();
						price1.setName(price.getName());
						price1.setPrice(price.getPrice());
						MenuItemPriceDAO.getInstance().saveOrUpdate(price1, session);
						priceList1.add(price1);
					}
				}
			}
			
			List<MenuItem> menuItems = elements.getMenuItems();
			if (menuItems != null) {
				for (MenuItem menuItem : menuItems) {

					objectMap.put(menuItem.getUniqueId(), menuItem);
					menuItem.setId(menuItem.getId());

					MenuGroup menuGroup = menuItem.getParent();
					if (menuGroup != null) {
						menuGroup = (MenuGroup) objectMap.get(menuGroup.getUniqueId());
						menuItem.setParent(menuGroup);

					}
					Tax tax = menuItem.getTax();
					if (tax != null) {
						tax = (Tax) objectMap.get(tax.getUniqueId());
						menuItem.setTax(tax);
					}
					List<MenuItemModifierGroup> menuItemModiferGroups = menuItem.getMenuItemModiferGroups();
					if (menuItemModiferGroups != null) {
						for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
							MenuItemModifierGroup menuItemModifierGroup2 = (MenuItemModifierGroup) objectMap.get(menuItemModifierGroup.getUniqueId());
							menuItemModifierGroup.setId(menuItemModifierGroup2.getId());
							menuItemModifierGroup.setModifierGroup(menuItemModifierGroup2.getModifierGroup());
						}
					}
					MenuItem it = new MenuItem();
					it.setId(menuItem.getId());
					String name = menuItem.getName();
					it.setName(name.length() > 30 ? name.substring(0, 29) : name);
					it.setItemId(menuItem.getItemId());
					it.setBarcode(menuItem.getBarcode());
					it.setGreen(menuItem.getGreen());
					it.setBlue(menuItem.getBlue());
					it.setRed(menuItem.getRed());
					it.setInstock(menuItem.getInstock());
					it.setSold(menuItem.getSold());
					it.setSubitemid(menuItem.getSubitemid());
					it.setFblue(menuItem.getFblue());
					it.setFgreen(menuItem.getFgreen());
					it.setFred(menuItem.getFred());
					it.setTax(menuItem.getTax());
					it.setParent(menuGroup);
					it.setBuyPrice(menuItem.getBuyPrice());
					it.setPrice(menuItem.getPrice());
					it.setImage(menuItem.getImage());					
					it.setChangeprice(menuItem.isChangeprice());
					it.setDiscountRate(menuItem.getDiscountRate());
					it.setWeightgrams(menuItem.getWeightgrams());
					if(menuItem.isChangeprice() != null)
						it.setChangeprice(menuItem.isChangeprice());
					it.setPriceCategory(menuItem.getPriceCategory());					
					it.setVisible(menuItem.isVisible());
					it.setLieferantName(menuItem.getLieferantName());
					it.setLiferantBuyPrice(menuItem.getLiferantBuyPrice());
					it.setMenuItemModiferGroups(menuItemModiferGroups);

					MenuItemDAO.getInstance().saveOrUpdate(it, session);
				}
			}

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(),
					"Saved! - Category = " + menuCategories.size() + ", Groups -" + menuGroups.size()
					+ ", Items - " + menuItems.size());

			if(transaction.wasCommitted())
				System.out.println("Transaction committed");
			transaction.commit();
		} catch (Exception e1) {

			if (transaction != null)
				transaction.rollback();
			throw e1;

		} finally {
			dao.closeSession(session);
			IOUtils.closeQuietly(reader);
		}
	}
}
