package cl.bbr.cupondscto.ctrl;

import java.util.List;

import cl.bbr.cupondscto.dao.CuponDsctoDAO;
import cl.bbr.cupondscto.dao.DAOFactory;
import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.cupondscto.exception.CuponDsctoException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

public class CuponDsctoCtrl {
	
	private Logging logger = new Logging(this);
	
	/**
	 * Busca cupon de descuento en base al codigo
	 * @param codigo
	 * @return
	 * @throws DAOException
	 */
	public CuponDsctoDTO getCuponDscto(String codigo) throws ServiceException, CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.getCuponDscto(codigo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error (Ctrl) getMontoLimite: ", e);
			throw new ServiceException(e);
		}
	}
	
	public CuponDsctoDTO getCuponDsctoById(int idCupon) throws ServiceException, CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.getCuponDsctoById(idCupon);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error (Ctrl) getCuponDsctoById: ", e);
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Verifica que cupon es para el cliente
	 * @param rut
	 * @param id_cupon
	 * @return
	 * @throws DAOException
	 */
	public boolean isCuponForRut(long rut, long id_cupon) throws CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.isCuponForRut(rut, id_cupon);
		} catch (Exception e) {
			logger.error("Error (Ctrl) isCuponForRut: ", e);
			throw new CuponDsctoException(e);
		}
	}
	
	/**
	 * 
	 * @param id_cupon
	 * @param tipo
	 * @return
	 * @throws CuponDsctoException
	 */
	public List getProdsCupon (long id_cupon, String tipo) throws CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.getProdsCupon(id_cupon, tipo);
		} catch (Exception e) {
			logger.error("Error (Ctrl) getProdsCupon: ", e);
			throw new CuponDsctoException(e);
		}
	}

	/**
	 * 
	 * @param idCupon
	 * @return
	 * @throws CuponDsctoException
	 */
	public boolean dsctaStockCupon (long idCupon) throws CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.dsctaStockCupon(idCupon);
		} catch (Exception e) {
			logger.error("Error (Ctrl) dsctaStockCupon: ", e);
			throw new CuponDsctoException(e);
		}
	}
	
	
	/**
	 * 
	 * @param idCupon
	 * @param idPedido
	 * @return
	 * @throws CuponDsctoException
	 */
	public boolean setIdCuponIdPedido ( long idCupon, long idPedido ) throws CuponDsctoException {
		
		CuponDsctoDAO cdDAO = ( CuponDsctoDAO ) DAOFactory.getDAOFactory( DAOFactory.JDBC ).setIdCuponIdPedido();
		
		try {
		
			return cdDAO.setIdCuponIdPedido( idCupon, idPedido );
		
		} catch ( Exception e ) {
		
			logger.error( "Error (Ctrl) setIdCuponIdPedido: ", e );
			throw new CuponDsctoException( e );
		
		}
	
	}
	
	//-----Carro Abandonado
	
	/**
	 * Busca cupon de carro abandonado enviado por email en base a id
	 * @param codigo
	 * @return
	 * @throws DAOException
	 */
	public CarroAbandonadoDTO getCuponCarroAbandonado(int codigo) throws CuponDsctoException {
		CuponDsctoDAO cdDAO = (CuponDsctoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCuponDsctoDAO();
		try {
			return cdDAO.getCuponCarroAbandonado(codigo);
		} catch (Exception e) {
			logger.error("Error (Ctrl) getMontoLimite: ", e);
			throw new CuponDsctoException(e);
		}
	}

	//-----Fin Carro abandonado
	
}
