package cl.bbr.jumbocl.clientes.ctrl;

import java.util.ArrayList;
import java.util.List;
 
import cl.bbr.jumbocl.clientes.dao.DAOFactory;
import cl.bbr.jumbocl.clientes.dao.JdbcClientesDAO;
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.clientes.exceptions.DireccionesException;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.model.ZonaEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.clientes.dto.EstadoDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por direcciones y busqueda en base a criterios. 
 * Los resultados son listados de direcciones o datos de una direccion.
 * 
 * @author BBR
 *
 */
public class DireccionesCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Entrega una direccion dado su id de direccion
	 * 
	 * @param  id_direccion long
	 * @return DireccionesDTO
	 * @throws DireccionesException	 
	 */

	public DireccionesDTO getDireccionByIdDir(long id_direccion) throws DireccionesException {
		
		DireccionesDTO dirDto = new DireccionesDTO();
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			DireccionEntity dir = clientesDAO.getDireccionById(id_direccion);
			logger.debug("paso1");
			TipoCalleDTO tipCllDto = new TipoCalleDTO(dir.getTipo_calle().getId().longValue(),
					dir.getTipo_calle().getNombre(),dir.getTipo_calle().getEstado());
			logger.debug("paso2");
			String fecha = "";
			if(dir.getFec_crea()!=null)
				fecha = dir.getFec_crea().toString();
			logger.debug("paso3");
			dirDto = new DireccionesDTO(dir.getId().intValue(), dir.getCli_id().longValue(),
						dir.getAlias(),	dir.getCalle(), dir.getNom_comuna(), dir.getDepto(), dir.getComentarios(),
						dir.getEstado(), fecha,
						dir.getNumero(), dir.getNom_local(), dir.getNom_zona(), dir.getNom_tip_calle(), dir.getFnueva(), dir.getCuadrante(),
						dir.getLoc_cod().longValue(), dir.getCom_id().longValue(), dir.getZon_id().longValue(),
						tipCllDto, dir.getNom_region(), dir.getDir_conflictiva(), dir.getDir_conflictiva_comentario(),
						dir.getTipoPicking(), dir.getId_poligono());
		}catch(ClientesDAOException ex){
			logger.debug("Problema getDireccionByIdDir:"+ex);
			throw new DireccionesException(ex);
		}
		return dirDto;
	}
	
	/**
	 * Entrega el listado de direcciones en base a un id de cliente
	 * 
	 * @param  id_cliente long
	 * @return List de DireccionesDTO's
	 * @throws DireccionesException
	 */
	public List getDireccionesByIdCliente(long id_cliente)  throws DireccionesException {
		List dirs= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaDir = new ArrayList();
			listaDir = (List) clientesDAO.listadoDirecciones(id_cliente);
			DireccionEntity dir = null;
			String fecha = "";
			for (int i = 0; i < listaDir.size(); i++) {
				fecha ="";
				dir = null;
				dir = (DireccionEntity) listaDir.get(i);
				TipoCalleDTO tipCllDto = new TipoCalleDTO(dir.getTipo_calle().getId().longValue(),
						dir.getTipo_calle().getNombre(),dir.getTipo_calle().getEstado());
				if(dir.getFec_crea()!=null)
					fecha = dir.getFec_crea().toString();
				DireccionesDTO dirdto = new DireccionesDTO(dir.getId().intValue(),dir.getCli_id().longValue(),
						dir.getAlias(),	dir.getCalle(),dir.getNom_comuna(),dir.getDepto(),dir.getComentarios(),
						dir.getEstado(),fecha,
						dir.getNumero(),dir.getNom_local(),dir.getNom_tip_calle(),dir.getFnueva(), dir.getCuadrante(),
						dir.getLoc_cod().longValue(),dir.getCom_id().longValue(), tipCllDto, dir.getNom_region());
				dirs.add(dirdto);
				logger.debug("se coloca el dir:"+i);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema DireccionesCTRL:"+ex);
			throw new DireccionesException(ex);
		}
		return dirs;

	}

	/**
	 * Actualiza la información de la direccion, ingresada en procparam
	 * 
	 * @param  procparam ProcModAddrBookDTO
	 * @return boolean
	 * @throws DireccionesException
	 */
	public boolean setModAddrBook(ProcModAddrBookDTO procparam) throws DireccionesException {
		boolean resUpdate = false; 
		try{
			
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	

			resUpdate = clientesDAO.actualizaDireccionById(procparam);
			logger.debug("actualizo dir ok? "+resUpdate);
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema setModAddrBook:"+ex);
			throw new DireccionesException(ex);
		}
	    return resUpdate;
	}
	
	
	/**
	 * Obtiene una lista de estados correspondientes a las Direcciones
	 * 
	 * @return List de EstadoDTO's
	 * @throws DireccionesException
	 */	
	
	public  List getEstadosDirecciones() throws DireccionesException {
		List result= new ArrayList();
		try{
			logger.debug("en getEstadosDirecciones");
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) clientesDAO.getEstados("ALL","S"); 
			
			
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema getEstadosDirecciones:"+ex);
			throw new DireccionesException(ex);
		}
		
		return result;
	}

	/**
	 * Obtiene una listado de zonas
	 * 
	 * @return List de ZonaDTO's
	 * @throws DireccionesException
	 */	
	
	
	public List getZonas()  throws DireccionesException {
		List zons= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaZon = new ArrayList();
			listaZon = (List) clientesDAO.listadoZonas();
			ZonaEntity zon = null;
			for (int i = 0; i < listaZon.size(); i++) {
				zon = null;
				zon = (ZonaEntity) listaZon.get(i);
				ZonaDTO dto = new ZonaDTO(zon.getId_zona().longValue(), zon.getNombre(), zon.getDescrip(), 
				                          zon.getTarifa_normal_alta(), zon.getTarifa_normal_media(), 
				                          zon.getTarifa_normal_baja(), zon.getTarifa_economica(),
				                          zon.getTarifa_express(), zon.getEstado_tarifa_economica(),
				                          zon.getEstado_tarifa_express());
				zons.add(dto);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return zons;

	}
	
	
	public List getLocales()  throws DireccionesException {
		List locs= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaLoc = new ArrayList();
			listaLoc = (List) clientesDAO.listadoLocales();
			LocalEntity loc = null;
			for (int i = 0; i < listaLoc.size(); i++) {
				loc = null;
				loc = (LocalEntity) listaLoc.get(i);
				String fec_carga = Formatos.frmFechaHora(loc.getFec_carga_prec());
				LocalDTO locDto = new LocalDTO();
				locDto.setId_local(loc.getId_local().longValue());
				locDto.setCod_local(loc.getCod_local());
				locDto.setNom_local(loc.getNom_local());
				locDto.setOrden(loc.getOrden().intValue());
				locDto.setFec_carga_prec(fec_carga);
				if (loc.getCod_local_pos() != null){
				    locDto.setCod_local_pos(loc.getCod_local_pos().intValue());
				}
				locDto.setTipo_flujo(loc.getTipo_flujo());
				locDto.setCod_local_promocion(loc.getCod_local_promocion());
				locDto.setTipo_picking(loc.getTipo_picking());
				
				locDto.setRetirolocal(loc.getRetirolocal());
				locDto.setId_zona_retiro(loc.getId_zona_retiro());
				locs.add(locDto);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return locs;

	}
	
	/**
	 * Obtiene un listado de Locales
	 * 
	 * @return List de LocalDTO's
	 * @throws DireccionesException
	 */
	public List getLocal(String cod_sucursal)  throws DireccionesException {
		List locs= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaLoc = new ArrayList();
			listaLoc = (List) clientesDAO.listadoLocales();
			LocalEntity loc = null;
			for (int i = 0; i < listaLoc.size(); i++) {
				loc = null;
				loc = (LocalEntity) listaLoc.get(i);
				String fec_carga = Formatos.frmFechaHora(loc.getFec_carga_prec());
				LocalDTO locDto = new LocalDTO();
				locDto.setId_local(loc.getId_local().longValue());
				locDto.setCod_local(loc.getCod_local());
				locDto.setNom_local(loc.getNom_local());
				locDto.setOrden(loc.getOrden().intValue());
				locDto.setFec_carga_prec(fec_carga);
				if (loc.getCod_local_pos() != null){
				    locDto.setCod_local_pos(loc.getCod_local_pos().intValue());
				}
				locDto.setTipo_flujo(loc.getTipo_flujo());
				locDto.setCod_local_promocion(loc.getCod_local_promocion());
				locDto.setTipo_picking(loc.getTipo_picking());
				
				if(cod_sucursal.equalsIgnoreCase(loc.getId_local().longValue()+"")){
					locs.clear();
					locs.add(locDto);
					break;
				}else{
					locs.add(locDto);
				}
				
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return locs;

	}

	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List de ComunaDTO's
	 * @throws DireccionesException
	 */
	public List getComunas()  throws DireccionesException {
		List coms= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaCom = new ArrayList();
			listaCom = (List) clientesDAO.listadoComunas();
			ComunaEntity com = null;
			for (int i = 0; i < listaCom.size(); i++) {
				com = null;
				com = (ComunaEntity) listaCom.get(i);
				ComunasDTO comDto = new ComunasDTO(com.getId_comuna().longValue(), com.getId_region().longValue(),
						com.getNombre(), com.getReg_nombre());
				coms.add(comDto);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return coms;
	}

	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List de ComunaDTO's
	 * @throws DireccionesException
	 */
	public List getComunasConPoligonos()  throws DireccionesException {
		List coms= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaCom = new ArrayList();
			listaCom = (List) clientesDAO.getComunasConPoligonos();
			ComunaEntity com = null;
			for (int i = 0; i < listaCom.size(); i++) {
				com = null;
				com = (ComunaEntity) listaCom.get(i);
				ComunasDTO comDto = new ComunasDTO(com.getId_comuna().longValue(), com.getId_region().longValue(),
						com.getNombre(), com.getReg_nombre());
				coms.add(comDto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return coms;
	}


	
	public String getComunaById(long id_comuna)  throws DireccionesException {
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			return clientesDAO.getComunaById(id_comuna);
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
	}


	/**
	 * Obtiene un listado de comunas con Zona Asignada
	 * 
	 * @return List de ComunaDTO's
	 * @throws DireccionesException
	 */
	/*public List getComunasConZona()  throws DireccionesException {
		List coms= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaCom = new ArrayList();
			listaCom = (List) clientesDAO.listadoComunasConZona();
			ComunaEntity com = null;
			for (int i = 0; i < listaCom.size(); i++) {
				com = null;
				com = (ComunaEntity) listaCom.get(i);
				ComunaDTO comDto = new ComunaDTO(com.getId_comuna().longValue(), com.getId_region().longValue(),
						com.getNombre(), com.getReg_nombre());
				coms.add(comDto);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return coms;
	}*/

	
	/**
	 * Obtiene un listado de comunas de Facturación
	 * 
	 * @return List de ComunaDTO's
	 * @throws DireccionesException
	 */
	public List getComunasFact()  throws DireccionesException {
		List coms= new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
			List listaCom = new ArrayList();
			listaCom = (List) clientesDAO.listadoComunasFact();
			ComunaEntity com = null;
			for (int i = 0; i < listaCom.size(); i++) {
				com = null;
				com = (ComunaEntity) listaCom.get(i);
				ComunasDTO comDto = new ComunasDTO(com.getId_comuna().longValue(), com.getId_region().longValue(),
						com.getNombre(), com.getReg_nombre());
				coms.add(comDto);
			}
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return coms;
	}
	
	/**
	 * Obtiene un listado de los tipos de calles
	 * 
	 * @return List de TipoCalleDTO's
	 * @throws DireccionesException
	 */
	
		public List getTiposCalle()  throws DireccionesException {
			List tipos = new ArrayList();
			try{ 
				JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
				// HibernateClientesDAO clientesDAO =
				// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
		
				List listaTipCalle = new ArrayList();
				listaTipCalle = (List) clientesDAO.listadoTiposCalle();
				TipoCalleEntity tip = null;
				for (int i = 0; i < listaTipCalle.size(); i++) {
					tip = null;
					tip = (TipoCalleEntity) listaTipCalle.get(i);
					TipoCalleDTO tipDto = new TipoCalleDTO(tip.getId().longValue(),tip.getNombre(),tip.getEstado());
					tipos.add(tipDto);
				}
				
			}catch(ClientesDAOException ex){
				logger.debug("Problema getLocales:"+ex);
				throw new DireccionesException(ex);
			}
			return tipos;
	}

	/**
	 * Obtiene un listado de comunas de acuerdo a un id de zona
	 * 
	 * @param  id_zona long
	 * @return List de ComunaDTO's
	 * @throws DireccionesException
	 */
	/*public List getComunasByZona(long id_zona)  throws DireccionesException {
			List comunas = new ArrayList();
			try{ 
				JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
				List lst_com = new ArrayList();
				lst_com = (List) clientesDAO.getComunasByZona(id_zona);
				ComunaEntity com = null;
				for (int i = 0; i < lst_com.size(); i++) {
					com = null;
					com = (ComunaEntity) lst_com.get(i);
					ComunaDTO tipDto = new ComunaDTO(com.getId_comuna().longValue(), com.getId_region().longValue(), 
							com.getNombre(), com.getReg_nombre());
					comunas.add(tipDto);
				}
			}catch(ClientesDAOException ex){
				logger.debug("Problema getLocales:"+ex);
				throw new DireccionesException(ex);
			}
			return comunas;
	}*/

	/**
	 * Obtiene un listado de zonas de acuerdo a un id de comuna
	 * 
	 * @param  id_comuna long
	 * @return List de ZonaDTO's
	 * @throws DireccionesException
	 */
	public List getZonasByComuna(long id_comuna)  throws DireccionesException {
		List zons = new ArrayList();
		try{ 
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
			List listaZon = new ArrayList();
			listaZon = (List) clientesDAO.getZonasByComuna(id_comuna);
			ZonaEntity zon = null;
			for (int i = 0; i < listaZon.size(); i++) {
				zon = null;
				zon = (ZonaEntity) listaZon.get(i);
				ZonaDTO dto = new ZonaDTO(zon.getId_zona().longValue(), zon.getNombre(), zon.getDescrip(), 
				                          zon.getTarifa_normal_alta(), zon.getTarifa_normal_media(), 
				                          zon.getTarifa_normal_baja(), zon.getTarifa_economica(), 
				                          zon.getTarifa_express(), zon.getEstado_tarifa_economica(),
				                          zon.getEstado_tarifa_express());
				zons.add(dto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new DireccionesException(ex);
		}
		return zons;
	
	}
	
	/**
	 * Obtiene un listado de locales de acuerdo a un id de zona
	 * 
	 * @param  id_zona long
	 * @return List de LocalDTO's
	 * @throws DireccionesException
	 */
		public List getLocalesByZona(long id_zona)  throws DireccionesException {
			List locales = new ArrayList();
			try{ 
				JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
				List lst_loc = new ArrayList();
				lst_loc = (List) clientesDAO.getLocalesByZona(id_zona);
				LocalEntity loc = null;
				for (int i = 0; i < lst_loc.size(); i++) {
					loc = null;
					loc = (LocalEntity) lst_loc.get(i);
					LocalDTO tipDto = new LocalDTO();
					
					/*
					LocalDTO tipDto = new LocalDTO(loc.getId_local().longValue(), loc.getCod_local(), 
							loc.getNom_local());
					*/
					tipDto.setId_local(loc.getId_local().longValue());
					tipDto.setCod_local(loc.getCod_local());
					tipDto.setNom_local(loc.getNom_local());
					
					tipDto.setId_zona_retiro(loc.getId_zona_retiro());
					tipDto.setDireccion(loc.getDireccion());
					tipDto.setRetirolocal(loc.getRetirolocal());
					
					locales.add(tipDto);
				}
				
			}catch(ClientesDAOException ex){
				logger.debug("Problema getLocales:"+ex);
				throw new DireccionesException(ex);
			}
			return locales;
		
	}

	/**
	 * Obtiene la información de una zona dado su id_zona
	 * 
	 * @param  id_zona long
	 * @return ZonaDTO
	 * @throws DireccionesException
	 */
		public ZonaDTO getZonaById(long id_zona)  throws DireccionesException {
			ZonaDTO dto = new ZonaDTO();
			try{ 
				JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
				ZonaEntity zon =  clientesDAO.getZonaById(id_zona);
				dto = new ZonaDTO(zon.getId_zona().longValue(), zon.getNombre(), zon.getDescrip(), 
				                  zon.getTarifa_normal_alta(), zon.getTarifa_normal_media(), 
				                  zon.getTarifa_normal_baja(), zon.getTarifa_economica(), 
				                  zon.getTarifa_express(), zon.getEstado_tarifa_economica(),
				                  zon.getEstado_tarifa_express());
				dto.setNom_local(zon.getNom_local());
				
			}catch(ClientesDAOException ex){
				logger.debug("Problema getLocales:"+ex);
				throw new DireccionesException(ex);
			}
			return dto;
		
	}

}
