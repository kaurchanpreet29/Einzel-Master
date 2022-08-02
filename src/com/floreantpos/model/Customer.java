package com.floreantpos.model;

import java.util.Comparator;
import java.util.HashMap;

import com.floreantpos.model.base.BaseCustomer;

public class Customer extends BaseCustomer {
	private static final long serialVersionUID = 1L;
	public static class customerComparator implements Comparator {
		public customerComparator(){}
	    public int compare(Object o1, Object o2) {
	      if (!(o1 instanceof Customer) || !(o2 instanceof Customer))
	        throw new ClassCastException();

	      Customer e1 = (Customer) o1;
	      Customer e2 = (Customer) o2;
	      return (int) (Integer.parseInt(e1.getLoyaltyNo()) - Integer.parseInt(e2.getLoyaltyNo()));
    }
	}
	
	public static class customerNameComparator implements Comparator {
		public customerNameComparator(){}
		
	    public int compare(Object o1, Object o2) {
	     
	    	if (!(o1 instanceof Customer) || !(o2 instanceof Customer))
	        throw new ClassCastException();

	      Customer e1 = (Customer) o1;
	      Customer e2 = (Customer) o2;
	    
	      return e1.getLoyaltyNo().compareTo(e2.getLoyaltyNo());
	   
	    	
    }
	}
/*[CONSTRUCTOR MARKER BEGIN]*/
	public Customer () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Customer (java.lang.Integer autoId) { 
		super(autoId);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public void addProperty(String name, String value) {
		if(getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}
		
		getProperties().put(name, value);
	}
	
	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}
	
	public String getProperty(String key) {
		if(getProperties() == null) {
			return null;
		}
		
		return getProperties().get(key);
	}
	
	@Override
	public String toString() {
		String name = getName();
		return name;
	}
}