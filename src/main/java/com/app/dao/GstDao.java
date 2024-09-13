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
import com.app.entity.GST;

@Repository
public class GstDao {
	
	@Autowired
	SessionFactory sessionFactory;

	public List<GstResponse> findByGet() {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT g.id, g.name, g.gst_percentage, g.cgst_percentage, g.sgst_percentage, g.igst_percentage, g.description, g.status\r\n"
				+ "FROM gst g "
				+ "ORDER BY g.status";
				//+ "ORDER BY b.branch_name";

		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<GstResponse> list = new ArrayList<GstResponse>();
		GstResponse obj = null;
		for (Object[] row : rows) {
			obj = new GstResponse();
			obj.setId(UUID.fromString(row[0].toString()));
			obj.setName(row[1].toString());
			obj.setGst(Double.valueOf(row[2].toString()));
			obj.setCgst(Double.valueOf(row[3].toString()));
			obj.setSgst(Double.valueOf(row[4].toString()));
			obj.setIgst(Double.valueOf(row[5].toString()));
			obj.setDescription(row[6].toString());
			obj.setStatus(row[7].toString());

           list.add(obj);
		}

		return list;


	}
}
