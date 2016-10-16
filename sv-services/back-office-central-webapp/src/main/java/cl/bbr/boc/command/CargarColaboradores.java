package cl.bbr.boc.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.commons.fileupload.FileItem; 
import org.apache.commons.fileupload.FileUploadException; 
import org.apache.commons.fileupload.disk.DiskFileItemFactory; 
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.ColaboradorDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CargarColaboradores extends Command {

	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,	UserDTO usr) throws Exception {		
		try {
			logger.info("Inicio de CargarColaboradores");
			logger.debug("User: " + usr.getLogin());
			int resultado = 0;
			BizDelegate biz = new BizDelegate();
			List colaboradores = leerExcel(upFileMemory(req));
			if(colaboradores != null && biz.truncateTableColaborador() && biz.cargarColaboradores(colaboradores)){
				resultado = 1;
			} else {
				resultado = 0;
			}
			String cantidad = biz.cantidadColaboradores();
			logger.info("Inicio de CargarColaboradores");
			res.sendRedirect("ViewPromoDsctoColab?resultado=" + resultado + "&cantidad=" + cantidad);
		} catch (Exception e) {
			logger.error("Error: ", e);
			res.sendRedirect("ViewError?mensaje=Ocurrio un error en el proceso de carga de colaboradores");
		}		
	}
	
	private InputStream upFileMemory(HttpServletRequest request) throws FileUploadException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List  items = upload.parseRequest(request);
			
			Iterator iter = items.iterator(); 
			while (iter.hasNext()) { 
				FileItem item = (FileItem) iter.next();
	
			  if (!item.isFormField())   { 
			// Empezamos a leer las propiedades del archivo
			/*	String fieldName = item.getFieldName(); 
				String fileName = item.getName(); 
				String contentType = item.getContentType(); 
				boolean isInMemory = item.isInMemory(); 
				long sizeInBytes = item.getSize();*/
	
				return  item.getInputStream();	
	
			  }
			}
		}
		return null;
	}
	
	private List leerExcel(InputStream input) throws IOException{
		logger.info("Inicio de Lectura XLS");
		POIFSFileSystem fs = new POIFSFileSystem(input);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		int numRows = sheet.getLastRowNum() + 1;
		logger.info("Cantidad de Filas: " + numRows);
		List listaColaboradores = new ArrayList();
		boolean isFirst = true;
		int contFilas = 0;
		int contColumnas = 0;
		Iterator rows = sheet.rowIterator();
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			ColaboradorDTO colaborador = new ColaboradorDTO();
			contFilas++;
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				HSSFCell cell = (HSSFCell) cells.next();
				contColumnas++;
				if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
					if (isFirst) {
						colaborador.setColRut((int)cell.getNumericCellValue());
						isFirst = false;
					} else {
						colaborador.setCodEmpresa((int)cell.getNumericCellValue());
						isFirst = true;
					}
				} else if (HSSFCell.CELL_TYPE_STRING == cell.getCellType()) {
					colaborador.setEmpresa(cell.getRichStringCellValue().toString());
				} else if (HSSFCell.CELL_TYPE_BLANK == cell.getCellType()) {
					logger.info("Registro en Blanco Fila: " + contFilas	+ " Columna:" + contColumnas);
					if(numRows > contFilas){
						return null;
					}
				} else {
					logger.info("Registro en Invalido Fila: " + contFilas + " Columna:" + contColumnas);
					return null;
				}
			}
			contColumnas = 0;
			//if(!existeDuplicidad(colaborador, listaColaboradores))
				listaColaboradores.add(colaborador);
		}
		logger.info("Fin de Lectura XLS");
		logger.info("Cantidad de Filas: " + listaColaboradores.size());
		return listaColaboradores;
	}
	
	/*private boolean existeDuplicidad(ColaboradorDTO colaborador, List listaColaboradores){
		Iterator it = listaColaboradores.iterator();
		while(it.hasNext()){
		  ColaboradorDTO colab = (ColaboradorDTO) it.next();
		  if(colaborador.getColRut() == colab.getColRut()){
			  return true;
		  }
		}
		return false;
	}*/
	
}
