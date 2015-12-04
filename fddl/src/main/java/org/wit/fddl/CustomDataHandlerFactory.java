package org.wit.fddl;

import java.util.Arrays;

import org.wit.fddl.datahandle.CustomDataHandler;

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
