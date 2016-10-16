package cl.bbr.vte.empresas.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CompradoresEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dao.DAOFactory;
import cl.bbr.vte.empresas.dao.JdbcCompradoresDAO;
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresDAOException;
import cl.bbr.vte.empresas.exceptions.CompradoresException;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO Compradores a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class CompradoresCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Obtiene informacion del comprador segun el Id
	 * 
	 * @param  comprador_id		Identificador único del comprador
	 * @return CompradoresDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradoresById(long comprador_id) throws CompradoresException, SystemException{
		CompradoresDTO dto = null;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO(); 
		
		try {
			CompradoresEntity comp = dao.getCompradoresById(comprador_id);
			if(comp!=null){
				dto = new CompradoresDTO();
				dto.setCpr_id(comp.getCpr_id());
				dto.setCpr_rut(comp.getCpr_rut());
				dto.setCpr_dv(comp.getCpr_dv());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_nombres(comp.getCpr_nombres());
				dto.setCpr_ape_pat(comp.getCpr_ape_pat());
				dto.setCpr_ape_mat(comp.getCpr_ape_mat());
				dto.setCpr_genero(comp.getCpr_genero());
				dto.setCpr_fono1(comp.getCpr_fono1());
				dto.setCpr_fono2(comp.getCpr_fono2());
				dto.setCpr_fono3(comp.getCpr_fono3());
				dto.setCpr_email(comp.getCpr_email());
				dto.setCpr_estado(comp.getCpr_estado());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_pregunta(comp.getCpr_pregunta());
				dto.setCpr_respuesta(comp.getCpr_respuesta());
				dto.setCpr_fec_crea(Formatos.frmFechaHora(comp.getCpr_fec_crea()));
			}
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador " + comprador_id );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}
	
	/**
	 * Obtiene informacion del comprador según el Rut.
	 * 
	 * @param  rut	RUT comprador a buscar
	 * @return CompradoresDTO 	DTO con datos del comprador
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradoresByRut(long rut) throws CompradoresException, SystemException{
		CompradoresDTO dto = null;
		
		/**
		 * Instacia del DAO
		 */
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO(); 
		
		try {
			CompradoresEntity comp = dao.getCompradoresByRut(rut);
			if(comp!=null){
				dto = new CompradoresDTO();
				dto.setCpr_id(comp.getCpr_id());
				dto.setCpr_rut(comp.getCpr_rut());
				dto.setCpr_dv(comp.getCpr_dv());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_nombres(comp.getCpr_nombres());
				dto.setCpr_ape_pat(comp.getCpr_ape_pat());
				dto.setCpr_ape_mat(comp.getCpr_ape_mat());
				dto.setCpr_genero(comp.getCpr_genero());
				dto.setCpr_fono1(comp.getCpr_fono1());
				dto.setCpr_fono2(comp.getCpr_fono2());
				dto.setCpr_fono3(comp.getCpr_fono3());
				dto.setCpr_email(comp.getCpr_email());
				dto.setCpr_estado(comp.getCpr_estado());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_pregunta(comp.getCpr_pregunta());
				dto.setCpr_respuesta(comp.getCpr_respuesta());
				
				if( comp.getCpr_fec_login() != null )
					dto.setCpr_fec_login(comp.getCpr_fec_login().getTime());
				else
					dto.setCpr_fec_login( 0 );
				if( comp.getCpr_intentos()+"" != null )
					dto.setCpr_intentos(comp.getCpr_intentos());
				else
					dto.setCpr_intentos( 0 );
				
			}
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador " + rut );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}

	/**
	 * Inserta informacion del nuevo comprador
	 *  
	 * @param  comprador
	 * @return boolean
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public long setCreaComprador(CompradoresDTO comprador) throws CompradoresException, SystemException{
		long id = 0;
		
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try {

			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);

		} catch (CompradoresDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}		
		
		try{
			
			boolean flag_rollback = false;
			
			if (comprador.getCpr_id() == 0){//Si es que el comprador es nuevo
			
				//verifica si el rut ingresado existe, en este caso, senvia mensaje
				CompradoresEntity ent = dao.getCompradoresByRut(comprador.getCpr_rut());
				if(ent!=null)
					throw new CompradoresException("RUT:"+comprador.getCpr_rut()+Constantes.MSJE_COMP_RUT_EXISTE);
			
				if( comprador.getCpr_rut() != 0 )
					id = dao.setCreaComprador(comprador);
				
				if( id == 0 )
					flag_rollback = true;
				
			}
			logger.debug("ID: " + id);
			// Si es un comprador antiguo se recupera el ID 
			if( id == 0 ) {
				id = comprador.getCpr_id();
			}
			
			if (id>0){
				// agregar la relacion con sucursal				
				comprador.setCpr_id(id);
				//verifica si existe la relacion entre comprador y sucursal
				boolean saltar_s = false;
				boolean rel = true;
				
				List lst_suc = dao.getListSucursalesByCompradorId(comprador.getCpr_id());
				
				for(int i=0; i<lst_suc.size(); i++){
					ComprXSucDTO cs = (ComprXSucDTO)lst_suc.get(i);
					if(cs.getId_sucursal() == comprador.getId_sucursal())
						saltar_s = true;
				}				
				if( saltar_s == false )
					rel = dao.setRelCompradorSucursal(comprador);
				logger.debug("rel: "+rel);
				if(rel == false)
					flag_rollback = true;
				//agregar la relación con la empresa si es administrador	
				
				if (!comprador.getCpr_tipo().equals("")){		
					if( comprador.getCpr_tipo().equals("A") ) {
						boolean saltar = false;
						boolean rel1 = true;
						//verifica si existe la relacion entre comprador y empresa
						lst_suc = dao.getListAdmEmpresasByCompradorId(comprador.getCpr_id());
						for(int i=0; i<lst_suc.size(); i++){
							EmpresasDTO cs = (EmpresasDTO)lst_suc.get(i);
							if(cs.getEmp_id() == comprador.getId_empresa())
								saltar = true;
						}
						
						if( saltar == false )
							rel1 = dao.setRelCompradorEmpresa(comprador);
						logger.debug("rel1: "+rel1);
						if(rel1 == false)
							flag_rollback = true;
					}
				}
				logger.debug("3");
			}
			
			if( flag_rollback == true )
				throw new CompradoresDAOException( "Error en las consultas." );			
			
		   //Finaliza la transacción
		   trx.end();			
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
				logger.warn("Comprador ya existe." );
				throw new CompradoresException(Constantes._EX_KEY_DUPLICADA, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (DAOException e) {
		    try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		    e.printStackTrace();
		    throw new SystemException(e);
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
		}
		
		return id;
	}

	/**
	 * Modifica informacion del comprador
	 * 
	 * @param  comprador	información del comprador (CompradoresDTO)
	 * @return boolean		True: éxito False:error
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean setModComprador(CompradoresDTO comprador) throws CompradoresException, SystemException{
		boolean result = false;
		
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();		
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try {

			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);

		} catch (CompradoresDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}		
		
		try{
			
			boolean flag_rollback = false;
			
			//revisar si modifico el rut, si el rut modificado existe , enviar mensaje
			CompradoresEntity ent = dao.getCompradoresById(comprador.getCpr_id());
			if(ent.getCpr_rut() != comprador.getCpr_rut()){
				CompradoresEntity entR = dao.getCompradoresByRut(comprador.getCpr_rut());
				if (entR != null){
					throw new CompradoresException("Rut:"+comprador.getCpr_rut()+Constantes.MSJE_COMP_RUT_MOD_EXISTE);
				}
			}
			result = dao.setModComprador(comprador);
			if( result == false )
				flag_rollback = true;

			//modificar la relación con la empresa si es administrador
			
			if (!comprador.getCpr_tipo().equals("")){			
				boolean rel = false;			
				if( comprador.getCpr_tipo().equals("A") ) {
					rel = dao.setRelCompradorEmpresa(comprador);
				} else {
					rel = dao.delRelCompradorEmpresa(comprador);				
				}
				logger.debug("rel: "+rel);							
				if( rel == false )
					flag_rollback = true;				
			}
			
			
			if( flag_rollback == true )
				throw new CompradoresDAOException( "Error en las consultas." );

		   //Finaliza la transacción
		   trx.end();
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador" );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (DAOException e) {
		    try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		    e.printStackTrace();
		    throw new SystemException(e);			
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Asocia comprador con la sucursal y con la empresa si es administrador de esta
	 * 
	 * @param  comprador	información del comprador (CompradoresDTO)
	 * @return boolean		True: éxito False:error
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean setSucEmpComprador(CompradoresDTO comprador) throws CompradoresException, SystemException{
		boolean result = false;
		
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();		
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try {

			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);

		} catch (CompradoresDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}		
		
		try{
			
			boolean flag_rollback = false;
			
			result = dao.setModComprador(comprador);
			if( result == false )
				flag_rollback = true;

			// Relación con la sucursal
			if(result){
				boolean res = dao.setRelCompradorSucursal(comprador);
				logger.debug("res: "+res);
				if( res == false )
					flag_rollback = true;				
			}

			// Relación con la empresa si es administrador
			boolean res;
			if( comprador.getCpr_tipo().equals("A") ) {
				res = dao.setRelCompradorEmpresa(comprador);
			} else {
				res = dao.delRelCompradorEmpresa(comprador);
			}
			logger.debug("res: "+res);			
			
			if( res == false )
				flag_rollback = true;
			
			if( flag_rollback == true )
				throw new CompradoresDAOException( "Error en las consultas." );

		   //Finaliza la transacción
		   trx.end();
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador" );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (DAOException e) {
		    try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		    e.printStackTrace();
		    throw new SystemException(e);			
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
		}
		
		return result;
	}	
	
	/**
	 * Elimina un comprador
	 * 
	 * @param  comprador_id	Identificador único del comprador
	 * @return True: éxito False: error
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean delCompradores(long comprador_id) throws CompradoresException, SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			CompradoresDTO dto = new CompradoresDTO(); 
			dto.setCpr_id(comprador_id);
			result = dao.delComprador(dto);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador");
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}


	/**
	 * Obtiene el listado de compradores de una sucursal, segun el id de sucursal
	 * 
	 * @param  id_sucursal
	 * @return List 
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			List lst_com = dao.getListCompradoresBySucursalId(id_sucursal);
			CompradoresDTO dto = null;
			for(int i=0;i<lst_com.size(); i++){
				CompradoresEntity ent = (CompradoresEntity)lst_com.get(i);
				dto = new CompradoresDTO();
				dto.setCpr_id(ent.getCpr_id());
				dto.setCpr_rut(ent.getCpr_rut());
				dto.setCpr_dv(ent.getCpr_dv());
				dto.setCpr_nombres(ent.getCpr_nombres());
				dto.setCpr_ape_pat(ent.getCpr_ape_pat());
				dto.setCpr_ape_mat(ent.getCpr_ape_mat());
				result.add(dto);
			}
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de compradores de una sucursal, segun el id de sucursal, TipoAcceso y comprador_id
	 * 
	 * @param  id_sucursal, TipoAcceso y comprador_id
	 * @return List 
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal, String TipoAcceso, long comprador_id) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			List lst_com = dao.getListCompradoresBySucursalId(id_sucursal, TipoAcceso, comprador_id);
			CompradoresDTO dto = null;
			for(int i=0;i<lst_com.size(); i++){
				CompradoresEntity ent = (CompradoresEntity)lst_com.get(i);
				dto = new CompradoresDTO();
				dto.setCpr_id(ent.getCpr_id());
				dto.setCpr_rut(ent.getCpr_rut());
				dto.setCpr_dv(ent.getCpr_dv());
				dto.setCpr_nombres(ent.getCpr_nombres());
				dto.setCpr_ape_pat(ent.getCpr_ape_pat());
				dto.setCpr_ape_mat(ent.getCpr_ape_mat());
				result.add(dto);
			}
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	
	/**
	 * Obtiene el listado de compradores que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  dtoC
	 * @return List CompradoresDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getCompradoresByCriteria(CompradorCriteriaDTO dtoC) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			List lst_com = dao.getCompradoresByCriteria(dtoC);
			CompradoresDTO dto = null;
			for(int i=0;i<lst_com.size(); i++){
				CompradoresEntity ent = (CompradoresEntity)lst_com.get(i);
				dto = new CompradoresDTO();
				dto.setCpr_id(ent.getCpr_id());
				dto.setCpr_rut(ent.getCpr_rut());
				dto.setCpr_dv(ent.getCpr_dv());
				dto.setCpr_nombres(ent.getCpr_nombres());
				dto.setCpr_ape_pat(ent.getCpr_ape_pat());
				dto.setCpr_ape_mat(ent.getCpr_ape_mat());
				result.add(dto);
			}
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene la cantidad de compradores que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  dto
	 * @return int
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public int getCompradoresCountByCriteria(CompradorCriteriaDTO dto) throws CompradoresException, SystemException{
		int result = 0;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getCompradoresCountByCriteria(dto);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListSucursalesByCompradorId(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListSucursalesByCompradorId(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	
	/**
	 * Obtiene el listado de sucursales relacionadas a un Usuario
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListSucursalesByUser(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListSucursalesByUser(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de sucursales relacionadas a un Usuario
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public String getListSucursalesByUser2(long id_comprador) throws CompradoresException, SystemException{
		String result = "";
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListSucursalesByUser2(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	
	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador para las empresas que el administrador tiene acceso
	 * 
	 * @param id_comprador
	 * @param id_administrador
	 * @return List ComprXSucDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */	
	public List getListSucursalesByCompradorId(long id_comprador, long id_administrador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListSucursalesByCompradorId(id_comprador, id_administrador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}	
	

	/**
	 * Agrega la relacion entre sucursal y comprador
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO dto) throws CompradoresException, SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si existe la relacion entre comprador y sucursal
			List lst_suc = dao.getListSucursalesByCompradorId(dto.getId_comprador());
			for(int i=0; i<lst_suc.size(); i++){
				ComprXSucDTO cs = (ComprXSucDTO)lst_suc.get(i);
				if(cs.getId_sucursal() == dto.getId_sucursal())
					throw new CompradoresException("La relacion entre sucursal y comprador ya existe. No se puede agregar");
			}
			
			result = dao.addRelSucursalComprador(dto);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Elimina la relacion entre sucursal y comprador
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO dto) throws CompradoresException, SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si existe la relacion entre comprador y sucursal
			List lst_suc = dao.getListSucursalesByCompradorId(dto.getId_comprador());
			boolean existe = false;
			for(int i=0; i<lst_suc.size(); i++){
				ComprXSucDTO cs = (ComprXSucDTO)lst_suc.get(i);
				if(cs.getId_sucursal() == dto.getId_sucursal())
					existe = true;
			}
			if(!existe)
				throw new CompradoresException("La relacion entre sucursal y comprador no existe. No se puede eliminar");
			
			result = dao.delRelSucursalComprador(dto);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de sucursales no asociados a un comprador
	 * 
	 * @param id_comprador Identificador único del comprador
	 * @param id_administrador Identificador único del comprador administrador
	 * @return List SucursalesDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListSucursalesNoAsocComprador(long id_administrador, long id_comprador)  throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListSucursalesNoAsocComprador(id_administrador, id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene informacion del comprador, y el tipo de comprador segun id de sucursal
	 *  
	 * @param id_comprador
	 * @param id_sucursal
	 * @return CompradoresDTO
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradorXSucursal(long id_comprador, long id_sucursal) throws CompradoresException, SystemException{
		CompradoresDTO dto = null;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO(); 
		
		try {
			CompradoresEntity comp = dao.getCompradoresById(id_comprador);
			if(comp!=null){
				dto = new CompradoresDTO();
				dto.setCpr_id(comp.getCpr_id());
				dto.setCpr_rut(comp.getCpr_rut());
				dto.setCpr_dv(comp.getCpr_dv());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_nombres(comp.getCpr_nombres());
				dto.setCpr_ape_pat(comp.getCpr_ape_pat());
				dto.setCpr_ape_mat(comp.getCpr_ape_mat());
				dto.setCpr_genero(comp.getCpr_genero());
				dto.setCpr_fono1(comp.getCpr_fono1());
				dto.setCpr_fono2(comp.getCpr_fono2());
				dto.setCpr_fono3(comp.getCpr_fono3());
				dto.setCpr_email(comp.getCpr_email());
				dto.setCpr_estado(comp.getCpr_estado());
				dto.setCpr_pass(comp.getCpr_pass());
				dto.setCpr_pregunta(comp.getCpr_pregunta());
				dto.setCpr_respuesta(comp.getCpr_respuesta());
				dto.setCpr_fec_crea(Formatos.frmFechaHora(comp.getCpr_fec_crea()));
				
				String tipo = dao.getComprXSuc(id_comprador, id_sucursal);
				dto.setCpr_tipo(tipo);
			}
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador " + id_comprador );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}

	/**
	 * Obtiene el listado de Empresas en la cual el comprador es administrador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListAdmEmpresasByCompradorId(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de sucursales para las empresas a las cuales el comprador es administrador.
	 * 
	 * @param id_comprador	Identificador único del comprador
	 * @return Lista con datos (SucursalesDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListAdmSucursalesByCompradorId(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListAdmSucursalesByCompradorId(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de compradores relacionados a una empresa, segun el id de la empresa.
	 * 
	 * @param  id_empresa Identificador de la empresa
	 * @return List CompradoresEntity
	 * @throws CompradoresException, SystemException
	 */
	public List getListCompradoresByEmpresalId(long id_empresa) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			// Si el tipo es mayor a 0, es administrador de la empresa (tipo = id_empresa)
			result = dao.getListCompradoresByEmpresalId(id_empresa);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}	

	/**
	 * Obtiene el listado de compradores para las sucursales que el administrador tiene acceso.
	 * 
	 * @param id_administrador Identificador único del administrador
	 * @return Listado con datos de los compradores
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public List getListAdmCompradoresByAdministradorId(long id_administrador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListAdmCompradoresByAdministradorId(id_administrador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Agrega la relacion entre empresa y comprador, donde el comprador sera administrador
	 *  
	 * @param dto
	 * @return true, si agrego con exito, false en caso contrario
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean addRelEmpresaComprador(ComprXEmpDTO dto) throws CompradoresException, SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si existe la relacion entre comprador y empresa
			List lst_suc = dao.getListAdmEmpresasByCompradorId(dto.getId_comprador());
			for(int i=0; i<lst_suc.size(); i++){
				EmpresasDTO cs = (EmpresasDTO)lst_suc.get(i);
				if(cs.getEmp_id() == dto.getId_empresa())
					throw new CompradoresException("La relacion entre empresa y comprador ya existe. No se puede agregar");
			}
			CompradoresDTO dtoC = new CompradoresDTO();
			dtoC.setCpr_id(dto.getId_comprador());
			dtoC.setId_empresa(dto.getId_empresa());
			result = dao.setRelCompradorEmpresa(dtoC);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Elimina la relacion entre empresa y comprador, donde el comprador era administrador
	 * 
	 * @param dto
	 * @return true, si agrego con exito, false en caso contrario
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean delRelEmpresaComprador(ComprXEmpDTO dto) throws CompradoresException, SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si existe la relacion entre comprador y empresa
			List lst_suc = dao.getListAdmEmpresasByCompradorId(dto.getId_comprador());
			boolean existe = false;
			for(int i=0; i<lst_suc.size(); i++){
				EmpresasDTO cs = (EmpresasDTO)lst_suc.get(i);
				if(cs.getEmp_id() == dto.getId_empresa())
					existe = true;
			}
			if(!existe)
				throw new CompradoresException("La relacion entre empresa y comprador no existe. No se puede eliminar");
			
			CompradoresDTO dtoC = new CompradoresDTO();
			dtoC.setId_empresa(dto.getId_empresa());
			dtoC.setCpr_id(dto.getId_comprador());
			result = dao.delRelCompradorEmpresa(dtoC);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByCompradorId(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListEmpresasByCompradorId(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}	
	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByUser(long id_comprador) throws CompradoresException, SystemException{
		List result = new ArrayList();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.getListEmpresasByUser(id_comprador);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}	

	
	/**
	 * Obtiene los datos del ejecutivo fono empresa
	 * @param  login
	 * @return UserDTO
	 * @throws CompradoresDAOException
	 */	
	public UserDTO getEjecutivoGetByRut(String login )	throws CompradoresException, SystemException{
		UserDTO dto = null;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO(); 
		
		try {
			UserDTO eje = dao.getEjecutivoGetByRut(login);
			if(eje != null){
				dto = new UserDTO();
				dto.setId_usuario(eje.getId_usuario());
				dto.setPassword(eje.getPassword());
			}		
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe el ejecutivo " + login );
				throw new CompradoresException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return dto;	
	}	
	
	/**
	 * Verifica si el comprador es administrador de la empresa
	 * @param id_comprador
	 * @param id_sucursal
	 * @return
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean esAdministrador(long id_comprador, long id_sucursal) throws CompradoresException , SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si es administrador
			result = dao.esAdministrador(id_comprador,id_sucursal);
			
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new CompradoresException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Modifica los datos password - estado de un comprador
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @throws VteException, SystemException
	 */
	public boolean compradorChangePass(CompradoresDTO comprador) throws CompradoresException , SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			//verifica si es administrador
			result = dao.compradorChangePass(comprador);
			
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new CompradoresException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	
	/**
	 * Modifica la cantidad de intentos de logeo de un comprador
	 * 
	 * @param comprador_id 	Identificador único del comprador
	 * @param accion		Acción a gatillar 1: Aumentar, 0: Reset
	 * @throws VteException 
	 */
	public boolean updateIntentos(long comprador_id, long accion) throws CompradoresException , SystemException{
		boolean result = false;
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try{
			result = dao.updateIntentos(comprador_id, accion);
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			throw new CompradoresException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Ejecucion del Wizard
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @param  sucursales Lista con datos de la sucursal
	 * @param  empresas Lista DTO con datos de la empresa
	 * @throws VteException
	 */
	public boolean setExecWizard(CompradoresDTO comprador, List sucursales, List empresas) throws CompradoresException , SystemException{	
		long id = 0;
		boolean resultsuc;
		boolean resultemp;
		ComprXSucDTO dto_suc = null;
		CompradoresDTO dto_emp = null;
		List suc = (List) sucursales;
		List emp = (List) empresas;

		
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();
		
		JdbcCompradoresDAO dao = (JdbcCompradoresDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCompradoresDAO();
		
		try {

			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);

		} catch (CompradoresDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}		
		
		try{
			
			boolean flag_rollback = false;
			
			if (comprador.getCpr_id() == 0){//Si es que el comprador es nuevo
			
				//verifica si el rut ingresado existe, en este caso, senvia mensaje
				CompradoresEntity ent = dao.getCompradoresByRut(comprador.getCpr_rut());
				if(ent!=null)
					throw new CompradoresException("RUT:"+comprador.getCpr_rut()+Constantes.MSJE_COMP_RUT_EXISTE);
			
				if( comprador.getCpr_rut() != 0 )
					id = dao.setCreaComprador(comprador);
				
				if( id == 0 )
					flag_rollback = true;
				
			}
			logger.debug("ID: " + id);
			// Si es un comprador antiguo se recupera el ID 
			if( id == 0 ) {
				id = comprador.getCpr_id();
			}
			
			if (id>0){
				// Se recorre la lista de sucursales y se asigna al comprador
				for(int i=0; i < suc.size(); ++i){
					dto_suc = new ComprXSucDTO();
					ComprXSucDTO cs = (ComprXSucDTO) suc.get(i);
					dto_suc.setId_comprador(id);
					dto_suc.setId_sucursal(cs.getId_sucursal());
					resultsuc = dao.addRelSucursalComprador(dto_suc);
					logger.debug("resultsuc: "+resultsuc);
					if (resultsuc == false)
						flag_rollback = true;
				}

				// Se recorre la lista de empresas y se asigna al comprador
				if( emp != null ) {
					for(int i=0; i < emp.size(); ++i){
						dto_emp = new CompradoresDTO();
						ComprXEmpDTO ce = (ComprXEmpDTO) emp.get(i);
						dto_emp.setCpr_id(id);
						dto_emp.setId_empresa(ce.getId_empresa());
						resultemp = dao.setRelCompradorEmpresa(dto_emp);
						logger.debug("resultemp: "+resultemp);
						if(resultemp == false)
							flag_rollback = true;
					}
				}
			}
			
			if( flag_rollback == true )
				throw new CompradoresDAOException( "Error en las consultas." );			
			
		   //Finaliza la transacción
		   trx.end();			
			
		}catch(CompradoresDAOException ex){
			logger.warn("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
				logger.warn("Comprador ya existe." );
				throw new CompradoresException(Constantes._EX_KEY_DUPLICADA, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (DAOException e) {
		    try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		    e.printStackTrace();
		    throw new SystemException(e);
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
		}
		
		return true;
	}
	
}
