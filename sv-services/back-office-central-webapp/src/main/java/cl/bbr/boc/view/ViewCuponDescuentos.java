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
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ViewCuponDescuentos extends Command {
	private final static long serialVersionUID = 1;
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
		int regsperpage;
		View salida = new View(res);
		//int cat_id = -1;
		
		String html;
		logger.debug("User: " + usr.getLogin());
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		
		// Recupera pagina desde web.xml
       
		html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		BizDelegate bizDelegate = new BizDelegate();

		ArrayList cupones = new ArrayList();
		List listTiposCupones = bizDelegate.getListaTiposCupones();

		logger.debug("4.2.1");
		
		for (int i = 0; i< listTiposCupones.size(); i++){
			IValueSet fila_tip = new ValueSet();
			CuponEntity cupon = (CuponEntity)listTiposCupones.get(i);
				
			fila_tip.setVariable("{id_cup_dto}", String.valueOf(cupon.getId_cup_dto()));
			fila_tip.setVariable("{codigo}"	, String.valueOf(cupon.getCodigo()));
			fila_tip.setVariable("{descuento}"	, String.valueOf(cupon.getDescuento()));
			fila_tip.setVariable("{stock}"	, String.valueOf(cupon.getCantidad()));
				
			if(cupon.getTipo().equals("P")) {
			
				fila_tip.setVariable("{tipo}"	, "Producto");
				fila_tip.setVariable("{check_2}", "block");
			
			}else if(cupon.getTipo().equals("D")) {
				
				fila_tip.setVariable("{tipo}"	, "Solo despacho");
				fila_tip.setVariable("{check_2}", "none");
			
			}else if(cupon.getTipo().equals("S")) {
				
				fila_tip.setVariable("{tipo}"	, "Seccion");
				fila_tip.setVariable("{check_2}", "block");
				
			}else if(cupon.getTipo().equals("R")) {
				
				fila_tip.setVariable("{tipo}"	, "Rubro");
				fila_tip.setVariable("{check_2}", "block");
				
			}else if(cupon.getTipo().equals("TS")){
					
				fila_tip.setVariable("{tipo}"	, "Todas las secciones");
				fila_tip.setVariable("{check_2}", "none");
				
			}else{
				
				fila_tip.setVariable("{tipo}"	, " ");
				fila_tip.setVariable("{check_2}", "block");
			}
				
				fila_tip.setVariable("{cantidad}"	, String.valueOf(cupon.getStock()));
				
			if(cupon.getDespacho() == 1) 
				fila_tip.setVariable("{despacho}"	, String.valueOf("Si"));
			else
				fila_tip.setVariable("{despacho}"	, String.valueOf("No"));
			
			if(cupon.getPublico() == 1) {
				
				fila_tip.setVariable("{publico}"	, String.valueOf("Si"));
				fila_tip.setVariable("{check_1}", "none");
			
			}else {
				
				fila_tip.setVariable("{publico}"	, String.valueOf("No"));
				fila_tip.setVariable("{check_1}", "block");
			}
			
			if(cupon.getMedio_pago() == 1)
				fila_tip.setVariable("{medio_pago}"	, String.valueOf("CAT"));
			else if(cupon.getMedio_pago() == 2)
				fila_tip.setVariable("{medio_pago}"	, String.valueOf("TBK"));
			else
				fila_tip.setVariable("{medio_pago}"	, String.valueOf("Todo"));
			
				fila_tip.setVariable("{fecha_ini}"	, Formatos.frmFecha(cupon.getFecha_ini()));
				fila_tip.setVariable("{fecha_fin}"	, Formatos.frmFecha(cupon.getFecha_fin()));		
				
				cupones.add(fila_tip);
		}
		
		
		if (listTiposCupones.size() < 1 )
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		else
			top.setVariable("{mje1}","");
		
			top.setDynamicValueSets("CUPONES",cupones);
	
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
	    
		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();		
	}
}
