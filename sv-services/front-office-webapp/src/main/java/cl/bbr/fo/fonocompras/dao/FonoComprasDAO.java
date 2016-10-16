package cl.bbr.fo.fonocompras.dao;

import java.sql.Connection;

import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.fonocompras.exception.FonoComprasDAOException;

/**
 * Interfaz para implementaci�n de m�todos en DAO para diferentes tipos de conexi�n a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface FonoComprasDAO {

	/**
	 * Recupera los datos del ejecutivo de fonocompras
	 * 
	 * @param conexion	Objeto de conexi�n que se instancia en el service
	 * @param login		Login del ejecutivo de fonocompras
	 * @return			DTO con datos del ejecutivo
	 * @throws FonoComprasDAOException
	 */
	public UsuarioDTO ejecutivoGetByRut(String login ) throws FonoComprasDAOException;
	
}
