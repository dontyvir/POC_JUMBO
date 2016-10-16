package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.bolsas.dao.BolsasDAO;
import cl.bbr.jumbocl.bolsas.dao.JdbcBolsasDAO;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.eventos.dao.JdbcEventosDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.vte.empresas.dao.EmpresasDAO;
import cl.bbr.vte.empresas.dao.JdbcEmpresasDAO;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 * @author BBR
 *
 */
public class JdbcDAOFactory extends DAOFactory {

	/**
	 * Permite la conexion con la base de datos. 
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();
	
	/**
	 * Obtiene la conexión JDBC con la base de datos.
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException 
	{
		//	Recommend connection pool implementation/usage
		return conexionutil.getConexion();
	}

	//****************** metodos del DAO *******************//

	public PedidosDAO getPedidosDAO() {
		//	JdbcPedidosDAO implements PedidosDAO
		return new JdbcPedidosDAO();
	}

	public LocalDAO getLocalDAO() {
		return new JdbcLocalDAO();
	}

	public DespachosDAO getDespachosDAO() {
		return new JdbcDespachosDAO();
	}

	public CalendarioDAO getCalendarioDAO() {
		return new JdbcCalendarioDAO();
	}
	
	public RondasDAO getRondasDAO() {
		return new JdbcRondasDAO();
	}

	public ZonasDespachoDAO getZonasDespachoDAO() {
		return new JdbcZonasDespachoDAO();
	}
	
	public ComunasDAO getComunasDAO() {
		return new JdbcComunasDAO();
	}

	public TrxMedioPagoDAO getTrxMedioPagoDAO() {
		return new JdbcTrxMedioPagoDAO();
	}

	public JornadasDAO getJornadasDAO() {
		return new JdbcJornadasDAO();
	}

	public SectorPickingDAO getSectorPickingDAO() {		
		return new JdbcSectorPickingDAO();
	}

	public PoligonosDAO getPoligonosDAO() {		
		return new JdbcPoligonosDAO();
	}

	public EmpresasDAO getEmpresasDAO() {		
		return new JdbcEmpresasDAO();
	}

    /* (sin Javadoc)
     * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getEventosDAO()
     */
    public JdbcEventosDAO getEventosDAO() {
        return new JdbcEventosDAO();
    }

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getBolsaDAO()
	 */
	public BolsasDAO getBolsaDAO() {
		return new JdbcBolsasDAO();
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.DAOFactory#getProductosDAO()
	 */
	public ProductosDAO getProductosDAO() {
		return new JdbcProductosDAO();
	}

	public PedidosDAO getDescuentosAplicados() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcPedidosDAO();
	}
	
	public ExcesoDAO getExcesoDAO() {
		return new JdbcExcesoDAO();
	}
	
}
