package cl.bbr.jumbocl.eventos.service;

import java.util.List;
import java.util.Vector;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.eventos.ctrl.EventosCtrl;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.exceptions.EventosException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite manejar información y operaciones sobre Casos. 
 * 
 * @author imoyano
 *
 */
public class EventosService {
    
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);		
	
	/**
	 * Retorna listado de casos segun el criterio
	 * 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.casos.dto.MonitorCasosDTO
	 */
	public List getEventosByCriterio(EventosCriterioDTO criterio) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventosByCriterio(criterio);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

    /**
     * @param criterio
     * @return
     */
    public double getCountEventosByCriterio(EventosCriterioDTO criterio) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getCountEventosByCriterio(criterio);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getTiposEventos() throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getTiposEventos();
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param usr
     * @return
     */
    public long getEventoEnEdicionByUsuario(UserDTO usr) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventoEnEdicionByUsuario(usr);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getPasos() throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getPasos();
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param evento
     * @return
     */
    public long addEvento(EventoDTO evento) throws ServiceException, SystemException {
        EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.addEvento(evento);
		} catch (EventosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public EventoDTO getEvento(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEvento(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idEvento
     * @param idUsuario
     * @return
     */
    public boolean setModEvento(long idEvento, long idUsuario) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.setModEvento(idEvento,idUsuario);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param evento
     */
    public void modEvento(EventoDTO evento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			eventos.modEvento(evento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean setLiberaEvento(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.setLiberaEvento(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param evento
     * @return
     */
    public boolean existeEvento(EventoDTO evento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.existeEvento(evento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param evento
     */
    public void delEvento(EventoDTO evento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			eventos.delEvento(evento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public long getIdUsuarioEditorDeEvento(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getIdUsuarioEditorDeEvento(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean eventoUtilizado(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.eventoUtilizado(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param ruts
     * @param idEvento
     * @return
     */
    public String addRutsEvento(Vector ruts, EventoDTO evento) throws ServiceException, SystemException {
        EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.addRutsEvento(ruts,evento);
		} catch (EventosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public List getRutsByEvento(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getRutsByEvento(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @return
     */
    public List getEventosByRutCliente(long rutCliente) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventosByRutCliente(rutCliente);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @return
     */
    public boolean tieneEventoByRutCliente(long rutCliente) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.tieneEventoByRutCliente(rutCliente);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param clienteRut
     * @return
     */
    public EventoDTO getEventoMostrarByRut(long clienteRut) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventoMostrarByRut(clienteRut);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param fingreso
     * @return
     */
    public EventoDTO getEventoMostrarByRutYFecha(long rutCliente, String fingreso) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventoMostrarByRutYFecha(rutCliente, fingreso);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * 
     * @param rutCliente
     * @param idPedido
     * @return
     * @throws ServiceException
     */
    public boolean subirOcurrenciaEvento(long rutCliente, long idPedido) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.subirOcurrenciaEvento(rutCliente, idPedido);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idPedido
     * @param rutCliente
     * @return
     */
    public boolean bajarOcurrenciaEvento(long idPedido, long rutCliente) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.bajarOcurrenciaEvento(idPedido, rutCliente);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @param idPedido
     * @return
     */
    public List getEventosByRutClienteAndPedido(long rutCliente, long idPedido) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventosByRutClienteAndPedido(rutCliente, idPedido);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @param idPedido
     * @return
     */
    public EventoDTO getEventoByPedido(long rutCliente, long idPedido) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getEventoByPedido(rutCliente, idPedido);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idEvento
     * @return
     */
    public long getCantidadRutsUsaronEvento(long idEvento) throws ServiceException {		
		EventosCtrl eventos = new EventosCtrl();
		try {
			return eventos.getCantidadRutsUsaronEvento(idEvento);
		} catch (EventosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param clienteRut
     * @return
     */
    public boolean tieneEventosActivosConValidacionManual(long clienteRut) throws ServiceException {        
        EventosCtrl eventos = new EventosCtrl();
        try {
            return eventos.tieneEventosActivosConValidacionManual( clienteRut );
        } catch (EventosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

}
