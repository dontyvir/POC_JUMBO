package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.boc.dto.BOPreciosLocalesDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * 
 * @author jolazogu
 *
 */
public interface StockOnLineDAO {

	public BOPreciosLocalesDTO 	getPreciosLocalesPorIDLocalYIDProducto( int idLocal, int idProducto ) throws DAOException;
	public long[] 				cantidadDeProductosTendranCambios( long idLocal ) throws DAOException;
	public long[]				cantidadDeProductosActualmente( long idLocal ) throws DAOException;
	public List 				getDetalleSemiautomatico( long idLocal ) throws DAOException;
	public List					getProductosPorLocal ( long idLocal ) throws DAOException;
	
	
	
}
