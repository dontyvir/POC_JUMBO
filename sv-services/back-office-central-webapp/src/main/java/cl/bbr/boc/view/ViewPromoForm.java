package cl.bbr.boc.view;

import java.io.PrintWriter;
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
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductoPromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega el formulario que contiene los datos de la sucursal
 * 
 * @author BBRI
 */
public class ViewPromoForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		int codigo;
		if(req.getParameter("codigo")!=null)
		    codigo = Integer.parseInt(req.getParameter("codigo"));
		else
			throw new ParametroObligatorioException("codigo no encontrado");

		BizDelegate biz= new BizDelegate();
		// Obtiene datos de la promoción
		PromocionDTO promo= biz.getPromocion(codigo);
		
		if(promo!=null){
			logger.debug("id_promocion retirna codigo:"+promo.getCod_promo());
			top.setVariable("{codigo}",String.valueOf(promo.getCod_promo()));
			top.setVariable("{nom_local}",promo.getNom_local());
			top.setVariable("{tipo}",String.valueOf(promo.getTipo_promo()));
			top.setVariable("{descripcion}",promo.getDescr());
			top.setVariable("{fec_inicio}",Formatos.frmFechaHora(promo.getFini()));
			top.setVariable("{fec_fin}", Formatos.frmFechaHora(promo.getFfin()));
			if (promo.getBanner()!= null && !promo.getBanner().equals(""))
				top.setVariable("{banner}", promo.getBanner());
			else
				top.setVariable("{banner}", "");
			if (promo.getSustituible() == null || promo.getSustituible().equals("S")){
				top.setVariable("{sel_si}","selected");
				top.setVariable("{sel_no}","");
			}else{
				top.setVariable("{sel_si}","");
				top.setVariable("{sel_no}","selected");
			}
			if (promo.getFaltante() == null || promo.getFaltante().equals("S")){
				top.setVariable("{falt}","selected");
				top.setVariable("{no_falt}","");
			}else{
				top.setVariable("{falt}","");
				top.setVariable("{no_falt}","selected");
			}
		}
		
		
		
		
		// Listado de Productos de la promocion
		List lst_prod = new ArrayList();
		
		lst_prod= biz.getPromocionProductos(codigo);
		
		ArrayList prod_promo = new ArrayList();
		if (lst_prod.size() <=  0 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
		}
        
        if ( req.getParameter("_LIST_PROD") != null ) {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("Id;Cod. Sap;Uni. Med;Descripción;Tipo<br/>");
            for (int i = 0; i < lst_prod.size(); i++) {
                ProductoPromocionDTO prod = (ProductoPromocionDTO)lst_prod.get(i);
                out.println(String.valueOf(prod.getId_producto())+";"+prod.getCod_prod1()+";"+prod.getUni_med()+";"+prod.getDescr()+";"+prod.getTipo() + "<br/>");
            }
            return;            
        } 
        
        int MAX = 10000;
		for (int i = 0; i < lst_prod.size(); i++) {
            if ( i == MAX ) {
                break;
            }
            IValueSet fila = new ValueSet();
            ProductoPromocionDTO prod = (ProductoPromocionDTO)lst_prod.get(i);
            fila.setVariable("{prod_id}", String.valueOf(prod.getId_producto()));
            fila.setVariable("{sap}",	prod.getCod_prod1());
            fila.setVariable("{um}",	prod.getUni_med());
            fila.setVariable("{desc}",	prod.getDescr());
            fila.setVariable("{tipo}",	prod.getTipo());
            prod_promo.add(fila);            
		}
        if ( MAX < lst_prod.size() ) {
            top.setVariable("{more_prods}","Existen <font color=\"red\">más productos</font> de esta promoción, para ver todos <a href=\"ViewPromoForm?codigo="+codigo+"&pagina=1&_LIST_PROD=ok\" target=\"_blank\">clic aquí.</a><br/><br/>");
        } else {
            top.setVariable("{more_prods}","");
        }
        
		// Listado de Categorias relacionadas con promocion seccion (al total)
		
		List lst_catsap =null;
		lst_catsap= biz.getCategoriasSapByPromocionSeccion(promo.getId_local(), (int)promo.getTipo_promo());
		
		ArrayList catsap_promo = new ArrayList();
		if ((lst_catsap==null) || (lst_catsap.size() <=  0) ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
			for (int i = 0; i < lst_catsap.size(); i++) {			
				IValueSet fila = new ValueSet();
				CategoriaSapDTO cat = (CategoriaSapDTO)lst_catsap.get(i);							
				fila.setVariable("{id_catsap}", cat.getId_cat());
				fila.setVariable("{nombre}", cat.getDescrip());
				
				if(cat.getEstado().equals("1"))				
					fila.setVariable("{estado}", "Activa");
				else
					fila.setVariable("{estado}", "No Activa");
				if(cat.getTipo().equals("I"))
					fila.setVariable("{tipo}", "Intermedia");
				else
					fila.setVariable("{tipo}", "Terminal");
				
				
				
				catsap_promo.add(fila);
			}		
		}

		
		

		top.setVariable("{id_promo}"  , String.valueOf(codigo));
		top.setDynamicValueSets("PRODUCTOS", prod_promo);
		top.setDynamicValueSets("CATEGORIAS", catsap_promo);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
	
}

