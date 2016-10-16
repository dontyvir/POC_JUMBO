package cl.bbr.jumbocl.pedidos.ctrl;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcComunasDAO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioException;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por comunas y busqueda en base a criterios. 
 * Los resultados son listados de comunas y zonas.
 * 
 * @author BBR
 *
 */
public class ComunasCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Listado de Comunas
	 * 
	 * @return List of ComunaDTO's
	 * @throws ComunasException
	 * @throws SystemException, en caso exista error de sistema. 
	 */
	public List getComunasAll()
		throws ComunasException, SystemException{
		
		JdbcComunasDAO dao = (JdbcComunasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getComunasDAO();

		try {
			return dao.getComunasAll();
		} catch (ComunasDAOException e) {
			logger.error("error en getComunasAll");
			throw new SystemException(e);
		}
	
	}
		

	/**
	 * Obtiene listado de Zonas de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of ZonaxComunaDTO's
	 * @throws SystemException 
	 * @throws CalendarioException
	 */
	public List getZonasxComuna(long id_comuna)
		throws ComunasException, SystemException{
		
		JdbcComunasDAO dao = (JdbcComunasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getComunasDAO();

		try {
			return dao.getZonasxComuna(id_comuna);
		} catch (ComunasDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	
	}
	
	
	/**
	 * Actualiza el orden de una relación comuna-zona.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  procModZonaxComuna List of ProcModZonaxComuna's
	 * 
	 * @throws SystemException 
	 * @throws CalendarioException
	 * 
	 * 
	 */
	/*public void doActualizaOrdenZonaxComuna(List procModZonaxComuna)
		throws ComunasException, SystemException{
		
		JdbcComunasDAO dao = (JdbcComunasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getComunasDAO();
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (DAOException e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (ComunasDAOException e2) {
			logger.error("Error al asignar transacción al dao Comunas");
			throw new SystemException("Error al asignar transacción al dao Comunas");
		}	
		
		
		try {
			for(int i=0; i<procModZonaxComuna.size(); i++){
				ProcModZonaxComuna zxc = (ProcModZonaxComuna)procModZonaxComuna.get(i);
				dao.doActualizaOrdenZonaxComuna( zxc.getId_zona(), zxc.getId_comuna(), zxc.getOrden() );
			}
		} catch (ComunasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.error("error al actualizar orden de comunas");
			throw new SystemException(e);
		}
	
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
	}*/
	
	
	
	
}
