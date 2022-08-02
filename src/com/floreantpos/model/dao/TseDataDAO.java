
package com.floreantpos.model.dao;

import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.TSEReceiptData;

import antlr.collections.List;

public class TseDataDAO  extends BaseTSEReceiptDataDAO {
 
	public TseDataDAO () {}
	
	public  TSEReceiptData findById()
	{
		Session session = null;

		try {
			String lTseNr="";
			if(TerminalConfig.getLastTseNr()!=null&&Integer.valueOf(TerminalConfig.getLastTseNr())>0) {
				 lTseNr=TerminalConfig.getLastTseNr();
			} else {
				 lTseNr="1";
			}
			
			
			
		    int highNr =Integer.valueOf(lTseNr);
			
			Random r = new Random();
			int low = 0;
			int high = highNr;
			int result = r.nextInt(high-low) + low;
		   
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.addOrder( Order.desc("id") );	
			criteria.setFirstResult(result);
			criteria.setMaxResults(1);			
			return (TSEReceiptData) criteria.uniqueResult();
			
		/*	session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());			
			 criteria.addOrder( Order.desc("id") );					
			 criteria.setMaxResults(1);			
			return (TSEReceiptData) criteria.uniqueResult();*/
						
		} finally {
			closeSession(session);
		}
	}
}