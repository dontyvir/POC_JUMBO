package cl.bbr.jumbocl.pedidos.dao;

import cl.bbr.jumbocl.bolsas.dao.BolsasDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.eventos.dao.JdbcEventosDAO;
import cl.bbr.vte.empresas.dao.EmpresasDAO;

/**
 * Permite definir la modalidad de conexion con la base de datos, Jdbc, Hibernate, etc.
 *  
 * @author BBR
 *
 */
public abstract class DAOFactory {
	
	/**
	 * Identifica que la conexión a usar es Hibernate.  
	 */
	public static final int HIBERNATE = 1;
	  
	/**
	 * Identifica que la conexión a usar es Jdbc.
	 */
	public static final int JDBC = 2;
	  
	/**
	 * Obtiene la modalidad de conexion a la base de datos.
	 * @param  whichFactory
	 * @return DAOFactory
	 */
	public static DAOFactory getDAOFactory(
		  int whichFactory) {
	  
		switch (whichFactory) {
		  case HIBERNATE: 
			  return new HibernateDAOFactory();
		  case JDBC: 
		   	  return new JdbcDAOFactory();
		  default: 
			  return null;
		}
	  }
    
	
	/**
	 * Obtiene la clase que realiza consultas de Pedidos.
	 * 
	 * @return PedidosDAO
	 */
	public abstract PedidosDAO getPedidosDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Locales.
	 * 
	 * @return LocalDAO
	 */
	public abstract LocalDAO getLocalDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Despachos.
	 * 
	 * @return DespachosDAO
	 */
	public abstract DespachosDAO getDespachosDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Jornadas.
	 * 
	 * @return JornadasDAO
	 */
	public abstract JornadasDAO getJornadasDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Calendarios.
	 * 
	 * @return CalendarioDAO
	 */
	public abstract CalendarioDAO getCalendarioDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Rondas.
	 * 
	 * @return RondasDAO
	 */
	public abstract RondasDAO getRondasDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Zonas de despacho.
	 * 
	 * @return ZonasDespachoDAO
	 */
	public abstract ZonasDespachoDAO getZonasDespachoDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Comunas.
	 * 
	 * @return ComunasDAO
	 */
	public abstract ComunasDAO getComunasDAO();
    
	/**
	 * Obtiene la clase que realiza consultas de Transacciones de Pago.
	 * 
	 * @return TrxMedioPagoDAO
	 */
	public abstract TrxMedioPagoDAO getTrxMedioPagoDAO();	
	/**
	 * Obtiene la clase que realiza consultas de Sectores de picking.
	 * 
	 * @return SectorPickingDAO
	 */
	public abstract SectorPickingDAO getSectorPickingDAO();
	/**
	 * Obtiene la clase que realiza consultas de los Poligonos de Despacho.
	 * 
	 * @return PoligonosDAO
	 */
	public abstract PoligonosDAO getPoligonosDAO();
	/**
	 * Obtiene la clase que realiza consultas de Empresas.
	 * 
	 * @return EmpresasDAO
	 */
	public abstract EmpresasDAO getEmpresasDAO();

    /**
     * @return
     */
    public abstract JdbcEventosDAO getEventosDAO();
    
    /**
	 * Obtiene la clase que realiza consultas de Bolsas.
	 * 
	 * @return PedidosDAO
	 */
	public abstract BolsasDAO getBolsaDAO();
	
	
	public abstract ProductosDAO getProductosDAO();
	
	/**
	 * 
	 * @return
	 */
	public PedidosDAO getValidaCuponYPromocionPorIdPedido() {
		
		return new JdbcPedidosDAO();
	
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract PedidosDAO getDescuentosAplicados();
	
	/**
	 * 
	 * @return
	 */
	public PedidosDAO getValidaDespachoSinPromocionSinCupon() {
		
		return new JdbcPedidosDAO();
	
	}
	
	/**
	 * Obtiene la clase que realiza consultas de Excesos.
	 * 
	 * @return ExcesoDAO
	 */
	public abstract ExcesoDAO getExcesoDAO();
	
}
