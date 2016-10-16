package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de despacho, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author imoyano
 */

public class ViewMonitorDespacho extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
        long idZona   = -1;
		long idPedido = -1;
        String fecha  = "";
        String tipoPedido = ""; //origen
        String reprogramada = "";
        String horasJor = ""; 
        
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String key = rb.getString("GoogleMaps.key");

		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		if (req.getParameter("id_zona") != null) {
			idZona = Long.parseLong(req.getParameter("id_zona"));			
		}
        if (req.getParameter("horas") != null) {
            horasJor = req.getParameter("horas");           
        }
        if (req.getParameter("id_pedido") != null) {
            idPedido = Long.parseLong(req.getParameter("id_pedido"));           
        }
		if (req.getParameter("fecha") != null) {
			fecha = req.getParameter("fecha");
		}
        if (req.getParameter("tipo") != null) {
            tipoPedido = req.getParameter("tipo");
        }
        if (req.getParameter("reprogramada") != null) {
            reprogramada = req.getParameter("reprogramada");
        }
		
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		BizDelegate biz = new BizDelegate();
		// creamos los criterios
		DespachoCriteriaDTO criterio = new DespachoCriteriaDTO();
		
		if ( idZona > 0 )
			criterio.setId_zona(idZona);
        if ( idPedido > 0 ) {
            criterio.setId_pedido(idPedido);
            PedidoDTO ped = biz.getPedido(idPedido);
            fecha = ped.getFdespacho();
        }
		if ( !fecha.equals("") )
			criterio.setF_despacho(fecha);
		if ( !tipoPedido.equals("") )
            criterio.setOrigen(tipoPedido);
        criterio.setReprogramada(reprogramada);
        
        if (!horasJor.equals("")) {
            int pos = horasJor.indexOf("-");
            criterio.setH_inicio(horasJor.substring(0, pos));
            criterio.setH_fin(horasJor.substring(pos + 1));
        }
        
		criterio.setFiltro_estados( Constantes.ESTADOS_PEDIDO_DESPACHO ); 
		criterio.setId_local(usr.getId_local());
		
		List ordenarpor = new ArrayList();
		ordenarpor.add(DespachoCriteriaDTO.ORDEN_ZONA_ORD+" "+DespachoCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(DespachoCriteriaDTO.ORDEN_JORNADA+" "+DespachoCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(DespachoCriteriaDTO.ORDEN_COMUNA+" "+DespachoCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(DespachoCriteriaDTO.ORDEN_ESTADO+" "+DespachoCriteriaDTO.ORDEN_ASCENDENTE);
		criterio.setOrden_columnas(ordenarpor);		
		
		LocalDTO local = biz.getLocalByID(usr.getId_local());
		top.setVariable("{latitud}", local.getLatitud()+"");
		top.setVariable("{longitud}", local.getLongitud()+"");
		top.setVariable("{nom_local}", usr.getLocal());
		top.setVariable("{key}", key);
		
		List listadespachos = biz.getDespachosParaMonitorByCriteria(criterio);
		String zona_anterior="";
		ArrayList datos = new ArrayList();
		for (int i = 0; i < listadespachos.size(); i++) {
			MonitorDespachosDTO desp1 = (MonitorDespachosDTO) listadespachos.get(i);
			if ( !desp1.getEstado().equals("Anulado") || desp1.getId_estado()== 20 ) {
                IValueSet fila = new ValueSet();
                
                if (desp1.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
                    fila.setVariable("{id_pedido}", "C" + desp1.getNroGuiaCaso() + "<br/>(" + desp1.getId_pedido() + ")");
                } else if (desp1.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
                    fila.setVariable("{id_pedido}", "JV" + desp1.getNroGuiaCaso() + "<br/>(" + desp1.getId_pedido() + ")");
                } else {
                    fila.setVariable("{id_pedido}", String.valueOf(desp1.getId_pedido()));
                }
                fila.setVariable("{id_pedido_real}", String.valueOf(desp1.getId_pedido()));
                fila.setVariable("{jdespacho}" , Formatos.frmHoraSola(desp1.getH_ini()) + "-" + Formatos.frmHoraSola(desp1.getH_fin()) + "   " + Formatos.frmFecha(desp1.getF_despacho()));
                fila.setVariable("{fecha}", Formatos.frmFecha(desp1.getF_despacho()));
    			fila.setVariable("{cant_bin}", String.valueOf(desp1.getCant_bins()));
    			fila.setVariable("{tipo_despacho}", Formatos.getDescripcionOrigenPedido(desp1.getOrigen()));
			
    			fila.setVariable("{zona_desp}", desp1.getZona_despacho());
			    
    			if (desp1.isConfirmada()){
    			    fila.setVariable("{color_texto_op}","#000000");
        			fila.setVariable("{lat}", desp1.getLatitud()+"");
        			fila.setVariable("{lng}", desp1.getLongitud()+"");
    			} else {
    			    fila.setVariable("{color_texto_op}","#de0606");
        			fila.setVariable("{lat}", "0");
        			fila.setVariable("{lng}", "0");
    			}

    			if ( desp1.getTipo_despacho() == null ) {
    			    desp1.setTipo_despacho("N");
    			}
                
                if (desp1.getTipo_despacho().equals("R")) { //Retiro en Local
                    fila.setVariable("{color_fondo_op}","#FFFF99");
                    fila.setVariable("{persona}",desp1.getPersonaRetiraRecibe() + " " + desp1.getRutCompletoPersona());
                    
                } else {
        			if ( desp1.getTipo_despacho().equals("E")) {//Express
        			    fila.setVariable("{color_fondo_op}","#F7BB8B");
        			} else if (desp1.getTipo_despacho().equals("C")) {//Económico
        			    fila.setVariable("{color_fondo_op}","#C4FFC4");
        			} else {
        			    fila.setVariable("{color_fondo_op}","#FFFFFF");
        			}
                    fila.setVariable("{persona}",desp1.getPersonaRetiraRecibe());
                }
                
    			if ( desp1.getDir_conflictiva() != null && desp1.getDir_conflictiva().equals("1")) {
    			    fila.setVariable("{dir_conflictiva}", "<a href='ViewDespacho?id_pedido="+ desp1.getId_pedido() +"'>Sí</a>");
    			} else {
    			    fila.setVariable("{dir_conflictiva}", "-");
    			}
    			//grosor del borde			
    			String zona_actual = desp1.getZona_despacho();
    			fila.setVariable("{borderstyle}","");
    			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
    				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
    			}
    			zona_anterior = zona_actual;
                
                if (desp1.getReprogramada() > 0) {
                    fila.setVariable("{rep}" , "Si");
                } else {
                    fila.setVariable("{rep}" , "No");
                }
			
                if (desp1.getComuna() == null) {
                    fila.setVariable("{comuna_desp}" ,"Retiro en Local");
                } else {
                    fila.setVariable("{comuna_desp}" , desp1.getComuna());
                }
    			fila.setVariable("{estado_desp}"	, desp1.getEstado());
    			
    			fila.setVariable("{dir_calle}"	, desp1.getDir_calle());
    			fila.setVariable("{dir_numero}"	, desp1.getDir_numero());
    			if ( !desp1.getDir_depto().equals("") ) {
    				fila.setVariable("{dir_depto}"	, "-"+desp1.getDir_depto());
    			} else {
    				fila.setVariable("{dir_depto}"	, "&nbsp;");
    			}
                if ( desp1.getIdRuta() > 0 || desp1.getTipo_despacho().equalsIgnoreCase("R") || !pedidoPuedeSerEnrutado(desp1.getId_estado()) ) {
                    fila.setVariable("{checkbox}"  , "&nbsp;");    
                } else { 
                    fila.setVariable("{checkbox}"  , "<input name=\"pedidos\" type=\"checkbox\" value=\""+desp1.getId_pedido()+"\" onClick=\"javascript:asignaLocal("+local.getLatitud() + "," + local.getLongitud() +",'" + usr.getLocal() + "'); cambioBinsSeleccionados();\"> ");
                }
                fila.setVariable("{doc}"  , desp1.getTipoDocumento());
                         
//                String fecHoraPick = desp1.getFecha_picking()+" "+desp1.getHfin_jpick();
//                
//                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date datePick = (Date)formatter.parse(fecHoraPick);
//                Date dateActual = formatter.parse(formatter.format(new Date()));
//                long tiempoPicking = datePick.getTime();
//                long tiempoActual = dateActual.getTime();
                
//                if ((desp1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_INGRESADO  ||
//    			    desp1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ||
//    			    desp1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO   ||
//    			    desp1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PICKING ||
//    			    desp1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA) ) { //&& tiempoPicking < tiempoActual   ) {
//                    fila.setVariable("{cam_color1}"	, " <FONT color=red> ");
//					fila.setVariable("{cam_color2}"	, " </FONT> ");
//                } else {
                    fila.setVariable("{cam_color1}"	, "");
                    fila.setVariable("{cam_color2}"	, "");
//                }
                datos.add(fila);
            }
		}
		
        ArrayList htmlZonas = new ArrayList();
        List zonas = biz.getZonasLocal(usr.getId_local());
        for ( int i=0; i < zonas.size(); i++ ) {
            ZonaDTO zona = (ZonaDTO) zonas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_zona}", String.valueOf(zona.getId_zona()));
            fila.setVariable("{nombre}", zona.getNombre());
            if ( idZona == zona.getId_zona() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlZonas.add(fila);            
        }
        
        ArrayList htmlTipos = new ArrayList();
        
        List tiposPedido = Utils.comboTipoPedido();
        for (int i=0; i < tiposPedido.size(); i++) {
            ObjetoDTO obj = (ObjetoDTO) tiposPedido.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{tipo}", obj.getCodigo());
            fila.setVariable("{nombre}", obj.getNombre());
            if ( obj.getCodigo().equalsIgnoreCase(tipoPedido)) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlTipos.add(fila);
        }
        /*
        ArrayList htmlJornadas = new ArrayList();
        List jornadas = biz.getJornadasDespachoParaFiltro(usr.getId_local(), fecha, idZona);
        for ( int i=0; i < jornadas.size(); i++ ) {
            JornadaDTO jor = (JornadaDTO) jornadas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_jornada}", String.valueOf(jor.getId_jornada()));
            fila.setVariable("{jornada}", Formatos.frmHoraSola(jor.getH_inicio()) + " - " + Formatos.frmHoraSola(jor.getH_fin()) + "   " + Formatos.frmFecha(jor.getF_jornada()));
            if ( idJornada == jor.getId_jornada() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlJornadas.add(fila);            
        }
        */
        List jornadas = biz.getJornadasDespachoParaFiltro(usr.getId_local(), fecha, idZona);
        Set horasJornadas = new TreeSet();
        for ( int i=0; i< jornadas.size();i++ ) {
            JornadaDTO jor = (JornadaDTO) jornadas.get(i);
            horasJornadas.add(Formatos.frmHoraSola( jor.getH_inicio() )+"-"+ Formatos.frmHoraSola( jor.getH_fin() ));
        }
        ArrayList htmlJornadas = new ArrayList();
        for( Iterator it = horasJornadas.iterator(); it.hasNext();) {
            IValueSet fila = new ValueSet();
            String horasTotal =(String)it.next();
            fila.setVariable("{horas}"    ,horasTotal);
            if (horasTotal.equalsIgnoreCase(horasJor))
                fila.setVariable("{selected}", "selected");
            else
                fila.setVariable("{selected}", "");
            htmlJornadas.add(fila);
        }
        
        
        ArrayList htmlReprogramadas = new ArrayList();
        List reprogramadas = Utils.comboReprogramada();
        for (int i=0; i < reprogramadas.size(); i++) {
            ObjetoDTO obj = (ObjetoDTO) reprogramadas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{rep}", obj.getCodigo());
            fila.setVariable("{desc}", obj.getNombre());
            if ( obj.getCodigo().equalsIgnoreCase(reprogramada)) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlReprogramadas.add(fila);
        }
        
        ArrayList htmlRutas = new ArrayList();
        List rutas = biz.getRutasDisponibles( usr.getId_local() );
        for ( int i=0; i < rutas.size(); i++ ) {
            RutaDTO ruta = (RutaDTO) rutas.get(i);
            IValueSet fila = new ValueSet();
            //Usamos la cant de bins que quedan disponibles (total camion - usados)
            if ( ruta.getPatente().getCantMaxBins() - ruta.getCantBins() < 0 ) {
                fila.setVariable("{id_ruta}", Formatos.frmFecha(ruta.getFechaCreacion()) + "-=-" + String.valueOf(ruta.getIdRuta()) + "-=-0" );
            } else {
                fila.setVariable("{id_ruta}", Formatos.frmFecha(ruta.getFechaCreacion()) + "-=-" + String.valueOf(ruta.getIdRuta()) + "-=-" + (ruta.getPatente().getCantMaxBins() - ruta.getCantBins() ) );
            }
            fila.setVariable("{ruta}", ruta.getIdRuta() + ") [" + Formatos.frmFecha(ruta.getFechaCreacion()) + "] " + ruta.getChofer().getNombre() + " - " + ruta.getFono().getNombre() + " (" + ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero() + ") - " + ruta.getPatente().getPatente());
            htmlRutas.add(fila);            
        }
        
		top.setVariable("{fecha}", fecha);
        top.setVariable("{id_pedido}", String.valueOf(idPedido));
		
		top.setDynamicValueSets("listado", datos);
        top.setDynamicValueSets("ZONAS", htmlZonas);
        top.setDynamicValueSets("TIPOS", htmlTipos);
        top.setDynamicValueSets("JORNADAS", htmlJornadas);
        top.setDynamicValueSets("RUTAS", htmlRutas);
        top.setDynamicValueSets("REPROGRAMADA", htmlReprogramadas);
        
        
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}

    /**
     * @param id_estado
     * @return
     */
    private boolean pedidoPuedeSerEnrutado(long idEstado) {
        for (int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_RUTAS.length; i++) {
            if ( idEstado == Long.parseLong(Constantes.ESTADOS_PEDIDO_PARA_RUTAS[i]) ) {
                return true;
            }
        }
        return false;
    }

    
}
