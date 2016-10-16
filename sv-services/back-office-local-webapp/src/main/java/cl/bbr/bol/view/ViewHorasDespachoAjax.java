package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO;
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
	 	Date   hoy = new Date();
	 	
	 	if (req.getParameter("fecha") != null){
	 		fecha = req.getParameter("fecha");
	 	}else{
	 		fecha = new SimpleDateFormat("yyyy-MM-dd").format(hoy);
	 	}
	 	if (req.getParameter("hora_desp") != null){
	 		h_despacho = req.getParameter("hora_desp");
	 	}
	 	String result = "";
		View salida = new View(res);
		BizDelegate biz = new BizDelegate();
//		4.5 Hora Despacho
		DespachoCriteriaDTO cri = new DespachoCriteriaDTO();
		cri.setF_despacho(fecha);
		cri.setId_local(usr.getId_local());
		cri.setRegsperpag(10000);
		List listahoras = biz.getDespachosByCriteria(cri);
	//	ArrayList horas = new ArrayList();
		Set hora = new TreeSet();
		Set hora_completa = new TreeSet();
		for(int i= 0; i< listahoras.size();i++){			
			MonitorDespachosDTO hora1 = new MonitorDespachosDTO();
			hora1 = (MonitorDespachosDTO) listahoras.get(i);
			hora.add(hora1.getH_ini());
			String hora_comp = hora1.getH_ini()+"-"+hora1.getH_fin();
			hora_completa.add(hora_comp);

		}
		logger.debug("lista de horas: "+listahoras.size());
		//String h_ini = "";
	//	String h_total = "";
		Iterator it2 = hora_completa.iterator();
		long contador = 0;
		//if (cant_horas > 0){
			for( Iterator it = hora.iterator(); it.hasNext();) {	
				if (contador > 0)
					result += "#";
				
				String h_ini = (String)it.next();
				logger.debug("h_ini: "+h_ini);
				IValueSet fila = new ValueSet();
				String h_total =(String)it2.next();
				it2.hasNext();
				logger.debug("h_total: "+h_total);			
				fila.setVariable("{h_total}"	,h_total);
				fila.setVariable("{h_inicio}"	,h_ini);
				if (h_despacho != null
						&& h_despacho.equals(h_ini))
					fila.setVariable("{sel}", "selected");
				else
					fila.setVariable("{sel}", "");
				//if (contador == 0)
				//	result += h_ini + "|" + h_total+;
				//else
					result +=  h_ini + "|" + h_total ;
				contador++;
			//	horas.add(fila);
			}
		//}
		logger.debug("result: " + result);
		salida.setHtmlOut(result);
		salida.Output();
		
	}


}
