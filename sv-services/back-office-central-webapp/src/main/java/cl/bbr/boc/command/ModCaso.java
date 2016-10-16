package cl.bbr.boc.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosEstadosUtil;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modificar los datos de un caso
 * 
 * @author imoyano
 */

public class ModCaso extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {        
        logger.debug("Comienzo ModCaso Execute");

        String paramUrl = getServletConfig().getInitParameter("TplFile");        
        long idCaso 	= 0;

        //Estos campos pueden modificarse
        String selEstado = "";
        String fechaCompromisoSolucion = "";
        String selJornada = "";
        String comentarioBoc = "Comentario ingresado: ";
        String satisfaccionCliente = "";
        String fechaEntrega = "";
        String horaEntrega = "";
        String escalamiento = "";
        String indicaciones = "";
        String indicacionesOld = "";

        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso"));
        }
        if (req.getParameter("sel_estados") != null) {
            selEstado = req.getParameter("sel_estados");
        }
        if (req.getParameter("fecha_resolucion") != null) {
            fechaCompromisoSolucion = req.getParameter("fecha_resolucion");
        }
        if (req.getParameter("sel_jornada") != null) {
            selJornada = req.getParameter("sel_jornada");
        }
        if (req.getParameter("comentario_log") != null) {
            comentarioBoc += req.getParameter("comentario_log");
        }
        if (req.getParameter("satisfaccion_cliente") != null) {
            satisfaccionCliente += req.getParameter("satisfaccion_cliente");
        }
        if (req.getParameter("fecha_entrega") != null) {
            fechaEntrega = req.getParameter("fecha_entrega");
        }
        if (req.getParameter("hora_entrega") != null) {
            horaEntrega = req.getParameter("hora_entrega");
        }
        if (req.getParameter("caso_escalamiento") != null) {
            escalamiento = req.getParameter("caso_escalamiento");
        }
        if (req.getParameter("indicaciones") != null) {
            indicaciones = req.getParameter("indicaciones");
        }
        if (req.getParameter("indicaciones_copia") != null) {
            indicacionesOld = req.getParameter("indicaciones_copia");
        }

        logger.debug("url: " + paramUrl);
        logger.debug("idCaso: " + idCaso);
        logger.debug("selEstado:" + selEstado);
        logger.debug("fechaCompromisoSolucion:" + fechaCompromisoSolucion);
        logger.debug("Nueva fechaResolucion:" + CasosUtil.cambioFrmFc(fechaCompromisoSolucion));
        logger.debug("selJornada:" + selJornada);
        logger.debug("comentarioBoc ingresado:" + comentarioBoc);
        logger.debug("satisfaccionCliente:" + satisfaccionCliente);
        logger.debug("fechaEntrega:" + fechaEntrega);
        logger.debug("horaEntrega:" + horaEntrega);
        logger.debug("escalamiento:" + escalamiento);        

        BizDelegate bizDelegate = new BizDelegate();

        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
        boolean escalamientoOld = caso.isEscalamiento();
        EstadoCasoDTO estado = new EstadoCasoDTO();
        estado.setIdEstado(Long.parseLong(selEstado));
        caso.setEstado(estado);
        caso.setFcCompromisoSolucion(CasosUtil.cambioFrmFc(fechaCompromisoSolucion));
        JornadaDTO jornada = new JornadaDTO();
        jornada.setIdJornada(Long.parseLong(selJornada));
        caso.setJornada(jornada);
        caso.setSatisfaccionCliente(satisfaccionCliente);
        caso.setFcHoraEntrega(fechaEntrega+" "+horaEntrega);
        if (escalamiento.equalsIgnoreCase("S")) {
            caso.setEscalamiento(true);
        } else {
            caso.setEscalamiento(false);
        }
        caso.setIndicaciones(indicaciones);
        
        String textoSinRetornoCarro="";
        if(caso.getIndicaciones() != null && caso.getIndicaciones().length() > 0){
        	textoSinRetornoCarro = caso.getIndicaciones().replaceAll("[\r]","");
        	caso.setIndicaciones(textoSinRetornoCarro.trim());
        }
        

        if (bizDelegate.modCaso(caso)) {
            logger.debug("Inicio del loggeo");
            if ( !indicaciones.equalsIgnoreCase(indicacionesOld) ) {
                LogCasosDTO log = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_MOD_IND_CASO + caso.getIndicaciones());
                bizDelegate.addLogCaso(log);
            }
            
            if (caso.getEstado().getIdEstado() != CasosEstadosUtil.VERIFICADO && caso.getEstado().getIdEstado() != CasosEstadosUtil.ANULADO && caso.isEscalamiento() != escalamientoOld) {
			    LogCasosDTO log0 = new LogCasosDTO();
	            if (caso.isEscalamiento()) {
	                //enviar mail al supervisor indicando que quedo un caso escalado
	                bizDelegate.enviarMailSupervisor(datosParaMail(caso,fechaCompromisoSolucion));
	                log0 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_ESCALAMIENTO_CASO);
	            } else {
	                log0 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_DESESCALAMIENTO_CASO);
	            }
	            bizDelegate.addLogCaso(log0);            
            }
            LogCasosDTO log1 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_MOD_FIN_CASO + comentarioBoc);
            bizDelegate.addLogCaso(log1);
            logger.debug("Fin del loggeo");

            //una vez modificada, se libera el caso
            if (bizDelegate.setLiberaCaso(idCaso)) {
                logger.debug("Inicio del loggeo");
                LogCasosDTO log2 = new LogCasosDTO(idCaso, estado, usr.getLogin(), "[BOC] " + CasosConstants.LOG_LIB_CASO);
                bizDelegate.addLogCaso(log2);
                logger.debug("Fin del loggeo");
            } else {
                logger.error("No se pudo liberar el caso: " + idCaso);
            }
            paramUrl += "?msje=" + CasosConstants.MSJ_MOD_CASO_EXITO;

        } else {
            paramUrl += "?msje=" + CasosConstants.MSJ_MOD_CASO_ERROR;
        }

        logger.debug("Fin ModCaso Execute");
        res.sendRedirect(paramUrl);
    }

    /**
     * @param caso
     * @return
     */
    private MailDTO datosParaMail(CasoDTO caso, String fechaCompromisoSolucion) {
        BizDelegate biz = new BizDelegate();
        JornadaDTO j = new JornadaDTO();
        try {
            j = biz.getJornadaCasoById(caso.getJornada().getIdJornada());
        } catch (BocException e) {
            e.printStackTrace();
        } 
        MailDTO mail = new MailDTO();
        ResourceBundle rb = ResourceBundle.getBundle("confCasos");
        
        String body = rb.getString("texto");
        body = body.replaceAll("@id_caso",String.valueOf(caso.getIdCaso()));
        body = body.replaceAll("@nro_pedido",String.valueOf(caso.getIdPedido()));
        body = body.replaceAll("@rut_cliente",String.valueOf(caso.getCliRut()) + "-" + caso.getCliDv() );
        body = body.replaceAll("@nombre_cliente",caso.getCliNombre());
        body = body.replaceAll("@telefono1",caso.getCliFonoCod1()+"-"+caso.getCliFonoNum1());
        body = body.replaceAll("@telefono2",caso.getCliFonoCod2()+"-"+caso.getCliFonoNum2());
        body = body.replaceAll("@fecha_jornada",fechaCompromisoSolucion+" " + j.getDescripcion());
        
        mail.setFsm_subject(rb.getString("titulo") + " " + caso.getIdCaso());
		mail.setFsm_data(body);
		mail.setFsm_destina(rb.getString("destinatario")); 
		mail.setFsm_remite(rb.getString("remitente"));
        return mail;
    }
}
