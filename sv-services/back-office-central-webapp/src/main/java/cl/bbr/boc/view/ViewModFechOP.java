package cl.bbr.boc.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Servlet implementation class ViewModFechOP
 */
public class ViewModFechOP extends Command {
	private static final long serialVersionUID = 1L;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	paramMod 		= "";
		String param_id_pedido  = "";
		String  mensajeSistema	= "";
		long 	id_pedido		= -1;
		String rc               = "";
		String 	paramFecha 		= "";
		String fechaOld="";
		boolean resultado =false;
        int origen = 0; //Origen 1 = Monitor de despacho
        logger.debug("User: " + usr.getLogin());
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		// 2. Procesa parámetros del request
		
		paramMod = req.getParameter("mod");
		
		// 2.0 parámetro msg
		if ( req.getParameter("msg") != null )
			mensajeSistema = req.getParameter("msg");
		if (req.getParameter("rc") != null ) 
            rc = req.getParameter("rc");
        if (req.getParameter("origen") != null ) {
            try {
                origen = Integer.parseInt(req.getParameter("origen"));    
            } catch (Exception e) {}            
        }
		// 2.1 parámetro id_pedido
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}

		param_id_pedido = req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);
		logger.debug("id_pedido: " + param_id_pedido);
	

		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		// 4.1 Obtenemos información del pedido
		
		PedidoDTO pedido = bizDelegate.getPedidosById(id_pedido); 
		Date fecha = new Date();	
		
		if ("1".equals(paramMod) && req.getParameter("id_pedido") != null && req.getParameter("fecha") != null) {
			
			try {
				fechaOld=req.getParameter("fecha_old");
				fecha = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("fecha"));
				paramFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha); 
				resultado= bizDelegate.modFechaOP(id_pedido, paramFecha, fechaOld,usr.getLogin());
				if (!resultado) {
					mensajeSistema = "Error al modificar fecha de ingreso de OP. ";
				}else{
					mensajeSistema = "Fecha de ingreso modificada satisfactoriamente";
				}
				ArrayList btns = new ArrayList();
		        IValueSet fila = new ValueSet();
		        fila.setVariable("{id_pedido}", param_id_pedido);
		        btns.add(fila);
				top.setDynamicValueSets("BOTONES_MON_PEDIDO", btns);
				top.setVariable( "{mns}"	, mensajeSistema);
				top.setVariable("{fecha}",paramFecha);
				top.setVariable("{id_pedido}"    , String.valueOf(id_pedido));
				top.setVariable("{fechaCreacion}", paramFecha );
				top.setVariable("{fecha_old}",fechaOld);
				
			} catch (Exception e) {
				logger.error("Error: ", e);
				mensajeSistema = "Error al modificar fecha de ingreso de OP. ";
			}

		}else{
	        // 5. Setea variables del template
			String strFecha;
			mensajeSistema="";
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse( pedido.getFingreso().toString());
			strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha); 
			
			
			top.setVariable("{fecha}",strFecha);
			top.setVariable("{id_pedido}"    , String.valueOf(id_pedido));
			top.setVariable("{mns}"	, mensajeSistema);
			top.setVariable("{fechaCreacion}", Formatos.frmFecha( pedido.getFingreso().toString()) );
			top.setVariable("{fecha_old}",Formatos.frmFecha( pedido.getFingreso().toString()) );
		}
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		
	}




 
}
