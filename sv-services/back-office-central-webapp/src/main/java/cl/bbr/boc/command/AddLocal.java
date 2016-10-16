package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.ctrl.LocalCtrl;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddCampana Comando Process
 * Agrega una Campaña
 * @author BBRI
 */

public class AddLocal extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 		="";
     //long id_local 		= 0L;
     String cod_local 		= "";
     String nom_local 		= "";
     int orden 				= 0;
     int cod_local_pos 		= 0;
     String tipo_flujo 		= "";
     String cod_local_promo = "";
     String tipo_picking    = "";
     String UrlError		= "";
     long id=0L;
     String direccion		= "";
     String retirolocal="N";//por defecto no es retiro local
     BizDelegate biz = new BizDelegate();
     long idZona =0;
     long comuna=0;
     long id_poligono = 0;
     ZonaDTO zona = new ZonaDTO();
     
     
     // 2. Procesa parámetros del request
     	logger.debug("Procesando parámetros...");
     
		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
		
     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("cod_local") == null ){throw new ParametroObligatorioException("cod_local es nulo");}
     if ( req.getParameter("nom_local") == null ){throw new ParametroObligatorioException("nom_local es nulo");}
     if ( req.getParameter("orden") == null ){throw new ParametroObligatorioException("orden es nulo");}
     if ( req.getParameter("cod_local_pos") == null ){throw new ParametroObligatorioException("cod_local_pos es nulo");}
     if ( req.getParameter("tipo_flujo") == null ){throw new ParametroObligatorioException("tipo_flujo es nulo");}
     if ( req.getParameter("cod_local_promo") == null ){throw new ParametroObligatorioException("cod_local_promo es nulo");} 
     if ( req.getParameter("tipo_picking") == null ){throw new ParametroObligatorioException("tipo_picking es nulo");} 
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("UrlError es null");}
     if ( req.getParameter("direccion") == null ){throw new ParametroObligatorioException("Falta Ingresar la direccion");}
     
     // 2.2 obtiene parametros desde el request
     paramUrl 		= req.getParameter("url");
     cod_local 		= req.getParameter("cod_local"); //string:obligatorio:si
     nom_local 		= req.getParameter("nom_local"); //string:obligatorio:si
     orden 			= Integer.parseInt(req.getParameter("orden")); //string:obligatorio:si
     cod_local_pos 	= Integer.parseInt(req.getParameter("cod_local_pos")); //string:obligatorio:si
     tipo_flujo		= req.getParameter("tipo_flujo"); //string:obligatorio:si    
     cod_local_promo = req.getParameter("cod_local_promo"); //string:obligatorio:si
     tipo_picking = req.getParameter("tipo_picking"); //string:obligatorio:si
     UrlError  		= req.getParameter("UrlError");
     direccion		= req.getParameter("direccion");
     if (req.getParameter("retirolocal")==null){
    	 retirolocal="N";
     }else{
    	 retirolocal=req.getParameter("retirolocal");//string:obligatorio:no
    	 comuna		= Long.valueOf(req.getParameter("comuna")).longValue();
    	//id_zona_retiro=Integer.parseInt(req.getParameter("zona"));
    	//modificamos la zona para que tenga orden 1000 (muestre al final la lista de zonas)      	
      	//LocalCtrl local = new LocalCtrl();
      	//local.doModZonaDespachoOrden(id_zona_retiro, 1000);
     }
     

     
     // 2.3 log de parametros y valores
     logger.debug("-----------Datos de Local Nuevo----------");
     logger.debug("url: " + paramUrl);
     logger.debug("cod_local: " + cod_local);
     logger.debug("nom_local: " + nom_local);
     logger.debug("orden: " + orden);
     logger.debug("cod_local_pos: " + cod_local_pos);
     logger.debug("tipo_flujo: " + tipo_flujo);
     logger.debug("cod_local_promo: " + cod_local_promo);
     logger.debug("tipo_picking: " + tipo_picking);
     logger.debug("urlerror: " + UrlError);
     logger.debug("retiro_local: "+ retirolocal);
     logger.debug("IdZona: "+ idZona);
     logger.debug("comuna: "+ comuna);
     logger.debug("direccion: "+ direccion);
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
     	ForwardParameters fp = new ForwardParameters();
 	    fp.add( req.getParameterMap() );
 	    
  	
  		LocalDTO dto = new LocalDTO();
  		
  		dto.setCod_local(cod_local);
  		dto.setNom_local(nom_local);
  		dto.setOrden(orden);
  		dto.setCod_local_pos(cod_local_pos);
  		dto.setTipo_flujo(tipo_flujo);
     	dto.setCod_local_promocion(cod_local_promo);
     	dto.setTipo_picking(tipo_picking);
     	dto.setRetirolocal(retirolocal);
     	dto.setId_zona_retiro((int) idZona);
     	if (retirolocal.equalsIgnoreCase("S")){
     		dto.setRetirolocal("S");
     	}else
     		dto.setRetirolocal("N");
     	dto.setDireccion(direccion);
  		
  		try{
  			int cod_nuevo_local = biz.doAddLocalWithZone(dto);
  			if (retirolocal.equalsIgnoreCase("S")){
  				//datos por defecto para la creacion de la zona
  				 zona.setNombre("Z8 Retiro Local "+ nom_local.replaceAll("Jumbo", ""));
  		    	 zona.setOrden(1000);
  		    	 zona.setId_local(cod_nuevo_local);
  		    	 zona.setDescripcion("Zona Retiro en Local");
  		    	 zona.setTarifa_economica(990);
  		    	 zona.setTarifa_express(990);
  		    	 zona.setTarifa_normal_alta(990);
  		    	 zona.setTarifa_normal_baja(990);
  		    	 zona.setTarifa_normal_media(990);
  		    	 zona.setRegalo_clientes(1);
  		    	 zona.setEstado_descuento_cat(3);
  		    	 zona.setEstado_descuento_par(3);
  		    	 zona.setEstado_descuento_tbk(3);
  		    	 zona.setMonto_descuento_cat(120000);
  		    	 zona.setMonto_descuento_tbk(120000);
  		    	 zona.setMonto_descuento_par(120000);
  		    	 zona.setMonto_descuento_pc_cat(50000);
  		    	 zona.setMonto_descuento_pc_tbk(50000);
  		    	 zona.setMonto_descuento_pc_par(50000);
  		    	 LocalCtrl local= new LocalCtrl();
  		    	 idZona= local.doAgregaZonaDespacho(zona); 
  		    	 //modificamos el local para que se agregue la zona
  		    	 LocalDTO dtolocal = new LocalDTO();  		    	
  		    	 dtolocal.setId_local(cod_nuevo_local);
  		    	 dtolocal.setId_zona_retiro((int)idZona);
  		    	 dtolocal.setRetirolocal("S");
  		    	 biz.doModZonaLocal(dtolocal);
  		    	 PoligonoDTO pol=new PoligonoDTO();
  		    	 pol.setId_zona(idZona);
  		    	 pol.setDescripcion("Polígono 999 Retiro en Local "+ nom_local);
  		    	 pol.setNum_poligono(999);
  		    	 pol.setId_comuna(comuna);
  		    	 id_poligono = biz.addPoligonoWithZona(pol);
  			}
  			logger.debug("id_poligono"+id_poligono);
  			logger.debug("resultado?"+cod_nuevo_local);
  			
  			if(cod_nuevo_local>0){
  				fp.add( "emp_id" , String.valueOf(id));
  				fp.add( "msje" , mensaje_exito);
				paramUrl = paramUrl + fp.forward(); 
  			}else{
  				fp.add( "msje" , mensaje_fracaso);
  				paramUrl = UrlError + fp.forward();
  			}
  			
  			
  		} catch(BocException e){
  	  		
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
     		
			if ( e.getMessage()!=null &&  !e.getMessage().equals("")){
				logger.debug(e.getMessage());
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		
  		logger.debug("id:"+id);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}