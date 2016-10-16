/*
 * Creado el 13-12-2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.cencosud.jumbo.ejemplo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



/**
 * @author rbelmar
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class POI extends TestCase {

    public static void main(String[] args) {
		
        try{
            SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMdd_HHmmss");
            HSSFWorkbook wb = new HSSFWorkbook();
    		HSSFSheet sheet = wb.createSheet("test");
    		HSSFRow row0 = sheet.createRow((short) 0);


    	    // Create the drawing patriarch.  This is the top level container for
    	    // all shapes. This will clear out any existing shapes for that sheet.
    	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

    	    HSSFClientAnchor anchor;
    	    anchor = new HSSFClientAnchor(0,0,0,0,(short)1,2,(short)4,7);
    	    anchor.setAnchorType( 4 );
    	    patriarch.createPicture(anchor, loadPicture( "d:/18288806.jpg", wb ));
    	    
    	    /*assertEquals(0, anchor.getCol1());
            assertEquals(0, anchor.getRow1());
            assertEquals(1, anchor.getCol2());
            assertEquals(9, anchor.getRow2());
            assertEquals(0, anchor.getDx1());
            assertEquals(0, anchor.getDy1());
            assertEquals(848, anchor.getDx2());
            assertEquals(240, anchor.getDy2());*/
            
    	    /*a = new HSSFClientAnchor( 0, 0, 1023, 255, (short) 1, 0, (short) 1, 0 );
    	    group = patriarch.createGroup( a );
    	    group.setCoordinates( 0, 0, 80 * 4 , 12 * 23  );
    	    float verticalPointsPerPixel = a.getAnchorHeightInPoints(sheet) / (float)Math.abs(group.getY2() - group.getY1());
    	    g = new EscherGraphics( group, wb, Color.black, verticalPointsPerPixel );
    	    g2d = new EscherGraphics2d( g );
    	    drawChemicalStructure( g2d );*/

    	    
    	    String Fecha = dateFormat2.format(new Date());
    		String filename = "c:/TestPOI_" + Fecha + ".xls";
    		FileOutputStream fos = new FileOutputStream(filename);
    		wb.write(fos);
    		fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
		

    }
    
    private static int loadPicture( String path, HSSFWorkbook wb ) throws IOException
    {
        int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = new FileInputStream( path);
            bos = new ByteArrayOutputStream( );
            int c;
            while ( (c = fis.read()) != -1)
                bos.write( c );
            pictureIndex = wb.addPicture( bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG );
        }
        finally
        {
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
        }
        return pictureIndex;
    }

    private static HSSFPicture createPicture(HSSFWorkbook workbook,String sheetName,String imageFile,int dx1,int dy1,int dx2,int dy2,short col1,int row1,short col2,int row2){
        //Create the drawing patriarch.  This is the top level container for
        //all shapes. This will clear out any existing shapes for that sheet.
        HSSFPatriarch patriarch = workbook.getSheet(sheetName).createDrawingPatriarch();

        HSSFClientAnchor anchor;
        anchor = new HSSFClientAnchor(dx1,dy1,dx2,dy2,col1,row1,col2,row2);
        anchor.setAnchorType( 2 );

        try {
            return patriarch.createPicture(anchor, loadPicture( imageFile, workbook ));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
