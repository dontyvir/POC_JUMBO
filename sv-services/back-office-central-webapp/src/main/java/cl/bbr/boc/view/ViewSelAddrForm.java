package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario para seleccionar la direccion para el despacho de un pedido de un cliente
 * @author BBRI
 */
public class ViewSelAddrForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido;
		long id_dir;
		long id_cliente;
		//int mod=0;
		boolean edicion = false;

		logger.debug("User: " + usr.getLogin());
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		// 2. Procesa parámetros del request
		if ( req.getParameter("id_pedido") == null ){
	    	 throw new ParametroObligatorioException("id_pedido es nulo");
	    }
		id_pedido = Long.parseLong(req.getParameter("id_pedido"));
		
		//if ( req.getParameter("mod")!=null)
		//	mod = Integer.parseInt(req.getParameter("mod"));
		
		//		3. Template

		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas

		// Modo de edición
		if ( usr.getId_pedido() == id_pedido ){
			edicion = true;
		}
		
		
		// 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();
		//obtener id_cliente, id_dir
		PedidoDTO ped = bizDelegate.getPedidosById(id_pedido); 
		id_cliente = ped.getId_cliente();
		id_dir = ped.getDir_id(); 
		logger.debug("id_pedido:"+id_pedido);
		logger.debug("id_cliente:"+id_cliente);
		logger.debug("id_dir:"+id_dir);

		//Recupera Datos del Cliente segun Id
		ClientesDTO clidto = bizDelegate.getClienteById(id_cliente);
		
		//obtiene lista de direcciones
		ArrayList direcciones = new ArrayList();
		List lst_dir = bizDelegate.getDireccionesByIdCliente(id_cliente);
		List lst_est = bizDelegate.getEstadosByVis("ALL","S");
		for(int i=0;i<lst_dir.size();i++){
			
			IValueSet filaDir = new ValueSet();
			DireccionesDTO direc = new DireccionesDTO(); 
			direc = (DireccionesDTO) lst_dir.get(i);
			if(direc.getId_dir()==id_dir) filaDir.setVariable("{check}","checked");
			else filaDir.setVariable("{check}","");
			filaDir.setVariable("{valor}"		, String.valueOf(direc.getId_dir()));
			filaDir.setVariable("{alias}"		, String.valueOf(direc.getAlias()));
			filaDir.setVariable("{desc_direc}"	, String.valueOf(direc.getCalle()+" "+direc.getNumero()+" "+direc.getDepto()));
			filaDir.setVariable("{comuna}"		, String.valueOf(direc.getComuna()));
			filaDir.setVariable("{estado}"		, FormatoEstados.frmEstado(lst_est,direc.getEstado()));
			direcciones.add(filaDir);
		}
		top.setVariable("{nom_cliente}", String.valueOf(clidto.getNombre()+" "+clidto.getPaterno()+" "+clidto.getMaterno()));
		
		
		top.setVariable("{dir_id}",String.valueOf(id_dir) );
		top.setVariable("{id_cliente}",String.valueOf(id_cliente) );
		top.setVariable("{id_pedido}",String.valueOf(id_pedido) );
		top.setVariable("{mod}","" );
		//5 Paginador

		// 6. Setea variables bloques
		top.setDynamicValueSets("DIRECCIONES", direcciones);

		//		 7. Salida Final
		String result = tem.toString(top);
		
		//Fin
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}