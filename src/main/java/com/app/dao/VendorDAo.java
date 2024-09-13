package com.app.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.VendorUpdateDTO;
import com.app.entity.Vendor;

@Repository
public class VendorDAo {
	@Autowired
	SessionFactory sessionFactory;

		
	public Vendor findByphone(String phoneNumber, UUID id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Vendor.class);
		crit.add(Restrictions.eq("phoneNumber", phoneNumber));
		
		crit.add(Restrictions.ne("id", id));
		List<Vendor> list = crit.list();
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {

		}
		return null;
	}


	public Vendor findByemails(String emailId, UUID id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Vendor.class);
		crit.add(Restrictions.eq("emailId", emailId));
		
		crit.add(Restrictions.ne("id", id));
		List<Vendor> list = crit.list();
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {

		}
		return null;
	}




}
