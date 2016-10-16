package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que agrega un nuevo producto generico
 * @author bbr
 *
 */
public class NewGenericProduct extends Command {
	private final static long serialVersionUID = 1;
 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     String paramCodSap = "";
	     String paramAtrdiff_nom = "";
	     String paramTipo = "";
	     int paramMarca = 0;
	     String paramDescr_corta = "";
	     String paramDesc_larga = "";
	     int paramUmedida = 0;
	     double paramContenido = 0;
	     String paramAdmite_com="";
	     String paramPreparable="";
	     String paramUniVenta = "";
	     double paramInterv_med = 0;
	     double paramMaximo = 0;
	     String paramImg1 = "";
	     String paramImg2 = "";
	     
	     String mensaje = getServletConfig().getInitParameter("Mensaje");
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es nulo");}
	     if ( req.getParameter("cod_sap") == null ){throw new ParametroObligatorioException("cod_sap es nulo");}
	     if ( req.getParameter("atr_difer") == null ){throw new ParametroObligatorioException("atr_difer es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramCodSap = req.getParameter("cod_sap");//string:obligatorio:si
	     paramAtrdiff_nom = req.getParameter("atr_difer"); //string:obligatorio:si
	     paramTipo = req.getParameter("tipo"); //string:obligatorio:no
	     paramMarca = Integer.parseInt(req.getParameter("marca")); //int:obligatorio:no
	     paramDescr_corta = req.getParameter("desc_corta"); //string:obligatorio:no
	     paramDesc_larga = req.getParameter("desc_larga"); //string:obligatorio:no
	     paramImg1 = req.getParameter("img1"); //string:obligatorio:no
	     paramImg2 = req.getParameter("img2"); //string:obligatorio:no
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("cod_sap: " + paramCodSap);
	     logger.debug("atr_difer: " + paramAtrdiff_nom);
	     logger.debug("tipo: " + paramTipo);
	     logger.debug("marca: " + paramMarca);
	     logger.debug("descr_corta: " + paramDescr_corta);
	     logger.debug("desc_larga: " + paramDesc_larga);
	     logger.debug("img1: " + paramImg1);
	     logger.debug("img2: " + paramImg2);
	     
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
	     
		 String fec_crea = Formatos.getFecHoraActual();
		 ProcAddProductDTO param = new ProcAddProductDTO();
		 //llenar el objeto param
		 param.setCod_sap(paramCodSap);
		 param.setEstado(Constantes.ESTADO_DESPUBLICADO);		//el producto genérico se crea con estado "Despublicado"
		 param.setGenerico(Constantes.TIPO_GENERICO);		//solo se crea producto genérico
		 param.setTipo(paramTipo);
		 param.setMarca(paramMarca);
		 param.setDesc_corta(paramDescr_corta);
		 param.setDesc_larga(paramDesc_larga);
		 param.setAtr_difer(paramAtrdiff_nom);
		 param.setUni_medida(paramUmedida);
		 param.setContenido(paramContenido);
		 param.setAdm_coment(paramAdmite_com);
		 param.setEs_prepar(paramPreparable);
		 param.setUni_vta(paramUniVenta);
		 param.setInt_medida(paramInterv_med);
		 param.setInt_max(paramMaximo);
		 param.setFecha(fec_crea);
		 param.setId_user(usr.getId_usuario());
		 param.setId_bo(0);
		 param.setImg_min_ficha(paramImg1);
		 param.setImg_ficha(paramImg2);
		 
		 int id_prod = -1;
		 id_prod = biz.setAddProduct(param);
		 logger.debug("se guardo, id:"+id_prod);
		 if(id_prod!=-1){
			 ProductoLogDTO log = new ProductoLogDTO();
			 log.setCod_prod(id_prod);
			 log.setCod_prod_bo(Constantes.LOG_BO_PRO_ID);
			 log.setFec_crea(fec_crea);
			 log.setUsuario(usr.getApe_paterno());
			 log.setTexto(mensaje);
			 int resLog = biz.setLogProduct(log);
			 logger.debug("se guardo log con id:"+resLog);
		 }				
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	 }//execute

}
