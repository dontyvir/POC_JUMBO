package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.casos.utils.ReclamosClientesCasos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado de clientes y sus puntajes de reclamos de acuerdo a un rango de fecha
 * 
 * @author imoyano
 */
public class ViewReclamosClientes extends Command {
    
    private String nombreExcel = "";

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewReclamosClientes Execute");
	    
	    // Variables
	    String tipoSalida	= "";
	    String fechaIni		= "";
	    String fechaFin		= "";
	    
	    // Sacamos la info de la pagina
		if (req.getParameter("tipo_salida") != null) {
		    tipoSalida = req.getParameter("tipo_salida").toString();
	    }
		if (req.getParameter("fecha_ini") != null) {
		    fechaIni = req.getParameter("fecha_ini").toString();
	    }
		if (req.getParameter("fecha_fin") != null) {
		    fechaFin = req.getParameter("fecha_fin").toString();
	    }
	    
		//Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		logger.debug("tipoSalida:"+tipoSalida);
		logger.debug("fechaIni:"+fechaIni);
		logger.debug("fechaFin:"+fechaFin);
	    
	    if (tipoSalida.equalsIgnoreCase("PLANILLA")) {
	        logger.debug("Inicio GENERAMOS UNA TABLA TIPO PLANILLA");
	        
	        res.setContentType("application/vnd.ms-excel");
	        
	        //res.setHeader("Content-Disposition","inline;filename=reclamos.xls");
	        res.setHeader("Content-Disposition","attachment;filename="+nombreExcel+".xls");	      
	        
	        res.setCharacterEncoding("UTF-8");
            res.getWriter().write(respuestaHTML(fechaIni,fechaFin,tipoSalida));
	        
	        logger.debug("Fin GENERAMOS UNA TABLA TIPO PLANILLA");
	        return;
			
	    } else if (tipoSalida.equalsIgnoreCase("HTML")) {
	        logger.debug("Inicio GENERAMOS LA TABLA EN HTML");
	        
	        res.setContentType("text/html");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(respuestaHTML(fechaIni,fechaFin,tipoSalida));
	        
            logger.debug("Fin GENERAMOS LA TABLA EN HTML");
            return;
	        
	    } else {
	        // Mostramos la página HTML lista para que el usuario pueda realizar la busqueda
		    
	        top.setVariable("{fecha_ini}", fechaIni);
	        top.setVariable("{fecha_fin}", fechaFin);
	        top.setVariable("{fecha_actual}", CasosUtil.fechaActualMasDiaResolucion(0));
	        
	        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
			Date now = new Date();
			top.setVariable("{hdr_fecha}", now.toString());	
			
			logger.debug("Fin ViewReclamosClientes Execute");
			String result = tem.toString(top);
			salida.setHtmlOut(result);
			salida.Output();
	    
	    }
		
	}

    /**
     * @return
     */
    private String respuestaHTML(String fechaIni, String fechaFin, String tipoSalida) {
        List reclamosClientes = new ArrayList();
        ReclamosClientesCasos rcc = new ReclamosClientesCasos(fechaIni, fechaFin, tipoSalida);        
        BizDelegate biz = new BizDelegate();
        try {
            nombreExcel = "Puntajes_Reclamos_" + rcc.ddIni + "-" + rcc.mmIni + "-" + rcc.aaaaIni + "_" + rcc.ddFin + "-" + rcc.mmFin + "-" + rcc.aaaaFin;
            reclamosClientes = biz.getTablaReclamosClientes(rcc.ddIni + "/" + rcc.mmIni + "/" + rcc.aaaaIni, rcc.ddFin + "/" + rcc.mmFin + "/" + rcc.aaaaFin, rcc.llaves);
        } catch (BocException e) {}
        return rcc.tablaHtmlReclamos(reclamosClientes);
    }
}
