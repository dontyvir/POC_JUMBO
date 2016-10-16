package cl.bbr.boc.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresaLogDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos
 * ingresados en el formulario
 * 
 * @author imoyano
 *  
 */
public class ModEmpresaLinea extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

        long paramId_empresa = -1;
        double saldoNew = 0;
        double saldoOld = 0;
        
        String nombreEmpresa = "";
        String rutCompleto = "";
        
        String msjOK = "<font color=blue>Monto asignado exitosamente</font>";
        String msjER = "<font color=red>Error al asignar el nuevo monto</font>";
        
        if ( req.getParameter("id_empresa") == null ) {
            throw new ParametroObligatorioException("id_empresa es null");
        }
        if ( req.getParameter("new_monto") == null ) {
            throw new ParametroObligatorioException("new_monto es null");
        }
        if ( req.getParameter("old_monto") == null ) {
            throw new ParametroObligatorioException("old_monto es null");
        }

        paramId_empresa = Long.parseLong(req.getParameter("id_empresa"));
        saldoNew = Double.parseDouble(req.getParameter("new_monto"));
        saldoOld = Double.parseDouble(req.getParameter("old_monto"));
        
        nombreEmpresa = req.getParameter("empresa");
        rutCompleto = req.getParameter("rut_completo");

        String paramUrl = getServletConfig().getInitParameter("Url") + "?id_empresa=" + paramId_empresa;
        
        EmpresasDTO prm = new EmpresasDTO();
        prm.setEmp_id(paramId_empresa);
        prm.setEmp_saldo(saldoNew);

        BizDelegate bizDelegate = new BizDelegate();

        try {
            boolean result = bizDelegate.setModEmpresaLinea(prm);
            if ( result ) {                
                EmpresaLogDTO log = new EmpresaLogDTO();
                log.setIdEmpresa(paramId_empresa);
                log.setIdUsuario(usr.getId_usuario());
                log.setSaldoOld(saldoOld);
                log.setSaldoNew(saldoNew);
                try {
                    bizDelegate.setEmpresaLineaLog(log);    
                } catch (Exception e) {}
                
                try {                    
                    ResourceBundle rb = ResourceBundle.getBundle("bo");
                    MailDTO mail = new MailDTO();
                    mail.setFsm_subject( rb.getString("mail.ve_linea.titulo") );
                    mail.setFsm_destina( rb.getString("mail.ve_linea.destinatario") );
                    mail.setFsm_remite(  rb.getString("mail.ve_linea.remitente") );
                    
                    String body = rb.getString("mail.ve_linea.texto");
                    body = body.replaceAll("@empresa",nombreEmpresa);
                    body = body.replaceAll("@rut",rutCompleto);
                    body = body.replaceAll("@old_saldo",Formatos.formatoNumeroSinDecimales(saldoOld));
                    body = body.replaceAll("@new_saldo",Formatos.formatoNumeroSinDecimales(saldoNew));
                    body = body.replaceAll("@user",usr.getNombre() + " " + usr.getApe_paterno() + " (" + usr.getLogin() + ")");
                    mail.setFsm_data( body );
                    
                    bizDelegate.enviarMailSupervisor( mail );
                    
                } catch (Exception e) {
                    logger.error(e.getMessage());    
                }
                
                paramUrl += "&msje="+msjOK;
            } else {
                paramUrl += "&msje="+msjER;
            }

        } catch ( BocException e ) {
            paramUrl += "&msje="+msjER;
        }

        res.sendRedirect(paramUrl);

    }

}
