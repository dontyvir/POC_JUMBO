package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.EstadoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosEstadosUtil;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado para el monitor de casos
 * @author imoyano
 */
public class ViewMonitorCasos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMonitorCasos Execute");
	    
	    // Variables
	    String filtroAlarma = "T";
	    String filtroEstado = "T";
	    String nroPedido 	= "";
	    String cliApellido 	= "";
	    String cliRut 		= "";
	    String campoCliente = "";
	    String nroCaso 		= "";
	    String cliDatosSel 	= "";
	    String mensaje 		= "";
	    int pag 			= 1;
		int regsperpage 	= 10;
		long casoEnEdicion 	= 0;
		String fcBusquedaSel= "";
		String fcIniCreacion= "";
	    String fcFinCreacion= "";
	    String fcIniCompromiso = "";
	    String fcFinCompromiso = "";
		
		String accionVer = "<img src=\"img/info.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Ver detalle del caso\">";
		String accionSeparador = "&nbsp;";
		String accionEditar = "<img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar el caso\">";
		String accionEscalarOn = "<img src=\"img/star_on.gif\" border=\"0\" height=\"15\" width=\"15\" title=\"Caso en Escalamiento\">";
		String accionEscalarOff = "<img src=\"img/star_off.gif\" border=\"0\" height=\"15\" width=\"15\" title=\"Caso sin Escalamiento\">";
	    
		//Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//Sacamos la info de la pagina
		if (req.getParameter("msje") != null) {
		    mensaje = req.getParameter("msje").toString();
	    }
	    if (req.getParameter("sel_alarma") != null) {
	        filtroAlarma = req.getParameter("sel_alarma").toString();
	    }
	    if (req.getParameter("sel_est") != null) {
	        filtroEstado = req.getParameter("sel_est").toString();
	    }
	    if (req.getParameter("nro_pedido") != null) {
	        nroPedido = req.getParameter("nro_pedido").toString();
	    }
	    top.setVariable("{check_1}","checked");
		top.setVariable("{check_2}","");
	    if (req.getParameter("cli_datos_sel") != null && req.getParameter("cli_datos") != null) {
	        cliDatosSel = req.getParameter("cli_datos_sel").toString();
	        if (req.getParameter("cli_datos_sel").toString().equalsIgnoreCase("APE")) {
	            cliApellido = req.getParameter("cli_datos").toString();
	            campoCliente = cliApellido;
	            top.setVariable("{check_1}","");
	            top.setVariable("{check_2}","checked");	            
	        } else {
	            cliRut = req.getParameter("cli_datos").toString();
	            campoCliente = cliRut;
	            top.setVariable("{check_1}","checked");
	            top.setVariable("{check_2}","");	            
	        }	        
	    }
	    
	    top.setVariable("{check_bus_1}","checked");
		top.setVariable("{check_bus_2}","");
	    if (req.getParameter("bus_fecha_sel") != null) {
	        fcBusquedaSel = req.getParameter("bus_fecha_sel").toString();
	        if (fcBusquedaSel.length() > 0) {
		        if (req.getParameter("bus_fecha_sel").toString().equalsIgnoreCase("CREA")) {
		            if (req.getParameter("fc_ini") != null) {
		                fcIniCreacion = req.getParameter("fc_ini").toString();
		            }
		            if (req.getParameter("fc_fin") != null) {
		                fcFinCreacion = req.getParameter("fc_fin").toString();
		            }
		            top.setVariable("{check_bus_1}","checked");
		            top.setVariable("{check_bus_2}","");	            
		        } else {
		            if (req.getParameter("fc_ini") != null) {
		                fcIniCompromiso = req.getParameter("fc_ini").toString();
		            }
		            if (req.getParameter("fc_fin") != null) {
		                fcFinCompromiso = req.getParameter("fc_fin").toString();
		            }
		            top.setVariable("{check_bus_1}","");
	            top.setVariable("{check_bus_2}","checked");	            
	        }
	        }	        
	    }
	    
	    if (req.getParameter("nro_caso") != null) {
	        nroCaso = req.getParameter("nro_caso").toString();
	    }
	    
	    //Paginacion
	    if (req.getParameter("pagina") != null) {
			pag = Integer.parseInt(req.getParameter("pagina"));
			logger.debug("pagina: " + req.getParameter("pagina"));
		} else {
			pag = 1; // por defecto mostramos la página 1
		}
	    regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
	    
	    //Verificamos si el usuario esta editando un caso
	    BizDelegate bizDelegate = new BizDelegate();
	    casoEnEdicion = bizDelegate.getCasoEnEdicionByUsuario(usr);
	    
	    // Rescatamos la informacion que necesitamos
	    CasosCriterioDTO criterio = new CasosCriterioDTO(filtroEstado,filtroAlarma,String.valueOf(usr.getId_local()),nroPedido,cliRut,cliApellido,nroCaso,pag,regsperpage,"BOL",fcIniCreacion,fcFinCreacion,fcIniCompromiso,fcFinCompromiso);
		logger.debug("vamos a buscar los casos");		
		
		// ---- casos ----
		List listCasos = bizDelegate.getCasosByCriterio(criterio);
		ArrayList datos = new ArrayList();
		for (int i = 0; i < listCasos.size(); i++) {			
			IValueSet fila = new ValueSet();
			CasoDTO caso = (CasoDTO)listCasos.get(i);
			fila.setVariable("{id_pedido}" , String.valueOf(caso.getIdPedido()));
			fila.setVariable("{id_caso}" , String.valueOf(caso.getIdCaso()));
			fila.setVariable("{estado_caso}" , String.valueOf(caso.getEstado().getNombre()));
			fila.setVariable("{cli_rut}" , String.valueOf(caso.getCliRut()));
			fila.setVariable("{cli_dv}" , String.valueOf(caso.getCliDv()));
			fila.setVariable("{cliente}" , String.valueOf(caso.getCliNombre()));
			fila.setVariable("{local}" , caso.getLocal().getNombre());
			fila.setVariable("{fc_creacion}" , Formatos.frmFecha(caso.getFcCreacionCaso()) );
			fila.setVariable("{fc_resolucion}" , Formatos.frmFecha(caso.getFcCompromisoSolucion()) );
			fila.setVariable("{jornada}" , caso.getJornada().getDescripcion());
			if (caso.getAlarma().equalsIgnoreCase("V")) {
			    fila.setVariable("{color_fuente}" , CasosConstants.ESTADO_VENCIDO_COLOR);
			} else if (caso.getAlarma().equalsIgnoreCase("P")) {
			    fila.setVariable("{color_fuente}" , CasosConstants.ESTADO_POR_VENCER_COLOR);
			} else if (caso.getAlarma().equalsIgnoreCase("F")) {
			    fila.setVariable("{color_fuente}" , CasosConstants.ESTADO_FINALIZADO_COLOR);
			} else {
			    fila.setVariable("{color_fuente}" , CasosConstants.ESTADO_NORMAL_COLOR);
			}			
			fila.setVariable("{accion1}" , "");
			fila.setVariable("{palito}" , accionSeparador);
			fila.setVariable("{accion2}" , "");
			fila.setVariable("{direccion1}" , "");
			fila.setVariable("{direccion2}" , "");
			
			if (caso.isEscalamiento()) {
			    fila.setVariable("{accion3}" , accionEscalarOn);
			} else {
			    fila.setVariable("{accion3}" , accionEscalarOff);
			}
			
			if (caso.getEditUsuario() != null && caso.getEditUsuario().length() > 0) {
			    fila.setVariable("{direccion1}" , "ViewDetCaso?id_caso="+caso.getIdCaso());				
			    if (caso.getEditUsuario().equalsIgnoreCase(String.valueOf(usr.getId_usuario()))) {			        
			        fila.setVariable("{accion1}" , accionVer);
					fila.setVariable("{accion2}" , accionEditar);
					
					if (caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO || caso.getEstado().getIdEstado() == CasosEstadosUtil.ANULADO) {
					    fila.setVariable("{direccion2}" , "javascript:alert('"+CasosConstants.MSJ_MOD_CASO_FINALIZADO+"');");
					} else {
					    fila.setVariable("{direccion2}" , "ViewModificarCasoForm?id_caso="+caso.getIdCaso());
					}
					
			    } else {
			        fila.setVariable("{accion1}" , accionVer);					    
			    }
			    
			} else { 
			    fila.setVariable("{direccion1}" , "ViewDetCaso?id_caso="+caso.getIdCaso());
			    if (casoEnEdicion != 0) {
			        fila.setVariable("{accion1}" , accionVer);					
			    } else {
				    fila.setVariable("{accion1}" , accionVer);
					fila.setVariable("{accion2}" , accionEditar);

					if(caso.getEstado().getIdEstado() == CasosEstadosUtil.VERIFICADO || caso.getEstado().getIdEstado() == CasosEstadosUtil.ANULADO) {
					    fila.setVariable("{direccion2}" , "javascript:alert('"+CasosConstants.MSJ_MOD_CASO_FINALIZADO+"');");
					} else {
					    fila.setVariable("{direccion2}" , "ViewModificarCasoForm?id_caso="+caso.getIdCaso());
					}

			    }
			}			
			datos.add(fila);			
		}
		
		if (listCasos.size() < 1 ) {
			top.setVariable("{mje1}","La consulta no arrojó resultados");
			top.setVariable("{dis}","disabled");
		} else {
			top.setVariable("{mje1}","");
			top.setVariable("{dis}","");
		}		
		
		// ---- estados de casos ----
		List listestados = bizDelegate.getEstadosDeCasos();
		ArrayList edos = new ArrayList();
		
		for (int i = 0; i < listestados.size(); i++) {			
			IValueSet fila = new ValueSet();
			EstadoCasoDTO estados = (EstadoCasoDTO)listestados.get(i);
            if ( estados.getIdEstado() != CasosEstadosUtil.PRE_INGRESADO ) {
    			fila.setVariable("{id_estado}",String.valueOf(estados.getIdEstado()));
    			fila.setVariable("{estado}",estados.getNombre());
    			if (filtroEstado.equalsIgnoreCase(String.valueOf(estados.getIdEstado()))) {
    				fila.setVariable("{sel_est}", "selected");
    			} else {
    				fila.setVariable("{sel_est}", "");
    			}
    			edos.add(fila);
            }
		}
		
		// ---- Alarma ----
		ArrayList alarms = CasosUtil.generaComboAlarma(filtroAlarma);		
		
		// Paginador para la pagina
		ArrayList pags = new ArrayList();
		double totalRegistros = bizDelegate.getCountCasosByCriterio(criterio);
		
		logger.debug("tot_reg: " + totalRegistros + "");
		double total_pag = (double) Math.ceil( totalRegistros / regsperpage );
		logger.debug ("round: " + total_pag);
		if (total_pag == 0) {
			total_pag = 1;
		}
		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag) {
				fila.setVariable("{sel}", "selected");
			} else {
				fila.setVariable("{sel}", "");
			}
			pags.add(fila);
		}	
		//anterior y siguiente
		if( pag > 1) {
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    } else if ( pag == 1) {
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag < total_pag) {
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    } else {
	    	top.setVariable("{siguiente_label}","");
	    }		
		
		//Informacion para la pagina
	    top.setDynamicValueSets("PAGINAS", pags);
		top.setDynamicValueSets("MON_CASOS", datos);
		top.setDynamicValueSets("ESTADOS_CASOS", edos);
		top.setDynamicValueSets("ALARMAS_CASOS", alarms);
		
		top.setVariable("{nro_pedido}", nroPedido);
		top.setVariable("{campo_cliente}", campoCliente);
		top.setVariable("{nro_caso}", nroCaso);
		top.setVariable("{cli_datos_sel}", cliDatosSel);
		top.setVariable("{bus_fecha_sel}", fcBusquedaSel);
		top.setVariable("{sel_est}", filtroEstado);
		top.setVariable("{sel_alarma}", filtroAlarma);
		
		if (fcIniCreacion.length() > 0) {
		    top.setVariable("{fc_ini}", fcIniCreacion);
		} else {
		    top.setVariable("{fc_ini}", fcIniCompromiso);
		}
		if (fcFinCreacion.length() > 0) {
		    top.setVariable("{fc_fin}", fcFinCreacion);
		} else {
		    top.setVariable("{fc_fin}", fcFinCompromiso);
		}
		
		top.setVariable("{mensaje_error}","");
		top.setVariable("{mensaje}", mensaje);	
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		if (casoEnEdicion != 0) {
			logger.debug("caso en edicion: " + casoEnEdicion);
			String var_usrped = " Ud. está editando el Caso: " +
								" <a href='ViewModificarCasoForm?id_caso="+casoEnEdicion+"&mod=0'> "+casoEnEdicion+"</a> " +
								//" (<a href ='LibCaso?id_caso="+casoEnEdicion+"&url=ViewMonitorCasos'> Liberar Caso </a> ) ";
								" (<a href ='ViewModificarCasoForm?id_caso="+casoEnEdicion+"&mod=1'> Liberar Caso </a> ) ";
			top.setVariable("{usr_caso_tomado}", var_usrped);			
		} else {
		    top.setVariable("{usr_caso_tomado}", "");		    
		}
		
		logger.debug("Fin ViewMonitorCasos Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
