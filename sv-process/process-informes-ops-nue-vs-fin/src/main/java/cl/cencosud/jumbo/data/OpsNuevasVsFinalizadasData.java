package cl.cencosud.jumbo.data;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import cl.cencosud.jumbo.util.JFactory;


public class OpsNuevasVsFinalizadasData {
	
	protected static Logger logger = Logger.getLogger(OpsNuevasVsFinalizadasData.class.getName());
	
	public static String getfechaReporte(Connection conn) {		
		  	
		String fecha="";
		
        try {
			String sql = "SELECT (current date - 1 day) as fecha FROM SYSIBM.SYSDUMMY1 WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" getfechaReporte::SQL: " + sql);
			Object[] array = (Object[]) JFactory.getQueryRunner().query(conn, sql, new ArrayHandler());
			fecha = String.valueOf(array[0]);
			
        } catch (Exception e) {
        	logger.error(OpsNuevasVsFinalizadasData.class.getName()+".getfechaReporte :: Error: ", e);
        }
        return fecha;
	}
	
	public static List getOpsCreadas(Connection conn, String fechaSolicitada) {		
		  
		List List_OpsCreadas  = null;
		StringBuffer sql = JFactory.getStringBuffer();		
		
        try {
			sql.append("SELECT ");
			sql.append(" p.id_pedido as id_pedido, replace(CHAR(p.fcreacion,LOCAL),'/','-') as fcreacion, p.hcreacion as hcreacion, ");
			sql.append(" p.id_local as id_local, p.rut_cliente as rut_cliente, p.dv_cliente as dv_cliente,");
			sql.append(" INTEGER(p.monto_reservado) as monto_reserva, p.medio_pago as medio_pago, p.id_estado as id_estado, p.id_jdespacho as id_jdespacho,");			
			sql.append(" l.cod_local as cod_local, l.nom_local as nombre_local, e.nombre as nombre_estado, replace(CHAR(d.fecha,LOCAL),'/','-') as fecha_despacho");

			sql.append(" FROM bodba.bo_pedidos p, bodba.bo_locales l, bodba.bo_jornada_desp d, bodba.bo_estados e");
			
			sql.append(" WHERE p.id_estado not in (1,68,69)");
			
			if(fechaSolicitada == null){
				sql.append(" AND p.fcreacion=current date - 1 day ");
			}else{
				sql.append(" AND p.fcreacion='"+fechaSolicitada+"'");
			}

			sql.append(" AND p.id_local=l.id_local  ");
			sql.append(" AND p.id_jdespacho=d.id_jdespacho");
			sql.append(" AND p.id_estado=e.id_estado  ");
			sql.append(" ORDER BY id_pedido WITH UR");
			
			if(logger.isDebugEnabled())	logger.debug(" getOpsCreadas::SQL: " + sql.toString());
			List_OpsCreadas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());

        } catch (Exception e) {
        	logger.error(OpsNuevasVsFinalizadasData.class.getName()+".getOpsCreadas :: Error: ", e);
        }
        return List_OpsCreadas;
	}
	
	public static List getOpsFinalizadas(Connection conn, String fechaSolicitada) {
		
		List List_OpsCerradas  = null;
		StringBuffer sql = JFactory.getStringBuffer();
		
        try {            
            sql.append("SELECT ");
			sql.append(" p.id_pedido as id_pedido, p.id_estado as id_estado, p.id_local as id_local, p.medio_pago as medio_pago,");
			sql.append(" p.rut_cliente as rut_cliente, p.dv_cliente as dv_cliente,");
			sql.append(" l.cod_local as cod_local, l.nom_local as nombre_local, t.fecha as fecha_finalizacion,");		
			sql.append(" e.nombre as nombre_estado, mp.id_trxmp as idtrxmp, INTEGER(mp.pos_monto_fp) as monto_cobrado, cl.cli_email as cli_email,");		 
			sql.append(" bp.cat_codigo_autorizacion as cat_cod, ");
			sql.append(" substr(bp.cat_nro_tarjeta,length(bp.cat_nro_tarjeta)-3, 4) as cat_final, ");	
			sql.append(" wp.tbk_codigo_autorizacion as tbk_cod, ");
			sql.append(" wp.tbk_final_numero_tarjeta as tbk_final");
			
			sql.append(" FROM bodba.bo_pedidos p ");			
			sql.append(" LEFT JOIN bodba.bo_tracking_od t on p.id_pedido=t.id_pedido");
			sql.append(" LEFT JOIN bodba.bo_locales l on p.id_local=l.id_local");
			sql.append(" LEFT JOIN bodba.bo_estados e on p.id_estado=e.id_estado");
			sql.append(" LEFT JOIN bodba.botonpagocat bp on p.id_pedido = bp.id_pedido");
			sql.append(" LEFT JOIN bodba.webpays wp on p.id_pedido = wp.id_pedido");
			sql.append(" LEFT JOIN fodba.fo_clientes cl on p.id_cliente = cl.cli_id");
			sql.append(" LEFT JOIN bodba.bo_trx_mp mp on p.id_pedido=mp.id_pedido");
			
			sql.append(" WHERE  descripcion='[BOC] FINALIZADO.' ");
			
			if(fechaSolicitada == null){
				sql.append(" AND date(t.fecha)=current date-1 day ");
			}else{
				//fecha Ej: 2013-09-11 anno-mes-dia
				sql.append(" AND fecha between '"+fechaSolicitada+" 00:00:00' and '"+fechaSolicitada+" 23:59:59' ");
				//sql.append(" AND date(t.fecha)='"+fechaSolicitada+"' ");
			}
			sql.append(" AND p.id_estado=10 ");
			sql.append(" ORDER BY t.fecha  WITH UR");
			
			if(logger.isDebugEnabled())	logger.debug(" getOpsFinalizadas::SQL: " + sql.toString());
			List_OpsCerradas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
 
        } catch (Exception e) {
        	logger.error(OpsNuevasVsFinalizadasData.class.getName()+".getOpsFinalizadas Error: ", e);
        }

        return List_OpsCerradas;
	}
	
}
