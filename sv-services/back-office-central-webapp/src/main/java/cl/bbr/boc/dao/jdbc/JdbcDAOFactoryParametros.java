package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.boc.dao.CambiaEstadoValidadoDAO;
import cl.bbr.boc.dao.CargarColaboradoresDAO;
import cl.bbr.boc.dao.CatalogacionMasivaDAO;
import cl.bbr.boc.dao.CriterioSustitucionDAO;
import cl.bbr.boc.dao.CuponesDCDAO;
import cl.bbr.boc.dao.ParametrosEditablesDCDAO;
import cl.bbr.boc.dao.SeccionesExcluidasDCDAO;
import cl.bbr.boc.dao.StockOnLineDAO;
import cl.bbr.boc.dao.UmbralDAO;
import cl.bbr.jumbocl.contenidos.dao.CampanaDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasSapDAO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.ElementoDAO;
import cl.bbr.jumbocl.contenidos.dao.EstadosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosSapDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 * 
 * @author RMI - DNT
 * 
 */
public class JdbcDAOFactoryParametros extends DAOFactory {

	/**
	 * Permite la conexion con la base de datos.
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();

	/**
	 * Obtiene la conexión JDBC con la base de datos.
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException {
		return conexionutil.getConexion();
	}

	/**
	 * Métodos del DAO
	 * 
	 */

	public UmbralDAO getUmbralDAO() {
		return new JdbcUmbral();
	}

	public CargarColaboradoresDAO getCargarColaboradoresDAO() {
		return new JdbcCargarColaboradoresDAO();
	}

	public ParametrosEditablesDCDAO getParametrosEditablesDCDAO() {
		return new JdbcParametrosEditablesDCDAO();
	}
	
	public CriterioSustitucionDAO getCriterioSustitucionDAO() {
		return new JdbcCriterioSustitucionDAO();
	}
	
	public CambiaEstadoValidadoDAO getCambiaEstadoValidadoDAO() {
		return new JdbcCambiaEstadoValidadoDAO();
	}
	
	public StockOnLineDAO getStockOnLineDAO() {
		return new JdbcStockOnLineDAO();
	}

	public CategoriasDAO getCategoriasDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public ProductosDAO getProductosDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public ProductosSapDAO getProductosSapDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public CategoriasSapDAO getCategoriasSapDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public EstadosDAO getEstadosDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public CampanaDAO getCampanaDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public ElementoDAO getElementoDAO() {
		// TODO Apéndice de método generado automáticamente
		return null;
	}
	
	/**
	 * @return JdbcSeccionesExcluidasDCDAO
	 */
	public SeccionesExcluidasDCDAO getSeccionesExluidasDCDAO(){
		return new JdbcSeccionesExcluidasDCDAO();
	}
	
	public CuponesDCDAO getListaTiposCupones() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO setGuardarCupon() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getDatoCuponPorId() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getDatoCuponPorCodigo() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getListaRubros() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getListaSecciones() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	
	public CuponesDCDAO setCargaRutMasiva() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getCantidadRutAsociado() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO setCuponAsociarTipo() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CuponesDCDAO getListaCuponAsociado() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}

	public JdbcCuponesDCDAO setTodasLasSeccionesAsociado() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public JdbcCuponesDCDAO setBorrarAsociacionesPorIdCupon() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponesDCDAO();
	}
	
	public CatalogacionMasivaDAO getCatalogacionMasivaDAO() {
		return new JdbcCatalogacionMasivaDAO();
	}
	
	public JdbcCargaSapMasivaDAO getCargaSapMasivaDAO() {
		return new JdbcCargaSapMasivaDAO();
	}
	
}
