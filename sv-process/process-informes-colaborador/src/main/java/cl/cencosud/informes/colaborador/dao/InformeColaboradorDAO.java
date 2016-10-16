package cl.cencosud.informes.colaborador.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import cl.cencosud.informes.colaborador.util.JFactory;
import cl.cencosud.informes.colaborador.util.Util;

/**
 * Clase que se encarga de obtener el acceso a datos para 
 * recuperar el informe desde la base de datos
 *
 */
public class InformeColaboradorDAO {
	
	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(InformeColaboradorDAO.class.getName());
	
	private Connection conn;
	
	public InformeColaboradorDAO() {
		this.conn = JFactory.getConnectionDB2();
	}
	
	/**
	 * Método que se encarga de recuperar el reporte de ventas descuento colaborador.
	 * @return <code>List</code> Listado de registros
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public List getReporteVentaDescuentoColaborador()throws SQLException{
		
		logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Ingreso al metodo");
		
		List 			reporte  = null;

		try {
			
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Recupero la query para el reporte");
			StringBuffer sql = getQueryReporteDescuentoColaborador();
			
			logger.info("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] la query a ejecutar es " + sql);
			
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Realizo llamado a obtener el reporte");
			reporte = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
		
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Obtuve resultado.");
			
		} catch (SQLException e) {
			
			logger.error("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Error al recuperar el reporte", e);
			throw e;
			
		}
		
		logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Finaliza el método.");
		return reporte;
	}

	public List descuentoPorId(long id_pedido)throws SQLException{
		logger.debug("[InformeColaboradorDAO][descuentoPorId] Ingreso al metodo");
		
		List 			descuentos  = null;
		try {
			
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Recupero la query para el reporte");
			StringBuffer sql = getDescuentoPorVenta(id_pedido);
			
			logger.info("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] la query a ejecutar es " + sql);
			
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Realizo llamado a obtener el reporte");
			descuentos = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
		
			logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Obtuve resultado.");
			
		} catch (SQLException e) {
			
			logger.error("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Error al recuperar el reporte", e);
			throw e;
			
		}
		
		logger.debug("[InformeColaboradorDAO][getReporteVentaDescuentoColaborador] Finaliza el método.");
		return descuentos;
	}
	
	
	private StringBuffer getDescuentoPorVenta(long id_pedido){
		StringBuffer sql = JFactory.getStringBuffer();
		sql.append("SELECT DPE.ID_PEDIDO,CAST(((dpe.CANT_SOLIC * dpe.PRECIO) - (DPK.CANT_PICK * DPK.PRECIO)) AS INT) AS DIF_PRECIO ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_DETALLE_PICKING DPK INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DPK.ID_DETALLE = DPE.ID_DETALLE ");
		sql.append("\n");
		sql.append("WHERE DPE.ID_PEDIDO = ");
		sql.append(id_pedido);
		sql.append(" AND (DPE.PRECIO * DPE.CANT_SOLIC ) > (DPK.PRECIO * DPK.CANT_PICK) AND DPK.SUSTITUTO = 'S' ");
		sql.append("\n");
		sql.append("UNION SELECT DPE.ID_PEDIDO,CAST(((dpe.CANT_SOLIC * dpe.PRECIO) - (DPK.CANT_PICK * DPK.PRECIO)) AS INT) AS DIF_PRECIO ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_DETALLE_PICKING DPK INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DPK.ID_DETALLE = DPE.ID_DETALLE WHERE DPE.ID_PEDIDO = ");
		sql.append(id_pedido);
		sql.append("\n");
		sql.append("AND (DPE.PRECIO * DPE.CANT_SOLIC ) = (DPK.PRECIO * DPK.CANT_PICK) AND DPK.SUSTITUTO = 'S' ");
		return sql;
		
	}
	
	
	/**
	 * Método que se encarga de recuperar la query para el reporte de descuento colaborador.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryReporteDescuentoColaborador() {
		StringBuffer 	sql 	 = JFactory.getStringBuffer();
		
		/*
		 * Se recuperan las siguientes columnas
		 * - Rut con digito verificados
		 * - Nombre del cliente.
		 * - Total bruto de la compra. Esta se calcula : Precio lista * (Cantidad solicitada - cantidad faltante)
		 * - Total con descuento. Esta se calcula: total bruto - descuento.
		 * - Desceunto. Esta se calcula: (Precio de lista - precio)*(cantidad solicitada - cantidad faltante)
		 * - Nombre del local de la compra.
		 * - Fecha de la compra.
		 * - OP
		 */
		sql.append("SELECT CONCAT(CONCAT(RTRIM(CAST(A.RUT_CLIENTE AS CHAR(12))),'-'), A.DV_CLIENTE) RUT, A.NOM_CLIENTE NOMBRE, ");
		sql.append("\n");
		sql.append("       (COALESCE(SUM(CAST(ROUND((B.PRECIO_LISTA*(B.CANT_SOLIC)),0) AS INT)), 0) +1) TOTAL_BRUTO,  ");
		sql.append("\n");
		sql.append("         A.MONTO_RESERVADO TOTAL_RESERVADO,  ");
		sql.append("\n");
		sql.append("       ROUND(COALESCE(SUM((B.PRECIO_LISTA - B.PRECIO) * (B.CANT_SOLIC)), 0),0) TOTAL_DESCUENTO_RESERVADO,  ");
		sql.append("\n");
		sql.append("       E.NOM_LOCAL LOCAL, A.FCREACION FECHA_COMPRA, A.ID_PEDIDO OP, F.POS_MONTO_FP TOTAL_REAL, ");
		sql.append("\n");
		sql.append("      CAST(ROUND(COALESCE((SUM(((CAST(B.PRECIO_LISTA AS INT)) - (CAST(B.PRECIO AS INT))) * (B.CANT_SOLIC - B.CANT_FALTAN))),0), 0)AS INT) DESCUENTO_FINAL ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_PEDIDOS A ");
		sql.append("\n");
		sql.append("INNER JOIN BODBA.BO_DETALLE_PEDIDO B ON A.ID_PEDIDO = B.ID_PEDIDO ");
		sql.append("\n");
		sql.append("INNER JOIN BODBA.BO_TRX_MP F ON F.ID_PEDIDO = A.ID_PEDIDO");
		sql.append("\n");
		sql.append("INNER JOIN FODBA.FO_COLABORADOR D ON D.COL_RUT = A.RUT_CLIENTE ");
		sql.append("\n");
		sql.append("INNER JOIN BODBA.BO_LOCALES E ON E.ID_LOCAL = A.ID_LOCAL ");
		
		String day = Util.getDiaActual();
		logger.info("[InformeColaboradorDAO][getQueryReporteDescuentoColaborador] today is " + day);
		if(Integer.parseInt(day) > 15){
			
			// En caso de ser mayor al dia 15, la consulta se realizará entre el primer día del mes en curso
			// y el día 15 del mes en curso.
			sql.append("WHERE A.FCREACION BETWEEN ((CURRENT DATE + 1 DAY) - (DAY(CURRENT DATE)) DAY)  ");
			sql.append("\n");
			sql.append("                  AND ((CURRENT DATE + 1 DAY) - (DAY(CURRENT DATE)) DAY) + 14 DAY ");
		
			
		}else{
			
			// En caso de ser menor, la consulta se realizará entre el día 16 del mes anterior y el ultimo día del 
			// mes anterior.
			sql.append("WHERE A.FCREACION BETWEEN ((CURRENT DATE + 1 DAY) - (DAY(CURRENT DATE)) DAY - 1 MONTH)  + 15 DAY ");
			sql.append("\n");
			sql.append("                  AND (CURRENT DATE) - (DAY(CURRENT DATE)) DAY");
		
			
		}
		sql.append("\n");
		sql.append("AND A.ID_ESTADO IN ("+Util.getPropertiesString("reporte.estados")+") ");
		sql.append("\n");
		sql.append("GROUP BY A.RUT_CLIENTE , A.NOM_CLIENTE , E.NOM_LOCAL    , A.FCREACION   , A.ID_PEDIDO	, A.DV_CLIENTE, F.POS_MONTO_FP, A.MONTO_RESERVADO ");
		sql.append("\n");
		sql.append("ORDER BY A.FCREACION WITH UR");
		
		return sql;
	}
	
	
}
