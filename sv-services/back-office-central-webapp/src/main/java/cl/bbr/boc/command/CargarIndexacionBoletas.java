package cl.bbr.boc.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.commons.fileupload.FileItem; 
import org.apache.commons.fileupload.FileUploadException; 
import org.apache.commons.fileupload.disk.DiskFileItemFactory; 
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cl.bbr.boc.dto.ClienteDTO;
import cl.bbr.boc.dto.ProductoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CargarIndexacionBoletas extends Command {

	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,	UserDTO usr) throws Exception {		
		try {						
			logger.info("Inicio de CargarIndexacionBoletas");
			logger.debug("User: " + usr.getLogin());
			
			int resultado = 0;
			IValueSet top = new ValueSet();
			String mensaje = "";
			FileItem  fileItem = null;
			ITemplate tem = null;
			View salida = null;
			List listaClones = null;
			try{								
				salida = new View(res);				
				
				String html = getServletConfig().getInitParameter("TplFile");
				html = path_html + html;
				logger.debug("Template: " + html);
				
				TemplateLoader load = new TemplateLoader(html);
				tem = load.getTemplate();
				
				top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
				Date now = new Date();
				top.setVariable("{hdr_fecha}"	,now.toString());	
				
				fileItem = upFileMemory(req);		
				InputStream  inputStream = fileItem.getInputStream();		
				POIFSFileSystem fs = new POIFSFileSystem( inputStream );
				HSSFWorkbook wb = new HSSFWorkbook(fs);			
				
				listaClones = leerExcel(inputStream, wb);
										
				if(listaClones != null && listaClones.size()>0){
					resultado = 1;
				} else {
					resultado = 0;
				}
													
			} catch (Exception e) {
				mensaje = e.getMessage();
			}
			
			if(resultado == 1) {
				String fileName = fileItem.getName().substring(0, fileItem.getName().lastIndexOf('.'));
				String prefijo = fileName.substring(0,3).toLowerCase();
				
				if(!prefijo.equals("ibc")){					
					fileName = "IBC_"+fileName;
				}
				DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");	
				try {
					String dateTime = fileName.substring(fileName.length()-14, fileName.length());					 
					
					df.parse(dateTime);								 	
				 } catch (ParseException e) {					
					 fileName = fileName+"_"+df.format(new Date());
				 } catch (Exception e) {
					 fileName = fileName+"_"+df.format(new Date());
				}
				
				File saveFile = new File(ResourceBundle.getBundle("bo").getString("PATH_CLONES")+fileName+".xls");				
				saveFile.createNewFile();
				fileItem.write(saveFile);

				top.setVariable("{resultado}", " Archivo, cargado exitosamente!");
			}else {
				top.setVariable("{resultado}", " Ocurrio un error en la carga ! Favor verificar archivo, " + mensaje);
			}
			listaClones = null;
			String result = tem.toString(top);			
			salida.setHtmlOut(result);
			salida.Output();	
		
		} catch (Exception e) {
			logger.error("Error: ", e);
			res.sendRedirect("ViewError?mensaje=Ocurrio un error en el proceso de carga de indexacion boletas clientes");
		}		
	}
	
	private FileItem upFileMemory(HttpServletRequest request) throws FileUploadException, IOException, Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			List  items = upload.parseRequest(request);
			
			Iterator iter = items.iterator(); 
			while (iter.hasNext()) { 
				FileItem item = (FileItem) iter.next();
	
				if (!item.isFormField()){ 
                    return item;
				}
			}
		}
		return null;
	}
	
	private List leerExcel(InputStream input, HSSFWorkbook wb) throws IOException, Exception{
		logger.info("Inicio de Lectura XLS");
		
		ClienteDTO clienteDTO = null;
	
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator rowSheetIter = sheet.iterator();
		List listaClientes = new ArrayList();
		
		String[] arrayCabezeras = getCabezeras(wb, 0, 21);
		
		if(arrayCabezeras.length==21){

			while (rowSheetIter.hasNext()) {
				HSSFRow rowSheet = (HSSFRow) rowSheetIter.next();
				HSSFCell cellSheet = rowSheet.getCell(0);

				if (sheet.getRow(rowSheet.getRowNum()).getRowNum() > 0) {
					if(null==cellSheet){
						throw new Exception ("ERROR, al leer Celda en Hoja ["+wb.getSheetName(1)+"], Fila["+(rowSheet.getRowNum()+1)+"].");
					}
					if(cellSheet.getRowIndex()!=0){
						clienteDTO = new ClienteDTO();
						clienteDTO = clienteDTO.crearDTOCliente(clienteDTO, rowSheet, arrayCabezeras, wb.getSheetName(0));
						clienteDTO.setListaProducto( getMapClientesSku(wb) );						
						listaClientes.add( clienteDTO );
					}
				}
			}
		}else{
			throw new Exception ("Error total de Cabezeras en Hoja ["+wb.getSheetName(0)+"].");
		}
		return listaClientes;
	}
	
	private static List getMapClientesSku(HSSFWorkbook wb) throws Exception {
		ProductoDTO productoDTO = null;		
		List listClienteSku =  new ArrayList();

		HSSFSheet sheet = wb.getSheetAt(1);
		Iterator rowSheetIter = sheet.iterator();

		String[] arrayCabezeras = getCabezeras(wb, 1, 6);
		if(arrayCabezeras.length==6){
			while (rowSheetIter.hasNext()) {				
				HSSFRow rowSheet = (HSSFRow) rowSheetIter.next();
				HSSFCell cellSheet = rowSheet.getCell(0);

				if (sheet.getRow(rowSheet.getRowNum()).getRowNum() > 0) {
					if(null==cellSheet){
						throw new Exception ("ERROR, al leer Celda en Hoja ["+wb.getSheetName(1)+"], Fila["+(rowSheet.getRowNum()+1)+"].");
					}

					if(cellSheet.getRowIndex()!=0){
						productoDTO = new ProductoDTO();
						listClienteSku.add( productoDTO.crearObjetoClientesSku(productoDTO, rowSheet, arrayCabezeras, wb.getSheetName(1)) );
					}
				}
			}
		}else{
			throw new Exception ("Error total de Cabezeras en Hoja ["+wb.getSheetName(1)+"].");
		}
		return listClienteSku;
	}
	
	private static String[] getCabezeras(HSSFWorkbook wb, int hoja, int largoArray) throws Exception{		
		HSSFSheet sheet = wb.getSheetAt( hoja );
		int noOfColumns =  sheet.getRow(0).getPhysicalNumberOfCells();
		Iterator rowSheetIter = sheet.iterator();		
		String[] arrayCabezeras = new String[ noOfColumns ];
		while (rowSheetIter.hasNext()) {
			HSSFRow rowSheet = (HSSFRow) rowSheetIter.next();						
			Iterator cells = rowSheet.cellIterator();
			try{
				if (sheet.getRow(rowSheet.getRowNum()).getRowNum() == 0) {
					int index = 0;
					while (cells.hasNext()) {
						HSSFCell cell = (HSSFCell) cells.next();
						arrayCabezeras[index] = cell.getRichStringCellValue().toString();
						index++;
					}
				}
				break;
			}catch(Exception e){
				throw new Exception ("Error al leer Cabezeras en Hoja ["+wb.getSheetName(hoja)+"].");
			}
		}
		return arrayCabezeras;
	}
}
