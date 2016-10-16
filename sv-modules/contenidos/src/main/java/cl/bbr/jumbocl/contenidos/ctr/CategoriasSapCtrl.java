package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcCategoriasSapDAO;
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Controlador de procesos sobre las categorías SAP.
 * @author BBR Ingeniería
 *
 */
public class CategoriasSapCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Se obtiene la lista de categorias sap, segun la categoria seleccionada
	 * 
	 * @param  cod_cat String 
	 * @param  cod_cat_padre String 
	 * @return List CategoriaSapDTO
	 * @throws CategoriasSapException
	 * */
	public List getCategoriasSapById(String cod_cat, String cod_cat_padre) throws CategoriasSapException{
		List result = new ArrayList();
		try{
			logger.debug("en getCategoriasSapById");
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			List listaCat= new ArrayList();
			if(!cod_cat.equals("-1"))
				listaCat = (List) categoriasSapDAO.getCategoriasSapById(cod_cat);
			else if(cod_cat.equals("-1")){
				listaCat = (List) categoriasSapDAO.getCategoriasSapById(cod_cat_padre);
			}
			//opcion 2
			if (listaCat.size()==0){
				//obtiene el list de cod_cat_padre
				listaCat = (List) categoriasSapDAO.getCategoriasSapById(cod_cat_padre);
			}
				
			CategoriaSapEntity cat = null;
			for (int i = 0; i < listaCat.size(); i++) {
				cat = null;
				cat = (CategoriaSapEntity) listaCat.get(i);
				CategoriaSapDTO catdto = new CategoriaSapDTO();
				catdto.setId_cat(cat.getId_cat());
				catdto.setDescrip(cat.getDescrip());
				catdto.setNivel(cat.getNivel().intValue());
				catdto.setTipo(cat.getTipo());
				catdto.setId_cat_padre(cat.getId_cat_padre());
				
				
				/*CategoriaSapDTO catdto = new CategoriaSapDTO(cat.getId_cat(), cat.getDescrip(),cat.getId_cat_padre());*/

				result.add(catdto);
			}
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCategoriasSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}
	
	/**
	 * Obtiene el código de la categoría padre
	 * 
	 * @param  cod_cat String 
	 * @return String, cod_cat_padre
	 * @throws CategoriasSapException
	 * */
	public String getCodCatPadre(String cod_cat) throws CategoriasSapException{
			String result = "";
			try{
				logger.debug("en getCodCatPadre");
				JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
				
					result = categoriasSapDAO.getCodCatPadre(cod_cat);
			}catch(CategoriasSapDAOException ex){
				logger.debug("Problema getCategoriasSapById:"+ex);
				throw new CategoriasSapException(ex);
			}
			return result;
		
	}
	
	/**
	 * Se obtiene el nombre de la categoria sap, segun la categoria seleccionada
	 * 
	 * @param  cod_cat String 
	 * @return String
	 * @throws CategoriasSapException
	 * */
	public String getCatSapById(String cod_cat) throws CategoriasSapException{
		String result = "";
		try{
			logger.debug("en getCatSapById");
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
				result = categoriasSapDAO.getCatSapById(cod_cat);
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCatSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}	
	
	/**
	 * Se obtiene la lista de todas las secciones sap
	 * 
	 * @return List CategoriaSapDTO
	 * @throws CategoriasSapException
	 * 
	 * */
	public List getSeccionesSap() throws CategoriasSapException{
		List result = new ArrayList();
		try{
			logger.debug("en getCategoriasSapById");
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			List listaCat= new ArrayList();
				listaCat = (List) categoriasSapDAO.getSeccionesSap();
				
			CategoriaSapEntity cat = null;
			for (int i = 0; i < listaCat.size(); i++) {
				cat = null;
				cat = (CategoriaSapEntity) listaCat.get(i);
				CategoriaSapDTO catdto = new CategoriaSapDTO();
				catdto.setId_cat(cat.getId_cat());
				catdto.setDescrip(cat.getDescrip());				
			result.add(catdto);
			}
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCategoriasSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la categoria sap, segun el codigo
	 * @param  id_cat
	 * @return CategoriaSapDTO 
	 * @throws CategoriasSapException
	 */
	public CategoriaSapDTO getCategoriaSapById(String id_cat) throws CategoriasSapException{
		CategoriaSapDTO result = new CategoriaSapDTO();
		
		try{
			logger.debug("en getCategoriaSapById");
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			CategoriaSapEntity cat = categoriasSapDAO.getCategoriaSapById(id_cat);
			
			if (result==null){
				throw new CategoriasSapException(Constantes._EX_CAT_ID_NO_EXISTE);
			}
			//setear informacion en result
			result.setId_cat(cat.getId_cat());
			result.setId_cat_padre(cat.getId_cat_padre());
			result.setDescrip(cat.getDescrip());
			result.setNivel(cat.getNivel().intValue());
			result.setTipo(cat.getTipo());
			result.setEstado(cat.getEstado());
			
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCategoriaSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}
	

	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws CategoriasSapException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */	
	public LinkedHashMap getCategoriasInGRB(int inicio, int fin, String idCat) throws CategoriasSapException{
		LinkedHashMap result =  new LinkedHashMap();
		
		try{
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			LinkedHashMap listaCat = categoriasSapDAO.getCategoriasInGRB(inicio, fin, idCat);
			
			Set set = listaCat.entrySet();
		    Iterator i = set.iterator();
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      CategoriaSapEntity catEntity = (CategoriaSapEntity) me.getValue();
		     
		      CategoriaSapDTO catdto = new CategoriaSapDTO();
		      catdto.setId_cat(catEntity.getId_cat());
			  catdto.setDescrip(catEntity.getDescrip());
			  result.put(me.getKey(), catdto);
		    }
				
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCategoriasSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}
	
	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws CategoriasSapException 
	 * @see    cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO
	 */	
	public LinkedHashMap getCategoriasNoGRB(String idCat) throws CategoriasSapException{
		LinkedHashMap result =  new LinkedHashMap();
		
		try{
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			LinkedHashMap listaCat =  categoriasSapDAO.getCategoriasNoGRB(idCat);
	
			Set set = listaCat.entrySet();
		    Iterator i = set.iterator();
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      CategoriaSapEntity catEntity = (CategoriaSapEntity) me.getValue();
		      
		      CategoriaSapDTO catdto = new CategoriaSapDTO();
			  catdto.setId_cat(catEntity.getId_cat());
			  catdto.setDescrip(catEntity.getDescrip());

			  result.put(me.getKey(), catdto);
			}
		}catch(CategoriasSapDAOException ex){
			logger.debug("Problema getCategoriasSapById:"+ex);
			throw new CategoriasSapException(ex);
		}
		return result;
	}

	public int deleteCategoriaById(String seccion, int segmento) throws CategoriasSapException{
		try{
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			return categoriasSapDAO.deleteCategoriaById(seccion, segmento);	
			
		}catch(CategoriasSapDAOException ex){
			throw new CategoriasSapException(ex);
		}
	}

	public int addCategoriaById(String seccion, int segmento) throws CategoriasSapException{
		try{
			JdbcCategoriasSapDAO categoriasSapDAO = (JdbcCategoriasSapDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getCategoriasSapDAO();
			
			return categoriasSapDAO.addCategoriaById(seccion, segmento);	
			
		}catch(CategoriasSapDAOException ex){
			throw new CategoriasSapException(ex);
		}
	}
	
	
}
