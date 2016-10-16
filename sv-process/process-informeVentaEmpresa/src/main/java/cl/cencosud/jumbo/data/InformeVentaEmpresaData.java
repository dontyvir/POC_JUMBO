package cl.cencosud.jumbo.data;

import java.util.List;

import org.apache.commons.dbutils.handlers.MapListHandler;

import cl.cencosud.jumbo.util.JFactory;


public class InformeVentaEmpresaData extends GenericData {
	
	
	
	public InformeVentaEmpresaData(){
		super();
	}
		
	public List getVentaEmpresa(String fechaSolicitadaInicio, String fechaSolicitadaFin) {		
		  
		List Listinforme  = null;
		StringBuffer sql = JFactory.getStringBuffer();		
		
        try {
				
			sql.append(" SELECT distinct t.fcreacion ffact,case when p.TIPO_DOC='F' then 'Factura' else 'Boleta' end TIPODOCTO,t.pos_boleta NDOCUMENTO, jd.fecha , p.fcreacion, p.id_pedido, p.rut_cliente, p.nom_cliente, ");
			sql.append(" p.telefono, p.telefono2, t.pos_monto_fp, p.medio_pago, l.nom_local, c.nombre, r.reg_nombre, e.nombre estado ");
			sql.append(" FROM bodba.bo_pedidos p, bodba.bo_detalle_pedido dp, bodba.bo_trx_mp t, ");
			sql.append(" bodba.bo_jornada_desp jd, bodba.bo_locales l, bodba.bo_estados e, bodba.bo_comunas c, bodba.bo_regiones r ");
			sql.append(" WHERE p.id_pedido=dp.id_pedido and p.id_pedido=t.id_pedido ");
			sql.append(" and p.id_jdespacho=jd.id_jdespacho and p.id_local=l.id_local ");
			sql.append(" and p.id_estado=e.id_estado and p.id_comuna=c.id_comuna and c.reg_id=r.reg_id ");
			sql.append(" and p.id_estado between 4 and 10 ");
			if(fechaSolicitadaInicio != null && fechaSolicitadaFin != null){
				sql.append(" and t.fcreacion between '"+fechaSolicitadaInicio+"' and '"+fechaSolicitadaFin+"'");
			}else{
				sql.append(" and t.fcreacion between trim(year(current date) ||'-' || month(current date) ||'-01') and current date ");
			}
			sql.append(" and p.origen='V' WITH UR");
			
				
			if(logger.isDebugEnabled())	logger.debug(" getVentaEmpresa::SQL: " + sql.toString());
			Listinforme = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());

        } catch (Exception e) {
        	logger.error(InformeVentaEmpresaData.class.getName()+".getOpsCreadas :: Error: ", e);
        }
        return Listinforme;
	}


	

	
}
