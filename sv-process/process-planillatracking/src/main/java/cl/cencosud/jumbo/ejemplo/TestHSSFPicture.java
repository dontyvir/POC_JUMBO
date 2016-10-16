/*
 * Creado el 13-12-2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.cencosud.jumbo.ejemplo;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageReader;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Test <code>HSSFPicture</code>.
 *
 * @author Yegor Kozlov (yegor at apache.org)
 */
public class TestHSSFPicture extends TestCase{

    public static final int PICTURE_TYPE_EMF = HSSFWorkbook.PICTURE_TYPE_EMF;           
    // Windows Enhanced Metafile
    public static final int PICTURE_TYPE_WMF = HSSFWorkbook.PICTURE_TYPE_WMF;           
    // Windows Metafile
    public static final int PICTURE_TYPE_PICT = HSSFWorkbook.PICTURE_TYPE_PICT;         
    // Macintosh PICT
    public static final int PICTURE_TYPE_JPEG = HSSFWorkbook.PICTURE_TYPE_JPEG;         
    // JFIF
    public static final int PICTURE_TYPE_PNG = HSSFWorkbook.PICTURE_TYPE_PNG;           
    // PNG
    public static final int PICTURE_TYPE_DIB = HSSFWorkbook.PICTURE_TYPE_DIB;           
    // Windows DIB
 
    int pictureIndex;
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sh1 = wb.createSheet();
    HSSFPatriarch p1 = sh1.createDrawingPatriarch();

    private static final POILogger log = POILogFactory.getLogger(HSSFPicture.class);
    
    
    
    public void testResize() throws Exception {
        //HSSFWorkbook wb = new HSSFWorkbook();
        //HSSFSheet sh1 = wb.createSheet();
        //HSSFPatriarch p1 = sh1.createDrawingPatriarch();

        int idx1 = loadPicture( "d:/18288806.jpg", wb);
        HSSFPicture picture1 = p1.createPicture(new HSSFClientAnchor(), idx1);
        //HSSFClientAnchor anchor = picture1.getPreferredSize();
        HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,255,(short)2,2,(short)4,7);

        //assert against what would BiffViewer print if we insert the image in xls and dump the file
        assertEquals(0, anchor.getCol1());
        assertEquals(0, anchor.getRow1());
        assertEquals(1, anchor.getCol2());
        assertEquals(9, anchor.getRow2());
        assertEquals(0, anchor.getDx1());
        assertEquals(0, anchor.getDy1());
        assertEquals(848, anchor.getDx2());
        assertEquals(240, anchor.getDy2());
    }

    /**
     * Copied from org.apache.poi.hssf.usermodel.examples.OfficeDrawing
     */
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

    /**
     * Reset the image to the original size.
     *
     * @since POI 3.0.2
     */
    /*public void resize(){
        HSSFClientAnchor anchor = (HSSFClientAnchor)getAnchor();
        anchor.setAnchorType(2);

        HSSFClientAnchor pref = getPrefferedSize();

        int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
        int col2 = anchor.getCol1() + (pref.getCol2() - pref.getCol1());

        anchor.setCol2((short)col2);
        anchor.setDx1(0);
        anchor.setDx2(pref.getDx2());

        anchor.setRow2(row2);
        anchor.setDy1(0);
        anchor.setDy2(pref.getDy2());
    }*/
    
    /*public HSSFClientAnchor getPrefferedSize(){
        HSSFClientAnchor anchor = new HSSFClientAnchor();

        EscherBSERecord bse = (EscherBSERecord)p1.sheet.book.getBSERecord(pictureIndex);
        byte[] data = bse.getBlipRecord().getPicturedata();
        int type = bse.getBlipTypeWin32();
        switch (type){
            //we can calculate the preffered size only for JPEG and PNG
            //other formats like WMF, EMF and PICT are not supported in Java
            case HSSFWorkbook.PICTURE_TYPE_JPEG:
            case HSSFWorkbook.PICTURE_TYPE_PNG:
                BufferedImage img = null;
                ImageReader r = null;
                try {
                    //read the image using javax.imageio.*
                    ImageInputStream iis = ImageIO.createImageInputStream( new ByteArrayInputStream(data));
                    Iterator i = ImageIO.getImageReaders( iis );
                    r = (ImageReader) i.next();
                    r.setInput( iis );
                    img = r.read(0);

                    int[] dpi = getResolution(r);
                    int imgWidth = img.getWidth()*96/dpi[0];
                    int imgHeight = img.getHeight()*96/dpi[1];

                    //Excel measures cells in units of 1/256th of a character width.
                    //The cell width calculated based on this info is always "off".
                    //A better approach seems to be to use empirically obtained cell width and row height
                    int cellwidth = 64;
                    int rowheight = 17;

                    int col2 = imgWidth/cellwidth;
                    int row2 = imgHeight/rowheight;

                    int dx2 = (int)((float)(imgWidth % cellwidth)/cellwidth * 1024);
                    int dy2 = (int)((float)(imgHeight % rowheight)/rowheight * 256);

                    anchor.setCol2((short)col2);
                    anchor.setDx2(dx2);

                    anchor.setRow2(row2);
                    anchor.setDy2(dy2);

                } catch (IOException e){
                    //silently return if ImageIO failed to read the image
                    log.log(POILogger.WARN, e);
                    e.printStackTrace();
                    img = null;
                }

                break;
        }
        return anchor;
    }*/
    
    /**
    * The metadata of PNG and JPEG can contain the width of a pixel in millimeters.
    * Return the the "effective" dpi calculated as <code>25.4/HorizontalPixelSize</code>
    * and <code>25.4/VerticalPixelSize</code>.  Where 25.4 is the number of mm in inch.
    *
    * @return array of two elements: <code>{horisontalPdi, verticalDpi}</code>.
    * {96, 96} is the default.
    */
    protected int[] getResolution(ImageReader r) throws IOException {
        int hdpi=96, vdpi=96;
        double mm2inch = 25.4;

        NodeList lst;
        Element node = (Element)r.getImageMetadata(0).getAsTree("javax_imageio_1.0");
        lst = node.getElementsByTagName("HorizontalPixelSize");
        if(lst != null && lst.getLength() == 1) hdpi = (int)(mm2inch/Float.parseFloat(((Element)lst.item(0)).getAttribute("value")));
     
        lst = node.getElementsByTagName("VerticalPixelSize");
        if(lst != null && lst.getLength() == 1) vdpi = (int)(mm2inch/Float.parseFloat(((Element)lst.item(0)).getAttribute("value")));
        return new int[]{hdpi, vdpi};
    }
}
