package com.floreantpos.add.service;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.floreantpos.model.MenuItem;
import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

public class ExportToAccesDatabase {

	public static void main(String[] args) {
		//doWrite();
	}
	/*
	 * Test for the mdb writer
	 */
	public static void doWrite(List<MenuItem> items, File file) {
		Database db;
		Table newTable;
		try {
			db = DatabaseBuilder.create(Database.FileFormat.V2000, file);
			newTable = new TableBuilder("Item List")
					.addColumn(new ColumnBuilder("Description")
							.setType(DataType.TEXT))
					.addColumn(new ColumnBuilder("UnitPrice")
							.setType(DataType.DOUBLE))
					.addColumn(new ColumnBuilder("Barcode")
							.setType(DataType.TEXT))
					.toTable(db);
			for(MenuItem Item:items) {				
				try {
					newTable.addRow(Item.getName(), Item.getPrice(), Item.getBarcode());
				}catch(Exception exx) {
					exx.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "Erfolg");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}
}
