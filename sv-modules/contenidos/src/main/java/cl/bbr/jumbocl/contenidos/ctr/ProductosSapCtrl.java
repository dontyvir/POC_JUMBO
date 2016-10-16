package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CodBarraSapEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModMPVProductDTO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosSapDAO;
import cl.bbr.jumbocl.contenidos.dao.JdbcCategoriasSapDAO;
import cl.bbr.jumbocl.contenidos.dto.CodigosBarraSapDTO;
import cl.bbr.jumbocl.contenidos.dto.PreciosSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosSapException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Controlador de procesos sobre los productos SAP.
 * @author BBR
 *
 */
public class ProductosSapCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene la lista de productos sap, segun los criterios de busqueda: cod. sap producto, cod. sap categorias,  
	 * y categoria seleccionada
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @return List ProductosSapDTO
	 * @throws ProductosSapException
	 * */
	public List getProductosSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapException{
		List result = new ArrayList();
		try{
			logger.debug("en getProductosSapByCriteria");
			JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
			
			List listaProd= new ArrayList();
			listaProd = (List) productosSapDAO.getProductosSapByCriteria(criterio);
			ProductoSapEntity prod = null;
			for (int i = 0; i < listaProd.size(); i++) {
				prod = null;
				prod = (ProductoSapEntity) listaProd.get(i);
				String fecha = "";
				if(prod.getFecCarga()!=null)
					fecha = prod.getFecCarga().toString();
				ProductosSapDTO proddto = new ProductosSapDTO();
				proddto.setId(prod.getId().longValue());
				proddto.setCod_prod_1(prod.getCod_prod_1());
				proddto.setUni_med(prod.getUni_med());
				proddto.setDes_corta(prod.getDes_corta());
				proddto.setNom_cat_sap(prod.getNom_cat_sap());
				proddto.setFec_carga(fecha);
				proddto.setFlag_mix(prod.getMixWeb());
				proddto.setEstado(prod.getEstado());
				proddto.setEstActivo(prod.getEstActivo());
				proddto.setCon_precio(prod.getCon_precio());

				result.add(proddto);
			}
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getProductosByCriteria:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la cantidad de productos sap, segun los criterios de busqueda: cod. sap producto, cod. sap categorias,  
	 * y categoria seleccionada
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @return int
	 * @throws ProductosSapException
	 * */
	public int getCountProdSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapException{
		int result = 0;
		try{
			logger.debug("en getCountProdSapByCriteria");
			JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
			
			result =  productosSapDAO.getCountProdSapByCriteria(criterio);
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getCountProdSapByCriteria:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
	}
	

	/**
	 * Se obtiene la lista de productos sap, segun los criterios de busqueda: 
	 * Rango de fechas de inicio y fin, respecto a la fecha de carga
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @param  fecha_ini String 
	 * @param  fecha_fin String 
	 * @return List ProductosSapDTO
	 * @throws ProductosSapException
	 * */
	public List getProductosSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapException{
		
		List result = new ArrayList();
		try{
			logger.debug("en getProductosSapByCriteriaByDateRange");
			JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
			
			List listaProd= new ArrayList();
			listaProd = (List) productosSapDAO.getProductosSapByCriteriaByDateRange(criterio, fecha_ini, fecha_fin);
			ProductoSapEntity prod = null;
			for (int i = 0; i < listaProd.size(); i++) {
				prod = null;
				prod = (ProductoSapEntity) listaProd.get(i);
				String fecha = "";
				if(prod.getFecCarga()!=null)
					fecha = prod.getFecCarga().toString();
				/*ProductosSapDTO proddto = new ProductosSapDTO(prod.getId().longValue(), prod.getCod_prod_1(), prod.getDes_corta(), 
						prod.getNom_cat_sap(), fecha, prod.getMixWeb(), prod.getEstado(), prod.getEstActivo());
						*/
				ProductosSapDTO proddto = new ProductosSapDTO();
				proddto.setId(prod.getId().longValue());
				proddto.setCod_prod_1(prod.getCod_prod_1());
				proddto.setUni_med(prod.getUni_med());
				proddto.setDes_corta(prod.getDes_corta());
				proddto.setNom_cat_sap(prod.getNom_cat_sap());
				proddto.setFec_carga(fecha);
				proddto.setFlag_mix(prod.getMixWeb());
				proddto.setEstado(prod.getEstado());
				proddto.setEstActivo(prod.getEstActivo());
				proddto.setCon_precio(prod.getCon_precio());

				result.add(proddto);
			}
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getProductosSapByCriteriaByDateRange:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
	}

	/**
	 * Se obtiene la cantidad de productos sap, segun los criterios de busqueda: 
	 * Rango de fechas de inicio y fin, respecto a la fecha de carga
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @param  fecha_ini String 
	 * @param  fecha_fin String 
	 * @return int
	 * @throws ProductosSapException
	 * */
	public int getCountProdSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapException{
		
		int result = 0;
		try{
			logger.debug("en getCountProdSapByCriteriaByDateRange");
			JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
			
			result = productosSapDAO.getCountProdSapByCriteriaByDateRange(criterio, fecha_ini, fecha_fin);
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getCountProdSapByCriteriaByDateRange:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
	}
	
	/**
	 * Proceso de agregar un producto al Mix y sacar un producto del Mix.<br>
	 * Nota: Este método tiene transaccionalidad.
	 * 
	 * @param  param ProcModMPVProductDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó o se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosSapException
	 * @throws SystemException
	 * */
	public boolean setModMPVProduct(ProcModMPVProductDTO param) throws ProductosSapException, SystemException{
		boolean res = false;
		String imagen_string="";
		
		JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
		
		
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		// Marcamos los dao's con la transacción
		try {
			productosSapDAO.setTrx(trx1);
		} catch (ProductosSapDAOException e2) {
			logger.error("Error al asignar transacción al dao ProductoSap");
			throw new SystemException("Error al asignar transacción al dao ProductoSap");
		}

		
		try{
			logger.debug("en setModMPVProduct");
			
			ProductoSapEntity prod = productosSapDAO.getProductSapById(param.getId_prod());
			if(param.getAction().equals("Agregar")){
				//verificar si el estado es 1, cc. lanzar excepcion
				if(prod.getEstado().equals("0")){
					trx1.rollback();
					throw new ProductosSapException(Constantes._EX_PSAP_DESACTIVADO);
				}
				//verificar si el estado activo es 1, cc. lanzar excepcion
				if(prod.getEstActivo().equals("0")){
					trx1.rollback();
					throw new ProductosSapException(Constantes._EX_PSAP_DESACTIVADO);
				}
				
				/* COMENTAR para cargas masivas BEGIN */
				//verifica si tiene precio y cod_barras, c.c. lanzar excepcion
				int cant_precios =  productosSapDAO.getPreciosByProdId(prod.getId().longValue()).size();
				logger.debug("precios?"+cant_precios);
				if(cant_precios==0){
					trx1.rollback();
					throw new ProductosSapException(Constantes._EX_OPE_PRECIO_NO_EXISTE);
				}
				int cant_codbarra = productosSapDAO.getCodBarrasByProdId(prod.getId().longValue()).size();
				logger.debug("codbarra?"+cant_codbarra);
				if(cant_codbarra==0){
					trx1.rollback();
					throw new ProductosSapException(Constantes._EX_OPE_CODBARRA_NO_EXISTE);
				}
				/* COMENTAR para cargas masivas END */
						
				
				//A: agregar producto del mix, agregar en fo_productos, marcar en bo_productos
				JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
				
				//buscar si el producto habia sido agregado anteriormente y fue eliminado (estado = 'E') enviar mensaje
				ProductoEntity prodEnt = productosDAO.getProductByProdMix(prod.getCod_prod_1(), prod.getUni_med());
				
				if(prodEnt==null){
					//Insertar producto 
					logger.debug("Insertar producto");
					ProcAddProductDTO dto = new ProcAddProductDTO();
					dto.setCod_sap(prod.getCod_prod_1());
					dto.setEstado(Constantes.MIX_ESTADO);//estado = nuevo
					dto.setGenerico(Constantes.MIX_GENERICO);//P Producto
					dto.setUni_vta(prod.getUni_med());
					dto.setTipo(Constantes.MIX_TIPO);
					//dto.setMarca(Constantes.MIX_MARCA);//fo necesita codigo
					dto.setDesc_corta(prod.getDes_corta());
					dto.setDesc_larga(prod.getDes_larga());
					//dto.setUni_medida(Constantes.MIX_UNIDAD_MEDIDA);//fo necesita codigo
					dto.setContenido(Constantes.MIX_CONTENIDO); 
					dto.setAdm_coment(Constantes.MIX_ADM_COMENTARIOS);
					dto.setEs_prepar(Constantes.MIX_ES_PREPARABLE);
					dto.setInt_medida(Constantes.MIX_INT_MEDIDA);
					dto.setInt_max(Constantes.MIX_INT_MAXIMO);
					dto.setFecha(Formatos.getFecHoraActual());
					dto.setId_user(param.getId_user());
					dto.setId_bo(param.getId_prod());
					imagen_string  = prod.getCod_prod_1()+"-"+prod.getUni_med()+Constantes.PRODUCTO_FO_IMG_EXT;
					logger.debug("Archivo de imagen : "+imagen_string);
					dto.setImg_min_ficha(imagen_string);
					dto.setImg_ficha(imagen_string);
					
					
					int id = -1;
					id = productosDAO.addByProductMix(dto);//sin id_marca, sin id_ume
					logger.debug("nuevo id:"+id);
					if (id!=-1){
						//colocar en el log
						ProductoLogDTO log = new ProductoLogDTO();
						log.setCod_prod(id);
						log.setCod_prod_bo(prod.getId().longValue());
						log.setFec_crea(dto.getFecha());
						log.setUsuario(param.getUsr_login());
						log.setTexto(param.getMnsAgreg());
						productosDAO.agregaLogProducto(log);
						//obtiene de bo_precios
						List lst_precios = productosSapDAO.getPreciosByProdId(prod.getId().longValue());
						//agregar en fo_precios_locales
						boolean addPrecios = false;
						PrecioSapEntity  precio = null;
						for(int i = 0;i<lst_precios.size();i++){
							precio = (PrecioSapEntity)lst_precios.get(i);
							try{
								addPrecios = productosSapDAO.setPreciosByProdId(precio,id);
							}catch(ProductosSapDAOException ex){
								if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
									logger.debug("precio duplicado");
									trx1.rollback();
									throw new ProductosSapException(Constantes._EX_OPE_DUP_COD_PRECIO);
								}
							}
							logger.debug("precio:"+precio.getPrec_valor().doubleValue()+", res?"+addPrecios);
						}
						
						//obtiene de bo_codbarra
						List lst_codbarras = productosSapDAO.getCodBarrasByProdId(prod.getId().longValue());
						//agregar en fo_cod_Barra
						boolean addCodBarras = false;
						CodBarraSapEntity codBarra = null;
						for(int j=0; j<lst_codbarras.size(); j++){
							codBarra = (CodBarraSapEntity) lst_codbarras.get(j);
							try{
								addCodBarras = productosSapDAO.setCodBarrasByProdId(codBarra,id);
							}catch(ProductosSapDAOException ex){
								if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
									logger.debug("codigo de barra duplicada");
									trx1.rollback();
									throw new ProductosSapException(Constantes._EX_OPE_DUP_COD_BARRA);
								}
							}
							logger.debug("codBarra:"+codBarra.getCod_barra()+", res?"+addCodBarras);
						}
						//marcar en bo_productos
						prod.setMixWeb("S");
						productosSapDAO.updProduct(prod);
						res = true;
					}
				} else{
					logger.debug("agregar: estado fo:"+prodEnt.getEstado());
					//el producto existe, ver estado, si es publicado, actualizar datos
					if (prodEnt.getEstado().equals(Constantes.ESTADO_PUBLICADO)){
						
						prodEnt.setCod_sap(prod.getCod_prod_1());
						prodEnt.setTipre(prod.getUni_med());
						prodEnt.setDesc_corta(prod.getDes_corta());
						prodEnt.setDesc_larga(prod.getDes_larga());
						prodEnt.setUser_mod(new Integer(param.getId_user()+""));
						boolean actualizo = productosDAO.actProductoByProdMixId(prodEnt);
						logger.debug("actualizo:"+actualizo);
						//colocar en el log
						ProductoLogDTO log = new ProductoLogDTO();
						log.setCod_prod(prodEnt.getId().longValue());
						log.setCod_prod_bo(prod.getId().longValue());
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(param.getUsr_login());
						log.setTexto(param.getMnsAgreg());
						productosDAO.agregaLogProducto(log);
						//marcar en bo_productos
						prod.setMixWeb("S");
						productosSapDAO.updProduct(prod);
						res = true;
					}else if (prodEnt.getEstado().equals(Constantes.ESTADO_DESPUBLICADO)){
						logger.debug("producto con estado despublicado");
						trx1.rollback();
						throw new ProductosSapException(Constantes.ESTADO_DESPUBLICADO);
					}
					//si el producto existe y tiene estado eliminado, enviar mensaje	FALTA
					//return res;
				}
			} else if(param.getAction().equals("Sacar")){
				//S: sacar producto del mix, eliminar de fo_productos, desmarcar en bo_productos
				JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
				
				//verifica estado del producto
				
				ProductoEntity fo_prod = productosDAO.getProductoByProdSapId(param.getId_prod());
				logger.debug("sacar: estado fo:"+fo_prod.getEstado());
				if(fo_prod.getEstado().equals(Constantes.ESTADO_PUBLICADO)){
					//actualiza el estado del fo_product
					fo_prod.setEstado(Constantes.ESTADO_DESPUBLICADO);
					boolean act = productosDAO.actEstProductoById(fo_prod);
					logger.debug("act:"+act);
					if(act){
						//marcar en bo_productos
						prod.setMixWeb("N");
						productosSapDAO.updProduct(prod);
						//colocar en el log
						ProductoLogDTO log = new ProductoLogDTO();
						log.setCod_prod(fo_prod.getId().longValue());
						log.setCod_prod_bo(prod.getId().longValue());
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(param.getUsr_login());
						log.setTexto(param.getMnsElim());
						productosDAO.agregaLogProducto(log);
						res = true;
					}
					
				}
				if(fo_prod.getEstado().equals(Constantes.ESTADO_NUEVO)){
					//obtiene el id del fo-producto a eliminar
					long id_fo_producto = fo_prod.getId().longValue();//productosDAO.
					//eliminar 
					boolean elimina = productosDAO.delByProductMix(prod.getCod_prod_1(), prod.getUni_med());
					if (elimina){
						//marcar en bo_productos
						prod.setMixWeb("N");
						productosSapDAO.updProduct(prod);
						
						//colocar en el log
						ProductoLogDTO log = new ProductoLogDTO();
						log.setCod_prod(id_fo_producto);
						log.setCod_prod_bo(prod.getId().longValue());
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(param.getUsr_login());
						log.setTexto(param.getMnsElim());
						productosDAO.agregaLogProducto(log);
						
						res = true;
					}

				}
				if(fo_prod.getEstado().equals(Constantes.ESTADO_DESPUBLICADO)){
					logger.debug("producto con estado despublicado");
					trx1.rollback();
					throw new ProductosSapException(Constantes.ESTADO_DESPUBLICADO);
				}
				

			}
		}catch(ProductosDAOException ex){
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del producto sap");
				throw new ProductosSapException(Constantes._EX_PSAP_ID_NO_EXISTE, ex);
			} if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
				logger.debug("duplicado en codigo de barra");
				throw new ProductosSapException(Constantes._EX_OPE_DUP_COD_BARRA, ex);
			}
			throw new SystemException("Error no controlado",ex);
		}catch(ProductosSapDAOException ex2){
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			logger.debug("Problema setModMPVProduct:"+ex2);
			throw new ProductosSapException(ex2);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
		}
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
		logger.debug("res:"+res);
	    return res;
	}

	/**
	 * Obtener información del producto SAP
	 * 
	 * @param  codProd long 
	 * @return ProductosSapDTO 
	 * @throws ProductosSapException
	 * @throws SystemException
	 * */
	public ProductosSapDTO getProductosSapById(long codProd)throws ProductosSapException, SystemException{
		ProductosSapDTO res = null;
		try{
			logger.debug("en getProductosSapById");
			JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory			
			.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
			
			JdbcCategoriasSapDAO catSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			ProductoSapEntity prod = productosSapDAO.getProductSapById(codProd);
			int num_conv=0;
			if(prod.getNum_conv()!=null)
				num_conv = prod.getNum_conv().intValue();
			int den_conv=0;
			if(prod.getDen_conv()!=null)
				den_conv = prod.getDen_conv().intValue();
			String fec_carga = "";
			if(prod.getFecCarga()!=null)
				fec_carga = prod.getFecCarga().toString();

				
			res=new ProductosSapDTO();
			/*res = new ProductosSapDTO(prod.getId().longValue(), prod.getId_cat(), prod.getUni_med(),
					prod.getCod_prod_1(), prod.getCod_prod_2(), prod.getDes_corta(), prod.getDes_larga(),
					prod.getEstado(), prod.getMarca(), prod.getCod_propal(), prod.getOrigen(),
					prod.getUn_base(), prod.getEan13(), prod.getUn_empaque(), num_conv, 
					den_conv, prod.getAtrib9(), prod.getAtrib10(), prod.getEstActivo(), 
					prod.getNom_cat_sap(), fec_carga, prod.getMixWeb(),prod.getNom_cat_sap());*/
			res.setId(prod.getId().longValue());
			res.setId_cat(prod.getId_cat());
			res.setUni_med(prod.getUni_med());
			res.setCod_prod_1(prod.getCod_prod_1());
			res.setCod_prod_2(prod.getCod_prod_2());
			res.setDes_corta(prod.getDes_corta());
			res.setDes_larga(prod.getDes_larga());
			res.setEstado(prod.getEstado());
			res.setMarca(prod.getMarca());
			res.setCod_propal(prod.getCod_propal());
			res.setOrigen(prod.getOrigen());
			res.setUn_base(prod.getUn_base());
			res.setEan13(prod.getEan13());
			res.setUn_empaque(prod.getUn_empaque());
			res.setNum_conv(num_conv);
			res.setDen_conv(den_conv);
			res.setAtrib9(prod.getAtrib9());
			res.setAtrib10(prod.getAtrib10());
			res.setEstActivo(prod.getEstActivo());
			res.setFec_carga(fec_carga);
			res.setFlag_mix(prod.getMixWeb());
			res.setNom_cat_sap(prod.getNom_cat_sap());

			logger.debug("seccion: "+prod.getSeccion());
			logger.debug("rubro: "+prod.getRubro());
			logger.debug("subrubro: "+prod.getSubrubro());
			logger.debug("grupo: "+prod.getGrupo());
			
			
			res.setCat_seccion(catSapDAO.getNomCatSapById(prod.getSeccion()));
			res.setCat_rubro(catSapDAO.getNomCatSapById(prod.getSeccion()+prod.getRubro()));
			res.setCat_subrubro(catSapDAO.getNomCatSapById(prod.getSeccion()+prod.getRubro()+prod.getSubrubro()));
			res.setCat_grupo(catSapDAO.getNomCatSapById(prod.getSeccion()+prod.getRubro()+prod.getSubrubro()+prod.getGrupo()));
			
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getProductosSapById:"+ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del producto sap");
				throw new ProductosSapException(Constantes._EX_PSAP_ID_NO_EXISTE);
			}
            throw new SystemException("Error no controlado",ex);
		} catch (CategoriasSapDAOException ex) {
			logger.debug("Problema getProductosSapById:"+ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod de la categoria sap");
				throw new ProductosSapException(Constantes._EX_PSAP_CAT_DECOINVALIDA);
			}
            throw new SystemException("Error no controlado",ex);
		}
		return res;
	}
	
	/**
	 * Obtener la lista de código de barras de un producto 
	 * 
	 * @param  codProd long 
	 * @return List CodigosBarraSapDTO  
	 * @throws ProductosSapException
	 * @throws SystemException
	 * */
	public List getCodBarrasByProdId(long codProd)throws ProductosSapException, SystemException{
		JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory			
		.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
		List result = new ArrayList();
		try{
			logger.debug("en getCodBarrasByProdId");
			
			List listaBarras= new ArrayList();
			listaBarras = (List) productosSapDAO.getCodBarrasByProdId(codProd);
			CodBarraSapEntity barra;
			for (int i = 0; i < listaBarras.size(); i++) {
				barra = null;
				barra = (CodBarraSapEntity) listaBarras.get(i);
				
				CodigosBarraSapDTO reg_barra = new CodigosBarraSapDTO();
				
				reg_barra.setCod_barra(barra.getCod_barra());
				reg_barra.setCod_prod_1(barra.getCod_prod_1());
				reg_barra.setEstado(barra.getEstado());
				reg_barra.setId_prod(barra.getId_prod());
				reg_barra.setTip_cod_barra(barra.getTip_cod_barra());
				reg_barra.setUni_med(barra.getUni_med());				
				
				result.add(reg_barra);
			}
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getCodBarrasByProdId:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
		
		}

	/**
	 * Obtener la lista de precios de un producto 
	 * 
	 * @param  codProd long 
	 * @return List PreciosSapDTO   
	 * @throws ProductosSapException
	 * @throws SystemException
	 * */
	public List getPreciosByProdId(long codProd)throws ProductosSapException, SystemException{
		JdbcProductosSapDAO productosSapDAO = (JdbcProductosSapDAO) DAOFactory			
		.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();
		List result = new ArrayList();
		try{
			logger.debug("en getPreciosByProdId");
			
			List listaPrecios= new ArrayList();
			listaPrecios = (List) productosSapDAO.getPreciosByProdId(codProd);
			PrecioSapEntity precio;
			for (int i = 0; i < listaPrecios.size(); i++) {
				precio = null;
				precio = (PrecioSapEntity) listaPrecios.get(i);
				
				PreciosSapDTO reg_precio = new PreciosSapDTO();		
				
				reg_precio.setId_loc(precio.getId_loc());				
				reg_precio.setId_prod(precio.getId_prod());
				reg_precio.setCod_prod_1(precio.getCod_prod_1());
				reg_precio.setCod_local(precio.getCod_local());
				reg_precio.setPrec_valor(precio.getPrec_valor());				
				reg_precio.setUni_med(precio.getUni_med());				
				reg_precio.setCod_barra(precio.getCod_barra());
				reg_precio.setEst_act(precio.getEst_act());								
				
				result.add(reg_precio);
			}
		}catch(ProductosSapDAOException ex){
			logger.debug("Problema getPreciosByProdId:"+ex);
			throw new ProductosSapException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la informacion del producto web , segun codigo del producto SAP
	 * 
	 * @param id_producto
	 * @return ProductoEntity
	 * @throws PedidosException
	 * @throws SystemException
	 */
	public ProductoEntity getProductoFOByIdProdSap(long id_producto) throws ProductosSapException, SystemException{
		ProductoEntity prod1 = null;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
		try{
			
			prod1 = productosDAO.getProductoByProdSapId(id_producto);
			
		}catch (ProductosDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod de producto sap");
				throw new ProductosSapException(Constantes._EX_PSAP_ID_NO_EXISTE, ex);
			}
			throw new SystemException("Error no controlado",ex);
		}
		
		return prod1;
	}
	
	/**
	 * Inserta productos de la categoría para Grability 
	 * 
	 * @param id_cat
	 * @return boolean
	 * @throws ProductosSapException
	 */
	public boolean updateMarcaGrabilityProducto(String id_cat, String flag) 
			throws ProductosSapException{
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		boolean resultado = false;
		try{
			
			resultado = productosDAO.updateMarcaGrabilityProducto(id_cat, flag);
			
		}catch (ProductosDAOException ex) {
			logger.debug("Error al actualizar productos " + ex.getMessage());
			throw new ProductosSapException("Error al actualizar productos " + ex.getMessage());
		}
		
		return resultado;
	}
	
	/**
	 * Elimina productos de la categoría para que no aparezcan en Grability
	 * 
	 * @param id_cat
	 * @return boolean
	 * @throws ProductosSapException
	 */
	public boolean deleteMarcaGrabilityProducto(String id_cat) 
			throws ProductosSapException{
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		boolean resultado = false;
		try{
			
			resultado = productosDAO.deleteMarcaGrabilityProducto(id_cat);
			
		}catch (ProductosDAOException ex) {
			logger.debug("Error al actualizar productos " + ex.getMessage());
			throw new ProductosSapException("Error al actualizar productos " + ex.getMessage());
		}
		
		return resultado;
	}
}
