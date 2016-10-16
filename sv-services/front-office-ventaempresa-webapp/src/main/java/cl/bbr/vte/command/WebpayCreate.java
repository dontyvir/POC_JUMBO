package cl.bbr.vte.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;

/**
 * @author imoyano
 */
public class WebpayCreate extends Command {

    protected void execute(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        System.out.println("**** entramos a WebpayCreate (VE) ****");
        PrintWriter out = res.getWriter();
        WebpayDTO webpayDTO = new WebpayDTO();
        webpayDTO.setTBK_CODIGO_AUTORIZACION(req.getParameter("TBK_CODIGO_AUTORIZACION"));
        webpayDTO.setTBK_FECHA_CONTABLE(req.getParameter("TBK_FECHA_CONTABLE"));
        webpayDTO.setTBK_FECHA_TRANSACCION(req.getParameter("TBK_FECHA_TRANSACCION"));
        webpayDTO.setTBK_FINAL_NUMERO_TARJETA(req.getParameter("TBK_FINAL_NUMERO_TARJETA"));
        webpayDTO.setTBK_HORA_TRANSACCION(req.getParameter("TBK_HORA_TRANSACCION"));
        webpayDTO.setTBK_ID_SESION(req.getParameter("TBK_ID_SESION"));
        webpayDTO.setTBK_ID_TRANSACCION(new BigDecimal(req.getParameter("TBK_ID_TRANSACCION")));
        webpayDTO.setTBK_MAC(req.getParameter("TBK_MAC"));
        webpayDTO.setTBK_MONTO(Integer.parseInt(req.getParameter("TBK_MONTO")));
        webpayDTO.setTBK_NUMERO_CUOTAS(Integer.parseInt(req.getParameter("TBK_NUMERO_CUOTAS")));
        webpayDTO.setTBK_ORDEN_COMPRA(Integer.parseInt(req.getParameter("TBK_ORDEN_COMPRA")));
        webpayDTO.setTBK_RESPUESTA(Integer.parseInt(req.getParameter("TBK_RESPUESTA")));
        if (req.getParameter("TBK_TASA_INTERES_MAX") != null)
            webpayDTO.setTBK_TASA_INTERES_MAX(Integer.parseInt(req.getParameter("TBK_TASA_INTERES_MAX")));
        else
            webpayDTO.setTBK_TASA_INTERES_MAX(0);
        webpayDTO.setTBK_TIPO_PAGO(req.getParameter("TBK_TIPO_PAGO"));
        webpayDTO.setTBK_TIPO_TRANSACCION(req.getParameter("TBK_TIPO_TRANSACCION"));
        if (req.getParameter("TBK_VCI") != null)
            webpayDTO.setTBK_VCI(req.getParameter("TBK_VCI"));
        else
            webpayDTO.setTBK_VCI("");
        //quitar dos últimos ceros, que representan decimales
        webpayDTO.setTBK_MONTO(webpayDTO.getTBK_MONTO() / 100);
        //guardamos el id_pedido interno
        webpayDTO.setIdPedido( Utils.idOrdenToIdPedido(webpayDTO.getTBK_ORDEN_COMPRA()));

        try {
            System.out.println("datos recibidos");
            BizDelegate biz = new BizDelegate();
            biz.webpaySave(webpayDTO);
            System.out.println("datos guardados");
            if (webpayDTO.getTBK_RESPUESTA() == 0) {
                String resultado = checkMac(req);
                System.out.println("checkmac->"+resultado);
                //verifica MAC
                if ("CORRECTO".equals(resultado)) {
                    int monto = biz.webpayMonto( webpayDTO.getIdPedido() );
                    //verifica monto
                    if (webpayDTO.getTBK_MONTO() == monto) {
                        //validamos que no este preingresado el pedido
                        int estado = biz.webpayGetEstado(webpayDTO.getIdPedido());
                        if ( estado == Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
                            out.print("ACEPTADO");    
                        } else {
                            out.print("RECHAZADO");
                        }
                    } else {
                        out.print("RECHAZADO");
                    }
                } else {
                    out.print("RECHAZADO");
                }
            } else {
                //Acepta no autorización de la transacción
                out.print("ACEPTADO");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //rechazado por error de base de datos.
            out.print("RECHAZADO");
        }
        return;        
    }

    /**
     * @param webpayDTO
     * @return
     * @throws IOException
     * @throws IOException
     */
    private String checkMac(HttpServletRequest req) throws IOException {
        System.out.println("en checkmac");
        ResourceBundle rb = ResourceBundle.getBundle("vte");
        String ruta = rb.getString("webpay.kit.ruta");
        System.out.println("ruta desde properties:" + ruta);
        String archivo = "log/tbk_" + req.getParameter("TBK_ORDEN_COMPRA") + ".txt";
        StringBuffer sb = new StringBuffer();
        sb.append("TBK_ORDEN_COMPRA=" + req.getParameter("TBK_ORDEN_COMPRA") + "&");
        sb.append("TBK_TIPO_TRANSACCION=" + req.getParameter("TBK_TIPO_TRANSACCION") + "&");
        sb.append("TBK_RESPUESTA=" + req.getParameter("TBK_RESPUESTA") + "&");
        sb.append("TBK_MONTO=" + req.getParameter("TBK_MONTO") + "&");
        sb.append("TBK_CODIGO_AUTORIZACION=" + req.getParameter("TBK_CODIGO_AUTORIZACION") + "&");
        sb.append("TBK_FINAL_NUMERO_TARJETA=" + req.getParameter("TBK_FINAL_NUMERO_TARJETA") + "&");
        sb.append("TBK_FECHA_CONTABLE=" + req.getParameter("TBK_FECHA_CONTABLE") + "&");
        sb.append("TBK_FECHA_TRANSACCION=" + req.getParameter("TBK_FECHA_TRANSACCION") + "&");
        sb.append("TBK_HORA_TRANSACCION=" + req.getParameter("TBK_HORA_TRANSACCION") + "&");
        sb.append("TBK_ID_SESION=" + req.getParameter("TBK_ID_SESION") + "&");
        sb.append("TBK_ID_TRANSACCION=" + req.getParameter("TBK_ID_TRANSACCION") + "&");
        sb.append("TBK_TIPO_PAGO=" + req.getParameter("TBK_TIPO_PAGO") + "&");
        sb.append("TBK_NUMERO_CUOTAS=" + req.getParameter("TBK_NUMERO_CUOTAS") + "&");
        if ( req.getParameter("TBK_TASA_INTERES_MAX") != null && !"".equals( req.getParameter("TBK_TASA_INTERES_MAX") ) )
            sb.append("TBK_TASA_INTERES_MAX=" + req.getParameter("TBK_TASA_INTERES_MAX") + "&");
        if ( req.getParameter("TBK_VCI") != null && !"".equals( req.getParameter("TBK_VCI") ) )
            sb.append("TBK_VCI=" + req.getParameter("TBK_VCI") + "&");
        sb.append("TBK_MAC=" + req.getParameter("TBK_MAC") );

        File file = new File(ruta + archivo);
        Writer output = new BufferedWriter(new FileWriter(file));
        output.write(sb.toString());
        output.close();

        String comando = ruta + "tbk_check_mac.cgi " + ruta + archivo;

        Process p = Runtime.getRuntime().exec(comando);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }
}
