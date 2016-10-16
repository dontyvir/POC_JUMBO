package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import cl.bbr.boc.actions.ProductosDelegate;
import cl.bbr.boc.components.ServicioFactory;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.factory.ProductosFactory;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class FormLeySuperOcho extends Command {

	private static final long serialVersionUID = 1L;
	
	private String paramUrl = null;
	private String urlError = null; 
	private String nameForm = null;
	
	private int idProductoFO = 0;
	private NutricionalLeySupeDTO dto = null;
	private FichaNutricionalDTO fichaNutricionalDto = null;	
	private ProductosDelegate prodDelegate = null;    
	 
	private static final String NAME_FORM_LEY = "formLeySuper";	
	private static final String NAME_FORM_FICHA = "formFichaNutricional";
	
	private static String msj = null;
	private static final String MSJ_OK_LEY_SUPER_OCHO = "La informacion ha sido almacenada";
	private static final String MSJ_OK_FICHA_NUTRICIONAL = "La informacion ha sido almacenada";
	private static final String MSJ_NOK_DUPLICADO_FICHA_NUTRICIONAL = "El valor del campo item ya se encuentra.";
		
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {		
		ForwardParameters fp = null;
		try{
			prodDelegate = ServicioFactory.getService(new ProductosFactory()).getInstanceProductosDelegate();
			
			fp = new ForwardParameters();
			fp.add(req.getParameterMap());
			
			urlError = getServletConfig().getInitParameter("UrlError");			
			if(urlError==null){
				throw new IllegalArgumentException("Parametro [urlError] no existe o es nulo.");
			}
			
			validateParam(req);				
			
			nameForm = (String)req.getParameter("nameForm");
			paramUrl = req.getParameter("url");
			
			logger.debug("Nombre de accion a ejecutar:["+nameForm+"]");
			if(validateLeySuper(req)){
				paramUrl = req.getParameter("url");
				guardarLeySuperOcho( createObjetLeySuper(req) );
				paramUrl = paramUrl +"&msjLeySuperOcho=" + msj;
			}	
			//cabecera
			
			//SE COMENTA PARA PASAR SOLO LOS LOGOS
			idProductoFO = getIdProductoFO(req);
			guardarFichaNutricional( createObjectFichaNutricional(req), idProductoFO );
			/*paramUrl = paramUrl +"&msjLeySuperOcho=" + msj;	*/
			
		} catch (BocException boce) {
			logger.debug("ERROR, " + boce);
			paramUrl = urlError + fp.forward();
		}catch(IllegalArgumentException iae){						
			logger.debug("ERROR, " + iae);
			paramUrl = urlError + fp.forward();			
		}catch(Exception ex){
			logger.debug("ERROR, " + ex);
			paramUrl = urlError + fp.forward();
		}
		res.sendRedirect(paramUrl);
	}
		
	private void validateParam(HttpServletRequest req)throws Exception{
		if (req.getParameter("url") == null) {
			throw new IllegalArgumentException("Parametro [url] no existe o es nulo.");
		}
		if (req.getParameterValues("nameForm")==null ){
			throw new IllegalArgumentException("Parametro [nameForm] no existe o es nulo");
		}
	}
	private boolean validateLeySuper(HttpServletRequest req)throws Exception{
		if (req.getParameter("url") == null) {
			throw new IllegalArgumentException("Parametro [url] no existe o es nulo.");
		}
		if (req.getParameterValues("exceso_check")==null ){
			logger.debug("Warning Parametro [exceso_check] no existe o es nulo");
		}
		if (req.getParameterValues("fo_idProducto")==null){
			throw new IllegalArgumentException("Parametro [fo_idProducto] no existe o es nulo.");
		}
		if(StringUtils.isBlank(req.getParameterValues("fo_idProducto").toString()) ){
			throw new IllegalArgumentException("Parametro [exceso_check] es blanco.");
		}		
		return true;
	}
	
	private NutricionalLeySupeDTO createObjetLeySuper(HttpServletRequest req) throws Exception{
		idProductoFO = getIdProductoFO(req);		
		dto = new NutricionalLeySupeDTO.Builder(convertStringToList(req), idProductoFO).build();		
		logger.info("FormularioDto " + NAME_FORM_LEY +" " + dto.toString());
		
		return dto;
	}
	private Map createObjectFichaNutricional(HttpServletRequest req) throws Exception {
		Enumeration params = req.getParameterNames();
		Map mapItem = new HashMap();
		
		if(StringUtils.isBlank(req.getParameterValues("fo_idProducto").toString()) ){
			throw new IllegalArgumentException("Parametro [exceso_check] es blanco.");
		}
		String cabecera = (String) req.getParameter("cabecera");
		
		idProductoFO = getIdProductoFO(req);
		List items = new ArrayList();
		
		while (params.hasMoreElements()) {
			String paramNameItem = (String) params.nextElement();
			try{
				if (paramNameItem.substring(0, 6).equals("itemFN")) {
					String paramNameDesc  = "itemDescripFN" + paramNameItem.substring(6);
					String paramNameDesc2 = "itemDescrip2FN" + paramNameItem.substring(6);
					String paramNameorder = "orderFN" + paramNameItem.substring(6);
					String paramValueItem = (String) req.getParameter(paramNameItem);
					String paramValueDesc = (String) req.getParameter(paramNameDesc);
					String paramValueDesc2 = (String) req.getParameter(paramNameDesc2);
					
					int paramValueOrder = Integer.parseInt((String) req.getParameter(paramNameorder));
	
					if (!mapItem.containsKey(paramValueItem)) {
						fichaNutricionalDto = new FichaNutricionalDTO.Builder(idProductoFO, paramValueItem, items)
											.cabecera(cabecera)
											.descripcion(paramValueDesc)
											.descripcion2(paramValueDesc2)
											.order(paramValueOrder)
											.build();
						mapItem.put(paramValueItem, fichaNutricionalDto);
					} else {						
						//mapItem.put("VD", "DUPLICA2");
						logger.debug("valor duplicado");
						break;
					}
				}
			}catch(Exception e){
				logger.debug("Error " + e);
			}
		}				
		return mapItem;
	}
	
	private void guardarLeySuperOcho(NutricionalLeySupeDTO dto) throws Exception{		
		if(prodDelegate.guardarLeySuperOcho(dto)){
			msj = MSJ_OK_LEY_SUPER_OCHO;
		}
	}
	private void guardarFichaNutricional(Map mapa, int idProductoFO) throws Exception{				
		if (prodDelegate.guardarFichaNutricional(mapa, idProductoFO)){
			msj = MSJ_OK_FICHA_NUTRICIONAL;
		}			
	}
	
	private List convertStringToList(HttpServletRequest req){
		List newLista = null;
		try{
			String [] array = req.getParameterValues("exceso_check");
			newLista = Arrays.asList(array);
		}catch(Exception ex){
			logger.error("Warning check estan vacios");
			newLista = new ArrayList();
		}
					
		return newLista;
	}
	
	private int getIdProductoFO(HttpServletRequest req)throws Exception{
		return new Integer(req.getParameter("fo_idProducto")).intValue();	
	}

	private void printServletRequest(HttpServletRequest request){		
		printHeaderNames(request);
		printParameterNames(request);
		printAttributeNames(request);		
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
