package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.ProductosPedidoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickRelacionDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.BarraAuditoriaSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.BarraDetallePedidosRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.FOSustitutosCriteriosDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickSustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.IdPedidoCantDTO;
import cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetPedRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDetalleResumenDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.TPAuditSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.TPDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TPRegistroPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.BolException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosDAOException;

/**
 * Clase que permite consultar las Rondas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcRondasDAO implements RondasDAO{

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
	
	/**
	 * detalle de pedidos que se encuentran sobre umbral y deben ser marcados como no pickeados al actualizar la ronda
	 */
	List detallesPedidosSobreUmbral;
	
	
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
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws RondasDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws RondasDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new RondasDAOException(e);
		}
	}
	// ************ Métodos de Negocio *************** //
	
	/**
	 * Agrega registro al log del pedido
	 * 
	 * @param  id_ronda long 
	 * @param  login String 
	 * @param  log String 
	 * 
	 * @throws RondasDAOException
	 */
	public void doAddLogRonda(long id_ronda, String login, String log)
		throws RondasDAOException {
		
		PreparedStatement stm=null;
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(
							"INSERT INTO bo_log_rondas (id_ronda, usuario, descr, fechahora) " +
							"VALUES (?, ?, ?, ?)"
							);
			stm.setLong(1, id_ronda);
			stm.setString(2, login);
			stm.setString(3, log);
			stm.setDate(4,new Date(System.currentTimeMillis()));
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
    public boolean getRondaConFaltantes(long id_ronda) throws RondasDAOException{
		PreparedStatement stm=null;
		ResultSet rs=null;
		boolean res = false;
		
		System.out.println("en getRondaConFaltantes:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "SELECT DR.ID_RONDA, R.ID_ESTADO, COUNT(*) AS PROD_SPICK, "
                   + "       SUM(DR.CANT_SPICK) AS CANT_PROD_SPICK "
                   + "FROM BODBA.BO_RONDAS R "
                   + "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_RONDA = R.ID_RONDA "
                   + "WHERE R.ID_ESTADO = 13 "
                   + "  AND R.ID_RONDA = ? "
                   + "  AND DR.CANT_SPICK > 0 "
                   + "GROUP BY DR.ID_RONDA, R.ID_ESTADO ";
	
		System.out.println("SQL : " + SQL);
		System.out.println("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1,id_ronda);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }

    
    public boolean setEstadoVerDetalle(long id_ronda) throws RondasDAOException{
		PreparedStatement stm=null;
		boolean res = false;
		
		logger.debug("en setEstadoVerDetalle:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "UPDATE BODBA.BO_RONDAS R "
                   + "   SET R.ESTADO_VER_DETALLE = 'S' "
                   + "WHERE R.ID_RONDA = " + id_ronda;
	
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL);
			int Numreg = stm.executeUpdate();
			
			if (Numreg > 0) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }

    public boolean setEstadoImpEtiqueta(long id_ronda) throws RondasDAOException{
		PreparedStatement stm = null;
		boolean res = false;
		
		logger.debug("en setEstadoImpEtiqueta:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "UPDATE BODBA.BO_RONDAS R "
                   + "   SET R.ESTADO_IMP_ETIQUETA = 'S' "
                   + "WHERE R.ID_RONDA = " + id_ronda;
	
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL);
			int Numreg = stm.executeUpdate();
			
			if (Numreg > 0) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }


    public boolean setFechaImpListadoPKL(long id_ronda) throws RondasDAOException{
		PreparedStatement stm=null;
		boolean res = false;
		
		logger.debug("en setFechaImpListadoPKL:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "UPDATE BODBA.BO_RONDAS R "
                   + "   SET R.FECHA_IMP_LIST_PKL = CURRENT TIMESTAMP "
                   + "WHERE R.ID_RONDA = " + id_ronda;
	
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL);
			int Numreg = stm.executeUpdate();
			
			if (Numreg > 0) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }


    public boolean setFechaIniciaJornadaPKL(long id_ronda) throws RondasDAOException{
		PreparedStatement stm=null;
		boolean res = false;
		
		logger.debug("en setFechaIniciaJornadaPKL:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "UPDATE BODBA.BO_RONDAS R "
                   + "   SET R.FECHA_INI_RONDA_PKL = CURRENT TIMESTAMP "
                   + "WHERE R.ID_RONDA = " + id_ronda;
	
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL);
			int Numreg = stm.executeUpdate();
			
			if (Numreg > 0) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }
    

    public boolean ExisteFechaIniciaJornadaPKL(long id_ronda) throws RondasDAOException{
		PreparedStatement stm=null;
		ResultSet rs=null;
		boolean res = false;
		
		logger.debug("en ExisteFechaIniciaJornadaPKL:");
		//leer los datos de la tabla bo_prod_sector
		String SQL = "SELECT R.FECHA_INI_RONDA_PKL "
                   + "FROM BODBA.BO_RONDAS R "
                   + "WHERE R.ID_RONDA = " + id_ronda;
	
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			rs = stm.executeQuery();
			rs.next();
			if (rs.getString("FECHA_INI_RONDA_PKL") != null) {
				res = true;
			}

		}catch (SQLException e) {
		    throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return res;
    }
    
    
	/**
	 * Obtiene listado del log de una ronda
	 * 
	 * @param  id_ronda long 
	 * @return List LogSimpleDTO
	 * @throws RondasDAOException 
	 */
	public List getLogRonda(long id_ronda)	throws RondasDAOException{
		
		List result = new ArrayList();
		
		PreparedStatement stm =null;
		ResultSet rs=null;

		String SQLStmt = 
			" SELECT id_log, usuario, fechahora, descr " +
			" FROM bo_log_rondas " +
			" WHERE id_ronda = ? " +
			" ORDER BY fechahora desc ";
			
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setLong(1,id_ronda);

			rs = stm.executeQuery();
			while (rs.next()) {
				LogSimpleDTO log1 = new LogSimpleDTO();
				log1.setId_log(rs.getLong("id_log"));
				log1.setUsuario(rs.getString("usuario"));
				log1.setDescripcion(rs.getString("descr"));
				log1.setFecha(rs.getString("fechahora"));
				result.add(log1);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene listado de estados de una ronda
	 * 
	 * @return List EstadoDTO
	 * @throws RondasDAOException 
	 */
	public List getEstadosRonda() throws RondasDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {
			String cadQuery=	"	SELECT id_estado,nombre " +
								"	FROM bo_estados " +
								"	WHERE tipo_estado= ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setString(1,"RD");
			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO log1 = new EstadoDTO();
				log1.setId_estado(rs.getLong("id_estado"));
				log1.setNombre(rs.getString("nombre"));
				result.add(log1);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Retorna listado de rondas de acuerdo a un criterio
	 * 
	 * @param  criterio RondasCriteriaDTO 
	 * @return List MonitorRondasDTO
	 * @throws RondasDAOException 
	 */
	public List getRondasByCriteriaCMO(RondasCriteriaDTO criterio) throws RondasDAOException {

		PreparedStatement stm = null;
		ResultSet rs = null;
		
		List result = new ArrayList();

		long id_estado 	= 0;
		long id_jornada = 0;
		long id_ronda	= 0;
		long id_sector	= 0;
		long id_local 	= 0;
		long id_zona    = 0;
		String str_fecha = null;
		String SQLStmt = "";
		String tipo_ve = null;
		String str_zona = "";
		String esPickingLight = "";
		String CampoIdPedido  = "";
		String CamposSector   = "";
		String CamposSector2  = "";
		String TablaSector    = "";
		
		id_estado 	= criterio.getId_estado();
		id_jornada 	= criterio.getId_jornada();
		id_ronda	= criterio.getId_ronda();
		id_sector	= criterio.getId_sector();
		id_local	= criterio.getId_local();
		str_fecha	= criterio.getF_ronda();
		tipo_ve     = criterio.getTipo_ve();
		id_zona     = criterio.getId_zona();
		esPickingLight = criterio.getEsPickingLight();
		
		//paginacion
		int pag 	= criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		
		
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;	
		
		logger.debug("Ejecutando DAO getRondasByCriteria");
		logger.debug("id_local:"+id_local );
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		
//		orden de columnas
		String SQLOrder="";
		if (criterio.getOrden_columnas()!=null){
			
			String  obj;			
			int x=1;
			for (int i=0; i<criterio.getOrden_columnas().size(); i++) {
				if (x==1){
					SQLOrder += " ORDER BY ";
					}
				obj = (String)criterio.getOrden_columnas().get(i);
				logger.debug(  "Columna : "+obj );
				
				if (x>1) SQLOrder += " ,";					
				SQLOrder += " "+obj+" ";
				x++;
				}
		}
		
		if (esPickingLight.equals("S")){//incorpora id_pedido
		    CampoIdPedido = "DR.ID_PEDIDO, ";
		}else{
		    CamposSector  = " s.nombre as sector, s.id_sector,";
		    CamposSector2 = " s.nombre, s.id_sector,";
		    TablaSector   = "  JOIN bo_sector s ON s.id_sector = r.id_sector ";
		}

		if (id_zona > 0){
		    str_zona = "     JOIN BODBA.BO_PEDIDOS P ON P.ID_PEDIDO = DR.ID_PEDIDO AND P.ID_ZONA = " + id_zona + " AND P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ")  ";
		}
		
		// Paginador
		SQLStmt +=
			" SELECT * FROM ( " +
			" SELECT row_number() over("+SQLOrder+") as row, ";
		
		// SQL principal
		SQLStmt += CampoIdPedido +
			" r.id_ronda, r.id_jpicking, SUM(DR.CANTIDAD) CANTIDAD_PRODUCTOS, r.id_estado, e.nombre as estado," +
			CamposSector + " r.id_local, r.fcreacion fcreacion, r.ffin_picking FFIN_PICKING, " +
			" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(CURRENT_TIMESTAMP) - TIMESTAMP(r.fcreacion) ) ) AS dif_crea , " +
			" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(CURRENT_TIMESTAMP) - TIMESTAMP(r.fini_picking) ) ) AS dif_picking, " +
			" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(r.ffin_picking) - TIMESTAMP(r.fini_picking)) ) AS dif_termino, " +
			" r.tipo_ve , SUM(dr.cant_spick) cant_spick, r.ESTADO_IMP_ETIQUETA, r.ESTADO_VER_DETALLE, " +
			" r.FECHA_IMP_LIST_PKL, r.FECHA_INI_RONDA_PKL, r.ESTADO_AUDIT_SUSTITUCION " +
			" FROM bo_rondas r " +
			"  JOIN bo_estados e ON e.id_estado = r.id_estado " +
			TablaSector +
			"  JOIN bo_jornadas_pick j ON j.id_jpicking = r.id_jpicking AND j.id_local = ? " +
			"  JOIN bo_detalle_rondas dr ON dr.id_ronda = r.id_ronda " +
			str_zona +
			" WHERE 1=1 " + 
			"   AND r.id_local = ?";	
		
		// Filtro id_estado
		if (id_estado > 0){
			SQLStmt += " AND r.id_estado = " + id_estado + " ";
		}
		
		// Filtro id_jornada
		if (id_jornada > 0){
			SQLStmt += " AND r.id_jpicking = " + id_jornada + " ";
		}	
		
		// Filtro id_pedido
		if (id_ronda > 0){
			SQLStmt += " AND r.id_ronda = " + id_ronda + " ";
		}
		
		// Filtro id_sector
		if (esPickingLight.equals("S")){
		    SQLStmt += " AND r.id_sector = 0 ";
		}else{
			if (id_sector > 0){
				SQLStmt += " AND r.id_sector = " + id_sector + " ";
			}
		}

		//		Filtro tipo_ve
		if (tipo_ve != null){
			SQLStmt += " AND r.tipo_ve = '" + tipo_ve + "' ";
		}
		
		// Filtro fecha
		if (str_fecha != null){
			SQLStmt += " AND j.fecha = '" + str_fecha + "' ";
		}

		// Agrupacion
		
		SQLStmt += " GROUP BY " + CampoIdPedido + "r.id_ronda, r.id_jpicking, r.id_estado, e.nombre, "
				+ CamposSector2 + " r.id_local, r.fcreacion, r.ffin_picking, " 
				+" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(CURRENT_TIMESTAMP) - TIMESTAMP(r.fcreacion) ) ), " 
				+" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(CURRENT_TIMESTAMP) - TIMESTAMP(r.fini_picking) ) ), " 
				+" TIMESTAMPDIFF(2, CHAR(TIMESTAMP(r.ffin_picking) - TIMESTAMP(r.fini_picking)) ), " 
				+ " r.tipo_ve, r.ESTADO_IMP_ETIQUETA, r.ESTADO_VER_DETALLE, r.FECHA_IMP_LIST_PKL, "
                + " r.FECHA_INI_RONDA_PKL, r.ESTADO_AUDIT_SUSTITUCION ";
		// Paginador
		SQLStmt +=
			") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
		

		logger.debug("SQLStmt: "+SQLStmt);
		
		try {
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_local);
			stm.setLong(2, id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				
				MonitorRondasDTO ronda = new MonitorRondasDTO();
				if (esPickingLight.equals("S")){
				    ronda.setId_pedido(rs.getLong("ID_PEDIDO"));
				}else{
				    ronda.setSector(rs.getString("sector"));
					ronda.setId_sector(rs.getLong("id_sector"));
				}
				ronda.setCant_prods(rs.getDouble("CANTIDAD_PRODUCTOS"));
				ronda.setEstado(rs.getString("estado"));
				ronda.setId_estado(rs.getLong("id_estado"));
				ronda.setId_jornada(rs.getLong("id_jpicking"));
				ronda.setId_local(rs.getLong("id_local"));
				ronda.setId_ronda(rs.getLong("id_ronda"));
				ronda.setF_creacion(rs.getString("fcreacion"));
				ronda.setDif_creacion(rs.getInt("dif_crea"));
				ronda.setDif_picking(rs.getInt("dif_picking"));
				ronda.setDif_termino(rs.getInt("dif_termino"));
				ronda.setTipo_ve(rs.getString("tipo_ve"));
				ronda.setCant_spick(rs.getDouble("cant_spick"));
				ronda.setEstadoImpEtiqueta(rs.getString("ESTADO_IMP_ETIQUETA"));
				ronda.setEstadoVerDetalle(rs.getString("ESTADO_VER_DETALLE"));
				ronda.setFecha_imp_listado_pkl(rs.getString("FECHA_IMP_LIST_PKL"));
				ronda.setFecha_inico_ronda_pkl(rs.getString("FECHA_INI_RONDA_PKL"));
                ronda.setEstadoAuditSustitucion(rs.getString("ESTADO_AUDIT_SUSTITUCION"));
				result.add(ronda);				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene número de registros de una búsuqe da de rondas por criterio
	 * 
	 * @param  criterio RondasCriteriaDTO 
	 * @return long
	 * @throws RondasDAOException
	 */
	public long getCountRondasByCriteria(RondasCriteriaDTO criterio) throws RondasDAOException {
		
		
		PreparedStatement stm  = null;
		ResultSet rs  = null;
		long count = 0;

		long id_estado 	= 0;
		long id_jornada = 0;
		long id_ronda	= 0;
		long id_sector	= 0;
		long id_local	= 0;
		String str_fecha = null;
		String tipo_ve = null;
		
		id_estado 	= criterio.getId_estado();
		id_jornada 	= criterio.getId_jornada();
		id_ronda	= criterio.getId_ronda();
		id_sector	= criterio.getId_sector();
		id_local	= criterio.getId_local();
		str_fecha	= criterio.getF_ronda();
		tipo_ve = criterio.getTipo_ve();
		
		logger.debug("Ejecutando DAO getCountRondasByCriteria");
		logger.debug("id_local:"+id_local );
		
		String SQLStmt = 
			" SELECT count(*) as cantidad " +
			" FROM bo_rondas r" +
			"  JOIN bo_estados e ON e.id_estado = r.id_estado " +
			"  JOIN bo_sector s ON s.id_sector = r.id_sector AND r.id_local = ? " +
			"  JOIN bo_jornadas_pick j ON j.id_jpicking = r.id_jpicking AND j.id_local = ? " +
			" WHERE 1=1 "
			;	
		
		// Filtro id_estado
		if (id_estado > 0){
			SQLStmt += " AND r.id_estado = " + id_estado + " ";
		}
		
		// Filtro id_jornada
		if (id_jornada > 0){
			SQLStmt += " AND r.id_jpicking = " + id_jornada + " ";
		}	
		
		// Filtro id_pedido
		if (id_ronda > 0){
			SQLStmt += " AND r.id_ronda = " + id_ronda + " ";
		}
		
		// Filtro id_sector
		if (id_sector > 0){
			SQLStmt += " AND r.id_sector = " + id_sector + " ";
		}		
		
		//		Filtro tipo_ve
		if (tipo_ve != null){
			SQLStmt += " AND r.tipo_ve = '" + tipo_ve + "' ";
		}
		
		// Filtro fecha
		if (str_fecha != null){
			SQLStmt += " AND j.fecha = '" + str_fecha + "' ";
		}
		
	
		logger.debug( "SQL:" + SQLStmt );
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" ) ;
			stm.setLong(1,id_local);	
			stm.setLong(2,id_local);	
			
			
			rs = stm.executeQuery();
			if (rs.next()) {
				count = rs.getLong("cantidad");
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene lista de rondas para un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List MonitorRondasDTO
	 * @throws RondasDAOException
	 */
	public List getRondasByIdPedido(long id_pedido) throws RondasDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs =null; 
		String SQL = "";
		List result = new ArrayList();

		logger.debug("Ejecutando DAO getRondasByIdPedido");
		
		try {
			SQL = "SELECT SUM(DR.CANTIDAD) AS CANT_PRODUCTOS, R.ID_RONDA, ID_JPICKING, "
                + "       R.ID_ESTADO, E.NOMBRE AS ESTADO, S.NOMBRE AS SECTOR, "
                + "       R.ESTADO_AUDIT_SUSTITUCION "
                + "FROM BODBA.BO_RONDAS R "
                + "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_RONDA = R.ID_RONDA "
                + "     LEFT JOIN BODBA.BO_ESTADOS    E ON E.ID_ESTADO = R.ID_ESTADO "
                + "     LEFT JOIN BODBA.BO_SECTOR     S ON S.ID_SECTOR = R.ID_SECTOR "
                + "WHERE ID_PEDIDO = ? "
                + "GROUP BY R.ID_RONDA, ID_JPICKING, CANT_PRODUCTOS, R.ID_ESTADO, "
                + "         E.NOMBRE, S.NOMBRE, R.ESTADO_AUDIT_SUSTITUCION";
			conn = this.getConnection();					
			stm = conn.prepareStatement(SQL + " WITH UR");
	
			stm.setLong(1, id_pedido);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				
				MonitorRondasDTO mon1 = new MonitorRondasDTO();
				mon1.setCant_prods(rs.getDouble("cant_productos"));
				mon1.setEstado(rs.getString("estado"));
				mon1.setId_estado(rs.getLong("id_estado"));
				mon1.setId_jornada(rs.getLong("id_jpicking"));
				mon1.setId_ronda(rs.getLong("id_ronda"));
				if (rs.getString("sector") == null){
				    mon1.setSector(Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
				}else{
				    mon1.setSector(rs.getString("sector"));   
				}
                mon1.setEstadoAuditSustitucion(rs.getString("ESTADO_AUDIT_SUSTITUCION"));
				result.add(mon1);
				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene detalle de una ronda.
	 * 
	 * @param  id_ronda long 
	 * @return RondaDTO
	 * @throws RondasDAOException
	 */
	public RondaDTO getRondaById(long id_ronda) throws RondasDAOException {
		
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		RondaDTO ronda 			= null;

		logger.debug("Ejecutando DAO getRondaById");
		
		String SQLStmt = 
			"SELECT id_ronda, id_jpicking, id_usuario, cant_productos, r.id_estado, " +
			"       e.nombre as estado, s.nombre as sector, fcreacion, r.id_local, " +
			"       fini_picking, ffin_picking, r.tipo_ve, " +
			//	---------- mod_ene09 - ini------------------------
			"       r.tipo_picking, r.id_usuario_fiscal, " +
			"       r.e1, r.e2, r.e3,r.e4,r.e5,r.e6,r.e7, " +
			//	---------- mod_ene09 - fin------------------------
			"       FECHA_IMP_LIST_PKL, FECHA_INI_RONDA_PKL " +
			"FROM bo_rondas r " +
			"     LEFT JOIN bo_estados e ON e.id_estado = r.id_estado " +
			"     LEFT JOIN bo_sector  s ON s.id_sector = r.id_sector " +
			"WHERE id_ronda = ?"
			;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {
			
			conn = this.getConnection();			
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");	
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			while (rs.next()) {
				ronda = new RondaDTO();
				ronda.setCant_prods(rs.getDouble("cant_productos"));
				ronda.setEstado(rs.getString("estado"));
				ronda.setF_creacion(rs.getString("fcreacion"));
				ronda.setPickeador(rs.getLong("id_usuario"));
				ronda.setId_ronda(rs.getLong("id_ronda"));
				ronda.setId_estado(rs.getLong("id_estado"));
				ronda.setId_jpicking(rs.getLong("id_jpicking"));
				ronda.setSector(rs.getString("sector"));
				ronda.setId_local(rs.getLong("id_local"));
				ronda.setFini_picking(rs.getTimestamp("fini_picking"));
				ronda.setFfin_picking(rs.getTimestamp("ffin_picking"));
				ronda.setTipo_ve(rs.getString("tipo_ve"));
				
				//	---------- mod_ene09 - ini------------------------
				ronda.setTipo_picking(rs.getString("tipo_picking"));
				ronda.setFiscalizador(rs.getLong("id_usuario_fiscal"));
				ronda.setE1(rs.getInt("e1"));
				ronda.setE2(rs.getInt("e2"));
				ronda.setE3(rs.getInt("e3"));
				ronda.setE4(rs.getInt("e4"));
				ronda.setE5(rs.getInt("e5"));
				ronda.setE6(rs.getInt("e6"));
				ronda.setE7(rs.getInt("e7"));				
				//	---------- mod_ene09 - fin------------------------
				ronda.setFecha_inico_ronda_pkl(rs.getTimestamp("FECHA_INI_RONDA_PKL"));
				ronda.setFecha_imp_listado_pkl(rs.getTimestamp("FECHA_IMP_LIST_PKL"));

			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return ronda;
			
	}
	
	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas y ops que están en estado 
	 * Validadas y En Picking
	 * 
	 * @param  ProcRondasPropuestasDTO criterio con id_sector, id_jornada, id_localm y tipo_ve
	 * @return List RondaPropuestaDTO
	 * @throws RondasDAOException
	 */
	public List getRondasPropuestasDet(ProcRondasPropuestasDTO criterio) throws RondasDAOException {
		//long id_local;
		long id_sector;
		long id_jornada;
		String tipo_ve=null;
		
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		List result 			= new ArrayList();

		logger.debug("Ejecutando DAO getRondasPropuestas");
		
		//id_local = criterio.getId_local();
		id_sector = criterio.getId_sector();
		id_jornada = criterio.getId_jornada();
		tipo_ve = criterio.getTipo_ve();
		
		String SQLtipo_ve = "";
		if((tipo_ve!=null)&&(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE))){
			SQLtipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}
		else{
			SQLtipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";
		}
		String SQLid_zona ="";
		if (criterio.getId_zona() > 0){
			SQLid_zona = " AND P.ID_ZONA = " + criterio.getId_zona();
		}
		
		String SQLStmt = "";
		SQLStmt = "SELECT D.ID_DETALLE, COALESCE(D.CANT_SPICK,0) CANT, D.ID_PEDIDO ID_PEDIDO,  D.ID_SECTOR ID_SECTOR, " 
                + "       P.ID_JPICKING ID_JPICKING,  S.NOMBRE SECTOR, J.FECHA FECHA, H.HINI HINI, H.HFIN HFIN, "        
                + "       C.NOMBRE AS COMUNA, Z.NOMBRE AS ZONA, P.TIPO_DESPACHO TIPO_DESPACHO, " 
                + "       CASE WHEN PICK.ID_DRONDA IS NOT NULL THEN 'S' "
                + "       ELSE 'N' "
                + "       END AS PROD_SPICK_CON_ANT, "
                + "       D.DESCRIPCION "
                + "FROM BODBA.BO_DETALLE_PEDIDO D "     
                + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS R  ON (D.ID_DETALLE = R.ID_DETALLE) "      
                + "     JOIN BODBA.BO_PEDIDOS P              ON P.ID_PEDIDO   = D.ID_PEDIDO   AND "
                + "                                             P.ID_ESTADO IN (" + Constantes.ID_ESTAD_PEDIDO_VALIDADO + ", " 
                                                                                  + Constantes.ID_ESTAD_PEDIDO_EN_PICKING + ") "                                                                              
                + SQLtipo_ve + SQLid_zona 
                + "     JOIN BODBA.BO_SECTOR         S       ON S.ID_SECTOR   = D.ID_SECTOR "     
                + "     JOIN BODBA.BO_JORNADAS_PICK J        ON J.ID_JPICKING = P.ID_JPICKING AND J.ID_JPICKING = ? "      
                + "     JOIN BODBA.BO_HORARIO_PICK H         ON H.ID_HOR_PICK = J.ID_HOR_PICK        "
                + "     JOIN BODBA.BO_COMUNAS C              ON C.ID_COMUNA   = P.ID_COMUNA "        
                + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA " 
                + "     LEFT JOIN (SELECT MAX(DR2.ID_DRONDA) AS ID_DRONDA, DR2.ID_DETALLE, "
                + "                       DP2.ID_SECTOR, DP2.ID_PEDIDO "
                + "                FROM BODBA.BO_DETALLE_PEDIDO DP2 "
                + "                     JOIN BODBA.BO_DETALLE_RONDAS DR2 ON DR2.ID_DETALLE = DP2.ID_DETALLE "
                + "                     JOIN BODBA.BO_RONDAS R2 ON R2.ID_RONDA = DR2.ID_RONDA "
                + "                WHERE DP2.CANT_SPICK > 0 "
                + "                GROUP BY DR2.ID_DETALLE, DP2.ID_SECTOR, DP2.ID_PEDIDO "
                + "                ORDER BY DR2.ID_DETALLE ASC) PICK ON PICK.ID_DETALLE = D.ID_DETALLE AND "
                + "                                                     PICK.ID_SECTOR  = D.ID_SECTOR AND "
                + "                                                     PICK.ID_PEDIDO  = P.ID_PEDIDO "
                + "WHERE D.ID_SECTOR = ? " 
                + "  AND (R.ID_DETALLE IS NULL OR R.CANTIDAD <= D.CANT_SOLIC) " 
                + "GROUP BY D.ID_DETALLE, D.ID_PEDIDO, P.ID_JPICKING, D.CANT_SPICK, D.ID_SECTOR, S.NOMBRE, " 
                + "         J.FECHA, H.HINI, H.HFIN, C.NOMBRE, Z.NOMBRE,Z.ORDEN_ZONA, TIPO_DESPACHO, " 
                + "         PICK.ID_DRONDA, D.DESCRIPCION "
                + "HAVING COALESCE(D.CANT_SPICK,0) > 0 "
                + "ORDER BY Z.ORDEN_ZONA ASC, C.NOMBRE, D.ID_PEDIDO ";
	    
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_jornada: " + id_jornada);
		logger.debug("id_sector: " + id_sector);
		logger.debug("tipo_ve: "+tipo_ve);
		
		try {
			
			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );	
			stm.setLong(1, id_jornada);
			stm.setLong(2, id_sector);			
			rs = stm.executeQuery();
			while (rs.next()) {				
				RondaPropuestaDTO rp = new RondaPropuestaDTO();
				rp.setH_inicio(rs.getString("hini"));
				rp.setH_fin(rs.getString("hfin"));
				rp.setId_jornada(rs.getLong("id_jpicking"));
				rp.setId_op(rs.getLong("id_pedido"));
				if (rs.getString("tipo_despacho") == null){
				    rp.setTipo_despacho("N");
				}else{
				    rp.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				rp.setId_sector(rs.getLong("id_sector"));
				rp.setSector(rs.getString("sector"));
				rp.setCant_prods(rs.getDouble("cant"));
				rp.setComuna(rs.getString("comuna"));
				rp.setZona(rs.getString("zona"));
				rp.setProd_SPick_con_ant(rs.getString("PROD_SPICK_CON_ANT"));
				result.add(rp);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("Se listaron " + result.size() + " registros");
		return result;				
	}

	
	public List getRondasPropuestas(ProcRondasPropuestasDTO criterio) throws RondasDAOException {
		long id_sector;
		long id_jornada;
		String tipo_ve=null;
		
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		List result 			= new ArrayList();

		logger.debug("Ejecutando DAO getRondasPropuestas");
        
        id_sector = criterio.getId_sector();
		id_jornada = criterio.getId_jornada();
		tipo_ve = criterio.getTipo_ve();
		
		String SQLtipo_ve = "";
		if((tipo_ve!=null)&&(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE))){
			SQLtipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}
		else{
			SQLtipo_ve = " AND P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";
		}
		String SQLid_zona ="";
		if (criterio.getId_zona() > 0){
			SQLid_zona = " AND P.ID_ZONA = " + criterio.getId_zona();
		}
        //Esto fue agregado, puesto que en local de la reina extrañamente le aparecían pedidos de la florida
        //disponibles para pickear (IM 29/09/11) 
        String SQLid_local ="";
        if ( criterio.getId_local() > 0 ) {
            SQLid_local = " AND P.ID_LOCAL = " + criterio.getId_local();
        }
		
		String SQLStmt = "SELECT D.ID_PEDIDO, P.ORIGEN, P.ID_JPICKING,  D.ID_SECTOR, S.NOMBRE as SECTOR, "
                       + "J.FECHA , H.HINI, H.HFIN,  C.NOMBRE AS COMUNA, Z.NOMBRE AS ZONA, P.TIPO_DESPACHO, "
                       + "         sum(D.CANT_SPICK ) as CANT, 	"
                       + "       CASE WHEN coalesce(x.cantidad, 0) > 0  THEN 'S' "
                       + "       ELSE 'N' "
                       + "       END AS PROD_SPICK_CON_ANT "
                       + "FROM BODBA.BO_DETALLE_PEDIDO D    " 
                       + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS R  ON (D.ID_DETALLE = R.ID_DETALLE) "      
                       + "     JOIN BODBA.BO_PEDIDOS P              ON P.ID_PEDIDO   = D.ID_PEDIDO   AND "
                       + "                                             P.ID_ESTADO IN (" + Constantes.ID_ESTAD_PEDIDO_VALIDADO + ", " 
                                                                                         + Constantes.ID_ESTAD_PEDIDO_EN_PICKING + ") "
                       + SQLtipo_ve + SQLid_zona + SQLid_local      
                       + "     JOIN BODBA.BO_SECTOR         S       ON S.ID_SECTOR   = D.ID_SECTOR "
                       + "     JOIN BODBA.BO_JORNADAS_PICK J        ON J.ID_JPICKING = P.ID_JPICKING AND J.ID_JPICKING = ? "      
                       + "     JOIN BODBA.BO_HORARIO_PICK H         ON H.ID_HOR_PICK = J.ID_HOR_PICK       "
                       + "     JOIN BODBA.BO_COMUNAS C              ON C.ID_COMUNA   = P.ID_COMUNA        "
                       + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                       + "     left outer join ( "
                       + "          select dp.id_pedido, count(*) as cantidad from bo_detalle_rondas dr " 
                       + "          inner join bo_detalle_pedido dp on dp.id_detalle = dr.id_detalle "
                       + "          inner join bo_pedidos bp on bp.id_pedido = dp.id_pedido and bp.ID_JPICKING = ? "
                       + "          where bp.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and dr.CANT_SPICK > 0 and dp.id_sector = ? "
                       + "          group by dp.id_pedido "
                       + "         )  x on  x.id_pedido = d.id_pedido "
                       + "WHERE D.ID_SECTOR = ? " 
                       + "  AND (R.ID_DETALLE IS NULL OR R.CANTIDAD <= D.CANT_SOLIC) " 
                       + "GROUP BY D.ID_PEDIDO, P.ORIGEN, P.ID_JPICKING,  D.ID_SECTOR, S.NOMBRE,  "
                       + "         J.FECHA , H.HINI, H.HFIN,  C.NOMBRE, Z.NOMBRE, P.TIPO_DESPACHO, x.cantidad "
                       + "HAVING   sum(D.CANT_SPICK )   > 0 "  
                       + "ORDER BY Z.NOMBRE ASC, C.NOMBRE, D.ID_PEDIDO ";

		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_jornada: " + id_jornada);
		logger.debug("id_sector: " + id_sector);
		logger.debug("tipo_ve: "+tipo_ve);
		
		try {
			
			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );	
			stm.setLong(1, id_jornada);
			stm.setLong(2, id_jornada);
			stm.setLong(3, id_sector);			
			stm.setLong(4, id_sector);
			rs = stm.executeQuery();
			while (rs.next()) {				
				RondaPropuestaDTO rp = new RondaPropuestaDTO();
				rp.setH_inicio(rs.getString("hini"));
				rp.setH_fin(rs.getString("hfin"));
				rp.setId_jornada(rs.getLong("id_jpicking"));
				rp.setId_op(rs.getLong("id_pedido"));
				if (rs.getString("tipo_despacho") == null){
				    rp.setTipo_despacho("N");
				}else{
				    rp.setTipo_despacho(rs.getString("tipo_despacho"));
				}
				rp.setId_sector(rs.getLong("id_sector"));
				rp.setSector(rs.getString("sector"));
				rp.setCant_prods(rs.getDouble("cant"));
				rp.setComuna(rs.getString("comuna"));
				rp.setZona(rs.getString("zona"));
				rp.setProd_SPick_con_ant(rs.getString("PROD_SPICK_CON_ANT"));
                rp.setOrigenPedido(rs.getString("ORIGEN"));
				result.add(rp);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("Se listaron " + result.size() + " registros");
		return result;				
	}

	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas y ops que están en estado 
	 * Validadas y En Picking
	 * 
	 * @param  ProcRondasPropuestasDTO criterio con id_sector, id_jornada, id_localm y tipo_ve
	 * @return List RondaPropuestaDTO
	 * @throws RondasDAOException
	 */
	public RondaPropuestaDTO getRondasPropuestasPKL(ProcRondasPropuestasDTO criterio) throws RondasDAOException {
		//long id_local;
		long id_pedido;
		long id_jornada;
		long id_sector;
		String tipo_ve=null;
		RondaPropuestaDTO rp = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		//List result  = new ArrayList();

		logger.debug("Ejecutando DAO getRondasPropuestas");
		
		//id_local   = criterio.getId_local();
		id_jornada = criterio.getId_jornada();
		id_pedido  = criterio.getId_pedido();
		tipo_ve    = criterio.getTipo_ve();
		id_sector  = criterio.getId_sector();
		
		String SQLtipo_ve = "";
		if((tipo_ve!=null)&&(tipo_ve.equals(Constantes.TIPO_VE_SPECIAL_CTE))){
			SQLtipo_ve = "P.TIPO_VE = '"+Constantes.TIPO_VE_SPECIAL_CTE+"' ";
		}else{
			SQLtipo_ve = "P.TIPO_VE = '"+Constantes.TIPO_VE_NORMAL_CTE+"' ";
		}
		
		String SQL = "";
	    SQL = "SELECT DP.ID_PEDIDO, SUM(COALESCE(DP.CANT_SPICK,0)) CANT, "
            + "       P.ID_JPICKING, J.FECHA, H.HINI, H.HFIN, S.ID_SECTOR, S.NOMBRE AS SECTOR, "
            + "       C.NOMBRE AS COMUNA, Z.NOMBRE AS ZONA, P.TIPO_DESPACHO "
            + "FROM BODBA.BO_DETALLE_PEDIDO DP "
            + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS DR ON DP.ID_DETALLE = DR.ID_DETALLE "
            + "     JOIN BODBA.BO_PEDIDOS P              ON P.ID_PEDIDO   = DP.ID_PEDIDO AND "
            + "                                             P.ID_ESTADO IN (" + Constantes.ID_ESTAD_PEDIDO_VALIDADO + ", " 
                                                                              + Constantes.ID_ESTAD_PEDIDO_EN_PICKING + ") AND "
            + "                                             " + SQLtipo_ve
            + "     JOIN BODBA.BO_SECTOR S               ON S.ID_SECTOR = ? "
            + "     JOIN BODBA.BO_JORNADAS_PICK J        ON J.ID_JPICKING = P.ID_JPICKING AND J.ID_JPICKING = ? "
            + "     JOIN BODBA.BO_HORARIO_PICK H         ON H.ID_HOR_PICK = J.ID_HOR_PICK "
            + "     JOIN BODBA.BO_COMUNAS C              ON C.ID_COMUNA   = P.ID_COMUNA "
            + "     JOIN BODBA.BO_ZONAS Z                ON Z.ID_ZONA     = P.ID_ZONA "
            + "WHERE DP.ID_PEDIDO = ? "
            + "  AND (DR.ID_DETALLE IS NULL OR DR.CANTIDAD <= DP.CANT_SOLIC) "
            + "  AND COALESCE(DP.CANT_SPICK,0) > 0 "
            + "GROUP BY DP.ID_PEDIDO, P.ID_JPICKING, S.ID_SECTOR, S.NOMBRE, "
            + "         J.FECHA, H.HINI, H.HFIN, C.NOMBRE, Z.NOMBRE, Z.ORDEN_ZONA, P.TIPO_DESPACHO "
            + "ORDER BY Z.ORDEN_ZONA ASC, C.NOMBRE, DP.ID_PEDIDO";
	    
		logger.debug("SQL: " + SQL);
		logger.debug("id_sector : " + id_sector);
		logger.debug("id_jornada: " + id_jornada);
		logger.debug("id_pedido : " + id_pedido);
		logger.debug("tipo_ve   : " + tipo_ve);
		
		try {
			conn = this.getConnection();
			
			stm = conn.prepareStatement(SQL + " WITH UR");	
			stm.setLong(1, id_sector);
			stm.setLong(2, id_jornada);
			stm.setLong(3, id_pedido);
			rs = stm.executeQuery();
			
			if (rs.next()) {				
				rp = new RondaPropuestaDTO();
				rp.setId_op(rs.getLong("ID_PEDIDO"));
				rp.setCant_prods(rs.getDouble("CANT"));
				rp.setId_jornada(rs.getLong("ID_JPICKING"));
				rp.setH_inicio(rs.getString("HINI"));
				rp.setH_fin(rs.getString("HFIN"));
				rp.setId_sector(rs.getLong("ID_SECTOR"));
				rp.setSector(rs.getString("SECTOR"));
				rp.setComuna(rs.getString("COMUNA"));
				rp.setZona(rs.getString("ZONA"));
				if (rs.getString("TIPO_DESPACHO") == null){
				    rp.setTipo_despacho("N");
				}else{
				    rp.setTipo_despacho(rs.getString("TIPO_DESPACHO"));
				}
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return rp;
	}

	
	/**
	 * Cambia estado a una ronda
	 * 
	 * @param  id_estado long 
	 * @param  id_ronda long 
	 * 
	 * @throws RondasDAOException
	 */
	public void doCambiaEstadoRonda(long id_estado, long id_ronda) throws RondasDAOException {
		
		
		PreparedStatement stm=null;
		String SQLStmt = 
				" UPDATE  bo_rondas " +
				" SET id_estado = ? " +
				" WHERE id_ronda = ? "
				;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setLong(1, id_estado);
			stm.setLong(2, id_ronda);
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Inserta un nuevo registro de ronda
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long, nuevo id
	 * @throws RondasDAOException
	 */
	public long setCreaRonda(CreaRondaDTO ronda) throws RondasDAOException {
		PreparedStatement stm=null;
		ResultSet rs = null;
		long id_ronda=-1L;
		
		String SQLStmt = 
				" INSERT INTO BO_RONDAS  " +
				"(id_sector, id_jpicking, id_estado, id_local, cant_productos, fcreacion, tipo_ve)" +
				"VALUES ( ? ,? , ? , ?, ? , CURRENT TIMESTAMP, ?)";
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_sector:"	+ ronda.getId_sector());
		logger.debug("id_jpicking:"	+ ronda.getId_jpicking());
		logger.debug("id_estado:"	+ ronda.getId_estado());
		logger.debug("id_local:"	+ ronda.getId_local());	
		logger.debug("tipo_ve:"		+ronda.getTipo_ve());
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt ,Statement.RETURN_GENERATED_KEYS );
			
			stm.setLong(1, ronda.getId_sector());
			stm.setLong(2, ronda.getId_jpicking());
			stm.setLong(3, ronda.getId_estado()); //estado rondas : creada
			stm.setLong(4, ronda.getId_local());
			
			double suma=0.0;
			for (int i =0; i<ronda.getPedidos().size();i++){
				suma += ((IdPedidoCantDTO)ronda.getPedidos().get(i)).getCant();				
			}
			logger.debug("cantidad de productos de la ronda nueva:"+suma);
			stm.setDouble(5, suma);
			
			if (ronda.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)){
				stm.setString(6, Constantes.TIPO_VE_SPECIAL_CTE);	
			}else{
				stm.setString(6, Constantes.TIPO_VE_NORMAL_CTE);
			}
			
			logger.debug("id_local:"+ronda.getId_local());
			int i = stm.executeUpdate();			
			
			logger.debug("Resultado Ejecución: " + i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {				
				 id_ronda = rs.getLong(1);
				 logger.debug("el id_ronda nuevo es:"+id_ronda);
			}else{
			    logger.debug("No puede obtener nuevo id_ronda");
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return id_ronda;
	}

	
	/**
	 * Inserta un nuevo registro de ronda
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long, nuevo id
	 * @throws RondasDAOException
	 */
	public long setCreaRondaPKL(CreaRondaDTO ronda) throws RondasDAOException {
		PreparedStatement stm=null;
		ResultSet rs = null;
		long id_ronda=-1L;
		
		String SQLStmt = 
				" INSERT INTO BO_RONDAS  " +
				"(id_sector, id_jpicking, id_estado, id_local, cant_productos, fcreacion, tipo_ve, tipo_picking)" +
				"VALUES ( ? ,? , ? , ?, ? , CURRENT TIMESTAMP, ?, 'L')";
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_sector  : " + ronda.getId_sector());
		logger.debug("id_jpicking: " + ronda.getId_jpicking());
		logger.debug("id_estado  : " + ronda.getId_estado());
		logger.debug("id_local   : " + ronda.getId_local());	
		logger.debug("tipo_ve    : " + ronda.getTipo_ve());
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt ,Statement.RETURN_GENERATED_KEYS );
			
			stm.setLong(1, ronda.getId_sector());
			stm.setLong(2, ronda.getId_jpicking());
			stm.setLong(3, ronda.getId_estado()); //estado rondas : creada
			stm.setLong(4, ronda.getId_local());
			
			logger.debug("cantidad de productos de la ronda nueva: "+ronda.getCant_prod());
			stm.setDouble(5, Double.parseDouble(ronda.getCant_prod()));
			
			if (ronda.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)){
				stm.setString(6, Constantes.TIPO_VE_SPECIAL_CTE);	
			}else{
				stm.setString(6, Constantes.TIPO_VE_NORMAL_CTE);
			}
			
			logger.debug("id_local:"+ronda.getId_local());
			int i = stm.executeUpdate();
			
			logger.debug("Resultado Ejecución: " + i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {				
				 id_ronda = rs.getLong(1);
				 logger.debug("el id_ronda nuevo es:"+id_ronda);
			}else{
			    logger.debug("No puede obtener nuevo id_ronda");
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return id_ronda;
	}

	
	/**
	 * Agrega a la tabla BO_DETALLE_RONDAS
	 * 
	 * @param  id_sector long 
	 * @param  id_ronda long 
	 * @param  id_pedido long 
	 * @param  cant double 
	 * @return long
	 * @throws RondasDAOException
	 */
	public long setDetalleRonda(long id_sector,long id_ronda, long id_pedido, double cant) throws RondasDAOException {
		
		PreparedStatement stm=null;
		PreparedStatement stm2=null;
		PreparedStatement stm3=null;
		ResultSet rs=null;

		/*
		 * ciclar por los detalles de cada pedido 
		 * ordenados por cantidad_prods decreciente
		 */
		double asignar = cant;
		long id_detalle;
		double qtydisp, rxa, qtyinsert;
			
		/* OPCION 2 : el id_sector se eliminara de la tabla detalle pedido */ 
		//la cantidad a comparar sera la cantidad de productos sin pickear.
		String SqlStmt = "SELECT " +
			" dp.id_detalle detalle, " +
			" dp.cant_solic disponible, dp.cant_spick as asignado " +
			" FROM BO_DETALLE_PEDIDO dp " +
			" LEFT JOIN BO_DETALLE_RONDAS dr ON dr.id_detalle = dp.id_detalle " +
			" WHERE dp.id_sector = ? AND dp.id_pedido = ? " +
			" GROUP BY dp.id_detalle, dp.cant_solic, dp.cant_spick " +
			" HAVING dp.cant_spick >0 " +
			" ORDER BY asignado ASC "; 

		logger.debug("SQL :"+SqlStmt);
		logger.debug("id_sector:"+id_sector);
		logger.debug("id_pedido:"+id_pedido);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SqlStmt + " WITH UR" );
			stm.setLong(1, id_sector);
			stm.setLong(2, id_pedido);
			
			rs = stm.executeQuery();
			
			List lista = new ArrayList();
			
			while (rs.next()){
				String aux = rs.getString("detalle") + "@" + rs.getDouble("asignado");
				lista.add(aux);
			}

			for ( int k=0; k<lista.size(); k++ ) {
				/* se realiza el insert en base a la cantidad general = gastar * por cada detalle se revisa la cantid_solic si X es mayor
				 * inserta el registro con el valor de cant_solic * gastar' = gastar - cant_solicit (detalle i)  * continua con el siguiente detalle
				 */

				/*
				logger.debug("antes de id_detalle");
				id_detalle = rs.getLong("detalle");
				logger.debug("antes de qtydisp");
				qtydisp = rs.getDouble("asignado");
				logger.debug("id_detalle: "+ id_detalle +" qtydisponible:"+qtydisp);
				*/
				
				String resultado = (String)lista.get(k);
				logger.debug("resultado:"+resultado);
				String[] arr = null;
				arr = resultado.split("@");  // 0: detalle; 1: asignado
				logger.debug("arr:"+arr[0]);
				
				id_detalle 	= Long.parseLong(arr[0]);
				qtydisp		= Formatos.formatoNum3Dec(Double.parseDouble(arr[1]));
				
				
				if (asignar>0){
					rxa = Formatos.formatoNum3Dec(asignar - qtydisp);
					logger.debug("restante("+rxa+") = asignar ("+asignar+") - qtydisponible ("+qtydisp+")");
					if (rxa >= 0)
                        qtyinsert = qtydisp;
					else
                        qtyinsert = asignar;
					//insertar
					String SQLStmt2 = " INSERT INTO BO_DETALLE_RONDAS (id_detalle, id_ronda, id_pedido, cantidad, cant_spick) " +
										" VALUES (? , ? , ? , ?, ?) ";
					logger.debug("SQL: " + SQLStmt2);
					//conn = this.getConnection();
					stm2 = conn.prepareStatement( SQLStmt2 );
					stm2.setLong(1, id_detalle);
					stm2.setLong(2, id_ronda);
					stm2.setLong(3, id_pedido);
					stm2.setDouble(4, qtyinsert);
					stm2.setDouble(5, qtyinsert);
					int i = stm2.executeUpdate();
					logger.debug("Resultado Ejecución: " + i);

					//fin de insertar
					asignar = (double)rxa;
					logger.debug("Nuevo asignar:"+asignar);
					
					//actualizar cant_spick en el bo_detalle_pedido 
					String sqlStmt3 = " UPDATE BO_DETALLE_PEDIDO SET cant_spick = ( cant_spick - ? ) WHERE ID_DETALLE = ? ";
					logger.debug("SQL: " + sqlStmt3);
					//conn = this.getConnection();
					stm3 = conn.prepareStatement( sqlStmt3 );
					stm3.setDouble(1, qtyinsert);
					stm3.setLong(2, id_detalle);
					int j = stm3.executeUpdate();
					logger.debug("Resultado Ejecución actualizar: " + j);

					logger.debug("luego de stm3");
				}
				logger.debug("fuera del if");
			}//while

			logger.debug("antes de con");

		}catch (SQLException e) {
			logger.error("Creando Ronda: ERROR insert/update Detalle de Rondas: " + e.getErrorCode() + " / " + e.getMessage());
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				if (stm3 != null)
					stm3.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				stm2=null;
				stm3=null;
				
			}
		}
		return 0;
	}

	
	/**
	 * Agrega a la tabla BO_DETALLE_RONDAS
	 * 
	 * @param  id_sector long 
	 * @param  id_ronda long 
	 * @param  id_pedido long 
	 * @param  cant double 
	 * @return long
	 * @throws RondasDAOException
	 */
	public long setDetalleRondaPKL(long id_ronda, long id_pedido, double cant) throws RondasDAOException {
		
		PreparedStatement stm=null;
		PreparedStatement stm2=null;
		PreparedStatement stm3=null;
		ResultSet rs=null;

		/*
		 * ciclar por los detalles de cada pedido 
		 * ordenados por cantidad_prods decreciente
		 */
		double asignar = cant;
		long id_detalle;
		double qtydisp, rxa, qtyinsert;
			
		/* OPCION 2 : el id_sector se eliminara de la tabla detalle pedido */ 
		//la cantidad a comparar sera la cantidad de productos sin pickear.
		String SqlStmt = "SELECT dp.id_detalle detalle, dp.cant_solic disponible, "
                       + "       dp.cant_spick as asignado "
                       + "FROM BO_DETALLE_PEDIDO dp "
                       + "     LEFT JOIN BO_DETALLE_RONDAS dr ON dr.id_detalle = dp.id_detalle "
                       + "WHERE dp.id_pedido = ? "
                       + "GROUP BY dp.id_detalle, dp.cant_solic, dp.cant_spick "
                       + "HAVING dp.cant_spick >0 "
                       + "ORDER BY asignado ASC "; 
		logger.debug("SQL : " + SqlStmt);
		//logger.debug("id_sector: " + id_sector);
		logger.debug("id_pedido: " + id_pedido);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SqlStmt + " WITH UR" );
			stm.setLong(1, id_pedido);
			
			rs = stm.executeQuery();
			
			List lista = new ArrayList();
			
			while (rs.next()){
				String aux = rs.getString("detalle") + "@" + rs.getDouble("asignado");
				lista.add(aux);
			}
			logger.debug("antes de rs");
			logger.debug("antes de stm");

			for ( int k=0; k<lista.size(); k++ ) {
				/* se realiza el insert en base a la cantidad general = gastar * por cada detalle se revisa la cantid_solic si X es mayor
				 * inserta el registro con el valor de cant_solic * gastar' = gastar - cant_solicit (detalle i)  * continua con el siguiente detalle
				 */

				String resultado = (String)lista.get(k);
				logger.debug("resultado:"+resultado);
				String[] arr = null;
				arr = resultado.split("@");  // 0: detalle; 1: asignado(cant_spick)
				logger.debug("arr[0]: " + arr[0]);
				
				id_detalle 	= Long.parseLong(arr[0]);
				qtydisp		= Formatos.formatoNum3Dec(Double.parseDouble(arr[1]));
				
				
				if (asignar>0){
					rxa = Formatos.formatoNum3Dec(asignar - qtydisp);
					logger.debug("restante("+rxa+") = asignar ("+asignar+") - qtydisponible ("+qtydisp+")");
					if (rxa >= 0)
                        qtyinsert = qtydisp;
					else
                        qtyinsert = asignar;
					//insertar
					String SQLStmt2 = "INSERT INTO BO_DETALLE_RONDAS (id_detalle, id_ronda, id_pedido, cantidad, cant_spick) "
                                    + "VALUES (? , ? , ? , ?, ?) ";
					logger.debug("SQL: " + SQLStmt2);
					//conn = this.getConnection();
					stm2 = conn.prepareStatement( SQLStmt2 );
					stm2.setLong(1, id_detalle);
					stm2.setLong(2, id_ronda);
					stm2.setLong(3, id_pedido);
					stm2.setDouble(4, qtyinsert);
					stm2.setDouble(5, qtyinsert);
					int i = stm2.executeUpdate();
					logger.debug("Resultado Ejecución: " + i);
					//fin de insertar
					asignar = (double)rxa;
					logger.debug("Nuevo asignar:"+asignar);
					
					//actualizar cant_spick en el bo_detalle_pedido 
					String sqlStmt3 = " UPDATE BO_DETALLE_PEDIDO SET cant_spick = ( cant_spick - ? ) WHERE ID_DETALLE = ? ";
					logger.debug("SQL: " + sqlStmt3);
					//conn = this.getConnection();
					stm3 = conn.prepareStatement( sqlStmt3 );
					stm3.setDouble(1, qtyinsert);
					stm3.setLong(2, id_detalle);
					int j = stm3.executeUpdate();
					logger.debug("Resultado Ejecución actualizar: " + j);
					logger.debug("luego de stm3");
				}
				logger.debug("fuera del if");
			}//while

			logger.debug("antes de con");

		}catch (SQLException e) {
			logger.error("Creando Ronda: ERROR insert/update Detalle de Rondas: " + e.getErrorCode() + " / " + e.getMessage());
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				if (stm3 != null)
					stm3.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				stm2=null;
				stm3=null;
				
			}
		}
		return 0;
	}

	
	/**
	 * Retorna listado resumen segun el id de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List RondaDetalleResumenDTO
	 * @throws RondasDAOException
	 */
	public List getResumenRondaById(long id_ronda) throws RondasDAOException {

		PreparedStatement stm = null;
		ResultSet rs = null;
		List result = new ArrayList();
		String SqlStmt = "SELECT d.id_pedido pedido, s.nombre sector, SUM(d.cantidad) cantidad " +
				" FROM BO_RONDAS r " +
				" JOIN BO_DETALLE_RONDAS d ON d.id_ronda = r.id_ronda " +
				" LEFT JOIN BO_SECTOR         s ON s.id_sector = r.id_sector " +
				" WHERE r.id_ronda = ? " +
				" GROUP BY d.id_pedido, s.nombre ";
		
		logger.debug("SQL :"+SqlStmt);
		logger.debug("id_ronda:"+id_ronda);
		
		try {		
			conn = this.getConnection();
			stm = conn.prepareStatement( SqlStmt + " WITH UR" );	
			stm.setLong(1, id_ronda);
			rs = stm.executeQuery();			
			while (rs.next()) {
				RondaDetalleResumenDTO rp = new RondaDetalleResumenDTO();
				rp.setId_pedido(rs.getLong("pedido"));
				if (rs.getString("sector") != null){
					rp.setNom_sector(rs.getString("sector"));				    
				}else{
				    rp.setNom_sector(Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
				}
				rp.setCant_asignada(rs.getDouble("cantidad"));
				result.add(rp);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene listado de productos que deben ser pickeados en una ronda
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws RondasDAOException
	 */
	public List getProductosRonda(long id_ronda) throws RondasDAOException {
		
		PreparedStatement stm = null;
		PreparedStatement stm2 = null;
		ResultSet rs = null;
		ResultSet re2 = null;
		List result = new ArrayList();

		String SQL = "SELECT row_number() over( ORDER BY DP.DESCRIPCION ASC) AS ORDEN, "
                   + "       DP.ID_DETALLE, DP.ID_PEDIDO, DP.COD_PROD1, DP.UNI_MED, S.NOMBRE NOM_SECTOR, "
                   + "       DP.ID_SECTOR, DP.ID_PRODUCTO, DP.DESCRIPCION DESCR, DR.CANTIDAD, DR.CANT_SPICK, "
                   + "       COALESCE(DP.OBSERVACION, '') OBS, DP.PREPARABLE, DP.CON_NOTA NOTA, DP.PRECIO, "
                   + "       DP.PESABLE, DR.ID_DRONDA, P.POL_SUSTITUCION, PROD.ID_CATPROD, P.ID_ZONA, "
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_RONDAS DR "
                   + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_DETALLE = DR.ID_DETALLE "
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON SC.ID_CRITERIO = DP.ID_CRITERIO "
                   + "     JOIN BODBA.BO_SECTOR S ON S.ID_SECTOR  = DP.ID_SECTOR "
                   + "     JOIN BODBA.BO_PEDIDOS P ON P.ID_PEDIDO = DR.ID_PEDIDO "
                   + "     JOIN BODBA.BO_PRODUCTOS PROD ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO "
                   + "WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND DR.ID_RONDA = ? "
                   + "ORDER BY NOM_SECTOR ASC";
			//" ORDER BY cod_prod1 asc, descr asc";
		String SQLCODBARRA = "SELECT COD_BARRA as ean FROM BODBA.BO_CODBARRA WHERE ID_PRODUCTO=? AND ESTADOACTIVO = '1'";
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getProductosRonda");
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			while (rs.next()) {
				
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getInt("id_detalle"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_producto(rs.getString("cod_prod1"));
				prod.setUnid_medida(rs.getString("uni_med"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_sector(rs.getInt("id_sector"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setObservacion(rs.getString("obs"));
				prod.setSector(rs.getString("nom_sector"));
				prod.setCant_solic(rs.getDouble("cantidad"));
				prod.setCant_spick(rs.getDouble("cant_spick"));				
				prod.setPrecio(rs.getDouble("precio"));
				prod.setPesable(rs.getString("pesable"));
				prod.setId_dronda(rs.getInt("id_dronda"));
				prod.setPol_sustitucion(rs.getString("pol_sustitucion"));
				prod.setId_catprod(rs.getString("id_catprod"));
				prod.setId_zona(rs.getLong("id_zona"));
                
                prod.setIdCriterio(rs.getInt("id_criterio"));
                if ( rs.getInt("id_criterio") == 4 ) {
                    prod.setDescCriterio( rs.getString("desc_criterio") );
                } else {
                    prod.setDescCriterio( rs.getString("criterio") );
                }
                prod.setOrden(rs.getString("ORDEN"));
                result.add(prod);
			}
			for (Iterator iterator = result.iterator(); iterator.hasNext();) {
				ProductosPedidoDTO producto = (ProductosPedidoDTO) iterator.next();
				stm2 = conn.prepareStatement(SQLCODBARRA + " WITH UR");
	             stm2.setLong(1, producto.getId_producto());
	             re2 = stm2.executeQuery();
	             String codBarras="";
	             if(re2.next()){
	             	codBarras = re2.getString("EAN");;
	             }
	             while(re2.next()){
	             	codBarras += " /"+re2.getString("EAN");
	             }
	             producto.setCod_barra(codBarras);
				
			} 

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if(re2 != null)
					re2.close();
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if(stm2 != null)
					stm2.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				re2=null;
				stm2=null;
			}
		}
		
		return result;
		
	}

	
	
	/**
	 * Obtiene listado de productos que deben ser pickeados en una ronda
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws RondasDAOException
	 */
	public List getProductosRondaPKL(long id_ronda) throws RondasDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List result = new ArrayList();

		String SQL = "SELECT * FROM (SELECT ROW_NUMBER() OVER( ORDER BY S.ID_SECTOR, "
                   + "                                                  CASE WHEN SXS.ID_SECCION IS NULL  THEN INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) "
                   + "                                                       ELSE INTEGER(RTRIM(SXS.ORDEN)) "
                   + "                                                  END, "
                   + "                                                  CASE WHEN RS.ID_RUBRO IS NULL  THEN INTEGER(SUBSTR(PROD.ID_CATPROD, 3, 2)) "
                   + "                                                       ELSE INTEGER(RTRIM(RS.ORDEN)) "
                   + "                                                  END, "
                   + "                                                  DP.DESCRIPCION) AS ORDEN, " 
                   + "                      DP.ID_SECTOR, S.NOMBRE NOM_SECTOR, " 
                   + "                      CASE WHEN SEC.ID_SECCION IS NULL  THEN INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) "
                   + "                           ELSE INTEGER(SEC.ID_SECCION) "
                   + "                      END AS ID_SECCION, "
                   + "                      CASE WHEN SEC.NOMBRE IS NULL  THEN SEC2.NOMBRE "
                   + "                           ELSE SEC.NOMBRE "
                   + "                      END AS SECCION, "
                   + "                      PROD.ID_CATPROD, DP.COD_PROD1, DP.UNI_MED, DP.ID_PRODUCTO, DP.DESCRIPCION DESCR, DR.CANTIDAD, DR.CANT_SPICK, "
                   + "                      COALESCE(DP.OBSERVACION, '') OBS, DP.PREPARABLE, DP.CON_NOTA NOTA, DP.PRECIO, DP.PESABLE, DR.ID_DRONDA, "
                   + "                      P.POL_SUSTITUCION, PROD.ID_CATPROD, P.ID_ZONA, DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "               FROM BODBA.BO_RONDAS R "
                   + "                    JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_RONDA      = R.ID_RONDA "
                   + "                    JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_DETALLE    = DR.ID_DETALLE "
                   + "                    LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON SC.ID_CRITERIO = DP.ID_CRITERIO "
                   + "                    JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO "
                   + "                    LEFT JOIN BODBA.BO_SECTOR     S ON S.ID_SECTOR      = DP.ID_SECTOR " 
                   + "                    JOIN BODBA.BO_PEDIDOS         P ON P.ID_PEDIDO      = DR.ID_PEDIDO "
                   + "                    LEFT JOIN BODBA.BO_SECCIONXSECTOR SXS ON SXS.ID_SECCION = INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AND "
                   + "                                                             SXS.ID_SECTOR  = S.ID_SECTOR "
                   + "                    LEFT JOIN BODBA.BO_SECCION SEC  ON SEC.ID_SECCION = SXS.ID_SECCION "
                   + "                    JOIN BODBA.BO_SECCION SEC2 ON SEC2.ID_SECCION = INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) "
                   + "                    LEFT JOIN BODBA.BO_RUBROXSECCION  RS ON RS.ID_SECCION = INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AND "
                   + "                                                            RS.ID_RUBRO   = INTEGER(SUBSTR(PROD.ID_CATPROD, 3, 2)) "
                   + "               WHERE P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND R.ID_RONDA = ? "
                   + "             ) AS TEMP "
                   + "WHERE ORDEN BETWEEN 1 AND 9999";
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getProductosRondaPKL");
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_sector(rs.getInt("ID_SECTOR"));
				prod.setSector(rs.getString("NOM_SECTOR"));
				prod.setId_seccion(rs.getInt("ID_SECCION"));
				prod.setSeccion(rs.getString("SECCION"));
				prod.setId_catprod(rs.getString("ID_CATPROD"));
				prod.setCod_producto(rs.getString("COD_PROD1"));
				prod.setUnid_medida(rs.getString("UNI_MED"));
				prod.setId_producto(rs.getInt("ID_PRODUCTO"));
				prod.setDescripcion(rs.getString("DESCR"));
				prod.setCant_solic(rs.getDouble("CANTIDAD"));
				prod.setCant_spick(rs.getDouble("CANT_SPICK"));				
				prod.setObservacion(rs.getString("OBS"));
				prod.setPrecio(rs.getDouble("PRECIO"));
				prod.setPesable(rs.getString("PESABLE"));
				prod.setPol_sustitucion(rs.getString("POL_SUSTITUCION"));
				prod.setId_dronda(rs.getInt("ID_DRONDA"));
				prod.setId_zona(rs.getLong("ID_ZONA"));
                prod.setIdCriterio(rs.getInt("ID_CRITERIO"));
                if (rs.getInt("ID_CRITERIO") == 4) {
                    prod.setDescCriterio(rs.getString("DESC_CRITERIO"));
                }else{
                    prod.setDescCriterio(rs.getString("CRITERIO"));
                }
                result.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene id_pedido asociado a Ronda Picking Light
	 * 
	 * @param  id_ronda long 
	 * @return long id_pedido
	 * @throws RondasDAOException
	 */
	public long getIdPedidoByRondaPKL(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		long id_pedido = 0L;

		String SQL = "SELECT DISTINCT DR.ID_PEDIDO "
                   + "FROM BODBA.BO_DETALLE_RONDAS DR "
                   + "WHERE DR.ID_RONDA = ?";
			//" ORDER BY cod_prod1 asc, descr asc";
		logger.debug("Ejecución DAO: " + getClass().getName()+"getIdPedidoByRondaPKL");
		logger.debug("SQL : " + SQL);
		logger.debug("id_ronda: " + id_ronda);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			if (rs.next()) {
				id_pedido = rs.getLong("ID_PEDIDO");
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return id_pedido;
	}

	/**
	 * Retorna listado de códigos de barra asociados a los productos de un pedido
	 * 
	 * @param  id_ronda long 
	 * @return List BarraDetallePedidosRondaDTO
	 * @throws RondasDAOException
	 */
	public List getBarrasRondaDetallePedido(long id_ronda) throws RondasDAOException {
		
		PreparedStatement stm = null;		
		ResultSet rs = null;
		List result = new ArrayList();

		String SQLStmt =
			"SELECT dp.id_detalle, cb.cod_barra, cb.tip_codbar " +
			"FROM BO_DETALLE_RONDAS dr " +
			"     JOIN bo_detalle_pedido dp ON dp.id_detalle = dr.id_detalle " +
			"     JOIN bo_codbarra cb ON cb.id_producto = dp.id_producto " +		
			"WHERE dr.id_ronda = ? ";
		
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getBarrasRonda");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			while (rs.next()) {
				BarraDetallePedidosRondaDTO bar = new BarraDetallePedidosRondaDTO();
				bar.setId_detalle(rs.getLong("id_detalle"));
				bar.setCod_barra(rs.getString("cod_barra"));
				bar.setTip_codbar(rs.getString("tip_codbar"));
				result.add(bar);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
     * Retorna listado de códigos de barra asociados a los productos de un pedido
     * 
     * @param  id_ronda long 
     * @return List BarraDetallePedidosRondaDTO
     * @throws RondasDAOException
     */
    public List getBarrasAuditoriaSustitucion(List listBarras, long id_ronda) throws RondasDAOException {
        
        PreparedStatement stm = null;       
        ResultSet rs = null;
        List result = new ArrayList();
        String codBarras = "";

        for (int i=0; i < listBarras.size(); i++){
            if (codBarras.equals("")){
                codBarras += "'" + listBarras.get(i) + "'";
            }else{
                codBarras += ", '" + listBarras.get(i) + "'";    
            }
            
        }
        
        String SQL = "";
        SQL = "SELECT CB.COD_BARRA, CB.TIP_CODBAR, CB.UNID_MED, CB.ID_PRODUCTO ID_PRODUCTO, " 
            + "       P.DES_LARGA DESCRIPCION, PR.PREC_VALOR PRECIO "
            + "FROM BODBA.BO_CODBARRA CB "
            + "     JOIN BODBA.BO_PRODUCTOS  P ON P.ID_PRODUCTO  = CB.ID_PRODUCTO "
            + "     JOIN BODBA.BO_PRECIOS   PR ON PR.ID_PRODUCTO = P.ID_PRODUCTO " 
            + "     JOIN BODBA.BO_RONDAS     R ON R.ID_RONDA = ? "
            + "WHERE CB.COD_BARRA IN ( " + codBarras + " ) "
            + "  AND PR.ID_LOCAL = R.ID_LOCAL ";
        
        
        logger.debug("Ejecución DAO: " + getClass().getName()+"getBarrasAuditoriaSustitucion");
        logger.debug("SQL : " + SQL);
        logger.debug("id_ronda: " + id_ronda);
        
        try {

            conn = this.getConnection();
            stm = conn.prepareStatement( SQL + " WITH UR" );
            stm.setLong(1, id_ronda);
            rs = stm.executeQuery();
            while (rs.next()) {
                BarraAuditoriaSustitucionDTO bar = new BarraAuditoriaSustitucionDTO();
                bar.setCod_barra(rs.getString("COD_BARRA"));
                bar.setTip_codbar(rs.getString("TIP_CODBAR"));
                bar.setUnid_med(rs.getString("UNID_MED"));
                bar.setId_producto(rs.getLong("ID_PRODUCTO"));
                bar.setDescripcion(rs.getString("DESCRIPCION"));
                bar.setPrecio(rs.getDouble("PRECIO"));
                result.add(bar);
            }

        }catch (SQLException e) {
            throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
        } finally {
            //      COD_BARRA       TIP_CODBAR  UNID_MED    ID_PRODUCTO DESCRIPCION                         PRECIO     
            //1     24021043        BC          ST          70594       TORTA SELVA NEGRA JUMBO             5990.00    
            //2     7803473000767   HE          ST          150469      PAN PITA BLANCO CHICO IDEAL 8 UN    919.00     
            /*BarraAuditoriaSustitucionDTO bar = new BarraAuditoriaSustitucionDTO();
            
            bar.setCod_barra("24021043");
            bar.setTip_codbar("BC");
            bar.setUnid_med("ST");
            bar.setId_producto(Long.parseLong("70594"));
            bar.setDescripcion("TORTA SELVA NEGRA JUMBO");
            bar.setPrecio(Double.parseDouble("5990.00"));
            result.add(bar);

            bar = new BarraAuditoriaSustitucionDTO();
            bar.setCod_barra("7803473000767");
            bar.setTip_codbar("HE");
            bar.setUnid_med("ST");
            bar.setId_producto(Long.parseLong("150469"));
            bar.setDescripcion("PAN PITA BLANCO CHICO IDEAL 8 UN");
            bar.setPrecio(Double.parseDouble("919.00"));
            result.add(bar);*/

            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
                
            } catch (SQLException e) {
                logger.error("[getBarrasAuditoriaSustitucion] : xxx - Problema SQL (close)", e);
            }finally{
                rs=null;
                stm=null;
                
            }
        }
        
        return result;
    }


    
	/**
	 * Actualiza tabla BO_RONDAS con el pickeador asignado y actualiza fecha inicio picking
	 * 
	 * @param  id_ronda long 
	 * @param  id_usuario long 
	 * 
	 * @throws RondasDAOException
	 */
	public void doAsignaPickeadorRonda(long id_ronda, long id_usuario) throws RondasDAOException {

		PreparedStatement stm=null;
		String SQLStmt = 
				" UPDATE  bo_rondas " +
				" SET id_usuario = ? ," +
				" fini_picking = CURRENT TIMESTAMP " +
				" WHERE id_ronda = ? "
				;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setLong(1, id_usuario);
			stm.setLong(2, id_ronda);
			
			
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);

				stm=null;
				
			}
		}
		
	}


	/**
	 * Obtiene el listado de productos sustitutos de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List SustitutoDTO
	 * @throws RondasDAOException
	 */
	public List getSustitutosByRondaId(long id_ronda)
	throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getSustitutos");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getSustitutos:");
		logger.debug("id_ronda:"+id_ronda);
		
		String Sql = "SELECT DP.COD_PROD1 COD_PROD1, DP.UNI_MED UNI_MED1, DP.DESCRIPCION DESCR1, "
                   + "       DP.OBSERVACION OBS1, DP.PRECIO PRECIO1, "
                   + "       DP.ID_PEDIDO ID_PEDIDO, DR.CANTIDAD CANT1, P.COD_PROD1 COD_PROD2, P.UNI_MED UNI_MED2, "
                   + "       PIK.DESCRIPCION DESCR2, PIK.CANT_PICK CANT2, PIK.PRECIO PRECIO2, "
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO "
                   + "FROM BODBA.BO_DETALLE_RONDAS DR "
                   + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO      DP ON DP.ID_DETALLE  = DR.ID_DETALLE "
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON (DP.ID_CRITERIO = SC.ID_CRITERIO) "
                   + "     JOIN BODBA.BO_DETALLE_PICKING         PIK ON PIK.ID_DETALLE = DP.ID_DETALLE "
                   + "     LEFT JOIN BODBA.BO_PRODUCTOS            P ON P.ID_PRODUCTO = PIK.ID_PRODUCTO "
                   + "WHERE DR.ID_RONDA = ? "
                   + "  AND DR.SUSTITUTO = 'S' "
                   + "  AND INTEGER(DP.COD_PROD1) <> INTEGER(P.COD_PROD1) "
                   + "  AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
                   + "  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "
                   + "                                    FROM BODBA.BO_INF_SYF SF "
                   + "                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1 "
                   + "                                      AND SF.UME_ORIGINAL      = DP.UNI_MED "
                   + "                                      AND SF.COD_PROD_SUS      = P.COD_PROD1 "
                   + "                                      AND SF.UME_SUS           = P.UNI_MED) " 
                   + "ORDER BY DP.COD_PROD1, DP.UNI_MED ASC";

		
		logger.debug("SQL sustitutos :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			//stm.setString(2, Constantes.PROD_SUSTITUIDO);
			rs = stm.executeQuery();
			while (rs.next()) {
				SustitutoDTO prod =new SustitutoDTO();
				
				prod.setCod_prod1(rs.getString("cod_prod1"));
				prod.setUni_med1(rs.getString("uni_med1"));
				prod.setCant1(rs.getDouble("cant1"));		
				prod.setDescr1(rs.getString("descr1"));
				prod.setObs1(rs.getString("obs1"));
				prod.setPrecio1(rs.getDouble("precio1"));
				prod.setCod_prod2(rs.getString("cod_prod2"));				
				prod.setUni_med2(rs.getString("uni_med2"));				
				prod.setCant2(rs.getDouble("cant2"));							
				prod.setDescr2(rs.getString("descr2"));	
				prod.setPrecio2(rs.getDouble("precio2"));
				prod.setId_pedido(rs.getLong("id_pedido"));
                
                prod.setIdCriterio( rs.getLong("id_criterio") );
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio( rs.getString("desc_criterio") );
                } else {
                    prod.setDescCriterio( rs.getString("criterio") );
                }
                
				list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_prod;
	}

	/**
	 * Obtiene el listado de productos faltantes de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoEntity
	 * @throws RondasDAOException
	 */
	public List getFaltantesByRondaId(long id_ronda)
	throws RondasDAOException {
		PreparedStatement stm = null;
		//PreparedStatement stm2 = null;
		ResultSet rs = null;
		//ResultSet rs2 = null;
		
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+" getFaltantesByRondaId");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getFaltantesPedido:");
		logger.debug("id_ronda:"+id_ronda);
		
		//segun ultimo modelamiento, sera necesario un solo query
		String Sql = "SELECT DP.ID_DETALLE,  S.NOMBRE NOM_SECTOR,   DP.ID_SECTOR,   DP.ID_PRODUCTO, "   
                   + "       DP.COD_PROD1,    DP.DESCRIPCION DESCR,  DR.CANT_FALTAN CANTIDAD,  DP.OBSERVACION OBS, "  
                   + "       DP.PREPARABLE,   DP.CON_NOTA NOTA,  DP.UNI_MED, DP.PRECIO,  DP.ID_PEDIDO, " 
                   + "       DP.ID_CRITERIO, DP.DESC_CRITERIO, SC.DESCRIPCION CRITERIO " 
                   + "FROM BODBA.BO_RONDAS R "
                   + "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_RONDA = R.ID_RONDA "
                   + "     JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_DETALLE = DR.ID_DETALLE "
                   + "     LEFT JOIN FODBA.FO_SUSTITUTOS_CRITERIO SC ON SC.ID_CRITERIO = DP.ID_CRITERIO " 
                   + "     JOIN BODBA.BO_SECTOR S  ON S.ID_SECTOR = DP.ID_SECTOR "  
                   + "WHERE R.ID_RONDA = ? "
                   + "  AND DR.CANT_FALTAN > 0";
		logger.debug("SQL:"+Sql);
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoEntity prod =new ProductosPedidoEntity();
				prod.setId_detalle(rs.getInt("id_detalle"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_prod1(rs.getString("cod_prod1"));
				prod.setId_sector(rs.getInt("id_sector"));
				prod.setNom_sector(rs.getString("nom_sector"));
				prod.setCant_solic(rs.getDouble("cantidad"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setObservacion(rs.getString("obs"));
				prod.setPreparable(rs.getString("preparable"));
				prod.setCon_nota(rs.getString("nota"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setPrecio(rs.getDouble("precio"));	
				prod.setId_pedido(rs.getInt("id_pedido"));
                
                prod.setIdCriterio( rs.getLong("id_criterio") );
                if ( rs.getLong("id_criterio") == 4 ) {
                    prod.setDescCriterio( rs.getString("desc_criterio") );
                } else {
                    prod.setDescCriterio( rs.getString("criterio") );
                }
                
                list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("cantidad:"+list_prod.size());
		return list_prod;
	}

	
	/**
	 * Actualiza el estado de la ronda a Terminado y la fecha y hora de término
	 * 
	 * @param  id_ronda long 
	 * 
	 * @throws RondasDAOException
	 */
	public void doFinalizaRonda(long id_ronda) throws RondasDAOException {

		PreparedStatement stm=null;
		String SQLStmt = 
				" UPDATE  bo_rondas " +
				" SET id_estado = " + Constantes.ID_ESTADO_RONDA_FINALIZADA +
				" , ffin_picking = CURRENT TIMESTAMP " +
				" WHERE id_ronda = ? "
				;
		
		logger.debug("SQL: " + SQLStmt);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, id_ronda);
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			e.printStackTrace();
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Cambia el estado de la ronda a creada.
	 * 
	 * @param id_ronda
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setReseteaRonda(long id_ronda) throws RondasDAOException {
		boolean result = false;
		
		PreparedStatement stm=null;
		String SQLStmt = 
				" UPDATE  bo_rondas " +
				" SET id_estado = " + Constantes.ID_ESTADO_RONDA_CREADA +
				" WHERE id_ronda = ? " ;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, id_ronda);
			
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getBinsFormPickByRondaId(long)
	 */
	public List getBinsFormPickByRondaId(long id_ronda)	throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		List list_bins = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+" getBinsFormPickByRondaId");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getBinsFormPickByRondaId:");
		logger.debug("id_ronda: "+id_ronda);
				
		String Sql = " SELECT id_form_bin, id_ronda, cod_bin, tipo, " +
					" cod_ubicacion, id_pedido, posicion " +
					 " FROM bo_fpickbins " +
					 " WHERE id_ronda = ?";
		logger.debug("SQL:"+Sql);
		try {
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				BinFormPickDTO bin =new BinFormPickDTO();				
				bin.setId_fpbin(rs.getLong("id_form_bin"));
				bin.setId_ronda(rs.getLong("id_ronda"));
				bin.setCod_bin(rs.getString("cod_bin"));
				bin.setTipo(rs.getString("tipo"));
				bin.setCod_ubicacion(rs.getString("cod_ubicacion"));
				bin.setId_pedido(rs.getLong("id_pedido"));
				bin.setPosicion(rs.getInt("posicion"));
				list_bins.add(bin);
				}
			
			
		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_bins;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaBinRonda(cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO)
	 */
	public long doAgregaBinRonda(BinFormPickDTO bin) throws RondasDAOException {
		PreparedStatement stm=null;
		long binId = -1; 
		ResultSet rs = null;
		String Sql = " INSERT INTO bo_fpickbins (id_ronda, cod_bin, tipo, cod_ubicacion, id_pedido, posicion)" +
				     " VALUES (?,?,?,?,?,?)";
		logger.debug("SQL:"+Sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql, Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, bin.getId_ronda());
			stm.setString(2,bin.getCod_bin());
			stm.setString(3,bin.getTipo());
			stm.setString(4,bin.getCod_ubicacion());
			stm.setLong(5, bin.getId_pedido());
			stm.setInt(6, bin.getPosicion());
			int i = stm.executeUpdate();			
			logger.debug("Resultado Ejecución: " + i);
			rs = stm.getGeneratedKeys();
			logger.debug("results: " + rs);
			if (rs.next()) {				
				 binId = rs.getLong(1);
				 logger.debug("el id_bin nuevo es:"+ binId);
			 }
			 else{ logger.debug("No puede obtener nuevo id_Bin"); }

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return binId;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaDetalleFormPicking(cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO)
	 */
	public boolean doAgregaDetalleFormPicking(DetalleFormPickingDTO pick) throws RondasDAOException {
		PreparedStatement stm=null;
		boolean result=false;
		
		String Sql = " INSERT INTO bo_fpickdetpick (id_detalle, id_dronda, cbarra, tipo, cantidad, " +
					"  pesable, id_form_bin, id_ronda, mot_sust, id_fpick, cant_rel_ped )" +
				     " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		logger.debug("SQL:"+Sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setLong(1,pick.getId_detalle());
			stm.setLong(2,pick.getId_det_ronda());
			stm.setString(3,pick.getCBarra());
			stm.setString(4,pick.getTipo());
			stm.setDouble(5,pick.getCantidad());
			stm.setString(6,pick.getPesable());
			stm.setLong(7, pick.getId_form_bin());
			stm.setLong(8,pick.getId_ronda());
			stm.setInt(9,pick.getMot_sust());
			stm.setLong(10,pick.getId_fpick());
			stm.setDouble(11,pick.getCant_rel_ped());
			int i = stm.executeUpdate();
			if (i>0){
				result = true;
			}
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	
	public long getCountFormPickBin(String  cod_bin,long id_ronda) throws RondasDAOException{
	
	long cant = 0;
	
	PreparedStatement stm =null;
	ResultSet rs=null;

	String SQLStmt = " SELECT Count(*) cantidad " +
		" FROM bo_fpickbins " +
		" WHERE cod_bin LIKE ? AND id_ronda = ?" ;
		
	logger.debug("SQL: " + SQLStmt);
	
	try {

		conn = this.getConnection();
		stm = conn.prepareStatement( SQLStmt + " WITH UR" );
		stm.setString(1,cod_bin);
		stm.setLong(2,id_ronda);

		rs = stm.executeQuery();
		if (rs.next()) {
			cant = rs.getLong("cantidad");
		}

	}catch (SQLException e) {
		throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	return cant;

}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getDetallePickFormPick(long)
	 */
	public List getDetallePickFormPick(ProcFormPickDetPickDTO datos) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		List result = new ArrayList();
		
		long id_ronda;
		
		id_ronda = datos.getId_ronda();
		
		String sql_cant_pend="";
		String sql_por_idpick="";
		if (datos.isPendientes()) {
			sql_cant_pend = " AND p.cant_pend > 0 ";
		}
		if (datos.isPor_idpick()){
			sql_por_idpick = " AND p.id_fpick = " + datos.getId_pick();
			
		}
		
		String SQLStmt =
			" SELECT p.id_fpick, p.id_ronda, p.cbarra, p.cantidad, p.cant_pend,p.id_form_bin, " +
			" b.cod_bin, b.posicion, b.id_pedido " +
			" FROM bo_fpick p " +
			" JOIN bo_fpickbins b ON p.id_form_bin = b.id_form_bin " +
			" WHERE p.id_ronda = ? " + sql_cant_pend + sql_por_idpick;
		
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getDetallePickFormPick");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				FPickDTO pick = new FPickDTO();
				pick.setId_fpick(rs.getLong("id_fpick"));
				pick.setId_ronda(rs.getLong("id_ronda"));
				pick.setCbarra(rs.getString("cbarra"));
				pick.setCantidad(rs.getDouble("cantidad"));
				pick.setCant_pend(rs.getDouble("cant_pend"));
				pick.setId_form_bin(rs.getLong("id_form_bin"));
				pick.setCod_bin(rs.getString("cod_bin"));
				pick.setPosicion(rs.getInt("posicion"));
				pick.setId_op(rs.getLong("id_pedido"));				
				
				
				result.add(pick);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getProductosCbarraRonda(long)
	 */
	public List getProductosCbarraRonda(ProcFormPickDetPedDTO datos) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		List result = new ArrayList();
		
		long id_ronda;
		
		id_ronda = datos.getId_ronda();
		String sql_sin_pickear="";
		if (datos.isSin_pickear()){
			sql_sin_pickear=" AND T.CANT_SPICKEAR>0 ";
		}
		String sql_por_idDet="";
		if (datos.isPor_idDet()){
			sql_por_idDet=" AND T.ID_DETALLE = " + datos.getId_detalle();
		}
		String SQLStmt = "SELECT T.ID_DETALLE id_detalle, " +
				" T.ID_PEDIDO id_pedido, " +
				" T.ID_PRODUCTO id_producto, " +
				" T.COD_PROD1 cod_prod1, " +
				" T.UNI_MED uni_med, " +
				" T.DESCR descr, " +
				" T.CANT_PEDIDO cantidad, " +
				" T.CANT_PICKEADA cant_pick, " +
				" T.CANT_FALTANTE cant_falt, " +
				" T.CANT_SPICKEAR cant_spick, " +
				" T.PRECIO precio, " +
				" T.ES_PESABLE pesable, " +
				" T.SECTOR nom_sector, " +
				" T.SUST_CFORMATO sust_cformato, " +
				" T.ID_DRONDA id_dronda, " +
				" T.POL_SUST pol_sustitucion, " +
				" T.MOT_SUST mot_sustitucion, " +
				" coalesce(T.OBS, '') obs, " +
				" T.ESTADO estado " +
			//	" ,coalesce(cb.cod_barra,'') codbarra" +
				" FROM BO_FPICKDETPED T " +
			//	" JOIN BO_FPICK_OP P ON P.id_pedido = T.ID_PEDIDO AND p.id_ronda=t.id_ronda " +
			//	" LEFT JOIN bo_codbarra cb ON T.id_producto = cb.id_producto " +				
				" WHERE T.id_ronda  = ? " + sql_sin_pickear + sql_por_idDet + 
				" ORDER BY T.DESCR ASC ";	
		
		logger.debug("Ejecución DAO: " + getClass().getName()+":getProductosCbarraRonda");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);
				
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getLong("id_detalle"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_producto(rs.getString("cod_prod1"));
				prod.setUnid_medida(rs.getString("uni_med"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setCant_solic(rs.getDouble("cantidad"));
				prod.setCant_pick(rs.getDouble("cant_pick"));
				prod.setCant_faltan(rs.getDouble("cant_falt"));
				prod.setCant_spick(rs.getDouble("cant_spick"));
				prod.setPrecio(rs.getDouble("precio"));
				prod.setPesable(rs.getString("pesable"));
				prod.setSector(rs.getString("nom_sector"));
				prod.setSust_cformato(rs.getString("sust_cformato"));
				prod.setId_dronda(rs.getLong("id_dronda"));
				prod.setPol_sustitucion(rs.getString("pol_sustitucion"));
				prod.setMot_sustitucion(rs.getInt("mot_sustitucion"));
				prod.setObservacion(rs.getString("obs"));
				prod.setEstado(rs.getString("estado"));
			//	prod.setCod_barra(rs.getString("codbarra"));
				result.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getProductoByCbarra(java.lang.String)
	 */
	public ProductoCbarraDTO getProductoByCbarra(String cod_barra) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		ProductoCbarraDTO prod = null;

		String SQLStmt =
			" SELECT p.id_producto pro_id, p.cod_prod1 cod_sap,  " +
			" p.uni_med uni_med, p.des_corta des_corta, " +
			" cb.cod_barra " +
			" FROM BO_PRODUCTOS p " +
			" JOIN bo_codbarra cb ON p.id_producto = cb.id_producto " +
			" WHERE cb.cod_barra =  ? ";
			
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getProductoByCbarra");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("cod_barra: " + cod_barra);
	
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setString(1, cod_barra);			
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductoCbarraDTO();
				prod.setId_prod(rs.getLong("pro_id"));
				prod.setCod_sap(rs.getString("cod_sap"));
				prod.setDescripcion(rs.getString("des_corta"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setCod_barra(rs.getString("cod_barra"));
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		
		return prod;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doDelDetalleFormPicking(long)
	 */
	public boolean doDelDetalleFormPicking(long id_row) throws RondasDAOException {
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			" DELETE FROM bo_fpickdetpick " +
			" WHERE id_row = ? ";
		
		logger.debug("en doDelDetalleFormPicking");
		logger.debug("SQL: " + sql + " id_row "+ id_row );
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_row);
	
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaFormPickRonda(cl.bbr.jumbocl.pedidos.dto.FormPickRondaDTO)
	 */
	public void doAgregaFormPickRonda(FormPickRondaDTO datos) throws RondasDAOException {
		PreparedStatement stm=null;
		String Sql = " INSERT INTO BO_FPICK_RONDA ( ID_RONDA, ESTADO )" +
         				" VALUES (?, ?)";

		logger.debug("doAgregaFormPickRonda SQL:"+Sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setLong(1,datos.getId_ronda());
			stm.setString(2,datos.getEstado());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaFormPickOp(cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO)
	 */
	public void doAgregaFormPickOp(FormPickOpDTO datos) throws RondasDAOException {
		PreparedStatement stm=null;
		String Sql = " INSERT INTO BO_FPICK_OP (id_pedido, id_ronda, estado)" +
				     " VALUES (?,?,?)";
		logger.debug("SQL:"+Sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setLong(1,datos.getId_pedido());
			stm.setLong(2,datos.getId_ronda());
			stm.setString(3,datos.getEstado());
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaFormPickDetPed(cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO)
	 */
	public void doAgregaFormPickDetPed(ProductosPedidoDTO datos) throws RondasDAOException {
		PreparedStatement stm=null;
		String Sql = " INSERT INTO BO_FPICKDETPED (" +
				" ID_DETALLE, " +
				" ID_PEDIDO, " +
				" COD_PROD1, " +
				" UNI_MED, " +
				" DESCR, " +
				" CANT_PEDIDO, " +
				" CANT_PICKEADA, " +
				" CANT_FALTANTE, " +
				" CANT_SPICKEAR, " +
				" PRECIO, " +
				" ES_PESABLE, " +
				" SECTOR, " +
				" SUST_CFORMATO, " +
				" ID_DRONDA, " +
				" POL_SUST, " +
				" MOT_SUST, " +
				" OBS, " +
				" ESTADO," +
				" ID_PRODUCTO," +
				" ID_RONDA )" +
         " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

		logger.debug("SQL:"+Sql);
		try {
		
			conn = this.getConnection();
			
			stm = conn.prepareStatement(Sql);
			
			stm.setLong(1,datos.getId_detalle());
			stm.setLong(2,datos.getId_pedido());
			stm.setString(3,datos.getCod_producto());
			stm.setString(4,datos.getUnid_medida());
			stm.setString(5,datos.getDescripcion());
			stm.setDouble(6,datos.getCant_solic());
			stm.setDouble(7,datos.getCant_pick());
			stm.setDouble(8,datos.getCant_faltan());
			stm.setDouble(9,datos.getCant_spick());
			stm.setDouble(10,datos.getPrecio());
			stm.setString(11,datos.getPesable());
			stm.setString(12,datos.getSector());
			stm.setString(13,datos.getSust_cformato());
			stm.setLong(14,datos.getId_dronda());
			stm.setString(15,datos.getPol_sustitucion());
			stm.setLong(16,datos.getMot_sustitucion());
			stm.setString(17,datos.getObservacion());
			stm.setString(18,datos.getEstado());
			stm.setLong(19,datos.getId_producto());
			stm.setLong(20,datos.getId_ronda());
			
			logger.debug(datos.getId_detalle()+","+datos.getId_pedido() +
					","+datos.getCod_producto()+","+datos.getUnid_medida()+
					","+datos.getDescripcion()+","+datos.getCant_solic()+
					","+datos.getCant_pick()+","+datos.getCant_faltan()+
					","+datos.getCant_spick()+","+datos.getPrecio()+
					","+datos.getPesable()+","+datos.getSector()+
					","+datos.getSust_cformato()+","+datos.getId_dronda()+
					","+datos.getPol_sustitucion()+","+datos.getMot_sustitucion()+
					","+datos.getObservacion()+","+datos.getEstado()+
					","+datos.getId_producto()+","+datos.getId_ronda());
			
			
			int i = stm.executeUpdate();
			
			
			logger.debug("Resultado Ejecución: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#setActFormPickRonda(cl.bbr.jumbocl.pedidos.dto.FormPickRondaDTO)
	 */
	public boolean setActFormPickRonda(FormPickRondaDTO datos) throws RondasDAOException {
		boolean result = false;		
		PreparedStatement stm=null;
		
		String SQLStmt = 
				" UPDATE  BO_FPICK_RONDA " +
				" SET estado = ? " +
				" WHERE id_ronda = ? ";
		
		logger.debug("setActFormPickRonda SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setString(1, datos.getEstado());
			stm.setLong(2, datos.getId_ronda());
			
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#setActFormPickOp(cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO)
	 */
	public boolean setActFormPickOp(FormPickOpDTO datos) throws RondasDAOException {
		boolean result = false;		
		PreparedStatement stm=null;
		
		String SQLStmt = 
				" UPDATE  BO_FPICK_OP " +
				" SET estado = ?" +
				" WHERE id_pedido = ? AND id_ronda = ? ";
		
		logger.debug("setActFormPickOp SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setString(1, datos.getEstado());
			stm.setLong(2, datos.getId_pedido());
			stm.setLong(3, datos.getId_ronda());
			
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#setActFormPickDetPed(cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO)
	 */
	public boolean setActFormPickDetPed(ProductosPedidoDTO datos) throws RondasDAOException {
		boolean result = false;		
		PreparedStatement stm=null;
		
		String SQLStmt =" UPDATE  BO_FPICKDETPED SET id_detalle = id_detalle ";
		
		if (datos.getId_pedido() >0)
			SQLStmt += ", ID_PEDIDO = " + datos.getId_pedido();
		if (datos.getCod_producto() != null)
			SQLStmt += ", COD_PROD1 = '" + datos.getCod_producto()+"'";
		if (datos.getUnid_medida() != null)
			SQLStmt += ", UNI_MED = '" + datos.getUnid_medida()+"'";
		if (datos.getDescripcion() != null)
			SQLStmt += ", DESCR = '" + datos.getDescripcion()+"'";
		if (datos.getCant_solic() >0)
			SQLStmt += ", CANT_PEDIDO = " + datos.getCant_solic();
		if (datos.getCant_pick() >-1)
			SQLStmt += ", CANT_PICKEADA = " + datos.getCant_pick();
		if (datos.getCant_faltan() >-1)
			SQLStmt += ", CANT_FALTANTE = " + datos.getCant_faltan();
		if (datos.getCant_spick() >-1)
			SQLStmt += ", CANT_SPICKEAR = " + datos.getCant_spick();
		if (datos.getPrecio()>0)
			SQLStmt += ", PRECIO = " + datos.getPrecio();
		if (datos.getPesable() != null)
			SQLStmt += ", ES_PESABLE = '" + datos.getPesable()+"'";
		if (datos.getSector() != null)
			SQLStmt += ", SECTOR = '" + datos.getSector()+"'";
		if (datos.getSust_cformato() != null)
			SQLStmt += ", SUST_CFORMATO = '" + datos.getSust_cformato()+"'";
		if (datos.getId_dronda() >0)
			SQLStmt += ", ID_DRONDA = " + datos.getId_dronda();
		if (datos.getPol_sustitucion() != null)
			SQLStmt += ", POL_SUST = '" + datos.getPol_sustitucion()+"'";
		if (datos.getMot_sustitucion() >-1)
			SQLStmt += ", MOT_SUST = " + datos.getMot_sustitucion();
		if (datos.getObservacion() != null)
			SQLStmt += ", OBS = '" + datos.getObservacion()+"'";
		if (datos.getEstado() != null)
			SQLStmt += ", ESTADO = '" + datos.getEstado()+"'";
		if (datos.getId_producto() >0)
			SQLStmt += ", ID_PRODUCTO = '" + datos.getId_producto();
		
		SQLStmt +=" WHERE id_detalle = ? ";
				
		
		logger.debug("setActFormPickDetPed SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setLong(1,datos.getId_detalle());
			
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#setActFormPickDetPick(cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO)
	 */
	public boolean setActFormPickDetPick(DetalleFormPickingDTO datos) throws RondasDAOException{
		boolean result = false;		
		PreparedStatement stm=null;

		String SQLStmt = " UPDATE BO_FPICKDETPICK SET ID_ROW = ID_ROW ";
		
		if (datos.getId_detalle()>0){					
			SQLStmt += ", ID_DETALLE	= "+datos.getId_detalle();
		}
		if (datos.getId_det_ronda()>0){					
			SQLStmt += ", ID_DRONDA	= "+datos.getId_det_ronda();
		}
		if (datos.getCBarra()!=null){					
			SQLStmt += ", CBARRA	= '"+datos.getCBarra()+"'";
		}
		if (datos.getTipo()!=null){					
			SQLStmt += ", TIPO	= '"+datos.getTipo()+"'";
		}
		if (datos.getCantidad()>0){					
			SQLStmt += ", CANTIDAD	= "+datos.getCantidad();
		}
		if (datos.getPesable()!=null){					
			SQLStmt += ", PESABLE	= '"+datos.getPesable()+"'";
		}			
		if (datos.getId_form_bin()>0){					
			SQLStmt += ", ID_FORM_BIN	= "+datos.getId_form_bin();
		}			
		if (datos.getId_ronda()>0){					
			SQLStmt += ", ID_RONDA	= "+datos.getId_ronda();
		}			
		if (datos.getMot_sust()>0){					
			SQLStmt += ", MOT_SUST	= "+datos.getMot_sust();
		}			
			
		SQLStmt +=	" WHERE ID_ROW = ? ";
		
		logger.debug("setActFormPickDetPed SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, datos.getId_row());
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);
			
		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	public FormPickRondaDTO getFormPickRonda(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		FormPickRondaDTO ronda = new FormPickRondaDTO();

		String SQLStmt =
			" SELECT id_ronda, estado " +
			" FROM BO_FPICK_RONDA r " +
			" WHERE id_ronda = ? ";		
				
		logger.debug("getFormPickRonda SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				
				ronda.setId_ronda(rs.getLong("id_ronda"));
				ronda.setEstado(rs.getString("estado"));				
				//result.add(ronda);
			}
		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		//return result;
		return ronda;
	}

	public FormPickOpDTO getFormPickOp(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		FormPickOpDTO pedido = new FormPickOpDTO();

		String SQLStmt =
			" SELECT id_pedido, id_ronda, estado " +
			" FROM BO_FPICK_OP r " +
			" WHERE id_ronda = ? ";	

		logger.debug("getFormPickRonda SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			
			if (rs.next()) {
				
				pedido.setId_ronda(rs.getLong("id_ronda"));
				pedido.setEstado(rs.getString("estado"));				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		//return result;
		return pedido;
	}

	public List getDetalleRondas(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		List result = new ArrayList();

		String SQLStmt =
			" SELECT T.ID_DRONDA id_dronda, " +
				" T.ID_DETALLE id_detalle, " +
				" T.ID_RONDA id_ronda, " +
				" T.ID_PEDIDO id_pedido, " +
				" T.CANTIDAD cant_solic, " +
				" T.CANT_PICK cant_pick, " +
				" T.CANT_FALTAN cant_faltan, " +
				" T.CANT_SPICK cant_spick, " +
				" T.SUSTITUTO sust, " +
				" T.MOT_SUSTITUCION mot_sust, " +
				" P.COD_PROD1 cod_prod1," +
				" P.UNI_MED uni_med, " +
				" P.DESCRIPCION descripcion, " +
				" P.PESABLE pesable, " +
				" P.PRECIO precio," +
				" P.OBSERVACION obs, " +
				" S.NOMBRE nom_sector, " +
				" P.ID_PRODUCTO id_producto " +
			" FROM BO_DETALLE_RONDAS T " +
			" JOIN BO_DETALLE_PEDIDO P ON P.ID_DETALLE = T.ID_DETALLE" +
			" JOIN BO_SECTOR S ON S.ID_SECTOR = P.ID_SECTOR " +
			" WHERE T.id_ronda = ? ";
		
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getDetalleRondas");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				DetalleRondaDTO ron = new DetalleRondaDTO();
				ron.setId_dronda(rs.getLong("id_dronda"));
				ron.setId_detalle(rs.getLong("id_detalle"));
				ron.setId_ronda(rs.getLong("id_ronda"));
				ron.setId_pedido(rs.getLong("id_pedido"));
				ron.setCantidad(rs.getDouble("cant_solic"));
				ron.setCant_pick(rs.getDouble("cant_pick"));
				ron.setCant_faltan(rs.getDouble("cant_faltan"));
				ron.setCant_spick(rs.getDouble("cant_spick"));
				ron.setSustituto(rs.getString("sust"));
				ron.setMot_sustitucion(rs.getInt("mot_sust"));
				ron.setCod_prod1(rs.getString("cod_prod1"));
				ron.setUni_med(rs.getString("uni_med"));
				ron.setDescripcion(rs.getString("descripcion"));
				ron.setPrecio(rs.getDouble("precio"));
				ron.setPesable(rs.getString("pesable"));
				ron.setObservacion(rs.getString("obs"));
				ron.setNom_Sector(rs.getString("nom_sector"));			
				ron.setId_producto(rs.getLong("id_producto"));
				result.add(ron);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doAgregaPick(FPickDTO)
	 */
	public long doAgregaPick(FPickDTO pick) throws RondasDAOException {
		PreparedStatement stm=null;
		ResultSet rs = null;
		long id_fpick=0;
		
		String  Sql = "INSERT INTO BO_FPICK (ID_RONDA, ID_FORM_BIN, CBARRA, CANTIDAD, CANT_PEND)" +
				" VALUES (?,?,?,?,?) ";
		
		logger.debug("SQL:"+Sql);
		try {
		
			conn = this.getConnection();

			stm = conn.prepareStatement( Sql ,Statement.RETURN_GENERATED_KEYS );
					
			stm.setLong(1,pick.getId_ronda());
			stm.setLong(2,pick.getId_form_bin());
			stm.setString(3,pick.getCbarra());
			stm.setDouble(4,pick.getCantidad());
			stm.setDouble(5,pick.getCant_pend());
			int i = stm.executeUpdate();
			
			logger.debug("Resultado Ejecución: " + i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {				
				 id_fpick = rs.getLong(1);
				 logger.debug("el id_ronda nuevo es:"+id_fpick);
			 }
			 else{ logger.debug("No puede obtener nuevo id_ronda"); }
			

		}catch (SQLException e) {
			logger.debug("e.getErrorCode()"+e.getErrorCode());
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return id_fpick;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getDetallePick(ProcFPickDTO)
	 */
	public List getDetallePick(ProcFPickDTO pick) throws RondasDAOException{
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		long id_ronda;
		long id_fpick;

		try {
			id_ronda =pick.getId_ronda();
			id_fpick =pick.getId_fpick();	
			
			String SqlWhere ="";

			String SqlRondas ="";
			if (id_ronda>0){
				SqlRondas =" id_ronda = "+id_ronda;
				SqlWhere="WHERE "+SqlRondas;
			}
			
			String SqlReg ="";
			if (id_fpick>0){
				SqlReg =" id_fpick = "+id_fpick;
				SqlWhere="WHERE "+SqlReg;
			}
			String cadQuery="SELECT ID_FPICK, ID_RONDA, ID_FORM_BIN, CBARRA, CANTIDAD, CANT_PEND " +
							"FROM BO_FPICK " + SqlWhere;
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setString(1,"RD");
			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				FPickDTO reg = new FPickDTO();
				reg.setId_fpick(rs.getLong("id_fpick"));
				reg.setId_ronda(rs.getLong("id_donda"));
				reg.setId_form_bin(rs.getLong("id_form_bin"));				
				reg.setCbarra(rs.getString("cbarra"));
				reg.setCantidad(rs.getDouble("cantidad"));
				reg.setCant_pend(rs.getDouble("cant_pend"));
				result.add(reg);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#setActFPick(FPickDTO)
	 */
	public boolean setActFPick(FPickDTO datos) throws RondasDAOException {
		boolean result = false;		
		PreparedStatement stm=null;
		
		try {

			String SqlCantPend ="";
			if (datos.getCant_pend()>=0){
				SqlCantPend = ", cant_pend = "+ datos.getCant_pend();				
			}
			String SQLStmt = 
					" UPDATE  BO_FPICK SET " +
					" id_fpick = id_fpick " +
					SqlCantPend +					
					" WHERE id_fpick = ?";
			
			logger.debug("setActFPick SQL: " + SQLStmt);
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			logger.debug("ID_FPICK:  " +datos.getId_fpick());
			stm.setLong(1, datos.getId_fpick());
			
			int i = stm.executeUpdate();
			if(i>0){
				result = true;
			}
			logger.debug("rc: " + i);

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	public boolean doDelFPicking(long id_pick) throws RondasDAOException {
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		try {
			
			String sql =
				" DELETE FROM bo_fpick " +
				" WHERE id_fpick = ? ";
			
			logger.debug("en doDelFPicking");
			logger.debug("SQL: " + sql + " id_fpick="+ id_pick );
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_pick);
	
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getFormPickFaltantes(long)
	 */
	public List getFormPickFaltantes(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		List result = new ArrayList();

		String SQLStmt =
			    " SELECT dp.id_detalle, dp.id_pedido ,dp.id_producto, dp.cod_prod1, " +
				" dp.descr, dp.cant_faltante " +
				" FROM bo_fpickdetped dp " +
				" JOIN bo_fpick_op p ON p.id_pedido = dp.id_pedido " +
				" JOIN bo_detalle_rondas dr ON dr.id_detalle = dp.id_detalle and dr.id_ronda = ? "+
				" WHERE p.id_ronda = ? AND dp.cant_faltante > 0 ";
		
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getDetalleRondas");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_ronda: " + id_ronda);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR");
			stm.setLong(1, id_ronda);
			stm.setLong(2, id_ronda);			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getLong("id_detalle"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_producto(rs.getInt("id_producto"));
				prod.setCod_producto(rs.getString("cod_prod1"));
				prod.setDescripcion(rs.getString("descr"));
				prod.setCant_faltan(rs.getDouble("cant_faltante"));
				result.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getFormPickSustitutos(long)
	 */
	public List getFormPickSustitutos(long id_ronda, long id_local)	throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getFormPickSustitutos");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getFormPickSustitutos:");
		logger.debug("id_ronda: "+id_ronda);
		logger.debug("id_local: "+id_local);
		
		String Sql = " SELECT dpe.id_detalle id_det_ped, " +
				" dpe.cod_prod1 cod_sap_ped, " +
				" dpe.descr desc_ped, " +
				" dpe.cant_pickeada cant_ped, " +
				" dpe.precio precio_ped, " +
				" dpe.mot_sust mot_sust, " +
				" dpi.id_row id_row, " +
				" dpi.id_detalle id_det_pick , " +
				" dpi.cbarra cbarra, " +
				" dpi.cantidad cant_pick, " +
				" dpi.cant_rel_ped cant_rel_ped, " +
				" dpi.id_fpick id_fpick, " +
				" p.cod_prod1 cod_sap_pick, " +
				" p.des_corta desc_pick, " +
				" pr.prec_valor precio_pick " +
				" FROM bo_fpickdetped dpe " +
				" JOIN bo_fpickdetpick dpi ON dpe.id_detalle = dpi.id_detalle " +
				" JOIN bo_codbarra cb  ON dpi.cbarra = cb.cod_barra " +
				" JOIN bo_productos p  ON cb.id_producto = p.id_producto " +
				" LEFT JOIN bo_precios pr ON p.id_producto = pr.id_producto AND id_local = ? " +
				" WHERE dpi.id_ronda = ? AND dpi.mot_sust > 0 " +
				" ORDER BY dpe.id_detalle"; 
				     
		logger.debug("SQL sustitutos :"+Sql);
		
		try {
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_local);
			stm.setLong(2, id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				FormPickSustitutoDTO prod =new FormPickSustitutoDTO();
				prod.setId_row(rs.getLong("id_row"));
				prod.setSap_ped(rs.getString("cod_sap_ped"));
				prod.setCant_ped(rs.getDouble("cant_ped"));		
				prod.setDesc_ped(rs.getString("desc_ped"));
				prod.setPrecio_ped(rs.getDouble("precio_ped"));
				prod.setSap_pick(rs.getString("cod_sap_pick"));				
				prod.setCant_pick(rs.getDouble("cant_pick"));							
				prod.setDesc_pick(rs.getString("desc_pick"));	
				prod.setPrecio_pick(rs.getDouble("precio_pick"));
				prod.setId_det_ped(rs.getLong("id_det_ped"));
				prod.setId_det_pick(rs.getLong("id_det_pick"));
				prod.setCant_relped(rs.getDouble("cant_rel_ped"));
				prod.setId_fpick(rs.getLong("id_fpick"));
				list_prod.add(prod);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_prod;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getRelacionFormPickById(long)
	 */
	public DetalleFormPickingDTO getRelacionFormPickById(long id_row) throws RondasDAOException {
		PreparedStatement stm = null;		
		ResultSet rs = null;
		DetalleFormPickingDTO prod = null;

		String SQLStmt =
			    " SELECT id_row, id_detalle ,id_dronda, cbarra, " +
				" tipo, cantidad, pesable, id_form_bin, id_ronda, mot_sust, " +
				" id_fpick, cant_rel_ped " +
				" FROM bo_fpickdetpick " +
				" WHERE id_row = ? ";
		
		
		logger.debug("Ejecución DAO: " + getClass().getName()+"getRelacionFormPickById");
		logger.debug("SQL : " + SQLStmt);
		logger.debug("id_row: " + id_row);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");
			stm.setLong(1, id_row);			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				prod = new DetalleFormPickingDTO();
				prod.setId_row(rs.getLong("id_row"));
				prod.setId_detalle(rs.getLong("id_detalle"));
				prod.setId_det_ronda(rs.getLong("id_dronda"));
				prod.setCBarra(rs.getString("cbarra"));
				prod.setTipo(rs.getString("tipo"));
				prod.setCantidad(rs.getDouble("cantidad"));
				prod.setPesable(rs.getString("pesable"));
				prod.setId_form_bin(rs.getLong("id_form_bin"));
				prod.setId_ronda(rs.getLong("id_ronda"));
				prod.setMot_sust(rs.getInt("mot_sust"));
				prod.setId_fpick(rs.getLong("id_fpick"));
				prod.setCant_rel_ped(rs.getDouble("cant_rel_ped"));				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		
		return prod;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getCountFormPickRelacion(long)
	 */
	public long getCountFormPickRelacion(long id_fpick) throws RondasDAOException{
		
		long cant = 0;
		
		PreparedStatement stm =null;
		ResultSet rs=null;

		String SQLStmt = 
			" SELECT Count(*) cantidad " +
			" FROM bo_fpickdetpick " +
			" WHERE id_fpick = ?" ;
			
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");
			stm.setLong(1,id_fpick);

			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getLong("cantidad");
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

		return cant;

	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getFormPickRelacion(long)
	 */
	public List getFormPickRelacion(ProcFormPickRelacionDTO datos) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_pick = new ArrayList();
		long id_fpick = -1;
		long id_ronda = -1;
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getFormPickRelacion");
		logger.debug("-----------------------------------------------------------------");
		try {
			
			logger.debug("Parametros getFormPickRelacion:");
			
			String sql_where ="";
			if (datos.getId_fpick()>0){
				id_fpick = datos.getId_fpick();
				logger.debug("id_fpick: "+id_fpick);
				sql_where =" WHERE dp.id_fpick = " +id_fpick;				
				}
			
			if (datos.getId_ronda()>0){
				id_ronda = datos.getId_ronda();
				logger.debug("id_ronda: "+id_ronda);
				sql_where =" WHERE dp.id_ronda = " +id_ronda;				
				}
			
			
			String Sql = " SELECT dp.id_row, dp.id_detalle, dp.id_dronda, dp.cbarra, dp.tipo, dp.cantidad, dp.pesable, " +
					" dp.id_form_bin, dp.id_ronda, dp.mot_sust, dp.id_fpick, dp.cant_rel_ped, b.posicion "+
					" FROM bo_fpickdetpick dp" +
					" JOIN bo_fpickbins b ON dp.id_form_bin = b.id_form_bin " +
					sql_where;
					 
					     
			logger.debug("SQL detalle picking :"+Sql);
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			rs = stm.executeQuery();
			while (rs.next()) {
				DetalleFormPickingDTO pick = new DetalleFormPickingDTO();
				pick.setId_row(rs.getLong("id_row"));
				pick.setId_detalle(rs.getLong("id_detalle"));
				pick.setId_det_ronda(rs.getLong("id_dronda"));
				pick.setCBarra(rs.getString("cbarra"));
				pick.setTipo(rs.getString("tipo"));
				pick.setCantidad(rs.getDouble("cantidad"));
				pick.setPesable(rs.getString("pesable"));
				pick.setId_form_bin(rs.getLong("id_form_bin"));
				pick.setId_ronda(rs.getLong("id_ronda"));
				pick.setMot_sust(rs.getInt("mot_sust"));
				pick.setId_fpick(rs.getLong("id_fpick"));
				pick.setCant_rel_ped(rs.getDouble("cant_rel_ped"));
				pick.setPosicion(rs.getInt("posicion"));
				list_pick.add(pick);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_pick;
	}



	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getPickSinRelacionFormPick(long)
	 */
	public List getPickSinRelacionFormPick(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_pick = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPickSinRelacionFormPick");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getPickSinRelacionFormPick:");
		logger.debug("id_ronda: "+id_ronda);
		
		String Sql = " SELECT p.id_fpick, p.id_ronda, p.cbarra, p.cantidad, " +
				     " p.id_form_bin, b.id_pedido " +
			 		 " FROM bo_fpick p " +
			 		 " JOIN bo_fpickbins b ON b.id_form_bin = p.id_form_bin" +
			 		 " LEFT JOIN bo_fpickdetpick dp on p.id_fpick = dp.id_fpick " +
			 		 " WHERE p.id_ronda = ? and  dp.id_fpick is null";
				 
				     
		logger.debug("SQL detalle picking :"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				FPickDTO pick = new FPickDTO();
				pick.setId_fpick(rs.getLong("id_fpick"));
				pick.setId_ronda(rs.getLong("id_ronda"));
				pick.setCbarra(rs.getString("cbarra"));
				pick.setCantidad(rs.getDouble("cantidad"));
				pick.setId_form_bin(rs.getLong("id_form_bin"));
				pick.setId_op(rs.getLong("id_pedido"));
				list_pick.add(pick);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_pick;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getPedSinRelacionFormPick(long)
	 */
	public List getPedSinRelacionFormPick(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedSinRelacionFormPick");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getPedSinRelacionFormPick:");
		logger.debug("id_ronda: "+id_ronda);
		
		String Sql = " SELECT dpe.id_detalle, dpe.id_pedido, dpe.id_producto, " +
				" dpe.cant_pedido, dpe.cant_pickeada,cb.cod_barra, dpe.es_pesable," +
				" dpe.id_dronda, pp.id_ronda " +
				" FROM bo_fpickdetped dpe " +
				" JOIN bo_fpick_op pp ON pp.id_pedido = dpe.id_pedido " +
				" JOIN bo_codbarra cb ON dpe.id_producto = cb.id_producto " +
				" LEFT JOIN bo_fpickdetpick dp on dp.id_detalle = dpe.id_detalle " +
				" WHERE pp.id_ronda = ? and  dp.id_detalle is null";
				 
				     
		logger.debug("SQL:"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosPedidoDTO prod = new ProductosPedidoDTO();
				prod.setId_detalle(rs.getLong("id_detalle"));
				prod.setId_pedido(rs.getLong("id_pedido"));
				prod.setId_producto(rs.getLong("id_producto"));
				prod.setCant_solic(rs.getDouble("cant_pedido"));
				prod.setCant_pick(rs.getDouble("cant_pickeada"));
				prod.setCod_barra(rs.getString("cod_barra"));
				prod.setPesable(rs.getString("es_pesable"));
				prod.setId_dronda(rs.getLong("id_dronda"));
				prod.setId_ronda(rs.getLong("id_ronda"));
				list_prod.add(prod);
				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_prod;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#existeRonda(long)
	 */
	public boolean existeRonda(long id_ronda) throws RondasDAOException {
		boolean result = false;
		long cant = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"existeRonda");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros existeRonda:");
		logger.debug("id_ronda: "+id_ronda);
		
		String Sql = " SELECT count(*) cantidad " +
					 " FROM bo_rondas " +
					 " WHERE id_ronda = ?";
				 
				     
		logger.debug("SQL:"+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( Sql + " WITH UR" );
			stm.setLong(1,id_ronda);

			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getLong("cantidad");
			}
			if (cant > 0 ){
				result = true;
			}
				

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#existePedidoEnRonda(long)
	 */
	public boolean existePedidoEnRonda(long id_ronda, long id_pedido) throws RondasDAOException {
		boolean result = false;
		long cant = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"existePedidoEnRonda");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros existePedidoEnRonda:");
		logger.debug("id_ronda: "+id_ronda);
		
		String Sql = " SELECT count(*) cantidad " +
					 " FROM bo_fpick_op " +
					 " WHERE id_ronda = ? AND id_pedido= ?";
				 
				     
		logger.debug("SQL:"+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( Sql + " WITH UR" );
			stm.setLong(1,id_ronda);
			stm.setLong(2,id_pedido);

			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getLong("cantidad");
			}
			if (cant > 0 ){
				result = true;
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	public boolean setFormPickReseteaRonda(long id_ronda) throws RondasDAOException {
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			" DELETE FROM bo_fpick_ronda " +
			" WHERE id_ronda = ? ";
		
		logger.debug("en setFormPickReseteaRonda");
		logger.debug("SQL: " + sql + " id_ronda "+ id_ronda );
		
		try {
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_ronda);
	
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

			logger.debug("elimino ronda? " + result);
			
		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getPedidosByRonda(long)
	 */
	public List getPedidosByRonda(long id_ronda) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_op = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidosByRonda");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getPedidosByRonda:");
		logger.debug("id_ronda: "+id_ronda);
		
		String Sql = " SELECT id_pedido, id_ronda, estado FROM bo_fpick_op WHERE id_ronda = ?";
				 
				     
		logger.debug("SQL:"+Sql);
		
		try {

			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				FormPickOpDTO op_ronda = new FormPickOpDTO();
				op_ronda.setId_pedido(rs.getLong("id_pedido"));
				op_ronda.setId_ronda(rs.getLong("id_ronda"));
				op_ronda.setEstado(rs.getString("estado"));
				
				list_op.add(op_ronda);
				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return list_op;
	}
	

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getCbarraByIdProd(long)
	 */
	public String getCbarraByIdProd(long id_prod) throws RondasDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		String cbarra = "";
		
		String Sql = " SELECT * FROM (" +
				     " SELECT row_number() over() as row, cod_barra " +
				     " FROM bo_codbarra " +
				     " WHERE id_producto = ? )AS TEMP WHERE row " +
				     " BETWEEN 1 AND 1 ";

		logger.debug("SQL:"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				cbarra = rs.getString("cod_barra");				
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return cbarra;
		
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getZonasFinalizadas(long)
	 */
	public List getZonasFinalizadas(long id_ronda) throws RondasDAOException {
		
		List resultado = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String query = " SELECT distinct zon.id_zona, zon.nombre " +
		" FROM bo_pedidos ped left join bo_detalle_rondas ron on ped.id_pedido = ron.id_pedido " +
		" join bo_zonas zon on zon.id_zona = ped.id_zona " +
		" WHERE ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND id_jpicking in ( " +
		" SELECT distinct id_jpicking " +
		" FROM bo_pedidos ped left join bo_detalle_rondas ron on ped.id_pedido = ron.id_pedido " +
		" join bo_zonas zon on zon.id_zona = ped.id_zona " +
		" where ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND ron.id_ronda = ? ) " +
		" and zon.id_zona not in ( " +
		" SELECT distinct zon.id_zona " +
		" FROM bo_pedidos ped left join bo_detalle_rondas ron on ped.id_pedido = ron.id_pedido " +
		" join bo_zonas zon on zon.id_zona = ped.id_zona " +
		" WHERE ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ",7,8,9,10,54,21,20) " +
		" and id_jpicking in ( " +
		" SELECT distinct id_jpicking " +
		" FROM bo_pedidos ped left join bo_detalle_rondas ron on ped.id_pedido = ron.id_pedido " +
		" join bo_zonas zon on zon.id_zona = ped.id_zona " +
		" where ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") AND ron.id_ronda = ? ) )";		 
	     
		logger.debug("SQL:"+query + " id_ronda=" + id_ronda);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//los que no estan en picking
			stm = conn.prepareStatement(query + " WITH UR");
			stm.setLong(1, id_ronda);
			stm.setLong(2, id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				
				ZonaDTO zona = new ZonaDTO();
				zona.setId_zona(rs.getLong("id_zona"));
				zona.setNombre(rs.getString("nombre"));
				resultado.add(zona);
			}

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return resultado;
	}
	/* *************************************************************
	 *            Funciones de Prorrateo
	 ***************************************************************/
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getDetPickingByIdDetalle(long)
	 */
	public List getDetPickingByIdDetalle(long id_detalle) throws RondasDAOException {
		logger.debug("en getDetPickingByIdDetalle");
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT 	dp.id_dpicking, dp.id_detalle, dp.id_bp, dp.id_producto, dp.id_pedido, dp.cbarra,"+
						 "		dp.descripcion, dp.precio, dp.cant_pick, dp.sustituto"+ 
						 "	FROM 	bo_detalle_picking dp"+
						 "	WHERE dp.id_detalle = ?";
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,id_detalle);
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_detalle);

			rs = stm.executeQuery();
			while (rs.next()) {
				DetallePickingDTO dpick = new DetallePickingDTO();
				dpick.setId_dpicking(rs.getLong("id_dpicking"));
				dpick.setId_detalle(rs.getLong("id_detalle"));
				dpick.setId_bp(rs.getLong("id_bp"));
				dpick.setId_producto(rs.getLong("id_producto"));
				dpick.setId_pedido(rs.getLong("id_pedido"));
				dpick.setCBarra(rs.getString("cbarra"));
				dpick.setDescripcion(rs.getString("descripcion"));
				dpick.setPrecio(rs.getDouble("precio"));
				dpick.setCant_pick(rs.getDouble("cant_pick"));
				dpick.setSustituto(rs.getString("sustituto"));
				result.add(dpick);
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getCountDetPickByIdDet(long)
	 */
	public long getCountDetPickByIdDet(long id_detalle) throws RondasDAOException {
		PreparedStatement stm = null; 
		ResultSet rs = null;
		long cant = 0;
		logger.debug("en getCountDetPickByIdDet:");
		try {

			String sql = 	"SELECT count(*) cantidad " +
							" FROM BO_DETALLE_PICKING " +
							" WHERE id_detalle = ?";

			logger.debug("SQL query: " + sql+" id_detalle: " +id_detalle);
		
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_detalle);
			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getLong("cantidad");
			}
			
		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("ok");
		return cant;		
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#doActualizaPrecioDetPick(long, double)
	 */
	public boolean doActualizaPrecioDetPick(long id_dpick, double precio) throws RondasDAOException {
		PreparedStatement stm = null;
		boolean result = false;
		
		logger.debug("En doActualizaPrecioDetPick...");
		
		try {
			
			String Sql = "UPDATE bo_detalle_picking set " +
					    " precio = ? " +
					    " where id_dpicking = ? ";
	
			logger.debug("SQL :"+Sql);	
			logger.debug("precio : " + precio);
			logger.debug("id_dpicking : " + id_dpick);
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setDouble(1, precio);
			stm.setLong(2, id_dpick);
						
			int i = stm.executeUpdate();
			if (i>0) {
	            result = true;
	        }
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RondasDAOException(e);			
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
		logger.debug("result : "+result);
		return result;

	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getDetallesPedido(long)
	 */
	public List getDetallesPedido(long id_pedido) throws RondasDAOException {
			logger.debug("en getDetallesPedido");
			
			List result = new ArrayList();
			PreparedStatement stm=null;
			ResultSet rs=null;

			try {

				logger.debug("getDetallesPedido " );
				String sql = " SELECT id_detalle, id_pedido, id_sector, id_producto, cod_prod1, uni_med, " +
					" descripcion, cant_solic, observacion, preparable, con_nota, pesable, cant_pick, cant_faltan, cant_spick, " +
					" precio_lista, precio " +
					" FROM bo_detalle_pedido " +
					" WHERE id_pedido = ? ";
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
					prod.setPrecio(rs.getDouble("precio"));
					prod.setPrecio_lista(rs.getDouble("precio_lista"));
					result.add(prod);
				}

			} catch (SQLException e) {
				throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getPromocionesRonda(long)
	 */
	public List getPromocionesRonda(long id_ronda) throws RondasDAOException {
		logger.debug("en getPromocionesRonda");
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			logger.debug("getPromocionesRonda " );
			String sql = "SELECT dr.id_ronda, dp.id_detalle, dp.id_producto, " +
					" dp.id_pedido, dpd.id_promocion, p.sustituible, p.faltante " +
					" FROM bo_detalle_rondas dr " +
					" JOIN bo_detalle_pedido dp ON dr.id_detalle = dp.id_detalle " +
					" JOIN bo_promos_detped dpd ON dp.id_detalle = dpd.id_detalle " +
					" JOIN pr_promocion p ON dpd.id_promocion = p.id_promocion " +
					" WHERE dr.id_ronda = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_ronda);
			logger.debug("SQL: " + sql);
			logger.debug("id_ronda: " + id_ronda);
			rs = stm.executeQuery();
			while (rs.next()) {
				
				PromoDetPedRondaDTO promo = new PromoDetPedRondaDTO();
				promo.setId_ronda(rs.getLong("id_ronda"));
				promo.setId_detalle(rs.getLong("id_detalle"));
				promo.setId_producto(rs.getLong("id_producto"));
				promo.setId_pedido(rs.getLong("id_pedido"));
				promo.setId_promocion(rs.getLong("id_promocion"));
				promo.setSustituible(rs.getString("sustituible"));
				promo.setFaltante(rs.getString("faltante"));
				
				result.add(promo);
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#getPromocionesByIdDetalle(long)
	 */
	public List getPromocionesByIdDetalle(long id_detalle) throws RondasDAOException {
		logger.debug("en getPromocionesByIdDetalle");
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			String sql = "SELECT dp.id_detalle, dp.id_producto, " +
					" dp.id_pedido, dp.id_promocion, p.sustituible, p.faltante " +
					" FROM bo_promos_detped dp  " +
					" JOIN pr_promocion p ON dp.id_promocion = p.id_promocion " +
					" WHERE dp.id_detalle = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_detalle);
			logger.debug("SQL: " + sql);
			logger.debug("id_detalle: " + id_detalle);
			rs = stm.executeQuery();
			while (rs.next()) {
				
				PromoDetPedRondaDTO promo = new PromoDetPedRondaDTO();
				promo.setId_detalle(rs.getLong("id_detalle"));
				promo.setId_producto(rs.getLong("id_producto"));
				promo.setId_promocion(rs.getLong("id_promocion"));
				promo.setSustituible(rs.getString("sustituible"));
				promo.setFaltante(rs.getString("faltante"));
				
				result.add(promo);
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	public List getSustitutosCriterio() throws RondasDAOException {
		logger.debug("en getSustitutosCriterio");
		
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = "SELECT id_criterio, descripcion FROM FO_SUSTITUTOS_CRITERIO  " ;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL: " + sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				
				FOSustitutosCriteriosDTO scrit = new FOSustitutosCriteriosDTO();
				scrit.setId_criterio(rs.getInt("id_criterio"));
				scrit.setNombre_criterio(rs.getString("descripcion"));
				
				result.add(scrit);
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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

	public long getIdSectorByNombre(String nombre) throws RondasDAOException {
		logger.debug("en getIdSectorByNombre");
		long id_sector = 0L;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			String sql = "SELECT ID_SECTOR FROM BODBA.BO_SECTOR WHERE NOMBRE = ?";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, nombre);
			
			logger.debug("SQL: " + sql);

			rs = stm.executeQuery();
			if (rs.next()) {
				id_sector = rs.getLong("ID_SECTOR");
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("id_sector: " + id_sector);
		return id_sector;
	}

	public Hashtable setOrdenProductosPDA(long id_ronda) throws RondasDAOException {
		logger.debug("en setOrdenProductosPDA");
		Hashtable ordenRonda = new Hashtable();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
		    String sql = "SELECT * FROM (SELECT row_number() over( ORDER BY S.ID_SECTOR, "
		               + "                                                  CASE WHEN SXS.ID_SECCION IS NULL  THEN INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) "
                       + "                                                       ELSE INTEGER(RTRIM(SXS.ORDEN)) "
                       + "                                                  END, "
                       + "                                                  CASE WHEN RS.ID_RUBRO IS NULL  THEN INTEGER(SUBSTR(PROD.ID_CATPROD, 3, 2)) "
                       + "                                                       ELSE INTEGER(RTRIM(RS.ORDEN)) "
                       + "                                                  END, "
                       + "                                                  DP.DESCRIPCION) AS ORDEN, " 
                       + "                      DP.ID_DETALLE "
                       + "               FROM BODBA.BO_RONDAS R "
                       + "                    JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_RONDA      = R.ID_RONDA "
                       + "                    JOIN BODBA.BO_DETALLE_PEDIDO DP ON DP.ID_DETALLE    = DR.ID_DETALLE "
                       + "                    JOIN BODBA.BO_PRODUCTOS    PROD ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO "
                       + "                    LEFT JOIN BODBA.BO_SECTOR     S ON S.ID_SECTOR      = DP.ID_SECTOR "
                       + "                    LEFT JOIN BODBA.BO_SECCIONXSECTOR SXS ON SXS.ID_SECCION = INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AND "
                       + "                                                             SXS.ID_SECTOR  = S.ID_SECTOR "
                       + "                    LEFT JOIN BODBA.BO_SECCION SC ON SC.ID_SECCION = SXS.ID_SECCION "
                       + "                    LEFT JOIN BODBA.BO_RUBROXSECCION  RS ON RS.ID_SECCION = INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AND "
                       + "                                                            RS.ID_RUBRO   = INTEGER(SUBSTR(PROD.ID_CATPROD, 3, 2)) "
                       + "               WHERE R.ID_RONDA = ? "
                       + "             ) AS TEMP "
                       + "WHERE ORDEN BETWEEN 1 AND 9999";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_ronda);
			
			logger.debug("SQL: " + sql);

			rs = stm.executeQuery();
			while (rs.next()) {
			    ordenRonda.put(rs.getString("ID_DETALLE"), rs.getString("ORDEN"));
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
		logger.debug("Elementos en ordenRonda: " + ordenRonda.size());
		return ordenRonda;
		
	}

	//	---------- mod_ene09 - ini------------------------
	
	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.RondasDAO#updRondaFinal(cl.bbr.jumbocl.pedidos.dto.RondaDTO)
	 */

	public void updRondaFinal(RondaDTO ronda) throws RondasDAOException {

		PreparedStatement stm=null;
		String SQLStmt = 
				" UPDATE  bo_rondas " +
				" SET id_usuario_fiscal = ? ," +
				"     e1 = ? ," +
				"     e2 = ? ," +
				"     e3 = ? ," +
				"     e4 = ? ," +
				"     e5 = ? ," +
				"     e6 = ? ," +
				"     e7 = ? " +
				" WHERE id_ronda = ? "
				;
		
		logger.debug("SQL updRondaFinal: " + SQLStmt);
		
		try {
			logger.debug(ronda.getFiscalizador()+",e1:"+
					ronda.getE1()+",e2:"+
					ronda.getE2()+",e3:"+
					ronda.getE3()+",e4:"+
					ronda.getE4()+",e5:"+
					ronda.getE5()+",e6:"+
					ronda.getE6()+",e7:"+
					ronda.getE7()+",ronda:"+
					ronda.getId_ronda());
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, ronda.getFiscalizador());
			stm.setInt(2, ronda.getE1());
			stm.setInt(3, ronda.getE2());
			stm.setInt(4, ronda.getE3());
			stm.setInt(5, ronda.getE4());
			stm.setInt(6, ronda.getE5());
			stm.setInt(7, ronda.getE6());
			stm.setInt(8, ronda.getE7());
			stm.setLong(9, ronda.getId_ronda());			
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			stm.close();

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene id de usuario a partir del login
	 * 
	 * @param  login String 
	 * @return id_usuario; -1 si no encuentra al usuario
	 * @throws UsuariosDAOException
	 */
	public long getUserIdByLogin(String login) throws UsuariosDAOException {

		PreparedStatement stm = null;
		ResultSet rs = null;
		long id_usuario = -1;
		int i=0;
		
		String SQLStmt =
			"SELECT id_usuario " +
			" FROM bo_usuarios " +
			" WHERE UPPER(login) = '" + login.toUpperCase() + "' " ;
		
		logger.debug("DAO getUserByLogin");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("login: " + login);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			rs = stm.executeQuery();

			while (rs.next()) {
				id_usuario = rs.getLong("id_usuario");
				i++;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
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
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		
		if ( i > 1 ){
			logger.info("Se encontró más de un usuario con el mismo login!!!!");
			logger.debug("N° regs: " + i);
		}		
		
		logger.debug("id_usuario: " + id_usuario);
		
		return id_usuario;
		
	}	
	
	//	---------- mod_ene09 - fin------------------------
	

    //  Jean: ////////////////////////////////////////////////////////

    /**
     * Realiza la operación completa en una transacción
     * 
     * @author jdroguett
     * @param binsConDetalle
     * @param resultadoPedido
     * @param detallePicking
     * @param usuarioFiscalId
     * @throws RondasDAOException
	 * @throws BolException
	 * @throws PedidosDAOException
     */
	public void grabarRonda(long rondaId, List binsConDetalle,
			List resultadoPedidos, List detallePicking,
			TPRegistroPickingDTO regPick, long usuarioFiscalId)
			throws RondasDAOException, BolException, PedidosDAOException {

        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            
            detallesPedidosSobreUmbral = new ArrayList();
           
            logger.debug("Vamos a grabarBinsDetallePicking..");
           
            grabarBinsDetallePicking(conn, binsConDetalle, resultadoPedidos);
            
			//actualizo el detalle de pedido para marcar como no pickeados los
			// productos sobre umbral
			logger.debug("a setear como no pickeados detallesPedidosSobreUmbral "
							+ detallesPedidosSobreUmbral.toString());
			for (Iterator iter = detallesPedidosSobreUmbral.iterator(); iter
					.hasNext();) {
				Long element = (Long) iter.next();
				for (Iterator iterator = resultadoPedidos.iterator(); iterator
						.hasNext();) {
					TPDetallePedidoDTO dtPed = (TPDetallePedidoDTO) iterator
							.next();
					//valido si es que el id de detalle del pedido es el del
					// producto sobre umbral
					if(dtPed.getId_detalle() == element.longValue()){
						//si es el valor, seteo como si fuese un elemento no
						// pickeado
						dtPed.setCant_faltante(0);
						dtPed.setCant_sinpickear(dtPed.getCant_pedida());
						dtPed.setCant_pickeada(0);
						dtPed
								.setSust_camb_form(Constantes.SUST_CAMB_FORM_NO_EXISTE);
					}
				}
				
			}
			actualizarDetalleRondaFinalizar(conn, rondaId, resultadoPedidos,
					detallePicking, regPick, usuarioFiscalId);
            
            actualizarDetallePedidos(conn, resultadoPedidos);

            //HashSet para no tener ids de pedidos repetidos
            HashSet pedidosIds = new HashSet();
            HashSet AllpedidosIds = new HashSet();
            
            for (int i = 0; i < resultadoPedidos.size(); i++) {
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO) resultadoPedidos
						.get(i);
                AllpedidosIds.add(new Long(detPOS.getId_op()));
                if (detPOS.getId_op() != Constantes.ID_ESTAD_PEDIDO_ANULADO){
                    pedidosIds.add(new Long(detPOS.getId_op()));
                }
            }
            String ids = join(Arrays.asList(pedidosIds.toArray()));


            
			//** @Inicio by Indra 06-10-12 *//*
			
           /**
			 * ---INICIO COMPARACION UMBRALES--// Realizo la consulta para saber
			 * si la ronda cumple con los montos y unidades de umbral de los
			 * parametros, para luego pasarselo a grabarRondas para que no en el
			 * caso de que cumpla la condicion grabe el estado en BODEGA, pero
			 * en caso contrario, lo deje en un estado pendiente. AQUI REALIZO
			 * LA CONSULTA A LA JDBC PARA SACAR LA CANTIDAD DE LOS UMBRALES DE
			 * MONTO Y UNIDAD
			 */
            
            
		    Iterator iter = pedidosIds.iterator();
			String itera = "";
			String conParentesis = "";
			String npedido = "";
			boolean  masRondas = false;
			boolean umbralActivacion;
			double total_unidad = 0;
			double total_monto = 0;
			long pedido = 0;
			while (iter.hasNext()) {
				pedido= Long.parseLong(String.valueOf(iter.next()));
				itera = String.valueOf(pedido);
				conParentesis = "(" + itera + ")";
				umbralActivacion  = umbralActivado(pedido);
				masRondas = pedidoTieneMasrondas(pedido);
				if (!masRondas) //si todas las rondas finalizan el estado
									 // de spick deberia ser 0
				{
					
					if(umbralActivacion)
					{
						
						List lst_umb = null;
						total_unidad = 0;
						total_monto = 0;
						lst_umb = getAvanceUmbralParametros(pedido);//saco el
																	   // umbral
																	   // monto y
																	   // unidad de
																	   // la tabla
																	   // BO_UMBRALES
						for (int i = 0; i < lst_umb.size(); i++) {
							AvanceDTO det = (AvanceDTO) lst_umb.get(i);

							total_unidad = det.getCant_unidad_umbrales();
							total_monto = det.getCant_monto_umbrales();
							}
						// Aqui realizo la consulta de la cantida de productos
						// solicitados del pedido
						long prodsolicitados = getTotalProductoSolicitado(pedido);
						long precioTotal = getAvanceUmbralMonto(pedido);
						double total_monto_pickeado = 0;
						double tot_prod_pick = 0;
						double porc_prod_pickeados = 0;
						double porc_montos_pickeados = 0;
						double tot_prod_pickCS = 0;
						double tot_prod_pickCSNormal = 0;
						double porc_prod_pickeadosCS = 0;
						double precio_total = 0;

						long precioTotalPickeado = getAvanceUmbralMontoSinSust(pedido);
						porc_montos_pickeados = ((precioTotalPickeado * 100) / precioTotal);//porcentaje
																							// de
																							// productos
																							// pickeados
																							// por
																							// monto
						logger.debug("Precio Total " + porc_montos_pickeados);
						System.out
								.println("Precio Tottal " + porc_montos_pickeados);
						// Realizo la consulta para sacar los productos pickeados
						// SIN SUSTITUTOS
						List lst_umb1 = null;
						lst_umb1 = getAvanceUmbralPedidoSinSust(pedido);
						System.out.print(lst_umb1.size());
						for (int i = 0; i < lst_umb1.size(); i++) {
							AvanceDTO det = (AvanceDTO) lst_umb1.get(i);
							tot_prod_pick = (det.getCant_prod_en_bodega());
							logger.debug("Total Productos Pickeados sin sustituo"
									+ tot_prod_pick);

						}
						// Realizo la consulta para sacar los productos pickeados
						// CON SUSTITUTOS CAMBIO FORMATO
						List lst_umb2 = null;
						lst_umb2 = getAvanceUmbralPedidoConSustNormal(pedido);
						System.out.print(lst_umb1.size());
						for (int i = 0; i < lst_umb2.size(); i++) {
							AvanceDTO det = (AvanceDTO) lst_umb2.get(i);

							tot_prod_pickCS = (det.getCant_prod_en_bodega());
							}

						double tot_prod_pickTOTAL = tot_prod_pickCS + tot_prod_pick;
						porc_prod_pickeados = (tot_prod_pickTOTAL * 100)
								/ prodsolicitados;//porcentaje de productos
												  // pickeados por unidad
						//Realizo la consulta de la tabla porcentaje de pedidos
							
						
						// revisar
						long consulta = consultaPorcPedidos(itera);

						if (consulta > 0) {
							updatePorcPedido(itera, porc_prod_pickeados,
									porc_montos_pickeados);

						} else {
							insertarPorcPedido(itera, porc_prod_pickeados,
									porc_montos_pickeados);

						}
						if (total_unidad > 0 && total_monto > 0) {
							//AQUI REALIZO LA MODIF DEL ESTADO DEL PEDIDO SI SE
							// CUMPLE LA CONDICION SI NO QUE SIGA IGUAL
							if ((porc_prod_pickeados < total_unidad)
									|| (porc_montos_pickeados < total_monto)) {

								
								actualizarEstadoPedidosRevFaltante(conn,
										conParentesis, rondaId);
							// cumple con el umbral
								
							} else {
								conn = this.getConnection();
								logger.debug("Actualizar Estado Pedido ");
								actualizarEstadoPedidos(conn, conParentesis,
										rondaId);
							}
						}
						
					}
						
					
					//tiene activado el Umbral

					else {
						conn = this.getConnection();
						logger.debug("Actualizar Estado Pedido else ");
						actualizarEstadoPedidos(conn, conParentesis, rondaId);
					}

				}//Tiene mas rondas el pedido

				else {
					conn = this.getConnection();
					actualizarEstadoPedidos(conn, conParentesis, rondaId);
				}

				//---FIN COMPARACION UMBRALES--//
				// actualizarEstadoPedidos(conn, ids, rondaId);

				// de los pedidos que quedaron en bodega, vemos cuales estan
				// sobrepasados con el monto reservado
				// y los dejamos marcados!

				//** @Fin by Indra 06-10-12 *//*
				/**
				 * * PROMOCIONES ** DELETE esto se cambia de posicion ya que hay
				 * que hacerlo una vez que se re-apliquen las promociones
				 * 
				 * marcaMontosReservadosExcedidos(conn, ids);
				 *  
            */
            
			}
            insertarAlertaPorFaltantes(conn, ids);
            
			insertarTrackingPedido(conn,
					Arrays.asList(AllpedidosIds.toArray()), regPick);
            
            insertarTrackingRonda(conn, rondaId, regPick);
 
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("grabar Ronda: " + e.getMessage());
            try {
				conn = this.getConnection();
                conn.rollback();
            } catch (SQLException e1) {
                logger.error(e1.getMessage());
                throw new RondasDAOException(e1);
            }
            throw new RondasDAOException(e);
		} finally {
			try {
				detallesPedidosSobreUmbral.clear();
				conn.commit();
				conn.setAutoCommit(true);
				releaseConnection();

			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @author jdroguett
     * @param conn
     * @throws SQLException
     */
    private void insertarTrackingRonda(Connection conn, long rondaId, TPRegistroPickingDTO regPick) throws SQLException {
        String sql = "INSERT INTO bo_log_rondas (id_ronda, usuario, descr, fechahora) " +
					"VALUES (?, ?, ?, current_timestamp)";
		PreparedStatement stm = null; 
		try {
			stm = conn.prepareStatement(sql);
			stm.setLong(1, rondaId);
			stm.setString(2, regPick.getUsuario());
			stm.setString(3, Constantes.RONDA_MNS_FIN_DESCARGA);
			stm.executeUpdate();
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
        
    }

    /**
     * FIXME talvez esté metodo ya no sirva con la nuevas modificaciones que entegará BBR.
     * Acá se pueden insertar varios usuario para un mismo pedido.
     * @author jdroguett 
     * @param conn
     * @throws SQLException
     */
    private void insertarTrackingPedido(Connection conn, List pedidosIds, TPRegistroPickingDTO regPick) throws SQLException {
        PreparedStatement stm = null;
        try {
            for (int i = 0; i < pedidosIds.size(); i++) {
                Long pedidoId = (Long) pedidosIds.get(i);
                String sql = "INSERT INTO bo_tracking_od (id_pedido, usuario, descripcion, id_mot, id_mot_ant) "
                        + " SELECT id_pedido, '"
                        + regPick.getUsuario()
                        + "', (select nombre from bo_perfiles where id_perfil = "
                        + regPick.getPerfil()
                        + ") "
                        + " || ' realizó operación a las "
                        + regPick.getHora()
                        + "'"
                        + ", id_mot, id_mot_ant "
                        + " FROM bo_tracking_od "
                        + " where id_tracking = (select max(id_tracking) from bo_tracking_od where id_pedido = "
                        + pedidoId + " )";
                logger.debug("insertarTrackingPedido sql: " + sql);
                stm = conn.prepareStatement(sql);
                stm.executeUpdate();
            }
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
    }

    /**
     * inserta las alertar por faltantes de productos
     * @author jdroguett
     * @param conn
     * @param pedidosIds
     * @throws SQLException
     */
    private void insertarAlertaPorFaltantes(Connection conn, String pedidosIds) throws SQLException {
        String sql = "insert into bo_alerta_op(id_alerta, id_pedido)                                      "
                + " select " + Constantes.ALE_OP_TODOS_FALTANTES + ", id_pedido                           "
                + " from bo_detalle_pedido where id_pedido in " + pedidosIds
                + " group by id_pedido having (sum(cant_solic) - sum(cant_faltan) ) <= 0";
        logger.debug("insertarAlertaPorFaltantes: " + sql);
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(sql);
            stm.executeUpdate();
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
    }

    /**
     * Marca los pedidos terminado con estado en bodega. Un pedido terminado
     * tiene todas sus rondas finalizadas y no posee productos sin pickear.
     * 
     * @author jdroguett
     * @param conn
     * @param resultadoPedidos
     * @throws SQLException
     */
    private void actualizarEstadoPedidos(Connection conn, String pedidosIds, long rondaId) throws SQLException {
      	String sqlBins = "update bo_pedidos set cant_bins = (coalesce(cant_bins,0) + " +
      			"(      select count(id_bp) from bo_bins_pedido " +
      			"       where id_ronda = ? and bo_bins_pedido.id_pedido = bo_pedidos.id_pedido " +
      			")) where id_pedido in " + pedidosIds;

        //Se agrega condicio que pedido no este anulado al momento de descargar la ronda
        String sql = "update bo_pedidos set id_estado = "
                + Constantes.ID_ESTAD_PEDIDO_EN_BODEGA
                + " where id_estado != " + Constantes.ID_ESTAD_PEDIDO_ANULADO + " and id_pedido in                                                   "
                + "(select id_pedido from bo_pedidos where id_pedido in " + pedidosIds + " except "
                + " ((select id_pedido from bo_rondas r inner join bo_detalle_rondas dr on dr.id_ronda = r.id_ronda "
                + " where id_pedido in " + pedidosIds + " and r.id_estado != " + Constantes.ID_ESTADO_RONDA_FINALIZADA
                + ") " + " union (select id_pedido from bo_detalle_pedido where id_pedido in " + pedidosIds
                + " group by id_pedido having sum(cant_spick)  > 0)))";
        
        logger.debug("actualizarEstadoPedidos: " + sql);
        
        PreparedStatement stm = null;
        try {
        	stm = conn.prepareStatement(sqlBins);
        	stm.setLong(1, rondaId);
        	stm.executeUpdate();
            
        	stm = conn.prepareStatement(sql);
            int NumRegUpd = stm.executeUpdate();
            
            logger.debug("actualizarEstadoPedidos (NumRegUpd): " + NumRegUpd);
            
        } catch(SQLException e){
            logger.error("actualizarEstadoPedidos: " + e.getMessage());
            throw e;
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
    }
		
	//* Inicio @Code by Indra
    /**
	 * Marca los pedidos en Revision Fatante cuando el umbral no ha sido
	 * sobreapasado
	 * 
	 * 
	 * @author RMI-DNT
	 * @param conn
	 * @param resultadoPedidos
	 * @throws SQLException
	 */
	private void actualizarEstadoPedidosRevFaltante(Connection conn,
			String pedidosIds, long rondaId) throws SQLException {
		String sqlBins = "update bo_pedidos set cant_bins = (coalesce(cant_bins,0) + "
				+ "(      select count(id_bp) from bo_bins_pedido "
				+ "       where id_ronda = ? and bo_bins_pedido.id_pedido = bo_pedidos.id_pedido "
				+ ")) where id_pedido in " + pedidosIds;

		//Se agrega condicion que pedido no este anulado al momento de descargar
		// la ronda
		
		String sql = "update bo_pedidos set id_estado = "
			+ Constantes.ID_ESTAD_PEDIDO_REVISION_FALTAN
			+ " where id_estado != "
			+ Constantes.ID_ESTAD_PEDIDO_ANULADO
			+ " and id_pedido in                                                   "
			+ "(select id_pedido from bo_pedidos where id_pedido in "
			+ pedidosIds
			+ " except "
			+ " ((select id_pedido from bo_rondas r inner join bo_detalle_rondas dr on dr.id_ronda = r.id_ronda "
			+ " where id_pedido in "
			+ pedidosIds
			+ " and r.id_estado != "
			+ Constantes.ID_ESTADO_RONDA_FINALIZADA
			+ " )"
			+ " union (select id_pedido from bo_detalle_pedido where id_pedido in "
			+ pedidosIds
			+ " group by id_pedido having sum(cant_spick)  > 0)))";
		logger.debug("actualizarEstadoPedidosRev faltantes: " + sql);

		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sqlBins);
			stm.setLong(1, rondaId);
			stm.executeUpdate();

			stm = conn.prepareStatement(sql);
			int NumRegUpd = stm.executeUpdate();

			logger.debug("actualizarEstadoPedidos (NumRegUpd): " + NumRegUpd);

		} catch (SQLException e) {
			logger.error("actualizarEstadoPedidos: " + e.getMessage());
			throw e;
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			} finally {
				stm = null;
			}
		}
	}

	//  * Fin @Code by Indra
	/**
     * Actualiza pedidos. Actualiza las cantidades pikeadas del pedido.
     * 
     * @author jdroguett
     * @param conn
     * @param resultadoPedidos
     * @throws SQLException
     */
	private void actualizarDetallePedidos(Connection conn, List resultadoPedidos)
			throws SQLException {
        logger.debug("Ingreso al metodo actualizarDetallePedidos");
        logger.debug("Preparo la query");
        String sql = " UPDATE bo_detalle_pedido "
                + " SET cant_pick = ( coalesce(cant_pick,0) + ? ), cant_faltan = ( coalesce(cant_faltan,0) + ? ), "
                + " cant_spick =  (coalesce(cant_spick,0) + ? ) WHERE id_detalle = ? ";
        PreparedStatement stm = null;
        logger.debug("sql " + sql);
        try {
        	logger.debug("Preparo el sql");
            stm = conn.prepareStatement(sql);
            logger.debug("Recorro el resultado de pedidos");
            for (int i = 0; i < resultadoPedidos.size(); i++) {
				TPDetallePedidoDTO detPed = (TPDetallePedidoDTO) resultadoPedidos
						.get(i);
                
                // agrego flag para mis productos sobre margen
                boolean sobreMargen = false;
                // recorro mis productos sobre margen marcados 
				for (Iterator iter = detallesPedidosSobreUmbral.iterator(); iter
						.hasNext();) {
    				Long element = (Long) iter.next();				
					//valido si es que el id de detalle del pedido es el del
					// producto sobre umbral
					if(detPed.getId_detalle() == element.longValue()){
						//si es el valor, seteo como si fuese un elemento no
						// pickeado
						detPed.setCant_faltante(0);
						detPed.setCant_sinpickear(detPed.getCant_pedida());
						detPed.setCant_pickeada(0);
						detPed
								.setSust_camb_form(Constantes.SUST_CAMB_FORM_NO_EXISTE);
						sobreMargen = true;
					}
    				
    			}
                logger.debug("sobreMargen " + sobreMargen);
                // si no es uno de mis productos sobre margen aplico logica
                if(!sobreMargen){
                
	                if (detPed.getSust_camb_form() == Constantes.SUST_CAMB_FORM_EXISTE) {
	                    detPed.setCant_sinpickear(0);
	                } else {
						double cantSpick = Formatos.formatoNum3Dec(detPed
								.getCant_pedida()
								- detPed.getCant_pickeada()
	                            - detPed.getCant_faltante());
	                    cantSpick = (cantSpick < 0 ? 0 : cantSpick);
						double cantFaltMin = Formatos
								.formatoNum3Dec(detPed.getCant_pedida()
										* (double) (Constantes.PORCENTAJE_APROX_FALTANTES)
										/ 100);
	                    if (detPed.getCant_faltante() < cantFaltMin)
	                        detPed.setCant_faltante(0);
	                    if (detPed.getCant_sinpickear() < cantFaltMin)
	                        detPed.setCant_sinpickear(0);
	                }
	                
                }
                
				logger.debug("[" + i + "]cantidad pickeada: "
						+ detPed.getCant_pickeada());
				logger.debug("[" + i + "]faltante: "
						+ detPed.getCant_faltante());
				logger.debug("[" + i + "]cantidad sin pickear: "
						+ detPed.getCant_sinpickear());
                logger.debug("["+i+"]id detalle " + detPed.getId_detalle());
                logger.debug("["+i+"]id pedido " + detPed.getId_op());
                
                stm.setDouble(1, detPed.getCant_pickeada());
                stm.setDouble(2, detPed.getCant_faltante());
                stm.setDouble(3, detPed.getCant_sinpickear());
                stm.setDouble(4, detPed.getId_detalle());
                logger.debug("Ejecuto query");
                stm.executeUpdate();
                logger.debug("Fin de actualizacion");
            }
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
    }

    /**
     * Actualiza el detalle de la ronda y la marca como finalizada
     * 
     * @author jdroguett
     * @param conn
     * @param rondaId
     * @param resultadoPedidos
     * @param detallePicking
     * @param registroPicking
     * @throws SQLException
     */
	private void actualizarDetalleRondaFinalizar(Connection conn, long rondaId,
			List resultadoPedidos, List detallePicking,
			TPRegistroPickingDTO regPick, long usuarioFiscalId)
			throws SQLException {
        logger.debug("resultadoPedidos.size: " + resultadoPedidos.size());
        logger.debug("id ronda: " + rondaId);
        
        //Hastable para buscar más rápido
        Hashtable detalles = new Hashtable();
        for (int i = 0; i < detallePicking.size(); i++) {
			DetallePickingDTO detallePickingDTO = (DetallePickingDTO) detallePicking
					.get(i);
			detalles.put(new Long(detallePickingDTO.getId_detalle()),
					detallePickingDTO);
        }
        String sql = " UPDATE bo_detalle_rondas "
                + " SET cant_pick = ?, cant_faltan = ?, cant_spick = ?, sustituto = ?, mot_sustitucion = ? "
                + " WHERE id_dronda = ? ";
        
        String sqlFinalizar = " UPDATE  bo_rondas SET id_estado = ?, ffin_picking = CURRENT TIMESTAMP, " 
                + "id_usuario_fiscal = ?, e1=?, e2=?, e3=?, e4=?, e5=?, e6=?, e7=? " 
                + "WHERE id_ronda = ? ";
        PreparedStatement stm = null;
        try {
        	stm = conn.prepareStatement(sql);
            for (int i = 0; i < resultadoPedidos.size(); i++) {
				TPDetallePedidoDTO detPed = (TPDetallePedidoDTO) resultadoPedidos
						.get(i);
                stm.setDouble(1, detPed.getCant_pickeada());
                stm.setDouble(2, detPed.getCant_faltante());
                stm.setDouble(3, detPed.getCant_sinpickear());

				DetallePickingDTO detDTO = (DetallePickingDTO) detalles
						.get(new Long(detPed.getId_detalle()));
                if (detDTO != null)
                	stm.setString(4, detDTO.getSustituto());
                else
                	stm.setString(4, Constantes.DET_PICK_NORMAL);
                stm.setInt(5, detPed.getMot_sustitucion()); //raro esto
                stm.setLong(6, detPed.getId_dronda());
                stm.executeUpdate();
            }

            stm = conn.prepareStatement(sqlFinalizar);
            stm.setInt(1, Constantes.ID_ESTADO_RONDA_FINALIZADA);
            stm.setLong(2, usuarioFiscalId);
            stm.setInt(3, regPick.getE1());
            stm.setInt(4, regPick.getE2());
            stm.setInt(5, regPick.getE3());
            stm.setInt(6, regPick.getE4());
            stm.setInt(7, regPick.getE5());
            stm.setInt(8, regPick.getE6());
            stm.setInt(9, regPick.getE7());
            stm.setLong(10, rondaId);
            stm.executeUpdate();
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
    }

    /**
	 * Graba los bins y sus detalles de picking a partir de los datos
	 * descargados por la PDA a traves de WS_BOL
     * 
     * @author jdroguett
     * @param conn
     * @param binsConDetalle
     * @throws SQLException
     */
	private void grabarBinsDetallePicking(Connection conn, List binsConDetalle,
			List resultadoPedidos) throws SQLException {
		System.out
				.println("[grabarBinsDetallePicking]Ingreso al metodo grabarBinsDetallePicking");
    	String sqlBin = "INSERT INTO bo_bins_pedido "
                + "(id_ronda, id_pedido, cod_bin, cod_sello1, cod_sello2, cod_ubicacion, tipo, visualizado) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        String sqlPic = "INSERT INTO bo_detalle_picking "
                + "(id_detalle, id_bp, id_producto, id_pedido, cbarra, descripcion, precio, cant_pick, sustituto, auditado) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        PreparedStatement psBin = null;
        PreparedStatement psDet = null;
        ResultSet rs = null;

        try {
			psBin = conn.prepareStatement(sqlBin,
					Statement.RETURN_GENERATED_KEYS);
            psDet = conn.prepareStatement(sqlPic);
            for (int i = 0; i < binsConDetalle.size(); i++) {
                BinDTO bin = (BinDTO) binsConDetalle.get(i);
                psBin.setLong(1, bin.getId_ronda());
                psBin.setLong(2, bin.getId_pedido());
                psBin.setString(3, bin.getCod_bin());
                psBin.setString(4, bin.getCod_sello1());
                psBin.setString(5, bin.getCod_sello2());
                psBin.setString(6, bin.getCod_ubicacion());
                psBin.setString(7, bin.getTipo());
                psBin.setString(8, bin.getVisualizado());
                int n = psBin.executeUpdate();
                logger.debug("inserto?" + n);
                rs = psBin.getGeneratedKeys();
                long binId = 0;
                if (rs.next()) {
                    binId = rs.getLong(1);
                }
                logger.debug("id del nuevo bin : " + binId);
                if (binId != 0) {
                    List detallePicking = bin.getDetallePicking();
					logger.debug("grabando detallepicking size: "
							+ detallePicking.size());
                    logger.debug("Recooro el detalle de picking");
                    for (int j = 0; j < detallePicking.size(); j++) {
						DetallePickingDTO det = (DetallePickingDTO) detallePicking
								.get(j);
                        // recupero el detalle de la ronda 
						DetalleRondaDTO rondaDTO = getDetalleRondaByIdAndDetalle(
								bin.getId_ronda(), det.getId_detalle());
						// Logica solicitada para evaluar si un producto es
						// sustituto
                        boolean flujoNormal = true;
						logger.debug("Valido si el producto es sustituto: "
								+ det.getSustituto());
						if (det.getSustituto() != null
								&& det.getSustituto().trim().toUpperCase()
										.equals("S")) {
                        	// En caso de ser un producto sustituto
							// valido que el precio del producto sustituto no
							// sea superior al precio del producto original *
							// 1,5
                        	flujoNormal = false;
							// recupero la información original del producto
							// solicitado por el id de detalle
							logger
									.debug("Realizo la llamada a getProductoPedido para recuperar el producto pedido orignalmente");
							logger.debug("Envio id detalle : "
									+ det.getId_detalle());
							logger
									.debug("para recuperar producto de id de pedido : "
											+ det.getId_pedido());
							ProductosPedidoDTO productosPedidos = getProductoPedido(
									conn, det.getId_detalle());
							logger.debug("Recupero producto "
									+ productosPedidos.getId_producto());
							logger.debug("descripcion "
									+ productosPedidos.getDescripcion());
							// si tiene cambio de formato mantengo la cantidad
							// solicitada
                        	double cantidad_sol_x_ronda = 0;
							if (isProdCambioFormato(resultadoPedidos, det
									.getId_detalle())) {
								cantidad_sol_x_ronda = rondaDTO.getCantidad(); // la
																			   // cantidad
																			   // del
																			   // producto
																			   // solicitado
																			   // para
																			   // esta
																			   // ronda
                        	}else{
								// si no tiene cambio de formato, dejare como la
								// cantidad solicitada la cantidad pickeada
								// esto en caso de que pickeen de manera
								// separada la cantidad del producto y no
								// obtener el resultado esperado
                        		cantidad_sol_x_ronda = det.getCant_pick();
                        	}
							double precio_unidad_sol = productosPedidos
									.getPrecio();
							double precio_final_sol = cantidad_sol_x_ronda
									* precio_unidad_sol;
							logger
									.debug("Recupere producto original solicitado");
							logger.debug("Cantidad solicitado para ronda: "
									+ cantidad_sol_x_ronda);
							logger
									.debug("Precio por unidad del producto solicitado "
											+ precio_unidad_sol);
							logger
									.debug("Precio final del producto solicitado segun cantidad "
											+ precio_final_sol);
                        	// recupero la información del producto sustituto
							double dscto_item = productosPedidos
									.getDscto_item() / 100;
                        	double cantidad_pick_sust = det.getCant_pick();
							double precio_unidad_sust = det.getPrecio()
									- (det.getPrecio() * dscto_item);
							double precio_final_sust = cantidad_pick_sust
									* precio_unidad_sust;
                        	double porcentaje_umbral = Constantes.UMBRAL_PRECIO_SUSTITUTO;
							logger.debug("Descuento del producto aplicado "
									+ dscto_item);
							logger
									.debug("Recupero los valores del producto sustituto pickeado");
							logger.debug("Cantidad picked del sustituto "
									+ cantidad_pick_sust);
							logger
									.debug("Precio por unidad del producto sustituto "
											+ precio_unidad_sust);
							logger
									.debug("Precio final del producto sustituto segun cantidad"
											+ precio_final_sust);
							logger
									.debug("Recupero porcentaje umbral de bloqueo "
											+ porcentaje_umbral);
							// valido si el precio del producto sustituto es
							// menor al precio del producto orignal
                        	if(precio_final_sust <= precio_final_sol){
								// en caso de ser menor el precio del producto
								// sustituto que el original
								// mantengo el precio del producto sustituto,
								// por lo cual vuelvo al flujo normal
                        		flujoNormal = true;
                        	}else{
								// en caso de que el precio del producto
								// sustituto sea mayor al precio del producto
								// original
								// valido que el precio del producto sustituto
								// sea menor o igual al umbral de sustitución
                        		if(precio_final_sust  <= (precio_final_sol * (1 + porcentaje_umbral))){
									logger
											.debug("Producto sustituto bajo umbral");
									// en caso de que el precio del producto
									// sustituto sea menor o igual al umbral
									// calculo el precio unitario por cada
									// unidad del producto sustituto
									double nuevo_precio_unidad = Math
											.floor((cantidad_sol_x_ronda * productosPedidos
													.getPrecio_lista())
													/ cantidad_pick_sust);
									// finalmente seteo el valor del precio del
									// detalle que sera insertado en el detalle
									// de picking
									logger
											.debug("Obtengo el precio por unidad del producto sustituto "
													+ nuevo_precio_unidad);
                        			det.setPrecio(nuevo_precio_unidad);
									// finalmente marco como flujo normal para
									// realizar la insercion
                        			flujoNormal = true;
                        		}else{
									logger
											.debug("Producto sustituto supera umbral");
									// realizo la insercion del producto
									// sustituto al reporte de sustitución sobre
									// umbral
									// marco el flag flujoNormal como falso ya
									// que no se insertará en el detalle de
									// picking
                        			flujoNormal = false;
                        			insertaProductoSobreUmbral(conn, det,bin);
									logger
											.debug("Inserto el id de detalle a la lista"
													+ det.getId_detalle());
									detallesPedidosSobreUmbral.add(new Long(det
											.getId_detalle()));
                        		}
                        	}
                        	
                        }
                        // consulto si sigo por el flujo normal para insertar
						// en caso de que no sea asi es porque es un producto
						// sustituto de umbral de sustitución mayor al definido
						// (50% mayor al precio original)
                        if(flujoNormal){
                        	psDet.setLong(1, det.getId_detalle());
                        	psDet.setLong(2, binId);
                        	if (det.getId_producto() > 0) {
                        		psDet.setLong(3, det.getId_producto());
                        	} else {
                        		psDet.setNull(3, Types.BIGINT);
                        	}
                        	psDet.setLong(4, det.getId_pedido());
                        	psDet.setString(5, det.getCBarra());
                        	psDet.setString(6, det.getDescripcion());
                        	psDet.setDouble(7, det.getPrecio());
                        	psDet.setDouble(8, det.getCant_pick());
                        	psDet.setString(9, det.getSustituto());
                        	psDet.setString(10, det.getAuditado());
							logger.debug("ID detalle:" + det.getId_detalle()
									+ ", Descripcion:" + det.getDescripcion()
									+ ", Precio:" + det.getPrecio());
                        	psDet.executeUpdate();
                        }
                    }
                }
            }
        }catch (Exception e) {
			logger.debug("Error, detalle " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (psBin != null)
					psBin.close();
				if (psDet != null)
					psDet.close();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				psBin=null;
				psDet=null;

			}
		}
    }
    
    /**
	 * Metodo que se encarga de recupera el producto solicitado originalmente
	 * por el cliente, esto a través de su id de detalle, esto se utilizará
	 * inicialmente para realizar el calculo de umbral de sustitución en caso de
	 * que exista para respetar el precio original del producto solicitado.
     * 
	 * @param idDetalle
	 *            id del detalle que recuperará el producto pedido originalmente
	 *            por el cliente
	 * @return <code>ProductosPedidoDTO</code> DTO con el producto original
	 *         que se solicito para la compra
     */
	private ProductosPedidoDTO getProductoPedido(Connection conn, long idDetalle)
			throws SQLException {
		logger.debug("Ingreso al metodo getProductoPedido con el idDetalle "
				+ idDetalle);
    	ProductosPedidoDTO prod = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		try {
			logger.debug("Preparo el SQL para recuperar el producto pedido");
			
			String sql = " SELECT id_detalle, id_pedido, id_sector, id_producto, cod_prod1, uni_med, "
					+ " descripcion, cant_solic, observacion, preparable, con_nota, pesable, cant_pick, cant_faltan, cant_spick, "
					+ " precio_lista, precio , dscto_item"
					+ " FROM bo_detalle_pedido " + " WHERE ID_DETALLE = ? ";
			
			logger.debug("Preparo la conexión con el SQL formado");
			stm = conn.prepareStatement(sql + " WITH UR");
							
			logger.debug("Seteo el valor de idDetalle");
			stm.setLong(1,idDetalle);
			
			logger.debug("SQL a eviar: " + sql);
			
			logger.debug("Realizo la ejecución del SQL");
			rs = stm.executeQuery();
			
			logger.debug("Obtengo el resultado y lo recorro");
			while (rs.next()) {
				prod = new ProductosPedidoDTO();
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
				prod.setPrecio(rs.getDouble("precio"));
				prod.setDscto_item(rs.getDouble("dscto_item"));
				prod.setPrecio_lista(rs.getDouble("precio_lista"));
				
				logger.debug("Obtengo el producto original: "
						+ prod.getId_producto());
			}
			
			logger.debug("Cierro las conexiones");
		}catch (SQLException e) {
			logger.debug("Error en la ejecución " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				//releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}

		return prod;
    } // fin de metodo getProductoPedido
    
    /**
	 * Metodo que se encarga de realizar la inserción en la tabla
	 * BO_SUSTITUTO_SOBRE_UMBRAL creada para almacenar los productos sustitutos
	 * que sobrepasan el umbral de sustitución y que quedarán como pendientes,
	 * estos datos serán mostrados posteriormente en un reporte para poder
	 * extraer los productos con sobre umbral de los bins.
	 * 
	 * @param conn
	 *            coneccion establecida para realizar la insercion
	 * @param det
	 *            Detalle de picking con producto sobre umbral para ser
	 *            insertado y mostrado en el reporte
     * @throws SQLException
     */
	private void insertaProductoSobreUmbral(Connection conn,
			DetallePickingDTO det, BinDTO bin) throws SQLException {
		logger
				.debug("Ingreso al metodo insertaProductoSobreUmbral con el detale "
						+ det.getId_detalle());
		logger
				.debug("Ingreso al metodo insertaProductoSobreUmbral con el pedido "
						+ det.getId_pedido());
		logger
				.debug("Ingreso al metodo insertaProductoSobreUmbral con el cod_bin "
						+ bin.getCod_bin());
		PreparedStatement stm=null;
		try {
			logger.debug("Se recupera cod_bin " + bin.getCod_bin());
			String cod_bin = "";
			String tipo_bin = "";
			
			cod_bin = bin.getCod_bin();
			tipo_bin = bin.getTipo();
			logger.debug("Se recupera tipo bin " + bin.getTipo());
			if(tipo_bin != null && "V".equals(tipo_bin)){
				cod_bin = "VIRTUAL";
			}
			if(cod_bin == null || "".equals(cod_bin)){
				cod_bin = " ";
			}
			logger.debug("Preparo el SQL para recuperar el producto pedido");
			long id_detalle = det.getId_detalle();	
			String descripcion = det.getDescripcion();
			long id_pedido = det.getId_pedido();
			long id_producto = det.getId_producto();
			long id_ronda = bin.getId_ronda();
			String sql = "INSERT INTO BODBA.BO_SUSTITUTO_SOBRE_UMBRAL "
				+" (FECHA_OPERACION			,ID_DETALLE,  ID_RONDA	  , ID_LOCAL, PICKEADOR_NOMBRE, PICKEADOR_APELLIDO, DESCRIPCION_PRODUCTO, ID_PRODUCTO ,COD_BIN,ID_PEDIDO)"
					+ " (SELECT CURRENT TIMESTAMP, "
					+ id_detalle
					+ ", BR.ID_RONDA , BR.ID_LOCAL , USR.NOMBRE	, USR.APELLIDO		, '"
					+ descripcion
					+ "'  ,	"
					+ id_producto
					+ "	,'"
					+ cod_bin
					+ "',	"
					+ id_pedido
					+ ""
					+ " FROM BO_RONDAS BR "
					+ " INNER JOIN BO_USUARIOS USR ON  USR.ID_USUARIO = BR.ID_USUARIO  "
					+ " WHERE BR.ID_RONDA = " + id_ronda + ")";
			logger.debug("preparo el sql: " + sql);
	    	stm = conn.prepareStatement(sql);
	    	logger.debug("preparo parametros");
	    	logger.debug("id_detalle: " + det.getId_detalle());
	    	logger.debug("descripcion: " + det.getDescripcion());
	    	logger.debug("id_producto: " + det.getId_producto());
	    	logger.debug("cod_bin: " + cod_bin);
	    	logger.debug("id_pedido: " + det.getId_pedido());
            logger.debug("ID_RONDA: " + bin.getId_ronda());
            logger.debug("executo la actualización del sql");
            stm.executeUpdate();
            logger.debug("Fin de la inserción");
		}catch (Exception e) {
			logger.debug("Error en la ejecución " + e.getMessage());
		} finally {
			try {
				if (stm != null)
					stm.close();
				//releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				
			}
		}
    	
    }// fin metodo insertaProductoSobreUmbral
    
	private boolean isProdCambioFormato(List resultadoPedidos,
			long idDetallePedido) {
    	boolean resultado = false;
    	// Hastable para buscar más rápido
        Hashtable detalles = new Hashtable();
        if(resultadoPedidos != null){
	        for (int i = 0; i < resultadoPedidos.size(); i++) {
				TPDetallePedidoDTO dtPed = (TPDetallePedidoDTO) resultadoPedidos
						.get(i);
	            detalles.put(new Long(dtPed.getId_detalle()), dtPed);
	        }
        }
        
		TPDetallePedidoDTO detPed = (TPDetallePedidoDTO) detalles.get(new Long(
				idDetallePedido));
        if(detPed != null){
        	if (detPed.getSust_camb_form() == Constantes.SUST_CAMB_FORM_EXISTE) {
        		resultado = true;
            } else {
            	resultado = false;
            }
        }
        
    	return resultado;
    }// fin metodo isProdCambioFormato
    
    
    
    /**
	 * Metodo que se encarga de recuperrar el detalle especifico de una ronda
	 * para un producto
	 * 
	 * @param id_ronda
	 *            id de la ronda a buscar
	 * @param id_detalle
	 *            id del detalle de pedido a buscar
     * @return	<code>DetalleRondaDTO</code> objeto con el detalle de ronda.
     * @throws RondasDAOException
     */
	public DetalleRondaDTO getDetalleRondaByIdAndDetalle(long id_ronda,
			long id_detalle) throws RondasDAOException {
		System.out.println("Ingreso al getDetalleRondaByIdAndDetalle");
		System.out.println("Parametros de entrada id_ronda " + id_ronda
				+ " id_detalle " + id_detalle);
    	PreparedStatement stm = null;		
		ResultSet rs = null;
		DetalleRondaDTO ron = null;
		
		logger.debug("Preparo sql");
		String SQLStmt = "SELECT DBR.ID_DRONDA, " + "		DBR.ID_DETALLE,"
				+ "		DBR.ID_RONDA, " + "		DBR.ID_PEDIDO, " + "		DBR.CANTIDAD, "
				+ "		DBR.CANT_PICK, " + "		DBR.CANT_FALTAN, "
				+ "		DBR.CANT_SPICK, " + "		DBR.SUSTITUTO, "
				+ "		DBR.MOT_SUSTITUCION"
				+ " 		FROM BODBA.BO_DETALLE_RONDAS DBR"
				+ "		WHERE DBR.ID_RONDA = ?" + "		AND DBR.ID_DETALLE = ?";
		
		
		try {

			//conn = this.getConnection();
			System.out.println("preparo sql " + SQLStmt);
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1, id_ronda);
			stm.setLong(2, id_detalle);
			System.out.println("Executo query");
			rs = stm.executeQuery();
			System.out.println("Recupere resultados");
			while (rs.next()) {
				ron = new DetalleRondaDTO();
				ron.setId_dronda(rs.getLong("ID_DRONDA"));
				ron.setId_detalle(rs.getLong("ID_DETALLE"));
				ron.setId_ronda(rs.getLong("ID_RONDA"));
				ron.setId_pedido(rs.getLong("ID_PEDIDO"));
				ron.setCantidad(rs.getDouble("CANTIDAD"));
				ron.setCant_pick(rs.getDouble("CANT_PICK"));
				ron.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				ron.setCant_spick(rs.getDouble("CANT_SPICK"));
				ron.setSustituto(rs.getString("SUSTITUTO"));
				ron.setMot_sustitucion(rs.getInt("MOT_SUSTITUCION"));
			}
			logger.debug("Recupere detalle de ronda  " + ron.getId_dronda());
			logger.debug("Cierro conexiones");

		}catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				//releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		logger.debug("Retorno ron");
		return ron;
	}
    

    /**
     * @param ids
     * @return Formato: "(123,43234,454,56456)"
     */
    private String join(List list) {
        StringBuffer lista = new StringBuffer("(");
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object elemento = iter.next();
            lista.append(elemento.toString());
            if (iter.hasNext())
                lista.append(",");
        }
        lista.append(")");
        return lista.toString();
    }

    /**
     * @param rondaId
     * @return
     * @throws SQLException
     * @throws RondasDAOException
     */
    public boolean rondaTieneBins(long rondaId) throws RondasDAOException {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
			String sql = "select count(*) from bodba.bo_bins_pedido where id_ronda = "
					+ rondaId;
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            if(rs.next()){
                long n = rs.getLong(1);
                if(n > 0)
                    return true;
            }
        }catch(SQLException e){
            throw new RondasDAOException(e);
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
        return false;
    }
    

    /**
     * Marca los pedidos terminado con estado en bodega. Un pedido terminado
     * tiene todas sus rondas finalizadas y no posee productos sin pickear.
     * 
     * @author jdroguett
     * @param conn
     * @param resultadoPedidos
     * @throws SQLException
     */
	public void insetaAuditoriaSustitutos(TPAuditSustitucionDTO AudSust)
			throws SQLException {

        String SQL = "";

        SQL = "INSERT INTO BODBA.BO_AUDITORIA_SUSTITUCION( "
            + "   ID_RONDA, ID_DRONDA, ID_PEDIDO, ID_DETALLE, "
            + "   ID_USUARIO, NUM_AUDITORIA, COD_BARRA_ORIG, "
            + "   COD_BARRA_SUST, CANTIDAD_SUST, PRECIO_SUST, "
				+ "   UNI_MED_SUST, ACCION, FECHA) " + "VALUES "
            + "   (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT TIMESTAMP)";
        
        logger.debug("insetaAuditoriaSustitutos: " + SQL);
        
        conn = this.getConnection();
        conn.setAutoCommit(true);
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(SQL);
            stm.setLong  ( 1, AudSust.getId_ronda());
            stm.setLong  ( 2, AudSust.getId_dronda());
            stm.setLong  ( 3, AudSust.getId_pedido());
            stm.setLong  ( 4, AudSust.getId_detalle());
            stm.setLong  ( 5, AudSust.getId_usuario());
            stm.setLong  ( 6, AudSust.getId_auditoria());
            stm.setString( 7, AudSust.getCod_barra_orig());
            stm.setString( 8, AudSust.getCod_barra_sust());
            stm.setInt   ( 9, AudSust.getCantidad_sust());
            stm.setDouble(10, AudSust.getPrecio_sust());
            stm.setString(11, AudSust.getUnid_med_sust());
            stm.setString(12, AudSust.getAccion());
            //AudSust.getId();

            int NumRegUpd = stm.executeUpdate();
            
            logger.debug("insetaAuditoriaSustitutos (NumRegUpd): " + NumRegUpd);
            
        } catch(SQLException e){
            logger.error("insetaAuditoriaSustitutos: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (stm != null)
                    stm.close();
            } catch (SQLException e) {
				logger
						.error(
								"[insetaAuditoriaSustitutos] : xxx - Problema SQL (close)",
								e);
            }finally{
                stm=null;
}
        }
    }
    

    /**
     * Marca los pedidos terminado con estado en bodega. Un pedido terminado
     * tiene todas sus rondas finalizadas y no posee productos sin pickear.
     * 
     * @author jdroguett
     * @param conn
     * @param resultadoPedidos
     * @throws SQLException
     */
	public void marcaRondaAuditoriaSustitucion(int id_ronda)
			throws SQLException {

        String SQL = "";

        SQL = "UPDATE BODBA.BO_RONDAS "
            + "   SET ESTADO_AUDIT_SUSTITUCION = 'S' "
            + "WHERE ID_RONDA = " + id_ronda;
        
        logger.debug("marcaRondaAuditoriaSustitucion: " + SQL);
        
        conn = this.getConnection();
        conn.setAutoCommit(true);
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(SQL);
            int NumRegUpd = stm.executeUpdate();
            
			logger.debug("marcaRondaAuditoriaSustitucion (NumRegUpd): "
					+ NumRegUpd);
            
        } catch(SQLException e){
            logger.error("marcaRondaAuditoriaSustitucion: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (stm != null)
                    stm.close();
            } catch (SQLException e) {
				logger
						.error(
								"[marcaRondaAuditoriaSustitucion] : xxx - Problema SQL (close)",
								e);
            }finally{
                stm=null;
            }
        }
    }
        
	//  ** @Inicio by Indra
	/**
	 * 
	 * 
	 * @param id_estado
	 *            long
	 * @param id_ronda
	 *            long
	 * 
	 * @throws RondasDAOException
	 */
	public void updatePorcPedido(String ids, double porc_unidad,
			double porc_mmonto) throws RondasDAOException {

		logger.debug("En  updatePorcPedido");
		ids = ids.replace('"', ' ');
		ids = ids.replaceAll(" ", "");

		PreparedStatement stm = null;
		String SQLStmt = " UPDATE  BO_PEDIDOS_PORCENT "
				+ " SET PORC_UNIDAD = ? , PORC_MONTO = ? "
				+ " WHERE ID_PEDIDO = ? ";

		logger.debug("SQL: " + SQLStmt);

		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(SQLStmt);

			stm.setDouble(1, porc_unidad);
			stm.setDouble(2, porc_mmonto);
			stm.setString(3, ids);

			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();

			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			} finally {

				stm = null;

}
		}
	}
	public long consultaPorcPedidos(String ids) throws RondasDAOException {
		logger.debug("en consultaPorcPedidos");
		long id_pedido = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT ID_PEDIDO FROM BODBA.BO_PEDIDOS_PORCENT WHERE ID_PEDIDO = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, ids);

			logger.debug("SQL: " + sql);

			rs = stm.executeQuery();
			if (rs.next()) {
				id_pedido = rs.getLong("ID_PEDIDO");
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();

			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			} finally {
				rs = null;
				stm = null;

			}
		}
		logger.debug("id_pedido: " + id_pedido);
		return id_pedido;
	}

	/**
	 * @author jdroguett
	 * @param conn
	 * @throws SQLException
	 * @throws SQLException
	 */
	private void insertarPorcPedido(String ids, double porc_unidad,
			double porc_mmonto) throws RondasDAOException, SQLException {

		logger.debug("En insertarPorcPedido");
		PreparedStatement stm = null;

		String sql = "INSERT INTO BO_PEDIDOS_PORCENT (ID_PEDIDO, PORC_UNIDAD, PORC_MONTO) "
				+ "VALUES (?, ?, ?)";

		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, ids);
			stm.setDouble(2, porc_unidad);
			stm.setDouble(3, porc_mmonto);

			stm.executeUpdate();
		} finally {
			try {
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger
						.error(
								"[Metodo insertarPorcPedido] : xxx - Problema SQL (close)",
								e);
			} finally {
				stm = null;
			}
		}

	}

	/**
	 * Obtiene cantidad de umbrales de la tabla para ver si se pasa el pedido a
	 * bodega o no
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getAvanceUmbralParametros(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = null;
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En getAvanceUmbralParametros");

		try {

			//Consulta con la nueva tabla bo_umbrales

			String sql = "  SELECT UMB.U_MONTO AS MONTO, UMB.U_UNIDAD AS UNIDAD "
					+ " FROM BO_UMBRALES UMB , BO_PEDIDOS PED "
					+ " WHERE UMB.ID_LOCAL = PED.ID_LOCAL "
					+ " AND PED.ID_PEDIDO =? " + " AND UMB.U_ACTIVACION ='S' ";
			logger.debug("Query  para ejecutar:  " + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			double total_unidad = 0d;
			double total_monto = 0d;

			while (rs.next()) {

				AvanceDTO umb = new AvanceDTO();
				umb.setCant_monto_umbrales(rs.getLong("MONTO"));
				umb.setCant_unidad_umbrales(rs.getLong("UNIDAD"));
				result.add(umb);

			}

			det = new AvanceDTO();
			det.setCant_unidad_umbrales(total_unidad);
			det.setCant_monto_umbrales(total_monto);

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
		return result;
	}

	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public long getAvanceUmbralMonto(long id_pedido) throws PedidosDAOException {

		AvanceDTO det = null;
		long precio_total = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : getAvanceUmbralMonto");

		try {

			String sql = "SELECT SUM(ROUND(precio*cant_solic,0)) AS tot_det "
					+ "FROM bodba.bo_detalle_pedido dp "
					+ "WHERE dp.id_pedido = ? ";

			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				precio_total += rs.getDouble("tot_det");

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
		return precio_total;
	}

	/**
	 * Obtiene si el pedido tiene mas rondas pendientes
	 *  
	 */

	public boolean pedidoTieneMasrondas(long id_pedido) throws PedidosDAOException {

		AvanceDTO det = null;
		boolean masRondas;
		long ID_PEDIDO_REC = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : getCantidadSinPick");

		try {

			String sql ="select id_pedido from bo_pedidos where id_pedido in  ("
			+ id_pedido+")"
			+ " except "
			+ " ((select id_pedido from bo_rondas r inner join bo_detalle_rondas dr on dr.id_ronda = r.id_ronda "
			+ " where id_pedido in ("
			+ id_pedido+")"
			+ " and r.id_estado != "
			+ Constantes.ID_ESTADO_RONDA_FINALIZADA
			+ " )"
			+ " union (select id_pedido from bo_detalle_pedido where id_pedido in ("
			+ id_pedido+")"
			+ " group by id_pedido having sum(cant_spick)  > 0))";

			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();

			while (rs.next()) {

				ID_PEDIDO_REC += rs.getDouble("ID_PEDIDO");
				System.out.println("ID_PEDDIO " + ID_PEDIDO_REC);

			}
			
			
			if (ID_PEDIDO_REC > 0)
			{
				masRondas = false;
			}
			else
			{
				masRondas = true;
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
				logger.error(
						"[Metodo] : pedidoTieneMasrondas - Problema SQL (close)",
						e);
			}
		}
		return masRondas;
	}

	
	/**
	 * 
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	
	
	/**
	 * Obtiene si el pedido tiene mas rondas pendientes
	 *  
	 */

	public boolean umbralActivado(long id_pedido) throws PedidosDAOException {
		String activacion = "";
		AvanceDTO det = null;
		boolean umbralActivado;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : umbralActivado");

		try {

			String sql = "SELECT U_ACTIVACION FROM BO_UMBRALES WHERE ID_LOCAL = (SELECT ID_LOCAL FROM BO_PEDIDOS WHERE ID_PEDIDO = ?)";
			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {

				activacion += rs.getString("U_ACTIVACION");

			}
		
			if (activacion.equals("S"))
			{
				umbralActivado = true;
			}
			else{
				
				umbralActivado = false;
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
				logger.error(
						"[Metodo] : umbralActivado - Problema SQL (close)",
						e);
			}
		}
		return umbralActivado;
	}
//
	
	
	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public long getAvanceUmbralMontoSinSust(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = null;
		long precio_total = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : getAvanceUmbralMonto");

		try {

			/**
			 * String sql = "SELECT SUM(ROUND(precio*cant_solic,0)) AS tot_det " +
			 * "FROM bodba.bo_detalle_pedido dp " + "WHERE dp.id_pedido = ? AND
			 * dp.CANT_PICK = dp.CANT_SOLIC " ;
			 */
			//	String sql = " SELECT
			// COALESCE(SUM(ROUND(dE.precio*cant_PICK,0)),0) AS tot_det FROM
			// BODBA.BO_DETALLE_PICKING de WHERE dE.id_pedido = ? AND
			// DE.SUSTITUTO='N'";

			String sql = "SELECT COALESCE(SUM(ROUND(DE.PRECIO*DE.cant_PICK,0)),0) AS tot_det "
					+ "FROM  BODBA.BO_DETALLE_PICKING DE  "
					+ " WHERE DE.ID_PEDIDO = ?";

			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				precio_total += rs.getDouble("tot_det");

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
		return precio_total;
	}

	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public long getAvanceUmbralMontoConSust(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = null;
		long precioTotalConSust = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : getAvanceUmbralMonto");

		try {

			/**
			 * String sql = " SELECT
			 * COALESCE(SUM(ROUND((de.precio*de.cant_pick),0)),0) AS tot_det " + "
			 * FROM bodba.bo_detalle_pedido dp , BODBA.BO_DETALLE_PICKING de " + "
			 * WHERE dp.id_pedido = ? AND dp.CANT_PICK <>dp.CANT_SOLIC and
			 * de.ID_DETALLE = dp.id_detalle " ;
			 */

			String sql = "SELECT SUM(ROUND(dE.precio*cant_PICK,0)) AS tot_det FROM  BODBA.BO_DETALLE_PICKING de 	WHERE dE.id_pedido = ?  AND DE.SUSTITUTO='S' ";
			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				precioTotalConSust += rs.getDouble("tot_det");
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
		return precioTotalConSust;
	}

	/**
	 * Obtiene cantidad de productos solicitados por pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public long getTotalProductoSolicitado(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = null;
		long total_productos = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En : getTotalProductoSolicitado");

		try {

			String sql = " SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC "
					+ " FROM BODBA.BO_DETALLE_PEDIDO DP "
					+ " WHERE DP.ID_PEDIDO = ? ";
			logger.debug("Query para ejecutar :" + sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				total_productos += rs.getDouble("CAT_SOLIC");
				System.out.println("Cat solicitada : " + total_productos);
				logger.debug("Cat solicitada : " + total_productos);
				;

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
		return total_productos;
	}

	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getAvanceUmbralPedidoSinSust(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = new AvanceDTO();
		AvanceDTO umb = new AvanceDTO();
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En :" + this.getClass());

		try {

			/**
			 * String sql = " SELECT " + " COALESCE(SUM(DP.CANT_PICK), 0) AS
			 * CANT_PICK " + " FROM BODBA.BO_DETALLE_PEDIDO DP " + " WHERE
			 * DP.ID_PEDIDO = ? AND DP.CANT_SOLIC = DP.CANT_PICK ";
			 */

			//String sql = "SELECT COALESCE(SUM(DE.CANT_PICK), 0) AS CANT_PICK
			// FROM BODBA.BO_DETALLE_PICKING de WHERE DE.ID_PEDIDO = ? AND
			// DE.SUSTITUTO='N'";
			String sql = "SELECT COALESCE(SUM(CANT_PICK), 0) AS CANT_PICK "
					+ " FROM BO_DETALLE_PEDIDO " + " WHERE ID_PEDIDO = ? "
					+ " AND CANT_FALTAN <> 0  " + " AND CANT_SPICK = 0 ";

			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			logger.debug(sql);
			rs = stm.executeQuery();

			while (rs.next()) {

				umb.setCant_prod_en_bodega(rs.getDouble("CANT_PICK"));

				result.add(umb);
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
		return result;
	}

	//** @Fin by Indra
	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List X(long id_pedido) throws PedidosDAOException {

		AvanceDTO det = new AvanceDTO();
		AvanceDTO umb = new AvanceDTO();
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En :" + this.getClass());

		try {

			/**
			 * String sql = " SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC " + " FROM
			 * BODBA.BO_DETALLE_PEDIDO DP , BODBA.BO_DETALLE_PICKING DE " + "
			 * WHERE DP.ID_PEDIDO = ? AND DP.ID_DETALLE = DE.ID_DETALLE AND
			 * DP.CANT_SOLIC <>DP.CANT_PICK ";
			 */

			// Con Cambio Normal y Formato cuando todos estos fueron pickeados o
			// remp,azados en caso de cambio formato
			/**
			 * String sql = "SELECT COALESCE(SUM(PE.CANT_SOLIC), 0) AS CAT_SOLIC " + "
			 * FROM BODBA.BO_DETALLE_PICKING de , BODBA.BO_DETALLE_PEDIDO PE " + "
			 * WHERE DE.ID_PEDIDO = ? AND DE.SUSTITUTO='S' " + " AND
			 * PE.ID_PEDIDO=DE.ID_PEDIDO" + " AND PE.CANT_FALTAN = 0 AND
			 * PE.ID_DETALLE = DE.ID_DETALLE" ;
			 */

			String sql = "SELECT COALESCE(SUM(CANT_SOLIC), 0) AS CAT_SOLIC "
					+ " FROM BO_DETALLE_PEDIDO " + " WHERE ID_PEDIDO = ? "
					+ " AND CANT_FALTAN = 0  " + " AND CANT_SPICK = 0 ";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				umb.setCant_prod_en_bodega(rs.getDouble("CAT_SOLIC"));

				result.add(umb);
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
		return result;
	}

	/**
	 * Obtiene cantidad de productos por pedido segun estado de ronda
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_sector
	 *            long
	 * @param id_local
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getAvanceUmbralPedidoConSustNormal(long id_pedido)
			throws PedidosDAOException {

		AvanceDTO det = new AvanceDTO();
		AvanceDTO umb = new AvanceDTO();
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("En :" + this.getClass());

		try {

			/**
			 * String sql = " SELECT SUM(DP.CANT_SOLIC) AS CAT_SOLIC " + " FROM
			 * BODBA.BO_DETALLE_PEDIDO DP , BODBA.BO_DETALLE_PICKING DE " + "
			 * WHERE DP.ID_PEDIDO = ? AND DP.ID_DETALLE = DE.ID_DETALLE AND
			 * DP.CANT_SOLIC <>DP.CANT_PICK ";
			 */

			String sql = "SELECT COALESCE(SUM(CANT_SOLIC), 0) AS CAT_SOLIC "
					+ " FROM BO_DETALLE_PEDIDO " + " WHERE ID_PEDIDO = ? "
					+ " AND CANT_FALTAN = 0  " + " AND CANT_SPICK = 0 ";
			logger.debug(sql);
			logger.debug("id_pedido:" + id_pedido);

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			while (rs.next()) {

				umb.setCant_prod_en_bodega(rs.getDouble("CAT_SOLIC"));
				logger.debug("EN getAvanceUmbralPedidoConSustNormal"
						+ umb.getPorc_prod_en_bodega());
				result.add(umb);
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
		return result;
		//indra
	}
}
