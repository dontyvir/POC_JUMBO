package cl.bbr.jumbocl.casos.service;

import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.casos.ctrl.CasosCtrl;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.exceptions.CasosException;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite manejar información y operaciones sobre Casos. 
 * 
 * @author imoyano
 *
 */
public class CasosService {

    
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
	public List getCasosByCriterio(CasosCriterioDTO criterio)
		throws ServiceException{
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getCasosByCriterio(criterio);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado de estados de caso
	 * 
	 * @return List EstadoCasoDTODTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.casos.dto.EstadoCasoDTODTO
	 */
	public List getEstadosDeCasos()
		throws ServiceException{
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getEstadosDeCasos();
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

    /**
     * @param criterio
     * @return
     */
    public double getCountCasosByCriterio(CasosCriterioDTO criterio) 
    	throws ServiceException {
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getCoutCasosByCriterio(criterio);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param usr
     * @return
     */
    public long getCasoEnEdicionByUsuario(UserDTO usr) throws ServiceException {
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getCasoEnEdicionByUsuario(usr);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param usr
     * @param idCaso
     * @return
     */
    public boolean setLiberaCaso(long idCaso) throws ServiceException {
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.setLiberaCaso(idCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @return
     */
    public CasoDTO getCasoByIdCaso(long idCaso) throws ServiceException {
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getCasoByIdCaso(idCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @param tipoEnviar
     * @return
     */
    public List getProductosByCasoAndTipo(long idCaso, String tipoEnviar) throws ServiceException {
		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getProductosByCasoAndTipo(idCaso,tipoEnviar);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idPedido
     * @return
     */
    public PedidoCasoDTO getPedidoById(long idPedido) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getPedidoById(idPedido);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @param id_usuario
     * @return
     */
    public boolean setModCaso(long idCaso, long id_usuario) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.setModCaso(idCaso, id_usuario);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getJornadas() throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getJornadas();
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param caso
     * @return
     */
    public long addCaso(CasoDTO caso) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addCaso(caso);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @return
     */
    public List getQuiebres() throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getQuiebres();
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getResponsables() throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getResponsables();
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param producto
     * @return
     */
    public long addProductoCaso(ProductoCasoDTO producto) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addProductoCaso(producto);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param producto
     * @return
     */
    public boolean modProductoCaso(ProductoCasoDTO producto) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.modProductoCaso(producto);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idProducto
     * @return
     */
    public ProductoCasoDTO getProductoById(long idProducto) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getProductoById(idProducto);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idProducto
     * @return
     */
    public boolean delProductoCaso(long idProducto) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.delProductoCaso(idProducto);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param caso
     * @return
     */
    public boolean modCaso(CasoDTO caso) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.modCaso(caso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param log
     * @return
     */
    public void addLogCaso(LogCasosDTO log) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			casos.addLogCaso(log);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @return
     */
    public List getLogCaso(long idCaso) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getLogCaso(idCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idQuiebre
     * @return
     */
    public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getQuiebreById(idQuiebre);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idObjeto
     * @return
     */
    public ObjetoDTO getResponsableById(long idResponsable) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getResponsableById(idResponsable);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param quiebre
     * @return
     */
    public long addQuiebre(QuiebreCasoDTO quiebre) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addQuiebre(quiebre);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param quiebre
     */
    public void modQuiebre(QuiebreCasoDTO quiebre) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.modQuiebre(quiebre);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param idObjeto
     */
    public void delQuiebre(long idQuiebre) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.delQuiebre(idQuiebre);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param responsable
     * @return
     */
    public long addResponsable(ObjetoDTO responsable) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addResponsable(responsable);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param responsable
     */
    public void modResponsable(ObjetoDTO responsable) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.modResponsable(responsable);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param idObjeto
     */
    public void delResponsable(long idResponsable) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.delResponsable(idResponsable);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param jornada
     * @return
     */
    public long addJornada(JornadaDTO jornada) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addJornada(jornada);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param jornada
     */
    public void modJornada(JornadaDTO jornada) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.modJornada(jornada);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param idJornada
     * @return
     */
    public JornadaDTO getJornadaCasoById(long idJornada) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getJornadaCasoById(idJornada);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idJornada
     */
    public void delJornadaDeCaso(long idJornada) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			casos.delJornadaDeCaso(idJornada);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param estado
     * @return
     */
    public List getJornadasByEstado(String estado) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getJornadasByEstado(estado);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param estado
     * @return
     */
    public List getQuiebresByEstado(String estado) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getQuiebresByEstado(estado);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param estado
     * @return
     */
    public List getResponsablesByEstado(String estado) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getResponsablesByEstado(estado);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @return
     */
    public long getIdUsuarioEditorDeCaso(long idCaso) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getIdUsuarioEditorDeCaso(idCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idPedido
     * @return
     */
    public List getCasosByOp(long idPedido) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getCasosByOp(idPedido);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idCaso
     * @return
     */
    public List getDocBolCasoByCaso(long idCaso) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getDocBolCasoByCaso(idCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idDocBolCaso
     * @return
     */
    public CasosDocBolDTO getDocBolCasoById(long idDocBolCaso) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getDocBolCasoById(idDocBolCaso);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param caso
     * @return
     */
    public long addDocBolCaso(CasosDocBolDTO caso) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
		try {
			return casos.addDocBolCaso(caso);
		} catch (CasosException e) {
		    e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List getTablaReclamosClientes(String fechaIni, String fechaFin, Hashtable llaves) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getTablaReclamosClientes(fechaIni, fechaFin, llaves);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param mail
     */
    public void enviarMailSupervisor(MailDTO mail) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			casos.enviarMailSupervisor(mail);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroComprasByCliente(long rutCliente) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getNroComprasByCliente(rutCliente);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param rutCliente
     * @return
     */
    public long getNroCasosByCliente(long rutCliente) throws ServiceException {		
		CasosCtrl casos = new CasosCtrl();
		try {
			return casos.getNroCasosByCliente(rutCliente);
		} catch (CasosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getMotivos() throws ServiceException {      
        CasosCtrl casos = new CasosCtrl();
        try {
            return casos.getMotivos();
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param motivo
     * @return
     */
    public long addMotivo(ObjetoDTO motivo) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
        try {
            return casos.addMotivo(motivo);
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param motivo
     */
    public void modMotivo(ObjetoDTO motivo) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
        try {
            casos.modMotivo(motivo);
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idMotivo
     * @return
     */
    public ObjetoDTO getMotivoById(long idMotivo) throws ServiceException {     
        CasosCtrl casos = new CasosCtrl();
        try {
            return casos.getMotivoById(idMotivo);
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idMotivo
     */
    public void delMotivo(long idMotivo) throws ServiceException, SystemException {
        CasosCtrl casos = new CasosCtrl();
        try {
            casos.delMotivo(idMotivo);
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param estado
     * @return
     */
    public List getMotivosByEstado(String estado) throws ServiceException {     
        CasosCtrl casos = new CasosCtrl();
        try {
            return casos.getMotivosByEstado(estado);
        } catch (CasosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
	
	
	
}
