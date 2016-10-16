package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.common.model.ProductosPedidoEntity;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.pedidos.collaboration.ProcSectoresJornadaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcZonasJornadaDTO;
import cl.bbr.jumbocl.pedidos.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.pedidos.dto.ComandaPreparadosDTO;
import cl.bbr.jumbocl.pedidos.dto.ComandaVerifStockDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.dto.SemanaJornadaPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TotProdSecJorDTO;
import cl.bbr.jumbocl.pedidos.dto.TotProdZonJorDTO;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Clase que permite consultar las jornadas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcJornadasDAO implements JornadasDAO {

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
		return this.conn;

	}
	
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
            	logger.error(e.getMessage(),e);
            }
		}
			
	}

	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws JornadasDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws JornadasDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new JornadasDAOException(e);
		}
	}
	
	
	/**
	 * Obtiene detalle de una jornada de picking
	 * 
	 * @param  id_jornada long 
	 * @return JornadaDTO
	 * @throws JornadasDAOException 
	 */
	public JornadaDTO getJornadaById(long id_jornada) throws JornadasDAOException {

		
		PreparedStatement stm = null;
		ResultSet rs = null;
		JornadaDTO jornada = new JornadaDTO();

		logger.debug("Ejecutando DAO getJornadaById");
		
		try {
			
			conn = this.getConnection();
			String sql = " SELECT count(p.id_jpicking) as cant_op, SUM(cant_productos) as cant_prods,  j.id_jpicking,  j.id_local,  j.id_hor_pick, " +
				" j.id_semana id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre as estado, hini, hfin, j.id_estado AS id_estado, " +
				" j.hrs_validacion  hrs_validacion, j.hrs_ofrecido_web hrs_ofrecido_web" +
				" FROM bo_jornadas_pick j " +
				" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
				" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " +
				" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " +
				" WHERE j.id_jpicking = ? " +
				" GROUP BY j.id_jpicking,  j.id_local,  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre, " +
				" hini, hfin, j.id_estado, j.hrs_validacion  , j.hrs_ofrecido_web " +
				" ORDER BY id_hor_pick ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
	
			stm.setLong(1, id_jornada);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				jornada.setId_local(rs.getLong("id_local"));
				jornada.setCant_ped_en_bodega(0);
				jornada.setCant_ped_en_picking(0);
				jornada.setCant_ped_por_pickear(0);
				jornada.setCant_pedidos(rs.getInt("cant_op"));
				jornada.setCant_prod_en_bodega(0);
				jornada.setCant_prod_sin_asignar(0);
				jornada.setCapac_picking_max(rs.getInt("capac_picking"));
				jornada.setCapac_picking_ut(rs.getInt("capac_ocupada"));
				jornada.setEstado(rs.getString("estado"));
				jornada.setF_jornada(rs.getString("fecha"));
				jornada.setH_fin(rs.getTime("hfin").toString());
				jornada.setH_inicio(rs.getTime("hini").toString());
				jornada.setId_jornada(rs.getLong("id_jpicking"));
				jornada.setId_estado(rs.getLong("id_estado"));
				jornada.setPorc_prod_en_bodega(0);
				jornada.setPorc_prod_sin_asignar(0.0);
				jornada.setPorc_utilizacion(0.0);
				jornada.setHrs_validacion(rs.getInt("hrs_validacion"));
				jornada.setHrs_ofrecido_web(rs.getInt("hrs_ofrecido_web"));
				jornada.setId_semana(rs.getLong("id_semana"));
				jornada.setDow(rs.getInt("dow"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
		return jornada;
		
	}
	
	
	
	/**
	 * Obtiene listado de jornadas por fecha
	 * 
	 * @param  fecha Date 
	 * @param  id_local long 
	 * @return List MonitorJornadasDTO
	 * @throws JornadasDAOException 
	 */
	public List getJornadasPickingByFecha(Date fecha, long id_local) throws JornadasDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt =
			" SELECT count(p.id_jpicking) as cant_op, " +
			"       SUM(cant_productos) as cant_prods,  " +
			"       j.id_jpicking,  " +
			"       j.id_local,  " +
			"       j.id_hor_pick, " +
			"       j.id_semana, " +
			"       dow, " +
			"       fecha, " +
			"       capac_picking,  " +
			"       capac_ocupada, " +
			"       e.nombre as estado, " +
			"       hini, " +
			"       hfin " +
			" FROM bo_jornadas_pick j " +
			" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
			" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " +
			" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " +
			" WHERE fecha = ? AND j.id_local = ? " +
			" GROUP BY j.id_jpicking,  j.id_local,  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre, hini, hfin " +
			" ORDER BY id_hor_pick ";
		
		logger.debug("Ejecutando DAO getJornadasPickingByFecha");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_local: " + id_local);
		logger.debug("fecha: " + fecha.toString());
		
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );

			stm.setObject(1, new java.sql.Date(fecha.getTime()));
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorJornadasDTO jor1 = new MonitorJornadasDTO();
				jor1.setId_jornada(rs.getLong("id_jpicking"));
				jor1.setId_local(rs.getLong("id_local"));
				jor1.setHinicio(rs.getTime("hini").toString());
				jor1.setHfin(rs.getTime("hfin").toString());
				jor1.setEstado(rs.getString("estado"));
				jor1.setCant_op(rs.getInt("cant_op"));
				jor1.setCant_prods(rs.getInt("cant_prods"));
				jor1.setPorc_av_op(0.0);
				jor1.setPorc_av_prods(0.0);
				result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene jornadas de acuerdo a criterios de consulta
	 * 
	 * @param  criterio JornadaCriteria 
	 * @param  id_local long 
	 * @return List MonitorJornadasDTO
	 * @throws JornadasDAOException 
	 */	
	public List getJornadasPickingByCriteria(JornadaCriteria criterio, long id_local)
		throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getJornadasPickingByCriteria");
		
		String fjornada = "";
		long id_jornada = 0;
		
		fjornada = criterio.getF_jornada();
		id_jornada = criterio.getId_jornada();
		//obtener los estados de pedidos que no se consideran en la consulta
		String[] filtro_estados = criterio.getEst_pedido_no_mostrar();
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		//mostrar los pedidos q no se encuentren en los estados q se definen en sqlEstados.   
		String sqlEstados ="";
		//if ( !estados_in.equals("") ){ sqlEstados = " AND p.id_estado NOT IN (" + estados_in + ") " ; }
		if ( !estados_in.equals("") ){ sqlEstados = " AND p.id_estado IN (" + estados_in + ") " ; }
		
		//Maxbell - Esta query considera los decimales
		String SQLStmt =
			//" SELECT count(distinct(p.id_pedido)) as cant_op, SUM(CANT_SOLIC) as cant_prods,  j.id_jpicking,  j.id_local, " +
			//Maxbell - Homologacion pantallas BOL
			" SELECT count(distinct(p.id_pedido)) as cant_op, SUM((cast(CANT_SOLIC as int)) - (cast(CANT_PICK as int))) AS CANT_PRODS,  j.id_jpicking,  j.id_local, " +
			"  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre as estado, hini, hfin " +
			" FROM bo_jornadas_pick j " +
			" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
			" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " + 
			" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " + sqlEstados +
			" LEFT JOIN BO_DETALLE_PEDIDO dp ON dp.id_pedido = p.id_pedido "+
			" WHERE j.id_local = ? ";
		
		//Maxbell - Esta query no considera los decimales
//		String SQLStmt =
//				" SELECT count(p.id_jpicking) as cant_op, SUM(cant_productos) as cant_prods,  j.id_jpicking,  j.id_local, " +
//				"  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre as estado, hini, hfin " +
//				" FROM bo_jornadas_pick j " +
//				" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
//				" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " + 
//				" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " + sqlEstados +
//				" WHERE j.id_local = ? ";
		
		if(fjornada != null)
			SQLStmt = SQLStmt + " AND fecha = '" + fjornada + "' ";
		
		if(id_jornada > 0 )
			SQLStmt = SQLStmt + " AND j.id_jpicking = " + id_jornada + " ";		
		
		
		//SQLStmt = SQLStmt + " AND (P.TIPO_VE = 'N' OR P.TIPO_VE IS NULL) ";	
		
		SQLStmt +=
			" GROUP BY j.id_jpicking,  j.id_local,  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre, hini, hfin " +
			" ORDER BY id_hor_pick "
			;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
		
			stm.setLong(1, id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorJornadasDTO jor1 = new MonitorJornadasDTO();
				jor1.setId_jornada(rs.getLong("id_jpicking"));
				jor1.setId_local(rs.getLong("id_local"));
				jor1.setHinicio(rs.getTime("hini").toString());
				jor1.setHfin(rs.getTime("hfin").toString());
				jor1.setEstado(rs.getString("estado"));
				jor1.setCant_op(rs.getInt("cant_op"));
				jor1.setCant_prods(rs.getInt("cant_prods"));
				jor1.setPorc_av_op(0.0);
				jor1.setPorc_av_prods(0.0);
				jor1.setFecha(rs.getDate("fecha").toString());
				result.add(jor1);
				//calcularDiferencia(jor1.getId_jornada());
			}

			

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	
	
	//Maxbell arreglo homologacion
	public List getJornadasPickingByCriteriaEspeciales(JornadaCriteria criterio, long id_local)
			throws JornadasDAOException {
			
			List result = new ArrayList();
			
			PreparedStatement stm = null;
			ResultSet rs = null;
			logger.debug("Ejecutando DAO getJornadasPickingByCriteria");
			
			String fjornada = "";
			long id_jornada = 0;
			
			fjornada = criterio.getF_jornada();
			id_jornada = criterio.getId_jornada();
			//obtener los estados de pedidos que no se consideran en la consulta
			String[] filtro_estados = criterio.getEst_pedido_no_mostrar();
			String estados_in = "";
			if ( filtro_estados != null && filtro_estados.length > 0 ){
				for (int i=0; i<filtro_estados.length; i++){
					estados_in += "," + filtro_estados[i];
				}
				estados_in = estados_in.substring(1);
			}
			
			//mostrar los pedidos q no se encuentren en los estados q se definen en sqlEstados.   
			String sqlEstados ="";
			//if ( !estados_in.equals("") ){ sqlEstados = " AND p.id_estado NOT IN (" + estados_in + ") " ; }
			if ( !estados_in.equals("") ){ sqlEstados = " AND p.id_estado IN (" + estados_in + ") " ; }
			
			//Maxbell - Esta query considera los decimales
			String SQLStmt =
				//" SELECT count(distinct(p.id_pedido)) as cant_op, SUM(CANT_SOLIC) as cant_prods,  j.id_jpicking,  j.id_local, " +
				//Maxbell - Homologacion pantallas BOL
				" SELECT count(distinct(p.id_pedido)) as cant_op, SUM((cast(CANT_SOLIC as int)) - (cast(CANT_PICK as int))) AS CANT_PRODS,  j.id_jpicking,  j.id_local, " +
				"  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre as estado, hini, hfin " +
				" FROM bo_jornadas_pick j " +
				" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
				" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " + 
				" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " + sqlEstados +
				" LEFT JOIN BO_DETALLE_PEDIDO dp ON dp.id_pedido = p.id_pedido "+
				" WHERE j.id_local = ? ";
			
			//Maxbell - Esta query no considera los decimales
//			String SQLStmt =
//					" SELECT count(p.id_jpicking) as cant_op, SUM(cant_productos) as cant_prods,  j.id_jpicking,  j.id_local, " +
//					"  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre as estado, hini, hfin " +
//					" FROM bo_jornadas_pick j " +
//					" LEFT JOIN bo_estados e on  e.id_estado = j.id_estado " +
//					" JOIN bo_horario_pick h on h.id_hor_pick=j.id_hor_pick " + 
//					" LEFT JOIN bo_pedidos p ON p.id_jpicking = j.id_jpicking " + sqlEstados +
//					" WHERE j.id_local = ? ";
			
			if(fjornada != null)
				SQLStmt = SQLStmt + " AND fecha = '" + fjornada + "' ";
			
			if(id_jornada > 0 )
				SQLStmt = SQLStmt + " AND j.id_jpicking = " + id_jornada + " ";		
			
			
			//SQLStmt = SQLStmt + " AND (P.TIPO_VE = 'N' OR P.TIPO_VE IS NULL) ";	
			SQLStmt = SQLStmt + " AND P.TIPO_VE = 'S' ";	
			
			SQLStmt +=
				" GROUP BY j.id_jpicking,  j.id_local,  j.id_hor_pick, j.id_semana, dow, fecha, capac_picking, capac_ocupada, e.nombre, hini, hfin " +
				" ORDER BY id_hor_pick "
				;
			
			logger.debug("SQL: " + SQLStmt);
			
			try {
	
				conn = this.getConnection();
				
				stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
				stm.setLong(1, id_local);
				
				rs = stm.executeQuery();
				while (rs.next()) {
					MonitorJornadasDTO jor1 = new MonitorJornadasDTO();
					jor1.setId_jornada(rs.getLong("id_jpicking"));
					jor1.setId_local(rs.getLong("id_local"));
					jor1.setHinicio(rs.getTime("hini").toString());
					jor1.setHfin(rs.getTime("hfin").toString());
					jor1.setEstado(rs.getString("estado"));
					jor1.setCant_op(rs.getInt("cant_op"));
					jor1.setCant_prods(rs.getInt("cant_prods"));
					jor1.setPorc_av_op(0.0);
					jor1.setPorc_av_prods(0.0);
					jor1.setFecha(rs.getDate("fecha").toString());
					result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	
	
	public int calcularDiferenciaJornada(long id_jpicking) {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cantidad = 0;
		try {
			String sql="select id_pedido, SUM(cast(CANT_SOLIC as int) - cast(CANT_PICK as int)) AS CANT_PRODS "+
					"from bodba.bo_detalle_pedido  "+
					"where id_pedido in (select id_pedido from BO_PEDIDOS where ID_JPICKING = ? and id_estado in(5,6,7,8,9,10,21,54,70))  "+
					"group by id_pedido  "+
					"HAVING SUM(cast(CANT_SOLIC as int))  < SUM(cast(CANT_PICK as int)) ";
			conn = this.getConnection();
	
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_jpicking);
			rs = stm.executeQuery();
			while (rs.next()) {
				cantidad += rs.getInt("CANT_PRODS");
			}	
		} catch(Exception ex) {
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
	
	
	/**
	 * Obtiene listado con las jornadas de despacho para una semana determinada en un local
	 * para ser utilizado en el calendario de despacho
	 * 
	 * @param  id_semana long 
	 * @param  id_local long 
	 * @return List JornadaPickingEntity
	 * @throws JornadasDAOException 
	 */
	public List getJornadasPickingByIdSemana(long id_semana, long id_local)	throws JornadasDAOException {
	
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getCalendarioJornadasPicking");
		
		try {
			String cadQuery=" SELECT id_jpicking, id_local, id_hor_pick, id_semana, dow, fecha, capac_picking, capac_ocupada, " +
							" hrs_validacion, hrs_ofrecido_web " +
							" FROM bo_jornadas_pick " +
							" WHERE id_semana = ? AND id_local = ? " +
							" ORDER BY fecha, id_hor_pick";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery + " WITH UR");

			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			
			List idsJpicking = new ArrayList();
			Hashtable hash = new Hashtable();
			while (rs.next()) {
				JornadaPickingEntity jor1 = new JornadaPickingEntity();
				idsJpicking.add(new Long(rs.getLong("id_jpicking")));
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setId_local(rs.getLong("id_local"));
				jor1.setId_hor_picking(rs.getLong("id_hor_pick"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				//jor1.setCapac_ocupada(rs.getLong("capac_ocupada"));
				jor1.setHrs_validacion(rs.getInt("hrs_validacion"));
				jor1.setHrs_ofrecido_web(rs.getInt("hrs_ofrecido_web"));
				result.add(jor1);
				hash.put(new Long(jor1.getId_jpicking()), jor1);
			}
			
			if (idsJpicking.size() > 0) {
				// Maxbell - Homologacion pantallas BOL
				// String sql =
				// "select SUM(cast(CANT_SOLIC as int)) from BO_DETALLE_PEDIDO where id_pedido in "
				// +
				// "(select id_pedido from bo_pedidos where id_jpicking in " +
				// DString.join(idsJpicking) +
				// " and id_estado in(5,6,7,8,10))";

				String sqlPorPickear = "select p.id_jpicking , SUM((cast(CANT_SOLIC as int))- (cast(CANT_PICK as int))) as capac_ocup  from "
						+ "BO_DETALLE_PEDIDO dp "
						+ "JOIN BO_PEDIDOS p on dp.id_pedido = p.id_pedido "
						+ "where p.id_jpicking in "
						+ DString.join(idsJpicking)
						+ " "
						+ "and p.id_estado in("
						//+ Constantes.ESTADOS_PEDIDO_PARA_PICKING
						+ Constantes.ESTADOS_PICKEADOS_FINALIZADOS
						+ ") "
						//+" AND (p.TIPO_VE = 'N' or p.TIPO_VE is null) "
						+" AND p.TIPO_VE <> 'S' "
						+ "GROUP BY p.id_jpicking ";
				stm = conn.prepareStatement(sqlPorPickear + " WITH UR");
				rs = stm.executeQuery();
				while (rs.next()) {
					long id = rs.getLong("id_jpicking");
					JornadaPickingEntity jpe = (JornadaPickingEntity) hash
							.get(new Long(id));
					if (jpe != null) {
						jpe.setCapac_ocupada(rs.getLong("capac_ocup"));
					}
				}

				String sqlPickeados = "SELECT JP.ID_JPICKING, SUM(DP.CANT_SOLIC) AS CAT_SOLIC, COALESCE(SUM(DP.CANT_PICK), 0) AS CANT_PICK, COALESCE(SUM(DP.CANT_FALTAN), 0) AS CANT_FALT  "+
						"FROM BODBA.BO_JORNADAS_PICK JP  "+
						"        JOIN BODBA.BO_PEDIDOS P  "+
						"        ON P.ID_JPICKING = JP.ID_JPICKING  "+
						//"        AND P.ID_ESTADO NOT IN (1,20,68,69,7,3,4)  "+
						//Maxbell arreglo homologacion
						"        AND P.ID_ESTADO NOT IN (1,20,68,69,3,4)  "+
						"            JOIN BODBA.BO_DETALLE_PEDIDO DP  "+
						"            ON DP.ID_PEDIDO = P.ID_PEDIDO  "+
						"WHERE JP.ID_JPICKING IN "+ DString.join(idsJpicking) +" " +
						//Filtro maldito
						"AND p.TIPO_VE <> 'S' "+
								//"AND (p.TIPO_VE = 'N' or p.TIPO_VE is null) "+
								"GROUP BY JP.ID_JPICKING ";
				
//				String sqlPickeados = "select p.id_jpicking, SUM(cast(dp.CANT_PICK as int)) as capac_pickeada from BO_DETALLE_PICKING dp "
//						+ "JOIN bo_pedidos p on dp.id_pedido = p.id_pedido "
//						+ "where p.id_jpicking in "
//						+ DString.join(idsJpicking)
//						+ " " + "group by p.ID_JPICKING ";
				
				stm = conn.prepareStatement(sqlPickeados + " WITH UR");
				rs = stm.executeQuery();
				while (rs.next()) {
					long id = rs.getLong("id_jpicking");
					JornadaPickingEntity jpe = (JornadaPickingEntity) hash
							.get(new Long(id));
					if (jpe != null) {
						jpe.setCapac_pickeada(rs.getLong("CANT_PICK"));
					}
				}
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene listado con las jornadas de picking para una semana, un local y una zona determinada
	 * para ser utilizado en el calendario de despacho masivo
	 * 
	 * @param  id_semana long
	 * @param  id_local long
	 * @param  id_zona long
	 * @return List JornadaPickingEntity
	 * @throws JornadasDAOException 
	 */
	public List getListaJornadasPickingByIdSemanaLocalZona(long id_semana, long id_local, long id_zona)
			throws JornadasDAOException {
	
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getCalendarioJornadasPicking");
		
		try {

			conn = this.getConnection();

			String SQL =" SELECT id_jpicking, id_local, id_hor_pick, id_semana, dow, fecha, capac_picking, capac_ocupada, " +
						" hrs_validacion, hrs_ofrecido_web " +
						" FROM bo_jornadas_pick " +
						" WHERE id_semana = ? AND id_local = ? " +
						" ORDER BY fecha, id_hor_pick";	
						
			stm = conn.prepareStatement(SQL + " WITH UR");

			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				JornadaPickingEntity jor1 = new JornadaPickingEntity();
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setId_local(rs.getLong("id_local"));
				jor1.setId_hor_picking(rs.getLong("id_hor_pick"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				jor1.setCapac_ocupada(rs.getLong("capac_ocupada"));
				jor1.setHrs_validacion(rs.getInt("hrs_validacion"));
				jor1.setHrs_ofrecido_web(rs.getInt("hrs_ofrecido_web"));
				result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene listado de jornadas de picking para un horario
	 * 
	 * @param  id_hor_pick long 
	 * @return List JornadaPickingEntity
	 * @throws JornadasDAOException 
	 */
	public List getJornadasByIdHorario(long id_hor_pick)
		throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getJornadasByIdHorario");

		try {
			String cadquery = 	" SELECT id_jpicking, id_local, id_hor_pick, id_semana, dow, fecha, " +
								" capac_picking, capac_ocupada, hrs_validacion, hrs_ofrecido_web " +
								" FROM bo_jornadas_pick " +
								" WHERE id_hor_pick = ? " +
								" ORDER BY fecha";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadquery + " WITH UR");

			stm.setLong(1, id_hor_pick);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				JornadaPickingEntity jor1 = new JornadaPickingEntity();
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setId_local(rs.getLong("id_local"));
				jor1.setId_hor_picking(rs.getLong("id_hor_pick"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				jor1.setCapac_ocupada(rs.getLong("capac_ocupada"));
				jor1.setHrs_validacion(rs.getInt("hrs_validacion"));
				jor1.setHrs_ofrecido_web(rs.getInt("hrs_ofrecido_web"));
				result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Inserta registro en tabla bo_jornadas_picking
	 * 
	 * @param  id_local long 			
	 * @param  id_hor_pick long 
	 * @param  id_semana long 
	 * @param  dow int 				día de la semana (1:lunes, ..., 7:domingo)
	 * @param  fecha Date 
	 * @param  capac_picking int 
	 * @param  horas_val int 
	 * @param  horas_web int 
	 * @return long, nuevo id
	 * @throws JornadasDAOException 
	 */
	public long doInsJornadaPicking(long id_local, long id_hor_pick, long id_semana, int dow, Date fecha, int capac_picking, int horas_val, int horas_web)
		throws JornadasDAOException {

		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int	rc;
		long id = -1;

		String SQLStmt = 
				" INSERT INTO bo_jornadas_pick (id_local, id_hor_pick, id_semana, id_estado, dow, fecha, capac_picking, capac_ocupada, hrs_validacion, hrs_ofrecido_web) " +
				" VALUES (?,?,?,?,?,?,?,0,?,?)"
				;
		
		logger.debug("Ejecutando DAO InsJornadaPicking");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("values:"+id_local+", "+id_hor_pick+", "+id_semana+", est, "+dow+", fecha, "+capac_picking+", 0, "+horas_val+", "+horas_web);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt, Statement.RETURN_GENERATED_KEYS );

			stm.setLong(1,id_local);
			stm.setLong(2,id_hor_pick);
			stm.setLong(3,id_semana);
			stm.setInt(4,Constantes.ID_ESTADO_JORNADA_NO_INICIADA);
			stm.setInt(5,dow);
			stm.setObject(6, new java.sql.Date(fecha.getTime()));
			stm.setInt(7,capac_picking);
			stm.setInt(8,horas_val);
			stm.setInt(9,horas_web);
			
						
			rc = stm.executeUpdate();
			
			logger.debug("rc: " + rc);
			
	        rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getLong(1);
	        }
	        

		} catch (SQLException e) {
			logger.debug("Problema en InsJornadaPicking:"+ e);
			throw new JornadasDAOException(e);
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
	 * Borra jornadas de picking de un horario
	 * 
	 * @param  id_hor_pick long 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doDelJornadaPicking(long id_hor_pick) throws JornadasDAOException {

		PreparedStatement stm = null;
		int	rc;
		logger.debug("Ejecutando DAO DelJornadaPicking");

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(" DELETE FROM bo_jornadas_pick WHERE id_hor_pick = ? ");
			stm.setLong(1,id_hor_pick);
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
			
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
	 * Modifica la información de una jornada
	 * 
	 * @param  id_jpicking long 
	 * @param  capac_picking int 
	 * @param  hrs_val int 
	 * @param  hrs_web int 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doModJornadaPicking(long id_jpicking, int capac_picking, int hrs_val, int hrs_web) throws JornadasDAOException{
		
		
		PreparedStatement stm = null;
		int	rc;

		logger.debug("Ejecutando DAO ModJornadaPicking");
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement(
					" UPDATE bo_jornadas_pick " +
					" SET capac_picking = ?, hrs_validacion =?, hrs_ofrecido_web =? " +
					" WHERE id_jpicking = ? "		
					);

			stm.setInt(1,capac_picking);
			stm.setInt(2,hrs_val);
			stm.setInt(3,hrs_web);
			stm.setLong(4,id_jpicking);
						
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			

		} catch (Exception e) {
			logger.debug("Problema en ModJornadaPicking:"+ e);
			throw new JornadasDAOException(e);
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
	 * Incrementa la ocupación de una jornada de picking en una cantidad determinada
	 * (si el valor es negativo, lo decrementa)
	 * 
	 * @param  id_jpicking long 
	 * @param  incremento int 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doOcupaCapacidadPicking(long id_jpicking, int incremento) throws JornadasDAOException{
		
		
		PreparedStatement stm = null;
		int capacidadOcupada = 0;
		int	rc;

		String SQLStmt = "";		
		
		try {

			conn = this.getConnection();

			//Maxbell - inconsistencias v3 
			//Este es un flag en bo_parametros para desactivar el cambio hecho se debe ejecutar la siguientes querys
			//update bo_parametro set valor = 'FALSE' where nombre = 'ACTUALIZAR_CAPACIDAD'
			if(actualizarCapacidadPorQuery("ACTUALIZAR_CAPACIDAD_PICKING")) {
				capacidadOcupada = ObtenerCapacidadOcupada(id_jpicking, incremento);
				
				stm = conn.prepareStatement(" UPDATE bo_jornadas_pick " +
						" SET capac_ocupada = ? " +
						" WHERE id_jpicking = ? ");
				stm.setInt(1,capacidadOcupada);
				stm.setLong(2,id_jpicking);

			} else {
				stm = conn.prepareStatement("UPDATE bo_jornadas_pick " +
						" SET capac_ocupada = ( capac_ocupada + ? ) " +
						" WHERE id_jpicking = ? ");
				stm.setInt(1,incremento);
				stm.setLong(2,id_jpicking);
			}
						
			rc = stm.executeUpdate();

		} catch (SQLException e) {
			logger.debug("Problema en ModJornadaPicking:"+ e);
			throw new JornadasDAOException(e);
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
	
	public int ObtenerCapacidadOcupada(long id_jpicking,int incremento) {
		int capacidadOcupada = 0;
		PreparedStatement stmCapacOcupada = null;
		ResultSet rs = null;
		
		String SQLObtenerCapacidadOcupada = "select SUM(CANT_PRODUCTOS) as capac_ocup from BO_PEDIDOS where ID_JPICKING = ? AND TIPO_VE <> 'S' "
				+ "and id_estado not in("
				+ Constantes.ESTADOS_PEDIDO_NULOS
				+ ") ";
		try {

			conn = this.getConnection();
			
			stmCapacOcupada = conn.prepareStatement(SQLObtenerCapacidadOcupada);
			stmCapacOcupada.setLong(1, id_jpicking);
			rs = stmCapacOcupada.executeQuery();
			if(rs.next()) {
				capacidadOcupada = rs.getInt("capac_ocup");
			}
			
			if(capacidadOcupada <= 0) {
				capacidadOcupada = incremento;
			}

		} catch (SQLException e) {
			logger.error("Problema en ModJornadaPicking:"+ e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
				}	
				if (stmCapacOcupada != null) {
					stmCapacOcupada.close();
				}					
				releaseConnection();
			} catch (SQLException e) {}
		}
		return capacidadOcupada;
	}
		
	/*
	 * Jornadas de Despacho
	 ***********************************************************/
	
	/**
	 * Obtiene las jornadas de despacho de una semana para una zona
	 * 
	 * @param  id_semana long 
	 * @param  id_zona long 
	 * @return List of JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasDespachoByIdSemana(long id_semana, long id_zona) throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt =
				" SELECT d.id_jdespacho, id_zona, d.id_hor_desp, d.id_semana, d.id_jpicking, d.dow, d.fecha, " +
				" d.capac_despacho, d.capac_ocupada AS capac_ocupada_despacho, d.tarifa_express, d.tarifa_normal, d.tarifa_economica, d.tarifa_umbral, " +
				" p.capac_picking, p.capac_ocupada AS capac_ocupada_picking, " +
				" p.hrs_validacion AS hrs_validacion, p.hrs_ofrecido_web AS hrs_web, p.fecha fecha_pick, " +
				" h.hini AS hini, h.hfin AS hfin " +
				" FROM bo_jornada_desp d " +
				" JOIN bo_jornadas_pick p ON p.id_jpicking=d.id_jpicking " +
				" JOIN bo_horario_pick h ON h.id_hor_pick = p.id_hor_pick " +
				" WHERE d.id_semana = ? " +
				" AND id_zona = ? " +
				" ORDER BY fecha"
				;
		
		logger.debug("Ejecutando DAO getJornadasDespachoByIdSemana");
		logger.debug("SQL: " + SQLStmt);
		
		logger.debug("DAO id_Semana: " + id_semana);
		logger.debug("DAO id_zona: " + id_zona);
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");

			stm.setLong(1, id_semana);
			stm.setLong(2, id_zona);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				JornadaDespachoEntity jor1 = new JornadaDespachoEntity();
				jor1.setId_jdespacho(rs.getLong("id_jdespacho"));
				jor1.setId_zona(rs.getLong("id_zona"));
				jor1.setId_hor_desp(rs.getLong("id_hor_desp"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_despacho(rs.getLong("capac_despacho"));
				jor1.setCapac_despacho_ocupada(rs.getLong("capac_ocupada_despacho"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				jor1.setCapac_picking_ocupada(rs.getLong("capac_ocupada_picking"));
				jor1.setTarifa_express(rs.getInt("tarifa_express"));
				jor1.setTarifa_normal(rs.getInt("tarifa_normal"));
				jor1.setTarifa_economica(rs.getInt("tarifa_economica"));
				jor1.setTarifa_umbral(rs.getInt("tarifa_umbral"));
				jor1.setHrs_validacion(rs.getInt("hrs_validacion"));
				jor1.setHrs_ofrecido_web(rs.getInt("hrs_web"));
				jor1.setHoraIniPicking(rs.getTime("hini"));
				jor1.setHoraFinPicking(rs.getTime("hfin"));
				jor1.setFecha_picking(rs.getDate("fecha_pick"));
				result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Retorna listado con jornadas de despacho para un horario
	 * 
	 * @param  id_hor_desp long 
	 * @return List JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasDespachoByIdHorario(long id_hor_desp) throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt =
			" SELECT d.id_jdespacho, id_zona, d.id_hor_desp, d.id_semana, d.id_jpicking, d.dow, d.fecha, " +
			" d.capac_despacho, d.capac_ocupada AS capac_ocupada_despacho, d.tarifa_express, d.tarifa_normal, d.tarifa_economica, d.tarifa_umbral, " +
			" p.capac_picking, p.capac_ocupada AS capac_ocupada_picking " +
			" FROM bo_jornada_desp d " +
			" JOIN bo_jornadas_pick p ON p.id_jpicking=d.id_jpicking " +
			" WHERE d.id_hor_desp = ? " +
			" ORDER BY fecha"
			;
	
		logger.debug("Ejecutando DAO getJornadasDespachoByIdHorario");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_hor_desp" + id_hor_desp);


		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );

			stm.setLong(1, id_hor_desp);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				JornadaDespachoEntity jor1 = new JornadaDespachoEntity();
				jor1.setId_jdespacho(rs.getLong("id_jdespacho"));
				jor1.setId_zona(rs.getLong("id_zona"));
				jor1.setId_hor_desp(rs.getLong("id_hor_desp"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_despacho(rs.getLong("capac_despacho"));
				jor1.setCapac_despacho_ocupada(rs.getLong("capac_ocupada_despacho"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				jor1.setCapac_picking_ocupada(rs.getLong("capac_ocupada_picking"));
				jor1.setTarifa_express(rs.getInt("tarifa_express"));
				jor1.setTarifa_normal(rs.getInt("tarifa_normal"));
				jor1.setTarifa_economica(rs.getInt("tarifa_economica"));
				jor1.setTarifa_umbral(rs.getInt("tarifa_umbral"));
				result.add(jor1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Inserta registro en tabla bo_jornada_desp
	 * 
	 * @param  id_zona long 
	 * @param  id_hor_desp long 
	 * @param  id_semana long 
	 * @param  id_jpicking long 
	 * @param  dow int 
	 * @param  fecha Date 
	 * @param  capac_despacho int 
	 * @param  precio int 
	 * @return  long, nuevo id
	 * @throws JornadasDAOException
	 */
	public long doInsJornadaDespacho(long id_zona, long id_hor_desp, long id_semana, long id_jpicking, int dow, Date fecha, int capac_despacho, int tarifa_express, int tarifa_normal, int tarifa_economica, int tarifa_umbral)
		throws JornadasDAOException {

		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int	rc;
		long id = -1;
		
		String SQLStmt =
				" INSERT INTO bo_jornada_desp (id_zona, id_hor_desp, id_semana, id_jpicking, dow, " +
				" id_estado, fecha, capac_despacho, capac_ocupada, tarifa_express, tarifa_normal, tarifa_economica, tarifa_umbral) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"	;
		
		logger.debug("Ejecutando DAO InsJorndaDespacho");
		logger.debug("SQL             : " + SQLStmt);
		logger.debug("id_zona         : " + id_zona);
		logger.debug("id_hor_desp     : " + id_hor_desp);
		logger.debug("id_semana       : " + id_semana);
		logger.debug("id_jpicking     : " + id_jpicking);
		logger.debug("dow             : " + dow);
		logger.debug("capac_despacho  : " + capac_despacho);
		logger.debug("tarifa_express  : " + tarifa_express);
		logger.debug("tarifa_normal   : " + tarifa_normal);
		logger.debug("tarifa_economica: " + tarifa_economica);
		logger.debug("tarifa_umbral	  :	" + tarifa_umbral);
		
		
				
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt , Statement.RETURN_GENERATED_KEYS );

			stm.setLong(1, id_zona);
			stm.setLong(2, id_hor_desp);
			stm.setLong(3, id_semana);
			stm.setLong(4, id_jpicking);
			stm.setInt(5, dow);
			stm.setLong(6, Constantes.ID_ESTADO_JORNADA_NO_INICIADA);
			stm.setObject(7, new java.sql.Date(fecha.getTime()));
			stm.setInt(8, capac_despacho);
			stm.setInt(9, 0); // capac_ocupada, inicialmente es cero
			stm.setInt(10, tarifa_express);
			stm.setInt(11, tarifa_normal);
			stm.setInt(12, tarifa_economica);
			stm.setInt(13, tarifa_umbral);
			
			rc = stm.executeUpdate();
			logger.debug("rc = " +  rc);
	        rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getLong(1);
	        }

		} catch (SQLException e) {
			logger.debug("Problema en InsJorndaDespacho:"+ e);
			throw new JornadasDAOException(e);
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
	 * Elimina jornadas de picking de un horario
	 * 
	 * @param  id_hor_desp long 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doDelJornadaDespacho(long id_hor_desp)
		throws JornadasDAOException {
		
		
		PreparedStatement stm = null;
		int	rc;
		
		logger.debug("Ejecutando DAO DelJornadaDespacho");

		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(
					" DELETE FROM bo_jornada_desp " +
					" WHERE id_hor_desp = ?"		
					);

			stm.setLong(1,id_hor_desp);
						
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
	 * Modifica datos de una jornada de despacho
	 * 
	 * @param  id_jdespacho long 
	 * @param  id_jpicking long 
	 * @param  capac_despacho int 
	 * @param  precio int 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doModJornadaDespacho(long id_jdespacho, long id_jpicking, int capac_despacho, int tarifa_express, int tarifa_normal, int tarifa_economica, int tarifa_umbral)
		throws JornadasDAOException {

		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = 
			"UPDATE bo_jornada_desp " +
			"SET capac_despacho = ? " +
			"   ,id_jpicking = ? " +
			"   ,tarifa_express = ? " +
			"   ,tarifa_normal = ? " +
			"   ,tarifa_economica = ? " +
			"   ,tarifa_umbral = ? " +
			"WHERE id_jdespacho = ? "	;
		
		logger.debug("Ejecutando DAO ModJornadaPicking");
		logger.debug("SQL             : " + SQLStmt);
		logger.debug("id_jdespacho    : " + id_jdespacho);
		logger.debug("id_jpicking     : " + id_jpicking);
		logger.debug("capac_despacho  : " + capac_despacho);
		logger.debug("tarifa_express  : " + tarifa_express);
		logger.debug("tarifa_normal   : " + tarifa_normal);
		logger.debug("tarifa_economica: " + tarifa_economica);
		logger.debug("tarifa_umbral	  : " + tarifa_umbral);
		
		
		try {
			conn = this.getConnection();

			stm = conn.prepareStatement( SQLStmt );

			stm.setInt(1,capac_despacho);
			stm.setLong(2,id_jpicking);
			stm.setInt(3, tarifa_express);
			stm.setInt(4, tarifa_normal);
			stm.setInt(5, tarifa_economica);
			stm.setLong(6,tarifa_umbral);
			stm.setLong(7,id_jdespacho);
		
						
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			

		} catch (SQLException e) {
			logger.debug("Problema en ModJornadaDespacho:"+ e);
			throw new JornadasDAOException(e);
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
	 * Modifica datos de una jornada de despacho
	 * 
	 * @param  id_jdespacho long 
	 * @param  id_jpicking long 
	 * @param  capac_despacho int 
	 * @param  precio int 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doModTarifaEconomicaJornadaDespachoByDiaSemana(int id_zona, String fecha, int tarifa_economica)
		throws JornadasDAOException {

		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = "UPDATE BO_JORNADA_DESP "
                       + "SET TARIFA_ECONOMICA = " + tarifa_economica + " "
                       + "WHERE ID_ZONA = " + id_zona + " "
                       + "  AND DATE(FECHA) = DATE('" + fecha + "') ";
		
		logger.debug("Ejecutando DAO doModTarifaEconomicaJornadaDespachoByDiaSemana");
		logger.debug("SQL : " + SQLStmt);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQLStmt);
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			

		} catch (SQLException e) {
			logger.debug("Problema en doModTarifaEconomicaJornadaDespachoByDiaSemana:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene el detalle del total de productos por sector y jornada
	 * 
	 * @param  id_jornada long 
	 * @param  id_sector long 
	 * @return List TotProdSecJorDTO
	 * @throws JornadasDAOException
	 */
	public List getTotalProductosSectorJornadaDetalle(long id_jornada, long id_sector)
		throws JornadasDAOException {
				
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT S.ID_SECTOR, S.NOMBRE SECTOR, COUNT(DP.ID_PRODUCTO) CANTIDAD"+
			" FROM BO_DETALLE_PEDIDO DP "+
			" JOIN BO_SECTOR S ON S.ID_SECTOR = DP.ID_SECTOR "+
			" JOIN BO_PEDIDOS PED ON PED.ID_PEDIDO   = DP.ID_PEDIDO " +
			"                    AND PED.ID_JPICKING = ? AND PED.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
			" GROUP BY S.ID_SECTOR, S.NOMBRE " +
			" ORDER BY S.NOMBRE ASC";

		logger.debug("Ejecutando DAO getTotalProductosSectorJornadaDetalle");
		logger.debug("SQL : " + Sql);	
		logger.debug("id_sector:  " + id_sector);
		
		try {
	
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1, id_jornada);		
			
			rs = stm.executeQuery();
			while (rs.next()) {
				TotProdSecJorDTO prod1 = new TotProdSecJorDTO();
				prod1.setId_sector(rs.getLong("id_sector"));
				prod1.setSector(rs.getString("sector"));
				prod1.setCant_prods(rs.getLong("cantidad"));				
				result.add(prod1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene los pedidos por zona de una jornada dada
	 * @param vars
	 * @return List TotProdZonJorDTO
	 * @throws JornadasDAOException
	 */
	public List getTotalProductosZonasJornada(ProcZonasJornadaDTO vars)	throws JornadasDAOException {
		long id_jornada;
		int flag_especiales=0;
		long id_zona;
		
		id_jornada = vars.getId_jornada();
		flag_especiales = vars.getFlag_especiales();
		id_zona = vars.getId_zona();
		
		String where_especiales ="";
		if (flag_especiales==0){//especiales
			where_especiales = " AND ped.tipo_ve = '" + Constantes.TIPO_VE_NORMAL_CTE+ "' ";			
		}		
		else{//normales
			where_especiales = " AND ped.tipo_ve = '" + Constantes.TIPO_VE_SPECIAL_CTE+ "' ";
		}
		
		
		String[] filtro_estados = Constantes.ESTADOS_PEDIDO_DESPACHO;
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
		for (int i=0; i<filtro_estados.length; i++){
		estados_in += "," + filtro_estados[i];
		}
		estados_in = estados_in.substring(1);
		}

		String where_est = "";
		if ( !estados_in.equals("") )
			where_est += " AND ped.id_estado IN (" + estados_in + ") ";

		String where_zona="";
		if (id_zona>0){
			where_zona = " AND ped.id_zona="+id_zona;
		}
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql =
			" SELECT z.id_zona zona_id,z.nombre zona_nom,z.orden_zona zona_orden, SUM(dp.cant_solic) cantidad, SUM(dp.cant_pick) cant_pick, " +
			" 		SUM(dp.cant_faltan) cant_faltan, SUM(dp.cant_spick) as cant_spick "+
			" FROM bo_detalle_pedido dp "+
			" JOIN bo_pedidos ped ON ped.id_pedido = dp.id_pedido AND ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
			" AND ped.id_jpicking = ? " + where_est  + where_especiales + where_zona +
			" JOIN bo_zonas z on z.id_zona = ped.id_zona AND z.id_local = ped.id_local " + 
			" GROUP BY z.id_zona,z.nombre ,z.orden_zona " +
			" ORDER BY z.orden_zona ASC";
		
		
		logger.debug("Ejecutando DAO getTotalProductosZonasJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_jornada" + id_jornada);
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1, id_jornada);
						
			rs = stm.executeQuery();
			while (rs.next()) {
				TotProdZonJorDTO prod1 = new TotProdZonJorDTO();
				prod1.setId_zona(rs.getLong("zona_id"));
				prod1.setZona(rs.getString("zona_nom"));
				prod1.setCant_prods(rs.getDouble("cantidad"));
				prod1.setCant_pick(rs.getDouble("cant_pick"));
				prod1.setCant_falt(rs.getDouble("cant_faltan"));
				prod1.setCant_prods_no_asig(rs.getDouble("cant_spick"));
				result.add(prod1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene la suma de los items solicitado en todos los pedidos de una jornada 
	 * agrupado por sector de picking
	 * 
	 * @param  id_jornada long 
	 * @return List TotProdSecJorDTO
	 * @throws JornadasDAOException
	 */
	public List getTotalProductosSectorJornada(ProcSectoresJornadaDTO vars)
		throws JornadasDAOException {
		
		long id_jornada;
		int flag_especiales=0;
		long id_zona;
		
		id_jornada = vars.getId_jornada();
		flag_especiales = vars.getFlag_especiales();
		id_zona = vars.getId_zona();
		
		String where_especiales ="";
		if (flag_especiales==0){//especiales
			where_especiales = " AND PED.TIPO_VE = '" + Constantes.TIPO_VE_NORMAL_CTE+ "' ";			
		}else{//normales
			where_especiales = " AND PED.TIPO_VE = '" + Constantes.TIPO_VE_SPECIAL_CTE+ "' ";
		}
		
		String[] filtro_estados = Constantes.ESTADOS_PEDIDO_DESPACHO;
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
		for (int i=0; i<filtro_estados.length; i++){
		estados_in += "," + filtro_estados[i];
		}
		estados_in = estados_in.substring(1);
		}

		String where_est = "";
		if ( !estados_in.equals("") )
			where_est += " AND PED.ID_ESTADO IN (" + estados_in + ") ";

		String where_zona="";
		if (id_zona>0){
			where_zona = " AND PED.ID_ZONA = " + id_zona;
		}
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql =
			" SELECT S.ID_SECTOR, S.NOMBRE SECTOR, SUM(DP.CANT_SOLIC) CANTIDAD, SUM(DP.CANT_PICK) CANT_PICK, " +
			" 		SUM(DP.CANT_FALTAN) CANT_FALTAN, SUM(DP.CANT_SPICK) AS CANT_SPICK "+
			" FROM BO_DETALLE_PEDIDO DP "+
			" JOIN BO_PEDIDOS PED ON PED.ID_PEDIDO = DP.ID_PEDIDO AND PED.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
		//	"          AND PED.ID_JPICKING = ? AND PED.ID_ESTADO IN " +
		//	" (" + Constantes.ESTADOS_PEDIDO_DESPACHO + ") " +
			" AND PED.ID_JPICKING = ?" + where_est  + where_especiales + where_zona +
			" JOIN BO_SECTOR S ON S.ID_SECTOR = DP.ID_SECTOR " + // AND S.ID_LOCAL = PED.ID_LOCAL " + 
			" GROUP BY S.ID_SECTOR,S.NOMBRE " +
			" ORDER BY S.NOMBRE ASC";
		
		logger.debug("Ejecutando DAO getTotalProductosSectorJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_jornada: " + id_jornada);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jornada);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				TotProdSecJorDTO prod1 = new TotProdSecJorDTO();
				prod1.setId_sector(rs.getLong("id_sector"));
				prod1.setSector(rs.getString("sector"));
				prod1.setCant_prods(rs.getDouble("cantidad"));	
				prod1.setCant_pick(rs.getDouble("cant_pick"));
				prod1.setCant_falt(rs.getDouble("cant_faltan"));
				prod1.setCant_prods_no_asig(rs.getDouble("cant_spick"));
				result.add(prod1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene la lista de comanda de preparados 
	 * agrupado por sector de picking
	 * 
	 * @param  id_jornada long 
	 * @param  id_categoria String 
	 * @return List ComandaPreparadosDTO
	 * @throws JornadasDAOException
	 */
	public List getComandaPreparados(long id_jornada, String id_categoria) 
		throws JornadasDAOException {
				
		List result 	= new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs 	= null;
		String Sql = 
			" SELECT DP.id_pedido id_pedido, CP3.descat seccion, " +
				" DP.cod_prod1 cod_sap, DP.uni_med uni_med, " +
				" DP.descripcion descr, DP.cant_solic cantidad, dp.observacion obs, p.tipo_ve tipo_ve" +
				" FROM bo_pedidos P  JOIN bo_detalle_pedido DP ON DP.id_pedido = P.id_pedido AND DP.preparable = '"+Constantes.PRODUCTO_ES_PREPARABLE+"' " +
				" JOIN bo_productos Pr ON Pr.id_producto = DP.id_producto " +
				" JOIN bo_catprod CP ON CP.id_catprod = Pr.id_catprod " +
				" JOIN bo_catprod CP1 ON CP1.id_catprod = CP.id_catprod_padre " +
				" JOIN bo_catprod CP2 ON CP2.id_catprod = CP1.id_catprod_padre" +
				" JOIN bo_catprod CP3 ON CP3.id_catprod = CP2.id_catprod_padre " +
				" WHERE P.id_jpicking = ? AND P.id_estado in ("+Constantes.ID_ESTAD_PEDIDO_EN_PICKING+","+Constantes.ID_ESTAD_PEDIDO_VALIDADO+") AND CP3.id_catprod = ?";
		
		logger.debug("Ejecutando DAO getComandaPreparados");
		logger.debug("Sql :"+Sql);
		logger.debug("id_sector" + id_categoria);
		logger.debug("id_jornada" + id_jornada);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jornada);
			stm.setString(2, id_categoria);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ComandaPreparadosDTO prod1 = new ComandaPreparadosDTO();
				prod1.setId_pedido(rs.getString("id_pedido"));
				prod1.setSector(rs.getString("seccion"));
				prod1.setCod_sap(rs.getString("cod_sap"));
				prod1.setUni_med(rs.getString("uni_med"));
				prod1.setDescr(rs.getString("descr"));
				prod1.setCantidad(rs.getString("cantidad"));
				prod1.setObs(rs.getString("obs"));
				prod1.setTipo_ve(rs.getString("tipo_ve"));
				result.add(prod1);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Obtiene la lista de comanda de preparados 
	 * agrupado por sector de picking
	 * 
	 * @param id_jornada
	 * @param id_categoria
	 * @return List ComandaVerifStockDTO
	 * @throws JornadasDAOException
	 */
	public List getComandaVerifStock(long id_jornada, long id_sector) 
	throws JornadasDAOException {
			
	List result 	= new ArrayList();
	PreparedStatement stm = null;
	ResultSet rs 	= null;

	String Sql = "SELECT S.ID_SECTOR, S.NOMBRE NOMBRE_SECCION, DP.ID_PEDIDO, "
	           + "       DP.ID_PRODUCTO, DP.COD_PROD1 COD_SAP, DP.UNI_MED, "
	           + "       DP.DESCRIPCION DESCR, DP.CANT_SOLIC CANTIDAD, P.TIPO_VE, "
	           + "       DP.OBSERVACION OBS, Z.NOMBRE ZONA_NOM, Z.ORDEN_ZONA "
               + "FROM BO_DETALLE_PEDIDO DP "  
               + "     JOIN BO_PEDIDOS      P ON P.ID_PEDIDO    = DP.ID_PEDIDO AND P.ID_JPICKING = ? "
               + "     JOIN BO_ZONAS        Z ON Z.ID_ZONA      = P.ID_ZONA "
               + "     JOIN BO_PRODUCTOS   PR ON PR.ID_PRODUCTO = DP.ID_PRODUCTO   "
               + "     JOIN BO_PROD_SECTOR PS ON PS.ID_PRODUCTO = PR.ID_PRODUCTO AND PS.ID_SECTOR = ? "
               + "     JOIN BO_SECTOR       S ON S.ID_SECTOR    = PS.ID_SECTOR   "
               + "     JOIN BO_LOCALES      L ON L.ID_LOCAL     = P.ID_LOCAL  "
               + "WHERE DP.CANT_SOLIC >= S.CANT_MIN_PRODS "
               + "  AND P.ID_ESTADO IN (" + Constantes.ID_ESTAD_PEDIDO_VALIDADO +", "
                                          + Constantes.ID_ESTAD_PEDIDO_EN_PICKING + ") " 
               + "ORDER BY Z.ORDEN_ZONA ASC";
	
	logger.debug("Ejecutando DAO getComandaVerifStock");
	logger.debug("Sql :"+Sql);
	logger.debug("id_sector" + id_sector);
	logger.debug("id_jornada" + id_jornada);
	
	try {
		conn = this.getConnection();
		stm = conn.prepareStatement(Sql + " WITH UR");
		stm.setLong(1, id_jornada);
		stm.setLong(2, id_sector);
		
		rs = stm.executeQuery();
		while (rs.next()) {
			ComandaVerifStockDTO prod1 = new ComandaVerifStockDTO();
			prod1.setId_pedido(rs.getString("id_pedido"));
			prod1.setSector(rs.getString("nombre_seccion"));
			prod1.setCod_sap(rs.getString("cod_sap"));
			prod1.setUni_med(rs.getString("uni_med"));
			prod1.setDescr(rs.getString("descr"));
			prod1.setCantidad(rs.getString("cantidad"));
			prod1.setObs(rs.getString("obs"));
			prod1.setTipo_ve(rs.getString("tipo_ve"));
			prod1.setZona_nom(rs.getString("zona_nom"));
			result.add(prod1);
		}

	} catch (SQLException e) {
		logger.debug("Problema:"+ e);
		throw new JornadasDAOException(e);
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
	 * Obtiene los logs de la jornada 
	 * 
	 * @param  id_jornada long 
	 * @return List LogSimpleDTO
	 * @throws JornadasDAOException
	 */
	public List getLogJornada(long id_jornada) 	throws JornadasDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getLogJornada");		
		try {
			conn = this.getConnection();
			String Sql = 	" SELECT id_log, usuario, descr, fecha FROM bo_log_jpick " +
							" WHERE id_jpicking = ? ORDER BY fecha desc ";
			
			logger.debug("Sql :"+Sql);
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1, id_jornada);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				LogSimpleDTO log = new LogSimpleDTO();
				log.setId_log(rs.getLong("id_log"));
				log.setUsuario(rs.getString("usuario"));
				log.setDescripcion(rs.getString("descr"));
				log.setFecha(rs.getString("fecha"));				
				result.add(log);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Inserta un nuevo log a una jornada 
	 * 
	 * @param  id_jornada long 
	 * @param  user String , login del usuario
	 * @param  descr String 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doAddLogJornadaPick(long id_jornada, String user, String descr) throws JornadasDAOException {

		PreparedStatement stm = null;
		int	rc;
		logger.debug("Ejecutando DAO Jornada addLogJornadaPick");
		String sql =	" INSERT INTO BO_LOG_JPICK (id_jpicking, usuario, descr)" +
						" VALUES ( ?, ?, ? ) ";
		
		logger.debug( "Insert : "+ sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_jornada);
			stm.setString(2,user);
			stm.setString(3,descr);			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			
		} catch (SQLException e) {
			logger.debug("Problema en addLogJornadaPick:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
	}

	public void doAddLogJornadaDesp(long id_jornada, String user, String descr) throws JornadasDAOException {

		PreparedStatement stm = null;
		int	rc;
		logger.debug("Ejecutando DAO Jornada addLogJornadaDesp");
		String sql =	" INSERT INTO BO_LOG_JDESP (id_jdespacho, usuario, descr)" +
						" VALUES ( ?, ?, ? ) ";
		
		logger.debug( "Insert : "+ sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_jornada);
			stm.setString(2,user);
			stm.setString(3,descr);			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			
		} catch (SQLException e) {
			logger.debug("Problema en addLogJornadaDesp:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
	}


	/**
	 * Obtiene los estados de la jornada 
	 * 
	 * @return List EstadoDTO
	 * @throws JornadasDAOException
	 */
	public List getEstadosJornada() throws JornadasDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("Ejecutando DAO getEstadosJornada");		
		try {
			conn = this.getConnection();
			String Sql = 	" SELECT id_estado, nombre, tipo_estado FROM bo_estados WHERE tipo_estado = ?  ";
			logger.debug("Sql :"+Sql);
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setString(1, "JP");
			
			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO edo = new EstadoDTO();
				edo.setId_estado(rs.getLong("id_estado"));
				edo.setNombre(rs.getString("nombre"));
				edo.setTipo(rs.getString("tipo_estado"));	
				result.add(edo);
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
	 * Cambia estado jornada de picking
	 * 
	 * @param  id_jpicking long 
	 * @param  id_estado int 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doCambiaEstadoJornadaPicking(long id_jpicking, int id_estado) throws JornadasDAOException{
		PreparedStatement stm = null;
		int	rc;
		
		String sql =
			" UPDATE bo_jornadas_pick " +
			" SET id_estado = ? " +
			" WHERE id_jpicking = ? ";

		logger.debug("Ejecutando DAO doCambiaEstadoJornadaPicking");
		logger.debug("Sql: "+sql);
		logger.debug("id_jpicking: " + id_jpicking);
		logger.debug("id_estado: " + id_estado);
				
		try {
			conn = this.getConnection();

			stm = conn.prepareStatement(sql);

			stm.setInt(1	, id_estado);
			stm.setLong(2	, id_jpicking);
						
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			
		} catch (SQLException e) {
			logger.debug("Problema en doCambiaEstadoJornadaPicking:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		
		
	}


	/**
	 * Obtiene jornada de despacho a partir del id
	 * 
	 * @param  id_jdespacho long 
	 * @return JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public JornadaDespachoEntity getJornadaDespachoById(long id_jdespacho) throws JornadasDAOException {

		PreparedStatement stm = null;
		ResultSet rs = null;
		JornadaDespachoEntity jor1 = null;

		String SQLStmt =
			" SELECT d.id_jdespacho, id_zona, d.id_hor_desp, d.id_semana, d.id_jpicking, d.dow, d.fecha, " +
			" d.capac_despacho, d.capac_ocupada AS capac_ocupada_despacho, tarifa_express, tarifa_normal, tarifa_economica, tarifa_umbral, " +
			" p.capac_picking, p.capac_ocupada AS capac_ocupada_picking " +
			" FROM bo_jornada_desp d " +
			" JOIN bo_jornadas_pick p ON p.id_jpicking=d.id_jpicking " +
			" WHERE d.id_jdespacho = ? " ;
	
		logger.debug("Ejecutando DAO getJornadaDespachoById");
		logger.debug("SQL: " + SQLStmt);

		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");
			stm.setLong(1, id_jdespacho);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				jor1 = new JornadaDespachoEntity();
				jor1.setId_jdespacho(rs.getLong("id_jdespacho"));
				jor1.setId_zona(rs.getLong("id_zona"));
				jor1.setId_hor_desp(rs.getLong("id_hor_desp"));
				jor1.setId_semana(rs.getLong("id_semana"));
				jor1.setId_jpicking(rs.getLong("id_jpicking"));
				jor1.setDay_of_week(rs.getInt("dow"));
				jor1.setFecha(rs.getDate("fecha"));
				jor1.setCapac_despacho(rs.getLong("capac_despacho"));
				jor1.setCapac_despacho_ocupada(rs.getLong("capac_ocupada_despacho"));
				jor1.setCapac_picking(rs.getLong("capac_picking"));
				jor1.setCapac_picking_ocupada(rs.getLong("capac_ocupada_picking"));
				jor1.setTarifa_express(rs.getInt("tarifa_express"));
				jor1.setTarifa_normal(rs.getInt("tarifa_normal"));
				jor1.setTarifa_economica(rs.getInt("tarifa_economica"));
				jor1.setTarifa_umbral(rs.getInt("tarifa_umbral"));
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
		return jor1;
		
	}

	
	/**
	 * Obtiene jornada de despacho con Mayor Capacidad de Picking
	 * 
	 * @param  id_jdespacho long 
	 * @return JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public long getJornadaDespachoMayorCapacidad(long id_zona, String fecha, double cant_prod) throws JornadasDAOException {
		Statement stm = null;
		ResultSet rs = null;
		long id_jdespacho = 0;

		String SQLStmt = "SELECT JD.ID_JDESPACHO,  "
                       + "       MAX(JP.CAPAC_PICKING - JP.CAPAC_OCUPADA) AS MAX_CAPAC "
                       + "FROM BODBA.BO_JORNADAS_PICK JP "
                       + "     JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JPICKING = JP.ID_JPICKING "
                       + "     JOIN BODBA.BO_HORARIO_PICK HP ON HP.ID_HOR_PICK = JP.ID_HOR_PICK "
                       + " WHERE JD.ID_ZONA = " + id_zona
                       + "  AND JD.FECHA = DATE('" + fecha + "') "
                       + "  AND (JD.CAPAC_DESPACHO - JD.CAPAC_OCUPADA) > 0 "
                       + "  AND (JP.CAPAC_PICKING - JP.CAPAC_OCUPADA) >= " + cant_prod
                       + "  AND ((((DAYS(JP.FECHA)*24*60) + (HOUR(HP.HINI)*60) + MINUTE(HP.HINI)) - ((DAYS(CURRENT TIMESTAMP)*24*60) + (HOUR(CURRENT TIME) * 60) + MINUTE (CURRENT TIME))) >= (JP.HRS_OFRECIDO_WEB*60)) "
                       + " GROUP BY JP.FECHA, JD.ID_JDESPACHO, JP.ID_JPICKING, JD.FECHA, HP.HINI, JP.HRS_OFRECIDO_WEB "
                       + " ORDER BY MAX_CAPAC DESC, ID_JDESPACHO DESC "
                       + " FETCH FIRST 1 ROWS ONLY";
		logger.debug("Ejecutando DAO getJornadaDespachoMayorCapacidad");
		logger.debug("SQL: " + SQLStmt);
		
		try {
			conn = this.getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(SQLStmt);
            if (rs.next()) {
                id_jdespacho = rs.getLong("ID_JDESPACHO");
            }

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				throw new JornadasDAOException(e);
			}finally{
				rs=null;
				stm=null;
	
			}
		}
		return id_jdespacho;
	}


	/**
	 * Actualiza las catidades sin pickear de los pedidos relacionados a una jornada
	 * 
	 * @param  id_jornada long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws JornadasDAOException
	 */
	public boolean setPedidosByIdJornada(long id_jornada) throws JornadasDAOException {
		
		boolean result = false;
		PreparedStatement stm = null;
		String SQLStmt =
			" UPDATE bo_detalle_pedido set cant_spick = cant_solic  " +
			" WHERE id_pedido IN (SELECT id_pedido FROM bo_pedidos WHERE id_jpicking = ? ) ";
	
		logger.debug("Ejecutando DAO setPedidosByIdJornada");
		logger.debug("SQL: " + SQLStmt);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, id_jornada);
			int i = stm.executeUpdate();
			if(i>0){
				result=true;
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}		
		return result;
	}
	
	/**
	 * Obtiene las secciones SAP segun el id_producto
	 * 
	 * @param  lst_prods List 
	 * @return List CategoriaSapEntity
	 * @throws ProductosSapDAOException
	 * 
	 * */
	public List getSeccionesSAPByListIdProducto(List lst_prods)	throws ProductosSapDAOException {
			
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		//Convertir la lista de productos en id_producto 
		String productos_in = "";
		if ( lst_prods != null && lst_prods.size()> 0 ){
			for (int i=0; i<lst_prods.size(); i++){
				ProductosPedidoEntity prod = (ProductosPedidoEntity)lst_prods.get(i);
				productos_in += "," + prod.getId_producto();
			}
			productos_in = productos_in.substring(1);
		}

		String Sql =
			" SELECT CP3.id_catprod id_catprod, CP3.id_catprod_padre id_catprod_padre, CP3.descat descat, " +
			" 	CP3.cat_nivel cat_nivel, CP3.cat_tipo cat_tipo, CP3.estadoactivo estadoactivo" +
			" FROM bo_productos P " +
			" JOIN bo_catprod CP ON CP.id_catprod = P.id_catprod " +
			" JOIN bo_catprod CP1 ON CP1.id_catprod = CP.id_catprod_padre " +
			" JOIN bo_catprod CP2 ON CP2.id_catprod = CP1.id_catprod_padre " +
			" JOIN bo_catprod CP3 ON CP3.id_catprod = CP2.id_catprod_padre " +
			" WHERE P.id_producto IN ("+productos_in+")";
		
		logger.debug("Ejecutando DAO getSeccionesSAPByIdProducto");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_productos:" + productos_in);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			rs = stm.executeQuery();
			while (rs.next()) {
				CategoriaSapEntity cat = new CategoriaSapEntity();
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setId_cat_padre(rs.getString("id_catprod_padre"));
				cat.setDescrip(rs.getString("descat"));
				cat.setNivel(new Integer(rs.getInt("cat_nivel")));
				cat.setTipo(rs.getString("cat_tipo"));
				cat.setEstado(rs.getString("estadoactivo"));
				result.add(cat);
			}
			
			if(result.size()==0){
				throw new ProductosSapDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
			}
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new ProductosSapDAOException(String.valueOf(e.getErrorCode()));
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
	 * Obtiene los detalles del pedido (preparables) segun el id_jornada.<br>
	 * Condiciones:<br>
	 * - El pedido se encuentra en estado En Picking<br>
	 * - El detalle del pedido es un producto preparable.<br> 
	 * 
	 * @param  id_jornada long 
	 * @return List ProductosPedidoEntity
	 * @throws JornadasDAOException
	 * 
	 * */
	public List getDetPedidoPreparablesByIdJornada(long id_jornada)	throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql =
			" SELECT distinct DP.id_producto, DP.cod_prod1, DP.uni_med, DP.descripcion " +
			" FROM bo_pedidos P " +
			" JOIN bo_detalle_pedido DP ON DP.id_pedido = P.id_pedido AND DP.preparable = '"+Constantes.PRODUCTO_ES_PREPARABLE+"' " +
			" WHERE P.id_jpicking = ? AND P.id_estado = "+Constantes.ID_ESTAD_PEDIDO_EN_PICKING+" ";
		
		logger.debug("Ejecutando DAO getDetPedidoPreparablesByIdJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_producto" + id_jornada);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1, id_jornada);
						
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoEntity prod = new ProductosPedidoEntity();
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_prod1(rs.getString("cod_prod1"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setDescripcion(rs.getString("descripcion"));
				result.add(prod);
			}

			if(result.size()==0)
				throw new JornadasDAOException(Constantes._EX_PROD_PREP_NO_EXISTE);
		}catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
	 * Verifica si existe la jornada 
	 * 
	 * @param  id_jornada long 
	 * @return boolean, devuelve <i>true</i> si la jornada existe, caso contrario devuelve <i>false</i>.
	 * @throws JornadasDAOException
	 * 
	 * */
	public boolean existeJornadaById(long id_jornada) throws JornadasDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQLStmt =" SELECT id_jpicking FROM bo_jornadas_pick WHERE id_jpicking = ?  ";
	
		logger.debug("Ejecutando DAO existeJornadaById");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_jornada);
			rs = stm.executeQuery();
			if(rs.next()){
				result=true;
			}
		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
		return result;
	}


	/**
	 * Obtiene las secciones SAP segun el id_jornada
	 * 
	 * @param  id_jornada long 
	 * @return List CategoriaSapEntity
	 * @throws JornadasDAOException
	 * 
	 * */
	public List getSeccionesSAPByIdJornada(long id_jornada) throws JornadasDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT distinct CP3.id_catprod id_catprod, CP3.id_catprod_padre id_catprod_padre, CP3.descat descat," +
			" 	CP3.cat_nivel cat_nivel, CP3.cat_tipo cat_tipo, CP3.estadoactivo estadoactivo" +
			" FROM bo_pedidos P " +
			" JOIN bo_detalle_pedido DP ON DP.id_pedido = P.id_pedido AND DP.preparable = '"+Constantes.PRODUCTO_ES_PREPARABLE+"' " +
			" JOIN bo_productos Pr ON Pr.id_producto = DP.id_producto " +
			" JOIN bo_catprod CP ON CP.id_catprod = Pr.id_catprod " +
			" JOIN bo_catprod CP1 ON CP1.id_catprod = CP.id_catprod_padre " +
			" JOIN bo_catprod CP2 ON CP2.id_catprod = CP1.id_catprod_padre " +
			" JOIN bo_catprod CP3 ON CP3.id_catprod = CP2.id_catprod_padre " +
			" WHERE P.id_jpicking = ? AND P.id_estado in ("+Constantes.ID_ESTAD_PEDIDO_EN_PICKING+","+Constantes.ID_ESTAD_PEDIDO_VALIDADO+")";
		
		logger.debug("Ejecutando DAO getSeccionesSAPByIdJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_jornada:" + id_jornada);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jornada);
			rs = stm.executeQuery();
			while (rs.next()) {
				CategoriaSapEntity cat = new CategoriaSapEntity();
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setId_cat_padre(rs.getString("id_catprod_padre"));
				cat.setDescrip(rs.getString("descat"));
				cat.setNivel(new Integer(rs.getInt("cat_nivel")));
				cat.setTipo(rs.getString("cat_tipo"));
				cat.setEstado(rs.getString("estadoactivo"));
				result.add(cat);
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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

	public List getSeccionesLocalVerifStockByIdJornada(long id_jornada) throws JornadasDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
        String Sql = "SELECT S.ID_SECTOR, P.ID_LOCAL, S.NOMBRE, S.MAX_PROD, " 
                   + "       S.MAX_OP, S.MIN_OP_FILL, S.CANT_MIN_PRODS, L.NOM_LOCAL "
                   + "FROM BO_DETALLE_PEDIDO DP "
                   + "     JOIN BO_PEDIDOS        P ON P.ID_PEDIDO    = DP.ID_PEDIDO AND "
                   + "                                 P.ID_JPICKING  = ? "
                   + "     JOIN BO_PRODUCTOS     PR ON PR.ID_PRODUCTO = DP.ID_PRODUCTO "
                   + "     JOIN BO_PROD_SECTOR   PS ON PS.ID_PRODUCTO = PR.ID_PRODUCTO " 
                   + "     JOIN BO_SECTOR         S ON S.ID_SECTOR    = PS.ID_SECTOR " //AND "
                   //+ "                                 S.ID_LOCAL     = P.ID_LOCAL "
                   + "     JOIN BO_LOCALES        L ON L.ID_LOCAL     = P.ID_LOCAL "
                   + "WHERE DP.CANT_SOLIC >= S.CANT_MIN_PRODS "
                   + "  AND P.ID_ESTADO IN (" + Constantes.ID_ESTAD_PEDIDO_VALIDADO + ", " 
                                              + Constantes.ID_ESTAD_PEDIDO_EN_PICKING + ") "
                   + "GROUP BY S.ID_SECTOR, P.ID_LOCAL, S.NOMBRE, S.MAX_PROD, S.MAX_OP, "
                   + "         S.MIN_OP_FILL, S.CANT_MIN_PRODS, L.NOM_LOCAL "
            //       +" HAVING  SUM(DP.CANT_SOLIC) >= S.CANT_MIN_PRODS " 
                   + "ORDER BY 1 ASC ";

		
		logger.debug("Ejecutando DAO getSeccionesLocalVerifStockByIdJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_jornada:" + id_jornada);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jornada);
			rs = stm.executeQuery();
			while (rs.next()) {
				SectorLocalDTO sector = new SectorLocalDTO();
				sector.setId_sector(rs.getLong("id_sector"));
				sector.setId_local(rs.getLong("id_local"));
				sector.setNombre_local(rs.getString("nom_local"));
				sector.setNombre(rs.getString("nombre"));
				sector.setMax_prod(rs.getLong("max_prod"));
				sector.setMax_op(rs.getLong("max_op"));
				sector.setMin_op_fill(rs.getLong("min_op_fill"));
				sector.setCant_min_prods(rs.getLong("cant_min_prods"));
				result.add(sector);
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
	 * Obtiene cantidad de productos en ronda 
	 * 
	 * @param  id_jornada long 
	 * @return double
	 * @throws JornadasDAOException
	 * 
	 * */
	public double getCountProdEnRondaByIdJornada(long id_jornada) throws JornadasDAOException {
		
		double result = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT sum(dr.cantidad) cantidad " +
			" FROM bo_rondas r " +
			" JOIN bo_detalle_rondas dr ON dr.id_ronda = r.id_ronda " +
			" WHERE r.id_jpicking = ? ";
		
		logger.debug("Ejecutando DAO getCountProdEnRondaByIdJornada");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_jornada:" + id_jornada);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jornada);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cantidad");
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);
		
		return result;	
	}

	public List getSemJorDespPedidoBySem(long id_semana, long id_zona) throws JornadasDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT n_semana, ano, fini, J.id_jdespacho id_jdespacho, J.dow dow, J.fecha fecha, " +
			"  P.id_pedido id_pedido, P.id_estado id_estado " +
			" FROM BO_SEMANAS S " +
			" JOIN BO_JORNADA_DESP J ON J.id_semana = S.id_semana " +
			" JOIN BO_PEDIDOS P ON P.id_jdespacho  = J.id_jdespacho " +
			" WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND S.id_semana = ? AND J.id_zona = ? ";
		
		logger.debug("Ejecutando DAO getSemJorDespPedidoBySem");		
		logger.debug("Sql :"+Sql);
		logger.debug("id_semana:" + id_semana);
		logger.debug("id_zona:" + id_zona);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_semana);
			stm.setLong(2, id_zona);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				SemanaJornadaPedidoDTO sem = new SemanaJornadaPedidoDTO();
				sem.setId_semana(id_semana);
				sem.setN_semana(rs.getInt("n_semana"));
				sem.setAno(rs.getInt("ano"));
				sem.setFec_ini(rs.getString("fini"));
				sem.setId_jdespacho(rs.getLong("id_jdespacho"));
				sem.setDia_semana(rs.getInt("dow"));
				sem.setFec_jdespacho(rs.getString("fecha"));
				sem.setId_pedido(rs.getLong("id_pedido"));
				sem.setId_estado_pedido(rs.getLong("id_estado"));
				result.add(sem);
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
	 * Obtiene la jornada de picking, segun los datos ingresados
	 *  
	 * @param dow
	 * @param id_semana
	 * @param hor_pick
	 * @return long id de jornada de picking
	 * @throws JornadasDAOException
	 */
	public long getJornadasPickingByDatos(int dow, long id_semana, HorarioPickingEntity hor_pick) throws JornadasDAOException {
		long result = -1;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		// Se agrega a la query original, HOUR(h.hini) = ?, pues, originalmente al no considerar la hora, se
		// obtenían varios id_jpicking sin un "orden" aparente, y se tomaba el primer id_jpicking de la lista
		// y es lo que producía el problema, pues en algunas ocasiones obtenía un id_jpicking incorrecto
		// Así, al condicionar que la hora corresponda con la hora copiada, aseguramos que sea el id correcto.
		String Sql =
			" SELECT id_jpicking" +
			" FROM bo_jornadas_pick p " +
			" JOIN bo_horario_pick h ON h.id_hor_pick = p.id_hor_pick AND h.id_semana = ?  AND h.id_local = ? " +
			" WHERE p.dow = ? and HOUR(h.hini) = ?";

		logger.debug("Ejecutando DAO getJornadasPickingByDatos");		
		logger.debug("Sql :"+Sql);
		logger.debug("dow:" + dow);
		logger.debug("id_semana:" + id_semana);
		logger.debug("hor_pick:" + hor_pick.getId_local()+", "+hor_pick.getH_ini() +", "+ hor_pick.getH_fin());
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_semana);
			stm.setLong(2, hor_pick.getId_local());
			stm.setInt(3, dow);
			
			// Se agrega parámetro adicional que condiciona la búsqueda de id_jpicking a que coincida con
			// el horario de la jornada de picking que se está copiando.
			stm.setInt(4, hor_pick.getH_ini().getHours());
		
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getLong("id_jpicking");
				logger.debug("id_jpicking:" + result);
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);
		
		return result;
	}

	/**
	 * Obtiene la ultima jornada picking segun la fecha ingresada 
	 * 
	 * @param fecha
	 * @param id_local
	 * @return JornadaPickingEntity
	 * @throws JornadasDAOException
	 */
	public JornadaPickingEntity getJornadaPickingUltimo(String fecha, long id_local) throws JornadasDAOException {
		JornadaPickingEntity result = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT max( fecha) max, id_hor_pick, id_jpicking " +
			" FROM BO_JORNADAS_PICK " +
			" where fecha <= ? AND id_local = ? " +
			" group by fecha, id_hor_pick, id_jpicking " +
			" order by fecha desc, id_hor_pick desc ";
		
		logger.debug("Ejecutando DAO getJornadaPickingUltimo");		
		logger.debug("Sql	:"+Sql);
		logger.debug("fecha	:" + fecha);
		logger.debug("id_local	:" + id_local);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setString(1, fecha);
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new JornadaPickingEntity(); 
					result.setFecha(rs.getDate("max"));
					result.setId_hor_picking(rs.getLong("id_hor_pick"));
					result.setId_jpicking(rs.getLong("id_jpicking"));
					
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Elimina las jornadas de despacho segun la semana ingresada
	 *  
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelJornadaDespachoBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
		String Sql = " DELETE FROM bo_jornada_desp WHERE id_semana >= ? AND id_semana <= ? AND id_zona = ? ";
		
		logger.debug("Ejecutando DAO doDelJornadaDespachoBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_sem_ini	:" + id_sem_ini);
		logger.debug("id_sem_fin	:" + id_sem_fin);
		logger.debug("id_zona	:" + id_zona);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_sem_ini);
			stm.setLong(2, id_sem_fin);
			stm.setLong(3, id_zona);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}
		
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Elimina los horarios de despacho segun la semana ingresada
	 * 
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelHorarioDespachoBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
		String Sql = " DELETE FROM bo_horario_desp WHERE id_semana >= ? AND id_semana <= ? AND id_zona = ? ";
		
		logger.debug("Ejecutando DAO doDelHorarioDespachoBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_sem_ini	:" + id_sem_ini);
		logger.debug("id_sem_fin	:" + id_sem_fin);
		logger.debug("id_zona	:" + id_zona);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_sem_ini);
			stm.setLong(2, id_sem_fin);
			stm.setLong(3, id_zona);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}	
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Elimina las jornadas de picking segun la semana ingresada
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelJornadaPickingBySemana(long id_semana, long id_local) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
		String Sql = " DELETE FROM bo_jornadas_pick WHERE id_semana = ? AND id_local = ? ";
		
		logger.debug("Ejecutando DAO doDelJornadaPickingBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_semana	:" + id_semana);
		logger.debug("id_local	:" + id_local);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Elimina las jornadas de picking segun la semana ingresada
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelJornadaPickingBySemana(long id_semana, long id_local, long id_zona) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
        String Sql = "DELETE FROM bo_jornadas_pick "
                   + "WHERE ID_JPICKING IN (SELECT JP.ID_JPICKING "
                   + "                      FROM BODBA.BO_JORNADAS_PICK JP "
                   + "                           JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JPICKING = JP.ID_JPICKING "
                   + "                           JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = JD.ID_ZONA "
                   + "                      WHERE JP.ID_SEMANA = ? "
                   + "                        AND JP.ID_LOCAL = ? "
                   + "                        AND JD.ID_ZONA = ?) ";

		logger.debug("Ejecutando DAO doDelJornadaPickingBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_semana	:" + id_semana);
		logger.debug("id_local	:" + id_local);
		logger.debug("id_zona	:" + id_zona);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			stm.setLong(3, id_zona);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}
		
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		logger.debug("result:"+result);
		return result;
	}

	
	/**
	 * Obtiene la cantidad de jornadas de despacho asociadas a las jornadas de picking
	 * 
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_local
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public int getJornadaDespXJornadaPickingBySemanaLocalZona(long id_sem_ini, long id_sem_fin, long id_zona, long id_local) throws JornadasDAOException {
		int result = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
        String Sql = "SELECT COUNT(JD.ID_JDESPACHO) AS CANTIDAD "
                   + "FROM BODBA.BO_JORNADAS_PICK JP "
                   + "     JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JPICKING = JP.ID_JPICKING "
                   + "WHERE JD.ID_ZONA NOT IN (?) "
                   + "  AND JP.ID_LOCAL = ? "
                   + "  AND JP.ID_SEMANA BETWEEN ? AND ?";
		logger.debug("Ejecutando DAO doDelJornadaPickingBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_zona	:" + id_zona);
		logger.debug("id_local	:" + id_local);
		logger.debug("id_sem_ini:" + id_sem_ini);
		logger.debug("id_sem_fin:" + id_sem_fin);
		
		try {
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_zona);
			stm.setLong(2, id_local);
			stm.setLong(3, id_sem_ini);
			stm.setLong(4, id_sem_fin);
			
			rs = stm.executeQuery();
			rs.next();
			result = rs.getInt("CANTIDAD");
			
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
				stm=null;
	
			}
		}	
		logger.debug("result:"+result);
		return result;
	}

	
	/**
	 * Elimina los horarios de picking segun la semana ingresada
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelHorarioPickingBySemana(long id_semana, long id_local) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
		String Sql = " DELETE FROM bo_horario_pick WHERE id_semana = ? AND id_local = ? ";
		
		logger.debug("Ejecutando DAO doDelHorarioPickingBySemana");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_sem_ini	:" + id_semana);
		logger.debug("id_local	:" + id_local);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Obtiene el id de jornada de despacho, a partir de la jornada de picking y la semana inicial de busqueda
	 *  
	 * @param  id_jor_pick
	 * @param  id_semana
	 * @param  id_zona
	 * @return long, id de jornada
	 * @throws JornadasDAOException
	 */
	public long getIdJornadaDespByJornadaPick(long id_jor_pick, long id_semana, long id_zona) throws JornadasDAOException {
		long result=-1;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" SELECT MIN(id_jdespacho) minimo " +
			" FROM bo_jornada_desp " +
			" WHERE id_jpicking = ? AND id_semana > ? AND id_zona = ? ";
		
		logger.debug("Ejecutando DAO getIdJornadaDespByJornadaPick");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_jor_pick	:" + id_jor_pick);
		logger.debug("id_semana	:" + id_semana);
		logger.debug("id_zona	:" + id_zona);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_jor_pick);
			stm.setLong(2, id_semana);
			stm.setLong(3, id_zona);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getLong("minimo");
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);		
		
		return result;
	}

	/**
	 * Actualiza la jornada de picking en la jornada de despacho
	 *  
	 * @param  id_jor_desp
	 * @param  id_jor_pick
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doActJDespachoByJPick(long id_jor_desp, long id_jor_pick) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		
		String Sql = " UPDATE bo_jornada_desp SET id_jpicking = ? " +
				" WHERE id_jdespacho = ? ";
		
		logger.debug("Ejecutando DAO doActJDespachoByJPick");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_jor_desp	:" + id_jor_desp);
		logger.debug("id_jor_pick	:" + id_jor_pick);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, id_jor_pick);
			if(id_jor_pick<0){
				stm.setNull(1,Types.INTEGER);
			}else{
				stm.setLong(1, id_jor_pick);
			}
			stm.setLong(2, id_jor_desp);
			
			int fila = stm.executeUpdate();
			if (fila>0) {
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
	
			}
		}
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * Obtiene el listado de jornadas de despacho que no se encuentra en el rango de semanas pero que tienen jpicking 
	 * que pertenecen en el rango de semanas
	 *  
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @return List JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJDespachoRelBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException {
		List lst_despacho = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String Sql =
			" select id_jdespacho, id_semana, fecha " +
			" from bo_jornada_desp " +
			" where id_jpicking IN " +
			" (select id_jpicking from bo_jornadas_pick where id_semana >= ? AND id_semana <= ? ) " +
			" AND (id_semana < ? OR id_semana > ?) " +
			" AND id_zona = ? "+
			" order by id_semana, id_jdespacho ";
		
		logger.debug("Ejecutando DAO getJDespachoRelBySemanas");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_sem_ini	:" + id_sem_ini);
		logger.debug("id_sem_fin	:" + id_sem_fin);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_sem_ini);
			stm.setLong(2, id_sem_fin);
			stm.setLong(3, id_sem_ini);
			stm.setLong(4, id_sem_fin);
			stm.setLong(5, id_zona);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				JornadaDespachoEntity jor = new JornadaDespachoEntity();
				jor.setId_jdespacho(rs.getLong("id_jdespacho"));
				jor.setId_semana(rs.getLong("id_semana"));
				jor.setFecha(rs.getDate("fecha"));
				lst_despacho.add(jor);
			}
	
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("lst_despacho.size()	:"+lst_despacho.size());		

		return lst_despacho;
	}

	/**
	 * Verifica si existe jornadas y horarios de despacho y picking entre semana inicial y final
	 * 
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean existeJornadasHorariosBySemanas(long id_sem_ini, long id_sem_fin, long id_zona, long id_local) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql =
			" select id_jdespacho id, 'DESP' tipo from bo_jornada_desp where id_semana >= ? AND id_semana <= ? AND id_zona = ? " +
			" UNION " +
			" select id_hor_desp id, 'HDESP' tipo from bo_horario_desp where id_semana >= ? AND id_semana <= ? AND id_zona = ? " +
			" UNION " +
			" select id_jpicking id, 'PICK' tipo from bo_jornadas_pick where id_semana >= ? AND id_semana <= ? AND id_local = ? " +
			" UNION " +
			" select id_hor_pick id, 'HPICK' tipo from bo_horario_pick where id_semana >= ? AND id_semana <= ? AND id_local = ? " +
			" order by tipo, id ";
		
		logger.debug("Ejecutando DAO existeJornadasHorariosBySemanas");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_sem_ini	:" + id_sem_ini);
		logger.debug("id_sem_fin	:" + id_sem_fin);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_sem_ini);
			stm.setLong(2, id_sem_fin);
			stm.setLong(3, id_zona);
			stm.setLong(4, id_sem_ini);
			stm.setLong(5, id_sem_fin);
			stm.setLong(6, id_zona);
			stm.setLong(7, id_sem_ini);
			stm.setLong(8, id_sem_fin);
			stm.setLong(9, id_local);
			stm.setLong(10, id_sem_ini);
			stm.setLong(11, id_sem_fin);
			stm.setLong(12, id_local);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);		
		return result;
	}

	/**
	 * Revisa si existen pedidos en las jornadas de picking, segun rango de semanas y local
	 * 
	 * @param id_semana
	 * @param id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean existePedidoByJPicking(long id_semana, long id_local) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql =
			" select id_pedido, id_local, id_zona from bo_pedidos where id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND id_jpicking in " +
			" ( select id_jpicking " +
			" FROM bo_jornadas_pick WHERE id_semana = ? AND id_local = ? ) ";
		
		logger.debug("Ejecutando DAO existePedidoByJPicking");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_semana	:" + id_semana);
		logger.debug("id_local	:" + id_local);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_semana);
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);		
		return result;
	}


	/**
	 * Revisa si existen pedidos en las jornadas de picking, segun rango de semanas y local
	 * 
	 * @param id_semana
	 * @param id_local
	 * @param id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean existePedidoByJPicking(long id_semana, long id_local, long id_zona) throws JornadasDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String Sql = "SELECT P.ID_PEDIDO, P.ID_LOCAL, P.ID_ZONA " 
                   + "FROM BODBA.BO_PEDIDOS P "
                   + "WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND P.ID_ZONA = ? "
                   + "  AND P.ID_JPICKING IN (SELECT JP.ID_JPICKING "
                   + "                        FROM BODBA.BO_JORNADAS_PICK JP " 
                   + "                        WHERE JP.ID_SEMANA = ? "
                   + "                          AND JP.ID_LOCAL = ? )";
		
		logger.debug("Ejecutando DAO existePedidoByJPicking");		
		logger.debug("Sql	:"+Sql);
		logger.debug("id_zona	:" + id_zona);
		logger.debug("id_semana	:" + id_semana);
		logger.debug("id_local	:" + id_local);
		
		try {
	
			conn = this.getConnection();
	
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_zona);
			stm.setLong(2, id_semana);
			stm.setLong(3, id_local);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new JornadasDAOException(String.valueOf(e.getErrorCode()));
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
		logger.debug("result:"+result);		
		return result;
	}

    /**
     * @param fecha
     * @param id_semana
     * @param idZona
     * @return
     */
    public List getJornadasDespachoByFecha(Date fecha, long idSemana, long idZona) throws JornadasDAOException {
        
        List result = new ArrayList();
        
        PreparedStatement stm = null;
        ResultSet rs = null;
        String SQLStmt =
                " SELECT d.id_jdespacho, id_zona, d.id_hor_desp, d.id_semana, d.id_jpicking, d.dow, d.fecha, " +
                " d.capac_despacho, d.capac_ocupada AS capac_ocupada_despacho, d.tarifa_express, d.tarifa_normal, d.tarifa_economica, " +
                " p.capac_picking, p.capac_ocupada AS capac_ocupada_picking, " +
                " p.hrs_validacion AS hrs_validacion, p.hrs_ofrecido_web AS hrs_web, p.fecha fecha_pick, " +
                " h.hini AS hini, h.hfin AS hfin " +
                " FROM bo_jornada_desp d " +
                " JOIN bo_jornadas_pick p ON p.id_jpicking=d.id_jpicking " +
                " JOIN bo_horario_pick h ON h.id_hor_pick = p.id_hor_pick " +
                " WHERE d.id_semana = ? " +
                " AND id_zona = ? AND d.fecha = ? " +
                " ORDER BY fecha"
                ;
        
        logger.debug("Ejecutando DAO getJornadasDespachoByIdSemana");
        logger.debug("SQL: " + SQLStmt);
        
        logger.debug("DAO id_Semana: " + idSemana);
        logger.debug("DAO id_zona: " + idZona);
        
        try {

            conn = this.getConnection();
            
            stm = conn.prepareStatement( SQLStmt + " WITH UR" );

            stm.setLong(1, idSemana);
            stm.setLong(2, idZona);
            stm.setDate(3, new java.sql.Date(fecha.getTime()));
            
            rs = stm.executeQuery();
            while (rs.next()) {
                JornadaDespachoEntity jor1 = new JornadaDespachoEntity();
                jor1.setId_jdespacho(rs.getLong("id_jdespacho"));
                jor1.setId_zona(rs.getLong("id_zona"));
                jor1.setId_hor_desp(rs.getLong("id_hor_desp"));
                jor1.setId_semana(rs.getLong("id_semana"));
                jor1.setId_jpicking(rs.getLong("id_jpicking"));
                jor1.setDay_of_week(rs.getInt("dow"));
                jor1.setFecha(rs.getDate("fecha"));
                jor1.setCapac_despacho(rs.getLong("capac_despacho"));
                jor1.setCapac_despacho_ocupada(rs.getLong("capac_ocupada_despacho"));
                jor1.setCapac_picking(rs.getLong("capac_picking"));
                jor1.setCapac_picking_ocupada(rs.getLong("capac_ocupada_picking"));
                jor1.setTarifa_express(rs.getInt("tarifa_express"));
                jor1.setTarifa_normal(rs.getInt("tarifa_normal"));
                jor1.setTarifa_economica(rs.getInt("tarifa_economica"));
                jor1.setHrs_validacion(rs.getInt("hrs_validacion"));
                jor1.setHrs_ofrecido_web(rs.getInt("hrs_web"));
                jor1.setHoraIniPicking(rs.getTime("hini"));
                jor1.setHoraFinPicking(rs.getTime("hfin"));
                jor1.setFecha_picking(rs.getDate("fecha_pick"));
                result.add(jor1);
            }

        } catch (SQLException e) {
            logger.debug("Problema:"+ e);
            throw new JornadasDAOException(e);
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

    /**(+) INDRA (+)
	 * Verifica si existe la jornada con algun pedido en estado Revision Faltante
	 * 
	 * @param  id_jornada long 
	 * @return boolean, devuelve <i>true</i> si la jornada existe, caso contrario devuelve <i>false</i>.
	 * @throws JornadasDAOException
	 * 
	 * */
	public boolean existeJornadaRevFaltante(long id_jornada) throws JornadasDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQLStmt =" SELECT id_jpicking FROM bo_pedidos WHERE id_jpicking= ? and id_estado = " +  Constantes.ID_ESTAD_PEDIDO_REVISION_FALTAN +"" ;
	
		logger.debug("Ejecutando DAO existeJornadaRevFaltante");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_jornada);
			rs = stm.executeQuery();
			if(rs.next()){
				result=true;
}
		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new JornadasDAOException(e);
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
		return result;
	}
//(-) INDRA (-)	
	
	/**
	 * @param idJpicking
	 * @param cant_productos
	 * @return
	 * @throws JornadasDAOException
	 */
	public boolean isCapacidadPickingValida(long idJpicking, int cantProductos) throws JornadasDAOException {

		boolean result = false;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int capacidadOcupada = 0;
		
		String SQLStmtPicking  = " SELECT ID_JPICKING FROM BO_JORNADAS_PICK WHERE CAPAC_PICKING >= (CAPAC_OCUPADA + ?) AND ID_JPICKING = ? ";
		try {

			conn = this.getConnection();
			
			if(actualizarCapacidadPorQuery("ACTUALIZAR_CAPACIDAD_PICKING")) {
				SQLStmtPicking  = " SELECT ID_JPICKING FROM BO_JORNADAS_PICK WHERE CAPAC_PICKING >= ? AND ID_JPICKING = ? ";
				capacidadOcupada = ObtenerCapacidadOcupada(idJpicking, cantProductos);
				stm = conn.prepareStatement( SQLStmtPicking + " WITH UR" );
				stm.setInt(1, capacidadOcupada+cantProductos);
				stm.setLong(2, idJpicking);	
				rs = stm.executeQuery();
				if(rs.next()){
					result=true;
				}
			} else {
				stm = conn.prepareStatement( SQLStmtPicking + " WITH UR" );
				stm.setInt(1, cantProductos);
				stm.setLong(2, idJpicking);
				rs = stm.executeQuery();
				if(rs.next()){
					result=true;
				}
			}
			
		} catch (SQLException e) {
			logger.error("Problema:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {}
		}		
		return result;		
	}
	
	public int actualizarCapacidadOcupadaPicking(long id_jpicking) throws JornadasDAOException{
		int registroActualizado = 0;
		int capacidad = ObtenerCapacidadOcupada(id_jpicking, 0);
		try {
			conn = this.getConnection();
			PreparedStatement pst = conn.prepareStatement("update BO_JORNADAS_PICK set CAPAC_OCUPADA = ? where ID_JPICKING = ?");
			pst.setInt(1, capacidad);
			pst.setLong(2, id_jpicking);
			registroActualizado = pst.executeUpdate();
			pst.close();
		} catch(Exception ex) {
			throw new JornadasDAOException(ex);
		} finally {
			releaseConnection();
		}
		return registroActualizado;
	}
	
	public boolean valoresNegativos() {
		boolean valoresNegativos = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement("SELECT * FROM BO_PARAMETROS WHERE NOMBRE = 'VALORES_NEGATIVOS'");
			rs = pst.executeQuery();
			if(rs.next()) {
				valoresNegativos = true;
			}
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				releaseConnection();
			} catch (SQLException e) {
			}finally{
				rs=null;
				pst=null;
	
			}
		}
		return valoresNegativos;
	}
	
	//Inconsistencia de Despachos
	
	/**
	 * Incrementa la ocupación de una jornada de despacho en una cantidad determinada
	 * (si el valor es negativo, lo decrementa)
	 * 
	 * @param  incremento int  
	 * @param  id_jdespacho long 
	 * 
	 * @throws JornadasDAOException
	 */
	public void doOcupaCapacidadDespacho(long id_jdespacho, int incremento) throws JornadasDAOException{
		
		
		PreparedStatement stm = null;
		int	rc;
		
		try {

			conn = this.getConnection();
			
			//Este es un flag en bo_parametros para desactivar el cambio hecho se debe ejecutar la siguientes querys
			//update bo_parametro set valor = 'FALSE' where nombre = 'ACTUALIZAR_CAPACIDAD_DESPACHO'
			if(actualizarCapacidadPorQuery("ACTUALIZAR_CAPACIDAD_DESPACHO")) {
				int capacidadOcupada = ObtenerCapacidadDespachoOcupada(id_jdespacho);
				String SQLStmt = " UPDATE bo_jornada_desp SET capac_ocupada = ? WHERE id_jdespacho = ? ";
				stm = conn.prepareStatement(SQLStmt);
				stm.setInt(1,capacidadOcupada);
				stm.setLong(2,id_jdespacho);
				logger.info("Actualizo la capacidad "+capacidadOcupada+" - jornada "+id_jdespacho);
				
			} else {
				
				String SQLStmt = " UPDATE bo_jornada_desp SET capac_ocupada = ( capac_ocupada + ? ) WHERE id_jdespacho = ? ";				
				stm = conn.prepareStatement(SQLStmt);
				stm.setInt(1,incremento);
				stm.setLong(2,id_jdespacho);
				logger.info("Incremento la capacidad "+incremento+" - jornada "+id_jdespacho);
			}
						
			rc = stm.executeUpdate();

		} catch (SQLException e) {
			logger.error("Error:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {}
		}
	}
		
	public boolean  actualizarCapacidadPorQuery(String tipoCapacidad) {
		
		boolean actualiza = false;
		ResultSet rs = null;
		PreparedStatement pst = null;
		
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement("SELECT VALOR FROM BODBA.BO_PARAMETROS WHERE NOMBRE = ? with ur");
			pst.setString(1, tipoCapacidad);
			rs = pst.executeQuery();
			if(rs.next()) {
				if(rs.getString("valor").trim().equalsIgnoreCase("TRUE"))
					actualiza = true;
			}
		} catch(Exception ex) {
			logger.error(ex);			
		} finally {
			try {
				if (rs != null) 
					rs.close(); 
				if (pst != null) 
				releaseConnection();
			} catch (SQLException e) {}
			
		}
		return actualiza;
	}
	
	/**
	 * @param idJdespacho
	 * @return
	 * @throws JornadasDAOException
	 */
	public boolean isCapacidadDespachoValida(long idJdespacho) throws JornadasDAOException {

		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQLStmtDespacho = " SELECT ID_JDESPACHO FROM BO_JORNADA_DESP WHERE CAPAC_DESPACHO > CAPAC_OCUPADA AND ID_JDESPACHO = ? ";	
		try {

			conn = this.getConnection();		
			
			if(actualizarCapacidadPorQuery("ACTUALIZAR_CAPACIDAD_DESPACHO")) {
				SQLStmtDespacho  = " SELECT ID_JDESPACHO FROM BO_JORNADA_DESP WHERE CAPAC_DESPACHO > ? AND ID_JDESPACHO = ? ";
				int capacidadOcupada = ObtenerCapacidadDespachoOcupada(idJdespacho);
				stm = conn.prepareStatement( SQLStmtDespacho + " WITH UR" );
				stm.setInt(1, capacidadOcupada);
				stm.setLong(2, idJdespacho);	
				rs = stm.executeQuery();
				if(rs.next()){
					result=true;
				}
			} else {
				stm = conn.prepareStatement( SQLStmtDespacho + " WITH UR" );
				stm.setLong(1, idJdespacho);
				rs = stm.executeQuery();
				if(rs.next()){
					result=true;
				}
			}		
			
		} catch (SQLException e) {
			logger.error("Problema:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(stm != null)
					stm.close();
				releaseConnection();				
			} catch (SQLException e) {}
		}		
		
		return result;		
	}	
	
	//Capacidad de despacho utilizada desde BO_PEDIDOS
	public int ObtenerCapacidadDespachoOcupada(long idJdespacho) {
		
		int capacidadOcupada = 0;
		PreparedStatement stmCapacOcupada = null;
		ResultSet rs = null;
		
		String sql = " SELECT COUNT(ID_PEDIDO) AS CAPAC_OCUP FROM BO_PEDIDOS WHERE ID_JDESPACHO = ? AND TIPO_VE <> 'S'"
				+ "AND ID_ESTADO NOT IN("+ Constantes.ESTADOS_PEDIDO_NULOS+ ") WITH UR ";
		try {

			conn = this.getConnection();
			
			stmCapacOcupada = conn.prepareStatement(sql);
			stmCapacOcupada.setLong(1, idJdespacho);
			rs = stmCapacOcupada.executeQuery();
			if(rs.next()) {
				capacidadOcupada = rs.getInt("CAPAC_OCUP");
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {				
				if(rs != null) 
					rs.close();
				if(stmCapacOcupada != null) 
					stmCapacOcupada.close();
				releaseConnection();				
			} catch (SQLException e) {}
		}
		
		return capacidadOcupada;
	}
	
	
	/**
	 * @param idJdespacho
	 * @return
	 * @throws JornadasDAOException
	 */
	public boolean isValidJornadasLocal(long idJdespacho, long idJpicking, long idLocal) throws JornadasDAOException {

		boolean result = false;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQLStmtDespacho = " SELECT JP.ID_JPICKING, JD.ID_JDESPACHO, JP.ID_LOCAL FROM BO_JORNADAS_PICK JP" +
				" inner join BO_JORNADA_DESP JD on JP.ID_JPICKING=JD.ID_JPICKING " +
				" WHERE  JP.ID_JPICKING= ?  ";	
		try {

			conn = this.getConnection();		
			stm = conn.prepareStatement( SQLStmtDespacho + " WITH UR" );
			stm.setLong(1, idJpicking);
			rs = stm.executeQuery();
            while (rs.next()) {
	           if(rs.getLong("ID_JDESPACHO") == idJdespacho && rs.getLong("ID_LOCAL") == idLocal){
	        	   result = true;
	        	   break;
	           }	                
            }
	
			
		} catch (SQLException e) {
			logger.error("Problema:"+ e);
			throw new JornadasDAOException(e);
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(stm != null)
					stm.close();
				releaseConnection();				
			} catch (SQLException e) {}
		}		
		
		return result;		
	}	
	
}
