package cl.bbr.jumbocl.usuarios.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ComandosEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dao.DAOFactory;
import cl.bbr.jumbocl.usuarios.dao.JdbcPerfilesDAO;
import cl.bbr.jumbocl.usuarios.dto.ComandoDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModPerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.exceptions.PerfilesDAOException;
import cl.bbr.jumbocl.usuarios.exceptions.PerfilesException;

/**
 * Entrega metodos de navegacion por perfiles y busqueda en base a criterios. 
 * Los resultados son listados de perfiles y datos de comandos.
 * 
 * @author BBR Ingenieria
 *
 */
public class PerfilCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	/**
	 * Retorna información de un perfil
	 * 
	 * @param  id_perf
	 * @return PerfilDTO
	 */
	public PerfilDTO getPerfilById(long id_perf)  throws PerfilesException{
		PerfilDTO result=null;
		try{
			logger.debug("en getPerfilById");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			PerfilesEntity perf = perfilesDAO.getPerfilById(id_perf);
			List lst_cmd = perfilesDAO.getComandos(id_perf);
			perf.setLst_cmd(lst_cmd);
			
			result = new PerfilDTO(perf.getIdPerfil().longValue(), perf.getNombre(), perf.getDescripcion(), perf.getLst_cmd());
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		return result;
	}

	
	/**
	 * Obtiene el listados de los perfiles, segun el criterio de busqueda.
	 * 
	 * @param  criterio
	 * @return List PerfilDTO
	 * @throws PerfilesException
	 */
	public List getPerfilesAll(PerfilesCriteriaDTO criterio)  throws PerfilesException{
		List result=new ArrayList();
		try{
			logger.debug("en getPerfilesAll");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			List lstPrf = new ArrayList();
			lstPrf = perfilesDAO.getPerfilesAll(criterio);
			PerfilesEntity per = null;
			
			for(int i=0; i<lstPrf.size();i++){
				per = null;
				per = (PerfilesEntity)lstPrf.get(i);
				PerfilDTO dto = new PerfilDTO(per.getIdPerfil().longValue(), per.getNombre(), per.getDescripcion());
				result.add(dto);
			}
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		return result;
	}
	
	
	/**
	 * Obtiene la cantidad de perfiles, segun el criterio de busqueda.
	 * 
	 * @param  criterio
	 * @return int
	 * @throws PerfilesException
	 */
	public int getPerfilesAllCount(PerfilesCriteriaDTO criterio)  throws PerfilesException{
		int result=0;
		try{
			logger.debug("en getPerfilesAllCount");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			result = perfilesDAO.getPerfilesAllCount(criterio);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		return result;
	}

	
	/**
	 * Obtiene el listado de los comandos
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws PerfilesException
	 */
	//Este metodo envia Entity´s
	public List getComandosAll(long id) throws PerfilesException{
		List result= new ArrayList();
		try{
			logger.debug("en getComandosAll");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			result = perfilesDAO.getComandosAll(id);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		return result;
	}

	
	/**
	 * Obtiene información de un Comando
	 * 
	 * @param  comando nombre del comando
	 * @return ComandoDTO
	 * @throws PerfilesException
	 */
	public ComandoDTO getComandoByName(String comando)
	 	throws PerfilesException{

		try{
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			ComandosEntity cmd = perfilesDAO.getComandoByName(comando);
			ComandoDTO cmddto = null;
			
			if (cmd != null){
				cmddto = new ComandoDTO();
				cmddto.setId_cmd(cmd.getId_cmd());
				cmddto.setNombre(cmd.getNombre());
				cmddto.setDescripcion(cmd.getDescripcion());
				cmddto.setSeguridad(cmd.getSeguridad());
				cmddto.setActivo(cmd.getActivo());
			}

			return cmddto;
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}

	}
	
	
	/**
	 * Modifica información de un perfil
	 * 
	 * @param  perf
	 * @return boolean, devuelve <i>true</i> si la modificación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean setModPerfil(PerfilDTO perf)throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en setModPerfil");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.setModPerfil(perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}
	
	
	/**
	 * Agrega un nuevo perfil.
	 * 
	 * @param  perf
	 * @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean addPerfil(PerfilDTO perf) throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en addPerfil");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.addPerfil(perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}

	
	/**
	 * Elimina un perfil
	 * 
	 * @param  perf
	 * @return boolean, devuelve <i>true</i> si la eliminación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean delPerfil(PerfilDTO perf) throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en delPerfil");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.delPerfil(perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}

	
	/**
	 * Agrega la relación entre un comando y un perfil.
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se agregó con éxito, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean agregaCmdPerf(long id_cmd, long id_perf) throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en agregaCmdPerf");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.agregaCmdPerf(id_cmd, id_perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}
	
	
	/**
	 * Elimina la relación entre un comando y un perfil.
	 * 
	 * @param id_cmd
	 * @param id_perf
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean elimCmdPerf(long id_cmd, long id_perf) throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en elimCmdPerf");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.elimCmdPerf(id_cmd, id_perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}
	
	
	/**
	 * Elimina todos los comandos relacionados a un perfil.
	 * 
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se eliminó con éxito, caso contrario devuelve <i>false</i>.
	 * @throws PerfilesException
	 */
	public boolean elimCmdsByIdPerf(long id_perf) throws PerfilesException {
		boolean res = false;
		try{
			logger.debug("en elimCmdsByIdPerf");
			JdbcPerfilesDAO perfilesDAO = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
			
			res = perfilesDAO.elimCmdsByIdPerf(id_perf);
			
		}catch(PerfilesDAOException ex){
			logger.debug("Problema :"+ex);
			throw new PerfilesException(ex);
		}
		
		return res;
	}
	
	
	/**
	 * Transacción que actualiza datos del perfil y 
	 * el listado de comandos relacionados con un perfil.
	 * 
	 * @param  dto ProcModPerfilDTO
	 * @throws PerfilesException
	 * @throws SystemException 
	 */
	public void doModComandosPerfil(ProcModPerfilDTO dto)
 		throws PerfilesException, SystemException {

		// Creamos daos
		JdbcPerfilesDAO dao1 = (JdbcPerfilesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();

		
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (DAOException e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		} catch (SQLException e) {
			logger.error("Error al iniciar transacción");
			e.printStackTrace();
			throw new SystemException("Error al iniciar transacción");			
		}
		
		// Marcamos los dao's con la transacción
		try {
			dao1.setTrx(trx1);
		} catch (PerfilesDAOException e2) {
			logger.error("Error al asignar transacción al dao");
			throw new SystemException("Error al asignar transacción al dao");
		}
		
		PerfilDTO per1 = new PerfilDTO();
		per1.setDescripcion( dto.getDescripcion() );
		per1.setNombre( dto.getNombre() );
		per1.setIdPerfil( dto.getId_perfil() );
		
		try {

			// Actualiza datos del perfil
			dao1.setModPerfil( per1 );
			
			// borra todos los comandos asociados
			dao1.elimCmdsByIdPerf( dto.getId_perfil() );
			
			// inserta los nuevos comandos asociados al perfil
			long[] id_cmds = dto.getId_comandos();

			if (id_cmds != null){
				for (int i=0; i<id_cmds.length; i++){
					logger.debug("Insertar cmd " + id_cmds[i]);
					dao1.agregaCmdPerf( id_cmds[i], dto.getId_perfil() );
				}
			}
			
		} catch (PerfilesDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new SystemException("Error al realizar la operación");
		}

		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}

		
	}
	
	
	/**
	 * Consulta si un perfil tiene permiso para ejecutar un comando
	 * 
	 * @param  usr UserDTO 
	 * @param  id_comando
	 * @return boolean, devuelve <i>true</i> si tiene permiso, caso contrario devuelve <i>false</i>.
	 */
	public boolean doCheckPermisoPerfilComando(UserDTO usr, long id_comando)
	 	throws PerfilesException {
		
		try{
			JdbcPerfilesDAO dao = (JdbcPerfilesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getPerfilesDAO();
						
			
			//logger.debug("doCheckPermisoPerfilComando apellido " + usr.getApellido());
			
			List perfiles = usr.getPerfiles();
			//logger.debug("Perfiles Size " + perfiles.size());
			
			String in_id_perfil = "";
					
			for (int i=0; i<perfiles.size(); i++){
				PerfilesEntity perfil = (PerfilesEntity)perfiles.get(i);
				//logger.debug("-->" + perfil.getIdPerfil());
				in_id_perfil += perfil.getIdPerfil() + ",";
			}
			
			in_id_perfil = in_id_perfil.substring(0,in_id_perfil.length()-1);
			
			logger.debug("str_aux=" + in_id_perfil);
			
			return dao.doCheckPermisoPerfilComando(in_id_perfil, id_comando);
			
			
		}catch(PerfilesDAOException e){
			logger.debug("Problema :"+e);
			e.printStackTrace();
			throw new PerfilesException(e);
		}
		
	}
	
	
}
