package cl.bbr.vte.empresas.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.SucursalesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresaLogDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.EstadoDTO;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcEmpresasDAO implements EmpresasDAO{
	
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
	public void setTrx(JdbcTransaccion trx)	throws EmpresasDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new EmpresasDAOException(e);
		}
	}
	
	//Constructor
	public JdbcEmpresasDAO(){
		
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEmpresaById(long)
	 */
	public EmpresasEntity getEmpresaById(long id) throws EmpresasDAOException{
		EmpresasEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
				" SELECT emp_id, emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, emp_tamano, emp_qtyemp, " +
				" emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, emp_mail_contacto, emp_cargo_contacto, " +
				" emp_saldo, emp_fact_saldo, emp_fmod, emp_estado, emp_mg_min, emp_fec_crea, " +
				" emp_mod_rzsoc, emp_dscto_max, emp_fon_cod_1, emp_fon_cod_2, emp_fon_cod_3 " +
				" FROM ve_empresa WHERE emp_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);
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
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
				ent.setCod_fon1(rs.getString("emp_fon_cod_1"));
				ent.setCod_fon2(rs.getString("emp_fon_cod_2"));
				ent.setCod_fon3(rs.getString("emp_fon_cod_3"));
				
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresaById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return ent;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#setCreaEmpresa(cl.bbr.vte.empresas.dto.EmpresasDTO)
	 */
	public long setCreaEmpresa(EmpresasDTO dto) throws EmpresasDAOException {
		ResultSet rs = null;
		long id = 0;
		PreparedStatement stm = null; 
		logger.debug("en insEmpresa:");
		
		try{
			String sql =" INSERT INTO ve_empresa (emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, " +
					" emp_tamano, emp_qtyemp, emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, " +
					" emp_mail_contacto, emp_cargo_contacto, emp_saldo, emp_fact_saldo, emp_fmod, emp_estado, " +
					" emp_mg_min, emp_fec_crea, emp_mod_rzsoc, emp_dscto_max,  emp_fon_cod_1, emp_fon_cod_2, emp_fon_cod_3)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, CURRENT TIMESTAMP, ?,?, ?,?,?)";
			logger.debug("sql:"+sql);
			logger.debug("nombre:"+dto.getEmp_nom());
			logger.debug("razon:"+dto.getEmp_rzsocial());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1,dto.getEmp_rut());
			stm.setString(2,dto.getEmp_dv());
			stm.setString(3,dto.getEmp_nom());
			stm.setString(4,dto.getEmp_descr());
			stm.setString(5,dto.getEmp_rzsocial());
			stm.setString(6,dto.getEmp_rubro());
			stm.setString(7,dto.getEmp_tamano());
			stm.setLong(8,dto.getEmp_qtyemp());
			stm.setString(9,dto.getEmp_nom_contacto());
			stm.setString(10,dto.getEmp_fono1_contacto());
			stm.setString(11,dto.getEmp_fono2_contacto());
			stm.setString(12,dto.getEmp_fono3_contacto());
			stm.setString(13,dto.getEmp_mail_contacto());
			stm.setString(14,dto.getEmp_cargo_contacto());
			stm.setDouble(15,dto.getEmp_saldo());
			stm.setString(16,dto.getEmp_fact_saldo());
			stm.setString(17, Formatos.getFecHoraActual());
			stm.setString(18,dto.getEmp_estado());
			stm.setDouble(19, dto.getEmp_mrg_minimo());
			stm.setInt(20, Constantes.EMP_NO_MOD_RAZON_SOCIAL);
			stm.setDouble(21, dto.getEmp_dscto_max());

			stm.setString(22, dto.getEmp_cod_fon1());
			stm.setString(23, dto.getEmp_cod_fon2());
			stm.setString(24, dto.getEmp_cod_fon3());
			
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
	            id = rs.getInt(1);
	            logger.debug("id:"+id);
	        }
		} catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new EmpresasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setCreaEmpresa - Problema SQL (close)", e);
			}
		}

		return id;
	}	

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#modEmpresa(cl.bbr.vte.empresas.dto.EmpresasDTO)
	 */
	public boolean modEmpresa(EmpresasDTO dto) throws EmpresasDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en modEmpresa:");
		try{
			String sql =" UPDATE ve_empresa " +
			" SET emp_rut = ?, emp_dv = ?, emp_nom = ?, emp_descr = ?, emp_rzsocial = ?, emp_rubro = ?, emp_tamano = ?, emp_qtyemp = ?, " +
			" emp_nom_contacto = ?, emp_fono1_contacto = ?, emp_fono2_contacto = ?, emp_fono3_contacto = ?, " +
			" emp_mail_contacto = ?, emp_cargo_contacto = ?, " +
			" emp_fmod = CURRENT TIMESTAMP, emp_estado = ?, emp_mg_min = ? , emp_mod_rzsoc = ?, emp_dscto_max = ?, " +
			" emp_fon_cod_1= ?, emp_fon_cod_2 = ?, emp_fon_cod_3 = ? " +
			" WHERE emp_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getEmp_id());
			logger.debug("nombre:"+dto.getEmp_nom());
			logger.debug("razon:"+dto.getEmp_rzsocial());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, dto.getEmp_rut());
			stm.setString(2, dto.getEmp_dv());
			stm.setString(3, dto.getEmp_nom());
			stm.setString(4, dto.getEmp_descr());
			stm.setString(5, dto.getEmp_rzsocial());
			stm.setString(6, dto.getEmp_rubro());
			stm.setString(7, dto.getEmp_tamano());
			stm.setLong(8, dto.getEmp_qtyemp());
			stm.setString(9, dto.getEmp_nom_contacto());
			stm.setString(10, dto.getEmp_fono1_contacto());
			stm.setString(11, dto.getEmp_fono2_contacto());
			stm.setString(12, dto.getEmp_fono3_contacto());
			stm.setString(13, dto.getEmp_mail_contacto());
			stm.setString(14, dto.getEmp_cargo_contacto());
			stm.setString(15, dto.getEmp_estado());
			stm.setDouble(16, dto.getEmp_mrg_minimo());
			stm.setInt(17, dto.getEmp_mod_rzsoc());
			stm.setDouble(18, dto.getEmp_dscto_max());
			stm.setString(19, dto.getEmp_cod_fon1());
			stm.setString(20, dto.getEmp_cod_fon2());
			stm.setString(21, dto.getEmp_cod_fon3());
			stm.setLong(22, dto.getEmp_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new EmpresasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modEmpresa - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#delEmpresa(cl.bbr.vte.empresas.dto.EmpresasDTO)
	 */
	public boolean delEmpresa(EmpresasDTO dto) throws EmpresasDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delEmpresa:");
		
		try{
			String sql =" UPDATE ve_empresa  set emp_estado = 'E', emp_fmod = ? WHERE emp_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getEmp_id());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,Formatos.getFecHoraActual());
			stm.setLong(2,dto.getEmp_id());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new EmpresasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delEmpresa - Problema SQL (close)", e);
			}
		}

		return result;
	}		

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEmpresaByRut(long)
	 */
	public EmpresasEntity getEmpresaByRut(long rut) throws EmpresasDAOException{
		EmpresasEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getEmpresaByRut...");
			conn = this.getConnection();
			String sql = 
				" SELECT emp_id, emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, emp_tamano, emp_qtyemp, " +
				" emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, emp_mail_contacto, emp_cargo_contacto, " +
				" emp_saldo, emp_fact_saldo,  emp_fmod, emp_estado, emp_mg_min, emp_fec_crea, emp_mod_rzsoc, emp_dscto_max " +
				" FROM ve_empresa WHERE emp_rut = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,rut);
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
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresaByRut - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return ent;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#insDireccionEmpresa(cl.bbr.vte.empresas.dto.DireccionesDTO)
	 */
	public boolean insMail(MailDTO mail) throws EmpresasDAOException  {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delEmpresa:");
		
		try{
			String sql ="INSERT INTO FO_SEND_MAIL ( " +
				"FSM_IDFRM, FSM_REMITE, FSM_DESTINA, FSM_COPIA, FSM_SUBJECT, " +
				"FSM_DATA, FSM_ESTADO, FSM_STMPSAVE, FSM_STMPSEND ) " +
				"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
			logger.debug("sql:"+sql);
			logger.debug("mail.getFsm_remite()	:	"+mail.getFsm_remite());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
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
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new EmpresasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insMail - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEmpresasByCriteria()
	 */
	public List getEmpresasByCriteria() throws EmpresasDAOException  {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getEmpresasByCriteria sin criteria...");
			
			String sql = " SELECT emp_id, " +
					" emp_rut, " +
					" emp_dv, " +
					" emp_nom, " +
					" emp_descr, " +
					" emp_rzsocial, " +
					" emp_rubro, " +
					" emp_tamano, " +
					" emp_qtyemp, " +
					" emp_nom_contacto, " +
					" emp_fono1_contacto, " +
					" emp_fono2_contacto, " +
					" emp_fono3_contacto, " +
					" emp_mail_contacto, " +
					" emp_cargo_contacto, " +
					" emp_saldo, " +
					" emp_fact_saldo, " +
					" emp_fmod, " +
					" emp_estado, " +
					" emp_mg_min, " +
					" emp_fec_crea, " +
					" emp_mod_rzsoc, " +
					" emp_dscto_max " +
					" FROM ve_empresa";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			EmpresasEntity ent = null;
			while (rs.next()) {
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
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
				
				result.add(ent);
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresasByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, result tiene "+result.size());
		return result;
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEmpresasByCriteria(cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO)
	 */
	public List getEmpresasByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasDAOException  {
		List result = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getEmpresasByCriteria...");
			//verifica los criterios de busqueda
			//rut de empresa
			String rut = criterio.getRut();
			logger.debug("rut	:"+rut);
			String sqlRut = "";
			if( (rut != null) && (!rut.equals("")) )
				sqlRut = " AND emp_rut = " + rut;
			//razon social
			String razon_social = criterio.getRazon_social();
			logger.debug("razon_social	:"+razon_social);
			String sqlRazSoc = "";
			if ((razon_social != null) && (!razon_social.equals("")) ){
				sqlRazSoc = " AND (upper(emp_rzsocial) like '%"+razon_social.toUpperCase()+"%') ";
			}
			//estado
			String estado = criterio.getEstado();
			logger.debug("estado	:"+estado);
			String sqlEstado="";
			if( (estado != null) && (!estado.equals("")) )
				sqlEstado = " AND emp_estado = '" + estado+"' ";
			
			//variables de paginacion
			int pag = criterio.getPag();
			int regXpag = criterio.getRegsperpage();
			if(pag<0) pag = 1;
			if(regXpag<10) regXpag = 10;
			int iniReg = (pag-1)*regXpag + 1;
			int finReg = pag*regXpag;
			
			String sql = " SELECT * FROM ( " +
					" SELECT row_number() over(order by emp_id) as row," +
					" emp_id, emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, emp_tamano, emp_qtyemp, " +
					" emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, emp_mail_contacto, emp_cargo_contacto, " +
					" emp_saldo, emp_fact_saldo, emp_fmod, emp_estado, emp_mg_min, emp_mod_rzsoc, emp_dscto_max " +
					" FROM ve_empresa WHERE emp_id >= 0 " + sqlRut + sqlRazSoc + sqlEstado +
					") AS TEMP " +
					"WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			EmpresasEntity ent = null;
			while (rs.next()) {
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
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
				
				result.add(ent);
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresasByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, result tiene "+result.size());
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEmpresasCountByCriteria(cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO)
	 */
	public int getEmpresasCountByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasDAOException  {
		int result = 0;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("En getEmpresasCountByCriteria...");
			//verifica los criterios de busqueda
			//rut de empresa
			String rut = criterio.getRut();
			String sqlRut = "";
			if( (rut != null) && (!rut.equals("")) )
				sqlRut = " AND emp_rut = " + rut;
			//razon social
			String razon_social = criterio.getRazon_social();
			String sqlRazSoc = "";
			if ((razon_social != null) && (!razon_social.equals("")) ){
				sqlRazSoc = " AND (upper(emp_rzsocial) like '%"+razon_social.toUpperCase()+"%') ";
			}
			//estado
			String estado = criterio.getEstado();
			logger.debug("estado	:"+estado);
			String sqlEstado="";
			if( (estado != null) && (!estado.equals("")) )
				sqlEstado = " AND emp_estado = '" + estado+"' ";
			
			String sql = 
					"SELECT count(emp_id) as cantidad " +
					" FROM ve_empresa WHERE emp_id >= 0 " + sqlRut + sqlRazSoc + sqlEstado ;
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cantidad");
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresasCountByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok, result : "+result);

		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getSucursalesByEmpresaId(long)
	 */
	public List getSucursalesByEmpresaId(long id) throws EmpresasDAOException  {
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
					" FROM fo_clientes where cli_emp_id = ? ";
			
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
				ent.setSuc_razon(rs.getString("cli_nombre"));
				ent.setSuc_nombre(rs.getString("cli_apellido_pat"));
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
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getEstadosByTipo(java.lang.String)
	 */
	public List getEstadosByTipo(String tipo) throws EmpresasDAOException  {
		List result = new ArrayList();
		
		EstadoDTO dto = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT id, estado, tipo, visible " +
					" FROM fo_estados where tipo = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1,tipo);
			logger.debug("En getEstadosByTipo()");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				dto = new EstadoDTO();
				dto.setId(rs.getString("id"));
				dto.setEstado(rs.getString("estado"));
				dto.setTipo(rs.getString("tipo"));
				dto.setVisible(rs.getString("visible"));
				result.add(dto);
			}
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEstadosByTipo - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getCargoByEmpresaId(long)
	 */
	/*public CargosEntity getCargoByEmpresaId(long id) throws EmpresasDAOException  {
		CargosEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = 
					" SELECT ve_car_id, emp_id, id_pedido, monto_cargo, fechaing " +
					" FROM ve_cargos where emp_id = ? ORDER BY ve_car_id DESC ";
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id);
			logger.debug("En getCargoByEmpresaId()");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				ent = new CargosEntity();
				ent.setId_cargo(new Long(rs.getLong("ve_car_id")));
				ent.setId_empresa(new Long(rs.getLong("emp_id")));
				ent.setId_pedido(new Long(rs.getLong("id_pedido")));
				ent.setMonto_cargo(new Double(rs.getDouble("monto_cargo")));
				ent.setFecha_ing(rs.getTimestamp("fechaing"));
			}
			rs.close();
			stm.close();
			releaseConnection();
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return ent;
	}
	*/
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getLocalComuna(long)
	 */
	/*public String getLocalComuna( long comuna ) throws EmpresasDAOException  {
		String local = null;
		PreparedStatement stm=null;
		ResultSet rs=null;		
		try {

			conn = this.getConnection();
			String sql ="SELECT bo_zonas.id_zona, id_local " +
							"FROM bo_comunaxzona join bo_zonas on bo_comunaxzona.id_zona=bo_zonas.id_zona " +
							"where id_comuna = ? " +
							"order by orden";			
			stm = conn.prepareStatement(sql);
			stm.setLong(1,comuna);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if(rs.next()) {
				local = rs.getString("id_zona") + "-" + rs.getString("id_local");
			}
			else {
				throw new EmpresasDAOException( "No existe zona para la comuna" );
			}
			
		}catch (SQLException e) {
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
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
		return local;	
	}*/
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#insSolReg(cl.bbr.vte.empresas.dto.SolRegDTO)
	 */
	public long insSolReg(SolRegDTO dto) throws EmpresasDAOException  { 
		ResultSet rs = null;
		long id = 0;
		PreparedStatement stm = null; 
		logger.debug("en delEmpresa:");
		
		try{
			String sql ="INSERT INTO VE_REG_EMPRESA (NOM_CONTACTO, CARGO, TEL_COD1, TEL_NUM1, TEL_COD2, " +
					"TEL_NUM2, TEL_COD3, TEL_NUM3, EMAIL, RUT_EMP, NOM_EMP, RAZ_SOCIAL, GIRO, TAM_EMPRESA, " +
					"DIR_COMERCIAL, ESTADO, CIUDAD, COMUNA, FEC_ING) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, CURRENT TIMESTAMP) ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setString(1, dto.getNom_contacto());
			stm.setString(2, dto.getCargo());
			stm.setString(3, dto.getTel_cod1());
			stm.setString(4, dto.getTel_num1());
			stm.setString(5, dto.getTel_cod2());
			stm.setString(6, dto.getTel_num2());
			stm.setString(7, dto.getTel_cod3());
			stm.setString(8, dto.getTel_num3());
			stm.setString(9, dto.getEmail());
			stm.setString(10, dto.getRut_emp());
			stm.setString(11, dto.getNom_emp());
			stm.setString(12, dto.getRaz_social());
			stm.setString(13, dto.getGiro());
			stm.setLong(14, dto.getTam_empresa());
			stm.setString(15, dto.getDir_comercial());
			stm.setString(16, dto.getEstado());
			stm.setString(17, dto.getCiudad());
			stm.setString(18, dto.getComuna());
			
			logger.debug("SQL (insSolReg): " + stm.toString());
			
			
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
	            id = rs.getInt(1);
	            logger.debug("id:"+id);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new EmpresasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insSolReg - Problema SQL (close)", e);
			}
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.EmpresasDAO#getSaldoActualDisponible(long)
	 */
	public double getSaldoActualPendiente(long empresa) throws EmpresasDAOException {
		double saldo_pend_actual =0.0;
		PreparedStatement stm=null;
		ResultSet rs=null;		
		try {

			conn = this.getConnection();
			
			String sql = "SELECT SUM (TRX.MONTO_TRXMP) PENDIENTES "
                       + "FROM VE_EMPRESA E "
                       + "     JOIN VE_COTIZACION C    ON E.EMP_ID = C.COT_EMP_ID    AND E.EMP_ID = ? "
                       + "     JOIN BO_PEDIDOS P       ON C.COT_ID = P.ID_COTIZACION AND P.ID_ESTADO IN " + Constantes.ESTADOS_PEDIDO_PENDIENTES
                       + " 	                                                         AND P.MEDIO_PAGO = 'CRE' "
                       + "     INNER JOIN BO_TRX_MP TRX ON TRX.ID_PEDIDO = P.ID_PEDIDO  "
                       + "WHERE TRX.FCREACION IS NULL OR TRX.FCREACION >= (CURRENT DATE - " + Constantes.IF_LINEA_CREDITO_DIAS + " DAY) ";
			
			logger.debug("SQL getSaldoActualPendiente:"+sql);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,empresa);
            System.out.println("empresa:"+empresa);
			
			rs = stm.executeQuery();
			if ( rs.next() ) {
                if ( rs.getString("pendientes") != null ) {
                    saldo_pend_actual = rs.getDouble("pendientes");
                }
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new EmpresasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSaldoActualPendiente - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		
		return saldo_pend_actual;
	}

    public boolean modEmpresaLinea(EmpresasDTO prm) throws EmpresasDAOException {
        boolean result = false;
        PreparedStatement stm = null; 
        try {
            String sql =" UPDATE fodba.ve_empresa SET emp_saldo = ?, emp_fmod = CURRENT TIMESTAMP WHERE emp_id = ? ";
            conn = this.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setDouble(1, prm.getEmp_saldo());
            stm.setLong(2, prm.getEmp_id());
            
            int i= stm.executeUpdate();
            if ( i > 0 ) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmpresasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modEmpresaLinea - Problema SQL (close)", e);
			}
		}
        return result;
    }

    public void setEmpresaLineaLog(EmpresaLogDTO log) throws EmpresasDAOException {
        PreparedStatement stm = null; 
        try {
            String sql =" INSERT into FODBA.VE_EMPRESA_LINEA_LOG " +
                        " (ID_EMPRESA, ID_USUARIO, EMP_OLD_SALDO, EMP_NEW_SALDO, FECHA) " +
                        " values (?,?,?,?,CURRENT TIMESTAMP) ";
            conn = this.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, log.getIdEmpresa());
            stm.setLong(2, log.getIdUsuario());
            stm.setDouble(3, log.getSaldoOld());
            stm.setDouble(4, log.getSaldoNew());
            
            stm.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmpresasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setEmpresaLineaLog - Problema SQL (close)", e);
			}
		}
    }	
}
