package cl.bbr.jumbocl.bolsas.dao;

import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.bolsas.exceptions.BolsasDAOException;



/**
 * Permite las operaciones en base de datos sobre los Casos.
 * 
 * @author imoyano
 *
 */
public interface BolsasDAO { 
    
    /**
	 * Lista casos por criterio
	 * El cirterio esta dado por :CasosCriterioDTO
	 * 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
//	public List getCasosByCriterio(CasosCriterioDTO criterio) throws CasosDAOException;
	
	public List getStockBolsasRegalo(String cod_sucursal) throws BolsasDAOException;
	
	public List getBolsasRegalo() throws BolsasDAOException;
	
	public List getBitacoraBolsasRegalo(String cod_bolsa) throws BolsasDAOException;
	
	public boolean existeStockBolsaSucursal(String id_sucursal, String cod_bolsa) throws BolsasDAOException;
	
	public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal, int stock) throws BolsasDAOException;
	
	public void insertarRegistroBitacoraBolsas(String desc_operacion, String usuario, String cod_sucursal) throws BolsasDAOException;
	
	public void asignacionBolsaCliente(String rut_cliente, String cod_bolsa) throws BolsasDAOException;
	
	public boolean existeAsignacionCliente(String rut_cliente) throws BolsasDAOException;
	
	public void crearBolsaRegalo(BolsaDTO bolsa) throws BolsasDAOException;
	
	public void eliminarBolsaRegalo(BolsaDTO bolsa) throws BolsasDAOException;
	
	public BolsaDTO getBolsaRegalo(String cod_bolsa) throws BolsasDAOException;
	
	public List getAsignacionesBolsas() throws BolsasDAOException;
	
	public Hashtable getAsignacionBolsaCliente(String rut_cliente) throws BolsasDAOException;
	
	public void actualizarBolsa(BolsaDTO bolsa) throws BolsasDAOException;
	
	public void asignarBolsaCliente(String rut_cliente, String id_bolsa) throws BolsasDAOException;
	
	public boolean existeEnListaBase(String rut_cliente) throws BolsasDAOException;
	
	

}
