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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega el home
 * @author bbr
 */
public class ViewMonitorTrxMp extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int pag;
		long id_pedido 	  = -1;
		long id_estado    = -1;
		String tipo_fecha = "";
		String fecha_ini  = "";
		String fecha_fin  = "";
		String origen     = ""; //T:Todos, J:Jumbo.cl, VE:Venta Empresa
		String tipo_doc   = ""; //T:Todos, B:Boleta, F:Factura
		String MsjeError  = "";

		int regsperpage = 10;
		String param_id_pedido = "-1";
		String param_id_estado = "-1";
		String param_tipo_fecha= "";
		String param_fecha_ini = "";
		String param_fecha_fin = "";
		String mensaje_fracaso ="" ;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);
		
		if (getServletConfig().getInitParameter("MensajeFracaso") != null) {
			mensaje_fracaso = getServletConfig().getInitParameter("MensajeFracaso");	
			}	
		logger.debug("mensaje_fracaso: " + mensaje_fracaso);
		
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		
		// 2. Procesa parámetros del request
		if (req.getParameter("pag") != null) {
			pag = Integer.parseInt(req.getParameter("pag"));
			logger.debug("Pag: " + req.getParameter("pag"));
		} else {
			pag = 1; // por defecto mostramos la página 1
		}
		
		logger.debug("id_pedido: " + req.getParameter("id_pedido"));
		if ((req.getParameter("id_pedido") != null)&&(!req.getParameter("id_pedido").equals(""))) {			
			param_id_pedido = req.getParameter("id_pedido");
			id_pedido = Long.parseLong(param_id_pedido);
			
		}
		
		logger.debug("id_estado: " + req.getParameter("id_estado"));
		if ((req.getParameter("id_estado") != null)&&(!req.getParameter("id_estado").equals(""))) {
			
			param_id_estado = req.getParameter("id_estado");
			id_estado = Long.parseLong(param_id_estado);			
		}
		
		logger.debug("tipo_fecha: " + req.getParameter("tipo_fecha"));
		if ((req.getParameter("tipo_fecha") != null)&&(!req.getParameter("tipo_fecha").equals(""))) {			
			param_tipo_fecha = req.getParameter("tipo_fecha");
			tipo_fecha = param_tipo_fecha;			
		}
		
		logger.debug("fecha_ini: " + req.getParameter("fecha_ini"));
		if ((req.getParameter("fecha_ini") != null)&&(!req.getParameter("fecha_ini").equals(""))) {			
			param_fecha_ini = req.getParameter("fecha_ini");
			fecha_ini= param_fecha_ini;			
		}
		
		logger.debug("fecha_fin: " + req.getParameter("fecha_fin"));
		if ((req.getParameter("fecha_fin") != null)&&(!req.getParameter("fecha_fin").equals(""))) {
			logger.debug("fecha_fin: " + req.getParameter("fecha_fin"));
			param_fecha_fin = req.getParameter("fecha_fin");
			fecha_fin= param_fecha_fin;			
		}
		
		//***************************
		logger.debug("origen: " + req.getParameter("origen"));
		if ((req.getParameter("origen") != null) && (!req.getParameter("origen").equals(""))) {
			origen = req.getParameter("origen");
		}
		
		logger.debug("tipo_doc: " + req.getParameter("tipo_doc"));
		if ((req.getParameter("tipo_doc") != null) && (!req.getParameter("tipo_doc").equals(""))) {
		    tipo_doc = req.getParameter("tipo_doc");
		}

		
		
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
//		4.1 Trx. Medio de Pago
		List listaTrxMp = new ArrayList();
		TrxMpCriteriaDTO crit  =new TrxMpCriteriaDTO();
		crit.setLocal_fact(usr.getId_local());
		if (id_pedido != -1){
			crit.setId_pedido(id_pedido);
		}
		if (id_estado != -1){
			crit.setId_estado(id_estado);
		}
		if (!tipo_fecha.equals("")){
			crit.setTipo_fecha(tipo_fecha);
		}
		if (fecha_ini!=null && !fecha_ini.equals("")){
			logger.debug("fecha_ini:"+fecha_ini+" to sql:"+Formatos.formatFechaHoraIni(fecha_ini));
			crit.setFecha_ini(Formatos.formatFechaHoraIni(fecha_ini));
		}
		if (fecha_fin!=null && !fecha_fin.equals("")){
			logger.debug("fecha_fin:"+fecha_fin+" to sql:"+Formatos.formatFechaHoraFin(fecha_fin));
			crit.setFecha_fin(Formatos.formatFechaHoraFin(fecha_fin));
		}
		crit.setOrigen(origen);
		crit.setTipo_doc(tipo_doc);
		crit.setPag(pag);
		crit.setRegsperpag(regsperpage);
		
		if (id_pedido != -1 || id_estado != -1 || !tipo_fecha.equals("") || !fecha_ini.equals("") ||
		        !fecha_fin.equals("") || !origen.equals("") || !tipo_doc.equals("")){
			listaTrxMp = bizDelegate.getTrxMpByCriteria(crit);
		}else{
		    MsjeError = "Debe ingresar criterio de búsqueda";
		}
		
		if (listaTrxMp.size() < 1  && MsjeError.equals("")){
		    MsjeError = mensaje_fracaso;
		}else if (listaTrxMp.size() > 0){
		    MsjeError = "";
		    logger.debug("numero de trx:" + listaTrxMp.size());
		}
		
		ArrayList trx = new ArrayList();
		
		for (int i=0; i<listaTrxMp.size(); i++){
			IValueSet fila = new ValueSet();
			MonitorTrxMpDTO row = new MonitorTrxMpDTO();
			row = (MonitorTrxMpDTO)listaTrxMp.get(i);
			fila.setVariable("{trxmp_nro}"		,String.valueOf(row.getId_trxmp()));
			fila.setVariable("{trxmp_monto}"	,String.valueOf(row.getMonto_trxmp()));
			fila.setVariable("{trxmp_qtyprods}"	,String.valueOf(Formatos.formatoNum3Dec(row.getCant_prods())));
			fila.setVariable("{trxmp_estado}"	,String.valueOf(row.getEstado_nom()));
			fila.setVariable("{id_pedido}"		,String.valueOf(row.getId_pedido()));
			if(row.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
				fila.setVariable("{trxmp_tipo_doc}", "BOLETA");
				fila.setVariable("{trxmp_num_doc}", String.valueOf(row.getNum_doc()));
				fila.setVariable("{trxmp_img_factura}", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			}
			if(row.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
				fila.setVariable("{trxmp_tipo_doc}", "FACTURA");
				fila.setVariable("{trxmp_num_doc}", "--");
				int cantFacturas = bizDelegate.getCantFacturasIngresadas(row.getId_pedido());
				if (row.getEstado_nom().equals("Pagada")){
				    if (cantFacturas > 0){
				        fila.setVariable("{trxmp_img_factura}", "<a title=\"imprimir\" href=\"javascript:popUpWindow('ViewPrint?url='+escape('ViewActListFacturas?id_pedido=" + row.getId_pedido() + "'), 200, 250, 350, 280)\"><img src='img/ico_factura2.gif' width='16' height='16' border='0' alt='Ingresar Facturas'></a>");
				    }else{
				        fila.setVariable("{trxmp_img_factura}", "<a title=\"imprimir\" href=\"javascript:popUpWindow('ViewPrint?url='+escape('ViewActListFacturas?id_pedido=" + row.getId_pedido() + "'), 200, 250, 350, 280)\"><img src='img/ico_factura.gif' width='16' height='16' border='0' alt='Ingresar Facturas'></a>");
				    }
				}else{
				    fila.setVariable("{trxmp_img_factura}", "<img src='img/ico_factura.gif' width='16' height='16' border='0' alt='Ingresar Facturas'>");
				}
				
				/*if (row.getPos_monto_fp() > 0.0 && cantFacturas > 0){
					fila.setVariable("{trxmp_img_factura}", "<a title=\"imprimir\" href=\"javascript:popUpWindow('ViewPrint?url='+escape('ViewActListFacturas?id_pedido=" + row.getId_pedido() + "'), 200, 250, 350, 280)\"><img src='img/ico_factura2.gif' width='16' height='16' border='0' alt='Ingresar Facturas'></a>");
				}else if (row.getPos_monto_fp() > 0.0 && cantFacturas == 0){
					fila.setVariable("{trxmp_img_factura}", "<a title=\"imprimir\" href=\"javascript:popUpWindow('ViewPrint?url='+escape('ViewActListFacturas?id_pedido=" + row.getId_pedido() + "'), 200, 250, 350, 280)\"><img src='img/ico_factura.gif' width='16' height='16' border='0' alt='Ingresar Facturas'></a>");
				}else if (row.getPos_monto_fp() == 0.0){
					fila.setVariable("{trxmp_img_factura}", "<img src='img/ico_factura.gif' width='16' height='16' border='0' alt='Ingresar Facturas'>");
				}else{
					fila.setVariable("{trxmp_img_factura}", "<a title=\"imprimir\" href=\"javascript:popUpWindow('ViewPrint?url='+escape('ViewActListFacturas?id_pedido=" + row.getId_pedido() + "'), 200, 250, 350, 280)\"><img src='img/ico_factura.gif' width='16' height='16' border='0' alt='Ingresar Facturas'></a>");
				}*/
			}
			
			//nuevas columnas
			fila.setVariable("{trxmp_num_caja}", String.valueOf(row.getNum_caja()));
			fila.setVariable("{trxmp_pos_monto_fp}", String.valueOf(row.getPos_monto_fp()));
			if(	row.getPos_fecha()!=null){ fila.setVariable("{trxmp_pos_fecha}", String.valueOf(row.getPos_fecha()));}
			else{	fila.setVariable("{trxmp_pos_fecha}"	,Constantes.SIN_DATO); }
			if(	row.getPos_hora()!=null){ fila.setVariable("{trxmp_pos_hora}", String.valueOf(row.getPos_hora()));}
			else{	fila.setVariable("{trxmp_pos_hora}"	,Constantes.SIN_DATO); }
			if(	row.getPos_fp()!=null){ fila.setVariable("{trxmp_pos_fp}", String.valueOf(row.getPos_fp()));}
			else{	fila.setVariable("{trxmp_pos_fp}", Constantes.SIN_DATO); }
			fila.setVariable("{fecha_picking}", row.getFecha_picking());
			fila.setVariable("{fecha_despacho}", row.getFecha_despacho());
			fila.setVariable("{edit}", "0"); //0: NO Editable,  1:Editable
			
			if (row.getOrigen().equals(Constantes.ORIGEN_VE_CTE)){
			    fila.setVariable("{color_fila}", "#FFAC59"); //Color Venta Empresa
			}else if (row.getOrigen().equals(Constantes.ORIGEN_WEB_CTE)){
			    fila.setVariable("{color_fila}", "#FFFFFF"); //Color Jumbo.cl
			}
			
			trx.add(fila);
		}
		

//		 4.2 Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = 0;
		
		if (id_pedido != -1 || id_estado != -1 || !tipo_fecha.equals("") || !fecha_ini.equals("") ||
		        !fecha_fin.equals("") || !origen.equals("") || !tipo_doc.equals("")){
		    tot_reg = bizDelegate.getCountTrxMpByCriteria(crit);
		}
		if (tot_reg == 0){
			top.setVariable("{dis1}", "disabled");
		}else{
			top.setVariable("{dis1}", "");
		}
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		logger.debug("round: " + (double) Math.ceil(tot_reg / regsperpage));

		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag)
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			pags.add(fila);
		}
		//anterior y siguiente
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
		logger.debug("antes de estado");
		
		
		//4.3 Estados Trx Mp		
		List listaestados = bizDelegate.getEstadosTrxMp();

		ArrayList estados = new ArrayList();
		for (int i = 0; i < listaestados.size(); i++) {
			IValueSet fila = new ValueSet();
			EstadoDTO estado1 = new EstadoDTO();
			estado1 = (EstadoDTO) listaestados.get(i);			
			fila.setVariable("{id_estado}", estado1.getId_estado() + "");
			fila.setVariable("{estado_nom}", estado1.getNombre());

			if (Long.toString(id_estado) != null
					&& Long.toString(id_estado).equals(String.valueOf(estado1.getId_estado())))
				fila.setVariable("{sel_estado}", "selected");
			else
				fila.setVariable("{sel_estado}", "");
			
			estados.add(fila);
		}
		logger.debug("despues de estado");
		
		
		
		//4.4.2 fecha ini
		
		if (fecha_ini!=null && !fecha_ini.equals("")){
			top.setVariable("{fec_ini}",fecha_ini);
		}else{
			top.setVariable("{fec_ini}","");
		}
		
		//4.4.3 fecha fin
		if (fecha_fin!=null && !fecha_fin.equals("")){
			top.setVariable("{fec_fin}",fecha_fin);
		}else{
			top.setVariable("{fec_fin}","");
		}
		//*************************************************************
		//4.4.4 Tipo Documento
		ArrayList listado_tipo_doc = new ArrayList();
		IValueSet filatd1 = new ValueSet();
		//tipo_doc
		//boleta
		filatd1.setVariable("{valor_td}", "B");
		filatd1.setVariable("{nombre_td}", "Boleta");
		if (tipo_doc.equals(Constantes.TIPO_DOC_BOLETA)){
		    filatd1.setVariable("{estado_td}", "selected");
		}
		listado_tipo_doc.add(filatd1);
		
		//factura
		IValueSet filatd2 = new ValueSet();
		filatd2.setVariable("{valor_td}", "F");
		filatd2.setVariable("{nombre_td}", "Factura");
		if (tipo_doc.equals(Constantes.TIPO_DOC_FACTURA)){
		    filatd2.setVariable("{estado_td}", "selected");
		}
		listado_tipo_doc.add(filatd2);
		
		
		if (tipo_doc!=null && !tipo_doc.equals("")){
			top.setVariable("{sel_tipo_doc}", tipo_doc);
		}else{
			top.setVariable("{sel_tipo_doc}","");
		}
		
		
		//4.4.5 Origen
		
		ArrayList listado_origen = new ArrayList();
		IValueSet filao1 = new ValueSet();
		//origen
		//Jumbo.cl
		filao1.setVariable("{valor_o}", Constantes.ORIGEN_WEB_CTE);
		filao1.setVariable("{nombre_o}", Constantes.ORIGEN_WEB_TXT);
		if (origen.equals(Constantes.ORIGEN_WEB_CTE)){
		    filao1.setVariable("{estado_o}", "selected");
		}
		listado_origen.add(filao1);
		
		//Venta Empresas
		IValueSet filao2 = new ValueSet();
		filao2.setVariable("{valor_o}", Constantes.ORIGEN_VE_CTE);
		filao2.setVariable("{nombre_o}", Constantes.ORIGEN_VE_TXT);
		if (origen.equals(Constantes.ORIGEN_VE_CTE)){
		    filao2.setVariable("{estado_o}", "selected");
		}
		listado_origen.add(filao2);

		
		if (origen!=null && !origen.equals("")){
			top.setVariable("{sel_origen}", origen);
		}else{
			top.setVariable("{sel_origen}","");
		}
		
		
		
		// 5. Setea variables del template
		// 5.1 id_pedido
		if(!param_id_pedido.equals("-1")){
			top.setVariable("{id_pedido}", param_id_pedido);
		}else{
			top.setVariable("{id_pedido}", "");
		}
		
		//5.2 id_estado
		if(!param_id_estado.equals("-1")){
			top.setVariable("{id_estado}", param_id_estado);
		}else{
			top.setVariable("{id_estado}", "");
		}
		
		//5.3 tipo_fecha
		if (tipo_fecha.equals("P")){
			top.setVariable("{check_fec_pik}","checked");
			top.setVariable("{tipo_fecha}",tipo_fecha);
		}else if (tipo_fecha.equals("D")){
			top.setVariable("{check_fec_desp}","checked");
			top.setVariable("{tipo_fecha}",tipo_fecha);			
		}else{
			top.setVariable("{check_fec_pik}","");
			top.setVariable("{check_fec_desp}","");
			top.setVariable("{tipo_fecha}","");
		}
		
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		

		top.setVariable("{mensaje}",MsjeError);
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado_estados_trxmp", estados);
		top.setDynamicValueSets("listado_trxmp", trx);
		top.setDynamicValueSets("listado_tipo_doc", listado_tipo_doc);
		top.setDynamicValueSets("listado_origen", listado_origen);
		top.setDynamicValueSets("paginador", pags);
		
		logger.debug("User: " + usr.getLogin());
		logger.debug("IdLocal: " + usr.getId_local());
		logger.debug("IdPerfil: " + usr.getId_perfil());
		
		
		// 7. Salida Final		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
