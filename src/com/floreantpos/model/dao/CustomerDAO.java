package com.floreantpos.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Customer;

public class CustomerDAO extends BaseCustomerDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CustomerDAO() {
	}

	public List<Customer> findBy(String phone, String loyalty, String name,String zipCode,String Address,String city,String firmName,String phone2) {
		Session session = null;		

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();
			
			if(StringUtils.isNotEmpty(phone))
				disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO, "%" + phone + "%"));			

			if(StringUtils.isNotEmpty(phone))
				disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO_2, "%" + phone2 + "%"));
			
			if(StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));
			
			if(StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.like(Customer.PROP_NAME, "%" + name + "%"));
			
			if(StringUtils.isNotEmpty(zipCode))
				disjunction.add(Restrictions.like(Customer.PROP_ZIP_CODE, "%" + zipCode + "%"));
			
			if(StringUtils.isNotEmpty(Address))
				disjunction.add(Restrictions.like(Customer.PROP_ADDRESS, "%" + Address + "%"));
			
			if(StringUtils.isNotEmpty(city))
				disjunction.add(Restrictions.like(Customer.PROP_CITY, "%" + city + "%"));
			
			if(StringUtils.isNotEmpty(firmName))
				disjunction.add(Restrictions.like(Customer.PROP_FIRM_NAME, "%" + firmName + "%"));
			
			criteria.add(disjunction);
			
			return criteria.list();
			
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
		public List<Customer> findBy(String phone, String loyalty, String name) {
		Session session = null;
		

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();
			
			if(StringUtils.isNotEmpty(phone))
				disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO, "%" + phone + "%"));
			
			if(StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));
			
			criteria.add(disjunction);
			
			return criteria.list();
			
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	
	}
		
		public Customer findByLoyalty(String loyalty) {
			Session session = null;	

			try {
				session = getSession();
				Criteria criteria = session.createCriteria(getReferenceClass());
				Disjunction disjunction = Restrictions.disjunction();
				
				if(StringUtils.isNotEmpty(loyalty))
					disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));				
				criteria.add(disjunction);
				
				return (Customer) criteria.uniqueResult();
				
			} finally {
				if (session != null) {
					closeSession(session);
				}
			}

		
		}
		
		
		public List<Customer> findBy2(String phone, String loyalty, String name) {
			Session session = null;
			

			try {
				session = getSession();
				Criteria criteria = session.createCriteria(getReferenceClass());
				Disjunction disjunction = Restrictions.disjunction();
				
				if(StringUtils.isNotEmpty(phone))
					disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO_2, "%" + phone + "%"));
				
				if(StringUtils.isNotEmpty(loyalty))
					disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));
				
				criteria.add(disjunction);
				
				return criteria.list();
				
			} finally {
				if (session != null) {
					closeSession(session);
				}
			}

		
		}
	public List<Customer> findByPhoneName(String phone, String name) {
			Session session = null;
			

			try {
				session = getSession();
				Criteria criteria = session.createCriteria(getReferenceClass());
				Disjunction disjunction = Restrictions.disjunction();
				
				if(StringUtils.isNotEmpty(phone))
					disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO, "%" + phone + "%"));
				if(StringUtils.isNotEmpty(name))
					disjunction.add(Restrictions.like(Customer.PROP_NAME, "%" + name + "%"));
				criteria.add(disjunction);
				
				return criteria.list();
				
			} finally {
				if (session != null) {
					closeSession(session);
				}
			}
		}
}