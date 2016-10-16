package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;

/**
 * Listado de comunas para formulario de direcciones de despacho
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ComunasList extends Command {

	/**
	 * Lista de comunas para la región seleccionada
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta
		long reg_id = Long.parseLong(arg0.getParameter("reg_id"));
		//20121005_GS
		// List comunas = biz.regionesGetAllComunas( reg_id );
		List comunas = biz.comunasConCoberturaByRegion( reg_id );
		//-20121005_GS
		this.getLogger().info("Comunas para region "+reg_id+":"+comunas.size());
		
		String strcomunas= "";
		for (int i = 0; i < comunas.size(); i++) {
			ComunaDTO com = (ComunaDTO) comunas.get(i);
			strcomunas = strcomunas +  com.getId() + "-" + com.getNombre() + "|";
		}
		out.print( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Jumbo.cl</title></head><BODY onLoad='top.procom()'><form name=frmcom action=\"\"><textarea  name=com>"+strcomunas+"</textarea></form></BODY></HTML>" );

	}

}