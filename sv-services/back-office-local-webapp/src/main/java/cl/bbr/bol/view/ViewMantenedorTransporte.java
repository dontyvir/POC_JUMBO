package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado para el monitor de rutas
 * @author imoyano
 */
public class ViewMantenedorTransporte extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMantenedorTransporte Execute");
	    
        View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");       
        html = path_html + html;
        logger.debug("Template: " + html);      
        
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();
        
        String msj = "";
        
        if ( req.getParameter("mensaje") != null ) {
            msj = req.getParameter("mensaje");
        }
        
        BizDelegate biz = new BizDelegate();        
        
        // ---- Patentes de transporte ----
        List patentes = biz.getPatentesDeTransporteByLocal(usr.getId_local());
        ArrayList lPatentes = new ArrayList();
        
        for (int i = 0; i < patentes.size(); i++) {          
            IValueSet fila = new ValueSet();
            PatenteTransporteDTO patente = (PatenteTransporteDTO) patentes.get(i);
            fila.setVariable("{id}",String.valueOf(patente.getIdPatente()));         
            fila.setVariable("{patente}",patente.getPatente());
            fila.setVariable("{bins}",String.valueOf(patente.getCantMaxBins()));
            fila.setVariable("{id_empresa}",String.valueOf(patente.getEmpresaTransporte().getIdEmpresaTransporte()));
            fila.setVariable("{empresa}",patente.getEmpresaTransporte().getNombre());
            if ( "S".equalsIgnoreCase( patente.getActivado() )) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",patente.getActivado());
            lPatentes.add(fila);
        }
        if (lPatentes.size() > 0) {
            top.setVariable("{msj_patentes}","");
        } else {
            top.setVariable("{msj_patentes}","No existen patentes.");
        }
        
        // ---- Telefonos de Transportes ----
        List fonos = biz.getFonosDeTransporteByLocal(usr.getId_local());
        ArrayList lFonos = new ArrayList();
        
        for (int i = 0; i < fonos.size(); i++) {          
            IValueSet fila = new ValueSet();
            FonoTransporteDTO fono = (FonoTransporteDTO) fonos.get(i);
            fila.setVariable("{id}",String.valueOf(fono.getIdFono()));         
            fila.setVariable("{nombre}",fono.getNombre());
            fila.setVariable("{codigo}",String.valueOf(fono.getCodigo()));
            fila.setVariable("{numero}",String.valueOf(fono.getNumero()));
            fila.setVariable("{id_empresa}",String.valueOf(fono.getEmpresaTransporte().getIdEmpresaTransporte()));
            fila.setVariable("{empresa}",fono.getEmpresaTransporte().getNombre());
            if ( "S".equalsIgnoreCase( fono.getActivado() )) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",fono.getActivado());
            lFonos.add(fila);
        }
        if (lFonos.size() > 0) {
            top.setVariable("{msj_fonos}","");
        } else {
            top.setVariable("{msj_fonos}","No existen fonos.");
        }
        
        //---- Choferes transportistas --
        List choferes = biz.getChoferesDeTransporteByLocal(usr.getId_local());
        ArrayList lChoferes = new ArrayList();
        
        for (int i = 0; i < choferes.size(); i++) {          
            IValueSet fila = new ValueSet();
            ChoferTransporteDTO chofer = (ChoferTransporteDTO) choferes.get(i);
            fila.setVariable("{id}",String.valueOf(chofer.getIdChofer()));         
            fila.setVariable("{nombre}",chofer.getNombre());
            if ( chofer.getRut() == 0 ) {
                fila.setVariable("{rut}","");
            } else {
                fila.setVariable("{rut}",String.valueOf(chofer.getRut()));    
            }
            if ( chofer.getDv() == null ) {
                fila.setVariable("{dv}","");
            } else {
                fila.setVariable("{dv}",chofer.getDv());    
            }
            fila.setVariable("{id_empresa}",String.valueOf(chofer.getEmpresaTransporte().getIdEmpresaTransporte()));
            fila.setVariable("{empresa}",chofer.getEmpresaTransporte().getNombre());
            if ( "S".equalsIgnoreCase( chofer.getActivado() )) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",chofer.getActivado());
            lChoferes.add(fila);
        }
        if (lChoferes.size() > 0) {
            top.setVariable("{msj_choferes}","");
        } else {
            top.setVariable("{msj_choferes}","No existen choferes.");
        }

        //---- Empresas Activas --
        List empresas = biz.getEmpresasTransporteActivas();
        ArrayList lEmpresasActivas = new ArrayList();
        
        for (int i = 0; i < empresas.size(); i++) {          
            IValueSet fila = new ValueSet();
            EmpresaTransporteDTO empresa = (EmpresaTransporteDTO) empresas.get(i);
            fila.setVariable("{id}",String.valueOf(empresa.getIdEmpresaTransporte()));         
            fila.setVariable("{nombre}",empresa.getNombre());
            lEmpresasActivas.add(fila);
        }
        
        top.setDynamicValueSets("LIST_PATENTES", lPatentes);
        top.setDynamicValueSets("LIST_FONOS", lFonos);
        top.setDynamicValueSets("LIST_CHOFERES", lChoferes);
        top.setDynamicValueSets("LIST_EMPRESAS_ACTIVAS", lEmpresasActivas);
        
        top.setVariable("{msj}", msj);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewMantenedorTransporte Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
