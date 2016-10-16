package cl.bbr.boc.command;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Servlet implementation class InformeManualProductividad
 */
public class InformeManualProductividad extends Command {
	private static final long serialVersionUID = 1L;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		   int dia_ini			= 0;
		   int mes_ini			= 0;
		   int dia_fin			= 0;
		   int mes_fin			= 0;
		   int agno_ini 			= 0; 
		   int agno_fin 			= 0; 
		   int flag_manual	    = 0;
        ResourceBundle rb = ResourceBundle.getBundle("bo");
        String pathCopihue = rb.getString("conf.path.informes.manual"); //  /informes/
 		String  mensajeSistema	= "";
 		
        logger.debug("User: " + usr.getLogin());
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		// 2. Procesa parámetros del request
		
		// 2.0 parámetro msg
		if ( req.getParameter("msg") != null )
			mensajeSistema = req.getParameter("msg");

		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.0 Creamos al BizDelegator
		
		Date fecha = new Date();
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_fecha}"	,fecha.toString());

		if ("1".equals(req.getParameter("mod")) && req.getParameter("fec_ini") != null && req.getParameter("fec_fin") != null) {

			   
			   String cadFechaIni= req.getParameter("fec_ini");
			   StringTokenizer fecIni = new StringTokenizer(cadFechaIni, "-");
			   while(fecIni.hasMoreTokens()) {
				   dia_ini = Integer.parseInt(fecIni.nextToken());
				   mes_ini = Integer.parseInt(fecIni.nextToken());
				   agno_ini = Integer.parseInt(fecIni.nextToken());
			   }
			   
			   String cadFechaFin= req.getParameter("fec_fin");
			   StringTokenizer fecFin = new StringTokenizer(cadFechaFin, "-");
			   	while(fecFin.hasMoreTokens()) {
			   		dia_fin = Integer.parseInt(fecFin.nextToken());
			   		mes_fin = Integer.parseInt(fecFin.nextToken());
			   		agno_fin = Integer.parseInt(fecFin.nextToken());
			   }
			
			
	           try {  
	        	   String yourShellInput = dia_ini+" "+mes_ini+" "+dia_fin+" "+mes_fin+" "+agno_ini+" "+agno_fin; 
	        	   
	        	   logger.debug("paremtro InformeManual : " + yourShellInput);
	        	   
	        	   String[] commandAndArgs = new String[]{ pathCopihue,yourShellInput };
	        	   Runtime.getRuntime().exec(commandAndArgs);
	        	   
		   			mensajeSistema="Informe consolidado para el cálculo de los incentivos de Jumbo.cl, será enviado en los proximos 30 minutos.";
					top.setVariable("{fec_ini}",req.getParameter("fec_ini"));
					top.setVariable("{fec_fin}",req.getParameter("fec_fin"));
					top.setVariable("{hab_mod_fec_op}", "disabled");
					top.setVariable("{mns}"	, mensajeSistema);
	   
	                } catch (IOException e) {  
	                       e.printStackTrace();
	                    mensajeSistema="Error al generar el informe";
	                    top.setVariable("{mns}"	, mensajeSistema);
	           			top.setVariable("{fec_ini}","");
	        			top.setVariable("{fec_fin}","");
	                } 
			

		}else{
			mensajeSistema="";
			top.setVariable("{fec_ini}","");
			top.setVariable("{fec_fin}","");
			top.setVariable("{mns}"	, mensajeSistema);
			top.setVariable("{hab_mod_fec_op}", "enabled");
		}
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
