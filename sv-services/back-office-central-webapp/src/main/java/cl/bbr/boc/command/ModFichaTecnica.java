package cl.bbr.boc.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModFichaTecnicaDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la ficha tecnica del producto
 * 
 * @author bbr
 *  
 */
public class ModFichaTecnica extends Command {   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BizDelegate biz;

protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     
      // 1. seteo de Variables del método
      String paramUrl = "";
      long paramId_producto = 0L;
      String paramTipo = "";
            
      String item[];
      String itemDescripcion[];

      String paramTieneFicha = "";
      long tieneFicha = 0;
      String paramTieneFichaOld = "";
      long tieneFichaOld = 0;
      
      // 2. Procesa parámetros del request

      logger.debug("Procesando parámetros... ModFichaTecnica");

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
            
      item = req.getParameterValues("item") != null ? req.getParameterValues("item") : null;
      itemDescripcion = req.getParameterValues("itemDescripcion") != null ? req.getParameterValues("itemDescripcion") : null;
      
      paramTieneFicha = "0";//req.getParameter("tiene_ficha") != null ? req.getParameter("tiene_ficha") : "0";
      tieneFicha = paramTieneFicha != null ? Long.valueOf(paramTieneFicha).intValue() : 0;
      
      paramTieneFichaOld = req.getParameter("tieneFichaOld") != null ? req.getParameter("tieneFichaOld") : "0";
      tieneFichaOld = paramTieneFichaOld != null ? Long.valueOf(paramTieneFichaOld).intValue() : 0;
            
      logger.debug("tieneFicha: " + tieneFicha);
      logger.debug("tieneFichaOld: " + tieneFichaOld);
      
      boolean cambioEstadoFicha = false;
      if (tieneFicha != tieneFichaOld) {
    	  cambioEstadoFicha = true;
      }
            
      // 2.3 log de parametros y valores
      logger.debug("url: " + paramUrl);
      logger.debug("id_producto: " + paramId_producto);
      logger.debug("tipo: " + paramTipo);
      
      ForwardParameters fp = new ForwardParameters();
      fp.add(req.getParameterMap());
      biz = new BizDelegate();
      String mensActual = getServletConfig().getInitParameter("MensActual");
      
      try {
    	boolean resMod = false; 
    	if (item != null && item.length > 0 && itemDescripcion != null && itemDescripcion.length > 0) {    		
    		//Obtengo el listado de items    		
    		List listItem = biz.getItemFichaProductoAll();    		
    		Map mapItem = new HashMap();
    		//Lleno map items
    		llenaMapItems(listItem, mapItem);    		    		
    		
    		resMod =  biz.eliminaFichaProductoById(paramId_producto);   	
    		boolean actualizaLog = true;
    		
    		if (resMod) {
        		for (int i = 0; i < item.length; i++) {
        			int secuencia = i + 1;
            		ItemFichaProductoDTO itemFichaProductoDTO = null;
            		long idItem = -1;
            		String valorItem = item[i] != null ? item[i] : "";
            		logger.debug("ModFichaTecnica->itemDescripcion["+i+"]: " + valorItem);
            		if (mapItem.containsKey(valorItem.toUpperCase())) {
            			logger.debug("ModFichaTecnica->mapItem.containsKey("+ itemDescripcion[i] +")");
            			itemFichaProductoDTO = (ItemFichaProductoDTO) mapItem.get(valorItem.toUpperCase());
            			if (itemFichaProductoDTO != null) {
            				logger.debug("ModFichaTecnica->itemFichaProductoDTO.getPfiDescripcion().toUpperCase(): " + itemFichaProductoDTO.getPfiDescripcion().toUpperCase());
                			logger.debug("ModFichaTecnica->valorItemDescripcion.toUpperCase(): " + valorItem.toUpperCase());
                			idItem = itemFichaProductoDTO.getPfiItem();
            			}
            		}
            		logger.debug("ModFichaTecnica->idItem : " + idItem);
            		ProcModFichaTecnicaDTO modFichaTecnica = new ProcModFichaTecnicaDTO(paramId_producto, idItem, secuencia, itemDescripcion[i], 1, Constantes.MJS_UPDATE_FICHA_TECNICA, usr.getLogin());
            		logger.debug("iteracion : " + i);
            		logger.debug(modFichaTecnica.toString());

            		resMod = biz.setModFichaTecnica(modFichaTecnica, cambioEstadoFicha, item[i], actualizaLog);
            		
            		if (resMod) {
            			actualizaLog = false;			
            		} else {
            			break;
            		}
        		}
    		}
    		
    		if (resMod && cambioEstadoFicha) {
    			resMod = biz.actualizaEstadoFichaTecnica(paramId_producto, tieneFicha, usr.getLogin(), tieneFicha == 1 ? Constantes.MJS_TIENE_FICHA_TECNICA : Constantes.MJS_NO_FICHA_TECNICA);
                logger.debug("respuesta estado ficha tecnica?" + resMod);
    		}
    	}
    	if (resMod) {
    		paramUrl +="&mensajeFichaTec="+mensActual;    
		} else {
			paramUrl +="&mensajeFichaTec=No se realizaron cambios";
		}
          
        logger.debug("res?" + resMod);    	      	 
      } catch (Exception e) {
        logger.debug("Controlando excepción del ModFichaTecnica: " + e.getMessage());
        String UrlError = getServletConfig().getInitParameter("UrlError");
        fp.add("rc", Constantes._EX_PFT_NO_REALIZO_UPDATE);
        paramUrl +="&mensajeFichaTec="+"No se realizo la modificación.";         
        paramUrl = UrlError + fp.forward();
     }     
      // 4. Redirecciona salida
      res.sendRedirect(paramUrl);

   }//execute

   /**
    * 
    * @param listItem
    */
   private void llenaMapItems(List listItem, Map mapItem) {	       		    
		if (listItem != null && !listItem.isEmpty()) { 			
			for (int i = 0; i < listItem.size(); i++) {
				ItemFichaProductoDTO dto = (ItemFichaProductoDTO) listItem.get(i);				
				if(dto != null && !(mapItem.containsKey(dto.getPfiDescripcion().toUpperCase()))){
					logger.debug("ModFichaTecnica->dto.getPfiDescripcion() : " + dto.getPfiDescripcion());
					logger.debug("ModFichaTecnica->llenaMapItems(" + dto.getPfiDescripcion().toUpperCase() +")");
					mapItem.put(dto.getPfiDescripcion().toUpperCase(), dto);
        	   }
			}
		}
   }      
}