/*
 * Created on 02/04/2012
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bbr.boc.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOPrecioDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class ConsultaProductoSAP extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
   	System.out.println("=ok=");
	res.setCharacterEncoding("UTF-8");
    res.setHeader("Cache-Control", "no-cache");
    PrintWriter salida = res.getWriter();

////    ServletOutputStream out = res.getOutputStream();
    String html = path_html + "Templates/producto.htm";
//    String layout = path_html + "precios/layout.html";
//    ITemplate template = (new TemplateLoader(layout)).getTemplate();
    ITemplate temHtml = (new TemplateLoader(html)).getTemplate();
    //pagina
    String codsap = req.getParameter("codsap");
System.out.println("==" + codsap);
    ProductosService serv = new ProductosService();
    List productos = serv.getProductosBO(codsap);
    int regsperpage = 10;
    System.out.println("=a=");
	ProductosCriteriaDTO criterio = new ProductosCriteriaDTO(1, "", "=" + codsap, regsperpage, true,'0', '0', "", 0);

	System.out.println("=b=");
    
    IValueSet top = vistaProductos2(productos,criterio);
    top.setVariable("{msg}", req.getSession().getAttribute("msg"));
    req.getSession().setAttribute("msg", "");
    top.setVariable("{codsap}", codsap);

System.out.println("==" );
    String result = temHtml.toString(top);
    //end pagina
    
    //IValueSet yield = new ValueSet();
    //yield.setVariable("{yield}", result);
//    salida.print(template.toString(yield));
    salida.print(result);
}

private IValueSet vistaProductos(List productos){
   IValueSet top = new ValueSet();
   List lista = new ArrayList();
   for (int i = 0; i < productos.size(); i++) {
      BOProductoDTO pro = (BOProductoDTO)productos.get(i);
      IValueSet p = new ValueSet();
      p.setVariable("{id}", pro.getId()+"");
      p.setVariable("{descripcion}", pro.getDescripcion());
      p.setVariable("{unidad}", pro.getUnidad());
      p.setVariable("{tipo}", "AA");
      p.setVariable("{marca}", "AA");
      p.setVariable("{precio}", "AA");
      List precios = new ArrayList();
      for (int j = 0; j < pro.getPrecios().size(); j++) {
         BOPrecioDTO pre = (BOPrecioDTO)pro.getPrecios().get(j);
         IValueSet pr = new ValueSet();
         pr.setVariable("{id}", pro.getId()+"");
         pr.setVariable("{local_id}", pre.getLocal().getId()+"");
         pr.setVariable("{local_nom}", pre.getLocal().getNombre());
         pr.setVariable("{precio}", pre.getPrecio());
         pr.setVariable("{bloqueado}", pre.isBloqueado() ? "checked=\"checked\"" :"");
         precios.add(pr);
      }
      p.setDynamicValueSets("PRECIOS", precios);
      lista.add(p);
   }
   top.setDynamicValueSets("PRODUCTOS", lista);
   return top;
}


private IValueSet vistaProductos2(List productosx,ProductosCriteriaDTO criterio){

    // Recupera los paths para las imagenes de los productos
    ResourceBundle rb = ResourceBundle.getBundle("bo");
    String path_img_gr = rb.getString("conf.path_prod_img.grande");
    String path_img_me = rb.getString("conf.path_prod_img.mediana");
    String path_img_ch = rb.getString("conf.path_prod_img.chica");
	
   IValueSet top = new ValueSet();
   List lista = new ArrayList();
   
   

   BizDelegate bizDelegate = new BizDelegate();

   IValueSet p = new ValueSet();
   
   
   for (int i = 0; i < productosx.size(); i++) {
      BOProductoDTO pro = (BOProductoDTO)productosx.get(i);
//      IValueSet p = new ValueSet();
/*      p.setVariable("{id}", pro.getId()+"");
      p.setVariable("{descripcion}", pro.getDescripcion());
      p.setVariable("{unidad}", pro.getUnidad());
      p.setVariable("{tipo}", "AA");
      p.setVariable("{marca}", "AA");
      p.setVariable("{precio}", "AA");
*/      List precios = new ArrayList();
      for (int j = 0; j < pro.getPrecios().size(); j++) {
         BOPrecioDTO pre = (BOPrecioDTO)pro.getPrecios().get(j);
/*         IValueSet pr = new ValueSet();
         pr.setVariable("{id}", pro.getId()+"");
         pr.setVariable("{local_id}", pre.getLocal().getId()+"");
         pr.setVariable("{local_nom}", pre.getLocal().getNombre());
         pr.setVariable("{precio}", pre.getPrecio());
         pr.setVariable("{bloqueado}", pre.isBloqueado() ? "checked=\"checked\"" :"");
         precios.add(pr);
*/
         p.setVariable("{precio_producto}", pre.getPrecio());
                  }
//      p.setDynamicValueSets("PRECIOS", precios);
//      lista.add(p);
   }
   
   
   
try{
	List listProd = bizDelegate.getProductosByCriteria(criterio);
   
   long cod_prod = 0;
   for (int i = 0; i < listProd.size(); i++) {

	IValueSet fila_prod = new ValueSet();
	ProductosDTO prod1 = (ProductosDTO)listProd.get(i);
   	
//      BOProductoDTO pro = (BOProductoDTO)productos.get(i);
      
      cod_prod=prod1.getId();

		p.setVariable("{id}", String.valueOf(cod_prod));
      logger.debug("cod_prod:" + cod_prod);
      if(cod_prod>0){

	      try{
		      ProductosDTO prod = bizDelegate.getProductosById(cod_prod);
		
			  if (prod.getTipo() != null)
		         p.setVariable("{tipo}", prod.getTipo() + "");
		      else
		         p.setVariable("{tipo}", "");
		
		      if (prod.getNom_marca() != null)
		         p.setVariable("{marca}", prod.getNom_marca() + "");
		      else
		         p.setVariable("{marca}", "");
		
		      if (prod.getDesc_corta() != null)
		         p.setVariable("{desc_corta}", prod.getDesc_corta() + "");
		      else
		         p.setVariable("{desc_corta}", "");
		
		      if (prod.getDesc_larga() != null)
		         p.setVariable("{desc_larga}", prod.getDesc_larga() + "");
		      else
		         p.setVariable("{desc_larga}", "");
	
	          p.setVariable("{imagen2}", String.valueOf(prod.getImg_ficha()));
	          p.setVariable("{path_gr}", path_img_gr);
			  
		/*      p.setVariable("{id}", pro.getId()+"");
		      p.setVariable("{descripcion}", pro.getDescripcion());
		      p.setVariable("{unidad}", pro.getUnidad());
		      p.setVariable("{tipo}", "AA");
		      p.setVariable("{marca}", "AA");
		      p.setVariable("{precio}", "AA");
		      List precios = new ArrayList();
		      for (int j = 0; j < pro.getPrecios().size(); j++) {
		         BOPrecioDTO pre = (BOPrecioDTO)pro.getPrecios().get(j);
		         IValueSet pr = new ValueSet();
		         pr.setVariable("{id}", pro.getId()+"");
		         pr.setVariable("{local_id}", pre.getLocal().getId()+"");
		         pr.setVariable("{local_nom}", pre.getLocal().getNombre());
		         pr.setVariable("{precio}", pre.getPrecio());
		         pr.setVariable("{bloqueado}", pre.isBloqueado() ? "checked=\"checked\"" :"");
		         precios.add(pr);
		      }
		      p.setDynamicValueSets("PRECIOS", precios);
		*/      lista.add(p);
	      }catch (Exception ex) {
	        logger.debug("Excepcion:" + ex);
	     }
      }
      else
      {
      	
      }
   }
   top.setDynamicValueSets("PRODUCTOS", lista);
   if(listProd.size()==0){
	top.setDynamicValueSets("SIN_PRODUCTOS", null);
	
	List bloqueados = new ArrayList();
	IValueSet bloq = new ValueSet();
	bloq.setVariable("{sin_productos}","");
	bloqueados.add(bloq);
	top.setDynamicValueSets("SIN_PRODUCTOS", bloqueados);
   }
}
catch(Exception ex){
	
}
   return top;
 
   
}




}
