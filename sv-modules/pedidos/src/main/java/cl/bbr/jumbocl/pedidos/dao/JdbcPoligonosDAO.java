package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las comunas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcPoligonosDAO implements PoligonosDAO{

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;

	// ************ Métodos Privados *************** //
	
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
		return this.conn;
	}

//	 ************ Métodos Publicos *************** //
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
	 * @throws ComunasDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws PoligonosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new PoligonosDAOException(e);
		}
	}

	/**
	 * Agrega un sector de picking
	 * @param sector SectorLocalDTO 
	 */		
	public boolean verificaNumPoligono(int id_comuna, int num_pol) throws PoligonosDAOException {		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean existe = false;
		try {
			String SQL = "SELECT P.NUM_POLIGONO "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "WHERE P.ID_COMUNA = ? "
                       + "AND P.NUM_POLIGONO = ?";
			logger.debug("SQL (verificaNumPoligono): " + SQL);
			
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR" );

			//stm.setLong(1,sector.getId_local());
			stm.setInt(1, id_comuna);
			stm.setInt(2, num_pol);
			
			logger.debug("id_comuna:" + id_comuna);
			logger.debug("num_pol  :" + num_pol);
			
			rs = stm.executeQuery();
			if (rs.next()){
			    if (rs.getInt("NUM_POLIGONO") > -1){
			        existe = true;
			    }
			}

		} catch (SQLException e) {
			logger.debug("Problema en verificaNumPoligono:"+ e.getMessage());
			throw new PoligonosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		return existe;
	}
	
	
	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getPoligonosXComuna(long id_comuna) throws PoligonosDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, P.DESCRIPCION, "
                   + "       COALESCE(L.ID_LOCAL, 0) AS ID_LOCAL, L.NOM_LOCAL, "
                   + "       COALESCE(Z.ID_ZONA, 0) AS ID_ZONA, Z.NOMBRE AS NOM_ZONA, "
                   + "       C.ID_COMUNA, C.NOMBRE AS NOM_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     LEFT JOIN BODBA.BO_ZONAS   Z ON Z.ID_ZONA   = P.ID_ZONA "
                   + "     LEFT JOIN BODBA.BO_LOCALES L ON L.ID_LOCAL  = Z.ID_LOCAL "
                   + "     JOIN BODBA.BO_COMUNAS      C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_COMUNA = ? "
                   + "  AND P.NUM_POLIGONO <> " + Constantes.NUM_POLIGONO_RETIRO_LOCAL + " "
                   + "  AND P.ID_ZONA > 0 ";
		
		logger.debug("SQL (getPoligonosXComuna): " + SQL);
		logger.debug("id_comuna: " + id_comuna);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR");
			
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				PoligonoxComunaDTO pxc = new PoligonoxComunaDTO();
				pxc.setId_poligono(rs.getInt("ID_POLIGONO"));
				pxc.setNum_poligono(rs.getInt("NUM_POLIGONO"));
				pxc.setDesc_poligono(rs.getString("DESCRIPCION"));
				pxc.setId_local(rs.getInt("ID_LOCAL"));
				if (rs.getString("NOM_LOCAL") == null){
				    pxc.setNom_local("---");
				}else{
				    pxc.setNom_local(rs.getString("NOM_LOCAL"));
				}
				pxc.setId_zona(rs.getInt("ID_ZONA"));
				if (rs.getString("NOM_ZONA") == null){
				    pxc.setNom_zona("---");
				}else{
				    pxc.setNom_zona(rs.getString("NOM_ZONA"));
				}
				pxc.setId_comuna(rs.getInt("ID_COMUNA"));
				if (rs.getString("NOM_COMUNA") == null){
				    pxc.setNom_comuna("---");
				}else{
				    pxc.setNom_comuna(rs.getString("NOM_COMUNA"));
				}
				result.add(pxc);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getPoligonosXComunaAll(long id_comuna) throws PoligonosDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		

		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, P.DESCRIPCION, "
                   + "       COALESCE(L.ID_LOCAL, 0) AS ID_LOCAL, L.NOM_LOCAL, "
                   + "       COALESCE(Z.ID_ZONA, 0) AS ID_ZONA, Z.NOMBRE AS NOM_ZONA, "
                   + "       C.ID_COMUNA, C.NOMBRE AS NOM_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     LEFT JOIN BODBA.BO_ZONAS   Z ON Z.ID_ZONA   = P.ID_ZONA "
                   + "     LEFT JOIN BODBA.BO_LOCALES L ON L.ID_LOCAL  = Z.ID_LOCAL "
                   + "     JOIN BODBA.BO_COMUNAS      C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_COMUNA = ? ";
		
		logger.debug("SQL (getPoligonosXComuna): " + SQL);
		logger.debug("id_comuna: " + id_comuna);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR"  );
			
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				PoligonoxComunaDTO pxc = new PoligonoxComunaDTO();
				pxc.setId_poligono(rs.getInt("ID_POLIGONO"));
				pxc.setNum_poligono(rs.getInt("NUM_POLIGONO"));
				pxc.setDesc_poligono(rs.getString("DESCRIPCION"));
				pxc.setId_local(rs.getInt("ID_LOCAL"));
				if (rs.getString("NOM_LOCAL") == null){
				    pxc.setNom_local("---");
				}else{
				    pxc.setNom_local(rs.getString("NOM_LOCAL"));
				}
				pxc.setId_zona(rs.getInt("ID_ZONA"));
				if (rs.getString("NOM_ZONA") == null){
				    pxc.setNom_zona("---");
				}else{
				    pxc.setNom_zona(rs.getString("NOM_ZONA"));
				}
				pxc.setId_comuna(rs.getInt("ID_COMUNA"));
				if (rs.getString("NOM_COMUNA") == null){
				    pxc.setNom_comuna("---");
				}else{
				    pxc.setNom_comuna(rs.getString("NOM_COMUNA"));
				}
				result.add(pxc);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}


	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getPoligonosXComunaSinZona(long id_comuna) throws PoligonosDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, P.DESCRIPCION, "
                   + "       C.ID_COMUNA, C.NOMBRE AS NOM_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_COMUNAS C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_ZONA IS NULL "
                   + "  AND P.ID_COMUNA = ?";
		
		logger.debug("SQL (getPoligonosXComunaSinZona): " + SQL);
		logger.debug("id_comuna: " + id_comuna);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR"  );
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				PoligonoxComunaDTO pxc = new PoligonoxComunaDTO();
				pxc.setId_poligono(rs.getInt("ID_POLIGONO"));
				pxc.setNum_poligono(rs.getInt("NUM_POLIGONO"));
				pxc.setDesc_poligono(rs.getString("DESCRIPCION"));
				pxc.setId_comuna(rs.getInt("ID_COMUNA"));
				if (rs.getString("NOM_COMUNA") == null){
				    pxc.setNom_comuna("---");
				}else{
				    pxc.setNom_comuna(rs.getString("NOM_COMUNA"));
				}
				result.add(pxc);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getComunasConPoligonosSinZona() throws PoligonosDAOException {

		List result = new ArrayList();
		
		Statement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT DISTINCT C.ID_COMUNA, C.NOMBRE AS NOM_COMUNA, C.TIPO, "
		           + "       R.REG_ID, R.REG_NOMBRE, R.REG_ORDEN "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "     JOIN BODBA.BO_REGIONES R ON R.REG_ID = C.REG_ID "
                   + "WHERE P.ID_ZONA IS NULL "
                   + "  AND C.TIPO = 'W' "
                   + "ORDER BY R.REG_ORDEN ASC, C.NOMBRE ASC";
		logger.debug("SQL (getComunasConPoligonosSinZona): " + SQL);
		
		try {
			conn = this.getConnection();
			stm = conn.createStatement();
			
			rs = stm.executeQuery(SQL);
			while (rs.next()) {
				ComunaDTO com = new ComunaDTO();
				com.setId_comuna(rs.getInt("ID_COMUNA"));
				com.setNombre(rs.getString("NOM_COMUNA"));
			    com.setTipo(rs.getString("TIPO"));
				result.add(com);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public List getPoligonosXZona(long id_zona) throws PoligonosDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, "
                   + "       P.DESCRIPCION, P.ID_COMUNA, C.NOMBRE AS NOM_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_COMUNAS C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_ZONA = ?";
		logger.debug("SQL (getPoligonosXZona): " + SQL);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, id_zona);
			rs = stm.executeQuery();
			while (rs.next()) {
				PoligonoxComunaDTO pol = new PoligonoxComunaDTO();
				pol.setId_poligono(rs.getInt("ID_POLIGONO"));
				pol.setNum_poligono(rs.getInt("NUM_POLIGONO"));
				pol.setDesc_poligono(rs.getString("DESCRIPCION"));
				pol.setId_comuna(rs.getInt("ID_COMUNA"));
				pol.setNom_comuna(rs.getString("NOM_COMUNA"));
				result.add(pol);
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}


	
	/**
	 * Obtiene listado de Poligonos de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public PoligonoDTO getPoligonoById(long id_poligono) throws PoligonosDAOException {

	    PoligonoDTO pol = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, P.DESCRIPCION, P.ID_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "WHERE P.ID_POLIGONO = ?";
		
		logger.debug("SQL (getPoligonoById): " + SQL);
		logger.debug("id_poligono: " + id_poligono);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR");
			
			stm.setLong(1, id_poligono);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				pol = new PoligonoDTO();
				pol.setId_poligono(rs.getLong("ID_POLIGONO"));
				pol.setId_comuna(rs.getLong("ID_COMUNA"));
				pol.setNum_poligono(rs.getLong("NUM_POLIGONO"));
				pol.setDescripcion(rs.getString("DESCRIPCION"));
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
		return pol;
	}

	
	/**
	 * Obtiene Poligono de despacho para una dirección
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public PoligonoDTO getPoligonoByIdDireccion(long id_direccion) throws PoligonosDAOException {

	    PoligonoDTO pol = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT P.ID_POLIGONO, P.NUM_POLIGONO, P.DESCRIPCION, P.ID_COMUNA "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN FODBA.FO_DIRECCIONES D ON D.ID_POLIGONO = P.ID_POLIGONO "
                   + "WHERE D.DIR_ID = ?";
		
		logger.debug("SQL (getPoligonoByIdDireccion): " + SQL);
		logger.debug("id_direccion: " + id_direccion);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL  + " WITH UR" );
			
			stm.setLong(1, id_direccion);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				pol = new PoligonoDTO();
				pol.setId_poligono(rs.getLong("ID_POLIGONO"));
				pol.setId_comuna(rs.getLong("ID_COMUNA"));
				pol.setNum_poligono(rs.getLong("NUM_POLIGONO"));
				pol.setDescripcion(rs.getString("DESCRIPCION"));
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			
			}
		}
		return pol;
	}

	
	/**
	 * Obtiene Cantidad de Poligonos Distintos al 0 (Base) y 999 (Retiro en Local)
	 * 
	 * @param  id_comuna long 
	 * @return List of PoligonoxComunaDTO
	 * @throws PoligonosDAOException
	 */
	public long getCantidadPoligonoDifBaseyRetiroLocal(long id_comuna) throws PoligonosDAOException {

	    long cantidad = 0L;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQL = "SELECT COUNT(*) AS CANTIDAD "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "WHERE P.ID_COMUNA = ? "
                   + "  AND P.NUM_POLIGONO <> 0 "
                   + "  AND P.NUM_POLIGONO <> " + Constantes.NUM_POLIGONO_RETIRO_LOCAL
                   + "  AND P.ID_ZONA IS NOT NULL";
		
		logger.debug("SQL (getComunaConMasDeUnPoligono): " + SQL);
		logger.debug("id_comuna: " + id_comuna);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR"  );
			
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getLong("CANTIDAD");
			}

		}catch (SQLException e) {
			throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
		return cantidad;
	}
	public long AddPoligonoWithZona(PoligonoDTO pol) throws PoligonosDAOException {
        PreparedStatement stm = null;
        ResultSet results     = null;
        long id_poligono      = 0L;
        
        String SQL = "INSERT INTO BODBA.BO_POLIGONO (ID_ZONA, ID_COMUNA, NUM_POLIGONO, DESCRIPCION) VALUES (?, ?, ?, ?)";
        
        logger.debug("SQL (AddPoligono): " + SQL);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            //stm = conn.prepareStatement(sql);
            stm = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, pol.getId_zona());
            stm.setLong(2, pol.getId_comuna());
            stm.setLong(3, pol.getNum_poligono());            
            if (pol.getDescripcion().length() > 255){
                stm.setString(4, pol.getDescripcion().substring(0, 255));
            }else{
                stm.setString(4, pol.getDescripcion());
            }
            
            stm.executeUpdate();
            
			results = stm.getGeneratedKeys();
			if (results.next()) {				
				id_poligono = results.getLong(1);
				logger.debug("id_poligono insertado: " + id_poligono);
			}			


        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				results = null;
				
			}
		}
        return id_poligono;
    }




    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public long AddPoligono(PoligonoDTO pol) throws PoligonosDAOException {
        PreparedStatement stm = null;
        ResultSet results     = null;
        long id_poligono      = 0L;
        
        String SQL = "INSERT INTO BODBA.BO_POLIGONO (ID_COMUNA, NUM_POLIGONO, DESCRIPCION) VALUES (?, ?, ?)";
        
        logger.debug("SQL (AddPoligono): " + SQL);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            //stm = conn.prepareStatement(sql);
            stm = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, pol.getId_comuna());
            stm.setLong(2, pol.getNum_poligono());
            if (pol.getDescripcion().length() > 255){
                stm.setString(3, pol.getDescripcion().substring(0, 255));
            }else{
                stm.setString(3, pol.getDescripcion());
            }
            
            stm.executeUpdate();
            
			results = stm.getGeneratedKeys();
			if (results.next()) {				
				id_poligono = results.getLong(1);
				logger.debug("id_poligono insertado: " + id_poligono);
			}			


        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				results = null;
			}
		}
        return id_poligono;
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public boolean ModPoligono(PoligonoDTO pol) throws PoligonosDAOException {
        PreparedStatement stm = null;
        boolean actualizado = false;
        int numreg = 0;
        
        String SQL = "UPDATE BODBA.BO_POLIGONO P "
                   + "   SET P.NUM_POLIGONO = ?, "
                   + "       P.DESCRIPCION  = ? "
                   + "WHERE P.ID_POLIGONO = ?";
        
        logger.debug("SQL (ModPoligono): " + SQL);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL);
            stm.setLong(1, pol.getNum_poligono());
            if (pol.getDescripcion().length() > 255){
                stm.setString(2, pol.getDescripcion().substring(0, 255));
            }else{
                stm.setString(2, pol.getDescripcion());
            }
            stm.setLong(3, pol.getId_poligono());
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {				
				logger.debug("id_poligono " + pol.getId_poligono() + " actualizado");
				actualizado = true;
			}		

        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				
			}
		}
        return actualizado;
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public int VerificaPoligonoEnDirecciones(long id_poligono) throws PoligonosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int numreg = 0;
        
        String SQL = "SELECT COUNT(D.DIR_ID) AS CANTIDAD "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN FODBA.FO_DIRECCIONES D ON D.ID_POLIGONO = P.ID_POLIGONO "
                   + "WHERE P.ID_POLIGONO = ?";
        logger.debug("SQL (VerificaPoligonoEnDirecciones): " + SQL);
        
        try {
            conn = this.getConnection();
            
            stm = conn.prepareStatement(SQL + " WITH UR" );
            stm.setLong(1, id_poligono);
            
            rs = stm.executeQuery();
            
            
			if (rs.next()) {
			    numreg = rs.getInt("CANTIDAD");
				logger.debug("Se encontraron " + numreg + " Direcciones enlazadas al Poligono ID: " + id_poligono);
			}else{
				logger.debug("No Se encontraron Direcciones enlazadas al Poligono ID: " + id_poligono);
			}

        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				
			}
		}
        return numreg;
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public boolean DelPoligono(long id_poligono) throws PoligonosDAOException {
        PreparedStatement stm = null;
        boolean eliminado = false;
        int numreg = 0;
        
        String SQL = "DELETE FROM BODBA.BO_POLIGONO WHERE ID_POLIGONO = ?";
        
        logger.debug("SQL (DelPoligono): " + SQL);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL);
            stm.setLong(1, id_poligono);
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("El Poligono ID: " + id_poligono + " fue Eliminado.");
				eliminado = true;
			}		

        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				
			}
		}
        return eliminado;
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public boolean DelPoligonoPorZona(long id_poligono, long id_zona) throws PoligonosDAOException {
        PreparedStatement stm = null;
        boolean eliminado = false;
        int numreg = 0;
        
        String SQL = "UPDATE BODBA.BO_POLIGONO P "
                   + "   SET P.ID_ZONA = NULL "
                   + "WHERE P.ID_POLIGONO = ? "
                   + "  AND P.ID_ZONA = ?";
        
        logger.debug("SQL (DelPoligonoPorZona): " + SQL);
        
        try {
             conn = this.getConnection();
             stm = conn.prepareStatement(SQL);
            stm.setLong(1, id_poligono);
            stm.setLong(2, id_zona);
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("El Poligono ID: " + id_poligono + " fue Liberado de la Zona " + id_zona + ".");
				eliminado = true;
			}		
        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			
			}
		}
        return eliminado;
    }


    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public boolean AddPoligonoPorZona(long id_poligono, long id_zona) throws PoligonosDAOException {
        PreparedStatement stm = null;
        boolean eliminado = false;
        int numreg = 0;
        
        String SQL = "UPDATE BODBA.BO_POLIGONO P "
                   + "   SET P.ID_ZONA = ? "
                   + "WHERE P.ID_POLIGONO = ?";
        
        logger.debug("SQL (DelPoligonoPorZona): " + SQL);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL);
            stm.setLong(1, id_zona);
            stm.setLong(2, id_poligono);
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("El Poligono ID: " + id_poligono + " fue Asociado a la Zona " + id_zona + ".");
				eliminado = true;
			}		
        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{

				stm=null;
				
			}
		}
        return eliminado;
    }



    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public int getLocalByPoligono(int id_poligono) throws PoligonosDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int id_local = 0;
        
        String SQL = "SELECT Z.ID_LOCAL "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                   + "WHERE ID_POLIGONO = ?";
        
        logger.debug("SQL (getLocalByPoligono): " + SQL);
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement(SQL + " WITH UR" );
            stm.setLong(1, id_poligono);
            rs = stm.executeQuery();
  		if (rs.next()) {
			    id_local = rs.getInt("ID_LOCAL");
				logger.debug("El Local " + id_local + " esta asociado al Poligono ID: " + id_poligono);
			}		

        }catch (SQLException e) {
            throw new PoligonosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
				
			}
		}
        return id_local;
    }


	/**
	 * Actualiza un sector de picking
	 * 
	 * @param  sector SectorLocalDTO 
	 */	
	/*public void doActualizaSectorPicking(SectorLocalDTO sector) throws SectorPickingDAOException {
		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = "UPDATE bo_sector " +
				" set nombre 		= ? , " +
				" max_prod 			= ? , " +
				" max_op 			= ? , " +
				" min_op_fill 		= ? , " +
				" cant_min_prods    = ?  " +
				" where id_sector 	= ? ";
		logger.debug(SQLStmt);
		try {			

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setString(1,sector.getNombre());						
			stm.setLong(2,sector.getMax_prod());
			stm.setLong(3,sector.getMax_op());
			stm.setLong(4,sector.getMin_op_fill());
			stm.setLong(5,sector.getCant_min_prods());
			stm.setLong(6,sector.getId_sector());
			logger.debug("nombre:"+sector.getNombre());			
			logger.debug("max_prod:"+sector.getMax_prod());
			logger.debug("max_op:"+sector.getMax_op());
			logger.debug("min_op_fill:"+sector.getMin_op_fill());
			logger.debug("id_sector:"+sector.getId_sector());
			logger.debug("cant_min_prods:"+sector.getCant_min_prods());
			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.debug("Problema en doActualizaSectorPicking:"+ e);
			throw new SectorPickingDAOException(e);
		}finally {
            try {
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
	}*/
}
