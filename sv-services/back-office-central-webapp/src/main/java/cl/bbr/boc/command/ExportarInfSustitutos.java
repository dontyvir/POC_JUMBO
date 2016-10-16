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

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.informes.dto.OriginalesYSustitutosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agrega un nuevo evento
 * 
 * @author imoyano
 */

public class ExportarInfSustitutos extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        logger.debug("Comienzo ExportarInfSustitutos Execute");        
        try {
            HSSFWorkbook objWB = planillaExcel();           
        
            ServletContext context = getServletConfig().getServletContext();
            String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
            File directorioDestino = new File(rutaUpload);
            EventosUtil.eliminarArchivosAntiguos(directorioDestino);
            
            File archivoNuevo = File.createTempFile("sustitutos_ori_", ".xls", directorioDestino);
            
            FileOutputStream out = new FileOutputStream(archivoNuevo);
            objWB.write(out);            
            out.close();
            
            res.setContentType("application/x-filedownload");
            res.setHeader("Content-Disposition", "attachment;filename=ProdOrigYSust_" + Utils.getFechaActualByPatron("dd-MM-yyyy_HH-mm")  + ".xls");
     
            RequestDispatcher rd = context.getRequestDispatcher("/upload_ruts_eventos/"+archivoNuevo.getName());
            rd.forward(req,res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("Fin ExportarInfSustitutos Execute");
	}

    /**
     * @return
     * @throws BocException
     */
    private HSSFWorkbook planillaExcel() throws BocException {
        
        BizDelegate biz = new BizDelegate();
        
        //creamos el libro
        HSSFWorkbook objWB = new HSSFWorkbook();
        
        //creamos hoja
        HSSFSheet hoja1 = objWB.createSheet("hoja 1");
        
        //creamos una fila
        HSSFRow fila = hoja1.createRow((short) 0);
        
        //Seteamos estilos para la planilla
        HSSFFont fuente = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_BOLD, (short)39);
        HSSFCellStyle estiloCelda = estiloCelda(objWB, fuente);
        
        //creamos celda
        HSSFCell celda = fila.createCell(0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_STRING);
        celda.setCellValue(new HSSFRichTextString("Cod. Original"));        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Unidad Medida"));        
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Cod. Sustituto"));        
        celda = fila.createCell(3);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Unidad Medida"));        
        		
		List sustitutos = biz.getOriginalesYSustitutos();
        
		HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, (short)8);
        HSSFCellStyle estiloCeldaNormal = estiloCelda(objWB, fuenteNormal);
		
        for (int i = 0; i < sustitutos.size(); i++) {			
            OriginalesYSustitutosDTO prod = (OriginalesYSustitutosDTO) sustitutos.get(i);
            fila = hoja1.createRow((short) (i+1));
            
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(prod.getCodProdOriginal()));  
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(prod.getUniMedProdOriginal()));
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(prod.getCodProdSustituto()));        
            celda = fila.createCell(3);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(prod.getUniMedProdSustituto()));            
		}        
        return objWB;
    }

    /**
     * @return
     */
    private HSSFCellStyle estiloCelda(HSSFWorkbook objWB, HSSFFont fuente) {
        HSSFCellStyle estiloCelda = objWB.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setFont(fuente);
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
