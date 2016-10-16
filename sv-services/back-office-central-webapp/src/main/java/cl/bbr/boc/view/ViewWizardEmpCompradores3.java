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
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
/**
 * despliega el formulario que permite crear un nuevo comprador
 * 
 * @author BBRI
 */
public class ViewWizardEmpCompradores3 extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long emp_id =0;
		String msje="";
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id  es null");}
		emp_id = Long.parseLong(req.getParameter("emp_id"));
		if ( req.getParameter("msje") != null )
			msje=req.getParameter("msje") ;

		BizDelegate biz = new BizDelegate();
		
		// Listado de Compradores de la empresa
		ArrayList compradores = new ArrayList();
		List listComp = biz.getListCompradoresByEmpresalId(emp_id);
		int cant_reg = listComp.size();
		for (int i=0; i<listComp.size();i++){
			IValueSet fila_comp = new ValueSet();
			CompradoresDTO comp1 = (CompradoresDTO)listComp.get(i);
			fila_comp.setVariable("{comp_id}", String.valueOf(comp1.getCpr_id()));
			fila_comp.setVariable("{comp_rut}"	, String.valueOf(comp1.getCpr_rut()));
			fila_comp.setVariable("{comp_dv}"	, comp1.getCpr_dv());
			fila_comp.setVariable("{comp_nombre}"	, comp1.getCpr_nombres());
			fila_comp.setVariable("{comp_paterno}"	, comp1.getCpr_ape_pat());
			fila_comp.setVariable("{comp_materno}"	, comp1.getCpr_ape_mat());
			
			// Verificamos si es administrador 		
			if ( comp1.getCpr_tipo() != null &&  comp1.getCpr_tipo().equals(String.valueOf(emp_id))){
				fila_comp.setVariable("{comp_tipo}"	, Constantes.TIPO_COMPR_ADM_TXT);
			}else{
				fila_comp.setVariable("{comp_tipo}"	, Constantes.TIPO_COMPR_TXT);
			}
				
		
			compradores.add(fila_comp);
		}
		// Listado de sucursales activas de la empresa
		SucursalCriteriaDTO criterio = new SucursalCriteriaDTO();
		criterio.setId_empresa(emp_id);
		criterio.setEstado(Constantes.ESTADO_ACTIVADO);
		criterio.setPag(1);
		criterio.setRegsperpage(1000);


		logger.debug("Obtiene listado de sucursales");
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = null;
		
		lst_sucursales= biz.getSucursalesByCriteria(criterio);// biz.getSucursalesByEmpresaId(emp_id);
		
		
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_suc = new ValueSet();
			SucursalesDTO suc1 = (SucursalesDTO)lst_sucursales.get(i);
			fila_suc.setVariable("{suc_id}", String.valueOf(suc1.getSuc_id()));
			fila_suc.setVariable("{suc_nombre}"	, String.valueOf(suc1.getSuc_nombre()));
			fila_suc.setVariable("{suc_estado}"	, String.valueOf(suc1.getSuc_estado()));
			sucursales.add(fila_suc);
		}
		
				
		
		
		//estados
		ArrayList estados = new ArrayList();
		List listEst = biz.getEstadosByVis("COM","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			if (est1.getId_estado() == Constantes.ESTADO_COMPRADOR_CAMBIO_CLAVE){
				fila_est.setVariable("{sel_est}", "selected");
			}else{
				fila_est.setVariable("{sel_est}", "");
			}
			estados.add(fila_est);
		}
		
		
//		 Si tiene al menos un comprador habilita el boton siguiente
		if (cant_reg > 0){
			top.setVariable("{hab}"	,"Enabled");
		}else{
			top.setVariable("{hab}"	,"Disabled");
		}
		
		top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setDynamicValueSets("COMPRADORES", compradores);
		top.setDynamicValueSets("ESTADOS", estados);
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		top.setVariable("{emp_id}"	,String.valueOf(emp_id));
		top.setVariable("{msje}"	,msje);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}