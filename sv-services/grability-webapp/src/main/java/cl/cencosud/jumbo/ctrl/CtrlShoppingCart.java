package cl.cencosud.jumbo.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.bizdelegate.BizDelegatePurchaseOrder;
import cl.cencosud.jumbo.bizdelegate.BizDelegateShoppingCart;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.ShoppingCart.DeleteInputShoppingCartDTO;
import cl.cencosud.jumbo.input.dto.ShoppingCart.GetInputShoppingCartDTO;
import cl.cencosud.jumbo.input.dto.ShoppingCart.PutInputShoppingCartDTO;
import cl.cencosud.jumbo.output.dto.ShoppingCart.DeleteOutputShoppingCartDTO;
import cl.cencosud.jumbo.output.dto.ShoppingCart.GetOutputShoppingCartDTO;
import cl.cencosud.jumbo.output.dto.ShoppingCart.PutOutputShoppingCartDTO;
import cl.cencosud.jumbo.util.Util;

public class CtrlShoppingCart extends Ctrl {
	
	private GetInputShoppingCartDTO oGetShoppingCartDTO;
	private PutInputShoppingCartDTO oPutShoppingCartDTO;
	private DeleteInputShoppingCartDTO oDeleteShoppingCartDTO;
	
	/**
	* Constructor para asignar el objeto del tipo GetInputClientDTO.
	*/	
	public CtrlShoppingCart(GetInputShoppingCartDTO oGetShoppingCartDTO) {
		super();
		this.oGetShoppingCartDTO = oGetShoppingCartDTO;
	} 
	
	/**
	* Constructor para asignar el objeto del tipo GetInputClientDTO.
	*/	
	public CtrlShoppingCart(PutInputShoppingCartDTO putShoppingCartDTO) {
		super();
		this.oPutShoppingCartDTO = putShoppingCartDTO;
	} 
	
	/**
	* Constructor para asignar el objeto del tipo GetInputClientDTO.
	*/	
	public CtrlShoppingCart(DeleteInputShoppingCartDTO deleteShoppingCartDTO) {
		super();
		this.oDeleteShoppingCartDTO = deleteShoppingCartDTO;
	} 
	
	/**
	 * Obtiene carro de compras de un cliente registrado o invitado genera un objeto de salida del tipo GetOutputShoppingCartDTO. 
	 * <p>
	 * Este metodo recupera el carro de al cliente  a traves del objeto oGetShoppingCartDTO,
	 * setea el objeto de salida outputDTO con los datos del carro de compras.
	 * Retorna un objeto del tipo GetOutputShoppingCartDTO. 
	 *
	 * @return  GetOutputShoppingCartDTO, objeto de salida que implementa el metodo toJson.
	 * @see     GetOutputShoppingCartDTO
	 * @throws	GrabilityException
	 */
	public GetOutputShoppingCartDTO getShoppingCart() throws GrabilityException{
		
		GetOutputShoppingCartDTO outputDTO = new GetOutputShoppingCartDTO();
		BizDelegateShoppingCart bizShoppingCart = new BizDelegateShoppingCart();


		long idCliente = 0;
		String idSession = "";
		boolean isInvitado = false;
		
		if(StringUtils.equals(oGetShoppingCartDTO.getGuest_user(), "0")){
			idCliente = (long)oGetShoppingCartDTO.getClient_id();
			idSession = "1";
			isInvitado = false;	
		}else if(StringUtils.equals(oGetShoppingCartDTO.getGuest_user(), "1")){
			idCliente = 1;
			idSession = String.valueOf(oGetShoppingCartDTO.getClient_id());
			isInvitado = true;			
		}
				
		//List listaCarro = bizShoppingCart.carroComprasGetProductos(1, String.valueOf(getShoppingCartDTO.getLocal_id()), String.valueOf(getShoppingCartDTO.getClient_id()));
		List listaCarro = bizShoppingCart.carroComprasPorCategorias(idCliente, String.valueOf(oGetShoppingCartDTO.getLocal_id()), idSession);
					             
		if(listaCarro != null && listaCarro.size() > 0){
				
			List l_torec = new ArrayList();
			listaCarro = bizShoppingCart.cargarPromocionesMiCarro(listaCarro, l_torec, oGetShoppingCartDTO.getLocal_id());	
								
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
				
			//PRODUCTOS
			//*************************************
			outputDTO.setProducts(this.getProductsCart(listaCarro, oGetShoppingCartDTO.getLocal_id(),bizShoppingCart));			
			
			//TOTAL
			//*************************************            
			outputDTO.setTotal(this.getTotal(listaCarro, oGetShoppingCartDTO.getLocal_id(),bizShoppingCart,oGetShoppingCartDTO.getCliente()));
				
			//DISCOUNTS					
			outputDTO.setDiscounts(this.getDiscounts(listaCarro, oGetShoppingCartDTO.getLocal_id(),bizShoppingCart,oGetShoppingCartDTO.getCliente()));
			
			//COUPON
			//*************************************
			ClientesDTO oClientesDTO = oGetShoppingCartDTO.getCliente();
			long rut = (oClientesDTO != null)? oClientesDTO.getRut():0;					
			outputDTO.setCoupon(this.getCupon(listaCarro, oGetShoppingCartDTO.getCoupon_code(), rut));	

		}else{
			outputDTO.setStatus(ConstantShoppingCart.SC_CARRO_VACIO);
			outputDTO.setError_message(ConstantShoppingCart.MSG_CARRO_VACIO);
		}			
		
		return outputDTO;		
	}
	
	/**
	 * Actualiza carro de compras genera un objeto de salida del tipo PutOutputShoppingCartDTO. 
	 * <p>
	 * Este metodo actualiza el carro de compras de un cliente registrado o invitado a traves del objeto oGetShoppingCartDTO,
	 * setea el objeto de salida outputDTO con los datos del carro de compras.
	 * Retorna un objeto del tipo PutOutputShoppingCartDTO. 
	 *
	 * @return  PutOutputShoppingCartDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PutOutputShoppingCartDTO
	 * @throws	GrabilityException
	 */
	public PutOutputShoppingCartDTO putShoppingCart() throws GrabilityException {
		
		PutOutputShoppingCartDTO outputDTO = new PutOutputShoppingCartDTO();
		BizDelegateShoppingCart bizShoppingCart = new BizDelegateShoppingCart();
				 
		//try{
			
		long idCliente = 0;
		String idSession = "";
		boolean isInvitado = false;
		
		
		if(StringUtils.equals(oPutShoppingCartDTO.getGuest_user(), "0")){
			idCliente = (long)oPutShoppingCartDTO.getClient_id();
			idSession = "1";
			isInvitado = false;	
		}else if(StringUtils.equals(oPutShoppingCartDTO.getGuest_user(), "1")){
			idCliente = 1;
			idSession = String.valueOf(oPutShoppingCartDTO.getClient_id());
			isInvitado = true;			
		}
						
		//Recorremos el json enviado con la estructura de arriba.
		Iterator it = oPutShoppingCartDTO.getJsProducts().iterator();		
					
		List listaNewCarro = new ArrayList();
		List criteriosProductos = new ArrayList();
			 
		while( it.hasNext() ) {
			JSONObject json = (JSONObject)it.next();
			
			String strCant = String.valueOf(json.get("quantity"));
			double cant = (StringUtils.isNumeric(strCant.replaceAll(",", "").replaceAll(".", "")))? Double.parseDouble(strCant.replace(',', '.')):0;
		    
		    String strIdProduct = String.valueOf(json.get("product_id"));
		    int idProduct = (StringUtils.isNumeric(strIdProduct))? Integer.parseInt(strIdProduct):0;
		    
		    String strIdCriteria = String.valueOf(json.get("substitution_criteria_id"));
		    int idCriteria = (StringUtils.isNumeric(strIdCriteria))? Integer.parseInt(strIdCriteria):0;
		    
		    String strDescCriteria = String.valueOf(json.get("susbtitution_description"));
		    
		    if(idProduct > 0){
		    	
			    CarroCompraDTO carro = new CarroCompraDTO();
			    carro.setPro_id(strIdProduct);
	            carro.setCantidad(cant);
	            carro.setLugar_compra(ConstantShoppingCart.SOURCE_CART);
	            
	            if(idCriteria > 0){
	            	CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
                    criterio.setIdProducto((long)idProduct);
                    criterio.setIdCriterio((long)idCriteria);
                    if ( criterio.getIdCriterio() == 4 ) {
                        criterio.setSustitutoCliente(strDescCriteria);
                    }
                    criteriosProductos.add(criterio);
                }
	            
	            listaNewCarro.add(carro);
		    }	            
		}
		
		//Insert en carro de compras.
		bizShoppingCart.carroComprasInsertProducto(listaNewCarro, idCliente, idSession);
		bizShoppingCart.guardaCriteriosMiCarro(new Long(idCliente), criteriosProductos, idSession);
		
		//List listaCarro = bizShoppingCart.carroComprasGetProductos(1, String.valueOf(getShoppingCartDTO.getLocal_id()), String.valueOf(getShoppingCartDTO.getClient_id()));
		List listaCarro = bizShoppingCart.carroComprasPorCategorias(idCliente, String.valueOf(oPutShoppingCartDTO.getLocal_id()), idSession);
					             
		if(listaCarro != null && listaCarro.size() > 0){
				
			List l_torec = new ArrayList();
			listaCarro = bizShoppingCart.cargarPromocionesMiCarro(listaCarro, l_torec, oPutShoppingCartDTO.getLocal_id());	
								
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
				
			//PRODUCTOS
			outputDTO.setProducts(this.getProductsCart(listaCarro, oPutShoppingCartDTO.getLocal_id(), bizShoppingCart));
			
			//TOTAL
			outputDTO.setTotal(this.getTotal(listaCarro, oPutShoppingCartDTO.getLocal_id(), bizShoppingCart,oPutShoppingCartDTO.getCliente()));
				
			//DISCOUNTS
			outputDTO.setDiscounts(this.getDiscounts(listaCarro, oPutShoppingCartDTO.getLocal_id(), bizShoppingCart,oPutShoppingCartDTO.getCliente()));

		}else{
			outputDTO.setStatus(ConstantShoppingCart.SC_CARRO_VACIO);
			outputDTO.setError_message(ConstantShoppingCart.MSG_CARRO_VACIO);
		}

		return outputDTO;	
		
	}
	
	/**
	 * Actualiza carro de compras genera un objeto de salida del tipo PutOutputShoppingCartDTO. 
	 * <p>
	 * Este metodo actualiza el carro de compras de un cliente registrado o invitado a traves del objeto oGetShoppingCartDTO,
	 * setea el objeto de salida outputDTO con los datos del carro de compras.
	 * Retorna un objeto del tipo PutOutputShoppingCartDTO. 
	 *
	 * @return  PutOutputShoppingCartDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PutOutputShoppingCartDTO
	 * @throws	GrabilityException
	 */
	public DeleteOutputShoppingCartDTO deleteShoppingCart() throws GrabilityException {
		
		DeleteOutputShoppingCartDTO outputDTO = new DeleteOutputShoppingCartDTO();
		BizDelegateShoppingCart bizShoppingCart = new BizDelegateShoppingCart();

		long idCliente = 0;
		String idSession = "";
		boolean isInvitado = false;			
		
		if(StringUtils.equals(oDeleteShoppingCartDTO.getGuest_user(), "0")){
			idCliente = (long)oDeleteShoppingCartDTO.getClient_id();
			idSession = "1";
			isInvitado = false;	
		}else if(StringUtils.equals(oDeleteShoppingCartDTO.getGuest_user(), "1")){
			idCliente = 1;
			idSession = String.valueOf(oDeleteShoppingCartDTO.getClient_id());
			isInvitado = true;			
		}
		
		bizShoppingCart.deleteCarroCompraAll(idCliente, idSession);
		List listaCarro = bizShoppingCart.carroComprasPorCategorias(idCliente, String.valueOf(oDeleteShoppingCartDTO.getLocal_id()), idSession);
        
		if(listaCarro != null && listaCarro.size() == 0){
			
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);				

		}else{
										
			List l_torec = new ArrayList();
			listaCarro = bizShoppingCart.cargarPromocionesMiCarro(listaCarro, l_torec, oDeleteShoppingCartDTO.getLocal_id());	
								
			outputDTO.setStatus(ConstantShoppingCart.SC_NO_ES_POSIBLE_ELIMINAR_CARRO);
			outputDTO.setError_message(ConstantShoppingCart.MSG_NO_ES_POSIBLE_ELIMINAR_CARRO);	
				
			//PRODUCTOS
			outputDTO.setProducts(this.getProductsCart(listaCarro, oDeleteShoppingCartDTO.getLocal_id(), bizShoppingCart));
			
			//TOTAL		                
			outputDTO.setTotal(this.getTotal(listaCarro, oDeleteShoppingCartDTO.getLocal_id(), bizShoppingCart, oDeleteShoppingCartDTO.getCliente()));
				
			//DISCOUNTS		
			outputDTO.setDiscounts(this.getDiscounts(listaCarro, oDeleteShoppingCartDTO.getLocal_id(), bizShoppingCart, oDeleteShoppingCartDTO.getCliente()));

		}
	
		return outputDTO;			
	}
	
	//
	private ArrayList getProductsCart(List listaCarro, long idLocal,BizDelegateShoppingCart bizShoppingCart) throws GrabilityException{
		ArrayList productos = new ArrayList(); 
		
		try{			
			Iterator it = listaCarro.iterator();				
			while(it.hasNext()){
				MiCarroDTO oMiCarroDTO = (MiCarroDTO) it.next();
				
				HashMap listProd = new HashMap();
				listProd.put("origin", String.valueOf(oMiCarroDTO.getTipoSel()));
				listProd.put("added_date",Util.formatoFechaGrability(oMiCarroDTO.getCar_fec_crea()));
				listProd.put("quantity", String.valueOf(oMiCarroDTO.getCantidad()));
				listProd.put("product_id", String.valueOf(oMiCarroDTO.getPro_id()));
				listProd.put("total_price", Formatos.formatoPrecioFO(oMiCarroDTO.getPrecio() * oMiCarroDTO.getCantidad()));
				listProd.put("unit_price", Formatos.formatoPrecioFO(oMiCarroDTO.getPrecio()));
				
				//*** Servicio carro de compras agregar nombre y descripcion
				//listProd.put("product", String.valueOf(oMiCarroDTO.getTipo_producto()));
				//listProd.put("brand", String.valueOf(oMiCarroDTO.getNom_marca()));
				
				//Para calcular el descuento sobre el producto
				//Un truco sucio.
				List listaCarroPromo = new ArrayList();
				listaCarroPromo.add(oMiCarroDTO);
				
				//"all means of payment unitary price, this price includes promotions",
				//HashMap listTotalTBK = (HashMap) getDiscountXProduct(listaCarroPromo, idLocal, Constant.TBK, bizShoppingCart);				
				//String totalDescTBK = String.valueOf(listTotalTBK.get("total"));
				//String totalDescSavingTBK = String.valueOf(listTotalTBK.get("savings"));
			        			        
				listProd.put("tmp_price","0");//totalDescTBK);
				
				//HashMap listTotalCAT = (HashMap) getDiscountXProduct(listaCarroPromo, idLocal, Constant.CAT, bizShoppingCart);
				//: "price paying with Cencosud Credit Card, this price includes promotions"
				//String totalDescCAT = String.valueOf(listTotalCAT.get("total"));
				//String totalDescSavingCAT = String.valueOf(listTotalCAT.get("savings"));
				
				listProd.put("cat_price","0");//totalDescCAT);
				
				if (isProductAvailable(oMiCarroDTO)) {
					//listProd.put("available", String.valueOf(oMiCarroDTO.getStock()));
					listProd.put("available", "1");
				}else{
					listProd.put("available", "0");
				}
	
				if(oMiCarroDTO.getIdCriterio() > 0){
					listProd.put("substitution_criteria_id", String.valueOf(oMiCarroDTO.getIdCriterio()));
					if(oMiCarroDTO.getSustitutoCliente() != null){
						listProd.put("susbtitution_description", oMiCarroDTO.getSustitutoCliente());	
					}else if(oMiCarroDTO.getDescripcion() != null){
						listProd.put("susbtitution_description", oMiCarroDTO.getDescripcion());
					}else{
						listProd.put("susbtitution_description", "");
					}
				}else{
					listProd.put("substitution_criteria_id", "0");
					listProd.put("susbtitution_description", "");
				}
					
				productos.add(listProd);
				listProd=null;
				oMiCarroDTO = null;
			}	
		} catch (Exception e) {
			throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_PRODUCTOS_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_PRODUCTOS_CARRO, e);
		}
		
		return productos;
	}
	
	private Map getDiscountXProduct(List listaCarro, long idLocal, String Fpago, BizDelegateShoppingCart bizShoppingCart) throws GrabilityException{
		
		HashMap listTotal = new HashMap();
		long precioTotal = 0;
		double descuento_promo_tmas = 0;
        double descuento_promo_tbk  = 0;
        
        doRecalculoResultado resultadoTMAS = null;
     	doRecalculoResultado resultadoTBK = null;
     	//BizDelegateShoppingCart bizShoppingCart = new BizDelegateShoppingCart();
     	List l_torec = new ArrayList();
		
    	try {
		    for (int i = 0; i < listaCarro.size(); i++) {
		    	MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
	            //Información del producto
	            //if (car.tieneStock()) {
	            if (isProductAvailable(car)) {
	            	precioTotal += Utils.redondearFO(car.getCantidad() * car.getPrecio());
	            }
		    }
		    
	        List l_prod = new ArrayList();
	        
	        for (int i = 0; i < listaCarro.size(); i++) {
	        	MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
	            //if (car.tieneStock()) {
	            if (isProductAvailable(car)) {
	                ProductoPromosDTO pro = new ProductoPromosDTO();
	                pro.setId_producto(car.getId_bo());
	                pro.setCod_barra(car.getCodbarra());
	                pro.setSeccion_sap(car.getCatsap());
	                pro.setCant_solicitada(car.getCantidad());
	                if(car.getPesable()!=null && car.getPesable().equals("S") )
	                    pro.setPesable("P");
	                else
	                    pro.setPesable("C");
	                pro.setPrecio_lista(car.getPrecio());              
	                pro.setRubro(car.getId_rubro());  
	                l_prod.add(pro);
	               // cantidadProductosCarro = cantidadProductosCarro + (int)pro.getCant_solicitada();
	            }
	        }
	        
	        if (l_prod != null && l_prod.size() > 0 ){
	        
	        	if(StringUtils.equals(Fpago, Constant.CAT)){
			        //Obtener datos de la promomocion CAT
			        doRecalculoCriterio catRecalculoDTO = new doRecalculoCriterio();
			        catRecalculoDTO.setCuotas(Constant.CUOTAS_CAT);
			        catRecalculoDTO.setF_pago(Constant.CAT);
			        catRecalculoDTO.setId_local((int)idLocal);
			        catRecalculoDTO.setGrupos_tcp(l_torec);
			        catRecalculoDTO.setProductos(l_prod);
			        
			        resultadoTMAS = bizShoppingCart.doRecalculoPromocionNew(catRecalculoDTO);
			        descuento_promo_tmas = resultadoTMAS.getDescuento_pedido();
			        if ( precioTotal < resultadoTMAS.getDescuento_pedido() ) {
		                descuento_promo_tmas = precioTotal;
		            }
	        	}
		        
		        if(StringUtils.equals(Fpago, Constant.TBK)){
			        //Obtener datos de la promomocion TBK
			        doRecalculoCriterio tbkRecalculoDTO = new doRecalculoCriterio();
			        tbkRecalculoDTO.setCuotas(Constant.CUOTAS_TBK);
			        tbkRecalculoDTO.setF_pago(Constant.TBK);
			        tbkRecalculoDTO.setId_local((int)idLocal);
			        tbkRecalculoDTO.setGrupos_tcp(l_torec);
			        tbkRecalculoDTO.setProductos( l_prod );		       		
					
					resultadoTBK  = bizShoppingCart.doRecalculoPromocionNew(tbkRecalculoDTO);               
					descuento_promo_tbk  = resultadoTBK.getDescuento_pedido();	            
		            
		            if ( precioTotal < resultadoTBK.getDescuento_pedido() ) {
		                descuento_promo_tbk = precioTotal;
		            }
		        }
	        }
	        
	    	/*{
				"total": "shopping cart total paying with TBK card",
				"savings": "shopping cart savings due to promotions (paying with TBK card)",
				"total_cat": "shopping cart total paying with CAT card",
				"savings_cat": "shopping cart savings due to promotions (paying with CAT card)",
			}*/
	        
	        if(StringUtils.equals(Fpago, Constant.TBK)){
		        listTotal.put("total",  Formatos.formatoPrecioFO(precioTotal - descuento_promo_tbk));
		        listTotal.put("savings",Formatos.formatoPrecioFO(descuento_promo_tbk));
	        }
	        
	        if(StringUtils.equals(Fpago, Constant.CAT)){
		        listTotal.put("total", Formatos.formatoPrecioFO(precioTotal - descuento_promo_tmas));
		        listTotal.put("savings", Formatos.formatoPrecioFO(descuento_promo_tmas));
	        }
        
    	} catch (Exception e) {
    		if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_TOTAL_PRODUCTO_PROMO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_TOTAL_PRODUCTO_PROMO, e);
 			}
    		
		}
		return listTotal;
	}
	
	private Map getTotal(List listaCarro, int idLocal, BizDelegateShoppingCart bizShoppingCart, ClientesDTO oClientesDTO) throws GrabilityException{
		
		HashMap listTotal = new HashMap();
		long precioTotal = 0;
		double descuento_promo_tmas = 0;
        double descuento_promo_tbk  = 0;
        
        doRecalculoResultado resultadoTMAS = null;
     	doRecalculoResultado resultadoTBK = null;
     	//BizDelegateShoppingCart bizShoppingCart = new BizDelegateShoppingCart();
     	List l_torec = new ArrayList();
		
    	try {
		    for (int i = 0; i < listaCarro.size(); i++) {
		    	MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
	            //Información del producto
	            if (isProductAvailable(car)) {
	            	precioTotal += Utils.redondearFO(car.getCantidad() * car.getPrecio());
	            }
		    }
		    
	        List l_prod = new ArrayList();
	        
	        for (int i = 0; i < listaCarro.size(); i++) {
	        	MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
	        	if (isProductAvailable(car)) {
	        	//if (car.tieneStock()) {
	                ProductoPromosDTO pro = new ProductoPromosDTO();
	                pro.setId_producto(car.getId_bo());
	                pro.setCod_barra(car.getCodbarra());
	                pro.setSeccion_sap(car.getCatsap());
	                pro.setCant_solicitada(car.getCantidad());
	                if(car.getPesable()!=null && car.getPesable().equals("S") )
	                    pro.setPesable("P");
	                else
	                    pro.setPesable("C");
	                pro.setPrecio_lista(car.getPrecio());              
	                pro.setRubro(car.getId_rubro());  
	                l_prod.add(pro);
	               // cantidadProductosCarro = cantidadProductosCarro + (int)pro.getCant_solicitada();
	            }
	        }
	        
	        if (l_prod != null && l_prod.size() > 0 ){
	        
		        //Obtener datos de la promomocion CAT
		        doRecalculoCriterio catRecalculoDTO = new doRecalculoCriterio();
		        catRecalculoDTO.setCuotas(Constant.CUOTAS_CAT);
		        catRecalculoDTO.setF_pago(Constant.CAT);
		        catRecalculoDTO.setId_local(idLocal);
		        catRecalculoDTO.setGrupos_tcp(l_torec);
		        catRecalculoDTO.setProductos(l_prod);
		        if(oClientesDTO.isColaborador())
		        	catRecalculoDTO.setRutColaborador(new Long(oClientesDTO.getRut()));
		        
		        //Obtener datos de la promomocion TBK
		        doRecalculoCriterio tbkRecalculoDTO = new doRecalculoCriterio();
		        tbkRecalculoDTO.setCuotas(Constant.CUOTAS_TBK);
		        tbkRecalculoDTO.setF_pago(Constant.TBK);
		        tbkRecalculoDTO.setId_local(idLocal);
		        tbkRecalculoDTO.setGrupos_tcp(l_torec);
		        tbkRecalculoDTO.setProductos( l_prod );	
	       		
				resultadoTMAS = bizShoppingCart.doRecalculoPromocionNew(catRecalculoDTO);
				resultadoTBK  = bizShoppingCart.doRecalculoPromocionNew(tbkRecalculoDTO);						                       
				descuento_promo_tmas = resultadoTMAS.getDescuento_pedido();
				descuento_promo_tbk  = resultadoTBK.getDescuento_pedido();
	           
	            if ( precioTotal < resultadoTMAS.getDescuento_pedido() ) {
	                descuento_promo_tmas = precioTotal;
	            }
	            
	            if ( precioTotal < resultadoTBK.getDescuento_pedido() ) {
	                descuento_promo_tbk = precioTotal;
	            }
	        }
	        
	    	/*{
				"total": "shopping cart total paying with TBK card",
				"savings": "shopping cart savings due to promotions (paying with TBK card)",
				"total_cat": "shopping cart total paying with CAT card",
				"savings_cat": "shopping cart savings due to promotions (paying with CAT card)",
			}*/
	        
	        listTotal.put("total",  Formatos.formatoPrecioFO(precioTotal - descuento_promo_tbk));
	        listTotal.put("savings",Formatos.formatoPrecioFO(descuento_promo_tbk));
	        
	        listTotal.put("total_cat", Formatos.formatoPrecioFO(precioTotal - descuento_promo_tmas));
	        listTotal.put("savings_cat", Formatos.formatoPrecioFO(descuento_promo_tmas));
        
    	} catch (Exception e) {
    		if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_TOTAL_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_TOTAL_CARRO, e);
 			}
    		
		}
		return listTotal;
	}
	
	private ArrayList getDiscounts(List listaCarro, int idLocal, BizDelegateShoppingCart bizShoppingCart, ClientesDTO oClientesDTO) throws GrabilityException{
		//Volver a la revision anterior por svn para rescatar el detalle de los descuentos.
		ArrayList descuentos = new ArrayList();
		return descuentos;
	}
	
	private Map getCupon(List listaCarro, String cupon, long rut) throws GrabilityException{
	
		Map cuponHash = new HashMap();
		cuponHash.put("coupon_description", "El cupon es invalido o vigencia de esta promocion ya se acabo.");
		cuponHash.put("coupon_status", ConstantShoppingCart.CUPON_DSCTO_STATUS_NOK);
		return cuponHash;		
	}
	
	private boolean isProductAvailable(MiCarroDTO product){
		return (product.tieneStock() && "A".equals(product.getEstadoPreciosLocales()));
	}

}
