package cl.bbr.irs.promolib.dao;

//import javax.naming.NamingException;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

//import cl.bbr.jumbocl.shared.hibernate.HibernateUtil;

/**
 * Factory para Hibernate. 
 *  
 * @author BBR e-commerce & retail
 *
 */
public class HibernateDAOFactory extends DAOFactory {

/*	private static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() throws HibernateException, NamingException
	{
		if(sessionFactory == null)
		{
			// solo por el WAS
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		return sessionFactory;
	}

	public static Session getSession() throws HibernateException, NamingException
	{
		return getSessionFactory().openSession();
	}
*/
	/**
	 * metodos del DAO 
	 * 
	 */
	public IrsPromocionesDAO getPromocionesDAO() {
		return null;
	}
	
}
