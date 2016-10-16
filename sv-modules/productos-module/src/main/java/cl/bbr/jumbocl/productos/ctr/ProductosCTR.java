package cl.bbr.jumbocl.productos.ctr;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import cl.bbr.jumbocl.common.model.FichaProductoEntity;
import cl.bbr.jumbocl.contenidos.dto.FichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.productos.dao.DAOFactory;
import cl.bbr.jumbocl.productos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.exception.ProductosDAOException;
import cl.bbr.jumbocl.productos.exception.ProductosException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.exception.PromocionesException;
import cl.bbr.promo.lib.service.PromocionesService;


/**
 * <p>
 * Clase para que controla el flujo de datos desde el reposotorio de datos DAO a
 * la aplicación.
 * 
 * @author BBR ecommerce & retail
 *  
 */
public class ProductosCTR {

    /**
     * Instancia para log
     */
    static Logging logger = new Logging(ProductosCTR.class);
    
    private JdbcProductosDAO productosDAO;

    /**
     * Constructor
     *  
     */
    public ProductosCTR() {
        this.productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
    }

    /**
     * Recupera el listado de categorías
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista con DTO de categorías
     * @throws ProductosException
     */
    public List Categoria(long cliente_id) throws ProductosException {
        List result = new ArrayList();
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            List lista = (List) productosDAO.getCategoriasList(cliente_id);
            CategoriaDTO cat1 = null;
            CategoriaDTO cat2 = null;
            CategoriaDTO cat3 = null;
            List terminales = new ArrayList();
            List intermedias = new ArrayList();

            // separa los padres de sus hijos
            for (int i = 0; i < lista.size(); i++) {
                cat1 = null;
                cat1 = (CategoriaDTO) lista.get(i);
                if (cat1.getTipo().equals("C")) {
                    for (int f = 0; f < lista.size(); f++) {
                        cat2 = null;
                        cat2 = (CategoriaDTO) lista.get(f);
                        if (cat2.getId_padre() == cat1.getId()) {
                            for (int g = 0; g < lista.size(); g++) {
                                cat3 = null;
                                cat3 = (CategoriaDTO) lista.get(g);
                                if (cat3.getId_padre() == cat2.getId()) {
                                    terminales.add(cat3);
                                }
                            }
                            cat2.setCategorias(terminales);
                            intermedias.add(cat2);
                        }
                        
                    }
                    cat1.setCategorias(intermedias);
                    result.add(cat1);
                }
            }

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (Categoria)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (Categoria)", ex);
            throw new ProductosException(ex);
        }
        return result;
    }
    
    /**
     * Recupera el listado de categorías
     * 
     * @param cliente_id Identificador único del cliente
     * @param cabecera_id Identificador de la categoría cabecera
     * @return Lista con DTO de categorías
     * @throws ProductosException
     */
    public List categoriasGetInteryTerm(long cliente_id, long cabecera_id) throws ProductosException {
        List result = new ArrayList();
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            List lista = (List) productosDAO.categoriasGetInteryTerm(cliente_id, cabecera_id);
            CategoriaDTO cat1 = null;
            CategoriaDTO cat2 = null;
            List terminales = new ArrayList();

            // separa los padres de sus hijos
            for (int i = 0; i < lista.size(); i++) {
                cat1 = null;
                cat1 = (CategoriaDTO) lista.get(i);
                if (cat1.getTipo().equals("I")) {
                    for (int f = 0; f < lista.size(); f++) {
                        cat2 = null;
                        cat2 = (CategoriaDTO) lista.get(f);
                        if (cat2.getId_padre() == cat1.getId()) {
                            terminales.add(cat2);
                        }
                    }    
                    cat1.setCategorias(terminales);
                    result.add(cat1);
                }
            }    

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (categoriasGetInteryTerm)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (categoriasGetInteryTerm)", ex);
            throw new ProductosException(ex);
        }
        return result;
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
     * @throws ProductosException
     */
    public List getProductosList(String local_id, long categoria_id, long cliente_id, String marca, String orden,
            List lista_tcp) throws ProductosException {
        List result = new ArrayList();
        List aux = new ArrayList();
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            aux = (List) productosDAO.getProductosList(local_id, categoria_id, cliente_id, marca, orden);

            // Recuperar las promociones por producto
            PromocionesService promoctr = new PromocionesService();

            for (int i = 0; i < aux.size(); i++) {
                ProductoDTO prod = (ProductoDTO) aux.get(i);

                List l_promo = promoctr.getPromocionesByProductoId(prod.getPro_id(),prod.getPro_id_bo(), local_id, lista_tcp);
                if (l_promo.size() > 0)
                    prod.setListaPromociones(l_promo);

                result.add(prod);

            }

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getProductosList)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getProductosList)", ex);
            throw new ProductosException(ex);
        } catch (PromocionesException ex) {
            logger.error("Problema (getProductosList)", ex);
            throw new ProductosException(ex);
        }
        return result;
    }

    /**
     * Recupera datos de la categoría
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return DTO con información de la categoría
     * @throws ProductosException
     */
    public CategoriaDTO getCategoria(long categoria_id) throws ProductosException {
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            return productosDAO.getCategoria(categoria_id);

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getCategoria)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getCategoria)", ex);
            throw new ProductosException(ex);
        }

    }

    /**
     * Recupera las marcas para una categoría
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return Lista de DTO con datos de las marcas
     * @throws ProductosException
     */
    public List getMarcas(long categoria_id) throws ProductosException {
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            return productosDAO.getMarcas(categoria_id);

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getMarcas)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getMarcas)", ex);
            throw new ProductosException(ex);
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
     * @throws ProductosException
     */
    public List getProducto(long producto_id, long cliente_id, long local_id, List lista_tcp, String idSession) throws ProductosException {

        List result = new ArrayList();

        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            List aux = productosDAO.getProducto(producto_id, cliente_id, local_id, idSession);

            // Recuperar las promociones por producto
            PromocionesService promoctr = new PromocionesService();

            for (int i = 0; i < aux.size(); i++) {
                ProductoDTO prod = (ProductoDTO) aux.get(i);

                List l_promo = promoctr.getPromocionesByProductoId(prod.getPro_id(), prod.getPro_id_bo(), local_id + "", lista_tcp);
                if (l_promo.size() > 0)
                    prod.setListaPromociones(l_promo);

                result.add(prod);

            }

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getProducto)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getProducto)", ex);
            throw new ProductosException(ex);
        } catch (PromocionesException e) {
            logger.error("Problema (getProducto)", e);
            throw new ProductosException(e);
        }

        return result;

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
     * @throws ProductosException
     */
    public List getSugerido(long producto_id, long cliente_id, long local_id, String idSession) throws ProductosException {
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            return productosDAO.getSugerido(producto_id, cliente_id, local_id, idSession);
        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getSugerido)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getSugerido)", ex);
            throw new ProductosException(ex);
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
     * @throws ProductosException
     */
    public List getSearch(List patron, long local_id) throws ProductosException {
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            return productosDAO.getSearch(patron, local_id);

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getSearch)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getSearch)", ex);
            throw new ProductosException(ex);
        }
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
     *            Lista de TCP para el cliente
     * @return Lista de DTO con información de los productos
     * @throws ProductosException
     */
    public List getProductoMarca(String local_id, long marca_id, long cliente_id, String orden, List patron,
            List lista_tcp) throws ProductosException {

        List result = new ArrayList();

        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            List aux = productosDAO.getSearchMarca(local_id, marca_id, cliente_id, orden, patron);

            // Recuperar las promociones por producto
            PromocionesService promoctr = new PromocionesService();

            for (int i = 0; i < aux.size(); i++) {
                ProductoDTO prod = (ProductoDTO) aux.get(i);

                List l_promo = promoctr.getPromocionesByProductoId(prod.getPro_id(), prod.getPro_id_bo(), local_id, lista_tcp);
                if (l_promo.size() > 0)
                    prod.setListaPromociones(l_promo);

                result.add(prod);

            }

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getProductoMarca)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getProductoMarca)", ex);
            throw new ProductosException(ex);
        } catch (PromocionesException ex) {
            logger.error("Problema (getProductoMarca)", ex);
            throw new ProductosException(ex);
        }

        return result;

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
     * @throws ProductosException
     */
    public List getProductosListSeccion(String local_id, long categoria_id, long cliente_id, String marca,
            String orden, List patron, List lista_tcp) throws ProductosException {
        List result = new ArrayList();
        try {
            JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC)
                    .getProductosDAO();

            List aux = (List) productosDAO.getSearchSeccion(local_id, categoria_id, cliente_id, marca, orden, patron);

            // Recuperar las promociones por producto
            PromocionesService promoctr = new PromocionesService();

            for (int i = 0; i < aux.size(); i++) {
                ProductoDTO prod = (ProductoDTO) aux.get(i);

                List l_promo = promoctr.getPromocionesByProductoId(prod.getPro_id(),prod.getPro_id_bo(), local_id, lista_tcp);
                if (l_promo.size() > 0)
                    prod.setListaPromociones(l_promo);

                result.add(prod);

            }

        } catch (NullPointerException ex) {
            logger.error("Problema con null en los datos (getProductosListSeccion)");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (getProductosListSeccion)", ex);
            throw new ProductosException(ex);
        } catch (PromocionesException e) {
            logger.error("Problema (getProductosListSeccion)", e);
            throw new ProductosException(e);
        }
        return result;
    }

    /**
     * Lista de categorias del primer nivel
     * @return
     * @throws ProductosException
     */
    public List categorias() throws ProductosException {
        try {
            return productosDAO.categorias();
        } catch (ProductosDAOException ex) {
            logger.error("Problema (categorias)", ex);
            throw new ProductosException(ex);
        }
    }

    /**
     * @param idCategoria
     * @return
     * @throws ProductosException
     */
    public List subCategorias(int idCategoria) throws ProductosException {
        try {
            return productosDAO.subCategorias(idCategoria);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (categorias)", ex);
            throw new ProductosException(ex);
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
     * @throws ProductosException
     * @throws PromocionesException
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idMarca, String ordenarPor, int filaNumero, int filaCantidad) throws ProductosException{
        try {
            return productosDAO.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idMarca, ordenarPor, filaNumero, filaCantidad);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productos)", ex);
            throw new ProductosException(ex);
        }
    }
    
    /**
     * @param idCliente
     * @param idLocal
     * @param idSubCategoria
     * @return
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idCategoria, String idSession) throws ProductosException{
       try {
          return productosDAO.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idCategoria, idSession);
      } catch (ProductosDAOException ex) {
          throw new ProductosException(ex);
      }
    }

    
    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ProductosException
     */
    public List marcasPorSubCategoria(int idLocal, int idSubCategoria) throws ProductosException {
        try {
            return productosDAO.marcasPorSubCategoria(idLocal, idSubCategoria);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (marcas)", ex);
            throw new ProductosException(ex);
        }
    }

    
    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ProductosException
     */
    public int cantidadProductosDeSubCategoria(int idLocal, int idSubCategoria, int idMarca) throws ProductosException {
        try {
            return productosDAO.cantidadProductosDeSubCategoria(idLocal, idSubCategoria, idMarca);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productos)", ex);
            throw new ProductosException(ex);
        }
    }

    /**
     * @param idsProductos
     * @param idLocal
     * @param idCategoria
     * @param idSubCategoria
     * @return
     * @throws ProductosException
     */
    public int cantidadProductosPorIds(List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria) throws ProductosException {
        try {
            return productosDAO.cantidadProductosPorIds(idsProductos, idLocal, idMarca, idCategoria, idSubCategoria);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productos)", ex);
            throw new ProductosException(ex);
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
     * @throws ProductosException
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria, String ordenarPor, int filaNumero, int filaCantidad) throws ProductosException {
        try {
            return productosDAO.productosPorIds(idCliente,idsProductos, idLocal, idMarca, idCategoria, idSubCategoria, ordenarPor, filaNumero, filaCantidad);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productos)", ex);
            throw new ProductosException(ex);
        }
    }
    
    /**
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @return
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal)  throws ProductosException {
       try {
          return productosDAO.productosPorIds(idCliente,idsProductos, idLocal);
      } catch (ProductosDAOException ex) {
          logger.error("Problema (productos)", ex);
          throw new ProductosException(ex);
      }
    }
    public ProductoDTO productoById(int idCliente, int idProducto, int idLocal)  throws ProductosException {
        try {
           return productosDAO.productoById(idCliente, idProducto, idLocal);
       } catch (ProductosDAOException ex) {
           logger.error("Problema (productos)", ex);
           throw new ProductosException(ex);
       }
     }
    
    // INICIO INDRA 22-10-2012
    public List getProductosDespubOrSinStock(long idCliente, List idProductos, int idLocal) throws ProductosException{
    	try {
    		return productosDAO.getProductosDespubOrSinStock(idCliente, idProductos, idLocal);
    	} catch (ProductosDAOException ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new ProductosException(ex);
    	}
    }
    
    public List getProductosSinStockDespublicadosPorLista(long idCliente, int idLista, int idLocal) throws ProductosException{
    	try {
    		return productosDAO.getProductosSinStockDespublicadosPorLista(idCliente, idLista, idLocal);
    	} catch (ProductosDAOException ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new ProductosException(ex);
    	}
    }
    // FIN INDRA 22-10-2012
    /**
     * @param marcasIds
     * @param idLocal
     * @return
     * @throws ProductosException
     */
    public List marcasPorIds(List marcasIds, int idLocal) throws ProductosException {
        try {
            return productosDAO.marcasPorIds(marcasIds, idLocal);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productos)", ex);
            throw new ProductosException(ex);
        }
    }

    /**
     * @param clienteId
     * @return
     * @throws ProductosException
     */
    public Hashtable productosCarro(int clienteId, String idSession) throws ProductosException {
        try {
            return productosDAO.productosCarro(clienteId, idSession);
        } catch (ProductosDAOException ex) {
            logger.error("Problema (productosCarro)", ex);
            throw new ProductosException(ex);
        }
    }
    
    public Hashtable getProductosDestacadosDeHoy(int localId) throws ProductosException {
        try {
            return productosDAO.getProductosDestacadosDeHoy(localId);
        } catch (ProductosDAOException ex) {
            logger.error("Error getProductosDestacadosDeHoy: " + ex);
            throw new ProductosException(ex);
        }
    }

   /**
    * @param localId
    * @param categoriaId
    * @param clienteId
    * @return
    * @throws ProductosException
    */
   public List productosMasVendidos(int localId, int categoriaId, int clienteId, String idSession) throws ProductosException {
      try {
         return productosDAO.productosMasVendidos(localId, categoriaId, clienteId, idSession);
     } catch (ProductosDAOException ex) {
         logger.error("Error productosMasVendidos: " + ex);
         throw new ProductosException(ex);
     }
   }
   
   /**
    *   Carro Abandonado
    * @param clienteId
    * @throws ProductosException
    */
   public void updateFechaMiCarro(int clienteId) throws ProductosException {
       try {
           productosDAO.updateFechaMiCarro(clienteId);
       } catch (ProductosDAOException ex) {
           logger.error("Error updateFechaMiCarro: " + ex);
           throw new ProductosException(ex);
       }
   }
   /**
	 * Obtiene la información de la ficha del producto
	 * @param codProd
	 * @return
	 * @throws ProductosException
	 */
	public List getFichaProductoById(long codProd) throws ProductosException{			
		List result = new ArrayList();
		try{
			
			logger.debug("en getFichaProductoPorId");
			JdbcProductosDAO datosFichaDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
			
			List lstDatosFicha = new ArrayList();
			lstDatosFicha = datosFichaDAO.getFichaProductoById(codProd);
			FichaProductoEntity datosFichaEntity = null;
			
			for(int i=0;i<lstDatosFicha.size();i++){
				datosFichaEntity = null;
				datosFichaEntity = (FichaProductoEntity)lstDatosFicha.get(i);
				FichaProductoDTO dto = new FichaProductoDTO(datosFichaEntity.getPftProId(), datosFichaEntity.getPftPfiItem(), datosFichaEntity.getPftPfiSecuencia(), datosFichaEntity.getPftDescripcionItem(), datosFichaEntity.getPftEstadoItem());
								
				result.add(dto);
			}
			
		}catch(ProductosDAOException ex){
			logger.debug("Problema getFichaProductoById:"+ex);
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
     * @param idCliente
     * @return
     */
    public List productosSustitutosByClienteFO(long idCliente) throws ProductosException {
        try {
        	JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            return productosDAO.productosSustitutosByClienteFO( idCliente );
        } catch ( NullPointerException ex ) {
            logger.error("productosSustitutosByCliente - Problema con null");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("productosSustitutosByCliente - Problem", ex);
            throw new ProductosException(ex);
        }
    }
    

    /**
     * @param idCliente
     * @param esResumen
     * @return
     */
    public List productosSustitutosPorCategoriaFO(long idCliente, boolean esResumen) throws ProductosException {
        try {
        	JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            return productosDAO.productosSustitutosPorCategoriaFO( idCliente, esResumen );
        } catch ( NullPointerException ex ) {
            logger.error("productosSustitutosPorCategoria - Problema con null");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("productosSustitutosPorCategoria - Problem", ex);
            throw new ProductosException(ex);
        }
    }

	public ProductoDTO getProductoPrecioFO(String idProd, long idLocal) throws ProductosException {
		try {
        	JdbcProductosDAO productosDAO = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
            return productosDAO.getProductoPrecioFO( idProd, idLocal );
        } catch ( NullPointerException ex ) {
            logger.error("productosSustitutosPorCategoria - Problema con null");
            throw new ProductosException(ex);
        } catch (ProductosDAOException ex) {
            logger.error("productosSustitutosPorCategoria - Problem", ex);
            throw new ProductosException(ex);
        }	}

}
