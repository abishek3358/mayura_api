package com.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.app.entity.User;
import com.app.enumeration.UserRoleType;

@Repository
public class UserDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	public List<User> getVendorStaff() {
		
		Session session = sessionFactory.getCurrentSession();

		String sqlQurey = "SELECT u.user_id ,u.user_name ,u.email ,u.phone_no ,u.user_role ,u.user_type "
				+ " FROM users u "
				+ " WHERE u.user_type = 'VENDOR_STAFF' ";

		Query query = session.createNativeQuery(sqlQurey);
		List<Object[]> objectList = query.getResultList();
		User Obj = null;
		List<User> DtoList = new ArrayList<User>();
		for (Object[] columns : objectList) {

			Obj = new User();
			Obj.setUserId(UUID.fromString(columns[0].toString()));
			Obj.setUserName(columns[1].toString());
			Obj.setEmail(columns[2].toString());
			Obj.setMobileNo(columns[3].toString());
			Obj.setRoleId(UUID.fromString(columns[4].toString()));
			Obj.setUserRole(UserRoleType.valueOf(columns[5].toString()));

			DtoList.add(Obj);

		}
		
		
		
		return DtoList;
	}
	
	
}

