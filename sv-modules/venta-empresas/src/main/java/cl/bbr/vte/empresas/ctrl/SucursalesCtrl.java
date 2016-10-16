package cl.bbr.vte.empresas.ctrl;


import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.SucursalesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.vte.empresas.dao.DAOFactory;
import cl.bbr.vte.empresas.dao.JdbcSucursalesDAO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.exceptions.EmpresasException;
import cl.bbr.vte.empresas.exceptions.SucursalesDAOException;
import cl.bbr.vte.empresas.exceptions.SucursalesException;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO sucursales a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class SucursalesCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	public SucursalesDTO getSucursalById(long id) throws SucursalesException, SystemException{
		SucursalesDTO dto = null;
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			SucursalesEntity ent = dao.getSucursalById(id);
			if(ent != null){
				dto = new SucursalesDTO();
				dto.setSuc_id(ent.getSuc_id().longValue());
				dto.setSuc_emp_id(ent.getSuc_emp_id().longValue());
				dto.setSuc_rut(ent.getSuc_rut().longValue());
				dto.setSuc_dv(ent.getSuc_dv());
				dto.setSuc_razon(ent.getSuc_razon());
				dto.setSuc_nombre(ent.getSuc_nombre());
				dto.setSuc_descr(ent.getSuc_descr());
				dto.setSuc_clave(ent.getSuc_clave());
				dto.setSuc_email(ent.getSuc_email());
				dto.setSuc_fono_cod1(ent.getSuc_fono_cod1());
				dto.setSuc_fono_num1(ent.getSuc_fono_num1());
				dto.setSuc_fono_cod2(ent.getSuc_fono_cod2());
				dto.setSuc_fono_num2(ent.getSuc_fono_num2());
				dto.setSuc_rec_info(ent.getSuc_rec_info().intValue());
				dto.setSuc_fec_crea(Formatos.frmFechaHora(ent.getSuc_fec_crea()));
				dto.setSuc_fec_act(Formatos.frmFechaHora(ent.getSuc_fec_act()));
				dto.setSuc_estado(ent.getSuc_estado());
				dto.setSuc_fec_nac(Formatos.frmFecha(ent.getSuc_fec_nac()));
				dto.setSuc_genero(ent.getSuc_genero());
				dto.setSuc_pregunta(ent.getSuc_pregunta());
				dto.setSuc_respuesta(ent.getSuc_respuesta());
				dto.setSuc_bloqueo(ent.getSuc_bloqueo());
				dto.setSuc_mod_dato(ent.getSuc_mod_dato());
				dto.setSuc_fec_login(Formatos.frmFechaHora(ent.getSuc_fec_login()));
				dto.setSuc_intentos(ent.getSuc_intentos().intValue());
				dto.setSuc_tipo(ent.getSuc_tipo());
				
				/*
				///obtener listado de compradores
				List lst_comp_dto = new ArrayList();
				List lst_comp = dao.getCompradoresBySucursalId(id);
				CompradoresDTO dtoC = null;
				for(int i=0; i<lst_comp.size(); i++){
					CompradoresEntity entC = (CompradoresEntity)lst_comp.get(i);
					dtoC = new CompradoresDTO();
					dtoC.setCpr_id(entC.getCpr_id());
					dtoC.setCpr_rut(entC.getCpr_rut());
					dtoC.setCpr_dv(entC.getCpr_dv());
					dtoC.setCpr_nombres(entC.getCpr_nombres());
					dtoC.setCpr_ape_pat(entC.getCpr_ape_pat());
					dtoC.setCpr_ape_mat(entC.getCpr_ape_mat());
					dtoC.setCpr_genero(entC.getCpr_genero());
					dtoC.setCpr_tipo(entC.getCpr_tipo());
					dtoC.setCpr_fono1(entC.getCpr_fono1());
					dtoC.setCpr_fono2(entC.getCpr_fono2());
					dtoC.setCpr_fono3(entC.getCpr_fono3());
					dtoC.setCpr_email(entC.getCpr_email());
					dtoC.setCpr_fmod(Formatos.frmFechaHora(entC.getCpr_fmod()));
					dtoC.setCpr_estado(entC.getCpr_estado());
					dtoC.setCpr_pass(entC.getCpr_pass());
					dtoC.setCpr_fec_crea(Formatos.frmFechaHora(entC.getCpr_fec_crea()));
					dtoC.setCpr_pregunta(entC.getCpr_pregunta());
					dtoC.setCpr_respuesta(entC.getCpr_respuesta());
					dtoC.setCpr_bloqueo(entC.getCpr_bloqueo());
					dtoC.setCpr_mod_dato(entC.getCpr_mod_dato());
					dtoC.setCpr_fec_login(Formatos.frmFechaHora(entC.getCpr_fec_login()));
					dtoC.setCpr_intentos(entC.getCpr_intentos());
					lst_comp_dto.add(dtoC);
				}
				dto.setLst_comprador(lst_comp_dto);
				
				//obtener listado de dirección de despacho
				List lst_dir_desp_dto = new ArrayList();
				List lst_dir_desp = dao.getDirsDespachoBySucursalId(id);
				DireccionesDTO dtoD = null;
				for(int i=0; i<lst_dir_desp.size(); i++){
					DireccionEntity entD = (DireccionEntity)lst_dir_desp.get(i);
					dtoD = new DireccionesDTO();
					dtoD.setId(entD.getId().longValue());
					dtoD.setLoc_cod(entD.getLoc_cod().longValue());
					dtoD.setTip_id(entD.getTip_id().longValue());
					dtoD.setCli_id(entD.getCli_id().longValue());
					dtoD.setCom_id(entD.getCom_id().longValue());
					dtoD.setZona_id(entD.getZona_id().longValue());
					dtoD.setAlias(entD.getAlias());
					dtoD.setCalle(entD.getCalle());
					dtoD.setNumero(entD.getNumero());
					dtoD.setDepto(entD.getDepto());
					dtoD.setComentarios(entD.getComentarios());
					dtoD.setEstado(entD.getEstado().toString());
					dtoD.setFec_crea(Formatos.frmFechaHora(entD.getFec_crea()));
					dtoD.setFnueva(entD.getFnueva().toString());
					dtoD.setCuadrante(entD.getCuadrante());
					dtoD.setCiudad(entD.getCiudad());
					dtoD.setFax(entD.getFax());
					dtoD.setReg_id(entD.getReg_id().longValue());
					dtoD.setOtra_comuna(entD.getOtra_comuna());
					lst_dir_desp_dto.add(dtoD);
				}
				dto.setLst_dir_despacho(lst_dir_desp_dto);
				
				//obtener listado de dirección de facturación
				List lst_dir_fac_dto  = new ArrayList();
				List lst_dir_fac = dao.getDirsFacturacionBySucursalId(id);
				DirFacturacionDTO dtoF = null;
				for(int i=0; i<lst_dir_fac.size(); i++){
					DirFacturacionEntity entF = (DirFacturacionEntity)lst_dir_fac.get(i);
					dtoF = new DirFacturacionDTO();
					dtoF.setDfac_id(entF.getDfac_id().longValue());
					dtoF.setDfac_tip_id(entF.getDfac_tip_id().longValue());
					dtoF.setDfac_cli_id(entF.getDfac_cli_id().longValue());
					dtoF.setDfac_com_id(entF.getDfac_com_id().longValue());
					dtoF.setDfac_alias(entF.getDfac_alias());
					dtoF.setDfac_calle(entF.getDfac_calle());
					dtoF.setDfac_numero(entF.getDfac_numero());
					dtoF.setDfac_depto(entF.getDfac_depto());
					dtoF.setDfac_comentarios(entF.getDfac_comentarios());
					dtoF.setDfac_estado(entF.getDfac_estado());
					dtoF.setDfac_ciudad(entF.getDfac_ciudad());
					dtoF.setDfac_fax(entF.getDfac_fax());
					dtoF.setDfac_nom_contacto(entF.getDfac_nom_contacto());
					dtoF.setDfac_cargo(entF.getDfac_cargo());
					dtoF.setDfac_email(entF.getDfac_email());
					dtoF.setDfac_fono1(entF.getDfac_fono1());
					dtoF.setDfac_fono2(entF.getDfac_fono2());
					dtoF.setDfac_fono3(entF.getDfac_fono3());
					lst_dir_fac_dto.add(dtoF);
				}
				dto.setLst_dir_facturacion(lst_dir_fac_dto);
				*/
				
				}
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}

	/**
	 * Obtiene el listado de las sucursales que coincidan con los criterios de búsqueda ingresados 
	 * 
	 * @param criterio
	 * @return List SucursalesDTO
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio) throws SucursalesException, SystemException{
		List lst_suc = new ArrayList();
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			List lst_ent = dao.getSucursalesByCriteria(criterio);
			SucursalesDTO dto = null;
			for(int i=0; i<lst_ent.size(); i++){
				SucursalesEntity ent = (SucursalesEntity)lst_ent.get(i);
				dto = new SucursalesDTO();
				dto.setSuc_id(ent.getSuc_id().longValue());
				dto.setSuc_emp_id(ent.getSuc_emp_id().longValue());
				dto.setSuc_rut(ent.getSuc_rut().longValue());
				dto.setSuc_dv(ent.getSuc_dv());
				dto.setSuc_razon(ent.getSuc_razon());
				dto.setSuc_descr(ent.getSuc_descr());
				dto.setSuc_nombre(ent.getSuc_nombre());
				dto.setSuc_clave(ent.getSuc_clave());
				dto.setSuc_email(ent.getSuc_email());
				dto.setSuc_fono_cod1(ent.getSuc_fono_cod1());
				dto.setSuc_fono_num1(ent.getSuc_fono_num1());
				dto.setSuc_fono_cod2(ent.getSuc_fono_cod2());
				dto.setSuc_fono_num2(ent.getSuc_fono_num2());
				dto.setSuc_rec_info(ent.getSuc_rec_info().intValue());
				dto.setSuc_fec_crea(Formatos.frmFechaHora(ent.getSuc_fec_crea()));
				dto.setSuc_fec_act(Formatos.frmFechaHora(ent.getSuc_fec_act()));
				dto.setSuc_estado(ent.getSuc_estado());
				dto.setSuc_fec_nac(Formatos.frmFecha(ent.getSuc_fec_nac()));
				dto.setSuc_genero(ent.getSuc_genero());
				dto.setSuc_pregunta(ent.getSuc_pregunta());
				dto.setSuc_respuesta(ent.getSuc_respuesta());
				dto.setSuc_bloqueo(ent.getSuc_bloqueo());
				dto.setSuc_mod_dato(ent.getSuc_mod_dato());
				dto.setSuc_fec_login(Formatos.frmFechaHora(ent.getSuc_fec_login()));
				dto.setSuc_intentos(ent.getSuc_intentos().intValue());
				dto.setSuc_tipo(ent.getSuc_tipo());
				dto.setSuc_desc_estado(ent.getSuc_desc_estado());
				dto.setNom_empresa(ent.getEmp_nom());
				lst_suc.add(dto);
			}
				
				
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return lst_suc;
	}

	/**
	 * Obtiene la cantidad de sucursales que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  criterio
	 * @return int
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public int getSucursalesCountByCriteria(SucursalCriteriaDTO criterio) throws SucursalesException, SystemException{
		int cant = 0;
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			cant = dao.getSucursalesCountByCriteria(criterio);
			
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return cant;
	}

	/**
	 * Obtiene el listado de sucursales que pertenecen a una empresa, segun el id de la empresa
	 * 
	 * @param id_empresa
	 * @return List SucursalesDTO
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public List getSucursalesByEmpresaId(long id_empresa) throws SucursalesException, SystemException {
		List lst_dto = new ArrayList();
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			List lst_ent = dao.getSucursalesByEmpresaId(id_empresa);
			SucursalesDTO dtoS = null;
			for(int i=0; i<lst_ent.size(); i++){
				SucursalesEntity entS = (SucursalesEntity)lst_ent.get(i);
				dtoS = new SucursalesDTO();
				dtoS.setSuc_id(entS.getSuc_id().longValue());
				dtoS.setSuc_emp_id(entS.getSuc_emp_id().longValue());
				dtoS.setSuc_rut(entS.getSuc_rut().longValue());
				dtoS.setSuc_dv(entS.getSuc_dv());
				dtoS.setSuc_razon(entS.getSuc_razon());
				dtoS.setSuc_descr(entS.getSuc_descr());
				dtoS.setSuc_nombre(entS.getSuc_nombre());
				dtoS.setSuc_clave(entS.getSuc_clave());
				dtoS.setSuc_email(entS.getSuc_email());
				dtoS.setSuc_fono_cod1(entS.getSuc_fono_cod1());
				dtoS.setSuc_fono_num1(entS.getSuc_fono_num1());
				dtoS.setSuc_fono_cod2(entS.getSuc_fono_cod2());
				dtoS.setSuc_fono_num2(entS.getSuc_fono_num2());
				dtoS.setSuc_rec_info(entS.getSuc_rec_info().intValue());
				dtoS.setSuc_fec_crea(Formatos.frmFechaHora(entS.getSuc_fec_crea()));
				dtoS.setSuc_fec_act(Formatos.frmFechaHora(entS.getSuc_fec_act()));
				dtoS.setSuc_estado(entS.getSuc_estado());
				dtoS.setSuc_fec_nac(Formatos.frmFecha(entS.getSuc_fec_nac()));
				dtoS.setSuc_genero(entS.getSuc_genero());
				dtoS.setSuc_pregunta(entS.getSuc_pregunta());
				dtoS.setSuc_respuesta(entS.getSuc_respuesta());
				dtoS.setSuc_bloqueo(entS.getSuc_bloqueo());
				dtoS.setSuc_mod_dato(entS.getSuc_mod_dato());
				dtoS.setSuc_fec_login(Formatos.frmFechaHora(entS.getSuc_fec_login()));
				dtoS.setSuc_intentos(entS.getSuc_intentos().intValue());
				dtoS.setSuc_tipo(entS.getSuc_tipo());
				lst_dto.add(dtoS);
				
			}
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return lst_dto;
	}

	
	/**
	 * Permite crear la sucursal con los datos ingresados en el formulario
	 * 
	 * @param  prm , contiene la información del formulario
	 * @return long
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public long setCreaSucursal(SucursalesDTO prm) throws SucursalesException, SystemException {
		long id= 0;
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			//verifica si el rut de sucursal ingresado existe, si es asi, envia mensaje
			//SucursalesEntity ent = dao.getSucursalByRut(prm.getSuc_rut());
			//if(ent!=null)
			//	throw new SucursalesException("RUT:"+prm.getSuc_rut() +Constantes.MSJE_SUCR_RUT_EXISTE);
			
			id = dao.insSucursal(prm);
			logger.debug("En setCreaSucursal(), id:"+id);
			if(id <= 0)
				throw new SucursalesException(Constantes._EX_SUC_ID_NO_CREADO);
			
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return id;
	}

	/**
	 * Permite actualizar los datos de la sucursal, segun las modificaciones ingresadas en el formulario de detalle
	 *  
	 * @param  prm , contiene la información del formulario
	 * @return boolean,
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public boolean setModSucursal(SucursalesDTO prm) throws SucursalesException, SystemException {
		boolean result = false;
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			//revisar si modifico el rut, si el rut modificado existe , enviar mensaje
			/*SucursalesEntity ent = dao.getSucursalById(prm.getSuc_id());
			if(ent.getSuc_rut().longValue() != prm.getSuc_rut()){
				SucursalesEntity entR = dao.getSucursalByRut(prm.getSuc_rut());
				if(entR != null){
					throw new SucursalesException("Rut:"+prm.getSuc_rut()+Constantes.MSJE_SUCR_RUT_MOD_EXISTE);
				}
			}*/
			result = dao.modSucursal(prm);
			logger.debug("En setModSucursal(), result:"+result);
			if(!result)
				throw new EmpresasException(Constantes._EX_SUC_ID_NO_CREADO);
			
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return result;
	}

	
	/** Realiza un cambio de estado al campo cli_estado a 'E'
	 * @param prm : SucursalesDTO
	 * @return : True o False
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public boolean delSucursal(SucursalesDTO prm) throws SucursalesException, SystemException {
		boolean result = false;
		
		JdbcSucursalesDAO dao = (JdbcSucursalesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSucursalesDAO();
		
		try{
			result = dao.delSucursal(prm);
			logger.debug("En delSucursal(), result:"+result);
			
		}catch(SucursalesDAOException e){
			logger.debug("problema:"+e);
			throw new SucursalesException(e);
			
		}catch (Exception e) {
			throw new SystemException(e);
		}
		return result;
	}	
	
}
