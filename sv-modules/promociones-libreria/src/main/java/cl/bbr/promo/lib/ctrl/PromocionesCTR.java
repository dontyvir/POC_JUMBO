package cl.bbr.promo.lib.ctrl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.dao.DAOFactory;
import cl.bbr.promo.lib.dao.PromocionesDAO;
import cl.bbr.promo.lib.dto.ClienteKccDTO;
import cl.bbr.promo.lib.dto.ClientePRDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;
import cl.bbr.promo.lib.dto.MedioPagoNormalizadoDTO;
import cl.bbr.promo.lib.dto.PrioridadPromosDTO;
import cl.bbr.promo.lib.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionCriterio;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionSeccionDTO;
import cl.bbr.promo.lib.dto.TcpClienteDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.bbr.promo.lib.exception.PromocionesDAOException;
import cl.bbr.promo.lib.exception.PromocionesException;


/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */

public class PromocionesCTR {

	/**
	 * Instancia para el tipo de factoria
	 */
	private int tipo_factory = DAOFactory.JDBC;
	
	/**
	 * Instancia para log4j
	 */
	private Logging logger = new Logging(this);
	
	/**
	 * Conexión a la base de datos
	 */
	private Connection conexion = null;

	/**
	 * Constructor
	 *
	 */
	public PromocionesCTR() {
		this.logger.debug("New PromocionesCTR");
	}
	
	/**
	 * Constructor
	 *
	 */
	public PromocionesCTR( int tipo ) {
		this.logger.debug("New PromocionesCTR tipo:"+tipo);
		this.tipo_factory = tipo;
			
	}
	
	private void closeConexion() throws PromocionesException {
		try {
			if (conexion != null && !conexion.isClosed())
				conexion.close();
		} catch (SQLException e) {
			this.logger.error("closeConection", e);
			throw new PromocionesException( "closeConection", e);			
		}
	}

	/**
	 * Recupera la lista de promociones para un producto.
	 * 
	 * @param	id_producto	Identificador único del producto
	 * @param	id_local	Identificador único del local
	 * @param	lista_tcp	Lista de TCP del cliente
	 * @return	Lista de DTO
	 * @throws PromocionesException
	 */	
	public List getPromocionesByProductoId(long idProductoFO, long idProductoBO, String id_local, List lista_tcp ) throws PromocionesException {

		List result = new ArrayList();

		try {
			
			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/

			PromocionCriterio criterio = new PromocionCriterio();
			criterio.setId_producto_fo(idProductoFO);
			criterio.setId_local(Long.parseLong(id_local));
			criterio.setLista_tcp(lista_tcp);
			
            if (idProductoBO == 0)
            	criterio.setId_producto_bo(PromocionesDAO.getIdBoProductoId( criterio ));
            else
                criterio.setId_producto_bo(idProductoBO);    
			// Recuperar el id_bo del producto
			//	(J)no tiene sentido ir a buscar cada pro_id_bo cuando se pueden obtener antes
			//criterio.setId_producto_bo(PromocionesDAO.getIdBoProductoId( criterio ));
			// se corrige así
			
			result = (List) PromocionesDAO.getPromocionesByProductoId( criterio );


		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromocionesByProductoId)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromocionesByProductoId)", ex);
			throw new PromocionesException(ex);
		}

		return result;
	}

	/**
	 * Recuperar promociones por TCP
	 * 
	 * @param tcp	Listado de TCP
	 * @return		Listado de promociones
	 * @throws PromocionesException
	 */
	public List getPromocionesByTCP(List tcp) throws PromocionesException {
		List result = new ArrayList();

		try {
			
			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/

			result = (List) PromocionesDAO.getPromocionesByTCP( tcp );


		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromocionesByTCP)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromocionesByTCP)", ex);
			throw new PromocionesException(ex);
		}

		return result;
	}

	/**
	 * Recuperar promociones por TCP
	 * 
	 * @param tcp	    Listado de TCP
	 * @param id_local	Identificador del Local
	 * @return		    Listado de promociones
	 * @throws PromocionesException
	 */
	public List getPromocionesByTCP(List tcp, String id_local) throws PromocionesException {
		List result = new ArrayList();

		try {
			
			PromocionesDAO PromocionesDAO =  (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/

			result = (List) PromocionesDAO.getPromocionesByTCP(tcp, id_local );


		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromocionesByTCP)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromocionesByTCP)", ex);
			throw new PromocionesException(ex);
		}

		return result;
	}
	
	/**
	 * Recalcula las promociones para un pedido en un local.
	 * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
	 * @param recalculo
	 * @param cddto
	 * @param cuponProds
	 * @return
	 * @throws PromocionesException
	 */
	public doRecalculoResultado doRecalculoPromocion( doRecalculoCriterio recalculo, CuponDsctoDTO cddto, List cuponProds) throws PromocionesException {
        //logger.debug("********** doRecalculoPromocion ***************");
		int id_local   = recalculo.getId_local();
		String fpago   = recalculo.getF_pago();
		int cuotas     = recalculo.getCuotas();
		//[20121113avc
		Long rutColaborador = recalculo.getRutColaborador();
		//]20121113avc
		
		PromocionesDAO dao = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
		/*if( this.tipo_factory == DAOFactory.JDBC )
			dao = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
		else
			dao = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
		
		doRecalculoResultado result = new doRecalculoResultado();

		//OJO hay que definir el canal en las constantes
		int id_canal = 1;
		char flag_cant = '-'; //Flag producto cantidad 'C', o producto 'P' Pesable		
		char flag_venta = 'V'; // Venta o anulacion
		
		try {
			//logger.debug("PROMO --> INI doRecalculoPromocion");
			
			//	se inicializa ambiente de libreria promociones
			//[20121113avc
			PromoCtrl lib = new PromoCtrl(id_local,id_canal, rutColaborador);
			//]20121113avc
			
			//	se setea el listado de tcp (al menos 1 debe ser seteado)
			List TCP = new ArrayList();
			if( recalculo.getGrupos_tcp() == null || recalculo.getGrupos_tcp().size() == 0 ) {
				//logger.debug("PROMO --> TCP no existen");
				TcpClienteDTO tcpdto;
				tcpdto = new TcpClienteDTO(0,"",2000,0);
				TCP.add(tcpdto);
			} else {
				//logger.debug("PROMO --> TCP existen: " + recalculo.getGrupos_tcp().size());
				for( int t = 0; t < recalculo.getGrupos_tcp().size(); t++ ) {
					FOTcpDTO tcp = (FOTcpDTO)recalculo.getGrupos_tcp().get(t);
					TcpClienteDTO tcpdto;
					//logger.debug("PROMO --> TCP add:"+tcp.toString());
					tcpdto = new TcpClienteDTO(tcp.getTcp_nro(),tcp.getCupon(),tcp.getTcp_max(),0);
					TCP.add(tcpdto);
				}
			}
			
			//recoge listado de productos a calcular
			List lst_detped = recalculo.getProductos();
			/*if( lst_detped == null || lst_detped.size() == 0 ) {
				throw new PromocionesException("No hay productos para calcular promociones");
			    //logger.debug("No hay existen productos para calcular promociones");
			}*/
				
			//logger.debug("PROMO --> Productos del carro de compras:"+lst_detped.size());
			
			for (int i=0; i<lst_detped.size(); i++){
				ProductoPromosDTO prod1 = (ProductoPromosDTO) lst_detped.get(i);
				
				PrioridadPromosDTO codigos_promo = (PrioridadPromosDTO)dao.getPromosPrioridadProducto(prod1.getId_producto(), id_local);

				//	genera un nuevo reigstro para el calculo
				ProductoDTO pro= new ProductoDTO();
				
				//busca las promociones asociadas por prioridad promocional a cada producto
				List promo = new ArrayList();
				
				//si no hay promociones o son todas 0 sigue el ciclo ya que no hay promociones a calcular
				/*
				if ((codigos_promo==null) || ((codigos_promo.getCodPromoEvento()+codigos_promo.getCodPromoNormal()+codigos_promo.getCodPromoPeriodica())==0)){
					this.logger.debug("PROMO --> "+i+".-producto NO tiene promociones");	
					continue;
				}
				*/
				//this.logger.debug("PROMO --> "+i+".-producto SI tiene promociones " + codigos_promo.getCodPromoEvento()+","+codigos_promo.getCodPromoPeriodica()+","+codigos_promo.getCodPromoNormal());
				
				//si existen promociones las adjunta al registro de productos				
				if (codigos_promo.getCodPromoEvento()>0){
					promo.add(String.valueOf(codigos_promo.getCodPromoEvento()));	
					//this.logger.debug("PROMO --> Evento:"+codigos_promo.getCodPromoEvento());
				}
				if (codigos_promo.getCodPromoPeriodica()>0){
					promo.add(String.valueOf(codigos_promo.getCodPromoPeriodica()));
					//this.logger.debug("PROMO --> Periodica:"+codigos_promo.getCodPromoPeriodica());
					}
				if (codigos_promo.getCodPromoNormal()>0){
					promo.add(String.valueOf(codigos_promo.getCodPromoNormal()));
					//this.logger.debug("PROMO --> Normal:"+codigos_promo.getCodPromoNormal());
				}
				
				//ingresa promociones del producto
				//logger.debug("total promos:"+promo.size());				
				pro.setPromocion(promo);			
				
				//codigo de barra
				long ean13long = Long.parseLong(prod1.getCod_barra());
				pro.setCodigo(ean13long);
				
// inicio cdd
				pro.setIdProducto(prod1.getId_producto());
				pro.setRubro(prod1.getRubro());
// fin cdd
				
				// Que va en departamento la seccion de la categoria SAP
				pro.setDepto(Integer.parseInt(prod1.getSeccion_sap()));				

				// La cantidad va multiplicada por 1000 si es pesable
				int cant_solicitada=0;
				
				// flag	pesable 'P'=Pesable o 'C'=Cantidad 
				if (prod1.getPesable().equals("P")){					
					cant_solicitada =(int)Math.round(prod1.getCant_solicitada()*1000);
					flag_cant = 'P';
				}
				else{
					//Inicio (17-12-2014): Contingencia_gramaje NSepulveda
					double menorPesable = 0.5;
					if(prod1.getCant_solicitada() < menorPesable){
						cant_solicitada = 1;
					}else{
						cant_solicitada = (int)Math.round(prod1.getCant_solicitada());
					}
					
					flag_cant = 'C';
					//Fin (17-12-2014): Contingencia_gramaje NSepulveda
					
					//Original:
					/*cant_solicitada = (int)Math.round(prod1.getCant_solicitada());
					flag_cant = 'C';*/
				}
				//logger.debug("prod1.getCant_solicitada() -> :  " +cant_solicitada);
				pro.setCantidad(cant_solicitada);
				
				//Flag producto cantidad = C o pesable = P
                //logger.debug("pro.setFlagCantidad() -> :  " +flag_cant);
				pro.setFlagCantidad(flag_cant);
				
				//Flag Venta=V Anulacion=A
                //logger.debug("pro.setFlagVenta() -> :  " +flag_venta);
				pro.setFlagVenta(flag_venta);
				
				//Precio del Producto
				pro.setPrecio(Math.round(prod1.getPrecio_lista()*prod1.getCant_solicitada()));
                //logger.debug("pro.getPrecio() -> :  " +pro.getPrecio());
								
				/*this.logger.debug("PROMO --> inserta producto :  " +prod1.getId_producto()+
						" barra="+pro.getCodigo()+ 
						" depto="+pro.getDepto()+
						" cantidad="+ pro.getCantidad()+
						" flag_cantidad="+pro.getFlagCantidad()+
						" flag_venta="+ pro.getFlagVenta()+
						" precio_lista="+pro.getPrecio()+
						" promo="+pro.getPromocion());*/
					
				//se insertan productos a la libreria			
				lib.insertaProducto(pro);
			}
			//this.logger.debug("PROMO --> FIN productos insertados");
			
			//	se calcula promociones
			
			//fpago hay que traducirlos a codificacion POS usar una tabla de normalizacion de medios de pago
			
			//this.logger.debug("PROMO --> INI calculo descuentos fpago="+fpago+" cuotas="+cuotas);	
			// se busca la forma de pago correspondiente => normalizar


// inicio cdd
			result.setDescuento_pedido(lib.calculaDescuentosNew(fpago,cuotas,TCP, cddto, cuponProds));
// fin cdd
			
			//this.logger.debug("PROMO --> FIN calculo descuentos");
			
			// se obtiene el listado de productos prorrateados
			List p_prod = lib.getProrrateoProducto();
            //this.logger.debug("lib.getProrrateoProducto() ->" + p_prod.size());
            
			// se obtiene el listaod de secciones prorrateadas
			List p_sec = lib.getProrrateoSeccion();
            //this.logger.debug("lib.getProrrateoSeccion() ->" + p_sec.size());
			
			//this.logger.debug("PROMO --> TOTAL DESCUENTO = "+result.getDescuento_pedido());
			
			//logger.debug("PROMO --> RESULTADO PRODUCTOS="+p_prod.size());
			List l_promociones = new ArrayList();
			for ( int i=0; i<p_prod.size(); i++ ) {
				ProrrateoPromocionProductoDTO promoproducto = (ProrrateoPromocionProductoDTO)p_prod.get(i);
				
				/*String linea_debug ="PROMO --> PROMO="+promoproducto.getCodigo()
					+"\t"+promoproducto.getDescripcion()
					+"\tdcto="+promoproducto.getDescuento()+"  ";
				this.logger.debug(linea_debug);	*/			
				
				List lst_prod_prorrat = promoproducto.getListadoProductos();
				
				// Inicio Requerimiento Jlazo 
				/*
				 String descripcion_Producto = "";
				 List listaCodPromo = new ArrayList();
				
				for ( int p = 0; p < lst_prod_prorrat.size() ; p++ ) {

					ProrrateoProductoDTO pr = ( ProrrateoProductoDTO )lst_prod_prorrat.get( p );
					listaCodPromo.add( pr );
				
				}
				
				double promoDescuento = 0;
				long codigoAux = 0;
				
				for ( int w = 0; w < lst_prod_prorrat.size(); w++ ) {
					
					ProrrateoProductoDTO p = ( ProrrateoProductoDTO )lst_prod_prorrat.get( w );
					this.logger.debug("PROMO --> ean13:"+p.getCodigo()+"-precio:"+p.getPrecio()+"-prorrateo:"+p.getProrrateo());
					
					descripcion_Producto = getProductoDescripcion( p.getCodigo() );
					
					if ( codigoAux != p.getCodigo() ) {
					
						if ( lst_prod_prorrat.size() > 1 ) {
						
							if ( promoproducto.getCodigo() == -1 || promoproducto.getCodigo() == -2 ) {
						
								PromocionDTO promocion = new PromocionDTO();
								
								promocion.setDescr( descripcion_Producto.toLowerCase() +"<br>"+ promoproducto.getDescripcion() );
								promocion.setDescuento1( p.getProrrateo() );
								promocion.setCod_promo( promoproducto.getCodigo() );
								promocion.setCantProdPromo( promoproducto.getListadoProductos().size() );
								
								l_promociones.add( promocion );
						
							}
							else {
														
								for (int j = 0; j < listaCodPromo.size(); j++) {
								
									ProrrateoProductoDTO pr = ( ProrrateoProductoDTO )lst_prod_prorrat.get( j );
								
									if ( p.getCodigo() == pr.getCodigo() ) {
									
										promoDescuento += pr.getProrrateo();
										codigoAux = pr.getCodigo();
								
									}
							
								}
							
								PromocionDTO promocion = new PromocionDTO();
								
								promocion.setDescr( descripcion_Producto.toLowerCase() +"<br>"+ promoproducto.getDescripcion() );
								promocion.setDescuento1( promoDescuento );
								promocion.setCod_promo( promoproducto.getCodigo() );
								promocion.setCantProdPromo( promoproducto.getListadoProductos().size() );
								
								l_promociones.add( promocion );
							
								promoDescuento = 0;
						
							}
						
						}
						else {
						
							PromocionDTO promocion = new PromocionDTO();
							
							promocion.setDescr( descripcion_Producto.toLowerCase() +"<br>"+ promoproducto.getDescripcion() );
							promocion.setDescuento1( promoproducto.getDescuento() );
							promocion.setCod_promo( promoproducto.getCodigo() );
							promocion.setCantProdPromo( promoproducto.getListadoProductos().size() );
						
							l_promociones.add( promocion );
						
							break;
					
						}
					
					}
				
				}*/
				
				// Fin Requerimiento Jlazo
				
				// Inicio, se agrega codigo para volver a mostrar el "ver detalle" de promociones actualmente.
				
				for ( int w = 0; w < lst_prod_prorrat.size(); w++ ) {
					
					ProrrateoProductoDTO p = ( ProrrateoProductoDTO )lst_prod_prorrat.get( w );
					//this.logger.debug("PROMO --> ean13:"+p.getCodigo()+"-precio:"+p.getPrecio()+"-prorrateo:"+p.getProrrateo());
				
				}
				
				
				PromocionDTO promocion = new PromocionDTO();
				
				promocion.setDescr( promoproducto.getDescripcion() );
				promocion.setDescuento1( promoproducto.getDescuento() );
				promocion.setCod_promo( promoproducto.getCodigo() );
				promocion.setCantProdPromo( promoproducto.getListadoProductos().size() );
			
				l_promociones.add( promocion );
				
				// Fin, se agrega codigo para volver a mostrar el "ver detalle" de promociones actualmente.
			
			}
			//this.logger.debug("PROMO --> RESULTADO SECCIONES="+p_sec.size());
			for ( int i=0; i<p_sec.size(); i++ ) {
				ProrrateoPromocionSeccionDTO promosecc = (ProrrateoPromocionSeccionDTO)p_sec.get(i);
				/*String linea_debug ="PROMO --> PROMO="+promosecc.getCodigo()
					+"|"+promosecc.getDescripcion()
					+"|dcto="+promosecc.getDescuento()+"  ";*/
				
				PromocionDTO promocion = new PromocionDTO();
				promocion.setDescr(promosecc.getDescripcion());
				promocion.setDescuento1(promosecc.getDescuento());
				promocion.setCod_promo(promosecc.getCodigo());
				
				l_promociones.add(promocion);
				
				//this.logger.debug(linea_debug);
				
			}
			//this.logger.debug("PROMO --> FIN debug resultados");			
			
			result.setPromociones(l_promociones);
			
			return result;
			
		} catch (PromocionesDAOException ex) {	
			logger.error("No puede obtener datos de promociones :" + ex);
			//ex.printStackTrace();
			throw new PromocionesException("Error en JdbcPromocionesDAO",ex);
			
		} catch (Exception e) {
			logger.error("Error en libreria de promociones:"+e.getMessage());
			e.printStackTrace();
			throw new PromocionesException(e);
		}	
	}
	
	/**
	 * 
	 * Recupera el medio de pago normalizado
	 * 
	 * @param mp	Medio de pago
	 * @return		Medio de pago
	 * @throws PromocionesException
	 */
	public MedioPagoNormalizadoDTO getMedioPAgoNormalizado(MedioPagoNormalizadoDTO mp) throws PromocionesException {
		try {
			
			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
		
			// Recuperar el id_bo del producto
			return(PromocionesDAO.getMedioPAgoNormalizado( mp ));


		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getMedioPAgoNormalizado)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getMedioPAgoNormalizado)", ex);
			throw new PromocionesException(ex);
		}

	}

	/**
	 * Recupera el id de local POS
	 * 
	 * @param id_local
	 * @return
	 */
	public int getCodigoLocalPos(long id_local) throws PromocionesException {
		try {
			
			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
		
			// Recuperar el id_bo del producto
			return(PromocionesDAO.getCodigoLocalPos( id_local ));


		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getMedioPAgoNormalizado)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getMedioPAgoNormalizado)", ex);
			throw new PromocionesException(ex);
		}
	}

    /**
     * @param listaProductos
     * @param string
     * @param lista_tcp
     * @return
     * @throws PromocionesException
     */
    public Hashtable getPromociones(String listaIdBo, int idLocal, List lista_tcp) throws PromocionesException {
		try {
			PromocionesDAO promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			//if( this.tipo_factory == DAOFactory.JDBC )
				//promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			//else
				//promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();
			
			
			return promocionesDAO.getPromociones( listaIdBo, idLocal, lista_tcp);
			
			
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromociones)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromociones)", ex);
			throw new PromocionesException(ex);
		}
	}
    
    /**
     * @param listaProductos
     * @param string
     * @param lista_tcp
     * @return
     * @throws PromocionesException
     */
    public Hashtable getPromocionesBanner(String listaIdProd, int idLocal, List lista_tcp) throws PromocionesException {
		try {
			PromocionesDAO promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			//if( this.tipo_factory == DAOFactory.JDBC )
				//promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			//else
				//promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();
			
			
			return promocionesDAO.getPromocionesBanner( listaIdProd, idLocal, lista_tcp);
			
			
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromocionesBanner)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromocionesBanner)", ex);
			throw new PromocionesException(ex);
		}
	}
    
    
    // FORMULARIO KCC
       /**
        * 
        * @param dataClienteKcc
        * @return
        * @throws PromocionesException
        */
   	public boolean addDataClienteKcc(ClienteKccDTO dataClienteKcc) throws PromocionesException {
   		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();;
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.addDataClienteKcc( dataClienteKcc ));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (addDataClienteKcc)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (addDataClienteKcc)", ex);
   			throw new PromocionesException(ex);
   		}

   	}
   	
 	public boolean addDataClientePR(ClientePRDTO dataClienteKcc) throws PromocionesException {
   		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();;
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.addDataClientePR( dataClienteKcc ));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (addDataClienteKcc)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (addDataClienteKcc)", ex);
   			throw new PromocionesException(ex);
   		}

   	}
	public boolean getClientePRByRut(String rut, String dv) throws PromocionesException {
   		try {
   			
   			PromocionesDAO PromocionesDAO =  (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getClientePRByRut( rut, dv ));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}

   	}
	
	public String getProductoDescripcion(long cod_barra) throws PromocionesException {
		try {
			PromocionesDAO promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			/*if( this.tipo_factory == DAOFactory.JDBC )
				promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
			else
				promocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
			
			
			return promocionesDAO.getProductoDescripcion( cod_barra);
			
			
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getPromociones)" );
			throw new PromocionesException(ex);
		} catch (PromocionesDAOException ex) {
			logger.error("Problema (getPromociones)", ex);
			throw new PromocionesException(ex);
		}
	}
	
   	/**
   	 * getClienteKccByRut
   	 * @param rut
   	 * @param dv
   	 * @return
   	 * @throws PromocionesException
   	 */
   	public boolean getClienteKccByRut(String rut, String dv) throws PromocionesException {
   		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getClienteKccByRut( rut, dv ));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}

   	}
    
   	/**
   	 * 
   	 * @param cliente
   	 * @return
   	 * @throws PromocionesException
   	 */
	public ClienteSG6DTO getClienteByRut(ClienteSG6DTO cliente) throws PromocionesException {
		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getClienteByRut(cliente));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}
	}
	/**
	 * 
	 * @param llave
	 * @return
	 * @throws PromocionesException
	 */
	public ArrayList getModelosSamsung(String llave)throws PromocionesException{
		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getModelosSamsung(llave));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}
	}

	public int getReservasSamsungCliente(ClienteSG6DTO cliente)throws PromocionesException{
		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getReservasSamsungCliente(cliente));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}
	}
	
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws PromocionesException
	 */
	public boolean registrarReservaSamsung(ClienteSG6DTO cliente) throws PromocionesException{
		try {
   			
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();;
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.registrarReservaSamsung(cliente));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}
	}

	public ClienteSG6DTO getDireccionCliente(ClienteSG6DTO cliente) throws PromocionesException{
		try {	
   			PromocionesDAO PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			/*if( this.tipo_factory == DAOFactory.JDBC )
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromocionesDAO();
   			else
   				PromocionesDAO = (PromocionesDAO) DAOFactory.getDAOFactory(DAOFactory.TEST).getPromocionesDAO();*/
   		
   			return(PromocionesDAO.getDireccionCliente(cliente));

   		} catch ( NullPointerException ex ) {
   			logger.error("Problema con null en los datos (getClienteKccByRut)" );
   			throw new PromocionesException(ex);
   		} catch (PromocionesDAOException ex) {
   			logger.error("Problema (getClienteKccByRut)", ex);
   			throw new PromocionesException(ex);
   		}
	}
    
}