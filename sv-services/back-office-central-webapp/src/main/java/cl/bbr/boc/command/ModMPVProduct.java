package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModMPVProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 *  Comando que permite modificar el Mix de Productos Vendibles
 * @author bbr
 *
 */
public class ModMPVProduct extends Command {
	private final static long serialVersionUID = 1;
	
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramAction="";
     long paramCodProd = 0;
     String paramCod_prod_sap="";
     String mnsAgreg = "";
     String mnsElim = "";
     String paramSelCat = "";
     String paramTipoSap = "";
     String paramSap = "";
     String paramFecIni = "";
     String paramFecFin = "";
     String paramAccion = "";
     String paramEstMix = "";
     String paramEstPre = "";
     String paramPag = "";
    
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     
     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
     if ( req.getParameter("cod_prod") == null ){throw new ParametroObligatorioException("cod_prod es nulo");}
     if ( req.getParameter("cod_prod_sap") == null ){throw new ParametroObligatorioException("cod_prod_sap es nulo");}

     // 2.2 obtiene parametros desde el request
     
     paramUrl = req.getParameter("url");
     paramAction = req.getParameter("action"); //string:obligatorio:si
     paramCodProd = Integer.parseInt(req.getParameter("cod_prod")); //int:obligatorio:si
     if (req.getParameter("sel_cat") != null)
    	 paramSelCat  = req.getParameter("sel_cat"); //string:obligatorio:no
     if (req.getParameter("tipo_sap") != null)
    	 paramTipoSap = req.getParameter("tipo_sap"); //string:obligatorio:no
     if (req.getParameter("sap") != null)
    	 paramSap     = req.getParameter("sap"); //string:obligatorio:no
     if (req.getParameter("fec_ini") != null)
    	 paramFecIni  = req.getParameter("fec_ini"); //string:obligatorio:no
     if (req.getParameter("fec_fin") != null)
    	 paramFecFin  = req.getParameter("fec_fin"); //string:obligatorio:no
     if (req.getParameter("accion") != null)
    	 paramAccion  = req.getParameter("accion"); //string:obligatorio:no
     if (req.getParameter("est_mix") != null)
    	 paramEstMix  = req.getParameter("est_mix"); //string:obligatorio:no
     if (req.getParameter("est_pre") != null)
    	 paramEstPre  = req.getParameter("est_pre"); //string:obligatorio:no
     if (req.getParameter("pagina") != null)
    	 paramPag     = req.getParameter("pagina"); //string:obligatorio:no
     
     // 2.3 log de parametros y valores
     logger.debug("url en el command----> : " + paramUrl);
     logger.debug("action: " + paramAction);
     logger.debug("cod_prod: " + paramCodProd);
     logger.debug("cod_prod_sap: " + paramCod_prod_sap);
     logger.debug("sel_cat: " + paramSelCat);
     logger.debug("tipo_sap: " + paramTipoSap);
     logger.debug("sap: " +    paramSap);
     logger.debug("fec_ini: " + paramFecIni);
     logger.debug("fec_fin: " + paramFecFin);
     logger.debug("accion: " + paramAccion);
     logger.debug("est_mix: " + paramEstMix);
     logger.debug("est_pre: " + paramEstPre);
     logger.debug("pagina: " + paramPag);
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
     	try{
     		mnsAgreg = getServletConfig().getInitParameter("MensAgreg");
     		mnsElim = getServletConfig().getInitParameter("MensElim");
     		
     		ProcModMPVProductDTO mpvparam = new ProcModMPVProductDTO (paramAction, paramCod_prod_sap, paramCodProd, 
     				usr.getId_usuario(), usr.getLogin(), mnsAgreg, mnsElim); 
     		boolean exito = biz.setModMPVProduct(mpvparam);
     		logger.debug("exito:"+exito);
     	}catch(BocException e){
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			fp.add("sel_cat", paramSelCat);
			fp.add("tipo_sap", paramTipoSap);
			fp.add("sap", paramSap);
			fp.add("fec_ini", paramFecIni);
			fp.add("fec_fin", paramFecFin);
			fp.add("accion", paramAccion);
			fp.add("est_mix", paramEstMix);
			fp.add("est_pre", paramEstPre);
			fp.add("pagina", paramPag);
			if ( e.getMessage().equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
				logger.debug("El producto sap no existe");
				fp.add( "rc" , Constantes._EX_PSAP_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_PSAP_COD_PROD_NO_EXISTE) ){
				logger.debug("El codigo de producto en producto sap no existe");
				fp.add( "rc" , Constantes._EX_PSAP_COD_PROD_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_PSAP_DESACTIVADO) ){
				logger.debug("El producto sap se encuentra desactivado");
				fp.add( "rc" , Constantes._EX_PSAP_DESACTIVADO );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_OPE_DUP_COD_BARRA) ){
				logger.debug("El producto tiene codigo de barra duplicado");
				fp.add( "rc" , Constantes._EX_OPE_DUP_COD_BARRA );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_OPE_DUP_COD_PRECIO) ){
				logger.debug("El producto tiene precio duplicado");
				fp.add( "rc" , Constantes._EX_OPE_DUP_COD_PRECIO );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_OPE_PRECIO_NO_EXISTE) ){
				logger.debug("El producto no tiene precio.");
				fp.add( "rc" , Constantes._EX_OPE_PRECIO_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_OPE_CODBARRA_NO_EXISTE) ){
				logger.debug("El producto tiene no tiene codigo de barra");
				fp.add( "rc" , Constantes._EX_OPE_CODBARRA_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes.ESTADO_DESPUBLICADO) ){
				logger.debug("El producto Web tiene tiene estado despublicado");
				fp.add( "rc" , Constantes.ESTADO_DESPUBLICADO );
				paramUrl = UrlError + fp.forward(); 
			} else {
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
     	
     	logger.debug("ESTA ES LA URL: " + paramUrl);
     	logger.debug("Aquí es donde se estaba redireccionanado: " + paramUrl+"&accion=bus_sap&tipo_sap=prod&sap="+paramCod_prod_sap);
     // 4. Redirecciona salida
    // res.sendRedirect(paramUrl+"&accion=bus_sap&tipo_sap=prod&sap="+paramCod_prod_sap);
     	 res.sendRedirect(paramUrl);
 }//execute

}
