package cl.bbr.boc.command;

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
 * Comando que agrega un nuevo producto generico
 * 
 * @author bbr
 * 
 */
public class CreateCuponDescuento extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {

		// 1. seteo de Variables
		View salida = new View(res);
		String codigo = "";
		String descuento = "";
		String cantidad = "";
		String fecha_ini = "";
		String fecha_fin = "";
		String rad_MedioPago = "";
		String rad_Publico = "";
		String id_cup_dto = "";
		
		boolean saveCupon = false;
		boolean cuponExiste = true;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		logger.debug("User: " + usr.getLogin());
		
		String UrlResponse = getServletConfig().getInitParameter("UrlResponse");
		logger.debug("UrlResponse: " + UrlResponse);
		
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		// 3. Template
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

	
		if (req.getParameter("codigo") != null) {
			codigo = req.getParameter("codigo").toUpperCase().trim();
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
		if (req.getParameter("rad_MedioPago") != null) {
			rad_MedioPago = req.getParameter("rad_MedioPago");
		}
		if (req.getParameter("id_cup_dto") != null) {
			id_cup_dto = req.getParameter("id_cup_dto");
		}
		if (req.getParameter("rad_Publico") != null) {
			rad_Publico = req.getParameter("rad_Publico");
		}
		
		
		BizDelegate biz = new BizDelegate();
		CuponEntity cupon = new CuponEntity();
		CuponEntity cuponE = new CuponEntity();
		
		
		if (!codigo.equals("") && !descuento.equals("") && !cantidad.equals("") && !fecha_ini.equals("") && !fecha_fin.equals("") && !rad_MedioPago.equals("")) {

				if(!id_cup_dto.equals("")){ 
				
					cupon.setId_cup_dto(Long.parseLong(id_cup_dto));
					CuponEntity ce = new CuponEntity();
					ce = biz.getDatoCuponPorId(cupon);
					cupon.setTipo(ce.getTipo());
				}
				else
					cupon.setTipo("");
					
				
				 cupon.setCodigo(codigo);
				 cupon.setDescuento(Integer.parseInt(descuento));
				 cupon.setCantidad(Integer.parseInt(cantidad));
				 cupon.setFecha_ini(Formatos.formatFecha(fecha_ini));
				 cupon.setFecha_fin(Formatos.formatFecha(fecha_fin));
				 cupon.setMedio_pago(Integer.parseInt(rad_MedioPago));
				 
				 if(rad_Publico.equals(""))
					 cupon.setPublico(1);
				 else
					 cupon.setPublico(Integer.parseInt(rad_Publico));
						 
				 cupon.setId_usuario(usr.getId_usuario()); 
				 
				 cuponE =  biz.getDatoCuponPorId(cupon);
				 
				if(cuponE != null)
					cupon.setDespacho(cuponE.getDespacho());
				 	
				if(!biz.setGuardarCupon(cupon)){
				
					saveCupon = false;
				
				}else {
					
					saveCupon = true;
					if(!rad_Publico.equals(""))
						res.sendRedirect(UrlResponse);
				}
		}
		else {
			
			if (!codigo.equals(""))
				top.setVariable("{codigo}", codigo);
			else 
				top.setVariable("{codigo}", "");
			
			if (!descuento.equals(""))
				top.setVariable("{descuento}", descuento);
			else
				top.setVariable("{descuento}", "");
			
			if (!cantidad.equals(""))
				top.setVariable("{cantidad}", cantidad);
			else
				top.setVariable("{cantidad}", "");
			
			if (!fecha_ini.equals(""))
				top.setVariable("{fecha_ini}", fecha_ini);
			else
				top.setVariable("{fecha_ini}", "");
			
			if (!fecha_fin.equals(""))
				top.setVariable("{fecha_fin}", fecha_fin);
			else 
				top.setVariable("{fecha_fin}", "");
			
			if(rad_MedioPago.equals("1"))
				top.setVariable("{check_1}", "checked");
			else if(rad_MedioPago.equals("2"))
				top.setVariable("{check_2}", "checked");
			else if(rad_MedioPago.equals("0"))
				top.setVariable("{check_3}", "checked");
			else
				top.setVariable("{check_3}", "");
			
				top.setVariable("{id_cup_dto}", "");
				top.setVariable("{check_4}", "none");
				top.setVariable("{check_5}", "none");
				top.setVariable("{check_6}", "none");
				top.setVariable("{check_7}", "none");
				top.setVariable("{check_8}", "none");
				top.setVariable("{check_9}", "block");
				top.setVariable("{check_10}", "none");
				
				top.setVariable("{mensaje}", "faltan datos por completar");
				
				cuponExiste = false;
			
		}
		
		if(saveCupon){
			
				cupon.setCodigo(codigo);
				cupon = biz.getDatoCuponPorCodigo(cupon);
		
				top.setVariable("{id_cup_dto}", String.valueOf(cupon.getId_cup_dto()));
				top.setVariable("{codigo}", cupon.getCodigo());
				top.setVariable("{descuento}", String.valueOf(cupon.getDescuento()));
				top.setVariable("{cantidad}", String.valueOf(cupon.getCantidad()));
				top.setVariable("{fecha_ini}", Formatos.frmFecha(cupon.getFecha_ini()));
				top.setVariable("{fecha_fin}", Formatos.frmFecha(cupon.getFecha_fin()));
				
				
				if(cupon.getMedio_pago() == 1)
					top.setVariable("{check_1}", "checked");
				else if(cupon.getMedio_pago() == 2)
					top.setVariable("{check_2}", "checked");
				else
					top.setVariable("{check_3}", "checked");
				
				
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
					top.setVariable("{mensaje}", "");
					
					
			
		}else if(!saveCupon && cuponExiste){
			
			if (!codigo.equals(""))
				top.setVariable("{codigo}", codigo);
			else 
				top.setVariable("{codigo}", "");
			
			if (!descuento.equals(""))
				top.setVariable("{descuento}", descuento);
			else
				top.setVariable("{descuento}", "");
			
			if (!cantidad.equals(""))
				top.setVariable("{cantidad}", cantidad);
			else
				top.setVariable("{cantidad}", "");
			
			if (!fecha_ini.equals(""))
				top.setVariable("{fecha_ini}", fecha_ini);
			else
				top.setVariable("{fecha_ini}", "");
			
			if (!fecha_fin.equals(""))
				top.setVariable("{fecha_fin}", fecha_fin);
			else 
				top.setVariable("{fecha_fin}", "");
			
			if(rad_MedioPago.equals("1"))
				top.setVariable("{check_1}", "checked");
			else if(rad_MedioPago.equals("2"))
				top.setVariable("{check_2}", "checked");
			else if(rad_MedioPago.equals("0"))
				top.setVariable("{check_3}", "checked");
			else
				top.setVariable("{check_3}", "");
			
				top.setVariable("{id_cup_dto}", "");
				top.setVariable("{check_4}", "none");
				top.setVariable("{check_5}", "none");
				top.setVariable("{check_6}", "none");
				top.setVariable("{check_7}", "none");
				top.setVariable("{check_8}", "none");
				top.setVariable("{check_9}", "block");
				top.setVariable("{check_10}", "none");
				
				top.setVariable("{mensaje}", "Cupón ya existe.");
				
		}
		
				top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
				Date now = new Date();
				top.setVariable("{hdr_fecha}"	,now.toString());
		
				String result = tem.toString(top);
				salida.setHtmlOut(result);
				salida.Output();

	}

}
