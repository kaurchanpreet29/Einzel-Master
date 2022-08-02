package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.VirtualPrinterDAO;

@XmlRootElement(name = "Loyalty")
public class Loyalty {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Loyalty getInstance()
	{
		return this;
	}
	
	public void IncrementAndsave(long id) {
		try {

			File file = new File("config", "Loyalty.xml");
			
			this.id++;
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Loyalty.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long getNewId() {
		try {

			File file = new File("config", "Loyalty.xml");
					
			if (!file.exists()) {
				return 0;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(Loyalty.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Loyalty loyalty = (Loyalty) unmarshaller.unmarshal(reader);
					
			return loyalty.id;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}
	
	public static Loyalty loadLoyalty() {
		try {

			File file = new File("config", "Loyalty.xml");
					
			if (!file.exists()) {
				return null;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(Loyalty.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Loyalty loyalty = (Loyalty) unmarshaller.unmarshal(reader);
					
			return loyalty;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
}
