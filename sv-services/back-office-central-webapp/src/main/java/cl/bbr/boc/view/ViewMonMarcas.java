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
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Muestra el monitor de Marcas
 * @author BBRI
 */
public class ViewMonMarcas extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int regsperpage = 10;
		int pag;
		
		//logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}

		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();
		

		// Obtiene el resultado
		List lstPer = null;
		
		MarcasCriteriaDTO criterio = new MarcasCriteriaDTO (); 
		criterio.setPag(pag);
		criterio.setRegsperpage(regsperpage);
		
		lstPer =  bizDelegate.getMarcas(criterio);
		ArrayList marcas = new ArrayList();
		
		List listEstados = bizDelegate.getEstadosByVis("ALL","S");
		
		try{	
			for (int i = 0; i < lstPer.size(); i++) {	
					IValueSet fila = new ValueSet();
					MarcasDTO mar = (MarcasDTO)lstPer.get(i);
					
					fila.setVariable("{mar_id}"	 ,String.valueOf(mar.getId()));
					fila.setVariable("{mar_nom}" ,String.valueOf(mar.getNombre()));
					fila.setVariable("{mar_est}" ,FormatoEstados.frmEstado(listEstados,mar.getEstado()));
					if (mar.getCant_prods()==0){
						fila.setVariable("{mar_img_del}" ,"<a href=\"javascript:validar_eliminar('Esta seguro que desea eliminar?','DelMarca?mar_id="+mar.getId()+"&url='+escape('ViewMonMarcas?pagina="+pag+"'));\"><IMG SRC='img/trash.gif' WIDTH='16' HEIGHT='16' BORDER='0' ALT='Eliminar Marca'></a>");
					}else{
						fila.setVariable("{mar_img_del}" ,"");
					}
					marcas.add(fila);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getMarcasAllCount();
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
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

		//Setea variables main del template 
	    top.setVariable("{num_pag}"		,String.valueOf(pag));

		// 6. Setea variables bloques
	    top.setDynamicValueSets("LST_MRC", marcas);
	    top.setDynamicValueSets("PAGINAS", pags);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		    
	    
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}

}
