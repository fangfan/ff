package com.adchina.dbutil.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adchina.dbutil.CustomDataHandlerContext;

/**
 * 用户服务.
 * 
 * @author F.Fang
 * 
 */
@Service(value = "userService")
public class UserService {

	@Transactional(readOnly = true)
	public Object queryUser(final String sql, final Object[] args) {
		//return CustomDataHandlerContext.getInstance().query(sql, args);
	    return CustomDataHandlerContext.getQuerySqlJdbcHandler().query(sql, args);
	}
}
