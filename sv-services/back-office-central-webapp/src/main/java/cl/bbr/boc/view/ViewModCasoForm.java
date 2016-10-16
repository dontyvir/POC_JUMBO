package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosEstadosUtil;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el Formulario para modificar un caso
 * 
 * @author imoyano
 */
public class ViewModCasoForm extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ViewModCasoForm Execute");

        //Variables
        String mensajeSistema 	= "";
        long idCaso 			= 0;
        int pagina 				= 0;
        long idUsuarioEditor	= 0;
        boolean aLiberar		= false;
        long casoEnEdicion		= 0;

        View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        logger.debug("Template: " + html);

        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        //Sacamos la info de la pagina
        if (req.getParameter("mensaje_sistema") != null) {
            mensajeSistema = req.getParameter("mensaje_sistema").toString();
        }
        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso").toString());
        }
        if (req.getParameter("pagina") != null) {
            pagina = Integer.parseInt(req.getParameter("pagina").toString());
        }
        if (req.getParameter("mod") != null) {
            if(Integer.parseInt(req.getParameter("mod").toString()) == 1) {
                aLiberar = true;
            }
        }

        BizDelegate bizDelegate = new BizDelegate();
        CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
        
        ResourceBundle rb = ResourceBundle.getBundle("confCasos");
        long idSupervisor = Long.parseLong(rb.getString("id_supervisor_boc"));
        
        //Validar q el caso no esté en edición
        idUsuarioEditor = bizDelegate.getIdUsuarioEditorDeCaso(idCaso);        
        casoEnEdicion = bizDelegate.getCasoEnEdicionByUsuario(usr);
        
        if (idUsuarioEditor != 0) {
            if (idUsuarioEditor != usr.getId_usuario()) {
                logger.debug("El caso no puede ser editado, ya que lo está editando el usuario: " + idUsuarioEditor);
                //mandar error
                UserDTO editor = bizDelegate.getUserById(idUsuarioEditor);
                res.sendRedirect( cmd_dsp_error +"?mensaje="+CasosConstants.MSJ_MOD_CASO_EN_EDICION+editor.getLogin()+"&PagErr=1");
                return;
            }            
        }
        if (casoEnEdicion != 0) {
            if (casoEnEdicion != idCaso) {
                logger.debug("El usuario no puede editar el caso, ya que esta editando uno.");
                //mandar error
                res.sendRedirect( cmd_dsp_error +"?mensaje="+CasosConstants.MSJ_MOD_CASO_USUARIO+casoEnEdicion+"&PagErr=1");
                return;
            }            
        }
        if ( caso.getEstado().getIdEstado() == CasosEstadosUtil.ANULADO ) {
            logger.debug("El caso no puede ser editado, ya que el estado no lo permite. (ANULADO)");
            //mandar error
            res.sendRedirect( cmd_dsp_error +"?mensaje="+CasosConstants.MSJ_MOD_CASO_FINALIZADO+"&PagErr=1");
            return;            
        }        
        if ( caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO && !CasosUtil.esSupervisor(usr, idSupervisor) ) {
            logger.debug("El caso no puede ser editado, ya que el estado no lo permite. (VERIFICADO)");
            //mandar error
            res.sendRedirect( cmd_dsp_error +"?mensaje="+CasosConstants.MSJ_MOD_CASO_FINALIZADO+"&PagErr=1");
            return;            
        }
        //El caso no esta siendo editado, x lo tanto lo loggeamos
        logger.debug("Inicio del loggeo");
        LogCasosDTO log = new LogCasosDTO(idCaso, caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_MOD_INI_CASO);
        bizDelegate.addLogCaso(log);
        logger.debug("Fin del loggeo");
    
        if ( !bizDelegate.setModCaso(idCaso, usr.getId_usuario()) ) {
            logger.error("No se pudo setear el caso en edición con el usuario:" + usr.getId_usuario());
        }

        // ---- estados de casos ----
        List listestados = bizDelegate.getEstadosDeCasos();
        ArrayList edos = new ArrayList();
        for (int i = 0; i < listestados.size(); i++) {
            IValueSet fila = new ValueSet();
            EstadoCasoDTO estados = (EstadoCasoDTO) listestados.get(i);
            if (CasosEstadosUtil.mostrarEstado(caso.getEstado().getIdEstado(),estados.getIdEstado(), "BOC") || caso.getEstado().getIdEstado() == estados.getIdEstado()) {
                fila.setVariable("{id_estado}", String.valueOf(estados.getIdEstado()));
                fila.setVariable("{estado}", estados.getNombre());
                if (estados.getIdEstado() == caso.getEstado().getIdEstado()) {
                    fila.setVariable("{sel_est}", "selected");
                } else {
                    fila.setVariable("{sel_est}", "");
                }
                edos.add(fila);
            }
        }
               
        // ---- Jornadas ----
        List listJornadas = bizDelegate.getJornadasByEstado("1");
        ArrayList jornadas = new ArrayList();
        for (int i = 0; i < listJornadas.size(); i++) {
            IValueSet fila = new ValueSet();
            JornadaDTO jornada = (JornadaDTO) listJornadas.get(i);
            fila.setVariable("{id_jornada}", String.valueOf(jornada.getIdJornada()));
            fila.setVariable("{jornada}", jornada.getDescripcion());

            if (jornada.getIdJornada() == caso.getJornada().getIdJornada()) {
                fila.setVariable("{sel_jorn}", "selected");
            } else {
                fila.setVariable("{sel_jorn}", "");
            }
            jornadas.add(fila);
        }

        // ---- Quiebres ----
        List listQuiebres = bizDelegate.getQuiebresByEstado("1");
        ArrayList quiebres = new ArrayList();
        for (int i = 0; i < listQuiebres.size(); i++) {
            IValueSet fila = new ValueSet();
            QuiebreCasoDTO quiebre = (QuiebreCasoDTO) listQuiebres.get(i);
            fila.setVariable("{id_quiebre}", String.valueOf(quiebre.getIdQuiebre()));
            fila.setVariable("{quiebre}", quiebre.getNombre());
            quiebres.add(fila);
        }
        
        // ---- Responsables ----
        List listResponsables = bizDelegate.getResponsablesByEstado("1");
        ArrayList responsables = new ArrayList();
        for (int i = 0; i < listResponsables.size(); i++) {
            IValueSet fila = new ValueSet();
            ObjetoDTO responsable = (ObjetoDTO) listResponsables.get(i);
            fila.setVariable("{id_responsable}", String.valueOf(responsable.getIdObjeto()));
            fila.setVariable("{responsable}", responsable.getNombre());
            responsables.add(fila);
        }

        // ---- Motivos ----
        List listMotivos = bizDelegate.getMotivosByEstado("1");
        ArrayList motivos = new ArrayList();
        for (int i = 0; i < listMotivos.size(); i++) {
            IValueSet fila = new ValueSet();
            ObjetoDTO motivo = (ObjetoDTO) listMotivos.get(i);
            fila.setVariable("{id_motivo}", String.valueOf(motivo.getIdObjeto()));
            fila.setVariable("{motivo}", motivo.getNombre());
            motivos.add(fila);
        }

		// ---- Productos a enviar ----
		List prodEnviar = bizDelegate.getProductosByCasoAndTipo(idCaso,"E");
		ArrayList pEnviar = new ArrayList();
		
		for (int i = 0; i < prodEnviar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodEnviar.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{producto_desc_log}",prod.getPpDescripcion().replaceAll("'",""));
            
			fila.setVariable("{cantidad}",prod.getPpCantidad());			
			fila.setVariable("{unidad}",prod.getPpUnidad());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{ps_producto}",prod.getPsDescripcion());
			fila.setVariable("{ps_cantidad}",prod.getPsCantidad());			
			fila.setVariable("{ps_unidad}",prod.getPsUnidad());
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
            fila.setVariable("{pickeador}",prod.getPickeador());
			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			
			pEnviar.add(fila);
		}
		if (pEnviar.size() > 0) {
		    top.setVariable("{msj_prods_enviar}","");
		} else {
		    top.setVariable("{msj_prods_enviar}","No existen productos para enviar");
		}
		
		// ---- Productos a Retirar ----
		List prodRetirar = bizDelegate.getProductosByCasoAndTipo(idCaso,"R");
		ArrayList pRetirar = new ArrayList();
		
		for (int i = 0; i < prodRetirar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodRetirar.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{producto_desc_log}",prod.getPpDescripcion().replaceAll("'",""));
            
			fila.setVariable("{cantidad}",prod.getPpCantidad());			
			fila.setVariable("{unidad}",prod.getPpUnidad());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{ps_producto}",prod.getPsDescripcion());
			fila.setVariable("{ps_cantidad}",prod.getPsCantidad());			
			fila.setVariable("{ps_unidad}",prod.getPsUnidad());
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
            fila.setVariable("{pickeador}",prod.getPickeador());
			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			
			pRetirar.add(fila);
		}
		if (pRetirar.size() > 0) {
		    top.setVariable("{msj_prods_retirar}","");
		} else {
		    top.setVariable("{msj_prods_retirar}","No existen productos para retirar");
		}
        

        // ---- Productos a Retirar ----
        List prodEnvioDinero = bizDelegate.getProductosByCasoAndTipo(idCaso,"P");
        ArrayList pEnvioDinero = new ArrayList();
        
        for (int i = 0; i < prodEnvioDinero.size(); i++) {          
            IValueSet fila = new ValueSet();
            ProductoCasoDTO prod = (ProductoCasoDTO)prodEnvioDinero.get(i);
            fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
            
            fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{producto_desc_log}",prod.getPpDescripcion().replaceAll("'",""));
            
            fila.setVariable("{cantidad}",prod.getPpCantidad());            
            fila.setVariable("{unidad}",prod.getPpUnidad());
            fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
            
            fila.setVariable("{ps_producto}",prod.getPsDescripcion());
            fila.setVariable("{ps_cantidad}",prod.getPsCantidad());         
            fila.setVariable("{ps_unidad}",prod.getPsUnidad());
            fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
            
            fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
            fila.setVariable("{responsable}",prod.getResponsable().getNombre());
            fila.setVariable("{pickeador}",prod.getPickeador());
            
            fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
            
            pEnvioDinero.add(fila);
        }
        if (pEnvioDinero.size() > 0) {
            top.setVariable("{msj_envio_dinero}","");
        } else {
            top.setVariable("{msj_envio_dinero}","No existen envios de dinero");
        }
		
		// ---- Productos a Especiales (Documentos) ----
		List prodDocumentos = bizDelegate.getProductosByCasoAndTipo(idCaso,"D");
		ArrayList pDocumentos = new ArrayList();
		
		for (int i = 0; i < prodDocumentos.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodDocumentos.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{producto_desc_log}",prod.getPpDescripcion().replaceAll("'",""));
            
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());			
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			pDocumentos.add(fila);
		}
		if (pDocumentos.size() > 0) {
		    top.setVariable("{msj_prods_docs}","");
		} else {
		    top.setVariable("{msj_prods_docs}","No existen documentos");
		}

        top.setVariable("{mensaje1}", "");
        top.setVariable("{mensaje2}", mensajeSistema);
        top.setVariable("{mns}", "");
        top.setVariable("{id_caso}", String.valueOf(caso.getIdCaso()));
        top.setVariable("{id_pedido}", String.valueOf(caso.getIdPedido()));
        top.setVariable("{estado}", caso.getEstado().getNombre());
        top.setVariable("{estado_caso}", String.valueOf(caso.getEstado().getIdEstado()));
        top.setVariable("{direccion}", caso.getDireccion());
        top.setVariable("{comuna}", caso.getComuna());
        top.setVariable("{fecha_pedido}", Formatos.frmFecha(caso.getFcPedido()));
        top.setVariable("{fecha_despacho}", Formatos.frmFecha(caso.getFcDespacho()));
        top.setVariable("{fecha_ingreso_caso}", CasosUtil.frmFechaHoraSinSegundos(caso.getFcCreacionCaso()));
        top.setVariable("{cli_rut}", String.valueOf(caso.getCliRut()));
        top.setVariable("{cli_dv}", caso.getCliDv());
        top.setVariable("{cli_nombre}", caso.getCliNombre());
        top.setVariable("{cli_cod1}", caso.getCliFonoCod1());
        top.setVariable("{cli_fono1}", caso.getCliFonoNum1());
        top.setVariable("{cli_cod2}", caso.getCliFonoCod2());
        top.setVariable("{cli_fono2}", caso.getCliFonoNum2());
        top.setVariable("{cli_cod3}", caso.getCliFonoCod3());
        top.setVariable("{cli_fono3}", caso.getCliFonoNum3());
        top.setVariable("{local}", caso.getLocal().getNombre());
        top.setVariable("{nro_compras}", String.valueOf(bizDelegate.getNroComprasByCliente(caso.getCliRut())));
        top.setVariable("{nro_casos}", String.valueOf(bizDelegate.getNroCasosByCliente(caso.getCliRut())));
        top.setVariable("{teleoperador}", caso.getDescripcionCanalCompra());
        top.setVariable("{fecha_resolucion}", Formatos.frmFecha(caso.getFcCompromisoSolucion()));
        
        top.setVariable("{fecha_entrega}", Formatos.frmFecha(caso.getFcHoraEntrega()));
        top.setVariable("{hora_entrega}", Formatos.frmHora(caso.getFcHoraEntrega()));
        
        top.setVariable("{fecha_resolucion_minima}", CasosUtil.fechaCreacionMasDiaResolucion(caso.getFcCreacionCaso(),CasosConstants.DIAS_RESOLUCION_CASO));
        top.setVariable("{jornada_resolucion}", caso.getJornada().getDescripcion());
        if (caso.getEditUsuario() != null) {
	        top.setVariable("{usuario_edit}", caso.getEditUsuario());
	    } else {
	        top.setVariable("{usuario_edit}", " ");
	    }
        if (caso.getCreadorUsuario() != null) {
	        top.setVariable("{usuario_creador}", caso.getCreadorUsuario());
	    } else {
	        top.setVariable("{usuario_creador}", " ");
	    }
        top.setVariable("{id_perfil}", String.valueOf(usr.getId_perfil()));
        top.setVariable("{indicaciones}",caso.getIndicaciones());
        
        if ( caso.getEstado().getIdEstado() == CasosEstadosUtil.PRE_INGRESADO ) {
            top.setVariable("{disp_link_call_center}","none");
            top.setVariable("{disp_call_center}","true");
        } else {
            top.setVariable("{disp_link_call_center}","true");
            top.setVariable("{disp_call_center}","none");
        }
        if ( caso.getComentarioCallCenter() != null && caso.getComentarioCallCenter().length() > 0 ) {
            top.setVariable("{msj_call_center}",caso.getComentarioCallCenter().replaceAll("\r\n","<br/>"));
        } else {
            top.setVariable("{msj_call_center}","");
        }
        
        top.setVariable("{satisfaccion}",caso.getSatisfaccionCliente());
        
        top.setVariable("{fecha_actual}", CasosUtil.fechaActualByPatron("dd/MM/yyyy"));
        top.setVariable("{hora_actual}", CasosUtil.fechaActualByPatron("HH:mm"));
        
        if (caso.isEscalamiento()) {
	        top.setVariable("{color_escalamiento}","#FF0000");
	        top.setVariable("{escalamiento}","Si");
	        top.setVariable("{cod_escalamiento}","S");
	        
	        if (CasosUtil.esSupervisor(usr, idSupervisor)) {
	            top.setVariable("{disabled_escalamiento}","");
	        } else {
	            top.setVariable("{disabled_escalamiento}","disabled");
	        }
	        
        } else {
            top.setVariable("{color_escalamiento}","#336699");
	        top.setVariable("{escalamiento}","No");
	        top.setVariable("{cod_escalamiento}","N");
	        top.setVariable("{disabled_escalamiento}","");
        }        
        
        if (aLiberar) {
            top.setVariable("{a_liberar}","S");
        } else {
            top.setVariable("{a_liberar}","N");
        }
        top.setVariable("{pagina}", String.valueOf(pagina));

        top.setDynamicValueSets("ESTADOS_CASOS", edos);
        top.setDynamicValueSets("JORNADAS_CASOS", jornadas);
        top.setDynamicValueSets("QUIEBRES_CASOS", quiebres);
        top.setDynamicValueSets("RESPONSABLES_CASOS", responsables);
        top.setDynamicValueSets("MOTIVOS_CASOS", motivos);
        
        top.setDynamicValueSets("PRDS_ENVIAR", pEnviar);
	    top.setDynamicValueSets("PRDS_RETIRAR", pRetirar);
	    top.setDynamicValueSets("PRDS_DOCS", pDocumentos);
        top.setDynamicValueSets("ENVIO_DINERO", pEnvioDinero);

	    top.setVariable("{prodRetirar}", String.valueOf(pRetirar.size()));
	    top.setVariable("{prodEnviar}", String.valueOf(pEnviar.size()));
	    top.setVariable("{prodDocs}", String.valueOf(pDocumentos.size()));
        top.setVariable("{prodEnvioDinero}", String.valueOf(pEnvioDinero.size()));
        	    
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString());

        logger.debug("Fin ViewModCasoForm Execute");
        String result = tem.toString(top);
        salida.setHtmlOut(result);
        salida.Output();
    }
}
