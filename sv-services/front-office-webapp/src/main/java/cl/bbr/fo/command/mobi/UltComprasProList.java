package cl.bbr.fo.command.mobi;

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
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasProductosDTO;
import cl.bbr.jumbocl.common.utils.Formatos;

/**
 * Despliega Productos de las ultimas compras seleccionadas para mobile
 * 
 * @author imoyano
 *  
 */
public class UltComprasProList extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        try {

            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Recupera pagina desde web.xml
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug("Template:" + pag_form);
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();

            IValueSet top = new ValueSet();

            int contador = 0;

            if (arg0.getParameter("tupla") != null) {

                // Instacia del bizdelegate
                BizDelegate biz = new BizDelegate();

                String tupla = arg0.getParameter("tupla");
                this.getLogger().debug("Listas de compras:" + tupla);
                
                // Recupera la sesi�n del usuario
                HttpSession session = arg0.getSession();

                List fm_cate = new ArrayList();
                List datos = biz.clientesGetUltComprasCategoriasProductos(tupla, session.getAttribute("ses_loc_id").toString(), Long.parseLong(session.getAttribute("ses_cli_id").toString()), Long.parseLong(session.getAttribute("ses_cli_rut").toString()));

                for (int j = 0; j < datos.size(); j++) {
                    UltimasComprasCategoriasDTO cat = (UltimasComprasCategoriasDTO) datos.get(j);

                    IValueSet fila_cat = new ValueSet();
                    fila_cat.setVariable("{categoria}", cat.getCategoria());

                    List prod = cat.getUltimasComprasProductosDTO();

                    List fm_prod = new ArrayList();
                    this.getLogger().debug("Categoria:" + cat.getCategoria() + "(" + cat.getId() + ") productos:" + prod.size());
                    for (int i = 0; i < prod.size(); i++) {
                        UltimasComprasProductosDTO producto = (UltimasComprasProductosDTO) prod.get(i);
                        this.getLogger().debug( "Producto:" + producto.getPro_id() + " Stock:" + producto.getStock());
                        IValueSet fila_pro = new ValueSet();
                        fila_pro.setVariable("{contador}", contador + "");
                        fila_pro.setVariable("{descripcion}", producto.getNombre() + "");
                        fila_pro.setVariable("{marca}", producto.getMarca() + "");
                        fila_pro.setVariable("{codigo}", producto.getCodigo() + "");
                        fila_pro.setVariable("{pro_id}", producto.getPro_id() + "");
                        if (producto.getEsParticionable().equalsIgnoreCase("S")) {
                            fila_pro.setVariable("{precio}", Formatos.formatoPrecioFO(producto.getPrecio() / producto.getParticion()) + " 1/" + producto.getParticion() + " " + producto.getTipre());
                        } else {
                            fila_pro.setVariable("{precio}", Formatos.formatoPrecioFO(producto.getPrecio()) + " " + producto.getTipre());
                        }
                        //cambia la cantidad de manera que sea acorde con el intervalo al momento del despliegue
                        double mod = producto.getCantidad() % producto.getInter_valor();
                        if (mod > 0) {
                            double div = producto.getCantidad() - mod + producto.getInter_valor();
                            producto.setCantidad(div);
                        } else {
                            producto.setCantidad(producto.getCantidad());
                        }
                        // Existe STOCK para el producto
                        if (producto.getStock() != 0) {
                            if (producto.isCon_nota() == true && producto.isEn_carro() == false) {
                                IValueSet aux_fila = new ValueSet();
                                List aux_lista = new ArrayList();
                                aux_fila.setVariable("{idprod}", producto.getPro_id() + "");
                                aux_fila.setVariable("{contador}", contador + "");
                                if (producto.getNota() != null)
                                    aux_fila.setVariable("{nota}", producto.getNota());
                                else
                                    aux_fila.setVariable("{nota}", "");

                                aux_lista.add(aux_fila);
                                fila_pro.setDynamicValueSets("COMENTARIO", aux_lista);
                            }
                            if (producto.isCon_nota() == true && producto.isEn_carro() == true) {
                                IValueSet aux_fila = new ValueSet();
                                aux_fila.setVariable("{idprod}", producto.getPro_id() + "");
                                aux_fila.setVariable("{contador}", contador + "");
                                if (producto.getNota() != null)
                                    aux_fila.setVariable("{nota}", producto.getNota());
                                else
                                    aux_fila.setVariable("{nota}", "");
                                List aux_lista = new ArrayList();
                                aux_lista.add(aux_fila);
                                fila_pro.setDynamicValueSets("COMENTARIO_NODISP", aux_lista);
                            }
                            if (!producto.isEn_carro()) {

                                producto.setUnidad_tipo("S"); //seteo para mobiles
                                // Si el producto es con seleccion
                                if (producto.getUnidad_tipo().charAt(0) == 'S') {

                                    IValueSet fila_lista_sel = new ValueSet();

                                    List aux_lista = new ArrayList();
                                    for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
                                        IValueSet aux_fila = new ValueSet();
                                        aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(v) + "");
                                        aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(v) + "");
                                        if (Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(producto.getCantidad())) == 0)
                                            aux_fila.setVariable("{selected}","selected");
                                        else
                                            aux_fila.setVariable("{selected}", "");
                                        aux_lista.add(aux_fila);
                                    }

                                    fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
                                    fila_lista_sel.setVariable("{contador}",contador + "");

                                    List aux_blanco = new ArrayList();
                                    aux_blanco.add(fila_lista_sel);

                                    fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);
                                } else {
                                    IValueSet fila_lista_sel = new ValueSet();

                                    fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad()) + "");
                                    fila_lista_sel.setVariable("{contador}", contador + "");
                                    fila_lista_sel.setVariable("{maximo}", producto.getInter_maximo() + "");
                                    fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor() + "");
                                    List aux_blanco = new ArrayList();
                                    aux_blanco.add(fila_lista_sel);
                                    fila_pro.setDynamicValueSets("INPUT_SEL", aux_blanco);
                                }

                                IValueSet aux_fila = new ValueSet();
                                aux_fila.setVariable("{contador}", contador + "");
                                aux_fila.setVariable("{id_prod}", producto.getPro_id() + "");

//                                int flag = 0;
//                                if (laux != null) {
//                                    for (int z = 0; z < laux.length; z++) {
//                                        if (flag == 0) {
//                                            if (laux[z].compareTo(producto.getPro_id() + "") == 0) {
//                                                aux_fila.setVariable("{checked}", "checked");
//                                                flag = 1;
//                                            } else {
//                                                aux_fila.setVariable("{checked}", "");
//                                            }
//                                        }
//                                    }
//                                } else {
                                    aux_fila.setVariable("{checked}", "checked");
//                                }

                                List aux_lista = new ArrayList();
                                aux_lista.add(aux_fila);
                                fila_pro.setVariable("{valor}", "");
                                fila_pro.setVariable("{unidad}", producto.getTipre()+ "");
                                fila_pro.setDynamicValueSets("CHECKBOX", aux_lista);
                                contador++;
                            } else {

                                IValueSet aux_fila = new ValueSet();
                                fila_pro.setVariable("{unidad}", producto.getTipre() + "");
                                fila_pro.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad()) + "");
                                List aux_lista = new ArrayList();
                                aux_lista.add(aux_fila);
                                fila_pro.setDynamicValueSets("IMAGEN", aux_lista);
                                contador++;
                            }
                            fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");
                            fila_pro.setVariable("{CLASE_CELDA}", "celda1");
                            fila_pro.setVariable("{NO_DISPONIBLE}", "");
                        } else { // no existe STOCK para el product
                            fila_pro.setVariable("{CLASE_TABLA}", "TablaNoDiponiblePaso1");
                            fila_pro.setVariable("{CLASE_CELDA}", "celda1NoDisponible");
                            fila_pro.setVariable("{NO_DISPONIBLE}", "(No disponible)");
                            fila_pro.setVariable("{unidad}", producto.getTipre() + "");
                            fila_pro.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad()) + "");

                            IValueSet aux_fila = new ValueSet();
                            List aux_lista = new ArrayList();
                            aux_lista.add(aux_fila);
                            fila_pro.setDynamicValueSets("IMAGEN_NODISP", aux_lista);

                            fila_pro.setDynamicValueSets("COMENTARIO_NODISP", aux_lista);
                        }
                        fm_prod.add(fila_pro);
                    }
                    fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
                    fm_cate.add(fila_cat);
                }
                top.setDynamicValueSets("CATEGORIAS", fm_cate);
            }

            top.setVariable("{max_productos}", contador + "");
            String result = tem.toString(top);
            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
            throw new CommandException(e);
        }
    }
}