package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Formulario de registro de clientes
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class RegisterForm extends Command {

	/**
	 * Formulario de registro
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		try {
		
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Registro", arg0);
	
			// Recupera la sesión del usuario sólo si existe
			//HttpSession session1 = arg0.getSession();
			//session1.invalidate(); // Se invalida para que no quede rastro de la anterior
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
	
			// Recupera parámetro desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
	
			// Template de despliegue
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
	
			IValueSet top = new ValueSet();
			
			if( session.getAttribute("cod_error") != null ) {
				this.getLogger().debug( "Cod_error:"+session.getAttribute("cod_error") );
				top.setVariable("{error}", "1");
				top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.error"));
				top.setVariable("{fsel_com}", arg0.getParameter("comuna") );
				session.removeAttribute("cod_error");
				
			}
			else {
				top.setVariable("{error}", "0");
				top.setVariable("{fsel_com}", "" );
			}		
			
			
			top.setVariable("{send_action}", "RegisterNew");
			top.setVariable("{readonly}", "");
			top.setVariable("{Mensaje_clave}", "" );
			
			if (arg0.getParameter("rut") == null)
				top.setVariable("{rut}", "" );
			else
				top.setVariable("{rut}", arg0.getParameter("rut")+"" );			
			
			if (arg0.getParameter("dv") == null)
				top.setVariable("{dv}", "" );
			else
				top.setVariable("{dv}", arg0.getParameter("dv")+"" );
	
			if (arg0.getParameter("nombre") == null)
				top.setVariable("{nombre}", "" );
			else
				top.setVariable("{nombre}", arg0.getParameter("nombre")+"" );
			
			if (arg0.getParameter("ape_pat") == null)
				top.setVariable("{ape_pat}", "" );
			else
				top.setVariable("{ape_pat}", arg0.getParameter("ape_pat")+"" );
			
			if (arg0.getParameter("ape_mat") == null)
				top.setVariable("{ape_mat}", "" );
			else
				top.setVariable("{ape_mat}", arg0.getParameter("ape_mat")+"" );
			
			if (arg0.getParameter("genero") == null){
				top.setVariable("{checkedF}", "" );	
				top.setVariable("{checkedM}", "" );
			}else if(arg0.getParameter("genero").compareTo("F") == 0){
				top.setVariable("{checkedF}", "checked" );
			}else if(arg0.getParameter("genero").compareTo("M") == 0){
				top.setVariable("{checkedM}", "checked" );
			}
			
			if (arg0.getParameter("mes") == null){
				top.setVariable("{mes1}", "" );
				top.setVariable("{mes2}", "" );
				top.setVariable("{mes3}", "" );
				top.setVariable("{mes4}", "" );
				top.setVariable("{mes5}", "" );
				top.setVariable("{mes6}", "" );
				top.setVariable("{mes7}", "" );
				top.setVariable("{mes8}", "" );
				top.setVariable("{mes9}", "" );
				top.setVariable("{mes10}", "" );
				top.setVariable("{mes11}", "" );
				top.setVariable("{mes12}", "" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 1){
				top.setVariable("{mes1}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 2){
				top.setVariable("{mes2}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 3){
				top.setVariable("{mes3}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 4){
				top.setVariable("{mes4}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 5){
				top.setVariable("{mes5}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 6){
				top.setVariable("{mes6}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 7){
				top.setVariable("{mes7}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 8){
				top.setVariable("{mes8}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 9){
				top.setVariable("{mes9}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 10){
				top.setVariable("{mes10}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 11){
				top.setVariable("{mes11}", "selected=\"selected\"" );
			}else if(Integer.parseInt(arg0.getParameter("mes")) == 12){
				top.setVariable("{mes12}", "selected=\"selected\"" );
			}
			
			if (arg0.getParameter("email1") == null)
				top.setVariable("{email1}", "" );
			else
				top.setVariable("{email1}", arg0.getParameter("email1")+"" );

			if (arg0.getParameter("dominio1") == null)
				top.setVariable("{dominio1}", "" );
			else
				top.setVariable("{dominio1}", arg0.getParameter("dominio1")+"" );
			
			if (arg0.getParameter("email2") == null)
				top.setVariable("{email2}", "" );
			else
				top.setVariable("{email2}", arg0.getParameter("email2")+"" );

			if (arg0.getParameter("dominio2") == null)
				top.setVariable("{dominio2}", "" );
			else
				top.setVariable("{dominio2}", arg0.getParameter("dominio2")+"" );
			
			if (arg0.getParameter("fon_cod_1") == null){
				top.setVariable("{selectfono02}", "" );
				top.setVariable("{selectfono32}", "" );
				top.setVariable("{selectfono33}", "" );
				top.setVariable("{selectfono34}", "" );
				top.setVariable("{selectfono35}", "" );
				top.setVariable("{selectfono41}", "" );
				top.setVariable("{selectfono42}", "" );
				top.setVariable("{selectfono43}", "" );
				top.setVariable("{selectfono45}", "" );
				top.setVariable("{selectfono51}", "" );
				top.setVariable("{selectfono52}", "" );
				top.setVariable("{selectfono53}", "" );
				top.setVariable("{selectfono55}", "" );
				top.setVariable("{selectfono57}", "" );
				top.setVariable("{selectfono58}", "" );
				top.setVariable("{selectfono61}", "" );
				top.setVariable("{selectfono63}", "" );
				top.setVariable("{selectfono64}", "" );
				top.setVariable("{selectfono65}", "" );
				top.setVariable("{selectfono67}", "" );
				top.setVariable("{selectfono71}", "" );
				top.setVariable("{selectfono72}", "" );
				top.setVariable("{selectfono73}", "" );
				top.setVariable("{selectfono75}", "" );
			}else if (arg0.getParameter("fon_cod_1") == "02"){
				top.setVariable("{selectfono02}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "32"){
				top.setVariable("{selectfono32}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "33"){
				top.setVariable("{selectfono33}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "34"){
				top.setVariable("{selectfono34}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "35"){
				top.setVariable("{selectfono35}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "41"){
				top.setVariable("{selectfono41}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "42"){
				top.setVariable("{selectfono42}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "43"){
				top.setVariable("{selectfono43}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "45"){
				top.setVariable("{selectfono45}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "51"){
				top.setVariable("{selectfono51}", "selected=\"selected\"" );				
			}else if (arg0.getParameter("fon_cod_1") == "52"){
				top.setVariable("{selectfono52}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "53"){
				top.setVariable("{selectfono53}", "selected=\"selected\"" );				
			}else if (arg0.getParameter("fon_cod_1") == "55"){
				top.setVariable("{selectfono55}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "57"){
				top.setVariable("{selectfono57}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "58"){
				top.setVariable("{selectfono58}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "61"){
				top.setVariable("{selectfono61}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "63"){
				top.setVariable("{selectfono63}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "64"){
				top.setVariable("{selectfono64}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "65"){
				top.setVariable("{selectfono65}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "67"){
				top.setVariable("{selectfono67}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "71"){
				top.setVariable("{selectfono71}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "72"){
				top.setVariable("{selectfono72}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "73"){
				top.setVariable("{selectfono73}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_1") == "75"){
				top.setVariable("{selectfono75}", "selected=\"selected\"" );
			}

			if (arg0.getParameter("fon_num_1") == null)
				top.setVariable("{fon_num_1}", "" );
			else
				top.setVariable("{fon_num_1}", arg0.getParameter("fon_num_1")+"" );
	
			if (arg0.getParameter("fon_num_2") == null)
				top.setVariable("{fon_num_2}", "" );
			else
				top.setVariable("{fon_num_2}", arg0.getParameter("fon_num_2")+"" );
	
			if (arg0.getParameter("fon_cod_3") == null){
				top.setVariable("{selectfono02}", "" );
				top.setVariable("{selectfono32}", "" );
				top.setVariable("{selectfono33}", "" );
				top.setVariable("{selectfono34}", "" );
				top.setVariable("{selectfono35}", "" );
				top.setVariable("{selectfono41}", "" );
				top.setVariable("{selectfono42}", "" );
				top.setVariable("{selectfono43}", "" );
				top.setVariable("{selectfono45}", "" );
				top.setVariable("{selectfono51}", "" );
				top.setVariable("{selectfono52}", "" );
				top.setVariable("{selectfono53}", "" );
				top.setVariable("{selectfono55}", "" );
				top.setVariable("{selectfono57}", "" );
				top.setVariable("{selectfono58}", "" );
				top.setVariable("{selectfono61}", "" );
				top.setVariable("{selectfono63}", "" );
				top.setVariable("{selectfono64}", "" );
				top.setVariable("{selectfono65}", "" );
				top.setVariable("{selectfono67}", "" );
				top.setVariable("{selectfono71}", "" );
				top.setVariable("{selectfono72}", "" );
				top.setVariable("{selectfono73}", "" );
				top.setVariable("{selectfono75}", "" );
			}else if (arg0.getParameter("fon_cod_3") == "02"){
				top.setVariable("{selectfono02}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "32"){
				top.setVariable("{selectfono32}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "33"){
				top.setVariable("{selectfono33}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "34"){
				top.setVariable("{selectfono34}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "35"){
				top.setVariable("{selectfono35}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "41"){
				top.setVariable("{selectfono41}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "42"){
				top.setVariable("{selectfono42}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "43"){
				top.setVariable("{selectfono43}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "45"){
				top.setVariable("{selectfono45}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "51"){
				top.setVariable("{selectfono51}", "selected=\"selected\"" );				
			}else if (arg0.getParameter("fon_cod_3") == "52"){
				top.setVariable("{selectfono52}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "53"){
				top.setVariable("{selectfono53}", "selected=\"selected\"" );				
			}else if (arg0.getParameter("fon_cod_3") == "55"){
				top.setVariable("{selectfono55}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "57"){
				top.setVariable("{selectfono57}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "58"){
				top.setVariable("{selectfono58}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "61"){
				top.setVariable("{selectfono61}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "63"){
				top.setVariable("{selectfono63}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "64"){
				top.setVariable("{selectfono64}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "65"){
				top.setVariable("{selectfono65}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "67"){
				top.setVariable("{selectfono67}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "71"){
				top.setVariable("{selectfono71}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "72"){
				top.setVariable("{selectfono72}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "73"){
				top.setVariable("{selectfono73}", "selected=\"selected\"" );
			}else if (arg0.getParameter("fon_cod_3") == "75"){
				top.setVariable("{selectfono75}", "selected=\"selected\"" );
			}

			if (arg0.getParameter("fon_num_3") == null)
				top.setVariable("{fon_num_3}", "" );
			else
				top.setVariable("{fon_num_3}", arg0.getParameter("fon_num_3")+"" );
			
			if (arg0.getParameter("pregunta") == null)
				top.setVariable("{pregunta}", "" );
			else
				top.setVariable("{pregunta}", arg0.getParameter("pregunta")+"" );
			
			if (arg0.getParameter("respuesta") == null)
				top.setVariable("{respuesta}", "" );
			else
				top.setVariable("{respuesta}", arg0.getParameter("respuesta")+"" );			
			
			if (arg0.getParameter("alias") == null)
				top.setVariable("{alias}", "" );
			else
				top.setVariable("{alias}", arg0.getParameter("alias")+"" );
	
			if (arg0.getParameter("calle") == null)
				top.setVariable("{calle}", "" );
			else
				top.setVariable("{calle}", arg0.getParameter("calle")+"" );
	
			if (arg0.getParameter("numero") == null)
				top.setVariable("{numero}", "" );
			else
				top.setVariable("{numero}", arg0.getParameter("numero")+"" );
	
			if (arg0.getParameter("departamento") == null)
				top.setVariable("{departamento}", "" );
			else
				top.setVariable("{departamento}", arg0.getParameter("departamento")+"" );
	
			if (arg0.getParameter("comentario") == null)
				top.setVariable("{comentario}", "ej: timbre malo" );
			else
				top.setVariable("{comentario}", arg0.getParameter("comentario")+"" );

			ArrayList arr_dias = new ArrayList();
			for (int i = 1; i <= 31; i++) {
				IValueSet fila = new ValueSet();
				if (arg0.getParameter("dia") == null ){
					fila.setVariable("{selectdia}", "" );
				}else if( Integer.parseInt(arg0.getParameter("dia")) == i){
					fila.setVariable("{selectdia}", "selected=\"selected\"" );
				}else{
					fila.setVariable("{selectdia}", "" );
				}
				
				fila.setVariable("{option_dia}", i + "");
				arr_dias.add(fila);
			}
			top.setDynamicValueSets("select_dias", arr_dias);
	
			ArrayList arr_anos = new ArrayList();
			Calendar ahora = Calendar.getInstance();
			for (int i = 1900; i <= ahora.get(Calendar.YEAR); i++) {
				IValueSet fila = new ValueSet();
				if (arg0.getParameter("ano") == null ){
					fila.setVariable("{selectano}", "" );
				}else if( Integer.parseInt(arg0.getParameter("ano")) == i){
					fila.setVariable("{selectano}", "selected=\"selected\"" );
				}else{
					fila.setVariable("{selectano}", "" );
				}
				
				fila.setVariable("{option_ano}", i + "");
				arr_anos.add(fila);
			}
			top.setDynamicValueSets("select_anos", arr_anos);
	
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
	
			// Listado de tipos de calle
			ArrayList arr_tipocalle = new ArrayList();
			List registros = biz.tiposCalleGetAll();
			this.getLogger().debug("Tipos de Calle:"+registros.size());
			for (int i = 0; i < registros.size(); i++) {
				TipoCalleDTO reg_tc = (TipoCalleDTO) registros.get(i);
				IValueSet fila = new ValueSet();
				if (arg0.getParameter("tipo_calle") == null ){
					fila.setVariable("{selectcalle}", "" );
				}else if( Integer.parseInt(arg0.getParameter("tipo_calle")) == i){
					fila.setVariable("{selectcalle}", "selected=\"selected\"" );
				}else{
					fila.setVariable("{selectcalle}", "" );
				}
				
				fila.setVariable("{option_calle_id}", reg_tc.getId() + "");
				fila.setVariable("{option_calle_nombre}", reg_tc.getNombre());
				arr_tipocalle.add(fila);
			}
			top.setDynamicValueSets("select_tipocalle", arr_tipocalle);
	
			ArrayList arr_regiones = new ArrayList();
			List regiones = biz.regionesGetAll();
			this.getLogger().debug("Regiones:"+regiones.size());
			for (int i = 0; i < regiones.size(); i++) {
				RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
				IValueSet fila = new ValueSet();
				if(arg0.getParameter("region") == null){
					top.setVariable("{selectregionaux}", "selected=\"selected\"");
					fila.setVariable("{selectregion}", "");
				}else if (Integer.parseInt(arg0.getParameter("region")) == dbregion.getId()){
					fila.setVariable("{selectregion}", "selected=\"selected\"");
				}else{
					fila.setVariable("{selectregion}", "");
				}
				
				fila.setVariable("{option_reg_id}", dbregion.getId()+"");
				fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
				arr_regiones.add(fila);
			}
			top.setDynamicValueSets("select_regiones", arr_regiones);
		
			String result = tem.toString(top);
	
			out.print(result);
		
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}		

	}

}