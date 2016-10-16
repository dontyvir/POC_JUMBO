package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CategoriaEntity;
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
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelAllProductCategory;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModFichaTecnicaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModGenericItemDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSugProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcCategoriasDAO;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.MotivosDespDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoSugerDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Controlador de procesos sobre los productos Web.
 * @author BBR
 *
 */
public class ProductosCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene un nuevo código sap para el producto generico
	 * 
	 * @return long 
	 * @throws ProductosException
	 * 
	 * */
	public long getCodSapGenerico() throws ProductosException{
		long result=0;
		try{
			logger.debug("en getCodSapGenerico");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.getCodSapGenerico();
		}catch(ProductosDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	

	/**
	 * Obtiene la lista, segun los criterios de busqueda: cod. producto, cod. producto sap, estado, tipo, 
	 * cod. categoria (en el caso que la consulta sea desde el monitor de categorias)
	 * 
	 * @param  criterio ProductosCriteriaDTO 
	 * @return List ProductosDTO
	 * @throws ProductosException
	 * 
	 * */
	public List getProductosByCriteria(ProductosCriteriaDTO criterio) throws ProductosException{
		List result= new ArrayList();
		
		try{
			logger.debug("en getProductosByCriteria");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaProd= new ArrayList();
			listaProd = (List) productosDAO.listadoProductosByCriteria(criterio);
			ProductoEntity prod = null;
			for (int i = 0; i < listaProd.size(); i++) {
				prod = null;
				prod = (ProductoEntity) listaProd.get(i);
				ProductosDTO proddto = new ProductosDTO();
				proddto.setId(prod.getId().longValue());
				proddto.setEstado(prod.getEstado());
				proddto.setTipo(prod.getTipo());
				proddto.setDesc_corta(prod.getDesc_corta());
				proddto.setDesc_larga(prod.getDesc_larga());
				proddto.setGenerico(prod.getGenerico());
				proddto.setUni_med_desc(prod.getUni_med_desc());
				proddto.setCod_sap(prod.getCod_sap());
				
				// Se agrega Marca
				proddto.setNom_marca(prod.getNom_marca());
				
                //Se agregan intervalo y máximo
                proddto.setInter_valor(prod.getInter_valor().doubleValue());
                proddto.setInter_max(prod.getInter_max().doubleValue());
                
				result.add(proddto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getProductosByCriteria:"+ex);
			throw new ProductosException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la cantidad de productos, segun los criterios de busqueda: cod. producto, cod. producto sap, estado, tipo, 
	 * cod. categoria (en el caso que la consulta sea desde el monitor de categorias)
	 * 
	 * @param  criterio ProductosCriteriaDTO 
	 * @return int
	 * @throws ProductosException
	 * 
	 * */
	public int getProductosCountByCriteria(ProductosCriteriaDTO criterio) throws ProductosException{
		int result=0;
		try{
			logger.debug("en getProductosCountByCriteria");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.getProductosCountByCriteria(criterio);
		}catch(ProductosDAOException ex){
			logger.debug("Problema getProductosCountByCriteria:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}

	/**
	 * Obtiene la información del producto web
	 * 
	 * @param  codProd long 
	 * @return ProductosDTO
	 * @throws ProductosException
	 * 
	 * */
	public ProductosDTO getProductosById(long codProd) throws ProductosException{
		
		ProductosDTO result=new ProductosDTO();
		try{
			logger.debug("en getProductosById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			ProductoEntity prod = productosDAO.getProductoById(codProd);
			logger.debug("prod: "+prod);
			if(prod== null){
				logger.debug("producto nulo:"+Constantes._EX_PROD_ID_NO_EXISTE);
				throw new ProductosException(Constantes._EX_PROD_ID_NO_EXISTE);
			}
				
			
			long id_padre = 0;
			long uni_id = 0;
			long mar_id = 0;
			double uni_med = 0;
			int rank_vtas = 0;
			long user_mod = 0;
			String fec_crea = "";
			String fec_mod = "";
			if(prod.getId_padre()!=null)
				id_padre = prod.getId_padre().longValue();
			if(prod.getUni_id()!=null)
				uni_id = prod.getUni_id().longValue();
			if(prod.getMar_id()!=null)
				mar_id = prod.getMar_id().longValue();
			if(prod.getUnidad_medidad()!=null)
				uni_med = prod.getUnidad_medidad().doubleValue();
			if(prod.getRank_ventas()!=null)
				rank_vtas = prod.getRank_ventas().intValue();
			if(prod.getFec_crea()!=null)
				fec_crea = prod.getFec_crea().toString();
			if(prod.getFec_mod()!=null)
				fec_mod = prod.getFec_mod().toString();
			if(prod.getUser_mod()!=null)
				user_mod = prod.getUser_mod().longValue();
			
			//lista de items
			List lst_items = productosDAO.getItemsByProductId(codProd);
			
			//lista de sugeridos
			List lst_sug = productosDAO.getSugeridosByProductId(codProd);
			
			result = new ProductosDTO(prod.getId().longValue(), id_padre,
					prod.getTipre(),prod.getCod_sap(), uni_id, mar_id,
					prod.getEstado(),prod.getTipo(), prod.getDesc_corta(),prod.getDesc_larga(), 
					prod.getImg_mini_ficha(), prod.getImg_ficha(),uni_med , 
					prod.getValor_difer(),rank_vtas ,fec_crea, fec_mod,
					user_mod ,prod.getGenerico(), prod.getNom_marca(), prod.getAdm_coment(), prod.getEs_prep(), prod.getEs_pesable(),
					prod.getInter_valor().doubleValue(), prod.getInter_max().doubleValue(), lst_items, lst_sug);
			result.setId_bo(prod.getId_bo().longValue());
			result.setEsParticionable(prod.getEsParticionable());
			result.setParticion(prod.getParticion());
			result.setEvitarPubDes(prod.isEvitarPubDes());
			result.setMotivoDesId(prod.getPro_id_desp());
            result.setPilaPorcion(prod.getPilaPorcion());
            result.setIdPilaUnidad(prod.getIdPilaUnidad());
            
            result.setBanner_prod(prod.getBanner_prod());
            result.setDesc_banner_prod(prod.getDesc_banner_prod());
            result.setColor_banner_prod(prod.getColor_banner_prod());
            
            result.setPublicadoGrability(prod.isPublicadoGrability());

		}catch(ProductosDAOException ex){
			logger.debug("Problema getProductosById:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}

	/**
	 * Agrega un item a un producto<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  proc ProcModGenericItemDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosException
	 * @throws SystemException
	 * 
	 * */
	public boolean agregaItemProducto(ProcModGenericItemDTO proc)throws ProductosException,SystemException{
		boolean result = false;
		
		logger.debug("en agregaItemProducto");
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();

//		 Creamos trx
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
				
		
		try{
			
			//result = productosDAO.agregaItemProducto(item);
			
			String paramAction = proc.getAction();
			//verificar si el producto y producto_item son iguales
			if(proc.getId_prod_gen()==proc.getId_prod_item()){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_PROD_ITEM_IGUAL_PROD);
			}
			//ver si existe item
			if(!productosDAO.isProductById(proc.getId_prod_item())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_PROD_ID_NO_EXISTE);
			}
			//ver si el producto es generico o es producto
			//ProductosDTO prodItem = getProductosById(proc.getId_prod_item());
			ProductoEntity prodItem = productosDAO.getProductoById(proc.getId_prod_item());
			if (paramAction.equals("agregar")){
				if( prodItem.getGenerico().equals("G")){
					trx1.rollback();
					throw new ProductosException(Constantes._EX_PROD_ITEM_GENERICO);
				}
				
				ProductoEntity item = new ProductoEntity();
				item.setId(new Long(proc.getId_prod_item()));//colocar id del producto escogido
				item.setId_padre(new Long(proc.getId_prod_gen()));//colocar id_del producto generico
				//item.setEstado("A");
				item.setValor_difer(proc.getAtrdiff_val());//colocar el el atributo diferenciador ingresado
				boolean resp = productosDAO.agregaItemProducto(item);
				logger.debug("agregar:"+resp);
				if(resp){
					// agregar la accion en el log del producto
					ProductoLogDTO log = new ProductoLogDTO(); 
					log.setCod_prod(proc.getId_prod_gen());
					log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
					log.setFec_crea(Formatos.getFecHoraActual());
					log.setUsuario(proc.getUsr_login());
					log.setTexto(prodItem.getDesc_corta()+":"+proc.getMns_agreg());
					int resLog = productosDAO.agregaLogProducto(log);
					logger.debug("se guardo log con id:"+resLog);
				}
			} else
				
			//si accion = desasociar: desasociar un item
			if (paramAction.equals("desasociar")){
				ProductoEntity item = new ProductoEntity();
				item.setId(new Long(proc.getId_prod_item()));//colocar id del producto escogido
				boolean resp = productosDAO.eliminaItemProducto(item);
				logger.debug("desasociar:"+resp);
				if(resp){
					// agregar la accion en el log del producto
					ProductoLogDTO log = new ProductoLogDTO(); 
					log.setCod_prod(proc.getId_prod_gen());
					log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
					log.setFec_crea(Formatos.getFecHoraActual());
					log.setUsuario(proc.getUsr_login());
					log.setTexto(prodItem.getDesc_corta()+":"+proc.getMns_desa());
					int resLog = productosDAO.agregaLogProducto(log);
						logger.debug("se guardo log con id:"+resLog);
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
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
		}
		logger.debug("result?"+result);
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
		return result;
	}

	/**
	 * Elimina un item relacionado a un producto<br>
	 * 
	 * @param  item ProductoEntity 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosException
	 * @throws SystemException
	 * 
	 * */
	public boolean eliminaItemProducto(ProductoEntity item)throws ProductosException, SystemException{
		boolean result = false;
		try{
			logger.debug("en eliminaItemProducto");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.eliminaItemProducto(item);
		}catch(ProductosDAOException ex){
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		}
		logger.debug("result?"+result);	
		return result;
	}

	/**
	 * Agregar un producto sugerido a otro producto<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  suger ProductoSugerDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosException
	 * @throws SystemException
	 * 
	 * */
	public boolean agregaSugeridoProducto(ProductoSugerDTO suger)throws ProductosException, SystemException{
		boolean result = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
//		 Creamos trx
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		
		try{
			logger.debug("en agregaSugeridoProducto");
			
			result = productosDAO.agregaSugeridoProducto(suger);
			//en el caso que el formato sea 'B': bidireccional
			if(suger.getFormato().equals("B")){
				long nvo_suger = suger.getId_base();
				long nvo_base = suger.getId_suger();
				suger.setId_base(nvo_base);
				suger.setId_suger(nvo_suger);
				boolean res = productosDAO.agregaSugeridoProducto(suger);
				logger.debug("res Bidireccional:"+res);
			}
			
		}catch(ProductosDAOException ex){
			
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			
			
			logger.debug("Problema agregaSugeridoProducto:"+ex);
			throw new ProductosException(ex);
		}
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
		logger.debug("result?"+result);	
		return result;
	}

	/**
	 * Eliminar un producto sugerido a otro producto<br>
	 * 
	 * @param  id_suger long
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosException
	 * 
	 * */
	public boolean eliminaSugeridoProducto(long id_suger)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en eliminaSugeridoProducto");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.eliminaSugeridoProducto(id_suger);
		}catch(ProductosDAOException ex){
			logger.debug("Problema eliminaSugeridoProducto:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
		return result;
	}

	/**
	 * Obtiene las categorias relacionadas al producto
	 * 
	 * @param  codProd long
	 * @return List CategoriasDTO
	 * @throws ProductosException
	 */
	public List getCategoriasByProductId(long codProd) throws ProductosException{
		
		List result= new ArrayList();
		try{
			logger.debug("en getCategoriasByProductId");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaCat = new ArrayList();
			listaCat = (List) productosDAO.getCategoriasByProductId(codProd);
			CategoriaEntity cat = null;
			for (int i = 0; i < listaCat.size(); i++) {
				cat = null;
				cat = (CategoriaEntity) listaCat.get(i); 
				CategoriasDTO catDto = new CategoriasDTO();
				catDto.setId_cat(cat.getId().longValue());
				catDto.setNombre(cat.getNombre());
				catDto.setOrden(cat.getOrden().intValue());
				catDto.setId_procat(cat.getProcat_id().longValue());
				catDto.setCon_pago(cat.getCon_pago());
				result.add(catDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getCategoriasByProductId:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene los tipos de producto
	 * 
	 * @return List EstadoDTO 
	 * @throws ProductosException
	 */
	public  List getTiposProductos()throws ProductosException{
		List result= new ArrayList();
		try{
			logger.debug("en getTiposProductos");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) productosDAO.getEstados("TPR","S");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getTiposProductos:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}

	/**
	 * Obtiene los estados visibles del producto
	 * 
	 * @return List EstadoDTO 
	 * @throws ProductosException
	 */
	public  List getEstadosProductos()throws ProductosException{
		List result= new ArrayList();
		try{
			logger.debug("en getEstadosProductos");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) productosDAO.getEstados("PR","S");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getEstadosProductos:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene los estados de producto, visibles o no
	 * 
	 * @return List EstadoDTO 
	 * @throws ProductosException
	 */
	public  List getEstadosAll()throws ProductosException{
		List result= new ArrayList();
		try{
			logger.debug("en getEstadosAll");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) productosDAO.getEstados("PR","");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getEstadosAll:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de unidad de medida
	 * 
	 * @return List UnidadMedidaDTO 
	 * @throws ProductosException
	 */
	public  List getUnidMedida()throws ProductosException{
		List result= new ArrayList();
		try{
			logger.debug("en getUnidMedida");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) productosDAO.getUnidMedida();
			UnidadMedidaEntity ume = null;
			for (int i = 0; i < listaEst.size(); i++) {
				ume = null;
				ume = (UnidadMedidaEntity) listaEst.get(i); 
				UnidadMedidaDTO estDto = new UnidadMedidaDTO(ume.getId().longValue(),ume.getDesc(),
						ume.getCantidad().doubleValue(),ume.getEstado());

				result.add(estDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getUnidMedida:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}

	/**
	 * Verifica si la categoria esta asociada al producto
	 * 
	 * @param  id_cat long 
	 * @param  id_prod long 
	 * @return boolean, devuelve <i>true</i> si existe la relación, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 */
	public boolean isCatAsocProd(long id_cat, long id_prod)throws ProductosException{
		boolean existe = false;
		try{
			logger.debug("en isCatAsocProd");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			existe = productosDAO.isCatAsocProd(id_cat, id_prod);
		}catch(ProductosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("existe?"+existe);	
		return existe;
	}
	
	/**
	 * Actualza la relación entre una categoría y un producto.
	 * 
	 * @param  param ProductoCategDTO
	 * @return boolean, devuelve <i>true</i> si existe la relación, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws CategoriasException
	 */
	public boolean setModCategoryProduct(ProductoCategDTO param)throws ProductosException{
		boolean existe = false;
		try{
			logger.debug("en isCatAsocProd");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			existe = productosDAO.ModCategoryProduct(param);
		}catch(ProductosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("existe?"+existe);	
		return existe;
	}
	/**
	 * Agregar la categoria al producto.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  procparam ProcAddCategoryProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se agregó con éxito, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws SystemException
	 * @throws CategoriasException
	 */
	public boolean setAddCategoryProduct(ProcAddCategoryProductDTO procparam)throws ProductosException,SystemException, CategoriasException{
		boolean agrega = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		JdbcCategoriasDAO catDAO = (JdbcCategoriasDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
		
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		try {
			catDAO.setTrx(trx1);
		} catch (CategoriasDAOException e2) {
			logger.error("Error al asignar transacción al dao Categoria");
			throw new SystemException("Error al asignar transacción al dao Categoria");
		}

		
		try{
			logger.debug("en setAddCategoryProduct");
			
			
			
			//verifica si existe producto
			boolean existeProd = productosDAO.isProductById(procparam.getId_producto());
			if (!existeProd){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_PROD_ID_NO_EXISTE);
			}
			//verifica si existe categoria
			boolean existeCat = catDAO.isCategoriaById(procparam.getId_categoria());
			if (!existeCat){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_CAT_ID_NO_EXISTE);
			}
			//verifica si existe la relacion entre categoria y producto
			boolean existeRelCatProd = productosDAO.isCatAsocProd(procparam.getId_categoria(), procparam.getId_producto());
			if (existeRelCatProd){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_CAT_PROD_REL_EXISTE);
			}
				CategoriaEntity cat = catDAO.getCategoriaById(procparam.getId_categoria());
				//si la categoria no es terminal, no debe agregar producto
				if(!cat.getTipo().equals("T")){
					trx1.rollback();
					throw new CategoriasException(Constantes._EX_CAT_NO_ES_TERMINAL);
				}
					
					//ver si ya existe la categoria asociada al producto
					if( !existeRelCatProd){
						agrega = productosDAO.agregaCategProducto(procparam);
						logger.debug("agrega:"+agrega);
				
						if(agrega){
							// agregar la accion en el log del producto
							String fec_crea = Formatos.getFecHoraActual();
							ProductoLogDTO log = new ProductoLogDTO(); 
							log.setCod_prod(procparam.getId_producto());
							log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
							log.setFec_crea(fec_crea);
							log.setUsuario(procparam.getUsr_login());
							log.setTexto(cat.getNombre()+":"+procparam.getMensaje());
							int resLog = productosDAO.agregaLogProducto(log);
							logger.debug("se guardo log con id:"+resLog);
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
				logger.debug("no existe cod del producto");
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
			
		}catch(CategoriasDAOException ex){
			
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}

			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new CategoriasException( Constantes._EX_CAT_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
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
		
		logger.debug("agrega?"+agrega);	
		return agrega;
	}
	
	/**
	 * Eliminar la categoria del producto.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  proc ProcDelCategoryProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se eliminó con éxito, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws SystemException
	 * @throws CategoriasException
	 */
	public boolean setDelCategoryProduct(ProcDelCategoryProductDTO proc)throws ProductosException, CategoriasException, SystemException{
		boolean elimina = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		JdbcCategoriasDAO catDAO = (JdbcCategoriasDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
		
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
				
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción",e1);
		}
		
		// Marcamos los dao's con la transacción
		try {
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		try {
			catDAO.setTrx(trx1);
		} catch (CategoriasDAOException e2) {
			logger.error("Error al asignar transacción al dao Categoria");
			throw new SystemException("Error al asignar transacción al dao Categoria");
		}
		
		try{
			logger.debug("en setDelCategoryProduct");
		
			//verifica si el producto existe
			if(!productosDAO.isProductById(proc.getId_producto())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_PROD_ID_NO_EXISTE);
			}
			//verifica si la categoria existe
			if(!catDAO.isCategoriaById(proc.getId_categoria())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_CAT_ID_NO_EXISTE);
			}
			//verifica si la relacion entre categoria y producto existe
			if(!productosDAO.isCatAsocProd(proc.getId_categoria(), proc.getId_producto())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_CAT_PROD_REL_NO_EXISTE);
			}
			elimina = productosDAO.eliminaCategProducto(proc);
			logger.debug("desasociar:"+elimina);
		     if(elimina){
				 // agregar la accion en el log del producto
		    	 ProductoLogDTO log = new ProductoLogDTO(); 
				 log.setCod_prod(proc.getId_producto());
				 log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
				 log.setFec_crea(Formatos.getFecHoraActual());
				 log.setUsuario(proc.getUsr_login());
				 log.setTexto(catDAO.getCategoriaById(proc.getId_categoria()).getNombre()+":"+proc.getMensaje());
				 int resLog = productosDAO.agregaLogProducto(log);
				 logger.debug("se guardo log con id:"+resLog);
				 List listCateg = productosDAO.getCategoriasByProductId(proc.getId_producto());
				 logger.debug("Tamaño lista de categorias: " + listCateg.size());
				 if (listCateg.size() == 0){
					 ProductoEntity prodEnt = new ProductoEntity();
					 prodEnt.setId(new Long(proc.getId_producto()+""));
					 prodEnt.setEstado("D");
					 productosDAO.actEstProductoById(prodEnt);
					 /*
					 throw new ProductosException(Constantes._EX_PROD_CAT_NO_TIENE);
					 */ 
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
			
			logger.debug("Problema setDelCategoryProduct:"+ex);
			throw new ProductosException(ex);
		}catch(CategoriasDAOException ex){
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			
			logger.debug("Problema setDelCategoryProduct:"+ex);
			throw new CategoriasException(ex);
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
		
		logger.debug("elimina?"+elimina);	
		return elimina;
	}
	
	//20121120avc	
	public boolean setDelAllProductCategory(ProcDelAllProductCategory proc)throws ProductosException, CategoriasException, SystemException{
		boolean elimina = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		JdbcCategoriasDAO catDAO = (JdbcCategoriasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
		
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
				
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción",e1);
		}
		
		// Marcamos los dao's con la transacción
		try {
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		try {
			catDAO.setTrx(trx1);
		} catch (CategoriasDAOException e2) {
			logger.error("Error al asignar transacción al dao Categoria");
			throw new SystemException("Error al asignar transacción al dao Categoria");
		}
		
		try{
			logger.debug("en setDelAllProductCategory");
		
			//verifica si la categoria existe
			if(!catDAO.isCategoriaById(proc.getId_categoria())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_CAT_ID_NO_EXISTE);
			}
			
			
			
			elimina = catDAO.eliminaAllProductoCateg(proc.getId_categoria());
			logger.debug("desasociar:"+elimina);
		    if(elimina) {
				 List listProd = catDAO.getProductosSinMasCategorias(proc.getId_categoria());
				 logger.debug("Tamaño lista de categorias: " + listProd.size());
				 for(int i=0;i<listProd.size();i++) {
				 	 ProductoEntity prodEnt = (ProductoEntity) listProd.get(i);
					 prodEnt.setEstado("D");
					 productosDAO.actEstProductoById(prodEnt);
					 /*
					 throw new ProductosException(Constantes._EX_PROD_CAT_NO_TIENE);
					 */ 
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
			
			logger.debug("Problema setDelCategoryProduct:"+ex);
			throw new ProductosException(ex);
		}catch(CategoriasDAOException ex){
			// rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			
			logger.debug("Problema setDelCategoryProduct:"+ex);
			throw new CategoriasException(ex);
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
		
		logger.debug("elimina?"+elimina);	
		return elimina;
	}
	//]20121120avc
	/**
	 * Eliminar el producto genérico.<br>
	 * 
	 * @param  procparam ProcDelCategoryProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se eliminó con éxito, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public boolean setDelGenericProduct(ProcDelGenericProductDTO procparam)throws ProductosException, SystemException{
		boolean elimina = false;
		try{
			logger.debug("en setDelGenericProduct");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			//obtener los items del producto generico, actualizar id_padre a null y atributo_difer = "";
			List lst_items = productosDAO.getItemsByProductId(procparam.getId_producto());
			ProductoEntity prod = null;
			for(int i=0; i>lst_items.size(); i++){
				prod = null;
				prod = (ProductoEntity) lst_items.get(i);
				productosDAO.eliminaItemProducto(prod);
				//agregar la accion en el log de item
				ProductoLogDTO log = new ProductoLogDTO(); 
				log.setCod_prod(procparam.getId_producto());
				log.setFec_crea(Formatos.getFecHoraActual());
				log.setUsuario(procparam.getUsr_login());
				log.setTexto(procparam.getMen_elim());
				procparam.setLog(log);
				ProductoLogDTO log1 = procparam.getLog();
				log1.setId(prod.getId().longValue());
				productosDAO.agregaLogProducto(log);
			}
			//ahora elimina el producto generico
			elimina = productosDAO.eliminaProducto(procparam);
			//ingresar en el log

			
		}catch(ProductosDAOException ex){
			logger.debug("Problema setDelGenericProduct:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("elimina?"+elimina);	
		return elimina;
	}	
	
	/**
	 * Actualiza información de un producto.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  procparam ProcModProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se actualizó con éxito, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public boolean setModProduct(ProcModProductDTO procparam)throws ProductosException, SystemException{
		boolean actualizo = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
				
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción",e1);
		}
		
		// Marcamos los dao's con la transacción
		try {
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		
		try{
			logger.debug("en setModProduct");

			
			//verifica si existe el producto
			if(!productosDAO.isProductById(procparam.getId_producto())){
				trx1.rollback();
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE);
			}
			
			if(procparam.getMarca()!=null)
				//verifica si existe el id_marca
				if(!productosDAO.isMarcaById(Long.parseLong(procparam.getMarca()))){
					trx1.rollback();
					throw new ProductosException( Constantes._EX_PROD_ID_MAR_NO_EXISTE);
				}
			
			if(procparam.getUmedida()!=null)
				//verifica si existe el id_unidad_medida
				if(!productosDAO.isUni_MedById(Long.parseLong(procparam.getUmedida()))){
					trx1.rollback();
					throw new ProductosException( Constantes._EX_PROD_ID_UME_NO_EXISTE);
				}
			
			actualizo = productosDAO.actualizaProductosById(procparam);
			//agregar en el log
			if(actualizo){
				//agregar al log
				ProductoLogDTO log = new ProductoLogDTO(); 
				log.setCod_prod(procparam.getId_producto());
				log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
				log.setFec_crea(Formatos.getFecHoraActual());
				log.setUsuario(procparam.getUsr_login());
				log.setTexto(procparam.getMensaje());
				int resLog = productosDAO.agregaLogProducto(log);
			}
		}catch(ProductosDAOException ex){
			logger.error(ex);
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error(e1);
					throw new SystemException("Error al hacer rollback");
				}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		} catch (DAOException e) {
			logger.error(e);
			throw new SystemException("Error al hacer rollback");
		}
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error(e);
			throw new SystemException("Error al finalizar transacción");
		}
	
		return actualizo;
	}
	
	/**
	 * Actualiza información del producto sugerido de un producto, agregar o eliminar.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  proc ProcModSugProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se actualizó con éxito, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public boolean setModSugProduct(ProcModSugProductDTO proc)throws ProductosException, SystemException{
		boolean cambio = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
				
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción",e1);
		}
		
		// Marcamos los dao's con la transacción
		try {
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		
		try{
			logger.debug("en setModSugProduct");

		
				//verifica si prod y prod_sug son diferentes
				if(proc.getId_producto()==proc.getId_producto_sug()){
					trx1.rollback();
					throw new ProductosException(Constantes._EX_PROD_SUG_IGUAL_PROD);
				}
					//si accion es agregar sugeridos al producto:
					if (proc.getAction().equals("agregar")){
//						verifica si existe producto sugerido
				    	boolean existeSug = productosDAO.isProductById(proc.getId_producto_sug());
				    	if(!existeSug) {
				    		trx1.rollback();
				    		throw new ProductosException(Constantes._EX_PROD_SUG_NO_EXISTE);
				    	}
				    	//verifica si prod sugerido ya existe como producto sugerido para producto
						if(productosDAO.isSugAsocProd(proc.getId_producto(), proc.getId_producto_sug())){
							trx1.rollback();
							throw new ProductosException(Constantes._EX_PROD_SUG_EXISTE);
						}
						//ver si el producto es generico o es producto
						ProductoEntity prodItem = productosDAO.getProductoById(proc.getId_producto_sug());
						if( prodItem.getGenerico().equals("G")){
							trx1.rollback();
							throw new ProductosException(Constantes._EX_PROD_SUG_GENERICO);
						}
						ProductoSugerDTO suger = new ProductoSugerDTO();
						suger.setId_suger(proc.getId_producto_sug());	// colocar id del prod. seleccionado
						suger.setId_base(proc.getId_producto());			//colocar id del prod. generico
						suger.setFec_crea(Formatos.getFecHoraActual());				//colocar fecha y hora actual
						//suger.setEstado("A");						//estado Activado
						suger.setFormato(proc.getDireccion());				//tipo de relacion: U unico, B bidireccional
						boolean resb = productosDAO.agregaSugeridoProducto(suger);
						logger.debug("agregar:"+resb);
						
						if(resb){
							// agregar la accion en el log del producto
						    ProductoLogDTO log = new ProductoLogDTO();
						    log.setCod_prod(proc.getId_producto());
						    log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
						    log.setFec_crea(Formatos.getFecHoraActual());
						    log.setUsuario(proc.getUsr_login());
						    log.setTexto(prodItem.getDesc_corta()+":"+proc.getMns_agre());
						    int resLog = productosDAO.agregaLogProducto(log);
						    logger.debug("se guardo log con id:"+resLog);
						    //si el formato es B, colocar el log en el producto sugerido
						    if(proc.getDireccion().equals("B")){
								// agregar la accion en el log del producto
							    log = new ProductoLogDTO();
							    log.setCod_prod(proc.getId_producto_sug());
							    log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
							    log.setFec_crea(Formatos.getFecHoraActual());
							    log.setUsuario(proc.getUsr_login());
							    log.setTexto(productosDAO.getProductoById(proc.getId_producto()).getDesc_corta()+":"+
							    		proc.getMns_agre());
							    resLog = productosDAO.agregaLogProducto(log);
							    logger.debug("se guardo log con id:"+resLog);
						    }
						}
					} else
						//si accion = desasociar: desasociar un item
						if (proc.getAction().equals("desasociar")){
							
					    	//verifica si existe producto sugerido
					    	boolean existeSug = productosDAO.isProductById(proc.getId_sug());
					    	if(!existeSug) {
					    		trx1.rollback();
					    		throw new ProductosException(Constantes._EX_PROD_SUG_NO_EXISTE);
					    	}

							boolean resb = productosDAO.eliminaSugeridoProducto(proc.getId_producto_sug());
							logger.debug("desasociar:"+resb);
							
							if(resb){
								// agregar la accion en el log del producto
							    ProductoLogDTO log = new ProductoLogDTO();
							    log.setCod_prod(proc.getId_producto());
							    log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
							    log.setFec_crea(Formatos.getFecHoraActual());
							    log.setUsuario(proc.getUsr_login());
							    log.setTexto(productosDAO.getProductoById(proc.getId_sug()).getDesc_corta()+":"+
							    		proc.getMns_desa());
							    int resLog = productosDAO.agregaLogProducto(log);
							    logger.debug("se guardo log con id:"+resLog);
							    //si formato es bidireccional, eliminar el otro sugerido
								boolean elim_bid = productosDAO.eliminaSugerProdBidirec(proc.getId_producto(),proc.getId_sug());
								logger.debug("elim_bid:"+elim_bid);
								//agregar la accion en el log del producto sugerido
								ProductoLogDTO logSug = new ProductoLogDTO();
								logSug.setCod_prod(proc.getId_sug());
								logSug.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
								logSug.setFec_crea(Formatos.getFecHoraActual());
								logSug.setUsuario(proc.getUsr_login());
								logSug.setTexto(productosDAO.getProductoById(proc.getId_producto()).getDesc_corta()+":"+
							    		proc.getMns_desa());
							    int resLogSug = productosDAO.agregaLogProducto(logSug);
							    logger.debug("se guardo log con id:"+resLogSug);
							    
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
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
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
		
	    return cambio;
	}
	
	/**
	 * Agregar un producto web.
	 * 
	 * @param  procparam ProcAddProductDTO 
	 * @return int, nuevo id 
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public int setAddProduct(ProcAddProductDTO procparam)throws ProductosException, SystemException{
		int res = -1;
		try{
			logger.debug("en setAddProduct");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			res = productosDAO.agregaProducto(procparam);
		}catch(ProductosDAOException ex){
			logger.debug("Problema setAddProduct:"+ex);
			throw new SystemException(ex);
		}
		logger.debug("res?"+res);	
		
	    return res;
	}
	
	public int setAddProductBolsa(ProcAddProductDTO procparam)throws ProductosException, SystemException{
		int res = -1;
		try{
			logger.debug("en setAddProduct");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			res = productosDAO.agregaProductoBolsa(procparam);
		}catch(ProductosDAOException ex){
			logger.debug("Problema setAddProduct:"+ex);
			throw new SystemException(ex);
		}
		logger.debug("res?"+res);	
		
	    return res;
	}
	
	/**
	 * Agregar el log del producto
	 * 
	 * @param  log ProductoLogDTO 
	 * @return int, nuevo id 
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public int setLogProduct(ProductoLogDTO log)throws ProductosException{
		int res = -1;
		try{
			logger.debug("en setLogProduct");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			res = productosDAO.agregaLogProducto(log);
		}catch(ProductosDAOException ex){
			logger.debug("Problema setLogProduct:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("res?"+res);	
		
	    return res;
	}
	
	/**
	 * Publica o despublica el producto.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  proc ProcPubDespProductDTO 
	 * @return boolean, devuelve <i>true</i> si existe se actualizó con éxito, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public boolean setPubDespProduct(ProcPubDespProductDTO proc)throws ProductosException, SystemException{
		boolean result = false;		
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		

//		 Creamos trx
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		
		
		try{
			logger.debug("en setPubDespProduct");
			
			//verifica si existe el producto
			if(!productosDAO.isProductById(proc.getId_producto())){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_PROD_ID_NO_EXISTE);
			}
			//verifica si accion es publicar y no tiene categoria -> lanzar exception
			if( (proc.getAction()==1) &&  !(productosDAO.getCategoriasByProductId(proc.getId_producto()).size()>0)){
				trx1.rollback();
				throw new ProductosException(Constantes._EX_PROD_CAT_NO_TIENE);
			}
			
			result = productosDAO.setPubDespProduct(proc);
			if(result){
				
				ProductoLogDTO log = new ProductoLogDTO(); 
				log.setCod_prod(proc.getId_producto());
				log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
				log.setFec_crea(Formatos.getFecHoraActual());
				log.setUsuario(proc.getUsr_login());
				//accion 1 : Publicar
				if(proc.getAction()==1) log.setTexto(proc.getMensPubl());
				//accion 2 : DesPublicar
				else if (proc.getAction()==2)  log.setTexto(proc.getMensDesp());
				int resLog = productosDAO.agregaLogProducto(log);
				logger.debug("se guardo log con id:"+resLog);
				
				if((proc.getGenerico().equals("P")) && (proc.getAction()==2) ){
					//es un producto item y se despublico
					//obtener prod generico donde el item despublicado fuera el unico item 
					//List lst_prod = biz.getProductGenericos(paramId_producto);
					//en el caso q exista, enviar mensaje. "Los siguientes productos genéricos procederan a ser despublicados"
					//con botones Aceptar y Cancelar
					//en caso q acepte, despublicar para cada producto generico:
					/*
					for (int i = 0; i < lst_prod.size(); i++) {
						ProductosDTO pro = (ProductosDTO) lst_prod.get(i); 
						ProcPubDespProductDTO paramPadre = new ProcPubDespProductDTO (pro.getId(), paramAction, paramGenerico);
						productosDAO.agregaLogProducto(paramPadre);
					}
					*/
				}
		
				
				if(proc.getAction()==2){			
					ProductoLogDTO log1 = new ProductoLogDTO();
					log1.setCod_prod(proc.getId_producto());
					log1.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
					log1.setFec_crea(Formatos.getFecHoraActual());
					log1.setUsuario(proc.getUsr_login());
					log1.setTexto("El motivo de Despublicación es: "+ proc.getMotivo());
					productosDAO.agregaLogProducto(log1);
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
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
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
		
		logger.debug("result?"+result);	
	    return result;
	}
	
	
	/**
	 * Obtiene el listado de productos genéricos
	 * 
	 * @param  cod_prod long 
	 * @return List ProductosDTO
	 * @throws ProductosException
	 */
	public List getProductGenericos(long cod_prod)throws ProductosException{
		List result = new ArrayList();
		try{
			logger.debug("en getProductGenericos");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaPro = new ArrayList();
			listaPro = (List) productosDAO.getProductGenericos(cod_prod);
			ProductoEntity prod = null;
			for (int i = 0; i < listaPro.size(); i++) {
				prod = null;
				prod = (ProductoEntity) listaPro.get(i); 
				ProductosDTO prodDto = new ProductosDTO();
				prodDto.setId(prod.getId().longValue());
				prodDto.setDesc_corta(prod.getDesc_corta());
				result.add(prodDto);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema getProductGenericos:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
	    return result;
	}
	
	/**
	 * Obtiene el listado de eventos del producto
	 * 
	 * @param  cod_prod long 
	 * @return List ProductoLogDTO
	 * @throws ProductosException
	 */
	public List getLogByProductId(long cod_prod) throws ProductosException{
		List logs = new ArrayList();
		try{
			logger.debug("en getLogByProductId");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaLog= new ArrayList();
			listaLog = (List) productosDAO.getLogByProductId(cod_prod);
			ProductoLogEntity log = null;
			for (int i = 0; i < listaLog.size(); i++) {
				log = null;
				log = (ProductoLogEntity) listaLog.get(i);
				String fecha = "";
				if(log.getFec_crea()!=null)
					fecha = log.getFec_crea().toString();
				ProductoLogDTO logdto = new ProductoLogDTO(log.getId().longValue(), log.getCod_prod().longValue(),
						fecha, log.getUsuario(), log.getTexto());
				logs.add(logdto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getLogByProductId:"+ex);
			throw new ProductosException(ex);
		}
		return logs;
	}
	
	/**
	 * Obtiene el listado de marcas
	 * 
	 * @return List MarcasDTO
	 * @throws ProductosException
	 */
	public List getMarcas() throws ProductosException{
		List marcas = new ArrayList();
		try{
			logger.debug("en getMarcas");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaMrc= new ArrayList();
			listaMrc = (List) productosDAO.getMarcas();
			MarcaEntity mrc = null;
			for (int i = 0; i < listaMrc.size(); i++) {
				mrc = null;
				mrc = (MarcaEntity) listaMrc.get(i);
				MarcasDTO mrcdto = new MarcasDTO(mrc.getId().longValue(), mrc.getNombre(), mrc.getEstado());
				marcas.add(mrcdto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getMarcas:"+ex);
			throw new ProductosException(ex);
		}
		return marcas;
	}
	
	/**
	 * Obtiene el listado de marcas
	 * 
	 * @param  dto MarcasCriteriaDTO 
	 * @return List MarcasDTO
	 * @throws ProductosException
	 */
	public List getMarcas(MarcasCriteriaDTO dto) throws ProductosException{
		List marcas = new ArrayList();
		try{
			logger.debug("en getMarcas");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaMrc= new ArrayList();
			listaMrc = (List) productosDAO.getMarcas(dto);
			MarcaEntity mrc = null;
			for (int i = 0; i < listaMrc.size(); i++) {
				mrc = null;
				mrc = (MarcaEntity) listaMrc.get(i);
				MarcasDTO mrcdto = new MarcasDTO(mrc.getId().longValue(), mrc.getNombre(), mrc.getEstado());
				mrcdto.setCant_prods(mrc.getCant_prods());
				marcas.add(mrcdto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getMarcas:"+ex);
			throw new ProductosException(ex);
		}
		return marcas;
	}

	/**
	 * Obtiene la cantidad total de marcas
	 * 
	 * @return int cantidad
	 * @throws ProductosException
	 */
	public int getMarcasAllCount() throws ProductosException{
		int cantidad = 0;
		try{
			logger.debug("en getMarcasAllCount");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			cantidad = productosDAO.getMarcasAllCount();
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getMarcasAllCount:"+ex);
			throw new ProductosException(ex);
		}
		return cantidad;
	}
	
	
	/**
	 * Obtiene una marca especifica, segun id
	 * 
	 * @param  codMarca int 
	 * @return MarcasDTO
	 * @throws ProductosException
	 */
	public MarcasDTO getMarcaById(int codMarca) throws ProductosException{
		MarcasDTO mrcdto = new MarcasDTO();
		try{
			logger.debug("en getMarcaById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			MarcaEntity mrc = productosDAO.getMarcaById(codMarca);
			mrcdto = new MarcasDTO(mrc.getId().longValue(), mrc.getNombre(), mrc.getEstado());
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getMarcaById:"+ex);
			throw new ProductosException(ex);
		}
		return mrcdto;
	}
	
	/**
	 * Agrega una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosException
	 */
	public boolean addMarca(MarcasDTO prm) throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en addMarca");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.addMarca(prm);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema addMarca:"+ex);
			throw new ProductosException(ex);
		}
		return result;
	}
	
	/**
	 * Actualizar información de Marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosException
	 */
	public boolean modMarca(MarcasDTO prm) throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en modMarca");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.modMarca(prm);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema modMarca:"+ex);
			throw new ProductosException(ex);
		}
		return result;
	}
	
	/**
	 * Elimina una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosException
	 */
	public boolean setDelMarca(MarcasDTO prm) throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en setDelMarca");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.setDelMarca(prm);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema setDelMarca:"+ex);
			throw new ProductosException(ex);
		}
		return result;
	}
	
	/**
	 * Verifica si el producto existe, segun id
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si el producto existe, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 */
	public boolean isProductById(long cod_prod)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en isProductById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.isProductById(cod_prod);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema isProductById:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
	    return result;
	}
	
	/**
	 * Verifica si la marca existe, segun id
	 * 
	 * @param  id_marca long 
	 * @return boolean, devuelve <i>true</i> si la marca existe, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 */
	public boolean isMarcaById(long id_marca)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en isMarcaById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.isMarcaById(id_marca);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema isMarcaById:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
	    return result;
	}
	
	/**
	 * Verifica si la unidad de medida existe, segun id
	 * 
	 * @param  id_ume long 
	 * @return boolean, devuelve <i>true</i> si la unidad de medida existe, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 */
	public boolean isUni_MedById(long id_ume)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en isUni_MedById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.isUni_MedById(id_ume);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema isUni_MedById:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
	    return result;
	}


	/**
	 * Obtiene el listado de motivos de despublicación 
	 * 
	 * @return List  MotivosDespDTO
	 * @throws ProductosException
	 */
	public List getMotivosDesp() throws ProductosException{
		List motDesp = new ArrayList();
		try{
			logger.debug("en getMotivosDesp");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List listaMtv= new ArrayList();
			listaMtv = (List) productosDAO.getMotivoDesp();
			MotivosDespEntity mtv = null;
			for (int i = 0; i < listaMtv.size(); i++) {
				mtv = null;
				mtv = (MotivosDespEntity) listaMtv.get(i);
				MotivosDespDTO mtvdto = new MotivosDespDTO();//mrc.getId().longValue(), mrc.getNombre(), mrc.getEstado());
				mtvdto.setId(mtv.getId_desp());
				mtvdto.setMotivo(mtv.getMotivo());
				mtvdto.setEstado(mtv.getEstado());
				logger.debug("id: "+ mtvdto.getId() + " estado: "+mtvdto.getEstado() + "motivo: "+mtvdto.getMotivo());
				motDesp.add(mtvdto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getMotivosDesp:"+ex);
			throw new ProductosException(ex);
		}
		return motDesp;
	}

	/**
	 * Obtiene el precio de un producto segun su id y id de local
	 * @param id_prod
	 * @param id_loc
	 * @return
	 * @throws ProductosException
	 */
	public double getProductosPrecios(long id_prod, long id_loc) throws ProductosException{
		double valor = 0;
		try{
			logger.debug("en getProductosPrecios");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			valor = productosDAO.getProductosPrecios(id_prod,id_loc);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getProductosPrecios:"+ex);
			throw new ProductosException(ex);
		}
		return valor;
	}
	
	/**
	 * Obtiene una lista de productos con sus codigos de barra
	 * @param criteria
	 * @return List
	 * @throws ProductosException
	 */
	public List getProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosException{
		List lista = null;
		JdbcProductosDAO dao = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
		try{
		lista = dao.getProdCbarraByCriteria(criteria);
		
		}catch (ProductosDAOException ex){
			logger.debug("Problema :" + ex);
			throw new ProductosException(ex); 
		}
		return lista;
	}

	/**
	 * Obtiene la cantidad de productos existentes
	 * @param criteria
	 * @return
	 * @throws ProductosException
	 */
	public int getCountProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosException{
		int result=0;
		try{
			logger.debug("en getCountProdCbarraByCriteria");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.getCountProdCbarraByCriteria(criteria);
		}catch(ProductosDAOException ex){
			logger.debug("Problema getCountProdCbarraByCriteria:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws ProductosException
	 */
	public List getTiposProductosPorCod(String codDesc) throws ProductosException{
		List result=null;
		try{
			logger.debug("en getCountProdCbarraByCriteria");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.getTiposProductosPorCod(codDesc);
		}catch(ProductosDAOException ex){
			logger.debug("Problema getCountProdCbarraByCriteria:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws ProductosException
	 */
	public List getTiposProductosPorDesc(String codDesc) throws ProductosException{
		List result=null;
		try{
			logger.debug("en getCountProdCbarraByCriteria");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.getTiposProductosPorDesc(codDesc);
		}catch(ProductosDAOException ex){
			logger.debug("Problema getCountProdCbarraByCriteria:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene la información de la ficha del producto
	 * @param codProd
	 * @return
	 * @throws ProductosException
	 */
	public List getFichaProductoPorId(long codProd) throws ProductosException {
		List result = new ArrayList();
		try{
			logger.debug("en getFichaProductoPorId");
			JdbcProductosDAO datosFichaDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List lstDatosFicha = new ArrayList();
			lstDatosFicha = datosFichaDAO.getFichaProductoPorId(codProd);
			FichaProductoEntity datosFichaEntity = null;
			
			for(int i=0;i<lstDatosFicha.size();i++){
				datosFichaEntity = null;
				datosFichaEntity = (FichaProductoEntity)lstDatosFicha.get(i);
				FichaProductoDTO dto = new FichaProductoDTO(datosFichaEntity.getPftProId(), datosFichaEntity.getPftPfiItem(), datosFichaEntity.getPftPfiSecuencia(), datosFichaEntity.getPftDescripcionItem(), datosFichaEntity.getPftEstadoItem());
								
				result.add(dto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getFichaProductoPorId:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws ProductosException
	 */
	public List geItemFichaProductoAll() throws ProductosException {
		List result = new ArrayList();
		try{
			logger.debug("en geItemFichaProductoAll");
			JdbcProductosDAO datosFichaDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List lstDatosFicha = new ArrayList();
			lstDatosFicha = datosFichaDAO.getItemFichaProductoAll();
			ItemFichaProductoDTO itemFichaDto = null;
			
			for(int i=0;i<lstDatosFicha.size();i++){				
				itemFichaDto = (ItemFichaProductoDTO)lstDatosFicha.get(i);
				if (itemFichaDto != null) {
					result.add(itemFichaDto);
				}							
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema geItemFichaProductoAll:"+ex);
			throw new ProductosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene el item de la ficha tecnica según id	
	 * @param idItem
	 * @return
	 * @throws ProductosException
	 */
	public ItemFichaProductoDTO getItemFichaProductoById(long idItem)  throws ProductosException{
		ItemFichaProductoDTO result=null;
		try{
			logger.debug("en getPerfilById");
			JdbcProductosDAO perfilesDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = perfilesDAO.getItemFichaProductoById(idItem);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new ProductosException(ex);
		}
		return result;
	}
		
	/**
	 * Actualiza información de una ficha tecnica de un producto.
	 * @param procparam
	 * @return
	 * @throws ProductosException
	 * @throws SystemException
	 */
	public boolean setModFichaTecnica(ProcModFichaTecnicaDTO procparam, boolean cambioEstadoFicha, String valorItem, boolean actualizaLog)throws ProductosException, SystemException, DAOException{
		boolean actualizo = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
		//		 Creamos trx
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		try{
			logger.debug("en setModFichaTecnica");
					
			long idItem;
			
			logger.debug("Paso 1 getCodItemFichaProducto (" + valorItem +")");
			if (procparam.getPftPfiItem() == -1) {
				//Guardo nuevo item
				idItem = productosDAO.getCodItemFichaProducto(valorItem);
				procparam.setPftPfiItem(idItem);
				logger.debug("setModFichaTecnica idItem : " + idItem);
			}

			logger.debug("Paso 2 actualizaFichaProductoById (" + procparam.toString() +")");
			actualizo = productosDAO.actualizaFichaProductoById(procparam);	
																
			//agregar en el log
			if(actualizo && actualizaLog){
				//agregar al log
				ProductoLogDTO log = new ProductoLogDTO(); 
				log.setCod_prod(procparam.getPftProId());
				log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
				log.setFec_crea(Formatos.getFecHoraActual());
				log.setUsuario(procparam.getUsr_login());
				log.setTexto(procparam.getMensaje());
				int resLog = productosDAO.agregaLogProducto(log);
				
				logger.debug("Paso 3 agregaLogProducto (" + log.toString() +")");
				
//				if (cambioEstadoFicha) {															
//					//agregar al log
//					ProductoLogDTO logEstadoFicha = new ProductoLogDTO(); 
//					logEstadoFicha.setCod_prod(procparam.getPftProId());
//					logEstadoFicha.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
//					logEstadoFicha.setFec_crea(Formatos.getFecHoraActual());
//					logEstadoFicha.setUsuario(procparam.getUsr_login());
//					logEstadoFicha.setTexto("");
//					int resLogEstadoFicha = productosDAO.agregaLogProducto(logEstadoFicha);
//				}
			}
		}catch(ProductosDAOException ex){
			logger.error(ex);
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error(e1);
					throw new SystemException("Error al hacer rollback");
				}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		} catch(Exception ex){
			logger.error(ex);
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error(e1);
					throw new SystemException("Error al hacer rollback");
				}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		}
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error(e);
			throw new SystemException("Error al finalizar transacción");
		}

		return actualizo;
	}
	
	/**
	 * Verifica si existe ficha producto, segun id
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si la ficha existe, caso contrario, devuelve <i>false</i>. 
	 * @throws ProductosException
	 */
	public boolean tieneFichaProductoById(long cod_prod)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en tieneFichaProductoById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.tieneFichaProductoById(cod_prod);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema tieneFichaProductoById:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
	    return result;
	}

	/**
	 * Actualiza estado ficha tecnica del producto
	 * @param prodId
	 * @param estadoFichaTecnica
	 * @return
	 * @throws ProductosException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public boolean actualizaEstadoFichaTecnica(long prodId, long estadoFichaTecnica, String usrLogin, String mensaje)throws ProductosException, SystemException, DAOException{
		boolean actualizo = false;
		
		JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
		
		//		 Creamos trx
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
			productosDAO.setTrx(trx1);
		} catch (ProductosDAOException e2) {
			logger.error("Error al asignar transacción al dao Productos");
			throw new SystemException("Error al asignar transacción al dao Productos");
		}
		try{
			logger.debug("en setModFichaTecnica");
			
			boolean existeRegistro = productosDAO.existeRegistroFichaTecnica(prodId);
			
			logger.debug("actualizaEstadoFichaTecnica existeRegistro: " + existeRegistro);
			
			actualizo = productosDAO.actualizaEstadoFichaTecnica(prodId, estadoFichaTecnica, existeRegistro);
			//agregar en el log
			if(actualizo){
				//agregar al log
				ProductoLogDTO log = new ProductoLogDTO(); 
				log.setCod_prod(prodId);
				log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
				log.setFec_crea(Formatos.getFecHoraActual());
				log.setUsuario(usrLogin);
				log.setTexto(mensaje);
				int resLog = productosDAO.agregaLogProducto(log);
			}
		}catch(ProductosDAOException ex){
			logger.error(ex);
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error(e1);
					throw new SystemException("Error al hacer rollback");
				}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		} catch(Exception ex){
			logger.error(ex);
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error(e1);
					throw new SystemException("Error al hacer rollback");
				}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new ProductosException( Constantes._EX_PROD_ID_NO_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		}
		
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error(e);
			throw new SystemException("Error al finalizar transacción");
		}

		return actualizo;
	}
	
	public boolean eliminaFichaProductoById(long prodId)throws ProductosException{
		boolean result = false;
		try{
			logger.debug("en eliminaFichaProductoById");
			JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			result = productosDAO.eliminaFichaProductoById(prodId);
		}catch(ProductosDAOException ex){
			logger.debug("Problema eliminaFichaProductoById:"+ex);
			throw new ProductosException(ex);
		}
		logger.debug("result?"+result);	
		return result;
	}
	
	public boolean guardarLeySuperOcho(cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO dto)throws ProductosException{
		boolean result = false;
		try{
			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			result = productosDAO.guardarLeySuperOcho(dto);
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [guardarLeySuperOcho], "+ex);
			throw new ProductosException(ex);
		}		
		return result;
	}
	public List listaLeySuperOcho(int idproductoFO)  throws ProductosException{
		try{
			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			return productosDAO.listaLeySuperOcho(idproductoFO);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [listaLeySuperOcho]:"+ex);
			throw new ProductosException(ex);
		}
	}
	public List listaFichaNutricional(int idproductoFO)  throws ProductosException{
		try{
			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			return productosDAO.listaFichaNutricional(idproductoFO);
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [listaFichaNutricional]:"+ex);
			throw new ProductosException(ex);
		}
	}
	public boolean guardarFichaNutricional(Map mapa, int idProductoFO)throws ProductosException{
		boolean result = false;
		try{

			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			String cabecera = "";
			
			if(mapa.size()>0){
				Iterator ite = mapa.entrySet().iterator();
				while (ite.hasNext()){
					Map.Entry entry = (Map.Entry) ite.next();
					
					FichaNutricionalDTO dto = (FichaNutricionalDTO)entry.getValue();
					cabecera = dto.getCabecera();
					break;
				}

			}	
			
			result = productosDAO.guardarCabeceraFichaNutricional(cabecera, idProductoFO);
			if(result){
				result = productosDAO.guardarFichaNutricional(mapa, idProductoFO);
			}
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [guardarFichaNutricional], "+ex);
			throw new ProductosException(ex);
		}		
		return result;
	}
	
	public boolean guardarTipoFicha(List listTipoFicha, long idProductoFO)throws ProductosException{
		boolean result = false;
		try{
			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			result = productosDAO.guardarTipoFicha(listTipoFicha, idProductoFO);
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [guardarTipoFicha], "+ex);
			throw new ProductosException(ex);
		}		
		return result;
	}
	
	public List obtenerTipoFicha(long idProductoFO)throws ProductosException{
		List result = new ArrayList();
		try{
			JdbcProductosDAO productosDAO = (JdbcProductosDAO)DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			result = productosDAO.obtenerTipoFicha(idProductoFO);
		}catch(ProductosDAOException ex){
			logger.debug("Problema ProductosCtrl [obtenerTipoFicha], "+ex);
			throw new ProductosException(ex);
		}		
		return result;
	}
}