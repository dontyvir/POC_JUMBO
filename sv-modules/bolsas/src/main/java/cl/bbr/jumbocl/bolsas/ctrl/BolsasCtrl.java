package cl.bbr.jumbocl.bolsas.ctrl;

import java.util.List;

import cl.bbr.jumbocl.bolsas.dao.DAOFactory;
import cl.bbr.jumbocl.bolsas.dao.JdbcBolsasDAO;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.bolsas.exceptions.BolsasDAOException;
import cl.bbr.jumbocl.bolsas.exceptions.BolsasException;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por gestion de casos
 * 
 * @author imoyano
 *  
 */
public class BolsasCtrl {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    /**
     * @return
     */
    public List getStockBolsasRegalo(String cod_sucursal) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getStockBolsasRegalo(cod_sucursal);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    
    public List getBitacoraBolsasRegalo(String cod_sucursal) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getBitacoraBolsasRegalo(cod_sucursal);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }


    public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal, int stock) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public void insertarRegistroBitacoraBolsas(String desc_operacion, String usuario, String cod_sucursal) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.insertarRegistroBitacoraBolsas(desc_operacion, usuario, cod_sucursal);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public void asignacionBolsaCliente(String rut_cliente, String cod_bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.asignacionBolsaCliente(rut_cliente, cod_bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public void crearBolsaRegalo(BolsaDTO bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.crearBolsaRegalo(bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }

    public void activaBolsa() throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.activaBolsa();
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public void eliminarBolsaRegalo(BolsaDTO bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.eliminarBolsaRegalo(bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public void actualizarBolsa(BolsaDTO bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            dao.actualizarBolsa(bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public BolsaDTO getBolsaRegalo(String cod_bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getBolsaRegalo(cod_bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public List getAsignacionesBolsas() throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getAsignacionesBolsas();
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public boolean validaCodSap(String cod_sap) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.validaCodSap(cod_sap);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public boolean validaCodBolsa(String cod_bolsa) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.validaCodBolsa(cod_bolsa);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public boolean validaCodBolsaSap(String cod_bolsa, String cod_sap) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.validaCodBolsaSap(cod_bolsa, cod_sap);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public long getIdProdBO(String cod_sap) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getIdProdBO(cod_sap);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public long getIdProdFO(String cod_sap) throws BolsasException {
        JdbcBolsasDAO dao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsasDAO();
        try {
            return dao.getIdProdFO(cod_sap);
        } catch (BolsasDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }
    
    public ProductoEntity getProductoByIdProd(long idProd) throws BolsasException, ProductosException {
    	JdbcProductosDAO productosDao = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();
        try {
            return productosDao.getProductoById(idProd);
        } catch (ProductosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new BolsasException(ex);
        }
    }

 
}
