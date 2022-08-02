package com.floreantpos.model;

import java.util.Comparator;

import com.floreantpos.model.base.BaseTSEReceiptData;

public class TSEReceiptData extends BaseTSEReceiptData{

	private static final long serialVersionUID = 1L;
	public static class tseReceiptComparator implements Comparator {
		public tseReceiptComparator(){}
	    public int compare(Object o1, Object o2) {
	      if (!(o1 instanceof Customer) || !(o2 instanceof TSEReceiptData))
	        throw new ClassCastException();

	      TSEReceiptData e1 = (TSEReceiptData) o1;
	      TSEReceiptData e2 = (TSEReceiptData) o2;

	      return (int) (e1.getId() - e2.getId());
    }
	}
/*[CONSTRUCTOR MARKER BEGIN]*/
	public TSEReceiptData () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TSEReceiptData (java.lang.Integer id) { 
		super(id);
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
