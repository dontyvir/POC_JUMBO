/*
 * Created on 06-jul-2009
 */
package cl.bbr.boc.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.oreilly.servlet.MultipartRequest;

import cl.bbr.boc.dto.SubrubroDTO;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class UpdSubrubros extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
      MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
      File archivo = multi.getFile("archivo");
      List lista = cargarArchivo(archivo);
      ProductosService serv = new ProductosService();
      serv.updRubrubro(lista);
      String exito = getServletConfig().getInitParameter("exito");
      res.sendRedirect(exito);
   }

   /**
    * @param archivo
    * @throws IOException
    */
   private List cargarArchivo(File archivo) throws IOException {
      List lista = new ArrayList();
      FileInputStream inputfile = new FileInputStream(archivo.getAbsolutePath());
      HSSFWorkbook xls = new HSSFWorkbook(inputfile);
      HSSFSheet sheet = xls.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
         HSSFRow row = sheet.getRow(i);
         HSSFCell colAlias = row.getCell(0); //primera columna subrubro
         HSSFCell colPrecio = row.getCell(1); //segunda columna precio total
         String dato = colAlias.toString();
         SubrubroDTO subrubro = new SubrubroDTO();
         subrubro.setSubrubro(dato.trim().substring(0,6));
         subrubro.setPrecioTotal(new BigDecimal(colPrecio.getNumericCellValue()));
         lista.add(subrubro);
      }
      archivo.delete();
      return lista;
   }
}