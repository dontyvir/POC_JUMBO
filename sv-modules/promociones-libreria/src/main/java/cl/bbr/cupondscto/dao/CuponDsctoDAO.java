package cl.bbr.cupondscto.dao;

import java.util.List;

import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.cupondscto.exception.CuponDsctoDAOException;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

public interface CuponDsctoDAO {

	/**
	 * Busca cupon de descuento en base al codigo	
	 * @param codigo
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public CuponDsctoDTO getCuponDscto(String codigo) throws CuponDsctoDAOException;
	
	public CuponDsctoDTO getCuponDsctoById(int idCupon) throws CuponDsctoDAOException;
	
	/**
	 * Verifica que cupon es para el cliente
	 * @param rut
	 * @param id_cupon
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public boolean isCuponForRut(long rut, long id_cupon) throws CuponDsctoDAOException;

	/**
	 * Retorna los tipos asociados
	 * @param id_cupon
	 * @param tipo
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public List getProdsCupon (long id_cupon, String tipo) throws CuponDsctoDAOException;
	
	/**
	 * Descuenta stock de cupón una vez utilizado
	 * @param idCupon
	 * @return 
	 * @throws CuponDsctoDAOException
	 */
	public boolean dsctaStockCupon (long idCupon) throws CuponDsctoDAOException;
	
	/**
	 * 
	 * @param idCupon
	 * @param idPedido
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public boolean setIdCuponIdPedido ( long idCupon, long idPedido ) throws CuponDsctoDAOException;
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public CarroAbandonadoDTO getCuponCarroAbandonado(int codigo) throws CuponDsctoDAOException;

}
