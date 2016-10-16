package cl.bbr.jumbocl.casos.dao;

import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.exceptions.CasosDAOException;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Permite las operaciones en base de datos sobre los Casos.
 * 
 * @author imoyano
 *
 */
public interface CasosDAO { 
    
    /**
	 * Lista casos por criterio
	 * El cirterio esta dado por :CasosCriterioDTO
	 * 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
	public List getCasosByCriterio(CasosCriterioDTO criterio) throws CasosDAOException;
	
	/**
	 * Lista estados de los casos
	 * 
	 * @return List EstadoCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
	public List getEstadosDeCasos() throws CasosDAOException;
	
    /**
     * @param criterio
     * @return
     */
    public double getCountCasosByCriterio(CasosCriterioDTO criterio) throws CasosDAOException;
    
    /**
     * @param usr
     * @return
     */
    public long getCasoEnEdicionByUsuario(UserDTO usr) throws CasosDAOException;
    
    /**
     * @param usr
     * @param idCaso
     * @return
     */
    public boolean setLiberaCaso(long idCaso) throws CasosDAOException;
    
    /**
     * @param idCaso
     * @return
     */
    public CasoDTO getCasoByIdCaso(long idCaso) throws CasosDAOException;
    
    /**
     * @param idCaso
     * @param tipoEnviar
     * @return
     */
    public List getProductosByCasoAndTipo(long idCaso, String tipoAccion) throws CasosDAOException;
    
    /**
     * @param idPedido
     * @return
     */
    public PedidoCasoDTO getPedidoById(long idPedido) throws CasosDAOException;

    /**
     * Metodo para modificar el usuario que esta modificando (editando) el caso
     * 
     * @param idCaso
     * @param id_usuario
     * @return
     */
    public boolean setModCaso(long idCaso, long id_usuario) throws CasosDAOException;
    
    /**
     * @return
     */
    public List getJornadas() throws CasosDAOException;
    
    /**
     * @param caso
     * @return
     */
    public long addCaso(CasoDTO caso) throws CasosDAOException;
    
    /**
     * @return
     */
    public List getQuiebres()  throws CasosDAOException;
    
    /**
     * @return
     */
    public List getResponsables() throws CasosDAOException;
    
    /**
     * @param producto
     * @return
     */
    public long addProductoCaso(ProductoCasoDTO producto) throws CasosDAOException;
    
    /**
     * @param producto
     * @return
     */
    public boolean modProductoCaso(ProductoCasoDTO producto) throws CasosDAOException;
    
    /**
     * @param idProducto
     * @return
     */
    public ProductoCasoDTO getProductoById(long idProducto) throws CasosDAOException;
    
    /**
     * @param idProducto
     * @return
     */
    public boolean delProductoCaso(long idProducto) throws CasosDAOException;
    
    /**
     * @param caso
     * @return
     */
    public boolean modCaso(CasoDTO caso) throws CasosDAOException;
    
    /**
     * @param log
     * @return
     */
    public void addLogCaso(LogCasosDTO log) throws CasosDAOException;
    
    /**
     * @param idCaso
     * @return
     */
    public List getLogCaso(long idCaso) throws CasosDAOException;
    
    /**
     * @param idQuiebre
     * @return
     */
    public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws CasosDAOException;
    
    /**
     * @param idObjeto
     * @return
     */
    public ObjetoDTO getResponsableById(long idResponsable) throws CasosDAOException;
    
    /**
     * @param quiebre
     * @return
     */
    public long addQuiebre(QuiebreCasoDTO quiebre) throws CasosDAOException;
    
    /**
     * @param quiebre
     */
    public void modQuiebre(QuiebreCasoDTO quiebre) throws CasosDAOException;
    
    /**
     * @param idQuiebre
     */
    public void delQuiebre(long idQuiebre) throws CasosDAOException;
    
    /**
     * @param responsable
     * @return
     */
    public long addResponsable(ObjetoDTO responsable) throws CasosDAOException;
    
    /**
     * @param responsable
     */
    public void modResponsable(ObjetoDTO responsable) throws CasosDAOException;
    
    /**
     * @param idResponsable
     */
    public void delResponsable(long idResponsable) throws CasosDAOException;
    
    /**
     * @param jornada
     * @return
     */
    public long addJornada(JornadaDTO jornada) throws CasosDAOException;
    
    /**
     * @param jornada
     */
    public void modJornada(JornadaDTO jornada) throws CasosDAOException;
    
    /**
     * @param idJornada
     * @return
     */
    public JornadaDTO getJornadaCasoById(long idJornada) throws CasosDAOException;
    
    /**
     * @param idJornada
     */
    public void delJornadaDeCaso(long idJornada) throws CasosDAOException;
    
    /**
     * @param estado
     * @return
     */
    public List getJornadasByEstado(String estado) throws CasosDAOException;
    
    /**
     * @param estado
     * @return
     */
    public List getQuiebresByEstado(String estado) throws CasosDAOException;
    
    /**
     * @param estado
     * @return
     */
    public List getResponsablesByEstado(String estado) throws CasosDAOException;
    
    /**
     * @param idCaso
     * @return
     */
    public long getIdUsuarioEditorDeCaso(long idCaso) throws CasosDAOException;
    
    /**
     * @param idPedido
     * @return
     */
    public List getCasosByOp(long idPedido) throws CasosDAOException;
    
    /**
     * @param idCaso
     * @return
     */
    public List getDocBolCasoByCaso(long idCaso) throws CasosDAOException;
    
    /**
     * @param idDocBolCaso
     * @return
     */
    public CasosDocBolDTO getDocBolCasoById(long idDocBolCaso) throws CasosDAOException;
    
    /**
     * @param caso
     * @return
     */
    public long addDocBolCaso(CasosDocBolDTO caso) throws CasosDAOException;
    
    /**
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List getTablaReclamosClientes(String fechaIni, String fechaFin, Hashtable llaves) throws CasosDAOException;

}
