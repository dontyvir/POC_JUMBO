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
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario que permite liberar una Op agregando un motivo al log
 * @author BBRI
 */
public class ViewReseteaTrxMp extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido=0;
		long id_motivo=0;
		long id_trx_mp=0;
		String mensaje_retorno="";
		String obs="";
		int retorno=0;
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		logger.debug("Template: " + html);
		//2. Procesa par�metros del request
			
		if ( req.getParameter("id_pedido") != null ){id_pedido =  Long.parseLong(req.getParameter("id_pedido"));}
		logger.debug("Este es el id_pedido que viene:" + id_pedido);

		if ( req.getParameter("id_trx_mp") != null ){id_trx_mp =  Long.parseLong(req.getParameter("id_trx_mp"));}
		logger.debug("Este es el id_trx_mp que viene:" + id_trx_mp);

		if ( req.getParameter("mensaje") != null ){mensaje_retorno = req.getParameter("mensaje");}
		logger.debug("mensaje_retorno:" + mensaje_retorno );
		
		if ( req.getParameter("ret") != null ){retorno = Integer.parseInt(req.getParameter("ret"));}
		logger.debug("retorno:" + retorno);
		
		//if (req.getParameter("id_motivo")!=null){id_motivo = Long.parseLong(req.getParameter("id_motivo"));}
		//logger.debug("id_motivo:" + id_motivo);
		int origen = 1;
		if (req.getParameter("origen")!=null){origen = Integer.parseInt(req.getParameter("origen"));}
		logger.debug("origen:" + origen);
		
		if (req.getParameter("obs")!=null){obs=req.getParameter("obs");}
		logger.debug("obs:" + obs);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Din�micas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
	    EstadoDTO estadoPedido = bizDelegate.getEstadoPedido(id_pedido);
	    EstadoDTO estadoTrxMp  = bizDelegate.getEstadoTrxMp(id_trx_mp);
		
		if (origen == 1){
			top.setVariable("{url}","ViewOPFormPedido?id_pedido="+id_pedido);
			top.setVariable("{nom_target}","winpedido1");
		}
		
		if (origen == 2){
			top.setVariable("{url}","ViewMonOP");
			top.setVariable("{nom_target}","winpedido");
		}
		
		//Genera combo de seleccion de motivos
		
		/*List listaMots = new ArrayList();
		listaMots = bizDelegate.getMotivosPedidoBOC();
		ArrayList mots = new ArrayList();
		for (int i=0; i<listaMots.size(); i++){
			IValueSet filamot = new ValueSet();
			MotivoDTO mot1 = new MotivoDTO();
			mot1 = (MotivoDTO)listaMots.get(i);
			if (i==0){
				filamot.setVariable("{sel}"		,"selected");	
			}
			filamot.setVariable("{id_motivo}"		,String.valueOf(mot1.getId_mot()));
			filamot.setVariable("{nom_motivo}"		,String.valueOf(mot1.getId_mot())+" : "+String.valueOf(mot1.getNombre()));
			if (mot1.getId_mot()==id_motivo)
				filamot.setVariable("{sel}"		,"selected");
			else
				filamot.setVariable("{sel}"		,"");
			mots.add(filamot);
		}*/
		
		// 5. variables globales
		top.setVariable("{id_pedido}", String.valueOf(id_pedido));
		top.setVariable("{IdEstPedido}", String.valueOf(estadoPedido.getId_estado()));
		top.setVariable("{NomEstPedido}", String.valueOf(estadoPedido.getNombre()));

		top.setVariable("{id_trx_mp}", String.valueOf(id_trx_mp));
		top.setVariable("{IdEstTrxMp}", String.valueOf(estadoTrxMp.getId_estado()));
		top.setVariable("{NomEstTrxMp}", String.valueOf(estadoTrxMp.getNombre()));

		if (estadoTrxMp.getId_estado() == 50 || estadoTrxMp.getId_estado() == 52){
		    top.setVariable("{mensaje}", "No es necesario cambiar el Estado de la Transacci�n.");
		    top.setVariable("{type}", "hidden");
		}else{
		    top.setVariable("{mensaje}", mensaje_retorno);
		    top.setVariable("{type}", "button");
		}
		    
		
		
		top.setVariable("{obs}", obs);
		top.setVariable("{origen}", String.valueOf(origen));
		
		if (retorno==1) top.setVariable("{hab}", "disabled");
		else top.setVariable("{hab}", "enabled");
		
		// 6. listas dinamicas
		//top.setDynamicValueSets("MOTIVOS",mots);
		
		//7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}