package cl.bbr.jumbocl.clientes.dao;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import cl.bbr.jumbocl.clientes.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.FormaPagoDTO;
import cl.bbr.jumbocl.clientes.dto.ListaPedidoClienteDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.clientes.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.clientes.dto.ResumenCompraProductosDTO;
import cl.bbr.jumbocl.clientes.dto.RutConfiableDTO;
import cl.bbr.jumbocl.clientes.dto.RutConfiableLogDTO;
import cl.bbr.jumbocl.clientes.dto.SustitutosCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasProductosDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.DuplicateKeyException;
import cl.bbr.jumbocl.common.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.model.ZonaEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.jumbo.interfaces.ventaslocales.CompraCDP;
import cl.jumbo.interfaces.ventaslocales.CompraHistorica;

/**
 * Clase que permite consultar los clientes que se encuentran en la base de datos.
 * @author BBR
 *  
 */

public class JdbcClientesDAO implements ClientesDAO {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    /**
     * Permite la conexión con la base de datos.
     */
    Connection conn = null;

    /**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	
	static final int COD_ERROR_DUPLICATE = -803;
	
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
	 * @throws ClientesDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws ClientesDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ClientesDAOException(e);
		}
	}
	
	/**
	 * Obtiene el listado de clientes
	 * 
	 * @return List de ClienteEntity
	 * @throws ClientesDAOException
	 */
	public List listadoClientes() throws ClientesDAOException {
		List lista_clientes = new ArrayList();
		ClienteEntity cli = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en listadoClientes");
			conn = this.getConnection();
            //[20121114avc
            String sql = "SELECT cli_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, cli_fec_nac, "
                    + " cli_estado, cli_bloqueo "
                    + ", COL_RUT FROM fo_clientes LEFT JOIN fodba.fo_colaborador ON cli_rut = col_rut ";
            //]20121114avc
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				cli = new ClienteEntity();
				cli.setId( new Long(rs.getString("cli_id")));
				cli.setRut(new Long(rs.getString("cli_rut")));
				cli.setDv(rs.getString("cli_dv"));
				cli.setNombre(rs.getString("cli_nombre"));
				cli.setApellido_pat(rs.getString("cli_apellido_pat"));
				cli.setApellido_mat(rs.getString("cli_apellido_mat"));
				cli.setFec_nac(rs.getTimestamp("cli_fec_nac"));
				cli.setEstado(rs.getString("cli_estado"));
				cli.setBloqueo(rs.getString("cli_bloqueo"));

                //[20121107avc
                if (rs.getInt("COL_RUT") == 0)
                    cli.setColaborador(false);
                else
                    cli.setColaborador(true);
                //]20121107avc
				lista_clientes.add(cli);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoClientes - Problema SQL (close)", e);
			}
		}	
		return lista_clientes;
	}

	
	/**
	 * Obtiene el listado de Direcciones
	 * 
	 * @param  cliente_id long
	 * @return List de DireccionEntity
	 * @throws ClientesDAOException
	 */	
		
	public List listadoDirecciones(long cliente_id) throws ClientesDAOException {
		List lista_dirs = new ArrayList();
		DireccionEntity dir = null;
		TipoCalleEntity tip = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		long cli_id = cliente_id;

		try {

			logger.debug("en listadoDirecciones");
			/*String sql = "SELECT dir_id, dir_cli_id, dir_loc_id, dir_com_id, dir_tip_id, dir_alias, " +
					" dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fec_crea, dir_fnueva, " +
					" dir_cuadrante, nom_local, C.nombre nom_com, tip_id, tip_nombre, tip_estado , reg_nombre " +
					" FROM fo_direcciones " +
					" JOIN bo_locales L ON L.id_local = dir_loc_id " +
					" JOIN bo_comunas C ON id_comuna = dir_com_id " +
					//" JOIN bo_zonas Z ON id_zona = dir_zona_id " +
					" JOIN fo_tipo_calle ON tip_id = dir_tip_id " +
					" JOIN bo_regiones R ON R.reg_id = C.reg_id " +
					" WHERE dir_cli_id = ? AND dir_estado<>'E' ";*/
			String SQL = "SELECT D.DIR_ID, D.DIR_CLI_ID, L.ID_LOCAL, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_ALIAS, "
                       + "       D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, "
                       + "       D.DIR_FEC_CREA, D.DIR_FNUEVA, D.DIR_CUADRANTE, L.NOM_LOCAL, C.NOMBRE AS NOM_COMUNA, "
                       + "       TC.TIP_ID, TC.TIP_NOMBRE, TC.TIP_ESTADO, R.REG_NOMBRE "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS     C ON ID_COMUNA     = D.DIR_COM_ID "
                       + "     JOIN FODBA.FO_TIPO_CALLE TC ON TC.TIP_ID     = D.DIR_TIP_ID "
                       + "     JOIN BODBA.BO_REGIONES    R ON R.REG_ID      = C.REG_ID "
                       + "     JOIN BODBA.BO_POLIGONO    P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS       Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "     JOIN BODBA.BO_LOCALES     L ON L.ID_LOCAL    = Z.ID_LOCAL "
                       + "WHERE D.DIR_CLI_ID = ? "
                       + "  AND D.DIR_ESTADO <> 'E' "
                       + "  AND D.ID_POLIGONO IS NOT NULL";
			logger.debug("SQL (listadoDirecciones): " + SQL);
			logger.debug("cliente_id: " + cliente_id);
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");

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
				dir.setLoc_cod(new Long(rs.getString("ID_LOCAL")));
				dir.setCom_id(new Long(rs.getString("dir_com_id")));
				//dir.setZon_id(new Long(rs.getString("dir_zona_id")));
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
				dir.setNom_comuna(rs.getString("NOM_COMUNA"));
				//dir.setNom_zona(rs.getString("nom_zon"));
				dir.setNom_tip_calle(rs.getString("tip_nombre"));
				dir.setNom_region(rs.getString("reg_nombre"));
				lista_dirs.add(dir);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoDirecciones - Problema SQL (close)", e);
			}
		}
		return lista_dirs;
	}

	
	/**
	 * Obtiene cliente a partir de su id
	 * 
	 * @param  cliente_id long
	 * @return ClienteEntity
	 * @throws ClientesDAOException
	 */
	public ClientesDTO getClienteById(long idCliente) throws ClientesDAOException {
        ClientesDTO cli = new ClientesDTO();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getClienteById");
        String sql = "SELECT " +
        		" CLI_ID, CLI_RUT, CLI_DV, CLI_NOMBRE, CLI_APELLIDO_PAT, " +
        		" CLI_APELLIDO_MAT, CLI_CLAVE, CLI_EMAIL, CLI_FON_COD_1," +
        		" CLI_FON_NUM_1, CLI_FON_COD_2, CLI_FON_NUM_2, CLI_FON_COD_3, " +
        		" CLI_FON_NUM_3, CLI_REC_INFO, CLI_FEC_CREA, CLI_ESTADO, " +
        		" CLI_FEC_NAC, CLI_GENERO, CLI_FEC_ACT, CLI_PREGUNTA," +
        		" CLI_RESPUESTA, CLI_ENVIO_SMS, CLI_KEY_RECUPERA_CLAVE, " +
        		" CLI_BLOQUEO, CLI_ENVIO_MAIL, COL_RUT, CLI_MOD_DATO " +
        		" FROM FODBA.FO_CLIENTES LEFT JOIN FODBA.FO_COLABORADOR ON CLI_RUT = COL_RUT WHERE CLI_ID = ?  ";

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, idCliente); 

			rs = stm.executeQuery();
			if (rs.next()) {
				cli.setId_cliente( rs.getLong("cli_id"));
				cli.setRut(rs.getLong("cli_rut"));
				cli.setDv(rs.getString("cli_dv"));
				cli.setNombre(rs.getString("cli_nombre"));
				cli.setPaterno(rs.getString("cli_apellido_pat"));
				cli.setMaterno(rs.getString("cli_apellido_mat"));
				cli.setClave(rs.getString("cli_clave"));
				cli.setEmail(rs.getString("cli_email"));
				cli.setCodfono1(rs.getString("cli_fon_cod_1"));
				cli.setFono1(rs.getString("cli_fon_num_1"));
				cli.setCodfono2(rs.getString("cli_fon_cod_2"));
				cli.setFono2(rs.getString("cli_fon_num_2"));
				cli.setCodfono3(rs.getString("cli_fon_cod_3"));
				cli.setFono3(rs.getString("cli_fon_num_3"));
				cli.setRec_info(rs.getInt("cli_rec_info"));
				if (rs.getString("cli_fec_crea") != null) {
				    cli.setFecCrea(rs.getString("cli_fec_crea"));
                } else {
                    cli.setFecCrea("");    
                }
                if (rs.getString("cli_fec_act") != null) {
                    cli.setFecAct(rs.getString("cli_fec_act"));
                } else {
                    cli.setFecAct("");    
                }
                if (rs.getString("cli_fec_nac") != null) {
                    cli.setFnac(rs.getString("cli_fec_nac"));
                } else {
                    cli.setFnac("");    
                }
                
                cli.setEstado(rs.getString("cli_estado"));
				cli.setGenero(rs.getString("cli_genero"));
				cli.setEst_bloqueo(rs.getString("cli_bloqueo"));
				
				//[20121114avc
                if (rs.getInt("COL_RUT") == 0)
                    cli.setColaborador(false);
                else
                    cli.setColaborador(true);
                //]20121114avc
                
                cli.setCli_mod_dato(rs.getString("CLI_MOD_DATO"));
                cli.setPregunta(rs.getString("CLI_PREGUNTA"));
                cli.setRespuesta(rs.getString("CLI_RESPUESTA"));
                cli.setRecibeSms(rs.getLong("CLI_ENVIO_SMS"));
                cli.setCli_envio_mail(rs.getInt("CLI_ENVIO_MAIL"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getClienteById - Problema SQL (close)", e);
			}
		}
		return cli;
	}

	
	/**
	 * Obtiene cliente a partir de su RUT
	 * 
	 * @param  rut int
	 * @return ClienteEntity
	 * @throws ClientesDAOException
	 */
	public ClienteEntity getClienteByRut(long rut) throws ClientesDAOException {
		ClienteEntity cli = null;
		
		Statement stm = null;
		ResultSet rs = null;

		logger.debug("en getClienteById");
		String sql = "SELECT cli_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " 
                   + "       cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, "
                   + "       cli_fon_cod_3, cli_fon_num_3, cli_rec_info, cli_fec_crea, cli_fec_act, cli_estado, "
                   + "       cli_fec_nac, cli_genero, cli_bloqueo, cli_envio_mail "
                //                 [20121107avc
                + ", COL_RUT FROM fo_clientes LEFT JOIN fo_colaborador ON cli_rut = col_rut "
                //                 ]20121107avc
                   + "WHERE cli_rut = " + rut;
		logger.debug("SQL (getClienteByRut): " + sql);
		
		try {
			conn = this.getConnection();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			if (rs.next()) {		
				cli = new ClienteEntity();
				cli.setId( new Long(rs.getString("cli_id")));
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
				cli.setFon_cod_3(rs.getString("cli_fon_cod_3"));
				cli.setFon_num_3(rs.getString("cli_fon_num_3"));
				//cli.setRec_info(new Integer(0)); 
				cli.setRec_info(new Integer(rs.getString("cli_rec_info")));
				cli.setFec_crea(rs.getTimestamp("cli_fec_crea"));
				cli.setFec_act(rs.getTimestamp("cli_fec_act"));
				cli.setEstado(rs.getString("cli_estado"));
				cli.setFec_nac(rs.getTimestamp("cli_fec_nac"));
				cli.setGenero(rs.getString("cli_genero"));
				cli.setBloqueo(rs.getString("cli_bloqueo"));
				cli.setCli_envio_mail(rs.getInt("cli_envio_mail"));
				//[20121114avc
                if (rs.getInt("COL_RUT") == 0)
                    cli.setColaborador(false);
                else
                    cli.setColaborador(true);
                //]20121114avc
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getClienteByRut - Problema SQL (close)", e);
			}
		}
		return cli;
	}

	
	/**
	 * Obtiene Direccion a partir de su id
	 * 
	 * @param  direccion_id long
	 * @return DireccionEntity
	 * @throws ClientesDAOException
	 */	
	public DireccionEntity getDireccionById(long direccion_id) throws ClientesDAOException {
		DireccionEntity dir = null;
		TipoCalleEntity tip = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		long dir_id = direccion_id;

		try {
			logger.debug("en getDireccionById");
			/*String sql = "SELECT dir_id, dir_cli_id, dir_loc_id, dir_com_id, dir_tip_id, dir_zona_id, dir_alias, " +
					" dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fec_crea, dir_fnueva, " +
					" dir_cuadrante, nom_local, C.nombre nom_com, Z.nombre nom_zon, tip_id, tip_nombre, tip_estado , " +
					" reg_nombre, DIR_CONFLICTIVA, DIR_CONFLICTIVA_COMENTARIO, L.TIPO_PICKING, id_poligono " +
					" FROM fo_direcciones " +
					" JOIN bo_locales L ON L.id_local = dir_loc_id " +
					" JOIN bo_comunas C ON id_comuna = dir_com_id " +
					" JOIN fo_tipo_calle ON tip_id = dir_tip_id " +
					" JOIN bo_zonas Z ON id_zona = dir_zona_id " +
					" JOIN bo_regiones R ON R.reg_id = C.reg_id " +
					" WHERE dir_id = ? ";*/
			String SQL = "SELECT D.DIR_ID, D.DIR_CLI_ID, L.ID_LOCAL, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_ALIAS, "
                       + "       D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, " 
                       + "       D.DIR_FEC_CREA, D.DIR_FNUEVA, D.DIR_CUADRANTE, D.DIR_CONFLICTIVA, " 
                       + "       D.DIR_CONFLICTIVA_COMENTARIO, L.NOM_LOCAL, C.NOMBRE AS NOM_COMUNA, " 
                       + "       Z.NOMBRE AS NOM_ZONA, TC.TIP_ID, TC.TIP_NOMBRE, TC.TIP_ESTADO, " 
                       + "       R.REG_NOMBRE, L.TIPO_PICKING, P.ID_POLIGONO, P.ID_ZONA "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS     C ON C.ID_COMUNA   = D.DIR_COM_ID "
                       + "     JOIN FODBA.FO_TIPO_CALLE TC ON TC.TIP_ID     = D.DIR_TIP_ID "
                       + "     JOIN BODBA.BO_REGIONES    R ON R.REG_ID      = C.REG_ID "
                       + "     JOIN BODBA.BO_POLIGONO    P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS       Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "     JOIN BODBA.BO_LOCALES     L ON L.ID_LOCAL    = Z.ID_LOCAL "
                       + "WHERE D.DIR_ID = ?";
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, dir_id); 
			logger.debug("SQL (getDireccionById): " + SQL);
			logger.debug("dir_id: " + dir_id);
			rs = stm.executeQuery();
			if (rs.next()) {
				dir = new DireccionEntity();
				tip = new TipoCalleEntity();
				tip.setId(new Long(rs.getString("tip_id")));
				tip.setNombre(rs.getString("tip_nombre"));
				tip.setEstado(rs.getString("tip_estado"));
				dir.setTipo_calle(tip);
				dir.setId( new Long(rs.getString("dir_id")));
				dir.setCli_id(new Long(rs.getString("dir_cli_id")));
				dir.setLoc_cod(new Long(rs.getString("ID_LOCAL")));
				dir.setCom_id(new Long(rs.getString("dir_com_id")));
				dir.setZon_id(new Long(rs.getString("ID_ZONA")));
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
				dir.setNom_comuna(rs.getString("NOM_COMUNA"));
				dir.setNom_zona(rs.getString("NOM_ZONA"));
				dir.setNom_tip_calle(rs.getString("tip_nombre"));
				dir.setNom_region(rs.getString("reg_nombre"));
				dir.setDir_conflictiva(rs.getString("DIR_CONFLICTIVA"));
				dir.setDir_conflictiva_comentario(rs.getString("DIR_CONFLICTIVA_COMENTARIO"));
				dir.setTipoPicking(rs.getString("TIPO_PICKING"));
				dir.setId_poligono(rs.getInt("id_poligono"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDireccionById - Problema SQL (close)", e);
			}
		}
		logger.debug("dir:"+dir);
		return dir;
	}

	
	/**
	 * Obtiene listado de clientes de acuerdo a criterio de búsqueda
	 * 
	 * @param  criteria ClienteCriteriaDTO
	 * @return List de ClienteEntity's
	 * @throws ClientesDAOException
	 */
	public List listadoClientesByCriteria( ClienteCriteriaDTO criteria) throws ClientesDAOException {
		List lista_clientes = new ArrayList();
		//-------------
		ClienteEntity cli = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		 //(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		int countFiltros = 0;
		
		//variables de búsqueda para consultar en BD;
		//Apellido
		String apellidoSql = "";
		String apellido = "";
		if (criteria.getApellido() != null && !criteria.getApellido().equals("")){
			countFiltros = countFiltros + 1;
		    apellido = criteria.getApellido().toUpperCase();
		    //(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		    apellidoSql = " (upper(cli_apellido_pat) like \'" + apellido
		                + "%\' OR " + "upper(cli_apellido_mat) like \'" + apellido
		                + "%\')";
		}
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		//Email
		String emailSql = "";
		String email = "";
		if (criteria.getEmail() != null && !criteria.getEmail().equals("")){
			countFiltros = countFiltros + 1;
			email = criteria.getEmail().toUpperCase();
			emailSql = " (upper(cli_email) like \'%" + email + "\')";
		}
		
		//RUT
		String rut = criteria.getRut();
		logger.debug("Esto es lo que trae criteria.getRut: "+ criteria.getRut());
		String rutSql = "";
        if (rut != null && !rut.equals("") &&!rut.equals(" ")){
        	countFiltros = countFiltros + 1;
            rutSql = " cli_rut = " + rut;
        }
		
		//Estado
		String est_bloqueo = criteria.getEst_bloqueo();
		String estadoSql = "";
		if (est_bloqueo != null && !est_bloqueo.equals("")){
			countFiltros = countFiltros + 1;
			estadoSql = " cli_bloqueo = '" + est_bloqueo + "' ";
		}
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		String andSql = " AND ";
		String whereSql = "";
		
		if(countFiltros > 0){
			whereSql = " WHERE " + apellidoSql + emailSql + rutSql;
			
			String concat = "";
			concat = apellidoSql + emailSql + rutSql;
			
			if(!estadoSql.equalsIgnoreCase("")){
				if(!concat.equalsIgnoreCase("")){
					whereSql = whereSql + andSql + estadoSql;
				}else{
					whereSql = whereSql + estadoSql;
				}
			}
		}
		
		logger.debug("Variable WHERE, whereSql : ["+whereSql+"]");

		int pag = criteria.getPagina();
		int regXpag = criteria.getRegsperpage();
        if (pag < 0)
            pag = 1;
        if (regXpag < 10)
            regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		logger.debug("email:"+email);
		logger.debug("apellido:"+apellido);
		logger.debug("rut:"+rut);
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		logger.debug("est_bloqueo:"+est_bloqueo);
		
		logger.debug("en listadoClientesByCriteria");
        String sql = " SELECT * FROM ( "
                + " SELECT row_number() over(order by cli_id) as row,"
                + "  cli_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, "
                + "  cli_fec_nac, cli_estado, cli_bloqueo, emp_rzsocial, cli_emp_id "
                +
                //[20121107avc
                " , COL_RUT FROM fo_clientes LEFT JOIN fo_colaborador ON cli_rut = col_rut"
                +
                //]20121107avc
                " LEFT JOIN ve_empresa ON cli_emp_id = emp_id "
                //(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
                + whereSql + ") AS TEMP "
                + "WHERE row BETWEEN " + iniReg + " AND " + finReg;
		
		logger.debug("SQL: " + sql);
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql + " WITH UR");
		
			rs = stm.executeQuery();
			while (rs.next()) {
				cli = new ClienteEntity();
				cli.setId( new Long(rs.getString("cli_id")));
				cli.setRut(new Long(rs.getString("cli_rut")));
				cli.setDv(rs.getString("cli_dv"));
				cli.setNombre(rs.getString("cli_nombre"));
				cli.setApellido_pat(rs.getString("cli_apellido_pat"));
				cli.setApellido_mat(rs.getString("cli_apellido_mat"));
				cli.setFec_nac(rs.getTimestamp("cli_fec_nac"));
				cli.setEstado(rs.getString("cli_estado"));
				cli.setBloqueo(rs.getString("cli_bloqueo"));
				cli.setRzs_empresa(rs.getString("emp_rzsocial"));
				if (rs.getString("cli_emp_id")!=null){
					cli.setId_empresa(new Long(rs.getString("cli_emp_id")));
					}
				//[20121114avc
                if (rs.getInt("COL_RUT") == 0)
                    cli.setColaborador(false);
                else
                    cli.setColaborador(true);
                //]20121114avc
				lista_clientes.add(cli);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoClientesByCriteria - Problema SQL (close)", e);
			}
		}
		return lista_clientes;
	}

	
	/**
	 * Obtiene número de registros de una query por criterio
	 * 
	 * @param  criteria ClienteCriteriaDTO
	 * @return int número de registros total de la consulta por criteria
	 * @throws ClientesDAOException
	 */
	public int listadoClientesCountByCriteria(ClienteCriteriaDTO criteria) throws ClientesDAOException {
		int numCli = 0;
		//-------------
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		int countFiltros = 0;
		
		//variables de búsqueda para consultar en BD;
		//Apellido
		String apellidoSql = "";
		String apellido = "";
		
		if (criteria.getApellido() != null && !criteria.getApellido().equals("")){
			countFiltros = countFiltros + 1;
			apellido = criteria.getApellido().toUpperCase();
			//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
			apellidoSql = " (upper(cli_apellido_pat) like \'" + apellido
		                + "%\' OR " + "upper(cli_apellido_mat) like \'" + apellido
		                + "%\')";
		}
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		//Email
		String emailSql = "";
		String email = "";
		if (criteria.getEmail() != null && !criteria.getEmail().equals("")){
			countFiltros = countFiltros + 1;
			email = criteria.getEmail().toUpperCase();
			emailSql = " (upper(cli_email) like \'%" + email + "\')";
		}
		
		//RUT
		String rut = criteria.getRut();
		String rutSql = "";
        if (rut != null && !rut.equals("") && !rut.equals(" ")){
        	countFiltros = countFiltros + 1;
            rutSql = " cli_rut = " + rut;
        }
		
		//Estado
		String est_bloqueo = criteria.getEst_bloqueo();
		String estadoSql = "";
		if (!est_bloqueo.equals("")){
			countFiltros = countFiltros + 1;
			estadoSql = " cli_bloqueo = '" + est_bloqueo + "' ";
		}
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		String andSql = " AND ";
		String whereSql = "";
		
		if(countFiltros > 0){
			whereSql = " WHERE " + apellidoSql + emailSql + rutSql;
			
			String concat = "";
			concat = apellidoSql + emailSql + rutSql;
			
			if(!estadoSql.equalsIgnoreCase("")){
				if(!concat.equalsIgnoreCase("")){
					whereSql = whereSql + andSql + estadoSql;
				}else{
					whereSql = whereSql + estadoSql;
				}
			}
		}
		logger.debug("Variable WHERE, whereSql : ["+whereSql+"]");
		
		logger.debug("email:"+email);
		logger.debug("apellido:"+apellido);
		logger.debug("rut:"+rut);
		logger.debug("est_bloqueo:"+est_bloqueo);
		
		logger.debug("en listadoClientesCountByCriteria");
		String sql = " SELECT count(cli_id) as cantidad"+ 
			" FROM fo_clientes " +
			" LEFT JOIN ve_empresa ON cli_emp_id = emp_id "
			//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
			+ whereSql;
		
		logger.debug("Sql: "+ sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR") ;

			rs = stm.executeQuery();
			if (rs.next()) {
				numCli = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema listadoClientesCountByCriteria:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoClientesCountByCriteria - Problema SQL (close)", e);
			}
		}
		return numCli;
	}

	
	
	
	/**
	 * Actualiza una direccion de acuerdo a su id
	 * 
	 * @param  param ProcModAddrBookDTO
	 * @return boolean
	 * @throws ClientesDAOException
	 */
	public boolean actualizaDireccionById(ProcModAddrBookDTO param) throws ClientesDAOException {
		
		PreparedStatement stm = null;
		boolean result = false;
		long id_dir = param.getDir_id();
		
		try {
			String sql = "UPDATE fo_direcciones " +
				" SET dir_com_id = " + param.getId_comuna() +
				" , id_poligono = " + param.getId_poligono() +
				" , dir_tip_id = " + param.getId_tipo_calle() +
				" , dir_alias = \'"  + param.getAlias() + "\' " +
				" , dir_calle = \'"  + param.getNom_calle() + "\' " +
				" , dir_numero = \'" + param.getNum_calle() + "\' " +
				" , dir_depto = \'"  + param.getNum_depto() + "\' " +
				" , dir_comentarios = \'" + param.getComentarios() + "\' " +
				" , DIR_CONFLICTIVA = '" + param.getDir_conflictiva() + "' " +
				" , DIR_CONFLICTIVA_COMENTARIO = '" + param.getDir_conflictiva_comentario() + "' " +
				" , CONFIRMADA = 0 " +
				" WHERE dir_id = ? ";
			conn = this.getConnection();
			
			logger.debug("en actualizaDireccionById");
			logger.debug(sql+"id_dir:"+id_dir);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_dir);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

			
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaDireccionById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/**
	 * Obtiene un listado de zonas
	 * 
	 * @return List de ZonaEntity's
	 * @throws ClientesDAOException
	 */	
	
	public List listadoZonas() throws ClientesDAOException{
		List lista_zonas = new ArrayList();
		ZonaEntity zon = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en listadoZonas");
			String sql = "SELECT id_zona, id_local, nombre, descripcion, tarifa_normal_baja, tarifa_normal_media, "
                       + "       tarifa_normal_alta, tarifa_economica, tarifa_express, "
                       + "       estado_tarifa_economica, estado_tarifa_express "
                       + "FROM bo_zonas";
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				zon = new ZonaEntity();
				zon.setId_zona(new Long(rs.getString("id_zona")));
				zon.setId_local(new Long(rs.getString("id_local")));
				zon.setNombre(rs.getString("nombre"));
				zon.setDescrip(rs.getString("descripcion"));
				zon.setTarifa_normal_alta(rs.getInt("tarifa_normal_alta"));
				zon.setTarifa_normal_media(rs.getInt("tarifa_normal_media"));
				zon.setTarifa_normal_baja(rs.getInt("tarifa_normal_baja"));
				zon.setTarifa_economica(rs.getInt("tarifa_economica"));
				zon.setTarifa_express(rs.getInt("tarifa_express"));
				zon.setEstado_tarifa_economica(rs.getInt("estado_tarifa_economica"));
				zon.setEstado_tarifa_express(rs.getInt("estado_tarifa_express"));
				lista_zonas.add(zon);
			}

		} catch (Exception e) {
			logger.debug("Problema listadoZonas:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoZonas - Problema SQL (close)", e);
			}
		}
		logger.debug("cant:"+lista_zonas.size());
		return lista_zonas;
	}

	
	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public List listadoLocales() throws ClientesDAOException{
		List lista_locales = new ArrayList();
		LocalEntity loc = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en listadoLocales");			
			String sql = "SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios, "
                    + "       cod_local_pos, tipo_flujo, cod_local_promo, tipo_picking, retiro_local, id_zona_retiro "
                    + "FROM bo_locales "
                    + "ORDER BY orden ASC";
			
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				loc = new LocalEntity();
				loc.setId_local(new Long(rs.getString("id_local")));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				loc.setOrden(new Integer(rs.getString("orden")));
				loc.setFec_carga_prec(rs.getTimestamp("fecha_carga_precios"));
			    if (rs.getString("cod_local_pos") != null){
			        loc.setCod_local_pos(new Integer(rs.getString("cod_local_pos")));
			    }
				loc.setTipo_flujo(rs.getString("tipo_flujo"));
				loc.setCod_local_promocion(rs.getString("cod_local_promo"));
				loc.setTipo_picking(rs.getString("tipo_picking"));
				loc.setRetirolocal(rs.getString("retiro_local"));
				loc.setId_zona_retiro(rs.getInt("id_zona_retiro"));
				
				lista_locales.add(loc);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoLocales - Problema SQL (close)", e);
			}
		}
		return lista_locales;
	}

	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List de ComunaEntity's
	 * @throws ClientesDAOException
	 */	
	public List listadoComunas() throws ClientesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT id_comuna, C.reg_id reg_id, nombre , reg_nombre" +
					" FROM bo_comunas C " +
					" Join bo_regiones R ON R.reg_id = C.reg_id ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("id_comuna")));
				com.setId_region(new Long(rs.getString("reg_id")));
				com.setNombre(rs.getString("nombre"));
				com.setReg_nombre(rs.getString("reg_nombre"));
				lista_comunas.add(com);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoComunas - Problema SQL (close)", e);
			}
		}
		return lista_comunas;
	}

	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List de ComunaEntity's
	 * @throws ClientesDAOException
	 */
	public List getComunasConPoligonos() throws ClientesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT C.ID_COMUNA, R.REG_ID, C.NOMBRE, R.REG_NOMBRE, "
                       + "       COUNT(P.ID_POLIGONO) AS CANT_POLIGONOS "
                       + "FROM BODBA.BO_COMUNAS C "
                       + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID = C.REG_ID "
                       + "     JOIN BODBA.BO_POLIGONO P ON P.ID_COMUNA = C.ID_COMUNA AND "
                       + "                                 P.ID_ZONA > 0 AND "
                       + "                                 P.NUM_POLIGONO <> " + Constantes.NUM_POLIGONO_RETIRO_LOCAL + " "
                       + "GROUP BY C.ID_COMUNA, R.REG_ID, C.NOMBRE, R.REG_NOMBRE";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("ID_COMUNA")));
				com.setId_region(new Long(rs.getString("REG_ID")));
				com.setNombre(rs.getString("NOMBRE"));
				com.setReg_nombre(rs.getString("REG_NOMBRE"));
				lista_comunas.add(com);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunasConPoligonos - Problema SQL (close)", e);
			}
		}
		return lista_comunas;
	}

	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List de ComunaEntity's
	 * @throws ClientesDAOException
	 */	
	public String getComunaById(long id_comuna) throws ClientesDAOException {
		String comuna = "";
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT C.NOMBRE "
                       + "FROM BODBA.BO_COMUNAS C "
                       + "WHERE C.ID_COMUNA = " + id_comuna;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				comuna = rs.getString("NOMBRE");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunaById - Problema SQL (close)", e);
			}
		}
		return comuna;
	}

	/**
	 * Obtiene un listado de comunas de Facturación
	 * 
	 * @return List de ComunaEntity's
	 * @throws ClientesDAOException
	 */	
	public List listadoComunasFact() throws ClientesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT CG.COMUNA_ID, CG.REG_ID, CG.NOMBRE, R.REG_NOMBRE "
	                   + "FROM BO_COMUNAS_GENERAL CG "
	                   + "     JOIN BO_REGIONES R ON R.REG_ID = CG.REG_ID ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("COMUNA_ID")));
				com.setId_region(new Long(rs.getString("REG_ID")));
				com.setNombre(rs.getString("NOMBRE"));
				com.setReg_nombre(rs.getString("REG_NOMBRE"));
				lista_comunas.add(com);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoComunasFact - Problema SQL (close)", e);
			}
		}
		return lista_comunas;
	}
	
	
	/**
	 * Obtiene un listado de Tipo de Calles
	 * 
	 * @return List de TipoCalleEntity's
	 * @throws ClientesDAOException
	 */	
	public List listadoTiposCalle() throws ClientesDAOException {
		List lista_tipos = new ArrayList();
		TipoCalleEntity tip = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT tip_id, tip_nombre, tip_estado "
				+ "FROM fo_tipo_calle";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				tip = new TipoCalleEntity();
				tip.setId(new Long(rs.getString("tip_id")));
				tip.setNombre(rs.getString("tip_nombre"));
				tip.setEstado(rs.getString("tip_estado"));
				lista_tipos.add(tip);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoTiposCalle - Problema SQL (close)", e);
			}
		}
		return lista_tipos;
	}

	/**
	 * Obtiene un listado de estados de acuerdo al tipo de estado y si es visible o no en la web
	 * 
	 * @param  tip_estado String
	 * @param  visible String
	 * @return List de EstadoEntity's
	 * @throws ClientesDAOException
	 */	
	public List getEstados(String tip_estado,String visible) throws ClientesDAOException {
		List list_estado = new ArrayList();
		EstadoEntity est = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		//	visible
		String sqlWhere = "";
		if(!visible.equals(""))
			sqlWhere = " AND visible='"+visible+"' " ;
		
		try {
			String sql = " SELECT id, estado, tipo " +
				" FROM fo_estados " +
				" WHERE tipo='"+tip_estado+"' " + sqlWhere ;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			logger.debug(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				est = new EstadoEntity();
				est.setId(new Character(rs.getString("id").charAt(0)));
				est.setEstado(rs.getString("estado"));
				est.setTipo(rs.getString("tipo"));
				list_estado.add(est);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEstados - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_estado.size());
		
		return list_estado;
	}

	
	/**
	 * Cambia el estado de bloqueo del cliente
	 * 
	 * @param  id_cliente long
	 * @param  estado String
	 * @throws ClientesDAOException
	 */	
	public void updBloqueoCliente(long id_cliente, String estado) throws ClientesDAOException {
		
		PreparedStatement stm = null;
		
		String sql =
			"UPDATE fo_clientes " +
			"   SET cli_bloqueo = ? " +
			" WHERE cli_id = ? ";
		
		logger.debug("en setBloqClient");
		logger.debug("SQL: " + sql);
		logger.debug("id_cliente: " + id_cliente);
		logger.debug("estado: " + estado);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, estado);
			stm.setLong(2, id_cliente);
			
			int i = stm.executeUpdate();
			
			logger.debug("rc: " + i);

			
		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updBloqueoCliente - Problema SQL (close)", e);
			}
		}
	}
	
	/**
	 * Obtiene los id de cliente de acuerdo al rut o apellido
	 * 
	 * @param  rut String
	 * @param  apellido String
	 * @return long
	 * @throws ClientesDAOException
	 */	

	public long getClienteByTips(String rut, String apellido) throws ClientesDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		long id=-1;
		try {
			String sqlRut = "";
			String sqlApe = "";
			if(rut!=null && !rut.equals(""))
				sqlRut = " AND cli_rut = "+rut;
			if(apellido!=null && !apellido.equals(""))
				sqlApe = "upper(cli_apellido_pat) like \'"+apellido+"%\' ";
			
			String sql = "SELECT cli_id "+
				" FROM fo_clientes " +
				" WHERE cli_id>0 "+ sqlApe + sqlRut;
			
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("en getClienteByTips");
			logger.debug(sql);
			rs = stm.executeQuery();
			if(rs.next()) {
				id = rs.getLong("cli_id");
			}

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getClienteByTips - Problema SQL (close)", e);
			}
		}
		return id;
	}

	
/**
 * Obtiene un listado de comunas de acuerdo a un id de zona
 * 
 * @param  id_zona long
 * @return List de ComunaEntity's
 * @throws ClientesDAOException
 */	
	/*public List getComunasByZona(long id_zona) throws ClientesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			String sql = " SELECT C.id_comuna id_com, C.reg_id reg_id, nombre, reg_nombre " +
					" FROM bo_comunas C " +
					" JOIN bo_regiones R ON R.reg_id = C.reg_id " +
					" JOIN bo_comunaxzona CZ ON CZ.id_comuna=C.id_comuna " +
					" WHERE CZ.id_zona=? " ;
			conn = this.getConnection();
			stm = conn
					.prepareStatement(sql);
			stm.setLong(1,id_zona);

			logger.debug(sql+",id_zona:"+id_zona);
			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("id_com")));
				com.setId_region(new Long(rs.getString("reg_id")));
				com.setNombre(rs.getString("nombre"));
				com.setReg_nombre(rs.getString("reg_nombre"));
				lista_comunas.add(com);
			}
			rs.close();
			stm.close();
			releaseConnection();
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
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
		logger.debug("cant en lista:"+lista_comunas.size());
		
		return lista_comunas;
	}*/

	/**
	 * Obtiene un listado de Zonas de acuerdo a un id de comuna
	 * 
	 * @param  id_comuna long
	 * @return List de ZonaEntity's
	 * @throws ClientesDAOException
	 */	
	public List getZonasByComuna(long id_comuna) throws ClientesDAOException {
		List lst_zonas = new ArrayList();
		ZonaEntity zon = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			logger.debug("en getZonasByComuna");
			String sql = "SELECT DISTINCT Z.ID_ZONA, Z.ID_LOCAL, Z.NOMBRE, Z.DESCRIPCION, "
                       + "       Z.TARIFA_NORMAL_BAJA, Z.TARIFA_NORMAL_MEDIA, Z.TARIFA_NORMAL_ALTA, "
                       + "       Z.TARIFA_ECONOMICA, Z.TARIFA_EXPRESS, "
                       + "       Z.ESTADO_TARIFA_ECONOMICA, Z.ESTADO_TARIFA_EXPRESS "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                       + "WHERE P.ID_COMUNA = ?" ;
			logger.debug(sql);
			logger.debug("id_comuna:"+id_comuna);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comuna);

			rs = stm.executeQuery();
			while (rs.next()) {
				zon = new ZonaEntity();
				zon.setId_zona(new Long(rs.getString("ID_ZONA")));
				zon.setId_local(new Long(rs.getString("ID_LOCAL")));
				zon.setNombre(rs.getString("NOMBRE"));
				zon.setDescrip(rs.getString("DESCRIPCION"));
				zon.setTarifa_normal_alta(rs.getInt("TARIFA_NORMAL_ALTA"));
				zon.setTarifa_normal_media(rs.getInt("TARIFA_NORMAL_MEDIA"));
				zon.setTarifa_normal_baja(rs.getInt("TARIFA_NORMAL_BAJA"));
				zon.setTarifa_economica(rs.getInt("TARIFA_ECONOMICA"));
				zon.setTarifa_express(rs.getInt("ESTADO_TARIFA_EXPRESS"));
				zon.setEstado_tarifa_economica(rs.getInt("ESTADO_TARIFA_ECONOMICA"));
				zon.setEstado_tarifa_express(rs.getInt("ESTADO_TARIFA_EXPRESS"));
				lst_zonas.add(zon);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getZonasByComuna - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_zonas.size());
		
		return lst_zonas;
	}

	/**
	 * Obtiene un listado de locales de acuedo a un id de zona
	 * 
	 * @param  id_zona long
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public List getLocalesByZona(long id_zona) throws ClientesDAOException {
		
		List lista_locales = new ArrayList();
		LocalEntity loc = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
	
		try {
			String sql = "SELECT L.id_local id_loc, cod_local, nom_local, orden, fecha_carga_precios, L.retiro_local, L.direccion, L.id_zona_retiro  " +
					" FROM bo_locales L, bo_zonas Z" +
					" WHERE L.id_local = Z.id_local AND Z.id_zona=?";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_zona);

			logger.debug(sql+",id_zona:"+id_zona);
			rs = stm.executeQuery();
			while (rs.next()) {
				loc = new LocalEntity();
				loc.setId_local(new Long(rs.getString("id_loc")));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				loc.setOrden(new Integer(rs.getString("orden")));
				loc.setFec_carga_prec(rs.getTimestamp("fecha_carga_precios"));
				loc.setRetirolocal(rs.getString("retiro_local"));
				loc.setDireccion(rs.getString("direccion"));
				loc.setId_zona_retiro(rs.getInt("id_zona_retiro"));
				lista_locales.add(loc);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalesByZona - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_locales.size());
		
		return lista_locales;
	}

	/**
	 * Obtiene informacion de la zona de acuerdo a su id
	 * 
	 * @param  id_zona long
	 * @return ZonaEntity's
	 * @throws ClientesDAOException
	 */		
	public ZonaEntity getZonaById(long id_zona) throws ClientesDAOException{
		ZonaEntity zon = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			logger.debug("en getZonaById");
			String sql = "SELECT id_zona, Z.id_local id_loc, nombre, descripcion, tarifa_normal_baja, "
                       + "       tarifa_normal_media, tarifa_normal_alta, tarifa_express, "
                       + "       tarifa_economica, estado_tarifa_economica, estado_tarifa_express, "
                       + "       nom_local "
                       + "FROM bo_zonas Z, bo_locales L "
                       + "WHERE Z.id_local = L.id_local "
                       + "  AND id_zona = ? ";
			logger.debug(sql+",id_zona:"+id_zona);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_zona);

			rs = stm.executeQuery();
			while (rs.next()) {
				zon = new ZonaEntity();
				zon.setId_zona(new Long(rs.getString("id_zona")));
				zon.setId_local(new Long(rs.getString("id_loc")));
				zon.setNombre(rs.getString("nombre"));
				zon.setDescrip(rs.getString("descripcion"));
				zon.setTarifa_normal_alta(rs.getInt("tarifa_normal_alta"));
				zon.setTarifa_normal_media(rs.getInt("tarifa_normal_media"));
				zon.setTarifa_normal_baja(rs.getInt("tarifa_normal_baja"));
				zon.setTarifa_economica(rs.getInt("tarifa_economica"));
				zon.setTarifa_express(rs.getInt("tarifa_express"));
				zon.setEstado_tarifa_economica(rs.getInt("estado_tarifa_economica"));
				zon.setEstado_tarifa_express(rs.getInt("estado_tarifa_express"));
				zon.setNom_local(rs.getString("nom_local"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getZonaById - Problema SQL (close)", e);
			}
		}
		return zon;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.clientes.dao.ClientesDAO#getLocalById(long)
	 */
	public LocalDTO getLocalById(long id_local) throws ClientesDAOException {
		LocalDTO local=null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		String SQLStmt = "SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios, "
                       + "       id_local_sap, cod_local_pos, tipo_flujo, cod_local_promo, "
                       + "       tipo_picking, LATITUD, LONGITUD, retiro_local, id_zona_retiro, direccion "
                       + "FROM bo_locales "
                       + "WHERE id_local = ? ";

		logger.debug("Ejecución DAO getLocalById()");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1,id_local);
			rs = stm.executeQuery();
			while (rs.next()) {
				local = new LocalDTO();
				local.setId_local(rs.getLong("id_local"));
				local.setCod_local(rs.getString("cod_local"));
				local.setNom_local(rs.getString("nom_local"));
				local.setFec_carga_prec(rs.getString("fecha_carga_precios"));
				local.setOrden(rs.getInt("orden"));
				if (rs.getString("cod_local_pos") != null){
				    local.setCod_local_pos(rs.getInt("cod_local_pos"));
				}
				local.setTipo_flujo(rs.getString("tipo_flujo"));
				local.setCod_local_promocion(rs.getString("cod_local_promo"));
				local.setTipo_picking(rs.getString("tipo_picking"));
				local.setLatitud(rs.getDouble("LATITUD"));
				local.setLongitud(rs.getDouble("LONGITUD"));
				local.setRetirolocal(rs.getString("retiro_local"));
				local.setId_zona_retiro(rs.getInt("id_zona_retiro"));
				local.setDireccion(rs.getString("direccion"));
			}

		}catch (SQLException e) {
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalById - Problema SQL (close)", e);
			}
		}
		
		return local;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.jumbocl.clientes.dao.ClientesDAO#doModLocal(cl.bbr.jumbocl.clientes.dto.LocalDTO)
	 */
	public boolean doModLocal(LocalDTO dto) throws ClientesDAOException {
		String id_zona_retiro="NULL";
		if (!(dto.getId_zona_retiro()==0))
			id_zona_retiro=""+dto.getId_zona_retiro();
		PreparedStatement stm = null;
		boolean result = false;
		try {
			String sql = "UPDATE BO_LOCALES "
                       + "   SET nom_local = '"+dto.getNom_local()+"' "
                       + "      ,cod_local = '"+dto.getCod_local()+"' "
                       + "      ,cod_local_pos = "+dto.getCod_local_pos()
                       + "      ,orden = "+ dto.getOrden()
                       + "      ,tipo_flujo = '"+dto.getTipo_flujo()+"' "
                       + "      ,cod_local_promo = '"+dto.getCod_local_promocion()+"' "
                       + "      ,tipo_picking = '"+dto.getTipo_picking()+"' "
                       + "      ,retiro_local = '"+dto.getRetirolocal()+"' "
                       + "      ,id_zona_retiro = "+id_zona_retiro+" "
                       + "      ,direccion = '"+dto.getDireccion()+"' "
                       + "WHERE id_local = "+dto.getId_local();
			conn = this.getConnection();
			logger.debug("en doModLocal");
			logger.debug("sql:"+sql);			
			stm = conn.prepareStatement(sql);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
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
	public boolean doModZonaLocal(LocalDTO dto) throws ClientesDAOException {
		String id_zona_retiro="NULL";
		if (!(dto.getId_zona_retiro()==0))
			id_zona_retiro=""+dto.getId_zona_retiro();
		PreparedStatement stm = null;
		boolean result = false;
		try {
			String sql = "UPDATE BO_LOCALES "
                       + "   SET id_zona_retiro = "+dto.getId_zona_retiro()+" "
                       + "      ,retiro_local = '"+dto.getRetirolocal()+"' "
                       + "WHERE id_local = "+dto.getId_local();
			conn = this.getConnection();
			logger.debug("en doModZonaLocal");
			logger.debug("sql:"+sql);			
			stm = conn.prepareStatement(sql);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
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

	public boolean doAddLocal(LocalDTO dto) throws ClientesDAOException {
		PreparedStatement stm = null;
		boolean result = false;
		try {
			
			String sql = "INSERT INTO BO_LOCALES "
                       + " ( ID_LOCAL, COD_LOCAL, NOM_LOCAL, ORDEN, COD_LOCAL_POS, TIPO_FLUJO, COD_LOCAL_PROMO, TIPO_PICKING )  " 
                       + " VALUES ( (SELECT MAX(id_local)+1 FROM BO_LOCALES) , ?, ?, ?, ?, ?, ?, ? ) ";
			
			conn = this.getConnection();
			logger.debug("en doModLocal");
			logger.debug("sql:"+sql);
			stm = conn.prepareStatement(sql);
			stm.setString(1, dto.getCod_local());
			stm.setString(2, dto.getNom_local());
			stm.setInt(3, dto.getOrden());
			stm.setInt(4, dto.getCod_local_pos());
			stm.setString(5, dto.getTipo_flujo());
			stm.setString(6, dto.getCod_local_promocion());
			stm.setString(7, dto.getTipo_picking());
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : doAddLocal - Problema SQL (close)", e);
			}
		}
		return result;
	}
	public int doAddLocalWithZone(LocalDTO dto) throws ClientesDAOException {
		PreparedStatement stm = null;
		Statement stmt=null;
		ResultSet rs=null;
		int result = 0;
		try {
			
			String sql = "INSERT INTO BO_LOCALES "
                       + " ( ID_LOCAL, COD_LOCAL, NOM_LOCAL, ORDEN, COD_LOCAL_POS, TIPO_FLUJO, COD_LOCAL_PROMO, TIPO_PICKING, DIRECCION )  " 
                       + " VALUES ( (SELECT MAX(id_local)+1 FROM BO_LOCALES) , ?, ?, ?, ?, ?, ?, ?, ? ) ";
			
			
			
			logger.debug("en doModLocal");
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement( sql);//, Statement.RETURN_GENERATED_KEYS);
			stm.setString(1, dto.getCod_local());
			stm.setString(2, dto.getNom_local());
			stm.setInt(3, dto.getOrden());
			stm.setInt(4, dto.getCod_local_pos());
			stm.setString(5, dto.getTipo_flujo());
			stm.setString(6, dto.getCod_local_promocion());
			stm.setString(7, dto.getTipo_picking());
			stm.setString(8, dto.getDireccion());
			stm.executeUpdate();
			//logger.debug("Resultado Ejecución: " + i);
			 stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MAX(id_local) FROM BO_LOCALES");
			if (rs != null && rs.next()){
				result=rs.getInt(1);
			}
						
				 logger.debug("id_local insertado: " + result);
						

		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stmt != null)
					stmt.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : doAddLocal - Problema SQL (close)", e);
			}
		}
		return result;
	}

    public boolean clienteEsConfiable(long rut) throws ClientesDAOException {
        PreparedStatement stm   = null;
        ResultSet rs            = null;
        boolean esConfiable     = false;
        String SQLStmt = "SELECT cli_rut " +
                         "FROM fodba.FO_CLI_CONFIABLES " + 
                         "WHERE cli_rut = ? ";

        logger.debug("Ejecución DAO clienteEsConfiable()");
        logger.debug("SQL: " + SQLStmt);
        
        try {

            conn = this.getConnection();
            stm = conn.prepareStatement( SQLStmt + " WITH UR" );
            stm.setLong(1, rut);
            rs = stm.executeQuery();
            if (rs.next()) {
                esConfiable = true;
            }

        } catch (SQLException e) {
            throw new ClientesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : clienteEsConfiable - Problema SQL (close)", e);
			}
		}       
        return esConfiable;
    }

    /**
     * @return
     */
    public List getRutsConfiables() throws ClientesDAOException {
        List ruts = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String SQLStmt = "SELECT cli_rut, cli_dv, fecha_modificacion " +
                         "FROM fodba.FO_CLI_CONFIABLES ";

        logger.debug("Ejecución DAO getRutsConfiables()");
        logger.debug("SQL: " + SQLStmt);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( SQLStmt  + " WITH UR");
            rs = stm.executeQuery();
            while (rs.next()) {
                RutConfiableDTO rut = new RutConfiableDTO();
                rut.setRut(rs.getLong("cli_rut"));
                rut.setDv(rs.getString("cli_dv"));
                rut.setFechaCreacion(rs.getString("fecha_modificacion"));
                ruts.add(rut);
            }

        }catch (SQLException e) {
            throw new ClientesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getRutsConfiables - Problema SQL (close)", e);
			}
		}      
        return ruts;
    }

    /**
     * @param ruts
     * @return
     */
    public void addRutsConfiables(Vector ruts) throws ClientesDAOException {
        PreparedStatement stmDelete = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            conn = this.getConnection();
            String sqlDel = " DELETE FROM fodba.FO_CLI_CONFIABLES ";
            
            //Limpiamos la tabla
            stmDelete = conn.prepareStatement(sqlDel);
            stmDelete.executeUpdate();
            
            st = conn.createStatement();
            
            for (int i = 0; i < ruts.size(); i++) {
                RutConfiableDTO rut = (RutConfiableDTO) ruts.get(i);                
                String sqlIns = " INSERT INTO fodba.FO_CLI_CONFIABLES (cli_rut, cli_dv, fecha_modificacion) " +
                " VALUES (" + rut.getRut() + ",'" + rut.getDv() + "',CURRENT_DATE) ";
                st.addBatch(sqlIns);
            }
            st.executeBatch();
            
            
        } catch (SQLException e) {
            throw new ClientesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addRutsConfiables - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @return
     */
    public List getLogRutsConfiables() throws ClientesDAOException {
        List logs = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String SQLStmt = "SELECT id_log, fecha, usuario, nombre_usuario, descripcion " +
                         "FROM fodba.FO_CLI_CONFIABLES_LOG " +
                         "ORDER BY id_log DESC ";

        logger.debug("Ejecución DAO getLogRutsConfiables()");
        logger.debug("SQL: " + SQLStmt);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( SQLStmt + " WITH UR" );
            rs = stm.executeQuery();
            while (rs.next()) {
                RutConfiableLogDTO log = new RutConfiableLogDTO();
                log.setIdLog(rs.getLong("id_log"));
                log.setFechaHora(rs.getString("fecha"));
                log.setUsuario(rs.getString("usuario"));
                log.setNombreUsuario(rs.getString("nombre_usuario"));
                log.setDescripcion(rs.getString("descripcion"));
                logs.add(log);
            }

        }catch (SQLException e) {
            throw new ClientesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLogRutsConfiables - Problema SQL (close)", e);
			}
		}      
        return logs;
    }

    /**
     * @param user
     * @param nombre
     * @return
     */
    public void addLogRutConfiables(String user, String nombre, String msjLog) throws ClientesDAOException {
        PreparedStatement stmInsert = null;
        ResultSet rs = null;
        Calendar cal = new GregorianCalendar();
        try {
            conn = this.getConnection();
            String sqlIns = " INSERT INTO fodba.FO_CLI_CONFIABLES_LOG " +
                            " (fecha, usuario, nombre_usuario, descripcion) VALUES (?,?,?,?) ";
            
            stmInsert = conn.prepareStatement(sqlIns);
            stmInsert.setTimestamp( 1, new Timestamp( cal.getTimeInMillis() ) );
            stmInsert.setString(2, user);
            stmInsert.setString(3, nombre);
            stmInsert.setString(4, msjLog);
            stmInsert.executeUpdate();
            

                        
        }catch (SQLException e) {
            throw new ClientesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmInsert != null)
					stmInsert.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addLogRutConfiables - Problema SQL (close)", e);
			}
		}
    }

    /**
     * @param idCliente
     */
    public void actualizaContadorEncuestaCliente(long idCliente, long idPedido, int nroCompras) throws ClientesDAOException {
        PreparedStatement stmSelect = null;
        PreparedStatement stmUpd = null;
        PreparedStatement stmUpd2 = null;
        ResultSet rs = null;
        int countClient = 0;
        try {
            conn = this.getConnection();
            
            String sqlSelect = "SELECT CLI_CONTADOR_ENCUESTAS " +
                               "FROM FODBA.FO_CLIENTES " +
                               "WHERE CLI_ID = ?";
            
            stmSelect = conn.prepareStatement( sqlSelect + " WITH UR" );
            stmSelect.setLong( 1, idCliente );
            rs = stmSelect.executeQuery();
            
            if (rs.next()) {
                countClient = rs.getInt("CLI_CONTADOR_ENCUESTAS");              
            }
            
            String sqlUpd = "";
            boolean enviarEncuesta = false;
            if ( countClient < ( nroCompras - 1 ) ) {
                sqlUpd = "UPDATE FODBA.FO_CLIENTES " +
                         "SET CLI_CONTADOR_ENCUESTAS = ( CLI_CONTADOR_ENCUESTAS + 1 ) " +
                         "WHERE CLI_ID = ?";
            } else {
                enviarEncuesta = true;
                sqlUpd = "UPDATE FODBA.FO_CLIENTES " +
                         "SET CLI_CONTADOR_ENCUESTAS = 0 " +
                         "WHERE CLI_ID = ?";
            }
            stmUpd = conn.prepareStatement(sqlUpd);
            stmUpd.setLong( 1, idCliente );
            stmUpd.executeUpdate();            
           
            
            if ( enviarEncuesta ) {
                String sqlUpd2 = "UPDATE BODBA.BO_PEDIDOS_EXT " +
                                 "SET ENCUESTA_ENVIAR = 1 " +
                                 "WHERE ID_PEDIDO = ?";
                stmUpd2 = conn.prepareStatement(sqlUpd2);
                stmUpd2.setLong( 1, idPedido );
                stmUpd2.executeUpdate();            
               
            }

                        
        } catch (SQLException e) {
            throw new ClientesDAOException(String.valueOf(e.getErrorCode()),e);
        } finally {
            try {
                if (stmSelect != null) stmSelect.close();
                if (stmUpd != null) stmUpd.close();
                if (stmUpd2 != null) stmUpd2.close();
                if (rs != null)  rs.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaContadorEncuestaCliente - Problema SQL (close)", e);
			}
		}
    }
    
    public List getResumenCompraCatPro(long pedido) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        CarroCompraCategoriasDTO aux_cat = null;
        List aux_lpro = null;
        List l_cat = new ArrayList();
        ResumenCompraProductosDTO aux_pro = null;
        CarroCompraCategoriasDTO categoria = null;
        ResumenCompraProductosDTO productos = null;
        List l_pro = null;

        try {

            conexion = JdbcDAOFactory.getConexion();
            
            String order_by = "cat_nombre, pro_tipo_producto, pro_des_corta, precio";           
            
            String query = 	"select distinct cat_id, cat_nombre, cat_orden, " + 
				            "id_detalle, cant_solic, observacion,  " + 
				            "pro_id, pro_cod_sap, pro_tipo_producto, pro_des_corta, pro_des_larga, pro_unidad_medida, " + 
				            "prca_cat_id, prca_orden, precio, mar_id, mar_nombre  " + 
				            "from bo_detalle_pedido join fo_productos on id_producto = pro_id_bo " +
				            "join fo_productos_categorias on pro_id = prca_pro_id  " + 
				            "join fo_categorias on prca_cat_id = cat_id  " + 
				            "join fo_marcas on pro_mar_id = mar_id  " + 
				            "where id_pedido = ?  " + 
				            "order by " + order_by;
            
            stm = conexion.prepareStatement( query + " WITH UR" );
            stm.setLong(1, pedido);     
            logger.debug("getResumenCompraCatPro SQL: " + stm.toString());          
            rs = stm.executeQuery();
            
            while (rs.next()) {

                logger.logData( "getResumenCompraCatPro", rs);

                // Revisar si existe producto en alguna categoría anterior
                boolean flag = true;
                for( int i = 0; flag == true && i < l_cat.size(); i++ ) {
                    aux_cat = (CarroCompraCategoriasDTO)l_cat.get(i);
                    aux_lpro = aux_cat.getCarroCompraProductosDTO();
                    for( int j = 0; flag == true && j < aux_lpro.size(); j++ ) {
                        aux_pro = (ResumenCompraProductosDTO)aux_lpro.get(j);
                        if( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
                            flag = false;
                        }
                    }
                }
                if( flag == false ) {
                    continue; // Se salta el registro
                }

                // Listado de productos por categorias
                l_pro = new ArrayList();
                
                
                // Revisar si existe la categoría
                boolean flag_cat = true;
                for( int i = 0; i < l_cat.size(); i++ ) {
                    categoria = (CarroCompraCategoriasDTO)l_cat.get(i);
                    if( categoria.getId() == rs.getLong("cat_id") ) {
                        l_pro = categoria.getCarroCompraProductosDTO();
                        flag_cat = false;
                        break;
                    }
                }
                
                // Agregar categorias sólo si es una categoría vacía
                if( true || flag_cat == true ) {
                    categoria = new CarroCompraCategoriasDTO();
                    categoria.setId( rs.getLong("cat_id") );
                    categoria.setCategoria( rs.getString("cat_nombre") );
                }
                                
                // Agregar los productos
                
                productos = new ResumenCompraProductosDTO();
                
                productos.setPro_id(rs.getLong("pro_id"));
                productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
                productos.setCodigo(rs.getString("pro_cod_sap"));
                productos.setPrecio(rs.getDouble("precio"));                
                productos.setCantidad(rs.getDouble("cant_solic"));
                productos.setMarca(rs.getString("mar_nombre"));
                if( rs.getString("observacion") != null )
                    productos.setNota( rs.getString("observacion") );
                
                l_pro.add(productos);
                
                categoria.setCarroCompraProductosDTO(l_pro);
                
                // Sólo si es una categoría nueva
                if( flag_cat == true ) {
                    l_cat.add(categoria);
                }
                
            }
            
        } catch (SQLException ex) {
            logger.error("getCarroComprasCatPro - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroComprasCatPro - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroComprasCatPro - Problema SQL (close)", e);
            }
        }

        return l_cat;
    }
    
    public void addMail(MailDTO mail) throws ClientesDAOException {

        Connection conexion = null;
        PreparedStatement stm = null;
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement("INSERT INTO FO_SEND_MAIL ( " +
                            "FSM_IDFRM, " +
                            "FSM_REMITE, " +
                            "FSM_DESTINA, " +
                            "FSM_COPIA, " +
                            "FSM_SUBJECT, " +
                            "FSM_DATA, " +
                            "FSM_ESTADO, " +
                            "FSM_STMPSAVE, " +
                            "FSM_STMPSEND ) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
            stm.setString(1, mail.getFsm_idfrm() );         
            stm.setString(2, mail.getFsm_remite() );            
            stm.setString(3, mail.getFsm_destina() );
            if( mail.getFsm_copia() != null )
                stm.setString(4, mail.getFsm_copia() );
            else
                stm.setNull(4, java.sql.Types.VARCHAR);
            stm.setString(5, mail.getFsm_subject() );
            
            StringReader sr = new StringReader(mail.getFsm_data());
            stm.setCharacterStream(6, sr, mail.getFsm_data().length());         
            stm.setString(7, mail.getFsm_estado() );            
            stm.setTimestamp(8, new Timestamp( mail.getFsm_stmpsave() ) );          
            stm.setNull(9, java.sql.Types.TIMESTAMP);           
            logger.debug("SQL (addMail): " + stm.toString());
            stm.executeUpdate();            

        } catch (SQLException ex) {
            logger.error("addMail - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("addMail - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("addMail - Problema SQL (close)", e);
            }
        }
        
    }


	public boolean doBlanqueoDireccion(long id_cliente) throws ClientesDAOException {
		
		PreparedStatement stm = null;
		boolean result=false;
		String sql =" UPDATE FODBA.FO_DIRECCIONES " +
					"	SET DIR_COM_ID= 353, " +
					"		DIR_LOC_ID_2=4, " +
					"		DIR_ZONA_ID_2=null, " +
					"		DIR_CALLE='DIRECCION NO VALIDA', " +
					"		DIR_NUMERO='SIN NUMERO', " +
					"		DIR_COMENTARIOS='DIRECCION NO VALIDA', " +
					"		ID_POLIGONO=152" +
					"	WHERE DIR_CLI_ID = "+id_cliente;
		
	
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
		} catch (SQLException e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updBloqueoCliente - Problema SQL (close)", e);
			}
		}
		return result;
	}


	public boolean tieneDireccionesConCobertura(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean tiene = false;
        try {
           
            String SQL = "SELECT COUNT(DIR_ID) CANTIDAD "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "WHERE D.DIR_ESTADO = 'A' "
                       + "  AND D.DIR_CLI_ID = ? "
                       + "  AND D.ID_POLIGONO != 0";
            conexion = JdbcDAOFactory.getConexion();
            
            stm = conexion.prepareStatement(SQL + " WITH UR ");
            stm.setLong(1, idCliente );
            
            rs = stm.executeQuery();
            if ( rs.next() ) {
                if ( rs.getLong("CANTIDAD") > 0 ) {
                    tiene = true;
                }
            }
           
        } catch (SQLException ex) {
            logger.error("tieneDireccionesConCobertura - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("tieneDireccionesConCobertura - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
            	 if (rs != null)
                     rs.close();
            	 if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("tieneDireccionesConCobertura - Problema SQL (close)", e);
            }
        }
        return tiene;
    }
	
	/*
	 * DESDE EL FO
	 * */
	
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#listadoDirecciones(long)
	 */
	public List listadoDireccionesFO(long cliente_id) throws ClientesDAOException {

		List listaDirecciones = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "SELECT D.DIR_ID, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, D.DIR_CALLE, "
                       + "       D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, D.DIR_FEC_CREA, "
                       + "       C.NOMBRE AS COM_NOMBRE, C.REG_ID AS REGION, R.REG_NOMBRE, R.REG_ORDEN, P.ID_ZONA, Z.ID_LOCAL "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA   = D.DIR_COM_ID "
                       + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID      = C.REG_ID "
                       + "     JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                       
                       + "WHERE D.DIR_ESTADO = 'A' "
                       + "  AND D.DIR_CLI_ID = ? ";
		    conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, cliente_id);
			logger.debug("SQL (listadoDirecciones): " + SQL);
			logger.debug("cliente_id: " + cliente_id);
			rs = stm.executeQuery();

			while (rs.next()) {
                DireccionesDTO dir = new DireccionesDTO();				
				dir.setId(rs.getLong("dir_id"));
				//dir.setLoc_cod(rs.getLong("DIR_LOC_ID"));
				dir.setCom_id(rs.getLong("dir_com_id"));
				dir.setTipo_calle(rs.getLong("dir_tip_id"));
				dir.setId_cliente(rs.getLong("dir_cli_id"));
				dir.setAlias( rs.getString("dir_alias") );
				dir.setCalle( rs.getString("dir_calle") );
				dir.setNumero( rs.getString("dir_numero") );
				dir.setDepto( rs.getString("dir_depto") );			
				if(rs.getString("dir_comentarios") == null)
					dir.setComentarios( "." );
				else
					dir.setComentarios( rs.getString("dir_comentarios") );
				if ( rs.getTimestamp("dir_fec_crea") != null )
					dir.setFec_crea_long(rs.getTimestamp("dir_fec_crea").getTime());		
				dir.setEstado(rs.getString("dir_estado"));
				dir.setReg_id(rs.getLong("region"));
				dir.setCom_nombre(rs.getString("com_nombre"));
				dir.setReg_nombre(rs.getString("reg_nombre"));
                dir.setLoc_cod(rs.getLong("id_local"));
                dir.setZona_id(rs.getLong("id_zona"));
				listaDirecciones.add(dir);
			}
		} catch (SQLException ex) {
			logger.error("listadoDirecciones - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("listadoDirecciones - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("listadoDirecciones - Problema SQL (close)", e);
			}
		}
		return listaDirecciones;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getDireccion(long)
	 */
	public DireccionesDTO getDireccionFO(long dir_id) throws ClientesDAOException {
		DireccionesDTO dir    = new DireccionesDTO();
		Connection conexion   = null;
		PreparedStatement stm = null;
		ResultSet rs          = null;
		
		try {

		   /* String SQL = "SELECT D.DIR_ID, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, D.DIR_CALLE, "
                       + "       D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, D.DIR_FEC_CREA, "
                       + "       D.DIR_FNUEVA, C.NOMBRE AS COM_NOMBRE, C.REG_ID AS REGION, R.REG_NOMBRE, R.REG_ORDEN "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA = D.DIR_COM_ID "
                       + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID = C.REG_ID "
                       + "WHERE D.DIR_ID = ? ";*/


String SQL = " SELECT D.DIR_ID, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, D.DIR_CALLE, "
      + "       D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, D.DIR_FEC_CREA, "
      + "       D.DIR_FNUEVA, C.NOMBRE AS COM_NOMBRE, C.REG_ID AS REGION, R.REG_NOMBRE, R.REG_ORDEN, P.ID_ZONA, Z.ID_LOCAL "
      + " FROM FODBA.FO_DIRECCIONES D "
      + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA = D.DIR_COM_ID "
      + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID = C.REG_ID " 
      + "     JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO " 
      + "     JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA " 
      + " WHERE D.DIR_ID = ? ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, dir_id);
			logger.debug("SQL: " + SQL);
			rs = stm.executeQuery();

			if (rs.next()) {
				dir.setId(rs.getLong("dir_id"));
				//dir.setLoc_cod(rs.getLong("DIR_LOC_ID"));
				dir.setCom_id(rs.getLong("dir_com_id"));
				dir.setTipo_calle(rs.getLong("dir_tip_id"));
				dir.setId_cliente(rs.getLong("dir_cli_id"));
				dir.setAlias( rs.getString("dir_alias") );
				dir.setCalle( rs.getString("dir_calle") );
				dir.setNumero( rs.getString("dir_numero") );
				dir.setDepto( rs.getString("dir_depto") );
				dir.setComentarios( rs.getString("dir_comentarios") );
				if (rs.getTimestamp("dir_fec_crea") != null)
					dir.setFec_crea_long(rs.getTimestamp("dir_fec_crea").getTime());
				dir.setEstado(rs.getString("dir_estado"));
				//dir.setZona_id(rs.getLong("dir_zona_id"));
				dir.setReg_id(rs.getLong("region"));
				dir.setCom_nombre(rs.getString("com_nombre"));
				dir.setReg_nombre(rs.getString("reg_nombre"));
				dir.setNueva((rs.getString("dir_fnueva")));	
				dir.setZona_id(rs.getLong("ID_ZONA"));
				dir.setLoc_cod(rs.getLong("ID_LOCAL"));
			}
		} catch (SQLException ex) {
			logger.error("getDireccion - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getDireccion - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getDireccion - Problema SQL (close)", e);
			}
		}
		return dir;
	}	
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#insertCliente(cl.bbr.fo.clientes.model.ClienteEntity)
	 */
	public long insertClienteFO(cl.bbr.jumbocl.clientes.model.ClienteEntity cliente)
			throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
        ResultSet rs = null;
        long llave = 0;

		try {

			Calendar cal = new GregorianCalendar();
			long ahora = cal.getTimeInMillis();

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(""
							+ "insert into fo_clientes (cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_rec_info, cli_fec_crea, cli_estado, cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, CLI_ENVIO_MAIL, CLI_ENVIO_SMS) "
							+ "values ( "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
							+ ")");
			stm.setLong(1, cliente.getRut().longValue());
			stm.setString(2, Utils.stringToDbFO(String.valueOf(cliente.getDv()),1));
			stm.setString(3, Utils.stringToDbFO(cliente.getNombre(),50));
			stm.setString(4, Utils.stringToDbFO(cliente.getApellido_pat(),50));
			stm.setString(5, Utils.stringToDbFO(cliente.getApellido_mat(),50));
			stm.setString(6, cliente.getClave());
			stm.setString(7, Utils.stringToDbFO(cliente.getEmail(),50));
			stm.setString(8, Utils.stringToDbFO(cliente.getFon_cod_1(),5));
			stm.setString(9, Utils.stringToDbFO(cliente.getFon_num_1(),20));
			stm.setString(10, Utils.stringToDbFO(cliente.getFon_cod_2(),5));
			stm.setString(11, Utils.stringToDbFO(cliente.getFon_num_2(),20));
			stm.setInt(12, 1);
			stm.setTimestamp(13, new Timestamp(ahora));
			stm.setString(14, "A");
			stm.setDate(15, cliente.getFec_nac());
			stm.setString(16, Utils.stringToDbFO(String.valueOf(cliente.getGenero()),1));
			stm.setTimestamp(17, new Timestamp(ahora));
			stm.setString(18, Utils.stringToDbFO(String.valueOf(cliente.getPregunta()),200) );
			stm.setString(19, Utils.stringToDbFO(String.valueOf(cliente.getRespuesta()),200) );
            stm.setLong(20, cliente.getRecibeEMail());
            stm.setLong(21, cliente.getRecibeSms());
			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.info("insertCliente - No inserta ningún cliente pero sin excepciones");
			}
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                llave = rs.getInt(1);
            } else {
                logger.error("creaClienteDesdeInvitado - Problema al recuperar id insertado");
                throw new ClientesDAOException("creaClienteDesdeInvitado - Problema al recuperar id insertado");
            }

		} catch (SQLException ex) {
			logger.error("insertCliente - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("insertCliente - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
                if (rs != null)
                    rs.close();
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("insertCliente - Problema SQL (close)", e);
			}
		}
        return llave;

	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#updateCliente(cl.bbr.fo.clientes.model.ClienteEntity)
	 */
	public void updateClienteFO(cl.bbr.jumbocl.clientes.model.ClienteEntity cliente) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;

		Calendar cal = new GregorianCalendar();
		long ahora = cal.getTimeInMillis();

		try {

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(
							"update fo_clientes set " +
									"cli_nombre = ?, " +
									"cli_apellido_pat = ?, " +
									"cli_apellido_mat = ?, " +
									"cli_clave = ?, " +
									"cli_email = ?, " +
									"cli_fon_cod_1 = ?, " +
									"cli_fon_num_1 = ?, " +
									"cli_fon_cod_2 = ?, " +
									"cli_fon_num_2 = ?, " +
									"cli_fon_cod_3 = ?, " +
									"cli_fon_num_3 = ?, " +
									"cli_fec_nac = ?, " +
									"cli_genero = ?, " +
									"cli_fec_act = ?, " +
									"cli_pregunta = ?, " +
									"cli_respuesta = ?," +
									"cli_mod_dato = ?, " +
									"cli_envio_sms = ?, " +
									"CLI_ENVIO_MAIL = ? "
							+ "where cli_id = ? ");
			stm.setString(1, Utils.stringToDbFO(cliente.getNombre(),50));
			stm.setString(2, Utils.stringToDbFO(cliente.getApellido_pat(),50));
			stm.setString(3, Utils.stringToDbFO(cliente.getApellido_mat(),50));
			stm.setString(4, cliente.getClave());
			stm.setString(5, Utils.stringToDbFO(cliente.getEmail(),50));
			stm.setString(6, Utils.stringToDbFO(cliente.getFon_cod_1(),5));
			stm.setString(7, Utils.stringToDbFO(cliente.getFon_num_1(),20));
			stm.setString(8, Utils.stringToDbFO(cliente.getFon_cod_2(),5));
			stm.setString(9, Utils.stringToDbFO(cliente.getFon_num_2(),20));
			stm.setString(10, Utils.stringToDbFO(cliente.getFon_cod_3(),5));
			stm.setString(11, Utils.stringToDbFO(cliente.getFon_num_3(),20));
			stm.setDate(12, cliente.getFec_nac());
			stm.setString(13, Utils.stringToDbFO(String.valueOf(cliente.getGenero()),1));
			stm.setTimestamp(14, new Timestamp(ahora));
			stm.setString(15, Utils.stringToDbFO(String.valueOf(cliente.getPregunta()),200) );
			stm.setString(16, Utils.stringToDbFO(String.valueOf(cliente.getRespuesta()),200) );
			stm.setString(17, Utils.stringToDbFO(String.valueOf(cliente.getCli_mod_dato()),2) );
			stm.setLong(18, cliente.getRecibeSms());
			stm.setLong(19, cliente.getRecibeEMail());
			stm.setLong(20, cliente.getId().longValue());

			logger.debug("SQL (updateCliente): " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.info("updateCliente - No modifica ningún cliente pero sin excepciones");
			}

		} catch (SQLException ex) {
			logger.error("updateCliente - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("updateCliente - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("updateCliente - Problema SQL (close)", e);
			}
		}

	}

    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#updateDatosContactoCliente(cl.bbr.fo.clientes.model.ClienteEntity)
     */
    public void updateDatosContactoClienteFO(ClienteDTO cliente) throws ClientesDAOException {

        Connection conexion = null;
        PreparedStatement stm = null;

        Calendar cal = new GregorianCalendar();
        long ahora = cal.getTimeInMillis();

        try {

            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement("update fo_clientes set " +
                                    "cli_email = ?, " +
                                    "cli_fon_cod_1 = ?, " +
                                    "cli_fon_num_1 = ?, " +
                                    "cli_fon_cod_2 = ?, " +
                                    "cli_fon_num_2 = ?, " +
                                    "cli_fon_cod_3 = ?, " +
                                    "cli_fon_num_3 = ?, " +
                                    "cli_fec_act = ? "
                            + "where cli_id = ? ");
            stm.setString(1, Utils.stringToDbFO(cliente.getEmail(),50));
            stm.setString(2, Utils.stringToDbFO(cliente.getFon_cod_1(),5));
            stm.setString(3, Utils.stringToDbFO(cliente.getFon_num_1(),20));
            stm.setString(4, Utils.stringToDbFO(cliente.getFon_cod_2(),5));
            stm.setString(5, Utils.stringToDbFO(cliente.getFon_num_2(),20));
            stm.setString(6, Utils.stringToDbFO(cliente.getFon_cod_3(),5));
            stm.setString(7, Utils.stringToDbFO(cliente.getFon_num_3(),20));
            stm.setTimestamp(8, new Timestamp(ahora));
            stm.setLong(9, cliente.getId());
            logger.debug("SQL (updateDatosContactoCliente): " + stm.toString());
            if (stm.executeUpdate() == 0) {
                logger.info("updateDatosContactoCliente - No modifica ningún cliente pero sin excepciones");
            }

        } catch (SQLException ex) {
            logger.error("updateDatosContactoCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("updateDatosContactoCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("updateDatosContactoCliente - Problema SQL (close)", e);
            }
        }

    }
    
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#passupdCliente(long, java.lang.String)
	 */
	public void passupdClienteFO(long rut, String estado) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		try {
			conexion = JdbcDAOFactory.getConexion();
			String query = "update fo_clientes set cli_estado = ? where cli_rut = ?";
			stm = conexion.prepareStatement(query);
			stm.setString(1, estado);
			stm.setLong(2, rut);
			logger.debug("passupdCliente SQL : " + stm.toString());

			if (stm.executeUpdate() == 0) {
				logger.info("passupdCliente SQL : " + stm.toString());
				logger.info("No modifica estado cliente sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("passupdCliente - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("passupdCliente - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("passupdCliente - Problema SQL (close)", e);
			}
		}

	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#clienteChangeDatosPaso3(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void clienteChangeDatosPaso3FO(long cli_id, String email, String codigo, String telefono) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		String query = null;
		try {

			conexion = JdbcDAOFactory.getConexion();
			if ((telefono != null) && (!telefono.equals(""))) {
				query = "	update fo_clientes " +
						"	set cli_email = ?, cli_fon_cod_2 = ?, cli_fon_num_2 = ? where cli_id = ? ";
				stm = conexion.prepareStatement(query);
				stm.setString(1, email);
				stm.setString(2, codigo);
				stm.setString(3, telefono);
				stm.setLong(4, cli_id);
			} else {
				query = "update fo_clientes set cli_email = ? where cli_id = ?";
				stm = conexion.prepareStatement(query);
				stm.setString(1, email);
				stm.setLong(2, cli_id);
			}	

			logger.debug("SQL: " + stm.toString());

			if (stm.executeUpdate() == 0) {
				logger.info("SQL: " + stm.toString());
				logger.info("No modifica datos cliente sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("clienteChangeDatosPaso3 - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("clienteChangeDatosPaso3 - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("clienteChangeDatosPaso3 - Problema SQL (close)", e);
			}
		}
	}
	
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#passupdCliente(long, java.lang.String, java.lang.String)
	 */
	public void passupdClienteFO(long rut, String clave, String estado) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;

		try {

			conexion = JdbcDAOFactory.getConexion();			
			
			String query = 	"	update fo_clientes set " +
							"		cli_clave = ?, " +
							"		cli_estado = ?, " +
							"		cli_intentos = 0 " +
							"	where cli_rut = ?";

			stm = conexion.prepareStatement(query);
			stm.setString(1, clave );
			stm.setString(2, estado );
			stm.setLong(3, rut);
			logger.debug("SQL: " + stm.toString());

			if (stm.executeUpdate() == 0) {
				logger.info("passupdCliente SQL : " + stm.toString());
				logger.info("No modifica clave cliente sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("passupdCliente - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("passupdCliente - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("passupdCliente - Problema SQL (close)", e);
			}
		}

	}

	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getLocalComuna(long)
	 */
	public int getPoligonoCeroFO( long comuna ) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		int poligono = 0;
		
		try {
			conexion = JdbcDAOFactory.getConexion();
			String SQL = "SELECT DISTINCT P.ID_POLIGONO "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "WHERE P.ID_COMUNA = ? "
                       + "  AND P.NUM_POLIGONO = 0";
			
			stm = conexion.prepareStatement(SQL + " WITH UR");
 			stm.setLong(1, comuna);
			
			logger.debug("SQL (getPoligono): " + SQL);
			logger.debug("id_comuna: " + comuna);
			
			rs = stm.executeQuery();
			
			if ( rs.next() ) {
				logger.logData( "getPoligono", rs);
				poligono = rs.getInt("ID_POLIGONO");
			}
		} catch (SQLException ex) {
			logger.error("getLocalComuna - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getLocalComuna - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getLocalComuna - Problema SQL (close)", e);
			}
		}
		return poligono;
	}


	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#insertDireccion(cl.bbr.fo.clientes.dto.DireccionesDTO)
	 */
	public long insertDireccionFO(DireccionesDTO direccion) throws ClientesDAOException {
	 	Connection conexion = null;
		PreparedStatement stm = null;
        ResultSet result = null;
		int id_poligono = 0;
        long id = 0L;
		try {
			conexion = JdbcDAOFactory.getConexion();
			id_poligono = this.getPoligonoCeroFO(direccion.getCom_id());
        	String comentario = "";
			if( direccion.getComentarios() != null )
				comentario = Utils.stringToDbFO(direccion.getComentarios(),2000);

			String sql = 	"	INSERT INTO fo_direcciones (" +
            				"		id_poligono, " +
            				"		dir_com_id, " +
            				"		dir_tip_id, " +
            				"		dir_cli_id, " +
            				"		dir_alias, " +
							"		dir_calle, " +
							"		dir_numero, " +
							"		dir_depto, " +
							"		dir_comentarios, " +
							"		dir_estado, " +
							"		dir_fec_crea, " +
							"		dir_fnueva" +
							") VALUES (" +
								+ id_poligono + ", " 
								+ direccion.getCom_id() + ", " 
								+ direccion.getTipo_calle() + ", " 
								+ direccion.getId_cliente() + ", '" 
								+ Utils.stringToDbFO(direccion.getAlias(),20) + "', '" 
								+ Utils.stringToDbFO(direccion.getCalle(),200) + "', '" 
								+ Utils.stringToDbFO(direccion.getNumero(),20) + "', '" 
								+ Utils.stringToDbFO(direccion.getDepto(),20) + "', '" 
								+ comentario + "', '" 
								+ Utils.stringToDbFO(direccion.getEstado() + "",1) + "', " 
								+ " CURRENT TIMESTAMP, 'N')";

			stm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			logger.debug("SQL: " + sql);
			
			if (stm.executeUpdate() == 0) {
				logger.info("insertDireccion - Insert dirección despacho sin excepciones");
			}
            result = stm.getGeneratedKeys();
            
            if (result.next()) {
                id = result.getLong(1);
            }
            
		} catch (SQLException ex) {
			logger.error("insertDireccion - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("insertDireccion - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
                //No estaba el result, se puso 05/04/2012 - carriagada
                if (result != null)
                    result.close(); 
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("insertDireccion - Problema SQL (close)", e);
			}
		}
        return id;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#deleteDireccion(long)
	 */
	public void deleteDireccionFO(long direccion_id) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;	

		try {

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("update fo_direcciones set " +
											"	dir_estado = 'E' " +
											"	where dir_id = ?");
			stm.setLong(1, direccion_id);
			logger.debug("SQL: " + stm.toString());
			
			if (stm.executeUpdate() == 0) {
				logger.info("deleteDireccion - dirección despacho sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("deleteDireccion - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("deleteDireccion - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("deleteDireccion - Problema SQL (close)", e);
			}
		}
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#updateDireccion(cl.bbr.fo.clientes.dto.DireccionesDTO)
	 */
	public void updateDireccionFO(DireccionesDTO direccion) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		int id_poligono = 0;
		try {
			conexion = JdbcDAOFactory.getConexion();			
			String SQL = "";
 			id_poligono = this.getPoligonoCeroFO(direccion.getCom_id());
			SQL = "UPDATE FO_DIRECCIONES "
			    + "   SET ID_POLIGONO = ?, "
			    + "       DIR_COM_ID  = ?, "
			    + "       DIR_TIP_ID  = ?, "
			    + "       DIR_CLI_ID  = ?, "
			    + "       DIR_ALIAS   = ?, "
			    + "       DIR_CALLE   = ?, "
			    + "       DIR_NUMERO  = ?, "
			    + "       DIR_DEPTO   = ?, "
			    + "       DIR_COMENTARIOS = ?, "
			    + "       DIR_FNUEVA  = ?, "
			    + "       CONFIRMADA  = 0 "
			    + " WHERE DIR_ID = ? ";
			stm = conexion.prepareStatement(SQL);
			int i = 1;
			stm.setInt(i++, id_poligono);
			stm.setLong(i++, direccion.getCom_id());
			stm.setLong(i++, direccion.getTipo_calle());
			stm.setLong(i++, direccion.getId_cliente());
			stm.setString(i++, Utils.stringToDbFO(direccion.getAlias(),20));
			stm.setString(i++, Utils.stringToDbFO(direccion.getCalle(),200));
			stm.setString(i++, Utils.stringToDbFO(direccion.getNumero(),20));
			stm.setString(i++, Utils.stringToDbFO(direccion.getDepto(),20));
			if( direccion.getComentarios() != null )
				stm.setString(i++, Utils.stringToDbFO(direccion.getComentarios(),255));
			else
				stm.setString(i++, "" );
			stm.setString(i++, direccion.getNueva());
			stm.setLong(i++, direccion.getId());

			logger.debug("SQL: " + stm.toString());
			
			if (stm.executeUpdate() == 0) {
				logger.info("updateDireccion - Update dirección despacho sin excepciones");
			}
			
		} catch (SQLException ex) {
			logger.error("updateDireccion - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("updateDireccion - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("updateDireccion - Problema SQL (close)", e);
			}
		}
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getCarroComprasCatPro(java.lang.String, long, java.lang.String)
	 */
	public List getCarroComprasCatProFO(String local, long cliente_id) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		CarroCompraCategoriasDTO aux_cat = null;
		List aux_lpro = null;
		List l_cat = new ArrayList();
		cl.bbr.jumbocl.clientes.dto.CarroCompraProductosDTO aux_pro = null;
		CarroCompraCategoriasDTO categoria = null;
		List l_pro = null;
		try {
			String query = 	"SELECT distinct cat.cat_id, cat.cat_nombre, cat.cat_orden, cc.car_id, cc.car_cantidad, " +
                            "cc.car_nota, pro.pro_id, pro.pro_cod_sap, pro.pro_estado, pro.pro_tipre, " +
                            "pro.pro_tipo_producto, pro.pro_des_corta, pro.pro_des_larga, pro.pro_unidad_medida, " +
                            "pro.pro_ranking_ventas, pro.pro_generico, pro.pro_inter_valor, pro.pro_inter_max, " +
                            "pro.pro_preparable, pro.pro_nota, pro.pro_tipo_sel, loc.pre_costo, loc.pre_valor, " +
                            "loc.pre_stock, loc.pre_estado, loc.pre_tienestock, uni.uni_id, uni.uni_desc, uni.uni_cantidad, " +
                            "uni.uni_estado, mar.mar_id, mar.mar_nombre " +
                            "FROM fo_carro_compras cc " +
                            "join fo_productos pro on pro.pro_id = cc.car_pro_id " +
                            "join ( " +
                            "     select cc1.CAR_PRO_ID as pro_id, min(cat1.cat_id) as cat_id " +
                            "     FROM fo_carro_compras cc1 " +
                            "     join fo_productos pro1 on pro1.pro_id = cc1.car_pro_id " +
                            "     join fo_productos_categorias prca1 on pro1.pro_id = prca1.prca_pro_id " +
                            "     join fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                            "     join fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                            "     join fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                            "     join fo_precios_locales loc1 on loc1.pre_pro_id = pro1.pro_id and loc1.pre_loc_id = " + local + " and loc1.pre_estado = 'A' " +
                            "     WHERE cc1.car_cli_id = " + cliente_id + " " +
                            "     group by cc1.CAR_PRO_ID " +
                            "     ) as x on x.pro_id = pro.pro_id " +
                            "join fo_categorias cat on cat.CAT_ID = x.cat_id " +
                            "join fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id " +
                            "join fo_marcas mar on pro.pro_mar_id = mar.mar_id " +
                            "join fo_precios_locales loc on loc.pre_pro_id = pro.pro_id and loc.pre_loc_id = " + local + " and loc.pre_estado = 'A' " +
                            "WHERE cc.car_cli_id = " + cliente_id + " " +
							"order by cat_nombre, pro_tipo_producto, pro_des_corta, pre_valor" ;
			
			conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement( query  + " WITH UR");
			rs = stm.executeQuery();
			
			while (rs.next()) {
				logger.logData( "getCarroComprasCatPro", rs);
				// Revisar si existe producto en alguna categoría anterior
				boolean flag = true;
				for( int i = 0; flag == true && i < l_cat.size(); i++ ) {
					aux_cat = (CarroCompraCategoriasDTO)l_cat.get(i);
					aux_lpro = aux_cat.getCarroCompraProductosDTO();
					for( int j = 0; flag == true && j < aux_lpro.size(); j++ ) {
						aux_pro = (cl.bbr.jumbocl.clientes.dto.CarroCompraProductosDTO)aux_lpro.get(j);
						if( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
							flag = false;
						}
					}
				}
				if( flag == false ) {
					continue; // Se salta el registro
				}

				// Listado de productos por categorias
				l_pro = new ArrayList();
				
				// Revisar si existe la categoría
				boolean flag_cat = true;
				for( int i = 0; i < l_cat.size(); i++ ) {
					categoria = (CarroCompraCategoriasDTO)l_cat.get(i);
					if( categoria.getId() == rs.getLong("cat_id") ) {
						l_pro = categoria.getCarroCompraProductosDTO();
						flag_cat = false;
						break;
					}
				}
				
				// Agregar categorias sólo si es una categoría vacía
				if( true || flag_cat == true ) {
					categoria = new CarroCompraCategoriasDTO();
					categoria.setId( rs.getLong("cat_id") );
					categoria.setCategoria( rs.getString("cat_nombre") );
				}
								
				// Agregar los productos				
				cl.bbr.jumbocl.clientes.dto.CarroCompraProductosDTO productos = new cl.bbr.jumbocl.clientes.dto.CarroCompraProductosDTO();
				
				productos.setPro_id(rs.getLong("pro_id"));
				productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
				productos.setCodigo(rs.getString("pro_cod_sap"));
				if( rs.getString("uni_cantidad") != null && rs.getString("pre_valor") != null && rs.getString("pro_unidad_medida") != null ) {
					productos.setPpum(rs.getDouble("pre_valor")/(rs.getDouble("uni_cantidad")*rs.getDouble("pro_unidad_medida")));
				}
				productos.setPrecio(rs.getDouble("pre_valor"));
                if (rs.getInt("pre_tienestock") == 0)
                    productos.setTieneStock(false);
                else
                    productos.setTieneStock(true);
				productos.setCantidad(rs.getDouble("car_cantidad"));
				productos.setUnidad_nombre(rs.getString("uni_desc"));
				productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
				productos.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
				productos.setCar_id(rs.getLong("car_id"));
				productos.setInter_maximo( rs.getDouble("pro_inter_max") );
				if( rs.getString("pro_inter_valor") != null )
					productos.setInter_valor(rs.getDouble("pro_inter_valor"));
				else
					productos.setInter_valor(1.0);
				productos.setMarca(rs.getString("mar_nombre"));
				if( rs.getString("pro_nota").compareTo("S") == 0 )
					productos.setCon_nota( true );
				else
					productos.setCon_nota( false );
				if( rs.getString("car_nota") != null )
					productos.setNota( rs.getString("car_nota") );
				
				if( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
					productos.setStock(rs.getLong("pre_stock"));
				} else {
					productos.setStock(100); // No toma encuenta el stock nunca pone 0
				}
				
				if( rs.getString("pro_estado").compareTo("N") == 0 
						|| rs.getString("cat_id") == null 
						|| rs.getString("pre_valor") == null
						|| rs.getString("pro_estado").compareTo("A") != 0 ) {
				    productos.setStock(0);
				}
				
				l_pro.add(productos);
				
				categoria.setCarroCompraProductosDTO(l_pro);
				
				// Sólo si es una categoría nueva
				if( flag_cat == true ) {
					l_cat.add(categoria);
				}
				
			}
			
		} catch (SQLException ex) {
			logger.error("getCarroComprasCatPro - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCarroComprasCatPro - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCarroComprasCatPro - Problema SQL (close)", e);
			}
		}

		return l_cat;

	}	
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getClienteById(long)
	 */
	public cl.bbr.jumbocl.clientes.model.ClienteEntity getClienteByIdFO(long cliente_id) throws ClientesDAOException {

		cl.bbr.jumbocl.clientes.model.ClienteEntity cliente = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String cadQuery = "SELECT " +
							" CLI_ID, CLI_RUT, CLI_DV, CLI_NOMBRE, CLI_APELLIDO_PAT, " +
							" CLI_APELLIDO_MAT, CLI_CLAVE, CLI_EMAIL, CLI_FON_COD_1, " +
							" CLI_FON_NUM_1, CLI_FON_COD_2, CLI_FON_NUM_2, CLI_FON_COD_3, " +
							" CLI_FON_NUM_3, CLI_REC_INFO, CLI_FEC_CREA, CLI_ESTADO, " +
							" CLI_FEC_NAC, CLI_GENERO, CLI_FEC_ACT, CLI_PREGUNTA, " +
							" CLI_RESPUESTA, CLI_ENVIO_SMS, CLI_KEY_RECUPERA_CLAVE, " +
							" CLI_BLOQUEO, CLI_ENVIO_MAIL, COL_RUT, CLI_MOD_DATO " +
							" FROM FODBA.FO_CLIENTES LEFT JOIN FODBA.FO_COLABORADOR ON CLI_RUT = COL_RUT WHERE CLI_ID = ? ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(cadQuery  + " WITH UR");
			stm.setLong(1, cliente_id);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			if (rs.next()) {

				logger.logData( "getClienteById", rs);

				cliente = new cl.bbr.jumbocl.clientes.model.ClienteEntity();
				cliente.setId(new Long(rs.getLong("cli_id")));
				cliente.setRut(new Long(rs.getLong("cli_rut")));
				cliente.setDv(new Character(rs.getString("cli_dv").charAt(0)));
				cliente.setEstado(new Character(rs.getString("cli_estado").charAt(0)));
				cliente.setClave(rs.getString("cli_clave"));
				cliente.setNombre(rs.getString("cli_nombre"));
				cliente.setApellido_pat(rs.getString("cli_apellido_pat"));
				cliente.setApellido_mat(rs.getString("cli_apellido_mat"));
				cliente.setEmail(rs.getString("cli_email"));
				cliente.setFec_crea(rs.getTimestamp("cli_fec_crea"));
				cliente.setFec_nac(rs.getDate("cli_fec_nac"));
				cliente.setFon_cod_1(rs.getString("cli_fon_cod_1"));
				cliente.setFon_cod_2(rs.getString("cli_fon_cod_2"));
				cliente.setFon_cod_3(rs.getString("cli_fon_cod_3"));
				cliente.setFon_num_1(rs.getString("cli_fon_num_1"));
				cliente.setFon_num_2(rs.getString("cli_fon_num_2"));
				cliente.setFon_num_3(rs.getString("cli_fon_num_3"));
				cliente.setGenero(new Character(rs.getString("cli_genero").charAt(0)));
				cliente.setRec_info(new Long(rs.getLong("cli_rec_info")));
				cliente.setFec_act(rs.getTimestamp("cli_fec_act"));
				cliente.setPregunta( rs.getString("cli_pregunta") );
				cliente.setRespuesta( rs.getString("cli_respuesta") );
				cliente.setRecibeSms( rs.getLong("cli_envio_sms") );
				cliente.setCli_mod_dato(rs.getString("CLI_MOD_DATO"));
				cliente.setRecibeEMail( rs.getLong("cli_envio_mail") );			
                cliente.setKey_CambioClave( rs.getString("CLI_KEY_RECUPERA_CLAVE"));

                if(rs.getInt("COL_RUT")==0)
                    cliente.setColaborador(false);
                else
                    cliente.setColaborador(true);
              
			}
		} catch (SQLException ex) {
			logger.error("getClienteById - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getClienteById - Problema General", ex);
			
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getClienteById - Problema SQL (close)", e);
			}
		}

		return cliente;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getClienteByRut(long)
	 */
	public cl.bbr.jumbocl.clientes.model.ClienteEntity getClienteByRutFO(long rut) throws ClientesDAOException {
		cl.bbr.jumbocl.clientes.model.ClienteEntity cliente = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String cadQuery= "SELECT CLI_ID, CLI_RUT, CLI_DV, CLI_NOMBRE, CLI_APELLIDO_PAT, "
                           + "       CLI_APELLIDO_MAT, CLI_CLAVE, CLI_EMAIL, CLI_FON_COD_1, "
                           + "       CLI_FON_NUM_1, CLI_FON_COD_2, CLI_FON_NUM_2, CLI_REC_INFO, "
                           + "       CLI_FEC_CREA, CLI_ESTADO, CLI_FEC_NAC, CLI_GENERO, "
                           + "       CLI_FEC_ACT, CLI_PREGUNTA, CLI_RESPUESTA, CLI_INTENTOS, "
                           + "       CLI_FEC_LOGIN, CLI_KEY_RECUPERA_CLAVE " +
                           //[20121107avc
                           ", COL_RUT FROM FO_CLIENTES LEFT JOIN FODBA.FO_COLABORADOR ON CLI_RUT=COL_RUT "
						   //]20121107avc
						   + "WHERE CLI_RUT  = ? "
						   + "  AND CLI_TIPO = 'P' ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(cadQuery  + " WITH UR");
			stm.setLong(1, rut);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			if (rs.next()) {

				logger.logData( "getClienteByRut", rs);

				cliente = new cl.bbr.jumbocl.clientes.model.ClienteEntity();
				cliente.setId(new Long(rs.getLong("cli_id")));
				cliente.setRut(new Long(rs.getLong("cli_rut")));
				cliente.setDv(new Character(rs.getString("cli_dv").charAt(0)));
				cliente.setEstado(new Character(rs.getString("cli_estado").charAt(0)));
				cliente.setClave(rs.getString("cli_clave"));
				cliente.setNombre(rs.getString("cli_nombre"));
				cliente.setApellido_pat(rs.getString("cli_apellido_pat"));
				cliente.setApellido_mat(rs.getString("cli_apellido_mat"));
				cliente.setEmail(rs.getString("cli_email"));
				cliente.setFec_nac(rs.getDate("cli_fec_nac"));
				cliente.setFon_cod_1(rs.getString("cli_fon_cod_1"));
				cliente.setFon_cod_2(rs.getString("cli_fon_cod_2"));
				cliente.setFon_num_1(rs.getString("cli_fon_num_1"));
				cliente.setFon_num_2(rs.getString("cli_fon_num_2"));
				cliente.setGenero(new Character(rs.getString("cli_genero").charAt(0)));
				cliente.setRec_info(new Long(rs.getLong("cli_rec_info")));
				cliente.setPregunta( rs.getString("cli_pregunta") );
				cliente.setRespuesta( rs.getString("cli_respuesta") );
				cliente.setIntento( new Long(rs.getLong("cli_intentos")) );
				cliente.setFec_login( rs.getTimestamp("cli_fec_login") );
                if (rs.getString("CLI_KEY_RECUPERA_CLAVE") != null){
                    cliente.setKey_CambioClave( rs.getString("CLI_KEY_RECUPERA_CLAVE") );
                }else{
                    cliente.setKey_CambioClave("");
                }
                
//              [20121107avc
                if(rs.getInt("COL_RUT") == 0)
                    cliente.setColaborador(false);
                else
                    cliente.setColaborador(true);
                //]20121107avc
				
			}
		} catch (SQLException ex) {
			logger.error("getClienteByRut - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getClienteByRut - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getClienteByRut - Problema SQL (close)", e);
			}
		}

		return cliente;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getClienteById(long)
	 */
	public boolean desinscribeMailFO(ClienteDTO cliente) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		int flag = 0;
		try {
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("update fo_clientes set cli_envio_mail = 0 where cli_rut = ? and upper(cli_email) = ?");
			stm.setLong(1, cliente.getRut());
			stm.setString(2, cliente.getEmail().toUpperCase());
			logger.debug("SQL: update fo_clientes set cli_envio_mail = 0 where cli_rut = " + cliente.getRut() + " and upper(cli_email) = " + cliente.getEmail().toUpperCase());
			flag = stm.executeUpdate();
		} catch (SQLException ex) {
			logger.error("desinscribeMail - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("desinscribeMail - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("desinscribeMail - Problema SQL (close)", e);
			}
		}
		
		if (flag == 0) {
			return false;
        }
		return true;
	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getTiposCalle()
	 */
	public List getTiposCalleFO() throws ClientesDAOException {

		List lista = new ArrayList();
		cl.bbr.jumbocl.clientes.model.TipoCalleEntity entidad = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("SELECT TIP_ID, TIP_NOMBRE, TIP_ESTADO " +
											"FROM FO_TIPO_CALLE " +
											"WHERE TIP_ESTADO = 'A' WITH UR");
			
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {

				logger.logData( "getTiposCalle", rs);

				entidad = new cl.bbr.jumbocl.clientes.model.TipoCalleEntity();
				entidad.setId(new Long(rs.getString("tip_id")));
				entidad.setNombre(rs.getString("tip_nombre"));
				entidad.setEstado(new Character(rs.getString("tip_estado").charAt(0)));
				lista.add(entidad);
			}
		} catch (SQLException ex) {
			logger.error("getTiposCalle - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getTiposCalle - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getTiposCalle - Problema SQL (close)", e);
			}
		}

		return lista;

	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getCarroCompra(long, java.lang.String)
	 */
   


    public List getCarroCompraFO(long cliente_id, String local, String idSession ) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        CarroCompraDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
        
        if( local.compareTo("-1") != 0 ) {
            // Cuando se quiere saber si hay productos solamente
            /*query = "SELECT car_id, car_cantidad, car_nota, car_tipo_sel, pro_id, pro_des_corta, pro_inter_max, " +
                    "pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, " +
                    "pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2) as catsap, substr(id_catprod,3,2) as id_rubro, " +
                    "max(cod_barra) as barra " +
                    "FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
                    "inner join fo_precios_locales on pre_pro_id = pro_id " +
                    "inner join fo_unidades_medida on uni_id = pro_uni_id " +
                    "inner join fo_marcas on mar_id = pro_mar_id " +  
                    "inner join bo_productos pro on pro.id_producto = pro_id_bo " +
                    "inner join bo_codbarra cod on cod.id_producto = pro.id_producto " +  
                    "where car_cli_id = ? and pre_loc_id = ? and pro_estado = 'A' and pre_estado = 'A' ";*/
            // Cuando se quiere saber si hay productos solamente
            query = "SELECT car_id, car_cantidad, car_nota, car_tipo_sel, car_fec_crea, pro_id, pro_des_corta, pro_inter_max, " +
                    "pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, " +
                    "pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2) as catsap, substr(id_catprod,3,2) as id_rubro, " +
                    "max(cod_barra) as barra " +
                    "FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
                    
                    //Agregado para no considerar productos sin categorias. 
                    //diferencia montos FO y medios de pago.
                    "inner join fo_productos_categorias prcat on (car_pro_id = prcat.prca_pro_id) " +
                    "inner join fo_categorias ter on ter.cat_id = prcat.prca_cat_id and ter.cat_estado = 'A'" +
                    "inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
                    "inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A')" +
                    
                    "inner join fo_precios_locales on pre_pro_id = pro_id " +
                    "inner join fo_unidades_medida on uni_id = pro_uni_id " +
                    "inner join fo_marcas on mar_id = pro_mar_id " +  
                    "inner join bo_productos pro on pro.id_producto = pro_id_bo " +
                    "inner join bo_codbarra cod on cod.id_producto = pro.id_producto " +  
                    "where car_cli_id = ? and pre_loc_id = ? and pro_estado = 'A' and pre_estado = 'A' ";
            if ( cliente_id == 1 ) {
                query += "AND CAR_INVITADO_ID=" + idSession;
            }
            query +=" group by car_id, car_cantidad, car_nota, car_tipo_sel, car_fec_crea, pro_id, pro_des_corta, pro_inter_max, pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2), substr(id_catprod,3,2) " +
                    "order by pro_tipo_producto ";
        } else {
            query = "SELECT car_id, car_cantidad, car_nota, car_tipo_sel, pro_id, pro_des_corta, pro_inter_max, " +
                    "pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, " +
                    "pro_id_bo, pro_estado, '100' as pre_stock, '0' as pre_valor  " +
                    "FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
                    "where car_cli_id = ? and pro_estado = 'A' ";
            if ( cliente_id == 1 ) {
                query += "AND CAR_INVITADO_ID=" + idSession;
            }
            query +=" order by pro_tipo_producto ";
        }
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query  + " WITH UR");
            stm.setLong(1, cliente_id);
            if( local.compareTo("-1") != 0 )
                stm.setLong(2, Long.parseLong(local));
			logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                
                logger.logData( "getCarroCompra", rs);

                dto = new CarroCompraDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                String tipo = (Utils.isEmpty(rs.getString("car_tipo_sel")))? "W":rs.getString("car_tipo_sel");
                dto.setTipoSel(tipo);
                dto.setCar_fec_crea(rs.getString("car_fec_crea"));
                if( local.compareTo("-1") != 0 ) { // Si es para mostrar todos los productos
                    if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                        dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                    dto.setStock(rs.getLong("pre_stock"));
                    dto.setPrecio(rs.getDouble("pre_valor"));
                    dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                    dto.setUnidad_nombre( rs.getString("uni_desc") );
                    dto.setNom_marca(rs.getString("mar_nombre"));
                    dto.setCatsap(rs.getString("catsap"));
                    dto.setCodbarra(rs.getString("barra"));
                    dto.setId_rubro(rs.getInt("id_rubro"));
                }
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                
                if ( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                    dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }                
                if ( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                
                lista.add(dto);
            }
        } catch (SQLException ex) {
            logger.error("getCarroCompra - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroCompra - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
				if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroCompra - Problema SQL (close)", e);
            }
        }
        return lista;
    }
    
    /*
     * Crea el cliente desde la sesión de invitado para poder asociar el pedido y guardar la información
     */
    public long creaClienteDesdeInvitadoFO(ClienteDTO cliente) throws ClientesDAOException {
        Connection conexion = null;        
        PreparedStatement stm1 = null;
        ResultSet rs = null;
        long llave = 0;
        try {
        	conexion = JdbcDAOFactory.getConexion();
            Calendar cal = new GregorianCalendar();
            long ahora = cal.getTimeInMillis();
            conexion.setAutoCommit(true);
            /*
             * Inserta los datos del cliente
             */
            String inserta = "insert into fo_clientes (cli_email, cli_fec_crea, cli_estado, cli_fec_act, cli_envio_sms, cli_rut, cli_dv,"
                             + "cli_nombre, cli_apellido_pat, cli_apellido_mat, cli_fec_nac, cli_genero, CLI_FON_COD_2, CLI_FON_NUM_2) "
                             + "values ( "
                             + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
                             + ")";
                      
            stm1 = conexion.prepareStatement(inserta, Statement.RETURN_GENERATED_KEYS);
            stm1.setString(1, Utils.stringToDbFO(cliente.getEmail(),50));
            stm1.setTimestamp(2, new Timestamp(ahora));
            stm1.setString(3, "A");
            stm1.setTimestamp(4, new Timestamp(ahora));
            stm1.setLong(5, cliente.getRecibeSms());
            stm1.setLong(6, cliente.getRut());
            stm1.setString(7, cliente.getDv());
            stm1.setString(8, cliente.getNombre());
            stm1.setString(9, cliente.getApellido_pat());
            stm1.setString(10, cliente.getApellido_mat());
            stm1.setTimestamp(11, new Timestamp(cliente.getFec_nac()));
            stm1.setString(12, cliente.getGenero());
            stm1.setString(13, cliente.getFon_cod_2());
            stm1.setString(14, cliente.getFon_num_2());
                
            logger.debug("SQL: " + stm1.toString());
            if (stm1.executeUpdate() == 0) {
                logger.debug("creaClienteDesdeInvitado - No inserta ningún cliente pero sin excepciones");
                throw new ClientesDAOException("Se recupera 0 registros modificados sin error");
            }
                
            rs = stm1.getGeneratedKeys();
            if (rs.next()) {
                llave = rs.getInt(1);
            } else {
                logger.error("creaClienteDesdeInvitado - Problema al recuperar id insertado");
                throw new ClientesDAOException("creaClienteDesdeInvitado - Problema al recuperar id insertado");
            }
        } catch (SQLException ex) {
            logger.error("creaClienteDesdeInvitado - SQL insert cliente", ex);
            throw new ClientesDAOException("creaClienteDesdeInvitado - SQL insert cliente", ex);
        } catch (Exception ex) {
            logger.error("creaClienteDesdeInvitado - Problema insert cliente", ex);
            throw new ClientesDAOException("creaClienteDesdeInvitado - Problema insert cliente");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm1 != null)
                    stm1.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (Exception ex1) {
                logger.error("creaClienteDesdeInvitado - Problema", ex1);
            }
        }
        return llave;
    }

    /*
     * Crea la dirección del cliente Invitado
     */
    public long creaDireccionDesdeInvitadoFO(long idCliente, DireccionesDTO desp) throws ClientesDAOException {
        Connection conexion = null;        
        PreparedStatement stm1 = null;
        ResultSet rs = null;
        long llavedir = 0;
        try {
        	conexion = JdbcDAOFactory.getConexion();
            Calendar cal = new GregorianCalendar();
            int id_poligono = 0;
            if (desp.getId_poligono()==0){
            	id_poligono = this.getPoligonoCeroFO( desp.getCom_id() );
            }else{
            	id_poligono = desp.getId_poligono();
            }
            
            conexion.setAutoCommit(true);            
            String sql = "INSERT INTO fo_direcciones (" +
                         "id_poligono, dir_com_id, dir_tip_id, dir_cli_id, dir_alias, " +
                         "dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fec_crea, dir_fnueva, dir_region_id" +
                         ") VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stm1 = conexion.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            int j=1;
            stm1.setInt(j++, id_poligono);
            stm1.setLong(j++, desp.getCom_id());
            stm1.setLong(j++, desp.getTipo_calle());
            stm1.setLong(j++, idCliente);
            stm1.setString(j++, Utils.stringToDbFO(desp.getAlias(),20));
            stm1.setString(j++, Utils.stringToDbFO(desp.getCalle(),200));
            stm1.setString(j++, Utils.stringToDbFO(desp.getNumero(),20));
            stm1.setString(j++, Utils.stringToDbFO(desp.getDepto(),20));
            stm1.setString(j++, Utils.stringToDbFO(desp.getComentarios(),200));
            stm1.setString(j++, "A");
            stm1.setTimestamp(j++, new Timestamp(cal.getTimeInMillis()));
            stm1.setString(j++,"N");
            stm1.setLong(j++,desp.getReg_id());
            logger.debug("SQL: " + stm1.toString());
            if (stm1.executeUpdate() == 0) {
                logger.debug("creaDireccionDesdeInvitado - Insert dirección despacho sin excepciones");
                throw new ClientesDAOException("creaDireccionDesdeInvitado - Insert dirección despacho sin excepciones");
            }

            rs = stm1.getGeneratedKeys();
            if (rs.next()) {
                llavedir = rs.getInt(1);
            } else {
                logger.error("creaDireccionDesdeInvitado - Problema al recuperar id direccion insertado");
                throw new ClientesDAOException("creaDireccionDesdeInvitado - Problema al recuperar id direccion insertado");
            }
        } catch (SQLException ex) {
            logger.error("SQL insert dirección (creaDireccionDesdeInvitado) ", ex);
            throw new ClientesDAOException("SQL insert dirección (creaDireccionDesdeInvitado) ");
        } catch (Exception ex) {
            logger.error("Problema insert direccion (creaDireccionDesdeInvitado)", ex);
            throw new ClientesDAOException("Problema insert direccion (creaDireccionDesdeInvitado)");
        } finally{
            try {
                if (rs != null)
                    rs.close();
                if (stm1 != null)
                    stm1.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (Exception ex1) {
                logger.error("creaDireccionDesdeInvitado - Problema", ex1);
            }
        }
        return llavedir;
    }
    
    

    /*
     * Se asocia el carro de compras al Nuevo ID de Cliente
     */
    public boolean reasignaCarroDelInvitadoFO(long idCliente, long idInvitado) throws ClientesDAOException {
        Connection conexion = null;        
        PreparedStatement stmt = null;
        boolean result = false;
        try {
        	conexion = JdbcDAOFactory.getConexion();
            conexion.setAutoCommit(false);
            
            String SQL = "UPDATE FODBA.FO_CARRO_COMPRAS CC "
                       + "   SET CC.CAR_CLI_ID = ? "
                       + " WHERE CC.CAR_INVITADO_ID = ?";
            try {
                stmt = conexion.prepareStatement(SQL);
                
                stmt.setLong(1, idCliente);
                stmt.setLong(2, idInvitado);

                
                logger.debug("SQL: " + stmt.toString());
                if (stmt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (SQLException ex) {
                logger.error("reasignaCarroDelInvitado - SQL Update FO_CARRO_COMPRAS", ex);
                throw new ClientesDAOException("reasignaCarroDelInvitado - SQL Update FO_CARRO_COMPRAS", ex);
            } catch (Exception ex) {
                logger.error("reasignaCarroDelInvitado - Problema Update FO_CARRO_COMPRAS", ex);
                throw new ClientesDAOException("reasignaCarroDelInvitado - Problema Update FO_CARRO_COMPRAS");
            }
            conexion.commit();
            conexion.setAutoCommit(true);
        } catch (SQLException ex) {
            logger.error("Problema con SQL (reasignaCarroDelInvitado)", ex);
            logger.info("rollback");
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (Exception ex1) {
                logger.error("reasignaCarroDelInvitado - Problema", ex1);
            }
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("reasignaCarroDelInvitado - Problema", ex);
            logger.info("rollback");
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (Exception ex1) {
                logger.error("reasignaCarroDelInvitado - Problema", ex1);
            }
            throw new ClientesDAOException(ex);
        }
        finally{
            try {
                if (stmt != null)
                    stmt.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (Exception ex1) {
                logger.error("creaClienteDesdeInvitado - Problema", ex1);
            }
        }
        return result;
    }
    
    
    /*
     * Se asocian los criterios de sustitución al Nuevo ID de Cliente
     */
    public boolean reasignaSustitutosDelInvitadoFO(long idCliente, long idInvitado) throws ClientesDAOException {
        Connection conexion = null;        
        PreparedStatement stmt = null;
        boolean result = false;
        try {
        	conexion = JdbcDAOFactory.getConexion();
            conexion.setAutoCommit(false);
            
            String SQL = "UPDATE FODBA.FO_SUSTITUTOS_CLIENTES SC "
                       + "   SET SC.CLI_ID = ? "
                       + " WHERE SC.SC_INVITADO_ID = ?";
            try {
                stmt = conexion.prepareStatement(SQL);
                
                stmt.setLong(1, idCliente);
                stmt.setLong(2, idInvitado);

                logger.debug("SQL: " + stmt.toString());
                if (stmt.executeUpdate() > 0) {
                    result = true;
                }
            } catch (SQLException ex) {
                logger.error("reasignaSustitutosDelInvitado - SQL Update FO_SUSTITUTOS_CLIENTES", ex);
                throw new ClientesDAOException("reasignaSustitutosDelInvitado - SQL Update FO_SUSTITUTOS_CLIENTES", ex);
            } catch (Exception ex) {
                logger.error("reasignaSustitutosDelInvitado - Problema Update FO_SUSTITUTOS_CLIENTES", ex);
                throw new ClientesDAOException("reasignaSustitutosDelInvitado - Problema Update FO_SUSTITUTOS_CLIENTES");
            }
            conexion.commit();
            conexion.setAutoCommit(true);
        } catch (SQLException ex) {
            logger.error("Problema con SQL (reasignaSustitutosDelInvitado)", ex);
            logger.info("rollback");
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (Exception ex1) {
                logger.error("reasignaSustitutosDelInvitado - Problema", ex1);
            }
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("reasignaSustitutosDelInvitado - Problema", ex);
            logger.info("rollback");
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (Exception ex1) {
                logger.error("reasignaSustitutosDelInvitado - Problema", ex1);
            }
            throw new ClientesDAOException(ex);
        }
        finally{
            try {
                if (stmt != null)
                    stmt.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (Exception ex1) {
                logger.error("creaClienteDesdeInvitado - Problema", ex1);
            }
        }
        return result;
    }

    
    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#isCarroComprasEmpty(long, java.lang.String)
     */
    public boolean isCarroComprasEmptyFO(long cliente_id, String idSession ) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = null;
        long cantidad = 0;
        boolean respuesta = true;
        
        query = "SELECT COUNT(*) as cantidad " +
                "FROM fo_carro_compras " +
                "where car_cli_id = ? ";
        if ( cliente_id == 1 ) {
            query += "AND CAR_INVITADO_ID=" + idSession;
        }
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " with ur");
            
            stm.setLong(1, cliente_id);
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            
            while (rs.next()) {
            	cantidad = rs.getLong("cantidad");
            }
            
            if(cantidad > 0)
            	respuesta = false;
        } catch (SQLException ex) {
            logger.error("carroComprasGetCantidadProductos - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("carroComprasGetCantidadProductos - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("carroComprasGetCantidadProductos - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }
    
    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#carroComprasGetCantidadProductos(long, java.lang.String)
     */
    public long carroComprasGetCantidadProductosFO(long cliente_id, String idSession ) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = null;
        long cantidad = 0;
        
        query = "SELECT sum(car_cantidad) as cantidad " +
                "FROM fo_carro_compras " +
                "where car_cli_id = ? ";
        if ( cliente_id == 1 ) {
            query += "AND CAR_INVITADO_ID=" + idSession;
        }
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " with ur");
            
            stm.setLong(1, cliente_id);
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            
            while (rs.next()) {
                cantidad = rs.getLong("cantidad");
            }
        } catch (SQLException ex) {
            logger.error("carroComprasGetCantidadProductos - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("carroComprasGetCantidadProductos - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("carroComprasGetCantidadProductos - Problema SQL (close)", e);
            }
        }
        return cantidad;
    }    
    

    
    //riffo
    public List getCarroCompraCheckOutFO(long cliente_id, String local, String idSession ) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        CarroCompraDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;       
    
            // Cuando se quiere saber si hay productos solamente
            query = "SELECT car_pro_id, COUNT(car_pro_id)as cont, MAX(car_id) as idCarro FROM fodba.fo_carro_compras "+ 
                    "WHERE car_cli_id="+cliente_id+" GROUP BY car_pro_id "+
                    "having  count(car_pro_id)>1";
            
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " with ur ");
            //stm.setLong(1, cliente_id);
                        
            logger.debug("SQL CheckOut: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                
                logger.logData( "getCarroCompraCheckOut", rs);

                dto = new CarroCompraDTO();
                dto.setId(rs.getLong("idCarro"));
                //dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("car_pro_id"));
                                
                lista.add(dto);
            }
        } catch (SQLException ex) {
            logger.error("getCarroCompraCheckOut - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroCompraCheckOut - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroCompraCheckOut - Problema SQL (close)", e);
            }
        }
             return lista;
    }
    
    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#getCarroComprasPorCategorias(long, java.lang.String, java.lang.String)
     */
    public List getCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession ) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        MiCarroDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
        //INICIO INDRA 22-10-2012
        query = "select  min(inter.cat_id) as inter_id, min(inter.cat_nombre) as inter_nom, min(ter.cat_id) as ter_id, min(ter.cat_nombre) as ter_nom, " +
        		"pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, cc.car_fec_crea, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
                "pr.pro_pesable, pr.pro_id_bo, pr.pro_estado, pre.pre_valor, pre.pre_tienestock, pre.pre_stock,um.uni_cantidad,um.uni_desc, max(cod.cod_barra) as barra, substr(bopro.id_catprod,1,2) as catsap, substr(bopro.id_catprod,3,2) as id_rubro, " + 
				"pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, pr.pro_id_desp, pr.PRO_ESTADO estadoProducto, pre.PRE_ESTADO estadoPreciosLocales " +
				"from FODBA.fo_carro_compras cc inner join FODBA.fo_productos pr on (cc.car_pro_id = pr.pro_id) " +
				"inner join FODBA.fo_precios_locales pre on (pre.pre_pro_id = pr.pro_id and pre.pre_loc_id = " + local + ") " + 
				"inner join FODBA.fo_unidades_medida um on (um.uni_id = pr.pro_uni_id) " +
				"inner join FODBA.fo_marcas ma on (pr.pro_mar_id = ma.mar_id) " +
				"inner join FODBA.fo_productos_categorias prca on (pr.pro_id = prca.prca_pro_id) " +
				"inner join FODBA.fo_categorias ter on (prca.prca_cat_id = ter.cat_id and ter.cat_estado = 'A') " +
				"inner join FODBA.fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
				"inner join FODBA.fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
				"inner join BODBA.bo_productos bopro on (bopro.id_producto = pr.pro_id_bo) " +
				"inner join BODBA.bo_codbarra cod on (cod.id_producto = bopro.id_producto) " +
        "left join FODBA.fo_sustitutos_clientes sc on (pr.pro_id = sc.pro_id and cc.car_cli_id = sc.cli_id ";
        //FIN INDRA 22-10-2012
        if ( cliente_id == 1 ) {
            query += " and sc_invitado_id=" + idSession + ") ";
        } else {
            query += ") ";
        }
        query += "left join FODBA.fo_sustitutos_criterio scr on (sc.id_criterio = scr.id_criterio) " +
        "left join " +
        "(select car_pro_id, max(ter.cat_id) as ter_id " +
        "from FODBA.fo_carro_compras cc " +
        "inner join FODBA.fo_productos_categorias prcat on (cc.car_pro_id = prcat.prca_pro_id) " +
        "inner join FODBA.fo_categorias ter on ter.cat_id = prcat.prca_cat_id and ter.cat_estado = 'A' " +
        "inner join FODBA.fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
        "inner join FODBA.fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
        "WHERE cc.car_cli_id = " + cliente_id;
        if ( cliente_id == 1 ) {
            query += " and car_invitado_id=" + idSession;
        }
        query += " group by cc.car_pro_id " + 
        ") as x " +
        "on x.ter_id = ter.cat_id " +
        "where car_cli_id = " + cliente_id;
        if ( cliente_id == 1 ) {
            query += " and car_invitado_id=" + idSession;
        }
        //INICIO INDRA 22-10-2012
        query += " group by pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, cc.car_fec_crea, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
        "pr.pro_pesable,  pr.pro_id_bo, pr.pro_estado,pre.pre_valor, pre.pre_tienestock, pre.pre_stock, um.uni_cantidad, um.uni_desc, substr(bopro.id_catprod,1,2), substr(bopro.id_catprod,3,2), " +
        "pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, pr.pro_id_desp, pr.PRO_ESTADO, pre.PRE_ESTADO " +
        "order by inter_nom, ter_nom ";    
        // FIN INDRA 22-10-2012
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query  + " WITH UR");
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                //INICIO INDRA 22-10-2012
                logger.logData( "getCarroComprasPorCategorias", rs);

                dto = new MiCarroDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                
                String tipo = (Utils.isEmpty(rs.getString("car_tipo_sel")) || Utils.isBlank(rs.getString("car_tipo_sel")))? "W":rs.getString("car_tipo_sel"); 
                dto.setTipoSel(tipo);                
                //dto.setTipoSel(rs.getString("car_tipo_sel"));
                dto.setCar_fec_crea(rs.getString("car_fec_crea"));
                
                dto.setIdProDesp(rs.getLong("pro_id_desp"));
                if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                    dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                dto.setStock(rs.getLong("pre_stock"));
                dto.setPrecio(rs.getDouble("pre_valor"));
                if (rs.getInt("pre_tienestock") == 0)
                    dto.setTieneStock(false);
                else
                    dto.setTieneStock(true);
                dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                dto.setUnidad_nombre( rs.getString("uni_desc") );
                dto.setNom_marca(rs.getString("mar_nombre"));                   
                dto.setCatsap(rs.getString("catsap"));
                
                dto.setId_rubro(rs.getInt("id_rubro"));
                
                dto.setCodbarra(rs.getString("barra"));
                dto.setCodSap(rs.getString("pro_cod_sap"));
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                
                if ( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                    dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }                
                if ( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                dto.setIdTerminal(rs.getLong("ter_id"));
                dto.setIdIntermedia(rs.getLong("inter_id"));
                dto.setNombreTerminal(rs.getString("ter_nom"));
                dto.setNombreIntermedia(rs.getString("inter_nom"));
                if (rs.getString("id_criterio") != null) {
                    dto.setIdCriterio(rs.getLong("id_criterio"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    if (rs.getString("desc_criterio") != null)
                        dto.setSustitutoCliente(rs.getString("desc_criterio"));
                    dto.setAsignoCliente(rs.getString("asigno_cliente"));
                }
                dto.setEstadoProducto(rs.getString("estadoProducto"));
                dto.setEstadoPreciosLocales(rs.getString("estadoPreciosLocales"));
                lista.add(dto);
                //FIN INDRA 22-10-2012
            }
        } catch (SQLException ex) {
            logger.error("getCarroComprasPorCategorias - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroComprasPorCategorias - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroComprasPorCategorias - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#eliminaProdCarroNoDisp(long, java.lang.String, java.lang.String)
     */
    public void eliminaProdCarroNoDispFO(long cliente_id, String local, String idSession ) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        String query = null;
        
        query = "DELETE FROM fo_carro_compras ";
        query += "WHERE car_id in (" + 
                "SELECT car_id from fo_carro_compras cc INNER JOIN fo_productos pr ON (cc.car_pro_id = pr.pro_id) " +
                "INNER JOIN fo_precios_locales pre ON (pre.pre_pro_id = pr.pro_id and pre.pre_loc_id = " + local + ") " +
                "WHERE cc.car_cli_id = " + cliente_id + " " +
                "AND pre.pre_tienestock = 0";
        
        if ( cliente_id == 1 ) {
            query += " AND cc.car_invitado_id=" + idSession;
        }
        query += ")";
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query);
            logger.debug("SQL: " + stm.toString());
            stm.executeUpdate();
        } catch (SQLException ex) {
            logger.error("eliminaProdCarroNoDisp - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("eliminaProdCarroNoDisp - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("eliminaProdCarroNoDisp - Problema SQL (close)", e);
            }
        }
    }    
    
    
    
    
    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#getCarroComprasPorCategorias(long, java.lang.String, java.lang.String, java.lan.String)
     */
    public List getCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String filtro) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        MiCarroDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
        
        query = "select  min(inter.cat_id) as inter_id, min(inter.cat_nombre) as inter_nom, min(ter.cat_id) as ter_id, min(ter.cat_nombre) as ter_nom, " +
        "pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
        "pr.pro_pesable, pr.pro_id_bo, pr.pro_estado, pre.pre_valor, pre.pre_tienestock, pre.pre_stock,um.uni_cantidad,um.uni_desc, max(cod.cod_barra) as barra, substr(bopro.id_catprod,1,2) as catsap, " + 
        "pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, PR.PRO_IMAGEN_FICHA " +
        "from fo_carro_compras cc inner join fo_productos pr on (cc.car_pro_id = pr.pro_id and pr.pro_estado = 'A') " +
        "inner join fo_precios_locales pre on (pre.pre_pro_id = pr.pro_id and pre.pre_loc_id = " + local + ") " + 
        "inner join fo_unidades_medida um on (um.uni_id = pr.pro_uni_id) " +
        "inner join fo_marcas ma on (pr.pro_mar_id = ma.mar_id) " +
        "inner join fo_productos_categorias prca on (pr.pro_id = prca.prca_pro_id) " +
        "inner join fo_categorias ter on (prca.prca_cat_id = ter.cat_id and ter.cat_estado = 'A') " +
        "inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
        "inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
        "inner join bo_productos bopro on (bopro.id_producto = pr.pro_id_bo) " +
        "inner join bo_codbarra cod on (cod.id_producto = bopro.id_producto) " +
        "left join fo_sustitutos_clientes sc on (pr.pro_id = sc.pro_id and cc.car_cli_id = sc.cli_id ";
        if ( cliente_id == 1 ) {
            query += " and sc_invitado_id=" + idSession + ") ";
        } else {
            query += ") ";
        }
        query += "left join fo_sustitutos_criterio scr on (sc.id_criterio = scr.id_criterio) ";
        
        query += "left join " +
        "(select car_pro_id, max(ter.cat_id) as ter_id " +
        "from fo_carro_compras cc " +
        "inner join fo_productos_categorias prcat on (cc.car_pro_id = prcat.prca_pro_id) " +
        "inner join fo_categorias ter on ter.cat_id = prcat.prca_cat_id and ter.cat_estado = 'A' " +
        "inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
        "inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
        "WHERE cc.car_cli_id = " + cliente_id;
        if ( cliente_id == 1 ) {
            query += " and car_invitado_id=" + idSession;
        }
        query += " group by cc.car_pro_id " + 
        ") as x " +
        "on x.ter_id = ter.cat_id " +
        "where car_cli_id = " + cliente_id;
        if ( cliente_id == 1 ) {
            query += " and car_invitado_id=" + idSession;
        }
        if (!filtro.equals("1")){
            query+= " AND SC.ID_CRITERIO IS  NULL ";
        }
        query += " group by pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
        "pr.pro_pesable,  pr.pro_id_bo, pr.pro_estado,pre.pre_valor, pre.pre_tienestock, pre.pre_stock, um.uni_cantidad, um.uni_desc, substr(bopro.id_catprod,1,2), " +
        "pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, PR.PRO_IMAGEN_FICHA " +
        "order by inter_nom, ter_nom";    
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " with ur ");
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                
                logger.logData( "getCarroComprasPorCategorias", rs);

                dto = new MiCarroDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                dto.setTipoSel(rs.getString("car_tipo_sel"));
                if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                    dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                dto.setStock(rs.getLong("pre_stock"));
                dto.setPrecio(rs.getDouble("pre_valor"));
                if (rs.getInt("pre_tienestock") == 0)
                    dto.setTieneStock(false);
                else
                    dto.setTieneStock(true);
                dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                dto.setUnidad_nombre( rs.getString("uni_desc") );
                dto.setNom_marca(rs.getString("mar_nombre"));                   
                dto.setCatsap(rs.getString("catsap"));
                dto.setCodbarra(rs.getString("barra"));
                dto.setCodSap(rs.getString("pro_cod_sap"));
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                
                if ( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                    dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }                
                if ( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                dto.setIdTerminal(rs.getLong("ter_id"));
                dto.setIdIntermedia(rs.getLong("inter_id"));
                dto.setNombreTerminal(rs.getString("ter_nom"));
                dto.setNombreIntermedia(rs.getString("inter_nom"));
                if (rs.getString("id_criterio") != null) {
                    dto.setIdCriterio(rs.getLong("id_criterio"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    if (rs.getString("desc_criterio") != null)
                        dto.setSustitutoCliente(rs.getString("desc_criterio"));
                    dto.setAsignoCliente(rs.getString("asigno_cliente"));
                }
                dto.setImagen(rs.getString("PRO_IMAGEN_FICHA"));
                lista.add(dto);
            }
        } catch (SQLException ex) {
            logger.error("getCarroComprasPorCategorias - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroComprasPorCategorias - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroComprasPorCategorias - Problema SQL (close)", e);
            }
        }
        return lista;
    }
    
    
    
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#newCliente(cl.bbr.fo.clientes.dto.ClienteDTO, java.util.List)
	 */
	public String newClienteFO(ClienteDTO cliente, DireccionesDTO direccion) throws ClientesDAOException {
		Connection conexion = null;		
		PreparedStatement stmt = null;
		PreparedStatement stm1 = null;
		PreparedStatement stm2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
        ResultSet rs2 = null;
		long IdCliente = 0, IdDireccion = 0;
		try {
			conexion = JdbcDAOFactory.getConexion();
			Calendar cal = new GregorianCalendar();
			long ahora = cal.getTimeInMillis();

			conexion.setAutoCommit(false);
			
			//Revisa si existe el cliente
			String query = "select cli_id from fo_clientes where cli_rut = ? and cli_tipo = 'P' ";
			stmt = conexion.prepareStatement(query  + " WITH UR");
			stmt.setLong(1, cliente.getRut());
			rs = stmt.executeQuery();
			if (rs.next()) { 
				logger.info("Si existe el cliente en la BD no lo vuelve a insertar");
				conexion.commit();
				conexion.setAutoCommit(true);
				conexion.close();
				return "0-0";
			}
			
			/*
			 * Inserta los datos del cliente
			 */
			try {
				stm1 = conexion.prepareStatement(
								"insert into fo_clientes (cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, cli_clave, " +
								"cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_fon_cod_3, cli_fon_num_3, " +
								"cli_rec_info, cli_fec_crea, cli_estado, cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, " +
								"cli_envio_sms, CLI_ENVIO_MAIL,CLI_ID_FACEBOOK ) "
								+ "values ( "
								+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?"
								+ ")", Statement.RETURN_GENERATED_KEYS);
				
				stm1.setLong(1, cliente.getRut());
				stm1.setString(2, Utils.stringToDbFO(String.valueOf(cliente.getDv()),1));
				stm1.setString(3, Utils.stringToDbFO(cliente.getNombre(),50));
				stm1.setString(4, Utils.stringToDbFO(cliente.getApellido_pat(),50));
				stm1.setString(5, Utils.stringToDbFO(cliente.getApellido_mat(),50));
				stm1.setString(6, cliente.getClave());
				stm1.setString(7, Utils.stringToDbFO(cliente.getEmail(),50));
				stm1.setString(8, Utils.stringToDbFO(cliente.getFon_cod_1(),5));
				stm1.setString(9, Utils.stringToDbFO(cliente.getFon_num_1(),20));
				stm1.setString(10, Utils.stringToDbFO(cliente.getFon_cod_2(),5));
				stm1.setString(11, Utils.stringToDbFO(cliente.getFon_num_2(),20));
				stm1.setString(12, Utils.stringToDbFO(cliente.getFon_cod_3(),5));
				stm1.setString(13, Utils.stringToDbFO(cliente.getFon_num_3(),20));
				stm1.setInt(14, 1);
				stm1.setTimestamp(15, new Timestamp(ahora));
				stm1.setString(16, "A");
				stm1.setDate(17, new Date(cliente.getFec_nac()));
				stm1.setString(18, cliente.getGenero());
				stm1.setTimestamp(19, new Timestamp(ahora));
				stm1.setString(20, Utils.stringToDbFO(cliente.getPregunta() ,100));
				stm1.setString(21, Utils.stringToDbFO(cliente.getRespuesta() ,100));
				stm1.setLong(22, cliente.getRecibeSms());
				stm1.setLong(23, cliente.getRecibeEMail());
				stm1.setString(24, cliente.getId_facecebook());
                
				logger.debug("SQL: " + stm1.toString());
				if (stm1.executeUpdate() == 0) {
					logger.info("newCliente - No inserta ningún cliente pero sin excepciones");
					throw new ClientesDAOException("Se recupera 0 registros modificados sin error");
				}
			} catch (SQLException ex) {
				logger.error("newCliente - SQL insert cliente", ex);
				throw new ClientesDAOException("newCliente - SQL insert cliente", ex);
			} catch (Exception ex) {
				logger.error("newCliente - Problema insert cliente", ex);
				throw new ClientesDAOException("newCliente - Problema insert cliente");
			}

			/*
			 * Recupera información de la llave generada
			 */
			try {
				rs1 = stm1.getGeneratedKeys();
				if (rs1.next())
					IdCliente = rs1.getInt(1);
				else {
					logger.error("newCliente - Problema al recuperar id insertado");
					throw new ClientesDAOException("newCliente - Problema al recuperar id insertado");
				}
			} catch (SQLException ex) {
				logger.error("newCliente - SQL llave cliente", ex);
				throw new ClientesDAOException("newCliente - SQL llave cliente");
			} catch (Exception ex) {
				logger.error("newCliente - Problema llave cliente", ex);
				throw new ClientesDAOException("newCliente - Problema llave cliente");
			}

			/*
			 * Inserta las direcciones de despacho asociadas al cliente
			 */
			try {
				int id_poligono = 0;
				id_poligono = this.getPoligonoCeroFO( direccion.getCom_id() );
					
                String sql = "INSERT INTO fo_direcciones (" +
                             "id_poligono, dir_com_id, dir_tip_id, dir_cli_id, dir_alias, " +
                             "dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fec_crea, dir_fnueva" +
                             ") VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
				stm2 = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int j=1;
				stm2.setInt(j++, id_poligono);
				stm2.setLong(j++, direccion.getCom_id());
				stm2.setLong(j++, direccion.getTipo_calle());
				stm2.setLong(j++, IdCliente);
				stm2.setString(j++, Utils.stringToDbFO(direccion.getAlias(),20));
				stm2.setString(j++, Utils.stringToDbFO(direccion.getCalle(),200));
				stm2.setString(j++, Utils.stringToDbFO(direccion.getNumero(),20));
				stm2.setString(j++, Utils.stringToDbFO(direccion.getDepto(),20));
				stm2.setString(j++, Utils.stringToDbFO(direccion.getComentarios(),200));
				stm2.setString(j++, "A");
				stm2.setTimestamp(j++, new Timestamp(cal.getTimeInMillis()));
				stm2.setString(j++,"N");
				logger.debug("SQL: " + stm2.toString());
				if (stm2.executeUpdate() == 0) {
					logger.info("newCliente - Insert dirección despacho sin excepciones");
					throw new ClientesDAOException("newCliente - Insert dirección despacho sin excepciones");
				}
			} catch (SQLException ex) {
				logger.error("SQL insert dirección (newCliente) ", ex);
				throw new ClientesDAOException("SQL insert dirección (newCliente) ");
			} catch (Exception ex) {
				logger.error("Problema insert direccion (newCliente)", ex);
				throw new ClientesDAOException("Problema insert direccion (newCliente)");
			}
            
            /*
             * Recupera información de la llave generada
             */
            try {
                rs2 = stm2.getGeneratedKeys();
                if (rs2.next())
                    IdDireccion = rs2.getInt(1);
                else{
                    logger.error("newCliente - Problema al recuperar id insertado");
                    throw new ClientesDAOException("newCliente - Problema al recuperar id insertado");
                }
            } catch (SQLException ex) {
                logger.error("newCliente - SQL llave cliente", ex);
                throw new ClientesDAOException("newCliente - SQL llave cliente");
            } catch (Exception ex) {
                logger.error("newCliente - Problema llave cliente", ex);
                throw new ClientesDAOException("newCliente - Problema llave cliente");
            }
            
			conexion.commit();
			conexion.setAutoCommit(true);
		} catch (SQLException ex) {
			logger.error("Problema con SQL (newCliente)", ex);
			logger.info("rollback");
			try {
				conexion.rollback();
				conexion.setAutoCommit(true);
			} catch (Exception ex1) {
				logger.error("newCliente - Problema", ex1);
			}
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("newCliente - Problema", ex);
			logger.info("rollback");
			try {
				conexion.rollback();
				conexion.setAutoCommit(true);
			} catch (Exception ex1) {
				logger.error("newCliente - Problema", ex1);
			}
			throw new ClientesDAOException(ex);
		}
        finally{
            try {
                if (rs != null)
                    rs.close(); 
                if (rs1 != null)
                    rs1.close();
                if (rs2 != null)
                    rs2.close();
                if (stmt != null)
                    stmt.close();
                if (stm1 != null)
                    stm1.close();
                if (stm2 != null)
                    stm2.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (Exception ex1) {
                logger.error("newCliente - Problema", ex1);
            }
        }
        return IdCliente + "-" + IdDireccion;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#clienteExisteCarroCompras(long)
	 */
	public boolean clienteExisteCarroComprasFO(long cliente) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean res_bol = true;

		try {
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("	SELECT count(*) " +
											"	FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
											"	where car_cli_id = ? and pro_estado = 'A' with ur");
			stm.setLong(1, cliente);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {

				logger.logData( "clienteExisteCarroCompras", rs);

				if( rs.getLong(1) > 0 )
					res_bol = true;
				else
					res_bol = false;
			}
		} catch (SQLException ex) {
			logger.error("clienteExisteCarroCompras - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("clienteExisteCarroCompras - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("clienteExisteCarroCompras - Problema SQL (close)", e);
			}
		}
		return res_bol;

		
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#updateCarroCompra(long, long, double, java.lang.String)
	 */
	public void updateCarroCompraFO(long cliente_id, long carro_id, double cantidad, String nota, String idSession, String tipoSel) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		try {
			String sql = "";
			if ( nota.trim() != "" ) {
				sql = "UPDATE fo_carro_compras " +
                      "set car_cantidad = ?, car_fec_crea = ?, car_nota = ?, CAR_TIPO_SEL = ? " + 
                      "where car_cli_id = ? and car_id = ? ";
            } else {
				sql = "UPDATE fo_carro_compras " +
                      "set car_cantidad = ?, car_fec_crea = ?, CAR_TIPO_SEL = ? " +
                      "where car_cli_id = ? and car_id = ? ";
            }
            if ( cliente_id == 1 ) {
                sql += "and CAR_INVITADO_ID = " + idSession;
            }
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(sql);
			stm.setDouble(1, cantidad );
			Calendar cal = new GregorianCalendar();
			stm.setTimestamp( 2, new Timestamp( cal.getTimeInMillis() ) );
			if (nota != ""){
				stm.setString( 3, nota );
								
				if(tipoSel == null || Utils.isEmpty(tipoSel)){
					stm.setString(4, "W");
				}else{
					stm.setString(4, tipoSel);
				}
				
				stm.setLong(5, cliente_id);
				stm.setLong(6, carro_id);
				
			} else {
								
				if(tipoSel == null || Utils.isEmpty(tipoSel)){
					stm.setString(3, "W");
				}else{
					stm.setString(3, tipoSel);
				}
				
				stm.setLong(4, cliente_id);
				stm.setLong(5, carro_id);
			}
			
			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.info("updateCarroCompra - No modifica pero sin excepciones");
			}

		} catch (SQLException ex) {
			logger.error("updateCarroCompra - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("updateCarroCompra - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("updateCarroCompra - Problema SQL (close)", e);
			}
		}
		
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#deleteCarroCompra(long, long)
	 */
	public void deleteCarroCompraFO(long cliente_id, long carro_id, String idSession ) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		try {
			conexion = JdbcDAOFactory.getConexion();
            
            String sqlDel = "delete from fo_carro_compras " +
                            "where car_cli_id = ? and car_id = ? ";
            
            if ( cliente_id == 1 ) {
                sqlDel += "and CAR_INVITADO_ID = " + idSession;
            }
            
			stm = conexion.prepareStatement(sqlDel);
			stm.setLong(1, cliente_id);
			stm.setLong(2, carro_id);

			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.debug("deleteCarroCompra - No elimina pero sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("deleteCarroCompra - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("deleteCarroCompra - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("deleteCarroCompra - Problema SQL (close)", e);
			}
		}
		
	}

    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#modificarCantidadProductoMiCarro(long, long, double, String)
     */
    public void modificarCantidadProductoMiCarroFO(long cliente_id, long carro_id, double cantidad, String idSession, String tipoSel) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        try {
            conexion = JdbcDAOFactory.getConexion();
            
            String sqlDel = "update fo_carro_compras " +
                            "set car_cantidad = ?, CAR_TIPO_SEL = ? " +
                            "where car_cli_id = ? and car_id = ? ";
            
            if ( cliente_id == 1 ) {
                sqlDel += "and CAR_INVITADO_ID = " + idSession;
            }
            
            stm = conexion.prepareStatement(sqlDel);
            stm.setDouble(1, cantidad);
            
        	if(tipoSel == null || Utils.isEmpty(tipoSel)){
				stm.setString(2, "W");
			}else{
				stm.setString(2, tipoSel);
			}
        	
            stm.setLong(3, cliente_id);
            stm.setLong(4, carro_id);

            logger.debug("SQL: " + stm.toString());
            if (stm.executeUpdate() == 0) {
                logger.debug("modificarCantidadProductoMiCarro - No modifica pero sin excepciones");
            }
        } catch (SQLException ex) {
            logger.error("modificarCantidadProductoMiCarro - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("modificarCantidadProductoMiCarro - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("modificarCantidadProductoMiCarro - Problema SQL (close)", e);
            }
        }
        
    }
    
    
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#deleteCarroCompraProductoxId(long, long)
	 */
	public void deleteCarroCompraProductoxIdFO(long cliente_id, long id_producto, String idSession ) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		try {
			conexion = JdbcDAOFactory.getConexion();
            String sql = "delete from fo_carro_compras " +
                         "where car_cli_id = ? and car_pro_id = ? ";
            if ( cliente_id == 1 ) {
                sql += "and CAR_INVITADO_ID = " + idSession;
            }
            stm = conexion.prepareStatement(sql);
			stm.setLong(1, cliente_id);
			stm.setLong(2, id_producto);
			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.debug("deleteCarroCompraProductoxId - No elimina pero sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("deleteCarroCompraProductoxId - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("deleteCarroCompraProductoxId - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("deleteCarroCompraProductoxId - Problema SQL (close)", e);
			}
		}
		
	}
	
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getUltimasCompras(long)
	 */
	public List getUltimasComprasFO(long cliente_id)  throws ClientesDAOException {
		
		List lista = new ArrayList();
		UltimasComprasDTO compras = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			String cadQuery="	SELECT CH_ID, CH_ALIAS, CH_UNIDADES, " +
							"		CASE 	WHEN CH_TIPO='M' " +
							"				THEN 'Mi Lista' ELSE 'Compra Internet' " +
							"		END AS CH_LUGAR, " +
							"	DATE(CH_FEC_CREA) AS CH_FECHA, 'I' AS CH_TIPO " +
							"	FROM FO_CH_COMPRA_HISTORICAS " +
							"	WHERE CH_CLI_ID = ? " +
							"	ORDER BY CH_FECHA DESC ";
			conexion = JdbcDAOFactory.getConexion();

			stm = conexion.prepareStatement(cadQuery  + " WITH UR");			
			stm.setLong(1,cliente_id);
			//stm.setLong(2,cliente_id);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {

				logger.logData( "getUltimasCompras", rs);

				compras = null;
				compras = new UltimasComprasDTO();
				compras.setId(rs.getLong("ch_id"));
				compras.setNombre( rs.getString("ch_alias") );
				compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
				compras.setLugar_compra(rs.getString("ch_lugar"));
				compras.setUnidades(rs.getDouble("ch_unidades"));
				compras.setTipo(rs.getString("ch_tipo"));
				lista.add(compras);
			}
		} catch (SQLException ex) {
			logger.error("getTiposCalle - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getTiposCalle - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getTiposCalle - Problema SQL (close)", e);
			}
		}

		return lista;		

		
	}

	public List getUltimasComprasCatProFO(String listas, String local, long cliente_id, long rut ) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/fo_app.properties");
		Properties prop = new Properties();
		try {
			prop.load(log4jProps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List l_cat = new ArrayList();
		
		try {
			String lista_i = "";
			String lista_l = "";
            String lista_p = "";
			if ( listas != null && listas.compareTo("") != 0 ) {
				// Se filtran las diferentes listas
				String []laux = listas.split(",");
				String comai = "";
				String comal = "";
                String comap = "";
				for( int i = 0; i < laux.length; i++ ) {
					String []lista_aux = laux[i].split("-");
					if( lista_aux[1].compareTo("I") == 0 ) {
						lista_i += comai+""+lista_aux[0];
						comai=",";
					} else if( lista_aux[1].compareTo("L") == 0 ) {
						lista_l += comal+""+lista_aux[0];
						comal=",";
					} else if( lista_aux[1].compareTo("P") == 0 ) {
                        lista_p += comap+""+lista_aux[0];
                        comap=",";
                    }
				}
			}

            // lista_i: Listas productos internet   
            // lista_l: Lista de productos en local fisico
            // lista_p: Lista predefinida de productos
            if( lista_i.compareTo("") == 0 )
                lista_i = "-1";
            if( lista_l.compareTo("") == 0 )
                lista_l = "-1";
            if( lista_p.compareTo("") == 0 )
                lista_p = "-1";
			
			conexion = JdbcDAOFactory.getConexion();
			
			String query = 	"SELECT DISTINCT cat.cat_id, cat.cat_nombre, cat.cat_orden, " +
                            "pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, pro.pro_unidad_medida, pro.pro_inter_valor, " +
                            "pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, pro.pro_estado, " +
                            "pro.pro_tipo_sel, mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, " +
                            "pre_valor, pre_stock, pre_tienestock, car.car_id, car.car_cantidad, car.car_nota, pro.pro_particionable, pro.pro_particion, " +
                            "MAX(chp.chp_cantidad) as cantidad " +
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "FROM fo_ch_productos chp " +
                            "  JOIN fo_productos pro on chp.chp_pro_id = pro.pro_id " +
                            "  join (" +
                            "    select chp1.chp_pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                            "    FROM fo_ch_compra_historicas ch1 " +
                            "    join fo_ch_productos chp1 on ch1.ch_id = chp1.CHP_CH_ID" +
                            "    JOIN fo_productos_categorias prca1 on chp1.chp_pro_id = prca1.prca_pro_id " +
                            "    JOIN fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                            "    JOIN fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                            "    JOIN fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                            "    JOIN fo_precios_locales loc1 on chp1.chp_pro_id = loc1.pre_pro_id AND loc1.pre_estado = 'A' AND loc1.pre_loc_id = " + local + " " +
                            "    WHERE chp1.chp_ch_id in (" + lista_i + ") and ch1.CH_CLI_ID = " + cliente_id + " " +
                            "    group by chp1.chp_pro_id " +
                            "    ) as x on x.pro_id = pro.pro_id " +
                            "  JOIN fo_categorias cat on cat.CAT_ID = x.cat_id " +
                            "  LEFT JOIN fo_precios_locales loc on pro.pro_id = loc.pre_pro_id AND loc.pre_estado = 'A' AND loc.pre_loc_id = " + local + " " +
                            "  LEFT JOIN fo_marcas mar on mar.mar_id = pro.pro_mar_id " +
                            "  LEFT JOIN fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id AND uni.uni_estado = 'A' " +
                            "  LEFT JOIN fo_carro_compras car on car.car_pro_id = pro.pro_id AND car.car_cli_id = " + cliente_id + " " + 
                            "WHERE chp.chp_ch_id in (" + lista_i + ") " +
                            "GROUP BY cat.cat_id, cat.cat_nombre, cat.cat_orden, pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, pro.pro_unidad_medida, " +
                            "pro.pro_inter_valor, pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, pro.pro_estado, pro.pro_tipo_sel, " +
                            "mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, loc.pre_valor, loc.pre_stock, loc.pre_tienestock, car.car_id, car.car_cantidad, " +
                            "car.car_nota, pro.pro_particionable, pro.pro_particion " +                            
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "UNION " +
                            "SELECT DISTINCT cat.cat_id, cat.cat_nombre, cat.cat_orden, " +
                            "pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, pro.pro_unidad_medida, pro.pro_inter_valor, " +
                            "pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, pro.pro_estado, " +
                            "pro.pro_tipo_sel, mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, " +
                            "pre_valor, pre_stock, pre_tienestock, car.car_id, car.car_cantidad, car.car_nota, pro.pro_particionable, pro.pro_particion, " +
                            "MAX(chp.chp_cantidad) as cantidad " +
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "FROM fo_ch_productos chp " +
                            "  JOIN fo_productos pro on chp.chp_pro_id = pro.pro_id " +
                            "  join (" +
                            "    select chp1.chp_pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                            "    FROM fo_ch_compra_historicas ch1 " +
                            "    join fo_ch_productos chp1 on ch1.ch_id = chp1.CHP_CH_ID" +
                            "    JOIN fo_productos_categorias prca1 on chp1.chp_pro_id = prca1.prca_pro_id " +
                            "    JOIN fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                            "    JOIN fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                            "    JOIN fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                            "    JOIN fo_precios_locales loc1 on chp1.chp_pro_id = loc1.pre_pro_id AND loc1.pre_estado = 'A' AND loc1.pre_loc_id = " + local + " " +
                            "    WHERE chp1.chp_ch_id in (" + lista_p + ") " +
                            "    group by chp1.chp_pro_id " +
                            "    ) as x on x.pro_id = pro.pro_id " +
                            "  JOIN fo_categorias cat on cat.CAT_ID = x.cat_id " +
                            "  LEFT JOIN fo_precios_locales loc on pro.pro_id = loc.pre_pro_id AND loc.pre_estado = 'A' AND loc.pre_loc_id = " + local + " " +
                            "  LEFT JOIN fo_marcas mar on mar.mar_id = pro.pro_mar_id " +
                            "  LEFT JOIN fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id AND uni.uni_estado = 'A' " +
                            "  LEFT JOIN fo_carro_compras car on car.car_pro_id = pro.pro_id AND car.car_cli_id = " + cliente_id + " " + 
                            "WHERE chp.chp_ch_id in (" + lista_p + ") " +
                            "GROUP BY cat.cat_id, cat.cat_nombre, cat.cat_orden, pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, pro.pro_unidad_medida, " +
                            "pro.pro_inter_valor, pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, pro.pro_estado, pro.pro_tipo_sel, " +
                            "mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, loc.pre_valor, loc.pre_stock, loc.pre_tienestock, car.car_id, car.car_cantidad, " +
                            "car.car_nota, pro.pro_particionable, pro.pro_particion " +                            
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "UNION " +
                            "SELECT DISTINCT cat.cat_id, cat.cat_nombre, cat.cat_orden, pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, " +
                            "pro.pro_unidad_medida, pro.pro_inter_valor, pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, " +
                            "pro.pro_estado, pro.pro_tipo_sel, mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, loc.pre_valor, loc.pre_stock, loc.pre_tienestock, " +
                            "car.car_id, car.car_cantidad, car.car_nota, pro.pro_particionable, pro.pro_particion, pro.pro_inter_valor as cantidad " +
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "FROM fo_productos pro " +
                            "join ( " +
                            "     select pro1.pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                            "     FROM fo_productos pro1 " +
                            "     JOIN fo_productos_categorias prca1 on pro1.pro_id = prca1.prca_pro_id " +
                            "     JOIN fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                            "     JOIN fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                            "     JOIN fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                            "     JOIN fo_precios_locales loc1 on pro1.pro_id = loc1.pre_pro_id AND loc1.pre_estado = 'A' AND loc1.pre_loc_id = " + local + " " +
                            "     JOIN fo_cod_barra cb1 on cb1.bar_pro_id = pro1.pro_id AND cb1.bar_estado = 'A' " +
                            "     JOIN fo_productos_compra pc1 on substr( cb1.bar_codigo, 1, length(cb1.bar_codigo)-1 ) = pc1.fpc_ean " +
                            "     join fo_compras_locales cl1 on pc1.FCL_ID = cl1.FCL_ID and cl1.FCL_RUT = " + rut + " " +
                            "     WHERE pro1.pro_estado = 'A' AND pc1.fcl_id in (" + lista_l + ") " +
                            "     group by pro1.pro_id " +
                            "     ) as x on x.pro_id = pro.pro_id " +
                            "JOIN fo_categorias cat on x.cat_id = cat.CAT_ID " +
                            "JOIN fo_precios_locales loc on pro.pro_id = loc.pre_pro_id AND loc.pre_estado = 'A' AND loc.pre_loc_id = " + local + " " +
                            "JOIN fo_marcas mar on mar.mar_id = pro.pro_mar_id " +
                            "JOIN fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id AND uni.uni_estado = 'A' " +
                            "JOIN fo_cod_barra cb on cb.bar_pro_id = pro.pro_id AND cb.bar_estado = 'A' " +
                            "JOIN fo_productos_compra pc on substr( cb.bar_codigo, 1, length(cb.bar_codigo)-1 ) = pc.fpc_ean " +
                            "LEFT JOIN fo_carro_compras car on car.car_pro_id = pro.pro_id and car.car_cli_id = " + cliente_id + " " +
                            "WHERE pro.pro_estado = 'A' AND pc.fcl_id in (" + lista_l + ") " +
                            "GROUP BY cat.cat_id, cat.cat_nombre, cat.cat_orden, pro.pro_id, pro.pro_des_corta, pro.pro_cod_sap, pro.pro_unidad_medida, " +
                            "pro.pro_inter_valor, pro.pro_inter_max, pro.pro_nota, pro.pro_tipre, pro.pro_tipo_producto, pro.pro_estado, pro.pro_tipo_sel, " +
                            "mar.mar_id, mar.mar_nombre, uni.uni_desc, uni.uni_cantidad, loc.pre_valor, loc.pre_stock, loc.pre_tienestock, car.car_id, car.car_cantidad, " +
                            "car.car_nota, pro.pro_particionable, pro.pro_particion " +
//+coh20120713
",pro.pro_imagen_minificha " +                            
//-coh20120713
                            "order by cat_nombre, pro_tipo_producto, pro_des_corta, pre_valor ";
			
			stm = conexion.prepareStatement(query  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				ArrayList campos = new ArrayList();
				campos.add("pro_id");
				if( this.revisionCamposFO( rs, campos ) == false ) {
					continue;
				}
				
				// Revisar si existe producto en alguna categoría anterior
				boolean flag = true;
				for( int i = 0; flag == true && i < l_cat.size(); i++ ) {
					UltimasComprasCategoriasDTO aux_cat = (UltimasComprasCategoriasDTO)l_cat.get(i);
					List aux_lpro = aux_cat.getUltimasComprasProductosDTO();
					for( int j = 0; flag == true && j < aux_lpro.size(); j++ ) {
						UltimasComprasProductosDTO aux_pro = (UltimasComprasProductosDTO)aux_lpro.get(j);
						if( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
							flag = false;
						}
					}
				}
				if( flag == false ) {
					continue; // Se salta el registro
				}
				
				// Listado de productos por categorias
				List l_pro = new ArrayList();
				UltimasComprasCategoriasDTO categoria = null;
								
				// Revisar si existe la categoría
				boolean flag_cat = true;
                for( int i = 0; i < l_cat.size(); i++ ) {
                    categoria = (UltimasComprasCategoriasDTO)l_cat.get(i);
                    if( categoria.getId() == rs.getLong("cat_id") ) {
                        l_pro = categoria.getUltimasComprasProductosDTO();
                        logger.debug("Obtener lista de producto de categoría "+categoria.getCategoria()+" --->" + l_pro.size() );                       
                        flag_cat = false;
                        break;
                    }
                }               

                // Agregar categorias sólo si es una categoría nueva
                if( flag_cat ) {
                    categoria = new UltimasComprasCategoriasDTO();
                    // Si el producto no tiene categoría se pone en categoría Sin Clasificar
                    if( rs.getString("cat_id") == null ) {
                        categoria.setId(0);
                        categoria.setCategoria( prop.getProperty("jdbcclientesdao.nombrecategoria") );
                    }
                    else {
                        categoria.setId( rs.getLong("cat_id") );
                        categoria.setCategoria( rs.getString("cat_nombre") );
                    }
                }
					
				// Agregar los productos
				UltimasComprasProductosDTO productos = new UltimasComprasProductosDTO();
				productos.setPro_id(rs.getLong("pro_id"));
//+20120525coh
				productos.setTipo(rs.getString("pro_tipo_producto"));
				productos.setImg_chica(rs.getString("pro_imagen_minificha"));
//-20120525coh
				productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
				productos.setCodigo(rs.getString("pro_cod_sap"));
				if( rs.getString("uni_cantidad") != null && rs.getString("pre_valor") != null && rs.getString("pro_unidad_medida") != null )
					productos.setPpum(rs.getDouble("pre_valor")/(rs.getDouble("uni_cantidad")*rs.getDouble("pro_unidad_medida")));
				productos.setPrecio(rs.getDouble("pre_valor"));
				productos.setMarca(rs.getString("mar_nombre"));
				productos.setUnidad_nombre(rs.getString("uni_desc"));
				productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
				if( rs.getString("pro_tipre") != null )
					productos.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
				productos.setInter_maximo( rs.getDouble("pro_inter_max") );
				if( rs.getString("pro_inter_valor") != null )
					productos.setInter_valor(rs.getDouble("pro_inter_valor"));
				else
					productos.setInter_valor(1.0);

				// Se debe aproximar al más cercano
				productos.setCantidad(rs.getDouble("cantidad"));
				
				if( rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S' ) {
					productos.setCon_nota(true);
					productos.setNota( rs.getString("car_nota") );
				}
				else
					productos.setCon_nota(false);
				productos.setCar_id(rs.getLong("car_id"));				
				if( rs.getString("car_id") == null )
					productos.setEn_carro( false );
				else
					productos.setEn_carro( true );				
				
				if( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 )
					productos.setStock(rs.getLong("pre_stock"));
				else
					productos.setStock(100); // No toma encuenta el stock nunca pone 0
				
				//particionable
				if ( rs.getString("pro_particionable") != null ) {
				    if ( rs.getString("pro_particionable").toString().equalsIgnoreCase("S") ) {
				        productos.setEsParticionable("S");
				        if ( rs.getString("pro_particion") != null ) {
				            productos.setParticion(Long.parseLong( rs.getString("pro_particion") ));
				        } else {
				            productos.setParticion(1);    
				        }
				    } else {
				        productos.setEsParticionable("N");
				        productos.setParticion(1);
				    }				    
				}
				
				
				// Si no tiene información necesaria => stock=0 
				if( rs.getString("pro_estado").compareTo("N") == 0 
						|| rs.getString("cat_id") == null 
						|| rs.getString("pre_valor") == null
						|| rs.getString("pro_estado").compareTo("A") != 0 )
					productos.setStock(0);
				
                if (rs.getInt("pre_tienestock") == 0)
                    productos.setTieneStock(false);
                else
                    productos.setTieneStock(true);
				l_pro.add(productos);
				
				categoria.setUltimasComprasProductosDTO(l_pro);

				if ( flag_cat ) {
					l_cat.add(categoria);
				}
				
			}
			
		} catch (SQLException ex) {
			logger.error("getUltimasComprasCatPro - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getUltimasComprasCatPro - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getUltimasComprasCatPro - Problema SQL (close)", e);
			}
		}

		return l_cat;
	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#insertCarroCompra(java.util.List, long)
	 */
	public void insertCarroCompraFO(List listcarro, long cliente, String idSession) throws ClientesDAOException {

		Connection conexion = null;
		ResultSet rs = null;
		CarroCompraDTO carro = null;
		PreparedStatement stm1 = null;
		PreparedStatement stm = null;
		PreparedStatement stm2 = null;

		try {

			conexion = JdbcDAOFactory.getConexion();
			
			for (int i = 0; i < listcarro.size(); i++) {
				carro = (CarroCompraDTO) listcarro.get(i);				
				
				// Si existe el producto sólo se actualiza la cantidad
				String query = "select CAR_ID, CAR_CANTIDAD " +
                               "from FODBA.fo_carro_compras " +
                               "where car_pro_id = ? and car_cli_id = ? ";
                if ( cliente == 1 ) {
                    query += "and CAR_INVITADO_ID = " + idSession;
                }
				stm1 = conexion.prepareStatement(query + " WITH UR");
				stm1.setString(1, carro.getPro_id());
				stm1.setLong(2, cliente);
				rs = stm1.executeQuery();
				if (rs.next()) { // Existe producto

					if (carro.getCantidad() > 0 && carro.getCantidad() != rs.getLong("CAR_CANTIDAD")) { 

						try {
	                        String sqlUpd = "UPDATE FODBA.fo_carro_compras " +
	                                        "set car_cantidad = ?, " +
	                                        "car_nota = ?, " +
	                                        "car_fec_crea = ?, " + 
	                                        "car_tipo_sel = ? " + 
	                                        "where car_cli_id = ? " +
	                                        "and car_id = ? ";
	                        if ( cliente == 1 ) {
	                            sqlUpd += "and CAR_INVITADO_ID = " + idSession;
	                        }
							stm = conexion.prepareStatement( sqlUpd );
							stm.setDouble(1, carro.getCantidad() );
							if( carro.getNota() != null )
								stm.setString(2, carro.getNota() );
							else
								stm.setString(2, "" );
							Calendar cal = new GregorianCalendar();
							stm.setTimestamp( 3, new Timestamp( cal.getTimeInMillis() ) );						
							
							
							if( carro.getLugar_compra() == null || Utils.isEmpty(carro.getLugar_compra()) ){
								stm.setString(4, "W");
							}else{
								stm.setString(4, carro.getLugar_compra());
							}
							
							stm.setLong(5, cliente);
							stm.setLong(6, rs.getLong("CAR_ID"));
				
							logger.debug("SQL: " + stm.toString());
							
							logger.debug("nota dn jdbc: " + carro.getNota());
							if (stm.executeUpdate() == 0) {
								logger.debug("insertCarroCompra - No modifica pero sin excepciones");
							}
							
						} catch (SQLException ex) {
							logger.error("insertCarroCompra - SQL update producto", ex);
							throw new ClientesDAOException("insertCarroCompra - SQL update producto", ex);
						} catch (Exception ex) {
							logger.error("insertCarroCompra - SQL update producto", ex);
							throw new ClientesDAOException("insertCarroCompra - SQL update producto");
						}					
					}else if (carro.getCantidad() == 0) {//Se elimina producto						
						this.deleteCarroCompraFO(cliente, rs.getLong("car_id"), idSession);						            
					}	
				}else { // No existe producto
				
					try {						
						if (carro.getCantidad() > 0) { 
	                        String sqlIns = "";                        
	                        if ( cliente == 1 ) {
	                            sqlIns = "INSERT INTO FODBA.fo_carro_compras " +
	                                     "(car_pro_id, car_cli_id, car_cantidad, car_nota, car_fec_crea, car_tipo_sel, CAR_INVITADO_ID ) " +
	                                     "VALUES( ?, ?, ?, ?, ?, ?," + idSession + ")";
	                        } else {
	                            sqlIns = "INSERT INTO FODBA.fo_carro_compras " +
	                                     "(car_pro_id, car_cli_id, car_cantidad, car_nota, car_fec_crea, car_tipo_sel ) " +
	                                     "VALUES( ?, ?, ?, ?, ?, ?)";    
	                        }
	                        
							stm2 = conexion.prepareStatement(sqlIns);		
							int col = 1;
							stm2.setString(col++, carro.getPro_id());
							stm2.setLong(col++, cliente);
							stm2.setDouble(col++, carro.getCantidad());
							if( carro.getNota() != null )
								stm2.setString(col++, carro.getNota());
							else
								stm2.setString(col++, "" );
							Calendar cal = new GregorianCalendar();
							stm2.setTimestamp(col++, new Timestamp(cal.getTimeInMillis()));
							
							if( carro.getLugar_compra() == null || Utils.isEmpty(carro.getLugar_compra()) ){
								stm2.setString(col++, "W");
							}else{
								stm2.setString(col++, carro.getLugar_compra());
							}
							
							logger.debug("SQL: " + stm2.toString());
							logger.debug("nota dn jdbc: " + carro.getNota());
							if (stm2.executeUpdate() == 0) {
								logger.debug("insertCarroCompra - Insert carro sin excepciones");
								throw new ClientesDAOException("insertCarroCompra - Insert carro sin excepciones");
							}
						}
					} catch (SQLException ex) {
						logger.error("insertCarroCompra - SQL insert producto", ex);
						throw new ClientesDAOException("insertCarroCompra - SQL insert producto", ex);
					} catch (Exception ex) {
						logger.error("insertCarroCompra - SQL insert producto", ex);
						throw new ClientesDAOException("insertCarroCompra - SQL insert producto");
					}						
				}
			}
			
		} catch (SQLException ex) {
			logger.error("insertCarroCompra - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("insertCarroCompra - Problema General", ex);
			throw new ClientesDAOException(ex);
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
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("insertCarroCompra - Problema SQL (close)", e);
			}
		}
	}

	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#guardaListaCompra(String, long)
	 */
	public int guardaListaCompraFO(String nombre, long cliente) throws ClientesDAOException {
		Connection conexion = null;
		ResultSet rs = null;
		ResultSet key = null;
		PreparedStatement stm1 = null;
		PreparedStatement stm2 = null;
		PreparedStatement stm3 = null;
		int respuesta = 0;
		int cantidad_carro = 0;
		try {

			conexion = JdbcDAOFactory.getConexion();
			//comprobamos si existe el nombre de la lista
			String query = "select ch_alias from fo_ch_compra_historicas where ch_cli_id = ? and upper(ch_alias) = ?";
			stm1 = conexion.prepareStatement(query + " WITH UR ");
			stm1.setLong(1, cliente);
			stm1.setString(2, nombre.toUpperCase());
			rs = stm1.executeQuery();
			if (!rs.next()) { // no existe la lista 
				query = "select count(*) as cantidad from fo_carro_compras where car_cli_id = ?";
				stm1 = conexion.prepareStatement(query + " WITH UR ");
				stm1.setLong(1, cliente);
				rs = stm1.executeQuery();
				if (rs.next())
					cantidad_carro = rs.getInt("cantidad");
				
				//inserta la lista de compra
				conexion.setAutoCommit(false);
				stm2 = conexion.prepareStatement("insert into fo_ch_compra_historicas (ch_alias, ch_fec_crea, ch_tipo, ch_cli_id, ch_unidades) " + "values( ?, current_timestamp, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
				stm2.setString(1, nombre);
				stm2.setString(2, "M"); //M: Mi lista
				stm2.setLong(3, cliente);
				stm2.setInt(4, cantidad_carro);
				stm2.executeUpdate();
				key = stm2.getGeneratedKeys();
	            if (key.next()) {
	            	//inserta el detalle de la lista
	            	int id = key.getInt(1);
	            	stm3 = conexion.prepareStatement("insert into fo_ch_productos (chp_ch_id, chp_cantidad, chp_pro_id) select " + id + ",car_cantidad,car_pro_id from fo_carro_compras where car_cli_id = ?");
					stm3.setLong(1, cliente);
					stm3.executeUpdate();
					
	            	respuesta = 0;
	            } else {
	            	respuesta = 2;
	            }
	            
	            conexion.commit();
				
			} else { //ya existe la lista
				respuesta = 1;
			}
		} catch (SQLException ex) {
			logger.error("guardaListaCompra - Problema SQL", ex);
			ex.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e) {
				// Bloque catch generado automáticamente
				respuesta = 2;
				e.printStackTrace();
			}
			respuesta = 2; 
		} catch (Exception ex) {
			logger.error("guardaListaCompra - Problema General", ex);
			respuesta = 2;
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (key != null)
					key.close();
				if (stm1 != null)
					stm1.close();
				if (stm2 != null)
					stm2.close();
				if (stm3 != null)
					stm3.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("guardaListaCompra - Problema SQL (close)", e);
				respuesta = 2;
			}
		}
		return respuesta;
	}

    /* (sin Javadoc)
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#crearSesionInvitado(String)
     */
    public int crearSesionInvitadoFO(String idSesion) throws ClientesDAOException {
        Connection conexion = null;
        ResultSet rs = null;
        ResultSet key = null;
        PreparedStatement stm1 = null;
        int respuesta = 0;
        try {
            conexion = JdbcDAOFactory.getConexion();
            //inserta la sesion
            conexion.setAutoCommit(false);
            stm1 = conexion.prepareStatement("insert into FODBA.fo_invitados (inv_session_id, inv_fec_crea) " + "values( ?, current_timestamp)",Statement.RETURN_GENERATED_KEYS);
            stm1.setString(1, idSesion);
            stm1.executeUpdate();
            key = stm1.getGeneratedKeys();
            if (key.next()) {
                respuesta = key.getInt(1);
                conexion.commit();
            }
        } catch (SQLException ex) {
            logger.error("crearSesionInvitado - Problema SQL", ex);
            ex.printStackTrace();
            try {
                conexion.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("crearSesionInvitado - Problema General", ex);
            ex.printStackTrace();
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (key != null)
                    key.close();
                if (stm1 != null)
                    stm1.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("crearSesionInvitado - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }    
    
    
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#eliminaListaCompra(int)
	 */
	public int eliminaListaCompraFO(int id_lista) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm1 = null;
		int respuesta = 0;
		try {
			conexion = JdbcDAOFactory.getConexion();
			//comprobamos si existe el nombre de la lista
			String query = "delete from fo_ch_compra_historicas where ch_id = ?";
			stm1 = conexion.prepareStatement(query);
			stm1.setInt(1, id_lista);
			stm1.executeUpdate();
	        respuesta = 0;
		} catch (SQLException ex) {
			logger.error("eliminaListaCompra - Problema SQL", ex);
			ex.printStackTrace();
			respuesta = 1; 
		} catch (Exception ex) {
			logger.error("eliminaListaCompra - Problema General", ex);
			respuesta = 1;
			ex.printStackTrace();
		} finally {
			try {
				if (stm1 != null)
					stm1.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("eliminaListaCompra - Problema SQL (close)", e);
				respuesta = 1;
			}
		}
		return respuesta;
	}

	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getPoliticaSustitucion()
	 */
	public List getPoliticaSustitucionFO() throws ClientesDAOException {

		List lista = new ArrayList();
		PoliticaSustitucionDTO sustitucion = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String cadQuery="	SELECT SUS_ID, SUS_NOMBRE, SUS_DESCRIPCION, SUS_ESTADO, SUS_SELECCION " +
							"	FROM FO_POLITICAS_SUSTITUCION " +
							"	WHERE SUS_ESTADO = 'A' ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(cadQuery  + " WITH UR");
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {

				logger.logData( "getPoliticaSustitucion", rs);

				sustitucion = null;
				sustitucion = new PoliticaSustitucionDTO();
				sustitucion.setId(rs.getLong("sus_id"));
				sustitucion.setNombre(rs.getString("sus_nombre"));
				sustitucion.setDescripcion(rs.getString("sus_descripcion"));
				sustitucion.setEstado(rs.getString("sus_estado"));
				sustitucion.setSeleccion(rs.getString("sus_seleccion"));
				lista.add(sustitucion);
			}
		} catch (SQLException ex) {
			logger.error("getPoliticaSustitucion - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPoliticaSustitucion - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getPoliticaSustitucion - Problema SQL (close)", e);
			}
		}

		return lista;

	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getFormaPago()
	 */
	public List getFormaPagoFO() throws ClientesDAOException {

		List lista = new ArrayList();
		FormaPagoDTO formapago = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String cadQuery="	SELECT FOR_ID, FOR_NOMBRE, FOR_ESTADO " +
							"	FROM FO_FORMAS_PAGO " +
							"	WHERE FOR_ESTADO = 'A' ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(cadQuery  + " WITH UR");
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {

				logger.logData( "getFormaPago", rs);

				formapago = null;
				formapago = new FormaPagoDTO();
				formapago.setId(rs.getLong("for_id"));
				formapago.setNombre(rs.getString("for_nombre"));
				formapago.setEstado(rs.getString("for_estado"));
				lista.add(formapago);
			}
		} catch (SQLException ex) {
			logger.error("getFormaPago - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getFormaPago - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getFormaPago - Problema SQL (close)", e);
			}
		}
		return lista;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#deleteCarroCompraAll(long)
	 */
	public void deleteCarroCompraAllFO( long cliente_id, String ses_invitado_id ) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
        String SQL = "";

		try {
            SQL = "DELETE FROM FO_CARRO_COMPRAS "
                + "WHERE CAR_CLI_ID = ? ";
            if (cliente_id == 1){
                SQL += "  AND CAR_INVITADO_ID = ?";
            }
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(SQL);
			stm.setLong(1, cliente_id);
            if (cliente_id == 1){
                stm.setInt(2, Integer.parseInt(ses_invitado_id));
            }

			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.debug("deleteCarroCompraAll - No elimina pero sin excepciones");
			}
		} catch (SQLException ex) {
			logger.error("deleteCarroCompraAll - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("deleteCarroCompraAll - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("deleteCarroCompraAll - Problema SQL (close)", e);
			}
		}
	}

    /* 
     * @see cl.bbr.fo.clientes.dao.ClientesDAO#limpiarMiCarro(long, String)
     */
    public void limpiarMiCarroFO(long cliente_id, String id_session) throws ClientesDAOException {

        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String query = "delete from fo_carro_compras " + 
                           "where car_cli_id = ? ";
            if (cliente_id == 1)
                query += " and car_invitado_id = " + id_session;
            conexion = JdbcDAOFactory.getConexion();
            
            stm = conexion.prepareStatement(query);
            stm.setLong(1, cliente_id);

            logger.debug("SQL: " + stm.toString());
            if (stm.executeUpdate() == 0) {
                logger.debug("limpiarMiCarro - No elimina pero sin excepciones");
            }

        } catch (SQLException ex) {
            logger.error("limpiarMiCarro - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("limpiarMiCarro - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("limpiarMiCarro - Problema SQL (close)", e);
            }
        }
    }
    
    
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getLocalComuna(long)
	 */
	public String getLocalPoligonoFO( long comuna ) throws ClientesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		String local = null;
		
		try {
			conexion = JdbcDAOFactory.getConexion();
			String SQL = "SELECT DISTINCT P.ID_POLIGONO, Z.ID_LOCAL "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                       + "WHERE P.ID_COMUNA = ? "
                       + "  AND P.NUM_POLIGONO = 0";
			
			stm = conexion.prepareStatement(SQL + " WITH UR ");
                    
			stm.setLong(1, comuna);
			logger.debug("SQL (getLocalComuna): " + SQL);
			logger.debug("id_comuna: " + comuna);
			rs = stm.executeQuery();
			if ( rs.next() ) {
				logger.logData( "getLocalComuna", rs);
				local = rs.getString("ID_LOCAL") + "-" + rs.getString("ID_POLIGONO");
			}
            
		} catch (SQLException ex) {
			logger.error("getLocalComuna - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getLocalComuna - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getLocalComuna - Problema SQL (close)", e);
			}
		}
		return local;
	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#addTraking(java.lang.String, java.lang.Long, javax.servlet.http.HttpServletRequest)
	 */
	public void addTrakingFO(String seccion, Long rut, HashMap arg0) throws ClientesDAOException {

	    /*20120911avc
		Connection conexion = null;
		PreparedStatement stm = null;
		
		try {
			
			// Se recupera la sesión del usuario
			HttpSession session = arg0.getSession();			

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("INSERT INTO fo_tracking_web (tra_seccion, tra_pagina, tra_uid, tra_tracktime, tra_ip, tra_rut) VALUES( ?,?,?,?,?,? )");
			stm.setString(1,seccion);
			stm.setString(2,arg0.getRequestURI().toString());
			stm.setString(3,session.getId());
			Calendar cal = new GregorianCalendar();
			stm.setTimestamp(4, new Timestamp(cal.getTimeInMillis()));
			stm.setString(5,arg0.getRemoteAddr().toString());
			stm.setLong(6,rut.longValue());
			logger.debug("SQL: " + stm.toString());
			if (stm.executeUpdate() == 0) {
				logger.debug("saveTrackingS - No modifica pero sin excepciones");
			}

		} catch (SQLException ex) {
			logger.error("saveTrackingS - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("saveTrackingS - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("saveTrackingS - Problema SQL (close)", e);
			}
		}
		20120911avc*/
		
	}

	/**
	 * Dump de los datos de la consulta hacia el log
	 * @param rs
	 *               ResultSet para almacenar la información en el log
	 * @param campos
	 *               Listado de campos para almacenar en el log
	 * @return
	 *               True: éxito, False: error
	 * 
	 * @throws SQLException
	 */
	protected boolean revisionCamposFO( ResultSet rs, List campos ) throws SQLException {

		
		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if( rs.getString(campo) == null) {
				logger.info("getUltimasComprasCatPro - Se salta registro campo nulo -> " + campo);
				return false;
			}
		}
		
		return true;

	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#updateIntentos(long, long)
	 */
	public void updateIntentosFO(long cliente_id, long accion) throws ClientesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;

		try {
			Calendar cal = new GregorianCalendar();
			Timestamp fec_login = new Timestamp( cal.getTimeInMillis() );
			logger.debug("fec_login: " + fec_login);
			
			// Se modifican los campos 
			conexion = JdbcDAOFactory.getConexion();
			if (accion == 1){
				stm = conexion.prepareStatement("UPDATE FO_CLIENTES SET CLI_FEC_LOGIN = ?, CLI_INTENTOS = CLI_INTENTOS + 1 WHERE CLI_ID = ? ");
				stm.setTimestamp(1, fec_login);
				stm.setLong(2, cliente_id);
				logger.debug("SQL: " + stm.toString());
				if (stm.executeUpdate() == 0) {
					logger.debug("updateIntentos - No modifica pero sin excepciones");
				}

			}else{//Se limpia el campo cli_intentos
				stm = conexion.prepareStatement("UPDATE FO_CLIENTES SET  CLI_INTENTOS = 0  WHERE CLI_ID = ?");
				stm.setLong(1, cliente_id);
				logger.debug("SQL: " + stm.toString());
				if (stm.executeUpdate() == 0) {
					logger.debug("updateIntentos - No modifica pero sin excepciones");
				}
			}			
			
		} catch (SQLException ex) {
			logger.error("updateIntentos - Problema SQL", ex);
			throw new ClientesDAOException(ex);
		} catch (Exception ex) {
			logger.error("updateIntentos - Problema General", ex);
			throw new ClientesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("updateIntentos - Problema SQL (close)", e);
			}
		}
		
	}
    

    public void updateDatosInvitadoFO(ClienteDTO cliente, String opcion) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            // Se modifican los campos 
            conexion = JdbcDAOFactory.getConexion();
            String clave = Utils.encriptarFO((cliente.getRut()+"").substring(0, 5));
            String query = "";
            if (opcion.equals("1")){
                query = "UPDATE FO_CLIENTES "
                      + "   SET CLI_EMAIL        = ?"
                      + "      ,CLI_FON_COD_2    = ?"
                      + "      ,CLI_FON_NUM_2    = ?"
                      + "      ,CLI_RUT          = ?"
                      + "      ,CLI_DV           = ?"
                      + "      ,CLI_NOMBRE       = ?"
                      + "      ,CLI_APELLIDO_PAT = ?"
                      + "      ,CLI_CLAVE        = ?"
                      + " WHERE CLI_ID = ? ";
                stm = conexion.prepareStatement(query);
                stm.setString(1, cliente.getEmail());
                stm.setString(2, cliente.getFon_cod_2());
                stm.setString(3, cliente.getFon_num_2());
                stm.setLong(  4, cliente.getRut());
                stm.setString(5, cliente.getDv());
                stm.setString(6, cliente.getNombre());
                stm.setString(7, cliente.getApellido_pat());
                stm.setString(8, clave);
                stm.setLong(  9, cliente.getId());
                
                logger.debug("SQL: " + stm.toString());
                if (stm.executeUpdate() == 0) {
                    logger.debug("updateDatosInvitado - No modifica pero sin excepciones");
                }
            }else{//Se limpia el campo cli_intentos
                query = "UPDATE fo_clientes "
                      + "   set cli_email     = ?"
                      + "      ,cli_fon_cod_2 = ?"
                      + "      ,cli_fon_num_2 = ? "
                      + " where cli_id = ? ";
                stm = conexion.prepareStatement(query);
                stm.setString(1, cliente.getEmail());
                stm.setString(2, cliente.getFon_cod_2());
                stm.setString(3, cliente.getFon_num_2());
                stm.setLong(4, cliente.getId());
                logger.debug("SQL: " + stm.toString());
                if (stm.executeUpdate() == 0) {
                    logger.debug("updateDatosInvitado - No modifica pero sin excepciones");
                }
            }
        } catch (SQLException ex) {
            logger.error("updateDatosInvitado - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("updateDatosInvitado - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("updateDatosInvitado - Problema SQL (close)", e);
            }
        }
    }

    /**
     * @param idCliente
     * @param local
     * @param regXpag
     * @param pagina
     * @return
     */
    public List getCarroCompraMobiFO(long idCliente, String local, int regXpag, int pagina) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        CarroCompraDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String query = null;
        
        if (pagina <= 0) { 
            pagina = 1;
        }
        if (regXpag < 5) {
            regXpag = 5;
        }
        int iniReg = (pagina - 1) * regXpag + 1;
        int finReg = pagina * regXpag;
        String order = "pro_tipo_producto";
        
        if( local.compareTo("-1") != 0 ) {
            // Cuando se quiere saber si hay productos solamente
            query = "car_id, car_cantidad, car_nota, car_tipo_sel, pro_nota, pro_imagen_minificha, pro_id, " +
                    "pro_des_corta, pro_inter_max, pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, " +
                    "pro_unidad_medida, pro_pesable, pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, " +
                    "uni_desc, mar_nombre, substr(id_catprod,1,2) as catsap, max(cod_barra) as barra " +
                    "FROM fo_carro_compras " +
                    "inner join fo_productos on pro_id = car_pro_id " +
                    "inner join fo_precios_locales on pre_pro_id = pro_id " +
                    "inner join fo_unidades_medida on uni_id = pro_uni_id " +
                    "inner join fo_marcas on mar_id = pro_mar_id " +  
                    "inner join bo_productos pro on pro.id_producto = pro_id_bo " +
                    "inner join bo_codbarra cod on cod.id_producto = pro.id_producto " +  
                    "where car_cli_id = ? and pre_loc_id = ? and pre_estado = 'A' " +
                    "group by car_id, car_cantidad, car_nota, car_tipo_sel, pro_nota, pro_imagen_minificha, pro_id, pro_des_corta, pro_inter_max, pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2) ";
        } else {
            query = "car_id, car_cantidad, car_nota, car_tipo_sel, pro_nota, pro_imagen_minificha, pro_id, " +
                    "pro_des_corta, pro_inter_max, pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, " +
                    "pro_unidad_medida, pro_pesable, pro_id_bo, pro_estado " +
                    "FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
                    "where car_cli_id = ? ";
        }
        
        String sql = " SELECT * FROM " +
                     "  ( " +
                     "  SELECT row_number() over( " +
                     "  ORDER BY " + order + " " +
                     "  ) as row, " + query + " " +
                     " ) AS TEMP WHERE row BETWEEN " + iniReg + " AND " + finReg;
        
        logger.debug("* SQL con criterio :"+sql + " WITH UR ");
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql + " WITH UR ");
            stm.setLong(1, idCliente);
            if( local.compareTo("-1") != 0 )
                stm.setLong(2, Long.parseLong(local));
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            
            while (rs.next()) {                
                dto = new CarroCompraDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                dto.setTipoSel(rs.getString("car_tipo_sel"));
                if( local.compareTo("-1") != 0 ) { // Si es para mostrar todos los productos
                    if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                        dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                    dto.setStock(rs.getLong("pre_stock"));
                    dto.setPrecio(rs.getDouble("pre_valor"));
                    dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                    dto.setUnidad_nombre( rs.getString("uni_desc") );
                    dto.setNom_marca(rs.getString("mar_nombre"));                   
                    dto.setCatsap(rs.getString("catsap"));
                    dto.setCodbarra(rs.getString("barra"));
                }
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                if( rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S' ) {
                    dto.setTieneNota(true);
                } else {
                    dto.setTieneNota(false);
                }
                dto.setImagen(rs.getString("pro_imagen_minificha"));
                
                if( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                        dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }
                
                if( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                
                lista.add(dto);
            }
        } catch (SQLException ex) {
            logger.error("getCarroCompraMobi - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroCompraMobi - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroCompraMobi - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param idCliente
     * @param local
     * @return
     */
    public double getCountCarroComprasFO(long idCliente, String local) throws ClientesDAOException {
        double productosEnCarro = 0;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
        
        if( local.compareTo("-1") != 0 ) {
            // Cuando se quiere saber si hay productos solamente
            query = "SELECT count(*) cantidad " +
                    "FROM fo_carro_compras " +
                    "inner join fo_productos on pro_id = car_pro_id " +
                    "inner join fo_precios_locales on pre_pro_id = pro_id " +
                    "where car_cli_id = ? and pre_loc_id = ? and pro_estado = 'A' and pre_estado = 'A' ";
        } else {
            query = "SELECT count(*) cantidad " +
                    "FROM fo_carro_compras " +
                    "inner join fo_productos on pro_id = car_pro_id " +
                    "where car_cli_id = ? " +
                    "and pro_estado = 'A' ";
        }
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " WITH UR ");
            stm.setLong(1, idCliente);
            if( local.compareTo("-1") != 0 )
                stm.setLong(2, Long.parseLong(local));
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            if (rs.next()) {                
                productosEnCarro = rs.getDouble("cantidad");
            }
        } catch (SQLException ex) {
            logger.error("getCarroCompra - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroCompra - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroCompra - Problema SQL (close)", e);
            }
        }
        return productosEnCarro;
    }

    /**
     * @param idCliente
     * @param idCarro
     * @param idProducto
     * @param nota
     */
    public void updateNotaCarroCompraFO(long idCliente, long idCarro, long idProducto, String nota) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE FO_CARRO_COMPRAS SET CAR_NOTA = ? WHERE CAR_CLI_ID = ? AND CAR_ID = ? AND CAR_PRO_ID = ? ";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql);
            stm.setString( 1, nota );
            stm.setLong(2, idCliente );
            stm.setLong(3, idCarro );
            stm.setLong(4, idProducto );
            
            logger.debug("SQL: " + stm.toString());
            if (stm.executeUpdate() == 0) {
                logger.debug("updateNotaCarroCompra - No modifica pero sin excepciones");
            }

        } catch (SQLException ex) {
            logger.error("updateNotaCarroCompra - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("updateNotaCarroCompra - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("updateNotaCarroCompra - Problema SQL (close)", e);
            }
        }

    }

    /**
     * @param idCliente
     * @return
     */
    public List getUltimasComprasInternetFO(long idCliente) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql ="	SELECT CH_ID, CH_ALIAS, CH_UNIDADES, " +
            			"	'COMPRA INTERNET' AS CH_LUGAR, " +
						"	DATE(CH_FEC_CREA) AS CH_FECHA, 'I' AS CH_TIPO " +
						"	FROM FO_CH_COMPRA_HISTORICAS " +
						"	WHERE CH_CLI_ID = ? AND CH_TIPO != 'M' AND CH_TIPO != 'P' " +
						"	ORDER BY CH_FECHA DESC ";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,idCliente);
            logger.debug("SQL getUltimasComprasInternet: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO compras = new UltimasComprasDTO();
                compras.setId(rs.getLong("ch_id"));
                compras.setNombre( rs.getString("ch_alias") );
                compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
                compras.setLugar_compra(rs.getString("ch_lugar"));
                compras.setUnidades(rs.getDouble("ch_unidades"));
                compras.setTipo(rs.getString("ch_tipo"));
                lista.add(compras);
            }
        } catch (SQLException ex) {
            logger.error("getUltimasComprasInternet - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getUltimasComprasInternet - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getUltimasComprasInternet - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteMisListasFO(long idCliente) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql ="	SELECT CH_ID, CH_ALIAS, CH_UNIDADES, " +
            			"	'MI LISTA' AS CH_LUGAR, " +
						"	DATE(CH_FEC_CREA) AS CH_FECHA, 'I' AS CH_TIPO " +
						"	FROM FO_CH_COMPRA_HISTORICAS " +
						"	WHERE CH_CLI_ID = ? AND CH_TIPO = 'M' " +
						"	ORDER BY CH_FECHA DESC ";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,idCliente);
            logger.debug("SQL clienteMisListas: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO compras = new UltimasComprasDTO();
                compras.setId(rs.getLong("ch_id"));
                compras.setNombre( rs.getString("ch_alias") );
                compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
                compras.setLugar_compra(rs.getString("ch_lugar"));
                compras.setUnidades(rs.getDouble("ch_unidades"));
                compras.setTipo(rs.getString("ch_tipo"));
                lista.add(compras);
            }
        } catch (SQLException ex) {
            logger.error("clienteMisListas - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteMisListas - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteMisListas - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @return
     */
    public List clienteMisListasPredefinidasFO() throws ClientesDAOException {
       List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String  sql = 	"SELECT CH_ID, CH_ALIAS, CH_UNIDADES, " +
            				"'PREDEFINIDA' AS CH_LUGAR, " +
							"DATE(CH_FEC_CREA) AS CH_FECHA, CH_TIPO " +
							"FROM FO_CH_COMPRA_HISTORICAS " +
							"WHERE CH_TIPO = 'P' " +
							"ORDER BY CH_FECHA DESC ";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql   + " WITH UR");          
            logger.debug("SQL clienteMisListasPredefinidas: " + sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO compras = new UltimasComprasDTO();
                compras.setId(rs.getLong("ch_id"));
                compras.setNombre( rs.getString("ch_alias") );
                compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
                compras.setLugar_compra(rs.getString("ch_lugar"));
                compras.setUnidades(rs.getDouble("ch_unidades"));
                compras.setTipo(rs.getString("ch_tipo"));
                lista.add(compras);
            }
        } catch (SQLException ex) {
            logger.error("clienteMisListasPredefinidas - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteMisListasPredefinidas - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteMisListasPredefinidas - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneSustitutosFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean respuesta = false;
        try {
            String sql = "select count(cli_id) sustitutos " +
                         "from FODBA.FO_SUSTITUTOS_CLIENTES " +
                         "where cli_id = ? ";    
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,idCliente);
            logger.debug("SQL clienteTieneSustitutos: " + sql);
            rs = stm.executeQuery();
            if ( rs.next() ) {
                if ( rs.getInt("sustitutos") > 0 ) {
                    respuesta = true;
                }
            }
        } catch (SQLException ex) {
            logger.error("clienteTieneSustitutos - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteTieneSustitutos - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteTieneSustitutos - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }

    /**
     * @param idCliente
     * @param sustitutosPorCategorias
     */
    public void updateSustitutosClienteFO(Long idCliente, List sustitutosPorCategorias) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE fo_sustitutos_clientes " +
                         "set id_criterio = ?, desc_criterio = ?, fecha_modificacion = ?, asigno_cliente = ? " +
                         "where pro_id = ? and cli_id = ?";
            
            conexion = JdbcDAOFactory.getConexion();
            
            for ( int i = 0; i < sustitutosPorCategorias.size(); i++ ) {
            	CriterioClienteSustitutoDTO cri = (CriterioClienteSustitutoDTO) sustitutosPorCategorias.get(i);
                stm = conexion.prepareStatement(sql);
                stm.setLong( 1, cri.getIdCriterio() );
                if ( cri.getIdCriterio() == 4) {
                    stm.setString(2, cri.getSustitutoCliente() );
                } else {
                    stm.setNull(2, java.sql.Types.VARCHAR );
                }
                stm.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
                stm.setString(4, "S" );
                stm.setLong(5, cri.getIdProducto() );
                stm.setLong(6, idCliente.longValue() );
                stm.executeUpdate();
            }

        } catch (SQLException ex) {
            logger.error("updateSustitutosCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("updateSustitutosCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("updateSustitutosCliente - Problema SQL (close)", e);
            }
        }        
    }

    /**
     * @param idCliente
     * @param criteriosProductos
     * @param idSession
     */
    public void guardaCriteriosMiCarroFO(Long idCliente, List criteriosProductos, String idSession) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String select = "Select count(*) as cantidad from FODBA.fo_sustitutos_clientes " +
                            "where pro_id = ? and cli_id = ? ";
            if (idCliente.longValue() == 1)
                select += " and sc_invitado_id = " + idSession;
            String update = "UPDATE FODBA.fo_sustitutos_clientes " +
                         "set id_criterio = ?, desc_criterio = ?, fecha_modificacion = ?, asigno_cliente = ? " +
                         "where pro_id = ? and cli_id = ? ";
            if (idCliente.longValue() == 1)
                update += "and sc_invitado_id = " + idSession;
            String insert = "insert into FODBA.fo_sustitutos_clientes values (?,?,?,?,?,?,?)";
            
            conexion = JdbcDAOFactory.getConexion();
            
            for ( int i = 0; i < criteriosProductos.size(); i++ ) {
            	CriterioClienteSustitutoDTO cri = (CriterioClienteSustitutoDTO) criteriosProductos.get(i);
                
                stm = conexion.prepareStatement(select + " with ur");
                
                stm.clearParameters();
                stm.setLong( 1, cri.getIdProducto());
                stm.setLong( 2, idCliente.longValue());
                rs = stm.executeQuery();
                if (rs.next()){
                    if (rs.getInt("cantidad") == 0) {
                        stm = conexion.prepareStatement(insert);
                        stm.clearParameters();
                        stm.setLong( 1, cri.getIdProducto());
                        stm.setLong( 2, idCliente.longValue() );
                        stm.setLong( 3, cri.getIdCriterio() );
                        if ( cri.getIdCriterio() == 4) {
                            stm.setString(4, cri.getSustitutoCliente() );
                        } else {
                            stm.setNull(4, java.sql.Types.VARCHAR );
                        }
                        stm.setTimestamp(5, new Timestamp(new GregorianCalendar().getTimeInMillis()));
                        stm.setString(6, "S" );
                        if (idCliente.longValue() == 1)
                            stm.setLong(7, Long.parseLong(idSession) );
                        else
                            stm.setLong(7, 0);
                        stm.executeUpdate();
                    } else {
                        stm = conexion.prepareStatement(update);
                        stm.clearParameters();
                        stm.setLong( 1, cri.getIdCriterio());
                        if ( cri.getIdCriterio() == 4) {
                            stm.setString(2, cri.getSustitutoCliente() );
                        } else {
                            stm.setNull(2, java.sql.Types.VARCHAR );
                        }
                        stm.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
                        stm.setString(4, "S" );
                        stm.setLong(5, cri.getIdProducto() );
                        stm.setLong(6, idCliente.longValue() );
                        stm.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("guardaCriteriosMiCarro - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("guardaCriteriosMiCarro - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (rs != null)
                    rs.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("guardaCriteriosMiCarro - Problema SQL (close)", e);
            }
        }        
    }


    /**
     * @param idCliente
     * @param prodsNuevos
     */
    public void addSustitutosClienteFO(long idCliente, List prodsNuevos) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            //Esto deja por default el criterio Jumbo
            String sql = "INSERT INTO FO_SUSTITUTOS_CLIENTES (PRO_ID, CLI_ID) VALUES (?,?)";
            
            conexion = JdbcDAOFactory.getConexion();
            
            for ( int i = 0; i < prodsNuevos.size(); i++ ) {
                try {
                    CarroCompraDTO carro = (CarroCompraDTO) prodsNuevos.get(i);
                    stm = conexion.prepareStatement(sql);
                    stm.setLong(1, Long.parseLong( carro.getPro_id() ) );
                    stm.setLong(2, idCliente );
                    stm.executeUpdate();
                    
                } catch (Exception e) { 
                    logger.error("addSustitutosCliente - No se pudo asignar criterio a cliente " + idCliente + " : " + e.getMessage());    
                }                
            }
        } catch (Exception ex) {
            logger.error("addSustitutosCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("addSustitutosCliente - Problema SQL (close)", e);
            }
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteHaCompradoFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean respuesta = false;
        try {
            String sql ="SELECT COUNT(T.ID_PEDIDO) COMPRAS " +
            			"FROM BODBA.BO_PEDIDOS T " +
						"WHERE ID_CLIENTE = ? " +
						"AND (T.ID_ESTADO = 8 OR T.ID_ESTADO = 10) ";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,idCliente);
            logger.debug("SQL clienteHaComprado: " + sql);
            rs = stm.executeQuery();
            if ( rs.next() ) {
                if ( rs.getInt("compras") > 0 ) {
                    respuesta = true;
                }
            }
            
        } catch (SQLException ex) {
            logger.error("clienteHaComprado - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteHaComprado - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteHaComprado - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }
    
    public List listasEspecialesByGrupoFO(long idGrupo, int idLocal) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String coma = "";
        try {
            String ids = "";
            String cru = 	"	SELECT CH_ID " +
            				"	FROM FO_LST_GRP_CH " +
            				"	WHERE ID_LST_GRUPO = " + idGrupo;
            
            conexion = JdbcDAOFactory.getConexion();
            
            stm = conexion.prepareStatement(cru  + " WITH UR");          
            rs = stm.executeQuery();
            while (rs.next()) {
                ids += coma + rs.getString("ch_id");
                coma = ",";
            }
            if (ids.length() == 0) {
                ids = "0";
            }
            
            String sql = "SELECT ch.ch_id, ch.ch_alias, ch.ch_unidades, 'Especiales' as ch_lugar, date(ch.ch_fec_crea) as ch_fecha, 'E' as ch_tipo, " +
                         "sum( chp.CHP_CANTIDAD * pl.PRE_VALOR) total " +
                         "FROM fo_ch_compra_historicas ch " +
                         "    inner join fo_ch_productos chp on (chp.CHP_CH_ID = ch.CH_ID) " +
                         "    inner join fo_productos p on (chp.CHP_PRO_ID = p.PRO_ID) " +
                         "    inner join fo_precios_locales pl on (pl.PRE_PRO_ID = p.PRO_ID) " +
                         "WHERE ch.ch_tipo = 'E' and ch.ch_id in (" + ids + ") " +
                         "and pl.PRE_LOC_ID = ? " +
                         "GROUP BY ch.ch_id, ch.ch_alias, ch.ch_unidades, ch.ch_fec_crea, ch_tipo " +
                         "ORDER BY ch.ch_alias";
            
            stm = conexion.prepareStatement(sql  + " WITH UR");
            stm.setInt(1,idLocal);
            rs = stm.executeQuery();
            while (rs.next()) {
                UltimasComprasDTO compras = new UltimasComprasDTO();
                compras.setId(rs.getLong("ch_id"));
                compras.setNombre( rs.getString("ch_alias") );
                compras.setFecha( rs.getTimestamp("ch_fecha").getTime() );
                compras.setLugar_compra(rs.getString("ch_lugar"));
                compras.setUnidades(rs.getDouble("ch_unidades"));
                compras.setTipo(rs.getString("ch_tipo"));
                compras.setTotal(rs.getDouble("total"));
                lista.add(compras);
            }
        } catch (SQLException ex) {
            logger.error("listasEspecialesByGrupo - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("listasEspecialesByGrupo - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("listasEspecialesByGrupo - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param listas
     * @param local
     * @return
     */
    public List clientesGetProductosListasEspecialesFO(String listas, String local, long cliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/fo_app.properties");
        Properties prop = new Properties();
        try {
            prop.load(log4jProps);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        List l_cat = new ArrayList();
        
        try {
            String lista_i = "";
            if( listas != null && listas.compareTo("") != 0 ) {
                // Se filtran las diferentes listas
                String []laux = listas.split(",");
                String comai = "";
                for( int i = 0; i < laux.length; i++ ) {
                    String []lista_aux = laux[i].split("-");
                    if( lista_aux[1].compareTo("E") == 0 ) {
                        lista_i += comai+""+lista_aux[0];
                        comai=",";
                    }                  
                }               
            }
            if( lista_i.compareTo("") == 0 )
                lista_i = "-1";
            conexion = JdbcDAOFactory.getConexion();
            
            String query =  "SELECT cat_id, cat_nombre, cat_orden, pro_id, pro_des_corta, pro_cod_sap, pro_unidad_medida, " +
                            "   pro_inter_valor, pro_inter_max, pro_nota, pro_tipre, pro_tipo_producto, pro_estado, pro_tipo_sel, " +
                            "   mar_id, mar_nombre, uni_desc, uni_cantidad, pre_valor, pre_stock, car_id, car_cantidad, car_nota, " +
                            "   pro_particionable, pro_particion, sum(chp_cantidad) as cantidad " +
                            "FROM fo_ch_productos " +
                            "   JOIN fo_productos on chp_pro_id = pro_id " +
                            "   JOIN fo_ch_compra_historicas on ch_id = chp_ch_id " +
                            "   LEFT JOIN fo_productos_categorias on (pro_id = prca_pro_id or pro_id_padre = prca_pro_id) AND prca_estado = 'A' " +
                            "   LEFT JOIN fo_categorias on prca_cat_id = cat_id AND cat_estado = 'A' " +
                            "   LEFT JOIN fo_precios_locales on pro_id = pre_pro_id AND pre_estado = 'A' AND pre_loc_id = ? " +
                            "   LEFT JOIN fo_marcas on mar_id = pro_mar_id " +
                            "   LEFT JOIN fo_unidades_medida on uni_id = pro_uni_id AND uni_estado = 'A' " +
                            "   LEFT JOIN fo_carro_compras on car_pro_id = pro_id AND car_cli_id = ? " +
                            "WHERE chp_ch_id in ("+lista_i+") " +
                            "GROUP BY cat_id, cat_nombre, cat_orden, pro_id, pro_des_corta, pro_cod_sap, pro_unidad_medida, " +
                            "   pro_inter_valor, pro_inter_max, pro_nota, pro_tipre, pro_tipo_producto, pro_estado, " +
                            "   pro_tipo_sel, mar_id, mar_nombre, uni_desc, uni_cantidad, pre_valor, pre_stock, " +
                            "   car_id, car_cantidad, car_nota, pro_particionable, pro_particion " +                           
                            "order by cat_id, pro_id, pro_tipo_producto, pro_des_corta, pre_valor";
            
            stm = conexion.prepareStatement(query  + " WITH UR");
            stm.setString(1, local);
            stm.setLong(2, cliente);
            rs = stm.executeQuery();
            while (rs.next()) {
                ArrayList campos = new ArrayList();
                campos.add("pro_id");
                if( this.revisionCamposFO( rs, campos ) == false ) {
                    continue;
                }
                // Revisar si existe producto en alguna categoría anterior
                boolean flag = true;
                for( int i = 0; flag == true && i < l_cat.size(); i++ ) {
                    UltimasComprasCategoriasDTO aux_cat = (UltimasComprasCategoriasDTO)l_cat.get(i);
                    List aux_lpro = aux_cat.getUltimasComprasProductosDTO();
                    for( int j = 0; flag == true && j < aux_lpro.size(); j++ ) {
                        UltimasComprasProductosDTO aux_pro = (UltimasComprasProductosDTO)aux_lpro.get(j);
                        if( aux_pro.getPro_id() == rs.getLong("pro_id") ) {
                            flag = false;
                        }
                    }
                }
                if( flag == false ) {
                    continue; // Se salta el registro
                }
                
                // Listado de productos por categorias
                List l_pro = new ArrayList();
                UltimasComprasCategoriasDTO categoria = null;
                                
                // Revisar si existe la categoría
                boolean flag_cat = true;
                for( int i = 0; i < l_cat.size(); i++ ) {
                    categoria = (UltimasComprasCategoriasDTO)l_cat.get(i);
                    if( categoria.getId() == rs.getLong("cat_id") ) {
                        l_pro = categoria.getUltimasComprasProductosDTO();
                        logger.debug("Obtener lista de producto de categoría "+categoria.getCategoria()+" --->" + l_pro.size() );                       
                        flag_cat = false;
                        break;
                    }
                }               

                // Agregar categorias sólo si es una categoría nueva
                if( flag_cat == true ) {
                    categoria = new UltimasComprasCategoriasDTO();
                    // Si el producto no tiene categoría se pone en categoría Sin Clasificar
                    if( rs.getString("cat_id") == null ) {
                        categoria.setId(0);
                        categoria.setCategoria( prop.getProperty("jdbcclientesdao.nombrecategoria") );
                    }
                    else {
                        categoria.setId( rs.getLong("cat_id") );
                        categoria.setCategoria( rs.getString("cat_nombre") );
                    }
                }
                    
                // Agregar los productos
                UltimasComprasProductosDTO productos = new UltimasComprasProductosDTO();
                productos.setPro_id(rs.getLong("pro_id"));
                productos.setNombre(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
                productos.setCodigo(rs.getString("pro_cod_sap"));
                if( rs.getString("uni_cantidad") != null && rs.getString("pre_valor") != null && rs.getString("pro_unidad_medida") != null )
                    productos.setPpum(rs.getDouble("pre_valor")/(rs.getDouble("uni_cantidad")*rs.getDouble("pro_unidad_medida")));
                productos.setPrecio(rs.getDouble("pre_valor"));
                productos.setMarca(rs.getString("mar_nombre"));
                productos.setUnidad_nombre(rs.getString("uni_desc"));
                productos.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                if( rs.getString("pro_tipre") != null )
                    productos.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                productos.setInter_maximo( rs.getDouble("pro_inter_max") );
                if( rs.getString("pro_inter_valor") != null )
                    productos.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    productos.setInter_valor(1.0);

                // Se debe aproximar al más cercano
                if (rs.getString("car_cantidad") == null) {
                    productos.setCantidad(rs.getDouble("cantidad"));
                } else {
                    productos.setCantidad(rs.getDouble("car_cantidad"));
                }
                
                if( rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S' ) {
                    productos.setCon_nota(true);
                    productos.setNota( rs.getString("car_nota") );
                }
                else
                    productos.setCon_nota(false);
                productos.setCar_id(rs.getLong("car_id"));              
                if( rs.getString("car_id") == null )
                    productos.setEn_carro( false );
                else
                    productos.setEn_carro( true );              
                
                if( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 )
                    productos.setStock(rs.getLong("pre_stock"));
                else
                    productos.setStock(100); // No toma encuenta el stock nunca pone 0
                
                //particionable
                if ( rs.getString("pro_particionable") != null ) {
                    if ( rs.getString("pro_particionable").toString().equalsIgnoreCase("S") ) {
                        productos.setEsParticionable("S");
                        if ( rs.getString("pro_particion") != null ) {
                            productos.setParticion(Long.parseLong( rs.getString("pro_particion") ));
                        } else {
                            productos.setParticion(1);    
                        }
                    } else {
                        productos.setEsParticionable("N");
                        productos.setParticion(1);
                    }                   
                }
                
                
                // Si no tiene información necesaria => stock=0 
                if( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("cat_id") == null 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 )
                    productos.setStock(0);
                
                l_pro.add(productos);
                
                categoria.setUltimasComprasProductosDTO(l_pro);

                if( flag_cat == true ) {
                    l_cat.add(categoria);
                }
                
            }
            
        } catch (SQLException ex) {
            logger.error("clientesGetProductosListasEspeciales - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clientesGetProductosListasEspeciales - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clientesGetProductosListasEspeciales - Problema SQL (close)", e);
            }
        }

        return l_cat;
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneDisponibleRetirarEnLocalFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean respuesta = false;
        String SQL = "";
        
        try {

            SQL = "SELECT D.DIR_ID "
                + "FROM FODBA.FO_DIRECCIONES D "
                + "     JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                + "     JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA "
                + "     JOIN BODBA.BO_LOCALES  L ON L.ID_LOCAL    = Z.ID_LOCAL "
                + "WHERE D.DIR_CLI_ID = ? "
                + "  AND D.DIR_ESTADO = 'A' "
                + "  AND L.RETIRO_LOCAL = 'S' "
                + "  AND L.ID_ZONA_RETIRO IS NOT NULL";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(SQL + " WITH UR ");          
            stm.setLong(1,idCliente);
            logger.debug("SQL (clienteTieneDisponibleRetirarEnLocal): " + SQL);
            rs = stm.executeQuery();
            if ( rs.next() ) {
                respuesta = true;
            }
            
        } catch (SQLException ex) {
            logger.error("clienteTieneDisponibleRetirarEnLocal - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteTieneDisponibleRetirarEnLocal - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteTieneDisponibleRetirarEnLocal - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public CriterioClienteSustitutoDTO criterioSustitucionByClienteProductoFO(long idCliente, long idProducto) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
        try {
            String sql = "select id_criterio, desc_criterio " +
                         "from fo_sustitutos_clientes sc " +
                         "where sc.cli_id = ? and sc.pro_id = ?";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql + " WITH UR ");          
            stm.setLong(1,idCliente);
            stm.setLong(2,idProducto);
            logger.debug("SQL criterioSustitucionByClienteProducto: " + sql);
            rs = stm.executeQuery();
            if ( rs.next() ) {
                criterio.setIdCriterio(rs.getLong("id_criterio"));
                criterio.setDescripcion(rs.getString("desc_criterio"));                                
            }
            
        } catch (SQLException ex) {
            logger.error("criterioSustitucionByClienteProducto - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("criterioSustitucionByClienteProducto - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("criterioSustitucionByClienteProducto - Problema SQL (close)", e);
            }
        }
        return criterio;
    }

    /**
     * @param idCliente
     * @param criterio
     * @return
     */
    public void setCriterioClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE fo_sustitutos_clientes " +
                         "set id_criterio = ?, desc_criterio = ?, fecha_modificacion = ?, asigno_cliente = ? " +
                         "where pro_id = ? and cli_id = ?";
            
            conexion = JdbcDAOFactory.getConexion();
            
                stm = conexion.prepareStatement(sql);
                stm.setLong( 1, criterio.getIdCriterio() );
                if ( criterio.getIdCriterio() == 4) {
                    stm.setString(2, criterio.getSustitutoCliente() );
                } else {
                    stm.setNull(2, java.sql.Types.VARCHAR );
                }
                stm.setTimestamp(3, new Timestamp(new GregorianCalendar().getTimeInMillis()));
                stm.setString(4, "S" );
                stm.setLong(5, criterio.getIdProducto() );
                stm.setLong(6, idCliente );
                stm.executeUpdate();
            
        } catch (SQLException ex) {
            logger.error("setCriterioCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("setCriterioCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("setCriterioCliente - Problema SQL (close)", e);
            }
        }  
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void addSustitutoClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String sql = "insert into fo_sustitutos_clientes " +
                         "(pro_id, cli_id, id_criterio, desc_criterio, asigno_cliente) " +
                         "values (?,?,?,?,?)";
            
            conexion = JdbcDAOFactory.getConexion();
            
            stm = conexion.prepareStatement(sql);
            stm.setLong(1, criterio.getIdProducto() );
            stm.setLong(2, idCliente );
            stm.setLong(3, criterio.getIdCriterio() );
            if ( criterio.getIdCriterio() == 4) {
                stm.setString(4, criterio.getSustitutoCliente() );
            } else {
                stm.setNull(4, java.sql.Types.VARCHAR );
            }
            stm.setString(5,"S");
            stm.executeUpdate();
           
        } catch (SQLException ex) {
            logger.error("addSustitutoCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("addSustitutoCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("addSustitutoCliente - Problema SQL (close)", e);
            }
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean tieneDireccionesConCoberturaFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean tiene = false;
        try {
           
            String SQL = "SELECT COUNT(DIR_ID) CANTIDAD "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "WHERE D.DIR_ESTADO = 'A' "
                       + "  AND D.DIR_CLI_ID = ? "
                       + "  AND D.ID_POLIGONO != 0";
            conexion = JdbcDAOFactory.getConexion();
            
            stm = conexion.prepareStatement(SQL + " WITH UR ");
            stm.setLong(1, idCliente );
            
            rs = stm.executeQuery();
            if ( rs.next() ) {
                if ( rs.getLong("CANTIDAD") > 0 ) {
                    tiene = true;
                }
            }
           
        } catch (SQLException ex) {
            logger.error("tieneDireccionesConCobertura - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("tieneDireccionesConCobertura - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
            	if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("tieneDireccionesConCobertura - Problema SQL (close)", e);
            }
        }
        return tiene;
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteAllDireccionesConCoberturaFO(long idCliente) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;        
        try {
            String SQL = "SELECT D.DIR_ID, Z.ID_LOCAL, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, D.DIR_CALLE, "
                       + "       D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, D.DIR_FEC_CREA, P.ID_ZONA, "
                       + "       C.NOMBRE AS COM_NOMBRE, C.REG_ID AS REGION, R.REG_NOMBRE, R.REG_ORDEN, "
                       + "       D.LATITUD, D.LONGITUD, D.CONFIRMADA "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA   = D.DIR_COM_ID "
                       + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID      = C.REG_ID "
                       + "     JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "WHERE D.DIR_ESTADO = 'A' "
                       + "  AND D.DIR_CLI_ID = ? ";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(SQL + " WITH UR ");
            stm.setLong(1, idCliente);
            rs = stm.executeQuery();
            while (rs.next()) {
                DireccionesDTO dir = new DireccionesDTO();
                dir.setId(rs.getLong("dir_id"));
                dir.setLoc_cod(rs.getLong("ID_LOCAL"));
                dir.setCom_id(rs.getLong("dir_com_id"));
                dir.setTipo_calle(rs.getLong("dir_tip_id"));
                dir.setId_cliente(rs.getLong("dir_cli_id"));
                dir.setAlias( rs.getString("dir_alias") );
                dir.setCalle( rs.getString("dir_calle") );
                dir.setNumero( rs.getString("dir_numero") );
                dir.setDepto( rs.getString("dir_depto") );
                dir.setComentarios( rs.getString("dir_comentarios") );
                if (rs.getTimestamp("dir_fec_crea") != null)
                    dir.setFec_crea_long(rs.getTimestamp("dir_fec_crea").getTime());
                dir.setEstado(rs.getString("dir_estado"));
                dir.setZona_id(rs.getLong("ID_ZONA"));
                dir.setReg_id(rs.getLong("region"));
                dir.setCom_nombre(rs.getString("com_nombre"));
                dir.setReg_nombre(rs.getString("reg_nombre"));
                dir.setLatitud(rs.getDouble("LATITUD"));
                dir.setLongitud(rs.getDouble("LONGITUD"));
                dir.setConfirmada(rs.getInt("CONFIRMADA")!=0);
                lista.add(dir);
            }
        } catch (SQLException ex) {
            logger.error("clienteAllDireccionesConCobertura - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteAllDireccionesConCobertura - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteAllDireccionesConCobertura - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteConOPsTrackingFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean respuesta = false;
        try {
            String sql = "select count( p.ID_PEDIDO ) pedidos " + fromTrackingOpFO();  
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql + " WITH UR ");          
            stm.setDate(1, fechaParaTrackingOPFO());
            stm.setLong(2, idCliente);            
            logger.debug("SQL clienteConOPsTracking: " + sql + " WITH UR ");
            rs = stm.executeQuery();
            if ( rs.next() ) {
                if ( rs.getInt("pedidos") > 0 ) {
                    respuesta = true;
                }
            }
        } catch (SQLException ex) {
            logger.error("clienteConOPsTracking - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteConOPsTracking - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteConOPsTracking - Problema SQL (close)", e);
            }
        }
        return respuesta;
    }

    /**
     * Entrega el listado de ultimos 4 pedidos para el tracking op
     * @param idCliente
     * @return
     */
    public List clienteListaOPsTrackingFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List ops = new ArrayList();
        try {            
            
            String sql = "select p.ID_PEDIDO, p.ID_ESTADO, p.FCREACION fecha_compra, d.FECHA fecha_despacho, p.ID_ESTADO, p.secuencia_pago " +
                         fromTrackingOpFO() +
                         "order by d.FECHA desc ";  
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setDate(1, fechaParaTrackingOPFO());
            stm.setLong(2, idCliente);            
            logger.debug("SQL clienteListaOPsTracking: " + sql);
            rs = stm.executeQuery();
            int i = 0;
            while ( rs.next() && i <= 3 ) {
            	ListaPedidoClienteDTO op = new ListaPedidoClienteDTO();
                op.setId_pedido( rs.getLong("ID_PEDIDO") );
                op.setFingreso( rs.getString("fecha_compra") );
                op.setFdespacho( rs.getString("fecha_despacho") );
                op.setId_estado( rs.getLong("ID_ESTADO"));
                op.setEstado( estadoVisibleParaClientesFO( rs.getInt("ID_ESTADO") ) );
                op.setSecuenciaPago( rs.getString("secuencia_pago") );
                ops.add(op);   
                i++;
            }
        } catch (SQLException ex) {
            logger.error("clienteListaOPsTracking - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteListaOPsTracking - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteListaOPsTracking - Problema SQL (close)", e);
            }
        }
        return ops;
    }
    
    /**
     * Esto nos entrega el nombre del estado que ve el cliente
     * @param idEstado
     * @return
     */
    //FIXME : Eliminar SWITCH - CASE
    private String estadoVisibleParaClientesFO(int idEstado) {
        switch (idEstado) {
            case 3:
            case 4:
            case 34:
                return "Ingresado";
                
            case 5:
                return "Validado";
            
            case 6:
                return "En Picking";
               
            case 7:
            case 54:
            case 21:
                return "En Bodega";
                
            case 9:
            case 8:
                return "En Camino";
    
            case 10:
                return "Finalizado";
                
            case 20:
                return "Anulado";
        }
        return "Ingresado";
    }

    /**
     * Esto nos estrega la fecha de los pedidos que mostraremos en el tracking
     * @return
     */
    private Date fechaParaTrackingOPFO() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        return new Date(cal.getTimeInMillis());
    }
    
    /**
     * Parte de una query para tracking, se deja aca ya que si se modifica, el cambio afecta a 2 metodos
     * @return
     */
    private String fromTrackingOpFO() {
        return   "from bodba.bo_pedidos p " +
                 "    inner join bodba.bo_jornada_desp d on p.ID_JDESPACHO = d.ID_JDESPACHO " +
                 "where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and d.FECHA >= ? " +
                 "and p.ID_CLIENTE = ? ";
    }

    /**
     * @param rut
     * @return
     */
    public List clienteComprasEnLocalFO(long rut) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List compras = new ArrayList();
        try {            
            
            String sql = "SELECT compras.*, count(productos.FPC_ID) as unidades FROM FODBA.FO_COMPRAS_LOCALES as compras " +
                         "join FODBA.FO_PRODUCTOS_COMPRA as productos on productos.FCL_ID = compras.FCL_ID " +
                         "WHERE compras.FCL_RUT = ? and compras.FCL_ESTADO = ?" +
                         "group by compras.FCL_ID, compras.FCL_CODIGO_LOCAL, compras.FCL_RUT, " +
                         "compras.FCL_FECHA, compras.FCL_NOMBRE_LOCAL, compras.FCL_ESTADO " +
                         "order by compras.FCL_FECHA desc";  
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1, rut);
            stm.setInt(2, CompraCDP.CARGADO_A_CLIENTE);            
            logger.debug("SQL clienteComprasEnLocal: " + sql + " WITH UR ");
            rs = stm.executeQuery();
            while ( rs.next() ) {
                CompraHistorica compra = new CompraHistorica(rs.getInt("FCL_ID"));
                compra.setCodigoLocal(rs.getString("FCL_CODIGO_LOCAL"));
                compra.setRut(Integer.parseInt(""+rut));
                compra.setFecha(rs.getDate("FCL_FECHA"));
                if (rs.getString("FCL_NOMBRE_LOCAL") != null)
                    compra.setNombreLocal(rs.getString("FCL_NOMBRE_LOCAL"));
                compra.setUnidades(rs.getInt("unidades"));
                compras.add(compra);
            }
        } catch (SQLException ex) {
            logger.error("clienteComprasEnLocal - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("clienteComprasEnLocal - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("clienteComprasEnLocal - Problema SQL (close)", e);
            }
        }
        return compras;
    }

    /**
     * @param email
     * @throws DuplicateKeyException
     */
    public void addMailHomeFO(String email) throws ClientesDAOException, DuplicateKeyException {

        Connection conexion = null;
        PreparedStatement stm = null;
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement("INSERT INTO FO_EMAILS ( mail ) VALUES ( ? ) ");
            stm.setString(1, email );         
            stm.executeUpdate();  
        
        }	
        catch (SQLException ex) {
        	if(ex.getErrorCode() == COD_ERROR_DUPLICATE){
                logger.error("addMailHome - Problema SQL", ex);
                throw new DuplicateKeyException(ex);
        	}
            logger.error("addMailHome - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("addMailHome - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("addMailHome - Problema SQL (close)", e);
            }
        }
    }

    

    /**
     * @param double lat, double lng, long dir_id
     * @return
     */
    public boolean AlmacenaConfirmacionMapaFO(double lat, double lng, long dir_id) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean resp = false;
        String SQL = "";
        try {
            SQL = "UPDATE FODBA.FO_DIRECCIONES "
                + "   SET LATITUD   = ?, "
                + "       LONGITUD  = ?, "
                + "       CONFIRMADA= 1, "
                + "       FECHA_CONFIRMACION = CURRENT TIMESTAMP "
                + " WHERE DIR_ID    = ?  ";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(SQL);
            stm.setDouble(1, lat);
            stm.setDouble(2, lng);
            stm.setLong(3, dir_id);
            
            int numReg = stm.executeUpdate();
            if (numReg > 0){
                resp = true;
                logger.error("Dirección Confirmada con Exito");
            }
        } catch (SQLException ex) {
            logger.error("AlmacenaConfirmacionMapa - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("AlmacenaConfirmacionMapa - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("AlmacenaConfirmacionMapa - Problema SQL (close)", e);
            }
        }
        return resp;
    }

    public void eliminaSuscripcionEncuestaByRutFO(long rut) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = "UPDATE FODBA.FO_CLIENTES " +
                  "SET CLI_RECIBE_ENCUESTAS = 0 " +
                  "WHERE CLI_RUT = ?";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql);
            stm.setLong(1, rut);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            logger.error("eliminaSuscripcionEncuestaByRut - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("eliminaSuscripcionEncuestaByRut - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("eliminaSuscripcionEncuestaByRut - Problema SQL (close)", e);
            }
        }
    }

    /**
     * @param idCliente
     * @param idComuna
     * @return
     */
    public DireccionesDTO getDireccionClienteByComunaFO(long idCliente, long idComuna) throws ClientesDAOException {
        DireccionesDTO dir = new DireccionesDTO();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;        
        try {
            String sql = "SELECT D.DIR_ID, Z.ID_LOCAL, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, D.DIR_CALLE, " +
                         "D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, D.DIR_FEC_CREA, P.ID_ZONA, " +
                         "C.NOMBRE AS COM_NOMBRE, C.REG_ID AS REGION, R.REG_NOMBRE, R.REG_ORDEN, " +
                         "D.LATITUD, D.LONGITUD, D.CONFIRMADA " +
                         "FROM FODBA.FO_DIRECCIONES D " +
                         "JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA   = D.DIR_COM_ID " +
                         "JOIN BODBA.BO_REGIONES R ON R.REG_ID      = C.REG_ID " +
                         "JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO " +
                         "JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA " +
                         "WHERE D.DIR_ESTADO = 'A' AND D.DIR_CLI_ID = ? AND D.DIR_COM_ID = ?"; 
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");
            stm.setLong(1, idCliente);
            stm.setLong(2, idComuna);
            rs = stm.executeQuery();
            if (rs.next()) {
                dir.setId(rs.getLong("dir_id"));
                dir.setLoc_cod(rs.getLong("ID_LOCAL"));
                dir.setCom_id(rs.getLong("dir_com_id"));
                dir.setTipo_calle(rs.getLong("dir_tip_id"));
                dir.setId_cliente(rs.getLong("dir_cli_id"));
                dir.setAlias( rs.getString("dir_alias") );
                dir.setCalle( rs.getString("dir_calle") );
                dir.setNumero( rs.getString("dir_numero") );
                dir.setDepto( rs.getString("dir_depto") );
                dir.setComentarios( rs.getString("dir_comentarios") );
                if (rs.getTimestamp("dir_fec_crea") != null)
                    dir.setFec_crea_long(rs.getTimestamp("dir_fec_crea").getTime());
                dir.setEstado(rs.getString("dir_estado"));
                dir.setZona_id(rs.getLong("ID_ZONA"));
                dir.setReg_id(rs.getLong("region"));
                dir.setCom_nombre(rs.getString("com_nombre"));
                dir.setReg_nombre(rs.getString("reg_nombre"));
                dir.setLatitud(rs.getDouble("LATITUD"));
                dir.setLongitud(rs.getDouble("LONGITUD"));
                dir.setConfirmada(rs.getInt("CONFIRMADA")!=0);               
            }
        } catch (SQLException ex) {
            logger.error("getDireccionClienteByComuna - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getDireccionClienteByComuna - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getDireccionClienteByComuna - Problema SQL (close)", e);
            }
        }
        return dir;
    }

    /**
     * @param idCliente
     * @param idSession
     */
    public void convierteCarroDonaldFO(long idCliente, String idSession) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        PreparedStatement stm1 = null;
        ResultSet rs = null;        
        try {
            boolean tieneCarroDonald = false;
            String sqlSel = "select c.CAR_ID " +
                            "from fodba.fo_carro_compras c " +
                            "where c.CAR_CLI_ID = 1 and c.CAR_INVITADO_ID=?"; 
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlSel  + " WITH UR");
            stm.setString(1, idSession);
            rs = stm.executeQuery();
            if (rs.next()) {
                tieneCarroDonald = true;               
            }
            
            if ( tieneCarroDonald ) {                
                //Borramos actual carro
                String sqlDel = "delete from fodba.fo_carro_compras c where c.CAR_CLI_ID = ?";
                stm = conexion.prepareStatement(sqlDel);
                stm.setLong(1, idCliente);
                stm.executeUpdate();
                
                //Migramos el carro donald al cliente
                String sqlUpd = "update fodba.fo_carro_compras c set c.CAR_INVITADO_ID = 0, c.CAR_CLI_ID = ? " +
                                "where c.CAR_CLI_ID = 1 and c.CAR_INVITADO_ID=?";
                stm1 = conexion.prepareStatement(sqlUpd);
                stm1.setLong(1, idCliente);
                stm1.setString(2, idSession);
                stm1.executeUpdate();                
            }
            
        } catch (SQLException ex) {
            logger.error("conviertecarroDonald - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("conviertecarroDonald - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (stm1 != null)
                    stm1.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("conviertecarroDonald - Problema SQL (close)", e);
            }
        }
    }
    /**
     * @param idCliente - Cliente destino
     * @param idSession - Session del carro
     * @param idInvitado - Id usuario Invitado despues de pago
     */
    public void convierteCarroDonaldAfterPagoFO(long idCliente, String idSession, long idInvitado) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        PreparedStatement stm1 = null;
        ResultSet rs = null;        
        try {
            boolean tieneCarroDonald = false;
            String sqlSel = "select c.CAR_ID " +
                            "from fodba.fo_carro_compras c " +
                            "where c.CAR_CLI_ID = ? and c.CAR_INVITADO_ID=?"; 
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlSel  + " WITH UR");
            stm.setLong(1, idInvitado);
            stm.setString(2, idSession);
            rs = stm.executeQuery();
            if (rs.next()) {
                tieneCarroDonald = true;               
            }
            
            if ( tieneCarroDonald ) {                
                //Borramos actual carro del cliente registrado
                String sqlDel = "delete from fodba.fo_carro_compras c where c.CAR_CLI_ID = ?";
                stm = conexion.prepareStatement(sqlDel);
                stm.setLong(1, idCliente);
                stm.executeUpdate();
                
                //Migramos el carro donald al cliente
                String sqlUpd = "update fodba.fo_carro_compras c set c.CAR_INVITADO_ID = 0, c.CAR_CLI_ID = ? " +
                                "where c.CAR_CLI_ID = ? and c.CAR_INVITADO_ID=?";
                stm1 = conexion.prepareStatement(sqlUpd);
                stm1.setLong(1, idCliente);
                stm1.setLong(2, idInvitado);
                stm1.setString(3, idSession);
                stm1.executeUpdate();                
            }
            
        } catch (SQLException ex) {
            logger.error("convierteCarroDonaldAfterPago - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("convierteCarroDonaldAfterPago - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (stm1 != null)
                    stm1.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("convierteCarroDonaldAfterPago - Problema SQL (close)", e);
            }
        }
    }

    
    public void RecuperaClave_GuardaKeyClienteFO(long idCliente, String key) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            //Migramos el carro donald al cliente
            String sqlUpd = "UPDATE FODBA.FO_CLIENTES C "
                          + "   SET C.CLI_KEY_RECUPERA_CLAVE = ? "
                          + " WHERE C.CLI_ID = ?";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlUpd);
            stm.setString(1, key);
            stm.setLong(2, idCliente);
            stm.executeUpdate();
        } catch (SQLException ex) {
            logger.error("RecuperaClave_GuardaKeyCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("RecuperaClave_GuardaKeyCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("RecuperaClave_GuardaKeyCliente - Problema SQL (close)", e);
            }
        }
    }

    
    public String RecuperaClave_getKeyClienteFO(long idCliente) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String key = "";

        try {
            //Migramos el carro donald al cliente
            String sqlUpd = "SELECT COALESCE(C.CLI_KEY_RECUPERA_CLAVE, '') AS CLI_KEY_RECUPERA_CLAVE "
                          + "  FROM FODBA.FO_CLIENTES C "
                          + " WHERE C.CLI_ID = ?";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlUpd + " WITH UR");
            stm.setLong(1, idCliente);
            rs = stm.executeQuery();
            if (rs.next()){
                key = rs.getString("CLI_KEY_RECUPERA_CLAVE");
            }
        } catch (SQLException ex) {
            logger.error("RecuperaClave_GuardaKeyCliente - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("RecuperaClave_GuardaKeyCliente - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
            	if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("RecuperaClave_GuardaKeyCliente - Problema SQL (close)", e);
            }
        }
        return key;
    }
    
    public void setClienteFacebookFO(long id_cliente, String  id_facebook)throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;

        try {
            String sqlUpd = "UPDATE FODBA.FO_CLIENTES SET CLI_ID_FACEBOOK = ? WHERE CLI_ID = " + id_cliente;
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlUpd);
            stm.setString(1, id_facebook);
            stm.executeUpdate();
        } catch (SQLException ex) {
            logger.error("setClienteFace - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("setClienteFace - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("setClienteFacebook - Problema SQL (close)", e);
            }
        }
    	
    }
	
    public String getRutByIdFacebookFO(String idFacebook) throws ClientesDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String rut = null;

        try {
            String sqlUpd = "SELECT CLI_RUT FROM FODBA.FO_CLIENTES C WHERE C.CLI_ID_FACEBOOK = ?";
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sqlUpd + " WITH UR");
            stm.setString(1, idFacebook);
            rs = stm.executeQuery();
            if (rs.next()){
                rut = rs.getString("CLI_RUT");
            }
        }  catch (Exception ex) {
            logger.error("getRutByIdFacebook - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
            	if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getRutByIdFacebook - Problema SQL (close)", e);
            }
        }
        return rut;
    }
    
    /**
     * Filtro por idProducto 
     * @param cliente_id
     * @param local
     * @param idSession
     * @param idProducto
     * @return
     * @throws ClientesDAOException
     */
    public List getItemCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String idProducto ) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        MiCarroDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
        
        query = "select  min(inter.cat_id) as inter_id, min(inter.cat_nombre) as inter_nom, min(ter.cat_id) as ter_id, min(ter.cat_nombre) as ter_nom, " +
        		"pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
                "pr.pro_pesable, pr.pro_id_bo, pr.pro_estado, pre.pre_valor, pre.pre_tienestock, pre.pre_stock,um.uni_cantidad,um.uni_desc, max(cod.cod_barra) as barra, substr(bopro.id_catprod,1,2) as catsap, " + 
				"pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, pr.pro_id_desp, pr.PRO_ESTADO estadoProducto, pre.PRE_ESTADO estadoPreciosLocales " +
				//"pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, pr.pro_id_desp, pr.PRO_ESTADO estadoProducto, pre.PRE_ESTADO estadoPreciosLocales " +
				"from fo_carro_compras cc inner join fo_productos pr on (cc.car_pro_id = pr.pro_id) " +
				"inner join fo_precios_locales pre on (pre.pre_pro_id = pr.pro_id and pre.pre_loc_id = " + local + ") " + 
				"inner join fo_unidades_medida um on (um.uni_id = pr.pro_uni_id) " +
				"inner join fo_marcas ma on (pr.pro_mar_id = ma.mar_id) " +
				"inner join fo_productos_categorias prca on (pr.pro_id = prca.prca_pro_id) " +
				"inner join fo_categorias ter on (prca.prca_cat_id = ter.cat_id and ter.cat_estado = 'A') " +
				"inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
				"inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
				"inner join bo_productos bopro on (bopro.id_producto = pr.pro_id_bo) " +
				"inner join bo_codbarra cod on (cod.id_producto = bopro.id_producto) " ;
        //"left join fo_sustitutos_clientes sc on (pr.pro_id = sc.pro_id and cc.car_cli_id = sc.cli_id ";  
        
        /*if ( cliente_id == 1 ) {
            query += " and sc_invitado_id=" + idSession + ") ";
        } else {
            query += " ";
            //query += ") ";
        }
        query += "left join fo_sustitutos_criterio scr on (sc.id_criterio = scr.id_criterio) " +
        "left join " +
        "(select car_pro_id, max(ter.cat_id) as ter_id " +
        "from fo_carro_compras cc " +
        "inner join fo_productos_categorias prcat on (cc.car_pro_id = prcat.prca_pro_id) " +
        "inner join fo_categorias ter on ter.cat_id = prcat.prca_cat_id and ter.cat_estado = 'A' " +
        "inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) " +
        "inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') " +
        "WHERE cc.car_cli_id = " + cliente_id + " AND cc.car_pro_id =" + idProducto; 
        if ( cliente_id == 1 ) {
            query += " and car_invitado_id=" + idSession;
        }
        query += " group by cc.car_pro_id " + 
        ") as x " +
        "on x.ter_id = ter.cat_id " ;   */
        query += "where car_cli_id = " + cliente_id + " AND cc.car_pro_id =" + idProducto; 
        if ( cliente_id == 1 ) {
        query += " and car_invitado_id=" + idSession;
        }
         
        query += " group by pr.pro_id, pr.pro_des_corta, cc.car_id, cc.car_cantidad, cc.car_nota, cc.car_tipo_sel, pr.pro_inter_max, pr.pro_inter_valor, pr.pro_tipo_sel, " +
        "pr.pro_pesable,  pr.pro_id_bo, pr.pro_estado,pre.pre_valor, pre.pre_tienestock, pre.pre_stock, um.uni_cantidad, um.uni_desc, substr(bopro.id_catprod,1,2), " +
        "pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, pr.pro_id_desp, pr.PRO_ESTADO, pre.PRE_ESTADO " +
        //"pr.pro_unidad_medida, pr.pro_tipre, pr.pro_tipo_producto, pr.pro_cod_sap, ma.mar_nombre, sc.id_criterio, sc.desc_criterio, scr.descripcion, sc.asigno_cliente, pr.pro_id_desp, pr.PRO_ESTADO, pre.PRE_ESTADO " +
        "order by inter_nom, ter_nom ";    
         
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query  + " WITH UR");
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                 
                logger.logData( "getCarroComprasPorCategorias", rs);

                dto = new MiCarroDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                dto.setTipoSel(rs.getString("car_tipo_sel"));
                dto.setIdProDesp(rs.getLong("pro_id_desp"));
                if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                    dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                dto.setStock(rs.getLong("pre_stock"));
                dto.setPrecio(rs.getDouble("pre_valor"));
                if (rs.getInt("pre_tienestock") == 0)
                    dto.setTieneStock(false);
                else
                    dto.setTieneStock(true);
                dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                dto.setUnidad_nombre( rs.getString("uni_desc") );
                dto.setNom_marca(rs.getString("mar_nombre"));                   
                dto.setCatsap(rs.getString("catsap"));
                dto.setCodbarra(rs.getString("barra"));
                dto.setCodSap(rs.getString("pro_cod_sap"));
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                
                if ( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                    dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }                
                if ( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                dto.setIdTerminal(rs.getLong("ter_id"));
                dto.setIdIntermedia(rs.getLong("inter_id"));
                dto.setNombreTerminal(rs.getString("ter_nom"));
                dto.setNombreIntermedia(rs.getString("inter_nom"));
                /*try{
                if (rs.getString("id_criterio") != null) {
                    dto.setIdCriterio(rs.getLong("id_criterio"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    if (rs.getString("desc_criterio") != null)
                        dto.setSustitutoCliente(rs.getString("desc_criterio"));
                    dto.setAsignoCliente(rs.getString("asigno_cliente"));
                }
                }catch(SQLException ex){ logger.error("getCarroComprasPorCategorias - Problema SQL sustitutos", ex);}*/
                dto.setEstadoProducto(rs.getString("estadoProducto"));
                dto.setEstadoPreciosLocales(rs.getString("estadoPreciosLocales"));
                lista.add(dto);
                 
            }
        } catch (SQLException ex) {
            logger.error("getItemCarroComprasPorCategorias - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getItemCarroComprasPorCategorias - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getItemCarroComprasPorCategorias - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    public List getCarroCompraVentaMasiva(long cliente_id, String local, String idSession ) throws ClientesDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        CarroCompraDTO dto = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String query = null;
            query = "SELECT car_id, car_cantidad, car_nota, car_tipo_sel, car_fec_crea, pro_id, pro_des_corta, pro_inter_max, " +
                    "pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, " +
                    "pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2) as catsap, substr(id_catprod,3,2) as id_rubro, " +
                    "max(cod_barra) as barra " +
                    "FROM fo_carro_compras inner join fo_productos on pro_id = car_pro_id " +
                    
                    //Agregado para no considerar productos sin categorias. 
                    //diferencia montos FO y medios de pago.

                    
                    "inner join fo_precios_locales on pre_pro_id = pro_id " +
                    "inner join fo_unidades_medida on uni_id = pro_uni_id " +
                    "inner join fo_marcas on mar_id = pro_mar_id " +  
                    "inner join bo_productos pro on pro.id_producto = pro_id_bo " +
                    "inner join bo_codbarra cod on cod.id_producto = pro.id_producto " +  
                    "where car_cli_id = ? and pre_loc_id = ?  ";
            query +=" group by car_id, car_cantidad, car_nota, car_tipo_sel, car_fec_crea, pro_id, pro_des_corta, pro_inter_max, pro_inter_valor, pro_tipre, pro_tipo_producto, pro_tipo_sel, pro_unidad_medida, pro_pesable, pro_id_bo, pro_estado, pre_valor, pre_stock, uni_cantidad, uni_desc, mar_nombre, substr(id_catprod,1,2), substr(id_catprod,3,2) " +
                    "order by pro_tipo_producto ";
                
        try {
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query  + " WITH UR");
            stm.setLong(1, cliente_id);
            if( local.compareTo("-1") != 0 )
                stm.setLong(2, Long.parseLong(local));
			logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                
                logger.logData( "getCarroCompra", rs);

                dto = new CarroCompraDTO();
                dto.setId(rs.getLong("car_id"));
                dto.setCantidad(rs.getDouble("car_cantidad"));
                dto.setPro_id(rs.getString("pro_id"));
                dto.setNombre(rs.getString("pro_des_corta"));
                dto.setNota(rs.getString("car_nota"));
                String tipo = (Utils.isEmpty(rs.getString("car_tipo_sel")))? "W":rs.getString("car_tipo_sel");
                dto.setTipoSel(tipo);
                dto.setCar_fec_crea(rs.getString("car_fec_crea"));
                if( local.compareTo("-1") != 0 ) { // Si es para mostrar todos los productos
                    if( rs.getString("pro_unidad_medida") != null && rs.getString("uni_cantidad") != null )
                        dto.setPpum((rs.getDouble("pre_valor")/rs.getDouble("pro_unidad_medida")*rs.getDouble("uni_cantidad")));
                    dto.setStock(rs.getLong("pre_stock"));
                    dto.setPrecio(rs.getDouble("pre_valor"));
                    dto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
                    dto.setUnidad_nombre( rs.getString("uni_desc") );
                    dto.setNom_marca(rs.getString("mar_nombre"));
                    dto.setCatsap(rs.getString("catsap"));
                    dto.setCodbarra(rs.getString("barra"));
                    dto.setId_rubro(rs.getInt("id_rubro"));
                }
                dto.setInter_maximo(rs.getDouble("pro_inter_max"));
                if( rs.getString("pro_inter_valor") != null )
                    dto.setInter_valor(rs.getDouble("pro_inter_valor"));
                else
                    dto.setInter_valor(1.0);                
                dto.setTipre( Formatos.formatoUnidadFO(rs.getString("pro_tipre")) );
                dto.setTipo_producto(rs.getString("pro_tipo_producto"));
                dto.setPesable(rs.getString("pro_pesable"));
                dto.setId_bo(rs.getLong("pro_id_bo"));
                
                if ( rs.getString("pre_stock") != null && rs.getLong("pre_stock") > 0 ) {
                    dto.setStock(rs.getLong("pre_stock"));                    
                } else {
                    dto.setStock(100); // No toma encuenta el stock nunca pone 0
                }                
                if ( rs.getString("pro_estado").compareTo("N") == 0 
                        || rs.getString("pre_valor") == null
                        || rs.getString("pro_estado").compareTo("A") != 0 ) {
                    dto.setStock(0);
                }
                
                lista.add(dto);
            }
        } catch (SQLException ex) {
            logger.error("getCarroCompra - Problema SQL", ex);
            throw new ClientesDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCarroCompra - Problema General", ex);
            throw new ClientesDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
				if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCarroCompra - Problema SQL (close)", e);
            }
        }
        return lista;
    }
}
