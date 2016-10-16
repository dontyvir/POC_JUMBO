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
import cl.bbr.boc.dto.UmbralDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Despliega la informacion de la pestaña Adm Parametro
 * @author DNT - RMI
 */
public class ViewMantenedorParametros  extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int pag;
		int regsperpage=10; //registros mostrados por pagina
		String action = "";
		long tipo = 0;
		char activo;
		String mns = "";
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		if ( req.getParameter("action") != null )
			action =  req.getParameter("action");
		
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		logger.debug("mns:"+mns);		
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}
	
		String num_cam="";
		if ( req.getParameter("busq_num_cat") != null )
			num_cam =  req.getParameter("busq_num_cat");
		String nom_cam="";
		if ( req.getParameter("busq_nom_cat") != null )
			nom_cam =  req.getParameter("busq_nom_cat");
		String sel_cat = "";
		if ( req.getParameter("sel_cat") != null )
			sel_cat = req.getParameter("sel_cat");
		logger.debug("num_cam:"+num_cam);
		logger.debug("nom_cam:"+nom_cam);
		logger.debug("sel_cat:"+sel_cat);
		
//		3. Template
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();

		// 4.1 Listado de Elementos
		//Llega el elemento por busqueda o vacio y lo setea 
		ElementosCriteriaDTO criterio = new ElementosCriteriaDTO();
		criterio.setPag(pag);
		criterio.setNombre(nom_cam);
		criterio.setNumero(num_cam);
		criterio.setRegsperpage(regsperpage);
		criterio.setTipo(tipo);
		
		ArrayList elementos = new ArrayList();
		
		// al array nuevo le pasa los elementos encontrados en getParametrosByCriteria(criterio);
		List lst_elem = bizDelegate.getParametrosByCriteria(criterio);
	
			//Revisa el tamaño de la lista
		    logger.debug("Tamaño del Listado:"+lst_elem.size());
			if (lst_elem.size() < 1 ){
				top.setVariable("{mje1}","La consulta no arrojo resultados");
			}else{
				top.setVariable("{mje1}","");
			}
			
			
			//por cada elemnto encontrado en la busuqea por criteria 
			for (int i = 0; i< lst_elem.size(); i++){
				IValueSet fila_cam = new ValueSet();
				//llamado a umbral dto para el llenado de las variables 
				UmbralDTO  elem1 = (UmbralDTO)lst_elem.get(i);
				//seteo de variables del htm segun el resutado de el getParametrosByCriteria
				fila_cam.setVariable("{id_local}", String.valueOf(elem1.getId_local()));
				fila_cam.setVariable("{nom_local}", String.valueOf(elem1.getNom_local()));
				//fila_cam.setVariable("{u_unidad}"	, String.valueOf(elem1.getU_unidad()));
				if ((elem1.getU_unidad()<0)){
					fila_cam.setVariable("{u_unidad}","-");
				}else{
					fila_cam.setVariable("{u_unidad}", String.valueOf(elem1.getU_unidad()));
				}
				fila_cam.setVariable("{u_monto}"	, String.valueOf(elem1.getU_monto()));
			 //   fila_cam.setVariable("{u_activacion}"	, String.valueOf(elem1.getU_activacion()));
				if ((elem1.getU_activacion().equals(""))&&(elem1.getU_activacion()== null)){
					fila_cam.setVariable("{u_activacion}","N");
				}else{
					fila_cam.setVariable("{u_activacion}", String.valueOf(elem1.getU_activacion()));
				}
				fila_cam.setVariable("{fecha_modi}"	, String.valueOf(elem1.getFecha_modi()));
				fila_cam.setVariable("{ver}"	, "Ver");
				elementos.add(fila_cam);
				// Fin seteo de vatiables
			}
		
		//		 5 Paginador
		logger.debug(" paginador ");
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCountElementosByCriteria(criterio);
		logger.debug ("total de registros: " + tot_reg);
		logger.debug ("registros por pagina: " + regsperpage);

		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("Elemntos: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		
		
		
	    for (int i = 1; i <= total_pag; i++) {
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}		
		
	    top.setVariable("{num_pag}"		,String.valueOf(pag));
	    top.setVariable("{action}" ,action);
	    top.setVariable("{busq_num_cat}" ,num_cam);
	    top.setVariable("{busq_nom_cat}" ,nom_cam);
			
		//Setea variables main del template 
	    top.setDynamicValueSets("PAGINAS", pags);
	    top.setDynamicValueSets("ELEMENTOS", elementos);
	    top.setVariable("{mns}", mns );
	    
		// 6. Setea variables del template , variables del header
	
	    top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
	    
		// 7. Setea variables bloques		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
}

