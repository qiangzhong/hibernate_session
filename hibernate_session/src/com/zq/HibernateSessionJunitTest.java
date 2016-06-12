package com.zq;


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
	public void test(){
		
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
