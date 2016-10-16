package cl.jumbo.interfaces.ventaslocales;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * Realiza la carga de Ventas Históricas para un cliente que se registra en Jumbo.cl.
 * 
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.4 - 14/07/2006
 */
public class Cargador extends Thread {

	protected final static Logger logger = Logger.getLogger(Cargador.class);

	/**
	 * Rut del cliente
	 */
	private int rut;
	
	
	/**
	 * Flag de estado de este hilo.
	 */
	private boolean activo = false;

	
	/**
	 * Único constructor válido. Requiere el rut del cliente.
	 * @param rut Rut del Cliente
	 */
	public Cargador(int rut) {
		super();
		this.rut = rut;
	}
	
	/**
	 * Realiza el proceso de carga de ventas históricas al cliente, desde la base de datos interna
	 * de Jumbo (donde se almacenan las compras provenientes de CDP) hacia la Base de Datos del FrontOffice.
	 * @return Vector con excepciones que pudieran ocurrir, pero que no son motivo de cancelar el proceso.
	 * @throws Exception Algún error no previsto ni posible de controlar.
	 */
	private Vector cargaCompras() throws Exception {

		// Vector para registro de errores en el proceso. 
		Vector exceptions 		= new Vector();
		InterfazVentas interfaz = new InterfazVentas();
		Connection conJumbo 	= null;
		Connection conBDI 		= null;
		ResultSet rs 			= null;
		PreparedStatement pst 	= null;
		PreparedStatement pstInsertComprasClientes 		=  null;
		PreparedStatement pstUpdateComprasClientes 		=  null;
		PreparedStatement pstUpdateProductoCDP 			=  null;
		PreparedStatement pstInsertProductosClientes 	=  null;

		try {
			
			// Establece Flag de Cargador a activo
			activo = true;
			
			// Obtiene conexiones a Bases de Datos
			conJumbo = interfaz.getConnection("jmcl");
			conBDI = interfaz.getConnection("vthjm");
			
			// Toma el control de confirmación de cambios en Base de Datos
			conJumbo.setAutoCommit(false);
			conBDI.setAutoCommit(false);
			
			// Query para obtener compras desde BDI, CompraCDP.DISPONIBLE = aquellas compras cargadas
			// desde CDP, pero que no han sido cargadas a FrontOffice
			String queryBDI = 	"	SELECT * FROM cliente_productos " +
								"	WHERE cpr_rut = ?";

			pst = conBDI.prepareStatement(queryBDI + " WITH UR");
			pst.setLong(1, this.rut);
			rs = pst.executeQuery();
			
			boolean firstRegister = true;
			Vector productos = new Vector();
			CompraHistorica compra = new CompraHistorica(this.rut);
			
			while (rs.next()) {
				// Si la cantidad de productos es mayor o igual que el Mínimo requerido, considera la compra
				if (firstRegister) {
					compra.setCodigoLocal("J500");
					compra.setRut(this.rut);
					compra.setFecha(new Date((new java.util.Date()).getTime()));
					compra.setNombreLocal("Local");
					firstRegister = false;
				}
				
				ProductoCliente producto = new ProductoCliente();
				int estado = rs.getInt("es_id");
				if (estado == CompraCDP.CARGADO_EN_VTHJM) {
					producto.setCantidad(1.00);
					producto.setCodigoEAN(rs.getLong("cpr_codigo_ean") + "");
					producto.setIdProducto(rs.getInt("cpr_id_producto"));
					productos.addElement(producto);
				}
			}
		
			// Si se obtuvieron compras con más del Mínimo de productos requeridos, continuar con productos.
			if (compra != null) {
				
				// Query para ingresar compras en FrontOffice
				String queryCompras = "INSERT INTO FO_COMPRAS_LOCALES values (?, ?, ?, ?, ?, ?)";
				
				// Query para ingresar productos de la compra en FrontOffice
				String queryProductos = "INSERT INTO FO_PRODUCTOS_COMPRA values (?, ?, ?, ?)";
				
				// Query para actualizar estado de compras en FrontOffice (marcar compras cargadas completamente)
				// Esto es para evitar que desde el FrontOffice se haga una consulta por compras y sólo se obtenga
				// la información de la compra, pero la carga de los productos esté todavía en proceso
				String queryUpdateCompra = "UPDATE FO_COMPRAS_LOCALES set FCL_ESTADO = " + 
					CompraCDP.CARGADO_A_CLIENTE + " WHERE FCL_ID = ?";
				
				// Query para actualizar estado de compras en BDI (marcar compras que no serán consideradas en una
				// carga posterior)
				String queryUpdateProductoCDP = "UPDATE cliente_productos set es_id = " + 
					CompraCDP.CARGADO_A_CLIENTE + " WHERE cpr_rut = ? and cpr_id_producto = ?";
				
				
				pstInsertComprasClientes = conJumbo.prepareStatement(queryCompras);
				pstUpdateComprasClientes = conJumbo.prepareStatement(queryUpdateCompra);
				pstUpdateProductoCDP = conBDI.prepareStatement(queryUpdateProductoCDP);
				pstInsertProductosClientes = conJumbo.prepareStatement(queryProductos);
				
				// Flag de control de proceso. Controla si se debe seguir el 
				// proceso de grabación de productos
				boolean seguirGrabando = true;

				// Graba información de Compra en FrontOffice
				pstInsertComprasClientes.clearParameters();
				pstInsertComprasClientes.setInt(1, compra.getRut());
				pstInsertComprasClientes.setInt(2, CompraCDP.EN_PROCESO_DE_CARGA);
				pstInsertComprasClientes.setDate(3, compra.getFecha());
				pstInsertComprasClientes.setInt(4, this.rut);
				pstInsertComprasClientes.setString(5, compra.getCodigoLocal());
				pstInsertComprasClientes.setString(6, compra.getNombreLocal());
				int insert = 0;
				
				// Al realizar una grabación de la compra, se controlan las posibles 
				// excepciones en SQL, por ejemplo, que se intente grabar una compra que ya existe
				try {
					insert = pstInsertComprasClientes.executeUpdate();
				} catch (SQLException e) {
					//e.printStackTrace();
					// En caso de que exista un problema grabando la información de la compra
					// no se debe seguir grabando los productos. En forma adicional, se registra
					// el problema en el Vector de Exceptions
					
					switch (e.getErrorCode()) {
						case -803: // Compra ya existente
							break;

						default: // Otro problema SQL no conocido, registrarlo, pero seguir procesando las otras compras
							seguirGrabando = false;
							CargaCompraException cce = new CargaCompraException("Error al cargar compra Rut [" + compra.getIdCompra() + "]: " + e.getErrorCode() + " - " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
							exceptions.add(cce);
					}
				}
				// Si la grabación de la información de la compra fue exitosa, seguir 
				// con los productos.
				if (seguirGrabando) {
					
					// Flag de control para grabar productos. En caso de no haberlos, no se confirma
					// el grabado de la información de compra tampoco.
					boolean productoCargado = false;
					boolean hayProductos = false;
					
					// Ciclo de copiado de productos desde VTHJM a JMCL
					for (int i = 0; i < productos.size(); i++) {
						
						productoCargado = false;

						ProductoCliente producto = (ProductoCliente) productos.elementAt(i);
						// Graba productos en FrontOffice
						pstInsertProductosClientes.clearParameters();
						pstInsertProductosClientes.setInt(1, producto.getIdProducto());
						pstInsertProductosClientes.setInt(2, compra.getRut());
						pstInsertProductosClientes.setString(3, producto.getCodigoEAN());
						pstInsertProductosClientes.setDouble(4, producto.getCantidad());
						
						// Si hay errores SQL al grabar un producto. Registrar problema
						// pero seguir con el resto de los productos
						try {
							insert = pstInsertProductosClientes.executeUpdate();
							productoCargado = true;
						} catch (SQLException e) {
							switch (e.getErrorCode()) {
								case -803: // Compra ya existente
									productoCargado = true;
									break;
								
								default:
									CargaCompraException cce = new CargaCompraException("Error al cargar producto [" + producto.getCodigoEAN() + "], compra ID [" + compra.getIdCompra() + "]: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_JMCL);
									exceptions.add(cce);
							}
						}

						// Si al menos se grabó un producto. Confirmar cambios en las bases de datos
						if (productoCargado) {
		
							hayProductos = true;
							// Actualiza información de "Compra Cargada" en VTHJM
							pstUpdateProductoCDP.clearParameters();
							pstUpdateProductoCDP.setInt(1, compra.getIdCompra());
							pstUpdateProductoCDP.setInt(2, producto.getIdProducto());
							pstUpdateProductoCDP.executeUpdate();

						}
					}
					// Si al menos se grabó un producto. Confirmar cambios en las bases de datos
					if (hayProductos) {
						// Actualiza información de "Compra Cargada Completamente" en FrontOffice
						pstUpdateComprasClientes.clearParameters();
						pstUpdateComprasClientes.setInt(1, compra.getIdCompra());
						pstUpdateComprasClientes.executeUpdate();
					} else {
						// Si no se grabaron productos. Deshacer grabado de compras
						conBDI.rollback();
						conJumbo.rollback();
					}
				} else {
					// Si no siguió proceso para grabar productos. Deshacer grabado de compras
					conBDI.rollback();
					conJumbo.rollback();
				}
			} 
			
		} catch (Exception e) {			
			logger.error("CargaCompra - Problema", e);
			logger.info("rollback");
			try {
				conBDI.rollback();
				conJumbo.rollback();
			} catch (Exception ex1) {
				logger.error("CargaCompra - Problema", ex1);
			}
		   throw new CargaCompraException(e);
		} finally {

	            try {
	            	
	        		if (rs != null)
	        			rs.close();	

	        		if (pst != null)
	        			pst.close();

	        		if (pstInsertComprasClientes != null){
	        			pstInsertComprasClientes.clearBatch();
	        			pstInsertComprasClientes.close();
	        		}
	        		if (pstUpdateComprasClientes != null){
	        			pstUpdateComprasClientes.clearBatch();
	        			pstUpdateComprasClientes.close();
	        		}
	        		if (pstUpdateProductoCDP != null){
	        			pstUpdateProductoCDP.clearBatch();
	        			pstUpdateProductoCDP.close();
	        		}
	        		if (pstInsertProductosClientes != null){
	        			pstInsertProductosClientes.clearBatch();
	        			pstInsertProductosClientes.close();
	        		}
	                //Cierra coneccion
	        		if (conBDI != null && !conBDI.isClosed()){
	        			conBDI.commit();
	        			conBDI.setAutoCommit(true);
	        			conBDI.close();
	        		}

	        		if (conJumbo != null && !conJumbo.isClosed()){
	        			conJumbo.commit();
	        			conJumbo.setAutoCommit(true);
	        			conJumbo.close();
	        		}
	        		
	            } catch (SQLException e) {
	                logger.error("getCarroComprasPorCategorias - Problema SQL (close)", e);
	            }				
				
				
			activo = false;
		}//Fin finally
		
		return exceptions;
		
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			// Ejecuta proceso de Carga y recibe errores controlados
			Vector exceptions = cargaCompras();
			
			// Si hubieron errores, reportarlos
			if ((exceptions != null) && (exceptions.size() > 0)) {
				if ((exceptions != null) && (exceptions.size() > 0)) {
					// Genera reportes por casa Exception ocurrida
					for (int i = 0; i < exceptions.size(); i++) {
						CargaCompraException e = (CargaCompraException) exceptions.elementAt(i);
						logger.error("Cargador rut [" + this.rut + "] : " + e.getMessage());
					}
				} 
			}
		} catch (Exception e) {
			logger.error("Cargador rut [" + this.rut + "] : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Consulta el flag de estado del Cargador
	 * @return <B>true</B> - Cargador activo<BR><B>false</B> - Cargador inactivo
	 */
	public boolean isActivo() {
		return activo;
	}
}
