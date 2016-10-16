package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoProdDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar los productos de un pedido
 * @author bbr
 *
 */
public class ModPedidoProduct extends Command {
	private final static long serialVersionUID = 1;
 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     String paramAction = "";
	     long paramId_pedido = 0L;
	     long paramId_prod = 0L;
	     long paramId_det=0L;
	     double paramCant=0;
	     String paramObs="";

	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if (req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if (req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
	     if (req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
	     if (req.getParameter("id_prod") == null ){throw new ParametroObligatorioException("id_prod es nulo");}
	     if (req.getParameter("action").equals("eliminar")){
	    	 if (req.getParameter("id_detalle") == null ){throw new ParametroObligatorioException("id_detalle es nulo");}
	     }
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramAction = req.getParameter("action"); //string:obligatorio:si
	     paramId_pedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
	     paramId_prod = Long.parseLong(req.getParameter("id_prod")); //string:obligatorio:si
	     if (req.getParameter("action").equals("eliminar")){
	    	 paramId_det = Long.parseLong(req.getParameter("id_detalle"));
	     }
	     if ( req.getParameter("cant") != null ){
	    	 paramCant = Double.parseDouble(req.getParameter("cant").replaceAll(",","."));
	     }
	     if ( req.getParameter("obs") != null )
	    	 paramObs = req.getParameter("obs"); 
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("action: " + paramAction);
	     logger.debug("id_pedido: " + paramId_pedido);
	     logger.debug("id_prod: " + paramId_prod);
	     logger.debug("cant: " + paramCant);
	     logger.debug("obs: " + paramObs);
	     logger.debug("id_detalle: " + paramId_det);
	     
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
	     //long id_sec = 0L;
	     
	     try{
			 String mensAgreg = getServletConfig().getInitParameter("MensAgreg");
			 String mensElim = getServletConfig().getInitParameter("MensElim");
			 
	    	 ProcModPedidoProdDTO prm = new ProcModPedidoProdDTO (paramId_pedido, paramId_prod, paramCant, paramObs, 
	    			 paramAction, usr.getLogin(), mensAgreg, paramId_det, mensElim);
             if ( req.getParameter("id_criterio_seleccionado") != null ) {
                 prm.setIdCriterio(Integer.parseInt(req.getParameter("id_criterio_seleccionado")));
                 if ( (req.getParameter("desc_criterio_seleccionado") != null) && (prm.getIdCriterio() == 4) ) {
                     prm.setDescCriterio(req.getParameter("desc_criterio_seleccionado"));
                 } else {
                     prm.setDescCriterio("");
                 }
             }
	    	 boolean result = biz.setModPedidoProd(prm);
	    	 logger.debug("result:"+result);
             
             long idCliente = 0;
             if ( req.getParameter("id_cliente") != null ) {
                 idCliente = Long.parseLong(req.getParameter("id_cliente"));
             }
             
             if ( prm.getIdCriterio() != 0 && idCliente != 0 ) {
                 biz.addModCriterioCliente( paramId_prod, idCliente, prm.getIdCriterio(), prm.getDescCriterio() );
             }
	    	 
	     }catch(BocException e) {
				logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				ForwardParameters fp = new ForwardParameters();
				fp.add( req.getParameterMap() );
				fp.add( "mod" , "1" );
				logger.debug("El mensaje es: " + e.getMessage());
			    if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
					logger.debug("El pedido no existe");
					fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				}
			    if (  e.getMessage().equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
					logger.debug("El código del producto no existe");
					fp.add( "rc" , Constantes._EX_PSAP_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				} 
			    if (  e.getMessage().equals(Constantes._EX_PSAP_SECTOR_NO_EXISTE) ){
					logger.debug("El producto no tiene sector");
					fp.add( "rc" , Constantes._EX_PSAP_SECTOR_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				}
			    if (  e.getMessage().equals(Constantes._EX_OPE_PRECIO_NO_EXISTE) ){
					logger.debug("El producto no tiene precio");
					fp.add( "rc" , Constantes._EX_OPE_PRECIO_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				}
			    if (  e.getMessage().equals(Constantes._EX_OPE_CODBARRA_NO_EXISTE) ){
					logger.debug("El producto no tiene codigo de barra");
					fp.add( "rc" , Constantes._EX_OPE_CODBARRA_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				}
			    if (  e.getMessage().equals(Constantes._EX_PROD_DESPUBLICADO) ){
					logger.debug("El producto tiene estado despublicado");
					fp.add( "rc" , Constantes._EX_PROD_DESPUBLICADO );
					paramUrl = UrlError + fp.forward();
				}
			    if (  e.getMessage().equals(Constantes._EX_OPE_ID_PROD_NO_EXISTE) ){
					logger.debug("El producto no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				}			    
			    if (  e.getMessage().equals(Constantes._EX_JPICK_ID_INVALIDO) ){
					logger.debug("La jornada de picking es invalido");
					fp.add( "rc" , Constantes._EX_JPICK_ID_INVALIDO );
					paramUrl = UrlError + fp.forward();
				}
	     }
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	 }//execute
}
