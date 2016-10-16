package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.bolsas.dto.BitacoraDTO;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewMonitorBolsas extends Command {
	
	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String param_fecha       = "";
		String fecha             = "";
		Date   hoy               = new Date();
		String anterior          = "";
		String siguiente         = "";
		String fecha_jor         = "";
		int id_local			 = 0;
		
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("fecha") != null ){
			param_fecha = req.getParameter("fecha");
		}
		
		
		if( req.getParameter("id_sucursal") != null ){
			id_local = Integer.parseInt(req.getParameter("id_sucursal"));
		}
		
		
		
		// 3. Template
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		BizDelegate bizDelegate = new BizDelegate();
		
		
		
		// 4. listado stock de bolsas
		List datosStock = bizDelegate.getStockBolsasRegalo(id_local+"");
		List stockdebolsas = new ArrayList();
		
		for(int i = 0; i < datosStock.size(); i++){
			IValueSet fila = new ValueSet();
			BolsaDTO bolsa = (BolsaDTO)datosStock.get(i);
			
			fila.setVariable("{cod_bolsa}"   , bolsa.getCod_bolsa());
			fila.setVariable("{desc_bolsa}"   , bolsa.getDesc_bolsa());
			fila.setVariable("{id_producto}"   , bolsa.getId_producto()+"");
			fila.setVariable("{num_stock_bolsa}"   , bolsa.getStock()+"");
			fila.setVariable("{cod_barra_bolsa}"   , bolsa.getCod_barra_bolsa());
			
			if(bolsa.getAct().equals("S"))
			  top.setVariable("{activada}"   , "ACTIVADO");
			else
			  top.setVariable("{activada}"   , "DESACTIVADO");
			
			if(	!bolsa.getCod_bolsa().equalsIgnoreCase("0")
					&& !bolsa.getCod_bolsa().equalsIgnoreCase("1")
					&& !bolsa.getCod_bolsa().equalsIgnoreCase("2")){
				fila.setVariable("{eliminar}", "<img border=\"0\" src=\"img/publish_x.png\" />");
			}else{
				fila.setVariable("{eliminar}", "");
			}
			
			fila.setVariable("{id_sucursal}"   , id_local+"");
			
			stockdebolsas.add(fila);
		}
		
		top.setDynamicValueSets("listado", stockdebolsas);
		
		// 5. bitcora
		List datosBitacora = bizDelegate.getBitacoraBolsasRegalo(usr.getId_local()+"");
		List bitacorabolsas = new ArrayList();
		
		for(int i = 0; i < datosBitacora.size(); i++){
			IValueSet fila = new ValueSet();
			
			BitacoraDTO bitacoraReg = (BitacoraDTO)datosBitacora.get(i);
			
			fila.setVariable("{cod_operacion}"   , bitacoraReg.getCod_operacion());
			fila.setVariable("{desc_operacion}"   , bitacoraReg.getDesc_operacion());
			fila.setVariable("{fecha_operacion}"   , bitacoraReg.getFecha_operacion());
			fila.setVariable("{hora_operacion}"   , bitacoraReg.getHora_operacion());
			fila.setVariable("{usuario_operacion}"   , bitacoraReg.getUsuario_operacion());
			
			bitacorabolsas.add(fila);
		}
		
		top.setDynamicValueSets("bitacora", bitacorabolsas);
		
		// 6. locales
		List lst_loc = null;	
		lst_loc = bizDelegate.getLocales(usr.getId_local()+"");
		ArrayList locs = new ArrayList();
		
		for (int i = 0; i < lst_loc.size(); i++) {
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{id_loc}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{local}",loc1.getNom_local());
			
			if (id_local==loc1.getId_local())
				fila.setVariable("{sel_loc}", "selected");
			else
				fila.setVariable("{sel_loc}", "");
			locs.add(fila);
		}
		
		top.setDynamicValueSets("LOCALES", locs);
		
		
		
		// 7. variables del header
		if( req.getParameter("result_validacion") != null && !req.getParameter("result_validacion").equals("")){
			top.setVariable("{result_validacion}", req.getParameter("result_validacion"));
		}
		if( req.getParameter("respuesta_ok") != null && !req.getParameter("respuesta_ok").equals("")){
			top.setVariable("{respuesta_ok}", req.getParameter("respuesta_ok"));
		}
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}", usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());
		top.setVariable("{nom_sucursal}", usr.getLocal());
		
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
