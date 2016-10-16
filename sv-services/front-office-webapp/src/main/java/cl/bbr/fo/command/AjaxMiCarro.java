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
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * @author carriagada-IT4B
 * 
 */
public class AjaxMiCarro extends Command { 
    
    static final long   ID_LOCAL_DONALD         = 1;
    static final long   ID_ZONA_DONALD          = 1;
    static final String NOMBRE_COMUNA_DONALD    = "Las Condes";

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        try {
            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");         
            
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            arg1.setHeader("Cache-Control", "no-cache");
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=utf-8");
            
            String filtro = arg0.getParameter("filtro");
            
            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();
            
            // Recupera pagina desde web.xml y se inicia parser
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug( "Template:" + pag_form );
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();

            IValueSet top = new ValueSet();     

            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            String idSession = null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            List listaCarro = biz.carroComprasPorCategorias( Long.parseLong(session.getAttribute("ses_cli_id").toString()), session.getAttribute("ses_loc_id").toString(), idSession,filtro);
            long total = 0;
            long  valorid = 0;
            
            long id_ter = 0;
            long id_inter = 0;
            boolean auxinter = false;
            List intermedias = new ArrayList();
            List terminales = new ArrayList();
            List productos = new ArrayList();
            IValueSet intermedia = new ValueSet();
            IValueSet terminal = new ValueSet();
            IValueSet producto = new ValueSet();
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
                producto = new ValueSet();
                producto.setVariable("{nota}", car.getNota());
                producto.setVariable("{id}", car.getId()+"");
                producto.setVariable("{pro_id}",car.getPro_id()+"");
                valorid = car.getId();
                long precio_total = 0;
                precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                if (car.tieneStock()) {
                    total += precio_total;
                }
                String nombre_pro = (car.getTipo_producto()+" "+car.getNom_marca()).trim();
                nombre_pro = cl.bbr.jumbocl.common.utils.Utils.separarDescripcionesLargas(nombre_pro);
                int largo_pro  = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto"));
                int largo_desc = Integer.parseInt(rb.getString("orderitemdisplay.largodescripcionproducto"));
                if( nombre_pro.length() < largo_pro)
                    largo_pro = nombre_pro.length();
                producto.setVariable("{nombrepro}", nombre_pro.substring(0,largo_pro));
                //producto.setVariable("{nombre}", car.getNombre());
                
                //(productoDTO.getDescripcion().length()>54?(productoDTO.getDescripcion().substring(0,54) + "..."):productoDTO.getDescripcion())
                //(car.getNombre().length()>54?(car.getNombre().substring(0,54) + "..."):car.getNombre())
                producto.setVariable("{nombre}", (car.getNombre().length()>largo_desc?(car.getNombre().substring(0,largo_desc) + "..."):car.getNombre()));
                
                producto.setVariable("{nommarca}", car.getNom_marca());
                //producto.setVariable("{subtotal}", Formatos.formatoPrecio(precio_total) + "");
                producto.setVariable("{tipoprod}",car.getTipo_producto());
                producto.setVariable("{ppum}",Formatos.formatoPrecioFO(car.getPpum()) + " x " + car.getUnidad_nombre());
                producto.setVariable("{cod_sap}", car.getCodSap());
                producto.setVariable("{unidad}", car.getTipre().toUpperCase() + "" );
                producto.setVariable("{imagen}", car.getImagen());

                producto.setVariable("{i}", i + "");
                producto.setVariable("{id_ter}", car.getIdTerminal() + "");
                producto.setVariable("{id_inter}", car.getIdIntermedia() + "");
                
                String sustituto_cliente = "Ej: marca, sabor";
                if ( car.getIdCriterio() == 4 ) {
                    if ( car.getSustitutoCliente().length() > 0 ) {
                        sustituto_cliente = car.getSustitutoCliente();
                    }
                }
                
                if (car.getIdCriterio() == 0) {
                	producto.setVariable("{seleccionado}", "1");
                    IValueSet fila_sincriterio = new ValueSet();
                    List aux_sincriterio = new ArrayList();
                    fila_sincriterio.setVariable("{i}", i + "");
                    aux_sincriterio.add(fila_sincriterio);
                    producto.setDynamicValueSets("SIN_CRITERIO", aux_sincriterio);
                } else {
                	producto.setVariable("{seleccionado}", "2");
                    IValueSet fila_concriterio = new ValueSet();
                    List aux_concriterio = new ArrayList();
                    fila_concriterio.setVariable("{i}", i + "");
                    aux_concriterio.add(fila_concriterio);
                    producto.setDynamicValueSets("CON_CRITERIO", aux_concriterio);
                }
                
                promociones(car, producto);
                //Si el producto es con seleccion
                if (car.tieneStock()) {
                    if (car.getUnidad_tipo().charAt(0) == 'S') {
                        IValueSet fila_lista_sel = new ValueSet();
                        fila_lista_sel.setVariable("{id}", valorid+"" );
                        fila_lista_sel.setVariable("{pro_id}",car.getPro_id()+""); 
                        fila_lista_sel.setVariable("{contador}", i+"" );
                        fila_lista_sel.setVariable("{maximo}", car.getInter_maximo() + "");
                        fila_lista_sel.setVariable("{intervalo}", car.getInter_valor() + "");
                        fila_lista_sel.setVariable("{cod_sap}", car.getCodSap());
                        fila_lista_sel.setVariable("{unidad}", car.getTipre().toUpperCase() + "" );
                        fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(car.getCantidad()) + "" );
                        fila_lista_sel.setVariable("{precio}", car.getPrecio() + "" );
                        fila_lista_sel.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
                        fila_lista_sel.setVariable("{sustituto_cliente}", sustituto_cliente);
                        List aux_blanco = new ArrayList();
                        aux_blanco.add(fila_lista_sel);
                        producto.setDynamicValueSets("LISTA_SEL", aux_blanco);
                    } else {
                        IValueSet fila_lista_sel = new ValueSet();
                        fila_lista_sel.setVariable("{id}", valorid+"" );
                        fila_lista_sel.setVariable("{pro_id}",car.getPro_id()+""); 
                        fila_lista_sel.setVariable("{contador}", i+"" );
                        fila_lista_sel.setVariable("{maximo}", car.getInter_maximo() + "");
                        fila_lista_sel.setVariable("{intervalo}", car.getInter_valor() + "");
                        fila_lista_sel.setVariable("{cod_sap}", car.getCodSap());
                        fila_lista_sel.setVariable("{unidad}", car.getTipre().toUpperCase() + "" );
                        fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(car.getCantidad()) + "");
                        fila_lista_sel.setVariable("{precio}", car.getPrecio() + "" );
                        fila_lista_sel.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
                        fila_lista_sel.setVariable("{sustituto_cliente}", sustituto_cliente);
                        fila_lista_sel.setVariable("{id_ter}", car.getIdTerminal() + "");
                        fila_lista_sel.setVariable("{id_inter}", car.getIdIntermedia() + "");
                        
                        for ( int h = 1; h <= 5; h++ ) {
                            if ( h == car.getIdCriterio() ) {
                                fila_lista_sel.setVariable("{checked" + h + "}", "checked=\"checked\"");
                            } else {
                                fila_lista_sel.setVariable("{checked" + h + "}", "");
                            }                       
                        }
                        
                        /*if (car.getIdCriterio() == 0) {
                            fila_lista_sel.setVariable("{checked1}", "checked=\"checked\"");
                        }*/
                        
                        List aux_blanco = new ArrayList();
                        aux_blanco.add(fila_lista_sel);
                        producto.setDynamicValueSets("INPUT_SEL",aux_blanco);
                    }
                }else{// EN CASO DE NO TENER STOCK
                    IValueSet fila_notienestock = new ValueSet();
                    fila_notienestock.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
                    List aux_notienestock = new ArrayList();
                    aux_notienestock.add(fila_notienestock);
                    producto.setDynamicValueSets("NO_TIENE_STOCK", aux_notienestock);
                }

                if (car.getIdTerminal() != id_ter) {
                    if (id_ter == 0) {//primera iteración
                        terminal.setVariable("{id_ter}", car.getIdTerminal() + "");
                        terminal.setVariable("{nom_ter}", car.getNombreTerminal());
                        intermedia.setVariable("{id_inter}", car.getIdIntermedia() + "");
                        intermedia.setVariable("{nom_inter}", car.getNombreIntermedia());
                        productos.add(producto);
                        if (car.getIdIntermedia() != id_inter) {
                            if (id_inter == 0) {//primera iteración
                                intermedia.setVariable("{id_inter}", car.getIdIntermedia() + "");
                                intermedia.setVariable("{nom_inter}", car.getNombreIntermedia());
                                id_ter = car.getIdTerminal();
                                id_inter = car.getIdIntermedia();
                            } else {
                                auxinter = true;
                                intermedia.setDynamicValueSets("TERMINALES", terminales);
                                intermedias.add(intermedia);
                                intermedia = new ValueSet();
                                intermedia.setVariable("{id_inter}", car.getIdIntermedia() + "");
                                intermedia.setVariable("{nom_inter}", car.getNombreIntermedia());
                                terminales = new ArrayList();
                                productos = new ArrayList();
                                id_ter = car.getIdTerminal();
                                id_inter = car.getIdIntermedia();
                            }    
                        } else {
                            auxinter = false;
                            terminal.setDynamicValueSets("PRODUCTOS", productos);
                            terminales.add(terminal);
                            intermedia.setVariable("{id_inter}", car.getIdIntermedia() + "");
                            intermedia.setVariable("{nom_inter}", car.getNombreIntermedia());
                            id_ter = car.getIdTerminal();
                            id_inter = car.getIdIntermedia();
                        }
                    } else {
                        if (car.getIdIntermedia() != id_inter) {
                            terminal.setDynamicValueSets("PRODUCTOS", productos);
                            terminales.add(terminal);
                            intermedia.setDynamicValueSets("TERMINALES", terminales);
                            intermedias.add(intermedia);
                            terminal = new ValueSet();
                            terminal.setVariable("{id_ter}", car.getIdTerminal() + "");
                            terminal.setVariable("{nom_ter}", car.getNombreTerminal());
                            intermedia = new ValueSet();
                            intermedia.setVariable("{id_inter}", car.getIdIntermedia() + "");
                            intermedia.setVariable("{nom_inter}", car.getNombreIntermedia());
                            terminales = new ArrayList();
                            productos = new ArrayList();
                            productos.add(producto);
                            id_ter = car.getIdTerminal();
                            id_inter = car.getIdIntermedia();
                        } else {
                            terminal.setDynamicValueSets("PRODUCTOS", productos);
                            terminales.add(terminal);
                            terminal = new ValueSet();
                            terminal.setVariable("{id_ter}", car.getIdTerminal() + "");
                            terminal.setVariable("{nom_ter}", car.getNombreTerminal());
                            productos = new ArrayList();
                            productos.add(producto);
                            id_ter = car.getIdTerminal();
                            id_inter = car.getIdIntermedia();
                        }
                        id_ter = car.getIdTerminal();
                        id_inter = car.getIdIntermedia();
                    }
                } else {
                    productos.add(producto);
                    id_ter = car.getIdTerminal();
                    id_inter = car.getIdIntermedia();
                }
            }
            if (!auxinter){
                terminal.setDynamicValueSets("PRODUCTOS",productos);
                terminales.add(terminal);
                intermedia.setDynamicValueSets("TERMINALES", terminales);
                intermedias.add(intermedia);
            }
            if (listaCarro.size() > 0)
                top.setDynamicValueSets("INTERMEDIAS", intermedias);
    
            String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            throw new CommandException( e );
        }
    }
    
    /**
     * No indica ID de producto es un producto nuevo
     * @param idsProductos Lista de Id's de productos agregados al carro
     * @param pro_id Id de Producto del carro
     * @return 
     */
    private boolean esNuevoProducto(List idsProductos, String pro_id) {
        for (int i = 0; i < idsProductos.size(); i++) {
            if ( idsProductos.get(i).equals(pro_id) ) {
                return true;
            }
        }
        return false;
    }
    
    private void promociones(MiCarroDTO productoDTO, IValueSet producto) {
        //Revisar si existen promociones para el producto
        List lst_promo = productoDTO.getListaPromociones();
        if (lst_promo != null && lst_promo.size() > 0) {
           logger.debug("Existen Promociones:" + lst_promo.size());
           // Si sólo tiene una promoción se presenta el banner asociado.
           if (lst_promo.size() == 1) {
              PromocionDTO promocion = (PromocionDTO) lst_promo.get(0);
              List show_lst_promo = new ArrayList();
              IValueSet ivs_promo = new ValueSet();
              ivs_promo.setVariable("{promo_img}", promocion.getBanner());
              ivs_promo.setVariable("{promo_desc}", promocion.getDescr());
              show_lst_promo.add(ivs_promo);
              producto.setDynamicValueSets("CON_PROMOCION", show_lst_promo);
           } else if (lst_promo.size() > 1) { // Tiene más de una
              // promoción se presenta enlace con popup
              List show_lst_promo = new ArrayList();
              IValueSet ivs_promo = new ValueSet();
              ivs_promo.setVariable("{promo_img}", "");
              ivs_promo.setVariable("{pro_id}", productoDTO.getPro_id() + "");
              show_lst_promo.add(ivs_promo);
              producto.setDynamicValueSets("CON_N_PROMOCION", show_lst_promo);
           }
        }
     }

}