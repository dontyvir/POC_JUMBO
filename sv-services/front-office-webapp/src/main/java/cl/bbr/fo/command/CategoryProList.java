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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.MarcaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class CategoryProList extends Command {

    /**
     * Despliega productos
     * 
     * @param arg0
     *            Request recibido desde el navegador
     * @param arg1
     *            Response recibido desde el navegador
     * @throws Exception
     */
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {

        try {

            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");

            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            // Recupera pagina desde web.xml y se inicia parser
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);

            ITemplate tem = load.getTemplate();

            IValueSet top = new ValueSet();

            long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());

            long categoria_id = Long.parseLong(arg0.getParameter("idcategoria"));

            String marca = "";
            String orden = "";
            if (arg0.getParameter("mar_id") != null)
                marca = arg0.getParameter("mar_id").toString();
            if (arg0.getParameter("orden") != null)
                orden = arg0.getParameter("orden").toString();

            // Recuperar datos de la categoría
            CategoriaDTO cat = biz.getCategoria(categoria_id);
            CategoriaDTO cat_padre = null;
            if (cat.getId_padre() != 0) {
                cat_padre = biz.getCategoria(cat.getId_padre());
            }

            if (arg0.getParameter("idpadre") != null) {
                long categoria_padre_id = Long.parseLong(arg0.getParameter("idpadre"));
                top.setVariable("{idpadre}", arg0.getParameter("idpadre") + "");
                cat_padre = biz.getCategoria(categoria_padre_id);
            }

            if (cat_padre != null) {
                top.setVariable("{cat_nombre}", cat_padre.getNombre() + "/" + cat.getNombre() + "");
            } else
                top.setVariable("{cat_nombre}", cat.getNombre() + "");

            // Banner para la categoria
            if (cat.getBanner() != null && cat.getBanner().compareTo("") != 0)
                top.setVariable("{banner}", cat.getBanner() + "");
            else
                top.setVariable("{banner}", "");
            // totem para la categoria
            if (cat.getTotem() != null && cat.getTotem().compareTo("") != 0)
                top.setVariable("{totem}", cat.getTotem() + "");
            else
                top.setVariable("{totem}", "");

            top.setVariable("{categoria_id}", categoria_id + "");

            //if (arg0.getParameter("tip").compareTo("T") == 0) {
            // Marcas para las categoria
            List lista_marcas = new ArrayList();
            //obtiene las marcas de los productos para el filtro por marca
            List biz_marcas = biz.productosGetMarcas(categoria_id);
            for (int k = 0; k < biz_marcas.size(); k++) {
                IValueSet valueset_marcas = new ValueSet();
                MarcaDTO marcadto = (MarcaDTO) biz_marcas.get(k);
                int largo_pro = Integer.parseInt(rb.getString("search.largo.marca"));
                if (marcadto.getMar_nombre().length() < largo_pro)
                    largo_pro = marcadto.getMar_nombre().length();
                valueset_marcas.setVariable("{nombre}", marcadto.getMar_nombre().substring(0, largo_pro) + "");
                valueset_marcas.setVariable("{valor}", marcadto.getMar_id() + "");
                if (marca.compareTo(marcadto.getMar_id() + "") == 0)
                    valueset_marcas.setVariable("{seleccion}", "1");
                else
                    valueset_marcas.setVariable("{seleccion}", "0");
                lista_marcas.add(valueset_marcas);
            }
            top.setDynamicValueSets("FIL_MARCAS", lista_marcas);
            //}

            // Recuperar los cupones y los tcp de la sesión
            List l_torec = new ArrayList();
            List l_tcp = null;
            if (session.getAttribute("ses_promo_tcp") != null) {
                l_tcp = (List) session.getAttribute("ses_promo_tcp");
                l_torec.addAll(l_tcp);
            }
            if (session.getAttribute("ses_cupones") != null) {
                List l_cupones = (List) session.getAttribute("ses_cupones");
                l_torec.addAll(l_cupones);
            }

            //obtiene los productos de la catagoria seleccionada
            List list_prod = biz.productosGetProductos(session.getAttribute("ses_loc_id").toString(), categoria_id,
                    cliente_id, marca, orden, l_torec);

            List arr_productos = new ArrayList();
            long contador = 0;
            boolean isEn_carro = false;
            for (int i = 0; i < list_prod.size(); i++) {
                isEn_carro = false;
                contador = 0;
                ProductoDTO data = (ProductoDTO) list_prod.get(i);

                IValueSet fila = new ValueSet();
                fila.setVariable("{contador_form}", i + "");
                fila.setVariable("{contador}", contador + "");

                // Revisar si existen promociones para el producto
                if (data.getListaPromociones() != null && data.getListaPromociones().size() > 0) {
                    this.getLogger().debug("Existen Promociones:" + data.getListaPromociones().size());
                    // Si sólo tiene una promoción se presenta el banner
                    // asociado.
                    List lst_promo = data.getListaPromociones();
                    if (lst_promo.size() == 1) {
                        PromocionDTO promocion = (PromocionDTO) lst_promo.get(0);
                        List show_lst_promo = new ArrayList();
                        IValueSet ivs_promo = new ValueSet();
                        ivs_promo.setVariable("{promo_img}", promocion.getBanner());
                        ivs_promo.setVariable("{promo_desc}", promocion.getDescr());
                        show_lst_promo.add(ivs_promo);
                        fila.setDynamicValueSets("CON_PROMOCION", show_lst_promo);
                    } else if (lst_promo.size() > 1) { // Tiene más de una
                                                       // promoción se presenta
                                                       // enlace con popup
                        List show_lst_promo = new ArrayList();
                        IValueSet ivs_promo = new ValueSet();
                        ivs_promo.setVariable("{promo_img}", "");
                        ivs_promo.setVariable("{pro_id}", data.getPro_id() + "");
                        show_lst_promo.add(ivs_promo);
                        fila.setDynamicValueSets("CON_N_PROMOCION", show_lst_promo);
                    }
                }

                // Datos generales del producto
                fila.setVariable("{pro_imagen}", data.getImg_chica() + "");
                fila.setVariable("{pro_id}", data.getPro_id() + "");
                fila.setVariable("{pro_tipo}", data.getTipo_producto() + "");
                fila.setVariable("{pro_marca}", data.getMarca() + "");
                fila.setVariable("{pro_desc}", data.getDescripcion() + "");

                // Es producto genérico se buscan los item
                if (data.getGenerico() != null && data.getGenerico().compareTo("G") == 0) {
                    List data_items = data.getProductosDTO(); // Get item del
                                                              // producto padre
                    List lista_items = new ArrayList();
                    List lista_items_sel = new ArrayList();
                    if (data_items == null || data_items.size() <= 0) {
                        this.getLogger().debug(
                                "Producto genérico pero no tiene item asociados pro_id:" + data.getPro_id());
                        continue; // Se continua con el otro producto padre
                    }
                    for (int v = 0; v < data_items.size(); v++) {
                        ProductoDTO item = (ProductoDTO) data_items.get(v);
                        IValueSet valueset_items = new ValueSet();

                        if (item.getEsParticionable().equalsIgnoreCase("S")) {
                            valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(item.getPrecio()
                                    / item.getParticion())
                                    + "");
                            valueset_items.setVariable("{unidad}", " 1/" + item.getParticion() + " " + item.getTipre()
                                    + "");
                        } else {
                            valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(item.getPrecio()) + "");
                            valueset_items.setVariable("{unidad}", item.getTipre() + "");
                        }

                        valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(item.getPpum()) + "");
                        valueset_items.setVariable("{pro_uni_med_ppum}", item.getUnidad_nombre() + "");

                        lista_items.add(valueset_items);

                        IValueSet valueset_items_sel = new ValueSet();
                        // Si el producto es con seleccion
                        if (item.getUnidad_tipo().compareTo("S") == 0) {

                            IValueSet fila_lista_sel = new ValueSet();

                            List aux_lista = new ArrayList();
                            for (double h = 0; h <= item.getInter_maximo(); h += item.getInter_valor()) {
                                IValueSet aux_fila = new ValueSet();
                                aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(h) + "");
                                aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(h) + "");
                                if (Formatos.formatoIntervaloFO(h).compareTo(
                                        Formatos.formatoIntervaloFO(item.getCantidad())) == 0) {
                                    aux_fila.setVariable("{selected}", "selected");
                                } else
                                    aux_fila.setVariable("{selected}", "");
                                aux_lista.add(aux_fila);
                            }

                            fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
                            fila_lista_sel.setVariable("{contador}", contador + "");

                            List aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);

                            valueset_items_sel.setDynamicValueSets("LISTA_SEL", aux_blanco);
                        } else {
                            IValueSet fila_lista_sel = new ValueSet();
                            if (Formatos.formatoCantidadFO(item.getCantidad()) == 0)
                                fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(item.getInter_valor()
                                        - item.getInter_valor())
                                        + "");
                            else
                                fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(item.getCantidad())
                                        + "");

                            fila_lista_sel.setVariable("{contador}", contador + "");
                            fila_lista_sel.setVariable("{contador_form_aux}", i + "");
                            fila_lista_sel.setVariable("{maximo}", item.getInter_maximo() + "");
                            fila_lista_sel.setVariable("{intervalo}", item.getInter_valor() + "");
                            List aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);
                            valueset_items_sel.setDynamicValueSets("INPUT_SEL", aux_blanco);
                        }
                        valueset_items_sel.setVariable("{pro_id}", item.getPro_id() + "");
                        valueset_items_sel.setVariable("{unidad}", item.getTipre() + "");
                        valueset_items_sel.setVariable("{contador}", contador + "");
                        valueset_items_sel.setVariable("{dif}", item.getValor_diferenciador() + "");
                        lista_items_sel.add(valueset_items_sel);

                        if (item.isEn_carro() == true)
                            isEn_carro = true;

                        contador++;

                    } // for

                    fila.setDynamicValueSets("ITEMS", lista_items);
                    fila.setDynamicValueSets("ITEMS_SEL", lista_items_sel);

                } else { // Producto sin item

                    List lista_items = new ArrayList();
                    IValueSet valueset_items = new ValueSet();
                    if (data.getEsParticionable().equalsIgnoreCase("S")) {
                        valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(data.getPrecio()
                                / data.getParticion())
                                + "");
                        valueset_items
                                .setVariable("{unidad}", " 1/" + data.getParticion() + " " + data.getTipre() + "");
                    } else {
                        valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(data.getPrecio()) + "");
                        valueset_items.setVariable("{unidad}", data.getTipre() + "");
                    }

                    valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(data.getPpum()) + "");
                    valueset_items.setVariable("{pro_uni_med_ppum}", data.getUnidad_nombre() + "");

                    lista_items.add(valueset_items);
                    fila.setDynamicValueSets("SIN_ITEMS_UNI", lista_items);
                    fila.setDynamicValueSets("SIN_ITEMS", lista_items);

                    /** INI Selección de cantidad de productos * */
                    // Valueset para la lista de selección
                    IValueSet valueset_items_sel = new ValueSet();

                    // Si el producto es con seleccion
                    if (data.getUnidad_tipo().compareTo("S") == 0) {

                        IValueSet fila_lista_sel = new ValueSet();

                        List aux_lista = new ArrayList();
                        long cont = 0;
                        for (double v = 0; v <= data.getInter_maximo(); v += data.getInter_valor()) {
                            IValueSet aux_fila = new ValueSet();
                            aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(v) + "");
                            aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(v) + "");

                            if (Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(data.getCantidad())) == 0) {
                                aux_fila.setVariable("{selected}", "selected");
                            } else if (cont == 1 && (data.getCantidad() == 0.0))
                                aux_fila.setVariable("{selected}", "selected");
                            else
                                aux_fila.setVariable("{selected}", "");

                            aux_lista.add(aux_fila);
                            cont++;
                        }

                        fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
                        fila_lista_sel.setVariable("{contador}", contador + "");

                        List aux_blanco = new ArrayList();
                        aux_blanco.add(fila_lista_sel);

                        valueset_items_sel.setDynamicValueSets("LISTA_SEL", aux_blanco);
                    } else {
                        IValueSet fila_lista_sel = new ValueSet();
                        if (Formatos.formatoCantidadFO(data.getCantidad()) == 0)
                            fila_lista_sel
                                    .setVariable("{valor}", Formatos.formatoIntervaloFO(data.getInter_valor()) + "");
                        else
                            fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(data.getCantidad()) + "");

                        fila_lista_sel.setVariable("{contador}", contador + "");
                        fila_lista_sel.setVariable("{contador_form_aux}", i + "");
                        fila_lista_sel.setVariable("{maximo}", data.getInter_maximo() + "");
                        fila_lista_sel.setVariable("{intervalo}", data.getInter_valor() + "");

                        List aux_blanco = new ArrayList();
                        aux_blanco.add(fila_lista_sel);
                        valueset_items_sel.setDynamicValueSets("INPUT_SEL", aux_blanco);
                    }

                    List lista_items_sel = new ArrayList();
                    valueset_items_sel.setVariable("{pro_id}", data.getPro_id() + "");
                    valueset_items_sel.setVariable("{unidad}", data.getTipre() + "");
                    valueset_items_sel.setVariable("{contador}", contador + "");
                    valueset_items_sel.setVariable("{dif}", "");
                    lista_items_sel.add(valueset_items_sel);
                    fila.setDynamicValueSets("ITEMS_SEL", lista_items_sel);
                    /** FIN Selección de cantidad de productos * */

                    if (data.isEn_carro() == true)
                        isEn_carro = true;

                    contador++;

                }

                IValueSet lista_boton = new ValueSet();
                lista_boton.setVariable("{contador_form}", i + "");
                lista_boton.setVariable("{nota}", data.getNota() + "");

                List list_boton = new ArrayList();
                list_boton.add(lista_boton);
                if (data.tieneStock())
                    if (isEn_carro == true)
                        fila.setDynamicValueSets("IMG_MODIFICAR", list_boton);
                    else
                        fila.setDynamicValueSets("IMG_AGREGAR", list_boton);
                else {
                    List bloqueados = new ArrayList();
                    IValueSet bloq = new ValueSet();
                    bloq.setVariable("{bloqueado}","");
                    bloqueados.add(bloq);
                    fila.setDynamicValueSets("IMG_SINSTOCK", bloqueados);
                }    
                if (data.getNota() == null)
                    lista_boton.setVariable("{nota}", "");
                else
                    lista_boton.setVariable("{nota}", data.getNota() + "");

                if (data.isCon_nota() == true) {
                    lista_boton.setVariable("{pro_id}", data.getPro_id() + "");
                    fila.setDynamicValueSets("IMG_COMEN", list_boton);
                }

                fila.setVariable("{total_productos}", contador + "");

                arr_productos.add(fila);
            }
            top.setDynamicValueSets("PRODUCTOS", arr_productos);

            String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            throw new CommandException(e);
        }

    }

}