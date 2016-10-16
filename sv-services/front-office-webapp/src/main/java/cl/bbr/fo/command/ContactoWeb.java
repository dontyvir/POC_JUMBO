package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;

/**
 * Envio de información de contacto del sitio
 *  
 * @author imoyano
 *  
 */
public class ContactoWeb extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {        
        
        HttpSession session = arg0.getSession();
        
        if ( session.getAttribute("captcha_mail") != null ) {            
            if ( arg0.getParameter("gif_captcha").toString().equals(session.getAttribute("captcha_mail").toString()) ) {
                session.removeAttribute("captcha_mail");
            } else {
                throw new CommandException();
            }            
        } else {
            throw new CommandException();
        }
        
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        BizDelegate biz = new BizDelegate();
        
        String template = "";
        if ( "main".equalsIgnoreCase(arg0.getParameter("origen")) ) {
            template = getServletConfig().getInitParameter("mail");        
        } else if ( "auspicios".equalsIgnoreCase(arg0.getParameter("origen")) ) {
            template = getServletConfig().getInitParameter("mail_auspicios");
        } else if ( "proveedores".equalsIgnoreCase(arg0.getParameter("origen")) ) {
            template = getServletConfig().getInitParameter("mail_proveedores");
        } else {
            throw new CommandException();
        }        
        
        String mailTpl = rb.getString("conf.dir.html") + "" + template;
        TemplateLoader mailLoad = new TemplateLoader(mailTpl);
        ITemplate mailTem = mailLoad.getTemplate();        
        
        IValueSet mailTop = new ValueSet();
        
        mailTop = mailDatosEnComun(mailTop, arg0);
        
        if ( "auspicios".equalsIgnoreCase(arg0.getParameter("origen")) || "proveedores".equalsIgnoreCase(arg0.getParameter("origen")) ) {
            mailTop = mailDatosEnComunProveedoresYAuspicios(mailTop, arg0);
        } else {
            mailTop = mailDatosContacto(mailTop, arg0, biz);        
        }
        
        String mail_result = mailTem.toString(mailTop);
        // Se envía mail al cliente
        MailDTO mail = new MailDTO();
        mail.setFsm_subject( "Contacto de cliente via jumbo.cl" );
        mail.setFsm_data( mail_result );
        mail.setFsm_destina( destinatarios(arg0.getParameter("origen"), arg0.getParameter("rubro_empresa"), arg0.getParameter("destino")) );
        mail.setFsm_remite( rb.getString( "mail.checkout.remite" ) );
        biz.addMail( mail );
        
        arg1.sendRedirect( "/supermercado/contactenos/exito.html" );

    }

    /**
     * @param parameter
     * @param parameter2
     * @return
     */
    private String destinatarios(String origen, String rubro, String destino) {
        ResourceBundle rb = ResourceBundle.getBundle("contacto_mail");        
        if ( "proveedores".equalsIgnoreCase(origen) ) {
            return rb.getString(rubro.replaceAll(" ","_"));
        } else if ( "auspicios".equalsIgnoreCase(origen) ) {
            return rb.getString("auspicios");
        } 
        return rb.getString(destino.replaceAll(" ","_"));        
    }

    /**
     * @param mailTop
     * @param arg0
     * @return
     */
    private IValueSet mailDatosEnComun(IValueSet mailTop, HttpServletRequest arg0) {
        if (arg0.getParameter("nombre") != null)
            mailTop.setVariable("{nombre}", arg0.getParameter("nombre"));
        else
            mailTop.setVariable("{nombre}", "");
        
        if (arg0.getParameter("apellido_paterno") != null)
            mailTop.setVariable("{paterno}", arg0.getParameter("apellido_paterno"));
        else
            mailTop.setVariable("{paterno}", "");
        
        if (arg0.getParameter("apellido_materno") != null)
            mailTop.setVariable("{materno}", arg0.getParameter("apellido_materno"));
        else
            mailTop.setVariable("{materno}", "");
        
        if (arg0.getParameter("rut") != null)
            mailTop.setVariable("{rut}", arg0.getParameter("rut"));
        else
            mailTop.setVariable("{rut}", "");
        
        if (arg0.getParameter("mail") != null)
            mailTop.setVariable("{mail}", arg0.getParameter("mail"));
        else
            mailTop.setVariable("{mail}", "");
        
        if (arg0.getParameter("cod_fono") != null)
            mailTop.setVariable("{cod_fono}", arg0.getParameter("cod_fono"));
        else
            mailTop.setVariable("{cod_fono}", "");
        
        if (arg0.getParameter("fono") != null)
            mailTop.setVariable("{fono}", arg0.getParameter("fono"));
        else
            mailTop.setVariable("{fono}", "");
        
        if (arg0.getParameter("celular") != null && !"".equalsIgnoreCase(arg0.getParameter("celular"))) {
            mailTop.setVariable("{celu}", arg0.getParameter("celular"));            
            if (arg0.getParameter("cod_celular") != null)
                mailTop.setVariable("{cod_celu}", arg0.getParameter("cod_celular"));
            else
                mailTop.setVariable("{cod_celu}", "");
            
        } else {
            mailTop.setVariable("{celu}", "");
            mailTop.setVariable("{cod_celu}", "");
        }
        if (arg0.getParameter("comentarios") != null) {
            String comentario = arg0.getParameter("comentarios");
            comentario = comentario.replaceAll("\r\n","<br>");
            mailTop.setVariable("{comentario}", comentario);
        } else
            mailTop.setVariable("{comentario}", "");
        
        return mailTop;
    }

    /**
     * @param mailTop
     * @param arg0
     * @param biz
     * @return
     */
    private IValueSet mailDatosEnComunProveedoresYAuspicios(IValueSet mailTop, HttpServletRequest arg0) {
            
        if (arg0.getParameter("empresa") != null)
            mailTop.setVariable("{empresa}", arg0.getParameter("empresa"));
        else
            mailTop.setVariable("{empresa}", "");
        
        if (arg0.getParameter("rubro_empresa") != null)
            mailTop.setVariable("{rubro}", arg0.getParameter("rubro_empresa"));
        else
            mailTop.setVariable("{rubro}", "");
        
        return mailTop;
    }

    /**
     * @return
     */
    private IValueSet mailDatosContacto(IValueSet mailTop, HttpServletRequest arg0, BizDelegate biz) {
        
        if (arg0.getParameter("motivo") != null)
            mailTop.setVariable("{motivo}", arg0.getParameter("motivo"));
        else
            mailTop.setVariable("{motivo}", "");
        
        if ( arg0.getParameter("destino") != null && "Local".equalsIgnoreCase(arg0.getParameter("destino"))) {
            List loc = new ArrayList();
            IValueSet filaLoc = new ValueSet();
            
            if (arg0.getParameter("local") != null && !"0".equalsIgnoreCase(arg0.getParameter("local")))
                filaLoc.setVariable("{local}", arg0.getParameter("local"));
            else
                filaLoc.setVariable("{local}", "");
            
            loc.add(filaLoc);
            
            mailTop.setDynamicValueSets("LOCAL", loc);
        
        }       

        if ( arg0.getParameter("destino") != null && "Venta Empresas".equalsIgnoreCase(arg0.getParameter("destino"))) {
            List ve = new ArrayList();
            IValueSet filaVe = new ValueSet();
            
            if (arg0.getParameter("cargo") != null)
                filaVe.setVariable("{cargo}", arg0.getParameter("cargo"));
            else
                filaVe.setVariable("{cargo}", "");
            
            if (arg0.getParameter("empresa") != null)
                filaVe.setVariable("{empresa}", arg0.getParameter("empresa"));
            else
                filaVe.setVariable("{empresa}", "");
            
            if (arg0.getParameter("tamano_empresa") != null && !"0".equalsIgnoreCase(arg0.getParameter("tamano_empresa")))
                filaVe.setVariable("{tamano_empresa}", arg0.getParameter("tamano_empresa"));
            else
                filaVe.setVariable("{tamano_empresa}", "");
            
            if (arg0.getParameter("rubro_empresa") != null)
                filaVe.setVariable("{rubro}", arg0.getParameter("rubro_empresa"));
            else
                filaVe.setVariable("{rubro}", "");
            
            if (arg0.getParameter("como_supiste") != null && !"0".equalsIgnoreCase(arg0.getParameter("como_supiste")))
                filaVe.setVariable("{como}", arg0.getParameter("como_supiste"));
            else
                filaVe.setVariable("{como}", "");
            
            if (arg0.getParameter("id_region") != null && !"0".equalsIgnoreCase(arg0.getParameter("id_region"))) {
                RegionesDTO reg;
                try {
                    reg = biz.getRegionById(Integer.parseInt(arg0.getParameter("id_region")));
                    filaVe.setVariable("{region}", reg.getNombre());
                } catch (Exception e) {
                    filaVe.setVariable("{region}", "");
                }
                                
            } else
                filaVe.setVariable("{region}", "");
            
            if (arg0.getParameter("id_comuna") != null && !"0".equalsIgnoreCase(arg0.getParameter("id_comuna")))
                filaVe.setVariable("{comuna}", arg0.getParameter("id_comuna"));
            else
                filaVe.setVariable("{comuna}", "");
            
            ve.add(filaVe);
            
            mailTop.setDynamicValueSets("VEMPRESA", ve);
        }
        
        return mailTop;
    }
}