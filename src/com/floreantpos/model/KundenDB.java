package com.floreantpos.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.CustomerDAO;

@XmlRootElement(name = "Kunde")
public class KundenDB {
	private List<Kunden> kunden;

	public void setKunden(List<Kunden> kunden) {
		this.kunden = kunden;
	}
	
	public List<Kunden> getKunden()
	{
		return this.kunden;
	}
	public static boolean exportKunden(File file) {
	try {

				
		JAXBContext jaxbContext = JAXBContext.newInstance(KundenDB.class);
		Marshaller m = jaxbContext.createMarshaller();
		OutputStream outputStream = new FileOutputStream(file);
		m.setProperty(Marshaller.JAXB_ENCODING, "iso-8859-1");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "ISO-8859-1");
		
		List<Customer> customerList = CustomerDAO.getInstance().findAll();
		KundenDB kundenDb = new KundenDB();
		List<Kunden> kundenList = new ArrayList();
		for(Iterator itr = customerList.iterator();itr.hasNext();)
		{
			Customer customer = (Customer) itr.next();
			Kunden kunden = new Kunden();
			kunden.setAddresse(customer.getAddress());
			kunden.setBezirk(customer.getBezerk());
			kunden.setDob(customer.getDob());
			kunden.setEmail(customer.getEmail());
			kunden.setFirma(customer.getFirmName());
			kunden.setKlingel(customer.getBellName());
			kunden.setKundenNr(customer.getLoyaltyNo());
			kunden.setName(customer.getName());
			kunden.setPlz(customer.getZipCode());
			kunden.setSalutation(customer.getSalutation());
			kunden.setStadt(customer.getCity());
			kunden.setStrasseNr(customer.getDoorNo());
			kunden.setTelefon(customer.getTelephoneNo());
			kunden.setZusatz(customer.getLandMark());
			kundenList.add(kunden);
		}
		
		
		kundenDb.setKunden(kundenList);
		m.marshal(kundenDb, writer);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	}

	public static boolean importKunden(File file) 
	{
		try {

			if (!file.exists()) {	
				return false;
			}

			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "iso-8859-1");
			JAXBContext jaxbContext = JAXBContext.newInstance(KundenDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			KundenDB kundenDB = (KundenDB) unmarshaller.unmarshal(reader);
			
			if(kundenDB != null)
			{
				List<Kunden> kundenList = kundenDB.getKunden();
				for(Iterator<Kunden> itr = kundenList.iterator();itr.hasNext();)
				{
					Kunden kunden = itr.next();
					Customer customer = new Customer();
					customer.setName(kunden.getName().toUpperCase());
					customer.setAddress(kunden.getAddresse().toUpperCase());
					customer.setLoyaltyNo(kunden.getKundenNr());
					customer.setTelephoneNo(kunden.getTelefon());
					customer.setEmail(kunden.getEmail().toUpperCase());
					customer.setDob(kunden.getDob());
					customer.setCity(kunden.getStadt().toUpperCase());
					customer.setBellName(kunden.getKlingel().toUpperCase());
					customer.setLandMark(kunden.getZusatz().toUpperCase());
					customer.setCreditLimit(500.00);
					customer.setFirmName(kunden.getFirma().toUpperCase());
					customer.setSalutation(kunden.getSalutation());
					customer.setDoorNo(kunden.getStrasseNr());
					customer.setBezerk(kunden.getBezirk().toUpperCase());
					customer.setZipCode(kunden.getPlz());
					CustomerDAO.getInstance().saveOrUpdate(customer);
				}
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
