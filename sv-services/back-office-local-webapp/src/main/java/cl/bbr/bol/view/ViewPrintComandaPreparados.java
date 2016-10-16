package cl.bbr.bol.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.ComandaPreparadosDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega el popUp para imprimir la comanda de preparables
 * @author mleiva
 */
public class ViewPrintComandaPreparados  extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String param_id_sector	= "";
		

		String param_id_jornada	= "";
		long id_jornada			= -1;
		
		String param_nom_sector ="";
		

		/* preparables en la secion para esa jornada*/
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		if ( req.getParameter("id_sector") == null ){ throw new ParametroObligatorioException("id_sector");}	
		param_id_sector = req.getParameter("id_sector");		
		logger.debug("param_id_sector:"+param_id_sector);
		
		//id_sector = Long.parseLong(param_id_sector);
		if ( param_id_sector.equals("")) throw new ParametroObligatorioException("id_sector viene vacío");
		
		if ( req.getParameter("id_jornada") == null ){ throw new ParametroObligatorioException("id_jornada");}	
		param_id_jornada = req.getParameter("id_jornada");		
		id_jornada = Long.parseLong(param_id_jornada);
		logger.debug("id_jornada = " + req.getParameter("id_jornada"));
		if ( id_jornada == -1) throw new ParametroObligatorioException("id_jornada viene vacío");
		
		if ( req.getParameter("nom_sector") == null ){ throw new ParametroObligatorioException("nom_sector");}	
		param_nom_sector = req.getParameter("nom_sector");
		
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		List listaprods = new ArrayList();
		listaprods = bizDelegate.getComandaPreparados(id_jornada,param_id_sector);
		logger.debug("Total de sectores: "+listaprods.size());
		ArrayList detcomanda = new ArrayList();
		
		int j=0;
		
		for (int i=0; i<listaprods.size(); i++){			
			IValueSet fila = new ValueSet();
			ComandaPreparadosDTO prod1 = new ComandaPreparadosDTO();
			prod1 = (ComandaPreparadosDTO)listaprods.get(i);
			fila.setVariable("{id_pedido}",prod1.getId_pedido() );
			fila.setVariable("{seccion}",prod1.getSector() );
			fila.setVariable("{cod_sap}",prod1.getCod_sap() );
			fila.setVariable("{uni_med}",prod1.getUni_med() );
			fila.setVariable("{descr}",prod1.getDescr());
			fila.setVariable("{cantidad}",prod1.getCantidad());
			fila.setVariable("{obs}",prod1.getObs() );
			
			if (prod1.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE))
				fila.setVariable("{tipo_ve}", Constantes.TIPO_VE_SPECIAL_TXT );
			else
				fila.setVariable("{tipo_ve}", Constantes.TIPO_VE_NORMAL_TXT );
				
			detcomanda.add(fila);	

		}		
				
		// 5. Setea variables del template
		top.setVariable("{id_jornada}"	, param_id_jornada);
		top.setVariable("{nom_sector}"	, param_nom_sector);
		JornadaDTO jornada = new JornadaDTO();
		jornada = bizDelegate.getJornadaById(id_jornada);
			top.setVariable("{fecha}"		, jornada.getF_jornada());
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("detalle_comanda", detcomanda);
		
		
		
		// 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
	}
}
