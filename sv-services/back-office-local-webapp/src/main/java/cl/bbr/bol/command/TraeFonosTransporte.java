package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Trae los Fonos de Transporte (Ajax)
 * 
 * @author imoyano
 */

public class TraeFonosTransporte extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo TraeFonosTransporte [AJAX]");

        String tipo = req.getParameter("tipo");
        long idObjeto = 0;
        
        if ( tipo.equalsIgnoreCase("P") ) {
            String[] idx = req.getParameter("id").toString().split("-=-");
            idObjeto = Long.parseLong(idx[0]);
        } else {
            idObjeto = Long.parseLong(req.getParameter("id").toString());
        }
        long idEmpresaTransporte = 0;
        
        BizDelegate biz = new BizDelegate();
        
        if ( idObjeto != 0 ) {
            if (tipo.equalsIgnoreCase("F")) {
                FonoTransporteDTO fono = biz.getFonoTransporteById(idObjeto);
                idEmpresaTransporte = fono.getEmpresaTransporte().getIdEmpresaTransporte();
            } else if (tipo.equalsIgnoreCase("C")) {
                ChoferTransporteDTO chofer = biz.getChoferTransporteById(idObjeto);
                idEmpresaTransporte = chofer.getEmpresaTransporte().getIdEmpresaTransporte();
            } else if (tipo.equalsIgnoreCase("P")) {
                PatenteTransporteDTO fono = biz.getPatenteTransporteById(idObjeto);
                idEmpresaTransporte = fono.getEmpresaTransporte().getIdEmpresaTransporte();
            }
        }
        
        List fonos = new ArrayList();
        
        try {
            fonos = biz.getFonosDeTransporte(idEmpresaTransporte, usr.getId_local());
        } catch (Exception e) {}
        
        res.setContentType("text/html");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<option value='0'>Seleccionar</option>\n");
        for (int i=0; i < fonos.size(); i++) {
            FonoTransporteDTO fono = (FonoTransporteDTO) fonos.get(i);
            res.getWriter().write("<option value='" + fono.getIdFono() + "'>" + fono.getNombre() + " (" + fono.getCodigo() + " " + fono.getNumero() + ")</option>\n");
        }

		
        logger.debug("Fin TraeFonosTransporte [AJAX]");
    }
}
