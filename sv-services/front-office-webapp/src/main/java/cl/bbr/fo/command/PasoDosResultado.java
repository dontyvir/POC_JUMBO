/*
 * Created on 19-ago-2008
 */
package cl.bbr.fo.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.commons.lang.StringEscapeUtils;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.busqueda.Buscar;
import cl.bbr.fo.util.busqueda.Destacados;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.DestacadoDTO;
import cl.bbr.jumbocl.productos.dto.MarcaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoCarroDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.log.Logging;
//13122012 VMatheu 
//-13122012 VMatheu 
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * @author jdroguett
 */
public class PasoDosResultado extends AjaxCommand {
    private static Logging logger = new Logging(PasoDosResultado.class);

    private BizDelegate biz;

    private Hashtable cantidadesCalculadas = new Hashtable();
//13122012 VMatheu 
    private static int PARAM_KEY_MAX_PRODUCTOS = 6; 
//-13122012 VMatheu 
    private static int MAX_LISTA = 150; //cantidad máxima de productos a
                                        // mostrar

    // en la lista

    public PasoDosResultado() {
        biz = new BizDelegate();
    }

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        ServletOutputStream out = arg1.getOutputStream();
        try {
            arg0.setCharacterEncoding("UTF-8");
            HttpSession session = arg0.getSession();
            

            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");

            IValueSet top = new ValueSet();

            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();

            //20120823 Andres Valle
            int idLocal =0;
            try {
                idLocal = Integer.parseInt((String) session.getAttribute("ses_loc_id"));
            } catch (NumberFormatException e) {
                logger.error("ses_loc_id no es numerico [" +  idLocal + "]");
                throw new Exception(e);
            }
            int idCliente =0;
            try {
                idCliente = Integer.parseInt((String) session.getAttribute("ses_cli_id"));
            } catch (NumberFormatException e) {
            	//+20121024avc
                logger.error("ses_cli_id no es numerico [" + idCliente + "]");
                //-20121024avc
                throw new Exception(e);
            }
            //-20120823 Andres Valle

            String idInvitado = "";

            if (idCliente == 1) {
                if (session.getAttribute("ses_invitado_id") != null) {
                    idInvitado = session.getAttribute("ses_invitado_id").toString();
                }
            }
            String accion = arg0.getParameter("accion");
            //////////////////////////////////////////////////////////
            String ordenarPor = arg0.getParameter("ordenar_por");

            if (ordenarPor == null)
                ordenarPor = "default";

            int idCabecera = 0;
            int idCategoria = 0;
            int idSubCategoria = 0;

            /*
             * caip y cai vienen con valor cuando se hace click a la
             * imagengrande del paso dos
             */
            String sIdCabecera = arg0.getParameter("cab");
            String sIdCategoria = arg0.getParameter("int");
            String sIdSubCategoria = arg0.getParameter("ter");

            if (sIdCabecera == null)
                sIdCabecera = arg0.getParameter("cab");
            if (sIdSubCategoria == null)
                sIdSubCategoria = arg0.getParameter("int");
            if (sIdCategoria == null)
                sIdCategoria = arg0.getParameter("ter");

            if (sIdCabecera != null && !sIdCabecera.equals("undefined"))
                idCabecera = Integer.parseInt(sIdCabecera);
            if (sIdCategoria != null && !sIdCategoria.equals("undefined"))
                idCategoria = Integer.parseInt(sIdCategoria);
            if (sIdSubCategoria != null && !sIdSubCategoria.equals("undefined"))
                idSubCategoria = Integer.parseInt(sIdSubCategoria);

            /*
             * Recuperar los cupones y los tcp de la sesión (copy paste de
             * versión anterior)
             */
            List lista_tcp = new ArrayList();
            List l_tcp = null;
            if (session.getAttribute("ses_promo_tcp") != null) {
                l_tcp = (List) session.getAttribute("ses_promo_tcp");
                lista_tcp.addAll(l_tcp);
            }
            if (session.getAttribute("ses_cupones") != null) {
                List l_cupones = (List) session.getAttribute("ses_cupones");
                lista_tcp.addAll(l_cupones);
            }
            ///////////////////////////////////
            List listaProductos = null;
            MarcaDTO[] listaMarcas = null;
            String palabra = "";
            String[] palabras = null;
            String[] sugPorMax = null;

            if ("buscar".equals(accion)) {
                //////////// INICIO BUSCAR //////////////////////
                palabra = arg0.getParameter("buscar").trim();
                String sugerencias = arg0.getParameter("sugerencias");
                logger.debug("palabra a buscar: " + palabra);
                //List idsProductos = Buscar.search(palabra, idLocal);
                //listaProductos = biz.productosPorIds(idCliente, idsProductos,
                // idLocal);

                listaProductos = Buscar.productos(palabra, idLocal);

                if (sugerencias != null && !sugerencias.equals(""))
                    palabras = DString.decode(sugerencias);

                if (listaProductos.size() == 0) {
                    palabras = Buscar.ortografia(palabra);
                } else if (listaProductos.size() > MAX_LISTA) {
                    sugPorMax = Buscar.sugerencias(listaProductos);
                }
                //////////// FIN BUSCAR /////////////////////////
            } else { //mostrar

                long start=0L;
                long end=0L; // EAA

                start = System.currentTimeMillis(); //EAA

                listaProductos = biz.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idCategoria, idInvitado);

                end = System.currentTimeMillis(); // EAA

                logger.debug(" biz.productosPorSubCategoria (" + (end - start) + ") millisegundos"); // EAA

            }
            if (listaProductos != null && listaProductos.size() > 0) {

                /*
                 * EAA
                 */

            	 long start=0L;
                 long end=0L; // EAA

                start = System.currentTimeMillis();

                /*
                 * END EAA
                 */

                listaProductos = biz.cargarPromociones(listaProductos, lista_tcp, idLocal);

                /*
                 * EAA
                 */
                end = System.currentTimeMillis();

                logger.debug(" biz.cargarPromociones (" + (end - start) + ") millisegundos"); // EAA

                /*
                 * END EAA
                 */

                listaMarcas = marcasDeProductos(listaProductos);

            }

            /*
             * EAA
             */
            long start, end;
            start = System.currentTimeMillis();
            /*
             * END
             */

            Hashtable productosIdCarro = biz.productosCarro(idCliente, idInvitado);

            /*
             * EAA
             */
            end = System.currentTimeMillis();
            logger.debug(" biz.productosCarro (" + (end - start) + ") millisegundos");
            /*
             * END
             */
            
            String nombreCabecera = "";
            String nombreCategoria = "Todas";
            String nombreSubCategoria = "Mis Preferidos";
            String banner = null;
            String url_banner = null;

            //Mauricio Farias 23-10-2012
            if (idSubCategoria > 0) {
                CategoriaDTO subcat = biz.getCategoria(idSubCategoria);
                nombreSubCategoria = subcat.getNombre();
                banner = subcat.getBanner();
                url_banner = subcat.getUrl_banner();
                if (idCategoria == 0)
                    idCategoria = (int) subcat.getId_padre();
            }
            if (idCategoria != 0) {
                CategoriaDTO cat = biz.getCategoria(idCategoria);
                nombreCategoria = cat.getNombre();
                if(idSubCategoria < 0){
                    banner = cat.getBanner();
                    url_banner = cat.getUrl_banner();
                }
            }
            if (idCabecera != 0) {
                CategoriaDTO cab = biz.getCategoria(idCabecera);
                nombreCabecera = cab.getNombre();
            }
               
            if(banner != null)
            	banner = (banner.trim().length()==0) ? null:banner.trim();
            
            List productos = productos(listaProductos, productosIdCarro, idLocal, idCliente, rb, nombreCabecera, nombreCategoria, nombreSubCategoria); 
            List sugerencias = sugerencias(palabras);
            List sugerenciasPorMax = sugerenciasPorMax(sugPorMax);
            List marcas = marcas(listaMarcas);

            IValueSet resultado = new ValueSet();
                              
            //Banner por defecto
            if(banner == null){                 
            	resultado.setVariable("{activa_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"activa_banner_dafault"));
            	resultado.setVariable("{nombre_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"nombre_banner_dafault"));
                resultado.setVariable("{url_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"url_banner_dafault"));
                resultado.setVariable("{title_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"title_banner_dafault"));
                resultado.setVariable("{target_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"target_banner_dafault"));
                resultado.setVariable("{mxTracker_banner_dafault}", getDataBannerDefault(rb.getString("path.txt.banner.defaul"),"mxTracker_banner_dafault"));
            }
            

            resultado.setVariable("{texto}", palabra);
            resultado.setDynamicValueSets("PRODUCTOS", productos);
            resultado.setDynamicValueSets("SUGERENCIAS", sugerencias);
            resultado.setDynamicValueSets("SUGPORMAX", sugerenciasPorMax);
            resultado.setDynamicValueSets("MARCAS", marcas);
            resultado.setVariable("{total_encontrados}", "" + (listaProductos == null ? 0 : listaProductos.size()));

            resultado.setVariable("{cabecera_nombre}", Formatos.sanitizeAccentsFO(nombreCabecera));
            resultado.setVariable("{categoria_nombre}", Formatos.sanitizeAccentsFO(nombreCategoria));
            resultado.setVariable("{subcategoria_nombre}", Formatos.sanitizeAccentsFO(nombreSubCategoria));
            resultado.setVariable("{banner}", (banner == null ? "" : banner));
            resultado.setVariable("{url_banner}", (url_banner == null ? "" : url_banner));
            resultado.setVariable("{ultima_accion}", accion);
          // 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 
         	resultado.setVariable("{idcategoria}", ""+idCategoria);
         	resultado.setVariable("{idSubCategoria}", ""+idSubCategoria);
         // 05/10/2012 : FIN COREMETRICS                   
            
            //////////////////////////////////////////////////////////
            resultado.setDynamicValueSets("ORDENAR_POR", selectOrdenarPor());
            //////////////////////////////////////////////////////////
            List elementos = new ArrayList();
            elementos.add(resultado);
            top.setDynamicValueSets("RESULTADO", elementos);

            //////////////////////////////////////////////////////////
            //Ventana Info DESPACHOS DISPONIBLES 
            if("1".equals(rb.getString("ventana_info_disponibilidad_despacho"))){
	            if (session.getAttribute("ses_cli_id") != null && !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
	                if(session.getAttribute("show_calendario_despacho_Category").toString().equals("P")){		            	
	      	            session.setAttribute("show_calendario_despacho_Category","N");
	      	            top.setVariable("{display_calendario_despacho}", "block");	
	      	       }else{
	      	    	 top.setVariable("{display_calendario_despacho}", "none");	          	  
	      	       }	            
	            }else{            	
	            	top.setVariable("{display_calendario_despacho}", "none");
	            }
            }else{
            	top.setVariable("{display_calendario_despacho}", "none");
            }
          //Fin Ventana Info DESPACHOS DISPONIBLES   

            /*
             * EAA
             */

            start = System.currentTimeMillis();

            /*
             * END EAA
             */
            top.setVariable("{mx_content2}", "/"+Formatos.sanitizeAccentsFO(nombreCabecera)+"/"+Formatos.sanitizeAccentsFO(nombreCategoria)+"/"+Formatos.sanitizeAccentsFO(nombreSubCategoria)+"/Pagina1");

            String result = tem.toString(top);
            arg1.setContentLength(result.length());

            out.print(result);
            end = System.currentTimeMillis();

            logger.debug(" write.response (" + (end - start) + ") millisegundos");

        } catch (Exception e) {
            //20120823 Andres Valle
            if (e.getMessage() == null)
                logger.error("Error en execute: " + e.getClass().getName() + " en PasoDosResultado");
            else
                logger.error("Error en execute: " + e.getMessage() + " en PasoDosResultado");
            //-20120823 Andres Valle

            //e.printStackTrace();
            logger.error(e);

        } finally {
            out.flush();
        }

    }
    
   
    /**
     * Entrega las marcas que tienen los productos de las lista de productos
     * 
     * @param listaProductos
     * @return
     */
    private MarcaDTO[] marcasDeProductos(List listaProductos) {
        TreeSet set = new TreeSet();

        for (int i = 0; i < listaProductos.size(); i++) {
            ProductoDTO productoDTO = (ProductoDTO) listaProductos.get(i);
            MarcaDTO marcaDTO = new MarcaDTO();
            marcaDTO.setMar_id(productoDTO.getMarca_id());
            marcaDTO.setMar_nombre(productoDTO.getMarca());
            set.add(marcaDTO);
        }
        return (MarcaDTO[]) set.toArray(new MarcaDTO[set.size()]);
    }

    /**
     * Crea la vista de las sugerencias
     * 
     * @param palabras
     * @return
     */
    private List sugerencias(String[] palabras) {
        List sugerencias = new ArrayList();
        if (palabras != null && palabras.length > 0) {
            List lista = new ArrayList();
            for (int i = 0; i < palabras.length; i++) {
                IValueSet palabra = new ValueSet();
                palabra.setVariable("{palabra}", palabras[i]);
                lista.add(palabra);
            }
            IValueSet sug = new ValueSet();
            sug.setDynamicValueSets("PALABRAS", lista);
            sug.setVariable("{sugerencias}", DString.encode(palabras));
            sugerencias.add(sug);
        }
        return sugerencias;
    }

    private List sugerenciasPorMax(String[] palabras) {
        List sugerencias = new ArrayList();
        List lista = new ArrayList();
        if (palabras != null && palabras.length > 0) {
            for (int i = 0; i < palabras.length; i++) {
                IValueSet palabra = new ValueSet();
                palabra.setVariable("{palabra}", palabras[i]);
                lista.add(palabra);
            }
            IValueSet sug = new ValueSet();
            sug.setDynamicValueSets("PALABRAS", lista);
            sugerencias.add(sug);
        }
        return sugerencias;
    }

    /**
     * Crea la vista de las marca a mostrar en la página
     * 
     * @param idMarca
     * @param listaProductos
     * @return
     */
    private List marcas(MarcaDTO[] listaMarcas) {
        List lista = new ArrayList();
        if (listaMarcas != null) {
            for (int i = 0; i < listaMarcas.length; i++) {
                IValueSet marca = new ValueSet();
                MarcaDTO marcaDTO = (MarcaDTO) listaMarcas[i];
                //20121029avc
                marca.setVariable("{id}", String.valueOf(marcaDTO.getMar_id()));
                marca.setVariable("{value}", StringEscapeUtils.escapeHtml(marcaDTO.getMar_nombre()));
                //20121029avc
                marca.setVariable("{selected}", "");
                lista.add(marca);
            }
            IValueSet marca = new ValueSet();
            marca.setVariable("{id}", "0");
            marca.setVariable("{value}", "Todas");
            marca.setVariable("{selected}", "selected");
            lista.add(0, marca);
        }
        return lista;
    }

    /**
     * Crea vista de los productos a mostrar en la página
     * 
     * @param productosIdCarro
     * @param clienteId
     * @param list
     * @return
     */
    public List productos(List lista, Hashtable productosIdCarro, int localId, int idCliente, ResourceBundle rb, String nombreCabecera, String nombreCategoria, String nombreSubCategoria) {
        List listaProductos = new ArrayList();
//13122012 VMatheu        
        int maxProductos = 200;
        try {
           logger.debug("buscar parametro de maximo de productos"); 
           ParametroFoDTO par = biz.getParametroByID(PARAM_KEY_MAX_PRODUCTOS);
           logger.debug("parametro maximo cargado en: "+par.getValor());
           		maxProductos = Integer.valueOf(par.getValor()).intValue();
           
        } catch (Exception e) {
            logger.error("Error en buscar parametro maximo de carga de productos categorias en PasoDosResultado, Class Exception:" 
                    +e.getClass()+", Mensaje Exception:"+e.getMessage(),e);
        }  
        if (lista != null) {
            int largo_desc = Integer.parseInt(rb.getString("orderitemdisplay.largodescripcionproducto"));
            for (int i = 0; i < lista.size() && i < maxProductos; i++) {
//-13122012 VMatheu 
                ProductoDTO productoDTO = (ProductoDTO) lista.get(i);
                ProductoCarroDTO productoCarroDTO = (ProductoCarroDTO) productosIdCarro.get(new Integer((int) productoDTO.getPro_id()));
                IValueSet producto = new ValueSet();
                producto.setVariable("{id}", "" + productoDTO.getPro_id());
                producto.setVariable("{tipo}", StringEscapeUtils.escapeHtml(productoDTO.getTipo_producto()));
                producto.setVariable("{marca}", StringEscapeUtils.escapeHtml(productoDTO.getMarca()));
                //+20120411coh trunca a 54 la descripcion
                //            producto.setVariable("{descripcion}",
                // productoDTO.getDescripcion());
                String descripcion = productoDTO.getDescripcion().length() > largo_desc ? (productoDTO.getDescripcion().substring(0,
                        largo_desc) + "...") : productoDTO.getDescripcion();
                producto.setVariable("{descripcion}", StringEscapeUtils.escapeHtml(descripcion));
                //+20120411coh
                String particion = "";
                double precio = productoDTO.getPrecio();
                if ("S".equals(productoDTO.getEsParticionable()) && productoDTO.getParticion() != 0) {
                    particion = "1/" + productoDTO.getParticion() + " ";
                    precio /= productoDTO.getParticion();
                }
                producto.setVariable("{precio}", Formatos.formatoPrecioFO(precio) + " " + particion + Formatos.formatoUnidadFO(productoDTO.getTipre()));
                producto.setVariable("{precio_por_unidad}", Formatos.formatoPrecioFO(productoDTO.getPpum()) + " x " + productoDTO.getUnidad_nombre());
                ///unidad, cantidad, mínima y máxima no se utilizan en la
                // sección
                // mobi, sólo se usan en la sección normal
                producto.setVariable("{cantidad_minima}", productoDTO.getCantidadMinima() + "");
                producto.setVariable("{cantidad_maxima}", productoDTO.getCantidadMaxima() + "");
                producto.setVariable("{unidad}", Formatos.formatoUnidadFO(productoDTO.getTipre()));
                producto.setVariable("{imagen_chica}", "" + productoDTO.getImg_chica());

                if (productoCarroDTO != null)
                    producto.setVariable("{cantidad}", Formatos.cantidadFO(productoCarroDTO.getCantidad()));
                else
                    producto.setVariable("{cantidad}", Formatos.cantidadFO(productoDTO.getCantidadMinima()));
                //////////////////////////////////////
                promociones(productoDTO, producto);
                destacador(productoDTO, producto, localId);
                List elementos = new ArrayList();
                IValueSet value = new ValueSet();
                value.setVariable("{productoId}", "" + productoDTO.getPro_id());
                value.setVariable("{id}", "" + productoDTO.getPro_id());
                value.setVariable("{cantidad_minima}", productoDTO.getCantidadMinima() + "");
                value.setVariable("{nota}", (productoCarroDTO == null || productoCarroDTO.getNota() == null ? "" : StringEscapeUtils.escapeHtml(productoDTO.getNota())));
                elementos.add(value);
                if (productoDTO.tieneStock()) {
                    if (productoCarroDTO != null) {
                        producto.setDynamicValueSets("IMG_MODIFICAR", elementos);
                    } else {
                        producto.setDynamicValueSets("IMG_AGREGAR", elementos);
                    }
                    if (productoDTO.isCon_nota()) {
                        producto.setDynamicValueSets("IMG_CON_NOTA", elementos);
                    }
                } else {
                    List bloqueados = new ArrayList();
                    IValueSet bloq = new ValueSet();
                    bloq.setVariable("{bloqueado}", "");
                    bloqueados.add(bloq);
                    producto.setDynamicValueSets("IMG_SINSTOCK", bloqueados);
                }
                if (idCliente != 1) {
                    List viewDesLogin = new ArrayList();
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{id}", "" + productoDTO.getPro_id());
                    viewDesLogin.add(fila);
                    producto.setDynamicValueSets("VIEW_SUSTITUCION", viewDesLogin);
                }
                
              //fichaTAnalytics               
                producto.setVariable("{cabecera_nombre}", Formatos.sanitizeAccentsFO(nombreCabecera));
                producto.setVariable("{cabecera_nombreTA}", Formatos.sanitizeEneFO(Formatos.sanitizeAccentsFO(nombreCabecera)));
                producto.setVariable("{categoria_nombre}", Formatos.sanitizeAccentsFO(nombreCategoria));
                producto.setVariable("{categoria_nombreTA}", Formatos.sanitizeEneFO(Formatos.sanitizeAccentsFO(nombreCategoria)));
                producto.setVariable("{subcategoria_nombre}", Formatos.sanitizeAccentsFO(nombreSubCategoria));
                producto.setVariable("{subcategoria_nombreTA}", Formatos.sanitizeEneFO(Formatos.sanitizeAccentsFO(nombreSubCategoria)));
                producto.setVariable("{mx_content}", "/"+nombreCabecera+"/"+Formatos.sanitizeAccentsFO(nombreCategoria)+"/"+Formatos.sanitizeAccentsFO(nombreSubCategoria)+"/Pagina1");
                listaProductos.add(producto);
            }
        }
        return listaProductos;
    }

    private void promociones(ProductoDTO productoDTO, IValueSet producto) {
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
                ivs_promo.setVariable("{promo_color_banner}", promocion.getColorBann());
                show_lst_promo.add(ivs_promo);
                producto.setDynamicValueSets("CON_PROMOCION", show_lst_promo);
            } else if (lst_promo.size() > 1) { // Tiene mas de una promo
            	
            	boolean isPromo900 = false; //para las promo de seccion
            	String banner ="";
            	String descrip ="";
            	String colorBann ="";
            	Iterator it = lst_promo.iterator();
            	while(it.hasNext()){
            		PromocionDTO promocion = (PromocionDTO) it.next();
            		if(promocion.getTipo_promo() >= 900 && promocion.getTipo_promo() <= 914){
            			isPromo900=true;
            			descrip+=" / "+promocion.getDescr();
            		}else{
            			banner=promocion.getBanner();
            			descrip+=promocion.getDescr();
            			colorBann=" "+promocion.getColorBann();
            		}            		
            	}
            	if(isPromo900){
            		List show_lst_promo = new ArrayList();
                    IValueSet ivs_promo = new ValueSet();
                    ivs_promo.setVariable("{promo_img}",banner);
                    ivs_promo.setVariable("{promo_desc}", descrip);
                    ivs_promo.setVariable("{promo_color_banner}", colorBann);
                    show_lst_promo.add(ivs_promo);
                    producto.setDynamicValueSets("CON_PROMOCION", show_lst_promo);
            	}else{
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

    /**
     * @param productoDTO
     * @param prodDestacados
     * @param producto
     */
    private void destacador(ProductoDTO productoDTO, IValueSet producto, int localId) {
        Destacados des = Destacados.getInstance();
        Hashtable prodDestacados = des.getProductos(localId);
        if (prodDestacados == null)
            return;
        if (productoDTO.getListaPromociones() != null && productoDTO.getListaPromociones().size() > 0)
            return;
        DestacadoDTO destacadoDTO = (DestacadoDTO) prodDestacados.get(new Integer((new Long(productoDTO.getPro_id())).intValue()));
        if (destacadoDTO == null)
            return;
        List lista = new ArrayList();
        IValueSet dato = new ValueSet();
        dato.setVariable("{destacado_imagen}", destacadoDTO.getImagen());
        dato.setVariable("{destacado_descripcion}", destacadoDTO.getDescripcion());
        lista.add(dato);
        producto.setDynamicValueSets("DESTACADO", lista);
    }

    /**
     * Para crear el select de OrdernarPor
     * 
     * @param selected
     * @return
     */
    private List selectOrdenarPor() {
        String[] ids = { "default", "Descripcion", "Precio", "precio_por_unidad" };
        String[] values = { "Selecciona", "Nombre", "Precio", "Precio por unidad" };
        List lista = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            IValueSet ordenarPor = new ValueSet();
            ordenarPor.setVariable("{id}", ids[i]);
            ordenarPor.setVariable("{value}", values[i]);
            lista.add(ordenarPor);
        }
        return lista;
    }
    
    private String getDataBannerDefault(String path_archivo_banner,String param_solic){
    	File f = new File(path_archivo_banner);
    	BufferedReader entrada;
    	String valor_param="";
    	try {
    		entrada = new BufferedReader( new FileReader( f ) );
    		String linea;    		
    		while(entrada.ready()){
	    		linea = entrada.readLine();
	    		
	    		if(linea.startsWith("#"))
	    			continue;
	    			    		
	    		if(linea.startsWith(param_solic)){
	    			String [] aux_linea=linea.split("=",2);
	    			valor_param=(aux_linea.length > 1 && aux_linea[1]!= null)?aux_linea[1].toString():"";
	    			break;
	    		}
    		}
    	}catch (IOException e) {
    		logger.error(e);
    	}
		return valor_param;
    	
    }
}