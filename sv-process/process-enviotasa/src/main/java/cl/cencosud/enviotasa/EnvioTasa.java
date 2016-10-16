package cl.cencosud.enviotasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

import cl.cencosud.enviotasa.datos.EnvioMail;
import cl.cencosud.enviotasa.datos.Mail;
import cl.cencosud.enviotasa.datos.Pedido;
import cl.cencosud.enviotasa.datos.TraeInfo;
import cl.cencosud.enviotasa.util.Db;
import cl.cencosud.enviotasa.util.Util;


/**
 * @author imoyano
 */
public class EnvioTasa {
    
    public static final String PREFIX_JUMBO = "JM";
    public static final String PREFIX_EASY  = "EA";
    public static final int DIAS_ARCHIVO    = -2;

    public static void main(String[] args) throws Exception {        
        
        System.out.println("[" + Util.fecha() + "] Let's start the process to send the information about tax.");
        
        ResourceBundle rb = ResourceBundle.getBundle("enviotasa");
        
        if ( "SI".equalsIgnoreCase(rb.getString("jumbo.activo")) ) {
            System.out.println("\nThe JUMBO process is activated...");
            // ------------- Jumbo -------------
            // Read file from "boldo" and get this information organized by Pedido Objects
            List pedidosJumbo = readFile(rb,PREFIX_JUMBO);
            // Let's process these objects and we'll send the mails
            if ( pedidosJumbo != null && pedidosJumbo.size() > 0 )
                procesaPedidos(rb,pedidosJumbo,PREFIX_JUMBO);
        }
        
        if ( "SI".equalsIgnoreCase(rb.getString("easy.activo")) ) {
            System.out.println("\nThe EASY process is activated...");
            // ------------- Easy -------------
            // Read file from "boldo" and get this information organized by Pedido Objects
            List pedidosEasy = readFile(rb,PREFIX_EASY);
            //  Let's process these objects and we'll send the mails
            if ( pedidosEasy != null && pedidosEasy.size() > 0 )
                procesaPedidos(rb,pedidosEasy,PREFIX_EASY);
        }
        
        System.out.println("\n[" + Util.fecha() + "] The process has finished!\n");
    }

    private static void procesaPedidos(ResourceBundle rb, List pedidos, String negocio) {
        System.out.println("Let's process the objects...");
        try {            
            String prefix = "jumbo";
            if ( PREFIX_EASY.equalsIgnoreCase(negocio) ) {
                prefix = "easy";
            }
            String user = rb.getString(prefix + ".USER");
            String password = rb.getString(prefix + ".PASSWORD");
            String driver = rb.getString("DRIVER");
            String url = rb.getString(prefix + ".URL");
            Connection con = Db.conexion(user, password, driver, url);
            
            System.out.println("We're connected to BD " + url);
            
            String titulo    = rb.getString( prefix + ".titulo" );
            String template  = rb.getString( prefix + ".texto" );
            String remitente = rb.getString( prefix + ".remitente" );
            
            for ( int i=0; i < pedidos.size(); i++ ) {
                try {
                    Pedido ped = (Pedido) pedidos.get(i);
                    //Traemos la informacion que falta
                    if ( PREFIX_JUMBO.equalsIgnoreCase( negocio ) ) {
                        ped = TraeInfo.getInfoPedidoJumbo(con, ped);
                    } else {
                        ped = TraeInfo.getInfoPedidoEasy(con, ped);
                    }
                    
                    if ( ped.isExisteEnBD() ) {                    
                        //Completamos el html con los datos 
                        String body = template;
                        body = body.replaceAll("@tasa", ped.getTasaInteres());
                        
                        if ( Util.sinCuota(ped.getCuotas()) ) {
                            body = body.replaceAll("@cuotas", "Sin cuotas");
                            body = body.replaceAll("@valor_cuota", "&#36 " + Util.formatoNumero(ped.getMontoCaptura()) );
//                            body = body.replaceAll("@monto_total", "&#36 " + Util.formatoNumero(ped.getMontoCaptura()) );
                            
                        } else {
                            body = body.replaceAll("@cuotas", ped.getCuotas());
                            body = body.replaceAll("@valor_cuota", "&#36 " + Util.formatoNumero(ped.getValorCuota()));
//                            body = body.replaceAll("@monto_total", "&#36 " + Util.formatoNumero(ped.getMontoTotalConCuotas()));
                            
                        }
                        
//                        body = body.replaceAll("@fecha_primer_venc", Util.fechaAgregaSeparador(ped.getFechaPrimerVencimiento()));
                        body = body.replaceAll("@id_pedido", String.valueOf(ped.getIdPedido()));
                        body = body.replaceAll("@nombre_cliente", Util.capitalizeString(ped.getNombreCliente()));
                        body = body.replaceAll("@rut_cliente", Util.formatoNumero(ped.getRutCliente()) + "-" + ped.getDvCliente());
                        body = body.replaceAll("@mail_cliente", ped.getMailCliente());
                        body = body.replaceAll("@fecha_compra", Util.fechaOrdena(ped.getFechaCompra()));
                        body = body.replaceAll("@fecha_captura", Util.fechaOrdenaYAgrega( ped.getFechaCaptura()));
                        body = body.replaceAll("@tarjeta", Util.printNroTarjeta( ped.getTarjeta() ));
                        body = body.replaceAll("@monto_captura", "&#36 " + Util.formatoNumero(ped.getMontoCaptura()));
                        
                        body = body.replaceAll("@cliente_rut_mas", Util.printRutMas( ped.getRutClienteMas() ));
                        body = body.replaceAll("@local", ped.getLocal() );
                        
                        //Enviar el mail
                        Mail mail = new Mail();
                        mail.setRemitente(remitente);
                        
                        //PARA PRODUCCION
//                        mail.setDestinatario(ped.getMailCliente());
                        
                        //TEST LOCAL
                        mail.setDestinatario("patricia.herrerazumaran@cencosud.cl,maria.blanco@cencosud.cl,diego.cadiz@cencosud.cl,ivan.moyano@paris.cl");
                        
                        //TEST CENCOSUD
//                        mail.setDestinatario("maria.blanco@cencosud.cl,carolina.aros@cencosud.cl,beatriz.cabellos@paris.cl,patricia.herrerazumaran@cencosud.cl,lidia.reyes@paris.cl");
//                        mail.setCopia("Jackeline.TrivinoFuentes@cencosud.cl,ivan.moyano@paris.cl,cesar.tapia@paris.cl,araceli.pardo@cencosud.cl,diego.cadiz@cencosud.cl");
                                                
                        mail.setTitulo(titulo);
                        mail.setContenido(body);
                        
                        System.out.println("Saving information of OP " + ped.getIdPedido() + " to send mail to " + mail.getDestinatario());
                        EnvioMail.addMail(con,mail);
                    } else {
                        System.out.println("* Order " + ped.getIdPedido() + " doesn't exist in the BD!");
                    }
                    
                } catch (Exception e) {
                    System.out.println("* Error processing the information of the object " + (i+1) + " (" + negocio + ") *");
                    System.out.println(Util.getStackTrace(e));
                }                
            }            
            Db.close(con);
        } catch (Exception e) {
            System.out.println("* Error in the method procesaPedidos (" + negocio + ") *");
            System.out.println(Util.getStackTrace(e));
        }
    }
    
    public static List readFile(ResourceBundle rb, String negocio) {
        //-- VAMOS A BUSCAR EL ARCHIVO
        List pedidos = new ArrayList();
        try {
            File file = getFile(rb, negocio);
            if ( file != null ) {
                System.out.println("Reading the file: " + file.getName());
                String linea = null;
                int i = 0;
                FileReader entrada = new FileReader(file);
                BufferedReader br = new BufferedReader(entrada);
                while((linea = br.readLine()) != null) {
                    try {
                        i++;
                        Pedido p = new Pedido();
                        String idPed = linea.substring(18,34).trim();
                        if ( PREFIX_JUMBO.equalsIgnoreCase(negocio) ) {
                            idPed = idPed.substring(0, idPed.length()-2);
                        }
                        
                        p.setIdPedido( Long.parseLong(idPed) );
                        p.setTasaInteres( Integer.parseInt( linea.substring(123,125).trim() ) +","+linea.substring(125,127).trim());
                        p.setCuotas(linea.substring(114,116));                
                        p.setValorCuota( Double.parseDouble( linea.substring(116,123) ) );
                        p.setMontoTotalConCuotas( ( Integer.parseInt( p.getCuotas() ) * p.getValorCuota() ) );
                        p.setFechaPrimerVencimiento(linea.substring(127,135));
                        pedidos.add(p);
                        
                    } catch (Exception e) {
                        System.out.println("* Error getting information in the line: " + i + "(" + negocio + ") *");
                        System.out.println(Util.getStackTrace(e));
                    }                                
                }
            }
        } catch (IOException e) {
            System.out.println("* Error in the method readFile *");
            System.out.println(Util.getStackTrace(e));
        } catch (Exception e) {
            System.out.println("* Error in the method readFile *");
            System.out.println(Util.getStackTrace(e));
        }
        System.out.println("Total of found objects: " + pedidos.size() );
        return pedidos;
    }
    
    public static File getFile(ResourceBundle rb, String negocio) {
        System.out.println("First, looking for the file of : " + negocio);
        String catIp = rb.getString("cat.ip");
        String catCarpeta = rb.getString("cat.carpeta");
        String catUser = rb.getString("cat.user");
        String catPass = rb.getString("cat.pass");
        
        FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        FTPClient cliente = new FTPClient();
        cliente.configure(config);
        FileOutputStream fos = null;
        try {
            cliente.connect(catIp);
            cliente.login(catUser, catPass);
            cliente.changeWorkingDirectory("/");
            cliente.changeWorkingDirectory(catCarpeta);
            
            //se restan dos dias (dependiendo de la hora del proceso)
            String fechaArchivo = Util.fechaArchivo("yyyyMMdd",DIAS_ARCHIVO);            
            String fileName = "ITFPareo"+negocio+fechaArchivo+".TXT";
            System.out.println("We'll look for the file called : " + fileName);
            
            String[] ftpFile = cliente.listNames(fileName);
            
            if ( ftpFile == null ) {
                System.out.println("* The file '"+fileName+"' not found *");
                cliente.logout();
//                System.out.println("Fin del BATCH sin "+fileName+" encontrado");
//                System.out.println("Fin del batch sin "+fileName+" encontrado");
//                System.exit(0);
            } else {
                fos = new FileOutputStream(fileName);
                //System.out.println("Directorio actual: "+cliente.printWorkingDirectory());
                cliente.retrieveFile(fileName,fos);
                fos.close();
                cliente.disconnect();
                File file = new File(fileName);
                return file;
            }
        } catch (SocketException e) {
            System.out.println("* Error in the method getFile ("+negocio+") *");
            System.out.println(Util.getStackTrace(e));
        } catch (IOException e) {
            System.out.println("* Error in the method getFile ("+negocio+") *");
            System.out.println(Util.getStackTrace(e));
        }
        return null;        
    }
    
}
