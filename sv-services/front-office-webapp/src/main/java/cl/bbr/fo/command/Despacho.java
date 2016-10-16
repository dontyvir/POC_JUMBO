package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * @author carriagada-IT4B
 * 
 */
public class Despacho extends Command {
    
    static final long   ID_LOCAL_DONALD         = 1;
    static final long   ID_ZONA_DONALD          = 1;
    static final String NOMBRE_COMUNA_DONALD    = "Las Condes";

    
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        long total = 0;
        String carro = "";
        String carro2 = "";
        
		try {
		    // Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            arg1.setHeader("Cache-Control", "no-cache");
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
			IValueSet top = new ValueSet();		

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            
            //Trae la cantidad de productos en el carro para desplegar el calendario
            String idSession = null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            long cant_prod = biz.carroComprasGetCantidadProductos(Long.parseLong(session.getAttribute("ses_cli_id").toString()),idSession);
            
            long cli_id = 0L;
            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            List listaCarro = biz.carroComprasPorCategorias(cli_id, session.getAttribute("ses_loc_id").toString(), idSession);
            
         // Seteo datos cliente para chaordic_meta
    		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
    		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
    		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));
            
            long precio_total = 0;
            MiCarroDTO car;
            
            //Recuperar los cupones y los tcp de la sesión
            List l_torec = new ArrayList();
            List l_tcp = null;
            if( session.getAttribute("ses_promo_tcp") != null ) {
                l_tcp = (List)session.getAttribute("ses_promo_tcp");
                l_torec.addAll(l_tcp);
            }
            
            if( session.getAttribute("ses_cupones") != null ) {
                List l_cupones = (List)session.getAttribute("ses_cupones");
                l_torec.addAll(l_cupones);
            }
            
            if (listaCarro != null && listaCarro.size() > 0) {
                listaCarro = biz.cargarPromocionesMiCarro(listaCarro, l_torec, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
            }
            

            
            String criterio = "";
            //armamos carro aca tb
            for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                if (car.tieneStock()) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    total += precio_total;
                    
                    carro2+="[cant. "+car.getCantidad()+"]"+car.getPro_id()+"/"+car.getNom_marca()+"/"+car.getTipo_producto()+"/"+car.getNombre()+"/"+car.getDescripcion()+"/Precio unit-total: "+car.getPrecio()+"-"+precio_total+"<br>";
                    
                }
              //Tags Analytics - Obtener Criterio del Carro
                if(criterio!="Hibrido"){
                	if((car.getDescripcion().equalsIgnoreCase("No sustituir"))&&(criterio.equalsIgnoreCase("Criterio Jumbo"))||(car.getDescripcion().equalsIgnoreCase("Criterio Jumbo"))&&(criterio.equalsIgnoreCase("No sustituir"))){
                		criterio="Hibrido";
                	}
                	else{
                		criterio=car.getDescripcion();
                	}
 
                }
            }    
            
            top.setVariable("{mx_criterio}", criterio); //Tags Analytics - Criterio del Carro
            if (session.getAttribute("ses_cli_rut") != null){
            	top.setVariable("{rut_cli}", session.getAttribute("ses_cli_rut").toString()); //Tags Analytics - Criterio del Carro
            }else{
            	top.setVariable("{rut_cli}","");
            }
            //Trae los datos del cliente
            
			// Listado de tipos de calle
            ArrayList arr_tipocalle = new ArrayList();
            List registros = biz.tiposCalleGetAll();
            
            
            /************   LISTADO DE REGIONES   ****************/
            // Listado Regiones
            ArrayList arr_regiones = new ArrayList();
            long id_region = 0, id_comuna=0;
            
            if (session.getAttribute("ses_comuna_cliente") != null && 
                    !session.getAttribute("ses_comuna_cliente").toString().trim().equals("")){
                String tmp = session.getAttribute("ses_comuna_cliente").toString();
                String[] com = tmp.split("-=-"); //id_region-id_comuna-nom_comuna
                id_region = Long.parseLong(com[0]);
                id_comuna = Long.parseLong(com[1]);
            }
            
            List regiones = biz.regionesConCobertura();
            
            IValueSet fila0 = new ValueSet();
            fila0.setVariable("{option_reg_id}", "0");
            fila0.setVariable("{option_reg_nombre}", "Seleccione");

            if (id_region > 0){
                fila0.setVariable("{regselected}", "");
            }else{
                fila0.setVariable("{regselected}", "selected=true");
            }
            arr_regiones.add(fila0);

            for (int i = 0; i < regiones.size(); i++) {
                RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{option_reg_id}", dbregion.getId()+"");
                fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
                if (dbregion.getId() == id_region)
                    fila.setVariable("{regselected}","selected=true");
                else
                    fila.setVariable("{regselected}","");
                arr_regiones.add(fila);
            }
            top.setDynamicValueSets("REGIONES", arr_regiones);

            /*************   LISTADO DE COMUNAS   *****************/
            ArrayList arr_comunas = new ArrayList();
            List comunas = biz.comunasConCoberturaByRegion( id_region );
            IValueSet fila1 = new ValueSet();
            fila1.setVariable("{option_com_id}", "0");
            fila1.setVariable("{option_com_nombre}", "Seleccione");

            if (id_comuna > 0){
                fila1.setVariable("{comselected}", "");
            }else{
                fila1.setVariable("{comselected}", "selected=true");
            }
            arr_comunas.add(fila1);

            for (int i = 0; i < comunas.size(); i++) {
                ComunaDTO dbcomuna = (ComunaDTO) comunas.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{option_com_id}", dbcomuna.getId()+"");
                fila.setVariable("{option_com_nombre}", dbcomuna.getNombre());
                if (dbcomuna.getId() == id_comuna)
                    fila.setVariable("{comselected}","selected=true");
                else
                    fila.setVariable("{comselected}","");
                arr_comunas.add(fila);
            }
            top.setDynamicValueSets("COMUNAS",arr_comunas);
            
            /*************************************/
            
            List listaLocalesRetiro = biz.localesRetiro();
            List locales_retiro = new ArrayList();
            long local_retiro = 0;
            
            if (session.getAttribute("ses_loc_id") != null) {
                local_retiro = Long.parseLong(session.getAttribute("ses_loc_id").toString());
            }
            for (int i = 0; i < listaLocalesRetiro.size(); i++) {
                LocalDTO local = (LocalDTO) listaLocalesRetiro.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{dir_opcion}", local.getNom_local().replaceAll("Jumbo", ""));
                fila.setVariable("{dir_valor}", local.getId_local()+"");
                fila.setVariable("{dir_local}", local.getId_local()+"");
                fila.setVariable("{dir_zona}", local.getIdZonaRetiro()+"");
                fila.setVariable("{dir_direccion}", local.getDireccion()+"");
                //if (5 == local.getId_local()) //para que solamente quede seleccionado local Los dominicos "jumbo al auto"
                if (local_retiro == local.getId_local()) 
                    fila.setVariable("{selected}", "selected");
                else
                    fila.setVariable("{selected}", "");
                locales_retiro.add(fila);                         
            }
            
            List viewDespachos = new ArrayList(); 
            IValueSet filadespachos = new ValueSet();
            if ((session.getAttribute("ses_cli_id") != null) && (!session.getAttribute("ses_cli_id").toString().equals("1")) && (!session.getAttribute("ses_cli_rut").equals("123123")))  {
                long ses_dir_id = 0;
                if (session.getAttribute("ses_dir_id") != null &&
                        Long.parseLong(session.getAttribute("ses_dir_id").toString()) > 0 ){
                    ses_dir_id = Long.parseLong(session.getAttribute("ses_dir_id").toString());
                }

                Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
                int DirPreSeleccionada = 0;
                String DirSelected = "";
                // Recuperar direcciones de despacho del cliente
                List lista = biz.clientegetAllDirecciones(cliente_id.longValue());
                List datos = new ArrayList();
                for (int i = 0; i < lista.size(); i++) {
                    DireccionesDTO dir = (DireccionesDTO) lista.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{dir_opcion}", dir.getAlias() + "");
                    fila.setVariable("{dir_calle}", dir.getCalle() + "");
                    fila.setVariable("{dir_numero}", dir.getNumero() + "");
                    fila.setVariable("{dir_dpto}", dir.getDepto() + "");
                    fila.setVariable("{dir_valor}", dir.getId() + "");
                    fila.setVariable("{dir_comentario}", dir.getComentarios() + "");
                    int largo = 12;
                    if( dir.getComentarios() != null){
                        if (dir.getComentarios().length() < 12  ){
                            //largo = dir.getComentarios().length();
                            fila.setVariable("{dir_comentario_s}", dir.getComentarios() + "");
                        }else{
                            fila.setVariable("{dir_comentario_s}", dir.getComentarios().substring(0,largo) + "");    
                        }
                    }else{
                        fila.setVariable("{dir_comentario_s}", "");
                    }
                    
                    fila.setVariable("{dir_tipid}", dir.getTipo_calle() + "");
                    fila.setVariable("{dir_id_comuna}", dir.getCom_id() + "");
                    fila.setVariable("{dir_comuna}", dir.getCom_nombre() + "");
                    fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
                    fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
                    fila.setVariable("{id_local}", dir.getLoc_cod() + "");
                    fila.setVariable("{id_zona}", dir.getZona_id() + "");
                    
                    if (ses_dir_id > 0 && dir.getId() == ses_dir_id){
                        DirPreSeleccionada = i;
                        DirSelected = "selected=true";
                    }else{
                        DirSelected = "";
                    }
                    fila.setVariable("{selected}", DirSelected);
                    datos.add(fila);
                }
                filadespachos.setDynamicValueSets("DIR_DESPACHOS", datos);
                
                //recupera los datos del cliente(email, telefono)
                ClienteDTO cliente = biz.clienteGetById(cliente_id.longValue());
                if (cliente.getFon_cod_2() != null && !cliente.getFon_cod_2().equals("")) { 
                    for (int i = 6; i <= 9; i++) {
                        if (i == Integer.parseInt(cliente.getFon_cod_2()))
                            filadespachos.setVariable("{codigo" + i + "}","selected");
                        else
                            filadespachos.setVariable("{codigo" + i + "}","");
                    }
                } else {
                    for (int i = 5; i <= 9; i++) {
                        filadespachos.setVariable("{codigo" + i + "}","");
                    }
                }
                if (cliente.getFon_cod_2() != null){
                    filadespachos.setVariable("{cod_telefono}",cliente.getFon_cod_2());
                }else{  
                	filadespachos.setVariable("{cod_telefono}","");
                }
                if (cliente.getFon_num_2() != null){
                    filadespachos.setVariable("{telefono}",cliente.getFon_num_2());
                }else{
                    filadespachos.setVariable("{telefono}", "");
                }
                
                filadespachos.setVariable("{email}",cliente.getEmail());

                filadespachos.setVariable("{dir_id_comuna}", ((DireccionesDTO) lista.get(DirPreSeleccionada)).getCom_id() + "");
                filadespachos.setVariable("{dir_id_region}", ((DireccionesDTO) lista.get(DirPreSeleccionada)).getReg_id() + "");
                
                viewDespachos.add(filadespachos);
                top.setDynamicValueSets("DESPACHO_LOGUEADO",viewDespachos);
                
                for (int i = 0; i < registros.size(); i++) {
                    TipoCalleDTO reg_tc = (TipoCalleDTO) registros.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{option_calle_id}", reg_tc.getId() + "");
                    fila.setVariable("{option_calle_nombre}", reg_tc.getNombre());
                    if (reg_tc.getId() == ((DireccionesDTO) lista.get(DirPreSeleccionada)).getTipo_calle())
                        fila.setVariable("{calleselected}","selected=true");
                    else
                        fila.setVariable("{calleselected}","");
                    arr_tipocalle.add(fila);
                }
                
                /*for (int i = 0; i < regiones.size(); i++) {
                    RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{option_reg_id}", dbregion.getId()+"");
                    fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
                    if (dbregion.getId() == ((DireccionesDTO) lista.get(0)).getReg_id())
                        fila.setVariable("{regselected}","selected=true");
                    else
                        fila.setVariable("{regselected}","");
                    arr_regiones.add(fila);
                }*/
                

                
                /*List comunas = biz.regionesGetAllComunas(((DireccionesDTO) lista.get(0)).getReg_id());
                for (int i = 0; i < comunas.size(); i++) {
                    ComunaDTO dbcomuna = (ComunaDTO) comunas.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{option_com_id}", dbcomuna.getId()+"");
                    fila.setVariable("{option_com_nombre}", dbcomuna.getNombre());
                    if (dbcomuna.getId() == ((DireccionesDTO) lista.get(0)).getCom_id())
                        fila.setVariable("{comselected}","selected=true");
                    else
                        fila.setVariable("{comselected}","");
                    arr_comunas.add(fila);
                }
                top.setDynamicValueSets("COMUNAS",arr_comunas);*/

                top.setVariable("{calle}",((DireccionesDTO) lista.get(DirPreSeleccionada)).getCalle());
                top.setVariable("{numero}",((DireccionesDTO) lista.get(DirPreSeleccionada)).getNumero());
                top.setVariable("{departamento}",((DireccionesDTO) lista.get(DirPreSeleccionada)).getDepto());
                top.setVariable("{alias}",((DireccionesDTO) lista.get(DirPreSeleccionada)).getAlias());
                
            } else {
                session.setAttribute("ses_loc_id", "" + ID_LOCAL_DONALD);
                session.setAttribute("ses_dir_id", "1");
                session.setAttribute("ses_zona_id", "" + ID_ZONA_DONALD);

                filadespachos.setVariable("{codigo}","");
                
                
                filadespachos.setVariable("{email}", session.getAttribute("ses_cli_email"));
                
                if (session.getAttribute("calle") != null &&
                        !session.getAttribute("calle").toString().equals("")){
                    filadespachos.setVariable("{tipo_calle}", session.getAttribute("tipo_calle").toString());
                    filadespachos.setVariable("{calle}", session.getAttribute("calle").toString());
                    filadespachos.setVariable("{numero}", session.getAttribute("numero").toString());
                    filadespachos.setVariable("{departamento}", session.getAttribute("departamento").toString());
                    filadespachos.setVariable("{region}", session.getAttribute("region").toString());
                    filadespachos.setVariable("{comuna}", session.getAttribute("comuna").toString());
                    filadespachos.setVariable("{alias}", session.getAttribute("ses_dir_alias").toString());
                    filadespachos.setVariable("{telefono}", session.getAttribute("ses_telefono").toString());
                    filadespachos.setVariable("{cod_telefono}", session.getAttribute("tel_despacho").toString());
                }else{
                    filadespachos.setVariable("{tipo_calle}","2");
                    filadespachos.setVariable("{calle}","Ingresa tu calle");
                    filadespachos.setVariable("{numero}","000");
                    filadespachos.setVariable("{departamento}", "");
                    filadespachos.setVariable("{region}",(session.getAttribute("ses_comuna_cliente").toString().split("-=-"))[0]);
                    filadespachos.setVariable("{comuna}",(session.getAttribute("ses_comuna_cliente").toString().split("-=-"))[1]);
                    filadespachos.setVariable("{alias}", "Nombre");
                    //filadespachos.setVariable("{observacion}", "");
                    //filadespachos.setVariable("{autorizacion}", "Ej: Conserje");
                    filadespachos.setVariable("{telefono}", "");
                    filadespachos.setVariable("{cod_telefono}","");
                    
                }
                //session.setAttribute("rut_autorizado"
                
                viewDespachos.add(filadespachos);
                top.setDynamicValueSets("DESPACHO_NOLOGUEADO",viewDespachos);
                
                
                
                for (int i = 0; i < registros.size(); i++) {
                    TipoCalleDTO reg_tc = (TipoCalleDTO) registros.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{option_calle_id}", reg_tc.getId() + "");
                    fila.setVariable("{option_calle_nombre}", reg_tc.getNombre());
                    fila.setVariable("{calleselected}","");
                    arr_tipocalle.add(fila);
                }
                
                /*for (int i = 0; i < regiones.size(); i++) {
                    RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{option_reg_id}", dbregion.getId()+"");
                    fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
                    fila.setVariable("{regselected}","");
                    arr_regiones.add(fila);
                }*/
                
                top.setVariable("{calle}","");
                top.setVariable("{numero}","");
                top.setVariable("{departamento}","");
                top.setVariable("{alias}","");
                top.setVariable("{departamento}", "");
            }
            
            if (session.getAttribute("ses_telefono") != null &&
                    !session.getAttribute("ses_telefono").toString().equals("")){
                top.setVariable("{telefono}", session.getAttribute("ses_telefono").toString());
            }else{
                top.setVariable("{telefono}", "");
            }
            if (session.getAttribute("tel_despacho") != null &&
                    !session.getAttribute("tel_despacho").toString().equals("")){
                top.setVariable("{cod_telefono}", session.getAttribute("tel_despacho").toString());
            }else{
                top.setVariable("{cod_telefono}", "");
            }
            
            if(session.getAttribute("ses_cli_nombre").toString().equalsIgnoreCase("Invitado")){//si es invitado 
                top.setVariable("{autorizacion_nom}", "Ingrese Nombre");
                top.setVariable("{autorizacion_ape}", "Ingrese Apellido");
                top.setVariable("{rut_autorizado}","");
                top.setVariable("{dv_autorizado}", "");
                		
            }else{//si es registrado
            	if(!(session.getAttribute("ses_cli_rut")==null)){//si viene rut con dato, calcula dv
		           	top.setVariable("{rut_autorizado}", session.getAttribute("ses_cli_rut").toString());
		           	int M=0,S=1,T=Integer.parseInt(session.getAttribute("ses_cli_rut").toString());for(;T!=0;T/=10)S=(S+T%10*(9-M++%6))%11;            			
		           	top.setVariable("{dv_autorizado}", Character.toString((char)(S!=0?S+47:75)));	
            	}else{
            		top.setVariable("{rut_autorizado}","");
            		top.setVariable("{dv_autorizado}","");
            	}
		            	int i=0;
            	StringTokenizer token=new StringTokenizer(session.getAttribute("ses_cli_nombre").toString());
            	while(token.hasMoreTokens()){
            		if (i==0){
            			top.setVariable("{autorizacion_nom}", token.nextToken());
            			i++;
            		}else if(i==1){
            			top.setVariable("{autorizacion_ape}", token.nextToken());
            		}	
            	}
            	top.setVariable("{autorizacion_nom}", session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
            }
            
            if (session.getAttribute("observacion") != null &&
                    !session.getAttribute("observacion").toString().equals("")){
                top.setVariable("{observacion}", session.getAttribute("observacion").toString());
            }else{
                top.setVariable("{observacion}", "");
            }

            if (session.getAttribute("ses_forma_despacho") != null && 
                    session.getAttribute("ses_forma_despacho").toString().equals("R")){
                top.setVariable("{rut}", session.getAttribute("rut_autorizado").toString());
                top.setVariable("{dv}", session.getAttribute("dv_autorizado").toString());
            }else{
                top.setVariable("{rut}", "");
                top.setVariable("{dv}", "");
            }

            
            top.setDynamicValueSets("TIPOCALLE", arr_tipocalle);
            top.setDynamicValueSets("LOCALES_RETIRO", locales_retiro);
            
            top.setVariable("{nom_cliente}", session.getAttribute("ses_cli_nombre_pila").toString());
            top.setVariable("{ape_cliente}", session.getAttribute("ses_cli_apellido_pat").toString());
            if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
        		top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
        	} else {
            top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
        	}     
            //top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
            top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());
            
            top.setVariable("{cli_id}", session.getAttribute("ses_cli_id").toString());
            
            top.setVariable("{cant_prod}", cant_prod + "");
            
            List viewLogin = new ArrayList();
            IValueSet fila = new ValueSet();
            
            if ( "".equalsIgnoreCase( session.getAttribute("ses_comuna_cliente").toString() ) ) {
                fila.setVariable("{comuna_usuario}", NOMBRE_COMUNA_DONALD);
                fila.setVariable("{comuna_usuario_id}", "0");
            } else {
                String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");
                fila.setVariable("{comuna_usuario}", "" + loc[2]);
                fila.setVariable("{comuna_usuario_id}", "" + loc[1]);
            }
            viewLogin.add(fila);
            
            if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                top.setDynamicValueSets("MOSTRAR_NO_LOGUEADO", viewLogin); 
            } else {
                if ((session.getAttribute("ses_cli_rut") != null)  && (session.getAttribute("ses_cli_rut").equals("123123")))
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO_INVITADO", viewLogin);
                else
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin); 
            }
            
            if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                top.setVariable("{retirochecked}", "checked=\"true\"");
            } else {
                top.setVariable("{retirochecked}", "");
            }
            
            
            if(top.getVariable("{nombre_cliente}").equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
          }
          else{
                top.setVariable("{ta_mx_user}", "registrado");
          }
          //Tags Analytics - Captura de Comuna y Región en Texto
          /************   LISTADO DE REGIONES   ****************/
          
          String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
          if(ta_mx_loc.equals(""))
                ta_mx_loc="none-none";
          top.setVariable("{ta_mx_loc}", ta_mx_loc);
          
          
          top.setVariable("{ta_mx_content}", "Despacho");
          top.setVariable("{ta_mx_criterio}", "Jumbo");

          Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
          List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
          List categoriaCompletaProd = new ArrayList();
          for(int i=0; i <listaCarro.size(); i++){
          	MiCarroDTO lineCar = (MiCarroDTO) listaCarro.get(i);
          	String idTerminal = lineCar.getNombreIntermedia();
          	
                  CategoriaDTO cate = (CategoriaDTO)  list_categoria.get(0);
                  List cate2 =  cate.getCategorias();
                  for(int k=0; k <cate2.size(); k++){
                  	CategoriaDTO cate3 = (CategoriaDTO) cate2.get(k);
                  	 if(idTerminal.equals(cate3.getNombre())){
                  		 for(int l=0; l<list_categoria.size();l++){
	                             CategoriaDTO categ = (CategoriaDTO)  list_categoria.get(l);
	                    		 if(cate3.getId_padre()==categ.getId()){
	                             	categoriaCompletaProd.add(categ.getNombre()+"/"+lineCar.getNombreIntermedia()+"/"+lineCar.getNombreTerminal()+"/"+lineCar.getTipo_producto()+" "+lineCar.getNom_marca()+" "+lineCar.getNombre());
	                             	break;
	                    		 }
                  		 }

                       }
                  

                 
              }
          }
          
          session.setAttribute("listProdCategComplet", categoriaCompletaProd);
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}
	}
}