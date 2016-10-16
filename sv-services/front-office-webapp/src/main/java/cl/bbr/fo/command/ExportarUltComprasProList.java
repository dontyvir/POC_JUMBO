package cl.bbr.fo.command;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasProductosDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;


/**
 * Exporta a excel las ultimas compras
 * 
 * @author imoyano
 * 
 */
public class ExportarUltComprasProList extends Command {

	protected void execute(HttpServletRequest req, 
	        HttpServletResponse res) throws Exception {
	    this.getLogger().debug( "Inicio ExportarUltComprasProList" );
	    try {
            HSSFWorkbook objWB = planillaExcel(req);
            
            ResourceBundle rb = ResourceBundle.getBundle("fo");
            ServletContext context = getServletConfig().getServletContext();
            
            String rutaUpload = rb.getString("conf.dir.tmp_listas") + "/tmp_listas/";
            File directorioDestino = new File(rutaUpload);
            EventosUtil.eliminarArchivosAntiguos(directorioDestino);
            File archivoNuevo = File.createTempFile("lista", ".xls", directorioDestino);
            FileOutputStream out = new FileOutputStream(archivoNuevo);
            objWB.write(out);            
            out.close();            
            
            res.setHeader("Expires", "0");
            res.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            res.setHeader("Pragma", "public");

            res.setHeader("Pragma", "no-cache"); //HTTP 1.0
            res.setDateHeader("Expires", 0); //prevents caching at the proxy server
            res.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
            res.setHeader("Cache-Control", "max-age=0");
                        
            res.setHeader("Content-Disposition", "attachment;filename=listaJumboCL_" + Utils.fechaActualFO("ddMMyy")  + ".xls");
            res.setContentType("application/x-filedownload");
            
            context.getRequestDispatcher("/tmp_listas/"+archivoNuevo.getName()).forward(req,res);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().debug( "Fin ExportarUltComprasProList" );
	}	
	
	private HSSFWorkbook planillaExcel(HttpServletRequest req) throws IOException {
        
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        
	    //Fila del doc
	    int nroFila = 0;
        
        //creamos el libro
        HSSFWorkbook objWB = new HSSFWorkbook();
        
        //creamos hoja
        HSSFSheet hoja1 = objWB.createSheet("hoja 1");
        
        HSSFRow fila = hoja1.createRow((short) nroFila);
        fila.setHeight((short) (206*9));
        HSSFCell celda = fila.createCell((short) 0);
        celda.setCellType(HSSFCell.CELL_TYPE_STRING);
        hoja1.addMergedRegion(new Region(nroFila,(short)0 ,nroFila,(short)4)); //FILA-INI: ;COL-INI; FILA-FIN: ;COL-FIN
        celda.setCellValue( new HSSFRichTextString("") );      

        //creamos una fila para el logo
        String imagen = rb.getString("conf.dir.html") + "/img/jumbo_lista.jpg";
        HSSFPatriarch patriarch = hoja1.createDrawingPatriarch();
        HSSFClientAnchor anchor;     
        anchor = new HSSFClientAnchor(0,0,0,0,(short)0,0,(short)5,1);
        //(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2)
        //dx1 - the x coordinate within the first cell.
		//dy1 - the y coordinate within the first cell.
		//dx2 - the x coordinate within the second cell.
		//dy2 - the y coordinate within the second cell.
		//col1 - the column (0 based) of the first cell.
		//row1 - the row (0 based) of the first cell.
		//col2 - the column (0 based) of the second cell.
		//row2 - the row (0 based) of the second cell.
        
        //anchor.setAnchorType( 2 );
        patriarch.createPicture( anchor, loadPicture( imagen, objWB ));

        nroFila++;
        
        //creamos una fila
        fila = hoja1.createRow((short) nroFila);
        nroFila++;
        //Seteamos estilos para la planilla
        HSSFFont fuente = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_BOLD, (short)17);
        HSSFCellStyle estiloCelda = estiloCelda(objWB, fuente);
        
        //creamos celda
        celda = fila.createCell((short) 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Código"));
        
        celda = fila.createCell((short) 1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Producto"));
        
        celda = fila.createCell((short) 2);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Cantidad"));
        
        celda = fila.createCell((short) 3);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Precio Unitario ($)"));
        
        celda = fila.createCell((short) 4);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Unidad"));
        
        hoja1.setColumnWidth((short)0, (short) (256*9) );
        hoja1.setColumnWidth((short)1, (short) (256*50) );
        hoja1.setColumnWidth((short)2, (short) (256*10) );
        hoja1.setColumnWidth((short)3, (short) (256*15) );
        hoja1.setColumnWidth((short)4, (short) (256*10) );
        
        try {			
			if (req.getParameter("tupla") != null) {

				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();

				String tupla = req.getParameter("tupla");
				this.getLogger().debug( "Listas de compras:"+tupla );
				
				// Recupera la sesión del usuario
				HttpSession session = req.getSession();

				UltimasComprasProductosDTO producto = null;
				List datos = biz.clientesGetUltComprasCategoriasProductos(tupla, session.getAttribute("ses_loc_id").toString(), Long.parseLong(session.getAttribute("ses_cli_id").toString()), Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
				
				for (int j = 0; j < datos.size(); j++) {
					UltimasComprasCategoriasDTO cat = (UltimasComprasCategoriasDTO) datos.get(j);

					//Escribimos la categoría
					HSSFFont fuenteCategoria = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_BOLD, (short)53); //8 es negro
			        HSSFCellStyle estiloCeldaCategoria = estiloCelda(objWB, fuenteCategoria, HSSFCellStyle.ALIGN_CENTER);
					
		            fila = hoja1.createRow((short) (nroFila));
		            
		            celda = fila.createCell((short) 0);
		            celda.setCellStyle(estiloCeldaCategoria);
		            celda.setCellValue(new HSSFRichTextString(cat.getCategoria()));
		            
		            hoja1.addMergedRegion(new Region(nroFila,(short)0 ,nroFila,(short)4)); //FILA-INI: ;COL-INI; FILA-FIN: ;COL-FIN
		            nroFila++;
		            
					List prod = cat.getUltimasComprasProductosDTO();

					for (int i = 0; i < prod.size(); i++) {
						producto = (UltimasComprasProductosDTO) prod.get(i);
						
						// Escribimos el producto
						HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, (short)8);
				        HSSFCellStyle estiloCeldaNormalLeft = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_LEFT);
				        HSSFCellStyle estiloCeldaNormalRight = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_RIGHT);
						
			            fila = hoja1.createRow((short) (nroFila));
						
						//cambia la cantidad de manera que sea acorde con el intervalo al momento del despliegue
						double mod = producto.getCantidad()%producto.getInter_valor();
						if( mod > 0 ) {
							double div = producto.getCantidad() - mod + producto.getInter_valor();
							producto.setCantidad(div);
						} else {
							producto.setCantidad(producto.getCantidad());
						}					
                        
                        celda = fila.createCell((short) 0);
                        celda.setCellStyle(estiloCeldaNormalLeft);
                        celda.setCellValue(new HSSFRichTextString(producto.getCodigo()));
                        
                        celda = fila.createCell((short) 1);
                        celda.setCellStyle(estiloCeldaNormalLeft);
                        if (producto.getStock() != 0) {
                            celda.setCellValue(new HSSFRichTextString(producto.getNombre() + " - " + producto.getMarca()));
                        } else {
                            celda.setCellValue(new HSSFRichTextString(producto.getNombre() + " - " + producto.getMarca() + " - (No disponible)" ));
                        }
                        
						celda = fila.createCell((short) 2);
                        celda.setCellStyle(estiloCeldaNormalLeft);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        //celda.setCellValue(new HSSFRichTextString(Formatos.formatoIntervalo(producto.getCantidad()).replace('.',',')));
                        celda.setCellValue(producto.getCantidad());
						
			            celda = fila.createCell((short) 3);
			            celda.setCellStyle(estiloCeldaNormalRight);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			            if (producto.getEsParticionable().equalsIgnoreCase("S")) {
			                celda.setCellValue( producto.getPrecio() / producto.getParticion() );
			            } else {
			                celda.setCellValue( producto.getPrecio() );
			            }
                        
                        celda = fila.createCell((short) 4);
                        celda.setCellStyle(estiloCeldaNormalRight);
                        celda.setCellType(HSSFCell.CELL_TYPE_STRING);
                        if (producto.getEsParticionable().equalsIgnoreCase("S")) {
                            celda.setCellValue(new HSSFRichTextString(" 1/" + producto.getParticion() + " " + producto.getTipre()));
                        } else {
                            celda.setCellValue(new HSSFRichTextString(producto.getTipre()));
                        }
                        
			            nroFila++;
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
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
        estiloCelda.setBorderBottom((short) 1);
        estiloCelda.setBorderLeft((short) 1);
        estiloCelda.setBorderRight((short) 1);
        estiloCelda.setBorderTop((short) 1);
        return estiloCelda;
    }
    
    /**
     * @return
     */
    private HSSFCellStyle estiloCelda(HSSFWorkbook objWB, HSSFFont fuente, short align) {
        HSSFCellStyle estiloCelda = estiloCelda(objWB, fuente);
        estiloCelda.setAlignment(align);
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
    
    private int loadPicture( String path, HSSFWorkbook wb ) throws IOException {
        int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream( path);
            bos = new ByteArrayOutputStream( );
            int c;
            while ( (c = fis.read()) != -1)
                bos.write( c );
            pictureIndex = wb.addPicture(bos.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG);
        }
        finally {
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
        }
        return pictureIndex;
    } 
	
}