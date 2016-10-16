package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.dto.CategorizacionErroneaDTO;
import cl.bbr.boc.dto.DetalleCategorizacionErroneaDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Fecha creación: 26/10/2011
 * Version: 1.0
 *
 * Clase que se encarga de capturar la información a ser mostrará para la categorizacion masiva por archivo excel
 * */
public class ViewCategorizacionMasiva extends Command {
	
	/* Sobreescritura del metodo Execute que realiza la execucion de las asignaciones para mostrar en el categorizados masivo
	 * @see cl.bbr.common.framework.Command#Execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cl.bbr.jumbocl.usuarios.dto.UserDTO)
	 */
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		logger.debug("Se ingresa al metodo Execute del ViewCategorizacionMasiva");
		// 1.0 Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//3.0 Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		HttpSession session = req.getSession(true);
		//valido si tengo el reporte guardado (es porque ya cargo y tengo resultados
		logger.debug("Valido si es que existe el reporte de categorizacion");
		if(session.getAttribute("repCategorizacionMasiva") != null){
			logger.debug("Recupero el listado con el reporte de categorizacion masiva");
			CategorizacionErroneaDTO reporte  = (CategorizacionErroneaDTO)session.getAttribute("repCategorizacionMasiva");
			ArrayList listado = reporte.getDetalle();
			ArrayList datos = new ArrayList();
			logger.debug("Recorro la lista para obtener los resultados");
			for (Iterator iter = listado.iterator(); iter.hasNext();) {
				DetalleCategorizacionErroneaDTO dto = (DetalleCategorizacionErroneaDTO) iter.next();
				IValueSet fila = new ValueSet();
				fila.setVariable("{id_producto}",dto.getId_producto());
				fila.setVariable("{id_categoria}",dto.getId_categoria());
				
				//20121008 SBERNAL
				fila.setVariable("{orden}", dto.getOrden());
				//-20121008 SBERNAL
				
				fila.setVariable("{mensaje_error}",dto.getMensajeError());
				datos.add(fila);
			}
			top.setVariable("{filasCargar}",new Long(reporte.getCantidadFilasProcesadas()));
			top.setVariable("{filasCorrectas}",new Long(reporte.getCantidadCargada()));
			top.setVariable("{filasErroneas}",new Long(reporte.getCantidadFallida()));
			top.setVariable("{fin_carga}" , "true");
			top.setDynamicValueSets("listado",datos);
			session.removeAttribute("repCategorizacionMasiva");
		}
		// 4.0 las asigno al top para mostrarlas en la pagina
		logger.debug("Ingreso los datos del usuario");
		Date hoy = new Date(); 
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" , usr.getLocal());		
		top.setVariable("{hdr_fecha}" , hoy.toString());
		
		//5.0 Salida Final
		logger.debug("Seteo la salida");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
