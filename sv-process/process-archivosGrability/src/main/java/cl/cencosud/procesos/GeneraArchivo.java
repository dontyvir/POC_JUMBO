package cl.cencosud.procesos;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.cencosud.util.Parametros;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * Clase para la generación de archivos
 */
public class GeneraArchivo {
	
	private static Logger logger = Logger.getLogger(GeneraInterfaces.class);

	/**
	 * Genera el archivo con los datos obtenidos del query indicado en el XML de
	 * configuración y lo deja en la ruta especificada.
	 * 
	 * @param path
	 *            ruta donde queda el archivo generado
	 * @param fileName
	 *            nombre del archivo generado
	 * @param resource
	 *            xml de configuración
	 * @param timeStamp
	 *            fecha y hora que se concatenan al nombre del archivo, sirve
	 *            para posterior manejo de los archivos
	 * @param queryName
	 *            nombre del query registrado en los archivos XML de
	 *            configuración
	 * @param formateaMontos
	 *            indica si se deben formatear los montos por cada resultado
	 *            obtenido en la ejecución del query
	 * @throws Exception
	 */
	static public void getFile(String path, String fileName, String interfaz,
			String tipo_interfaz, String resource, String timeStamp, 
			String queryName, boolean formateaMontos)
			throws Exception {

		BufferedWriter buffWriter = null;
		Reader buffReader = null;
		boolean hayDatos = false;
		Connection conn = null;
		OutputStreamWriter osw = null;
		Reader rd = null;
		SqlMapClient offerSqlMap = null;
		try {
			// Parámetros 
			String user = Parametros.getString("USER");
			String pssw = Parametros.getString("PASSWORD");
			String drvr = Parametros.getString("DRIVER");
			String url  = Parametros.getString("URL");
			
			Class.forName(drvr);
			conn = DriverManager.getConnection(url, user, pssw);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			rd = Resources.getResourceAsReader(resource);		 
			offerSqlMap = SqlMapClientBuilder.buildSqlMapClient(rd);
			offerSqlMap.flushDataCache();

			Map mapeo = (Map) offerSqlMap.queryForMap(queryName, "", "key");
			Map newDato = new HashMap();
			boolean hayNew = false;
			Map updDato = new HashMap();
			boolean hayUpdated = false;
			Map delDato = new HashMap();
			boolean hayDeleted = false;

			String nuevoStr = "";

			if (!mapeo.isEmpty()) {
				
				osw = new OutputStreamWriter(new FileOutputStream(path + "/" + fileName + "_"+ timeStamp + ".csv"), "utf-8");
				buffWriter = new BufferedWriter(osw);
				
				//4si acaso! Archivo bandera MFT
				//new FileOutputStream(path + "/" + fileName + "_"+ timeStamp + ".trg");
				
				Iterator iter = mapeo.keySet().iterator();

				while (iter.hasNext()) {
					
					//hayUpdated = true;
					String key = (String)iter.next();
					Map datos = (Map) mapeo.get(key);
					
					String estado = ("" + datos.get("estado")).toUpperCase();
					
					if(estado.equals("M") || estado.equals("N") || estado.equals("E")){ 
						/* EN ARCHIVO SIEMPRE SE INFORMAN TODOS LOS REGISTRO 
						 * MODIFICADOS, NUEVOS Y ELIMINADOS 
						 * */
						if(!hayDatos){
							hayDatos = true;
						}
						
						if (formateaMontos) {
							nuevoStr = formateaMontoEnCadena(""
									+ datos.get("valor"));
						} else {
							nuevoStr = "" + datos.get("valor");
						}
	
						buffWriter.write("" + nuevoStr + "|" + estado);
						buffWriter.newLine();
						
						// Maps para actualizar datos en la tabla de interfaz
						if(estado.equals("M")){
							updDato.put(key, datos);
							hayUpdated = true;
						}
						if(estado.equals("N")){
							newDato.put(key, datos);
							hayNew = true;
						}
						if(estado.equals("E")){
							delDato.put(key, datos);
							hayDeleted = true;
						}
					
					}
					
				}
				
				if(hayUpdated || hayNew || hayDeleted){
					// SI HAY DATOS PARA ACTUALIZAR EN LA TABLA DE INTERFAZ
					
					String inicio = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss").format(new Date());
					logger.info("Inicio insert  : " + inicio);
					
					if(tipo_interfaz.toUpperCase().equals("T")){
						/* SI INTERFAZ ES T (todo) SE BORRAN 
						 * LOS DATOS DE LA TABLA DE INTERFAZ
						 * */
						borraInterfaz(conn, interfaz);
					}
					// insertar datos en tabla de interfaz
					if(hayNew){
						insertaDatosInterfaz(conn, interfaz, newDato);
					}
					// actualizar datos en tabla de interfaz
					if(hayUpdated){
						actualizaDatosInterfaz(conn, interfaz, updDato);
					}
					// borrar datos en tabla de interfaz
					if(hayDeleted){
						borraDatosInterfaz(conn, interfaz, delDato);
					}
					
					//actualizaInterfaz(conn, interfaz);
					
					String fin = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss").format(new Date());
					logger.info("Fin insert : " + fin);
					
				}
				offerSqlMap.endTransaction();

			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(buffWriter != null)
				buffWriter.close();
			if(buffReader != null)
				buffReader.close();
			if(rd != null)
				rd.close();
			if(osw != null)
				osw.close();
			if(conn != null)
				conn.close();
		}
	}
	
	/**
	 * Toma el monto enviado por parámetro y lo formatea como número, lo
	 * devuelve como un string que incluye el monto con los puntos de mil
	 * correspondientes
	 * 
	 * @param value
	 *            String con el monto a formatear
	 * @return String con monto en formato 9.999.999
	 * @throws Exception
	 */
	static public String aplicaFormato(String value) throws Exception {
		String retorno = "";
		try {
			NumberFormat.getInstance();
			NumberFormat formateo = NumberFormat.getNumberInstance();

			retorno = formateo.format(Long.parseLong(value));
			return retorno;
		} catch (Exception e) {
			throw new Exception("GeneraArchivo.aplicaFormato Error al aplicar formato ", e);
		}
	}

	/**
	 * Busca los montos delimitados con '@' (ej. '@9999999@') dentro del String
	 * enviado como parámetro y reemplaza estos montos con los montos
	 * formateados ($9.999.999), la demás información no se modifica, devuelve
	 * el nuevo String resultante
	 * 
	 * @param line
	 *            String con los montos a formatear
	 * @return String con montos en formato $9.999.999
	 * @throws Exception
	 */
	static public String formateaMontoEnCadena(String line) throws Exception {

		String strLeft = ""; // the substring before '@'
		String amount = ""; // the number between '@'s, string to be formatted
		String strRight = ""; // the substring after '@'
		int leftIndex = 0; // position of '@' character in line parameter
		int rightIndex = 0; // position of a second '@' character in line
							// parameter

		try {
			for (int index = 0; index < line.length(); index++) {

				if (line.indexOf("@") != -1) {
					strLeft = line.substring(leftIndex, line.indexOf("@"));

					leftIndex = line.indexOf('@');

					strRight = line.substring(leftIndex + 1);

					String amountStr = strRight.substring(0,
							strRight.indexOf("@"));
					amount = aplicaFormato(amountStr);

					rightIndex = line.substring(leftIndex + 1).indexOf('@');

					strRight = strRight.substring(rightIndex + 1);

					line = strLeft + "$" + amount + " " + strRight;

					leftIndex = 0;
					rightIndex = 0;

				}

			}
			return line;
		} catch (Exception e) {
			throw new Exception("GeneraArchivo.formateaMontoEnCadena Error al formatear monto ",e);
		}
	}
	
	/**
	 * Borra de tabla de interfaz los datos eliminados 
	 * @param conn la conexión
	 * @param interfaz String con el nombre de la interfaz
	 * @param deleted Map con datos a eliminar
	 * @return
	 * @throws Exception
	 */
	static public void borraDatosInterfaz(Connection conn, String interfaz, Map deleted) throws Exception {
		PreparedStatement stm = null;
		int[] results = null;
		
		String sqlDel = "";
		String llave = "";
		int i = 0;
		int cont = 0;

		try {
			sqlDel = "DELETE bodba." + interfaz + " ";
			sqlDel += "WHERE ID_" + interfaz.substring(3) + " = ?";
			
			//System.out.println(" sql = " + sqlDel);
		
			stm = conn.prepareStatement(sqlDel);			
			Iterator iter = deleted.keySet().iterator();
			
			while (iter.hasNext()) {
				i++;
				String key2 = (String) iter.next();
				Map datos = (Map) deleted.get(key2);
				
				llave = (String)datos.get("key");
				stm.setString(1, llave);

				stm.addBatch();
				
				if(i % 1000 == 0){
					results = stm.executeBatch();
					cont += results.length;
					stm.clearBatch();
					//System.out.println(" Registros borrados = " + cont);
				}

			}
			//proceso el resto de los registros       
			results = stm.executeBatch();
			cont += results.length;
			
			logger.info(" Registros borrados = " + cont);
			conn.setAutoCommit(true);
			conn.commit();
			
			stm.clearBatch();
			stm.close();
			
		} catch (Exception e) {
			throw new Exception("borraDatosInterfaz Error al eliminar datos de tabla  " + interfaz, e);
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Insert datos en tabla de interfaz
	 * 
	 * @param conn la conexión
	 * @param interfaz String con el nombre de la interfaz
	 * @param news Map con datos a insertar
	 * @return
	 * @throws Exception
	 */
	static public void insertaDatosInterfaz(Connection conn, String interfaz, Map news) throws Exception {
		PreparedStatement stm = null;
		int[] results = null;
		
		String sqlIns = "";
		String llave = "";
		String dato = "";
		int i = 0;
		int cont = 0;
		
		try {
			conn.setAutoCommit(false);
			
			sqlIns  = "INSERT INTO bodba." + interfaz + " ";
		    sqlIns += "(ID_" + interfaz.substring(3) + ", INT_DATA, INT_FECHA_MOD) ";
		    sqlIns += "VALUES (?, ?, current timestamp)";
		    
			//System.out.println("sql = " + sqlIns);
		    
		    stm = conn.prepareStatement(sqlIns);
		    
		 // Obtengo fecha hora-actual como timestamp
			//Calendar calendar = Calendar.getInstance();
			//java.util.Date now = calendar.getTime();
			//java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		    
			Iterator iter = news.keySet().iterator();

			while (iter.hasNext()) {
				i++;
				String key = (String) iter.next();
				Map datos = (Map) news.get(key);
				
				llave = (String)datos.get("key");
				dato = (String)datos.get("valor");

				stm.setString(1,llave);
				stm.setString(2,dato);
				//stm.setTimestamp(3,currentTimestamp);
				
				stm.addBatch();
				
				if(i % 1000 == 0){
					results = stm.executeBatch();
					cont += results.length;
					stm.clearBatch();
					//System.out.println(" Registros insertados = " + cont);
				}

			}
			//proceso el resto de los registros       
			results = stm.executeBatch();
			cont += results.length;
			
			logger.info(" Registros insertados = " + cont);
			conn.setAutoCommit(true);
			conn.commit();
			
			stm.clearBatch();
			stm.close();
			
		} catch (Exception e) {
			throw new Exception("insertaDatosInterfaz Error al insertar datos en tabla " + interfaz, e);
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Actualiza datos en tabla de interfaz
	 * 
	 * @param conn la conexión
	 * @param interfaz String con el nombre de la interfaz
	 * @param news Map con datos a insertar
	 * @return
	 * @throws Exception
	 */
	static public void actualizaDatosInterfaz(Connection conn, String interfaz, Map updated) throws Exception {
		PreparedStatement stm = null;
		int[] results = null;
		
		String sqlUpd = "";
		String llave = "";
		String dato = "";
		int i = 0;
		int cont = 0;

		try {		
			
			sqlUpd  = "UPDATE bodba." + interfaz + " ";
			sqlUpd += "SET INT_DATA = ? , ";
			sqlUpd += "INT_FECHA_MOD = current timestamp ";
			sqlUpd += "WHERE ID_" + interfaz.substring(3) + " = ? ";
			
			//System.out.println("sqlUpd " + sqlUpd);
			
			stm = conn.prepareStatement(sqlUpd);
			
			// Obtengo fecha-hora actual como timestamp
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			
			Iterator iter = updated.keySet().iterator();
			
			while (iter.hasNext()) {
				i++;
				String key = (String) iter.next();
				Map datos = (Map) updated.get(key);
				
				llave = (String)datos.get("key");
				dato = (String)datos.get("valor");

				stm.setString(1,dato);
				//stm.setTimestamp(2,currentTimestamp);
				stm.setString(2,llave);

				stm.addBatch();
				
				if(i % 1000 == 0){
					results = stm.executeBatch();
					cont += results.length;
					stm.clearBatch();
					//System.out.println(" Registros modificados = " + cont);
				}

			}
			
			//proceso el resto de los registros       
			results = stm.executeBatch();
			cont += results.length;
			
			logger.info(" Registros modificados = " + cont);
			conn.setAutoCommit(true);
			conn.commit();
			
			stm.clearBatch();
			stm.close();
			
		} catch (Exception e) {
			throw new Exception("actualizaDatosInterfaz Error al actualizar datos en tabla " + interfaz, e);
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Borra todos los datos de la interfaz para insertar todo 
	 * @param conn la conexión
	 * @param interfaz String con el nombre de la interfaz
	 * @param deleted Map con datos a eliminar
	 * @return
	 * @throws Exception
	 */
	static public void borraInterfaz(Connection conn, String interfaz) throws Exception {
		String sqlDel = "";
		PreparedStatement stm = null;
		try {
				//sqlDel = "DELETE bodba." + interfaz + " ";
				sqlDel="TRUNCATE TABLE BODBA." + interfaz + " IMMEDIATE";
				stm = conn.prepareStatement(sqlDel);
				stm.execute();

		} catch (Exception e) {
			throw new Exception("borraInterfaz Error al eliminar datos de tabla  " + sqlDel, e);
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Borra todos los datos de la interfaz para insertar todo 
	 * @param conn la conexión
	 * @param interfaz String con el nombre de la interfaz
	 * @param deleted Map con datos a eliminar
	 * @return
	 * @throws Exception
	 */
	static public void actualizaInterfaz(Connection conn, String interfaz) throws Exception {
		String sql = "";
		CallableStatement cst = null;
		try {
			sql = "bodba.SP_" + interfaz.substring(3);
			cst = conn.prepareCall("{call " + sql + "}");
            // Ejecuta el procedimiento almacenado
            cst.execute();
            
            cst.close();
		} catch (Exception e) {
			logger.info("actualizaInterfaz error " + e);
			throw new Exception("actualizaInterfaz Error al ejecutar " + sql, e);
		}finally{
			if (cst != null) {
				try {
					cst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
