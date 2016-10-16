package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewCrearBolsa extends Command {
	
	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		BizDelegate bizDelegate = new BizDelegate();
		
		
		// 2. Crear bolsa de regalo
		BolsaDTO bolsa = new BolsaDTO();
		String cod_bolsa = new String();
		String desc_bolsa = new String();
		String cod_barra_bolsa = new String();
		String cod_sap = new String();
		
		cod_bolsa = req.getParameter("cod_bolsa");
		desc_bolsa = req.getParameter("desc_bolsa");
		cod_barra_bolsa = req.getParameter("cod_barra_bolsa");
		cod_sap = req.getParameter("cod_sap");
		
		//long acod_sap = bizDelegate.getCodSapGenerico();
		bolsa.setCod_bolsa(cod_bolsa);
		bolsa.setDesc_bolsa(desc_bolsa);
		bolsa.setCod_barra_bolsa(cod_barra_bolsa);
		
		//3. creación de bolsa como producto
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
		
		paramCodSap = cod_sap+"";//string:obligatorio:si
		paramAtrdiff_nom = ""; //string:obligatorio:si
		paramTipo = ""; //string:obligatorio:no
		paramMarca = 869;//marca 'Jumbo' //int:obligatorio:no
		paramDescr_corta = desc_bolsa; //string:obligatorio:no
		paramDesc_larga = desc_bolsa; //string:obligatorio:no
		paramImg1 = ""; //string:obligatorio:no
		paramImg2 = ""; //string:obligatorio:no
		
		logger.debug("cod_sap: " + paramCodSap);
		logger.debug("atr_difer: " + paramAtrdiff_nom);
		logger.debug("tipo: " + paramTipo);
		logger.debug("marca: " + paramMarca);
		logger.debug("descr_corta: " + paramDescr_corta);
		logger.debug("desc_larga: " + paramDesc_larga);
		
		String fec_crea = Formatos.getFecHoraActual();
		ProcAddProductDTO param = new ProcAddProductDTO();
		
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
		param.setId_bo(bizDelegate.getIdProdBO(paramCodSap));
		param.setImg_min_ficha(paramImg1);
		param.setImg_ficha(paramImg2);
		
		int id_prod = -1;
		
		//LPC - primero valido que el producto no exista
		id_prod = (int)bizDelegate.getIdProdFO(paramCodSap);
		if(id_prod==0){
			logger.debug("voy a crear el producto, por que no existe");
			id_prod = bizDelegate.setAddProduct(param);// CREAR PRODUCTO
		} else {
			logger.debug("ya existia el producto");
		}
		
		logger.debug("ID_PRODUCTO=" + id_prod);
		
		bolsa.setId_producto(id_prod);
		bizDelegate.crearBolsaRegalo(bolsa);// CREAR BOLSA
		
		// 4. insertar log bolsas
		bizDelegate.insertarRegistroBitacoraBolsas("Creación de bolsa : \"" + 
				desc_bolsa + "\", código: " + cod_bolsa + ", id producto: " + id_prod,
				usr.getLogin(), (usr.getId_local()+""));
		
		
		String paramUrl = "ViewMonitorBolsas";
		res.sendRedirect(paramUrl);
	}
}
