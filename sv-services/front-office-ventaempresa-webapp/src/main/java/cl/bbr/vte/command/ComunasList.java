package cl.bbr.vte.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.ComunaDTO;

/**
 * <p>Comando que permite entregar la lista de comunas de acuerdo a la región seleccionada.</p>
 * <p>El despliegue esta generado para ser procesado por javascript.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ComunasList extends Command {
	/**
	 * Despliegue del listado de comunas para la región recibida como parámetro
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
	throws Exception {

	    long reg_id = 0;
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();
		
		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();
		
		String region = arg0.getParameter("id_reg");
		
		if (region != null){
		    reg_id = Long.parseLong(region);
		}
		
		// recuperar identificador de la región de consulta
		
		
		// Recuperar tipo de comunas (G: general N: normal)
		String tipo_com = arg0.getParameter("tipo")+"";
		
		// Llamada al método correspondiente en el bizdelegate
		List comunas = null;
		if( tipo_com.equals("G") )
		    comunas = biz.getComunasGeneral();
		else
		    comunas = biz.regionesGetAllComunas( reg_id );
			
		String strcomunas= "";
		for (int i = 0; i < comunas.size(); i++) {
			ComunaDTO com = (ComunaDTO) comunas.get(i);
			if( com.getId_region() == reg_id )
				strcomunas = strcomunas +  com.getId_comuna() + "-" + com.getNombre() + "|";
		}
		out.print( "<HTML><BODY onLoad='top.procom()'><form name=frmcom><textarea  name=com>"+strcomunas+"</textarea></form></BODY></HTML>" );
		
		}

}