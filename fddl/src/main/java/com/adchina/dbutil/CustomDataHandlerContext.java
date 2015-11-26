package com.adchina.dbutil;

import com.adchina.dbutil.datahandle.CustomDataHandlerForJdbc;
import com.adchina.dbutil.sqlhandler.QuerySqlJdbcHandler;

/**
 * Spring方式.
 * @author F.Fang
 *
 */
public class CustomDataHandlerContext {
	
	private static CustomDataHandlerForJdbc customDataHandler;
	
	private static QuerySqlJdbcHandler querySqlJdbcHandler;
	
	public static CustomDataHandlerForJdbc getInstance(){
		return customDataHandler;
	}

	public static void setCustomDataHandler(CustomDataHandlerForJdbc customDataHandler) {
		CustomDataHandlerContext.customDataHandler = customDataHandler;
	}

    public static QuerySqlJdbcHandler getQuerySqlJdbcHandler() {
        return querySqlJdbcHandler;
    }

    public static void setQuerySqlJdbcHandler(QuerySqlJdbcHandler querySqlJdbcHandler) {
        CustomDataHandlerContext.querySqlJdbcHandler = querySqlJdbcHandler;
    }
	
	

}
