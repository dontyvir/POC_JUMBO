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
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * despliega el formulario que permite mostra el detalle de comprador
 * 
 * @author BBRI
 */
public class ViewCompradorForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_comprador=0;
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
		if(req.getParameter("id_comprador")!=null)
			id_comprador = Long.parseLong(req.getParameter("id_comprador"));
		logger.debug("id_comprador:"+id_comprador);
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");

		BizDelegate bizDelegate = new BizDelegate();
		
		//obtiene datos de comprador
		CompradoresDTO comp = bizDelegate.getCompradoresById(id_comprador);
		
		top.setVariable("{id_comprador}"	,String.valueOf(comp.getCpr_id()));
		top.setVariable("{comp_rut}"		,String.valueOf(comp.getCpr_rut()));
		top.setVariable("{comp_dv}"			,String.valueOf(comp.getCpr_dv()));
		top.setVariable("{comp_nombre}"		,String.valueOf(comp.getCpr_nombres()));
		top.setVariable("{comp_paterno}"	,String.valueOf(comp.getCpr_ape_pat()));
		top.setVariable("{comp_materno}"	,String.valueOf(comp.getCpr_ape_mat()));
		top.setVariable("{comp_genero}"		,String.valueOf(comp.getCpr_genero()));
		top.setVariable("{comp_fec_crea}"	,String.valueOf(comp.getCpr_fec_crea()));
		top.setVariable("{comp_fono1}"		,String.valueOf(comp.getCpr_fono1()));
		top.setVariable("{comp_fono2}"		,String.valueOf(comp.getCpr_fono2()));
		top.setVariable("{comp_fono3}"		,String.valueOf(comp.getCpr_fono3()));
		top.setVariable("{comp_mail}"		,String.valueOf(comp.getCpr_email()));
		top.setVariable("{comp_estado}"		,String.valueOf(comp.getCpr_estado()));
		top.setVariable("{preg_secreta}"	,String.valueOf(comp.getCpr_pregunta()));
		top.setVariable("{resp_secreta}"	,String.valueOf(comp.getCpr_respuesta()));
		//top.setVariable("{clave}"		    ,String.valueOf(comp.getCpr_pass()));
		String estado = comp.getCpr_estado();
		logger.debug("id comp	:"+comp.getCpr_id());
		logger.debug("estado	:"+estado);
		
		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("COM","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			if (estado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");
			estados.add(fila_est);
		}
		
		//generos
		ArrayList generos = new ArrayList();
			//agrega masculino
			IValueSet fila_gen = new ValueSet();
			fila_gen.setVariable("{gen_id}"		, String.valueOf(Formatos.MSC_ID));
			fila_gen.setVariable("{gen_nom}"	, String.valueOf(Formatos.MSC_TXT));
			if (comp.getCpr_genero().equals(String.valueOf(Formatos.MSC_ID))){
				fila_gen.setVariable("{sel_gen}","selected");
			}
			else
				fila_gen.setVariable("{sel_gen}","");
			generos.add(fila_gen);
			//agrega femenino
			fila_gen = new ValueSet();
			fila_gen.setVariable("{gen_id}"		, String.valueOf(Formatos.FEM_ID));
			fila_gen.setVariable("{gen_nom}"	, String.valueOf(Formatos.FEM_TXT));
			if (comp.getCpr_genero().equals(String.valueOf(Formatos.FEM_ID))){
				fila_gen.setVariable("{sel_gen}","selected");
			}
			else
				fila_gen.setVariable("{sel_gen}","");
			generos.add(fila_gen);
			
		
		//listado de sucursales del comprador
		ArrayList suc_compr = new ArrayList();
		List listSuc = bizDelegate.getListSucursalesByCompradorId(comp.getCpr_id());

		for (int i = 0; i< listSuc.size(); i++){
			IValueSet fila_est = new ValueSet();
			ComprXSucDTO suc1 = (ComprXSucDTO)listSuc.get(i);
			fila_est.setVariable("{suc_id}"		, String.valueOf(suc1.getId_sucursal()));
			fila_est.setVariable("{suc_nom_emp}", String.valueOf(suc1.getNom_empresa()));
			fila_est.setVariable("{suc_nom}"	, String.valueOf(suc1.getNom_sucursal()));
			suc_compr.add(fila_est);
		}
		
		//Listado de empresas
		long id_empresa = -1;
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
			if (id_empresa<=0 && i==0)
				id_empresa = emp1.getEmp_id();
			fila_est.setVariable("{emp_id}", String.valueOf(emp1.getEmp_id()));
			fila_est.setVariable("{emp_nombre}"	, String.valueOf(emp1.getEmp_nom()));
			empresas.add(fila_est);
		}
		
		//Listado de sucursales
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = null;
		if(id_empresa >0)
			lst_sucursales = bizDelegate.getSucursalesByEmpresaId(id_empresa);
		else
			lst_sucursales = new ArrayList();
			//bizDelegate.getSucursalesByCriteria(crit_suc); 
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_est = new ValueSet();
			SucursalesDTO suc = (SucursalesDTO)lst_sucursales.get(i);
			fila_est.setVariable("{suc_id}", String.valueOf(suc.getSuc_id()));
			fila_est.setVariable("{suc_nombre}"	, String.valueOf(suc.getSuc_nombre()));
			sucursales.add(fila_est);
		}
		
		
		top.setVariable("{msje}"  , msje);
		top.setVariable("{id_comprador}"  , String.valueOf(id_comprador));
		top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("GENEROS", generos);
		top.setDynamicValueSets("SUC_COMPR", suc_compr);
		top.setDynamicValueSets("EMPRESAS", empresas);
		top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFecHoraActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}