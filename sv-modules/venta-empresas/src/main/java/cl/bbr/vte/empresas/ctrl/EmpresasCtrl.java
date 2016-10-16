package cl.bbr.vte.empresas.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dao.DAOFactory;
import cl.bbr.vte.empresas.dao.JdbcEmpresasDAO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresaLogDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;
import cl.bbr.vte.empresas.exceptions.EmpresasException;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO Empresas a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class EmpresasCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Obtiene datos de la empresa y listado de sucursales, segun id
	 * 
	 * @param  id	identificador único del la empresa
	 * @return EmpresasDTO 
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public EmpresasDTO getEmpresaById(long id) throws EmpresasException, SystemException{
		EmpresasDTO dto = new EmpresasDTO();
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			EmpresasEntity ent = dao.getEmpresaById(id);
			if(ent!=null){
				dto = new EmpresasDTO();
				dto.setEmp_id(ent.getId().longValue());
				dto.setEmp_rut(ent.getRut().longValue());
				dto.setEmp_dv(ent.getDv().toString());
				dto.setEmp_nom(ent.getNombre());
				dto.setEmp_descr(ent.getDescripcion());
				dto.setEmp_rzsocial(ent.getRsocial());
				dto.setEmp_rubro(ent.getRubro());
				dto.setEmp_tamano(ent.getTamano());
				dto.setEmp_qtyemp(ent.getQtyemp().intValue());
				dto.setEmp_nom_contacto(ent.getNom_contacto());
				dto.setEmp_fono1_contacto(ent.getFono1_contacto());
				dto.setEmp_fono2_contacto(ent.getFono2_contacto());
				dto.setEmp_fono3_contacto(ent.getFono3_contacto());
				dto.setEmp_mail_contacto(ent.getMail_contacto());
				dto.setEmp_cargo_contacto(ent.getCargo_contacto());
				dto.setEmp_saldo(ent.getSaldo().doubleValue());
				dto.setEmp_fact_saldo(Formatos.frmFechaHora(ent.getFact_saldo()));
				dto.setEmp_fmod(Formatos.frmFechaHora(ent.getFmod()));
				dto.setEmp_estado(ent.getEstado());
				dto.setEmp_mrg_minimo(ent.getMrg_minimo().doubleValue());
				dto.setEmp_fec_crea(Formatos.frmFechaHora(ent.getFec_crea()));
				dto.setEmp_mod_rzsoc(ent.getMod_rzsoc().intValue());
				dto.setEmp_dscto_max(ent.getDscto_max().doubleValue());
				dto.setEmp_cod_fon1(ent.getCod_fon1());
				dto.setEmp_cod_fon2(ent.getCod_fon2());
				dto.setEmp_cod_fon3(ent.getCod_fon3());
				
				//obtiene información de linea de crédito
				/*CargosEntity entCar = dao.getCargoByEmpresaId(id);
				CargosDTO   dtoCar = null;
				if(entCar != null){
					dtoCar = new CargosDTO();
					dtoCar.setId_cargo(entCar.getId_cargo().longValue());
					dtoCar.setId_empresa(entCar.getId_empresa().longValue());
					dtoCar.setId_pedido(entCar.getId_pedido().longValue());
					dtoCar.setMonto_cargo(entCar.getMonto_cargo().doubleValue());
					dtoCar.setFecha_ing(Formatos.frmFechaHora(entCar.getFecha_ing()));
				}
				dto.setCargo(dtoCar);*/
			}
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new EmpresasException(Constantes._EX_EMP_ID_NO_EXISTE, ex);
			}
            throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}
	
	/**
	 * Inserta nueva empresa
	 * 
	 * @param  dto
	 * @return long
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public long setCreaEmpresa(EmpresasDTO dto) throws EmpresasException, SystemException{
		long id= 0;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			//verifica si existe el rut de empresa ingresado, si existe, envia mensaje
			EmpresasEntity ent = dao.getEmpresaByRut(dto.getEmp_rut());
			if(ent!=null)
				throw new EmpresasException("RUT:"+dto.getEmp_rut()+Constantes.MSJE_EMPR_RUT_EXISTE);
				
			id = dao.setCreaEmpresa(dto);
			logger.debug("En setCreaEmpresa(), id:"+id);
			if(id <= 0)
				throw new EmpresasException(Constantes._EX_EMP_ID_NO_CREADO);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return id;
	}
	
	/**
	 * Modifica informacion de la empresa
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public boolean modEmpresa(EmpresasDTO dto) throws EmpresasException, SystemException{
		boolean result = false;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			//revisar si modifico el rut, si el rut modificado existe , enviar mensaje
			EmpresasEntity ent = dao.getEmpresaById(dto.getEmp_id());
			if(ent.getRut().longValue() != dto.getEmp_rut()){
				EmpresasEntity entR = dao.getEmpresaByRut(dto.getEmp_rut());
				if(entR!=null){
					throw new EmpresasException("Rut:"+dto.getEmp_rut()+Constantes.MSJE_EMPR_RUT_MOD_EXISTE);
				}
			}
			
			//revisar si tiene diferente razon social, si es asi, setear flag de modificacion a 1
			if(!ent.getRsocial().equals(dto.getEmp_rzsocial()))
				dto.setEmp_mod_rzsoc(Constantes.EMP_MOD_RAZON_SOCIAL);
			else
				dto.setEmp_mod_rzsoc(Constantes.EMP_NO_MOD_RAZON_SOCIAL);
			
			result = dao.modEmpresa(dto);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Solo realiza un update al campo emp_estado = E
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public boolean delEmpresa(EmpresasDTO dto) throws EmpresasException, SystemException{
		boolean result = false;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			result = dao.delEmpresa(dto);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene datos de la empresa, segun Rut
	 * 
	 * @param  rut
	 * @return EmpresasDTO 
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public EmpresasDTO getEmpresaByRut(long rut) throws EmpresasException, SystemException{
		EmpresasDTO dto = new EmpresasDTO();
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			EmpresasEntity emp = dao.getEmpresaByRut(rut);
			if(emp!=null){
				dto.setEmp_id(emp.getId().longValue());
			}
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dto;
	}
	
	/**
	 * Obtiene el listado de empresas, segun los criterios ingresados
	 * 
	 * @param  criterio
	 * @return List EmpresasDTO
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public List getEmpresasByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasException, SystemException{
		List result = new ArrayList();
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		List lst_emp;
		
		try {
			if (criterio==null){
				lst_emp= dao.getEmpresasByCriteria();
		    }
			else{
				lst_emp = dao.getEmpresasByCriteria(criterio);	
			}
			
			EmpresasDTO dto = null;
			for(int i=0; i<lst_emp.size(); i++){
				EmpresasEntity ent = (EmpresasEntity)lst_emp.get(i);
				dto = new EmpresasDTO();
				dto.setEmp_id(ent.getId().longValue());
				dto.setEmp_rut(ent.getRut().longValue());
				dto.setEmp_dv(ent.getDv().toString());
				dto.setEmp_nom(ent.getNombre());
				dto.setEmp_descr(ent.getDescripcion());
				dto.setEmp_rzsocial(ent.getRsocial());
				dto.setEmp_rubro(ent.getRubro());
				dto.setEmp_tamano(ent.getTamano());
				dto.setEmp_qtyemp(ent.getQtyemp().intValue());
				dto.setEmp_nom_contacto(ent.getNom_contacto());
				dto.setEmp_fono1_contacto(ent.getFono1_contacto());
				dto.setEmp_fono2_contacto(ent.getFono2_contacto());
				dto.setEmp_fono3_contacto(ent.getFono3_contacto());
				dto.setEmp_mail_contacto(ent.getMail_contacto());
				dto.setEmp_cargo_contacto(ent.getCargo_contacto());
				dto.setEmp_saldo(ent.getSaldo().doubleValue());
				dto.setEmp_fact_saldo(Formatos.frmFechaHora(ent.getFact_saldo()));
				dto.setEmp_fmod(Formatos.frmFechaHora(ent.getFmod()));
				dto.setEmp_estado(ent.getEstado());
				dto.setEmp_mrg_minimo(ent.getMrg_minimo().doubleValue());
				dto.setEmp_fec_crea(Formatos.frmFechaHora(ent.getFec_crea()));
				dto.setEmp_mod_rzsoc(ent.getMod_rzsoc().intValue());
				dto.setEmp_dscto_max(ent.getDscto_max().doubleValue());
				
				result.add(dto);
			}
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Obtiene la cantidad de empresas que coinciden con los criterios de busqueda ingresados.
	 * 
	 * @param  criterio
	 * @return int
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public int getEmpresasCountByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasException, SystemException{
		int result = 0;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		try {
			result = dao.getEmpresasCountByCriteria(criterio);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}


	/**
	 * Agrega registro de mail para ser enviado al ejecutivo
	 * 
	 * @param  mail
	 * @return boolean
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public boolean insMail(MailDTO mail) throws EmpresasException, SystemException{
		boolean result = false;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			result = dao.insMail(mail);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de estados segun el tipo
	 * 
	 * @param tipo
	 * @return List EstadoDTO
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public List getEstadosByTipo(String tipo) throws EmpresasException, SystemException{
		List result = new ArrayList();
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO();
		
		try{
			result = dao.getEstadosByTipo(tipo);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Inserta datos de solicitud de registro
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public long insSolReg(SolRegDTO dto) throws EmpresasException, SystemException{
		long id = 0;
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			id = dao.insSolReg(dto);
			
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return id;
	}	
	
	
	/**
	 * Inserta  mail a ejecutivo y datos para solicitud de registro
	 * @param solreg : DTO con los datos del formulario  solicitud  de registro 
	 * @return Id: Numero de solicitud de registro
	 * @throws ServiceException, SystemException
	 */
	
	public long setMailSolRegistro(SolRegDTO solreg)	throws EmpresasException, SystemException{
		long rel1 = 0;
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
		
		try {
			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);
			
		} catch (EmpresasDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		
		try{
			
			boolean flag_rollback = false;
			
			//Se inserta la solicitus de registro
			rel1 = dao.insSolReg(solreg);
			logger.debug("rel1: "+rel1);
			if(rel1 == 0)
				flag_rollback = true;
			
			if( flag_rollback == true )
				throw new EmpresasDAOException( "Error al insertar." );			
			
		   //Finaliza la transacción
		   trx.end();			
			
		}catch(EmpresasDAOException ex){
			logger.warn("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException("Error no controlado",ex);
		}
		catch (DAOException e) {
		    try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		    e.printStackTrace();
		    throw new SystemException(e);
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
		}		
		return rel1;
	}
	
	
	/**
	 * Obtiene el listado de estados segun el tipo
	 * 
	 * @param id_empresa
	 * @return double saldo_pendiente
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public double getSaldoActualPendiente(long id_empresa) throws EmpresasException, SystemException{
		
		JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO();
		
		try{
			return dao.getSaldoActualPendiente(id_empresa);
		}catch(EmpresasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new EmpresasException(ex);
		} catch (Exception e) {
			throw new SystemException(e);
		}		
	}

    /**
     * @param prm
     * @return
     */
    public boolean modEmpresaLinea(EmpresasDTO prm) throws EmpresasException, SystemException{
        JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
        try {
            return dao.modEmpresaLinea(prm);            
        } catch(EmpresasDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new EmpresasException(ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param log
     */
    public void setEmpresaLineaLog(EmpresaLogDTO log) throws EmpresasException, SystemException{
        JdbcEmpresasDAO dao = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO(); 
        try {
            dao.setEmpresaLineaLog(log);            
        } catch(EmpresasDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new EmpresasException(ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
	
}
