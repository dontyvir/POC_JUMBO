package cl.bbr.jumbocl.productos.service;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.productos.ctr.ProductosCTR;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.exception.ProductosException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.exception.PromocionesException;
import cl.bbr.promo.lib.service.PromocionesService;


/**
 * Capa de servicios para el área de regiones
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ProductosService {

    /**
     * Instancia para log
     */
    Logging logger = new Logging(this);

    /**
     * Constructor
     *  
     */
    public ProductosService() {
        this.logger.debug("New productosService");
    }

    /**
     * Recupera el listado de categorías
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista con DTO de categorías
     * @throws ServiceException
     */
    public List Categoria(long cliente_id) throws ServiceException {
        ProductosCTR dirctr = new ProductosCTR();
        try {
            return dirctr.Categoria(cliente_id);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (Categoria)", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * Recupera el listado de categorías intermedias y Terminales de una cabecera
     * 
     * @param cliente_id Identificador único del cliente
     * @param cabecera_id Identificador de la categoría cabecera
     * @return Lista con DTO de categorías
     * @throws ServiceException
     */
    public List categoriasGetInteryTerm(long cliente_id, long cabecera_id) throws ServiceException {
        ProductosCTR dirctr = new ProductosCTR();
        try {
            return dirctr.categoriasGetInteryTerm(cliente_id, cabecera_id);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (categoriasGetInteryTerm)", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * Listado de productos pra una categoría
     * 
     * @param local_id
     *            Identificador único del local
     * @param categoria_id
     *            Identificador único de la categoría
     * @param cliente_id
     *            Identificador único del cliente
     * @param marca
     *            Marca para filtro
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param lista_tcp
     *            Lista de TCP del cliente
     * @return Lista de DTO con datos delos productos
     * @throws ServiceException
     */
    public List getProductosList(String local_id, long categoria_id, long cliente_id, String marca, String orden,
            List lista_tcp) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();
        List list = null;

        try {
            list = dirctr.getProductosList(local_id, categoria_id, cliente_id, marca, orden, lista_tcp);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getProductosList)", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * Recupera datos de la categoría
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return DTO con información de la categoría
     * @throws ServiceException
     */
    public CategoriaDTO getCategoria(long categoria_id) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();

        try {
            return dirctr.getCategoria(categoria_id);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getCategoria)", ex);
            throw new ServiceException(ex);
        }

    }

    /**
     * Recupera las marcas para una categoría
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return Lista de DTO con datos de las marcas
     * @throws ServiceException
     */
    public List getMarcas(long categoria_id) throws ServiceException {
        ProductosCTR dirctr = new ProductosCTR();

        try {
            return dirctr.getMarcas(categoria_id);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getMarcas)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * Listado con un sólo producto que corresponde a ProductoDTO para el
     * producto consultado
     * 
     * @param producto_id
     *            Identificador único del producto
     * @param cliente_id
     *            Identificador único del cliente
     * @param local_id
     *            Identificador único del local
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return DTO con datos del producto
     * @throws ServiceException
     */
    public List getProducto(long producto_id, long cliente_id, long local_id, List lista_tcp, String idSession) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();
        List list = null;

        try {
            list = dirctr.getProducto(producto_id, cliente_id, local_id, lista_tcp, idSession);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getProducto)", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * Listado de los productos sugeridos
     * 
     * @param producto_id
     *            Identificador único del producto
     * @param cliente_id
     *            Identificador único del cliente
     * @param local_id
     *            Identificador único del local
     * @return Lista con DTO con datos de los productos sugeridos
     * @throws ServiceException
     */
    public List getSugerido(long producto_id, long cliente_id, long local_id, String idSession) throws ServiceException {
        ProductosCTR dirctr = new ProductosCTR();
        try {
            return dirctr.getSugerido(producto_id, cliente_id, local_id, idSession);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getSugerido)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * Resultado de la búsqueda
     * 
     * @param patron
     *            Patrón de búsqueda
     * @param local_id
     *            Identificador único del local
     * @return Lista de DTO con datos
     * @throws ServiceException
     */
    public List getSearch(List patron, long local_id) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();
        List list = null;

        try {
            list = dirctr.getSearch(patron, local_id);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getSearch)", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * Búsqueda de productos por marca
     * 
     * @param local_id
     *            Identificador único del local
     * @param marca_id
     *            Identificador único de la marca para ordenar
     * @param cliente_id
     *            Identificador único del cliente
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param patron
     *            Patrón de búsqueda de productos
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista de DTO con información de los productos
     * @throws ServiceException
     */
    public List getProductoMarca(String local_id, long marca_id, long cliente_id, String orden, List patron,
            List lista_tcp) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();
        List list = null;

        try {
            list = dirctr.getProductoMarca(local_id, marca_id, cliente_id, orden, patron, lista_tcp);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (getProductoMarca)", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * Búsqueda de productos por sección (categoría)
     * 
     * @param local_id
     *            Identificador único del local
     * @param categoria_id
     *            Identificador único de la categoría
     * @param cliente_id
     *            Identificador único del cliente
     * @param marca
     *            Marca para filtro
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param patron
     *            Patrón de búsqueda de productos
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista de DTO con información de los productos
     * @throws ServiceException
     */
    public List getProductosListSeccion(String local_id, long categoria_id, long cliente_id, String marca,
            String orden, List patron, List lista_tcp) throws ServiceException {

        ProductosCTR dirctr = new ProductosCTR();
        List list = null;

        try {
            list = dirctr.getProductosListSeccion(local_id, categoria_id, cliente_id, marca, orden, patron, lista_tcp);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos por seccion (getProductosListSeccion)", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * Categorias de primer nivel
     * 
     * @return
     * @throws ServiceException
     */
    public List categorias() throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.categorias();
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (categorias)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCategoria
     * @return
     * @throws ServiceException
     */
    public List subCategorias(int idCategoria) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.subCategorias(idCategoria);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (subCategorias)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCategoria
     * @param idSubCategoria
     * @param idMarca2
     * @param ordenarPor
     * @param filaCantidad
     * @param filaNumero
     * @return
     * @throws ServiceException
     * @throws PromocionesException
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idMarca,
            String ordenarPor, int filaNumero, int filaCantidad) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idMarca, ordenarPor,
                    filaNumero, filaCantidad);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productos)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idLocal
     * @param idSubCategoria
     * @return
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idCategoria, String idSession)  throws ServiceException {
       ProductosCTR productosCTR = new ProductosCTR();
       try {
           return productosCTR.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idCategoria, idSession);
       } catch (ProductosException ex) {
           throw new ServiceException(ex);
       }
    }

    /**
     * Se asigna a cada producto su promoción si la posee.
     * 
     * @param productos
     * @param lista_tcp
     * @param idLocal
     * @return
     * @throws PromocionesException
     */
    public List cargarPromociones(List productos, List lista_tcp, int idLocal) throws PromocionesException {
        /*
         * Creo la lista de productos en formato (id_bo1, id_bo2, id_bo3)
         */
        StringBuffer listaIdBo = new StringBuffer("(");
        StringBuffer listaIdProd = new StringBuffer("(");
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            ProductoDTO pro = (ProductoDTO) iter.next();
            listaIdBo.append(pro.getPro_id_bo());
            listaIdProd.append(pro.getPro_id());
            if (iter.hasNext()){
                listaIdBo.append(",");
                listaIdProd.append(",");
            }
            
        }
        listaIdBo.append(")");
        listaIdProd.append(")");
        ////////////////////////////////////////////////////
        PromocionesService promoctr = new PromocionesService();
        //carga solamente las promociones de productos
        Hashtable promociones = promoctr.getPromociones(listaIdBo.toString(), idLocal, lista_tcp);
            for (Iterator iter = productos.iterator(); iter.hasNext();) {
                ProductoDTO pro = (ProductoDTO) iter.next();
                Integer idProducto_bo = new Integer(pro.getPro_id_bo());
                List promPorProducto = (List) promociones.get(idProducto_bo);
                if(promPorProducto!=null){
                	if(!promPorProducto.isEmpty() && promPorProducto.size()>0){
                		pro.setListaPromociones(promPorProducto);
                	}
                }
            }
            
          //recato promociones de banner de productos si es que existen desde la tabla fo_productos_promo
          //se recorre la lista de productos y se pregunta por las lista de promociones null
          //si existen listas vacias, se buscan las promociones de banner de productos
          //y se asignas al producto.
        	Hashtable promocionesBann = promoctr.getPromocionesBanner(listaIdProd.toString(), idLocal, lista_tcp);
        	 for (Iterator iter = productos.iterator(); iter.hasNext();) {
                 ProductoDTO pro = (ProductoDTO) iter.next();
                 Long idProducto = new Long(pro.getPro_id());
                 
                 if(pro.getListaPromociones() == null) {
	                 int prducto_id = Integer.parseInt(idProducto.toString());
	                 List promPorProducto = (List) promocionesBann.get(new Integer(prducto_id));
	                 //si la lista de promosiones es vacia, busco si existe banner de producto para ese producto y se asignan
	                 if(promPorProducto!=null){
	                 	if(!promPorProducto.isEmpty() && promPorProducto.size()>0){
	                 		pro.setListaPromociones(promPorProducto);
	                 	}
	                 }
                 }
             }
        return productos;
    }
    
    /**
     * Se asigna a cada producto su promoción si la posee.
     * 
     * @param productos
     * @param lista_tcp
     * @param idLocal
     * @return
     * @throws PromocionesException
     */
    public List cargarPromocionesMiCarro(List productos, List lista_tcp, int idLocal) throws PromocionesException {
        /*
         * Creo la lista de productos en formato (id_bo1, id_bo2, id_bo3)
         */
        StringBuffer listaIdBo = new StringBuffer("(");
        StringBuffer listaIdProd = new StringBuffer("(");
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            MiCarroDTO pro = (MiCarroDTO) iter.next();
            listaIdBo.append(pro.getId_bo());
            listaIdProd.append(pro.getPro_id());
            if (iter.hasNext()) {
                listaIdBo.append(",");
            	listaIdProd.append(",");
            }
        }
        listaIdBo.append(")");
        listaIdProd.append(")");
        ////////////////////////////////////////////////////
        PromocionesService promoctr = new PromocionesService();
        Hashtable promociones = promoctr.getPromociones(listaIdBo.toString(), idLocal, lista_tcp);
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            MiCarroDTO pro = (MiCarroDTO) iter.next();
            Integer idProducto = new Integer(Integer.parseInt(pro.getId_bo()+""));
            List promPorProducto = (List) promociones.get(idProducto);
            pro.setListaPromociones(promPorProducto);
        }
        
        //recato promociones de banner de productos si es que existen
      	Hashtable promocionesBann = promoctr.getPromocionesBanner(listaIdProd.toString(), idLocal, lista_tcp);
      	 for (Iterator iter = productos.iterator(); iter.hasNext();) {
      		 	MiCarroDTO pro = (MiCarroDTO) iter.next();
      		 	Long idProducto = new Long(pro.getPro_id());
               
      		 	if(pro.getListaPromociones() == null) {
	                 int prducto_id = Integer.parseInt(idProducto.toString());
	                 List promPorProducto = (List) promocionesBann.get(new Integer(prducto_id));
	                 //si la lista de promosiones es vacia, busco si existe banner de producto para ese producto
	                 if(promPorProducto!=null){
	                 	if(!promPorProducto.isEmpty() && promPorProducto.size()>0){
	                 		pro.setListaPromociones(promPorProducto);
	                 	}
	                 }
      		 	}
           }
        return productos;
    }

    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ServiceException
     */
    public List marcasPorSubCategoria(int idLocal, int idSubCategoria) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.marcasPorSubCategoria(idLocal, idSubCategoria);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (marcas)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ServiceException
     */
    public int cantidadProductosDeSubCategoria(int idLocal, int idSubCategoria, int idMarca) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.cantidadProductosDeSubCategoria(idLocal, idSubCategoria, idMarca);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productos)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idsProductos
     * @param idLocal
     * @param idCategoria
     * @param idSubCategoria
     * @return
     * @throws ServiceException
     */
    public int cantidadProductosPorIds(List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria)
            throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.cantidadProductosPorIds(idsProductos, idLocal, idMarca, idCategoria, idSubCategoria);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productos)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idsProductos
     * @param idCategoria
     * @param idSubCategoria
     * @param ordenarPor
     * @param filaCantidad
     * @param filaNumero
     * @return
     * @throws ServiceException
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal, int idMarca, int idCategoria,
            int idSubCategoria, String ordenarPor, int filaNumero, int filaCantidad) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.productosPorIds(idCliente, idsProductos, idLocal, idMarca, idCategoria, idSubCategoria,
                    ordenarPor, filaNumero, filaCantidad);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productos)", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @return
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal) throws ServiceException {
       ProductosCTR productosCTR = new ProductosCTR();
       try {
           return productosCTR.productosPorIds(idCliente, idsProductos, idLocal);
       } catch (ProductosException ex) {
           logger.error("Problemas con controles de productos (productos)", ex);
           throw new ServiceException(ex);
       }
    }
    public ProductoDTO productoById(int idCliente, int idProducto, int idLocal) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.productoById(idCliente, idProducto, idLocal);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productos)", ex);
            throw new ServiceException(ex);
        }
     }
    // INICIO INDRA 22-10-2012
    public List getProductosDespubOrSinStock(long idCliente, List idProductos, int idLocal) throws ServiceException{
    	ProductosCTR productosCTR = new ProductosCTR();
    	try {
    		return productosCTR.getProductosDespubOrSinStock(idCliente, idProductos, idLocal);
    	} catch (ProductosException ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new ServiceException(ex);
    	}
    }

    public List getProductosSinStockDespublicadosPorLista(long idCliente, int idLista, int idLocal) throws ServiceException{
    	ProductosCTR productosCTR = new ProductosCTR();
    	try {
    		return productosCTR.getProductosSinStockDespublicadosPorLista(idCliente, idLista, idLocal);
    	} catch (ProductosException ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new ServiceException(ex);
    	}
    }
    // FIN INDRA 22-10-2012
    /**
     * @param marcasIds
     * @param idLocal
     * @return
     * @throws ServiceException
     */
    public List marcasPorIds(List marcasIds, int idLocal) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.marcasPorIds(marcasIds, idLocal);
        } catch (ProductosException ex) {
            logger.error("Problemas marcasPorIdsProductos", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param clienteId
     * @return
     * @throws ServiceException
     */
    public Hashtable productosCarro(int clienteId, String idSession) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.productosCarro(clienteId, idSession);
        } catch (ProductosException ex) {
            logger.error("Problemas con controles de productos (productosCarro)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param listaProductos
     * @param idLocal
     * @return
     */
    public Hashtable getProductosDestacadosDeHoy(int localId) throws ServiceException {
        ProductosCTR productosCTR = new ProductosCTR();
        try {
            return productosCTR.getProductosDestacadosDeHoy(localId);
        } catch (ProductosException ex) {
            logger.error("Error en getProductosDestacadosDeHoy: " + ex.getMessage());
            throw new ServiceException(ex);
        }
    }

   /**
    * @param localId
    * @param categoriaId
    * @param clienteId
    * @return
    * @throws ServiceException
    */
   public List productosMasVendidos(int localId, int categoriaId, int clienteId, String idSession) throws ServiceException {
      ProductosCTR productosCTR = new ProductosCTR();
      try {
          return productosCTR.productosMasVendidos(localId, categoriaId, clienteId, idSession);
      } catch (ProductosException ex) {
          logger.error("Error en productosMasVendidos: " + ex.getMessage());
          throw new ServiceException(ex);
      }
   }
   /**
    * Carro Abandonado
    * @param clienteId
    * @throws ServiceException
    */
   
   public void updateFechaMiCarro(int clienteId) throws ServiceException {
       ProductosCTR productosCTR = new ProductosCTR();
       try {
           productosCTR.updateFechaMiCarro(clienteId);
       } catch (ProductosException ex) {
           logger.error("Error en updateFechaMiCarro: " + ex.getMessage());
           throw new ServiceException(ex);
       }
   }

    /**
	 * Obtiene la ficha del producto
	 * @param codProd
	 * @return
	 * @throws ServiceException
	 */
	public List getFichaProductoById(long codProd)throws ServiceException {	
		ProductosCTR productosCTR = new ProductosCTR();
		List result = null;
		try{
			result = productosCTR.getFichaProductoById(codProd); 
		}catch(ProductosException ex){
			logger.debug( "Problemas con la ficha del producto id: " + codProd);
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
		ProductosCTR productosCTR = new ProductosCTR();
		List result = null;
		try{
			result = productosCTR.geItemFichaProductoAll();
		}catch(ProductosException ex){
			logger.debug("Problemas al obtener la lista de items de la ficha tecnica");
			throw new ServiceException(ex);
		}
		return result; 
	}
	
	/**
	 * Verifica si existe ficha del producto. 
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si la ficha existe, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean tieneFichaProductoById(long cod_prod) throws ServiceException {
		ProductosCTR productos = new ProductosCTR();
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
     * @param idCliente
     * @return
     */
    public List productosSustitutosByClienteFO(long idCliente) throws ServiceException {
    	ProductosCTR dirctr = new ProductosCTR();
        try {            
            return dirctr.productosSustitutosByClienteFO( idCliente );
        } catch (ProductosException ex) {
            logger.error( "Problemas (productosSustitutosByCliente)", ex);
            throw new ServiceException(ex);
        }
    }
    

    /**
     * @param idCliente
     * @param esResumen
     * @return
     */
    public List productosSustitutosPorCategoriaFO(long idCliente, boolean esResumen) throws ServiceException {
    	ProductosCTR dirctr = new ProductosCTR();
        try {            
            return dirctr.productosSustitutosPorCategoriaFO( idCliente, esResumen );
        } catch (ProductosException ex) {
            logger.error( "Problemas (productosSustitutosPorCategoria)", ex);
            throw new ServiceException(ex);
        }
    }

	public ProductoDTO getProductoPrecioFO(String idProd, long idLocal) throws ServiceException {
		ProductosCTR dirctr = new ProductosCTR();
        try {            
            return dirctr.getProductoPrecioFO( idProd, idLocal );
        } catch (ProductosException ex) {
            logger.error( "Problemas (getProductoPrecioFO)", ex);
            throw new ServiceException(ex);
        }
	}

}