package cl.bbr.jumbocl.informes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.informes.dto.OriginalesYSustitutosDTO;
import cl.bbr.jumbocl.informes.exceptions.InformesDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los Casos que se encuentran en la base de datos.
 * @author imoyano
 *
 */
public class JdbcInformesDAO implements InformesDAO {
	
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
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
		}
			
	}
	
	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws InformesDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new InformesDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcInformesDAO(){
		
	}

    /**
     * @return
     */
    public List getMailsSyF() throws InformesDAOException {
	    
		List mails = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BO_MAIL_SYF M ";
		
		logger.debug("* SQL getMailsSyF :"+sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);	
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    MailSustitutosFaltantesDTO mail = new MailSustitutosFaltantesDTO();
			    mail.setId(rs.getLong("id_mail_syf"));
			    mail.setFechaIngreso(rs.getString("fecha_creacion"));
			    mail.setFechaModificacion(rs.getString("fecha_modificacion"));
			    mail.setActivado(rs.getString("activado"));
			    mail.setNombre(rs.getString("nombres"));
			    mail.setApellido(rs.getString("apellidos"));
			    mail.setMail(rs.getString("mail"));
			    mails.add(mail);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
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
		return mails;
    }

    /**
     * @param m
     * @return
     */
    public long addMailSyF(MailSustitutosFaltantesDTO m) throws InformesDAOException {	    
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		long idMail				= 0;
		
		String sql = " INSERT INTO BO_MAIL_SYF " +
					 " (FECHA_CREACION, FECHA_MODIFICACION, ACTIVADO, NOMBRES, APELLIDOS, MAIL) " +
					 " VALUES " +
					 " (?,?,?,?,?,?) ";
		
		logger.debug("* SQL addMailSyF :"+sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );
			stm.setTimestamp(1, new Timestamp(System.currentTimeMillis()) );
			stm.setTimestamp(2, new Timestamp(System.currentTimeMillis()) );
			stm.setString(3, m.getActivado());
			stm.setString(4, m.getNombre());
			stm.setString(5, m.getApellido());
			stm.setString(6, m.getMail());
			
			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			rs = stm.getGeneratedKeys();
			
			if (rs.next()) {				
			    idMail = rs.getLong(1);
				logger.debug("idMail insertado:" + idMail);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
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
		return idMail;
    }

    /**
     * @param m
     */
    public void modMailSyF(MailSustitutosFaltantesDTO m) throws InformesDAOException {	    
		PreparedStatement stm 	= null;
		
		String sql = " UPDATE  BO_MAIL_SYF " +
					 " SET FECHA_MODIFICACION = ?, ACTIVADO = ?, NOMBRES = ?, APELLIDOS = ?, MAIL = ? " +
					 " WHERE ID_MAIL_SYF = ? ";
		
		logger.debug("* SQL modMailSyF :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setTimestamp(1, new Timestamp(System.currentTimeMillis()) );
			stm.setString(2, m.getActivado());
			stm.setString(3, m.getNombre());
			stm.setString(4, m.getApellido());
			stm.setString(5, m.getMail());
			stm.setLong(6, m.getId());
			int i = stm.executeUpdate();
			
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
    }

    /**
     * @param idMail
     * @return
     */
    public MailSustitutosFaltantesDTO getMailSyFById(long idMail) throws InformesDAOException {
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		MailSustitutosFaltantesDTO mail = new MailSustitutosFaltantesDTO();
		
		String sql = " SELECT * " +
					 " FROM BO_MAIL_SYF M " +
					 " WHERE M.ID_MAIL_SYF = ? ";
		
		logger.debug("* SQL getMailSyFById :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idMail);
			rs = stm.executeQuery();
			
			if (rs.next()) {			    
			    mail.setId(rs.getLong("id_mail_syf"));
			    mail.setFechaIngreso(rs.getString("fecha_creacion"));
			    mail.setFechaModificacion(rs.getString("fecha_modificacion"));
			    mail.setActivado(rs.getString("activado"));
			    mail.setNombre(rs.getString("nombres"));
			    mail.setApellido(rs.getString("apellidos"));
			    mail.setMail(rs.getString("mail"));
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
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
		return mail;
    }

    /**
     * @param idMail
     */
    public void delMailSyFById(long idMail) throws InformesDAOException {	    
		PreparedStatement stm 	= null;
		
		String sql = " DELETE FROM BO_MAIL_SYF " +
					 " WHERE ID_MAIL_SYF = ? ";
		
		logger.debug("* SQL delMailSyFById :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idMail);
			stm.executeUpdate();
			
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
    }

    /**
     * @return
     */
    public List getOriginalesYSustitutos() throws InformesDAOException {	    
		List productos = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BO_INF_SYF ";
		
		logger.debug("* SQL getOriginalesYSustitutos :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);	
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    OriginalesYSustitutosDTO prod = new OriginalesYSustitutosDTO();
			    prod.setCodProdOriginal(rs.getString("cod_prod_original"));
			    prod.setUniMedProdOriginal(rs.getString("ume_original"));
			    prod.setCodProdSustituto(rs.getString("cod_prod_sus"));
			    prod.setUniMedProdSustituto(rs.getString("ume_sus"));
			    productos.add(prod);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
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
		return productos;
    }

    /**
     * @param sustitutos
     * @return
     */
    public String addCodigosSyF(ArrayList sustitutos) throws InformesDAOException {	    
		PreparedStatement stmDel = null;
		PreparedStatement stmIns = null;
		long aInsertar = sustitutos.size();
		String msg = "";
		
		String sql1 = " DELETE FROM BO_INF_SYF ";
		
		String sql2 = " INSERT INTO BO_INF_SYF " +
					  " (cod_prod_original, ume_original, cod_prod_sus, ume_sus) " +
					  " VALUES " +
					  " (?,?,?,?) ";
		
		logger.debug("* SQL addCodigosSyF 1:"+sql1);
		logger.debug("* SQL addCodigosSyF 2:"+sql2);
		
		try {
		    
		    conn = this.getConnection();
		    stmDel = conn.prepareStatement(sql1);
		    stmDel.executeUpdate();
		    
		    stmIns 	= conn.prepareStatement(sql2);		    
		    for (int i = 0; i < sustitutos.size(); i++) {
		        OriginalesYSustitutosDTO syf = (OriginalesYSustitutosDTO) sustitutos.get(i);
		        stmIns.setString(1, syf.getCodProdOriginal() );
		        stmIns.setString(2, syf.getUniMedProdOriginal() );
		        stmIns.setString(3, syf.getCodProdSustituto() );
		        stmIns.setString(4, syf.getUniMedProdSustituto() );
		        stmIns.executeUpdate();		    
		    }
		    
		    if (stmDel != null) stmDel.close();
			if (stmIns != null) stmIns.close();
			releaseConnection();
			
			if (aInsertar == 1) {
			    msg += "<br>Se agregó " + aInsertar + " registro.<br>";
			} else if (aInsertar > 1) {
			    msg += "<br>Se agregaron " + aInsertar + " registros.<br>";
			}
			
		}catch (SQLException e) {
			throw new InformesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (stmDel != null) stmDel.close();
            	if (stmIns != null) stmIns.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return msg;
    }
	
	
	
    
}
