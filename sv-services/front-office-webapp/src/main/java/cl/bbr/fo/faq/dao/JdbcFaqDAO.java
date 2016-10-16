package cl.bbr.fo.faq.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import cl.bbr.fo.faq.dto.FaqCategoriaDTO;
import cl.bbr.fo.faq.dto.FaqPreguntaDTO;
import cl.bbr.fo.faq.exception.FaqDAOException;
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
public class JdbcFaqDAO implements FaqDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);

	/* (sin Javadoc)
	 * @see cl.bbr.fo.faq.dao.FaqDAO#getFaqCategoria()
	 */
	public List getFaqCategoria() throws FaqDAOException {
		List lista_faq = new ArrayList();
		FaqCategoriaDTO cat = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			
			String query = "select cat_id, cat_nombre, cat_estado, cat_orden  " +
						   "from fo_faq_categorias " +
						   "where cat_estado = 'A' order by cat_orden ";
			
			conexion = JdbcDAOFactory.getConexion();			
			stm = conexion.prepareStatement(query  + " WITH UR");
			logger.debug("SQL (getFaqCategoria): " + stm.toString());
			rs = stm.executeQuery();
			while (rs.next()) {
				this.logger.logData( "getFaqCategoria", rs);
				cat = null;
				cat = new FaqCategoriaDTO();

				cat.setCat_id( rs.getLong("cat_id") );
				cat.setNombre( rs.getString("cat_nombre") );
				cat.setEstado( rs.getString("cat_estado") );
				cat.setOrden( rs.getString("cat_orden") );
				lista_faq.add(cat);
			}
		} catch (SQLException ex) {
			logger.error("getFaqCategoria - Problema SQL", ex);
			throw new FaqDAOException(ex);
		} catch (Exception ex) {
			logger.error("getFaqCategoria - Problema General", ex);
			throw new FaqDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getFaqCategoria - Problema SQL (close)", e);
			}
		}
		return lista_faq;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.faq.dao.FaqDAO#getFaqPregunta(long)
	 */
	public List getFaqPregunta(long idcat) throws FaqDAOException {
		List lista_pre = new ArrayList();
		FaqPreguntaDTO pre = null;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			
			String query = "select pre_id, pre_cat_id, pre_pregunta, pre_respuesta, pre_estado  " +
						   "from fo_faq_pregunta join fo_faq_categorias on cat_id = pre_cat_id " +
						   "where pre_estado = 'A' and pre_cat_id = ? ";
			
			conexion = JdbcDAOFactory.getConexion();			
			stm = conexion.prepareStatement(query  + " WITH UR");
			stm.setLong(1, idcat);
			logger.debug("SQL (getFaqPregunta): " + stm.toString());
			rs = stm.executeQuery();
			
			while (rs.next()) {
						
				this.logger.logData( "getFaqPregunta", rs);
				pre = null;
				pre = new FaqPreguntaDTO();
				
				pre.setPre_id( rs.getLong("pre_id") );
				pre.setPre_cat_id( rs.getLong("pre_cat_id") );
				pre.setPregunta( rs.getString("pre_pregunta") );
				pre.setRespuesta( rs.getString("pre_respuesta") );
				pre.setEstado( rs.getString("pre_estado") );
				lista_pre.add(pre);				
			}
		} catch (SQLException ex) {
			logger.error("getFaqPregunta - Problema SQL", ex);
			throw new FaqDAOException(ex);
		} catch (Exception ex) {
			logger.error("getFaqPregunta - Problema General", ex);
			throw new FaqDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getFaqPregunta - Problema SQL (close)", e);
			}
		}
		return lista_pre;
	}

	
}
