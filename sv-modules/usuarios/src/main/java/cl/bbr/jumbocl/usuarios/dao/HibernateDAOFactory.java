package cl.bbr.jumbocl.usuarios.dao;

//import javax.naming.NamingException;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

//import cl.bbr.jumbocl.shared.hibernate.HibernateUtil;
import cl.bbr.jumbocl.usuarios.dao.PerfilesDAO;
import cl.bbr.jumbocl.usuarios.dao.UsuariosDAO;

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
			//HibernateUtil hutils = new HibernateUtil();
			//
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
	
	public UsuariosDAO getUsuariosDAO() {
		
		return null;
	}

	public PerfilesDAO getPerfilesDAO() {
		
		return null;
	}


	
}
