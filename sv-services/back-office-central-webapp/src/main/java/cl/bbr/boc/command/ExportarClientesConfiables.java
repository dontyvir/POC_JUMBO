package cl.bbr.boc.command;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.RutConfiableDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ExportarClientesConfiables extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {

        logger.debug( "Inicio ExportarClientesConfiables" );
        try {
            HSSFWorkbook objWB = planillaExcel();           
            
            ServletContext context = getServletConfig().getServletContext();
            String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
            File directorioDestino = new File(rutaUpload);
            EventosUtil.eliminarArchivosAntiguos(directorioDestino);
            
            File archivoNuevo = File.createTempFile("cli_confiables", ".xls", directorioDestino);
            
            FileOutputStream out = new FileOutputStream(archivoNuevo);
            objWB.write(out);            
            out.close();
            
            res.setContentType("application/x-filedownload");
            res.setHeader("Content-Disposition", "attachment;filename=ClientesConfiables_" + Utils.getFechaActualByPatron("dd-MM-yyyy_HH-mm")  + ".xls");
     
            RequestDispatcher rd = context.getRequestDispatcher("/upload_ruts_eventos/"+archivoNuevo.getName());
            rd.forward(req,res);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug( "Fin ExportarClientesConfiables" );
    }
    
    private HSSFWorkbook planillaExcel() throws BocException {
        
        BizDelegate biz = new BizDelegate();
        
        //creamos el libro
        HSSFWorkbook objWB = new HSSFWorkbook();
        
        //creamos hoja
        HSSFSheet hoja1 = objWB.createSheet("hoja 1");
        
        //creamos una fila
        HSSFRow fila = hoja1.createRow((short) 0);
        fila.setHeight((short) (206*5));
        
        //Seteamos estilos para la planilla
        HSSFFont fuente = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_BOLD, (short)8);
        HSSFCellStyle estiloCelda = estiloCelda(objWB, fuente, HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        
        //creamos celda
        HSSFCell celda = fila.createCell(0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_STRING);
        celda.setCellValue(new HSSFRichTextString("RUT CLIENTE  (Sin puntos y sin espacios)"));        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("DV CLIENTE"));
        
        hoja1.setColumnWidth(0, 256*15 );
        hoja1.setColumnWidth(1, 256*12 );
                
        List clientes = biz.getRutsConfiables();
        
        HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, (short)8);
        HSSFCellStyle estiloCeldaNormal = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_RIGHT);
        
        for (int i = 0; i < clientes.size(); i++) {           
            RutConfiableDTO rutCli = (RutConfiableDTO) clientes.get(i);
            fila = hoja1.createRow((short) (i+1));
            
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(rutCli.getRut());  
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(rutCli.getDv()));
                        
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
