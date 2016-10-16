package cl.bbr.jumbocl.casos.ctrl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.casos.dao.DAOFactory;
import cl.bbr.jumbocl.casos.dao.JdbcCasosDAO;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.exceptions.CasosDAOException;
import cl.bbr.jumbocl.casos.exceptions.CasosException;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.emails.dao.JdbcEmailDAO;
import cl.bbr.jumbocl.shared.emails.exceptions.EmailDAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Entrega metodos de navegacion por gestion de casos
 * 
 * @author imoyano
 *  
 */
public class CasosCtrl {

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
    public List getCasosByCriterio(CasosCriterioDTO caso) throws CasosException {

        List casos = new ArrayList();
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();

        try {

            casos = dao.getCasosByCriterio(caso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
        return casos;

    }

    /**
     * Obtiene la lista de los Estados de Casos
     * 
     * @return List EstadoCasosDTO
     * @throws CasosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getEstadosDeCasos() throws CasosException {

        List estados = new ArrayList();
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();

        try {

            estados = dao.getEstadosDeCasos();
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
        return estados;

    }

    /**
     * @param criterio
     * @return
     */
    public double getCoutCasosByCriterio(CasosCriterioDTO criterio)
            throws CasosException {
        double lista = 0;
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();

        try {

            lista = dao.getCountCasosByCriterio(criterio);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
        return lista;
    }

    /**
     * @param usr
     * @return
     */
    public long getCasoEnEdicionByUsuario(UserDTO usr) throws CasosException {
        long idCaso = 0;
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();

        try {

            idCaso = dao.getCasoEnEdicionByUsuario(usr);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
        return idCaso;
    }

    /**
     * @param usr
     * @param idCaso
     * @return
     */
    public boolean setLiberaCaso(long idCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.setLiberaCaso(idCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @return
     */
    public CasoDTO getCasoByIdCaso(long idCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getCasoByIdCaso(idCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @param tipoEnviar
     * @return
     */
    public List getProductosByCasoAndTipo(long idCaso, String tipoEnviar)
            throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getProductosByCasoAndTipo(idCaso, tipoEnviar);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public PedidoCasoDTO getPedidoById(long idPedido) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getPedidoById(idPedido);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @param id_usuario
     * @return
     */
    public boolean setModCaso(long idCaso, long id_usuario)
            throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.setModCaso(idCaso, id_usuario);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @return
     */
    public List getJornadas() throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getJornadas();
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param caso
     * @return
     */
    public long addCaso(CasoDTO caso) throws CasosException,
            SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addCaso(caso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }

    }

    /**
     * @return
     */
    public List getQuiebres() throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getQuiebres();
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @return
     */
    public List getResponsables() throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getResponsables();
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param producto
     * @return
     */
    public long addProductoCaso(ProductoCasoDTO producto) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addProductoCaso(producto);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param producto
     * @return
     */
    public boolean modProductoCaso(ProductoCasoDTO producto) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.modProductoCaso(producto);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idProducto
     * @return
     */
    public ProductoCasoDTO getProductoById(long idProducto) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getProductoById(idProducto);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idProducto
     * @return
     */
    public boolean delProductoCaso(long idProducto) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.delProductoCaso(idProducto);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param caso
     * @return
     */
    public boolean modCaso(CasoDTO caso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.modCaso(caso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param log
     * @return
     */
    public void addLogCaso(LogCasosDTO log) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            dao.addLogCaso(log);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @return
     */
    public List getLogCaso(long idCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getLogCaso(idCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idQuiebre
     * @return
     */
    public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getQuiebreById(idQuiebre);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idObjeto
     * @return
     */
    public ObjetoDTO getResponsableById(long idResponsable) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getResponsableById(idResponsable);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param quiebre
     * @return
     */
    public long addQuiebre(QuiebreCasoDTO quiebre) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addQuiebre(quiebre);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param quiebre
     */
    public void modQuiebre(QuiebreCasoDTO quiebre) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.modQuiebre(quiebre);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idObjeto
     */
    public void delQuiebre(long idQuiebre) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.delQuiebre(idQuiebre);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param responsable
     * @return
     */
    public long addResponsable(ObjetoDTO responsable) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addResponsable(responsable);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param responsable
     */
    public void modResponsable(ObjetoDTO responsable) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.modResponsable(responsable);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idResponsable
     */
    public void delResponsable(long idResponsable) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.delResponsable(idResponsable);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param jornada
     * @return
     */
    public long addJornada(JornadaDTO jornada) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addJornada(jornada);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param jornada
     */
    public void modJornada(JornadaDTO jornada) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.modJornada(jornada);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idJornada
     * @return
     */
    public JornadaDTO getJornadaCasoById(long idJornada) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getJornadaCasoById(idJornada);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idJornada
     */
    public void delJornadaDeCaso(long idJornada) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.delJornadaDeCaso(idJornada);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param estado
     * @return
     */
    public List getJornadasByEstado(String estado) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getJornadasByEstado(estado);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param estado
     * @return
     */
    public List getQuiebresByEstado(String estado) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getQuiebresByEstado(estado);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param estado
     * @return
     */
    public List getResponsablesByEstado(String estado) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getResponsablesByEstado(estado);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @return
     */
    public long getIdUsuarioEditorDeCaso(long idCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getIdUsuarioEditorDeCaso(idCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getCasosByOp(long idPedido) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getCasosByOp(idPedido);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idCaso
     * @return
     */
    public List getDocBolCasoByCaso(long idCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getDocBolCasoByCaso(idCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idDocBolCaso
     * @return
     */
    public CasosDocBolDTO getDocBolCasoById(long idDocBolCaso) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getDocBolCasoById(idDocBolCaso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param caso
     * @return
     */
    public long addDocBolCaso(CasosDocBolDTO caso) throws CasosException, SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addDocBolCaso(caso);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List getTablaReclamosClientes(String fechaIni, String fechaFin, Hashtable llaves) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getTablaReclamosClientes(fechaIni, fechaFin, llaves);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param mail
     */
    public void enviarMailSupervisor(MailDTO mail) throws CasosException {
        JdbcEmailDAO emailDAO = (JdbcEmailDAO) cl.bbr.jumbocl.shared.emails.dao.DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getEmailDAO();
        try {
            emailDAO.addMail(mail);
        } catch (EmailDAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroComprasByCliente(long rutCliente) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getNroComprasByCliente(rutCliente);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroCasosByCliente(long rutCliente) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getNroCasosByCliente(rutCliente);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @return
     */
    public List getMotivos() throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getMotivos();
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param motivo
     * @return
     */
    public long addMotivo(ObjetoDTO motivo) throws CasosException, SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.addMotivo(motivo);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param motivo
     */
    public void modMotivo(ObjetoDTO motivo) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.modMotivo(motivo);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idMotivo
     * @return
     */
    public ObjetoDTO getMotivoById(long idMotivo) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getMotivoById(idMotivo);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param idMotivo
     */
    public void delMotivo(long idMotivo) throws CasosException,
    SystemException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            dao.delMotivo(idMotivo);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

    /**
     * @param estado
     * @return
     */
    public List getMotivosByEstado(String estado) throws CasosException {
        JdbcCasosDAO dao = (JdbcCasosDAO) DAOFactory.getDAOFactory(
                DAOFactory.JDBC).getCasosDAO();
        try {
            return dao.getMotivosByEstado(estado);
        } catch (CasosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new CasosException(ex);
        }
    }

}
