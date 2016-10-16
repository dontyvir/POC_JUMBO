package cl.bbr.jumbocl.eventos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cl.bbr.jumbocl.contenidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.dto.PasoDTO;
import cl.bbr.jumbocl.eventos.dto.RutEventoDTO;
import cl.bbr.jumbocl.eventos.dto.TipoEventoDTO;
import cl.bbr.jumbocl.eventos.exceptions.EventosDAOException;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Clase que permite consultar los Casos que se encuentran en la base de datos.
 * @author imoyano
 *
 */
public class JdbcEventosDAO implements EventosDAO {
	
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
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws EventosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new EventosDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcEventosDAO(){
		
	}
	
	/**
	 * Lista casos por criterio
	 * El cirterio esta dado por :CasosCriterioDTO
	 * 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
	public List getEventosByCriterio(EventosCriterioDTO criterio) throws EventosDAOException {
	    
		List listaEventos 		= new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		String order 			= "";
		
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		//logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if (pag <= 0) { 
		    pag = 1;
		}
		if (regXpag < 10) {
		    regXpag = 10;
		}
		int iniReg = (pag - 1) * regXpag + 1;
		int finReg = pag * regXpag;
		
		
		// Orden de la Query
		order = " E.ID_EVENTO DESC ";
		
		String Sql = " SELECT * FROM ( " +
					 " SELECT row_number() over( ORDER BY " + order + "  ) as row, " +
					 " E.ID_EVENTO, E.FECHA_CREACION_EVENTO, E.FECHA_MODIFICACION_EVENTO, E.ID_USUARIO_EDITOR, " +
					 " E.ID_USUARIO_CREADOR, E.NOMBRE, E.DESCRIPCION, E.ID_TIPO_EVENTO, E.FECHA_INICIO, E.FECHA_FIN, " +
					 " E.OCURRENCIA, E.NOMBRE_ARCHIVO, E.ORDEN, E.ACTIVO, T.NOMBRE TIPO_EVENTO, " +
					 " CASE WHEN COUNT (R.CLI_RUT) IS NULL THEN 0 ELSE COUNT (R.CLI_RUT) END RUTS_TOTAL, " +
					 " CASE WHEN SUM(R.OCURRENCIA_POR_RUT) IS NULL THEN 0 ELSE SUM(R.OCURRENCIA_POR_RUT) END RUTS_EVENTO " +
					 " FROM EP_EVENTO E " +
					 " inner join EP_TIPO_EVENTO T on (E.ID_TIPO_EVENTO = T.ID_TIPO_EVENTO) " +
					 " left outer join EP_RUT_EVENTOS R on (E.ID_EVENTO = R.ID_EVENTO) ";
		
		Sql += obtenerWhereEventos(criterio);
		
		Sql +=		 " GROUP BY E.ID_EVENTO, E.FECHA_CREACION_EVENTO, E.FECHA_MODIFICACION_EVENTO, E.ID_USUARIO_EDITOR, " +
					 " E.ID_USUARIO_CREADOR, E.NOMBRE, E.DESCRIPCION, E.ID_TIPO_EVENTO, E.FECHA_INICIO, E.FECHA_FIN, " +
					 " E.OCURRENCIA, E.NOMBRE_ARCHIVO, E.ORDEN, E.ACTIVO, T.NOMBRE " +
					 " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
		
		logger.debug("* SQL de eventos con criterio :"+Sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				EventoDTO evento = new EventoDTO();
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				tipo.setNombre(rs.getString("tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setOrden(rs.getLong("orden"));
				evento.setActivo(rs.getString("activo"));
				evento.setRutsTotal(rs.getLong("ruts_total"));
				evento.setRutsEvento(rs.getLong("ruts_evento"));
				listaEventos.add(evento);
			}

		}catch (SQLException e) {
			logger.error(String.valueOf(e.getErrorCode()),e);
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventosByCriterio - Problema SQL (close)", e);
			}
		}
		return listaEventos;
	}

    /**
     * @param criterio
     * @return
     */
    private String obtenerWhereEventos(EventosCriterioDTO criterio) {
        
        String fcIni = "1900-01-01";
        String fcFin = "2100-01-01";
        String Sql = "WHERE";
        
        boolean hayWhere = false;
        boolean first = true;
        
        if (criterio.getFechaInicio().length() > 0 && criterio.getFechaFin().length() > 0) {
            fcIni = EventosUtil.cambioFrmFc( criterio.getFechaInicio() );
            fcFin = EventosUtil.cambioFrmFc( criterio.getFechaFin() );
            
            if (!first) {
                Sql += " AND";
            }
            first = false;
		    Sql += " E.FECHA_CREACION_EVENTO >= '" + fcIni + "' AND E.FECHA_CREACION_EVENTO <= '" + fcFin + "'";
		    hayWhere = true;
            
        }
        if (!criterio.getEstado().equalsIgnoreCase("T")) {
            if (!first) {
                Sql += " AND";
            }
            first = false;
		    Sql += " E.ACTIVO = '" + criterio.getEstado() + "'";
		    hayWhere = true;
		    
		}
        if (!criterio.getTipoEvento().equalsIgnoreCase("T")) {
            if (!first) {
                Sql += " AND";
            }
            first = false;
		    Sql += " E.ID_TIPO_EVENTO = " + criterio.getTipoEvento();
		    hayWhere = true;
		}
        
        if (!hayWhere) {
            return "";
        }
		
        return Sql;
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountEventosByCriterio(EventosCriterioDTO criterio) throws EventosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		double cantidad = 0;		
				
		String Sql = " SELECT count(*) cantidad FROM EP_EVENTO E inner join EP_TIPO_EVENTO T on (E.ID_TIPO_EVENTO = T.ID_TIPO_EVENTO) ";
		
		Sql += obtenerWhereEventos(criterio);
		
		logger.debug("* SQL para cantidad de eventos con criterio :"+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    cantidad = rs.getDouble("cantidad");				
			}
			
		} catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountEventosByCriterio - Problema SQL (close)", e);
			}
		}
        return cantidad;
    }

    /**
     * @return
     */
    public List getTiposEventos() throws EventosDAOException {	    
		List listaTipos 		= new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT * FROM EP_TIPO_EVENTO T ";		
		logger.debug("* SQL de tipos de eventos: "+Sql);
		
		try {
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				tipo.setNombre(rs.getString("nombre"));
				listaTipos.add(tipo);
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTiposEventos - Problema SQL (close)", e);
			}
		}
		return listaTipos;
    }

    /**
     * @param usr
     * @return
     */
    public long getEventoEnEdicionByUsuario(UserDTO usr) throws EventosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		long idEvento = 0;
		
		String Sql = " SELECT E.ID_EVENTO FROM EP_EVENTO E WHERE E.ID_USUARIO_EDITOR = " + usr.getId_usuario();
		
		logger.debug("* SQL para obtener el evento en edicion :"+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    idEvento = rs.getLong("id_evento");				
			}
			
		} catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventoEnEdicionByUsuario - Problema SQL (close)", e);
			}
		}
        return idEvento;
    }

    /**
     * @return
     */
    public List getPasos() throws EventosDAOException {	    
		List listaPasos 		= new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT * FROM EP_PASOS P ";		
		logger.debug("* SQL de Pasos: "+Sql);
		
		try {
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				PasoDTO paso = new PasoDTO();
				paso.setIdPaso(rs.getLong("id_paso"));
				paso.setNombre(rs.getString("descripcion"));
				listaPasos.add(paso);
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPasos - Problema SQL (close)", e);
			}
		}
		return listaPasos;
    }

    /**
     * @param evento
     * @return
     */
    public long addEvento(EventoDTO evento) throws EventosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idEvento 			= 0;
		
		logger.debug("getIdUsuarioCreador: " + evento.getIdUsuarioCreador());
		logger.debug("getNombre: " + evento.getNombre());
		logger.debug("getDescripcion: " + evento.getDescripcion());
		logger.debug("getTitulo: " + evento.getTitulo());
		logger.debug("getIdTipoEvento: " + evento.getTipoEvento().getIdTipoEvento());
		logger.debug("getFechaInicio: " + evento.getFechaInicio());
		logger.debug("getFechaFin: " + evento.getFechaFin());
		logger.debug("getOcurrencia: " + evento.getOcurrencia());
		logger.debug("getNombreArchivo: " + evento.getNombreArchivo());
		logger.debug("getOrden: " + evento.getOrden());
		logger.debug("getActivo: " + evento.getActivo());
		logger.debug("getPasos: " + evento.getPasos().size());
		
		
		String SQLStmt = " INSERT INTO EP_EVENTO ( " +
						 " fecha_creacion_evento, fecha_modificacion_evento, id_usuario_creador, nombre, descripcion," +
						 " id_tipo_evento, fecha_inicio, fecha_fin, ocurrencia, nombre_archivo," +
						 " orden, activo, titulo, validacion_manual ) " +
						 " VALUES (" +
						 " ?,?,?,?,?," +
						 " ?,?,?,?,?, " +
						 " ?,?,?,? )";
		
		String SQLEvenPaso = " INSERT INTO EP_EVENTO_PASO (ID_EVENTO,ID_PASO) VALUES (?,?)";

		try {
		    logger.debug("DAO addEvento - AGREGAMOS DATOS DEL EVENTO");
			logger.debug("SQL: " + SQLStmt );
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt , Statement.RETURN_GENERATED_KEYS );
			
			stm.setDate(1, new java.sql.Date(System.currentTimeMillis()) );
			stm.setDate(2, new java.sql.Date(System.currentTimeMillis()) );
			stm.setLong(3, evento.getIdUsuarioCreador());
			stm.setString(4, evento.getNombre());
			stm.setString(5, evento.getDescripcion());
			
			stm.setLong(6, evento.getTipoEvento().getIdTipoEvento());
			stm.setDate(7, EventosUtil.frmFecha(evento.getFechaInicio(),"dd/MM/yyyy"));
			stm.setDate(8, EventosUtil.frmFecha(evento.getFechaFin(),"dd/MM/yyyy"));
			stm.setLong(9, evento.getOcurrencia());
			stm.setString(10, evento.getNombreArchivo());
			
			stm.setLong(11, evento.getOrden());
			stm.setString(12, evento.getActivo());
			stm.setString(13, evento.getTitulo());
            stm.setString(14, evento.getValidacionManual());
			
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idEvento = results.getLong(1);
				logger.debug("idEvento insertado:" + idEvento);
			}
			
			logger.debug("DAO addEvento - AGREGAMOS LAS REFERENCIAS CON LOS PASOS");
			logger.debug("SQL: " + SQLEvenPaso );
			
			for (int j = 0; j < evento.getPasos().size(); j++) {			
    			PasoDTO paso = (PasoDTO) evento.getPasos().get(j);
    			
    			stm = null;
    			stm = conn.prepareStatement(SQLEvenPaso);
    			stm.setLong(1, idEvento);	
    			stm.setLong(2, paso.getIdPaso());	
    			stm.executeUpdate();
    			
    		}
		
		} catch (SQLException e) {
		    throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return idEvento;
    }

    /**
     * @param idEvento
     * @return
     */
    public EventoDTO getEvento(long idEvento) throws EventosDAOException {	    
        EventoDTO evento 		= new EventoDTO();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		List pasos 				= new ArrayList();
		
		String Sql = " SELECT E.*, T.NOMBRE TIPO FROM EP_EVENTO E " +
					 " INNER JOIN EP_TIPO_EVENTO T ON (E.ID_TIPO_EVENTO = T.ID_TIPO_EVENTO)" +
					 " WHERE E.ID_EVENTO = ?";
		
		String Sql2 = " SELECT EP.*, P.DESCRIPCION " +
					  " FROM EP_EVENTO_PASO EP " +
					  " INNER JOIN EP_PASOS P ON (EP.ID_PASO = P.ID_PASO) " +
					  " WHERE EP.ID_EVENTO = ?";
		
		logger.debug("* SQL EVENTO: "+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, idEvento);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				tipo.setNombre(rs.getString("tipo"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setOrden(rs.getLong("orden"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setActivo(rs.getString("activo"));
				evento.setTitulo(rs.getString("titulo"));
                evento.setValidacionManual(rs.getString("validacion_manual"));
			}
			
			stm = null;
			rs = null;
			
			stm = conn.prepareStatement(Sql2 + " WITH UR");
			stm.setLong(1,idEvento);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    PasoDTO paso = new PasoDTO();
			    paso.setIdPaso(rs.getLong("id_paso"));
			    paso.setNombre(rs.getString("descripcion"));
				pasos.add(paso);
			}			
			evento.setPasos(pasos);
			

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEvento - Problema SQL (close)", e);
			}
		}
		return evento;
    }

    /**
     * @param idEvento
     * @param idUsuario
     * @return
     */
    public boolean setModEvento(long idEvento, long idUsuario) throws EventosDAOException {
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			logger.debug("en setModEvento");
			String sql = "UPDATE EP_EVENTO SET ID_USUARIO_EDITOR = ? " + 
						 " WHERE ID_EVENTO = ? ";
			logger.debug(sql);
			
			logger.debug("idEvento:"+idEvento);
			logger.debug("idUsuario:"+idUsuario);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idUsuario);
			stm.setLong(2, idEvento);
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModEvento - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param evento
     */
    public void modEvento(EventoDTO evento) throws EventosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			String sql = " UPDATE EP_EVENTO SET " +
						 " FECHA_MODIFICACION_EVENTO = ?, NOMBRE = ?, DESCRIPCION = ?, ID_TIPO_EVENTO = ?, FECHA_INICIO = ?, " +
						 " FECHA_FIN = ?, OCURRENCIA = ?, NOMBRE_ARCHIVO = ?, ORDEN = ?, ACTIVO = ?, TITULO = ?, VALIDACION_MANUAL = ? " + 
						 " WHERE ID_EVENTO = ? ";
			logger.debug("SQl para modificar un evento:"+sql);
			
			logger.debug("getNombre:"+evento.getNombre());
			logger.debug("getDescripcion:"+evento.getDescripcion());
			logger.debug("getIdTipoEvento:"+evento.getTipoEvento().getIdTipoEvento());
			logger.debug("getFechaInicio:"+evento.getFechaInicio());
			logger.debug("getFechaFin:"+evento.getFechaFin());
			logger.debug("getOcurrencia:"+evento.getOcurrencia());
			logger.debug("getNombreArchivo:"+evento.getNombreArchivo());
			logger.debug("getOrden:"+evento.getOrden());
			logger.debug("getActivo:"+evento.getActivo());
			logger.debug("getTitulo:"+evento.getTitulo());
			
			
			stm = conn.prepareStatement(sql);
			stm.setDate(1, new java.sql.Date(System.currentTimeMillis()) );
			stm.setString(2, evento.getNombre());
			stm.setString(3, evento.getDescripcion());	
			stm.setLong(4, evento.getTipoEvento().getIdTipoEvento());
			stm.setDate(5, EventosUtil.frmFecha(evento.getFechaInicio(),"dd/MM/yyyy"));
			
			stm.setDate(6, EventosUtil.frmFecha(evento.getFechaFin(),"dd/MM/yyyy"));
			stm.setLong(7, evento.getOcurrencia());
			stm.setString(8, evento.getNombreArchivo());
			stm.setLong(9, evento.getOrden());
			stm.setString(10, evento.getActivo());
			stm.setString(11, evento.getTitulo());
            stm.setString(12, evento.getValidacionManual());
			
			stm.setLong(13, evento.getIdEvento());
			stm.executeUpdate();
			
			// borrar evento paso
			stm = null;
			String sql2 = " DELETE FROM EP_EVENTO_PASO WHERE ID_EVENTO = ? ";
			logger.debug("SQl para borrar los pasos del evento:"+sql2);
						
			stm = conn.prepareStatement(sql2);
			stm.setLong(1, evento.getIdEvento());	
			stm.executeUpdate();
			
			
			// agregamos los nuevos pasos
			String sql3 = " INSERT INTO EP_EVENTO_PASO (ID_EVENTO,ID_PASO) VALUES (?,?)";
			logger.debug("SQL para agregar los pasos del vento: " + sql3 );
			
			for (int j = 0; j < evento.getPasos().size(); j++) {			
    			PasoDTO paso = (PasoDTO) evento.getPasos().get(j);
    			
    			stm = null;
    			stm = conn.prepareStatement(sql3);
    			stm.setLong(1, evento.getIdEvento());	
    			stm.setLong(2, paso.getIdPaso());	
    			stm.executeUpdate();
    			
    		}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modEvento - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean setLiberaEvento(long idEvento) throws EventosDAOException {
		PreparedStatement stm = null;
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setLiberaEvento");
			String sql = "UPDATE EP_EVENTO SET ID_USUARIO_EDITOR = NULL " +
						 " WHERE ID_EVENTO = ? ";
			logger.debug(sql);
			
			logger.debug("idEvento:"+idEvento);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idEvento);		
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}
		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setLiberaEvento - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param evento
     * @return
     */
    public boolean existeEvento(EventoDTO evento) throws EventosDAOException {	    
		boolean existe = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT * FROM EP_EVENTO E WHERE LOWER(E.NOMBRE) = ? ";	
		
		if (evento.getIdEvento() != 0) {
		    Sql += "AND E.ID_EVENTO != " + evento.getIdEvento();
		}
		
		logger.debug("* SQL de existeEvento: "+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setString(1,evento.getNombre().toLowerCase());
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    existe = true;
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : existeEvento - Problema SQL (close)", e);
			}
		}
		return existe;
    }

    /**
     * @param evento
     */
    public void delEvento(EventoDTO evento) throws EventosDAOException {
		PreparedStatement stm = null;
		
		try {
			//Eliminamos pasos
			conn = this.getConnection();
			stm = null;
			logger.debug("en delEvento: Eliminamos pasos!");
			String sql1 = " DELETE FROM EP_EVENTO_PASO " +
						  " WHERE ID_EVENTO = ? ";
			logger.debug(sql1);
			
			stm = conn.prepareStatement(sql1);
			stm.setLong(1, evento.getIdEvento());		
			stm.executeUpdate();
			
			//Eliminamos los rut con op's asociadas
			stm = null;
			logger.debug("en delEvento: Eliminamos op's de rutEventos!");
			String sql2 = " DELETE FROM EP_OP_EVENTOS " +
						  " WHERE ID_EVENTO = ? ";
			logger.debug(sql2);
			
			stm = conn.prepareStatement(sql2);
			stm.setLong(1, evento.getIdEvento());		
			stm.executeUpdate();

			//Eliminamos los ruts
			stm = null;
			logger.debug("en delEvento: Eliminamos los ruts!");
			String sql3 = " DELETE FROM EP_RUT_EVENTOS " +
						  " WHERE ID_EVENTO = ? ";
			logger.debug(sql3);
			
			stm = conn.prepareStatement(sql3);
			stm.setLong(1, evento.getIdEvento());		
			stm.executeUpdate();
			
			// Eliminamos los eventos
			stm = null;
			logger.debug("en delEvento: Eliminamos los eventos!");
			String sql4 = " DELETE FROM EP_EVENTO " +
						  " WHERE ID_EVENTO = ? ";
			logger.debug(sql4);
			
			stm = conn.prepareStatement(sql4);
			stm.setLong(1, evento.getIdEvento());		
			stm.executeUpdate();

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delEvento - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public long getIdUsuarioEditorDeEvento(long idEvento) throws EventosDAOException {	    
		long idUsuarioEditor = 0;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT * FROM EP_EVENTO WHERE ID_EVENTO = ?";		
		logger.debug("* SQL getIdUsuarioEditorDeEvento: "+Sql);
		logger.debug("idEvento:"+idEvento);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1,idEvento);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    idUsuarioEditor = rs.getLong("id_usuario_editor");
			}
		} catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdUsuarioEditorDeEvento - Problema SQL (close)", e);
			}
		}
		return idUsuarioEditor;
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean eventoUtilizado(long idEvento) throws EventosDAOException {	    
		boolean eventoUtilizado = false;
        long suma = 0;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = "SELECT SUM(OCURRENCIA_POR_RUT) SUMA FROM EP_RUT_EVENTOS WHERE ID_EVENTO = ?";		
		logger.debug("* SQL eventoUtilizado: "+Sql);
		logger.debug("idEvento:"+idEvento);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1,idEvento);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    suma = rs.getLong("suma");
			}

		} catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eventoUtilizado - Problema SQL (close)", e);
			}
		}
		if (suma > 0) {
		    eventoUtilizado = true;
		}
		return eventoUtilizado;
    }

    /**
     * @param ruts
     * @param idEvento
     * @return
     */
    public String addRutsEvento(Vector ruts, EventoDTO evento) throws EventosDAOException {
		PreparedStatement stmSelect = null;
		PreparedStatement stmUpdate = null;
		PreparedStatement stmInsert = null;
		ResultSet rs = null;
		boolean existeRut = false;
		int insert 	= 0;
		int update 	= 0;
		String msg = "";
		
		try {
			conn = this.getConnection();
			String sql1 = " SELECT * FROM EP_RUT_EVENTOS " + 
						  " WHERE ID_EVENTO = ? AND CLI_RUT = ? ";
			String sql2 = " INSERT INTO EP_RUT_EVENTOS (" +
						  " CLI_RUT, ID_EVENTO, CLI_DV, OCURRENCIA_POR_RUT, FECHA_CREACION, " +
						  " FECHA_MODIFICACION) VALUES (?,?,?,?,?,?) ";
			String sql3 = " UPDATE EP_RUT_EVENTOS SET FECHA_MODIFICACION = ? " +
						  " WHERE ID_EVENTO = ? AND CLI_RUT = ? ";
			
			stmSelect	= conn.prepareStatement(sql1 + " WITH UR");
			stmInsert 	= conn.prepareStatement(sql2);
			stmUpdate 	= conn.prepareStatement(sql3);
			
			for (int i = 0; i < ruts.size(); i++) {
			    existeRut = false;
			    RutEventoDTO rut = (RutEventoDTO) ruts.get(i);
			    
			    stmSelect.setLong(1,evento.getIdEvento());
			    stmSelect.setLong(2,rut.getCliRut());
				rs = stmSelect.executeQuery();
				
				if (rs.next()) {
				    existeRut = true;
				}
				
				if (existeRut) {
				    // Actualizamos
				    stmUpdate.setDate(1, new java.sql.Date(System.currentTimeMillis()) );
				    stmUpdate.setLong(2, evento.getIdEvento());
				    stmUpdate.setLong(3, rut.getCliRut());
				    stmUpdate.executeUpdate();

				    update++;
				    
				    
				} else {
				    // Agregamos
				    stmInsert.setLong(1, rut.getCliRut());	
				    stmInsert.setLong(2, evento.getIdEvento());
				    stmInsert.setString(3, rut.getCliDv());
				    stmInsert.setLong(4, 0);
	    			stmInsert.setDate(5, new java.sql.Date(System.currentTimeMillis()) );
	    			stmInsert.setDate(6, new java.sql.Date(System.currentTimeMillis()) );
	    			stmInsert.executeUpdate();
   			
	    			insert++;
				}
			}
			if (insert == 1) {
			    msg += "<br>Se agregó " + insert + " nuevo Rut.<br>";
			} else if (insert > 1) {
			    msg += "<br>Se agregaron " + insert + " nuevos Rut's.<br>";
			}
			if (update == 1) {
			    msg += "<br>Se actualizó " + update + " Rut.<br>";
			} else if (update > 1) {
			    msg += "<br>Se actualizaron " + update + " Rut's.<br>";
			}
			
			if (update == 0 && insert == 0) {
			    msg += "<br><font color=red>No se agregó y no se actualizó ningún Rut.</font><br>";
			}
			
		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
                if (stmSelect != null) stmSelect.close();
    			if (stmInsert != null) stmInsert.close();
    			if (stmUpdate != null) stmUpdate.close();
    			if (rs != null)  rs.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addRutsEvento - Problema SQL (close)", e);
			}
		}
		
        return msg;
    }

    /**
     * @param idEvento
     * @return
     */
    public List getRutsByEvento(long idEvento) throws EventosDAOException {	    
		List ruts = new ArrayList();
        PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = "SELECT e.cli_rut, e.id_evento, e.cli_dv, e.ocurrencia_por_rut, e.fecha_creacion, " +
                     "e.fecha_modificacion " +
                     "FROM bodba.EP_RUT_EVENTOS e " +
                     "WHERE e.ID_EVENTO = ?";
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1,idEvento);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    RutEventoDTO re = new RutEventoDTO();
			    re.setCliRut(rs.getLong("cli_rut"));
			    re.setIdEvento(rs.getLong("id_evento"));
			    re.setCliDv(rs.getString("cli_dv"));
			    re.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
			    re.setFechaCreacion(rs.getString("fecha_creacion"));
			    re.setFechaModificacion(rs.getString("fecha_modificacion"));
			    ruts.add(re);
			}

		} catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getRutsByEvento - Problema SQL (close)", e);
			}
		}
		
		return ruts;
    }

    /**
     * @param rutCliente
     * @return
     */
    public List getEventosByRutCliente(long rutCliente) throws EventosDAOException {
	    
		List listaEventos 		= new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT E.ID_EVENTO, E.FECHA_CREACION_EVENTO, E.FECHA_MODIFICACION_EVENTO, E.ID_USUARIO_EDITOR, " +
					 " E.ID_USUARIO_CREADOR, E.NOMBRE, E.DESCRIPCION, E.ID_TIPO_EVENTO, E.FECHA_INICIO, E.FECHA_FIN, " +
					 " E.OCURRENCIA, E.NOMBRE_ARCHIVO, E.ORDEN, E.ACTIVO, T.NOMBRE TIPO_EVENTO, " +
					 " R.OCURRENCIA_POR_RUT " +
					 " FROM EP_EVENTO E " +
					 " inner join EP_TIPO_EVENTO T on (E.ID_TIPO_EVENTO = T.ID_TIPO_EVENTO) " +
					 " inner join EP_RUT_EVENTOS R on (E.ID_EVENTO = R.ID_EVENTO) " +
					 " WHERE R.CLI_RUT = ? AND R.OCURRENCIA_POR_RUT < E.OCURRENCIA " +
					 " ORDER BY E.ORDEN ";
		
		logger.debug("* SQL getEventosByRutCliente :"+Sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");	
			stm.setLong(1,rutCliente);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				EventoDTO evento = new EventoDTO();
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				tipo.setNombre(rs.getString("tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setOrden(rs.getLong("orden"));
				evento.setActivo(rs.getString("activo"));
				evento.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
				
				listaEventos.add(evento);
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventosByRutCliente - Problema SQL (close)", e);
			}
		}
		return listaEventos;
    }

    /**
     * @param rutCliente
     * @return
     */
    public boolean tieneEventoByRutCliente(long rutCliente) throws EventosDAOException {
	    
		boolean tieneEvento 	= false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT * " +
					 " FROM EP_EVENTO E " +
					 " inner join EP_RUT_EVENTOS R on (E.ID_EVENTO = R.ID_EVENTO) " +
					 " WHERE R.CLI_RUT = ? AND R.OCURRENCIA_POR_RUT < E.OCURRENCIA ";
        
		logger.debug("* SQL tieneEventoByRutCliente :"+Sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");	
			stm.setLong(1,rutCliente);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    tieneEvento = true;
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : tieneEventoByRutCliente - Problema SQL (close)", e);
			}
		}
		return tieneEvento;
    }

    /**
     * @param clienteRut
     * @return
     */
    public EventoDTO getEventoMostrarByRut(long clienteRut) throws EventosDAOException {
        EventoDTO evento 		= new EventoDTO();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		List pasos 				= new ArrayList();
		boolean tieneEvento	= false;
		
		String sql = " SELECT * " +
					 " FROM BODBA.EP_RUT_EVENTOS RE " +
					 " INNER JOIN BODBA.EP_EVENTO E ON (RE.ID_EVENTO = E.ID_EVENTO) " +
					 " WHERE RE.CLI_RUT = ? " +
					 " AND E.OCURRENCIA > RE.OCURRENCIA_POR_RUT " +
					 " AND (E.NOMBRE_ARCHIVO != '' AND E.NOMBRE_ARCHIVO IS NOT NULL ) " +
					 " AND E.FECHA_INICIO <= CURRENT_DATE " +
					 " AND E.FECHA_FIN >= CURRENT_DATE " +
					 " AND E.ACTIVO = 'S' " +
					 " ORDER BY ORDEN";
		
		String sql2 = " SELECT EP.ID_PASO, P.DESCRIPCION " +
					  " FROM BODBA.EP_EVENTO_PASO EP " +
					  " INNER JOIN BODBA.EP_PASOS P ON (EP.ID_PASO = P.ID_PASO) " +
					  " WHERE EP.ID_EVENTO = ?";
		
		try {
			conn = this.getConnection();
		    
			logger.debug("* SQL 1 getEventoMostrarByRut: "+sql);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, clienteRut);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setOrden(rs.getLong("orden"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setActivo(rs.getString("activo"));
				evento.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
				evento.setTitulo(rs.getString("titulo"));
				tieneEvento = true;
			}
			
			if (tieneEvento) {
			    stm = null;
				rs = null;
				stm = conn.prepareStatement(sql2 + " WITH UR");
				stm.setLong(1, evento.getIdEvento());
				rs = stm.executeQuery();
				
				while (rs.next()) {
				    PasoDTO paso = new PasoDTO();
				    paso.setIdPaso(rs.getLong("id_paso"));
				    paso.setNombre(rs.getString("descripcion"));
					pasos.add(paso);
				}			
				evento.setPasos(pasos);
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventoMostrarByRut - Problema SQL (close)", e);
			}
		}
		return evento;
    }

    /**
     * @param fingreso
     * @return
     */
    public EventoDTO getEventoMostrarByRutYFecha(long rutCliente, String fingreso) throws EventosDAOException {
        EventoDTO evento 		= new EventoDTO();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.EP_RUT_EVENTOS RE " +
					 " INNER JOIN BODBA.EP_EVENTO E ON (RE.ID_EVENTO = E.ID_EVENTO) " +
					 " WHERE RE.CLI_RUT = ? " +
					 " AND E.OCURRENCIA >= RE.OCURRENCIA_POR_RUT " +
					 " AND (E.NOMBRE_ARCHIVO != '' AND E.NOMBRE_ARCHIVO IS NOT NULL ) " +
					 " AND E.FECHA_INICIO <= ? " +
					 " AND E.FECHA_FIN >= ? " +
					 " AND E.ACTIVO = 'S' " +
					 " ORDER BY ORDEN";
		logger.debug("* SQL getEventoMostrarByRutYFecha: "+sql);
		
		try {
			conn = this.getConnection();
		    stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, rutCliente);
			stm.setDate(2, EventosUtil.frmFecha(fingreso,"yyyy-MM-dd"));
			stm.setDate(3, EventosUtil.frmFecha(fingreso,"yyyy-MM-dd"));
		    rs = stm.executeQuery();
			
			if (rs.next()) {
			    evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setOrden(rs.getLong("orden"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setActivo(rs.getString("activo"));
				evento.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventoMostrarByRutYFecha - Problema SQL (close)", e);
			}
		}
		return evento;
    }

    /**
     * @param idEvento
     * @param rutCliente
     * @param valor
     */
    public boolean subirOcurrenciaEvento(long rutCliente, long idPedido) throws EventosDAOException {
        
        logger.debug("en subirOcurrenciaEvento");        
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		boolean resultado 		= true;
		List eventos = new ArrayList();
		
		String sql1 = " INSERT INTO BODBA.EP_OP_EVENTOS " +
					  " (CLI_RUT,ID_EVENTO,ID_PEDIDO)" +
					  " VALUES " +
					  " (?,?,?)";
		
		String sql2 = " UPDATE BODBA.EP_RUT_EVENTOS " +
					  " SET OCURRENCIA_POR_RUT = OCURRENCIA_POR_RUT + 1 " +
					  " WHERE CLI_RUT = ? " +
					  " AND ID_EVENTO = ? ";
		
		String sqlEventos =  " SELECT E.ID_EVENTO as id_evento " +
							 " FROM BODBA.EP_RUT_EVENTOS RE " +
							 " INNER JOIN BODBA.EP_EVENTO E ON (RE.ID_EVENTO = E.ID_EVENTO) " +
							 " WHERE RE.CLI_RUT = ? " +
							 " AND E.OCURRENCIA > RE.OCURRENCIA_POR_RUT " +
							 " AND E.FECHA_INICIO <= CURRENT_DATE " +
							 " AND E.FECHA_FIN >= CURRENT_DATE " +
							 " AND E.ACTIVO = 'S' " +
							 " ORDER BY ORDEN";
		try {
		    conn = this.getConnection();
			
			logger.debug("Sql Obtener eventos del cliente:" + sqlEventos);
			stm = conn.prepareStatement(sqlEventos + " WITH UR");
			stm.setLong(1, rutCliente);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    EventoDTO eve = new EventoDTO();
			    eve.setIdEvento(rs.getLong("id_evento"));
			    eventos.add(eve);			    
			}
			
			for (int i = 0; i < eventos.size(); i++) {			    
			    EventoDTO eve = (EventoDTO) eventos.get(i);
				stm = null;
				logger.debug(sql1);
				stm = conn.prepareStatement(sql1);
				stm.setLong(1, rutCliente);
				stm.setLong(2, eve.getIdEvento());
				stm.setLong(3, idPedido);
				stm.executeUpdate();
				
				stm = null;
				logger.debug(sql2);
				stm = conn.prepareStatement(sql2);
				stm.setLong(1, rutCliente);
				stm.setLong(2, eve.getIdEvento());
				stm.executeUpdate();
				
			}

		} catch (SQLException e) {
		    e.printStackTrace();
			resultado = false;
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

    /**
     * @param idPedido
     * @param rutCliente
     * @return
     */
    public boolean bajarOcurrenciaEvento(long idPedido, long rutCliente) throws EventosDAOException {
        PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		boolean resultado 		= true;
		List eventos 			= new ArrayList();
		
		try {
			conn = this.getConnection();
			logger.debug("en subirOcurrenciaEvento");
			
			String sql1 = " SELECT * FROM BODBA.EP_OP_EVENTOS " +
						  " WHERE CLI_RUT = ? AND ID_PEDIDO = ?";
			
			String sql2 = " UPDATE BODBA.EP_RUT_EVENTOS " +
						  " SET OCURRENCIA_POR_RUT = OCURRENCIA_POR_RUT - 1 " +
						  " WHERE CLI_RUT = ? " +
						  " AND ID_EVENTO = ? ";
			
			logger.debug(sql1);
			
			stm = conn.prepareStatement(sql1 + " WITH UR");
			stm.setLong(1, rutCliente);
			stm.setLong(2, idPedido);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    EventoDTO eve = new EventoDTO();
			    eve.setIdEvento(Long.parseLong(rs.getString("id_evento")));
			    eventos.add(eve);
			}
			
			for ( int i = 0 ; i < eventos.size(); i++) {
			    EventoDTO eve = (EventoDTO) eventos.get(i);
				stm = null;
				logger.debug(sql2);
				stm = conn.prepareStatement(sql2);
				stm.setLong(1, rutCliente);
				stm.setLong(2, eve.getIdEvento());
				stm.executeUpdate();
			}

		} catch (SQLException e) {
		    e.printStackTrace();
		    resultado = false;
		    
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : bajarOcurrenciaEvento - Problema SQL (close)", e);
			}
		}
		return resultado; 
    }

    /**
     * Metodo para las alarmas.. ojo que no importa el estado, ni las ocurrencias del evento
     * solo retorna uno de los eventos que tenga asignado un pedido. Estos eventos quedaron asignados al momento de
     * realizarce la orden de pedido
     * 
     * @param rut_cliente
     * @param id_pedido
     * @return
     */
    public EventoDTO getEventoByPedido(long rutCliente, long idPedido) throws EventosDAOException {
        EventoDTO evento 		= new EventoDTO();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT EO.*, E.* " +
					 " FROM BODBA.EP_OP_EVENTOS EO " +
					 " INNER JOIN BODBA.EP_EVENTO E ON (EO.ID_EVENTO = E.ID_EVENTO) " +
					 " WHERE EO.CLI_RUT = ? AND EO.ID_PEDIDO = ? ";
		logger.debug("* SQL getEventoByPedido: "+sql);
		
		try {
			conn = this.getConnection();
		    stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, rutCliente);
			stm.setLong(2, idPedido);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setOrden(rs.getLong("orden"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setActivo(rs.getString("activo"));
                evento.setValidacionManual(rs.getString("validacion_manual"));
				//evento.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventoByPedido - Problema SQL (close)", e);
			}
		}
		return evento;
    }

    /**
     * @param rutCliente
     * @param idPedido
     * @return
     */
    public List getEventosByRutClienteAndPedido(long rutCliente, long idPedido) throws EventosDAOException {
	    
		List listaEventos 		= new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String Sql = " SELECT E.ID_EVENTO, E.FECHA_CREACION_EVENTO, E.FECHA_MODIFICACION_EVENTO, E.ID_USUARIO_EDITOR, " +
					 " E.ID_USUARIO_CREADOR, E.NOMBRE, E.DESCRIPCION, E.ID_TIPO_EVENTO, E.FECHA_INICIO, E.FECHA_FIN, " +
					 " E.OCURRENCIA, E.NOMBRE_ARCHIVO, E.ORDEN, E.ACTIVO, T.NOMBRE TIPO_EVENTO, " +
					 " R.OCURRENCIA_POR_RUT " +
					 " FROM EP_EVENTO E " +
					 " inner join EP_TIPO_EVENTO T on (E.ID_TIPO_EVENTO = T.ID_TIPO_EVENTO) " +
					 " inner join EP_RUT_EVENTOS R on (E.ID_EVENTO = R.ID_EVENTO) " +
					 " inner join EP_OP_EVENTOS O on (R.CLI_RUT = O.CLI_RUT AND R.ID_EVENTO = O.ID_EVENTO) " +
					 " WHERE R.CLI_RUT = ? AND " +
					 " O.ID_PEDIDO = ? " +
					 " ORDER BY E.ORDEN ";
		
		logger.debug("* SQL getEventosByRutClienteAndPedido :"+Sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");	
			stm.setLong(1,rutCliente);
			stm.setLong(2,idPedido);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				EventoDTO evento = new EventoDTO();
				evento.setIdEvento(rs.getLong("id_evento"));
				evento.setFechaCreacion(rs.getString("fecha_creacion_evento"));
				evento.setFechaModificacion(rs.getString("fecha_modificacion_evento"));
				evento.setIdUsuarioEditor(rs.getLong("id_usuario_editor"));
				evento.setNombre(rs.getString("nombre"));
				evento.setDescripcion(rs.getString("descripcion"));
				TipoEventoDTO tipo = new TipoEventoDTO();
				tipo.setIdTipoEvento(rs.getLong("id_tipo_evento"));
				tipo.setNombre(rs.getString("tipo_evento"));
				evento.setTipoEvento(tipo);
				evento.setFechaInicio(rs.getString("fecha_inicio"));
				evento.setFechaFin(rs.getString("fecha_fin"));
				evento.setOcurrencia(rs.getLong("ocurrencia"));
				evento.setNombreArchivo(rs.getString("nombre_archivo"));
				evento.setOrden(rs.getLong("orden"));
				evento.setActivo(rs.getString("activo"));
				evento.setOcurrenciaPorRut(rs.getLong("ocurrencia_por_rut"));
				
				listaEventos.add(evento);
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEventosByRutClienteAndPedido - Problema SQL (close)", e);
			}
		}
		return listaEventos;
    }

    /**
     * @param idEvento
     * @return
     */
    public long getCantidadRutsUsaronEvento(long idEvento) throws EventosDAOException {
	    
		long cantidad = 0;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT count(cli_rut) cantidad " +
					 " FROM EP_RUT_EVENTOS EP " +
					 " WHERE EP.OCURRENCIA_POR_RUT > 0 AND EP.ID_EVENTO = ? ";
		
		logger.debug("* SQL getCantidadRutsUsaronEvento :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			stm.setLong(1,idEvento);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    cantidad = rs.getLong("cantidad");				
			}

		}catch (SQLException e) {
			throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCantidadRutsUsaronEvento - Problema SQL (close)", e);
			}
		}
		return cantidad;
    }
    
    
    public boolean tieneEventosActivosConValidacionManual( long rutCliente ) throws EventosDAOException {
        boolean tieneEvento     = false;
        PreparedStatement stm   = null;
        ResultSet rs            = null;
        
        String Sql = " SELECT E.ID_EVENTO " +
                     " FROM BODBA.EP_RUT_EVENTOS RE " +
                     "  INNER JOIN BODBA.EP_EVENTO E ON (RE.ID_EVENTO = E.ID_EVENTO) " +
                     " WHERE RE.CLI_RUT = ? " +
                     " AND E.OCURRENCIA > RE.OCURRENCIA_POR_RUT " +
                     " AND E.FECHA_INICIO <= CURRENT_DATE " +
                     " AND E.FECHA_FIN >= CURRENT_DATE " +
                     " AND E.ACTIVO = 'S' " +
                     " AND E.VALIDACION_MANUAL = 'S'";
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(Sql + " WITH UR");   
            stm.setLong(1,rutCliente);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                tieneEvento = true;
            }

        } catch (SQLException e) {
            throw new EventosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : tieneEventosActivosConValidacionManual - Problema SQL (close)", e);
			}
		}
        return tieneEvento;
    }
    
}
