package cl.cencosud.jumbo.input.dto;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.bizdelegate.BizDelegateAddress;
import cl.cencosud.jumbo.bizdelegate.BizDelegateClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;

public abstract class InputDTO {
	
	/**
	* Atributo cliente.
	*/	
    private ClientesDTO cliente = new ClientesDTO();
    
	/**
	* Atributo direccion.
	*/	
	private DireccionesDTO direccion = new DireccionesDTO();  
	
	/**
	* Atributo comuna.
	*/	
	private ComunasDTO comuna = new ComunasDTO();
	
	/**
	* Atributo region.
	*/	
	private RegionesDTO region = new RegionesDTO();
	
	/**
	* Atributo local.
	*/	
	private LocalDTO local = new LocalDTO();
	
	Logging logger = new Logging(this);
	
	public InputDTO(){}

	public InputDTO(HttpServletRequest request, String integrationCode, String referenceID) throws ExceptionInParam, GrabilityException {
						
		request.setAttribute("IntegrationCode",integrationCode);
		request.setAttribute("ReferenceID",referenceID);
		
		loggingHead(request);
		
		if ((Util.isGetMethod(request.getMethod()) || Util.isDeleteMethod(request.getMethod())) && (!Util.isContentTypeJson(request.getContentType()))){
			//Error de Falta de Parametrización
			String queryString = request.getQueryString();
			logger.info("Request: "+queryString+"\n");
			if(queryString == null ){
				//Error de Falta de Parametrización
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST_PARAM, Constant.MSG_BAD_REQUEST_PARAM);
			}
		}else{
			if(Util.isContentTypeJson(request.getContentType())){
				JSONObject js =	getRequestJson(request);
				JSONObject jsEBMHeader = getJsonEBMHeader(request);
				JSONObject jsDataArea = getJsonDataArea(request);
				if(js == null || jsEBMHeader  == null || jsDataArea == null ){
					//Error de Falta de Parametrización
					logger.info("Request: "+Constant.MSG_BAD_REQUEST_PARAM+"\n");
					throw new ExceptionInParam(Constant.SC_BAD_REQUEST_PARAM, Constant.MSG_BAD_REQUEST_PARAM);
				}
				if(js != null){
					logger.info("Request: "+js.toString()+"\n");
				}
				
			}else{
				//Error de validación de Datos OSB a BAM
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
			}		
		}	
		
		try {
			ConexionUtil ob = new ConexionUtil();
			ob.isEnableConn();
		} catch (SQLException e) {
			throw new GrabilityException(Constant.SC_ERROR_BD, Constant.MSG_ERROR_BD, e);
		}
	}
	
	/**
	* Este metodo logea la cabecera del reguest.
	*/	
	private void loggingHead(HttpServletRequest request) {
		
		 logger.info("x-Request : "+request.getAttribute("requestId"));		 
		 logger.info("RemoteAddr : "+request.getRemoteAddr());
		 logger.info("RemoteHost : "+request.getRemoteHost());		 
		 logger.info("ContentType : "+request.getContentType());
		 logger.info("CharacterEncoding : "+request.getCharacterEncoding());
		 logger.info("ContentLength : "+request.getContentLength());
		 logger.info("ContextPath : "+request.getContextPath());
		 logger.info("LocalAddr : "+request.getLocalAddr());
		 logger.info("LocalName : "+request.getLocalName());
		 logger.info("LocalPort : "+request.getLocalPort());
		 logger.info("Method : "+request.getMethod());
		 logger.info("PathInfo : "+request.getPathInfo());
		 logger.info("PathTranslated : "+request.getPathTranslated());
		 logger.info("Protocol : "+request.getProtocol());
		 logger.info("QueryString : "+request.getQueryString());
		 logger.info("RemotePort : "+request.getRemotePort());
		 logger.info("RemoteUser : "+request.getRemoteUser());
		 logger.info("RequestURI : "+request.getRequestURI());
		 logger.info("Scheme : "+request.getScheme());
		 logger.info("ServerName : "+request.getServerName());
		 logger.info("ServerPort : "+request.getServerPort());
		 logger.info("ServletPath : "+request.getServletPath());
		 logger.info("Class : "+request.getClass());
		 logger.info("Locale : "+request.getLocale());
		 logger.info("RequestURL : "+request.getRequestURL());
		 logger.info("X-authorized : "+request.getHeader("X-authorized"));
		 		 
		 Enumeration headers = request.getHeaderNames();
         while (headers.hasMoreElements()) {

             String header = (String) headers.nextElement();
             logger.info(header + " : " + request.getHeader(header));

         }
	}
	
	/**
	* Este metodo toma del request el campo del parametro 'paramName' y retorna como tipo long.
	 * @throws GrabilityException 
	*/	
	protected long getParamLong(HttpServletRequest request, String paramName) throws ExceptionInParam{
		
		long parseLong = 0;
		String param = getParamString(request, paramName);
		try {
			parseLong = Long.parseLong(param);
		} catch (NumberFormatException e) {
			
		}
		return parseLong;
	}
	
	/**
	* Este metodo toma del request el campo del parametro 'paramName' y retorna como tipo int.
	 * @throws GrabilityException 
	*/	
	protected int getParamInt(HttpServletRequest request, String paramName) throws ExceptionInParam{
		
		int parseParam= 0;
		String param = getParamString(request, paramName);
		try {
			parseParam = Integer.parseInt(param);
		} catch (NumberFormatException e) {
			
		}
		return parseParam;
	}
	
	/**
	* Este metodo toma del request el campo del parametro 'paramName' y retorna como tipo String.
	 * @throws GrabilityException 
	*/	
	protected String getParamString(HttpServletRequest request, String paramName) throws ExceptionInParam {

		//if (request.getParameter(paramName) != null && StringUtils.equals("GET", request.getMethod())){
		if (request.getParameter(paramName) != null && 
				((Util.isGetMethod(request.getMethod()) || Util.isDeleteMethod(request.getMethod())) && (!Util.isContentTypeJson(request.getContentType())))){
			return request.getParameter(paramName).toString().trim();
		}else{			
			JSONObject jsDataArea = getJsonDataArea(request);
			String value =  null;
			try{
				 value =  jsDataArea.getString(paramName);
			}catch (Exception ex) {}
			
			return value;			
		}

	}
		
	/**
	* Este metodo retorna un objeto ClientesDTO.
	*/	
	public ClientesDTO getCliente() {
		return cliente;
	}

	/**
	* Este metodo asigna un objeto ClientesDTO al atributo cliente.
	*/	
	public void setCliente(ClientesDTO cliente) {
		this.cliente = cliente;
	}

	/**
	* Este metodo retorna un objeto DireccionesDTO.
	*/	
	public DireccionesDTO getDireccion() {
		return direccion;
	}

	/**
	* Este metodo asigna un objeto DireccionesDTO al atributo direccion.
	*/	
	public void setDireccion(DireccionesDTO direccion) {
		this.direccion = direccion;
	}

	/**
	* Este metodo retorna un objeto ComunasDTO.
	*/	
	public ComunasDTO getComuna() {
		return comuna;
	}

	/**
	* Este metodo asigna un objeto ComunasDTO al atributo comuna.
	*/	
	public void setComuna(ComunasDTO comuna) {
		this.comuna = comuna;
	}

	/**
	* Este metodo retorna un objeto RegionesDTO.
	*/	
	public RegionesDTO getRegion() {
		return region;
	}

	/**
	* Este metodo asigna un objeto RegionesDTO al atributo region.
	*/	
	public void setRegion(RegionesDTO region) {
		this.region = region;
	}	
	
	/**
	* Este metodo retorna un objeto LocalDTO.
	*/	
	public LocalDTO getLocal() {
		return local;
	}

	/**
	* Este metodo asigna un objeto LocalDTO al atributo local.
	*/	
	public void setLocal(LocalDTO local) {
		this.local = local;
	}

	/**
	 * Valida si cliente existe por id. 
	 * <p>
	 * Este metodo valida al cliente a traves de su identificado en base de datos
	 * y setea el atributo cliente con el objeto ClientesDTO
	 * Retorna verdadero si cliente existe. 
	 *
	 * @param	identificador del cliente en base de datos
	 * @return  Verdadero, si cliente es valido
	 * @see     bool
	 * @throws	ExceptionInParam
	 */
	protected boolean isValidClientById(long idCliente) throws ExceptionInParam, GrabilityException {		
		BizDelegateClient bizClient = new BizDelegateClient();
		ClientesDTO oClientesDTO =  bizClient.getClienteById(idCliente);
		if(oClientesDTO.getId_cliente() == 0){
			throw new ExceptionInParam(ConstantClient.SC_CLIENTE_NO_EXISTE, ConstantClient.MSG_CLIENTE_NO_EXISTE);
		}
		setCliente(oClientesDTO);
		return true;		
	}
	
	/**
	 * Valida si cliente existe por rut. 
	 * <p>
	 * Este metodo valida al cliente por rut y setea el atributo cliente con el objeto ClientesDTO
	 * Retorna verdadero si cliente existe. 
	 *
	 * @param	rut del cliente
	 * @return  Verdadero, si cliente es valido
	 * @see     bool
	 * @throws	ExceptionInParam
	 */
	protected boolean isValidClientByRut(long rutCliente) throws ExceptionInParam, GrabilityException {
		BizDelegateClient bizClient = new BizDelegateClient();
		ClientesDTO oClientesDTO =  bizClient.getClienteByRut(rutCliente);
		if(oClientesDTO == null || oClientesDTO.getId_cliente() == 0){
			throw new ExceptionInParam(ConstantClient.SC_CLIENTE_NO_EXISTE, ConstantClient.MSG_CLIENTE_NO_EXISTE);
		}
		setCliente(oClientesDTO);
		return true;
	}

	/**
	 * Valida si direccion existe por id. 
	 * <p>
	 * Este metodo si valida la direccion y setea el atributo direccion con el objeto DireccionesDTO
	 * Retorna verdadero si direccion existe, falso si direccion no existe. 
	 *
	 * @param	Identificador de direccion en base de datos
	 * @return  Verdadero, si direccion es valida
	 * @see     bool
	 * @throws	ExceptionInParam
	 */
	protected boolean isValidAddressById(long idDireccion) throws ExceptionInParam, GrabilityException {
		BizDelegateAddress bizAddress = new BizDelegateAddress();
		DireccionesDTO oDireccionesDTO =  bizAddress.getDireccionByIdDir(idDireccion);
		if(oDireccionesDTO.getId_dir() == 0){
			throw new ExceptionInParam(ConstantAddress.SC_ID_DIR_SIN_DIRECCION, ConstantAddress.MSG_ID_DIR_SIN_DIRECCION);
		}
		setDireccion(oDireccionesDTO);
		return true;	
	}

	/**
	 * Valida si region existe por id. 
	 * <p>
	 * Este metodo valida la region y setea el atributo region con el objeto RegionesDTO
	 * Retorna verdadero si region existe. 
	 *
	 * @param	identificador de region en base de datos
	 * @return  si region es valida
	 * @see     bool
	 * @throws	ExceptionInParam
	 */
	protected boolean isValidRegionById(long idRegion) throws ExceptionInParam, GrabilityException {
		BizDelegateAddress bizAddress = new BizDelegateAddress();
		RegionesDTO oRegionesDTO =  bizAddress.getRegionById((int)idRegion);
		if(oRegionesDTO.getId() == 0){
			throw new ExceptionInParam(ConstantAddress.SC_ID_REGION_INVALIDO, ConstantAddress.MSG_ID_REGION_INVALIDO);
		}
		setRegion(oRegionesDTO);
		return true;	
	}
	
	/**
	 * Valida si comuna existe por id. 
	 * <p>
	 * Este metodo si valida al cliente y setea el atributo cliente con el objeto ClientesDTO
	 * Retorna verdadero si cliente existe, falso si cliente no existe. 
	 *
	 * @param	identificador del cliente en base de datos
	 * @return  si cliente es valido
	 * @see     bool
	 * @throws	ExceptionInParam
	 */
	protected boolean isValidComunaById(long idComuna) throws ExceptionInParam, GrabilityException {
		BizDelegateAddress bizAddress = new BizDelegateAddress();
		List comunas =  bizAddress.getComunas();
		Iterator it = comunas.iterator();
		ComunasDTO comuna = null;
		while(it.hasNext()){
			comuna = (ComunasDTO) it.next();
			if(idComuna == comuna.getId_comuna()){
				break;
			}
		}
		if(comuna != null && idComuna == comuna.getId_comuna()){
			setComuna(comuna);
			return true;
		}else{
			throw new ExceptionInParam(ConstantAddress.SC_ID_COMUNA_INVALIDO, ConstantAddress.MSG_ID_COMUNA_INVALIDO);
		}	
	}
	
	/**
	 * Valida si el local existe por id. 
	 * <p>
	 * Este metodo valida el local y setea el atributo local con el objeto ClientesDTO
	 * Retorna verdadero si local existe.
	 *
	 * @param	identificador del local en base de datos
	 * @return  si local es valido
	 * @see     bool
	 * @throws	ExceptionInParam
	 * @throws GrabilityException 
	 */
	protected boolean isValidLocalById(long id_local) throws ExceptionInParam, GrabilityException {
		BizDelegateAddress biz = new BizDelegateAddress();
		LocalDTO olocalDTO =  biz.getLocalById(id_local);
		if(olocalDTO.getId_local() == 0){
			throw new ExceptionInParam(ConstantAddress.SC_ID_LOCAL_INVALIDO, ConstantAddress.MSG_ID_LOCAL_INVALIDO);
		}
		setLocal(olocalDTO);
		return true;
	}
	
	
	//Recupera json del body del request.
	protected JSONObject getRequestJson(HttpServletRequest request){
		
		JSONObject js = null;
		String inputLine;
		String json = "";
				
		try {	
			
			BufferedReader oBufferedReader = new BufferedReader(request.getReader());			
								
			while ((inputLine = oBufferedReader.readLine()) != null) {
				json += inputLine;
			}
			if(oBufferedReader != null){
				oBufferedReader.close();
			}
		    js = (JSONObject) JSONSerializer.toJSON(json);	
		    JSONObject jsEBMHeader = (JSONObject) JSONSerializer.toJSON(js.get("EBMHeader"));
		    JSONObject jsDataArea = (JSONObject) JSONSerializer.toJSON(js.get("DataArea"));
		    			    
		    request.setAttribute("request.getReader",js);
		    request.setAttribute("EBMHeader",jsEBMHeader);
		    request.setAttribute("DataArea",jsDataArea);
		    
		} catch (Exception e) {
			logger.error(e);
		}
		
		
	    return js;
	}
	
	protected JSONObject getJsonRequest(HttpServletRequest request){

		JSONObject jsRequest= null;
		try {	
			jsRequest = (JSONObject) JSONSerializer.toJSON(String.valueOf(request.getAttribute("request.getReader")));
		} catch (Exception ex) {}
			
		return jsRequest;
	}
	
	protected JSONObject getJsonDataArea(HttpServletRequest request){

		JSONObject jsDataArea = null;
		try {	
			jsDataArea = (JSONObject) JSONSerializer.toJSON(String.valueOf(request.getAttribute("DataArea")));
		} catch (Exception ex) {}
			
		return jsDataArea;
	}
	
	protected JSONObject getJsonEBMHeader(HttpServletRequest request){

		JSONObject jsEBMHeader = null;
		try {	
			jsEBMHeader= (JSONObject) JSONSerializer.toJSON(String.valueOf(request.getAttribute("EBMHeader")));
		} catch (Exception ex) {}
			
		return jsEBMHeader;
	}
		
	/**
	* Este metodo se debe implementar en las clases hijas para validar los datos de entrada del servicio invocado.
	*/	
	public abstract void isValid() throws ExceptionInParam, GrabilityException;

}