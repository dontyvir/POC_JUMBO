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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario para la creación de una ronda de picking
 * @author jsepulveda
 */
public class ViewCrearRonda extends Command {
	
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
	
	private double calcPropuestaTeorica(long id_pedido, int indice, double cantxopi, double acumulador ){		
		double res=0.0;
//		double valor_op_fill=0.0;
		
		logger.debug("propuesta teorica op:"+id_pedido+" indice:"+indice);
		logger.debug("max_prod:"+getMax_prod());
		logger.debug("max_op:"+getMax_op());
		logger.debug("min_op_fill:"+getMin_op_fill());		
		logger.debug("cantxopi:"+cantxopi);
		logger.debug("acumulador:"+acumulador);
		
		//si supera el maximo de OPs por ronda asigna 0.0 a las demas
		if (indice > getMax_op()){
			logger.debug("ya se asigno cantidad para todas las op ("+getMax_op()+") que acepta la ronda");
			return 0.0;
		}
		
		//revisa MAX_PROD
		if (cantxopi + acumulador > getMax_prod()){
			res = getMax_prod() - acumulador;
			setUltima_op(true);
			logger.debug("asigna op: max_prod - acumulador = "+res);
			}
		else {
			res = cantxopi;
			setUltima_op(false);
			logger.debug("asigna op: cantxopi = "+res);
		}
		logger.debug("flag ultima op:" + Ultima_op);
		
		
		//Revisa MAX_OP (si se cumple que es la ultima OP) si ya se asignaron todos los productos no entra
		/*
		if ( ( isUltima_op() ||(indice == max_op))&&(res!=0.0) ){
			valor_op_fill = (double)(min_op_fill)/100 *cantxopi;
			logger.debug("valor_op_fill = min_op_fill("+min_op_fill+")/100 * cantxopi("+cantxopi+")");
			logger.debug("valor_op_fill = "+valor_op_fill);
			if (valor_op_fill > res ){
				res=valor_op_fill;
				logger.debug("asigna op: valor_op_fill = "+res);
			}
		}
		*/
		
		
		return res;
	}
	
	private double calcPropuestaReal(double acumulador, double cantxopi, long id_pedido, double cant_teorica, List lista_prods){
		double res=0.0;
		double cota_baja 			= 0.0;
		double cota_alta 			= 0.0;
		double delta_bajo 			= 0.0;
		double delta_alto 			= 0.0;
		double cant_spick_acum 		= 0.0;
		double diff_maxp_zero		= 0.0;
		double diff_minopf_maxp 	= 0.0;
		double valor_op_fill 		= 0.0;
		
		// obtiene las cantidades spick de los productos en forma ascendente
		logger.debug("propuesta real op:"+id_pedido+" cant_teorica:"+cant_teorica + " acumulado:"+acumulador);
		
		// si es la ultima OP
		if (isUltima_op()){
			//debe tomar 0.0 para la ultima op o el MIN_OP_FILL dependiendo del MAX_PROD
			logger.debug("Es la ultima OP cantxop:"+cantxopi+", sobrescribe el teorico (actual="+cant_teorica+")");
			valor_op_fill = (double)(getMin_op_fill())/100 *cantxopi;			
			diff_maxp_zero = (getMax_prod()-acumulador) - 0.0;			
			diff_minopf_maxp = valor_op_fill - (getMax_prod()-acumulador);
			logger.debug("valor_op_fill    :(min_op_fi["+getMin_op_fill()+"%]/100 * cantxopi["+cantxopi+"])="+valor_op_fill);
			logger.debug("diff_maxp_zero   :((max_prod ["+getMax_prod()+"]-acumulador["+acumulador+"]) -0.0)   ="+diff_maxp_zero);
			logger.debug("diff_minopf_maxp :(valor_op_fill["+valor_op_fill+"] - (max_prod ["+getMax_prod()+"]-acumulador["+acumulador+"])) ="+diff_minopf_maxp);
			if (diff_minopf_maxp>diff_maxp_zero){				
				cant_teorica = 0.0;
				logger.debug("Es la ultima OP, sobrescribe el real final ="+0.0);
				return 0.0;
			}
            
            cant_teorica = valor_op_fill;
            logger.debug("Es la ultima OP, sobrescribe el teorico final ="+cant_teorica);
		}
		
		String msg_log ="";
		for (int i = 0; i < lista_prods.size(); i++) {
			msg_log = "";
			ProductosPedidoDTO producto = (ProductosPedidoDTO) lista_prods.get(i);
			cant_spick_acum += producto.getCant_spick();
			//logger.debug("op:"+id_pedido +" cantidad spick:"+producto.getCant_spick()+" Acumulado:"+cant_spick_acum);
			msg_log = "op:"+id_pedido +" cantidad spick:"+producto.getCant_spick()+" Acumulado:"+cant_spick_acum;
			if (cant_teorica > cant_spick_acum){
				cota_baja = cant_spick_acum;
				//logger.debug("nueva cota_baja : "+cota_baja);
				msg_log += " nueva cota_baja : "+cota_baja;
				logger.debug(msg_log);
			}else { //si son iguale o cant_teorica es menor
				cota_alta = cant_spick_acum; 
				//logger.debug("nueva cota_alta :"+cota_alta);
				msg_log += " nueva cota_alta :"+cota_alta;
				logger.debug(msg_log);
				break;
			}
		}
		
		logger.debug("finaliza con cota_baja="+cota_baja+" cota_alta="+cota_alta);
		delta_alto = cota_alta - cant_teorica;
		delta_bajo = cant_teorica - cota_baja;
		logger.debug("deltas alto:"+delta_alto+" bajo:"+delta_bajo);
		if ((delta_alto>delta_bajo)&&(cota_baja!=0.0)){
			res = cota_baja;
			logger.debug("elije cota_baja : "+cota_baja);
		}else{
			res = cota_alta;
			logger.debug("elije cota_alta :"+cota_alta);
		}
		return res;
	}
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
		long 	id_sector 			= -1;
		long	id_jornada			= -1;
		String	param_id_sector		= "";
		String	param_id_op			= "";
		String	param_id_jornada	= "";
		String  msg					= "";
		String  rc					= "";
		String  param_mjeError		= "";
		String  paramTipo_VE = "";
		String zona_anterior="";
		long    id_zona =-1;
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		String msjError = getServletConfig().getInitParameter("MsjError");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		// 2.1 Id Sector
		if ( req.getParameter("id_sector") != null ){
			param_id_sector = req.getParameter("id_sector");
			id_sector = Long.parseLong(param_id_sector);
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
		
		// 4.1 Sectores				
		List listasecciones = bizDelegate.getSectores();
		ArrayList seccion = new ArrayList();
		for (int i = 0; i < listasecciones.size(); i++) {
			IValueSet fila = new ValueSet();
			SectorLocalDTO sector1 = new SectorLocalDTO();
			sector1 = (SectorLocalDTO) listasecciones.get(i);
			fila.setVariable("{id_sector}"	, String.valueOf(sector1.getId_sector()));
			fila.setVariable("{sector}"	, sector1.getNombre());		

			if (param_id_sector != null
					&& param_id_sector.equals(
							String.valueOf(sector1.getId_sector()))){
				
				fila.setVariable("{sel1}", "selected");
				
				//setea parametros para propuestas
				setMax_prod(sector1.getMax_prod());
				setMax_op(sector1.getMax_op());
				setMin_op_fill(sector1.getMin_op_fill());
				logger.debug("parametros sector     ="+sector1.getId_sector());
				logger.debug("parametros MAX_PROD   ="+getMax_prod());
				logger.debug("parametros MAX_OP     ="+getMax_op());
				logger.debug("parametros MIN_OP_FILL="+getMin_op_fill());
			}else {
				fila.setVariable("{sel1}", "");
			}
			seccion.add(fila);
		}
		
		//Listado de Zonas
		List listaZonas = bizDelegate.getZonasLocal(usr.getId_local());
		ArrayList zona = new ArrayList();
		for (int i = 0; i < listaZonas.size(); i++) {
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = new ZonaDTO();
			zona1 = (ZonaDTO) listaZonas.get(i);
			fila.setVariable("{id_zona}"	, String.valueOf(zona1.getId_zona()));
			fila.setVariable("{nom_zona}"	, zona1.getNombre());
			if (id_zona == zona1.getId_zona()){
				fila.setVariable("{sel_zona}"	, "selected");
			}else{
				fila.setVariable("{sel_zona}"	, "");
			}
			zona.add(fila);
		}
		
		//4.2 listado de rondas
		List listarondas = new ArrayList();
		
		if (id_sector != -1){
			ProcRondasPropuestasDTO criterio = new ProcRondasPropuestasDTO();
			criterio.setId_local(usr.getId_local());
			criterio.setId_jornada(id_jornada);
			criterio.setId_sector(id_sector);
			criterio.setId_zona(id_zona);
			if (paramTipo_VE.equals(Constantes.TIPO_VE_SPECIAL_CTE))
					criterio.setTipo_ve(Constantes.TIPO_VE_SPECIAL_CTE);
			else
				criterio.setTipo_ve(Constantes.TIPO_VE_NORMAL_CTE);
			listarondas = bizDelegate.getRondasPropuestas(criterio);
		}
		if ( listarondas.size() == 0 ){
			msg = msjError;
			top.setVariable("{disabled}","disabled");
		}
		if(id_sector == -1){
			msg="Debe Seleccionar un Sector";
			top.setVariable("{disabled}","disabled");
		}	
		zona_anterior="";
		double acumulador = 0.0;
		double cantidad_asig_teorica = 0.0;
		double cant_asig_real = 0.0;
		List rondas = new ArrayList();
		List lista_prods = new ArrayList();
		for (int i = 0; i < listarondas.size(); i++) {
			IValueSet fila = new ValueSet();
			RondaPropuestaDTO ronda1 = (RondaPropuestaDTO) listarondas.get(i);
			setUltima_op(false);
			//---- propuesta teorica
			cantidad_asig_teorica = calcPropuestaTeorica(ronda1.getId_op(), i+1, ronda1.getCant_prods(), acumulador );
			if (lista_prods!=null){
				lista_prods.clear();
			}
			/* conversar si se añade que el MAX_PROD mande la sensibilidad de llenado de la ultima OP, dado que anula el param MAX_OP_FILL*/
			/*
			if ( isUltima_op() && ((acumulador+cantidad_asig_teorica) >getMax_prod())){
				logger.debug("Es la ultima OP, la cantidad asignada sobrepasa el max_prod ");
				logger.debug("cambia  cant_teorica " + cantidad_asig_teorica + " por max_prod["+ getMax_prod() +"] - acumulador ["+ acumulador +"]");				
				cantidad_asig_teorica = getMax_prod() - acumulador;
				logger.debug("por     cant_teorica " + cantidad_asig_teorica);				
			}*/
			
			
			//---- propuesta real
			//lista_prods = bizDelegate.getProdPedidoXSector(ronda1.getId_op(), id_sector, usr.getId_local());
			lista_prods = bizDelegate.getProdSinPickearXPedidoXSector(ronda1.getId_op(), id_sector, usr.getId_local());
			if (cantidad_asig_teorica>0.0){
				cant_asig_real = calcPropuestaReal(acumulador, ronda1.getCant_prods(),ronda1.getId_op(), cantidad_asig_teorica, lista_prods);				
			}else{
				cant_asig_real=0.0;
			}
			
			acumulador+=cantidad_asig_teorica;
			
			// finalmente genera la asignacion
			fila.setVariable("{hora_inicio}"	, ronda1.getH_inicio());
			fila.setVariable("{hora_fin}"		, ronda1.getH_fin());
			fila.setVariable("{id_jornada}"		, String.valueOf(ronda1.getId_jornada()));
			fila.setVariable("{comuna}"		    , String.valueOf(ronda1.getComuna()));
			fila.setVariable("{id_pedido}"		, String.valueOf(ronda1.getId_op()));
            fila.setVariable("{v_w}"            , tipoOrigen(ronda1.getOrigenPedido()));
			fila.setVariable("{sector}"			, ronda1.getSector());
			fila.setVariable("{cant_prod}"	    , String.valueOf(ronda1.getCant_prods()));
			if (ronda1.getProd_SPick_con_ant().equals("S")){
			    fila.setVariable("{cant_prod_view}"	, "<font color='#FF0000'><b>" + String.valueOf(ronda1.getCant_prods()) + "</b></font>");
			}else{
			    fila.setVariable("{cant_prod_view}"	, String.valueOf(ronda1.getCant_prods()));    
			}
			//fila.setVariable("{cant}"			, String.valueOf(ronda1.getCant_prods())+"-"+cant_asig_real);	
			fila.setVariable("{cant}"			, String.valueOf(cant_asig_real));
			fila.setVariable("{i}"				, String.valueOf(i));
			fila.setVariable("{id_sector}"		, String.valueOf(id_sector));			
			fila.setVariable("{zona}"		    , String.valueOf(ronda1.getZona()));
			
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
			
			/*List lst_prod= bizDelegate.getProdPedidoXSector(ronda1.getId_op(), id_sector, usr.getId_local());
			int cant = 0;			
			for (int j = 0; j < lst_prod.size(); j++) {
				ProductosPedidoDTO prod = (ProductosPedidoDTO) lst_prod.get(j);
				if(prod.getCant_spick()!=0){
					cant = cant+1;
				}
			}*/
			fila.setVariable("{cant_prod_distintos}", lista_prods.size()+"");
			logger.debug("cant productos distintos: "+lista_prods.size());
			rondas.add(fila);
		}
		
		//4.3 OP's
		ArrayList ops = new ArrayList();
		for(int i = 0; i< listarondas.size();i++){
			IValueSet fila = new ValueSet();
			RondaPropuestaDTO op1 = (RondaPropuestaDTO) listarondas.get(i);
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
		top.setVariable("{id_sector}", param_id_sector);
		top.setVariable("{id_jornada}", param_id_jornada);
		top.setVariable("{tipo_ve}", paramTipo_VE);
		top.setVariable("{msg}",msg);
		if(!rc.equals("")){
			top.setVariable("{mjeError}","<script language='JavaScript'>alert('"+param_mjeError+"');</script>");
		}else
			top.setVariable("{mjeError}","");
		if(listarondas.size()>0){
			top.setVariable("{disabled}","enabled");
		}
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_crea_ronda1", rondas);
		top.setDynamicValueSets("select_num_op", ops);		
		top.setDynamicValueSets("select_sector", seccion);
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
    /**
     * @param origenPedido
     * @return
     */
    private String tipoOrigen(String origenPedido) {
        if ( "W".equalsIgnoreCase( origenPedido ) )
            return "WEB";
        if ( "V".equalsIgnoreCase( origenPedido ) )
            return "V.E.";
        return "";
    }

	

	
	
}
