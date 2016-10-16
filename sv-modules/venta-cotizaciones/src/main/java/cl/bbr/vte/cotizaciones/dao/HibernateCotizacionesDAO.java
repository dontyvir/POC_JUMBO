package cl.bbr.vte.cotizaciones.dao;

import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.cotizaciones.exception.CotizacionesDAOException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.shared.log.Logging;
import java.util.List;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class HibernateCotizacionesDAO implements CotizacionesDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);

	/**
	 * Retorna las categorias activas
	 * @param cliente_id
	 * @return
	 * @throws CotizacionesDAOException
	 */

	public List getListCategoria( long cliente_id ) throws CotizacionesDAOException{
		return null;
	}

	/**
	 * Retorna una lista con las cotizaciones de acuerdo a un criterio de búsqueda.
	 */
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio) throws CotizacionesDAOException {
		return null;
	}

	/**
	 * Retorna una lista con Locales
	 */
	public List getLocales() throws CotizacionesDAOException {
		return null;
	}


	/**
	 * Retorna una lista con los estados de las cotizaciones
	 */
	public List getEstadosCotizacion() throws CotizacionesDAOException {
		return null;
	}

	/**
	 * Retorna el número de registros de cotizaciones de acuerdo a un criterio de búsqueda.
	 */
	public long getCountCotizacionesByCriteria(CotizacionesCriteriaDTO criterio) throws CotizacionesDAOException {
		return 0;
	}
	/**
	 * Retorna el Detalle de una cotización a partir de su Id.
	 */
	public CotizacionesDTO getCotizacionById(long cot_id) throws CotizacionesDAOException {
		return null;
	}

	/**
	 * Retorna la cantidad de productos asociados a una cotización segun id de cotización
	 */
	public long getCountProductosEnCotizacionById(long cot_id) throws CotizacionesDAOException {
		return 0;
	}

	/**
	 * Retorna una lista con las alertas para cotizaciones.
	 */
	public List getAlertasCotizacion(long id_cot) throws CotizacionesDAOException {
		return null;
	}

	/**
	 * Retorna una lista con los productos asociados a una cotización
	 */
	public List getProductosCotiz(long id_cot) throws CotizacionesDAOException {
		return null;
	}

	
	/**
	 * Retorn una lista con los pedidos asociados a una cotización
	 */
	public List getPedidosCotiz(long id_cot) throws CotizacionesDAOException {
		return null;
	}
	
	public long doInsCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesDAOException {
		return -1;
	}
	
	public boolean doInsDetCotizacion(ProcInsDetCotizacionDTO dto) throws CotizacionesDAOException {
		return false;
	}

	/**
	 * Retorna una lista con los logs asociados a una cotización.
	 */
	public List getLogCotiz(long id_cot) throws CotizacionesDAOException {
		return null;
	}

	public List getLstProductosByCotizacion(long id_cotizacion) throws CotizacionesDAOException {
		return null;
	}
	
	/**
	 * Modifica costo de despacho, observaciones y prod. fuera de mix de una cotización.
	 */
	public boolean doUpdCotizacion(ModCotizacionDTO cot) throws CotizacionesDAOException {
		return false;
	}

	/**
	 * Agrega un registro al log de cotizaciones
	 */
	public boolean addLogCotizacion(LogsCotizacionesDTO log) throws CotizacionesDAOException {
		return false;
	}
  
	/**
	 * Permite modificar el estado de una cotización
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado) throws CotizacionesDAOException {
		return false;
	}

	/**
	 * Permite agregar un producto a una cotización
	 */
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod) throws CotizacionesDAOException {
		return false;
	}

	/**
	 * Permite eliminar un producto de una cotización
	 */
	public boolean delProductoCotizacion(long detcot_id) throws CotizacionesDAOException {
		return false;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#CaducarCotizaciones()
	 */
	public boolean CaducarCotizaciones( long id_comprador ) throws CotizacionesDAOException {
		return false;
	}

	/*
	 *  (sin-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updProductoCotizacion(double, double)
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException {
		return false;
	}
	
	public boolean elimAlertaByCotizacion(long id_cotizacion)  throws CotizacionesDAOException {
		return false;
	}


	public EmpresasEntity getEmpresaById(long id_empresa) throws CotizacionesDAOException {
		return null;
	}
	
	public boolean addAlertaCotizacion(long id_cotizacion, int id_alerta) throws CotizacionesDAOException {
		return false;
	}
	
	public DireccionEntity getDireccionDespById(long id_dir_desp)  throws CotizacionesDAOException {
		return null;
	}
	
	public double getSumaCostosByCotizacion(CotizacionesDTO dto) throws CotizacionesDAOException{
		return 0;
	}
	
	public boolean getExisteAlertaActiva(long id_cotizacion) throws CotizacionesDAOException {
		return false;
	}
	
	public boolean setModEmpresaById(EmpresasEntity emp) throws CotizacionesDAOException {
		return false;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCategoriasList(long)
	 */
	public List getCategoriasList() throws CotizacionesDAOException {
		return null;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCategoriaById(long)
	 */
	public CategoriaDTO getCategoriaById(long id_categoria) throws CotizacionesDAOException {
		return null;
	}
	
/*
 *  (non-Javadoc)
 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#setAsignaCotizacion(cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO)
 */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException {
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#setLiberaCotizacion(cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO)
	 */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException {
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getTotalProductosCot(long)
	 */
	public double getTotalProductosCot(long id_cot) throws CotizacionesDAOException {
		return 0;
	}	
	
		/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListMarcasByCategoria(long)
	 */
	public List getListMarcasByCategoria(long categoria_id) throws CotizacionesDAOException {
		return null;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListProductosByCriteria(cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO)
	 */
	public List getListProductosByCriteria(ProductosCriteriaDTO criterio) throws CotizacionesDAOException {
		return null;
	}	
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getProductSapById(long)
	 */
	public ProductoSapEntity getProductSapById(long id_prod) throws CotizacionesDAOException {
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getProductoPedidoByIdProdFO(long)
	 */
	public ProductoEntity getProductoPedidoByIdProdFO(long id_prod_fo) throws CotizacionesDAOException {
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCodBarrasByProdId(long)
	 */
	public List getCodBarrasByProdId(long id_prod) throws CotizacionesDAOException {
		return null;
	}	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updCantProductoCotizacion(cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO)
	 */
	public boolean updCantProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException {
		return false;
	}	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 */
	public List getProductosDetCotiz(long id_cot) throws CotizacionesDAOException {
		return null;
	}	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updProductoCotizacion(cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO)
	 */
	public boolean updProductoCotizacion(ProductosCotizacionesDTO prod) throws CotizacionesDAOException {
		return false;
	}
	
	/**
	 * Modifica el comentario de la cotizacion
	 */
	public boolean setModComentarioCotizacion(long id_cot, String comentario, String fuera_mix) throws CotizacionesDAOException {
		return false;
	}
	


	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getSearch(java.util.List, long, long)
	 */
	public List getSearch( List patron, long local_id, long criterio )	throws CotizacionesDAOException {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListItems(java.lang.String, long, long)
	 */
	public List getListItems(String local_id, long pro_padre, long coti_id) throws CotizacionesDAOException {
		return null;
	}	

}
	


