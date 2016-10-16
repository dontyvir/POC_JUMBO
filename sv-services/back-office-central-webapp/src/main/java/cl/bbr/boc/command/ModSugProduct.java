package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSugProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar los sugeridos de un producto 
 * @author bbr
 *
 */
public class ModSugProduct extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_producto = 0L;
	     long paramId_producto_sug = 0L;
	     String paramAction = "";
	     String paramDirecc = "";
	     long paramSug=0L;
	     
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_producto") == null ){throw new ParametroObligatorioException("id_producto es nulo");}
	     if ( req.getParameter("id_producto_sug") == null ){throw new ParametroObligatorioException("id_producto_sug es nulo");}
	     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
	     if ( req.getParameter("direccion") == null ){throw new ParametroObligatorioException("direccion es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_producto = Long.parseLong(req.getParameter("id_producto")); //long:obligatorio:si
	     paramId_producto_sug = Long.parseLong(req.getParameter("id_producto_sug")); //long:obligatorio:si
	     paramAction = req.getParameter("action"); //string:obligatorio:si
	     paramDirecc = req.getParameter("direccion"); //string:obligatorio:si
	     if (req.getParameter("sug_sug")!=null)
	    	 paramSug = Long.parseLong(req.getParameter("sug_sug")); //string:obligatorio:si
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_producto: " + paramId_producto);
	     logger.debug("id_producto_sug: " + paramId_producto_sug);
	     logger.debug("action: " + paramAction);
	     logger.debug("direccion: " + paramDirecc);
	    	 
	     ForwardParameters fp = new ForwardParameters();
	     fp.add( req.getParameterMap() );
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
	     String mensAgreg = getServletConfig().getInitParameter("MensAgreg");
	     String mensDesa = getServletConfig().getInitParameter("MensDesa");
	     
	     try{  
	    	 ProcModSugProductDTO proc = new ProcModSugProductDTO(paramId_producto, paramId_producto_sug, paramAction,
	    			 paramDirecc,mensAgreg,mensDesa,usr.getLogin(),paramSug);
	    	 boolean cambio = biz.setModSugProduct(proc);
	    	 logger.debug("cambio?"+cambio);
	     }catch(BocException e) {
				logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				
				if ( e.getMessage().equals(Constantes._EX_PROD_SUG_IGUAL_PROD) ){
					logger.debug("El producto sugerido debe ser distinto al producto");
					fp.add( "rc" , Constantes._EX_PROD_SUG_IGUAL_PROD );
					paramUrl = UrlError + fp.forward(); 
				} else if ( e.getMessage().equals(Constantes._EX_PROD_SUG_NO_EXISTE) ){
					logger.debug("El código del producto sugerido ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_SUG_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
				} else if ( e.getMessage().equals(Constantes._EX_PROD_SUG_EXISTE) ){
					logger.debug("El producto seleccionado ya existe como sugerido");
					fp.add( "rc" , Constantes._EX_PROD_SUG_EXISTE );
					paramUrl = UrlError + fp.forward(); 
				} else if (e.getMessage().equals(Constantes._EX_PROD_SUG_GENERICO) ){
						logger.debug("No se permite sugerir un producto genérico");
						fp.add( "rc" , Constantes._EX_PROD_SUG_GENERICO );
						paramUrl = UrlError + fp.forward(); 
						logger.debug("paramUrl:"+paramUrl);
				} else if (e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código del producto no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
					logger.debug("paramUrl:"+paramUrl);
				} else{
					logger.debug("Controlando excepción: " + e.getMessage());
				}
		} catch(Exception e){
			logger.debug("Controlando excepción desconocida: " + e.getMessage());
				
		}
	     /*
	     for(int i=0;i<paramId_producto_sugs.length;i++){
	    	//verifica si existe producto sugerido
	    	boolean existeSug = biz.isProductById(Long.parseLong(paramId_producto_sugs[i]));
	    	if(existeSug) {
				//si accion es agregar sugeridos al producto:
				if (paramAction.equals("agregar")){
					//ver si el producto es generico o es producto
					ProductosDTO prodItem = biz.getProductosById(Long.parseLong(paramId_producto_sugs[i]));
					if( !prodItem.getGenerico().equals("G")){
						String fec_crea = Formato.getFecHoraActual();
						ProductoSugerDTO suger = new ProductoSugerDTO();
						suger.setId_suger(Long.parseLong(paramId_producto_sugs[i]));	// colocar id del prod. seleccionado
						suger.setId_base(paramId_producto);			//colocar id del prod. generico
						suger.setFec_crea(fec_crea);				//colocar fecha y hora actual
						//suger.setEstado("A");						//estado Activado
						suger.setFormato(paramDirecc);				//tipo de relacion: U unico, B bidireccional
						boolean resb = biz.agregaSugeridoProducto(suger);
						logger.debug("agregar:"+resb);
						
						if(resb){
						    String mensAgreg = getServletConfig().getInitParameter("MensAgreg");
							// agregar la accion en el log del producto
						    fec_crea = Formato.getFecHoraActual();
						    ProductoLogDTO log = new ProductoLogDTO();
						    log.setCod_prod(paramId_producto);
						    log.setFec_crea(fec_crea);
						    log.setUsuario(usr.getApe_paterno());
						    nom_item = biz.getProductosById(Long.parseLong(paramId_producto_sugs[i])).getDesc_corta();
						    log.setTexto(nom_item+":"+mensAgreg);
						    int resLog = biz.setLogProduct(log);
						    logger.debug("se guardo log con id:"+resLog);
						    //si el formato es B, colocar el log en el producto sugerido
						    if(paramDirecc.equals("B")){
						    	mensAgreg = getServletConfig().getInitParameter("MensAgreg");
								// agregar la accion en el log del producto
							    fec_crea = Formato.getFecHoraActual();
							    log = new ProductoLogDTO();
							    log.setCod_prod(Long.parseLong(paramId_producto_sugs[i]));
							    log.setFec_crea(fec_crea);
							    log.setUsuario(usr.getApe_paterno());
							    nom_item = biz.getProductosById(paramId_producto).getDesc_corta();
							    log.setTexto(nom_item+":"+mensAgreg);
							    resLog = biz.setLogProduct(log);
							    logger.debug("se guardo log con id:"+resLog);
						    }
						}
						
					}else{
						paramUrl = paramUrl+ "&mns=No se permite sugerir un producto genérico";
					}
				}
				
				//si accion = desasociar: desasociar un item
				if (paramAction.equals("desasociar")){
					long id_suger = Long.parseLong(paramId_producto_sugs[i]); 	
					boolean resb = biz.eliminaSugeridoProducto(id_suger);
					logger.debug("desasociar:"+resb);
					
					if(resb){
					    String mensDesa = getServletConfig().getInitParameter("MensDesa");
						// agregar la accion en el log del producto
					    String fec_cre = Formato.getFecHoraActual();
					    ProductoLogDTO log = new ProductoLogDTO();
					    log.setCod_prod(paramId_producto);
					    log.setFec_crea(fec_cre);
					    log.setUsuario(usr.getApe_paterno());
					    nom_item = biz.getProductosById(Long.parseLong(paramSug)).getDesc_corta();
					    log.setTexto(nom_item+":"+mensDesa);
					    int resLog = biz.setLogProduct(log);
					    logger.debug("se guardo log con id:"+resLog);
					}
				}
	    		
	    	}else {
	    		paramUrl = paramUrl+ "&mns=El código del producto sugerido ingresado no existe";
	    	}
	    		
	     }*/
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
