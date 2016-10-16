package cl.bbr.jumbocl.pedidos.ctrl;

import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcLocalDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcZonasDespachoDAO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.LocalException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por locales y busqueda en base a criterios. 
 * Los resultados son listados de locales.
 * 
 * @author BBR 
 *
 */
public class LocalCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	/**
	 * Retorna un listado de Sectores de Picking
	 * 
	 * @return List of SectorLocalDTO's
	 * @throws LocalException 
	 * @throws SystemException 
	 */
	public List getSectores() throws LocalException, SystemException{
	
		JdbcLocalDAO dao = (JdbcLocalDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getLocalDAO();
		
		try {
			return dao.getSectores();	
		} catch (LocalDAOException e) {
			throw new SystemException(e);
		}
				
	}
	
	
	/**
	 * Retorna listado con locales creados en el sistema
	 * 
	 * @return List of LocalDTO's
	 * @throws LocalException 
	 * @throws SystemException 
	 */
	public List getLocalesAll() throws LocalException, SystemException{
		
		JdbcLocalDAO dao = (JdbcLocalDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getLocalDAO();

		try {
			return dao.getLocalesAll();
		} catch (LocalDAOException e) {
			logger.error("error al obtener listado de locales");
			throw new SystemException(e);
		}
	}
	
	/**
	 * Retorna listado con locales creados en el sistema
	 * 
	 * @return List of LocalDTO's
	 * @throws LocalException 
	 * @throws SystemException 
	 */
	public LocalDTO getLocalSapByCodLocalPos(int cod_local_pos) throws LocalException, SystemException{
		
		JdbcLocalDAO dao = (JdbcLocalDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getLocalDAO();

		try {
			return dao.getLocalSapByCodLocalPos(cod_local_pos);
		} catch (LocalDAOException e) {
			logger.error("error al obtener listado de locales");
			throw new SystemException(e);
		}
	}

	/**
	 * Retorna listado con locales creados en el sistema
	 * 
	 * @return List of LocalDTO's
	 * @throws LocalException 
	 * @throws SystemException 
	 */
	public LocalDTO getLocalPromoByCodLocalSap(String cod_local_sap) throws LocalException, SystemException{
		
		JdbcLocalDAO dao = (JdbcLocalDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getLocalDAO();

		try {
			return dao.getLocalPromoByCodLocalSap(cod_local_sap);
		} catch (LocalDAOException e) {
			logger.error("error al obtener listado de locales");
			throw new SystemException(e);
		}
	}
	

	
	/* ********************** Zonas de Despacho	 ******************************* */
	
	/**
	 * Retorna un listado con las Zonas de Despacho de un Local
	 * 
	 * @param  id_local long 
	 * @return List ZonaDTO
	 * @throws SystemException 
	 */
	public List getZonasLocal(long id_local)
		throws SystemException {

		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		
		try {
			return dao.getZonasDespachoLocal(id_local);		
		} catch (ZonasDespachoDAOException e) {
			throw new SystemException(e);			
		}	

	}
	
	/**
	 * Obtiene el detalle de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return ZonaDTO
	 * @throws SystemException 
	 */	
	public ZonaDTO getZonaDespacho(long id_zona) throws SystemException {
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		try {
			return dao.getZonaDespachoById(id_zona);
		} catch (ZonasDespachoDAOException e) {
			throw new SystemException(e);			
		}
	}
	
	
	
	
	/**
	 * Agrega una zona de despacho para un local
	 * 
	 * @param  zona ZonaDTO 
	 * @return long id_zona insertada
	 * @throws LocalException, en caso que falten alguno de los parámetros de ZonaDTO.
	 * @throws SystemException 
	 */
	public long doAgregaZonaDespacho(ZonaDTO zona)
		throws LocalException, SystemException {
		
		// Validamos que vengan los parámetros requeridos
		if ( zona.getId_local() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getNombre() == "" )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_alta() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_media() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_baja() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_economica() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_express() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		
		if ( zona.getEstado_descuento_cat() > 0 ){
		    if (zona.getMonto_descuento_cat() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}
		if ( zona.getEstado_descuento_tbk() > 0 ){
		    if (zona.getMonto_descuento_tbk() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}
		if ( zona.getEstado_descuento_par() > 0 ){
		    if (zona.getMonto_descuento_par() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}

		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		
		try {
			return dao.doAgregaZonaDespacho(zona);
		} 
		catch (ZonasDespachoDAOException ex) {
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new LocalException( Constantes._EX_LOCAL_ID_INVALIDO, ex );
			}
			throw new SystemException(ex);
		}
		
	}
	public void doModZonaDespachoOrden(int idZona, int orden) throws LocalException, SystemException {
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		try{
			dao.doModZonaDespachoOrden(idZona, orden);
		}catch(ZonasDespachoDAOException ex){
			if(ex.getMessage().equals(Constantes._EX_ZONA_ID_INVALIDA)){
				throw new LocalException(Constantes._EX_ZONA_ID_INVALIDA, ex);
			}
			if(ex.getMessage().equals(Constantes._EX_LOCAL_ID_INVALIDO)){
				throw new LocalException(Constantes._EX_LOCAL_ID_INVALIDO, ex);
			}
			throw new SystemException(ex);	
		}
	}	

	/**
	 * Modifica una zona de despacho para un local
	 * 
	 * @param  zona ZonaDTO 
	 * @throws LocalException, en caso que falten alguno de los parámetros de ZonaDTO.
	 * @throws SystemException 
	 */
	public void doModZonaDespacho(ZonaDTO zona)
		throws LocalException, SystemException {
		
		// Validamos que vengan los parámetros requeridos
		if ( zona.getId_local() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getId_zona() <= 0)
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getNombre() == "" )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_alta() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_media() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_normal_baja() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_economica() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		if ( zona.getTarifa_express() <= 0 )
			throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);

		if ( zona.getEstado_descuento_cat() > 0 ){
		    if (zona.getMonto_descuento_cat() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}
		if ( zona.getEstado_descuento_tbk() > 0 ){
		    if (zona.getMonto_descuento_tbk() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}
		if ( zona.getEstado_descuento_par() > 0 ){
		    if (zona.getMonto_descuento_par() <= 0)
		        throw new LocalException(Constantes.PARAMETRO_OBLIGATORIO);
		}
		
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		
		try {
			dao.doModZonaDespacho(zona);
		} catch (ZonasDespachoDAOException ex) {
			if(ex.getMessage().equals(Constantes._EX_ZONA_ID_INVALIDA)){
				throw new LocalException(Constantes._EX_ZONA_ID_INVALIDA, ex);
			}
			if(ex.getMessage().equals(Constantes._EX_LOCAL_ID_INVALIDO)){
				throw new LocalException(Constantes._EX_LOCAL_ID_INVALIDO, ex);
			}
			throw new SystemException(ex);	
		}
	}
	
	
	/**
	 * Obtiene listado de comunas de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return List of ComunaDTO's
	 * @throws LocalException
	 * @throws SystemException 
	 */
	public List getComunasByIdZonaDespacho(long id_zona)
		throws LocalException, SystemException {
		
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();

		try {
			return dao.getComunasByIdZonaDespacho(id_zona);
		} catch (LocalDAOException e) {
			throw new SystemException(e);	
		}
		
	
	}

	
	/**
	 * Obtiene listado de comunas de un local
	 * 
	 * @param  id_local long 
	 * @return List of ComunaDTO's
	 * @throws LocalException
	 * @throws SystemException 
	 */
	public List getComunasLocal(long id_local)
		throws LocalException, SystemException {
		
		JdbcLocalDAO dao = (JdbcLocalDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getLocalDAO();

		try {
			return dao.getComunasLocal(id_local);
		} catch (LocalDAOException e) {
			throw new SystemException(e);	
		}
		
	
	}
	
	
	
	/**
	 * Obtiene listado de todas las comunas
	 * 
	 * @return List of ComunaDTO's
	 * @throws LocalException
	 * @throws SystemException 
	 */
	public List getComunasAll()
		throws LocalException, SystemException {
		
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();

		try {
			return dao.getComunasAll();
		} catch (LocalDAOException e) {
			throw new SystemException(e);
		}
		
		
	}
	
	
	/**
	 * Actualiza comunas asociadas a una zona de despacho.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  dto ComunasZonaDTO 
	 * 
	 * @throws LocalException
	 * @throws SystemException
	 * 
	 */
	
	/*public void doActualizaComunasZonaDespacho(ComunasZonaDTO dto)
		throws LocalException, SystemException {
			
		List lista = new ArrayList();
		long[] arr_com = dto.getComunas();	
	
		logger.debug("size: " + arr_com.length);
		
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		
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
		} catch (ZonasDespachoDAOException e2) {
			logger.error("Error al asignar transacción al dao ZonasDespacho");
			throw new SystemException("Error al asignar transacción al dao ZonasDespacho");
		}	

		
		try {
			// obtenemos las comunas actuales de la zona de despacho
			lista = dao.getComunasByIdZonaDespacho( dto.getId_zona() );
			
			// iteramos para borrar las comunas que se sacaron
			for( int i=0; i<lista.size(); i++ ){
				ComunaDTO com1 = (ComunaDTO)lista.get(i);
				
				boolean found = false;
				
				// iteramos las comunas seleccionadas
				for( int j=0; j<arr_com.length; j++ ){
					if( com1.getId_comuna() == arr_com[j] ){
						found = true;
					}
				}
				
				if (!found){
					// borramos el registro
					dao.doBorraComunaZonaDespacho( dto.getId_zona() , com1.getId_comuna() );
				}
				
			}
			
			// iteramos para insertar las comunas que se agregaron
			for( int i=0; i<arr_com.length; i++ ){
				
				boolean found = false;
				
				for( int j=0; j<lista.size(); j++ ){
					ComunaDTO com1 = (ComunaDTO)lista.get(j);
					
					if( arr_com[i] == com1.getId_comuna() ){
						found = true;
					}
					
				}
				
				if (!found){
					// agregamos el registro
					dao.doAgregaComunaZonaDespacho( dto.getId_zona() , arr_com[i], 99 );
				}
				
			}
			
		
		} catch (ZonasDespachoDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new SystemException(e);
		} catch (LocalDAOException e) {
			// rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new SystemException(e);
		}		
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
	}*/
}
