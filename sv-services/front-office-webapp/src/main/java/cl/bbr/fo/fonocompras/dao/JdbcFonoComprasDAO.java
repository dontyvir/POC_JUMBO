package cl.bbr.fo.fonocompras.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.fonocompras.exception.FonoComprasDAOException;
import cl.bbr.log_app.Logging;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcFonoComprasDAO implements FonoComprasDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);

	/* (sin Javadoc)
	 * @see cl.bbr.fo.fonocompras.dao.FonoComprasDAO#ejecutivoGetByRut(java.sql.Connection, java.lang.String)
	 */
	public UsuarioDTO ejecutivoGetByRut(String login ) throws FonoComprasDAOException {

		java.io.InputStream log4jProps = this.getClass().getClassLoader()
				.getResourceAsStream("/fo_app.properties");
		Properties prop = new Properties();
		Statement stm = null;
		UsuarioDTO usuario = null;
		ResultSet rs = null;
		String SQL = "";
		Connection conexion = null;
		try {
			prop.load(log4jProps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
						
			conexion = JdbcDAOFactory.getConexion();
			
			SQL = "SELECT U.ID_USUARIO, U.LOGIN, U.PASS, U.NOMBRE, " 
                + "       U.APELLIDO, U.APELLIDO_MAT, U.EMAIL " 
                + "FROM BODBA.BO_USUARIOS U "
                + "     JOIN BODBA.BO_USUXPERF UP ON UP.ID_USUARIO = U.ID_USUARIO "
                + "WHERE UPPER(U.LOGIN) = '" + login.toUpperCase() + "' "
                + "  AND U.ESTADO = 'A' "
                + "  AND UP.ID_PERFIL = " + Long.parseLong(prop.getProperty("fonocompras.id_perfil"));
			stm = conexion.createStatement();

			logger.debug("SQL (ejecutivoGetByRut): " + SQL + " WITH UR ");
			rs = stm.executeQuery(SQL + " WITH UR ");
			if (rs.next()) {

				this.logger.logData( "ejecutivoGetByRut", rs);

				usuario = new UsuarioDTO();
				usuario.setId_usuario( rs.getLong("id_usuario") );
				//usuario.setId_local( rs.getLong("id_local") );
				usuario.setLogin( rs.getString("login") );
				usuario.setPass( rs.getString("pass") );
				usuario.setNombre( rs.getString("nombre") );
				usuario.setApellido_p( rs.getString("apellido") );
				usuario.setApellido_m( rs.getString("apellido_mat") );
				usuario.setEmail( rs.getString("email") );
			}
			
		} catch (SQLException ex) {
			logger.error("ejecutivoGetByRut - Problema SQL", ex);
			throw new FonoComprasDAOException(ex);
		} catch (Exception ex) {
			logger.error("ejecutivoGetByRut - Problema General", ex);
			throw new FonoComprasDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("ejecutivoGetByRut - Problema SQL (close)", e);
			}
		}
		return usuario;
	}

	
	
}
