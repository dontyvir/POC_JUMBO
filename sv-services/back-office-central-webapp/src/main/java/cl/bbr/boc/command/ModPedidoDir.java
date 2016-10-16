package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoDirDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar la direccion de un pedido
 * @author bbr
 *
 */
public class ModPedidoDir extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del m�todo
     String paramUrl = "";
     long paramIdPedido=0L;
     long paramIdDir=0L;
     
     // 2. Procesa par�metros del request
     logger.debug("Procesando par�metros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
     if ( req.getParameter("id_dir") == null ){throw new ParametroObligatorioException("id_dir es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramIdPedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     paramIdDir = Long.parseLong(req.getParameter("id_dir")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramIdPedido);
     logger.debug("id_dir: " + paramIdDir);
     
     ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
     
  		boolean result = false;
  		try{
  		    PedidoDTO ped = biz.getPedidosById(paramIdPedido);
  		    
  		    if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ||
  		          ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO ||
  		            ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_INGRESADO){

  		        ProcModPedidoDirDTO proc  = new ProcModPedidoDirDTO();
  	  			/*proc.setId_pedido(paramIdPedido);
  	  			proc.setId_dir(paramIdDir);
  	  			proc.setUsr_login(usr.getLogin());*/
  	  			
  	            // Obtiene la direcci�n nueva
  	            DireccionesDTO nuevaDir = biz.getDireccionByIdDir(paramIdDir);

  	  			result = biz.setModPedidoDir(usr.getLogin(), nuevaDir, ped);
  	  			paramUrl += "&mensaje=Cambio de Direcci�n de Despacho";
                
                try {
                    if (ped.getTipo_despacho().equalsIgnoreCase("R")) {
                        //Si cambiamos la direccion, ya no sera RETIRO EN LOCAL
                        biz.modTipoDespachoDePedido(paramIdPedido,"N");                        
                    }
                    if (!ped.getTipo_picking().equals(nuevaDir.getTipoPicking())){
                        biz.modTipoPickingPedido(paramIdPedido, nuevaDir.getTipoPicking());
                    }
                } catch (Exception e) {
                    // no hacemos nada
                }
  		    }else{
  		      paramUrl += "&mensaje=No fue posible cambiar la Direcci�n de Despacho<br>"
  		                + "debido a que el pedido se encuentra e un estado distinto<br>"
  		                + "a INGRESADO, EN VALIDACI�N o VALIDADO";
  		    }
  		} catch(BocException e){
  			String UrlError = getServletConfig().getInitParameter("UrlError");
  			logger.debug("Controlando excepci�n: " + e.getMessage());
  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
				logger.debug("El c�digo del pedido no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			} if (  e.getMessage().equals(Constantes._EX_OPE_ID_DIR_NO_EXISTE) ){
				logger.debug("El c�digo de la direcci�n no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_DIR_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}
			else
				throw new SystemException(e);
  		}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
