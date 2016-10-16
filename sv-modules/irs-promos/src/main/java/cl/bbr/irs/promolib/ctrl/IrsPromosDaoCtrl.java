package cl.bbr.irs.promolib.ctrl;

import java.util.List;

import cl.bbr.irs.promolib.dao.DAOFactory;
import cl.bbr.irs.promolib.dao.JdbcIrsPromocionesDAO;
import cl.bbr.irs.promolib.entity.LocalBoEntity;
import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.irs.promolib.entity.ProductoPromosEntity;
import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.log.Logging;

//import cl.bbr.transactions.JdbcTransaccion;

public class IrsPromosDaoCtrl {
    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    public IrsPromosDaoCtrl() {
    }

    public void setTrx(JdbcTransaccion trx) throws IrsPromocionDAOException {
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        dao.setTrx(trx);
    }

    /**
	 * Metodo para obtener todos las promociones (tipos y codigos)
	 * de seccion activas para un determinado local
     * @param local
     * @return List PromoSeccionEntity
     * @throws IrsPromocionException
     */
    public List getAllPromoSeccion(int local) throws IrsPromocionException {
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getAllPromoSeccion(local);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado getAllPromoSeccion",e);
        }
    }

    /**
	 * Metdo para obtener una promocion de seccion
	 * activa para un determinado local
	 * @param tipo Tipo de promocion seccion (lunes, martes...)
	 * @param local Codigo de local
     * @return PromoSeccionEntity
     * @throws IrsPromocionException
     */
	public PromoSeccionEntity getPromoSeccion(int tipo, int local, boolean getSecciones) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getPromoSeccion(tipo, local, getSecciones);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado getPromoByTipo",e);
        }
    }

    /**
	 * Agregar o actualiza la referencia de la promocion para una determinada seccion
     * @param promo
     * @throws IrsPromocionException
     */
	public void updinsPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int reg = 0;
        try {
            reg = dao.updPromoSeccion(promo);
            if (reg > 0)
                logger.debug("PromoSeccion actualizada");
            else {
                dao.insPromoSeccion(promo);
            }
        } catch (IrsPromocionDAOException ex_upd) {
			logger.error("Problema al realizar Insert-Update de PromoSeccion:" + ex_upd);
			throw new IrsPromocionException("Error no controlado al realizar Insert-Update de PromoSeccion",ex_upd);
        }

    }

    /**
     * Agregar promocion para una determinada seccion
     * @param promo
     * @throws IrsPromocionException
     */
	public void insPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.insPromoSeccion(promo);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al insertar PromoSeccion",e);
        }

    }

    /**
     * actualiza la referencia de la promocion para una determinada seccion
     * @param promo
     * @return numero de registros actualizados
     * @throws IrsPromocionException
     */
	public int updPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            return dao.updPromoSeccion(promo);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al actualizar PromoSeccion",e);
        }
    }

    /**
     * elimina la promocion para una determinada seccion
     * @param promo
     * @throws IrsPromocionException
     */
	public void delPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.delPromoSeccion(promo);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al actualizar PromoSeccion",e);
        }
    }

    /**
	 * Metodo para obtener toda la matriz de seccion
	 * y promociones para un determinado local
     * @param local
     * @return List MatrizSeccionEntity
     * @throws IrsPromocionException
     */
    public List getAllMatrizSeccion(int local) throws IrsPromocionException {
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getAllMatrizSeccion(local);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado getAllMatrizSeccion",e);
        }
    }

    /**
	 * Metodo para obtener todas las promociones de una seccion para un determinado local
     * @param seccion
     * @param local
     * @return MatrizSeccionEntity
     * @throws IrsPromocionException
     */
	public MatrizSeccionEntity getMatrizSeccion(int seccion, int local) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getMatrizSeccion(seccion, local);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado getMatrizBySeccion",e);
        }
    }

    /**
     * Agrega o actualiza matriz de promociones activas por promocion
     * @param matriz
     * @throws IrsPromocionException
     */
	public void updinsMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int reg = 0;
        try {
            reg = dao.updMatrizSeccion(matriz);
            if (reg > 0) {
                logger.debug("MatrizSeccion actualizada");
				}
			else{
                dao.insMatrizSeccion(matriz);
            }
        } catch (IrsPromocionDAOException ex_upd) {
			logger.warn("Problema al realizar Insert-Update de MatrizSeccion:" + ex_upd);
			throw new IrsPromocionException("Error no controlado al realizar Insert-Update de MatrizSeccion",ex_upd);
        }
    }

    /**
     * Agrega matriz de promociones activas por promocion
     * @param matriz
     * @throws IrsPromocionException
     */
	public void insMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.insMatrizSeccion(matriz);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al insertar MatrizSeccion",e);
        }
    }

    /**
     * Actualiza matriz de promociones activas por promocion
     * @param matriz
     * @return numero de registros actualizados
     * @throws IrsPromocionException
     */
	public int updMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            return dao.updMatrizSeccion(matriz);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al actualizar MatrizSeccion",e);
        }
    }

    /**
     * elimina matriz de promociones activas por promocion
     * @param matriz
     * @throws IrsPromocionException
     */
	public void delMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.delMatrizSeccion(matriz);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al eliminar MatrizSeccion",e);
        }
    }

    /**
	 * Metodo para obtener una promocion a partir del codigo
	 * para un determinado local
     * @param codigo
     * @param local
     * @return PromocionEntity
     * @throws IrsPromocionException
     */
	public PromocionEntity getPromocion(int codigo, int local) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getPromocion(codigo, local);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado getPromoById",e);
        }
    }

    /**
	 * Metodo update-insert para actualizar o insertar(si no existe) una promocion
     * @param promo
     * @throws IrsPromocionException
     */
	public void updinsPromocion(PromocionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int reg = 0;
        try {
            reg = dao.updPromocion(promo);
            if (reg > 0) {
                logger.debug("promocion actualizada");
			}
			else{
                dao.insPromocion(promo);
            }
        } catch (IrsPromocionDAOException ex_upd) {
			logger.warn("Problema al realizar Insert-Update de Promocion:" + ex_upd);
			throw new IrsPromocionException("Error no controlado al realizar Insert-Update de Promocion",ex_upd);
        }
    }

    /**
	 * Metodo update-insert para actualizar o insertar(si no existe) una promocion
     * @param promo
     * @throws IrsPromocionException
     */
	public void insPromocion(PromocionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.insPromocion(promo);
            logger.debug("promocion insertada");
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al insertar Promocion",e);
        }
    }

    /**
     * Metodo para actualizar una promocion
     * @param promo
     * @return numero de registros actualizados
     * @throws IrsPromocionException
     */
    public int updPromocion(PromocionEntity promo) throws IrsPromocionException {
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            return dao.updPromocion(promo);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al Eliminar Promocion",e);
        }
    }

    /**
     * Metodo para borrar una promocion
     * @param promo
     * @throws IrsPromocionException
     */
	public void delPromocion(PromocionEntity promo) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO(); 
        try {
            dao.delPromocion(promo);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al Eliminar Promocion",e);
        }
    }

    /**
     * Obtiene un producto promocion con el codigo de barras y el local
     * @param ean13
     * @param id_local
     * @return ProductoPromosEntity
     * @throws IrsPromocionException
     */
	public ProductoPromosEntity getPromocionProducto(String ean13, int id_local) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int id_producto = 0;
        try {
            //revisa si existe el id_producto con el ean13
            id_producto = dao.getProductoBO(ean13);
			logger.debug("id_producto encontado con el ean13=["+ean13+"] es :"+id_producto);
            if (id_producto <= 0) {
				throw new IrsPromocionException("No existe un producto con el ean13"+ean13);
            }

            return dao.getPromocionProducto(id_producto, id_local);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al actualizar PromocionProducto",e);
        }

    }

    /**
     * Agrega o actualiza la referencia de la promocion para el producto
     * @param producto
     * @throws IrsPromocionException
     */
	public void updinsPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int id_producto = 0;
        int reg = 0;
        try {

            //revisa si existe el id_producto
            //si no existe lo consulta con el ean13
            //setea el id_producto en el entity
            if (producto.getId_producto() == 0) {
                id_producto = dao.getProductoBO(producto.getEan13());
				logger.debug("id_producto encontado con el ean13=["+producto.getEan13()+"] es :"+id_producto);
                if (id_producto <= 0) {
				throw new IrsPromocionException("No existe un producto con el ean13"+producto.getEan13());
                }
                producto.setId_producto(id_producto);
            }
            reg = dao.updPromocionProducto(producto);
            if (reg > 0) {
                logger.debug("PromocionProducto actualizada");
			}
			else{
                dao.insPromocionProducto(producto);
            }

        } catch (IrsPromocionDAOException ex_upd) {
			logger.warn("Problema al realizar Insert-Update de PromocionProducto:" + ex_upd);
			throw new IrsPromocionException("Error no controlado al realizar Insert-Update de PromocionProducto",ex_upd);
        }
    }

    /**
     * Agrega o actualiza la referencia de la promocion para el producto
     * @param producto
     * @throws IrsPromocionException
     */
	public void insPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int id_producto = 0;
        try {

            //revisa si existe el id_producto
            //si no existe lo consulta con el ean13
            //setea el id_producto en el entity
            if (producto.getId_producto() == 0) {
                id_producto = dao.getProductoBO(producto.getEan13());
				logger.debug("id_producto encontado con el ean13=["+producto.getEan13()+"] es :"+id_producto);
                if (id_producto <= 0) {
				throw new IrsPromocionException("No existe un producto con el ean13"+producto.getEan13());
                }
                producto.setId_producto(id_producto);
            }
            dao.insPromocionProducto(producto);
            logger.debug("PromocionProducto insertada");
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al insertar PromocionProducto",e);
        }
    }

    /**
     * Agrega o actualiza la referencia de la promocion para el producto
     * @param producto
     * @return numero de registros actualizados
     * @throws IrsPromocionException
     */
	public int updPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int id_producto = 0;
        try {

            //revisa si existe el id_producto
            //si no existe lo consulta con el ean13
            //setea el id_producto en el entity
            if (producto.getId_producto() == 0) {
                id_producto = dao.getProductoBO(producto.getEan13());
				logger.debug("id_producto encontado con el ean13=["+producto.getEan13()+"] es :"+id_producto);
                if (id_producto <= 0) {
				throw new IrsPromocionException("No existe un producto con el ean13"+producto.getEan13());
                }
                producto.setId_producto(id_producto);
            }
            return dao.updPromocionProducto(producto);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al actualizar PromocionProducto",e);
        }
    }

    /**
     * Elimina la referencia de la promocion para el producto
     * @param producto
     * @return numero de registros actualizados
     * @throws IrsPromocionException
     */
	public void delPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        int id_producto = 0;
        try {

            //revisa si existe el id_producto
            //si no existe lo consulta con el ean13
            //setea el id_producto en el entity
            if (producto.getId_producto() == 0) {
                id_producto = dao.getProductoBO(producto.getEan13());
				logger.debug("id_producto encontado con el ean13=["+producto.getEan13()+"] es :"+id_producto);
                if (id_producto <= 0) {
				throw new IrsPromocionException("No existe un producto con el ean13"+producto.getEan13());
                }
                producto.setId_producto(id_producto);
            }
            dao.delPromocionProducto(producto);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al eliminar PromocionProducto",e);
        }
    }

    /**
	 * Despublica promocion del producto quitando la referencia de la promoción para el producto
	 * en la posicion requerida. 
     * @param id_local
     * @param ean13
     * @param pos
     * @throws IrsPromocionException
	 
	public void despPromocionProducto(int id_local, String ean13, int pos) throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
		int id_producto =-1;
		try {
			//reemplaza a dao.delPromocionProducto(producto);
			
			if ((ean13==null) || (!ean13.equals("")) || (id_local<=0)|| (pos<=0)){				
				throw new IrsPromocionException("Los parametros de entrada son incorrectos");
			}
			//busca el id_producto			
			id_producto =dao.getProductoBO(ean13);	
			
			if (id_producto<=0){
				throw new IrsPromocionException("el producto no existe para el ean13="+ean13);
			}

			//recoge datos id_producto y id_local y pos como obligatorios, si no lanza una ex
			logger.debug("id_local="+id_local+" id_producto="+id_producto+" pos="+pos);			
			
			
			//busca el producto del local y obtiene sus datos
			ProductoPromosEntity nprod = new ProductoPromosEntity(); 
			nprod = dao.getPromocionProducto(id_producto, id_local);			
			
			//verifica cual promocion hay que eliminar
			if (pos==1){		nprod.setPromo1(0);		}
			else if(pos==2){	nprod.setPromo2(0);		}
			else if(pos==3){	nprod.setPromo3(0);		}
			else{
				logger.error("parametro posicion incorrecto pos="+pos);
				throw new IrsPromocionException("parametro posicion incorrecto pos="+pos);
			}
			//actualiza el registro			
			dao.updPromocionProducto(nprod);
			
		} catch (IrsPromocionDAOException e) {
			logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al Eliminar PromocionProducto",e);
		}
	}
     */

    /**
	 * Obtiene Local BO de jumbo.cl con un id_local del BackOffice de Promociones 
     * @param local_sap
     * @return LocalBoEntity
     * @throws IrsPromocionException
     */
	public LocalBoEntity getLocalBySap(String local_sap)throws IrsPromocionException{
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getLocalBySap(local_sap);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al Buscar Local BO",e);
        }
    }

    /**
	 * Obtiene id_producto del BO de jumbo.cl con un codigo SAP y Unidad de Medida 
     * @param ean13
     * @return id_producto del backoffice de jumbo.cl
     * @throws IrsPromocionException
     */
    public int getProductoBO(String ean13) throws IrsPromocionException {
		JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getProductoBO(ean13);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
			throw new IrsPromocionException("Error no controlado al Buscar Local BO",e);
        }
    }

	//[20121113avc
    public boolean isAfectoDescColaborador(int seccion) throws IrsPromocionException {
        JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory
                .getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.isAfectoDescColaborador(seccion);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
            throw new IrsPromocionException(
                    "Error no controlado en isAfectoDescColaborador", e);
        }
    }
    
    /**
     * @param rut_cliente
     * @return
     * @throws IrsPromocionException
     */
    public long getComprasAcumuladas(long rut) throws IrsPromocionException {
        JdbcIrsPromocionesDAO dao = (JdbcIrsPromocionesDAO) DAOFactory
        .getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
        try {
            return dao.getComprasAcumuladas(rut);
        } catch (IrsPromocionDAOException e) {
            logger.warn("Problema :" + e);
            throw new IrsPromocionException(
                    "Error no controlado en getComprasAcumuladas", e);
        }
    }    
    //]20121113avc
}
