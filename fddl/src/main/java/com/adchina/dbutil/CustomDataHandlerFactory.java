package com.adchina.dbutil;

import java.util.Arrays;

import com.adchina.dbutil.datahandle.CustomDataHandler;

/**
 * 非Spring方式.
 * CustomDataHandlerFactory.getInstance(new String[]{}).query(executor);
 * @author F.Fang
 *
 */
public class CustomDataHandlerFactory {
	
	public static CustomDataHandler getInstance(String[] dbs){
		return new CustomDataHandler(Arrays.asList(dbs));
	}

}
