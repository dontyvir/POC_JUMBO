package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AnulaOP Comando Process
 * comando que anula una Op
 * @author BBRI
 */

public class CambiaEstadoOP extends Command {

 
 /**
	 * 
	 */
	private static final long serialVersionUID = -3836701704769786486L;

protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	logger.debug("User: " + usr.getLogin());
     // 1. seteo de Variables del método
	 String url;
     String paramUrl = "";
     long id_pedido = 0L;
     long id_estado_actual = 0L;
     long new_estado = 0L;
     boolean cambioEstado = false;

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
     if ( req.getParameter("id_estado_actual") == null ){throw new ParametroObligatorioException("id_estado_actual es nulo");}
     if ( req.getParameter("new_estado") == null ){throw new ParametroObligatorioException("new_estado es nulo");}
     
     
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     id_pedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si
     id_estado_actual = Long.parseLong(req.getParameter("id_estado_actual"));
     new_estado = Long.parseLong(req.getParameter("new_estado"));

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + id_pedido);

     /*
      * 3. Procesamiento Principal
      */
	BizDelegate bizDelegate = new BizDelegate();
	List lstEstados = bizDelegate.getEstadosPedidoBOC();
	
	ForwardParameters fp = new ForwardParameters();
	fp.add( req.getParameterMap() );     
    try{
        
        if (new_estado == 7 && id_estado_actual == 6){
            cambioEstado = bizDelegate.setModEstadoPedido(id_pedido, new_estado);            
        }else if (new_estado == 7){
            if (bizDelegate.DelTrxMP(id_pedido)){
                cambioEstado = bizDelegate.setModEstadoPedido(id_pedido, new_estado);
            }
        }
//        else if (new_estado == 8){
//            cambioEstado = bizDelegate.setModEstadoPedido(id_pedido, new_estado);
//        }
		if (cambioEstado) {
		    EstadoDTO EstadoNuevo = new EstadoDTO();
		    EstadoDTO EstadoActual = new EstadoDTO();
		    for (int i=0; i < lstEstados.size(); i++){
		        EstadoDTO est = (EstadoDTO)lstEstados.get(i);
		        if (est.getId_estado() == id_estado_actual){
		            EstadoActual = est;
		        }
		        if (est.getId_estado() == new_estado){
		            EstadoNuevo = est;
		        }
		    }
		    
			//escribe en el log la liberacion
			LogPedidoDTO log = new LogPedidoDTO();
			log.setUsuario(usr.getLogin());
			log.setId_pedido(id_pedido);
			//log.setId_motivo(paramId_motivo);
			log.setLog("Se modifico el estado del pedido de <font color='#0033CC'><b>'" + EstadoActual.getNombre() + "'</b></font> a <font color='#FF0000'><b>'" + EstadoNuevo.getNombre() + "'</b></font>.");
			bizDelegate.addLogPedido(log);
			fp.add( "mensaje" , mensaje_exito );
			fp.add( "ret" , "1" );
			url = paramUrl+fp.forward();
        }else{
			logger.debug("No se pudo realizar los cambios.");
			fp.add( "mensaje" , mensaje_fracaso );
			fp.add( "ret" , "0" );
			url = paramUrl+fp.forward()+"&mensaje="+mensaje_fracaso+"&ret=0";
        }
     }catch (Exception ex){
    	    String UrlError = paramUrl;
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				logger.error("Id del pedido es inválido");
				fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO );
			}
			if(ex.getMessage().equals(Constantes._EX_ESTADO_INVALIDO)){
				logger.error("Id del estado es inválido");
				fp.add( "rc" , Constantes._EX_ESTADO_INVALIDO );
			}
			fp.add( "mensaje" , mensaje_fracaso );
			fp.add( "ret" , "0" );
			url = UrlError + fp.forward();
     }
	 
     // 4. Redirecciona salida
     res.sendRedirect(url);

 }//execute

}
