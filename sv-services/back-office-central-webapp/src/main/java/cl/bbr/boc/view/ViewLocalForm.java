package cl.bbr.boc.view;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra la Información del Usuario seleccionado
 * @author BBRI
 */
public class ViewLocalForm extends Command {
	private final static long serialVersionUID = 1;
	
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_local=0;
		String msje="";
		View salida = new View(res);
		//logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html; 
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		if(req.getParameter("id_local")!=null)
			id_local = Integer.parseInt(req.getParameter("id_local"));
		else
			throw new ParametroObligatorioException("falta parametro id_local");
		
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		
		logger.debug("id_local:"+id_local);
		
		BizDelegate biz = new BizDelegate();
		
		//datos del perfil
		//MarcasDTO mrc = bizDelegate.getMarcasById(mar_id);
		LocalDTO local = biz.getLocalById(id_local);
		try{
			top.setVariable("{id_local}"		,String.valueOf(local.getId_local()));
			top.setVariable("{cod_local}"		,local.getCod_local());
			top.setVariable("{nom_local}"		,local.getNom_local());
			top.setVariable("{orden}"		,String.valueOf(local.getOrden()));
			top.setVariable("{fec_carga_prec}"		,local.getFec_carga_prec());
			top.setVariable("{cod_local_pos}"		,String.valueOf(local.getCod_local_pos()));
			top.setVariable("{direccion}", String.valueOf(local.getDireccion()));
			//top.setVariable("{id_local_sap}"		,String.valueOf(local.get));
			
			if (local.getTipo_flujo().equals("N")){
				top.setVariable("{sel_normal}"		,"selected");	
				top.setVariable("{sel_parcial}"		,"");
			}else{
				top.setVariable("{sel_parcial}"		,"selected");
				top.setVariable("{sel_normal}"		,"");
			}			
			
			if(local.getCod_local_promocion()!=null && !local.getCod_local_promocion().equals("")){
				top.setVariable("{cod_local_promo}"		,local.getCod_local_promocion());
			}else{
				top.setVariable("{cod_local_promo}"		,"");
			}

			if (local.getTipo_picking().equals("N")){
				top.setVariable("{pick_normal}", "selected");	
				top.setVariable("{pick_light}" , "");
			}else{
				top.setVariable("{pick_normal}", "");
				top.setVariable("{pick_light}" , "selected");
			}	
			if (local.getRetirolocal().equals("S")){
				top.setVariable("{retiro_local}", "checked");
				top.setVariable("{habilita_combobox_zona}", "");
			}else{
				top.setVariable("{retiro_local}", "");
				top.setVariable("{habilita_combobox_zona}", "disabled");
			}
			int id_zona_retiro=0;
			
			if (!(local.getId_zona_retiro()==0)){
				id_zona_retiro=local.getId_zona_retiro();
			}
			
			List zonas =  biz.getZonasLocal(id_local);
			ArrayList listazona=new ArrayList();
			IValueSet fila1 = new ValueSet();
			fila1.setVariable("{id_zona}", "0");
            fila1.setVariable("{nombre_zona}", "Dummy");
            fila1.setVariable("{sel_zona}", "");
            listazona.add(fila1);
			for ( int i=0; i < zonas.size(); i++ ) {
				IValueSet fila = new ValueSet();
                ZonaDTO zona = (ZonaDTO) zonas.get(i);
                fila.setVariable("{id_zona}", String.valueOf(zona.getId_zona()));
                fila.setVariable("{nombre_zona}", zona.getNombre());                
                if (id_zona_retiro==zona.getId_zona()){
                	fila.setVariable("{sel_zona}", "selected");
                } else{
                	fila.setVariable("{sel_zona}", "");
                }        
                listazona.add(fila);
            }        
			top.setDynamicValueSets("ZONAS", listazona);
			

			
		}catch(Exception ex){
			logger.debug(ex.getMessage());
			ex.printStackTrace();
		}
		
		
		top.setVariable("{msje}"  , msje);
			
		// 6. Setea variables bloques
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}