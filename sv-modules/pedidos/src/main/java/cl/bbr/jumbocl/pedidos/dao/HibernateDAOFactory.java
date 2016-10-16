package cl.bbr.jumbocl.pedidos.dao;

import cl.bbr.jumbocl.bolsas.dao.BolsasDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.eventos.dao.JdbcEventosDAO;
import cl.bbr.vte.empresas.dao.EmpresasDAO;

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
   
	public PedidosDAO getPedidosDAO() {
		
		return null;
	}

	public LocalDAO getLocalDAO() {
		
		return null;
	}
	
	public DespachosDAO getDespachosDAO() {
		
		return null;
	}

	public CalendarioDAO getCalendarioDAO() {
		
		return null;
	}
	
	public RondasDAO getRondasDAO() {
		
		return null;
	}

	public ZonasDespachoDAO getZonasDespachoDAO() {
		
		return null;
	}

	public ComunasDAO getComunasDAO() {
		
		return null;
	}

	public TrxMedioPagoDAO getTrxMedioPagoDAO() {
		
		return null;
	}

	public JornadasDAO getJornadasDAO() {
		
		return null;
	}

	public SectorPickingDAO getSectorPickingDAO() {		
		return null;
	}
	

	public EmpresasDAO getEmpresasDAO() {		
		return null;
	}

	public PoligonosDAO getPoligonosDAO() {		
		return null;
	}

    /* (sin Javadoc)
     * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getEventosDAO()
     */
    public JdbcEventosDAO getEventosDAO() {
        return null;
    }

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getBolsaDAO()
	 */
	public BolsasDAO getBolsaDAO() {
		return null;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getProductosDAO()
	 */
	public ProductosDAO getProductosDAO() {
		return null;
	}

	public PedidosDAO getDescuentosAplicados() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}
		
	public ExcesoDAO getExcesoDAO() {
		return null;
	}

}
