package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.actions.ProductosDelegate;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.components.ServicioFactory;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.boc.dto.MotivoDespDTO;
import cl.bbr.boc.factory.ProductosFactory;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaNutricionalDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * muestra el detalle del producto
 * 
 * @author BBRI
 */
public class ViewProductForm extends Command {
   private final static long serialVersionUID = 1;
   private ProductosDelegate prodDelegate = null; 
   
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

      View salida = new View(res);
      int cod_prod = 0;
      long id_bo = 0L;
      long id_cat_padre = -1;
      String html;
      String tipo_prod = null;
      String rc = "";
      String mensaje = "";
      String mensajeFichaTec = "";
      String msjLeySuperOcho= "";
      String msjFichaNutricional = "";
      //ArrayList items = new ArrayList();
      //ArrayList sugeridos = new ArrayList();

      logger.debug("User: " + usr.getLogin());
      html = getServletConfig().getInitParameter("TplFile");

      // Recupera los paths para las imagenes de los productos
      ResourceBundle rb = ResourceBundle.getBundle("bo");
      String path_img_gr = rb.getString("conf.path_prod_img.grande");
      String path_img_me = rb.getString("conf.path_prod_img.mediana");
      String path_img_ch = rb.getString("conf.path_prod_img.chica");

      //ResourceBundle rb = ResourceBundle.getBundle("bo");  //img\destacados
      String path_url_imgs = rb.getString("conf.path_prod_img.banner.producto");     
      
      
      // Recupera pagina desde web.xml
      if (req.getParameter("tipo_prod") != null) {
         logger.debug("get parameter: tipo_prod: " + req.getParameter("tipo_prod"));
         tipo_prod = req.getParameter("tipo_prod");
         //if (tipo_prod.equals("Generico"))
         if (tipo_prod.equals("G"))
            html = getServletConfig().getInitParameter("TplFile2");
         else
            html = getServletConfig().getInitParameter("TplFile");
      }
      // le aasignamos el prefijo con la ruta
      html = path_html + html;
      logger.debug("html: " + html);

      if (req.getParameter("rc") != null)
         rc = req.getParameter("rc");
      logger.debug("*** rc:" + rc);

      logger.debug("tipo_prod: " + tipo_prod);
      logger.debug("Template: " + html);
      if (req.getParameter("mensaje") != null) {
         mensaje = req.getParameter("mensaje");
      }
      if (req.getParameter("mensajeFichaTec") != null) {
          mensajeFichaTec = req.getParameter("mensajeFichaTec");
      }
      
      if (req.getParameter("msjLeySuperOcho") != null) {
    	  msjLeySuperOcho = req.getParameter("msjLeySuperOcho");
      }
      if (req.getParameter("msjFichaNutricional") != null) {
    	  msjFichaNutricional = req.getParameter("msjFichaNutricional");
      }
      
      
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      //parámetros
      if (req.getParameter("cod_prod") != null)
         cod_prod = new Integer(req.getParameter("cod_prod")).intValue();
      if (req.getParameter("id_producto") != null)
         cod_prod = new Integer(req.getParameter("id_producto")).intValue();
      String sel_cat = "";
      if (req.getParameter("sel_cat") != null)
         sel_cat = req.getParameter("sel_cat");
      if (sel_cat != null && !sel_cat.equals(""))
         id_cat_padre = new Long(sel_cat).longValue();

      // 4. Rutinas Dinámicas
      // 4.0 Bizdelegator
      logger.debug("Codigo de Producto: " + cod_prod);
      BizDelegate bizDelegate = new BizDelegate();
      prodDelegate = ServicioFactory.getService(new ProductosFactory()).getInstanceProductosDelegate();
      
      String uni_med = "";
      String marca = "";
      String generico = "";

      List lst_tipo = bizDelegate.getEstadosByVis("TPR", "S");
      List lst_est = bizDelegate.getEstadosByVis("PR", "S");
      /**
       * Ficha tecnica
       */
      List listFichaProductos = null;
      boolean productoTieneFichaTecnica = false;
      boolean productoTieneFichaNutricional = false;
      boolean productoTieneAltoEn = false;
      List listTipoFicha = null;
      String tipoFicha = null;
      //obtener el producto
      try {
         logger.debug("cod_prod:" + cod_prod);
         ProductosDTO prod = bizDelegate.getProductosById(cod_prod);
         if (!"G".equals(tipo_prod)){
        	 /**
        	  * Datos de la ficha tecnica
        	  */
        	productoTieneFichaTecnica = bizDelegate.tieneFichaProductoById(cod_prod);
        	 
			listFichaProductos = bizDelegate.getFichaProductoPorId(cod_prod);
			  
			//Estado de la ficha tecnica
			listTipoFicha = prodDelegate.obtenerTipoFicha(cod_prod);
			Iterator itList = listTipoFicha.iterator();
			while(itList.hasNext()){
				tipoFicha = (String) itList.next();
				/*if(tipoFicha.equals("FTECNICA")){
				productoTieneFichaTecnica = true;
				}*/
				  
				if(tipoFicha.equals("FNUTRICIONAL")){
					productoTieneFichaNutricional = true;
				}
				  
				if(tipoFicha.equals("ALTOEN")){
					productoTieneAltoEn = true;
				}
			}
         }

         ProductosService serv = new ProductosService();
         List publicacion = serv.getPublicacion(cod_prod);
         List motivos = serv.getMotivosDespublicacion();

         UserDTO usermod = bizDelegate.getUserById(prod.getUser_mod());
         id_bo = prod.getId_bo();
         String nom_ape_usr = usermod.getNombre() + " " + usermod.getApe_paterno();
         top.setVariable("{id_prod}", prod.getId() + "");
         top.setVariable("{cod_prod}", cod_prod + "");
         if (prod.getCod_sap() != null)
            top.setVariable("{cod_sap}", prod.getCod_sap() + "");
         else
            top.setVariable("{cod_sap}", "");

         if (prod.getEstado() != null)
            top.setVariable("{estado}", FormatoEstados.frmEstado(lst_est, prod.getEstado()));
         else
            top.setVariable("{estado}", "");

         if (prod.getGenerico() != null)
            top.setVariable("{generico}", FormatoEstados.frmEstado(lst_tipo, prod.getGenerico()));
         else
            top.setVariable("{generico}", "");
         /**
         * Ficha tecnica
         */
         if (!"G".equals(tipo_prod)) {
        	 
        	 /*if(null != tipoFicha && tipoFicha.equals("FNUTRICIONAL")){
 				top.setVariable("{mrc_tip_fn}", "selected");
 				top.setVariable("{mrc_tip_ft}", "");
 				top.setVariable("{mrc_tip_sf}", "");
        	 }else{ 
        		 if(null == tipoFicha || tipoFicha.equals("FTECNICA") || tipoFicha.equals("NOTFOUND")){
        			 top.setVariable("{mrc_tip_ft}", "selected"); 
        			 top.setVariable("{mrc_tip_fn}", "");
        			 top.setVariable("{mrc_tip_sf}", "");
        		 }else{//NO TIENE FICHA
        			 top.setVariable("{mrc_tip_sf}", "selected");
        			 top.setVariable("{mrc_tip_fn}", "");
        			 top.setVariable("{mrc_tip_ft}", "");
        		 }
        	 }*/
        	 
        	 
        	 if (productoTieneFichaTecnica) {
            	 top.setVariable("{checkfic_1}", "checked");
                 top.setVariable("{checkfic_2}", "");
             } else {
            	 top.setVariable("{checkfic_2}", "checked");
            	 top.setVariable("{checkfic_1}", "");
             }
        	 
        	 if (productoTieneFichaNutricional) {
            	 top.setVariable("{checkficnut_si}", "checked");
                 top.setVariable("{checkficnut_no}", "");
             } else {
            	 top.setVariable("{checkficnut_no}", "checked");
            	 top.setVariable("{checkficnut_si}", "");
             }
        	 
        	 if (productoTieneAltoEn) {
            	 top.setVariable("{checkaltoen_si}", "checked");
                 top.setVariable("{checkaltoen_no}", "");
             } else {
            	 top.setVariable("{checkaltoen_no}", "checked");
            	 top.setVariable("{checkaltoen_si}", "");
             }
        	 

             top.setVariable("{ocultarFila}", "display:table-row;");
             top.setVariable("{ocultarDiv}", "");
             top.setVariable("{nonFood}", "1");
         } else {
        	 top.setVariable("{ocultarFila}", "display:none;");
        	 top.setVariable("{ocultarDiv}", "display:none;");
         }

         if (prod.getTipo() != null)
            top.setVariable("{tipo}", prod.getTipo() + "");
         else
            top.setVariable("{tipo}", "");

         if (prod.getNom_marca() != null)
            top.setVariable("{marca}", prod.getNom_marca() + "");
         else
            top.setVariable("{marca}", "");

         if (prod.getDesc_corta() != null)
            top.setVariable("{desc_corta}", prod.getDesc_corta() + "");
         else
            top.setVariable("{desc_corta}", "");

         if (prod.getDesc_larga() != null)
            top.setVariable("{desc_larga}", prod.getDesc_larga() + "");
         else
            top.setVariable("{desc_larga}", "");

         top.setVariable("{uni_med_id}", prod.getUni_id() + "");
         top.setVariable("{contenido}", prod.getUnidad_medidad() + "");
         if (prod.getTipre() != null)
            top.setVariable("{uni_vta}", prod.getTipre() + "");
         else
            top.setVariable("{uni_vta}", "");

         top.setVariable("{interv_val}", prod.getInter_valor() + "");
         top.setVariable("{interv_max}", prod.getInter_max() + "");
         top.setVariable("{ranking}", prod.getRank_ventas() + "");

         if (prod.getFec_crea() != null)
            top.setVariable("{fec_crea}", Formatos.frmFechaHora(String.valueOf(prod.getFec_crea())));
         else
            top.setVariable("{fec_crea}", "");

         if (prod.getFec_mod() != null)
            top.setVariable("{fec_act}", Formatos.frmFechaHora(String.valueOf(prod.getFec_mod())));
         else
            top.setVariable("{fec_act}", "");

         if (prod.getValor_difer() != null)
            top.setVariable("{atr_dif}", String.valueOf(prod.getValor_difer()));
         else
            top.setVariable("{atr_dif}", "");

         top.setVariable("{usr_act}", nom_ape_usr);

         if (prod.getImg_mini_ficha() != null && !prod.getImg_mini_ficha().equals("")) {
            top.setVariable("{img1}", String.valueOf(prod.getImg_mini_ficha()));
            top.setVariable("{imagen1}", String.valueOf(prod.getImg_mini_ficha()));
         } else {
            top.setVariable("{img1}", "");
            top.setVariable("{imagen1}", "logo2.jpg");
         }
         if (prod.getImg_ficha() != null && !prod.getImg_ficha().equals("")) {
            top.setVariable("{img2}", String.valueOf(prod.getImg_ficha()));
            top.setVariable("{imagen2}", String.valueOf(prod.getImg_ficha()));
         } else {
            top.setVariable("{img2}", "");
            top.setVariable("{imagen2}", "logo2.jpg");
         }
         
         top.setVariable("{url_imgs}",path_url_imgs);
         if (prod.getBanner_prod() != null && !prod.getBanner_prod().equals("")) {
             top.setVariable("{ImgBanner}", String.valueOf(prod.getBanner_prod()));
             top.setVariable("{imaBanner2}", String.valueOf(prod.getBanner_prod()));
          } else {
             top.setVariable("{ImgBanner}", "");
             top.setVariable("{ImgBanner2}", "logo2.jpg");
          }
          if (prod.getDesc_banner_prod() != null && !prod.getDesc_banner_prod().equals("")) {
             top.setVariable("{descBanner}", String.valueOf(prod.getDesc_banner_prod()));
          } else {
             top.setVariable("{descBanner}", "");
          }
          
          if (prod.getColor_banner_prod() != null && !prod.getColor_banner_prod().equals("")) {
              top.setVariable("{colorBanner}", String.valueOf(prod.getColor_banner_prod()));
           } else {
              top.setVariable("{colorBanner}", "");
           }
          
         //			activar el radio button para "admite comentarios" y "es preparable":
         //		cuando el valor sea "S" es SI, cuando el valor sea "N" es NO
         //logger.debug("esAdmCom?" + prod.getAdm_coment());
         //logger.debug("esPrep?" + prod.getEs_prep());
         if (prod.getEs_prep().equals("S")) {
            top.setVariable("{checkprep_1}", "checked");
            top.setVariable("{checkprep_2}", "");
         } else if (prod.getEs_prep().equals("N")) {
            top.setVariable("{checkprep_2}", "checked");
            top.setVariable("{checkprep_1}", "");
         } else {
            top.setVariable("{checkprep_2}", "");
            top.setVariable("{checkprep_1}", "");
         }

         if (prod.getAdm_coment().equals("S")) {
            top.setVariable("{checkcom_1}", "checked");
            top.setVariable("{checkcom_2}", "");
         } else if (prod.getAdm_coment().equals("N")) {
            top.setVariable("{checkcom_2}", "checked");
            top.setVariable("{checkcom_1}", "");
         } else {
            top.setVariable("{checkcom_2}", "");
            top.setVariable("{checkcom_1}", "");
         }

         //particionable
         if (prod.getEsParticionable().equalsIgnoreCase("S")) {
            top.setVariable("{checkpart_1}", "checked");
            top.setVariable("{checkpart_2}", "");
            top.setVariable("{particionable}", "" + prod.getParticion());
         } else {
            top.setVariable("{checkpart_1}", "");
            top.setVariable("{checkpart_2}", "checked");
            top.setVariable("{particionable}", "");
         }

         IValueSet newTopLey = setCheckLeySuperOcho(cod_prod, top);
         if(newTopLey!=null){
        	 top =  newTopLey;
         }
         IValueSet valorSet = null;
         List listaFichaNutricional = prodDelegate.listaFichaNutricional(cod_prod);
         List camposFichaNutricionalList = new ArrayList(); 
         if (listaFichaNutricional != null && listaFichaNutricional.size() > 0) {
        	Iterator ite = listaFichaNutricional.iterator();
        	int cont = 0;
        	while(ite.hasNext()){
        		valorSet = new ValueSet();
        		FichaNutricionalDTO dto = (FichaNutricionalDTO)ite.next();
        		if(cont == 0){
        			top.setVariable("{itemCabecera}", dto.getCabecera() != null && dto.getCabecera().length() > 0 ? dto.getCabecera() : "");
        		}
        		cont = cont + 1; 
        		valorSet.setVariable("{itemContadorFN}", "" + cont);
        		valorSet.setVariable("{itemSecuenciaFN}", "Item " + (cont + 1));
        		valorSet.setVariable("{itemFN}", dto.getItem());
        		valorSet.setVariable("{itemDescripcionFN}", dto.getDescripcion() != null && dto.getDescripcion().length() > 0 ? dto.getDescripcion() : "");
        		valorSet.setVariable("{itemDescripcion2FN}", dto.getDescripcion2() != null && dto.getDescripcion2().length() > 0 ? dto.getDescripcion2() : "");
        		camposFichaNutricionalList.add(valorSet);
        	}
        	top.setVariable("{totalFN}", "" + cont);
        	top.setDynamicValueSets("MOSTRAR_FICHA_NUTRICIONAL", camposFichaNutricionalList);
         }else{
        	 top.setVariable("{itemCabecera}","");
        	 top.setVariable("{totalFN}", "0");
         }
         
         uni_med = prod.getUni_id() + "";
         marca = prod.getMar_id() + "";
         generico = prod.getGenerico();
         top.setVariable("{gen}", generico);
         logger.debug("Este valor lleva el generico: " + generico);
         if (generico.equals("G")) {
            top.setVariable("{visible}", "");
         } else {
            top.setVariable("{visible}", "disabled");
         }
         logger.debug("estado:" + prod.getEstado());
         if (prod.getEstado().equals("A")) {
            top.setVariable("{accion}", "2");
            top.setVariable("{nom_boton}", "    Despublicar   ");
            top.setVariable("{tipo_form}", "D");
         } else if (prod.getEstado().equals("D")) {
            top.setVariable("{accion}", "1");
            top.setVariable("{nom_boton}", "    Publicar   ");
            top.setVariable("{tipo_form}", "P");
         } else {
            top.setVariable("{tipo_form}", "N");
            top.setVariable("{accion}", "1");
            top.setVariable("{nom_boton}", "    Publicar   ");
         }
         
         // Pilas nutricionales
         top.setVariable("{porcion_nutriente}", ""+prod.getPilaPorcion());
         ArrayList unidadesPorcion = new ArrayList();
         List lUnidadesPorcion = bizDelegate.getUnidadesNutricionales();
         for (int i = 0; i < lUnidadesPorcion.size(); i++) {
            IValueSet filaUP = new ValueSet();
            PilaUnidadDTO uni = (PilaUnidadDTO) lUnidadesPorcion.get(i);
            filaUP.setVariable("{id_unidad}", String.valueOf(uni.getIdPilaUnidad()));
            filaUP.setVariable("{descripcion}", uni.getDescripcion());
            if ( prod.getIdPilaUnidad() == uni.getIdPilaUnidad() ) {
                filaUP.setVariable("{select_unidad}", "selected");
            } else
                filaUP.setVariable("{select_unidad}", "");
            unidadesPorcion.add(filaUP);
         }
         
         ArrayList pilas = new ArrayList();
         List lPilas = bizDelegate.getPilasNutricionales();
         for (int i = 0; i < lPilas.size(); i++) {
            IValueSet filaP = new ValueSet();
            PilaNutricionalDTO pila = (PilaNutricionalDTO) lPilas.get(i);
            filaP.setVariable("{id_pila}", String.valueOf(pila.getIdPila()));
            filaP.setVariable("{nombre_nutriente}", pila.getNutriente());
            filaP.setVariable("{unidad}", pila.getUnidad().getUnidad());
            pilas.add(filaP);
         }         
         
         top.setDynamicValueSets("UNIDADES_PORCION", unidadesPorcion);
         top.setDynamicValueSets("NUTRIENTES", pilas);
         
         
         logger.debug("Id producto: " + prod.getId());
         top.setVariable("{url}", "ViewProductForm?cod_prod=" + cod_prod + "&tipo_prod=" + tipo_prod);

         top.setVariable("{evitar_pub_des_checked}", prod.isEvitarPubDes() ? "checked" : "");

         top.setDynamicValueSets("PUBLICACION", vistaPublicacionLocales(publicacion));
         
         top.setDynamicValueSets("MOTIVOS", vistaMotivosDesp(motivos, prod.getMotivoDesId()));
         
         // seteo campo Publicado Grability
         top.setVariable("{publ_grability_checked}", prod.isPublicadoGrability() ? "checked" : "");

      } catch (Exception ex) {
         logger.debug("Excepcion:" + ex);
         ex.printStackTrace();
         throw new Exception("Problemas parte I", ex);
      }

      //ver marcas
      ArrayList marcas = new ArrayList();
      List listMarcas = bizDelegate.getMarcas();

      for (int i = 0; i < listMarcas.size(); i++) {
         IValueSet fila_tip = new ValueSet();
         MarcasDTO tip1 = (MarcasDTO) listMarcas.get(i);
         fila_tip.setVariable("{mrc_id}", String.valueOf(tip1.getId()));
         fila_tip.setVariable("{mrc_nombre}", String.valueOf(tip1.getNombre()));

         if (marca.equals(String.valueOf(tip1.getId()))) {
            fila_tip.setVariable("{mrc_tip}", "selected");
         } else
            fila_tip.setVariable("{mrc_tip}", "");
         marcas.add(fila_tip);

      }

      //ver unidades de medida
      ArrayList unids = new ArrayList();
      List listUniMed = bizDelegate.getUnidMedida();

      for (int i = 0; i < listUniMed.size(); i++) {
         IValueSet fila_tip = new ValueSet();
         UnidadMedidaDTO tip1 = (UnidadMedidaDTO) listUniMed.get(i);
         fila_tip.setVariable("{ume_id}", String.valueOf(tip1.getId()));
         fila_tip.setVariable("{ume_nombre}", String.valueOf(tip1.getDesc()));

         if (uni_med.equals(String.valueOf(tip1.getId()))) {
            fila_tip.setVariable("{ume_tip}", "selected");
            //top.setVariable("{ume_checked}", String.valueOf(tip1.getId()));
         } else
            fila_tip.setVariable("{ume_tip}", "");
         unids.add(fila_tip);

      }

      //arbol de categorias
      ArrayList arbCategorias = new ArrayList();
      CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(1, 'A', 'T', 10, true, "", "", sel_cat);
      try {
         //List listArbCate = bizDelegate.getCategoriasByCriteria(criterioArbolCat);
         List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, id_cat_padre);

         logger.debug("arbCategorias -> " + listArbCate.size());

         for (int i = 0; i < listArbCate.size(); i++) {
            IValueSet fila_cat = new ValueSet();
            CategoriasDTO cat1 = (CategoriasDTO) listArbCate.get(i);
            fila_cat.setVariable("{nav_id_cat}", String.valueOf(cat1.getId_cat()));
            fila_cat.setVariable("{nav_nom_cat}", String.valueOf(cat1.getNombre()));
            if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))) {
               fila_cat.setVariable("{sel_cat}", "selected");
            } else
               fila_cat.setVariable("{sel_cat}", "");

            arbCategorias.add(fila_cat);

         }
      } catch (Exception ex) {
         logger.debug("err" + ex.getMessage());
      }
      top.setVariable("{tipo1}", tipo_prod);
      top.setVariable("{mns}", "");
      if (rc.equals(Constantes._EX_PROD_ID_SECTOR_PICKING)) {
         top.setVariable("{mns}", "<script language='JavaScript'>alert('El producto no tiene sector de picking');</script>");
      }
      
      if (rc.equals(Constantes._EX_PROD_ID_NO_EXISTE)) {
         top.setVariable("{mns}", "<script language='JavaScript'>alert('El código del producto no existe');</script>");
      }
      if (rc.equals(Constantes._EX_PROD_CAT_NO_TIENE)) {
         top
               .setVariable("{mns}",
                     "<script language='JavaScript'>alert('El producto no tiene categoria relacionada. No puede ser publicada.');</script>");
      }
      if (rc.equals(Constantes._EX_PROD_SIN_CAT)) {
         top.setVariable("{mns}",
               "<script language='JavaScript'>alert('Se eliminó la última categoría del producto.');</script>");
      }
      if (rc.equals(Constantes._EX_ID_SECTOR_INVALIDO)) {
         top.setVariable("{mns}",
               "<script language='JavaScript'>alert('El código del sector ingresado no existe.');</script>");
      }
      if (rc.equals(Constantes._EX_PSAP_ID_NO_EXISTE)) {
         top.setVariable("{mns}",
               "<script language='JavaScript'>alert('El código del producto ingresado no existe.');</script>");
      }

      //Sectores
      logger.debug("en getSectores");

      ArrayList sector = new ArrayList();

      boolean tiene_sector = false;
      IValueSet fila = new ValueSet();
      fila.setVariable("{cod_prod}", String.valueOf(cod_prod));
      fila.setVariable("{id_bo}", id_bo + "");

      ArrayList sectores = new ArrayList();

      List sectores_list = bizDelegate.getSectores();
      logger.debug("sectores_list.size: " + sectores_list.size());

      logger.debug("id_bo:" + id_bo);
      long id_sector1 = bizDelegate.getSectorByProdId(id_bo);
      if (id_sector1 > 0) {
         tiene_sector = true;
      }

      logger.debug("id_sector1:" + id_sector1);

      for (int j = 0; j < sectores_list.size(); j++) {
         IValueSet fila_sect = new ValueSet();
         SectorLocalDTO sector1 = (SectorLocalDTO) sectores_list.get(j);
         fila_sect.setVariable("{id_sector}", String.valueOf(sector1.getId_sector()));
         fila_sect.setVariable("{sector}", sector1.getNombre());
         if (id_sector1 == sector1.getId_sector()) {
            fila_sect.setVariable("{sec_tip}", "selected");
         } else {
            fila_sect.setVariable("{sec_tip}", "");
         }
         fila.setVariable("{sin_dato}", Constantes.SIN_DATO);
         sectores.add(fila_sect);
         fila.setDynamicValueSets("select_sectores", sectores);
      }
      sector.add(fila);

      if (tiene_sector) {
         top.setVariable("{flg_pub}", "1");
      } else {
         top.setVariable("{flg_pub}", "0");
      }

      /*
       * Ficha tecnica
       * Obtener ficha tecnica del producto
       */
      
       if (!"G".equals(tipo_prod)) {
    	   int cantidadItems = rb.getString("nro_item_ficha_tecnica") != null ? Integer.parseInt(rb.getString("nro_item_ficha_tecnica")) : 0;
           top.setVariable("{cantItems}", String.valueOf(cantidadItems));
        	  
           List camposFichaList = new ArrayList();          
           if (listFichaProductos != null && listFichaProductos.size() > 0) {
        	   logger.debug("listFichaProductos size :[" + listFichaProductos.size() + "]");

        	   List listItems = bizDelegate.getItemFichaProductoAll();
        	   Map mapItem = new HashMap();
        	   //Lleno map items
        	   llenaMapItems(listItems, mapItem);

        	   ItemFichaProductoDTO itemDto = null;
        	   int cont = 0;
        	   for (int i = 0; i < listFichaProductos.size(); i++) {
        		   FichaProductoDTO fichaDto = (FichaProductoDTO) listFichaProductos.get(i);
        		   if (fichaDto != null) {
        			   String itemDescripcion = "Sin descripci&oacute;n";
        			   if (mapItem.get(String.valueOf(fichaDto.getPftPfiItem())) != null) {
        				   itemDto = (ItemFichaProductoDTO) mapItem.get(String.valueOf(fichaDto.getPftPfiItem()));
        				   itemDescripcion = itemDto.getPfiDescripcion();
        			   }
        			   logger.debug("ViewProductForm->Ficha-> valor item[" +i+ "]: " + itemDescripcion);
    				   logger.debug("ViewProductForm->Ficha-> valor item descripcion:["+i+"]" + fichaDto.getPftDescripcionItem());
    				   IValueSet detalleFicha = new ValueSet();
    				   cont = i + 1; 
    				   detalleFicha.setVariable("{itemSecuencia}", "Item " + cont);
    				   detalleFicha.setVariable("{item}", itemDescripcion);
    				   detalleFicha.setVariable("{itemDescripcion}", fichaDto.getPftDescripcionItem() != null && fichaDto.getPftDescripcionItem().length() > 0 ? fichaDto.getPftDescripcionItem() : "");
    				   camposFichaList.add(detalleFicha);
        		   }    		
        		}				  
        		top.setDynamicValueSets("MOSTRAR_FICHA", camposFichaList);
          } else {
        	  logger.debug("ViewProductForm->Mostrar fila ficha tecnica");    		  
        	  IValueSet filaFicha = new ValueSet();
        	  filaFicha.setVariable("{filaPadreItem}", "filaPadreItem");
        	  List campoFilaList = new ArrayList();
        	  campoFilaList.add(filaFicha);
        	  top.setDynamicValueSets("MOSTRAR_FILA_FICHA", campoFilaList);
          }

          //VALOR OLD TIENE FICHA
          top.setVariable("{tieneFichaOld}", productoTieneFichaTecnica ? "1" : "0");
       }

      // Setea variables de los paths de las imagenes
      top.setVariable("{path_gr}", path_img_gr);
      top.setVariable("{path_me}", path_img_me);
      top.setVariable("{path_ch}", path_img_ch);
      if (mensaje == null) {
         top.setVariable("{mensaje}", " ");
      } else {
         top.setVariable("{mensaje}", mensaje);
      }      
      if (mensajeFichaTec == null) {
          top.setVariable("{mensajeFichaTec}", " ");
      } else {
          top.setVariable("{mensajeFichaTec}", mensajeFichaTec);
      }    

      //Ley Super Ocho
      if(msjLeySuperOcho == null){
    	  top.setVariable("{msjLeySuperOcho}", " ");
      }else{
    	  top.setVariable("{msjLeySuperOcho}", msjLeySuperOcho);
      }
      if(msjFichaNutricional == null){
    	  top.setVariable("{msjFichaNutricional}", " ");
      }else{
    	  top.setVariable("{msjFichaNutricional}", msjFichaNutricional);
      }
      
      //		Productos
      top.setVariable("{action}", "AddProdCatWeb");

      // 6. Setea variables bloques
      top.setDynamicValueSets("MARCAS", marcas);
      top.setDynamicValueSets("UNI_MED", unids);
      top.setDynamicValueSets("SEL_CATEGORIA_NAV", arbCategorias);
      top.setDynamicValueSets("select_sector", sector);

      // Setea variables del header
      top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
      Date now = new Date();
      top.setVariable("{hdr_fecha}", now.toString());

      String result = tem.toString(top);

      salida.setHtmlOut(result);
      salida.Output();
   }

   /**
    * @param publicacion
    * @return
    */
   private List vistaPublicacionLocales(List publicacion) {
      List lista = new ArrayList();
      for (int i = 0; i < publicacion.size(); i++) {
         IValueSet val = new ValueSet();
         LocalDTO localDTO = (LocalDTO) publicacion.get(i);
         val.setVariable("{local_id}", localDTO.getId() + "");
         val.setVariable("{local_nombre}", localDTO.getNombre());
         val.setVariable("{local_checked}", (localDTO.isPublicado() ? "checked" : ""));
         val.setVariable("{local_tienestock_checked}", (localDTO.tieneStock() ? "checked" : ""));
         lista.add(val);
      }
      return lista;
   }

   private List vistaMotivosDesp(List motivos, int motivoDesId) {
      List lista = new ArrayList();
      IValueSet val = new ValueSet();
      val.setVariable("{motivo_id}", "-1");
      val.setVariable("{motivo_nombre}", "Seleccione");
      val.setVariable("{selected}", (motivoDesId == -1 ? "selected" : ""));
      lista.add(val);
      for (int i = 0; i < motivos.size(); i++) {
         val = new ValueSet();
         MotivoDespDTO mot = (MotivoDespDTO) motivos.get(i);
         val.setVariable("{motivo_id}", mot.getId() + "");
         val.setVariable("{motivo_nombre}", mot.getMotivo());
         val.setVariable("{selected}", (mot.getId() == motivoDesId ? "selected" : ""));
         lista.add(val);
      }
      return lista;
   }
   
   /**
    * 
    * @param listItem
    * @param mapItem
    */
   private void llenaMapItems(List listItem, Map mapItem) {
	   if (listItem != null && !listItem.isEmpty()) {
		   for (int i = 0; i < listItem.size(); i++) {
			   ItemFichaProductoDTO dto = (ItemFichaProductoDTO) listItem.get(i);
				if(!mapItem.containsKey(String.valueOf(dto.getPfiItem()))  ){
					mapItem.put(String.valueOf(dto.getPfiItem()), dto);
				}
			}
		}
   }
   
   
   private IValueSet setCheckLeySuperOcho(int codigoProducto, IValueSet top){
	   try{
		   List lista = prodDelegate.listaLeySuperOcho(codigoProducto);
		   Iterator ite = lista.iterator();
			cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO dto = null;
			//while(ite.hasNext()){
				ite.hasNext();
				dto = (cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO)ite.next();
				if(dto.getExcesoAzuraces()=='P'){
					  top.setVariable("{checkEA}", "checked='checked'");
				}else{
					  top.setVariable("{checkEA}", "");
				}
				
				if(dto.getExcesoCalorias()=='P'){
					  top.setVariable("{checkEC}", "checked='checked'");
				}else{
					  top.setVariable("{checkEC}", "");
				}
				if(dto.getExcesoGrasasSaturadas()=='P'){
					  top.setVariable("{checkEGS}", "checked='checked'");
				}else{
					  top.setVariable("{checkEGS}", "");
				}
				if(dto.getExcesoSodio()=='P'){
					  top.setVariable("{checkES}", "checked='checked'");
				}else{
					  top.setVariable("{checkES}", "");
				}
				//break;
				return top;
			//}
	   }catch(Exception ex){
		   logger.debug("Error con check (Ficha Nutricional / Super8) ");
	   }
	   return null;
   }
   
   /*
   private IValueSet setFichaNutricional(int codigoProducto, IValueSet top){
	   try{
		   List lista = prodDelegate.listaFichaNutricional(codigoProducto);
		   Iterator ite = lista.iterator();
		   cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO dto = null;
		   
	   }catch(Exception ex){
		   logger.debug("Error con check (Ficha Nutricional / Super8) ");
	   }
	   return null;   
   }
   */
}