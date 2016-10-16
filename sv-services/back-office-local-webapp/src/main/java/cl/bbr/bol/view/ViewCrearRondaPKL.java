package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario para la creación de una ronda de picking
 * @author jsepulveda
 */
public class ViewCrearRondaPKL extends Command {
	
	private boolean Ultima_op = false;
	private long Max_prod = 0;
	private long Max_op = 0;
	private long Min_op_fill = 0;
	
	
	private boolean isUltima_op() {return Ultima_op;}
	private void setUltima_op(boolean ultima_op) {Ultima_op = ultima_op;}
	public long getMax_op() {return Max_op;}
	public void setMax_op(long max_op) {Max_op = max_op;}
	public long getMax_prod() {return Max_prod;}
	public void setMax_prod(long max_prod) {Max_prod = max_prod;}
	public long getMin_op_fill() {return Min_op_fill;}
	public void setMin_op_fill(long min_op_fill) {Min_op_fill = min_op_fill;}
	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
		long   id_pedido 		= -1;
		long   id_jornada		= -1;
		String param_id_pedido	= "";
		String param_id_op		= "";
		String param_id_jornada	= "";
		String msg				= "";
		String rc				= "";
		String param_mjeError	= "";
		String paramTipo_VE     = "";
		String zona_anterior    = "";
		long   id_zona          = -1;
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		String msjError = getServletConfig().getInitParameter("MsjError");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		// 2.1 Id Sector
		if ( req.getParameter("id_pedido") != null ){
		    param_id_pedido = req.getParameter("id_pedido");
			id_pedido = Long.parseLong(param_id_pedido);
		}

		// 2.2 Id Jornada
	    if ( req.getParameter("id_jornada") == null ){
			throw new ParametroObligatorioException("id_jornada es null");
		}
	    param_id_jornada = req.getParameter("id_jornada");		
		try {
			id_jornada = Long.parseLong(param_id_jornada);
		}
		catch ( NumberFormatException e){
			logger.error( "No se pudo convertir id_jornada a long" );
			throw new SystemException("No se pudo convertir id_jornada a long" , e);
		}
		
		// 2.3 Tipo_ve
		
		if ( req.getParameter("tipo_ve") != null ){
			paramTipo_VE = req.getParameter("tipo_ve");
			logger.debug("tipo_ve:"+paramTipo_VE);
		}
		
		//mensaje de error
		if ( req.getParameter("rc") != null ){
			rc  = req.getParameter("rc");
			
		}
		if ( req.getParameter("msje") != null ){
			param_mjeError = req.getParameter("msje");
			
		}
		if ( req.getParameter("id_zona") != null && !req.getParameter("id_zona").equals("") ){
			id_zona= Long.parseLong(req.getParameter("id_zona"));
		}
	    
		// 3. Template
		View salida = new View(res);		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		
		// 4.  Rutinas Dinámicas
		
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		
		//Listado de Zonas
		List listaZonas = bizDelegate.getZonasLocal(usr.getId_local());
		ArrayList zona = new ArrayList();
		for (int i = 0; i < listaZonas.size(); i++) {
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = new ZonaDTO();
			zona1 = (ZonaDTO) listaZonas.get(i);
			fila.setVariable("{id_zona}" , String.valueOf(zona1.getId_zona()));
			fila.setVariable("{nom_zona}", zona1.getNombre());
			if (id_zona == zona1.getId_zona()){
				fila.setVariable("{sel_zona}", "selected");
			}else{
				fila.setVariable("{sel_zona}", "");
			}
			zona.add(fila);
		}
		
		//4.2 listado de rondas
		List listapedidos = new ArrayList();
		PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
		criterio.setId_pedido(id_pedido);
		criterio.setId_jpicking(id_jornada);
		criterio.setId_zona(id_zona);
		criterio.setId_local(usr.getId_local());
		listapedidos = bizDelegate.getPedidosXJornada(criterio);


		/*if ( listarondas.size() == 0 )
			msg = msjError;
			top.setVariable("{disabled}","disabled");*/
		/*if(id_sector == -1){
			msg="Debe Seleccionar un Sector";
			top.setVariable("{disabled}","disabled");
		}*/
		zona_anterior="";
		double acumulador = 0.0;
		double cantidad_asig_teorica = 0.0;
		double cant_asig_real = 0.0;
		List rondas = new ArrayList();
		List lista_prods = new ArrayList();
		for (int i = 0; i < listapedidos.size(); i++) {
			IValueSet fila = new ValueSet();
			RondaPropuestaDTO ronda1 = (RondaPropuestaDTO) listapedidos.get(i);
			setUltima_op(false);
			

			// finalmente genera la asignacion
			fila.setVariable("{hora_inicio}", ronda1.getH_inicio());
			fila.setVariable("{hora_fin}"	, ronda1.getH_fin());
			fila.setVariable("{id_jornada}"	, String.valueOf(ronda1.getId_jornada()));
			fila.setVariable("{comuna}"	    , String.valueOf(ronda1.getComuna()));
			fila.setVariable("{id_pedido}"	, String.valueOf(ronda1.getId_op()));
			fila.setVariable("{cant_prod}"	, String.valueOf(ronda1.getCant_prods()));
			//fila.setVariable("{cant}"		, String.valueOf(ronda1.getCant_prods())+"-"+cant_asig_real);	
			fila.setVariable("{cant}"		, String.valueOf(cant_asig_real));
			fila.setVariable("{i}"			, String.valueOf(i));
			//fila.setVariable("{id_sector}", String.valueOf(id_sector));			
			fila.setVariable("{zona}"		, String.valueOf(ronda1.getZona()));
			
			if(cant_asig_real>0.0){
			    fila.setVariable("{chekear}", "checked"); 
			}else{
				fila.setVariable("{chekear}", "");
			}
			
			if (ronda1.getTipo_despacho() == null){
			    ronda1.setTipo_despacho("N");
			}
			if (ronda1.getTipo_despacho().equals("E")){//Express
			    fila.setVariable("{color_fondo_op}","#F7BB8B");
			}else if (ronda1.getTipo_despacho().equals("C")){//Económico
			    fila.setVariable("{color_fondo_op}","#C4FFC4");
			}else{
			    fila.setVariable("{color_fondo_op}","#FFFFFF");
			}

			//borde destacado  x c zona
			String zona_actual=ronda1.getZona();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			
			rondas.add(fila);
		}
		
		//4.3 OP's
		ArrayList ops = new ArrayList();
		for(int i = 0; i< listapedidos.size();i++){
			IValueSet fila = new ValueSet();
			RondaPropuestaDTO op1 = (RondaPropuestaDTO) listapedidos.get(i);
			fila.setVariable("{id_pedido}"	, String.valueOf(op1.getId_op()));
			if (param_id_op != null && param_id_op.equals(String.valueOf(op1.getId_op())))
				fila.setVariable("{sel}","selected");
			else				
				fila.setVariable("{sel}","");
			
			ops.add(fila);
		}
		
		// 5. Setea variables del template
		top.setVariable("{num_ped_sel}","");
		top.setVariable("{num_pro_sel}","");
		//top.setVariable("{id_sector}", param_id_sector);
		top.setVariable("{id_jornada}", param_id_jornada);
		top.setVariable("{tipo_ve}", paramTipo_VE);
		top.setVariable("{msg}",msg);
		if(!rc.equals("")){
			top.setVariable("{mjeError}","<script language='JavaScript'>alert('"+param_mjeError+"');</script>");
		}else{
			top.setVariable("{mjeError}","");
		}
		if(listapedidos.size()>0){
			top.setVariable("{disabled}","enabled");
		}
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_crea_ronda1", rondas);
		top.setDynamicValueSets("select_num_op", ops);		
		//top.setDynamicValueSets("select_sector", seccion);
		top.setDynamicValueSets("select_zonas", zona);		
		
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}

	

	
	
}
