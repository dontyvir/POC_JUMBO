package cl.bbr.jumbocl.pedidos.ctrl;


import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPoligonosDAO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonosZonaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosException;
import cl.bbr.jumbocl.pedidos.exceptions.SectorPickingDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public class PoligonosCtrl {
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Agrega un sector de picking
	 * 
	 * @param sector
	 * @throws SectorPickingDAOException
	 * @throws SystemException
	 */
	public boolean verificaNumPoligono(int id_comuna, int num_pol) throws SystemException{
		logger.debug("en ctrl: doAddSectorPicking");
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
		boolean existe = false;
		try{
			existe = dao.verificaNumPoligono(id_comuna, num_pol);
		}catch(PoligonosDAOException ex){
			logger.debug("en el catch");
			throw new SystemException("Error no controlado al consultar un poligono",ex);		
		}catch(Exception ex){
			logger.debug("error no controlado al consultar un poligono: "+ex.getMessage());
		}
		return existe;
	}	
	
	

	public List getPoligonosXComuna(long id_comuna)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonosXComuna(id_comuna);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public List getPoligonosXComunaAll(long id_comuna)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonosXComunaAll(id_comuna);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public List getPoligonosXComunaSinZona(long id_comuna)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonosXComunaSinZona(id_comuna);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public List getComunasConPoligonosSinZona()
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getComunasConPoligonosSinZona();
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public List getPoligonosXZona(long id_zona)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonosXZona(id_zona);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	


	public PoligonoDTO getPoligonoById(long id_poligono)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonoById(id_poligono);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public PoligonoDTO getPoligonoByIdDireccion(long id_direccion)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getPoligonoByIdDireccion(id_direccion);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	

	public long getCantidadPoligonoDifBaseyRetiroLocal(long id_comuna)
		throws PoligonosException, SystemException{
		
		JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

		try {
			return dao.getCantidadPoligonoDifBaseyRetiroLocal(id_comuna);
		} catch (PoligonosDAOException e) {
			logger.error("error en getZonasxComuna");
			throw new SystemException(e);
		}
	}
	


    public long AddPoligono(PoligonoDTO pol) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.AddPoligono(pol);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }
    public long AddPoligonoWithZona(PoligonoDTO pol) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.AddPoligonoWithZona(pol);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }


    public boolean ModPoligono(PoligonoDTO pol) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.ModPoligono(pol);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }


    public int VerificaPoligonoEnDirecciones(long id_poligono) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.VerificaPoligonoEnDirecciones(id_poligono);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public boolean DelPoligono(long id_poligono) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.DelPoligono(id_poligono);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }


    public int getLocalByPoligono(int id_poligono) throws PoligonosException, SystemException {
        JdbcPoligonosDAO dao = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();
        try {
            return dao.getLocalByPoligono(id_poligono);
        } catch (PoligonosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

//

    public void doActualizaPoligonosZonaDespacho(PoligonosZonaDTO dto) throws PoligonosException, SystemException {
		List lista = new ArrayList();
		long[] arr_pol = null;
		if (dto.getPoligonos() != null){
		    arr_pol = dto.getPoligonos();
		    logger.debug("size: " + arr_pol.length);
		}
		
        JdbcPoligonosDAO dao2 = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();

//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

//		 Marcamos los dao's con la transacción
		try {
			dao2.setTrx(trx1);
		} catch (PoligonosDAOException e2) {
			logger.error("Error al asignar transacción al dao ZonasDespacho");
			throw new SystemException("Error al asignar transacción al dao ZonasDespacho");
		}	

		
		try {
			// obtenemos las comunas actuales de la zona de despacho
		    lista = dao2.getPoligonosXZona(dto.getId_zona());
			
			if (arr_pol == null){
			    for( int i=0; i<lista.size(); i++ ){
			        PoligonoxComunaDTO pxc = (PoligonoxComunaDTO)lista.get(i);
			        dao2.DelPoligonoPorZona(pxc.getId_poligono(), dto.getId_zona());
			    }
			}else{
				// iteramos para borrar las comunas que se sacaron
				for( int i=0; i<lista.size(); i++ ){
				    PoligonoxComunaDTO pxc = (PoligonoxComunaDTO)lista.get(i);
					
					boolean found = false;
					
					// iteramos las comunas seleccionadas
					for( int j=0; j<arr_pol.length; j++ ){
						if( pxc.getId_poligono() == arr_pol[j] ){
							found = true;
						}
					}
					
					if (!found){
						// borramos el registro
					    dao2.DelPoligonoPorZona(pxc.getId_poligono(), dto.getId_zona());
					}
				}
				
				// iteramos para insertar las comunas que se agregaron
				for( int i=0; i<arr_pol.length; i++ ){
					
					boolean found = false;
					long id_poligono = 0L;
					for( int j=0; j<lista.size(); j++ ){
					    PoligonoxComunaDTO pxc = (PoligonoxComunaDTO)lista.get(j);
					    id_poligono = pxc.getId_poligono();
						if( arr_pol[i] == id_poligono ){
							found = true;
						}
					}
					
					if (!found){
						// agregamos el registro
					    dao2.AddPoligonoPorZona(arr_pol[i], dto.getId_zona());
					}
				}
			}
		} catch (PoligonosDAOException e) {
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
    }

	
	/**
	 * Actualiza un sector de picking
	 *	 
	 * @param sector
	 * @throws SectorPickingDAOException
	 * @throws SystemException
	 */
	/*public void doActualizaSectorPicking(SectorLocalDTO sector)
	throws SectorPickingDAOException, SystemException{
			
			JdbcSectorPickingDAO dao = (JdbcSectorPickingDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSectorPickingDAO();
			try{
				dao.doActualizaSectorPicking(sector);
			}catch(SectorPickingDAOException ex){
				if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
					throw new SectorPickingDAOException(Constantes._EX_ID_SECTOR_INVALIDO);
				}
				else
					throw new SystemException("Error no controlado al insertar un sector de picking",ex);
			}
	}*/
}
