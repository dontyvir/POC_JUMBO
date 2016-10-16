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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra la Información del Cliente seleccionado
 * @author BBRI
 */
public class ViewClientForm extends Command {
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_cliente;
		boolean FlagPerfilBlanquearDireccion = false;
		boolean FlagDireccionValida = false;
		String mje="";
		if ( req.getParameter("mje") != null ){
			if  (req.getParameter("mje").equals("1"))
				mje = "Usted No tiene permisos para Desbloquear Cliente";
			if (req.getParameter("mje").equals("2"))
				mje = "Desbloqueo realizado con éxito";
			if (req.getParameter("mje").equals("3"))
				mje = "Bloqueo realizado con éxito";
			
			if (("4").equals(req.getParameter("mje")))
				mje = "Blanqueo de direcci&oacute;n realizado con éxito";
				 
		}
		if ( req.getParameter("id_cliente") != null )
			id_cliente = Integer.parseInt( req.getParameter("id_cliente") );
		else {
			id_cliente = 0;
		}
		
		//+20131120_JMCE:
		List listperfiles = new ArrayList();
		listperfiles = usr.getPerfiles();
		logger.debug("tot_perfiles:"+listperfiles.size());
		for (int i=0; i<listperfiles.size(); i++){
			PerfilesEntity perf = new PerfilesEntity();
			perf = (PerfilesEntity)listperfiles.get(i);
       	 	
	        // Habilita el Blanqueo de una dirección.
			if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_BLANQUEA_DIRECCION){
				FlagPerfilBlanquearDireccion = true;
			}	
        }	
		
		
		//-20131120_JMCE:		
		
		View salida = new View(res);
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		String html_ficha_dir = "'"+getServletConfig().getInitParameter("PopUpAddr");
		
		
		
		logger.debug("Template: " + html);
		logger.debug("Url Ficha Popup: " + html_ficha_dir);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		BizDelegate bizDelegate = new BizDelegate();
		
		List lst_est = bizDelegate.getEstadosByVis("CLB","S");		
		//		Recupera Datos del Cliente segun Id
		
		ClientesDTO clientes = bizDelegate.getClienteById(id_cliente);
		top.setVariable("{mje}"		,mje);
		top.setVariable("{rut}"		,String.valueOf(clientes.getRut())+"-"+String.valueOf(clientes.getDv()));
		top.setVariable("{nom_cliente}" ,String.valueOf(clientes.getNombre())+ " "+String.valueOf(clientes.getPaterno())+" "+ String.valueOf(clientes.getMaterno()));
		top.setVariable("{nombre}"		,String.valueOf(clientes.getNombre()));
		top.setVariable("{paterno}"		,String.valueOf(clientes.getPaterno()));
		top.setVariable("{materno}"		,String.valueOf(clientes.getMaterno()));
		top.setVariable("{fec_nac}"		,Formatos.frmFecha(clientes.getFnac()));
		top.setVariable("{genero}"		,Formatos.frmGenero(clientes.getGenero()));
		if (clientes.getEmail()!= null )
			top.setVariable("{mail}"	,String.valueOf(clientes.getEmail()));
		else
			top.setVariable("{mail}"	,"");
		top.setVariable("{fono1}"		,"("+String.valueOf(clientes.getCodfono1())+") - "+String.valueOf(clientes.getFono1()));
		top.setVariable("{fono2}"		,"("+String.valueOf(clientes.getCodfono2())+") - "+String.valueOf(clientes.getFono2()));
		top.setVariable("{fono3}"		,"("+String.valueOf(clientes.getCodfono3())+") - "+String.valueOf(clientes.getFono3()));
		top.setVariable("{fec_crea}"	,Formatos.frmFecha(clientes.getFecCrea()));
		top.setVariable("{fec_act}"		,Formatos.frmFecha(clientes.getFecAct()));
		top.setVariable("{estado}"		,FormatoEstados.frmEstado(lst_est,clientes.getEst_bloqueo()));
		top.setVariable("{acciones}"		,"Ver");
		
		FlagDireccionValida= bizDelegate.tieneDireccionesConCobertura(id_cliente);
		String estadoBloqueado=clientes.getEst_bloqueo();
		
		// Permitir que debloquee cliente solo si es usuario es de perfil Supervisor Back Office
		if (clientes.getEst_bloqueo().equals("D")){
			top.setVariable("{blodes}" , "Bloquear Cliente");  
			top.setVariable("{cam_est}" , "Bloquear");
			top.setVariable("{visible}" , " Enabled ");
			//+20131120 - JMCE
			if (FlagPerfilBlanquearDireccion && !FlagDireccionValida){
				top.setVariable("{sol_cuenta_in}" , "Blanqueo Direcci&oacute;n");  
				top.setVariable("{cam_est_bql}" , "Blanqueo");
				top.setVariable("{visible_bql}" , " Enabled ");
			}else{
				top.setVariable("{cam_est_bql}" , "");
				top.setVariable("{sol_cuenta_in}" , "Blanqueo Direcci&oacute;n");
				top.setVariable("{visible_bql}" , " Disabled ");
			}	
			//-20131120 - JMCE
			
		}else if (clientes.getEst_bloqueo().equals("B")){
			top.setVariable("{blodes}" , "Desbloquear Cliente");
			top.setVariable("{cam_est}" , "Desbloquear");
			top.setVariable("{visible}" , " Enabled ");
		}else{
			top.setVariable("{cam_est}" , "");
			top.setVariable("{blodes}" , "Bloquear");
			top.setVariable("{visible}" , " Disabled ");
		}
		top.setVariable("{id_cliente}"		,String.valueOf(clientes.getId_cliente()));	
		//String cliente =  String.valueOf(clientes.getId_cliente());
		
		// Recupera las direcciones de los clientes segun Id
		List lst_estdir = bizDelegate.getEstadosByVis("ALL","S");	//Estados de las direcciones
		ArrayList direc  = new ArrayList();
		List listDirec = bizDelegate.getDireccionesByIdCliente(id_cliente);
	
		for (int i = 0; i< listDirec.size(); i++){
			IValueSet fila_dir = new ValueSet();
			DireccionesDTO direccion = (DireccionesDTO)listDirec.get(i);
			
			fila_dir.setVariable("{comuna}", String.valueOf(direccion.getComuna()));
			fila_dir.setVariable("{region}", String.valueOf(direccion.getNom_region()));
			fila_dir.setVariable("{local}"	, String.valueOf(direccion.getNom_local()));
			fila_dir.setVariable("{direccion}"	, String.valueOf(direccion.getNom_tipocalle()+" " + direccion.getCalle()+" " + direccion.getNumero() +", "+ direccion.getDepto()));
			fila_dir.setVariable("{estado}"	, FormatoEstados.frmEstado(lst_estdir,direccion.getEstado()));
			fila_dir.setVariable("{accion1}"	,"Editar");
			fila_dir.setVariable("{pag_url}", html_ficha_dir + "?dirid="+ direccion.getId_dir()+"&idcliente="+clientes.getId_cliente()+"'");
			direc.add(fila_dir);
			
		}

		//recupera los pedidos del cliente
		PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
		criterio.setId_cliente(id_cliente);
		criterio.setRegsperpag(10000);
		
		
		//	orden usando criterios

		List ordenarpor = new ArrayList();

		ordenarpor.add(PedidosCriteriaDTO.ORDEN_FECHA_COMPRA+" "+PedidosCriteriaDTO.ORDEN_DESCENDENTE);
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_ID_PEDIDO+" "+PedidosCriteriaDTO.ORDEN_DESCENDENTE);
		
		criterio.setOrden_columnas(ordenarpor);
		criterio.setTipo_picking("");
		criterio.setLimitarFecha(false);
		
		
		ArrayList templ_pedidos  = new ArrayList();
		List pedidos = bizDelegate.getPedidosByCriteria(criterio);
		for (int i = 0; i< pedidos.size(); i++){
			IValueSet fila_ped = new ValueSet();
			MonitorPedidosDTO ped1 = (MonitorPedidosDTO)pedidos.get(i);
            fila_ped.setVariable("{num_pedido}",String.valueOf(ped1.getId_pedido()));
			fila_ped.setVariable("{fec_compra}", Formatos.frmFecha(ped1.getFingreso()));
			fila_ped.setVariable("{local_desp}",ped1.getLocal_despacho());
			fila_ped.setVariable("{fec_desp}",Formatos.frmFecha(ped1.getFdespacho()));
			fila_ped.setVariable("{est_op}",ped1.getEstado());
			fila_ped.setVariable("{monto}",Formatos.formatoPrecio(Long.parseLong(String.valueOf(ped1.getMonto()))));
            fila_ped.setVariable("{cump}", Formatos.getDescripcionCumplimiento( ped1.getPedExt().getCumplimiento() ));
			templ_pedidos.add(fila_ped);			
		}
		top.setDynamicValueSets("DIREC_DESP", direc);
		top.setDynamicValueSets("HIS_PEDIDOS", templ_pedidos);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}