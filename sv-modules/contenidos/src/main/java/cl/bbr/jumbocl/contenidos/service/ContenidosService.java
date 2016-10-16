package cl.bbr.jumbocl.contenidos.service;

import java.util.LinkedHashMap;
import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelAllProductCategory;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModFichaTecnicaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModGenericItemDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModMPVProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSugProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.contenidos.ctr.CampanaCtrl;
import cl.bbr.jumbocl.contenidos.ctr.CategoriasCtrl;
import cl.bbr.jumbocl.contenidos.ctr.CategoriasSapCtrl;
import cl.bbr.jumbocl.contenidos.ctr.ElementoCtrl;
import cl.bbr.jumbocl.contenidos.ctr.EstadosCtrl;
import cl.bbr.jumbocl.contenidos.ctr.ProductosCtrl;
import cl.bbr.jumbocl.contenidos.ctr.ProductosSapCtrl;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoSugerDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CampanaException;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasException;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapException;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoException;
import cl.bbr.jumbocl.contenidos.exceptions.EstadosException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosSapException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Permite manejar información y operaciones sobre Productos Sap, Productos Web, Categorias Sap, Categorias Web. 
 * @author BBR
 *
 */
public class ContenidosService {
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	/*
	 * ----------------------- Estados -----------------------
	 */
	/**
	 * Obtiene el listado de estados, segun el tipo de estado.
	 * 
	 * @param  tipo String 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 * */
	public List getEstadosAll(String tipo) throws ServiceException {
		EstadosCtrl est = new EstadosCtrl(); 
		List result = null;
		try{
			result = est.getEstadosAll(tipo);
		}catch(EstadosException ex){
			logger.debug("Problemas con controles de estados");
			throw new ServiceException(ex);
		}
		return result; 
	}
	
	/**
	 * Obtiene el listado de estados, segun el tipo de estado y si es visible en Web.
	 * 
	 * @param  tipo String 
	 * @param  visible String 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 * */
	public List getEstadosByVis(String tipo, String visible) throws ServiceException {
		EstadosCtrl est = new EstadosCtrl(); 
		List result = null;
		try{
			result = est.getEstados(tipo, visible);
		}catch(EstadosException ex){
			logger.debug("Problemas con controles de estados");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/*
	 *  ------------------------------ Categorias ------------------------------
	 */
	/**
	 * Obtiene un listado de categorias dado el criterio de busqueda
	 * 
	 * @param  criterio CategoriasCriteriaDTO  
	 * @return List CategoriasDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriasDTO
	 */
	public List getCategoriasByCriteria(CategoriasCriteriaDTO criterio) throws ServiceException {
		CategoriasCtrl cats = new CategoriasCtrl();		
		List list = null;
		try{
			list = cats.getCategoriasByCriteria(criterio);
		}catch(CategoriasException ex){
			logger.debug("Problemas con controles de categorias");
			throw new ServiceException(ex);
		}
		return list; 
	}
	
	/**
	 * Muestra las subcategorias de una categoria
	 * 
	 * @param  criterio CategoriasCriteriaDTO   
	 * @param  cat_padre long
	 * @return List CategoriasDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriasDTO
	 */
	public List getCategoriasNavegacion(CategoriasCriteriaDTO criterio, long cat_padre)throws ServiceException{
		CategoriasCtrl cats = new CategoriasCtrl();
		try {
			return cats.getCategoriasNavegacion(criterio, cat_padre);
		} catch (CategoriasException ex) {
			logger.debug( "Problemas con getCategoriasNavegacion");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Entrega una categoria identificada por el id_categoria
	 * 
	 * @param  id_categoria long  
	 * @return CategoriasDTO
	 * @throws ServiceException
	 */
	public CategoriasDTO getCategoriasById(long id_categoria) throws ServiceException{
		CategoriasDTO cat = null;
		CategoriasCtrl cats = new CategoriasCtrl();
		try{
			cat = cats.getCategoriaById(id_categoria);
		}catch(CategoriasException ex){
			logger.debug( "Problemas con categoria by id");
			throw new ServiceException(ex);
		}
		return cat; 
	}
	
	 /** Listado de productos de una categoria.
	 * 
	 * @param  codCat long  
	 * @return List ProductoCategDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO
	 */
	public List getProductosByCategId(long codCat) throws ServiceException{
		CategoriasCtrl cats = new CategoriasCtrl();
		try{
			return cats.getProductosByCategId(codCat);
		}catch (CategoriasException ex) { //RemoteException ex
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
		}
	}
	
	 /** Listado de Tipos de las categorias presentadas en base 
	 * a un criterio de busqueda.
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 */
	public List getTiposCategorias() throws ServiceException{
		CategoriasCtrl cats = new CategoriasCtrl();
		try{
			return cats.getTiposCategorias();
		}catch (CategoriasException ex) { //RemoteException ex
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
		}
		
	}
	
	/** Listado de Estados de las categorias presentadas en base 
	 * a un criterio de busqueda.
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 */
	public List getEstadosCategorias() throws ServiceException{
		CategoriasCtrl cats = new CategoriasCtrl();
		try{
			return cats.getEstadosCategorias();
		}catch (CategoriasException ex) { //RemoteException ex
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
		}
		
	}

	/**
	 * Entrega el total de registros de categorias existentes para
	 * un criterio de busqueda.
	 * 
	 * @param  criterio CategoriasCriteriaDTO  
	 * @return int
	 * @throws ServiceException
	 */
	public int getCategoriasCountByCriteria(CategoriasCriteriaDTO criterio)throws ServiceException{
		CategoriasCtrl cats = new CategoriasCtrl();
		int numCat = 0;
		try{
			numCat = cats.getCategoriasCountByCriteria(criterio);
		}catch(CategoriasException ex){
			logger.debug( "Problemas con categorias count");
			throw new ServiceException(ex);
		}
		return numCat;
	}
	/**
	 * Agrega una categoria Web
	 * 
	 * @param  procparam ProcAddCatWebDTO  
	 * @return long, nuevo id
	 * @throws ServiceException
	 */
	public long setAddCatWeb(ProcAddCatWebDTO procparam) throws ServiceException {
	    CategoriasCtrl categorias = new CategoriasCtrl();
	    long ret;
	    try{
	        ret = categorias.setAddCatWeb(procparam);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
	    }
	    return ret;
	}

	/**
	 * Elimina una categoría Web
	 * 
	 * @param  procparam ProcDelCatWebDTO  
	 * @return boolean, devuelve <i>true</i> si se elimino con exito, caso contrario devuelve <i>false</i>
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public boolean setDelCatWeb(ProcDelCatWebDTO procparam) throws ServiceException, SystemException {
	    CategoriasCtrl categorias = new CategoriasCtrl();
	    try{
	    	return categorias.setDelCatWeb(procparam);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Actualiza una categoría Web
	 * 
	 * @param  procparam ProcModCatWebDTO  
	 * @throws ServiceException 
	 */
	public void setModCatWeb(ProcModCatWebDTO procparam) throws ServiceException {
	    CategoriasCtrl categorias = new CategoriasCtrl();
	    try{
	        categorias.setModCatWeb(procparam);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Obtiene el listado de categorias relacionadas a una categoria
	 * 
	 * @param  codCat long 
	 * @return List CategoriasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriasDTO
	 */
	public List getCategoriasByCategId(long codCat) throws  ServiceException {
	    CategoriasCtrl categorias = new CategoriasCtrl();
	    try{
	        return categorias.getCategoriasByCategId(codCat);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Asigna una subcategoria a una categoria
	 * 
	 * @param  procparam ProcModSubCatWebDTO  
	 * @return boolean, devuelve <i>true</i> si se asignó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setModSubCatWeb(ProcModSubCatWebDTO procparam) throws ServiceException, SystemException {
	    CategoriasCtrl categorias = new CategoriasCtrl();
	    try{
	    	return categorias.setModSubCatWeb(procparam);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias ");
	        throw new ServiceException(ex.getMessage());
	    }
	    
	}
	
	/**
	 * Verifica si la categoria existe.
	 * 
	 * @param  id long  
	 * @return boolean, devuelve <i>true</i> si se la categoría existe, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean isCategoriaById(long id) throws ServiceException {
		CategoriasCtrl categorias = new CategoriasCtrl();
	    boolean result = false;
	    try{
	        result = categorias.isCategoriaById(id);
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return result;
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Productos Monitor de productos ve solo los del FO------------------------------
	 * *************************************************************************************************************
	 */	
	/**
	 * Devuelve el código sap de un producto generico 
	 * 
	 * @return long
	 * @throws ServiceException
	 */
	public long getCodSapGenerico() throws ServiceException {
		long res = 0;
		ProductosCtrl prods = new ProductosCtrl();	
		try{
			res = prods.getCodSapGenerico();
		}catch(ProductosException ex){
			logger.debug( "Problemas con getCodSapGenerico");
			throw new ServiceException(ex);
		}
		return res;	
	}

	/**
	 * Retorna un listado de productos en base al criterio
	 * Se obtiene la lista, segun los criterios de busqueda: cod. producto, cod. producto sap, estado, tipo, 
	 * cod. categoria (en el caso que la consulta sea desde el monitor de categorias)
	 * 
	 * @param  criterio ProductosCriteriaDTO   
	 * @return List ProductosDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductosDTO
	 */
	public List getProductosByCriteria(ProductosCriteriaDTO criterio)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		List result = null;
		try{
			result = prods.getProductosByCriteria(criterio); 
			
		}catch(ProductosException ex){
			logger.debug( "Problemas con getProductosByCriteria");
			throw new ServiceException(ex);
		}
		return result;	
	}
	
	/**
	 * Retorna la cantidad de productos en base al criterio
	 * 
	 * @param  criterio ProductosCriteriaDTO   
	 * @return int
	 * @throws ServiceException
	 */
	public int getProductosCountByCriteria(ProductosCriteriaDTO criterio)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		int cant = 0;
		try{
			cant = prods.getProductosCountByCriteria(criterio); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return cant;	
	}
	
	/**
	 * Obtiene la información del producto
	 * 
	 * @param  codProd long  
	 * @return ProductosDTO 
	 * @throws ServiceException
	 */
	public ProductosDTO getProductosById(long codProd)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		ProductosDTO prod = null;
		try{
			prod = prods.getProductosById(codProd); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return prod;	
	}

	/**
	 * Agrega la relación entre un item y un producto.
	 * 
	 * @param  proc ProcModGenericItemDTO   
	 * @return ProductosDTO 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean agregaItemProducto(ProcModGenericItemDTO proc)throws ServiceException,SystemException {	
		ProductosCtrl prods = new ProductosCtrl();
		try{
			return prods.agregaItemProducto(proc);
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex.getMessage());
		}
			
	}

	/**
	 * Elimina la relación entre un item y un producto.
	 * 
	 * @param  item ProductoEntity    
	 * @return boolean, devuelve <i>true</i> si se eliminó con exito, caso contrario devuelve <i>false</i> 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean eliminaItemProducto(ProductoEntity item)throws ServiceException,SystemException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.eliminaItemProducto(item); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return result;	
	}

	/**
	 * Agrega la relación entre un producto y un producto sugerido
	 * 
	 * @param  suger ProductoSugerDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó con exito, caso contrario devuelve <i>false</i> 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean agregaSugeridoProducto(ProductoSugerDTO suger) throws ServiceException, SystemException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.agregaSugeridoProducto(suger); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return result;	
	}

	/**
	 * Elimina la relación entre un producto y un producto sugerido
	 * 
	 * @param  id_suger long 
	 * @return boolean, devuelve <i>true</i> si se eliminó con exito, caso contrario devuelve <i>false</i> 
	 * @throws ServiceException
	 */
	public boolean eliminaSugeridoProducto(long id_suger) throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.eliminaSugeridoProducto(id_suger); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return result;	
	}
	
	/**
	 * Obtiene las categorias relacionadas al producto
	 * 
	 * @param  codProd long 
	 * @return List CategoriasDTO  
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriasDTO
	 */
	public List getCategoriasByProductId(long codProd) throws ServiceException{
		ProductosCtrl prods = new ProductosCtrl();
		try{
			return prods.getCategoriasByProductId(codProd);
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
		}
		
	}

	/** 
	 * Obtiene el listado de Tipos de las productos presentadas.
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 */
	public List getTiposProductos() throws ServiceException{
		ProductosCtrl prods = new ProductosCtrl();
		try{
			return prods.getTiposProductos();
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
		}
		
	}
	
	/** 
	 * Obtiene el listado de estados de las productos presentadas. 
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.EstadoDTO
	 */
	public List getEstadosProductos() throws ServiceException{
		ProductosCtrl prods = new ProductosCtrl();
		try{
			return prods.getEstadosProductos();
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
		}
		
	}
	
	/** 
	 * Obtiene el listado de unidades de medida. 
	 * 
	 * @return List UnidadMedidaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO
	 */
	public List getUnidMedida() throws ServiceException{
		ProductosCtrl prods = new ProductosCtrl();
		try{
			return prods.getUnidMedida();
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
		}
		
	}

	/** 
	 * Verifica si existe la relación entre una categoría y un producto. 
	 * 
	 * @param  id_cat long 
	 * @param  id_prod long 
	 * @return boolean, devuelve <i>true</i> si la relación existe, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean isCatAsocProd(long id_cat, long id_prod) throws ServiceException {
	    ProductosCtrl productos = new ProductosCtrl();
	    boolean result = false; 
	    try{
	        result = productos.isCatAsocProd(id_cat, id_prod);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return result;
	}

	/**
	 * Agrega la relación entre una categoría y un producto.
	 * 
	 * @param  procparam ProcAddCategoryProductDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó la relación, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setAddCategoryProduct(ProcAddCategoryProductDTO procparam) throws ServiceException,SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return  productos.setAddCategoryProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias");
	        throw new ServiceException(ex.getMessage());
	    }
	}
	/**
	 * Actualiza la relación entre una categoría y un producto.
	 * 
	 * @param  param ProductoCategDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó la relación, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setModCategoryProduct(ProductoCategDTO param) throws ServiceException,SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return  productos.setModCategoryProduct(param);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }
	}
	
	/**
	 * Elimina la relación entre una categoría y un producto.
	 * 
	 * @param  procparam ProcDelCategoryProductDTO 
	 * @return boolean, devuelve <i>true</i> si se agregó la relación, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setDelCategoryProduct(ProcDelCategoryProductDTO procparam) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setDelCategoryProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias");
	        throw new ServiceException(ex.getMessage());
	    }
	}

	//[20121120avc
	public boolean setDelAllProductCategory(ProcDelAllProductCategory proc) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setDelAllProductCategory(proc);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }catch(CategoriasException ex){
	        logger.debug(" Problemas con controles de Categorias");
	        throw new ServiceException(ex.getMessage());
	    }
	    
	}
	//]20121120avc

	/**
	 * Elimina un producto genérico.
	 * 
	 * @param  procparam ProcDelGenericProductDTO 
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setDelGenericProduct(ProcDelGenericProductDTO procparam) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setDelGenericProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	}

	
	/**
	 * Actualiza información de un producto.
	 * 
	 * @param  procparam ProcModProductDTO 
	 * @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setModProduct(ProcModProductDTO procparam) throws ServiceException,SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setModProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Actualiza información de un producto sugerido de otro producto, agrega o elimina producto sugerido.
	 * 
	 * @param  procparam ProcModSugProductDTO 
	 * @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setModSugProduct(ProcModSugProductDTO procparam) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setModSugProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }
	}

	/**
	 * Agrega un producto web.
	 * 
	 * @param  procparam ProcAddProductDTO 
	 * @return int, nuevo id del producto
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public int setAddProductBolsa(ProcAddProductDTO procparam) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    int ret=-1;
	    try{
	        ret = productos.setAddProductBolsa(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return ret;
	}
	
	public int setAddProduct(ProcAddProductDTO procparam) throws ServiceException, SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    int ret=-1;
	    try{
	        ret =productos.setAddProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return ret;
	}

	/**
	 * Agrega el log de un producto web.
	 * 
	 * @param  log ProductLogDTO 
	 * @return int, nuevo id del log del producto
	 * @throws ServiceException
	 */
	public int setLogProduct(ProductoLogDTO log) throws ServiceException {
	    ProductosCtrl productos = new ProductosCtrl();
	    int ret=-1;
	    try{
	        ret =productos.setLogProduct(log);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return ret;
	}
	
	/**
	 * Permite publicar o despublicar un producto.
	 * 
	 * @param  procparam ProcPubDespProductDTO 
	 * @return boolean, devuelve <i>true</i> si el proceso se realizó con éxito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setPubDespProduct(ProcPubDespProductDTO procparam) throws ServiceException,SystemException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setPubDespProduct(procparam);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex.getMessage());
	    }
	}
	
	/**
	 * Obtiene el listado de productos genéricos.
	 * 
	 * @param  cod_prod long 
	 * @return List ProductosDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductosDTO
	 */
	public List getProductGenericos(long cod_prod) throws ServiceException {
	    ProductosCtrl productos = new ProductosCtrl();
	    List prods = null;
	    try{
	        prods = productos.getProductGenericos(cod_prod);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return prods;
	}

	/**
	 * Obtiene el listado de losgs de un producto.
	 * 
	 * @param  cod_prod long 
	 * @return List ProductoLogDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO
	 */
	public List getLogByProductId(long cod_prod)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		List mrcs = null;
		try{
			mrcs = prods.getLogByProductId(cod_prod); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getLogByProductId");
			throw new ServiceException(ex);
		}
	
		return mrcs;	
	}
	
	/**
	 * Obtiene el listado de marcas.
	 * 
	 * @return List MarcasDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.MarcasDTO
	 */
	public List getMarcas()throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		List mrcs = null;
		try{
			mrcs = prods.getMarcas(); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getMarcas");
			throw new ServiceException(ex.getMessage());
		}
	
		return mrcs;	
	}

	/**
	 * Obtiene el listado de marcas.
	 * 
	 * @param  dto MarcasCriteriaDTO
	 * @return List MarcasDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.MarcasDTO
	 */
	public List getMarcas(MarcasCriteriaDTO dto)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		List mrcs = null;
		try{
			mrcs = prods.getMarcas(dto); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getMarcas");
			throw new ServiceException(ex.getMessage());
		}
	
		return mrcs;	
	}
	/**
	 * Obtiene la cantidad total de marcas.
	 * 
	 * @return int cantidad
	 * @throws ServiceException
	 * 
	 */
	public int getMarcasAllCount()throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		int res = 0;
		try{
			res = prods.getMarcasAllCount(); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getMarcas");
			throw new ServiceException(ex.getMessage());
		}
	
		return res;	
	}
	
	/**
	 * Obtiene información de una marca.
	 * 
	 * @param  codMrc int 
	 * @return MarcasDTO
	 * @throws ServiceException
	 */
	public MarcasDTO getMarcasById(int codMrc)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		MarcasDTO mrcdto = null;
		try{
			mrcdto = prods.getMarcaById(codMrc); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getMarcasById");
			throw new ServiceException(ex);
		}
	
		return mrcdto;	
	}
	
	/**
	 * Agrega una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean addMarca(MarcasDTO prm) throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.addMarca(prm); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con addMarca");
			throw new ServiceException(ex);
		}
	
		return result;	
	}
	
	/**
	 * Actualizar información de Marca
	 * 
	 * @param  prm MarcasDTO 
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean modMarca(MarcasDTO prm) throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.modMarca(prm); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con modMarca");
			throw new ServiceException(ex);
		}
	
		return result;	
	}
	
	/**
	 * Elimina una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean setDelMarca(MarcasDTO prm) throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.setDelMarca(prm); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con setDelMarca");
			throw new ServiceException(ex.getMessage());
		}
	
		return result;	
	}
	
	/**
	 * Obtiene el listado de los motivos de despublicación. 
	 * 
	 * @return List MotivosDespDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.MotivosDespDTO
	 */
	public List getMotivosDesp()throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		List mtvs = null;
		try{
			mtvs = prods.getMotivosDesp(); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getMotivoDesp");
			throw new ServiceException(ex);
		}
	
		return mtvs;	
	}

	/**
	 * Verifica si existe un producto o no. 
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si el producto existe, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean isProductById(long cod_prod) throws ServiceException {
	    ProductosCtrl productos = new ProductosCtrl();
	    boolean result = false;
	    try{
	        result = productos.isProductById(cod_prod);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return result;
	}
	
	/**
	 * Obtiene el precio de un producto segun su id y id de local
	 * @param id_prod
	 * @param id_local
	 * @return
	 * @throws ServiceException
	 */
	public double getProductosPrecios(long id_prod, long id_local)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		double valor = 0;
		try{
			valor = prods.getProductosPrecios(id_prod, id_local); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con getProductosPrecios");
			throw new ServiceException(ex.getMessage());
		}
	
		return valor;	
	}
	
	/*
	 * *************************************************************************************************************
	 * ------------------------------ProductosSap para el Monitor de MPV------------------------------ 
	 * *************************************************************************************************************
	 */
	/**
	 * Obtiene el listado de productos SAP segun los criterios de búsqueda.
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @return List ProductosSapDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO
	 */
	public List getProductosSapByCriteria(ProductosSapCriteriaDTO criterio)throws ServiceException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getProductosSapByCriteria(criterio);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con controles de ProductosSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene la cantidad de productos SAP segun los criterios de búsqueda.
	 * 
	 * @param  criterio ProductosSapCriteriaDTO 
	 * @return int
	 * @throws ServiceException
	 */
	public int getCountProdSapByCriteria(ProductosSapCriteriaDTO criterio)throws ServiceException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getCountProdSapByCriteria(criterio);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con controles de ProductosSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene un listado de Productos SAP en base al criterio y a un RANGO 
	 * de fechas en que el producto SAP fue cargado.
	 * 
	 * @param  criterio ProductosSapCriteriaDTO  
	 * @param  fecha_ini String 
	 * @param  fecha_fin String 
	 * @return List ProductosSAPDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO
	 */
	public List getProductosSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin)throws ServiceException {
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getProductosSapByCriteriaByDateRange(criterio, fecha_ini, fecha_fin);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con controles de ProductosSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene la cantidad de Productos SAP en base al criterio y a un RANGO 
	 * de fechas en que el producto SAP fue cargado.
	 * 
	 * @param  criterio ProductosSapCriteriaDTO  
	 * @param  fecha_ini String 
	 * @param  fecha_fin String 
	 * @return int
	 * @throws ServiceException
	 */
	public int getCountProdSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin)throws ServiceException {
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getCountProdSapByCriteriaByDateRange(criterio, fecha_ini, fecha_fin);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con controles de ProductosSap ");
	        throw new ServiceException(ex);
	    }
	}	
	
	/**
	 * Proceso de agregar un producto MIX y sacar un producto del MIX
	 * 
	 * @param  procparam ProcModMPVProductDTO 
	 * @return boolean, devuelve <i>true</i> si el proceso se realizó con exito, caso contrario devuelve <i>false</i>
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public boolean setModMPVProduct(ProcModMPVProductDTO procparam) throws ServiceException, SystemException {
	    ProductosSapCtrl productossap = new ProductosSapCtrl();
	    try {
	    	return productossap.setModMPVProduct(procparam);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con controles de ProductosSap ");
	        throw new ServiceException(ex.getMessage());
	    }
	}
	
	/**
	 * Obtiene el listado de categorias Sap, segun la categoría seleccionada.
	 * 
	 * @param  cod_cat String 
	 * @param  cod_cat_padre String 
	 * @return List CategoriaSapDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */
	public List getCategoriasSapById(String cod_cat, String cod_cat_padre) throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getCategoriasSapById(cod_cat, cod_cat_padre);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene el código de la categoría padre. 
	 * 
	 * @param  cod_cat String 
	 * @return String
	 * @throws ServiceException 
	 */
	public String getCodCatPadre(String cod_cat) throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getCodCatPadre(cod_cat);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Obtiene información del producto Sap.  
	 * 
	 * @param  codProd long 
	 * @return ProductosSapDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public ProductosSapDTO getProductosSapById(long codProd)throws ServiceException, SystemException {	
		ProductosSapCtrl prods = new ProductosSapCtrl();
		try{
			return prods.getProductosSapById(codProd); 
		}catch(ProductosSapException ex){
			logger.debug( "Problemas con getProductosSapById");
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de código de barras del producto Sap.  
	 * 
	 * @param  codProd long 
	 * @return List CodigosBarraSapDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.contenidos.dto.CodigosBarraSapDTO
	 */
	public List getCodBarrasByProdId(long codProd) throws ServiceException, SystemException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getCodBarrasByProdId(codProd);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con codigos de barra sap ");
	        throw new ServiceException(ex);
	    }
		
	}
	
	/**
	 * Obtiene el listado de precios del producto Sap.  
	 * 
	 * @param  codProd long 
	 * @return List PreciosSapDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.contenidos.dto.PreciosSapDTO
	 */
	public List getPreciosByProdId(long codProd) throws ServiceException, SystemException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
	    try{
	    	return prodsap.getPreciosByProdId(codProd);
	    }catch(ProductosSapException ex){
	        logger.debug(" Problemas con precios sap ");
	        throw new ServiceException(ex);
	    }
		
	}

	/**
	 * Obtiene información de la categoría Sap.  
	 * 
	 * @param  cod_cat String 
	 * @return String
	 * @throws ServiceException
	 */
	public String getCatSapById(String cod_cat) throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getCatSapById(cod_cat);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene el listado de secciones Sap.  
	 * 
	 * @return List CategoriaSapDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */
	public List getSeccionesSap() throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getSeccionesSap();
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene los datos del producto Web segun el id del producto SAP
	 * 
	 * @param id_producto
	 * @return ProductoEntity
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public ProductoEntity getProductoFOByIdProdSap(long id_producto) throws ServiceException, SystemException{
		ProductosSapCtrl prod = new ProductosSapCtrl();
		try {
			return prod.getProductoFOByIdProdSap(id_producto); 
		} catch (ProductosSapException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la categoria Sap , segun el código 
	 * 
	 * @param  id_cat
	 * @return CategoriaSapDTO 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CategoriaSapDTO getCategoriaSapById(String id_cat) throws ServiceException, SystemException{
		CategoriasSapCtrl cat = new CategoriasSapCtrl();
		try {
			return cat.getCategoriaSapById(id_cat); 
		} catch (CategoriasSapException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de Campañas------------------------------ 
	 * *************************************************************************************************************
	 */
	
	/**
	 * Obtiene informacion de campana, segun codigo
	 * 
	 * @param  id_campana
	 * @return CampanaDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CampanaDTO getCampanaById(long id_campana) throws ServiceException, SystemException{
		CampanaCtrl cam = new CampanaCtrl();
		try {
			return cam.getCampanaById(id_campana); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de las campanias, segun los criterios ingresados
	 * 
	 * @param  criterio
	 * @return List CampanaDTO
	 * @throws ServiceException
	 */
	public List getCampanasByCriteria(CampanasCriteriaDTO criterio) throws ServiceException {
		CampanaCtrl cam = new CampanaCtrl();
		try {
			return cam.getCampanasByCriteria(criterio); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la cantidad de campanias, segun los criterios ingresados
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ServiceException
	 */
	public int getCountCampanasByCriteria(CampanasCriteriaDTO criterio) throws ServiceException {
		CampanaCtrl cam = new CampanaCtrl();
		try {
			return cam.getCountCampanasByCriteria(criterio); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Actualiza informacion de campania
	 * 
	 * @param  dto
	 * @throws ServiceException
	 */
	public boolean setModCampana(ProcModCampanaDTO dto) throws ServiceException {
	    CampanaCtrl campana = new CampanaCtrl();
	    try{
	    	return campana.setModCampana(dto);
	    }catch(CampanaException ex){
	        logger.debug(" Problemas "+ex);
	        throw new ServiceException(ex.getMessage());
	    }
		
	}
	
	/**
	 * Obtiene la información del elemento, segun el id
	 * 
	 * @param  id_elemento
	 * @return ElementoDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public ElementoDTO getElementoById(long id_elemento) throws ServiceException, SystemException{
		ElementoCtrl cam = new ElementoCtrl();
		try {
			return cam.getElementoById(id_elemento); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Obtiene el listado de elementos, segun criterios ingresados
	 * 
	 * @param  criterio
	 * @return List ElementoDTO
	 * @throws ServiceException
	 */
	public List getElementosByCriteria(ElementosCriteriaDTO criterio) throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.getElementosByCriteria(criterio); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la cantidad de elementos, segun los criterios ingresados
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ServiceException
	 */
	public int getCountElementosByCriteria(ElementosCriteriaDTO criterio) throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.getCountElementosByCriteria(criterio); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Modifica la relacion entre campaña y elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public boolean setModCampanaElemento(ProcModCampElementoDTO dto) throws ServiceException, SystemException {
		CampanaCtrl campana = new CampanaCtrl();
		try {
			return campana.setModCampanaElemento(dto); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Agrega uns nueva campaña.
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setAddCampana(ProcAddCampanaDTO dto) throws ServiceException, SystemException {
		CampanaCtrl campana = new CampanaCtrl();
		try {
			return campana.setAddCampana(dto); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina una campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setDelCampana(ProcDelCampanaDTO dto) throws ServiceException, SystemException {
		CampanaCtrl campana = new CampanaCtrl();
		try {
			return campana.setDelCampana(dto); 
		} catch (CampanaException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Actualiza la informacion de elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean setModElemento(ProcModElementoDTO dto) throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.setModElemento(dto); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de los tipos de elementos
	 * 
	 * @return List TipoElementoDTO
	 * @throws ServiceException
	 */
	public List getLstTipoElementos() throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.getLstTipoElementos(); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * Agregar un nuevo elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 */
	public long setAddElemento(ProcAddElementoDTO dto) throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.setAddElemento(dto); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina un elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean setDelElemento(ProcDelElementoDTO dto) throws ServiceException {
		ElementoCtrl elem = new ElementoCtrl();
		try {
			return elem.setDelElemento(dto); 
		} catch (ElementoException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene una lista de productos con sus codigos de barra
	 * @param criteria
	 * @return List
	 * @throws ServiceException
	 */
	public List getProdCbarraByCriteria(ProductosCriteriaDTO criteria)throws ServiceException {
		ProductosCtrl prod = new ProductosCtrl();
		try {
			return prod.getProdCbarraByCriteria(criteria);
		} catch (ProductosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la cantidad de productos existentes 
	 * @param criteria
	 * @return int
	 * @throws ServiceException
	 */
	public int getCountProdCbarraByCriteria(ProductosCriteriaDTO criteria)throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		int cant = 0;
		try{
			cant = prods.getCountProdCbarraByCriteria(criteria); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
	
		return cant;	
	}

    /**
     * @param idCategoria
     * @return
     */
    public String getNombresCategoriasPadreByIdCat(long idCategoria) throws ServiceException {
		CategoriasCtrl cats = new CategoriasCtrl();		
		try{
			return cats.getNombresCategoriasPadreByIdCat( idCategoria );
		}catch(CategoriasException ex){
			logger.debug("Problemas con controles de categorias");
			throw new ServiceException(ex);
		}
    }
    
  /**
   * 
   * @param codDesc
   * @return
   * @throws ServiceException
   */
	public List getTiposProductosPorCod(String codDesc) throws ServiceException{
		ProductosCtrl productos = new ProductosCtrl();
		try{
			return productos.getTiposProductosPorCod(codDesc);
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
		}
		
	}
	
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws ServiceException
	 */
	public List getTiposProductosPorDesc(String codDesc) throws ServiceException{
		ProductosCtrl productos = new ProductosCtrl();
		try{
			return productos.getTiposProductosPorDesc(codDesc);
		}catch (ProductosException ex) { 
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
		}
		
	}
	
	/**
	 * Obtiene la ficha del producto
	 * @param codProd
	 * @return
	 * @throws ServiceException
	 */
	public List getFichaProductoPorId(long codProd) throws ServiceException {
		ProductosCtrl prods = new ProductosCtrl();
		List result = null;
		try{
			result = prods.getFichaProductoPorId(codProd);
		}catch(ProductosException ex){
			logger.debug("Problemas con los datos de la ficha del producto con id : " + codProd);
			throw new ServiceException(ex);
		}
		return result; 
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws ServiceException
	 */	
	public List getListItemFichaProductoAll() throws ServiceException {
		ProductosCtrl prods = new ProductosCtrl();
		List result = null;
		try{
			result = prods.geItemFichaProductoAll();
		}catch(ProductosException ex){
			logger.debug("Problemas al obtener la lista de items de la ficha tecnica");
			throw new ServiceException(ex);
		}
		return result; 
	}
	
	/**
	 * Obtiene el item de la ficha tecnica según id	 
	 * @param idItem
	 * @return
	 * @throws ServiceException
	 */
	public ItemFichaProductoDTO getItemFichaProductoById(long idItem) throws ServiceException {
		ProductosCtrl prods = new ProductosCtrl();
		ItemFichaProductoDTO result = null;
		try{
			result = prods.getItemFichaProductoById(idItem);
		}catch(ProductosException ex){
			logger.debug("Problema con item de la ficha tecnica con id: " + idItem);
			throw new ServiceException(ex);
		}
		return result; 
	}		
		
	/**
	 * Actualiza información de una ficha tecnica de un producto.
	 * 
	 * @param  procparam ProcModProductDTO 
	 * @return boolean, devuelve <i>true</i> si se actualizó con éxito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException 
	 * @throws DAOException 
	 */
	public boolean setModFichaTecnica(ProcModFichaTecnicaDTO procparam, boolean cambioEstadoFicha, String valorItem, boolean actualizaLog) throws ServiceException,SystemException, DAOException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.setModFichaTecnica(procparam, cambioEstadoFicha, valorItem, actualizaLog);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Verifica si existe ficha del producto. 
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si la ficha existe, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean tieneFichaProductoById(long cod_prod) throws ServiceException {
	    ProductosCtrl productos = new ProductosCtrl();
	    boolean result = false;
	    try{
	        result = productos.tieneFichaProductoById(cod_prod);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	    return result;
	}

	/**
	 * Actualiza estado ficha tecnica del producto
	 * @param prodId
	 * @param estadoFichaTecnica
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public boolean actualizaEstadoFichaTecnica(long prodId, long estadoFichaTecnica, String usrLogin, String mensaje) throws ServiceException,SystemException, DAOException {
	    ProductosCtrl productos = new ProductosCtrl();
	    try{
	    	return productos.actualizaEstadoFichaTecnica(prodId, estadoFichaTecnica, usrLogin, mensaje);
	    }catch(ProductosException ex){
	        logger.debug(" Problemas con controles de Productos ");
	        throw new ServiceException(ex);
	    }
	}
	
	public boolean eliminaFichaProductoById(long prodId) throws ServiceException {	
		ProductosCtrl prods = new ProductosCtrl();
		boolean result = false;
		try{
			result = prods.eliminaFichaProductoById(prodId); 
		}catch(ProductosException ex){
			logger.debug( "Problemas al eliminar ficha producto con id producto : " + prodId);
			throw new ServiceException(ex);
		}
	
		return result;	
	}
	
	/**
	 * Inserta productos de la categoría para Grability 
	 * 
	 * @param  id_cat
	 * @return boolean 
	 * @throws ServiceException
	 */
	public boolean updateMarcaGrabilityProducto(String id_cat, String flag) 
			throws ServiceException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
		try {
			return prodsap.updateMarcaGrabilityProducto(id_cat, flag); 
		} catch (ProductosSapException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Elimina productos de la categoría para que no aparezcan en Grability
	 * 
	 * @param  id_cat
	 * @return boolean 
	 * @throws ServiceException
	 */
	public boolean deleteMarcaGrabilityProducto(String id_cat) 
			throws ServiceException{
		ProductosSapCtrl prodsap = new ProductosSapCtrl();
		try {
			return prodsap.deleteMarcaGrabilityProducto(id_cat); 
		} catch (ProductosSapException e) {
			throw new ServiceException(e.getMessage());
		}
	}	
	

	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */
	public LinkedHashMap getCategoriasInGRB(int inicio, int fin, String idCat) throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getCategoriasInGRB(inicio, fin, idCat);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */
	public LinkedHashMap getCategoriasNoGRB(String idCat) throws ServiceException{		
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.getCategoriasNoGRB(idCat);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	public int deleteCategoriaById(String seccion, int segmento) throws ServiceException{
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.deleteCategoriaById(seccion, segmento);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}

	public int addCategoriaById(String seccion, int segmento)  throws ServiceException{
		CategoriasSapCtrl catsap = new CategoriasSapCtrl();
	    try{
	    	return catsap.addCategoriaById(seccion, segmento);
	    }catch(CategoriasSapException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}


}
