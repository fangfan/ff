package org.wit.fddl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import org.wit.fddl.model.NonSon;
import org.wit.fddl.model.Son;

/**
 * getDeclareMethod只能取本类的方法.
 * getMethod可以取到父类的方法.
 * @author F.Fang
 *
 */
public class ClassReflectMethodTest {
	
	@Test
	public void testGetMethod(){
		try {
			//Method m = Son.class.getMethod("eat", null);
			Method m = Son.class.getMethod("eat", new Class[]{});
			assertNotNull(m);
		} catch (Exception e) {
			assertFalse(true);
		}
	}
	
	@Test
	public void testGetDeclareMethod(){
		try {
			//NonSon.class.getDeclaredMethod("eat", null);
			NonSon.class.getDeclaredMethod("eat", new Class[]{});
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	
	@Test
	public void testField(){
		try {
			System.out.println(Son.class.getFields());
			System.out.println(FieldUtils.getField(Son.class, "id",true).getType());
			System.out.println(FieldUtils.getField(Son.class, "id", true));
//			System.out.println(Son.class.getDeclaredField("name"));
//			System.out.println(Son.class.getField("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
