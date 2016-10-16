package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.common.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.CodBarraSapEntity;
import cl.bbr.jumbocl.common.model.DetallePickingEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.FacturaEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.common.model.ProductosPedidoEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.NumericUtils;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoIndicDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.pedidos.dto.AlertaDTO;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.BinCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioSustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturaDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JDespachoTrackingDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ModificacionPrecioDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.MotivoDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDesp;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCotizacionesDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PickingCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaNutricionalDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoPromoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoSinStockDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoSobreMargenDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosBinDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.TPBinOpDTO;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonasPorComunaVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.UsuariosDAOException;
import cl.bbr.jumbocl.pedidos.promos.dto.CuponPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.DetPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.MonitorPromocionesDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PedidoDatosSocketDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PrioridadPromosDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductoPromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductosRelacionadosPromoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromoMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.Promocion;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ResumenPedidoPromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.SafDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.TcpPedidoDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.promo.lib.dto.ProductoCatalogoExternoDTO;
import cl.bbr.promo.lib.dto.ProductoStockDTO;


/**
 * Clase que permite consultar los Pedidos que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcPedidosDAO implements PedidosDAO {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null; 

	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	public static final String GET_COD_CUPON = "SELECT codigo FROM BODBA.BO_CUPON_DSCTOXPEDIDO cdp "+
											   "inner join BODBA.BO_CUPON_DSCTO cd on cd.id_cup_dto = cdp.ID_CUPON "+
											   "WHERE cdp.ID_PEDIDO = ? WITH UR";
	

	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		
		logger.debug("Conexion usada por el dao: " + conn);
		
		return this.conn;

	}

	// ************ Métodos Publicos *************** //
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 * 
	 * 
	 */
	private void releaseConnection(){
		if ( trx == null ){
            try {
            	if (conn != null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage(), e);

            }
		}
			
	}
		
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws PedidosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new PedidosDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcPedidosDAO(){
		
	}
	//	 ********************************************************** //		
	
	/**
	 * Obtiene listado del log de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List LogPedidoDTO
	 * @throws PedidosDAOException 
	 */
	public List getLogPedido(long id_pedido) throws PedidosDAOException{
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String cadQuery=" SELECT l.id_tracking id_tracking, " +
							"	l.usuario usuario, " +
							"	l.fecha fecha, " +
							"	l.descripcion descripcion, " +
							"	l.id_mot id_mot, " +
							"	m.nombre motivo, " +
							"	l.id_mot_ant id_mot_ant, " +
							"	mant.nombre motivo_ant  " +
							" FROM bo_tracking_od l" +
							" LEFT JOIN BO_MOTIVO m ON m.id_mot=l.id_mot " +
							" LEFT JOIN BO_MOTIVO mant ON mant.id_mot=l.id_mot_ant " +														
							" WHERE id_pedido = ? " +
							" ORDER BY fecha desc";
			
			conn = this.getConnection();
			stm = conn.prepareStatement( cadQuery + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + stm.toString());

			rs = stm.executeQuery();
			
			while (rs.next()) {
				LogPedidoDTO log1 = new LogPedidoDTO();
				log1.setId_log(rs.getLong("id_tracking"));
				log1.setUsuario(rs.getString("usuario"));
				log1.setLog(rs.getString("descripcion"));
				log1.setFecha(rs.getString("fecha"));
				log1.setId_motivo(rs.getLong("id_mot"));
				log1.setMotivo(rs.getString("motivo"));
				log1.setId_motivo_anterior(rs.getLong("id_mot_ant"));
				log1.setMotivo_anterior(rs.getString("motivo_ant"));
				
				result.add(log1);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;

	}

	
	/**
	 * Obtiene listado del log de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List LogPedidoDTO
	 * @throws PedidosDAOException 
	 */
	public HashMap getListadoOP(Calendar cal, int id_local, String hora_desp) throws PedidosDAOException{
	    HashMap lstPedidos = null;
	    PreparedStatement stm = null;
		ResultSet rs=null;
		String filtroLocal = "";
		String filtroHoraDesp = "";

		try {
            DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
            String FechaQuery = DateFormatQuery.format(cal.getTime()).toUpperCase();
			
            if (!hora_desp.equalsIgnoreCase("todos")){
                String []hora = hora_desp.split("-");
                filtroHoraDesp = "  AND HD.HINI = TIME('" + hora[0] + "') "
                               + "  AND HD.HFIN = TIME('" + hora[1] + "') ";
            }
            
            if (id_local > 0){
                filtroLocal = "  AND P.ID_LOCAL = " + id_local + " ";
            }

			conn = this.getConnection();
			String SQL = "SELECT JD.FECHA "
	                   + "      ,CASE WHEN (P.ORIGEN = 'W') THEN 'Jumbo.cl' "
	                   + "            ELSE 'Venta Empresas' "
	                   + "       END AS ORIGEN "
	                   + "      ,UPPER(L.NOM_LOCAL) AS LOCAL "
	                   + "      ,UPPER(COM.NOMBRE) AS COMUNA "
	                   + "      ,UPPER(Z.DESCRIPCION) AS ZONA "
	                   + "      ,CONCAT(CONCAT(CONCAT(' ', SUBSTR(CHAR(HD.HINI),1, 2)), ' a '), CONCAT(SUBSTR(CHAR(HD.HFIN), 1, 2), ' ')) AS HORARIO "
	                   + "      ,E.NOMBRE AS ESTADO "
	                   + "      ,P.ID_PEDIDO AS OP "
	                   + "      ,CASE WHEN (P.TIPO_DESPACHO = 'N') THEN ' ' " //NORMAL
	                   + "            WHEN (P.TIPO_DESPACHO = 'C') THEN 'E' " //ECONÓMICO
	                   + "            WHEN (P.TIPO_DESPACHO = 'E') THEN 'EX' " //EXPRESS
	                   + "            WHEN (P.TIPO_DESPACHO = 'R') THEN 'RL' " //RETIRO EN LOCAL
	                   + "       END AS TIPO_DESPACHO "
	                   + "      ,CASE WHEN (P.CANT_BINS IS NULL) THEN 0 "
	                   + "            ELSE P.CANT_BINS "
	                   + "       END AS CANT_BINS "
	                   + "      ,CASE WHEN (COUNT(TMP.POS_MONTO_FP) > 0) THEN SUM(INT(TMP.POS_MONTO_FP)) "
	                   + "            ELSE 0 "
	                   + "       END AS POS_MONTO "
	                   + "      ,CASE WHEN (TMP.POS_FECHA IS NULL) THEN '-' "
	                   + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(SUBSTR(TMP.POS_FECHA, 5,2), '/'), SUBSTR(TMP.POS_FECHA, 3,2)), '/20'), SUBSTR(TMP.POS_FECHA, 1,2)) "
	                   + "       END AS POS_FECHA "
	                   + "      ,CASE WHEN (TMP.POS_HORA IS NULL) THEN '-' "
	                   + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(SUBSTR(TMP.POS_HORA, 1,2), ':'), SUBSTR(TMP.POS_HORA, 3,2)), ':'), SUBSTR(TMP.POS_HORA, 5,2)) "
	                   + "       END AS POS_HORA "
	                   + "      ,CASE WHEN (AP.ID_ALERTA IS NULL) THEN '-' "
	                   + "            ELSE 'SÍ' "
	                   + "       END AS PRIMERA_COMPRA "
	                   + "      ,UPPER(P.MEDIO_PAGO) AS MEDIO_PAGO "
	                   + "      ,UPPER(P.NOM_CLIENTE) AS NOM_CLIENTE "
	                   + "      ,CASE WHEN (C.CLI_EMAIL IS NULL) THEN '-' "
	                   + "            ELSE UPPER(C.CLI_EMAIL) "
	                   + "       END AS EMAIL "
	                   + "      ,CASE WHEN (P.DIR_DEPTO = '') THEN CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(', P.DIR_TIPO_CALLE), ') '), UPPER(P.DIR_CALLE)), '  #'), P.DIR_NUMERO) "
	                   + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(', P.DIR_TIPO_CALLE), ') '), UPPER(P.DIR_CALLE)), '  #'), P.DIR_NUMERO), '   DEPTO. N° '), P.DIR_DEPTO) "
	                   + "       END AS DIRECCION "
	                   + "      ,CASE WHEN (C.CLI_FON_NUM_2 = '' AND C.CLI_FON_NUM_3 = '') THEN "
	                   + "                    CONCAT(CONCAT(CONCAT('(',C.CLI_FON_COD_1), ') '), C.CLI_FON_NUM_1) "
	                   + "            WHEN (C.CLI_FON_NUM_3 = '') THEN "
	                   + "                    CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(',C.CLI_FON_COD_1), ') '), C.CLI_FON_NUM_1), '  /  '), '('), C.CLI_FON_COD_2), ') '), C.CLI_FON_NUM_2) "
	                   + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(',C.CLI_FON_COD_1), ') '), C.CLI_FON_NUM_1), '  /  '), '('), C.CLI_FON_COD_2), ') '), C.CLI_FON_NUM_2), '  /  '), '('), C.CLI_FON_COD_2), ') '), C.CLI_FON_NUM_2) "
	                   + "       END AS TELEFONOS "
	                   + "      ,CASE WHEN (UPPER(P.NOM_TBANCARIA) <> '[NULL]') THEN UPPER(P.NOM_TBANCARIA) "
	                   + "            ELSE 'CHEQUE' "
	                   + "       END AS TIPO_TARJETA "
	                   + "FROM BODBA.BO_PEDIDOS P "
	                   + "     JOIN BODBA.BO_JORNADA_DESP   JD ON JD.ID_JDESPACHO = P.ID_JDESPACHO AND JD.ID_JPICKING = P.ID_JPICKING "
	                   + "     JOIN BODBA.BO_HORARIO_DESP   HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP "
	                   + "     JOIN BODBA.FO_CLIENTES        C ON C.CLI_ID        = P.ID_CLIENTE "
	                   + "     JOIN BODBA.BO_COMUNAS       COM ON COM.ID_COMUNA   = P.ID_COMUNA "
	                   + "     LEFT JOIN BODBA.BO_TRX_MP   TMP ON TMP.ID_PEDIDO   = P.ID_PEDIDO "
	                   + "     JOIN BODBA.BO_LOCALES         L ON L.ID_LOCAL      = P.ID_LOCAL "
	                   + "     JOIN BODBA.BO_ZONAS           Z ON Z.ID_ZONA       = P.ID_ZONA "
	                   + "     LEFT JOIN BODBA.BO_ALERTA_OP AP ON AP.ID_PEDIDO    = P.ID_PEDIDO    AND AP.ID_ALERTA = 1 "
	                   + "     JOIN BODBA.BO_ESTADOS         E ON E.ID_ESTADO     = P.ID_ESTADO "
	                   + "WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and JD.FECHA = DATE('" + FechaQuery + "') "
	                   + filtroLocal
	                   + filtroHoraDesp
	                   + "GROUP BY L.NOM_LOCAL, HD.HINI, COM.NOMBRE, Z.DESCRIPCION, P.ID_PEDIDO, P.CANT_BINS, TMP.POS_FECHA, TMP.POS_HORA, "
	                   + "         P.NOM_CLIENTE, C.CLI_EMAIL, P.DIR_TIPO_CALLE, P.DIR_CALLE, P.DIR_NUMERO, P.DIR_DEPTO, P.MEDIO_PAGO, "
	                   + "         P.NOM_TBANCARIA, C.CLI_FON_COD_1, C.CLI_FON_NUM_1, C.CLI_FON_COD_2, C.CLI_FON_NUM_2, C.CLI_FON_COD_3, "
	                   + "         C.CLI_FON_NUM_3, HD.HFIN, JD.FECHA, AP.ID_ALERTA, E.NOMBRE, P.ORIGEN, P.TIPO_DESPACHO "
	                   + "ORDER BY L.NOM_LOCAL, HD.HINI, COM.NOMBRE, Z.DESCRIPCION, P.ID_PEDIDO";
			
			logger.debug("Query (getListadoOP - Planilla Tracking): " + SQL);

			stm = conn.prepareStatement(SQL + " WITH UR ");
			rs = stm.executeQuery();
			
			lstPedidos = new HashMap();
			while (rs.next()){
			    PedidoDesp ped = new PedidoDesp();
			    if (lstPedidos.get(rs.getString("OP")) == null){
				    ped.setFecha(rs.getString("FECHA")); //FECHA DE DESPACHO
				    ped.setOrigen(rs.getString("ORIGEN"));
				    ped.setLocal(rs.getString("LOCAL"));
				    ped.setComuna(rs.getString("COMUNA"));
				    ped.setZona(rs.getString("ZONA"));
				    ped.setHorario(rs.getString("HORARIO")); //HORARIO DE DESPACHO
				    ped.setEstado(rs.getString("ESTADO"));
				    ped.setOp(rs.getString("OP"));
				    ped.setTipo_despacho(rs.getString("TIPO_DESPACHO"));
				    ped.setCant_bins(rs.getString("CANT_BINS"));
				    ped.setPos_monto(rs.getLong("POS_MONTO"));
				    ped.setPos_fecha(rs.getString("POS_FECHA"));
				    ped.setPos_hora(rs.getString("POS_HORA"));
				    ped.setPrimera_compra(rs.getString("PRIMERA_COMPRA"));
				    ped.setMedio_pago(rs.getString("MEDIO_PAGO"));
				    ped.setNom_cliente(rs.getString("NOM_CLIENTE"));
				    ped.setEMail(rs.getString("EMAIL"));
				    ped.setDireccion(rs.getString("DIRECCION"));
				    ped.setTelefonos(rs.getString("TELEFONOS"));
				    ped.setTipo_tarjeta(rs.getString("TIPO_TARJETA"));
				    lstPedidos.put(rs.getString("OP"), ped);
			    }else{
			        PedidoDesp tmp = (PedidoDesp)lstPedidos.get(rs.getString("OP"));
			        long monto = tmp.getPos_monto() + rs.getLong("POS_MONTO");
			        tmp.setPos_monto(monto);
			    }
			}
			/*1.- FECHA
			2.- ORIGEN
			3.- LOCAL
			4.- COMUNA
			5.- ZONA
			6.- HORARIO
			7.- ESTADO
			8.- OP
			9.- TIPO_DESPACHO
			10.- CANT_BINS
			11.- POS_MONTO
			12.- POS_FECHA
			13.- POS_HORA
			14.- PRIMERA_COMPRA
			15.- MEDIO_PAGO
			16.- NOM_CLIENTE
			17.- EMAIL
			18.- DIRECCION
			19.- TELEFONOS
			20.- TIPO_TARJETA*/


		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+lstPedidos.size());
		return lstPedidos;

	}
	
	
	/**
	 * Obtiene el ultimo log de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return LogPedidoDTO
	 * @throws PedidosDAOException 
	 */
	public LogPedidoDTO getUltLogPedido(long id_pedido) throws PedidosDAOException{
		LogPedidoDTO log1 = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			String cadQuery =" SELECT l.id_tracking id_tracking, " +
							"	l.usuario usuario, " +
							"	l.fecha fecha, " +
							"	l.descripcion descripcion, " +
							"	l.id_mot id_mot, " +
							"	m.nombre motivo, " +
							"	l.id_mot_ant id_mot_ant, " +
							"	mant.nombre motivo_ant  " +
							" FROM bo_tracking_od l" +
							" LEFT JOIN BO_MOTIVO m ON m.id_mot=l.id_mot " +
							" LEFT JOIN BO_MOTIVO mant ON mant.id_mot=l.id_mot_ant " +														
							" where id_tracking in (select max(id_tracking) from bo_tracking_od where id_pedido = ? )";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + stm.toString());

			rs = stm.executeQuery();
			if (rs.next()) {
				log1 = new LogPedidoDTO();
				log1.setId_log(rs.getLong("id_tracking"));
				log1.setUsuario(rs.getString("usuario"));
				log1.setLog(rs.getString("descripcion"));
				log1.setFecha(rs.getString("fecha"));
				log1.setId_motivo(rs.getLong("id_mot"));
				log1.setMotivo(rs.getString("motivo"));
				log1.setId_motivo_anterior(rs.getLong("id_mot_ant"));
				log1.setMotivo_anterior(rs.getString("motivo_ant"));
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return log1;

	}
	
	/**
	 * Obtiene Listado de estados de un pedido 
	 * 
	 * @return List EstadoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getEstadosPedidoBOC() throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm =null;
		ResultSet rs = null;
		
		try {

			String cadQuery="SELECT id_estado,nombre, tipo_estado " +
							"FROM bo_estados " +
							"WHERE ( tipo_estado= ? OR tipo_estado= ? OR tipo_estado= ? OR ID_ESTADO = ? ) AND id_estado != 1 " +
							"ORDER BY id_estado ASC";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setString(1,"PB");
			stm.setString(2,"PE");
			stm.setString(3,"DE");
			stm.setInt(4, Constantes.ESTADO_RUTA_ANULADA);
			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO log1 = new EstadoDTO();
				log1.setId_estado(rs.getLong("id_estado"));
				log1.setNombre(rs.getString("nombre"));
				log1.setTipo(rs.getString("tipo_estado"));
				result.add(log1);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	
	/**
	 * Obtiene Listado de motivos de un pedido 
	 * 
	 * @return List MotivoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getMotivosPedidoBOC() throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			String sql = 	"SELECT id_mot, nombre, descripcion, activo " +
							"FROM bo_motivo " +
							"WHERE activo='1' " +
							"ORDER BY id_mot ASC";
			

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			logger.debug("SQL query: " + sql);

			rs = stm.executeQuery();
			while (rs.next()) {
				MotivoDTO mot = new MotivoDTO();
				mot.setId_mot(rs.getLong("id_mot"));
				mot.setNombre(rs.getString("nombre"));
				mot.setDescrip(rs.getString("descripcion"));
				mot.setActivo(rs.getString("activo"));
				result.add(mot);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Obtiene Listado de estados de los pedidos
	 * 
	 * @return List EstadoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getEstadosPedido() throws PedidosDAOException {

		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		
		// Filtros de estado
		//String[] filtro_estados = Constantes.ESTADOS_PEDIDO_PICKING;
		String[] filtro_estados = Constantes.ESTADOS_PEDIDO_PICKING_CON_ANULADO;
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		
		String sql= "SELECT id_estado,nombre " +
					"FROM bo_estados " +
					"WHERE 1=1 ";
			
		if ( !estados_in.equals("") )
			sql += " AND id_estado IN (" + estados_in + ")";
		
		logger.debug("SQL query: " + sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO log1 = new EstadoDTO();
				log1.setId_estado(rs.getLong("id_estado"));
				log1.setNombre(rs.getString("nombre"));
				result.add(log1);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	/**
	 * Obtiene el estado de una OP
	 * 
	 * @return EstadoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public EstadoDTO getEstadoPedido(long IdPedido) throws PedidosDAOException {

	    EstadoDTO estado = new EstadoDTO();
		PreparedStatement stm=null;
		ResultSet rs=null;


		
		String sql= "SELECT E.ID_ESTADO AS ID_ESTADO, E.NOMBRE AS NOMBRE " 
                  + "FROM BODBA.BO_ESTADOS E, BODBA.BO_PEDIDOS P "
                  + "WHERE E.ID_ESTADO = P.ID_ESTADO AND P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") "
                  + "  AND P.ID_PEDIDO = " + IdPedido;
		logger.debug("SQL query: " + sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			rs = stm.executeQuery();
			rs.next();
			estado.setId_estado(rs.getLong("ID_ESTADO"));
			estado.setNombre(rs.getString("NOMBRE"));


		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		//logger.debug("Se listaron:"+result.size());
		return estado;
	}

	
	/**
	 * Obtiene el estado de una TrxMp
	 * 
	 * @return EstadoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public EstadoDTO getEstadoTrxMp(long IdTrxMp) throws PedidosDAOException {

	    EstadoDTO estado = new EstadoDTO();
		PreparedStatement stm=null;
		ResultSet rs=null;

		String sql= "SELECT E.ID_ESTADO AS ID_ESTADO, E.NOMBRE AS NOMBRE " 
                  + "FROM BODBA.BO_ESTADOS E, BODBA.BO_TRX_MP T "
                  + "WHERE E.ID_ESTADO = T.ID_ESTADO "
                  + "  AND T.ID_TRXMP = " + IdTrxMp;
		logger.debug("SQL query: " + sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			rs = stm.executeQuery();
			rs.next();
			estado.setId_estado(rs.getLong("ID_ESTADO"));
			estado.setNombre(rs.getString("NOMBRE"));

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		//logger.debug("Se listaron:"+result.size());
		return estado;
	}

	
	/**
	 * Obtiene datos del pedido
	 * 
	 * @param  id_pedido long 
	 * @return PedidoDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public PedidoDTO getPedidoById(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		PedidoDTO ped1 = new PedidoDTO();
		try {

			conn = this.getConnection();
			
			String sql = "SELECT P.ID_PEDIDO, P.ID_JPICKING, P.FCREACION fingreso, P.HCREACION hingreso, JP.FECHA fpicking, " +
                         "P.CANT_PRODUCTOS cant_prods, P.CANT_BINS, JD.FECHA fdespacho, HD.HINI hdespacho, HD.HFIN hfindespacho, E.NOMBRE estado, " +
                         "P.MONTO_PEDIDO monto,P.MEDIO_PAGO, P.CLAVE_MP clave, P.ID_ESTADO, P.ID_JDESPACHO, P.ID_MOT, " +
                         "P.ID_MOT_ANT, P.ID_LOCAL, P.ID_COMUNA, P.ID_ZONA, P.ID_USUARIO, " +
                         "CASE WHEN (P.ID_CLIENTE is null) THEN 0 ELSE P.ID_CLIENTE END as id_cliente, P.GENERO, " +
                         "P.FNAC, P.RUT_CLIENTE, P.DV_CLIENTE, P.NOM_CLIENTE, P.TELEFONO2 telef2, P.DIR_TIPO_CALLE, " +
                         "P.DIR_CALLE, P.DIR_NUMERO, P.DIR_DEPTO, P.DIR_ID, P.TELEFONO telef, " +
                         "P.COSTO_DESPACHO costo_desp, P.SIN_GENTE_OP, P.SIN_GENTE_TXT, P.POL_SUSTITUCION, " +
                         "P.INDICACION, P.NUM_MP, P.RUT_TIT, P.MESES_LIBRPAGO, P.TB_BANCO, P.NUM_DOC, P.TIPO_DOC, " +
                         "P.MEDIO_PAGO, P.NOM_TIT, P.APAT_TIT, P.AMAT_TIT, P.CANT_BINS, L.NOM_LOCAL, " +
                         "LF.NOM_LOCAL nom_local_fact, M.NOMBRE nom_motivo, COALESCE(U.NOMBRE,'') nom_usuario, " +
                         "COALESCE(U.APELLIDO,'') ape_usuario, P.id_usuario_fono, COALESCE(UFONO.NOMBRE,'') nom_usuario_fono, " +
                         "COALESCE(UFONO.APELLIDO,'') ape_usuario_fono, P.fecha_exp, P.dv_tit, P.n_cuotas, " +
                         "P.nom_tbancaria, P.pol_id, P.observacion, HP.HINI hpicking, HP.HFIN hfinpicking, " +
                         "CASE P.ID_COMUNA WHEN 0 THEN ' ' ELSE cm.nombre END as nom_comuna, P.ORIGEN, P.TIPO_VE, " +
                         "CASE P.ID_COMUNA WHEN 0 THEN ' ' ELSE CM.TIPO END as tipo_comuna, P.id_cotizacion, " +
                         "P.ID_LOCAL_FACT, P.TIPO_DESPACHO, P.FLAG_REC_ELIM, P.FLAG_REC_MP, P.SIN_GENTE_RUT, " +
                         "P.SIN_GENTE_DV, P.TIPO_PICKING, PE.ID_RUTA, PE.NRO_GUIA_CASO, " +
                         "PE.REPROGRAMADA, PE.FECHA_HORA_LLEGADA_DOM, PE.FECHA_HORA_SALIDA_DOM, PE.CUMPLIMIENTO, PE.MAIL, P.MONTO_RESERVADO, " +
                         "P.TBK_SECUENCIA_X, P.TBK_SECUENCIA_Y, P.SECUENCIA_PAGO, P.monto_excedido, P.DISPOSITIVO, P.ANULAR_BOLETA, " +
                         "F.ID_FACTURA as id_factura, F.RAZON as razon_factura, F.RUT as rut_factura, F.DV as dv_factura, F.DIRECCION as direccion_factura, " +
                         "F.FONO as fono_factura, F.GIRO as giro_factura, F.COMUNA as comuna_factura, F.CIUDAD as ciudad_factura " +                            
                         "FROM BO_PEDIDOS P " +
                         "LEFT JOIN BO_ESTADOS E ON E.ID_ESTADO = P.ID_ESTADO " +
                         "JOIN BO_JORNADA_DESP JD ON P.ID_JDESPACHO = JD.ID_JDESPACHO " +
                         "LEFT OUTER JOIN BO_JORNADAS_PICK JP ON P.ID_JPICKING = JP.ID_JPICKING " +
                         "JOIN BO_HORARIO_DESP HD ON HD.ID_HOR_DESP = JD.ID_HOR_DESP " +
                         "LEFT OUTER JOIN BO_HORARIO_PICK HP ON HP.ID_HOR_PICK = JP.ID_HOR_PICK " +
                         "LEFT OUTER JOIN BO_COMUNAS CM ON CM.ID_COMUNA = P.ID_COMUNA " +
                         "LEFT JOIN BO_LOCALES L ON L.ID_LOCAL = P.ID_LOCAL " +
                         "LEFT JOIN BO_LOCALES LF ON LF.ID_LOCAL = P.ID_LOCAL_FACT " +
                         "LEFT JOIN BO_MOTIVO M ON M.ID_MOT = P.ID_MOT " +
                         "LEFT JOIN BO_USUARIOS U ON U.ID_USUARIO = P.ID_USUARIO " +
                         "LEFT JOIN BO_USUARIOS UFONO ON UFONO.ID_USUARIO = P.ID_USUARIO_FONO " +
                         "LEFT OUTER JOIN bodba.BO_PEDIDOS_EXT PE ON PE.ID_PEDIDO = P.ID_PEDIDO " +
                         "LEFT JOIN BO_FACTURAS_PED F ON F.ID_PEDIDO = P.ID_PEDIDO " +
                         "WHERE P.ID_PEDIDO = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL query: " + sql);
			int i=0;
			rs = stm.executeQuery();
			if (rs.next()) {				
				ped1.setId_pedido(rs.getLong("id_pedido"));
				ped1.setId_jpicking(rs.getLong("id_jpicking"));
				ped1.setFingreso(rs.getString("fingreso"));
				ped1.setHingreso(rs.getString("hingreso"));
				ped1.setFpicking(rs.getString("fpicking"));
				ped1.setCant_prods(rs.getLong("cant_prods"));
                ped1.setCant_bins(rs.getInt("cant_bins"));
				ped1.setFdespacho(rs.getString("fdespacho"));
				ped1.setHdespacho(rs.getString("hdespacho"));
				ped1.setHfindespacho(rs.getString("hfindespacho"));
				ped1.setEstado(rs.getString("estado"));
				ped1.setMonto(rs.getLong("monto"));
				ped1.setMedio_pago(rs.getString("medio_pago"));	
				ped1.setClave_mp(rs.getString("clave"));
				//datos para monitor de clientes, desde BOC
				ped1.setId_estado(rs.getLong("id_estado"));
				ped1.setId_jdespacho(rs.getLong("id_jdespacho"));
				ped1.setId_mot(rs.getLong("id_mot"));
				ped1.setId_mot_ant(rs.getLong("id_mot_ant"));
				ped1.setId_local(rs.getLong("id_local"));
				ped1.setId_comuna(rs.getLong("id_comuna"));
				ped1.setId_zona(rs.getLong("id_zona"));
				ped1.setId_usuario(rs.getLong("id_usuario"));
				ped1.setId_cliente(rs.getLong("id_cliente"));
				ped1.setGenero(rs.getString("genero"));
				ped1.setFnac(rs.getDate("fnac"));
				ped1.setRut_cliente(rs.getLong("rut_cliente"));
				ped1.setDv_cliente(rs.getString("dv_cliente"));
				ped1.setNom_cliente(rs.getString("nom_cliente"));
				ped1.setTelefono2(rs.getString("telef2"));
				if(rs.getString("dir_tipo_calle")!=null)
					ped1.setDir_tipo_calle(rs.getString("dir_tipo_calle"));
				else
					ped1.setDir_tipo_calle("");
				if(rs.getString("dir_calle")!=null)
					ped1.setDir_calle(rs.getString("dir_calle"));
				else
					ped1.setDir_calle("");
				if(rs.getString("dir_numero")!=null)
					ped1.setDir_numero(rs.getString("dir_numero"));
				else
					ped1.setDir_numero("");
				if(rs.getString("dir_depto")!=null)
					ped1.setDir_depto(rs.getString("dir_depto"));
				else
					ped1.setDir_depto("");
				ped1.setDir_id(rs.getInt("dir_id"));
				ped1.setTelefono(rs.getString("telef"));
				ped1.setCosto_despacho(rs.getDouble("costo_desp"));
				ped1.setSin_gente_op(rs.getInt("sin_gente_op"));
				
                if ( rs.getString("sin_gente_txt") != null ) {
                    ped1.setSin_gente_txt(rs.getString("sin_gente_txt"));
                } else {
                    ped1.setSin_gente_txt("");
                }
				
                if ( rs.getString("pol_sustitucion") != null ) {
				    ped1.setPol_sustitucion(rs.getString("pol_sustitucion"));
                } else {
                    ped1.setPol_sustitucion("");
                }
				ped1.setPol_id(rs.getLong("pol_id"));
				ped1.setIndicacion(rs.getString("indicacion"));
				ped1.setNum_mp(rs.getString("num_mp"));
				//ped1.setTitular(rs.getString("titular"));//flag , indica si es titular "T" o adicional "A", del medio de pago 
				ped1.setRut_tit(rs.getString("rut_tit"));
				ped1.setNom_tit(rs.getString("nom_tit"));				
				ped1.setApat_tit(rs.getString("apat_tit"));				
				ped1.setAmat_tit(rs.getString("amat_tit"));				
				ped1.setMeses_librpago(rs.getInt("meses_librpago"));
				if ( rs.getString("tb_banco") != null ) {
				    ped1.setTb_banco(rs.getString("tb_banco"));
                } else {
                    ped1.setTb_banco("");
                }
				ped1.setMedio_pago(rs.getString("medio_pago"));
				ped1.setNum_doc(rs.getInt("num_doc"));		
				ped1.setTipo_doc(rs.getString("tipo_doc"));
				ped1.setCant_bins(rs.getInt("cant_bins"));
				ped1.setNom_local(rs.getString("nom_local"));
				ped1.setNom_local_fact(rs.getString("nom_local_fact"));				
				if(ped1.getId_mot()>0)
					ped1.setNom_motivo(rs.getString("nom_motivo"));
				else
					ped1.setNom_motivo("");
				ped1.setId_usuario_fono(rs.getLong("id_usuario_fono"));
				ped1.setNom_usuario_bo(rs.getString("nom_usuario")+" "+rs.getString("ape_usuario"));
				ped1.setNom_ejecutivo(rs.getString("nom_usuario_fono")+" "+rs.getString("ape_usuario_fono"));
				if ( rs.getString("fecha_exp") != null ) {
				    ped1.setFecha_exp(rs.getString("fecha_exp"));
                } else {
                    ped1.setFecha_exp("");
                }
				ped1.setDv_tit(rs.getString("dv_tit"));
				ped1.setN_cuotas(rs.getInt("n_cuotas"));
                if ( rs.getString("nom_tbancaria") != null ) {
                    ped1.setNom_tbancaria(rs.getString("nom_tbancaria"));
                } else {
                    ped1.setNom_tbancaria("");
                }

				ped1.setHpicking(rs.getString("hpicking"));
				ped1.setHfinpicking(rs.getString("hfinpicking"));
				ped1.setNom_comuna(rs.getString("nom_comuna"));
                if ( rs.getString("DISPOSITIVO") != null ) {
                    ped1.setDispositivo(rs.getString("DISPOSITIVO"));
                } else {
                    ped1.setDispositivo("");
                }				
				if(rs.getString("observacion")!=null)
					ped1.setObservacion(rs.getString("observacion"));
				else
					ped1.setObservacion("");
				ped1.setOrigen(rs.getString("origen"));
				ped1.setTipo_ve(rs.getString("tipo_ve"));
				ped1.setTipo_comuna(rs.getString("tipo_comuna"));
				ped1.setId_cotizacion(rs.getLong("id_cotizacion"));
				ped1.setId_local_fact(rs.getLong("id_local_fact"));	
				
				if (rs.getString("tipo_despacho") == null) {
				    ped1.setTipo_despacho(Constantes.TIPO_DESPACHO_NORMAL_CTE);
				} else {
				    ped1.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				//flags de recalculo de promociones 1=true else false
				ped1.setFlg_recalc_prod(rs.getInt("flag_rec_elim")==1);
				ped1.setFlg_recalc_mp(rs.getInt("flag_rec_mp")==1);
                if ( rs.getString("SIN_GENTE_RUT") == null ) {
                    ped1.setSin_gente_rut(0);
                } else {
                    ped1.setSin_gente_rut(rs.getLong("SIN_GENTE_RUT"));    
                }
                if ( rs.getString("SIN_GENTE_DV") == null ) {
                    ped1.setSin_gente_dv("");
                } else {
                    ped1.setSin_gente_dv(rs.getString("SIN_GENTE_DV"));
                }
                ped1.setTipo_picking(rs.getString("TIPO_PICKING"));
                
                ped1.setTbk_secuencia_x(rs.getInt("TBK_SECUENCIA_X"));
                ped1.setTbk_secuencia_y(rs.getInt("TBK_SECUENCIA_Y"));
                
                ped1.setSecuenciaPago(rs.getString("SECUENCIA_PAGO"));
                
                if ( rs.getLong("monto_excedido") == 0 ) {
                    ped1.setMontoExcedido(false);
                } else {
                    ped1.setMontoExcedido(true);
                }
                if ( rs.getLong("ANULAR_BOLETA") == 0 ) {
                    ped1.setAnularBoleta(false);
                } else {
                    ped1.setAnularBoleta(true);
                }
                                
                if("F".equals(rs.getString("tipo_doc")) && rs.getString("id_factura") != null){                
                	FacturaDTO oFactura = new FacturaDTO();      
                	oFactura.setId_factura(rs.getLong("id_factura"));
                	oFactura.setRazon(rs.getString("razon_factura"));
                	oFactura.setRut(rs.getInt("rut_factura"));
                	oFactura.setDv(rs.getString("dv_factura"));
                	oFactura.setDireccion(rs.getString("direccion_factura"));
                	oFactura.setFono(rs.getString("fono_factura"));
                	oFactura.setGiro(rs.getString("giro_factura"));
                	oFactura.setComuna(rs.getString("comuna_factura"));
                	oFactura.setCiudad(rs.getString("ciudad_factura"));
                	ped1.setFactura(oFactura);
                }else{
                	ped1.setFactura(null);
                }
                
                PedidoExtDTO pe = new PedidoExtDTO();
                pe.setIdRuta(rs.getLong("ID_RUTA"));
                pe.setNroGuiaCaso(rs.getLong("NRO_GUIA_CASO"));
                pe.setReprogramada(rs.getInt("REPROGRAMADA"));
                ped1.setMonto_reservado(rs.getDouble("MONTO_RESERVADO"));
                
                if ( rs.getString("FECHA_HORA_LLEGADA_DOM") != null ) 
                    pe.setFcHoraLlegadaDomicilio(rs.getString("FECHA_HORA_LLEGADA_DOM"));
                else
                    pe.setFcHoraLlegadaDomicilio("");
                
                if ( rs.getString("FECHA_HORA_SALIDA_DOM") != null ) 
                    pe.setFcHoraSalidaDomicilio(rs.getString("FECHA_HORA_SALIDA_DOM"));
                else
                    pe.setFcHoraSalidaDomicilio("");
                
                if ( rs.getString("CUMPLIMIENTO") != null ) 
                    pe.setCumplimiento(rs.getString("CUMPLIMIENTO"));
                else
                    pe.setCumplimiento("");
                
                if ( rs.getString("MAIL") != null ) {
                    pe.setMail(rs.getString("MAIL"));
                } else {
                    pe.setMail("");
                }
                
                ped1.setPedidoExt(pe);
				i++;
			}

			if(i==0)
				throw new PedidosDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return ped1;		
	}

	public boolean tieneBolsaRegalo(String id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cantidad = 0;

	
		String sql= 
		" Select sum(sq.tot) as cantidad "
		+" From "
		+" ( "
		+" Select cod_prod1, count(cod_prod1) tot "
		+" FROM BODBA.BO_DETALLE_PEDIDO "
		+" Where ID_PEDIDO in ("+id_pedido+") "
		+" and cod_prod1 in "
		+" ( "
		+" Select f.pro_cod_sap "
		+" From bodba.fo_productos f, bodba.bo_bolsas b " 
		+" Where f.pro_cod_sap in ( "
		+" Select cod_prod1 FROM BODBA.BO_DETALLE_PEDIDO Where ID_PEDIDO in ("+id_pedido+") "
		+" ) "
		+" and f.pro_id = b.id_producto "
		+" Group by f.pro_cod_sap "
		+" ) "
		+" Group by cod_prod1 "
		+" )sq ";
		
		
		System.out.println("SQL query tieneBolsaRegalo : " + sql + " Pedidop : "+id_pedido);

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			rs = stm.executeQuery();
			if(rs.next()){
			   cantidad = rs.getInt("cantidad");
			}
			
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return cantidad <= 1;
	}
	
	/**
	 * Retorna el número de registros de una consulta por criterio
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return long
	 * @throws PedidosDAOException 
	 */
	public long getCountPedidosByCriteria(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		PreparedStatement stm =null;
		ResultSet rs =null ;
		
		int numPed = 0;
		GregorianCalendar hoy = new GregorianCalendar();
		String sqlLimitaFecha = "1=1 ";
		
		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();
		String fecha_despacho  = criterio.getFdespacho();
		String fecha_picking = criterio.getFpicking();
		String sql_tipo_picking = "";
		long id_estado = criterio.getId_estado();
		long id_jpicking = criterio.getId_jpicking();

		long id_local  = -1L;
		if (criterio.getId_local()>0){
			id_local = criterio.getId_local();	
		}
		
		long id_zona  = -1L;
		if (criterio.getId_zona()>0){
			id_zona = criterio.getId_zona();	
		}
		
		long id_local_fact =-1L;
		if (criterio.getId_local_fact()>0){
			id_local_fact = criterio.getId_local_fact();	
		} 
		
		long id_cliente = -1L;		
		if (criterio.getId_cliente()>0){
			id_cliente = criterio.getId_cliente();
		}
		
		//busq por rut , apellido o email del cliente
		long rut = -1L;
		if (criterio.getCli_rut()!=null && !criterio.getCli_rut().equals(""))
			rut = Long.parseLong(criterio.getCli_rut());
		String apellido = "";
		if (criterio.getCli_ape()!=null && !criterio.getCli_ape().equals(""))
			apellido = criterio.getCli_ape(); 
		String email ="";
		if(criterio.getCli_email() != null && !criterio.getCli_email().equals(""))
			email =criterio.getCli_email();
		//bus por motivo
		long id_motivo = -1;
		if (criterio.getId_motivo()>0)
			id_motivo = criterio.getId_motivo(); 

		//Motivos sin gestion
		String sql_mot_sin_gestion = "";
		if (criterio.isSin_gestion()){
			sql_mot_sin_gestion = " AND P.id_mot is null ";
		}
	
		//Jdespacho AM o PM
		String sql_jdesp_dia = "";
		String jdesp_dia = criterio.getJdesp_dia();
		if(jdesp_dia!=null){
			if(jdesp_dia.equals(Constantes.PARAMETRO_DIA)){
				sql_jdesp_dia = " AND HD.HINI < '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}else if(jdesp_dia.equals(Constantes.PARAMETRO_NOCHE)){
				sql_jdesp_dia = " AND HD.HINI >= '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}
		}
		
		//busqueda por rango de fechas
		String tip_fec = criterio.getTip_fec();
		String fec_ini = criterio.getFec_ini();
		String fec_fin = criterio.getFec_fin();

		// Filtros de estado
		String[] filtro_estados = criterio.getFiltro_estados();
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		//		Origen y Tipo
		String sql_origen_tipo_ve = "";
		String origen = criterio.getOrigen();
		String tipo_ve = criterio.getTipo_ve();
		if(origen!=null){
			if(origen.equals(Constantes.ORIGEN_WEB_CTE)){
				sql_origen_tipo_ve = " AND P.ORIGEN = '"+Constantes.ORIGEN_WEB_CTE+"' ";
			}else if(origen.equals(Constantes.ORIGEN_VE_CTE)){				
				sql_origen_tipo_ve = " AND P.ORIGEN = '"+Constantes.ORIGEN_VE_CTE+"' ";
				if (tipo_ve!=null){
					if(tipo_ve.equals(Constantes.TIPO_VE_NORMAL_CTE)){
						sql_origen_tipo_ve += " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";							
					} else if(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE)){
						sql_origen_tipo_ve += " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
					}
				}				
			}
		}
		
		if(criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_LIGHT_CTE)){
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_LIGHT_CTE + "' ";
		}else if (criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_NORMAL_CTE)){
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_NORMAL_CTE + "' ";
		}

		String tipo_desp = "";
		String sql_tipo_desp = "";
		
		if(criterio.getTipo_desp()!=null){
			tipo_desp = criterio.getTipo_desp();
		}
		if(tipo_desp.equals(Constantes.TIPO_DESPACHO_NORMAL_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_NORMAL_CTE+"' ";							
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_EXPRESS_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_EXPRESS_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_ECONOMICO_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_RETIRO_CTE)){
            sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_RETIRO_CTE+"' ";
        }

		if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
			hoy.add(Calendar.MONTH, -6);
			sqlLimitaFecha = " P.FCREACION >= ? ";
		}

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getCountPedidosByCriteria");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getCountPedidosByCriteria:");
		logger.debug("numero_pedido  : " + id_pedido);
		logger.debug("id estado      : " + id_estado);
		logger.debug("fecha_despacho : " + fecha_despacho);
		logger.debug("fecha_picking  : " + fecha_picking);	
		logger.debug("rut       : " + rut);
		logger.debug("apellido  : " + apellido);
		logger.debug("tip_fec   : " + tip_fec);
		logger.debug("fec_ini   : " + fec_ini);
		logger.debug("fec_fin   : " + fec_fin);
		logger.debug("jdesp_dia : " + jdesp_dia);
		logger.debug("id_local_fact: " + id_local_fact);
		logger.debug("origen    : " + origen);
		logger.debug("tipo_ve   : " + tipo_ve);
		logger.debug("tipo_desp : " + tipo_desp);
		logger.debug("id_zona   : " + id_zona);
		
//		genera sql		
		String Sqlpedido     = "";
		String Sqlestado     = "";
		String Sqlestados_in = "";
		String Sqlfdesp      = "";
		String Sqlfpick      = "";
		String Sqlcliente    = "";
		String Sqlocal       = "";
		String Sqlocal_fact  = "";
		String SqlZona       = "";
		String sqlRut        = "";
		String sqlApe        = "";
		String sqlMot        = "";
		String sqlFdesp      = "";
		String sqlFpick      = "";
		String sqlEmail      = ""; 
		

		String Sql =
			 " SELECT count(*) cantidad  " +
			 " FROM BO_PEDIDOS P " +
			 " JOIN BO_LOCALES L ON P.id_local = L.id_local " +
			 " JOIN BO_JORNADA_DESP JD ON P.ID_JDESPACHO = JD.ID_JDESPACHO "+
			 " left outer JOIN BO_JORNADAS_PICK JP ON P.ID_JPICKING = JP.ID_JPICKING "+
			 " JOIN BO_HORARIO_DESP HD ON HD.ID_HOR_DESP = JD.ID_HOR_DESP " + sql_jdesp_dia +
			 " JOIN BO_ESTADOS E ON E.ID_ESTADO = P.ID_ESTADO" +
			 " JOIN BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA " +
             " left outer join bodba.bo_pedidos_ext pe on p.id_pedido = pe.id_pedido " +
			 " left join FODBA.FO_CLIENTES FOC ON FOC.CLI_ID = P.ID_CLIENTE " +
			 " WHERE  " + sqlLimitaFecha + " and p.origen != 'C' and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + sql_origen_tipo_ve + sql_tipo_picking + sql_tipo_desp;
		
		if (id_cliente>0){ Sqlcliente = " AND P.id_cliente = "+id_cliente; }
		if (id_local>0){ Sqlocal = " AND P.id_local = "+id_local; }	
		if (id_local_fact>0){ Sqlocal_fact = " AND P.id_local_fact = "+id_local_fact; }
		if (id_zona>0){ SqlZona = " AND Z.id_zona = "+id_zona; }
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	
			if(id_estado==34) {Sqlestado = " AND P.id_estado IN ("+Constantes.ID_ESTAD_PEDIDO_INGRESADO+","+Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION+")";} 
			else {Sqlestado = " AND P.id_estado = "+id_estado;}	}
		if ( !estados_in.equals("") ){ Sqlestados_in = " AND P.id_estado IN (" + estados_in + ") " ; }
		if ((fecha_despacho!=null) && (fecha_despacho!=""))
			{ Sqlfdesp  = " AND JD.fecha = '"+fecha_despacho+"'"; }
		if ((fecha_picking!=null) && (fecha_picking!=""))
			{ Sqlfpick  = " AND JP.fecha = '"+fecha_picking+"'";	}
		
		//busqueda por rango de fechas
		if (tip_fec.equals("despacho") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFdesp = " AND (JD.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} else if (tip_fec.equals("picking") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFpick = " AND (JP.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} 
		
		if (id_jpicking >0) { Sqlpedido  += " AND JP.id_jpicking = "+id_jpicking;	}
		if (rut>0) { sqlRut = " AND rut_cliente = "+rut; }
		if (!apellido.equals("")) { sqlApe = " AND replace(replace(replace(replace(replace(UPPER(nom_cliente),'Á','A') ,'É','E') ,'Í','I'),'Ó','O') ,'Ú','U') like '%"+apellido.toUpperCase()+"%'";}
		if(!email.equals("")){sqlEmail ="AND UPPER(FOC.CLI_EMAIL)=UPPER('" +email+"')";}
		if (id_motivo>0) { sqlMot = " AND P.id_mot = "+id_motivo;}
		//genero las condiciones
		Sql = Sql + Sqlfpick + Sqlfdesp + sqlFdesp + sqlFpick + Sqlestado + Sqlestados_in + Sqlpedido + SqlZona + Sqlocal + Sqlocal_fact + Sqlcliente + sqlRut + sqlApe + sqlMot + sql_mot_sin_gestion+sqlEmail;
		logger.debug("SQL (getCountPedidosByCriteria): " + Sql);

		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR") ;
			if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
				stm.setDate(1,new java.sql.Date(hoy.getTimeInMillis()));
			}

			rs = stm.executeQuery();
			if (rs.next()) {
				numPed = rs.getInt("cantidad");
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return numPed;
	}

	
	
	/**
	 * Lista pedidos por criterio
	 * El cirterio esta dado por :PedidosCriteriaDTO
	 * nº pedido o nº de bin
	 * y fecha de despacho o fecha de picking
	 * y/o estado
	 * y paginacion
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getPedidosByCriteria(cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO)
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidosDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getPedidosByCriteria(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		List lista_pedidos = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		GregorianCalendar hoy = new GregorianCalendar();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidosByCriteria");
		logger.debug("-----------------------------------------------------------------");

		
		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();
		String fecha_despacho  = criterio.getFdespacho();
		String fecha_picking = criterio.getFpicking();
		long id_estado = criterio.getId_estado();
		long id_jpicking = criterio.getId_jpicking();
		
		long id_local  = -1L;
		if (criterio.getId_local()>0){
			id_local = criterio.getId_local();	
		}
		
		long id_zona  = -1L;
		if (criterio.getId_zona()>0){
			id_zona = criterio.getId_zona();	
		}		
		
		long id_local_fact =-1L;
		if (criterio.getId_local_fact()>0){
			id_local_fact = criterio.getId_local_fact();	
		}
		 
		long id_cliente = -1L;		
		if (criterio.getId_cliente()>0){
			id_cliente = criterio.getId_cliente();
		}
		
		//busq por rut  apellido del cliente o mail
		long rut = -1L;
		if (criterio.getCli_rut()!=null && !criterio.getCli_rut().equals(""))
			rut = Long.parseLong(criterio.getCli_rut());
		String apellido = "";
		if (criterio.getCli_ape()!=null && !criterio.getCli_ape().equals(""))
			apellido = criterio.getCli_ape(); 
		String email = "";
		if(criterio.getCli_email()!=null && !criterio.getCli_email().equals(""))
			email = criterio.getCli_email();
		//bus por motivo
		long id_motivo = -1;
		if (criterio.getId_motivo()>0)
			id_motivo = criterio.getId_motivo();
		
		//Motivos sin gestion
		String sql_mot_sin_gestion = "";
		if (criterio.isSin_gestion()){
			sql_mot_sin_gestion = " AND P.id_mot is null ";
		}
		
		//Jdespacho AM o PM
		String sql_jdesp_dia = "";
		String jdesp_dia = criterio.getJdesp_dia();
		if(jdesp_dia != null && !jdesp_dia.equals("-1") && !jdesp_dia.equals("T")){
		    sql_jdesp_dia = " AND HD.HINI = TIME('"+ jdesp_dia +"') ";
			/*if(jdesp_dia.equals(Constantes.PARAMETRO_DIA)){
				sql_jdesp_dia = " AND HD.HINI < '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}else if(jdesp_dia.equals(Constantes.PARAMETRO_NOCHE)){
				sql_jdesp_dia = " AND HD.HINI >= '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}*/
		}
			
		//busqueda por rango de fechas
		String tip_fec = criterio.getTip_fec();
		String fec_ini = criterio.getFec_ini();
		String fec_fin = criterio.getFec_fin();
		
		
		// Filtros de estado
		
		//AJARA: FILTRO para Monitor de Pedido 
		
		//String[] filtro_estados = criterio.getFiltro_estados();
		String[] filtro_estados= null;
		
		if(criterio.getCheckEstado69().equals("S"))
			filtro_estados = Constantes.ESTADOS_PEDIDO_PICKING_CON_ANULADO;
		else
			filtro_estados = Constantes.ESTADOS_PEDIDO_PICKING_CON_ANULADOS;
		
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		//		Origen y Tipo
		String sql_origen = "";
		String sql_tipo_picking = "";
		String sql_tipo_ve = "";
		String sql_tipo_desp = "";
		String sqlLimitaFecha = "1=1 ";
		//String sqlPedido = "";
		//String sql_LimiteFecha = "1=1 ";
		String origen = "";
		String tipo_ve="";
		String tipo_desp="";
		

		if (criterio.getOrigen()!=null){
			origen = criterio.getOrigen();
		}
				
		if (origen.equals(Constantes.ORIGEN_WEB_CTE)) {
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_WEB_CTE+"' ";
		} else if(origen.equals(Constantes.ORIGEN_VE_CTE)) {
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_VE_CTE+"' ";							
		} else if(origen.equals(Constantes.ORIGEN_JV_CTE)) {
            sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_JV_CTE+"' ";                         
        }
		
		if (criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_LIGHT_CTE)) {
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_LIGHT_CTE + "' ";
		} else if (criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_NORMAL_CTE)) {
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_NORMAL_CTE + "' ";
		}
		
		if(criterio.getTipo_ve()!=null){
			tipo_ve = criterio.getTipo_ve();
		}
		if(tipo_ve.equals(Constantes.TIPO_VE_NORMAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";							
		} else if(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}
		
		if(criterio.getTipo_desp()!=null){
			tipo_desp = criterio.getTipo_desp();
		}
		if(tipo_desp.equals(Constantes.TIPO_DESPACHO_NORMAL_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_NORMAL_CTE+"' ";							
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_EXPRESS_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_EXPRESS_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_ECONOMICO_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_RETIRO_CTE)){
            sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_RETIRO_CTE+"' ";
        }
		
		if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
			hoy.add(Calendar.MONTH, -6);
			sqlLimitaFecha = " P.FCREACION >= ? ";
		}

		//paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;		
		
		logger.debug("Parametros getPedidosByCriteria:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("id estado :"+id_estado);
		logger.debug("id jornada picking  :"+id_jpicking);
		
		logger.debug("fecha_despacho :" +fecha_despacho);
		logger.debug("fecha_picking :"+fecha_picking);		
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		logger.debug("id_cliente:"+id_cliente);
		logger.debug("id_local:"+id_local);
		logger.debug("rut:"+rut);
		logger.debug("apellido:"+apellido);
		logger.debug("id_motivo:"+id_motivo);
		logger.debug("tip_fec :"+tip_fec);
		logger.debug("fec_ini :"+fec_ini);
		logger.debug("fec_fin :"+fec_fin);
		logger.debug("jdesp_dia :"+jdesp_dia);
		logger.debug("id_local_fact:"+id_local_fact);
		logger.debug("origen:"+origen);
		logger.debug("tipo_ve:"+tipo_ve);
		logger.debug("tipo_desp:"+tipo_desp);
		logger.debug("id_zona:"+id_zona);
		
		
		//		orden de columnas		
		String SqlOrder="";
		if (criterio.getOrden_columnas()!=null){
			String obj;			
			int x=1;
			for (int i=0; i<criterio.getOrden_columnas().size(); i++){
				if (x==1){
					SqlOrder += " ORDER BY ";
				}
				obj = (String) criterio.getOrden_columnas().get(i);
				logger.debug(  "Columna : "+obj );
				
				if (x>1) SqlOrder += " ,";
				SqlOrder += " "+obj+" ";
				x++;
			}
		}
		
		//genera sql		
		String Sqlpedido ="";
		String Sqlestado ="";
		String Sqlestados_in ="";
		String Sqlfdesp  ="";
		String Sqlfpick  ="";
		String Sqlcliente = "";
		String Sqlocal = "";
		String Sqlocal_fact = "";
		String SqlZona = "";
		String sqlRut = "";
		String sqlApe = "";
		String sqlMot = "";
		String sqlFdesp  ="";
		String sqlFpick  ="";
		String sqlEmail ="";
		
		
		/*GregorianCalendar hoy = new GregorianCalendar();
		hoy.add(Calendar.MONTH, -6);*/

		
		String Sql = "SELECT * FROM ( " +
		 " SELECT row_number() over("+SqlOrder+") as row, "+ 
				 " P.ID_PEDIDO, " +
				 " P.ID_JPICKING, " +
				 " P.FCREACION fcompra, " +
				 " JP.FECHA fpicking, "+
				 " JD.FECHA fdespacho, " +
				 " HD.HINI hdespacho, " +
				 " HD.HFIN hdespachof, " +
				 " E.NOMBRE estado, " +
				 " E.ID_ESTADO id_estado,"+
				 " P.MONTO_PEDIDO monto," +
				 " P.MEDIO_PAGO, " +
				 " P.TIPO_DESPACHO, " +
				 " L.nom_local local, " +
				 " LF.nom_local local_fact, " +
				 " P.nom_cliente, " +
				 " P.rut_cliente, " +
				 " P.dv_cliente, " +
				 " COALESCE(P.id_usuario,0) id_usuario, "+
				 " HP.HINI as hini_jpicking, " +
				 " HP.HFIN as hfin_jpicking, " +
				 " P.origen," +
				 " P.tipo_ve, " +
				 " Z.id_zona, " +
				 " Z.nombre as zona_nombre," +
				 " Z.orden_zona as zona_orden," +
                 " pe.CUMPLIMIENTO," +
                 " P.MONTO_EXCEDIDO, " +
                 " P.ANULAR_BOLETA, " +
				 " COUNT(DISTINCT AP.ID_ALERTA) as alerta " +
		 " FROM BODBA.BO_PEDIDOS P " +
		 "      JOIN BODBA.BO_LOCALES         L ON P.id_local      = L.id_local " +
		 "      JOIN BODBA.BO_LOCALES        LF ON P.id_local_fact = LF.id_local " +
		 "      JOIN BODBA.BO_JORNADA_DESP   JD ON P.ID_JDESPACHO  = JD.ID_JDESPACHO " +
		 "      left outer JOIN BODBA.BO_JORNADAS_PICK  JP ON P.ID_JPICKING   = JP.ID_JPICKING " +
		 "      left outer JOIN BODBA.BO_HORARIO_PICK   HP ON HP.ID_HOR_PICK  = JP.ID_HOR_PICK " +
		 "      JOIN BODBA.BO_HORARIO_DESP   HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " + sql_jdesp_dia +
		 "      JOIN BODBA.BO_ESTADOS         E ON E.ID_ESTADO     = P.ID_ESTADO " +
		 "      JOIN BODBA.BO_ZONAS           Z ON Z.ID_ZONA       = P.ID_ZONA " +
		 "      left JOIN bodba.BO_ALERTA_OP AP ON AP.ID_PEDIDO    = P.ID_PEDIDO and AP.ID_ALERTA BETWEEN 16 AND 24 " +
         "      left outer join bodba.bo_pedidos_ext pe on p.id_pedido = pe.id_pedido " +
		 "      left join FODBA.FO_CLIENTES FOC ON FOC.CLI_ID = P.ID_CLIENTE  " +
//      " WHERE  " + sqlLimitaFecha + " and p.origen != 'C' and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + sql_origen  + sql_tipo_picking + sql_tipo_ve + sql_tipo_desp;
		" WHERE  " + sqlLimitaFecha + " and p.origen != 'C' and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS_SIN_ANULADO + ") " + sql_origen  + sql_tipo_picking + sql_tipo_ve + sql_tipo_desp;
		if (id_cliente>0){ Sqlcliente = " AND P.id_cliente = "+id_cliente; }
		if (id_local>0){ Sqlocal = " AND P.id_local = "+id_local; }
		if (id_local_fact>0){ Sqlocal_fact = " AND P.id_local_fact = "+id_local_fact; }
		if (id_zona>0){ SqlZona = " AND Z.id_zona = "+id_zona; }
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	
			if(id_estado==34) {Sqlestado = " AND P.id_estado IN ("+Constantes.ID_ESTAD_PEDIDO_INGRESADO+","+Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION+")";} 
			else {
					/*if(id_estado==20){ //se agrega anulada id 69
						Sqlestado = " AND P.id_estado in (20,69) ";
					}else{
						Sqlestado = " AND P.id_estado = "+id_estado;
					}*/
				Sqlestado = " AND P.id_estado = "+id_estado;
				
				 }	
			}
		if ( !estados_in.equals("") ){ Sqlestados_in = " AND P.id_estado IN (" + estados_in + ") " ; }//
		if ((fecha_despacho!=null) && (fecha_despacho!=""))
			{ Sqlfdesp  = " AND JD.fecha = '"+fecha_despacho+"'"; }
		if ((fecha_picking!=null) && (fecha_picking!=""))
			{ Sqlfpick  = " AND JP.fecha = '"+fecha_picking+"'";	}
		if (id_jpicking >0) { Sqlpedido  += " AND JP.id_jpicking = "+id_jpicking;	}
		//busqueda por rango de fechas
		if (tip_fec.equals("despacho") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFdesp = " AND (JD.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} else if (tip_fec.equals("picking") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFpick = " AND (JP.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} 
		
		if (rut>0) { sqlRut = " AND rut_cliente = "+rut; }
		if (!apellido.equals("")) { sqlApe = " AND replace(replace(replace(replace(replace(UPPER(nom_cliente),'Á','A') ,'É','E') ,'Í','I'),'Ó','O') ,'Ú','U') like '%"+apellido.toUpperCase()+"%'";}
		if(!email.equals("")){ sqlEmail ="AND UPPER(FOC.CLI_EMAIL)=UPPER('" +email+"')";}
		if (id_motivo>0) { sqlMot = " AND P.id_mot = "+id_motivo;}
		//genero las condiciones
		Sql = Sql + Sqlfpick + Sqlfdesp + sqlFdesp + sqlFpick + Sqlestado + Sqlestados_in + Sqlpedido + SqlZona + Sqlocal + Sqlocal_fact + Sqlcliente + sqlRut + sqlApe + sqlMot + sql_mot_sin_gestion+sqlEmail;
		//paginador
		Sql = Sql + " GROUP BY P.ID_PEDIDO, P.ID_JPICKING, P.FCREACION, JP.FECHA, JD.FECHA, HD.HINI, HD.HFIN, "
                  + "          E.NOMBRE, E.ID_ESTADO, P.MONTO_PEDIDO, P.MEDIO_PAGO, P.TIPO_DESPACHO, L.NOM_LOCAL, "
                  + "          LF.NOM_LOCAL, P.NOM_CLIENTE, P.RUT_CLIENTE, P.DV_CLIENTE, P.ID_USUARIO, "
                  + "          HP.HINI, HP.HFIN, P.ORIGEN, P.TIPO_VE, Z.ID_ZONA, Z.NOMBRE, Z.ORDEN_ZONA, pe.CUMPLIMIENTO, P.MONTO_EXCEDIDO, P.ANULAR_BOLETA, "
                  + "          P.CANT_PRODUCTOS "
                  + ") AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;

		logger.debug("SQL con criterio :"+Sql);
		
		try{

			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
			    stm.setDate(1, new java.sql.Date(hoy.getTimeInMillis()));
			}
			
			rs = stm.executeQuery();
			
			List idsPedidos = new ArrayList();
			Hashtable hash = new Hashtable();
			while (rs.next()) {
				MonitorPedidosDTO lin1 = new MonitorPedidosDTO();
				idsPedidos.add(new Long(rs.getLong("id_pedido")));
				lin1.setId_pedido(rs.getLong("id_pedido"));
				lin1.setId_jpicking(rs.getLong("id_jpicking"));
				lin1.setFpicking(rs.getString("fpicking"));
				lin1.setFdespacho(rs.getString("fdespacho"));
				lin1.setHdespacho(rs.getString("hdespacho"));
				lin1.setHdespacho_fin(rs.getString("hdespachof"));
				lin1.setEstado(rs.getString("estado"));
				lin1.setId_estado(rs.getLong("id_estado"));
				//lin1.setCant_prods(rs.getDouble("cant_prods"));
				//lin1.setCant_spick(rs.getDouble("CANT_SPICK"));
				lin1.setHini_jpicking(rs.getString("hini_jpicking"));
				lin1.setHfin_jpicking(rs.getString("hfin_jpicking"));
				//agregados para el boc
				lin1.setLocal_despacho(rs.getString("local"));
				lin1.setLocal_facturacion(rs.getString("local_fact"));
				lin1.setMonto(rs.getLong("monto"));
				lin1.setFingreso(rs.getString("fcompra"));
				lin1.setRut_cliente(rs.getLong("rut_cliente"));
				lin1.setDv_cliente(rs.getString("dv_cliente"));
				lin1.setNom_cliente(rs.getString("nom_cliente"));
				lin1.setId_usuario(rs.getLong("id_usuario"));
				lin1.setMedioPago(rs.getString("medio_pago"));
				if (rs.getString("tipo_despacho") == null){
				    lin1.setTipo_despacho("N");
				}else{
					lin1.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				lin1.setZona_nombre(rs.getString("zona_nombre"));
				lin1.setId_zona(rs.getLong("id_zona"));
                
                if ( rs.getLong("MONTO_EXCEDIDO") == 0 ) {
                    lin1.setMontoExcedido(false);
                } else {
                    lin1.setMontoExcedido(true);    
                }
                if ( rs.getLong("ANULAR_BOLETA") == 0 ) {
                    lin1.setAnularBoleta(false);
                } else {
                    lin1.setAnularBoleta(true);    
                }

				boolean AlertaMPago = false;
				if (rs.getInt("ALERTA") > 0){
					AlertaMPago = true;
				}
				lin1.setConAlertaMPago(AlertaMPago);
				lin1.setOrigen(rs.getString("origen"));
				lin1.setTipo_ve(rs.getString("tipo_ve"));
                
                PedidoExtDTO pedExt = new PedidoExtDTO();
                pedExt.setIdPedido(rs.getLong("id_pedido"));
                
                if ( rs.getString("CUMPLIMIENTO") != null ) 
                    pedExt.setCumplimiento(rs.getString("CUMPLIMIENTO"));
                else
                    pedExt.setCumplimiento("");
                
                
                lin1.setPedExt(pedExt);
                
				lista_pedidos.add(lin1);
				hash.put(new Long(lin1.getId_pedido()), lin1);
			}
			
			
			if (idsPedidos.size() > 0){
				String sql = "select id_pedido, SUM(CANT_SOLIC) AS CANT_PRODS, SUM(CANT_SPICK) AS CANT_SPICK from bodba.bo_detalle_pedido " +
				" where id_pedido in " + DString.join(idsPedidos) + 
				" group by id_pedido";
                stm = conn.prepareStatement(sql + " WITH UR");
         		rs = stm.executeQuery();
        		while(rs.next()){
		          long id = rs.getLong("id_pedido");
		          MonitorPedidosDTO mp = (MonitorPedidosDTO)hash.get(new Long(id));
		          if(mp != null){
		            mp.setCant_prods(rs.getDouble("CANT_PRODS"));
		            mp.setCant_spick(rs.getDouble("CANT_SPICK"));
		          }
	        	}
			}
			

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return lista_pedidos;
	}
	
	
	
	//Max Maxbell para Homologacion
	public List getPedidosByCriteriaHomologacion(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		List lista_pedidos = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		GregorianCalendar hoy = new GregorianCalendar();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidosByCriteria");
		logger.debug("-----------------------------------------------------------------");

		
		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();
		String fecha_despacho  = criterio.getFdespacho();
		String fecha_picking = criterio.getFpicking();
		long id_estado = criterio.getId_estado();
		long id_jpicking = criterio.getId_jpicking();
		
		long id_local  = -1L;
		if (criterio.getId_local()>0){
			id_local = criterio.getId_local();	
		}
		
		long id_zona  = -1L;
		if (criterio.getId_zona()>0){
			id_zona = criterio.getId_zona();	
		}		
		
		long id_local_fact =-1L;
		if (criterio.getId_local_fact()>0){
			id_local_fact = criterio.getId_local_fact();	
		}
		 
		long id_cliente = -1L;		
		if (criterio.getId_cliente()>0){
			id_cliente = criterio.getId_cliente();
		}
		
		//busq por rut o apellido del cliente
		long rut = -1L;
		if (criterio.getCli_rut()!=null && !criterio.getCli_rut().equals(""))
			rut = Long.parseLong(criterio.getCli_rut());
		String apellido = "";
		if (criterio.getCli_ape()!=null && !criterio.getCli_ape().equals(""))
			apellido = criterio.getCli_ape(); 
		
		//bus por motivo
		long id_motivo = -1;
		if (criterio.getId_motivo()>0)
			id_motivo = criterio.getId_motivo();
		
		//Motivos sin gestion
		String sql_mot_sin_gestion = "";
		if (criterio.isSin_gestion()){
			sql_mot_sin_gestion = " AND P.id_mot is null ";
		}
		
		//Jdespacho AM o PM
		String sql_jdesp_dia = "";
		String jdesp_dia = criterio.getJdesp_dia();
		if(jdesp_dia != null && !jdesp_dia.equals("-1") && !jdesp_dia.equals("T")){
		    sql_jdesp_dia = " AND HD.HINI = TIME('"+ jdesp_dia +"') ";
			/*if(jdesp_dia.equals(Constantes.PARAMETRO_DIA)){
				sql_jdesp_dia = " AND HD.HINI < '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}else if(jdesp_dia.equals(Constantes.PARAMETRO_NOCHE)){
				sql_jdesp_dia = " AND HD.HINI >= '"+Constantes.PARAMETRO_TOPE_MNA+"' ";
			}*/
		}
			
		//busqueda por rango de fechas
		String tip_fec = criterio.getTip_fec();
		String fec_ini = criterio.getFec_ini();
		String fec_fin = criterio.getFec_fin();
		
		
		// Filtros de estado
		String[] filtro_estados = criterio.getFiltro_estados();
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		//		Origen y Tipo
		String sql_origen = "";
		String sql_tipo_picking = "";
		String sql_tipo_ve = "";
		String sql_tipo_desp = "";
		String sqlLimitaFecha = "1=1 ";
		//String sqlPedido = "";
		//String sql_LimiteFecha = "1=1 ";
		String origen = "";
		String tipo_ve="";
		String tipo_desp="";
		

		if (criterio.getOrigen()!=null){
			origen = criterio.getOrigen();
		}
				
		if (origen.equals(Constantes.ORIGEN_WEB_CTE)) {
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_WEB_CTE+"' ";
		} else if(origen.equals(Constantes.ORIGEN_VE_CTE)) {
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_VE_CTE+"' ";							
		} else if(origen.equals(Constantes.ORIGEN_JV_CTE)) {
            sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_JV_CTE+"' ";                         
        }
		
		if (criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_LIGHT_CTE)) {
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_LIGHT_CTE + "' ";
		} else if (criterio.getTipo_picking().equals(Constantes.TIPO_PICKING_NORMAL_CTE)) {
		    sql_tipo_picking = " AND P.TIPO_PICKING = '" + Constantes.TIPO_PICKING_NORMAL_CTE + "' ";
		}
		
		if(criterio.getTipo_ve()!=null){
			tipo_ve = criterio.getTipo_ve();
		}
		if(tipo_ve.equals(Constantes.TIPO_VE_NORMAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";							
		} else if(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}
		
		if(criterio.getTipo_desp()!=null){
			tipo_desp = criterio.getTipo_desp();
		}
		if(tipo_desp.equals(Constantes.TIPO_DESPACHO_NORMAL_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_NORMAL_CTE+"' ";							
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_EXPRESS_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_EXPRESS_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)){
			sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_ECONOMICO_CTE+"' ";
		} else if(tipo_desp.equals(Constantes.TIPO_DESPACHO_RETIRO_CTE)){
            sql_tipo_desp = " AND P.TIPO_DESPACHO = '"+Constantes.TIPO_DESPACHO_RETIRO_CTE+"' ";
        }
		
		if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
			hoy.add(Calendar.MONTH, -6);
			sqlLimitaFecha = " P.FCREACION >= ? ";
		}

		//paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;		
		
		logger.debug("Parametros getPedidosByCriteria:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("id estado :"+id_estado);
		logger.debug("id jornada picking  :"+id_jpicking);
		
		logger.debug("fecha_despacho :" +fecha_despacho);
		logger.debug("fecha_picking :"+fecha_picking);		
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		logger.debug("id_cliente:"+id_cliente);
		logger.debug("id_local:"+id_local);
		logger.debug("rut:"+rut);
		logger.debug("apellido:"+apellido);
		logger.debug("id_motivo:"+id_motivo);
		logger.debug("tip_fec :"+tip_fec);
		logger.debug("fec_ini :"+fec_ini);
		logger.debug("fec_fin :"+fec_fin);
		logger.debug("jdesp_dia :"+jdesp_dia);
		logger.debug("id_local_fact:"+id_local_fact);
		logger.debug("origen:"+origen);
		logger.debug("tipo_ve:"+tipo_ve);
		logger.debug("tipo_desp:"+tipo_desp);
		logger.debug("id_zona:"+id_zona);
		
		
		//		orden de columnas		
		String SqlOrder="";
		if (criterio.getOrden_columnas()!=null){
			String obj;			
			int x=1;
			for (int i=0; i<criterio.getOrden_columnas().size(); i++){
				if (x==1){
					SqlOrder += " ORDER BY ";
				}
				obj = (String) criterio.getOrden_columnas().get(i);
				logger.debug(  "Columna : "+obj );
				
				if (x>1) SqlOrder += " ,";
				SqlOrder += " "+obj+" ";
				x++;
			}
		}
		
		//genera sql		
		String Sqlpedido ="";
		String Sqlestado ="";
		String Sqlestados_in ="";
		String Sqlfdesp  ="";
		String Sqlfpick  ="";
		String Sqlcliente = "";
		String Sqlocal = "";
		String Sqlocal_fact = "";
		String SqlZona = "";
		String sqlRut = "";
		String sqlApe = "";
		String sqlMot = "";
		String sqlFdesp  ="";
		String sqlFpick  ="";
		
		
		/*GregorianCalendar hoy = new GregorianCalendar();
		hoy.add(Calendar.MONTH, -6);*/

		
		String Sql = "SELECT * FROM ( " +
		 " SELECT row_number() over("+SqlOrder+") as row, "+ 
				 " P.ID_PEDIDO, " +
				 " P.ID_JPICKING, " +
				 " P.FCREACION fcompra, " +
				 " JP.FECHA fpicking, "+
				 " JD.FECHA fdespacho, " +
				 " HD.HINI hdespacho, " +
				 " HD.HFIN hdespachof, " +
				 " E.NOMBRE estado, " +
				 " E.ID_ESTADO id_estado,"+
				 " P.MONTO_PEDIDO monto," +
				 " P.MEDIO_PAGO, " +
				 " P.TIPO_DESPACHO, " +
				 " L.nom_local local, " +
				 " LF.nom_local local_fact, " +
				 " P.nom_cliente, " +
				 " P.rut_cliente, " +
				 " P.dv_cliente, " +
				 " COALESCE(P.id_usuario,0) id_usuario, "+
				 " HP.HINI as hini_jpicking, " +
				 " HP.HFIN as hfin_jpicking, " +
				 " P.origen," +
				 " P.tipo_ve, " +
				 " Z.id_zona, " +
				 " Z.nombre as zona_nombre," +
				 " Z.orden_zona as zona_orden," +
                 " pe.CUMPLIMIENTO," +
                 " P.MONTO_EXCEDIDO, " +
                 " P.ANULAR_BOLETA, " +
				 " COUNT(DISTINCT AP.ID_ALERTA) as alerta " +
		 " FROM BODBA.BO_PEDIDOS P " +
		 "      JOIN BODBA.BO_LOCALES         L ON P.id_local      = L.id_local " +
		 "      JOIN BODBA.BO_LOCALES        LF ON P.id_local_fact = LF.id_local " +
		 "      JOIN BODBA.BO_JORNADA_DESP   JD ON P.ID_JDESPACHO  = JD.ID_JDESPACHO " +
		 "      left outer JOIN BODBA.BO_JORNADAS_PICK  JP ON P.ID_JPICKING   = JP.ID_JPICKING " +
		 "      left outer JOIN BODBA.BO_HORARIO_PICK   HP ON HP.ID_HOR_PICK  = JP.ID_HOR_PICK " +
		 "      JOIN BODBA.BO_HORARIO_DESP   HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " + sql_jdesp_dia +
		 "      JOIN BODBA.BO_ESTADOS         E ON E.ID_ESTADO     = P.ID_ESTADO " +
		 "      JOIN BODBA.BO_ZONAS           Z ON Z.ID_ZONA       = P.ID_ZONA " +
		 "      left JOIN bodba.BO_ALERTA_OP AP ON AP.ID_PEDIDO    = P.ID_PEDIDO and AP.ID_ALERTA BETWEEN 16 AND 24 " +
         "      left outer join bodba.bo_pedidos_ext pe on p.id_pedido = pe.id_pedido " +
         //" WHERE  " + sqlLimitaFecha + " and p.origen != 'C' and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + sql_origen  + sql_tipo_picking + sql_tipo_ve + sql_tipo_desp;
       //Maxbell Homologacion pantallas BOL
         " WHERE  " + sqlLimitaFecha + " and p.origen != 'C' and P.id_estado in (" + Constantes.ESTADOS_PICKEADOS_FINALIZADOS + ") " + sql_origen  + sql_tipo_picking + sql_tipo_ve + sql_tipo_desp;
		if (id_cliente>0){ Sqlcliente = " AND P.id_cliente = "+id_cliente; }
		if (id_local>0){ Sqlocal = " AND P.id_local = "+id_local; }
		if (id_local_fact>0){ Sqlocal_fact = " AND P.id_local_fact = "+id_local_fact; }
		if (id_zona>0){ SqlZona = " AND Z.id_zona = "+id_zona; }
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	
			//Maxbell Homologacion pantallas BOL
			//if(id_estado==34) {Sqlestado = " AND P.id_estado IN ("+Constantes.ESTADOS_PEDIDO_PARA_PICK+")";}
			if(id_estado==34) {Sqlestado = " AND P.id_estado IN ("+Constantes.ID_ESTAD_PEDIDO_INGRESADO+","+Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION+")";} 
			else {Sqlestado = " AND P.id_estado = "+id_estado;}	}
		if ( !estados_in.equals("") ){ Sqlestados_in = " AND P.id_estado IN (" + estados_in + ") " ; }
		if ((fecha_despacho!=null) && (fecha_despacho!=""))
			{ Sqlfdesp  = " AND JD.fecha = '"+fecha_despacho+"'"; }
		if ((fecha_picking!=null) && (fecha_picking!=""))
			{ Sqlfpick  = " AND JP.fecha = '"+fecha_picking+"'";	}
		if (id_jpicking >0) { Sqlpedido  += " AND JP.id_jpicking = "+id_jpicking;	}
		//busqueda por rango de fechas
		if (tip_fec.equals("despacho") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFdesp = " AND (JD.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} else if (tip_fec.equals("picking") && !(fec_ini.equals("")) && !(fec_fin.equals("")) ){
			sqlFpick = " AND (JP.fecha between '" + fec_ini + "'  AND '" + fec_fin + "')";
		} 
		
		if (rut>0) { sqlRut = " AND rut_cliente = "+rut; }
		if (!apellido.equals("")) { sqlApe = " AND upper(nom_cliente) like '%"+apellido.toUpperCase()+"%'";}
		if (id_motivo>0) { sqlMot = " AND P.id_mot = "+id_motivo;}
		//genero las condiciones
		Sql = Sql + Sqlfpick + Sqlfdesp + sqlFdesp + sqlFpick + Sqlestado + Sqlestados_in + Sqlpedido + SqlZona + Sqlocal + Sqlocal_fact + Sqlcliente + sqlRut + sqlApe + sqlMot + sql_mot_sin_gestion;
		//paginador
		Sql = Sql + " GROUP BY P.ID_PEDIDO, P.ID_JPICKING, P.FCREACION, JP.FECHA, JD.FECHA, HD.HINI, HD.HFIN, "
                  + "          E.NOMBRE, E.ID_ESTADO, P.MONTO_PEDIDO, P.MEDIO_PAGO, P.TIPO_DESPACHO, L.NOM_LOCAL, "
                  + "          LF.NOM_LOCAL, P.NOM_CLIENTE, P.RUT_CLIENTE, P.DV_CLIENTE, P.ID_USUARIO, "
                  + "          HP.HINI, HP.HFIN, P.ORIGEN, P.TIPO_VE, Z.ID_ZONA, Z.NOMBRE, Z.ORDEN_ZONA, pe.CUMPLIMIENTO, P.MONTO_EXCEDIDO, P.ANULAR_BOLETA, "
                  + "          P.CANT_PRODUCTOS "
                  + ") AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;

		logger.debug("SQL con criterio :"+Sql);
		
		try{

			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			if (criterio.getId_pedido() <= 0 && criterio.isLimitarFecha()){
			    stm.setDate(1, new java.sql.Date(hoy.getTimeInMillis()));
			}
			
			rs = stm.executeQuery();
			
			List idsPedidos = new ArrayList();
			Hashtable hash = new Hashtable();
			while (rs.next()) {
				MonitorPedidosDTO lin1 = new MonitorPedidosDTO();
				idsPedidos.add(new Long(rs.getLong("id_pedido")));
				lin1.setId_pedido(rs.getLong("id_pedido"));
				lin1.setId_jpicking(rs.getLong("id_jpicking"));
				lin1.setFpicking(rs.getString("fpicking"));
				lin1.setFdespacho(rs.getString("fdespacho"));
				lin1.setHdespacho(rs.getString("hdespacho"));
				lin1.setHdespacho_fin(rs.getString("hdespachof"));
				lin1.setEstado(rs.getString("estado"));
				lin1.setId_estado(rs.getLong("id_estado"));
				//lin1.setCant_prods(rs.getDouble("cant_prods"));
				//lin1.setCant_spick(rs.getDouble("CANT_SPICK"));
				lin1.setHini_jpicking(rs.getString("hini_jpicking"));
				lin1.setHfin_jpicking(rs.getString("hfin_jpicking"));
				//agregados para el boc
				lin1.setLocal_despacho(rs.getString("local"));
				lin1.setLocal_facturacion(rs.getString("local_fact"));
				lin1.setMonto(rs.getLong("monto"));
				lin1.setFingreso(rs.getString("fcompra"));
				lin1.setRut_cliente(rs.getLong("rut_cliente"));
				lin1.setDv_cliente(rs.getString("dv_cliente"));
				lin1.setNom_cliente(rs.getString("nom_cliente"));
				lin1.setId_usuario(rs.getLong("id_usuario"));
				lin1.setMedioPago(rs.getString("medio_pago"));
				if (rs.getString("tipo_despacho") == null){
				    lin1.setTipo_despacho("N");
				}else{
					lin1.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				lin1.setZona_nombre(rs.getString("zona_nombre"));
				lin1.setId_zona(rs.getLong("id_zona"));
                
                if ( rs.getLong("MONTO_EXCEDIDO") == 0 ) {
                    lin1.setMontoExcedido(false);
                } else {
                    lin1.setMontoExcedido(true);    
                }
                if ( rs.getLong("ANULAR_BOLETA") == 0 ) {
                    lin1.setAnularBoleta(false);
                } else {
                    lin1.setAnularBoleta(true);    
                }

				boolean AlertaMPago = false;
				if (rs.getInt("ALERTA") > 0){
					AlertaMPago = true;
				}
				lin1.setConAlertaMPago(AlertaMPago);
				lin1.setOrigen(rs.getString("origen"));
				lin1.setTipo_ve(rs.getString("tipo_ve"));
                
                PedidoExtDTO pedExt = new PedidoExtDTO();
                pedExt.setIdPedido(rs.getLong("id_pedido"));
                
                if ( rs.getString("CUMPLIMIENTO") != null ) 
                    pedExt.setCumplimiento(rs.getString("CUMPLIMIENTO"));
                else
                    pedExt.setCumplimiento("");
                
                
                lin1.setPedExt(pedExt);
                
				lista_pedidos.add(lin1);
				hash.put(new Long(lin1.getId_pedido()), lin1);
			}
			
			
			if (idsPedidos.size() > 0){
				//String sql = "select id_pedido, SUM(CANT_SOLIC) AS CANT_PRODS, SUM(CANT_SPICK) AS CANT_SPICK from bodba.bo_detalle_pedido " +
				//Maxbell - Homologacion pantallas BOL
				//String sql = "select id_pedido, SUM(cast(CANT_SOLIC as int)) AS CANT_PRODS, SUM(CANT_SPICK) AS CANT_SPICK from bodba.bo_detalle_pedido " +
				String sql = "select id_pedido, SUM(cast(CANT_SOLIC as int)) AS CANT_PRODS, SUM(cast(CANT_PICK as int)) AS CANT_PICK, SUM(CANT_SPICK) AS CANT_SPICK " +
						"from bodba.bo_detalle_pedido " +
				" where id_pedido in " + DString.join(idsPedidos) + 
				" group by id_pedido";
                stm = conn.prepareStatement(sql + " WITH UR");
         		rs = stm.executeQuery();
        		while(rs.next()){
		          long id = rs.getLong("id_pedido");
		          MonitorPedidosDTO mp = (MonitorPedidosDTO)hash.get(new Long(id));
		          if(mp != null){
		            mp.setCant_prods(rs.getDouble("CANT_PRODS"));
		            mp.setCant_pick(rs.getDouble("CANT_PICK"));
		            mp.setCant_spick(rs.getDouble("CANT_SPICK"));
		          }
	        	}
			}
			

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return lista_pedidos;
	}

	/**
	 * Lista pedidos por criterio
	 * El cirterio esta dado por :PedidosCriteriaDTO
	 * nº pedido o nº de bin
	 * y fecha de despacho o fecha de picking
	 * y/o estado
	 * y paginacion
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getPedidosByCriteria(cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO)
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidosDTO
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getPedidosXJornada(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		List lista_pedidos = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidosByCriteria");
		logger.debug("-----------------------------------------------------------------");

		
		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();

		long id_estado = criterio.getId_estado();
		long id_jpicking = criterio.getId_jpicking();
		
		long id_local  = -1L;
		if (criterio.getId_local()>0){
			id_local = criterio.getId_local();	
		}
		
		long id_zona  = -1L;
		if (criterio.getId_zona()>0){
			id_zona = criterio.getId_zona();	
		}		
		
		//Origen y Tipo
		String sql_origen = "";
		String sql_tipo_ve = "";
		String sql_tipo_desp = "";
		String origen = "";
		String tipo_ve="";


		if (criterio.getOrigen()!=null){
			origen = criterio.getOrigen();
		}
		
		if(origen.equals(Constantes.ORIGEN_WEB_CTE)){
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_WEB_CTE+"' ";
		}else if(origen.equals(Constantes.ORIGEN_VE_CTE)){				
			sql_origen = " AND P.ORIGEN = '"+Constantes.ORIGEN_VE_CTE+"' ";							
		}
		
		if(criterio.getTipo_ve()!=null){
			tipo_ve = criterio.getTipo_ve();
		}
		if(tipo_ve.equals(Constantes.TIPO_VE_NORMAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";							
		} else if(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE)){
			sql_tipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}
		
				
		//paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;		
		
		logger.debug("Parametros getPedidosByCriteria:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("id estado :"+id_estado);
		logger.debug("id jornada picking  :"+id_jpicking);
		
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		logger.debug("id_local:"+id_local);
		logger.debug("origen:"+origen);
		logger.debug("tipo_ve:"+tipo_ve);
		logger.debug("id_zona:"+id_zona);
		
		
		//orden de columnas		
		String SqlOrder="";
		if (criterio.getOrden_columnas()!=null){
			String obj;			
			int x=1;
			for (int i=0; i<criterio.getOrden_columnas().size(); i++){
				if (x==1){
					SqlOrder += " ORDER BY ";
					}
				obj = (String) criterio.getOrden_columnas().get(i);
				logger.debug(  "Columna : "+obj );
				
				if (x>1) SqlOrder += " ,";					
				SqlOrder += " "+obj+" ";
				x++;
			}
		}
		
		//genera sql		
		String Sqlpedido ="";
		String Sqlestado ="";
		String Sqlestados_in ="";
		String Sqlfdesp  ="";
		String Sqlfpick  ="";
		String Sqlcliente = "";
		String Sqlocal = "";
		String Sqlocal_fact = "";
		String SqlZona = "";
		String sqlRut = "";
		String sqlApe = "";
		String sqlMot = "";
		String sqlFdesp  ="";
		String sqlFpick  ="";
		
		String Sql = "SELECT * FROM ( " +
		 " SELECT row_number() over("+SqlOrder+") as row, "+ 
				 " P.ID_PEDIDO id_pedido, " +
				 " P.ID_JPICKING id_jpicking, " +
				 " P.FCREACION fingreso, " +
				 " JP.FECHA fpicking, "+ 
				 " P.CANT_PRODUCTOS cant_prods, " +
				 " JD.FECHA fdespacho, " +
				 " HD.HINI hdespacho, " +
				 " HD.HFIN hdespachof, " +
				 " E.NOMBRE estado, " +
				 " E.ID_ESTADO id_estado,"+
				 " P.MONTO_PEDIDO monto," +
				 " P.MEDIO_PAGO medio_pago, " +
				 " P.TIPO_DESPACHO tipo_despacho, " +
				 " L.nom_local local, " +
				 " LF.nom_local local_fact, " +
				 " P.monto_pedido monto, " +
				 " P.fcreacion fcompra, " +
				 " P.nom_cliente nom_cliente, " +
				 " P.rut_cliente rut_cliente, " +
				 " P.dv_cliente dv_cliente, " +
				 " COALESCE(P.id_usuario,0) id_usuario, "+
				 " HP.HINI as hini_jpicking, " +
				 " HP.HFIN as hfin_jpicking, " +
                 "               CASE WHEN (CANTIDAD > 0) THEN 1 " +
                 "                    WHEN (CANTIDAD IS NULL) THEN 0 " +
                 "                    ELSE 0  " +
                 "               END AS ALERTA, " +				 
				 " P.origen origen," +
				 " P.tipo_ve tipo_ve, " +
				 " Z.id_zona id_zona, " +
				 " Z.nombre as zona_nombre," +
				 " Z.orden_zona as zona_orden " +
		 " FROM BO_PEDIDOS P "+
		 " JOIN BO_LOCALES L ON P.id_local = L.id_local " +
		 " JOIN BO_LOCALES LF ON P.id_local_fact = LF.id_local " +
		 " JOIN BO_JORNADA_DESP JD ON P.ID_JDESPACHO = JD.ID_JDESPACHO "+
		 " JOIN BO_JORNADAS_PICK JP ON P.ID_JPICKING = JP.ID_JPICKING "+
		 " JOIN BO_HORARIO_PICK HP ON HP.ID_HOR_PICK = JP.ID_HOR_PICK " +
		 " JOIN BO_ESTADOS E ON E.ID_ESTADO = P.ID_ESTADO " +
		 " JOIN BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA " +
		 " LEFT JOIN (SELECT COUNT(AP.ID_ALERTA) CANTIDAD, AP.ID_PEDIDO  "
                   + "            FROM BODBA.BO_ALERTA_OP AP "
                   + "            WHERE AP.ID_ALERTA BETWEEN 16 AND 24 "
                   + "            GROUP BY AP.ID_PEDIDO) AS ALERTA2 "
                   + "      ON ALERTA2.ID_PEDIDO = P.ID_PEDIDO  " +
         " WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + sql_origen + sql_tipo_ve + sql_tipo_desp;
		if (id_local>0){ Sqlocal = " AND P.id_local = "+id_local; }
		if (id_zona>0){ SqlZona = " AND Z.id_zona = "+id_zona; }
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	
			if(id_estado==34) {Sqlestado = " AND P.id_estado IN ("+Constantes.ID_ESTAD_PEDIDO_INGRESADO+","+Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION+")";} 
			else {Sqlestado = " AND P.id_estado = "+id_estado;}	}
		if (id_jpicking >0) { Sqlpedido  += " AND JP.id_jpicking = "+id_jpicking;	}
		//busqueda por rango de fechas
		//genero las condiciones
		Sql = Sql + Sqlfpick + Sqlfdesp + sqlFdesp + sqlFpick + Sqlestado + Sqlestados_in + Sqlpedido + SqlZona + Sqlocal + Sqlocal_fact + Sqlcliente + sqlRut + sqlApe + sqlMot;
		//paginador
		Sql = Sql + ") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
	

		logger.debug("SQL con criterio :"+Sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				MonitorPedidosDTO lin1 = new MonitorPedidosDTO();
				lin1.setId_pedido(rs.getLong("id_pedido"));
				lin1.setId_jpicking(rs.getLong("id_jpicking"));
				lin1.setFpicking(rs.getString("fpicking"));
				lin1.setFdespacho(rs.getString("fdespacho"));
				lin1.setHdespacho(rs.getString("hdespacho"));
				lin1.setHdespacho_fin(rs.getString("hdespachof"));
				lin1.setEstado(rs.getString("estado"));
				lin1.setId_estado(rs.getLong("id_estado"));
				lin1.setCant_prods(rs.getDouble("cant_prods"));
				lin1.setHini_jpicking(rs.getString("hini_jpicking"));
				lin1.setHfin_jpicking(rs.getString("hfin_jpicking"));
				//agregados para el boc
				lin1.setLocal_despacho(rs.getString("local"));
				lin1.setLocal_facturacion(rs.getString("local_fact"));
				lin1.setMonto(rs.getLong("monto"));
				lin1.setFingreso(rs.getString("fcompra"));
				lin1.setRut_cliente(rs.getLong("rut_cliente"));
				lin1.setDv_cliente(rs.getString("dv_cliente"));
				lin1.setNom_cliente(rs.getString("nom_cliente"));
				lin1.setId_usuario(rs.getLong("id_usuario"));
				lin1.setMedioPago(rs.getString("medio_pago"));
				if (rs.getString("tipo_despacho") == null){
				    lin1.setTipo_despacho("N");
				}else{
					lin1.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				lin1.setZona_nombre(rs.getString("zona_nombre"));
				lin1.setId_zona(rs.getLong("id_zona"));
				
				//Recupera el Listado de Alertas de un Pedido
/*				boolean AlertaMPago = false;
				List lstAlertas = this.getAlertasPedido(rs.getLong("id_pedido"));
				if (lstAlertas.size()>0){
					for(int i=0; i<lstAlertas.size(); i++){
						AlertaDTO alerta = (AlertaDTO)lstAlertas.get(i);
						int idAlerta = (int)alerta.getId_alerta();
						if (idAlerta > 15 && idAlerta < 25 && AlertaMPago == false){
							AlertaMPago = true;
						}
					}
				}
				lin1.setConAlertaMPago(AlertaMPago);
*/				//*******************************************
				boolean AlertaMPago = false;
				if (rs.getInt("ALERTA") > 0){
					AlertaMPago = true;
				}
				lin1.setConAlertaMPago(AlertaMPago);
				lin1.setOrigen(rs.getString("origen"));
				lin1.setTipo_ve(rs.getString("tipo_ve"));
				lista_pedidos.add(lin1);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return lista_pedidos;
	}

	
	
	/**
	 * Retorna el número de registros de una consulta por fecha
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidosDTO
	 * @throws PedidosDAOException 
	 */
	public List getPedidosByFecha(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		List lista_pedidos = new ArrayList();
		PreparedStatement stm = null;			
		ResultSet rs = null;
		
		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();
		long id_estado = criterio.getId_estado();
		String tip_fec = criterio.getTip_fec();
		String fec_ini = criterio.getFec_ini();
		String fec_fin = criterio.getFec_fin();
		long id_motivo = criterio.getId_motivo();
		
		//Motivos sin gestion
		String sql_mot_sin_gestion = "";
		if (criterio.isSin_gestion()){
			sql_mot_sin_gestion = " AND P.id_mot is null ";
		}
		
		//paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidosByFecha");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getPedidosByFecha:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("id estado :"+id_estado);
		logger.debug("tip_fec :"+tip_fec);
		logger.debug("fec_ini :"+fec_ini);
		logger.debug("fec_fin :"+fec_fin);
		logger.debug("id_motivo :"+id_motivo);

		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		
		//genera sql		
		String Sqlpedido ="";
		String Sqlestado ="";
		String sqlFdesp  ="";
		String sqlFpick  ="";
		String sqlMot 	 ="";
		
		String Sql = "SELECT * FROM ( " +
		 " SELECT row_number() over(order by id_pedido) as row, "+ 
		 " P.ID_PEDIDO id_pedido, P.ID_JPICKING id_jpicking, P.FCREACION fingreso, JP.FECHA fpicking, "+ 
		 " P.CANT_PRODUCTOS cant_prods, JD.FECHA fdespacho, HD.HINI hdespacho, E.NOMBRE estado, "+
		 " P.MONTO_PEDIDO monto,P.MEDIO_PAGO medio_pago, L.nom_local local, P.monto_pedido monto, " +
		 " P.fcreacion fcompra, P.nom_cliente nom_cliente, P.rut_cliente rut_cliente, " +
		 " P.dv_cliente dv_cliente, COALESCE(P.id_usuario,0) id_usuario"+
		 " FROM BO_PEDIDOS P "+
		 " JOIN BO_LOCALES L ON P.id_local = L.id_local " +
		 " JOIN BO_JORNADA_DESP JD ON P.ID_JDESPACHO = JD.ID_JDESPACHO "+
		 " JOIN BO_JORNADAS_PICK JP ON P.ID_JPICKING = JP.ID_JPICKING "+
		 " JOIN BO_HORARIO_DESP HD ON HD.ID_HOR_DESP = JD.ID_HOR_DESP " +
		 " JOIN BO_ESTADOS E ON E.ID_ESTADO = P.ID_ESTADO " +
		 " WHERE  P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	Sqlestado = " AND P.id_estado = "+id_estado;	}
		if (id_motivo>0) { sqlMot = " AND P.id_mot = "+id_motivo;}
		if (tip_fec.equals("despacho")){
			if(!fec_ini.equals(""))	sqlFdesp = " AND JD.fecha >= '"+fec_ini+"' ";
			if(!fec_fin.equals(""))	sqlFdesp += " AND JD.fecha <= '"+fec_fin+"' ";
		} else if (tip_fec.equals("picking")){
			if(!fec_ini.equals(""))	sqlFpick = " AND JP.fecha >= '"+fec_ini+"' ";
			if(!fec_fin.equals(""))	sqlFpick += " AND JP.fecha <= '"+fec_fin+"' ";
		} 
		//genero las condiciones
		Sql = Sql + sqlMot + sqlFdesp + sqlFpick + Sqlestado + Sqlpedido + sql_mot_sin_gestion ;
		//paginador
		Sql = Sql + ") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
		logger.debug("SQL con criterio :"+Sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorPedidosDTO lin1 = new MonitorPedidosDTO();
				lin1.setId_pedido(rs.getLong("id_pedido"));
				lin1.setId_jpicking(rs.getLong("id_jpicking"));
				lin1.setFpicking(rs.getString("fpicking"));
				lin1.setFdespacho(rs.getString("fdespacho"));
				lin1.setHdespacho(rs.getString("hdespacho"));
				lin1.setEstado(rs.getString("estado"));
				lin1.setCant_prods(rs.getDouble("cant_prods"));
				//agregados para el boc
				lin1.setLocal_despacho(rs.getString("local"));
				lin1.setMonto(rs.getLong("monto"));
				lin1.setFingreso(rs.getString("fcompra"));
				lin1.setRut_cliente(rs.getLong("rut_cliente"));
				lin1.setDv_cliente(rs.getString("dv_cliente"));
				lin1.setNom_cliente(rs.getString("nom_cliente"));
				lin1.setId_usuario(rs.getLong("id_usuario"));
				lista_pedidos.add(lin1);
				}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return lista_pedidos;
	}

	
	/**
	 * Obtiene Listado de Pedidos por Jornada de Picking
	 * 
	 * @param  jpicking long 
	 * @return List
	 * @throws PedidosDAOException 
	 * 
	 */
	public List getPedidoByJornadaPicking(long jpicking) throws PedidosDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		List ped1 = null;
		try {
			conn = this.getConnection();
			String sql = "SELECT P.ID_PEDIDO ID_PEDIDO "
			           + "FROM BODBA.BO_PEDIDOS P "
			           + "WHERE P.ID_JPICKING = " + jpicking;
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query(getPedidoByJornadaPicking): " + sql);
			rs = stm.executeQuery();
			ped1 = new ArrayList();
			int i=0;
			if (rs.next()) {
				ped1.add(i, rs.getString("ID_PEDIDO"));
				i++;
			}

			
			if(i==0)
				throw new PedidosDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return ped1;		
	}
	
	
	
	
	/**
	 * Retorna el número de registros de una consulta por fecha
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return long
	 * @throws PedidosDAOException 
	 */
	public long getCountPedidosByFecha(PedidosCriteriaDTO criterio) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;

		int numPed = 0;

		//solo se busca por numero de pedido y no por numero de bin
		long id_pedido = criterio.getId_pedido();
		long id_estado = criterio.getId_estado();
		String tip_fec = criterio.getTip_fec();
		String fec_ini = criterio.getFec_ini();
		String fec_fin = criterio.getFec_fin();
		long id_motivo = criterio.getId_motivo();

		//Motivos sin gestion
		String sql_mot_sin_gestion = "";
		if (criterio.isSin_gestion()){
			sql_mot_sin_gestion = " AND P.id_mot is null ";
		}
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getCountPedidosByFecha");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getCountPedidosByFecha:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("id estado :"+id_estado);
		logger.debug("tip_fec :"+tip_fec);
		logger.debug("fec_ini :"+fec_ini);
		logger.debug("fec_fin :"+fec_fin);

		//genera sql
		String Sqlpedido ="";
		String Sqlestado ="";
		String sqlFdesp  ="";
		String sqlFpick  ="";
		String sqlMot    ="";
		
		String Sql = "SELECT count(*) cantidad  " +
		 " FROM BO_PEDIDOS P "+
		 " JOIN BO_LOCALES L ON P.id_local = L.id_local " +
		 " JOIN BO_JORNADA_DESP JD ON P.ID_JDESPACHO = JD.ID_JDESPACHO "+
		 " JOIN BO_JORNADAS_PICK JP ON P.ID_JPICKING = JP.ID_JPICKING "+
		 " JOIN BO_HORARIO_DESP HD ON HD.ID_HOR_DESP = JD.ID_HOR_DESP " +
		 " JOIN BO_ESTADOS E ON E.ID_ESTADO = P.ID_ESTADO " +
		 " WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
		if (id_pedido>0){	Sqlpedido = " AND P.id_pedido = "+id_pedido;	}
		if (id_estado>0){	Sqlestado = " AND P.id_estado = "+id_estado;	}
		if (id_motivo>0) { sqlMot = " AND P.id_mot = "+id_motivo;}
		if (tip_fec.equals("despacho")){
			if(!fec_ini.equals(""))	sqlFdesp = " AND JD.fecha >= '"+fec_ini+"' ";
			if(!fec_fin.equals(""))	sqlFdesp += " AND JD.fecha <= '"+fec_fin+"' ";
		} else if (tip_fec.equals("picking")){
			if(!fec_ini.equals(""))	sqlFpick = " AND JP.fecha >= '"+fec_ini+"' ";
			if(!fec_fin.equals(""))	sqlFpick += " AND JP.fecha <= '"+fec_fin+"' ";
		} 
		//genero las condiciones
		Sql = Sql + sqlMot + sqlFpick + sqlFdesp + Sqlestado + Sqlpedido + sql_mot_sin_gestion ;
		logger.debug("SQL con criterio :"+Sql);

		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				numPed = rs.getInt("cantidad");
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return numPed;
	}
	
	
	/**
	 * Obtiene listado de productos de un pedido
	 * 
	 * @param  criterio DetallePedidoCriteriaDTO 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException 
	 */
	public List getProductosPedido(DetallePedidoCriteriaDTO criterio) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		long id_pedido 	= 0;
		//long id_ronda 	= 0;
		long id_sector	= 0;
		
		id_pedido 	= criterio.getId_pedido();
		id_sector	= criterio.getId_sector();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getProductosPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getProductosPedido:");
		logger.debug("numero_pedido:"+id_pedido);
		
		String Sql = "SELECT  DP.ID_DETALLE, DP.ID_PEDIDO, DP.COD_PROD1, DP.UNI_MED, S.NOMBRE NOM_SECTOR, DP.ID_SECTOR, "
                   + "        DP.ID_PRODUCTO, PF.PRO_ID ID_PROD_FO, DP.DESCRIPCION DESCR, DP.CANT_SOLIC CANTIDAD, DP.OBSERVACION OBS, " 
                   + "        DP.PREPARABLE, DP.CON_NOTA NOTA, DP.PRECIO, DP.PESABLE, DP.CANT_PICK, " 
                   + "        DP.CANT_FALTAN CANT_FALTAN, DP.CANT_SPICK, DP.TIPO_SEL, P.ID_CATPROD, " 
                   + "        DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_PEDIDO DP " 
                   + "     left join fodba.fo_sustitutos_criterio sc on (DP.ID_CRITERIO = sc.ID_CRITERIO) "
                   + "     LEFT JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR    = DP.ID_SECTOR "
                   + "     JOIN BODBA.BO_PRODUCTOS   P ON DP.ID_PRODUCTO = P.ID_PRODUCTO "
                   + "     JOIN FODBA.FO_PRODUCTOS  PF ON PF.PRO_ID_BO   = P.ID_PRODUCTO "
                   + "WHERE 1=1 ";
		if( id_pedido > 0 )
			Sql += " AND DP.ID_PEDIDO = " + id_pedido + " ";
			
		if( id_sector > 0 )
			Sql += " AND dp.id_sector = " + id_sector + " ";
		
		//Sql +=  " ORDER BY nom_sector, descr";
		Sql +=  " ORDER BY descr";
		
		logger.debug("SQL :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getInt("ID_DETALLE"));
				prod.setId_producto(rs.getInt("ID_PRODUCTO"));
				prod.setId_prod_fo(rs.getInt("ID_PROD_FO"));
				prod.setCod_producto(rs.getString("COD_PROD1"));
				prod.setCod_sap(rs.getString("COD_PROD1"));
				prod.setUnid_medida(rs.getString("UNI_MED"));
				prod.setId_pedido(rs.getLong("ID_PEDIDO"));
				prod.setId_sector(rs.getInt("ID_SECTOR"));
				prod.setDescripcion(rs.getString("DESCR"));
				prod.setObservacion(rs.getString("OBS"));
				prod.setSector(rs.getString("NOM_SECTOR"));
				prod.setCant_solic(rs.getDouble("CANTIDAD"));
				prod.setPrecio(rs.getDouble("PRECIO"));
				prod.setPesable(rs.getString("PESABLE"));
				prod.setCant_pick(rs.getDouble("CANT_PICK"));
				prod.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				prod.setCant_spick(rs.getDouble("CANT_SPICK"));
				prod.setTipoSel(rs.getString("TIPO_SEL"));
				prod.setId_catprod(rs.getString("ID_CATPROD"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));             
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    prod.setDescCriterio(rs.getString("criterio"));
                } 
                
				list_prod.add(prod);
			}
			

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}
	
	/**
	 * Obtiene listado de alertas de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List AlertaDTO
	 * @throws PedidosDAOException 
	 */
	public List getAlertasPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_aler = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getAlertasPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getAlertasPedido:");
		logger.debug("numero_pedido:"+id_pedido);
		
		String Sql = "SELECT A.id_alerta id_aler, A.nombre nombre, A.descripcion descrip, " 
			       + "       A.tipo tipo, A.orden orden, A.activo activo "
			       + "FROM bo_alertas A, bo_alerta_op AO " 
			       + "WHERE A.id_alerta = AO.id_alerta AND AO.id_pedido = ? " 
			       + "ORDER BY A.id_alerta ";
		logger.debug("sql:"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				AlertaDTO aler =new AlertaDTO();
				aler.setId_alerta(rs.getLong("id_aler"));
				aler.setNombre(rs.getString("nombre"));
				aler.setDescripcion(rs.getString("descrip"));
				aler.setTipo(rs.getString("tipo"));
				aler.setOrden(rs.getInt("orden"));
				aler.setActivo(rs.getString("activo"));
				list_aler.add(aler);
				}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_aler;
	}
	
	
	/**
	 * 
	 */
	public List getAlertaPedidoByKey(long id_pedido, int keyAlerta) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_aler = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getAlertaPedidoByKey");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getAlertaPedidoByKey:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("key Alerta:"+keyAlerta);
		
		String Sql = "SELECT A.id_alerta id_aler, A.nombre nombre, A.descripcion descrip, " 
			       + "       A.tipo tipo, A.orden orden, A.activo activo "
			       + "FROM bo_alertas A, bo_alerta_op AO " 
			       + "WHERE A.id_alerta = AO.id_alerta AND AO.id_pedido = ? AND A.id_alerta = ? " 
			       + "ORDER BY A.id_alerta ";
		logger.debug("sql:"+Sql);
		
		try {
			conn = this.getConnection();			
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			stm.setLong(2, keyAlerta);
			rs = stm.executeQuery();
			while (rs.next()) {
				AlertaDTO aler =new AlertaDTO();
				aler.setId_alerta(rs.getLong("id_aler"));
				aler.setNombre(rs.getString("nombre"));
				aler.setDescripcion(rs.getString("descrip"));
				aler.setTipo(rs.getString("tipo"));
				aler.setOrden(rs.getInt("orden"));
				aler.setActivo(rs.getString("activo"));
				list_aler.add(aler);
				}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_aler;
	}
	
	
	/**
	 * Obtiene listado de alertas de un pedido
	 * 
	 * @param  none
	 * @return List AlertaDTO
	 * @throws PedidosDAOException 
	 * agregado por Alvaro Gutierrez
	 */
//0121005_AG 
	public List getAlertasActivas() throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_aler = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getAlertasActivas");
		logger.debug("-----------------------------------------------------------------");
		
		
		String Sql = "SELECT A.ID_ALERTA, A.NOMBRE, A.DESCRIPCION, A.TIPO, A.ORDEN, A.ACTIVO" +
				" FROM BO_ALERTAS A" +
				" WHERE A.ACTIVO = 'A'";
		logger.debug("sql:"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				AlertaDTO aler =new AlertaDTO();				                          
				            
				aler.setId_alerta(rs.getLong("ID_ALERTA"));
				aler.setNombre(rs.getString("NOMBRE"));
				aler.setDescripcion(rs.getString("DESCRIPCION"));
				aler.setTipo(rs.getString("TIPO"));
				aler.setOrden(rs.getInt("ORDEN"));
				aler.setActivo(rs.getString("ACTIVO"));
				list_aler.add(aler);
				}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_aler;
//_0121005_AG 		
	}
	
	/**
	 * Entrega listado de bins de acuerdo a un criterio
	 * 
	 * @param  criterio BinCriteriaDTO 
	 * @return List BinDTO
	 * @throws PedidosDAOException 
	 */
	public List getBinsByCriteria(BinCriteriaDTO criterio) throws PedidosDAOException {		
		PreparedStatement stm 	= null;			
		ResultSet rs 			= null;
		List list_bins 			= new ArrayList();
		
		long id_pedido	= 0;
		long id_ronda	= 0;
		String idPedido = "";
		String idRonda  = "";
		
		id_pedido	= criterio.getId_pedido();
		id_ronda	= criterio.getId_ronda();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getBinsPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getBinsPedido:");
		logger.debug("numero_pedido:"+id_pedido);
		logger.debug("numero_ronda:"+id_ronda);
		
		if ( id_pedido > 0 )
			idPedido = "  AND B.ID_PEDIDO = " + id_pedido + " ";
		if ( id_ronda > 0 )
			idRonda  = "  AND B.ID_RONDA = " + id_ronda + " ";

		String Sql = "SELECT B.ID_BP, B.ID_PEDIDO, B.ID_RONDA, "
                   + "       B.COD_BIN, B.VISUALIZADO, "
                   + "       B.TIPO, R.ID_SECTOR, S.NOMBRE AS SECTOR, "
                   + "       COALESCE(PA.CANT_PROD_AUDIT, 0) AS CANT_PROD_AUDIT, "
                   + "       CASE WHEN (U.NOMBRE IS NULL) THEN '---' "
                   + "          ELSE CONCAT(CONCAT(CONCAT(CONCAT(RTRIM(U.NOMBRE), ' '), RTRIM(U.APELLIDO_MAT)), ' '), RTRIM(U.APELLIDO)) " 
                   + "       END AS AUDITOR "
                   + "FROM BODBA.BO_BINS_PEDIDO B "
                   + "     JOIN BODBA.BO_RONDAS R ON R.ID_RONDA = B.ID_RONDA "
                   + "     JOIN BODBA.BO_DETALLE_PICKING DP ON DP.ID_BP = B.ID_BP "
                   + "     LEFT JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR = R.ID_SECTOR "
                   + "     LEFT JOIN BODBA.BO_USUARIOS U ON U.ID_USUARIO = R.ID_USUARIO_FISCAL "
                   + "     LEFT JOIN (SELECT  B2.ID_BP, B2.ID_PEDIDO, B2.ID_RONDA, B2.COD_BIN, SUM(DP2.CANT_PICK) AS CANT_PROD_AUDIT "
                   + "                FROM BODBA.BO_BINS_PEDIDO B2 "
                   + "                     JOIN BODBA.BO_RONDAS           R2 ON R2.ID_RONDA = B2.ID_RONDA "
                   + "                     JOIN BODBA.BO_DETALLE_PICKING DP2 ON DP2.ID_BP   = B2.ID_BP "
                   + "                WHERE DP2.AUDITADO = 'S' "
                   + "                GROUP BY B2.ID_BP, B2.ID_PEDIDO, B2.ID_RONDA, B2.COD_BIN) PA ON PA.ID_BP     = B.ID_BP "
                   + "                                                                            AND PA.ID_PEDIDO = B.ID_PEDIDO "
                   + "                                                                            AND PA.ID_RONDA  = B.ID_RONDA "
                   + "                                                                            AND PA.COD_BIN   = B.COD_BIN " 
                   + "WHERE 1=1 "
                   + idPedido
                   + idRonda
                   + "GROUP BY B.ID_BP, B.ID_PEDIDO, B.ID_RONDA, B.COD_BIN, B.VISUALIZADO, "
                   + "         B.TIPO, R.ID_SECTOR, S.NOMBRE, U.NOMBRE, U.APELLIDO_MAT, "
                   + "         U.APELLIDO, PA.CANT_PROD_AUDIT";
		logger.debug("SQL :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			while (rs.next()) {
				//BinsPedidoEntity bins =new BinsPedidoEntity();
				BinDTO bins = new BinDTO();
				bins.setId_bp(rs.getInt("ID_BP"));
				bins.setId_pedido(rs.getLong("ID_PEDIDO"));
				bins.setId_ronda(rs.getInt("ID_RONDA"));
				bins.setCod_bin(rs.getString("COD_BIN"));
				bins.setVisualizado(rs.getString("VISUALIZADO"));
				bins.setTipo(rs.getString("TIPO"));
				bins.setId_sector_picking(rs.getLong("ID_SECTOR"));
				if (rs.getString("SECTOR") == null){
				    bins.setNombre_sector_picking(Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
				}else{
				    bins.setNombre_sector_picking(rs.getString("SECTOR"));
				}
				
				bins.setCant_prod_audit(rs.getInt("CANT_PROD_AUDIT"));
				bins.setAuditor(rs.getString("AUDITOR"));
				list_bins.add(bins);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_bins;
	}
	
	/**
	 * Entrega listado de bins de acuerdo a un criterio
	 * 
	 * @param  criterio BinCriteriaDTO 
	 * @return List BinDTO
	 * @throws PedidosDAOException 
	 */
	public List getBinsByCriteriaPKL(BinCriteriaDTO criterio) throws PedidosDAOException {		
		PreparedStatement stm 	= null;			
		ResultSet rs 			= null;
		List list_bins 			= new ArrayList();
		
		long id_pedido	= 0;
		long id_ronda	= 0;
		String idPedido = "";
		String idPedido2= "";
		String idRonda  = "";
		String idRonda2 = "";
		
		id_pedido = criterio.getId_pedido();
		id_ronda  = criterio.getId_ronda();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getBinsPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getBinsPedido:");
		logger.debug("numero_pedido: " + id_pedido);
		logger.debug("numero_ronda : " + id_ronda);
		
		if ( id_pedido > 0 ){
			idPedido  = "B3.ID_PEDIDO = B.ID_PEDIDO ";
			idPedido2 = "  AND B.ID_PEDIDO = " + id_pedido + " ";
		}
		if ( id_ronda > 0 ){
			idRonda  = "B3.ID_RONDA = B.ID_RONDA ";
			idRonda2 = "  AND B.ID_RONDA = " + id_ronda + " ";
		}
		String Sql = "SELECT BB.*, SS.NOMBRE AS SECTOR "
                   + "FROM BODBA.BO_SECTOR SS " 
                   + "     INNER JOIN (SELECT B.ID_BP, B.ID_PEDIDO, B.ID_RONDA, "
                   + "                        B.COD_BIN, B.VISUALIZADO, B.TIPO, "
                   + "                        (SELECT DEP3.ID_SECTOR "
                   + "                         FROM BODBA.BO_BINS_PEDIDO B3 "
                   + "                              JOIN BODBA.BO_DETALLE_PICKING DP3  ON DP3.ID_BP       = B3.ID_BP "
                   + "                              JOIN BODBA.BO_DETALLE_PEDIDO  DEP3 ON DEP3.ID_DETALLE = DP3.ID_DETALLE "
                   + "                              JOIN BODBA.BO_SECTOR            S3 ON S3.ID_SECTOR    = DEP3.ID_SECTOR "
                   + "                         WHERE " + idPedido + idRonda
                   + "                           AND B3.ID_BP = B.ID_BP "
                   + "                         GROUP BY DEP3.ID_SECTOR "
                   + "                         ORDER BY MAX(S3.PRIORIDAD) DESC "
                   + "                         FETCH FIRST 1 ROWS ONLY "
                   + "                        ) AS ID_SECTOR "       
                   + "                 FROM BODBA.BO_BINS_PEDIDO B "
                   + "                      JOIN BODBA.BO_RONDAS            R ON R.ID_RONDA = B.ID_RONDA "
                   + "                      JOIN BODBA.BO_DETALLE_PICKING DP  ON DP.ID_BP   = B.ID_BP "
                   + "                      JOIN BODBA.BO_DETALLE_PEDIDO  DEP ON DEP.ID_DETALLE = DP.ID_DETALLE "
                   + "                 WHERE 1=1 " + idPedido2 + idRonda2
                   + "                 GROUP BY B.ID_BP, B.ID_PEDIDO, B.ID_RONDA, B.COD_BIN, B.VISUALIZADO, B.TIPO "
                   + "                 ORDER BY B.ID_BP, B.ID_PEDIDO, B.ID_RONDA, B.COD_BIN "
                   + "                ) BB ON SS.ID_SECTOR = BB.ID_SECTOR "
                   + "ORDER BY BB.ID_PEDIDO ASC, BB.ID_RONDA ASC, BB.ID_SECTOR ASC";
		logger.debug("SQL :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			while (rs.next()) {
				//BinsPedidoEntity bins =new BinsPedidoEntity();
				BinDTO bins = new BinDTO();
				bins.setId_bp(rs.getInt("ID_BP"));
				bins.setId_pedido(rs.getLong("ID_PEDIDO"));
				bins.setId_ronda(rs.getInt("ID_RONDA"));
				bins.setCod_bin(rs.getString("COD_BIN"));
				bins.setVisualizado(rs.getString("VISUALIZADO"));
				bins.setTipo(rs.getString("TIPO"));
				bins.setId_sector_picking(rs.getLong("ID_SECTOR"));
				if (rs.getString("SECTOR") == null){
				    bins.setNombre_sector_picking(Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
				}else{
				    bins.setNombre_sector_picking(rs.getString("SECTOR"));
				}
				
				//bins.setCant_prod_audit(rs.getInt("CANT_PROD_AUDIT"));
				//bins.setAuditor(rs.getString("AUDITOR"));
				list_bins.add(bins);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_bins;
	}
	
	
	/**
	 * Obtiene listado de sustitutos de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List SustitutoDTO
	 * @throws PedidosDAOException
	 */
	public List getSustitutosByPedidoId(long id_pedido) throws PedidosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getSustitutos");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getSustitutos:");
		logger.debug("numero_pedido:"+id_pedido);
		
		// Se modifica Query para obtener sustitutos de Tipo 3 (sustitutos reales y no por cambio de formato)
		
		String Sql = "SELECT DP.ID_DETALLE AS ID_DETALLE, DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION DESCR1, "
                   + "       DP.CANT_SOLIC AS CANT1, DP.OBSERVACION AS OBS1, DP.PRECIO AS PRECIO1, "
                   + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.COD_PROD1 END COD_PROD2, "
                   + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.UNI_MED END UNI_MED2, "
                   + "       PIK.DESCRIPCION DESCR2, PIK.CANT_PICK AS CANT2, COALESCE(PIK.PRECIO, 0) AS PRECIO2, "
                   + "       PIK.ID_DPICKING AS ID_DPICKING, "
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_PICKING PIK "
                   + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO      DP ON PIK.ID_DETALLE   = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S' "
                   + "     LEFT JOIN BODBA.BO_PRODUCTOS         PROD ON PROD.ID_PRODUCTO = PIK.ID_PRODUCTO "
                   + "     JOIN BODBA.BO_PEDIDOS                   P ON P.ID_PEDIDO      = DP.ID_PEDIDO "
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON SC.ID_CRITERIO   = DP.ID_CRITERIO "
                   + "WHERE P.ID_PEDIDO = ? AND P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") "
                   + "  AND INTEGER(DP.COD_PROD1) <> INTEGER(PROD.COD_PROD1) "
                   + "  AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
                   + "  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "
                   + "                                    FROM BODBA.BO_INF_SYF SF "
                   + "                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1 "
                   + "                                      AND SF.UME_ORIGINAL      = DP.UNI_MED "
                   + "                                      AND SF.COD_PROD_SUS      = PROD.COD_PROD1 "
                   + "                                      AND SF.UME_SUS           = PROD.UNI_MED)";
		

		
		logger.debug("SQL sustitutos :"+Sql);
		
		try {

			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				SustitutoDTO prod = new SustitutoDTO();
				prod.setId_detalle1(rs.getLong("ID_DETALLE"));
				prod.setId_detalle_pick1(rs.getLong("ID_DPICKING"));
				prod.setCod_prod1(rs.getString("COD_PROD1"));
				prod.setUni_med1(rs.getString("UNI_MED1"));
				prod.setCant1(rs.getDouble("CANT1"));
				prod.setDescr1(rs.getString("DESCR1"));
				prod.setObs1(rs.getString("OBS1"));
				prod.setPrecio1(rs.getDouble("PRECIO1"));
				prod.setCod_prod2(rs.getString("COD_PROD2"));
				prod.setUni_med2(rs.getString("UNI_MED2"));
				prod.setCant2(rs.getDouble("CANT2"));
				prod.setDescr2(rs.getString("DESCR2"));
				prod.setPrecio2(rs.getDouble("PRECIO2"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));             
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    prod.setDescCriterio(rs.getString("criterio"));
                }
                
				list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}
	
	
	/**
	 * Retorna la diferencia entre lo pickeado y lo pedido
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getFaltantesByPedidoId(long)
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoEntity
	 * @throws PedidosDAOException
	 */
	public List getFaltantesByPedidoId(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;

		
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getFaltantesPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getFaltantesPedido:");
		logger.debug("numero_pedido:"+id_pedido);
				
		//segun ultimo modelamiento, sera necesario un solo query
		String Sql = "SELECT DP.ID_DETALLE, S.NOMBRE NOM_SECTOR, DP.ID_SECTOR, DP.ID_PRODUCTO, " 
                   + "       DP.COD_PROD1, DP.DESCRIPCION DESCR, DP.CANT_SOLIC CANTIDAD, DP.CANT_FALTAN, " 
                   + "       DP.OBSERVACION OBS, DP.PREPARABLE, DP.CON_NOTA NOTA, DP.UNI_MED, DP.PRECIO, "
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                   + "     JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR = DP.ID_SECTOR "
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON DP.ID_CRITERIO = SC.ID_CRITERIO "
                   + "WHERE DP.ID_PEDIDO = ? " 
                   + "  AND DP.CANT_FALTAN > 0 "
                   + "ORDER BY DP.DESCRIPCION";
		logger.debug("SQL:"+Sql);
		try {
			
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoEntity prod =new ProductosPedidoEntity();				
				prod.setId_detalle(rs.getInt("ID_DETALLE"));
				prod.setId_producto(rs.getInt("ID_PRODUCTO"));
				prod.setCod_prod1(rs.getString("COD_PROD1"));
				prod.setId_sector(rs.getInt("ID_SECTOR"));
				prod.setNom_sector(rs.getString("NOM_SECTOR"));
				prod.setCant_solic(rs.getDouble("CANTIDAD"));
				prod.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				prod.setDescripcion(rs.getString("DESCR"));
				prod.setObservacion(rs.getString("OBS"));
				prod.setPreparable(rs.getString("PREPARABLE"));
				prod.setCon_nota(rs.getString("NOTA"));
				prod.setId_pedido((int)id_pedido);
				prod.setUni_med(rs.getString("UNI_MED"));
				prod.setPrecio(rs.getDouble("PRECIO"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));             
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    prod.setDescCriterio(rs.getString("criterio"));
                }
                
				list_prod.add(prod);
			}
						
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+list_prod.size());
		return list_prod;
	}

    //fvasquez ini
    public List productosSolicitados(long id_pedido)
            throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList listProductos = new ArrayList();

        String Sql = "SELECT CANT_SOLIC, PRECIO, CANT_FALTAN, UNI_MED FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_PEDIDO = ? ";
        this.logger.debug("SQL productosSolicitados :" + Sql);

        try {
            this.conn = this.getConnection();
            stm = this.conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();

            while (rs.next()) {
                HashMap productos = new HashMap();
                productos.put("CANT_SOLIC", rs.getString("CANT_SOLIC"));
                productos.put("PRECIO", rs.getString("PRECIO"));
                productos.put("CANT_FALTAN", rs.getString("CANT_FALTAN"));
                productos.put("UNI_MED", rs.getString("UNI_MED"));
                listProductos.add(productos);

            }
        } catch (SQLException var16) {
            throw new PedidosDAOException(String.valueOf(var16.getErrorCode()),
                    var16);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stm != null) {
                    stm.close();
                }

                this.releaseConnection();
            } catch (SQLException var15) {
                this.logger.error("[Metodo] : xxx - Problema SQL (close)",
                        var15);
            }
        }
        return listProductos;
    }

    public List productosFaltantes(long id_pedido)
            throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList listProductos = new ArrayList();

        String Sql = "SELECT DESCRIPCION, CANT_FALTAN, PRECIO, UNI_MED, (CANT_FALTAN * PRECIO) as TOTAL_PRECIO FROM bodba.BO_DETALLE_PEDIDO WHERE ID_PEDIDO = ? AND CANT_FALTAN > 0 ORDER BY 1 ";
        this.logger.debug("SQL productosFaltantes :" + Sql);

        try {
            this.conn = this.getConnection();
            stm = this.conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();

            while (rs.next()) {
                HashMap productos = new HashMap();
                productos.put("DESCRIPCION", rs.getString("DESCRIPCION"));
                productos.put("CANT_FALTAN", rs.getString("CANT_FALTAN"));
                productos.put("PRECIO", rs.getString("PRECIO"));
                productos.put("UNI_MED", rs.getString("UNI_MED"));
                productos.put("TOTAL_PRECIO", rs.getString("TOTAL_PRECIO"));
                listProductos.add(productos);
            }
        } catch (SQLException var16) {
            throw new PedidosDAOException(String.valueOf(var16.getErrorCode()),
                    var16);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stm != null) {
                    stm.close();
                }

                this.releaseConnection();
            } catch (SQLException var15) {
                this.logger.error("[Metodo] : xxx - Problema SQL (close)",
                        var15);
            }
        }
        return listProductos;
    }
	
    public List productosReemplazadosMenorValor(long id_pedido, boolean isActivaCorreccionAutomatica) throws PedidosDAOException {
    	
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rs_detalle = null;
        ResultSet rs_trx = null;
        ArrayList listProductos = new ArrayList();

        //String Sql = "SELECT dpe.DESCRIPCION AS DESCRIP1, dpe.CANT_SOLIC AS CANT_SOLIC, dpe.PRECIO AS PRECIO_SOLIC, DPK.DESCRIPCION AS DESCRIP2,DPK.CANT_PICK AS CANT_PICK, DPK.PRECIO AS PRECIO_PICK, DPE.UNI_MED AS UNI_MED,((dpe.CANT_SOLIC * dpe.PRECIO) - (DPK.CANT_PICK * DPK.PRECIO)) AS DIF_PRECIO FROM BODBA.BO_DETALLE_PICKING DPK INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DPK.ID_DETALLE = DPE.ID_DETALLE WHERE DPE.ID_PEDIDO = ? AND (DPE.PRECIO * DPE.CANT_SOLIC ) > (DPK.PRECIO * DPK.CANT_PICK) AND DPK.SUSTITUTO = 'S' UNION SELECT dpe.DESCRIPCION AS DESCRIP1, dpe.CANT_SOLIC AS CANT_SOLIC, dpe.PRECIO AS PRECIO_SOLIC, DPK.DESCRIPCION AS DESCRIP2,DPK.CANT_PICK AS CANT_PICK, DPK.PRECIO AS PRECIO_PICK, DPE.UNI_MED AS UNI_MED,((dpe.CANT_SOLIC * dpe.PRECIO) - (DPK.CANT_PICK * DPK.PRECIO)) AS DIF_PRECIO FROM BODBA.BO_DETALLE_PICKING DPK INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DPK.ID_DETALLE = DPE.ID_DETALLE WHERE DPE.ID_PEDIDO = ? AND (DPE.PRECIO * DPE.CANT_SOLIC ) = (DPK.PRECIO * DPK.CANT_PICK) AND DPK.SUSTITUTO = 'S' ";
        
        StringBuffer bufferSQL = new StringBuffer();
       /* bufferSQL.append("SELECT ");
        bufferSQL.append(" DPE.ID_DETALLE AS ID_DETALLE, DPE.DESCRIPCION AS DESCRIP1, DPE.CANT_SOLIC AS CANT_SOLIC, DPE.PRECIO AS PRECIO_SOLIC,");
        bufferSQL.append(" DTRX.DESCRIPCION AS DESCRIP2, DTRX.CANTIDAD AS CANT_PICK, DTRX.PRECIO AS PRECIO_PICK, DPE.UNI_MED AS UNI_MED,");
        bufferSQL.append(" ((DPE.CANT_SOLIC * DPE.PRECIO) - (DTRX.CANTIDAD * DTRX.PRECIO)) AS DIF_PRECIO");
        
		bufferSQL.append(" FROM BODBA.BO_TRX_MP_DETALLE DTRX");
		bufferSQL.append(" INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DTRX.ID_DETALLE = DPE.ID_DETALLE ");
		bufferSQL.append(" INNER JOIN BODBA.BO_DETALLE_PICKING DPK ON DPK.ID_DETALLE = DPE.ID_DETALLE AND DPK.SUSTITUTO = 'S' ");
		bufferSQL.append(" WHERE DPE.ID_PEDIDO = ? ");
		bufferSQL.append(" AND DTRX.ID_TRXMP in (SELECT ID_TRXMP FROM BO_TRX_MP WHERE ID_PEDIDO = ?) ");
		bufferSQL.append(" AND (DPE.PRECIO * DPE.CANT_SOLIC ) >= (DTRX.PRECIO * DTRX.CANTIDAD) ");
		bufferSQL.append(" AND DPE.CANT_FALTAN = 0 ");*/
        
        //Obtengo los ID_DETALLE sustitutos sin faltantes
        bufferSQL.append("SELECT ");
        bufferSQL.append(" ID_DETALLE FROM BODBA.BO_DETALLE_PEDIDO DPE WHERE DPE.ID_PEDIDO = ?  ");
       	bufferSQL.append("	AND DPE.CANT_FALTAN = 0  ");
       	bufferSQL.append(" 	AND DPE.ID_DETALLE in ");
       	bufferSQL.append(" 	(SELECT distinct ID_DETALLE FROM BODBA.BO_DETALLE_PICKING DPK WHERE DPK.ID_PEDIDO = ? AND DPK.SUSTITUTO = 'S') ");
        
        String sql = bufferSQL.toString();

        try {
            this.conn = this.getConnection();
            stm = this.conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, id_pedido);
            stm.setLong(2, id_pedido);
            rs = stm.executeQuery();

            ArrayList ids_detalle = new ArrayList(); 
            HashMap producto_detalle = new HashMap();
            HashMap producto_trx = new HashMap();
            
            while(rs.next()) {            	  
            	 ids_detalle.add(rs.getString("ID_DETALLE"));        
            	 
            	 String sql_detalle = "SELECT DESCRIPCION AS DESCRIP1, CANT_SOLIC, PRECIO AS PRECIO_SOLIC, UNI_MED, (PRECIO * CANT_SOLIC) as TOTAL_SOLICITADO FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE="+rs.getInt("ID_DETALLE")+" AND ID_PEDIDO = "+id_pedido;
            	 Statement stmt =  this.conn.createStatement();
                 rs_detalle = stmt.executeQuery(sql_detalle + " WITH UR");
                 
                 if(rs_detalle.next()) {
                	HashMap producto = new HashMap();
                	producto.put("DESCRIP1", rs_detalle.getString("DESCRIP1"));
                	producto.put("CANT_SOLIC", rs_detalle.getString("CANT_SOLIC"));
                	producto.put("PRECIO_SOLIC", rs_detalle.getString("PRECIO_SOLIC"));
                	producto.put("UNI_MED", rs_detalle.getString("UNI_MED"));
                	producto.put("TOTAL_SOLICITADO", rs_detalle.getString("TOTAL_SOLICITADO"));
                	producto_detalle.put(rs.getString("ID_DETALLE"), producto);
                 }
                 
                 String sql_trx = " SELECT ID_TRXDET, DESCRIPCION AS DESCRIP2, CANTIDAD AS CANT_PICK, PRECIO AS PRECIO_PICK," +
             	 		" (PRECIO * CANTIDAD) as TOTAL_PICKEADO FROM BODBA.BO_TRX_MP_DETALLE " +
             	 		" WHERE ID_PEDIDO= "+id_pedido+" AND ID_DETALLE = "+rs.getInt("ID_DETALLE")+" " +
             	 		" AND ID_TRXMP IN (SELECT ID_TRXMP FROM BO_TRX_MP WHERE ID_PEDIDO="+id_pedido+")";
                 
                 if(isActivaCorreccionAutomatica){
                	 sql_trx = "SELECT ID_DPICKING as ID_TRXDET, DESCRIPCION AS DESCRIP2, CANT_PICK, " +
                	 		" case  WHEN PRECIO_PICK is null THEN PRECIO " +
                	 		" ELSE PRECIO_PICK END AS PRECIO_PICK," +
                	 		" case WHEN PRECIO_PICK is null THEN (PRECIO * CANT_PICK)" +
                	 		" ELSE (PRECIO_PICK * CANT_PICK) END AS TOTAL_PICKEADO" +
                 	 		" FROM BO_DETALLE_PICKING" +
                	 		" WHERE ID_DETALLE =  "+rs.getInt("ID_DETALLE")+" AND ID_PEDIDO = "+id_pedido; 
                 }               	             	
            	 
            	 Statement stmt2 =  this.conn.createStatement();
                 rs_trx = stmt2.executeQuery(sql_trx + " WITH UR");
                 
                 HashMap producto_trx_list = new HashMap();
                 while(rs_trx.next()) {
                	HashMap producto2 = new HashMap();
                	producto2.put("DESCRIP2", rs_trx.getString("DESCRIP2"));
 	                producto2.put("CANT_PICK", rs_trx.getString("CANT_PICK"));
 	                producto2.put("PRECIO_PICK", rs_trx.getString("PRECIO_PICK"));
 	                producto2.put("TOTAL_PICKEADO", rs_trx.getString("TOTAL_PICKEADO"));
 	                producto_trx_list.put(rs_trx.getString("ID_TRXDET"), producto2);           	
                 }   
                 producto_trx.put(rs.getString("ID_DETALLE"), producto_trx_list);
            }
            
            Iterator it =ids_detalle.iterator();
            while(it.hasNext()){
            	String id_detalle = (String) it.next();
            	
            	HashMap solicitado = (HashMap) producto_detalle.get(id_detalle);
            	double total_solicitado = Double.parseDouble((String)solicitado.get("TOTAL_SOLICITADO"));  
            	
            	HashMap enviado_trx = (HashMap) producto_trx.get(id_detalle);
            	Iterator it_trx = (Iterator)enviado_trx.keySet().iterator();
            	
            	double total_pickeado =0;
            	double cant_pickeado =0;
            	String desc_pickeado ="";
            	while(it_trx.hasNext()){
            		String trxDet	 = (String)it_trx.next();
            		HashMap pickeado = (HashMap) enviado_trx.get(trxDet);
            		total_pickeado	+= Double.parseDouble((String)pickeado.get("TOTAL_PICKEADO"));
            		cant_pickeado	+= Double.parseDouble((String)pickeado.get("CANT_PICK"));
            		desc_pickeado	+= (String)pickeado.get("DESCRIP2")+", ";
            	}
            	
            	if(total_solicitado >= total_pickeado){
            		HashMap producto_menor_precio = new HashMap();
            		producto_menor_precio.put("DESCRIP1", solicitado.get("DESCRIP1"));
            		producto_menor_precio.put("CANT_SOLIC", solicitado.get("CANT_SOLIC"));
            		producto_menor_precio.put("PRECIO_SOLIC", solicitado.get("PRECIO_SOLIC"));
	                
            		producto_menor_precio.put("DESCRIP2", String.valueOf(desc_pickeado));
            		producto_menor_precio.put("CANT_PICK", String.valueOf(cant_pickeado));
            		producto_menor_precio.put("PRECIO_PICK", String.valueOf(total_pickeado/cant_pickeado));
	                
            		producto_menor_precio.put("UNI_MED", solicitado.get("UNI_MED"));	                
            		producto_menor_precio.put("DIF_PRECIO", String.valueOf(total_solicitado - total_pickeado));
	
	                listProductos.add(producto_menor_precio);            		
            	}
            	
            }           
            
        } catch (SQLException var16) {
            throw new PedidosDAOException(String.valueOf(var16.getErrorCode()), var16);
        } finally {
            try {
            	
            	if (rs_trx != null) {
            		rs_trx.close();
            	}
            	if (rs_detalle != null) {
            		rs_detalle.close();
            	}
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                this.releaseConnection();
            } catch (SQLException var15) {
                this.logger.error("[Metodo] : xxx - Problema SQL (close)", var15);
            }
        }
        return listProductos;
    }


    public List productoosReemplazadosMayorValor(long id_pedido)
            throws PedidosDAOException {
    	
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList listProductos = new ArrayList();

        String Sql = "SELECT dpe.DESCRIPCION AS DESCRIP1, dpe.CANT_SOLIC AS CANT_SOLIC, dpe.PRECIO AS PRECIO_SOLIC, DPK.DESCRIPCION AS DESCRIP2,DPK.CANT_PICK AS CANT_PICK, DPK.PRECIO AS PRECIO_PICK, DPE.UNI_MED AS UNI_MED,((DPK.CANT_PICK * DPK.PRECIO) - (dpe.CANT_SOLIC * dpe.PRECIO)) AS DIF_PREC_BENEF FROM BODBA.BO_DETALLE_PICKING DPK INNER JOIN BODBA.BO_DETALLE_PEDIDO DPE ON DPK.ID_DETALLE = DPE.ID_DETALLE WHERE DPE.ID_PEDIDO = ? AND (DPE.PRECIO * DPE.CANT_SOLIC ) < (DPK.PRECIO * DPK.CANT_PICK) AND DPK.SUSTITUTO = 'S' ORDER BY 1 ";
        this.logger.debug("SQL productoosReemplazadosMayorValor :" + Sql);

        try {
            this.conn = this.getConnection();
            stm = this.conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();

            while (rs.next()) {
                HashMap productos = new HashMap();
                productos.put("DESCRIP1", rs.getString("DESCRIP1"));
                productos.put("CANT_SOLIC", rs.getString("CANT_SOLIC"));
                productos.put("PRECIO_SOLIC", rs.getString("PRECIO_SOLIC"));
                productos.put("DESCRIP2", rs.getString("DESCRIP2"));
                productos.put("CANT_PICK", rs.getString("CANT_PICK"));
                productos.put("PRECIO_PICK", rs.getString("PRECIO_PICK"));
                productos.put("UNI_MED", rs.getString("UNI_MED"));
                productos.put("DIF_PREC_BENEF", rs.getString("DIF_PREC_BENEF"));
                listProductos.add(productos);
            }
        } catch (SQLException var16) {
            throw new PedidosDAOException(String.valueOf(var16.getErrorCode()),
                    var16);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                this.releaseConnection();
            } catch (SQLException var15) {
                this.logger.error("[Metodo] : xxx - Problema SQL (close)",
                        var15);
            }
        }
        return listProductos;
    }
    public List productosEnviadosXmail(long id_pedido)
            throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList listProductos = new ArrayList();

        String Sql = "SELECT  CANT_PICK AS CANT_ENVIADA, DESCRIPCION AS DESCRIP_ENV, PRECIO AS PRECIO_ENV, SUSTITUTO AS SUST FROM bodba.BO_DETALLE_PICKING WHERE id_pedido = ? ORDER BY 2 ";
        this.logger.debug("SQL productosEnviadosXmail :" + Sql);

        try {
            this.conn = this.getConnection();
            stm = this.conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();

            while (rs.next()) {
                HashMap productos = new HashMap();
                productos.put("CANT_ENVIADA", rs.getString("CANT_ENVIADA"));
                productos.put("DESCRIP_ENV", rs.getString("DESCRIP_ENV"));
                productos.put("PRECIO_ENV", rs.getString("PRECIO_ENV"));
                productos.put("SUST", rs.getString("SUST"));
                listProductos.add(productos);

            }
        } catch (SQLException var16) {
            throw new PedidosDAOException(String.valueOf(var16.getErrorCode()),
                    var16);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stm != null) {
                    stm.close();
                }

                this.releaseConnection();
            } catch (SQLException var15) {
                this.logger.error("[Metodo] : xxx - Problema SQL (close)",
                        var15);
            }
        }
        return listProductos;
    }
//fvasquez fin
	
	
	/** 
	 * Rescate de productos de un pedido especifico en base a un bin
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getProductosBin(long, long)
	 * 
	 * @param  id_pedido long 
	 * @param  cod_bin long 
	 * @return List ProductosPedidoEntity
	 * @throws PedidosDAOException
	 */
	public List getProductosBin(long id_pedido, long cod_bin) throws PedidosDAOException {
		PreparedStatement stm =null;
		ResultSet rs=null;
		
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getFaltantesPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getFaltantesPedido:");
		logger.debug("numero_pedido:"+id_pedido);
		
		String Sql = "SELECT dp.id_detalle," +
				" s.nombre nom_sector, "+
				" dp.id_sector, " +
				" dp.id_producto, " +
				" dp.descripcion descr, " +
				" dp.cant_solic cantidad, " +
				" dp.observacion obs, " +
				" dp.preparable, "+
				" dp.con_nota nota" +
				" FROM BO_DETALLE_PEDIDO " +
				" JOIN BO_SECTOR s ON s.id_sector=dp.id_sector " +
				" JOIN BO_DETALLE_PICKING pik.id_producto = dp.id_producto " +
				" 					AND  pik.id_bp = ? " +
				"					AND  pik.id_pedido = dp.id_pedido " +				
				" WHERE dp.id_pedido = ? ";
		
		logger.debug("SQL con criterio :"+Sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, cod_bin);
			stm.setLong(2, id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoEntity prod =new ProductosPedidoEntity();				
				prod.setId_detalle(rs.getInt("id_detalle"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setId_sector(rs.getInt("id_sector"));
				prod.setNom_sector(rs.getString("nom_sector"));
				prod.setCant_solic(rs.getDouble("cantidad"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setObservacion(rs.getString("obs"));
				prod.setPreparable(rs.getString("preparable"));
				prod.setCon_nota(rs.getString("nota"));
				prod.setId_pedido((int)id_pedido);

				list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}
	
	
	/**
	 * Obtiene listado de productos pickeados de un pedido de acuerdo a un criterio
	 * 
	 * @param  criterio PickingCriteriaDTO 
	 * @return List ProductosBinDTO
	 * @throws PedidosDAOException
	 */
	public List getPickingPedidoByCriteria(PickingCriteriaDTO criterio)
		throws PedidosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List result = new ArrayList();

		long id_bp 			= 0;
		long id_detalle 	= 0;
		long id_dpicking	= 0;
		
		id_bp 		= criterio.getId_bp();
		id_detalle 	= criterio.getId_detalle();
		id_dpicking	= criterio.getId_dpicking();
		
		
		logger.debug("Ejecutando DAO getPickingPedidoByCriteria");
		
		// SQL principal
		String SQLStmt = "SELECT DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, "
                       + "       DP.ID_PRODUCTO, DP.ID_PEDIDO, DP.CBARRA, "
                       + "       DP.DESCRIPCION, DP.CANT_PICK, DP.SUSTITUTO, "
                       + "       D.COD_PROD1, D.UNI_MED, DP.AUDITADO "
                       + "FROM BO_DETALLE_PICKING DP "
                       + "     JOIN BO_DETALLE_PEDIDO D ON D.ID_DETALLE = DP.ID_DETALLE "
                       + "WHERE 1=1 ";	
		
		// Filtro id_bp
		if (id_bp > 0){
			SQLStmt += " AND DP.ID_BP = " + id_bp + " ";
		}

		// Filtro id_detalle
		if (id_detalle > 0){
			SQLStmt += " AND DP.ID_DETALLE = " + id_detalle + " ";
		}		

		// Filtro id_detalle
		if (id_dpicking > 0){
			SQLStmt += " AND DP.ID_DPICKING = " + id_dpicking + " ";
		}
		SQLStmt += "ORDER BY DP.DESCRIPCION ASC";
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );				
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosBinDTO prod1 = new ProductosBinDTO();
				prod1.setCantidad(rs.getDouble("CANT_PICK"));
				prod1.setCod_producto(rs.getString("ID_PRODUCTO"));
				prod1.setDescripcion(rs.getString("DESCRIPCION"));
				prod1.setId_bin(rs.getInt("ID_BP"));
				prod1.setCbarra(rs.getString("CBARRA"));
				prod1.setCod_prod_sap(rs.getString("COD_PROD1"));
				prod1.setUni_med(rs.getString("UNI_MED"));
				prod1.setAuditado(rs.getString("AUDITADO"));
				result.add(prod1);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	
	}

	/**
	 * Obtiene id del sector, segun producto y local.
	 * 
	 * @param  id_prod long 
	 * @return long, id
	 * @throws PedidosDAOException
	 */
	public long getSectorByProdId(long id_prod) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		long res = -1;
		
		logger.debug("en getSectorByProdId:");
		
		//leer los datos de la tabla bo_prod_sector
		String Sql = "SELECT ps.id_sector "
		           + "FROM bo_prod_sector ps "
                   + "WHERE ps.id_producto = ?";
		
		logger.debug("SQL :"+Sql);
		logger.debug("id_prod:"+id_prod);
	
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_prod);

			rs = stm.executeQuery();
			if (rs.next()) {
				res = rs.getLong("id_sector");
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return res;
	}
	
	public long agregaClienteMaestraBolsa(ProductosPedidoDTO prod) throws PedidosDAOException {
		long id_det = 0;
		PreparedStatement 	stm 	= null;
		ResultSet rs = null;
		
		String sql =
			"INSERT INTO bo_detalle_pedido (id_pedido, id_sector, id_producto, cod_prod1, uni_med, " +
			" descripcion, cant_solic, observacion, precio, cant_spick, pesable, preparable,con_nota, tipo_sel," +
			" precio_lista, dscto_item, id_criterio, desc_criterio) "+
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		try {
			
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, prod.getId_pedido());
			
			if(prod.getId_sector()>0){
				stm.setLong(2, prod.getId_sector());
			}else{
				stm.setNull(2,java.sql.Types.INTEGER);
			}
			
			stm.setLong(3, prod.getId_producto());
			stm.setString(4,prod.getCod_producto());
			stm.setString(5,prod.getUnid_medida());
			stm.setString(6,prod.getDescripcion());
			stm.setDouble(7,prod.getCant_solic());
			stm.setString(8,prod.getObservacion());
			stm.setDouble(9,prod.getPrecio());
			stm.setDouble(10,prod.getCant_spick());
			stm.setString(11,prod.getPesable());
			stm.setString(12,prod.getPreparable());
			stm.setString(13,prod.getCon_nota());
			stm.setString(14,prod.getTipoSel());
			stm.setDouble(15,prod.getPrecio_lista());
			stm.setDouble(16,prod.getDscto_item());
            if (prod.getIdCriterio() == 0) {
                stm.setLong(17, 1);
            } else {
                stm.setLong(17,prod.getIdCriterio());
            }
            stm.setString(18,prod.getDescCriterio());
            
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
			
			if (rs.next())
				id_det = rs.getInt(1);
			

		}catch (SQLException sqle) {
			logger.error("SQL1 EXCEPTION ="+sqle);
			sqle.printStackTrace();
			throw new PedidosDAOException(String.valueOf(sqle.getErrorCode()),sqle);
		}catch(Exception e){
			logger.error("SQL2 EXCEPTION ="+e);
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getCause()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return id_det;
	}
		
	
	/**
	 * Verifica si un producto se encuentra ya en el pedido
	 * 
	 * @param  prod ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si se agrego el producto, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean verificaProductoEnPedido(ProductosPedidoDTO prod) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean existe =false;
		String sql = "SELECT DP.ID_PEDIDO, DP.ID_DETALLE, DP.COD_PROD1 "
                   + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                   + "WHERE DP.COD_PROD1 = '" + prod.getCod_producto() + "' "
                   + "  AND DP.UNI_MED = '" + prod.getUnid_medida() + "' "
                   + "  AND DP.ID_PEDIDO = " + prod.getId_pedido();
		
		logger.debug("en verificaProductoEnPedido");
		logger.debug("SQL: " + sql);
        
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()){
			    existe = true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return existe;
	}

	//actualizaCantidadProductoPedido
	/**
	 * Actualiza la Cantidad de un Producto existente en el Pedido
	 * 
	 * @param  prod ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si se agrego el producto, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean actualizaCantidadProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException {
		PreparedStatement stm = null;
		boolean result =false;
		String sql = "UPDATE BODBA.BO_DETALLE_PEDIDO DP " +
                     "SET CANT_SOLIC  = " + prod.getCant_solic() + "," +
                     "    CANT_SPICK  = " + prod.getCant_spick() + "," +
                     "    OBSERVACION = '" + prod.getObservacion() + "'," +
                     "    ID_CRITERIO = " + prod.getIdCriterio() + "," +
                     "    DESC_CRITERIO = '" + prod.getDescCriterio() + "' " +
                     "WHERE DP.COD_PROD1 = '" + prod.getCod_producto() + "' " + 
                     "  AND DP.UNI_MED   = '" + prod.getUnid_medida() + "'" +
                     "  AND DP.ID_PEDIDO = " + prod.getId_pedido();
		
		logger.debug("en verificaProductoEnPedido");
		logger.debug("SQL: " + sql);
        
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			int NumReg = stm.executeUpdate();
			if(NumReg>0){
			    result = true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	
	
	
	public void descuentaStockBolsa(String cod_bolsa, String cod_sucursal) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		String sql = " UPDATE BODBA.BO_stock_BOLSAS " +
					 " SET STOCK = (STOCK - 1) " +
                     " WHERE COD_BOLSA = ? " + 
                     " AND COD_SUCURSAL = ? ";
		
		logger.debug("en descuentaStockBolsa");
		logger.debug("SQL: " + sql);

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
		
			stm.setString(1, cod_bolsa);
			stm.setString(2, cod_sucursal);
			
			stm.executeUpdate();

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}
	
	
	
	
	/**
	 * Elimina el detalle de un pedido
	 * 
	 * @param  prod ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si se eliminó el detalle, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean elimProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en elimProductoPedido");
			String sql = "DELETE FROM  bo_detalle_pedido " +
			" WHERE id_detalle = ? ";
			logger.debug(sql);
			logger.debug("vals:"+prod.getId_detalle());
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prod.getId_detalle());
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/**
	 * Asigna un pedido a un usuario.
	 * 
	 * @param  col AsignaOPDTO 
	 * @return boolean, devuelve <i>true</i> si la asignación fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setAsignaOP(AsignaOPDTO col) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm0=null;
		PreparedStatement stm=null;
		PreparedStatement stm2=null;
		ResultSet rs =null;
		
		long id_pedido_usuario = -1L;
		try {

			conn = this.getConnection();
			//debe consultar si el usuario ya tiene una op tomada
			String sql0 = "SELECT COALESCE(id_pedido,0) pedido " +
					" FROM BO_USUARIOS " +
					" WHERE id_usuario = ? ";
			
			logger.debug("Sql0:"+sql0);
			logger.debug("id_usuario:"+col.getId_usuario());
			stm0 = conn.prepareStatement(sql0 + " WITH UR");
			stm0.setLong(1, col.getId_usuario());
			rs = stm0.executeQuery();
			
			if(rs.next()){
				id_pedido_usuario = rs.getLong("pedido");
			}
			if (id_pedido_usuario>0){
				logger.debug("El usuario ya tiene tomada la OP:"+id_pedido_usuario+" solo puede tomar una OP a la vez");
				return false;
			}
			
			// si no la tiene se puede asignar
			
			logger.debug("en setAsignaOP");
			String sql = "UPDATE BO_PEDIDOS SET id_usuario = ? WHERE id_pedido = ?";
			logger.debug(sql);
			logger.debug("id_usuario:"+col.getId_usuario());
			logger.debug("id_pedido:"+col.getId_pedido());
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, col.getId_usuario());
			stm.setLong(2, col.getId_pedido());

			int i = stm.executeUpdate();
			if(i<=0) { //nro lineas actualizadas 0 o menos
				return false;
			}
			String sql2 = "UPDATE BO_USUARIOS SET id_pedido = ? WHERE id_usuario = ? ";
			logger.debug(sql2);
			logger.debug("id_usuario:"+col.getId_usuario());
			logger.debug("id_pedido:"+col.getId_pedido());
			
			stm2 = conn.prepareStatement(sql2);
			stm2.setLong(1, col.getId_pedido());
			stm2.setLong(2, col.getId_usuario());
			int j = stm2.executeUpdate();
			if (j>0)  
				result = true;
			else	  
				result = false;
			

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm0 != null)
					stm0.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	/**
	 * Libera un pedido, actualiza información del pedido y usuario.
	 * 
	 * @param  col AsignaOPDTO 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setLiberaOP(AsignaOPDTO col) throws PedidosDAOException {
		PreparedStatement stm = null;
		PreparedStatement stm2 =null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("en setLiberaOP");
			String sql = "UPDATE BO_PEDIDOS SET id_usuario = NULL, id_mot = ? " +
						 " WHERE id_pedido = ? AND id_usuario = ?";
			logger.debug(sql);
			logger.debug("id_usuario:"+col.getId_usuario());
			logger.debug("id_pedido:"+col.getId_pedido());
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, col.getId_motivo());
			stm.setLong(2, col.getId_pedido());
			stm.setLong(3, col.getId_usuario());

			int i = stm.executeUpdate();
			if(i<=0) {	
				return false;
			}
			String sql2 = "UPDATE BO_USUARIOS SET id_pedido = NULL WHERE id_usuario = ? ";
			stm2 = conn.prepareStatement(sql2);
			stm2.setLong(1, col.getId_usuario());
			int j = stm2.executeUpdate();
			if (j>0)  
				result = true;
			else	  
				result = false;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm2 != null)
					stm2.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Actualiza las jornadas de picking y de despacho al pedido
	 * 
	 * @param  id_pedido long 
	 * @param  id_jpicking long 
	 * @param  id_jdespacho long 
	 * @param  costo_despacho int 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean doActualizaPedidoJornadas(long id_pedido, long id_jpicking, long id_jdespacho, int costo_despacho, boolean modificarJPicking, boolean modificarPrecio)
		throws PedidosDAOException {		
		PreparedStatement stm=null;
		boolean result=false;
		
		String sql =  " UPDATE bo_pedidos  " +
                      " SET id_jdespacho = ? ";
        if ( modificarJPicking ) {
            sql += " ,id_jpicking = ? ";
        }
        if ( modificarPrecio ) {
            sql += " ,costo_despacho = ? ";
        }
        sql += " WHERE id_pedido = ?";
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( sql );
            int x = 1;
            stm.setLong(x, id_jdespacho);
            x++;            
            if ( modificarJPicking ) {
                if (id_jpicking == 0) {
                    stm.setNull(x, java.sql.Types.INTEGER );
                } else {
                    stm.setLong(x, id_jpicking);
                }
                x++;
            }            
            if ( modificarPrecio ) {
    			stm.setDouble(x, costo_despacho);
                x++;
            }
	        stm.setLong(x, id_pedido);
        	
			int i = stm.executeUpdate();
			if ( i > 0 ) {
				result= true;
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Actualiza el costo de despacho de un pedido
	 * 
	 * @param  id_pedido long 
	 * @param  costo_despacho int 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean doActualizaCostoDespacho(long id_pedido, int costo_despacho)
		throws PedidosDAOException {		
		PreparedStatement stm=null;
		boolean result=false;
		
		String SQLStmt = 
				" UPDATE bo_pedidos  " +
				" SET costo_despacho = ?" +
				" WHERE id_pedido = ?"
				;
		
		logger.debug("Ejecución DAO doActualizaCostoDespacho");
		logger.debug("SQL           : " + SQLStmt);
		logger.debug("id_pedido     : " + id_pedido);
		logger.debug("costo_despacho: " + costo_despacho);
		
		try {

			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setDouble(1, costo_despacho);
			stm.setLong(2, id_pedido);
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
		return result;
	}

	
	
	/**
	 * Actualiza medio de pago de un  pedido
	 * 
	 * @param  mp DatosMedioPagoDTO 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setCambiarMedio_pago(DatosMedioPagoDTO mp) throws PedidosDAOException {		
			PreparedStatement stm=null;
			String SqlTPT = "";
			String SqlTBK = "";
			String num_tc = "";
			boolean result = false;
			 
			logger.debug("Dao setCambiarMedio_pago");
			logger.debug("mp.getMedio_pago: "+mp.getMedio_pago());
			//logger.debug("mp.getNum_mp: "+mp.getNum_mp());	
			//logger.debug("mp.getFecha_expiracion()"+mp.getFecha_expiracion());
			logger.debug("mp.getBanco()"+mp.getBanco());
			logger.debug("mp.getCuotas()"+mp.getCuotas());
			logger.debug("mp.getId_pedido()"+mp.getId_pedido());
			//logger.debug("mp.getNom_tban()"+mp.getNom_tban());

			try{
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                String key = rb.getString("conf.bo.key");
                num_tc = Cifrador.desencriptar(key, mp.getNum_mp());
			}catch(Exception e){
			    e.printStackTrace();
			}
			if (mp.getMedio_pago().equalsIgnoreCase("CRE")){
				if (mp.getNum_mp().trim().equals("")){
					SqlTBK = ", num_mp = ''";
				}
				if (mp.getCuotas().trim().equals("")){
					SqlTBK +=", n_cuotas = 0";
				}
				if (mp.getNom_tban() != null && !mp.getNom_tban().trim().equals("")){
					SqlTBK +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}			    
			}else if (mp.getMedio_pago().equalsIgnoreCase("TBK")){
				if (!mp.getNum_mp().trim().equals("")){
					SqlTBK = ", num_mp = '"+mp.getNum_mp()+"'";
				}
				if (!mp.getFecha_expiracion().trim().equals("")){
					SqlTBK += ", fecha_exp = '"+mp.getFecha_expiracion()+"'";
				}
				if (!mp.getBanco().trim().equals("")){
					SqlTBK +=", tb_banco = '"+mp.getBanco()+"'";
				}
				if (!mp.getCuotas().trim().equals("")){
					SqlTBK +=", n_cuotas = "+mp.getCuotas()+"";
				}
				if (mp.getNom_tban() != null && !mp.getNom_tban().trim().equals("")){
					SqlTBK +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}
				//SqlTBK += ", meses_librpago  = NULL";
				//SqlTBK +=" nom_tbancaria = '"+mp.getNom_tban()+"',";
			}else if(mp.getMedio_pago().equalsIgnoreCase("CAT")){
				if (!mp.getNum_mp().trim().equals("")){
					SqlTPT = ", num_mp = '"+mp.getNum_mp()+"'";
				}
				if (!mp.getNom_tban().trim().equals("")){
					SqlTPT +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}
                //MEDIO DE PAGO ALTERNATIVO - CHEQUE
			    if (num_tc.equals("1111111111111111")){
		            SqlTPT +=", n_cuotas = 0";
			    }else{
					if (!mp.getCuotas().trim().equals("")){
				    	SqlTPT +=", n_cuotas = "+mp.getCuotas()+"";
				    }
			    }
				//SqlTPT += ", meses_librpago  = NULL";
				
                //PARIS
			}else if(mp.getMedio_pago().equalsIgnoreCase("PAR") && mp.getNom_tban().equals("")){
				if (!mp.getNum_mp().trim().equals("")){
					SqlTPT = ", num_mp = '"+mp.getNum_mp()+"'";
				}
				if (!mp.getCuotas().trim().equals("")){
			    	SqlTPT +=", n_cuotas = "+mp.getCuotas()+"";
			    }
				if (!mp.getNom_tban().trim().equals("")){
					SqlTPT +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}
				/*if (!mp.getMeses_LibrePago().trim().equals("")){
					SqlTPT += ", meses_librpago = "+mp.getMeses_LibrePago()+" ";
				}*/

				//PARIS TITULAR
			}else if(mp.getMedio_pago().equalsIgnoreCase("PAR") && mp.getNom_tban().equals("PARIS TITULAR")){
				if (!mp.getNum_mp().trim().equals("")){
					SqlTPT = ", num_mp = '"+mp.getNum_mp()+"'";
				}
				if (!mp.getCuotas().trim().equals("")){
			    	SqlTPT +=", n_cuotas = "+mp.getCuotas()+"";
			    }
				if (!mp.getNom_tban().trim().equals("")){
					SqlTPT +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}
				/*if (!mp.getMeses_LibrePago().trim().equals("")){
					SqlTPT += ", meses_librpago = "+mp.getMeses_LibrePago()+" ";
				}*/
				
				//PARIS ADICIONAL
			}else if(mp.getMedio_pago().equalsIgnoreCase("PAR") && mp.getNom_tban().equals("PARIS ADICIONAL")){
				if (!mp.getNum_mp().trim().equals("")){
					SqlTPT = ", num_mp = '"+mp.getNum_mp()+"'";
				}
				if (!mp.getCuotas().trim().equals("")){
			    	SqlTPT +=", n_cuotas = "+mp.getCuotas()+"";
			    }
				if (!mp.getNom_tban().trim().equals("")){
					SqlTPT +=", nom_tbancaria = '"+mp.getNom_tban()+"'";
				}
				/*if (!mp.getMeses_LibrePago().trim().equals("")){
					SqlTPT += ", meses_librpago = "+mp.getMeses_LibrePago()+" ";
				}*/
				
				/*****************************************************************/
				if (!mp.getRut_tit().trim().equals("")){
					SqlTPT +=", RUT_TIT = '"+mp.getRut_tit()+"'";
				}
				if (!mp.getDv_tit().trim().equals("")){
					SqlTPT +=", DV_TIT = '"+mp.getDv_tit()+"'";
				}
				if (!mp.getNombre_tit().trim().equals("")){
					SqlTPT +=", NOM_TIT = '"+mp.getNombre_tit()+"'";
				}
				if (!mp.getAppaterno_tit().trim().equals("")){
					SqlTPT +=", APAT_TIT = '"+mp.getAppaterno_tit()+"'";
				}
				if (!mp.getApmaterno_tit().trim().equals("")){
					SqlTPT +=", AMAT_TIT = '"+mp.getApmaterno_tit()+"'";
				}
				if (!mp.getDireccion_tit().trim().equals("")){
					SqlTPT +=", DIR_TIT = '"+mp.getDireccion_tit()+"'";
				}
				if (!mp.getNumero_tit().trim().equals("")){
					SqlTPT +=", DIR_NUM_TIT = '"+mp.getNumero_tit()+"'";
				}
			}
			
			if (!mp.getNom_tban().equals("PARIS ADICIONAL")){
				SqlTPT += ", RUT_TIT     = NULL";
				SqlTPT += ", DV_TIT      = NULL";
				SqlTPT += ", NOM_TIT     = NULL";
				SqlTPT += ", APAT_TIT    = NULL";
				SqlTPT += ", AMAT_TIT    = NULL";
				SqlTPT += ", DIR_TIT     = NULL";
				SqlTPT += ", DIR_NUM_TIT = NULL";
			}
			if (!mp.getMedio_pago().equalsIgnoreCase("TBK")){
				SqlTPT += ", fecha_exp = NULL";
				SqlTPT += ", tb_banco  = NULL";
			}
			String SQLStmt = 
				" UPDATE bo_pedidos " +
				" SET medio_pago = ? " +			
				SqlTPT +
				SqlTBK +
				" WHERE id_pedido = ?";
			logger.debug("Sql : " + SQLStmt);
			try {
				conn = this.getConnection();
				stm = conn.prepareStatement( SQLStmt );
				
				if (mp.getMedio_pago().equalsIgnoreCase("CAT") || 
				        mp.getMedio_pago().equalsIgnoreCase("TBK") ||
				            mp.getMedio_pago().equalsIgnoreCase("CRE")){
					stm.setString(1, mp.getMedio_pago());
			    }else{
			    	stm.setString(1, "PAR");
			    }
				stm.setLong(2, mp.getId_pedido());
				
				int i = stm.executeUpdate();
				logger.debug("Resultado Ejecución: " + i);
				if(i>0){
					result = true;
				}
				

			}catch (SQLException e) {
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			} finally {
				try {
					if (stm != null)
						stm.close();
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}
			return result;
	}
	
	
	/**
	 * Borra las alertas de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si la eliminación fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean elimAlertaByPedido(long id_pedido) throws PedidosDAOException {
		
		PreparedStatement stm =null;
		boolean result = false;
		
		logger.debug("en elimAlertaByPedido");
		String sql = "DELETE FROM bo_alerta_op WHERE id_pedido = ? ";
		logger.debug(sql);
		logger.debug("id_pedido: "+id_pedido);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
		
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_pedido);

			int i = stm.executeUpdate();
			logger.debug("elimino:"+i);
			
			result = true;
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	
	/**
	 * Borra las alertas de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si la eliminación fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean elimAlertaByPedidoFromMPago(long id_pedido) throws PedidosDAOException {
		
		PreparedStatement stm =null;
		boolean result = false;
		
		logger.debug("en elimAlertaByPedidoFromMPago");
		String sql = "DELETE FROM bo_alerta_op WHERE id_pedido= ? AND id_alerta between 16 and 24 ";
		logger.debug(sql);
		logger.debug("id_pedido: "+id_pedido);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
		
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_pedido);

			int i = stm.executeUpdate();
			logger.debug("elimino: "+i);
			
			result = true;
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	
	
	
	//
	
	/**
	 * Agrega alerta a un pedido
	 * 
	 * @param  id_pedido long 
	 * @param  id_alerta long 
	 * @return boolean, devuelve <i>true</i> si la inserción fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean addAlertToPedido(long id_pedido, long id_alerta) throws PedidosDAOException {
		PreparedStatement stm=null;
		boolean result = false;

		try {
			conn = this.getConnection(); 
			logger.debug("en addAlertToPedido");
			String sql = "INSERT INTO bo_alerta_op (id_alerta, id_pedido) VALUES (?,?) ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("id_alerta:"+id_alerta);
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_alerta);
			stm.setLong(2, id_pedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			} if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Modifica el estado de un pedido
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		boolean result = false;
		try {
			conn = this.getConnection();
			logger.debug("en setModEstadoPedido");
			String sql = "UPDATE  bo_pedidos SET id_estado = ? WHERE id_pedido = ? ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("id_estado:"+id_estado);
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_estado);
			stm.setLong(2, id_pedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	/**(+) INDRA 2012-12-12 (+)
	 * Modifica el detalle de un pedido cuando se repickea
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean setModDetallePedido(long id_pedido, double cantidad) throws PedidosDAOException {

		PreparedStatement stm=null;
		boolean result = false;
		try {
			conn = this.getConnection();
			logger.debug("en setModDetallePedido");
			String sql = " UPDATE BO_DETALLE_PEDIDO PED SET PED.CANT_FALTAN = 0.00, PED.CANT_SPICK = ? WHERE PED.ID_PEDIDO =  ? ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("cantidad:"+cantidad);
	
			stm = conn.prepareStatement(sql);

			stm.setDouble(1, cantidad);
			stm.setLong(2, id_pedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
//(-) INDRA (-)
	
	/**
	 * Modifica el estado de una TrxMp
	 * 
	 * @param  id_trx_mp long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean setModEstadoTrxMp(long id_trx_mp, long id_estado) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		boolean result = false;
		try {
			conn = this.getConnection();
			logger.debug("en setModEstadoTrxMp");
			String sql = "UPDATE  bo_trx_mp "
			           + "   SET id_estado = " + id_estado + " "
			           + "WHERE  id_trxmp  = " + id_trx_mp;
			logger.debug(sql);
			logger.debug("id_trx_mp:"+id_trx_mp);
			logger.debug("id_estado:"+id_estado);
			
			stm = conn.prepareStatement(sql);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Verifica si un pedido tiene alerta activa
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si existe alerta activa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean getExisteAlertaActiva(long id_pedido)  throws PedidosDAOException {
		PreparedStatement stm=null;
		ResultSet rs =null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en getExisteAlertaActiva");
			String sql = "SELECT id_pedido FROM bo_alerta_op AO, bo_alertas A " +
					" WHERE AO.id_alerta = A.id_alerta AND A.tipo = 'A' AND AO.id_pedido=? ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/**
	 * Actualiza la dirección de un pedido
	 * 
	 * @param  id_pedido  long el pedido a actualizar
	 * @param  dir DireccionEntity la dirección
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean setModPedidoDir(DireccionesDTO dir, PedidoDTO pedido) throws PedidosDAOException {

		PreparedStatement stm=null;
		boolean result = false;
		
		logger.debug("DAO setModPedidoDir");
		String sql =
				"UPDATE bo_pedidos SET " +
					"  dir_id         = " + dir.getId_dir() +
					", dir_tipo_calle ='" + dir.getTipoCalle().getNombre()+"' " +
					", dir_calle      ='" + dir.getCalle()+"' " +
					", dir_numero     ='" + dir.getNumero()+"' " +
					", dir_depto      ='" + dir.getDepto()+"' " +
					", indicacion     ='" + dir.getComentarios()+"' " +
					", id_zona        = " + dir.getId_zon() +
					", id_local       = " + dir.getId_loc() +
					", id_comuna      = " + dir.getId_com();
		if (pedido.getOrigen().equals("W")){
		     sql += ", ID_LOCAL_FACT  = " + dir.getId_loc(); 
		}
		sql += " WHERE id_pedido = ? ";
		
		logger.debug("SQL: " + sql);
		logger.debug("id_pedido: " 	+ pedido.getId_pedido());
		logger.debug("id_dir: "		+ dir.getId_dir());

		try {
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, pedido.getId_pedido() );
			
			int i = stm.executeUpdate();
			if (i>0) {
				result = true;
			}
			logger.debug("rc: " + result);
			
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result? "+result);
		return result;
	}
	
	
	/**
	 * Elimina la alerta de un pedido
	 * 
	 * @param  id_pedido long  
	 * @param  id_alerta int 
	 * @return boolean, devuelve <i>true</i> si la eliminación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean elimAlertaPedidoById(long id_pedido,int id_alerta) throws PedidosDAOException {
		
		
		PreparedStatement stm=null;
		boolean result = false;
		try {

			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en elimAlertaPedidoById");
			String sql = "DELETE FROM  bo_alerta_op " +
			" WHERE id_pedido = ? AND id_alerta = ? ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("id_alerta:"+id_alerta);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_pedido);
			stm.setLong(2, id_alerta);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
		
	}
	
	
	/**
	 * Modifica notas del pedido (indicación y observación)
	 * 
	 * @param  prm ProcModPedidoIndicDTO
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean setModPedidoIndic(ProcModPedidoIndicDTO prm) throws PedidosDAOException {

		PreparedStatement 	stm		= null;
		boolean 			result 	= false;
		
		logger.debug("en setModPedidoDir");
		String sql = 
			" UPDATE bo_pedidos " 	+
			" SET indicacion = ? "	+
			" , observacion = ? "	+
			" WHERE id_pedido = ? "
			;
		
		logger.debug(sql);
		logger.debug("id_pedido:"+prm.getId_pedido());
		logger.debug("indicacion:"+prm.getIndicacion());
		logger.debug("obs:"+prm.getObservacion());
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(sql);
			stm.setString(1, prm.getIndicacion());
			stm.setString(2, prm.getObservacion());
			stm.setLong(3, prm.getId_pedido());
			
			int i = stm.executeUpdate();
			if (i>0) {
				result = true;
			}

		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result?"+result);
		return result;
	}
	
	
	/**
	 * Obtiene lista de direcciones del cliente
	 * 
	 * @param  cliente_id long 
	 * @return List DireccionEntity
	 * @throws PedidosDAOException
	 */
	/*public List listadoDirecciones(long cliente_id) throws PedidosDAOException {
		List lista_dirs = new ArrayList();
		DireccionEntity dir = null;
		TipoCalleEntity tip = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		long cli_id = cliente_id;

		try {

			logger.debug("en listadoDirecciones");
			String sql = "SELECT dir_id, dir_cli_id, dir_loc_id, dir_com_id, dir_tip_id, dir_zona_id, dir_alias, " +
					" dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fec_crea, dir_fnueva, " +
					" dir_cuadrante, nom_local, C.nombre nom_com, Z.nombre nom_zon, tip_id, tip_nombre, tip_estado " +
					" FROM fo_direcciones, bo_locales L, bo_comunas C, fo_tipo_calle, bo_zonas Z " +
					" WHERE dir_cli_id = ? AND L.id_local = dir_loc_id " +
					" AND id_comuna = dir_com_id AND id_zona = dir_zona_id " +
					" AND tip_id = dir_tip_id AND dir_estado<>'E' ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);

			stm.setLong(1, cli_id);
			rs = stm.executeQuery();
			while (rs.next()) {
				dir = new DireccionEntity();
				tip = new TipoCalleEntity();
				tip.setId(new Long(rs.getString("tip_id")));
				tip.setNombre(rs.getString("tip_nombre"));
				tip.setEstado(rs.getString("tip_estado"));
				dir.setTipo_calle(tip);
				dir.setId( new Long(rs.getString("dir_id")));
				dir.setCli_id(new Long(rs.getString("dir_cli_id")));
				dir.setLoc_cod(new Long(rs.getString("dir_loc_id")));
				dir.setCom_id(new Long(rs.getString("dir_com_id")));
				dir.setZon_id(new Long(rs.getString("dir_zona_id")));
				dir.setAlias(rs.getString("dir_alias"));
				dir.setCalle(rs.getString("dir_calle"));
				dir.setNumero(rs.getString("dir_numero"));
				dir.setDepto(rs.getString("dir_depto"));
				dir.setComentarios(rs.getString("dir_comentarios"));
				dir.setEstado(rs.getString("dir_estado"));
				dir.setFec_crea(rs.getTimestamp("dir_fec_crea"));
				dir.setFnueva(rs.getString("dir_fnueva"));
				dir.setCuadrante(rs.getString("dir_cuadrante"));
				dir.setNom_local(rs.getString("nom_local"));
				dir.setNom_comuna(rs.getString("nom_com"));
				dir.setNom_zona(rs.getString("nom_zon"));
				dir.setNom_tip_calle(rs.getString("tip_nombre"));
				lista_dirs.add(dir);
			}
			rs.close();
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_dirs;
	}*/
		
	/**
	 * Verifica si existe pedido.
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si existe pedido, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean isPedidoById(long id_pedido) throws PedidosDAOException{
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en isPedidoById");
			String sql = "SELECT id_pedido FROM bo_pedidos " +
					"WHERE id_pedido = ? ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result = true;
			}

			if(!result)
				throw new PedidosDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result?"+result);
		return result;
	}
	
	
	/**
	 * Obtiene detalles del pedido
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getDetallesPedido(long id_pedido) throws PedidosDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("getDetallesPedido " );
			String sql = " SELECT id_detalle, id_pedido, id_sector, id_producto, cod_prod1, uni_med, " +
				" descripcion, cant_solic, observacion, preparable, con_nota, pesable, cant_pick, cant_faltan, cant_spick,  " +
				" precio, precio_lista " +
				" FROM bo_detalle_pedido " +
				" WHERE id_pedido = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getInt("id_detalle"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_sector(rs.getInt("id_sector"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_producto(rs.getString("cod_prod1"));
				prod.setUnid_medida(rs.getString("uni_med"));
				prod.setDescripcion(rs.getString("descripcion"));
				prod.setCant_solic(rs.getDouble("cant_solic"));
				prod.setObservacion(rs.getString("observacion"));
				prod.setPreparable(rs.getString("preparable"));
				prod.setCon_nota(rs.getString("con_nota"));
				prod.setPesable(rs.getString("pesable"));
				prod.setCant_pick(rs.getDouble("cant_pick"));
				prod.setCant_faltan(rs.getDouble("cant_faltan"));
				prod.setCant_spick(rs.getDouble("cant_spick"));
				prod.setPrecio_lista(rs.getDouble("precio_lista"));
				prod.setPrecio(rs.getDouble("precio"));
				result.add(prod);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	/**(+) INDRA (+)
	 * Obtiene los productos de un pedido de la tabla detalles del pedido
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getListadoProductosxPedido(long id_pedido) throws PedidosDAOException {

		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("getListadoProductosxPedido " );
			String sql = " SELECT DP.ID_PEDIDO, DP.ID_PRODUCTO, DP.COD_PROD1, DP.CANT_FALTAN, CANT_SPICK FROM BO_PRODUCTOS PP , " +
					" BO_DETALLE_PEDIDO DP , BO_PEDIDOS P WHERE P.ID_PEDIDO = DP.ID_PEDIDO " +
					" AND PP.ID_PRODUCTO = DP.ID_PRODUCTO AND DP.ID_PEDIDO =  ? AND CANT_FALTAN > 0";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_producto(rs.getLong("ID_PRODUCTO"));
				prod.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				prod.setCant_spick(rs.getDouble("CANT_SPICK"));
				prod.setCod_producto(rs.getString("COD_PROD1"));
								
				result.add(prod);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

//(-) INDRA (-)
	/**
	 * Actualiza el sector en el detalle del pedido
	 * 
	 * @param  id_detalle long 
	 * @param  id_sector long 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean actSectorDetalle(long id_detalle, long id_sector) throws PedidosDAOException {
		
		boolean result=false;
		PreparedStatement stm =null;

		try {

			logger.debug("actSectorDetalle " );
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String sql = "UPDATE bo_detalle_pedido SET id_sector = ? WHERE id_detalle = ? ";
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_detalle);
			logger.debug("id_sector: " + id_sector);

			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_sector);
			stm.setLong(2,id_detalle);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	/**(+) INDRA (+)
	 * Actualiza el sector en el detalle del pedido
	 * 
	 * @param  id_detalle long 
	 * @param  id_sector long 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */
	public boolean setModDetallePedido(long id_pedido, double cantidad, long id_producto) throws PedidosDAOException {
	
		boolean result=false;
		PreparedStatement stm =null;

		try {

			logger.debug("actSectorDetalle " );
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String sql = "UPDATE BO_DETALLE_PEDIDO SET CANT_FALTAN =0.00 , CANT_SPICK = ? " +
					" WHERE ID_PRODUCTO = ? AND ID_PEDIDO = ? ";
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_pedido);
			logger.debug("id_sector: " + id_producto);
			logger.debug("id_sector: " + cantidad);

			stm = conn.prepareStatement(sql);
			stm.setDouble(1,cantidad);
			stm.setLong(2,id_producto);
			stm.setLong(3,id_pedido);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	/**(-) INDRA (-)
	 * Recalcular el monto total del pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean recalcPedido(long idPedido, String tipoDoc) throws PedidosDAOException {
		 
		boolean result=false;
		PreparedStatement stm =null;
		PreparedStatement stm2 =null;
		ResultSet rs=null;

		try {
            conn = this.getConnection();
            String sql = "";
            if ( "F".equalsIgnoreCase( tipoDoc ) ) {
                sql = "SELECT SUM(ROUND(precio*cant_solic,0)) AS tot_det, SUM(ROUND( ( (dp.PRECIO*dp.CANT_SOLIC) * CASE WHEN rs.IMPUESTO IS NULL THEN 1 ELSE rs.IMPUESTO END) ,0)) AS tot_det_con_imp, sum(dp.CANT_SOLIC) AS cantidad " +
                      "FROM bodba.bo_detalle_pedido dp " +
                      "INNER JOIN bodba.bo_productos pro ON pro.ID_PRODUCTO = dp.ID_PRODUCTO " +
                      "LEFT JOIN bodba.bo_sector sec ON sec.ID_SECTOR = dp.ID_SECTOR " +
                      "LEFT JOIN bodba.bo_seccionxsector sxs ON sxs.ID_SECCION = INTEGER(SUBSTR(pro.ID_CATPROD, 1, 2)) AND sxs.ID_SECTOR  = sec.ID_SECTOR " +
                      "LEFT JOIN bodba.bo_rubroxseccion rs ON rs.ID_SECCION = INTEGER(SUBSTR(pro.ID_CATPROD, 1, 2)) AND rs.ID_RUBRO = INTEGER(SUBSTR(pro.ID_CATPROD, 3, 2)) " +
                      "WHERE dp.id_pedido = ? " +
                      "GROUP BY 1,2";
                
            } else {
                sql = "SELECT SUM(ROUND(precio*cant_solic,0)) AS tot_det, sum(cant_solic) AS cantidad " +
                      "FROM bodba.bo_detalle_pedido " +
                      "WHERE id_pedido = ? " +
                      "GROUP BY 1";
            }
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idPedido);
            rs = stm.executeQuery();
            
            if ( rs.next() ) {
                String sql2 = "UPDATE bodba.bo_pedidos " +
                              "SET monto_pedido = ?, cant_productos = ?, monto_reservado = ( costo_despacho + ? ) " +
                              "WHERE id_pedido = ?";

                stm2 = conn.prepareStatement(sql2);
                double montoPedido = rs.getDouble("tot_det");
                stm2.setDouble(1, montoPedido);
                stm2.setInt(2, rs.getInt("cantidad"));
                if ( "F".equalsIgnoreCase( tipoDoc ) ) {
                    //Para factura traemos calculado el total con los porcentajes considerados en los rubros
                    //double totConImpuesto = ( rs.getDouble("tot_det_con_imp") * 1.05 );
                    double totConImpuesto = rs.getDouble("tot_det_con_imp");
                    stm2.setDouble(3, Utils.redondear( totConImpuesto ) );      
                } else {
                    //Para boleta se agrega un 5% mas
                    //stm2.setDouble(3, Utils.redondear( ( montoPedido * 1.05 ) ) );
                    stm2.setDouble(3, Utils.redondear( montoPedido ) );
                }
                stm2.setLong(4,idPedido);
                int i = stm2.executeUpdate();
                if ( i > 0 )
                    result = true;
               
            }

            
            
            
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
		
	}
	
	
	/**
	 * Obtiene precio de un producto segun el local.
	 * 
	 * @param  id_local long 
	 * @param  id_producto long 
	 * @return double
	 * @throws PedidosDAOException
	 * 
	 */
	public double getPrecioByLocalProd(long id_local, long id_producto) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		double result = 0;
		try {
			conn = this.getConnection();
			logger.debug("en getPrecioByLocalProd");
			String sql = "SELECT prec_valor FROM bo_precios " +
					" WHERE id_local = ? AND id_producto = ? ";
			logger.debug(sql);
			logger.debug("id_local:"+id_local);
			logger.debug("id_producto:"+id_producto);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_local);
			stm.setLong(2, id_producto);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result=rs.getDouble("prec_valor");
			}

		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result? "+result);
		return result;
	}
	
	
	/**
	 * Actualiza dirección de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setDireccion(long id_pedido) throws PedidosDAOException {
		
		boolean result=false;
		PreparedStatement stm =null;
		
		try {

			logger.debug("en setDireccion " );
			String sql = " update fo_direcciones " +
				" set dir_fnueva=null " +
				" where dir_id in (select dir_id from bo_pedidos where id_pedido = ?)"; 

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			int i = stm.executeUpdate();
			if(i>0)
					result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene pedidos de un cliente
	 * 
	 * @param  id_cliente long 
	 * @return List PedidoDTO
	 * @throws PedidosDAOException
	 * 
	 */
	public List getPedidosByEstados(long id_cliente) throws PedidosDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT id_pedido, id_estado FROM bo_pedidos " +
				" WHERE id_cliente = ? " +
				" AND id_estado in ("+Constantes.ID_ESTAD_PEDIDO_VALIDADO+","+
				Constantes.ID_ESTAD_PEDIDO_EN_PICKING+","+
				Constantes.ID_ESTAD_PEDIDO_EN_BODEGA+","+
				Constantes.ID_ESTAD_PEDIDO_PAGADO+","+
				Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO+","+
				Constantes.ID_ESTAD_PEDIDO_FINALIZADO+")";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_cliente);
			logger.debug("SQL: " + sql);
			logger.debug("id_cliente: " + id_cliente);
			System.out.println("*SQL getPedidosByEstados: " + sql);
			System.out.println("*id_cliente: " + id_cliente);

			rs = stm.executeQuery();
			while (rs.next()) {
				PedidoDTO ped = new PedidoDTO();
				ped.setId_pedido(rs.getLong("id_pedido"));
				ped.setId_estado(rs.getLong("id_estado"));
				result.add(ped);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	
	
	
	public List getPedidosPorEstado(long id_cliente, long idPedido) throws PedidosDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT p.id_pedido, " +
			" p.id_estado, " +
			" (select fecha from BODBA.BO_JORNADA_DESP jd where p.id_jdespacho = jd.id_jdespacho ) as f_despacho " +
			" FROM bo_pedidos p" +
			" WHERE p.id_cliente = ? " +
			" AND p.id_estado not in (4,21,20,53,62,63,69) " +
			" and p.id_pedido not in ("+idPedido+")";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_cliente);
			logger.debug("SQL getPedidosPorEstado : " + sql);
			logger.debug("id_cliente: " + id_cliente);
			System.out.println("*SQL getPedidosByEstados: " + sql);
			System.out.println("*id_cliente: " + id_cliente);

			rs = stm.executeQuery();
			while (rs.next()) {
				PedidoDTO ped = new PedidoDTO();
				ped.setId_pedido(rs.getLong("id_pedido"));
				ped.setId_estado(rs.getLong("id_estado"));
				ped.setFdespacho(rs.getString("f_despacho"));
				result.add(ped);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}


	public List getPedidosPorEstadoR(long id_cliente) throws PedidosDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT p.id_pedido, " +
			" p.id_estado, " +
			" (select fecha from BODBA.BO_JORNADA_DESP jd where p.id_jdespacho = jd.id_jdespacho ) as f_despacho " +
			" FROM bo_pedidos p" +
			" WHERE p.id_cliente = ? " +
			" AND p.id_estado not in (4,21,20,53,62,63,69) ";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_cliente);
			logger.debug("SQL getPedidosPorEstado : " + sql);
			logger.debug("id_cliente: " + id_cliente);
			System.out.println("*SQL getPedidosByEstados: " + sql);
			System.out.println("*id_cliente: " + id_cliente);

			rs = stm.executeQuery();
			while (rs.next()) {
				PedidoDTO ped = new PedidoDTO();
				ped.setId_pedido(rs.getLong("id_pedido"));
				ped.setId_estado(rs.getLong("id_estado"));
				ped.setFdespacho(rs.getString("f_despacho"));
				result.add(ped);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	
	
	/**
	 * Obtiene el Monto Total Solicitado por el Cliente
	 * 
	 * @param  id_pedido long 
	 * @return double Monto Total Solicitado por el Cliente
	 * @throws PedidosDAOException
	 * 
	 */
	public double getMontoTotalDetPedidoByIdPedido(long id_pedido) throws PedidosDAOException {
		double MontoTotal = 0D;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT SUM(COALESCE(DP.PRECIO,0)*COALESCE(DP.CANT_SOLIC,0)) AS TOTAL "
                       + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                       + "WHERE DP.ID_PEDIDO = ?";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
                MontoTotal = rs.getDouble("TOTAL");
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Monto Total: " + MontoTotal);
		return MontoTotal;
	}

	
	/**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return double Monto Total del Picking
	 * @throws PedidosDAOException
	 * 
	 */
	public double getMontoTotalDetPickingByIdPedido(long id_pedido) throws PedidosDAOException {
		double MontoTotal = 0D;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT SUM(COALESCE(DP.PRECIO,0)*COALESCE(DP.CANT_PICK,0)) AS TOTAL "
                       + "FROM BODBA.BO_DETALLE_PICKING DP "
                       + "     LEFT JOIN BODBA.BO_BINS_PEDIDO BP ON BP.ID_BP = DP.ID_BP "
                       + "WHERE DP.ID_PEDIDO = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
                MontoTotal = rs.getDouble("TOTAL");
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Monto Total: " + MontoTotal);
		return MontoTotal;
	}

	
	/**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return double Monto Total del Picking
	 * @throws PedidosDAOException
	 * 
	 */
	public long getMontoTotalTrxByIdPedido(long id_pedido) throws PedidosDAOException {
	    long MontoTotal = 0L;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT SUM(TP.POS_MONTO_FP) AS TOTAL "
                       + "FROM BODBA.BO_TRX_MP TP "
                       + "WHERE TP.ID_PEDIDO = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
                MontoTotal = rs.getLong("TOTAL");
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Monto Total: " + MontoTotal);
		return MontoTotal;
	}


	/**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return HashMap DetallePickingEntity
	 * @throws PedidosDAOException
	 * 
	 */
	public HashMap getDetPickingByIdPedido(long id_pedido) throws PedidosDAOException {
		HashMap lstDetPick = new HashMap();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, DP.ID_PRODUCTO, "
                       + "       DP.ID_PEDIDO, DP.CBARRA, DP.DESCRIPCION, DP.PRECIO, "
                       + "       DP.CANT_PICK, DP.SUSTITUTO, BP.COD_BIN, P.COD_PROD1, P.UNI_MED "
                       + "FROM BODBA.BO_DETALLE_PICKING DP "
                       + "     LEFT JOIN BODBA.BO_BINS_PEDIDO   BP ON BP.ID_BP     = DP.ID_BP "
                       + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO P ON P.ID_DETALLE = DP.ID_DETALLE "
                       + "WHERE DP.ID_PEDIDO = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
			    //String key = rs.getString("id_detalle");
			    String key = rs.getString("cod_prod1") + rs.getString("uni_med") + rs.getString("cbarra");
			    if (lstDetPick.get(key) == null ){
					DetallePickingEntity dpick = new DetallePickingEntity();
					dpick.setId_dpicking(rs.getLong("id_dpicking"));
					dpick.setId_detalle(rs.getLong("id_detalle"));
					dpick.setId_bp(rs.getLong("id_bp"));
					dpick.setId_producto(rs.getLong("id_producto"));
					dpick.setId_pedido(rs.getLong("id_pedido"));
					dpick.setCBarra(rs.getString("cbarra"));
					dpick.setDescripcion(rs.getString("descripcion"));
					dpick.setPrecio(new Double(rs.getDouble("precio")));
					dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
					dpick.setSustituto(rs.getString("sustituto"));
					dpick.setCod_bin(rs.getString("cod_bin"));
					dpick.setCod_prod(rs.getString("cod_prod1"));
					dpick.setUni_med(rs.getString("uni_med"));
					lstDetPick.put(key, dpick);
			    }else{
			        double cant_pick = ((DetallePickingEntity)lstDetPick.get(key)).getCant_pick().doubleValue();
			        cant_pick = cant_pick + Double.parseDouble(rs.getString("cant_pick"));
			        ((DetallePickingEntity)lstDetPick.get(key)).setCant_pick(Double.valueOf(""+cant_pick));
			    }
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+lstDetPick.size());
		return lstDetPick;
	}
	
	
	/**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return HashMap DetallePickingEntity
	 * @throws PedidosDAOException
	 * 
	 */
	public List getDetPickingHojaDesp2(long id_pedido) throws PedidosDAOException {
		List lstDetPick = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		String SQL = "";

		try {
			/*String sql = "SELECT DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, DP.ID_PRODUCTO, "
                       + "       DP.ID_PEDIDO, DP.CBARRA, DP.DESCRIPCION, DP.PRECIO, "
                       + "       SUM(DP.CANT_PICK) AS CANT_PICK, DP.SUSTITUTO, BP.COD_BIN, "
                       + "       P.COD_PROD1, P.UNI_MED "
                       + "FROM BODBA.BO_DETALLE_PICKING DP "
                       + "     LEFT JOIN BODBA.BO_BINS_PEDIDO   BP ON BP.ID_BP     = DP.ID_BP "
                       + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO P ON P.ID_DETALLE = DP.ID_DETALLE "
                       + "WHERE DP.ID_PEDIDO = ? "
                       + "GROUP BY P.COD_PROD1, P.UNI_MED, DP.CBARRA, "
                       + "         DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, DP.ID_PRODUCTO, "
                       + "         DP.ID_PEDIDO, DP.DESCRIPCION, DP.PRECIO, DP.SUSTITUTO, BP.COD_BIN "
                       + "ORDER BY BP.COD_BIN, DP.DESCRIPCION ";*/
		    
		    SQL = "SELECT DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, DP.ID_PRODUCTO, "
                + "       DP.ID_PEDIDO, DP.CBARRA, DP.DESCRIPCION, DP.PRECIO, "
                + "       SUM(DP.CANT_PICK) AS CANT_PICK, DP.SUSTITUTO, " 
                + "       CASE WHEN BP.COD_BIN = '' THEN 'PRODUCTOS SUELTOS' "
                + "            ELSE BP.COD_BIN "
                + "       END AS COD_BIN, "
                + "       P.COD_PROD1, P.UNI_MED "
                + "FROM BODBA.BO_DETALLE_PICKING DP "
                + "     LEFT JOIN BODBA.BO_BINS_PEDIDO   BP ON BP.ID_BP     = DP.ID_BP "
                + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO P ON P.ID_DETALLE = DP.ID_DETALLE "
                + "WHERE DP.ID_PEDIDO = ? "
                + "GROUP BY P.COD_PROD1, P.UNI_MED, DP.CBARRA, "
                + "         DP.ID_DPICKING, DP.ID_DETALLE, DP.ID_BP, DP.ID_PRODUCTO, "
                + "         DP.ID_PEDIDO, DP.DESCRIPCION, DP.PRECIO, DP.SUSTITUTO, BP.COD_BIN "
                + "ORDER BY BP.COD_BIN, DP.DESCRIPCION";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL (getDetPickingHojaDesp2): " + SQL);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			int i=0;
			while (rs.next()) {
			    //String key = rs.getString("id_detalle");
			    //String key = rs.getString("cod_prod1") + rs.getString("uni_med") + rs.getString("cbarra");
			    //String key = "" + i;
				DetallePickingEntity dpick = new DetallePickingEntity();
				dpick.setId_dpicking(rs.getLong("id_dpicking"));
				dpick.setId_detalle(rs.getLong("id_detalle"));
				dpick.setId_bp(rs.getLong("id_bp"));
				dpick.setId_producto(rs.getLong("id_producto"));
				dpick.setId_pedido(rs.getLong("id_pedido"));
				dpick.setCBarra(rs.getString("cbarra"));
				dpick.setDescripcion(rs.getString("descripcion"));
				dpick.setPrecio(new Double(rs.getDouble("precio")));
				dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
				dpick.setSustituto(rs.getString("sustituto"));
				dpick.setCod_bin(rs.getString("cod_bin"));
				dpick.setCod_prod(rs.getString("cod_prod1"));
				dpick.setUni_med(rs.getString("uni_med"));
				lstDetPick.add(dpick);
				i++;
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+lstDetPick.size());
		return lstDetPick;
	}
	

	/*
    
	**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return HashMap DetallePickingEntity
	 * @throws PedidosDAOException
	 * 
	 *
	public HashMap getDetPickingByIdPedido(long id_pedido) throws PedidosDAOException {
		HashMap lstDetPick = new HashMap();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT dp.id_dpicking, dp.id_detalle, dp.id_bp, dp.id_producto, "
                       + "       dp.id_pedido, dp.cbarra, dp.descripcion, dp.precio, "
                       + "       dp.cant_pick, dp.sustituto, bp.cod_bin, p.cod_prod1, p.uni_med "
                       + "FROM bo_detalle_picking dp "
                       + "     LEFT JOIN bo_bins_pedido bp on bp.id_bp = dp.id_bp "
                       + "     LEFT JOIN bo_detalle_pedido p on p.id_detalle = dp.id_detalle "
                       + "WHERE dp.id_pedido = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
			    //String key = rs.getString("cod_prod1") + "-" + rs.getString("uni_med");
			    if (lstDetPick.get(key) == null ){
					DetallePickingEntity dpick = new DetallePickingEntity();
					dpick.setId_dpicking(new Long(rs.getLong("id_dpicking")));
					dpick.setId_detalle(new Long(rs.getLong("id_detalle")));
					dpick.setId_bp(new Long(rs.getLong("id_bp")));
					dpick.setId_producto(new Long(rs.getLong("id_producto")));
					dpick.setId_pedido(new Long(rs.getLong("id_pedido")));
					dpick.setCBarra(rs.getString("cbarra"));
					dpick.setDescripcion(rs.getString("descripcion"));
					dpick.setPrecio(new Double(rs.getDouble("precio")));
					dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
					dpick.setSustituto(rs.getString("sustituto"));
					dpick.setCod_bin(rs.getString("cod_bin"));
					dpick.setCod_prod(rs.getString("cod_prod1"));  // Código SAP
					dpick.setUni_med(rs.getString("uni_med"));
					lstDetPick.put(rs.getString("id_detalle"), dpick);
			    }else{
			        double cant_pick = ((DetallePickingEntity)lstDetPick.get(key)).getCant_pick().doubleValue();
			        cant_pick = cant_pick + Double.parseDouble(rs.getString("cant_pick"));
			        ((DetallePickingEntity)lstDetPick.get(key)).setCant_pick(Double.valueOf(""+cant_pick));
			    }
			    
			}
			rs.close();
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
            try {
                if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+lstDetPick.size());
		return lstDetPick;
	}





    public List getDetPickingByIdPedido(long id_pedido) throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT 	dp.id_dpicking, dp.id_detalle, dp.id_bp, dp.id_producto, dp.id_pedido, dp.cbarra,"+
						 "		dp.descripcion, dp.precio, dp.cant_pick, dp.sustituto, bp.cod_bin, p.cod_prod1, p.uni_med"+ 
						 "	FROM 	bo_detalle_picking dp"+
						 "	LEFT JOIN bo_bins_pedido bp on bp.id_bp = dp.id_bp"+
						 "	LEFT JOIN bo_detalle_pedido p on p.id_detalle = dp.id_detalle"+
						 "	WHERE dp.id_pedido = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				DetallePickingEntity dpick = new DetallePickingEntity();
				dpick.setId_dpicking(new Long(rs.getLong("id_dpicking")));
				dpick.setId_detalle(new Long(rs.getLong("id_detalle")));
				dpick.setId_bp(new Long(rs.getLong("id_bp")));
				dpick.setId_producto(new Long(rs.getLong("id_producto")));
				dpick.setId_pedido(new Long(rs.getLong("id_pedido")));
				dpick.setCBarra(rs.getString("cbarra"));
				dpick.setDescripcion(rs.getString("descripcion"));
				dpick.setPrecio(new Double(rs.getDouble("precio")));
				dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
				dpick.setSustituto(rs.getString("sustituto"));
				dpick.setCod_bin(rs.getString("cod_bin"));
				dpick.setCod_prod(rs.getString("cod_prod1"));
				dpick.setUni_med(rs.getString("uni_med"));
				result.add(dpick);
			}
			rs.close();
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
            try {
                if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+result.size());
		return result;
	}*/

	
	/**
	 * Obtiene las facturas de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List FacturaEntity
	 * @throws PedidosDAOException
	 * 
	 */
	public List getFacturasByIdPedido(long id_pedido) throws PedidosDAOException {
		
		List result= new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
		/*	String sql = " SELECT id_factura, id_pedido, razon, rut, dv, direccion, fono, giro " +
				" FROM bo_facturas_ped WHERE id_pedido = ? ";
		*/
			String sql = "SELECT  p.id_pedido id_pedido, p.num_doc num_doc, p.tipo_doc tipo_doc," +
					" f.id_factura id_factura, f.razon razon, f.rut rut, f.dv dv, f.direccion direccion," +
					" f.fono fono, f.giro giro, f.ciudad ciudad, f.comuna comuna" +
					" FROM bo_facturas_ped f " +
					" RIGHT JOIN bo_pedidos p on p.id_pedido = f.id_pedido " +
					" WHERE p.id_pedido = ? AND p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				FacturaEntity fact = new FacturaEntity();
				fact.setId_pedido(new Long(rs.getLong("id_pedido")));				
				fact.setNum_doc(new Long(rs.getLong("num_doc")));
				fact.setTipo_doc(rs.getString("tipo_doc"));
				fact.setId_pedido(new Long(rs.getLong("id_pedido")));				
				fact.setId_factura(new Long(rs.getLong("id_factura")));				
				fact.setRazon(rs.getString("razon"));
				fact.setRut(new Integer(rs.getInt("rut")));
				fact.setDv(rs.getString("dv"));
				fact.setDireccion(rs.getString("direccion"));
				fact.setFono(rs.getString("fono"));
				fact.setGiro(rs.getString("giro"));
				fact.setCiudad(rs.getString("ciudad"));
				fact.setComuna(rs.getString("comuna"));
				result.add(fact);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Agrega bins que pertenecen a una ronda. 
	 * 
	 * @param  lst_bin List 
	 * @param  id_ronda long 
	 * @return List TPBinOpDTO, con nuevo id_bin
	 * @throws PedidosDAOException
	 * 
	 */
	public List addBinsPedidoPOS(List lst_bin, long id_ronda) throws PedidosDAOException {
		
		List lst_bin_pos = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en addBinsPedidoPOS:");
			long id_bin = -1;
			
			for(int i=0; i<lst_bin.size(); i++){
				TPBinOpDTO binOp = null; 
				binOp = (TPBinOpDTO)lst_bin.get(i);
				//preparar el bindto a insertar
				BinDTO dto = new BinDTO();
				dto.setId_ronda(id_ronda);
				dto.setId_pedido(binOp.getId_op());
				dto.setCod_bin(binOp.getCod_bin()+"");
				dto.setCod_sello1(binOp.getCod_sello1()+"");
				dto.setCod_sello2(binOp.getCod_sello2()+"");
				dto.setCod_ubicacion(binOp.getCod_ubicacion());
				dto.setTipo(binOp.getTipo());
				//	---------- mod_feb09 - ini------------------------
				dto.setVisualizado(binOp.getVisualizado());			
				
				String sql = "INSERT INTO bo_bins_pedido (id_ronda, id_pedido, cod_bin, cod_sello1, " +
						" cod_sello2, cod_ubicacion, tipo, visualizado) "+
					" VALUES (?,?,?,?,?,?,?,?) ";
				logger.debug(sql);
				logger.debug("vals"+dto.getId_ronda()+","+dto.getId_pedido()+","+dto.getCod_bin()+","+
						dto.getCod_sello1()+","+dto.getCod_sello2()+","+dto.getCod_ubicacion()+","+dto.getTipo()+","+dto.getVisualizado());
				stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stm.setLong(1, dto.getId_ronda());
				stm.setLong(2, dto.getId_pedido());
				stm.setString(3, dto.getCod_bin());
				stm.setString(4, dto.getCod_sello1());
				stm.setString(5, dto.getCod_sello2());
				stm.setString(6, dto.getCod_ubicacion());
				stm.setString(7, dto.getTipo());
				stm.setString(8, dto.getVisualizado());
				//	---------- mod_feb09 - fin------------------------
				int j = stm.executeUpdate();
				logger.debug("inserto?"+j);
				
				rs = stm.getGeneratedKeys();
				if (rs.next()) {
					id_bin = rs.getLong(1);
				}
				binOp.setId_bin(id_bin);
				lst_bin_pos.add(binOp);
				
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
		return lst_bin_pos;
	}

	
	/**
	 * Agrega detalle de picking. 
	 * 
	 * @param  dto DetallePickingDTO 
	 * @return long, nuevo id
	 * @throws PedidosDAOException
	 * 
	 */
	public long addDetallePicking(DetallePickingDTO dto) throws PedidosDAOException {
		
		long id_det = -1;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en addDetallePicking:");
			
			if(dto.getId_producto()!=-1){
//				---------- mod_ene09 - ini------------------------
				String sql = "INSERT INTO bo_detalle_picking (id_detalle, id_bp, id_producto, id_pedido, cbarra, descripcion, precio, " +
					" cant_pick, sustituto, auditado) "+
					" VALUES (?,?,?,?,?,?,?,?,?, ?) ";
				logger.debug(sql);
				logger.debug("vals"+dto.getId_detalle()+","+dto.getId_bp()+","+dto.getId_producto()+","+dto.getId_pedido()+","+dto.getCBarra()+","+
						dto.getDescripcion()+","+dto.getPrecio()+","+dto.getCant_pick()+","+dto.getSustituto()+","+ dto.getAuditado());
//				---------- mod_ene09 - fin------------------------
				stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stm.setLong(1, dto.getId_detalle());
				stm.setLong(2, dto.getId_bp());
				stm.setLong(3, dto.getId_producto());
				stm.setLong(4, dto.getId_pedido());
				stm.setString(5, dto.getCBarra());
				stm.setString(6, dto.getDescripcion());
				stm.setDouble(7, dto.getPrecio());
				stm.setDouble(8, dto.getCant_pick());
				stm.setString(9, dto.getSustituto());
//				---------- mod_ene09 - ini------------------------
				stm.setString(10, dto.getAuditado());
//				---------- mod_ene09 - fin------------------------
				
			}else {
				String sql = "INSERT INTO bo_detalle_picking (id_detalle, id_bp, id_pedido, cbarra, descripcion, precio, " +
					" cant_pick, sustituto, auditado) "+
					" VALUES (?,?,?,?,?,?,?,?,?) ";
				logger.debug(sql);
				logger.debug("vals"+dto.getId_detalle()+","+dto.getId_bp()+","+dto.getId_pedido()+","+dto.getCBarra()+","+
						dto.getDescripcion()+","+dto.getPrecio()+","+dto.getCant_pick()+","+dto.getSustituto()+","+ dto.getAuditado());
				stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stm.setLong(1, dto.getId_detalle());
				stm.setLong(2, dto.getId_bp());
				stm.setLong(3, dto.getId_pedido());
				stm.setString(4, dto.getCBarra());
				stm.setString(5, dto.getDescripcion());
				stm.setDouble(6, dto.getPrecio());
				stm.setDouble(7, dto.getCant_pick());
				stm.setString(8, dto.getSustituto());
				stm.setString(8, dto.getAuditado());
				
			}
			
			int i = stm.executeUpdate();
			logger.debug("inserto?"+i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
				id_det = rs.getLong(1);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
		return id_det;
	}

	
	/**
	 * Obtiene el detalle de pedido, segun id del detalle. 
	 * 
	 * @param  id_detalle long 
	 * @return ProductosPedidoDTO
	 * @throws PedidosDAOException
	 * 
	 */
	public ProductosPedidoDTO getDetallePedidoById(long id_detalle) throws PedidosDAOException {
		
		ProductosPedidoDTO prod = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("En getDetallePedidoById:" );
			String sql = " SELECT id_detalle, id_pedido, id_sector, id_producto, cod_prod1, uni_med, precio, " +
				" descripcion, cant_solic, observacion, preparable, con_nota, pesable, cant_pick, cant_faltan, cant_spick  " +
				" FROM bo_detalle_pedido " +
				" WHERE id_detalle = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,id_detalle);
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_detalle);

			rs = stm.executeQuery();
			if (rs.next()) {
				
				prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getInt("id_detalle"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_sector(rs.getInt("id_sector"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_producto(rs.getString("cod_prod1"));
				prod.setUnid_medida(rs.getString("uni_med"));
				prod.setPrecio(rs.getDouble("precio"));
				prod.setDescripcion(rs.getString("descripcion"));
				prod.setCant_solic(rs.getDouble("cant_solic"));
				prod.setObservacion(rs.getString("observacion"));
				prod.setPreparable(rs.getString("preparable"));
				prod.setCon_nota(rs.getString("con_nota"));
				prod.setPesable(rs.getString("pesable"));
				prod.setCant_pick(rs.getDouble("cant_pick"));
				prod.setCant_faltan(rs.getDouble("cant_faltan"));
				prod.setCant_spick(rs.getDouble("cant_spick"));
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return prod;
	}

	/**
	 * Obtiene el producto sustituto, segun código de barra y id del local 
	 * 
	 * @param  cbarra String 
	 * @param  id_local long 
	 * @return ProductosPedidoDTO
	 * @throws PedidosDAOException
	 * 
	 */
	public ProductosPedidoDTO getDetSustituto(String cbarra,long id_local) throws PedidosDAOException {
		
		ProductosPedidoDTO prod = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("En getDetSustituto:" );
			String sql = " SELECT C.id_producto id_producto, P.des_larga descripcion, PR.prec_valor precio " +
					" FROM bo_codbarra C " +
					" JOIN bo_productos P ON P.id_producto = C.id_producto " +
					" JOIN bo_precios PR ON PR.id_producto = P.id_producto and PR.id_local = ? " +
					" WHERE C.cod_barra = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_local);
			stm.setString(2, cbarra);
			logger.debug("SQL: " + sql);
			logger.debug("cbarra: " + cbarra);
			logger.debug("id_local: " + id_local);

			rs = stm.executeQuery();
			if (rs.next()) {
				
				prod = new ProductosPedidoDTO();
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setDescripcion(rs.getString("descripcion"));
				prod.setPrecio(rs.getDouble("precio"));
				
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return prod;
	}

	/**
	 * Actualiza cantidades en el detalle de la ronda 
	 * 
	 * @param  detRndDto DetalleRondaDTO 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean actDetalleRonda(DetalleRondaDTO detRndDto) throws PedidosDAOException {
		
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En actDetalleRonda:" );
			String sql = " UPDATE bo_detalle_rondas SET cant_pick = ?, cant_faltan = ?, cant_spick = ?, sustituto = ?, mot_sustitucion = ? " +
					" WHERE id_dronda = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setDouble(1, detRndDto.getCant_pick());
			stm.setDouble(2, detRndDto.getCant_faltan());
			stm.setDouble(3, detRndDto.getCant_spick());
			stm.setString(4, detRndDto.getSustituto());
			stm.setInt(5, detRndDto.getMot_sustitucion());
			stm.setLong(6, detRndDto.getId_dronda());
			
			logger.debug("SQL: " + sql);
			logger.debug("id_dronda: " + detRndDto.getId_dronda());
			logger.debug("cant_pick: " + detRndDto.getCant_pick());
			logger.debug("cant_faltan: " + detRndDto.getCant_faltan());
			logger.debug("cant_spick: " + detRndDto.getCant_spick());
			logger.debug("sustituto: " + detRndDto.getSustituto());
			logger.debug("mot_sustitucion: " + detRndDto.getMot_sustitucion());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Actualiza el Estado del Envío del EMail
	 * 
	 * @param  long id_pedido 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean actEstadoEnvioEMail(long id_pedido) throws PedidosDAOException {
		
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			logger.debug("En actEstadoEnvioEMail:" );
			String sql = "UPDATE bo_pedidos "
                       + "   SET ESTADO_ENVIO_EMAIL = 'S' "
                       + "WHERE id_pedido = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
            
			stm.setLong(1, id_pedido);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);
            
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
	
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/**
	 * Actualiza cantidades en el detalle de pedido 
	 * 
	 * @param  dto ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean actCantDetallePedido(ProductosPedidoDTO dto) throws PedidosDAOException {
		
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			logger.debug("En actCantDetallePedido:" );
			String sql = " UPDATE bo_detalle_pedido SET cant_pick = ( coalesce(cant_pick,0) + ? ), " +
					" cant_faltan = ( coalesce(cant_faltan,0) + ? ), " +
					" cant_spick = ( coalesce(cant_spick,0) + ? ) " +
					" WHERE id_detalle = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setDouble(1, dto.getCant_pick());
			stm.setDouble(2, dto.getCant_faltan());
			stm.setDouble(3, dto.getCant_spick());
			stm.setDouble(4, dto.getId_detalle());
			
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + dto.getId_detalle());
			logger.debug("cant_pick: " + dto.getCant_pick());
			logger.debug("cant_faltan: " + dto.getCant_faltan());
			logger.debug("cant_spick: " + dto.getCant_spick());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Actualiza datos de un pedido que ha sido pickeado 
	 * 
	 * @param  dto PedidoDTO 
	 * @return boolean, devuelve <i>true</i> si actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean verificaPedidoPickeado(PedidoDTO dto) throws PedidosDAOException {
		
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En verificaPedidoPickeado:" );
			String sql = " UPDATE bo_pedidos SET id_estado=?, cant_bins=? " +
					" WHERE id_pedido = ? ";

			conn = this.getConnection();

			stm = conn.prepareStatement(sql);
	
			stm.setLong(1, dto.getId_estado());
			stm.setInt(2, dto.getCant_bins());
			stm.setLong(3, dto.getId_pedido());
			
			
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + dto.getId_pedido());
			logger.debug("id_estado: " + dto.getId_estado());
			logger.debug("cant_bins: " + dto.getCant_bins());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene cantidad de productos sin pickear de un pedido 
	 * 
	 * @param  id_pedido long 
	 * @return double 
	 * @throws PedidosDAOException
	 * 
	 */
	public double getCantSinPickearByPedido(long id_pedido) throws PedidosDAOException {
		
		double cantidad = 0;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("En getCantSinPickearByPedido:" );
			String sql = " SELECT SUM(cant_spick) cantidad FROM bo_detalle_pedido WHERE id_pedido = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getDouble("cantidad");
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return cantidad;
	}

	/**
	 * Verifica si las rondas de un pedido han finalizado. 
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si las rondas finalizaron, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean isFinalizadoRondasByPedido(long id_pedido) throws PedidosDAOException {
		
		boolean result = true;
		PreparedStatement stm=null;
		ResultSet rs = null;
		
		try {
			logger.debug("En isFinalizadoRondasByPedido:" );
			String sql = "SELECT COUNT (R.id_estado) cantidad " +
					" FROM bo_rondas R " +
					" JOIN bo_detalle_rondas DR ON DR.id_ronda = R.id_ronda " +
					" AND DR.id_pedido = ? AND R.id_estado IN (?, ?) ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	        
			stm.setLong(1, id_pedido);
			//stm.setInt(2, Constantes.ID_ESTADO_RONDA_FINALIZADA);
			stm.setInt(2, Constantes.ID_ESTADO_RONDA_CREADA);
			stm.setInt(3, Constantes.ID_ESTADO_RONDA_EN_PICKING);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);
			logger.debug("id_estado: " + Constantes.ID_ESTADO_RONDA_FINALIZADA);
			
			rs = stm.executeQuery();
			if(rs.next()){
				if( rs.getInt("cantidad")>0){
					result = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene la cantidad de bins de un pedido. 
	 * 
	 * @param  id_pedido long 
	 * @return int
	 * @throws PedidosDAOException
	 * 
	 */
	public int getCantBinsByPedido(long id_pedido) throws PedidosDAOException {
		
		int cantidad = 0;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("En getCantBinsByPedido:" );
			String sql = " SELECT COUNT(id_bp) cantidad FROM bo_bins_pedido WHERE id_pedido = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
				
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return cantidad;
	}
	
	/**
	 * Procesa transacción de pago Exitosa en POS
	 * 
	 * @param  fback POSFeedbackProcPagoDTO 
	 * 
	 * @throws PedidosDAOException
	 */
	public void doProcesaPagoOK(POSFeedbackProcPagoDTO fback) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		
		long id_pedido 	= 0;//fback.getId_pedido();
		int id_estado 	= Constantes.ID_ESTAD_PEDIDO_PAGADO;
		int pos_monto	= fback.getMonto_pagado();
		int num_caja 	= fback.getNum_pos();
		int pos_boleta  = fback.getNum_boleta();
		int pos_sma		= fback.getNum_sma();
		String fecha	= fback.getFecha();
		String hora		= fback.getHora();
		
		logger.debug("DAO doProcesaPagoOK:" );
		
		String sql =
			" UPDATE bo_pedidos " +
			" SET id_estado=?, pos_monto_fp=?, pos_num_caja=?, pos_boleta=?, " +
			" pos_trx_sma=?, pos_fecha=?, pos_hora=? " +
			" WHERE id_pedido = ? ";
		
		logger.debug("SQL: " + sql);
		logger.debug("id_pedido: " + id_pedido);
		logger.debug("id_estado: " + id_estado);
		logger.debug("pos_monto: " + pos_monto);
		logger.debug("pos_boleta: " + pos_boleta);
		logger.debug("num_caja: " + num_caja);
		logger.debug("pos_sma: " + pos_sma);
		logger.debug("fecha: " + fecha);
		logger.debug("hora: " + hora);

		
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( sql );
	
			stm.setLong(1, id_estado);
			stm.setInt(2, pos_monto);
			stm.setInt(3, num_caja);
			stm.setInt(4, pos_boleta);
			stm.setInt(5, pos_sma);
			stm.setString(6, fecha);
			stm.setString(7, hora);
			stm.setLong(8,id_pedido);
			
			int rc = stm.executeUpdate();
			logger.debug("rc:" + rc);

		} catch (SQLException e) {
			logger.debug("error: " + e.getMessage() );
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
	}
	
	
	/**
	 * Obtiene id_detalle de pedido, segun id de detalle de ronda.
	 * 
	 * @param  id_dronda long 
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getIdDetPedidoByIdDRonda(long id_dronda) throws PedidosDAOException {
		
		long id = -1;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("En getIdDetPedidoByIdDRonda:" );
			String sql = " SELECT id_detalle FROM bo_detalle_rondas WHERE id_dronda = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_dronda);
			logger.debug("SQL: " + sql);
			logger.debug("id_dronda: " + id_dronda);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id_detalle");
				
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return id;
	}

	/**
	 * Verifica si existe la ronda
	 * 
	 * @param  id_ronda long 
	 * @return boolean, devuelve <i>true</i> si existe la ronda, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean isRondaById(long id_ronda) throws PedidosDAOException {
		
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en isRondaById");
			String sql = "SELECT id_ronda FROM bo_rondas " +
					"WHERE id_ronda = ? ";
			logger.debug(sql);
			logger.debug("id_ronda:"+id_ronda);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_ronda);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result = true;
			}

			
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result?"+result);
		return result;
	}

	/**
	 * Verifica si existe relación entre el pedido y la ronda
	 * 
	 * @param  id_pedido long 
	 * @param  id_ronda long 
	 * @return boolean, devuelve <i>true</i> si existe la relación, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean isPedidoRelRondaByIds(long id_pedido, long id_ronda) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en isPedidoRelRondaByIds");
			String sql = "SELECT COUNT(id_dronda) cantidad FROM bo_detalle_rondas " +
					" WHERE id_ronda = ? and id_pedido = ? ";
			logger.debug(sql);
			logger.debug("id_ronda:"+id_ronda);
			logger.debug("id_pedido:"+id_pedido);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_ronda);
			stm.setLong(2, id_pedido);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				if(rs.getInt("cantidad")>0){
					result = true;
				}
			}

		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result?"+result);
		return result;
	}

	/**
	 * Obtener los datos del perfil.
	 * 
	 * @param  id_perfil int 
	 * @return PerfilesEntity
	 * @throws PedidosDAOException
	 */
	public PerfilesEntity getPerfilById(int id_perfil) throws PedidosDAOException {
		PerfilesEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en getPerfilById");
			String sql = "SELECT id_perfil, nombre, descripcion FROM bo_perfiles " +
					" WHERE id_perfil = ? ";
			logger.debug(sql);
			logger.debug("id_perfil:"+id_perfil);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_perfil);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result = new PerfilesEntity();
				result.setIdPerfil(new Long(rs.getLong("id_perfil")));
				result.setNombre(rs.getString("nombre"));
				result.setDescripcion(rs.getString("descripcion"));
			}

			
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result?"+result);
		return result;
	}
		
	/**
	 * Obtiene la cantidad de zonas, segun la comuna.
	 * 
	 * @param  id_comuna long 
	 * @return int 
	 * @throws PedidosDAOException
	 */
	/*public int getLocalesByIdComuna(long id_comuna) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		int result = 0;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en getLocalesByIdComuna");
			String sql = "SELECT count(id_zona) cantidad FROM bo_comunaxzona " +
					" WHERE id_comuna = ? ";
			//String sql = "";
			logger.debug(sql);
			logger.debug("id_comuna:"+id_comuna);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt("cantidad");
			}
			rs.close();
			stm.close();
			releaseConnection();
			
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("result?"+result);
		return result;
	}*/

	/**
	 * Obtiene la cantidad de productos de un pedido, segun la lógica de unidad de medida.
	 * 
	 * @param  id_pedido long 
	 * @return int 
	 * @throws PedidosDAOException
	 */
	public int  valMaxNumProductosByPedido(long id_pedido) throws PedidosDAOException {
		
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		int cantidad = 0; 
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en valMaxNumProductosByPedido");
			String sql = "SELECT id_pedido,  count(D.id_producto) cantidad " +
					" FROM bo_detalle_pedido D " +
					" JOIN bo_productos P1 ON P1.id_producto = D.id_producto AND P1.uni_med = 'ST' " +
					" WHERE D.cant_solic = 1 AND D.id_pedido = ? " +
					" GROUP BY id_pedido " +
					" UNION ALL " +
					" SELECT id_pedido,  2*count(D.id_producto) cantidad " +
					" FROM bo_detalle_pedido D " +
					" JOIN bo_productos P1 ON P1.id_producto = D.id_producto " +
					" WHERE NOT( D.cant_solic = 1 AND P1.uni_med = 'ST' ) AND D.id_pedido = ? " +
					" GROUP BY id_pedido ";
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			stm.setLong(2, id_pedido);
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				cantidad += rs.getInt("cantidad");
			}

			
		}catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+cantidad);
		return cantidad;
	}

	/**
	 * Obtiene datos del producto sap.
	 * 
	 * @param  id_prod long 
	 * @return ProductoSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public ProductoSapEntity getProductSapById(long id_prod) throws ProductosSapDAOException{
		ProductoSapEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductSapById:");
		
		try {
			String sql = " SELECT id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, " + 
				" des_larga, estado, marca, cod_proppal, origen, un_base, ean13,  " +
				" un_empaque, num_conv, den_conv, atrib9, atrib10, fcarga, mix_web, P.estadoactivo as est, descat, " +
				" substr(P.id_catprod,1,2) seccion, substr(P.id_catprod,3,2) rubro, " +
				" substr(P.id_catprod,5,2) subrubro, substr(P.id_catprod,7) grupo " +
				" FROM bo_productos P, bo_catprod C " +
				" WHERE C.id_catprod = P.id_catprod AND id_producto = ? ";
			/*			String sql = " SELECT id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, " +
				" des_larga, estado, marca, cod_proppal, origen, un_base, ean13, " +
				" un_empaque, num_conv, den_conv, atrib9, atrib10, fcarga, mix_web, P.estadoactivo as est, descat " + 
				" FROM bo_productos P, bo_catprod C" + 
				" WHERE C.id_catprod = P.id_catprod AND id_producto = ? ";*/
			logger.debug(sql);
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			
			int i=0;
			if (rs.next()) {
				prod = new ProductoSapEntity();
				prod.setId( new Long(rs.getString("id_producto")));
				prod.setId_cat(rs.getString("id_cat"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setCod_prod_1(rs.getString("cod_prod1"));
				prod.setCod_prod_2(rs.getString("cod_prod2"));
				prod.setDes_corta(rs.getString("des_corta"));
				prod.setDes_larga(rs.getString("des_larga"));
				prod.setEstado(rs.getString("estado"));
				if(rs.getString("marca")!=null)
					prod.setMarca(rs.getString("marca"));
				else
					prod.setMarca("");
				prod.setCod_propal(rs.getString("cod_proppal"));
				if(rs.getString("origen")!=null)
					prod.setOrigen(rs.getString("origen"));
				else
					prod.setOrigen("");
				prod.setUn_base(rs.getString("un_base"));
				prod.setEan13(rs.getString("ean13"));
				if(rs.getString("un_empaque")!=null)
					prod.setUn_empaque(rs.getString("un_empaque"));
				else
					prod.setUn_empaque("");
				if(rs.getString("num_conv")!=null)
					prod.setNum_conv(new Integer(rs.getString("num_conv")));
				else
					prod.setNum_conv(new Integer(0));
				if(rs.getString("den_conv")!=null)
					prod.setDen_conv(new Integer(rs.getString("den_conv")));
				else
					prod.setDen_conv(new Integer(0));
				prod.setAtrib9(rs.getString("atrib9"));
				prod.setAtrib10(rs.getString("atrib10"));
				if(rs.getString("fcarga")!=null)
					prod.setFecCarga(rs.getTimestamp("fcarga"));
				else
					prod.setFecCarga(null);
				prod.setMixWeb(rs.getString("mix_web"));
				prod.setEstActivo(rs.getString("est"));
				prod.setNom_cat_sap(rs.getString("descat"));				

				prod.setSeccion(rs.getString("seccion"));
				prod.setRubro(rs.getString("rubro"));
				prod.setSubrubro(rs.getString("subrubro"));
				prod.setGrupo(rs.getString("grupo"));
				
				i++;
			}

			if(i==0) 
				throw new  ProductosSapDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return prod;
	}

	/**
	 * Obtiene los precios de un producto 
	 * 
	 * @param  id_prod long 
	 * @return List PrecioSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getPreciosByProdId(long id_prod) throws ProductosSapDAOException {
		
		List lista_precios = new ArrayList();
		PrecioSapEntity prec = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getPreciosByProdId:");
		
		try {

			String sql = " SELECT id_local, id_producto, cod_prod1, cod_local, prec_valor, umedida, " +
					"cod_barra, estadoactivo " + 
				" FROM bo_precios WHERE id_producto = ? AND bloq_compra = 'NO' AND estadoactivo='1' "; 
				//agregar la condición de mix_local , debe ser igual a 1
				//" AND mix_local=1 ";
			logger.debug(sql);
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				prec = new PrecioSapEntity();
				prec.setId_loc(new Long(rs.getString("id_local")));
				prec.setId_prod(new Long(rs.getString("id_producto")));
				prec.setCod_prod_1(rs.getString("cod_prod1"));
				prec.setCod_local(rs.getString("cod_local"));
				prec.setPrec_valor(new Double(rs.getString("prec_valor")));
				prec.setUni_med(rs.getString("umedida"));
				prec.setCod_barra(rs.getString("cod_barra"));
				prec.setEst_act(rs.getString("estadoactivo"));
				lista_precios.add(prec);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_precios.size());
		return lista_precios;
	}

	/**
	 * Obtiene los códigos de barra de un producto 
	 * 
	 * @param  id_prod long 
	 * @return List CodBarraSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getCodBarrasByProdId(long id_prod) throws ProductosSapDAOException {
		
		List lista_codBarr = new ArrayList();
		CodBarraSapEntity codBarra = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCodBarrasByProdId:");
		
		try {

			String sql = " SELECT cod_prod1, cod_barra, tip_codbar, cod_ppal, unid_med, id_producto, estadoactivo " + 
				" FROM bo_codbarra WHERE id_producto = ? AND estadoactivo = '1' " ;
			logger.debug(sql);
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				codBarra = new CodBarraSapEntity();
				codBarra.setCod_prod_1(rs.getString("cod_prod1"));
				codBarra.setCod_barra(rs.getString("cod_barra"));
				codBarra.setTip_cod_barra(rs.getString("tip_codbar"));
				codBarra.setCod_ppal(rs.getString("cod_ppal"));
				codBarra.setUni_med(rs.getString("unid_med"));
				codBarra.setId_prod(new Long(rs.getString("id_producto")));
				codBarra.setEstado(rs.getString("estadoactivo"));
				lista_codBarr.add(codBarra);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_codBarr.size());
		return lista_codBarr;
	}
	
	/**
	 * Obtiene los sectores de productos del pedido, segun pedido y local. 
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO 
	 * @throws PedidosDAOException
	 */
	public List getSectorByDetPedido(long id_pedido) throws PedidosDAOException {
		
		List lst_secXDetPed = new ArrayList();
		ProductosPedidoDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getSectorByDetPedido:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
			String sql = "select dp.id_pedido, dp.id_detalle, DP.ID_SECTOR "
                       + "from bo_detalle_pedido dp "
                       + "where dp.id_pedido = ? "
                       + "order by dp.id_detalle"; 
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				det = new ProductosPedidoDTO();
				det.setId_pedido(id_pedido);
				det.setId_detalle(rs.getLong("id_detalle"));
				det.setId_sector(rs.getLong("id_sector"));
				lst_secXDetPed.add(det);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_secXDetPed.size());
		return lst_secXDetPed;
	}

	/**
	 * Obtiene datos del usuario. 
	 * 
	 * @param  id_usuario long 
	 * @return UsuariosEntity  
	 * @throws UsuariosDAOException
	 */
	public UsuariosEntity getUsuarioById(long id_usuario) throws UsuariosDAOException {
		UsuariosEntity usu = new UsuariosEntity();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String sql =
			"SELECT id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado, " +
			" u.id_local AS id_local, coalesce(id_pedido,0) id_pedido, nom_local " +			
			" FROM bo_usuarios u " +
			" JOIN bo_locales l on l.id_local=u.id_local " +
			" WHERE id_usuario = ? " ;
		
		logger.debug("getUserById");
		logger.debug("SQL: " + sql + ", id:" + id_usuario);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id_usuario);
			rs = stm.executeQuery();
			while (rs.next()) {
				usu = new UsuariosEntity();
				usu.setIdUsuario(rs.getLong("id_usuario"));
				usu.setLogin(rs.getString("login"));
				usu.setPass(rs.getString("pass"));
				usu.setNombre(rs.getString("nombre"));
				usu.setApe_paterno(rs.getString("apellido"));
				usu.setApe_materno(rs.getString("apellido_mat"));
				usu.setEmail(rs.getString("email"));
				usu.setEstado(rs.getString("estado"));
				usu.setId_local(rs.getLong("id_local"));
				usu.setId_pedido(rs.getLong("id_pedido"));
				usu.setLocal(rs.getString("nom_local"));
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new UsuariosDAOException(String.valueOf(e.getErrorCode()),e);
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return usu;
	}

	
	// ************************** Nuevos Métodos para el Front Office ******************************** //

	/**
	 * Retorna el id_pedido del último pedido cursado por un cliente. Si no encuentra
	 * ningún pedido, entonces retorna 0
	 * 
	 * @param  id_cliente long 
	 * @return long id_pedido
	 * @throws PedidosDAOException
	 */
	public long getUltimoIdPedidoCliente(long id_cliente) throws PedidosDAOException {
		List result = new ArrayList();
		
		Connection 			con 		= null;
		PreparedStatement 	stm 		= null;
		ResultSet 			rs			= null;

		long				id_pedido 	= 0;
		
		String SQLStmt =
			"SELECT MAX(id_pedido) AS id_pedido FROM bo_pedidos WHERE id_cliente = ? and id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
		
		try {

			con = this.getConnection();
			stm = con.prepareStatement( SQLStmt + " WITH UR");
			
			stm.setLong(1,id_cliente);
			logger.debug("SQL: " + SQLStmt);
			logger.debug("id_cliente: " + id_cliente);

			rs = stm.executeQuery();
			while (rs.next()) {
				id_pedido = rs.getLong( "id_pedido" );
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return id_pedido;
		
	}

	/**
	 * Actualiza datos del cliente
	 * 
	 * @param  cliente ClienteEntity 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 */
	public boolean actClienteById(ClienteEntity cliente) throws PedidosDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setModEstadoPedido");
			String sql = "UPDATE  fo_clientes SET cli_mod_dato = ? WHERE cli_id = ? ";
			logger.debug(sql);
			logger.debug("cli_id:"+cliente.getId().longValue());
			logger.debug("cli_mod_dato:"+cliente.getMod_dato());
			
			stm = conn.prepareStatement(sql);

			stm.setString(1, cliente.getMod_dato());
			stm.setLong(2, cliente.getId().longValue());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene los productos del pedido por sector
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProdPedidoXSector(long id_pedido, long id_sector, long id_local) throws PedidosDAOException {
		List lst_prods = new ArrayList();
		ProductosPedidoDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getProdPedidoXSector:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
			String sql = "SELECT id_detalle, id_producto, cod_prod1, descripcion, uni_med, cant_solic, "
                       + "       cant_faltan, cant_pick, cant_spick, nombre "
                       + "FROM BO_DETALLE_PEDIDO d "
                       + "     JOIN BO_SECTOR s ON s.id_sector = d.id_sector "
                       + "WHERE d.id_sector = ?  AND d.id_pedido = ? "
                       + "ORDER BY cant_spick ASC "; 
				
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("id_sector:"+id_sector);
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_sector);
			stm.setLong(2,id_pedido);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				det = new ProductosPedidoDTO();
				det.setId_pedido(id_pedido);
				det.setId_detalle(rs.getLong("id_detalle"));
				det.setId_producto(rs.getLong("id_producto"));
				det.setCod_producto(rs.getString("cod_prod1"));
				det.setDescripcion(rs.getString("descripcion"));
				det.setUnid_medida(rs.getString("uni_med"));
				det.setCant_solic(rs.getDouble("cant_solic"));
				det.setCant_pick(rs.getDouble("cant_pick"));
				det.setCant_faltan(rs.getDouble("cant_faltan"));
				det.setCant_spick(rs.getDouble("cant_spick"));
				det.setSector(rs.getString("nombre"));
				lst_prods.add(det);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_prods.size());
		return lst_prods;
	}

	
	/**
	 * Obtiene los productos del pedido por sector
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProdSinPickearXPedidoXSector(long id_pedido, long id_sector, long id_local) throws PedidosDAOException {
		List lst_prods = new ArrayList();
		ProductosPedidoDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getProdSinPickearXPedidoXSector:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
		    String Sql = "SELECT DP.ID_DETALLE, DP.ID_PRODUCTO, DP.COD_PROD1, DP.DESCRIPCION, DP.UNI_MED, "
                       + "       DP.CANT_SOLIC, DP.CANT_FALTAN, DP.CANT_PICK, DP.CANT_SPICK, S.NOMBRE AS SECTOR, "
                       + "       CASE WHEN (COALESCE(U.NOMBRE, '') <> '') THEN "
                       + "          CONCAT(CONCAT(CONCAT(CONCAT(RTRIM(U.NOMBRE), ' '), RTRIM(U.APELLIDO_MAT)), ' '), RTRIM(U.APELLIDO)) "
                       + "       ELSE "
                       + "          '---' "
                       + "       END AS USUARIO "
                       + "FROM BODBA.BO_DETALLE_PEDIDO DP " 
                       + "     JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR = DP.ID_SECTOR "
                       + "     LEFT JOIN (SELECT MAX(DR2.ID_DRONDA) AS ID_DRONDA, DR2.ID_DETALLE "
                       + "                FROM BODBA.BO_DETALLE_PEDIDO DP2 "
                       + "                     JOIN BODBA.BO_DETALLE_RONDAS DR2 ON DR2.ID_DETALLE = DP2.ID_DETALLE "
                       + "                     JOIN BODBA.BO_RONDAS R2 ON R2.ID_RONDA = DR2.ID_RONDA "
                       + "                WHERE DP2.ID_SECTOR = " + id_sector + " "
                       + "                  AND DP2.ID_PEDIDO = " + id_pedido + " "  
                       + "                  AND DP2.CANT_SPICK > 0 "
                       + "                GROUP BY DR2.ID_DETALLE "
                       + "                ORDER BY DR2.ID_DETALLE ASC) PICK ON PICK.ID_DETALLE = DP.ID_DETALLE "
                       + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DRONDA = PICK.ID_DRONDA "
                       + "     LEFT JOIN BODBA.BO_RONDAS R ON R.ID_RONDA = DR.ID_RONDA "
                       + "     LEFT JOIN BODBA.BO_USUARIOS U ON U.ID_USUARIO = R.ID_USUARIO "
                       + "WHERE DP.ID_SECTOR = " + id_sector + " "
                       + "  AND DP.ID_PEDIDO = " + id_pedido + " " 
                       + "  AND DP.CANT_SPICK > 0 "
                       + "GROUP BY DP.ID_DETALLE, DP.ID_PRODUCTO, DP.COD_PROD1, DP.DESCRIPCION, DP.UNI_MED, " 
                       + "         DP.CANT_SOLIC, DP.CANT_FALTAN, DP.CANT_PICK, DP.CANT_SPICK, S.NOMBRE, "
                       + "         U.NOMBRE, U.APELLIDO_MAT, U.APELLIDO "
                       + "ORDER BY DP.CANT_SPICK ASC";
		    logger.debug(Sql);
			logger.debug("id_pedido:"+id_pedido);
			logger.debug("id_sector:"+id_sector);
			logger.debug("id_local:"+id_local);
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			/*stm.setLong(1,id_local);
			stm.setLong(2,id_sector);
			stm.setLong(3,id_pedido);*/
			
			rs = stm.executeQuery();
			while (rs.next()) {
				det = new ProductosPedidoDTO();
				det.setId_pedido(id_pedido);
				det.setId_detalle(rs.getLong("ID_DETALLE"));
				det.setId_producto(rs.getLong("ID_PRODUCTO"));
				det.setCod_producto(rs.getString("COD_PROD1"));
				det.setDescripcion(rs.getString("DESCRIPCION"));
				det.setUnid_medida(rs.getString("UNI_MED"));
				det.setCant_solic(rs.getDouble("CANT_SOLIC"));
				det.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				det.setCant_pick(rs.getDouble("CANT_PICK"));
				det.setCant_spick(rs.getDouble("CANT_SPICK"));
				det.setSector(rs.getString("SECTOR"));
				det.setNombreUsuario(rs.getString("USUARIO"));
				lst_prods.add(det);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_prods.size());
		return lst_prods;
	}

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	/*public JornadaAvanceDTO getAvanceJornada(long id_jornada_picking) throws PedidosDAOException {
		List lst_prods = new ArrayList();
		JornadaAvanceDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvanceJornada:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
			String sql = "SELECT  COALESCE(SUM(DP.CANT_SOLIC), 0) AS CANT_SOLIC, " 
                       + "        COALESCE(SUM(DP.CANT_PICK), 0)  AS CANT_PICK,  COALESCE(DECIMAL(DOUBLE(SUM(DP.CANT_PICK)*100)/SUM(DP.CANT_SOLIC), 6,2), 0)  AS PORC_PICK, "
                       + "        COALESCE(SUM(DP.CANT_SPICK), 0) AS CANT_SPICK, COALESCE(DECIMAL(DOUBLE(SUM(DP.CANT_SPICK)*100)/SUM(DP.CANT_SOLIC), 6,2), 0) AS PORC_SPICK "
                       + "FROM BODBA.BO_JORNADAS_PICK JP "
                       + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING "
                       + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_PEDIDO  = P.ID_PEDIDO "
                       + "WHERE JP.ID_JPICKING = ? "; 
				
			logger.debug(sql);
			logger.debug("id_jornada_picking:"+id_jornada_picking);

			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_jornada_picking);
	
			rs = stm.executeQuery();
			while (rs.next()){
				det = new JornadaAvanceDTO();
				System.out.println(rs.getString("CANT_SOLIC"));
				System.out.println(rs.getString("CANT_PICK"));
				System.out.println(rs.getString("PORC_PICK"));
				System.out.println(rs.getString("CANT_SPICK"));
				System.out.println(rs.getString("PORC_SPICK"));
				det.setCant_prod_solicitados(rs.getDouble("CANT_SOLIC"));
				det.setCant_prod_en_bodega(rs.getDouble("CANT_PICK"));
				det.setPorc_prod_en_bodega(rs.getDouble("PORC_PICK"));
				det.setCant_prod_sin_pickear(rs.getDouble("CANT_SPICK"));
				det.setPorc_prod_sin_pickear(rs.getDouble("PORC_SPICK"));
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return det;
	}*/

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public AvanceDTO getAvanceJornada(long id_jornada_picking) throws PedidosDAOException {
//		List lst_prods = new ArrayList();
		AvanceDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvanceJornada:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
			/*String SQL = "SELECT COALESCE(R.ID_ESTADO, 0) AS ESTADO_RONDA, "
                       + "       CASE WHEN COALESCE(R.ID_ESTADO, 0) = 13 THEN "
                       + "                 COALESCE(SUM(DR.CANTIDAD), 0) - COALESCE(SUM(DR.CANT_SPICK), 0) "
                       + "            ELSE " 
                       + "                 COALESCE(SUM(DP.CANT_SOLIC), 0) "
                       + "       END AS CANT_PRODUCTOS "
                       + "FROM BODBA.BO_JORNADAS_PICK JP "
                       + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING AND P.ID_ESTADO NOT IN (3,4) "
                       + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_PEDIDO  = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS DR ON DP.ID_DETALLE = DR.ID_DETALLE "
                       + "     LEFT JOIN BODBA.BO_RONDAS         R  ON DR.ID_RONDA   = R.ID_RONDA "
                       + "WHERE JP.ID_JPICKING = ? "
                       + "GROUP BY R.ID_ESTADO"; */
		    /*String SQL = "SELECT COALESCE(R.ID_ESTADO, 0) AS ESTADO_RONDA, " 
                       + "       COALESCE(SUM(DP.CANT_SOLIC), 0) AS CANT_SOLIC, "
                       + "       COALESCE(SUM(DR.CANT_SPICK), 0) AS CANT_SPICK, "
                       + "       COALESCE(SUM(DR.CANTIDAD), 0) - COALESCE(SUM(DR.CANT_SPICK), 0) AS CANT_PICK "
                       + "FROM BODBA.BO_JORNADAS_PICK JP "
                       + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING AND P.ID_ESTADO NOT IN (3,4) "
                       + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_PEDIDO  = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS DR ON DP.ID_DETALLE = DR.ID_DETALLE "
                       + "     LEFT JOIN BODBA.BO_RONDAS         R  ON DR.ID_RONDA   = R.ID_RONDA "
                       + "WHERE JP.ID_JPICKING = ? "
                       + "GROUP BY R.ID_ESTADO";*/
			String SQL = "SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC, "
                       + "       COALESCE(SUM(DP.CANT_PICK), 0) AS CANT_PICK, "
                       + "       COALESCE(SUM(DP.CANT_FALTAN), 0) AS CANT_FALT "
                       + "FROM BODBA.BO_JORNADAS_PICK JP " 
                       //+ "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING AND P.ID_ESTADO NOT IN (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ",3,4) "
                       //Maxbell - Homologacion pantallas BOL
                       //+ "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING AND P.ID_ESTADO NOT IN (1,20,68,69,7,3,4) "
                       //Maxbell arreglo homologacion
                       + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_JPICKING = JP.ID_JPICKING AND P.ID_ESTADO NOT IN (1,20,68,69,3,4) "
                       + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_PEDIDO  = P.ID_PEDIDO "
                       + "WHERE JP.ID_JPICKING = ? "+
                       //" AND (P.TIPO_VE = 'N' OR P.TIPO_VE IS NULL) ";
                       " AND P.TIPO_VE <> 'S' ";
			logger.debug(SQL);
			logger.debug("id_jornada_picking:"+id_jornada_picking);

			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1,id_jornada_picking);
			rs = stm.executeQuery();
			
			double total_productos= 0d;
			double prod_bodega    = 0d;
			double prod_faltantes = 0d;
			double porc_prod_bodega=0d;
			double prod_sin_pickear=0d;
			double porc_prod_sin_pickear=0d;
			
			
			while (rs.next()){
				//System.out.println(rs.getString("CANT_PRODUCTOS"));
				//System.out.println(rs.getString("ESTADO_RONDA"));
				
                /*total_productos+=rs.getDouble("CANT_PRODUCTOS");
				if (rs.getInt("ESTADO_RONDA") == 13){//Ronda Terminada
				    prod_bodega+=rs.getDouble("CANT_PRODUCTOS");
				}else{
				    prod_sin_pickear+=rs.getDouble("CANT_PRODUCTOS");//Producto Sin Pickear o En Picking
				}*/
			    
				/*total_productos+=rs.getDouble("CANT_SOLIC");
				if (rs.getInt("ESTADO_RONDA") == 13){//Ronda Terminada
				    prod_bodega+=rs.getDouble("CANT_PICK");
				}else{
				    prod_sin_pickear+=rs.getDouble("CANT_SOLIC");//Producto Sin Pickear o En Picking
				}*/
				total_productos+=rs.getDouble("CAT_SOLIC");
				prod_bodega+=rs.getDouble("CANT_PICK");
				prod_faltantes+=rs.getDouble("CANT_FALT");
				//Producto Sin Pickear o En Picking
                prod_sin_pickear+=total_productos - (prod_bodega + prod_faltantes);				

			}


			if (total_productos > 0.0){
			    //porc_prod_bodega      = (prod_bodega * 100)/total_productos;
			    //porc_prod_sin_pickear = (prod_sin_pickear * 100)/total_productos;
			    porc_prod_bodega      = ((prod_bodega + prod_faltantes) * 100)/total_productos;
			    porc_prod_sin_pickear = (prod_sin_pickear * 100)/total_productos;
				det = new AvanceDTO();
				det.setCant_prod_solicitados(total_productos);
				det.setCant_prod_en_bodega(prod_bodega);
				det.setPorc_prod_en_bodega(porc_prod_bodega);
				det.setCant_prod_sin_pickear(prod_sin_pickear);
				det.setPorc_prod_sin_pickear(porc_prod_sin_pickear);
			}
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return det;
	}

	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public AvanceDTO getAvancePedido(long id_pedido) throws PedidosDAOException {
//		List lst_prods = new ArrayList();
		AvanceDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvancePedido:");
		
		try {

			//Consulta con la nueva tabla bo_prod_sector
			/*String sql = "SELECT COALESCE(R.ID_ESTADO, 0) AS ESTADO_RONDA, "
                       + "       CASE WHEN COALESCE(R.ID_ESTADO, 0) = 13 THEN "
                       + "                 COALESCE(SUM(DR.CANTIDAD), 0) - COALESCE(SUM(DR.CANT_SPICK), 0) "
                       + "            ELSE " 
                       + "                 COALESCE(SUM(DP.CANT_SOLIC), 0) "
                       + "       END AS CANT_PRODUCTOS "
                       + "FROM BODBA.BO_PEDIDOS P "
                       + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_PEDIDO  = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS DR ON DP.ID_DETALLE = DR.ID_DETALLE "
                       + "     LEFT JOIN BODBA.BO_RONDAS         R  ON DR.ID_RONDA   = R.ID_RONDA "
                       + "WHERE P.ID_PEDIDO = ? "
                       + "GROUP BY R.ID_ESTADO"; */
			String sql = "SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC, "
                       + "       COALESCE(SUM(DP.CANT_PICK), 0) AS CANT_PICK, "
                       + "       COALESCE(SUM(DP.CANT_FALTAN), 0) AS CANT_FALT "
                       + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                       + "WHERE DP.ID_PEDIDO = ? ";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			rs = stm.executeQuery();
			
			double total_productos=0d;
			double prod_bodega=0d;
			double prod_faltantes = 0d;
			double porc_prod_bodega=0d;
			double prod_sin_pickear=0d;
			double porc_prod_sin_pickear=0d;
			
			
			while (rs.next()){
				//System.out.println(rs.getString("CANT_PRODUCTOS"));
				//System.out.println(rs.getString("ESTADO_RONDA"));
				
				/*total_productos+=rs.getDouble("CANT_PRODUCTOS");
				if (rs.getInt("ESTADO_RONDA") == 13){//Ronda Terminada
				    prod_bodega+=rs.getDouble("CANT_PRODUCTOS");
				}else{
				    prod_sin_pickear+=rs.getDouble("CANT_PRODUCTOS");//Producto Sin Pickear o En Picking
				}*/
				total_productos+=rs.getDouble("CAT_SOLIC");
				prod_bodega+=rs.getDouble("CANT_PICK");
				prod_faltantes+=rs.getDouble("CANT_FALT");
				//Producto Sin Pickear o En Picking
                prod_sin_pickear+=total_productos - (prod_bodega + prod_faltantes);				
			}


			if (total_productos > 0.0){
			    porc_prod_bodega      = ((prod_bodega + prod_faltantes) * 100)/total_productos;
			    porc_prod_sin_pickear = (prod_sin_pickear * 100)/total_productos;
				det = new AvanceDTO();
				det.setCant_prod_solicitados(total_productos);
				det.setCant_prod_en_bodega(prod_bodega);
				det.setPorc_prod_en_bodega(porc_prod_bodega);
				det.setCant_prod_sin_pickear(prod_sin_pickear);
				det.setPorc_prod_sin_pickear(porc_prod_sin_pickear);
			}
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return det;
	}
	/**(+) INDRA (+)
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	
	//Maxbell arreglo homologacion
	public int getCantidadOpPasadosPorBodega(long id_jornada) {
		int cantidad = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String sql = "select count(id_pedido) as cantidad from bo_pedidos where id_jpicking = ? and id_estado in(7,8,9,10,54) AND TIPO_VE <> 'S' ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_jornada);
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return cantidad;
	}
	
	public AvanceDTO getAvanceUmbralPedido(long id_pedido) throws PedidosDAOException {
//		List lst_prods = new ArrayList();
		AvanceDTO det = null;

		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvanceUmbralPedido:");
		
		try {

			
			String sql = "SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC, "
                       + "       COALESCE(SUM(DP.CANT_PICK), 0) AS CANT_PICK, "
                       + "       COALESCE(SUM(DP.CANT_FALTAN), 0) AS CANT_FALT, "
                       + "       COALESCE(SUM(DP.PRECIO), 0) AS PRECIO "
                       + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                       + "WHERE DP.ID_PEDIDO = ? ";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			rs = stm.executeQuery();
			
			double total_productos=0d;
			double prod_bodega=0d;
			double prod_faltantes = 0d;
			double porc_prod_bodega=0d;
			double prod_sin_pickear=0d;
			double porc_prod_pickeados=0d;
			double precio=0d;
			
			while (rs.next()){
				//System.out.println(rs.getString("CANT_PRODUCTOS"));
				//System.out.println(rs.getString("ESTADO_RONDA"));
				
				/*total_productos+=rs.getDouble("CANT_PRODUCTOS");
				if (rs.getInt("ESTADO_RONDA") == 13){//Ronda Terminada
				    prod_bodega+=rs.getDouble("CANT_PRODUCTOS");
				}else{
				    prod_sin_pickear+=rs.getDouble("CANT_PRODUCTOS");//Producto Sin Pickear o En Picking
				}*/
				total_productos+=rs.getDouble("CAT_SOLIC");//Cant de productos solicitados
				prod_bodega+=rs.getDouble("CANT_PICK");// cant de prod pickeados
				prod_faltantes+=rs.getDouble("CANT_FALT");// cant de prod sin pickear
				precio+=rs.getDouble("PRECIO");//precio total del pedido (todos los prod pickeados y sin pickear)
				
				//Producto Sin Pickear o En Picking
              //  prod_sin_pickear+=total_productos - (prod_bodega + prod_faltantes);				
			}


			if (total_productos > 0.0){
			  //  porc_prod_bodega      = ((prod_bodega + prod_faltantes) * 100)/total_productos;
			    porc_prod_pickeados = (prod_bodega * 100)/total_productos;//porcentaje de productos pickeados
				det = new AvanceDTO();
				det.setCant_prod_solicitados(total_productos);
				det.setCant_prod_en_bodega(prod_bodega);
				//det.setPorc_prod_en_bodega(porc_prod_bodega);
				//det.setCant_prod_sin_pickear(prod_sin_pickear);
				det.setPorc_prod_pickeados(porc_prod_pickeados);
				det.setPrecio_total(precio);
				
			}
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return det;
	}
	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public AvanceDTO getAvanceUmbralMonto(long id_pedido) throws PedidosDAOException {
//		List lst_prods = new ArrayList();
		AvanceDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvanceUmbralMonto:");
		
		try {

			
			String sql = " SELECT SUM(DP.PRECIO) AS PRECIO_PICKEADOS "
                       + " FROM BODBA.BO_DETALLE_PEDIDO DP "
                       + " WHERE DP.ID_PEDIDO = ? and DP.CANT_PICK > 0";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			rs = stm.executeQuery();
			
			double total_productos=0d;
			double prod_bodega=0d;
			double prod_faltantes = 0d;
			double porc_prod_bodega=0d;
			double prod_sin_pickear=0d;
			double porc_prod_pickeados=0d;
			double precio_pickeado=0d;
			
			while (rs.next()){
				
				precio_pickeado+=rs.getDouble("PRECIO_PICKEADOS");
								
			}


			if (total_productos > 0.0){
			  //  porc_prod_bodega      = ((prod_bodega + prod_faltantes) * 100)/total_productos;
			    porc_prod_pickeados = (prod_bodega * 100)/total_productos;
				det = new AvanceDTO();
				
				det.setPrecio_total_pickeado(precio_pickeado);
				
			}
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return det;
	}
	/**
	 * Obtiene cantidad de umbrales de la tabla para ver si se pasa el pedido a bodega o no
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public AvanceDTO getAvanceUmbralParametros(long id_pedido) throws PedidosDAOException {
     //	List lst_prods = new ArrayList();
		AvanceDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getAvanceUmbralParametros:");
		
		try {

			//Consulta con la nueva tabla bo_umbrales
			
			String sql = "  SELECT UMB.U_MONTO AS MONTO, UMB.U_UNIDAD AS UNIDAD "
                       + " FROM BO_UMBRALES UMB , BO_PEDIDOS PED "
                       + " WHERE UMB.ID_LOCAL = PED.ID_LOCAL "
                       + " AND PED.ID_PEDIDO =? "
                       + " AND UMB.U_ACTIVACION ='S' ";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			rs = stm.executeQuery();
			
			double total_unidad=0d;
			double total_monto=0d;
			
			
			
			while (rs.next()){
				
				total_unidad+=rs.getDouble("UNIDAD");
				total_monto+=rs.getDouble("MONTO");
							
			}


				det = new AvanceDTO();
				det.setCant_unidad_umbrales(total_unidad);
				det.setCant_monto_umbrales(total_monto);
						
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return det;
	}
//(-) INDRA  2012-12-12(-)
	/**
	 * Obtiene el listado de los productos sueltos de un pedido, 
	 * que se encuentran contenidos en el bin de tipo Virtual
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProductosSueltosByPedidoId(long id_pedido) throws PedidosDAOException {
		List lst_prods = new ArrayList();
		ProductosPedidoDTO det = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getProductosSueltosByPedidoId:");
		
		try {
			String sql = "SELECT d.id_detalle id_detalle, d.id_producto as id_producto, " +
					"d.cod_prod1 as cod_prod1, bp.COD_PROD1, " +
					"case when bp.id_producto is null then 'SUSTITUIDO POR EL MISMO PRODUCTO - ' || VARCHAR(p.cbarra) else bp.des_larga end as descripcion, " +
					"d.uni_med uni_med, d.cant_solic cant_solic, d.cant_faltan cant_faltan, " +
					"p.cant_pick cant_pick, d.cant_spick cant_spick	" +
					"FROM bodba.BO_DETALLE_PEDIDO d " +
					"JOIN bodba.BO_detalle_picking p ON p.id_detalle = d.id_detalle " +
					"JOIN bodba.BO_bins_pedido b ON b.id_pedido = d.id_pedido AND b.id_bp = p.id_bp " +
					"LEFT JOIN bodba.BO_PRODUCTOS as bp on bp.ID_PRODUCTO = p.ID_PRODUCTO " +
					"where b.TIPO = '" + Constantes.TIPO_BIN_VIRTUAL + "' and d.ID_PEDIDO = ?";
			
			logger.debug(sql);
			logger.debug("id_pedido:"+id_pedido);
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_pedido);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				det = new ProductosPedidoDTO();
				det.setId_pedido(id_pedido);
				det.setId_detalle(rs.getLong("id_detalle"));
				det.setId_producto(rs.getLong("id_producto"));
				det.setCod_producto(rs.getString("cod_prod1"));
				det.setDescripcion(rs.getString("descripcion"));
				det.setUnid_medida(rs.getString("uni_med"));
				det.setCant_solic(rs.getDouble("cant_solic"));
				det.setCant_pick(rs.getDouble("cant_pick"));
				det.setCant_faltan(rs.getDouble("cant_faltan"));
				det.setCant_spick(rs.getDouble("cant_spick"));
				lst_prods.add(det);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_prods.size());
		return lst_prods;
	}

	/**
	 * Entrega un Listado de Todas las Secciones (nivel 2)
	 * 
	 * @return List of CategoriaSapEntity's
	 * @throws PedidosDAOException
	 */	
	public List getSeccionesSap() throws PedidosDAOException{

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List 		lista_secc 		= new ArrayList();
		CategoriaSapEntity 	cat 	= null;
		
		logger.debug("en getCategoriasSapById:");

		String SQLStmt =
				" SELECT id_catprod, descat " +
				" FROM BODBA.BO_CATPROD " +
				" WHERE cat_nivel = " + Constantes.NIVEL_SECCION;
	
		logger.debug( SQLStmt );

		
		try {

			conn 	= this.getConnection();
			stm 	= conn.prepareStatement( SQLStmt + " WITH UR" );
			rs 		= stm.executeQuery();
			
			while (rs.next()) {
				cat = new CategoriaSapEntity(); 
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setDescrip(rs.getString("descat"));
				lista_secc.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	
		logger.debug("cant en lista:"+lista_secc.size());
		return lista_secc;
	}

	/**
	 * Actualiza las cantidades del detalle de pedido
	 * 
	 * @param  dto ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */	
	public boolean actCantSinPickearDetallePedido(ProductosPedidoDTO dto) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En actCantSinPickearDetallePedido:" );
			String sql = " UPDATE bo_detalle_pedido SET cant_pick = ( coalesce(cant_pick,0) + ? ), " +
					" cant_faltan = ( coalesce(cant_faltan,0) + ? ), " +
					" cant_spick =  ? " +
					" WHERE id_detalle = ? ";

			conn = this.getConnection();

			stm = conn.prepareStatement(sql);
	
			stm.setDouble(1, dto.getCant_pick());
			stm.setDouble(2, dto.getCant_faltan());
			stm.setDouble(3, dto.getCant_spick());
			stm.setDouble(4, dto.getId_detalle());
			
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + dto.getId_detalle());
			logger.debug("cant_pick: " + dto.getCant_pick());
			logger.debug("cant_faltan: " + dto.getCant_faltan());
			logger.debug("cant_spick: " + dto.getCant_spick());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Actualiza el detalle de picking de un producto sustituto. 
	 * 
	 * @param  dto DetallePickingDTO 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PedidosDAOException
	 */	
	public boolean setDetallePickingSustituto(DetallePickingDTO dto) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En setDetallePickingSustituto:" );
			String sql = " UPDATE bo_detalle_picking " +
					" SET " +
				//	"cod_prod1 = ? , " +
					" descripcion = ? , " +
					" precio = ?  " +
					" WHERE id_dpicking = ? ";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			//stm.setString(1, dto.getCod_prod());
			stm.setString(1, dto.getDescripcion());
			stm.setDouble(2, dto.getPrecio());
			stm.setDouble(3, dto.getId_dpicking());
			
			logger.debug("SQL: " + sql);
			logger.debug("id_dpicking: " + dto.getId_dpicking());
			//logger.debug("CBarra: " + dto.getCBarra());
			logger.debug("Codigo Sap: " + dto.getCod_prod());
			logger.debug("descripcion: " + dto.getDescripcion());
			logger.debug("precio: " + dto.getPrecio());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
			logger.debug("actualizo?"+result);
			

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene datos del detalle de picking. 
	 * 
	 * @param  id_detalle long 
	 * @return DetallePickingEntity
	 * @throws PedidosDAOException
	 */	
	public DetallePickingEntity getDetPickingById(long id_detalle) throws PedidosDAOException {
		DetallePickingEntity dpick = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT 	dp.id_dpicking, dp.id_detalle, dp.id_bp, dp.id_producto, dp.id_pedido, dp.cbarra,"+
						 "		dp.descripcion, dp.precio, dp.cant_pick, dp.sustituto "+ 
						 "	FROM 	bo_detalle_picking dp"+
						 "	WHERE dp.id_dpicking = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_detalle);
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_detalle);

			rs = stm.executeQuery();
			if (rs.next()) {
				dpick = new DetallePickingEntity();
				dpick.setId_dpicking(rs.getLong("id_dpicking"));
				dpick.setId_detalle(rs.getLong("id_detalle"));
				dpick.setId_bp(rs.getLong("id_bp"));
				dpick.setId_producto(rs.getLong("id_producto"));
				dpick.setId_pedido(rs.getLong("id_pedido"));
				dpick.setCBarra(rs.getString("cbarra"));
				dpick.setDescripcion(rs.getString("descripcion"));
				dpick.setPrecio(new Double(rs.getDouble("precio")));
				dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
				dpick.setSustituto(rs.getString("sustituto"));
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		
		return dpick;
	}

	/**
	 * Entrega un Listado de los locales
	 * 
	 * @return List of LocalDTO
	 * @throws PedidosDAOException
	 */	
	public List getLocalesByProducto(long cod_prod) throws PedidosDAOException{

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List lista_local 		= new ArrayList();
		LocalDTO 	local 		= null;
		
		logger.debug("en getLocalesByProducto:");
		logger.debug("cod_prod: "+cod_prod);
		try {
			String SQLStmt =
					"	SELECT l.id_local, l.nom_local "+
					"	FROM bo_precios p "+
					"	JOIN bo_locales l on l.id_local = p.id_local "+
					"	JOIN bo_productos pro on pro.cod_prod1 = p.cod_prod1 "+
					"	WHERE pro.id_producto = ? "+
					"	GROUP BY l.id_local, l.nom_local ";						
		
			logger.debug( SQLStmt );
	

			conn 	= this.getConnection();
			stm 	= conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1,cod_prod);
			rs 		= stm.executeQuery();
			
			while (rs.next()) {
				local = new LocalDTO(); 
				local.setId_local(rs.getLong("id_local"));
				local.setNom_local(rs.getString("nom_local"));
				lista_local.add(local);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	
		logger.debug("cant en lista:"+lista_local.size());
		return lista_local;
	}

	/**
	 * Obtiene lista de sectores, segun producto y local
	 * 
	 * @param  id_producto
	 * @return List SectorLocalDTO
	 * @throws PedidosDAOException
	 */
	public long getProdXSector(long id_producto) throws PedidosDAOException {
		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		long sector = 0L;
		
		logger.debug("en getProdXSector:");
		try {
			String SQLStmt = "SELECT PS.ID_PRODUCTO, PS.ID_SECTOR "
                           + "FROM BODBA.BO_PROD_SECTOR PS "
                           + "WHERE PS.ID_PRODUCTO = ? ";
		
			logger.debug( SQLStmt );
			logger.debug("ID_PRODUCTO: " + id_producto);
	
			conn = this.getConnection();
			stm  = conn.prepareStatement( SQLStmt  + " WITH UR");
			
			stm.setLong(1,id_producto);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    sector = rs.getLong("ID_SECTOR");
			    logger.debug("ID_SECTOR: " + sector);
			}
		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return sector;
	}

	/**
	 * Elimina la relación entre producto y sector 
	 * 
	 * @param  id_producto
	 * @return boolean 
	 * @throws PedidosDAOException
	 */
	public boolean setDelProdXSector(long id_producto) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En setDelProdXSector:" );
			String sql = "DELETE FROM BO_PROD_SECTOR WHERE id_producto = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setLong(1, id_producto);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_producto: " + id_producto);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
			logger.debug("elimino?"+result);
			

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Agrega relación de producto y sector.
	 * 
	 * @param  id_producto
	 * @param  id_sector
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setAddProductoXSector(long id_producto, long id_sector) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		try {

			logger.debug("En setAddProductoXSector:" );
			String sql = " INSERT INTO bo_prod_sector (id_producto, id_sector) VALUES (?,?) ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setLong(1, id_producto);
			stm.setLong(2, id_sector);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_producto: " + id_producto);
			logger.debug("id_sector: " + id_sector);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
			logger.debug("agrego?"+result);
			
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica si existe id_sector
	 * 
	 * @param  id_sector
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean isSectorById(long id_sector) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		ResultSet rs = null;
		
		try {

			logger.debug("En isSectorById:" );
			String sql = " SELECT id_sector FROM bo_sector WHERE id_sector = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_sector);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_sector: " + id_sector);

			rs = stm.executeQuery();
			if(rs.next()){
				result = true;
			}
			
			logger.debug("existe?"+result);
			

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica si existe local
	 * 
	 * @param  id_local
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean isLocalById(long id_local) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		ResultSet rs = null;
		
		try {

			logger.debug("En isLocalById:" );
			String sql = " SELECT id_local FROM bo_locales " +
					" WHERE id_local = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_local);
			
			logger.debug("SQL: " + sql);
			logger.debug("id_local: " + id_local);

			rs = stm.executeQuery();
			if(rs.next()){
				result = true;
			}
			
			logger.debug("existe?"+result);
			

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Nulifica los sectores de picking para todo el detalle de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setNullSectoresDetallePedido(long id_pedido)
		throws PedidosDAOException {		
		PreparedStatement stm=null;
		boolean result=false;
		
		String SQLStmt = 
				" UPDATE bo_detalle_pedido  " +
				" SET id_sector = NULL" + 
				" WHERE id_pedido = ?";
		
		logger.debug("Ejecución DAO setNullSectoresDetallePedido");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ id_pedido);
		
		try {

			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, id_pedido);
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	
		
		return result;
	}

	/**
	 * Permite eliminar la relacion entre el pedido y la jornada de picking
	 * 
	 * @param id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean elimJPickByPedido(long id_pedido) throws PedidosDAOException {		
		PreparedStatement stm=null;
		boolean result=false;
		
		String SQLStmt = 
				" UPDATE bo_pedidos SET id_jpicking=NULL " +
				" WHERE id_pedido = ? ";
		
		logger.debug("Ejecución DAO elimJPickByPedido");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ id_pedido);
		
		try {

			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, id_pedido);
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	
		
		return result;
	}

	/**
	 * Cambia el estado de OP a pago rechazado
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setRechazaPagoOP(RechPagoOPDTO dto) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		String SQLStmt = 
			" UPDATE bo_pedidos SET id_estado = ? " +
			" WHERE id_pedido = ? ";
	
		logger.debug("Ejecución DAO setRechazaPagoOP");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ dto.getId_pedido());
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO);
			stm.setLong(2, dto.getId_pedido());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	

		return result;
	}

	/**
	 * Obtiene el listado de politicas de sustitucion
	 * 
	 * @return List PoliticaSustitucionDTO
	 * @throws PedidosDAOException
	 */
	public List getPolitSustitucionAll() throws PedidosDAOException{
		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List lst_pol_sust 		= new ArrayList();
		PoliticaSustitucionDTO polSust	= null;
		
		logger.debug("en getPolitSustitucionAll:");
		try {
			String SQLStmt =
					" SELECT sus_id, sus_nombre, sus_descripcion, sus_estado, sus_seleccion " +
					" FROM fo_politicas_sustitucion " ;
		
			logger.debug( SQLStmt );
	
			conn 	= this.getConnection();
			stm 	= conn.prepareStatement( SQLStmt  + " WITH UR");
			
			rs 		= stm.executeQuery();
			
			while (rs.next()) {
				polSust = new PoliticaSustitucionDTO(); 
				polSust.setId(rs.getLong("sus_id"));
				polSust.setNombre(rs.getString("sus_nombre"));
				polSust.setDescripcion(rs.getString("sus_descripcion"));
				polSust.setEstado(rs.getString("sus_estado"));
				polSust.setSeleccion(rs.getString("sus_seleccion"));
				lst_pol_sust.add(polSust);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_pol_sust.size());
		return lst_pol_sust;
	}

	/**
	 * Obtiene los datos de la politica de sustitucion, segun el codigo
	 * 
	 * @param  id_pol_sust
	 * @return PoliticaSustitucionDTO
	 * @throws PedidosDAOException
	 */
	public PoliticaSustitucionDTO getPolSustitById(long id_pol_sust) throws PedidosDAOException{
		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;

		PoliticaSustitucionDTO polSust	= null;
		
		logger.debug("en getPolSustitById:");
		try {
			String SQLStmt =
					" SELECT sus_id, sus_nombre, sus_descripcion, sus_estado, sus_seleccion " +
					" FROM fo_politicas_sustitucion " +
					" WHERE sus_id = ? " ;
		
			logger.debug( SQLStmt );
			logger.debug("id_pol_sust:"+id_pol_sust);
	
			conn 	= this.getConnection();
			stm 	= conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setLong(1, id_pol_sust);
			
			rs 		= stm.executeQuery();
			
			if (rs.next()) {
				polSust = new PoliticaSustitucionDTO(); 
				polSust.setId(rs.getLong("sus_id"));
				polSust.setNombre(rs.getString("sus_nombre"));
				polSust.setDescripcion(rs.getString("sus_descripcion"));
				polSust.setEstado(rs.getString("sus_estado"));
				polSust.setSeleccion(rs.getString("sus_seleccion"));
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return polSust;
	}

	/**
	 * Modifica la politica de sustitucion de un pedido
	 * 
	 * @param dto
	 * @return boolean
	 * @throws PedidosDAOException 
	 */
	public boolean setModPedidoPolSust(ProcModPedidoPolSustDTO dto) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm=null;
		
		String SQLStmt = 
			" UPDATE bo_pedidos SET pol_id = ?, pol_sustitucion = ? " +
			" WHERE id_pedido = ? ";
	
		logger.debug("Ejecución DAO setRechazaPagoOP");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ dto.getId_pedido());
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, dto.getId_pol_sust());
			stm.setString(2,dto.getDesc_pol_sust());
			stm.setLong(3, dto.getId_pedido());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/**
	 * Cambia estado del pedido a 'En Pago'
	 * 
	 * @param dto
	 * @return boolean
	 * @throws PedidosDAOException 
	 */
	public boolean setCambiarEnPagoOP(CambEnPagoOPDTO dto) throws PedidosDAOException {
		boolean result = false;

		PreparedStatement stm=null;
		
		String SQLStmt = 
			" UPDATE bo_pedidos SET id_estado = ? " +
			" WHERE id_pedido = ? ";
	
		logger.debug("Ejecución DAO setCambiarEnPagoOP");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ dto.getId_pedido());
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, Constantes.ID_ESTAD_PEDIDO_EN_PAGO);
			stm.setLong(2, dto.getId_pedido());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Cuenta la cantidad de productos sustitutos que se enviarán al cliente, 
	 * estos sustitutos corresponden a cambios en el Código SAP
	 * 
	 * @param  id_pedido
	 * @return int
	 * @throws PedidosDAOException
	 */
	public List productosSustitutosPedidoForEmail(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"productosSustitutosPedidoForEmail");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros productosSustitutosPedidoForEmail:");
		logger.debug("numero_pedido:"+id_pedido);
		
		// Se modifica Query para obtener sustitutos de Tipo 3 (sustitutos reales y no por cambio de formato)
	
		String Sql = "SELECT DISTINCT DP.ID_DETALLE ID_DETALLE, DP.COD_PROD1 COD_PROD1, DP.UNI_MED UNI_MED1, DP.DESCRIPCION DESCR1, "
                   + "       DP.CANT_SOLIC CANT1, DP.OBSERVACION OBS1, DP.PRECIO PRECIO1, " 
                   + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' " 
                   + "            ELSE P.COD_PROD1 " 
                   + "       END COD_PROD2, " 
                   + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' " 
                   + "            ELSE P.UNI_MED " 
                   + "       END UNI_MED2, " 
                   + "       PIK.DESCRIPCION DESCR2, PIK.CANT_PICK CANT2, PIK.PRECIO PRECIO2, PIK.ID_DPICKING ID_DPICKING, "
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_PICKING PIK " 
                   + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO AS DP ON PIK.ID_DETALLE = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S' " 
                   + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS AS DR ON DR.ID_DETALLE  = DP.ID_DETALLE AND DR.CANT_PICK > 0 " 
                   + "     LEFT JOIN BODBA.BO_PRODUCTOS      AS P  ON P.ID_PRODUCTO  = PIK.ID_PRODUCTO " 
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON DP.ID_CRITERIO = SC.ID_CRITERIO " 
                   + "WHERE DP.ID_PEDIDO = ? " 
                   + "  AND COALESCE(INTEGER(DP.COD_PROD1),0) <> COALESCE(INTEGER(P.COD_PROD1),0) "
                   + "  AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
                   + "  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "
                   + "                                    FROM BODBA.BO_INF_SYF SF "
                   + "                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1 "
                   + "                                      AND SF.UME_ORIGINAL      = DP.UNI_MED "
                   + "                                      AND SF.COD_PROD_SUS      = P.COD_PROD1 "
                   + "                                      AND SF.UME_SUS           = P.UNI_MED) "
                   + "ORDER BY 1, 2 ASC";
		logger.debug("SQL productosSustitutosPedidoForEmail :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				//logger.debug("entro al while");
				SustitutoDTO prod = new SustitutoDTO();
				prod.setId_detalle1(rs.getLong("ID_DETALLE"));
				prod.setId_detalle_pick1(rs.getLong("ID_DPICKING"));
				prod.setCod_prod1(rs.getString("COD_PROD1"));
				prod.setUni_med1(rs.getString("UNI_MED1"));
				prod.setCant1(rs.getDouble("CANT1"));
				prod.setDescr1(rs.getString("DESCR1"));
				prod.setObs1(rs.getString("OBS1"));
				prod.setPrecio1(rs.getDouble("PRECIO1"));
				prod.setCod_prod2(rs.getString("COD_PROD2"));
				prod.setUni_med2(rs.getString("UNI_MED2"));
				prod.setCant2(rs.getDouble("CANT2"));
				prod.setDescr2(rs.getString("DESCR2"));
				prod.setPrecio2(rs.getDouble("PRECIO2"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));             
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    prod.setDescCriterio(rs.getString("criterio"));
                }
				
				// Condición para establecer que es un sustituto por cambio de producto y no por
				// cambio de formato u otra índole
				if (!prod.getCod_prod1().equalsIgnoreCase(prod.getCod_prod2()))
					list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#productosEnviadosPedidoForEmail(int)
	 */
	public List productosEnviadosPedidoForEmail(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		CarroCompraCategoriasDTO aux_cat = null;
		List aux_lpro = null;
		List productosPorCategoria = new ArrayList();
		CarroCompraProductosDTO aux_pro = null;
		CarroCompraCategoriasDTO categoria = null;
		CarroCompraProductosDTO productos = null;
		List l_pro = null;

		try {
			String sql = "SELECT distinct cat.CAT_ID, pfo.PRO_ID, pfo.PRO_TIPO_PRODUCTO, pfo.PRO_DES_CORTA, " +
                         "pfo.PRO_COD_SAP, pfo.PRO_TIPO_SEL, pfo.PRO_TIPRE, pfo.PRO_INTER_MAX, pfo.PRO_INTER_VALOR, " +
                         "pfo.PRO_NOTA, mar.MAR_NOMBRE, dp.CANT_PICK, dp.PRECIO, cat.CAT_NOMBRE, " +
                         "case when dp.id_producto is null then '' else p.uni_med end as uni_med, uni.UNI_DESC " +
                         "FROM BODBA.bo_detalle_picking dp " +
                         "JOIN BODBA.bo_productos pbo on (dp.id_producto = pbo.ID_PRODUCTO) " +
                         "JOIN FODBA.fo_productos pfo on (pbo.ID_PRODUCTO = pfo.PRO_ID_BO) " +
                         "join ( " +
                         "        select pfo1.PRO_ID as pro_id, min(cat1.cat_id) as cat_id" +
                         "        FROM BODBA.bo_detalle_picking dp1" +
                         "        join BODBA.bo_productos pbo1 on (dp1.id_producto = pbo1.ID_PRODUCTO)" +
                         "        join FODBA.fo_productos pfo1 on (pbo1.ID_PRODUCTO = pfo1.PRO_ID_BO)" +
                         "        join FODBA.fo_productos_categorias prca1 on pfo1.pro_id = prca1.prca_pro_id" +
                         "        join FODBA.fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A'" +
                         "        join FODBA.fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id" +
                         "        join FODBA.fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A'" +
                         "        WHERE dp1.id_pedido = ? " +
                         "        group by pfo1.PRO_ID" +
                         "     ) as x on x.pro_id = pfo.PRO_ID " +
                         "join FODBA.fo_categorias cat on cat.CAT_ID = x.cat_id " +
                         "JOIN FODBA.fo_unidades_medida uni on pfo.PRO_UNI_ID = uni.UNI_ID " +
                         "JOIN FODBA.fo_marcas mar on mar.MAR_ID = pfo.PRO_MAR_ID " +
                         "LEFT JOIN BODBA.bo_bins_pedido bp on bp.id_bp = dp.id_bp " +
                         "LEFT JOIN BODBA.bo_detalle_pedido p on p.id_detalle = dp.id_detalle " +
                         "WHERE dp.id_pedido = ? " +
                         "order by cat.CAT_NOMBRE, pfo.PRO_TIPO_PRODUCTO, pfo.PRO_DES_CORTA, dp.precio";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1,id_pedido);
            stm.setLong(2,id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
			    boolean flag = true;
				for ( int i = 0; flag && i < productosPorCategoria.size(); i++ ) {
					aux_cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
					aux_lpro = aux_cat.getCarroCompraProductosDTO();
					for ( int j = 0; flag && j < aux_lpro.size(); j++ ) {
						aux_pro = (CarroCompraProductosDTO)aux_lpro.get(j);
						if ( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
						    if ( aux_cat.getId() == rs.getLong("cat_id") ) {
						        double newCantidad = aux_pro.getCantidad() + rs.getDouble("cant_pick");
						        if (NumericUtils.tieneDecimalesSignificativos(newCantidad, 3)) {
						            newCantidad = Utils.redondear(newCantidad,3);
						        }
						        ((CarroCompraProductosDTO)((CarroCompraCategoriasDTO) productosPorCategoria.get(i)).getCarroCompraProductosDTO().get(j)).setCantidad( newCantidad );
						        ((CarroCompraProductosDTO)((CarroCompraCategoriasDTO) productosPorCategoria.get(i)).getCarroCompraProductosDTO().get(j)).setPrecio( rs.getDouble("precio") * newCantidad );
						    }
						    flag = false;
						}
					}
				}
				if ( !flag ) {
					continue; // Se salta el registro
				}
				// Listado de productos por categorias
				l_pro = new ArrayList();				
				
				// Revisar si existe la categoría
				boolean flag_cat = true;
				for( int i = 0; i < productosPorCategoria.size(); i++ ) {
					categoria = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
					if( categoria.getId() == rs.getLong("cat_id") ) {
						l_pro = categoria.getCarroCompraProductosDTO();
						flag_cat = false;
						break;
					}
				}
				
				// Agregar categorias sólo si es una categoría vacía
				categoria = new CarroCompraCategoriasDTO();
				categoria.setId( rs.getLong("cat_id") );
				categoria.setCategoria( rs.getString("cat_nombre") );
								
				// Agregar los productos
				productos = new CarroCompraProductosDTO();
				productos.setPro_id(rs.getLong("pro_id"));
				productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
				productos.setCodigo(rs.getString("pro_cod_sap"));
				productos.setPpum(rs.getDouble("precio"));
				productos.setPrecio( rs.getDouble("precio") * rs.getDouble("cant_pick") );				
				productos.setCantidad(rs.getDouble("cant_pick"));
				productos.setUnidad_nombre(rs.getString("uni_desc"));
				productos.setUnidadMedida(rs.getString("uni_med"));
				productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
				
				productos.setTipre( Formatos.formatoUnidad(rs.getString("pro_tipre")) );
				productos.setInter_maximo( rs.getDouble("pro_inter_max") );
				if( rs.getString("pro_inter_valor") != null ) {
					productos.setInter_valor(rs.getDouble("pro_inter_valor"));
				} else {
					productos.setInter_valor(1.0);
				}
				productos.setMarca(rs.getString("mar_nombre"));
				if( rs.getString("pro_nota").compareTo("S") == 0 ) {
					productos.setCon_nota( true );
				} else {
					productos.setCon_nota( false );
				}
				productos.setStock(100); // No toma encuenta el stock nunca pone 0
				l_pro.add(productos);
				categoria.setCarroCompraProductosDTO(l_pro);
				
				// Sólo si es una categoría nueva
				if( flag_cat ) {
				    productosPorCategoria.add(categoria);
				}			    
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+productosPorCategoria.size());
		return productosPorCategoria;
	}
	
	
	/*
	 public List productosEnviadosPedidoForEmail(long id_pedido) throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT dp.id_dpicking, dp.id_detalle, dp.id_bp, dp.id_producto, dp.id_pedido, dp.cbarra,"+
						 " dp.descripcion, dp.precio, dp.cant_pick, dp.sustituto, bp.cod_bin,"+
						 " case when dp.id_producto is null then '' else p.cod_prod1 end as cod_prod1,"+
						 " case when dp.id_producto is null then '' else p.uni_med end as uni_med"+ 
						 " FROM bo_detalle_picking dp"+
						 " LEFT JOIN bo_bins_pedido bp on bp.id_bp = dp.id_bp"+
						 " LEFT JOIN bo_detalle_pedido p on p.id_detalle = dp.id_detalle"+
						 " WHERE dp.id_pedido = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				DetallePickingEntity dpick = new DetallePickingEntity();
				dpick.setId_dpicking(new Long(rs.getLong("id_dpicking")));
				dpick.setId_detalle(new Long(rs.getLong("id_detalle")));
				dpick.setId_bp(new Long(rs.getLong("id_bp")));
				dpick.setId_producto(new Long(rs.getLong("id_producto")));
				dpick.setId_pedido(new Long(rs.getLong("id_pedido")));
				dpick.setCBarra(rs.getString("cbarra"));
				dpick.setDescripcion(rs.getString("descripcion"));
				dpick.setPrecio(new Double(rs.getDouble("precio")));
				dpick.setCant_pick(new Double(rs.getDouble("cant_pick")));
				dpick.setSustituto(rs.getString("sustituto"));
				dpick.setCod_bin(rs.getString("cod_bin"));
				dpick.setCod_prod(rs.getString("cod_prod1"));
				dpick.setUni_med(rs.getString("uni_med"));
				result.add(dpick);
			}
			rs.close();
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
            try {
                if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	 */
	
	
	/**
	 * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
	 * (Modificación según req. 567)
	 * @return List String
	 * @throws PedidosDAOException, en el caso que exista error en la consulta en base de datos.
	 * 
	 */
	public List getHorasInicioJDespacho() throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT DISTINCT HD.HINI HINI "
                       + "FROM BO_JORNADA_DESP JD "
                       + "     JOIN BO_HORARIO_DESP HD ON JD.ID_HOR_DESP = HD.ID_HOR_DESP "
                       + "WHERE JD.FECHA BETWEEN (CURRENT DATE - 6 DAY)  AND (CURRENT DATE + 1 DAY) "
                       + "ORDER BY HD.HINI";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			logger.debug("SQL: " + sql);

			rs = stm.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("HINI"));
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size() + " Horarios.");
		return result;
	}

	/**
	 * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
	 * (Modificación según req. 567)
	 * @return List String
	 * @throws PedidosDAOException, en el caso que exista error en la consulta en base de datos.
	 * 
	 */
	public List getJornadasDespachoByFecha(String fecha, int local) throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT DISTINCT HD.HINI, HD.HFIN "
                       + "FROM BODBA.BO_JORNADA_DESP JD "
                       + "     JOIN BODBA.BO_HORARIO_DESP HD ON JD.ID_HOR_DESP = HD.ID_HOR_DESP "
                       + "     JOIN BODBA.BO_ZONAS         Z ON Z.ID_ZONA      = JD.ID_ZONA "
                       + "WHERE JD.FECHA = '" + fecha + "' "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND JD.CAPAC_OCUPADA > 0 "
                       + "ORDER BY HD.HINI";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			logger.debug("SQL (getJornadasDespachoByFecha): " + sql);
			logger.debug("fecha: " + fecha);

			rs = stm.executeQuery();
			while (rs.next()) {
			    JDespachoTrackingDTO jd = new JDespachoTrackingDTO();
			    jd.setH_ini(rs.getString("HINI"));
			    jd.setH_fin(rs.getString("HFIN"));
				result.add(jd);
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size() + " Horarios.");
		return result;
	}

	
	/**
	 * Verifica si el pedido tiene todos sus productos como Faltantes
	 * @param id_pedido
	 * @return true - Pedido tiene todos sus productos como faltantes, false - Otra situación
	 * @throws PedidosDAOException
	 */
	public boolean tieneTodosFaltantes(long id_pedido) throws PedidosDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs=null;
		try {
			String sql = " select sum(cant_solic), sum(cant_faltan) from bo_detalle_pedido where id_pedido = ?";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
				int cant_solic = rs.getInt(1);
				int cant_faltante = rs.getInt(2);
				if (cant_solic == cant_faltante)
					return true;
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Cantidad Total Solicitada Pedido [" + id_pedido + "] = " + result);
		return result;
	}
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#doGeneraPedido(cl.bbr.jumbocl.pedidos.dto.PedidoDTO)
	 */
	public long doGeneraPedido(PedidoDTO pedido) throws PedidosDAOException {
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		long id_pedido = 0;
		try {
            logger.debug("DAO doGeneraPedido..." );
            
            String genero = "null";
            if ( pedido.getGenero() != null )
                genero = "'"+pedido.getGenero()+"' ";
            
            String fecha_nac ="null";
            if ( pedido.getFnac() != null )
                fecha_nac = "'"+new Date(pedido.getFnac().getTime())+"' ";
            
            String tarj_bancaria ="null";
            if ( pedido.getNom_tbancaria() != null )
                tarj_bancaria ="'"+pedido.getNom_tbancaria()+"'";
            
            String id_usuario_fono = "null";
            if ( pedido.getId_usuario_fono() > 0 )
                id_usuario_fono = ""+pedido.getId_usuario_fono();
            
            String observacion = "null";
            if ( pedido.getObservacion() != null )
                observacion = "'"+pedido.getObservacion()+"'";
            
            String sql = "INSERT INTO bo_pedidos (" +
                         "id_estado, id_jdespacho, id_jpicking, id_local, id_comuna, id_zona, " +
                         "id_cliente, genero, fnac, nom_cliente, telefono2, telefono, " +
                         "costo_despacho, fcreacion, monto_pedido, indicacion, " +
                         "medio_pago, num_mp, fecha_exp, n_cuotas, nom_tbancaria, tb_banco, " +
                         "cant_productos, rut_cliente, dv_cliente, " +
                         "dir_id, dir_tipo_calle, dir_calle, dir_numero, dir_depto, " +
                         "pol_id, pol_sustitucion, sin_gente_op, sin_gente_txt, " +
                         "tipo_doc, id_usuario_fono, observacion," +
                         " origen, tipo_ve, id_cotizacion, id_local_fact, TIPO_PICKING) " +
                         " VALUES( " +
                         pedido.getId_estado()+", " +
                         pedido.getId_jdespacho()+", " +
                         pedido.getId_jpicking()+", " +
                         pedido.getId_local()+", " +
                         pedido.getId_comuna()+", " +
                         pedido.getId_zona()+", " +
                         pedido.getId_cliente()+", " +
                         genero+", " +
                         fecha_nac +", " +
                         " '" +pedido.getNom_cliente()+"', " +
                         " '" +pedido.getTelefono2()+"', " +
                         " '" +pedido.getTelefono()+"', " +
                         pedido.getCosto_despacho()+", " +
                         "CURRENT DATE, " +
                         pedido.getMonto()+", " +
                         " '" +pedido.getIndicacion()+"', " +
                         " '" +pedido.getMedio_pago()+"', " +
                         " '" +pedido.getNum_mp()+"', " +
                         " '" +pedido.getFecha_exp()+"', " +
                         pedido.getN_cuotas()+", " +
                         tarj_bancaria +", " +
                         " '" +pedido.getTb_banco()+"', " +
                         pedido.getCant_prods()+", " +
                         pedido.getRut_cliente()+", " +
                         " '" +pedido.getDv_cliente()+"', " +
                         pedido.getDir_id()+", " +
                         " '" +pedido.getDir_tipo_calle()+"', " +
                         " '" +pedido.getDir_calle() +"', " +
                         " '" +pedido.getDir_numero()+"', " +
                         " '" +pedido.getDir_depto()+"', " +
                         pedido.getPol_id()+", " +
                         " '" +pedido.getPol_sustitucion()+"', " +
                         pedido.getSin_gente_op()+", " +
                         " '" +pedido.getSin_gente_txt()+"', " +
                         " '" +pedido.getTipo_doc()+"', " +
                         id_usuario_fono+", " +
                         observacion+", " +
                         " '" +pedido.getOrigen()+"', " +
                         " '" +pedido.getTipo_ve()+"', " +
                         pedido.getId_cotizacion()+", " +
                         pedido.getId_local_fact()+", " +
                         "'" + pedido.getTipo_picking() + "')";
            
            logger.debug("SQL (doGeneraPedido): " + sql);
            con = this.getConnection();
            stm = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            
            int i = stm.executeUpdate();
            logger.debug("rc: " + i);
            rs = stm.getGeneratedKeys();
            if (rs.next())
                id_pedido = rs.getInt(1);

            
		} catch (SQLException e) {
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        PedidoExtDTO pex = new PedidoExtDTO();
        pex.setNroGuiaCaso(0);
        pedido.setPedidoExt(pex);
        pedido.setId_pedido(id_pedido);
        this.addDatosPedidoExt(pedido);
        return id_pedido;
    }

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#generaProductoPedido(cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO)
	 */
	public boolean generaProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException {
		
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			"INSERT INTO bo_detalle_pedido (id_pedido, id_sector, id_producto, cod_prod1, uni_med, " +
			" descripcion, cant_solic, observacion, precio, cant_spick, pesable, preparable, con_nota, " +
			" precio_lista, dscto_item ) "+
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		logger.debug("en generaProductoPedido");
		logger.debug("SQL: " + sql);
		//LPC 2011-04-19 se cambio el 3er parametro
		logger.debug("vals"+prod.getId_pedido()+","+ prod.getId_sector()+","+prod.getId_producto()+","+ //LPC .getCod_producto()
				prod.getCod_producto()+","+prod.getUnid_medida()+","+prod.getDescripcion()+","+
				prod.getCant_solic()+","+prod.getObservacion()+","+prod.getPrecio()+","+prod.getCant_spick()
				+","+prod.getPesable()+","+prod.getPreparable()+","+prod.getCon_nota());
	
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prod.getId_pedido());
			
			if(prod.getId_sector()>0)
				stm.setLong(2, prod.getId_sector());
			else
				stm.setNull(2,java.sql.Types.INTEGER);
			
			stm.setLong(3, prod.getId_producto());
			stm.setString(4,prod.getCod_producto());
			stm.setString(5,prod.getUnid_medida());
			stm.setString(6,prod.getDescripcion());
			stm.setDouble(7,prod.getCant_solic());
			stm.setString(8,prod.getObservacion());
			stm.setDouble(9,prod.getPrecio());
			stm.setDouble(10,prod.getCant_spick());
			stm.setString(11,prod.getPesable());
			stm.setString(12,prod.getPreparable());
			stm.setString(13,prod.getCon_nota());
			stm.setDouble(14,prod.getPrecio_lista());
			stm.setDouble(15,prod.getDscto_item());
			
			
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#setCambiarCotizacion(long, int)
	 */
	public boolean setCambiarCotizacion(long id_cotizacion, int estado) throws PedidosDAOException {
		
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			" UPDATE ve_cotizacion SET cot_estado = ? WHERE cot_id = ? ";
		
		logger.debug("en setCambiarCotizacion");
		logger.debug("SQL: " + sql);
		logger.debug("id_cotizacion: "+id_cotizacion);
		logger.debug("estado: "+estado);
	
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setInt(1, estado);
			stm.setLong(2, id_cotizacion);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getUltimoCargoByEmpresaId(cl.bbr.jumbocl.pedidos.dto.PedidoDTO)
	 */
	public double getUltimoCargoByEmpresaId(PedidoDTO dto) throws PedidosDAOException {
		double result = 0;

		PreparedStatement stm=null;
		ResultSet rs = null;
		
		String SQLStmt = 
			" SELECT  monto_cargo " +
			" FROM   ve_cargos " +
			" WHERE  emp_id = ? and id_pedido = ? " +
			" ORDER BY fechaing DESC ";
	
		logger.debug("Ejecución DAO getUltimoCargoByEmpresaId...");
		logger.debug("SQL: " 			+ SQLStmt);
		logger.debug("id_pedido" 		+ dto.getId_pedido());
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");
			
			stm.setLong(1, dto.getId_cliente());
			stm.setLong(2, dto.getId_pedido());
			
			rs = stm.executeQuery();
			if(rs.next()){
				result = rs.getDouble("monto_cargo");
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getEmpresaById(long)
	 */
	public EmpresasEntity getEmpresaById(long id_empresa)  throws PedidosDAOException {
		EmpresasEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("en getEmpresaById...");
			conn = this.getConnection();
			String sql = 
				" SELECT emp_id, emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, emp_tamano, emp_qtyemp, " +
				" emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, emp_mail_contacto, emp_cargo_contacto, " +
				" emp_saldo, emp_fact_saldo, emp_fmod, emp_estado, emp_mg_min, emp_fec_crea, " +
				" emp_dscto_max, emp_mod_rzsoc " +
				" FROM ve_empresa WHERE emp_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_empresa);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				ent = new EmpresasEntity();
				ent.setId(new Long(rs.getLong("emp_id")));
				ent.setRut(new Long(rs.getLong("emp_rut")));
				ent.setDv(new Character(rs.getString("emp_dv").charAt(0)));
				ent.setNombre(rs.getString("emp_nom"));
				ent.setDescripcion(rs.getString("emp_descr"));
				ent.setRsocial(rs.getString("emp_rzsocial"));
				ent.setRubro(rs.getString("emp_rubro"));
				ent.setTamano(rs.getString("emp_tamano"));
				ent.setQtyemp(new Integer(rs.getInt("emp_qtyemp")));
				ent.setNom_contacto(rs.getString("emp_nom_contacto"));
				ent.setFono1_contacto(rs.getString("emp_fono1_contacto"));
				ent.setFono2_contacto(rs.getString("emp_fono2_contacto"));
				ent.setFono3_contacto(rs.getString("emp_fono3_contacto"));
				ent.setMail_contacto(rs.getString("emp_mail_contacto"));
				ent.setCargo_contacto(rs.getString("emp_cargo_contacto"));
				ent.setSaldo(new Double(rs.getDouble("emp_saldo")));
				ent.setFact_saldo(rs.getTimestamp("emp_fact_saldo"));
				ent.setFmod(rs.getTimestamp("emp_fmod"));
				ent.setEstado(rs.getString("emp_estado"));
				ent.setMrg_minimo(new Double(rs.getDouble("emp_mg_min")));
				ent.setFec_crea(rs.getTimestamp("emp_fec_crea"));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return ent;
	}
	
	public List getPedidosCotiz(long id_cot) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_ped = new ArrayList();
		
		logger.debug("Parametros getPedidosCotiz:");
		logger.debug("numero_cot:"+id_cot);
	
		try {
			
			String Sql = " SELECT distinct p.id_pedido id, p.fcreacion fcrea, j.fecha fdesp, " +
					" l.nom_local local, p.costo_despacho cost_desp, " +
					" dp.cant_faltan cant_falt, e.nombre estado," +
					" LF.nom_local local_fact, p.id_estado id_estado " +
					" FROM bo_pedidos p " +
					" JOIN bo_detalle_pedido dp ON p.id_pedido =  dp.id_pedido " +
					" JOIN bo_locales l ON p.id_local = l.id_local " +
					" JOIN bo_jornada_desp j ON p.id_jdespacho = j.id_jdespacho " +
					" JOIN bo_estados e ON p.id_estado = e.id_estado " +
					" JOIN bo_locales LF ON LF.id_local = p.id_local_fact " +
					" WHERE p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and p.id_cotizacion = " + id_cot +
					" ORDER BY p.id_pedido " ;

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				
				PedidosCotizacionesDTO ped = new PedidosCotizacionesDTO();
				ped.setPed_id(rs.getLong("id"));
				ped.setFec_pedido(rs.getString("fcrea"));
				ped.setFec_despacho(rs.getString("fdesp"));
				ped.setLocal(rs.getString("local"));
				ped.setCosto_desp(rs.getLong("cost_desp"));
				ped.setCant_falt(rs.getDouble("cant_falt"));
				ped.setEstado(rs.getString("estado"));
				ped.setLocal_fact(rs.getString("local_fact"));
				ped.setId_estado(rs.getLong("id_estado"));
				
				list_ped.add(ped);
				}
			

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return list_ped;
	}	
	
	/**
	 * Cambia el estado a una cotización
	 * @param id_cot
	 * @param id_estado
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado) throws PedidosDAOException {
		
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setModEstadoCotizacion");
			String sql = "UPDATE  ve_cotizacion SET cot_estado = ? WHERE cot_id = ? ";
			logger.debug(sql);
			logger.debug("id_cot:"+id_cot);
			logger.debug("id_estado:"+id_estado);
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_estado);
			stm.setLong(2, id_cot);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	public double getSaldoActualPendiente(long empresa) throws PedidosDAOException {
		double saldo_pend_actual	= 0.0;
		PreparedStatement stm		= null;
		ResultSet rs				= null;		
		
		try {
			conn = this.getConnection();
            
			String sql = " SELECT SUM (TRX.MONTO_TRXMP) pendientes " +
						 " FROM VE_EMPRESA E " +
						 " 		JOIN VE_COTIZACION C    ON E.EMP_ID = C.COT_EMP_ID    AND E.EMP_ID = ? " +
						 "		JOIN BO_PEDIDOS P       ON C.COT_ID = P.ID_COTIZACION AND P.ID_ESTADO IN " + Constantes.ESTADOS_PEDIDO_PENDIENTES + " AND P.MEDIO_PAGO = 'CRE' " +
						 " 		LEFT JOIN BO_TRX_MP TRX ON TRX.ID_PEDIDO = P.ID_PEDIDO " +
						 " WHERE TRX.FCREACION IS NULL OR TRX.FCREACION >= (CURRENT DATE - " + Constantes.IF_LINEA_CREDITO_DIAS + " DAY) ";
			
			logger.debug("SQL getSaldoActualPendiente:"+sql);
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,empresa);
			
			rs = stm.executeQuery();
			if(rs.next()) {
				saldo_pend_actual = rs.getDouble("pendientes");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return saldo_pend_actual;
	}
	
	// Funciones de Promociones	
	

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#setAplicaProrrateo(long, long)
	 */
	public boolean setAplicaProrrateo(long id_detpick, long precio) throws PedidosDAOException{
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setAplicaProrrateo");
			String sql = " UPDATE  bo_detalle_picking " +
					     " SET precio = " + precio +
					     " WHERE id_dpicking ="+id_detpick;
			logger.debug(sql);

			
			stm = conn.prepareStatement(sql);

			

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getProductoPromos(long, long)
	 */
	public ProductoPromoDTO getProductoPromos(long id_producto, long id_local) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		ProductoPromoDTO pro = null;
	//	List list_promo = new ArrayList();
		
		logger.debug("Parametros getProductoPromos:");
		logger.debug("id_producto:"+id_producto+"-  id_local:"+id_local);
	
		try {
			
			String Sql = " SELECT id_prodpromos, id_producto, id_local, cod_promo1," +
					" cod_promo2, cod_promo3 " +
					" FROM pr_producto_promos " +
					" WHERE id_producto = " + id_producto +
					" AND id_local = " + id_local ;

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			
			while (rs.next()) {
				
				pro = new ProductoPromoDTO();
				pro.setId_prodpromo(rs.getLong("id_prodpromos"));
				pro.setId_producto(rs.getLong("id_producto"));
				pro.setId_local(rs.getLong("id_local"));
				pro.setCod_promo1(rs.getLong("cod_promo1"));
				pro.setCod_promo2(rs.getLong("cod_promo2"));
				pro.setCod_promo3(rs.getLong("cod_promo3"));
			}
			

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return pro;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#doEliminaPromoDPPedido(long)
	 */
	public boolean doEliminaPromoDPPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {

			//			con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en doEliminaPromoDetallePedido");
			String sql = "DELETE FROM  bo_promos_detped " +
			" WHERE id_pedido = ? ";
			logger.debug(sql);
			logger.debug("id_pedido: "+id_pedido);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_pedido);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#doModificaDescuentoDetallePedido(long)
	 */
	public boolean doModificaDescuentoDetallePedido(DetallePedidoDTO dp) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		boolean result = false;

		try {

			conn = this.getConnection();
			logger.debug("en doModificaDescuentoDetallePedido");
			String sql = " UPDATE bo_detalle_pedido " +
					     " SET precio = ? , " +
					     " dscto_item = ? " +
						 " WHERE id_detalle = ?";
			logger.debug(sql);
			logger.debug("precio: "+ dp.getPrecio());
			logger.debug("dscto_item: "+dp.getDscto_item());
			logger.debug("id_detalle: " + dp.getId_detalle());
			
			stm = conn.prepareStatement(sql);

			stm.setDouble(1, dp.getPrecio());
			stm.setDouble(2, dp.getDscto_item());
			stm.setLong(3, dp.getId_detalle());

			int i = stm.executeUpdate();
			if(i<=0) {	

				return false;
		}
			

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try{
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getCuponesPedidoByIdPedido(long)
	 */
	public List getCuponesPedidoByIdPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getCuponesPedidoByIdPedido:");

		 try {			
			
			String sql = "SELECT id_cupon, id_pedido, nro_cupon, id_tcp " +				
					" FROM BO_CUPON " +
					" WHERE id_pedido  = "+id_pedido;
					
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				CuponPedidoDTO cupon = new CuponPedidoDTO();
				cupon.setId_pedido(rs.getLong("id_pedido"));
				cupon.setId_cupon(rs.getLong("id_cupon"));
				cupon.setId_tcp(rs.getLong("id_tcp"));
				cupon.setNro_cupon(rs.getString("nro_cupon"));								
				result.add(cupon);
			}

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getTcpPedidosByIdPedido(long)
	 */
	public List getTcpPedidosByIdPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getTcpPedidosByIdPedido:");

		 try {			
			String sql = "SELECT t.id_tcp id_tcp, " +
					" t.id_pedido id_pedido, " +
					" t.nro_tcp nro_tcp, " +
					" t.cant_max cant_max, " +
					" t.cant_util cant_util " +				
					" FROM BO_TCP t " +
					" WHERE t.id_pedido  = "+id_pedido;
		
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			while (rs.next()) {
				TcpPedidoDTO tcp = new TcpPedidoDTO();
				tcp.setId_pedido(rs.getLong("id_pedido"));
				tcp.setId_tcp(rs.getLong("id_tcp"));
				tcp.setNro_tcp(rs.getInt("nro_tcp"));
				tcp.setCant_max(rs.getInt("cant_max"));
				tcp.setCant_util(rs.getInt("cant_util"));				
				result.add(tcp);
			}

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getProductosByPedido(long)
	 */
	public List getProductosByPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		//logger.debug("en getProductosByPedido:");
		/*
		 * long codigo (EAN13);
		 * int depto;
		 * int cantidad;
		 * long precio_lista;
		 * 
		 */
		 try {			

		 	String sql = "SELECT dp.id_detalle 	id_detalle, "
						+ " dp.id_producto  id_producto, " 
						+ " dp.id_pedido    id_pedido,   " 
						+ " dp.descripcion  descripcion,  " 
						+ " dp.cant_solic   cant_solic,  " 
						+ " dp.precio_lista precio_lista,  " 
						+ " dp.pesable pesable, "						
						+ " SUBSTR(p.id_catprod,1,2) seccion, "
						+ " SUBSTR(p.id_catprod,3,2) rubro, "
						+ " MAX(b.cod_barra) cod_barra " 
				+ " FROM bo_detalle_pedido dp "  
				+ " JOIN bo_codbarra b ON b.id_producto = dp.id_producto "  
				+ " JOIN bo_productos p ON p.id_producto = dp.id_producto "
				+ " WHERE dp.id_pedido = ?      " 
				+ " GROUP BY dp.id_detalle,  " 
						+ " dp.id_producto,  " 
						+ " dp.id_pedido,  " 
						+ " dp.descripcion,  " 
						+ " dp.cant_solic,  " 
						+ " dp.precio_lista,  "
						+ " dp.pesable, " 
						+ " p.id_catprod ";
					
			
			//logger.debug("SQL query: " + sql + ", "+ id_pedido );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				DetPedidoDTO detped = new DetPedidoDTO();
				detped.setId_pedido(rs.getLong("id_pedido"));
				detped.setId_detalle(rs.getLong("id_detalle"));
				detped.setId_producto(rs.getLong("id_producto"));
				detped.setDesc_prod(rs.getString("descripcion"));
				detped.setCant_solicitada(rs.getDouble("cant_solic"));
				detped.setPrecio_lista(rs.getDouble("precio_lista"));	
				detped.setPesable(rs.getString("pesable"));
				detped.setCod_barra(rs.getString("cod_barra"));
				detped.setSeccion_sap(rs.getString("seccion"));
				detped.setRubro(rs.getInt("rubro"));
				result.add(detped);
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		//logger.debug("ok");

		return result;
	}
	

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#updTcpCantUtil(long, long, long)
	 */
	public int updTcpCantUtil(long id_pedido, long nro_tcp, long cant_util) throws PedidosDAOException {
		PreparedStatement stm = null;
		int resultado=0;
		logger.debug("En setTcpPedido...");
		
		try {
			
			String Sql = " UPDATE BO_TCP SET cant_util="+cant_util
				+" WHERE id_pedido ="+id_pedido
				+" AND nro_tcp ="+nro_tcp;
	
			logger.debug("SQL :"+Sql);			
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);			
						
			resultado = stm.executeUpdate();
			logger.debug("Actualizados TCP:"+resultado);

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
		
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getPromocionPedidos(long)
	 */
	public List getResumenPromocionPedidos(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getPromocionPedidos:");
		try {
			String sql = " SELECT "     
					+ "    pdp.ID_PROMOCION	id_promocion, "
					+ "    pdp.PROMO_CODIGO	promo_codigo, "
					+ "    pdp.PROMO_DESCR	promo_descr, "
					+ "    pdp.PROMO_TIPO		promo_tipo, "
					+ "    pdp.PROMO_FINI		promo_fini, "
					+ "    pdp.PROMO_FFIN		promo_ffin, "
					//+ "    round(sum((DP.precio_lista * pdp.dscto_porc / 100)* cant_solic),0) monto_descuento "
					+ "    round(sum((DP.precio_lista * (1-(dec(round(DP.precio/DP.precio_lista,7),10,7))))* cant_solic),0) monto_descuento , "  
					+ "    count(p.ID_PROMOCION) existe_promo "
					+ " from bo_promos_detped pdp "
					+ "    join bo_detalle_pedido DP on DP.id_detalle = pdp.id_detalle "
					+ "    left join PR_PROMOCION p on pdp.ID_PROMOCION = p.ID_PROMOCION "
					+ " WHERE pdp.id_pedido = ? "
					+ " group by pdp.ID_PROMOCION, "
					+ " 		 pdp.PROMO_CODIGO, "
					+ " 		 pdp.PROMO_DESCR, "
					+ " 		 pdp.PROMO_TIPO, "
					+ " 		 pdp.PROMO_FINI, "
					+ " 		 pdp.PROMO_FFIN ";
			
			
			logger.debug("SQL query: " + sql + ", "+ id_pedido );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				ResumenPedidoPromocionDTO ped = new ResumenPedidoPromocionDTO();
				ped.setId_promocion(rs.getLong("id_promocion"));
				ped.setPromo_codigo(rs.getLong("promo_codigo"));
				ped.setDesc_promo(rs.getString("promo_descr"));				
				ped.setTipo_promo(rs.getLong("promo_tipo"));
				ped.setFec_ini(rs.getString("promo_fini"));
				ped.setFec_fin(rs.getString("promo_ffin"));
				ped.setMonto_descuento(rs.getLong("monto_descuento"));
				ped.setNum_promos(rs.getInt("existe_promo"));
				
				result.add(ped);
			}
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getProductosByPromocionPedido(long, long)
	 */
	public List getProductosByPromocionPedido(long id_promocion, long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getProductosByPromocionPedido:");
		try {

			String sql = " SELECT dp.id_producto id_producto, " +
					" dp.descripcion descr, " +
					" dp.id_detalle id_detalle " +
					" FROM BO_DETALLE_PEDIDO dp " +
					" JOIN BO_PROMOS_DETPED p ON p.id_detalle = dp.id_detalle AND p.id_promocion = ? " +
					" WHERE dp.id_pedido = ? ";
			
			
			logger.debug("SQL query: " + sql + " id_promocion="+id_promocion+", id_pedido="+ id_pedido );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			stm.setLong(1, id_promocion);
			stm.setLong(2, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosRelacionadosPromoDTO prod = new ProductosRelacionadosPromoDTO();
				prod.setId_producto(rs.getLong("id_producto"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setId_detalle(rs.getLong("id_detalle"));
				logger.debug("id_detalle:"+rs.getLong("id_detalle")
						+" producto:"+rs.getLong("id_producto")
						+ " descr:"+rs.getString("descr"));
				result.add(prod);
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}
	
	

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getPromocionesByCriteria(cl.bbr.jumbocl.promos.dto.PromocionesCriteriaDTO)
	 */
	//FIXME Eliminar
	public List getPromocionesByCriteria(PromocionesCriteriaDTO criteria) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getPromocionesByCriteria:");
		

		try {
			
			String sql="";						
			String sqlextra="";
			
			logger.debug("id_local:"+criteria.getId_local());
			logger.debug("cod_promo:"+criteria.getCod_promo());
			
			if (criteria.getId_local()>0){
				sqlextra +=" AND pr.id_local = "+criteria.getId_local();
			}
			
			
			if (criteria.getCod_promo()>0){
				sqlextra +=" AND pr.cod_promo = "+criteria.getCod_promo();
			}
			//paginacion
			int pag = criteria.getPag();
			int regXpag = criteria.getRegsperpag();
			logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
			if(pag<=0) pag = 1;
			if(regXpag<10) regXpag = 10;
			int iniReg = (pag-1)*regXpag + 1;
			int finReg = pag*regXpag;		
				
			logger.debug("pag:"+pag);
			logger.debug("regXpag:"+regXpag);
			logger.debug("iniReg:"+iniReg);
			logger.debug("finReg:"+finReg);
			
			

			sql = "SELECT * FROM ( " +
			" SELECT row_number() over(  ORDER BY pr.cod_promo DESC ) as row, " +
			" pr.id_promocion id_promocion," +
			" pr.cod_promo cod_promo, " +
			" l.nom_local nom_local," +
			" pr.tipo_promo tipo," +
			" pr.descr descr," +
			" pr.fini fecha_ini," +
			" pr.ffin fecha_fin" +
			" FROM PR_PROMOCION pr" +
			" JOIN BO_LOCALES l ON l.id_local = pr.id_local " +
			" WHERE 1=1 " + sqlextra;

			//paginador
			sql += ") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			
			
			
			logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			while (rs.next()) {
				MonitorPromocionesDTO mon = new MonitorPromocionesDTO();
				
				mon.setId_promocion(rs.getLong("id_promocion"));				
				mon.setCod_promocion(rs.getString("cod_promo"));
				mon.setTipo_promo(rs.getString("tipo"));				
				mon.setNom_local(rs.getString("nom_local"));				
				mon.setDescripcion(rs.getString("descr"));				
				mon.setFecha_inicio(rs.getString("fecha_ini"));				
				mon.setFecha_fin(rs.getString("fecha_fin"));
				
				result.add(mon);
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}
	
	
	public List getPromociones() throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();

		try {
			String sql="select pr.cod_promo, pr.tipo_promo, pr.descr, pr.fini, pr.ffin, l.nom_local "
			+ " FROM PR_PROMOCION pr inner join bo_locales l on pr.id_local = l.id_local "
			+ " order by pr.cod_promo desc, l.nom_local ";

			logger.debug("SQL getPromociones(): " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			int codigo = -1;
			Promocion promocion = null;
			while (rs.next()) {
			   if (rs.getInt("cod_promo") != codigo) {
			      codigo = rs.getInt("cod_promo");
			      promocion = new Promocion();
			      promocion.setCodigo(rs.getInt("cod_promo"));
			      promocion.setTipo(rs.getInt("tipo_promo"));
			      promocion.setDescripcion(rs.getString("descr"));
			      promocion.setFechaIni(new java.util.Date(rs.getTimestamp("fini").getTime()));				
			      promocion.setFechaFin(new java.util.Date(rs.getTimestamp("ffin").getTime()));
			      promocion.addLocal(rs.getString("nom_local"));				
			      result.add(promocion);
			   }else{
			      promocion.addLocal(rs.getString("nom_local"));
			   }
			}

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	
	
	
	public PromocionDTO getPromocion(int codigo) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		PromocionDTO promo = null;
		try {
			String sql = "SELECT ID_PROMOCION, COD_PROMO, p.ID_LOCAL, VERSION, TIPO_PROMO, FINI, FFIN, "
						+ "DESCR, CANT_MIN, MONTO_MIN, MONTO1, DESCUENTO1, MONTO2, DESCUENTO2, "
						+ "MONTO3, DESCUENTO3, MONTO4, DESCUENTO4, MONTO5, DESCUENTO5, FP1,"
						+ " NUM_CUOTA1, TCP1, BENEFICIO1, FP2, NUM_CUOTA2, TCP2, BENEFICIO2, FP3,"
						+ " NUM_CUOTA3, TCP3, BENEFICIO3, CONDICION1, CONDICION2, CONDICION3,"
						+ " PRORRATEO, RECUPERABLE, CANAL, SUSTITUIBLE, BANNER, FALTANTE, NOM_LOCAL "
			+ " FROM PR_PROMOCION p " 
			+ " JOIN BO_LOCALES l ON p.id_local = l.id_local " 
			+ " WHERE COD_PROMO = ? " ;
			
			logger.debug("SQL query: " + sql+ " codpromo=" + codigo);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			stm.setInt(1, codigo);
			rs = stm.executeQuery();
			String locales="";
			if (rs.next()) {
				promo = new  PromocionDTO();
				promo.setId_promocion(rs.getLong("ID_PROMOCION"));
				promo.setCod_promo(rs.getLong("COD_PROMO"));
				promo.setId_local(rs.getLong("ID_LOCAL"));
				promo.setVersion(rs.getLong("VERSION"));
				promo.setTipo_promo(rs.getLong("TIPO_PROMO"));
				promo.setFini(rs.getString("FINI"));
				promo.setFfin(rs.getString("FFIN"));
				promo.setDescr(rs.getString("DESCR"));
				promo.setCant_min(rs.getLong("CANT_MIN"));
				promo.setMonto_min(rs.getLong("MONTO_MIN"));
				
				promo.setMonto1(rs.getLong("MONTO1"));
				promo.setDescuento1(rs.getDouble("DESCUENTO1"));
				promo.setMonto2(rs.getLong("MONTO2"));
				promo.setDescuento2(rs.getDouble("DESCUENTO2"));
				promo.setMonto3(rs.getLong("MONTO3"));
				promo.setDescuento3(rs.getDouble("DESCUENTO3"));
				promo.setMonto4(rs.getLong("MONTO4"));
				promo.setDescuento4(rs.getDouble("DESCUENTO4"));
				promo.setMonto5(rs.getLong("MONTO5"));
				promo.setDescuento5(rs.getDouble("DESCUENTO5"));
				
				promo.setFp1(rs.getLong("FP1"));
				promo.setNum_cuota1(rs.getLong("NUM_CUOTA1"));
				promo.setTcp1(rs.getLong("TCP1"));
				promo.setBeneficio1(rs.getDouble("BENEFICIO1"));
				
				promo.setFp2(rs.getLong("FP2"));
				promo.setNum_cuota2(rs.getLong("NUM_CUOTA2"));
				promo.setTcp2(rs.getLong("TCP2"));
				promo.setBeneficio2(rs.getDouble("BENEFICIO2"));
				
				promo.setFp3(rs.getLong("FP3"));				
				promo.setNum_cuota3(rs.getLong("NUM_CUOTA3"));
				promo.setTcp3(rs.getLong("TCP3"));
				promo.setBeneficio3(rs.getDouble("BENEFICIO3"));
				
				promo.setCondicion1(rs.getLong("CONDICION1"));
				promo.setCondicion2(rs.getLong("CONDICION2"));
				promo.setCondicion3(rs.getLong("CONDICION3"));
				promo.setProrrateo(rs.getLong("PRORRATEO"));
				promo.setRecuperable(rs.getLong("RECUPERABLE"));
				promo.setCanal(rs.getLong("CANAL"));
				promo.setSustituible(rs.getString("SUSTITUIBLE"));
				promo.setBanner(rs.getString("BANNER"));
				promo.setFaltante(rs.getString("FALTANTE"));
				locales = rs.getString("NOM_LOCAL") + ", ";
			}
			
			while(rs.next()){
			   locales += rs.getString("NOM_LOCAL") + ", ";
			  
			}
			promo.setNom_local(locales);

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return promo;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getPromocionesByCriteriaCount(cl.bbr.jumbocl.promos.dto.PromocionesCriteriaDTO)
	 */
	public long getPromocionesByCriteriaCount(PromocionesCriteriaDTO criteria) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		long result = 0;
		logger.debug("en getPromocionesByCriteriaCount:");
		

		try {
			
			String sql="";
			
			String sqlextra="";
			if (criteria.getId_local()>0){
				sqlextra +=" AND pr.id_local = "+criteria.getId_local();
			}
			
			
			if (criteria.getCod_promo()>0){
				sqlextra +=" AND pr.cod_promo = "+criteria.getCod_promo();
			}
			
			sql = "SELECT count(*) total " +
			" FROM PR_PROMOCION pr" +
			" JOIN BO_LOCALES l ON l.id_local = pr.id_local " +
			" WHERE 1=1 " + sqlextra;
			
			logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				result = rs.getLong("total");
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}


	public PromocionDTO getPromocionById(long id_promocion) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		PromocionDTO promo = null;

		try {
			
			String sql = "SELECT ID_PROMOCION, "
						+ " COD_PROMO            ,"
						+ " p.ID_LOCAL           ,"
						+ " VERSION              ,"
						+ " TIPO_PROMO           ,"
						+ " FINI                 ,"
						+ " FFIN                 ,"
						+ " DESCR                ,"
						+ " CANT_MIN             ,"
						+ " MONTO_MIN            ,"
						+ " MONTO1               ,"
						+ " DESCUENTO1           ,"
						+ " MONTO2               ,"
						+ " DESCUENTO2           ,"
						+ " MONTO3               ,"
						+ " DESCUENTO3           ,"
						+ " MONTO4               ,"
						+ " DESCUENTO4           ,"
						+ " MONTO5               ,"
						+ " DESCUENTO5           ,"
						+ " FP1                  ,"
						+ " NUM_CUOTA1           ,"
						+ " TCP1                 ,"
						+ " BENEFICIO1           ,"
						+ " FP2                  ,"
						+ " NUM_CUOTA2           ,"
						+ " TCP2                 ,"
						+ " BENEFICIO2           ,"
						+ " FP3                  ,"
						+ " NUM_CUOTA3           ,"
						+ " TCP3                 ,"
						+ " BENEFICIO3           ,"
						+ " CONDICION1           ,"
						+ " CONDICION2           ,"
						+ " CONDICION3           ,"
						+ " PRORRATEO            ,"
						+ " RECUPERABLE          ,"
						+ " CANAL                ,"
						+ " SUSTITUIBLE          ,"
						+ " BANNER				 ,"
						+ " FALTANTE			 ," 
						+ " NOM_LOCAL "
			+ " FROM PR_PROMOCION p " 
			+ " JOIN BO_LOCALES l ON p.id_local = l.id_local " 
			+ " WHERE ID_PROMOCION = ? " ;
			
			//logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");			
			stm.setLong(1, id_promocion);
			rs = stm.executeQuery();
			if (rs.next()) {
				promo = new  PromocionDTO();
				promo.setId_promocion(rs.getLong("ID_PROMOCION"));
				promo.setCod_promo(rs.getLong("COD_PROMO"));
				promo.setId_local(rs.getLong("ID_LOCAL"));
				promo.setVersion(rs.getLong("VERSION"));
				promo.setTipo_promo(rs.getLong("TIPO_PROMO"));
				promo.setFini(rs.getString("FINI"));
				promo.setFfin(rs.getString("FFIN"));
				promo.setDescr(rs.getString("DESCR"));
				promo.setCant_min(rs.getLong("CANT_MIN"));
				promo.setMonto_min(rs.getLong("MONTO_MIN"));
				
				promo.setMonto1(rs.getLong("MONTO1"));
				promo.setDescuento1(rs.getDouble("DESCUENTO1"));
				promo.setMonto2(rs.getLong("MONTO2"));
				promo.setDescuento2(rs.getDouble("DESCUENTO2"));
				promo.setMonto3(rs.getLong("MONTO3"));
				promo.setDescuento3(rs.getDouble("DESCUENTO3"));
				promo.setMonto4(rs.getLong("MONTO4"));
				promo.setDescuento4(rs.getDouble("DESCUENTO4"));
				promo.setMonto5(rs.getLong("MONTO5"));
				promo.setDescuento5(rs.getDouble("DESCUENTO5"));
				
				promo.setFp1(rs.getLong("FP1"));
				promo.setNum_cuota1(rs.getLong("NUM_CUOTA1"));
				promo.setTcp1(rs.getLong("TCP1"));
				promo.setBeneficio1(rs.getDouble("BENEFICIO1"));
				
				promo.setFp2(rs.getLong("FP2"));
				promo.setNum_cuota2(rs.getLong("NUM_CUOTA2"));
				promo.setTcp2(rs.getLong("TCP2"));
				promo.setBeneficio2(rs.getDouble("BENEFICIO2"));
				
				promo.setFp3(rs.getLong("FP3"));				
				promo.setNum_cuota3(rs.getLong("NUM_CUOTA3"));
				promo.setTcp3(rs.getLong("TCP3"));
				promo.setBeneficio3(rs.getDouble("BENEFICIO3"));
				
				promo.setCondicion1(rs.getLong("CONDICION1"));
				promo.setCondicion2(rs.getLong("CONDICION2"));
				promo.setCondicion3(rs.getLong("CONDICION3"));
				promo.setProrrateo(rs.getLong("PRORRATEO"));
				promo.setRecuperable(rs.getLong("RECUPERABLE"));
				promo.setCanal(rs.getLong("CANAL"));
				promo.setSustituible(rs.getString("SUSTITUIBLE"));
				promo.setBanner(rs.getString("BANNER"));
				promo.setFaltante(rs.getString("FALTANTE"));
				promo.setNom_local(rs.getString("NOM_LOCAL"));
			}
			

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		//logger.debug("ok");

		return promo;
	}
	

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getCategoriasSapByPromocionSeccion(long, int)
	 */
	public List getCategoriasSapByPromocionSeccion(long id_local, int tipo) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getCategoriasSapByPromocionSeccion x id_local:"+id_local+" tipo:"+tipo);
		
		String sql_tipo_seccion="";
		
		if ((tipo<900) || (tipo>914)){
			logger.debug("No es una promocion seccion");
			return null;
		}
		
		if (tipo==900)
			sql_tipo_seccion = "lunes";
		else if (tipo==901)
			sql_tipo_seccion = "martes";
		else if (tipo==902)
			sql_tipo_seccion = "miercoles";
		else if (tipo==903)
			sql_tipo_seccion = "jueves";
		else if (tipo==904)
			sql_tipo_seccion = "viernes";
		else if (tipo==905)
			sql_tipo_seccion = "sabado";
		else if (tipo==906)
			sql_tipo_seccion = "domingo";
		else if (tipo==907)
			sql_tipo_seccion = "esp1";
		else if (tipo==908)
			sql_tipo_seccion = "esp2";
		else if (tipo==909)
			sql_tipo_seccion = "esp3";
		else if (tipo==910)
			sql_tipo_seccion = "esp4";
		else if (tipo==911)
			sql_tipo_seccion = "esp5";
		else if (tipo==912)
			sql_tipo_seccion = "esp6";
		else if (tipo==913)
			sql_tipo_seccion = "esp7";		
		else if (tipo==914)
			sql_tipo_seccion = "esp8";
		
		
		try{ 
			
			String sql="";
			sql +=" SELECT c.id_catprod id_catprod, " +
				" c.id_catprod_padre id_catprod_padre, " +
				" c.descat descat, " +
				" c.cat_nivel cat_nivel, " +
				" c.cat_tipo cat_tipo, " +
				" c.estadoactivo estadoactivo " +
				" FROM pr_matriz_seccion m " +
				" JOIN bo_catprod c ON integer(id_catprod) = m.id_seccion " +
				" WHERE m."+sql_tipo_seccion+"=1 and m.id_local="+id_local;
			
			logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			while (rs.next()) {
				CategoriaSapDTO cat = new CategoriaSapDTO();
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setId_cat_padre(rs.getString("id_catprod_padre"));
				cat.setDescrip(rs.getString("descat"));
				cat.setNivel(rs.getInt("cat_nivel"));
				cat.setTipo(rs.getString("cat_tipo"));
				cat.setEstado(rs.getString("estadoactivo"));
				result.add(cat);
			}
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return result;
	}

	public List getPromocionProductosByTipo(int codigo, String tipo) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getPromocionesByCriteria x tipo:"+tipo+" codigo:"+codigo);
		
		String sql_tipo_promo="";
		if (tipo.equals(Constantes.PROMO_TIPO_EVENTO)){
			sql_tipo_promo += "cod_promo1";
		}
		else if (tipo.equals(Constantes.PROMO_TIPO_PERIODICA)){
			sql_tipo_promo += "cod_promo2";
		}
		else if (tipo.equals(Constantes.PROMO_TIPO_NORMAL)){
			sql_tipo_promo += "cod_promo3";
		}		
		else{//error en el tipo devuelve nulo
			throw new PedidosDAOException("Tipo de promocion invalida:"+tipo);
		}
		
		try{
			String sql="";
			sql +="SELECT pr.id_prodpromos id_prodpromos, " +
					" pr.id_producto id_producto, " +
					" pr.id_local id_local, " +
					" l.nom_local nom_local, " +
					" pr."+sql_tipo_promo +" cod_promo, " +
					" prod.cod_prod1 cod_prod1," +
					" prod.uni_med uni_med," +
					" prod.des_corta descr "+
					" FROM PR_PRODUCTO_PROMOS pr " +
					" JOIN BO_PRODUCTOS prod ON prod.id_producto = pr.id_producto " +	
					" JOIN PR_PROMOCION p ON p.cod_promo = "+ codigo +
					" AND p.cod_promo = pr."+sql_tipo_promo+" AND p.id_local = pr.id_local"+
					" JOIN BO_LOCALES l ON l.id_local = p.id_local ";

			
			
			logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ProductoPromocionDTO prod = new ProductoPromocionDTO();
				prod.setId_prodpromos(rs.getLong("id_prodpromos"));
				prod.setId_producto(rs.getLong("id_producto"));
				prod.setId_local(rs.getLong("id_local"));
				prod.setNom_local(rs.getString("nom_local"));
				prod.setCod_prod1(rs.getString("cod_prod1"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setDescr(rs.getString("descr"));
				prod.setTipo(tipo);
				prod.setCod_promo(rs.getLong("cod_promo"));
				result.add(prod);
			}
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return result;
	}		
	
	/*
	public List getPromocionProductos_old(long id_promocion) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getPromocionesByCriteria:");
		try {			
			String sql="";
			sql = "SELECT pr.id_prodpromos id_prodpromos, " +
					" pr.id_producto id_producto, " +
					" pr.id_local id_local, " +
					" l.nom_local nom_local, " +
					" pr.cod_promo1 prn_id_promocion, " +
					" pr.cod_promo2 prp_id_promocion, " +
					" pr.cod_promo3 pre_id_promocion, " +
					" prod.cod_prod1 cod_prod1, " +
					" prod.uni_med uni_med, " +
					" prod.des_corta descr, " +
					//promocion 1 : Normal = PRN
					" prn.cod_promo prn_cod_promo, " +
					" prn.tipo_promo prn_tipo_promo, " +
					" prn.id_local prn_id_local, " +
					" prn.descr prn_descr, " +
					" prn.fini prn_fini, " +
					" prn.ffin prn_ffin, " +
					" pnl.nom_local prn_nom_local, " +
					//promocion 2 : Periodica PRP
					" prp.cod_promo prp_cod_promo, " +
					" prp.tipo_promo prp_tipo_promo, " +
					" prp.id_local prp_id_local, " +
					" prp.descr prp_descr, " +
					" prp.fini prp_fini, " +
					" prp.ffin prp_ffin, " +
					" ppl.nom_local prp_nom_local, " +
					//promocion 3 : Evento PRE
					" pre.cod_promo pre_cod_promo," +
					" pre.tipo_promo pre_tipo_promo, " +
					" pre.id_local pre_id_local, " +
					" pre.descr pre_descr, " +
					" pre.fini pre_fini, " +
					" pre.ffin pre_ffin, " +
					" pel.nom_local pre_nom_local " +
				" FROM PR_PRODUCTO_PROMOS pr " +
				" JOIN BO_LOCALES l ON l.id_local = pr.id_local " +
				" JOIN BO_PRODUCTOS prod ON prod.id_producto = pr.id_producto " +
				" LEFT JOIN PR_PROMOCION prn ON prn.cod_promo = pr.cod_promo1 AND prn.id_promocion="+id_promocion +
				" LEFT JOIN PR_PROMOCION prp ON prp.cod_promo = pr.cod_promo2 AND prp.id_promocion="+id_promocion +
				" LEFT JOIN PR_PROMOCION pre ON pre.cod_promo = pr.cod_promo3 AND pre.id_promocion="+id_promocion +
				" LEFT JOIN BO_LOCALES pnl ON pnl.id_local = prn.id_local " +
				" LEFT JOIN BO_LOCALES ppl ON ppl.id_local = prp.id_local " +
				" LEFT JOIN BO_LOCALES pel ON pel.id_local = pre.id_local " +
				" WHERE 1=1 ";
			
			logger.debug("SQL query: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);	
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ProductoPromocionDTO prod = new ProductoPromocionDTO();
				prod.setId_prodpromos(rs.getLong("id_prodpromos"));
				prod.setId_producto(rs.getLong("id_producto"));
				prod.setId_local(rs.getLong("id_local"));
				prod.setNom_local(rs.getString("nom_local"));
				prod.setCod_promo1(rs.getLong("prn_id_promocion"));
				prod.setCod_promo2(rs.getLong("prp_id_promocion"));
				prod.setCod_promo3(rs.getLong("pre_id_promocion"));
				prod.setCod_prod1(rs.getString("cod_prod1"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setDescr(rs.getString("descr"));
				
				MonitorPromocionesDTO mon1 = new MonitorPromocionesDTO();				
				mon1.setId_promocion(rs.getLong("prn_id_promocion"));				
				mon1.setCod_promocion(rs.getString("prn_cod_promo"));
				mon1.setTipo_promo(rs.getString("prn_tipo_promo"));	
				mon1.setId_local(rs.getLong("prn_id_local"));				
				mon1.setNom_local(rs.getString("prn_nom_local"));				
				mon1.setDescripcion(rs.getString("prn_descr"));				
				mon1.setFecha_inicio(rs.getString("prn_fini"));				
				mon1.setFecha_fin(rs.getString("prn_ffin"));
				
				prod.setPromo_normal(mon1);
				
				MonitorPromocionesDTO mon2 = new MonitorPromocionesDTO();				
				mon2.setId_promocion(rs.getLong("prp_id_promocion"));				
				mon2.setCod_promocion(rs.getString("prp_cod_promo"));
				mon2.setTipo_promo(rs.getString("prp_tipo_promo"));	
				mon2.setId_local(rs.getLong("prp_id_local"));				
				mon2.setNom_local(rs.getString("prp_nom_local"));				
				mon2.setDescripcion(rs.getString("prp_descr"));				
				mon2.setFecha_inicio(rs.getString("prp_fini"));				
				mon2.setFecha_fin(rs.getString("prp_ffin"));
				
				prod.setPromo_normal(mon2);
				
				MonitorPromocionesDTO mon3 = new MonitorPromocionesDTO();				
				mon3.setId_promocion(rs.getLong("pre_id_promocion"));				
				mon3.setCod_promocion(rs.getString("pre_cod_promo"));
				mon3.setTipo_promo(rs.getString("pre_tipo_promo"));	
				mon3.setId_local(rs.getLong("pre_id_local"));				
				mon3.setNom_local(rs.getString("pre_nom_local"));				
				mon3.setDescripcion(rs.getString("pre_descr"));				
				mon3.setFecha_inicio(rs.getString("pre_fini"));				
				mon3.setFecha_fin(rs.getString("pre_ffin"));
				
				prod.setPromo_normal(mon3);
				
				result.add(prod);
			}
			rs.close();
			stm.close();
			releaseConnection();
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("ok");

		return result;
	}
*/
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#updPromocion(cl.bbr.jumbocl.promos.dto.PromocionDTO)
	 */
	public boolean updPromocion(PromocionDTO dto) throws PedidosDAOException {
		PreparedStatement stm = null;
		boolean result = false;
		try {
			String Sql = "UPDATE pr_promocion set " +
					    " descr = ?, " +
					    " sustituible = ?, " +
					    " faltante = ?, " +
					    " banner = ? " +
						" where cod_promo = ? ";
	
			logger.debug("SQL :"+Sql);	
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setString(1, dto.getDescr());
			stm.setString(2, dto.getSustituible());
			stm.setString(3, dto.getFaltante());
			stm.setString(4, dto.getBanner());
			stm.setLong(5, dto.getCod_promo());
						
			int i = stm.executeUpdate();
			if (i>0) {
	            result = true;
	        }
			logger.debug("Actualiza : " + i );

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("result : "+result);
		return result;
	}	

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#getTcpPedidosByIdPedidoIdTcp(long, long)
	 */
	public TcpPedidoDTO getTcpPedidosByIdPedidoIdTcp(long id_pedido, long id_tcp) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		TcpPedidoDTO resultado = new TcpPedidoDTO();
		
		//logger.debug("en getPromosPrioridadProducto:");
		try {			
			
			
			String sql = " SELECT t.id_tcp id_tcp, " +
						" t.id_pedido id_pedido, " +
						" t.nro_tcp nro_tcp, " +
						" t.cant_max cant_max, " +
						" t.cant_util cant_util " +				
			" FROM BO_TCP t" +
			" WHERE t.id_pedido  = "+id_pedido+
			" AND t.id_tcp ="+id_tcp;
					
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {				
				resultado.setNro_tcp(rs.getInt("nro_tcp"));				
				resultado.setCant_max(rs.getInt("cant_max"));
				resultado.setCant_util(rs.getInt("cant_util"));
				resultado.setId_pedido(id_pedido);
				resultado.setId_tcp(id_tcp);
			}
			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#delTcpPedido(long)
	 */
	public void delTcpPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		logger.debug("En setTcpPedido...");
		
		try {
			
			String Sql = " DELETE FROM BO_TCP WHERE id_pedido ="+id_pedido;
	
			logger.debug("SQL :"+Sql);			
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);			
						
			int i = stm.executeUpdate();
			logger.debug("Eliminados TCP:"+i);

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}
	
	

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#getCuponesPedidoByIdCupon(long)
	 */
	public CuponPedidoDTO getCuponesPedidoByIdCupon(long id_cupon) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		CuponPedidoDTO resultado = new CuponPedidoDTO();
		
		logger.debug("en getCuponesPedidoByIdCupon:");
		try {			
			String sql="SELECT id_cupon, id_pedido, nro_cupon, id_tcp " +				
			" FROM BO_CUPON " +
			" WHERE id_cupon  = "+id_cupon;					
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				resultado.setId_cupon(id_cupon);				
				resultado.setId_pedido(rs.getLong("id_pedido"));
				resultado.setId_tcp(rs.getLong("id_tcp"));
				resultado.setNro_cupon(rs.getString("nro_cupon"));				
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#delCuponPedido(long)
	 */
	public void delCuponPedido(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		logger.debug("En delCuponPedido...");
		
		try {
			
			String Sql = " DELETE FROM BO_CUPON WHERE id_pedido ="+id_pedido;
	
			logger.debug("SQL :"+Sql);			
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);			
						
			int i = stm.executeUpdate();
			logger.debug("Eliminados CUPON:"+i);

		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#getPedidoDatos(long)
	 */
	public PedidoDatosSocketDTO getPedidoDatos(long id_pedido) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		PedidoDatosSocketDTO resultado = new PedidoDatosSocketDTO();
		
		logger.debug("en getPedidoDatos:");
		try {			
			String sql="";
			
			sql = "select p.id_pedido id_pedido, " 
				+" p.rut_cliente rut, " 
				+" l.cod_local_pos cod_local_pos "
				+" from bo_pedidos p "
				+" join bo_locales l on l.id_local = p.id_local "
				+" where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and p.id_pedido =  "+id_pedido;					
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {				
				resultado.setId_pedido(rs.getLong("id_pedido"));				
				resultado.setCod_local_pos(rs.getInt("cod_local_pos"));
				resultado.setRut(rs.getLong("rut"));				
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#insSaf(cl.bbr.jumbocl.promos.dto.SafDTO)
	 */
	public int insSaf(SafDTO dto) throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int result=0;
		
		logger.debug("En insSaf...");
		
		try {
			
			String Sql = " INSERT INTO BO_QUEMACUPON_SAF (estado, mensaje) " +
					" VALUES(?,?) ";
	
			logger.debug("SQL :"+Sql);	
			logger.debug( "estado:" + dto.getEstado() );
			logger.debug( "msg:[" + dto.getMsg()+"]" );
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql,Statement.RETURN_GENERATED_KEYS);
			stm.setString(1, dto.getEstado());
			stm.setString(2, dto.getMsg());
						
			int i = stm.executeUpdate();
			if (i>0) {
				rs = stm.getGeneratedKeys();
				
				if (rs.next())
					result = rs.getInt(1);
	        }

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try {
				
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("id_tcp : "+result);
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#updSaf(cl.bbr.jumbocl.promos.dto.SafDTO)
	 */
	public void updSaf(SafDTO dto) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		logger.debug("En updSaf...");
		
		try {
			
			String Sql = " UPDATE BO_QUEMACUPON_SAF " +
				" SET estado = ? , mensaje =? WHERE id_saf = ? ";
	
			logger.debug("SQL :"+Sql);			
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setString(1, dto.getEstado());
			stm.setString(2, dto.getMsg()); 
			stm.setLong(3, dto.getId_saf());
			int i = stm.executeUpdate();
			logger.debug("Actualizados :"+i);
			

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);			
		} finally {
			try{
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}	
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#getSafById(long)
	 */
	public SafDTO getSafById(long id_saf) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		SafDTO result = new SafDTO();
		logger.debug("en getSafById:");

		try {

			String sql = "SELECT id_saf, mensaje, estado " +				
				" FROM BO_QUEMACUPON_SAF " +
				" WHERE id_saf =  "+id_saf;

			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {				
				result.setId_saf(rs.getLong("id_saf"));
				result.setMsg(rs.getString("mensaje"));
				result.setEstado(rs.getString("estado"));
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.promos.dao.PromocionesDAO#getSafByEstado(java.lang.String)
	 */
	public List getSafByEstado(String estados) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		List result = new ArrayList();
		logger.debug("en getSaf:");

		try {			
			String sql = "SELECT id_saf, mensaje, estado " +				
						" FROM BO_QUEMACUPON_SAF " +
						" WHERE estado IN ("+estados+")";
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				SafDTO saf = new SafDTO();
				saf.setId_saf(rs.getLong("id_saf"));
				saf.setMsg(rs.getString("mensaje"));
				saf.setEstado(rs.getString("estado"));								
				result.add(saf);
			}

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#getTcpPedidosByIdPedidoNroTcp(long, long)
	 */
	public TcpPedidoDTO getTcpPedidosByIdPedidoNroTcp(long id_pedido, int nro_tcp) throws PedidosDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		TcpPedidoDTO resultado = new TcpPedidoDTO();
		
		//logger.debug("en getPromosPrioridadProducto:");
		try {			
			String sql = " SELECT t.id_tcp id_tcp, " +
						" t.id_pedido id_pedido, " +
						" t.nro_tcp nro_tcp, " +
						" t.cant_max cant_max, " +
						" t.cant_util cant_util " +				
			" FROM BO_TCP t" +
			" WHERE t.id_pedido  = "+id_pedido+
			" AND t.nro_tcp ="+nro_tcp;
					
			
			logger.debug("SQL query: " + sql );
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {				
				resultado.setNro_tcp(rs.getInt("nro_tcp"));				
				resultado.setCant_max(rs.getInt("cant_max"));
				resultado.setCant_util(rs.getInt("cant_util"));
				resultado.setId_pedido(rs.getInt("id_pedido"));
				resultado.setId_tcp(rs.getInt("id_tcp"));
			}

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.PedidosDAO#updFlagRecalculoPedido(cl.bbr.jumbocl.pedidos.dto.PedidoDTO)
	 */
	public void updFlagRecalculoPedido(PedidoDTO dto) throws PedidosDAOException {
		PreparedStatement stm = null;
		
		logger.debug("En updFlagRecalculoPëdido...");
		
		try {
			
			int flg_rec_elim=0;
			int flg_rec_mp=0;
			
			if (dto.isFlg_recalc_prod())
				flg_rec_elim=1;
			
			if (dto.isFlg_recalc_mp())
				flg_rec_mp=1;
			
				
			String Sql = " UPDATE BO_PEDIDOS SET " +
				"  flag_rec_elim = ? " +
				", flag_rec_mp =? " +
				"WHERE id_pedido = ? ";
	
			logger.debug("SQL :"+Sql);			
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setInt(1, flg_rec_elim);
			stm.setInt(2, flg_rec_mp);
			stm.setLong(3, dto.getId_pedido());
			int i = stm.executeUpdate();
			logger.debug("Actualizados :"+i);
			
	
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new PedidosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}

    /**
     * @param idPedido
     * @param id
     * @param criterio
     */
    public void updateCriterioSustitucionEnPedido(long idPedido, long idProducto, CriterioSustitutoDTO criterio) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        logger.debug("En updateCriterioSustitucionEnPedido...");        
        try {   
            String sql =" UPDATE BO_DETALLE_PEDIDO SET " +
                        " id_criterio = ?, desc_criterio = ? " +
                        " WHERE id_pedido = ? AND id_producto = ? ";
    
            logger.debug("SQL :"+sql);  

            conn = this.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, criterio.getIdCriterio());
            if ( criterio.getIdCriterio() == 4 ) {
                stm.setString(2, criterio.getSustitutoCliente());
            } else {
                stm.setString(2, "");
            }
            stm.setLong(3, idPedido);
            stm.setLong(4, idProducto);
            stm.executeUpdate();
            
          
        }catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        }catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        
    }

    /**
     * @param idProducto
     * @return
     */
    public long getIdProductoBOByIdProductoFO(long idProductoFO) throws PedidosDAOException {
        PreparedStatement stm = null; 
        ResultSet rs = null;
        long idProductoBO = 0;
        try {           
            String sql =  " SELECT pro_id_bo " +
                          " FROM FODBA.FO_PRODUCTOS " +
                          " WHERE pro_id = ? ";
                    
            
            logger.debug("SQL getIdProductoBOByIdProductoFO: " + sql );
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idProductoFO);
            rs = stm.executeQuery();
            if (rs.next()) {                
                idProductoBO = rs.getLong("pro_id_bo");
            }

            
        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idProductoBO;
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public SustitutoDTO getCriterioClientePorProducto(long idCliente, long idProducto) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        SustitutoDTO prod = new SustitutoDTO();
        prod.setIdCriterio(1);
        
        String Sql = "select sc.ID_CRITERIO, sc.DESC_CRITERIO " +
                     "from fodba.fo_sustitutos_clientes sc " +
                     "where cli_id = ? and pro_id = ?";
        
        logger.debug("SQL getCriterioClientePorProducto :"+Sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, idCliente);
            stm.setLong(2, idProducto);
            rs = stm.executeQuery();
            if (rs.next()) {
                prod.setIdCriterio(rs.getLong("ID_CRITERIO"));
                prod.setDescCriterio(rs.getString("DESC_CRITERIO"));                
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return prod;
    }

    /**
     * @param idProducto
     * @param idCliente
     * @return
     */
    public boolean existeCriterio(long idProducto, long idCliente) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean existe = false;
        
        String Sql = "select sc.ID_CRITERIO, sc.DESC_CRITERIO " +
                     "from fodba.fo_sustitutos_clientes sc " +
                     "where cli_id = ? and pro_id = ?";
        
        logger.debug("SQL getCriterioClientePorProducto :"+Sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(Sql + " WITH UR");
            stm.setLong(1, idCliente);
            stm.setLong(2, idProducto);
            rs = stm.executeQuery();
            if (rs.next()) {
                existe = true;            
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return existe;
    }

    /**
     * @param idProducto
     * @param idCliente
     * @param idCriterio
     * @param descCriterio
     */
    public void modCriterioCliente(long idProducto, long idCliente, long idCriterio, String descCriterio) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        logger.debug("En updateCriterioSustitucionEnPedido...");        
        try {   
            String sql ="UPDATE fodba.fo_sustitutos_clientes " +
                        "set id_criterio = ?, desc_criterio = ?, fecha_modificacion = ?, asigno_cliente = ? " +
                        "where pro_id = ? and cli_id = ?";            
    
            logger.debug("SQL :"+sql);  
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong( 1, idCriterio );
            if ( idCriterio == 4) {
                stm.setString(2, descCriterio );
            } else {
                stm.setNull(2, java.sql.Types.VARCHAR );
            }
            stm.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
            stm.setString(4, "S");
            stm.setLong(5, idProducto );
            stm.setLong(6, idCliente );
            stm.executeUpdate();
            
         
        }catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        }catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}       
    }

    /**
     * @param idProducto
     * @param idCliente
     * @param idCriterio
     * @param descCriterio
     */
    public void addCriterioCliente(long idProducto, long idCliente, long idCriterio, String descCriterio) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        logger.debug("En addCriterioCliente...");        
        try {   
            String sql ="insert into fodba.fo_sustitutos_clientes " +
                        "(pro_id, cli_id, id_criterio, desc_criterio, fecha_modificacion, asigno_cliente) " +
                        "values " +
                        "(?,?,?,?,?,?)";
    
            logger.debug("SQL :"+sql);  
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, idProducto );
            stm.setLong(2, idCliente );
            stm.setLong(3, idCriterio );
            if ( idCriterio == 4) {
                stm.setString(4, descCriterio );
            } else {
                stm.setNull(4, java.sql.Types.VARCHAR );
            }
            stm.setTimestamp(5, new Timestamp(new GregorianCalendar().getTimeInMillis()));
            stm.setString(6, "S");
            stm.executeUpdate();
            
            
        }catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        }catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @param idRonda
     * @return
     */
    public List getProductosPedidoRonda(long idPedido, long idRonda) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List list_prod = new ArrayList();
        
        
        logger.debug("-----------------------------------------------------------------");
        logger.debug("Ejecución DAO: " + getClass().getName()+"getProductosPedidoRonda");
        logger.debug("-----------------------------------------------------------------");
        
        logger.debug("Parametros getProductosPedidoRonda:");
        logger.debug("numero_pedido:"+idPedido);
        
        String sql = "SELECT  distinct DP.ID_PEDIDO, DP.ID_DETALLE, DP.COD_PROD1, DP.UNI_MED, S.NOMBRE NOM_SECTOR, DP.ID_SECTOR, "
                   + "        DP.ID_PRODUCTO, PF.PRO_ID ID_PROD_FO, DP.DESCRIPCION DESCR, DP.CANT_SOLIC CANTIDAD, DP.OBSERVACION OBS, " 
                   + "        DP.PREPARABLE, DP.CON_NOTA NOTA, DP.PRECIO, DP.PESABLE, DP.CANT_PICK, " 
                   + "        DP.CANT_FALTAN, DP.CANT_SPICK, DP.TIPO_SEL, P.ID_CATPROD, " 
                   + "        DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_PEDIDO DP " 
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON DP.ID_CRITERIO = SC.ID_CRITERIO "
                   + "     LEFT JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR    = DP.ID_SECTOR "
                   + "     JOIN BODBA.BO_PRODUCTOS   P ON DP.ID_PRODUCTO = P.ID_PRODUCTO "
                   + "     JOIN FODBA.FO_PRODUCTOS  PF ON PF.PRO_ID_BO   = P.ID_PRODUCTO " 
                   + "     join BODBA.BO_DETALLE_RONDAS DT on (DT.ID_DETALLE = DP.ID_DETALLE) "
                   + "WHERE DP.ID_PEDIDO = " + idPedido + " ";
        if( idRonda > 0 ) {
            sql += " AND DT.ID_RONDA = " + idRonda + " ";
        }
        sql +=  " ORDER BY descr";
        
        logger.debug("SQL :"+sql);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                prod.setId_detalle(rs.getInt("ID_DETALLE"));
                prod.setId_producto(rs.getInt("ID_PRODUCTO"));
                prod.setId_prod_fo(rs.getInt("ID_PROD_FO"));
                prod.setCod_producto(rs.getString("COD_PROD1"));
                prod.setUnid_medida(rs.getString("UNI_MED"));
                prod.setId_pedido(rs.getLong("ID_PEDIDO"));
                prod.setId_sector(rs.getInt("ID_SECTOR"));
                prod.setDescripcion(rs.getString("DESCR"));
                prod.setObservacion(rs.getString("OBS"));
                prod.setSector(rs.getString("NOM_SECTOR"));
                prod.setCant_solic(rs.getDouble("CANTIDAD"));
                prod.setPrecio(rs.getDouble("PRECIO"));
                prod.setPesable(rs.getString("PESABLE"));
                prod.setCant_pick(rs.getDouble("CANT_PICK"));
                prod.setCant_faltan(rs.getDouble("CANT_FALTAN"));
                prod.setCant_spick(rs.getDouble("CANT_SPICK"));
                prod.setTipoSel(rs.getString("TIPO_SEL"));
                prod.setId_catprod(rs.getString("ID_CATPROD"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));             
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    prod.setDescCriterio(rs.getString("criterio"));
                } 
                
                list_prod.add(prod);
            }
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return list_prod;
    }

    /**
     * @return
     */
    public List getGruposListado() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List gruposLista = new ArrayList();
        
        String sql = "select lg.ID_LST_GRUPO, lg.NOMBRE nombre_grupo, lg.ACTIVADO, " +
                     "tg.ID_LST_TIPO_GRUPO, tg.DESCRIPCION nombre_tipo " +
                     "from fodba.fo_lst_grupo lg " +
                     "inner join fodba.fo_lst_tipo_grupo tg on (tg.ID_LST_TIPO_GRUPO = lg.ID_LST_TIPO_GRUPO)";
        
        logger.debug("SQL getGruposListado :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaGrupoDTO lg = new ListaGrupoDTO();
                lg.setIdListaGrupo(rs.getInt("ID_LST_GRUPO"));
                lg.setNombre(rs.getString("nombre_grupo"));
                lg.setActivado(rs.getString("ACTIVADO"));
                ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                ltg.setIdListaTipoGrupo(rs.getInt("ID_LST_TIPO_GRUPO"));
                ltg.setDescripcion(rs.getString("nombre_tipo"));
                lg.setTipo(ltg);
                gruposLista.add(lg);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return gruposLista;
    }

    /**
     * @return
     */
    public List getTiposGruposListado() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List tiposGruposLista = new ArrayList();
        
        String sql = "select tg.ID_LST_TIPO_GRUPO, tg.DESCRIPCION nombre_tipo, tg.activado " +
                     "from fodba.fo_lst_tipo_grupo tg ";
        
        logger.debug("SQL getTiposGruposListado :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                ltg.setIdListaTipoGrupo(rs.getInt("ID_LST_TIPO_GRUPO"));
                ltg.setDescripcion(rs.getString("nombre_tipo"));
                ltg.setActivado(rs.getString("activado"));
                tiposGruposLista.add(ltg);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return tiposGruposLista;
    }

    /**
     * @param lg
     * @return
     */
    public long addGrupoLista(ListaGrupoDTO lg) throws PedidosDAOException {
        long idGrupoLista = 0;
        PreparedStatement   stm     = null;
        ResultSet rs = null;
        
        String sql = "INSERT INTO fodba.fo_lst_grupo " +
                     "(NOMBRE, ACTIVADO, ID_LST_TIPO_GRUPO) " +
                     " VALUES (?,?,?) ";
        
        logger.debug("en addGrupoLista");
        logger.debug("SQL: " + sql);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,lg.getNombre());
            stm.setString(2,lg.getActivado());
            stm.setLong(3, lg.getTipo().getIdListaTipoGrupo());
            
            stm.executeUpdate();
            
            rs = stm.getGeneratedKeys();
            
            if (rs.next())
                idGrupoLista = rs.getInt(1);
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idGrupoLista;
    }

    /**
     * @param lg
     */
    public void modGrupoLista(ListaGrupoDTO lg) throws PedidosDAOException {
        PreparedStatement   stm     = null;
        
        String sql = "UPDATE fodba.fo_lst_grupo " +
                     "SET NOMBRE = ?, ACTIVADO = ?, ID_LST_TIPO_GRUPO = ? " +
                     "WHERE ID_LST_GRUPO = ? ";
        
        logger.debug("en modGrupoLista");
        logger.debug("SQL: " + sql);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            stm.setString(1,lg.getNombre());
            stm.setString(2,lg.getActivado());
            stm.setLong(3, lg.getTipo().getIdListaTipoGrupo());
            stm.setLong(4, lg.getIdListaGrupo());
            stm.executeUpdate();
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idGrupoListado
     * @return
     */
    public ListaGrupoDTO getGrupoListadoById(long idGrupoListado) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ListaGrupoDTO lg = new ListaGrupoDTO();
        
        String sql = "select lg.ID_LST_GRUPO, lg.NOMBRE nombre_grupo, lg.ACTIVADO, " +
                     "tg.ID_LST_TIPO_GRUPO, tg.DESCRIPCION nombre_tipo " +
                     "from fodba.fo_lst_grupo lg " +
                     "inner join fodba.fo_lst_tipo_grupo tg on (tg.ID_LST_TIPO_GRUPO = lg.ID_LST_TIPO_GRUPO) " +
                     "where lg.ID_LST_GRUPO = ?";
        
        logger.debug("SQL getTiposGruposListado :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1,idGrupoListado);
            rs = stm.executeQuery();
            if (rs.next()) {
                lg.setIdListaGrupo(rs.getLong("ID_LST_GRUPO"));
                lg.setNombre(rs.getString("nombre_grupo"));
                lg.setActivado(rs.getString("ACTIVADO"));
                ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                ltg.setIdListaTipoGrupo(rs.getInt("ID_LST_TIPO_GRUPO"));
                ltg.setDescripcion(rs.getString("nombre_tipo"));
                lg.setTipo(ltg);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lg;
    }

    /**
     * @param idGrupoLista
     */
    public void delGrupoLista(long idGrupoLista) throws PedidosDAOException {
        PreparedStatement   stm     = null;
        
        String sql0 = "DELETE FROM FODBA.FO_LST_GRP_CH T WHERE id_lst_grupo = ?";
        
        String sql1 = "DELETE FROM fodba.fo_lst_grupo WHERE ID_LST_GRUPO = ?";
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql0);
            stm.setLong(1, idGrupoLista);
            stm.executeUpdate();
            stm.close();
            
            stm = conn.prepareStatement(sql1);
            stm.setLong(1, idGrupoLista);
            stm.executeUpdate();

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idGrupoLista
     * @return
     */
    public List clienteListasEspeciales(long idGrupoLista) throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT ch.ch_id, ch.ch_alias, ch.ch_unidades, 'Especial' as ch_lugar, " +
                        "date(ch.ch_fec_crea) as ch_fecha, 'E' as ch_tipo, " +
                        "CASE WHEN (lg.id_lst_grupo is null) THEN 0 ELSE lg.id_lst_grupo END AS id_lst_grupo, " +
                        "CASE WHEN (lg.nombre is null) THEN '- SIN GRUPO -' ELSE lg.nombre END AS nombre_grupo " +
                        "FROM fodba.fo_ch_compra_historicas ch " +
                        "left outer join fodba.fo_lst_grp_ch lgc on (lgc.CH_ID = ch.CH_ID) " +
                        "left outer join fodba.fo_lst_grupo lg on (lg.ID_LST_GRUPO = lgc.ID_LST_GRUPO) " +
                        "WHERE ch.ch_tipo = 'E' and ch.CH_CLI_ID is null ";
            
            if (idGrupoLista > 0) {
                sql += "and lgc.id_lst_grupo = " + idGrupoLista + " ";
            }
            sql += "ORDER BY ch_fecha desc";    
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            logger.debug("SQL clienteListasEspeciales: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO compras = new UltimasComprasDTO();
                compras.setId(rs.getLong("ch_id"));
                compras.setNombre( rs.getString("ch_alias") );
                compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
                compras.setLugar_compra(rs.getString("ch_lugar"));
                compras.setUnidades(rs.getDouble("ch_unidades"));
                compras.setTipo(rs.getString("ch_tipo"));
                ListaGrupoDTO grupo = new ListaGrupoDTO();
                grupo.setIdListaGrupo(rs.getLong("id_lst_grupo"));
                grupo.setNombre(rs.getString("nombre_grupo"));
                compras.setGrupoLista(grupo);
                lista.add(compras);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }

    /**
     * @param hash
     * @return
     */
    public String addListasEspeciales(Hashtable hash) throws PedidosDAOException {
        String msg = "";
   
        Enumeration alias = hash.keys();
        while (alias.hasMoreElements()) {
            String titulo = (String) alias.nextElement();
            List productos = (List) hash.get(titulo);
            long ini = System.currentTimeMillis();
            long id = insertarEncabezado(conn, titulo, productos.size());
            long ini2 = System.currentTimeMillis();
            logger.debug("tiempo: " + ((ini2 - ini) / 1000.0));
            insertarDetalle(conn, id, productos);
            logger.debug("tiempo: " + ((System.currentTimeMillis() - ini) / 1000.0));
        }
        
        msg += "<br>Se agregaron " + hash.size() + " listas nuevas.<br>";

        return msg;
    }
    
    private long insertarEncabezado(Connection con, String titulo, int cantidad) {
        logger.debug("Insertando encabezado receta");
        long id = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "insert into fodba.fo_ch_compra_historicas                                          "
                + "(CH_ALIAS, CH_FEC_CREA, CH_TIPO, CH_CLI_ID, CH_UNIDADES)                              "
                + " values ('" + titulo + "', CURRENT_TIMESTAMP, 'E', null," + cantidad + ")";
       
        try {
        	stm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        	stm.executeUpdate();
            
            rs = stm.getGeneratedKeys();        
            if (rs.next())
                id = rs.getInt(1);
            

            
        } catch (SQLException e) {
            e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}       
        logger.debug("FIN");
        return id;        
    }
    /*FIXME
     * 2012-07-10
     *
     */
    private void insertarDetalle(Connection con, long idCh, List productos) {
        logger.debug("Insertando detalle");
        Statement st;
        try {
            st = con.createStatement();
            for (int i = 0; i < productos.size(); i++) {
                ProductosPedidoDTO producto = (ProductosPedidoDTO) productos.get(i);
                String sql = "insert into FODBA.FO_CH_PRODUCTOS(                                            "
                        + "CHP_CH_ID, CHP_CANTIDAD, CHP_PRO_ID)                                             "
                        + "select ch_id, " + producto.getCantidad() + ", " + producto.getId_producto()
                        + " from fodba.fo_ch_compra_historicas                                               "
                        + "where CH_ID = " + idCh + "";
                st.addBatch(sql);
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("No se pudo agregar producto de lista en insertarDetalle:" + e.getMessage());
        }        
        logger.debug("Fin");
    }

    

    /**
     * @param idLista
     * @return
     */
    public List getGruposAsociadosLista(long idLista) throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT distinct gr.ID_LST_GRUPO, gr.NOMBRE nombre_grupo " +
                        "FROM fodba.fo_lst_grupo gr " +
                        "WHERE gr.ACTIVADO = '1' and " +
                        "gr.ID_LST_GRUPO in (" +
                        "   select distinct lgc.ID_LST_GRUPO from fodba.fo_lst_grp_ch lgc where lgc.CH_ID = ? " +
                        ") ORDER BY gr.ID_LST_GRUPO";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLista);
            logger.debug("SQL getGruposAsociadosLista: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaGrupoDTO grupo = new ListaGrupoDTO();
                grupo.setIdListaGrupo(rs.getLong("id_lst_grupo"));
                grupo.setNombre(rs.getString("nombre_grupo"));
                lista.add(grupo);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }

    /**
     * @param idLista
     * @return
     */
    public List getGruposNoAsociadosLista(long idLista) throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT distinct gr.ID_LST_GRUPO, gr.NOMBRE nombre_grupo " +
                        "FROM fodba.fo_lst_grupo gr " +
                        "WHERE gr.ACTIVADO = '1' and " +
                        "gr.ID_LST_GRUPO not in (" +
                        "   select distinct lgc.ID_LST_GRUPO from fodba.fo_lst_grp_ch lgc where lgc.CH_ID = ? " +
                        ") ORDER BY gr.ID_LST_GRUPO";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLista);
            logger.debug("SQL getGruposNoAsociadosLista: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaGrupoDTO grupo = new ListaGrupoDTO();
                grupo.setIdListaGrupo(rs.getLong("id_lst_grupo"));
                grupo.setNombre(rs.getString("nombre_grupo"));
                lista.add(grupo);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }

    /**
     * @param idLista
     * @return
     */
    public UltimasComprasDTO getListaById(long idLista) throws PedidosDAOException {
        UltimasComprasDTO listaCompra = new UltimasComprasDTO();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT ch.ch_id, ch.ch_alias, ch.ch_unidades, 'Especial' as ch_lugar, " +
                        "date(ch.ch_fec_crea) as ch_fecha, 'E' as ch_tipo " +
                        "FROM fodba.fo_ch_compra_historicas ch " +
                        "WHERE ch.CH_ID = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLista);
            logger.debug("SQL getListaById: " + sql);
            rs = stm.executeQuery();
            if (rs.next()) {                
                listaCompra.setId(rs.getLong("ch_id"));
                listaCompra.setNombre(rs.getString("ch_alias"));
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return listaCompra;
    }

    /**
     * @param lista
     * @param grupos
     */
    public void modLista(UltimasComprasDTO lista, String[] grupos) throws PedidosDAOException {
        PreparedStatement stm = null;
        try {
            
            String sqlDel = "delete from fodba.fo_lst_grp_ch where ch_id = ?";
            String sqlUpd = "update fodba.fo_ch_compra_historicas " +
                            "set ch_alias = ? where ch_id = ?";            
            String sqlIns = "insert into fodba.fo_lst_grp_ch " +
                            "(id_lst_grupo, ch_id) values (?,?)";
            
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sqlDel);
            stm.setLong(1, lista.getId());
            stm.executeUpdate();
           
            
            stm = conn.prepareStatement(sqlUpd);
            stm.setString(1, lista.getNombre());
            stm.setLong(2, lista.getId());
            stm.executeUpdate();
          
            
            stm = conn.prepareStatement(sqlIns);
            for (int i=0; i < grupos.length; i++) {
                if ( !grupos[i].equalsIgnoreCase("") ) {
                    stm.setLong(1, Long.parseLong( grupos[i] ));
                    stm.setLong(2, lista.getId());
                    stm.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idLista
     * @param idGrupo
     */
    public void delListaEspecial(long idLista, long idGrupo) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean borrar = true;
        
        try {
            String sqlDel = "delete from fodba.fo_lst_grp_ch where ch_id = ? and id_lst_grupo = ?";
            String sqlSel = "select id_lst_grupo from fodba.fo_lst_grp_ch where ch_id = ?";            
            String sqlDelListaDet = "delete from fodba.fo_ch_productos where chp_ch_id = ?";
            String sqlDelLista = "delete from fodba.fo_ch_compra_historicas where ch_id = ?";
            
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sqlDel);
            stm.setLong(1, idLista);
            stm.setLong(2, idGrupo);
            stm.executeUpdate();

            
            stm = conn.prepareStatement(sqlSel + " WITH UR ");
            stm.setLong(1, idLista);
            rs = stm.executeQuery();
            if (rs.next()) {                
                borrar = false;
            }

            
            if (borrar) {
                stm = conn.prepareStatement(sqlDelListaDet);
                stm.setLong(1, idLista);
                stm.executeUpdate();

                
                stm = conn.prepareStatement(sqlDelLista);
                stm.setLong(1, idLista);
                stm.executeUpdate();

            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idLista
     * @return
     */
    public List listaDeProductosByLista(long idLista) throws PedidosDAOException {
        List productos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT distinct CH.CHP_ID, CH.CHP_PRO_ID, CH.CHP_CANTIDAD, P.PRO_TIPO_PRODUCTO, P.PRO_DES_CORTA, " +
                        "M.MAR_NOMBRE, U.UNI_DESC " +
                        "FROM FODBA.FO_CH_PRODUCTOS CH " +
                        "INNER JOIN fo_productos P on (CH.CHP_PRO_ID = P.PRO_ID) " +
                        "LEFT JOIN fo_precios_locales PL on (P.PRO_ID = PL.PRE_PRO_ID AND PL.PRE_ESTADO = 'A') " +
                        "LEFT JOIN fo_marcas M on (M.MAR_ID = P.PRO_MAR_ID) " +
                        "LEFT JOIN fo_unidades_medida U on (U.UNI_ID = P.PRO_UNI_ID AND U.UNI_ESTADO = 'A') " +
                        "WHERE chp_ch_id = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLista);
            logger.debug("SQL listaDeProductosByLista: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO lista = new UltimasComprasDTO();
                lista.setId(rs.getLong("CHP_PRO_ID"));
                String desc = rs.getString("PRO_TIPO_PRODUCTO") + " " + rs.getString("PRO_DES_CORTA");
                if (rs.getString("MAR_NOMBRE").length() > 1) {
                    desc += " (" +rs.getString("MAR_NOMBRE") + ")";
                }
                lista.setNombre(desc);
                lista.setUnidades(rs.getDouble("CHP_CANTIDAD"));
                lista.setTipo(rs.getString("UNI_DESC"));
                productos.add(lista);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return productos;
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoById(int idTipoGrupo) throws PedidosDAOException {
        ListaTipoGrupoDTO marco = new ListaTipoGrupoDTO();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT id_lst_tipo_grupo, descripcion, activado, nombre_archivo, texto " +
                        "FROM fodba.fo_lst_tipo_grupo " +
                        "WHERE id_lst_tipo_grupo = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idTipoGrupo);
            logger.debug("SQL getTipoGrupoById: " + sql);
            rs = stm.executeQuery();
            if (rs.next()) {                
                marco.setIdListaTipoGrupo(rs.getInt("id_lst_tipo_grupo"));
                marco.setDescripcion(rs.getString("descripcion"));
                if ( rs.getString("activado") != null ) {
                    marco.setActivado(rs.getString("activado"));
                } else {
                    marco.setActivado("");    
                }
                if ( rs.getString("nombre_archivo") != null ) {
                    marco.setNombreArchivo(rs.getString("nombre_archivo"));
                } else {
                    marco.setNombreArchivo("");    
                }
                if ( rs.getString("texto") != null ) {
                    marco.setTexto(rs.getString("texto"));
                } else {
                    marco.setTexto("");
                }
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return marco;
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public List getGruposDeListasByTipo(int idTipoGrupo) throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT gr.ID_LST_GRUPO, gr.NOMBRE nombre_grupo " +
                        "FROM fodba.fo_lst_grupo gr " +
                        "WHERE gr.ACTIVADO = '1' and " +
                        "gr.ID_LST_TIPO_GRUPO = ? " +
                        "ORDER BY nombre_grupo";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idTipoGrupo);
            logger.debug("SQL getGruposDeListasByTipo: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaGrupoDTO grupo = new ListaGrupoDTO();
                grupo.setIdListaGrupo(rs.getLong("id_lst_grupo"));
                grupo.setNombre(rs.getString("nombre_grupo"));
                lista.add(grupo);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getPoligonosXZona(long id_zona) throws PoligonosDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, "
                   + "       P.DESCRIPCION, P.ID_COMUNA, C.NOMBRE AS NOM_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_COMUNAS C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_ZONA = ?";
		logger.debug("SQL (getPoligonosXZona): " + SQL);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, id_zona);
			rs = stm.executeQuery();
			while (rs.next()) {
				PoligonoxComunaDTO pol = new PoligonoxComunaDTO();
				pol.setId_poligono(rs.getInt("ID_POLIGONO"));
				pol.setNum_poligono(rs.getInt("NUM_POLIGONO"));
				pol.setDesc_poligono(rs.getString("DESCRIPCION"));
				pol.setId_comuna(rs.getInt("ID_COMUNA"));
				pol.setNom_comuna(rs.getString("NOM_COMUNA"));
				result.add(pol);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
    /**
     * @return
     */
    public List localesRetiro() throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql = "SELECT l.id_local id_local, l.nom_local nom_local, l.direccion, l.cod_local, l.ID_ZONA_RETIRO " +
                         "FROM bo_locales l " +
                         "WHERE l.retiro_local = 'S'";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            logger.debug("SQL localesRetiro: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                LocalDTO local = new LocalDTO(); 
                local.setId_local(rs.getLong("id_local"));
                local.setNom_local(rs.getString("nom_local"));
                local.setCod_local(rs.getString("cod_local"));
                local.setDireccion(rs.getString("direccion"));
                if (rs.getString("ID_ZONA_RETIRO") != null) {
                    local.setIdZonaRetiro(rs.getLong("ID_ZONA_RETIRO"));
                } else {
                    local.setIdZonaRetiro(0);
                }
                lista.add(local);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }

    /**
     * @param idZona
     * @return
     */
    public boolean zonaEsRetiroLocal(long idZona) throws PedidosDAOException {
        PreparedStatement stm   = null;
        ResultSet rs            = null;
        boolean respuesta       = false;
        try {            
            String sql = "select l.ID_LOCAL from bo_locales l " +
                         "where l.RETIRO_LOCAL = 'S' and l.ID_ZONA_RETIRO = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idZona);
            logger.debug("SQL zonaEsRetiroLocal: " + sql);
            rs = stm.executeQuery();
            if (rs.next()) {                 
                respuesta = true;
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return respuesta;
    }

    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public void modTipoDespachoDePedido(long idPedido, String tipoDespacho) throws PedidosDAOException {
        PreparedStatement   stm     = null;
        
        String sql = "UPDATE bodba.bo_pedidos " +
                     "SET tipo_despacho = ? " +
                     "WHERE id_pedido = ? ";
        
        logger.debug("en modTipoDespachoDePedido");
        logger.debug("SQL: " + sql);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, tipoDespacho);
            stm.setLong(2, idPedido);
            stm.executeUpdate();

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public void modTipoPickingPedido(long idPedido, String tipoPicking) throws PedidosDAOException {
        PreparedStatement   stm     = null;
        
        String sql = "UPDATE BODBA.BO_PEDIDOS " +
                     "SET TIPO_PICKING = ? " +
                     "WHERE ID_PEDIDO = ? ";
        
        logger.debug("en modTipoPickingPedido");
        logger.debug("SQL: " + sql);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, tipoPicking);
            stm.setLong(2, idPedido);
            stm.executeUpdate();

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    
    /**
     * @return
     */
    public java.util.Date fechaActualBD() throws PedidosDAOException {
        PreparedStatement stm   = null;
        ResultSet rs            = null;
        java.util.Date fecha = new java.util.Date();
        try {            
            //current date as fecha, current time as hora,
            String sql = "SELECT current timestamp as fecha_hora  FROM sysibm.sysdummy1";
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            if (rs.next()) {                 
                SimpleDateFormat patron = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                try {
                    fecha = patron.parse(rs.getString("fecha_hora"));
                } catch (Exception e) {
                    Calendar fecha1 = Calendar.getInstance();
                    fecha1.setFirstDayOfWeek(Calendar.MONDAY);
                    fecha = fecha1.getTime();
                }
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return fecha;
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConDespacho( long idCliente ) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PedidoDTO pedido = new PedidoDTO();
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            //La idea es que devuelva solo info de la tabla pedidos, para que no se ponga lenta la consulta
            //Si necesitan mas columnas, agregarlas            
            String sql = "select p.ID_PEDIDO, p.SIN_GENTE_OP, p.SIN_GENTE_TXT, P.SIN_GENTE_RUT, P.SIN_GENTE_DV " +
                         "from bodba.bo_pedidos p where p.ID_PEDIDO in ( " +
                         "  select max(p1.ID_PEDIDO) from bodba.bo_pedidos p1 where p1.ID_CLIENTE = ? and p1.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and p1.TIPO_DESPACHO != 'R'" +
                         ")";
            stm = conn.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1, idCliente);
            rs = stm.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getLong("id_pedido"));
                pedido.setSin_gente_op(rs.getInt("sin_gente_op"));
                pedido.setSin_gente_txt(rs.getString("sin_gente_txt"));
                if ( rs.getString("SIN_GENTE_RUT") == null ) {
                    pedido.setSin_gente_rut(0);
                } else {
                    pedido.setSin_gente_rut(rs.getLong("SIN_GENTE_RUT"));    
                }
                if ( rs.getString("SIN_GENTE_DV") == null ) {
                    pedido.setSin_gente_dv("");
                } else {
                    pedido.setSin_gente_dv(rs.getString("SIN_GENTE_DV"));
                }                
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}       
        return pedido;
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConRetiro(long idCliente) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PedidoDTO pedido = new PedidoDTO();
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            //La idea es que devuelva solo info de la tabla pedidos, para que no se ponga lenta la consulta
            //Si necesitan mas columnas, agregarlas            
            String sql = "select p.ID_PEDIDO, p.SIN_GENTE_OP, p.SIN_GENTE_TXT, P.SIN_GENTE_RUT, P.SIN_GENTE_DV " +
                         "from bodba.bo_pedidos p where p.ID_PEDIDO in ( " +
                         "  select max(p1.ID_PEDIDO) from bodba.bo_pedidos p1 where p1.ID_CLIENTE = ? and p1.TIPO_DESPACHO = 'R'" +
                         ")";
            stm = conn.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1, idCliente);
            rs = stm.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getLong("id_pedido"));
                pedido.setSin_gente_op(rs.getInt("sin_gente_op"));
                pedido.setSin_gente_txt(rs.getString("sin_gente_txt"));
                if ( rs.getString("SIN_GENTE_RUT") == null ) {
                    pedido.setSin_gente_rut(0);
                } else {
                    pedido.setSin_gente_rut(rs.getLong("SIN_GENTE_RUT"));    
                }
                if ( rs.getString("SIN_GENTE_DV") == null ) {
                    pedido.setSin_gente_dv("");
                } else {
                    pedido.setSin_gente_dv(rs.getString("SIN_GENTE_DV"));
                }                
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}     
        return pedido;
    }

   /**
    * @param id_pedido
    * @return True si posee producto con monto total superior al de la lista negra
    * @throws PedidosDAOException
    */
   public boolean tieneProductosDeListaNegra(long pedidoId) throws PedidosDAOException {
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
          conn = this.getConnection();
          String sql = "select lis.producto_id, rub.precio_total, sum(precio) "
             		+ "from bodba.bo_detalle_pedido det "
             		+ "inner join bodba.lista_negra_productos lis on lis.producto_id = det.id_producto " 
             		+ "inner join bodba.lista_negra_subrubro rub on rub.id = lis.subrubro_id "
             		+ "where id_pedido = ? "
             		+ "group by id_pedido, lis.producto_id, rub.precio_total " 
             		+ "having sum(precio) > rub.precio_total";
          ps = conn.prepareStatement(sql + " WITH UR");
          ps.setLong(1, pedidoId);
          rs = ps.executeQuery();
          
          if (rs.next()) {
             return true;
          }
      } catch (SQLException e) {
          throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
      return false;
   }

   /**
    * @param id_pedido
    * @param mp
    * @throws PedidosDAOException
    */
   public void updNumeroTarjeta(long id_pedido, String mp) throws PedidosDAOException {
      PreparedStatement ps = null;
      String sql = "update BODBA.BO_PEDIDOS set num_mp = ?, medio_pago = 'CAT' WHERE id_pedido = ? ";
      try {
          conn = this.getConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, mp);
          ps.setLong(2, id_pedido);
          ps.executeUpdate();

      }catch (SQLException e) {
          throw new PedidosDAOException(e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
      
   }

    /**
     * @param id_pedido
     * @return
     */
    public List getProductosSolicitadosById(long id_pedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        CarroCompraCategoriasDTO aux_cat = null;
        List aux_lpro = null;
        List productosPorCategoria = new ArrayList();
        CarroCompraProductosDTO aux_pro = null;
        CarroCompraCategoriasDTO categoria = null;
        CarroCompraProductosDTO productos = null;
        List l_pro = null;

        try {
            String sql = "SELECT distinct cat.CAT_ID, pro.PRO_ID, pro.PRO_TIPO_PRODUCTO, pro.PRO_DES_CORTA, " +
                         "pro.PRO_COD_SAP, pro.PRO_TIPO_SEL, pro.PRO_TIPRE, pro.PRO_INTER_MAX, pro.PRO_INTER_VALOR, " +
                         "pro.PRO_NOTA, ma.MAR_NOMBRE, dp.CANT_SOLIC, dp.PRECIO, cat.CAT_NOMBRE, " +
                         "case when dp.id_producto is null then '' else dp.uni_med end as uni_med, uni.UNI_DESC " +
                         "from bodba.bo_detalle_pedido dp " +
                         "    join fo_productos pro on (dp.ID_PRODUCTO = pro.PRO_ID_BO) " +
                         "    join " +
                         "           ( " +
                         "           select pro1.pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                         "           from bodba.bo_detalle_pedido dp1 " +
                         "           join fo_productos pro1 on (dp1.ID_PRODUCTO = pro1.PRO_ID_BO) " +
                         "           join fo_productos_categorias prca1 on pro1.PRO_ID = prca1.prca_pro_id " +
                         "           join fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                         "           join fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                         "           join fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                         "           where dp1.ID_PEDIDO = ? " +
                         "           group by pro1.pro_id " +
                         "           ) as x  on x.pro_id = pro.pro_id " +
                         "    join fo_categorias cat on cat.CAT_ID = x.cat_id " +
                         "    JOIN FODBA.fo_unidades_medida uni on pro.PRO_UNI_ID = uni.UNI_ID " +
                         "    left join fo_marcas ma on (pro.PRO_MAR_ID = ma.MAR_ID) " +
                         "where dp.ID_PEDIDO = ? order by cat.CAT_NOMBRE"; 

            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1,id_pedido);
            stm.setLong(2,id_pedido);

            rs = stm.executeQuery();
            while (rs.next()) {
                boolean flag = true;
                for ( int i = 0; flag && i < productosPorCategoria.size(); i++ ) {
                    aux_cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
                    aux_lpro = aux_cat.getCarroCompraProductosDTO();
                    for ( int j = 0; flag && j < aux_lpro.size(); j++ ) {
                        aux_pro = (CarroCompraProductosDTO)aux_lpro.get(j);
                        if ( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
                            if ( aux_cat.getId() == rs.getLong("cat_id") ) {
                                double newCantidad = aux_pro.getCantidad() + rs.getDouble("CANT_SOLIC");
                                if (NumericUtils.tieneDecimalesSignificativos(newCantidad, 3)) {
                                    newCantidad = Utils.redondear(newCantidad,3);
                                }
                                ((CarroCompraProductosDTO)((CarroCompraCategoriasDTO) productosPorCategoria.get(i)).getCarroCompraProductosDTO().get(j)).setCantidad( newCantidad );
                                ((CarroCompraProductosDTO)((CarroCompraCategoriasDTO) productosPorCategoria.get(i)).getCarroCompraProductosDTO().get(j)).setPrecio( rs.getDouble("precio") * newCantidad );
                            }
                            flag = false;
                        }
                    }
                }
                if ( !flag ) {
                    continue; // Se salta el registro
                }
                // Listado de productos por categorias
                l_pro = new ArrayList();                
                
                // Revisar si existe la categoría
                boolean flag_cat = true;
                for( int i = 0; i < productosPorCategoria.size(); i++ ) {
                    categoria = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
                    if( categoria.getId() == rs.getLong("cat_id") ) {
                        l_pro = categoria.getCarroCompraProductosDTO();
                        flag_cat = false;
                        break;
                    }
                }
                
                // Agregar categorias sólo si es una categoría vacía
                categoria = new CarroCompraCategoriasDTO();
                categoria.setId( rs.getLong("cat_id") );
                categoria.setCategoria( rs.getString("cat_nombre") );
                                
                // Agregar los productos
                productos = new CarroCompraProductosDTO();
                productos.setPro_id(rs.getLong("pro_id"));
                productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
                productos.setCodigo(rs.getString("pro_cod_sap"));
                productos.setPpum(rs.getDouble("precio"));
                productos.setPrecio( rs.getDouble("precio") * rs.getDouble("CANT_SOLIC") );              
                productos.setCantidad(rs.getDouble("CANT_SOLIC"));
                productos.setUnidad_nombre(rs.getString("uni_desc"));
                productos.setUnidadMedida(rs.getString("uni_med"));
                productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                
                productos.setTipre( Formatos.formatoUnidad(rs.getString("pro_tipre")) );
                productos.setInter_maximo( rs.getDouble("pro_inter_max") );
                if( rs.getString("pro_inter_valor") != null ) {
                    productos.setInter_valor(rs.getDouble("pro_inter_valor"));
                } else {
                    productos.setInter_valor(1.0);
                }
                productos.setMarca(rs.getString("mar_nombre"));
                if( rs.getString("pro_nota").compareTo("S") == 0 ) {
                    productos.setCon_nota( true );
                } else {
                    productos.setCon_nota( false );
                }
                productos.setStock(100); // No toma encuenta el stock nunca pone 0
                l_pro.add(productos);
                categoria.setCarroCompraProductosDTO(l_pro);
                
                // Sólo si es una categoría nueva
                if( flag_cat ) {
                    productosPorCategoria.add(categoria);
                }               
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return productosPorCategoria;
    }

    /**
     * @param idMarco
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoListadoById(long idMarco) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ListaTipoGrupoDTO marco = new ListaTipoGrupoDTO();
        
        String sql = "select tg.ID_LST_TIPO_GRUPO, tg.DESCRIPCION nombre_tipo, tg.activado, " +
                     "tg.nombre_archivo, tg.texto " +
                     "from fodba.fo_lst_tipo_grupo tg " +
                     "where tg.ID_LST_TIPO_GRUPO = ? ";
        
        logger.debug("SQL getTipoGrupoListadoById :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idMarco);
            rs = stm.executeQuery();
            if ( rs.next() ) {                
                marco.setIdListaTipoGrupo(rs.getInt("ID_LST_TIPO_GRUPO"));
                if ( rs.getString("nombre_tipo") != null ) {
                    marco.setDescripcion(rs.getString("nombre_tipo"));
                } else {
                    marco.setDescripcion("");    
                }
                if ( rs.getString("activado") != null ) {
                    marco.setActivado(rs.getString("activado"));
                } else {
                    marco.setActivado("");
                }
                if ( rs.getString("nombre_archivo") != null ) {
                    marco.setNombreArchivo(rs.getString("nombre_archivo"));
                } else {
                    marco.setNombreArchivo("");    
                }
                if ( rs.getString("texto") != null ) {
                    marco.setTexto(rs.getString("texto"));
                } else {
                    marco.setTexto("");    
                }
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return marco;
    }

    /**
     * @param marco
     * @return
     */
    public void addMarco(ListaTipoGrupoDTO marco) throws PedidosDAOException {
        PreparedStatement ps = null;
        String sql = "insert into FODBA.FO_LST_TIPO_GRUPO " +
                     "(DESCRIPCION, NOMBRE_ARCHIVO, TEXTO, ACTIVADO) VALUES (?,?,?,?) ";
        try {
            conn = this.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, marco.getDescripcion());
            ps.setString(2, marco.getNombreArchivo());
            ps.setString(3, marco.getTexto());
            ps.setString(4, marco.getActivado());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param marco
     * @return
     */
    public void modMarco(ListaTipoGrupoDTO marco) throws PedidosDAOException {
        PreparedStatement ps = null;
        String sql = "update FODBA.FO_LST_TIPO_GRUPO set DESCRIPCION = ?, NOMBRE_ARCHIVO = ?, TEXTO = ?, " +
                     "ACTIVADO = ? where ID_LST_TIPO_GRUPO = ?";
        try {
            conn = this.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, marco.getDescripcion());
            ps.setString(2, marco.getNombreArchivo());
            ps.setString(3, marco.getTexto());
            ps.setString(4, marco.getActivado());
            ps.setLong(5, marco.getIdListaTipoGrupo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param marco
     */
    public void delMarco(ListaTipoGrupoDTO marco) throws PedidosDAOException {
        PreparedStatement ps = null;
        String sql = "delete from FODBA.FO_LST_TIPO_GRUPO where ID_LST_TIPO_GRUPO = ?";
        try {
            conn = this.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, marco.getIdListaTipoGrupo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idMarco
     * @return
     */
    public List getGruposByMarco(int idMarco) throws PedidosDAOException {
        List lista = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {            
            String sql ="SELECT gr.ID_LST_GRUPO, gr.NOMBRE nombre_grupo " +
                        "FROM fodba.fo_lst_grupo gr " +
                        "WHERE gr.ID_LST_TIPO_GRUPO = ? ";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idMarco);
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaGrupoDTO grupo = new ListaGrupoDTO();
                grupo.setIdListaGrupo(rs.getLong("id_lst_grupo"));
                grupo.setNombre(rs.getString("nombre_grupo"));
                lista.add(grupo);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return lista;
    }

    /**
     * @return
     */
    public List getTiposGruposListadoActivos() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List tiposGruposLista = new ArrayList();
        
        String sql = "select tg.ID_LST_TIPO_GRUPO, tg.DESCRIPCION nombre_tipo, tg.activado " +
                     "from fodba.fo_lst_tipo_grupo tg " +
                     "where tg.ACTIVADO = 'S'";
        
        logger.debug("SQL getTiposGruposListado :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                ltg.setIdListaTipoGrupo(rs.getInt("ID_LST_TIPO_GRUPO"));
                ltg.setDescripcion(rs.getString("nombre_tipo"));
                ltg.setActivado(rs.getString("activado"));
                tiposGruposLista.add(ltg);
            }

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return tiposGruposLista;
    }
    

    public HashMap getFacturasIngresadas(long pedidoId) throws PedidosDAOException {
       PreparedStatement ps = null;
       ResultSet rs = null;
       HashMap list = new HashMap();
       try {
           conn = this.getConnection();
           
           String SQL = "SELECT DP.ID_PEDIDO, DP.NUM_DOC, "
               + "       DP.FECHA, DP.LOGIN "
               + "FROM BODBA.BO_DOC_PAGO DP "
               + "WHERE DP.ID_TRXMP IS NULL "
               + "  AND DP.ID_PEDIDO = ? "
               + "ORDER BY DP.FECHA";
           logger.info("SQL (getFacturasIngresadas): " + SQL);
           logger.info("pedidoId = " + pedidoId);
           ps = conn.prepareStatement(SQL + " WITH UR");
           ps.setLong(1, pedidoId);
           rs = ps.executeQuery();
           int i = 0;
           while (rs.next()) {
               FacturasDTO fact = new FacturasDTO();
               fact.setOrden(++i);
               fact.setId_pedido(rs.getLong("ID_PEDIDO"));
               fact.setNum_doc(rs.getLong("NUM_DOC"));
               fact.setFingreso(rs.getString("FECHA"));
               fact.setLogin(rs.getString("LOGIN"));
               list.put(""+i, fact);
           }
       } catch (SQLException e) {
           throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return list;
    }


    public int getCantFacturasIngresadas(long id_pedido) throws PedidosDAOException {
       PreparedStatement ps = null;
       ResultSet rs = null;
       int cantidad = 0;
       try {
           conn = this.getConnection();
           
           String SQL = "SELECT COUNT(*) AS CANTIDAD "
               + "FROM BODBA.BO_DOC_PAGO DP "
               + "WHERE DP.ID_TRXMP IS NULL "
               + "  AND DP.ID_PEDIDO = ? ";
           logger.info("SQL (getCantFacturasIngresadas): " + SQL);
           logger.info("id_pedido = " + id_pedido);
           ps = conn.prepareStatement(SQL + " WITH UR");
           ps.setLong(1, id_pedido);
           rs = ps.executeQuery();
           //int i = 0;
           if (rs.next()) {
               return rs.getInt("CANTIDAD");
           }
       } catch (SQLException e) {
           throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return cantidad;
    }



    /**
     * @param id_pedido
     * @return True si posee producto con monto total superior al de la lista negra
     * @throws PedidosDAOException
     */
    public boolean elimFactura(FacturasDTO fact) throws PedidosDAOException {
        PreparedStatement stm = null;
        boolean result = false;
        //HashMap list = new HashMap();
        try {
            conn = this.getConnection();
            
            String SQL = "";
            SQL = "DELETE FROM BODBA.BO_DOC_PAGO DP "
                + "WHERE DP.ID_TRXMP IS NULL "
                + "  AND DP.ID_PEDIDO = ? "
                + "  AND DP.NUM_DOC = ? ";
            logger.info("SQL (elimFactura): " + SQL);
            logger.info("pedidoId = " + fact.getId_pedido());
            logger.info("num_doc  = " + fact.getNum_doc());

            stm = conn.prepareStatement(SQL);
            stm.setLong(1, fact.getId_pedido());
            stm.setLong(2, fact.getNum_doc());
            
            int i = stm.executeUpdate();
            if(i>0)
                result = true;
        }catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			} if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
            //throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return result;
    }


    /**
     * @param id_pedido
     * @return True si posee producto con monto total superior al de la lista negra
     * @throws PedidosDAOException
     */
    public boolean ingFactura(FacturasDTO fact) throws PedidosDAOException {
       PreparedStatement stm = null;
       boolean result = false;

       try {
           conn = this.getConnection();
           
           String SQL = "";
           
           if (fact.getId_trxmp() > 0.0){ //INSERCIÓN DE BOLETA
               SQL = "INSERT INTO BODBA.BO_DOC_PAGO (ID_PEDIDO, ID_TRXMP, NUM_DOC, FECHA, LOGIN)"
                   + "VALUES (?, ?, ?, CURRENT TIMESTAMP, ?)";
               stm = conn.prepareStatement(SQL);
               stm.setLong(1, fact.getId_pedido());
               stm.setLong(2, fact.getId_trxmp());
               stm.setLong(3, fact.getNum_doc());
               stm.setString(4, fact.getLogin());
           }else{// INSERCIÓN DE FACTURA
               SQL = "INSERT INTO BODBA.BO_DOC_PAGO (ID_PEDIDO, NUM_DOC, FECHA, LOGIN)"
                   + "VALUES (?, ?, CURRENT TIMESTAMP, ?)";
               stm = conn.prepareStatement(SQL);
               stm.setLong(1, fact.getId_pedido());
               stm.setLong(2, fact.getNum_doc());
               stm.setString(3, fact.getLogin());
           }

           logger.info("SQL (ingFactura): " + SQL);
           logger.info("ID_PEDIDO = " + fact.getId_pedido());
           logger.info("ID_TRXMP  = " + fact.getId_trxmp());
           logger.info("NUM_DOC   = " + fact.getNum_doc());
           logger.info("LOGIN     = " + fact.getLogin());
           
           int i = stm.executeUpdate();
           if(i>0)
               result = true;
        }catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			} if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
				throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new PedidosDAOException(e);		
            //throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return result;
    }

    /**
     * @param criterio
     * @return
     */
    public List getProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws PedidosDAOException {        
        List productos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        int pag = criterio.getPag();
        int regXpag = criterio.getRegsperpage();
        
        if (pag <= 0) { 
            pag = 1;
        }
        if (regXpag < 10) {
            regXpag = 10;
        }
        int iniReg = (pag - 1) * regXpag + 1;
        int finReg = pag * regXpag;    
        
        String sql = " SELECT * FROM ( " +
                     " SELECT row_number() over( ORDER BY id_producto_carrusel ) as row, " +
                     " id_producto_carrusel, descripcion,codigo_sap,PRO_TIPO_PRODUCTO as tipo_producto , desc_precio as precio,  fecha_inicio, fecha_termino, imagen, CASE WHEN fecha_termino < CURRENT DATE THEN 'INACTIVO' ELSE 'ACTIVO' END as estado  " +
					 " FROM FODBA.FO_PRODUCTOS_CARRUSEL ,FODBA.FO_PRODUCTOS WHERE codigo_sap=PRO_COD_SAP AND 1=1 ";
        
        sql += obtenerWhereCarrusel(criterio);
        
        sql += " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
        
        logger.debug("* SQL con criterio :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");           
            rs = stm.executeQuery();
            
            while (rs.next()) {
                ProductoCarruselDTO prod = new ProductoCarruselDTO();
                prod.setIdProductoCarrusel(rs.getLong("id_producto_carrusel"));
                prod.setDescripcion(rs.getString("descripcion"));
                prod.setIdCodigoSAP(rs.getString("codigo_sap"));
                prod.setTipoProducto(rs.getString("tipo_producto"));
                prod.setDescPrecio(rs.getString("precio"));
                prod.setFcInicio(rs.getString("fecha_inicio"));
                prod.setFcTermino(rs.getString("fecha_termino"));
                prod.setImagen(rs.getString("imagen"));
                prod.setEstado(rs.getString("estado"));
                productos.add(prod);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return productos;
    }
    
    private String obtenerWhereCarrusel(CriterioCarruselDTO criterio) {
        String sql = "";
        if (criterio.getFcInicio().length() > 0) {
            sql += " AND FECHA_INICIO = '" + criterio.getFcInicio() + "' ";
        }
        if (criterio.getFcTermino().length() > 0) {
            sql += " AND FECHA_TERMINO = '" + criterio.getFcTermino() + "' ";
        }
        if (criterio.getFcCreacion().length() > 0) {
            sql += " AND DATE(FECHA_CREACION) = '" + criterio.getFcCreacion() + "' ";
        }
        return sql;
    }

    /**
     * @param prod
     */
    public long addEditProductoCarrusel(ProductoCarruselDTO prod) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        long id = 0;
        try {
            conn = this.getConnection();            
            String sql = "";            
            if ( prod.getIdProductoCarrusel() > 0 ) { 
                // MODIFICAMOS UN PRODUCTO
                sql =  "UPDATE FODBA.FO_PRODUCTOS_CARRUSEL SET " +
					"descripcion = ?, desc_precio = ?,  fecha_inicio = ?, fecha_termino = ?, link_destino = ? ";
                if (!"".equalsIgnoreCase(prod.getImagen())) {
                    sql += ", imagen = ? ";
                }
                sql += "WHERE id_producto_carrusel = ? ";
                stm = conn.prepareStatement(sql);
				stm.setString(1, prod.getDescripcion());
                stm.setString(2, prod.getDescPrecio());
				stm.setDate(3, new java.sql.Date( Formatos.getFechaDateByStringAndPatron(prod.getFcInicio(), "dd/MM/yyyy").getTime() ));
				stm.setDate(4, new java.sql.Date( Formatos.getFechaDateByStringAndPatron(prod.getFcTermino(), "dd/MM/yyyy").getTime() ));
				stm.setString(5, prod.getLinkDestino());
				stm.setString(6, prod.getImagen());
				stm.setLong(7,prod.getIdProductoCarrusel());
                stm.executeUpdate();
                id = prod.getIdProductoCarrusel();
                
            } else { 
                // INSERTAMOS UN NUEVO PRODUCTO
                sql = "INSERT INTO FODBA.FO_PRODUCTOS_CARRUSEL " +
				"(codigo_sap, descripcion, desc_precio,fecha_inicio, fecha_termino, link_destino, imagen) " + 
				"VALUES (?,?,?,?,?,?,?)";
                stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                stm.setString(1, prod.getIdCodigoSAP());
                stm.setString(2, prod.getDescripcion());
                stm.setString(3, prod.getDescPrecio());
                stm.setDate(4, new java.sql.Date( Formatos.getFechaDateByStringAndPatron(prod.getFcInicio(), "dd/MM/yyyy").getTime() ));
                stm.setDate(5, new java.sql.Date( Formatos.getFechaDateByStringAndPatron(prod.getFcTermino(), "dd/MM/yyyy").getTime() ));
                stm.setString(6, prod.getLinkDestino());
                stm.setString(7, prod.getImagen());
                stm.executeUpdate();
                rs = stm.getGeneratedKeys();
                
                if (rs.next())
                    id = rs.getInt(1);
            }
            
            

         } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            throw new PedidosDAOException(e);
 		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
         return id;
    }

    /**
     * @param idProductoCarrusel
     * @return
     */
    public ProductoCarruselDTO getProductoCarruselById(long idProductoCarrusel) throws PedidosDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductoCarruselDTO producto = new ProductoCarruselDTO();
        try {
            conn = this.getConnection();
            String sql = "SELECT " + 
                         "id_producto_carrusel, codigo_sap, descripcion, desc_precio, fecha_inicio, fecha_termino, link_destino,imagen " +
						 "FROM FODBA.FO_PRODUCTOS_CARRUSEL " + 
                         "WHERE id_producto_carrusel = ? ";
            ps = conn.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idProductoCarrusel);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setIdProductoCarrusel(rs.getLong("id_producto_carrusel"));
                producto.setIdCodigoSAP(rs.getString("codigo_sap"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setDescPrecio(rs.getString("desc_precio"));
                producto.setFcInicio(rs.getString("fecha_inicio"));
                producto.setFcTermino(rs.getString("fecha_termino"));
                producto.setLinkDestino(rs.getString("link_destino"));
                producto.setImagen(rs.getString("imagen"));             
            }
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return producto;
    }

    /**
     * @param idProductoCarrusel
     */
    public void deleteProductoCarruselById(long idProductoCarrusel) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();            
            String sql = "DELETE FROM FODBA.FO_PRODUCTOS_CARRUSEL WHERE id_producto_carrusel = ? ";
            stm = conn.prepareStatement(sql);
            stm.setLong(1,idProductoCarrusel);
            stm.executeUpdate();

         } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            throw new PedidosDAOException(e);
 		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param usr
     * @param strLog
     */
    public void addLogCarrusel(UserDTO usr, String strLog) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();            
            String sql = "INSERT INTO FODBA.FO_LOG_PRODUCTOS_CARRUSEL " +
                         "(id_usuario, descripcion) " +
                         "VALUES " +
                         "(?,?) ";
            stm = conn.prepareStatement(sql);
            stm.setLong(1,usr.getId_usuario());
            stm.setString(2,strLog);
            stm.executeUpdate();

         } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            throw new PedidosDAOException(e);
 		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getProductosCarruselActivos() throws PedidosDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List productos = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "SELECT id_producto_carrusel, id_producto_fo, nombre, descripcion, desc_precio, " +
            "imagen, fecha_inicio, fecha_termino, fecha_creacion, con_criterio, link_destino, " +
            "PRO_TIPO_PRODUCTO,PRO_DES_CORTA, pre_valor as precio, PRO_IMAGEN_FICHA,PRO_TIPRE,pro_particionable,pro_particion, MAR_NOMBRE, pre_valor " +
            "FROM FODBA.FO_PRODUCTOS_CARRUSEL , FODBA.FO_PRODUCTOS, FODBA.FO_MARCAS, FODBA.fo_precios_locales " +
			"WHERE codigo_sap=PRO_COD_SAP AND PRO_MAR_ID = MAR_ID  AND  PRO_ID = PRE_PRO_ID AND pre_loc_id =1 AND FECHA_INICIO <= CURRENT_DATE and FECHA_TERMINO >= CURRENT_DATE " +
            "ORDER BY nombre";
            ps = conn.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoCarruselDTO producto = new ProductoCarruselDTO();
                producto.setIdProductoCarrusel(rs.getLong("id_producto_carrusel"));
                producto.setIdProductoFo(rs.getLong("id_producto_fo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setDescPrecio(rs.getString("desc_precio"));
                producto.setImagen(rs.getString("imagen"));
                producto.setFcInicio(rs.getString("fecha_inicio"));
                producto.setFcTermino(rs.getString("fecha_termino"));
                producto.setFcCreacion(rs.getString("fecha_creacion"));
                producto.setConCriterio(rs.getString("con_criterio"));
                producto.setLinkDestino(rs.getString("link_destino"));
                producto.setTipoProducto(rs.getString("PRO_TIPO_PRODUCTO"));
                producto.setDescripcionProducto(rs.getString("PRO_DES_CORTA"));
                producto.setPrecioProducto(rs.getString("pre_valor"));
                producto.setImagenProducto(rs.getString("PRO_IMAGEN_FICHA"));
                producto.setMarcaProducto(rs.getString("MAR_NOMBRE"));
                producto.setTipre(rs.getString("PRO_TIPRE"));
                producto.setEsParticionable(rs.getString("pro_particionable"));
                producto.setParticion(rs.getLong("pro_particion"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return productos;
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws PedidosDAOException {        
        double filas = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        String sql = " SELECT count(id_producto_carrusel) total FROM FODBA.FO_PRODUCTOS_CARRUSEL WHERE 1=1 ";
        sql += obtenerWhereCarrusel(criterio);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");           
            rs = stm.executeQuery();
            
            if (rs.next()) {
                filas = rs.getDouble("total");                
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return filas;
    }

    /**
     * @param fecha
     * @return
     */
    public List getLogCarruselByFecha(String fecha) throws PedidosDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List logs = new ArrayList();
        try {
            conn = this.getConnection();
            
            String sql = "select c.id_log_carrusel, c.fecha, c.descripcion, u.LOGIN usuario " +
                         "from fodba.fo_log_productos_carrusel c " +
                         "inner join bodba.bo_usuarios u on u.ID_USUARIO = c.ID_USUARIO ";
            
            if ( fecha.length() > 0 ) {
                sql +=      "where date(c.FECHA) = '" + fecha + "' ";
            } else {
                sql +=      "where date(c.FECHA) = CURRENT_DATE ";
            }
            sql +=      "order by id_log_carrusel";
            
            
            ps = conn.prepareStatement(sql + " WITH UR");
            
            
            rs = ps.executeQuery();
            while (rs.next()) {
                LogCarruselDTO log = new LogCarruselDTO();
                log.setIdLog(rs.getLong("id_log_carrusel"));
                log.setFecha(rs.getString("fecha"));
                log.setLog(rs.getString("descripcion"));
                log.setUsuario(rs.getString("usuario"));
                logs.add(log);
            }
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return logs;
        
    }

    /**
     * @param idComuna
     * @return
     */
    public RegionDTO getRegionByComuna(long idComuna) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        RegionDTO region = new RegionDTO();
        try {
            conn = this.getConnection();
            String sql = "select r.REG_ID, r.REG_NOMBRE, r.REG_NUMERO " +
                         "from bodba.bo_comunas c " +
                         "inner join bodba.bo_regiones r on r.REG_ID = c.REG_ID " +
                         "where c.ID_COMUNA = ?";
            stm = conn.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1, idComuna);
            rs = stm.executeQuery();
            if (rs.next()) {
                region.setIdRegion(rs.getInt("REG_ID"));
                region.setNombre(rs.getString("REG_NOMBRE"));
                region.setNumero(rs.getInt("REG_NUMERO"));                                
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return region;
    }

    /**
     * @return
     */
    public List getRegiones() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List regiones = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select r.REG_ID, r.REG_NOMBRE, r.REG_NUMERO " +
                         "from bodba.bo_regiones r " +
                         "order by r.REG_ORDEN";
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                RegionDTO region = new RegionDTO();
                region.setIdRegion(rs.getInt("REG_ID"));
                region.setNombre(rs.getString("REG_NOMBRE"));
                region.setNumero(rs.getInt("REG_NUMERO"));
                regiones.add(region);                               
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return regiones;
    }

    /**
     * @param idRegion
     * @return
     */
    public List getComunasByRegion(int idRegion) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List comunas = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select c.ID_COMUNA, c.NOMBRE " +
                         "from bodba.bo_comunas c " +
                         "where c.REG_ID = ? and c.TIPO = 'W' " +
                         "order by c.NOMBRE";
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idRegion);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ComunasDTO comuna = new ComunasDTO();
                comuna.setId_comuna(rs.getInt("ID_COMUNA"));
                comuna.setNombre(rs.getString("NOMBRE"));
                comunas.add(comuna);                               
            }
            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return comunas;
    }

    /**
     * @param idComuna
     * @return
     */
    public List getZonasByComuna(int idComuna) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List zonas = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select distinct z.ID_ZONA, z.NOMBRE, z.DESCRIPCION " +
                         "from bodba.bo_poligono p " +
                         "inner join bodba.bo_zonas z on p.ID_ZONA = z.ID_ZONA " +
                         "where p.ID_COMUNA = ? and z.ID_ZONA not in ( " +
                         "    select l.ID_ZONA_RETIRO " +
                         "    from bodba.bo_locales l " +
                         "    where l.RETIRO_LOCAL = 'S' " +
                         ")";
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idComuna);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ZonaDTO zona = new ZonaDTO();
                zona.setId_zona(rs.getInt("ID_ZONA"));
                zona.setNombre(rs.getString("NOMBRE"));
                zona.setDescripcion(rs.getString("DESCRIPCION"));
                zonas.add(zona);                               
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}     
        return zonas;
    }

    /**
     * @param ped
     */
    public long addPedidoExt(PedidoDTO pedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        long id_pedido = 0;        
        
        String SQLStmt = "INSERT INTO bodba.bo_pedidos (" +
                "id_estado, " +
                "id_jdespacho, " +
                "id_local, " +
                "id_local_fact, " +
                "id_comuna, " +
                "id_zona, " +
                "id_cliente, " +
                "nom_cliente, " +
                "telefono2, " +
                "telefono, " +
                "tipo_despacho, " +
                "costo_despacho, " +
                "fcreacion, " +
                "monto_pedido, " +
                "medio_pago, " +
                "num_mp, " +
                "rut_cliente, " +
                "dv_cliente, " +
                "dir_calle, " +
                "tipo_doc, " +              
              "CANT_BINS, " +
              "indicacion," +
              "origen " +
                ") " +
            " VALUES( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        logger.debug("SQL (addPedidoExt): " + SQLStmt);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( SQLStmt, Statement.RETURN_GENERATED_KEYS );
    
            stm.setLong     (1, pedido.getId_estado() );
            stm.setLong     (2, pedido.getId_jdespacho() );
            //stm.setLong       (3, pedido.getId_jpicking() );
            stm.setLong     (3, pedido.getId_local() );
            stm.setLong     (4, pedido.getId_local() );
            if ( pedido.getId_comuna() == 0 ) {
                stm.setNull(5, java.sql.Types.INTEGER );
            } else {
                stm.setLong(5, pedido.getId_comuna() );
            }
            stm.setLong     (6, pedido.getId_zona() );
            if ( pedido.getId_cliente() == 0 ) {
                stm.setNull(7, java.sql.Types.INTEGER );
            } else {
                stm.setLong     (7, pedido.getId_cliente() );
            }
            stm.setString   (8, pedido.getNom_cliente() );
            stm.setString   (9, pedido.getTelefono2() );
            stm.setString   (10, pedido.getTelefono() );
            stm.setString (11, "N" );
            stm.setDouble (12, 1 );
            stm.setDate     (13, new Date(System.currentTimeMillis()) );
            stm.setDouble   (14, pedido.getMonto() );
            stm.setString (15, "" );
            stm.setString (16, "0" );
            
            stm.setLong     (17, pedido.getRut_cliente() );
            stm.setString   (18, pedido.getDv_cliente() + "" );
            stm.setString   (19, pedido.getDir_calle() + "" ); 
          stm.setString   (20, pedido.getTipo_doc() );
          stm.setInt(21, pedido.getCant_bins());          
          stm.setString (22, pedido.getIndicacion() );
          stm.setString (23, pedido.getOrigen() );

            int i = stm.executeUpdate();
            logger.debug("rc: " + i);
            rs = stm.getGeneratedKeys();
            if (rs.next())
                id_pedido = rs.getInt(1);
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        pedido.setId_pedido(id_pedido);
        addDatosPedidoExt(pedido);
        
        return id_pedido;        
    }
    
    public void addDatosPedidoExt(PedidoDTO pedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "INSERT INTO bodba.bo_pedidos_ext (" +
                     "id_pedido, nro_guia_caso, mail, bins_desc, tipo_jumbo_va " +
                     ") " +
                     " VALUES( ?,?,?,?,? )";

        logger.debug("SQL (addPedidoExt): " + sql);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( sql );
    
            stm.setLong     (1, pedido.getId_pedido() );
            stm.setLong     (2, pedido.getPedidoExt().getNroGuiaCaso() );
            if (pedido.getPedidoExt().getMail() == null) {
                stm.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stm.setString(3, pedido.getPedidoExt().getMail() );
            }
            if (pedido.getPedidoExt().getBinsDescripcion() == null) {
                stm.setNull(4, java.sql.Types.VARCHAR );
            } else {
                stm.setString(4, pedido.getPedidoExt().getBinsDescripcion() );
            }            
            if (pedido.getPedidoExt().getTipoJumboVA() == null) {
                stm.setNull(5, java.sql.Types.VARCHAR );
            } else {
                stm.setString(5, pedido.getPedidoExt().getTipoJumboVA() );
            }
            stm.executeUpdate();
            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        
        //Agregamos los documentos
        for (int x=0; x < pedido.getPedidoExt().getDocumentos().size(); x++) {
            FacturasDTO doc = (FacturasDTO) pedido.getPedidoExt().getDocumentos().get(x);
            doc.setId_pedido(pedido.getId_pedido());
            ingFactura(doc);
        }
        
    }

    /**
     * @param criterio
     * @return
     */
    public PedidoDTO getUltimoPedidoJumboVAByRut(long rut) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PedidoDTO ped = new PedidoDTO();
        try {
            conn = this.getConnection();
            String sql = "select p.ID_PEDIDO, p.NOM_CLIENTE, p.DIR_TIPO_CALLE, p.DIR_CALLE, " +
                         "p.DIR_NUMERO, p.ID_COMUNA, p.TELEFONO, p.TELEFONO2, pe.mail " +
                         "from bodba.bo_pedidos p " +
                         "left outer join bodba.bo_pedidos_ext pe on pe.id_pedido = p.id_pedido " +
                         "where p.RUT_CLIENTE = ? and p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
                         "order by p.FCREACION desc";
            
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, rut);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                ped.setId_pedido(rs.getLong("ID_PEDIDO"));
                ped.setNom_cliente(rs.getString("NOM_CLIENTE"));
                if (rs.getString("DIR_TIPO_CALLE") != null) {
                    ped.setDir_tipo_calle(rs.getString("DIR_TIPO_CALLE"));
                } else {
                    ped.setDir_tipo_calle("");
                }
                if ( rs.getString("DIR_CALLE") != null ) {
                    ped.setDir_calle(rs.getString("DIR_CALLE"));
                } else {
                    ped.setDir_calle("");
                }
                if ( rs.getString("DIR_NUMERO") != null ) {
                    ped.setDir_numero(rs.getString("DIR_NUMERO"));
                } else {
                    ped.setDir_numero("");
                }
                ped.setId_comuna(rs.getLong("ID_COMUNA"));
                ped.setTelefono(rs.getString("TELEFONO"));
                ped.setTelefono2(rs.getString("TELEFONO2"));
                PedidoExtDTO pe = new PedidoExtDTO();
                pe.setMail(rs.getString("mail"));
                ped.setPedidoExt(pe);
            }
            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return ped;
    }

    /**
     * @param idFono
     * @return
     */
    public FonoTransporteDTO getFonoTransporteById(long idFono) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        FonoTransporteDTO fono = new FonoTransporteDTO();
        try {
            conn = this.getConnection();
            String sql = "select f.ID_FONO_TRANS, f.CODIGO, f.NUMERO, f.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_fono_trans f " +
                         "where f.ID_FONO_TRANS = ? and f.ACTIVADO = 'S'";
            
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idFono);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                fono.setCodigo(rs.getLong("CODIGO"));
                fono.setNumero(rs.getLong("NUMERO"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                fono.setEmpresaTransporte(emp);                                                                               
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return fono;
    }

    /**
     * @param idChofer
     * @param idLocal
     * @return
     */
    public ChoferTransporteDTO getChoferTransporteById(long idChofer) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ChoferTransporteDTO chofer = new ChoferTransporteDTO();
        try {
            conn = this.getConnection();
            String sql = "select c.ID_CHOFER_TRANS, c.NOMBRE_CHOFER, c.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_chofer_trans c " +
                         "where c.ID_CHOFER_TRANS = ? and c.ACTIVADO = 'S'";
            
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idChofer);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                chofer.setEmpresaTransporte(emp);                                                                               
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return chofer;
    }

    /**
     * @param idPatente
     * @param idLocal
     * @return
     */
    public PatenteTransporteDTO getPatenteTransporteById(long idPatente) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PatenteTransporteDTO patente = new PatenteTransporteDTO();
        try {
            conn = this.getConnection();
            String sql = "select p.ID_PATENTE_TRANS, p.PATENTE, p.CANT_MAX_BINS, p.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_patente_trans p " +
                         "where p.ID_PATENTE_TRANS = ? and p.ACTIVADO = 'S'";
            
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idPatente);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                patente.setPatente(rs.getString("PATENTE"));
                patente.setCantMaxBins(rs.getLong("CANT_MAX_BINS"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                patente.setEmpresaTransporte(emp);                                                                               
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return patente;
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List fonos = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select f.ID_FONO_TRANS, f.CODIGO, f.NUMERO, f.NOMBRE, f.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_fono_trans f " +
                         "where f.ACTIVADO = 'S'";
            if ( idLocal != -1 ) {
                sql += " and f.ID_LOCAL = " + idLocal;
            }
            if ( idEmpresaTransporte != 0 ) {
                sql += " and f.ID_EMP_TRANSPORTE = " + idEmpresaTransporte;
            }
            
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {                
                FonoTransporteDTO fono = new FonoTransporteDTO();
                fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                fono.setCodigo(rs.getLong("CODIGO"));
                fono.setNumero(rs.getLong("NUMERO"));
                fono.setNombre(rs.getString("NOMBRE"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                fono.setEmpresaTransporte(emp);
                fonos.add(fono);
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}     
        return fonos;
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List choferes = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select c.ID_CHOFER_TRANS, c.NOMBRE_CHOFER, c.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_chofer_trans c " +
                         "where c.ACTIVADO = 'S'";
            if ( idLocal != -1 ) {
                sql += " and c.ID_LOCAL = " + idLocal;
            }            
            if ( idEmpresaTransporte != 0 ) {
                sql += " and c.ID_EMP_TRANSPORTE = " + idEmpresaTransporte;
            }
            
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {                
                ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                chofer.setEmpresaTransporte(emp); 
                choferes.add(chofer);
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}       
        return choferes;
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List patentes = new ArrayList();
        try {
            conn = this.getConnection();
            String sql = "select p.ID_PATENTE_TRANS, p.PATENTE, p.CANT_MAX_BINS, p.ID_EMP_TRANSPORTE " +
                         "from bodba.bo_patente_trans p " +
                         "where p.ACTIVADO = 'S'";
            if ( idLocal != -1 ) {
                sql += " and p.ID_LOCAL = " + idLocal;
            }
            if ( idEmpresaTransporte != 0 ) {
                sql += " and p.ID_EMP_TRANSPORTE = " + idEmpresaTransporte;
            }
            
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {                
                PatenteTransporteDTO patente = new PatenteTransporteDTO();
                patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                patente.setPatente(rs.getString("PATENTE"));
                patente.setCantMaxBins(rs.getLong("CANT_MAX_BINS"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                patente.setEmpresaTransporte(emp);
                patentes.add(patente);
            }

            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
        return patentes;
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getDetPickingToHojaDespacho(long id_pedido) throws PedidosDAOException {
        List lstDetPick         = new ArrayList();
        PreparedStatement stm   = null;
        ResultSet rs            = null;

        try {
            String sql = "SELECT  bp.cod_bin, p.cod_prod1, dp.ID_PRODUCTO, p.uni_med, dp.descripcion, sum(dp.cant_pick) cant_pick " +
                         "FROM bodba.bo_detalle_picking dp " +
                         "LEFT JOIN bodba.bo_bins_pedido bp on bp.id_bp = dp.id_bp " +
                         "LEFT JOIN bodba.bo_detalle_pedido p on p.id_detalle = dp.id_detalle " +
                         "WHERE dp.id_pedido = ? " +
                         "group by bp.cod_bin, p.cod_prod1, dp.ID_PRODUCTO, p.uni_med, dp.descripcion " +
                         "order by dp.DESCRIPCION, bp.COD_BIN";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1,id_pedido);
            logger.debug("SQL getDetPickingToHojaDespacho: " + sql);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                DetallePickingDTO dpick = new DetallePickingDTO();
                dpick.setCod_bin(rs.getString("cod_bin"));
                dpick.setCod_prod(rs.getString("cod_prod1"));
                dpick.setUni_med(rs.getString("uni_med"));
                dpick.setDescripcion(rs.getString("descripcion"));
                dpick.setCant_pick(rs.getDouble("cant_pick"));
                dpick.setId_producto(rs.getLong("id_producto"));
                lstDetPick.add(dpick);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        logger.debug("Se listaron:"+lstDetPick.size());
        return lstDetPick;
    }

    /**
     * @param idRuta
     * @param cantBins
     */
    public void actualizaCantBinsRuta(long idRuta) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE bodba.bo_ruta SET cant_bins = ( " +
                     "  select sum( p.CANT_BINS ) bins from bodba.bo_pedidos p " +
                     "  inner join bodba.bo_pedidos_ext pe on pe.ID_PEDIDO = p.ID_PEDIDO " +
                     "  where id_ruta = ? and p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and p.CANT_BINS is not null " +
                     ") WHERE id_ruta = ?";

        logger.debug("SQL (actualizaCantBinsRuta): " + sql);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( sql );
    
            stm.setLong(1, idRuta);
            stm.setLong(2, idRuta);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @param operacion
     */
    public void modificaVecesEnRutaDePedido(long idPedido, String operacion) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE bodba.bo_pedidos_ext SET veces_en_ruta = (veces_en_ruta "+operacion+" 1) WHERE id_pedido = ?";

        logger.debug("SQL (modificaVecesEnRutaDePedido): " + sql);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( sql );
    
            stm.setLong(1, idPedido );
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @return
     */
    public List getDocumentosByPedido(long idPedido) throws PedidosDAOException {
        List documentos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_pedido, num_doc, fecha, login " +
                         "FROM bodba.bo_doc_pago " +
                         "WHERE id_pedido = ? ";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1,idPedido);
            logger.debug("SQL getDocumentosByPedido: " + sql);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                FacturasDTO doc = new FacturasDTO();
                doc.setId_pedido(rs.getLong("id_pedido"));
                doc.setNum_doc(rs.getLong("num_doc"));
                doc.setFingreso(rs.getString("fecha"));
                doc.setLogin(rs.getString("login"));
                documentos.add(doc);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return documentos;
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNC() throws PedidosDAOException {
        List responsables = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_responsable_desp, nombre_responsable, activado " +
                         "FROM bodba.bo_responsable_despacho " +
                         "WHERE activado = 'S'";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ObjetoDTO resp = new ObjetoDTO();
                resp.setIdObjeto(rs.getLong("id_responsable_desp"));
                resp.setNombre(rs.getString("nombre_responsable"));
                resp.setActivado(rs.getString("activado"));
                responsables.add(resp);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return responsables;
    }

    /**
     * @return
     */
    public List getMotivosDespachoNC() throws PedidosDAOException {
        List motivos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_motivo_desp, motivo, activado " +
                         "FROM bodba.bo_motivo_despacho " +
                         "WHERE activado = 'S'";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ObjetoDTO mot = new ObjetoDTO();
                mot.setIdObjeto(rs.getLong("id_motivo_desp"));
                mot.setNombre(rs.getString("motivo"));
                mot.setActivado(rs.getString("activado"));
                motivos.add(mot);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return motivos;
    }

    /**
     * @param pedExt
     * @throws ParseException
     */
    public void updPedidoFinalizado(PedidoExtDTO pedExt) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE bodba.bo_pedidos_ext " +
                     "SET fecha_hora_llegada_dom = ?, fecha_hora_salida_dom = ?," +
                     "cumplimiento = ?, ID_MOTIVO_CUMPLIMIENTO = ?, ID_RESPONSABLE_CUMPLIMIENTO = ?, " +
                     "con_devolucion = ?, veces_en_ruta = (veces_en_ruta + 1) WHERE id_pedido = ?";

        logger.debug("SQL (updPedidoFinalizado): " + sql);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( sql );
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            java.util.Date parsedDate = dateFormat.parse(pedExt.getFcHoraLlegadaDomicilio());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            
            java.util.Date parsedDate2 = dateFormat.parse(pedExt.getFcHoraSalidaDomicilio());
            java.sql.Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
    
            stm.setTimestamp(1, timestamp );
            stm.setTimestamp(2, timestamp2 );
            stm.setString(3, pedExt.getCumplimiento());
            if ( pedExt.getMotivoNoCumplimiento().getIdObjeto() != 0 ) {
                stm.setLong(4, pedExt.getMotivoNoCumplimiento().getIdObjeto());
            } else {
                stm.setNull(4, java.sql.Types.INTEGER);    
            }
            if ( pedExt.getResponsableNoCumplimiento().getIdObjeto() != 0  ) {
                stm.setLong(5, pedExt.getResponsableNoCumplimiento().getIdObjeto());
            } else {
                stm.setNull(5, java.sql.Types.INTEGER);
            }
            stm.setString(6, pedExt.getConDevolucion());
            stm.setLong(7, pedExt.getIdPedido());
            stm.executeUpdate();
            
        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
        } catch (ParseException e) {
            throw new PedidosDAOException(String.valueOf(""),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idRuta
     * @return
     */
    public int getCountPedidoNoFinalizadosByRuta(long idRuta) throws PedidosDAOException {
        int pedidosNoFinalizados = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "select count(*) pedidos " +
                         "from bodba.bo_pedidos_ext pe " +
                         "inner join bodba.bo_pedidos p on p.ID_PEDIDO = pe.ID_PEDIDO " +
                         "where pe.ID_RUTA = ? and p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ",10)";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idRuta);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                pedidosNoFinalizados = rs.getInt("pedidos");                
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidosNoFinalizados;
    }

    /**
     * @param idPedido
     */
    public void updPedidoReagendado(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE bodba.bo_pedidos_ext " +
                     "SET reprogramada = ( reprogramada + 1 ) " +
                     "WHERE id_pedido = ?";

        logger.debug("SQL (updPedidoReagendado): " + sql);

        try {

            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idPedido);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param criterio
     * @return
     */
    public List getPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws PedidosDAOException {
        List listaPedidos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        //paginacion
        int pag = criterio.getPag();
        int regXpag = criterio.getRegsperpag();
        if ( pag <= 0) pag = 1;
        if ( regXpag < 10) regXpag = 10;
        int iniReg = (pag-1)*regXpag + 1;
        int finReg = pag*regXpag;
        
        String sql = "SELECT * FROM ( " +
                     " SELECT row_number() over( order by p.ID_PEDIDO ) as row, " +
                     " P.ID_PEDIDO, JD.FECHA fdespacho, HD.HINI hdespacho, HD.HFIN hdespachof, " +
                     " L.nom_local local, P.origen, P.tipo_ve, Z.id_zona, Z.nombre as zona_nombre," +
                     " p.dir_tipo_calle, p.dir_calle, p.dir_numero, p.dir_depto, " +
                     " pe.nro_guia_caso, E.ID_ESTADO, E.NOMBRE NOMBRE_ESTADO, c.NOMBRE comuna, PE.REPROGRAMADA " +
                     " FROM BODBA.BO_PEDIDOS P " +
                     "      JOIN BODBA.BO_LOCALES         L ON P.id_local      = L.id_local " +
                     "      JOIN BODBA.BO_JORNADA_DESP   JD ON P.ID_JDESPACHO  = JD.ID_JDESPACHO " +
                     "      JOIN BODBA.BO_HORARIO_DESP   HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " + 
                     "      JOIN BODBA.BO_ZONAS           Z ON Z.ID_ZONA       = P.ID_ZONA " +
                     "      JOIN BODBA.BO_ESTADOS   E ON E.ID_ESTADO = P.ID_ESTADO " +
                     "      JOIN bodba.bo_pedidos_ext pe on p.id_pedido = pe.id_pedido " +
                     "      LEFT JOIN bo_comunas c ON c.id_comuna = p.id_comuna " +                     
                     " WHERE  pe.id_ruta is null AND P.ID_ESTADO NOT IN (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ",10,20) " + wherePedidosPendientesbyCriterio(criterio) +
                     " ORDER BY JD.FECHA ";
        sql +=       " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");   
            rs = stm.executeQuery();
            
            while (rs.next()) {
                MonitorPedidosDTO lin1 = new MonitorPedidosDTO();
                lin1.setId_pedido(rs.getLong("id_pedido"));
                lin1.setFdespacho(rs.getString("fdespacho"));
                lin1.setHdespacho(rs.getString("hdespacho"));
                lin1.setHdespacho_fin(rs.getString("hdespachof"));
                lin1.setLocal_despacho(rs.getString("local"));
                lin1.setZona_nombre(rs.getString("zona_nombre"));
                lin1.setId_zona(rs.getLong("id_zona"));
                lin1.setOrigen(rs.getString("origen"));
                lin1.setTipo_ve(rs.getString("tipo_ve"));
                lin1.setId_estado(rs.getLong("ID_ESTADO"));
                lin1.setEstado(rs.getString("NOMBRE_ESTADO"));
                String dir = "";
                if ( rs.getString("dir_tipo_calle") != null )
                    dir += rs.getString("dir_tipo_calle") + " ";
                if ( rs.getString("dir_calle") != null )
                    dir += rs.getString("dir_calle") + " ";
                if ( rs.getString("dir_numero") != null )
                    dir += rs.getString("dir_numero") + " ";
                if ( rs.getString("dir_depto") != null )
                    dir += rs.getString("dir_depto") + " ";
                lin1.setDireccion(dir);
                
                if ( rs.getString("comuna") == null ) {
                    lin1.setComuna("");
                } else {
                    lin1.setComuna(rs.getString("comuna"));
                }
                
                PedidoExtDTO pedExt = new PedidoExtDTO();
                pedExt.setIdPedido(rs.getLong("id_pedido"));
                pedExt.setNroGuiaCaso(rs.getLong("nro_guia_caso"));
                pedExt.setReprogramada(rs.getInt("REPROGRAMADA"));
                lin1.setPedExt(pedExt);                
                listaPedidos.add(lin1);                
            }
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return listaPedidos;
    }

    /**
     * @param criterio
     * @return
     */
    private String wherePedidosPendientesbyCriterio(DespachoCriteriaDTO criterio) {
        String sql = "";
        if ( criterio.getClienteRut().length() > 0 ) {            
            long rut = 0;
            try {
                rut = Long.parseLong(criterio.getClienteRut());    
            } catch (Exception e) { }            
            sql += " and p.RUT_CLIENTE = " + rut;
        }
        if ( criterio.getClienteApellido().length() > 0 ) {
            sql += " and UPPER(p.NOM_CLIENTE) like '%" + criterio.getClienteApellido().toUpperCase() + "%'";
        }
        if ( criterio.getF_despacho() != null && !"".equalsIgnoreCase(criterio.getF_despacho()) )
            sql += " and JD.fecha = '" + criterio.getF_despacho() + "'";
        if ( criterio.getH_inicio() != null && !"".equalsIgnoreCase( criterio.getH_inicio() ) ) {
            sql += " AND HD.hini = '" + criterio.getH_inicio() + "'";
        }
        if ( criterio.getH_fin() != null && !"".equalsIgnoreCase( criterio.getH_fin() ) ) {
            sql += " AND HD.hfin = '" + criterio.getH_fin() + "'";
        }
        if ( criterio.getId_zona() != 0 && criterio.getId_zona() != -1 )
            sql += " and Z.id_zona = " + criterio.getId_zona();
        if ( criterio.getId_local() != 0 && criterio.getId_local() != -1 )
            sql += " and P.id_local = " + criterio.getId_local();
        if ( criterio.getOrigen() != null && !"".equalsIgnoreCase(criterio.getOrigen()) && !"-1".equalsIgnoreCase(criterio.getOrigen()) )
            sql += " and p.origen = '" + criterio.getOrigen() + "'";
        if ( criterio.getReprogramada() != null && !"".equalsIgnoreCase(criterio.getReprogramada()) && "S".equalsIgnoreCase(criterio.getReprogramada()) ) {
            sql += " and pe.reprogramada > 0";
        } else {
            sql += " and pe.reprogramada = 0";
        }
        if ( criterio.getId_pedido() != 0 && criterio.getId_pedido() != -1 )
            sql += " and P.id_pedido = " + criterio.getId_pedido();
               
        return sql;
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws PedidosDAOException {
        double pedidos = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;

        
        String sql = " SELECT count(*) pedidos " +
                     " FROM BODBA.BO_PEDIDOS P " +
                     "      JOIN BODBA.BO_LOCALES         L ON P.id_local      = L.id_local " +
                     "      JOIN BODBA.BO_JORNADA_DESP   JD ON P.ID_JDESPACHO  = JD.ID_JDESPACHO " +
                     "      JOIN BODBA.BO_HORARIO_DESP   HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " +
                     "      JOIN BODBA.BO_ZONAS           Z ON Z.ID_ZONA       = P.ID_ZONA " +
                     "      JOIN BODBA.BO_ESTADOS   E ON E.ID_ESTADO = P.ID_ESTADO " +
                     "      JOIN bodba.bo_pedidos_ext pe on p.id_pedido = pe.id_pedido " +
                     " WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and pe.id_ruta is null " + wherePedidosPendientesbyCriterio(criterio);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");   
            rs = stm.executeQuery();
            
            if (rs.next()) {
                pedidos = rs.getDouble("pedidos");                                
            }
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }

    /**
     * @param idPedido
     * @param idMotivo
     * @param idResponsable
     */
    public void addMotivoResponsableReprogramacion(long idPedido, long idMotivo, long idResponsable, long idJornadaDespachoAnterior, long idUsuario) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "insert into bodba.bo_reprogramacion_desp " +
                     "(id_pedido, id_responsable_desp, id_motivo_desp, id_jdespacho_ant, id_usuario) " +
                     "values (?,?,?,?,?)";

        logger.debug("SQL (addMotivoResponsableReprogramacion): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idPedido);
            stm.setLong(2, idResponsable);
            stm.setLong(3, idMotivo);
            stm.setLong(4, idJornadaDespachoAnterior);
            stm.setLong(5, idUsuario);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getMotivosDespachoNCAll() throws PedidosDAOException {
        List motivos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_motivo_desp, motivo, activado " +
                         "FROM bodba.bo_motivo_despacho ";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ObjetoDTO mot = new ObjetoDTO();
                mot.setIdObjeto(rs.getLong("id_motivo_desp"));
                mot.setNombre(rs.getString("motivo"));
                mot.setActivado(rs.getString("activado"));
                motivos.add(mot);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return motivos;
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNCAll() throws PedidosDAOException {
        List responsables = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_responsable_desp, nombre_responsable, activado " +
                         "FROM bodba.bo_responsable_despacho ";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ObjetoDTO resp = new ObjetoDTO();
                resp.setIdObjeto(rs.getLong("id_responsable_desp"));
                resp.setNombre(rs.getString("nombre_responsable"));
                resp.setActivado(rs.getString("activado"));
                responsables.add(resp);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return responsables;
    }

    /**
     * @param idMotivo
     */
    public void delMotivoNCById(long idMotivo) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from bodba.bo_motivo_despacho " +
                     "where id_motivo_desp = ? ";

        logger.debug("SQL (delMotivoNCById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idMotivo);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param motivo
     */
    public void addMotivoNC(ObjetoDTO motivo) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "insert into bodba.bo_motivo_despacho (motivo, activado) " +
                     "values (?,?) ";

        logger.debug("SQL (addMotivoNC): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, motivo.getNombre());
            stm.setString(2, motivo.getActivado());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param motivo
     */
    public void modMotivoNC(ObjetoDTO motivo) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "update bodba.bo_motivo_despacho set motivo = ?, activado = ? " +
                     "where id_motivo_desp = ? ";

        logger.debug("SQL (modMotivoNC): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, motivo.getNombre());
            stm.setString(2, motivo.getActivado());
            stm.setLong(3, motivo.getIdObjeto());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idResponsable
     */
    public void delResponsableNCById(long idResponsable) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from bodba.bo_responsable_despacho " +
                     "where id_responsable_desp = ? ";

        logger.debug("SQL (delResponsableNCById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idResponsable);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param motivo
     */
    public void addResponsableNC(ObjetoDTO responsable) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "insert into bodba.bo_responsable_despacho (nombre_responsable, activado) " +
                     "values (?,?) ";

        logger.debug("SQL (addResponsableNC): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, responsable.getNombre());
            stm.setString(2, responsable.getActivado());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param motivo
     */
    public void modResponsableNC(ObjetoDTO responsable) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "update bodba.bo_responsable_despacho set nombre_responsable = ?, activado = ? " +
                     "where id_responsable_desp = ? ";

        logger.debug("SQL (modResponsableNC): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, responsable.getNombre());
            stm.setString(2, responsable.getActivado());
            stm.setLong(3, responsable.getIdObjeto());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idEmpresa
     */
    public void delEmpresaTransporteById(long idEmpresa) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from bodba.bo_emp_transporte " +
                     "where id_emp_transporte = ? ";

        logger.debug("SQL (delEmpresaTransporteById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idEmpresa);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try{
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param empresa
     */
    public void addEmpresaTransporte(EmpresaTransporteDTO empresa) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "insert into bodba.bo_emp_transporte (nombre_empresa, activado) " +
                     "values (?,?) ";

        logger.debug("SQL (addEmpresaTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, empresa.getNombre());
            stm.setString(2, empresa.getActivado());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param empresa
     */
    public void modEmpresaTransporte(EmpresaTransporteDTO empresa) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "update bodba.bo_emp_transporte set nombre_empresa = ?, activado = ? " +
                     "where id_emp_transporte = ? ";

        logger.debug("SQL (modEmpresaTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, empresa.getNombre());
            stm.setString(2, empresa.getActivado());
            stm.setLong(3, empresa.getIdEmpresaTransporte());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getEmpresasTransporteAll() throws PedidosDAOException {
        List empresas = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_emp_transporte, nombre_empresa, activado " +
                         "FROM bodba.bo_emp_transporte ";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("id_emp_transporte"));
                emp.setNombre(rs.getString("nombre_empresa"));
                emp.setActivado(rs.getString("activado"));
                empresas.add(emp);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return empresas;
    }

    /**
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporteByLocal(long idLocal) throws PedidosDAOException {
        List patentes = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT PT.ID_PATENTE_TRANS, PT.PATENTE, PT.ACTIVADO, PT.CANT_MAX_BINS, PT.ID_LOCAL, " +
                         "EM.ID_EMP_TRANSPORTE, EM.NOMBRE_EMPRESA " +
                         "FROM BODBA.BO_PATENTE_TRANS PT " +
                         "INNER JOIN BODBA.BO_EMP_TRANSPORTE EM ON EM.ID_EMP_TRANSPORTE = PT.ID_EMP_TRANSPORTE " +
                         "WHERE PT.ID_LOCAL = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLocal);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                PatenteTransporteDTO patente = new PatenteTransporteDTO();
                patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                patente.setPatente(rs.getString("PATENTE"));
                patente.setActivado(rs.getString("ACTIVADO"));
                patente.setCantMaxBins(rs.getLong("CANT_MAX_BINS"));
                patente.setIdLocal(rs.getLong("ID_LOCAL"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                emp.setNombre(rs.getString("NOMBRE_EMPRESA"));
                patente.setEmpresaTransporte(emp);
                patentes.add(patente);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return patentes;
    }

    /**
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporteByLocal(long idLocal) throws PedidosDAOException {
        List fonos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT F.ID_FONO_TRANS, F.CODIGO, F.NUMERO, F.ACTIVADO, F.NOMBRE, F.ID_LOCAL, " +
                         "F.ID_EMP_TRANSPORTE, EM.NOMBRE_EMPRESA " +
                         "FROM  BODBA.BO_FONO_TRANS F " +
                         "INNER JOIN BODBA.BO_EMP_TRANSPORTE EM ON EM.ID_EMP_TRANSPORTE = F.ID_EMP_TRANSPORTE " +
                         "WHERE F.ID_LOCAL = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLocal);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                FonoTransporteDTO fono = new FonoTransporteDTO();
                fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                fono.setCodigo(rs.getLong("CODIGO"));
                fono.setNumero(rs.getLong("NUMERO"));
                fono.setActivado(rs.getString("ACTIVADO"));
                fono.setNombre(rs.getString("NOMBRE"));
                fono.setIdLocal(rs.getLong("ID_LOCAL"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                emp.setNombre(rs.getString("NOMBRE_EMPRESA"));
                fono.setEmpresaTransporte(emp);
                fonos.add(fono);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return fonos;
    }

    /**
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporteByLocal(long idLocal) throws PedidosDAOException {
        List choferes = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT CH.ID_CHOFER_TRANS, CH.NOMBRE_CHOFER, CH.ACTIVADO, CH.RUT, CH.DV, CH.ID_LOCAL, " +
                         "CH.ID_EMP_TRANSPORTE, EM.NOMBRE_EMPRESA " +
                         "FROM BODBA.BO_CHOFER_TRANS CH " +
                         "INNER JOIN BODBA.BO_EMP_TRANSPORTE EM ON EM.ID_EMP_TRANSPORTE = CH.ID_EMP_TRANSPORTE " +
                         "WHERE CH.ID_LOCAL = ?";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLocal);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                chofer.setActivado(rs.getString("ACTIVADO"));
                chofer.setRut(rs.getLong("RUT"));
                chofer.setDv(rs.getString("DV"));
                chofer.setIdLocal(rs.getLong("ID_LOCAL"));
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                emp.setNombre(rs.getString("NOMBRE_EMPRESA"));
                chofer.setEmpresaTransporte(emp);
                choferes.add(chofer);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return choferes;
    }

    /**
     * @param idPatente
     * @return
     */
    public void delPatenteTransporteById(long idPatente) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from BODBA.BO_PATENTE_TRANS " +
                     "where ID_PATENTE_TRANS = ? ";

        logger.debug("SQL (delPatenteTransporteById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idPatente);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param patente
     */
    public long addPatenteTransporte(PatenteTransporteDTO patente) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;   
        ResultSet rs = null;
        long idPatente = 0;
        
        String sql = "INSERT INTO BODBA.BO_PATENTE_TRANS " +
                     "(PATENTE, ACTIVADO, CANT_MAX_BINS, ID_LOCAL, ID_EMP_TRANSPORTE) " +
                     "VALUES (?,?,?,?,?)";

        logger.debug("SQL (addEmpresaTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            stm.setString(1, patente.getPatente());
            stm.setString(2, patente.getActivado());
            stm.setLong(3, patente.getCantMaxBins());
            stm.setLong(4, patente.getIdLocal());
            stm.setLong(5, patente.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                idPatente = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idPatente;
    }

    /**
     * @param patente
     */
    public void modPatenteTransporte(PatenteTransporteDTO patente) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE BODBA.BO_PATENTE_TRANS SET " +
                     "PATENTE=?, ACTIVADO=?, CANT_MAX_BINS=?, ID_LOCAL=?, ID_EMP_TRANSPORTE=? " +
                     "WHERE ID_PATENTE_TRANS = ?";

        logger.debug("SQL (addEmpresaTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, patente.getPatente());
            stm.setString(2, patente.getActivado());
            stm.setLong(3, patente.getCantMaxBins());
            stm.setLong(4, patente.getIdLocal());
            stm.setLong(5, patente.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.setLong(6, patente.getIdPatente());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idFono
     */
    public void delFonoTransporteById(long idFono) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from BODBA.BO_FONO_TRANS " +
                     "where ID_FONO_TRANS = ? ";

        logger.debug("SQL (delFonoTransporteById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idFono);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param fono
     */
    public long addFonoTransporte(FonoTransporteDTO fono) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null; 
        ResultSet rs = null;
        long idFono = 0;
        
        String sql = "INSERT INTO BODBA.BO_FONO_TRANS " +
                     "(CODIGO, NUMERO, ACTIVADO, NOMBRE, ID_LOCAL, ID_EMP_TRANSPORTE) " +
                     "VALUES (?,?,?,?,?,?)";

        logger.debug("SQL (addFonoTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            stm.setLong(1, fono.getCodigo());
            stm.setLong(2, fono.getNumero());
            stm.setString(3, fono.getActivado());
            stm.setString(4, fono.getNombre());
            stm.setLong(5, fono.getIdLocal());
            stm.setLong(6, fono.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                idFono = rs.getLong(1);
            }
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idFono;
    }

    /**
     * @param fono
     */
    public void modFonoTransporte(FonoTransporteDTO fono) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE BODBA.BO_FONO_TRANS SET " +
                     "CODIGO=?, NUMERO=?, ACTIVADO=?, NOMBRE=?, ID_LOCAL=?, ID_EMP_TRANSPORTE=? " +
                     "WHERE ID_FONO_TRANS=?";

        logger.debug("SQL (modFonoTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, fono.getCodigo());
            stm.setLong(2, fono.getNumero());
            stm.setString(3, fono.getActivado());
            stm.setString(4, fono.getNombre());
            stm.setLong(5, fono.getIdLocal());
            stm.setLong(6, fono.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.setLong(7, fono.getIdFono());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idChofer
     */
    public void delChoferTransporteById(long idChofer) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "delete from BODBA.BO_CHOFER_TRANS " +
                     "where ID_CHOFER_TRANS = ? ";

        logger.debug("SQL (delChoferTransporteById): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idChofer);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param chofer
     */
    public long addChoferTransporte(ChoferTransporteDTO chofer) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        long idChofer = 0;
        
        String sql = "INSERT INTO BODBA.BO_CHOFER_TRANS " +
                     "(NOMBRE_CHOFER, ACTIVADO, RUT, DV, ID_LOCAL, ID_EMP_TRANSPORTE) " +
                     "VALUES (?,?,?,?,?,?)";

        logger.debug("SQL (addChoferTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            stm.setString(1, chofer.getNombre());
            stm.setString(2, chofer.getActivado());
            stm.setLong(3, chofer.getRut());
            stm.setString(4, chofer.getDv());
            stm.setLong(5, chofer.getIdLocal());
            stm.setLong(6, chofer.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                idChofer = rs.getLong(1);
            }
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idChofer;
    }

    /**
     * @param chofer
     */
    public void modChoferTransporte(ChoferTransporteDTO chofer) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE BODBA.BO_CHOFER_TRANS SET " +
                     "NOMBRE_CHOFER=?, ACTIVADO=?, RUT=?, DV=?, ID_LOCAL=?, ID_EMP_TRANSPORTE=? " +
                     "WHERE ID_CHOFER_TRANS=?";

        logger.debug("SQL (addChoferTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setString(1, chofer.getNombre());
            stm.setString(2, chofer.getActivado());
            stm.setLong(3, chofer.getRut());
            stm.setString(4, chofer.getDv());
            stm.setLong(5, chofer.getIdLocal());
            stm.setLong(6, chofer.getEmpresaTransporte().getIdEmpresaTransporte());
            stm.setLong(7, chofer.getIdChofer());
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getEmpresasTransporteActivas() throws PedidosDAOException {
        List empresas = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT EM.ID_EMP_TRANSPORTE, EM.NOMBRE_EMPRESA " +
                         "FROM BODBA.BO_EMP_TRANSPORTE EM " +
                         "WHERE EM.ACTIVADO = 'S'";
            
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                EmpresaTransporteDTO emp = new EmpresaTransporteDTO();
                emp.setIdEmpresaTransporte(rs.getLong("ID_EMP_TRANSPORTE"));
                emp.setNombre(rs.getString("NOMBRE_EMPRESA"));
                empresas.add(emp);
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return empresas;
    }

    /**
     * @param idChofer
     * @param descripcion
     * @param idUsuario
     */
    public void addLogChoferTransporte(long idChofer, String descripcion, long idUsuario) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "INSERT INTO BODBA.BO_CHOFER_TRANS_LOG " +
                     "(ID_CHOFER_TRANS, DESCRIPCION, ID_USUARIO) " +
                     "VALUES (?,?,?)";

        logger.debug("SQL (addLogChoferTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idChofer);
            stm.setString(2, descripcion);
            stm.setLong(3, idUsuario);
            
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idFono
     * @param descripcion
     * @param idUsuario
     */
    public void addLogFonoTransporte(long idFono, String descripcion, long idUsuario) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "INSERT INTO BODBA.BO_FONO_TRANS_LOG " +
                     "(ID_FONO_TRANS, DESCRIPCION, ID_USUARIO) " +
                     "VALUES (?,?,?)";

        logger.debug("SQL (addLogFonoTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idFono);
            stm.setString(2, descripcion);
            stm.setLong(3, idUsuario);
            
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try{
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPatente
     * @param descripcion
     * @param idUsuario
     */
    public void addLogPatenteTransporte(long idPatente, String descripcion, long idUsuario) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "INSERT INTO BODBA.BO_PATENTE_TRANS_LOG " +
                     "(ID_PATENTE_TRANS, DESCRIPCION, ID_USUARIO) " +
                     "VALUES (?,?,?)";

        logger.debug("SQL (addLogPatenteTransporte): " + sql);

        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idPatente);
            stm.setString(2, descripcion);
            stm.setLong(3, idUsuario);
            
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }
    
    /**
     * @param id_pedido
     * @return
     * @throws PedidosDAOException
     */
    public boolean esEscolares(long id_pedido) throws PedidosDAOException {
    	System.out.print(">>>>>>>>>>>>>>>>entrando a esEscolares en jdbcPedidosDAO().<<<<<<<<<<<<<<<<<");
    	
       String sql = "select sum(cant_solic) as CANTIDAD from ( "
             + " select distinct id_producto, cant_solic from bodba.bo_detalle_pedido ped "
             + " inner join fodba.fo_productos fpro on fpro.pro_id_bo = ped.id_producto and ped.id_pedido = "
             + id_pedido + " inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id "
             + " inner join fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id "
             + " inner join fodba.fo_catsubcat fsub on fsub.subcat_id = fsubcat.cat_id "
             + " inner join fodba.fo_categorias fcat on fcat.cat_id = fsub.cat_id  and fcat.cat_nombre = 'Librería' "
             + " ) as x ";
       PreparedStatement stm = null;
       ResultSet rs = null;
       int cantidad = 0;
       try {
          conn = this.getConnection();
          stm = conn.prepareStatement(sql + " WITH UR");
          rs = stm.executeQuery();
          if (rs.next()) {
             cantidad = rs.getInt("CANTIDAD");
          }
          
       } catch (SQLException e) {
          throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return cantidad >= 20;
    }

    
    /**
     * Regla para la validacion de productos con categoria "Validacion"
     * fecha 18NOV2013
     * autor: HRP
     * @param id_pedido
     * @return
     * @throws PedidosDAOException
     */
    public boolean verificaPrimeraCompraRetiroEnLocal(long id_pedido) throws PedidosDAOException {      
    	System.out.println(">>>>>>>>>>>>>>>>entrando a enValidacionManual en jdbcPedidosDAO().<<<<<<<<<<<<<<<<<");
    	StringBuffer sb0 = new StringBuffer().append("SELECT ID_CLIENTE, TIPO_DESPACHO FROM BODBA.BO_PEDIDOS WHERE ID_PEDIDO = ::1 WITH UR");
    	PreparedStatement stm = null;
        ResultSet rs = null;
        int cantidad = 0;
        String id_cliente = "";
        String tipoDespacho="";
        try {
        	 conn = this.getConnection();
             stm = conn.prepareStatement(sb0.toString().replaceFirst("::1", String.valueOf(id_pedido)));
             rs = stm.executeQuery();
             if (rs.next()) {
                 id_cliente = rs.getString("ID_CLIENTE");
                 tipoDespacho= rs.getString("TIPO_DESPACHO");
             }
             if(null != tipoDespacho && "R".equalsIgnoreCase(tipoDespacho)){    	
            	 sb0 = new StringBuffer().append("SELECT COUNT (ID_PEDIDO) AS EXISTE_COMPRA_RETIRO FROM BO_PEDIDOS WHERE ID_CLIENTE = ::1 AND TIPO_DESPACHO = 'R' WITH UR");
                 conn = this.getConnection();
                 stm = conn.prepareStatement(sb0.toString().replaceFirst("::1", String.valueOf(id_cliente)));
                 rs = stm.executeQuery();
                 if (rs.next()) {
                    cantidad = rs.getInt("EXISTE_COMPRA_RETIRO");
                 }
             }
       } catch (SQLException e) {
          throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return cantidad == 1;
    }
    
//    public ProductosPedidoDTO getPedidoAlertaPrimeraCompraEnLocal(long id_pedido) throws PedidosDAOException {      
//    	System.out.println(">>>>>>>>>>>>>>>>entrando a enValidacionManual en jdbcPedidosDAO().<<<<<<<<<<<<<<<<<");
//    	
//       StringBuffer sb0 = new StringBuffer().append("SELECT COUNT (ID_PEDIDO) AS EXISTE_COMPRA_RETIRO FROM BO_PEDIDOS WHERE ID_CLIENTE = ::1 AND TIPO_DESPACHO = 'R' WITH UR");
//    		   
//       PreparedStatement stm = null;
//       ResultSet rs = null;
//       int cantidad = 0;
//       try {
//          conn = this.getConnection();
//          stm = conn.prepareStatement(sb0.toString().replaceFirst("::1", String.valueOf(id_cliente)));
//          rs = stm.executeQuery();
//          if (rs.next()) {
//             cantidad = rs.getInt("EXISTE_COMPRA_RETIRO");
//          }
//          
//       } catch (SQLException e) {
//          throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
//		} finally {
//			try {
//				if (rs != null)
//					rs.close();
//				if (stm != null)
//					stm.close();
//				releaseConnection();
//			} catch (SQLException e) {
//				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
//			}
//		}
//       return cantidad < 1;
//    }
    /**
     * Regla para la validacion de productos con categoria "Validacion"
     * fecha 18NOV2013
     * autor: HRP
     * @param id_pedido
     * @return
     * @throws PedidosDAOException
     */
    public boolean enValidacionManual(long id_pedido, String key_Validation) throws PedidosDAOException {      
    	System.out.println(">>>>>>>>>>>>>>>>entrando a enValidacionManual en jdbcPedidosDAO().<<<<<<<<<<<<<<<<<");
    	
       StringBuffer sb0 = new StringBuffer().append("select sum(cant_solic) as CANTIDAD from ( ")
    		   .append(" select distinct id_producto, fsubcat.cat_id, cant_solic from bodba.bo_detalle_pedido ped ")
			   .append(" inner join fodba.fo_productos fpro on fpro.pro_id_bo = ped.id_producto ")
			   .append(" inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id ")
			   .append(" inner join fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id ")
			   .append(" WHERE ped.id_pedido = ::1 ")
			   .append(" and upper(fsubcat.cat_nombre) = upper('::2')")
    		   .append(") as DualX WITH UR");
    		   
       PreparedStatement stm = null;
       ResultSet rs = null;
       int cantidad = 0;
       try {
          conn = this.getConnection();
          stm = conn.prepareStatement(sb0.toString().replaceFirst("::1", String.valueOf(id_pedido)).replaceFirst("::2", key_Validation));
          rs = stm.executeQuery();
          if (rs.next()) {
             cantidad = rs.getInt("CANTIDAD");
          }
          
       } catch (SQLException e) {
          throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
       return cantidad >= 1;
    }


    
    /**
     * @param idPedido
     * @return
     */
    public List getProductosXAlerta(long id_pedido, String key_Validation) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List productos = new ArrayList();
        
        StringBuffer sb0 = new StringBuffer()        
		        .append(" select distinct id_producto, pro_cod_sap, fsubcat.cat_id, cant_solic, ")
		        .append(" PRO_TIPO_PRODUCTO, fmarca.MAR_NOMBRE,PRO_DES_CORTA, ")
		        .append(" ped.precio, loc.nom_local, jd.fecha, hd.HINI, hd.HFIN ")
		        .append(" from bodba.bo_detalle_pedido ped ")
		        .append(" inner join bodba.bo_pedidos p on (p.id_pedido = ped.id_pedido) ")
		        .append(" inner join bodba.bo_locales loc on (loc.id_local = p.id_local) ")
		        .append(" inner join bodba.bo_jornada_desp jd on (jd.id_jdespacho = p.id_jdespacho) ")
		        .append(" inner join bodba.bo_horario_desp hd on (hd.id_hor_desp = jd.id_hor_desp) ")
		        .append(" inner join fodba.fo_productos fpro on fpro.pro_id_bo = ped.id_producto ")
		        .append(" inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id ")
		        .append(" inner join fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id ")
		        .append(" inner join fodba.FO_MARCAS fmarca on fmarca.MAR_ID = fpro.PRO_MAR_ID ")
		  		.append(" WHERE ped.id_pedido = ::1 ")
		  		.append(" and upper(fsubcat.cat_nombre) = upper('::2')")
		      	.append(" WITH UR");
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sb0.toString().replaceFirst("::1", String.valueOf(id_pedido)).replaceFirst("::2", key_Validation));
            rs = stm.executeQuery();
            
            while (rs.next()) {
                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                prod.setId_producto(rs.getLong("ID_PRODUCTO"));
                prod.setCod_sap(rs.getString("PRO_COD_SAP"));
                prod.setId_catprod(rs.getString("CAT_ID"));
                prod.setCant_solic(rs.getDouble("CANT_SOLIC"));
                prod.setPrecio(rs.getDouble("PRECIO"));
                prod.setNombreLocal(rs.getString("NOM_LOCAL"));
                prod.setFechaDespacho(rs.getString("FECHA"));
                prod.setHoraInicio(rs.getString("HINI"));
                prod.setHoraFin(rs.getString("HFIN"));
                prod.setDescripcion(rs.getString("PRO_TIPO_PRODUCTO")+ " "+rs.getString("MAR_NOMBRE") + " " + rs.getString("PRO_DES_CORTA"));
                
                productos.add(prod);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosXAlerta - Problema SQL (close)", e);
			}
		}
        return productos;
    }
    
    /**
     * @param idPedido
     * @return
     */
    public List getProductosTodasTrxByPedido(long idPedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List productos = new ArrayList();
        
        String  sql = "SELECT distinct td.ID_PRODUCTO, td.PRECIO, td.DESCRIPCION " +
                      "FROM BO_TRX_MP_DETALLE td " +
                      "WHERE td.ID_PEDIDO = ? and td.ID_PRODUCTO != 0 " +
                      "ORDER BY td.DESCRIPCION ";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idPedido);
            rs = stm.executeQuery();
            while (rs.next()) {
                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                prod.setId_producto(rs.getLong("ID_PRODUCTO"));
                prod.setPrecio(rs.getDouble("PRECIO"));
                prod.setDescripcion(rs.getString("DESCRIPCION"));
                productos.add(prod);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return productos;
    }

    /**
     * @param idProducto
     * @param porcion
     * @param unidadPorcion
     */
    public void modPorcionProducto(long idProducto, double porcion, long unidadPorcion) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "UPDATE FODBA.FO_PRODUCTOS " +
                     "SET PILA_PORCION=?, ID_PILA_UNIDAD=? " +
                     "WHERE PRO_ID=? ";
        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setDouble(1, porcion);
            stm.setLong(2, unidadPorcion);
            stm.setLong(3, idProducto);            
            stm.executeUpdate();            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idProducto
     * @param idPila
     */
    public void delPilaProducto(long idProducto, long idPila) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "DELETE FROM FODBA.FO_PILA_PRODUCTO " +
                     "WHERE PRO_ID=? AND ID_PILA=? ";
        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, idProducto);
            stm.setLong(2, idPila);
            stm.executeUpdate();            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param pilaProd
     */
    public void addPilaProducto(PilaProductoDTO pilaProd) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        
        String sql = "INSERT INTO FODBA.FO_PILA_PRODUCTO " +
                     "(PRO_ID,ID_PILA,NUTRIENTE_PORCION,PORCENTAJE) " +
                     "VALUES " +
                     "(?,?,?,?)";
        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, pilaProd.getIdProductoFO());
            stm.setLong(2, pilaProd.getPila().getIdPila());
            stm.setDouble(3, pilaProd.getNutrientePorPorcion());
            stm.setDouble(4, pilaProd.getPorcentaje());
            stm.executeUpdate();            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getUnidadesNutricionales() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List unidades = new ArrayList();
        
        String  sql = "SELECT ID_PILA_UNIDAD, UNIDAD, DESCRIPCION " +
                      "FROM FODBA.FO_PILA_UNIDAD";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                PilaUnidadDTO pila = new PilaUnidadDTO();
                pila.setIdPilaUnidad(rs.getLong("ID_PILA_UNIDAD"));
                pila.setUnidad(rs.getString("UNIDAD"));
                pila.setDescripcion(rs.getString("DESCRIPCION"));
                unidades.add(pila);
            }
        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return unidades;
    }

    /**
     * @param idProducto
     * @return
     */
    public List getPilasNutricionalesByProductoFO(long idProducto) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pilas = new ArrayList();
        
        String  sql = "SELECT FP.NUTRIENTE_PORCION, FP.PORCENTAJE, P.ID_PILA, " +
                      "P.NOMBRE_NUTRIENTE, PU.ID_PILA_UNIDAD, PU.UNIDAD, PU.DESCRIPCION " +
                      "FROM FODBA.FO_PILA_PRODUCTO FP " +
                      "INNER JOIN FODBA.FO_PILA P ON P.ID_PILA = FP.ID_PILA " +
                      "INNER JOIN FODBA.FO_PILA_UNIDAD PU ON PU.ID_PILA_UNIDAD = P.ID_PILA_UNIDAD " +
                      "WHERE FP.PRO_ID=? ";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1,idProducto);
            rs = stm.executeQuery();
            while (rs.next()) {
                PilaUnidadDTO uni = new PilaUnidadDTO();
                uni.setIdPilaUnidad(rs.getLong("ID_PILA_UNIDAD"));
                uni.setUnidad(rs.getString("UNIDAD"));
                uni.setDescripcion(rs.getString("DESCRIPCION"));
                
                PilaNutricionalDTO pila = new PilaNutricionalDTO();
                pila.setIdPila(rs.getLong("ID_PILA"));
                pila.setNutriente(rs.getString("NOMBRE_NUTRIENTE"));
                pila.setUnidad(uni);
                
                PilaProductoDTO pilaProducto = new PilaProductoDTO();
                pilaProducto.setNutrientePorPorcion(rs.getDouble("NUTRIENTE_PORCION"));
                pilaProducto.setPorcentaje(rs.getDouble("PORCENTAJE"));
                pilaProducto.setPila(pila);
                
                pilas.add(pilaProducto);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pilas;
    }

    /**
     * @return
     */
    public List getPilasNutricionales() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pilas = new ArrayList();
        
        String  sql = "SELECT P.ID_PILA, P.NOMBRE_NUTRIENTE, PU.ID_PILA_UNIDAD, PU.UNIDAD, PU.DESCRIPCION " +
                      "FROM FODBA.FO_PILA P " +
                      "INNER JOIN FODBA.FO_PILA_UNIDAD PU ON PU.ID_PILA_UNIDAD = P.ID_PILA_UNIDAD ";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                PilaUnidadDTO uni = new PilaUnidadDTO();
                uni.setIdPilaUnidad(rs.getLong("ID_PILA_UNIDAD"));
                uni.setUnidad(rs.getString("UNIDAD"));
                uni.setDescripcion(rs.getString("DESCRIPCION"));
                
                PilaNutricionalDTO pila = new PilaNutricionalDTO();
                pila.setIdPila(rs.getLong("ID_PILA"));
                pila.setNutriente(rs.getString("NOMBRE_NUTRIENTE"));
                pila.setUnidad(uni);
                
                pilas.add(pila);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pilas;
    }

    /**
     * @param idUnidadPila
     * @return
     */
    public PilaUnidadDTO getPilaUnidadById(long idUnidadPila) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PilaUnidadDTO unidad = new PilaUnidadDTO();
        
        String  sql = "SELECT ID_PILA_UNIDAD, UNIDAD, DESCRIPCION " +
                      "FROM FODBA.FO_PILA_UNIDAD " +
                      "WHERE ID_PILA_UNIDAD = ?";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setLong(1,idUnidadPila);
            rs = stm.executeQuery();
            if (rs.next()) {
                unidad.setIdPilaUnidad(rs.getLong("ID_PILA_UNIDAD"));
                unidad.setUnidad(rs.getString("UNIDAD"));
                unidad.setDescripcion(rs.getString("DESCRIPCION"));
            }
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return unidad;
    }
    

    /**
     * @param id_jdespacho, TipoDespacho
     * @return
     */
    public boolean VerificaHoraCompra(long id_jdespacho, String TipoDespacho) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean horaValida = false;
        
        /* ENTREGA LA DIFERENCIA EN MINUTOS ENTRE LA FECHA-HORA ACTUAL Y LA FECHA-HORA DE INICIO DEL PICKING
             EN "MIN_LIMITE_CON_VALIDACION" CONSIDERA EL TIEMPO DE VALIDACIÓN Y ES USADO EN LOS DESPACHOS NORMAL Y ECONOMICO
             EN "MIN_LIMITE_SIN_VALIDACION" NO CONSIDERA EL TIEMPO DE VALIDACIÓN Y ES USADO EN LOS DESPACHOS EXPRESS  
        */

        String  sql = "SELECT TIMESTAMPDIFF (4, CHAR ( (TIMESTAMP(CONCAT(CONCAT(CHAR(JP.FECHA), ' '), CHAR(HP.HINI, LOCAL))) - (JP.HRS_VALIDACION*60) MINUTES) - CURRENT TIMESTAMP)) AS MIN_LIMITE_CON_VALIDACION, "
                    + "       TIMESTAMPDIFF (4, CHAR ( TIMESTAMP(CONCAT(CONCAT(CHAR(JP.FECHA), ' '), CHAR(HP.HINI, LOCAL)))  - CURRENT TIMESTAMP)) AS MIN_LIMITE_SIN_VALIDACION, "
                    + "       (JP.HRS_VALIDACION*60) AS MIN_VALIDACION "
                    + "FROM BODBA.BO_JORNADA_DESP  JD " 
                    + "     JOIN BODBA.BO_JORNADAS_PICK JP ON JP.ID_JPICKING  = JD.ID_JPICKING "
                    + "     JOIN BODBA.BO_HORARIO_DESP  HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP "
                    + "     JOIN BODBA.BO_HORARIO_PICK  HP ON HP.ID_HOR_PICK  = JP.ID_HOR_PICK "
                    + "WHERE JD.ID_JDESPACHO = ?";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setLong(1, id_jdespacho);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                if (TipoDespacho.equals("N") || TipoDespacho.equals("C") || TipoDespacho.equals("R") ){//Normal o Económico o Retiro
                    if (rs.getLong("MIN_LIMITE_CON_VALIDACION") > 0){
                        horaValida = true;
                    }
                }else if (TipoDespacho.equals("E")){//Express
                    if (rs.getLong("MIN_LIMITE_SIN_VALIDACION") > 0){
                        horaValida = true;
                    }
                }
            }
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return horaValida;
    }

	/**
	 * @param id_pedido
	 * @return
	 */
	public double getTotalDetPickingByOP(long id_pedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        double total = 0;
        
        String  sql = "SELECT sum(cant_pick * precio) sub_tot FROM BODBA.BO_DETALLE_PICKING WHERE ID_PEDIDO = ? ";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setLong(1,id_pedido);
            rs = stm.executeQuery();
            if (rs.next()) {
                total = (rs.getDouble("sub_tot"));
            }

        } catch (Exception e) {
            logger.error("getTotalDetPickingByOP - Problema SQL", e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("getTotalDetPickingByOP - Problema SQL (close)", e);
			}
		}
        return total ;
	}

	/**
	 * @return
	 */
	public HashMap obtenerLstOPByTBK() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        HashMap pedidos = new HashMap();

        
        String sql = "SELECT TP.ID_PEDIDO, SUM(TP.POS_MONTO_FP) SUMA "
                   + "FROM BODBA.BO_TRX_MP TP "
                   + "WHERE TP.ID_PEDIDO IN (SELECT WP.ID_PEDIDO "
                   + "                       FROM BODBA.WEBPAYS WP "
                   + "                            JOIN BODBA.BO_PEDIDOS P ON P.ID_PEDIDO = WP.ID_PEDIDO "
                   + "                       WHERE WP.TBK_FLG_APTO_CAPTURA IN ('S', 'R') "
                   + "                         AND DATE(WP.CREATED_AT) > (CURRENT DATE - 10 DAYS) "
                   //+ "                         AND P.ID_ESTADO IN (54, 21) "
                   + "                         AND P.ID_ESTADO IN (54) "
                   + "                       ) "
                   + "  AND TP.POS_MONTO_FP IS NOT NULL "
                   + "GROUP BY TP.ID_PEDIDO "
                   + "HAVING SUM(TP.POS_MONTO_FP) > 0.0";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            
            while (rs.next()) {
                pedidos.put(rs.getString("ID_PEDIDO"), new Long(rs.getLong("SUMA")));
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos ;
	}

	/**
	 * @param id_pedido
	 */
	public boolean setActivarFlagCaptura(long id_pedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        boolean res = false;
        
        String sql = "UPDATE BODBA.WEBPAYS "
                   + "   SET TBK_FLG_APTO_CAPTURA = 'S' "
                   + "WHERE ID_PEDIDO = ? ";
        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            stm.setLong(1, id_pedido);
            int i = stm.executeUpdate();
            if (i>0) res = true;
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return res;
	}
	
    /**
     * @param webpayDTO
     */
    public void webpaySave(WebpayDTO w) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;    
        
        String sql = "insert into bodba.webpays(TBK_ORDEN_COMPRA,TBK_CODIGO_AUTORIZACION,TBK_FECHA_CONTABLE, "
            + "TBK_FECHA_TRANSACCION,TBK_FINAL_NUMERO_TARJETA,TBK_HORA_TRANSACCION,TBK_ID_SESION,TBK_ID_TRANSACCION,"
            + "TBK_MAC,TBK_MONTO,TBK_NUMERO_CUOTAS,TBK_RESPUESTA,TBK_TASA_INTERES_MAX,TBK_TIPO_PAGO,"
            + "TBK_TIPO_TRANSACCION,TBK_VCI,ID_PEDIDO) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            con = this.getConnection();

            ps = con.prepareStatement(sql);
            ps.setInt(1, w.getTBK_ORDEN_COMPRA());
            if ( w.getTBK_CODIGO_AUTORIZACION() != null ) {
                ps.setString(2, w.getTBK_CODIGO_AUTORIZACION().trim());    
            } else {
                ps.setString(2, "");
            }            
            ps.setString(3, w.getTBK_FECHA_CONTABLE());
            ps.setString(4, w.getTBK_FECHA_TRANSACCION());
            ps.setString(5, w.getTBK_FINAL_NUMERO_TARJETA());
            ps.setString(6, w.getTBK_HORA_TRANSACCION());
            ps.setString(7, w.getTBK_ID_SESION());
            ps.setBigDecimal(8, w.getTBK_ID_TRANSACCION());
            ps.setString(9, w.getTBK_MAC());
            ps.setInt(10, w.getTBK_MONTO());
            ps.setInt(11, w.getTBK_NUMERO_CUOTAS());
            ps.setInt(12, w.getTBK_RESPUESTA());
            ps.setInt(13, w.getTBK_TASA_INTERES_MAX());
            ps.setString(14, w.getTBK_TIPO_PAGO());
            ps.setString(15, w.getTBK_TIPO_TRANSACCION());
            ps.setString(16, w.getTBK_VCI());
            ps.setInt(17, w.getIdPedido());
            ps.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayMonto(int idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int monto = 0;
        
        String sql = "select MONTO_RESERVADO from bodba.bo_pedidos where id_pedido = ?";
        try {
            con = this.getConnection();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next())
                monto = rs.getInt("MONTO_RESERVADO");
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return monto;
    }

    /**
     * @param idPedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        WebpayDTO wp = new WebpayDTO();
        
        String sql = "select TBK_ORDEN_COMPRA, TBK_TIPO_TRANSACCION, TBK_RESPUESTA, TBK_MONTO, TBK_CODIGO_AUTORIZACION, " +
                     "TBK_FINAL_NUMERO_TARJETA, TBK_FECHA_CONTABLE, TBK_FECHA_TRANSACCION, TBK_HORA_TRANSACCION, " +
                     "TBK_ID_SESION, TBK_ID_TRANSACCION, TBK_TIPO_PAGO, TBK_NUMERO_CUOTAS, TBK_TASA_INTERES_MAX, " +
                     "TBK_VCI, TBK_MAC, ID_PEDIDO from bodba.webpays where ID_PEDIDO = ? order by TBK_ORDEN_COMPRA desc";
        
        try {
            con = this.getConnection();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next()) {
                wp.setTBK_ORDEN_COMPRA(rs.getInt("TBK_ORDEN_COMPRA"));
                wp.setTBK_TIPO_TRANSACCION(rs.getString("TBK_TIPO_TRANSACCION"));
                wp.setTBK_RESPUESTA(rs.getInt("TBK_RESPUESTA"));
                wp.setTBK_MONTO(rs.getInt("TBK_MONTO"));
                wp.setTBK_CODIGO_AUTORIZACION(rs.getString("TBK_CODIGO_AUTORIZACION"));
                wp.setTBK_FINAL_NUMERO_TARJETA(rs.getString("TBK_FINAL_NUMERO_TARJETA"));
                wp.setTBK_FECHA_CONTABLE(rs.getString("TBK_FECHA_CONTABLE"));
                wp.setTBK_FECHA_TRANSACCION(rs.getString("TBK_FECHA_TRANSACCION"));
                wp.setTBK_HORA_TRANSACCION(rs.getString("TBK_HORA_TRANSACCION"));
                wp.setTBK_ID_SESION(rs.getString("TBK_ID_SESION"));
                wp.setTBK_ID_TRANSACCION(rs.getBigDecimal("TBK_ID_TRANSACCION"));
                wp.setTBK_TIPO_PAGO(rs.getString("TBK_TIPO_PAGO"));
                wp.setTBK_NUMERO_CUOTAS(rs.getInt("TBK_NUMERO_CUOTAS"));
                wp.setTBK_TASA_INTERES_MAX(rs.getInt("TBK_TASA_INTERES_MAX"));
                wp.setTBK_VCI(rs.getString("TBK_VCI"));
                wp.setTBK_MAC(rs.getString("TBK_MAC"));
                wp.setIdPedido(rs.getInt("ID_PEDIDO"));
            }
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return wp;
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayGetEstado(int idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idEstado = 0;
        
        String sql = "select ID_ESTADO from bodba.bo_pedidos where id_pedido = ?";
        try {
            con = this.getConnection();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next())
                idEstado = rs.getInt("ID_ESTADO");

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return idEstado;
    }


    /** Entregala informacion de un pedido para enviar por boton de pago
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetPedido(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BotonPagoDTO bp = new BotonPagoDTO();
        
        String sql = "SELECT monto_reservado ";
        sql += "FROM bodba.bo_pedidos ";
        sql += "WHERE id_pedido = ?";
        
        try {
			con = this.getConnection();
			
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next()) {
            	bp.setIdPedido(idPedido);
            	bp.setMontoReservado(rs.getDouble("monto_reservado"));
            }
            

        } catch (Exception e) {
            logger.error("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return bp;
    }

    /**
     * Inserta un registro del resultado del pago con Boton de Pago CAT
     * 
     * @param BotonPagoDTO
     */
    public void botonPagoSave(BotonPagoDTO bp) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;    
        
        String sql = "INSERT INTO bodba.botonpagocat(id_pedido,cat_id_pedido,cat_tipo_autorizacion,cat_codigo_autorizacion,cat_fecha_autorizacion,cat_nro_cuotas,cat_monto_cuota,cat_monto_operacion) ";
        sql += "VALUES (?, ?, ?, ?, CURRENT TIMESTAMP, ?, ?, ?)";
        
        try {
            con = this.getConnection();

            ps = con.prepareStatement(sql);
            ps.setLong(1, bp.getIdPedido());
            ps.setLong(2, bp.getIdCatPedido());
            ps.setString(3, bp.getTipoAutorizacion());
            ps.setString(4, bp.getCodigoAutorizacion());
            ps.setInt(5, bp.getNroCuotas().intValue());
            ps.setInt(6, bp.getMontoCuota().intValue());
            ps.setInt(7, bp.getMontoOperacion().intValue());
            
            ps.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    
    /** Entregala informacion de un registro de boton de pago segun el id del pedido
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BotonPagoDTO bp = new BotonPagoDTO();
        
        String sql = "SELECT id, cat_id_pedido, cat_tipo_autorizacion, cat_codigo_autorizacion, cat_fecha_autorizacion, cat_nro_cuotas, cat_monto_cuota, cat_monto_operacion, cat_cliente_validado, cat_nro_tarjeta, cat_rut_cliente ";
        sql += "FROM bodba.botonpagocat ";
        sql += "WHERE id_pedido = ? ";
        
        try {
			con = this.getConnection();
			
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next()) {
                // id, cat_tipo_autorizacion, cat_codigo_autorizacion, cat_fecha_autorizacion, 
                // cat_nro_cuotas, cat_monto_cuota, cat_monto_operacion,
                // cat_cliente_validado, cat_nro_tarjeta, cat_rut_cliente
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(rs.getTimestamp("cat_fecha_autorizacion").getTime());
                
                bp.setId(rs.getLong("id"));
                bp.setIdPedido(idPedido);
                bp.setIdCatPedido(rs.getLong("cat_id_pedido"));
                bp.setTipoAutorizacion(rs.getString("cat_tipo_autorizacion"));
                bp.setCodigoAutorizacion(rs.getString("cat_codigo_autorizacion"));
                bp.setFechaAutorizacion(cal);
            	bp.setNroCuotas(new Integer(rs.getInt("cat_nro_cuotas")));
            	bp.setMontoCuota(new Integer(rs.getInt("cat_monto_cuota")));
            	bp.setMontoOperacion(new Integer(rs.getInt("cat_monto_operacion")));
                bp.setClienteValidado(rs.getString("cat_cliente_validado"));
                if ( rs.getString("cat_nro_tarjeta") != null ) {
                    bp.setNroTarjeta(rs.getString("cat_nro_tarjeta"));
                } else {
                    bp.setNroTarjeta("*****");    
                }
                bp.setRutCliente(rs.getString("cat_rut_cliente"));
            }
            

        } catch (Exception e) {
            logger.error("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return bp;
    }
    
    public BotonPagoDTO botonPagoGetByPedidoAprobado(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BotonPagoDTO bp = new BotonPagoDTO();
        
        String sql = "SELECT id, cat_id_pedido, cat_tipo_autorizacion, cat_codigo_autorizacion, cat_fecha_autorizacion, cat_nro_cuotas, cat_monto_cuota, cat_monto_operacion, cat_cliente_validado, cat_nro_tarjeta, cat_rut_cliente ";
        sql += "FROM bodba.botonpagocat ";
        sql += "WHERE id_pedido = ? and cat_tipo_autorizacion = 'A' ";
        
        try {
            con = this.getConnection();
            
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next()) {

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(rs.getTimestamp("cat_fecha_autorizacion").getTime());
                
                bp.setId(rs.getLong("id"));
                bp.setIdPedido(idPedido);
                bp.setIdCatPedido(rs.getLong("cat_id_pedido"));
                bp.setTipoAutorizacion(rs.getString("cat_tipo_autorizacion"));
                bp.setCodigoAutorizacion(rs.getString("cat_codigo_autorizacion"));
                bp.setFechaAutorizacion(cal);
                bp.setNroCuotas(new Integer(rs.getInt("cat_nro_cuotas")));
                bp.setMontoCuota(new Integer(rs.getInt("cat_monto_cuota")));
                bp.setMontoOperacion(new Integer(rs.getInt("cat_monto_operacion")));
                bp.setClienteValidado(rs.getString("cat_cliente_validado"));
                if ( rs.getString("cat_nro_tarjeta") != null ) {
                    bp.setNroTarjeta(rs.getString("cat_nro_tarjeta"));
                } else {
                    bp.setNroTarjeta("*****");    
                }
                bp.setRutCliente(rs.getString("cat_rut_cliente"));
            }
            

        } catch (Exception e) {
            logger.error("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return bp;
    }
	

	/**
	 * @param id_pedido
	 * @return
	 */
	public WebpayDTO getWebpayByOP(long id_pedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        WebpayDTO wp = null;
        
        String  sql = " SELECT TBK_ORDEN_COMPRA,TBK_CODIGO_AUTORIZACION,TBK_FECHA_CONTABLE, " +
            		"TBK_FECHA_TRANSACCION,TBK_FINAL_NUMERO_TARJETA,TBK_HORA_TRANSACCION,TBK_ID_SESION,TBK_ID_TRANSACCION," +
           			"TBK_MAC,TBK_MONTO,TBK_NUMERO_CUOTAS,TBK_RESPUESTA,TBK_TASA_INTERES_MAX,TBK_TIPO_PAGO," +
					"TBK_TIPO_TRANSACCION,TBK_VCI FROM bodba.webpays WHERE ID_PEDIDO = ?  AND TBK_RESPUESTA=0";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();
            while (rs.next()) {
            	wp = new WebpayDTO();
            	wp.setTBK_CODIGO_AUTORIZACION(rs.getString("TBK_CODIGO_AUTORIZACION"));
            	wp.setTBK_FECHA_CONTABLE(rs.getString("TBK_FECHA_CONTABLE"));
            	wp.setTBK_FECHA_TRANSACCION(rs.getString("TBK_FECHA_TRANSACCION"));
            	wp.setTBK_FINAL_NUMERO_TARJETA(rs.getString("TBK_FINAL_NUMERO_TARJETA"));
            	wp.setTBK_ORDEN_COMPRA(rs.getInt("TBK_ORDEN_COMPRA"));
            	wp.setTBK_MONTO(rs.getInt("TBK_MONTO"));
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return wp ;
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public String getCodAutorizByOP(long id_pedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String cod = "";
        
        String  sql = " SELECT cat_codigo_autorizacion FROM bodba.botonpagocat WHERE cat_id_pedido = ? ";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, id_pedido);
            rs = stm.executeQuery();
            if (rs.next()) {
            	cod = rs.getString("cat_cod_autorizacion");
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return cod ;
	}
	
	/**
	 * Actualiza registro de Boton de pago CAT
	 */
	public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;    
        boolean res = false;
        
        String sql =" 	UPDATE bodba.botonpagocat " +
        			"	SET cat_cliente_validado= ?, cat_nro_tarjeta= ?, cat_rut_cliente= ?, cat_codigoRespuesta= ?, cat_glosaRespuesta= ? " +
        			"	WHERE id = ? ";
        
        try {
            con = this.getConnection();
            stm = con.prepareStatement( sql );
            
            if ( bp.getClienteValidado() != null ) {
                stm.setString(1, bp.getClienteValidado());    
            } else {
                stm.setNull(1, java.sql.Types.VARCHAR);    
            }
            if ( bp.getNroTarjeta() != null ) {
                stm.setString(2, bp.getNroTarjeta());
            } else {
                stm.setNull(2, java.sql.Types.VARCHAR);
            }
            if ( bp.getRutCliente() != null ) {
                stm.setString(3, bp.getRutCliente());
            } else {
                stm.setNull(3, java.sql.Types.VARCHAR);
            }
            if ( bp.getCodRespuesta() != null ) {
                stm.setString(4, bp.getCodRespuesta());
            } else {
                stm.setNull(4, java.sql.Types.VARCHAR);
            }
            if ( bp.getGlosaRespuesta() != null ) {
                stm.setString(5, bp.getGlosaRespuesta());
            } else {
                stm.setNull(5, java.sql.Types.VARCHAR);
            }
            stm.setLong(6, bp.getId());
            
            int i = stm.executeUpdate();
            if (i>0) res = true;

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return res;
	}

    /**
     * @param idComprador
     * @return
     */
    public List getPedidosPorPagar(long idComprador) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();
        
        String sql = "select P.ID_PEDIDO, P.FCREACION, P.MONTO_PEDIDO monto, P.COSTO_DESPACHO despacho, " +
                     "P.MEDIO_PAGO, P.TIPO_DOC, P.CANT_PRODUCTOS, P.monto_reservado " +
                     "from bodba.bo_pedidos P " +
                     "inner join fodba.ve_cotizacion co on (P.ID_COTIZACION = co.COT_ID) " +
                     "where P.ORIGEN = 'V' and P.ID_ESTADO = 1 and co.COT_CPR_ID = ?";
        
        try {
            con = this.getConnection();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setLong(1, idComprador);
            rs = ps.executeQuery();
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId_pedido(rs.getLong("ID_PEDIDO"));
                ped.setFingreso(rs.getString("FCREACION"));
                ped.setMonto(rs.getDouble("monto"));
                ped.setMonto_reservado(rs.getDouble("monto_reservado"));
                ped.setCosto_despacho(rs.getDouble("despacho"));
                ped.setMedio_pago(rs.getString("MEDIO_PAGO"));
                ped.setTipo_doc(rs.getString("TIPO_DOC"));
                ped.setCant_prods(rs.getLong("CANT_PRODUCTOS"));
                pedidos.add(ped);
            }
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }

    /**
     * @param idPedido
     * @return
     */
    public void actualizaSecuenciaPago(long idPedido) throws PedidosDAOException {
        Connection con = null;
        PreparedStatement stm = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sqlSelect = "select secuencia_pago from bodba.bo_pedidos WHERE id_pedido = ?";
        String sqlUpdate = "UPDATE bodba.bo_pedidos SET secuencia_pago = ? WHERE id_pedido = ?";
        
        try {
            con = this.getConnection();
            
            String secuencia = "";    
            ps = con.prepareStatement(sqlSelect + " WITH UR");
            ps.setLong(1, idPedido);
            rs = ps.executeQuery();
            if (rs.next()) {
                secuencia = Utils.secuenciaStr( rs.getInt("secuencia_pago") + 1 );
            }
            rs.close();
            ps.close();
            
            stm = con.prepareStatement( sqlUpdate );
            stm.setString(1, secuencia);
            stm.setLong(2, idPedido);
            stm.executeUpdate();
            

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @return
     */
    public boolean pedidoEsFonoCompra(int idPedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean fonoCompra = false;        
        String  sql = " SELECT id_usuario_fono FROM bodba.bo_pedidos WHERE id_pedido = ?";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idPedido);
            rs = stm.executeQuery();
            if (rs.next()) {
                if ( rs.getString("id_usuario_fono") != null ) {
                    if ( rs.getLong("id_usuario_fono") > 0 ) {
                        fonoCompra = true;
                    }
                }
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return fonoCompra;
    }

	/**
	 * @param id_pedido, flag
	 * @return
	 */
	public boolean setModFlagWebpayByOP(long id_pedido, String flag)  throws PedidosDAOException {
        PreparedStatement stm = null;
        boolean actualizado = false;
        int numreg = 0;
        
        String SQL = "UPDATE BODBA.WEBPAYS SET TBK_FLG_APTO_CAPTURA = ? WHERE ID_PEDIDO = ? ";
        
        logger.debug("SQL (setModFlagWebpayByOP): " + SQL);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL);
            stm.setString(1, flag);
            stm.setLong(2, id_pedido);
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("La informacion de Webpay de: " + id_pedido + " fueron actualizados.");
				actualizado = true;
			}		

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return actualizado;
	}

    /**
     * @param idPedido
     * @return
     */
    public List getProductosPickeadosByIdPedido(long idPedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();        
        String  sql = "SELECT T.ID_DPICKING, T.ID_DETALLE, T.ID_BP, T.ID_PRODUCTO, T.ID_PEDIDO, T.CBARRA, " +
                      "T.DESCRIPCION, T.PRECIO, T.CANT_PICK, T.SUSTITUTO, T.AUDITADO " +
                      "FROM BODBA.BO_DETALLE_PICKING T " +
                      "WHERE T.ID_PEDIDO = ? " +
                      "ORDER BY T.DESCRIPCION";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idPedido);
            rs = stm.executeQuery();
            while (rs.next()) {
                ProductosPedidoDTO p = new ProductosPedidoDTO();
                p.setId_producto(rs.getLong("ID_PRODUCTO"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setCant_pick(rs.getDouble("CANT_PICK"));
                p.setPrecio(rs.getDouble("PRECIO"));
                pedidos.add(p);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }

    /**
     * @param idPedido
     * @return
     */
    public List getSustitutosYPesablesByPedidoId(long idPedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List sustitutos = new ArrayList();        
        String  sql = "SELECT DP.COD_PROD1, DP.CANT_SOLIC, DP.DESCRIPCION DESC_SOLICITADO, DP.PRECIO PRECIO_SOLICITADO, " +
                      "PIK.ID_DPICKING, PIK.ID_PRODUCTO, PIK.DESCRIPCION DESC_PICKEADO, PIK.CANT_PICK, PIK.PRECIO PRECIO_PICK " +
                      "FROM BODBA.BO_DETALLE_PICKING PIK " +
                      "INNER JOIN BODBA.BO_DETALLE_PEDIDO DP ON PIK.ID_DETALLE = DP.ID_DETALLE AND (PIK.SUSTITUTO = 'S' OR DP.UNI_MED = 'KG') " +
                      "WHERE PIK.ID_PEDIDO = ? " +
                      "ORDER BY DP.DESCRIPCION";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idPedido);
            rs = stm.executeQuery();
            while (rs.next()) {
                SustitutoDTO s = new SustitutoDTO();
                s.setCod_prod1(rs.getString("COD_PROD1"));
                s.setCant1(rs.getDouble("CANT_SOLIC"));
                s.setDescr1(rs.getString("DESC_SOLICITADO"));
                s.setPrecio1(rs.getDouble("PRECIO_SOLICITADO"));
                s.setId_detalle_pick1(rs.getLong("ID_DPICKING"));
                s.setCod_prod2(rs.getString("ID_PRODUCTO"));
                s.setCant2(rs.getDouble("CANT_PICK"));
                s.setDescr2(rs.getString("DESC_PICKEADO"));
                s.setPrecio2(rs.getDouble("PRECIO_PICK"));                
                sustitutos.add(s);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return sustitutos;
    }

    /**
     * @param idPedido
     * @param datosCambiados
     */
    public void cambiarPreciosPickeados(long idPedido, String datosCambiados) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        String sql = "UPDATE bodba.bo_detalle_picking SET precio = ? WHERE id_pedido = ? and id_dpicking = ? ";
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            
            String[] prods = datosCambiados.split("-=-"); 
            
            for ( int i=0; i < prods.length; i++ ) {
                String[] datos = prods[i].split("#");
                
                stm.setLong(1, Long.parseLong( datos[1] ) );
                stm.setLong(2, idPedido);
                stm.setLong(3, Long.parseLong( datos[0] ) );
                
                stm.executeUpdate();    
                
            }
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idPedido
     * @param excedido
     */
    public void modPedidoExcedido(long idPedido, boolean excedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        String sql = "UPDATE bodba.bo_pedidos SET monto_excedido = ? WHERE id_pedido = ?";
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            if ( excedido ) {
                stm.setLong(1, 1);    
            } else {
                stm.setLong(1, 0);
            }
            stm.setLong(2, idPedido);
            stm.executeUpdate();    
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getPedidosEnTransicionTEMP(long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();        
        String  sql = "select p.ID_PEDIDO, p.ID_ESTADO, e.NOMBRE ESTADO, p.ID_LOCAL, l.NOM_LOCAL, p.NUM_MP NRO_TARJETA, p.N_CUOTAS, " +
                      "p.RUT_CLIENTE, p.DV_CLIENTE, p.FECHA_EXP, p.NOM_TBANCARIA, " +
                      "p.FCREACION FECHA_COMPRA, jd.FECHA FECHA_DESPACHO, p.MEDIO_PAGO, sum( trx.MONTO_TRXMP ) MONTO_TRXMP " +
                      "from bodba.bo_pedidos p " +
                      "inner join bodba.bo_jornada_desp jd on p.ID_JDESPACHO = jd.ID_JDESPACHO " +
                      "inner join bodba.bo_horario_desp hd on jd.ID_HOR_DESP = hd.ID_HOR_DESP " +
                      "inner join bodba.bo_estados e on p.ID_ESTADO = e.ID_ESTADO " +
                      "left outer join bodba.bo_locales l on p.ID_LOCAL = l.ID_LOCAL " +
                      "left outer join bodba.bo_trx_mp trx on p.ID_PEDIDO = trx.ID_PEDIDO " +
                      "where jd.FECHA > date('2010-12-08') and p.ID_ESTADO not in (1,20) " +
                      "and p.NUM_MP != 'X' and p.NUM_MP is not null and p.MONTO_RESERVADO = 0 and p.ORIGEN != 'A' and p.MEDIO_PAGO != 'CRE' ";
        if ( idLocal != -1 ) {
            sql += "and p.ID_LOCAL = " +idLocal + " ";
        }
        sql += "group by p.ID_PEDIDO, p.ID_ESTADO, e.NOMBRE, p.ID_LOCAL, l.NOM_LOCAL, p.NUM_MP, p.N_CUOTAS, p.RUT_CLIENTE, p.DV_CLIENTE, p.FECHA_EXP,  p.NOM_TBANCARIA, p.FCREACION, jd.FECHA,  p.MEDIO_PAGO " +
               "order by p.ID_PEDIDO";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                PedidoDTO p = new PedidoDTO();
                p.setId_pedido(rs.getLong("ID_PEDIDO"));
                p.setId_local(rs.getLong("ID_LOCAL"));
                p.setId_estado(rs.getLong("ID_ESTADO"));
                p.setEstado(rs.getString("ESTADO"));
                p.setNom_local(rs.getString("NOM_LOCAL"));
                p.setNum_mp(rs.getString("NRO_TARJETA"));
                
                p.setRut_cliente(rs.getLong("RUT_CLIENTE"));
                p.setDv_cliente(rs.getString("DV_CLIENTE"));
                p.setFecha_exp(rs.getString("FECHA_EXP"));
                p.setNom_tbancaria(rs.getString("NOM_TBANCARIA"));
                
                p.setN_cuotas(rs.getInt("N_CUOTAS"));
                p.setFingreso(rs.getString("FECHA_COMPRA"));
                p.setFdespacho(rs.getString("FECHA_DESPACHO"));
                if ( rs.getString("MONTO_TRXMP") != null ) {
                    p.setMonto(rs.getDouble("MONTO_TRXMP"));    
                } else {
                    p.setMonto(0);    
                }
                pedidos.add(p);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }

    /**
     * @param pedido
     */
    public void purgaPedidoPreIngresado(PedidoDTO pedido, long idCliente) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        String upd1 = "update BODBA.BO_JORNADAS_PICK set CAPAC_OCUPADA = (CAPAC_OCUPADA - ?) " +
                      "where ID_JPICKING = ? and CAPAC_OCUPADA >= ?";
        
        String upd2 = "update BODBA.BO_JORNADA_DESP set CAPAC_OCUPADA = (CAPAC_OCUPADA - 1) " +
                      "where ID_JDESPACHO = ? and CAPAC_OCUPADA >= 1";
        
        String del1 = "delete FROM BODBA.WEBPAYS WHERE ID_PEDIDO = ?";
        
        String del2 = "delete FROM BODBA.BOTONPAGOCAT WHERE ID_PEDIDO = ?";
        
        String del3 = "delete FROM BODBA.BO_TCP WHERE ID_PEDIDO = ?";
        
        String del4 = "delete FROM BODBA.BO_PEDIDOS_EXT WHERE ID_PEDIDO = ?";
        
        String del5 = "delete FROM BODBA.BO_PEDIDOS WHERE ID_ESTADO = 1 AND ID_PEDIDO = ? AND ID_CLIENTE = ?";
        
        
        
        try {
            conn = this.getConnection();
            //Liberamos capacidad de picking
            stm = conn.prepareStatement(upd1);
            stm.setLong(1, pedido.getCant_prods());
            stm.setLong(2, pedido.getId_jpicking());
            stm.setLong(3, pedido.getCant_prods());
            stm.executeUpdate();    
            
            //Liberamos capacidad de despacho
            stm = conn.prepareStatement(upd2);
            stm.setLong(1, pedido.getId_jdespacho());
            stm.executeUpdate();
            
            if ( Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(pedido.getMedio_pago()) ) {
                //Borramos el dato en la tabla webpays
                stm = conn.prepareStatement(del1);
                stm.setLong(1, pedido.getId_pedido());
                stm.executeUpdate();
                
            } else if ( Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(pedido.getMedio_pago()) ) {
                //Borramos el dato en la tabla botondepagocat
                stm = conn.prepareStatement(del2);
                stm.setLong(1, pedido.getId_pedido());
                stm.executeUpdate();
            }
            //Borramos datos de las promociones
            stm = conn.prepareStatement(del3);
            stm.setLong(1, pedido.getId_pedido());
            stm.executeUpdate();
            
            //Borramos datos del pedido ext
            stm = conn.prepareStatement(del4);
            stm.setLong(1, pedido.getId_pedido());
            stm.executeUpdate();
            
            //Borramos datos del pedido
            stm = conn.prepareStatement(del5);
            stm.setLong(1, pedido.getId_pedido());
            stm.setLong(2, idCliente);
            stm.executeUpdate();
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        
    }

    /**
     * @param idProductoFO
     * @param idPedido
     * @param idDetallePedido
     * @param cantidad
     */
    public void modProductoDePedido(long idPedido, long idDetallePedido, double cantidad) throws PedidosDAOException {
        PreparedStatement stm = null;
        
        String del = "delete FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ?";
        
        String upd = "update BODBA.BO_DETALLE_PEDIDO set CANT_SOLIC = ?, CANT_SPICK = ? WHERE ID_DETALLE = ? AND ID_PEDIDO = ?";
        
        try {
            conn = this.getConnection();
            
            if ( cantidad == 0 ) {
                stm = conn.prepareStatement(del);
                stm.setLong(1, idDetallePedido);
                stm.setLong(2, idPedido);
            } else {
                stm = conn.prepareStatement(upd);
                stm.setDouble(1, cantidad);
                stm.setDouble(2, cantidad);
                stm.setLong(3, idDetallePedido);
                stm.setLong(4, idPedido);
            }
            stm.executeUpdate();    
                       

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param fcIni
     * @param fcFin
     * @param local
     * @param usuario
     * @return
     */
    public List getInformeModificacionDePrecios(String fcIni, String fcFin, long local, String usuario) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List informes = new ArrayList();        
        String  sql = "select T.FECHA fecha_hora, T.USUARIO, T.ID_PEDIDO, dp.ID_DPICKING, dp.DESCRIPCION, " +
                      "SUBSTR(t.DESCRIPCION, LOCATE('ANT PRECIO=', t.DESCRIPCION)+11, ( LOCATE(' NVO', t.DESCRIPCION)-11 - LOCATE('ANT PRECIO=', t.DESCRIPCION) ) ) PRECIO_ANTIGUO, " +
                      "SUBSTR(t.DESCRIPCION, LOCATE('NVO PRECIO=', t.DESCRIPCION)+11) PRECIO_NUEVO, " +
                      "p.MONTO_PEDIDO op_monto_original, p.MONTO_RESERVADO op_monto_reserva, " +
                      " ( select sum( dp.CANT_PICK * dp.PRECIO ) total " +
                      "  from bodba.bo_detalle_picking dp " +
                      "  where dp.ID_PEDIDO = t.ID_PEDIDO " +
                      " ) op_monto_nuevo, " +
                      " ( " +
                      "  SELECT sum(p.prec_valor * dp.CANT_PICK) suma" +
                      "  FROM bodba.bo_precios p " +
                      "  join bodba.bo_detalle_picking dp on p.ID_PRODUCTO = dp.ID_PRODUCTO " +
                      "  join bodba.bo_pedidos ped on ped.ID_PEDIDO = dp.ID_PEDIDO and ped.ID_LOCAL = p.ID_LOCAL " +
                      "  WHERE dp.ID_PEDIDO = t.ID_PEDIDO " +
                      ") op_monto_pick_orig, " +
                      "p.COSTO_DESPACHO " +
                      "from bodba.bo_tracking_od t " +
                      "  inner join bodba.bo_detalle_picking dp on dp.ID_DPICKING = INTEGER( SUBSTR(t.DESCRIPCION, LOCATE('G=', t.DESCRIPCION)+2, ( LOCATE(' ANT', t.DESCRIPCION)-2 - LOCATE('G=', t.DESCRIPCION) ) ) ) " +
                      "  inner join bodba.bo_pedidos p on p.ID_PEDIDO = t.ID_PEDIDO  " +
                      "where t.DESCRIPCION like 'Precio modificado:%' ";
        if ( !"".equalsIgnoreCase(fcIni) ) {
            sql +=    "and date(t.FECHA) >= '" + fcIni + "' ";
        } /*else {
            sql +=    "and t.FECHA = ( current timestamp  ) ";
        }*/
        if ( !"".equalsIgnoreCase(fcFin) ) {
            sql +=    "and date(t.FECHA) <= '" + fcFin + "' ";
       }  /*else {
            sql +=    "and t.FECHA = current timestamp ";
        }*/
        if ( !"".equalsIgnoreCase(usuario) ) {
            sql +=    "and UPPER(t.USUARIO) = UPPER('"+usuario+"') ";
        }
        if ( local != 0 ) {
            sql += "and p.ID_LOCAL = "+local+" ";
        }
        
        if ( "".equalsIgnoreCase(fcIni)&& "".equalsIgnoreCase(fcFin) && "".equalsIgnoreCase(usuario) && local == 0 ) {
            sql += "and date(t.FECHA) = CURRENT DATE " ;            
        }
        sql += "order by T.FECHA";
        
        logger.debug("sql Mod :"+sql);
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                ModificacionPrecioDTO inf = new ModificacionPrecioDTO();
                inf.setFechaHora(rs.getString("fecha_hora"));
                inf.setUsuario(rs.getString("USUARIO"));
                inf.setIdPedido(rs.getLong("ID_PEDIDO"));
                inf.setIdDetPicking(rs.getLong("ID_DPICKING"));
                inf.setProducto(rs.getString("DESCRIPCION"));
                inf.setPrecioOriginal(rs.getDouble("PRECIO_ANTIGUO"));
                inf.setPrecioNuevo(rs.getDouble("PRECIO_NUEVO"));
                inf.setMontoReservaOp(rs.getDouble("op_monto_reserva"));
                inf.setMontoOriginalOp(rs.getDouble("op_monto_pick_orig"));
                inf.setMontoNuevoOp(rs.getDouble("op_monto_nuevo"));
                inf.setDespacho(rs.getDouble("COSTO_DESPACHO"));
                informes.add(inf);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return informes;
    }

    /**
     * @return
     */
    public List getUsuariosInformeModPrecios() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List usuarios = new ArrayList();        
        String  sql = "select distinct T.USUARIO " +
                      "from bodba.bo_tracking_od t " +
                      "where t.DESCRIPCION like 'Precio modificado:%' " +
                      "order by T.USUARIO";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                UsuariosEntity usu = new UsuariosEntity();
                usu.setLogin(rs.getString("USUARIO"));
                usuarios.add(usu);
            }
            

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return usuarios;
    }

    /**
     * @param fcIni
     * @param fcFin
     * @param local
     * @param usuario
     * @return
     */
    public List getInformeProductosSinStock() throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List informes = new ArrayList();        
        String sql = "SELECT pro_id, nom_local " +
        "FROM fo_productos " +
        "inner join fo_precios_locales on pre_pro_id = pro_id " +
        "inner join bo_locales on pre_loc_id = id_local " +
        "and pre_tienestock = 0";
        
        logger.debug("sql Sin Stock :"+sql);
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                ProductoSinStockDTO prod = new ProductoSinStockDTO();
                prod.setIdProductoFo(rs.getLong("pro_id"));
                prod.setNombreLocal(rs.getString("nom_local"));
                informes.add(prod);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return informes;
    }
    
    
    /**
     * @param idPedido
     * @param marcar
     */
    public void marcaAnulacionBoletaEnLocal(long idPedido, boolean marcar) throws PedidosDAOException {
        PreparedStatement stm = null;
        
		String sql = "UPDATE bodba.bo_pedidos SET ANULAR_BOLETA = ? WHERE id_pedido = ?";
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            if ( marcar ) {
                stm.setLong(1, 1);    
            } else {
                stm.setLong(1, 0);
            }
            stm.setLong(2, idPedido);
            stm.executeUpdate();    
            

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * De los pedidos que quedaron en bodega, vemos cuales estan sobrepasados con el monto reservado
     * y los dejamos marcados como montos excedidos
     * 
     * @param id_pedido
     */
    public void marcaMontosReservadosExcedidos(long idPedido) throws PedidosDAOException {
        String sql = "update bodba.bo_pedidos  set MONTO_EXCEDIDO = 1 where id_pedido in ( " +
                     "  select p1.ID_PEDIDO " +
                     "  from bodba.bo_pedidos p1 " +
                     "    inner join ( " +
                     "      select p.ID_PEDIDO, p.COSTO_DESPACHO, sum( dp.PRECIO * dp.CANT_PICK ) as total_pick " +
                     "      from bodba.bo_pedidos p " +
                     "        inner join bodba.bo_detalle_picking dp on dp.ID_PEDIDO = p.ID_PEDIDO " +
                     "      where p.ID_ESTADO = " + Constantes.ID_ESTAD_PEDIDO_EN_BODEGA +
                     "        and p.ID_PEDIDO = " + idPedido +
                     "      group by p.ID_PEDIDO, p.COSTO_DESPACHO " +
                     "    ) as x on ( x.ID_PEDIDO = p1.ID_PEDIDO and ( x.total_pick + x.COSTO_DESPACHO ) > p1.MONTO_RESERVADO ) " +
                     ")";
        logger.debug("SQL marcaMontosReservadosExcedidos: " + sql);
        PreparedStatement stm = null;
        try {
        	stm = conn.prepareStatement(sql);
            int numReg = stm.executeUpdate();
            if ( numReg > 0 ) {
                logger.debug(" El pedido " + idPedido + " quedó con exceso.");
            }
            
        } catch(SQLException e){
            logger.error("marcaMontosReservadosExcedidos: " + e.getMessage());
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}      
    }
    
    /**
     * @return
     */
    public List getPedidosRechazadosErroneamenteTBK(int dias, int minutos) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();        
        String sql = "select p.SECUENCIA_PAGO, p.ORIGEN, p.ID_USUARIO_FONO, p.RUT_CLIENTE, p.NOM_CLIENTE, p.FCREACION, max(p.ID_PEDIDO) ID_PEDIDO, p.TIPO_DESPACHO " +
                     "from bodba.bo_pedidos p " +
                     "inner join bodba.webpays wp on wp.ID_PEDIDO = p.ID_PEDIDO " +
                     "where p.ID_ESTADO in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and wp.TBK_RESPUESTA = 0 " +
                     "and p.FCREACION >= current date - " + dias + " days " +
                     "and wp.CREATED_AT <= current timestamp - " + minutos + " minutes " +
                     "and p.RUT_CLIENTE not in ( " +
                     "   SELECT p.RUT_CLIENTE " +
                     "   FROM BODBA.BO_PEDIDOS p " +
                     "   inner join bodba.webpays wp on wp.ID_PEDIDO = p.ID_PEDIDO " +
                     "   INNER JOIN ( " +
                     "     SELECT p.RUT_CLIENTE, P.FCREACION " +
                     "     FROM BODBA.BO_PEDIDOS p " +
                     "     inner join bodba.webpays wp on wp.ID_PEDIDO = p.ID_PEDIDO " +
                     "     WHERE p.ID_ESTADO in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and wp.TBK_RESPUESTA = 0 " +
                     "     and p.FCREACION >= current date - " + dias + " days " +
                     "     and wp.CREATED_AT <= current timestamp - " + minutos + " minutes " +
                     "   ) as x on x.RUT_CLIENTE = p.RUT_CLIENTE and x.FCREACION = P.FCREACION " +
                     "   WHERE wp.TBK_RESPUESTA = 0 and p.ID_ESTADO not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
                     "   and p.FCREACION >= current date - " + dias + " days " +
                     ") " +
                     "group by p.SECUENCIA_PAGO, p.ORIGEN, p.ID_USUARIO_FONO, p.RUT_CLIENTE, p.NOM_CLIENTE, p.FCREACION, p.TIPO_DESPACHO " +
                     "order by p.RUT_CLIENTE";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId_pedido(rs.getLong("ID_PEDIDO"));
                ped.setSecuenciaPago(rs.getString("SECUENCIA_PAGO"));
                ped.setFingreso(rs.getString("FCREACION"));
                ped.setOrigen(rs.getString("ORIGEN"));
                ped.setId_usuario_fono(rs.getLong("ID_USUARIO_FONO"));
                ped.setRut_cliente(rs.getLong("RUT_CLIENTE"));
                ped.setNom_cliente(rs.getString("NOM_CLIENTE"));
                ped.setTipo_despacho(rs.getString("TIPO_DESPACHO"));
                pedidos.add(ped);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }
    public List getPedidosRechazadosErroneamenteCAT(int dias, int minutos) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();        
        String sql = "select p.SECUENCIA_PAGO, p.ORIGEN, p.ID_USUARIO_FONO, p.RUT_CLIENTE, p.NOM_CLIENTE, p.FCREACION, max(p.ID_PEDIDO) ID_PEDIDO,p.TIPO_DESPACHO " +
                     "from bodba.bo_pedidos p " +
                     "inner join bodba.botonpagocat bp on bp.ID_PEDIDO = p.ID_PEDIDO " +
                     "where p.ID_ESTADO in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and bp.CAT_TIPO_AUTORIZACION = 'A' " +
                     "and p.FCREACION >= current date - " + dias + " days " +
                     "and bp.CREATED_AT <= current timestamp - " + minutos + " minutes " +
                     "and p.RUT_CLIENTE not in ( " +
                     "   SELECT p.RUT_CLIENTE " +
                     "   FROM BODBA.BO_PEDIDOS p " +
                     "   inner join bodba.webpays wp on wp.ID_PEDIDO = p.ID_PEDIDO " +
                     "   INNER JOIN ( " +
                     "     SELECT p.RUT_CLIENTE, P.FCREACION " +
                     "     FROM BODBA.BO_PEDIDOS p " +
                     "     inner join bodba.botonpagocat bp on bp.ID_PEDIDO = p.ID_PEDIDO " +
                     "     WHERE p.ID_ESTADO in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and bp.CAT_TIPO_AUTORIZACION = 'A' " +
                     "     and p.FCREACION >= current date - " + dias + " days " +
                     "     and bp.CREATED_AT <= current timestamp - " + minutos + " minutes " +
                     "   ) as x on x.RUT_CLIENTE = p.RUT_CLIENTE and x.FCREACION = P.FCREACION " +
                     "   WHERE bp.CAT_TIPO_AUTORIZACION = 'A' and p.ID_ESTADO not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
                     "   and p.FCREACION >= current date - " + dias + " days " +
                     ") " +
                     "group by p.SECUENCIA_PAGO, p.ORIGEN, p.ID_USUARIO_FONO, p.RUT_CLIENTE, p.NOM_CLIENTE, p.FCREACION, p.TIPO_DESPACHO " +
                     "order by p.RUT_CLIENTE";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId_pedido(rs.getLong("ID_PEDIDO"));
                ped.setSecuenciaPago(rs.getString("SECUENCIA_PAGO"));
                ped.setFingreso(rs.getString("FCREACION"));
                ped.setOrigen(rs.getString("ORIGEN"));
                ped.setId_usuario_fono(rs.getLong("ID_USUARIO_FONO"));
                ped.setRut_cliente(rs.getLong("RUT_CLIENTE"));
                ped.setNom_cliente(rs.getString("NOM_CLIENTE"));
                ped.setTipo_despacho(rs.getString("TIPO_DESPACHO"));
                pedidos.add(ped);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return pedidos;
    }

    /**
     * @param idProducto
     * @param idLocal
     * @return
     */
    public boolean existeProductoEnLocal(long idProducto, long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean respuesta = false;        
        String sql = "SELECT pro_id " +
                     "FROM fo_productos " +
                     "inner join fo_precios_locales on pre_pro_id = pro_id " +
                     "where pro_id = " + idProducto + " " +
                     "and pre_loc_id = " + idLocal + " " +
                     "and pro_estado = 'A' " +
                     "and pre_estado = 'A'";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            if ( rs.next() ) {
                respuesta = true;
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return respuesta;
    }
    
    
    //Maxbell - Mejoras al catalogo interno 2014/06/30
    public ProductoStockDTO productoTieneStockEnLocal(long idProducto, long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        //boolean respuesta = false;
        ProductoStockDTO producto = new ProductoStockDTO();
        producto.setTieneStock(false);
        producto.setCantidadMaxima(0);
        String sql = "SELECT pro_id, pre_tienestock, pro_inter_max " +
                     "FROM fo_productos " +
                     "inner join fo_precios_locales on pre_pro_id = pro_id " +
                     "where pro_id = " + idProducto + " " +
                     "and pre_loc_id = " + idLocal + " " +
                     "and pro_estado = 'A' " +
                     "and pre_estado = 'A'";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            rs = stm.executeQuery();
            if ( rs.next() ) {
            	if(rs.getInt("pre_tienestock") > 0) {
            		producto.setPro_id(rs.getLong("pro_id"));
            		producto.setTieneStock(true);
            		producto.setCantidadMaxima(rs.getLong("pro_inter_max"));
            	}                
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return producto;
    }
    
    /**
     * Metodo que se encarga de recuperar los productos que se encuentran sobre el margen de sustitución 
     * para ser retirados del bin.
     * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
     * @throws PedidosDAOException
     */
    public List getProductosSustitutosSobreMargen(long id_local)throws PedidosDAOException{
    	logger.debug("Se ingresa al metodo getProductosSustitutosSobreMargen");
    	logger.debug("con id_local " + id_local);
    	PreparedStatement stm = null;
        ResultSet rs = null;
        List productos = new ArrayList();        
        String sql = "SELECT ID_SUSTITUTO_SOBRE_UMBRAL, FECHA_OPERACION, ID_DETALLE, ID_RONDA ," +
        		" PICKEADOR_NOMBRE, PICKEADOR_APELLIDO, DESCRIPCION_PRODUCTO , " +
        		" ID_PRODUCTO, COD_BIN , MENSAJE, ID_PEDIDO" +
        		" FROM BO_SUSTITUTO_SOBRE_UMBRAL" +
        		" WHERE FECHA_OPERACION BETWEEN ((current TIMESTAMP) - 36 HOURS) AND (CURRENT TIMESTAMP) " +
				" AND ID_LOCAL = "+ id_local + " "+
        		" ORDER BY FECHA_OPERACION DESC";
        try {
        	logger.debug("Recupero la conexion");
            conn = this.getConnection();
            logger.debug("Preparo el sql  " + sql );
            stm = conn.prepareStatement( sql + " WITH UR" );
            logger.debug("Executo la query");
            rs = stm.executeQuery();
            logger.debug("Recorro los resultados y los guardo");
            while (rs.next()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	ProductoSobreMargenDTO prod = new ProductoSobreMargenDTO();
                prod.setApellidoPickeador(rs.getString("PICKEADOR_APELLIDO"));
                prod.setCodBin(rs.getString("COD_BIN"));
                prod.setDescripcionProducto(rs.getString("DESCRIPCION_PRODUCTO"));
                prod.setFechaOperacion(format.parse(rs.getString("FECHA_OPERACION")));
                prod.setIdDetalle(rs.getLong("ID_DETALLE"));
                prod.setIdPedido(rs.getLong("ID_PEDIDO"));
                prod.setIdProducto(rs.getLong("ID_PRODUCTO"));
                prod.setIdRonda(rs.getLong("ID_RONDA"));
                prod.setIdSustitutoSobreUmbral(rs.getLong("ID_SUSTITUTO_SOBRE_UMBRAL"));
                prod.setMensaje(rs.getString("MENSAJE"));
                prod.setNombrePickeador(rs.getString("PICKEADOR_NOMBRE"));
                productos.add(prod);
            }
            logger.debug("Cierro las conexiones");

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        logger.debug("Retorno los productos");
        return productos;
    }
    
    /**
     * Metodo que se encarga de eliminar el detalle del reporte de productos de productos sustitutos sobre margen
     * @param idEliminar	id a elminar
     * @return boolean true en caso de exito false en caso de error
     * @throws PedidosDAOException
     */
    public boolean eliminarProductoSustitutoSobreMargen(long idEliminar) throws PedidosDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {
			conn = this.getConnection();
			logger.debug("en eliminarProductoSustitutoSobreMargen");
			String sql = "DELETE FROM  BO_SUSTITUTO_SOBRE_UMBRAL  " +
			" WHERE ID_SUSTITUTO_SOBRE_UMBRAL = ? ";
			logger.debug(sql);
			logger.debug("vals:"+idEliminar);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idEliminar);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
     * @param idPedido
     */
    public void cambiaEstadoWebPays(long idPedido) throws PedidosDAOException {
        PreparedStatement stm = null;
        String upd = "update BODBA.WEBPAYS set TBK_RESPUESTA = -1 WHERE ID_PEDIDO = ?";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(upd);
            stm.setLong(1, idPedido);
            stm.executeUpdate();                       

        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

	public boolean modFechaOP(long id_pedido, String paramFecha) throws PedidosDAOException {
        PreparedStatement stm = null;
        boolean respuesta = false;
        
		String sql = "UPDATE bodba.bo_pedidos SET FCREACION='"+paramFecha+"' WHERE id_pedido = "+id_pedido;
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(sql);
            int numReg = stm.executeUpdate();
            if ( numReg > 0 ) {
                logger.debug(" El pedido " + id_pedido + " quedó con nueva fecha de ingreso.");
                respuesta=true;
            }
   
        }catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return respuesta;
    }
	public void registrarTracking(long id_pedido, String usuario, String descripcion) throws DAOException {
		PreparedStatement stm = null;
		try {
			
			conn = this.getConnection();
			String sql = "INSERT INTO BO_TRACKING_OD(ID_PEDIDO, USUARIO, DESCRIPCION) VALUES(?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_pedido);
			stm.setString(2, usuario);
			stm.setString(3, descripcion);
			stm.executeUpdate();

		} catch (SQLException e) {
			logger.debug("Problema en registrarTracking:"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}
	
	public List getMailPM() throws UsuariosDAOException {

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List lst_mailPM = new ArrayList();
		
		logger.debug("getLocales");
		String SQLStmt =	"SELECT BO_USUARIOS.EMAIL " +
							"FROM BO_CARGOS  " +
							"INNER JOIN BO_USUARIOS   " +
							"ON BO_CARGOS.ID_CARGO=BO_USUARIOS.ID_CARGO  " +
							"WHERE  " +
							"BO_CARGOS.NOMBRE_CARGO='Product Manager Food No Perecibles' OR " +
							"BO_CARGOS.NOMBRE_CARGO='Product Manager Food Perecibles' OR " +
							"BO_CARGOS.NOMBRE_CARGO='Product Manager Non Food' OR " +
							"BO_CARGOS.NOMBRE_CARGO='Product Manager Otros' OR " +
							"BO_CARGOS.NOMBRE_CARGO='Asistente Comercial'" ;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			
			rs = stm.executeQuery();
			while (rs.next()) {
				lst_mailPM.add(rs.getString("EMAIL"));
			}
		} catch (SQLException e) {
			logger.debug("Problema getLocales :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("total registros: "+lst_mailPM.size());
		return lst_mailPM;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	//public class JdbcNewPedidosDAO implements PedidosDAO
	
    /**
     * Obtiene datos del cliente
     * 
     * @param id_cliente
     *            long
     * @return ClienteEntity
     * @throws ClientesDAOException
     */
    public ClienteEntity getClienteById(long id_cliente) throws ClientesDAOException {

        ClienteEntity cli = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        long cli_id = id_cliente;

        try {

            logger.debug("en getClienteById");
            String sql = "SELECT cli_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat,"
                    + " cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2,"
                    + " cli_rec_info, cli_fec_crea, cli_fec_act, cli_estado, cli_fec_nac, cli_genero, " + " cli_bloqueo, cli_mod_dato, cli_emp_id "
                    //[20121107avc
                    + ", COL_RUT FROM fo_clientes " +
                    		"LEFT JOIN fodba.fo_colaborador ON cli_rut = col_rut WHERE cli_id = ? ";
            		//]20121107avc
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");

            stm.setLong(1, cli_id);
            logger.debug(sql + ", cli_id:" + cli_id);
            rs = stm.executeQuery();
            int i = 0;
            if (rs.next()) {
                cli = new ClienteEntity();
                cli.setId(new Long(rs.getString("cli_id")));
                if (rs.getString("cli_rut") != null)
                    cli.setRut(new Long(rs.getString("cli_rut")));
                cli.setDv(rs.getString("cli_dv"));
                cli.setNombre(rs.getString("cli_nombre"));
                cli.setApellido_pat(rs.getString("cli_apellido_pat"));
                cli.setApellido_mat(rs.getString("cli_apellido_mat"));
                cli.setClave(rs.getString("cli_clave"));
                cli.setEmail(rs.getString("cli_email"));
                cli.setFon_cod_1(rs.getString("cli_fon_cod_1"));
                cli.setFon_num_1(rs.getString("cli_fon_num_1"));
                cli.setFon_cod_2(rs.getString("cli_fon_cod_2"));
                cli.setFon_num_2(rs.getString("cli_fon_num_2"));
                //cli.setRec_info(new Integer(0));
                cli.setRec_info(new Integer(rs.getString("cli_rec_info")));
                cli.setFec_crea(rs.getTimestamp("cli_fec_crea"));
                cli.setFec_act(rs.getTimestamp("cli_fec_act"));
                cli.setEstado(rs.getString("cli_estado"));
                cli.setFec_nac(rs.getTimestamp("cli_fec_nac"));
                cli.setGenero(rs.getString("cli_genero"));
                cli.setBloqueo(rs.getString("cli_bloqueo"));
                cli.setMod_dato(rs.getString("cli_mod_dato"));
                cli.setId_empresa(new Long(rs.getLong("cli_emp_id")));
                //[2012113avc
                if(rs.getInt("COL_RUT") == 0)
                    cli.setColaborador(false);
                else 
                    cli.setColaborador(true);
                //]20121113avc
                i++;
            }
            if (i == 0)
                throw new ClientesDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            if (e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new ClientesDAOException(String.valueOf(e.getErrorCode()), e);
            }
            throw new ClientesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //cierra coneccion
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        return cli;
    }
    
    /**
     * @param idLocal
     * @return
     */
    public LocalDTO getLocalRetiro(long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        LocalDTO local = new LocalDTO();
        try {
            String sql = "SELECT l.id_local id_local, l.nom_local nom_local, l.direccion, l.cod_local, l.TIPO_PICKING " + "FROM bo_locales l "
                    + "WHERE l.retiro_local = 'S' and l.id_local = ?";

            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLocal);
            logger.debug("SQL getLocalRetiro: " + sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                local.setId_local(rs.getLong("id_local"));
                local.setNom_local(rs.getString("nom_local"));
                local.setCod_local(rs.getString("cod_local"));
                local.setDireccion(rs.getString("direccion"));
                local.setTipo_picking(rs.getString("TIPO_PICKING"));
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //cierra coneccion
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        return local;
    }
    
    /**
     * Obtiene datos de direccion
     * 
     * @param dir_id
     *            long
     * @return DireccionEntity
     * @throws PedidosDAOException
     */
    public DireccionEntity getDireccionById(long dir_id) throws PedidosDAOException {
        DireccionEntity dir = null;
        TipoCalleEntity tip = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            logger.debug("en getDireccionById");
            /*
             * String sql = "SELECT D.DIR_ID, D.DIR_CLI_ID, D.DIR_LOC_ID,
             * D.DIR_COM_ID, D.DIR_ZONA_ID, D.DIR_ALIAS, " + " D.DIR_FNUEVA,
             * D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS,
             * D.DIR_ESTADO, " + " D.DIR_FEC_CREA, D.DIR_CUADRANTE, L.NOM_LOCAL,
             * C.NOMBRE NOM_COM, Z.NOMBRE NOM_ZON, " + " TC.TIP_ID,
             * TC.TIP_NOMBRE, TC.TIP_ESTADO, L.TIPO_PICKING " + "FROM
             * FODBA.FO_DIRECCIONES D " + " JOIN BODBA.BO_LOCALES L ON
             * L.ID_LOCAL = D.DIR_LOC_ID " + " JOIN BODBA.BO_COMUNAS C ON
             * C.ID_COMUNA = D.DIR_COM_ID " + " JOIN FODBA.FO_TIPO_CALLE TC ON
             * TC.TIP_ID = D.DIR_TIP_ID " + " JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA =
             * D.DIR_ZONA_ID " + "WHERE DIR_ID = ? ";
             */
            String SQL = "SELECT D.DIR_ID, D.DIR_CLI_ID, Z.ID_LOCAL, D.DIR_COM_ID, Z.ID_ZONA, D.DIR_ALIAS, "
                    + "       D.DIR_FNUEVA, D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, "
                    + "       D.DIR_FEC_CREA, D.DIR_CUADRANTE, L.NOM_LOCAL, C.NOMBRE AS NOM_COMUNA, Z.NOMBRE AS NOM_ZONA, "
                    + "       TC.TIP_ID, TC.TIP_NOMBRE, TC.TIP_ESTADO, L.TIPO_PICKING " + "FROM FODBA.FO_DIRECCIONES D "
                    + "     JOIN BODBA.BO_COMUNAS       C ON C.ID_COMUNA   = D.DIR_COM_ID "
                    + "     JOIN FODBA.FO_TIPO_CALLE   TC ON TC.TIP_ID     = D.DIR_TIP_ID "
                    + "     LEFT JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                    + "     LEFT JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA " + "     JOIN BODBA.BO_LOCALES       L ON L.ID_LOCAL    = Z.ID_LOCAL "
                    + "WHERE D.DIR_ID = ? ";

            logger.debug("SQL (getDireccionById): " + SQL);
            logger.debug("dir_id: " + dir_id);
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL + " WITH UR");
            stm.setLong(1, dir_id);
            rs = stm.executeQuery();
            if (rs.next()) {
                dir = new DireccionEntity();
                tip = new TipoCalleEntity();
                tip.setId(new Long(rs.getString("TIP_ID")));
                tip.setNombre(rs.getString("TIP_NOMBRE"));
                tip.setEstado(rs.getString("TIP_ESTADO"));
                dir.setTipo_calle(tip);
                dir.setId(new Long(rs.getString("DIR_ID")));
                dir.setCli_id(new Long(rs.getString("DIR_CLI_ID")));
                dir.setLoc_cod(new Long(rs.getString("ID_LOCAL")));
                dir.setCom_id(new Long(rs.getString("DIR_COM_ID")));
                dir.setZon_id(new Long(rs.getString("ID_ZONA")));
                dir.setAlias(rs.getString("DIR_ALIAS"));
                dir.setCalle(rs.getString("DIR_CALLE"));
                dir.setNumero(rs.getString("DIR_NUMERO"));
                dir.setDepto(rs.getString("DIR_DEPTO"));
                dir.setComentarios(rs.getString("DIR_COMENTARIOS"));
                dir.setEstado(rs.getString("DIR_ESTADO"));
                dir.setFec_crea(rs.getTimestamp("DIR_FEC_CREA"));
                dir.setFnueva(rs.getString("DIR_FNUEVA"));
                dir.setCuadrante(rs.getString("DIR_CUADRANTE"));
                dir.setNom_local(rs.getString("NOM_LOCAL"));
                dir.setNom_comuna(rs.getString("NOM_COMUNA"));
                dir.setNom_zona(rs.getString("NOM_ZONA"));
                dir.setNom_tip_calle(rs.getString("TIP_NOMBRE"));
                dir.setTipoPicking(rs.getString("TIPO_PICKING"));
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            if (e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
            }
            throw new PedidosDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //cierra coneccion
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        logger.debug("dir:" + dir);
        return dir;
    }
    
    /**
     * @param idLocal
     * @return
     */
    public LocalDTO getLocalById(long idLocal) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        LocalDTO local = new LocalDTO();
        try {
            String sql = "SELECT l.id_local id_local, l.nom_local nom_local, l.direccion, l.cod_local, l.TIPO_PICKING, l.DPC " + "FROM bo_locales l "
                    + "WHERE l.id_local = ?";

            conn = this.getConnection();
            //conn= JdbcDAOFactory.getConexion();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idLocal);
            logger.debug("SQL getLocalRetiro: " + sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                local.setId_local(rs.getLong("id_local"));
                local.setNom_local(rs.getString("nom_local"));
                local.setCod_local(rs.getString("cod_local"));
                local.setDireccion(rs.getString("direccion"));
                local.setTipo_picking(rs.getString("TIPO_PICKING"));
                local.setDpc(rs.getLong("DPC"));
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //cierra coneccion
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        return local;
    }
    
    public boolean esPrimeraCompra(long id_cliente) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int cantidad = 0;

        if (id_cliente == 1) {
            cantidad = 1;
        } else {
            // Filtros de estado
            String[] filtro_estados = Constantes.ESTADOS_PEDIDO_COMPRA_WEB;
            String estados_in = "";
            if (filtro_estados != null && filtro_estados.length > 0) {
                for (int i = 0; i < filtro_estados.length; i++) {
                    estados_in += "," + filtro_estados[i];
                }
                estados_in = estados_in.substring(1);
            }

            String sql = "SELECT COUNT(*) AS CANTIDAD FROM BODBA.BO_PEDIDOS P " + "WHERE P.ID_ESTADO IN (" + estados_in + ") AND P.ID_CLIENTE = " + id_cliente;
            logger.debug("SQL query: " + sql);

            try {
                conn = this.getConnection();
                stm = conn.prepareStatement(sql + " WITH UR");

                rs = stm.executeQuery();
                if (rs.next()) {
                    cantidad = rs.getInt("CANTIDAD");
                }

            } catch (SQLException e) {
                throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (stm != null)
                        stm.close();
                    releaseConnection();
                } catch (SQLException e) {
                    logger.error("[Metodo] : xxx - Problema SQL (close)", e);
                }
            }
        }
        return cantidad == 0;
    }
    

    /**
     * Obtiene un producto del catálogo del FO
     * 
     * @param idProdFo
     *            long
     * @return ProductoEntity
     * @throws PedidosDAOException
     *  
     */
    public ProductoEntity getProductoPedidoByIdProdFO(long idProdFo) throws PedidosDAOException {

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        ProductoEntity prod = null;

        logger.debug("en getProductoPedidoByIdProdFO:");


        String SQL = "SELECT PRO_ID, PRO_ID_PADRE, PRO_TIPRE, PRO_COD_SAP, PRO_UNI_ID, PRO_MAR_ID, PRO_ESTADO, PRO_TIPO_PRODUCTO, PRO_DES_CORTA, PRO_DES_LARGA, PRO_IMAGEN_MINIFICHA, PRO_IMAGEN_FICHA, PRO_UNIDAD_MEDIDA, PRO_VALOR_DIFER, PRO_RANKING_VENTAS, PRO_FCREA, PRO_FMOD, PRO_USER_MOD, PRO_GENERICO, PRO_INTER_VALOR, PRO_INTER_MAX, PRO_PREPARABLE, PRO_NOTA, PRO_ID_BO, MAR_NOMBRE, PRO_PESABLE, PS.ID_SECTOR, rs.impuesto, pro.ID_CATPROD, MAX(b.cod_barra) cod_barra " +
        		"FROM FODBA.FO_PRODUCTOS P " +
        		"LEFT JOIN FODBA.FO_MARCAS M ON MAR_ID = PRO_MAR_ID " +
        		"LEFT JOIN BODBA.BO_PROD_SECTOR PS ON PS.ID_PRODUCTO = P.PRO_ID_BO " +
        		"JOIN bodba.bo_productos pro ON pro.ID_PRODUCTO = p.PRO_ID_BO " +
        		"JOIN bo_codbarra b ON b.id_producto = pro.id_producto " +
        		"LEFT JOIN bodba.bo_rubroxseccion rs ON rs.ID_SECCION = INTEGER(SUBSTR(pro.ID_CATPROD, 1, 2)) AND rs.ID_RUBRO = INTEGER(SUBSTR(pro.ID_CATPROD, 3, 2)) " +
        		"WHERE PRO_ID = ? " +
        		"group by PRO_ID, PRO_ID_PADRE, PRO_TIPRE, PRO_COD_SAP, PRO_UNI_ID, PRO_MAR_ID, PRO_ESTADO, PRO_TIPO_PRODUCTO, PRO_DES_CORTA, PRO_DES_LARGA, PRO_IMAGEN_MINIFICHA, PRO_IMAGEN_FICHA, PRO_UNIDAD_MEDIDA, PRO_VALOR_DIFER, PRO_RANKING_VENTAS, PRO_FCREA, PRO_FMOD, PRO_USER_MOD, PRO_GENERICO, PRO_INTER_VALOR, PRO_INTER_MAX, PRO_PREPARABLE, PRO_NOTA, PRO_ID_BO, MAR_NOMBRE, PRO_PESABLE, PS.ID_SECTOR, rs.impuesto, pro.ID_CATPROD " +
        		"WITH UR";

        logger.debug("SQL: " + SQL + ", id_prod_fo: " + idProdFo);

        try {

            con = this.getConnection();
            stm = con.prepareStatement(SQL);
            stm.setLong(1, idProdFo);
            rs = stm.executeQuery();

            if (rs.next()) {

                prod = new ProductoEntity();
                prod.setId(new Long(rs.getString("pro_id")));

                if (rs.getString("pro_id_padre") != null)
                    prod.setId_padre(new Long(rs.getString("pro_id_padre")));
                else
                    prod.setId_padre(new Long(0));

                prod.setTipre(rs.getString("pro_tipre"));
                prod.setCod_sap(rs.getString("pro_cod_sap"));

                if (rs.getString("pro_uni_id") != null)
                    prod.setUni_id(new Long(rs.getString("pro_uni_id")));
                else
                    prod.setUni_id(new Long(0));
                if (rs.getString("pro_mar_id") != null)
                    prod.setMar_id(new Long(rs.getString("pro_mar_id")));
                else
                    prod.setMar_id(new Long(0));

                prod.setEstado(rs.getString("pro_estado"));
                prod.setTipo(rs.getString("pro_tipo_producto"));
                prod.setDesc_corta(rs.getString("pro_des_corta"));
                prod.setDesc_larga(rs.getString("pro_des_larga"));
                prod.setImg_mini_ficha(rs.getString("pro_imagen_minificha"));
                prod.setImg_ficha(rs.getString("pro_imagen_ficha"));

                if (rs.getString("pro_unidad_medida") != null)
                    prod.setUnidad_medidad(new Double(rs.getString("pro_unidad_medida")));
                else
                    prod.setUnidad_medidad(new Double(0));

                prod.setValor_difer(rs.getString("pro_valor_difer"));

                if (rs.getString("pro_ranking_ventas") != null)
                    prod.setRank_ventas(new Integer(rs.getString("pro_ranking_ventas")));
                else
                    prod.setRank_ventas(new Integer(0));

                prod.setFec_crea(rs.getTimestamp("pro_fcrea"));
                prod.setFec_mod(rs.getTimestamp("pro_fmod"));

                if (rs.getString("pro_user_mod") != null)
                    prod.setUser_mod(new Integer(rs.getString("pro_user_mod")));
                else
                    prod.setUser_mod(new Integer(0));

                prod.setGenerico(rs.getString("pro_generico"));

                if (rs.getString("pro_inter_valor") != null)
                    prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
                else
                    prod.setInter_valor(new Double("0"));

                if (rs.getString("pro_inter_max") != null)
                    prod.setInter_max(new Double(rs.getString("pro_inter_max")));
                else
                    prod.setInter_max(new Double("0"));

                if (rs.getString("pro_preparable") != null)
                    prod.setEs_prep(rs.getString("pro_preparable"));
                else
                    prod.setEs_prep("N");

                logger.debug("preparable: " + rs.getString("pro_preparable"));

                if (rs.getString("pro_nota") != null)
                    prod.setAdm_coment(rs.getString("pro_nota"));
                else
                    prod.setAdm_coment("");

                prod.setNom_marca(rs.getString("mar_nombre"));
                prod.setId_bo(new Long(rs.getString("pro_id_bo")));
                prod.setEs_pesable(rs.getString("pro_pesable"));
                prod.setId_sector(rs.getLong("ID_SECTOR"));
                
                double impuesto = rs.getDouble("impuesto");
                if(!rs.wasNull()) {
                    prod.setImpuesto(new Double(impuesto));
                }
                
                prod.setCodBarra(rs.getString("cod_barra"));
                String idCatPro = rs.getString("id_catprod");
                prod.setIdSeccion(new Integer(idCatPro.substring(0,2)).intValue());
                prod.setRubro(new Integer(idCatPro.substring(2,4)).intValue());
            }

        } catch (SQLException e) {
            logger.debug("Problema:" + e.getMessage());
            throw new PedidosDAOException(e.getErrorCode() + "");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }

        return prod;
    }
    

    /**
     * @param id_cliente
     * @param id_producto_fo
     * @return
     */
    public CriterioSustitutoDTO getCriterioByClienteAndProducto(long id_cliente, long id_producto_fo) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        CriterioSustitutoDTO criterio = new CriterioSustitutoDTO();

        try {
            String sql = " SELECT SC.id_criterio, SC.desc_criterio, CR.descripcion " + " FROM FODBA.FO_SUSTITUTOS_CLIENTES SC"
                    + "     INNER JOIN FODBA.FO_SUSTITUTOS_CRITERIO CR on (SC.id_criterio = CR.id_criterio)" + " WHERE SC.cli_id = ? " + " AND SC.pro_id = ?";

            logger.debug("SQL getCriterioByClienteAndProducto: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, id_cliente);
            stm.setLong(2, id_producto_fo);
            rs = stm.executeQuery();
            if (rs.next()) {
                criterio.setIdCriterio(rs.getInt("id_criterio"));
                if (rs.getLong("id_criterio") == 4) {
                    criterio.setDescCriterio(rs.getString("desc_criterio"));
                } else {
                    criterio.setDescCriterio(rs.getString("descripcion"));
                }
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        return criterio;
    }
    
    /**
     * Inserta Encabezado de Pedido
     * 
     * @param pedido
     *            PedidoDTO
     * @return long id_pedido, el numero de pedido insertado
     * @throws PedidosDAOException
     *  
     */
    public long doInsEncPedido(PedidoDTO pedido) throws PedidosDAOException {

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        long id_pedido = 0;

        logger.debug("DAO doInsEncPedido");

        String SQLStmt = "INSERT INTO bo_pedidos (id_estado, id_jdespacho, id_jpicking, id_local, id_local_fact, id_comuna, "
                + "id_zona, id_cliente, genero, fnac, nom_cliente, telefono2, telefono, tipo_despacho, "
                + "costo_despacho, fcreacion, monto_pedido, indicacion, medio_pago, num_mp, fecha_exp, n_cuotas, "
                + "nom_tbancaria, tb_banco, cant_productos, rut_cliente, dv_cliente, dir_id, dir_tipo_calle, dir_calle, "
                + "dir_numero, dir_depto, pol_id, pol_sustitucion, sin_gente_op, sin_gente_txt, tipo_doc, id_usuario_fono, "
                + "observacion, RUT_TIT, DV_TIT, NOM_TIT, APAT_TIT, AMAT_TIT, DIR_TIT, DIR_NUM_TIT, CLAVE_MP, "
                + "DISPOSITIVO, SIN_GENTE_RUT, SIN_GENTE_DV, TIPO_PICKING, monto_reservado) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "CURRENT DATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        logger.debug("SQL (doInsEncPedido): " + SQLStmt);
        String values ="(";
        try {

            con = this.getConnection();
            stm = con.prepareStatement(SQLStmt, Statement.RETURN_GENERATED_KEYS);

            stm.setLong(1, pedido.getId_estado());
            values += pedido.getId_estado()+",";
            stm.setLong(2, pedido.getId_jdespacho());
            values += pedido.getId_jdespacho()+",";
            stm.setLong(3, pedido.getId_jpicking());
            values += pedido.getId_jpicking()+",";
            stm.setLong(4, pedido.getId_local());
            values += pedido.getId_local()+",";
            stm.setLong(5, pedido.getId_local());
            values += pedido.getId_local()+",";
            if (pedido.getId_comuna() == 0) {
                stm.setNull(6, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setLong(6, pedido.getId_comuna());
                values += pedido.getId_comuna()+",";
            }
            stm.setLong(7, pedido.getId_zona());
            values += pedido.getId_zona()+",";
            stm.setLong(8, pedido.getId_cliente());
            values += pedido.getId_cliente()+",";
            stm.setString(9, pedido.getGenero());
            values += "'"+pedido.getGenero()+"',";
            stm.setDate(10, new Date(pedido.getFnac().getTime()));
            values += pedido.getFnac().getTime()+",";
            stm.setString(11, pedido.getNom_cliente());
            values += "'"+pedido.getNom_cliente()+"',";
            stm.setString(12, pedido.getTelefono2());
            values += "'"+pedido.getTelefono2()+"',";
            stm.setString(13, pedido.getTelefono());
            values += "'"+pedido.getTelefono()+"',";
            stm.setString(14, pedido.getTipo_despacho());
            values += "'"+pedido.getTipo_despacho()+"',";
            stm.setDouble(15, pedido.getCosto_despacho());
            values += pedido.getCosto_despacho()+",";
            //stm.setDate (16, new Date(System.currentTimeMillis()) );
            //MONTO DEL PEDIDO
            stm.setDouble(16, pedido.getMonto());
            values += pedido.getMonto()+",";
            stm.setString(17, pedido.getIndicacion());
            values += "'"+pedido.getIndicacion()+"',";
            stm.setString(18, pedido.getMedio_pago());
            values += "'"+pedido.getMedio_pago()+"',";
            stm.setString(19, "X");
            values += "'X',";

            if (pedido.getFecha_exp() != null) {
                stm.setString(20, pedido.getFecha_exp());
                values += "'"+pedido.getFecha_exp()+"',";
            } else {
                stm.setNull(20, java.sql.Types.INTEGER);
                values += "NULL,";
            }
            stm.setString(21, pedido.getN_cuotas() + "");
            values += "'"+pedido.getN_cuotas()+"',";
            if (pedido.getNom_tbancaria() != null) {
                stm.setString(22, pedido.getNom_tbancaria());
                values += "'"+pedido.getNom_tbancaria()+"',";
            } else {
                stm.setNull(22, java.sql.Types.INTEGER);
                values += "NULL,";
            }
            stm.setString(23, pedido.getTb_banco());
            values += "'"+pedido.getTb_banco()+"',";
            stm.setLong(24, pedido.getCant_prods());
            values += pedido.getCant_prods()+",";
            stm.setLong(25, pedido.getRut_cliente());
            values += pedido.getRut_cliente()+",";
            stm.setString(26, pedido.getDv_cliente() + "");
            values += "'"+pedido.getDv_cliente()+"',";
            if (pedido.getDir_id() == 0) {
                stm.setNull(27, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setLong(27, pedido.getDir_id());
                values += pedido.getDir_id()+",";
            }
            stm.setString(28, pedido.getDir_tipo_calle() + "");
            values += "'"+pedido.getDir_tipo_calle()+"',";
            stm.setString(29, pedido.getDir_calle() + "");
            values += "'"+pedido.getDir_calle()+"',";
            stm.setString(30, pedido.getDir_numero() + "");
            values += "'"+pedido.getDir_numero()+"',";
            stm.setString(31, pedido.getDir_depto() + "");
            values += "'"+pedido.getDir_depto()+"',";
            stm.setLong(32, pedido.getPol_id());
            values += pedido.getPol_id()+",";
            stm.setString(33, pedido.getPol_sustitucion());
            values += "'"+pedido.getPol_sustitucion()+"',";
            stm.setLong(34, pedido.getSin_gente_op());
            values += pedido.getSin_gente_op()+",";
            stm.setString(35, pedido.getSin_gente_txt());
            values += "'"+pedido.getSin_gente_txt()+"',";
            stm.setString(36, pedido.getTipo_doc() + "");
            values += "'"+pedido.getTipo_doc()+"',";

            if (pedido.getId_usuario_fono() > 0) {
                stm.setLong(37, pedido.getId_usuario_fono());
                values += pedido.getId_usuario_fono()+",";
            } else {
                stm.setNull(37, java.sql.Types.INTEGER);
                values += "NULL,";
            }
            //setear observacion
            if (pedido.getObservacion() == null) {
                stm.setNull(38, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(38, pedido.getObservacion());
                values += "'"+pedido.getObservacion()+"',";
            }

            //*************************************************
            //setear observacion
            if (pedido.getRut_tit() == null) {
                stm.setNull(39, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(39, pedido.getRut_tit());
                values += "'"+pedido.getRut_tit()+"',";
            }
            //setear observacion
            if (pedido.getDv_tit() == null) {
                stm.setNull(40, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(40, pedido.getDv_tit());
                values += "'"+pedido.getDv_tit()+"',";
            }
            //setear observacion
            if (pedido.getNom_tit() == null) {
                stm.setNull(41, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(41, pedido.getNom_tit());
                values += "'"+pedido.getNom_tit()+"',";
            }
            //setear observacion
            if (pedido.getApat_tit() == null) {
                stm.setNull(42, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(42, pedido.getApat_tit());
                values += "'"+pedido.getApat_tit()+"',";
            }
            //setear observacion
            if (pedido.getAmat_tit() == null) {
                stm.setNull(43, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(43, pedido.getAmat_tit());
                values += "'"+pedido.getAmat_tit()+"',";
            }
            //setear observacion
            if (pedido.getDir_tit() == null) {
                stm.setNull(44, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(44, pedido.getDir_tit());
                values += "'"+pedido.getDir_tit()+"',";
            }
            //setear observacion
            if (pedido.getDir_num_tit() == null) {
                stm.setNull(45, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(45, pedido.getDir_num_tit());
                values += "'"+pedido.getDir_num_tit()+"',";
            }
            if (pedido.getClave_mp() == null) {
                stm.setNull(46, java.sql.Types.INTEGER);
                values += "NULL,";
            } else {
                stm.setString(46, pedido.getClave_mp());
                values += "'"+pedido.getClave_mp()+"',";
            }
            if (pedido.getDispositivo() == null) {
                stm.setString(47, "B");
                values += "'B',";
            } else {
                stm.setString(47, pedido.getDispositivo());
                values += "'"+pedido.getDispositivo()+"',";
            }
            stm.setLong(48, pedido.getSin_gente_rut());
            values += pedido.getSin_gente_rut()+",";
            stm.setString(49, pedido.getSin_gente_dv());
            values += "'"+pedido.getSin_gente_dv()+"',";
            stm.setString(50, pedido.getTipo_picking());
            values += "'"+pedido.getTipo_picking()+"',";

            stm.setDouble(51, pedido.getMonto_reservado());
            values += pedido.getMonto_reservado()+")";
            logger.info("Values:"+ values);
            //*************************************************

            int i = stm.executeUpdate();

            logger.debug("rc: " + i);

            rs = stm.getGeneratedKeys();
            if (rs.next())
                id_pedido = rs.getInt(1);

        } catch (SQLException e) {
            throw new PedidosDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        //Agregamos los datos extra del pedido
        pedido.setId_pedido(id_pedido);
        PedidoExtDTO pex = new PedidoExtDTO();
        pex.setNroGuiaCaso(0);
        pedido.setPedidoExt(pex);
        this.addDatosPedidoExt(pedido);

        return id_pedido;
    }
    
    /**
     * Agrega registro al log del pedido
     * 
     * @param log
     *            LogPedidoDTO
     * 
     * @throws PedidosDAOException
     *  
     */
    public void addLogPedido(LogPedidoDTO log) throws PedidosDAOException {
        PreparedStatement stmAnt = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            //consultar el ultimo log
            String sqlLog = " SELECT l.id_tracking id_tracking, " + "	l.usuario usuario, " + "	l.fecha fecha, " + "	l.descripcion descripcion, "
                    + "	l.id_mot id_mot, " + "	m.nombre motivo, " + "	l.id_mot_ant id_mot_ant, " + "	mant.nombre motivo_ant  " + " FROM bo_tracking_od l"
                    + " LEFT JOIN BO_MOTIVO m ON m.id_mot=l.id_mot " + " LEFT JOIN BO_MOTIVO mant ON mant.id_mot=l.id_mot_ant "
                    + " where id_tracking in (select max(id_tracking) from bo_tracking_od where id_pedido = ? )";

            conn = this.getConnection();
            //consulta el ultimo log
            stmAnt = conn.prepareStatement(sqlLog + " WITH UR");
            stmAnt.setLong(1, log.getId_pedido());

            rs = stmAnt.executeQuery();

            long id_mot = 0;
            long id_mot_ant = 0;
            if (rs.next()) {
                id_mot = rs.getLong("id_mot");
                id_mot_ant = rs.getLong("id_mot_ant");
            }

            //valores del motivo y motivo anterior
            if (log.getId_motivo() > 0) {
                log.setId_motivo_anterior(id_mot);
            } else {
                log.setId_motivo(id_mot);
                log.setId_motivo_anterior(id_mot_ant);
            }

            String SQLStmt = " INSERT INTO bo_tracking_od (id_pedido, usuario, descripcion, id_mot, id_mot_ant) " + " VALUES (?, ?, ?, ?, ?) ";
            //" INSERT INTO bo_tracking_od (id_pedido, usuario, descripcion,
            // fecha, id_mot, id_mot_ant) " +
            //" VALUES (?, ?, ?, ?, ?, ?) ";

            logger.debug("Ejecución DAO addLogPedido");
            logger.debug("SQL: " + SQLStmt);
            logger.debug("id_pedido:" + log.getId_pedido());
            logger.debug("login: " + log.getUsuario());
            logger.debug("log: " + log.getLog());
            logger.debug("id_motivo: " + log.getId_motivo());
            logger.debug("id_motivo_ant: " + log.getId_motivo_anterior());

            stm = conn.prepareStatement(SQLStmt);

            stm.setLong(1, log.getId_pedido());
            stm.setString(2, log.getUsuario());
            stm.setString(3, log.getLog());
            //stm.setDate(4, new Date(System.currentTimeMillis()));

            if (log.getId_motivo() > 0)
                stm.setLong(4, log.getId_motivo());
            else
                stm.setNull(4, java.sql.Types.INTEGER);

            if (log.getId_motivo_anterior() > 0)
                stm.setLong(5, log.getId_motivo_anterior());
            else
                stm.setNull(5, java.sql.Types.INTEGER);

            int i = stm.executeUpdate();
            logger.debug("Resultado Ejecución: " + i);

        } catch (SQLException e) {
            logger.error(e.toString());
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmAnt != null)
                    stmAnt.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        logger.debug("fin log");

    }
    
    /**
     * Agrega un producto al detalle de un pedido
     * 
     * @param prod
     *            ProductosPedidoDTO
     * @return boolean, devuelve <i>true </i> si se agrego el producto, caso
     *         contrario devuelve <i>false </i>.
     * @throws PedidosDAOException
     *  
     */
    public long agregaProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException {
        long id_det = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String sql = "INSERT INTO bo_detalle_pedido (id_pedido, id_sector, id_producto, cod_prod1, uni_med, "
                + " descripcion, cant_solic, observacion, precio, cant_spick, pesable, preparable,con_nota, tipo_sel,"
                + " precio_lista, dscto_item, id_criterio, desc_criterio) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        try {

            conn = this.getConnection();

            stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, prod.getId_pedido());

            if (prod.getId_sector() > 0) {
                stm.setLong(2, prod.getId_sector());
            } else {
                stm.setNull(2, java.sql.Types.INTEGER);
            }

            stm.setLong(3, prod.getId_producto());
            stm.setString(4, prod.getCod_producto());
            stm.setString(5, prod.getUnid_medida());
            stm.setString(6, prod.getDescripcion());
            stm.setDouble(7, prod.getCant_solic());
            stm.setString(8, prod.getObservacion());
            stm.setDouble(9, prod.getPrecio());
            stm.setDouble(10, prod.getCant_spick());
            stm.setString(11, prod.getPesable());
            stm.setString(12, prod.getPreparable());
            stm.setString(13, prod.getCon_nota());
            stm.setString(14, prod.getTipoSel());
            stm.setDouble(15, prod.getPrecio_lista());
            stm.setDouble(16, prod.getDscto_item());
            if (prod.getIdCriterio() == 0) {
                stm.setLong(17, 1);
            } else {
                stm.setLong(17, prod.getIdCriterio());
            }
            stm.setString(18, prod.getDescCriterio());

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();

            if (rs.next())
                id_det = rs.getInt(1);

        } catch (SQLException sqle) {
            logger.error("SQL1 EXCEPTION =" + sqle);
            sqle.printStackTrace();
            throw new PedidosDAOException(String.valueOf(sqle.getErrorCode()), sqle);
        } catch (Exception e) {
            logger.error("SQL2 EXCEPTION =" + e);
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getCause()), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        return id_det;
    }
    
    public boolean addPromoDetallePedido(PromoDetallePedidoDTO dto) throws PedidosDAOException {
        boolean result = false;
        PreparedStatement stm = null;

        try {
            conn = this.getConnection();
            logger.debug("en addPromoDetallePedido:");
            String sql = "INSERT INTO bo_promos_detped (id_detalle, id_promocion, id_producto, id_pedido, promo_codigo, promo_descr, promo_tipo, promo_fini, promo_ffin, dscto_porc ) " +
            		"VALUES (?,?,?,?,?,?,?,?,?,?) ";

            logger.debug(sql);
            logger.debug("valores: " + dto.getId_detalle() + "," + dto.getId_promocion() + "," + dto.getId_producto() + "," + dto.getId_pedido() + ","
                    + dto.getPromo_codigo() + "," + dto.getPromo_desc() + "," + dto.getPromo_tipo() + "," + dto.getPromo_fini() + "," + dto.getPromo_ffin()+ ","+dto.getPromo_dscto_porc());
            stm = conn.prepareStatement(sql);
            stm.setLong(1, dto.getId_detalle());
            stm.setLong(2, dto.getId_promocion());
            stm.setLong(3, dto.getId_producto());
            stm.setLong(4, dto.getId_pedido());
            stm.setLong(5, dto.getPromo_codigo());
            stm.setString(6, dto.getPromo_desc());
            stm.setLong(7, dto.getPromo_tipo());
            stm.setString(8, dto.getPromo_fini());
            stm.setString(9, dto.getPromo_ffin());
            stm.setDouble(10, dto.getPromo_dscto_porc());

            int i = stm.executeUpdate();
            logger.debug("inserto?" + i);
            if (i == 1)
                result = true;

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } finally {
            try {

                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }

        return result;
    }
    
    public int setTcpPedido(TcpPedidoDTO tcp) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int result = 0;

        logger.debug("En setTcpPedido...");

        try {

            String Sql = " INSERT INTO BO_TCP (id_pedido, nro_tcp, cant_max, cant_util) " + " VALUES(?,?,?,?) ";

            logger.debug("SQL :" + Sql);
            logger.debug("DTO:" + tcp.toString());

            conn = this.getConnection();
            stm = conn.prepareStatement(Sql, Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, tcp.getId_pedido());
            stm.setInt(2, tcp.getNro_tcp());
            stm.setInt(3, tcp.getCant_max());
            stm.setInt(4, tcp.getCant_util());

            int i = stm.executeUpdate();
            if (i > 0) {
                rs = stm.getGeneratedKeys();

                if (rs.next())
                    result = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PedidosDAOException(e);
        } finally {
            try {

            	if (rs != null)
                    rs.close();
            	if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        logger.debug("id_tcp : " + result);
        return result;
    }
    
    public int setCuponPedido(CuponPedidoDTO cupon) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int result = 0;

        logger.debug("En setCuponPedido...");

        try {

            String Sql = " INSERT INTO BO_CUPON (id_pedido, id_tcp, nro_cupon ) " + " VALUES(?,?,?) ";

            logger.debug("SQL :" + Sql);
            logger.debug("DTO:" + cupon.toString());

            conn = this.getConnection();
            stm = conn.prepareStatement(Sql, Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, cupon.getId_pedido());
            stm.setLong(2, cupon.getId_tcp());
            stm.setString(3, cupon.getNro_cupon());

            int i = stm.executeUpdate();
            if (i > 0) {
                rs = stm.getGeneratedKeys();

                if (rs.next())
                    result = rs.getInt(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PedidosDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        logger.debug("id_tcp : " + result);
        return result;
    }
    
    /**
     * Elimina y agrega los datos de una nueva factura.
     * 
     * @param prm
     *            ProcModFacturaDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrario devuelve <i>false </i>.
     * @throws PedidosDAOException
     */
    public boolean setModFactura(ProcModFacturaDTO prm) throws PedidosDAOException {

        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        int i_insert = 0;
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            logger.debug("en setModFactura");

            if (prm.getId_pedido() > 0 || prm.getId_factura() > 0) {

                String sql_delete = "DELETE FROM bo_facturas_ped WHERE id_pedido = ? ";
                logger.debug("Ejecución DAO setModFactura");
                logger.debug("SQL: " + sql_delete);
                logger.debug("id_pedido:" + prm.getId_pedido());
                //logger.debug("id_factura:" + prm.getId_factura());
                stm = conn.prepareStatement(sql_delete);
                stm.setLong(1, prm.getId_pedido());
                //stm.setLong(2, prm.getId_factura());

                int j = stm.executeUpdate();
                logger.debug("j:" + j);
            }
            long llave = 0;
            logger.debug("prm.getTipo_doc():" + prm.getTipo_doc());
            if (prm.getTipo_doc().equals("F")) {
                logger.debug("tratando de hacer el insert");
                String sql_insert = "INSERT INTO bo_facturas_ped (id_pedido,  razon, rut, dv, direccion, fono, giro, ciudad, comuna) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                logger.debug("Ejecución DAO setModFactura");
                logger.debug("SQL: " + sql_insert);
                logger.debug("id_pedido:" + prm.getId_pedido());
                logger.debug("razon:" + prm.getRazon());
                logger.debug("rut:" + prm.getRut());
                logger.debug("dv:" + prm.getDv());
                logger.debug("direccion:" + prm.getDireccion());
                logger.debug("fono:" + prm.getTelefono());
                logger.debug("giro:" + prm.getGiro());
                logger.debug("ciudad:" + prm.getCiudad());
                logger.debug("comuna:" + prm.getComuna());

                stm = conn.prepareStatement(sql_insert, Statement.RETURN_GENERATED_KEYS);

                stm.setLong(1, prm.getId_pedido());
                stm.setString(2, prm.getRazon());
                stm.setLong(3, prm.getRut());
                stm.setString(4, prm.getDv());
                stm.setString(5, prm.getDireccion());
                stm.setString(6, prm.getTelefono());
                stm.setString(7, prm.getGiro());
                stm.setString(8, prm.getCiudad());
                stm.setString(9, prm.getComuna());
                i_insert = stm.executeUpdate();
                logger.debug("Resultado Ejecución: " + i_insert);
                logger.debug("la inserción se realizó");

                rs = stm.getGeneratedKeys();
                if (rs.next())
                    llave = rs.getInt(1);

                if (i_insert > 0) {
                    result = true;
                    //logger.debug("se pudo realizar la actualizacion");
                }/*
                  * else{ logger.debug("no se pudo generar la actualizacion"); }
                  */

            }
            if (prm.getTipo_doc().equals("B") || prm.getTipo_doc().equals("F")) {
                logger.debug("tratando de hacer el update");
                String sql_update = "UPDATE bo_pedidos SET num_doc = ?, tipo_doc = ? WHERE id_pedido = ?";
                logger.debug("Ejecución DAO setModFactura");

                stm = conn.prepareStatement(sql_update);
                if (prm.getTipo_doc().equals("B"))
                    stm.setLong(1, prm.getId_factura());
                else {
                    prm.setId_factura(llave);
                    stm.setLong(1, prm.getId_factura());
                }
                logger.debug("SQL: " + sql_update);
                logger.debug("id_factura:" + prm.getId_factura());
                logger.debug("tipo_doc:" + prm.getTipo_doc());
                logger.debug("id_pedido:" + prm.getId_pedido());
                stm.setString(2, prm.getTipo_doc());
                stm.setLong(3, prm.getId_pedido());
                int i_update = stm.executeUpdate();
                logger.debug("Resultado Ejecución: " + i_update);
                logger.debug("el Update se realizó");

                if (i_update > 0) {
                    result = true;
                    logger.debug("se pudo realizar la actualizacion");
                } else {
                    logger.debug("no se pudo generar la actualizacion");
                }
            }

            /*
             * else{ String sql_delete = "DELETE FROM bo_facturas_ped WHERE
             * id_pedido = ? AND id_factura = ?"; logger.debug("Ejecución DAO
             * setModFactura"); logger.debug("SQL: " + sql_delete);
             * logger.debug("id_pedido:" + prm.getId_pedido());
             * logger.debug("id_factura:" + prm.getId_factura()); stm =
             * con.prepareStatement(sql_delete); stm.setLong(1,
             * prm.getId_pedido()); stm.setLong(2, prm.getId_factura());
             * 
             * int j = stm.executeUpdate();
             * 
             * logger.debug("tratando de hacer el insert"); String sql_insert =
             * "INSERT INTO bo_facturas_ped (id_pedido, id_factura, razon, rut,
             * dv, direccion, fono, giro) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
             * logger.debug("Ejecución DAO setModFactura"); logger.debug("SQL: " +
             * sql_insert); logger.debug("id_pedido:" + prm.getId_pedido());
             * logger.debug("id_factura:" + prm.getId_factura());
             * logger.debug("razon:" + prm.getRazon()); logger.debug("rut:" +
             * prm.getRut()); logger.debug("dv:" + prm.getDv());
             * logger.debug("direccion:" + prm.getDireccion());
             * logger.debug("fono:" + prm.getTelefono()); logger.debug("giro:" +
             * prm.getGiro());
             * 
             * stm = con.prepareStatement( sql_insert );
             * 
             * stm.setLong(1, prm.getId_pedido()); stm.setLong(2,
             * prm.getId_factura()); stm.setString(3, prm.getRazon());
             * stm.setString(4, prm.getRut()); stm.setString(5, prm.getDv());
             * stm.setString(6, prm.getDireccion()); stm.setString(7,
             * prm.getTelefono()); stm.setString(8, prm.getGiro()); int i_insert =
             * stm.executeUpdate(); logger.debug("Resultado Ejecución: " +
             * i_insert); logger.debug("la inserción se realizó");
             *  }
             */

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
            if (e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
            }
            throw new PedidosDAOException(e);
        } finally {
            try {

            	if (rs != null)
                    rs.close();
            	if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : xxx - Problema SQL (close)", e);
            }
        }
        logger.debug("result?" + result);
        return result;
    }
    
	public PrioridadPromosDTO getPromosPrioridadProducto(long id_producto, long id_local) throws PedidosDAOException {

		PreparedStatement stm1 = null;
		PreparedStatement stm2 = null;
		PreparedStatement stm3 = null;

		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		PrioridadPromosDTO resultado = new PrioridadPromosDTO();

		try {		

		    
            conn = this.getConnection();
            
            String q1 = "SELECT prod.cod_promo1 " +
                        "FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "inner join fodba.pr_promocion pro1 on prod.cod_promo1 = pro1.cod_promo " +
                        "WHERE prod.id_producto = ? " +
                        "AND pro1.id_local = ? and prod.id_local = ? " +
                        "and ( date(pro1.fini) <= current date and date(pro1.ffin) >= current date )";
            stm1 = conn.prepareStatement(q1 + " WITH UR");
            stm1.setLong(1, id_producto);
            stm1.setLong(2, id_local);
            stm1.setLong(3, id_local);
            rs1 = stm1.executeQuery();
            if (rs1.next()) {
                resultado.setCodPromoEvento(rs1.getLong("cod_promo1"));
            }

            
            String q2 = "SELECT prod.cod_promo2 " +
                        "FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "inner join fodba.pr_promocion pro2 on prod.cod_promo2 = pro2.cod_promo " +
                        "WHERE prod.id_producto = ? " +
                        "AND pro2.id_local = ? and prod.id_local = ? " +
                        "and ( date(pro2.fini) <= current date and date(pro2.ffin) >= current date )";
            stm2 = conn.prepareStatement(q2 + " WITH UR");
            stm2.setLong(1, id_producto);
            stm2.setLong(2, id_local);
            stm2.setLong(3, id_local);
            rs2 = stm2.executeQuery();
            if (rs2.next()) {
                resultado.setCodPromoPeriodica(rs2.getLong("cod_promo2"));
            }

            
            String q3 = "SELECT prod.cod_promo3 " +
                        "FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "inner join fodba.pr_promocion pro3 on prod.cod_promo3 = pro3.cod_promo " +
                        "WHERE prod.id_producto = ? " +
                        "AND pro3.id_local = ? and prod.id_local = ? " +
                        "and ( date(pro3.fini) <= current date and date(pro3.ffin) >= current date )";
            stm3 = conn.prepareStatement(q3 + " WITH UR");
            stm3.setLong(1, id_producto);
            stm3.setLong(2, id_local);
            stm3.setLong(3, id_local);
            rs3 = stm3.executeQuery();
            if (rs3.next()) {
                resultado.setCodPromoNormal(rs3.getLong("cod_promo3"));
            }

			
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (stm1 != null)
					stm1.close();
				if (rs2 != null)
					rs2.close();
				if (stm2 != null)
					stm2.close();
				if (rs3 != null)
					rs3.close();
				if (stm3 != null)
					stm3.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return resultado;
	}
	
    public PromoMedioPagoDTO getPromoMedioPagoByMPJmcl(String jm_mp, int jm_mp_cuotas) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PromoMedioPagoDTO result = new PromoMedioPagoDTO();

        
        //logger.debug("en getPromoMedioPagoByJmp:");
        try {
        	conn = this.getConnection();
            String sql = "SELECT mp_promo, mp_jmcl , mp_jmcl_ncuotas " +
            		"FROM pr_medio_pago " +
            		"WHERE mp_jmcl= ? " +
            		"AND mp_jmcl_ncuotas = ? " +
            		"WITH UR";

            stm = conn.prepareStatement(sql);
            stm.setString(1, jm_mp);
            stm.setInt(2, jm_mp_cuotas);
            rs = stm.executeQuery();
            if (rs.next()) {
                result.setMp_jmcl(rs.getString("mp_jmcl"));
                result.setMp_promo(rs.getString("mp_promo"));
                result.setMp_jmcl_ncuotas(rs.getInt("mp_jmcl_ncuotas"));
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        }
        finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return result;
    }
    
    public PromocionDTO getPromocionByCodigo(long cod_promo) throws PedidosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PromocionDTO promocionDto = null;
        logger.debug("en getPromocionByCodigo:");
        
        
        try {
        	conn = this.getConnection();
            String sql = "SELECT ID_PROMOCION, COD_PROMO, p.ID_LOCAL, VERSION, TIPO_PROMO, FINI, FFIN, DESCR, CANT_MIN, MONTO_MIN, MONTO1, DESCUENTO1, MONTO2, DESCUENTO2, MONTO3, DESCUENTO3, MONTO4, DESCUENTO4, MONTO5, DESCUENTO5, FP1, NUM_CUOTA1, TCP1, BENEFICIO1, FP2, NUM_CUOTA2, TCP2, BENEFICIO2, FP3, NUM_CUOTA3, TCP3, BENEFICIO3, CONDICION1, CONDICION2, CONDICION3, PRORRATEO, RECUPERABLE, CANAL, SUSTITUIBLE, BANNER, FALTANTE, NOM_LOCAL " +
            		"FROM PR_PROMOCION p " +
            		"JOIN BO_LOCALES l ON p.id_local = l.id_local " +
            		"WHERE COD_PROMO = ? " +
            		"WITH UR";

            logger.debug("SQL query: " + sql + " codpromo=" + cod_promo);
            
            stm = conn.prepareStatement(sql );
            stm.setLong(1, cod_promo);
            rs = stm.executeQuery();
            if (rs.next()) {
                promocionDto = new PromocionDTO();
                promocionDto.setId_promocion(rs.getLong("ID_PROMOCION"));
                promocionDto.setCod_promo(rs.getLong("COD_PROMO"));
                promocionDto.setId_local(rs.getLong("ID_LOCAL"));
                promocionDto.setVersion(rs.getLong("VERSION"));
                promocionDto.setTipo_promo(rs.getLong("TIPO_PROMO"));
                promocionDto.setFini(rs.getString("FINI"));
                promocionDto.setFfin(rs.getString("FFIN"));
                promocionDto.setDescr(rs.getString("DESCR"));
                promocionDto.setCant_min(rs.getLong("CANT_MIN"));
                promocionDto.setMonto_min(rs.getLong("MONTO_MIN"));

                promocionDto.setMonto1(rs.getLong("MONTO1"));
                promocionDto.setDescuento1(rs.getDouble("DESCUENTO1"));
                promocionDto.setMonto2(rs.getLong("MONTO2"));
                promocionDto.setDescuento2(rs.getDouble("DESCUENTO2"));
                promocionDto.setMonto3(rs.getLong("MONTO3"));
                promocionDto.setDescuento3(rs.getDouble("DESCUENTO3"));
                promocionDto.setMonto4(rs.getLong("MONTO4"));
                promocionDto.setDescuento4(rs.getDouble("DESCUENTO4"));
                promocionDto.setMonto5(rs.getLong("MONTO5"));
                promocionDto.setDescuento5(rs.getDouble("DESCUENTO5"));

                promocionDto.setFp1(rs.getLong("FP1"));
                promocionDto.setNum_cuota1(rs.getLong("NUM_CUOTA1"));
                promocionDto.setTcp1(rs.getLong("TCP1"));
                promocionDto.setBeneficio1(rs.getDouble("BENEFICIO1"));

                promocionDto.setFp2(rs.getLong("FP2"));
                promocionDto.setNum_cuota2(rs.getLong("NUM_CUOTA2"));
                promocionDto.setTcp2(rs.getLong("TCP2"));
                promocionDto.setBeneficio2(rs.getDouble("BENEFICIO2"));

                promocionDto.setFp3(rs.getLong("FP3"));
                promocionDto.setNum_cuota3(rs.getLong("NUM_CUOTA3"));
                promocionDto.setTcp3(rs.getLong("TCP3"));
                promocionDto.setBeneficio3(rs.getDouble("BENEFICIO3"));

                promocionDto.setCondicion1(rs.getLong("CONDICION1"));
                promocionDto.setCondicion2(rs.getLong("CONDICION2"));
                promocionDto.setCondicion3(rs.getLong("CONDICION3"));
                promocionDto.setProrrateo(rs.getLong("PRORRATEO"));
                promocionDto.setRecuperable(rs.getLong("RECUPERABLE"));
                promocionDto.setCanal(rs.getLong("CANAL"));
                promocionDto.setSustituible(rs.getString("SUSTITUIBLE"));
                promocionDto.setBanner(rs.getString("BANNER"));
                promocionDto.setFaltante(rs.getString("FALTANTE"));
                promocionDto.setNom_local(rs.getString("NOM_LOCAL"));
            }

        } catch (SQLException e) {
            throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
        }
        finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
        return promocionDto;
    }

    
    /**
     * 
     * @param idPedido
     * @return
     * @throws PedidosDAOException
     */
       public PedidoDTO getValidaCuponYPromocionPorIdPedido( long idPedido ) throws PedidosDAOException {
       	
    	   PedidoDTO pedido = new PedidoDTO();
           
    	   pedido.setPromocion( getValidaPromocionPorIdPedido( idPedido ) );
    	   
    	   PreparedStatement stm = null;
           ResultSet rs = null;

           try {
           	
               conn = this.getConnection();
               logger.debug("en getValidaCuponYPromocionPorIdPedido:");
               
//               String sql =	 "SELECT ID_CUPON "+
//	   						 "FROM BODBA.BO_CUPON_DSCTOXPEDIDO  cdp " +
//	   						 "inner join BO_CUPON_DSCTO on cd "+
//	   						 "WHERE ID_PEDIDO = ? WITH UR";
               
              // String sql = GET_COD_CUPON;
               
               //TODO aqui va la query

               logger.debug( GET_COD_CUPON );
               
               stm = conn.prepareStatement( GET_COD_CUPON );
               stm.setLong( 1, idPedido );
              
               rs = stm.executeQuery();
               
        	   if ( rs.next() ) {
               	
               	   if( rs.getString( 1 ) != null ) {
               	
               		   pedido.setCupon( true );
               		   pedido.setCodigoCupon(rs.getString(1));
               	
               	   }
               	
               }	
               
           } catch ( SQLException e ) {
           	
        	   throw new PedidosDAOException( String.valueOf( e.getErrorCode() ), e );
           
           }finally {
           	
        	   try {
                   
        		   if ( rs != null )
      					rs.close();
        		   
        		   if ( stm != null )
                       stm.close();
                   
        		   releaseConnection();
               
        	   } catch ( SQLException e ) {
               	
                   logger.error( "[Metodo] : xxx - Problema SQL (close)", e );
               
               }
        	   
           }

           return pedido;
       
       }
       public int getIdCuponByIdPedido( long idPedido ) throws PedidosDAOException {
     	   PreparedStatement stm = null;
           ResultSet rs = null;
           int idCupon = 0;
           try {
               conn = this.getConnection();
               logger.debug("en getIdCuponByIdPedido:");
               logger.debug( GET_COD_CUPON );
               stm = conn.prepareStatement( GET_COD_CUPON );
               stm.setLong( 1, idPedido );
               rs = stm.executeQuery();
        	   if ( rs.next() ) {
               	   if( rs.getString( 1 ) != null ) {
               		   idCupon=rs.getInt(1);
               	   }
               }	
           } catch ( SQLException e ) {
        	   throw new PedidosDAOException( String.valueOf( e.getErrorCode() ), e );
           }finally {
        	   try {
        		   if ( rs != null )
      					rs.close();
        		   if ( stm != null )
                       stm.close();
        		   releaseConnection();
        	   } catch ( SQLException e ) {
                   logger.error( "[Metodo] : xxx - Problema SQL (close)", e );
               }
           }
           return idCupon;
       }
       
       /**
        * 
        * @param idPedido
        * @return
        * @throws PedidosDAOException
        */
          public boolean getValidaPromocionPorIdPedido( long idPedido ) throws PedidosDAOException {
          	
        	  boolean promocion = false;
              PreparedStatement stm = null;
              ResultSet rs = null;

              try {
              	
                  conn = this.getConnection();
                  logger.debug( "en getValidaPromocionPorIdPedido:" );
                  
                  String sql =	"SELECT ID_PEDIDO "+
   	   						 	"FROM BODBA.BO_PROMOS_DETPED "+
   	   						 	"WHERE ID_PEDIDO = ? "+
   	   						 	"GROUP BY ID_PEDIDO WITH UR";

                  logger.debug( sql );
                  
                  stm = conn.prepareStatement( sql );
                  stm.setLong( 1, idPedido );
                 
                  rs = stm.executeQuery();
                  
                  if ( rs.next() ) {
                  	
                  	    if ( rs.getString( 1 ) != null ) {
                  		
                  		    promocion = true;
                  	
                  	   }
                  	
                  }	
                  
              } catch ( SQLException e ) {
              	
           	   throw new PedidosDAOException( String.valueOf( e.getErrorCode() ), e );
              
              }finally {
              	
           	   try {
                      
           		   if ( rs != null )
         					rs.close();
           		   
           		   if ( stm != null )
                          stm.close();
                      
           		   releaseConnection();
                  
           	   } catch ( SQLException e ) {
                  	
                      logger.error( "[Metodo] : xxx - Problema SQL (close)", e );
                  
                  }
           	   
              }

              return promocion;
          
          }
        
       /**
        * 
        * @param criterio
        * @return
        * @throws PedidosDAOException
        */
       public List getTiposDescuentosAplicados( long id_pedido ) throws PedidosDAOException {
   		
   		PreparedStatement stm = null;
   		ResultSet rs = null;
   		DetallePedidoDTO detallePedido = null;
   		List listDetallePedido = new ArrayList();
   		//String descProducto = "";
   		
   		logger.debug( "-----------------------------------------------------------------" );
   		logger.debug( "Ejecución DAO: " + getClass().getName()+"getTiposDescuentosAplicados" );
   		logger.debug( "-----------------------------------------------------------------" );
   		
   		logger.debug( "Parametros getTiposDescuentosAplicados:" );
   		logger.debug( "numero_pedido:" +id_pedido );

   		/*String Sql = "SELECT pd.PROMO_CODIGO, pd.PROMO_DESCR, SUM((dp.CANT_SOLIC * dp.PRECIO_LISTA) - (dp.CANT_SOLIC * dp.PRECIO)), fp.PRO_TIPO_PRODUCTO, fp.PRO_DES_CORTA, fm.MAR_NOMBRE "+ 
					 "FROM BO_DETALLE_PEDIDO dp "+
					 "INNER JOIN BO_PROMOS_DETPED pd ON dp.ID_DETALLE = pd.ID_DETALLE "+
					 "INNER JOIN FO_PRODUCTOS fp on fp.PRO_ID_BO = dp.ID_PRODUCTO "+
                     "INNER JOIN FO_MARCAS fm on fm.MAR_ID = fp.PRO_MAR_ID "+
					 "WHERE pd.ID_PEDIDO = ? "+ 
					 "GROUP BY pd.PROMO_CODIGO, pd.PROMO_DESCR, fp.PRO_TIPO_PRODUCTO, fp.PRO_DES_CORTA, fm.MAR_NOMBRE ";*/
   					
   		
   		String Sql = "SELECT pd.PROMO_CODIGO, pd.PROMO_DESCR, SUM((dp.CANT_SOLIC * dp.PRECIO_LISTA) - (dp.CANT_SOLIC * dp.PRECIO)) "+
					 "FROM BO_DETALLE_PEDIDO dp "+
					 "INNER JOIN BO_PROMOS_DETPED pd ON dp.ID_DETALLE = pd.ID_DETALLE "+
					 "INNER JOIN FO_PRODUCTOS fp on fp.PRO_ID_BO = dp.ID_PRODUCTO "+
                     "INNER JOIN FO_MARCAS fm on fm.MAR_ID = fp.PRO_MAR_ID "+
					 "WHERE pd.ID_PEDIDO = ? "+
					 "GROUP BY pd.PROMO_CODIGO, pd.PROMO_DESCR ";
   		
   		logger.debug( "SQL :" +Sql );
   		
   		try {
   			
   			conn = this.getConnection();
   			stm = conn.prepareStatement( Sql + " WITH UR" );
   			stm.setLong( 1, id_pedido );
   			
   			rs = stm.executeQuery();
   			
   			while ( rs.next() ) {
   				
   				if( rs.getString( 1 ) != null ) {
   				
   					detallePedido = new DetallePedidoDTO();
   	   				
   					/*descProducto = rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6);
   		            
   					if (descProducto.length() > 255) {
   		             
   						descProducto = descProducto.substring(0, 255);
   		            
   					}*/
   					
   					detallePedido.setCodPromo( rs.getLong( 1 ) );
   					detallePedido.setDescripcion( rs.getString( 2 ) ); // detallePedido.setDescripcion( descProducto + "<br>" + rs.getString( 2 ) );
   	   				detallePedido.setPrecio( rs.getDouble( 3 ) );
   	   			
   	   				listDetallePedido.add( detallePedido );
   	   				
   				}
   				
   			}
   			
   			DetallePedidoDTO detalleDespacho = getTipoDespacho( id_pedido );
   			
   			if( detalleDespacho != null )
   				listDetallePedido.add( detalleDespacho );
   			
   		}catch ( SQLException e ) {
   			
   			throw new PedidosDAOException( String.valueOf( e.getErrorCode() ), e );
   		
   		} finally {
   			
   			try {
   				
   				if (rs != null)
   					rs.close();
   				
   				if (stm != null) 
   					stm.close();
   				
   				releaseConnection();
   			
   			} catch ( SQLException e ) {
   				
   				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
   			
   			}
   		
   		}
   		
   		return listDetallePedido;
   	
       }
       
       
       /**
        * 
        * @param idDetalle
        * @return
        * @throws PedidosDAOException
        */
       public DetallePedidoDTO getTipoDespacho( long id_pedido ) throws PedidosDAOException {
   		
   		PreparedStatement stm = null;
   		ResultSet rs = null;
   		DetallePedidoDTO detallePedido = null;
   		
   		
   		logger.debug( "-----------------------------------------------------------------" );
   		logger.debug( "Ejecución DAO: " + getClass().getName()+"getTipoDespacho" );
   		logger.debug( "-----------------------------------------------------------------" );
   		logger.debug( "Parametros getTipoDespacho:" );
   		logger.debug( "numero_pedido:" +id_pedido );

   		String Sql =  "SELECT p.COSTO_DESPACHO, fc.COL_RUT, cp.ID_CUPON "+ 
 				  "FROM BO_PEDIDOS p "+
 				  "LEFT JOIN FO_COLABORADOR fc ON fc.COL_RUT = p.RUT_CLIENTE "+
 				  "LEFT JOIN BODBA.BO_CUPON_DSCTOXPEDIDO cp on cp.ID_PEDIDO = p.ID_PEDIDO "+
 				  "WHERE p.ID_PEDIDO = ? "+
 				  "GROUP BY p.COSTO_DESPACHO, fc.COL_RUT, cp.ID_CUPON ";
	   					
   		logger.debug( "SQL :" +Sql );
   		
   		try {
   			
   			conn = this.getConnection();
   			stm = conn.prepareStatement( Sql + " WITH UR" );
   			stm.setLong( 1, id_pedido );
   			
   			rs = stm.executeQuery();
   			
   			if ( rs.next() ) {
   			
   				if( rs.getLong( 1 ) == 1 && rs.getString( 2 ) == null && rs.getString(3) != null ) {
   				
   					detallePedido = new  DetallePedidoDTO();
   					
   					detallePedido.setCodPromo( -2 );
   					detallePedido.setDescripcion( Constantes.TEXT_DESCTO_DESPACHO );
   					
   				} else if( rs.getLong( 1 ) == 1 && rs.getString( 2 ) != null) {
   					
   					detallePedido = new  DetallePedidoDTO();
   					
   					detallePedido.setCodPromo( -1 );
   					detallePedido.setDescripcion( Constantes.TEXT_DESCTO_DESPACHO );
   					
   				}else if( rs.getLong( 1 ) == 1 ) {
   					
   					detallePedido = new  DetallePedidoDTO();
   					
   					detallePedido.setDescripcion( Constantes.TEXT_DESCTO_DESPACHO );
   					
   				}
   				
   			}
   			
   		}catch ( SQLException e ) {
   			
   			throw new PedidosDAOException( String.valueOf( e.getErrorCode() ), e );
   		
   		} finally {
   			
   			try {
   				
   				if ( rs != null )
   					rs.close();
   				
   				if (stm != null) 
   					stm.close();
   				
   				releaseConnection();
   			
   			} catch ( SQLException e ) {
   				
   				logger.error( "[Metodo] : xxx - Problema SQL (close)", e );
   			
   			}
   			
   		}
   		
   		return detallePedido;
   	
       }
       
       /**(Catalogo Externo) - NSepulveda
        * Metodo que se encarga de recuperar la categoria padre, intermedia y terminal del ultimo producto 
        * agregado al carro de compras.
        * @param idProducto <code>long<code> Identificador del producto.
        * @return <code>ProductoCatalogoExternoDTO</code>
        * @throws PedidosDAOException
        */
       public ProductoCatalogoExternoDTO getCatUltimoProductoCatalogoExterno(long idProducto) throws PedidosDAOException {
       	logger.debug("Se ingresa al metodo getCatUltimoProductoCatalogoExterno.");
       	PreparedStatement stm = null;
           ResultSet rs = null;
           ProductoCatalogoExternoDTO categoriaProducto = null;
           String sqlQuery = "";
           
           try{
           	logger.debug("Recupero la conexion.");
               conn = this.getConnection();
               
               sqlQuery = "SELECT S.CAT_ID AS CABECERA, CAT.INTER AS INTERMEDIA, CAT.TER AS TERMINAL "+
               		   "FROM( "+
               		   		"SELECT C.CAT_ID AS ter, S.CAT_ID AS inter "+
               		   		"FROM FODBA.FO_CATEGORIAS C, FODBA.FO_CATSUBCAT S, FODBA.FO_PRODUCTOS P, FODBA.FO_PRODUCTOS_CATEGORIAS PC "+
               		   		"WHERE C.CAT_ID = S.SUBCAT_ID "+
               		   		"AND PC.PRCA_CAT_ID = C.CAT_ID "+
               		   		"AND P.PRO_ID = PC.PRCA_PRO_ID "+
               		   		"AND P.PRO_ID = ? AND C.CAT_ESTADO='A' AND C.CAT_TIPO='T') CAT, FODBA.FO_CATSUBCAT S "+
               		   "WHERE CAT.INTER = S.SUBCAT_ID "+
               		   "FETCH FIRST 1 ROWS ONLY";
               
               stm = conn.prepareStatement( sqlQuery + " WITH UR" );
               stm.setLong(1, idProducto);
           	rs = stm.executeQuery();
           	
           	if(rs.next()){
           		categoriaProducto = new ProductoCatalogoExternoDTO();
           		categoriaProducto.setCatPadre(Long.parseLong(rs.getString("CABECERA")));
           		categoriaProducto.setCatIntermedia(Long.parseLong(rs.getString("INTERMEDIA")));
           		categoriaProducto.setCatTerminal(Long.parseLong(rs.getString("TERMINAL")));
           	}
           	
           	logger.debug("Cierro las conexiones.");
           	
           }catch (Exception e) {
               logger.debug("Problema:"+ e);
               throw new PedidosDAOException(e);
   		} finally {
   			try {
   				if (rs != null)
   					rs.close();
   				if (stm != null)
   					stm.close();
   				releaseConnection();
   			} catch (SQLException e) {
   				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
   			}
   		}
           
           return categoriaProducto;
       }
       
       /**(Catalogo Externo) - NSepulveda
        * Metodo que se encarga de validar los productos solicitados desde el catalogo externo 
        * para posteriormente agregarlos al carro de compras.
        * @param listCatalogoExt <code>List</code> Listado de productos a validar.
        * @return <code>List</code> Listado de <code>ProductoCatalogoExternoDTO</code> con los productos validados.
        * @throws PedidosDAOException
        */
       public List getValidacionProductosCatalogoExterno(List listCatalogoExt) throws PedidosDAOException {
       	logger.debug("Se ingresa al metodo getValidacionProductosCatalogoExterno.");
       	ResultSet rsLocal = null;
       	ResultSet rsStock = null;
       	ResultSet rsNombreProd = null;
       	PreparedStatement stmLocal = null;
       	PreparedStatement stmStock = null;
       	PreparedStatement stmNombreProd = null;
           ProductoCatalogoExternoDTO productoCatalogoExt = null;
           
           try{
           	logger.debug("Recupero la conexion.");
               conn = this.getConnection();
               
               for (int i = 0; i < listCatalogoExt.size(); i++) {
               	productoCatalogoExt = (ProductoCatalogoExternoDTO) listCatalogoExt.get(i);
               	productoCatalogoExt.setTieneStock(false);
               	productoCatalogoExt.setCantidadMaxima(0);
               	productoCatalogoExt.setAgregarCarro(false);
               	productoCatalogoExt.setMensajeSistemaCantMax("Cantidad de productos fuera de rango.");
               	
               	//Validar si el Producto esta disponible en el Local.
               	logger.debug("Se valida si el producto ["+productoCatalogoExt.getId_producto_catalogo()+"] esta disponible en el local.");
               	
               	String sqlLocal = 	"SELECT pro_id " +
   		                        	"FROM fodba.fo_productos " +
   		                        	"inner join fo_precios_locales on pre_pro_id = pro_id " +
   		                        	"where pro_id = ? "+
   		                        	"and pre_loc_id = ? " +
   		                        	"and pro_estado = 'A' " +
   		                        	"and pre_estado = 'A'";
               	
               	stmLocal = conn.prepareStatement( sqlLocal + " WITH UR" );
               	stmLocal.setLong(1, productoCatalogoExt.getLongIdProducto());
               	stmLocal.setLong(2, productoCatalogoExt.getLongIdLocal());
               	rsLocal = stmLocal.executeQuery();
   				
   				if (rsLocal.next()) { //Existe producto
   					productoCatalogoExt.setMensajeSistemaLocal("OK");
   				}else{
   					productoCatalogoExt.setMensajeSistemaLocal("Producto no se encuentra disponible en la Comuna de ");
   				}
   				
   				//Validar si el Producto tiene Stock en el local.
   				logger.debug("Se valida si el producto ["+productoCatalogoExt.getId_producto_catalogo()+"] tiene stock en el local.");
   				
   				String sqlStock = "SELECT pro_id, pre_tienestock, pro_inter_max " +
   			                     "FROM fodba.fo_productos " +
   			                     "inner join fo_precios_locales on pre_pro_id = pro_id " +
   			                     "where pro_id = ? " +
   			                     "and pre_loc_id = ? " +
   			                     "and pro_estado = 'A' " +
   			                     "and pre_estado = 'A'";
   				
   				stmStock = conn.prepareStatement( sqlStock + " WITH UR" );
   				stmStock.setLong(1, productoCatalogoExt.getLongIdProducto());
   				stmStock.setLong(2, productoCatalogoExt.getLongIdLocal());
   				rsStock = stmStock.executeQuery();
   				
   				if(rsStock.next()){
   	            	if(rsStock.getInt("pre_tienestock") > 0) {
   	            		productoCatalogoExt.setTieneStock(true);
   	            		productoCatalogoExt.setCantidadMaxima(rsStock.getLong("pro_inter_max"));
   	            		productoCatalogoExt.setMensajeSistemaStockLocal("OK");
   	            		
   	            		/*Validar que la cantidad de productos del catalogo externo solicitada
   	            		 * no sea mayor a la cantidad maxima definida. */
   	            		logger.debug("Se valida que la cantidad del producto ["+productoCatalogoExt.getId_producto_catalogo()+"] "+
   	            						"solicitado no sea mayor a la cantidad maxima permitida.");
   	            		
   	            		double dcantMax = (double) productoCatalogoExt.getCantidadMaxima();
   	            		
   	            		if(productoCatalogoExt.getCantidad_producto_catalogo() > dcantMax){
   	            			productoCatalogoExt.setMensajeSistemaCantMax("Solo se pueden agregar hasta "+
   	            					productoCatalogoExt.getCantidadMaxima()+" productos.");
   	            		}else{
   	            			productoCatalogoExt.setAgregarCarro(true);
   	            			productoCatalogoExt.setMensajeSistemaCantMax("OK");
   	            		}
   	            	}else{
   	            		productoCatalogoExt.setMensajeSistemaStockLocal("Producto no tiene stock en la Comuna de ");
   	            	}
   	            }else{
   	            	productoCatalogoExt.setMensajeSistemaStockLocal("Producto no tiene stock en la Comuna de ");
   	            }
   				
   				//Se obtiene el nombre del producto desde la base de datos 'FODBA.FO_PRODUCTOS'.
   				logger.debug("Se obtiene el nombre del producto ["+productoCatalogoExt.getId_producto_catalogo()+"] desde la base de datos.");
   				
   				String sqlNamePROD = "SELECT PR.PRO_ID AS ID_PRODUCTO, "+
   									 "PR.PRO_TIPO_PRODUCTO AS NOMBRE_PRODUCTO, "+
   									 "MA.MAR_NOMBRE AS MARCA_PRODUCTO "+
   			                         "FROM FODBA.FO_PRODUCTOS PR " +
   									 "INNER JOIN FODBA.FO_MARCAS MA ON (PR.PRO_MAR_ID = MA.MAR_ID) "+
   			                         "WHERE PR.PRO_ID = ?";
   	
   				stmNombreProd = conn.prepareStatement( sqlNamePROD + " WITH UR" );
   				stmNombreProd.setLong(1, productoCatalogoExt.getLongIdProducto());
   				rsNombreProd = stmNombreProd.executeQuery();
   				
   				if (rsNombreProd.next()){ //Existe producto
   					logger.debug("[Obtiene Nombre Producto] Existe producto ID=["+productoCatalogoExt.getLongIdProducto()+"] en la base de datos.");
   					productoCatalogoExt.setExisteProducto(1);
   					productoCatalogoExt.setNombre_producto_catalogo(rsNombreProd.getString("NOMBRE_PRODUCTO"));
   					productoCatalogoExt.setMarca_producto(rsNombreProd.getString("MARCA_PRODUCTO"));
   				}else{
   					logger.debug("[Obtiene Nombre Producto] No existe producto ID=["+productoCatalogoExt.getLongIdProducto()+"] en la base de datos.");
   					productoCatalogoExt.setExisteProducto(0);
   				}
   				
   				listCatalogoExt.set(i, productoCatalogoExt);
               }
               
               logger.debug("Cierro las conexiones.");
           	
           }catch (Exception e) {
               logger.debug("Problema:"+ e);
               throw new PedidosDAOException(e);
   		} finally {
   			try {
   				if (rsLocal != null)
   					rsLocal.close();
   				if (rsStock != null)
   					rsStock.close();
   				if (rsNombreProd != null)
   					rsNombreProd.close();
   				if (stmLocal != null)
   					stmLocal.close();
   				if (stmStock != null)
   					stmStock.close();
   				if (stmNombreProd != null)
   					stmNombreProd.close();
   				releaseConnection();
   			} catch (SQLException e) {
   				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
   			}
   		}
           
           logger.debug("Se retornan los productos validados para agregar al carro de compras.");
           
           return listCatalogoExt;
       }
	   
	   /**
      * Cambia el idCliente al pedido (solo para clientes que compraron como invitados y se quieren que registrar)
      * 
       * @param  String idCliente, int pedido
      * @return void
      * @throws PedidosDAOException 
       * 
       */
      public void setIdClienteIntoPedido(long idCliente, long pedido) throws PedidosDAOException{
            //update bo_pedidos set id_cliente=?, dv_cliente=? where id_pedido=?
            PreparedStatement stm=null;
            
            try{
                  conn=this.getConnection();
                  String sql = "UPDATE BODBA.BO_PEDIDOS "
                             + "SET ID_CLIENTE=? "                          
                             + "WHERE ID_PEDIDO=?";
                  stm=conn.prepareStatement(sql);
                  
                  stm.setLong(1, idCliente);               
                  stm.setLong(2, pedido);
                  stm.executeUpdate();
            }catch (SQLException e) {
                  throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
            } finally {
                  if (stm != null)
                        try {
                             stm.close();
                        } catch (SQLException e) {
                             logger.error("[Metodo] : setRutIntoPedido - Problema SQL (close)", e);
                        }
            }
      }
      
      /**
    	 * Actualiza las jornadas de picking y de despacho al pedido
    	 * 
    	 * @param  id_pedido long 
    	 * @param  id_jpicking long 
    	 * @param  id_jdespacho long 
    	 * @param  costo_despacho int 
    	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
    	 * @throws PedidosDAOException
    	 * 
    	 */
    	
    	public boolean doActualizaPedidoJornadasLocalRetiroxRetiro(PedidoDTO oPedido, 
    			JorDespachoCalDTO oJorDespachoNuevaCalDTO, long idJPicking, LocalDTO localDtoNuevo, int costoDespachoNuevo,long idComuna)
    		throws PedidosDAOException {		
    		
    		PreparedStatement stm=null;
    		boolean result=false;
    		
    		//ID_LOCAL, ID_JDESPACHO, ID_JPICKING, ID_COMUNA, ID_ZONA, INDICACION, DIR_CALLE(direccion local)
    		String sql =  " UPDATE bo_pedidos  SET id_local=?, id_jdespacho=?, id_jpicking=?, id_comuna=?, " +
    				"id_zona=?, indicacion=?, dir_calle=?, costo_despacho=?, id_local_fact=? WHERE id_pedido = ?";
    		
    		try {

    			conn = this.getConnection();
    			stm = conn.prepareStatement( sql );
  			stm.setLong(1, localDtoNuevo.getId_local());
  			stm.setLong(2, oJorDespachoNuevaCalDTO.getId_jdespacho());
  			stm.setLong(3, idJPicking);
  			stm.setLong(4, idComuna);
  			stm.setLong(5, localDtoNuevo.getIdZonaRetiro());
  			stm.setString(6, "Retira en Local "+ localDtoNuevo.getNom_local());
  			stm.setString(7, localDtoNuevo.getDireccion());
  			stm.setLong(8, costoDespachoNuevo);
  			stm.setLong(9, localDtoNuevo.getId_local());
  			stm.setLong(10, oPedido.getId_pedido());
  			
    			int i = stm.executeUpdate();
    			if ( i > 0 ) {
    				result= true;
    			}

    		} catch (SQLException e) {
    			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				releaseConnection();
    			} catch (SQLException e) {
    				logger.error("[Metodo] : doActualizaPedidoJornadasLocalRetiroxRetiro - Problema SQL (close)", e);
    			}
    		}
    		return result;
    	}
    	
    	public boolean doActualizaPedidoJornadasLocalRetiroxDomicilio(PedidoDTO oPedido, 
    			JorDespachoCalDTO oJorDespachoNuevaCalDTO, long idJPicking, LocalDTO localDtoNuevo, int costoDespachoNuevo, DireccionEntity oDireccion)
    		throws PedidosDAOException {		
    		
    		PreparedStatement stm=null;
    		boolean result=false;
    		
    		String sql =  " UPDATE bo_pedidos  SET id_local=?, id_jdespacho=?, id_jpicking=?, id_comuna=?," +
    				" id_zona=?, costo_despacho=?, id_local_fact=?, dir_id=?, dir_tipo_calle=?, dir_calle=?, " +
    				" dir_numero=?, dir_depto=?, tipo_despacho=?  WHERE id_pedido = ? ";
    		
    		try {

    			conn = this.getConnection();
    			stm = conn.prepareStatement( sql );
  			stm.setLong(1, localDtoNuevo.getId_local());
  			stm.setLong(2, oJorDespachoNuevaCalDTO.getId_jdespacho());
  			stm.setLong(3, idJPicking);
  			stm.setLong(4, oDireccion.getCom_id().longValue());
  			stm.setLong(5, oDireccion.getZon_id().longValue());
  			stm.setLong(6, costoDespachoNuevo);
  			stm.setLong(7, localDtoNuevo.getId_local());			
  			stm.setLong(8, oDireccion.getId().longValue());			
  			stm.setString(9, oDireccion.getNom_tip_calle());
  			stm.setString(10, oDireccion.getCalle());
  			stm.setString(11, oDireccion.getNumero());
  			stm.setString(12, oDireccion.getDepto());	
  			stm.setString(13, "N");
  			stm.setLong(14, oPedido.getId_pedido());
  			
    			int i = stm.executeUpdate();
    			if ( i > 0 ) {
    				result= true;
    			}

    		} catch (SQLException e) {
    			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				releaseConnection();
    			} catch (SQLException e) {
    				logger.error("[Metodo] : doActualizaPedidoJornadasLocalRetiroxDomicilio - Problema SQL (close)", e);
    			}
    		}
    		return result;
    	}
    	
    	public boolean doActualizaPedidoJornadasLocalDomicilioxRetiro(PedidoDTO oPedido, 
    			JorDespachoCalDTO oJorDespachoNuevaCalDTO, long idJPicking, LocalDTO localDtoNuevo, int costoDespachoNuevo,long idComuna)
    		throws PedidosDAOException {		
    		
    		PreparedStatement stm=null;
    		boolean result=false;
    		
    		//ID_LOCAL, ID_JDESPACHO, ID_JPICKING, ID_COMUNA, ID_ZONA, INDICACION, DIR_CALLE(direccion local)
    		String sql =  " UPDATE bo_pedidos  SET id_local=?, id_jdespacho=?, id_jpicking=?, id_comuna=?, " +
    				"id_zona=?, indicacion=?, dir_calle=?, costo_despacho=?, id_local_fact=?, dir_id=?, dir_tipo_calle=?, " +
    				" dir_numero=?, dir_depto=?, tipo_despacho=? WHERE id_pedido = ?";
    		
    		try {

    			conn = this.getConnection();
    			stm = conn.prepareStatement( sql );
  			stm.setLong(1, localDtoNuevo.getId_local());
  			stm.setLong(2, oJorDespachoNuevaCalDTO.getId_jdespacho());
  			stm.setLong(3, idJPicking);
  			stm.setLong(4, idComuna);
  			stm.setLong(5, localDtoNuevo.getIdZonaRetiro());
  			stm.setString(6, "Retira en Local "+ localDtoNuevo.getNom_local());
  			stm.setString(7, localDtoNuevo.getDireccion());
  			stm.setLong(8, costoDespachoNuevo);
  			stm.setLong(9, localDtoNuevo.getId_local());
  			
  			stm.setLong(10,java.sql.Types.NULL);	
  			stm.setString(11, "");
  			stm.setString(12, "");
  			stm.setString(13, "");
  			stm.setString(14, "R");	
  			stm.setLong(15, oPedido.getId_pedido());
  			
    			int i = stm.executeUpdate();
    			if ( i > 0 ) {
    				result= true;
    			}

    		} catch (SQLException e) {
    			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				releaseConnection();
    			} catch (SQLException e) {
    				logger.error("[Metodo] : doActualizaPedidoJornadasLocalRetiroxRetiro - Problema SQL (close)", e);
    			}
    		}
    		return result;
    	}
    	
    	public boolean doActualizaPedidoJornadasDomicilioxDomicilio(PedidoDTO oPedido, 
    			JorDespachoCalDTO oJorDespachoNuevaCalDTO, long idJPicking, LocalDTO localDtoNuevo, int costoDespachoNuevo, DireccionEntity oDireccion)
    		throws PedidosDAOException {		
    		
    		PreparedStatement stm=null;
    		boolean result=false;
    		
    		String sql =  " UPDATE bo_pedidos  SET id_local=?, id_jdespacho=?, id_jpicking=?, id_comuna=?," +
    				" id_zona=?, costo_despacho=?, id_local_fact=?, dir_id=?, dir_tipo_calle=?, dir_calle=?, " +
    				" dir_numero=?, dir_depto=?  WHERE id_pedido = ? ";
    		
    		try {

    			conn = this.getConnection();
    			stm = conn.prepareStatement( sql );
  			stm.setLong(1, localDtoNuevo.getId_local());
  			stm.setLong(2, oJorDespachoNuevaCalDTO.getId_jdespacho());
  			stm.setLong(3, idJPicking);
  			stm.setLong(4, oDireccion.getCom_id().longValue());
  			stm.setLong(5, oDireccion.getZon_id().longValue());
  			stm.setLong(6, costoDespachoNuevo);
  			stm.setLong(7, localDtoNuevo.getId_local());			
  			stm.setLong(8, oDireccion.getId().longValue());			
  			stm.setString(9, oDireccion.getNom_tip_calle());
  			stm.setString(10, oDireccion.getCalle());
  			stm.setString(11, oDireccion.getNumero());
  			stm.setString(12, oDireccion.getDepto());	
  			stm.setLong(13, oPedido.getId_pedido());
  			
    			int i = stm.executeUpdate();
    			if ( i > 0 ) {
    				result= true;
    			}

    		} catch (SQLException e) {
    			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				releaseConnection();
    			} catch (SQLException e) {
    				logger.error("[Metodo] : doActualizaPedidoJornadasLocalRetiroxDomicilio - Problema SQL (close)", e);
    			}
    		}
    		return result;
    	}
    	
    	/* (sin Javadoc)
    	 * @see cl.bbr.fo.pedidos.dao.PedidosDAO#setCompraHistorica(java.lang.String, long, long, long)
    	 */
    	public void setCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id)
    	throws PedidosDAOException {

    		long llave = 0;
    		Connection conexion    = null;
    		PreparedStatement stm  = null;
    		PreparedStatement stm2 = null;
            PreparedStatement stm3 = null;
    		ResultSet rs = null;
    		
    		try {
    			conexion = JdbcDAOFactory.getConexion();
    			Calendar cal = new GregorianCalendar();
    			long ahora = cal.getTimeInMillis();
    		
    	        String QueryInsCH = "insert into fo_ch_compra_historicas (ch_alias, ch_fec_crea, ch_tipo, ch_cli_id, ch_unidades) "
                                  + "values ( ?, ?, ?, ?, ?)";
    			conexion.setAutoCommit(false);
    			stm = conexion.prepareStatement(QueryInsCH, Statement.RETURN_GENERATED_KEYS);
    			stm.setString(1, nombre);
    			stm.setTimestamp(2, new Timestamp(ahora));
    			stm.setString(3, "W");
    			stm.setLong(4, id_cliente);
    			stm.setLong(5, unidades);
    			logger.debug("SQL setCompraHistorica: " + QueryInsCH);
    			
    			if (stm.executeUpdate() == 0) {
    				logger.debug("setCompraHistorica - No inserta compra pero sin excepciones");
    			}
    			// ------
                else {     
                    rs = stm.getGeneratedKeys();
                    if (rs.next()) {
                        llave = rs.getInt(1);
                    } else {
                        logger.error("setCompraHistorica - Problema al recuperar id insertado");
                        throw new PedidosDAOException("setCompraHistorica - Problema al recuperar id insertado");
                    }
                }
                try {    
                    String sqlSelect = "select cant_solic, pro_id " +
                                       "from bo_detalle_pedido " +
                                       "join fo_productos on pro_id_bo = id_producto " +
                                       "where id_pedido = ?";
                    String sqlinsert = "insert into fo_ch_productos " +
                                       "(chp_ch_id, chp_cantidad, chp_pro_id) " +
                                       "values (?,?,?) ";
                    
                    stm3 = conexion.prepareStatement(sqlSelect  + " WITH UR");
                    stm3.setLong(1, pedido_id);
                    logger.debug("SQL: " + stm3.toString());
                    ResultSet rset = stm3.executeQuery();
                    
                    List productos = new ArrayList();
                    while (rset.next()) {
                        ProductoDTO pro = new ProductoDTO();
                        pro.setCantidad(rset.getDouble("cant_solic"));
                        pro.setPro_id(rset.getLong("pro_id"));
                        productos.add(pro);
                    }
                    
                    stm2 = conexion.prepareStatement(sqlinsert);
                    for (int i=0; i<productos.size(); i++) {
                        ProductoDTO pro = (ProductoDTO) productos.get(i);
                        stm2.setLong(1, llave);
                        stm2.setDouble(2, pro.getCantidad());
                        stm2.setLong(3, pro.getPro_id());
                        stm2.executeUpdate();
                    }                
                    
                } catch (SQLException ex) {
                    logger.error("setCompraHistorica - SQL llave", ex);
                    throw new PedidosDAOException("Problema insert compra (setCompraHistorica)");
                } catch (Exception ex) {
                    logger.error("setCompraHistorica - Problema llave", ex);
                    throw new PedidosDAOException("Problema insert compra (setCompraHistorica)");
                }

    		} catch (SQLException ex) {
    			logger.error("Problema con SQL (setCompraHistorica)", ex);
    			logger.info("rollback");
    			try {
    				conexion.rollback();
    				
    			} catch (SQLException e) {
    				logger.error("setCompraHistorica - Problema SQL (close)", ex);
    			}
    			logger.error("setCompraHistorica - Problema SQL", ex);
    			throw new PedidosDAOException(ex);
    			
    		} catch (Exception ex) {
    			logger.error("setCompraHistorica - Problema", ex);
    			logger.info("rollback");
    			try {
    				conexion.rollback();
    		
    			} catch (SQLException e) {
    				logger.error("setCompraHistorica - Problema SQL (close)", ex);
    			}
    			logger.error("setCompraHistorica - Problema General", ex);
    			throw new PedidosDAOException(ex);
    		} finally {
    			try {
    				if (rs != null)
    					rs.close();	
    				if (stm != null)
    					stm.close();
    				if (stm2 != null)
    					stm2.close();
    				
    				conexion.commit();
    				conexion.setAutoCommit(true);
    				
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("getRegiones - Problema SQL (close)", e);
    			}
    		}
    	}
    	public long setCompraHistoricaForMobile(String nombre, long id_cliente, ArrayList productos)
    	    	throws PedidosDAOException {

    	    		long llave = 0;
    	    		Connection conexion    = null;
    	    		PreparedStatement stm  = null;
    	    		PreparedStatement stm2 = null;
    	            PreparedStatement stm3 = null;
    	    		ResultSet rs = null;
    	    		
    	    		try {
    	    			double cantidadProductos = 0.0;
    	    			for (int i=0; i<productos.size();i++){
    	    				ProductoDTO prod = (ProductoDTO) productos.get(i);
    	    				cantidadProductos =cantidadProductos+prod.getCantidad();
    	    			}
    	    			conexion = JdbcDAOFactory.getConexion();
    	    			Calendar cal = new GregorianCalendar();
    	    			long ahora = cal.getTimeInMillis();
    	    			
    	    	        String QueryInsCH = "insert into fo_ch_compra_historicas (ch_alias, ch_fec_crea, ch_tipo, ch_cli_id, ch_unidades) "
    	                                  + "values ( ?, ?, ?, ?,?)";
    	    			conexion.setAutoCommit(false);
    	    			stm = conexion.prepareStatement(QueryInsCH, Statement.RETURN_GENERATED_KEYS);
    	    			stm.setString(1, nombre);
    	    			stm.setTimestamp(2, new Timestamp(ahora));
    	    			stm.setString(3, "M");
    	    			stm.setLong(4, id_cliente);
    	    			stm.setLong(5, new Double(cantidadProductos).longValue());
    	    			logger.debug("SQL setCompraHistorica: " + QueryInsCH);
    	    			
    	    			if (stm.executeUpdate() == 0) {
    	    				logger.debug("setCompraHistorica - No inserta compra pero sin excepciones");
    	    			}
    	    			// ------
    	                else {     
    	                    rs = stm.getGeneratedKeys();
    	                    if (rs.next()) {
    	                        llave = rs.getInt(1);
    	                    } else {
    	                        logger.error("setCompraHistorica - Problema al recuperar id insertado");
    	                        throw new PedidosDAOException("setCompraHistorica - Problema al recuperar id insertado");
    	                    }
    	                }
    	                try {    
//    	                    String sqlSelect = "select cant_solic, pro_id " +
//    	                                       "from bo_detalle_pedido " +
//    	                                       "join fo_productos on pro_id_bo = id_producto " +
//    	                                       "where id_pedido = ?";
    	                    String sqlinsert = "insert into fo_ch_productos " +
    	                                       "(chp_ch_id, chp_cantidad, chp_pro_id) " +
    	                                       "values (?,?,?) ";
    	                    
/*    	                    stm3 = conexion.prepareStatement(sqlSelect  + " WITH UR");
    	                    stm3.setLong(1, pedido_id);
    	                    logger.debug("SQL: " + stm3.toString());
    	                    ResultSet rset = stm3.executeQuery();
    	                    
    	                    List productos = new ArrayList();
    	                    while (rset.next()) {
    	                        ProductoDTO pro = new ProductoDTO();
    	                        pro.setCantidad(rset.getDouble("cant_solic"));
    	                        pro.setPro_id(rset.getLong("pro_id"));
    	                        productos.add(pro);
    	                    }*/
    	                    
    	                    stm2 = conexion.prepareStatement(sqlinsert);
    	                    for (int i=0; i<productos.size(); i++) {
    	                        ProductoDTO pro = (ProductoDTO) productos.get(i);
    	                        stm2.setLong(1, llave);
    	                        stm2.setDouble(2, pro.getCantidad());
    	                        stm2.setLong(3, pro.getPro_id());
    	                        stm2.executeUpdate();
    	                    }                
    	                    
    	                } catch (SQLException ex) {
    	                    logger.error("setCompraHistorica - SQL llave", ex);
    	                    throw new PedidosDAOException("Problema insert compra (setCompraHistorica)");
    	                } catch (Exception ex) {
    	                    logger.error("setCompraHistorica - Problema llave", ex);
    	                    throw new PedidosDAOException("Problema insert compra (setCompraHistorica)");
    	                }

    	    		} catch (SQLException ex) {
    	    			logger.error("Problema con SQL (setCompraHistorica)", ex);
    	    			logger.info("rollback");
    	    			try {
    	    				conexion.rollback();
    	    				
    	    			} catch (SQLException e) {
    	    				logger.error("setCompraHistoricaForMobile - Problema SQL (close)", ex);
    	    			}
    	    			logger.error("setCompraHistoricaForMobile - Problema SQL", ex);
    	    			throw new PedidosDAOException(ex);
    	    			
    	    		} catch (Exception ex) {
    	    			logger.error("setCompraHistoricaForMobile - Problema", ex);
    	    			logger.info("rollback");
    	    			try {
    	    				conexion.rollback();
    	    		
    	    			} catch (SQLException e) {
    	    				logger.error("setCompraHistoricaForMobile - Problema SQL (close)", ex);
    	    			}
    	    			logger.error("setCompraHistoricaForMobile - Problema General", ex);
    	    			throw new PedidosDAOException(ex);
    	    		} finally {
    	    			try {
    	    				if (rs != null)
    	    					rs.close();	
    	    				if (stm != null)
    	    					stm.close();
    	    				if (stm2 != null)
    	    					stm2.close();
    	    				
    	    				conexion.commit();
    	    				conexion.setAutoCommit(true);
    	    				
    	    				if (conexion != null && !conexion.isClosed())
    	    					conexion.close();
    	    			} catch (SQLException e) {
    	    				logger.error("setCompraHistoricaForMobile - Problema SQL (close)", e);
    	    			}
    	    		}
    	    		return llave;
    	    	}
    	
    	/* (sin Javadoc)
    	 * @see cl.bbr.fo.pedidos.dao.PedidosDAO#updateCompraHistorica(java.lang.String, long, long, long, long)
    	 */
    	public void updateCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id, long id_lista)
    	throws PedidosDAOException {

    		Connection conexion = null;
    		PreparedStatement stm = null;
    		PreparedStatement stm1 = null;
    		PreparedStatement stm2 = null;
    		PreparedStatement stm3 = null;
    		ResultSet rs = null;
    		
    		try {
    		
    			conexion = JdbcDAOFactory.getConexion();
    			
    			stm1 = conexion.prepareStatement("delete from fo_ch_compra_historicas  "
    							+ "where ch_id = ? ");
    			stm1.setLong(1, id_lista);
    			logger.debug("SQL: " + stm1.toString());
    			if (stm1.executeUpdate() == 0) {
    				logger.debug("updateCompraHistorica - No modifica pedido pero sin excepciones");
    			}
    			
    			long llave = 0;
    			conexion = JdbcDAOFactory.getConexion();
    			
    			try {
    			
    				Calendar cal = new GregorianCalendar();
    				long ahora = cal.getTimeInMillis();
    			
    		
    				conexion.setAutoCommit(false);
    				stm = conexion.prepareStatement("insert into fo_ch_compra_historicas (ch_alias, ch_fec_crea, ch_tipo, ch_cli_id, ch_unidades) "
    								+ "values ( "
    								+ "?, ?, ?, ?, ?"
    								+ ")", Statement.RETURN_GENERATED_KEYS);
    				stm.setString(1, nombre);
    				stm.setTimestamp(2, new Timestamp(ahora));
    				stm.setString(3, "W");
    				stm.setLong(4, id_cliente);
    				stm.setLong(5, unidades);
    				logger.debug("SQL: " + stm.toString());
    				if (stm.executeUpdate() == 0) {
    					logger.debug("updateCompraHistorica - No inserta pedido pero sin excepciones");
    				}
    				
    				try {
    					rs = stm.getGeneratedKeys();
    					if (rs.next()){
    						llave = rs.getInt(1);
    					}else {
    						logger
    								.error("updateCompraHistorica - Problema al recuperar id insertado");
    						throw new PedidosDAOException("setCompraHistorica - Problema al recuperar id insertado");
    					}
    				} catch (SQLException ex) {
    					logger.error("updateCompraHistorica - SQL llave pedido", ex);
    					throw new PedidosDAOException("setCompraHistorica - SQL llave pedido");
    				} catch (Exception ex) {
    					logger.error("updateCompraHistorica - Problema llave pedido", ex);
    					throw new PedidosDAOException("updateCompraHistorica - Problema llave pedido");
    				}			
    				
    				try{
    					//RECORRER E INSERTAR 
    					String querySel = "SELECT pro_id, cant_solic " +
    									  "FROM bo_detalle_pedido join fo_productos on pro_id_bo = id_producto " +
    									  "WHERE id_pedido = ?";
    					
    					stm2 = conexion.prepareStatement(querySel  + " WITH UR");
    					stm2.setLong(1, pedido_id);
    					logger.debug("SQL: " + stm2.toString());
    					rs = stm2.executeQuery();
    					
    					while (rs.next()) {
    						stm3 = conexion.prepareStatement(""
    								+ "insert into fo_ch_productos (chp_ch_id, chp_cantidad, chp_pro_id) "
    								+ "values ( "
    								+ "?, ?, ?"
    								+ ")");
    						stm3.setLong(1, llave);
    						stm3.setDouble(2, rs.getDouble("cant_solic"));
    						stm3.setLong(3, rs.getLong("pro_id"));
    						
    						if (stm3.executeUpdate() == 0) {
    							logger.debug("updateCompraHistorica - No inserta pedido pero sin excepciones");
    						}
    					}
    				
    				} catch (SQLException ex) {
    					logger.error("SQL insert pedido (updateCompraHistorica) ", ex);
    					throw new PedidosDAOException("SQL insert pedido(updateCompraHistorica) ");
    				} catch (Exception ex) {
    					logger.error("Problema insert pedido (updateCompraHistorica)", ex);
    					throw new PedidosDAOException("Problema insert pedido (updateCompraHistorica)");
    				}
    			} catch (SQLException ex) {
    				logger.error("Problema con SQL (updateCompraHistorica)", ex);
    				logger.info("rollback");
    				try {
    					conexion.rollback();
    				} catch (SQLException e) {
    					logger.error("updateCompraHistorica - Problema SQL (close)", ex);
    				}
    				logger.error("updateCompraHistorica - Problema SQL", ex);
    				throw new PedidosDAOException(ex);
    			} catch (Exception ex) {
    				logger.error("updateCompraHistorica - Problema", ex);
    				logger.info("rollback");
    				try {
    					conexion.rollback();
    				} catch (SQLException e) {
    					logger.error("updateCompraHistorica - Problema SQL (close)", ex);
    				}
    				logger.error("updateCompraHistorica - Problema General", ex);
    				throw new PedidosDAOException(ex);
    			}
    		} catch (SQLException ex) {
    			logger.error("updateCompraHistorica - Problema SQL", ex);
    			throw new PedidosDAOException(ex);
    		} catch (Exception ex) {
    			logger.error("updateCompraHistorica - Problema General", ex);
    			throw new PedidosDAOException(ex);
    		} finally {
    			try {
    				if (rs != null)
    					rs.close();	
    				if (stm != null)
    					stm.close();
    				if (stm1 != null)
    					stm1.close();
    				if (stm2 != null)
    					stm2.close();
    				if (stm3 != null)
    					stm3.close();
    				logger.info("commit");
    				conexion.commit();
    				conexion.setAutoCommit(true);

    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateCompraHistorica - Problema SQL (close)", e);
    			}
    		}

    	}
    	
    	/* (sin Javadoc)
    	 * @see cl.bbr.fo.pedidos.dao.PedidosDAO#updateRankingVentas(java.util.List)
    	 */
    	public void updateRankingVentas(long id_cliente ) throws PedidosDAOException {

    		Connection conexion = null;
    		PreparedStatement stm = null;
    		
    		try {
    			conexion = JdbcDAOFactory.getConexion();
    			
    			String sql = "merge into fodba.fo_productos fp "
    				   + "using (select distinct fcc.CAR_PRO_ID, fcc.CAR_CANTIDAD "
    				   + "from fodba.fo_carro_compras as fcc where fcc.CAR_CLI_ID = ?) "
    				   + "as ra on fp.pro_id = ra.car_pro_id when matched then "
    				   + "update set fp.PRO_RANKING_VENTAS = fp.PRO_RANKING_VENTAS + ra.CAR_CANTIDAD";
    			
    			stm = conexion.prepareStatement(sql);
    			stm.setLong(1,id_cliente);
    			
    			if (stm.executeUpdate() == 0) {
    				logger.debug("updateRankingVentas - No inserta pedido pero sin excepciones");
    			}
    				
    		} catch (SQLException ex) {
    			logger.error("updateRankingVentas - Problema SQL", ex);
    			throw new PedidosDAOException(ex);
    		} catch (Exception ex) {
    			logger.error("updateRankingVentas - Problema General", ex);
    			throw new PedidosDAOException(ex);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateRankingVentas - Problema SQL (close)", e);
    			}
    		}
    	}

    	
    	/* (sin Javadoc)
    	 * @see cl.bbr.fo.pedidos.dao.PedidosDAO#updateRankingVentas(java.util.List)
    	 */
    	/*public void updateRankingVentas( List carro_compras ) throws PedidosDAOException {

    		Connection conexion = null;
    		PreparedStatement stm3 = null;
    		
    		try {
    		
    			conexion = JdbcDAOFactory.getConexion();
    			
    			for (int i = 0; i < carro_compras.size(); i++) {
    				CarroCompraDTO prods = (CarroCompraDTO)carro_compras.get(i);;
    				stm3 = conexion.prepareStatement("update fo_productos " +
    						"set pro_ranking_ventas = pro_ranking_ventas + ? " +
    						"where pro_id = ? " );
    				stm3.setLong(1, Math.round(Math.ceil(prods.getCantidad())) );
    				stm3.setLong(2, Long.parseLong(prods.getPro_id()) );				
    				if (stm3.executeUpdate() == 0) {
    					logger.debug("updateCompraHistorica - No inserta pedido pero sin excepciones");
    				}
    			}
    				
    		} catch (SQLException ex) {
    			logger.error("updateCompraHistorica - Problema SQL", ex);
    			throw new PedidosDAOException(ex);
    		} catch (Exception ex) {
    			logger.error("updateCompraHistorica - Problema General", ex);
    			throw new PedidosDAOException(ex);
    		} finally {
    			try {
    				if (stm3 != null)
    					stm3.close();
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateCompraHistorica - Problema SQL (close)", e);
    			}
    		}
    	}*/

    	
    	/* (sin Javadoc)
    	 * @see cl.bbr.fo.pedidos.dao.PedidosDAO#updateNombreCompraHistorica(java.util.List)
    	 */
    	public void updateNombreCompraHistorica( String nombre, long id_cliente ) throws PedidosDAOException {

    		Connection conexion = null;
    		PreparedStatement stm2 = null;
    		PreparedStatement stm3 = null;
    		ResultSet rs = null;
    		try {
    		
    			conexion = JdbcDAOFactory.getConexion();
    			
    			String querySel = "select max(ch_id) as maxid from fo_ch_compra_historicas where ch_cli_id = ? ";
    			stm2 = conexion.prepareStatement(querySel  + " WITH UR");
    			stm2.setLong(1, id_cliente);
    			logger.debug("SQL: " + stm2.toString());
    			rs = stm2.executeQuery();
    			
    			long max_id = 0;
    			if (rs.next()) {
    				try{
    					max_id = rs.getLong("maxid"); 
    					stm3 = conexion.prepareStatement("update fo_ch_compra_historicas " +
    							"set ch_alias = ? " +
    							"where ch_id = ? and ch_cli_id = ? " );
    					if (nombre != null)
    						stm3.setString(1, nombre);
    					else
    						stm3.setNull(1, Types.VARCHAR);	
    					stm3.setLong(2, max_id);
    					stm3.setLong(3, id_cliente);				
    					if (stm3.executeUpdate() == 0) {
    						logger.debug("updateNombreCompraHistorica - No modifica nombre lista pero sin excepciones");
    					}
    				} catch (SQLException ex) {
    					logger.error("updateNombreCompraHistorica - SQL update nombre", ex);
    					throw new PedidosDAOException("updateNombreCompraHistorica - SQL update nombre lista", ex);
    				} catch (Exception ex) {
    					logger.error("updateNombreCompraHistorica - SQL nombre lista", ex);
    					throw new PedidosDAOException("updateNombreCompraHistorica - SQL update nombre lista");
    				}
    			}	
    		} catch (SQLException ex) {
    			logger.error("updateNombreCompraHistorica - Problema SQL", ex);
    			throw new PedidosDAOException(ex);
    		} catch (Exception ex) {
    			logger.error("updateNombreCompraHistorica - Problema General", ex);
    			throw new PedidosDAOException(ex);
    		} finally {
    			try {
    				if (rs != null)
                        rs.close(); 
    				if (stm2 != null)
    					stm2.close();
    				if (stm3 != null)
    					stm3.close();
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateNombreCompraHistorica - Problema SQL (close)", e);
    			}
    		}
    	}
    	
    	/**
    	 * Cristian Valdebenito
    	 */
    	public void updateDatosPedidoInvitado( int idpedido ,long rut , String dv, String nombre, String apellido )  throws PedidosDAOException 
    	{
    		Connection conexion = null;
    		PreparedStatement stm = null;
    		
    		try {
    			conexion = JdbcDAOFactory.getConexion();
    			String sql = "update BODBA.bo_pedidos " +
    						 " set rut_cliente=?, dv_cliente=?, nom_cliente=? " +
    						 " where id_pedido=? ";
    						
    			
    			stm = conexion.prepareStatement(sql);
    			stm.setLong(1,rut);
    			stm.setString(2, dv);
    			stm.setString(3,nombre+" "+apellido);
    			stm.setInt(4,idpedido);

    			if (stm.executeUpdate() == 0) {
    				logger.debug("setCompraHistorica - No inserta compra pero sin excepciones");
    				System.out.println("OK");
    			}
    				
    		} catch (SQLException ex) {
    			logger.error("Problemas en Update  Datos Cliente OP ", ex);
    			throw new PedidosDAOException(String.valueOf(ex.getErrorCode()),ex);
    		} finally {
    			try {
    				if (stm != null)
    					stm.close();
    				
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateNombreCompraHistorica - Problema SQL (close)", e);
    			}
    		
    		}
    		
    	}
    	public PagoGrabilityDTO getPagoByOP(long id_pedido)throws PedidosDAOException {
    		
    		PreparedStatement stm = null;
    		ResultSet rs = null;
    		PagoGrabilityDTO pago = null;
    		Connection conec = null;
    		try {
    			conec=getConnection();
    			String sql = "SELECT ID_PEDIDO, TOKEN_PAGO, ESTADO, FCREACION, FVALIDACION " +
    						 " FROM BODBA.BO_PAGO_GRABILITY WHERE ID_PEDIDO = ?";
    			stm = conn.prepareStatement( sql + " WITH UR");
    			stm.setLong(1, id_pedido);
    			logger.debug("SQL: " + stm.toString());
    			rs = stm.executeQuery();
    			while (rs.next()){
    				pago = new PagoGrabilityDTO();
    				pago.setIdPedido(rs.getLong("ID_PEDIDO"));
    				pago.setTokenPago(rs.getString("TOKEN_PAGO"));
    				pago.setEstado(rs.getString("ESTADO"));
    				pago.setfCreacion(rs.getString("FCREACION"));
    				pago.setfValidacion(rs.getString("FVALIDACION"));
    			}
    			} catch (Exception e) {
    	               logger.debug("Problema:"+ e);
    	               throw new PedidosDAOException(e);
    	   		} finally {
    	   			
    	   			stm = null;
    	   			rs = null;
    	   		}
    		return pago;
    	}
    	
    	public PagoGrabilityDTO getPagoByToken(String token)throws PedidosDAOException {
    		
    		PreparedStatement stm = null;
    		ResultSet rs = null;
    		PagoGrabilityDTO pago = null;
    		Connection conec = null;
    		try {
    			conec=getConnection();
    			String sql = "SELECT ID_PEDIDO, TOKEN_PAGO, ESTADO, FCREACION, FVALIDACION " +
    						 " FROM BODBA.BO_PAGO_GRABILITY WHERE TOKEN_PAGO = ?";
    			stm = conec.prepareStatement( sql + " WITH UR");
    			stm.setString(1, token);
    			logger.debug("SQL: " + stm.toString());
    			rs = stm.executeQuery();
    			while (rs.next()){
    				pago = new PagoGrabilityDTO();
    				pago.setIdPedido(rs.getLong("ID_PEDIDO"));
    				pago.setTokenPago(rs.getString("TOKEN_PAGO"));
    				pago.setEstado(rs.getString("ESTADO"));
    				pago.setfCreacion(rs.getString("FCREACION"));
    				pago.setfValidacion(rs.getString("FVALIDACION"));
    			}
    			} catch (Exception e) {
    	               logger.debug("Problema:"+ e);
    	               throw new PedidosDAOException(e);
    			} finally {
    	   			
    	   			try {
						stm.close();					
						rs.close();
						conec.close();
    	   			} catch (SQLException e) {						
						e.printStackTrace();
					}
    	   		}
    		return pago;
    	}


		public void insertRegistroPago(PagoGrabilityDTO oPagoGrabilityDTO) throws PedidosDAOException {
			
			PreparedStatement stm = null;
			String sql =" INSERT INTO BODBA.BO_PAGO_GRABILITY (ID_PEDIDO, TOKEN_PAGO) VALUES (?,?) ";

			try {
				
				conn = this.getConnection();				
				stm = conn.prepareStatement(sql);
				
				stm.setLong(1, oPagoGrabilityDTO.getIdPedido());
				stm.setString(2,oPagoGrabilityDTO.getTokenPago());
	            
				stm.executeUpdate();
				
			}catch (SQLException e) {
				logger.error(e);
				throw new PedidosDAOException(e);
			} finally {
				try {
					if (stm != null)
						stm.close();
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}			
		}       
		public void actualizaPagoGrabilityByOP(PagoGrabilityDTO pago) throws PedidosDAOException {
			
			PreparedStatement stm = null;
			String sql =" UPDATE BODBA.BO_PAGO_GRABILITY SET ESTADO=?, FVALIDACION=current_timestamp WHERE ID_PEDIDO=? ";
			try {
				
				conn = this.getConnection();				
				stm = conn.prepareStatement(sql);
				
				stm.setString(1, pago.getEstado());
				stm.setLong(2,pago.getIdPedido());
	            
				stm.executeUpdate();
				
			}catch (SQLException e) {
				logger.error(e);
				throw new PedidosDAOException(e);
			} finally {
				try {
					if (stm != null)
						stm.close();
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}			
		}

		public List listaDeProductosPreferidos(int client_id) throws PedidosDAOException {
			Connection conexion = null;
    		PreparedStatement stm2 = null;
    		PreparedStatement stm3 = null;
    		ResultSet rs = null;
    		ArrayList preferidos = new ArrayList();
    		try {
    		
    			conexion = JdbcDAOFactory.getConexion();
    			
    			String querySel = "SELECT P.CHP_CANTIDAD cantidad, P.CHP_PRO_ID idProducto from FODBA.FO_CH_PRODUCTOS P" +
    							" INNER JOIN FODBA.FO_CH_COMPRA_HISTORICAS  C " +
    							" ON  C.CH_ID=P.CHP_CH_ID " +
    							" WHERE C.CH_CLI_ID = ? GROUP by p.CHP_PRO_ID,P.CHP_CANTIDAD ";
    			stm2 = conexion.prepareStatement(querySel  + " WITH UR");
    			stm2.setLong(1, client_id);
    			logger.debug("SQL: " + stm2.toString());
    			rs = stm2.executeQuery();
    			
    			while (rs.next()) {
    				ProductoDTO prod = new ProductoDTO();
    				prod.setCantidad(rs.getDouble("cantidad"));
    				prod.setPro_id(rs.getLong("idProducto"));
    				preferidos.add(prod);
    				
    			}
    				} catch (SQLException ex) {
    					logger.error("listaDeProductosPreferidos - SQL update nombre", ex);
    					throw new PedidosDAOException("updateNombreCompraHistorica - SQL update nombre lista", ex);
    				} catch (Exception ex) {
    					logger.error("listaDeProductosPreferidos - SQL nombre lista", ex);
    					throw new PedidosDAOException("updateNombreCompraHistorica - SQL update nombre lista");
    				}
    				
    		
    		 finally {
    			try {
    				if (rs != null)
                        rs.close(); 
    				if (stm2 != null)
    					stm2.close();
    				if (stm3 != null)
    					stm3.close();
    				if (conexion != null && !conexion.isClosed())
    					conexion.close();
    			} catch (SQLException e) {
    				logger.error("updateNombreCompraHistorica - Problema SQL (close)", e);
    			}
    		}
    		return preferidos;
		}

	public long updateList(long idClient, ArrayList productsArray,
			String listType, long listId, String listName) throws PedidosDAOException {
		PreparedStatement stm = null;
		PreparedStatement stm2 = null;
		PreparedStatement stm3 = null;
		// primero borramos los productos de la lista
		String sqldelete = "delete from FODBA.FO_CH_PRODUCTOS WHERE CHP_CH_ID = ? ";
		// String sql
		// =" UPDATE BODBA.BO_PAGO_GRABILITY SET ESTADO=?, FCREACION=? WHERE ID_PEDIDO=? ";
		// ahora insertamos los productos nuevamente que vienen en el array

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sqldelete);
			stm.setLong(1, listId);
			stm.executeUpdate(); //se ejecuta delete
			String sqlinsert = "insert into fo_ch_productos "
					+ "(chp_ch_id, chp_cantidad, chp_pro_id) "
					+ "values (?,?,?) ";
			if (productsArray.size()>0){
				stm2 = conn.prepareStatement(sqlinsert);
				for (int i = 0; i < productsArray.size(); i++) {
					ProductoDTO pro = (ProductoDTO) productsArray.get(i);
					stm2.setLong(1, listId);
					stm2.setDouble(2, pro.getCantidad());
					stm2.setLong(3, pro.getPro_id());
					stm2.executeUpdate();
				}
			}
			String sqlUpdateListName = "update fodba.fo_ch_compra_historicas " +
										"set ch_alias= ? where ch_id= ? ";
			if (listName!=null){
				stm3 = conn.prepareStatement(sqlUpdateListName);
				stm3.setString(1, listName);
				stm3.setLong(2,listId);
				stm3.executeUpdate();
			}
			}catch (SQLException e) {
				logger.error(e);
				throw new PedidosDAOException(e);
			} finally {
				try {
					if (stm != null)
						stm.close();
					if (stm2 != null)
						stm2.close();
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}			
			return listId;
			
			
		}   
		
	public boolean isOPConProductosFaltantesEnPromoMxN(long id_pedido) throws PedidosDAOException {
		List result = new ArrayList();
		PreparedStatement stm=null;
		PreparedStatement stm1=null;
		PreparedStatement stm2=null;
		PreparedStatement stm4=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		ResultSet rs4=null;
		
		try {
			
			logger.debug("getDetallesPedidoMxN " );
			  
			String sqlparam = "SELECT VALOR FROM BODBA.BO_PARAMETROS WHERE NOMBRE = 'ERROR_PROMOCION_MXN' WITH UR";
			    			
			String sql = "SELECT dp.ID_PEDIDO, dp.ID_DETALLE, bpt.PROMO_CODIGO " +
					" FROM  bo_detalle_pedido dp " +
					" inner join BO_PROMOS_DETPED bpt on bpt.ID_DETALLE=dp.ID_DETALLE " +
					" WHERE dp.id_pedido = ? and bpt.PROMO_CODIGO in(SELECT  bpt.PROMO_CODIGO " +
					" FROM  bo_detalle_pedido dp " +
					" inner join BO_PROMOS_DETPED bpt on bpt.ID_DETALLE=dp.ID_DETALLE " +
					" WHERE dp.id_pedido = ? and dp.PRECIO=1 and bpt.PROMO_TIPO=11 and  dp.CANT_FALTAN<>dp.CANT_SOLIC ) "; 
			
			
			String sql2 = "SELECT count(1) AS TOTAL FROM  bo_detalle_pedido dp where dp.ID_DETALLE=? and dp.ID_PEDIDO=? and (dp.CANT_FALTAN>0 or CANT_SPICK>0) ";

			String sql3 = " SELECT count(1) AS TOTAL FROM  BO_DETALLE_PICKING dpk where dpk.ID_DETALLE=? and dpk.ID_PEDIDO=? and dpk.SUSTITUTO='S' ";
			
			int totalTrue = 0;
			conn = this.getConnection();
			    			    			
			stm4 = conn.prepareStatement(sqlparam);
			rs4 = stm4.executeQuery();
			String isTrue="FALSE";
			if (rs4.next()){
				isTrue = rs4.getString("VALOR");
			}
			
			if(isTrue.equals("FALSE")){
				return false;
			}
					
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,id_pedido);
			stm.setLong(2,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			int total = 0;
			rs = stm.executeQuery();
			while (rs.next()) {
				
				//ProductosPedidoDTO prod = new ProductosPedidoDTO();
				long idpedido = rs.getLong("ID_PEDIDO");
				long idetalle = rs.getInt("ID_DETALLE");
				long promo = rs.getInt("PROMO_CODIGO");
				    				
				stm1 = conn.prepareStatement(sql2 + " WITH UR");
				stm1.setLong(1,idetalle);
				stm1.setLong(2,idpedido);
				rs2 = stm1.executeQuery();
				if (rs2.next()) {

					total = rs2.getInt("TOTAL");
					if(total>0){
						totalTrue++;
						break;
					}else{

	    				stm2 = conn.prepareStatement(sql3 + " WITH UR");
	    				stm2.setLong(1,idetalle);
	    				stm2.setLong(2,idpedido);
	    				rs3 = stm2.executeQuery();
	    				if (rs3.next()) {
	    					total = rs3.getInt("TOTAL");
	    					if(total>0){
	    						totalTrue++;
	    						break;
	    					}
	    				}  
					}    					
				}
				
			}

			if(totalTrue>0){
				return true;
			}
		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return false;
		
	}
	
	public Map getOPConProductosFaltantesEnPromoMxN(long id_pedido) throws PedidosDAOException{
    	List result = new ArrayList();
		PreparedStatement stm=null;
		Map mapa = new HashMap();
		
		ResultSet rs=null;
		
		try {
			
			logger.debug("getDetallesPedidoMxN " );
			    			    			
			String sql = "SELECT dp.ID_PEDIDO, dp.ID_DETALLE, dp.PRECIO, bpt.PROMO_CODIGO " +
					" FROM  bo_detalle_pedido dp " +
					" inner join BO_PROMOS_DETPED bpt on bpt.ID_DETALLE=dp.ID_DETALLE " +
					" WHERE dp.id_pedido = ? and bpt.PROMO_CODIGO in(SELECT  bpt.PROMO_CODIGO " +
					" FROM  bo_detalle_pedido dp " +
					" inner join BO_PROMOS_DETPED bpt on bpt.ID_DETALLE=dp.ID_DETALLE " +
					" WHERE dp.id_pedido = ? and dp.PRECIO=1 and bpt.PROMO_TIPO=11 ) "; 						
			
			int totalTrue = 0;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,id_pedido);
			stm.setLong(2,id_pedido);
			logger.debug("SQL: " + sql);
			logger.debug("id_pedido: " + id_pedido);

			int total = 0;
			rs = stm.executeQuery();
			List lista = null;
			while (rs.next()) {
				String codpromo = String.valueOf(rs.getInt("PROMO_CODIGO"));
				
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getInt("ID_DETALLE"));
				prod.setId_pedido(rs.getLong("ID_PEDIDO"));
				prod.setPrecio(rs.getDouble("PRECIO"));
				prod.setCodigoPromocion(rs.getInt("PROMO_CODIGO"));
				lista = new ArrayList();									
				lista.add( prod );
				List listAux = (List)mapa.get( codpromo );

				if(null==listAux){
					mapa.put(codpromo, lista);
				}else{
					listAux.add(prod);						
					//mapa.remove(codpromo);
					mapa.put(codpromo, listAux);
				}
			}

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return mapa; 
     }
	public JornadaDTO getDatosJornada(String hora, String fecha, long idLocal) throws PedidosDAOException, ParseException {

		JornadaDTO jornada = new JornadaDTO();
		PreparedStatement stm = null;
		ResultSet rs = null;
		long idJornadaDespacho = 0;
		try {
			conn = this.getConnection();
			String sql = "select j.id_jdespacho, h.hini,h.hfin,j.fecha from BO_JORNADA_DESP j "
					+ "inner join BO_HORARIO_DESP h "
					+ "on j.ID_HOR_DESP =  h.ID_HOR_DESP "
					+ "where j.fecha=? and SUBSTR((char(time(h.hini),USA)),7,2)=? " 
					//"? between h.hini and h.hfin "
					+ "and j.id_zona = (select id_zona from BO_ZONAS where NOMBRE= 'Z99 Venta Masiva' and id_local=?) ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1,fecha);
			stm.setString(2,hora);
			stm.setLong(3,idLocal);
			rs = stm.executeQuery();
			while (rs.next()) {
				idJornadaDespacho=rs.getLong("ID_JDESPACHO");
			}
			jornada.setId_jornada(idJornadaDespacho);		

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDatosJornada - Problema SQL (close)", e);
			}

		}
		return jornada;
	}

	
	/* 
	 * INICIO VENTA MASIVA 
	 * */	
	public void addPagoVentaMasiva(PagoVentaMasivaDTO dto ) throws PedidosDAOException {
		PreparedStatement stm = null;		
		String sql =" INSERT INTO BODBA.BO_VENTAMASIVA_PURCHASE (ID_PEDIDO, TOKEN_PAGO) VALUES (?,?) ";
		try {
			conn = this.getConnection();		
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, dto.getIdPedido());
			stm.setString(2,dto.getTokenPago());

			stm.executeUpdate();
			//rs = stm.getGeneratedKeys();
			//if (rs.next())
			//	System.out.println(rs.getInt(1));
				
		}catch (SQLException e) {
			logger.error(e);			
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addPagoVentaMasiva - Problema SQL (close)", e);
			}
		}		
	}       
	public PagoVentaMasivaDTO getPagoVentaMasivaByToken(String token)throws PedidosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		PagoVentaMasivaDTO pago = null;	
		try {
			conn = this.getConnection();		
			String sql = "SELECT ID_PEDIDO, TOKEN_PAGO, ESTADO, FCREACION, FVALIDACION " +
						 " FROM BODBA.BO_VENTAMASIVA_PURCHASE WHERE TOKEN_PAGO = ?";
			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setString(1, token);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()){
				pago = new PagoVentaMasivaDTO();
				pago.setIdPedido(rs.getLong("ID_PEDIDO"));
				pago.setTokenPago(rs.getString("TOKEN_PAGO"));
				pago.setEstado(rs.getString("ESTADO"));
				pago.setfCreacion(rs.getString("FCREACION"));
				pago.setfValidacion(rs.getString("FVALIDACION"));
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();				
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPagoVentaMasivaByToken - Problema SQL (close)", e);
			}						
		}
		return pago;
	}
	public void actualizaPagoVentaMasivaByOP(PagoVentaMasivaDTO pago) throws PedidosDAOException {		
		PreparedStatement stm = null;
		String sql =" UPDATE BODBA.BO_VENTAMASIVA_PURCHASE SET ESTADO=?, FVALIDACION=current_timestamp WHERE ID_PEDIDO=? ";
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, pago.getEstado());
			stm.setLong(2,pago.getIdPedido());
            
			stm.executeUpdate();
			
		}catch (SQLException e) {
			logger.error(e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPagoVentaMasivaByToken - Problema SQL (close)", e);
			}
		}			
	}
	
	public void addDetallePickingVentaMasiva(List lista, CreaRondaDTO dtoRonda, BinDTO dtoBin) throws PedidosDAOException {			
		PreparedStatement stm = null;
		PreparedStatement psRonda = null;
		PreparedStatement psBin = null;
		ResultSet rs = null;
		long id_ronda=0;
		long binId = 0;
		int n;
		try {
			conn = this.getConnection();			
			logger.debug("en addDetallePicking:");			    												

			String sqlRonda = " INSERT INTO BO_RONDAS  " +
					"(id_sector, id_jpicking, id_estado, id_local, cant_productos, fcreacion, tipo_ve)" +
					"VALUES ( ? ,? , ? , ?, ? , CURRENT TIMESTAMP, ?)";
									
			String sqlBin = "INSERT INTO bo_bins_pedido "
	                + "(id_ronda, id_pedido, cod_bin, cod_sello1, cod_sello2, cod_ubicacion, tipo, visualizado) "
	                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";			
			
			String sql = "INSERT INTO BO_DETALLE_PICKING " 
							+ "(ID_DETALLE, ID_BP, ID_PRODUCTO, ID_PEDIDO, CBARRA, DESCRIPCION, " 
							+ "PRECIO, CANT_PICK, SUSTITUTO, AUDITADO, PRECIO_PICK) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			psRonda = conn.prepareStatement(sqlRonda, Statement.RETURN_GENERATED_KEYS);
			psRonda.setLong(1, dtoRonda.getId_sector());
			psRonda.setLong(2, dtoRonda.getId_jpicking());
			psRonda.setLong(3, dtoRonda.getId_estado()); //estado rondas : creada
			psRonda.setLong(4, dtoRonda.getId_local());
			psRonda.setDouble(5, lista.size());
			psRonda.setString(6, Constantes.TIPO_VE_NORMAL_CTE);					
			n = psRonda.executeUpdate();						
			rs = psRonda.getGeneratedKeys();
			if (rs.next()) {				
				 id_ronda = rs.getLong(1);
				 logger.debug("el id_ronda nuevo es:"+id_ronda);			
			}
			if (id_ronda == 0) {
				throw new PedidosDAOException("No puede obtener nuevo id_ronda");
			}
			
			psBin = conn.prepareStatement(sqlBin, Statement.RETURN_GENERATED_KEYS);
			psBin.setLong(1, id_ronda);
			psBin.setLong(2, dtoBin.getId_pedido());
			psBin.setString(3, dtoBin.getCod_bin());
			psBin.setString(4, dtoBin.getCod_sello1());
			psBin.setString(5, dtoBin.getCod_sello2());
			psBin.setString(6, dtoBin.getCod_ubicacion());
			psBin.setString(7, dtoBin.getTipo());
			psBin.setString(8, dtoBin.getVisualizado());
			n = psBin.executeUpdate();            
            rs = psBin.getGeneratedKeys();           
            if (rs.next()) {
                binId = rs.getLong(1);
            }
            if (binId == 0) {
				throw new PedidosDAOException("No puede obtener nuevo id_bind");
			}
            logger.debug("id del nuevo bin : " + binId);
            
			stm = conn.prepareStatement(sql + " WITH UR");
			if (lista.size()>0){
				logger.debug("grabando detallepicking size: " + lista.size());
				for (int i = 0; i < lista.size(); i++) {
					DetallePickingDTO pro = (DetallePickingDTO) lista.get(i);
					stm.setLong(1, pro.getId_detalle());
					stm.setLong(2, binId);					
					stm.setLong(3, pro.getId_producto());
					stm.setLong(4, pro.getId_pedido());
					stm.setString(5, pro.getCBarra());
					stm.setString(6, pro.getDescripcion());
					stm.setDouble(7, pro.getPrecio());
					stm.setDouble(8, pro.getCant_pick());
					stm.setString(9,pro.getSustituto());
					stm.setString(10,pro.getAuditado());
					stm.setDouble(11,  pro.getPrecio());
					stm.addBatch();
				}
													
				int[] result = stm.executeBatch();
				stm.clearBatch();	
			}
		}catch (SQLException e) {
			logger.error(e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (psRonda != null)
					psRonda.close();
				if (psBin != null)
					psBin.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}		
		//return id_det;
	}
	
	 public List getProductosSolicitadosVMById(long id_pedido) throws PedidosDAOException {
		 PreparedStatement stm = null;
		 ResultSet rs = null;
		 CarroCompraCategoriasDTO aux_cat = null;
		 List aux_lpro = null;
		 List productosPorCategoria = new ArrayList();
		 CarroCompraProductosDTO aux_pro = null;
		 CarroCompraCategoriasDTO categoria = null;
		 CarroCompraProductosDTO productos = null;
		 List l_pro = null;

		 try {
 			 
			 String sql = "SELECT pro.PRO_ID_BO, pro.PRO_TIPO_PRODUCTO, pro.PRO_DES_CORTA, " +
                     "pro.PRO_COD_SAP, pro.PRO_TIPO_SEL, pro.PRO_TIPRE, pro.PRO_INTER_MAX, pro.PRO_INTER_VALOR, " +
                     "pro.PRO_NOTA, ma.MAR_NOMBRE, dp.CANT_SOLIC, dp.PRECIO," +
                     "case when dp.id_producto is null then '' else dp.uni_med end as uni_med, uni.UNI_DESC, " +
                     "dp.ID_DETALLE, p.EAN13 " +
                     "from bodba.bo_detalle_pedido dp " +
                     "JOIN bodba.BO_PRODUCTOS p on (p.ID_PRODUCTO=dp.ID_PRODUCTO) " +
                     "JOIN fo_productos pro on (dp.ID_PRODUCTO = pro.PRO_ID_BO) " +  
                     "    join " +
                     "           ( " +
                     "           select pro1.pro_id as pro_id " +
                     "           from bodba.bo_detalle_pedido dp1 " +
                     "           join fo_productos pro1 on (dp1.ID_PRODUCTO = pro1.PRO_ID_BO) " +                                          
                     "           where dp1.ID_PEDIDO = ? " +
                     "           group by pro1.pro_id " +
                     "           ) as x  on x.pro_id = pro.pro_id " +
                     "    JOIN FODBA.fo_unidades_medida uni on pro.PRO_UNI_ID = uni.UNI_ID " +
                     "    left join fo_marcas ma on (pro.PRO_MAR_ID = ma.MAR_ID) " +
                     "where dp.ID_PEDIDO = ? order by pro.PRO_ID"; 
	            conn = this.getConnection();
	            stm = conn.prepareStatement(sql + " WITH UR");
	            stm.setLong(1,id_pedido);
	            stm.setLong(2,id_pedido);

	            rs = stm.executeQuery();
	            while (rs.next()) {
	                l_pro = new ArrayList();                	                
	                categoria = new CarroCompraCategoriasDTO();
	                                
	                // Agregar los productos
	                productos = new CarroCompraProductosDTO();
	                productos.setPro_id(rs.getLong("PRO_ID_BO"));
	                productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
	                productos.setCodigo(rs.getString("pro_cod_sap"));
	                productos.setPpum(rs.getDouble("precio"));
	                productos.setPrecio( rs.getDouble("precio") );              
	                productos.setCantidad(rs.getDouble("CANT_SOLIC"));
	                productos.setUnidad_nombre(rs.getString("uni_desc"));
	                productos.setUnidadMedida(rs.getString("uni_med"));
	                productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
	                
	                productos.setTipre( Formatos.formatoUnidad(rs.getString("pro_tipre")) );
	                productos.setInter_maximo( rs.getDouble("pro_inter_max") );
	                if( rs.getString("pro_inter_valor") != null ) {
	                    productos.setInter_valor(rs.getDouble("pro_inter_valor"));
	                } else {
	                    productos.setInter_valor(1.0);
	                }
	                productos.setMarca(rs.getString("mar_nombre"));
	                if( rs.getString("pro_nota").compareTo("S") == 0 ) {
	                    productos.setCon_nota( true );
	                } else {
	                    productos.setCon_nota( false );
	                }
	                productos.setStock(100); // No toma encuenta el stock nunca pone 0
	                productos.setIdDetalle(rs.getLong("ID_DETALLE"));
	                productos.setEan13(rs.getString("EAN13"));
	                productos.setDescripcion(rs.getString("PRO_DES_CORTA"));
	                l_pro.add(productos);
	                categoria.setCarroCompraProductosDTO(l_pro);	                
	                productosPorCategoria.add(categoria);	                             
	            }

	        } catch (SQLException e) {
	            throw new PedidosDAOException(String.valueOf(e.getErrorCode()),e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stm != null)
						stm.close();
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}
	        return productosPorCategoria;
	    }
	 
	 public PagoVentaMasivaDTO getPagoVentaMasivaByIdPedido(long idPedido)throws PedidosDAOException {
			PreparedStatement stm = null;
			ResultSet rs = null;
			PagoVentaMasivaDTO pago = null;	
			try {
				conn = this.getConnection();		
				String sql = "SELECT TOKEN_PAGO" +
							 " FROM BODBA.BO_VENTAMASIVA_PURCHASE WHERE ID_PEDIDO = ?";
				stm = conn.prepareStatement( sql + " WITH UR");
				stm.setLong(1, idPedido);
				logger.debug("SQL: " + stm.toString());
				rs = stm.executeQuery();
				while (rs.next()){
					pago = new PagoVentaMasivaDTO();
					//pago.setIdPedido(rs.getLong("ID_PEIDO"));
					pago.setTokenPago(rs.getString("TOKEN_PAGO"));
					//pago.setEstado(rs.getString("ESTADO"));
					//pago.setfCreacion(rs.getString("FCREACION"));
					//pago.setfValidacion(rs.getString("FVALIDACION"));
				}
			} catch (Exception e) {
				logger.debug("Problema:"+ e);
				throw new PedidosDAOException(e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stm != null)
						stm.close();				
					releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : getPagoVentaMasivaByIdPedido - Problema SQL (close)", e);
				}						
			}
			return pago;
		}

	/* 
	 * FIN INICIO VENTA MASIVA 
	 * */

	public JornadaDTO getDatosJornadaDespachoSegunComuna(String hora,String fecha, long comuna_id, long idLocal) throws PedidosDAOException, ParseException {
		ZonasPorComunaVentaMasivaDTO zonaXComuna = new ZonasPorComunaVentaMasivaDTO();
		JornadaDTO jornada = new JornadaDTO();
		PreparedStatement stm = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sqlZonaXComuna = "select ZVM_ID, ZVM_ID_COMUNA, ZVM_ID_ZONA" +
				" from BO_ZONASXCOMUNA_VENTA_MASIVA " +
				"where ZVM_ID_COMUNA = ?";
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sqlZonaXComuna + " WITH UR");
			stm.setLong(1, comuna_id);
			rs1 = stm.executeQuery();
			while (rs1.next()){
				zonaXComuna.setZvmId(rs1.getLong("ZVM_ID"));
				zonaXComuna.setZvmIdComuna(rs1.getLong("ZVM_ID_COMUNA"));
				zonaXComuna.setZvmIdZona(rs1.getLong("ZVM_ID_ZONA"));
				
			}
		}catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		}

		long idJornadaDespacho = 0;
		try {
			conn = this.getConnection();
			String sql = "select j.id_jdespacho, h.hini,h.hfin,j.fecha from BO_JORNADA_DESP j "
					+ "inner join BO_HORARIO_DESP h "
					+ "on j.ID_HOR_DESP =  h.ID_HOR_DESP "
					+ "where j.fecha=? and SUBSTR((char(time(h.hini),USA)),7,2)=? " 
					//+ "and ? between h.hini and h.hfin "
					+ "and j.id_zona = ?  ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1,fecha);
			stm.setString(2,hora);
			//stm.setLong(3,idLocal);
			stm.setLong(3, zonaXComuna.getZvmIdZona());
			rs2 = stm.executeQuery();
			while (rs2.next()) {
				idJornadaDespacho=rs2.getLong("ID_JDESPACHO");
			}
			jornada.setId_jornada(idJornadaDespacho); 		

		} catch (SQLException e) {
			throw new PedidosDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (rs2 != null)
					rs2.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDatosJornada - Problema SQL (close)", e);
			}

		}
		return jornada;
	}

	public int getPoligonoVentaMasivaPorComuna(long comuna_id) throws PedidosDAOException, ParseException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int idpoligono=0;
		try {
			conn = this.getConnection();		
			String sql = "SELECT ID_POLIGONO FROM bo_poligono p " +
						" where ID_ZONA = ( " +
						" select zvm_id_zona from BO_ZONASXCOMUNA_VENTA_MASIVA where zvm_ID_COMUNA = ?) ";
			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setLong(1, comuna_id);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()){
				idpoligono = rs.getInt("ID_POLIGONO");
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PedidosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();				
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPoligonoVentaMasivaPorComuna - Problema SQL (close)", e);
			}						
		}
		return idpoligono;
	}
}