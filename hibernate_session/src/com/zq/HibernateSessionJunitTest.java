package com.zq;


import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateSessionJunitTest {
	private Session session;
	
	private SessionFactory sessionFactory;
	
	private Transaction transaction;
	
	@SuppressWarnings("deprecation")
	@Before
	public void init(){
		//映射数据库配置信息以及对象关系映射文件
		Configuration configuration = new Configuration().configure();
		//任何服务配置都要进行服务注册后才能生效
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		//创建sessionFactory对象
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		//创建session
		session = sessionFactory.openSession();
		//开启事务
		transaction = session.beginTransaction();
	}
	
	@Test
	public void testSessionCache(){
		User user2 = (User) session.get(User.class, 1);
		System.out.println(user2.toString());
		User user3 = (User) session.get(User.class, 1);
		System.out.println(user3.toString());
	}
	
	/**
	 * flush():使数据库表中的记录与session缓存中的记录保持一致，
	 * 为了保持一致，则可能会发送对应的sql语句
	 * 1、在Transaction的commit()方法中：先调用session的flush方法，再提交事务
	 * 2、flush()可能会发送sql语句，但不会提交事务
	 * 3、注意：在未提交事务或显示调用session.flush()方法之前，也有可能会进行flush()操作
	 * 1)、执行HQL或QBC查询，会先进行flush()操作，以得到数据表的最新的记录
	 * 2)、若记录的ID是由底层数据库使用自增的方式生成的，则在调用save()方法后，就会立即发送insert语句，
	 * 因为save方法后必须保证对象的ID是存在的
	 */
	@Test
	public void testSessionflush(){
		User user = (User) session.get(User.class, 1);
		user.setName("Tom");
		@SuppressWarnings("unused")
		User user1 = (User) session.createCriteria(User.class).uniqueResult();
		
	}
	
	@Test
	public void testSessionFlush2(){
		User user = new User();
		user.setName("Jim");
		user.setAge(13);
		session.save(user);
		System.out.println(user);
	}
	
	/**
	 * refresh()：会强制发送select语句，以使Session缓存中对象的状态和数据库表中对应的记录保持一致
	 */
	@Test
	public void testSessionRefresh(){
		User user = (User) session.get(User.class, 1);
		System.out.println(user);
		session.refresh(user);
		System.out.println(user);
	}
	
	/**
	 * clear():清理缓存
	 */
	@Test
	public void testclear(){
		User user = (User) session.load(User.class, 1);
		System.out.println(user);
		session.clear();
		User user2 = (User) session.load(User.class, 1);
		System.out.println(user2);
	}
	@After
	public void destory(){
		//提交事务
		transaction.commit();
		//关闭session
		session.close();
		//关闭sessionFactory
		sessionFactory.close();
	}
	
}
