package cl.bbr.jumbocl.clientes.dao;

//import javax.naming.NamingException;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

import cl.bbr.jumbocl.clientes.dao.ClientesDAO;
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
	public ClientesDAO getClientesDAO() {
	  // retorna el dao para hibernate
	  return null;	//new HibernateClientesDAO();
	}
	
	public RegionesDAO getRegionesDAO() {
		  // retorna el dao para jdb
		  return null;
		}

}
