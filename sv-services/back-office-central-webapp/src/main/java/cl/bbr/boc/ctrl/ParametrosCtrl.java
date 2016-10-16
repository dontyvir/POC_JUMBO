/*
 * Creado el 12-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.boc.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcUmbral;
import cl.bbr.boc.dto.UmbralDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.jumbocl.common.model.ParametroEntity;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author RMI-DNT
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ParametrosCtrl {
	
	Logging logger = new Logging(this);
	

	/**
	 * Obtiene el Listado de parametros, segun criterios ingresados
	 * 
	 * @param  criterio
	 * @return List UmbralDTO
	 * @throws ElementoException
	 * @throws DAOException
	 */
	public List getParametrosByCriteria(ElementosCriteriaDTO criterio) throws ElementoException, DAOException{
		List result = new ArrayList();
		try{
			logger.debug("en getParametrosByCriteria");
			JdbcUmbral umbralDAO = (JdbcUmbral) DAOFactoryParametros
			.getDAOFactory(DAOFactoryParametros.JDBC).getUmbralDAO();
			
			List lst_campana = (List) umbralDAO.getParametrosByCriteria(criterio);
			ParametroEntity ent = null;
			for (int i = 0; i < lst_campana.size(); i++) {
				ent = (ParametroEntity) lst_campana.get(i);
				UmbralDTO elemdto = new UmbralDTO();
				elemdto = new UmbralDTO();
				elemdto.setId_local(ent.getId_local());
				elemdto.setNom_local(ent.getNom_local());
				elemdto.setU_unidad(ent.getU_unidad());
				elemdto.setU_monto(ent.getU_monto());
				elemdto.setU_activacion(ent.getU_activacion());
				if(ent.getFecha_modi()!=null){
					elemdto.setFecha_modi(ent.getFecha_modi().toString());
				}else{
					elemdto.setFecha_modi("");
				}
				result.add(elemdto);
			}
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result; 
	}
	
	public UmbralDTO getPorcenUmbralById(long id_pedido) throws ElementoException, DAOException, BocException{
		
		UmbralDTO umr2 = new UmbralDTO();
		double  porc_u = 0; 
		double  porc_monto = 0;
		
		try{
			logger.debug("en getPorcenUmralById");
			JdbcUmbral umbralDAO = (JdbcUmbral) DAOFactoryParametros
			.getDAOFactory(DAOFactoryParametros.JDBC).getUmbralDAO();
			UmbralDTO umr = umbralDAO.getPorcenUmbralById(id_pedido);
			
			porc_u = umr.getU_unidad();
			porc_monto = umr.getU_monto();
			umr2.setU_monto(porc_monto);
			umr2.setU_unidad(porc_u);
			
			}
			
		catch(BocException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		
		return  umr2;
	}

	
	
	/**
	 * Inserta Umbrales
	 * 
	 * @param  id_local
	 * @param  fecha_modi
	 * @param  nom_usuario
	 * @param  u_unidad
	 * @param  u_monto
	 * @param  id_localvacion
	 * @param  u_acti
	 * @throws DAOException
	 */


		   public void insertarUmbrales(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws Exception {
		   	JdbcUmbral umbralDAO = (JdbcUmbral) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getUmbralDAO();
	        try {
	            umbralDAO.insertarUmbrales(id_local,fecha_modi,nom_usuario,u_unidad,u_monto,u_activacion);
	        } catch (Exception ex) {
	            logger.debug("Problema :" + ex.getMessage());
	            throw new DAOException(ex);
	        }
	    }
		   
		   public void updateUmbral(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws Exception {
		   	JdbcUmbral umbralDAO = (JdbcUmbral) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getUmbralDAO();
	        try {
	            umbralDAO.updateUmbral(id_local,fecha_modi,nom_usuario,u_unidad,u_monto,u_activacion);
	        } catch (Exception ex) {
	            logger.debug("Problema :" + ex.getMessage());
	            throw new DAOException(ex);
}
	    }

		   public long consultaIdLocal(long id_local) throws Exception {
		   	JdbcUmbral umbralDAO = (JdbcUmbral) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getUmbralDAO();
		   	long id_local_ret = 0;
	        try {
	            id_local_ret = umbralDAO.consultaIdLocal(id_local);
	            
	         	            } catch (Exception ex) {
	            logger.debug("Problema :" + ex.getMessage());
	            throw new DAOException(ex);
	        }
	        return id_local_ret;
	    }
		   
		   
}

