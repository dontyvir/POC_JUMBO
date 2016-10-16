package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDetalleFODTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * GeneraPedido Comando Process
 * Agrega un nuevo pedido a partir de datos de una cotizacion
 * 
 * @author BBRI
 */

public class GeneraPedido extends Command {
	private final static long serialVersionUID = 1;
 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	
	     // 1. seteo de Variables del método
	     String paramUrl 			= "";
	     long 	paramId_cotizacion	= -1;
	     long 	paramId_loc_desp	= -1;
	     long 	paramId_jornada		= -1;
	     long 	paramId_loc_fact	= -1;
	     double paramPorc_desp		=  0;
	     String paramComentario		= "";
	     List 	indices 	= new ArrayList();
	     String paramTipoVe 		= "";
	     long   paramId_Politica	= 0;
	     double costo_desp          = 0;
	     long local = 0;
	     long jornada = 0;
	     String mod="0";
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_cotizacion") == null ){throw new ParametroObligatorioException("id_cotizacion es null");}
	     if ( req.getParameter("sel_loc_desp") == null ){throw new ParametroObligatorioException("sel_loc_desp es null");}
	     if ( req.getParameter("id_jornada") == null ){throw new ParametroObligatorioException("id_jornada es null");}
	     if ( req.getParameter("sel_loc_fac") == null ){throw new ParametroObligatorioException("sel_loc_fac es null");}
	     if ( req.getParameter("porc_desp") == null ){throw new ParametroObligatorioException("porc_desp es null");}
	     
	     //if ( req.getParameter("sustitutos") == null ){throw new ParametroObligatorioException("sustitutos es null");}
	     
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl 			= req.getParameter("url");
	     paramId_cotizacion	= Long.parseLong(req.getParameter("id_cotizacion"));
	     paramId_loc_desp	= Long.parseLong(req.getParameter("sel_loc_desp"));
	     paramId_jornada	= Long.parseLong(req.getParameter("id_jornada"));
	     paramId_loc_fact	= Long.parseLong(req.getParameter("sel_loc_fac"));
	     paramPorc_desp		= Double.parseDouble(req.getParameter("porc_desp"));
	     paramId_Politica	= Long.parseLong(req.getParameter("sel_politica"));
	     paramComentario    = req.getParameter("comentario");
	     paramTipoVe = req.getParameter("sel_tipo_ve");
	     if (req.getParameter("jornada") != null) jornada = Long.parseLong(req.getParameter("jornada"));
	     if (req.getParameter("local") != null) local = Long.parseLong(req.getParameter("local"));	     
	     if (req.getParameter("mod") != null) mod = req.getParameter("mod");
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_cotizacion	: " + paramId_cotizacion);
	     logger.debug("sel_loc_desp		: " + paramId_loc_desp);
	     logger.debug("id_jornada		: " + paramId_jornada);
	     logger.debug("sel_loc_fac		: " + paramId_loc_fact);
	     logger.debug("porc_desp		: " + paramPorc_desp);
	     logger.debug("sel_tipo_ve		: " + paramTipoVe);
	     logger.debug("pol_id			: " + paramId_Politica);
	     logger.debug("mod  			: " + mod);
	     
	     //procesa los productos seleccionados
		 Enumeration enume = req.getParameterNames();
		 while (enume.hasMoreElements()) {
			String name = (String)enume.nextElement();
			if (name.startsWith("chkb_")){
				if (req.getParameter(name).equals("on")){
					String n = name.replaceFirst("chkb_","");
					indices.add(n);					
				}
			}
		 }
		 //iteramos sobre los indices para crear mapa
		 List lista = new ArrayList();
		 for (int i=0; i<indices.size(); i++){
		 	String idx = (String)indices.get(i);
		 	String str_det_prod_id 	= req.getParameter("det_prod_id_bo_" + idx);
		 	String str_det_id_fo	= req.getParameter("det_prod_id_fo_" + idx);
		 	String str_porc_dscto 	= req.getParameter("porc_dscto_" + idx);
		 	String str_precio_lista	= req.getParameter("precio_lista_" + idx);
			String str_cant			= req.getParameter("cant_" + idx);
				
			long 	prod_id 	= Long.parseLong(str_det_prod_id);
			long 	prod_id_fo 	= Long.parseLong(str_det_id_fo);
			double 	porc_dscto	= Double.parseDouble(str_porc_dscto);
			double 	prec_lista	= Double.parseDouble(str_precio_lista);
			double 	cant		= Double.parseDouble(str_cant);		
			
			//setear cada detalle elegido
			ProcInsPedidoDetalleFODTO det = new ProcInsPedidoDetalleFODTO();
	  		det.setId_producto_fo(prod_id_fo);
	  		det.setId_prod_bo(prod_id);
	  		det.setCant_solic(cant);
	  	//	det.setPrecio_unitario(prec_lista*(1-porc_dscto)/100);
	  		det.setPrecio_unitario((prec_lista * (1- (porc_dscto/100))));
	  		det.setPrecio_lista(prec_lista);
	  		det.setDscto_item(porc_dscto);
	  		lista.add(det);
		}
	     
	     //ForwardParameters fp = new ForwardParameters();
	     /*
	      * 3. Procesamiento Principal
	      */
	  		BizDelegate biz = new BizDelegate();
	  		CotizacionesDTO cot = biz.getCotizacionesById(paramId_cotizacion);
	  		ProcInsPedidoDTO pedido = new ProcInsPedidoDTO ();
	  		pedido.setId_cotizacion(paramId_cotizacion);
	  		pedido.setId_cliente(cot.getCot_cli_id());
	  		pedido.setId_jdespacho(paramId_jornada);
	  		pedido.setId_local_desp(paramId_loc_desp);
	  		pedido.setId_local_fact(paramId_loc_fact);
	  		pedido.setMedio_pago(cot.getCot_mpago());
	  		pedido.setFecha_exp(cot.getFecha_expira());
	  		pedido.setN_cuotas(cot.getCot_ncuotas());
	  		pedido.setNum_mp(cot.getCot_num_mpago());
            if ( cot.getPersona_auto() != null )
                pedido.setSin_gente_txt(cot.getPersona_auto());
            else
                pedido.setSin_gente_txt(Constantes.CADENA_VACIA);
	  		pedido.setNom_tbancaria(cot.getCot_nomtbank());
	  		pedido.setTipo_doc(cot.getCot_tipo_doc());
	  		pedido.setSin_gente_op(0);
	  		pedido.setDir_id(cot.getCot_dir_id());
	  		pedido.setPol_id(paramId_Politica);
	  		//buscar la descripcion de la sustitucion
	  		List lst_pols = biz.getPolitSustitucionAll();			
			for (int i = 0; i < lst_pols.size(); i++) {			
				PoliticaSustitucionDTO pol = (PoliticaSustitucionDTO) lst_pols.get(i);				
				if(paramId_Politica==pol.getId()){
					pedido.setPol_sustitucion(pol.getDescripcion());
				}
			}
			
	  		pedido.setObservacion(cot.getCot_obs());
	  		costo_desp = cot.getCot_costo_desp()*paramPorc_desp/100;
	  		// Si el costo de despacho es menor a 1, por defecto es 1
	  		if (costo_desp >= 1){
	  			pedido.setCosto_desp(costo_desp);
	  		}else{
	  			pedido.setCosto_desp(1);
	  		}
	  		pedido.setOrigen(Constantes.ORIGEN_VE_CTE);
	  		
            List perfiles_usr = new ArrayList();
	  		perfiles_usr = usr.getPerfiles();
	  		boolean perfil_supervisor_ve=false;
	  		for (int i=0;i<perfiles_usr.size();i++){
	  			PerfilesEntity perf = new PerfilesEntity();
	  			perf= (PerfilesEntity) perfiles_usr.get(i);
	  			if (perf.getIdPerfil().intValue()==Constantes.ID_PERFIL_SUPERVISOR_VE){
	  				perfil_supervisor_ve=true;
	  			}
	  		}
	  		logger.debug("Revisa si es un pedido especial autorizado por el supervisor VE");
	  		logger.debug("Perfil supervisor?="+perfil_supervisor_ve);
	  		logger.debug("paramTipoVe?="+paramTipoVe);
	  		
	  		if ((perfil_supervisor_ve) && (paramTipoVe.equals(Constantes.TIPO_VE_SPECIAL_CTE))) {
	  			pedido.setTipo_ve(Constantes.TIPO_VE_SPECIAL_CTE);
	  			logger.debug("El pedido es ESPECIAL");
	  		}
	  		else {
	  			pedido.setTipo_ve(Constantes.TIPO_VE_NORMAL_CTE);
	  			logger.debug("El pedido es NORMAL");
	  		}
	  		
	  		//setear datos del documento de factura 
	  		if( pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
	  			//obtener los datos de la empresa 
	  			EmpresasDTO emp = biz.getEmpresaById(cot.getCot_emp_id());
	  			//obtener los datos de la direccion de facturacion
	  			logger.debug("dir_fact : "+cot.getCot_dirfac_id());
	  			DirFacturacionDTO dfac = biz.getDireccionesFactByIdFact(cot.getCot_dirfac_id());
	  			//setear en pedido
	  			pedido.setFac_razon(emp.getEmp_rzsocial());
	  			pedido.setFac_rut(emp.getEmp_rut());
	  			pedido.setFac_dv(emp.getEmp_dv());
	  			pedido.setFac_direccion(dfac.getDfac_calle()+" "+dfac.getDfac_numero()+","+dfac.getDfac_depto());
	  			//pedido.setFac_fono(emp.getEmp_fono1_contacto());
	  			pedido.setFac_fono(dfac.getDfac_fono1());
	  			pedido.setFac_giro(emp.getEmp_rubro());
	  			pedido.setFac_comuna(dfac.getNom_comuna());
	  			pedido.setFac_ciudad(dfac.getDfac_ciudad());
	  		}
	  		
	  		//setear el listado de productos en el pedido
	  		pedido.setProductos(lista);
	  		
	  		//prm.setEmp_fmod(Formatos.getFecHoraActual());
	  		//La hora se pone automaticamente al ser creado
	  		//prm.setFec_crea(fec_crea);
	     
	  		//obtiene mensajes
			String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");
	 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
	  	    
	  		long idPedido = 0;
	  		try{
              idPedido = biz.doGeneraPedido(pedido);
	  			logger.debug("result?"+idPedido);
	  			if ( idPedido > 0 ) {
	  				String mjelog ="";
	  				LogsCotizacionesDTO log = new LogsCotizacionesDTO();
	  				if (paramComentario != null && !paramComentario.equals("")){
		  				mjelog = "Cantidad agregada supera la cantidad solicitada: ";
		  				log.setCot_id(paramId_cotizacion);
		  				log.setDescripcion(mjelog + paramComentario);
		  				log.setUsuario(usr.getLogin());
		  				biz.addLogCotizacion(log);
	  				}
	  				mjelog = "Se ha generado un nuevo pedido.";
	  				log.setCot_id(paramId_cotizacion);
	  				log.setDescripcion(mjelog);
	  				log.setUsuario(usr.getLogin());
	  				biz.addLogCotizacion(log);
                    paramUrl += "?id_cotizacion="+paramId_cotizacion+"&msje=" +  mensaje_exito+"&jornada="+jornada+"&local="+local+"&mod="+mod;
                    
                    if ( Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(pedido.getMedio_pago()) || Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(pedido.getMedio_pago()) ) {
                        //Enviar mail para que comprador ingrese a pagar su pedido
                        
                        try {
                            
                            ResourceBundle rb = ResourceBundle.getBundle("bo");
                            String mail_tpl = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("arc_mail");
                            TemplateLoader mail_load = new TemplateLoader(mail_tpl);
                            ITemplate mail_tem = mail_load.getTemplate();
                            String mail_result = mail_tem.toString(contenidoMailResumen(cot, idPedido, biz));
                            // Se envía mail al cliente
                            MailDTO mail = new MailDTO();
                            mail.setFsm_subject(rb.getString("mail.ve.subject"));
                            mail.setFsm_data(mail_result);
                            logger.debug("cot.getMailComprador()->"+cot.getMailComprador());
                            mail.setFsm_destina(cot.getMailComprador());
                            mail.setFsm_remite(rb.getString("mail.ve.remite"));
                            biz.addMail(mail);
                        } catch (Exception e) {
                            logger.error("Problemas con mail", e);
                        }
                        
                    }
                    
	  			}else{
	  				paramUrl += "?id_cotizacion="+paramId_cotizacion+"?&msje=" +  mensaje_fracaso+"&jornada="+jornada+"&local="+local+"&mod="+mod;
	  			}
	  			
	  		} catch(BocException e){
	  	  		ForwardParameters fp = new ForwardParameters();
	  	  	    //fp.add( req.getParameterMap() );
	  	  	    
	     		logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				if ( e.getMessage()!=null &&  !e.getMessage().equals("")){
					logger.debug(e.getMessage());
					fp.add( "id_cotizacion" , String.valueOf(paramId_cotizacion) );
					fp.add( "jornada" , String.valueOf(jornada) );
					fp.add( "local" , String.valueOf(local) );
					fp.add( "mod" , String.valueOf(mod) );
					fp.add( "id_local_fact" , String.valueOf(paramId_loc_fact) );
					if (e.getMessage().matches("(?i).*"+Constantes._EX_VE_GP_CAMPOS_INC+".*")){
						fp.add( "msje" , "Problemas con los campos entregados" );	
					}
					else if (e.getMessage().matches("(?i).*"+Constantes._EX_VE_GP_EXCEDE_CAPAC+".*")){
						fp.add( "msje" , "El pedido sobrepasa las capacidades de picking y/o despacho" );						
					}
					
					paramUrl = UrlError + fp.forward(); 
				} else { 
					logger.debug("Controlando excepción: " + e.getMessage());
				}
	     	}
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	 }//execute

    /**
     * @param cot
     * @param pedido
     * @return
     * @throws SystemException
     * @throws BocException
     */
    private IValueSet contenidoMailResumen(CotizacionesDTO cot, long idPedido, BizDelegate biz) throws BocException, SystemException {
        IValueSet mail_top = new ValueSet();
        mail_top.setVariable("{nombre_cliente}", cot.getCot_nom_comp());
        PedidoDTO ped = biz.getPedidosById(idPedido);
        mail_top.setVariable("{idped}", idPedido + ped.getSecuenciaPago());
        mail_top.setVariable("{idcot}", cot.getCot_id()+"");
        return mail_top;
    }

}
