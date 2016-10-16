package cl.jumbo.interfaces.ventaslocales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class InterfazVentas {
	
    ResourceBundle rb = ResourceBundle.getBundle("ventas");
    
    protected final static Logger logger = Logger.getLogger(InterfazVentas.class);
	
	private HashMap mapaDataSource = new HashMap();
	private Connection conCargaCDP = null;
	private PreparedStatement pstUpdate_cliente_productos = null;
	private PreparedStatement pstUpdate_compra_productos = null;
	private PreparedStatement pstInsert_cliente_productos = null;
	private PreparedStatement pstSelect_secuencias = null;
	private PreparedStatement pstUpdate_secuencias = null;
	private PreparedStatement pstUpdate_compras = null;
	private PreparedStatement pstSelectCodigoEan_cliente_productos = null;
	private PreparedStatement pstInsert_compras = null;
	private PreparedStatement pstInsert_compra_productos = null;
	private PreparedStatement pstUpdateEstado_compras = null;
	private PreparedStatement pstSelect_fidelizacion = null;
	
	/**
	 * Constante para obtener la lista de compras ordenadas por fecha en forma
	 * ascendente.
	 */
	public static int ORDEN_ASCENDENTE = 0;
	
	/**
	 * Constante para obtener la lista de compras ordenadas por fecha en forma
	 * descendente.
	 */
	public static int ORDEN_DESCENDENTE = 1;
	
	private String pathProcess = "";
	
	public InterfazVentas() {
		PropertyConfigurator.configure(InterfazVentas.class.getResource("/log4j.properties"));
		configureJNDI();
	}
	
	public InterfazVentas(String pathProcess) {
		PropertyConfigurator.configure(InterfazVentas.class.getResource("/log4j.properties"));
		this.pathProcess = pathProcess;
		configureJNDI();
	}

	/**
	 * Indica si el cliente tiene compras registradas.
	 * @param rut Rut del Cliente
	 * @return true - Tiene compras<BR>false - No tiene compras
	 */
	public synchronized boolean tieneCompras(int rut) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// Obtiene conexión para Base de Datos JUMBO
			conn = getConnection("jmcl");
			String query = 	"SELECT compras.fcl_id FROM FO_COMPRAS_LOCALES as compras " +
							"join FO_PRODUCTOS_COMPRA as productos on productos.FCL_ID = compras.FCL_ID " + 
							"WHERE compras.FCL_RUT = ? and compras.FCL_ESTADO = ?";
			pst = conn.prepareStatement(query + " WITH UR");

			// Establece parámetros en Query. Rut y ventas disponibles (que fueron cargadas y no están
			// en proceso de carga)
			pst.setInt(1, rut);
			pst.setInt(2, CompraCDP.CARGADO_A_CLIENTE);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null){
					pst.clearBatch();
					pst.close();
				}
				//cierra coneccion
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

		return false;
	}

	/**
	 * Informa que un cliente se ha registrado para gatillar la búsqueda y carga
	 * de sus compras históricas.
	 * 
	 * @param rut Rut del Cliente registrado.
	 */
	public void clienteRegistrado(int rut) {
		try {
			Cargador cargador = new Cargador(rut);
			cargador.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}

	/**
	 * Obtiene una conexión a base de datos configurada dado un nombre de referencia
	 * @param connecctionName Nombre de referencia a conexión: <BR><li>'jumbo' - Conexión a Jumbo.cl</li><BR><li>'bdi' - Conexión a BDI (as400)</li>
	 * @return 
	 * @throws NullConnectionException Conexión obtenida nula
	 */
	public Connection getConnection(String connecctionName) throws NullConnectionException {
		Connection con = null;
		
		try {
			con = ((DataSource) mapaDataSource.get(connecctionName)).getConnection();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		if (con == null)
			throw new NullConnectionException("Conexión para '" + connecctionName + "' nula.");
		return con;
	}
	
	/**
	 * Configura la Interfaz para usar JNDI en las conexiones a las bases de datos. 
	 * @param doc Contiene la información leida desde el XMl o Properties de configuración
	 * @throws JNDIConfigException Error al configurar JNDI
	 */
	private void configureJNDI() {
		try {
			Context ctx;
			ctx = new InitialContext();
			String lookupWebXML = "java:comp/env/";
			try {
				//DataSource dsJMCL = (DataSource) ctx.lookup(lookupWebXML + "jdbc/fodba");
				//DataSource dsVTHJM = (DataSource) ctx.lookup(lookupWebXML + "jdbc/vthdba");
				DataSource dsJMCL = (DataSource) ctx.lookup(lookupWebXML + rb.getString("JNDI_JUMBO"));
				DataSource dsVTHJM = (DataSource) ctx.lookup(lookupWebXML + rb.getString("JNDI_BDI"));
				mapaDataSource.put("jmcl", dsJMCL);
				mapaDataSource.put("vthjm", dsVTHJM);
			} catch (Exception e) {
				//DataSource dsJMCL = (DataSource) ctx.lookup("jdbc/fodba");
				//DataSource dsVTHJM = (DataSource) ctx.lookup("jdbc/vthdba");
				DataSource dsJMCL = (DataSource) ctx.lookup(rb.getString("JNDI_JUMBO"));
				DataSource dsVTHJM = (DataSource) ctx.lookup(rb.getString("JNDI_BDI"));
				mapaDataSource.put("jmcl", dsJMCL);
				mapaDataSource.put("vthjm", dsVTHJM);
			}
		} catch (NamingException e) {
			logger.error("Problemas al configurar JNDI en Interfaz de Ventas: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Problemas al configurar JNDI en Interfaz de Ventas: " + e.getMessage());
		}
	}
	
	private void initSaveComprasCDP() throws NullConnectionException, SQLException {
		if (conCargaCDP == null)
			conCargaCDP = getConnection("vthjm");

		// Query para obtener productos ya cargados para clientes
		String querySelectCodigoEan_cliente_productos =  "SELECT cpr_codigo_ean FROM cliente_productos where cpr_rut = ?";

		// Query para insertar productos nuevos para clientes
		String queryInsert_cliente_productos =  "INSERT into cliente_productos values (?, ?, ?, ?, ?)";
		
		// Query para obtener Id de compras
		String querySelect_secuencia = "SELECT MAX(sec_valor) FROM secuencias where sec_id = ?";
		
		// Query que inserta información de una compra
		String queryInsert_compras = "INSERT INTO compras values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Query que inserta información de un producto
		String queryInsert_compra_productos = "INSERT INTO compra_productos values (?, ?, ?)";
		
		// Query que actualiza ID en generador secuencial de Id's
		String queryUpdate_secuencias = "UPDATE secuencias set sec_valor = ? where sec_id = ?";
		
		// Query que actualiza estado de la información de la compra (durante la carga 
		// el estado es 'EN PROCESO', después que se carga el último producto, pasa a 
		// 'DISPONIBLE' para que pueda ser cargado a Jumbo.cl
		String updateEstado_compras = "UPDATE compras set es_id = ? where co_id_compra = ?";
		
		String queryIdCliente_rut = "SELECT fi_rut FROM fidelizacion where fi_id_cliente = ?";
		
		// Crea PreparedStatement para las queries anteriores
		pstSelectCodigoEan_cliente_productos = conCargaCDP.prepareStatement(querySelectCodigoEan_cliente_productos + " WITH UR");
		pstInsert_cliente_productos = conCargaCDP.prepareStatement(queryInsert_cliente_productos);
		pstSelect_secuencias = conCargaCDP.prepareStatement(querySelect_secuencia + " WITH UR");
		pstInsert_compras = conCargaCDP.prepareStatement(queryInsert_compras);
		pstInsert_compra_productos = conCargaCDP.prepareStatement(queryInsert_compra_productos);
		pstUpdate_secuencias = conCargaCDP.prepareStatement(queryUpdate_secuencias);
		pstUpdateEstado_compras = conCargaCDP.prepareStatement(updateEstado_compras);
		pstSelect_fidelizacion = conCargaCDP.prepareStatement(queryIdCliente_rut + " WITH UR");
	}
	
	protected void closeConnectionCargaCDP() throws CargaCompraException {
		if (conCargaCDP != null)
			try {
				if (!conCargaCDP.isClosed()) {
					
					pstSelectCodigoEan_cliente_productos.clearBatch();
					pstInsert_cliente_productos.clearBatch();
					pstSelect_secuencias.clearBatch();
					pstInsert_compras.clearBatch();
					pstInsert_compra_productos.clearBatch();
					pstUpdate_secuencias.clearBatch();
					pstUpdateEstado_compras.clearBatch();
					
					pstSelectCodigoEan_cliente_productos.close();
					pstInsert_cliente_productos.close();
					pstSelect_secuencias.close();
					pstInsert_compras.close();
					pstInsert_compra_productos.close();
					pstUpdate_secuencias.close();
					pstUpdateEstado_compras.close();
					
					pstSelectCodigoEan_cliente_productos = null;
					pstInsert_cliente_productos = null;
					pstSelect_secuencias = null;
					pstInsert_compras = null;
					pstInsert_compra_productos = null;
					pstUpdate_secuencias = null;
					pstUpdateEstado_compras = null;
					
					conCargaCDP.close();
					conCargaCDP = null;
				}
			} catch (SQLException e1) {
				throw new CargaCompraException("Error cerrando conexión al grabar en VTHJM: " + e1.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e1);
			} finally {
				conCargaCDP = null;
			}
	}
	
	/**
	 * Graba una compra en BDI (AS400) desde la interfaz que llega desde CDP (archivo de texto). Este
	 * método no lee el archivo, recibe la información encapsulada en un objeto y la escribe.
	 * @param compraCDP Objeto con información encapsulada de una compra
	 * @return Un vector con excepciones ocurridas al grabar la información. Esto es para evitar que
	 * el proceso termine, si no es necesario (grabar lo que más se pueda)
	 * @throws CargaCompraException Error al grabar la información de la compra.
	 * @throws SQLException 
	 */
	protected synchronized Vector saveComprasCDP(CompraCDP compraCDP) throws CargaCompraException {
		Vector exceptions = null;
		ResultSet rsRut = null;
		ResultSet rs = null;
		// Inicializa vector de exceptiones
		exceptions = new Vector();

		try {
			if (conCargaCDP == null)
				initSaveComprasCDP();
			
			// Toma el control del 'commit' en la base de datos, para controlar la escritura de información íntegra
			conCargaCDP.setAutoCommit(false);
			
			// Graba compras por clientes o ruts
			Iterator iter = compraCDP.getClientes().values().iterator();
			
			
			// Ciclo de lectura y escritura de la compra por cliente
			while (iter.hasNext()) {
				
				// Flag de control para continuar proceso de escritura de productos si la escritura
				// de la compra es exitosa
				boolean seguirGrabando = true;
				
				// Obtiene cliente
				ClienteCDP cliente = (ClienteCDP) iter.next();
				int rut = cliente.getRut();
				
				if (cliente.getMedioDePago().getCodigo() == MedioDePago.FIDELIZACION) {
					pstSelect_fidelizacion.clearParameters();
					pstSelect_fidelizacion.setInt(1, rut);
					rsRut = pstSelect_fidelizacion.executeQuery();
					if (rsRut.next())
						rut = rsRut.getInt(1);
				}
				
				// Obtiene Códigos EAN de Productos ya ingresados para clientes
				pstSelectCodigoEan_cliente_productos.clearParameters();
				pstSelectCodigoEan_cliente_productos.setInt(1, rut);
				rs = pstSelectCodigoEan_cliente_productos.executeQuery();
				Vector mapCodigosRegistrados = new Vector();
				
				while (rs.next()) {
					Long codigo = new Long(rs.getLong(1));
					mapCodigosRegistrados.addElement(codigo);
				}
				
				// Obtiene ID de Compra
				pstSelect_secuencias.clearParameters();
				pstSelect_secuencias.setInt(1, 1);
				rs = pstSelect_secuencias.executeQuery();
				
				int idCompra = 0;
				if (rs.next())
					idCompra = rs.getInt(1) + 1;
				
				// Escribe compra
				pstInsert_compras.clearParameters();
				pstInsert_compras.setInt(1, idCompra);
				pstInsert_compras.setString(2, compraCDP.getCodigoLocal());
				pstInsert_compras.setInt(3, cliente.getMedioDePago().getCodigo());
				pstInsert_compras.setInt(4, CompraCDP.EN_PROCESO_DE_CARGA);
				pstInsert_compras.setInt(5, cliente.getRut());
				pstInsert_compras.setDate(6, compraCDP.getFecha());
				pstInsert_compras.setInt(7, compraCDP.getCaja());
				pstInsert_compras.setDouble(8, compraCDP.getMontoTotal());
				pstInsert_compras.setInt(9, compraCDP.getTicket());
				int insert = 0;
				try {
					insert = pstInsert_compras.executeUpdate();
					
				// Si hay error, compra no se pudo grabar, no continuar grabando productos, evaluar error
				} catch (SQLException e) {
					seguirGrabando = false;
					switch (e.getErrorCode()) {

					// Error al insertar un valor desconocido, campo relacionado con otra tabla y valor debe estar
					// en dicha tabla (violación a foreing key)
					case -530:
						CargaCompraException cce530 = null;
						if (e.getMessage().toUpperCase().indexOf("FK_MDP_COMPRA") != -1)
							cce530 = new CargaCompraException("Código de Medio de Pago [" + cliente.getMedioDePago().getCodigo() + "] para Cliente [" + cliente.getRut() + "] no existe: Fecha [" + compraCDP.getFecha().toString() + "], Caja [" + compraCDP.getCaja() + "], Ticket [" + compraCDP.getTicket() + "]", CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
						else if (e.getMessage().toUpperCase().indexOf("FK_LOCAL_COMPRAS") != -1)
							cce530 = new CargaCompraException("Código de Local [" + compraCDP.getCodigoLocal() + "] no existe: Fecha [" + compraCDP.getFecha().toString() + "], Caja [" + compraCDP.getCaja() + "], Ticket [" + compraCDP.getTicket() + "]", CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
						else 
							cce530 = new CargaCompraException(e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
						exceptions.add(cce530);
						break;
					
					// Error al insertar compra, la compra ya existe (violación de Llave única Rut + Fecha + Caja + Ticket + Local)
					case -803:
//						internalError(e);
						CargaCompraException cce803 = new CargaCompraException("Compra existente cliente [" + cliente.getRut() + "], Fecha [" + compraCDP.getFecha().toString() + "], Caja [" + compraCDP.getCaja() + "], Ticket [" + compraCDP.getTicket() + "]", CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
						exceptions.add(cce803);
						break;

					// Otro tipo de error
					default:
						logger.error(e.getErrorCode() + " - " + e.getMessage());
						break;
					}
				}
				
				// Si la compra se grabó exitosamente, seguir con los productos
				if (seguirGrabando) {
					boolean hayProductos = false;
					for (int i = 0; i < compraCDP.getProductos().size(); i++) {
						ProductoCompraHistorica producto = (ProductoCompraHistorica) compraCDP.getProductos().elementAt(i);

						// Escribe producto
						pstInsert_compra_productos.clearParameters();
						pstInsert_compra_productos.setInt(1, idCompra);
						pstInsert_compra_productos.setLong(2, Long.parseLong(producto.getCodigoEAN()));
						pstInsert_compra_productos.setDouble(3, producto.getCantidad());
						try {
							insert = pstInsert_compra_productos.executeUpdate();
							if (!mapCodigosRegistrados.contains(new Long(Long.parseLong(producto.getCodigoEAN())))) {
								
								// Obtiene ID de Compra
								pstSelect_secuencias.clearParameters();
								pstSelect_secuencias.setInt(1, 2);
								rs = pstSelect_secuencias.executeQuery();
								
								int idproducto = 0;
								if (rs.next())
									idproducto = rs.getInt(1) + 1;
								
								// Actualiza ID de Compra en Secuenciador de Id's
								pstUpdate_secuencias.clearParameters();
								pstUpdate_secuencias.setInt(1, idproducto);
								pstUpdate_secuencias.setInt(2, 2);
								insert = pstUpdate_secuencias.executeUpdate();

								pstInsert_cliente_productos.clearParameters();
								pstInsert_cliente_productos.setInt(1, rut);
								pstInsert_cliente_productos.setInt(2, idproducto);
								pstInsert_cliente_productos.setInt(3, CompraCDP.CARGADO_EN_VTHJM);
								pstInsert_cliente_productos.setLong(4, Long.parseLong(producto.getCodigoEAN()));
								pstInsert_cliente_productos.setDouble(5, 1);
								insert = pstInsert_cliente_productos.executeUpdate();

								hayProductos = true;
								
								mapCodigosRegistrados.remove(new Long(Long.parseLong(producto.getCodigoEAN())));
							}
						// Si hay error, compra no se pudo grabar, no continuar grabando productos, evaluar error
						} catch (SQLException e) {
							seguirGrabando = false;
							CargaCompraException ce = new CargaCompraException("Error al grabar Producto CDP: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
							exceptions.addElement(ce);
						}
						
					}
	
					//Al terminar de grabar productos, cambia estado de compra para que esté disponible
					if (hayProductos) {
						pstUpdateEstado_compras.clearParameters();
						pstUpdateEstado_compras.setInt(1, CompraCDP.CARGADO_EN_VTHJM);
						pstUpdateEstado_compras.setInt(2, idCompra);
						insert = pstUpdateEstado_compras.executeUpdate();

						// Actualiza ID de Compra en Secuenciador de Id's
						pstUpdate_secuencias.clearParameters();
						pstUpdate_secuencias.setInt(1, idCompra);
						pstUpdate_secuencias.setInt(2, 1);
						insert = pstUpdate_secuencias.executeUpdate();

						// Si compra y productos se grabó exitosamente, hacer efectivos los cambios en la Base de datos
						conCargaCDP.commit();
						
					} else {
						conCargaCDP.rollback();
					}

				} else {
					conCargaCDP.rollback();
				}
				
			}

		} catch (SQLException e) {
			exceptions.addElement(e);
			try {
				if ((conCargaCDP != null) && !conCargaCDP.isClosed())
					conCargaCDP.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getErrorCode() + " - " + e1.getMessage());
			}
			throw new CargaCompraException("Error grabando información en VTHJM: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e);
		} catch (NullConnectionException e) {
			exceptions.addElement(e);
			try {
				if ((conCargaCDP != null) && !conCargaCDP.isClosed())
					conCargaCDP.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getErrorCode() + " - " + e1.getMessage());
			}
			throw new CargaCompraException("Error grabando información en VTHJM: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e);
		} catch (Exception e) {
			exceptions.addElement(e);
			try {
				if ((conCargaCDP != null) && !conCargaCDP.isClosed())
					conCargaCDP.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getErrorCode() + " - " + e1.getMessage());
			}
			logger.error(e.getMessage());
		} finally {
			
			try {
				if (rsRut != null)
					rsRut.close();
				if (rs != null){
					rs.close();
				}
				//cierra coneccion
				closeConnectionCargaCDP();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		
		}
		
		return exceptions;
	}
	
	/**
	 * Obtiene la lista de Ruts registrados en JUMBO
	 * @return
	 * @throws ErrorReporterConfigException Error al intentar utilizar clase para reporte de errores
	 */
	protected synchronized Vector getRuts() {
		Vector result = new Vector();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			conn = getConnection("jmcl");
			String query = "SELECT distinct CLI_RUT FROM FO_CLIENTES WHERE CLI_ESTADO = ?";
			pst = conn.prepareStatement(query + " WITH UR");
			
			// Sólo considera aquellos Ruts con estado Activo ('A')
			pst.setString(1, "A");
			rs = pst.executeQuery();
			while (rs.next()) {
				int rut = rs.getInt(1);
				result.addElement(new Integer(rut));
			}

			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null){
					pst.clearBatch();
					pst.close();
				}
				//cierra coneccion
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
		return result;
	}

	/**
	 * Obtiene las compras históricas asociadas a un Rut, ordenadas por fecha. Las compras corresponden a aquellas
	 * que contengan 10 o más productos distintos. Puede ajustarse este límite en InterfazVentas.FILTRO_MIN_PRODUCTOS
	 * 
	 * @param rut Rut del cliente a consultar.
	 * @param orden Orden del listado por fechas. <BR>
	 * <li>ORDEN_ASCENDENTE: ascendente.
	 * <li>ORDEN_DESCENDENTE: descendente.
	 * @return Vector con lista de compras ordenadas por fecha.
	 * @throws InvalidSortException Parámetro 'orden' no es válido. Utilice InterfazVentas.ORDEN_ASCENDENTE o
	 * InterfazVentas.ORDEN_DESCENDENTE
	 * @throws ErrorReporterConfigException Error al intentar utilizar clase para reporte de errores
	 */
	public synchronized Vector getComprasByRut(int rut, int orden) throws InvalidSortException {
		Vector result = new Vector();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// Obtiene conexión para Base de Datos JUMBO
			conn = getConnection("jmcl");
			String query = "SELECT compras.*, count(productos.FPC_ID) as unidades FROM FO_COMPRAS_LOCALES as compras " +
				"join FO_PRODUCTOS_COMPRA as productos on productos.FCL_ID = compras.FCL_ID " + 
				"WHERE compras.FCL_RUT = ? and compras.FCL_ESTADO = ?" +
				"group by compras.FCL_ID, compras.FCL_CODIGO_LOCAL, compras.FCL_RUT, " +
				"compras.FCL_FECHA, compras.FCL_NOMBRE_LOCAL, compras.FCL_ESTADO order by compras.FCL_FECHA";
			
			logger.debug(query);
			logger.debug("compras.FCL_RUT = " + rut);
			logger.debug("compras.FCL_ESTADO = " + CompraCDP.CARGADO_A_CLIENTE);
			
			// Selecciona orden del listado por fecha, de acuerdo a parámetro 'orden'
			if (orden == InterfazVentas.ORDEN_ASCENDENTE)
				query += " asc";
			else if (orden == InterfazVentas.ORDEN_DESCENDENTE)
				query += " desc";
			else
				throw new InvalidSortException("Orden especificado '" + orden + "' inválido, utilice 'InterfazVentas.ORDEN_ASCENDENTE' o InterfazVentas.ORDEN_DESCENDENTE");
			
			pst = conn.prepareStatement(query + " WITH UR");

			// Establece parámetros en Query. Rut y ventas disponibles (que fueron cargadas y no están
			// en proceso de carga)
			logger.debug("Rut llegado a Interfaz: " + rut);
			pst.setInt(1, rut);
			pst.setInt(2, CompraCDP.CARGADO_A_CLIENTE);
			rs = pst.executeQuery();
			while (rs.next()) {
				CompraHistorica compra = new CompraHistorica(rs.getInt("FCL_ID"));
				compra.setCodigoLocal(rs.getString("FCL_CODIGO_LOCAL"));
				compra.setRut(rut);
				compra.setFecha(rs.getDate("FCL_FECHA"));
				if (rs.getString("FCL_NOMBRE_LOCAL") != null)
					compra.setNombreLocal(rs.getString("FCL_NOMBRE_LOCAL"));
				compra.setUnidades(rs.getInt("unidades"));
				result.add(compra);
			}
		} catch (InvalidSortException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null){
					pst.clearBatch();
					pst.close();
				}
				//cierra coneccion
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComprasByRut - Problema SQL (close)", e);
			}
		}
		
		return result;
	}
	
	/**
	 * Obtiene las compras históricas asociadas a un Rut ordenadas por fecha
	 * descendente.Las compras corresponden a aquellas
	 * que contengan 10 o más productos distintos. Puede ajustarse este límite en InterfazVentas.FILTRO_MIN_PRODUCTOS
	 * 
	 * @param rut Rut del cliente a consultar.
	 * @return Vector con lista de compras ordenadas por fecha.
	 * @throws InvalidSortException Parámetro 'orden' no es válido. Utilice InterfazVentas.ORDEN_ASCENDENTE o
	 * InterfazVentas.ORDEN_DESCENDENTE
	 * @throws ErrorReporterConfigException Error al intentar utilizar clase para reporte de errores
	 */
	public synchronized Vector getComprasByRut(int rut) throws InvalidSortException {
		return getComprasByRut(rut, InterfazVentas.ORDEN_DESCENDENTE);
	}

}
