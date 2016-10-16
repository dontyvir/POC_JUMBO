package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.model.CodBarraSapEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los Productos Sap que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcProductosSapDAO implements ProductosSapDAO{
	
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
	* @throws ProductosSapDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws ProductosSapDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ProductosSapDAOException(e);
		}
	}

	/**
	* Obtiene el listado de productos Sap segun los criterios de búsqueda
	* 
	* @param  criterio ProductosSapCriteriaDTO 
	* @return List ProductoSapEntity 
	* @throws ProductosSapDAOException
	*/
	public List getProductosSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapDAOException {
		List lista_productos = new ArrayList();
		ProductoSapEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductosSapByCriteria:");
		
		//variable del criterio de búsqueda
		//cod sap de producto
		String cod_sap_prod= criterio.getCodSapProd();
		String sqlCodSapProd = "";
		if (!cod_sap_prod.equals(""))
			sqlCodSapProd = " AND P.cod_prod1 LIKE '"+cod_sap_prod + "%'";
		//cod sap de categoria
		String cod_sap_cat = criterio.getCodSapCat();
		String sqlCodSapCat = "";
		if (!cod_sap_cat.equals(""))
			sqlCodSapCat = " AND P.id_catprod LIKE '"+cod_sap_cat+"%'";
		
		//id categoria seleccionada
		String id_cat = criterio.getCat_sel();
		String sqlSelCat = "";
		if(!id_cat.equals("") && !id_cat.equals("-") && !id_cat.equals("-1") &&  !id_cat.equals("0"))
			sqlSelCat = " AND P.id_catprod = '"+id_cat+"'";
		//mix_producto
		String sqlMix = "";
		if(criterio.getMix_opcion()!=null){
			if(criterio.getMix_opcion().equals("S"))
				sqlMix = " AND P.mix_web = 'S' ";
			else if(criterio.getMix_opcion().equals("N"))
				sqlMix = " AND P.mix_web = 'N' ";
		}
		
		//con precio
		String sqlConPrecio = "";
		if(criterio.getCon_precio() !=null){
			if(criterio.getCon_precio().equals("S"))
				sqlConPrecio = " AND P.con_precio = 'S' ";
			else if(criterio.getCon_precio().equals("N"))
				sqlConPrecio = " AND P.con_precio = 'N' ";
		}
		
		//id_cat_all muestra todos los productos
		String sqlIdCatAll = "";
		if( (criterio.getId_cat_all()) != null && (!criterio.getId_cat_all().equals("")) ){
			sqlIdCatAll = " AND P.id_catprod LIKE '"+criterio.getId_cat_all()+"%' ";
		}
		
		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {

			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by descat) as row," +
				" id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, des_larga, " +
				" P.estadoactivo as estadoAct, cod_proppal, fcarga, mix_web, descat, P.estado as estado, con_precio " + 
				" FROM bo_productos P " +
				" JOIN bo_catprod C on C.id_catprod = P.id_catprod " + 
				" WHERE 1 = 1 " +
				sqlCodSapProd + sqlCodSapCat + sqlSelCat + sqlMix + sqlConPrecio + sqlIdCatAll +
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			logger.debug("la query: "+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductoSapEntity();
				prod.setId( new Long(rs.getString("id_producto")));
				prod.setCod_prod_1(rs.getString("cod_prod1"));
				prod.setUni_med(rs.getString("uni_med"));
				//logger.debug("rs.getString(uni_med): "+rs.getString("uni_med"));
				prod.setDes_corta(rs.getString("des_corta"));
				prod.setNom_cat_sap(rs.getString("descat"));
				if(rs.getString("fcarga")!=null)
					prod.setFecCarga(rs.getTimestamp("fcarga"));
				else
					prod.setFecCarga(null);
				prod.setMixWeb(rs.getString("mix_web"));
				prod.setEstado(rs.getString("estado"));
				prod.setEstActivo(rs.getString("estadoAct"));
				prod.setCon_precio(rs.getString("con_precio"));
							
				lista_productos.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosSapByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_productos.size());
		return lista_productos;
	}

	/**
	* Obtiene la cantidad de productos Sap segun los criterios de búsqueda
	* 
	* @param  criterio ProductosSapCriteriaDTO 
	* @return int 
	* @throws ProductosSapDAOException
	*/
	public int getCountProdSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapDAOException {
		int cant = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCountProdSapByCriteria:");
		
		//variable del criterio de búsqueda
		//cod sap de producto
		String cod_sap_prod= criterio.getCodSapProd();
		String sqlCodSapProd = "";
		if (!cod_sap_prod.equals(""))
			sqlCodSapProd = " AND P.cod_prod1 LIKE '"+cod_sap_prod + "%'";
		//cod sap de categoria
		String cod_sap_cat = criterio.getCodSapCat();
		String sqlCodSapCat = "";
		if (!cod_sap_cat.equals(""))
			sqlCodSapCat = " AND P.id_catprod LIKE '"+cod_sap_cat+"%'";
		
		//id categoria seleccionada
		String id_cat = criterio.getCat_sel();
		String sqlSelCat = "";
		if(!id_cat.equals("") && !id_cat.equals("-") &&  !id_cat.equals("-1") && !id_cat.equals("0"))
			sqlSelCat = " AND P.id_catprod = '"+id_cat+"'";
		//mix_producto
		String sqlMix = "";
		if(!criterio.getMix_opcion().equals("")){
			if(criterio.getMix_opcion().equals("S"))
				sqlMix = " AND P.mix_web = 'S' ";
			else if(criterio.getMix_opcion().equals("N"))
				sqlMix = " AND P.mix_web = 'N' ";
		}

		//con precio
		String sqlConPrecio = "";
		if(criterio.getCon_precio() !=null){
			if(criterio.getCon_precio().equals("S"))
				sqlConPrecio = " AND P.con_precio = 'S' ";
			else if(criterio.getCon_precio().equals("N"))
				sqlConPrecio = " AND P.con_precio = 'N' ";
		}
		
		//id_cat_all muestra todos los productos
		String sqlIdCatAll = "";
		if( (criterio.getId_cat_all()) != null && (!criterio.getId_cat_all().equals("")) ){
			sqlIdCatAll = " AND P.id_catprod LIKE '"+criterio.getId_cat_all()+"%' ";
		}

		try {

			String sql = " SELECT count(P.id_catprod) as cantidad " + 
				" FROM bo_productos P " +
				" JOIN bo_catprod C on C.id_catprod = P.id_catprod " + 
				" WHERE 1 = 1 " +
				sqlCodSapProd + sqlCodSapCat + sqlSelCat + sqlMix + sqlConPrecio + sqlIdCatAll ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountProdSapByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant:"+cant);
		return cant;
	}
	
	/**
	* Obtiene la lista de productos Sap segun los criterios de busqueda por fecha
	* 
	* @param  criterio ProductosSapCriteriaDTO 
	* @return List ProductoSapEntity  
	* @throws ProductosSapDAOException
	*/
	public List getProductosSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapDAOException {
		List lista_productos = new ArrayList();
		ProductoSapEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductosSapByCriteriaByDateRange:");
		
		//variable del criterio de búsqueda
		//por fechas 
		String sqlFecha = "";
		if(!fecha_ini.equals("") && !fecha_fin.equals("")){
			sqlFecha = " AND ( DATE(P.fcarga) BETWEEN  '"+fecha_ini+"' AND '"+fecha_fin+"') ";
		}
		/*
		String sqlFechaIni = "";
		if(!fecha_ini.equals(""))
			sqlFechaIni = " AND P.fcarga >= '"+fecha_ini+"'";
		String sqlFechaFin = "";
		if(!fecha_fin.equals(""))
			sqlFechaFin = " AND P.fcarga <= '"+fecha_fin+"'";
		*/
		//mix_producto
		String sqlMix = "";
		if(criterio.getMix_opcion()!=null){
			if(criterio.getMix_opcion().equals("S"))
				sqlMix = " AND P.mix_web = 'S' ";
			else if(criterio.getMix_opcion().equals("N"))
				sqlMix = " AND P.mix_web = 'N' ";
		}
		
		//con precio
		String sqlConPrecio = "";
		if(criterio.getCon_precio() !=null){
			if(criterio.getCon_precio().equals("S"))
				sqlConPrecio = " AND P.con_precio = 'S' ";
			else if(criterio.getCon_precio().equals("N"))
				sqlConPrecio = " AND P.con_precio = 'N' ";
		}
		
		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {

			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by id_producto) as row," +
				" id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, des_larga, " +
				" P.estadoactivo as estadoAct, cod_proppal, fcarga, mix_web, descat, P.estado as estado, con_precio " + 
				" FROM bo_productos P " +
				" JOIN bo_catprod C on C.id_catprod = P.id_catprod " + 
				" WHERE 1 = 1 " +
				sqlFecha + sqlMix + sqlConPrecio +
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductoSapEntity();
				prod.setId( new Long(rs.getString("id_producto")));
				prod.setCod_prod_1(rs.getString("cod_prod1"));
				prod.setDes_corta(rs.getString("des_corta"));
				prod.setUni_med(rs.getString("uni_med"));
				logger.debug("rs.getString(uni_med)"+rs.getString("uni_med"));
				prod.setNom_cat_sap(rs.getString("descat"));
				if(rs.getString("fcarga")!=null)
					prod.setFecCarga(rs.getTimestamp("fcarga"));
				else
					prod.setFecCarga(null);
				prod.setMixWeb(rs.getString("mix_web"));
				prod.setEstado(rs.getString("estado"));
				prod.setEstActivo(rs.getString("estadoAct"));
				prod.setCon_precio(rs.getString("con_precio"));
				
				lista_productos.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosSapByCriteriaByDateRange - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_productos.size());
		return lista_productos;
	}

	/**
	* Obtiene la cantidad de productos Sap segun los criterios de busqueda por fecha
	* 
	* @param  criterio ProductosSapCriteriaDTO 
	* @return int  
	* @throws ProductosSapDAOException
	*/
	public int getCountProdSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapDAOException {
		
		int cant = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCountProdSapByCriteriaByDateRange:");
		
		//mix_producto
		String sqlMix = "";
		if(criterio.getMix_opcion()!=null){
			if(criterio.getMix_opcion().equals("S"))
				sqlMix = " AND P.mix_web = 'S' ";
			else if(criterio.getMix_opcion().equals("N"))
				sqlMix = " AND P.mix_web = 'N' ";
		}

		//con precio
		String sqlConPrecio = "";
		if(criterio.getCon_precio() !=null){
			if(criterio.getCon_precio().equals("S"))
				sqlConPrecio = " AND P.con_precio = 'S' ";
			else if(criterio.getCon_precio().equals("N"))
				sqlConPrecio = " AND P.con_precio = 'N' ";
		}
		
		//variable del criterio de búsqueda
		//por fechas 
		String sqlFecha = "";
		if(!fecha_ini.equals("") && !fecha_fin.equals("")){
			sqlFecha = " AND ( DATE(P.fcarga) BETWEEN  '"+fecha_ini+"' AND '"+fecha_fin+"') ";
		}


		try {

			String sql = " SELECT count(P.id_producto) as cantidad " + 
				" FROM bo_productos P " +
				" JOIN bo_catprod C on C.id_catprod = P.id_catprod " + 
				" WHERE 1 = 1 " +
				sqlFecha + sqlMix + sqlConPrecio ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountProdSapByCriteriaByDateRange - Problema SQL (close)", e);
			}
		}
		logger.debug("cant:"+cant);
		return cant;
	}
	
	/**
	* Obtiene información del producto Sap
	* 
	* @param  id_prod long 
	* @return int
	* @throws ProductosSapDAOException
	*/
	public ProductoSapEntity getProductSapById(long id_prod) throws ProductosSapDAOException{
		ProductoSapEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductSapById:");
		
		try {
			String sql = " SELECT id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, " + 
				" des_larga, estado, marca, cod_proppal, origen, un_base, ean13,  " +
				" un_empaque, num_conv, den_conv, atrib9, atrib10, fcarga, mix_web, P.estadoactivo as est, descat, " +
				" substr(P.id_catprod,1,2) seccion, substr(P.id_catprod,3,2) rubro, " +
				" substr(P.id_catprod,5,2) subrubro, substr(P.id_catprod,7) grupo " +
				" FROM bo_productos P, bo_catprod C " +
				" WHERE C.id_catprod = P.id_catprod AND id_producto = ? ";

			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			
			int i=0;
			if (rs.next()) {
				prod = new ProductoSapEntity();
				prod.setId( new Long(rs.getString("id_producto")));
				prod.setId_cat(rs.getString("id_cat"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setCod_prod_1(rs.getString("cod_prod1"));
				prod.setCod_prod_2(rs.getString("cod_prod2"));
				prod.setDes_corta(rs.getString("des_corta"));
				prod.setDes_larga(rs.getString("des_larga"));
				prod.setEstado(rs.getString("estado"));
				if(rs.getString("marca")!=null)
					prod.setMarca(rs.getString("marca"));
				else
					prod.setMarca("");
				prod.setCod_propal(rs.getString("cod_proppal"));
				if(rs.getString("origen")!=null)
					prod.setOrigen(rs.getString("origen"));
				else
					prod.setOrigen("");
				prod.setUn_base(rs.getString("un_base"));
				prod.setEan13(rs.getString("ean13"));
				if(rs.getString("un_empaque")!=null)
					prod.setUn_empaque(rs.getString("un_empaque"));
				else
					prod.setUn_empaque("");
				if(rs.getString("num_conv")!=null)
					prod.setNum_conv(new Integer(rs.getString("num_conv")));
				else
					prod.setNum_conv(new Integer(0));
				if(rs.getString("den_conv")!=null)
					prod.setDen_conv(new Integer(rs.getString("den_conv")));
				else
					prod.setDen_conv(new Integer(0));
				prod.setAtrib9(rs.getString("atrib9"));
				prod.setAtrib10(rs.getString("atrib10"));
				if(rs.getString("fcarga")!=null)
					prod.setFecCarga(rs.getTimestamp("fcarga"));
				else
					prod.setFecCarga(null);
				prod.setMixWeb(rs.getString("mix_web"));
				prod.setEstActivo(rs.getString("est"));
				prod.setNom_cat_sap(rs.getString("descat"));				

				prod.setSeccion(rs.getString("seccion"));
				prod.setRubro(rs.getString("rubro"));
				prod.setSubrubro(rs.getString("subrubro"));
				prod.setGrupo(rs.getString("grupo"));
				
				i++;
			}

			if(i==0) 
				throw new  ProductosSapDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductSapById - Problema SQL (close)", e);
			}
		}
		return prod;
	}
	
	/**
	* Actualiza datos del producto Sap
	* 
	* @param  prod ProductoSapEntity 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosSapDAOException
	*/
	public boolean updProduct(ProductoSapEntity prod) throws ProductosSapDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		try {

			conn = this.getConnection();
			logger.debug("en updProduct");
			String sql = "UPDATE bo_productos SET mix_web = ? WHERE id_producto = ? ";
			logger.debug(sql);
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, prod.getMixWeb());
			stm.setLong(2,prod.getId().longValue());
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updProduct - Problema SQL (close)", e);
			}
		}
		logger.debug("res?"+res);
		return res;
	}

	/**
	* Obtiene el listado de precios de un producto
	* 
	* @param  id_prod long 
	* @return List PrecioSapEntity 
	* @throws ProductosSapDAOException
	*/
	public List getPreciosByProdId(long id_prod) throws ProductosSapDAOException {
		List lista_precios = new ArrayList();
		PrecioSapEntity prec = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getPreciosByProdId:");
		
		try {

			String sql = " SELECT id_local, id_producto, cod_prod1, cod_local, prec_valor, umedida, " +
					"cod_barra, estadoactivo " + 
				" FROM bo_precios WHERE id_producto = ? AND bloq_compra = 'NO' AND estadoactivo='1' " +
				//agregar la condición de mix_local , debe ser igual a 1
				" AND mix_local=1 " ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				prec = new PrecioSapEntity();
				prec.setId_loc(new Long(rs.getString("id_local")));
				prec.setId_prod(new Long(rs.getString("id_producto")));
				prec.setCod_prod_1(rs.getString("cod_prod1"));
				prec.setCod_local(rs.getString("cod_local"));
				prec.setPrec_valor(new Double(rs.getString("prec_valor")));
				prec.setUni_med(rs.getString("umedida"));
				prec.setCod_barra(rs.getString("cod_barra"));
				prec.setEst_act(rs.getString("estadoactivo"));
				lista_precios.add(prec);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPreciosByProdId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_precios.size());
		return lista_precios;
	}

	/**
	* Agregar el precio a un producto Web.
	* 
	* @param  precio PrecioSapEntity 
	* @param  id_prod long 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>. 
	* @throws ProductosSapDAOException
	*/
	public boolean setPreciosByProdId(PrecioSapEntity precio, long id_prod) throws ProductosSapDAOException {
		boolean res = false;
		
		PreparedStatement stm = null;
		logger.debug("en setPreciosByProdId:");
		
		try {

			String sql = " INSERT INTO fo_precios_locales " +
					"(pre_loc_id, pre_pro_id, pre_costo, pre_valor, pre_stock, pre_estado) " + 
					" VALUES (?,?,?,?,?,?) " ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,precio.getId_loc().longValue());
			stm.setLong(2,id_prod);
			stm.setDouble(3,0);
			stm.setDouble(4,precio.getPrec_valor().doubleValue());
			stm.setInt(5,0);
			stm.setString(6,"A");
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
				throw new ProductosSapDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setPreciosByProdId - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}

	/**
	* Obtiene el listado de código de barras de un producto.
	* 
	* @param  id_prod long 
	* @return List CodBarraSapEntity  
	* @throws ProductosSapDAOException
	*/
	public List getCodBarrasByProdId(long id_prod) throws ProductosSapDAOException {
		
		List lista_codBarr = new ArrayList();
		CodBarraSapEntity codBarra = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCodBarrasByProdId:");
		
		try {

			String sql = " SELECT cod_prod1, cod_barra, tip_codbar, cod_ppal, unid_med, id_producto, estadoactivo " + 
				" FROM bo_codbarra WHERE id_producto = ? AND estadoactivo = '1' " ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				codBarra = new CodBarraSapEntity();
				codBarra.setCod_prod_1(rs.getString("cod_prod1"));
				codBarra.setCod_barra(rs.getString("cod_barra"));
				codBarra.setTip_cod_barra(rs.getString("tip_codbar"));
				codBarra.setCod_ppal(rs.getString("cod_ppal"));
				codBarra.setUni_med(rs.getString("unid_med"));
				codBarra.setId_prod(new Long(rs.getString("id_producto")));
				codBarra.setEstado(rs.getString("estadoactivo"));
				lista_codBarr.add(codBarra);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCodBarrasByProdId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_codBarr.size());
		return lista_codBarr;
	}

	/**
	* Agrega el código de barras a un producto Web.
	* 
	* @param  codBarra CodBarraSapEntity 
	* @param  id_prod long 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosSapDAOException
	*/
	public boolean setCodBarrasByProdId(CodBarraSapEntity codBarra, long id_prod) throws ProductosSapDAOException {
		
		boolean res = false;
		
		PreparedStatement stm = null;
		logger.debug("en setCodBarrasByProdId:");
		
		try {

			String sql = " INSERT INTO fo_cod_barra " +
					"(bar_codigo, bar_tipo_codigo, bar_pro_id, bar_estado) " + 
					" VALUES (?,?,?,?) " ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,codBarra.getCod_barra());
			stm.setString(2,codBarra.getTip_cod_barra());
			stm.setLong(3,id_prod);
			stm.setString(4,"A");//Activado
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
				throw new ProductosSapDAOException(String.valueOf(e.getErrorCode()),e);
			}
            throw new ProductosSapDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setCodBarrasByProdId - Problema SQL (close)", e);
			}
		}
		logger.debug("res:"+res);
		
		return res;
	}

}
