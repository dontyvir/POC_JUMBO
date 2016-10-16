package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CategoriaEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.ProductoCategEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcCategoriasDAO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;


/**
 * Entrega metodos de navegacion por categorias y busqueda en base a criterios. 
 * Los resultados son listados de categorias o datos de categorias web.
 * 
 * @author BBR Ingenieria
 *
 */
public class CategoriasCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	/**
	 * Retorna los datos escenciales de una categoria en base a su id
	 * 
	 * @param  id_categoria
	 * @return CategoriaDTO
	 * @throws CategoriasException
	 */
	public CategoriasDTO getCategoriaById(long id_categoria) throws CategoriasException{
		CategoriasDTO row1 = new CategoriasDTO();
		try{
			logger.debug("en getCategoriaById");
			
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			CategoriaEntity cat = categoriasDAO.getCategoriaById(id_categoria);
			logger.debug("est: *"+cat.getEstado().toString()+"*");
			String fec_crea = "";
			if(cat.getFec_crea()!=null)
				fec_crea = cat.getFec_crea().toString();
			String fec_mod = "";
			if(cat.getFec_mod()!=null)
				fec_mod = cat.getFec_mod().toString();
			int user_mod = -1;
			if(cat.getUser_mod()!=null)
				user_mod = cat.getUser_mod().intValue();
			row1 = new CategoriasDTO(cat.getId().longValue(), 0,	//cat.getId_padre().longValue(), 
					cat.getNombre(), cat.getDescripcion(), cat.getEstado(), cat.getTipo(), cat.getOrden().intValue(),
					cat.getPorc_ranking().doubleValue(), cat.getBanner(), cat.getNom_cat_padre(),
					fec_crea, fec_mod, user_mod);
			row1.setTotem(cat.getTotem());
            row1.setImagen(cat.getImagen());
            row1.setUrl_banner((cat.getUrl_imagen() != null && !"".equals(cat.getUrl_imagen()))?cat.getUrl_imagen():"");
            
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getCategoriaById:"+ex);
			throw new CategoriasException(ex);
		}
		return row1;
	}
	
	/**
	 * Obtiene el listado de categorias, segun los criterios de búsqueda
	 * 
	 * @param  criterio CategoriasCriteriaDTO 
	 * @return List CategoriasDTO
	 * @throws CategoriasException
	 */
	public List getCategoriasByCriteria(CategoriasCriteriaDTO criterio) throws CategoriasException {
		List result= new ArrayList();
		try{
			logger.debug("en getCategoriasByCriteria");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			List listaCat = new ArrayList();
			listaCat = (List) categoriasDAO.listadoCategoriasByCriteria(criterio);
			CategoriaEntity cat = null;
			for (int i = 0; i < listaCat.size(); i++) {
				cat = null;
				cat = (CategoriaEntity) listaCat.get(i);
				CategoriasDTO catdto = new CategoriasDTO(cat.getId().longValue(), cat.getId_padre().longValue(), 
						cat.getNombre(), cat.getDescripcion(), cat.getEstado(), 
						cat.getTipo(), cat.getOrden().intValue(),
						cat.getPorc_ranking().doubleValue(), cat.getBanner(), cat.getNom_cat_padre());

				result.add(catdto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getCategoriasByCriteria:"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene la cantidad de categorias, segun los criterios de búsqueda
	 * 
	 * @param  criterio CategoriasCriteriaDTO 
	 * @return int, cantidad
	 * @throws CategoriasException
	 */
	public int getCategoriasCountByCriteria(CategoriasCriteriaDTO criterio) throws CategoriasException {
		int result =0;
		try{
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
				
			result = categoriasDAO.countCategoriasByCriteria(criterio); 
				
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getCategoriasCountByCriteria:"+ex);
			throw new CategoriasException(ex);
		}
		return result;
	}
		
	/**
	 * Obtiene el listado de categorias por navegación, segun los criterios de búsqueda y la categoría padre
	 * 
	 * @param  criterio CategoriasCriteriaDTO 
	 * @param  cat_padre long 
	 * @return List CategoriasDTO
	 * @throws CategoriasException
	 */
	public List getCategoriasNavegacion(CategoriasCriteriaDTO criterio, long cat_padre)throws CategoriasException{
		List result= new ArrayList();
		try{
			logger.debug("en getCategoriasNavegacion");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			List listaCat = new ArrayList();
			listaCat = (List) categoriasDAO.listadoCategoriasNavegacion(criterio,cat_padre);
			CategoriaEntity cat = null;
			for (int i = 0; i < listaCat.size(); i++) {
				cat = null;
				cat = (CategoriaEntity) listaCat.get(i);
				CategoriasDTO catdto = new CategoriasDTO(cat.getId().longValue(), cat.getId_padre().longValue(), 
						cat.getNombre(), cat.getDescripcion(), cat.getEstado(), 
						cat.getTipo(), cat.getOrden().intValue(),
						cat.getPorc_ranking().doubleValue(), cat.getBanner(), cat.getNom_cat_padre());

				result.add(catdto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getCategoriasNavegacion:"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de productos que pertenecen a una categoria
	 * 
	 * @param  codCat long 
	 * @return List ProductoCategDTO
	 * @throws CategoriasException
	 */
	public  List getProductosByCategId(long codCat)throws CategoriasException{
		List result= new ArrayList();
		try{
			logger.debug("en getProductosByCategId");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			List listaProd = new ArrayList();
			listaProd = (List) categoriasDAO.getProductosByCategId(codCat);
			ProductoCategEntity prd = null;
			for (int i = 0; i < listaProd.size(); i++) {
				prd = null;
				prd = (ProductoCategEntity) listaProd.get(i); 
				ProductoCategDTO estDto = new ProductoCategDTO();
				estDto.setId_prod(prd.getId_prod().longValue());
				
				//este es el id de la relacion producto categoria no va
				estDto.setId(prd.getId().longValue());
				estDto.setNom_prod(prd.getNom_prod());
				estDto.setOrden(prd.getOrden().intValue());
				estDto.setTipo_prod(prd.getTipo_prod());
				estDto.setNom_marca(prd.getNom_marca());	
				estDto.setCon_pago(prd.getCon_pago());

				result.add(estDto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene el listado de tipos de categoría
	 * 
	 * @return List EstadoDTO
	 * @throws CategoriasException
	 */
	public  List getTiposCategorias()throws CategoriasException{
		List result= new ArrayList();
		try{
			logger.debug("en getTiposCategorias");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) categoriasDAO.getEstados("CAT");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getTiposCategorias:"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene el listado de estados de categoría
	 * 
	 * @return List EstadoDTO
	 * @throws CategoriasException
	 */
	public  List getEstadosCategorias()throws CategoriasException{
		List result= new ArrayList();
		try{
			logger.debug("en getEstadosCategorias");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) categoriasDAO.getEstados("ALL");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getEstadosCategorias:"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Agrega una nueva categoría
	 * 
	 * @param  procparam ProcAddCatWebDTO 
	 * @return long, nuevo id
	 * @throws CategoriasException
	 */
	public long setAddCatWeb(ProcAddCatWebDTO procparam) throws CategoriasException{
		try{
			logger.debug("en setAddCatWeb");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			// HibernateCategoriasDAO categoriasDAO =
			// (HibernateCategoriasDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCategoriasDAO();
			
			boolean ok = categoriasDAO.creaCategoria(procparam);
			logger.debug("ok:"+ok);
			
			
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CategoriasException(ex);
		}
	    return 0L;
	}
    
	/**
	 * Elimina una categoría
	 * 
	 * @param  proc ProcDelCatWebDTO 
	 * @return boolean
	 * @throws CategoriasException
	 * @throws SystemException
	 */
	public boolean setDelCatWeb(ProcDelCatWebDTO proc)throws CategoriasException, SystemException{
		boolean res = false;
		try{
			logger.debug("en setDelCatWeb");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			CategoriaEntity catent = categoriasDAO.getCategoriaById(proc.getId_categoria());
			if(catent.getTipo().equals("T")){
				//no debe tener productos
				List lst_prod = categoriasDAO.getProductosByCategId(proc.getId_categoria());
				//puede contener productos con estado Inactivo, entonces, ¿la categoria puede ser eliminada?
				//No, debido a que siguen catalogando.
				if(lst_prod.size()>0){
					throw new CategoriasException(Constantes._EX_CAT_PROD_REL_EXISTE);
					//eliminar=true;
				} //else	paramUrl = paramUrl+ "&mns=La categoria tiene productos relacionados.\nNo puede ser eliminada";
			} else if(catent.getTipo().equals("I")){
				//no debe tener subcategorias
				List lst_sub = categoriasDAO.getCategoriasByCategId(proc.getId_categoria());
				if(lst_sub.size()>0) {
					throw new CategoriasException(Constantes._EX_CAT_SUBCAT_REL_EXISTE);
					//eliminar = true;
				} //else paramUrl = paramUrl+ "&mns=La categoria tiene subcategorias relacionadas.\nNo puede ser eliminada";
			}
			
				//luego eliminar la categoria
				res = categoriasDAO.eliminaCategoria(proc.getId_categoria());
				logger.debug("res:"+res);
			
			
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new CategoriasException( Constantes._EX_CAT_ID_NO_EXISTE, ex );
			}
			if ( ex.getMessage().equals( DbSQLCode.SQL_EXIST_DEP_TBLS ) ){ 
				throw new CategoriasException( Constantes._EX_CAT_SUBCAT_REL_EXISTE, ex );
			}
			throw new SystemException("Error no controlado",ex);
		}
		logger.debug("res:"+res);
		
	    return res;
	}
	
	/**
	 * Actualizar información de categoría
	 * 
	 * @param  procparam ProcModCatWebDTO 
	 * @throws CategoriasException
	 */
	public void setModCatWeb(ProcModCatWebDTO procparam)throws CategoriasException{
		try{
			logger.debug("en setModCatWeb");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			//verifica si existe la categoria
			if(!categoriasDAO.isCategoriaById(procparam.getId_categoria()))
				throw new CategoriasException(Constantes._EX_CAT_ID_NO_EXISTE); 
			boolean ok = categoriasDAO.modificaCategoria(procparam);
			logger.debug("ok:"+ok);
			
			
		}catch(CategoriasDAOException ex){
			logger.debug("Problema setModCatWeb:"+ex);
			throw new CategoriasException(ex);
		}
	    return;
	}
	
	/**
	 * Obtener la lista de categorias asociadas a una categoria
	 * 
	 * @param  codCat long 
	 * @return List CategoriasDTO
	 * @throws CategoriasException
	 */
	public List getCategoriasByCategId(long codCat) throws CategoriasException{
		List result= new ArrayList();
		try{
			logger.debug("en getCategoriasByCategId");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			List lstCat = new ArrayList();
			lstCat = (List) categoriasDAO.getCategoriasByCategId(codCat);
			CategoriaEntity cat = null;
			for (int i = 0; i < lstCat.size(); i++) {
				cat = null;
				cat = (CategoriaEntity) lstCat.get(i); 
				CategoriasDTO catDto = new CategoriasDTO(cat.getId().longValue(), cat.getNombre(), 
						cat.getOrden().intValue());

				result.add(catDto);
			}
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Asignar una categoría a una sub-categoría.<br>
	 * Nota: Este método tiene transaccionalidad.
	 * 
	 * @param  prm ProcModSubCatWebDTO
	 * @return boolean, devuelve <i>true</i> si la asignación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws CategoriasException
	 * @throws SystemException
	 */
	public boolean setModSubCatWeb(ProcModSubCatWebDTO prm)throws CategoriasException, SystemException{
		boolean res = false;
		logger.debug("en setModSubCatWeb");
		JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
		
        CategoriaEntity ca1 = null;
        CategoriaEntity ca2 = null;
        try {
            ca1 = categoriasDAO.getCategoriaById(prm.getId_cat());
            ca2 = categoriasDAO.getCategoriaById(prm.getId_subcategoria());
        } catch (CategoriasDAOException c1) {
            logger.error("Error al procesar categorias");
            throw new SystemException("Error al procesar categorias - setModSubCatWeb");
        }    
        
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		// Marcamos los dao's con la transacción
		try {
			categoriasDAO.setTrx(trx1);
		} catch (CategoriasDAOException e2) {
			logger.error("Error al asignar transacción al dao Categorias");
			throw new SystemException("Error al asignar transacción al dao Categorias");
		}
		
		try{			
			logger.debug("action:"+prm.getAction());
			//verifica que cat_id y subcat_id sean diferentes
			if(prm.getId_cat()==prm.getId_subcategoria()){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_CAT_SUBCAT_IGUALES);
			}
			//verificar q exista cat_id y subcat_id
			if(!categoriasDAO.isCategoriaById(prm.getId_cat())){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_CAT_ID_NO_EXISTE);
			}
			if(!categoriasDAO.isCategoriaById(prm.getId_subcategoria())){
				trx1.rollback();
				throw new CategoriasException(Constantes._EX_CAT_SUBCAT_ID_NO_EXISTE);
			}
            
            //Verificar correcta asociación
            if ((ca1.getTipo().equals("C")) && (!ca2.getTipo().equals("I"))) {
                trx1.rollback();
                throw new CategoriasException(Constantes._EX_CAT_SUBCAT_ASIG_INCORRECTA);
            }
            if ((ca1.getTipo().equals("I")) && (!ca2.getTipo().equals("T"))) {
                trx1.rollback();
                throw new CategoriasException(Constantes._EX_CAT_SUBCAT_ASIG_INCORRECTA);
            }
			if(prm.getAction().equals("agregar")){
				//verifica si ya existe la relacion entre cat y sub_cat
				if(categoriasDAO.isCategRelSubCateg(prm.getId_cat(),prm.getId_subcategoria())){
					trx1.rollback();
					throw new CategoriasException(Constantes._EX_CAT_SUBCAT_REL_EXISTE);
				}
				//asociar subcategoria a categoria padre, agregar en fo_catsubcat
				boolean agregar = categoriasDAO.addSubCategory(prm);
				logger.debug("agregar"+agregar);
				res = true;
			} else if(prm.getAction().equals("eliminar")){
				//desasociar subcategoria de categoria padre, eliminar en fo_catsubcat y actualizar orden a 0
				boolean eliminar = categoriasDAO.delSubCategory(prm);
				logger.debug("eliminar"+eliminar);
				if(eliminar){
					//actualizar orden a 0 en fo_categorias
					CategoriaEntity cat = categoriasDAO.getCategoriaById(prm.getId_subcategoria());
					ProcModCatWebDTO proc = new ProcModCatWebDTO();
					proc.setId_categoria(cat.getId().longValue());
					proc.setNombre(cat.getNombre());
					proc.setDescripcion(cat.getDescripcion());
					proc.setEstado(cat.getEstado());
					proc.setTipo(cat.getTipo());
					proc.setOrden(0);
					proc.setFec_modif(Formatos.getFecHoraActual());
					proc.setUsu_modif(prm.getId_usr());
					if(categoriasDAO.modificaCategoria(proc))
						res = true;
				}
			}
			
			
		}catch(CategoriasDAOException ex){			
			logger.debug("Problema setModCatWeb:"+ex);
//			 rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			throw new CategoriasException(ex);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
		}
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
	    return res;
	}

	/**
	 * Asignar una categoría a una sub-categoría.<br>
	 * Nota: Este método tiene transaccionalidad.
	 * 
	 * @param  id long
	 * @return boolean, devuelve <i>true</i> si la asignación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws CategoriasException
	 * @throws SystemException
	 */
	public boolean isCategoriaById(long id) throws CategoriasException{
		boolean result = false;
		try{
			logger.debug("en isCategoriaById");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			result = categoriasDAO.isCategoriaById(id);
			
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}
	
	/**
	 * Verifica si una subcategoria esta relacionada a una categoria.<br>
	 * 
	 * @param  id_cat long 
	 * @param  id_subcat long 
	 * @return boolean, devuelve <i>true</i> si existe relación, caso contrario devuelve <i>false</i>.
	 * @throws CategoriasException
	 */
	public boolean isCategRelSubCateg(long id_cat, long id_subcat) throws CategoriasException{
		boolean result = false;
		try{
			logger.debug("en isCategRelSubCateg");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			result = categoriasDAO.isCategRelSubCateg(id_cat, id_subcat);
			
		}catch(CategoriasDAOException ex){
			logger.debug("Problema :"+ex);
			throw new CategoriasException(ex);
		}
		
		return result;
	}

    /**
     * @param idCategoria
     * @return
     */
    public String getNombresCategoriasPadreByIdCat(long idCategoria) throws CategoriasException {
		try{
			logger.debug("en getNombresCategoriasPadreByIdCat");
			JdbcCategoriasDAO categoriasDAO = (JdbcCategoriasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCategoriasDAO();
			
			return categoriasDAO.getNombresCategoriasPadreByIdCat(idCategoria);
		}catch(CategoriasDAOException ex){
			logger.debug("Problema getNombresCategoriasPadreByIdCat:"+ex);
			throw new CategoriasException(ex);
		}		
    }
	
}
