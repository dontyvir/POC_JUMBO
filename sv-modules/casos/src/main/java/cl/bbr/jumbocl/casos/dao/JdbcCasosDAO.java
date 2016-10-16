package cl.bbr.jumbocl.casos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.dto.ReclamosClienteDTO;
import cl.bbr.jumbocl.casos.exceptions.CasosDAOException;
import cl.bbr.jumbocl.casos.utils.CasosEstadosUtil;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.contenidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Clase que permite consultar los Casos que se encuentran en la base de datos.
 * @author imoyano
 *
 */
public class JdbcCasosDAO implements CasosDAO {
	
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
	public void setTrx(JdbcTransaccion trx)	throws CasosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CasosDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcCasosDAO(){
		
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
	public List getCasosByCriterio(CasosCriterioDTO criterio) throws CasosDAOException {
	    
		List listaCasos = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		String order = "";
		
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if (pag <= 0) { 
		    pag = 1;
		}
		if (regXpag < 10) {
		    regXpag = 10;
		}
		int iniReg = (pag - 1) * regXpag + 1;
		int finReg = pag * regXpag;
		
		
		// Orden de la Query
		if (criterio.getSistema().equalsIgnoreCase("BOC")) {
		    //order = " L.NOM_LOCAL ";
		    order = " C.ID_CASO DESC ";
		} else {
		    //order = " C.FECHA_CREACION_CASO ";
		    order = " C.ID_CASO DESC ";
		}		
		
		String sql = " SELECT * FROM ( " +
					 " SELECT row_number() over( ORDER BY " + order + " ) as row, " +
					 " C.id_caso,c.fecha_creacion_caso,C.fecha_modificacion_caso,C.id_pedido,C.direccion, " +
                     " C.comuna,C.fecha_despacho,C.cli_rut,C.cli_dv,C.cli_nombre,C.cli_fon_cod_1,C.cli_fon_num_1, " +
                     " C.cli_fon_cod_2,C.cli_fon_num_2,C.cli_fon_cod_3,C.cli_fon_num_3,C.fecha_compromiso_solucion, " +
                     " C.id_usuario_editor,C.id_usuario_creador,C.id_local,C.id_jornada,C.id_estado,C.escalamiento, " +
                     " J.DESCRIPCION JORNADA, E.NOMBRE ESTADO, L.NOM_LOCAL LOCAL  FROM GPV_CASO C, GPV_JORNADA J, GPV_ESTADO E, BO_LOCALES L  WHERE C.ID_JORNADA = J.ID_JORNADA AND  C.ID_ESTADO = E.ID_ESTADO AND C.ID_LOCAL = L.ID_LOCAL ";
		
		sql += obtenerWhereCasos(criterio);
		
		sql += " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
		
		logger.debug("* SQL con criterio :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				CasoDTO caso = new CasoDTO();
				caso.setIdCaso(rs.getLong("id_caso"));
				caso.setFcCreacionCaso(rs.getString("fecha_creacion_caso"));
				caso.setFcModificacionCaso(rs.getString("fecha_modificacion_caso"));
				caso.setIdPedido(rs.getLong("id_pedido"));
				caso.setDireccion(rs.getString("direccion"));
				caso.setComuna(rs.getString("comuna"));
				caso.setFcDespacho(rs.getString("fecha_despacho"));
				caso.setCliRut(rs.getLong("cli_rut"));
				caso.setCliDv(rs.getString("cli_dv"));
				caso.setCliNombre(rs.getString("cli_nombre"));
				caso.setCliFonoCod1(rs.getString("cli_fon_cod_1"));
				caso.setCliFonoNum1(rs.getString("cli_fon_num_1"));
				caso.setCliFonoCod2(rs.getString("cli_fon_cod_2"));
				caso.setCliFonoNum2(rs.getString("cli_fon_num_2"));
				caso.setCliFonoCod3(rs.getString("cli_fon_cod_3"));
				caso.setCliFonoNum3(rs.getString("cli_fon_num_3"));
				caso.setFcCompromisoSolucion(rs.getString("fecha_compromiso_solucion"));
				caso.setIdUsuarioEdit(rs.getLong("id_usuario_editor"));
				caso.setEditUsuario(rs.getString("id_usuario_editor"));
				caso.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				caso.setCreadorUsuario(rs.getString("id_usuario_creador"));
				ObjetoDTO local = new ObjetoDTO();
				local.setIdObjeto(rs.getLong("id_local"));
				local.setNombre(rs.getString("local"));
				caso.setLocal(local);
				JornadaDTO jornada = new JornadaDTO();
				jornada.setIdJornada(rs.getLong("id_jornada"));
				jornada.setDescripcion(rs.getString("jornada"));
				caso.setJornada(jornada);
				EstadoCasoDTO estado = new EstadoCasoDTO();
				estado.setIdEstado(rs.getLong("id_estado"));
				estado.setNombre(rs.getString("estado"));
				caso.setEstado(estado);
				caso.setAlarma(obtenerTipoAlarmaDeCaso(caso.getFcCompromisoSolucion(), caso.getEstado()));
				if (rs.getString("escalamiento") == null || rs.getString("escalamiento").equalsIgnoreCase("N")) {
				    caso.setEscalamiento(false);
				} else {
				    caso.setEscalamiento(true);
				}
				
				listaCasos.add(caso);
			}
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCasosByCriterio - Problema SQL (close)", e);
			}
		}
		return listaCasos;
	}
	
	/**
     * @param criterio
     * @return
     */
    private String obtenerWhereCasos(CasosCriterioDTO criterio) {
        String sql = "";
        if (criterio.getPedido().length() > 0) {
            sql += " AND C.ID_PEDIDO = " + criterio.getPedido();
		}
		if (criterio.getCliRut().length() > 0) {
		    sql += " AND C.CLI_RUT = " + criterio.getCliRut();
		}
		if (criterio.getCliApellido().length() > 0) {
		    sql += " AND LOWER(C.CLI_NOMBRE) LIKE '%" + criterio.getCliApellido().toLowerCase() + "%'";
		}
		if (criterio.getNroCaso().length() > 0) {
		    sql += " AND C.ID_CASO = " + criterio.getNroCaso();
		}
		if (!criterio.getEstado().equalsIgnoreCase("T")) {
		    sql += " AND C.ID_ESTADO = " + criterio.getEstado();
		}
        if ("BOL".equalsIgnoreCase(criterio.getSistema())) {
            sql += " AND C.ID_ESTADO != " + CasosEstadosUtil.PRE_INGRESADO;
        }
		if (!criterio.getLocal().equalsIgnoreCase("T")) {
		    sql += " AND C.ID_LOCAL = " + criterio.getLocal() ;
		}
		if (criterio.getFcIniCreacion().length() > 0 && criterio.getFcFinCreacion().length() > 0) {
		    sql += " AND ( C.FECHA_CREACION_CASO >= '" + CasosUtil.cambioFrmFc(criterio.getFcIniCreacion()) + " 00:00:00' AND C.FECHA_CREACION_CASO <= '" + CasosUtil.cambioFrmFc(criterio.getFcFinCreacion()) + " 23:59:59' )";
		}
		if (criterio.getFcIniCopromiso().length() > 0 && criterio.getFcFinCopromiso().length() > 0) {
		    sql += " AND ( C.FECHA_COMPROMISO_SOLUCION >= '" + CasosUtil.cambioFrmFc(criterio.getFcIniCopromiso()) + "' AND C.FECHA_COMPROMISO_SOLUCION <= '" + CasosUtil.cambioFrmFc(criterio.getFcFinCopromiso()) + "' )";
		}
		if (!criterio.getAlarma().equalsIgnoreCase("T")) {
		    Calendar cHoy = Calendar.getInstance();
		    Calendar cManana = Calendar.getInstance();
		    cManana.add(Calendar.DAY_OF_YEAR, 0); //Se cambió a 'cero' ya q es necesario comparar con la fecha actual y dejar los casos del dia de hoy de color naranjo
		    
		    Date dHoy = cHoy.getTime();
		    Date dManana = cManana.getTime();
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    
		    //logger.debug("Fechas para la query: HOY (" +sdf.format(dHoy) + ") - MAÑANA (" +sdf.format(dManana)+ ") ");
		    if (criterio.getAlarma().equalsIgnoreCase("E")) {
		        sql += " AND C.ESCALAMIENTO = 'S' ";
		    } else if (criterio.getAlarma().equalsIgnoreCase("V")) {
		        sql += " AND C.FECHA_COMPROMISO_SOLUCION < '" + sdf.format(dHoy) + "' AND (C.ID_ESTADO != 7 AND C.ID_ESTADO != 8) ";
		    } else if (criterio.getAlarma().equalsIgnoreCase("P")) {
		        sql += " AND ( C.FECHA_COMPROMISO_SOLUCION = '" + sdf.format(dHoy) + "' OR C.FECHA_COMPROMISO_SOLUCION = '" + sdf.format(dManana) + "' ) AND (C.ID_ESTADO != 7 AND C.ID_ESTADO != 8) ";
		    } else if (criterio.getAlarma().equalsIgnoreCase("F")) {
		        sql += " AND (C.ID_ESTADO = 7 OR C.ID_ESTADO = 8) ";
		    } else {
		        sql += " AND C.FECHA_COMPROMISO_SOLUCION > '" + sdf.format(dManana) + "' AND (C.ID_ESTADO != 7 AND C.ID_ESTADO != 8) ";
		    }
		}
		
        return sql;
    }

    /**
     * Método para obtener el color de los casos de acuerdo a las alarmas
     * 
     * @param fcResolucion Fecha de resolucion
     * @return estado de alarma para el caso:
     * 
     * 		V: Vencida - Rojo - FF0000
     * 		P: Por vencer - Naranjo - FF9933
     * 		N: Normal - Verde - 339900
     * 		F: Finalizado - Negro - 000000
     * 
     */
    private String obtenerTipoAlarmaDeCaso(String fcResolucion, EstadoCasoDTO estado) {
        
		try {		    
		    if (estado.getIdEstado() == 7 || estado.getIdEstado() == 8) {
		        return "F";
		    }
		    
		    Calendar cHoy = Calendar.getInstance();
		    Calendar cManana = Calendar.getInstance();
		    cManana.add(Calendar.DAY_OF_YEAR, 0); //Se cambió a 'cero' ya q es necesario comparar con la fecha actual y dejar los casos del dia de hoy de color naranjo
		    
		    Date dHoy = cHoy.getTime();
		    Date dManana = cManana.getTime();
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date dResolucion = sdf.parse(fcResolucion);		    
		    
//		    logger.debug("Fechas para obtener el tipo de alarma: HOY (" +sdf.format(dHoy) + ") - MAÑANA (" +sdf.format(dManana)+ ") - Resolucion (" + sdf.format(dResolucion) + ")");
		    
		    if (sdf.format(dHoy).equalsIgnoreCase(sdf.format(dResolucion)) || sdf.format(dManana).equalsIgnoreCase(sdf.format(dResolucion))) {
		        return "P";		        
		    } 
		    
		    if (dHoy.after(dResolucion)) {
		        return "V";
		    }            
                        
        } catch (ParseException e) {
        }
        return "N";
    }
	
	
	
	/**
	 * Lista estados de los casos
	 * 
	 * @return List EstadoCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
	public List getEstadosDeCasos() throws CasosDAOException {
		List listaEstados = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getEstadosDeCasos");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_estado,nombre FROM GPV_ESTADO order by ORDEN ";

		logger.debug("SQL con criterio :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				EstadoCasoDTO estado = new EstadoCasoDTO();
				estado.setIdEstado(rs.getLong("id_estado"));
				estado.setNombre(rs.getString("nombre"));
				listaEstados.add(estado);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEstadosDeCasos - Problema SQL (close)", e);
			}
		}
		return listaEstados;
	}

    /**
     * @param criterio
     * @return
     */
    public double getCountCasosByCriterio(CasosCriterioDTO criterio) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		double cantidad = 0;		
				
		String sql = " SELECT count(*) cantidad  FROM GPV_CASO C, GPV_JORNADA J, GPV_ESTADO E, BO_LOCALES L WHERE C.ID_JORNADA = J.ID_JORNADA AND C.ID_ESTADO = E.ID_ESTADO AND C.ID_LOCAL = L.ID_LOCAL ";
		
		sql += obtenerWhereCasos(criterio);
		
		logger.debug("* SQL para cantidad de casos con criterio :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    cantidad = rs.getDouble("cantidad");				
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountCasosByCriterio - Problema SQL (close)", e);
			}
		}

        return cantidad;
    }

    /**
     * @param usr
     * @return
     */
    public long getCasoEnEdicionByUsuario(UserDTO usr) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		long idCaso = 0;
		
				
		String sql = " SELECT C.ID_CASO FROM GPV_CASO C WHERE C.ID_USUARIO_EDITOR = " + usr.getId_usuario();
		
		
		logger.debug("* SQL para obtener el caso en edicion :" + sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    idCaso = rs.getLong("id_caso");				
			}

			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCasoEnEdicionByUsuario - Problema SQL (close)", e);
			}
		}
        return idCaso;
    }

    /**
     * @param usr
     * @param idCaso
     * @return
     */
    public boolean setLiberaCaso(long idCaso) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en setLiberaCaso");
			String sql = "UPDATE GPV_CASO SET ID_USUARIO_EDITOR = NULL " +
						 " WHERE ID_CASO = ? ";
			logger.debug(sql);
			
			logger.debug("idCaso:"+idCaso);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idCaso);		
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setLiberaCaso - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param idCaso
     * @return
     */
    public CasoDTO getCasoByIdCaso(long idCaso) throws CasosDAOException {
        CasoDTO caso = new CasoDTO();
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = " SELECT C.id_caso,C.fecha_creacion_caso,C.fecha_entrega_caso,C.fecha_modificacion_caso, " +
                     " C.id_pedido,C.fecha_pedido,C.direccion,C.comuna,C.fecha_despacho,C.cli_rut,C.cli_dv, " +
                     " C.cli_nombre,C.cli_fon_cod_1,C.cli_fon_num_1,C.cli_fon_cod_2,C.cli_fon_num_2,C.cli_fon_cod_3,C.cli_fon_num_3, " +
                     " C.fecha_compromiso_solucion,C.fecha_caso_verificado,C.indicaciones,C.id_usu_fono,C.usu_fono_nombre, " +
                     " C.id_usuario_editor,C.id_usuario_creador,C.id_local,C.id_jornada,C.id_estado,C.satisfaccion_cliente, " +
                     " C.escalamiento,  J.DESCRIPCION JORNADA, E.NOMBRE ESTADO, L.NOM_LOCAL LOCAL, U.LOGIN USUARIO, CR.LOGIN CREADOR," +
                     " C.COMENTARIO_CALL_CENTER " +
					 " FROM GPV_CASO C " +
					 " left outer join GPV_JORNADA J on (C.ID_JORNADA = J.ID_JORNADA) " +
					 " left outer join GPV_ESTADO E on (C.ID_ESTADO = E.ID_ESTADO) " +
					 " left outer join BO_LOCALES L on (C.ID_LOCAL = L.ID_LOCAL) " +
					 " left outer join BO_USUARIOS U on (C.ID_USUARIO_EDITOR = U.ID_USUARIO) " +
					 " left outer join BO_USUARIOS CR on (C.ID_USUARIO_CREADOR = CR.ID_USUARIO) " +
					 " WHERE C.ID_CASO = " + idCaso;
		
		logger.debug("* SQL getCasoByIdCaso :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {				
				caso.setIdCaso(rs.getLong("id_caso"));
				caso.setFcCreacionCaso(rs.getString("fecha_creacion_caso"));
				caso.setFcHoraEntrega(rs.getString("fecha_entrega_caso"));
				caso.setFcModificacionCaso(rs.getString("fecha_modificacion_caso"));
				caso.setIdPedido(rs.getLong("id_pedido"));
				caso.setFcPedido(rs.getString("fecha_pedido"));
				caso.setDireccion(rs.getString("direccion"));
				caso.setComuna(rs.getString("comuna"));
				caso.setFcDespacho(rs.getString("fecha_despacho"));
				caso.setCliRut(rs.getLong("cli_rut"));
				caso.setCliDv(rs.getString("cli_dv"));
				caso.setCliNombre(rs.getString("cli_nombre"));
				caso.setCliFonoCod1(rs.getString("cli_fon_cod_1"));
				caso.setCliFonoNum1(rs.getString("cli_fon_num_1"));
				caso.setCliFonoCod2(rs.getString("cli_fon_cod_2"));
				caso.setCliFonoNum2(rs.getString("cli_fon_num_2"));
				caso.setCliFonoCod3(rs.getString("cli_fon_cod_3"));
				caso.setCliFonoNum3(rs.getString("cli_fon_num_3"));
				caso.setFcCompromisoSolucion(rs.getString("fecha_compromiso_solucion"));
				caso.setFcCasoVerificado(rs.getString("fecha_caso_verificado"));
				caso.setIndicaciones(rs.getString("indicaciones"));
				if (rs.getString("id_usu_fono") == null || rs.getString("id_usu_fono") == "") {
				    caso.setIdUsuFonoCompra(0);
				    caso.setUsuFonoCompraNombre("");
				    caso.setCanalCompra("W");
				} else {
				    caso.setIdUsuFonoCompra(rs.getLong("id_usu_fono"));
				    caso.setUsuFonoCompraNombre(rs.getString("usu_fono_nombre"));
				    caso.setCanalCompra("F");
				}
				caso.setIdUsuarioEdit(rs.getLong("id_usuario_editor"));
				caso.setEditUsuario(rs.getString("usuario"));
				caso.setIdUsuarioCreador(rs.getLong("id_usuario_creador"));
				caso.setCreadorUsuario(rs.getString("creador"));
				ObjetoDTO local = new ObjetoDTO();
				local.setIdObjeto(rs.getLong("id_local"));
				local.setNombre(rs.getString("local"));
				caso.setLocal(local);
				JornadaDTO jornada = new JornadaDTO();
				jornada.setIdJornada(rs.getLong("id_jornada"));
				jornada.setDescripcion(rs.getString("jornada"));
				caso.setJornada(jornada);
				EstadoCasoDTO estado = new EstadoCasoDTO();
				estado.setIdEstado(rs.getLong("id_estado"));
				estado.setNombre(rs.getString("estado"));
				caso.setEstado(estado);
				caso.setAlarma(obtenerTipoAlarmaDeCaso(caso.getFcCompromisoSolucion(),caso.getEstado()));
				caso.setSatisfaccionCliente(rs.getString("satisfaccion_cliente"));
				if (rs.getString("escalamiento") == null || rs.getString("escalamiento").equalsIgnoreCase("N")) {
				    caso.setEscalamiento(false);
				} else {
				    caso.setEscalamiento(true);
				}
                caso.setComentarioCallCenter(rs.getString("COMENTARIO_CALL_CENTER"));
				
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCasoByIdCaso - Problema SQL (close)", e);
			}
		}
        return caso;
    }

    /**
     * @param idCaso
     * @param tipoEnviar
     * @return
     */
    public List getProductosByCasoAndTipo(long idCaso, String tipoAccion) throws CasosDAOException {
            PreparedStatement stm = null;
    		ResultSet rs = null;
    		List productos = new ArrayList();
    		
    		String sql = " SELECT P.id_producto,P.id_caso,P.pp_cantidad,P.pp_unidad,P.pp_descripcion,P.tipo_accion, " +
                         " P.comentario_boc,P.ps_cantidad,P.ps_unidad,P.ps_descripcion,P.pickeador,P.comentario_bol, " +
                         " P.precio,P.id_quiebre,P.id_responsable , Q.nombre quiebre, Q.puntaje, R.nombre responsable " +
    					 " FROM GPV_PRODUCTO P " +
    					 " left outer join GPV_QUIEBRE Q on (P.id_quiebre = Q.id_quiebre) " +
    					 " left outer join GPV_RESPONSABLE R on (P.id_responsable = R.id_responsable) " +
    					 " WHERE P.ID_CASO = " + idCaso;
    		
    		if (!tipoAccion.equalsIgnoreCase("T")) {
    		    sql += " AND P.TIPO_ACCION = '" + tipoAccion + "'";
    		}
    		sql += " ORDER BY P.TIPO_ACCION ";
    		
    		logger.debug("* SQL getProductosByCasoAndEnviar :"+sql);
    		
    		try{
    			conn = this.getConnection();
    			stm = conn.prepareStatement(sql  + " WITH UR");			
    			rs = stm.executeQuery();
    			
    			while (rs.next()) {
    			    ProductoCasoDTO prod = new ProductoCasoDTO();
    			    prod.setIdProducto(rs.getLong("id_producto"));
    			    prod.setIdCaso(rs.getLong("id_caso"));
    			    prod.setPpCantidad(rs.getString("pp_cantidad"));
    			    prod.setPpUnidad(rs.getString("pp_unidad"));
    			    prod.setPpDescripcion(rs.getString("pp_descripcion"));
    			    prod.setTipoAccion(rs.getString("tipo_accion"));
    			    prod.setComentarioBOC(rs.getString("comentario_boc"));
    			    prod.setPsCantidad(rs.getString("ps_cantidad"));
    			    prod.setPsUnidad(rs.getString("ps_unidad"));
    			    prod.setPsDescripcion(rs.getString("ps_descripcion"));
    			    prod.setPickeador(rs.getString("pickeador"));
    			    prod.setComentarioBOL(rs.getString("comentario_bol"));
    			    prod.setPrecio(rs.getLong("precio"));
    			    
    			    QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
    			    if (rs.getString("id_quiebre") != null) {
    			        quiebre.setIdQuiebre(rs.getLong("id_quiebre"));
    			        quiebre.setNombre(rs.getString("quiebre"));
    			        quiebre.setPuntaje(rs.getLong("puntaje"));
    			    }
    			    prod.setQuiebre(quiebre);
    			        			    
    			    ObjetoDTO responsable = new ObjetoDTO();
    			    if (rs.getString("id_responsable") != null) {
    			        responsable.setIdObjeto(rs.getLong("id_responsable"));
    			        responsable.setNombre(rs.getString("responsable"));
    			    }
    			    prod.setResponsable(responsable);
    			    
    			    productos.add(prod);
    			}
    			
    		}catch (SQLException e) {
    			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
                
    		} finally {
    			try {
    				if (rs != null)
    					rs.close();
    				if (stm != null)
    					stm.close();
				releaseConnection();
    			} catch (SQLException e) {
    				logger.error("[Metodo] : getProductosByCasoAndTipo - Problema SQL (close)", e);
    			}
    		}
            return productos;
    }

    /**
     * @param idPedido
     * @return
     */
    public PedidoCasoDTO getPedidoById(long idPedido) throws CasosDAOException {
        PedidoCasoDTO pedido = new PedidoCasoDTO();
        pedido.setExistePedido(false);
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getPedidoById");
		logger.debug("-----------------------------------------------------------------");
		
		try {		
			String sql = " SELECT P.ID_PEDIDO, P.FCREACION FECHA_PEDIDO, " +
						 " P.DIR_TIPO_CALLE ||' '|| P.DIR_CALLE ||' ' || P.DIR_NUMERO ||' '|| P.DIR_DEPTO DIRECCION, " +
						 " C.NOMBRE COMUNA, P.RUT_CLIENTE, P.DV_CLIENTE, P.NOM_CLIENTE, " +
						 " CLI.CLI_FON_COD_1, CLI.CLI_FON_NUM_1, CLI.CLI_FON_COD_2, CLI.CLI_FON_NUM_2, CLI.CLI_FON_COD_3, CLI.CLI_FON_NUM_3," +
						 " P.ID_LOCAL, J.FECHA FECHA_DESPACHO, P.ID_CLIENTE, " +
						 " P.ID_USUARIO_FONO ID_USU_FONO, U.NOMBRE ||' '|| U.APELLIDO USU_FONO_NOMBRE " +
						 " FROM BO_PEDIDOS P " +
						 " left outer join BO_COMUNAS C on (P.ID_COMUNA = C.ID_COMUNA) " +
						 " left outer join FO_CLIENTES CLI on (P.ID_CLIENTE = CLI.CLI_ID) " +
						 " left outer join BO_JORNADA_DESP J on (P.ID_JDESPACHO = J.ID_JDESPACHO)" +
						 " left outer join BO_USUARIOS U on (P.ID_USUARIO_FONO = U.ID_USUARIO) " +
						 " WHERE P.ID_PEDIDO = " + idPedido;
	
			logger.debug("SQL getPedidoById :"+sql);
	    
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    pedido.setExistePedido(true);
			    pedido.setIdPedido(rs.getLong("id_pedido"));
				pedido.setFechaPedido(rs.getString("fecha_pedido"));
				pedido.setFechaDespacho(rs.getString("fecha_despacho"));
				pedido.setDireccion(rs.getString("direccion"));
				pedido.setComuna(rs.getString("comuna"));
				pedido.setCliRut(rs.getLong("rut_cliente"));
				pedido.setCliDv(rs.getString("dv_cliente"));
				pedido.setCliNombre(rs.getString("nom_cliente"));
				pedido.setCliFonCod1(rs.getString("cli_fon_cod_1"));
				pedido.setCliFonNum1(rs.getString("cli_fon_num_1"));
				pedido.setCliFonCod2(rs.getString("cli_fon_cod_2"));
				pedido.setCliFonNum2(rs.getString("cli_fon_num_2"));
				pedido.setCliFonCod3(rs.getString("cli_fon_cod_3"));
				pedido.setCliFonNum3(rs.getString("cli_fon_num_3"));
				pedido.setIdLocal(rs.getLong("id_local"));
				if (rs.getString("id_usu_fono") == null || rs.getString("id_usu_fono") == "") {
				    pedido.setIdUsuFonoCompra(0);
				} else {
				    pedido.setIdUsuFonoCompra(rs.getLong("id_usu_fono"));
				    pedido.setUsuFonoCompraNombre(rs.getString("usu_fono_nombre"));
				}
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPedidoById - Problema SQL (close)", e);
			}
		}		return pedido;
    }

    /**
     * Metodo para marcar que el usuario esta modificando (editando) el caso
     * 
     * @param idCaso
     * @param id_usuario
     * @return
     */
    public boolean setModCaso(long idCaso, long id_usuario) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			
			conn = this.getConnection();
			logger.debug("en setModCaso");
			String sql = "UPDATE GPV_CASO SET ID_USUARIO_EDITOR = ? " + 
						 " WHERE ID_CASO = ? ";
			logger.debug(sql);
			
			logger.debug("idCaso:"+idCaso);
			logger.debug("id_usuario:"+id_usuario);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_usuario);	
			stm.setLong(2, idCaso);	
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}
		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModCaso - Problema SQL (close)", e);
			}
		}

		return true;
    }

    /**
     * @return
     */
    public List getJornadas() throws CasosDAOException {
		List listaJornadas = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getJornadas()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_jornada,descripcion,activado FROM GPV_JORNADA ";

		logger.debug("SQL con criterio :"+sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				JornadaDTO jornada = new JornadaDTO();
				jornada.setIdJornada(rs.getLong("id_jornada"));
				jornada.setDescripcion(rs.getString("descripcion"));
				jornada.setActivado(rs.getString("activado"));
				listaJornadas.add(jornada);
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getJornadas - Problema SQL (close)", e);
			}
		}		return listaJornadas;
    }

    /**
     * @param caso
     * @return
     */
    public long addCaso(CasoDTO caso) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idCaso 	= 0;
		
		String sql = " INSERT INTO GPV_CASO ( " +
					 " fecha_creacion_caso, fecha_modificacion_caso, id_usuario_editor, id_pedido, fecha_pedido, " +
					 " direccion, comuna, id_local, fecha_despacho, cli_rut, " +
					 " cli_dv, cli_nombre, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, " +
					 " cli_fon_num_2, cli_fon_cod_3, cli_fon_num_3, fecha_compromiso_solucion , id_usu_fono, " +
					 " usu_fono_nombre, id_jornada, id_estado, indicaciones, id_usuario_creador, COMENTARIO_CALL_CENTER ) " +
					 " VALUES (?,?,?,?,?,?,?,?,?,?, " +
					 " ?,?,?,?,?,?,?,?,?,?," +
					 " ?,?,?,?,?,? )";

		logger.debug("DAO addCaso");
		logger.debug("SQL: " + sql );
		
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setTimestamp(1, new Timestamp(System.currentTimeMillis()) );
			stm.setDate(2, new java.sql.Date(System.currentTimeMillis()) );
			stm.setLong(3, caso.getIdUsuarioEdit());
			stm.setLong(4, caso.getIdPedido());
			stm.setDate(5, CasosUtil.frmFecha(caso.getFcPedido(),"dd/MM/yyyy"));
			stm.setString(6, caso.getDireccion());
			stm.setString(7, caso.getComuna());
			stm.setLong(8, caso.getLocal().getIdObjeto());
			stm.setDate(9, CasosUtil.frmFecha(caso.getFcDespacho(),"dd/MM/yyyy"));
			stm.setLong(10, caso.getCliRut());
			
			stm.setString(11, caso.getCliDv());
			stm.setString(12, caso.getCliNombre());
			stm.setString(13, caso.getCliFonoCod1());
			stm.setString(14, caso.getCliFonoNum1());
			stm.setString(15, caso.getCliFonoCod2());
			stm.setString(16, caso.getCliFonoNum2());
			stm.setString(17, caso.getCliFonoCod3());
			stm.setString(18, caso.getCliFonoNum3());
			stm.setDate(19, CasosUtil.frmFecha(caso.getFcCompromisoSolucion(),"dd/MM/yyyy"));			
			if (caso.getIdUsuFonoCompra() != 0) {
			    stm.setLong(20, caso.getIdUsuFonoCompra());
			} else {
			    stm.setNull(20, Types.INTEGER);
			}
			
			stm.setString(21, caso.getUsuFonoCompraNombre());
			stm.setLong(22, caso.getJornada().getIdJornada());
			stm.setLong(23, caso.getEstado().getIdEstado());
			stm.setString(24, caso.getIndicaciones());
			stm.setLong(25, caso.getIdUsuarioEdit());
            stm.setString(26, caso.getComentarioCallCenter());
			
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
			idCaso = results.getLong(1);
			logger.debug("id_caso insertado:" + idCaso);
			}			

		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addCaso - Problema SQL (close)", e);
			}
		}
		return idCaso;
    }

    /**
     * @return
     */
    public List getQuiebres()  throws CasosDAOException {
		List listaQuiebres = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getQuiebres()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_quiebre,nombre,puntaje,activado FROM GPV_QUIEBRE ";

		logger.debug("SQL :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
				quiebre.setIdQuiebre(rs.getLong("id_quiebre"));
				quiebre.setNombre(rs.getString("nombre"));
				quiebre.setPuntaje(rs.getLong("puntaje"));
				quiebre.setActivado(rs.getString("activado"));
				listaQuiebres.add(quiebre);
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getQuiebres - Problema SQL (close)", e);
			}
		}		
		return listaQuiebres;
    }    

    /**
     * @return
     */
    public List getResponsables() throws CasosDAOException {
		List listaResponsables = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getResponsables()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_responsable,nombre,activado FROM GPV_RESPONSABLE ";

		logger.debug("SQL :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ObjetoDTO responsable = new ObjetoDTO();
				responsable.setIdObjeto(rs.getLong("id_responsable"));
				responsable.setNombre(rs.getString("nombre"));
				responsable.setActivado(rs.getString("activado"));
				listaResponsables.add(responsable);
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getResponsables - Problema SQL (close)", e);
			}
		}
		return listaResponsables;
    }

    /**
     * @param producto
     * @return
     */
    public long addProductoCaso(ProductoCasoDTO producto) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idProducto 	= 0;
		
		logger.debug("getIdCaso: " + producto.getIdCaso());
		logger.debug("getPpCantidad: " + producto.getPpCantidad());
		logger.debug("getPpUnidad: " + producto.getPpUnidad());
		logger.debug("getPpDescripcion: " + producto.getPpDescripcion());
		logger.debug("getTipoAccion: " + producto.getTipoAccion());
		logger.debug("getComentarioBOC: " + producto.getComentarioBOC());
		logger.debug("getPsCantidad: " + producto.getPsCantidad());
		logger.debug("getPsUnidad: " + producto.getPsUnidad());
		logger.debug("getPsDescripcion: " + producto.getPsDescripcion());
		logger.debug("getComentarioBOL: " + producto.getComentarioBOL());
		logger.debug("getQuiebre: " + producto.getQuiebre().getIdQuiebre());
		logger.debug("getResponsable: " + producto.getResponsable().getIdObjeto());
		logger.debug("getPickeador: " + producto.getPickeador());	
		logger.debug("getPrecio: " + producto.getPrecio());	
		
		
		String sql = " INSERT INTO GPV_PRODUCTO ( " +
					 " id_caso, tipo_accion, pp_descripcion, pp_cantidad, pp_unidad, " +
					 " comentario_boc, ps_descripcion, ps_cantidad, ps_unidad, comentario_bol, " +
					 " id_quiebre, id_responsable, pickeador, precio, id_motivo) " +
					 " VALUES (?,?,?,?,?,?,?,?,?,?, " +
					 " ?,?,?,?,? )";

		logger.debug("DAO addProductoCaso");
		logger.debug("SQL: " + sql );
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setLong(1, producto.getIdCaso() );
			stm.setString(2, producto.getTipoAccion() );
			stm.setString(3, producto.getPpDescripcion() );
			stm.setString(4, producto.getPpCantidad() );
			stm.setString(5, producto.getPpUnidad() );
			stm.setString(6, producto.getComentarioBOC() );
			stm.setString(7, producto.getPsDescripcion() );
			stm.setString(8, producto.getPsCantidad() );
			stm.setString(9, producto.getPsUnidad() );
			stm.setString(10, producto.getComentarioBOL() );
			if (producto.getQuiebre().getIdQuiebre() != 0) {
			    stm.setLong(11, producto.getQuiebre().getIdQuiebre() );
			} else {
			    stm.setNull(11, Types.INTEGER);
			}
			if (producto.getResponsable().getIdObjeto() != 0) {
			    stm.setLong(12, producto.getResponsable().getIdObjeto() );
			} else {
			    stm.setNull(12, Types.INTEGER);
			}
			stm.setString(13, producto.getPickeador() );
			stm.setLong(14, producto.getPrecio() );
            if (producto.getMotivo().getIdObjeto() != 0) {
                stm.setLong(15, producto.getMotivo().getIdObjeto() );
            } else {
                stm.setNull(15, Types.INTEGER);
            }
						
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idProducto = results.getLong(1);
				logger.debug("idProducto insertado:" + idProducto);
			}			
		
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addProductoCaso - Problema SQL (close)", e);
			}
		}
		return idProducto;
    }

    /**
     * @param producto
     * @return
     */
    public boolean modProductoCaso(ProductoCasoDTO producto) throws CasosDAOException {
		PreparedStatement stm = null;

		try {
			conn = this.getConnection();
			logger.debug("en modProductoCaso");
			String sql = " UPDATE GPV_PRODUCTO SET " +
						 " TIPO_ACCION = ?, PP_DESCRIPCION = ?, PP_CANTIDAD = ?, PP_UNIDAD = ?, COMENTARIO_BOC = ?, " +
						 " PS_DESCRIPCION = ?, PS_CANTIDAD = ?, PS_UNIDAD = ?, COMENTARIO_BOL = ?, ID_QUIEBRE = ?, " +
						 " ID_RESPONSABLE = ?, PICKEADOR = ?, PRECIO = ?, ID_MOTIVO = ? " + 
						 " WHERE ID_PRODUCTO = ? AND ID_CASO = ? ";
			logger.debug("modProductoCaso: " + sql);
			
			stm = conn.prepareStatement(sql);
			stm.setString(1, producto.getTipoAccion());	
			stm.setString(2, producto.getPpDescripcion());
			stm.setString(3, producto.getPpCantidad());
			stm.setString(4, producto.getPpUnidad());
			stm.setString(5, producto.getComentarioBOC());
			
			stm.setString(6, producto.getPsDescripcion());
			stm.setString(7, producto.getPsCantidad());
			stm.setString(8, producto.getPsUnidad());
			stm.setString(9, producto.getComentarioBOL());
			if (producto.getQuiebre().getIdQuiebre() == 0) {
			    stm.setNull(10, Types.INTEGER);
			} else {
			    stm.setLong(10, producto.getQuiebre().getIdQuiebre());    
			}
			
			if (producto.getResponsable().getIdObjeto() == 0) {
			    stm.setNull(11, Types.INTEGER);
			} else {
			    stm.setLong(11, producto.getResponsable().getIdObjeto());    
			}
			stm.setString(12, producto.getPickeador());
			stm.setLong(13, producto.getPrecio());
			if (producto.getMotivo().getIdObjeto() == 0) {
                stm.setNull(14, Types.INTEGER);
            } else {
                stm.setLong(14, producto.getMotivo().getIdObjeto());    
            }
            
			stm.setLong(15, producto.getIdProducto());
			stm.setLong(16, producto.getIdCaso());
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modProductoCaso - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param idProducto
     * @return
     */
    public ProductoCasoDTO getProductoById(long idProducto) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		ProductoCasoDTO prod = new ProductoCasoDTO();
		
		String sql = " SELECT P.id_producto,P.id_caso,P.pp_cantidad,P.pp_unidad,P.pp_descripcion, " +
                     " P.tipo_accion,P.comentario_boc,P.ps_cantidad,P.ps_unidad,P.ps_descripcion, " +
                     " P.pickeador,P.comentario_bol,P.precio,P.id_quiebre,P.id_responsable, " +
                     " P.id_motivo, Q.nombre quiebre, Q.puntaje, R.nombre responsable, M.nombre motivo " +
					 " FROM GPV_PRODUCTO P " +
					 " left outer join GPV_QUIEBRE Q on (P.id_quiebre = Q.id_quiebre) " +
					 " left outer join GPV_RESPONSABLE R on (P.id_responsable = R.id_responsable) " +
                     " left outer join GPV_MOTIVO M on (P.id_motivo = M.id_motivo) " +
					 " WHERE P.ID_PRODUCTO = " + idProducto;
		
		logger.debug("* SQL getProductoById :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    prod.setIdProducto(rs.getLong("id_producto"));
			    prod.setIdCaso(rs.getLong("id_caso"));
			    prod.setPpCantidad(rs.getString("pp_cantidad"));
			    prod.setPpUnidad(rs.getString("pp_unidad"));
			    prod.setPpDescripcion(rs.getString("pp_descripcion"));
			    prod.setTipoAccion(rs.getString("tipo_accion"));
			    prod.setComentarioBOC(rs.getString("comentario_boc"));
			    prod.setPsCantidad(rs.getString("ps_cantidad"));
			    prod.setPsUnidad(rs.getString("ps_unidad"));
			    prod.setPsDescripcion(rs.getString("ps_descripcion"));
			    prod.setPickeador(rs.getString("pickeador"));
			    prod.setComentarioBOL(rs.getString("comentario_bol"));
			    prod.setPrecio(rs.getLong("precio"));
			    
			    QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
			    if (rs.getString("id_quiebre") != null) {
			        quiebre.setIdQuiebre(rs.getLong("id_quiebre"));
			        quiebre.setNombre(rs.getString("quiebre"));
			        quiebre.setPuntaje(rs.getLong("puntaje"));
			    }
			    prod.setQuiebre(quiebre);
			        			    
			    ObjetoDTO responsable = new ObjetoDTO();
			    if (rs.getString("id_responsable") != null) {
			        responsable.setIdObjeto(rs.getLong("id_responsable"));
			        responsable.setNombre(rs.getString("responsable"));
			    }
			    prod.setResponsable(responsable);
                
                ObjetoDTO motivo = new ObjetoDTO();
                if (rs.getString("id_motivo") != null) {
                    motivo.setIdObjeto(rs.getLong("id_motivo"));
                    motivo.setNombre(rs.getString("motivo"));
                }
                prod.setMotivo(motivo);
			    
			}
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductoById - Problema SQL (close)", e);
			}
		}
        return prod;
    }

    /**
     * @param idProducto
     * @return
     */
    public boolean delProductoCaso(long idProducto) throws CasosDAOException {
		PreparedStatement stm = null;

		
		try {
			conn = this.getConnection();
			logger.debug("en delProductoCaso");
			
			String sql = " DELETE FROM GPV_PRODUCTO " + 
						 " WHERE ID_PRODUCTO = ? ";
			logger.debug(sql);
			
			logger.debug("idProducto:"+idProducto);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idProducto);	
			
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delProductoCaso - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param caso
     * @return
     */
    public boolean modCaso(CasoDTO caso) throws CasosDAOException {
		PreparedStatement stm = null;
		
		logger.debug("fecha_creacion: " + caso.getFcCreacionCaso());
		logger.debug("fecha_modificacion: " + caso.getFcModificacionCaso());
		logger.debug("id_usuario_editor: " + caso.getIdUsuarioEdit());
		logger.debug("id_pedido: " + caso.getIdPedido());
		logger.debug("fecha_pedido: " + caso.getFcPedido());
		
		logger.debug("direccion: " + caso.getDireccion());
		logger.debug("comuna: " + caso.getComuna());
		logger.debug("id_local: " + caso.getLocal().getIdObjeto());
		logger.debug("fecha_despacho: " + caso.getFcDespacho());
		logger.debug("cli_rut: " + caso.getCliRut());
		
		logger.debug("cli_dv: " + caso.getCliDv());
		logger.debug("cli_nombre: " + caso.getCliNombre());
		logger.debug("cli_fon_cod_1: " + caso.getCliFonoCod1());
		logger.debug("cli_fon_num_1: " + caso.getCliFonoNum1());
		logger.debug("cli_fon_cod_2: " + caso.getCliFonoCod2());
		
		logger.debug("cli_fon_num_2: " + caso.getCliFonoNum2());
		logger.debug("cli_fon_cod_3: " + caso.getCliFonoCod3());
		logger.debug("cli_fon_num_3: " + caso.getCliFonoNum3());
		logger.debug("fecha_compromiso_solucion: " + caso.getFcCompromisoSolucion());
		
		logger.debug("id_jornada: " + caso.getJornada().getIdJornada());
		logger.debug("id_estado: " + caso.getEstado().getIdEstado());
		logger.debug("indicaciones: " + caso.getIndicaciones());
		logger.debug("id_usu_fono: " + caso.getIdUsuFonoCompra());
		logger.debug("usu_fono_nombre: " + caso.getUsuFonoCompraNombre());
		logger.debug("satisfaccion_cliente: " + caso.getSatisfaccionCliente());
				
		logger.debug("id_CASO: " + caso.getIdCaso());
		
		try {
			conn = this.getConnection();
			logger.debug("en delProductoCaso");
			
			String sql = " UPDATE GPV_CASO SET " +
						 " fecha_modificacion_caso=?, id_usuario_editor=?, id_pedido=?, fecha_pedido=?, direccion=?, " +
						 " comuna=?, id_local=?, fecha_despacho=?, cli_rut=?, cli_dv=?, " +
						 " cli_nombre=?, cli_fon_cod_1=?, cli_fon_num_1=?, cli_fon_cod_2=?, cli_fon_num_2=?, " +
						 " cli_fon_cod_3=?, cli_fon_num_3=?, fecha_compromiso_solucion=?, id_jornada=?, id_estado=?, " +
						 " indicaciones=?, id_usu_fono=?, usu_fono_nombre=?, fecha_caso_verificado=?, satisfaccion_cliente=?, " +
						 " fecha_entrega_caso=?, escalamiento=? " +
						 " WHERE id_caso = ? ";
			
			logger.debug(sql);
			
			stm = conn.prepareStatement(sql);
			
			stm.setDate(1, new java.sql.Date(System.currentTimeMillis()) );
			if (caso.getIdUsuarioEdit() != 0 && caso.getEstado().getIdEstado() != CasosEstadosUtil.ANULADO) {
			    stm.setLong(2, caso.getIdUsuarioEdit());
			} else {
			    stm.setNull(2, Types.INTEGER);    
			}
			stm.setLong(3, caso.getIdPedido());
			stm.setDate(4, CasosUtil.frmFecha(caso.getFcPedido(),"yyyy-MM-dd"));			
			stm.setString(5, caso.getDireccion());
			
			stm.setString(6, caso.getComuna());
			stm.setLong(7, caso.getLocal().getIdObjeto());
			stm.setDate(8, CasosUtil.frmFecha(caso.getFcDespacho(),"yyyy-MM-dd"));
			stm.setLong(9, caso.getCliRut());			
			stm.setString(10, caso.getCliDv());
			
			stm.setString(11, caso.getCliNombre());
			stm.setString(12, caso.getCliFonoCod1());
			stm.setString(13, caso.getCliFonoNum1());
			stm.setString(14, caso.getCliFonoCod2());			
			stm.setString(15, caso.getCliFonoNum2());
			
			stm.setString(16, caso.getCliFonoCod3());
			stm.setString(17, caso.getCliFonoNum3());
			stm.setDate(18, CasosUtil.frmFecha(caso.getFcCompromisoSolucion(),"yyyy-MM-dd"));
			if (caso.getJornada().getIdJornada() != 0) {
			    stm.setLong(19, caso.getJornada().getIdJornada());
			} else {
			    stm.setNull(19, Types.INTEGER);    
			}
			if (caso.getEstado().getIdEstado() != 0) {
			    stm.setLong(20, caso.getEstado().getIdEstado());
			} else {
			    stm.setNull(20, Types.INTEGER);    
			}
			
			stm.setString(21, caso.getIndicaciones());
			if (caso.getIdUsuFonoCompra() != 0) {
			    stm.setLong(22, caso.getIdUsuFonoCompra());
			} else {
			    stm.setNull(22, Types.INTEGER);
			}
			stm.setString(23, caso.getUsuFonoCompraNombre());
			if (caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO) {
			    stm.setDate(24, new java.sql.Date(System.currentTimeMillis()));
			} else {
			    stm.setNull(24, Types.DATE);
			}
			stm.setString(25, caso.getSatisfaccionCliente());
			
			if (caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO) {
			    stm.setTimestamp(26, CasosUtil.frmTimestamp(caso.getFcHoraEntrega(),"dd/MM/yyyy HH:mm"));
			} else {
			    stm.setNull(26, Types.DATE);
			}
			if (caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO || caso.getEstado().getIdEstado() == CasosEstadosUtil.ANULADO) {
			    stm.setString(27, "N");
			} else {
			    if (caso.isEscalamiento()) {
			        stm.setString(27, "S");
			    } else {
			        stm.setString(27, "N");    
			    }
			}			
			
			stm.setLong(28, caso.getIdCaso());			
			
			int i = stm.executeUpdate();
			
			if ( i <= 0 ) {	
				return false;
			}

		} catch (SQLException e) {
		    logger.error(e.getMessage());
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modCaso - Problema SQL (close)", e);
			}
		}
		return true;
    }

    /**
     * @param log
     * @return
     */
    public void addLogCaso(LogCasosDTO log) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		
		logger.debug("------------------------------------------------------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"addLogCaso(LogCasosDTO log)");
		logger.debug("------------------------------------------------------------------------------------------------------------");
		
		logger.debug("getIdCaso: " + log.getIdCaso());
		logger.debug("getIdEstado: " + log.getEstado().getIdEstado());
		logger.debug("getUsuario: " + log.getUsuario());
		logger.debug("getDescripcion: " + log.getDescripcion());
		
		//(03-10-2014) - NSepulveda
		logger.debug("No se envia fecha, ya que el campo 'FECHA' tiene un valor por defecto en la base de datos (CURRENT_TIMESTAMP).");
		
		String sql = " INSERT INTO GPV_LOG ( " +
			 	 	 " id_caso, id_estado, usuario, descripcion) " +
			 	 	 " VALUES (?,?,?,?)";
		
		logger.debug("DAO addLogCaso");
		logger.debug("SQL: " + sql );
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql );
			
			stm.setLong(1, log.getIdCaso() );
			stm.setLong(2, log.getEstado().getIdEstado() );
			stm.setString(3, log.getUsuario() );
			stm.setString(4, log.getDescripcion() );
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);

			
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addLogCaso - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idCaso
     * @return
     */
    public List getLogCaso(long idCaso) throws CasosDAOException {
		List listaLog = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getLogCaso()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT L.id_log,L.id_caso,L.id_estado,L.fecha,L.usuario,L.descripcion, " +
                     " E.NOMBRE ESTADO FROM GPV_LOG L " +
					 " left outer join GPV_ESTADO E on (L.ID_ESTADO = E.ID_ESTADO) " +
					 " WHERE L.ID_CASO = ? ORDER BY L.FECHA DESC ";

		logger.debug("SQL getLogCaso: "+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");	
			stm.setLong(1, idCaso );
			rs = stm.executeQuery();
			
			while (rs.next()) {
				LogCasosDTO log = new LogCasosDTO();
				log.setIdLog(rs.getLong("id_log"));
				log.setIdCaso(rs.getLong("id_caso"));
				
			    EstadoCasoDTO estado = new EstadoCasoDTO();
				estado.setIdEstado(rs.getLong("id_estado"));
				estado.setNombre(rs.getString("estado"));				
				log.setEstado(estado);
				
				log.setFecha(rs.getString("fecha"));
				log.setUsuario(rs.getString("usuario"));
				log.setDescripcion(rs.getString("descripcion"));
				listaLog.add(log);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLogCaso - Problema SQL (close)", e);
			}
		}
		return listaLog;
    }

    /**
     * @param idQuiebre
     * @return
     */
    public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
		
		String sql = " SELECT id_quiebre,nombre,puntaje,activado " +
					 " FROM GPV_QUIEBRE Q " +
					 " WHERE Q.ID_QUIEBRE = " + idQuiebre;
		
		logger.debug("* SQL getQuiebreById :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
		        quiebre.setIdQuiebre(rs.getLong("id_quiebre"));
		        quiebre.setNombre(rs.getString("nombre"));
		        quiebre.setPuntaje(rs.getLong("puntaje"));
		        quiebre.setActivado(rs.getString("activado"));
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getQuiebreById - Problema SQL (close)", e);
			}
		}
        return quiebre;
    }

    /**
     * @param idObjeto
     * @return
     */
    public ObjetoDTO getResponsableById(long idResponsable) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		ObjetoDTO responsable = new ObjetoDTO();
		
		String sql = " SELECT id_responsable,nombre,activado " +
					 " FROM GPV_RESPONSABLE R " +
					 " WHERE R.ID_RESPONSABLE = " + idResponsable;
		
		logger.debug("* SQL getResponsableById :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    responsable.setIdObjeto(rs.getLong("id_responsable"));
			    responsable.setNombre(rs.getString("nombre"));
			    responsable.setActivado(rs.getString("activado"));
			}
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getResponsableById - Problema SQL (close)", e);
			}
		}
        return responsable;
    }

    /**
     * @param quiebre
     * @return
     */
    public long addQuiebre(QuiebreCasoDTO quiebre) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idQuiebre 			= 0;
		
		logger.debug("getNombre: " + quiebre.getNombre());
		logger.debug("getPuntaje: " + quiebre.getPuntaje());
		logger.debug("getActivado: " + quiebre.getActivado());
		
		String sql = " INSERT INTO GPV_QUIEBRE ( " +
					 " nombre, puntaje, activado) " +
					 " VALUES (?,?,?)";

		logger.debug("DAO addQuiebre");
		logger.debug("SQL: " + sql );
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setString(1, quiebre.getNombre() );
			stm.setLong(2, quiebre.getPuntaje() );
			stm.setString(3, quiebre.getActivado() );
									
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idQuiebre = results.getLong(1);
				logger.debug("idQuiebre insertado:" + idQuiebre);
			}
		
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
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
		return idQuiebre;
    }

    /**
     * @param quiebre
     */
    public void modQuiebre(QuiebreCasoDTO quiebre) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en modQuiebre");
			String sql = "UPDATE GPV_QUIEBRE SET NOMBRE = ?, PUNTAJE = ?, ACTIVADO = ? " + 
						 " WHERE ID_QUIEBRE = ? ";
			logger.debug(sql);
			
			logger.debug("getIdQuiebre:"+quiebre.getIdQuiebre());
			logger.debug("getNombre:"+quiebre.getNombre());
			logger.debug("getPuntaje:"+quiebre.getPuntaje());
			
			stm = conn.prepareStatement(sql);
			stm.setString(1, quiebre.getNombre());	
			stm.setLong(2, quiebre.getPuntaje());
			stm.setString(3, quiebre.getActivado());
			stm.setLong(4, quiebre.getIdQuiebre());
			stm.executeUpdate();

			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modQuiebre - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idQuiebre
     */
    public void delQuiebre(long idQuiebre) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en delQuiebre");
			
			String sql = " DELETE FROM GPV_QUIEBRE " + 
						 " WHERE ID_QUIEBRE = ? ";
			logger.debug("delQuiebre: " + sql);
			
			logger.debug("idQuiebre:"+idQuiebre);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idQuiebre);	
			
			stm.executeUpdate();

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delQuiebre - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param responsable
     * @return
     */
    public long addResponsable(ObjetoDTO responsable) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idResponsable	 	= 0;
		
		logger.debug("getNombre: " + responsable.getNombre());
		logger.debug("getActivado: " + responsable.getActivado());
		
		String sql = " INSERT INTO GPV_RESPONSABLE (nombre,activado) " +
					 " VALUES (?,?)";

		logger.debug("DAO addResponsable");
		logger.debug("SQL addResponsable: " + sql );
		
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setString(1, responsable.getNombre() );
			stm.setString(2, responsable.getActivado() );
									
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idResponsable = results.getLong(1);
				logger.debug("idResponsable insertado:" + idResponsable);
			}
		
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addResponsable - Problema SQL (close)", e);
			}
		}
		return idResponsable;
    }

    /**
     * @param responsable
     */
    public void modResponsable(ObjetoDTO responsable) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en modResponsable");
			String sql = "UPDATE GPV_RESPONSABLE SET NOMBRE = ?, ACTIVADO = ? " + 
						 " WHERE ID_RESPONSABLE = ? ";
			logger.debug(sql);
			
			logger.debug("getId Responsable:"+responsable.getIdObjeto());
			logger.debug("getNombre:"+responsable.getNombre());
			
			stm = conn.prepareStatement(sql);
			stm.setString(1, responsable.getNombre());
			stm.setString(2, responsable.getActivado());
			stm.setLong(3, responsable.getIdObjeto());
			stm.executeUpdate();

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modResponsable - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idResponsable
     */
    public void delResponsable(long idResponsable) throws CasosDAOException {
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			logger.debug("en delResponsable");
			
			String sql = " DELETE FROM GPV_RESPONSABLE " + 
						 " WHERE ID_RESPONSABLE = ? ";
			logger.debug(sql);
			
			logger.debug("idResponsable:"+idResponsable);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idResponsable);	
			
			stm.executeUpdate();
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delResponsable - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param jornada
     * @return
     */
    public long addJornada(JornadaDTO jornada) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idJornada	 		= 0;
		
		logger.debug("getDescripcion: " + jornada.getDescripcion());
		logger.debug("getActivado: " + jornada.getActivado());		
		
		String sql = " INSERT INTO GPV_JORNADA (descripcion, activado) " +
					 " VALUES (?,?)";

		logger.debug("DAO addJornada");
		logger.debug("SQL addJornada: " + sql );		
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setString(1, jornada.getDescripcion() );
			stm.setString(2, jornada.getActivado() );
									
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idJornada = results.getLong(1);
				logger.debug("idJornada insertado:" + idJornada);
			}
		
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addJornada - Problema SQL (close)", e);
			}
		}
		return idJornada;
    }

    /**
     * @param jornada
     */
    public void modJornada(JornadaDTO jornada) throws CasosDAOException {
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			logger.debug("en modJornada");
			String sql = "UPDATE GPV_JORNADA SET DESCRIPCION = ?, ACTIVADO = ? " + 
						 " WHERE ID_JORNADA = ? ";
			logger.debug(sql);
			
			logger.debug("getIdJornada:"+jornada.getIdJornada());
			logger.debug("getDescripcion:"+jornada.getDescripcion());
			logger.debug("getActivado:"+jornada.getActivado());
			
			stm = conn.prepareStatement(sql);
			stm.setString(1, jornada.getDescripcion());
			stm.setString(2, jornada.getActivado());	
			stm.setLong(3, jornada.getIdJornada());
			stm.executeUpdate();

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modJornada - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idJornada
     * @return
     */
    public JornadaDTO getJornadaCasoById(long idJornada) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		JornadaDTO jornada = new JornadaDTO();
		
		String sql = " SELECT id_jornada,descripcion,activado " +
					 " FROM GPV_JORNADA R " +
					 " WHERE R.ID_JORNADA = " + idJornada;
		
		logger.debug("* SQL getJornadaCasoById :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    jornada.setIdJornada(rs.getLong("id_jornada"));
			    jornada.setDescripcion(rs.getString("descripcion"));
			    jornada.setActivado(rs.getString("activado"));
			}
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getJornadaCasoById - Problema SQL (close)", e);
			}
		}
        return jornada;
    }

    /**
     * @param idJornada
     */
    public void delJornadaDeCaso(long idJornada) throws CasosDAOException {
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			logger.debug("en delJornadaDeCaso");
			
			String sql = " DELETE FROM GPV_JORNADA " + 
						 " WHERE ID_JORNADA = ? ";
			logger.debug(sql);
			
			logger.debug("idJornada:"+idJornada);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idJornada);	
			
			stm.executeUpdate();
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delJornadaDeCaso - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param estado
     * @return
     */
    public List getJornadasByEstado(String estado) throws CasosDAOException {
		List listaJornadas = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getJornadas()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_jornada,descripcion,activado FROM GPV_JORNADA WHERE ACTIVADO = ?";

		logger.debug("SQL con criterio :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setString(1, estado);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				JornadaDTO jornada = new JornadaDTO();
				jornada.setIdJornada(rs.getLong("id_jornada"));
				jornada.setDescripcion(rs.getString("descripcion"));
				jornada.setActivado(rs.getString("activado"));
				listaJornadas.add(jornada);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getJornadasByEstado - Problema SQL (close)", e);
			}
		}
		return listaJornadas;
    }

    /**
     * @param estado
     * @return
     */
    public List getQuiebresByEstado(String estado) throws CasosDAOException {
		List listaQuiebres = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getQuiebresByEstado()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = "SELECT id_quiebre,nombre,puntaje,activado FROM GPV_QUIEBRE WHERE ACTIVADO = ?";

		logger.debug("SQL :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setString(1, estado);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
				quiebre.setIdQuiebre(rs.getLong("id_quiebre"));
				quiebre.setNombre(rs.getString("nombre"));
				quiebre.setPuntaje(rs.getLong("puntaje"));
				quiebre.setActivado(rs.getString("activado"));
				listaQuiebres.add(quiebre);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getQuiebresByEstado - Problema SQL (close)", e);
			}
		}
		return listaQuiebres;
    }

    /**
     * @param estado
     * @return
     */
    public List getResponsablesByEstado(String estado) throws CasosDAOException {
		List listaResponsables = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getResponsables()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_responsable,nombre,activado FROM GPV_RESPONSABLE WHERE ACTIVADO = ?";

		logger.debug("SQL getResponsablesByEstado: "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setString(1, estado);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				ObjetoDTO responsable = new ObjetoDTO();
				responsable.setIdObjeto(rs.getLong("id_responsable"));
				responsable.setNombre(rs.getString("nombre"));
				responsable.setActivado(rs.getString("activado"));
				listaResponsables.add(responsable);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getResponsablesByEstado - Problema SQL (close)", e);
			}
		}
		return listaResponsables;
    }

    /**
     * @param idCaso
     * @return
     */
    public long getIdUsuarioEditorDeCaso(long idCaso) throws CasosDAOException {
		long idUsuario = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getIdUsuarioEditorDeCaso()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT ID_USUARIO_EDITOR FROM GPV_CASO WHERE ID_CASO = ?";

		logger.debug("SQL getIdUsuarioEditorDeCaso: "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, idCaso);
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    idUsuario = rs.getLong("id_usuario_editor");				
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdUsuarioEditorDeCaso - Problema SQL (close)", e);
			}
		}
		return idUsuario;
    }

    /**
     * @param idPedido
     * @return
     */
    public List getCasosByOp(long idPedido) throws CasosDAOException {
		List casos = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getCasosByOp()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT ID_CASO FROM GPV_CASO WHERE ID_PEDIDO = ?";

		logger.debug("SQL getCasosByOp: "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, idPedido);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    casos.add(rs.getString("id_caso"));
			}

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCasosByOp - Problema SQL (close)", e);
			}
		}
		return casos;
    }

    /**
     * @param idCaso
     * @return
     */
    public List getDocBolCasoByCaso(long idCaso) throws CasosDAOException {
		List docs = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getDocBloCasoByCaso()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_doc_bol,id_caso,fecha,usuario,comentario, " +
                     " cooler1,cooler2,cooler3,cooler4,cooler5,cooler6, " +
                     " bin1,bin2,bin3,bin4,bin5,bin6 " +
                     " FROM GPV_DOC_BOL WHERE ID_CASO = ? ORDER BY ID_DOC_BOL DESC";

		logger.debug("SQL getDocBolCasoByCaso: "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, idCaso);
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    CasosDocBolDTO doc = new CasosDocBolDTO();
			    doc.setIdDocBol(rs.getLong("id_doc_bol"));
			    doc.setIdCaso(rs.getLong("id_caso"));
			    doc.setFecha(rs.getString("fecha"));
			    doc.setUsuario(rs.getString("usuario"));
			    doc.setComentario(rs.getString("comentario"));
			    
			    doc.setCooler1(rs.getString("cooler1"));
			    doc.setCooler2(rs.getString("cooler2"));
			    doc.setCooler3(rs.getString("cooler3"));
			    doc.setCooler4(rs.getString("cooler4"));
			    doc.setCooler5(rs.getString("cooler5"));
			    doc.setCooler6(rs.getString("cooler6"));
			    
			    doc.setBin1(rs.getString("bin1"));
			    doc.setBin2(rs.getString("bin2"));
			    doc.setBin3(rs.getString("bin3"));
			    doc.setBin4(rs.getString("bin4"));
			    doc.setBin5(rs.getString("bin5"));
			    doc.setBin6(rs.getString("bin6"));

			    docs.add(doc);
			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
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
		return docs;
    }

    /**
     * @param idDocBolCaso
     * @return
     */
    public CasosDocBolDTO getDocBolCasoById(long idDocBolCaso) throws CasosDAOException {
        CasosDocBolDTO doc = new CasosDocBolDTO();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getDocBolCasoById()");
		logger.debug("-----------------------------------------------------------------");
		
		String sql = " SELECT id_doc_bol,id_caso,fecha,usuario,comentario, " +
                     " cooler1,cooler2,cooler3,cooler4,cooler5,cooler6, " +
                     " bin1,bin2,bin3,bin4,bin5,bin6 " +
                     " FROM GPV_DOC_BOL WHERE ID_DOC_BOL = ?";

		logger.debug("SQL :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, idDocBolCaso);
			rs = stm.executeQuery();
			
			while (rs.next()) {			    
			    doc.setIdDocBol(rs.getLong("id_doc_bol"));
			    doc.setIdCaso(rs.getLong("id_caso"));
			    doc.setFecha(rs.getString("fecha"));
			    doc.setUsuario(rs.getString("usuario"));
			    doc.setComentario(rs.getString("comentario"));
			    
			    doc.setCooler1(rs.getString("cooler1"));
			    doc.setCooler2(rs.getString("cooler2"));
			    doc.setCooler3(rs.getString("cooler3"));
			    doc.setCooler4(rs.getString("cooler4"));
			    doc.setCooler5(rs.getString("cooler5"));
			    doc.setCooler6(rs.getString("cooler6"));
			    
			    doc.setBin1(rs.getString("bin1"));
			    doc.setBin2(rs.getString("bin2"));
			    doc.setBin3(rs.getString("bin3"));
			    doc.setBin4(rs.getString("bin4"));
			    doc.setBin5(rs.getString("bin5"));
			    doc.setBin6(rs.getString("bin6"));

			}

		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDocBolCasoById - Problema SQL (close)", e);
			}
		}
		return doc;
    }

    /**
     * @param caso
     * @return
     */
    public long addDocBolCaso(CasosDocBolDTO caso) throws CasosDAOException {
        PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long idDocBol 	= 0;
		
		logger.debug("getIdCaso: " + caso.getIdCaso());
		logger.debug("getUsuario: " + caso.getUsuario());
		logger.debug("getComentario: " + caso.getComentario());
		logger.debug("getCooler1: " + caso.getCooler1());
		logger.debug("getCooler2: " + caso.getCooler2());
		logger.debug("getCooler3: " + caso.getCooler3());
		logger.debug("getCooler4: " + caso.getCooler4());
		logger.debug("getCooler5: " + caso.getCooler5());
		logger.debug("getCooler6: " + caso.getCooler6());
		logger.debug("getBin1: " + caso.getBin1());
		logger.debug("getBin2: " + caso.getBin2());
		logger.debug("getBin3: " + caso.getBin3());
		logger.debug("getBin4: " + caso.getBin4());
		logger.debug("getBin5: " + caso.getBin5());
		logger.debug("getBin6: " + caso.getBin6());
		
		String sql = " INSERT INTO GPV_DOC_BOL ( " +
					 " id_caso, fecha, usuario, comentario, " +
					 " cooler1, cooler2, cooler3, cooler4, cooler5, cooler6, " +
					 " bin1, bin2, bin3, bin4, bin5, bin6) " +
					 " VALUES (?,?,?,?," +
					 "?,?,?,?,?,?, " +
					 "?,?,?,?,?,?)";

		logger.debug("DAO addProductoCaso");
		logger.debug("SQL: " + sql );
		
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			
			stm.setLong(1, caso.getIdCaso() );
			stm.setDate(2, new java.sql.Date(System.currentTimeMillis()) );
			stm.setString(3, caso.getUsuario() );
			stm.setString(4, caso.getComentario() );
			
			stm.setString(5, caso.getCooler1() );
			stm.setString(6, caso.getCooler2() );
			stm.setString(7, caso.getCooler3() );
			stm.setString(8, caso.getCooler4() );
			stm.setString(9, caso.getCooler5() );
			stm.setString(10, caso.getCooler6() );
			
			stm.setString(11, caso.getBin1() );
			stm.setString(12, caso.getBin2() );
			stm.setString(13, caso.getBin3() );
			stm.setString(14, caso.getBin4() );
			stm.setString(15, caso.getBin5() );
			stm.setString(16, caso.getBin6() );
			
						
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				idDocBol = results.getLong(1);
				logger.debug("idDocBol insertado:" + idDocBol);
			}
		
		} catch (SQLException e) {
		    throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addDocBolCaso - Problema SQL (close)", e);
			}
		}		
		return idDocBol;
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List getTablaReclamosClientes(String fechaIni, String fechaFin, Hashtable llaves) throws CasosDAOException {
		List reclamos = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("--------------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getTablaReclamosClientes()");
		logger.debug("--------------------------------------------------------------------");
		
		String sql = " SELECT C.CLI_RUT rut, C.CLI_DV dv, " +
					 " LTRIM(RTRIM( SUBSTR(CHAR(WEEK(C.FECHA_CREACION_CASO)+100),2,3) ))||''||LTRIM(RTRIM(CHAR(YEAR(C.FECHA_CREACION_CASO)))) llave, " +
					 " SUM(Q.PUNTAJE) acumulado " +
					 " FROM GPV_CASO C " +
					 "	INNER JOIN GPV_PRODUCTO P ON (C.ID_CASO = P.ID_CASO) " +
					 "	INNER JOIN GPV_QUIEBRE Q ON (P.ID_QUIEBRE = Q.ID_QUIEBRE) " +
					 " WHERE C.FECHA_CREACION_CASO >= ? AND C.FECHA_CREACION_CASO <= ? AND" +
					 "	C.ID_ESTADO != 8 " +
					 " GROUP BY C.CLI_RUT, C.CLI_DV, " +
					 "	LTRIM(RTRIM( SUBSTR(CHAR(WEEK(C.FECHA_CREACION_CASO)+100),2,3) ))||''||LTRIM(RTRIM(CHAR(YEAR(C.FECHA_CREACION_CASO)))) " +
					 " ORDER BY C.CLI_RUT ";

		logger.debug("SQL :"+sql);
		
		try {
		    //Hashtable map       = getMapIndexReclamos();
            ReclamosClienteDTO rc = new ReclamosClienteDTO();
            int rut             = 0;            
            int indice          = 0;
            
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setDate(1, CasosUtil.frmFecha(fechaIni,"dd/MM/yyyy") );
			stm.setDate(2, CasosUtil.frmFecha(fechaFin,"dd/MM/yyyy") );
			rs = stm.executeQuery();
			
			String puntos = "0";
			while (rs.next()) {
                if (rut != rs.getInt("rut")) {
                    rc = new ReclamosClienteDTO(llaves.size());
                    rc.setCliRut(rs.getInt("rut"));
                    rc.setDv(rs.getString("dv"));;
                    reclamos.add(rc);                    
                    rut = rs.getInt("rut");
                }
                                
                //seteamos el codigo de horas
                if (rs.getString("acumulado") == null) {
                    puntos = "0";
                } else {
                    puntos = rs.getString("acumulado");
                }
                indice = ((Integer)llaves.get(rs.getString("llave"))).intValue();
                rc.getReclamos()[indice] = puntos;                    
                
            }

		}catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTablaReclamosClientes - Problema SQL (close)", e);
			}
		}		return reclamos;
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroComprasByCliente(long rutCliente) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		long cantidad = 0;		
				
		String sql = " SELECT COUNT(*) NRO_COMPRAS " +
					 " FROM BO_PEDIDOS " +
					 " WHERE RUT_CLIENTE = " + rutCliente + " AND (ID_ESTADO = 8 OR ID_ESTADO = 9 OR ID_ESTADO = 10)";
		
		logger.debug("* SQL getNroComprasByCliente :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    cantidad = rs.getLong("nro_compras");				
			}

			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getNroComprasByCliente - Problema SQL (close)", e);
			}
		}
        return cantidad;
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroCasosByCliente(long rutCliente) throws CasosDAOException {
        PreparedStatement stm = null;
		ResultSet rs = null;
		
		long cantidad = 0;		
				
		String sql = " SELECT COUNT(*) NRO_CASOS " +
					 " FROM GPV_CASO " +
					 " WHERE CLI_RUT = " + rutCliente + " AND ID_ESTADO != 8";
		
		logger.debug("* SQL getNroCasosByCliente :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");			
			rs = stm.executeQuery();
			
			if (rs.next()) {
			    cantidad = rs.getLong("nro_casos");				
			}
			
		} catch (SQLException e) {
			throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getNroCasosByCliente - Problema SQL (close)", e);
			}
		}
        return cantidad;
    }

    /**
     * @return
     */
    public List getMotivos() throws CasosDAOException {
        List listaMotivos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        logger.debug("-----------------------------------------------------------------");
        logger.debug("Ejecución DAO: " + getClass().getName()+"getMotivos()");
        logger.debug("-----------------------------------------------------------------");
        
        String sql = " SELECT id_motivo,nombre,activado FROM GPV_MOTIVO ";

        logger.debug("SQL :"+sql);
        
        try{
            //prepare
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            stm = conn.prepareStatement(sql  + " WITH UR");           
            rs = stm.executeQuery();
            
            while (rs.next()) {
                ObjetoDTO motivo = new ObjetoDTO();
                motivo.setIdObjeto(rs.getLong("id_motivo"));
                motivo.setNombre(rs.getString("nombre"));
                motivo.setActivado(rs.getString("activado"));
                listaMotivos.add(motivo);
            }

        }catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMotivos - Problema SQL (close)", e);
			}
		}
        return listaMotivos;
    }

    /**
     * @param motivo
     * @return
     */
    public long addMotivo(ObjetoDTO motivo) throws CasosDAOException {
        PreparedStatement stm   = null;
        ResultSet results       = null;
        long idMotivo      = 0;
        
        logger.debug("getNombre: " + motivo.getNombre());
        logger.debug("getActivado: " + motivo.getActivado());
        
        String sql = " INSERT INTO GPV_MOTIVO (nombre,activado) " +
                     " VALUES (?,?)";

        logger.debug("DAO addMotivo");
        logger.debug("SQL addMotivo: " + sql );        
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
            
            stm.setString(1, motivo.getNombre() );
            stm.setString(2, motivo.getActivado() );
                                    
            int i = stm.executeUpdate();
            logger.debug("rc: " + i);

            results = stm.getGeneratedKeys();
            
            if (results.next()) {               
                idMotivo = results.getLong(1);
                logger.debug("idMotivo insertado:" + idMotivo);
            }
          
        } catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addMotivo - Problema SQL (close)", e);
			}
		}
		return idMotivo;
    }

    /**
     * @param motivo
     */
    public void modMotivo(ObjetoDTO motivo) throws CasosDAOException {
        PreparedStatement stm = null;
        
        try {
            conn = this.getConnection();
            logger.debug("en modMotivo");
            String sql = "UPDATE GPV_MOTIVO SET NOMBRE = ?, ACTIVADO = ? " + 
                         "WHERE ID_MOTIVO = ? ";
            logger.debug(sql);
            
            logger.debug("getId motivo:"+motivo.getIdObjeto());
            logger.debug("getNombre:"+motivo.getNombre());
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, motivo.getNombre());
            stm.setString(2, motivo.getActivado());
            stm.setLong(3, motivo.getIdObjeto());
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modMotivo - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idMotivo
     * @return
     */
    public ObjetoDTO getMotivoById(long idMotivo) throws CasosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ObjetoDTO motivo = new ObjetoDTO();
        
        String sql = " SELECT id_motivo,nombre,activado " +
                     " FROM GPV_MOTIVO M " +
                     " WHERE M.ID_MOTIVO = " + idMotivo;
        
        logger.debug("* SQL getMotivoById :"+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql  + " WITH UR");           
            rs = stm.executeQuery();
            
            if (rs.next()) {
                motivo.setIdObjeto(rs.getLong("id_motivo"));
                motivo.setNombre(rs.getString("nombre"));
                motivo.setActivado(rs.getString("activado"));
            }

        } catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
            
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMotivoById - Problema SQL (close)", e);
			}
		}
        return motivo;
    }

    /**
     * @param idMotivo
     */
    public void delMotivo(long idMotivo) throws CasosDAOException {
        PreparedStatement stm = null;
        
        try {
            conn = this.getConnection();
            logger.debug("en delMotivo");
            
            String sql = " DELETE FROM GPV_MOTIVO " + 
                         " WHERE ID_MOTIVO = ? ";
            logger.debug(sql);
            
            logger.debug("idMotivo:"+idMotivo);
            
            stm = conn.prepareStatement(sql);
            stm.setLong(1, idMotivo);  
            
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delMotivo - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param estado
     * @return
     */
    public List getMotivosByEstado(String estado) throws CasosDAOException {
        List listaMotivos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;

        logger.debug("-----------------------------------------------------------------");
        logger.debug("Ejecución DAO: " + getClass().getName()+"getMotivosByEstado()");
        logger.debug("-----------------------------------------------------------------");
        
        String sql = " SELECT id_motivo,nombre,activado FROM GPV_MOTIVO WHERE ACTIVADO = ?";

        logger.debug("SQL getMotivosByEstado: "+sql);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(sql  + " WITH UR");
            stm.setString(1, estado);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                ObjetoDTO motivo = new ObjetoDTO();
                motivo.setIdObjeto(rs.getLong("id_motivo"));
                motivo.setNombre(rs.getString("nombre"));
                motivo.setActivado(rs.getString("activado"));
                listaMotivos.add(motivo);
            }

        } catch (SQLException e) {
            throw new CasosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMotivosByEstado - Problema SQL (close)", e);
			}
		}
        return listaMotivos;
    }
    
}
