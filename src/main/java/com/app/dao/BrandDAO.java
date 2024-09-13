package com.app.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.entity.Brand;

@Repository
public class BrandDAO {

	@Autowired
	SessionFactory sessionFactory;

	public Brand findByNameId(String brandName, UUID id) {
		
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Brand.class);
		crit.add(Restrictions.eq("brandName", brandName));
		crit.add(Restrictions.ne("id", id));
		List<Brand> list = crit.list();
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
		}

		
		
		return null;
	}
	
	
}
