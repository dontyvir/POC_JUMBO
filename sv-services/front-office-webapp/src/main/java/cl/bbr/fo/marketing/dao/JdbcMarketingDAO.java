package cl.bbr.fo.marketing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.bbr.fo.marketing.dto.MarketingElementoDTO;
import cl.bbr.fo.marketing.exception.MarketingDAOException;
import cl.bbr.log_app.Logging;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcMarketingDAO implements MarketingDAO {

	/**
	 * Instancia del log
	 */
	Logging logger = new Logging(this);


	/* (sin Javadoc)
	 * @see cl.bbr.fo.marketing.dao.MarketingDAO#getMarkElemento(long, long)
	 */
	public MarketingElementoDTO getMarkElemento( long ele_id, long mar_id) throws MarketingDAOException {

		MarketingElementoDTO marketing = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		PreparedStatement stm2 = null;
		ResultSet rs = null;
		
		try {

			String query = "SELECT elca_click, ele_url_destino, elca_ele_id, elca_cam_id " +
					"FROM fo_eltos_cam join fo_mar_elementos on elca_ele_id = ele_id " +
					"where elca_ele_id = ? and elca_cam_id = ?";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setLong(1, ele_id);
			stm.setLong(2, mar_id);
			logger.debug("SQL (getMarkElemento): " + stm.toString());
			rs = stm.executeQuery();
			if (rs.next()) {
				this.logger.logData( "getMarkElemento", rs);
				marketing = new MarketingElementoDTO();
				marketing.setElca_eleid(rs.getLong("elca_ele_id"));
				marketing.setElca_camid(rs.getLong("elca_cam_id"));
				marketing.setClick(rs.getLong("elca_click"));
				marketing.setUrl(rs.getString("ele_url_destino"));
				
				conexion = JdbcDAOFactory.getConexion();
				stm2 = conexion.prepareStatement("UPDATE fo_eltos_cam set elca_click = (elca_click + 1) where elca_ele_id = ? and elca_cam_id = ? ");
				stm2.setLong(1, ele_id);
				stm2.setLong(2, mar_id);

				logger.debug("SQL: " + stm2.toString());
				if (stm2.executeUpdate() == 0) {
					logger.debug("getMarkElemento - No modifica pero sin excepciones");
				}				
			}

		} catch (SQLException ex) {
			logger.error("getMarkElemento - Problema SQL", ex);
			throw new MarketingDAOException(ex);
		} catch (Exception ex) {
			logger.error("getMarkElemento - Problema General", ex);
			throw new MarketingDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getMarkElemento - Problema SQL (close)", e);
			}
		}

		return marketing;

	}	
	
	
}
