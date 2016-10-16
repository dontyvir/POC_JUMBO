/*
 * Created on 12-abr-2010
 */
package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.jumbo.common.dto.CategoriaBannerDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author carriagada
 */
public class PasoDosPub extends Command {
   protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
      HttpSession session = arg0.getSession();
      arg0.setCharacterEncoding("UTF-8");
      arg1.setContentType("text/html; charset=iso-8859-1");

      ResourceBundle rb = ResourceBundle.getBundle("fo");
      IValueSet top = new ValueSet();
      PrintWriter out   = arg1.getWriter();
      String pag_form   = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
      String pdefault   = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("default");
      String solobanner = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("solobanner");

      int localId        = Integer.parseInt((String) session.getAttribute("ses_loc_id"));
      int clienteId      = Integer.parseInt((String) session.getAttribute("ses_cli_id"));
      int cabeceraId     = Integer.parseInt(arg0.getParameter("cab"));
      int categoriaId    = Integer.parseInt(arg0.getParameter("int"));
      String invitado_id = "";
      if (session.getAttribute("ses_invitado_id") != null &&
              !session.getAttribute("ses_invitado_id").toString().equals("")){
          invitado_id = session.getAttribute("ses_invitado_id").toString();
      }
      
      
      //Ventana Info DESPACHOS DISPONIBLES 
      if("1".equals(rb.getString("ventana_info_disponibilidad_despacho"))){
	      if (session.getAttribute("ses_cli_id") != null && !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
	          if(session.getAttribute("show_calendario_despacho_Category").toString().equals("P")){		            	
		            session.setAttribute("show_calendario_despacho_Category","N");
		            top.setVariable("{display_calendario_despacho}", "block");	
		       }else{
		          	top.setVariable("{display_calendario_despacho}", "none");	          	  
		       }	            
	      }else{            	
	      	top.setVariable("{display_calendario_despacho}", "none");
	      }
      }else{
      	top.setVariable("{display_calendario_despacho}", "none");
      }
    //Fin Ventana Info DESPACHOS DISPONIBLES   

      /*
       * Recuperar los cupones y los tcp de la sesión (copy paste de versión anterior)
       */
      List lista_tcp = new ArrayList();
      List l_tcp = null;
      if (session.getAttribute("ses_promo_tcp") != null) {
         l_tcp = (List) session.getAttribute("ses_promo_tcp");
         lista_tcp.addAll(l_tcp);
      }
      if (session.getAttribute("ses_cupones") != null) {
         List l_cupones = (List) session.getAttribute("ses_cupones");
         lista_tcp.addAll(l_cupones);
      }
      
      BizDelegate biz = new BizDelegate();
      CategoriaMasvDTO mas = null;
      CategoriaBannerDTO ban = null;
      String nombreCabecera = "";
      String nombreCategoria = "";
      String idCategoria=""; // 05/10/2012 : COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
      List categorias = new ArrayList();
      List despliegue = new ArrayList();
      
      if ((categoriaId <= 0) && (cabeceraId != 0)) { //Es una categoría Cabecera
          CategoriaDTO cat = biz.getCategoria(cabeceraId);
          nombreCabecera = cat.getNombre();
          CategoriaDTO cat1 = null;
          IValueSet nivel0 = new ValueSet();
          List list_categoria = biz.categoriasGetInteryTerm(clienteId, cabeceraId);
          for( int i = 0; i < list_categoria.size() ; i++ ) {
                cat1 = (CategoriaDTO) list_categoria.get(i);
                IValueSet nivel1 = new ValueSet();
                //String url_imagen =  cat1.getUrl_banner();
                //nivel1.setVariable("{url_imagen}", (url_imagen == null ? "" : url_imagen));
                //Intermedias
                nivel1.setVariable("{cat_id}",  cat1.getId()+"");   
                nivel1.setVariable("{cat_nombre}", cat1.getNombre()+"");
                nivel1.setVariable("{cat_tipo}", cat1.getTipo()+"");

 //13112012 VMatheu            
                nivel1.setVariable("{cat_padre}", cat1.getId_padre()+"");
///-13112012 VMatheu
                nivel1.setVariable("{nombre_subcat}", cat1.getNombre()+""); // se agrega
                nivel1.setVariable("{nombre_padre}", nombreCabecera); // se agrega
                
                if ((cat1.getImagen() != null) && (!cat1.getImagen().equals("")))
                    nivel1.setVariable("{cat_imagen}", cat1.getImagen());
                else
                    nivel1.setVariable("{cat_imagen}", "");
                
                //Despliegue de a 4 categorías
                //if (i%4 == 0) 
                    //nivel1.setDynamicValueSets("DIV4", div4);
                
                // Buscar Terminales
                CategoriaDTO cat2 = null;
                List terminales = cat1.getCategorias();
                List aux_terminales = new ArrayList();
                
                IValueSet nivel2 = null;
                for (int  j=0; j< terminales.size(); j++){
                    nivel2 = new ValueSet();
                    cat2 = (CategoriaDTO)terminales.get(j);
                    if (cat1.getId() == cat2.getId_padre()){
                        nivel2.setVariable("{cat_nombre}", cat2.getNombre() + "");
                        nivel2.setVariable("{cat_id}",  cat2.getId()+"");   
                        nivel2.setVariable("{cat_tipo}", cat2.getTipo()+"");
                        nivel2.setVariable("{cat_padre}", cat2.getId_padre()+"");
                        nivel2.setVariable("{cabecera}", cabeceraId + "");
                        nivel2.setVariable("{intermedia}", cat1.getId() + "");
                        nivel2.setVariable("{terminal}", cat2.getId() + "");
                        nivel2.setVariable("{nombre_padre}", cat1.getNombre()+""); // se agrega
                        nivel2.setVariable("{nombre_abuelo}", nombreCabecera); // se agrega
                        nivel2.setVariable("{nombre_subcat}", cat2.getNombre() + ""); // se agrega
                        aux_terminales.add(nivel2);
                    }
                }
                nivel1.setDynamicValueSets("TER", aux_terminales);
                categorias.add(nivel1);
                if ((i+1)%4==0){
                    nivel0.setDynamicValueSets("INT", categorias);
                    categorias = new ArrayList();
                    despliegue.add(nivel0);
                    nivel0 = new ValueSet();
                } else if ((i+1) == list_categoria.size()) {
                    nivel0.setDynamicValueSets("INT", categorias);
                    despliegue.add(nivel0);
                }
          }
          top.setDynamicValueSets("DIV4", despliegue);
          String banner = cat.getBanner();
          String url_banner = cat.getUrl_banner();
          top.setVariable("{banner}", (banner == null ? "" : banner));
          top.setVariable("{url_banner}", (url_banner == null ? "" : url_banner));
           
          
          //mas = biz.categoriaMasv(cabeceraId);
          ban = biz.categoriaBanner(cabeceraId);
      } else if ((cabeceraId != 0) && (categoriaId != 0)) {  //Es una categoría Intermedia
          CategoriaDTO cab = biz.getCategoria(cabeceraId);
          nombreCabecera = cab.getNombre();
          CategoriaDTO cat = biz.getCategoria(categoriaId);
          nombreCategoria = cat.getNombre();
          idCategoria = ""; // 05/10/2012 : COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com}
          //mas = biz.categoriaMasv(categoriaId);
          ban = biz.categoriaBanner(categoriaId);
      }    

      List productos = new ArrayList();
      GregorianCalendar dhoy = new GregorianCalendar();
      dhoy.set(Calendar.HOUR_OF_DAY, 0);
      dhoy.set(Calendar.MINUTE, 0);
      dhoy.set(Calendar.SECOND, 0);
      dhoy.set(Calendar.MILLISECOND, 0);
      long hoy = dhoy.getTimeInMillis();
      
      
      top.setVariable("{nombre}", nombreCategoria);
      top.setVariable("{nombreAnalytics}", Formatos.sanitizeAccentsFO(nombreCategoria));
      
      top.setVariable("{nombreCabecera}", nombreCabecera);
      top.setVariable("{nombreCabeceraAnalytics}", Formatos.sanitizeAccentsFO(nombreCabecera));
      
      top.setVariable("{idCategoria}", ""+idCategoria); // 05/10/2012 : COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com}
      
      TemplateLoader load = new TemplateLoader(solobanner);
      ITemplate tem = load.getTemplate();
      
      if ((mas == null) && (ban != null)) { //si la categoria solo tiene asociado banners
          if (ban.getEstado()) { //si los banners están activados
              if (ban.getBannerPrincipal() != null && !ban.getBannerPrincipal().equals("")
                      && ban.getFechaInicio() != null && ban.getFechaTermino() != null
                      && hoy >= ban.getFechaInicio().getTime() && hoy <= ban.getFechaTermino().getTime()) {
              
                  List vistaBanner = vistaBanner(ban);
                  top.setDynamicValueSets("BANNERS", vistaBanner);
              } 
          } 
      }
      
      String result = tem.toString(top);
      out.print(result);  
   }

   /**
    * @param cat
    * @return
    */
   private List vistaBanner(CategoriaBannerDTO ban) {
      List lista = new ArrayList();
      if (ban.getEstado()) {
         IValueSet valueSet = new ValueSet();
         valueSet.setVariable("{banner_principal}", ban.getBannerPrincipal());
         lista.add(valueSet);
      }
      return lista;
   }

}
