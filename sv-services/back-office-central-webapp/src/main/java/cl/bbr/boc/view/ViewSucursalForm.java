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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * Despliega el formulario que contiene los datos de la sucursal
 * 
 * @author BBRI
 */
public class ViewSucursalForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_sucursal=0;
		String msje			= "";
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		if(req.getParameter("id_sucursal")!=null)
			id_sucursal = Long.parseLong(req.getParameter("id_sucursal"));
		logger.debug("id_sucursal:"+id_sucursal);
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		

		BizDelegate bizDelegate = new BizDelegate();
		
		SucursalesDTO suc = bizDelegate.getSucursalById(id_sucursal);
		
		if(suc==null)
			throw new Exception(); 
			
		top.setVariable("{id_sucursal}"	,String.valueOf(suc.getSuc_id()));
		top.setVariable("{rut}"			,String.valueOf(suc.getSuc_rut()));
		top.setVariable("{dv}"			,String.valueOf(suc.getSuc_dv()));
		top.setVariable("{raz_social}"	,String.valueOf(suc.getSuc_razon()));
		top.setVariable("{nombre}"		,String.valueOf(suc.getSuc_nombre()));
		top.setVariable("{descrip}"		,String.valueOf(suc.getSuc_descr()));
		top.setVariable("{id_empresa}"	,String.valueOf(suc.getSuc_emp_id()));
		top.setVariable("{fec_crea}"	,Formatos.formatFecha(suc.getSuc_fec_crea()));
		top.setVariable("{telf_1}"		,String.valueOf(suc.getSuc_fono_num1()));
		top.setVariable("{telf_2}"		,String.valueOf(suc.getSuc_fono_num2()));
		top.setVariable("{email}"		,String.valueOf(suc.getSuc_email()));
		top.setVariable("{fec_mod}"		,String.valueOf(suc.getSuc_fec_act()));
		top.setVariable("{enc_recep}"   ,suc.getSuc_pregunta());
		EmpresasDTO emp = bizDelegate.getEmpresaById(suc.getSuc_emp_id());
		top.setVariable("{empresa}"		,String.valueOf(emp.getEmp_rzsocial()));
		
		if (suc.getSuc_fono_cod1()!=null){
			top.setVariable("{selcod_tel1_"+suc.getSuc_fono_cod1()+"}"		,"selected");
		}
		
		if (suc.getSuc_fono_cod2()!=null){
			top.setVariable("{selcod_tel2_"+suc.getSuc_fono_cod2()+"}"		,"selected");
		}
		
		
		//long id_empresa = suc.getSuc_emp_id();
		String campEstado = suc.getSuc_estado();
		logger.debug("estado:"+campEstado);
		/*
		//listado de empresas
		logger.debug("Obtiene listado de empresas");
		ArrayList empresas = new ArrayList();
		EmpresaCriteriaDTO crit_emp = new EmpresaCriteriaDTO ();
		crit_emp.setEstado("");
		crit_emp.setRazon_social("");
		crit_emp.setRut("");
		crit_emp.setRegsperpage(1000);
		crit_emp.setPag(1);
		List lst_empresas = bizDelegate.getEmpresasByCriteria(crit_emp); 
		for (int i = 0; i< lst_empresas.size(); i++){
			IValueSet fila_est = new ValueSet();
			EmpresasDTO emp1 = (EmpresasDTO)lst_empresas.get(i);
			fila_est.setVariable("{emp_id}", String.valueOf(emp1.getEmp_id()));
			fila_est.setVariable("{emp_raz_social}"	, String.valueOf(emp1.getEmp_rzsocial()));
			if(id_empresa == emp1.getEmp_id()){
				fila_est.setVariable("{sel2}","selected");	
			}else
				fila_est.setVariable("{sel2}","");		
			empresas.add(fila_est);
		}
		*/
		//estados
		logger.debug("Obtiene listado de estados");
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("SUC","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if (campEstado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel1}","selected");
			}
			else
				fila_est.setVariable("{sel1}","");		
			estados.add(fila_est);
		}
		
		//listado de compradores
		logger.debug("Obtiene listado de compradores");
		ArrayList compradores = new ArrayList();
		List listComp = bizDelegate.getListCompradoresBySucursalId(suc.getSuc_id());

		for (int i = 0; i< listComp.size(); i++){
			IValueSet fila_est = new ValueSet();
			CompradoresDTO com = (CompradoresDTO)listComp.get(i);
			fila_est.setVariable("{com_nombre}", String.valueOf(com.getCpr_nombres()));
			fila_est.setVariable("{com_ape_pat}", String.valueOf(com.getCpr_ape_pat()));
			fila_est.setVariable("{com_ape_mat}", String.valueOf(com.getCpr_ape_mat()));
				// Verificamos si es administrador 		
			if (bizDelegate.esAdministrador(com.getCpr_id(),suc.getSuc_id())){
				fila_est.setVariable("{com_tipo}"	, String.valueOf(Constantes.TIPO_COMPR_ADM_TXT));
			}else{
				fila_est.setVariable("{com_tipo}"	, String.valueOf(Constantes.TIPO_COMPR_TXT));
			}
				
			compradores.add(fila_est);
		}
		
		//listado de direcciones de despacho
		logger.debug("Obtiene listado de direcciones de despacho");
		ArrayList dir_despachos = new ArrayList();
		List listDirDesp = bizDelegate.getListDireccionDespBySucursal(suc.getSuc_id());

		for (int i = 0; i< listDirDesp.size(); i++){
			IValueSet fila_est = new ValueSet();
			DireccionesDTO dir = (DireccionesDTO)listDirDesp.get(i);
			fila_est.setVariable("{dir_id}", String.valueOf(dir.getId()));
			fila_est.setVariable("{dir_nom_tip_calle}", String.valueOf(dir.getNom_tip_calle()));
			fila_est.setVariable("{dir_calle}", String.valueOf(dir.getCalle()));
			fila_est.setVariable("{dir_numero}", String.valueOf(dir.getNumero()));
			fila_est.setVariable("{dir_depto}", String.valueOf(dir.getDepto()));
			fila_est.setVariable("{dir_nom_comuna}", String.valueOf(dir.getNom_comuna()));
			fila_est.setVariable("{dir_nom_region}", String.valueOf(dir.getReg_nombre()));
			fila_est.setVariable("{dir_nom_local}", String.valueOf(dir.getNom_local()));
			fila_est.setVariable("{id_sucursal}", String.valueOf(id_sucursal));
			
			dir_despachos.add(fila_est);
		}
		
		//listado de direcciones de facturacion
		logger.debug("Obtiene listado de direcciones de facturacion");
		ArrayList dir_facturacion = new ArrayList();
		List listDirFact = bizDelegate.getListDireccionFactBySucursal(suc.getSuc_id());

		for (int i = 0; i< listDirFact.size(); i++){
			IValueSet fila_est = new ValueSet();
			DirFacturacionDTO dir = (DirFacturacionDTO)listDirFact.get(i);
			fila_est.setVariable("{dir_id}", String.valueOf(dir.getDfac_id()));
			fila_est.setVariable("{dir_nom_tip_calle}", String.valueOf(dir.getNom_tip_calle()));
			fila_est.setVariable("{dir_calle}", String.valueOf(dir.getDfac_calle()));
			fila_est.setVariable("{dir_numero}", String.valueOf(dir.getDfac_numero()));
			fila_est.setVariable("{dir_depto}", String.valueOf(dir.getDfac_depto()));
			fila_est.setVariable("{dir_nom_comuna}", String.valueOf(dir.getNom_comuna()));
			fila_est.setVariable("{dir_nom_region}", String.valueOf(dir.getNom_region()));
			fila_est.setVariable("{id_sucursal}", String.valueOf(id_sucursal));
			//fila_est.setVariable("{dir_nom_local}", String.valueOf(dir.getNom_local()));
			
			dir_facturacion.add(fila_est);
		}
		
		
		top.setVariable("{msje}"  , msje);
		top.setVariable("{id_sucursal}"  , String.valueOf(id_sucursal));

		//top.setDynamicValueSets("EMPRESAS", empresas);
		top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("COMPRADORES", compradores);
		top.setDynamicValueSets("DIR_DESPACHOS", dir_despachos);
		top.setDynamicValueSets("DIR_FACTURACION", dir_facturacion);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
	
}

