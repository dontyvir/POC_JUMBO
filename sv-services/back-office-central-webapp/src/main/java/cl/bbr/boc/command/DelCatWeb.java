package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCatWebDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelCatWeb Comando Process
 * permite eliminar una categoria web
 * @author BBRI
 */

public class DelCatWeb extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_categoria=0L;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_categoria") == null ){throw new ParametroObligatorioException("id_categoria es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_categoria = Long.parseLong(req.getParameter("id_categoria")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_categoria: " + paramId_categoria);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
     /*
      * 3. Procesamiento Principal
      */
		BizDelegate biz = new BizDelegate();
		
		try{
			ProcDelCatWebDTO prm = new ProcDelCatWebDTO(paramId_categoria);
			boolean elim = biz.setDelCatWeb(prm);
			logger.debug("elim?"+elim);
		}catch(BocException e){
			logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
				logger.debug("La categoria no existe");
				fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
				fp.add("categoria_id", paramId_categoria+"");
				paramUrl = UrlError + fp.forward(); 
			} else if ( e.getMessage().equals(Constantes._EX_CAT_PROD_REL_EXISTE) ){
				logger.debug("La categoria tiene productos relacionados. No puede ser eliminada");
				fp.add( "rc" , Constantes._EX_CAT_PROD_REL_EXISTE );
				fp.add("categoria_id", paramId_categoria+"");
				paramUrl = UrlError + fp.forward(); 
			} else if ( e.getMessage().equals(Constantes._EX_CAT_SUBCAT_REL_EXISTE) ){
				logger.debug("La categoria tiene subcategorias relacionadas. No puede ser eliminada");
				fp.add( "rc" , Constantes._EX_CAT_SUBCAT_REL_EXISTE );
				fp.add("categoria_id", paramId_categoria+"");
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
		}
		/*
		boolean eliminar = false;
		CategoriasDTO catdto = biz.getCategoriasById(paramId_categoria);
		if(catdto.getTipo().equals("T")){
			//no debe tener productos
			List lst_prod = biz.getProductosByCategId(paramId_categoria);
			//puede contener productos con estado Inactivo, entonces, ¿la categoria puede ser eliminada?
			//No, debido a que siguen catalogando.
			if(lst_prod.size()==0){
				eliminar=true;
			} else
				paramUrl = paramUrl+ "&mns=La categoria tiene productos relacionados.\nNo puede ser eliminada";
		} else if(catdto.getTipo().equals("I")){
			//no debe tener subcategorias
			List lst_sub = biz.getCategoriasByCategId(paramId_categoria);
			if(lst_sub.size()==0) {
				eliminar = true;
			} else
				paramUrl = paramUrl+ "&mns=La categoria tiene subcategorias relacionadas.\nNo puede ser eliminada";
		}
		if(eliminar){
			//luego eliminar la categoria
			ProcDelCatWebDTO prm = new ProcDelCatWebDTO(paramId_categoria);
			boolean elim = biz.setDelCatWeb(prm);
			logger.debug("elim:"+elim);
		}*/

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
