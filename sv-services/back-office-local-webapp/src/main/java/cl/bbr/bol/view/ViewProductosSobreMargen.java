package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import cl.bbr.jumbocl.pedidos.dto.ProductoSobreMargenDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Fecha Cración: 12/10/2011
 *
 * Clase encargada de mostrar el reporte de productos sobre el margen de sustitución para 
 * ser retirados de los bins correspondientes.
 * 
 */
public class ViewProductosSobreMargen extends Command {

	
	/* Sobreescritura del metodo Execute para realizar las acciones para mostrar el reporte
	 * @see cl.bbr.common.framework.Command#Execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cl.bbr.jumbocl.usuarios.dto.UserDTO)
	 */
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		logger.debug("Se ingresa al metodo Execute del ViewProductosSobreMargen");
		// 1.0 Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		String paramUrl = getServletConfig().getInitParameter("url");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//3.0 Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		// 4.0 Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		// 5.0 realizo la recuperacion del reporte de productos sustitutos sobre el margen de sustitución
		ArrayList datos = new ArrayList();
		logger.debug("Se realiza llamado de getProductosSustitutosSobreMarge");
		List listado = biz.getProductosSustitutosSobreMargen(usr.getId_local());
		logger.debug("Se recupera el listado de productos");
		logger.debug("Recorro la lista para obtener los resultados");
		for (Iterator iter = listado.iterator(); iter.hasNext();) {
			ProductoSobreMargenDTO dto = (ProductoSobreMargenDTO) iter.next();
			SimpleDateFormat formatFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			IValueSet fila = new ValueSet();
			fila.setVariable("{id_sustituto_sobre_umbral}",String.valueOf(dto.getIdSustitutoSobreUmbral()));
			fila.setVariable("{fecha_operacion}",formatFecha.format(dto.getFechaOperacion()));
			fila.setVariable("{pickeador_nombre}",dto.getNombrePickeador());
			fila.setVariable("{pickeador_apellido}",dto.getApellidoPickeador());
			fila.setVariable("{id_producto}",String.valueOf(dto.getIdProducto()));
			fila.setVariable("{descripcion_producto}",dto.getDescripcionProducto());
			fila.setVariable("{id_ronda}",String.valueOf(dto.getIdRonda()));
			fila.setVariable("{cod_bin}",dto.getCodBin());
			fila.setVariable("{mensaje}",dto.getMensaje());
			datos.add(fila);
		}
		logger.debug("Asigno el listado");
		// 6.0 las asigno al top para mostrarlas en el reporte (listado)
		Date hoy = new Date(); 
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" , usr.getLocal());		
		top.setVariable("{hdr_fecha}" , hoy.toString());
		top.setDynamicValueSets("listado",datos);
		//7.0 Salida Final
		logger.debug("Seteo la salida");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
	
}
