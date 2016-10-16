package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.contenidos.exceptions.EstadosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los estados que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcEstadosDAO  implements EstadosDAO{
	
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
	JdbcTransaccion trx = null;

//	 ************ Métodos Privados *************** //

	/**
	* Obtiene la conexión
	* 
	* @return
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
	* @throws EstadosDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws EstadosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new EstadosDAOException(e);
		}
	}

	
	/**
	* Obtiene la lista de estados, segun el tipo de estado y si es visible en web o no 
	* 
	* @param  tip_estado String 
	* @param  visible String 
	* @return List EstadoEntity
	* @throws EstadosDAOException
	*/
	public List getEstados(String tip_estado,String visible) throws EstadosDAOException {
		List list_estado = new ArrayList();
		EstadoEntity est = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		//visible
		String sqlWhere = "";
		if(!visible.equals(""))
			sqlWhere = " AND visible='"+visible+"' " ;
		
		String sql = 
				" SELECT id, estado, tipo " +
				" FROM fo_estados " + " WHERE tipo='"+tip_estado + "' " + sqlWhere;
		
		logger.debug("SQL:" + sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");

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
			e.printStackTrace();
			throw new EstadosDAOException(e);
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
		logger.debug("cant en lista:"+list_estado.size());
		
		return list_estado;
	}

	
}
