package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.ClientesDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.ReprogramacionDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Clase que permite consultar las jornadas de despachos que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcDespachosDAO implements DespachosDAO{

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
	 */
	private Connection getConnection()throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
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
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión.
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws DespachosDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws DespachosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new DespachosDAOException(e);
		}
	}
	
	/**
	 * Agrega registro al log del despacho.
	 * 
	 * @param  id_pedido long 
	 * @param  login String 
	 * @param  log String 
	 * 
	 * @throws DespachosDAOException 
	 */
	public void addLogDespacho(long id_pedido, String login, String log)
		throws DespachosDAOException {
		
		
		PreparedStatement stm = null;
		
		String SQLStmt = 
			"INSERT INTO bo_log_despacho (id_pedido, usuario, descripcion) " +
			"VALUES (?, ?, ?)" ;
		
		logger.debug("Ejecución DAO addLogDespacho");
		logger.debug("SQL: " + SQLStmt);
		
		logger.debug("id_pedido: " + id_pedido);
		logger.debug("login: " + login);
		logger.debug("log: " + log);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, id_pedido);
			stm.setString(2, login);
			stm.setString(3, log);

			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		} catch (Exception e) {
			logger.debug("Problema al insertar log: "+ e);
			throw new DespachosDAOException(e);
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
	 * Obtiene listado del log de un despacho.
	 * 
	 * @param  id_pedido long 
	 * @return List of LogSimpleDTO's
	 * @throws DespachosDAOException 
	 */
	public List getLogDespacho(long id_pedido)
		throws DespachosDAOException{
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt = 
			" SELECT id_log, usuario, fechahora, descripcion " +
			" FROM bo_log_despacho " +
			" WHERE id_pedido = ? " +
			" ORDER BY fechahora desc ";
			
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setLong(1,id_pedido);

			rs = stm.executeQuery();
			while (rs.next()) {
				LogSimpleDTO log1 = new LogSimpleDTO();
				log1.setId_log(rs.getLong("id_log"));
				log1.setUsuario(rs.getString("usuario"));
				log1.setDescripcion(rs.getString("descripcion"));
				log1.setFecha(rs.getString("fechahora"));
				result.add(log1);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DespachosDAOException(e);
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
	 * Obtiene listado de estados de despacho
	 * 
	 * @return List of EstadoDTO's
	 * @throws DespachosDAOException 
	 */
	public List getEstadosDespacho() throws DespachosDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt = 
				"SELECT id_estado,nombre " +
				"FROM bo_estados " +
				"WHERE tipo_estado= ? "
				;

		logger.debug("Ejecución getEstadosDespacho()");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setString(1,"DE");
			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO est = new EstadoDTO();
				est.setId_estado(rs.getLong("id_estado"));
				est.setNombre(rs.getString("nombre"));
				result.add(est);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e.getMessage());
			throw new DespachosDAOException(e);
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
	 * Obtiene listado de despachos por criterio
	 * 
	 * @param  criterio DespachoCriteriaDTO 
	 * @return List MonitorDespachosDTO
	 * @throws DespachosDAOException
	 */
	public List getDespachosByCriteria(DespachoCriteriaDTO criterio) throws DespachosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		List result = new ArrayList();

		long id_comuna 	= 0;
		long id_estado 	= 0;
		long id_pedido	= 0;
		long id_local 	= 0;
		long id_jdespacho = 0;
		long id_zona	= 0;
		String str_fecha = null;
		String str_hini	 = null;
		String str_hfin	 = null;
		String SQLStmt = "";
		
		id_comuna 	 = criterio.getId_comuna();
		id_estado 	 = criterio.getId_estado();
		id_pedido	 = criterio.getId_pedido();
		id_local	 = criterio.getId_local();
		id_jdespacho = criterio.getId_jdespacho();
		id_zona		 = criterio.getId_zona();
		str_hini	 = criterio.getH_inicio();
		str_hfin     = criterio.getH_fin();
		str_fecha	 = criterio.getF_despacho();
		
		// Filtros de estado
		String[] filtro_estados = criterio.getFiltro_estados();
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		
		//paginacion
		int pag 	= criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;	
		
		
		logger.debug("Ejecutando DAO getDespachosByCriteria");
		
		//		orden de columnas
		String SQLOrder="";
		if (criterio.getOrden_columnas()!=null){
			
			String obj;			
			int x=1;
			for(int i=0; i<criterio.getOrden_columnas().size(); i++){
				if (x==1){
					SQLOrder += " ORDER BY ";
				}
				obj = (String)criterio.getOrden_columnas().get(i);
				logger.debug(  "objeto : "+obj  );
				if (x>1) SQLOrder += " ,";					
				SQLOrder += " "+obj+" ";
				x++;
			}
		}

		// Paginador
		SQLStmt +=
			" SELECT * FROM ( " +
			" SELECT row_number() over("+SQLOrder+") as row, ";
		
		// SQL principal
		SQLStmt +=			
			" p.id_pedido, p.id_jpicking, p.id_jdespacho, p.cant_bins, p.id_estado as id_estado, " +
			" e.nombre as estado, p.nom_cliente, p.dir_tipo_calle, p.dir_calle, p.dir_numero, p.dir_depto, c.nombre as comuna, " +
			" j.fecha, p.id_local, p.id_zona, z.nombre AS zona, z.descripcion desc, h.hini, h.hfin, " +
			" hp.hini as hini_jpick, hp.hfin as hfin_jpick, jp.fecha as fecha_picking, " +
			" p.dir_calle dir_calle, p.dir_numero dir_numero, p.dir_depto dir_depto, p.tipo_despacho tipo_despacho, " +
			" D.DIR_CONFLICTIVA, p.sin_gente_txt, p.sin_gente_rut, p.sin_gente_dv, pe.id_ruta " +
			" FROM bo_pedidos p" +
			"      LEFT JOIN FO_DIRECCIONES D ON D.DIR_ID = P.DIR_ID " +
			"      LEFT JOIN bo_estados e ON e.id_estado = p.id_estado " +
			"      LEFT JOIN bo_zonas z ON z.id_zona = p.id_zona " +
			"      LEFT JOIN bo_jornada_desp j ON j.id_jdespacho = p.id_jdespacho " +
			"      LEFT JOIN bo_horario_desp h ON h.id_hor_desp = j.id_hor_desp " +
			"      LEFT JOIN bo_comunas c ON c.id_comuna = p.id_comuna " +
			"      LEFT JOIN bo_jornadas_pick jp ON jp.id_jpicking = p.id_jpicking " +
			"      LEFT JOIN bo_horario_pick hp ON hp.id_hor_pick = jp.id_hor_pick " +
            "      LEFT OUTER JOIN bodba.bo_pedidos_ext pe on pe.id_pedido = p.id_pedido " +
			" WHERE p.id_estado NOT IN (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ", 4, 20) ";
		
		// Filtro id_local
		if (id_local > 0){
			SQLStmt += " AND p.id_local = " + id_local + " ";
		}
		
		// Filtro id_estado
		if (id_estado > 0){
			SQLStmt += " AND p.id_estado = " + id_estado + " ";
		}
		
		// Filtro estados IN
		if ( !estados_in.equals("") ){ 
			SQLStmt += " AND p.id_estado IN (" + estados_in + ") " ;
		}
		
		// Filtro id_jdespacho
		if (id_jdespacho > 0){
			SQLStmt += " AND p.id_jdespacho = " + id_jdespacho + " ";
		}	
		
		// Filtro id_pedido
		if (id_pedido > 0){
			SQLStmt += " AND p.id_pedido = " + id_pedido + " ";
		}
		
		// Filtro id_sector
		if (id_zona > 0){
			SQLStmt += " AND p.id_zona = " + id_zona + " ";
		}		

		//	Filtro id_comuna
		if (id_comuna > 0){
			SQLStmt += " AND p.id_comuna = " + id_comuna + " ";
		} else if (id_comuna == -2) { //El combo del monitor tendra este valo para Retiro Local
            SQLStmt += " AND p.tipo_despacho = 'R' ";
        }		
		
		// Filtro fecha
		if (str_fecha != null){
			SQLStmt += " AND j.fecha = '" + str_fecha + "' ";
		}
		
		// Filtro hini
		if (str_hini != null){
			SQLStmt += " AND h.hini = '" + str_hini + "' ";
		}

		// Filtro hfin
		if (str_hfin != null){
			SQLStmt += " AND h.hfin = '" + str_hfin + "' ";
		}
			
		// Paginador
		SQLStmt +=
			") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
		

		logger.debug("SQLStmt: "+SQLStmt);
		
		try {
			
			conn = this.getConnection();
					
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
	
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorDespachosDTO mon1 = new MonitorDespachosDTO();
				if (rs.getString("cant_bins") == null ) {
				    mon1.setCant_bins(0);
                } else {
                    mon1.setCant_bins(rs.getInt("cant_bins"));    
                }                
				mon1.setEstado(rs.getString("estado"));
				mon1.setId_estado(rs.getLong("id_estado"));
				mon1.setId_pedido(rs.getLong("id_pedido"));
				if (rs.getString("id_jpicking") == null ) {
                    mon1.setId_jpicking(0);
                } else {
                    mon1.setId_jpicking(rs.getLong("id_jpicking"));
                }		
                if (rs.getString("id_jdespacho") == null ) {
                    mon1.setIdJDespacho(0);
                } else {
                    mon1.setIdJDespacho(rs.getLong("id_jdespacho"));
                }
                mon1.setId_zona(rs.getLong("id_zona"));
				mon1.setZona_despacho(rs.getString("zona"));
				mon1.setDesc_zona_desp(rs.getString("desc"));
				mon1.setF_despacho(rs.getString("fecha"));
				if (rs.getString("tipo_despacho") == null){
					mon1.setTipo_despacho("N");
				}else{
					mon1.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				mon1.setH_ini(rs.getString("hini"));
				mon1.setH_fin(rs.getString("hfin"));
				mon1.setComuna(rs.getString("comuna"));
				mon1.setHini_jpick(rs.getTime("hini_jpick"));
				mon1.setHfin_jpick(rs.getTime("hfin_jpick"));
				mon1.setFecha_picking(rs.getDate("fecha_picking"));
				mon1.setDir_calle(rs.getString("dir_calle"));
				mon1.setDir_numero(rs.getString("dir_numero"));
				mon1.setDir_depto(rs.getString("dir_depto"));
				mon1.setDir_conflictiva(rs.getString("DIR_CONFLICTIVA"));
                if ( rs.getString("sin_gente_txt") != null ) {
                    mon1.setPersonaRetiraRecibe(rs.getString("sin_gente_txt"));
                } else {
                    mon1.setPersonaRetiraRecibe("");    
                }
                if ( rs.getString("sin_gente_rut") != null && rs.getString("sin_gente_dv") != null ) {
                    mon1.setRutCompletoPersona(rs.getString("sin_gente_rut") + "-" + rs.getString("sin_gente_dv"));
                } else {
                    mon1.setRutCompletoPersona("");
                }
                if ( rs.getString("id_ruta") != null ) {
                    mon1.setIdRuta( rs.getLong("id_ruta") );
                }
				result.add(mon1);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DespachosDAOException(e);
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
	 * Retorna el número de registros de una query con criterio
	 * 
	 * @param  criterio DespachoCriteriaDTO 
	 * @return long
	 * @throws DespachosDAOException
	 */
	public long getCountDespachosByCriteria(DespachoCriteriaDTO criterio) throws DespachosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		long id_comuna 	= 0;
		long id_estado 	= 0;
		long id_pedido	= 0;
		long id_local 	= 0;
		long id_jdespacho = 0;
		long id_zona	= 0;
		String str_fecha = null;
		String str_hini	 = null;
		String SQLStmt	= "";
		long	count	= 0;
		
		id_comuna 	= criterio.getId_comuna();
		id_estado 	= criterio.getId_estado();
		id_pedido	= criterio.getId_pedido();
		id_local	= criterio.getId_local();
		id_jdespacho = criterio.getId_jdespacho();
		id_zona		= criterio.getId_zona();
		str_hini	= criterio.getH_inicio();
		str_fecha	= criterio.getF_despacho();
		
		// Filtros de estado
		String[] filtro_estados = criterio.getFiltro_estados();
		String estados_in = "";
		if ( filtro_estados != null && filtro_estados.length > 0 ){
			for (int i=0; i<filtro_estados.length; i++){
				estados_in += "," + filtro_estados[i];
			}
			estados_in = estados_in.substring(1);
		}
		
		logger.debug("Ejecutando DAO getCountDespachosByCriteria");
		

		// Paginador
		SQLStmt +=
			" SELECT count(*) AS cuenta " +
			" FROM bo_pedidos p" +
			" LEFT JOIN bo_estados e ON e.id_estado = p.id_estado " +
			" LEFT JOIN bo_zonas z ON z.id_zona = p.id_zona " +
			" LEFT JOIN bo_jornada_desp j ON j.id_jdespacho = p.id_jdespacho " +
			" LEFT JOIN bo_horario_desp h ON h.id_hor_desp = j.id_hor_desp " +
			" LEFT JOIN bo_comunas c ON c.id_comuna = p.id_comuna " +
			" LEFT JOIN bo_jornadas_pick jp ON jp.id_jpicking = p.id_jpicking " +
			" LEFT JOIN bo_horario_pick hp ON hp.id_hor_pick = jp.id_hor_pick " +
			" WHERE p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") "
			;	
		
		// Filtro id_local
		if (id_local > 0){
			SQLStmt += " AND p.id_local = " + id_local + " ";
		}
		
		// Filtro id_estado
		if (id_estado > 0){
			SQLStmt += " AND p.id_estado = " + id_estado + " ";
		}
		
		// Filtro estados IN
		if ( !estados_in.equals("") ){ 
			SQLStmt += " AND p.id_estado IN (" + estados_in + ") " ;
		}
		
		// Filtro id_jdespacho
		if (id_jdespacho > 0){
			SQLStmt += " AND p.id_jdespacho = " + id_jdespacho + " ";
		}	
		
		// Filtro id_pedido
		if (id_pedido > 0){
			SQLStmt += " AND p.id_pedido = " + id_pedido + " ";
		}
		
		// Filtro id_sector
		if (id_zona > 0){
			SQLStmt += " AND p.id_zona = " + id_zona + " ";
		}		
		
		//	Filtro id_comuna
		if (id_comuna > 0){
			SQLStmt += " AND p.id_comuna = " + id_comuna + " ";
		}
		
		// Filtro fecha
		if (str_fecha != null){
			SQLStmt += " AND j.fecha = '" + str_fecha + "' ";
		}		
		
		// Filtro hini
		if (str_hini != null){
			SQLStmt += " AND h.hini = '" + str_hini + "' ";
		}
		
		
		logger.debug("SQLStmt: "+SQLStmt);
		
		try {
			
			conn = this.getConnection();	
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
	
			rs = stm.executeQuery();
			while (rs.next()) {
				count = rs.getLong("cuenta");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DespachosDAOException(e);
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
		return count;	
		
	}

	
	/**
	 * Obtiene información del despacho 
	 * 
	 * @param  id_pedido long 
	 * @return DespachoDTO
	 * @throws DespachosDAOException
	 */
	public DespachoDTO getDespachoById(long id_pedido) throws DespachosDAOException {		
		PreparedStatement stm = null;
		ResultSet rs = null;
		DespachoDTO desp = new DespachoDTO();
		
		String	sql	= "SELECT p.id_pedido, p.id_jpicking, p.id_jdespacho, p.cant_bins, p.id_estado, " +
		              "e.nombre as estado, p.fcreacion as fecha_venta, p.hcreacion as hora_venta, p.monto_pedido, p.num_doc, " +
                      "p.tipo_doc, p.nom_cliente, p.dir_tipo_calle, p.dir_calle, p.dir_numero, p.tipo_despacho, " +
                      "p.dir_depto, c.nombre as comuna, p.sin_gente_txt, p.sin_gente_rut, p.sin_gente_dv, " +
                      "j.fecha, p.id_local, p.indicacion, p.observacion, " +
                      "p.id_zona, z.nombre AS zona, z.descripcion zonDescrip, h.hini, h.hfin, cli.cli_fon_cod_1 as codigo_fono, " +
                      "cli.cli_fon_num_1 as numero_fono, cli.cli_fon_cod_2 as codigo_fono2, cli.cli_fon_num_2 as numero_fono2, " +
                      "D.DIR_CONFLICTIVA, D.DIR_CONFLICTIVA_COMENTARIO, p.rut_cliente, p.dv_cliente, p.medio_pago " +
                      "FROM bo_pedidos p " +
                      " LEFT JOIN bo_estados e ON e.id_estado = p.id_estado " +
                      " LEFT JOIN bo_zonas z ON z.id_zona = p.id_zona " +
                      " LEFT JOIN bo_jornada_desp j ON j.id_jdespacho = p.id_jdespacho " +
                      " LEFT JOIN bo_horario_desp h ON h.id_hor_desp = j.id_hor_desp " +
                      " LEFT JOIN bo_comunas c ON c.id_comuna = p.id_comuna " +
                      " LEFT JOIN fo_clientes cli ON cli.cli_id = p.id_cliente " +
                      " LEFT JOIN FO_DIRECCIONES D ON D.DIR_ID = P.DIR_ID " +
                      "WHERE p.id_pedido = ? and p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";	

		logger.debug("SQLStmt: "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql  + " WITH UR");
			stm.setLong(1, id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
				desp.setCant_bins(rs.getInt("cant_bins"));
				desp.setEstado(rs.getString("estado"));
				desp.setId_estado(rs.getLong("id_estado"));
				desp.setId_pedido(rs.getLong("id_pedido"));
				desp.setId_jpicking(rs.getLong("id_jpicking"));
				desp.setId_zona(rs.getLong("id_zona"));
				desp.setZona_despacho(rs.getString("zona"));
				desp.setZona_descripcion(rs.getString("zonDescrip"));
				desp.setF_despacho(rs.getString("fecha"));
				desp.setH_ini(rs.getString("hini")); 
				desp.setH_fin(rs.getString("hfin"));
				desp.setNom_cliente(rs.getString("nom_cliente"));
				desp.setMedioPago(rs.getString("medio_pago"));
                if (rs.getString("dir_tipo_calle") != null) {
                    desp.setDir_tipo_calle(rs.getString("dir_tipo_calle"));
                } else {
                    desp.setDir_tipo_calle("");    
                }
                if (rs.getString("dir_calle") != null) {
                    desp.setDir_calle(rs.getString("dir_calle"));
                } else {
                    desp.setDir_calle("");    
                }
                if (rs.getString("dir_numero") != null) {
                    desp.setDir_numero(rs.getString("dir_numero"));
                } else {
                    desp.setDir_numero("");
                }
                if (rs.getString("dir_depto") != null) {
                    desp.setDir_depto(rs.getString("dir_depto"));
                } else {
                    desp.setDir_depto("");    
                }
				desp.setComuna(rs.getString("comuna"));
				desp.setF_ingreso(rs.getString("fecha_venta"));	
				desp.setH_ingreso(rs.getString("hora_venta"));
				desp.setNum_doc(rs.getLong("num_doc"));
				desp.setTipo_doc(rs.getString("tipo_doc"));
				desp.setCod_telefono(rs.getString("codigo_fono"));
				desp.setTelefono(rs.getString("numero_fono"));
				desp.setCod_telefono2(rs.getString("codigo_fono2"));
				desp.setTelefono2(rs.getString("numero_fono2"));
                if (rs.getString("sin_gente_txt") != null) {
                    desp.setPers_autorizada(rs.getString("sin_gente_txt"));
                } else {
                    desp.setPers_autorizada("");    
                }
                if ( rs.getString("sin_gente_rut") != null && rs.getString("sin_gente_dv") != null ) {
                    desp.setRutPersonaRetira(rs.getString("sin_gente_rut")+"-"+rs.getString("sin_gente_dv"));
                }
                if ( rs.getString("observacion") != null ) {
                    desp.setObservaciones(rs.getString("observacion"));
                } else {
                    desp.setObservaciones("");
                }
                if (rs.getString("indicacion") != null) {
                    desp.setIndicaciones(rs.getString("indicacion"));
                } else {
                    desp.setIndicaciones("");    
                }
				desp.setMonto_total(rs.getLong("monto_pedido"));
				desp.setDir_conflictiva(rs.getString("DIR_CONFLICTIVA"));
				desp.setDir_conflictiva_comentario(rs.getString("DIR_CONFLICTIVA_COMENTARIO"));
                desp.setTipoDespacho(rs.getString("tipo_despacho"));
                ClientesDTO cli = new ClientesDTO();
                cli.setRut(rs.getLong("rut_cliente"));
                cli.setDv(rs.getString("dv_cliente"));
                desp.setCliente(cli);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DespachosDAOException(e);
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


		return desp;
	
		
	}


	/**
	 * Cambia el estado a un pedido en su fase de despacho
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * 
	 * @throws DespachosDAOException
	 */
	public void doCambiaEstadoDespacho( long id_pedido, long id_estado ) throws DespachosDAOException {
		PreparedStatement stm = null;
		String SQLStmt =
				" UPDATE bo_pedidos " +
				" SET id_estado = ? " +
				" WHERE id_pedido = ? "
				;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, id_estado);
			stm.setLong(2, id_pedido);

			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		} catch (Exception e) {
			logger.debug("Problema en operación:"+ e);
			throw new DespachosDAOException(e);
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
     * @param criterio
     * @return
     */
    public List getDespachosParaMonitorByCriteria(DespachoCriteriaDTO criterio) throws DespachosDAOException {
        
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        List result = new ArrayList();

        long id_local   = 0;
        long id_zona    = 0;
        String str_fecha = null;
        
        id_local     = criterio.getId_local();
        id_zona      = criterio.getId_zona();
        str_fecha    = criterio.getF_despacho();
        
        // Filtros de estado
        String[] filtro_estados = criterio.getFiltro_estados();
        String estados_in = "";
        if ( filtro_estados != null && filtro_estados.length > 0 ){
            for (int i=0; i<filtro_estados.length; i++){
                estados_in += "," + filtro_estados[i];
            }
            estados_in = estados_in.substring(1);
        }
        
        logger.debug("Ejecutando DAO getDespachosParaMonitorByCriteria");
        
        //orden de columnas
        String sqlOrder = "";
        if ( criterio.getOrden_columnas() != null ) {
            String obj;         
            int x = 1;
            for (int i=0; i<criterio.getOrden_columnas().size(); i++) {
                if (x == 1) {
                    sqlOrder += " ORDER BY ";
                }
                obj = (String) criterio.getOrden_columnas().get(i);
                if ( x > 1 ) sqlOrder += " ,";                  
                sqlOrder += " "+obj+" ";
                x++;
            }
        }

        String sql = "select p.id_pedido, p.id_jpicking, p.id_jdespacho, p.cant_bins, p.id_estado as id_estado, " +
                     "e.nombre as estado, p.nom_cliente, p.dir_tipo_calle, p.dir_calle, p.dir_numero, p.dir_depto, c.nombre as comuna, " +
                     "j.fecha, p.id_local, p.id_zona, z.nombre AS zona, z.descripcion desc, h.hini, h.hfin, " +
                     "hp.hini as hini_jpick, hp.hfin as hfin_jpick, jp.fecha as fecha_picking, " +
                     "p.dir_calle dir_calle, p.dir_numero dir_numero, p.dir_depto dir_depto, p.tipo_despacho tipo_despacho, " +
                     "D.DIR_CONFLICTIVA, p.sin_gente_txt, p.sin_gente_rut, p.sin_gente_dv, " +
                     "pe.nro_guia_caso, PE.ID_RUTA, pe.reprogramada, p.tipo_doc, p.origen, " + 
                     "D.CONFIRMADA, D.LATITUD, D.LONGITUD " +
                     "FROM bo_pedidos p " +
                     "  LEFT JOIN FO_DIRECCIONES D ON D.DIR_ID = P.DIR_ID " +
                     "  LEFT JOIN bo_estados e ON e.id_estado = p.id_estado " +
                     "  LEFT JOIN bo_zonas z ON z.id_zona = p.id_zona " +
                     "  LEFT JOIN bo_jornada_desp j ON j.id_jdespacho = p.id_jdespacho " +
                     "  LEFT JOIN bo_horario_desp h ON h.id_hor_desp = j.id_hor_desp " +
                     "  LEFT JOIN bo_comunas c ON c.id_comuna = p.id_comuna " +
                     "  LEFT JOIN bo_jornadas_pick jp ON jp.id_jpicking = p.id_jpicking " +
                     "  LEFT JOIN bo_horario_pick hp ON hp.id_hor_pick = jp.id_hor_pick " +
                     "  left outer join bodba.bo_pedidos_ext pe on pe.ID_PEDIDO = p.ID_PEDIDO " +
                     " WHERE p.id_estado NOT IN (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ", 4, 20) ";
        
        // Filtro id_local
        if ( criterio.getId_pedido() > 0 ) {
            sql += " AND p.id_pedido = " + criterio.getId_pedido() + " ";
        }
        
        // Filtro id_local
        if ( id_local > 0 ) {
            sql += " AND p.id_local = " + id_local + " ";
        }
        
        // Filtro estados IN
        if ( !estados_in.equals("") ){ 
            sql += " AND p.id_estado IN (" + estados_in + ") " ;
        }  
        
        // Filtro ZONA
        if ( id_zona > 0 ) {
            sql += " AND p.id_zona = " + id_zona + " ";
        }       
        
        // Filtro fecha
        if ( str_fecha != null ) {
            sql += " AND j.fecha = '" + str_fecha + "' ";
        }
        
        if (criterio.getH_inicio() != null && !"".equalsIgnoreCase(criterio.getH_inicio()) ) {
            sql += " AND h.hini = '" + criterio.getH_inicio() + "' ";
        }

        if (criterio.getH_fin() != null && !"".equalsIgnoreCase(criterio.getH_inicio()) ) {
            sql += " AND h.hfin = '" + criterio.getH_fin() + "' ";
        }
        
        if ( criterio.getOrigen() != null && !criterio.getOrigen().equalsIgnoreCase("-1") ) {
            sql += " AND p.origen = '" + criterio.getOrigen() + "' ";
        }
        if ( criterio.getReprogramada() != null && !criterio.getReprogramada().equalsIgnoreCase("-1") && !criterio.getReprogramada().equalsIgnoreCase("") ) {
            if ("N".equalsIgnoreCase(criterio.getReprogramada())) {
                sql += " AND reprogramada = 0 ";
            } else {
                sql += " AND reprogramada > 0 ";
            }
        }
        
        sql += sqlOrder;
            
        logger.debug("SQL getDespachosParaMonitorByCriteria: " + sql );
        
        try {
            
            conn = this.getConnection();
                    
            stm = conn.prepareStatement( sql + " WITH UR" );
    
            rs = stm.executeQuery();
            while (rs.next()) {
                MonitorDespachosDTO mon1 = new MonitorDespachosDTO();
                if (rs.getString("cant_bins") == null ) {
                    mon1.setCant_bins(0);
                } else {
                    mon1.setCant_bins(rs.getInt("cant_bins"));    
                }                
                mon1.setEstado(rs.getString("estado"));
                mon1.setId_estado(rs.getLong("id_estado"));
                mon1.setId_pedido(rs.getLong("id_pedido"));
                if (rs.getString("id_jpicking") == null ) {
                    mon1.setId_jpicking(0);
                } else {
                    mon1.setId_jpicking(rs.getLong("id_jpicking"));
                }
                mon1.setId_zona(rs.getLong("id_zona"));
                mon1.setZona_despacho(rs.getString("zona"));
                mon1.setDesc_zona_desp(rs.getString("desc"));
                mon1.setF_despacho(rs.getString("fecha"));
                if (rs.getString("tipo_despacho") == null){
                    mon1.setTipo_despacho("N");
                }else{
                    mon1.setTipo_despacho(rs.getString("tipo_despacho"));
                }
                mon1.setReprogramada(rs.getInt("reprogramada"));
                mon1.setH_ini(rs.getString("hini"));
                mon1.setH_fin(rs.getString("hfin"));
                mon1.setComuna(rs.getString("comuna"));
                
                if ( rs.getString("fecha_picking") != null ) {
                    mon1.setHini_jpick(rs.getTime("hini_jpick"));
                    mon1.setHfin_jpick(rs.getTime("hfin_jpick"));
                    mon1.setFecha_picking(rs.getDate("fecha_picking"));
                }
                mon1.setDir_calle(rs.getString("dir_calle"));
                if (rs.getString("dir_numero") == null) {
                    mon1.setDir_numero("");   
                } else {
                    mon1.setDir_numero(rs.getString("dir_numero"));    
                }
                if ( rs.getString("dir_depto") == null ) {
                    mon1.setDir_depto("");    
                } else {
                    mon1.setDir_depto(rs.getString("dir_depto"));
                } 
                mon1.setDir_conflictiva(rs.getString("DIR_CONFLICTIVA"));
                if ( rs.getString("sin_gente_txt") != null ) {
                    mon1.setPersonaRetiraRecibe(rs.getString("sin_gente_txt"));
                } else {
                    mon1.setPersonaRetiraRecibe("");    
                }
                if ( rs.getString("sin_gente_rut") != null && rs.getString("sin_gente_dv") != null ) {
                    mon1.setRutCompletoPersona(rs.getString("sin_gente_rut") + "-" + rs.getString("sin_gente_dv"));
                } else {
                    mon1.setRutCompletoPersona("");
                }
                mon1.setOrigen(rs.getString("origen"));
                if ( rs.getString("nro_guia_caso") != null ) {
                    mon1.setNroGuiaCaso(rs.getLong("nro_guia_caso"));
                }
                if ( rs.getString("ID_RUTA") != null ) {
                    mon1.setIdRuta(rs.getLong("ID_RUTA"));
                }
                if ( rs.getString("tipo_doc") == null ) {
                    mon1.setTipoDocumento("");    
                } else {
                    mon1.setTipoDocumento(rs.getString("tipo_doc"));
                }
                if(rs.getInt("CONFIRMADA") == 0){
                    mon1.setConfirmada(false);
                }else{
                    mon1.setConfirmada(true);
                }
                mon1.setLatitud(rs.getDouble("LATITUD"));
                mon1.setLongitud(rs.getDouble("LONGITUD"));

                result.add(mon1);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
     * @return
     */
    public List getJornadasDespachoParaFiltro(long idLocal, String fecha, long idZona) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        List jornadas = new ArrayList();
        
        boolean conFecha = false;
        if ( fecha != null && !"".equalsIgnoreCase(fecha)) {
            conFecha = true;
        }
        
        String  sql = "select j.ID_JDESPACHO, h.HINI, h.HFIN, j.fecha " +
                      "from bodba.bo_jornada_desp j " +
                      "inner join bodba.bo_horario_desp h on h.ID_HOR_DESP = j.ID_HOR_DESP " +
                      "inner join bodba.bo_zonas z on z.ID_ZONA = j.ID_ZONA " +
                      "where ";
        if ( conFecha ) {
            sql += " j.FECHA = '" + fecha + "' ";
        } else {
            sql += " j.FECHA >= ? and j.FECHA <= ? ";
        }        
        if ( idLocal != -1 ) {
            sql += " and z.ID_LOCAL = " + idLocal;
        }
        
        if ( idZona != -1 ) {
            sql += " and z.ID_ZONA = " + idZona;
        }
        
        sql += " order by j.FECHA";
        
        try {            
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            if ( !conFecha ) {
                int diasMasMenos = 5;
                
                GregorianCalendar ini = new GregorianCalendar();
                ini.setFirstDayOfWeek(Calendar.MONDAY);
                
                GregorianCalendar fin = new GregorianCalendar();
                fin.setFirstDayOfWeek(Calendar.MONDAY);
                fin.add(Calendar.DAY_OF_YEAR, diasMasMenos);
                
                stm.setDate(1, new java.sql.Date(ini.getTimeInMillis()));
                stm.setDate(2, new java.sql.Date(fin.getTimeInMillis()));
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                JornadaDTO desp = new JornadaDTO();
                desp.setId_jornada(rs.getLong("ID_JDESPACHO"));
                desp.setF_jornada(rs.getString("fecha"));
                desp.setH_inicio(rs.getString("hini")); 
                desp.setH_fin(rs.getString("hfin"));
                jornadas.add(desp);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return jornadas;
    }
    
    
    public List getJornadasDespacho(int localId, Date fechaIni, Date fechaFin) throws DespachosDAOException {      
       PreparedStatement stm = null;
       ResultSet rs = null;
       List jornadas = new ArrayList();
       
       String  sql = "select h.hini, h.hfin "
          	+ "from bodba.bo_jornada_desp j inner join bodba.bo_horario_desp h on j.id_hor_desp = h.id_hor_desp "
          	+ "inner join bodba.bo_zonas z on z.id_zona = j.id_zona "
          	+ "where z.id_local = ? and j.fecha >= ? and j.fecha <= ? "
          	+ "group by h.hini, h.hfin " 
          	+ "order by h.hini ";

       
       try {            
           conn = this.getConnection();
           stm = conn.prepareStatement( sql + " WITH UR" );
           stm.setInt(1, localId);
           stm.setDate(2, fechaIni);
           stm.setDate(3, fechaFin);
           rs = stm.executeQuery();
           while (rs.next()) {
               JornadaDTO desp = new JornadaDTO();
               desp.setH_inicio(rs.getString("hini")); 
               desp.setH_fin(rs.getString("hfin"));
               jornadas.add(desp);
           }

       } catch (Exception e) {
           logger.debug("Problema:"+ e);
           throw new DespachosDAOException(e);
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
       return jornadas;
   }
    

    /**
     * @param ruta
     */
    public long addRuta(RutaDTO ruta) throws DespachosDAOException {
        PreparedStatement stm = null;
        ResultSet rs =  rs = null;
        long idRuta = 0;
        String sql = " insert into bo_ruta " +
                     "(id_fono_trans, id_chofer_trans, id_patente_trans, fecha_creacion, id_estado, id_local) " +
                     "values " +
                     "(?,?,?,?,?,?)";
        
        logger.debug("SQL addRuta: " + sql);
        
        try {

            conn = this.getConnection();
            
            stm = conn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            
            stm.setLong(1, ruta.getFono().getIdFono());
            stm.setLong(2, ruta.getChofer().getIdChofer());
            stm.setLong(3, ruta.getPatente().getIdPatente());
            stm.setDate(4, new Date(System.currentTimeMillis()) );
            stm.setLong(5, ruta.getEstado().getId_estado());
            stm.setLong(6, ruta.getLocal().getId_local());

            int i = stm.executeUpdate();
            logger.debug("rc: " + i);
             rs = stm.getGeneratedKeys();
            if (rs.next())
                idRuta = rs.getInt(1);

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
        return idRuta;
    }

    /**
     * @param pedidos
     * @return
     */
    public int addPedidosRuta(String pedidos, long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        int pedidosRuta = 0;
        String sql = "update bodba.bo_pedidos_ext set ID_RUTA = ? " +
                     "where ID_PEDIDO in (" + pedidos.replaceAll("-=-",",") + ") " +
                     "and ID_RUTA is null";
        
        logger.debug("SQL addPedidosRuta: " + sql);
        
        try {

            conn = this.getConnection();
            stm = conn.prepareStatement( sql );
            stm.setLong(1, idRuta);
            pedidosRuta = stm.executeUpdate();

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
        return pedidosRuta;
    }

    /**
     * @param log
     */
    public void addLogRuta(LogRutaDTO log) throws DespachosDAOException {
        PreparedStatement stm = null;
        String sql = "insert into bodba.bo_log_ruta (id_ruta, fecha, descripcion, id_usuario, id_estado) values (?,?,?,?,?) ";
        
        logger.debug("SQL addLogRuta: " + sql);
        
        try {

            conn = this.getConnection();
             stm = conn.prepareStatement( sql );
            stm.setLong(1, log.getIdRuta());
            stm.setDate(2, new Date(System.currentTimeMillis()) );
            stm.setString(3, log.getDescripcion());
            stm.setLong(4, log.getUsuario().getId_usuario());
            stm.setLong(5, log.getEstado().getId_estado());
            
            stm.executeUpdate();

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int addPedidoRuta(long idPedido, long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        int respuesta = 0;
        String sql = "update bodba.bo_pedidos_ext set ID_RUTA = ? " +
                     "where ID_PEDIDO = ? and ID_RUTA is null";
        
        logger.debug("SQL addPedidoRuta: " + sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql );
            stm.setLong(1, idRuta);
            stm.setLong(2, idPedido);
            respuesta = stm.executeUpdate();

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
        return respuesta;
    }

    /**
     * @param idLocal
     * @return
     */
    public List getRutasDisponibles(long idLocal) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        List rutas = new ArrayList();
        
        String  sql = "select r.ID_RUTA, r.CANT_BINS, ft.ID_FONO_TRANS, ft.CODIGO, ft.NUMERO, ft.NOMBRE, ch.ID_CHOFER_TRANS, ch.NOMBRE_CHOFER, " +
                      "pt.ID_PATENTE_TRANS, pt.PATENTE, pt.CANT_MAX_BINS, r.FECHA_HORA_SALIDA, r.fecha_creacion " +
                      "from bodba.bo_ruta r " +
                      "inner join bodba.bo_chofer_trans ch on ch.ID_CHOFER_TRANS = r.ID_CHOFER_TRANS " +
                      "inner join bodba.bo_fono_trans ft on ft.ID_FONO_TRANS = r.ID_FONO_TRANS " +
                      "inner join bodba.bo_patente_trans pt on pt.ID_PATENTE_TRANS = r.ID_PATENTE_TRANS " +
                      "inner join bodba.bo_emp_transporte et on et.ID_EMP_TRANSPORTE = ch.ID_EMP_TRANSPORTE " +
                      "where r.ID_ESTADO = 65 and r.ID_LOCAL = ? " +
                      "order by r.fecha_creacion ";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idLocal);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                RutaDTO ruta = new RutaDTO();
                ruta.setIdRuta(rs.getLong("ID_RUTA"));
                ruta.setCantBins(rs.getLong("CANT_BINS"));
                
                FonoTransporteDTO fono = new FonoTransporteDTO();
                fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                fono.setCodigo(rs.getLong("CODIGO"));
                fono.setNumero(rs.getLong("NUMERO"));
                fono.setNombre(rs.getString("NOMBRE"));
                ruta.setFono(fono);
                
                ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                ruta.setChofer(chofer);
                
                PatenteTransporteDTO patente = new PatenteTransporteDTO();
                patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                patente.setPatente(rs.getString("PATENTE"));
                patente.setCantMaxBins(rs.getLong("CANT_MAX_BINS"));
                ruta.setPatente(patente);
                
                ruta.setFechaSalida(rs.getString("FECHA_HORA_SALIDA"));
                ruta.setFechaCreacion(rs.getString("fecha_creacion"));
                rutas.add(ruta);
                
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return rutas;
    }

    /**
     * @param idRuta
     * @return
     */
    public RutaDTO getRutaById(long idRuta) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        RutaDTO ruta = new RutaDTO();
        boolean primera = true;
    
        
        String sql = "SELECT R.ID_RUTA, R.CANT_BINS, R.FECHA_CREACION, FT.ID_FONO_TRANS, FT.CODIGO, " 
            + "       FT.NUMERO, CH.ID_CHOFER_TRANS, CH.NOMBRE_CHOFER, PT.ID_PATENTE_TRANS, " 
            + "       PT.PATENTE, R.FECHA_HORA_SALIDA, E.ID_ESTADO, E.NOMBRE ESTADO, "
            + "       Z.ID_ZONA, Z.NOMBRE ZONA, JD.ID_JDESPACHO, HD.HINI, HD.HFIN, " 
            + "       P.CANT_BINS, L.ID_LOCAL, L.NOM_LOCAL "
            + "FROM BODBA.BO_RUTA R "
            + "     INNER JOIN BODBA.BO_CHOFER_TRANS  CH ON CH.ID_CHOFER_TRANS  =  R.ID_CHOFER_TRANS "
            + "     INNER JOIN BODBA.BO_FONO_TRANS    FT ON FT.ID_FONO_TRANS    =  R.ID_FONO_TRANS "
            + "     INNER JOIN BODBA.BO_PATENTE_TRANS PT ON PT.ID_PATENTE_TRANS =  R.ID_PATENTE_TRANS "
            + "     INNER JOIN BODBA.BO_ESTADOS        E ON  E.ID_ESTADO        =  R.ID_ESTADO "
            + "     INNER JOIN BODBA.BO_LOCALES        L ON  L.ID_LOCAL         =  R.ID_LOCAL "
            + "     LEFT OUTER JOIN BODBA.BO_PEDIDOS_EXT  PE ON PE.ID_RUTA      =  R.ID_RUTA "
            + "     LEFT OUTER JOIN BODBA.BO_PEDIDOS       P ON  P.ID_PEDIDO    = PE.ID_PEDIDO "
            + "     LEFT OUTER JOIN BODBA.BO_ZONAS         Z ON  Z.ID_ZONA      =  P.ID_ZONA "
            + "     LEFT OUTER JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JDESPACHO =  P.ID_JDESPACHO "
            + "     LEFT OUTER JOIN BODBA.BO_HORARIO_DESP HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP "
            + "WHERE R.ID_RUTA = ? and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idRuta);
            
            rs = stm.executeQuery();
            while (rs.next()) {                
                if ( primera ) {                
                    ruta.setIdRuta(rs.getLong("ID_RUTA"));
                    ruta.setCantBins(rs.getLong("CANT_BINS"));
                    
                    FonoTransporteDTO fono = new FonoTransporteDTO();
                    fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                    fono.setCodigo(rs.getLong("CODIGO"));
                    fono.setNumero(rs.getLong("NUMERO"));
                    ruta.setFono(fono);
                    
                    ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                    chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                    chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                    ruta.setChofer(chofer);
                    
                    PatenteTransporteDTO patente = new PatenteTransporteDTO();
                    patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                    patente.setPatente(rs.getString("PATENTE"));
                    ruta.setPatente(patente);
                    
                    ruta.setFechaSalida(rs.getString("FECHA_HORA_SALIDA"));
                    ruta.setFechaCreacion(rs.getString("fecha_creacion"));
                    
                    EstadoDTO estado = new EstadoDTO();
                    estado.setId_estado(rs.getLong("ID_ESTADO"));
                    estado.setNombre(rs.getString("estado"));
                    ruta.setEstado(estado);
                    
                    LocalDTO local = new LocalDTO();
                    local.setId_local(rs.getLong("id_local"));
                    local.setNom_local(rs.getString("nom_local"));
                    ruta.setLocal(local);
                    
                
                }

                boolean existeZona = false;
                for (int i=0; i < ruta.getZonas().size(); i++) {
                    ZonaDTO zona = (ZonaDTO) ruta.getZonas().get(i);
                    if ( zona.getId_zona() == rs.getLong("ID_ZONA") ) {
                        existeZona = true;
                    }                    
                }
                if (!existeZona) {
                    ZonaDTO zona = new ZonaDTO();
                    zona.setId_zona( rs.getLong("ID_ZONA") );
                    zona.setNombre(rs.getString("zona"));
                    ruta.getZonas().add(zona);
                }
                
                boolean existeJornada = false;
                for (int i=0; i < ruta.getJornadas().size(); i++) {
                    JornadaDTO jornada = (JornadaDTO) ruta.getJornadas().get(i);
                    if ( jornada.getId_jornada() == rs.getLong("ID_JDESPACHO") ) {
                        existeJornada = true;
                    }                    
                }
                if (!existeJornada) {
                    JornadaDTO jornada = new JornadaDTO();
                    jornada.setId_jornada( rs.getLong("ID_JDESPACHO") );
                    jornada.setH_inicio(rs.getString("HINI"));
                    jornada.setH_fin(rs.getString("HFIN"));
                    ruta.getJornadas().add(jornada);
                }
                
                
                primera = false;
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return ruta;
    }

    /**
     * @param criterio
     * @return
     */
    public List getRutasByCriterio(RutaCriterioDTO criterio) throws DespachosDAOException {
        
        List listaRutas = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String rutas = "";
        
        int pag = criterio.getPag();
        int regXpag = criterio.getRegsperpage();
        if (pag <= 0) { 
            pag = 1;
        }
        int iniReg = (pag - 1) * regXpag + 1;
        int finReg = pag * regXpag;
        
        String sql = " SELECT * FROM ( " +
                     " SELECT row_number() over( ORDER BY r.ID_RUTA desc ) as row, " +
                     " r.ID_RUTA " +
                     " from bodba.bo_ruta r " +
                     "  inner join ( " +
                     "    select distinct r.id_ruta " +
                     "    from  bodba.bo_ruta r" +
                     "      inner join bodba.bo_chofer_trans ch on ch.ID_CHOFER_TRANS = r.ID_CHOFER_TRANS " +
                     "      inner join bodba.bo_fono_trans ft on ft.ID_FONO_TRANS = r.ID_FONO_TRANS " +
                     "      inner join bodba.bo_patente_trans pt on pt.ID_PATENTE_TRANS = r.ID_PATENTE_TRANS " +
                     "      inner join bodba.bo_estados e on e.ID_ESTADO = r.ID_ESTADO" +
                     "      left outer join bodba.bo_pedidos_ext pe on pe.ID_RUTA = r.ID_RUTA " +
                     "      left outer join bodba.bo_pedidos p on p.ID_PEDIDO = pe.ID_PEDIDO " +
                     "      left outer join bodba.bo_jornada_desp jd on jd.ID_JDESPACHO = p.ID_JDESPACHO " +
                     "      left outer join bodba.bo_horario_desp h on h.id_hor_desp = jd.id_hor_desp " +
                     "    where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
        sql += obtenerWhereRutas(criterio);
        sql +=       "   ) as x on x.id_ruta = r.id_ruta ";
        sql +=       " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            
            String coma = "";
            while (rs.next()) {
                rutas += ( coma + rs.getLong("ID_RUTA") );
                coma = ",";
            }

            
            if ( rutas.length() > 0 ) {
        
                String sql2 =   "select r.ID_RUTA, r.fecha_creacion, ft.ID_FONO_TRANS, ft.CODIGO, ft.NUMERO, ch.ID_CHOFER_TRANS, ch.NOMBRE_CHOFER, " +
                                "pt.ID_PATENTE_TRANS, pt.PATENTE, r.FECHA_HORA_SALIDA, e.ID_ESTADO, e.NOMBRE estado, " +
                                "p.ID_PEDIDO, z.ID_ZONA, z.NOMBRE zona, jd.ID_JDESPACHO, hd.HINI, hd.HFIN, " +
                                "l.id_local, l.nom_local " +
                                "from bodba.bo_ruta r " +
                                "inner join bodba.bo_chofer_trans ch on ch.ID_CHOFER_TRANS = r.ID_CHOFER_TRANS " +
                                "inner join bodba.bo_fono_trans ft on ft.ID_FONO_TRANS = r.ID_FONO_TRANS " +
                                "inner join bodba.bo_patente_trans pt on pt.ID_PATENTE_TRANS = r.ID_PATENTE_TRANS " +
                                "inner join bodba.bo_estados e on e.ID_ESTADO = r.ID_ESTADO " +
                                "inner join bodba.bo_locales l on l.id_local = r.id_local " +
                                "left outer join bodba.bo_pedidos_ext pe on pe.ID_RUTA = r.ID_RUTA " +
                                "left outer join bodba.bo_pedidos p on p.ID_PEDIDO = pe.ID_PEDIDO " +
                                "left outer join bodba.bo_zonas z on z.ID_ZONA = p.ID_ZONA " +
                                "left outer join bodba.bo_jornada_desp jd on jd.ID_JDESPACHO = p.ID_JDESPACHO " +
                                "left outer join bodba.bo_horario_desp hd on hd.ID_HOR_DESP = jd.ID_HOR_DESP " +
                                "where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and r.ID_RUTA in (" + rutas + ") " +
                                "order by r.ID_RUTA desc, jd.ID_JDESPACHO ";
                
                conn = this.getConnection();
                stm = conn.prepareStatement(sql2 + " WITH UR");
                rs = stm.executeQuery();
                
                
                RutaDTO ruta = new RutaDTO();
                long oldIdRuta = 0;
                while (rs.next()) {                
                    if ( oldIdRuta != 0 && oldIdRuta != rs.getLong("ID_RUTA")) {
                        listaRutas.add(ruta);
                        ruta = new RutaDTO();
                    }
                    if ( oldIdRuta != rs.getLong("ID_RUTA") ) { 
                        ruta.setIdRuta(rs.getLong("ID_RUTA"));
                        
                        FonoTransporteDTO fono = new FonoTransporteDTO();
                        fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                        fono.setCodigo(rs.getLong("CODIGO"));
                        fono.setNumero(rs.getLong("NUMERO"));
                        ruta.setFono(fono);
                        
                        ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                        chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                        chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                        ruta.setChofer(chofer);
                        
                        PatenteTransporteDTO patente = new PatenteTransporteDTO();
                        patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                        patente.setPatente(rs.getString("PATENTE"));
                        ruta.setPatente(patente);
                        
                        ruta.setFechaSalida(rs.getString("FECHA_HORA_SALIDA")); // Para el listado dejamos en este parametro la fecha con la hora
                        ruta.setFechaCreacion(rs.getString("fecha_creacion"));
                        
                        EstadoDTO estado = new EstadoDTO();
                        estado.setId_estado(rs.getLong("ID_ESTADO"));
                        estado.setNombre(rs.getString("estado"));
                        ruta.setEstado(estado);

                        LocalDTO loc = new LocalDTO();
                        loc.setId_local(rs.getLong("id_local"));
                        loc.setNom_local(rs.getString("nom_local"));
                        ruta.setLocal(loc);
                        
                    }
                    if ( rs.getString("ID_ZONA") != null ) {
                        boolean existeZona = false;
                        for (int i=0; i < ruta.getZonas().size(); i++) {
                            ZonaDTO zona = (ZonaDTO) ruta.getZonas().get(i);
                            if ( zona.getId_zona() == rs.getLong("ID_ZONA") ) {
                                existeZona = true;
                            }                    
                        }
                        if (!existeZona) {
                            ZonaDTO zona = new ZonaDTO();
                            zona.setId_zona( rs.getLong("ID_ZONA") );
                            zona.setNombre(rs.getString("zona"));
                            ruta.getZonas().add(zona);
                        }
                    }
                    if ( rs.getString("ID_JDESPACHO") != null ) {
                        boolean existeJornada = false;
                        for (int i=0; i < ruta.getJornadas().size(); i++) {
                            JornadaDTO jornada = (JornadaDTO) ruta.getJornadas().get(i);
                            if ( jornada.getId_jornada() == rs.getLong("ID_JDESPACHO") ) {
                                existeJornada = true;
                            }                    
                        }
                        if (!existeJornada) {
                            JornadaDTO jornada = new JornadaDTO();
                            jornada.setId_jornada( rs.getLong("ID_JDESPACHO") );
                            jornada.setH_inicio(rs.getString("HINI"));
                            jornada.setH_fin(rs.getString("HFIN"));
                            ruta.getJornadas().add(jornada);
                        }
                    }
                    
                    oldIdRuta = rs.getLong("ID_RUTA");
                    
                }
                if ( ruta.getIdRuta() != 0 ) {
                    listaRutas.add(ruta);
                }
            }
            
        } catch (SQLException e) {
            throw new DespachosDAOException(String.valueOf(e.getErrorCode()),e);
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
        return listaRutas;
    }

    /**
     * @param criterio
     * @return
     */
    private String obtenerWhereRutas(RutaCriterioDTO criterio) {
        String sql = "";
        if ( criterio.getFechaDespacho().length() > 0 ) {
            sql += " AND jd.FECHA = '" + criterio.getFechaDespacho() + "'";
        }
        if ( criterio.getHIni() != null && !"".equalsIgnoreCase( criterio.getHIni() ) ) {
            sql += " AND h.hini = '" + criterio.getHIni() + "'";
        }
        if ( criterio.getHFin() != null && !"".equalsIgnoreCase( criterio.getHFin() ) ) {
            sql += " AND h.hfin = '" + criterio.getHFin() + "'";
        }        
        if ( criterio.getIdZona() != -1 ) {
            sql += " AND p.ID_ZONA = " + criterio.getIdZona();
        }
        if ( criterio.getIdChofer() != -1 ) {
            sql += " AND ch.ID_CHOFER_TRANS = " + criterio.getIdChofer();
        }
        if ( criterio.getIdPatente() != -1 ) {
            sql += " AND pt.ID_PATENTE_TRANS = " + criterio.getIdPatente();
        }
        if ( criterio.getIdEstado() != -1 ) {
            sql += " AND r.ID_ESTADO = " + criterio.getIdEstado();
        }
        if ( criterio.getIdRuta() != -1 ) {
            sql += " AND r.ID_RUTA = " + criterio.getIdRuta();
        }
        if ( criterio.getIdLocal() != -1 ) {
            sql += " AND r.ID_LOCAL = " + criterio.getIdLocal();
        }
        if ( criterio.getIdPedido() != -1 ) {
            sql += " AND p.ID_PEDIDO = " + criterio.getIdPedido();
        }
        if ( criterio.getClienteRut().length() > 0 ) {
            
            long rut = 0;
            try {
                rut = Long.parseLong(criterio.getClienteRut());    
            } catch (Exception e) { }            
            
            sql += " AND p.RUT_CLIENTE = " + rut;
        }
        if ( criterio.getClienteApellido().length() > 0 ) {
            sql += " AND UPPER(p.NOM_CLIENTE) like '%" + criterio.getClienteApellido().toUpperCase() + "%'";
        }
        return sql;
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountRutasByCriterio(RutaCriterioDTO criterio) throws DespachosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        double total = 0;
        
        String sql = " SELECT count(r.ID_RUTA) total " +
                     " from bodba.bo_ruta r " +
                     " inner join " +
                     " ( " +
                     "    select distinct r.id_ruta " +
                     "       from  bodba.bo_ruta r " +
                     "       inner join bodba.bo_chofer_trans ch on ch.ID_CHOFER_TRANS = r.ID_CHOFER_TRANS " +
                     "       inner join bodba.bo_fono_trans ft on ft.ID_FONO_TRANS = r.ID_FONO_TRANS " +
                     "       inner join bodba.bo_patente_trans pt on pt.ID_PATENTE_TRANS = r.ID_PATENTE_TRANS " +
                     "       inner join bodba.bo_estados e on e.ID_ESTADO = r.ID_ESTADO " +
                     "       left outer join bodba.bo_pedidos_ext pe on pe.ID_RUTA = r.ID_RUTA " +
                     "       left outer join bodba.bo_pedidos p on p.ID_PEDIDO = pe.ID_PEDIDO " +
                     "       left outer join bodba.bo_jornada_desp jd on jd.ID_JDESPACHO = p.ID_JDESPACHO " +
                     "       left outer join bodba.bo_horario_desp h on h.id_hor_desp = jd.id_hor_desp " +
                     "    where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") ";
        sql += obtenerWhereRutas(criterio);
        sql +=       "  ) as x on x.id_ruta = r.id_ruta ";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            throw new DespachosDAOException(String.valueOf(e.getErrorCode()),e);
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
        return total;
    }

    /**
     * @return
     */
    public List getEstadosRuta() throws DespachosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List estados = new ArrayList();
        
        String sql = " select e.ID_ESTADO, e.NOMBRE from bodba.bo_estados e where e.TIPO_ESTADO = 'RU'";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            
            while (rs.next()) {
                EstadoDTO estado = new EstadoDTO();
                estado.setId_estado( rs.getLong("ID_ESTADO") );
                estado.setNombre( rs.getString("NOMBRE") );
                estados.add(estado);
            }
            
        } catch (SQLException e) {
            throw new DespachosDAOException(String.valueOf(e.getErrorCode()),e);
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
        return estados;
    }

    /**
     * @param idZona
     * @param fecha
     * @return
     */
    public List getJornadasDespachoDisponiblesByZona(long idZona, String fecha, String tipoPedido) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        List jornadas = new ArrayList();
        
        boolean conFecha = false;
        if ( fecha != null && !"".equalsIgnoreCase(fecha)) {
            conFecha = true;
        }
        
        String  sql = "select j.ID_JDESPACHO, h.HINI, h.HFIN, j.fecha " +
                      "from bodba.bo_jornada_desp j " +
                      "inner join bodba.bo_horario_desp h on h.ID_HOR_DESP = j.ID_HOR_DESP " +
                      "inner join bodba.bo_zonas z on z.ID_ZONA = j.ID_ZONA " +
                      "where z.ID_ZONA = ? and ";
        if ( !Constantes.ORIGEN_JV_CTE.equalsIgnoreCase(tipoPedido) ) {
            sql += "j.CAPAC_DESPACHO > j.CAPAC_OCUPADA and ";
        }        
        if ( conFecha ) {
            sql += "j.FECHA = '" + fecha + "' ";
        } else {
            sql += "j.FECHA >= ? and j.FECHA <= ? ";
        }
        sql += "order by j.FECHA";
        
        try {            
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idZona);
            if ( !conFecha ) {
                int diasMas = 5;
                
                GregorianCalendar ini = new GregorianCalendar();
                ini.setFirstDayOfWeek(Calendar.MONDAY);
                
                GregorianCalendar fin = new GregorianCalendar();
                fin.setFirstDayOfWeek(Calendar.MONDAY);
                fin.add(Calendar.DAY_OF_YEAR, diasMas);
                
                stm.setDate(2, new java.sql.Date(ini.getTimeInMillis()));
                stm.setDate(3, new java.sql.Date(fin.getTimeInMillis()));
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                JornadaDTO desp = new JornadaDTO();
                desp.setId_jornada(rs.getLong("ID_JDESPACHO"));
                desp.setF_jornada(rs.getString("fecha"));
                desp.setH_inicio(rs.getString("hini")); 
                desp.setH_fin(rs.getString("hfin"));
                jornadas.add(desp);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return jornadas;
    }

    /**
     * @return
     */
    public List getLogRuta(long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List logs = new ArrayList();
        
        String sql = "select l.FECHA, u.ID_USUARIO, u.LOGIN, e.ID_ESTADO, e.NOMBRE, l.DESCRIPCION " +
                     "from bodba.bo_log_ruta l " +
                     "inner join bodba.bo_usuarios u on u.ID_USUARIO = l.ID_USUARIO " +
                     "inner join bodba.bo_estados e on e.ID_ESTADO = l.ID_ESTADO " +
                     "where l.ID_RUTA = ? " +
                     "order by l.FECHA desc ";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idRuta);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                LogRutaDTO log = new LogRutaDTO();
                log.setFecha( rs.getString("FECHA") );
                UserDTO usu = new UserDTO();
                usu.setId_usuario( rs.getLong("ID_USUARIO") );
                usu.setLogin( rs.getString("LOGIN") );
                log.setUsuario(usu);
                EstadoDTO est = new EstadoDTO();
                est.setId_estado( rs.getLong("ID_ESTADO") );
                est.setNombre( rs.getString("NOMBRE") );
                log.setEstado(est);
                log.setDescripcion( rs.getString("DESCRIPCION") );                
                logs.add(log);
            }
            
        } catch (SQLException e) {
            throw new DespachosDAOException(String.valueOf(e.getErrorCode()),e);
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
        return logs;
    }

    /**
     * @param idRuta
     * @return
     */
    public List getPedidosByRuta(long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List pedidos = new ArrayList();
       
        
        String sql = "SELECT P.ID_PEDIDO, P.ORIGEN, PE.NRO_GUIA_CASO, PE.REPROGRAMADA, P.DIR_TIPO_CALLE, "
            + "       P.DIR_CALLE, P.DIR_DEPTO, P.DIR_NUMERO, C.ID_COMUNA, C.NOMBRE COMUNA, " 
            + "       Z.ID_ZONA, Z.NOMBRE ZONA, JD.FECHA, HD.HINI, HD.HFIN, E.ID_ESTADO, " 
            + "       E.NOMBRE ESTADO, PE.FECHA_HORA_LLEGADA_DOM, PE.FECHA_HORA_SALIDA_DOM, " 
            + "       P.ID_USUARIO, P.TIPO_DOC, P.MEDIO_PAGO, P.NOM_TBANCARIA, "
            + "       D.LATITUD, D.LONGITUD, D.CONFIRMADA, P.TIPO_DESPACHO "
            + "FROM BODBA.BO_PEDIDOS P "
            + "     INNER JOIN BODBA.BO_PEDIDOS_EXT  PE ON PE.ID_PEDIDO    =  P.ID_PEDIDO "
            + "     INNER JOIN BODBA.BO_COMUNAS       C ON  C.ID_COMUNA    =  P.ID_COMUNA "
            + "     INNER JOIN BODBA.BO_ZONAS         Z ON  Z.ID_ZONA      =  P.ID_ZONA "
            + "     INNER JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JDESPACHO =  P.ID_JDESPACHO "
            + "     INNER JOIN BODBA.BO_HORARIO_DESP HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP "
            + "     INNER JOIN BODBA.BO_ESTADOS       E ON  E.ID_ESTADO    =  P.ID_ESTADO "
            + "     LEFT  JOIN FODBA.FO_DIRECCIONES   D ON  D.DIR_ID       =  P.DIR_ID "
            + "WHERE PE.ID_RUTA = ? and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") "
            + "ORDER BY P.FCREACION DESC";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idRuta);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId_pedido(rs.getLong("ID_PEDIDO"));
                ped.setTipo_despacho(rs.getString("TIPO_DESPACHO"));
                PedidoExtDTO pe = new PedidoExtDTO();
                pe.setNroGuiaCaso(rs.getLong("NRO_GUIA_CASO"));
                pe.setReprogramada(rs.getInt("REPROGRAMADA"));
                ped.setOrigen(rs.getString("ORIGEN"));
                if (rs.getString("TIPO_DOC") == null) {
                    ped.setTipo_doc("");
                } else {
                    ped.setTipo_doc(rs.getString("TIPO_DOC"));
                }
                if (rs.getString("MEDIO_PAGO") == null) {
                    ped.setMedio_pago("");
                } else {
                    ped.setMedio_pago(rs.getString("MEDIO_PAGO"));
                }
                if (rs.getString("NOM_TBANCARIA") == null) {
                    ped.setNom_tbancaria("");
                } else {
                    ped.setNom_tbancaria(rs.getString("NOM_TBANCARIA"));
                }
                if (rs.getString("DIR_TIPO_CALLE") == null) {
                    ped.setDir_tipo_calle("");
                } else {
                    ped.setDir_tipo_calle(rs.getString("DIR_TIPO_CALLE"));
                }
                if (rs.getString("DIR_CALLE") == null) {
                    ped.setDir_calle("");
                } else {
                    ped.setDir_calle(rs.getString("DIR_CALLE"));
                }
                if (rs.getString("DIR_NUMERO") == null) {
                    ped.setDir_numero("");
                } else {
                    ped.setDir_numero(rs.getString("DIR_NUMERO"));
                }
                if (rs.getString("DIR_DEPTO") == null) {
                    ped.setDir_depto("");
                } else {
                    ped.setDir_depto(rs.getString("DIR_DEPTO"));
                }
                ped.setId_comuna(rs.getLong("ID_COMUNA"));
                ped.setNom_comuna(rs.getString("comuna"));
                ped.setId_zona(rs.getLong("ID_ZONA"));
                ped.setNomZona(rs.getString("zona"));
                ped.setFdespacho(rs.getString("FECHA"));
                ped.setHdespacho(rs.getString("HINI"));
                ped.setHfindespacho(rs.getString("HFIN"));
                ped.setId_estado(rs.getLong("ID_ESTADO"));
                ped.setEstado(rs.getString("estado"));
                ped.setId_usuario(rs.getLong("id_usuario"));
                
                if (rs.getInt("CONFIRMADA") == 1){
                    ped.setConfirmada(true);
                }else{
                    ped.setConfirmada(false);
                }
                ped.setLatitud(rs.getDouble("LATITUD"));
                ped.setLongitud(rs.getDouble("LONGITUD"));

                if (rs.getString("FECHA_HORA_LLEGADA_DOM") == null) {
                    pe.setFcHoraLlegadaDomicilio("");
                } else {
                    pe.setFcHoraLlegadaDomicilio(rs.getString("FECHA_HORA_LLEGADA_DOM"));
                }
                if (rs.getString("FECHA_HORA_SALIDA_DOM") == null) {
                    pe.setFcHoraSalidaDomicilio("");
                } else {
                    pe.setFcHoraSalidaDomicilio(rs.getString("FECHA_HORA_SALIDA_DOM"));
                }
                ped.setPedidoExt(pe);
                pedidos.add(ped);
            }
            
        } catch (SQLException e) {
            throw new DespachosDAOException(String.valueOf(e.getErrorCode()),e);
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
        return pedidos;
    }

    /**
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int delPedidoRuta(long idPedido) throws DespachosDAOException {
        PreparedStatement stm = null;
        int respuesta = 0;
        String sql = "update bodba.bo_pedidos_ext set ID_RUTA = ? " +
                     "where ID_PEDIDO = ? ";
        
        logger.debug("SQL delPedidoRuta: " + sql);
        
        try {

            conn = this.getConnection();
            
            stm = conn.prepareStatement( sql );
            
            stm.setNull(1, java.sql.Types.INTEGER);
            stm.setLong(2, idPedido);
            respuesta = stm.executeUpdate();

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
        return respuesta;
    }

    /**
     * @param estado
     * @param idRuta
     */
    public void setEstadoRuta(int estado, long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        String sql = "";
        
        if ( estado == Constantes.ESTADO_RUTA_EN_RUTA) {
            sql = "update bodba.bo_ruta set ID_ESTADO = ?, FECHA_HORA_SALIDA = ? where ID_RUTA = ? ";
        } else {
            sql = "update bodba.bo_ruta set ID_ESTADO = ? where ID_RUTA = ? ";    
        }
        
        logger.debug("SQL setEstadoRuta: " + sql);
        
        try {

            conn = this.getConnection();
            
            stm = conn.prepareStatement( sql );
            stm.setInt(1, estado);
            if ( estado == Constantes.ESTADO_RUTA_EN_RUTA) {
                stm.setDate(2, new Date(System.currentTimeMillis()) );
                stm.setLong(3, idRuta);
            } else {            
                stm.setLong(2, idRuta);
            }
            stm.executeUpdate();

            
        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
     * @param idRuta
     */
    public void liberarPedidosByRuta(long idRuta) throws DespachosDAOException {
        PreparedStatement stm = null;
        String sql = "update bodba.bo_pedidos_ext set ID_RUTA = ? " +
                     "where ID_RUTA = ? ";
        
        logger.debug("SQL liberarPedidosByRuta: " + sql);
        
        try {

            conn = this.getConnection();
            
            stm = conn.prepareStatement( sql );
            
            stm.setNull(1, java.sql.Types.INTEGER);
            stm.setLong(2, idRuta);
            stm.executeUpdate();

        } catch (Exception e) {
            logger.debug("Problema en operación:"+ e);
            throw new DespachosDAOException(e);
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
     * @param idPedido
     * @return
     */
    public RutaDTO getRutaByPedido(long idPedido) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        RutaDTO ruta = new RutaDTO();
        
        String  sql = "select r.ID_RUTA, r.ID_ESTADO, f.ID_FONO_TRANS, f.NOMBRE, f.CODIGO, f.NUMERO, ch.ID_CHOFER_TRANS, " +
                      "ch.NOMBRE_CHOFER, pa.ID_PATENTE_TRANS, pa.PATENTE, r.FECHA_HORA_SALIDA " +
                      "from bodba.bo_ruta r " +
                      "inner join bodba.bo_pedidos_ext pe on pe.ID_RUTA = r.ID_RUTA " +
                      "inner join bodba.bo_fono_trans f on f.ID_FONO_TRANS = r.ID_FONO_TRANS " +
                      "inner join bodba.bo_chofer_trans ch on ch.ID_CHOFER_TRANS = r.ID_CHOFER_TRANS " +
                      "inner join bodba.bo_patente_trans pa on pa.ID_PATENTE_TRANS = r.ID_PATENTE_TRANS " +
                      "where pe.ID_PEDIDO = ?";        
        try {            
            
            conn = this.getConnection();
            stm = conn.prepareStatement( sql + " WITH UR" );
            stm.setLong(1, idPedido);
            
            rs = stm.executeQuery();
            while (rs.next()) {          
                ruta.setIdRuta(rs.getLong("ID_RUTA"));
                
                FonoTransporteDTO fono = new FonoTransporteDTO();
                fono.setIdFono(rs.getLong("ID_FONO_TRANS"));
                fono.setCodigo(rs.getLong("CODIGO"));
                fono.setNumero(rs.getLong("NUMERO"));
                ruta.setFono(fono);
                
                ChoferTransporteDTO chofer = new ChoferTransporteDTO();
                chofer.setIdChofer(rs.getLong("ID_CHOFER_TRANS"));
                chofer.setNombre(rs.getString("NOMBRE_CHOFER"));
                ruta.setChofer(chofer);
                
                PatenteTransporteDTO patente = new PatenteTransporteDTO();
                patente.setIdPatente(rs.getLong("ID_PATENTE_TRANS"));
                patente.setPatente(rs.getString("PATENTE"));
                ruta.setPatente(patente);
                
                EstadoDTO estado = new EstadoDTO();
                estado.setId_estado(rs.getLong("ID_ESTADO"));
                ruta.setEstado(estado);
                
                if ( rs.getString("FECHA_HORA_SALIDA") != null ) {
                    ruta.setFechaSalida(rs.getString("FECHA_HORA_SALIDA"));
                } else {
                    ruta.setFechaSalida("");    
                }
                
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return ruta;
    }

    /**
     * @param idPedido
     * @return
     */
    public JornadaDTO getJornadaDespachoOriginalDePedidoReprogramado(long idPedido) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        JornadaDTO jor = new JornadaDTO();
        
        String  sql = "SELECT JD.ID_JDESPACHO, JD.FECHA FC_DESPACHO, HD.HINI HORA_INI, HD.HFIN HORA_FIN " +
                      "FROM BODBA.BO_JORNADA_DESP JD " +
                      "    INNER JOIN BODBA.BO_HORARIO_DESP HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " +
                      "    INNER JOIN BODBA.BO_REPROGRAMACION_DESP R ON R.ID_JDESPACHO_ANT = JD.ID_JDESPACHO " +
                      "WHERE R.ID_REPROGRAMACION = " +
                      "( " +
                      "SELECT MIN( R.ID_REPROGRAMACION ) " +
                      "FROM BODBA.BO_PEDIDOS P " +
                      "    INNER JOIN BODBA.BO_REPROGRAMACION_DESP R ON R.ID_PEDIDO = P.ID_PEDIDO " +
                      " WHERE P.ID_PEDIDO = ? and P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
                      ")";        
        try {            
            
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setLong(1, idPedido);
            
            rs = stm.executeQuery();
            if (rs.next()) {          
                jor.setId_jornada(rs.getLong("ID_JDESPACHO"));
                jor.setF_jornada(rs.getString("FC_DESPACHO"));
                jor.setH_inicio(rs.getString("HORA_INI"));
                jor.setH_fin(rs.getString("HORA_FIN"));
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return jor;
    }

    /**
     * @param idPedido
     * @return
     */
    public List getReprogramacionesByPedido(long idPedido) throws DespachosDAOException {      
        PreparedStatement stm = null;
        ResultSet rs = null;
        List reprogramaciones = new ArrayList();
        
        String  sql = "SELECT R.ID_RESPONSABLE_DESP, RES.NOMBRE_RESPONSABLE, R.ID_MOTIVO_DESP, MOT.MOTIVO, " +
                      "R.FECHA FECHA_REPROGRAMACION, JD.FECHA FC_DESPACHO, HD.HINI, HD.HFIN, U.LOGIN " +
                      "FROM BODBA.BO_REPROGRAMACION_DESP R " +
                      "INNER JOIN BODBA.BO_RESPONSABLE_DESPACHO RES ON RES.ID_RESPONSABLE_DESP = R.ID_RESPONSABLE_DESP " +
                      "INNER JOIN BODBA.BO_MOTIVO_DESPACHO MOT ON MOT.ID_MOTIVO_DESP = R.ID_MOTIVO_DESP " +
                      "INNER JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JDESPACHO = R.ID_JDESPACHO_ANT " +
                      "INNER JOIN BODBA.BO_HORARIO_DESP HD ON HD.ID_HOR_DESP  = JD.ID_HOR_DESP " +
                      "INNER JOIN BODBA.BO_USUARIOS U ON U.ID_USUARIO = R.ID_USUARIO " +
                      "WHERE R.ID_PEDIDO = ? " +
                      "ORDER BY R.ID_REPROGRAMACION";        
        try {            
            
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setLong(1, idPedido);
            
            rs = stm.executeQuery();
            while (rs.next()) {          
                ReprogramacionDTO repro = new ReprogramacionDTO();
                ObjetoDTO responsableReprogramacion = new ObjetoDTO();
                responsableReprogramacion.setIdObjeto(rs.getLong("ID_RESPONSABLE_DESP"));
                responsableReprogramacion.setNombre(rs.getString("NOMBRE_RESPONSABLE"));
                repro.setResponsableReprogramacion(responsableReprogramacion);
                ObjetoDTO motivoReprogramacion = new ObjetoDTO();
                motivoReprogramacion.setIdObjeto(rs.getLong("ID_MOTIVO_DESP"));
                motivoReprogramacion.setNombre(rs.getString("MOTIVO"));
                repro.setMotivoReprogramacion(motivoReprogramacion);
                repro.setFechaReprogramacion(rs.getString("FECHA_REPROGRAMACION"));                
                JornadaDTO jor = new JornadaDTO();
                jor.setF_jornada(rs.getString("FC_DESPACHO"));
                jor.setH_inicio(rs.getString("HINI"));
                jor.setH_fin(rs.getString("HFIN"));
                repro.setJornadaDespachoAnterior(jor);
                repro.setUsuario(rs.getString("LOGIN"));
                reprogramaciones.add(repro);
            }

        } catch (Exception e) {
            logger.debug("Problema:"+ e);
            throw new DespachosDAOException(e);
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
        return reprogramaciones;
    }	
}
