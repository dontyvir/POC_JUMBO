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
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.pedidos.dto.ProductoSinStockDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra informe Modificacion de Precios
 * 
 * @author imoyano
 */
public class ViewInformeProdSinStock extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewInformeProdSinStock");
	    
	    String tipo = "";
	    
		if (req.getParameter("tipo") != null) {
            tipo = req.getParameter("tipo").toString();
        }
		
	    BizDelegate biz = new BizDelegate();
	    
		List informes = biz.getInformeProductosSinStock();
                
        if ("P".equalsIgnoreCase(tipo)) {
            //Generamos la planilla excel
            try {
                HSSFWorkbook objWB = planillaExcel(informes);           
                
                ServletContext context = getServletConfig().getServletContext();
                String rutaUpload = req.getSession().getServletContext().getRealPath("upload_productos_sinstock");
                File directorioDestino = new File(rutaUpload);
                EventosUtil.eliminarArchivosAntiguos(directorioDestino);
                
                File archivoNuevo = File.createTempFile("productos_sinstock", ".xls", directorioDestino);
                
                FileOutputStream out = new FileOutputStream(archivoNuevo);
                objWB.write(out);            
                out.close();
                
                res.setContentType("application/x-filedownload");
                res.setHeader("Content-Disposition", "attachment;filename=" + Utils.getFechaActualByPatron("ddMMyyyy")  + "_ProductosSinStock.xls");
         
                RequestDispatcher rd = context.getRequestDispatcher("/upload_productos_sinstock/"+archivoNuevo.getName());
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
                ProductoSinStockDTO inf = (ProductoSinStockDTO)informes.get(i); 
                fila.setVariable("{id_prod}" , String.valueOf( inf.getIdProductoFo() ));
                fila.setVariable("{nombre_local}" , inf.getNombreLocal());
                datos.add(fila);            
            }
            
            if ( informes.size() == 0 ) {
                top.setVariable("{mje1}","La consulta no arrojó resultados");
                top.setVariable("{dis_btn}","disabled");
            } else {
                top.setVariable("{mje1}","");
                top.setVariable("{dis_btn}","");
            }
            
            top.setDynamicValueSets("INFORME", datos);
            
            top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
            Date now = new Date();
            top.setVariable("{hdr_fecha}", now.toString());
            
            logger.debug("Fin ViewInformeProdSinStock Execute");
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
        celda.setCellValue(new HSSFRichTextString("Id Producto"));        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Local"));        
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda);
                
        HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, HSSFColor.BLACK.index);
        HSSFCellStyle estiloCeldaNormal = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_RIGHT);        
        
        for (int i = 0; i < informes.size(); i++) {         
            ProductoSinStockDTO inf = (ProductoSinStockDTO)informes.get(i);
            fila = hoja1.createRow((short) (i+1));
            
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(String.valueOf( inf.getIdProductoFo() )));
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(inf.getNombreLocal()));
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
