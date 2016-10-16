package cl.bbr.boc.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.jumbocl.common.utils.Formatos;

public class CambiarMontosEnOP extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {

        logger.debug("Comienzo CambiarMontosEnOP Execute");
        
        String tplFile = getServletConfig().getInitParameter("TplFile");
        String modProd_mail = getServletConfig().getInitParameter("modProd_mail");
        String pmod = "[PRECIO MODIFICADO]";

        if ( req.getParameter("datos_cambiados") == null ) { throw new ParametroObligatorioException("faltan datos a cambiar"); }
        if ( req.getParameter("id_pedido") == null ) { throw new ParametroObligatorioException("faltan el pedido"); }

        // 2.2 obtiene parametros desde el request
        String datosCambiados = req.getParameter("datos_cambiados");
        long idPedido = Long.parseLong( req.getParameter("id_pedido") ); 
        
        BizDelegate biz = new BizDelegate();
        
        if(biz.isActivaCorreccionAutomatica()){	
        	res.sendRedirect("/JumboBOCentral/ViewError?mensaje=Correccion de excesos automatico esta activado.&url=&PagErr=1");        
        }
        
        PedidoDTO ped = biz.getPedidosById(idPedido);
        
        // guardar nuevos precios
        biz.cambiarPreciosPickeados(idPedido, datosCambiados);
        
        // indicamos que pedido ya no esta excedido
        biz.modPedidoExcedido(idPedido, false);
        
        // guardamos en log los parametros modificados
        String[] prods = datosCambiados.split("-=-");
      
        ProductosService serv = new ProductosService();
        String catCompleta, catCompletaAux = "";
        String logPedido ="";
        List prepareMail = new ArrayList();
        for ( int i=0; i < prods.length; i++ ) {
            String[] datos = prods[i].split("#");
            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(idPedido);
            log.setUsuario(usr.getLogin());
            catCompleta = serv.getCategCompletaProducto(Integer.parseInt(datos[3]));
            catCompletaAux = catCompleta;
            int lastSlash = 0;
            
            for(int l=0; l<3;l++){
            	if(catCompletaAux.indexOf("/")>=0){
            		lastSlash=catCompletaAux.indexOf("/");
            		catCompletaAux=catCompletaAux.replaceFirst("/", "");

            	}
            		
            }
            catCompleta = catCompleta.substring(0, lastSlash+2);
            //OJO: Si se modifica este string, es necesario cambiar la query que genera el informe
            
            logPedido = ", Cod SAP=" + datos[3]+", Picking=" + datos[0] + ", Precio ANT=" + Formatos.formatoPrecio(Double.parseDouble(datos[2])) + ", Precio NVO=" + Formatos.formatoPrecio(Double.parseDouble(datos[1]))+", ["+usr.getLogin()+"]";
            char[] caracteres = catCompleta.toLowerCase().toCharArray();
            for (int j = 0; j < catCompleta.length()- 2; j++) {
                if (caracteres[j] == ' ' || caracteres[j] == '.' || caracteres[j] == ',' || caracteres[j] == '/')
                  caracteres[j + 1] = Character.toUpperCase(caracteres[j + 1]);
            }
            catCompleta = new String(caracteres);
            
            log.setLog(pmod+": "+catCompleta+logPedido);
            biz.addLogPedido(log);
            prepareMail.add(log);
            
            try {              
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                String mail_tpl = rb.getString("conf.path.html") + "" + modProd_mail;
                TemplateLoader mail_load = new TemplateLoader(mail_tpl);
                ITemplate mail_tem = mail_load.getTemplate();
                
                List listaLog = new ArrayList();
        		listaLog = biz.getLogPedido(idPedido);
		
    			LogPedidoDTO log1 = new LogPedidoDTO();
    			log1 = (LogPedidoDTO)listaLog.get(0);
    			String fechaHora = Formatos.frmFechaHora(log1.getFecha());
        		String mail_result = mail_tem.toString(contenidoMailLog(String.valueOf(idPedido), datos[3], datos[0], datos[2], datos[1], usr.getLogin(), catCompleta+"/", datos[4], fechaHora));
                // Se envía mail al cliente
                MailDTO mail = new MailDTO();
                String subject = "[Precio Modificado] "+catCompleta+"/"+datos[4];
                mail.setFsm_subject(subject);
                mail.setFsm_data(mail_result);
                List mailPMs = new ArrayList();
                mailPMs = biz.getMailPM();
                String from = "";
                for(int k=0; k<mailPMs.size(); k++){
                	if(k>0)
                		from=from+","+mailPMs.get(k).toString();
                	else
                		from=mailPMs.get(k).toString();
                }
                logger.debug("Mail Product Managers-> "+from);
                mail.setFsm_destina(from);
                mail.setFsm_remite(rb.getString("mail.cambio_precio_op_exceso.remitente"));
                biz.addMail(mail);

            } catch (Exception e) {
                logger.error("Problemas con mail", e);
            }
            
            
        }
        

        
        if ( ped.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ) {
            //borrar trx's
            if ( biz.DelTrxMP( idPedido ) ) {
                LogPedidoDTO log1 = new LogPedidoDTO();
                log1.setId_pedido(idPedido);
                log1.setUsuario(usr.getLogin());
                log1.setLog("Transacciones borradas");
                biz.addLogPedido(log1);
            }
            
            //cambiar estado del pedido
            biz.setModEstadoPedido(idPedido, Constantes.ID_ESTAD_PEDIDO_EN_BODEGA);
            LogPedidoDTO log2 = new LogPedidoDTO();
            log2.setId_pedido(idPedido);
            log2.setUsuario(usr.getLogin());
            log2.setLog("Pedido queda 'En Bodega'");
            biz.addLogPedido(log2);
        }
        ForwardParameters fp = new ForwardParameters();
        fp.add("id_pedido", ""+idPedido);
        fp.add("msg", "OK");        
        
        res.sendRedirect(tplFile + fp.forward());

    }
    /**
     * @param cot
     * @param pedido
     * @return
     * @throws SystemException
     * @throws BocException
     */

    private IValueSet contenidoMailLog(String op, String codSap, String detallePicking, String antPrecio, String nvoPrecio, String userMod, String prodMod, String nomProd, String fechaMod) throws BocException, SystemException {
        IValueSet mail_top = new ValueSet();
        
        mail_top.setVariable("{ordenPedido}",op);
        mail_top.setVariable("{codigoSap}",codSap);
        mail_top.setVariable("{seccionRubro}",prodMod);
        mail_top.setVariable("{nombreProducto}",nomProd);      
        mail_top.setVariable("{precioPrevio}",Formatos.formatoPrecio(Double.parseDouble(antPrecio)));
        mail_top.setVariable("{precioNuevo}",Formatos.formatoPrecio(Double.parseDouble(nvoPrecio)));
        mail_top.setVariable("{fechaModificacion}",fechaMod);
        mail_top.setVariable("{usuarioModifico}",userMod);

        return mail_top;
    }
}