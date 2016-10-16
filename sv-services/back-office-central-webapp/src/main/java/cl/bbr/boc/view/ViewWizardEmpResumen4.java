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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * Muestra el monitor de Empresas
 * despliega los datos de la empresa, se puede utilizar filtros de búsqueda.
 * 
 * @author BBR
 *
 */
public class ViewWizardEmpResumen4 extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long emp_id	= 0;
		
		// 1. Parámetros de inicialización servlet
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id  es null");}
		emp_id = Long.parseLong(req.getParameter("emp_id"));
		
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate biz = new BizDelegate();
		
		//Datos de la Empresa 
		EmpresasDTO emp = biz.getEmpresaById(emp_id);
		List lst_est = biz.getEstadosByVis("EMP","S");
			
		top.setVariable("{emp_id}",	String.valueOf(emp.getEmp_id()));
		top.setVariable("{emp_rut}",	String.valueOf(emp.getEmp_rut()));
		top.setVariable("{emp_dv}",	String.valueOf(emp.getEmp_dv()));
		top.setVariable("{emp_rzsocial}",	String.valueOf(emp.getEmp_rzsocial()));
		top.setVariable("{emp_rubro}",	String.valueOf(emp.getEmp_rubro()));
		top.setVariable("{emp_descr}",	String.valueOf(emp.getEmp_descr()));
		top.setVariable("{emp_estado}",	FormatoEstados.frmEstado(lst_est,(emp.getEmp_estado())));
		
		// Lista de Compradores
		ArrayList compradores = new ArrayList();
		List listComp = biz.getListCompradoresByEmpresalId(emp_id);
		
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
		
		//Lista de Sucursales
		logger.debug("Obtiene listado de sucursales");
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = null;
		
		lst_sucursales=  biz.getSucursalesByEmpresaId(emp_id);
		
		
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_suc = new ValueSet();
			SucursalesDTO suc1 = (SucursalesDTO)lst_sucursales.get(i);
			fila_suc.setVariable("{suc_id}", String.valueOf(suc1.getSuc_id()));
			fila_suc.setVariable("{suc_nombre}"	, String.valueOf(suc1.getSuc_nombre()));
			fila_suc.setVariable("{suc_estado}"	, String.valueOf(suc1.getSuc_estado()));
			
			
//			 Listado de Direcciones de Facturacion creadas para la sucursal
			logger.debug("Obtiene listado de direcciones de facturacion");
			ArrayList dir_facturacion = new ArrayList();
			List listDirFact = biz.getListDireccionFactBySucursal(suc1.getSuc_id());

			for (int j = 0; j< listDirFact.size(); j++){
				IValueSet fila_est = new ValueSet();
				DirFacturacionDTO dir = (DirFacturacionDTO)listDirFact.get(j);
				fila_est.setVariable("{dir_id}", String.valueOf(dir.getDfac_id()));
				fila_est.setVariable("{dir_nom_tip_calle}", String.valueOf(dir.getNom_tip_calle()));
				fila_est.setVariable("{dir_calle}", String.valueOf(dir.getDfac_calle()));
				fila_est.setVariable("{dir_numero}", String.valueOf(dir.getDfac_numero()));
				fila_est.setVariable("{dir_depto}", String.valueOf(dir.getDfac_depto()));
				fila_est.setVariable("{dir_nom_comuna}", String.valueOf(dir.getNom_comuna()));
				fila_est.setVariable("{dir_nom_region}", String.valueOf(dir.getNom_region()));
				fila_est.setVariable("{id_sucursal}", String.valueOf(suc1.getSuc_id()));
				//fila_est.setVariable("{dir_nom_local}", String.valueOf(dir.getNom_local()));
				
				dir_facturacion.add(fila_est);
			}		
			fila_suc.setDynamicValueSets("DIR_FACTURACION", dir_facturacion);
			//Listado de direcciones de despacho asociadas a la sucursal
			

			logger.debug("Obtiene listado de direcciones de despacho");
			ArrayList dir_despachos = new ArrayList();
			List listDirDesp = biz.getListDireccionDespBySucursal(suc1.getSuc_id());

			for (int m = 0; m< listDirDesp.size(); m++){
				IValueSet fila_est = new ValueSet();
				DireccionesDTO dir = (DireccionesDTO)listDirDesp.get(m);
				fila_est.setVariable("{dir_id}", String.valueOf(dir.getId()));
				fila_est.setVariable("{tip_calle}", String.valueOf(dir.getNom_tip_calle()));
				fila_est.setVariable("{calle}", String.valueOf(dir.getCalle()));
				fila_est.setVariable("{numero}", String.valueOf(dir.getNumero()));
				fila_est.setVariable("{depto}", String.valueOf(dir.getDepto()));
				fila_est.setVariable("{comuna}", String.valueOf(dir.getNom_comuna()));
				fila_est.setVariable("{region}", String.valueOf(dir.getReg_nombre()));
				fila_est.setVariable("{local}", String.valueOf(dir.getNom_local()));
				fila_est.setVariable("{id_sucursal}", String.valueOf(suc1.getSuc_id()));
				
				dir_despachos.add(fila_est);
				
				
			}
			
			
			fila_suc.setDynamicValueSets("DIR_DESPACHO",dir_despachos);
			sucursales.add(fila_suc);
			
			
		}
		
		

		// 6. Setea variables bloques
		top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setDynamicValueSets("COMPRADORES",compradores);
		
		
			
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}
	
}
