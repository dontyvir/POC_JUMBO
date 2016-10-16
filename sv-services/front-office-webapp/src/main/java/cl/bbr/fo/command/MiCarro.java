package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * @author carriagada-IT4B
 * 
 */
public class MiCarro extends Command {
    
    static final long   ID_LOCAL_DONALD         = 1;
    static final long   ID_ZONA_DONALD          = 1;
    static final String NOMBRE_COMUNA_DONALD    = "Las Condes";
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
		try {
		    long idProductoAgregado = 0;
		    
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
			this.getLogger().debug( "Template:" + pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();
			IValueSet ch_prod = null;

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            
            //Trae las Categorìas
            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            
         // Seteo datos cliente para chaordic_meta
    		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
    		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
    		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));

            
            List categorias = new ArrayList();
            CategoriaDTO cat1 = null;
            List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
            this.getLogger().debug("Categoria:"+list_categoria.size());
            for( int i = 0; i < list_categoria.size() ; i++ ) {
                cat1 = (CategoriaDTO) list_categoria.get(i);
                IValueSet nivel1 = new ValueSet();
                
                //Cabeceras
                nivel1.setVariable("{cat_id}",  cat1.getId()+"");   
                nivel1.setVariable("{cat_nombre}", cat1.getNombre()+"");
                nivel1.setVariable("{cat_tipo}", cat1.getTipo()+"");
                nivel1.setVariable("{cat_padre}", "0");
                nivel1.setVariable("{numero}", i + "");

                // Buscar Intermedias
                CategoriaDTO cat2 = null;
                List intermedias = cat1.getCategorias();
                List aux_intermedias = new ArrayList();
                
                IValueSet nivel2 = null;
                for (int  j=0; j< intermedias.size(); j++){
                    nivel2 = new ValueSet();
                    cat2 = (CategoriaDTO)intermedias.get(j);
                    if (cat1.getId() == cat2.getId_padre()){
                        nivel2.setVariable("{nombre_subcat}", cat2.getNombre() + "");
                        nivel2.setVariable("{cat_id}",  cat2.getId()+"");   
                        nivel2.setVariable("{cat_tipo}", cat2.getTipo()+"");
                        nivel2.setVariable("{cat_padre}", cat2.getId_padre()+"");
                        nivel2.setVariable("{cat_cab}", cat1.getId()+"");
                        nivel2.setVariable("{nombre_padre}", cat1.getNombre()+""); // se agrega

                        //Buscar Terminales
                        CategoriaDTO cat3 = null;
                        List terminales = cat2.getCategorias();
                        List aux_terminales = new ArrayList();
                        
                        IValueSet aux_fila2 = null;
                        for (int  k=0; k< terminales.size(); k++){
                            aux_fila2 = new ValueSet();
                            cat3 = (CategoriaDTO)terminales.get(k);
                            if (cat2.getId() == cat3.getId_padre()){
                                aux_fila2.setVariable("{nombre_subcat}", cat3.getNombre() + "");
                                aux_fila2.setVariable("{cat_id}",  cat3.getId()+""); 
                                aux_fila2.setVariable("{cat_tipo}", cat3.getTipo()+"");
                                aux_fila2.setVariable("{cat_padre}", cat3.getId_padre()+"");
                                aux_fila2.setVariable("{cat_cab}", cat1.getId()+"");
                                aux_fila2.setVariable("{banner_subcat}", cat3.getBanner()+"");
                                aux_fila2.setVariable("{nombre_padre}", cat2.getNombre()+""); // se agrega
                                aux_fila2.setVariable("{nombre_abuelo}", cat1.getNombre()+""); // se agrega
                                aux_fila2.setVariable("{numero}", i + "");
                                
                                aux_terminales.add(aux_fila2);
                            }
                        }
                        nivel2.setDynamicValueSets("TERMINALES", aux_terminales);
                        aux_intermedias.add(nivel2);
                    }
                }
                this.getLogger().debug("ID categoria:" + cat1.getId() + " Cantidad subcategorias:" + aux_intermedias.size());               
                nivel1.setDynamicValueSets("INTERMEDIAS", aux_intermedias);             
                
                categorias.add(nivel1);
            }
            top.setDynamicValueSets("CATEGORIAS", categorias);
            String idSession = null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            List listaCarro = biz.carroComprasPorCategorias( Long.parseLong(session.getAttribute("ses_cli_id").toString()), session.getAttribute("ses_loc_id").toString(), idSession);
			long total = 0;
			
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
			for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                long precio_total = 0;
                precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                if (car.tieneStock()) {
                    total += precio_total;
                }
            }    
    
			//double descuento_promo = total;
			double descuento_promo_tmas = 0;
            double descuento_promo_tban = 0;
			String desc_promo_tmas = "";
            String desc_promo_tban = "";
			// Obtener datos de la promomocion TMAS
			try {
				doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
				recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
				recalculoDTO.setF_pago( rb.getString("promociones.jumbomas.formapago") );
				recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
				recalculoDTO.setGrupos_tcp(l_torec);
//				[20121108avc
                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
                    recalculoDTO.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
				//]20121108avc
				List l_prod = new ArrayList();
				List chaordic_prd = new ArrayList();
				StringBuffer detalleItems = new StringBuffer();

				for (int i = 0; i < listaCarro.size(); i++) {
					ch_prod = new ValueSet();
					// agrego separador de productos sólo si hay más de 1
					if(i != 0){
						detalleItems.append("|");
					}
					car = (MiCarroDTO) listaCarro.get(i);
                    if (car.tieneStock()) {
                        ProductoPromosDTO pro = new ProductoPromosDTO();
                        pro.setId_producto(car.getId_bo());
                        pro.setCod_barra(car.getCodbarra());
                        pro.setSeccion_sap(car.getCatsap());
                        pro.setCant_solicitada(car.getCantidad());
                        if(car.getPesable()!=null && car.getPesable().equals("S") )
                            pro.setPesable("P");
                        else
                            pro.setPesable("C");
                        pro.setPrecio_lista(car.getPrecio());
                        l_prod.add(pro);
                        
                    }
                    
					//seteo producto para generar chaordic_meta
					ch_prod.setVariable("{id_prod}" , car.getPro_id()   + "");
					ch_prod.setVariable("{precio}"  , car.getPrecio()   + "");
					ch_prod.setVariable("{cantidad}", car.getCantidad() + "");
					chaordic_prd.add(ch_prod);
                    
				}
				//top.setVariable("detalleItems", detalleItems.toString()+"]}");
				//System.out.println(detalleItems.toString()+"]}");
				//top.setVariable("{detalle_items}", detalleItems.toString());
				//System.out.println(detalleItems.toString());				
				top.setDynamicValueSets("CHAORDIC_PROD", chaordic_prd);

				recalculoDTO.setProductos( l_prod );

				if (l_prod != null && l_prod.size() > 0 ){
				   /////////////////////////////////////////////////////
				   //CALCULO
				    doRecalculoResultado resultado = biz.doRecalculoPromocion( recalculoDTO );
				   /////////////////////////////////////////////////////

					List l_promo = resultado.getPromociones();
					for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
						PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
						this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
						desc_promo_tmas += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
					}
					
					descuento_promo_tmas = resultado.getDescuento_pedido();
					if ( total < resultado.getDescuento_pedido() ) {
						descuento_promo_tmas = total;
	                }
				}
			} catch( SystemException e ) {
				this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
			}
			
            
			//Obtener datos de la promomocion TBAN
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.tjacredito.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.tjacredito.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);

                List l_prod = new ArrayList();
                
                for (int i = 0; i < listaCarro.size(); i++) {
                    car = (MiCarroDTO) listaCarro.get(i);
                    if (car.tieneStock()) {
                        ProductoPromosDTO pro = new ProductoPromosDTO();
                        pro.setId_producto(car.getId_bo());
                        pro.setCod_barra(car.getCodbarra());
                        pro.setSeccion_sap(car.getCatsap());
                        pro.setCant_solicitada(car.getCantidad());
                        if(car.getPesable()!=null && car.getPesable().equals("S") )
                            pro.setPesable("P");
                        else
                            pro.setPesable("C");
                        pro.setPrecio_lista(car.getPrecio());
                        l_prod.add(pro);
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                   /////////////////////////////////////////////////////
                   //CALCULO
                    doRecalculoResultado resultado = biz.doRecalculoPromocion( recalculoDTO );
                   /////////////////////////////////////////////////////

                    List l_promo = resultado.getPromociones();
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_tban += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                    }
                    
                    descuento_promo_tban = resultado.getDescuento_pedido();
                    if ( total < resultado.getDescuento_pedido() ) {
                        descuento_promo_tban = total;
                    }
                }
            } catch( SystemException e ) {
                this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
            }
            
			if (listaCarro != null && listaCarro.size()>0) {
			    List viewTotalizador = new ArrayList(); 
			    IValueSet filatotal = new ValueSet();
			    session.setAttribute("##_total_descuento_promo_tmas",String.valueOf(total-descuento_promo_tmas));//Solo para el calculo del despacho
			    filatotal.setVariable("{total_desc_tmas}", Formatos.formatoPrecioFO(total-descuento_promo_tmas));
			    filatotal.setVariable("{total_desc_sf_tmas}", Formatos.formatoPrecioFO(descuento_promo_tmas));
                filatotal.setVariable("{total_desc_tban}", Formatos.formatoPrecioFO(total-descuento_promo_tban));
                filatotal.setVariable("{total_desc_sf_tban}", Formatos.formatoPrecioFO(descuento_promo_tban));
			    viewTotalizador.add(filatotal);
                top.setDynamicValueSets("TOTALIZADOR",viewTotalizador);
            } else {
                List viewVacio = new ArrayList(); 
                IValueSet filavacio = new ValueSet();
                viewVacio.add(filavacio);
                top.setDynamicValueSets("VACIO",viewVacio);
            }
            
            top.setVariable("{cant_reg}", listaCarro.size() +"" );
            top.setVariable("{id_prod_add}", idProductoAgregado +"" );
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
                if ((session.getAttribute("ses_cli_rut") != null)  && (session.getAttribute("ses_cli_rut").equals("123123"))){
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO_INVITADO", viewLogin);
                }
                else{
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin);
                    biz.updateFechaMiCarro(Integer.parseInt(session.getAttribute("ses_cli_id").toString()));
                }
            }
            
          
          if(top.getVariable("{nombre_cliente}").equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
          }
          else{
                top.setVariable("{ta_mx_user}", "registrado");
          }
          //Locales Retiro - framos
          ArrayList locales = new ArrayList();
          List localesRetiro = biz.localesRetiro();
          for (int i = 0; i < localesRetiro.size(); i++) {
          	IValueSet fila1 = new ValueSet();
          	LocalDTO local = (LocalDTO)localesRetiro.get(i);
          	PoligonoxComunaDTO pol = new PoligonoxComunaDTO();
          	long id_zona = local.getIdZonaRetiro();
          	JdbcPedidosDAO dao = new JdbcPedidosDAO();
          	List listaPoligonosZona = dao.getPoligonosXZona(id_zona);
          	for (int j = 0 ; j < listaPoligonosZona.size();j++){
          		pol = (PoligonoxComunaDTO)listaPoligonosZona.get(j);
          	}
          	fila1.setVariable("{comuna_local}", Long.toString(pol.getId_comuna()));
          	fila1.setVariable("{nombre_local}", local.getNom_local());
          	locales.add(fila1);
          }
          top.setDynamicValueSets("LOCALES_RETIRO",locales);		
          //Locales Retiro
          //Tags Analytics - Captura de Comuna y Región en Texto
          /************   LISTADO DE REGIONES   ****************/
          
          String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
          if(ta_mx_loc.equals(""))
                ta_mx_loc="none-none";
          top.setVariable("{ta_mx_loc}", ta_mx_loc);
          
          
          top.setVariable("{ta_mx_content}", "Mi Carro");


            
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}
	}
}