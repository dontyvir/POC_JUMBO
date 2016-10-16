package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.actions.ProductosDelegate;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.components.ServicioFactory;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.factory.ProductosFactory;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar los productos
 * 
 * @author bbr
 *  
 */
public class ModProduct extends Command {
   private final static long serialVersionUID = 1;

   private ProductosDelegate prodDelegate = null;    
   
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     
      // 1. seteo de Variables del método
      String paramUrl = "";
      long paramId_producto = 0L;
      String paramTipo = "";
      String paramMarca = "";
      String paramDescr_corta = "";
      String paramDesc_larga = "";
      String paramUmedida = "";
      double paramContenido = 0;
      String paramAdmite_com = "";
      String paramPreparable = "";
      double paramInterv_med = 0;
      double paramMaximo = 0;
      String paramImg1 = "";
      String paramImg2 = "";
      String paramBannerProd = "";
      String paramDescBannerProd = "";
      String paramColorBannerProd = "";
      
      String  paramVal_dif = "";
      String  paramEsParticionable = "N";
      Integer paramParticion = new Integer(0);
      String  paramIsNonFood = "";      
      String  paramTieneFichaTecnica = "";
      String  paramTieneFichaNutricional = "";
      String  paramTieneAltoEn = "";


      logger.debug("Procesando parámetros... ModProduct");

      // 2.1 revision de parametros obligatorios
      if (req.getParameter("url") == null) {
         throw new ParametroObligatorioException("url es null");
      }
      if (req.getParameter("id_producto") == null) {
         throw new ParametroObligatorioException("id_producto es nulo");
      }

      // 2.2 obtiene parametros desde el request
      paramUrl = req.getParameter("url");
      paramId_producto = Long.parseLong(req.getParameter("id_producto")); //long:obligatorio:si

      paramTipo = req.getParameter("tipo"); //string:obligatorio:no
      paramMarca = req.getParameter("marca"); //string:obligatorio:no

      paramDescr_corta = req.getParameter("descr_corta"); //string:obligatorio:no
      paramDesc_larga = req.getParameter("desc_larga"); //string:obligatorio:no

      paramUmedida = req.getParameter("fmedida"); //string:obligatorio:no

      if (req.getParameter("contenido") != null && !req.getParameter("contenido").equals("")) {
         paramContenido = Double.parseDouble(req.getParameter("contenido")); //long:obligatorio:no
      } else {
         paramContenido = 0;
      }

      if (req.getParameter("admite_com") != null)
         paramAdmite_com = req.getParameter("admite_com"); //boolean:obligatorio:no
      if (req.getParameter("preparable") != null)
         paramPreparable = req.getParameter("preparable"); //boolean:obligatorio:no

      if (req.getParameter("interv_med") != null && !req.getParameter("interv_med").equals("")) {
         paramInterv_med = Double.parseDouble(req.getParameter("interv_med")); //long:obligatorio:no
      } else {
         paramInterv_med = 0;
      }
      if (req.getParameter("maximo") != null && !req.getParameter("maximo").equals("")) {
         paramMaximo = Double.parseDouble(req.getParameter("maximo")); //long:obligatorio:no
      } else {
         paramMaximo = 0;
      }
      paramImg1 = req.getParameter("img1"); //string:obligatorio:no
      paramImg2 = req.getParameter("img2"); //string:obligatorio:no
      
      paramBannerProd = req.getParameter("ImgBanner"); //string:obligatorio:no
      paramDescBannerProd = req.getParameter("descBanner"); //string:obligatorio:no 
      paramColorBannerProd = req.getParameter("colorBanner"); //string:obligatorio:no
      paramIsNonFood = req.getParameter("non_food");
     
      paramTieneFichaTecnica = (req.getParameter("ficha_tec") != null ? req.getParameter("ficha_tec") : "0"); //string:obligatorio:no
      paramTieneFichaNutricional = (req.getParameter("ficha_nutri")	!= null ? req.getParameter("ficha_nutri") : "0"); //string:obligatorio:no
      paramTieneAltoEn = (req.getParameter("alto_en") != null ? req.getParameter("alto_en") : "0"); //string:obligatorio:no
      
      //paramTipoFicha = req.getParameter("tipoFicha"); //string:obligatorio:no
      
      paramVal_dif = req.getParameter("atr_dif");

      if (req.getParameter("check_partic") != null) {
         if (req.getParameter("check_partic").toString().equalsIgnoreCase("S")) {
            paramEsParticionable = "S";
            if (req.getParameter("particionable") != null) {
               paramParticion = new Integer(req.getParameter("particionable").toString());
            }
         }
      }
      //Para evitar publicación o despublicación por bloqueo del producto
      String evitarPubDes = req.getParameter("evitar_pub_des");
      
      //Publicado Grability
      //String publGrability = req.getParameter("publ_grability");
      
      String[] locales = req.getParameterValues("local_check");
      if(locales == null)
         locales = new String[0];
      String[] localestienestock = req.getParameterValues("local_tienestock_check");
      if(localestienestock == null)
         localestienestock = new String[0];
      String motivoDes = req.getParameter("motivo_des");
      String obs = req.getParameter("obs");

      // 2.3 log de parametros y valores
      logger.debug("url: " + paramUrl);
      logger.debug("id_producto: " + paramId_producto);
      logger.debug("tipo: " + paramTipo);
      logger.debug("marca: " + paramMarca);
      logger.debug("descr_corta: " + paramDescr_corta);
      logger.debug("desc_larga: " + paramDesc_larga);
      logger.debug("umedida: " + paramUmedida);
      logger.debug("contenido: " + paramContenido);
      logger.debug("admite_com: " + paramAdmite_com);
      logger.debug("preparable: " + paramPreparable);
      logger.debug("interv_med: " + paramInterv_med);
      logger.debug("maximo: " + paramMaximo);
      logger.debug("img1: " + paramImg1);
      logger.debug("img2: " + paramImg2);
      logger.debug("atr_dif: " + paramVal_dif);
      logger.debug("paramEsParticionable: " + paramEsParticionable);
      logger.debug("paramParticion: " + paramParticion);
      logger.debug("evitarPubDes: " + evitarPubDes);
      
      //Publicado Grability
      //logger.debug("publGrability: " + publGrability);
      
      logger.debug("paramIsNonFood: " + paramIsNonFood);
      //logger.debug("paramTieneFichaTecnica: " + paramTieneFichaTecnica);       

      ForwardParameters fp = new ForwardParameters();
      fp.add(req.getParameterMap());
      /*
       * 3. Procesamiento Principal
       */
      BizDelegate biz = new BizDelegate();
      String mensActual = getServletConfig().getInitParameter("MensActual");

      try {
    	  prodDelegate = ServicioFactory.getService(new ProductosFactory()).getInstanceProductosDelegate();
    	  
         ProcModProductDTO modProd = new ProcModProductDTO(paramId_producto, paramTipo, paramMarca, paramDescr_corta,
               paramDesc_larga, paramUmedida, paramContenido, paramAdmite_com, paramPreparable, paramInterv_med,
               paramMaximo, paramImg1, paramImg2, paramVal_dif, usr.getId_usuario(), usr.getLogin(), mensActual);
         modProd.setEsParticionable(paramEsParticionable);
         modProd.setParticion(paramParticion);
         modProd.setEvitarPubDes(evitarPubDes == null ? false : true);
         
         logger.debug("evitarPubDes: " + modProd.isEvitarPubDes());
         
         //Publicado Grability
         //modProd.setPublicadoGrability(publGrability == null ? false : true);
         
         //set de campos banner de productos
         modProd.setImgBannerProducto(paramBannerProd);
         modProd.setDescBannerProducto(paramDescBannerProd);
         modProd.setColBannerProducto(paramColorBannerProd);
         
         boolean resMod = biz.setModProduct(modProd);

         ProductosService serv = new ProductosService();
         if (!serv.tieneSectorPicking((int) modProd.getId_producto()))
            throw new BocException(Constantes._EX_PROD_ID_SECTOR_PICKING);
         serv.publicar(paramId_producto, locales, motivoDes, obs, usr.getLogin());
         serv.setTieneStock(paramId_producto, localestienestock, usr.getLogin());
         
         int isNonFood = paramIsNonFood != null ? Integer.parseInt(paramIsNonFood) : 0;
         if (isNonFood == 1) {
        	 /**
              * Actualiza estado ficha tecnica
              */
             long estadoFichaTecnica = paramTieneFichaTecnica != null ? Long.valueOf(paramTieneFichaTecnica).intValue() : 0;

             boolean resEstadoFichaTecnica = false;
             String msj = estadoFichaTecnica == 1 ? Constantes.MJS_TIENE_FICHA_TECNICA : Constantes.MJS_NO_FICHA_TECNICA;
             resEstadoFichaTecnica = biz.actualizaEstadoFichaTecnica(modProd.getId_producto(), estadoFichaTecnica, usr.getLogin(), msj);
             logger.debug("respuesta estado ficha tecnica?" + resEstadoFichaTecnica);
         }

         guardarTipoFicha(paramTieneFichaNutricional, paramTieneAltoEn, modProd.getId_producto());
         logger.debug("res?" + resMod);
      } catch (BocException e) {
         logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
         String UrlError = getServletConfig().getInitParameter("UrlError");
         
         if (e.getMessage().equals(Constantes._EX_PROD_ID_SECTOR_PICKING)) {
            logger.debug("El producto no tiene sector de picking");
            fp.add("rc", Constantes._EX_PROD_ID_SECTOR_PICKING);
            paramUrl = UrlError + fp.forward();
         }
         if (e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE)) {
            logger.debug("El código de producto ingresado no existe");
            fp.add("rc", Constantes._EX_PROD_ID_NO_EXISTE);
            paramUrl = UrlError + fp.forward();
         }
         if (e.getMessage().equals(Constantes._EX_PROD_ID_MAR_NO_EXISTE)) {
            logger.debug("El código de marca no existe");
            fp.add("rc", Constantes._EX_PROD_ID_MAR_NO_EXISTE);
            paramUrl = UrlError + fp.forward();
         }
         if (e.getMessage().equals(Constantes._EX_PROD_ID_UME_NO_EXISTE)) {
            logger.debug("El código de unidad de medida no existe");
            fp.add("rc", Constantes._EX_PROD_ID_UME_NO_EXISTE);
            paramUrl = UrlError + fp.forward();
         } else {
            logger.debug("Controlando excepción: " + e.getMessage());
         }
      }

      // 4. Redirecciona salida
      res.sendRedirect(paramUrl);

   }//execute

   
   private void guardarTipoFicha(String fnutricional, String altoen, long paramId_producto) throws Exception{
	   List listTipoFicha = new ArrayList();
	   if(fnutricional.equals("1")){
		   listTipoFicha.add("FNUTRICIONAL");
	   }
  
	   if(altoen.equals("1")){
		   listTipoFicha.add("ALTOEN");
	   }
	   boolean isOk = prodDelegate.guardarTipoFicha(listTipoFicha, paramId_producto);
	   logger.info("guardarTipoFicha result:" + isOk);
   }
}