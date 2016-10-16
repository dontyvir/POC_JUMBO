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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * formulario para el ingreso de nuevos productos al monitor de productos
 * @author BBRI
 */
public class ViewProductNewForm extends Command {
	private final static long serialVersionUID = 1;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		long id_cat_padre = -1; 
		
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
		String sel_cat = "";
		if ( req.getParameter("sel_cat") != null )
			sel_cat = req.getParameter("sel_cat");
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		String uni_med = "";
		String marca = "";
		String estado = "N";
		String generico = "G";
		String cod_sap = "G"+bizDelegate.getCodSapGenerico();
		//Random ran = new Random();
		//String cod_sap = "G"+ran.nextInt(100);//bizDelegate.getCodSAP();
		String fec_crea = Formatos.getFechaActual();
		long user_mod = -1;
		
		List lst_tip = bizDelegate.getEstadosByVis("TPR","S");
		List lst_est = bizDelegate.getEstadosByVis("PR","S");
		
		ProductosDTO prod = new ProductosDTO(0, -1, "", cod_sap, 0, 0, estado, "", "", "", "","", 0, 
				"", 0, fec_crea,"", user_mod, generico, 0, 0, "", "", ""); 
		
		//valores a mostrar
		top.setVariable("{cod_sap}"		,String.valueOf(cod_sap));
		top.setVariable("{estado}"		,FormatoEstados.frmEstado(lst_est,prod.getEstado()));
		top.setVariable("{generico}"	,FormatoEstados.frmEstado(lst_tip,prod.getGenerico()));
		top.setVariable("{tipo}"		,String.valueOf(prod.getTipo()));
		top.setVariable("{marca}"		,String.valueOf(prod.getNom_marca()));
		top.setVariable("{desc_corta}"	,String.valueOf(prod.getDesc_corta()));
		top.setVariable("{desc_larga}"	,String.valueOf(prod.getDesc_larga()));
		top.setVariable("{atr_difer}"	,String.valueOf(prod.getValor_difer()));
		top.setVariable("{fec_crea}"	,Formatos.frmFecha(Formatos.getFecHoraActual()));
		top.setVariable("{img1}"		,"");
		top.setVariable("{img2}"		,"");
		
		//guardar data del form -
		//usar comando NewGenericProduct
		
		//ver marcas
		ArrayList marcas = new ArrayList();
		List listMarcas = bizDelegate.getMarcas();

		for (int i = 0; i< listMarcas.size(); i++){
			IValueSet fila_tip = new ValueSet();
			MarcasDTO tip1 = (MarcasDTO)listMarcas.get(i);
			fila_tip.setVariable("{mrc_id}", String.valueOf(tip1.getId()));
			fila_tip.setVariable("{mrc_nombre}"	, String.valueOf(tip1.getNombre()));
			
			if (marca.equals(String.valueOf(tip1.getId()))){
				fila_tip.setVariable("{mrc_tip}","selected");
			}
			else
				fila_tip.setVariable("{mrc_tip}","");		
			marcas.add(fila_tip);
			
		}

		//ver unidades de medida
		ArrayList unids = new ArrayList();
		List listUniMed = bizDelegate.getUnidMedida();

		for (int i = 0; i< listUniMed.size(); i++){
			IValueSet fila_tip = new ValueSet();
			UnidadMedidaDTO tip1 = (UnidadMedidaDTO)listUniMed.get(i);
			fila_tip.setVariable("{ume_id}", String.valueOf(tip1.getId()));
			fila_tip.setVariable("{ume_nombre}"	, String.valueOf(tip1.getDesc()));
			
			if (uni_med.equals(String.valueOf(tip1.getId()))){
				fila_tip.setVariable("{ume_tip}","selected");
			}
			else
				fila_tip.setVariable("{ume_tip}","");		
			unids.add(fila_tip);
			
		}

		//arbol de categorias
		ArrayList arbCategorias = new ArrayList();
		CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(1, 'A', 'T', 10, true,
				"", "", sel_cat);
		try{
			//List listArbCate = bizDelegate.getCategoriasByCriteria(criterioArbolCat);
			List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, id_cat_padre);
	
			logger.debug("arbCategorias -> "+listArbCate.size());
	
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listArbCate.get(i);
				fila_cat.setVariable("{nav_id_cat}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{nav_nom_cat}"	, String.valueOf(cat1.getNombre()));
				if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))){
					fila_cat.setVariable("{sel_cat}","selected");
				}
				else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
				
			}
		}catch(Exception ex){
			logger.debug("cate 4.2.1"+ex.getMessage());
		}
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		// 6. Setea variables bloques	
		top.setDynamicValueSets("MARCAS", marcas);
	    top.setDynamicValueSets("UNI_MED", unids);
	    top.setDynamicValueSets("SEL_CATEGORIA_NAV", arbCategorias);
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
