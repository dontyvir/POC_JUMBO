package cl.bbr.boc.actions;

import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.contenidos.ctr.ProductosCtrl;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.productos.ctr.ProductosCTR;
import cl.bbr.log.Logging;

public class ProductosDelegate {
	
	private Logging logger = new Logging(this);
	
	private cl.bbr.jumbocl.productos.service.ProductosService proService;
	
	public ProductosDelegate(){}
	
	public ProductosDelegate(cl.bbr.jumbocl.productos.service.ProductosService instance){
		this.proService = instance;
	}
	
    public boolean guardarLeySuperOcho(NutricionalLeySupeDTO dto)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.guardarLeySuperOcho(dto);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con controles de Productos ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [guardarLeySuperOcho], ", ex);
            //throw new SystemException(ex);
        }
        return false;
    }
    
    public List listaLeySuperOcho(int idproductoFO)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.listaLeySuperOcho(idproductoFO);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con [guardarLeySuperOcho] ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [guardarLeySuperOcho], ", ex);
            //throw new SystemException(ex);
        }
        return null;
    }
    
    public List listaFichaNutricional(int idproductoFO)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.listaFichaNutricional(idproductoFO);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con [listaFichaNutricional] ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [listaFichaNutricional], ", ex);
            //throw new SystemException(ex);
        }
        return null;
    }
    
    public boolean guardarFichaNutricional(Map mapa, int idProductoFO)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.guardarFichaNutricional(mapa, idProductoFO);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con controles de Productos [guardarFichaNutricional] ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [guardarFichaNutricional], ", ex);
            //throw new SystemException(ex);
        }
        return false;
    }
    
    public boolean guardarTipoFicha(List listTipoFicha, long idProductoFO)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.guardarTipoFicha(listTipoFicha, idProductoFO);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con controles de Productos [guardarTipoFicha] ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [guardarTipoFicha], ", ex);
            //throw new SystemException(ex);
        }
        return false;
    }
    
    public List obtenerTipoFicha(long idProductoFO)  {
        try {        	
            ProductosCtrl productos = new ProductosCtrl();
    	    try{
    	    	return productos.obtenerTipoFicha(idProductoFO);
    	    }catch(ProductosException ex){
    	        logger.debug(" Problemas con controles de Productos [obtenerTipoFicha] ");
    	        throw new ServiceException(ex);
    	    }                    	
        } catch (Exception ex) {
            logger.error("Problema ProductosDelegate [obtenerTipoFicha], ", ex);
            //throw new SystemException(ex);
        }
        return null;
    }
   
}
