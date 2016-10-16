package cl.bbr.boc.view;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.pedidos.dto.ModificacionPrecioDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra informe Modificacion de Precios
 * 
 * @author imoyano
 */
public class ViewInformeModPrecios extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewInformeModPrecios Execute");
	    
	    String fcIni = "";
        String fcFin = "";
        long local = 0;
        String usuario = "";
        String tipo = "";
	    
		if (req.getParameter("tipo") != null) {
            tipo = req.getParameter("tipo").toString();
        }
		if (req.getParameter("fc_ini") != null) {
		    fcIni = req.getParameter("fc_ini").toString();
	    }
	    if (req.getParameter("fc_fin") != null) {
	        fcFin = req.getParameter("fc_fin").toString();
	    }
	    if (req.getParameter("local") != null) {
	        local = Long.parseLong( req.getParameter("local").toString() );
	    }
	    if (req.getParameter("usuario") != null) {
	        usuario = req.getParameter("usuario").toString();
	    }
	    
	    BizDelegate biz = new BizDelegate();
	    
		List informes = biz.getInformeModificacionDePrecios(fcIni,fcFin,local,usuario);
                
        if ("P".equalsIgnoreCase(tipo)) {
            //Generamos la planilla excel
            try {
                HSSFWorkbook objWB = planillaExcel(informes);           
                
                ServletContext context = getServletConfig().getServletContext();
                String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
                File directorioDestino = new File(rutaUpload);
                EventosUtil.eliminarArchivosAntiguos(directorioDestino);
                
                File archivoNuevo = File.createTempFile("modificacion_precios", ".xls", directorioDestino);
                
                FileOutputStream out = new FileOutputStream(archivoNuevo);
                objWB.write(out);            
                out.close();
                
                res.setContentType("application/x-filedownload");
                res.setHeader("Content-Disposition", "attachment;filename=" + Utils.getFechaActualByPatron("ddMMyyyy")  + "_ModificacionPrecios.xls");
         
                RequestDispatcher rd = context.getRequestDispatcher("/upload_ruts_eventos/"+archivoNuevo.getName());
                rd.forward(req,res);

            } catch (Exception e) {
                e.printStackTrace();
           } 
        
        } else {
            //Mostramos el resultado por pantalla
            View salida = new View(res);
            String html = path_html + getServletConfig().getInitParameter("TplFile");       
            
            TemplateLoader load = new TemplateLoader(html);
            ITemplate tem = load.getTemplate();
            IValueSet top = new ValueSet();
            
            ArrayList datos = new ArrayList();
             
            for (int i = 0; i < informes.size(); i++) {         
                IValueSet fila = new ValueSet(); 
                ModificacionPrecioDTO inf = (ModificacionPrecioDTO)informes.get(i); 
                fila.setVariable("{fecha_hora}" , Formatos.frmFechaHora( inf.getFechaHora() ));
                fila.setVariable("{usuario}" , inf.getUsuario());
                fila.setVariable("{id_pedido}" , String.valueOf(inf.getIdPedido()));
                fila.setVariable("{id_det_picking}" , String.valueOf(inf.getIdDetPicking()));
                fila.setVariable("{prod}" , inf.getProducto());
                fila.setVariable("{precio_ori}" , Formatos.formatoPrecio( inf.getPrecioOriginal() ));
                fila.setVariable("{precio_nvo}" , Formatos.formatoPrecio( inf.getPrecioNuevo() ));
                fila.setVariable("{monto_reserva}" , Formatos.formatoPrecio( inf.getMontoReservaOp() ));
                fila.setVariable("{monto_original}" , Formatos.formatoPrecio( inf.getMontoOriginalOp() + inf.getDespacho() ));
                fila.setVariable("{monto_nuevo}" , Formatos.formatoPrecio( inf.getMontoNuevoOp() + inf.getDespacho() ));
                datos.add(fila);            
            }
            
            if ( informes.size() == 0 ) {
                top.setVariable("{mje1}","La consulta no arrojó resultados");
                top.setVariable("{dis_btn}","disabled");
            } else {
                top.setVariable("{mje1}","");
                top.setVariable("{dis_btn}","");
            }
            
            List users = biz.getUsuariosInformeModPrecios();
            ArrayList ausers = new ArrayList();
            for (int i = 0; i < users.size(); i++) {          
                IValueSet fila = new ValueSet();
                UsuariosEntity usu = (UsuariosEntity) users.get(i);
                fila.setVariable("{usuario}",String.valueOf(usu.getLogin()));
                if ( usu.getLogin().equalsIgnoreCase( usuario ) ) {
                    fila.setVariable("{sel_est}", "selected");
                } else {
                    fila.setVariable("{sel_est}", "");
                }
                ausers.add(fila);
            }
            
            List locales = biz.getLocales();
            ArrayList locs = new ArrayList();
            for (int i = 0; i < locales.size(); i++) {          
                IValueSet fila = new ValueSet();
                LocalDTO loc = (LocalDTO)locales.get(i);
                fila.setVariable("{id_local}",String.valueOf(loc.getId_local()));
                fila.setVariable("{local}",loc.getNom_local());
                if (local == loc.getId_local()) {
                    fila.setVariable("{sel_est}", "selected");
                } else {
                    fila.setVariable("{sel_est}", "");
                }
                locs.add(fila);
            }
            
            top.setVariable("{fc_ini}",fcIni);
            top.setVariable("{fc_fin}",fcFin);
            top.setDynamicValueSets("INFORME", datos);
            top.setDynamicValueSets("USUARIOS", ausers);
            top.setDynamicValueSets("LOCALES", locs);
            
            top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
            Date now = new Date();
            top.setVariable("{hdr_fecha}", now.toString()); 
            
            logger.debug("Fin ViewInformeModPrecios Execute");
            String result = tem.toString(top);
            salida.setHtmlOut(result);
            salida.Output();
        }		
	}

    /**
     * @param informes
     * @return
     */
    private HSSFWorkbook planillaExcel(List informes) {
        //creamos el libro
        HSSFWorkbook objWB = new HSSFWorkbook();
        
        //creamos hoja
        HSSFSheet hoja1 = objWB.createSheet("hoja 1");
        
        //creamos una fila
        HSSFRow fila = hoja1.createRow((short) 0);
        
        //Seteamos estilos para la planilla
        HSSFFont fuente = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.GREEN.index);
        HSSFCellStyle estiloCelda = estiloCelda(objWB, fuente, HSSFCellStyle.ALIGN_CENTER);
        
        //creamos celda
        HSSFCell celda = fila.createCell(0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_STRING);
        celda.setCellValue(new HSSFRichTextString("Fecha/Hora"));        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Usuario"));        
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("OP"));        
        celda = fila.createCell(3);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Detalle de Picking"));
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Producto"));
        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Precio Original"));
        celda = fila.createCell(6);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Precio Nuevo"));
        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("OP Reserva"));
        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("OP Pickeado"));
        celda = fila.createCell(9);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("OP Nuevo"));
                
        HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, HSSFColor.BLACK.index);
        HSSFCellStyle estiloCeldaNormal = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_RIGHT);        
        
        for (int i = 0; i < informes.size(); i++) {         
            ModificacionPrecioDTO inf = (ModificacionPrecioDTO)informes.get(i);
            fila = hoja1.createRow((short) (i+1));
            
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.frmFechaHora( inf.getFechaHora() )));
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(inf.getUsuario()));
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(String.valueOf(inf.getIdPedido())));
            celda = fila.createCell(3);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(String.valueOf(inf.getIdDetPicking())));
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(inf.getProducto()));
            celda = fila.createCell(5);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.formatoPrecio( inf.getPrecioOriginal() )));
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.formatoPrecio( inf.getPrecioNuevo() )));
            celda = fila.createCell(7);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.formatoPrecio( inf.getMontoReservaOp() )));
            celda = fila.createCell(8);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.formatoPrecio( inf.getMontoOriginalOp() + inf.getDespacho() ) ));
            celda = fila.createCell(9);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(Formatos.formatoPrecio( inf.getMontoNuevoOp() + inf.getDespacho() )));
                        
        }
        return objWB;
    }

    /**
     * @return
     */
    private HSSFCellStyle estiloCelda(HSSFWorkbook objWB, HSSFFont fuente, short alineacion) {
        HSSFCellStyle estiloCelda = objWB.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setAlignment(alineacion);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setFont(fuente);
        estiloCelda.setBorderBottom((short) 1);
        estiloCelda.setBorderLeft((short) 1);
        estiloCelda.setBorderRight((short) 1);
        estiloCelda.setBorderTop((short) 1);
        return estiloCelda;
    }

    /**
     * @return
     */
    private HSSFFont estiloFuente(HSSFWorkbook objWB, short bold, short color) {
        HSSFFont fuente = objWB.createFont();
        fuente.setColor(color);
        fuente.setFontHeightInPoints((short) 9);
        fuente.setFontName(HSSFFont.FONT_ARIAL);
        fuente.setBoldweight(bold);
        return fuente;
    } 
}
