package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
/**
 * formulario para cambiar el medio de pago
 * @author BBRI
 */

public class ViewMPOPCreForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String paramId_pedido = "";
		String mensaje 		  = "";
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());

        // Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		if ( req.getParameter("id_pedido") == null ){
			logger.error("Par�metro id_pedido es obligatorio");
			throw new ParametroObligatorioException("id_pedido es null");
		}
		
		paramId_pedido = req.getParameter("id_pedido");
		long id_pedido = Long.parseLong(paramId_pedido);

		if(req.getParameter("mensaje")!=null)
			mensaje = req.getParameter("mensaje");
		
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//datos asociados al Medio de Pago
		BizDelegate bizDelegate = new BizDelegate();
		PedidoDTO pedido =  bizDelegate.getPedidosById(id_pedido);
		
		
		top.setVariable("{nombre_titular}"	, pedido.getNom_cliente());
		top.setVariable("{rut_titular}"		, (pedido.getRut_tit()==null?"":pedido.getRut_tit())+"-"+(pedido.getDv_cliente()==null?"":pedido.getDv_cliente()));		
		top.setVariable("{titular}"			, (pedido.getNom_tit()==null?"":pedido.getNom_tit())+" "+(pedido.getApat_tit()==null?"":pedido.getApat_tit())+" "+(pedido.getAmat_tit()==null?"":pedido.getApat_tit()));
		top.setVariable("{tipo_pago}"		, pedido.getMedio_pago()); //JBM:Jumbo mas TBK:Tarjeta bancaria TPT:Tarjeta Paris Titular TPA:Tarjeta Paris Adicional
		top.setVariable("{num_mp1}"			, "");
		top.setVariable("{num_mp2}"			, "");
		top.setVariable("{num_mp3}"			, "");
		top.setVariable("{num_mp4}"			, "");
		
		top.setVariable("{tipo_TP_ant}"	, "");

		if (pedido.getNom_tbancaria() != null){
			if (pedido.getNom_tbancaria().equals("PARIS TITULAR")){
				top.setVariable("{tipo_TP_ant}"	, "1");
			}else if (pedido.getNom_tbancaria().equals("PARIS ADICIONAL")){
				top.setVariable("{tipo_TP_ant}"	, "0");
			}else{
				top.setVariable("{tipo_TP_ant}"	, "");
			}
		}
		 
		
/*		top.setVariable("{num_mp}", "");
		if(pedido.getMedio_pago().equals("CRE")){
			top.setVariable("{nom_tarj}", "");
			top.setVariable("{expira}"	, "");
		}
		
		if(pedido.getMedio_pago().equals("JBM") || pedido.getMedio_pago().equals("CAT")){
		  if(pedido.getNum_mp().length()>12){
			  top.setVariable("{num_mp1}", pedido.getNum_mp().substring(0,4));
			  top.setVariable("{num_mp2}", pedido.getNum_mp().substring(4,8));
			  top.setVariable("{num_mp3}", pedido.getNum_mp().substring(8,12));
			  top.setVariable("{num_mp4}", pedido.getNum_mp().substring(12));
		  }else{
			  top.setVariable("{num_mp}", pedido.getNum_mp());
		  }
		  top.setVariable("{nom_tarj}", "");
		  top.setVariable("{expira}"	, "");
		}
		if(pedido.getMedio_pago().equals("TBK")){
			top.setVariable("{num_mp}"	, pedido.getNum_mp());
			top.setVariable("{nom_tarj}", "<tr class='tabla2'><td>Nombre de Tarjeta Bancaria</td><td>"+pedido.getNom_tbancaria()+"</td></tr>");
			top.setVariable("{expira}"	, "<tr class='tabla2'><td>Expiraci�n</td><td>"+pedido.getFecha_exp()+"</td></tr>");
		}
		if(pedido.getMedio_pago().equals("PAR")){
			top.setVariable("{num_mp}"	, pedido.getNum_mp());
			top.setVariable("{nom_tarj}", "<tr class='tabla2'><td>Nombre de Tarjeta Bancaria</td><td>"+pedido.getNom_tbancaria()+"</td></tr>");
			//top.setVariable("{meses_librpago}", "<tr class='tabla2'><td>Mese Libres de Pago</td><td>"+String.valueOf(pedido.getMeses_librpago())+"</td></tr>");
			top.setVariable("{expira}"	, "");
		}

		top.setVariable("{num_cuotas}", String.valueOf(pedido.getN_cuotas()));
		top.setVariable("{num_doc}"   , String.valueOf(pedido.getNum_doc()));
		top.setVariable("{tipo_doc}"  , pedido.getTipo_doc());
		top.setVariable("{tb_banco}"  , pedido.getTb_banco());
*/		
		
		top.setVariable("{num_mp}", "");
		if(pedido.getMedio_pago().equals("CRE")){
			top.setVariable("{nom_tarj}", "");
			top.setVariable("{expira}"	, "");
			top.setVariable("{num_cuotas}", "");
		}else{
			top.setVariable("{num_cuotas}", "<tr class='tabla2'><td>N&deg; Cuotas</td><td>"+pedido.getN_cuotas()+"</td></tr>");
		}

		if(pedido.getMedio_pago().equals("JBM") || pedido.getMedio_pago().equals("CAT")){
		 // if(pedido.getNum_mp().length()>12){
			  /*top.setVariable("{num_mp1}", pedido.getNum_mp().substring(0,4));
			  top.setVariable("{num_mp2}", pedido.getNum_mp().substring(4,8));
			  top.setVariable("{num_mp3}", pedido.getNum_mp().substring(8,12));
			  top.setVariable("{num_mp4}", pedido.getNum_mp().substring(12));*/
		      top.setVariable("{num_mp}", "<tr class='tabla2'><td>N&deg; Tarjeta Actual</td><td>"+pedido.getNum_mp()+"</td></tr>");
		 // }else{
			//  top.setVariable("{num_mp}", pedido.getNum_mp());
		  //}
		  top.setVariable("{nom_tarj}", "");
		  top.setVariable("{expira}"  , "");
		}
		if(pedido.getMedio_pago().equals("TBK")){
			top.setVariable("{num_mp}"	, "<tr class='tabla2'><td>N&deg; Tarjeta Actual</td><td>"+pedido.getNum_mp()+"</td></tr>");
			top.setVariable("{nom_tarj}", "<tr class='tabla2'><td>Nombre de Tarjeta Bancaria</td><td>"+pedido.getNom_tbancaria()+"</td></tr>");
			top.setVariable("{expira}"	, "<tr class='tabla2'><td>Expiraci�n</td><td>"+pedido.getFecha_exp()+"</td></tr>");
		}
		if(pedido.getMedio_pago().equals("PAR")){
			top.setVariable("{num_mp}"	, "<tr class='tabla2'><td>N&deg; Tarjeta Actual</td><td>"+pedido.getNum_mp()+"</td></tr>");
			top.setVariable("{nom_tarj}", "<tr class='tabla2'><td>Nombre de Tarjeta Bancaria</td><td>"+pedido.getNom_tbancaria()+"</td></tr>");
			//top.setVariable("{meses_librpago}", "<tr class='tabla2'><td>Mese Libres de Pago</td><td>"+String.valueOf(pedido.getMeses_librpago())+"</td></tr>");
			top.setVariable("{expira}"	, "");
		}

		top.setVariable("{num_doc}"   , String.valueOf(pedido.getNum_doc()));
		top.setVariable("{tipo_doc}"  , pedido.getTipo_doc());
		top.setVariable("{tb_banco}"  , pedido.getTb_banco());
		
		/*if(pedido.getMeses_librpago()>=0){
			top.setVariable("{meses_librpago}", "<tr class='tabla2'><td>Mese Libres de Pago</td><td>"+String.valueOf(pedido.getMeses_librpago())+"</td></tr>");
		}
		else{
			top.setVariable("{meses_librpago}", "");
		}*/
		if(pedido.getFecha_exp()!=null){
			top.setVariable("{f_expiracion}", pedido.getFecha_exp());
		}else{
			top.setVariable("{f_expiracion}", "");
		}
		top.setVariable("{id_pedido}", paramId_pedido);
		top.setVariable("{disabled_banc}", "disabled");
		top.setVariable("{disabled_paris}", "disabled");
		//if(req.getParameter("mensaje")!=null)
		//	top.setVariable("{mensaje}", mensaje);
		//else
		//	top.setVariable("{mensaje}", mensaje);

		ClientesDTO cliente = bizDelegate.getClienteById(pedido.getId_cliente());
		EmpresasDTO emp = bizDelegate.getEmpresaById(cliente.getId_empresa());

		double saldo_pendiente = bizDelegate.getSaldoActualPendiente(emp.getEmp_id());
		double disp_actual = emp.getEmp_saldo() - saldo_pendiente - pedido.getMonto();
		
        logger.debug("saldo pendiente: " + saldo_pendiente);
        logger.debug("Saldo          : " + emp.getEmp_saldo());
        logger.debug("monto pedido   : " + pedido.getMonto());
        logger.debug("disp_actual    : " + disp_actual);

        top.setVariable("{rut_empresa}"    , emp.getEmp_rut()+"");
        top.setVariable("{rs_empresa}"     , emp.getEmp_rzsocial()+"");
        top.setVariable("{saldo}"          , emp.getEmp_saldo()+"");
        top.setVariable("{saldo_pendiente}", saldo_pendiente+"");
        top.setVariable("{monto_pedido}"   , pedido.getMonto()+"");
        top.setVariable("{disp_actual}"    , disp_actual+"");
        
        if (disp_actual < 0.0) {
            logger.info("Alerta Forma de pago con Linea de credito Insuficiente");
            mensaje = "La Linea de credito es Insuficiente para el Monto del Pedido";
        }

        top.setVariable("{mensaje}", mensaje);

		
		String result = tem.toString(top);	
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}