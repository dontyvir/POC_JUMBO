package cl.bbr.fo.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;


/**
 * Trae los datios de una dirección de despacho para un cliente
 *  
 * @author carriagada it4b
 *  
 */
public class AjaxTraeDireccion extends Command {

    /**
     * Trae los datos de una dirección de despacho para un cliente
     * 
     * @param arg0  Request recibido desde el navegador
     * @param arg1  Response recibido desde el navegador
     * @throws Exception
     */
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        try {
        	
			//AjaxTraeDireccion
			HttpSession session = arg0.getSession();
			
			//Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            
			//Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
        
			String setDirSession = arg0.getParameter("setDirSession");
        
	        if(setDirSession != null){//Setea direccion de despacho session.
	        	String tipo = arg0.getParameter("tipo");
	        	if("D".equals(tipo)){
	        		if (arg0.getParameter("idDir") != null){
	        			long id_dir = 0;
	        			try{id_dir = Long.parseLong(arg0.getParameter("idDir").toString());}catch (NumberFormatException e) {} 
	        			//long idDirSession = (session.getAttribute("ses_dir_id")!= null)?Long.parseLong(session.getAttribute("ses_dir_id").toString()):0;
			            //if(id_dir != 0 && idDirSession != id_dir){
	        			if(id_dir != 0){
				            DireccionesDTO direccion = biz.clienteGetDireccion(id_dir);
			        		session.setAttribute("ses_comuna_cliente", ""+direccion.getReg_id()+"-=-"+direccion.getCom_id()+"-=-"+direccion.getCom_nombre());
			        			
			 	            /*Setea direccion a la session del cliente.*/
			 	      		session.setAttribute("ses_zona_id", String.valueOf(direccion.getZona_id()));
			 	      		session.setAttribute("ses_loc_id", String.valueOf(direccion.getLoc_cod()));
			 	      		session.setAttribute("ses_dir_id", String.valueOf(direccion.getId()));
			 	      		session.setAttribute("ses_dir_alias", direccion.getAlias());                        
			 	      		//session.setAttribute("ses_forma_despacho", "N");
			            }
	        		}
	 	      		
	        	}else if("R".equals(tipo)){
	        		if (arg0.getParameter("idZona") != null){
						long idZona = 0;
						try{idZona = Long.parseLong(arg0.getParameter("idZona").toString());}catch (NumberFormatException e) {} 
						
						if(idZona != 0){					 
							boolean esRetiroLocal = biz.zonaEsRetiroLocal(idZona);				
							if(esRetiroLocal){
								ZonaDTO zona = biz.getZonaDespachoById(idZona);
							 
								session.setAttribute("cdd_is_checked_rd", String.valueOf("false"));
								session.setAttribute("cdd_is_checked_rr", String.valueOf("true"));
								
								session.setAttribute("ses_loc_id", String.valueOf( zona.getId_local() ));
								session.setAttribute("ses_zona_id", String.valueOf( zona.getId_zona()));  							 
							} 
						}
	            	 }
	        	}else{
	        		
	        	}
	        	
	        }else{
                        
	            long id_dir = Long.parseLong(arg0.getParameter("id_dir").toString());
	            
	            DireccionesDTO direccion = biz.clienteGetDireccion(id_dir);
	            
	           
	            //ComunaDTO com = biz.getComunaConLocal(direccion.getCom_id());
	            session.setAttribute("ses_comuna_cliente", ""+direccion.getReg_id()+"-=-"+direccion.getCom_id()+"-=-"+direccion.getCom_nombre());
	
	            /*Setea direccion a la session del cliente.*/
	      		session.setAttribute("ses_zona_id", String.valueOf(direccion.getZona_id()));
	      		session.setAttribute("ses_loc_id", String.valueOf(direccion.getLoc_cod()));
	      		session.setAttribute("ses_dir_id", String.valueOf(direccion.getId()));
	      		session.setAttribute("ses_dir_alias", direccion.getAlias());                        
	      		//session.setAttribute("ses_forma_despacho", "N");
	          		
	            arg1.setContentType("text/xml");
	            arg1.setHeader("Cache-Control", "no-cache");
	            arg1.setCharacterEncoding("UTF-8");
	            
	            String result = "<respuesta>";
	            result += "<tipocalle>" + direccion.getTipo_calle() + "</tipocalle>";
	            result += "<calle>" + direccion.getCalle() + "</calle>";
	            result += "<numero>" + direccion.getNumero() + "</numero>";
	            result += "<departamento>" + direccion.getDepto() + "</departamento>";
	            result += "<region>" + direccion.getReg_id() + "</region>";
	            result += "<comuna>" + direccion.getCom_id() + "</comuna>";
	            result += "<alias>" + direccion.getAlias() + "</alias>";
	            result += "</respuesta>";
            
	            out.print(result);        	
	        }        

      } catch (Exception e) {
          this.getLogger().error(e);
          e.printStackTrace();
          throw new CommandException( e );
      }  
    }

}
