package cl.cencosud.procesos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cl.cencosud.util.Parametros;

/**
 * Clase para el manejo de generación de archivos de Locales
 */
public class GeneraInterfaces {
	

	private static Logger logger = Logger.getLogger(GeneraInterfaces.class);
	public static void main(String[] args) {

		//logger.debug("Inicio proceso Generación archivo Local ");
		//System.out.print("args = " + args.length);
		try {
			
			//Borra las tablas BO_INT_? y genera las interfaces
			if(args.length == 0 || args[0].equals("")){
				
				GeneraInterfaces.productosEnMovil();
				
				 //parametro T significa todo
				GeneraInterfaces.archivoCarga("CATALOGO", "T");
				GeneraInterfaces.archivoCarga("COMUNAS", "T");
				GeneraInterfaces.archivoCarga("CRITERIOS", "T");
				GeneraInterfaces.archivoCarga("DISPONIBILIDAD", "T");
				GeneraInterfaces.archivoCarga("LOCALES", "T");
				GeneraInterfaces.archivoCarga("PRECIOS", "T");				
				GeneraInterfaces.archivoCarga("PROMOCIONES", "T");
				GeneraInterfaces.archivoCarga("PROMOCIONES_MATCH", "T");
				GeneraInterfaces.archivoCarga("REGIONES", "T");
				GeneraInterfaces.archivoCarga("CBARRA", "T");
			}else{
				String param = args[0].toUpperCase();
				char op = param.charAt(0);
				switch(op){
				case 'N': 
					GeneraInterfaces.productosEnMovil();
					//parametro N significa Novedades
					//genera todas las interfaces sólo con novedades
					
					GeneraInterfaces.archivoCarga("CATALOGO", "N");
					GeneraInterfaces.archivoCarga("COMUNAS", "N");
					GeneraInterfaces.archivoCarga("CRITERIOS", "N");
					GeneraInterfaces.archivoCarga("DISPONIBILIDAD", "N");
					GeneraInterfaces.archivoCarga("LOCALES", "N");
					GeneraInterfaces.archivoCarga("PRECIOS", "N");				
					GeneraInterfaces.archivoCarga("PROMOCIONES", "N");
					GeneraInterfaces.archivoCarga("PROMOCIONES_MATCH", "N");
					GeneraInterfaces.archivoCarga("REGIONES", "N");
					GeneraInterfaces.archivoCarga("CBARRA", "N");
					break;
				case 'C': 
					GeneraInterfaces.productosEnMovil();
					// genera las interfaces data solo con novedades
					GeneraInterfaces.archivoCarga("CATALOGO", "T");
					GeneraInterfaces.archivoCarga("DISPONIBILIDAD", "T");
					GeneraInterfaces.archivoCarga("PRECIOS", "T");	
					GeneraInterfaces.archivoCarga("CBARRA", "T");
					
					break;
				case 'S': 
					//genera las interfaces setup solo con novedades
					GeneraInterfaces.archivoCarga("COMUNAS", "T");
					GeneraInterfaces.archivoCarga("LOCALES", "T");
					GeneraInterfaces.archivoCarga("REGIONES", "T");
					GeneraInterfaces.archivoCarga("CRITERIOS", "T");
					break;
				case 'P': 
					
					GeneraInterfaces.productosEnMovil();
					// genera todas las interfaces de promociones
					GeneraInterfaces.archivoCarga("PROMOCIONES", "T");
					GeneraInterfaces.archivoCarga("PROMOCIONES_MATCH", "T");
					break;

				default :
					System.out.println("Opción No Válida");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	public static void archivoCarga(String interfaz, String tipo){
		
		try{			
			String timeStamp = getfechaReporte();//new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());			
			String resource = Parametros.getString("RESOURCES_GRABILITY");
			String path = Parametros.getString("PATH_ARCHIVOS_GRABILITY");
						
			String archivo = Parametros.getString(interfaz+".interfaz");
			String query = Parametros.getString(interfaz+".query");
			if("N".equals(tipo)){
				query 	= Parametros.getString(interfaz+".query_novedades");
			}

			String tablaBD = Parametros.getString(interfaz+".tabla_bd");
			Boolean formato = Boolean.valueOf(Parametros.getString(interfaz+".formato_montos"));
						
			logger.info("Inicio: "+archivo+" SQL:"+query+ "Tabla BD:"+tablaBD);
			GeneraArchivo.getFile(path, archivo, tablaBD, tipo, resource, timeStamp, query, formato.booleanValue());
			logger.info("FIN: "+archivo+" SQL:"+query+ "Tabla BD:"+tablaBD);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
	}
	
	/**Ingresa los productos a la tabla de productos movil en funcion de las categorias SAP
	Simpre corre pq puede existir un producto de baja. */
	private static int productosEnMovil() {
		
		int rows = 0;
		
		try {
			//Parametros BD 
			String user = Parametros.getString("USER");
			String pssw = Parametros.getString("PASSWORD");
			String drvr = Parametros.getString("DRIVER");
			String url  = Parametros.getString("URL");
		
			Class.forName(drvr);	
			Connection conn = DriverManager.getConnection(url, user, pssw);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String truncate="TRUNCATE TABLE FODBA.FO_PROD_MOVIL IMMEDIATE";
			PreparedStatement st = conn.prepareStatement(truncate);
			st.execute();
			st.close();
			
			String insert=" INSERT INTO FODBA.FO_PROD_MOVIL (PRO_ID_FO)" +
					" ("+
					" SELECT distinct FOP.PRO_ID FROM BODBA.BO_PRODUCTOS BOP" +
					" inner join BODBA.BO_CATEGORIA_PROD_MOVIL PCM on BOP.ID_CATPROD = PCM.ID_CATPROD" +
					" inner join FODBA.FO_PRODUCTOS FOP on FOP.PRO_ID_BO = BOP.ID_PRODUCTO" +
					" inner join FODBA.FO_PRECIOS_LOCALES PRE on PRE.PRE_PRO_ID = FOP.PRO_ID" +
					" where FOP.PRO_ESTADO = 'A' AND PRE.PRE_ESTADO = 'A' AND PRE.PRE_TIENESTOCK = 1 " +
					" )";
			PreparedStatement stm = conn.prepareStatement(insert);	
			rows =  stm.executeUpdate();
			
			if(stm != null)
				stm.close();
			
			if(conn != null){
				conn.commit();
				conn.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rows;	
	}
	
	public static String getfechaReporte() {		
	  	
		String fecha=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
        try {	
			
			//Parametros BD 
			String user = Parametros.getString("USER");
			String pssw = Parametros.getString("PASSWORD");
			String drvr = Parametros.getString("DRIVER");
			String url  = Parametros.getString("URL");
		
			Class.forName(drvr);	
			Connection conn = DriverManager.getConnection(url, user, pssw);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String sql = "SELECT VARCHAR_FORMAT(CURRENT TIMESTAMP, 'yyyymmddhh24MIss')  as fecha FROM SYSIBM.SYSDUMMY1 WITH UR";
			
			PreparedStatement stm = conn.prepareStatement(sql);	
			ResultSet rs =  stm.executeQuery();
			if(rs.next())
				fecha = rs.getString("fecha");
			
			if(rs != null)
				rs.close();
			
			if(stm != null)
				stm.close();
			
			if(conn != null)
				conn.close();			
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return fecha;
	}
}
