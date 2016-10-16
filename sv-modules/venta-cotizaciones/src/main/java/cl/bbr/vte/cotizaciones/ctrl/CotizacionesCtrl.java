package cl.bbr.vte.cotizaciones.ctrl;

import java.util.ArrayList;
import java.util.List;


import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dao.DAOFactory;
import cl.bbr.vte.cotizaciones.dao.JdbcCotizacionesDAO;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LocalDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductoDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.cotizaciones.exception.CotizacionesDAOException;
import cl.bbr.vte.cotizaciones.exception.CotizacionesException;
import cl.bbr.vte.empresas.dao.JdbcEmpresasDAO;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */

public class CotizacionesCtrl {

	/**
	 * Instancia para log4j
	 */
	private Logging logger = new Logging(this);
	

	/**
	 * Constructor
	 *
	 */
	/*public CotizacionesCTR() {
		this.logger.debug("New CotizacionesCTR");
		this.logger.debug("CotizacionesCTR - conexión a BD ");
		conexion = JdbcDAOFactory.getConexion();
	}*/

	/**
	 * Recupera el listado de categorías
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista con DTO de categorías
	 * @throws SystemException 
	 * @throws ProductosException
	 */
	
	public List getListCategoria( long cliente_id ) throws CotizacionesException, SystemException {
		List result = new ArrayList();
		try {
			JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
			
			List lista = (List) dao.getListCategoria( cliente_id );

			CategoriaDTO cat1 = null;
			CategoriaDTO cat2 = null;
			List hijos = new ArrayList();
			
			// separa los padres de sus hijos
			for( int i = 0; i < lista.size(); i++ ) {
				cat1 = null;
				cat1 = (CategoriaDTO)lista.get(i);
				if(cat1.getId_padre() == 0 ) {
					for( int f = 0; f < lista.size(); f++ ) {
						cat2 = null;
						cat2 = (CategoriaDTO)lista.get(f);
						if( cat2.getId_padre() == cat1.getId() ) {
							hijos.add(cat2);
						}
					}
					cat1.setCategorias(hijos);
					result.add(cat1);
				}
			}
		
		}catch(CotizacionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CotizacionesException(ex); 
			/*if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new EmpresasException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}*/
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return result;
	}	

	
	/**
	 * Recupera el listado de cotizaciones de acuerdo a un criterio especificado
	 * 
	 * @param criterio
	 * @return result
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	
	public List getCotizacionesByCriteria( CotizacionesCriteriaDTO criterio) throws CotizacionesException, SystemException {
		List lista = null;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try{
		lista = dao.getCotizacionesByCriteria(criterio);
		
		}catch (CotizacionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CotizacionesException(ex); 
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		return lista;
	}
	
	
	
	/**
	 * Obtiene un listado de Locales
	 * 
	 * @return List de LocalDTO's
	 * @throws CotizacionesException
	 */
	public List getLocales()  throws CotizacionesException {
		List locs= new ArrayList();
		try{ 
			JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
			List listaLoc = new ArrayList();
			listaLoc = (List) dao.getLocales();
			LocalEntity loc = null;
			for (int i = 0; i < listaLoc.size(); i++) {
				loc = null;
				loc = (LocalEntity) listaLoc.get(i);
				String fec_carga = Formatos.frmFechaHora(loc.getFec_carga_prec());
				LocalDTO locDto = new LocalDTO(loc.getId_local().longValue(), loc.getCod_local(), loc.getNom_local(),
						loc.getOrden().intValue(), fec_carga);
				locs.add(locDto);
			}
			
		}catch(CotizacionesDAOException ex){
			logger.debug("Problema getLocales:"+ex);
			throw new CotizacionesException(ex);
		}
		return locs;

	}


	/**
	 * Obtiene listado de estados de Cotizaciones
	 * 
	 * @return List de EstadoDTO's
	 * @throws CotizacionesException
	 */
	
	public  List getEstadosCotizaciones() throws CotizacionesException {
		List lista = new ArrayList();
		
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			//retorna EstadosDTO
			lista = dao.getEstadosCotizacion();
			
				
		} catch (CotizacionesDAOException e) {
			e.printStackTrace();
			throw new CotizacionesException(e);	
		}
		
		return lista;
	}

	/**
	 * Obtiene la cantidad de cotizaciones, segun criterio de búsqueda.
	 * 
	 * @param  criterio
	 * @return int cantidad
	 * @throws CotizacionesException.
	 * 
	 */
	public int getCountCotizacionesByCriteria( CotizacionesCriteriaDTO criterio )
		throws CotizacionesException, SystemException{
		int res=-1;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			//retorna EstadosDTO
			res = (int) dao.getCountCotizacionesByCriteria(criterio);					
			} catch (CotizacionesDAOException ex) {
				logger.debug("Problema :"+ex.getMessage());
				throw new CotizacionesException(ex);			
			}
			catch (Exception e) {
				throw new SystemException(e);
			}
		return res;
	}
	
	/**
	 * Retorna el detalle de la cotización segun Id
	 * @param cot_id
	 * @return CotizacionesDTO
	 * @throws CotizacionesException
	 */
	
	public CotizacionesDTO getCotizacionById( long cot_id) throws CotizacionesException , SystemException{
		CotizacionesDTO dto = new CotizacionesDTO();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try{
			dto = dao.getCotizacionById(cot_id);
			
			/*	
			// Desencriptamos los 4 ult digitos del número de la tarjeta
			ResourceBundle rb = ResourceBundle.getBundle("bo");
			String key = rb.getString("conf.bo.key");
			String mp = Cifrador.desencriptar(key,dto.getCot_num_mpago());
				*/
			
		}catch (CotizacionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CotizacionesException(ex); 
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		return dto;
		
	}
	

	public String getEstadoSaldoEmpresaByCotizacion( long cot_id) throws CotizacionesException , SystemException{
		//CotizacionesDTO dto = new CotizacionesDTO();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try{
			return dao.getEstadoSaldoEmpresaByCotizacion(cot_id);
		}catch (CotizacionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CotizacionesException(ex); 
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	
	public long getDireccionByCotizacion( long cot_id) throws CotizacionesException , SystemException{
		//CotizacionesDTO dto = new CotizacionesDTO();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try{
			return dao.getDireccionByCotizacion(cot_id);
		}catch (CotizacionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new CotizacionesException(ex); 
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}


	/**
	 * Obtiene la cantidad de productos asociados a una cotización 
	 * segun su id
	 * @param cot_id
	 * @return long
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public long getCountProductosEnCotizacionById( long cot_id) throws CotizacionesException , SystemException{
		long res=0;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			res = (long) dao.getCountProductosEnCotizacionById(cot_id);			
			} catch (CotizacionesDAOException ex) {
				logger.debug("Problema :"+ex.getMessage());
				throw new CotizacionesException(ex);			
			}
			catch (Exception e) {
				throw new SystemException(e);
			}
		return res;
	}
	

	
	/**
	 * Obtiene la cantidad de productos asociados a una cotización
	 * @param id_cot
	 * @return List AlertasDTO
	 * @throws CotizacionesException
	 */
	public List getAlertasCotizacion(long id_cot)
		throws CotizacionesException{
		
		List result = new ArrayList();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try {
			result = dao.getAlertasCotizacion(id_cot);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		return result;
	}
	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesException
	 */
	public List getProductosCotiz(long id_cot)
	throws CotizacionesException{
	
	List result = new ArrayList();
	JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
	try {
		result = dao.getProductosCotiz(id_cot);
	} catch (CotizacionesDAOException e) {
		logger.debug("Problema :"+e.getMessage());
		throw new CotizacionesException(e);	
	}		
return result;
}

	/**
	 * Inserta un nuevo encabezado de cotización sin detalle de productos
	 * 
	 * @param dto Datos de la cotización
	 * @return Identificador único de la cotización insertada
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public long doInsCotizacionHeader( ProcInsCotizacionDTO dto )  throws CotizacionesException, SystemException{
		long id = -1;
		
		//validar campos obligatorios de la cotizacion
		if ( dto.getId_empresa() <= 0 ){
			throw new CotizacionesException("No existe id_empresa 	: "+ dto.getId_empresa());
		}
		if ( dto.getId_sucursal() <= 0 ){
			throw new CotizacionesException("No existe id_sucursal 	: "+ dto.getId_sucursal());
		}
		if ( dto.getId_comprador()<= 0 ){
			throw new CotizacionesException("No existe id_comprador : "+ dto.getId_comprador());
		}
		if ( dto.getId_dir_desp()<= 0 ){
			throw new CotizacionesException("No existe id_dir_desp 	: "+ dto.getId_dir_desp());
		}
		if ( dto.getId_dir_fac()<= 0 ){
			throw new CotizacionesException("No existe id_dir_fac 	: "+ dto.getId_dir_fac());
		}
		if ( dto.getMedio_pago()==null || dto.getMedio_pago().equals("")){
			throw new CotizacionesException("No existe medio_pago 	: "+ dto.getMedio_pago());
		}
		if ( dto.getFec_acordada()==null || dto.getFec_acordada().equals("")){
			throw new CotizacionesException("No existe fec_acordada : "+ dto.getFec_acordada());
		}
		
		//creacion del dao
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			//llenar y setear dto
			if ( dto.getTipo_cpr()==null )	dto.setTipo_cpr(Constantes.TIPO_VE_NORMAL_CTE);
			dto.setFec_ingreso(Formatos.getFecHoraActual());
			dto.setMonto_total(0);
			if ( dto.getNumero_tarjeta()==null )		dto.setNumero_tarjeta(Constantes.CADENA_VACIA);
			if ( dto.getMedio_pago_clave()==null )	dto.setMedio_pago_clave(Constantes.CADENA_VACIA);
			if ( dto.getObs()==null )			dto.setObs(Constantes.CADENA_VACIA);
			dto.setEstado(Constantes.ID_EST_COTIZACION_INGRESADA);
			if ( dto.getNombre_banco()==null )		dto.setNombre_banco(Constantes.CADENA_VACIA);
			if ( dto.getFueramix()==null )		dto.setFueramix(Constantes.CADENA_VACIA);
			if ( dto.getTipo_doc()==null )		dto.setTipo_doc(Constantes.TIPO_DOC_FACTURA);
			
			id = dao.doInsCotizacion(dto);
			logger.debug("nuevo id: "+id);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		
		return id;
		
	}

	/**
	 * Inserta una lista de detalles (productos) a una cotización.
	 * 
	 * @param lst_det Lista con DTO de los detalles
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public void doInsCotizacionDetail( List lst_det )  throws CotizacionesException, SystemException{
		
		try {
			//creacion del dao
			JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();		

			boolean existe_producto = false;
			
			// Se itera por los productos a insertar
			for(int i=0; i<lst_det.size(); i++){
				
				existe_producto = false;
				
				ProcInsDetCotizacionDTO det = (ProcInsDetCotizacionDTO)lst_det.get(i);
				
				// Recuperar datos del producto para completar la información
				List l_prod = dao.getProductosCotiz( det.getCot_id() );
				ProductosCotizacionesDTO prod = null;
				for( int j = 0; j < l_prod.size(); j++ ) {
					prod = (ProductosCotizacionesDTO)l_prod.get(j);
					if( prod.getDetcot_id_fo() == det.getPro_id() ) {
						logger.debug("Existe el producto: " + det.getPro_id() );
						//actualizar la cantidad
						ModProdCotizacionesDTO procot = new ModProdCotizacionesDTO();
						procot.setDetcot_id( prod.getDetcot_id() );
						procot.setDetcot_cantidad( det.getQsolic() );
						boolean resDet = dao.updCantProductoCotizacion( procot );
						logger.debug("resDet : "+resDet);
						existe_producto = true;
						break;
					}
				}
				if( existe_producto == false ) {
					logger.debug("No existe el producto: " + det.getPro_id() );
					boolean resDet = dao.doInsDetCotizacion(det);
					logger.debug("resDet : "+resDet);
				}
			}
		} catch (CotizacionesDAOException e) {
			e.printStackTrace();
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}
		
	}
	
	/**
	 * Inserta una nueva cotizacion y los detalles que la contiene
	 * 
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  dto
	 * @return long, indica nuevo id de la cotizacion
	 * @throws CotizacionesException
	 * @throws SystemException 
	 */
	public long doInsCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesException, SystemException{
		
		long id = -1;
		
		//validar campos obligatorios de la cotizacion
		if ( dto.getId_empresa() <= 0 ){
			throw new CotizacionesException("No existe id_empresa 	: "+ dto.getId_empresa());
		}
		if ( dto.getId_sucursal() <= 0 ){
			throw new CotizacionesException("No existe id_sucursal 	: "+ dto.getId_sucursal());
		}
		if ( dto.getId_comprador()<= 0 ){
			throw new CotizacionesException("No existe id_comprador : "+ dto.getId_comprador());
		}
		if ( dto.getId_dir_desp()<= 0 ){
			throw new CotizacionesException("No existe id_dir_desp 	: "+ dto.getId_dir_desp());
		}
		if ( dto.getId_dir_fac()<= 0 ){
			throw new CotizacionesException("No existe id_dir_fac 	: "+ dto.getId_dir_fac());
		}
		if ( dto.getMedio_pago()==null || dto.getMedio_pago().equals("")){
			throw new CotizacionesException("No existe medio_pago 	: "+ dto.getMedio_pago());
		}
		if ( dto.getFec_acordada()==null || dto.getFec_acordada().equals("")){
			throw new CotizacionesException("No existe fec_acordada : "+ dto.getFec_acordada());
		}
		if ( dto.getFec_vencimiento()==null || dto.getFec_vencimiento().equals("")){
			throw new CotizacionesException("No existe fec_vencimiento : "+ dto.getFec_vencimiento());
		}
		if ( dto.getTipo_doc()==null || dto.getTipo_doc().equals("")){
			throw new CotizacionesException("No existe tipo_doc 	: "+ dto.getTipo_doc());
		}
		
		//validar campos obligatorios del detalle de cotizacion
		List lst_det = dto.getLst_detalle();
		if ( lst_det.size()==0) {
			throw new CotizacionesException("No existen detalles de la cotizacion	: "+ lst_det.size());
		}
		for(int i=0; i<lst_det.size(); i++){
			ProcInsDetCotizacionDTO det = (ProcInsDetCotizacionDTO)lst_det.get(i);
			if( det.getPro_id()<=0){
				throw new CotizacionesException("Detalle - No existe Id_producto	: "+ det.getPro_id());
			}
			if( det.getPro_id_bo()<=0){
				throw new CotizacionesException("Detalle - No existe Id_producto_BO	: "+ det.getPro_id_bo());
			}
			if( det.getCod_prod1()==null || det.getCod_prod1().equals("")){
				throw new CotizacionesException("Detalle - No existe cod_prod1	: "+ det.getCod_prod1());
			}
			if( det.getUni_med()==null || det.getUni_med().equals("")){
				throw new CotizacionesException("Detalle - No existe uni_med	: "+ det.getUni_med());
			}
		}
		
		
		//iteramos los detalle de la cotizaciones
		double cant_prods = 0;
		double monto_total = 0;
		List lst_det_cot = dto.getLst_detalle();
		for(int i = 0; i<lst_det_cot.size(); i++){
			ProcInsDetCotizacionDTO det = (ProcInsDetCotizacionDTO) lst_det_cot.get(i);
			cant_prods 	+= det.getQsolic();
			monto_total += (det.getPrecio_lista() * det.getQsolic() * (1 - det.getDscto_item()));
		}
		
		//creacion del dao
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		//Creamos la transacción
		JdbcTransaccion trx = new JdbcTransaccion();
		
		try {

			// Iniciamos la transacción
			trx.begin();
			
			// Asignamos la transacción a los dao's
			dao.setTrx(trx);
		} catch (CotizacionesDAOException e){
			e.printStackTrace();
			throw new SystemException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
			
				
		try {
			//llenar y setear dto
			if ( dto.getTipo_cpr()==null )				dto.setTipo_cpr(Constantes.TIPO_VE_NORMAL_CTE);
			if ( dto.getNumero_tarjeta()==null )		dto.setNumero_tarjeta(Constantes.CADENA_VACIA);
			if ( dto.getMedio_pago_clave()==null )		dto.setMedio_pago_clave(Constantes.CADENA_VACIA);
			if ( dto.getObs()==null )					dto.setObs(Constantes.CADENA_VACIA);
			if ( dto.getTbk_nombre_tarjeta()==null )	dto.setTbk_nombre_tarjeta(Constantes.CADENA_VACIA);
			if ( dto.getFueramix()==null )				dto.setFueramix(Constantes.CADENA_VACIA);
			if ( dto.getTipo_doc()==null )				dto.setTipo_doc(Constantes.TIPO_DOC_FACTURA);
			dto.setFec_ingreso(Formatos.getFecHoraActual());
			dto.setMonto_total(monto_total);
			dto.setEstado(Constantes.ID_EST_COTIZACION_INGRESADA);
			
			id = dao.doInsCotizacion(dto);
			logger.debug("nuevo id: "+id);
			if(id>0){
				//insertar cada detalle de la cotizacion
				for(int i=0; i<lst_det.size(); i++){
					ProcInsDetCotizacionDTO det = (ProcInsDetCotizacionDTO)lst_det.get(i);
					
					//llenar y setear detalle de cotizacion
					det.setCot_id(id);
					if ( det.getDescripcion() == null)	det.setDescripcion(Constantes.CADENA_VACIA);
					if ( det.getObs() == null)			det.setObs(Constantes.CADENA_VACIA);
					if ( det.getPreparable() == null)	det.setPreparable(Constantes.INDICADOR_NO);
					if ( det.getPesable() == null)		det.setPesable(Constantes.INDICADOR_NO);
					if ( det.getCon_nota() == null)		det.setCon_nota(Constantes.INDICADOR_NO);
					
					boolean resDet = dao.doInsDetCotizacion(det);
					logger.debug("resDet : "+resDet);
				}
			}
			
			//Finaliza la transacción
			
			
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			try { 
				trx.rollback();
			} catch (DAOException e1) { 
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new CotizacionesException(e);	
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); throw new SystemException("Error al hacer rollback"); }
			throw new SystemException(e);
        } finally{
            //  cerramos trx
            try {
                trx.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }	
		
		return id;
	}
	
	
	/**
	 * Retorna una lista con los pedidos asociados a una cotización
	 * @param id_cot
	 * @return
	 * @throws CotizacionesException
	 */
	public List getPedidosCotiz(long id_cot)
	throws CotizacionesException{
	
	List result = new ArrayList();
	JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try {
			result = dao.getPedidosCotiz(id_cot);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		return result;
	}
	
	
	
	/**
	 * Retorna una lista de logs asociados a una cotización.
	 * @param id_cot
	 * @return List LogsCotizacionesDTO
	 * @throws CotizacionesException
	 */
	public List getLogsCotiz(long id_cot) throws CotizacionesException{
		List result = new ArrayList();
		
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try{
			result = dao.getLogCotiz(id_cot);
		}catch (CotizacionesDAOException e){
			logger.debug("Problema: " + e.getMessage());
			throw new CotizacionesException(e);
		}
		
		return result;
	}


	/**
	 * Obtiene el listado de productos de la cotizacion, incluyendo pedidos relacionados
	 * 
	 * @param id_cotizacion
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesException
	 */
	public List getLstProductosByCotizacion(long id_cotizacion) throws CotizacionesException{
		List result = new ArrayList();
		
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try{
			logger.debug("inicio getLstProductosByCotizacion");
			//revisar si la cotizacion tiene pedidos
			List lst_ped = dao.getPedidosCotiz(id_cotizacion);
			if(lst_ped.size()>0){
				//obtener el listado de productos en cotizacion y en pedidos relacionados
				List lst_aux = dao.getLstProductosByCotizacion(id_cotizacion);
				if(lst_aux.size()>0){
					double cant_tot = 0;
					String str_id_pedidos = "";
					logger.debug("Total lst_aux: " + lst_aux.size());
					for(int i=0; i<lst_aux.size(); i++){
						List l_ped = new ArrayList();
						ProductosCotizacionesDTO aux = (ProductosCotizacionesDTO) lst_aux.get(i);
						cant_tot = aux.getCant_pedido();
						logger.debug("Cantidad total i = " + i + " es = a: " + cant_tot);
						if( aux.getId_pedido()!=null && !aux.getId_pedido().equals("") && !aux.getId_pedido().equals("0")){
							str_id_pedidos = aux.getId_pedido();
							l_ped.add(aux.getId_pedido());
						}else{
							str_id_pedidos = "";
						}
						for(int j=i+1; j<lst_aux.size(); j++){
							ProductosCotizacionesDTO auxJ = (ProductosCotizacionesDTO) lst_aux.get(j);
							if(aux.getDetcot_proId()==auxJ.getDetcot_proId()){
								
								if( auxJ.getId_pedido()!=null && !auxJ.getId_pedido().equals("") && !auxJ.getId_pedido().equals("0")){
									/*
									boolean existe =str_id_pedidos.contains(auxJ.getId_pedido());
									if (!existe){
									*/
									//if( str_id_pedidos.indexOf( auxJ.getId_pedido() ) > -1 ) {
									if( !l_ped.contains( auxJ.getId_pedido() ) ) {
										cant_tot += auxJ.getCant_pedido();
										str_id_pedidos += ", "+auxJ.getId_pedido();
										l_ped.add(auxJ.getId_pedido());
									}
								}
								i++;
							}else{
								aux.setCant_pedido(cant_tot);
								aux.setId_pedido(str_id_pedidos);
								result.add(aux);
								break;
							}
						}
						if(i+1 == lst_aux.size()){
							aux.setCant_pedido(cant_tot);
							aux.setId_pedido(str_id_pedidos);
							result.add(aux);
						}
					}

				}//fin del list_aux
			} else {
				//no existen pedidos, entonces se obtiene los productos de la cotizacion
				List lst_aux = dao.getProductosCotiz(id_cotizacion);
				for(int i=0; i<lst_aux.size(); i++){
					ProductosCotizacionesDTO aux = (ProductosCotizacionesDTO)lst_aux.get(i);
					aux.setCant_pedido(0);
					aux.setId_pedido(Constantes.CADENA_VACIA);
					result.add(aux);
				}
			}

		}catch (CotizacionesDAOException e){
			logger.debug("Problema: " + e.getMessage());
			throw new CotizacionesException(e);
		}
		
		return result;
	}
	
	/**
	 * Permite agregar un registro de log a la cotizacion
	 * @param log
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean addLogCotizacion(LogsCotizacionesDTO log)
	throws CotizacionesException, SystemException{ 
	boolean result = false;
	JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
	
	try{
		result=dao.addLogCotizacion(log);
	}catch(CotizacionesDAOException ex){
		if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
			throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE);
		}
		else
			throw new SystemException("Error no controlado al insertar log cotizacion",ex);
	}		
	return result;
	
}
	
	
	/**
	 * Actualiza una cotización de acuerdo a los parametros obtenidos
	 * @param id_cot
	 * @param obs
	 * @param fueraMix
	 * @param costo_despacho
	 * @return boolean
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setModCotizacion(ModCotizacionDTO cot)
	throws CotizacionesException, SystemException {
	
	// Creamos los dao's
	JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
	
	boolean actualizo = false;
	LogsCotizacionesDTO log = new LogsCotizacionesDTO ();
	LogsCotizacionesDTO log2 = new LogsCotizacionesDTO ();
	CotizacionesDTO cotizacion = null;
		
	//Creamos la transacción
	JdbcTransaccion trx = new JdbcTransaccion();
	
	String fecha_venc="";
	String fecha_actual="";
	long fvenc=0;
	long factual=0;
	long diferencia=0;

	try {	
		// Iniciamos la transacción
		trx.begin();
		
		// Asignamos la transacción a los dao's
		dao.setTrx(trx);
		
		//actualizar la direccion en el pedido
		actualizo = dao.doUpdCotizacion(cot);
		if (actualizo){			
			//logear el cambio
			String mjeLog = "Se ha Modificado la cotización.";
			log.setCot_id(cot.getId_cot());
			log.setDescripcion(mjeLog);
			log.setUsuario(cot.getUsuario());			
			dao.addLogCotizacion(log);
			
			//busca si el estado actual de la cotizacion es caducada
			cotizacion = dao.getCotizacionById(cot.getId_cot());
			
			//revisa la fecha de vencimiento			
			fecha_venc = Formatos.formatFecha(cot.getFecha_venc());
			fecha_actual= Formatos.getFechaActual();
			
			fvenc = Long.parseLong(fecha_venc.substring(0,4)+fecha_venc.substring(5,7)+fecha_venc.substring(8,10));
			factual = Long.parseLong(fecha_actual.substring(0,4)+fecha_actual.substring(5,7)+fecha_actual.substring(8,10));
			diferencia =fvenc-factual;
			logger.debug("fecha vencimiento:"+fecha_venc+" valor:"+fvenc);
			logger.debug("fecha actual:"+fecha_actual+" valor:"+factual);
			logger.debug("diferencia (V-A):"+diferencia);
			
			
			if ((cotizacion.getCot_estado_id()==Constantes.ID_EST_COTIZACION_CADUCADA)
					&&(diferencia>0)){
				/* si esta caducada y la fecha de vencimiento que viene en esta modificacion 
				 * es mayor a la fecha actual debe cambiar el estado de la cotizacion a En Revisión 
				 */ 	
				if (dao.setModEstadoCotizacion(cot.getId_cot(), Constantes.ID_EST_COTIZACION_EN_REVISION)){
					log2.setCot_id(cot.getId_cot());
					log2.setDescripcion("Se ha descaducado la cotizacion");
					log2.setUsuario(cot.getUsuario());					
					dao.addLogCotizacion(log2);
				}				
			}			
		}
		else{
			logger.debug("No se pudo modificar cotización");						
		}
		
	} catch (CotizacionesDAOException ex) {
		logger.debug("Problema :" + ex);
		try { 
			trx.rollback(); 
		} catch (DAOException e1) { 
			e1.printStackTrace (); 
		}
		if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
			logger.debug("no existe id cotización");
			throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
		}
		else
			throw new SystemException("Error no controlado",ex);				
		
	} catch (Exception e) {
		try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
		throw new SystemException(e);
    } finally{
        //  cerramos trx
        try {
            trx.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
    
    }

	return actualizo;
	
}

	/**
	 * Permite anular una cotizacion
	 * @param id_cot
	 * @return boolean
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setAnularCotizacion(long id_cot) throws CotizacionesException, SystemException {
		boolean res = false; 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		JdbcTransaccion trx = new JdbcTransaccion();
		
		try {
			
			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);			
			
			boolean flag_rollback = false;			

			List resultDet = dao.getProductosDetCotiz(id_cot);
			
			// Se itera por los productos y modifica
			for(int i=0; i<resultDet.size(); i++){
				ProductosCotizacionesDTO det = (ProductosCotizacionesDTO)resultDet.get(i);

				det.setDetcot_desc(det.getDetcot_desc());
				det.setPro_tipre(det.getPro_tipre());
				//det.setDetcot_precio(det.getDetcot_precio());
				det.setDetcot_cot_id(id_cot);
				det.setDetcot_proId(det.getDetcot_proId());
				
				// Actualiza datos de los productos
				res = dao.updProductoCotizacion(det);
				logger.debug("res : "+res);
				if(res == false)
					flag_rollback = true;
				
			}
			if( flag_rollback == true )
				throw new CotizacionesDAOException( "Error al insertar." );				
			
			int id_estado = Constantes.ID_EST_COTIZACION_ANULADA;	//estado anulado
			res = dao.setModEstadoCotizacion(id_cot, id_estado);
			if(res == false)
				flag_rollback = true;
			
			if( flag_rollback == true )
				throw new CotizacionesDAOException( "Error al anular la cotización." );	

			//Finaliza la transacción
								
			
		}  catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
        } finally{
            //  cerramos trx
            try {
                trx.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		
		return res;
	}
	
	
	/**
	 * Permite agregar un producto a la cotización
	 * @param prod
	 * @return
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod)
	throws CotizacionesException, SystemException {
	
	// Creamos los dao's
	JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
	
	boolean actualizo = false;
	//LogsCotizacionesDTO log = new LogsCotizacionesDTO ();
	
	try {

		ProductoEntity prod1 = null;
		
		prod1 = dao.getProductoPedidoByIdProdFO(prod.getDetcot_proId());
		logger.debug("prod1: " + prod1);
		if (prod1==null){
			logger.debug("El producto no existe");
			throw new CotizacionesException(Constantes._EX_COT_PROD_ID_NO_EXISTE);
		}
		long id_prod_bo = prod.getDetcot_pro_id_bo();

    	//verificar el estado del producto web
		if( !prod1.getEstado().equals(Constantes.ESTADO_PUBLICADO) ){
			throw new CotizacionesException(Constantes._EX_COT_PROD_DESPUBLICADO);
		}


    	//revisar si tiene cod_barras
    	int cant_cbarra = dao.getCodBarrasByProdId(id_prod_bo).size();
    	logger.debug("cant_cbarra:"+cant_cbarra);
    	if(cant_cbarra == 0){
    		throw new CotizacionesException(Constantes._EX_COT_CODBARRA_NO_EXISTE);
    	}
    	
		//actualizar la direccion en el pedido
		if (prod.getDetcot_precio_lista() == 0){
			logger.debug("Precio = 0");
			throw new CotizacionesException(Constantes._EX_COT_PROD_PRECIO_CERO);
		}
		
		//descripcion mejorada
		prod.setDetcot_desc(prod1.getTipo()+" "+prod1.getNom_marca()+" "+prod1.getDesc_corta());
		actualizo = dao.addProductoCotizacion(prod);
	
	} catch (CotizacionesDAOException ex) {
		logger.debug("Problema :" + ex);
		if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
			logger.debug("no existe id cotización");
			throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
		}
		else
			throw new SystemException("Error no controlado",ex);
	} 

	return actualizo;
	
}
	
	
	/**
	 * Permite eliminar un producto de una cotizacion
	 * @param detcot_id
	 * @return boolean
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean delProductoCotizacion(long detcot_id)
	throws CotizacionesException, SystemException {
	
		// Creamos los dao's
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		boolean elimino = false;
		//LogsCotizacionesDTO log = new LogsCotizacionesDTO ();
		
		try {
			//actualizar la direccion en el pedido
			elimino = dao.delProductoCotizacion(detcot_id);
		
			/*if (elimino){
				//logear el cambio
				dao.addLogCotizacion(log);
			}
			else{
				logger.debug("No se pudo modificar cotización");						
			}*/
		} catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe id detalle cotización");
				throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else
				throw new SystemException("Error no controlado",ex);
		} 

		return elimino;
	
	}
	
	
	/**
	 * Permite caducar  cotizaciones con fecha mayor a la fecha de expiracion
	 * @param id_comprador Identificador único del comprador
	 * @return
	 * @throws SystemException
	 */
	public boolean CaducarCotizaciones( long id_comprador ) throws SystemException {
		boolean res = false; 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			
			res = dao.CaducarCotizaciones( id_comprador );
			
		} catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} 
		return res;
	}
	
	
	/**
	 * Permite modificar datos(cantidad, descuento, precio) de un producto asociado a una cotizacion
	 * @param prod
	 * @return
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesException, SystemException {
		boolean res = false; 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			res = dao.updProductoCotizacion(prod);
			
		}  catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe id detalle cotización");
				throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else
				throw new SystemException("Error no controlado",ex);
		} 
		return res;
	}


	/**
	 * Realiza el proceso de validación de la cotizacion. Las definiciones de alertas se encuentran en la
	 * tabla VE_COTZ_ALERT<br>
	 * 
	 * Reglas de validación:<br> 
	 * 
	 * 1. Cumplimiento de margen minimo
	 * 2. Cumplimiento de descuento maximo
	 * 3. Cambio de razon social
	 * 4. Direccion nueva de despacho
	 * 5. Empresa esta bloqueada 
	 * 
	 * Nota: Este Método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  id_cotizacion
	 * @return boolean devuelve <i>true</i> en caso que el proceso de validación fue satisactoria, caso contrario devuelve <i>false</i>
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setValidarCotizacion(long id_cotizacion) throws CotizacionesException, SystemException {
		//revisar si existe la cotizacion
		CotizacionesDTO cot = this.getCotizacionById(id_cotizacion);
		if(cot==null)
			throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE);
		
		
		boolean res = false; 
		
		logger.debug("Inicio Proceso de Validación de Cotizacion...");
		
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		JdbcEmpresasDAO dao2 = (JdbcEmpresasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEmpresasDAO();
		
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		try {
			dao.setTrx(trx1);
			dao2.setTrx(trx1);
		} catch (CotizacionesDAOException e){
			logger.error("Error al asignar transacción al dao Cotizaciones");
			throw new SystemException("Error al asignar transacción al dao Cotizaciones");
		} catch (EmpresasDAOException e) {
			logger.error("Error al asignar transacción al dao Empresas");
			throw new SystemException("Error al asignar transacción al dao Empresas");
		}
		
		try {
			//eliminacion de alertas
			dao.elimAlertaByCotizacion(id_cotizacion);
			List lst_alarmas = dao.getAlertasCotizacion(id_cotizacion);
			
			if(lst_alarmas.size()==0){
				logger.debug("Inicia revision de alertas");
				boolean flag_costo_cero=false;
				
				/*
				 * Alerta 1: Cumplimiento de margen minimo
				 * - Calcular sumatoria de costos
				 * - Calcular sumatoria de precios
				 * - margen = sum_costos / sum_precios 
				 * */
				logger.debug("--- ALERTA 1:Cumplimiento margen minimo ---");
				double sum_costos = dao.getSumaCostosByCotizacion(cot);
				double sum_precios = 0;
				List lst_det = dao.getProductosCotiz(cot.getCot_id());
				for(int i=0; i<lst_det.size(); i++){
					ProductosCotizacionesDTO det = (ProductosCotizacionesDTO) lst_det.get(i);
					sum_precios += (det.getDetcot_precio() * det.getDetcot_cantidad());
					logger.debug("producto:"+i+" costo:"+det.getPre_costo());
					if (det.getPre_costo()==0){
						
						flag_costo_cero = true;
					}
				}
				double margen=0;
				if(sum_costos == 0.0){
					logger.debug("Los productos no tienen costos");					
				}
				else {
					margen = (1-(sum_costos / sum_precios))*100;
					logger.debug("margen:"+margen+" sum_costos / sum_precios :"+sum_costos +"/"+ sum_precios);
				}
				
				EmpresasEntity emp = dao.getEmpresaById(cot.getCot_emp_id());
				logger.debug("margen minimo:"+emp.getMrg_minimo().doubleValue());
				if(margen < emp.getMrg_minimo().doubleValue()){
					
					//Autorizacion de margen
					if(!cot.getCot_aut_margen().equals(Constantes.INDICADOR_SI)){
						res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_MARGEN_MINIMO);
						logger.info("MARCA Alerta Cumplimiento de margen minimo");
						}
					else{
						logger.info("ANULA!! Alerta Cumplimiento de margen minimo, la cotización fue autorizada por un supervisor");						
						res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_AUT_MARGEN);
						logger.info("MARCA Alerta Margen Menor Autorizado");
						}
					
					
				}
				
				/*
				 * Alerta 2: Cumplimiento de descuento maximo
				 * - Calcular sumatoria de precio de lista
				 * - Calcular sumatoria de precio final
				 * divider,
				 * - margen 
				 * */
				logger.debug("--- ALERTA 2:Cumplimiento de descuento maximo ---");
				
				double sum_prec_lista = 0;
				double sum_prec_final = sum_precios;
				for(int i=0; i<lst_det.size(); i++){
					ProductosCotizacionesDTO det = (ProductosCotizacionesDTO) lst_det.get(i);
					sum_prec_lista += (det.getDetcot_precio_lista() * det.getDetcot_cantidad());
					
				}
				double descuento = (1-(sum_prec_final/sum_prec_lista))*100;
				logger.debug("descuento:"+descuento+" sum_prec_final/sum_prec_lista :"+sum_prec_final+"/"+sum_prec_lista);
				logger.debug("descuento maximo:"+emp.getDscto_max().doubleValue());

				if(descuento >= emp.getDscto_max().doubleValue()){
					
					if (!cot.getCot_aut_dscto().equals(Constantes.INDICADOR_SI)){
						res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_DSCTO_MAXIMO);
						logger.info("MARCA Alerta Cumplimiento de descuento maximo");
						}
					else {
						logger.info("ANULA!! Alerta Cumplimiento de descuento maximo, la cotización fue autorizada por un supervisor");						
						res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_AUT_DESCTO);
						logger.info("MARCA Alerta Descuento Mayor Autorizado");
					}
					
					
				}
				
				/*
				 * Alerta 3 : Cambio de razon social
				 * - Obtener el flag q indica si modifico o no la razon social
				 * -  
				 * */
				logger.debug("--- ALERTA 3:Cambio de razon social ---");
				if(emp.getMod_rzsoc().intValue() == Constantes.EMP_MOD_RAZON_SOCIAL){
					res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_MOD_RAZ_SOCIAL);
					logger.info("Marca Alerta Cambio de razon social");
				}
		
				/*
				 * Alerta 4 : Direccion nueva de despacho 
				 * - Obtener el flag q indica si la direccion de despacho es nueva
				 * -  
				 * */
				logger.debug("--- ALERTA 4:Direccion nueva de despacho ---");
				DireccionEntity dirDesp = dao.getDireccionDespById(cot.getCot_dir_id());
				if(dirDesp.getFnueva()!= null && dirDesp.getFnueva().equals(Constantes.ESTADO_NUEVO)){
					res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_DIRECC_DESP_NUEVA);
					logger.info("Marca Alerta Direccion nueva despacho");
				}
				
				/*
				 * Alerta 5 : Empresa esta bloqueada 
				 * - Obtener el estado q indica si la empresa es bloqueada
				 * -  
				 * */
				logger.debug("--- ALERTA 5:Empresa esta bloqueada  ---");
				if(emp.getEstado().equals(Constantes.EMP_BLOQUEADO)){
					res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_EMPRESA_BLOQUEADA);
					logger.info("Marca Alerta Empresa esta bloqueada ");
				}
				/*
				 * Alerta 6: La cotización no tiene fecha de vencimiento
				 * - Revisar si la fecha de vencimiento es nula 
				 * - Alerta Dura
				 * */
				logger.debug("--- ALERTA 6:La cotización no tiene fecha de vencimiento  ---");
				if (cot.getCot_fec_vencimiento()==null){
					res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_SIN_FECHA_VENC);
					logger.info("Marca Alerta sin fecha de vencimiento");
				}

				/*
				 * Alerta 7: La cotizacion tiene productos con costo cero.
				 * - Revisar los productos y establecer la alerta si uno de ellos tiene costo cero
				 * - Alerta Informatica
				 * */				
				logger.debug("--- ALERTA 7:La cotizacion tiene productos con costo cero  ---");
				if (flag_costo_cero==true){
					res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_PROD_SIN_COSTO);
					logger.info("Marca Alerta La cotizacion tiene productos con costo cero");
				}
				
				
				/*
				 * ALERTA 8: Medio de pago sin cupo
				 * - Revisa el saldo de los medios de pago 
				 * - Linea de credito : "CRE"
				 * 		Debe disminuir el sado de lo pedidos que esten pendientes que no esten anulados.
				 * 		Los pedidos pagados se consideran hasta 48 horas de la trx. de pago.
				 * - Tarjeta Paris "Pend" 
				 */
				logger.debug("--- ALERTA 8:Medio de pago sin cupo  ---");
				
				
				if (cot.getCot_mpago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO)){
					logger.debug("Verifica cupo del medio de pago:"+cot.getCot_mpago());
					
					double saldo_reportado_if = emp.getSaldo().doubleValue();
					double saldo_pendiente  = dao2.getSaldoActualPendiente(cot.getCot_emp_id());
					double disponible_actual = saldo_reportado_if - saldo_pendiente;
					logger.debug("saldo reportado por la interfaz : "+saldo_reportado_if);
					logger.debug("saldo pedidos pendientes        : "+saldo_pendiente);
					logger.debug("disponible actual               : "+disponible_actual);
					
					//obtiene monto total en variable suma_precios_con_descto 
					double suma_precios_con_descto=0.0;
					List listaProductos = null;
					listaProductos =  dao.getProductosCotiz(id_cotizacion);				
					for (int i=0; i<listaProductos.size(); i++){	
						ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
						prod = (ProductosCotizacionesDTO)listaProductos.get(i);
						/*
						 if (document.form.descuento_{i}.value==""){	
							suma_precios_con_descto+= document.form.cantidad_{i}.value *{precio_lista};}
						else{	
							suma_precios_con_descto+= document.form.cantidad_{i}.value *{precio_lista} * (1 - (document.form.descuento_{i}.value/100) );	}					
						 */
						
						if (prod.getDetcot_dscto_item()<=0.0){
							suma_precios_con_descto+=  prod.getDetcot_cantidad() * prod.getDetcot_precio_lista();
						}
						else{
							suma_precios_con_descto+=  prod.getDetcot_cantidad() * prod.getDetcot_precio_lista() *(1-prod.getDetcot_dscto_item()/100);						
						}
					}
					double saldo_cotizacion = disponible_actual - (suma_precios_con_descto + cot.getCot_costo_desp());	
					logger.debug("saldo cotizacion                : "+saldo_cotizacion);
					if (saldo_cotizacion<= 0.0){
						//alerta de linea de credito
						res = dao.addAlertaCotizacion(id_cotizacion, Constantes.COT_ALE_SIN_CUPO);
						logger.info("Marca Alerta el medio de pago no tiene cupo");
					}
					
				}
				
				
				
				//actualiza el estado a "En validacion"
				dao.setModEstadoCotizacion(id_cotizacion, Constantes.ID_EST_COTIZACION_EN_VALIDACION);
				
				logger.debug("Finaliza revision de alertas");
				res = true;
				
				
			}
			
			
			
		}  catch (CotizacionesDAOException ex) {
			//rollback trx
			try { trx1.rollback(); } catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe id detalle cotización");
				throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else
				throw new SystemException("Error no controlado",ex);
		} catch (EmpresasDAOException ex) {
			//			rollback trx
			try { trx1.rollback(); } catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new SystemException("Error no controlado",ex);
		} finally{
            //  cerramos trx
            try {
                trx1.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		

		
		return res;
	}


	/**
	 * Permite cambiar el estado de la cotizacion a Cotizada, segun el <b>id</b> de cotizacion.<br> 
	 * Luego de cotizar, actualiza información en la empresa.
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param id_cotizacion
	 * @param usr
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrartio devuelve <i>false</i>.
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setConfirmarCotizacion(long id_cotizacion, UserDTO usr) throws CotizacionesException, SystemException {
		//ver si la cotizacion es valida 
		boolean isValida = this.setValidarCotizacion(id_cotizacion);
		
		logger.debug("isValida ? "+isValida);
		
		
		boolean result = false;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		logger.debug("Inicio proceso de confirmación de cotizacion...");
		
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		try {
			dao.setTrx(trx1);
		} catch (CotizacionesDAOException e){
			logger.error("Error al asignar transacción al dao Cotizaciones");
			throw new SystemException("Error al asignar transacción al dao Cotizaciones");
		}

		try{
			// Si la OP está validada sigue con el proceso de confirmacion
			if( isValida && !dao.getExisteAlertaActiva(id_cotizacion) ){
				result = dao.setModEstadoCotizacion(id_cotizacion, Constantes.ID_EST_COTIZACION_COTIZADA);
				LogsCotizacionesDTO log = new LogsCotizacionesDTO ();
				log.setCot_id(id_cotizacion);
				log.setUsuario(usr.getLogin());
				String mnsLog = "La Cotizacion se encuentra en estado : Cotizada";
				log.setDescripcion(mnsLog);
				dao.addLogCotizacion(log);
				
				//actualizar el indicador de modificacion de datos
				CotizacionesDTO cot = dao.getCotizacionById(id_cotizacion);
				EmpresasEntity emp = dao.getEmpresaById(cot.getCot_emp_id());
				if(emp.getMod_rzsoc().intValue()==Constantes.EMP_MOD_RAZON_SOCIAL){
					emp.setMod_rzsoc(new Integer(Constantes.EMP_NO_MOD_RAZON_SOCIAL));
					boolean act = dao.setModEmpresaById(emp);
					logger.debug("act empresa:"+act);
				}
			}else{
				
				throw new CotizacionesException(Constantes._EX_COT_TIENE_ALERTA_ACT);
			}
		}catch ( CotizacionesDAOException ex) {
			
			// rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new CotizacionesException(Constantes._EX_COT_ID_NO_EXISTE);
			}
			else
				throw new SystemException("Error no controlado",ex);
		} finally{
            //  cerramos trx
            try {
                trx1.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		
		return result;
	}

	/**
	 * Recupera el listado de categorías
	 * 
	 * @return Lista de categorías
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public List getCategoriasList() throws CotizacionesException, SystemException {
		List res = new ArrayList(); 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			
			List lista = (List)dao.getCategoriasList();
			
			CategoriaDTO cat1 = null;
			CategoriaDTO cat2 = null;
			List hijos = new ArrayList();
			
			// separa los padres de sus hijos
			for( int i = 0; i < lista.size(); i++ ) {
				cat1 = null;
				cat1 = (CategoriaDTO)lista.get(i);
				if (cat1.getTipo().equalsIgnoreCase("I")) {
					for( int f = 0; f < lista.size(); f++ ) {
						cat2 = null;
						cat2 = (CategoriaDTO)lista.get(f);
						if( cat2.getId_padre() == cat1.getId() ) {
							hijos.add(cat2);
						}
					}
					cat1.setCategorias(hijos);
					res.add(cat1);
				}
			}
			
		} catch (CotizacionesDAOException ex) {
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		} 
		return res;
	}
	
	/**
	 * Recupera información de la categoría.
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return DTO con datos de la categoría
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public CategoriaDTO getCategoriaById(long id_categoria) throws CotizacionesException, SystemException {

		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			
			return dao.getCategoriaById( id_categoria );
			
		} catch (CotizacionesDAOException ex) {
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		} 
		
	}
	
	/**
	 * Revisa la cotización si esta asignada a un ejecutivo<br>
	 * Si la Cotización no esta asignada :<br>
	 *  	- asigna la cotización al usuario que hace la edición<br>
	 *   	- anota en el log la asignacion<br>
	 *    	
	 * Si la Cotización ya está asignada :<br>
	 *     	- si ya está asignada despliega el mensaje en el monitor de Cotizaciones<br>
     * 	- "La Cotización XXXXX esta asignada al usuario YYYY"<br>
	 * 
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  dto AsignaCotizacionDTO 
	 * @return boolean, devuelve <i>true</i> en el caso que la asignación fue satisfactoria, caso contrario devuelve <i>false</i>. 
	 * @throws CotizacionesException, en el caso que no se cumplan ciertas condiciones:<br>
	 * - El usuario tiene cotización asignada, <br>
	 * - La cotización tiene otro usuario, <br>
	 * - No existe la cotización.
	 * @throws SystemException, en el caso que exista error de sistema.
	 * 
	 * */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO dto)	throws CotizacionesException, SystemException {
		
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		boolean result = false;
		
		
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		// Marcamos los dao's con la transacción		
		try {
			dao.setTrx(trx1);
		} catch (CotizacionesDAOException e2) {
			logger.error("Error al asignar transacción al dao Cotizaciones");
			throw new SystemException("Error al asignar transacción al dao Pedidos");
		}		
		
		
		try {
			//return dao.setAsignaOP(col);	
		     /*
		      * 
		      * setAsignaOP : metodo que asigna el usuario a la orden de pedido.
		      */

		     //Si el usuario ya tiene un pedido asignado no puede tomar otro
		     if ((dto.getId_cot_usr_act()>0)&&(dto.getId_cot_usr_act()!=dto.getId_cotizacion())){
		    	 trx1.rollback();
		    	 throw new CotizacionesException(Constantes._EX_COT_USR_TIENE_OTRA_COT);
		    	 //logger.debug("El usuario "+usr.getLogin()+" debe primero liberar la OP:"+usr.getId_pedido());
			     //url = paramUrlfracaso+"?mensaje_error="+mensaje_fracaso2+usr.getId_pedido();
		     }
			 //si el usuario es identico al que esta asignado, no hace nada, lo pasa
			 // si son distintos envia el mensaje de error
		     
		     long id_usuario_cot = dao.getCotizacionById(dto.getId_cotizacion()).getCot_id_usuario();
			 if ( (id_usuario_cot>0) && ( dto.getId_usuario() != id_usuario_cot) ){
				 trx1.rollback();
				 throw new CotizacionesException(Constantes._EX_COT_TIENE_OTRO_USR);
				 //logger.debug("La OP :"+paramId_op+" pertenece al usuario :"+paramId_usuario_ped);
				 //url = paramUrlfracaso+"?mensaje_error="+mensaje_fracaso+usr.getLogin(); 
			 }
			 else{
				 if ( id_usuario_cot<=0 ){
					 
					 //si no esta asignado lo asigna si ya esta asignado es a el mismo, salta a la url
					 result=dao.setAsignaCotizacion(dto);
					 if (result){
						 //usr.setId_pedido(paramId_op);
						 logger.debug("Se ha asignado la cotización "+dto.getId_cotizacion()+" al usuario:"+dto.getUsr_login());
						 //El log pasa por abrir un popup al usuario para que anote la observacion y el motivo
						 LogsCotizacionesDTO log = new LogsCotizacionesDTO();
						 log.setCot_id(dto.getId_cotizacion());
						 log.setUsuario(dto.getUsr_login());
						 log.setDescripcion(dto.getLog());
						 dao.addLogCotizacion(log);
						 //url= paramUrl+"&mod=1";
					 }
					 else{
						 logger.debug("No se ha logrado asignar la Cotización: "+dto.getId_cotizacion()+" al usuario:"+dto.getUsr_login());
						 //url = paramUrlfracaso+"?mensaje_error="+mensaje_fracaso+usr.getLogin();
					 }
				 }
				 else{
					 logger.debug("La Cotización ya esta asignada al usuario");
					 //url= paramUrl+"&mod=1";
				 }
			 }
		     
		     	

		} catch (CotizacionesDAOException ex) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new CotizacionesException(Constantes._EX_COT_ID_NO_EXISTE);
			}
			else
				throw new SystemException("Error no controlado ",ex);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
        } finally{
            //  cerramos trx
            try {
                trx1.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		return result;
	}

	
	
	/**
	 * Permite liberar una Cotización.<br>  
	 * Actualiza la informacion en base de datos, las tablas de ve_cotizacion y usuarios.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  col AsignaCotizacionDTO
	 * @return boolean, devuelve <i>true</i> en el caso que la liberación fue satisfactoria, caso contrario devuelve <i>false</i>. 
	 * @throws CotizacionesException, en el caso q exista error en la actualización en base de datos.
	 * @throws SystemException, en el caso que exista error de sistema.
	 * 
	 * */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col)
		throws CotizacionesException, SystemException {
		boolean result=false;
			
				
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		try {
			dao.setTrx(trx1);
		} catch (CotizacionesDAOException e2) {
			logger.error("Error al asignar transacción al dao Cotizaciones");
			throw new SystemException("Error al asignar transacción al dao Cotizaciones");
		}	
		
		try {
			result = dao.setLiberaCotizacion(col);	
		} catch (CotizacionesDAOException e) {
			//				rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new CotizacionesException(e);	
        } finally{
            //  cerramos trx
            try {
                trx1.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		return result;

	}

	/**
	 * Recupera todas las marcas diferentes de los productos para una categoría.  
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return Listado de marcas
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public List getListMarcasByCategoria(long id_categoria) throws CotizacionesException, SystemException {

		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			
			return dao.getListMarcasByCategoria( id_categoria );
			
		} catch (CotizacionesDAOException ex) {
			logger.warn("Problema :" + ex);
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			throw new SystemException(e);
		} 
		
	}
	
	
	/**
	 * retorna la suma de las cantidades de los productos asociados a una cotización
	 * @param cot_id
	 * @return double
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public double getTotalProductosCot( long cot_id) throws CotizacionesException , SystemException{
		double res=0;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			res = (double) dao.getTotalProductosCot(cot_id);			
			} catch (CotizacionesDAOException ex) {
				logger.debug("Problema :"+ex.getMessage());
				throw new CotizacionesException(ex);			
			}
			catch (Exception e) {
				throw new SystemException(e);
			}
		return res;
	}

	/**
	 * Recupera los productos para una categoría.
	 * 
	 * Debe considerar los criterios de búsquedas.
	 * 
	 * @param criterio DTO con información de los diferetentes datos para filtrar y ordenar el listado de productos
	 * @return Listado de productos
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public List getListProductosByCriteria(ProductosCriteriaDTO criterio) throws CotizacionesException , SystemException{

		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			List lista = dao.getListProductosByCriteria(criterio);
			
			// Recorrer los productos genéricos para obtener sus item
			for( int i = 0; i < lista.size(); i++ ) {
				ProductoDTO producto_aux = (ProductoDTO)lista.get(i); // Recuperar producto desde la lista
				// Si es producto genérico se deben obtener sus items
				if( producto_aux.getGenerico().compareTo("G") == 0 ) {
					List lista_item = new ArrayList();
					lista_item = this.getListItems( criterio.getId_local()+"", producto_aux.getPro_id(), criterio.getId_cotizacion() );
					if( lista_item.size() > 0 ){
						producto_aux.setProductosDTO(lista_item);
					}
				} // if Generico
			} // for
			
			return lista;
			
			} catch (CotizacionesDAOException ex) {
				logger.debug("Problema :"+ex.getMessage());
				throw new CotizacionesException(ex);			
			}
			catch (Exception e) {
				throw new SystemException(e);
			}
	}
	
	/** Recupera los itemes de los productos
	 * @param local_id: Identificador de local
	 * @param pro_padre : Identificador del producto padre
	 * @param coti_id : Identificador de la cotizacion
	 * @return
	 * @throws CotizacionesException
	 */
	public List getListItems(String local_id, long pro_padre, long coti_id) throws CotizacionesException{
	
		List result = new ArrayList();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try {
			result = dao.getListItems(local_id, pro_padre, coti_id);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		return result;
	}
	
	/**
	 * Permite modificar datos(cantidad) de un producto asociado a una cotizacion
	 * @param prod
	 * @return
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean updCantProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesException, SystemException {
		boolean res = false; 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try {
			res = dao.updCantProductoCotizacion(prod);
			
		}  catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe id detalle cotización");
				throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else
				throw new SystemException("Error no controlado",ex);
		} 
		return res;
	}	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesException
	 */
	public List getProductosDetCotiz(long id_cot)	throws CotizacionesException{
	
		List result = new ArrayList();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try {
			result = dao.getProductosDetCotiz(id_cot);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		return result;
	}
	
	/**
	 * Permite agregar al encabezado (estado, observacion) y al detalle de la cotizacion (descripcion, precio unitario, cantidad)
	 * @param id_cot, comentario
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean SendCotizacion( CotizacionesDTO cotizacion )  throws CotizacionesException, SystemException{
		boolean resDet = false;
		boolean resultEsta = false;
		boolean resultCom = false;
		int id_estado = Constantes.ID_EST_COTIZACION_EN_REVISION;
		JdbcTransaccion trx = new JdbcTransaccion();
		
		long id_cot = cotizacion.getCot_id();
		String comentario = cotizacion.getCot_obs();
		String fuera_mix = cotizacion.getCot_fueramix();
		
		try {
			List resultDet = new ArrayList(); 
			JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();

			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);
			
		
			boolean flag_rollback = false;
			
			resultCom = dao.setModComentarioCotizacion(id_cot, comentario, fuera_mix);
			if(resultCom == false)
				flag_rollback = true;
			
			
			resultEsta = dao.setModEstadoCotizacion(id_cot, id_estado);
			if(resultEsta == false)
				flag_rollback = true;

			
			resultDet = dao.getProductosDetCotiz(id_cot);
			
			// Se itera por los productos y modifica
			for(int i=0; i<resultDet.size(); i++){
				ProductosCotizacionesDTO det = (ProductosCotizacionesDTO)resultDet.get(i);

				//det.setDetcot_desc(det.getDetcot_desc());
				//det.setPro_tipre(det.getPro_tipre());
				//det.setDetcot_precio(det.getDetcot_precio());
				det.setDetcot_precio_lista(det.getDetcot_precio());
				det.setDetcot_cot_id(id_cot);
				//det.setDetcot_proId(det.getDetcot_proId());

				if( det.getDetcot_precio_lista() <= 0 ) {
					resDet = dao.delProductoCotizacion( det.getDetcot_id() );
					if(resDet == false)
						flag_rollback = true;
					logger.debug("resDet : "+resDet);					
				} else {
					resDet = dao.updProductoCotizacion(det);
					if(resDet == false)
						flag_rollback = true;
					logger.debug("resDet : "+resDet);
				}
				
			}
			if( flag_rollback == true )
				throw new CotizacionesDAOException( "Error al insertar." );	

			//Finaliza la transacción
		
		}catch(CotizacionesDAOException ex){
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
        } finally{
            //  cerramos trx
            try {
                trx.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }	
		return resDet;
	}	
	
	/**
	 * Recupera categorías y marcas para la búsqueda
	 *  
	 * @param patron		Patrón de búsqueda
	 * @param local_id		Identificador único del local
	 * @param criterio		criterio de busqueda
	 * @return				Lista de DTO con datos de las categorías
	 * @throws SystemException
	 */	
	public List getSearch( List patron, long local_id, long criterio )	throws CotizacionesException{
		
		List result = new ArrayList();
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		try {
			result = dao.getSearch(patron, local_id, criterio);
		} catch (CotizacionesDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new CotizacionesException(e);	
		}		
		return result;
	}

	/**
	 * Cambia a estado ACEPTADA la cotizacion y graba la informacion del resumen
	 * @param dto
	 * @return True: éxito False: error
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setAceptarCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesException, SystemException {
		boolean res = false; 
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		JdbcTransaccion trx = new JdbcTransaccion();
		
		try {
			
			if ( dto.getNumero_tarjeta()==null )		dto.setNumero_tarjeta(Constantes.CADENA_VACIA);
			if ( dto.getObs()==null )			dto.setObs(Constantes.CADENA_VACIA);
			if ( dto.getTbk_nombre_tarjeta()==null )		dto.setTbk_nombre_tarjeta(Constantes.CADENA_VACIA);
			if ( dto.getSustitucion()==null )	dto.setSustitucion(Constantes.CADENA_VACIA);
			if ( dto.getSgente_txt()==null )	dto.setSgente_txt(Constantes.CADENA_VACIA);
			if ( dto.getNombre_banco()==null )	dto.setNombre_banco(Constantes.CADENA_VACIA);
			if ( dto.getTbk_fec_expira()==null )	dto.setTbk_fec_expira(Constantes.CADENA_VACIA);
			
			// Iniciamos la transacción
			trx.begin();

			// Asignamos la transacción a los dao's
			dao.setTrx(trx);			
			
			boolean flag_rollback = false;			

			int id_estado = Constantes.ID_EST_COTIZACION_ACEPTADA;	//estado aceptada
			res = dao.setModEstadoCotizacion(dto.getCot_id(), id_estado);
			if(res == false)
				flag_rollback = true;
			
			if( flag_rollback == true )
				throw new CotizacionesDAOException( "Error al aceptar la cotización." );	
			
			res = dao.updCotizacion(dto);
			if(res == false)
				flag_rollback = true;
			
			if( flag_rollback == true )
				throw new CotizacionesDAOException( "Error al grabar la cotización." );	

					
			
		}  catch (CotizacionesDAOException ex) {
			logger.debug("Problema :" + ex);
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException("Error no controlado",ex);
		} catch (Exception e) {
			try { trx.rollback(); } catch (DAOException e1) { e1.printStackTrace (); }
			throw new SystemException(e);
        } finally{
            //  cerramos trx
            try {
                trx.end();
            } catch (DAOException e) {
                logger.error("Error al finalizar transacción");
                throw new SystemException("Error al finalizar transacción");
            }
        
        }
		
		return res;
	}

	/**
	 * Permite modificar el estado de las cotizaciones
	 * @param id_cot
	 * @param id_estado
	 * @return
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado) throws CotizacionesException, SystemException {
		boolean result = false;
		JdbcCotizacionesDAO dao = (JdbcCotizacionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCotizacionesDAO();
		
		try{
			result=dao.setModEstadoCotizacion(id_cot,id_estado);
		}catch(CotizacionesDAOException ex){
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new CotizacionesException(Constantes._EX_REG_ID_NO_EXISTE);
			}
			else
				throw new SystemException("Error no controlado al insertar log cotizacion",ex);
		}		
		return result;
		
	}	

}
