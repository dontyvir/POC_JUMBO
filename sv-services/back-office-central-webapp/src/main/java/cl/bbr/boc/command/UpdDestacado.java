package cl.bbr.boc.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.oreilly.servlet.MultipartRequest;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author Jean
 */

public class UpdDestacado extends Command {
    private final static long serialVersionUID = 1;

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
        MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
        String id = verificaNull(multi.getParameter("destacado_id"));
        String descripcion = verificaNull(multi.getParameter("descripcion"));
        String fechaIni = verificaNull(multi.getParameter("fecha_ini"));
        String horaIni = verificaNull(multi.getParameter("hora_ini"));

        String fechaFin = verificaNull(multi.getParameter("fecha_fin"));
        String horaFin = verificaNull(multi.getParameter("hora_fin"));

        String imagen = verificaNull(multi.getParameter("imagen"));
        String[] locales = multi.getParameterValues("local");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date fechaHoraIni = sdf.parse(fechaIni + " " + horaIni);
        Date fechaHoraFin = sdf.parse(fechaFin + " " + horaFin);

        DestacadoDTO destacadoDTO = new DestacadoDTO();
        destacadoDTO.setId(Integer.parseInt(id));
        destacadoDTO.setDescripcion(descripcion);
        destacadoDTO.setFechaHoraIni(fechaHoraIni);
        destacadoDTO.setFechaHoraFin(fechaHoraFin);
        destacadoDTO.setImagen(imagen);

        for (int i = 0; i < locales.length; i++) {
            LocalDTO localDTO = new LocalDTO();
            localDTO.setId(Integer.parseInt(locales[i]));
            destacadoDTO.addLocal(localDTO);
        }

        File archivo = multi.getFile("archivo");
        if(archivo != null)
            cargarArchivo(archivo, destacadoDTO);

        BizDelegate biz = new BizDelegate();
        biz.updDestacado(destacadoDTO);

        //FIXME falta el control de excepciones
        String exito = getServletConfig().getInitParameter("exito");
        res.sendRedirect(exito);
    }

    /**
     * @param archivo
     * @throws IOException
     * @throws IOException
     */
    private void cargarArchivo(File archivo, DestacadoDTO destacadoDTO) throws IOException {
        FileInputStream inputfile = new FileInputStream(archivo.getAbsolutePath());
        HSSFWorkbook xls = new HSSFWorkbook(inputfile);
        HSSFSheet sheet = xls.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell colAlias = row.getCell(0); //primera columna
            double dato = colAlias.getNumericCellValue();
            FOProductoDTO pro = new FOProductoDTO();
            pro.setId((int) dato);
            destacadoDTO.addProducto(pro);
        }
        archivo.delete();
    }

    private String verificaNull(String dato) throws ParametroObligatorioException {
        if (dato == null) {
            throw new ParametroObligatorioException("Dato es null");
        }
        return dato;
    }
}
