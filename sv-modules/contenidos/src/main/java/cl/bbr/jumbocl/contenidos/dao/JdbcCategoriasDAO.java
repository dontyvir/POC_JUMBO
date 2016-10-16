package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.model.CategoriaEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.ProductoCategEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las categorias Web que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcCategoriasDAO implements CategoriasDAO{ 

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
	* @throws CategoriasDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws CategoriasDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CategoriasDAOException(e);
		}
	}
	
	/**
	* Obtiene el listado de las categorías, segun los criterios de búsqueda.
	* 
	* @param  criteria CategoriasCriteriaDTO 
	* @return List CategoriaEntity
	* @throws CategoriasDAOException
	*/
	public List listadoCategoriasByCriteria(CategoriasCriteriaDTO criteria) throws CategoriasDAOException {
		
		List lista_categorias = new ArrayList();
		CategoriaEntity cat = null;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en listadoCategoriasByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de categoria
		String nom_cat = criteria.getNombre();
		String sqlNomCat = "";
		if (!nom_cat.equals(""))
			sqlNomCat = " AND upper(cat_nombre) like \'"+nom_cat.toUpperCase()+"%\'";
		//numero de categoria
		String num_cat = criteria.getNumero();
		String sqlNumCat = "";
		if (!num_cat.equals(""))
			sqlNumCat = " AND cat_id = "+num_cat;
		//segun estado
		char estado = criteria.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND cat_estado = \'"+estado+"\'";
		//segun tipo
		char tipo = criteria.getTipo();
		String sqlTipo = "";
		if (tipo!='0')
			sqlTipo = "AND cat_tipo = \'"+tipo+"'";
		
		//categoria padre seleccionada
		String sel_cat = criteria.getId_cat_padre();
		String sqlSelCatFrom = "";
		String sqlSelCatWhere = "";
		long id_cat_padre = -1;
		if (!sel_cat.equals("") && !(sel_cat.equals("-1"))){
			sqlSelCatFrom = ", fo_catsubcat CS";
			sqlSelCatWhere = " AND CS.subcat_id = C.cat_id and CS.cat_id = "+sel_cat;
			id_cat_padre = new Long(sel_cat).longValue();
		}
		//variables de paginacion
		int pag = criteria.getPag();
		int regXpag = criteria.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {

			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by UPPER(C.cat_nombre)) as row," +
				" C.cat_id as id_cat, cat_nombre, cat_descripcion, cat_estado, cat_tipo," +
				" cat_orden, cat_porc_ranking, cat_banner " +
				" FROM fo_categorias C " + sqlSelCatFrom +
				" WHERE C.cat_id > 0 " +
				sqlNomCat + sqlNumCat + sqlSelCatWhere + sqlEstado + sqlTipo +
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg ;
			logger.debug("sql:"+sql);
			
			conn = this.getConnection();
			stm =conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaEntity();
				cat.setId( new Long(rs.getString("id_cat")));
				cat.setNombre(rs.getString("cat_nombre"));
				cat.setDescripcion(rs.getString("cat_descripcion"));
				cat.setEstado(rs.getString("cat_estado"));
				cat.setTipo(rs.getString("cat_tipo"));
				if(rs.getString("cat_orden")!=null)
					cat.setOrden(new Integer(rs.getString("cat_orden")));
				else
					cat.setOrden(new Integer(0));
				if(rs.getString("cat_porc_ranking")!=null)
					cat.setPorc_ranking(new Double(rs.getString("cat_porc_ranking")));
				else
					cat.setPorc_ranking(new Double(0));
				cat.setBanner(rs.getString("cat_banner"));
				cat.setId_padre(new Long(id_cat_padre));
				lista_categorias.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoCategoriasByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_categorias.size());
		return lista_categorias;
	}

	/**
	* Obtiene la cantidad de las categorías, segun los criterios de búsqueda.
	* 
	* @param  criteria CategoriasCriteriaDTO 
	* @return int
	* @throws CategoriasDAOException
	*/
	public int countCategoriasByCriteria(CategoriasCriteriaDTO criteria) throws CategoriasDAOException {
		
		int numCat = 0;
		
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en countCategoriasByCriteria:");
		
		//variable del criterio de búsqueda
		//nombre de categoria
		String nom_cat = criteria.getNombre();
		String sqlNomCat = "";
		if (!nom_cat.equals(""))
			sqlNomCat = " AND upper(cat_nombre) like \'"+nom_cat.toUpperCase()+"%\'";
		//numero de categoria
		String num_cat = criteria.getNumero();
		String sqlNumCat = "";
		if (!num_cat.equals(""))
			sqlNumCat = " AND cat_id = "+num_cat;
		//segun estado
		char estado = criteria.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND cat_estado = \'"+estado+"\'";
		//segun tipo
		char tipo = criteria.getTipo();
		String sqlTipo = "";
		if (tipo!='0')
			sqlTipo = "AND cat_tipo = \'"+tipo+"'";
		
		//categoria padre seleccionada
		String sel_cat = criteria.getId_cat_padre();
		String sqlSelCatFrom = "";
		String sqlSelCatWhere = "";
		long id_cat_padre = -1;
		if (!sel_cat.equals("") && !(sel_cat.equals("-1"))){
			sqlSelCatFrom = ", fo_catsubcat CS";
			sqlSelCatWhere = " AND CS.subcat_id = C.cat_id and CS.cat_id = "+sel_cat;
			id_cat_padre = new Long(sel_cat).longValue();
		}
		logger.debug("id_cat_padre:"+id_cat_padre);

		try {
			String sql =" SELECT count(C.cat_id) as cantidad" +
				" FROM fo_categorias C " + sqlSelCatFrom +
				" WHERE C.cat_id > 0 " +
				sqlNomCat + sqlNumCat + sqlSelCatWhere + sqlEstado + sqlTipo ;
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				numCat = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : countCategoriasByCriteria - Problema SQL (close)", e);
			}
		}
		return numCat;
	}
	
	/**
	* Crea una nueva categoria.
	* 
	* @param  categoria ProcAddCatWebDTO 
	* @return boolean, devuelve <i>true</i> si se creó la categoría con éxito, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean creaCategoria(ProcAddCatWebDTO categoria) throws CategoriasDAOException {
		
		
		PreparedStatement stm = null;
		String fecha = Formatos.getFecHoraActual();
		boolean result = false;
		try {

			String sql = "INSERT INTO fo_categorias " +
				" (cat_nombre,cat_descripcion,cat_estado,cat_tipo,cat_orden,cat_fcrea,cat_banner,cat_totem,cat_imagen,cat_url_banner) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?) ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			logger.debug("en creaCategoria:"+sql);
			logger.debug("valores:"+categoria.getNombre()+","+categoria.getDescripcion()+","+
					categoria.getEstado()+","+categoria.getTipo()+","+categoria.getOrden()+","+
					categoria.getRuta_banner()+","+categoria.getTotem()+","+categoria.getImagen()+","+categoria.getUrlBanner());
			
			stm.setString(1,categoria.getNombre());
			stm.setString(2,categoria.getDescripcion());
			stm.setString(3,categoria.getEstado());
			stm.setString(4,categoria.getTipo());
			stm.setLong(5,categoria.getOrden());
			stm.setString(6,fecha);
			stm.setString(7,categoria.getRuta_banner());
			stm.setString(8,categoria.getTotem());
            stm.setString(9,categoria.getImagen());
            stm.setString(10,categoria.getUrlBanner());
            
			int i = stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema creaCategoria:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : creaCategoria - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Actualiza la información de una categoría.
	* 
	* @param  categoria ProcModCatWebDTO 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean modificaCategoria(ProcModCatWebDTO categoria) throws CategoriasDAOException {
		
		
		PreparedStatement stm = null;
		long id_cat = categoria.getId_categoria();
		boolean result = false;
		try {
			String sql = "UPDATE fo_categorias SET "+
				" cat_nombre = ?, cat_descripcion = ?, cat_estado = ?, cat_tipo = ?, " +
				" cat_orden = ?, cat_fmod = ?, cat_user_mod = ?, cat_banner = ?, cat_totem = ?, cat_imagen = ?, cat_url_banner = ?"+
				" WHERE cat_id = ? "; 

			conn = this.getConnection();
			stm =conn.prepareStatement(sql);
			logger.debug("en modificaCategoria:");
			logger.debug(sql);
			logger.debug("id_cat:"+id_cat);
			logger.debug("nombre:"+categoria.getNombre());
			logger.debug("descripcion:"+categoria.getDescripcion());
			logger.debug("estado:"+categoria.getEstado());
			logger.debug("tipo:"+categoria.getTipo());
			logger.debug("orden:"+categoria.getOrden());
			logger.debug("fec_modif:"+categoria.getFec_modif());
			logger.debug("usu_modif:"+categoria.getUsu_modif());
			logger.debug("ruta_banner:"+categoria.getRuta_banner());
			logger.debug("totem:"+categoria.getTotem());
            logger.debug("imagen:"+categoria.getImagen());
			logger.debug("id_cat:"+id_cat);
			logger.debug("url_banner:"+categoria.getUrlBanner());
			
			stm.setString(1,categoria.getNombre());
			stm.setString(2,categoria.getDescripcion());
			stm.setString(3,categoria.getEstado());
			stm.setString(4,categoria.getTipo());
			stm.setLong(5, categoria.getOrden());
			stm.setString(6,categoria.getFec_modif());
			stm.setLong(7,categoria.getUsu_modif());
			stm.setString(8,categoria.getRuta_banner());
			stm.setString(9,categoria.getTotem());
            stm.setString(10,categoria.getImagen());
			stm.setString(11, categoria.getUrlBanner());
			stm.setLong(12, id_cat);
			
			int i = stm.executeUpdate();
			logger.debug("i:"+i);
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema modificaCategoria:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modificaCategoria - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Elimina una categoría.
	* 
	* @param  categoria_id long 
	* @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean eliminaCategoria(long categoria_id) throws CategoriasDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			String sql = "DELETE FROM fo_categorias WHERE cat_id = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			logger.debug("en eliminaCategoria:");
			logger.debug(sql);
			logger.debug("categoria_id:"+categoria_id);
			stm.setLong(1, categoria_id);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new CategoriasDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_EXIST_DEP_TBLS) ){ 
				throw new CategoriasDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new CategoriasDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaCategoria - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Obtiene datos de una categoría.
	* 
	* @param  categoria_id long 
	* @return CategoriaEntity 
	* @throws CategoriasDAOException
	*/
	public CategoriaEntity getCategoriaById(long categoria_id) throws CategoriasDAOException {
		
		CategoriaEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT cat_id, cat_nombre, cat_descripcion, cat_estado, cat_tipo," +
				" cat_orden, cat_porc_ranking, cat_banner, cat_fcrea, cat_fmod, cat_user_mod, cat_totem, cat_imagen, cat_url_banner" +
				" FROM fo_categorias where cat_id = ?";
			conn = this.getConnection();
			stm =conn.prepareStatement(sql + " WITH UR");

			logger.debug("en el select de getCategoriaById");
			logger.debug("sql:"+sql);
			logger.debug("categoria_id:"+categoria_id);
			stm.setLong(1, categoria_id);
			rs = stm.executeQuery();
			if (rs.next()) {
				cat = new CategoriaEntity();
				cat.setId( new Long(rs.getString("cat_id")));
				cat.setNombre(rs.getString("cat_nombre"));
				cat.setDescripcion(rs.getString("cat_descripcion"));
				cat.setEstado(rs.getString("cat_estado"));
				cat.setTipo(rs.getString("cat_tipo"));
				if(rs.getString("cat_orden")!=null)
					cat.setOrden(new Integer(rs.getString("cat_orden")));
				else
					cat.setOrden(new Integer(0));
				if(rs.getString("cat_porc_ranking")!=null)
					cat.setPorc_ranking(new Double(rs.getString("cat_porc_ranking")));
				else
					cat.setPorc_ranking(new Double(0));
				cat.setBanner(rs.getString("cat_banner"));
				cat.setFec_crea(rs.getTimestamp("cat_fcrea"));
				cat.setFec_mod(rs.getTimestamp("cat_fmod"));
				if(rs.getString("cat_user_mod")!=null)
					cat.setUser_mod(new Integer(rs.getString("cat_user_mod")));
				else
					cat.setUser_mod(null);
				if(rs.getString("cat_totem")==null){
					cat.setTotem("");
				}else{
					cat.setTotem(rs.getString("cat_totem"));
				}
                if(rs.getString("cat_imagen")==null){
                    cat.setImagen("");
                }else{
                    cat.setImagen(rs.getString("cat_imagen"));
                }
                cat.setUrl_imagen(rs.getString("cat_url_banner"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriaById - Problema SQL (close)", e);
			}
		}
		return cat;
	}

	/**
	* Obtiene el listado de categorias por navegación.
	* 
	* @param  criterio CategoriasCriteriaDTO 
	* @param  cat_padre long 
	* @return List CategoriaEntity 
	* @throws CategoriasDAOException
	*/
	public List listadoCategoriasNavegacion(CategoriasCriteriaDTO criterio, long cat_padre) throws CategoriasDAOException {
		
		List lista_categorias = new ArrayList();
		CategoriaEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en listadoCategoriasNavegacion:");
		
		//variable del criterio de búsqueda
		String sqlNomCat = "";
		String sqlNumCat = "";
		String sqlSelCatFrom = "";
		String sqlSelCatWhere = "";
		long id_cat_padre = -1;
		//estado
		char estado = criterio.getActivo();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND cat_estado = \'"+estado+"\'";
		//segun tipo
		char tipo = criterio.getTipo();
		String sqlTipo = "";
		if (tipo!='0')
			sqlTipo = " AND cat_tipo = \'"+tipo+"'";

		try {
			String sql = " SELECT "+
				" C.cat_id as id_cat, cat_nombre, cat_descripcion, cat_estado, cat_tipo," +
				" cat_orden, cat_porc_ranking, cat_banner " +
				" FROM fo_categorias C " + sqlSelCatFrom +
				" WHERE C.cat_id > -1 " +
				sqlNomCat + sqlNumCat + sqlSelCatWhere + sqlEstado + sqlTipo;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			logger.debug("listadoCategoriasNavegacion");
			logger.debug("sql:"+sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaEntity();
				cat.setId( new Long(rs.getString("id_cat")));
				cat.setNombre(rs.getString("cat_nombre"));
				cat.setDescripcion(rs.getString("cat_descripcion"));
				cat.setEstado(rs.getString("cat_estado"));
				cat.setTipo(rs.getString("cat_tipo"));
				if(rs.getString("cat_orden")!=null)
					cat.setOrden(new Integer(rs.getString("cat_orden")));
				else
					cat.setOrden(new Integer(0));
				if(rs.getString("cat_porc_ranking")!=null)
					cat.setPorc_ranking(new Double(rs.getString("cat_porc_ranking")));
				else
					cat.setPorc_ranking(new Double(0));
				cat.setBanner(rs.getString("cat_banner"));
				cat.setId_padre(new Long(id_cat_padre));
				lista_categorias.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoCategoriasNavegacion - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_categorias.size());
		return lista_categorias;
	}

	/**
	* Obtiene el listado de productos de una categoria.
	* 
	* @param  codCat long 
	* @return List ProductoCategEntity 
	* @throws CategoriasDAOException
	*/
	public List getProductosByCategId(long codCat) throws CategoriasDAOException {
		
		List list_prod = new ArrayList();
		ProductoCategEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			logger.debug("en getProductosByCategId");
			String sql = " SELECT pro_id, pro_des_corta, prca_id, prca_orden, prca_con_pago, " +
					     " pro_tipo_producto, mar_nombre " +  
					     " FROM fo_productos " + 
					     " LEFT JOIN fo_marcas ON mar_id = pro_mar_id " + 
					     " JOIN  fo_productos_categorias ON  prca_pro_id = pro_id " +
					     " WHERE prca_cat_id = ? " ;
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("sql:"+sql);
			logger.debug("codCat:"+codCat);
			stm.setLong(1,codCat);
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductoCategEntity();
				prod.setId(new Long (rs.getString("prca_id")));
				prod.setId_prod(new Long(rs.getString("pro_id")));
				prod.setNom_prod(rs.getString("pro_des_corta"));
				prod.setTipo_prod(rs.getString("pro_tipo_producto"));
				logger.debug("TipoDeProducto: " 
						+ rs.getString("pro_tipo_producto")
						+" Marca: "+rs.getString("mar_nombre"));
				prod.setNom_marca(rs.getString("mar_nombre"));
				if(rs.getString("prca_orden")!=null)
					prod.setOrden(new Integer(rs.getString("prca_orden")));
				else
					prod.setOrden(new Integer("0"));
				if(rs.getString("prca_con_pago")!=null)
					prod.setCon_pago(rs.getString("prca_con_pago"));
				else
					prod.setCon_pago("");
				list_prod.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosByCategId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_prod.size());
		
		return list_prod;
	}
	
	/**
	* Obtiene el listado de estados, segun el tipo de estado.
	* 
	* @param  tip_estado String 
	* @return List EstadoEntity  
	* @throws CategoriasDAOException
	*/
	public List getEstados(String tip_estado) throws CategoriasDAOException {
		
		List list_estado = new ArrayList();
		EstadoEntity est = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			String sql = " SELECT id, estado, tipo " +
				" FROM fo_estados " +
				" WHERE tipo='"+tip_estado+"'" ;
            
            if (tip_estado.equals("CAT"))
                sql = sql + " order by estado asc"; 
            
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			logger.debug("sql:"+sql);
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
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEstados - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_estado.size());
		
		return list_estado;
	}

	/**
	* Obtiene el listado de categorias asociadas a una categoria.
	* 
	* @param  codCat long 
	* @return List CategoriaEntity  
	* @throws CategoriasDAOException
	*/
	public List getCategoriasByCategId(long codCat) throws CategoriasDAOException {
		
		List lst_Categ = new ArrayList();
		CategoriaEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String sql = " SELECT C.cat_id as id_cat, cat_nombre, cat_descripcion, cat_orden " +
				" FROM fo_categorias C, fo_catsubcat S " +
				" WHERE subcat_id = C.cat_id AND S.cat_id = ? ";
			conn = this.getConnection();
			logger.debug("en getCategoriasByCategId:");
			stm = conn.prepareStatement(sql + " WITH UR");

			logger.debug(" sql:"+sql);
			logger.debug("codCat:"+codCat);
			stm.setLong(1,codCat);
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaEntity();
				cat.setId(new Long(rs.getString("id_cat")));
				cat.setNombre(rs.getString("cat_nombre"));
				cat.setDescripcion(rs.getString("cat_descripcion"));
				if(rs.getString("cat_orden")!=null)
					cat.setOrden(new Integer(rs.getString("cat_orden")));
				else
					cat.setOrden(new Integer(0));
				lst_Categ.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasByCategId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_Categ.size());
		
		return lst_Categ;
	}
	
	/**
	* Agregar una subcategoria a una categoria.
	* 
	* @param  prm ProcModSubCatWebDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean addSubCategory(ProcModSubCatWebDTO prm) throws CategoriasDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en addSubCategory");
			String sql = " INSERT INTO fo_catsubcat VALUES (?,?) " ;
			stm = conn.prepareStatement(sql);
			logger.debug("sql:"+sql);
			logger.debug("vals:"+prm.getId_cat()+","+prm.getId_subcategoria());

			stm.setLong(1,prm.getId_cat());
			stm.setLong(2,prm.getId_subcategoria());
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addSubCategory - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}
	
	/**
	* Eliminar la relación entre una categoria y una subcategoria.
	* 
	* @param  prm ProcModSubCatWebDTO 
	* @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean delSubCategory(ProcModSubCatWebDTO prm) throws CategoriasDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		try {
			String sql = " DELETE FROM fo_catsubcat WHERE cat_id = ? AND subcat_id = ? ";
			conn = this.getConnection();
			logger.debug("en delSubCategory");
			logger.debug(sql);
			logger.debug("cat_id:"+prm.getId_cat());
			logger.debug("subcat_id:"+prm.getId_subcategoria());
			stm = conn.prepareStatement(sql);

			stm.setLong(1,prm.getId_cat());
			stm.setLong(2,prm.getId_subcategoria());
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delSubCategory - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}

	/**
	* Verifica si existe la categoria.
	* 
	* @param  id long 
	* @return boolean, devuelve <i>true</i> si la categoría existe, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean isCategoriaById(long id) throws CategoriasDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String sql = " SELECT  cat_id FROM fo_categorias WHERE cat_id = ? ";
			conn = this.getConnection();
			logger.debug("en isCategoriaById");
			logger.debug(sql);
			logger.debug("id:"+id);
			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id);
			
			rs = stm.executeQuery();
			if(rs.next())
				res = true;

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isCategoriaById - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}

	/**
	* Verifica si existe la relación entre una categoria y una subcategoría.
	* 
	* @param  id_cat long 
	* @param  id_subcat long 
	* @return boolean, devuelve <i>true</i> si la relación existe, caso contrario, devuelve <i>false</i>.
	* @throws CategoriasDAOException
	*/
	public boolean isCategRelSubCateg(long id_cat, long id_subcat) throws CategoriasDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			String sql = " SELECT  cat_id FROM fo_catsubcat WHERE cat_id = ? AND subcat_id = ?";
			conn = this.getConnection();
			logger.debug("en isCategRelSubCateg");
			logger.debug(sql);
			logger.debug("cat_id:"+id_cat);
			logger.debug("subcat_id:"+id_subcat);
			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id_cat);
			stm.setLong(2,id_subcat);
			
			
			rs = stm.executeQuery();
			if(rs.next())
				res = true;

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isCategRelSubCateg - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}

    /**
     * @param idCategoria
     * @return
     */
    public String getNombresCategoriasPadreByIdCat(long idCategoria) throws CategoriasDAOException {		
		PreparedStatement stm	= null; 
		ResultSet rs 			= null;
		String categoriasPadre  = "";
		logger.debug("en getNombresCategoriasPadreByIdCat:");
		
		try {

			String sql = " SELECT P.CAT_NOMBRE as PADRE " +
						 " FROM fo_categorias C " +
						 " 		inner join fo_catsubcat SC on (C.CAT_ID = SC.SUBCAT_ID) " +
						 " 		inner join fo_categorias P on (SC.CAT_ID = P.CAT_ID) " +
						 " WHERE P.CAT_ESTADO = 'A' AND C.CAT_ID = ?";
			logger.debug("sql:"+sql);
			
			conn = this.getConnection();
			stm  = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, idCategoria);
			rs   = stm.executeQuery();
            int maximoPadre = 0;
			while (rs.next()) {
                if ( maximoPadre < 3 ) {
                    categoriasPadre += rs.getString("PADRE") + " | ";
                }
                maximoPadre++;
			}
			if (categoriasPadre.length() > 0) {
			    categoriasPadre = categoriasPadre.substring(0, categoriasPadre.length()-3);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getNombresCategoriasPadreByIdCat - Problema SQL (close)", e);
			}
		}
		return categoriasPadre;
    }

	//[20121120avc
	public boolean eliminaAllProductoCateg(long cat_id) throws CategoriasDAOException {
		PreparedStatement stm = null;

		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en eliminaAllProductoCateg");
			String sql = "DELETE FROM fo_productos_categorias " +
				" WHERE prca_cat_id = ? ";
			logger.debug(sql);
			logger.debug("valores:"+cat_id);
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, cat_id);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new CategoriasDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaAllProductoCateg - Problema SQL (close)", e);
}
		}
		return result;
	}
    /**
     * @param id_categoria
     * @return
     * @throws CategoriasDAOException
     */
	public List getProductosSinMasCategorias(long id_categoria) throws CategoriasDAOException {
	    PreparedStatement stm = null;
	    ResultSet rs = null;
	    List result = new ArrayList();
	    
	    try {
	        conn = this.getConnection();
	        logger.debug("en getProductosSinMasCategorias");
	        String sql = "select fo_productos.* from fo_productos join fo_productos_categorias ON pro_id = prca_pro_id where prca_cat_id = ? and pro_id not in (select pro_id from fo_productos join fo_productos_categorias ON pro_id = prca_pro_id where prca_cat_id <> ?)";
	        
	        logger.debug(sql);
	        logger.debug("valores:"+id_categoria);
	        stm = conn.prepareStatement(sql);
	        
	        stm.setLong(1, id_categoria);
	        stm.setLong(2, id_categoria);
	        
	        rs = stm.executeQuery();
	        while(rs.next()) {
	            ProductoEntity producto = new ProductoEntity();
	            producto.setId( new Long(rs.getString("pro_id")));
	            producto.setTipo(rs.getString("pro_tipo_producto"));
	            producto.setDesc_corta(rs.getString("pro_des_corta"));
	            producto.setDesc_larga(rs.getString("pro_des_larga"));
	            producto.setGenerico(rs.getString("pro_generico"));
	            producto.setEstado(rs.getString("pro_estado"));
	            producto.setUni_med_desc(rs.getString("uni_med"));
	            producto.setCod_sap(rs.getString("codsap"));
	            producto.setNom_marca(rs.getString("pro_marca"));
	            producto.setInter_valor(new Double(rs.getString("pro_inter_valor")));
	            producto.setInter_max(new Double(rs.getString("pro_inter_max")));
	            result.add(producto);
	            
	        }
	        
	    } catch (Exception e) {
	        logger.debug("Problema :"+ e);
	        throw new CategoriasDAOException(e);
	    } finally {
	        try {
	        	if (rs != null)
	                rs.close();
	        	if (stm != null)
	                stm.close();
	            releaseConnection();
	        } catch (SQLException e) {
	            logger.error("[Metodo] : getProductosSinMasCategorias - Problema SQL (close)", e);
	        }
	    }
	    return result;
	    
	    
	}
	//]20121120avc

}
