package cl.bbr.boc.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.pedidos.dto.JDespachoTrackingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega los codigos de barra
 * @author BBR
 *
 */
public class ViewHorasDespachoAjax extends Command {

 

 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {
	 	String fecha ="";
	 	String h_despacho  = "";
	 	int local = 0;
	 	Date   hoy = new Date();
	 	
	 	if (req.getParameter("fecha") != null){
	 		fecha = req.getParameter("fecha");
	 	}else{
	 		fecha = new SimpleDateFormat("yyyy-MM-dd").format(hoy);
	 	}
	 	if (req.getParameter("hora_desp") != null){
	 		h_despacho = req.getParameter("hora_desp");
	 	}
	 	
	 	local = Integer.parseInt(req.getParameter("local"));
	 	
	 	String result = "";
		View salida = new View(res);
		BizDelegate biz = new BizDelegate();
		
//		4.5 Hora Despacho
		List listahoras = biz.getJornadasDespachoByFecha(fecha, local);

		Set hora_completa = new TreeSet();
		for(int i= 0; i< listahoras.size();i++){
			JDespachoTrackingDTO hora1 = (JDespachoTrackingDTO) listahoras.get(i);
			String hora_comp = hora1.getH_ini()+"-"+hora1.getH_fin();
			hora_completa.add(hora_comp);
		}
		logger.debug("lista de horas: "+listahoras.size());

		long contador = 0;
		for( Iterator it = hora_completa.iterator(); it.hasNext();) {	
			if (contador > 0)
				result += "#";
			String h_total =(String)it.next();
			logger.debug("h_total: "+h_total);
			result +=  h_total ;
			contador++;
		}
		logger.debug("result: " + result);
		salida.setHtmlOut(result);
		salida.Output();
	}
}