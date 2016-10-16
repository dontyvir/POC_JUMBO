package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las categorias Sap que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcCategoriasSapDAO implements CategoriasSapDAO{
	
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
	* @throws CategoriasSapDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws CategoriasSapDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CategoriasSapDAOException(e);
		}
	}


	/**
	* Obtiene el nombre de la categoria sap, segun el código.
	* 
	* @param  cod_cat String 
	* @return String
	* @throws CategoriasSapDAOException
	*/
	public String getNomCatSapById(String cod_cat) throws CategoriasSapDAOException{
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String nombre="";
		try {
			String cadQuery=" SELECT descat FROM bo_catprod WHERE id_catprod = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery  + " WITH UR");
			stm.setString(1,cod_cat);
			rs = stm.executeQuery();
			if (rs.next()) {				
					nombre = rs.getString("descat");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getNomCatSapById - Problema SQL (close)", e);
			}
		}
		logger.debug("nombre categoria:"+nombre);
		
		return nombre;		
	}
	
	/**
	* Obtiene el listado de categorias, segun el código de categoria padre.
	* 
	* @param  cod_cat String 
	* @return List CategoriaSapEntity
	* @throws CategoriasSapDAOException
	*/
	public List getCategoriasSapById(String cod_cat) throws CategoriasSapDAOException{
		
		List lista_cats = new ArrayList();
		CategoriaSapEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sqlWhere = "";
		if(!cod_cat.equals("") && !cod_cat.equals("-1"))
			sqlWhere = " AND id_catprod_padre = '"+cod_cat+"'";
		else 
			sqlWhere += " AND cat_nivel = 1";
		//variable del criterio de búsqueda

		try {
			String cadQuery =	" SELECT id_catprod, descat, cat_tipo, cat_nivel, id_catprod_padre " +
								" FROM bo_catprod " +
								" WHERE estadoactivo = '1' "+sqlWhere+
								" ORDER BY 2";
			
			logger.debug("getCategoriasSapById : " + cadQuery);
			
			conn = this.getConnection();
			stm = conn.prepareStatement( cadQuery  + " WITH UR");
			
			
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaSapEntity(); 
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setDescrip(rs.getString("descat"));
				cat.setNivel(new Integer(rs.getString("cat_nivel")));
				cat.setTipo(rs.getString("cat_tipo"));
				cat.setId_cat_padre(rs.getString("id_catprod_padre"));
				lista_cats.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasSapById - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_cats.size());
		return lista_cats;
	}
	
	/**
	* Obtiene el codigo de categoria padre, segun el codigo de una categoria
	* 
	* @param  cod_cat String 
	* @return String
	* @throws CategoriasSapDAOException
	*/
	public String getCodCatPadre(String cod_cat) throws CategoriasSapDAOException {
		String result="";
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String cadQuery=" SELECT id_catprod_padre " +
							" FROM bo_catprod " +
							" WHERE id_catprod = ? ";
			logger.debug("getCodCatPadre : "+cadQuery);
			
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setString(1,cod_cat);
			rs = stm.executeQuery();
			if (rs.next()) {
				if(rs.getString("id_catprod_padre")!=null)
					result = rs.getString("id_catprod_padre");
				logger.debug("entro en if, result"+result);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCodCatPadre - Problema SQL (close)", e);
			}
		}
		logger.debug("id_catprod_padre:"+result);
		
		return result;
	}

	/**
	* Obtiene el nombre de la categoría seleccionada 
	* 
	* @param  cod_cat String 
	* @return String
	* @throws CategoriasSapDAOException
	*/
	public String getCatSapById(String cod_cat) throws CategoriasSapDAOException{
	String result="";
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String cadQuery="SELECT id_catprod, descat, cat_nivel,cat_tipo " +
							" FROM bo_catprod " +
							" WHERE id_catprod = ? and cat_nivel = "+Constantes.NIVEL_SECCION;
			logger.debug("getCatSapById : " + cadQuery);
			
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery  + " WITH UR");
			
			stm.setString(1,cod_cat);
			rs = stm.executeQuery();
			if (rs.next()) {
				if(rs.getString("descat")!=null)
					result = rs.getString("descat");
				logger.debug("entro en if, result"+result);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCatSapById - Problema SQL (close)", e);
			}
		}
		logger.debug("descat:"+result);
		
		return result;
	}
	
	/**
	* Obtiene el Listado de Todas las categorias, secciones SAP 
	* 
	* @return List CategoriaSapEntity  
	* @throws CategoriasSapDAOException
	*/
	public List getSeccionesSap() throws CategoriasSapDAOException{
		List lista_secc = new ArrayList();
		CategoriaSapEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			String cadQuery=" 	SELECT id_catprod, descat  " +
							"	FROM BODBA.BO_CATPROD WHERE cat_nivel = "+Constantes.NIVEL_SECCION ;
			logger.debug("getCategoriasSapById : "+cadQuery);
			
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaSapEntity(); 
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setDescrip(rs.getString("descat"));
				lista_secc.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSeccionesSap - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_secc.size());
		return lista_secc;
	}

	/**
	 * Obtiene categoria Sap, segun el codigo
	 * 
	 * @param  id_cat
	 * @return CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public CategoriaSapEntity getCategoriaSapById(String id_cat) throws CategoriasSapDAOException{
		CategoriaSapEntity cat = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String sql = " SELECT id_catprod, id_catprod_padre, descat, cat_nivel, cat_tipo, estadoactivo" +
					" FROM BO_CATPROD WHERE id_catprod = ? AND estadoactivo = ? ";
			logger.debug("getCategoriaSapById: " + sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setString(1,id_cat);
			stm.setString(2,Constantes.ESTADO_COD_SAP_ACTIVO);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				cat = new CategoriaSapEntity(); 
				cat.setId_cat(rs.getString("id_catprod"));
				cat.setId_cat_padre(rs.getString("id_catprod_padre"));
				cat.setDescrip(rs.getString("descat"));
				cat.setNivel(new Integer(rs.getInt("cat_nivel")));
				cat.setTipo(rs.getString("cat_tipo"));
				cat.setEstado(rs.getString("estadoactivo"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriaSapById - Problema SQL (close)", e);
			}
		}
		return cat;
	}
	

	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public LinkedHashMap getCategoriasInGRB(int inicio, int fin, String idCat) throws CategoriasSapDAOException{
		
		LinkedHashMap lista_cats =  new LinkedHashMap();
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			String cadQuery =	" SELECT distinct CAST(SUBSTR(CG.ID_CATPROD,"+inicio+","+fin+") AS INT ) as CATPROD , SUBSTR(CG.ID_CATPROD,"+inicio+","+fin+") as ID_CATPROD,  CT.DESCAT as DESCRIP" +
					" FROM BODBA.BO_CATEGORIA_PROD_MOVIL CG" +
					" inner join BO_CATPROD CT on SUBSTR(CG.ID_CATPROD,"+inicio+","+fin+")=CT.ID_CATPROD";
			if(fin == 2){
				cadQuery += " and CT.CAT_NIVEL = 1";
			}else if(fin == 4){
				cadQuery += " and CT.ID_CATPROD_PADRE='"+idCat+"' and CT.CAT_NIVEL = 2";
			}else if(fin == 6){
				cadQuery += " and CT.ID_CATPROD_PADRE='"+idCat+"' and CT.CAT_NIVEL = 3";
			}else if(fin == 9){
				cadQuery += " and CT.ID_CATPROD_PADRE='"+idCat+"' and CT.CAT_NIVEL = 4";
			}
			
			cadQuery += " order by CATPROD";
			
			conn = this.getConnection();
			stm = conn.prepareStatement( cadQuery  + " WITH UR");			
			
			rs = stm.executeQuery();
			while (rs.next()) {
				CategoriaSapEntity cat = new CategoriaSapEntity(); 
				//int catInt = rs.getInt("CATPROD");
				//String catt = rs.getString("CATPROD");//(catInt < 10)? "0"+catInt:""+catInt;
				cat.setId_cat( rs.getString("ID_CATPROD"));
				cat.setDescrip(rs.getString("DESCRIP"));
				lista_cats.put(rs.getString("CATPROD"), cat);
			}

		} catch (Exception e) {
			logger.error(e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasInGRB - Problema SQL (close)", e);
			}
		}
		return lista_cats;
	}
	
	/**
	 * Obtiene el listado de categorias sap que no estan disponibles en mobile
	 * 
	 * @param id Categoria
	 * @return List CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public LinkedHashMap getCategoriasNoGRB(String idCat) throws CategoriasSapDAOException{
		
		LinkedHashMap lista_cats =  new LinkedHashMap();		
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			int segmento = ("0".equals(idCat))? 0 : idCat.length();
			//segmento = 0 se obtienen todas las secciones
			//segmento = 2 cuando se envia la seccion se obtienen los rubros asociados.
			//segmento = 4 cuando se envia el rubro se obtienen los sub rubros asociados.
			//segmento = 6 cuando se envia el sub rubro se obtienen los grupos asociados.
			
			String cadQuery = " SELECT CAST(ID_CATPROD AS INT ) as CATPROD, ID_CATPROD, DESCAT FROM BO_CATPROD WHERE ID_CATPROD in (";
					if(segmento == 6){
						cadQuery+=" SELECT distinct (CP.ID_CATPROD) as secc FROM BO_CATPROD CP";
					}else{
						cadQuery+=" SELECT distinct substr(CP.ID_CATPROD,1,"+(segmento+2)+") as secc FROM BO_CATPROD CP ";						
					}
					cadQuery+=" left join BODBA.BO_CATEGORIA_PROD_MOVIL CG on (CP.ID_CATPROD = CG.ID_CATPROD) "+
								" where CG.ID_CATPROD is null and length(CP.ID_CATPROD) = 9 ";
									
					if(segmento == 0){
						cadQuery+= " and substr(CP.ID_CATPROD,1,2) in (SELECT ID_CATPROD FROM BODBA.BO_CATPROD WHERE CAT_NIVEL=1 and CAT_TIPO='I' and ESTADOACTIVO=1)";
					}else{
						cadQuery +=" and substr(CP.ID_CATPROD,1,"+segmento+") in ('"+idCat+"')";
					}
					
					cadQuery+=" )";
					
					cadQuery += " order by CATPROD";
			
			conn = this.getConnection();
			stm = conn.prepareStatement( cadQuery  + " WITH UR");			
			
			rs = stm.executeQuery();
			while (rs.next()) {
				CategoriaSapEntity cat = new CategoriaSapEntity(); 
				//int catInt = rs.getInt("CATPROD");
				//String catt = (catInt < 10)? "0"+catInt:""+catInt;
				cat.setId_cat(rs.getString("ID_CATPROD"));
				cat.setDescrip(rs.getString("DESCAT"));
				lista_cats.put(rs.getString("CATPROD"), cat);
			}

		} catch (Exception e) {
			logger.error(e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasNoGRB - Problema SQL (close)", e);
			}
		}
		return lista_cats;
	}	
	
	
	/**
	 * Elimina categorias en mobile
	 * 
	 * @param seccion
	 * @param segmento
	 * @return List CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public int deleteCategoriaById(String seccion, int segmento) throws CategoriasSapDAOException{
		
		PreparedStatement stm = null;

		try {			
			String sql = " DELETE FROM BODBA.BO_CATEGORIA_PROD_MOVIL WHERE ";			 
			if(segmento == 9){
				sql += " ID_CATPROD in ("+seccion+")";
			}else{
				sql += " substr(ID_CATPROD,1,"+segmento+") = '"+seccion+"'";
			}
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);	
			return stm.executeUpdate();

		} catch (Exception e) {
			logger.error(e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getGruposNoGRbByIdSubRubro - Problema SQL (close)", e);
			}
		}
	}

	/**
	 * inserta categorias para mobile
	 * 
	 * @param id Categoria
	 * @param segmento
	 * @return List CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public int addCategoriaById(String seccion, int segmento)  throws CategoriasSapDAOException{
		PreparedStatement stm = null;

		try {			
			
			//segmento == 2 insert seccion
			//segmento == 4 insert rubro
			//segmento == 6 insert sub rubro
			//segmento == 9 insert grupo
			
			String insert = " INSERT INTO BODBA.BO_CATEGORIA_PROD_MOVIL (ID_CATPROD)" +
						" (" +
						" SELECT distinct CP.ID_CATPROD FROM BO_CATPROD CP" +
						" left join BODBA.BO_CATEGORIA_PROD_MOVIL CG on (CP.ID_CATPROD = CG.ID_CATPROD)" +
						" where CG.ID_CATPROD is null and length(CP.ID_CATPROD) = 9 ";
						if(segmento == 9){
							insert +=" and CP.ID_CATPROD in ("+seccion+")";
						}else{
							insert +=" and substr(CP.ID_CATPROD,1,"+segmento+") = '"+seccion+"'";
						}					
						insert += " )";
				
			conn = this.getConnection();
			stm = conn.prepareStatement(insert);	
			return stm.executeUpdate();

		} catch (Exception e) {
			logger.error(e);
			throw new CategoriasSapDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getGruposNoGRbByIdSubRubro - Problema SQL (close)", e);
			}
		}
	}		
	
		
	
}
