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
		//ӳ�����ݿ�������Ϣ�Լ������ϵӳ���ļ�
		Configuration configuration = new Configuration().configure();
		//�κη������ö�Ҫ���з���ע��������Ч
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		//����sessionFactory����
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		//����session
		session = sessionFactory.openSession();
		//��������
		transaction = session.beginTransaction();
	}
	
	@Test
	public void test(){
		
	}
	
	@After
	public void destory(){
		//�ύ����
		transaction.commit();
		//�ر�session
		session.close();
		//�ر�sessionFactory
		sessionFactory.close();
	}
	
}
