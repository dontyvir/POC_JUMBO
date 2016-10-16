package cl.jumbo.ventamasiva.ctrl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDetalleFODTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.jumbo.ventamasiva.BizDelegate.BizDelegate;
import cl.jumbo.ventamasiva.Constant.Constant;
import cl.jumbo.ventamasiva.command.Command;
import cl.jumbo.ventamasiva.dto.PedidoDTO;
import cl.jumbo.ventamasiva.exceptions.ExceptionInParam;
import cl.jumbo.ventamasiva.exceptions.VentaMasivaException;
import cl.jumbo.ventamasiva.log.Logging;
import cl.jumbo.ventamasiva.utils.Util;


public class CtrlPurchaseOrder extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logging logger = new Logging(this);
	
	
	boolean generaClienteInvitado = false;
	
	protected void execute(HttpServletRequest request, HttpServletResponse response) throws VentaMasivaException {
		
		PedidoDTO pedidoDTO = new PedidoDTO(); 
		ProcInsPedidoDTO pedido = new ProcInsPedidoDTO(); //pedido para insertar
		ProcInsPedidoDetalleFODTO carro = new ProcInsPedidoDetalleFODTO();
		String urlError = new String() ;
		JornadaDTO jornadasDTO = new JornadaDTO();
		String tokenRecibido = null;
		
		try {		
//			Cookie[] cook = request.getCookies();
//			if (cook != null) {
//				for (int i = 0; i < cook.length; i++) {
//					if ("token".equals(cook[i].getName())) {
//						tokenRecibido = cook[i].getValue();
//						break;
//					}					
//				}				
//			}			//valida si el token existe en la solicitud
//			if (!(request.getParameter("tienecook")==null)){
//				tokenRecibido="";
//			}
//			if(tokenRecibido == null){
//				throw new Exception();				
//			}
			printServletRequest(request);
			String nameIn = Constant.REDIRECT_URL_PAYMENT_TBRAINS + "', '"
					+ Constant.REDIRECT_URL_SUCCESS_TBRAINS + "', '"
					+ Constant.REDIRECT_URL_ERROR_TBRAINS + "', '"
					+ Constant.REDIRECT_URL_PAYMENT_BASE_TBRAINS +"', '"
					+ Constant.URL_PAGO_CAT_TBRAINS + "', '"
					+ "POLIGONO_VENTA_MASIVA" + "', '" 
					+ Constant.VALOR_MINIMO_DESPACHO_VENTA_MASIVA + "', '";
			BizDelegate bizDelegate = new BizDelegate();
			Map mapParams =  bizDelegate.getParametroByNameIn(nameIn);
			ParametroDTO paramUrlError    = (ParametroDTO)mapParams.get(Constant.REDIRECT_URL_ERROR_TBRAINS);	
			urlError = paramUrlError.getValor(); 
			
			if (!validateParametersLocal(request)) {
				logger.info("Faltan parametros minimos (CtrlPurchaseOrder):"+ Constant.REG_FALTAN_PARAM);											
				response.sendRedirect(urlError+"?err=10");
			} else {	
				//recibe datos											
				pedidoDTO.setRut(request.getParameter("RUT"));
				pedidoDTO.setName( getParamString(request, "NAME"));
				pedidoDTO.setLast_name( getParamString(request, "LAST_NAME") );
				pedidoDTO.setEmail( getParamString(request, "EMAIL"));
				pedidoDTO.setPhone_number( getParamString(request, "PHONE_NUMBER"));
				//pedidoDTO.setAddress( getParamString(request, "address") );
				pedidoDTO.setType_street(Long.parseLong(request.getParameter("TYPE_STREET").toString()));
				pedidoDTO.setStreet_name(getParamString(request, "STREET_NAME"));
				pedidoDTO.setNumber_street(getParamString(request, "NUMBER_STREET"));
				pedidoDTO.setApartment(getParamString(request, "APARTMENT"));
				pedidoDTO.setRegion_id(Long.parseLong(request.getParameter ("REGION_ID").toString()));
				pedidoDTO.setComuna_id(Long.parseLong(request.getParameter ("COMUNA_ID").toString()));
				pedidoDTO.setObservation( getParamString(request, "OBSERVATION"));				
				pedidoDTO.setDate( getParamString(request, "DATE"));
				pedidoDTO.setWindow("AM");
				pedidoDTO.setDelivery_price( getParamString(request, "DELIVERY_PRICE"));
				pedidoDTO.setPayment_method( getParamString(request, "PAYMENT_METHOD"));
				pedidoDTO.setProducts((JSONArray)JSONSerializer.toJSON(request.getParameter("PRODUCTS").toString()));
				//System.out.println("Pedido >>> " + pedidoDTO.toString());
				List productsArray = new ArrayList();
				List criteriosProductos = new ArrayList();
				for (int i=0;i<pedidoDTO.getProducts().size();i++){ 
					JSONObject prod = pedidoDTO.getProducts().optJSONObject(i);
					CarroCompraDTO pro = new CarroCompraDTO();
					pro.setCantidad(prod.optDouble("QUANTITY"));
					pro.setPro_id(prod.optString("PRODUCT_ID"));
					CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
                    criterio.setIdProducto(Integer.parseInt(prod.optString("PRODUCT_ID")));
                    criterio.setIdCriterio(5);//No sustituir
                    criteriosProductos.add(criterio);
				    productsArray.add(pro);
				}
				
				ParametroDTO valorDespacho    = (ParametroDTO)mapParams.get(Constant.VALOR_MINIMO_DESPACHO_VENTA_MASIVA);	
				long valorMinimoDespacho = Long.parseLong(valorDespacho.getValor()); 
				//validamos si el valor de despacho es menor al minimo permitido
				if (valorMinimoDespacho>Long.parseLong(pedidoDTO.getDelivery_price())){
					pedidoDTO.setDelivery_price(valorMinimoDespacho+"");
				}
				//si el telefono es mayor a 20 caracteres
				if (pedidoDTO.getPhone_number().length()>20){
					pedidoDTO.setPhone_number(pedidoDTO.getPhone_number().substring(0,20));
				}
				//validamos que el nombre no supere los 50 caracteres
				if (pedidoDTO.getName().length()>50){
					pedidoDTO.setName(pedidoDTO.getName().substring(0,50));
				}
				//validamos que el nombre no supere los 50 caracteres
				if (pedidoDTO.getLast_name().length()>50){
					pedidoDTO.setLast_name(pedidoDTO.getLast_name().substring(0,50));
				}
				//validamos que el nombre de la calle no supere los 200 caracteres
				if (pedidoDTO.getStreet_name().length()>200){
					pedidoDTO.setStreet_name(pedidoDTO.getStreet_name().substring(0,200));
				}
				//validamos que los comentarios no superen los 255 caracteres
				if (pedidoDTO.getObservation().length()>255){
					pedidoDTO.setObservation(pedidoDTO.getObservation().substring(0,255));
				}
				//Se agrega cliente como invitado
				logger.info("Se creara cliente invitado y su direccion");
				String respuesta = generaClienteInvitado(bizDelegate, pedidoDTO);
				long id = -1;
				long id_dir = -1;
                id = Long.parseLong(respuesta.split("--")[0]);
                if (id == 0) {
                    logger.error("No se pudo crear cliente invitado id = 0 resp=" + respuesta);
                    //throw new VentaMasivaException();
                }
                id_dir = Long.parseLong(respuesta.split("--")[1]);
                long idCliente = id;
        		//Insert en carro de compras.
        		bizDelegate.carroComprasInsertProducto(productsArray, idCliente, "");
        		bizDelegate.guardaCriteriosMiCarro(new Long(idCliente), criteriosProductos, "");
    			double cantidadProductos = 0.0;
    			double montoTotal = 0.0;
    			// Detalle del pedido
    			
    			List list_det = new ArrayList();
    			List lcarro = bizDelegate.carroComprasGetProductosVentaMasiva(idCliente, Constant.ID_LOCAL_VENTA_MASIVA+"", null);
    			// Recuperar los datos de los productos y dejarlo en arreglo
    			logger.info("Se sacan los datos del producto");
    			if (lcarro.size()==0){//si el carro no tiene productos
    				logger.error("Carro de compras sin productos o no pertenecen al local... Cliente: "+ pedidoDTO.getName() + " "+ pedidoDTO.getLast_name());
    				throw new Exception();
    			}
    			for (int i = 0; i < lcarro.size(); i++) {
                    CarroCompraDTO prods = (CarroCompraDTO) lcarro.get(i);
                    ProcInsPedidoDetalleFODTO detalle = new ProcInsPedidoDetalleFODTO();
                    detalle.setId_producto_fo(Long.parseLong(prods.getPro_id()));
                    detalle.setCant_solic(prods.getCantidad());
                    detalle.setCon_nota("");
                    if (prods.getNota() == null) {
                        detalle.setObservacion("");
                    } else {
                        if (prods.getNota().equalsIgnoreCase("null")) {
                            detalle.setObservacion("");
                        } else {
                            detalle.setObservacion(prods.getNota());
                        }
                    }
                    detalle.setPrecio_unitario(prods.getPrecio());
                    detalle.setTipoSel(prods.getTipoSel());
                    detalle.setPrecio_lista(prods.getPrecio());
                    list_det.add(detalle);
                    cantidadProductos += prods.getCantidad();
                    montoTotal += (prods.getCantidad() * prods.getPrecio());                   
                }
    			////// SACAMOS ID JORNADA DE DESPACHO segun la comuna escogida
    			logger.info("Se extrae ID de Jornada de despacho");
    			jornadasDTO = bizDelegate.getDatosJornadaDespachoSegunComuna(pedidoDTO.getWindow(),pedidoDTO.getDate(), pedidoDTO.getComuna_id(),Constant.ID_LOCAL_VENTA_MASIVA);
    			if (jornadasDTO.getId_jornada()==0){
    				logger.error("No se pudo obtener jornada de despacho");
    				throw new Exception();
    			}
    			logger.info("ID de Jornada de despacho:" + jornadasDTO.getId_jornada());
				//Ahora se agregan los datos del pedido al objeto para su insercion
    			logger.info("Se vacian en objeto pedido todos los datos del pedido para su insercion");
    			String rut = pedidoDTO.getRut().replaceAll("-", "");
    	        int n=rut.length();
    	        char dv = rut.charAt(n-1);
    	        rut= rut.substring(0,rut.length()-1);
    			pedido.setLst_cupones(new ArrayList());
    			pedido.setCantidadProductos(cantidadProductos);
				pedido.setRut_tit(rut);
				pedido.setDv_tit(dv+"");
				pedido.setNom_tit(pedidoDTO.getName());
				pedido.setApat_tit(pedidoDTO.getLast_name());
				pedido.setDir_id(id_dir);
				pedido.setObservacion(pedidoDTO.getObservation());
				pedido.setSin_gente_rut(0);
				pedido.setSin_gente_dv("");
				pedido.setId_cliente(idCliente);
				pedido.setTipo_doc(Constant.TYPE_DOCUMENT);
				pedido.setDispositivo("M");//Masiva
				pedido.setTipo_despacho("N");
				pedido.setMedio_pago(pedidoDTO.getPayment_method());
				if (pedido.getMedio_pago().equals(Constant.PAYMENT_METHOD_TBK)){
					pedido.setId_usuario_fono(Constant.ID_USER_PHONE_DEFAULT);
					pedido.setNom_tbancaria(Constant.NAME_CARD_TBK_GENERIC);
				}
				pedido.setId_estado(Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO);
				pedido.setPol_id(Constant.ID_POLIGONO);
				pedido.setPol_sustitucion(Constant.POLIGONO_SUSTITUCION);
				pedido.setCosto_desp(Double.parseDouble(pedidoDTO.getDelivery_price()));
				pedido.setId_local_fact(Constant.ID_LOCAL_FACTURADOR);
				pedido.setId_local_desp(Constant.ID_LOCAL_VENTA_MASIVA);
				pedido.setMontoTotal(montoTotal);
				pedido.setProductos(list_det);
				pedido.setId_jdespacho(jornadasDTO.getId_jornada());
				//cupones vacios				
				List lst_tcp = new ArrayList();
				pedido.setLst_tcp(lst_tcp);
				pedido.setLst_cupones(lst_tcp);
				//inserta pedido
				logger.info("Se insertara pedido");
				long idPedido = bizDelegate.doInsPedidoNew(pedido);
				if (idPedido == 0){
					logger.error("No se pudo generar OP");
					throw new Exception();
				}
				logger.info("ID de pedido obtenido es "+ idPedido);
				request.setAttribute("id_pedido",idPedido+"");
			    request.getRequestDispatcher("/PurchasePay").forward(request, response);		    
			}		
		} catch (Exception e) {
			logger.error("Error, " + e);
			try {
				response.sendRedirect(urlError+"?err=4");
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}
	}
	private String generaClienteInvitado(BizDelegate bizDelegate,PedidoDTO pedidoDTO) throws Exception {
		long id = -1;
		 
        String respuesta = "";
        long fec_crea = 0;
        long recibeSms = 0;
        long region = 13;
        String rut = pedidoDTO.getRut();
        //rut=rut.replaceAll(".", "");//sacamos puntos
        rut = rut.replaceAll("-", "");
        int n=rut.length();
        char dv = rut.charAt(n-1);
        rut= rut.substring(0,rut.length()-1);
        String formaDespacho="D";
        //this.getLogger().info("Intenta crear objeto cliente.");
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id);
        cliente.setRut(123123);
        cliente.setDv("3");//sacar digito verificador
        cliente.setNombre("Invitado");
        cliente.setApellido_pat((String) pedidoDTO.getLast_name());
        cliente.setApellido_mat("");
        cliente.setEmail((String) pedidoDTO.getEmail());
        cliente.setRecibeSms(recibeSms);
        cliente.setFec_crea(fec_crea);
        cliente.setFec_nac(fec_crea);
        cliente.setEstado("A");
        cliente.setGenero("M");
        cliente.setClave((String) pedidoDTO.getRut().toString().substring(5));
        //cliente.setFon_cod_2("");//sacar codigo de area
        cliente.setFon_num_2(pedidoDTO.getPhone_number());//sacar resto de numero
        //this.getLogger().info("Intenta crear objeto direcciones.");
        DireccionesDTO direccion = new DireccionesDTO();
        direccion.setAlias("Direccion 1");
        direccion.setCalle(pedidoDTO.getStreet_name());//sacar nombre de calle
        direccion.setTipo_calle(pedidoDTO.getType_street());//sacar tipo (avda, calle, pasaje)
        direccion.setNumero(pedidoDTO.getNumber_street());//sacar numero de direccion
        direccion.setDepto(pedidoDTO.getApartment());//Sacar departamento si corresponde
        direccion.setReg_id(pedidoDTO.getRegion_id());//siempre RM - 13
        direccion.setCom_id(pedidoDTO.getComuna_id());//siempre comuna santiago
        direccion.setComentarios("");
        int idpoligono= bizDelegate.getPoligonoVentaMasivaPorComuna(pedidoDTO.getComuna_id());
        direccion.setId_poligono(idpoligono);
        //this.getLogger().info("Llama a la creacion desde invitado.");
        respuesta = bizDelegate.creaClienteDesdeInvitado(cliente, direccion, formaDespacho);//id_cliente y id_direccion
        //this.getLogger().info("Llego esta respuesta desde creaClienteDesdeInvitado.");
		return respuesta; 
	}
	


	protected String getParamString(HttpServletRequest request, String paramName) throws ExceptionInParam {
		//logger.info("<<<<< " + request.getMethod());
		if (request.getParameter(paramName) != null && Util.isPostMethod(request.getMethod())){
			//logger.info(request.getParameter(paramName).toString().trim());
			return request.getParameter(paramName).toString().trim();
		}else{			
//			JSONObject jsDataArea = getJsonDataArea(request);
//			String value =  null;
//			try{
//				 value =  jsDataArea.getString(paramName);
//			}catch (Exception ex) {}
//			
//			return value;
			return null;	
		}
		

	}
	
	private boolean validateParametersLocal(HttpServletRequest arg0) {       
		ArrayList campos = new ArrayList();
		campos.add("rut");
        campos.add("name");
        campos.add("last_name");
        campos.add("email");
        campos.add("phone_number");
        //campos.add("address");
        campos.add("observation");
        campos.add("products");
        campos.add("date");
        //campos.add("window");
        campos.add("delivery_price");
        campos.add("payment_method");
        campos.add("street_name");
        campos.add("region_id");
        campos.add("comuna_id");
        campos.add("type_street");
        // Ver si algun valor viene null o no viene
        for (int i = 0; i < campos.size(); i++) {
        	String campo = (String) campos.get(i);
            if (arg0.getParameter(campo.toUpperCase()) == null) {
            	logger.error("Faltan parametro de entrada:["+campo+"] en CtrlPurchaseOrder");
                return false;
            }
        }
        //validar email valido
        Matcher coincide = Pattern.compile(Constant.PATRON_MAIL).matcher(arg0.getParameter("EMAIL"));
        if (!coincide.matches()){
        	logger.error("Problema con mail en CtrlPurchaseOrder");
        	return false;
        }
        //fin valida email valido
        //valida fecha de despacho
        try{
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formatoFecha.setLenient(false);
        formatoFecha.parse(arg0.getParameter("DATE"));
        }catch (ParseException e){
        	logger.error("Problema con Fecha en CtrlPurchaseOrder");
        	return false;
        }
        //fin valida fecha de despacho
        //Valida ventana si es AM o PM
//        String [] ventanas = Constant.VENTANAS.split("-");
//		boolean isVentanaValida = false;
//		for (int i=0;i<ventanas.length;i++){
//			if (ventanas[i].equals(arg0.getParameter("WINDOW").toString())){
//				isVentanaValida = true;
//				break;
//			}
//		}
//		if (!isVentanaValida){
//			logger.error("Problema con ventana en CtrlPurchaseOrder");
//        	return false;
//		}
        //Fin Valida ventana si es AM o PM
        //valida si viene solo numero en el telefono
        String fono = arg0.getParameter("PHONE_NUMBER").toString();
        fono = fono.replaceAll("-", "");//si viene algun guion se saca
        if (!StringUtils.isNumeric(fono)){
        	logger.error("Problema con telefono en CtrlPurchaseOrder");
        	return false;
        }        
        //fin valida numero telefonico
        //valida medio de pago
        String [] medios = Constant.MEDIOS_DE_PAGO_VALIDOS.split("-");
		boolean isMedioPagoValido = false;
		for (int i=0;i<medios.length;i++){
			if (medios[i].equals(arg0.getParameter("PAYMENT_METHOD").toString())){
				isMedioPagoValido = true;
				break;
			}
		}
		if (!isMedioPagoValido){
			logger.error("Problema con Medio de pago en CtrlPurchaseOrder");
        	return false;
		}
		//Fin Valida medio de pago
		//valida tipo de calle
		if (!StringUtils.isNumeric(arg0.getParameter("TYPE_STREET"))){
			logger.error("Problema con tipo de calle en CtrlPurchaseOrder");
        	return false;
		}
		if ((Long.parseLong(arg0.getParameter("TYPE_STREET"))<=0)||(Long.parseLong(arg0.getParameter("TYPE_STREET"))>=4)){
			logger.error("Problema con tipo de calle en CtrlPurchaseOrder");
        	return false;
		}
		//fin valida tipo de calle
		//valida region id
		if (!StringUtils.isNumeric(arg0.getParameter("REGION_ID"))){
			logger.error("Problema con id region en CtrlPurchaseOrder");
        	return false;
		}
		if ((Long.parseLong(arg0.getParameter("REGION_ID"))<=0)||(Long.parseLong(arg0.getParameter("REGION_ID"))>=16)){
			logger.error("Problema con region id en CtrlPurchaseOrder");
        	return false;
		}
		//fin valida region id
		//valida comuna id
		if (!StringUtils.isNumeric(arg0.getParameter("COMUNA_ID"))){
			logger.error("Problema con id comuna en CtrlPurchaseOrder");
		  	return false;
		}
		if (Long.parseLong(arg0.getParameter("COMUNA_ID"))<=0){
			logger.error("Problema con comuna id en CtrlPurchaseOrder");
		   	return false;
		}
		//fin valida comuna
						
        return true;

    }
	
	private void printServletRequest(HttpServletRequest request){		
		//printHeaderNames(request);
		printParameterNames(request);
		//printAttributeNames(request);		
	}
	private void printHeaderNames(HttpServletRequest arg0){
		logger.info("*** HEADERS ***");
		Enumeration headers = arg0.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = (String) headers.nextElement();
			logger.info(" {" + header + "," + arg0.getHeader(header)+ "}");
         }
	}
	private void printParameterNames(HttpServletRequest arg0){
		logger.info("*** PARAMETERS ***");
		Enumeration params = arg0.getParameterNames();
		while (params.hasMoreElements()) {
			String element = (String) params.nextElement();
			logger.info(" {" + element + "," + arg0.getParameter(element) + "}");
	      }
	}
	private void printAttributeNames(HttpServletRequest arg0){
		logger.info("*** ATTRIBUTES ***");
		Enumeration attributes = arg0.getAttributeNames();
		while (attributes.hasMoreElements()) {
			String element = (String) attributes.nextElement();
			logger.info(" {" + element + "," + arg0.getAttribute(element) + "}");
		}
	}
    
}