package cl.bbr.jumbocl.pedidos.dao;


import java.util.List;

import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosDAOException;


/**
 * Permite las operaciones en base de datos sobre los Sectores de picking.
 * @author BBR
 *
 */
public interface PoligonosDAO {

	
	/**
	 * Verifica si el Numero del Poligono ya existe para una Comuna
	 * 
	 * @param  sector
	 * @throws PoligonosDAOException
	 */
	public boolean verificaNumPoligono(int id_comuna, int num_pol)
		throws PoligonosDAOException;	
	
	public List getPoligonosXComuna(long id_comuna) throws PoligonosDAOException;
	
	public List getPoligonosXComunaAll(long id_comuna) throws PoligonosDAOException;
	
	public PoligonoDTO getPoligonoById(long id_poligono) throws PoligonosDAOException;
	
	public long AddPoligono(PoligonoDTO pol) throws PoligonosDAOException;
	
	public boolean ModPoligono(PoligonoDTO pol) throws PoligonosDAOException;
	
	public int VerificaPoligonoEnDirecciones(long id_poligono) throws PoligonosDAOException;
	
	public boolean DelPoligono(long id_poligono) throws PoligonosDAOException;
	
	public int getLocalByPoligono(int id_poligono) throws PoligonosDAOException;
	
	public List getPoligonosXComunaSinZona(long id_comuna) throws PoligonosDAOException;
	
	public List getComunasConPoligonosSinZona() throws PoligonosDAOException;
	
}
