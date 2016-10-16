package cl.bbr.jumbocl.eventos.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.eventos.dao.DAOFactory;
import cl.bbr.jumbocl.eventos.dao.JdbcEventosDAO;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.exceptions.EventosDAOException;
import cl.bbr.jumbocl.eventos.exceptions.EventosException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Entrega metodos de navegacion por gestion de casos
 * 
 * @author imoyano
 *  
 */
public class EventosCtrl {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    /**
     * Obtiene la lista de Casos, segun el <b>CasosCriterioDTO </b> el cual
     * contiene los criterios de consulta <br>
     * 
     * @param caso
     *            CasosCriterioDTO
     * @return List MonitorCasosDTO
     * @throws CasosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getEventosByCriterio(EventosCriterioDTO criterio) throws EventosException {

        List eventos = new ArrayList();
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();

        try {
            eventos = dao.getEventosByCriterio(criterio);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
        return eventos;
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountEventosByCriterio(EventosCriterioDTO criterio) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getCountEventosByCriterio(criterio);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @return
     */
    public List getTiposEventos() throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getTiposEventos();
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param usr
     * @return
     */
    public long getEventoEnEdicionByUsuario(UserDTO usr) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventoEnEdicionByUsuario(usr);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @return
     */
    public List getPasos() throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getPasos();
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param evento
     * @return
     */
    public long addEvento(EventoDTO evento) throws EventosException, SystemException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.addEvento(evento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public EventoDTO getEvento(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEvento(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @param idUsuario
     * @return
     */
    public boolean setModEvento(long idEvento, long idUsuario) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.setModEvento(idEvento,idUsuario);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param evento
     */
    public void modEvento(EventoDTO evento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            dao.modEvento(evento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean setLiberaEvento(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.setLiberaEvento(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param evento
     * @return
     */
    public boolean existeEvento(EventoDTO evento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.existeEvento(evento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param evento
     */
    public void delEvento(EventoDTO evento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            dao.delEvento(evento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public long getIdUsuarioEditorDeEvento(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getIdUsuarioEditorDeEvento(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public boolean eventoUtilizado(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.eventoUtilizado(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param ruts
     * @param idEvento
     * @return
     */
    public String addRutsEvento(Vector ruts, EventoDTO evento) throws EventosException, SystemException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.addRutsEvento(ruts, evento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public List getRutsByEvento(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getRutsByEvento(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param rutCliente
     * @return
     */
    public List getEventosByRutCliente(long rutCliente) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventosByRutCliente(rutCliente);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param rutCliente
     * @return
     */
    public boolean tieneEventoByRutCliente(long rutCliente) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.tieneEventoByRutCliente(rutCliente);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param clienteRut
     * @return
     */
    public EventoDTO getEventoMostrarByRut(long clienteRut) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventoMostrarByRut(clienteRut);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param fingreso
     * @return
     */
    public EventoDTO getEventoMostrarByRutYFecha(long rutCliente, String fingreso) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventoMostrarByRutYFecha(rutCliente, fingreso);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * 
     * @param rutCliente
     * @param idPedido
     * @return
     * @throws EventosException
     */
    public boolean subirOcurrenciaEvento(long rutCliente, long idPedido) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.subirOcurrenciaEvento(rutCliente, idPedido);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idPedido
     * @param rutCliente
     * @return
     */
    public boolean bajarOcurrenciaEvento(long idPedido, long rutCliente) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.bajarOcurrenciaEvento(idPedido, rutCliente);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param rutCliente
     * @param idPedido
     * @return
     */
    public List getEventosByRutClienteAndPedido(long rutCliente, long idPedido) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventosByRutClienteAndPedido(rutCliente, idPedido);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param rutCliente
     * @param idPedido
     * @return
     */
    public EventoDTO getEventoByPedido(long rutCliente, long idPedido) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getEventoByPedido(rutCliente, idPedido);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param idEvento
     * @return
     */
    public long getCantidadRutsUsaronEvento(long idEvento) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.getCantidadRutsUsaronEvento(idEvento);
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

    /**
     * @param clienteRut
     * @return
     */
    public boolean tieneEventosActivosConValidacionManual(long clienteRut) throws EventosException {
        JdbcEventosDAO dao = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();
        try {
            return dao.tieneEventosActivosConValidacionManual( clienteRut );
        } catch (EventosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new EventosException(ex);
        }
    }

}
