package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.JornadaDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar un datos para la configuracion de casos
 * Puede ser Tipo de Quiebre, Responsables o Jornadas (Ajax)
 * 
 * @author imoyano
 */

public class AddModConfigCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModConfigCaso [AJAX]");
        
        String mensajeSistema	= "";
        long idObjeto 			= 0;
    	String tipo	 			= "";
    	String tipoDescripcion	= "";
    	String descripcion 		= "";
    	long puntaje	 		= 0;
    	boolean esNuevo			= true;
    	String estActivado		= "";
    	QuiebreCasoDTO quiebreOrigen = new QuiebreCasoDTO();
    	ObjetoDTO responsableOrigen  = new ObjetoDTO();
        ObjetoDTO motivoOrigen  = new ObjetoDTO();
    	JornadaDTO jornadaOrigen 	 = new JornadaDTO();
    	BizDelegate bizDelegate 	 = new BizDelegate();

        try {
            
            if (req.getParameter("tipo") != null) {
                tipo = req.getParameter("tipo").toString();
            }
            if (req.getParameter("id_objeto") != null) {
                idObjeto = Long.parseLong(req.getParameter("id_objeto").toString());
            }
            if (req.getParameter("desc_objeto") != null) {
                descripcion = req.getParameter("desc_objeto").toString();
            }
            if (req.getParameter("activado") != null) {
                estActivado = req.getParameter("activado").toString();
            }
            if (tipo.equalsIgnoreCase("Q")) {
	            if (req.getParameter("puntaje") != null) {
	                puntaje = Long.parseLong(req.getParameter("puntaje").toString());
	            }
            }

            if (idObjeto == 0) {
                esNuevo = true;
                logger.debug("Objeto nuevo de tipo '" + tipo + "' para ser guardado.");
                if (tipo.equalsIgnoreCase("Q")) {
                    logger.debug("Agregaremos un nuevo tipo de quiebre");
                    QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
                    quiebre.setNombre(descripcion);
                    quiebre.setPuntaje(puntaje);
                    quiebre.setActivado(estActivado);
                    idObjeto = bizDelegate.addQuiebre(quiebre);
                    mensajeSistema = CasosConstants.MSJ_ADD_QUIEBRE_EXITO;
                    tipoDescripcion = "Tipo de Quiebre";
                    
                } else if (tipo.equalsIgnoreCase("R")) {
                    logger.debug("Agregaremos un nuevo responsable");
                    ObjetoDTO responsable = new ObjetoDTO();
                    responsable.setNombre(descripcion);
                    responsable.setActivado(estActivado);
                    idObjeto = bizDelegate.addResponsable(responsable);
                    mensajeSistema = CasosConstants.MSJ_ADD_RESPONSABLE_EXITO;
                    tipoDescripcion = "Responsable";
                    
                } else if (tipo.equalsIgnoreCase("M")) {
                    logger.debug("Agregaremos un nuevo motivo");
                    ObjetoDTO motivo = new ObjetoDTO();
                    motivo.setNombre(descripcion);
                    motivo.setActivado(estActivado);
                    idObjeto = bizDelegate.addMotivo(motivo);
                    mensajeSistema = CasosConstants.MSJ_ADD_MOTIVO_EXITO;
                    tipoDescripcion = "Motivo";
                    
                } else if (tipo.equalsIgnoreCase("J")) {
                    logger.debug("Agregaremos una nueva jornada");
                    JornadaDTO jornada = new JornadaDTO();
                    jornada.setDescripcion(descripcion);
                    jornada.setActivado(estActivado);
                    idObjeto = bizDelegate.addJornada(jornada);
                    mensajeSistema = CasosConstants.MSJ_ADD_JORNADA_EXITO;                    
                    tipoDescripcion = "Jornada";
                    
                }
                
                
            } else {
                esNuevo = false;
                logger.debug("Objeto de tipo '" + tipo + "' para ser modificado.");
                
                if (tipo.equalsIgnoreCase("Q")) {
                    logger.debug("A modificar un tipo de quiebre");
                    QuiebreCasoDTO quiebre = new QuiebreCasoDTO();
                    quiebre.setIdQuiebre(idObjeto);
                    quiebre.setNombre(descripcion);
                    quiebre.setPuntaje(puntaje);
                    quiebre.setActivado(estActivado);
                    bizDelegate.modQuiebre(quiebre);
                    mensajeSistema = CasosConstants.MSJ_ADD_QUIEBRE_EXITO;
                    tipoDescripcion = "Tipo de Quiebre";
                    
                } else if (tipo.equalsIgnoreCase("R")) {
                    logger.debug("A modificar un responsable");
                    ObjetoDTO responsable = new ObjetoDTO();
                    responsable.setIdObjeto(idObjeto);
                    responsable.setNombre(descripcion);
                    responsable.setActivado(estActivado);
                    bizDelegate.modResponsable(responsable);
                    mensajeSistema = CasosConstants.MSJ_ADD_RESPONSABLE_EXITO;
                    tipoDescripcion = "Responsable";
                    
                } else if (tipo.equalsIgnoreCase("M")) {
                    logger.debug("A modificar un motivo");
                    ObjetoDTO motivo = new ObjetoDTO();
                    motivo.setIdObjeto(idObjeto);
                    motivo.setNombre(descripcion);
                    motivo.setActivado(estActivado);
                    bizDelegate.modMotivo(motivo);
                    mensajeSistema = CasosConstants.MSJ_ADD_MOTIVO_EXITO;
                    tipoDescripcion = "Motivo";
                    
                } else if (tipo.equalsIgnoreCase("J")) {
                    logger.debug("A modificar una jornada");
                    JornadaDTO jornada = new JornadaDTO();
                    jornada.setIdJornada(idObjeto);
                    jornada.setDescripcion(descripcion);
                    jornada.setActivado(estActivado);
                    bizDelegate.modJornada(jornada);
                    mensajeSistema = CasosConstants.MSJ_ADD_JORNADA_EXITO;
                    tipoDescripcion = "Jornada";
                    
                }                
            }
            
        } catch (BocException e) {
            e.printStackTrace();

            if (tipo.equalsIgnoreCase("Q")) {
                if (esNuevo) {
                    mensajeSistema = CasosConstants.MSJ_ADD_QUIEBRE_ERROR;
                } else {
                    //En caso de estar modificando y se cae el servicio, se rescatan los datos originales y se muestran en pantalla
                    quiebreOrigen = bizDelegate.getQuiebreById(idObjeto);
                    descripcion   = quiebreOrigen.getNombre();
                    puntaje		  = quiebreOrigen.getPuntaje();
                    estActivado	  = quiebreOrigen.getActivado();
                }
                
            } else if (tipo.equalsIgnoreCase("R")) {
                if (esNuevo) {
                    mensajeSistema = CasosConstants.MSJ_ADD_RESPONSABLE_ERROR;
                } else {
                    //En caso de estar modificando y se cae el servicio, se rescatan los datos originales y se muestran en pantalla
                    responsableOrigen = bizDelegate.getResponsableById(idObjeto);
                    descripcion   = responsableOrigen.getNombre();
                    estActivado	  = responsableOrigen.getActivado();
                }
                
            } else if (tipo.equalsIgnoreCase("M")) {
                if (esNuevo) {
                    mensajeSistema = CasosConstants.MSJ_ADD_MOTIVO_ERROR;
                } else {
                    //En caso de estar modificando y se cae el servicio, se rescatan los datos originales y se muestran en pantalla
                    motivoOrigen = bizDelegate.getMotivoById(idObjeto);
                    descripcion   = motivoOrigen.getNombre();
                    estActivado   = motivoOrigen.getActivado();
                }
                
            } else if (tipo.equalsIgnoreCase("J")) {
                if (esNuevo) {
                    mensajeSistema = CasosConstants.MSJ_ADD_JORNADA_ERROR;
                } else {
                    //En caso de estar modificando y se cae el servicio, se rescatan los datos originales y se muestran en pantalla
                    jornadaOrigen = bizDelegate.getJornadaCasoById(idObjeto);
                    descripcion   = jornadaOrigen.getDescripcion();
                    estActivado	  = jornadaOrigen.getActivado();
                }
            }            
        }   
            
        //RESPUESTA            
        if (esNuevo) {            
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("<datos_objeto>");
            res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
            res.getWriter().write("<id_objeto_new>" + idObjeto + "</id_objeto_new>");
            res.getWriter().write("</datos_objeto>");            
            
        } else {
            res.setContentType("text/html");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("<td align=\"left\">"+idObjeto+"</td>");
            res.getWriter().write("<td align=\"left\">"+descripcion+"</td>");
            if (tipo.equalsIgnoreCase("Q")) {
                res.getWriter().write("<td align=\"left\">"+puntaje+"</td>");
            }
            if (estActivado.equals("1")) {
                res.getWriter().write("<td align=\"center\">Si&nbsp;</td>");
            } else {
                res.getWriter().write("<td align=\"center\">No&nbsp;</td>");
            }
            res.getWriter().write("<td align=\"center\" nowrap><a href=\"javascript:ventanaObjeto('Modificar "+tipoDescripcion+"','"+descripcion+"','"+puntaje+"','"+idObjeto+"','"+tipo+"','"+estActivado+"');\"><img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar "+tipoDescripcion+"\"></a>&nbsp;");
            res.getWriter().write("<a href=\"javascript:aEliminar('"+idObjeto+"','"+tipo+"');\"><img src=\"img/trash.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Eliminar "+tipoDescripcion+"\"></a></td>");
            
        }

        logger.debug("Fin AddModConfigCaso [AJAX]");
    }
}
