package org.wit.fddl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import org.wit.fddl.sql.SqlTree;
import org.wit.fddl.sqlparser.SelectSqlParser;

public class SqlParserTest {
	
	@Test
	public void testParse(){
//		String sql = "SELECT SOURCE_TYPE AS sourceType,EVENT_ACTION AS eventAction,"
//						+ "COUNT AS count,CHECKOUT AS checkout,"
//						+ "TOTAL as total FROM report_ecommerce_shortcut "
//						+ "WHERE MINISITE_ID = 250093 AND SETTLED_TIME >= '2014-6-16'";
		//将所有字段换成小写,并不支持别名.
		String sqlFormat = "SELECT source_type,event_action,count(count),checkout,total FROM report_ecommerce_shortcut "
						+ "WHERE MINISITE_ID = 250093 AND SETTLED_TIME >= '2014-6-16'";
		
		SelectSqlParser parser = new SelectSqlParser(sqlFormat);
		SqlTree sqlTree = parser.parse();
		
		System.out.println(sqlTree.getDbExecuteSql());
	}

	@Test
	public void testSqlRemoveSymbol(){
		String sql = "select * from users where id=\"1\" ;".replaceAll("\"", "'");
		assertEquals("select * from users where id='1' ;",sql);
	}
	
	
	
	@Test
	public void testKeyWordsOrder() {
		//原始的数组 关键字在sql语句字符串的起始位置.
		int[] keyWordsOrder = new int[] { 5, 15, -1, 20, -1, -1, 25 };
		int len = keyWordsOrder.length;
		print(keyWordsOrder);
		
		//复制原始数组.
		int[] keyWordsOrderCopy = Arrays.copyOf(keyWordsOrder, len);
		print(keyWordsOrderCopy);
		
		//对复制数组排序.
		Arrays.sort(keyWordsOrderCopy);
		print(keyWordsOrderCopy);
		
		//从原始数组里过滤 为-1的数字.
		int[] keyWordsOrderFilt = new int[len];
		Arrays.fill(keyWordsOrderFilt, -1);
		print(keyWordsOrderFilt);
		
		int size = 0;
		int j = 0;
		for(int i=0;i<len;++i){
			if(keyWordsOrder[i]!=-1){
				keyWordsOrderFilt[j++] = keyWordsOrder[i];
				size++;
			}
		}
		print(keyWordsOrderFilt);
		
		int[] arr1 = Arrays.copyOfRange(keyWordsOrderFilt, 0, size);
		int[] arr2 = Arrays.copyOfRange(keyWordsOrderCopy, len-size, len);
		
		print(arr1);
		print(arr2);
		assertEquals(Arrays.toString(arr1),Arrays.toString(arr2));
	}
	
	@Test
	public void testKeyWordsOrder1() {
		//原始的数组
		int[] keyWordsOrder = new int[] { 5, 15, -1, 20, -1, -1, 25 };
		int len = keyWordsOrder.length;
		print(keyWordsOrder);
		
		//复制原始数组.
		int[] keyWordsOrderCopy = Arrays.copyOf(keyWordsOrder, len);
		print(keyWordsOrderCopy);
		
		//对复制数组排序.
		Arrays.sort(keyWordsOrderCopy);
		print(keyWordsOrderCopy);
		
		//从原始数组里过滤 为-1的数字.
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		for(int i=0;i<len;++i){
			if(keyWordsOrder[i]!=-1){
				sb1.append(keyWordsOrder[i]);
			}
			if(keyWordsOrderCopy[i]!=-1){
				sb2.append(keyWordsOrderCopy[i]);
			}
		}
		System.out.println(sb1.toString());
		System.out.println(sb2.toString());
		assertEquals(sb1.toString(),sb2.toString());
	}
	
	private void print(int[] keyWordsOrder){
		for(int keyWord: keyWordsOrder){
			System.out.print(keyWord+" ");
		}
		System.out.println();
	}
	
	

}
