package com.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.UnitOfMeasurementResponse;

@Repository
public class UnitOfMeasurementDao {
	
	@Autowired
	SessionFactory sessionFactory;

	public List<UnitOfMeasurementResponse> findByGetAll() {
		Session session = sessionFactory.getCurrentSession();
		String sqlQuery = "SELECT u.id, u.name, u.short_name, u.status\r\n"
				+ " FROM unit_of_measurement u "
				+ "ORDER BY u.status";
				//+ "ORDER BY b.branch_name";

		Query query = session.createSQLQuery(sqlQuery);
		List<Object[]> rows = query.list();
		List<UnitOfMeasurementResponse> list = new ArrayList<UnitOfMeasurementResponse>();
		UnitOfMeasurementResponse obj = null;
		for (Object[] row : rows) {
			
			obj = new UnitOfMeasurementResponse();
			obj.setId(UUID.fromString(row[0].toString()));
			obj.setName(row[1].toString());
			obj.setShortName(row[2].toString());
			obj.setStatus(row[3].toString());
			
           list.add(obj);
		}

		return list;


	}
}
