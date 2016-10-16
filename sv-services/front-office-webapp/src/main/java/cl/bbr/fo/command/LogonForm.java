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
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

/**
 * Muestra el home del sitio
 * 
 * @author BBRI
 *  
 */
public class LogonForm extends Command {

    static final long   ID_LOCAL_DONALD         = 1;
    static final long   ID_ZONA_DONALD          = 1;
    static final String NOMBRE_COMUNA_DONALD    = "Las Condes";

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		//		 Recupera la sesión del usuario
		//20120823 Andres Valle
		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		//-20120823 Andres Valle
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();
        
        arg1.setContentType("text/html");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("ISO-8859-1");

		
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		
		// Recupera pagina desde web.xml
		String pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
		this.getLogger().debug( "Template:"+pag_form );
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		// Se almacena tracking en este sector
		//Tracking_web.saveTracking( "Login", arg0 );
        
        IValueSet top = new ValueSet();
        
        BizDelegate biz = new BizDelegate();
        
        Long cliente_id = null;
        if (session.getAttribute("ses_cli_id")!= null)
            cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
        else{
            cliente_id = new Long(1);
            session.setAttribute("ses_cli_id", "1");
			//[20121107avc
			session.setAttribute("ses_colaborador", "false");
			//]20121107avc
        }
        List categorias = new ArrayList();
        
     // Seteo datos cliente para chaordic_meta
        
        String cli_id = ""+session.getAttribute("ses_cli_id");
        String cli_nombre = ""+session.getAttribute("ses_cli_nombre");
        String cli_email = ""+session.getAttribute("ses_cli_email");
        
        // cliente entró como invitado
		if("1".equals(cli_id) && "null".equals(cli_nombre)){
			session.setAttribute("ses_cli_nombre","Invitado");
			session.setAttribute("ses_cli_email","invitado@invitado.cl");
		}
		
		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));
		
        CategoriaDTO cat1 = null;
        
        List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
        session.setAttribute("CATEGORIAS", list_categoria); //MCA
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
        
        List prods = biz.getProductosCarruselActivos();
        List carrusel = new ArrayList();
        if ( prods.size() > 0 ) {
            
            IValueSet carru = new ValueSet();
            carru.setVariable("{vacio}" , "");
            
            List productosCarrusel = new ArrayList();
            for ( int i = 0; i < prods.size(); i++ ) {
                ProductoCarruselDTO prod = (ProductoCarruselDTO) prods.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{nombre}" , prod.getNombre());
                fila.setVariable("{imagen}" , prod.getImagen());
                fila.setVariable("{descripcion}" ,(prod.getDescripcion().length()>54?(prod.getDescripcion().substring(0,54) + "..."):prod.getDescripcion())  ); //descripcion truncada
                fila.setVariable("{desc_precio}" , prod.getDescPrecio());
//+20120328coh
				fila.setVariable("{id_carrusel}" , String.valueOf(prod.getIdProductoCarrusel()));
                fila.setVariable("{tipo_producto}" , prod.getTipoProducto());
                fila.setVariable("{descripcion_producto}" , (prod.getDescripcionProducto().length()>54?(prod.getDescripcionProducto().substring(0,54) + "..."):prod.getDescripcionProducto()));

                fila.setVariable("{link_producto}" , prod.getLinkDestino());
                String particion = "";
                double precio = Double.parseDouble(prod.getPrecioProducto());
                if ("S".equals(prod.getEsParticionable()) && prod.getParticion() != 0) {
                   particion = "1/" + prod.getParticion() + " ";
                   precio /= prod.getParticion();
                }
                fila.setVariable("{precio_producto}", Formatos.formatoPrecioFO(precio) + " " + particion
                      + Formatos.formatoUnidadFO(prod.getTipre()));
//                fila.setVariable("{precio_producto}" , prod.getPrecioProducto());
                fila.setVariable("{imagen_producto}" , prod.getImagenProducto());
                fila.setVariable("{marca}" , prod.getMarcaProducto());
//-20120328coh
                if ("S".equalsIgnoreCase(prod.getConCriterio())) {
                    fila.setVariable("{con_criterio}" , "<br /><img src=\"/FO_IMGS/images/productos/pagandocon.jpg\" alt=\"pagando con tarjeta más\" width=\"97\" height=\"23\" />");
                } else {            
                    fila.setVariable("{con_criterio}" , "");
                }
                if (i == 0) {
                    fila.setVariable("{class}" , "panel primero");
                } else if (i == (prods.size() - 1)) {
                    fila.setVariable("{class}" , "panel ultimo");    
                } else {
                    fila.setVariable("{class}" , "panel");
                }
                
                productosCarrusel.add(fila);
            }
            carru.setDynamicValueSets("PRODUCTOS_CARRUSEL", productosCarrusel);
            
            carrusel.add(carru);
        
        }
        top.setDynamicValueSets("CARRUSEL", carrusel);

        //session.setAttribute("ses_loc_id", "" + 1);
        
        if ( session.getAttribute("ses_loc_id") != null ) {
            top.setVariable("{SES_ID_LOC}",session.getAttribute("ses_loc_id").toString());
        } else {
            top.setVariable("{SES_ID_LOC}","");
        }
        
        //Tags Analytics - Captura de estado de user
        if((session.getAttribute("ses_cli_nombre_pila")==null) || (session.getAttribute("ses_cli_nombre_pila").toString().equals("Invitado"))){
            top.setVariable("{mx_user}", "invitado");
        }
        else{
            top.setVariable("{mx_user}", "registrado");
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
		top.setVariable("{mx_loc}", ta_mx_loc);
		  
		top.setVariable("{mx_content}", "Pagina Principal Jumbo.cl");
		
        
        String result = tem.toString(top);
		out.print(result);
	}

}