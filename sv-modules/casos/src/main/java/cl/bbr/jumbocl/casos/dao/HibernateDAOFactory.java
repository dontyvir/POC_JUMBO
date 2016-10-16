package cl.bbr.jumbocl.casos.dao;


//import javax.naming.NamingException;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

//import cl.bbr.jumbocl.shared.hibernate.HibernateUtil;

/**
 * Clase que maneja la sesión para la conexión con la base de datos mediante Hibernate.
 * 
 * @author BBR
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
   
	public CasosDAO getCasosDAO() {
		
		return null;
	}


}
