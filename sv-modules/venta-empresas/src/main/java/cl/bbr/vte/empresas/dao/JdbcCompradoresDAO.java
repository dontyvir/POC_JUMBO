package cl.bbr.vte.empresas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CompradoresEntity;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresDAOException;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcCompradoresDAO implements CompradoresDAO{
	
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
	 * @throws CompradoresDAOException
	 */
	public void setTrx(JdbcTransaccion trx)	throws CompradoresDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CompradoresDAOException(e);
		}
	}
	
	//Constructor
	public JdbcCompradoresDAO(){
		
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getCompradoresByRut(long)
	 */
	public CompradoresEntity getCompradoresByRut(long rut) throws CompradoresDAOException{
		CompradoresEntity comprador = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		logger.debug("en getCompradoresByRut:");
		
		try {

			conn = this.getConnection();
			String sql = "SELECT cpr_id, cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat, cpr_ape_mat, " +
			" cpr_genero, cpr_fono1, cpr_fono2, cpr_fono3,  cpr_email, cpr_estado, cpr_pass, " +
			" cpr_pregunta, cpr_respuesta, cpr_intentos, cpr_fec_login " +
			" FROM ve_comprador WHERE cpr_rut = ?";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,rut);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				comprador = new CompradoresEntity();
				comprador.setCpr_id(rs.getLong("cpr_id"));
				comprador.setCpr_rut(rs.getLong("cpr_rut"));
				comprador.setCpr_dv(rs.getString("cpr_dv"));
				comprador.setCpr_nombres(rs.getString("cpr_nombres"));
				comprador.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				comprador.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				comprador.setCpr_genero(rs.getString("cpr_genero"));
				comprador.setCpr_fono1(rs.getString("cpr_fono1"));
				comprador.setCpr_fono2(rs.getString("cpr_fono2"));
				comprador.setCpr_fono3(rs.getString("cpr_fono3"));
				comprador.setCpr_email(rs.getString("cpr_email"));
				comprador.setCpr_estado(rs.getString("cpr_estado"));
				comprador.setCpr_pass(rs.getString("cpr_pass"));
				comprador.setCpr_pregunta(rs.getString("cpr_pregunta"));
				comprador.setCpr_respuesta(rs.getString("cpr_respuesta"));
				comprador.setCpr_intentos(rs.getLong("cpr_intentos"));
				comprador.setCpr_fec_login(rs.getTimestamp("cpr_fec_login"));
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCompradoresByRut - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return comprador;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#insComprador(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public long setCreaComprador(CompradoresDTO dto) throws CompradoresDAOException {
		long id = 0;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en setCreaComprador:");
		
		try{
				String sql2 =" INSERT INTO ve_comprador (cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat, cpr_ape_mat, " +
						" cpr_genero, cpr_fono1, cpr_fono2, cpr_fono3, cpr_email, cpr_estado, cpr_pass, " +
						" cpr_pregunta, cpr_respuesta, cpr_fec_crea)" +
						" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT TIMESTAMP)";
				logger.debug("sql:"+sql2);
				logger.debug("nombre:"+dto.getCpr_nombres());
				logger.debug("estado:"+dto.getCpr_estado());
				
				conn = this.getConnection();
				stm = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
				stm.setLong(1,dto.getCpr_rut());
				stm.setString(2,Utils.stringToDb(String.valueOf(dto.getCpr_dv()),1));
				stm.setString(3,Utils.stringToDb(String.valueOf(dto.getCpr_nombres()), 50));
				stm.setString(4,Utils.stringToDb(String.valueOf(dto.getCpr_ape_pat()), 50));
				stm.setString(5,Utils.stringToDb(String.valueOf(dto.getCpr_ape_mat()), 50));
				stm.setString(6,Utils.stringToDb(String.valueOf(dto.getCpr_genero()), 1));
				stm.setString(7,Utils.stringToDb(String.valueOf(dto.getCpr_fono1()), 20));
				stm.setString(8,Utils.stringToDb(String.valueOf(dto.getCpr_fono2()), 20));
				stm.setString(9,Utils.stringToDb(String.valueOf(dto.getCpr_fono3()), 20));
				stm.setString(10,Utils.stringToDb(String.valueOf(dto.getCpr_email()), 100));
				stm.setString(11,Utils.stringToDb(String.valueOf(dto.getCpr_estado()), 1));
				stm.setString(12,dto.getCpr_pass());
				stm.setString(13,Utils.stringToDb(String.valueOf(dto.getCpr_pregunta()), 200));
				stm.setString(14,Utils.stringToDb(String.valueOf(dto.getCpr_respuesta()), 200));
				
				stm.executeUpdate();
				
				rs = stm.getGeneratedKeys();
				if (rs.next()) {
		            id = rs.getInt(1);
		            logger.debug("id:"+id);
		        }
				
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setCreaComprador - Problema SQL (close)", e);
			}
		}

		return id;
	}	

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#modComprador(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean setModComprador(CompradoresDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en modComprador:");
		try{
			String sql ="";
			if((dto.getCpr_pass()!=null) && !(dto.getCpr_pass().equals("")) ){
				sql = " UPDATE ve_comprador " +
				" SET cpr_nombres = ?, cpr_ape_pat = ?, cpr_ape_mat = ?, cpr_genero = ?, " +
				" cpr_fono1 = ?, cpr_fono2 = ?, cpr_fono3 = ?, cpr_email = ?, " + 
				" cpr_estado = ?, cpr_pass = ?, cpr_pregunta = ?, cpr_respuesta = ?, cpr_rut = ?, cpr_dv = ?, cpr_fmod = CURRENT TIMESTAMP " +
				" WHERE cpr_id = ? ";
			}else{
				sql = " UPDATE ve_comprador " +
				" SET cpr_nombres = ?, cpr_ape_pat = ?, cpr_ape_mat = ?, cpr_genero = ?, " +
				" cpr_fono1 = ?, cpr_fono2 = ?, cpr_fono3 = ?, cpr_email = ?, " + 
				" cpr_estado = ?, cpr_pregunta = ?, cpr_respuesta = ?, cpr_rut = ?, cpr_dv = ?, cpr_fmod = CURRENT TIMESTAMP " +
				" WHERE cpr_id = ? ";
			}
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getCpr_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setString(1,dto.getCpr_nombres());
			logger.debug("nombres:"+dto.getCpr_nombres());
			stm.setString(2,dto.getCpr_ape_pat());
			logger.debug("paterno:"+dto.getCpr_ape_pat());
			stm.setString(3,dto.getCpr_ape_mat());
			logger.debug("materno:"+dto.getCpr_ape_mat());
			stm.setString(4,Utils.stringToDb(String.valueOf(dto.getCpr_genero()), 1));
			logger.debug("genero:"+Utils.stringToDb(String.valueOf(dto.getCpr_genero()), 1));
			stm.setString(5,Utils.stringToDb(String.valueOf(dto.getCpr_fono1()), 20));
			stm.setString(6,Utils.stringToDb(String.valueOf(dto.getCpr_fono2()), 20));
			stm.setString(7,Utils.stringToDb(String.valueOf(dto.getCpr_fono3()), 20));
			stm.setString(8,Utils.stringToDb(String.valueOf(dto.getCpr_email()), 100));
			stm.setString(9,Utils.stringToDb(String.valueOf(dto.getCpr_estado()), 1));
			logger.debug("estado:"+Utils.stringToDb(String.valueOf(dto.getCpr_estado()), 1));
			
			if( (dto.getCpr_pass()!=null)&& !(dto.getCpr_pass().equals("")) ){
				stm.setString(10,dto.getCpr_pass());
				stm.setString(11,Utils.stringToDb(String.valueOf(dto.getCpr_pregunta()), 200));
				stm.setString(12,Utils.stringToDb(String.valueOf(dto.getCpr_respuesta()), 200));
				stm.setLong(13, dto.getCpr_rut());
				stm.setString(14, dto.getCpr_dv());
				stm.setLong(15,dto.getCpr_id());
				
			}else{
				stm.setString(10,Utils.stringToDb(String.valueOf(dto.getCpr_pregunta()), 200));
				stm.setString(11,Utils.stringToDb(String.valueOf(dto.getCpr_respuesta()), 200));
				stm.setLong(12, dto.getCpr_rut());
				stm.setString(13, dto.getCpr_dv());
				stm.setLong(14,dto.getCpr_id());
			}

			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModComprador - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#delComprador(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean delComprador(CompradoresDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delComprador:");
		
		try{
			String sql =" UPDATE ve_comprador set cpr_estado = 'E' WHERE cpr_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getCpr_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getCpr_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delComprador - Problema SQL (close)", e);
			}
		}

		return result;
	}	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getCompradoresById(long)
	 */
	public CompradoresEntity getCompradoresById(long comprador_id) throws CompradoresDAOException {
		CompradoresEntity comprador = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getCompradoresById()...");
			
			conn = this.getConnection();
			String sql = "SELECT cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat,cpr_ape_mat, cpr_genero, " +
					" cpr_fono1, cpr_fono2, cpr_fono3, cpr_email, cpr_estado, " +
					" cpr_pass, cpr_pregunta, cpr_respuesta, cpr_fec_crea " +
					" FROM ve_comprador where cpr_id = ? ";
			
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,comprador_id);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				comprador = new CompradoresEntity();
				logger.debug("nombre= "+rs.getString("cpr_nombres"));
				logger.debug("paterno = "+rs.getString("cpr_ape_pat"));
				logger.debug("materno = "+rs.getString("cpr_ape_mat"));
				comprador.setCpr_rut(rs.getLong("cpr_rut"));
				comprador.setCpr_dv(rs.getString("cpr_dv"));
				comprador.setCpr_nombres(rs.getString("cpr_nombres"));
				comprador.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				comprador.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				comprador.setCpr_genero(rs.getString("cpr_genero"));
				comprador.setCpr_fono1(rs.getString("cpr_fono1"));
				comprador.setCpr_fono2(rs.getString("cpr_fono2"));
				comprador.setCpr_fono3(rs.getString("cpr_fono3"));
				comprador.setCpr_email(rs.getString("cpr_email"));
				comprador.setCpr_estado(rs.getString("cpr_estado"));
				comprador.setCpr_pass(rs.getString("cpr_pass"));
				comprador.setCpr_pregunta(rs.getString("cpr_pregunta"));
				comprador.setCpr_respuesta(rs.getString("cpr_respuesta"));
				comprador.setCpr_fec_crea(rs.getTimestamp("cpr_fec_crea"));
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCompradoresById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return comprador;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListCompradoresBySucursalId(long)
	 */
	public List getListCompradoresBySucursalId(long id_sucursal) throws CompradoresDAOException {
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			logger.debug("En getListCompradoresBySucursalId()...");
			conn = this.getConnection();
			String sql = "SELECT C.cpr_id as cpr_id, cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat, cpr_ape_mat " +
					" FROM ve_comprador C " +
					" JOIN ve_comprxsuc CS ON CS.cpr_id = C.cpr_id " +
					" WHERE CS.cli_id = ? AND C.cpr_estado <> 'E'  ";
			
			logger.debug("id sucursal:"+id_sucursal);
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_sucursal);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			CompradoresEntity com = null;
			while (rs.next()) {
				com = new CompradoresEntity();
				com.setCpr_id(rs.getLong("cpr_id"));
				com.setCpr_rut(rs.getLong("cpr_rut"));
				com.setCpr_dv(rs.getString("cpr_dv"));
				com.setCpr_nombres(rs.getString("cpr_nombres"));
				com.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				com.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				result.add(com);
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListCompradoresBySucursalId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListCompradoresBySucursalId(long, String, long)
	 */
	public List getListCompradoresBySucursalId(long id_sucursal, String TipoAcceso, long comprador_id) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListCompradoresBySucursalId()...");
			conn = this.getConnection();
            
			String param = "";
			if (TipoAcceso.equals("COMPRADOR")){
			    param = "  AND CS.CPR_ID = " + comprador_id;
			}
			String sql = "SELECT C.CPR_ID AS CPR_ID, CPR_RUT, CPR_DV, " 
                       + "       CPR_NOMBRES, CPR_APE_PAT, CPR_APE_MAT "
                       + "FROM FODBA.VE_COMPRADOR C "
                       + "     JOIN FODBA.VE_COMPRXSUC CS ON CS.CPR_ID = C.CPR_ID "
                       + "WHERE CS.CLI_ID = " + id_sucursal + " "
                       + param
                       + "  AND C.CPR_ESTADO <> 'E' ";
			
			logger.debug("id sucursal    : " + id_sucursal);
			logger.debug("id TipoAcceso  : " + TipoAcceso);
			logger.debug("id comprador_id: " + comprador_id);
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			CompradoresEntity com = null;
			while (rs.next()) {
				com = new CompradoresEntity();
				com.setCpr_id(rs.getLong("cpr_id"));
				com.setCpr_rut(rs.getLong("cpr_rut"));
				com.setCpr_dv(rs.getString("cpr_dv"));
				com.setCpr_nombres(rs.getString("cpr_nombres"));
				com.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				com.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				result.add(com);
			}
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListCompradoresBySucursalId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getCompradoresByCriteria(cl.bbr.vte.empresas.dto.CompradorCriteriaDTO)
	 */
	public List getCompradoresByCriteria(CompradorCriteriaDTO criterio) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getCompradoresByCriteria...");
			//verifica los criterios de busqueda
			//id de empresa
			long id_empresa = criterio.getId_empresa();
			logger.debug("id_empresa: "+id_empresa);
			long id_sucursal = criterio.getId_sucursal();
			logger.debug("id_sucursal: "+id_sucursal);
			String sqlId_emp_suc = "";
			String sqlJoin_suc = "";
			String sqlRut_Apellido = "";
			if( (id_empresa>0) && (id_sucursal>0) ){
				sqlJoin_suc = " JOIN ve_comprxsuc CS ON CS.cpr_id = C.cpr_id ";
				sqlId_emp_suc = " AND CS.cli_id = "+id_sucursal;
			}
			String rut = criterio.getRut();
			String apellido = criterio.getApellido();
			
			if (rut != null && !rut.equals("")){
				sqlRut_Apellido = " AND cpr_rut = " + rut;
			}
			if (apellido != null && !apellido.equals("")){
				sqlRut_Apellido = " AND UPPER(cpr_ape_pat) LIKE '%" + apellido.toUpperCase() 
					+ "%' OR  UPPER(cpr_ape_mat) LIKE '%"+apellido.toUpperCase()+"%' ";
			}
			//variables de paginacion
			int pag = criterio.getPag();
			int regXpag = criterio.getRegsperpage();
			if(pag<0) pag = 1;
			if(regXpag<10) regXpag = 10;
			int iniReg = (pag-1)*regXpag + 1;
			int finReg = pag*regXpag;
			
			
			String sql = " SELECT * FROM ( "+
					" SELECT row_number() over(order by C.cpr_id) as row, " +
					" C.cpr_id as cpr_id, cpr_rut, cpr_dv, cpr_nombres, cpr_ape_pat, cpr_ape_mat " +
					" FROM ve_comprador C " + sqlJoin_suc +
					" WHERE C.cpr_id >0 "+sqlId_emp_suc + sqlRut_Apellido+
					") AS TEMP " +
					"WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			CompradoresEntity com = null;
			while (rs.next()) {
				com = new CompradoresEntity();
				com.setCpr_id(rs.getLong("cpr_id"));
				com.setCpr_rut(rs.getLong("cpr_rut"));
				com.setCpr_dv(rs.getString("cpr_dv"));
				com.setCpr_nombres(rs.getString("cpr_nombres"));
				com.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				com.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				result.add(com);
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCompradoresByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getCompradoresCountByCriteria(cl.bbr.vte.empresas.dto.CompradorCriteriaDTO)
	 */
	public int getCompradoresCountByCriteria(CompradorCriteriaDTO criterio) throws CompradoresDAOException {
		int result =0;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getCompradoresCountByCriteria...");
			//verifica los criterios de busqueda
			//id de empresa
			long id_empresa = criterio.getId_empresa();
			logger.debug("id_empresa: "+id_empresa);
			long id_sucursal = criterio.getId_sucursal();
			logger.debug("id_sucursal: "+id_sucursal);
			String sqlId_emp_suc = "";
			String sqlJoin_suc = "";
			String  sqlRut_Apellido="";
			if( (id_empresa>0) && (id_sucursal>0) ){
				sqlJoin_suc = " JOIN ve_comprxsuc CS ON CS.cpr_id = C.cpr_id ";
				sqlId_emp_suc = " AND CS.cli_id = "+id_sucursal;
			}
			
			String rut = criterio.getRut();
			String apellido = criterio.getApellido();
			
			if (rut != null && !rut.equals("")){
				sqlRut_Apellido = " AND cpr_rut = " + rut;
			}
			if (apellido != null && !apellido.equals("")){
				sqlRut_Apellido = " AND cpr_ape_pat LIKE '%" + apellido + "%' OR  cpr_ape_mat LIKE '%"+apellido+"%' ";
			}
			
			String sql = " SELECT count(C.cpr_id) as cantidad " +
					" FROM ve_comprador C " + sqlJoin_suc +
					" WHERE C.cpr_id >0 "+sqlId_emp_suc + sqlRut_Apellido;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cantidad");
			}
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCompradoresCountByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#setRelCompradorSucursal(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean setRelCompradorSucursal(CompradoresDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setRelCompradorSucursal:");
		
		try{
			
			
				String sql2 =" INSERT INTO ve_comprxsuc (cli_id, cpr_id )" +
						" VALUES (?,?)";
				logger.debug("sql:"+sql2+" id suc = "+dto.getId_sucursal()+" id comp= "+dto.getCpr_id());

				conn = this.getConnection();
				stm = conn.prepareStatement(sql2);
				stm.setLong(1,dto.getId_sucursal());
				stm.setLong(2,dto.getCpr_id());
				
				int i= stm.executeUpdate();
				logger.debug("i:"+i);
				if(i>0){
					result=true;
				}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setRelCompradorSucursal - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesByCompradorId(long)
	 */
	/*public List getListSucursalesByCompradorId(long id_comprador) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListSucursalesByCompradorId(long)...");
			conn = this.getConnection();
			String sql = "SELECT  CS.cli_id cli_id, CS.cpr_id cpr_id, S.cli_nombre nom_sucursal, " +
					" E.emp_id emp_id, E.emp_nom nom_empresa " +
					" FROM    ve_comprxsuc CS " +
					" JOIN    ve_comprador C ON C.cpr_id = CS.cpr_id " +
					" JOIN    fo_clientes  S ON S.cli_id = CS.cli_id " +
					" JOIN    ve_empresa   E ON E.emp_id = S.cli_emp_id " +
					" WHERE   CS.cpr_id = ? AND S.cli_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			ComprXSucDTO dto = null;
			while (rs.next()) {
				dto = new ComprXSucDTO();
				dto.setId_sucursal(rs.getLong("cli_id"));
				dto.setId_comprador(rs.getLong("cpr_id"));
				dto.setId_empresa(rs.getLong("emp_id"));
				dto.setNom_sucursal(rs.getString("nom_sucursal"));
				dto.setNom_empresa(rs.getString("nom_empresa"));
				result.add(dto);
			}
			rs.close();
			stm.close();
			releaseConnection();
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
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
	}*/

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesByCompradorId(long,long)
	 */
	public List getListSucursalesByCompradorId(long id_comprador, long id_administrador) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListSucursalesByCompradorId(long, long)...");
			conn = this.getConnection();
			String sql = "SELECT  CS.cli_id cli_id, CS.cpr_id cpr_id, S.cli_nombre nom_sucursal, " +
					" E.emp_id emp_id, E.emp_nom nom_empresa " +
					" FROM    ve_comprxsuc CS " +
					" JOIN    ve_comprador C ON C.cpr_id = CS.cpr_id " +
					" JOIN    fo_clientes  S ON S.cli_id = CS.cli_id " +
					" JOIN    ve_empresa   E ON E.emp_id = S.cli_emp_id " +
					" JOIN    ve_compxemp  A ON A.emp_id = E.emp_id and A.cpr_id = ? " +
					" WHERE   CS.cpr_id = ? AND S.cli_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_administrador);
			stm.setLong(2,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			ComprXSucDTO dto = null;
			while (rs.next()) {
				dto = new ComprXSucDTO();
				dto.setId_sucursal(rs.getLong("cli_id"));
				dto.setId_comprador(rs.getLong("cpr_id"));
				dto.setId_empresa(rs.getLong("emp_id"));
				dto.setNom_sucursal(rs.getString("nom_sucursal"));
				dto.setNom_empresa(rs.getString("nom_empresa"));
				result.add(dto);
			}
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListSucursalesByCompradorId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#addRelSucursalComprador(cl.bbr.vte.empresas.dto.ComprXSucDTO)
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en addRelSucursalComprador...");
		try{
			String sql =" INSERT INTO ve_comprxsuc (cli_id, cpr_id) values (?,?) ";
			logger.debug("sql:"+sql);
			logger.debug("id_suc: "+dto.getId_sucursal());
			logger.debug("id_comp: "+dto.getId_comprador());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,dto.getId_sucursal());
			stm.setLong(2,dto.getId_comprador());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addRelSucursalComprador - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#delRelSucursalComprador(cl.bbr.vte.empresas.dto.ComprXSucDTO)
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delRelSucursalComprador...");
		try{
			String sql =" DELETE FROM ve_comprxsuc WHERE cli_id = ? AND cpr_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id_suc: "+dto.getId_sucursal());
			logger.debug("id_comp: "+dto.getId_comprador());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,dto.getId_sucursal());
			stm.setLong(2,dto.getId_comprador());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delRelSucursalComprador - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesNoAsocComprador(long)
	 */
	public List getListSucursalesNoAsocComprador(long id_administrador, long id_comprador) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListSucursalesNoAsocComprador()...");
			conn = this.getConnection();
			String sql = " select distinct suc.cli_emp_id, suc.cli_nombre, suc.cli_id " +
					" from ve_compxemp as coem " +
					" join fo_clientes as suc on coem.emp_id = suc.cli_emp_id " +
					" where coem.cpr_id = ? and suc.cli_estado = 'A' " +
					" and suc.cli_id NOT IN ( SELECT cli_id FROM ve_comprxsuc CS WHERE CS.cpr_id = ?) " +
					" order by suc.cli_emp_id, suc.cli_nombre";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_administrador);
			stm.setLong(2,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			SucursalesDTO ent = null;
			while (rs.next()) {
				ent = new SucursalesDTO();
				ent.setSuc_id(rs.getLong("cli_id"));
				ent.setSuc_emp_id(rs.getLong("cli_emp_id"));
				ent.setSuc_nombre(rs.getString("cli_nombre"));
				result.add(ent);
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListSucursalesNoAsocComprador - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getComprXSuc(long, long)
	 */
	public String getComprXSuc(long id_comprador, long id_sucursal) throws CompradoresDAOException {
		String result = "";
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getComprXSuc()...");
			conn = this.getConnection();
			String sql = "SELECT  cpr_tipo " +
					" FROM    ve_comprxsuc " +
					" WHERE   cpr_id = ? AND cli_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comprador);
			stm.setLong(2,id_sucursal);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getString("cpr_tipo");
			}
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComprXSuc - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListempresasByCompradorId(long)
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador) throws CompradoresDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListAdmEmpresasByCompradorId()...");
			conn = this.getConnection();

			String sql = " SELECT emp.emp_id, emp.emp_rut, emp.emp_dv, emp.emp_nom, emp.emp_descr, emp.emp_rzsocial, emp.emp_saldo" +
					" FROM ve_empresa as emp " +
					" JOIN ve_compxemp as coem ON emp.emp_id = coem.emp_id and coem.cpr_id = ? " +
					" WHERE emp.emp_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			EmpresasDTO dto = null;
			while (rs.next()) {
				dto = new EmpresasDTO();
				dto.setEmp_id(rs.getLong("emp_id"));
				dto.setEmp_rut(rs.getLong("emp_rut"));
				dto.setEmp_dv(rs.getString("emp_dv"));
				dto.setEmp_nom(rs.getString("emp_nom"));
				dto.setEmp_descr(rs.getString("emp_descr"));
				dto.setEmp_rzsocial(rs.getString("emp_rzsocial"));
				dto.setEmp_saldo(rs.getDouble("emp_saldo"));
				result.add(dto);
			}
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListAdmEmpresasByCompradorId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
		
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListAdmSucursalesByCompradorId(long)
	 */
	public List getListAdmSucursalesByCompradorId(long id_comprador) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListAdmSucursalesByCompradorId()...");
			conn = this.getConnection();

			String sql = "select distinct suc.cli_emp_id, suc.cli_nombre, suc.cli_id " +
			" from ve_compxemp as coem " +
			" join fo_clientes as suc on coem.emp_id = suc.cli_emp_id " +
			" where coem.cpr_id = ? and suc.cli_estado = 'A' " +
			" order by suc.cli_emp_id, suc.cli_nombre";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			SucursalesDTO dto = null;
			while (rs.next()) {
				dto = new SucursalesDTO();
				dto.setSuc_id(rs.getLong("cli_id"));
				dto.setSuc_emp_id(rs.getLong("cli_emp_id"));
				dto.setSuc_nombre(rs.getString("cli_nombre"));
				result.add(dto);
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListAdmSucursalesByCompradorId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#setRelCompradorEmpresa(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean setRelCompradorEmpresa(CompradoresDTO dto) throws CompradoresDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setRelCompradorEmpresa:");
		
		try{
				String sql2 =" INSERT INTO ve_compxemp (cpr_id, emp_id) VALUES (?,?)";
				logger.debug("sql:"+sql2);
				
				conn = this.getConnection();
				stm = conn.prepareStatement(sql2);
				stm.setLong(1,dto.getCpr_id());
				stm.setLong(2,dto.getId_empresa());
				logger.debug("cpr_id: " + dto.getCpr_id());
				logger.debug("Id_empresa: " + dto.getId_empresa());
				int i= stm.executeUpdate();
				logger.debug("i:"+i);
				if(i>0){
					result=true;
				}
				
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setRelCompradorEmpresa - Problema SQL (close)", e);
			}
		}

		return result;

	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#delRelCompradorEmpresa(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean delRelCompradorEmpresa(CompradoresDTO dto) throws CompradoresDAOException {

		boolean result = true;
		PreparedStatement stm = null; 
		logger.debug("en delRelCompradorEmpresa...");
		try{
			String sql =" DELETE FROM ve_compxemp WHERE emp_id = ? AND cpr_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id_emp: "+dto.getId_empresa());
			logger.debug("id_comp: "+dto.getCpr_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1,dto.getId_empresa());
			stm.setLong(2,dto.getCpr_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delRelCompradorEmpresa - Problema SQL (close)", e);
			}
		}
		return result;	
	
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListCompradoresByEmpresalId(long)
	 */
	public List getListCompradoresByEmpresalId(long id_empresa)
	throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			logger.debug("En getListCompradoresByEmpresalId()...");
			conn = this.getConnection();
			String sql = "select distinct com.cpr_id, com.cpr_rut, com.cpr_dv, " +
					" com.cpr_nombres, com.cpr_ape_pat, com.cpr_ape_mat, coem.emp_id as tipo" +
					" from ve_comprador as com " +
					" join ve_comprxsuc as cosu on com.cpr_id = cosu.cpr_id " +
					" join fo_clientes as suc on cosu.cli_id = suc.cli_id " +
					" left join ve_compxemp as coem on coem.cpr_id = com.cpr_id and coem.emp_id = suc.cli_emp_id " +
					" where suc.cli_emp_id = ? and com.cpr_estado <> 'E' " +
					" order by com.cpr_nombres ";			
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_empresa);
			logger.debug("SQL query: " + sql);
			logger.debug("cli_emp_id: " + id_empresa);
			
			rs = stm.executeQuery();
			CompradoresDTO com = null;
			while (rs.next()) {
				com = new CompradoresDTO();
				com.setCpr_id(rs.getLong("cpr_id"));
				com.setCpr_rut(rs.getLong("cpr_rut"));
				com.setCpr_dv(rs.getString("cpr_dv"));
				com.setCpr_nombres(rs.getString("cpr_nombres"));
				com.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				com.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				com.setCpr_tipo(rs.getString("tipo"));
				result.add(com);
			}
			
		} catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),
					e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListCompradoresByEmpresalId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return result;
	}

    /* (non-Javadoc)
     * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesByCompradorId(long)
     */
    public List getListSucursalesByCompradorId(long id_comprador) throws CompradoresDAOException {
    	List result = new ArrayList();
    	
    	PreparedStatement stm=null;
    	ResultSet rs=null;
    	
    	try {
    
    		logger.debug("En getListSucursalesByCompradorId(long)...");
    		conn = this.getConnection();
    		String sql = "SELECT  CS.cli_id cli_id, CS.cpr_id cpr_id, S.cli_nombre nom_sucursal, " +
    				" E.emp_id emp_id, E.emp_nom nom_empresa " +
    				" FROM    ve_comprxsuc CS " +
    				" JOIN    ve_comprador C ON C.cpr_id = CS.cpr_id " +
    				" JOIN    fo_clientes  S ON S.cli_id = CS.cli_id " +
    				" JOIN    ve_empresa   E ON E.emp_id = S.cli_emp_id " +
    				" WHERE   CS.cpr_id = ? AND S.cli_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";
    		
    		
    		
    		stm = conn.prepareStatement(sql + " WITH UR");
    		stm.setLong(1,id_comprador);
    		logger.debug("SQL query: " + sql);
    		
    		rs = stm.executeQuery();
    		ComprXSucDTO dto = null;
    		while (rs.next()) {
    			dto = new ComprXSucDTO();
    			dto.setId_sucursal(rs.getLong("cli_id"));
    			dto.setId_comprador(rs.getLong("cpr_id"));
    			dto.setId_empresa(rs.getLong("emp_id"));
    			dto.setNom_sucursal(rs.getString("nom_sucursal"));
    			dto.setNom_empresa(rs.getString("nom_empresa"));
    			result.add(dto);
    		}
    		
    	}catch (SQLException e) {
    		throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListSucursalesByCompradorId - Problema SQL (close)", e);
			}
		}
    	logger.debug("ok");
    
    	return result;
    }


    /* (non-Javadoc)
     * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesByCompradorId(long)
     */
    public List getListSucursalesByUser(long id_comprador) throws CompradoresDAOException {
    	List result = new ArrayList();
    	
    	PreparedStatement stm=null;
    	ResultSet rs=null;
    	
    	try {
    
    		logger.debug("En getListSucursalesByUser(long)...");
    		conn = this.getConnection();
    		String sql = "SELECT DISTINCT (CS.CLI_ID) CLI_ID, CLI.CLI_NOMBRE NOM_SUCURSAL, "
                       + "       EMP.EMP_ID EMP_ID, EMP.EMP_NOM NOM_EMPRESA, 'COMPRADOR' AS TIPO "
                       + "FROM FODBA.VE_COMPRXSUC CS "
                       + "     JOIN FODBA.FO_CLIENTES CLI ON CLI.CLI_ID = CS.CLI_ID "
                       + "     JOIN FODBA.VE_EMPRESA EMP  ON EMP.EMP_ID = CLI.CLI_EMP_ID "
                       + "WHERE CLI.CLI_ID IN (SELECT CLI1.CLI_ID "
                       + "                     FROM FODBA.FO_CLIENTES CLI1 "
                       + "                          JOIN FODBA.VE_COMPRXSUC CS1 ON CS1.CLI_ID = CLI1.CLI_ID "
                       + "                     WHERE CS1.CPR_ID = " + id_comprador + ") "
                       + "  AND CLI.CLI_ID NOT IN (SELECT CLI2.CLI_ID "
                       + "                     FROM FODBA.FO_CLIENTES CLI2 "
                       + "                          JOIN FODBA.VE_COMPXEMP CE1 ON CE1.EMP_ID = CLI2.CLI_EMP_ID "
                       + "                     WHERE CE1.CPR_ID = " + id_comprador + ") "
                       + "  AND CLI.CLI_ESTADO = '"+Constantes.ESTADO_ACTIVADO+"' "
                       + " "
                       + "UNION "
                       + " "
                       + "SELECT DISTINCT (CS.CLI_ID) CLI_ID, CLI.CLI_NOMBRE NOM_SUCURSAL, "
                       + "       EMP.EMP_ID EMP_ID, EMP.EMP_NOM NOM_EMPRESA, 'ADMIN' AS TIPO "
                       + "FROM FODBA.VE_COMPRXSUC CS "
                       + "     JOIN FODBA.FO_CLIENTES CLI ON CLI.CLI_ID = CS.CLI_ID "
                       + "     JOIN FODBA.VE_EMPRESA EMP  ON EMP.EMP_ID = CLI.CLI_EMP_ID "
                       + "WHERE CLI.CLI_ID IN (SELECT CLI2.CLI_ID "
                       + "                     FROM FODBA.FO_CLIENTES CLI2 "
                       + "                          JOIN FODBA.VE_COMPXEMP CE1 ON CE1.EMP_ID = CLI2.CLI_EMP_ID "
                       + "                     WHERE CE1.CPR_ID = " + id_comprador + ") "
                       + "  AND CLI.CLI_ESTADO = '"+Constantes.ESTADO_ACTIVADO+"' "
                       + "ORDER BY EMP_ID, CLI_ID";
    		
    		stm = conn.prepareStatement(sql + " WITH UR");
    		logger.debug("SQL query: " + sql);
    		
    		rs = stm.executeQuery();
    		ComprXSucDTO dto = null;
    		while (rs.next()) {
    			dto = new ComprXSucDTO();
    			dto.setId_sucursal(rs.getLong("CLI_ID"));
    			dto.setId_empresa(rs.getLong("EMP_ID"));
    			dto.setNom_sucursal(rs.getString("NOM_SUCURSAL"));
    			dto.setNom_empresa(rs.getString("NOM_EMPRESA"));
    			dto.setTipo_acceso(rs.getString("TIPO"));
    			result.add(dto);
    		}
    		
    	}catch (SQLException e) {
    		throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListSucursalesByUser - Problema SQL (close)", e);
			}
		}
    	logger.debug("ok");
    
    	return result;
    }

    
    /* (non-Javadoc)
     * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListSucursalesByCompradorId(long)
     */
    public String getListSucursalesByUser2(long id_comprador) throws CompradoresDAOException {
    	String result = "";
    	
    	PreparedStatement stm=null;
    	ResultSet rs=null;
    	
    	try {
    
    		logger.debug("En getListSucursalesByUser2(long)...");
    		conn = this.getConnection();
    		String sql = "SELECT DISTINCT (CS.CLI_ID) CLI_ID "
                       + "FROM FODBA.VE_COMPRXSUC CS "
                       + "     JOIN FODBA.FO_CLIENTES CLI ON CLI.CLI_ID = CS.CLI_ID "
                       + "WHERE (CLI.CLI_ID IN (SELECT CLI1.CLI_ID "
                       + "                      FROM FODBA.FO_CLIENTES CLI1 "
                       + "    		                 JOIN FODBA.VE_COMPRXSUC CS1 ON CS1.CLI_ID = CLI1.CLI_ID "
                       + "    		            WHERE CS1.CPR_ID = " + id_comprador + ") "
                       + "    OR CLI.CLI_ID IN (SELECT CLI2.CLI_ID "
                       + "                      FROM FODBA.FO_CLIENTES CLI2 "
                       + "                           JOIN FODBA.VE_COMPXEMP CE1 ON CE1.EMP_ID = CLI2.CLI_EMP_ID "
                       + "                      WHERE CE1.CPR_ID = " + id_comprador + ")) "
                       + "  AND CLI.CLI_ESTADO = '"+Constantes.ESTADO_ACTIVADO+"' "
                       + "ORDER BY CS.CLI_ID ";
    		
    		stm = conn.prepareStatement(sql + " WITH UR");
    		logger.debug("SQL query: " + sql);
    		
    		rs = stm.executeQuery();
            boolean flag = false;
    		while (rs.next()) {
    		    if (flag){
    		        result += ", " + rs.getString("CLI_ID");
    		    }else{
    		        result += rs.getString("CLI_ID"); 
    		        flag = true;
    		    }
    		    
    		}
   		
    	}catch (SQLException e) {
    		throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListSucursalesByUser2 - Problema SQL (close)", e);
			}
		}
    	logger.debug("ok");
    
    	return result;
    }
    
    /* (sin Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListAdmCompradoresByAdministradorId(long)
	 */
	public List getListAdmCompradoresByAdministradorId(long id_administrador) throws CompradoresDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			logger.debug("En getListAdmCompradoresByAdministradorId()...");
			conn = this.getConnection();
			String sql = "select distinct com.cpr_id, com.cpr_rut, com.cpr_dv, com.cpr_nombres, com.cpr_ape_pat, com.cpr_ape_mat " +
					" from ve_compxemp as coem " +
					" join fo_clientes as suc on coem.emp_id = suc.cli_emp_id " +
					" join ve_comprxsuc as cosu on cosu.cli_id = suc.cli_id " +
					" join ve_comprador as com on com.cpr_id = cosu.cpr_id " +
					" where coem.cpr_id = ? " +
					" order by cpr_nombres ";		
						
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_administrador);
			logger.debug("SQL query: " + sql);
			logger.debug("cpr_id: " + id_administrador);
			
			rs = stm.executeQuery();
			CompradoresDTO com = null;
			while (rs.next()) {
				com = new CompradoresDTO();
				com.setCpr_id(rs.getLong("cpr_id"));
				com.setCpr_rut(rs.getLong("cpr_rut"));
				com.setCpr_dv(rs.getString("cpr_dv"));
				com.setCpr_nombres(rs.getString("cpr_nombres"));
				com.setCpr_ape_pat(rs.getString("cpr_ape_pat"));
				com.setCpr_ape_mat(rs.getString("cpr_ape_mat"));
				result.add(com);
			}
			
		} catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),
					e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListAdmCompradoresByAdministradorId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return result;		
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListEmpresasByCompradorId(long)
	 */
	public List getListEmpresasByCompradorId(long id_comprador) throws CompradoresDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListEmpresasByCompradorId()...");
			conn = this.getConnection();

			String sql =" select distinct(emp.emp_id), emp.emp_nom, emp.emp_saldo, emp.emp_rut, emp.emp_dv, emp.emp_rzsocial from ve_comprxsuc com join fo_clientes " +
						" cli on cli.cli_id = com.cli_id join ve_empresa emp on cli.cli_emp_id = emp.emp_id " +
						" where cpr_id = ? and emp.emp_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			EmpresasDTO dto = null;
			while (rs.next()) {
				dto = new EmpresasDTO();
				dto.setEmp_id(rs.getLong("emp_id"));
				dto.setEmp_nom(rs.getString("emp_nom"));
				dto.setEmp_saldo(rs.getDouble("emp_saldo"));
				dto.setEmp_rut(rs.getLong("emp_rut"));
				dto.setEmp_dv(rs.getString("emp_dv"));
				dto.setEmp_rzsocial(rs.getString("emp_rzsocial"));
				result.add(dto);
			}
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListEmpresasByCompradorId - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
		
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getListEmpresasByCompradorId(long)
	 */
	public List getListEmpresasByUser(long id_comprador) throws CompradoresDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getListEmpresasByUser()...");
			conn = this.getConnection();

			String sql = "SELECT DISTINCT(EMP.EMP_ID) EMP_ID, EMP.EMP_NOM, EMP.EMP_SALDO, EMP.EMP_RUT, EMP.EMP_DV, EMP.EMP_RZSOCIAL "
                       + "FROM FODBA.VE_COMPRXSUC CS, FODBA.VE_COMPXEMP CE, FODBA.VE_EMPRESA EMP "
                       + "WHERE CS.CPR_ID = " + id_comprador + " "
                       + "  AND CE.CPR_ID = " + id_comprador + " "
                       + "  AND (EMP.EMP_ID = CE.EMP_ID OR "
                       + "       EMP.EMP_ID IN (SELECT CLI.CLI_EMP_ID "
                       + "                      FROM FODBA.FO_CLIENTES CLI "
                       + "                      WHERE CLI.CLI_ID = CS.CLI_ID)) "
                       + "  AND EMP.EMP_ESTADO = '"+Constantes.ESTADO_ACTIVADO+"' "
                       + "UNION "
                       + "SELECT DISTINCT(EMP.EMP_ID) EMP_ID, EMP.EMP_NOM, EMP.EMP_SALDO, EMP.EMP_RUT, EMP.EMP_DV, EMP.EMP_RZSOCIAL " 
                       + "FROM FODBA.VE_COMPRXSUC CS, FODBA.VE_COMPXEMP CE, FODBA.VE_EMPRESA EMP "
                       + "WHERE CS.CPR_ID = " + id_comprador + " "
                       + "  AND EMP.EMP_ID IN (SELECT CLI.CLI_EMP_ID "
                       + "                     FROM FODBA.FO_CLIENTES CLI "
                       + "                     WHERE CLI.CLI_ID = CS.CLI_ID) "
                       + "  AND EMP.EMP_ESTADO = '"+Constantes.ESTADO_ACTIVADO+"' ";
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			EmpresasDTO dto = null;
			while (rs.next()) {
				dto = new EmpresasDTO();
				dto.setEmp_id(rs.getLong("emp_id"));
				dto.setEmp_nom(rs.getString("emp_nom"));
				dto.setEmp_saldo(rs.getDouble("emp_saldo"));
				dto.setEmp_rut(rs.getLong("emp_rut"));
				dto.setEmp_dv(rs.getString("emp_dv"));
				dto.setEmp_rzsocial(rs.getString("emp_rzsocial"));
				result.add(dto);
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListEmpresasByUser - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
		
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#getEjecutivoGetByRut(java.lang.String)
	 */
	public UserDTO getEjecutivoGetByRut(String login ) throws CompradoresDAOException {
		UserDTO usuario = null;
		PreparedStatement stm=null;
		ResultSet rs = null;
		// Carga properties
		ResourceBundle rb = ResourceBundle.getBundle("vte");

		try {
			conn = this.getConnection();
			
			logger.debug("en ejecutivoGetByRut...");
			
			String sql = "select bo_usuarios.id_usuario, id_local, login, pass, nombre, apellido, apellido_mat, email " +
					"from bo_usuarios join bo_usuxperf on bo_usuarios.id_usuario = bo_usuxperf.id_usuario " +
					"where upper(login) = ? " +
					"and estado = 'A' " +
					"and id_perfil = ?";

			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, login.toUpperCase() );
			stm.setLong(2, Integer.parseInt(rb.getString("fono.perfil.ejecutivo")) );

			logger.debug("SQL: " + sql );
			logger.debug("login: " + login);
			rs = stm.executeQuery();
			while (rs.next()) {
				this.logger.logData( "getEjecutivoGetByRut", rs);
				usuario = new UserDTO();
				usuario.setId_usuario( rs.getLong("id_usuario") );
				usuario.setPassword( rs.getString("pass") );
			}

		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEjecutivoGetByRut - Problema SQL (close)", e);
			}
		}
		return usuario;	
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#esAdministrador(long, long)
	 */
	public boolean esAdministrador(long id_comprador, long id_sucursal) throws CompradoresDAOException {
		boolean result  =false;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En esAdministrador()...");
			conn = this.getConnection();

			String sql =" SELECT count(*) cantidad " +
					" FROM ve_compxemp ce " +
					" JOIN fo_clientes c ON ce.emp_id = c.cli_emp_id " +
					" WHERE c.cli_id = ? AND ce.cpr_id =?";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_sucursal);
			stm.setLong(2,id_comprador);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			long cantidad = 0;
			while (rs.next()) {
				cantidad = rs.getLong("cantidad");
			}

			if (cantidad > 0 )
				result = true;
			else
				result = false;
			
		}catch (SQLException e) {
			throw new CompradoresDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : esAdministrador - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");

		return result;
	}	
	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#compradorChangePass(cl.bbr.vte.empresas.dto.CompradoresDTO)
	 */
	public boolean compradorChangePass(CompradoresDTO comprador) throws CompradoresDAOException {
		boolean result  =false;
		PreparedStatement stm=null;
		
		try {

			conn = this.getConnection();
			String query = "update ve_comprador set cpr_pass = ?, cpr_estado = ? where cpr_id = ?";
			logger.debug("query:"+query);
			logger.debug("Estado: "+comprador.getCpr_estado());
			logger.debug("Id: "+comprador.getCpr_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(query);
			
			stm.setString(1,comprador.getCpr_pass());
			stm.setString(2,comprador.getCpr_estado());
			stm.setLong(3,comprador.getCpr_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : compradorChangePass - Problema SQL (close)", e);
			}
		}
		return result;	
	}
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.CompradoresDAO#updateIntentos(long, long)
	 */
	public boolean updateIntentos(long comprador_id, long accion) throws CompradoresDAOException {
		boolean result  =false;
		PreparedStatement stm=null;
		
		try {
			conn = this.getConnection();
			if (accion == 1){
				String query = "UPDATE ve_comprador set cpr_fec_login = CURRENT TIMESTAMP, cpr_intentos = cpr_intentos + 1 where cpr_id = ? ";
				logger.debug("query:"+query);
				conn = this.getConnection();
				stm = conn.prepareStatement(query);
				
				stm.setLong(1, comprador_id);
				
			}else{//Se limpia el campo cli_intentos
				String query = "UPDATE ve_comprador set  cpr_intentos = 0  where cpr_id = ? ";
				
				logger.debug("SQL: " + stm.toString());
				conn = this.getConnection();
				stm = conn.prepareStatement(query);
				
				stm.setLong(1, comprador_id);
				
			}			
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CompradoresDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updateIntentos - Problema SQL (close)", e);
			}
		}
		return result;	
	}	
}
