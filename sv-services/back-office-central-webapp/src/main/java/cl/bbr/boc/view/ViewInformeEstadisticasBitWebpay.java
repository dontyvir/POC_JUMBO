package cl.bbr.boc.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author rriffog
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ViewInformeEstadisticasBitWebpay extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ViewInformeEstadisticas Execute");
        String fcIni = "";
        String fcFin = "";
        String tipo = "";
        long tipoBit = 0;
        
        if (req.getParameter("tipo") != null) {
            tipo = req.getParameter("tipo").toString();
        }
        if (req.getParameter("fc_ini") != null) {
            fcIni = req.getParameter("fc_ini").toString();
        }
        
        if (req.getParameter("fc_fin") != null) {
            fcFin = req.getParameter("fc_fin").toString();
        }
        
        if (req.getParameter("bitacora") != null) {
             tipoBit = Long.parseLong( req.getParameter("bitacora").toString() );
        }     
        
        View salida = new View(res);
        String html = path_html + getServletConfig().getInitParameter("TplFile"); 
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();      
    
        if ("W".equalsIgnoreCase(tipo) ) {         
        
          if(!req.getParameter("fc_ini").equalsIgnoreCase("") && !req.getParameter("fc_fin").equalsIgnoreCase("")){
            HashSet tiposPermitidos = new HashSet(); 
            long tiposPermitidosMonto = 0;          
            List okSinreintentos = new ArrayList();
            long okSinreintentosMonto = 0;
            List okConReintentos = new ArrayList();
            long okConReintentosMonto = 0;          
            List rechazoFinal =  new ArrayList();
            long rechazoFinalMonto = 0;            
            
            List lista = new ArrayList();
            List lista2 = new ArrayList();
            lista2.add("0000:0000:000000:000:0:0000");
            List lista3 = new ArrayList();            
           
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Calendar hoy = Calendar.getInstance();       
            Date fechaFinal = formato.parse(fcFin);
            Date fechaIni = formato.parse(fcIni);
            Date prueba = formato.parse(fcIni);             
            long time = fechaFinal.getTime() - fechaIni.getTime();
            int dias = (int)(time/(3600*24*1000));
            String [][] values = new String[10000][];
            
            for(int j = 0; j<= dias ;j++){
               hoy.setTime(prueba);
               hoy.getTime();
               hoy.add(Calendar.DATE,j);
               formato.format(hoy.getTime());
               String f = formato.format(hoy.getTime()).toString();
               String diaIni = f.substring(0,2);
               String mesIni = f.substring(3,5);
               try{
               values = cargarBitacora("tbk_bitacora_TR_NORMAL_"+mesIni+diaIni+".log",tipoBit );           
               for (int i = 0; i < values.length; i++) {
                   String tipoError = values[i][0].toString();
                   
                  if(!tipoError.equalsIgnoreCase("ERR")) {
                   String codigotarjeta = values[i][7].substring(25,values[i][7].length());  
                   String respuestaTran = values[i][4].substring(14,values[i][4].length());
                    if(respuestaTran.equalsIgnoreCase("0")){
                        respuestaTran = "00";
                    }
                   String fechaTran = values[i][9].substring(22,values[i][9].length());
                   String hora = values[i][10].substring(21,values[i][10].length());
                   String monto = values[i][5].substring(10,values[i][5].length());//Long.parseLong()
                   String montoOkSinreintentos = "";
                   
                   lista.add(codigotarjeta+":"+fechaTran+":"+hora+":"+monto);            
                   lista2.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);              
                   lista3.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);
                                     
                  } else{                      
                      String codigotarjeta = values[i][6].substring(25,values[i][6].length());  
                      String respuestaTran = values[i][3].substring(14,values[i][3].length());
                      if(respuestaTran.equalsIgnoreCase("0")){
                          respuestaTran = "00";
                      }
                      String fechaTran = values[i][8].substring(22,values[i][8].length());
                      String hora = values[i][9].substring(21,values[i][9].length());
                      String monto = values[i][4].substring(10,values[i][4].length());//Long.parseLong()
                      String montoOkSinreintentos = "";                    
                      lista.add(codigotarjeta+":"+fechaTran+":"+hora+":"+monto);            
                      lista2.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);              
                      lista3.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);
                  }
              }
               
           }catch(Exception e){
               logger.debug("no existe bitacora "+e);              
           }
               
         } //for grande      
            Collections.sort(lista);
            Collections.sort(lista2); 
            Collections.sort(lista3);          
            lista.add("0000:0000:000000:0000");
            lista3.add("0000:0000:000000:000:0:0000");           
           
            for (int i = 0; i < lista2.size(); i++) { 
                 String r = lista2.get(i).toString();
                 String s = lista3.get(i).toString();
                 String t = lista.get(i).toString();
                            
                   if(!s.substring(0,4).equals(r.substring(0,4)) ){
                      logger.debug("tiposPermitidos :"+r+" monto "+r.substring(24,r.length())); 
                       tiposPermitidos.add(r);                     
                       tiposPermitidosMonto += Long.parseLong(s.substring(24,s.length()));
                   }               
                  if(!s.substring(0,4).equals(r.substring(0,4)) && s.substring(17,20).equalsIgnoreCase("ACK") && s.substring(21,23).equalsIgnoreCase("00") ){
                    //logger.debug("okSinreintentos :"+s+" monto "+s.substring(24,s.length()));
                      okSinreintentos.add(r);                      
                      okSinreintentosMonto += Long.parseLong(s.substring(24,s.length())); 
                   }
                  if(s.substring(0,4).equals(r.substring(0,4)) && r.substring(21,23).equals("-1") && s.substring(21,23).equals("00")){
                     // logger.debug("okSinreintentos :"+s+" monto "+s.substring(24,s.length()));
                      okConReintentos.add(r);                   
                      okConReintentosMonto += Long.parseLong(s.substring(24,s.length()));
                   }
                  
                  if((!s.substring(0,4).equals(r.substring(0,4)) && r.substring(17,20).equalsIgnoreCase("ACK")) && !r.substring(21,23).equals("00")
|| ( !s.substring(0,4).equals(r.substring(0,4)) && !r.substring(17,20).equalsIgnoreCase("ACK") &&  r.substring(21,23).equals("00"))){
                   //logger.debug("okSinreintentos :"+r+" monto "+r.substring(24,r.length()));
                      rechazoFinal.add(r);                  
                      rechazoFinalMonto += Long.parseLong(r.substring(24,r.length()));              
                  }     
                     
               } //fin for
            
            ArrayList datos = new ArrayList();
            List titulos = new ArrayList();
            titulos.add("Clientes únicos Totales");
            titulos.add("Clientes únicos OK sin Reintentos");
            titulos.add("Clientes únicos OK con Reintentos");
            titulos.add("Clientes únicos con Rechazo final");
            List detalle = new ArrayList();
            detalle.add(String.valueOf(tiposPermitidos.size()-1));
            detalle.add(String.valueOf(okSinreintentos.size()));
            detalle.add(String.valueOf(okConReintentos.size()));
            detalle.add(String.valueOf(rechazoFinal.size()));
            
            List detalle2 = new ArrayList();
            detalle2.add(Formatos.formatoPrecio(tiposPermitidosMonto));
            detalle2.add(Formatos.formatoPrecio(okSinreintentosMonto));
            detalle2.add(Formatos.formatoPrecio(okConReintentosMonto));
            detalle2.add(Formatos.formatoPrecio(rechazoFinalMonto));
            for (int t = 0; t <= 3; t++) {         
                IValueSet fila = new ValueSet();
                fila.setVariable("{titulos}" ,titulos.get(t).toString());
                fila.setVariable("{cantidad}" ,detalle.get(t).toString());
                fila.setVariable("{monto}" ,detalle2.get(t).toString());               
                datos.add(fila);            
            }
            /*  ArrayList datosBit = new ArrayList();
            IValueSet fila2 = new ValueSet();
            if (tipoBit == 1 ) {
                fila2.setVariable("{sel_est1}", "selected");
            
            }else if(tipoBit == 2){
                fila2.setVariable("{sel_est2}", "selected");                
            }else {
                fila2.setVariable("{sel_est}", "");
            }
            datosBit.add(fila2);*/
           // top.setDynamicValueSets("BITACORA", datosBit);
            top.setDynamicValueSets("INFORME", datos);
          } else{
              
          }
          
            
      }else if("P".equalsIgnoreCase(tipo)){
         
          try {
              HSSFWorkbook objWB = planillaExcel(fcIni,fcFin,tipo,tipoBit);           
              
              ServletContext context = getServletConfig().getServletContext();
              String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
              File directorioDestino = new File(rutaUpload);
              EventosUtil.eliminarArchivosAntiguos(directorioDestino);
              
              File archivoNuevo = File.createTempFile("modificacion_precios", ".xls", directorioDestino);
              
              FileOutputStream out = new FileOutputStream(archivoNuevo);
              objWB.write(out);            
              out.close();
              
              res.setContentType("application/x-filedownload");
              res.setHeader("Content-Disposition", "attachment;filename=" + Utils.getFechaActualByPatron("ddMMyyyy")  + "informe_estadistica_bitacora.xls");
       
              RequestDispatcher rd = context.getRequestDispatcher("/upload_ruts_eventos/"+archivoNuevo.getName());
              rd.forward(req,res);

          } catch (Exception e) {
              e.printStackTrace();
         } 
          
      }//fin if grande

     top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
     Date now = new Date();
     top.setVariable("{hdr_fecha}", now.toString()); 
     top.setVariable("{fc_ini}",fcIni);
     top.setVariable("{fc_fin}",fcFin);
     
     logger.debug("Fin ViewInformeModPrecios Execute");
     String result = tem.toString(top);
     salida.setHtmlOut(result);
     salida.Output();
     
   } 
    
    
    public static String[][] cargarBitacora(String archivo, long tipoBit) throws IOException {
        BufferedReader br;         
        if(tipoBit == 1){
        br = new BufferedReader(new FileReader("C:\\Documents and Settings\\rriffog\\Escritorio\\todos log\\" + archivo));
         }else{
        br = new BufferedReader(new FileReader("C:\\Documents and Settings\\rriffog\\Escritorio\\todos log\\fono\\" + archivo));
       } 
         String linea = null;
        List lista = new ArrayList();
        while ((linea = br.readLine()) != null) {
            String col[] = linea.split(";");
            List columna = new ArrayList();            
            for (int j = 0; j < col.length; j++) {
                col[j] = col[j].trim();                   
                
                columna.add(col[j]);
            }
            lista.add(columna);                      
        }
        String[][] datos = new String[lista.size()][];
        for (int i = 0; i < lista.size(); i++) {                
            datos[i] = (String[]) ((List) lista.get(i)).toArray(new String[((ArrayList) lista.get(i)).size()]);
        }
        
        br.close();
      
        return datos;
    }
    
    
    /**
     * @param informes
     * @return
     * @throws ParseException
     */
    private HSSFWorkbook planillaExcel(String fcIni,String fcFin,String tipo,long tipoBit) throws ParseException {
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
        celda.setCellValue(new HSSFRichTextString(" ")); 
        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Cantidad Clientes"));     
        
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda);
        celda.setCellValue(new HSSFRichTextString("Monto ($)"));    
       
        
        HSSFFont fuenteNormal = estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL, HSSFColor.BLACK.index);
        HSSFCellStyle estiloCeldaNormal = estiloCelda(objWB, fuenteNormal, HSSFCellStyle.ALIGN_RIGHT);        
        HashSet tiposPermitidos = new HashSet(); 
        long tiposPermitidosMonto = 0;          
        List okSinreintentos = new ArrayList();
        long okSinreintentosMonto = 0;
        List okConReintentos = new ArrayList();
        long okConReintentosMonto = 0;          
        List rechazoFinal =  new ArrayList();
        long rechazoFinalMonto = 0;            
        
        List lista = new ArrayList();
        List lista2 = new ArrayList();
        lista2.add("0000:0000:000000:000:0:0000");
        List lista3 = new ArrayList();            
       
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoy = Calendar.getInstance();       
        Date fechaFinal = formato.parse(fcFin);
        Date fechaIni = formato.parse(fcIni);
        Date prueba = formato.parse(fcIni);             
        long time = fechaFinal.getTime() - fechaIni.getTime();
        int dias = (int)(time/(3600*24*1000));
        String [][] values = new String[10000][];
        
        for(int j = 0; j<= dias ;j++){
           hoy.setTime(prueba);
           hoy.getTime();
           hoy.add(Calendar.DATE,j);
           formato.format(hoy.getTime());
           String f = formato.format(hoy.getTime()).toString();
           String diaIni = f.substring(0,2);
           String mesIni = f.substring(3,5);
           try{
           values = cargarBitacora("tbk_bitacora_TR_NORMAL_"+mesIni+diaIni+".log",tipoBit );           
           for (int i = 0; i < values.length; i++) {
               String tipoError = values[i][0].toString();
               
              if(!tipoError.equalsIgnoreCase("ERR")) {
               String codigotarjeta = values[i][7].substring(25,values[i][7].length());  
               String respuestaTran = values[i][4].substring(14,values[i][4].length());
                if(respuestaTran.equalsIgnoreCase("0")){
                    respuestaTran = "00";
                }
               String fechaTran = values[i][9].substring(22,values[i][9].length());
               String hora = values[i][10].substring(21,values[i][10].length());
               String monto = values[i][5].substring(10,values[i][5].length());//Long.parseLong()
               String montoOkSinreintentos = "";
               
               lista.add(codigotarjeta+":"+fechaTran+":"+hora+":"+monto);            
               lista2.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);              
               lista3.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);
                                 
              } else{                      
                  String codigotarjeta = values[i][6].substring(25,values[i][6].length());  
                  String respuestaTran = values[i][3].substring(14,values[i][3].length());
                  if(respuestaTran.equalsIgnoreCase("0")){
                      respuestaTran = "00";
                  }
                  String fechaTran = values[i][8].substring(22,values[i][8].length());
                  String hora = values[i][9].substring(21,values[i][9].length());
                  String monto = values[i][4].substring(10,values[i][4].length());//Long.parseLong()
                  String montoOkSinreintentos = "";                    
                  lista.add(codigotarjeta+":"+fechaTran+":"+hora+":"+monto);            
                  lista2.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);              
                  lista3.add(codigotarjeta+":"+fechaTran+":"+hora+":"+tipoError+":"+respuestaTran+":"+monto);
              }
          }
           
       }catch(Exception e){
           logger.debug("no existe bitacora "+e);              
       }
           
     } //for grande      
        Collections.sort(lista);
        Collections.sort(lista2); 
        Collections.sort(lista3);          
        lista.add("0000:0000:000000:0000");
        lista3.add("0000:0000:000000:000:0:0000");           
       
        for (int i = 0; i < lista2.size(); i++) { 
             String r = lista2.get(i).toString();
             String s = lista3.get(i).toString();
             String t = lista.get(i).toString();
                        
               if(!s.substring(0,4).equals(r.substring(0,4)) ){
                  logger.debug("tiposPermitidos :"+r+" monto "+r.substring(24,r.length())); 
                   tiposPermitidos.add(r);                     
                   tiposPermitidosMonto += Long.parseLong(s.substring(24,s.length()));
               }               
              if(!s.substring(0,4).equals(r.substring(0,4)) && s.substring(17,20).equalsIgnoreCase("ACK") && s.substring(21,23).equalsIgnoreCase("00") ){
                //logger.debug("okSinreintentos :"+s+" monto "+s.substring(24,s.length()));
                  okSinreintentos.add(r);                      
                  okSinreintentosMonto += Long.parseLong(s.substring(24,s.length())); 
               }
              if(s.substring(0,4).equals(r.substring(0,4)) && r.substring(21,23).equals("-1") && s.substring(21,23).equals("00")){
                 // logger.debug("okSinreintentos :"+s+" monto "+s.substring(24,s.length()));
                  okConReintentos.add(r);                   
                  okConReintentosMonto += Long.parseLong(s.substring(24,s.length()));
               }
              
              if((!s.substring(0,4).equals(r.substring(0,4)) && r.substring(17,20).equalsIgnoreCase("ACK")) && !r.substring(21,23).equals("00")
|| ( !s.substring(0,4).equals(r.substring(0,4)) && !r.substring(17,20).equalsIgnoreCase("ACK") &&  r.substring(21,23).equals("00"))){
               //logger.debug("okSinreintentos :"+r+" monto "+r.substring(24,r.length()));
                  rechazoFinal.add(r);                  
                  rechazoFinalMonto += Long.parseLong(r.substring(24,r.length()));              
              }     
                 
           } //fin for
        
        ArrayList datos = new ArrayList();
        List titulos = new ArrayList();
        titulos.add("Clientes únicos Totales");
        titulos.add("Clientes únicos OK sin Reintentos");
        titulos.add("Clientes únicos OK con Reintentos");
        titulos.add("Clientes únicos con Rechazo final");
        List detalle = new ArrayList();
        detalle.add(String.valueOf(tiposPermitidos.size()-1));
        detalle.add(String.valueOf(okSinreintentos.size()));
        detalle.add(String.valueOf(okConReintentos.size()));
        detalle.add(String.valueOf(rechazoFinal.size()));
        
        List detalle2 = new ArrayList();
        detalle2.add(Formatos.formatoPrecio(tiposPermitidosMonto));
        detalle2.add(Formatos.formatoPrecio(okSinreintentosMonto));
        detalle2.add(Formatos.formatoPrecio(okConReintentosMonto));
        detalle2.add(Formatos.formatoPrecio(rechazoFinalMonto));           
            for (int t = 0; t <= 3; t++) {                    
          
            fila = hoja1.createRow((short) (t+1)); 
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(titulos.get(t).toString()));
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(detalle.get(t).toString()));
            celda = fila.createCell(2);  
            celda.setCellStyle(estiloCeldaNormal);
            celda.setCellValue(new HSSFRichTextString(detalle2.get(t).toString()));
           
            } 
       // }   
        
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
