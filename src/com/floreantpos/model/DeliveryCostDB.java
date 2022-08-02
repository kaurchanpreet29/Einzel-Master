package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.DeliveryCostDAO;
import com.floreantpos.model.dao.StreetDAO;

@XmlRootElement(name = "liefergebiet")
public class DeliveryCostDB {
	
	private List<Gebiet> gebiet;

	public void setGebiet(List<Gebiet> gebiet) {
		this.gebiet = gebiet;
	}
	
	public List<Gebiet> getGebiet()
	{
		return this.gebiet;
	}
	
	public void save() {
		try {

			File file = new File("config", "liefergebiet.xml");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryCostDB.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean exportLiefer(File file) {
		try {

					
			JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryCostDB.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			
			List<Gebiet> gebietList = DeliveryCostDAO.getInstance().findAll();
			DeliveryCostDB lieferDb = new DeliveryCostDB();
			List<Gebiet> newGebietList = new ArrayList();
			for(Iterator itr = gebietList.iterator();itr.hasNext();)
			{
				Gebiet gebiet = (Gebiet) itr.next();
				newGebietList.add(gebiet);
			}
			lieferDb.setGebiet(newGebietList);
			m.marshal(lieferDb, writer);

			FileUtils.write(file, writer.toString());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static DeliveryCostDB loadDeliveryCostList() {
		try {

			File file = new File("config", "liefergebiet.xml");
					
			if (!file.exists()) {
				return null;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryCostDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			DeliveryCostDB deliveryCostDB = (DeliveryCostDB) unmarshaller.unmarshal(reader);
			return deliveryCostDB;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
