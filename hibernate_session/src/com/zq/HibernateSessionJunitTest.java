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
	public void testSessionCache(){
		User user2 = (User) session.get(User.class, 1);
		System.out.println(user2.toString());
		User user3 = (User) session.get(User.class, 1);
		System.out.println(user3.toString());
	}
	
	/**
	 * flush():ʹ���ݿ���еļ�¼��session�����еļ�¼����һ�£�
	 * Ϊ�˱���һ�£�����ܻᷢ�Ͷ�Ӧ��sql���
	 * 1����Transaction��commit()�����У��ȵ���session��flush���������ύ����
	 * 2��flush()���ܻᷢ��sql��䣬�������ύ����
	 * 3��ע�⣺��δ�ύ�������ʾ����session.flush()����֮ǰ��Ҳ�п��ܻ����flush()����
	 * 1)��ִ��HQL��QBC��ѯ�����Ƚ���flush()�������Եõ����ݱ�����µļ�¼
	 * 2)������¼��ID���ɵײ����ݿ�ʹ�������ķ�ʽ���ɵģ����ڵ���save()�����󣬾ͻ���������insert��䣬
	 * ��Ϊsave��������뱣֤�����ID�Ǵ��ڵ�
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
	 * refresh()����ǿ�Ʒ���select��䣬��ʹSession�����ж����״̬�����ݿ���ж�Ӧ�ļ�¼����һ��
	 */
	@Test
	public void testSessionRefresh(){
		User user = (User) session.get(User.class, 1);
		System.out.println(user);
		session.refresh(user);
		System.out.println(user);
	}
	
	/**
	 * clear():������
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
		//�ύ����
		transaction.commit();
		//�ر�session
		session.close();
		//�ر�sessionFactory
		sessionFactory.close();
	}
	
}
