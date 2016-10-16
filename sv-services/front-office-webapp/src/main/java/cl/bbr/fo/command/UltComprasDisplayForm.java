package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.jumbo.interfaces.ventaslocales.CompraHistorica;

/**
 * Despliega página de últimas compras
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class UltComprasDisplayForm extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("fo");

        try {
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");
            
            int acordeonConDatos = 0;

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Recupera pagina desde web.xml
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug("Template:" + pag_form);
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();

            IValueSet top = new ValueSet();

            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());

            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            SimpleDateFormat sdf = new java.text.SimpleDateFormat(Formatos.DATE);

            //trae ultimas compras historicas (de locales fisicos)
            List comprasLocales = biz.clienteComprasEnLocal( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
            List comprasInternet = biz.clienteGetUltComprasInternet(cliente_id.longValue());
            
            int comprasInt = 0;
            if (comprasInternet.size() > 0) {
//+20120529coh
//                if (comprasInternet.size() > 10)
//                    comprasInt = 10;
                if (comprasInternet.size() > 5)
                    comprasInt = 5;
//-20120529coh
                else
                    comprasInt = comprasInternet.size();    
            } else {
                comprasInt = 0;
            }
            
            List misListas = biz.clienteMisListas(cliente_id.longValue());
            List misListasPredefinidas = biz.clienteMisListasPredefinidas();

            // Indica al HTML cuantos registros hay
            top.setVariable("{cantreg}", comprasInt + comprasLocales.size() + misListas.size() + misListasPredefinidas.size() + "");

            List ultComprasInternet = new ArrayList();
            List ultComprasInternetFull = new ArrayList();
            int suma = 0;
            //+20121207 SBernal
            for (int i = 0; i < comprasInternet.size(); i++) {
            //-20121207 SBernal
                UltimasComprasDTO data = (UltimasComprasDTO) comprasInternet.get(i);
                IValueSet fila = new ValueSet();

                Calendar fecha = new GregorianCalendar();
                fecha.setTimeInMillis(data.getFecha()); // setea con fecha recibida

                //Se chequea solo la última compra de internet
                if (i == 0) {
                    fila.setVariable("{checked}", "checked");
                    acordeonConDatos = 0;
                } else {
                    fila.setVariable("{checked}", "");
                }
                fila.setVariable("{id}", data.getId() + "-" + data.getTipo());
                fila.setVariable("{fila}", data.getId() + "");
                fila.setVariable("{fecha}", sdf.format(fecha.getTime()));
                fila.setVariable("{lugar}", data.getLugar_compra());
                fila.setVariable("{unidades}", Formatos.formatoCantidadFO( data.getUnidades() ) + " Unidades");
                fila.setVariable("{nombre}", data.getNombre());
                fila.setVariable("{elim}", "<a href=\"javascript:eliminar_lista('" + data.getId() + "','" + data.getNombre() + "', 'I')\"><img src=\"/FO_IMGS/img/estructura/paso1/eliminar-p1.gif\" border=\"0\" alt=\"Eliminar Lista\" title=\"Eliminar Lista\"");
                fila.setVariable("{i}", suma + "");
                
                if(data.getNombre().length() > 0){
                	fila.setVariable("{nombre_fecha}", data.getNombre()+" - "+sdf.format(fecha.getTime()));
                }
                else{
                	fila.setVariable("{nombre_fecha}", sdf.format(fecha.getTime()));
                }
                
                suma++;
                if(i<5)
                	ultComprasInternet.add(fila);
            	ultComprasInternetFull.add(fila);
            }
            
            if (ultComprasInternet.size() > 0) {
                top.setDynamicValueSets("ULT_COMPRAS_INTERNET", ultComprasInternet);
                top.setDynamicValueSets("ULT_COMPRAS_INTERNET_FULL", ultComprasInternetFull);
                top.setVariable("{msj_listas_internet}", "");
                top.setVariable("{msj_listas_internet_full}", "");
            } else {
                top.setVariable("{msj_listas_internet}", "No existen listas para este item.");
                top.setVariable("{msj_listas_internet_full}", "No existen listas para este item.");
            }
            
            //se agregan ultimas compras fisicas para desplegar en el formulario
            List ultComprasLocales = new ArrayList();
//+20120516coh
/*            for (int i = 0; i < comprasLocales.size(); i++) {*/
            for (int i = 0; i < comprasLocales.size(); i++) {
//-20120516coh
                CompraHistorica ch = (CompraHistorica) comprasLocales.get(i);
                IValueSet fila = new ValueSet();
                if (suma == 0) {
                    fila.setVariable("{checked}", "checked");
                    acordeonConDatos = 1;
                } else {
                    fila.setVariable("{checked}", "");
                }
                fila.setVariable("{id}", ch.getIdCompra() + "-" + "L");
                fila.setVariable("{fila}", ch.getIdCompra() + "");
//              14122012 VMATHEU
                fila.setVariable("{fecha}", "-");
                fila.setVariable("{lugar}", "Compras últimos 3 meses en locales");
                fila.setVariable("{nombre}", "Compras últimos 3 meses en locales");
//              --14122012 VMATHEU
                
                fila.setVariable("{unidades}", Formatos.formatoCantidadFO( ch.getUnidades() ) + " Unidades");
                
                fila.setVariable("{i}", suma + "");
                fila.setVariable("{elim}", "");
                ultComprasLocales.add(fila);
                suma++;
            }
            
            if (ultComprasLocales.size() > 0) {
                top.setDynamicValueSets("ULT_COMPRAS_LOCAL", ultComprasLocales);
                top.setDynamicValueSets("ULT_COMPRAS_LOCAL_FULL", ultComprasLocales);
                top.setVariable("{msj_listas_locales}", "");
                top.setVariable("{msj_listas_locales_full}", "");
            } else {
                top.setVariable("{msj_listas_locales}", "No existen listas para este item.");
                top.setVariable("{msj_listas_locales_full}", "No existen listas para este item.");
            }

            top.setVariable("{cant_listas_internet}", comprasInternet.size() + "");

            List misListasCliente = new ArrayList();
//          +20120516coh
            /*            for (int i = 0; i < misListas.size(); i++) {*/
            List misListasClienteFull = new ArrayList();
            for (int i = 0; i < misListas.size(); i++) {
//            -20120516coh
                UltimasComprasDTO data = (UltimasComprasDTO) misListas.get(i);
                IValueSet fila = new ValueSet();

                Calendar fecha = new GregorianCalendar();
                fecha.setTimeInMillis(data.getFecha()); // setea con fecha recibida

                if (suma == 0) {
                    fila.setVariable("{checked}", "checked");
                    acordeonConDatos = 2;
                } else {
                    fila.setVariable("{checked}", "");
                }
                fila.setVariable("{id}", data.getId() + "-" + data.getTipo());
                fila.setVariable("{fila}", data.getId() + "");
                fila.setVariable("{fecha}", sdf.format(fecha.getTime()));
                fila.setVariable("{lugar}", data.getLugar_compra());
                fila.setVariable("{unidades}", Formatos.formatoCantidadFO( data.getUnidades() ) + " Unidades");
                fila.setVariable("{nombre}", data.getNombre());
                fila.setVariable("{elim}", "<a href=\"javascript:eliminar_lista('" + data.getId() + "','" + data.getNombre() + "', 'M')\"><img src=\"/FO_IMGS/img/estructura/paso1/eliminar-p1.gif\" border=\"0\" alt=\"Eliminar Lista\" title=\"Eliminar Lista\"");
                fila.setVariable("{i}", suma + "");
                suma++;
                if(i<5)
                	misListasCliente.add(fila);
            	misListasClienteFull.add(fila);
            }
            if (misListasCliente.size() > 0) {
                top.setDynamicValueSets("MIS_LISTAS_INTERNET", misListasCliente);
                //20121207 SBernal
                top.setDynamicValueSets("MIS_LISTAS_INTERNET_FULL", misListasClienteFull);
                //-20121207 SBernal
                top.setVariable("{msj_mis_listas}", "");
                top.setVariable("{msj_mis_listas_full}", "");
            } else {
                top.setVariable("{msj_mis_listas}", "No existen listas para este item.");
                top.setVariable("{msj_mis_listas_full}", "No existen listas para este item.");
            }

            top.setVariable("{cant_listas_mis}", misListas.size() + "");
            
            List misListasPredefinidasCliente = new ArrayList();
//+20120516coh
            List misListasPredefinidasClienteFull = new ArrayList();
            for (int i = 0; i < misListasPredefinidas.size(); i++) {
//            for (int i = 0; i < 5; i++) {
//-20120516coh
                UltimasComprasDTO data = (UltimasComprasDTO) misListasPredefinidas.get(i);
                IValueSet fila = new ValueSet();

                Calendar fecha = new GregorianCalendar();
                fecha.setTimeInMillis(data.getFecha()); // setea con fecha recibida

                if (suma == 0) {
                    fila.setVariable("{checked}", "checked");
                    acordeonConDatos = 3;
                } else {
                    fila.setVariable("{checked}", "");
                }
                fila.setVariable("{id}", data.getId() + "-" + data.getTipo());
                fila.setVariable("{fila}", data.getId() + "");
                fila.setVariable("{fecha}", sdf.format(fecha.getTime()));
                fila.setVariable("{lugar}", data.getLugar_compra());
                fila.setVariable("{unidades}", data.getUnidades() + "");
                fila.setVariable("{nombre}", data.getNombre());
                fila.setVariable("{elim}", "");
                fila.setVariable("{i}", suma + "");
                suma++;
                if(i<5)
                	misListasPredefinidasCliente.add(fila);
                misListasPredefinidasClienteFull.add(fila);
            }
            if (misListasPredefinidasClienteFull.size() > 0) {
                top.setDynamicValueSets("LISTAS_PREDEFINIDAS", misListasPredefinidasCliente);
                top.setDynamicValueSets("LISTAS_PREDEFINIDAS_FULL", misListasPredefinidasClienteFull);
                top.setVariable("{msj_listas_predefinidas}", "");
                top.setVariable("{msj_listas_predefinidas_full}", "");
            } else {
                top.setVariable("{msj_listas_predefinidas}", "No existen listas para este item.");
                top.setVariable("{msj_listas_predefinidas_full}", "No existen listas para este item.");
            }
            
            top.setVariable("{lista_seleccionada}", ""+acordeonConDatos);

           
            if(session.getAttribute("ses_cli_nombre_pila").toString().equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
            }
            else{
                top.setVariable("{ta_mx_user}", "registrado");
            }
          //Tags Analytics - Captura de Comuna y Región en Texto
          /************   LISTADO DE REGIONES   ****************/
          
          String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
          if(ta_mx_loc.equals(""))
                ta_mx_loc="none-none";
          top.setVariable("{ta_mx_loc}", ta_mx_loc);
          
          top.setVariable("{ta_mx_content}", "Mis Listas - Menú Lateral");
          String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
            throw new CommandException(e);
        }
    }
}