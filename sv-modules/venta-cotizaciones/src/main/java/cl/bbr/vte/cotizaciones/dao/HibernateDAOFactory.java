package cl.bbr.vte.cotizaciones.dao;

//import javax.naming.NamingException;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

//import cl.bbr.jumbocl.shared.hibernate.HibernateUtil;
import cl.bbr.vte.empresas.dao.EmpresasDAO;

public class HibernateDAOFactory extends DAOFactory {

/*	private static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() throws HibernateException, NamingException
	{
		if(sessionFactory == null)
		{
			// solo por el WAS
			HibernateUtil hutils = new HibernateUtil();
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
	public CotizacionesDAO getCotizacionesDAO(){
		return null;
	}

	public EmpresasDAO getEmpresasDAO(){
		return null;
	}
}
