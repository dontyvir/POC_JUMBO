package cl.bbr.boc.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agrega un nuevo caso
 * 
 * @author imoyano
 */

public class AddCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddCaso Execute");

        String paramUrl = "";
        String UrlError = getServletConfig().getInitParameter("UrlError");
        String UrlExito = getServletConfig().getInitParameter("UrlExito");
        String UrlExitoCall = getServletConfig().getInitParameter("UrlExitoCallCenter");
        
        ResourceBundle rb = ResourceBundle.getBundle("confCasos");
        long idCallCenter = Long.parseLong(rb.getString("id_call_center"));
        boolean esCallCenter = CasosUtil.esCallCenter(usr, idCallCenter);
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        //fp.add(req.getParameterMap());

        try {
            
            if (biz.getCasoEnEdicionByUsuario(usr) != 0) {
                logger.debug("Usuario ya creó un caso y está en edición, no puede crear otro... al parecer hizo back con el navegador.");
                
                fp.add("mensaje_error", CasosConstants.MSJ_ADD_CASO_ERROR);
                paramUrl = UrlError + fp.forward();
                
            } else {
                logger.debug("Vamos a crear el nuevo caso...");
	            // Ingreso del caso exitoso
	            CasoDTO caso = new CasoDTO();
	            caso.setIdUsuarioEdit(usr.getId_usuario());
	
	            caso.setIdPedido(Long.parseLong(req.getParameter("num_pedido")));
	            caso.setDireccion(req.getParameter("direccion"));
	            caso.setComuna(req.getParameter("comuna"));
	            caso.setFcPedido(req.getParameter("fecha_pedido"));
	            caso.setFcDespacho(req.getParameter("fecha_despacho"));
	            caso.setCliRut(Long.parseLong(req.getParameter("cli_rut")));
	            caso.setCliDv(req.getParameter("cli_dv"));
	            caso.setCliNombre(req.getParameter("cli_nombre"));
	            caso.setCliFonoCod1(req.getParameter("cli_fon_cod1"));
	            caso.setCliFonoNum1(req.getParameter("cli_fon_num1"));
	            caso.setCliFonoCod2(req.getParameter("cli_fon_cod2"));
	            caso.setCliFonoNum2(req.getParameter("cli_fon_num2"));
	            caso.setCliFonoCod3(req.getParameter("cli_fon_cod3"));
	            caso.setCliFonoNum3(req.getParameter("cli_fon_num3"));
	            ObjetoDTO local = new ObjetoDTO();
	            local.setIdObjeto(Long.parseLong(req.getParameter("sel_local")));
	            caso.setLocal(local);
	            caso.setIdUsuFonoCompra(Long.parseLong(req.getParameter("id_usu_fono")));
	            caso.setUsuFonoCompraNombre(req.getParameter("usu_fono_nombre"));
	            caso.setIndicaciones(req.getParameter("indicaciones"));
	            EstadoCasoDTO estado = new EstadoCasoDTO();
	            estado.setIdEstado(Long.parseLong(req.getParameter("sel_estados")));
	            caso.setEstado(estado);
	            caso.setFcCompromisoSolucion(req.getParameter("fecha_resolucion"));
	            JornadaDTO jornada = new JornadaDTO();
	            jornada.setIdJornada(Long.parseLong(req.getParameter("sel_jornada")));
	            caso.setJornada(jornada);
                if ( req.getParameter("comentario_call") != null) {
                    caso.setComentarioCallCenter(req.getParameter("comentario_call"));    
                }
                
                //(13/10/2014) Req. Mejoras al Monitor de Casos (Extras) - NSepulveda.
                logger.debug("[AddCaso.java] [caso.getIndicaciones()] Largo de String Indicaciones es: ["+caso.getIndicaciones().length()+"]");
                String textoSinRetornoCarro="";
                if(!caso.getIndicaciones().equalsIgnoreCase("")){
                	textoSinRetornoCarro = caso.getIndicaciones().replaceAll("[\r]","");
                	logger.debug("[AddCaso.java] [textoSinRetornoCarro] Largo String textoSinRetornoCarro es: ["+textoSinRetornoCarro.trim().length()+"]");
                	
                	caso.setIndicaciones(textoSinRetornoCarro.trim());
                }
	            	
	            long idCaso = biz.addCaso(caso);
	
	            logger.debug("Inicio del loggeo");
                LogCasosDTO log1 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_ADD_CASO);
	            biz.addLogCaso(log1);
	            if ( !caso.getIndicaciones().equalsIgnoreCase("") ) {
	            	
                    LogCasosDTO log = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_ADD_IND_CASO + caso.getIndicaciones());
                    biz.addLogCaso(log);
                }
                logger.debug("Fin del loggeo");
	            
                if ( esCallCenter ) {
                    biz.setLiberaCaso(idCaso);
                    fp.add("msje", "El caso N°" + idCaso + " ha sido PRE INGRESADO con éxito");
                    paramUrl = UrlExitoCall + fp.forward();
                } else {
                    fp.add("mensaje_sistema", CasosConstants.MSJ_ADD_CASO_EXITO);
                    fp.add("id_caso", String.valueOf(idCaso));
                    paramUrl = UrlExito + fp.forward();    
                }
	            
            }

        } catch (BocException e) {
            fp.add("mensaje_error", CasosConstants.MSJ_ADD_CASO_ERROR);
            paramUrl = UrlError + fp.forward();
        }

        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin AddCaso Execute");
        res.sendRedirect(paramUrl);
    }
}
