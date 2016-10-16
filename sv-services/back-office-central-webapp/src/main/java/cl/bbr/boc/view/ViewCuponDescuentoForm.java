package cl.bbr.boc.view;

import java.util.Date;

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
/**
 * formulario para el ingreso de nuevos productos al monitor de productos
 * @author BBRI
 */
public class ViewCuponDescuentoForm extends Command {
	private final static long serialVersionUID = 1;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		String id_cup_dto = "";
		String codigo = "";
		String descuento = "";
		String cantidad = "";
		String fecha_ini = "";
		String fecha_fin = "";
	   
		if (req.getParameter("id_cup_dto") != null) {
			id_cup_dto = req.getParameter("id_cup_dto");
		}
		if (req.getParameter("codigo") != null) {
			codigo = req.getParameter("codigo");
		}
		if (req.getParameter("descuento") != null) {
			descuento = req.getParameter("descuento");
		}
		if (req.getParameter("cantidad") != null) {
			cantidad = req.getParameter("cantidad");
		}
		if (req.getParameter("fec_ini") != null) {
			fecha_ini = req.getParameter("fec_ini");
		}
		if (req.getParameter("fec_fin") != null) {
			fecha_fin = req.getParameter("fec_fin");
		}
		
		
		if(id_cup_dto.equals("")){
			
			top.setVariable("{codigo}"	,codigo);
			top.setVariable("{id_cup_dto}"	,"");
			top.setVariable("{descuento}"	,descuento);
			top.setVariable("{cantidad}"	,cantidad);
			top.setVariable("{fecha_ini}"	,fecha_ini);
			top.setVariable("{fecha_fin}"	,fecha_fin);
			top.setVariable("{check_4}", "none");
			top.setVariable("{check_7}", "none");
			top.setVariable("{check_8}", "none");
			top.setVariable("{check_9}", "block");
			top.setVariable("{check_10}", "none");
			top.setVariable("{mensaje}"	,"");
			
		}else if(!id_cup_dto.equals("")) {
				
				CuponEntity cup = new CuponEntity();
				CuponEntity cupon = new CuponEntity();
				BizDelegate biz = new BizDelegate();
				
				cup.setId_cup_dto(Integer.parseInt(id_cup_dto));
				cupon = biz.getDatoCuponPorId(cup);
				
				top.setVariable("{codigo}"	,cupon.getCodigo());
				top.setVariable("{id_cup_dto}"	,String.valueOf(cupon.getId_cup_dto()));
				top.setVariable("{descuento}"	,String.valueOf(cupon.getDescuento()));
				top.setVariable("{cantidad}"	,String.valueOf(cupon.getCantidad()));
				top.setVariable("{fecha_ini}"	,Formatos.frmFecha(cupon.getFecha_ini()));
				top.setVariable("{fecha_fin}"	,Formatos.frmFecha(cupon.getFecha_fin()));
				
				
				if (cupon.getMedio_pago() == 1) {
					top.setVariable("{check_1}", "checked");
				}else if(cupon.getMedio_pago() == 2){
					top.setVariable("{check_2}", "checked");
				}else{
					top.setVariable("{check_3}", "checked");
				}
				
				
				
				if(cupon.getPublico() == 1) {
					top.setVariable("{check_4}", "block");
					top.setVariable("{check_5}", "checked");
					top.setVariable("{check_7}", "none");
				}else{
					top.setVariable("{check_4}", "block");
					top.setVariable("{check_6}", "checked");
					top.setVariable("{check_7}", "block");
				}
				
				top.setVariable("{check_8}", "block");
				
				top.setVariable("{fec_crea}"	,Formatos.frmFecha(String.valueOf(cupon.getFecha_ins())));
				top.setVariable("{fec_act}"		,Formatos.frmFecha(String.valueOf(cupon.getFecha_upd())));
				top.setVariable("{usu_act}"		,cupon.getNombre_usuario());
				
				top.setVariable("{check_9}", "none");
				top.setVariable("{check_10}", "block");
				top.setVariable("{mensaje}"	,"");
		}
		
		
		// 6. Setea variables del template
				// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
