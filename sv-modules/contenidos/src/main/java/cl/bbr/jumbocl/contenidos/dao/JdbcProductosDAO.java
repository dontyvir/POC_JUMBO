package cl.bbr.jumbocl.contenidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.model.CategoriaEntity;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.FichaProductoEntity;
import cl.bbr.jumbocl.common.model.MarcaEntity;
import cl.bbr.jumbocl.common.model.MotivosDespEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoLogEntity;
import cl.bbr.jumbocl.common.model.UnidadMedidaEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModFichaTecnicaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoSugerDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCbarraDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;



public class JdbcProductosDAO  implements ProductosDAO{
	
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
	* @return
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
	* @throws ProductosDAOException
	*/
	public void setTrx(JdbcTransaccion trx)
	throws ProductosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ProductosDAOException(e);
		}
	}


	/**
	* Obtiene el máximo código que se le asigna a un producto genérico 
	* 
	* @return long, nuevo id de producto genérico
	* @throws ProductosDAOException
	*/
	public long getCodSapGenerico() throws ProductosDAOException {
		
		long id = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String sql = " INSERT INTO fo_secuencia (texto) VALUES (?)" ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setString(1,"Producto generico");
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCodSapGenerico - Problema SQL (close)", e);
			}
		}
		logger.debug("id:"+id);
		return id;
	}
	
	/**
	* Obtiene el listados de estados, segun el tipo de estado y si es visible en web o no  
	* 
	* @param  tip_estado String 
	* @param  visible String 
	* @return List EstadoEntity 
	* @throws ProductosDAOException
	*/
	public List getEstados(String tip_estado,String visible) throws ProductosDAOException {
		
		List list_estado = new ArrayList();
		EstadoEntity est = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		//visible
		String sqlWhere = "";
		if(!visible.equals(""))
			sqlWhere = " AND visible='"+visible+"' " ;
		try {

			conn = this.getConnection();
			String sql = " SELECT id, estado, tipo " +
				" FROM fo_estados " + " WHERE tipo='"+tip_estado + "' " + sqlWhere;
			stm = conn.prepareStatement(sql  + " WITH UR");

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
			throw new ProductosDAOException(e);
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
	* Obtiene el listados de unidades de medida  
	* 
	* @return List UnidadMedidaEntity  
	* @throws ProductosDAOException
	*/
	public List getUnidMedida() throws ProductosDAOException {
		
		List list_ume = new ArrayList();
		UnidadMedidaEntity ume = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			String sql = " SELECT uni_id, uni_desc, uni_cantidad, uni_estado" +
				" FROM fo_unidades_medida " + " WHERE uni_estado='A'" ;
			stm = conn.prepareStatement(sql  + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				ume = new UnidadMedidaEntity();
				ume.setId(new Long(rs.getString("uni_id")));
				ume.setDesc(rs.getString("uni_desc"));
				ume.setCantidad(new Double(rs.getDouble("uni_cantidad")));
				ume.setEstado(rs.getString("uni_estado"));
				list_ume.add(ume);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUnidMedida - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_ume.size());
		
		return list_ume;
	}
	
   /**
	* Obtiene el listado de los productos, segun los criterios: cod producto, cod sap de producto, estado, tipo
	* y cod categoria.
	* 
	* @param  criteria ProductosCriteriaDTO 
	* @return List ProductoEntity   
	* @throws ProductosDAOException
	*/
	public List listadoProductosByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException {
		
		List lista_productos = new ArrayList();
		ProductoEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en listadoProductosByCriteria:");
		
		//variable del criterio de búsqueda
		//cod de producto
		String cod_prod= criteria.getCodprod();
		String sqlCodProd = "";
		if (!cod_prod.equals(""))
			sqlCodProd = " AND p.pro_id ="+cod_prod;
		//cod sap de producto
		String cod_sap_prod = criteria.getProdsap();
		String sqlCodSapProd = "";
		
//realiza busqueda exacta, anexando caracter = antes del codigo sap

		if (!cod_sap_prod.equals("")){
			if( cod_sap_prod.indexOf('=')> -1)
				sqlCodSapProd = " AND p.pro_cod_sap = '"+cod_sap_prod.replaceAll("=","")+"'";
			else
				sqlCodSapProd = " AND p.pro_cod_sap LIKE '"+cod_sap_prod+"%'";
		}	
		
		//segun estado
		char estado = criteria.getEstado();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND p.pro_estado = \'"+estado+"\'";
		//segun tipo
		char tipo = criteria.getTipo();
		String sqlTipo = "";
		if (tipo!='0')
			sqlTipo = " AND p.pro_generico = '"+tipo+"'";
		//segun descrip
		String descrip = criteria.getDescrip();
		String sqlDesc = "";
		if (!descrip.equals("")){
			List patron = new ArrayList();
			patron.add(Formatos.frmSinEspacios(descrip.toUpperCase()));
			sqlDesc = " AND (" + Formatos.frmPatron(patron) + " ) ";
		}
		
		//id categoria seleccionada
		int id_cat = criteria.getId_cat();
		String sqlSelCat = "";
		String sqlSelCatFrom = "";
		if((id_cat !=0) && (id_cat !=-1)){
			sqlSelCatFrom = " JOIN fo_productos_categorias ON prca_pro_id = p.pro_id AND prca_cat_id = "+id_cat;			
		}
		//variables de paginacion
		int pag = criteria.getPag();
		int regXpag = criteria.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		// Se agrega columna "Marca" al SQL
		try {			
			String sql = " SELECT * FROM ( " +
				" SELECT row_number() over(order by p.pro_id) as row," +
				" p.pro_id pro_id, p.pro_tipo_producto pro_tipo_producto, m.mar_nombre as pro_marca, p.pro_des_corta pro_des_corta, p.pro_des_larga pro_des_larga, " +
				" p.pro_generico pro_generico, p.pro_estado pro_estado, me.uni_desc uni_desc, p.pro_cod_sap codsap, p.pro_tipre uni_med, " + 
                " p.pro_inter_valor, p.pro_inter_max " + sqlSelCat +
				" FROM fo_productos p" +
				" LEFT JOIN fo_unidades_medida me ON me.uni_id = p.pro_uni_id and me.uni_estado = 'A'  " +
				" " + sqlSelCatFrom +
				" LEFT JOIN fo_marcas m ON m.mar_id = p.pro_mar_id" +
				" WHERE p.pro_estado <>'E' " +
				sqlCodProd + sqlCodSapProd + sqlEstado + sqlTipo + sqlDesc +
				") AS TEMP " +
				"WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductoEntity();
				prod.setId( new Long(rs.getString("pro_id")));
				prod.setTipo(rs.getString("pro_tipo_producto"));
				prod.setDesc_corta(rs.getString("pro_des_corta"));
				prod.setDesc_larga(rs.getString("pro_des_larga"));
				prod.setGenerico(rs.getString("pro_generico"));
				prod.setEstado(rs.getString("pro_estado"));
				prod.setUni_med_desc(rs.getString("uni_med"));
				prod.setCod_sap(rs.getString("codsap"));
				prod.setNom_marca(rs.getString("pro_marca"));
				prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
				prod.setInter_max(new Double(rs.getString("pro_inter_max")));
				lista_productos.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : listadoProductosByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_productos.size());
		return lista_productos;
	}
	
   /**
	* Obtiene la cantidad de productos, segun los criterios: cod producto, cod sap de producto, estado, tipo
	* y cod categoria.
	* 
	* @param  criteria ProductosCriteriaDTO 
	* @return int   
	* @throws ProductosDAOException
	*/	
	public int getProductosCountByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException {
		
		int cantidad = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getProductosCountByCriteria:");
		
		//variable del criterio de búsqueda
		//cod de producto
		String cod_prod= criteria.getCodprod();
		String sqlCodProd = "";
		if (!cod_prod.equals(""))
			sqlCodProd = " AND pro_id ="+cod_prod;
		//cod sap de producto
		String cod_sap_prod = criteria.getProdsap();
		String sqlCodSapProd = "";
		if (!cod_sap_prod.equals(""))
			sqlCodSapProd = " AND pro_cod_sap LIKE '"+cod_sap_prod+"%'";
		//segun estado
		char estado = criteria.getEstado();
		String sqlEstado = "";
		if (estado!='0')
			sqlEstado = " AND pro_estado = \'"+estado+"\'";
		//segun tipo
		char tipo = criteria.getTipo();
		String sqlTipo = "";
		if (tipo!='0')
			sqlTipo = " AND pro_generico = '"+tipo+"'";
		//segun descrip
		String descrip = criteria.getDescrip();
		String sqlDesc = "";
		if (!descrip.equals("")){
			List patron = new ArrayList();
			patron.add(Formatos.frmSinEspacios(descrip.toUpperCase()));
			sqlDesc = " AND " + Formatos.frmPatron(patron);

		}
		
		//id categoria seleccionada
		int id_cat = criteria.getId_cat();
		String sqlSelCatFrom = "";
		if((id_cat !=0) && (id_cat !=-1)){
			sqlSelCatFrom = " JOIN fo_productos_categorias ON prca_pro_id = pro_id AND prca_cat_id = "+id_cat;
		}

		try {

			String sql = " SELECT count(pro_id) as cantidad " +
				" FROM fo_productos " + sqlSelCatFrom +
				" LEFT JOIN fo_unidades_medida me ON me.uni_id = pro_uni_id and me.uni_estado = 'A'  " +
				" LEFT JOIN bo_productos b ON b.id_producto = pro_id_bo " +
				" WHERE pro_estado <>'E' " +
				sqlCodProd + sqlCodSapProd + sqlEstado + sqlTipo + sqlDesc;
			logger.debug("Esta es la query "+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosCountByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+cantidad);
		return cantidad;
	}
	
	/**
	* Obtiene información del producto web
	* 
	* @param  idProd long 
	* @return ProductoEntity   
	* @throws ProductosDAOException
	*/
	public ProductoEntity getProductoById(long idProd) throws ProductosDAOException {
		
		ProductoEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductoById:");
		
		try {
			
			String sql = " SELECT PRD.PRO_ID, PRD.PRO_ID_PADRE, PRD.PRO_TIPRE, PRD.PRO_COD_SAP, " +
					"        PRD.PRO_UNI_ID, PRD.PRO_MAR_ID, PRD.PRO_ESTADO, " + 
					"        PRD.PRO_TIPO_PRODUCTO, PRD.PRO_DES_CORTA, PRD.PRO_DES_LARGA, " + 
					"        PRD.PRO_IMAGEN_MINIFICHA, PRD.PRO_IMAGEN_FICHA, " + 
					"        PRD.PRO_UNIDAD_MEDIDA, PRD.PRO_VALOR_DIFER, PRD.PRO_RANKING_VENTAS, " + 
					"        PRD.PRO_FCREA, PRD.PRO_FMOD, PRD.PRO_USER_MOD, PRD.PRO_GENERICO, " + 
					"        PRD.PRO_INTER_VALOR, PRD.PRO_INTER_MAX, PRD.PRO_PREPARABLE, " + 
					"        PRD.PRO_NOTA, PRD.PRO_ID_BO, PRD.PRO_PARTICIONABLE, PRD.PRO_PARTICION, " + 
					"        PRD.EVITAR_PUB_DES, PRD.PRO_ID_DESP, PRD.PILA_PORCION, " + 
					"        PRD.ID_PILA_UNIDAD, MAR.MAR_NOMBRE, " + 
					"        COALESCE(PROMO.PRM_NOM_BANNER,'') as pro_nom_banner, " + 
					"        COALESCE(PROMO.PRM_DESC_BANNER ,'') as pro_desc_banner, " + 
					"        COALESCE(PROMO.PRM_COLOR_BANNER ,'') as pro_color_banner, " + 
					"        NVL(GRB.PRO_PUBLICADO_MOVIL,0) as PRO_PUBLICADO_MOVIL " + 
					"   FROM fodba.FO_PRODUCTOS AS PRD " + 
					"   LEFT OUTER JOIN FODBA.FO_PRODUCTOS_PROMO AS PROMO " + 
					"        ON PRD.PRO_ID = PROMO.PRM_PROD_ID " + 
					"   LEFT OUTER JOIN fodba.FO_PROD_MOVIL AS GRB " + 
					"        ON GRB.PRO_ID_FO = PRD.PRO_ID " + 
					"   LEFT OUTER JOIN FODBA.FO_MARCAS AS MAR " + 
					"        ON PRO_MAR_ID = MAR.MAR_ID " + 
					"  WHERE PRD.PRO_ID = ? ";

			logger.debug(sql + " getProductoById, id: " + idProd);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, idProd);
			rs = stm.executeQuery();

			if (rs.next()) {
				prod = new ProductoEntity();
				prod.setId(new Long(rs.getString("pro_id")));
				if(rs.getString("pro_id_padre")!=null)
					prod.setId_padre(new Long(rs.getString("pro_id_padre")));
				else
					prod.setId_padre(new Long(0));
				prod.setTipre(rs.getString("pro_tipre"));
				prod.setCod_sap(rs.getString("pro_cod_sap"));
				if(rs.getString("pro_uni_id")!=null)
					prod.setUni_id(new Long(rs.getString("pro_uni_id")));
				else
					prod.setUni_id(new Long(0));
				if(rs.getString("pro_mar_id")!=null)
					prod.setMar_id(new Long(rs.getString("pro_mar_id")));
				else
					prod.setMar_id(new Long(0));
				prod.setEstado(rs.getString("pro_estado"));
				prod.setTipo(rs.getString("pro_tipo_producto"));
				prod.setDesc_corta(rs.getString("pro_des_corta"));
				prod.setDesc_larga(rs.getString("pro_des_larga"));
				prod.setImg_mini_ficha(rs.getString("pro_imagen_minificha"));
				prod.setImg_ficha(rs.getString("pro_imagen_ficha"));
				
				//tachar precios
				prod.setBanner_prod(rs.getString("pro_nom_banner"));
				prod.setDesc_banner_prod(rs.getString("pro_desc_banner"));
				prod.setColor_banner_prod(rs.getString("pro_color_banner"));
				
				if(rs.getString("pro_unidad_medida")!=null)
					prod.setUnidad_medidad(new Double(rs.getString("pro_unidad_medida")));
				else
					prod.setUnidad_medidad(new Double(0));
				prod.setValor_difer(rs.getString("pro_valor_difer"));
				if(rs.getString("pro_ranking_ventas")!=null)
					prod.setRank_ventas(new Integer(rs.getString("pro_ranking_ventas")));
				else
					prod.setRank_ventas(new Integer(0));
				prod.setFec_crea(rs.getTimestamp("pro_fcrea"));
				prod.setFec_mod(rs.getTimestamp("pro_fmod"));
				if(rs.getString("pro_user_mod")!=null)
					prod.setUser_mod(new Integer(rs.getString("pro_user_mod")));
				else
					prod.setUser_mod(new Integer(0));
				prod.setGenerico(rs.getString("pro_generico"));
				if(rs.getString("pro_inter_valor")!=null)
					prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
				else
					prod.setInter_valor(new Double("0"));
				if(rs.getString("pro_inter_max")!=null)
					prod.setInter_max(new Double(rs.getString("pro_inter_max")));
				else
					prod.setInter_max(new Double("0"));
				if(rs.getString("pro_preparable")!=null)
					prod.setEs_prep(rs.getString("pro_preparable"));
				else
					prod.setEs_prep("");
				if(rs.getString("pro_nota")!=null)
					prod.setAdm_coment(rs.getString("pro_nota"));
				else
					prod.setAdm_coment("");
				prod.setNom_marca(rs.getString("MAR_NOMBRE"));
				prod.setId_bo(new Long(rs.getLong("pro_id_bo")));
				prod.setEsParticionable(rs.getString("pro_particionable"));
				if (rs.getString("pro_particion") != null) {
				    prod.setParticion( new Integer( rs.getString("pro_particion") ) );
				}
				prod.setEvitarPubDes("S".equals(rs.getString("evitar_pub_des")) ? true : false);
				prod.setPro_id_desp(rs.getInt("pro_id_desp"));
                
                prod.setPilaPorcion(rs.getDouble("pila_porcion"));
                prod.setIdPilaUnidad(rs.getLong("id_pila_unidad"));
                
                //prod.setPublicadoGrability(1 == rs.getInt("pro_publicado_grability") ? true : false);
                prod.setPublicadoGrability(1 == rs.getInt("PRO_PUBLICADO_MOVIL") ? true : false);
                
				if(rs.wasNull())
				   prod.setPro_id_desp(-1);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductoById - Problema SQL (close)", e);
			}
		}
		//logger.debug("prod:"+prod.getDesc_corta());
		return prod;
	}
	
	/**
	* Obtiene el listado de items relacionados a un producto
	* 
	* @param  codProd long 
	* @return List ProductoEntity   
	* @throws ProductosDAOException
	*/
	public List getItemsByProductId(long codProd) throws ProductosDAOException {
		List list_items = new ArrayList();
		ProductoEntity pro = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			conn = this.getConnection();
			logger.debug("getItemsByProductId");
			String sql = "SELECT pro_id, pro_des_corta, pro_valor_difer, pro_tipo_producto, M.mar_nombre as marca" +
					" FROM fo_productos P " +
					" Left JOIN fo_marcas M on M.mar_id = P.pro_mar_id " +
					" WHERE pro_id_padre = ? ";	//and pro_generico = ? " ;
			logger.debug(sql);
			stm = conn.prepareStatement(sql  + " WITH UR");

			stm.setLong(1,codProd);
			//stm.setString(2,"P");	//indica que es un item
			rs = stm.executeQuery();
			while (rs.next()) {
				pro = new ProductoEntity();
				pro.setId(new Long(rs.getString("pro_id")));
				pro.setDesc_corta(rs.getString("pro_des_corta"));
				pro.setValor_difer(rs.getString("pro_valor_difer"));
				pro.setTipo(rs.getString("pro_tipo_producto"));
				pro.setNom_marca(rs.getString("marca"));
				list_items.add(pro);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getItemsByProductId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_items.size());
		
		return list_items;
		
	}

	/**
	* Agrega la relación entre un producto y un item.
	* 
	* @param  item ProductoEntity 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.   
	* @throws ProductosDAOException
	*/
	public boolean agregaItemProducto(ProductoEntity item) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en agregaItemProducto");
			String sql = "UPDATE fo_productos " +
				" SET pro_id_padre = ? " +
				" , pro_valor_difer = ? " +
			//	" , pro_estado = ? "  +
				" WHERE pro_id = ? ";
			logger.debug(sql);
			logger.debug("vals:"+item.getId_padre().longValue()+","+item.getValor_difer()+","+//item.getEstado()+","+
					item.getId().longValue());
			stm = conn.prepareStatement(sql);
			stm.setLong(1, item.getId_padre().longValue());
			stm.setString(2,item.getValor_difer());
			//stm.setString(3,item.getEstado());
			stm.setLong(3,item.getId().longValue());
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaItemProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Elimina la relación entre un producto y un item.
	* 
	* @param  item ProductoEntity 
	* @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.   
	* @throws ProductosDAOException
	*/
	public boolean eliminaItemProducto(ProductoEntity item) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		try {

			conn = this.getConnection();
			logger.debug("en eliminaItemProducto");
			String sql = "UPDATE fo_productos " +
				" SET pro_id_padre = null " +
				" , pro_valor_difer = '' " +
				" WHERE pro_id = ? ";
			logger.debug(sql);
			logger.debug("vals:"+item.getId());
			stm = conn.prepareStatement(sql);
			stm.setLong(1,item.getId().longValue());
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaItemProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	* Obtiene el listado de productos sugeridos de un producto.
	* 
	* @param  codProd long 
	* @return List ProductoEntity    
	* @throws ProductosDAOException
	*/
	public List getSugeridosByProductId(long codProd) throws ProductosDAOException {
		List list_items = new ArrayList();
		ProductoEntity pro = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			conn = this.getConnection();
			logger.debug("getSugeridosByProductId");
			
			String sql = " SELECT S.pro_id as id, S.pro_tipo_producto as tipo_prod, M.mar_nombre as marca, " + 
			" 				S.pro_des_corta as des_corta, PS.sug_id sug_id, PS.sug_formato sug_formato " +
			" FROM fo_productos S " +
			" LEFT JOIN fo_marcas M ON M.mar_id = S.pro_mar_id " + 
			" JOIN fo_pro_sugerencias PS ON S.pro_id = PS.sug_sug  " +
			" JOIN fo_productos G ON G.pro_id = PS.sug_base AND G.pro_id = ? ";
			 

			logger.debug(sql);
			stm = conn.prepareStatement(sql  + " WITH UR");

			stm.setLong(1,codProd);
			rs = stm.executeQuery();
			while (rs.next()) {
				pro = new ProductoEntity();
				pro.setId(new Long(rs.getString("id")));
				pro.setTipo(rs.getString("tipo_prod"));
				pro.setNom_marca(rs.getString("marca"));
				pro.setDesc_corta(rs.getString("des_corta"));
				pro.setId_padre(new Long(rs.getString("sug_id")));
				pro.setDesc_larga(rs.getString("sug_formato"));
				list_items.add(pro);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSugeridosByProductId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_items.size());
		
		return list_items;
		
	}

	/**
	* Agrega la relación entre un producto y un producto sugerido
	* 
	* @param  suger ProductoSugerDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>. 
	* @throws ProductosDAOException
	*/
	public boolean agregaSugeridoProducto(ProductoSugerDTO suger) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en agregaSugeridoProducto");
			String sql = "INSERT INTO fo_pro_sugerencias (sug_sug, sug_base, sug_fec_crea, "+//sug_estado," +
				" sug_formato) VALUES (?,?,?,?) ";
			logger.debug(sql);
			logger.debug("vals:"+suger.getId());
			stm = conn.prepareStatement(sql);
			stm.setLong(1, suger.getId_suger());
			stm.setLong(2, suger.getId_base());
			stm.setString(3,suger.getFec_crea());
			//stm.setString(4,suger.getEstado());
			stm.setString(4,suger.getFormato());
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			if(suger.getFormato().equals("B")){
				sql = "INSERT INTO fo_pro_sugerencias (sug_sug, sug_base, sug_fec_crea, "+//sug_estado," +
				" sug_formato) VALUES (?,?,?,?) ";
				logger.debug(sql);
				logger.debug("vals:"+suger.getId());
				stm = conn.prepareStatement(sql);
				stm.setLong(1, suger.getId_base());
				stm.setLong(2, suger.getId_suger());
				stm.setString(3,suger.getFec_crea());
				//stm.setString(4,suger.getEstado());
				stm.setString(4,suger.getFormato());
				
				i = stm.executeUpdate();
				if(i>0)
					result = true;
			}

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaSugeridoProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Eliminar la relación entre un producto y un producto sugerido
	* 
	* @param  id_suger long 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>. 
	* @throws ProductosDAOException
	*/
	public boolean eliminaSugeridoProducto(long id_suger) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en eliminaSugeridoProducto");
			String sql = "DELETE FROM  fo_pro_sugerencias " +
			" WHERE sug_id = ? ";
			logger.debug(sql);
			logger.debug("vals:"+id_suger);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_suger);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaSugeridoProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Obtiene el listado de categorías relacionadas a un producto
	* 
	* @param  codProd long 
	* @return List CategoriaEntity  
	* @throws ProductosDAOException
	*/
	public List getCategoriasByProductId(long codProd) throws ProductosDAOException {
		List list_cats = new ArrayList();
		CategoriaEntity cat = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			logger.debug("getCategoriasByProductId");
			String sql = " SELECT cat_id, cat_nombre, prca_id, prca_orden, prca_con_pago " +
				" FROM fo_productos, fo_productos_categorias, fo_categorias " +
				" WHERE cat_id = prca_cat_id AND prca_pro_id = pro_id AND pro_id = ?" ;
			logger.debug(sql);
			stm = conn.prepareStatement(sql  + " WITH UR");

			stm.setLong(1,codProd);
			rs = stm.executeQuery();
			while (rs.next()) {
				cat = new CategoriaEntity();
				cat.setId(new Long(rs.getString("cat_id")));
				cat.setNombre(rs.getString("cat_nombre"));
				cat.setProcat_id(new Long(rs.getString("prca_id")));
				if(rs.getString("prca_orden")!=null)
					cat.setOrden(new Integer(rs.getString("prca_orden")));
				else
					cat.setOrden(new Integer("0"));
				if(rs.getString("prca_con_pago")!=null)
					cat.setCon_pago(rs.getString("prca_con_pago"));
				else
					cat.setCon_pago("");
				
				list_cats.add(cat);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasByProductId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_cats.size());
		
		return list_cats;
	}
	
	/**
	* Actualiza información del producto segun el parámetro.
	* 
	* @param  param ProcModProductDTO 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean actualizaProductosById(ProcModProductDTO param) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		boolean result = false;
		long id_prod = param.getId_producto();
		try {
			
			conn = this.getConnection();
			String sql = "UPDATE fo_productos " +
				" SET pro_tipo_producto = ? , pro_mar_id = ?, pro_des_corta = ?, pro_des_larga = ? " +
				", pro_uni_id = ?, pro_unidad_medida = ?, pro_nota = ?, pro_preparable = ? " +
				", pro_inter_valor = ?, pro_inter_max = ?, pro_user_mod = ?, pro_fmod = ? "+
				", pro_imagen_minificha = ?, pro_imagen_ficha = ?, pro_valor_difer = ? " +
				", pro_particionable = ?, pro_particion = ?, evitar_pub_des = ? " +
				//", pro_publicado_grability = ? " +
				//", pro_nom_banner = ?, pro_desc_banner = ?, pro_obs_banner =?, pro_usr_banner=?, pro_datetime_banner=CURRENT_TIMESTAMP "+   //HB current_timestamp
				" WHERE pro_id = ? ";
			
			stm = conn.prepareStatement(sql);
			stm.setString(1,param.getTipo());
			stm.setInt(2,Integer.parseInt(param.getMarca()));
			stm.setString(3,param.getDescr_corta());
			stm.setString(4,param.getDesc_larga());
			if(param.getUmedida()!=null){
				stm.setLong(5,Integer.parseInt(param.getUmedida()));
			}else{
				stm.setNull(5,Types.INTEGER);
			}
			
			stm.setDouble(6,param.getContenido());
			stm.setString(7,param.getAdmite_com());
			stm.setString(8,param.getPreparable());
			stm.setDouble(9,param.getInterv_med());
			stm.setDouble(10,param.getMaximo());
			stm.setLong(11,param.getId_usr());
			stm.setString(12,Formatos.getFecHoraActual());
			stm.setString(13,param.getImg1());
			stm.setString(14,param.getImg2());
			stm.setString(15,param.getVal_dif());
			stm.setString(16,param.getEsParticionable());
			stm.setInt(17,Integer.parseInt(param.getParticion().toString()));
			stm.setString(18, param.isEvitarPubDes() ? "S" : "N");
			
			/*stm.setString(19,param.getImgBannerProducto());
			stm.setString(20,param.getDescBannerProducto());
			stm.setString(21,param.getColBannerProducto());  //pro_obs_banner se utilizra por colorbanner
			stm.setString(22,param.getUsr_login());*/
			
			//Seteo columna pro_publicado_grability
			//stm.setInt(19, param.isPublicadoGrability() ? 1 : 0);
			
			stm.setLong(19, id_prod);


			int i = stm.executeUpdate();
			
			if(i>0) {
				updateBannerProductoOnLy(id_prod, param.getImgBannerProducto(), param.getDescBannerProducto(), param.getColBannerProducto(), "UPD_FICHA_PROD", param.getId_usr());
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e);
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);
		}
		 catch (Exception ex) {
			logger.error(ex);			
			throw new ProductosDAOException(ex);
		}
		finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaProductosById - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	private int updateBannerProductoOnLy(long id_prod, String nombreBanner, String descripcionBanner, String colorBanner, String obs, long usr) throws DAOException { 
	    int respuestaSQL =0;
	     String sqlSelect= " SELECT PRM_PROD_ID FROM FODBA.FO_PRODUCTOS_PROMO WHERE PRM_PROD_ID = ?";
	     
	     String sqlUpdate = " UPDATE FODBA.FO_PRODUCTOS_PROMO SET " +
	      		"  PRM_NOM_BANNER = ? " +
	            ", PRM_DESC_BANNER = ? " +
	            ", PRM_OBS_BANNER = ?"  +
	            ", PRM_FCREACION = CURRENT_TIMESTAMP"+
	            ", PRM_USR_BANNER = ?"+ 
	            ", PRM_COLOR_BANNER = ?"+
	            "  WHERE PRM_PROD_ID = ?  ";
	     
	     String sqlInsert = " INSERT INTO FODBA.FO_PRODUCTOS_PROMO (PRM_PROD_ID,PRM_NOM_BANNER,PRM_DESC_BANNER,PRM_USR_BANNER,PRM_OBS_BANNER,PRM_FCREACION,PRM_COLOR_BANNER) " +
		            " VALUES (?,?,?,?,?,CURRENT_TIMESTAMP,?) ";

	     String sqlDelete = " DELETE FROM FODBA.FO_PRODUCTOS_PROMO WHERE PRM_PROD_ID = ? ";
	     
	      Connection con = null;
	      PreparedStatement psUpdate = null;
	      PreparedStatement psSelect = null;
	      PreparedStatement psInsert = null;
	      PreparedStatement psDelete = null;	      
	      ResultSet rsSelect = null;
	      
	      try {
	         con = this.getConnection();
	        
	         psSelect = con.prepareStatement(sqlSelect);
	         psSelect.setLong(1, id_prod);
	         rsSelect = psSelect.executeQuery();
	         if(rsSelect.next()){
	        	 if("".equals(nombreBanner) && "".equals(descripcionBanner) && "".equals(colorBanner)){
	        		psDelete = con.prepareStatement(sqlDelete);
	        		psDelete.setLong(1, id_prod);
	        		respuestaSQL = psDelete.executeUpdate();	
	        	 }else{
			         psUpdate = con.prepareStatement(sqlUpdate);
			         if("".equals(nombreBanner)){
			        	 nombreBanner="img_vacio.gif";
			         }
			         psUpdate.setString(1, nombreBanner);
			         psUpdate.setString(2, descripcionBanner);
			         psUpdate.setString(3, obs);
			         psUpdate.setLong(4, usr);
			         psUpdate.setString(5, colorBanner);
			         psUpdate.setLong(6, id_prod);
			         respuestaSQL = psUpdate.executeUpdate();	        			
	        	 }
	         }else{
	        	 if(!"".equals(nombreBanner) || !"".equals(descripcionBanner)){
		        	 psInsert = con.prepareStatement(sqlInsert);
		        	 psInsert.setLong(1, id_prod);
		        	 if(!nombreBanner.matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)")){
		        		 nombreBanner="img_vacio.gif";
		        	 }
		        	 psInsert.setString(2, nombreBanner);
		        	 psInsert.setString(3, descripcionBanner);
		        	 psInsert.setLong(4, usr);
		        	 psInsert.setString(5, obs);
		        	 
		        	 if(!colorBanner.matches("^#[A-Z0-9]{6}")){
		        		 colorBanner="#B53137";
		        	 }
		        	 psInsert.setString(6, colorBanner);
		        	 respuestaSQL = psInsert.executeUpdate();
	        	 }
	         }
	      } catch (Exception e) {
	    	  logger.error(e);
	         throw new DAOException(e);
	      } finally {
	      	 try {
	      		if (psUpdate != null)
	      			psUpdate.close();
	      		if (psSelect != null)
	      			psSelect.close();
				if (psInsert != null)
					psInsert.close();
				if (psDelete != null)
					psDelete.close();				
				if (rsSelect != null)
					rsSelect.close();
	      		
			} catch (Exception e1) {
				logger.error("[Metodo] : Banner de Promosion - Problema SQL (close)", e1);
			}

	      }
	      return respuestaSQL;
	   }
	

	/**
	* Agregar un producto web
	* 
	* @param  param ProcAddProductDTO 
	* @return int, nuevo código.
	* @throws ProductosDAOException
	*/
	public int agregaProducto(ProcAddProductDTO param) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int id = -1;
		try {

			conn = this.getConnection();
			logger.debug("en agregaProducto");
			String sql = "INSERT INTO fo_productos (pro_cod_sap, pro_estado, pro_generico, pro_tipre, " +
			" pro_tipo_producto, pro_mar_id, pro_des_corta, pro_des_larga, " +
			" pro_uni_id, pro_unidad_medida, pro_nota, pro_preparable, pro_inter_valor, pro_inter_max, "+
			" pro_fcrea, pro_user_mod, pro_id_bo, pro_imagen_minificha, pro_imagen_ficha, pro_valor_difer ) " +
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.debug(sql);
				
			logger.debug("val:"+param.getCod_sap()+","+param.getEstado()+","+param.getGenerico()+","
					+param.getUni_vta()+","+param.getTipo()+","+param.getMarca()+","+param.getDesc_corta()+","
					+param.getDesc_larga()+","+param.getUni_medida()+","+param.getContenido()+","
					+param.getAdm_coment()+","+param.getEs_prepar()+","+param.getInt_medida()+","
					+param.getInt_max()+","+param.getFecha()+","+param.getId_user()+","+param.getId_bo()+","
					+param.getImg_min_ficha()+","+param.getImg_ficha()+","+param.getAtr_difer());
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stm.setString(1, param.getCod_sap());
			stm.setString(2, param.getEstado());
			stm.setString(3, param.getGenerico());
			stm.setString(4, param.getUni_vta());
			stm.setString(5, param.getTipo());
			stm.setLong(6, param.getMarca());
			stm.setString(7, param.getDesc_corta());
			stm.setString(8, param.getDesc_larga());
			if(param.getUni_medida()!=0)
				stm.setInt(9, param.getUni_medida());
			else
				stm.setNull(9,Types.INTEGER);
			stm.setDouble(10, param.getContenido());
			stm.setString(11, param.getAdm_coment());
			stm.setString(12, param.getEs_prepar());
			stm.setDouble(13, param.getInt_medida());
			stm.setDouble(14, param.getInt_max());
			stm.setString(15, param.getFecha());
			stm.setLong(16,param.getId_user());
			stm.setLong(17,param.getId_bo());
			stm.setString(18, param.getImg_min_ficha());
			stm.setString(19, param.getImg_ficha());
			stm.setString(20, param.getAtr_difer());
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaProducto - Problema SQL (close)", e);
			}
		}
		return id;
	}
	
	
	
	
	
	
	public int agregaProductoBolsa(ProcAddProductDTO param) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int id = -1;
		try {

			conn = this.getConnection();
			logger.debug("en agregaProducto 2");
			String sql = "INSERT INTO fo_productos (pro_cod_sap, pro_estado, pro_generico, pro_tipre, " +
			" pro_tipo_producto, pro_mar_id, pro_des_corta, pro_des_larga, " +
			" pro_uni_id, pro_unidad_medida, pro_nota, pro_preparable, pro_inter_valor, pro_inter_max, "+
			" pro_fcrea, pro_user_mod, pro_id_bo, pro_imagen_minificha, pro_imagen_ficha, pro_valor_difer ) " +
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.debug(sql);
				
			logger.debug("val:"+param.getCod_sap()+","+param.getEstado()+","+param.getGenerico()+","
					+param.getUni_vta()+","+param.getTipo()+","+param.getMarca()+","+param.getDesc_corta()+","
					+param.getDesc_larga()+","+param.getUni_medida()+","+param.getContenido()+","
					+param.getAdm_coment()+","+param.getEs_prepar()+","+param.getInt_medida()+","
					+param.getInt_max()+","+param.getFecha()+","+param.getId_user()+","+param.getId_bo()+","
					+param.getImg_min_ficha()+","+param.getImg_ficha()+","+param.getAtr_difer());
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stm.setString(1, param.getCod_sap());
			stm.setString(2, param.getEstado());
			stm.setString(3, param.getGenerico());
			stm.setString(4, param.getUni_vta());
			stm.setString(5, param.getTipo());
			stm.setLong(6, param.getMarca());
			stm.setString(7, param.getDesc_corta());
			stm.setString(8, param.getDesc_larga());
			if(param.getUni_medida()!=0)
				stm.setInt(9, param.getUni_medida());
			else
				stm.setNull(9,Types.INTEGER);
			stm.setDouble(10, param.getContenido());
			stm.setString(11, param.getAdm_coment());
			stm.setString(12, param.getEs_prepar());
			stm.setDouble(13, param.getInt_medida());
			stm.setDouble(14, param.getInt_max());
			stm.setString(15, param.getFecha());
			stm.setLong(16,param.getId_user());
			stm.setLong(17,param.getId_bo());
			stm.setString(18, param.getImg_min_ficha());
			stm.setString(19, param.getImg_ficha());
			stm.setString(20, param.getAtr_difer());
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaProductoBolsa - Problema SQL (close)", e);
			}
		}
		return id;
	}
	
	/**
	* Agregar un log de evento al producto web
	* 
	* @param  log ProductoLogDTO 
	* @return int, nuevo código.
	* @throws ProductosDAOException
	*/
	public int agregaLogProducto(ProductoLogDTO log) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int id = -1;
		try {

			conn = this.getConnection();
			logger.debug("en agregaLogProducto");
			if(log.getUsuario()==null)
				log.setUsuario("Usuario 1");
			String sql = "INSERT INTO fo_pro_tracking (tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto ) " +
				" VALUES (?,?, current_timestamp,?,?)";
			logger.debug(sql);
			logger.debug("val:"+log.getCod_prod()+","+log.getCod_prod_bo()+","+log.getFec_crea()+","+log.getUsuario()+","+log.getTexto());
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stm.setLong(1, log.getCod_prod());
			stm.setLong(2, log.getCod_prod_bo());
			//stm.setString(3, log.getFec_crea());
			stm.setString(3, log.getUsuario());
			stm.setString(4, log.getTexto());
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaLogProducto - Problema SQL (close)", e);
			}
		}
		return id;
		
	}
	
	/**
	* Verifica si una categoria esta relacionada a un producto 
	* 
	* @param  id_cat long 
	* @param  id_prod long 
	* @return boolean, devuelve <i>true</i> si la relación existe, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean isCatAsocProd(long id_cat, long id_prod) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en isCatAsocProd");
			String sql = "SELECT count(*) as cantidad FROM fo_productos_categorias " +
					" WHERE prca_cat_id=? AND prca_pro_id=? ";
			logger.debug(sql);
			logger.debug("valores:"+id_cat+","+id_prod);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, id_cat);
			stm.setLong(2, id_prod);
			int cant = 0;
			rs = stm.executeQuery();
			if(rs.next())
				cant = rs.getInt("cantidad");
			if(cant>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isCatAsocProd - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	* Agrega la relación entre la categoría y el producto. 
	* 
	* @param  param ProcAddCategoryProductDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean agregaCategProducto(ProcAddCategoryProductDTO param) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		
		try {

			conn = this.getConnection();
			logger.debug("en agregaCategProducto");
			String sql = "INSERT INTO fo_productos_categorias (prca_cat_id, prca_pro_id, prca_estado, " +
				"prca_orden, prca_con_pago)" + " VALUES (?,?,?,?,?)";
			logger.debug(sql);
			logger.debug("valores:"+param.getId_categoria()+","+param.getId_producto()+","+param.getEstado()+","+
					param.getOrden()+","+param.getCon_pago());
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, param.getId_categoria());
			stm.setLong(2, param.getId_producto());
			stm.setString(3, param.getEstado());
			stm.setInt(4, param.getOrden());
			stm.setString(5, param.getCon_pago());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ // -530: No existe id_padre
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaCategProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	/**
	* Actualiza la relación entre la categoría y el producto. 
	* 
	* @param  param ProductoCategDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean ModCategoryProduct(ProductoCategDTO param) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		
		try {

			conn = this.getConnection();
			logger.debug("en agregaCategProducto");
			String sql = "UPDATE fo_productos_categorias SET prca_pro_id = ?, prca_orden = ?, prca_con_pago =? WHERE prca_id = ?";
			logger.debug(sql);
			logger.debug("prca_pro_id"+param.getId_prod());
			logger.debug("prca_orden"+param.getOrden());
			logger.debug("prca_con_pago"+param.getCon_pago());
			logger.debug("prca_id"+param.getId());
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, param.getId_prod());
			stm.setInt(2, param.getOrden());
			stm.setString(3, param.getCon_pago());
			stm.setLong(4, param.getId());			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ // -530: No existe id_padre
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : ModCategoryProduct - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	
	/**
	* Actualiza la relación entre la categoría y el producto. 
	* 
	* @param  param ProcAddCategoryProductDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean actualizaCategProducto(ProcAddCategoryProductDTO param) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		
		try {

			conn = this.getConnection();
			logger.debug("en agregaCategProducto");
			String sql = "INSERT INTO fo_productos_categorias (prca_cat_id, prca_pro_id, prca_estado, " +
				"prca_orden, prca_con_pago)" + " VALUES (?,?,?,?,?)";
			logger.debug(sql);
			logger.debug("valores:"+param.getId_categoria()+","+param.getId_producto()+","+param.getEstado()+","+
					param.getOrden()+","+param.getCon_pago());
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, param.getId_categoria());
			stm.setLong(2, param.getId_producto());
			stm.setString(3, param.getEstado());
			stm.setInt(4, param.getOrden());
			stm.setString(5, param.getCon_pago());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ // -530: No existe id_padre
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaCategProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	* Elimina la relación entre la categoría y el producto. 
	* 
	* @param  param ProcDelCategoryProductDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean eliminaCategProducto(ProcDelCategoryProductDTO param) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en eliminaCategProducto");
			String sql = "DELETE FROM fo_productos_categorias " +
				" WHERE prca_cat_id = ? AND prca_pro_id = ? ";
			logger.debug(sql);
			logger.debug("valores:"+param.getId_categoria()+","+param.getId_producto());
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, param.getId_categoria());
			stm.setLong(2, param.getId_producto());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaCategProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	* Actualiza información del producto, en caso se publique o despublique. 
	* 
	* @param  param ProcPubDespProductDTO 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	* @throws ProductosDAOException
	*/
	public boolean setPubDespProduct(ProcPubDespProductDTO param) throws ProductosDAOException {
		boolean res = false;
		
		PreparedStatement stm = null;
		String estado= "";
		try {

			conn = this.getConnection();
			if(param.getAction()==1) {
				//publicar
				estado = Constantes.ESTADO_PUBLICADO; 
			}else if(param.getAction()==2) {
				//despublicar
				estado = Constantes.ESTADO_DESPUBLICADO; 
			}
			String sql = "UPDATE fo_productos SET pro_estado = ? ,";
			if(param.getAction()==1){
				sql += " pro_tipo_producto = ?, pro_mar_id = ?, pro_des_corta = ?, pro_des_larga = ?, pro_uni_id = ?, " + 
					" pro_unidad_medida = ?, pro_nota = ?, pro_preparable = ?, pro_inter_valor = ?, pro_inter_max = ?, "+
					" pro_user_mod = ?, pro_fmod = ?";
			}else if(param.getAction()==2){
				sql += " pro_id_desp ="+param.getId_motivo();
			}
			
			sql += " WHERE pro_id = ?" ; 
			logger.debug(sql);
			logger.debug("id_prod:"+param.getId_producto());
			logger.debug("estado:"+estado);
			
			stm = conn.prepareStatement(sql);
			
			logger.debug("en setPubDespProduct");
			stm.setString(1, estado);
			if(param.getAction()==1){
				stm.setString(2,param.getTipo());
				if (param.getIdMarca()!=null && !param.getIdMarca().equals("")) {
					stm.setInt(3,Integer.parseInt(param.getIdMarca()));
				}
				else {
					stm.setNull(3,Types.INTEGER);
				}
				stm.setString(4,param.getDesCor());
				stm.setString(5,param.getDesLar());
				if (param.getUniMed()!=null && !param.getUniMed().equals("")) {
					stm.setInt(6,Integer.parseInt(param.getUniMed()));
				}
				else {
					stm.setNull(6,Types.INTEGER);
				}
				stm.setDouble(7,param.getConten());
				stm.setString(8,param.getAdmCom());
				stm.setString(9,param.getEsPrep());
				stm.setDouble(10,param.getIntVal());
				stm.setDouble(11,param.getIntMax());
				stm.setLong(12,param.getId_usr());
				stm.setString(13, Formatos.getFecHoraActual());				
				stm.setLong(14,param.getId_producto()); 
			} 
			if(param.getAction()==2)
				stm.setLong(2,param.getId_producto());
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setPubDespProduct - Problema SQL (close)", e);
			}
		}
		
		return res;
	}
	
	/**
	* Obtiene el listado de productos de un producto genérico. 
	* 
	* @param  cod_prod long 
	* @return List ProductoEntity  
	* @throws ProductosDAOException
	*/
	public List getProductGenericos(long cod_prod) throws ProductosDAOException {
		List lst_prods = null;
		
		ProductoEntity prod = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			logger.debug("en getProductGenericos");
			String sql = "SELECT G.pro_id as id_prod, G.pro_des_corta as descrip FROM fo_productos G, fo_productos P " +
				" WHERE G.pro_id = P.pro_id_padre AND P.pro_id = ? ";
			logger.debug(sql);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1,cod_prod);
			logger.debug("cod_prod:"+cod_prod);
			rs = stm.executeQuery();
			
			while(rs.next()){
				logger.debug("en while");
				prod = new ProductoEntity();
				prod.setId(new Long(rs.getString("id_prod")));
				prod.setDesc_corta(rs.getString("descrip"));
				lst_prods.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductGenericos - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_prods.size());
		return lst_prods;
	}
	
	/**
	* Elimina un producto. 
	* 
	* @param  param ProcDelGenericProductDTO 
	* @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.  
	* @throws ProductosDAOException
	*/
	public boolean eliminaProducto(ProcDelGenericProductDTO param) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			String sql = "DELETE FROM fo_productos WHERE pro_id = ? ";
			logger.debug(sql);
			stm = conn.prepareStatement(sql);
			
			logger.debug("en eliminaProducto");
			stm.setLong(1, param.getId_producto());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Obtiene el listado de logs de eventos del producto 
	* 
	* @param  cod_prod long 
	* @return List ProductoLogEntity    
	* @throws ProductosDAOException
	*/
	public List getLogByProductId(long cod_prod) throws ProductosDAOException {
		
		List lst_log = new ArrayList();
		ProductoLogEntity log = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			String sql = " SELECT tra_id, tra_pro_id, tra_fec_crea, tra_usuario, tra_texto " +
				" FROM fo_pro_tracking WHERE tra_pro_id = ? ORDER BY 3 DESC"  ;
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,cod_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				log = new ProductoLogEntity();
				log.setId(new Long(rs.getString("tra_id")));
				log.setCod_prod(new Long(rs.getString("tra_pro_id")));
				log.setFec_crea(rs.getTimestamp("tra_fec_crea"));
				log.setUsuario(rs.getString("tra_usuario"));
				log.setTexto(rs.getString("tra_texto"));
				lst_log.add(log);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLogByProductId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_log.size());
		return lst_log;
	}
	
	/**
	* Obtiene el listado de marcas 
	* 
	* @return List MarcaEntity    
	* @throws ProductosDAOException
	*/
	public List getMarcas() throws ProductosDAOException {
		
		List lstMrc = new ArrayList();
		MarcaEntity mrc = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			String sql = " SELECT mar_id, mar_nombre, mar_estado " +
				" FROM fo_marcas " +
				" WHERE mar_estado = ? " +
				" ORDER BY UPPER(mar_nombre) ";
			stm = conn.prepareStatement(sql  + " WITH UR"  );
			stm.setString(1,Constantes.ESTADO_ACTIVADO);
			rs = stm.executeQuery();
			while (rs.next()) {
				mrc = new MarcaEntity();
				mrc.setId(new Long(rs.getString("mar_id")));
				mrc.setNombre(rs.getString("mar_nombre"));
				mrc.setEstado(rs.getString("mar_estado"));
				lstMrc.add(mrc);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMarcas - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lstMrc.size());
		return lstMrc;
	}
	
	/**
	* Obtiene el listado de marcas 
	* 
	* @param  criterio MarcasCriteriaDTO 
	* @return List ProductoLogEntity    
	* @throws ProductosDAOException
	*/
	public List getMarcas(MarcasCriteriaDTO criterio) throws ProductosDAOException {
		
		List lstMrc = new ArrayList();
		MarcaEntity mrc = null;
		
		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			logger.debug("En getMarcas(criterio)...");
			conn = this.getConnection();
			String sql = " select * from ( " +
					" SELECT row_number() over(order by UPPER(mar_nombre)) as row," +
					" mar_id, mar_nombre, mar_estado,  count(  p.pro_id)  cant_prod " +
					" FROM fo_marcas  " +
					" LEFT JOIN fo_productos p ON p.pro_mar_id = mar_id" +
					" GROUP BY mar_id, mar_nombre, mar_estado " +
					" ) AS TEMP WHERE row BETWEEN "+iniReg+" AND "+finReg ;
			logger.debug("SQL:"+sql);
			stm = conn.prepareStatement(sql   + " WITH UR" );
			rs = stm.executeQuery();
			while (rs.next()) {
				mrc = new MarcaEntity();
				mrc.setId(new Long(rs.getString("mar_id")));
				mrc.setNombre(rs.getString("mar_nombre"));
				mrc.setEstado(rs.getString("mar_estado"));
				mrc.setCant_prods(rs.getInt("cant_prod"));
				lstMrc.add(mrc);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMarcas - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lstMrc.size());
		return lstMrc;
	}

	/**
	 * Obtiene la cantidad total de marcas.
	 * 
	 * @return int, cantidad
	 * @throws ProductosDAOException
	 */
	public int getMarcasAllCount() throws ProductosDAOException {
		
		int cant = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			String sql = " SELECT count(mar_id)  cant FROM fo_marcas ";
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cant");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMarcasAllCount - Problema SQL (close)", e);
			}
		}
		
		return cant;
	}
	
	/**
	* Obtiene datos de la marca 
	* 
	* @param  id long 
	* @return MarcaEntity
	* @throws ProductosDAOException
	*/
	public MarcaEntity getMarcaById(long id) throws ProductosDAOException {
		
		MarcaEntity mrc = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			String sql = " SELECT mar_id, mar_nombre, mar_estado " +
				" FROM fo_marcas " + " WHERE mar_id = ? " ;
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, id);
			rs = stm.executeQuery();
			if (rs.next()) {
				mrc = new MarcaEntity();
				mrc.setId(new Long(rs.getString("mar_id")));
				mrc.setNombre(rs.getString("mar_nombre"));
				mrc.setEstado(rs.getString("mar_estado"));
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMarcaById - Problema SQL (close)", e);
			}
		}
		
		return mrc;
	}
	
	/** 
	 * Agrega una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean addMarca(MarcasDTO prm) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		try {

			conn = this.getConnection();
			String sql = " INSERT INTO FO_MARCAS (mar_nombre, mar_estado) " +
				" VALUES (?,?) " ;
			logger.debug(sql);
			logger.debug("nombre:"+prm.getNombre());
			logger.debug("estado"+prm.getEstado());
			stm = conn.prepareStatement(sql);
			stm.setString(1, prm.getNombre());
			stm.setString(2, prm.getEstado());
			int i = stm.executeUpdate();
			if (i>0) {
				result = true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addMarca - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Actualiza información de la marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean modMarca(MarcasDTO prm) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		try {

			conn = this.getConnection();
			String sql = " UPDATE FO_MARCAS SET mar_nombre =?, mar_estado=? " +
				" WHERE mar_id = ? " ;
			stm = conn.prepareStatement(sql);
			stm.setString(1, prm.getNombre());
			stm.setString(2, prm.getEstado());
			stm.setLong(3,prm.getId());
			
			int i = stm.executeUpdate();
			if (i>0) {
				result = true;
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modMarca - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Elimina una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean setDelMarca(MarcasDTO prm) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		try {

			conn = this.getConnection();
			String sql = " DELETE FROM FO_MARCAS " +
				" WHERE mar_id = ? " ;
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prm.getId());
			
			int i = stm.executeUpdate();
			if (i>0) {
				result = true;
			}
			if(i==0){
				throw new ProductosDAOException(Constantes._EX_MAR_ID_NO_EXISTE);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setDelMarca - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
   /**
	* Permite obtener el producto, segun codigo sap y unidad de medida del Producto MIX 
	* 
	* @param  cod_sap_prod String 
	* @param  uni_med String 
	* @return ProductoEntity
	* @throws ProductosDAOException
	* */
	public ProductoEntity getProductByProdMix(String cod_sap_prod, String uni_med) throws ProductosDAOException {
		
		ProductoEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductByProdMix:");
		
		try {

			String sql = " SELECT pro_id, pro_id_padre, pro_tipre, pro_cod_sap, pro_uni_id, pro_mar_id, pro_estado, " +
				" pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, " +
				" pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, " +
				" pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota " +
				" FROM fo_productos " + 
				" WHERE pro_cod_sap = ? AND pro_tipre = ? " ;
			logger.debug(sql+"val:"+cod_sap_prod+","+uni_med);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setString(1, cod_sap_prod);
			stm.setString(2, uni_med);
			rs = stm.executeQuery();
			if (rs.next()) {
				prod = new ProductoEntity();
				prod.setId(new Long(rs.getString("pro_id")));
				if(rs.getString("pro_id_padre")!=null)
					prod.setId_padre(new Long(rs.getString("pro_id_padre")));
				else
					prod.setId_padre(new Long(0));
				prod.setTipre(rs.getString("pro_tipre"));
				prod.setCod_sap(rs.getString("pro_cod_sap"));
				if(rs.getString("pro_uni_id")!=null)
					prod.setUni_id(new Long(rs.getString("pro_uni_id")));
				else
					prod.setUni_id(new Long(0));
				if(rs.getString("pro_mar_id")!=null)
					prod.setMar_id(new Long(rs.getString("pro_mar_id")));
				else
					prod.setMar_id(new Long(0));
				prod.setEstado(rs.getString("pro_estado"));
				prod.setTipo(rs.getString("pro_tipo_producto"));
				prod.setDesc_corta(rs.getString("pro_des_corta"));
				prod.setDesc_larga(rs.getString("pro_des_larga"));
				prod.setImg_mini_ficha(rs.getString("pro_imagen_minificha"));
				prod.setImg_ficha(rs.getString("pro_imagen_ficha"));
				if(rs.getString("pro_unidad_medida")!=null)
					prod.setUnidad_medidad(new Double(rs.getString("pro_unidad_medida")));
				else
					prod.setUnidad_medidad(new Double(0));
				prod.setValor_difer(rs.getString("pro_valor_difer"));
				if(rs.getString("pro_ranking_ventas")!=null)
					prod.setRank_ventas(new Integer(rs.getString("pro_ranking_ventas")));
				else
					prod.setRank_ventas(new Integer(0));
				prod.setFec_crea(rs.getTimestamp("pro_fcrea"));
				prod.setFec_mod(rs.getTimestamp("pro_fmod"));
				if(rs.getString("pro_user_mod")!=null)
					prod.setUser_mod(new Integer(rs.getString("pro_user_mod")));
				else
					prod.setUser_mod(new Integer(0));
				prod.setGenerico(rs.getString("pro_generico"));
				if(rs.getString("pro_inter_valor")!=null)
					prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
				else
					prod.setInter_valor(new Double("0"));
				if(rs.getString("pro_inter_max")!=null)
					prod.setInter_max(new Double(rs.getString("pro_inter_max")));
				else
					prod.setInter_max(new Double("0"));
				if(rs.getString("pro_preparable")!=null)
					prod.setEs_prep(rs.getString("pro_preparable"));
				else
					prod.setEs_prep("");
				if(rs.getString("pro_nota")!=null)
					prod.setAdm_coment(rs.getString("pro_nota"));
				else
					prod.setAdm_coment("");
				prod.setNom_marca("");
				logger.debug("si tiene");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductByProdMix - Problema SQL (close)", e);
			}
		}
		logger.debug("saliendo");

		return prod;
	}

	/**
	 * Permite agregar un producto en la tabla fo_productos, del tipo Item.
	 * La información se obtiene de la tabla bo_productos
	 * 
	 * @param  param ProcAddProductDTO 
	 * @return int
	 * @throws ProductosDAOException
	 * */
	public int addByProductMix(ProcAddProductDTO param) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int id = -1;
		try {

			conn = this.getConnection();
			logger.debug("en addByProductMix");
			String sql = "INSERT INTO fo_productos (pro_cod_sap, pro_estado, pro_generico, pro_tipre, " +
				" pro_tipo_producto, pro_des_corta, pro_des_larga, " +
				" pro_unidad_medida, pro_nota, pro_preparable, pro_inter_valor, pro_inter_max, "+
				" pro_fcrea, pro_user_mod, pro_id_bo, pro_imagen_minificha, pro_imagen_ficha ) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?)";
			logger.debug(sql);
			
			logger.debug("valores:"+param.getCod_sap()+","+param.getEstado()+","+param.getGenerico()+","
					+param.getUni_vta()+","+param.getTipo()+","+param.getDesc_corta()+","
					+param.getDesc_larga()+","+param.getContenido()+","
					+param.getAdm_coment()+","+param.getEs_prepar()+","+param.getInt_medida()+","
					+param.getInt_max()+","+param.getFecha()+","+param.getId_user()+","+param.getId_bo()+","
					+param.getImg_min_ficha()+","+param.getImg_ficha());
			
			
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stm.setString(1, param.getCod_sap());
			stm.setString(2, param.getEstado());
			stm.setString(3, param.getGenerico());
			stm.setString(4, param.getUni_vta());
			stm.setString(5, param.getTipo());
			stm.setString(6, param.getDesc_corta());
			stm.setString(7, param.getDesc_larga());
			stm.setDouble(8, param.getContenido());
			stm.setString(9, param.getAdm_coment());
			stm.setString(10, param.getEs_prepar());
			stm.setDouble(11, param.getInt_medida());
			stm.setDouble(12, param.getInt_max());
			stm.setString(13, param.getFecha());
			stm.setLong(14, param.getId_user());
			stm.setLong(15, param.getId_bo());			
			stm.setString(16, param.getImg_min_ficha());
			stm.setString(17, param.getImg_ficha());
			
			stm.executeUpdate();
			
				rs = stm.getGeneratedKeys();
		        if (rs.next()) {
		            id = rs.getInt(1);
		        }

		} catch(SQLException sqlEx){
			sqlEx.getErrorCode();
			logger.debug("sqlEx:"+sqlEx);
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addByProductMix - Problema SQL (close)", e);
			}
		}
		return id;
	}

	/**
	 * Permite eliminar el producto en fo_productos.
	 * Solo se elimina un producto, segun codigo sap y unidad de medida. 
	 * 
	 * @param  cod_sap_prod String 
	 * @param  uni_med String 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean delByProductMix(String cod_sap_prod, String uni_med) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en delByProductMix");
			String sql = "DELETE FROM fo_productos WHERE pro_cod_sap = ? AND pro_tipre = ? ";
			logger.debug(sql);
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, cod_sap_prod);
			stm.setString(2, uni_med);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delByProductMix - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Eliminar log referente al producto Mix a eliminar
	 * 
	 * @param  cod_sap_prod String 
	 * @param  uni_med String 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean delLogByProdMix(String cod_sap_prod, String uni_med) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en delLogByProdMix");
			String sql = "DELETE FROM fo_pro_tracking "+
				" WHERE tra_pro_id in (SELECT pro_id FROM fo_productos WHERE pro_cod_sap = ? AND pro_tipre = ?) ";
			logger.debug(sql);
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, cod_sap_prod);
			stm.setString(2, uni_med);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delLogByProdMix - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Permite actualizar el estado del fo_producto a Eliminado
	 * 
	 * @param  cod_sap_prod String 
	 * @param  uni_med String 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean eliminaByProductMix(String cod_sap_prod, String uni_med) throws ProductosDAOException {
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en eliminaByProductMix");
			String sql = "UPDATE fo_productos SET pro_estado = 'E' WHERE pro_cod_sap = ? AND pro_tipre = ? ";
			logger.debug(sql);
			logger.debug("pro_cod_sap:"+cod_sap_prod);
			logger.debug("pro_tipre:"+uni_med);
			
			stm = conn.prepareStatement(sql);
			
			stm.setString(1, cod_sap_prod);
			stm.setString(2, uni_med);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaByProductMix - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica si el producto existe.
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si el producto existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean isProductById(long cod_prod) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en isProductById");
			String sql = "SELECT pro_id FROM fo_productos WHERE pro_id = ? ";
			logger.debug(sql);
			logger.debug("prod_id="+cod_prod);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, cod_prod);
			rs = stm.executeQuery();
			if(rs.next())
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isProductById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica si la marca existe.
	 * 
	 * @param  id_marca long 
	 * @return boolean, devuelve <i>true</i> si la marca existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean isMarcaById(long id_marca) throws ProductosDAOException{
		
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en isMarcaById");
			String sql = "SELECT mar_id FROM fo_marcas WHERE mar_id = ? ";
			logger.debug(sql);
			logger.debug("id_marca:"+id_marca);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, id_marca);
			rs = stm.executeQuery();
			if(rs.next())
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isMarcaById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica si la unidad de medida existe.
	 * 
	 * @param  id_ume long 
	 * @return boolean, devuelve <i>true</i> si la unidad de medida existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean isUni_MedById(long id_ume) throws ProductosDAOException{
		
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en isUni_MedById");
			String sql = "SELECT uni_id FROM fo_unidades_medida WHERE uni_id = ? ";
			logger.debug(sql);
			logger.debug("id_ume:"+id_ume);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, id_ume);
			rs = stm.executeQuery();
			if(rs.next())
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isUni_MedById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Verifica la relación entre el producto y el producto sugerido
	 * 
	 * @param  id_producto long 
	 * @param  id_producto_sug long 
	 * @return boolean, devuelve <i>true</i> si la relación existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean isSugAsocProd(long id_producto, long id_producto_sug) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {

			conn = this.getConnection();
			logger.debug("en isSugAsocProd");
			String sql = "SELECT sug_id  FROM fo_pro_sugerencias WHERE sug_sug = ? AND sug_base = ? ";
			logger.debug(sql);
			logger.debug("id_producto:"+id_producto);
			logger.debug("id_producto_sug:"+id_producto_sug);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, id_producto_sug);
			stm.setLong(2, id_producto);
			rs = stm.executeQuery();
			if(rs.next())
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : isSugAsocProd - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Obtiene información del producto web, segun el código del producto SAP
	 * 
	 * @param  id_prodSap long 
	 * @return ProductoEntity
	 * @throws ProductosDAOException
	 * */
	public ProductoEntity getProductoByProdSapId(long id_prodSap) throws ProductosDAOException {
		
		ProductoEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductoByProdSapId:");
		
		try {

			
			String sql = " SELECT pro_id, pro_id_padre, pro_tipre, pro_cod_sap, pro_uni_id, pro_mar_id, pro_estado, " +
				" pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, " +
				" pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, " +
				" pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, um.uni_desc uni_desc  " +
				" FROM fo_productos " +
				" LEFT JOIN fo_unidades_medida um ON pro_uni_id=um.uni_id   " + 
				" WHERE pro_id_bo = ? " ;
			logger.debug(sql+", id:"+id_prodSap);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, id_prodSap);
			rs = stm.executeQuery();
			int i=0;
			if (rs.next()) {
				prod = new ProductoEntity();
				prod.setId(new Long(rs.getString("pro_id")));
				if(rs.getString("pro_id_padre")!=null)
					prod.setId_padre(new Long(rs.getString("pro_id_padre")));
				else
					prod.setId_padre(new Long(0));
				prod.setTipre(rs.getString("pro_tipre"));
				prod.setCod_sap(rs.getString("pro_cod_sap"));
				if(rs.getString("pro_uni_id")!=null)
					prod.setUni_id(new Long(rs.getString("pro_uni_id")));
				else
					prod.setUni_id(new Long(0));
				if(rs.getString("pro_mar_id")!=null)
					prod.setMar_id(new Long(rs.getString("pro_mar_id")));
				else
					prod.setMar_id(new Long(0));
				prod.setEstado(rs.getString("pro_estado"));
				prod.setTipo(rs.getString("pro_tipo_producto"));
				prod.setDesc_corta(rs.getString("pro_des_corta"));
				prod.setDesc_larga(rs.getString("pro_des_larga"));
				prod.setImg_mini_ficha(rs.getString("pro_imagen_minificha"));
				prod.setImg_ficha(rs.getString("pro_imagen_ficha"));
				if(rs.getString("pro_unidad_medida")!=null)
					prod.setUnidad_medidad(new Double(rs.getString("pro_unidad_medida")));
				else
					prod.setUnidad_medidad(new Double(0));
				prod.setValor_difer(rs.getString("pro_valor_difer"));
				if(rs.getString("pro_ranking_ventas")!=null)
					prod.setRank_ventas(new Integer(rs.getString("pro_ranking_ventas")));
				else
					prod.setRank_ventas(new Integer(0));
				prod.setFec_crea(rs.getTimestamp("pro_fcrea"));
				prod.setFec_mod(rs.getTimestamp("pro_fmod"));
				if(rs.getString("pro_user_mod")!=null)
					prod.setUser_mod(new Integer(rs.getString("pro_user_mod")));
				else
					prod.setUser_mod(new Integer(0));
				prod.setGenerico(rs.getString("pro_generico"));
				if(rs.getString("pro_inter_valor")!=null)
					prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
				else
					prod.setInter_valor(new Double("0"));
				if(rs.getString("pro_inter_max")!=null)
					prod.setInter_max(new Double(rs.getString("pro_inter_max")));
				else
					prod.setInter_max(new Double("0"));
				if(rs.getString("pro_preparable")!=null)
					prod.setEs_prep(rs.getString("pro_preparable"));
				else
					prod.setEs_prep("");
				if(rs.getString("pro_nota")!=null)
					prod.setAdm_coment(rs.getString("pro_nota"));
				else
					prod.setAdm_coment("");
				prod.setNom_marca("");
				prod.setUni_med_desc(rs.getString("uni_desc"));
				i++;
			}

			if(i==0)
				throw new ProductosDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductoByProdSapId - Problema SQL (close)", e);
			}
		}
		logger.debug("prod:"+prod.getDesc_corta());
		return prod;
	}

	/**
	 * Actualiza el estado del producto Web
	 * 
	 * @param  prod ProductoEntity 
	 * @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean actEstProductoById(ProductoEntity prod) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			logger.debug("en actEstProductoById");
			conn = this.getConnection();
			String sql = "UPDATE fo_productos " +
				" SET pro_estado = ? " +
				" WHERE pro_id = ? ";
			logger.debug(sql);
			logger.debug("pro_id:"+prod.getId().longValue());
			
			stm = conn.prepareStatement(sql);
			stm.setString(1, prod.getEstado());
			stm.setLong(2, prod.getId().longValue());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actEstProductoById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Actualiza información del producto Web.
	 * 
	 * @param  prod ProductoEntity 
	 * @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean actProductoByProdMixId(ProductoEntity prod) throws ProductosDAOException {
		
		
		PreparedStatement stm = null;
		boolean result = false;
		try {

			logger.debug("en actProductoByProdMixId");
			conn = this.getConnection();
			String sql = "UPDATE fo_productos " +
				" SET pro_cod_sap = ? , pro_tipre =? , pro_des_corta = ? " +
				", pro_des_larga = ? , pro_user_mod = ? " +
				" WHERE pro_id = ? ";
			logger.debug(sql);
			logger.debug("pro_id:"+prod.getId().longValue());
			logger.debug("pro_cod_sap:"+prod.getCod_sap());
			logger.debug("pro_tipre:"+prod.getTipre());
			logger.debug("pro_tipre:"+prod.getDesc_corta());
			logger.debug("pro_tipre:"+prod.getDesc_larga());
			logger.debug("pro_tipre:"+prod.getUser_mod().intValue());
			
			
			stm = conn.prepareStatement(sql);
			stm.setString(1,prod.getCod_sap());
			stm.setString(2,prod.getTipre());
			stm.setString(3,prod.getDesc_corta());
			stm.setString(4,prod.getDesc_larga());
			stm.setInt(5,prod.getUser_mod().intValue());
			stm.setLong(6, prod.getId().longValue());
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actProductoByProdMixId - Problema SQL (close)", e);
			}
		}
		return result;
		
	}

	/**
	 * Elimina la relación entre el producto y el producto sugerido
	 * 
	 * @param  id_producto long 
	 * @param  id_sug long 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean eliminaSugerProdBidirec(long id_producto, long id_sug) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {

			conn = this.getConnection();
			logger.debug("en eliminaSugerProdBidirec");
			String sql = "DELETE FROM  fo_pro_sugerencias " +
			" WHERE sug_sug = ? AND sug_base = ? ";
			logger.debug(sql);
			logger.debug("vals:"+id_producto+","+id_sug);
			stm = conn.prepareStatement(sql);
			stm.setLong(2, id_sug);
			stm.setLong(1, id_producto);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminaSugerProdBidirec - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Obtiene el listado de los motivos de despublicacion
	 * 
	 * @return List MotivosDespEntity 
	 * @throws ProductosDAOException
	 * */
	public List getMotivoDesp() throws ProductosDAOException {
		
		List lstMtv = new ArrayList();
		MotivosDespEntity mtv = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			logger.debug("en getMotivoDesp");
			conn = this.getConnection();
			String sql = " SELECT id_desp, motivo, estado " +
				" FROM FO_PROD_DESP" +
				" WHERE ESTADO = '"+Constantes.ESTADO_PUBLICADO+"'";
			logger.debug("sql:"+sql);
			stm = conn.prepareStatement(sql  + " WITH UR"  );
			rs = stm.executeQuery();
			while (rs.next()) {
				mtv = new MotivosDespEntity();
				mtv.setId_desp(rs.getLong("id_desp"));
				mtv.setMotivo(rs.getString("motivo"));
				mtv.setEstado(rs.getString("estado"));
				lstMtv.add(mtv);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getMotivoDesp - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lstMtv.size());
		return lstMtv;
	}

	
	public double getProductosPrecios(long id_prod, long id_loc) throws ProductosDAOException {
		double prod = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductosPrecios:");
		
		try {

			String sql = " SELECT pre_valor " +
					     " FROM fo_precios_locales " +
					     " WHERE pre_estado = 'A' AND pre_pro_id = ? AND pre_loc_id = ? ";
				
			logger.debug("SQL: "+ sql+", id:"+id_prod + ", id_loc" +id_loc);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1, id_prod);
			stm.setLong(2, id_loc);
			rs = stm.executeQuery();
			if (rs.next()) {
				prod = rs.getDouble("pre_valor");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosPrecios - Problema SQL (close)", e);
			}
		}
		return prod;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.contenidos.dao.ProductosDAO#listProdCbarraByCriteria(cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO)
	 */	
	public List getProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException {
		
		List lista_productos = new ArrayList();
		ProductosCbarraDTO prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en listProdCbarraByCriteria:");
		
		//variable del criterio de búsqueda
		//cod de producto
		String cod_prod= criteria.getCodprod();
		String sqlCodProd = "";
		if (!cod_prod.equals(""))
			sqlCodProd = " AND p.id_producto ="+cod_prod;
		//cod sap de producto
		String cod_sap_prod = criteria.getProdsap();
		String sqlCodSapProd = "";
		if (!cod_sap_prod.equals(""))
			sqlCodSapProd = " AND p.cod_prod1 LIKE '"+cod_sap_prod+"%'";
		//segun estado
		//char estado = criteria.getEstado();
		String sqlEstado = "";
		/*if (estado!='0')
			sqlEstado = " AND p.pro_estado = \'"+estado+"\'";
		*///segun tipo
		//char tipo = criteria.getTipo();
		String sqlTipo = "";
		/*if (tipo!='0')
			sqlTipo = " AND p.pro_generico = '"+tipo+"'";
		*///segun descrip
		String descrip = criteria.getDescrip();
		String sqlDesc = "";
		if (!descrip.equals("")){
			List patron = new ArrayList();
			patron.add(Formatos.frmSinEspacios(descrip.toUpperCase()));
			sqlDesc = " AND (" + Formatos.frmPatronBO(patron) + " ) ";
			/*sqlDesc = " AND (upper(p.pro_des_corta) LIKE '"+descrip.toUpperCase()+"%' OR " +
					"upper(p.pro_des_larga) LIKE '"+descrip.toUpperCase()+"%' OR " +
					"upper(p.pro_tipo_producto) LIKE '"+descrip.toUpperCase()+"%')";*/
		}
		
		//id categoria seleccionada
	
		//variables de paginacion
		int pag = criteria.getPag();
		int regXpag = criteria.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		try {			
			String sql = " SELECT * FROM ( " +
					" SELECT row_number() over(order by p.id_producto) as row," +
					" p.id_producto pro_id, p.des_corta des_corta, " +
					" p.des_larga des_larga,  p.estado estado, uni_med uni_med, " +
					" p.cod_prod1 codsap,  coalesce(cb.cod_barra,'') cod_barra " +
					" FROM bo_productos p " +
					" LEFT JOIN bo_codbarra cb ON p.id_producto = cb.id_producto " +
					" WHERE p.estadoactivo ='1' and p.estado=1 " +
					sqlCodProd + sqlCodSapProd + sqlEstado + sqlTipo + sqlDesc +
				    " ) AS TEMP " +
				    " WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				prod = new ProductosCbarraDTO();
				prod.setPro_id(rs.getLong("pro_id"));
				prod.setPro_desc_corta(rs.getString("des_corta"));
				prod.setPro_desc_larga(rs.getString("des_larga"));
				prod.setPro_estado(rs.getString("estado"));
				prod.setPro_unimed(rs.getString("uni_med"));
				prod.setPro_cod_sap(rs.getString("codsap"));
				prod.setPro_cod_barra(rs.getString("cod_barra"));
				lista_productos.add(prod);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProdCbarraByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_productos.size());
		return lista_productos;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.contenidos.dao.ProductosDAO#getCountProdCbarraByCriteria(cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO)
	 */
	public int getCountProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException {
		int cantidad = 0;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		logger.debug("en getCountProdCbarraByCriteria:");
		
		//variable del criterio de búsqueda
		//cod de producto
		String cod_prod= criteria.getCodprod();
		String sqlCodProd = "";
		if (!cod_prod.equals(""))
			sqlCodProd = " AND p.id_producto ="+cod_prod;
		//cod sap de producto
		String cod_sap_prod = criteria.getProdsap();
		String sqlCodSapProd = "";
		if (!cod_sap_prod.equals(""))
			sqlCodSapProd = " AND p.cod_prod1 LIKE '"+cod_sap_prod+"%'";
		//segun estado
		//char estado = criteria.getEstado();
		String sqlEstado = "";
		/*if (estado!='0')
		 sqlEstado = " AND pro_estado = \'"+estado+"\'";
		 *///segun tipo
		//char tipo = criteria.getTipo();
		String sqlTipo = "";
		/*if (tipo!='0')
		 sqlTipo = " AND pro_generico = '"+tipo+"'";
		 *///segun descrip
		String descrip = criteria.getDescrip();
		String sqlDesc = "";
		if (!descrip.equals("")){
			List patron = new ArrayList();
			patron.add(Formatos.frmSinEspacios(descrip.toUpperCase()));
			sqlDesc = " AND " + Formatos.frmPatronBO(patron);
			/*sqlDesc = " AND (upper(p.pro_des_corta) LIKE '"+descrip.toUpperCase()+"%' OR " +
			 "upper(p.pro_des_larga) LIKE '"+descrip.toUpperCase()+"%' OR " +
			 "upper(p.pro_tipo_producto) LIKE '"+descrip.toUpperCase()+"%')";*/
		}
		
		try {
			
			String sql = " SELECT count(p.id_producto) as cantidad " +
						" FROM bo_productos p " +
						" LEFT JOIN bo_codbarra cb ON p.id_producto = cb.id_producto " +
						" WHERE p.estadoactivo ='1' and p.estado=1 " +
			sqlCodProd + sqlCodSapProd + sqlEstado + sqlTipo + sqlDesc;
			
			logger.debug("Esta es la query "+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountProdCbarraByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+cantidad);
		return cantidad;
	}
	
	/*
	 * (sin Javadoc)
	 * @see cl.bbr.jumbocl.contenidos.dao.ProductosDAO#getTiposProductosPorCod(java.lang.String)
	 */
	public List getTiposProductosPorCod(String codDesc) throws ProductosDAOException {
		
		List list_tipoProductos = new ArrayList();
		CuponEntity cupon = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			String sql = " SELECT PRO_ID_BO, PRO_TIPO_PRODUCTO, PRO_DES_CORTA FROM FODBA.FO_PRODUCTOS WHERE PRO_COD_SAP = ? WITH UR" ;
			stm = conn.prepareStatement(sql);
			stm.setString(1, codDesc);
			rs = stm.executeQuery();

			while (rs.next()) {
				cupon = new CuponEntity();
				cupon.setDato_tipo_cod(rs.getString("PRO_ID_BO"));
				cupon.setDato_tipo_desc(rs.getString("PRO_TIPO_PRODUCTO")+" "+rs.getString("PRO_DES_CORTA"));
				list_tipoProductos.add(cupon);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTiposProductosPorCod - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_tipoProductos.size());
		
		return list_tipoProductos;
	}

	/*
	 * (sin Javadoc)
	 * @see cl.bbr.jumbocl.contenidos.dao.ProductosDAO#getTiposProductosPorDesc(java.lang.String)
	 */
	public List getTiposProductosPorDesc(String codDesc) throws ProductosDAOException {
		
		List list_tipoProductos = new ArrayList();
		CuponEntity cupon = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			conn = this.getConnection();
			String sql = "SELECT PRO_ID_BO, PRO_TIPO_PRODUCTO, MAR_NOMBRE, PRO_DES_CORTA FROM FODBA.FO_PRODUCTOS " +
					"INNER JOIN FODBA.FO_MARCAS ON FODBA.FO_MARCAS.MAR_ID = FODBA.FO_PRODUCTOS.PRO_MAR_ID " +
					"WHERE lower(PRO_TIPO_PRODUCTO) LIKE '%"+codDesc+"' OR lower(PRO_DES_CORTA) LIKE '%"+codDesc+"' or " +
					"lower(PRO_DES_LARGA) LIKE '%"+codDesc+"' or lower(MAR_NOMBRE) LIKE '%"+codDesc+"'";
			stm = conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			
			while (rs.next()) {
				cupon = new CuponEntity();
				cupon.setDato_tipo_cod(rs.getString("PRO_ID_BO"));
				cupon.setDato_tipo_desc(rs.getString("PRO_TIPO_PRODUCTO")+" "+rs.getString("MAR_NOMBRE")+" "+rs.getString("PRO_DES_CORTA"));
				list_tipoProductos.add(cupon);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTiposProductosPorDesc - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+list_tipoProductos.size());
		
		return list_tipoProductos;
	}
	
	/**
	 * Obtiene información de la ficha del producto web
	 * @param codProd
	 * @return
	 * @throws ProductosDAOException
	 */
	public List getFichaProductoPorId(long codProd) throws ProductosDAOException {
		
		List lstDatosFicha = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		FichaProductoEntity datosFichaEntity = new FichaProductoEntity();

		logger.debug("getFichaProductoPorId");
		String sql =
			"SELECT PFT_PRO_ID, PFT_PFI_ITEM, PFT_PFI_SECUENCIA, PFT_DESCRIPCION_ITEM, PFT_ESTADO_ITEM " +
			"FROM FODBA.FO_PRODUCTOS_FICHA_TEC " +
			"WHERE PFT_PRO_ID = " + codProd + " ORDER BY PFT_PFI_SECUENCIA";
		
		logger.debug("SQL: " + sql);
				
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				datosFichaEntity = new FichaProductoEntity();
				datosFichaEntity.setPftProId(rs.getLong("PFT_PRO_ID"));
				datosFichaEntity.setPftPfiItem(rs.getLong("PFT_PFI_ITEM"));
				datosFichaEntity.setPftPfiSecuencia(rs.getInt("PFT_PFI_SECUENCIA"));
				datosFichaEntity.setPftDescripcionItem(rs.getString("PFT_DESCRIPCION_ITEM"));
				lstDatosFicha.add(datosFichaEntity);
			}
			
		} catch (Exception e) {
			logger.debug("Problema getFichaProductoPorId :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getFichaProductoPorId - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+lstDatosFicha.size());
		return lstDatosFicha;
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws ProductosDAOException
	 */
	public List getItemFichaProductoAll() throws ProductosDAOException {

		List lstItems = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		ItemFichaProductoDTO itemFichaDTO = new ItemFichaProductoDTO();

		logger.debug("getItemFichaProductoAll");
		String sql = "SELECT PFI_ITEM, PFI_DESCRIPCION " +
					 "FROM FODBA.FO_PRODUCTOS_ITEM_FICHA_TEC ORDER ORDER BY PFI_ITEM";
		
		logger.debug("SQL: " + sql);
				
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				itemFichaDTO = new ItemFichaProductoDTO();
				itemFichaDTO.setPfiItem(rs.getLong("PFI_ITEM"));
				itemFichaDTO.setPfiDescripcion(rs.getString("PFI_DESCRIPCION"));
				lstItems.add(itemFichaDTO);
			}
			
		} catch (Exception e) {
			logger.debug("Problema getItemFichaProductoAll :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getItemFichaProductoAll - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+lstItems.size());
		return lstItems;
	}
	
	/**
	 * Obtiene el item de la ficha tecnica según id	
	 * @param idItem
	 * @return
	 * @throws ProductosDAOException
	 */
	public ItemFichaProductoDTO getItemFichaProductoById(long idItem) throws ProductosDAOException {

		ItemFichaProductoDTO item = new ItemFichaProductoDTO();
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		String sql = "SELECT PFI_ITEM, PFI_DESCRIPCION " +
					 " FROM FODBA.FO_PRODUCTOS_ITEM_FICHA_TEC " +
					 " WHERE PFI_ITEM = ? " ;
						
		logger.debug("getItemFichaProducto");
		logger.debug(sql+", id:"+idItem);
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,idItem);
			rs = stm.executeQuery();
			while (rs.next()) {
				item = new ItemFichaProductoDTO();
				item.setPfiItem(rs.getLong("PFI_ITEM"));
				item.setPfiDescripcion(rs.getString("PFI_DESCRIPCION"));
			}

		} catch (Exception e) {
			logger.debug("Problema getItemFichaProducto :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return item;
	}
	
	/**
	* Obtiene el máximo código que se le asigna a un item ficha producto
	* 
	* @return long, nuevo id del item ficha producto
	* @throws ProductosDAOException
	*/
	public long getCodItemFichaProducto(String valorItem) throws ProductosDAOException {

		logger.debug("En getCodItemFichaProducto(" + valorItem + ")");
		
		long id = 0;

		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			String sql = " INSERT INTO FODBA.FO_PRODUCTOS_ITEM_FICHA_TEC (PFI_DESCRIPCION) VALUES (?)" ;
			logger.debug(sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm.setString(1, valorItem);
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCodItemFichaProducto - Problema SQL (close)", e);
			}
		}
		logger.debug("En getCodItemFichaProducto id:"+id);
		return id;
	}

	/**
	 * Actualiza información ficha del producto segun el parámetro.
	 * @param param
	 * @param existeRegistro
	 * @return
	 * @throws ProductosDAOException
	 */
	public boolean actualizaFichaProductoById(ProcModFichaTecnicaDTO param) throws ProductosDAOException {

		PreparedStatement stm = null;
		boolean result = false;		
		try {
			conn = this.getConnection();
			String sql = "INSERT INTO FODBA.FO_PRODUCTOS_FICHA_TEC " +
			          	 "(PFT_PRO_ID, PFT_PFI_ITEM, PFT_PFI_SECUENCIA, PFT_DESCRIPCION_ITEM, PFT_ESTADO_ITEM) " +
			             "VALUES(?, ?, ?, ?, ?)";
			
			logger.debug("En actualizaFichaProductoById ( " + param.toString() +")");

			stm = conn.prepareStatement(sql);			
			stm.setLong(1, param.getPftProId());
			stm.setLong(2, param.getPftPfiItem());
			stm.setLong(3, param.getPftPfiSecuencia());
			stm.setString(4, param.getPftDescripcionItem());
			stm.setLong(5, param.getPftEstadoItem());

			int i = stm.executeUpdate();
			
			if(i>0) {				
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e);
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);
		}
		 catch (Exception ex) {
			logger.error(ex);			
			throw new ProductosDAOException(ex);
		}
		finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaProductosById - Problema SQL (close)", e);
			}
		}
		
		logger.debug("En actualizaFichaProductoById result " + result);
		
		return result;
	}
	
	/**
	 * Verifica si el producto tiene ficha
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si la ficha existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean tieneFichaProductoById(long cod_prod) throws ProductosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		long estado = 0;
		try {

			conn = this.getConnection();
			logger.debug("en tieneFichaProductoById");
			String sql = "SELECT EPF_ESTADO_FICHA_TEC FROM FODBA.FO_ESTADO_PRODUCTOS_FICHA_TEC WHERE EPF_PRO_ID = ? ";
			logger.debug(sql);
			logger.debug("pft_pro_id="+cod_prod);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, cod_prod);
			rs = stm.executeQuery();		
			if(rs.next()) {
				estado = rs.getLong("EPF_ESTADO_FICHA_TEC");
			}
			if (estado == 1) {
				result = true;
			}	
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : tieneFichaProductoById - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	public boolean existeRegistroFichaTecnica(long cod_prod) throws ProductosDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;		
		try {

			conn = this.getConnection();
			logger.debug("en existeRegistroFichaTecnica");
			String sql = "SELECT * FROM FODBA.FO_ESTADO_PRODUCTOS_FICHA_TEC WHERE EPF_PRO_ID = ? ";
			logger.debug(sql);
			logger.debug("pft_pro_id="+cod_prod);
			stm = conn.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, cod_prod);
			rs = stm.executeQuery();		
			if(rs.next()) {
				result = true;
			}
				
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : existeRegistroFichaTecnica - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Actualiza estado ficha tecnica del producto
	 * @param prodId
	 * @param estadoFichaTecnica
	 * @return
	 * @throws ProductosDAOException
	 */
	public boolean actualizaEstadoFichaTecnica(long prodId, long estadoFichaTecnica, boolean existeRegistro) throws ProductosDAOException {

		PreparedStatement stm = null;
		boolean result = false;
		try {
			logger.debug("actualizaEstadoFichaTecnica prodId " + prodId);
		    logger.debug("actualizaEstadoFichaTecnica estadoFichaTecnica " + estadoFichaTecnica);
			
			conn = this.getConnection();
			String sql = "";
			
			if (existeRegistro) {
				sql = "UPDATE FODBA.FO_ESTADO_PRODUCTOS_FICHA_TEC " + 
					  "SET EPF_ESTADO_FICHA_TEC = ? " +
			          "WHERE EPF_PRO_ID = ?";
			} else {
				sql = "INSERT INTO FODBA.FO_ESTADO_PRODUCTOS_FICHA_TEC " +
					  "(EPF_PRO_ID, EPF_ESTADO_FICHA_TEC) " +
					  "VALUES(?, ?)";
			}
			
			logger.debug("actualizaEstadoFichaTecnica sql : " + sql);

			stm = conn.prepareStatement(sql);
			
			if (existeRegistro) {
				stm.setLong(1, estadoFichaTecnica);
				stm.setLong(2, prodId);
			} else {
				stm.setLong(1, prodId);
				stm.setLong(2, estadoFichaTecnica);
			}
						
			int i = stm.executeUpdate();
			
			if(i>0) {				
				result = true;
			}

		} catch (SQLException e) {
			logger.error(e);
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);
		}
		 catch (Exception ex) {
			logger.error(ex);			
			throw new ProductosDAOException(ex);
		}
		finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizaEstadoFichaTecnica - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	 * Elimino los datos de la ficha tecnica según id producto
	 * @param prodId
	 * @return
	 * @throws ProductosDAOException
	 */
	public boolean eliminaFichaProductoById(long prodId) throws ProductosDAOException {
		boolean result = true;
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			logger.debug("en eliminaFichaProductoById");
			String sql = "DELETE FROM FODBA.FO_PRODUCTOS_FICHA_TEC " +
			" WHERE PFT_PRO_ID = ? ";
			logger.debug(sql);
			logger.debug("vals:"+prodId);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prodId);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			result = false;
			throw new ProductosDAOException(e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				result = false;
				logger.error("[Metodo] : eliminaFichaProductoById - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	* Inserta productos de la categoría para Grability 
	* 
	* @param  id_cat, flag 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.   
	* @throws ProductosDAOException
	*/
	public boolean updateMarcaGrabilityProducto(String id_cat, String flag) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			
			int largo = id_cat.length();
			
			logger.debug("Actualiza marca Grability");
			
			String sql ="  INSERT INTO fodba.FO_PROD_GRABILITY " +
						"         (PRO_ID,PRO_TIPRE,PRO_COD_SAP, " +
						"         ID_CATPROD_PADRE,PRO_PUBLICADO_GRABILITY) " +
						"  SELECT DISTINCT PRO.PRO_ID,PRO.PRO_TIPRE, " +
						"         PRO.PRO_COD_SAP,CPR.ID_CATPROD_PADRE, ? " +
						"    FROM fodba.FO_PRODUCTOS AS PRO, " +
						"         bodba.BO_PRODUCTOS AS BPR, " +
						"         fodba.FO_PRECIOS_LOCALES AS PRE, " +
						"         bodba.BO_CATPROD AS CPR " +
						"   WHERE PRE.PRE_PRO_ID = PRO.PRO_ID " +
						"     AND PRO.PRO_ESTADO = 'A' " +
						"     AND PRE.PRE_ESTADO = 'A' " +
						"     AND PRE.PRE_TIENESTOCK = 1 " +
						"     AND PRO.PRO_ID_BO = BPR.ID_PRODUCTO " +
						"     AND CPR.ID_CATPROD = BPR.ID_CATPROD " +
						"     AND PRO.PRO_ID NOT IN " +
						"         (SELECT DISTINCT GRB.PRO_ID  " +
						"         FROM fodba.FO_PROD_GRABILITY AS GRB) " +
						"     AND substr(CPR.ID_CATPROD_PADRE,1,"+largo+") = ? ";
			
			logger.debug(sql);
			
			stm = conn.prepareStatement(sql);
			stm.setString(1,flag);
			stm.setString(2,id_cat);
			
			int i = stm.executeUpdate();
			if (i > 0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updateMarcaGrabilityProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	/**
	* Elimina productos de la categoría para que no aparezcan en Grability
	* 
	* @param  id_cat, flag 
	* @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario, devuelve <i>false</i>.   
	* @throws ProductosDAOException
	*/
	public boolean deleteMarcaGrabilityProducto(String id_cat) throws ProductosDAOException {
		
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try {
			conn = this.getConnection();
			
			int largo = id_cat.length();
			
			logger.debug("Elimina marca Grability");
			
			String sql ="  DELETE fodba.FO_PROD_GRABILITY  " +
						"   WHERE PRO_ID IN (" +
						"         SELECT DISTINCT PRO.PRO_ID " +
						"           FROM fodba.FO_PRODUCTOS AS PRO, "+                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
						"                bodba.BO_PRODUCTOS AS BPR, " +
						"                bodba.BO_CATPROD AS CPR " +			   
						"          WHERE PRO.PRO_ID_BO = BPR.ID_PRODUCTO " + 
						"            AND CPR.ID_CATPROD = BPR.ID_CATPROD " +
						"     AND substr(CPR.ID_CATPROD_PADRE,1,"+largo+") = ? )";
			
			logger.debug(sql);
			
			stm = conn.prepareStatement(sql);
			stm.setString(1,id_cat);
			
			int i = stm.executeUpdate();
			if (i > 0)
				result = true;

		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : deleteMarcaGrabilityProducto - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	public boolean guardarLeySuperOcho(NutricionalLeySupeDTO dto) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stmSel = null;
		PreparedStatement stm = null;			
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = this.getConnection();
						
			sql = "SELECT COUNT(PRO_ID) CANTIDAD FROM FODBA.FO_LEYSUPER_OCHO WHERE PRO_ID = ? ";
			stmSel = conn.prepareStatement(sql  + " WITH UR");
			stmSel.setLong(1, dto.getIdProductoFO());
			rs = stmSel.executeQuery();	
			int cantidad=0;
			if(rs.next()) {
				cantidad = rs.getInt("cantidad");
			}
			
			if(cantidad==0){			
				 sql ="INSERT INTO FODBA.FO_LEYSUPER_OCHO" 
						+ " (PRO_ID, LSO_PUBLICAR_GRASAS_SATURADAS, LSO_PUBLICAR_SODIO, LSO_PUBLICAR_AZUCARES, LSO_PUBLICAR_CALORICAS)"
						+ " VALUES(?, ?, ?, ?, ?)";
				 stm = conn.prepareStatement(sql);
				 stm.setInt(1, dto.getIdProductoFO());
				 stm.setString(2 ,String.valueOf(dto.getExcesoGrasasSaturadas()));
				 stm.setString(3 ,String.valueOf(dto.getExcesoSodio()));
				 stm.setString(4 ,String.valueOf(dto.getExcesoAzuraces()));
				 stm.setString(5 ,String.valueOf(dto.getExcesoCalorias()));
			}else{
				 sql ="UPDATE FODBA.FO_LEYSUPER_OCHO" 
						 + " SET LSO_PUBLICAR_GRASAS_SATURADAS=?, LSO_PUBLICAR_SODIO=?, LSO_PUBLICAR_AZUCARES=?, LSO_PUBLICAR_CALORICAS=?"
						 + " WHERE PRO_ID = ?";
				 
				 stm = conn.prepareStatement(sql);				 
				 stm.setString(1 ,String.valueOf(dto.getExcesoGrasasSaturadas()));
				 stm.setString(2 ,String.valueOf(dto.getExcesoSodio()));
				 stm.setString(3 ,String.valueOf(dto.getExcesoAzuraces()));
				 stm.setString(4 ,String.valueOf(dto.getExcesoCalorias()));
				 stm.setInt(5, dto.getIdProductoFO());				
			}
												
			int i = stm.executeUpdate();
			if (i > 0)
				result = true;
			
			
		} catch (SQLException e) {
			logger.error("JdbcProductosDAO [guardarLeySuperOcho], SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stmSel != null)
					stmSel.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [guardarLeySuperOcho] - Problema SQL (close)", e);
			}
		}
		return result;
	}	
	public List listaLeySuperOcho(int idproductoFO) throws ProductosDAOException {
		List lista = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String sql = "SELECT PRO_ID, LSO_PUBLICAR_GRASAS_SATURADAS, LSO_PUBLICAR_SODIO, " 
					 + " LSO_PUBLICAR_AZUCARES, LSO_PUBLICAR_CALORICAS"
				     + " FROM FODBA.FO_LEYSUPER_OCHO WHERE PRO_ID = ? ";
		try {
			lista = new ArrayList();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, idproductoFO);
			rs = stm.executeQuery();
			while (rs.next()) {
				int id = (int) rs.getLong("PRO_ID");				

				char e1 = rs.getString("LSO_PUBLICAR_AZUCARES").toString().charAt(0);
				char e2 = rs.getString("LSO_PUBLICAR_CALORICAS").toString().charAt(0);
				char e3 = rs.getString("LSO_PUBLICAR_GRASAS_SATURADAS").toString().charAt(0);
				char e4 = rs.getString("LSO_PUBLICAR_SODIO").toString().charAt(0);
				
				NutricionalLeySupeDTO dto = new cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO.Builder(id, e1, e2, e3, e4 ).build();
				lista.add(dto);
			}
			
		} catch (Exception e) {			
			logger.error("JdbcProductosDAO [listaLeySuperOcho] - ", e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [listaLeySuperOcho] - Problema SQL (close)", e);
			}
		}		
		return lista;
	}
	
	public List listaFichaNutricional(int idproductoFO) throws ProductosDAOException {
		List lista = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String cabecera = "";
		
		String sqlCab = "SELECT CFN_CABECERA FROM FODBA.FO_CABECERA_FICHA_NUTRICIONAL WHERE PRO_ID = ? ";
				
		
		String sql = "SELECT PRO_ID, PFN_ITEM, PFN_DESCRIPCION, PFN_DESCRIPCION2, PFN_SECUENCIA, PFN_PUBLICAR" 					 
				     + " FROM FODBA.FO_PRODUCTOS_FICHA_NUTRICIONAL WHERE PRO_ID = ? "
				     + " ORDER BY PFN_SECUENCIA";
		try {
			lista = new ArrayList();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sqlCab + " WITH UR");
			stm.setLong(1, idproductoFO);
			rs = stm.executeQuery();
			while (rs.next()) {
				cabecera =  rs.getString("CFN_CABECERA").toString().trim();	
			}
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, idproductoFO);
			rs = stm.executeQuery();
			while (rs.next()) {
				//int id = (int) rs.getLong("PRO_ID");				

				String item = rs.getString("PFN_ITEM").toString().trim();
				String desc = rs.getString("PFN_DESCRIPCION").toString().trim();
				String desc2 = rs.getString("PFN_DESCRIPCION2").toString().trim();
				//int e3 = rs.getInt("PFN_SECUENCIA");
				
				FichaNutricionalDTO dto = new FichaNutricionalDTO.Builder(idproductoFO, item)
												.cabecera(cabecera)
												.descripcion(desc)
												.descripcion2(desc2)
												.build();
				lista.add(dto);
			}
			
		} catch (Exception e) {
			logger.error("JdbcProductosDAO [listaFichaNutricional] - ", e);			
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [listaFichaNutricional] - Problema SQL (close)", e);				
			}
		}		
		return lista;
	}
	
	public boolean guardarFichaNutricional(Map mapa, int idProductoFO) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stmDelete = null;
		PreparedStatement stmInsert = null;
		String sqlDelete = null;
		String sqlInsert = null;
		
		int[] results = null;
		int conta = 1;		
		int totalItemInsertado = 0;
		
		try {
			conn = this.getConnection();

			sqlDelete = "DELETE FROM FODBA.FO_PRODUCTOS_FICHA_NUTRICIONAL WHERE PRO_ID = ? ";
			stmDelete = conn.prepareStatement(sqlDelete);
			stmDelete.setLong(1, idProductoFO);
			stmDelete.executeUpdate();	
			
			sqlInsert ="INSERT INTO FODBA.FO_PRODUCTOS_FICHA_NUTRICIONAL " 
					+ " (PRO_ID, PFN_ITEM, PFN_DESCRIPCION, PFN_DESCRIPCION2, PFN_SECUENCIA, PFN_PUBLICAR)"
					+ " VALUES(?, ?, ?, ?, ?, ?)";
			stmInsert = conn.prepareStatement(sqlInsert);
			
			if(mapa.size()>0){
				Iterator ite = mapa.entrySet().iterator();
				while (ite.hasNext()){
					Map.Entry entry = (Map.Entry) ite.next();
					
					FichaNutricionalDTO dto = (FichaNutricionalDTO)entry.getValue();	
					
					stmInsert.setInt(1, idProductoFO);
					stmInsert.setString(2 ,dto.getItem());
					stmInsert.setString(3 ,dto.getDescripcion());
					stmInsert.setString(4 ,dto.getDescripcion2());
					stmInsert.setInt(5 ,  dto.getOrder());
					stmInsert.setString(6, "D"); // VER SI ESTE CAMPPO ES nesecsario
					stmInsert.addBatch();
					
					if (conta % 20 == 0) {	
						results = stmInsert.executeBatch();	
						totalItemInsertado = totalItemInsertado + results.length;
						stmInsert.clearBatch();
					}
					conta++;				
				}
				results = stmInsert.executeBatch();	
				totalItemInsertado = totalItemInsertado + results.length;
				logger.debug(" Registros: insertados["+totalItemInsertado+"]");
			}			
			result = true;
		} catch (SQLException e) {
			logger.error("JdbcProductosDAO [guardarFichaNutricional], SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (stmDelete != null)
					stmDelete.close();
				if (stmInsert != null)
					stmInsert.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [guardarFichaNutricional] - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	public boolean guardarCabeceraFichaNutricional(String cabecera, long idProductoFO) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		PreparedStatement stmSel = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = this.getConnection();
			
			String sqlSel = "SELECT COUNT(PRO_ID) CANTIDAD FROM FODBA.FO_CABECERA_FICHA_NUTRICIONAL WHERE PRO_ID = ? ";
			stmSel = conn.prepareStatement(sqlSel  + " WITH UR");
			stmSel.setLong(1, idProductoFO);
			rs = stmSel.executeQuery();	
			int cantidad = 0;
			if(rs.next()) {
				cantidad = rs.getInt("CANTIDAD");
			}
			
			if(cantidad==0){	
				sql ="INSERT INTO FODBA.FO_CABECERA_FICHA_NUTRICIONAL(PRO_ID, CFN_CABECERA) VALUES (?, ?)";		
				stm = conn.prepareStatement(sql);
				stm.setLong(1, idProductoFO);
				stm.setString(2,cabecera);
				
			}else{
				sql ="UPDATE FODBA.FO_CABECERA_FICHA_NUTRICIONAL SET CFN_CABECERA=? WHERE PRO_ID = ?";
				stm = conn.prepareStatement(sql);
				stm.setString(1, cabecera);
				stm.setLong(2, idProductoFO);
			}
			int i = stm.executeUpdate();
			if (i > 0)
				result = true;
			
		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);		
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [guardarTipoFicha] - Problema SQL (close)", e);
			}
		}
		return result;
	}
		
	public boolean guardarTipoFicha(List listTipoFicha, long idProductoFO) throws ProductosDAOException {
		boolean result = false;
		PreparedStatement stmDelete = null;
		PreparedStatement stmInsert = null;
		String sqlDelete = null;
		String sqlInsert = null;
		try {
			conn = this.getConnection();
			
			sqlDelete = "DELETE FROM FODBA.FO_ESTADOS_FICHA_PRODUCTO WHERE PRO_ID = ? ";
			stmDelete = conn.prepareStatement(sqlDelete);
			stmDelete.setLong(1, idProductoFO);
			stmDelete.executeUpdate();	
			
			sqlInsert ="INSERT INTO FODBA.FO_ESTADOS_FICHA_PRODUCTO(PRO_ID, EFP_PUBLICAR) VALUES (?, ?)";
			stmInsert = conn.prepareStatement(sqlInsert);
			
			if(listTipoFicha.size()>0){
				Iterator ite = listTipoFicha.iterator();
				while (ite.hasNext()){
					String tipoFicha = (String) ite.next();

					stmInsert.setLong(1, idProductoFO);
					stmInsert.setString(2 ,tipoFicha);

					int i = stmInsert.executeUpdate();
					if (i > 0)
						result = true;
				}
			}	
		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){ 
				throw new ProductosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (stmDelete != null)
					stmDelete.close();
				if (stmInsert != null)
					stmInsert.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [guardarTipoFicha] - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	public List obtenerTipoFicha(long idproductoFO) throws ProductosDAOException {
		List listTipoFicha = new ArrayList();
		String estado = "";
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sqlSel = "SELECT EFP_PUBLICAR FROM FODBA.FO_ESTADOS_FICHA_PRODUCTO WHERE PRO_ID = ? ";
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sqlSel + " WITH UR");
			stm.setLong(1, idproductoFO);
			rs = stm.executeQuery();
			while(rs.next()){
				estado = (String) rs.getString("EFP_PUBLICAR");
				listTipoFicha.add(estado);
			}
		} catch (Exception e) {
			logger.error("JdbcProductosDAO [obtenerTipoFicha] - ", e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("JdbcProductosDAO [obtenerTipoFicha] - Problema SQL (close)", e);
			}
		}		
		return listTipoFicha;
	}
}