/*
 * Creado el 31-oct-2012
 * Indra Company
 * 
 */
package cl.bbr.bol.command.faltantes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.bol.dto.ProductoFaltantesDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class InformeFaltantesGenerar extends Command{
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
//		 Recupera la sesión del usuario
        HttpSession session = req.getSession();
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/plain;");
        
        // Se recupera la salida para el servlet
//        PrintWriter out = res.getWriter();

        // Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		long idLocal = usr.getId_local();
		
		String inputFechaConsulta = (String)req.getParameter("fechaConsulta");
		String inputHorarioConsulta = (req.getParameter("horarioConsulta") != null && req.getParameter("horarioConsulta").compareToIgnoreCase("")!= 0) ? req.getParameter("horarioConsulta") : "0";
		String inputJornadaConsulta = (req.getParameter("jornadaConsulta") != null && req.getParameter("jornadaConsulta").compareToIgnoreCase("")!= 0) ? req.getParameter("jornadaConsulta") : "0";
		String inputTxtHorario = req.getParameter("txtHorario");
		
		BizDelegate bizDelegate = new BizDelegate();
		ParametroConsultaFaltantesDTO parametro = new ParametroConsultaFaltantesDTO();
		parametro.setFechaConsulta(sdf.parse(inputFechaConsulta));
		parametro.setIndicadorJornada(Long.parseLong(inputHorarioConsulta));
		parametro.setNumeroJornada(Long.parseLong(inputJornadaConsulta));
		parametro.setIdLocal(idLocal);
		parametro.setTextoJornada(inputTxtHorario);
		
		/*CONTENIDO FIJO*/
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}", usr.getLocal());
		top.setVariable("{hdr_fecha}", new Date());
		top.setVariable("{jornadaActual}",inputJornadaConsulta);
		top.setVariable("{fechaActual}",inputFechaConsulta);
		top.setVariable("{horarioActual}",inputTxtHorario);
		/*CONTENIDO FIJO*/

		
		
		String error = "";
		String strFila ="";
		try {
			HashMap informe = bizDelegate.getInformeFaltantes(parametro);
			
			if (informe.get("error") != null){
				error = (String)informe.get("error");
				res.setContentType("text/xml");
				res.getWriter().write("<datos_producto>");
		        res.getWriter().write("<mensaje>" + error + "</mensaje>");
		        res.getWriter().write("</datos_producto>");
				
				
			}else {
				if (inputHorarioConsulta.compareToIgnoreCase("0") == 0)	
					inputTxtHorario = (String)informe.get("textoHorario");
				top.setVariable("{horarioActual}",inputTxtHorario);
				List lista = (List)informe.get("listaResultado");
				String[] jornadas = (String[])informe.get("jornadas");
				for (int x = 0; x < jornadas.length; x++){
					top.setVariable("{jornada" + x + "}", jornadas[x]);
				}
				List productosList = new ArrayList();
				for (int x = 0; x < lista.size(); x++){ //OJO ... ESTA ENTRANDO SOLO UNA VEZ
					IValueSet fila = new ValueSet();
					List productos = (List)lista.get(x);
					ProductoFaltantesDTO prod = new ProductoFaltantesDTO();
					prod = (ProductoFaltantesDTO)productos.get(0);
					fila.setVariable("{idProducto}", String.valueOf(prod.getIdProducto()));
					fila.setVariable("{descripcion}", String.valueOf(prod.getDescripcion()));
					fila.setVariable("{sectorPicking}", String.valueOf(prod.getSectorPicking()));

					for (int y = 0; y < productos.size(); y++){
						ProductoFaltantesDTO producto = new ProductoFaltantesDTO();
						producto = (ProductoFaltantesDTO)productos.get(y);
						fila.setVariable("{cantidadProductosJ" + y + "}", String.valueOf(producto.getCantidadProductos()));
						fila.setVariable("{presenciaProductosEnJornadaJ" + y + "}", String.valueOf(producto.getPresenciaProductosEnJornada()));
						fila.setVariable("{opsTotalesPorJornadaJ" + y + "}", String.valueOf(producto.getOpsTotalesPorJornada()));
						fila.setVariable("{porcentajePresenciaJ" + y + "}", String.valueOf(producto.getPorcentajePresencia()));
					}
					productosList.add(fila);
				}
				top.setDynamicValueSets("LISTADO_PRODUCTOS", productosList);
				
				String result = tem.toString(top);
				salida.setHtmlOut(result);
				salida.Output();
				}
		}catch(Exception e) {
			logger.error(e);
			throw new Exception(e);
		}
		
	}  
}
