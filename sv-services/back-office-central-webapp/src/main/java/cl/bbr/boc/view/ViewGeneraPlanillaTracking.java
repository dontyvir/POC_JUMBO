package cl.bbr.boc.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.JDespachoTrackingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Llena el popup de direcciones
 * @author BBRI 
 */
public class ViewGeneraPlanillaTracking extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		logger.debug("User: " + usr.getLogin());
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		
		// 2. Procesa parámetros del request
		/*if (req.getParameter("idcliente") != null){
			id_cliente = Integer.parseInt(req.getParameter("idcliente"));
		}else{
			id_cliente = 0;
		}*/
		
		//long local = usr.getId_local();
		
		/*if (req.getParameter("mje_ok") != null){
			mje_ok = Integer.parseInt(req.getParameter("mje_ok"));
		}*/
		
		//3. Template
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas

		// 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();
		//Recupera Datos de Direccion segun  Id Cliente
		
		
        //Listado de Locales
		ArrayList locales = new ArrayList();
		List listLocales = bizDelegate.getLocales();

		if ( listLocales.size() > 0 ){
			for (int i = 0; i < listLocales.size(); i++) {
				IValueSet fila_loc = new ValueSet();
				LocalDTO loc = (LocalDTO) listLocales.get(i);
				
				fila_loc.setVariable("{id_local}", String.valueOf(loc.getId_local()));
				fila_loc.setVariable("{nom_local}", String.valueOf(loc.getNom_local()));
				/*if (local != -1 && local == loc.getId_local()) {
				    fila_loc.setVariable("{sel_loc}", "selected");
				} else {*/
				    //fila_loc.setVariable("{sel_loc}", "");
				//}
				locales.add(fila_loc);
		
				logger.debug("Esto es lo que devuelve listLoc " + loc.getNom_local() );
			}
		}
		
		//Listado de Horas de despacho
		String fechaDesp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List listahoras = bizDelegate.getJornadasDespachoByFecha(fechaDesp, 1);
		ArrayList horas = new ArrayList();
		
		Set hora_completa = new TreeSet();
		for(int i= 0; i< listahoras.size();i++){
		    JDespachoTrackingDTO hora1 = (JDespachoTrackingDTO) listahoras.get(i);
			String hora_comp = hora1.getH_ini()+"-"+hora1.getH_fin();
			hora_completa.add(hora_comp);
		}
        
		for( Iterator it = hora_completa.iterator(); it.hasNext();) {
			IValueSet fila = new ValueSet();
			String h_total =(String)it.next();
			logger.debug("h_total: "+h_total);
			fila.setVariable("{h_total}"	,h_total);
			horas.add(fila);
		}
		
		
		/*if (mje_ok == 1)
			top.setVariable("{mensaje}","Dirección modificada satisfactoriamente");
		else if (mje_ok == 2)
			top.setVariable("{mensaje}","No se pudo modificar dirección");
		else
			top.setVariable("{mensaje}","");*/
		
		//top.setVariable("{id_cliente}",String.valueOf(id_cliente) );
		//top.setVariable("{id_local}",local+"");
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date fecha = new Date();
		top.setVariable("{hdr_fecha}", fecha.toString());

		// 5 Paginador

		// 6. Setea variables bloques
		top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("HORA_DESPACHO", horas);

		// 7. Salida Final
		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();
	}
}