package cl.cencosud.jumbo.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import cl.cencosud.jumbo.util.ConnectionDB2;
import cl.cencosud.jumbo.util.JFactory;

/**
 * @author AGUTIERREZG
 * @version 1
 * @see Acceso a los datos para generar reporte cambio precio PM
 *
 */

public class CambioPrecioDao {
	
	protected static Logger logger = Logger.getLogger(CambioPrecioDao.class.getName());
	
	/**
	 * 
	 */
	public static void close() {ConnectionDB2.closeConnectionDB2();}
	
	/**
	 * @param days
	 * @return
	 */
	public static String getfechaReporte(String days) {		
		  	
		String fecha="";
		Connection conn = JFactory.getConnectionDB2();
		
        try {
        	days = ("0".equals(days))?"+0":days;
			String sql = "SELECT CHAR((current date "+days+" day), ISO) FROM SYSIBM.SYSDUMMY1 WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" getfechaReporte::SQL: " + sql);
			Object[] array = (Object[]) JFactory.getQueryRunner().query(conn, sql, new ArrayHandler());
			fecha = String.valueOf(array[0]);
			
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getfechaReporte :: Error: ", e);
        }
        return fecha;
	}	
	
	public static boolean isActivaCorreccionAutomatica() {		
	  	
		boolean isActiva = false;
		Connection conn = JFactory.getConnectionDB2();
		
        try {
			String sql = "SELECT ID_PARAMETRO, NOMBRE, VALOR FROM BO_PARAMETROS WHERE NOMBRE='ACTIVA_CORRECCION_EXCESO' WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" isActivaCorreccionAutomatica::SQL: " + sql);
			Object[] array = (Object[]) JFactory.getQueryRunner().query(conn, sql, new ArrayHandler());
			String activa = (array != null && array.length > 0)? String.valueOf(array[2]):"";
			isActiva = ("1".equals(activa))?true:false;
			
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".isActivaCorreccionAutomatica :: Error: ", e);
        }
        return isActiva;
	}
	
	/**
	 * @param fechaSolicitada
	 * @return
	 */
	public static List getOpsConCambioPrecioPorDia(String fechaSolicitada) {
		
		List ListResulSet  = new ArrayList();
		Connection conn = JFactory.getConnectionDB2();
		
        try {            
        	String fechaInicio=fechaSolicitada+" 00:00:00";
        	String fechaFin=fechaSolicitada+" 23:59:59";
        	
        	String sql = "SELECT distinct(ID_PEDIDO) FROM BO_TRACKING_OD " +
        			"WHERE USUARIO='EXCESO' and FECHA between '"+fechaInicio+"' and '"+fechaFin+"' WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" getOpsConCambioPrecioPorDia::SQL: " + sql.toString());
			ListResulSet = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new ArrayListHandler());
						
 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getOpsConCambioPrecioPorDia Error: ", e);
        }

        return ListResulSet;
	}
	
	/**
	 * @param op
	 * @param sector
	 * @return
	 */
	public static List getDetallePedidoConCambioPrecioPorSector(long op, long sector) {
		
		List ListResulSet  = new ArrayList();
		Connection conn = JFactory.getConnectionDB2();
		
        try {            
        	
        	String sql = "SELECT distinct(dp.ID_DETALLE) as ID_DETALLE FROM  BO_DETALLE_PEDIDO dp" +
        			" inner join BO_DETALLE_PICKING dpk on dpk.ID_DETALLE= dp.ID_DETALLE" +
        			" and dp.ID_PEDIDO="+op+" and dp.ID_SECTOR="+sector+" and dpk.PRECIO_PICK is not null " +
        			" order by ID_DETALLE asc WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" getOpsConCambioPrecioPorSector::SQL: " + sql.toString());
			ListResulSet = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new ArrayListHandler());
						
 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getOpsConCambioPrecioPorSector Error: ", e);
        }

        return ListResulSet;
	}
	
	/**
	 * @return
	 */
	public static List getSectoresProductos() {
		List ListResulSet  = new ArrayList();		
		Connection conn = JFactory.getConnectionDB2();
		
        try {          	
        	String sql = "SELECT ID_SECTOR, NOMBRE, PRIORIDAD, MAX_OP, MAX_PROD, MIN_OP_FILL, CANT_MIN_PRODS FROM BO_SECTOR WITH UR";
			
        	if(logger.isDebugEnabled())	logger.debug(" getSectoresProductos::SQL: " + sql.toString());
        	ListResulSet = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
						 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getSectoresProductos Error: ", e);
        }

        return ListResulSet;
	}
	
	/**
	 * @param op
	 * @return
	 */
	public static Object[] getInfoOP(long op) {
		Object[]  ListResulSet  = new Object[9];
		Connection conn = JFactory.getConnectionDB2();
        try {            
        	
        	String sql = "SELECT ID_PEDIDO, ID_ESTADO, ID_LOCAL, FCREACION, MONTO_PEDIDO, " +
        			"MONTO_RESERVADO, COSTO_DESPACHO, RUT_CLIENTE, DV_CLIENTE FROM BO_PEDIDOS WHERE ID_PEDIDO = "+op+" WITH UR";
			
        	if(logger.isDebugEnabled())	logger.debug(" getInfoOP::SQL: " + sql.toString());
        	ListResulSet = (Object[]) JFactory.getQueryRunner().query(conn, sql.toString(), new ArrayHandler());
						 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getInfoOP Error: ", e);
        }

        return ListResulSet;
	}
	
	/**
	 * @param idDetalle
	 * @return
	 */
	public static Object[] getDetallePedidoPorIdDetalle(long idDetalle) {
		Object[]  ListResulSet  = new Object[5];
		Connection conn = JFactory.getConnectionDB2();
        try {            
        	
        	String sql = "SELECT ID_DETALLE, COD_PROD1, DESCRIPCION, CANT_SOLIC, PRECIO " +
        			"FROM BO_DETALLE_PEDIDO WHERE ID_DETALLE = "+idDetalle+" WITH UR";
			
        	if(logger.isDebugEnabled())	logger.debug(" getDetallePedidoPorIdDetalle::SQL: " + sql.toString());
        	ListResulSet = (Object[]) JFactory.getQueryRunner().query(conn, sql.toString(), new ArrayHandler());
						 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getDetallePedidoPorIdDetalle Error: ", e);
        }

        return ListResulSet;
	}

	/**
	 * @param idDetalle
	 * @return
	 */
	public static List getDetallePickingPorIdDetalle(long idDetalle) {
		List ListResulSet  = new ArrayList();
		Connection conn = JFactory.getConnectionDB2();
        try {            
        	
        	String sql = "SELECT CBARRA, DESCRIPCION, CANT_PICK, PRECIO, PRECIO_PICK" +
        			" FROM BO_DETALLE_PICKING WHERE ID_DETALLE = "+idDetalle+" WITH UR";
			
        	if(logger.isDebugEnabled())	logger.debug(" getDetallePickingPorIdDetalle::SQL: " + sql.toString());
        	ListResulSet = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
						 
        } catch (Exception e) {
        	logger.error(CambioPrecioDao.class.getName()+".getDetallePickingPorIdDetalle Error: ", e);
        }

        return ListResulSet;
	}
	
	public static List getMailPM(String strCargos) {

		List ListResulSet  = new ArrayList();
		Connection conn = JFactory.getConnectionDB2();

		try {
			
			String SQLStmt = " SELECT BO_USUARIOS.EMAIL as MAILPM" +
					" FROM BO_CARGOS" +
					" INNER JOIN BO_USUARIOS    ON BO_CARGOS.ID_CARGO=BO_USUARIOS.ID_CARGO" +
					" WHERE BO_CARGOS.NOMBRE_CARGO in ("+strCargos+") WITH UR ";

			if (logger.isDebugEnabled())
				logger.debug(" getMailPM::SQL: "+ SQLStmt.toString());
			ListResulSet = (List) JFactory.getQueryRunner().query(conn,	SQLStmt, new MapListHandler());

		} catch (Exception e) {
			logger.error(CambioPrecioDao.class.getName()+ ".getMailPM Error: ", e);
		}
		
		return ListResulSet;

	}
	

}
