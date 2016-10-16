package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.model.CampanaEntity;
import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CampanaDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las campañas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcCampanaDAO implements CampanaDAO{
	
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
	 * @return Connection
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
	* @throws CampanaDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws CampanaDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CampanaDAOException(e);
		}
	}


	
	/**
	 * Obtiene la campaña, segun el código
	 * 
	 * @param  id_campana
	 * @return CampanaEntity
	 * @throws CampanaDAOException
	 */
	public CampanaEntity getCampanaById(long id_campana) throws CampanaDAOException{
		
		CampanaEntity camp = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCampanaById:");

		try {
			String sql = " SELECT cam_id, cam_nombre, cam_descripcion, cam_estado, cam_fec_crea " +
				" FROM fo_mar_campana " +
				" WHERE cam_id = ? ";
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_campana);
			rs = stm.executeQuery();
			if (rs.next()) {
				camp = new CampanaEntity(); 
				camp.setId_campana(rs.getLong("cam_id"));
				if(rs.getString("cam_nombre") != null){
					camp.setNombre(rs.getString("cam_nombre"));
				}else{
					camp.setNombre("");
				}
				if(rs.getString("cam_descripcion") != null){
					camp.setDescripcion(rs.getString("cam_descripcion"));
				}else{
					camp.setDescripcion("");
				}
				if(rs.getString("cam_estado") != null){
					camp.setEstado(rs.getString("cam_estado"));
				}else{
					camp.setEstado("");
				}
				camp.setFec_creacion(rs.getTimestamp("cam_fec_crea"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCampanaById - Problema SQL (close)", e);
			}
		}
		return camp;
	}

	/**
	 * Obtener el listado de campañas, segun los criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List CampanaEntity
	 * @throws CampanaDAOException
	 */
	public List getCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaDAOException{
		List lst_camp = new ArrayList();
		CampanaEntity camp = null;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getCampanasByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de campana
		String nom_cam = criterio.getNombre();
		String sqlNomCam = "";
		if (!nom_cam.equals(""))
			sqlNomCam = " AND upper(cam_nombre) like \'"+nom_cam.toUpperCase()+"%\'";
		//numero de campana
		String num_cam = criterio.getNumero();
		String sqlNumCam = "";
		if (!num_cam.equals(""))
			sqlNumCam = " AND cam_id = "+num_cam;
		//segun estado
		char estado = criterio.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND cam_estado = \'"+estado+"\'";

		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {

			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by UPPER(C.cam_nombre)) as row," +
				" C.cam_id as cam_id, cam_nombre, cam_descripcion, cam_estado, cam_fec_crea " +
				" FROM fo_mar_campana C " + 
				" WHERE C.cam_id > 0 " +
				sqlNomCam + sqlNumCam + sqlEstado + 
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg ;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm =conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				camp = new CampanaEntity();
				camp.setId_campana(rs.getLong("cam_id"));
				if(rs.getString("cam_nombre") != null){
					camp.setNombre(rs.getString("cam_nombre"));
				}else{
					camp.setNombre("");
				}
				if(rs.getString("cam_descripcion") != null){
					camp.setDescripcion(rs.getString("cam_descripcion"));
				}else{
					camp.setDescripcion("");
				}
				if(rs.getString("cam_estado") != null){
					camp.setEstado(rs.getString("cam_estado"));
				}else{
					camp.setEstado("");
				}
				camp.setFec_creacion(rs.getTimestamp("cam_fec_crea"));
				lst_camp.add(camp);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCampanasByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_camp.size());
		return lst_camp;
	}

	/**
	 * Obtiene la cantidad de campañas, segun el criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws CampanaDAOException
	 */
	public int getCountCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaDAOException{
		int cantidad = 0;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getCountCampanasByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de campana
		String nom_cam = criterio.getNombre();
		String sqlNomCam = "";
		if (!nom_cam.equals(""))
			sqlNomCam = " AND upper(cam_nombre) like \'"+nom_cam.toUpperCase()+"%\'";
		//numero de campana
		String num_cam = criterio.getNumero();
		String sqlNumCam = "";
		if (!num_cam.equals(""))
			sqlNumCam = " AND cam_id = "+num_cam;
		//segun estado
		char estado = criterio.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND cam_estado = \'"+estado+"\'";

		try {
			String sql =" SELECT count(C.cam_id) as cantidad" +
				" FROM fo_mar_campana C " + 
				" WHERE C.cam_id > 0 " +
				sqlNomCam + sqlNumCam + sqlEstado ;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountCampanasByCriteria - Problema SQL (close)", e);
			}
		}
		return cantidad;

	}

	/**
	 * Obtiene el listado de elementos de una campaña
	 * 
	 * @param  id_campana
	 * @return List ElementoEntity
	 * @throws CampanaDAOException
	 */
	public List getElementosByCampanaId(long id_campana) throws CampanaDAOException {
		List lst_elem = new ArrayList();
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getElementosByCampanaId:");
		
		try{
			String sql =" SELECT ele_id, ele_tip_id, ele_fec_crea, ele_nombre, ele_descripcion, ele_estado, ele_url_destino  , elca_click " +
					" FROM fo_mar_elementos " +
					" JOIN fo_eltos_cam ON elca_ele_id = ele_id AND elca_cam_id = ? " +
					" WHERE ele_id in ( SELECT elca_ele_id FROM fo_eltos_cam WHERE elca_cam_id = ? ) " +
					" ORDER BY ele_nombre ";
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_campana);
			stm.setLong(2,id_campana);
			rs = stm.executeQuery();
			ElementoEntity elem = null; 
			while(rs.next()){
				elem = new ElementoEntity();
				elem.setId_elemento(rs.getLong("ele_id"));
				elem.setId_tipo_elem(rs.getLong("ele_tip_id"));
				if(rs.getString("ele_nombre")!=null){
					elem.setNombre(rs.getString("ele_nombre"));
				}else{
					elem.setNombre("");
				}
				if(rs.getString("ele_descripcion")!=null){
					elem.setDescripcion(rs.getString("ele_descripcion"));
				}else{
					elem.setDescripcion("");
				}
				if(rs.getString("ele_estado")!=null){
					elem.setEstado(rs.getString("ele_estado"));
				}else{
					elem.setEstado("");
				}
				elem.setFec_creacion(rs.getTimestamp("ele_fec_crea"));
				if(rs.getString("ele_url_destino")!=null){
					elem.setUrl_destino(rs.getString("ele_url_destino"));
				}else{
					elem.setUrl_destino("");
				}
				elem.setClicks(rs.getLong("elca_click"));
				lst_elem.add(elem);
			}
		
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getElementosByCampanaId - Problema SQL (close)", e);
			}
		}
		return lst_elem;
	}

	/**
	 * Actualiza informacion de campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setModCampana(ProcModCampanaDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setModCampana:");
		try{
			String sql =" UPDATE fo_mar_campana " +
			" SET cam_nombre = ?, cam_descripcion = ?, cam_estado =? " +
			" WHERE cam_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("nom:"+dto.getNombre());
			logger.debug("desc:"+dto.getDescripcion());
			logger.debug("est:"+dto.getEstado());
			logger.debug("id:"+dto.getId_campana());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,dto.getNombre());
			stm.setString(2,dto.getDescripcion());
			stm.setString(3,dto.getEstado());
			stm.setLong(4,dto.getId_campana());
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModCampana - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Permite agregar la relacion entre campaña y elemento
	 *  
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean addCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en addCampanaElemento:");
		
		try{
			String sql =" INSERT INTO fo_eltos_cam (elca_cam_id, elca_ele_id, elca_click) values(?,?,?) ";
			logger.debug("sql:"+sql);
			logger.debug("id_campana:"+dto.getId_campana());
			logger.debug("id_elemento:"+dto.getId_elemento());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_campana());
			stm.setLong(2,dto.getId_elemento());
			stm.setInt(3,0);
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addCampanaElemento - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/**
	 * Permite eliminar la relacion entre campaña y elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean delCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en delCampanaElemento:");
		
		try{
			String sql =" DELETE FROM fo_eltos_cam WHERE elca_cam_id = ? AND elca_ele_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id_campana:"+dto.getId_campana());
			logger.debug("id_elemento:"+dto.getId_elemento());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_campana());
			stm.setLong(2,dto.getId_elemento());
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delCampanaElemento - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/**
	 * Agrega una nueva campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setAddCampana(ProcAddCampanaDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setAddCampana:");
		
		try{
			String sql =" INSERT INTO fo_mar_campana (cam_nombre, cam_descripcion, cam_estado, cam_fec_crea) " +
					" VALUES (?,?,?,CURRENT TIMESTAMP)";
			logger.debug("sql:"+sql);
			logger.debug("nombre:"+dto.getNombre());
			logger.debug("descripcion:"+dto.getDescripcion());
			logger.debug("estado:"+dto.getEstado());
			//logger.debug("fec_creacion:"+dto.getFec_crea());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,dto.getNombre());
			stm.setString(2,dto.getDescripcion());
			stm.setString(3,dto.getEstado());
			//stm.setString(4,dto.getFec_crea());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setAddCampana - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/**
	 * Elimina la campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setDelCampana(ProcDelCampanaDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setAddCampana:");
		
		try{
			String sql =" DELETE FROM fo_mar_campana WHERE cam_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("cam_id:"+dto.getId_campana());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_campana());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setDelCampana - Problema SQL (close)", e);
			}
		}

		return result;
	}

	/**
	 * Modificar relacion entre campaña y elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean modCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en modCampanaElemento:");
		try{
			String sql =" UPDATE fo_eltos_cam " +
			" SET elca_click = ? " +
			" WHERE elca_ele_id = ? AND elca_cam_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("elca_ele_id:"+dto.getId_elemento());
			logger.debug("elca_cam_id:"+dto.getId_campana());
			logger.debug("elca_click:"+dto.getClicks());
			logger.debug("id:"+dto.getId_campana());
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getClicks());
			stm.setLong(2,dto.getId_elemento());
			stm.setLong(3,dto.getId_campana());
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new CampanaDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modCampanaElemento - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
}
