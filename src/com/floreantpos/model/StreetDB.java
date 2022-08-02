package com.floreantpos.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;













import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.StreetDAO;



@XmlRootElement(name = "Streets")
public class StreetDB {
	
	private List<Street> street;
	private static List<Street> streetList;
	public void setStreet(List<Street> street) {
		this.street = street;
	}
	
	public List<Street> getStreet()
	{
		return this.street;
	}
	
	public static void save(StreetDB streetDb) {
		try {

			File file = new File("config", "Streets.xml");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(StreetDB.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			m.marshal(streetDb, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean exportStreet(File file) {
		try {

					
			JAXBContext jaxbContext = JAXBContext.newInstance(StreetDB.class);
			Marshaller m = jaxbContext.createMarshaller();
			OutputStream outputStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream, "ISO-8859-1");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			List<Street> streetList = StreetDAO.getInstance().findAll();
			StreetDB streetDb = new StreetDB();
			List<Street> newStreetList = new ArrayList();
			for(Iterator itr = streetList.iterator();itr.hasNext();)
			{
				Street street = (Street) itr.next();
				newStreetList.add(street);
			}
			streetDb.setStreet(newStreetList);
			m.marshal(streetDb, writer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static StreetDB loadStreetList(File file) {
		try {

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(StreetDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StreetDB streetDB = (StreetDB) unmarshaller.unmarshal(reader);
			return streetDB;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

 }

