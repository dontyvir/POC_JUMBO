package cl.bbr.jumbocl.shared.emails.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.shared.emails.exceptions.EmailDAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */

public class JdbcEmailDAO implements EmailDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);

	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#addMail(cl.bbr.fo.clientes.dto.MailDTO)
	 */
	public void addMail(MailDTO mail) throws EmailDAOException {
		PreparedStatement stm= null;
		Connection conexion = null;
		
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
				stm.setNull(4, java.sql.Types.INTEGER );
			stm.setString(5, mail.getFsm_subject() );			
			stm.setString(6, mail.getFsm_data() );			
			stm.setString(7, mail.getFsm_estado() );			
			stm.setTimestamp(8, new Timestamp( mail.getFsm_stmpsave() ) );			
			stm.setNull(9, java.sql.Types.INTEGER );	
			
			logger.debug("SQL (addMail): " + stm.toString());
			
			stm.executeUpdate();
			
		} catch (SQLException ex) {
			logger.error("addMail - Problema SQL", ex);
			throw new EmailDAOException(ex);
		} catch (Exception ex) {
			logger.error("addMail - Problema General", ex);
			throw new EmailDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				//cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : addMail - Problema SQL (close)", e);
			}
		}
		
	}	
	
}
