/*
 * Created on 13-11-2007
 *
 */
package cl.jumbo.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cl.jumbo.interfaces.ventaslocales.CargaCompraException;
import cl.jumbo.interfaces.ventaslocales.ClienteCDP;
import cl.jumbo.interfaces.ventaslocales.CompraCDP;
import cl.jumbo.interfaces.ventaslocales.InterfazVentas;
import cl.jumbo.interfaces.ventaslocales.MedioDePago;
import cl.jumbo.interfaces.ventaslocales.NullConnectionException;
import cl.jumbo.interfaces.ventaslocales.ProductoCompraHistorica;
import cl.jumbo.util.FiltroArchivos;

/**
 * @author jvillalobos
 *
 */
public class CargaVentasLocales extends Thread implements ProcesoInterfaz {

	protected final static Logger logger = Logger.getLogger(CargaVentasLocales.class);

	private boolean stopFlag = false;
	private boolean sleepFlag = false;
	private boolean pauseFlag = false;
	private long timeSleep = 0;
	private Status status = null;
	private Vector statusHistory = new Vector();
	private String dirProcess = "";
	private ControladorProcesos controlador = null;
	private FileMonitorUtil fileUtil = new FileMonitorUtil();
	private int rutCache = 0;


	public CargaVentasLocales(
			String dirProcess, 
			ControladorProcesos controlador) {
		super();
		this.dirProcess = dirProcess;
		this.controlador = controlador;
		PropertyConfigurator.configure(CargaVentasLocales.class.getResource("/log4j.properties"));
	}

	/**
	 * @param target
	 */
	public CargaVentasLocales(Runnable target) {
		super(target);
	}

	/**
	 * @param group
	 * @param target
	 */
	public CargaVentasLocales(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	/**
	 * @param name
	 */
	public CargaVentasLocales(String name) {
		super(name);
	}

	/**
	 * @param group
	 * @param name
	 */
	public CargaVentasLocales(ThreadGroup group, String name) {
		super(group, name);
	}

	/**
	 * @param target
	 * @param name
	 */
	public CargaVentasLocales(Runnable target, String name) {
		super(target, name);
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public CargaVentasLocales(ThreadGroup group, Runnable target,
			String name) {
		super(group, target, name);
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public CargaVentasLocales(ThreadGroup group, Runnable target,
			String name, long stackSize) {
		super(group, target, name, stackSize);
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#runProcess()
	 */
	public void runProcess() {
		start();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toStop()
	 */
	public void toStop() {
		stopFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatusHistory()
	 */
	public Vector getStatusHistory() {
		return statusHistory;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatus()
	 */
	public Status getStatus() {
		return this.status;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toPause()
	 */
	public void toPause() {
		pauseFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toResume()
	 */
	public void toResume() {
		pauseFlag = false;
		notify();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toSleep(long)
	 */
	public void toSleep(long time) {
		timeSleep = time;
		sleepFlag = true;
		if (time == 0)
			sleepFlag = false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.debug("Iniciando Análisis y Carga de Archivos Para Ventas Locales");
		setStatus(new Status(1, "Iniciando Analisis y Carga de Archivos Para Ventas Locales"));
		try {
			File processDirectory = new File(this.dirProcess);
			File[] files = processDirectory.listFiles(new FiltroArchivos("JCV.*", null));
			logger.info("Archivos encontrados para procesar: " + ((files!=null)?files.length:0));
			validar(files);
			logger.debug("Análisis y Carga de Archivos Terminado Exitosamente");
			setStatus(new Status(0, "Analisis y Carga de Archivos Terminado Exitosamente"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.debug("Análisis y Carga de Archivos Terminado con Problemas");
			setStatus(new Status(0, "Analisis y Carga de Archivos Terminado con Problemas"));
		}
	}
	
	private void setStatus(Status status) {
		this.status = status;
		statusHistory.add(0, status);
		controlador.setStatusHistory(statusHistory);
	}

	/**
	 * Realiza la lectura de los archivos, validación de la información y carga de las ventas desde CDP
	 * @param archivos Arreglo de archivos válidos, listos para procesar
	 * @throws Exception Algún error ocurrido y no controlado
	 */
	private void validar(File[] archivos) throws Exception {
		
		boolean logSalida = true;
		
		// Ciclo de lectura de archivos
		for (int i = 0; i < archivos.length; i++) {
			setStatus(new Status(1, "Procesando: " + archivos[i].getName()));
			logger.debug("Procesando: " + archivos[i].getName());
			BufferedReader buffer = new BufferedReader(new FileReader(archivos[i]));
			String line = null;
			CompraCDP compraCDP = null;
			TreeMap clientes = null;
			HashMap productos = null;
			
			// Números de Línea
			int lineNumber = 0;
			int initialLineNumber = 0;
			int finalLineNumber = 0;
			
			// Flag de control para la omisión o consideración de información a grabar
			boolean omitirRegistro = false;
			boolean registrando = true;
			boolean errorInArchive = false;
			boolean releer = false;
			
			// Objeto con información a reportar
			String fileNameError = null;
			
			// Análisis por línea
			while (registrando) {
				
				if (!this.stopFlag) {
					
					if (!releer) {
						line = buffer.readLine();
						lineNumber++;
						releer = false;
					}
					
					try {
						// Si line == null entonces se llegó al final del archivo
						if (line == null) {
							finalLineNumber = lineNumber - 1;
							registrando = false;
							if (!omitirRegistro) {
								saveCompra(compraCDP, clientes, productos, lineNumber, initialLineNumber, finalLineNumber, archivos[i].getName());
							}
						} else {
							// Encabezado TK (identifica el comienzo de una compra)
							if (line.startsWith("TK")) {
								
								// Limpia información de Ruts en Cache
								rutCache = 0;
								
								// Registra el número de línea final del registro anterior
								finalLineNumber = lineNumber - 1;
								if ((compraCDP != null) && (!releer) && (!omitirRegistro)) {
									// Si hay una lectura de compra en proceso y no se está omitiendo el registro, grabar
									saveCompra(compraCDP, clientes, productos, lineNumber, initialLineNumber, finalLineNumber, archivos[i].getName());
								} 
	
								omitirRegistro = false;
								releer = false;
								initialLineNumber = lineNumber;
								clientes = new TreeMap();
								productos = new HashMap();
								compraCDP = fillHeaderCompra(line);
							// Información de Cliente
							} else if ((line.startsWith("CL") && (!omitirRegistro))) {
								fillClienteCompra(clientes, line);
							// Información de los Productos	
							} else if ((line.startsWith("AR") && (!omitirRegistro))) {
								fillProductos(productos, line);
							// Información de los Medios de Pagos asociados a los clientes
							} else if ((line.startsWith("MP") && (!omitirRegistro))) {
								compraCDP = fillMedioDePago(compraCDP, line);
							// Otra información es un error
							} else if (!omitirRegistro) {
								throw new CargaCompraException("Informacion desconocida: [" + line + "]", CargaCompraException.ERROR_INFORMACION_DESCONOCIDA);					
							}
						}
					} catch (CargaCompraException e) {
						e.printStackTrace();
						errorInArchive = true;
						logger.debug("Error en " + archivos[i].getName() + " : Línea " + lineNumber + " : " + e.getMessage());
	
						// Si ocurre un error, evaluar código de error y de acuerdo a este, generar reporte o hacer
						// alguna cosa adicional
						switch (e.getErrorCode()) {
						
							// Si ocurre un error al grabar en BDI, reeler = true permite no perder
							// la información en cache que se obtiene cuando se detecta un encabezado
							// TK de inicio de compra. No significa que vuelve a reeler el archivo.
							case CargaCompraException.ERROR_GRABANDO_EN_VTHJM:
								releer = true;
								break;
								
							// Para los siguientes casos, no se debe seguir procesando la información
							// en el archivos, hasta un próximo registro de Compra en el archivo. Flag "omitirRegistro"
							case CargaCompraException.ERROR_EN_HEADER:
							case CargaCompraException.ERROR_EN_CLIENTE_SALTAR_REGISTRO:
							case CargaCompraException.ERROR_EN_PRODUCTOS_SALTAR_REGISTRO:
							case CargaCompraException.ERROR_INFORMACION_DESCONOCIDA:
								omitirRegistro = true;
								break;
							
							// Para los siguientes casos, son datos que pueden faltar.
							case CargaCompraException.ERROR_EN_CLIENTE:
							case CargaCompraException.ERROR_EN_MEDIOS_DE_PAGO:
							case CargaCompraException.ERROR_EN_PRODUCTOS:
								break;
		
							default:
								break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Error en " + archivos[i].getName() + " : Línea " + lineNumber + " : " + e.getMessage());
						//setStatus(new Status(-1, "Error en " + archivos[i].getName()));
					}
				} else {
					if (logSalida) {
						logger.debug("Proceso Cancelado");
						setStatus(new Status(-1, "Proceso Cancelado"));
						logSalida = false;
					}
					registrando = false;
				}

			}
			
			buffer.close();
			archivos[i].delete();
			if (!logSalida) {
				logger.debug("Proceso de archivo: " + archivos[i].getName() + " terminado.");
				setStatus(new Status(1, "Proceso de archivo: " + archivos[i].getName() + " terminado."));
			}
		}	
	}

	/**
	 * Graba un registro de compra en la Base de Datos. Utiliza Interfaz de Ventas.
	 * @param compraCDP Objeto con información del registro de compra
	 * @param clientes Mapa de clientes asociados a la compra
	 * @param productos Productos contenidos en la compra
	 * @param lineNumber Número de la última línea en el archivo leída
	 * @param initialLineNumber Número de línea en el archivo donde comienza el registro
	 * @param finalLineNumber Número de la líena en el archivo donde finaliza el registro
	 * @param fileName Nombre del archivo leido
	 * @throws CargaCompraException Error al grabar registro de Compra en Base de Datos
	 * @throws XMLInvalidException Error en configuración via XMl en Interfaz de Ventas
	 * @throws ErrorReporterConfigException Error al configurar clase que reporta errores en Interfaz de Ventas
	 * @throws ConfigException Error al configurar Interfaz de Ventas
	 * @throws PropertiesConfigException Error al configurar via Properties la Interfaz de Ventas
	 */
	private void saveCompra(CompraCDP compraCDP, TreeMap clientes, HashMap productos, int lineNumber, int initialLineNumber, int finalLineNumber, String fileName) throws CargaCompraException {
		
			// Si no hay clientes, entonces no vale la pena grabar compra
			if ((clientes == null) || (clientes.values().size() == 0))
				throw new CargaCompraException("Compra registrada no tiene clientes asociados", CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
			
			// Si no hay productos, tampoco vale la pena grabar la compra
			else if ((productos == null) || (productos.size() == 0))
				throw new CargaCompraException("Compra registrada no tiene productos registrados", CargaCompraException.ERROR_GRABANDO_EN_VTHJM);
			
			// Agrega la información de clientes y productos a la compra
			compraCDP.setClientes(clientes);
			compraCDP.setProductos(productos);
			
			// Si el importe total de la compra > 0, no es nota de crédito
			if (compraCDP.getMontoTotal() > 0) {
				Vector exceptions = saveComprasCDP(compraCDP);
				
				// Si hubo problemas al grabar, reportar dichos problemas
				if ((exceptions != null) && (exceptions.size() > 0)) {
					for (int i = 0; i < exceptions.size(); i++) {
						CargaCompraException e = (CargaCompraException) exceptions.elementAt(i);
						logger.error(fileName + ": " + e.getMessage());
					}
				} else {
					// Registrar compras cargadas
					Iterator iter = clientes.values().iterator();
					while (iter.hasNext()) {
						ClienteCDP cliente = (ClienteCDP) iter.next();
						logger.debug(fileName + ": Compra cargada, RUT " + cliente.getRut());
						logger.debug("Datos Compra: Fecha [" + compraCDP.getFecha().toString() + "], Ticket [" + compraCDP.getTicket() + "], Caja [" + compraCDP.getCaja() + "], Local [" + compraCDP.getCodigoLocal() + "]");
					}
				}
			} else {
				// Reportar notas de créditos (Importe total < 0)
				Iterator iter = clientes.values().iterator();
				while (iter.hasNext()) {
					ClienteCDP cliente = (ClienteCDP) iter.next();
					logger.debug(fileName + ": Compra NOTA DE CRÉDITO, RUT " + cliente.getRut());
					logger.debug("Datos Compra: Fecha [" + compraCDP.getFecha().toString() + "], Ticket [" + compraCDP.getTicket() + "], Caja [" + compraCDP.getCaja() + "], Local [" + compraCDP.getCodigoLocal() + "]");
				}
			}
	}

	public Vector saveComprasCDP(CompraCDP compraCDP) throws CargaCompraException {
		Vector exceptions = null;
		// Inicializa vector de exceptiones
		
		PreparedStatement pstUpdate_cliente_productos = null;
		PreparedStatement pstUpdate_compra_productos = null;
		PreparedStatement pstInsert_cliente_productos = null;
		PreparedStatement pstSelect_secuencias = null;
		PreparedStatement pstUpdate_secuencias = null;
		PreparedStatement pstUpdate_compras = null;
		PreparedStatement pstSelectCodigoEan_cliente_productos = null;
		PreparedStatement pstInsert_compras = null;
		PreparedStatement pstInsert_compra_productos = null;
		PreparedStatement pstUpdateEstado_compras = null;
		PreparedStatement pstSelect_fidelizacion = null;

		exceptions = new Vector();
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rsRut = null;
		
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
		
		try {
			conn = (new InterfazVentas()).getConnection("vthjm");

			// Crea PreparedStatement para las queries anteriores
			pstSelectCodigoEan_cliente_productos = conn.prepareStatement(querySelectCodigoEan_cliente_productos + " WITH UR");
			pstInsert_cliente_productos = conn.prepareStatement(queryInsert_cliente_productos);
			pstSelect_secuencias = conn.prepareStatement(querySelect_secuencia + " WITH UR");
			pstInsert_compras = conn.prepareStatement(queryInsert_compras);
			pstInsert_compra_productos = conn.prepareStatement(queryInsert_compra_productos);
			pstUpdate_secuencias = conn.prepareStatement(queryUpdate_secuencias);
			pstUpdateEstado_compras = conn.prepareStatement(updateEstado_compras);
			pstSelect_fidelizacion = conn.prepareStatement(queryIdCliente_rut + " WITH UR");

			// Toma el control del 'commit' en la base de datos, para controlar la escritura de información íntegra
			conn.setAutoCommit(false);
			
			// Graba compras por clientes o ruts
			Iterator iter = compraCDP.getClientes().values().iterator();
			
			
			// Ciclo de lectura y escritura de la compra por cliente
			while (iter.hasNext()) {
				
				if (!this.stopFlag) {
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
							
							
						} else {
							conn.rollback();
						}
	
					} else {
						conn.rollback();
					}
				}	
			}

		} catch (SQLException e) {
			exceptions.addElement(e);
			try {
					conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getErrorCode() + " - " + e.getMessage());
			}
			throw new CargaCompraException("Error grabando información en VTHJM: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e);
		} catch (NullConnectionException e) {
			exceptions.addElement(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
			}
			throw new CargaCompraException("Error grabando información en VTHJM: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e);
		} catch (Exception e) {
			exceptions.addElement(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getErrorCode() + " - " + e.getMessage());
			}
			logger.error(e.getMessage());
			throw new CargaCompraException("Error cerrando conexión al grabar en VTHJM: " + e.getMessage(), CargaCompraException.ERROR_GRABANDO_EN_VTHJM, e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (rsRut != null)
					rsRut.close();
				
				if (pstUpdate_cliente_productos != null){
					pstUpdate_cliente_productos.clearBatch();
					pstUpdate_cliente_productos.close();
				}
				if (pstUpdate_compra_productos != null){
					pstUpdate_compra_productos.clearBatch();
					pstUpdate_compra_productos.close();
				}				
				if (pstInsert_cliente_productos != null){
					pstInsert_cliente_productos.clearBatch();
					pstInsert_cliente_productos.close();
				}
				if (pstSelect_secuencias != null){
					pstSelect_secuencias.clearBatch();
					pstSelect_secuencias.close();
				}
				if (pstUpdate_secuencias != null){
					pstUpdate_secuencias.clearBatch();
					pstUpdate_secuencias.close();
				}
				if (pstUpdate_compras != null){
					pstUpdate_compras.clearBatch();
					pstUpdate_compras.close();
				}
				if (pstSelectCodigoEan_cliente_productos != null){
					pstSelectCodigoEan_cliente_productos.clearBatch();
					pstSelectCodigoEan_cliente_productos.close();
				}
				if (pstInsert_compras != null){
					pstInsert_compras.clearBatch();
					pstInsert_compras.close();
				}
				if (pstInsert_compra_productos != null){
					pstInsert_compra_productos.clearBatch();
					pstInsert_compra_productos.close();
				}
				if (pstUpdateEstado_compras != null){
					pstUpdateEstado_compras.clearBatch();
					pstUpdateEstado_compras.close();
				}
				if (pstSelect_fidelizacion != null){
					pstSelect_fidelizacion.clearBatch();
					pstSelect_fidelizacion.close();
				}
				//cierra coneccion
				if (conn != null && !conn.isClosed())
					conn.commit();
					conn.setAutoCommit(true);
					conn.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : saveComprasCDP - Problema SQL (close)", e);
			}
		}
		
		return exceptions;
	}
	
	/**
	 * Registra información de Encabezado de Compra leida desde el archivo.
	 * @param line Linea leida desde el archivo
	 * @return Objeto con información de un registro de compra
	 * @throws CargaCompraException Error al validar encabezado de compra
	 */
	private CompraCDP fillHeaderCompra(String line) throws CargaCompraException {
		CompraCDP compraCDP = null;
		try {
			compraCDP = new CompraCDP();
			// Separa información de acuerdo a separador "|"
			String[] header = line.split("\\|");
			
			// Total de campos en encabezado: 5 (Encabezado, Local, Caja, Ticket Fecha)
			if ((header == null) || (header.length != 5))
				throw new CargaCompraException("Información de encabezado inválida: [" + line + "]", CargaCompraException.ERROR_EN_HEADER);
			
			// Segundo campo es LOCAL, es char(4). Si no es de 4 caracteres es incorrecto 
			if (header[1].trim().length() != 4)
				throw new CargaCompraException("Campo 'Código de Local': Largo = " + header[1].trim().length(), CargaCompraException.ERROR_EN_HEADER);
			
			// Registra código del Local
			compraCDP.setCodigoLocal(header[1].trim());
			
			try {
				// Campo Caja, debe ser número entero
				compraCDP.setCaja(Integer.parseInt(header[2].trim()));
			} catch (NumberFormatException e) {
				throw new CargaCompraException("Campo 'Caja': No es numérico [" + header[2].trim() + "]", CargaCompraException.ERROR_EN_HEADER);
			}

			try {
				// Valida Campo Fecha
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			    sdf.setLenient(false);
			    java.util.Date dt2 = sdf.parse(header[4].trim());
			    
			    // Fecha de comparación para antiguedad del dato. No más antiguo que el año 2006
				java.util.Date underLimitDate = sdf.parse("20060101");
				java.util.Date fechaTemp = null;
				fechaTemp = sdf.parse(header[4].trim());
				if (fechaTemp.before(underLimitDate))
					throw new CargaCompraException("Campo 'Fecha': Fecha inválida (anterior al año 2006) [" + header[4].trim() + "]", CargaCompraException.ERROR_EN_HEADER);
				compraCDP.setFecha(new Date(fechaTemp.getTime()));
			} catch (ParseException e1) {
				throw new CargaCompraException("Campo 'Fecha': Fecha inválida o formato no reconocible [" + header[4].trim() + "]", CargaCompraException.ERROR_EN_HEADER);
			}
			
			try {
				// Campo Ticket debe ser número entero
				compraCDP.setTicket(Integer.parseInt(header[3].trim()));
			} catch (NumberFormatException e) {
				throw new CargaCompraException("Campo 'Ticket': No es numérico [" + header[3].trim() + "]", CargaCompraException.ERROR_EN_HEADER);
			}
		} catch (CargaCompraException e) {
			throw e;
		} catch (Exception e) {
			// Cualquier otro error no prevenido, lo registra y lo identifica como error en encabezado
			throw new CargaCompraException("Error no identificable: " + e.getMessage(), CargaCompraException.ERROR_EN_HEADER, e);
		}
		
		return compraCDP;
	}

	/**
	 * Registra información de Clientes asociados a la compra
	 * @param clientes Mapa de clientes asociados a la compra. Es un mapa, para controlar información repetida.
	 * @param line Línea leida desde el archivo
	 * @throws CargaCompraException Error al validar clientes asociados a la compra
	 */
	private void fillClienteCompra(TreeMap clientes, String line) throws CargaCompraException {
		// Si el mapa de clientes es nulo, siginifica que el cliente en el archivo está "volando", es decir,
		// no hay una compra registrada al cual asociarle
		if (clientes == null) {
			throw new CargaCompraException("Encabezado de compra TK perdido", CargaCompraException.ERROR_EN_CLIENTE_SALTAR_REGISTRO);
		}
		
		// Separa información de acuerdo a separador "|"
		String[] cliente = line.split("\\|");
		int rut = 0;
		
		// Total de campos en Cliente: 2 (Rut, Código CDP Medio de Pago)
		if ((cliente == null) && (cliente.length != 2))
			throw new CargaCompraException("Información de Cliente inválida: [" + line + "]", CargaCompraException.ERROR_EN_CLIENTE);

		// Valida RUT, que es varchar(10) no es válido si tiene más
		try {
			if (cliente[1].trim().length() > 10)
				throw new CargaCompraException("Campo 'Rut': Largo > 10 [" + cliente[1].trim() + "]", CargaCompraException.ERROR_EN_CLIENTE);
			rut = Integer.parseInt(cliente[1].trim());
		} catch (NumberFormatException e) {
			rut = Integer.parseInt(cliente[1].trim().substring(0, cliente[1].trim().length() - 1));
			try {
			} catch (NumberFormatException e1) {
				throw new CargaCompraException("Campo 'Rut': No es numérico [" + cliente[1].trim() + "]", CargaCompraException.ERROR_EN_CLIENTE);
			}
		}
		
		// Validación de Rut
		try {
			// Si rut no es 0 (no se puede validar un rut si está correcto, porque no hay dígito verificador)
			if (rut > 0) {
				ClienteCDP clienteCDP = new ClienteCDP(rut, Integer.parseInt(cliente[2].trim()));
				
				// Compara el rut con el rut en cache (rut leido anteriormente), por ejemplo si llegan
				// dos ruts: 10450450 y 104504500
				
				// Si el rut anterior es mayor que el que se analiza ahora, entonces es posible que
				// el anterior sea el rut incorrecto
				if (rut < rutCache) {
					
					// Se divide el rut en cache por 10, para dejarlo numéricamente comparable
					int rutComparator = (int) Math.rint((double) rutCache / 10);
					
					// Si el rut resulta ser el mismo, entonces el rut anterior era el incorrecto,
					// por lo tanto se debe reemplazar el antiguo registro con el rut nuevo, y eliminar el 
					// antiguo o se grabará un rut que no existe.
					if (rutComparator == rut) {
						if (clientes.get(rutCache + "") != null) {
							clienteCDP = (ClienteCDP) clientes.get(rutCache + "");
							clienteCDP.getMedioDePago().setCodigo(Integer.parseInt(cliente[2].trim()));
							clienteCDP.setRut(rut);
							clientes.remove(rutCache + "");
						}
					}
					clientes.put(rut + "", clienteCDP);
				} else if (rut > rutCache) {
					// Si el rut nuevo es mayor, entonces es probable que el rut anterior era el correcto
					// y hay que omitir el actual
					int rutComparator = (int) Math.rint((double) rut / 10);
					
					// Se divide por 10 el actual y se compara con el rut en cache, si no son iguales,
					// entonces se registra la información, porque es un rut distinto.
					if (rutComparator != rutCache) {
						clientes.put(rut + "", clienteCDP);
					}
				}
				
				// Se actualiza la información del rut en cache
				rutCache = rut;
			}
		} catch (NumberFormatException e) {
			throw new CargaCompraException("Campo 'CódigoMedioPagoCDP': No es numérico [" + cliente[2].trim() + "]", CargaCompraException.ERROR_EN_CLIENTE);
		}
	}
	
	/**
	 * Registra la información de los productos
	 * @param productos Mapa de Productos, para acumular productos iguales (suma cantidades)
	 * @param line Línea leida desde archivo
	 * @throws CargaCompraException Error al validar productos asociados a la compra
	 */
	private void fillProductos(HashMap productos, String line) throws CargaCompraException {
		
		// Si el vector de productos es nulo, quiere decir que no se registró un encabezado de compra, luego
		// los productos leidos andan "volando"
		if (productos == null) {
			throw new CargaCompraException("Encabezado de compra TK perdido", CargaCompraException.ERROR_EN_PRODUCTOS_SALTAR_REGISTRO);
		}
		
		// Separa información de acuerdo a separador "|"
		String[] producto = line.split("\\|");

		// Total de campos en Productos: 2 (Código EAN, Cantidad)
		if ((producto == null) && (producto.length != 2))
			throw new CargaCompraException("Información de producto inválida [" + line + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		
		String EAN = producto[1].trim();
		
		// Campo Código EAN es char(12)
		if (EAN.length() > 12)
			throw new CargaCompraException("Campo 'Código EAN': Largo distinto de 12 [" + EAN + "]", CargaCompraException.ERROR_EN_PRODUCTOS);

		double cantidad = 0;
		// Cantidad debe ser numérico
		try {
			cantidad = Double.parseDouble(producto[2].trim());
		} catch (NumberFormatException e) {
			throw new CargaCompraException("Campo 'Cantidad': No es numérico [" + producto[2].trim() + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		}
		
		// Cantidad debe ser > 0
		if (cantidad <= 0)
			throw new CargaCompraException("Campo 'Cantidad': Negativo o 0 [" + producto[2].trim() + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		
		// Los productos pueden llegar por separados en cantidad, es decir, pueden llegar varios productos
		// iguales, en cuyo caso se deben sumar las cantidades.
		ProductoCompraHistorica productoCompra = null;
		
		// Si producto no estaba ya registrado en la compra, crearlo. De lo contrario obtener registro
		if (productos.get(EAN) == null)
			productoCompra = new ProductoCompraHistorica();
		else
			productoCompra = (ProductoCompraHistorica) productos.get(EAN);
		productoCompra.setCodigoEAN(EAN);
		
		// A la cantidad leida desde el archivo se le suma la cantidad antigua (si estaba ya registrado el producto)
		cantidad += productoCompra.getCantidad();
		productoCompra.setCantidad(cantidad);
		productos.put(EAN, productoCompra);
	}
	
	/**
	 * Registra la información de los medios de pago asociados a la compra
	 * @param compraCDP Objeto con información de la compra
	 * @param line Línea leida desde archivo
	 * @return Objeto con información de compra actualizada
	 * @throws CargaCompraException Error al validar Medios de Pago asociados a la compra
	 */
	private CompraCDP fillMedioDePago(CompraCDP compraCDP, String line) throws CargaCompraException {

		// Separa información de acuerdo a separador "|"
		String[] medioDePago = line.split("\\|");
		
		// Total de campos en Medio de pago: 2 (Código SAP Medio de Pago, Importe)
		if ((medioDePago == null) && (medioDePago.length != 2))
			throw new CargaCompraException("Información en Medio de Pago inválida [" + line + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		
		String codigo = medioDePago[1].trim();

		// Campo Código Medio de Pago es char(2)
		if (codigo.length() != 2)
			throw new CargaCompraException("Campo 'Código SAP Medio de Pago': Largo distinto de 2 [" + codigo + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		
		// Valida que Importe sea numérico
		Integer code = new Integer(0);
		int importe = 0;
		try {
			importe = Integer.parseInt(medioDePago[2].trim());
		} catch (NumberFormatException e) {
			throw new CargaCompraException("Campo 'Importe': No es numérico [" + medioDePago[2].trim() + "]", CargaCompraException.ERROR_EN_PRODUCTOS);
		}
		
		compraCDP.setMontoTotal(compraCDP.getMontoTotal() + importe);
		return compraCDP;
		
	}
}
