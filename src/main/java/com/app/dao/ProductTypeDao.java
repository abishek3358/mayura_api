package com.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.GstResponse;
import com.app.dto.ProductTypeResponse;

@Repository
public class ProductTypeDao {

	@Autowired
	SessionFactory sessionFactory;

	public List<ProductTypeResponse> findByGet() {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT pt.id, pt.name, pt.description, pt.status\r\n" 
		+ "FROM product_type pt "
				+ "ORDER BY pt.status";
		// + "ORDER BY b.branch_name";

		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<ProductTypeResponse> list = new ArrayList<ProductTypeResponse>();
		ProductTypeResponse obj = null;
		for (Object[] row : rows) {
			obj = new ProductTypeResponse();
			obj.setId(UUID.fromString(row[0].toString()));
			obj.setName(row[1].toString());
			obj.setDescription(row[2].toString());
            obj.setStatus(row[3].toString());

			list.add(obj);
		}

		return list;

	}
}
