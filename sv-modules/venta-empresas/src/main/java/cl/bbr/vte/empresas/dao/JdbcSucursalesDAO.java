package cl.bbr.vte.empresas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CompradoresEntity;
import cl.bbr.jumbocl.common.model.DirFacturacionEntity;
import cl.bbr.jumbocl.common.model.SucursalesEntity;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;
import cl.bbr.vte.empresas.exceptions.SucursalesDAOException;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcSucursalesDAO implements SucursalesDAO{
	
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
	 * @throws EmpresasDAOException
	 */
	public void setTrx(JdbcTransaccion trx)	throws SucursalesDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new SucursalesDAOException(e);
		}
	}
	
	//Constructor
	public JdbcSucursalesDAO(){
		
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getSucursalById(long)
	 */
	public SucursalesEntity getSucursalById(long id) throws SucursalesDAOException{
		SucursalesEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT cli_id, cli_emp_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " +
					" cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_rec_info, " +
					" cli_fec_crea, cli_estado,  cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, " +
					" cli_bloqueo, cli_mod_dato, cli_fec_login, cli_intentos, cli_tipo " +
					" FROM fo_clientes where cli_id = ? AND cli_tipo = '"+Constantes.TIPO_CLIENTE_SUCURSAL+"'";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				ent = new SucursalesEntity();
				ent.setSuc_id(new Long(rs.getLong("cli_id")));
				ent.setSuc_emp_id(new Long(rs.getLong("cli_emp_id")));
				ent.setSuc_rut(new Long(rs.getLong("cli_rut")));
				ent.setSuc_dv(rs.getString("cli_dv"));
				ent.setSuc_razon(rs.getString("cli_apellido_pat"));
				ent.setSuc_nombre(rs.getString("cli_nombre"));
				ent.setSuc_descr(rs.getString("cli_apellido_mat"));
				ent.setSuc_clave(rs.getString("cli_clave"));
				ent.setSuc_email(rs.getString("cli_email"));
				ent.setSuc_fono_cod1(rs.getString("cli_fon_cod_1"));
				ent.setSuc_fono_num1(rs.getString("cli_fon_num_1"));
				ent.setSuc_fono_cod2(rs.getString("cli_fon_cod_2"));
				ent.setSuc_fono_num2(rs.getString("cli_fon_num_2"));
				ent.setSuc_rec_info(new Integer(rs.getInt("cli_rec_info")));
				ent.setSuc_fec_crea(rs.getTimestamp("cli_fec_crea"));
				ent.setSuc_estado(rs.getString("cli_estado"));
				ent.setSuc_fec_nac(rs.getDate("cli_fec_nac"));
				ent.setSuc_genero(rs.getString("cli_genero"));
				ent.setSuc_fec_act(rs.getTimestamp("cli_fec_act"));
				ent.setSuc_pregunta(rs.getString("cli_pregunta"));
				ent.setSuc_respuesta(rs.getString("cli_respuesta"));
				ent.setSuc_bloqueo(rs.getString("cli_bloqueo"));
				ent.setSuc_mod_dato(rs.getString("cli_mod_dato"));
				ent.setSuc_fec_login(rs.getTimestamp("cli_fec_login"));
				ent.setSuc_intentos(new Integer(rs.getInt("cli_intentos")));
				ent.setSuc_tipo(rs.getString("cli_tipo"));
			}
			
		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSucursalById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return ent;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#insSucursal(cl.bbr.vte.empresas.dto.SucursalesDTO)
	 */
	public long insSucursal(SucursalesDTO dto) throws SucursalesDAOException {
		ResultSet rs = null;
		long id = 0;
		PreparedStatement stm = null; 
		logger.debug("en insSucursal():");
		
		try{
			String sql =" INSERT INTO fo_clientes (cli_emp_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " +
					" cli_fon_num_1, cli_fon_num_2, cli_email, cli_estado, cli_fec_crea, cli_tipo, cli_pregunta, cli_fon_cod_1, cli_fon_cod_2 ) " +
					" VALUES (?,?,?,?,?,?,?,?,?,?,CURRENT TIMESTAMP,'"+Constantes.TIPO_CLIENTE_SUCURSAL+"',?,?,?)";
			logger.debug("sql:"+sql);
			logger.debug("razon:"+dto.getSuc_razon());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, dto.getSuc_emp_id());
			stm.setLong(2,dto.getSuc_rut());
			stm.setString(3,dto.getSuc_dv());
			stm.setString(4,dto.getSuc_nombre());
			stm.setString(5,dto.getSuc_razon());
			stm.setString(6,dto.getSuc_descr());
			stm.setString(7,dto.getSuc_fono_num1());
			stm.setString(8,dto.getSuc_fono_num2());
			stm.setString(9,dto.getSuc_email());
			stm.setString(10,dto.getSuc_estado());
			stm.setString(11,dto.getSuc_pregunta());
			stm.setString(12,dto.getSuc_fono_cod1());
			stm.setString(13,dto.getSuc_fono_cod2());
			
			int i= stm.executeUpdate();
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
	            id = rs.getInt(1);
	            logger.debug("id:"+id);
	        }

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new SucursalesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insSucursal - Problema SQL (close)", e);
			}
		}

		return id;
	}	

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#modSucursal(cl.bbr.vte.empresas.dto.SucursalesDTO)
	 */
	public boolean modSucursal(SucursalesDTO dto) throws SucursalesDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en modSucursal:");
		try{
			String sql =" UPDATE fo_clientes " +
			" SET cli_emp_id = ?, cli_rut = ?, cli_dv = ?, cli_nombre = ?, cli_apellido_pat = ?, cli_apellido_mat = ?, cli_email = ?, " +
			" cli_fon_num_1 = ?, cli_fon_num_2 = ?, cli_fon_cod_1 = ?, cli_fon_cod_2 = ?, cli_estado = ?, cli_fec_act = CURRENT TIMESTAMP, cli_pregunta = ? " +
			" WHERE  cli_id = ?";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getSuc_id());
			logger.debug("razon:"+dto.getSuc_razon());
			logger.debug("descripcion:"+dto.getSuc_descr());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getSuc_emp_id());
			stm.setLong(2,dto.getSuc_rut());
			stm.setString(3,dto.getSuc_dv());
			stm.setString(4,dto.getSuc_nombre());
			stm.setString(5,dto.getSuc_razon());
			stm.setString(6,dto.getSuc_descr());
			stm.setString(7,dto.getSuc_email());
			stm.setString(8,dto.getSuc_fono_num1());
			stm.setString(9,dto.getSuc_fono_num2());
			stm.setString(10,dto.getSuc_fono_cod1());
			stm.setString(11,dto.getSuc_fono_cod2());
			stm.setString(12,dto.getSuc_estado());
			stm.setString(13,dto.getSuc_pregunta());
			stm.setLong(14,dto.getSuc_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new SucursalesDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modSucursal - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#delSucursal(cl.bbr.vte.empresas.dto.SucursalesDTO)
	 */
	public boolean delSucursal(SucursalesDTO dto) throws SucursalesDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delSucursal:");
		
		try{
			String sql =" UPDATE fo_clientes  set cli_estado = 'E' WHERE cli_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getSuc_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getSuc_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new SucursalesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delSucursal - Problema SQL (close)", e);
			}
		}

		return result;
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getCompradoresBySucursalId(long)
	 */
	public List getCompradoresBySucursalId(long id) throws SucursalesDAOException {
		List lst = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		CompradoresEntity ent = null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT C.cpr_id cpr_id, cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat, cpr_ape_mat, cpr_genero,  " +
					" cpr_fono1, cpr_fono2, cpr_fono3, cpr_email, cpr_fmod, cpr_estado, cpr_pass, cpr_fec_crea, " +
					" cpr_pregunta, cpr_respuesta, cpr_bloqueo, cpr_mod_dato, cpr_fec_login, cpr_intentos " +
					" FROM ve_comprador C " +
					" JOIN ve_comprxsuc CS ON CS.cpr_id = C.cpr_id " +
					" WHERE CS.cli_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ent = new CompradoresEntity();
				ent.setCpr_id(rs.getLong("cpr_id"));
				ent.setCpr_rut(rs.getLong("cpr_rut"));
				ent.setCpr_dv(rs.getString("cpr_dv"));
				ent.setCpr_nombres(rs.getString("cpr_nombres"));
				ent.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				ent.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				ent.setCpr_genero(rs.getString("cpr_genero"));
				ent.setCpr_fono1(rs.getString("cpr_fono1"));
				ent.setCpr_fono2(rs.getString("cpr_fono2"));
				ent.setCpr_fono3(rs.getString("cpr_fono3"));
				ent.setCpr_email(rs.getString("cpr_email"));
				ent.setCpr_fmod(rs.getTimestamp("cpr_fmod"));
				ent.setCpr_fmod(rs.getTimestamp("cpr_fmod"));
				ent.setCpr_estado(rs.getString("cpr_estado"));
				ent.setCpr_pass(rs.getString("cpr_pass"));
				ent.setCpr_fec_crea(rs.getTimestamp("cpr_fec_crea"));
				ent.setCpr_pregunta(rs.getString("cpr_pregunta"));
				ent.setCpr_respuesta(rs.getString("cpr_respuesta"));
				ent.setCpr_bloqueo(rs.getString("cpr_bloqueo"));
				ent.setCpr_mod_dato(rs.getString("cpr_mod_dato"));
				ent.setCpr_fec_login(rs.getTimestamp("cpr_fec_login"));
				ent.setCpr_intentos(rs.getLong("cpr_intentos"));
				lst.add(ent);
			}

		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCompradoresBySucursalId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, lst:"+lst.size());
		
		return lst;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getDirsFacturacionBySucursalId(long)
	 */
	public List getDirsFacturacionBySucursalId(long id) throws SucursalesDAOException {
		List lst = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		DirFacturacionEntity ent = null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT dfac_id, dfac_tip_id, dfac_cli_id, dfac_com_id, dfac_alias, " +
					" dfac_calle, dfac_numero, dfac_depto, dfac_comentarios, dfac_estado, dfac_ciudad, dfac_fax, " +
					" dfac_nom_contacto, dfac_cargo, dfac_email, dfac_fono1, dfac_fono2, dfac_fono3 " +
					" FROM ve_dirfact " +
					" WHERE dfac_cli_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ent = new DirFacturacionEntity();
				ent.setDfac_id(new Long(rs.getLong("dfac_id")));
				ent.setDfac_tip_id(new Long(rs.getLong("dfac_tip_id")));
				ent.setDfac_cli_id(new Long(rs.getLong("dfac_cli_id")));
				ent.setDfac_com_id(new Long(rs.getLong("dfac_com_id")));
				ent.setDfac_alias(rs.getString("dfac_alias"));
				ent.setDfac_calle(rs.getString("dfac_calle"));
				ent.setDfac_numero(rs.getString("dfac_numero"));
				ent.setDfac_depto(rs.getString("dfac_depto"));
				ent.setDfac_comentarios(rs.getString("dfac_comentarios"));
				ent.setDfac_estado(rs.getString("dfac_estado"));
				ent.setDfac_ciudad(rs.getString("dfac_ciudad"));
				ent.setDfac_fax(rs.getString("dfac_fax"));
				ent.setDfac_nom_contacto(rs.getString("dfac_nom_contacto"));
				ent.setDfac_cargo(rs.getString("dfac_cargo"));
				ent.setDfac_email(rs.getString("dfac_email"));
				ent.setDfac_fono1(rs.getString("dfac_fono1"));
				ent.setDfac_fono2(rs.getString("dfac_fono2"));
				ent.setDfac_fono3(rs.getString("dfac_fono3"));
				lst.add(ent);
			}
			
		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDirsFacturacionBySucursalId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, lst:"+lst.size());
		
		return lst;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getSucursalesByCriteria(cl.bbr.vte.empresas.dto.SucursalCriteriaDTO)
	 */
	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio) throws SucursalesDAOException {
		List lst_suc = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getSucursalesByCriteria...");
			//verifica los criterios de busqueda
			//id de empresa
			long idEmpresa = criterio.getId_empresa();
			logger.debug("idEmpresa	:"+idEmpresa);
			String sqlId_empresa = "";
			if(idEmpresa>0)
				sqlId_empresa = " AND cli_emp_id = "+idEmpresa;
			
			//list de empresas
			if(criterio.getListempresas() != null)
				sqlId_empresa = " AND cli_emp_id in ("+criterio.getListempresas()+")";
			
			//rut de sucursal
			String rut = criterio.getRut();
			logger.debug("rut	:"+rut);
			String sqlRut = "";
			if( (rut != null) && (!rut.equals("")) )
				sqlRut = " AND cli_rut = " + rut;
			//razon social
			String razon_social = criterio.getRazon_social();
			logger.debug("razon_social	:"+razon_social);
			String sqlRazSoc = "";
			if ((razon_social != null) && (!razon_social.equals("")) ){
				sqlRazSoc = " AND (upper(cli_apellido_pat) like '%"+razon_social.toUpperCase()+"%') ";
			}
			//nombre, que buscara en descripcion
			String nombre = criterio.getNombre();
			logger.debug("nombre	:"+nombre);
			String sqlDescrip = "";
			if ((nombre != null) && (!nombre.equals("")) ){
				sqlDescrip = " AND (upper(cli_nombre) like '%"+nombre.toUpperCase()+"%') ";
			}
			//estado
			String estado = criterio.getEstado();
			logger.debug("estado	:"+estado);
			String sqlEstado="";
			if( (estado != null) && (!estado.equals("")) )
				sqlEstado = " AND cli_estado = '" + estado+"' ";
			
			//variables de paginacion
			int pag = criterio.getPag();
			int regXpag = criterio.getRegsperpage();
			if(pag<0) pag = 1;
			if(regXpag<10) regXpag = 10;
			int iniReg = (pag-1)*regXpag + 1;
			int finReg = pag*regXpag;

			String sql = " SELECT * FROM ( " +
					" SELECT row_number() over(order by cli_id) as row, " +
					" cli_id, cli_emp_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " +
					" cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_rec_info, " +
					" cli_fec_crea, cli_estado,  cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, " +
					" cli_bloqueo, cli_mod_dato, cli_fec_login, cli_intentos, cli_tipo , estado, emp_nom  FROM " +
					" fo_clientes left join fo_estados on cli_estado = id and tipo = ? left join ve_empresa on cli_emp_id = emp_id " +
					" where cli_id > 0 AND " +
					" cli_tipo = '"+Constantes.TIPO_CLIENTE_SUCURSAL+"' " + sqlId_empresa + sqlRut + 
					sqlRazSoc + sqlDescrip + sqlEstado + ") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1,"SUC");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			SucursalesEntity ent = null;
			while (rs.next()) {
				ent = new SucursalesEntity();
				ent.setSuc_id(new Long(rs.getLong("cli_id")));
				ent.setSuc_emp_id(new Long(rs.getLong("cli_emp_id")));
				ent.setSuc_rut(new Long(rs.getLong("cli_rut")));
				ent.setSuc_dv(rs.getString("cli_dv"));
				ent.setSuc_razon(rs.getString("cli_apellido_pat"));
				ent.setSuc_nombre(rs.getString("cli_nombre"));
				ent.setSuc_descr(rs.getString("cli_apellido_mat"));
				ent.setSuc_clave(rs.getString("cli_clave"));
				ent.setSuc_email(rs.getString("cli_email"));
				ent.setSuc_fono_cod1(rs.getString("cli_fon_cod_1"));
				ent.setSuc_fono_num1(rs.getString("cli_fon_num_1"));
				ent.setSuc_fono_cod2(rs.getString("cli_fon_cod_2"));
				ent.setSuc_fono_num2(rs.getString("cli_fon_num_2"));
				ent.setSuc_rec_info(new Integer(rs.getInt("cli_rec_info")));
				ent.setSuc_fec_crea(rs.getTimestamp("cli_fec_crea"));
				ent.setSuc_estado(rs.getString("cli_estado"));
				ent.setSuc_fec_nac(rs.getDate("cli_fec_nac"));
				ent.setSuc_genero(rs.getString("cli_genero"));
				ent.setSuc_fec_act(rs.getTimestamp("cli_fec_act"));
				ent.setSuc_pregunta(rs.getString("cli_pregunta"));
				ent.setSuc_respuesta(rs.getString("cli_respuesta"));
				ent.setSuc_bloqueo(rs.getString("cli_bloqueo"));
				ent.setSuc_mod_dato(rs.getString("cli_mod_dato"));
				ent.setSuc_fec_login(rs.getTimestamp("cli_fec_login"));
				ent.setSuc_intentos(new Integer(rs.getInt("cli_intentos")));
				ent.setSuc_tipo(rs.getString("cli_tipo"));
				ent.setSuc_desc_estado(rs.getString("estado"));
				ent.setEmp_nom(rs.getString("emp_nom"));
				lst_suc.add(ent);
			}
			
		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSucursalesByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, lst_suc tiene "+lst_suc.size());
		
		return lst_suc;
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getSucursalesCountByCriteria(cl.bbr.vte.empresas.dto.SucursalCriteriaDTO)
	 */
	public int getSucursalesCountByCriteria(SucursalCriteriaDTO criterio) throws SucursalesDAOException {
		int cant = 0;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getSucursalesCountByCriteria...");
			//verifica los criterios de busqueda
			//id de empresa
			long idEmpresa = criterio.getId_empresa();
			logger.debug("idEmpresa"+idEmpresa);
			String sqlId_empresa = "";
			if(idEmpresa>0)
				sqlId_empresa = " AND cli_emp_id = "+idEmpresa;
			//rut de sucursal
			String rut = criterio.getRut();
			logger.debug("rut	:"+rut);
			String sqlRut = "";
			if( (rut != null) && (!rut.equals("")) )
				sqlRut = " AND cli_rut = " + rut;
			//razon social
			String razon_social = criterio.getRazon_social();
			logger.debug("razon_social	:"+razon_social);
			String sqlRazSoc = "";
			if ((razon_social != null) && (!razon_social.equals("")) ){
				sqlRazSoc = " AND (upper(cli_apellido_pat) like '%"+razon_social.toUpperCase()+"%') ";
			}
			//nombre, que buscara en descripcion
			String nombre = criterio.getNombre();
			logger.debug("nombre	:"+nombre);
			String sqlDescrip = "";
			if ((nombre != null) && (!nombre.equals("")) ){
				sqlDescrip = " AND (upper(cli_nombre) like '%"+nombre.toUpperCase()+"%') ";
			}
			//estado
			String estado = criterio.getEstado();
			logger.debug("estado	:"+estado);
			String sqlEstado="";
			if( (estado != null) && (!estado.equals("")) )
				sqlEstado = " AND cli_estado = '" + estado+"' ";
			
			String sql = 
					" SELECT count(cli_id) as cantidad" +
					" FROM fo_clientes where cli_id > 0 AND cli_tipo = '"+Constantes.TIPO_CLIENTE_SUCURSAL+"' "+ sqlId_empresa + sqlRut + sqlRazSoc + sqlDescrip + sqlEstado;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}
			
		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSucursalesCountByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant:"+cant);
		
		return cant;
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getSucursalesByEmpresaId(long)
	 */
	public List getSucursalesByEmpresaId(long id) throws SucursalesDAOException  {
		List result = new ArrayList();
		
		SucursalesEntity ent = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT cli_id, cli_emp_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " +
					" cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_rec_info, " +
					" cli_fec_crea, cli_estado,  cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, " +
					" cli_bloqueo, cli_mod_dato, cli_fec_login, cli_intentos, cli_tipo " +
					" FROM fo_clientes where cli_emp_id = ? and cli_estado='"+Constantes.ESTADO_ACTIVADO+"'";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ent = new SucursalesEntity();
				ent.setSuc_id(new Long(rs.getLong("cli_id")));
				ent.setSuc_emp_id(new Long(rs.getLong("cli_emp_id")));
				ent.setSuc_rut(new Long(rs.getLong("cli_rut")));
				ent.setSuc_dv(rs.getString("cli_dv"));
				ent.setSuc_razon(rs.getString("cli_apellido_pat"));
				ent.setSuc_nombre(rs.getString("cli_nombre"));
				ent.setSuc_descr(rs.getString("cli_apellido_mat"));
				ent.setSuc_clave(rs.getString("cli_clave"));
				ent.setSuc_email(rs.getString("cli_email"));
				ent.setSuc_fono_cod1(rs.getString("cli_fon_cod_1"));
				ent.setSuc_fono_num1(rs.getString("cli_fon_num_1"));
				ent.setSuc_fono_cod2(rs.getString("cli_fon_cod_2"));
				ent.setSuc_fono_num2(rs.getString("cli_fon_num_2"));
				ent.setSuc_rec_info(new Integer(rs.getInt("cli_rec_info")));
				ent.setSuc_fec_crea(rs.getTimestamp("cli_fec_crea"));
				ent.setSuc_estado(rs.getString("cli_estado"));
				ent.setSuc_fec_nac(rs.getDate("cli_fec_nac"));
				ent.setSuc_genero(rs.getString("cli_genero"));
				ent.setSuc_fec_act(rs.getTimestamp("cli_fec_act"));
				ent.setSuc_pregunta(rs.getString("cli_pregunta"));
				ent.setSuc_respuesta(rs.getString("cli_respuesta"));
				ent.setSuc_bloqueo(rs.getString("cli_bloqueo"));
				ent.setSuc_mod_dato(rs.getString("cli_mod_dato"));
				ent.setSuc_fec_login(rs.getTimestamp("cli_fec_login"));
				ent.setSuc_intentos(new Integer(rs.getInt("cli_intentos")));
				ent.setSuc_tipo(rs.getString("cli_tipo"));
				result.add(ent);
			}

		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSucursalesByEmpresaId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.SucursalesDAO#getSucursalByRut(long)
	 */
	public SucursalesEntity getSucursalByRut(long rut) throws SucursalesDAOException{
		SucursalesEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT cli_id, cli_emp_id, cli_rut, cli_dv, cli_nombre, cli_apellido_pat, cli_apellido_mat, " +
					" cli_clave, cli_email, cli_fon_cod_1, cli_fon_num_1, cli_fon_cod_2, cli_fon_num_2, cli_rec_info, " +
					" cli_fec_crea, cli_estado,  cli_fec_nac, cli_genero, cli_fec_act, cli_pregunta, cli_respuesta, " +
					" cli_bloqueo, cli_mod_dato, cli_fec_login, cli_intentos, cli_tipo " +
					" FROM fo_clientes where cli_rut = ? AND cli_tipo = '"+Constantes.TIPO_CLIENTE_SUCURSAL+"'" +
					" and cli_estado = '"+Constantes.ESTADO_ACTIVADO+"'";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,rut);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				ent = new SucursalesEntity();
				ent.setSuc_id(new Long(rs.getLong("cli_id")));
				ent.setSuc_emp_id(new Long(rs.getLong("cli_emp_id")));
				ent.setSuc_rut(new Long(rs.getLong("cli_rut")));
				ent.setSuc_dv(rs.getString("cli_dv"));
				ent.setSuc_razon(rs.getString("cli_apellido_pat"));
				ent.setSuc_nombre(rs.getString("cli_nombre"));
				ent.setSuc_descr(rs.getString("cli_apellido_mat"));
				ent.setSuc_clave(rs.getString("cli_clave"));
				ent.setSuc_email(rs.getString("cli_email"));
				ent.setSuc_fono_cod1(rs.getString("cli_fon_cod_1"));
				ent.setSuc_fono_num1(rs.getString("cli_fon_num_1"));
				ent.setSuc_fono_cod2(rs.getString("cli_fon_cod_2"));
				ent.setSuc_fono_num2(rs.getString("cli_fon_num_2"));
				ent.setSuc_rec_info(new Integer(rs.getInt("cli_rec_info")));
				ent.setSuc_fec_crea(rs.getTimestamp("cli_fec_crea"));
				ent.setSuc_estado(rs.getString("cli_estado"));
				ent.setSuc_fec_nac(rs.getDate("cli_fec_nac"));
				ent.setSuc_genero(rs.getString("cli_genero"));
				ent.setSuc_fec_act(rs.getTimestamp("cli_fec_act"));
				ent.setSuc_pregunta(rs.getString("cli_pregunta"));
				ent.setSuc_respuesta(rs.getString("cli_respuesta"));
				ent.setSuc_bloqueo(rs.getString("cli_bloqueo"));
				ent.setSuc_mod_dato(rs.getString("cli_mod_dato"));
				ent.setSuc_fec_login(rs.getTimestamp("cli_fec_login"));
				ent.setSuc_intentos(new Integer(rs.getInt("cli_intentos")));
				ent.setSuc_tipo(rs.getString("cli_tipo"));
			}
			
		}catch (SQLException e) {
			throw new SucursalesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSucursalByRut - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return ent;
	}

	
}
