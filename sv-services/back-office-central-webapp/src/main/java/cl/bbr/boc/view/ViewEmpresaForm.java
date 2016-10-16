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
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;

/**
 * Despliega el formulario que contiene los datos de la empresa
 * 
 * @author BBRI
 */
public class ViewEmpresaForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_empresa=0;
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
		if(req.getParameter("id_empresa")!=null)
			id_empresa = Long.parseLong(req.getParameter("id_empresa"));
		logger.debug("id_empresa:"+id_empresa);
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		

		BizDelegate bizDelegate = new BizDelegate();
		
		EmpresasDTO emp = bizDelegate.getEmpresaById(id_empresa);
		
		
		top.setVariable("{id_empresa}"	,String.valueOf(emp.getEmp_id()));
		top.setVariable("{rut}"			,String.valueOf(emp.getEmp_rut()));
		top.setVariable("{dv}"			,String.valueOf(emp.getEmp_dv()));
		top.setVariable("{raz_social}"	,String.valueOf(emp.getEmp_rzsocial()));
		top.setVariable("{nombre}"		,String.valueOf(emp.getEmp_nom()));
		top.setVariable("{rubro}"		,String.valueOf(emp.getEmp_rubro()));
		top.setVariable("{descr}"		,String.valueOf(emp.getEmp_descr()));
		//top.setVariable("{cant_emp}"	,String.valueOf(emp.getEmp_qtyemp()));
		
		if (emp.getEmp_qtyemp() == 2){
			top.setVariable("{sel_2}"	,"selected");
			top.setVariable("{sel_3}"	,"");
		}else if (emp.getEmp_qtyemp() == 3){
			top.setVariable("{sel_3}"	,"selected");
			top.setVariable("{sel_2}"	,"");
		}else{
			top.setVariable("{sel_2}"	,"");
			top.setVariable("{sel_3}"	,"");
		}
		
			
		top.setVariable("{nom_cont}"	,String.valueOf(emp.getEmp_nom_contacto()));
		top.setVariable("{cargo}"		,String.valueOf(emp.getEmp_cargo_contacto()));
		
		// codigos de telefono
		if (emp.getEmp_cod_fon1()!=null){
			top.setVariable("{selcod_tel1_"+emp.getEmp_cod_fon1()+"}"		,"selected");	
		}
		if (emp.getEmp_cod_fon2()!=null){
			top.setVariable("{selcod_tel2_"+emp.getEmp_cod_fon2()+"}"		,"selected");
				}
		if (emp.getEmp_cod_fon3()!=null){
			top.setVariable("{selcod_tel3_"+emp.getEmp_cod_fon3()+"}"		,"selected");
		}
		top.setVariable("{telf_1}"		,String.valueOf(emp.getEmp_fono1_contacto()));
		top.setVariable("{telf_2}"		,String.valueOf(emp.getEmp_fono2_contacto()));
		top.setVariable("{telf_3}"		,String.valueOf(emp.getEmp_fono3_contacto()));
		top.setVariable("{email}"		,String.valueOf(emp.getEmp_mail_contacto()));
		top.setVariable("{margen}"		,String.valueOf(emp.getEmp_mrg_minimo()));
		top.setVariable("{fec_mod}"		,String.valueOf(emp.getEmp_fmod()));
		
		top.setVariable("{saldo}"		 ,Formatos.formatoPrecio(emp.getEmp_saldo()) );
		top.setVariable("{fec_act_saldo}",String.valueOf(emp.getEmp_fact_saldo()));
		
		double saldo_actual_pend = bizDelegate.getSaldoActualPendiente(emp.getEmp_id());
		double disponible = emp.getEmp_saldo() - saldo_actual_pend;
		top.setVariable("{disp}", Formatos.formatoPrecio(disponible));
		top.setVariable("{dscto}", String.valueOf(emp.getEmp_dscto_max()));
		
		String campEstado = emp.getEmp_estado();
		logger.debug("estado:"+campEstado);

		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("EMP","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if (campEstado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
				
		// Verificamos si el usuario conectado es supervisor venta empresa
		List perfiles = usr.getPerfiles();
		long perfil =0;
		boolean esAdmVE = false; 
		for (int p=0; p < perfiles.size();p++){
			PerfilesEntity per =	(PerfilesEntity)perfiles.get(p);
			
			perfil = per.getIdPerfil().longValue();
			logger.debug("Perfil: " + perfil);
			if (perfil == Constantes.ID_PERFIL_SUPERVISOR_VE){
				esAdmVE = true;
				break;
			}
		}
		top.setVariable("{margen_inf}", Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_INF);
		top.setVariable("{margen_sup}", Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_SUP);
		top.setVariable("{descuento_inf}", Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_INF);
		top.setVariable("{descuento_sup}", Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_SUP);
		if (esAdmVE){
			top.setVariable("{sup_ve}", "1");
			top.setVariable("{rango_margen}", "");
			top.setVariable("{rango_descuento}","");
		}else{
			top.setVariable("{sup_ve}", "0");
			top.setVariable("{rango_margen}", "(Entre: "+Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_INF+"% - "+Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_SUP+"%)");
			top.setVariable("{rango_descuento}",  "(Entre: "+Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_INF+"% - "+Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_SUP+"%)");
		}
		
		//sucursales
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = bizDelegate.getSucursalesByEmpresaId(id_empresa); //emp.getLst_sucursales();

		for (int i = 0; i < lst_sucursales.size(); i++) {
			IValueSet fila_elem = new ValueSet();
			SucursalesDTO elem = (SucursalesDTO) lst_sucursales.get(i);
			fila_elem.setVariable("{suc_id}", String.valueOf(elem.getSuc_id()));
			fila_elem.setVariable("{suc_descr}", String.valueOf(elem.getSuc_descr()));
			sucursales.add(fila_elem);
		}
		if (lst_sucursales.size()==0){			
			top.setVariable("{disabled_eltos}"	,"disabled");
		}
		else{
			top.setVariable("{disabled_eltos}"	,"enabled");
		}
		
		top.setVariable("{msje}"  , msje);

		top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setDynamicValueSets("ESTADOS", estados);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
	
}

