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
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.common.codes.Constantes;

/**
 * Muestra el monitor de Clientes
 * despliega los datos del cliente, se puede utilizar filtros de búsqueda.
 * @author bbr
 */
public class ViewMonClient extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int regsperpage = 10;
		String nombre="";
		String rut="";
		//char idestado;
		String idestado;
		int pag;
		String action = "";
		String buscapor = "";
		logger.debug("User: " + usr.getLogin());
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		String email = "";
		int maxLengthBuscar = 30;//maxLength=30, para busqueda por rut y apellido.

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("action") != null )
			action= req.getParameter("action");
		if ( req.getParameter("pagina") != null ){
			pag = Integer.parseInt( req.getParameter("pagina") );			
		}	
		else {
			pag = 1;			
		}
		if ( req.getParameter("estado") != null )
			idestado = req.getParameter("estado");
			//idestado = req.getParameter("estado").charAt(0);
		else
			idestado="";
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();
		
		//segun el radio button "buscapor"
		buscapor = req.getParameter("buscapor");
		logger.debug("Buscar por = " + buscapor);
		if( buscapor!= null ){
			//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
			
			//Buscar por Apellido:
			if(buscapor.equals("apellido")){
				if (!req.getParameter("buscar").equals("")){
					nombre = (String) req.getParameter("buscar");
				}
				top.setVariable("{buscar}", nombre);
				
				top.setVariable("{check_1}"   , "");//rut
		    	top.setVariable("{check_2}","checked");//apellido
		    	top.setVariable("{check_3}"   , "");//email
		    	maxLengthBuscar = 30;
			}//Buscar por Rut:
			else if(buscapor.equals("rut")){
				if (!req.getParameter("buscar").equals("")){
					rut = (String) req.getParameter("buscar");
				}
				top.setVariable("{buscar}", rut);
				
				top.setVariable("{check_2}"   ,"");//apellido
				top.setVariable("{check_3}"   ,"");//email
		    	top.setVariable("{check_1}","checked");//rut
		    	maxLengthBuscar = 30;
			}//Busqueda por Email:
			else if(buscapor.equals("email")){
				if (!req.getParameter("buscar").equals("")){
					email = (String) req.getParameter("buscar");
				}
				top.setVariable("{buscar}", email);
				
				top.setVariable("{check_1}"   , "");//rut
		    	top.setVariable("{check_2}","");//apellido
		    	top.setVariable("{check_3}","checked");//email
		    	maxLengthBuscar = 50;//maxLength=50, para busqueda por email.
			}
			
			
			/* Codigo Original:
			if(buscapor.equals("apellido")){
				if (!req.getParameter("buscar").equals("")){
					nombre = (String) req.getParameter("buscar");
				}
				top.setVariable("{buscar}", nombre);
			}
			if(buscapor.equals("rut")){
				if (!req.getParameter("buscar").equals("")){
					rut = (String) req.getParameter("buscar");
				}
				top.setVariable("{buscar}", rut);
				
				
					top.setVariable("{check_2}"   ,"");
			    	top.setVariable("{check_1}","checked");
			} else {
				top.setVariable("{check_1}"   , "");
		    	top.setVariable("{check_2}","checked");
			}*/	
		}else{
			top.setVariable("{buscar}", "");
			buscapor = "";
		}
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		//Asigna valor de maxlength al input de buscar:
		top.setVariable("{maxLengthBuscar}",String.valueOf(maxLengthBuscar));
		
		//parametros para paginar
    	top.setVariable("{check_1}","checked");


		// 4.1 Listado de Clientes
		logger.debug("Este es el buscapor: " + buscapor);
		logger.debug("Este es el rut: " + rut);
		//ClienteCriteriaDTO criterio = new ClienteCriteriaDTO(pag, rut, nombre, idestado, regsperpage, true); //Original
		
		//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
		ClienteCriteriaDTO criterio = new ClienteCriteriaDTO();
		criterio.setPagina(pag);
		criterio.setRut(rut);
		criterio.setApellido(nombre);
		criterio.setEst_bloqueo(idestado);
		criterio.setRegsperpage(regsperpage);
		criterio.setPag_activa(true);
		criterio.setEmail(email);
		
		// Obtiene el resultado

			ArrayList estados = new ArrayList();
		
			List listEstados = bizDelegate.getEstadosByVis("CLB","S");
		
			for (int i = 0; i< listEstados.size(); i++){
				IValueSet fila_est = new ValueSet();
				EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
				fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
				fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
				
				if (idestado != "" && String.valueOf(idestado).equals(String.valueOf(est1.getId_estado()))){
					fila_est.setVariable("{sel1}","selected");
				}
				else
					fila_est.setVariable("{sel1}","");		

				estados.add(fila_est);
				
			}

	//	Lista de clientes
		List listclientes = null;
		
		listclientes=  bizDelegate.getClientesByCriteria(criterio);
			
		List lst_est = bizDelegate.getEstadosByVis("CLB","S");
	
		ArrayList datos = new ArrayList();
		if (listclientes.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
		}

		for (int i = 0; i < listclientes.size(); i++) {			
				IValueSet fila = new ValueSet();
				ClientesDTO cli1 = (ClientesDTO)listclientes.get(i);
				
				fila.setVariable("{rut}"		  ,String.valueOf(cli1.getRut())+"-"+cli1.getDv());
				//fila.setVariable("{rut}"		  ,String.valueOf(cli1.getRut())) ;
				fila.setVariable("{nombres}"	  ,String.valueOf(cli1.getNombre()));
				fila.setVariable("{apellido_pat}" ,String.valueOf(cli1.getPaterno()));
				fila.setVariable("{apellido_mat}" ,String.valueOf(cli1.getMaterno()));
				fila.setVariable("{f_nac}"		  , Formatos.frmFecha(cli1.getFnac()));
				fila.setVariable("{bloq}"		  , FormatoEstados.frmEstado(lst_est,cli1.getEst_bloqueo()));	
				fila.setVariable("{acciones}"	  ,"Ver");
				fila.setVariable("{id_cliente}"	  ,String.valueOf(cli1.getId_cliente()));
				if (cli1.getRzs_empresa()!= null){
					fila.setVariable("{empresa}"  , String.valueOf(cli1.getRzs_empresa()));
					fila.setVariable("{ver_empresa}"	  , "ViewEmpresaForm?id_empresa="+cli1.getId_empresa() );					
					fila.setVariable("{ver}"	  , "ViewSucursalForm?id_sucursal="+cli1.getId_cliente());
					
				}else{
					fila.setVariable("{empresa}"	  , Constantes.SIN_DATO);
					fila.setVariable("{ver_empresa}"	  , "#");
					fila.setVariable("{ver}"	  , "ViewClientForm?id_cliente="+cli1.getId_cliente());
				}
				datos.add(fila);
			}		
		
	
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getClientesCountByCriteria(criterio);
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		if (total_pag == 0){
			total_pag = 1;
		}
		logger.debug("Total de registros: " + tot_reg);
	    for (int i = 1; i <= total_pag; i++) {
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}	    
	    if( pag >1){
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    }else if (pag==1){
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag <total_pag){
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    }else{
	    	top.setVariable("{siguiente_label}","");
	    }
		//Setea variables main del template 
	    
	    top.setVariable("{buscar}"  ,rut+nombre+email);//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
	    top.setVariable("{buscapor}",buscapor);
	    top.setVariable("{num_pag}"	,String.valueOf(pag));
		top.setVariable("{estado}"  , String.valueOf(idestado));
		top.setVariable("{action}"	,action);

		// 6. Setea variables bloques
	    top.setDynamicValueSets("listado_cli", datos);
	    top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("PAGINAS", pags);

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}

}
