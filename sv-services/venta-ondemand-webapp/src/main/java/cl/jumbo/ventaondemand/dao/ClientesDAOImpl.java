package cl.jumbo.ventaondemand.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import cl.jumbo.ventaondemand.dao.factory.DAOManager;
import cl.jumbo.ventaondemand.dao.model.FOClientesEntity;
import cl.jumbo.ventaondemand.exceptions.DAOException;
import cl.jumbo.ventaondemand.web.dto.LogonDTO;
import cl.jumbo.ventaondemand.web.servlet.Logon;

public class ClientesDAOImpl implements ClientesDAO {

	private static final Logger logger = Logger.getLogger(Logon.class);
    private Connection conn = null;
    private PreparedStatement stm = null;
    private ResultSet rs = null;
	
	public FOClientesEntity getClienteByRutFO(LogonDTO logon) throws DAOException {
		FOClientesEntity cliente = null;
		conn = null;
		stm = null;
		rs = null;
		try {
			String selQuery= "SELECT CLI_ID, CLI_RUT, CLI_DV, CLI_NOMBRE, CLI_APELLIDO_PAT, "
                           + "       CLI_APELLIDO_MAT, CLI_CLAVE, CLI_EMAIL, CLI_FON_COD_1, "
                           + "       CLI_FON_NUM_1, CLI_FON_COD_2, CLI_FON_NUM_2, CLI_REC_INFO, "
                           + "       CLI_FEC_CREA, CLI_ESTADO, CLI_FEC_NAC, CLI_GENERO, "
                           + "       CLI_FEC_ACT, CLI_PREGUNTA, CLI_RESPUESTA, CLI_INTENTOS, "
                           + "       CLI_FEC_LOGIN, CLI_KEY_RECUPERA_CLAVE, COL_RUT "
                           +" FROM FO_CLIENTES LEFT JOIN FODBA.FO_COLABORADOR ON CLI_RUT=COL_RUT "
						   + " WHERE CLI_RUT = ? AND CLI_DV = ? AND CLI_TIPO = 'P' ";
			
			logger.debug("Consulta > 'getClienteByRutFO', Parametro Entrada: rut["+logon.getRut()+"], dv["+logon.getDv()+"]");
			
			conn = DAOManager.getConexion();
			stm = conn.prepareStatement(selQuery  + " WITH UR");
			stm.setLong(1, Long.parseLong(logon.getRut()) );
			stm.setString(2, logon.getDv());
			
			rs = stm.executeQuery();
			if (rs.next()) {

				logData( "getClienteByRutFO", rs);
				
				cliente = new FOClientesEntity();
				cliente.setCli_id(new Long(rs.getLong("cli_id")));
				cliente.setCli_rut(new Long(rs.getLong("cli_rut")));
				cliente.setCli_dv(new Character(rs.getString("cli_dv").charAt(0)));
				cliente.setCli_estado(new Character(rs.getString("cli_estado").charAt(0)));
				cliente.setCli_clave(rs.getString("cli_clave"));
				cliente.setCli_nombre(rs.getString("cli_nombre"));
				cliente.setCli_apellido_pat(rs.getString("cli_apellido_pat"));
				cliente.setCli_apellido_mat(rs.getString("cli_apellido_mat"));
				cliente.setCli_email(rs.getString("cli_email"));
				cliente.setCli_fec_nac(rs.getDate("cli_fec_nac"));
				cliente.setCli_fon_cod_1(rs.getString("cli_fon_cod_1"));
				cliente.setCli_fon_cod_2(rs.getString("cli_fon_cod_2"));
				cliente.setCli_fon_num_1(rs.getString("cli_fon_num_1"));
				cliente.setCli_fon_num_2(rs.getString("cli_fon_num_2"));
				cliente.setCli_genero(new Character(rs.getString("cli_genero").charAt(0)));
				cliente.setCli_rec_info(new Long(rs.getLong("cli_rec_info")));
				cliente.setCli_pregunta( rs.getString("cli_pregunta") );
				cliente.setCli_respuesta( rs.getString("cli_respuesta") );
				cliente.setCli_intento(new Long(rs.getLong("cli_intentos")));
				cliente.setCli_fec_login( rs.getTimestamp("cli_fec_login") );
				
				String recuperaClave  = rs.getString("CLI_KEY_RECUPERA_CLAVE") != null ? rs.getString("CLI_KEY_RECUPERA_CLAVE"): "";
				cliente.setCli_key_recupera_clave(recuperaClave);

                boolean isColaborador  = rs.getInt("COL_RUT") == 0 ? false: true;
                cliente.setColaborador(isColaborador);
			}
		} catch (SQLException sqle) {
			logger.error("Error 'getClienteByRut' - Problema SQL: ", sqle);
			throw new DAOException(sqle);
		} catch (Exception ex) {
			logger.error("Error 'getClienteByRut' - Problema Excepcion: ", ex);
			throw new DAOException(ex);
		} finally {
			try {
				DAOManager.closeResulset(rs);
				DAOManager.closeStatement(stm);
				DAOManager.closeConnection(conn);
				
			} catch (SQLException sqle) {
				logger.error("Error 'getClienteByRut' - Problema SQL (close):", sqle);
			}
		}

		return cliente;
	}

	
	/*
	 * Revisar datos que se recuperan desde la base de datos
	 */
	public static void logData( String metodo, ResultSet rs ) {				
		try {
			ResultSetMetaData rsmd = rs.getMetaData();			
			for( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
				logger.debug( metodo + " - " + rsmd.getTableName(i) + "." + rsmd.getColumnName(i) + "[" + rs.getString( rsmd.getColumnName(i) ) + "]" );
			}
		} catch (SQLException e) {
			logger.error("Problemas en logData", e);
			e.printStackTrace();
		}
	}	
}
