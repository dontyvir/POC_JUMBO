/*
 * Created on 18-mar-2010
 */
package cl.cencosud.encuestas;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cl.cencosud.encuestas.datos.Mail;
import cl.cencosud.encuestas.datos.Pedido;
import cl.cencosud.util.Db;

/**
 * @author imoyano
 */
public class Encuestas {

    public static void main(String[] args) throws Exception {        
        ResourceBundle rb = ResourceBundle.getBundle("encuestas");
        String user = rb.getString("USER");
        String password = rb.getString("PASSWORD");
        String driver = rb.getString("DRIVER");
        String url = rb.getString("URL");
        String emailRemite = rb.getString("email");
        String tituloMail = rb.getString("titulo_mail");

        Connection con = Db.conexion(user, password, driver, url);

        CargaEncuesta enc = new CargaEncuesta();
        List pedidos = enc.getPedidosParaEncuestas(con);

        System.out.println("[" + fecha() + "] Cantidad de pedidos para envio de encuesta: " + pedidos.size());
        
        if ( pedidos.size() > 0 ) {        
            List mails = new ArrayList();
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido ped = (Pedido) pedidos.get(i);
                String html = Encuestas.llenarHtml(rb.getString("HTML"), rb.getString("link_" + ped.getMesPedido()), ped, strUrl( ped.getRutCliente(), rb.getString("url_elimina_suscripcion") ) );
                mails.add( new Mail(ped.getIdPedido(), emailRemite, ped.getEmailCliente(), tituloMail, html) ); 
            }
            
            String pedidosConEncuestaEnviada = enc.addMail(con, mails);
            
            if ( pedidosConEncuestaEnviada.length() > 0 ) {
                System.out.println("Encuesta enviada a los pedidos: " + pedidosConEncuestaEnviada);
                enc.modificaPedidoConEncuestaEnviada(con, pedidosConEncuestaEnviada);
            }            
        }        
        Db.close(con);
        System.out.println("[" + fecha() + "] Envio de encuesta Finalizado");
    }

    /**
     * @return
     */
    private static String fecha() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.util.Date date = new java.util.Date();
        String s = dateFormat.format(date);
        return s;
    }

    private static String strUrl(String rutCliente, String url) {
        String[] ram = {"O","2","4","T","1","5","8","Y","3","9","0","1","4","7","8","7"};
        char[] numeros = rutCliente.toCharArray();        
        for (int i=numeros.length-1; i >= 0; i--) {
            url += ram[i] + numeros[i];
        }
        return url;
    }
    
    private static String llenarHtml(String html, String link, Pedido pedido, String urlEliminaSuscripcion) {
        html = html.replaceAll("@link", link + pedido.getIdPedido());
        html = html.replaceAll("@nombre", pedido.getNombreCliente());
//        html = html.replaceAll("@direccion", pedido.getDireccionCliente());
//        html = html.replaceAll("@comuna", pedido.getComunaCliente());
        html = html.replaceAll("@fecha", frmFecha(pedido.getFechaHoraEntrega()));
        html = html.replaceAll("@opcliente", numOP(pedido.getIdPedido()));
        html = html.replaceAll("@url_elimina_suscripcion", urlEliminaSuscripcion);
        return html;
    }
    
    private static String frmFecha(String fecha){
        if (!(fecha==null) && !fecha.equals("") && (fecha.length()>=10)) {
            return fecha.substring(8,10)+"/"+fecha.substring(5,7)+"/"+fecha.substring(0,4);
        }
        return "N/A";
    }
    
    public static String numOP( long op ) {        
        String ret = (2*op+1) + "";        
        return ret;        
    }
}
