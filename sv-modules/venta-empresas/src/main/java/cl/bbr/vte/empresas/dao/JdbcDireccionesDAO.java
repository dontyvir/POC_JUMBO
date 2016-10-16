package cl.bbr.vte.empresas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import cl.bbr.fo.clientes.exception.ClientesDAOException;
//import cl.bbr.fo.regiones.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.RegionEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.model.ZonaEntity;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcDireccionesDAO implements DireccionesDAO{
	
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
	 * @throws DireccionesDAOException
	 */
	public void setTrx(JdbcTransaccion trx)	throws DireccionesDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new DireccionesDAOException(e);
		}
	}
	
	//Constructor
	public JdbcDireccionesDAO(){
		
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getTiposCalleAll()
	 */
	public List getTiposCalleAll() throws DireccionesDAOException {
		List result = new ArrayList();
		
		TipoCalleEntity entidad = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
			String sql = "select tip_id, tip_nombre, tip_estado "
				+ "FROM fo_tipo_calle where tip_estado = 'A' ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				entidad = new TipoCalleEntity();
				entidad.setId(new Long(rs.getString("tip_id")));
				entidad.setNombre(rs.getString("tip_nombre"));
				entidad.setEstado(rs.getString("tip_estado"));
				result.add(entidad);
			}
			
		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTiposCalleAll - Problema SQL (close)", e);
			}
		}
		logger.debug("tam. result ? "+result.size());
		
		return result;
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getComunas()
	 */
	public List getComunas() throws DireccionesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT id_comuna, C.reg_id reg_id, nombre , reg_nombre" +
					" FROM bo_comunas C " +
					" Join bo_regiones R ON R.reg_id = C.reg_id ";
			conn = this.getConnection();
			stm = conn
					.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("id_comuna")));
				com.setId_region(new Long(rs.getString("reg_id")));
				com.setNombre(rs.getString("nombre"));
				com.setReg_nombre(rs.getString("reg_nombre"));
				lista_comunas.add(com);
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunas - Problema SQL (close)", e);
			}
		}
		return lista_comunas;
	}


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#delDirDespacho(cl.bbr.vte.empresas.dto.DireccionesDTO)
	 */
	public boolean delDirDespacho(DireccionesDTO dto) throws DireccionesDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delDirDespacho:");
		
		try{
			String sql =" UPDATE fo_direcciones set dir_estado = 'E' WHERE dir_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getId());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delDirDespacho - Problema SQL (close)", e);
			}
		}
		return result;
	}	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getDireccionesDespByIdDesp(long)
	 */
	public DireccionEntity getDireccionesDespByIdDesp(long direccion_id) throws DireccionesDAOException {
		DireccionEntity dir = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			conn = this.getConnection();
            String SQL = "SELECT D.DIR_ID, Z.ID_LOCAL, D.DIR_COM_ID, D.DIR_TIP_ID, D.DIR_CLI_ID, D.DIR_ALIAS, " 
                       + "       D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, D.DIR_COMENTARIOS, D.DIR_ESTADO, " 
                       + "       D.DIR_FEC_CREA, D.DIR_FNUEVA, D.DIR_CUADRANTE, D.DIR_CIUDAD, "
                       + "       D.DIR_FAX, D.DIR_OTRA_COMUNA, C.NOMBRE AS NOM_COMUNA, C.REG_ID, R.REG_NOMBRE, "
                       + "       R.REG_NUMERO, D.ID_POLIGONO, P.DESCRIPCION AS DESC_POLIGONO, "
                       + "       Z.ID_ZONA, Z.NOMBRE AS NOM_ZONA "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN BODBA.BO_COMUNAS       C ON C.ID_COMUNA   = D.DIR_COM_ID "
                       + "     JOIN BODBA.BO_REGIONES      R ON R.REG_ID      = C.REG_ID "
                       + "     LEFT JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     LEFT JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "WHERE D.DIR_ID = ? ";
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1,direccion_id);
			logger.debug("SQL query: " + SQL);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				dir = new DireccionEntity();
				dir.setId(new Long(rs.getLong("dir_id")));
				dir.setLoc_cod(new Long(rs.getLong("ID_LOCAL")));
				dir.setNom_comuna(rs.getString("NOM_COMUNA"));
				dir.setCom_id(new Long(rs.getLong("dir_com_id")));
				dir.setTip_id(new Long(rs.getLong("dir_tip_id")));
				dir.setCli_id(new Long(rs.getLong("dir_cli_id")));
				dir.setAlias( rs.getString("dir_alias") );
				dir.setCalle( rs.getString("dir_calle") );
				dir.setNumero( rs.getString("dir_numero") );
				dir.setDepto( rs.getString("dir_depto") );
				dir.setComentarios( rs.getString("dir_comentarios") );
				if (rs.getTimestamp("dir_fec_crea") != null)
					dir.setFec_crea(rs.getTimestamp("dir_fec_crea"));
				dir.setEstado(rs.getString("dir_estado"));
				dir.setZona_id(new Long(rs.getLong("ID_ZONA")));
				dir.setFnueva(rs.getString("dir_fnueva"));
				dir.setCuadrante(rs.getString("dir_cuadrante"));
				dir.setCiudad(rs.getString("dir_ciudad"));
				dir.setFax(rs.getString("dir_fax"));
				dir.setOtra_comuna(rs.getString("dir_otra_comuna"));
				dir.setReg_id(new Long(rs.getLong("REG_ID")));
				dir.setNom_region(rs.getString("REG_NOMBRE"));
				if (rs.getString("ID_POLIGONO") != null){
				    dir.setId_poligono(rs.getInt("ID_POLIGONO"));
				}else{
				    dir.setId_poligono(0);
				}
			}
			
		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDireccionesDespByIdDesp - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return dir;
	}	
	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getLocalDireccion(long)
	 */
	public long getLocalDireccion(long direccion_id) throws DireccionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		long id_local = 0L; 
		
		try {

			conn = this.getConnection();
            String SQL = "SELECT Z.ID_LOCAL "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     LEFT JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     LEFT JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "WHERE D.DIR_ID = ? ";
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1,direccion_id);
			logger.debug("SQL query: " + SQL);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				id_local = rs.getLong("ID_LOCAL");
			}
		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalDireccion - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return id_local;
	}	
	


	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getRegiones()
	 */
	public List getRegiones() throws DireccionesDAOException {
		List lista_regiones = new ArrayList();
		RegionEntity reg = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "select reg_id, reg_nombre, reg_numero FROM bo_regiones";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				reg = new RegionEntity();
				reg.setId(new Long( rs.getString("reg_id") ));
				reg.setNombre( rs.getString("reg_nombre") );
				reg.setNumero( new Long(rs.getString("reg_numero")) );
				lista_regiones.add(reg);
			}
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getRegiones - Problema SQL (close)", e);
			}
		}
		return lista_regiones;
	}
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getAllComunas(long)
	 */
	public List getAllComunas( long reg_id ) throws DireccionesDAOException {
		List lista = new ArrayList();
		ComunaEntity entidad = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			String SQL = "SELECT DISTINCT C.ID_COMUNA, C.REG_ID, C.NOMBRE "
                       + "FROM BODBA.BO_COMUNAS C "
                       + "     JOIN BODBA.BO_POLIGONO P ON P.ID_COMUNA = C.ID_COMUNA AND "
                       + "                                 P.ID_ZONA IS NOT NULL "
                       + "WHERE REG_ID = ? "
                       + "ORDER BY NOMBRE";
			
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, reg_id);
			
			logger.debug("SQL: " + stm.toString());			

			rs = stm.executeQuery();
			while (rs.next()) {
				
				entidad = new ComunaEntity();
				entidad.setId_comuna( new Long(rs.getLong("id_comuna")) );
				entidad.setNombre( rs.getString("nombre") );
				entidad.setId_region( new Long(rs.getLong("reg_id")) );
				lista.add(entidad);
			}
		} catch (Exception e) {
			logger.error( "(getAllComunas) Problema", e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getAllComunas - Problema SQL (close)", e);
			}
		}
		return lista;	
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getListDireccionDespBySucursal(long)
	 */
	public List getListDireccionDespBySucursal(long id_sucursal) throws DireccionesDAOException {
		List lista = new ArrayList();
		DireccionesDTO dto = null;
		//DireccionEntity entidad = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();

			String SQL = "SELECT D.DIR_ID, T.TIP_NOMBRE, D.DIR_CALLE, D.DIR_NUMERO, D.DIR_DEPTO, "
                       + "       C.NOMBRE AS NOM_COMUNA, R.REG_NOMBRE, L.NOM_LOCAL, D.DIR_ALIAS, "
                       + "       P.ID_POLIGONO, P.ID_ZONA, Z.NOMBRE AS NOM_ZONA "
                       + "FROM FODBA.FO_DIRECCIONES D "
                       + "     JOIN FODBA.FO_TIPO_CALLE    T ON T.TIP_ID      = D.DIR_TIP_ID "
                       + "     JOIN BODBA.BO_COMUNAS       C ON C.ID_COMUNA   = D.DIR_COM_ID "
                       + "     LEFT JOIN BODBA.BO_REGIONES R ON R.REG_ID      = C.REG_ID "
                       + "     LEFT JOIN BODBA.BO_POLIGONO P ON P.ID_POLIGONO = D.ID_POLIGONO "
                       + "     LEFT JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA     = P.ID_ZONA "
                       + "     JOIN BODBA.BO_LOCALES       L ON L.ID_LOCAL    = Z.ID_LOCAL "
                       + "WHERE D.DIR_CLI_ID = ? "
                       + "  AND D.DIR_ESTADO='"+Constantes.ESTADO_ACTIVADO+"'";
			stm = conn.prepareStatement( SQL + " WITH UR");
			stm.setLong(1,id_sucursal);
			logger.debug("SQL (getListDireccionDespBySucursal): " + SQL);
			logger.debug("id_sucursal: " + id_sucursal);

			rs = stm.executeQuery();
			
			while (rs.next()) {
				dto = new DireccionesDTO();
				dto.setId(rs.getLong("dir_id"));
				dto.setNom_tip_calle(rs.getString("tip_nombre"));
				dto.setCalle(rs.getString("dir_calle"));
				dto.setNumero(rs.getString("dir_numero"));
				dto.setDepto(rs.getString("dir_depto"));
				dto.setNom_comuna(rs.getString("NOM_COMUNA"));
				dto.setReg_nombre(rs.getString("reg_nombre"));
				dto.setNom_local(rs.getString("nom_local"));
				dto.setAlias( rs.getString("dir_alias") );
				dto.setId_poligono(rs.getInt("id_poligono"));
				dto.setZona_id(rs.getLong("ID_ZONA"));
				dto.setNom_zona(rs.getString("NOM_ZONA"));

				lista.add(dto);
			}

		} catch (Exception e) {
			logger.error( "(getAllComunas) Problema", e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListDireccionDespBySucursal - Problema SQL (close)", e);
			}
		}
		return lista;	
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#modDirDespacho(cl.bbr.vte.empresas.dto.DireccionesDTO)
	 */
	public boolean modDirDespacho(DireccionesDTO direccion) throws DireccionesDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en modDirDespacho:");
		int id_poligono = 0;
		try{
		    if (direccion.getId_poligono() == 0.0){
				id_poligono = this.getPoligonoCero(direccion.getCom_id());
				direccion.setId_poligono(id_poligono);
		    }

		   String  SQL = "UPDATE FODBA.FO_DIRECCIONES "
		        + "   SET ID_POLIGONO     = ?, "
		        + "       DIR_COM_ID      = ?, "
		        + "       DIR_TIP_ID      = ?, " 
		        + "       DIR_CLI_ID      = ?, "
		        + "       DIR_ALIAS       = ?, "
		        + "       DIR_CALLE       = ?, "
		        + "       DIR_NUMERO      = ?, "
		        + "       DIR_DEPTO       = ?, "
		        + "       DIR_COMENTARIOS = ?, " 
		        + "       DIR_FNUEVA      = ?, "
		        + "       DIR_OTRA_COMUNA = ?, "
		        + "       CONFIRMADA      = 0 "
		        + " WHERE DIR_ID = ? ";
			logger.debug("SQL (modDirDespacho):"+ SQL);
			logger.info("DIR_ID : " + direccion.getId());
			
			conn = this.getConnection();
			stm = conn.prepareStatement(SQL);
			//stm.setLong(1, direccion.getLoc_cod());
			int i=1;
			stm.setLong(i++, direccion.getId_poligono());
			stm.setLong(i++, direccion.getCom_id());
			stm.setLong(i++, direccion.getTip_id());
			stm.setLong(i++, direccion.getCli_id());
			stm.setString(i++, Utils.stringToDb(direccion.getAlias(),20));
			stm.setString(i++, Utils.stringToDb(direccion.getCalle(),200));
			stm.setString(i++, Utils.stringToDb(direccion.getNumero(),20));
			stm.setString(i++, Utils.stringToDb(direccion.getDepto(),20));
			if( direccion.getComentarios() != null )
				stm.setString(i++, Utils.stringToDb(direccion.getComentarios(),2000));
			else
				stm.setString(i++, "" );
			stm.setString(i++, direccion.getFnueva());
			stm.setString(i++, direccion.getOtra_comuna());
			stm.setLong(i++, direccion.getId());			
			int numReg= stm.executeUpdate();
			logger.debug("numReg: "+numReg);
			if(numReg>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modDirDespacho - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getLocalComuna(long)
	 */
	public String getLocalComuna( long comuna ) throws DireccionesDAOException  {
		String local = null;
		PreparedStatement stm=null;
		ResultSet rs=null;		
		try {

			conn = this.getConnection();

			String SQL = "SELECT DISTINCT Z.ID_LOCAL, Z.ORDEN_ZONA "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "     JOIN BODBA.BO_ZONAS Z ON Z.ID_ZONA = P.ID_ZONA "
                       + "WHERE P.ID_COMUNA = ? "
                       + "ORDER BY Z.ORDEN_ZONA";
			stm = conn.prepareStatement(SQL + " WITH UR");
			stm.setLong(1,comuna);
			logger.debug("SQL query (getLocalComuna): " + SQL);
			
			rs = stm.executeQuery();
			if(rs.next()) {
				local = rs.getString("ID_LOCAL");
			}
			else {
				throw new DireccionesDAOException( "No existe zona para la comuna" );
			}
			
		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalComuna - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return local;
	}

	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.clientes.dao.ClientesDAO#getLocalComuna(long)
	 */
	public int getPoligonoCero( long comuna ) throws DireccionesDAOException {

		Connection conexion = null;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		int poligono = 0;
		
		try {
			conexion = JdbcDAOFactory.getConexion();
			String SQL = "SELECT DISTINCT P.ID_POLIGONO "
                       + "FROM BODBA.BO_POLIGONO P "
                       + "WHERE P.ID_COMUNA = ? "
                       + "  AND P.NUM_POLIGONO = 0";
			
			stm = conexion.prepareStatement(SQL + " WITH UR");
                    
			stm.setLong(1, comuna);
			logger.debug("SQL (getPoligono): " + SQL);
			logger.debug("id_comuna: " + comuna);
			rs = stm.executeQuery();
			if ( rs.next() ) {
				logger.logData( "getPoligono", rs);
				poligono = rs.getInt("ID_POLIGONO");
			}
		} catch (SQLException ex) {
			logger.error("getLocalComuna - Problema SQL", ex);
			throw new DireccionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getLocalComuna - Problema General", ex);
			throw new DireccionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();	
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getPoligonoCero - Problema SQL (close)", e);
			}
		}
		return poligono;
	}



	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#insDirDespacho(cl.bbr.vte.empresas.dto.DireccionesDTO)
	 */
	public long insDirDespacho(DireccionesDTO dto) throws DireccionesDAOException {
		long id = -1;
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en insDirDespacho:");
		try{
				//String local = "";
		        int id_poligono = 0;
				// Recuperar local por defecto para la comuna de la dirección
				//local = this.getLocalComuna( dto.getCom_id() );
				if (dto.getId_poligono() == 0.0){
				    id_poligono = this.getPoligonoCero(dto.getCom_id());
				    dto.setId_poligono(id_poligono);
				}

				
				String sql2 =" INSERT INTO fo_direcciones (id_poligono, dir_com_id, dir_tip_id, dir_cli_id, dir_alias, " +
						" dir_calle, dir_numero, dir_depto, dir_comentarios, dir_estado, dir_fnueva, dir_region_id, dir_otra_comuna, dir_fec_crea) "
							+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP )";
				logger.debug("SQL (insDirDespacho): " + sql2);
				
				conn = this.getConnection();
				stm = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				stm.setLong(i++, dto.getId_poligono());
				stm.setLong(i++, dto.getCom_id());
				stm.setLong(i++, dto.getTip_id());
				stm.setLong(i++, dto.getCli_id());
				stm.setString(i++, Utils.stringToDb(dto.getAlias(),20));
				stm.setString(i++, Utils.stringToDb(dto.getCalle(),200));
				stm.setString(i++, Utils.stringToDb(dto.getNumero(),20));
				stm.setString(i++, Utils.stringToDb(dto.getDepto(),20));
				if( dto.getComentarios() != null )
					stm.setString(i++, Utils.stringToDb(dto.getComentarios(),255));
				else
					stm.setString(i++, "" );
				stm.setString(i++, Utils.stringToDb(dto.getEstado() + "",1));
				stm.setString(i++, "N");
				stm.setLong(i++, dto.getReg_id());
				stm.setString(i++, dto.getOtra_comuna());
				logger.debug("SQL: " + stm.toString());
				
				stm.executeUpdate();
				
				rs = stm.getGeneratedKeys();
				if (rs.next()) {
		            id = rs.getInt(1);
		            logger.debug("id:"+id);
		        }

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insDirDespacho - Problema SQL (close)", e);
			}
		}

		return id;
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getTiposCalleById(long)
	 */
	public TipoCalleEntity getTiposCalleById(long id_tipo_calle) throws DireccionesDAOException {
		TipoCalleEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT tip_id, tip_nombre, tip_estado " +
					" FROM fo_tipo_calle " +
					" WHERE tip_id = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_tipo_calle);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new TipoCalleEntity();
				result.setId(new Long(rs.getLong("tip_id")));
				result.setNombre(rs.getString("tip_nombre"));
				result.setEstado(rs.getString("tip_estado"));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTiposCalleById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getComunaById(long)
	 */
	public ComunaEntity getComunaById(long id_comuna) throws DireccionesDAOException {
		ComunaEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT id_comuna, reg_id, nombre  " +
					" FROM bo_comunas " +
					" WHERE id_comuna = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_comuna);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new ComunaEntity();
				result.setId_comuna(new Long(rs.getLong("id_comuna")));
				result.setId_region(new Long(rs.getLong("reg_id")));
				result.setNombre(rs.getString("nombre"));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunaById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getComunaFactById(long)
	 */
	public ComunaEntity getComunaFactById(long comuna_id) throws DireccionesDAOException {
		ComunaEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT COMUNA_ID, REG_ID, NOMBRE  " 
                       + "FROM BO_COMUNAS_GENERAL " 
                       + "WHERE COMUNA_ID = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, comuna_id);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new ComunaEntity();
				result.setId_comuna(new Long(rs.getLong("comuna_id")));
				result.setId_region(new Long(rs.getLong("reg_id")));
				result.setNombre(rs.getString("nombre"));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunaFactById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getRegionById(long)
	 */
	public RegionEntity getRegionById(long id_region) throws DireccionesDAOException {
		RegionEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT reg_id, reg_nombre, reg_numero   " +
					" FROM bo_regiones " +
					" WHERE reg_id = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_region);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new RegionEntity();
				result.setId(new Long(rs.getLong("reg_id")));
				result.setNombre(rs.getString("nombre"));
				result.setNumero(new Long(rs.getLong("reg_numero")));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getRegionById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getLocalById(long)
	 */
	public LocalEntity getLocalById(long id_local) throws DireccionesDAOException {
		LocalEntity result = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios, cod_local_pos " +
					" FROM bo_locales " +
					" WHERE id_local = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_local);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new LocalEntity();
				result.setId_local(new Long(rs.getLong("id_local")));
				result.setCod_local(rs.getString("cod_local"));
				result.setNom_local(rs.getString("nom_local"));
				result.setOrden(new Integer(rs.getInt("orden")));
				result.setFec_carga_prec(rs.getTimestamp("fecha_carga_precios"));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getZonaById(long)
	 */
	public ZonaEntity getZonaById(long id_zona) throws DireccionesDAOException {
		ZonaEntity zona = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {

			conn = this.getConnection();
			String sql = "SELECT id_zona, id_local, nombre, descripcion, tarifa_normal_baja, tarifa_normal_media, "
                       + "       tarifa_normal_alta, tarifa_economica, tarifa_express "
                       + "FROM bo_zonas "
                       + "WHERE id_zona = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_zona);
			logger.debug("SQL query: " + sql);
	
			rs = stm.executeQuery();
			if (rs.next()) {
			    zona = new ZonaEntity();
			    zona.setId_zona(new Long(rs.getLong("id_zona")));
			    zona.setId_local(new Long(rs.getLong("id_local")));
			    zona.setNombre(rs.getString("nombre"));
			    zona.setDescrip(rs.getString("descripcion"));
			    zona.setTarifa_normal_baja(rs.getInt("tarifa_normal_baja"));
			    zona.setTarifa_normal_media(rs.getInt("tarifa_normal_media"));
			    zona.setTarifa_normal_alta(rs.getInt("tarifa_normal_alta"));
			    zona.setTarifa_economica(rs.getInt("tarifa_economica"));
			    zona.setTarifa_express(rs.getInt("tarifa_express"));
			}

		}catch (SQLException e) {
			throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getZonaById - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return zona;
	}


	public List getListDireccionDespByComprador(long comprador_id) throws DireccionesDAOException {
		List lista = new ArrayList();
		DireccionesDTO dto = null;
		//DireccionEntity entidad = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT distinct(dir.dir_id), dir.dir_cli_id, dir.dir_alias, dir.dir_calle, dir.dir_numero, " +
					" dir.dir_depto, com.nombre, tip.tip_nombre, R.reg_nombre from ve_comprxsuc comx join fo_direcciones " +
					" dir on dir.dir_cli_id = comx.cli_id join bo_comunas com on dir.dir_com_id = com.id_comuna " +
					" join fo_tipo_calle tip on tip.tip_id = dir.dir_tip_id " + 
					" left join   bo_regiones R ON R.reg_id = dir.dir_region_id " + 
					" where cpr_id = ? and dir.dir_estado <> 'E' order by dir.dir_cli_id ";
			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setLong(1,comprador_id);
			logger.debug("SQL: " + sql);
			logger.debug("comprador_id: " + comprador_id);

			rs = stm.executeQuery();
			
			while (rs.next()) {
				dto = new DireccionesDTO();
				dto.setCli_id(rs.getLong("dir_cli_id"));
				dto.setId(rs.getLong("dir_id"));
				dto.setAlias( rs.getString("dir_alias") );
				dto.setCalle(rs.getString("dir_calle"));
				dto.setNumero(rs.getString("dir_numero"));
				dto.setDepto(rs.getString("dir_depto"));
				dto.setNom_comuna(rs.getString("nombre"));
				dto.setNom_tip_calle(rs.getString("tip_nombre"));
				dto.setNom_region(rs.getString("reg_nombre"));
				lista.add(dto);
			}

		} catch (Exception e) {
			logger.error( "(getListDireccionDespByComprador) Problema", e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListDireccionDespByComprador - Problema SQL (close)", e);
			}
		}
		return lista;	
	}	
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DireccionesDAO#getComunasGeneral()
	 */
	public List getComunasGeneral() throws DireccionesDAOException {
		List lista_comunas = new ArrayList();
		ComunaEntity com = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT comuna_id, C.reg_id, nombre, reg_nombre " +
					" FROM BO_COMUNAS_GENERAL C " +
					" Join bo_regiones R ON R.reg_id = C.reg_id ";			
			logger.debug("sql : "+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				com = new ComunaEntity();
				com.setId_comuna(new Long(rs.getString("comuna_id")));
				com.setId_region(new Long(rs.getLong("reg_id")));
				com.setNombre(rs.getString("nombre"));
				com.setReg_nombre(rs.getString("reg_nombre"));
				lista_comunas.add(com);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComunasGeneral - Problema SQL (close)", e);
			}
		}
		return lista_comunas;
	}
	

    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public int getLocalByPoligono(int id_poligono) throws DireccionesDAOException {
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
            stm = conn.prepareStatement(SQL + " WITH UR");
            stm.setLong(1, id_poligono);
            
            rs = stm.executeQuery();
            
			if (rs.next()) {
			    id_local = rs.getInt("ID_LOCAL");
				logger.debug("El Local " + id_local + " esta asociado al Poligono ID: " + id_poligono);
			}		

        }catch (SQLException e) {
            throw new DireccionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocalByPoligono - Problema SQL (close)", e);
			}
		}
        return id_local;
    }


}
