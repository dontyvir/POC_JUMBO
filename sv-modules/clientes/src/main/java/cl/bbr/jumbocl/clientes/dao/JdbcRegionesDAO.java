package cl.bbr.jumbocl.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.exceptions.RegionesDAOException;
import cl.bbr.jumbocl.shared.log.Logging;


/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcRegionesDAO implements RegionesDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	//Logging logger = new Logging( this );
	Logging logger = new Logging(this);
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.fo.regiones.dao.RegionesDAO#getRegiones()
	 */
	public List getRegiones() throws RegionesDAOException {
		List listaDirecciones = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement("select reg_id, reg_nombre, reg_orden " +
											"FROM bo_regiones " +
											"order by reg_orden with ur");
			
			rs = stm.executeQuery();
			while (rs.next()) {
                RegionesDTO reg = new RegionesDTO();
                reg.setId(rs.getLong("reg_id"));
                reg.setNombre( rs.getString("reg_nombre") );
                reg.setOrden( rs.getLong("reg_orden") );
                listaDirecciones.add(reg);
			}
		} catch (Exception e) {
			logger.error( "(getRegiones) Problema", e);
			throw new RegionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getRegiones - Problema SQL (close)", e);
			}
		}
		return listaDirecciones;

	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.fo.regiones.dao.RegionesDAO#getRegion()
	 */
	public RegionesDTO getRegion(long id_comuna) throws RegionesDAOException {		
		RegionesDTO region = new RegionesDTO();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conexion = JdbcDAOFactory.getConexion();
			String Query = "SELECT reg.reg_id ID, reg.reg_nombre NOMBRE, reg.reg_orden "
                         + "FROM bo_regiones reg "
						 + "     INNER JOIN bo_comunas com ON (reg.reg_id = com.reg_id) "
						 + "WHERE com.id_comuna = " + id_comuna;
			stm = conexion.prepareStatement(Query  + " WITH UR");
			logger.debug("SQL getRegion: " + Query);

			rs = stm.executeQuery();
			if (rs.next()) {
				region = new RegionesDTO();
				region.setId(rs.getLong("ID"));
				region.setNombre(rs.getString("NOMBRE") );
				region.setOrden(rs.getLong("reg_orden"));
			}
		} catch (Exception e) {
			logger.error( "(getRegion) Problema", e);
			throw new RegionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getRegion - Problema SQL (close)", e);
			}
		}
		return region;
	}
	
	/*
	 * (non-Javadoc)
	 * @see cl.bbr.fo.regiones.dao.RegionesDAO#getComunas(long)
	 */
	public List getComunas( long idRegion ) throws RegionesDAOException {
		List lista = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
            String sql = "SELECT id_comuna, reg_id, nombre " + 
                         "FROM bo_comunas " +
                         "WHERE reg_id = ? and tipo = 'W' " +
                         "ORDER BY nombre";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(sql  + " WITH UR");
            stm.setLong(1, idRegion);
			
			rs = stm.executeQuery();
			while (rs.next()) {                
                ComunaDTO comuna = new ComunaDTO(); 
				comuna.setId( rs.getLong("id_comuna") );
				comuna.setNombre( rs.getString("nombre") );
				comuna.setReg_id( rs.getLong("reg_id") );
				lista.add(comuna);
			}

		} catch (Exception e) {
			logger.error( "(getComunas) Problema", e);
			throw new RegionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getComunas - Problema SQL (close)", e);
			}
		}
		return lista;	
	}

    /**
     * @param idRegion
     * @return
     */
    public RegionesDTO getRegionById(int idRegion) throws RegionesDAOException {        
        RegionesDTO region = new RegionesDTO();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "SELECT reg.reg_id ID, reg.reg_nombre NOMBRE, reg.reg_orden " +
                           "FROM bo_regiones reg " +
                           "WHERE reg.reg_id = " + idRegion;
            stm = conexion.prepareStatement(sql  + " WITH UR");
            logger.debug("SQL getRegionById: " + sql);

            rs = stm.executeQuery();
            if (rs.next()) {
                region = new RegionesDTO();
                region.setId(rs.getLong("ID"));
                region.setNombre(rs.getString("NOMBRE") );
                region.setOrden(rs.getLong("reg_orden"));
            }
        } catch (Exception e) {
            logger.error( "(getRegionById) Problema", e);
            throw new RegionesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getRegionById - Problema SQL (close)", e);
            }
        }
        return region;
    }

    /**
     * @return
     */
    public List regionesConCobertura() throws RegionesDAOException {        
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List regiones = new ArrayList();
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "select r.REG_ID, r.REG_NOMBRE, r.REG_ORDEN " +
                         "from bodba.bo_poligono p " +
                         "inner join bodba.bo_comunas c on c.ID_COMUNA = p.ID_COMUNA " +
                         "inner join bodba.bo_regiones r on r.REG_ID = c.REG_ID " +
                         "group by r.REG_ID, r.REG_NOMBRE, r.REG_ORDEN " +
                         "order by r.REG_ORDEN";
            
            stm = conexion.prepareStatement(sql  + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                RegionesDTO region = new RegionesDTO();
                region.setId(rs.getLong("REG_ID"));
                region.setNombre(rs.getString("REG_NOMBRE") );
                region.setOrden(rs.getLong("REG_ORDEN"));
                regiones.add(region);
            }
        } catch (Exception e) {
            logger.error( "(regionesConCobertura) Problema", e);
            throw new RegionesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("regionesConCobertura - Problema SQL (close)", e);
            }
        }
        return regiones;
    }

    /**
     * @param idRegion
     * @return
     */
    public List comunasConCoberturaByRegion(long idRegion) throws RegionesDAOException {        
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List comunas = new ArrayList();
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "select c.ID_COMUNA, c.REG_ID, c.NOMBRE " +
                         "from bodba.bo_poligono p " +
                         "inner join bodba.bo_comunas c on c.ID_COMUNA = p.ID_COMUNA " +
                         "where c.TIPO = 'W' and c.REG_ID = " + idRegion + " " +
                         "group by c.ID_COMUNA, c.REG_ID, c.NOMBRE " +
                         "order by c.NOMBRE";
            stm = conexion.prepareStatement(sql  + " WITH UR");
            
            rs = stm.executeQuery();
            while (rs.next()) {
                ComunaDTO comuna = new ComunaDTO();
                comuna.setId(rs.getLong("ID_COMUNA"));
                comuna.setNombre(rs.getString("NOMBRE") );
                comuna.setReg_id(rs.getLong("REG_ID"));
                comunas.add(comuna);
            }
        } catch (Exception e) {
            logger.error( "(comunasConCoberturaByRegion) Problema", e);
            throw new RegionesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("comunasConCoberturaByRegion - Problema SQL (close)", e);
            }
        }
        return comunas;
    }

    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getComunaConLocal(long idComuna) throws RegionesDAOException {        
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ComunaDTO comuna = new ComunaDTO();
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "select c.ID_COMUNA, c.NOMBRE, c.REG_ID, z.ID_ZONA, l.ID_LOCAL, l.NOM_LOCAL " +
                         "from bodba.bo_comunas c " +
                         "inner join bodba.bo_poligono p on p.ID_COMUNA = c.ID_COMUNA " +
                         "inner join bodba.bo_zonas z on z.ID_ZONA = p.ID_ZONA " +
                         "inner join bodba.bo_locales l on l.ID_LOCAL = z.ID_LOCAL " +
                         "where c.ID_COMUNA = " + idComuna + " " +
                         "group by c.ID_COMUNA, c.NOMBRE, c.REG_ID, z.ID_ZONA, l.ID_LOCAL, l.NOM_LOCAL";
            stm = conexion.prepareStatement(sql  + " WITH UR");
            
            rs = stm.executeQuery();
            if (rs.next()) {
                comuna.setId(rs.getLong("ID_COMUNA"));
                comuna.setNombre(rs.getString("NOMBRE") );
                comuna.setReg_id(rs.getLong("REG_ID"));   
                comuna.setZona_id(rs.getLong("ID_ZONA"));
                comuna.setLocal_id(rs.getLong("ID_LOCAL"));
            }
        } catch (Exception e) {
            logger.error( "(getComunaConLocal) Problema", e);
            throw new RegionesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getComunaConLocal - Problema SQL (close)", e);
            }
        }
        return comuna;
    }	
    
    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getZonaxComuna(long idComuna) throws RegionesDAOException {        
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ComunaDTO comuna = new ComunaDTO();
        
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "select c.ID_COMUNA, c.NOMBRE, c.REG_ID, z.ID_ZONA, l.ID_LOCAL, l.NOM_LOCAL " +
                         "from bodba.bo_comunas c " +
                         "inner join bodba.bo_poligono p on p.ID_COMUNA = c.ID_COMUNA " +
                         "inner join bodba.bo_zonas z on z.ID_ZONA = p.ID_ZONA " +
                         "inner join bodba.bo_locales l on l.ID_LOCAL = z.ID_LOCAL " +
                         "where c.ID_COMUNA = " + idComuna + " " +
                         " and p.num_poligono = 0 " +
                         "group by c.ID_COMUNA, c.NOMBRE, c.REG_ID, z.ID_ZONA, l.ID_LOCAL, l.NOM_LOCAL";
            
            stm = conexion.prepareStatement(sql + " with ur ");
            
            rs = stm.executeQuery();
            if (rs.next()) {
                comuna.setId(rs.getLong("ID_COMUNA"));
                comuna.setNombre(rs.getString("NOMBRE") );
                comuna.setReg_id(rs.getLong("REG_ID"));   
                comuna.setZona_id(rs.getLong("ID_ZONA"));
                comuna.setLocal_id(rs.getLong("ID_LOCAL"));
            }
        } catch (Exception e) {
            logger.error( "(getZonaxComuna) Problema", e);
            throw new RegionesDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getZonaxComuna - Problema SQL (close)", e);
            }
        }
        return comuna;
    }       

}
