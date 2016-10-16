package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.model.CampanaEntity;
import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.common.model.TipoElementoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los elementos que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcElementoDAO implements ElementoDAO{
	
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
	* @throws ElementoDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws ElementoDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ElementoDAOException(e);
		}
	}


	
	/**
	 * Obtiene el elemento, segun el código
	 * 
	 * @param  id_elemento
	 * @return ElementoEntity
	 * @throws ElementoDAOException
	 */
	public ElementoEntity getElementoById(long id_elemento) throws ElementoDAOException{
		
		ElementoEntity elem = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getElementoById:");

		try {
			String sql = " SELECT ele_id, ele_tip_id, ele_fec_crea, ele_nombre, ele_descripcion, ele_estado, ele_url_destino " +
				" FROM fo_mar_elementos " +
				" WHERE ele_id = ? ";
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_elemento);
			rs = stm.executeQuery();
			if (rs.next()) {
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
				
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getElementoById - Problema SQL (close)", e);
			}
		}
		return elem;
	}

	/**
	 * Obtener el listado de elementos, segun los criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List ElementoEntity
	 * @throws ElementoDAOException
	 */
	public List getElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoDAOException{
		List lst_elem = new ArrayList();
		ElementoEntity elem = null;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getElementosByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de elemento
		String nom_elem = criterio.getNombre();
		String sqlNomElem = "";
		if (!nom_elem.equals(""))
			sqlNomElem = " AND upper(ele_nombre) like \'"+nom_elem.toUpperCase()+"%\'";
		//numero de elemento
		String num_elem = criterio.getNumero();
		String sqlNumElem = "";
		if (!num_elem.equals(""))
			sqlNumElem = " AND ele_id = "+num_elem;
		//segun estado
		char estado = criterio.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND ele_estado = \'"+estado+"\'";
		//segun el tipo
		String sqlTipo = "";
		long tipo = criterio.getTipo();
		if (tipo>0)
			sqlTipo = " AND ele_tip_id = "+tipo;

		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {

			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by UPPER(ele_nombre)) as row," +
				" ele_id, ele_tip_id, ele_fec_crea, ele_nombre, ele_descripcion, ele_estado, ele_url_destino " +
				" FROM fo_mar_elementos " + 
				" WHERE ele_id > 0 " +
				sqlNomElem + sqlNumElem + sqlEstado + sqlTipo +
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg ;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm =conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
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
				lst_elem.add(elem);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getElementosByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_elem.size());
		return lst_elem;
	}

	/**
	 * Obtiene la cantidad de elementos, segun el criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ElementoDAOException
	 */
	public int getCountElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoDAOException{
		int cantidad = 0;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getCountElementosByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de elemento
		String nom_elem = criterio.getNombre();
		String sqlNomElem = "";
		if (!nom_elem.equals(""))
			sqlNomElem = " AND upper(ele_nombre) like \'"+nom_elem.toUpperCase()+"%\'";
		//numero de elemento
		String num_elem = criterio.getNumero();
		String sqlNumElem = "";
		if (!num_elem.equals(""))
			sqlNumElem = " AND ele_id = "+num_elem;
		//segun estado
		char estado = criterio.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND ele_estado = \'"+estado+"\'";
		//segun el tipo
		String sqlTipo = "";
		long tipo = criterio.getTipo();
		if (tipo>0)
			sqlTipo = " AND ele_tip_id = "+tipo;

		try {
			String sql =" SELECT count(E.ele_id) as cantidad" +
				" FROM fo_mar_elementos E " + 
				" WHERE E.ele_id > 0 " +
				sqlNomElem + sqlNumElem + sqlEstado + sqlTipo;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountElementosByCriteria - Problema SQL (close)", e);
			}
		}
		return cantidad;

	}

	/**
	 * Obtiene el listado de campanias de un elemento
	 * 
	 * @param  id_elemento
	 * @return List CampanaEntity
	 * @throws ElementoDAOException
	 */
	public List getCampanasByElementoId(long id_elemento) throws ElementoDAOException {
		List lst_camp = new ArrayList();
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getCampanasByElementoId:");
		
		try{
			String sql =" SELECT cam_id, cam_nombre, cam_descripcion, cam_estado, cam_fec_crea " +
					" FROM fo_mar_campana" +
					" WHERE cam_id in (SELECT elca_cam_id FROM fo_eltos_cam WHERE elca_ele_id = ? ) " +
					" ORDER BY cam_nombre ";
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_elemento);
			rs = stm.executeQuery();
			CampanaEntity camp = null; 
			while(rs.next()){
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
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCampanasByElementoId - Problema SQL (close)", e);
			}
		}
		return lst_camp;
	}

	/**
	 * Actualiza la información del elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoDAOException
	 */
	public boolean setModElemento(ProcModElementoDTO dto) throws ElementoDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		logger.debug("en setModElemento:");
		try{
			String sql =" UPDATE fo_mar_elementos " +
			" SET ele_nombre = ?, ele_descripcion = ?, ele_estado = ?, ele_url_destino =?, ele_tip_id = ? " +
			" WHERE ele_id = ? ";
			logger.debug("sql:"+sql);
			logger.debug("id:"+dto.getId_elemento());
			logger.debug("nom:"+dto.getNombre());
			logger.debug("desc:"+dto.getDescripcion());
			logger.debug("est:"+dto.getEstado());
			logger.debug("url:"+dto.getUrl_destino());
			logger.debug("tip:"+dto.getTipo());
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,dto.getNombre());
			stm.setString(2,dto.getDescripcion());
			stm.setString(3,dto.getEstado());
			stm.setString(4,dto.getUrl_destino());
			stm.setLong(5,dto.getTipo());
			stm.setLong(6,dto.getId_elemento());
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0){
				result=true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new ElementoDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModElemento - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene el listado de todos los tipos de elementos
	 * 
	 * @return List TipoElementoEntity
	 * @throws ElementoDAOException 
	 */
	public List getLstTipoElementos() throws ElementoDAOException {
		List lst_tip = new ArrayList();
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getLstTipoElementos:");
		
		try{
			String sql =" SELECT tip_id, tip_nombre, tip_estado " +
					" FROM fo_mar_tipo_elemento " ;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			TipoElementoEntity tip = null; 
			while(rs.next()){
				tip = new TipoElementoEntity();
				tip.setId_tipo(rs.getLong("tip_id"));
				if(rs.getString("tip_nombre") != null){
					tip.setNombre(rs.getString("tip_nombre"));
				}else{
					tip.setNombre("");
				}
				if(rs.getString("tip_estado") != null){
					tip.setEstado(rs.getString("tip_estado"));
				}else{
					tip.setEstado("");
				}
				lst_tip.add(tip);
			}
		
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLstTipoElementos - Problema SQL (close)", e);
			}
		}
		return lst_tip;
	}

	/**
	 * Agrega un nuevo elemento
	 * 
	 * @param  dto
	 * @return long
	 * @throws ElementoDAOException
	 */
	public long setAddElemento(ProcAddElementoDTO dto) throws ElementoDAOException {
		long result = -1;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		
		logger.debug("en setAddElemento:");
		try{
			String sql =" INSERT INTO fo_mar_elementos (ele_nombre, ele_descripcion, ele_estado, ele_url_destino, ele_tip_id, ele_fec_crea) " +
					" VALUES (?,?,?,?,?,CURRENT TIMESTAMP) " ;
			logger.debug("sql:"+sql);
			logger.debug("nom:"+dto.getNombre());
			logger.debug("desc:"+dto.getDescripcion());
			logger.debug("est:"+dto.getEstado());
			logger.debug("url:"+dto.getUrl_dest());
			logger.debug("tip:"+dto.getTipo());
			//logger.debug("fec:"+dto.getFec_crea());
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stm.setString(1,dto.getNombre());
			stm.setString(2,dto.getDescripcion());
			stm.setString(3,dto.getEstado());
			stm.setString(4,dto.getUrl_dest());
			stm.setLong(5,dto.getTipo());
			//stm.setString(6,dto.getFec_crea());
			
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getLong(1);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setAddElemento - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Elimina un elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoDAOException
	 */
	public boolean setDelElemento(ProcDelElementoDTO dto) throws ElementoDAOException {
		boolean result = false;
		PreparedStatement stm = null; 
		
		logger.debug("en setDelElemento:");
		try{
			String sql =" DELETE FROM fo_mar_elementos WHERE ele_id = ? " ;
			logger.debug("sql:"+sql);
			logger.debug("eleo_id:"+dto.getId_elemento());
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_elemento());
			int i= stm.executeUpdate();
			logger.debug("i:"+i);
			
			if(i>0){
				result = true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			e.printStackTrace();
			throw new ElementoDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setDelElemento - Problema SQL (close)", e);
			}
		}
		return result;
	}

}
