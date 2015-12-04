package org.wit.fddl.service;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 测试用户服务
 * @author F.Fang
 *
 */

@ContextConfiguration( "classpath:applicationContext-jdbc.xml" )
public class UserServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void testConfig(){
//		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
//		assertNotNull(context);
	}

	@Test
	public void testGetUser() {
		UserService userService = (UserService) applicationContext.getBean("userService");
		
		//System.out.println(userService.queryUser("select id,username,password from users",new Object[]{}));
		
		//System.out.println(userService.queryUser("select id,username,password from users where id=?",new Object[]{"222"}));
		
		//System.out.println(userService.queryUser("select id,max(username),min(password) from users where id=? group by id order by id desc limit 0,1",new Object[]{"222"}));
		System.out.println(userService.queryUser("select id,min(password) as mpwd from users group by id,username order by mpwd desc",new Object[]{}));
	}
	
	@Test
	public void testGetUserEmptyResult(){
		UserService userService = (UserService) applicationContext.getBean("userService");
		
		System.out.println(userService.queryUser("select id,username,password from users where id=?",new Object[]{"333"}));
	}
	
	@Test
	public void testGetUserGroupBy(){
		UserService userService = (UserService) applicationContext.getBean("userService");
		
		//System.out.println(userService.queryUser("select id,max(username),min(password) from users group by id",new Object[]{}));
		//System.out.println(userService.queryUser("select id,max(username),min(password) from users group by id",new Object[]{}));
		//System.out.println(userService.queryUser("select id,min(username),min(password) from users group by id",new Object[]{}));
		//System.out.println(userService.queryUser("select id,min(password) from users group by id",new Object[]{}));
		
		System.out.println(userService.queryUser("select id,min(password) from users group by id,username order by id",new Object[]{}));
	}
	
	@Test
	public void testGetUserByIn(){
	    UserService userService = (UserService) applicationContext.getBean("userService");
	   // System.out.println(userService.queryUser("select id,username,password from users where id in('111','222','333')",new Object[]{}));
	    //System.out.println(userService.queryUser("select id,username,password from users where id in(?,?,?)",new Object[]{"111","222","333"}));
	    System.out.println(userService.queryUser("select id,name,pay from audience where id in(?)",new Object[]{"1,2"}));
	}
	
	@Test
	public void testGetUserMax(){
		UserService userService = (UserService) applicationContext.getBean("userService");
		System.out.println(userService.queryUser("select count(id) from users",new Object[]{}));

	}
	
	@Test
	public void testGetUserOrderBy(){
		UserService userService = (UserService) applicationContext.getBean("userService");
		//System.out.println(userService.queryUser("select id,username,max(password) from users group by id,username order by id asc,username asc",new Object[]{}));
		System.out.println(userService.queryUser("select id,username,password from users order by id asc,username asc,password desc",new Object[]{}));
		
	}
	
	@Test
	public void testGetUserAlias(){
		UserService userService = (UserService) applicationContext.getBean("userService");
		//System.out.println(userService.queryUser("select id,username,max(password) from users group by id,username order by id asc,username asc",new Object[]{}));
		//System.out.println(userService.queryUser("select id,username as 'from',password from users where 1=1;",new Object[]{}));
		System.out.println(userService.queryUser("select id,sum(username) as username from users order by id",new Object[]{}));
	}

}
